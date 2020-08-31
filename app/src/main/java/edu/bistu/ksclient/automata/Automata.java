package edu.bistu.ksclient.automata;

import android.util.Log;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Automata implements Runnable
{
    private AbstractStatus currentStatus;

    private StatusFactory statusFactory;

    private LinkedBlockingQueue<Event> eventQueue;

    @Override
    public void run()
    {
        Log.d(this.getClass().getName(), "状态自动机启动");

        statusFactory = new StatusFactory();
        eventQueue = new LinkedBlockingQueue<>();

        try
        {
            Thread.sleep(1500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        changeCurrentStatus(1, null);

        while(true)
        {
            try
            {
                Event event = eventQueue.take();
                if(event.getEventNumber() == -1)
                    break;
                eventTriggered(event);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        Log.d(this.getClass().getName(), "状态自动机结束循环");
    }

    private void changeCurrentStatus(Integer newStatusNumber, Event event)
    {
        /**
         * 转换当前状态至下一状态
         */

        if(currentStatus != null)
        {
            PriorityQueue<Event> priorityQueue = currentStatus.getUnSupportedEvents();
            AbstractStatus newStatus = statusFactory.getStatus(newStatusNumber);
            currentStatus = newStatus;
            currentStatus.initialize(event, priorityQueue);
        }
        else
        {
            currentStatus = statusFactory.getStatus(newStatusNumber);
            currentStatus.initialize(null, null);
        }

    }

    private void eventTriggered(Event event)
    {
        /**
         * 某个事件被触发
         * 先进行判断事件是否被当前状态支持
         * 再进行状态转换
         */

        Log.d(getClass().getName(), "事件被触发：" + event.getEventNumber());
        Integer nextStatus = currentStatus.eventTriggered(event);
        if(nextStatus != null)
        {
           /*
           当前状态支持被触发的事件
            */

           Log.d(getClass().getName(), "下一状态：" + nextStatus);
           changeCurrentStatus(nextStatus, event);
        }
    }

    public void receiveEvent(Event event)
    {
        if(event != null)
            eventQueue.add(event);
    }
}
