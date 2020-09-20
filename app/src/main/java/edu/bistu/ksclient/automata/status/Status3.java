package edu.bistu.ksclient.automata.status;

import android.os.Message;
import android.util.Log;

import java.util.PriorityQueue;

import edu.bistu.ksclient.Memory;
import edu.bistu.ksclient.automata.AbstractStatus;
import edu.bistu.ksclient.automata.Event;

public class Status3 extends AbstractStatus
{
    public Status3()
    {
        super();
        statusNumber = 3;
        supportedNextStatus.put(5, 4);
    }

    @Override
    protected void initialize(Event from, PriorityQueue<Event> unSupportedEvents)
    {
        super.initialize(from, unSupportedEvents);
        Log.d(getClass().getName(), "initialize()");

        Message message = new Message();
        message.what = -1;
        Memory.currentActivity.receiveMessage(message);

        Long subjectID = (Long) from.getAttachment();
        Memory.selectedSubjectID = subjectID;
        Memory.startNetworkService();

        checkUnSupportedEvent();
    }
}
