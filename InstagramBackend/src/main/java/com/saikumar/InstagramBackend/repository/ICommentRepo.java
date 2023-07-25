package com.saikumar.InstagramBackend.repository;

import com.saikumar.InstagramBackend.model.Comment;
import com.saikumar.InstagramBackend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICommentRepo extends JpaRepository<Comment,Integer> {
    List<Comment> findAllByInstaPost(Post post);
}
