package com.lucho.repository;

import com.lucho.domain.QTweet;
import com.lucho.domain.QUser;
import com.lucho.domain.Tweet;
import com.lucho.domain.User;
import com.lucho.repository.TweetRepositoryCustom;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;

import javax.persistence.Query;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: luciano
 * Date: 4/3/12
 * Time: 11:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class TweetRepositoryImpl extends QueryDslRepositorySupport implements TweetRepositoryCustom {

    private static final int MAX_RESULTS = 20;

    @Override
    public List<Tweet> getTweetsForUserIncludingFollows(final User user) {
        QTweet qtweet = QTweet.tweet1;
        QUser followedBy = new QUser("followedBy");
        return this.from(qtweet).join(qtweet.owner.followedBy, followedBy)
                .where(followedBy.id.eq(user.getId())).limit(MAX_RESULTS).list(qtweet);
    }

    @Override
    public Tweet newTweet(final User user, final String text, final String language) {
        Tweet tweet = new Tweet();
        tweet.setOwner(user);
        tweet.setTweet(text);
        tweet.setLanguage(language);
        tweet.setCreationDate(new DateTime());
        this.getEntityManager().persist(tweet);
        return tweet;
    }

    @Override
    public List<Tweet> searchTweets(final String textToSearch) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(this.getEntityManager());

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
