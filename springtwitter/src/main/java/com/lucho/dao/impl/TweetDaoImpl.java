package com.lucho.dao.impl;

import com.lucho.dao.TweetDao;
import com.lucho.domain.Tweet;
import com.lucho.domain.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class TweetDaoImpl implements TweetDao {

    private static final int MAX_RESULTS = 20;

    private SessionFactory sessionFactory;

    @Override
    @SuppressWarnings("unchecked")
    public List<Tweet> getTweetsForUser(final User user) {
        Session session = this.getSessionFactory().getCurrentSession();
        Query query = session.createQuery("from Tweet where owner.id = :userId");
        query.setParameter("userId", user.getId());
        query.setMaxResults(MAX_RESULTS);
        return query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Tweet> getTweetsForUserIncludingFollows(final User user) {
        Session session = this.getSessionFactory().getCurrentSession();
        Query query = session.createQuery("from Tweet tweet inner join tweet.owner.followedBy followed where followed.id = :userId");
        query.setParameter("userId", user.getId());
        query.setMaxResults(MAX_RESULTS);
        return query.list();
    }

    @Override
    public Tweet newTweet(final User user, final String text) {
        Tweet tweet = new Tweet();
        tweet.setOwner(user);
        tweet.setTweet(text);
        tweet.setCreationDate(new Date());
        Session session = this.getSessionFactory().getCurrentSession();
        session.persist(tweet);
        return tweet;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Tweet> searchTweets(final String textToSearch) {
        Session session = this.getSessionFactory().getCurrentSession();
        FullTextSession fullTextSession = Search.getFullTextSession(session);

        // create native Lucene query unsing the query DSL
        // alternatively you can write the Lucene query using the Lucene query parser
        // or the Lucene programmatic API. The Hibernate Search DSL is recommended though
        QueryBuilder qb = fullTextSession.getSearchFactory()
                .buildQueryBuilder().forEntity(Tweet.class).get();
        org.apache.lucene.search.Query lQuery = qb.keyword()
                .onFields("tweet").matching(textToSearch).createQuery();

        // wrap Lucene query in a org.hibernate.Query
        Query query =fullTextSession.createFullTextQuery(lQuery, Tweet.class);
        query.setMaxResults(MAX_RESULTS);
        return query.list();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
