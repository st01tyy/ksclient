package edu.bistu.ksclient.network;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;

import edu.bistu.ksclient.Memory;
import edu.bistu.ksclient.automata.Event;

public class NetworkService implements Runnable
{
    private LinkedBlockingQueue<ServerMessage> queue;

    private Thread receiverThread;
    private MessageReceiver messageReceiver;

    @Override
    public void run()
    {
        Log.d(getClass().getName(), "网络服务启动");

        queue = new LinkedBlockingQueue<>();

        try
        {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(Memory.serverIP, Memory.serverSocketPort));
            messageReceiver = new MessageReceiver(socketChannel);
            receiverThread = new Thread(messageReceiver, "MessageReceiver");
            receiverThread.start();

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);

            Event event = new Event(5, null, System.currentTimeMillis());
            Memory.automata.receiveEvent(event);

            while(true)
            {
                ServerMessage serverMessage = queue.take();
                if(serverMessage.getStudentID() == -1)
                {
                    //终止服务
                    break;
                }

                loadByteBuffer(ServerMessage.report(), byteBuffer);
                Integer length = socketChannel.write(byteBuffer);
                Log.d(getClass().getName(), "发送了长度为" + length + "的数据");
            }

            messageReceiver.shutdown();
            receiverThread.join();

            loadByteBuffer(ServerMessage.disconnect(), byteBuffer);
            socketChannel.write(byteBuffer);

            Thread.sleep(2000);
            socketChannel.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            error();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            error();
        }

        Memory.networkService = null;
        Log.d(getClass().getName(), "网络服务结束");
    }

    private void loadByteBuffer(ServerMessage message, ByteBuffer byteBuffer)
    {
        byteBuffer.clear();
        byteBuffer.putLong(message.getStudentID());
        byteBuffer.putLong(message.getTime());
        byteBuffer.putInt(message.getServiceNumber());
        byteBuffer.putInt(message.getMessageType());
        byteBuffer.putInt(message.getN());
        if(message.getN() > 0)
        {
            Integer[] arr = message.getArr();
            for(int i = 0; i < message.getN(); i++)
            {
                byteBuffer.putInt(arr[i]);
            }
        }
        byteBuffer.limit(byteBuffer.position());
        byteBuffer.position(0);

        Log.d(getClass().getName(), "缓冲区装载完毕" + "position = " + byteBuffer.position() + ", limit = " + byteBuffer.limit());
    }

    private void error()
    {

    }

    public void sendMessage(ServerMessage serverMessage)
    {
        queue.add(serverMessage);
    }

}
