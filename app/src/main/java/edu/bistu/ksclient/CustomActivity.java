package edu.bistu.ksclient;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

import edu.bistu.ksclient.model.GameInfo;
import edu.bistu.ksclient.runnable.LogoutService;

public abstract class CustomActivity extends AppCompatActivity
{
    private ProgressDialog tcpProgressDialog;

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
                else if(i == 2)
                {
                    /* 跳转至MainActivity */
                    intent = new Intent(CustomActivity.this, MainActivity.class);
                }
                else if(i == 3)
                {
                    /* 跳转至GameActivity */
                    GameInfo gameInfo = (GameInfo) msg.obj;
                    intent = new Intent(CustomActivity.this, GameActivity.class);
                    intent.putExtra("gameInfo", gameInfo);
                }

                startActivity(intent);
            }
            else if(msg.what == 0)
            {
                /* 异常 */
//                lockDownUI();
//                AlertDialog.Builder builder = new AlertDialog.Builder(CustomActivity.this);
//                builder.setTitle("出BUG了");
//                builder.setMessage((String) msg.obj);
//                AlertDialog dialog = builder.create();
//                dialog.show();
            }
            else if(msg.what == -1)
            {
                /* 建立连接 */
                lockDownUI();
                if(tcpProgressDialog == null)
                    tcpProgressDialog = new ProgressDialog(CustomActivity.this);

                tcpProgressDialog.setMessage("正在连接服务器");

                if(!tcpProgressDialog.isShowing())
                {
                    tcpProgressDialog.setCancelable(false);
                    tcpProgressDialog.show();
                }
            }
            else if(msg.what == -2)
            {
                if(tcpProgressDialog == null)
                    tcpProgressDialog = new ProgressDialog(CustomActivity.this);

                tcpProgressDialog.setMessage("正在登录");
                if(!tcpProgressDialog.isShowing())
                {
                    tcpProgressDialog.setCancelable(false);
                    tcpProgressDialog.show();
                }
            }
            else if(msg.what == -3)
            {
                if(tcpProgressDialog == null)
                    tcpProgressDialog = new ProgressDialog(CustomActivity.this);

                tcpProgressDialog.setMessage("正在匹配");
                if(!tcpProgressDialog.isShowing())
                {
                    tcpProgressDialog.setCancelable(false);
                    tcpProgressDialog.show();
                }
            }
            else if(msg.what == -4)
            {
                if(tcpProgressDialog == null)
                    tcpProgressDialog = new ProgressDialog(CustomActivity.this);

                tcpProgressDialog.setMessage("正在加载资源");
                if(!tcpProgressDialog.isShowing())
                {
                    tcpProgressDialog.setCancelable(false);
                    tcpProgressDialog.show();
                }
            }
        }
    }

    protected Handler handler;

    protected boolean token = false;
    private boolean isSwitchActivity = false;

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
            Log.d(getClass().getName(), "token = false");
            new Thread(new LogoutService()).start();
            Memory.shutdown();
        }

        if(tcpProgressDialog != null && tcpProgressDialog.isShowing())
            tcpProgressDialog.dismiss();

        Log.d(this.getClass().getName(), "活动被销毁");
    }

    @Override
    public void startActivity(Intent intent)
    {
        token = true;
        isSwitchActivity = true;
        super.startActivity(intent);
    }

    @Override
    public abstract void onBackPressed();

    @Override
    protected void onStop()
    {
        super.onStop();
        if(isSwitchActivity)
            finish();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
