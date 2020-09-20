package edu.bistu.ksclient.automata.status;

import android.os.Message;
import android.util.Log;

import java.util.PriorityQueue;

import edu.bistu.ksclient.Memory;
import edu.bistu.ksclient.automata.AbstractStatus;
import edu.bistu.ksclient.automata.Event;
import edu.bistu.ksclient.network.ServerMessage;

public class Status4 extends AbstractStatus
{
    public Status4()
    {
        super();
        statusNumber = 4;
        supportedNextStatus.put(6, 5);
    }

    @Override
    protected void initialize(Event from, PriorityQueue<Event> unSupportedEvents)
    {
        super.initialize(from, unSupportedEvents);
        Log.d(getClass().getName(), "initialize()");

        Message message = new Message();
        message.what = -2;
        Memory.currentActivity.receiveMessage(message);

        ServerMessage serverMessage = ServerMessage.report();
        Memory.networkService.sendMessage(serverMessage);

        checkUnSupportedEvent();
    }
}
