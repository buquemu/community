package buquemu.community.controller;

import buquemu.community.Model.Question;
import buquemu.community.Model.User;
import buquemu.community.dto.PageDTO;
import buquemu.community.dto.QuestionDTO;
import buquemu.community.mapper.QuestionMapper;
import buquemu.community.mapper.UserMapper;
import buquemu.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    QuestionService questionService;

    private User user;
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/")
    public String hello(HttpServletRequest request
                        ,Model model,
                        @RequestParam(name="page",defaultValue = "1") Integer page,
                        @RequestParam(name="size",defaultValue = "2") Integer size
    ){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length!=0)
      //  System.out.println(cookies.toString());
        for (Cookie cs:cookies) {
            if("token".equals(cs.getName())){
                String token = cs.getValue();
                user = userMapper.findBycookie(token);
                if(user!=null) {
                    request.getSession().setAttribute("githubuser", user);
                }
                break;
            }
        }


        PageDTO pagination = questionService.list(page,size);
        model.addAttribute("paginations",pagination);

        return "index";
   }
}
