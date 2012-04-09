package com.lucho.repository;

import com.lucho.domain.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Repository that handles Tweet objects.
 * @author Luciano.Leggieri
 */
public interface TweetRepository extends JpaRepository<Tweet, Integer>,
        TweetRepositoryCustom, QueryDslPredicateExecutor {
}
