package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "docname")
    private String docName;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "uploaddate")
    private Date uploadDate;

    @Column(name = "updatedate")
    private Date updateDate;

    public Document() {
    }

    public Document(long id, String docName, User user, Date uploadDate, Date updateDate) {
        this.id = id;
        this.docName = docName;
        this.user = user;
        this.uploadDate = uploadDate;
        this.updateDate = updateDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
