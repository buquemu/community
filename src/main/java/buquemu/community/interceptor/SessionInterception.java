package buquemu.community.interceptor;

import buquemu.community.model.User;
import buquemu.community.mapper.UserMapper;
import buquemu.community.model.UserExample;
import buquemu.community.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class SessionInterception implements HandlerInterceptor {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NoticeService noticeService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length!=0)
            //  System.out.println(cookies.toString());
            for (Cookie cs:cookies) {
                if("token".equals(cs.getName())){
                    String token = cs.getValue();
                    UserExample userExample = new UserExample();
                    userExample.createCriteria().andTokenEqualTo(token);
                    List<User> users = userMapper.selectByExample(userExample);
                    if(users.size()!=0) {
                        request.getSession().setAttribute("githubuser", users.get(0));
                        int unreadCount = noticeService.unreadCount(users.get(0).getId());
                        request.getSession().setAttribute("unReadCount",unreadCount);
                    }
                    break;
                }
            }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
