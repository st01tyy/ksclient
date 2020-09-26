package edu.bistu.ksclient.runnable;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import edu.bistu.ksclient.Memory;
import edu.bistu.ksclient.automata.Event;
import edu.bistu.ksclient.model.GameInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GameInfoGetter implements Runnable
{
    private Integer gameID;

    public GameInfoGetter(Integer gameID)
    {
        this.gameID = gameID;
    }

    @Override
    public void run()
    {
        Log.d(getClass().getName(), "run()");

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url("http://" + Memory.serverIP + ":" + Memory.serverApiPort + "/get_game_info"
                + "?gameID=" + gameID + "&playerID=" + Memory.user.getId());
        requestBuilder.get();

        Request request = requestBuilder.build();

        OkHttpClient okHttpClient = new OkHttpClient();

        try
        {
            Response response = okHttpClient.newCall(request).execute();
            String json = response.body().string();
            Gson gson = new Gson();
            GameInfo gameInfo = gson.fromJson(json, GameInfo.class);
            gameInfo.setGameID(gameID);
            Event event = new Event(8, gameInfo, System.currentTimeMillis());
            Memory.automata.receiveEvent(event);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
