package buquemu.community.controller;

import buquemu.community.Scheduled.ScheduledTasks;
import buquemu.community.dto.HotTagDTO;
import buquemu.community.dto.QuestionDTO;
import buquemu.community.model.Question;

import buquemu.community.service.ESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {
    @Autowired
    private ESService searchService;
    @Autowired
    ScheduledTasks scheduledTasks;
//    搜索功能
    @GetMapping("/search")
    public String search(
            Model model,
            @RequestParam("query")String text,
                         @RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "6") Integer rows) {

//查询的question
        List<QuestionDTO> questions = searchService.search(text, page, rows);

//热门标签
        List<HotTagDTO> hotTagDTOS = scheduledTasks.hotTags();
        model.addAttribute("hotTags",hotTagDTOS);
        model.addAttribute("paginations",questions);
        return "search";
    }
}
