package buquemu.community.controller;

import buquemu.community.dto.CommentDTO;
import buquemu.community.dto.CommentPageDTO;
import buquemu.community.dto.QuestionDTO;
import buquemu.community.enums.CommentTypeEnum;
import buquemu.community.model.Question;
import buquemu.community.service.CommentService;
import buquemu.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(
            @PathVariable("id") Integer id,
            Model model) {

        QuestionDTO questionDTO = questionService.getById(id);
        List<CommentDTO> comments = commentService.ListByTargetId(id, CommentTypeEnum.QUESTION.getType());

        List<Question> RelevantQuestion = questionService.RelevantQuestion(questionDTO);
//       阅读数
        questionService.addView(id);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", comments);

        model.addAttribute("RelevantQuestion",RelevantQuestion);
        return "question";
    }
}
