package com.saikumar.InstagramBackend.repository;

import com.saikumar.InstagramBackend.model.Admin;
import com.saikumar.InstagramBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAdminRepo extends JpaRepository<Admin,Long> {
}
