package com.blog.webapp.controllers;

import com.blog.db.entities.User;
import com.blog.services.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private SignupService signupService;

    @Qualifier("consumerTokenServices")
    @Autowired
    private ConsumerTokenServices tokenServices;

    /**
     * Allows users to execute "SignOut" operation, this operation revokes the access token.
     *
     * @param tokenId - access token
     * @return if operation was succeed - 200 Http status code
     * otherwise - 500 Http status code
     */
    @DeleteMapping(value = "/oauth/token/{tokenId}")
    public ResponseEntity<?> deleteToken(@PathVariable String tokenId) {
        if (tokenServices.revokeToken(tokenId)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * Allows registering of the new user.
     *
     * @param user - object that contains necessary data for registering
     * @return the same user-object but with all filled fields
     */
    @PostMapping(value = "/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        User newUser = signupService.addUser(user);

        if (newUser != null) {
            return new ResponseEntity<>(newUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format("User with username %s already exists in database",
                    user.getUsername()), HttpStatus.CONFLICT);
        }
    }
}
