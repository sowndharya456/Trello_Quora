package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class UserBusinessService {
    @Autowired
    UserDao userDao;

    @Autowired
    PasswordCryptographyProvider passwordCryptographyProvider;

   @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signUp(UserEntity userEntity){
       String [] password= passwordCryptographyProvider.encrypt(userEntity.getPassword());
       userEntity.setSalt(password[0]);
       userEntity.setPassword(password[1]);
       return userDao.createUser(userEntity);
   }

   public UserEntity getUserByEmail(String email){
       return userDao.getUserByEmail(email);
   }


   public UserEntity getUserByUsername(String username){
       return userDao.getUserByUsername(username);
   }

    @Transactional(propagation = Propagation.REQUIRED)
   public UserAuthEntity authenticate(String username, String password)throws AuthenticationFailedException {
       UserEntity userEntity = getUserByUsername(username);
       if (userEntity == null) {
           throw new AuthenticationFailedException("ATH-001", "This username does not exist");
       }
       UserAuthEntity userAuthEntity = new UserAuthEntity();
       String encodedPassword = PasswordCryptographyProvider.encrypt(password, userEntity.getSalt());
       if (encodedPassword.equals(userEntity.getPassword())) {
           JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encodedPassword);
           userAuthEntity.setUser(userEntity);
           final ZonedDateTime now = ZonedDateTime.now();
           final ZonedDateTime end = now.plusHours(8);
           userAuthEntity.setAccess_token(jwtTokenProvider.generateToken(userEntity.getUUID(), now, end));
           userAuthEntity.setLogin_at(now);
           userAuthEntity.setExpires_at(end);
           userAuthEntity.setUuid(userEntity.getUUID());
           UserAuthEntity userAuthEntity1= userDao.createUserAuth(userAuthEntity);
           userDao.updateUser(userEntity);
           return userAuthEntity1;
       } else {
           throw new AuthenticationFailedException("ATH-002", "Password failed");
       }
   }
   @Transactional(propagation = Propagation.REQUIRED)
   public UserAuthEntity signOut(String access_token) throws SignOutRestrictedException{
       UserAuthEntity userAuthEntity= userDao.getAccessToken(access_token);
       if(userAuthEntity!=null){
           final ZonedDateTime now = ZonedDateTime.now();
           userAuthEntity.setLogout_at(now);
           userDao.updateUserAuth(userAuthEntity);
           return userAuthEntity;
       }
       else
       {
               throw new SignOutRestrictedException("SGR-001","User is not Signed in");
       }
   }


}
