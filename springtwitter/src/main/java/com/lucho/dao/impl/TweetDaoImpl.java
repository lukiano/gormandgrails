package com.lucho.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import com.lucho.dao.TweetDao;
import com.lucho.domain.Tweet;
import com.lucho.domain.User;

@Repository
final class TweetDaoImpl implements TweetDao {

    private static final int MAX_RESULTS = 20;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<Tweet> getTweetsForUser(final User user) {
    	Query query = this.entityManager.createQuery("from Tweet where owner.id = :userId");
        query.setParameter("userId", user.getId());
        query.setMaxResults(MAX_RESULTS);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Tweet> getTweetsForUserIncludingFollows(final User user) {
        Query query = this.entityManager.createQuery("select tweet from Tweet tweet inner join tweet.owner.followedBy followed where followed.id = :userId");
        query.setParameter("userId", user.getId());
        query.setMaxResults(MAX_RESULTS);
        return query.getResultList();
    }

    @Override
    public Tweet newTweet(final User user, final String text) {
        Tweet tweet = new Tweet();
        tweet.setOwner(user);
        tweet.setTweet(text);
        tweet.setCreationDate(new DateTime());
        return this.entityManager.merge(tweet);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Tweet> searchTweets(final String textToSearch) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        // create native Lucene query unsing the query DSL
        // alternatively you can write the Lucene query using the Lucene query parser
        // or the Lucene programmatic API. The Hibernate Search DSL is recommended though
        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Tweet.class).get();
        org.apache.lucene.search.Query lQuery = qb.keyword()
                .onFields("tweet").matching(textToSearch).createQuery();

        // wrap Lucene query in a org.hibernate.Query
        Query query =fullTextEntityManager.createFullTextQuery(lQuery, Tweet.class);
        query.setMaxResults(MAX_RESULTS);
        return query.getResultList();
    }

}
