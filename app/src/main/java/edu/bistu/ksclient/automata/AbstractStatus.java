package edu.bistu.ksclient.automata;

import android.util.Log;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public abstract class AbstractStatus
{
    protected Integer statusNumber; //状态序号

    protected Map<Integer, Integer> supportedNextStatus;   //<事件序号，状态序号>

    protected Event from;   //由该事件转换而来

    public PriorityQueue<Event> unSupportedEvents;    //非当前状态支持的触发事件集合（网络消息后发先到）

    protected AbstractStatus()
    {
        supportedNextStatus = new HashMap<>();
    }

    protected void initialize(Event from, PriorityQueue<Event> unSupportedEvents)
    {
        //刚转换至当前状态时客户端需要进行的一系列操作
        this.from = from;
        this.unSupportedEvents = unSupportedEvents;
    }

    public Integer eventTriggered(Event event)
    {
        /**
         * 某个事件被触发时调用
         * 在此方法中判断被触发的事件是否可以转换状态
         * 若可以转换，则返回新事件的序号
         * 若不能，则返回空
         */

        Integer nextStatus = supportedNextStatus.get(event.getEventNumber());
        if(nextStatus == null)
        {
            /*
            被触发的事件不是当前状态下应该被被触发的
            说明发生了异常（应该是服务端多线程消息发送导致的时间不同步）
             */

            if(unSupportedEvents == null)
            {
                unSupportedEvents = new PriorityQueue<>(new Comparator<Event>()
                {
                    @Override
                    public int compare(Event o1, Event o2)
                    {
                        if(o1.getEventTime() < o2.getEventTime())
                            return -1;
                        else if(o1.getEventTime() == o2.getEventTime())
                            return 0;
                        else
                            return 1;
                    }
                });
            }
            unSupportedEvents.add(event);   //将当前事件添加到优先队列中，以待处理
            Log.e("UnsupportedEvent", "当前状态序号：" + statusNumber
                    + "收到不支持的事件序号：" + event.getEventNumber());
            return null;
        }
        else
        {
            /*
            被触发的事件是当前状态所支持的事件，属正常状态
             */

            return nextStatus;
        }
    }

    public PriorityQueue<Event> getUnSupportedEvents()
    {
        return unSupportedEvents;
    }

    protected void checkUnSupportedEvent()
    {

    }
}
