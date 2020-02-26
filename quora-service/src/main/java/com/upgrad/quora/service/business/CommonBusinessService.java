package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonBusinessService {
    @Autowired
    UserDao userDao;

    public void verifyAccessToken(String authorization) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userDao.getAccessToken(authorization);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if(userAuthEntity.getLogout_at()!=null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get user details");
        }
    }
    public UserEntity getUserByUserId(String userId){
        return userDao.getUserByUserId(userId);
    }
}
