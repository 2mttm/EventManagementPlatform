package me.twometrue.eventmanager.services;

import me.twometrue.eventmanager.models.Comment;
import me.twometrue.eventmanager.models.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
    public Comment save(Comment comment){
        comment.setCreationTime(LocalDateTime.now());
        return commentRepository.save(comment);
    }
}
