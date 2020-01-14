package buquemu.community.service;

import buquemu.community.Model.User;
import buquemu.community.dto.GithubUser;
import buquemu.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
public class AuthorService {
    @Autowired
    private UserMapper userMapper;
    public Boolean author(GithubUser githubuser, HttpServletResponse rsp){
        if(githubuser!=null) {
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setAccountId(String.valueOf(githubuser.getId()));
            user.setName(githubuser.getName());
            //当前的毫秒数
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubuser.getAvatar_url());
            userMapper.insert(user);
            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(60*60*24*6);
            rsp.addCookie(cookie);
            return true;
        }
        return false;
    }
}
