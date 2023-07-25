package com.saikumar.InstagramBackend.service;

import com.saikumar.InstagramBackend.model.Comment;
import com.saikumar.InstagramBackend.model.Post;
import com.saikumar.InstagramBackend.model.User;
import com.saikumar.InstagramBackend.repository.ICommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    ICommentRepo commentRepo;

    public String addInstagramComment(Comment comment, User commentOwner) {
        comment.setCommentCreationTimeStamp(LocalDateTime.now());
        comment.setCommentOwner(commentOwner);
        commentRepo.save(comment);
        return "Comment added Successfully!!!!";
    }

    public Comment findComment(Integer commentId) {
        return commentRepo.findById(commentId).orElse(null);
    }

    public void removeInstagramComment(Comment comment) {
        commentRepo.delete(comment);
    }

    public Integer getCommentsCountByPost(Post post) {
       return commentRepo.findAllByInstaPost(post).size();
    }

    public List<Comment> findAllCommentsByInstaPost(Post post) {
        return commentRepo.findAllByInstaPost(post);
    }


    public void deleteAllCommentsOnPost(Post post) {
        List<Comment> commentsOnPost = commentRepo.findAllByInstaPost(post);
        commentRepo.deleteAll(commentsOnPost);
    }
}
