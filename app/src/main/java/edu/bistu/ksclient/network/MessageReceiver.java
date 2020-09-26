package edu.bistu.ksclient.network;

import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import edu.bistu.ksclient.Memory;
import edu.bistu.ksclient.automata.Event;

public class MessageReceiver implements Runnable
{
    private SocketChannel socketChannel;

    private Boolean isShutdown;
    protected final ReentrantReadWriteLock shutdownLock;

    public MessageReceiver(SocketChannel socketChannel)
    {
        this.socketChannel = socketChannel;
        isShutdown = false;
        shutdownLock = new ReentrantReadWriteLock();
    }

    @Override
    public void run()
    {
        Log.d(getClass().getName(), "消息接收器启动");
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);

        try
        {
            Selector selector = Selector.open();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);

            while(!isShutdown())
            {
                selector.select(5000);  //5s阻塞时间
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext())
                {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isReadable())
                    {
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        if(channel != null)
                        {
                            Integer n = channel.read(byteBuffer);
                            Log.d(getClass().getName(), "接收长度为" + n + "字节的数据");

                            if(n == 0)
                                Log.d(getClass().getName(), "数据长度为0");

                            else
                            {
                                byteBuffer.limit(byteBuffer.position());
                                byteBuffer.position(0);

                                while(byteBuffer.position() < byteBuffer.limit())
                                {
                                    ClientMessage message = new ClientMessage();
                                    message.setTime(byteBuffer.getLong());
                                    message.setType(byteBuffer.getInt());
                                    message.setN(byteBuffer.getInt());
                                    if(message.getN() > 0)
                                    {
                                        Integer[] arr = new Integer[message.getN()];
                                        for(int i = 0; i < message.getN(); i++)
                                        {
                                            arr[i] = byteBuffer.getInt();
                                        }
                                        message.setArr(arr);
                                    }

                                    Memory.automata.receiveEvent(getEvent(message));
                                }
                            }

                            byteBuffer.clear();
                        }
                    }

                    iterator.remove();
                }
            }

            selector.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Log.d(getClass().getName(), "消息接收器结束");
    }

    private Event getEvent(ClientMessage message)
    {
        Integer type = message.getType();
        Event event = null;
        if(type == 1)
            event = new Event(6, null, message.getTime());
        else if(type == 3)
            event = new Event(7, message.getArr()[0], message.getTime());
        else if(type == 4)
            event = new Event(9, message.getArr()[0], message.getTime());
        else if(type == 5)
            event = new Event(10, message.getArr(), message.getTime());
        else if(type == 6)
            event = new Event(13, message.getArr(), message.getTime());
        return event;
    }

    protected Boolean isShutdown()
    {
        //检查终止信号
        shutdownLock.readLock().lock();
        Boolean res = isShutdown;
        shutdownLock.readLock().unlock();
        return res;
    }

    public void shutdown()
    {
        //修改终止信号
        shutdownLock.writeLock().lock();
        isShutdown = true;
        shutdownLock.writeLock().unlock();
    }
}
