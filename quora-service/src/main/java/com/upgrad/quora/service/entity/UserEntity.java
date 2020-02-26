package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name="USERS")
@NamedQueries({
        @NamedQuery(name = "userByEmail", query = "select u from UserEntity u where u.email=:email"),
        @NamedQuery(name="userByUsername",query="select u from UserEntity u where u.username=:username"),
        @NamedQuery(name="userByUserId",query="select u from UserEntity u where u.UUID=:uuid")
})
public class UserEntity implements Serializable {

   @Id
   @Column(name="ID")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

   @Column(name="UUID")
   @Size(max =200)
    private String UUID;

   @Column(name="firstname")
   @Size(max=30)
   private String first_name;

   @Column(name="lastname")
   @Size(max=30)
   private String last_name;

    @Column(name="username")
    @Size(max=30)
    private String username;

    @Column(name="email")
    @Size(max=50)
    private String email;

    @Column(name="password")
    @Size(max=255)
    private String password;

    @Column(name="salt")
    @Size(max=200)
    private String salt;

    @Column(name="country")
    @Size(max=30)
    private String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(name="aboutme")
    @Size(max=50)
    private String aboutme;

    @Column(name="dob")
    @Size(max=30)
    private String dob;

    @Column(name="role")
    @Size(max=30)
    private String role;

    @Column(name="contactnumber")
    @Size(max=30)
    private String contactnumber;

    public UserEntity() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }
}
