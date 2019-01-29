package com.blog.services;

import com.blog.db.entities.User;
import com.blog.db.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public User getUserByServletRequest(HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getUserPrincipal() == null) {
            logger.warn("Principal for getting user is null!");
            return null;
        }

        return userRepository.findByUsername(httpServletRequest.getUserPrincipal().getName());
    }

}
