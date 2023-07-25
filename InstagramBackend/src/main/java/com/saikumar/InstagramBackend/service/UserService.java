package com.saikumar.InstagramBackend.service;

import com.saikumar.InstagramBackend.model.*;
import com.saikumar.InstagramBackend.model.dto.SignInInput;
import com.saikumar.InstagramBackend.model.dto.SignUpOutput;
import com.saikumar.InstagramBackend.repository.IUserRepo;
import com.saikumar.InstagramBackend.service.emailUtility.EmailHandler;
import com.saikumar.InstagramBackend.service.hashingUtility.PasswordEncrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    IUserRepo userRepo;
    @Autowired
    AuthenticationTokenService authenticationTokenService;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;
    @Autowired
    FollowService followService;
    public SignUpOutput SignupInstagramUser(User user) {
        boolean signUpStatus = true;
        String signUpStatusMessage = null;
        // check the user email  if null
        String newEmail = user.getUserEmail();
        if(newEmail == null){
            signUpStatus = false;
            signUpStatusMessage = "The Instagram User email should not be null";
            return new SignUpOutput(signUpStatus,signUpStatusMessage);
        }
        // check if already exists??
        User existingUser = userRepo.findFirstByUserEmail(newEmail);
        if(existingUser != null){
            signUpStatus = false;
            signUpStatusMessage = "The Instagram User already exists!! Try with signIn";
            return new SignUpOutput(signUpStatus,signUpStatusMessage);
        }
        // encrypt the password of user by hashing
        try {
            String encryptedPassword = PasswordEncrypter.encryptPassword(user.getUserPassword());
            // set the encrypted password of user and save the encrypted password
            user.setUserPassword(encryptedPassword);
            userRepo.save(user);
            return new SignUpOutput(signUpStatus,"User registered Successfully!!");
        }catch(Exception e){
            signUpStatus = false;
            signUpStatusMessage = "Internal error occurred during sign up";
            return new SignUpOutput(signUpStatus,signUpStatusMessage);
        }
    }

    public String SignInInstagramUser(SignInInput signInInput) {
        String signInInputMessage = null;
        String signInEmail = signInInput.getEmail();
        // check if email is null
        if(signInEmail == null){
            signInInputMessage = "The Instagram user email should not be null";
            return signInInputMessage;
        }
        // check email exists are not
        User existingUser = userRepo.findFirstByUserEmail(signInEmail);
        if(existingUser == null){
            signInInputMessage = "No such Instagram User found!! Try with signUp";
            return signInInputMessage;
        }
        try {
            String encryptedPassword = PasswordEncrypter.encryptPassword(signInInput.getPassword());
            if(existingUser.getUserPassword().equals(encryptedPassword)){
                AuthenticationToken authenticationToken = new AuthenticationToken(existingUser);
                authenticationTokenService.save(authenticationToken);
                EmailHandler.sendEmail(signInEmail,"Testing email",authenticationToken.getTokenValue());
                return "Token sent to your email";
            }else{
                signInInputMessage = "Invalid credentials";
                return signInInputMessage;
            }
        }catch(Exception e){
            signInInputMessage =  "Internal error occurred during signing In";
            return signInInputMessage;
        }
    }

    public String SignOutInstagramUser(String email) {
        User user = userRepo.findFirstByUserEmail(email);
        AuthenticationToken authenticationToken = authenticationTokenService.findFirstByUser(user);
        authenticationTokenService.remove(authenticationToken);
        return "user signOut successfully";
    }

    public String createInstagramPost(Post post, String email) {
        User postOwner = userRepo.findFirstByUserEmail(email);
        post.setPostOwner(postOwner);
        return  postService.createInstagramPost(post);
    }

    public String removeInstagramPost(Integer postId, String email) {
        User postOwner = userRepo.findFirstByUserEmail(email);
        return postService.removeInstagramPost(postOwner,postId);
    }

    public String addInstagramComment(Comment comment, String commentOwnerEmail) {
       boolean isValidPost = postService.validatePost(comment.getInstaPost());
       if(isValidPost) {
           User commentOwner = userRepo.findFirstByUserEmail(commentOwnerEmail);
           return commentService.addInstagramComment(comment,commentOwner);
       }else{
           return "No Such Post exists!!!";
       }
    }
    private boolean authorizeCommentRemover(Comment comment, String email) {
        String commentOwnerEmail = comment.getCommentOwner().getUserEmail();
        String postOwnerEmail = comment.getInstaPost().getPostOwner().getUserEmail();
        return commentOwnerEmail.equals(email) || postOwnerEmail.equals(email);
    }

    public String removeInstagramComment(Integer commentId, String commentOwnerEmail) {
        Comment comment = commentService.findComment(commentId);
        if(comment != null){
            if(authorizeCommentRemover(comment,commentOwnerEmail)){
                commentService.removeInstagramComment(comment);
                return "Comment deleted successfully!!";
            }else{
                return "Unauthorized delete detected...Not allowed!!";
            }
        }else{
           return "No such comment" +" : "+ commentId +" "+ "exists!!";
        }
    }


    public String getCommentsCountByPost(Integer postId, String email) {
        Post post = postService.findByPostId(postId);
        if(postService.validatePost(post)){
            Integer commentsCount = commentService.getCommentsCountByPost(post);
                return commentsCount != 0 ? String.valueOf(commentsCount) : "No comments exists on post "+" : " + postId;
        }else{
            return "No such post " +" : "+ postId +" "+ "exists!!";
        }
    }

    public String addLike(Like like, String email) {
        Post instaPost = like.getInstaPost();
        boolean isValidPost = postService.validatePost(instaPost);
        if(isValidPost) {
            User liker = userRepo.findFirstByUserEmail(email);
            if(likeService.isLikeAllowedOnThisPost(instaPost,liker)){
                likeService.save(like,liker);
                return "Like added successfully on post"+" : "+ liker.getUserId();
            }else{
                return " Like denied...! Post already liked by user";
            }
        }else{
            return "No such post " +" : "+ instaPost.getPostId() +" "+ "exists!!";
        }
    }
    private boolean authorizeLikeRemover(Like like, String likerEmail) {
        User existedLikeOwner = like.getLiker();
        return  existedLikeOwner.getUserEmail().equals(likerEmail);
    }

    public String removeInstagramLike(Integer likeId, String likerEmail) {
        Like like = likeService.findLikeOwner(likeId);
        if(like != null){
           if(authorizeLikeRemover(like,likerEmail)){
               likeService.removeInstagramLike(like);
               return "Like" + " : "+ likeId +"removed successfully";
           }else {
               return "Unauthorized delete detected...Not allowed!!";
           }
        }else{
            return "Invalid Like";
        }
    }


    public String getLikesCountByPost(Integer postId) {
        Post post = postService.findByPostId(postId);
        if(postService.validatePost(post)){
            Integer likeCount = likeService.getLikesCountByPost(post);
                return likeCount != 0 ? likeCount.toString() : "No likes existed on post"+" : "+postId;
        }else{
            return "Invalid post : " + postId;
        }
    }

    public String followUser(Follow follow, String followerEmail) {
        User followTargetUser = userRepo.findById(follow.getCurrentUser().getUserId()).orElse(null);
        User follower = userRepo.findFirstByUserEmail(followerEmail);
        if(followTargetUser != null){
             if(followService.isFollowAllowed(followTargetUser,follower)){
                 followService.startFollowing(follow,follower);
                 return follower.getUserHandle()  + " is now following " + followTargetUser.getUserHandle();

             }else{
                 return follower.getUserHandle()  + " already follows " + followTargetUser.getUserHandle();
                 }
        }else{
            return "User to be followed is Invalid!!!";
        }
    }

    private boolean authorizeUnFollow(Follow follow, String email) {
        String targetEmail = follow.getCurrentUser().getUserEmail();
        String followerEmail = follow.getCurrentUserFollower().getUserEmail();
        return targetEmail.equals(email) || followerEmail.equals(email);
    }

    public String UnFollowUser(Integer followId,String FollowerEmail) {
        Follow follow = followService.findFollow(followId);
        if(follow != null){
            if(authorizeUnFollow(follow,FollowerEmail)){
                followService.removeFollow(follow);
                return "Successfully unfollowed";
            }else{
                return "Unauthorized unfollow detected...Not allowed!!";
            }
        }else{
            return "Invalid follow request";
        }
    }


    public String getFollowersCountOfUser(Integer userId) {
        User user = userRepo.findById(userId).orElse(null);
        if(user != null){
          Integer followersCount =   followService.getFollowersCountOfUser(user);
          return String.valueOf(followersCount);
        }else{
          return "No such user : " + userId +"  exists!!!!";
        }
    }

    public String getFollowingCountOfUser(Integer userId) {
        User user = userRepo.findById(userId).orElse(null);
        if(user != null){
            Integer followingCount =   followService.getFollowingCountOfUser(user);
            return String.valueOf(followingCount);
        }else{
            return "No such user : " + userId +"  exists!!!!";
        }
    }
}
