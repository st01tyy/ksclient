package edu.bistu.ksclient.automata.status;

import android.os.Message;
import android.util.Log;

import java.util.PriorityQueue;

import edu.bistu.ksclient.Memory;
import edu.bistu.ksclient.automata.AbstractStatus;
import edu.bistu.ksclient.automata.Event;
import edu.bistu.ksclient.network.ServerMessage;

public class Status9 extends AbstractStatus
{
    public Status9()
    {
        super();
        statusNumber = 9;
        supportedNextStatus.put(9, 8);
        supportedNextStatus.put(10, 9);
        supportedNextStatus.put(13, 11);
    }

    @Override
    protected void initialize(Event from, PriorityQueue<Event> unSupportedEvents)
    {
        super.initialize(from, unSupportedEvents);
        Log.d(getClass().getName(), "initialize()");

        if(from.getEventNumber() == 10)
        {
            Message message = new Message();
            message.what = 4;
            message.obj = from.getAttachment();
            Memory.currentActivity.receiveMessage(message);
        }
        else if(from.getEventNumber() == 11)
        {
            Integer[] arr = (Integer[]) from.getAttachment();
            Memory.networkService.sendMessage(ServerMessage.userSelect(arr));
        }

        checkUnSupportedEvent();
    }
}
