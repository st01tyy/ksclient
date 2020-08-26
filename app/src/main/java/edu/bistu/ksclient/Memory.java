package edu.bistu.ksclient;

import android.util.Log;

import edu.bistu.ksclient.automata.Automata;
import edu.bistu.ksclient.automata.Event;
import edu.bistu.ksclient.model.User;

public class Memory
{
    public static Thread automataThread;
    public static Automata automata;

    public static CustomActivity currentActivity;

    public final static String serverIP = "192.168.0.106";

    public static User user;

    public static void initialize()
    {
        Log.d(Memory.class.getName(), "内存初始化");

        automata = new Automata();
        automataThread = new Thread(automata, "automata-thread");

        user =  null;

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

}
