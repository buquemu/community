package buquemu.community.kit;

import buquemu.community.dto.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class OkHttp {
    public String getAccessToken(AccessToken accessToken){
            MediaType mediaType
                    = MediaType.get("application/json; charset=utf-8");

            OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(accessToken));
                Request request = new Request.Builder()
                        .url("https://github.com/login/oauth/access_token")
                        .post(body)
                        .build();
                try (Response response = client.newCall(request).execute()) {
                    String string = response.body().string();
                   // System.out.println(string); 只要accession_token
                    String s1 = string.split("&")[0];
                    String s2 = s1.split("=")[1];
                    return s2;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
        }

        //get提交  https://api.github.com/user USE API 传过去的是携带accessToken的USE方法
        public GithubUser getUser(String accessToken){
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.github.com/user?access_token="+accessToken)
                    .build();

                try (Response response = client.newCall(request).execute()) {
                    String string =  response.body().string();
                    GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
                    return githubUser;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

        }


    public String getQQAccessToken(String code){

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id=101866996&client_secret=1b9e6bdccfe2ffc3afa507a725f291c5&code="+code+"&redirect_uri=http://buquemu.cn/callback/QQ")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String string =  response.body().string();
            String s1 = string.split("&")[0];
            String accessToken = s1.split("=")[1];
            return accessToken;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    public String getOpenID(String qqAccessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://graph.qq.com/oauth2.0/me?access_token="+qqAccessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String jsonString = string.split(" ")[1].split(" ")[0];
            JSONObject obj = JSONObject.parseObject(jsonString);
            String openid = obj.getString("openid");

            return openid;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public QQUserDTO getQQUser(String qqAccessToken, String openid) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://graph.qq.com/user/get_user_info?access_token="+qqAccessToken+"&openid="+openid+"&oauth_consumer_key=101866996")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            QQUserDTO qqUserDTO = JSON.parseObject(string, QQUserDTO.class);
            qqUserDTO.setOpenId(openid);
            return qqUserDTO;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
