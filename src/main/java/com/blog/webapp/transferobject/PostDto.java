package com.blog.webapp.transferobject;

public class PostDto {

    private String bodyOfPost;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBodyOfPost() {
        return bodyOfPost;
    }

    public void setBodyOfPost(String bodyOfPost) {
        this.bodyOfPost = bodyOfPost;
    }
}
