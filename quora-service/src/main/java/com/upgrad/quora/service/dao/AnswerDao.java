package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {

    @PersistenceContext
    EntityManager entityManager;

    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    public AnswerEntity getAnswerByUUID( String answerId){
        try{
            return entityManager.createNamedQuery("answerByuuid",AnswerEntity.class).setParameter("uuid",answerId).getSingleResult();
        }
        catch(NoResultException nre){
            return null;
        }

    }

    public void updateAnswer(AnswerEntity answerEntity) {
        entityManager.merge(answerEntity);
    }

    public void deleteAnswer(AnswerEntity answerEntity) {
        entityManager.remove(answerEntity);
    }

    public List<AnswerEntity> getAllAnswersToQuestions(String questionId) {
     try{
         return entityManager.createNamedQuery("getAllAnswerstoQuestion",AnswerEntity.class).setParameter("questionId",questionId).getResultList();
     }
     catch(NoResultException nre){
         return null;
     }
    }
}
