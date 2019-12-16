package buquemu.community.controller;

import buquemu.community.Model.User;
import buquemu.community.dto.AccessToken;
import buquemu.community.dto.GithubUser;
import buquemu.community.kit.OkHttp;
import buquemu.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


@Controller
public class AuthorController {
    @Autowired
    private OkHttp okHttp;
    @Autowired
    private UserMapper userMapper;
    //通过配置文件方式注入
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.redirect.uri}")
    private String redirect_uri;
    @Value("${github.client.secret}")
    private String clientSecret;
    //授权登录
    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code,
                           @RequestParam("state") String state,
                           HttpServletRequest req, HttpServletResponse rsp
                           ){
        AccessToken accessToken = new AccessToken();
        accessToken.setClient_id(clientId);
        accessToken.setRedirect_uri(redirect_uri);
        accessToken.setClient_secret(clientSecret);
        accessToken.setCode(code);
        accessToken.setState(state);
        //发送post请求 获取access-token
        //POST https://github.com/login/oauth/access_token
        String accessToken1 = okHttp.getAccessToken(accessToken);
        //发送get请求 携带accessToken 获取User信息
        GithubUser githubuser = okHttp.getUser(accessToken1);

        //登录 cookie和session
        if(githubuser!=null){
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setAccountId(String.valueOf(githubuser.getId()));
            user.setName(githubuser.getName());
            //当前的毫秒数
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            rsp.addCookie(new Cookie("token",token));


            //这有个坑 /
            return "redirect:/";
        }else{
            return "redirect:/";
        }
    }
}

