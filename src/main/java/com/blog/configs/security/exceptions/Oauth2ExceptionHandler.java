package com.blog.configs.security.exceptions;

import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;

public class Oauth2ExceptionHandler extends ClientAuthenticationException {

    public Oauth2ExceptionHandler(String msg, Throwable t) {
        super(msg, t);
    }

    public Oauth2ExceptionHandler(String msg) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "Login exception";
    }
}