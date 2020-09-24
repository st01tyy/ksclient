package edu.bistu.ksclient.network;

import edu.bistu.ksclient.Memory;

public class ServerMessage
{
    private Long studentID;
    private Long time;
    private Integer serviceNumber;
    private Integer messageType;
    private Integer n;
    private Integer[] arr;

    private ServerMessage()
    {
        studentID = Memory.user.getId();
        time = System.currentTimeMillis();
    }

    public Long getStudentID() {
        return studentID;
    }

    public void setStudentID(Long studentID) {
        this.studentID = studentID;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getServiceNumber() {
        return serviceNumber;
    }

    public void setServiceNumber(Integer serviceNumber) {
        this.serviceNumber = serviceNumber;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Integer[] getArr() {
        return arr;
    }

    public void setArr(Integer[] arr) {
        this.arr = arr;
    }

    public static ServerMessage report()
    {
        ServerMessage message = new ServerMessage();
        message.setServiceNumber(0);
        message.setMessageType(1);
        message.setN(0);
        return message;
    }

    public static ServerMessage disconnect()
    {
        ServerMessage message = new ServerMessage();
        message.setServiceNumber(0);
        message.setMessageType(2);
        message.setN(0);
        return message;
    }

    public static ServerMessage shutdownSignal()
    {
        ServerMessage message = new ServerMessage();
        message.setStudentID((long) -1);
        return message;
    }

    public static ServerMessage matchRequest()
    {
        ServerMessage message = new ServerMessage();
        message.setServiceNumber(1);
        message.setMessageType(1);
        message.setN(1);
        Integer[] arr = new Integer[1];
        arr[0] = Memory.selectedSubjectID.intValue();
        message.setArr(arr);
        return message;
    }

}
