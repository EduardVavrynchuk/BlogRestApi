package com.blog.webapp.controllers;

import com.blog.services.PostService;
import com.blog.webapp.transferobject.PostDto;
import com.blog.webapp.transferobject.ResponsePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * Allows users to create new post
     *
     * @param postDto - object which represent post info
     * @return if operation was succeed - 200 Http status code
     * otherwise - 500 Http status code
     */
    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public ResponseEntity<?> createPost(@RequestBody PostDto postDto, HttpServletRequest httpServletRequest) {
        ResponsePost post = postService.createNewPost(postDto, httpServletRequest);

        if (post == null) {
            return new ResponseEntity<>("Post was not created", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    /**
     * Allows users to update an existing post
     *
     * @param postId - id of post which must be updated
     * @param postDto - object which represent new post info
     * @return if operation was succeed - 200 Http status code
     * otherwise - 500 Http status code
     */
    @RequestMapping(value = "/post", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePost(@RequestParam("post_id") Long postId, @RequestBody PostDto postDto, HttpServletRequest httpServletRequest) {
        ResponsePost post = postService.updatePost(postId, postDto, httpServletRequest);

        if (post == null) {
            return new ResponseEntity<>("Post was not updated", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(post, HttpStatus.OK);

    }

    /**
     * Allows users to delete an existing post
     *
     * @param postId - id of post which must be deleted
     * @return if operation was succeed - 200 Http status code
     * otherwise - 500 Http status code
     */
    @RequestMapping(value = "/post", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePost(@RequestParam("post_id") Long postId, HttpServletRequest httpServletRequest) {
        ResponsePost post = postService.deletePost(postId, httpServletRequest);

        if (post == null) {
            return new ResponseEntity<>("Post was not removed", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(post, HttpStatus.OK);

    }

    /**
     * Allows users to get all posts
     *
     * @param ownPosts - filter which load all/own posts
     * @param page - number of the page
     * @param size - amount posts on page
     * @return if operation was succeed - 200 Http status code
     */
    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    public ResponseEntity<?> getPosts(
            @RequestParam("own_posts") boolean ownPosts,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            HttpServletRequest httpServletRequest) {
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "id"));
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ResponsePost> posts = postService.getPosts(ownPosts, httpServletRequest, pageable);

        if (posts == null) {
            return new ResponseEntity<>("Posts not found", HttpStatus.OK);
        }

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    /**
     * Allows users to load post by id
     *
     * @param postId - d of post which must be loaded
     * @return if operation was succeed - 200 Http status code
     * otherwise - 400 Http status code
     */
    @RequestMapping(value = "/post", method = RequestMethod.GET)
    public ResponseEntity<?> getPostById(@RequestParam("post_id") Long postId) {
        ResponsePost post = postService.getPostById(postId);

        if (post == null)
            return new ResponseEntity<>("Post not found!", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(post, HttpStatus.OK);
    }

}
