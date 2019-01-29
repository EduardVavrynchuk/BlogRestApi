package com.blog.utils;

import com.blog.db.entities.Post;
import com.blog.webapp.transferobject.ResponsePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static com.blog.utils.DateUtils.formatFullDate;

public class ObjectGenerator {

    public static String generatePostStatus(Post post) {

        if (post.getPostStatus() == null)
            return "New";

        switch (post.getPostStatus()) {
            case NEW: {
                return "New";
            }
            case REMOVED: {
                return "Removed";
            }
            case UPDATED: {
                return "Updated";
            }

            default:
                return null;
        }
    }

    public static ResponsePost generatePost(Post post) {
        if (post == null)
            return null;

        return new ResponsePost()
                .setId(post.getId())
                .setTitle(post.getTitle())
                .setBody(post.getBody())
                .setCreationDate(formatFullDate(post.getCreationDate()))
                .setUpdateDate(formatFullDate(post.getUpdateDate()))
                .setOwner(post.getOwner().getUsername())
                .setPostStatus(generatePostStatus(post));
    }

    public static Page<ResponsePost> generateListOfPosts(Page<Post> posts, Pageable pageable) {
        if (posts == null)
            return null;

        List<ResponsePost> responsePostList = new ArrayList<>();

        posts.forEach(post -> {
            ResponsePost responsePost = new ResponsePost()
                    .setId(post.getId())
                    .setTitle(post.getTitle())
                    .setCreationDate(formatFullDate(post.getCreationDate()))
                    .setOwner(post.getOwner().getUsername())
                    .setPostStatus(generatePostStatus(post));

            responsePostList.add(responsePost);
        });

        return new PageImpl<>(responsePostList, pageable, posts.getTotalElements());
    }
}
