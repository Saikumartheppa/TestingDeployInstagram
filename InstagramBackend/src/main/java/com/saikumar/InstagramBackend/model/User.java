package com.saikumar.InstagramBackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.saikumar.InstagramBackend.model.enums.AccountType;
import com.saikumar.InstagramBackend.model.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer userId;
   private String userName;
   private String userHandle;
   private String userBio;
   @Email
   @Column(unique = true)
   private String userEmail;
   @NotBlank
   private String userPassword;
   @Enumerated(EnumType.STRING)
   private Gender userGender;
   @Enumerated(EnumType.STRING)
   private AccountType accountType;
   @JsonProperty(access = JsonProperty.Access.READ_ONLY)
   private  boolean blueTick;
}
