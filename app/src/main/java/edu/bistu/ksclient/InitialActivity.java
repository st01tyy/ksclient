package edu.bistu.ksclient;

import android.os.Bundle;
import android.os.Message;

import androidx.annotation.NonNull;

public class InitialActivity extends CustomActivity
{
    private class Handler extends CustomActivity.Handler
    {
        @Override
        public void handleMessage(@NonNull Message msg)
        {
            super.handleMessage(msg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        initialize();
    }

    @Override
    protected void initialize()
    {
        super.initialize();

        handler = new Handler();
        Memory.initialize();
    }

    @Override
    protected void lockDownUI() {

    }

    @Override
    protected void unlockUI() {

    }

    @Override
    public void onBackPressed()
    {
        //do nothing
    }
}
