package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name="ANSWER")
@NamedQueries({
        @NamedQuery(name="answerByuuid",query="select a from AnswerEntity a where a.uuid=:uuid"),
        @NamedQuery(name="getAllAnswerstoQuestion",query = "select a from AnswerEntity a where a.question.uuid=:questionId")
})
public class AnswerEntity implements Serializable {

    @Id
    @Column(name="Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="uuid")
    @Size(max=200)
    private String uuid;

    @Column(name="ans")
    @Size(max=255)
    private String ans;

    @Column(name="date")
    private ZonedDateTime date;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name="question_id")
    private QuestionEntity question;

    public AnswerEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public QuestionEntity getQuestion() {
        return question;
    }

    public void setQuestion(QuestionEntity question) {
        this.question = question;
    }
}
