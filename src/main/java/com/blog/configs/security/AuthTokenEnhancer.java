package com.blog.configs.security;

import com.blog.configs.security.entity.AuthUserDetails;
import com.blog.configs.security.exceptions.Oauth2ExceptionHandler;
import com.blog.db.entities.User;
import com.blog.db.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class AuthTokenEnhancer implements TokenEnhancer {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsernameAndPassword(
                userDetails.getUsername(), userDetails.getPassword());

        if (user != null) {
            Map<String, Object> additionalInfo = new HashMap<>();
            additionalInfo.put("user_id", user.getId());

            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

            return accessToken;
        } else {
            throw new Oauth2ExceptionHandler("You have entered the wrong credentials\n" +
                    "Please check your entry and try again");
        }
    }

}