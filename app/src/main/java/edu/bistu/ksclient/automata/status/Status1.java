package edu.bistu.ksclient.automata.status;

import android.os.Message;

import java.util.PriorityQueue;

import edu.bistu.ksclient.Memory;
import edu.bistu.ksclient.automata.Event;
import edu.bistu.ksclient.automata.AbstractStatus;

public class Status1 extends AbstractStatus
{
    public Status1()
    {
        super();
        statusNumber = 1;
        supportedNextStatus.put(1, 2);
        supportedNextStatus.put(2, 1);
    }

    @Override
    public void initialize(Event from, PriorityQueue<Event> unSupportedEvents)
    {
        super.initialize(from, unSupportedEvents);

        /* 跳转至LoginActivity */
        Message message = new Message();
        message.what = 1;
        message.arg1 = 1;
        Memory.currentActivity.receiveMessage(message);

        checkUnSupportedEvent();
    }
}
