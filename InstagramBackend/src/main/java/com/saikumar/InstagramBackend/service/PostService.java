package com.saikumar.InstagramBackend.service;

import com.saikumar.InstagramBackend.model.Comment;
import com.saikumar.InstagramBackend.model.Post;
import com.saikumar.InstagramBackend.model.User;
import com.saikumar.InstagramBackend.repository.IPostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {
    @Autowired
    IPostRepo postRepo;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;
    public String createInstagramPost(Post post) {
        post.setPostCreatedTimeStamp(LocalDateTime.now());
        postRepo.save(post);
        return "Post created Successfully";
    }

    public String removeInstagramPost(User user, Integer postId) {
        Post post  = postRepo.findById(postId).orElse(null);
        if(post != null && post.getPostOwner().equals(user)){
            commentService.deleteAllCommentsOnPost(post);
            likeService.deleteAllLikesOnPost(post);
            postRepo.delete(post);
            return "post deleted successfully!!!!";
        }else if(post == null) {
            return "No such post exists!!!";
        }else{
          return  "Un-Authorised user detected..! Not allowed!!";
        }
    }

    public boolean validatePost(Post post) {
        return post != null && postRepo.existsById(post.getPostId());
    }

    public Post findByPostId(Integer postId) {
        return postRepo.findById(postId).orElse(null);
    }


}
