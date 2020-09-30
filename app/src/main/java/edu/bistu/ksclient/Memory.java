package edu.bistu.ksclient;

import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;

import edu.bistu.ksclient.automata.Automata;
import edu.bistu.ksclient.automata.Event;
import edu.bistu.ksclient.model.Subject;
import edu.bistu.ksclient.model.User;
import edu.bistu.ksclient.network.NetworkService;
import edu.bistu.ksclient.network.ServerMessage;

public class Memory
{
    public static Thread automataThread;
    public static Automata automata;

    public static Thread networkServiceThread;
    public static NetworkService networkService;
    public static Long selectedSubjectID;

    public static CustomActivity currentActivity;

    public static String serverIP = "182.92.202.209";

    public final static String serverApiPort = "8080";
    public final static int serverSocketPort = 2333;

    public static User user;
    public static Subject[] subjects;

    public static void initialize()
    {
        Log.d(Memory.class.getName(), "内存初始化");

        automata = new Automata();
        automataThread = new Thread(automata, "automata-thread");

        user =  null;
        subjects = null;

        selectedSubjectID = null;

        automataThread.start();
    }

    public static void setCurrentActivity(CustomActivity activity)
    {
        currentActivity = activity;
    }

    public static void shutdown()
    {
        Log.d(Memory.class.getName(), "开始销毁内存");

        if(Memory.networkService != null)
            Memory.shutdownNetworkService();

        Event event = new Event(-1, null, null);
        automata.receiveEvent(event);

        try
        {
            automataThread.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        Log.d(Memory.class.getName(), "销毁内存结束");
    }

    public static void bugOccurred(@NotNull String msg)
    {
        Log.e("bugOccurred", msg);
        Message message = new Message();
        message.what = 0;
        message.obj = msg;
        currentActivity.receiveMessage(message);
    }

    public static void startNetworkService()
    {
        networkService = new NetworkService();
        networkServiceThread = new Thread(networkService, "NetworkService");
        networkServiceThread.start();
    }

    public static void shutdownNetworkService()
    {
        Log.d(Memory.class.getName(), "开始终止网络服务");

        ServerMessage serverMessage = ServerMessage.shutdownSignal();
        Memory.networkService.sendMessage(serverMessage);

        try
        {
            networkServiceThread.join();
            networkServiceThread = null;
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        selectedSubjectID = null;
        Log.d(Memory.class.getName(), "网络服务已终止");
    }


}
