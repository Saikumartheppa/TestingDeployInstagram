package com.saikumar.InstagramBackend.repository;

import com.saikumar.InstagramBackend.model.Follow;
import com.saikumar.InstagramBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFollowRepo extends JpaRepository<Follow,Integer> {
    List<Follow> findByCurrentUserAndCurrentUserFollower(User targetUser, User follower);

    List<Follow> findByCurrentUser(User currentUser);

    List<Follow> findByCurrentUserFollower(User currentUserFollower);
}
