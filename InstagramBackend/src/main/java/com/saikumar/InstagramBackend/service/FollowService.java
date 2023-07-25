package com.saikumar.InstagramBackend.service;

import com.saikumar.InstagramBackend.model.Follow;
import com.saikumar.InstagramBackend.model.User;
import com.saikumar.InstagramBackend.repository.IFollowRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowService {
    @Autowired
    IFollowRepo followRepo;

    public boolean isFollowAllowed(User targetUser, User follower) {
        List<Follow> followList = followRepo.findByCurrentUserAndCurrentUserFollower(targetUser,follower);
        return followList != null && followList.isEmpty() && !targetUser.equals(follower);
    }


    public void startFollowing(Follow follow, User follower) {
        follow.setCurrentUserFollower(follower);
        followRepo.save(follow);
    }


    public Follow findFollow(Integer followId) {
        return followRepo.findById(followId).orElse(null);
    }

    public void removeFollow(Follow follow) {
        followRepo.delete(follow);
    }

    public Integer getFollowersCountOfUser(User currentUser) {
        List<Follow> followerList = followRepo.findByCurrentUser(currentUser);
        return followerList.size();
    }

    public Integer getFollowingCountOfUser(User currentUserFollower) {
        List<Follow> followerList = followRepo.findByCurrentUserFollower(currentUserFollower);
        return followerList.size();
    }
}
