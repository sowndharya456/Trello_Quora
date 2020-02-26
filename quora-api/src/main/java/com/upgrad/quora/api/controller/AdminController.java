package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.AdminBusinessService;
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
public class AdminController {

    @Autowired
    AdminBusinessService adminBusinessService;

    @RequestMapping(method= RequestMethod.DELETE,path="/admin/user/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> userDelete(@PathVariable("userId") String userId, @RequestHeader("authorization") String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UserEntity userEntity =adminBusinessService.userDelete(userId,authorization);

        UserDeleteResponse userDeleteResponse=new UserDeleteResponse().id(userEntity.getUUID()).status("USER SUCCESSFULLY DELETED");

        return new ResponseEntity<UserDeleteResponse>(userDeleteResponse, HttpStatus.OK);

    }
}
