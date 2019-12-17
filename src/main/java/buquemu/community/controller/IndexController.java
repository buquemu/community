package buquemu.community.controller;

import buquemu.community.Model.User;
import buquemu.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    private User user;
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/")
    public String hello(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
      //  System.out.println(cookies.toString());
        for (Cookie cs:cookies) {
            if("token".equals(cs.getName())){
                String token = cs.getValue();
                user = userMapper.findBycookie(token);
                break;
            }
        }
        if(user!=null) {
            request.getSession().setAttribute("githubuser", user);
        }
        return "index";
   }
}
