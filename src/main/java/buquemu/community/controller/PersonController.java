package buquemu.community.controller;

import buquemu.community.model.User;
import buquemu.community.dto.PageDTO;
import buquemu.community.service.NoticeService;
import buquemu.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PersonController {

    @Autowired
    QuestionService questionService;
    @Autowired
    private NoticeService noticeService;

    @GetMapping("/profile/{action}")
    public String profile(
            HttpServletRequest request,
            @PathVariable(name = "action")String action,
            Model model,
            @RequestParam(name="page",defaultValue = "1") Integer page,
            @RequestParam(name="size",defaultValue = "5") Integer size
    ){
        User user = (User) request.getSession().getAttribute("githubuser");
        if(user==null){
            model.addAttribute("error","用户未登录");
            return "redirect:/";
        }

        if("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
            PageDTO pagination = questionService.find(user.getId(), page, size);
            model.addAttribute("paginations",pagination);
        }else if("replies".equals(action)){

//            需要在模型放问题和回复者
            PageDTO pagination = noticeService.find(user.getId(),page, size);
            int unreadCount = noticeService.unreadCount(user.getId());
            model.addAttribute("section","replies");
            model.addAttribute("paginations",pagination);
            model.addAttribute("sectionName","最新回复");

        }
        return "person";
    }
}
