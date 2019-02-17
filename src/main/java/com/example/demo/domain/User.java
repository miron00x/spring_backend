package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "username")
    private String userName;

    @JsonIgnore
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Document> documents = new ArrayList<>();

    public User() {
    }

    public User(long id, String userName, String password, List<Document> documents, Role role) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.documents = documents;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
