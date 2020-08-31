package edu.bistu.ksclient.automata.status;

import android.os.Message;
import android.util.Log;

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
        Log.d(getClass().getName(), "initialize()");

        if(from == null)
        {
            /* 跳转至LoginActivity */
            Message message = new Message();
            message.what = 1;
            message.arg1 = 1;
            Memory.currentActivity.receiveMessage(message);
        }
        else
        {
            Integer i = from.getEventNumber();
            try
            {
                if(i == null)
                {
                    /* 异常 */
                    Memory.bugOccured("事件序号为空");
                }
                else if(i == 2)
                {
                    Integer j = (Integer) from.getAttachment();
                    if(j == null)
                        Memory.bugOccured("登录结果为空");
                    else
                    {
                        /* 登录失败 */
                        Message message = new Message();
                        message.what = 2;
                        message.arg1 = j;
                        Memory.currentActivity.receiveMessage(message);
                    }
                }
                else
                {
                    /* 异常 */
                    Memory.bugOccured("事件序号异常，当前状态为：" + statusNumber + "，事件序号为：" + i);
                }
            }
            catch (Exception e)
            {
                /* 有可能在类型转换时发生错误 */
                e.printStackTrace();
                Log.e(this.getClass().getName(), e.getMessage());
                Memory.bugOccured(e.getMessage());
            }
        }

        checkUnSupportedEvent();
    }
}
