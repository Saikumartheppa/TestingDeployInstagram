package com.saikumar.InstagramBackend.service;

import com.saikumar.InstagramBackend.model.Like;
import com.saikumar.InstagramBackend.model.Post;
import com.saikumar.InstagramBackend.model.User;
import com.saikumar.InstagramBackend.repository.ILikeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {
    @Autowired
    ILikeRepo likeRepo;

    public boolean isLikeAllowedOnThisPost(Post instaPost, User liker) {
        List<Like> likeList = likeRepo.findByInstaPostAndLiker(instaPost,liker);
        return likeList!=null && likeList.isEmpty();
    }

    public void save(Like like, User user) {
        like.setLiker(user);
        likeRepo.save(like);
    }

    public Like findLikeOwner(Integer likeId) {
        return likeRepo.findById(likeId).orElse(null);
    }

    public void removeInstagramLike(Like like) {
        likeRepo.delete(like);
    }

    public Integer getLikesCountByPost(Post post) {
        return likeRepo.findAllByInstaPost(post).size();
    }

    public void deleteAllLikesOnPost(Post post) {
        List<Like> likesOnPost = likeRepo.findAllByInstaPost(post);
        likeRepo.deleteAll(likesOnPost);
    }
}
