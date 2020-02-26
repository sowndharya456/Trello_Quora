package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.CommonBusinessService;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
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
public class QuestionController {
         @Autowired
         QuestionBusinessService questionBusinessService;
         @Autowired
         UserBusinessService userBusinessService;
         @Autowired
    CommonBusinessService commonBusinessService;

         @RequestMapping(method=RequestMethod.POST,path="/question/create",produces= MediaType.APPLICATION_JSON_UTF8_VALUE,consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createQuestion(@RequestHeader("authorization") String authorization, QuestionRequest questionRequest) throws SignOutRestrictedException, AuthorizationFailedException {
        UserAuthEntity userAuthEntity = questionBusinessService.getUserByAuth(authorization);

        if(userAuthEntity==null){
            throw new AuthorizationFailedException( "ATHR-001","User has not signed in");
        }
        if(userAuthEntity.getLogout_at()!=null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post a question");
        }

        QuestionEntity questionEntity=new QuestionEntity();
        questionEntity.setContent(questionRequest.getContent());
        questionEntity.setDate(ZonedDateTime.now());
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setUserEntity(userAuthEntity.getUser());

        QuestionEntity createdQuestion= questionBusinessService.createQuestion(questionEntity);
        QuestionResponse questionResponse= new QuestionResponse().id(createdQuestion.getUuid()).status("QUESTION CREATED");

        return new ResponseEntity<QuestionResponse>(questionResponse,HttpStatus.OK);
    }

     @RequestMapping(method=RequestMethod.GET,path="/question/all",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization")String authorization) throws AuthorizationFailedException {
         UserAuthEntity userAuthEntity = questionBusinessService.getUserByAuth(authorization);

         if(userAuthEntity==null){
             throw new AuthorizationFailedException( "ATHR-001","User has not signed in");
         }
         if(userAuthEntity.getLogout_at()!=null){
             throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get all questions");
         }
             List<QuestionEntity> questionList= questionBusinessService.getAllQuestions();

        List <QuestionDetailsResponse> questionDetailsResponses= new ArrayList<>();
         for (QuestionEntity question:questionList) {
             QuestionDetailsResponse questionResponse =new QuestionDetailsResponse();
             questionResponse.id(question.getUuid()).content(question.getContent());

             questionDetailsResponses.add(questionResponse);
         }
       return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponses,HttpStatus.OK);

    }
    @RequestMapping(method= RequestMethod.PUT,path="/question/edit/{questionId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity editQuestions(QuestionEditRequest questionEditRequest, @PathVariable("questionId") String questionId,@RequestHeader("authorization") String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = questionBusinessService.getUserByAuth(authorization);

            if(userAuthEntity==null){
                 throw new AuthorizationFailedException("ATHR-001","User has not signed in");
             }

             if(userAuthEntity.getLogout_at()!=null){
                 throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to edit the question");
             }
             QuestionEntity questionEntity= questionBusinessService.getQuestionById(questionId);
             if(questionEntity==null){
                 throw new InvalidQuestionException("QUES-001","Entered question uuid does not exist");
             }
             if(!userAuthEntity.getUser().getUUID().equals(questionEntity.getUserEntity().getUUID())){
                 throw new AuthorizationFailedException("ATHR-003","Only the question owner can edit the question");

             }

             questionEntity.setContent(questionEditRequest.getContent());
             questionBusinessService.updateQuestion(questionEntity);

        QuestionEditResponse questionEditResponse=new QuestionEditResponse().id(questionEntity.getUuid()).status("QUESTION EDITED");
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse,HttpStatus.OK);
    }

        @RequestMapping(method = RequestMethod.DELETE,path="/question/delete/{questionId}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
        public  ResponseEntity<QuestionDeleteResponse> deleteQuestion(@PathVariable("questionId") String questionId, @RequestHeader("authorization") String authorization) throws AuthorizationFailedException, InvalidQuestionException {
             UserAuthEntity userAuthEntity= questionBusinessService.getUserByAuth(authorization);
             if(userAuthEntity==null){
                 throw new AuthorizationFailedException("ATHR-001","User has not signed in");
             }
             if(userAuthEntity.getLogout_at()!=null){
                 throw new AuthorizationFailedException("ATHR-002" ,"User is signed out.Sign in first to delete a question");
             }
             QuestionEntity questionEntity= questionBusinessService.getQuestionById(questionId);
             if(questionEntity==null){
                 throw new InvalidQuestionException("QUES-001" ,"Entered question uuid does not exist");
             }
             if(!(userAuthEntity.getUser().getUUID().equals(questionEntity.getUserEntity().getUUID())) && !(userAuthEntity.getUser().getRole().equals("admin")))
             {
                 throw new AuthorizationFailedException("ATHR-003","Only the question owner or admin can delete the question");
             }

             questionBusinessService.deleteQuestion(questionEntity);
             QuestionDeleteResponse questionDeleteResponse= new QuestionDeleteResponse().id(questionId).status("QUESTION DELETED");

             return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse,HttpStatus.OK);
        }

        @RequestMapping(method=RequestMethod.GET, path="question/all/{userId}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
        public ResponseEntity <List<QuestionDetailsResponse>> getAllQuestionsByUser(@PathVariable("userId")String userId,@RequestHeader("authorization")String authorization) throws AuthorizationFailedException, UserNotFoundException {
             UserAuthEntity userAuthEntity= questionBusinessService.getUserByAuth(authorization);
             if(userAuthEntity==null){
                 throw new AuthorizationFailedException("ATHR-001","User has not signed in");
             }
             if(userAuthEntity.getLogout_at()!=null){
                 throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get all questions posted by a specific user");
             }

            UserEntity userEntity = commonBusinessService.getUserByUserId(userId);
             if(userEntity==null){
                 throw new UserNotFoundException("USR-001" ,"User with entered uuid whose question details are to be seen does not exist");
             }
             List<QuestionEntity> questionEntityList=questionBusinessService.getAllQuestionsByUser(userId);
             List<QuestionDetailsResponse> questionDetailsResponses=new ArrayList<>();
             for(QuestionEntity question:questionEntityList){
                 QuestionDetailsResponse questionDetailsResponse=new QuestionDetailsResponse().id(question.getUuid()).content(question.getContent());
                 questionDetailsResponses.add(questionDetailsResponse);
             }
             return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponses,HttpStatus.OK);
        }


}
