package edu.bistu.ksclient.runnable;

import com.google.gson.Gson;

import java.io.IOException;

import edu.bistu.ksclient.Memory;
import edu.bistu.ksclient.model.LogoutRequest;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class LogoutService implements Runnable
{
    @Override
    public void run()
    {
        if(Memory.user == null)
            return;

        Gson gson = new Gson();
        LogoutRequest logoutRequest = new LogoutRequest(Memory.user.getId(), Memory.user.getToken());
        String json = gson.toJson(logoutRequest);

        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url("http://" + Memory.serverIP + ":" + Memory.serverApiPort + "/logout");
        requestBuilder.post(requestBody);

        Request request = requestBuilder.build();

        OkHttpClient okHttpClient = new OkHttpClient();
        try
        {
            okHttpClient.newCall(request).execute();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
