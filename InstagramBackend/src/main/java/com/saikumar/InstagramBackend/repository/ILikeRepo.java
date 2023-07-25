package com.saikumar.InstagramBackend.repository;

import com.saikumar.InstagramBackend.model.Like;
import com.saikumar.InstagramBackend.model.Post;
import com.saikumar.InstagramBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ILikeRepo extends JpaRepository<Like,Integer> {

    List<Like> findByInstaPostAndLiker(Post instaPost, User liker);

    List<Like> findAllByInstaPost(Post post);
}
