package com.blog.services;

import com.blog.db.entities.Role;
import com.blog.db.entities.User;
import com.blog.db.repositories.RoleRepository;
import com.blog.db.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.blog.utils.VariableUtils.PUBLISHER_ROLE;

@Service
@Transactional
public class SignupService {

    private static final Logger log = LogManager.getLogger(SignupService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;


    public User addUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            log.warn("User with same username: " + user.getUsername() + " is already exists");
            return null;
        }

        Role publisherRole = roleRepository.findByName(PUBLISHER_ROLE);
        user.setRole(publisherRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        log.info("User was created!");

        return userRepository.save(user);
    }

}
