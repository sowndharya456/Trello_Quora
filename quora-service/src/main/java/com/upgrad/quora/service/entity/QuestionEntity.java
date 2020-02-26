package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name="QUESTION")
@NamedQueries({
        @NamedQuery(name="getAllquestion",query="select q from QuestionEntity q"),
        @NamedQuery(name="getQuestionById",query="select q from QuestionEntity q where q.uuid=:uuid"),
        @NamedQuery(name="getAllQuestionsByUser",query="Select q from QuestionEntity q where q.userEntity.UUID=:userid")

})
public class QuestionEntity implements Serializable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "uuid")
    @Size(max=200)
    private String uuid;

    @Column(name="content")
    @Size(max=500)
    private String content;

    @Column(name="date")
    private ZonedDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public QuestionEntity() {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}

