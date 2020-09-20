package edu.bistu.ksclient.automata;

import java.util.HashMap;
import java.util.Map;

import edu.bistu.ksclient.automata.status.Status1;
import edu.bistu.ksclient.automata.status.Status2;
import edu.bistu.ksclient.automata.status.Status3;
import edu.bistu.ksclient.automata.status.Status4;

public class StatusFactory
{
    /**
     * 用以创建和储存各类状态的工厂类
     */

    private Map<Integer, AbstractStatus> statusMap;

    public StatusFactory()
    {
        statusMap = new HashMap<>();
        statusMap.put(1, new Status1());
        statusMap.put(2, new Status2());
        statusMap.put(3, new Status3());
        statusMap.put(4, new Status4());
    }

    public AbstractStatus getStatus(Integer statusNumber)
    {
        return statusMap.get(statusNumber);
    }

}
