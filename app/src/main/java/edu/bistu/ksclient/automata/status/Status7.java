package edu.bistu.ksclient.automata.status;

import android.os.Message;
import android.util.Log;

import java.util.PriorityQueue;

import edu.bistu.ksclient.Memory;
import edu.bistu.ksclient.automata.AbstractStatus;
import edu.bistu.ksclient.automata.Event;

public class Status7 extends AbstractStatus
{
    public Status7()
    {
        super();
        statusNumber = 7;
        supportedNextStatus.put(9, 8);
    }

    @Override
    protected void initialize(Event from, PriorityQueue<Event> unSupportedEvents)
    {
        super.initialize(from, unSupportedEvents);
        Log.d(getClass().getName(), "initialize()");

        Message message = new Message();
        message.what = 1;
        message.arg1 = 3;
        message.obj = from.getAttachment();
        Memory.currentActivity.receiveMessage(message);

        checkUnSupportedEvent();
    }
}
