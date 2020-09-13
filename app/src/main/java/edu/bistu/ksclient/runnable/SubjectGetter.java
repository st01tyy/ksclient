package edu.bistu.ksclient.runnable;

import android.os.Message;
import android.telephony.mbms.MbmsErrors;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import edu.bistu.ksclient.Memory;
import edu.bistu.ksclient.model.Subject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SubjectGetter implements Runnable
{
    @Override
    public void run()
    {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url("http://" + Memory.serverIP + ":" + Memory.serverApiPort + "/get_subjects");
        requestBuilder.get();

        Request request = requestBuilder.build();

        OkHttpClient okHttpClient = new OkHttpClient();
        try
        {
            Response response = okHttpClient.newCall(request).execute();
            String json = response.body().string();
            Gson gson = new Gson();
            List<Subject> subjectList = gson.fromJson(json, new TypeToken<List<Subject>>(){}.getType());
            if(subjectList == null)
            {
                Log.e(getClass().getName(), "subjectList为空");
                throw new Exception();
            }
            else
            {
                Memory.subjects = subjectList.toArray(new Subject[1]);
                Message message = new Message();
                message.what = 2;
                Memory.currentActivity.receiveMessage(message);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Memory.bugOccurred("获取科目信息失败" + e.getMessage());
        }
    }
}
