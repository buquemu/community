package buquemu.community.controller;

import buquemu.community.dto.AccessToken;
import buquemu.community.dto.GithubUser;
import buquemu.community.kit.OkHttp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorController {
    @Autowired
    private OkHttp okHttp;
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
                           @RequestParam("state") String state
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
        GithubUser user = okHttp.getUser(accessToken1);
        System.out.println(user.getName());
        return "index";
    }
}

