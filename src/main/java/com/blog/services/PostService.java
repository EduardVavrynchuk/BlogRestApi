package com.blog.services;

import com.blog.db.entities.Post;
import com.blog.db.entities.User;
import com.blog.db.repositories.PostRepository;
import com.blog.webapp.transferobject.PostDto;
import com.blog.webapp.transferobject.ResponsePost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.blog.utils.ObjectGenerator.generateListOfPosts;
import static com.blog.utils.ObjectGenerator.generatePost;

@Service
public class PostService {

    private static final Logger logger = LogManager.getLogger(PostService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PostRepository postRepository;

    public ResponsePost createNewPost(PostDto postDto, HttpServletRequest request) {
        User ownerPost = userService.getUserByServletRequest(request);

        if (ownerPost == null) {
            logger.warn("User not found!");
            return null;
        }

        Post userPost = new Post()
                .setTitle(postDto.getTitle())
                .setBody(postDto.getBodyOfPost())
                .setCreationDate(new Date())
                .setOwner(ownerPost);

        userPost = postRepository.save(userPost);

        logger.info("Post for user: " + ownerPost.getUsername() + " was created!");
        return generatePost(userPost);
    }

    public ResponsePost updatePost(Long postId, PostDto postDto, HttpServletRequest request) {
        User user = userService.getUserByServletRequest(request);

        if (user == null) {
            logger.warn("User not found");
            return null;
        }

        Post post = postRepository.findByIdAndPostStatusNot(postId, Post.PostStatus.REMOVED);

        if (post == null) {
            logger.warn("Post not found!");
            return null;
        }

        if (userIsNotOwnerOfPost(user, post)) {
            logger.warn("User is not owner of post!");
            return null;
        }

        post.setTitle(postDto.getTitle())
                .setBody(postDto.getBodyOfPost())
                .setUpdateDate(new Date())
                .setPostStatus(Post.PostStatus.UPDATED);

        post = postRepository.save(post);

        logger.info("Post with id: " + postId + " was updated");
        return generatePost(post);

    }

    public ResponsePost deletePost(Long postId, HttpServletRequest request) {
        User user = userService.getUserByServletRequest(request);

        if (user == null) {
            logger.warn("User not found");
            return null;
        }

        Post post = postRepository.findById(postId).orElse(null);

        if (post == null) {
            logger.warn("Post not found!");
            return null;
        }

        if (userIsNotOwnerOfPost(user, post)) {
            logger.warn("User is not owner of post!");
            return null;
        }

        post.setPostStatus(Post.PostStatus.REMOVED);
        post = postRepository.save(post);

        logger.info("Post with id: " + postId + " was removed");
        return generatePost(post);
    }

    public Page<ResponsePost> getPosts(boolean loadOwnPosts, HttpServletRequest request, Pageable pageable) {
        if (loadOwnPosts) {
            User user = userService.getUserByServletRequest(request);

            if (user == null) {
                logger.warn("User not found");
                return null;
            }

            return generateListOfPosts(postRepository.findAllByOwnerAndPostStatusNot(user, Post.PostStatus.REMOVED, pageable), pageable);

        } else {
            return generateListOfPosts(postRepository.findAllByPostStatusNot(Post.PostStatus.REMOVED, pageable), pageable);
        }
    }

    public ResponsePost getPostById(Long postId) {
        return generatePost(postRepository.findByIdAndPostStatusNot(postId, Post.PostStatus.REMOVED));
    }

    private boolean userIsNotOwnerOfPost(User user, Post post) {
        return !post.getOwner().getId().equals(user.getId());
    }
}
