package com.lucho.repository;

import java.util.List;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.DatabaseRetrievalMethod;
import org.hibernate.search.query.ObjectLookupMethod;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import com.lucho.domain.QTweet;
import com.lucho.domain.QUser;
import com.lucho.domain.Tweet;
import com.lucho.domain.User;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Default {@link TweetRepository} implementation.
 * @author Luciano.Leggieri
 */
@Transactional(readOnly = true)
public class TweetRepositoryImpl extends QueryDslRepositorySupport implements
        TweetRepositoryCustom {

    /**
     * Maximum number of tweets to return in a search.
     */
    private static final int MAX_RESULTS = 20;

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<Tweet> getTweetsForUserIncludingFollows(final User user,
            final Long millis) {
        QTweet qtweet = QTweet.tweet1;
        QUser followedBy = new QUser("followedBy");
        BooleanExpression whereClause = followedBy.id.eq(user.getId());
        if (millis != null) {
            DateTime dateTime = new DateTime(millis.longValue());
            whereClause = whereClause.and(qtweet.creationDate.after(dateTime));
        }
        return this.from(qtweet).join(qtweet.owner.followedBy, followedBy)
                .where(whereClause).orderBy(qtweet.creationDate.desc())
                .limit(MAX_RESULTS).list(qtweet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void reindex() {
        FullTextEntityManager fullTextEntityManager = Search
                .getFullTextEntityManager(this.getEntityManager());
        for (Tweet tweet : this.from(QTweet.tweet1).list(QTweet.tweet1)) {
            fullTextEntityManager.index(tweet);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<Tweet> searchTweets(final String textToSearch,
            final User user) {
        FullTextEntityManager fullTextEntityManager = Search
                .getFullTextEntityManager(this.getEntityManager());

        // create native Lucene query using the query DSL
        // alternatively you can write the
        // Lucene query using the Lucene query parser
        // or the Lucene programmatic API.
        // The Hibernate Search DSL is recommended though.
        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Tweet.class).get();
        Query lQuery = qb.keyword().onField("tweet").matching(textToSearch)
                .createQuery();

        // wrap Lucene query in a org.hibernate.Query
        FullTextQuery query = fullTextEntityManager.createFullTextQuery(lQuery,
                Tweet.class);
        query.initializeObjectsWith(
                // first looks in 2nd level cache, we take advantage of it.
                ObjectLookupMethod.SECOND_LEVEL_CACHE,
                DatabaseRetrievalMethod.QUERY);
        query.setMaxResults(MAX_RESULTS);
        List<Tweet> tweetList = query.getResultList();
        for (Tweet tweet : tweetList) {
            boolean followed = tweet.getOwner().getFollowedBy().contains(user);
            tweet.getOwner().setCanFollow(!followed);
        }
        return tweetList;
    }

}
