package com.lucho.repository;

import com.lucho.domain.QTweet;
import com.lucho.domain.QUser;
import com.lucho.domain.Tweet;
import com.lucho.domain.User;
import com.mysema.query.types.OrderSpecifier;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;

import javax.persistence.Query;
import java.util.List;

/**
 * Default {@link TweetRepository} implementation.
 * @author Luciano.Leggieri
 */
public class TweetRepositoryImpl extends QueryDslRepositorySupport
        implements TweetRepositoryCustom {

    private static final int MAX_RESULTS = 20;

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<Tweet> getTweetsForUserIncludingFollows(final User user) {
        QTweet qtweet = QTweet.tweet1;
        QUser followedBy = new QUser("followedBy");
        return this.from(qtweet).join(qtweet.owner.followedBy, followedBy)
                .where(followedBy.id.eq(user.getId()))
                .orderBy(qtweet.creationDate.desc())
                .limit(MAX_RESULTS)
                .list(qtweet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Tweet newTweet(final User user, final String text,
                          final String language) {
        Tweet tweet = new Tweet(user, text, language);
        this.getEntityManager().persist(tweet);
        return tweet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Tweet> searchTweets(final String textToSearch) {
        FullTextEntityManager fullTextEntityManager =
                Search.getFullTextEntityManager(this.getEntityManager());

        // create native Lucene query using the query DSL
        // alternatively you can write the Lucene query using the Lucene query parser
        // or the Lucene programmatic API. The Hibernate Search DSL is recommended though
        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Tweet.class).get();
        org.apache.lucene.search.Query lQuery = qb.keyword()
                .onFields("tweet").matching(textToSearch).createQuery();

        // wrap Lucene query in a org.hibernate.Query
        Query query = fullTextEntityManager.createFullTextQuery(lQuery, Tweet.class);
        query.setMaxResults(MAX_RESULTS);
        return query.getResultList();
    }

}
