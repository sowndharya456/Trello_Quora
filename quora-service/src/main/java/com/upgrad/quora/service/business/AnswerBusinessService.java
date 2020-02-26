package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerBusinessService {
    @Autowired
    AnswerDao answerDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(AnswerEntity answerEntity){
        return answerDao.createAnswer(answerEntity);
    }

    public AnswerEntity getAnswerByUUID(String answerId){
        return answerDao.getAnswerByUUID(answerId);
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public void updateAnswer(AnswerEntity answerEntity) {
        answerDao.updateAnswer(answerEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAnswer(AnswerEntity answerEntity) {
        answerDao.deleteAnswer(answerEntity);
    }

    public List<AnswerEntity> getAllAnswersToQuestion(String questionId) {
        return answerDao.getAllAnswersToQuestions(questionId);
    }
}
