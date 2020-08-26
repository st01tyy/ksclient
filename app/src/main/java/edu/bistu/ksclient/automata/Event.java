package edu.bistu.ksclient.automata;

public class Event
{
    protected Integer eventNumber;  //事件序号

    protected Object attachment;    //附属数据

    protected Long eventTime;   //事件发生时间

    public Integer getEventNumber() {
        return eventNumber;
    }

    public Object getAttachment() {
        return attachment;
    }

    public Long getEventTime() {
        return eventTime;
    }

    public Event(Integer eventNumber, Object attachment, Long eventTime)
    {
        this.eventNumber = eventNumber;
        this.attachment = attachment;
        this.eventTime = eventTime;
    }
}
