package edu.bistu.ksclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.bistu.ksclient.automata.Event;
import edu.bistu.ksclient.runnable.LogoutService;
import edu.bistu.ksclient.runnable.SubjectGetter;

public class MainActivity extends CustomActivity
{
    private TextView textView_name;
    private ImageView imageView_logout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayout linearLayout_history;
    private LinearLayout linearLayout_book;

    private SubjectAdapter subjectAdapter;

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
                 * 刷新科目列表
                 */

                if(recyclerView != null)
                {
                    if(subjectAdapter == null)
                    {
                        subjectAdapter = new SubjectAdapter();
                        subjectAdapter.setSubjects(Memory.subjects);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        recyclerView.setAdapter(subjectAdapter);
                    }
                    else
                    {
                        subjectAdapter.setSubjects(Memory.subjects);
                        subjectAdapter.notifyDataSetChanged();
                    }

                    if(swipeRefreshLayout.isRefreshing())
                        swipeRefreshLayout.setRefreshing(false);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    @Override
    protected void initialize()
    {
        super.initialize();
        handler = new Handler();

        textView_name = findViewById(R.id.textView_name);
        imageView_logout = findViewById(R.id.imageView_logout);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        linearLayout_history = findViewById(R.id.linearLayout_history);
        linearLayout_book = findViewById(R.id.linearLayout_book);

        textView_name.setText(Memory.user.getName());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                Log.d(getClass().getName(), "用户下拉刷新");
                new Thread(new SubjectGetter()).start();
            }
        });
    }

    @Override
    protected void lockDownUI()
    {
        imageView_logout.setClickable(false);
        linearLayout_history.setClickable(false);
        linearLayout_book.setClickable(false);
    }

    @Override
    protected void unlockUI()
    {
        imageView_logout.setClickable(true);
        linearLayout_history.setClickable(true);
        linearLayout_book.setClickable(true);
    }

    @Override
    public void onBackPressed()
    {

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        new Thread(new SubjectGetter()).start();

        imageView_logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d(getClass().getName(), "用户点击了登出按钮");
                new Thread(new LogoutService()).start();
                Memory.automata.receiveEvent(new Event(3, null, System.currentTimeMillis()));
            }
        });
    }
}
