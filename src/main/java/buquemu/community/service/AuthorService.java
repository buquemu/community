package buquemu.community.service;

import buquemu.community.model.User;
import buquemu.community.dto.GithubUser;
import buquemu.community.mapper.UserMapper;
import buquemu.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
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
            UserExample userExample = new UserExample();
            userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
            List<User> users = userMapper.selectByExample(userExample);
            if (users.size() == 0) {
//   没找到        插入
                //当前的毫秒数
                user.setGmtCreate(System.currentTimeMillis());
                user.setGmtModified(user.getGmtCreate());
                userMapper.insert(user);
            } else {
//           更新
                User dbUser = users.get(0);
                User updateUser = new User();
                updateUser.setAvatarUrl(user.getAvatarUrl());
                updateUser.setName(user.getName());
                updateUser.setToken(user.getToken());
                updateUser.setGmtModified(System.currentTimeMillis());

                UserExample example = new UserExample();
                example.createCriteria().andIdEqualTo(dbUser.getId());
                userMapper.updateByExampleSelective(updateUser, example);
            }
            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(60 * 60 * 24 * 6);
            rsp.addCookie(cookie);
            return true;
        }
        return false;
    }
}
