package buquemu.community.kit;

import buquemu.community.dto.AccessToken;
import buquemu.community.dto.GithubUser;
import com.alibaba.fastjson.JSON;
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


}
