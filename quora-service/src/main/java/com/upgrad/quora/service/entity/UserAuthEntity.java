package com.upgrad.quora.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name="USER_AUTH")
@NamedQuery(name = "userAuthByAccessToken", query = "select u from UserAuthEntity u where u.access_token=:access_token")
public class UserAuthEntity implements Serializable {

@Id
@Column(name="ID")
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long id;

@Column(name="uuid")
@Size(max=200)
private String uuid;

@ManyToOne
@OnDelete(action = OnDeleteAction.CASCADE )
@JoinColumn(name="user_id")
    private UserEntity user;

@Column(name="access_token")
@Size(max=500)
    private String access_token;

@Column(name="expires_at")
    private ZonedDateTime expires_at;

@Column(name="login_at")
    private ZonedDateTime login_at;

@Column(name="logout_at")
    private ZonedDateTime logout_at;

    public UserAuthEntity() {
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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public ZonedDateTime getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(ZonedDateTime expires_at) {
        this.expires_at = expires_at;
    }

    public ZonedDateTime getLogin_at() {
        return login_at;
    }

    public void setLogin_at(ZonedDateTime login_at) {
        this.login_at = login_at;
    }

    public ZonedDateTime getLogout_at() {
        return logout_at;
    }

    public void setLogout_at(ZonedDateTime logout_at) {
        this.logout_at = logout_at;
    }
}
