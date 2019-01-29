package com.blog.webapp.transferobject;

public class PostDto {

    private String bodyOfPost;
    private String title;

    public String getTitle() {
        return title;
    }

    public PostDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBodyOfPost() {
        return bodyOfPost;
    }

    public PostDto setBodyOfPost(String bodyOfPost) {
        this.bodyOfPost = bodyOfPost;
        return this;
    }
}
