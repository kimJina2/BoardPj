package com.boardproject.comment;

import com.boardproject.comment.dto.CommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {
    private final CommentService commentService;

    @RequestMapping("/create")
    public String create(@Valid CommentRequest.CreateDTO createDTO, Error error) {
        commentService.create(createDTO);
        return "redirect:/board/" + createDTO.getBoardId();
    }

    @RequestMapping("/delete")
    public String delete(@Valid CommentRequest.DeleteDTO deleteDTO, Error error) {
        commentService.delete(deleteDTO);
        return "redirect:/board/" + deleteDTO.getBoardId();
    }
}
