package buquemu.community.controller;
import buquemu.community.Scheduled.ScheduledTasks;
import buquemu.community.dto.HotTagDTO;
import buquemu.community.dto.PageDTO;

import buquemu.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class IndexController {
    @Autowired
    QuestionService questionService;
    @Autowired
    ScheduledTasks scheduledTasks;

    @GetMapping("/")
    public String hello(Model model,
                        @RequestParam(name="page",defaultValue = "1") Integer page,
                        @RequestParam(name="size",defaultValue = "6") Integer size,
                        @RequestParam(name="tag",defaultValue = "") String tag
    ){

         PageDTO pagination = questionService.list(page,size,tag);
// 热门标签
        List<HotTagDTO> hotTagDTOS = scheduledTasks.hotTags();

        model.addAttribute("paginations",pagination);
        model.addAttribute("hotTags",hotTagDTOS);

        return "index";
   }
}
