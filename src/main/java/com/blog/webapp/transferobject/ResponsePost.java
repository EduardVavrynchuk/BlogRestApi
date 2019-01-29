package com.blog.webapp.transferobject;

public class ResponsePost {

    private Long id;

    private String title;

    private String body;

    private String creationDate;

    private String updateDate;

    private String postStatus;

    private String owner;

    public Long getId() {
        return id;
    }

    public ResponsePost setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ResponsePost setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBody() {
        return body;
    }

    public ResponsePost setBody(String body) {
        this.body = body;
        return this;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public ResponsePost setCreationDate(String creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public ResponsePost setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public ResponsePost setPostStatus(String postStatus) {
        this.postStatus = postStatus;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public ResponsePost setOwner(String owner) {
        this.owner = owner;
        return this;
    }

}
