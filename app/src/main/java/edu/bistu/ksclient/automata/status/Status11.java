package edu.bistu.ksclient.automata.status;

import android.os.Message;
import android.util.Log;

import java.util.PriorityQueue;

import edu.bistu.ksclient.Memory;
import edu.bistu.ksclient.automata.AbstractStatus;
import edu.bistu.ksclient.automata.Event;

public class Status11 extends AbstractStatus
{
    public Status11()
    {
        super();
        statusNumber = 11;
        supportedNextStatus.put(14, 2);
    }

    @Override
    protected void initialize(Event from, PriorityQueue<Event> unSupportedEvents)
    {
        super.initialize(from, unSupportedEvents);
        Log.d(getClass().getName(), "initialize()");

        Message message = new Message();
        message.what = 6;
        message.obj = from.getAttachment();
        Memory.currentActivity.receiveMessage(message);

        checkUnSupportedEvent();
    }
}
