package com.blog.db.repositories;

import com.blog.db.entities.Post;
import com.blog.db.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {

    Post findByIdAndPostStatusNot(Long id, Post.PostStatus status);

    Page<Post> findAllByOwnerAndPostStatusNot(User user, Post.PostStatus status, Pageable pageable);

    Page<Post> findAllByPostStatusNot(Post.PostStatus status, Pageable pageable);
}
