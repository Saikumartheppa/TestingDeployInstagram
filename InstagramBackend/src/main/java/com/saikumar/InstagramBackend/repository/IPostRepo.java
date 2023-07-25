package com.saikumar.InstagramBackend.repository;

import com.saikumar.InstagramBackend.model.Post;
import com.saikumar.InstagramBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPostRepo extends JpaRepository<Post,Integer> {

}
