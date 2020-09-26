package edu.bistu.ksclient.automata;

import java.util.HashMap;
import java.util.Map;

import edu.bistu.ksclient.automata.status.Status1;
import edu.bistu.ksclient.automata.status.Status10;
import edu.bistu.ksclient.automata.status.Status11;
import edu.bistu.ksclient.automata.status.Status2;
import edu.bistu.ksclient.automata.status.Status3;
import edu.bistu.ksclient.automata.status.Status4;
import edu.bistu.ksclient.automata.status.Status5;
import edu.bistu.ksclient.automata.status.Status6;
import edu.bistu.ksclient.automata.status.Status7;
import edu.bistu.ksclient.automata.status.Status8;
import edu.bistu.ksclient.automata.status.Status9;

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
        statusMap.put(5, new Status5());
        statusMap.put(6, new Status6());
        statusMap.put(7, new Status7());
        statusMap.put(8, new Status8());
        statusMap.put(9, new Status9());
        statusMap.put(10, new Status10());
        statusMap.put(11, new Status11());
    }

    public AbstractStatus getStatus(Integer statusNumber)
    {
        return statusMap.get(statusNumber);
    }

}
