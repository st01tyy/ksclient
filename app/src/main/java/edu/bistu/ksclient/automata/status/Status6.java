package edu.bistu.ksclient.automata.status;

import android.os.Message;
import android.util.Log;

import java.util.PriorityQueue;

import edu.bistu.ksclient.Memory;
import edu.bistu.ksclient.automata.AbstractStatus;
import edu.bistu.ksclient.automata.Event;
import edu.bistu.ksclient.runnable.GameInfoGetter;

public class Status6 extends AbstractStatus
{
    public Status6()
    {
        super();
        statusNumber = 6;
        supportedNextStatus.put(8,7);
    }

    @Override
    protected void initialize(Event from, PriorityQueue<Event> unSupportedEvents)
    {
        super.initialize(from, unSupportedEvents);
        Log.d(getClass().getName(), "initialize()");

        Message message = new Message();
        message.what = -4;
        Memory.currentActivity.receiveMessage(message);

        Integer gameID = (Integer) from.getAttachment();
        GameInfoGetter gameInfoGetter = new GameInfoGetter(gameID);
        new Thread(gameInfoGetter).start();

        checkUnSupportedEvent();
    }
}
