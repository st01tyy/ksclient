package edu.bistu.ksclient.automata.status;

import android.os.Message;
import android.util.Log;

import java.util.PriorityQueue;

import edu.bistu.ksclient.MainActivity;
import edu.bistu.ksclient.Memory;
import edu.bistu.ksclient.automata.AbstractStatus;
import edu.bistu.ksclient.automata.Event;
import edu.bistu.ksclient.network.ServerMessage;

public class Status2 extends AbstractStatus
{
    public Status2()
    {
        super();
        statusNumber = 2;
        supportedNextStatus.put(3, 1);
        supportedNextStatus.put(4, 3);
    }

    @Override
    protected void initialize(Event from, PriorityQueue<Event> unSupportedEvents)
    {
        super.initialize(from, unSupportedEvents);
        Log.d(getClass().getName(), "initialize()");

        if(from.getEventNumber() == 14)
        {
            Memory.networkService.sendMessage(ServerMessage.disconnect());
            Memory.networkService.sendMessage(ServerMessage.shutdownSignal());
        }

//        /* 跳转至MainActivity */
//        if(!(Memory.currentActivity instanceof MainActivity))
//        {
//            Message message = new Message();
//            message.what = 1;
//            message.arg1 = 2;
//            Memory.currentActivity.receiveMessage(message);
//        }

        Message message = new Message();
        message.what = 1;
        message.arg1 = 2;
        Memory.currentActivity.receiveMessage(message);

        checkUnSupportedEvent();
    }
}
