package com.blog.services;

import com.blog.TestConfiguration;
import com.blog.db.entities.Post;
import com.blog.db.entities.User;
import com.blog.db.repositories.PostRepository;
import com.blog.db.repositories.UserRepository;
import com.blog.webapp.transferobject.PostDto;
import com.blog.webapp.transferobject.ResponsePost;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.http.HttpServletRequest;

import java.security.Principal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@TestPropertySource("classpath:test.properties")
@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private SignupService signupService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @MockBean
    private Principal principal;

    private User user;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        User user = new User()
                .setUsername("TestUser" + System.currentTimeMillis())
                .setPassword("SomePassword");

        this.user = signupService.addUser(user);
        assertNotNull(user);
    }

    @After
    public void tearDown() throws Exception {
        if (user != null) {
            postRepository.deleteAll();
            userRepository.delete(user);
        }
    }

    @Test
    public void createNewPost() {
        // Mock HttpServletRequest
        HttpServletRequest request = mock(HttpServletRequest.class);

        // Mock Principal
        Mockito.when(principal.getName()).thenReturn(user.getUsername());
        Mockito.when(request.getUserPrincipal()).thenReturn(principal);

        PostDto postDto = new PostDto()
                .setBodyOfPost("SomeBody")
                .setTitle("SomeTitle");

        long userAmount = postRepository.count();
        ResponsePost responsePost = postService.createNewPost(postDto, request);
        assertNotNull(responsePost);

        assertEquals(userAmount + 1, postRepository.count());
    }

    @Test
    public void updatePost() {
        // Mock HttpServletRequest
        HttpServletRequest request = mock(HttpServletRequest.class);

        // Mock Principal
        Mockito.when(principal.getName()).thenReturn(user.getUsername());
        Mockito.when(request.getUserPrincipal()).thenReturn(principal);

        PostDto postDto = new PostDto()
                .setBodyOfPost("SomeBody")
                .setTitle("SomeTitle");

        ResponsePost responsePost = postService.createNewPost(postDto, request);
        assertNotNull(responsePost);

        postDto.setTitle("Updated post");
        responsePost = postService.updatePost(responsePost.getId(), postDto, request);
        assertNotNull(responsePost);

        Post updatedPost = postRepository.findById(responsePost.getId()).orElse(null);
        assertNotNull(updatedPost);

        assertEquals(Post.PostStatus.UPDATED, updatedPost.getPostStatus());

        // create second user and try to change post, expect 'User is not owner of post' and returned value = null
        User user = new User()
                .setUsername("TestUser" + System.currentTimeMillis())
                .setPassword("SomePassword");

        user = signupService.addUser(user);

        Mockito.when(principal.getName()).thenReturn(user.getUsername());
        responsePost = postService.updatePost(responsePost.getId(), postDto, request);
        assertNull(responsePost);

        userRepository.delete(user);
    }

    @Test
    public void deletePost() {
        // Mock HttpServletRequest
        HttpServletRequest request = mock(HttpServletRequest.class);

        // Mock Principal
        Mockito.when(principal.getName()).thenReturn(user.getUsername());
        Mockito.when(request.getUserPrincipal()).thenReturn(principal);

        PostDto postDto = new PostDto()
                .setBodyOfPost("SomeBody")
                .setTitle("SomeTitle");

        ResponsePost responsePost = postService.createNewPost(postDto, request);
        assertNotNull(responsePost);

        long postId = responsePost.getId();

        // create second user and try to delete post, expect 'User is not owner of post' and returned value = null
        User user = new User()
                .setUsername("TestUser" + System.currentTimeMillis())
                .setPassword("SomePassword");

        user = signupService.addUser(user);

        Mockito.when(principal.getName()).thenReturn(user.getUsername());
        responsePost = postService.deletePost(postId, request);
        assertNull(responsePost);

        userRepository.delete(user);

        // delete posy by owner
        Mockito.when(principal.getName()).thenReturn(this.user.getUsername());
        responsePost = postService.deletePost(postId, request);
        assertNotNull(responsePost);

        Post deletedPost = postRepository.findById(postId).orElse(null);
        assertNotNull(deletedPost);

        assertEquals(Post.PostStatus.REMOVED, deletedPost.getPostStatus());

    }

    @Test
    public void getPosts() {
        final long amountAddedDocuments = 3;
        final int page = 0;
        final int size = 5;

        // Mock HttpServletRequest
        HttpServletRequest request = mock(HttpServletRequest.class);

        // Mock Principal
        Mockito.when(principal.getName()).thenReturn(user.getUsername());
        Mockito.when(request.getUserPrincipal()).thenReturn(principal);

        PostDto postDto = new PostDto()
                .setBodyOfPost("SomeBody")
                .setTitle("SomeTitle");

        long amountPosts = postRepository.count();

        for (int i = 0; i < amountAddedDocuments; i++) {
            ResponsePost responsePost = postService.createNewPost(postDto, request);
            assertNotNull(responsePost);
        }

        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "id"));
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ResponsePost> posts = postService.getPosts(false, request, pageable);
        assert posts.getTotalElements() >= amountAddedDocuments;

        assertEquals(amountPosts + amountAddedDocuments, postRepository.count());

        posts = postService.getPosts(true, request, pageable);
        assertEquals(amountAddedDocuments, posts.getTotalElements());
    }

    @Test
    public void getPostById() {
        // Mock HttpServletRequest
        HttpServletRequest request = mock(HttpServletRequest.class);

        // Mock Principal
        Mockito.when(principal.getName()).thenReturn(user.getUsername());
        Mockito.when(request.getUserPrincipal()).thenReturn(principal);

        PostDto postDto = new PostDto()
                .setBodyOfPost("SomeBody")
                .setTitle("SomeTitle");

        ResponsePost responsePost = postService.createNewPost(postDto, request);
        assertNotNull(responsePost);

        ResponsePost post = postService.getPostById(responsePost.getId());
        assertNotNull(post);

    }
}