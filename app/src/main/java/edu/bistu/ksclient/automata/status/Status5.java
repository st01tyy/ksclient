package edu.bistu.ksclient.automata.status;

import android.os.Message;
import android.util.Log;

import java.util.PriorityQueue;

import edu.bistu.ksclient.Memory;
import edu.bistu.ksclient.automata.AbstractStatus;
import edu.bistu.ksclient.automata.Event;
import edu.bistu.ksclient.network.ServerMessage;

public class Status5 extends AbstractStatus
{
    public Status5()
    {
        super();
        statusNumber = 5;
        supportedNextStatus.put(7,6);
        supportedNextStatus.put(14, 2);
    }

    @Override
    protected void initialize(Event from, PriorityQueue<Event> unSupportedEvents)
    {
        super.initialize(from, unSupportedEvents);
        Log.d(getClass().getName(), "initialize()");

        Message message = new Message();
        message.what = -3;
        Memory.currentActivity.receiveMessage(message);

        ServerMessage serverMessage = ServerMessage.matchRequest();
        Memory.networkService.sendMessage(serverMessage);

        checkUnSupportedEvent();
    }
}
