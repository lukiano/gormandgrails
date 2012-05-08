package com.lucho.repository;

import com.lucho.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Repository that handles User objects.
 *
 * @author Luciano.Leggieri
 */
public interface UserRepository extends JpaRepository<User, Integer>,
        UserRepositoryCustom, QueryDslPredicateExecutor<User> {

    /**
     * Find a user by its username.
     *
     * @param username the name of the user to find.
     * @return a User object that holds the username information or null
     *         if no user with that name exists.
     */
    User findByUsername(String username);

}
