package buquemu.community.service;

import buquemu.community.model.User;
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
    UserMapper userMapper;

    public Boolean author(GithubUser githubuser, HttpServletResponse rsp) {
        if (githubuser != null && githubuser.getId() != null) {
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setAccountId(String.valueOf(githubuser.getId()));
            user.setName(githubuser.getName());
            user.setAvatarUrl(githubuser.getAvatar_url());
            User dbUser = userMapper.findByAccounId(user.getAccountId());
            if (dbUser == null) {
//   没找到        插入
                //当前的毫秒数
                user.setGmtCreate(System.currentTimeMillis());
                user.setGmtModified(user.getGmtCreate());
                userMapper.insert(user);
            } else {
//           更新
                dbUser.setAvatarUrl(user.getAvatarUrl());
                dbUser.setName(user.getName());
                dbUser.setToken(user.getToken());
                dbUser.setGmtModified(System.currentTimeMillis());
                userMapper.upDate(dbUser);
            }
            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(60 * 60 * 24 * 6);
            rsp.addCookie(cookie);
            return true;
        }
        return false;
    }
}
