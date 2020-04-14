package buquemu.community.controller;

import buquemu.community.dto.AccessToken;
import buquemu.community.dto.GithubUser;
import buquemu.community.dto.QQOpenId;
import buquemu.community.dto.QQUserDTO;
import buquemu.community.kit.OkHttp;
import buquemu.community.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.client.RestTemplate;

@Controller
public class AuthorController {
    @Autowired
    private OkHttp okHttp;
    @Autowired
    private AuthorService authorService;
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
                           HttpServletResponse rsp//HttpServletRequest req
                           ){
        System.out.println(code);
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
       // req.getSession().setAttribute("githubuser",githubuser);
        if(authorService.author(githubuser,rsp)==true){
            return "redirect:/";
        }
        else {
            return "redirect:/";
        }
    }

    //QQ授权登录
    @RequestMapping("/callback/QQ")
    public String QQCallback(
            HttpServletRequest request,
            HttpServletResponse rsp
    ){
        String code = request.getParameter("code");
//        String format = String.format("https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id=101866996&client_secret=1b9e6bdccfe2ffc3afa507a725f291c5&code=" + code + "+&redirect_uri=https://buquemu.cn/callback/QQ");
//        1.   get 请求通过授权码获取令牌 返回是字符串
       String qqAccessToken = okHttp.getQQAccessToken(code);
//        RestTemplate restTemplate = new RestTemplate();
//        String string = restTemplate.getForObject(format, String.class);
//        String[] split = string.split("&");
//        String[] split1 = split[0].split("=");
//        String qqAccessToken = split1[1];


//        2. 根据令牌 获取OpenID
        String openid = okHttp.getOpenID(qqAccessToken);
//        3.根据 openId 和 AccessToken调用API
        QQUserDTO qqUser = okHttp.getQQUser(qqAccessToken, openid);
        if(authorService.QQauthor(qqUser,rsp)==true){
            return "redirect:/";
        }
        else {
            return "redirect:/";
        }

    }





    @GetMapping("/logout")
    public String logOut(HttpServletRequest request,HttpServletResponse response){
        request.getSession().removeAttribute("githubuser");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }


}



