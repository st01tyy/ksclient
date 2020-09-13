package edu.bistu.ksclient;

import android.os.Message;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import edu.bistu.ksclient.automata.Automata;
import edu.bistu.ksclient.automata.Event;
import edu.bistu.ksclient.model.Subject;
import edu.bistu.ksclient.model.User;

public class Memory
{
    public static Thread automataThread;
    public static Automata automata;

    public static CustomActivity currentActivity;

    //public final static String serverIP = "192.168.0.106";
    public final static String serverIP = "192.168.2.107";

    public final static String serverApiPort = "8080";

    public static User user;
    public static Subject[] subjects;

    public static void initialize()
    {
        Log.d(Memory.class.getName(), "内存初始化");

        automata = new Automata();
        automataThread = new Thread(automata, "automata-thread");

        user =  null;
        subjects = null;

        automataThread.start();
    }

    public static void setCurrentActivity(CustomActivity activity)
    {
        currentActivity = activity;
    }

    public static void shutdown()
    {
        Log.d(Memory.class.getName(), "开始销毁内存");

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

}
