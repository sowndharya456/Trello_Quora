package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.CommonBusinessService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CommonController {
     @Autowired
    CommonBusinessService commonBusinessService;

    @RequestMapping(method= RequestMethod.GET,path="/userprofile/{userId}" ,produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> userProfile(@PathVariable("userId") String userId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
       commonBusinessService.verifyAccessToken(authorization);

        UserEntity userEntity=commonBusinessService.getUserByUserId(userId);
        if(userEntity==null){
            throw new UserNotFoundException("USR-001" ,"User with entered uuid does not exist");
        }
        UserDetailsResponse userDetailsResponse=new UserDetailsResponse().firstName(userEntity.getFirst_name()).lastName(userEntity.getLast_name()).userName(userEntity.getUsername()).
                aboutMe(userEntity.getAboutme()).contactNumber(userEntity.getContactnumber()).country(userEntity.getCountry()).
                dob(userEntity.getDob()).emailAddress(userEntity.getEmail());

        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);

    }
}
