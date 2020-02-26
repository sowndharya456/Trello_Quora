package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionBusinessService {

    @Autowired
    QuestionDao questionDao;
    @Autowired
    UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        return questionDao.createQuestion(questionEntity);
    }

    public List<QuestionEntity> getAllQuestions()
    {
        return questionDao.getAllQuestions();
    }
    public QuestionEntity getQuestionById(String questionId){
           return questionDao.getQuestionById(questionId);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateQuestion(QuestionEntity questionEntity){
        questionDao.updateQuestion(questionEntity);
    }

    public UserAuthEntity getUserByAuth(String authorization){
        return userDao.getAccessToken(authorization);
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public void deleteQuestion (QuestionEntity questionEntity){
        questionDao.deleteQuestion(questionEntity);
    }

    public List<QuestionEntity> getAllQuestionsByUser(String userId){
        return questionDao.getAllQuestionsByUser(userId);
    }
}
