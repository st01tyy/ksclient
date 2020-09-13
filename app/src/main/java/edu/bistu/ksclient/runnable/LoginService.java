package edu.bistu.ksclient.runnable;

import android.os.Message;

import com.google.gson.Gson;

import java.io.IOException;

import edu.bistu.ksclient.Memory;
import edu.bistu.ksclient.automata.Event;
import edu.bistu.ksclient.model.LoginRequest;
import edu.bistu.ksclient.model.LoginResult;
import edu.bistu.ksclient.model.User;
import okhttp3.FormBody;
import okhttp3.MediaType;
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
        LoginRequest loginRequest = new LoginRequest(id, pw);
        Gson gson = new Gson();
        String json = gson.toJson(loginRequest);

        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url("http://" + Memory.serverIP + ":" + Memory.serverApiPort + "/login");
        requestBuilder.post(requestBody);

        Request request = requestBuilder.build();

        OkHttpClient okHttpClient = new OkHttpClient();

        Event event = null;

        try
        {
            Response response = okHttpClient.newCall(request).execute();
            String responseData = response.body().string();
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
                    event = new Event(1, null, System.currentTimeMillis());
                }
                else
                    event = new Event(2, loginResult.getResult(), System.currentTimeMillis());
            }
            else
                throw new Exception("loginResult为空");
        }
        catch (IOException e)
        {
            /* execute()方法触发 */
            e.printStackTrace();
            event = new Event(2, 104, System.currentTimeMillis());
        }
        catch (Exception e)
        {
            /* 捕获json字符串转换时可能发生的异常 */
            e.printStackTrace();
            event = new Event(2, 105, System.currentTimeMillis());
        }
        finally 
        {
            Memory.automata.receiveEvent(event);
        }
    }
}
