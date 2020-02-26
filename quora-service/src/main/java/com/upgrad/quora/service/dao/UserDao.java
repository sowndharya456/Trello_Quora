package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;


@Repository
public class UserDao {

    @PersistenceContext
    EntityManager entityManager;

    public UserEntity createUser(UserEntity userEntity){
        entityManager.persist(userEntity);
        return userEntity;
    }

    public UserEntity getUserByEmail(String email){
         try {
             return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
         }
         catch (NoResultException nre) {
             return null;
         }

    }

    public UserEntity getUserByUsername(String username){
          try {
              return entityManager.createNamedQuery("userByUsername", UserEntity.class).setParameter("username", username).getSingleResult();
          }
          catch (NoResultException nre) {
              return null;
          }
    }

    public UserAuthEntity createUserAuth(UserAuthEntity userAuthEntity){
        entityManager.persist(userAuthEntity);
        return userAuthEntity;
    }
    public void updateUser(UserEntity userEntity){
        entityManager.merge(userEntity);
    }
    public UserAuthEntity getAccessToken(String access_token){
        try{
           return entityManager.createNamedQuery("userAuthByAccessToken",UserAuthEntity.class).setParameter("access_token",access_token).getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }
    public void updateUserAuth(UserAuthEntity userAuthEntity){
        entityManager.merge(userAuthEntity);
    }
    public UserEntity getUserByUserId(String userId){
        try{
            return entityManager.createNamedQuery("userByUserId",UserEntity.class).setParameter("uuid",userId).getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public void deleteUser(UserEntity userEntity){
        entityManager.remove(userEntity);
    }
}
