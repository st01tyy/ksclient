package edu.bistu.ksclient.automata;

import java.util.HashMap;
import java.util.Map;

import edu.bistu.ksclient.automata.status.Status1;

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
    }

    public AbstractStatus getStatus(Integer statusNumber)
    {
        return statusMap.get(statusNumber);
    }

}
