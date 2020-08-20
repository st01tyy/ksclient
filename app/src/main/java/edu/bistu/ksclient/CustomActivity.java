package edu.bistu.ksclient;

import android.content.Intent;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public abstract class CustomActivity extends AppCompatActivity
{
    protected class Handler extends android.os.Handler
    {
        @Override
        public void handleMessage(@NonNull Message msg)
        {
            if(msg.what == 1)
            {
                /* 切换Activity */
                Intent intent = null;

                int i = msg.arg1;
                if(i == 1)
                {
                    /* 跳转至LoginActivity */
                    intent = new Intent(CustomActivity.this, LoginActivity.class);

                    int j = msg.arg2;
                    if(j == 1)
                    {
                        /* 登录失败 */
                        intent.putExtra("isLoginFailed", true);
                    }
                }

                token = true;
                startActivity(intent);
                finish();
            }
        }
    }

    protected Handler handler;

    protected boolean token = false;

    protected void initialize()
    {
        Log.d(this.getClass().getName(), "活动初始化");

        Memory.setCurrentActivity(this);
    }

    public void receiveMessage(Message message)
    {
        handler.sendMessage(message);
    }

    protected abstract void lockDownUI();

    protected abstract void unlockUI();

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if(!token)
        {
            /* 强制结束活动，需要销毁内存 */
            Memory.shutdown();
        }

        Log.d(this.getClass().getName(), "活动被销毁");
    }

    @Override
    public abstract void onBackPressed();
}
