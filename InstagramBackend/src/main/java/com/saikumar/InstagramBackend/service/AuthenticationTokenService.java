package com.saikumar.InstagramBackend.service;

import com.saikumar.InstagramBackend.model.AuthenticationToken;
import com.saikumar.InstagramBackend.model.User;
import com.saikumar.InstagramBackend.repository.IAuthenticationTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationTokenService {
    @Autowired
    IAuthenticationTokenRepo authenticationTokenRepo;

    public boolean authenticate(String email, String tokenValue) {
      AuthenticationToken authenticationToken = authenticationTokenRepo.findFirstByTokenValue(tokenValue);
      if(authenticationToken == null){
          return false;
      }
      String tokenConnectedEmail = authenticationToken.getUser().getUserEmail();
      return email.equals(tokenConnectedEmail);
    }

    public void save(AuthenticationToken authenticationToken) {
        authenticationTokenRepo.save(authenticationToken);
    }

    public AuthenticationToken findFirstByUser(User user) {
        return authenticationTokenRepo.findFirstByUser(user);
    }

    public void remove(AuthenticationToken authenticationToken) {
        authenticationTokenRepo.delete(authenticationToken);
    }
}
