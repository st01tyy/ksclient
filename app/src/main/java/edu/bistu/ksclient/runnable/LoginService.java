package edu.bistu.ksclient.runnable;

import android.os.Message;

import com.google.gson.Gson;

import java.io.IOException;

import edu.bistu.ksclient.Memory;
import edu.bistu.ksclient.model.LoginResult;
import edu.bistu.ksclient.model.User;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginService implements Runnable
{
    private Long id;
    private String pw;

    public LoginService(Long id, String pw)
    {
        this.id = id;
        this.pw = pw;
    }

    @Override
    public void run()
    {
        FormBody.Builder requestBodyBuilder = new FormBody.Builder();
        requestBodyBuilder.add("id", String.valueOf(id));
        requestBodyBuilder.add("pw", pw);

        RequestBody requestBody = requestBodyBuilder.build();

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url("http://" + Memory.serverIP + "/login");
        requestBuilder.post(requestBody);

        Request request = requestBuilder.build();

        OkHttpClient okHttpClient = new OkHttpClient();

        Message message = new Message();
        try
        {
            Response response = okHttpClient.newCall(request).execute();
            String responseData = response.body().string();
            Gson gson = new Gson();
            LoginResult loginResult = gson.fromJson(responseData, LoginResult.class);
            if(loginResult != null)
            {
                if(loginResult.getResult() == 100)
                {
                    /* 登录成功 */
                    User user = loginResult.getUser();
                    if(user == null)
                        throw new Exception("user为空");
                    Memory.user = loginResult.getUser();
                }
            }
            else
                throw new Exception("loginResult为空");
        }
        catch (IOException e)
        {
            /* execute()方法触发 */
            e.printStackTrace();
        }
        catch (Exception e)
        {
            /* 捕获json字符串转换时可能发生的异常 */
            e.printStackTrace();
        }
    }
}
