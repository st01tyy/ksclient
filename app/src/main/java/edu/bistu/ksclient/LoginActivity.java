package edu.bistu.ksclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import edu.bistu.ksclient.runnable.LoginService;

public class LoginActivity extends CustomActivity
{
    private EditText editText_id;
    private EditText editText_pw;
    private TextView textView_hint;
    private Button button_login;

    private EditText editText_ipPart1;
    private EditText editText_ipPart2;
    private EditText editText_ipPart3;
    private EditText editText_ipPart4;

    private ProgressDialog progressDialog;

    class CustomListener implements EditText.OnFocusChangeListener
    {
        private Integer position;

        CustomListener(Integer position)
        {
            this.position = position;
        }

        @Override
        public void onFocusChange(View view, boolean b)
        {
            EditText editText = (EditText) view;
            if(!b)
            {
                try
                {
                    Integer val = Integer.parseInt(editText.getText().toString());
                    if(val >= 0 && val <= 255)
                    {
                        String str = editText_ipPart1.getText().toString()
                                + "."
                                + editText_ipPart2.getText().toString()
                                + "."
                                + editText_ipPart3.getText().toString()
                                + "."
                                + editText_ipPart4.getText().toString();
                        Memory.serverIP = str;
                    }
                    else
                        throw new Exception();
                }
                catch (Exception e)
                {
                    String str = Memory.serverIP.split("\\.")[position];
                    editText.setText(str);
                }
            }
        }
    }

    private class Handler extends CustomActivity.Handler
    {
        @Override
        public void handleMessage(@NonNull Message msg)
        {
            super.handleMessage(msg);

            int i = msg.what;
            if(i == 2)
            {
                /**
                 * 登录失败
                 * 101：C语言平台API返回登录失败
                 * 102：C语言平台API出现异常
                 * 103：账号已登录
                 * 104：catch IOException
                 * 105：json转换异常
                 */

                int j = msg.arg1;
                String text = null;
                if(j == 101)
                    text = "用户名或密码错误";
                else if(j == 102)
                    text = "验证身份时出现异常";
                else if(j == 103)
                    text = "该账号已登录";
                else if(j == 104)
                    text = "无法访问服务器";
                else if(j == 102)
                    text = "程序异常，请重试";
                else
                    text = "未知错误代码：" + j;
                textView_hint.setText(text);
                textView_hint.setVisibility(View.VISIBLE);
                if(progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                unlockUI();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("storage", MODE_PRIVATE);
        String str = sharedPreferences.getString("ip", null);
        if(str != null)
            Memory.serverIP = str;

        Log.e(getClass().getName(), Memory.serverIP);
        String[] arr = Memory.serverIP.split("\\.");
        editText_ipPart1.setText(arr[0]);
        editText_ipPart2.setText(arr[1]);
        editText_ipPart3.setText(arr[2]);
        editText_ipPart4.setText(arr[3]);
    }

    @Override
    protected void initialize()
    {
        super.initialize();

        handler = new Handler();

        editText_id = findViewById(R.id.editText_id);
        editText_pw = findViewById(R.id.editText_pw);
        textView_hint = findViewById(R.id.textView_hint);
        button_login = findViewById(R.id.button_login);

        editText_ipPart1 = findViewById(R.id.editText_ipPart1);
        editText_ipPart2 = findViewById(R.id.editText_ipPart2);
        editText_ipPart3 = findViewById(R.id.editText_ipPart3);
        editText_ipPart4 = findViewById(R.id.editText_ipPart4);

        editText_ipPart1.setOnFocusChangeListener(new CustomListener(0));
        editText_ipPart2.setOnFocusChangeListener(new CustomListener(1));
        editText_ipPart3.setOnFocusChangeListener(new CustomListener(2));
        editText_ipPart4.setOnFocusChangeListener(new CustomListener(3));

        button_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(editText_id.getText() != null && editText_pw.getText() != null)
                {
                    lockDownUI();
                    Log.d(getClass().getName(),"用户点击了登录按钮");
                    editText_ipPart1.clearFocus();
                    editText_ipPart2.clearFocus();
                    editText_ipPart3.clearFocus();
                    editText_ipPart4.clearFocus();
                    if(editText_id.getText() == null || editText_pw.getText() == null)
                        return;
                    try
                    {
                        Long val = Long.valueOf(editText_id.getText().toString());
                        if(progressDialog == null)
                        {
                            progressDialog = new ProgressDialog(LoginActivity.this);
                            progressDialog.setMessage("正在登录");
                            progressDialog.setCancelable(false);
                        }
                        progressDialog.show();
                        new Thread(new LoginService(val, editText_pw.getText().toString())).start();
                    }
                    catch (Exception e)
                    {
                        Log.e(getClass().getName(), e.getMessage());
                        unlockUI();
                    }
                }
            }
        });
    }

    @Override
    protected void lockDownUI()
    {
        button_login.setClickable(false);
    }

    @Override
    protected void unlockUI()
    {
        button_login.setClickable(true);
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认退出程序？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                /* 退出程序 */
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
