package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerBusinessService;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AnswerController {

    @Autowired
    AnswerBusinessService answerBusinessService;

    @Autowired
    QuestionBusinessService questionBusinessService;

    @RequestMapping(method= RequestMethod.POST,path="/question/{questionId}/answer/create" , produces= MediaType.APPLICATION_JSON_UTF8_VALUE,consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(AnswerRequest answer, @PathVariable("questionId")String questionId, @RequestHeader("authorization") String authorization) throws InvalidQuestionException, AuthorizationFailedException {


        QuestionEntity questionEntity=questionBusinessService.getQuestionById(questionId);
        if(questionEntity==null) {
            throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
        }
        UserAuthEntity userAuthEntity=questionBusinessService.getUserByAuth(authorization);

        if(userAuthEntity==null){
            throw new AuthorizationFailedException("ATHR-001" ,"User has not signed in");
        }

        if(userAuthEntity.getLogout_at()!=null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post an answer");
        }
        AnswerEntity answerEntity=new AnswerEntity();
        answerEntity.setAns(answer.getAnswer());
        answerEntity.setDate(ZonedDateTime.now());
        answerEntity.setQuestion(questionEntity);
        answerEntity.setUser(userAuthEntity.getUser());
        answerEntity.setUuid(UUID.randomUUID().toString());
        AnswerEntity createEntity = answerBusinessService.createAnswer(answerEntity);
        AnswerResponse answerResponse=new AnswerResponse().id(createEntity.getUuid()).status("ANSWER CREATED");
         return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.OK);
    }

    @RequestMapping(method=RequestMethod.PUT,path="/answer/edit/{answerId}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE,consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswerContent(AnswerEditRequest answerEditRequest,@PathVariable("answerId")String answerId,@RequestHeader("authorization")String authorization) throws AuthorizationFailedException, AnswerNotFoundException {
        UserAuthEntity userAuthEntity=questionBusinessService.getUserByAuth(authorization);


        if(userAuthEntity==null){
            throw new AuthorizationFailedException("ATHR-001" ,"User has not signed in");
        }

        if(userAuthEntity.getLogout_at()!=null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to edit an answer");
        }

        AnswerEntity answerEntity=answerBusinessService.getAnswerByUUID(answerId);
        if(answerEntity==null){
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }
        if(answerEntity.getUser().getUUID()!=userAuthEntity.getUser().getUUID()){
            throw new AuthorizationFailedException("ATHR-003","Only the answer owner can edit the answer");
        }

        answerEntity.setAns(answerEditRequest.getContent());
        answerBusinessService.updateAnswer(answerEntity);

        AnswerEditResponse answerEditResponse=new AnswerEditResponse().id(answerId).status("ANSWER EDITED");
        return new ResponseEntity<AnswerEditResponse>(answerEditResponse,HttpStatus.OK);
    }

    @RequestMapping(method=RequestMethod.DELETE,path="/answer/delete/{answerId}")
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@PathVariable("answerId")String answerId,@RequestHeader("authorization")String authorization) throws AuthorizationFailedException, AnswerNotFoundException {
        UserAuthEntity userAuthEntity=questionBusinessService.getUserByAuth(authorization);


        if(userAuthEntity==null){
            throw new AuthorizationFailedException("ATHR-001" ,"User has not signed in");
        }

        if(userAuthEntity.getLogout_at()!=null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to delete an answer");
        }

        AnswerEntity answerEntity=answerBusinessService.getAnswerByUUID(answerId);
        if(answerEntity==null){
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }


        if (!(answerEntity.getUser().getUUID().equals(userAuthEntity.getUser().getUUID()))
                && !(userAuthEntity.getUser().getRole().equals("admin"))) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
        }

        answerBusinessService.deleteAnswer(answerEntity);
        AnswerDeleteResponse answerDeleteResponse=new AnswerDeleteResponse().id(answerId).status("ANSWER DELETED");
        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse,HttpStatus.OK);
    }

    @RequestMapping(method=RequestMethod.GET,path="answer/all/{questionId}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
       public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersToQuestion(@PathVariable("questionId")String questionId, @RequestHeader("authorization")String authorization) throws InvalidQuestionException, AuthorizationFailedException {
        UserAuthEntity userAuthEntity=questionBusinessService.getUserByAuth(authorization);


        if(userAuthEntity==null){
            throw new AuthorizationFailedException("ATHR-001" ,"User has not signed in");
        }

        if(userAuthEntity.getLogout_at()!=null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to delete an answer");
        }
        QuestionEntity questionEntity=questionBusinessService.getQuestionById(questionId);
        if(questionEntity==null) {
            throw new InvalidQuestionException("QUES-001", "The question with entered uuid whose details are to be seen does not exist");
        }
        List <AnswerEntity> answerEntities=answerBusinessService.getAllAnswersToQuestion(questionId);
        List<AnswerDetailsResponse> answerDetailsResponses=new ArrayList<>();
        for(AnswerEntity answer:answerEntities){
            AnswerDetailsResponse answerDetailsResponse=new AnswerDetailsResponse().id(answer.getUuid()).questionContent(answer.getQuestion().getContent()).answerContent(answer.getAns());
              answerDetailsResponses.add(answerDetailsResponse);
        }

        return new ResponseEntity<List<AnswerDetailsResponse>>(answerDetailsResponses,HttpStatus.OK);
       }
}

