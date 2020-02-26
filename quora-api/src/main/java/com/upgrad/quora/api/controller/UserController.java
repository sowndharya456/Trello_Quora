package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    UserBusinessService userBusinessService;

    @RequestMapping(method=RequestMethod.POST, path="/user/signup",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> signup(final SignupUserRequest signupUserRequest) throws SignUpRestrictedException
    {
        UserEntity userEntity;
        userEntity=userBusinessService.getUserByUsername(signupUserRequest.getUserName());

        if(userEntity!=null){
            throw new SignUpRestrictedException("SGR-001","Try any other Username, this Username has already been taken");

           }

        userEntity= userBusinessService.getUserByEmail(signupUserRequest.getEmailAddress());

        if(userEntity!=null){
            throw new SignUpRestrictedException("SGR-002","This user has already been registered, try with any other emailId");
        }
        userEntity = new UserEntity();
        userEntity.setFirst_name(signupUserRequest.getFirstName());
        userEntity.setLast_name(signupUserRequest.getLastName());
        userEntity.setCountry(signupUserRequest.getCountry());
        userEntity.setAboutme(signupUserRequest.getAboutMe());
        userEntity.setContactnumber(signupUserRequest.getContactNumber());
        userEntity.setDob(signupUserRequest.getDob());
        userEntity.setEmail(signupUserRequest.getEmailAddress());
        userEntity.setUUID(UUID.randomUUID().toString());
        userEntity.setSalt("1234abc");
        userEntity.setRole("nonadmin");
        userEntity.setUsername(signupUserRequest.getUserName());
        userEntity.setPassword(signupUserRequest.getPassword());

        UserEntity createdUserEntity=userBusinessService.signUp(userEntity);
        SignupUserResponse signupUserResponse= new SignupUserResponse().id(createdUserEntity.getUUID()).status("USER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupUserResponse>( signupUserResponse, HttpStatus.CREATED);

    }
    @RequestMapping(method=RequestMethod.POST, path="/user/signin",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> signin(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {
        byte[] encodedText = Base64.getDecoder().decode(authorization.split(" ")[1]);

        String tempString=new String(encodedText);
        String[] decode= tempString.split(":");
        UserAuthEntity userAuthEntity = userBusinessService.authenticate(decode[0],decode[1]);
        SigninResponse signinResponse =new SigninResponse().id(userAuthEntity.getUuid()).message("SIGNED IN SUCCESSFULLY");

        return new ResponseEntity<SigninResponse>(signinResponse,HttpStatus.OK);
    }

    @RequestMapping(method=RequestMethod.POST,path="/user/signout")
    public ResponseEntity<SignoutResponse> signout(@RequestHeader("authorization") final String authorization) throws SignOutRestrictedException {
        UserAuthEntity userAuthEntity = userBusinessService.signOut(authorization);

        SignoutResponse signoutResponse =new SignoutResponse().id(userAuthEntity.getUuid()).message("SIGNED OUT SUCCESSFULLY");
        return new ResponseEntity<SignoutResponse>(signoutResponse,HttpStatus.OK);

    }
}
