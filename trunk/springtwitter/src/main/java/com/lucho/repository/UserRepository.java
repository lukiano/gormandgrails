package com.lucho.repository;

import com.lucho.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("select count(user) from User user inner join user.followedBy followed where user.id = :userToFollowId and followed.id = :userId")
	boolean notFollowedBy(final User user, final User userToFollow);

}
