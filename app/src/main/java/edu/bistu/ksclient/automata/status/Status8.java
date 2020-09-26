package edu.bistu.ksclient.automata.status;

import android.os.Message;
import android.util.Log;

import java.util.PriorityQueue;

import edu.bistu.ksclient.Memory;
import edu.bistu.ksclient.automata.AbstractStatus;
import edu.bistu.ksclient.automata.Event;

public class Status8 extends AbstractStatus
{
    public Status8()
    {
        super();
        statusNumber = 8;
        supportedNextStatus.put(10,8);
        supportedNextStatus.put(11, 9);
        supportedNextStatus.put(12, 10);
    }

    @Override
    protected void initialize(Event from, PriorityQueue<Event> unSupportedEvents)
    {
        super.initialize(from, unSupportedEvents);
        Log.d(getClass().getName(), "initialize()");

        Message message = new Message();
        if(from.getEventNumber() == 9)
        {
            message.what = 3;
            message.arg1 = (Integer) from.getAttachment();
        }
        else if(from.getEventNumber() == 10)
        {
            message.what = 4;
            message.obj = from.getAttachment();
        }

        Memory.currentActivity.receiveMessage(message);
        checkUnSupportedEvent();
    }
}
