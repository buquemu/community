package buquemu.community.controller;
import buquemu.community.dto.PageDTO;

import buquemu.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class IndexController {
    @Autowired
    QuestionService questionService;

    @GetMapping("/")
    public String hello(Model model,
                        @RequestParam(name="page",defaultValue = "1") Integer page,
                        @RequestParam(name="size",defaultValue = "5") Integer size
    ){
        PageDTO pagination = questionService.list(page,size);
        model.addAttribute("paginations",pagination);
        return "index";
   }
}
