package com.saikumar.InstagramBackend.service;

import com.saikumar.InstagramBackend.repository.IAdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    IAdminRepo adminRepo;

}
