package edu.bistu.ksclient.automata.status;

import android.os.Message;
import android.util.Log;

import java.util.PriorityQueue;

import edu.bistu.ksclient.Memory;
import edu.bistu.ksclient.automata.AbstractStatus;
import edu.bistu.ksclient.automata.Event;

public class Status10 extends AbstractStatus
{
    public Status10()
    {
        super();
        statusNumber = 10;
        supportedNextStatus.put(9, 8);
        supportedNextStatus.put(13, 11);
    }

    @Override
    protected void initialize(Event from, PriorityQueue<Event> unSupportedEvents)
    {
        super.initialize(from, unSupportedEvents);
        Log.d(getClass().getName(), "initialize()");

        Message message = new Message();
        message.what = 5;
        Memory.currentActivity.receiveMessage(message);

        checkUnSupportedEvent();
    }

}
