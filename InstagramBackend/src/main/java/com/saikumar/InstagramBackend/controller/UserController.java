package com.saikumar.InstagramBackend.controller;

import com.saikumar.InstagramBackend.model.*;
import com.saikumar.InstagramBackend.model.dto.SignInInput;
import com.saikumar.InstagramBackend.model.dto.SignUpOutput;
import com.saikumar.InstagramBackend.service.AuthenticationTokenService;
import com.saikumar.InstagramBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationTokenService authenticationTokenService;
    //SignUp SignIn SignOut
    @PostMapping("user/signUp")
    public SignUpOutput SignupInstagramUser(@RequestBody User user){
        return userService.SignupInstagramUser(user);
    }
    @PostMapping("user/signIn")
    public String SignInInstagramUser(@RequestBody SignInInput signInInput){
        return userService.SignInInstagramUser(signInInput);
    }
    @DeleteMapping("user/signOut")
    public String SignOutInstagramUser(@RequestParam String email,@RequestParam String tokenValue){
        if(authenticationTokenService.authenticate(email,tokenValue)){
            return userService.SignOutInstagramUser(email);
        }else{
            return "Sign out not allowed for non authenticated user.";
        }
    }
    // content of instagram
    @PostMapping("post")
    public  String createInstagramPost(@RequestBody Post post,@RequestParam String email,@RequestParam String tokenValue) {
        if (authenticationTokenService.authenticate(email, tokenValue)) {
            return userService.createInstagramPost(post,email);
        }else{
            return "Not an Authenticated user activity!!!";
        }
    }
    @DeleteMapping("post")
    public String removeInstagramPost(@RequestParam Integer postId,@RequestParam String email,@RequestParam String tokenValue){

        if (authenticationTokenService.authenticate(email, tokenValue)) {
            return userService.removeInstagramPost(postId,email);
        }else{
            return "Not an Authenticated user activity!!!";
        }
    }
    // comment functionalities on Instagram
    @PostMapping("comment")
    public String addInstagramComment(@RequestBody Comment comment,@RequestParam String commentOwnerEmail,String commentOwnerTokenValue){
        if(authenticationTokenService.authenticate(commentOwnerEmail,commentOwnerTokenValue)){
            return userService.addInstagramComment(comment,commentOwnerEmail);
        }else{
            return "Not an Authenticated user activity!!!";
        }
    }

    @DeleteMapping("comment")
    public String removeInstagramComment(@RequestParam Integer commentId,@RequestParam String email,@RequestParam String token){
        if(authenticationTokenService.authenticate(email,token)){
            return userService.removeInstagramComment(commentId,email);
        }else{
            return "Not an Authenticated user activity!!!";
        }
    }
    @GetMapping("comments/count/post/{postId}")
    public String getCommentsCountByPost(@PathVariable Integer postId,@RequestParam String email,@RequestParam String token){
        if(authenticationTokenService.authenticate(email,token)){
            return userService.getCommentsCountByPost(postId,email);
        }else{
            return "Not an Authenticated user activity!!!";
        }
    }
    //Like functionalities on Instagram;
    @PostMapping("like")
    public String addLike(@RequestBody Like like,@RequestParam String email,@RequestParam String token) {
        if(authenticationTokenService.authenticate(email,token)){
            return userService.addLike(like,email);
        }else{
            return "Not an Authenticated user activity!!!";
        }
    }
    @DeleteMapping("like")
    public String removeInstagramLike(@RequestParam Integer likeId,@RequestParam String likerEmail,@RequestParam String likerToken) {
       if(authenticationTokenService.authenticate(likerEmail,likerToken)){
           return userService.removeInstagramLike(likeId,likerEmail);
       }else{
           return "Not an Authenticated user activity!!!!";
       }
    }
    @GetMapping("like/count/post/{postId}")
    public String getLikesCountByPost(@PathVariable Integer postId,@RequestParam String email,@RequestParam String token){
        if(authenticationTokenService.authenticate(email,token)){
           return  userService.getLikesCountByPost(postId);
        }else{
            return "Not an Authenticated user activity!!!!";
        }
    }
    //follow functionalities  on Instagram
    @PostMapping("follow")
    public String followUser(@RequestBody Follow follow,@RequestParam String followerEmail,@RequestParam String followerToken){
        if(authenticationTokenService.authenticate(followerEmail,followerToken)){
            return userService.followUser(follow,followerEmail);
        }else{
            return "Not an Authenticated user activity!!!!";
        }
    }
    @DeleteMapping("unfollow/targetUser/{followId}")
    public String UnFollowUser(@PathVariable Integer followId,@RequestParam String followerEmail,@RequestParam String token){
        if(authenticationTokenService.authenticate(followerEmail,token)){
            return userService.UnFollowUser(followId,followerEmail);
        }else{
            return "Not an Authenticated user activity!!!";
        }
    }
    @GetMapping("follow/followers/count/user/{userId}")
    public String getFollowersCountOfUser(@PathVariable Integer userId,@RequestParam String email,@RequestParam String token){
        if(authenticationTokenService.authenticate(email,token)){
            return userService.getFollowersCountOfUser(userId);
        }else{
            return "Not an Authenticated user activity!!!!!";
        }
    }
    @GetMapping("follow/following/count/user/{userId}")
    public String getFollowingCountOfUser(@PathVariable Integer userId,@RequestParam String email,@RequestParam String token){
        if(authenticationTokenService.authenticate(email,token)){
            return userService.getFollowingCountOfUser(userId);
        }else{
            return "Not an Authenticated user activity!!!!!";
        }
    }
}
    
