package edu.bistu.ksclient;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import edu.bistu.ksclient.automata.Event;
import edu.bistu.ksclient.model.GameInfo;
import edu.bistu.ksclient.model.PlayerStatus;
import edu.bistu.ksclient.model.Question;
import edu.bistu.ksclient.network.ServerMessage;

public class GameActivity extends CustomActivity
{
    private RecyclerView recyclerView_teamBluePlayers;
    private RecyclerView recyclerView_teamRedPlayers;

    private PlayerAdapter teamBlueAdapter;
    private PlayerAdapter teamRedAdapter;

    private TextView textView_teamBlueScore;
    private TextView textView_countDown;
    private TextView textView_teamRedScore;

    private TextView textView_description;

    private RecyclerView recyclerView_selections;
    private SelectionAdapter selectionAdapter;

    private GameInfo gameInfo;
    private PlayerStatus[] playerStatusArr;

    private Counter counter;
    private ExecutorService threadPool;

    private Integer currentQuestion;

    private Boolean uiLock;

    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;

    private class Handler extends CustomActivity.Handler
    {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg)
        {
            super.handleMessage(msg);

            int i = msg.what;
            if(i == 2)
            {
                /* 刷新剩余时间 */
                textView_countDown.setText(Integer.toString(msg.arg1));
            }
            else if(i == 3)
            {
                /* 开始游戏 */
                setQuestion(msg.arg1);
            }
            else if(i == 4)
            {
                /* 刷新答题状态 */
                Integer[] arr = (Integer[]) msg.obj;
                if(arr != null)
                {
                    for(int j = 0; j < playerStatusArr.length; j++)
                    {
                        playerStatusArr[j].setStatus(arr[j]);
                    }
                    refreshPlayerStatus();
                    textView_teamBlueScore.setText(Integer.toString(arr[arr.length - 2]));
                    textView_teamRedScore.setText(Integer.toString(arr[arr.length - 1]));
                }
            }
            else if(i == 5)
            {
                /* 锁定界面 */
                if(progressDialog == null)
                {
                    progressDialog = new ProgressDialog(GameActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("时间到，请稍后");
                }

                progressDialog.show();
            }
            else if(i == 6)
            {
                /* 游戏结束 */
                AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                builder.setTitle("游戏结束");
                Integer[] arr = (Integer[]) msg.obj;
                Integer win = arr[0];
                Integer teamScore = arr[1];
                Integer playerScore = arr[2];
                if(win == 0)
                {
                    builder.setMessage("你的队伍获得胜利！"
                            + "\n队伍总分：" + Integer.toString(teamScore)
                            + "\n个人得分：" + Integer.toString(playerScore));
                }
                else
                {
                    builder.setMessage("你的队伍失败了！"
                            + "\n队伍总分：" + Integer.toString(teamScore)
                            + "\n个人得分：" + Integer.toString(playerScore));
                }
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        Event event = new Event(14, null, System.currentTimeMillis());
                        Memory.automata.receiveEvent(event);
                    }
                });
                builder.setCancelable(false);
                alertDialog = builder.create();

                if(progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                alertDialog.show();
            }
        }
    }

    private class Counter implements Runnable
    {
        private Double time;
        private Boolean stop;
        private final ReentrantReadWriteLock lock;

        Counter(Integer time)
        {
            this.time = time.doubleValue();
            lock = new ReentrantReadWriteLock();
            stop = false;
        }

        @Override
        public void run()
        {
            while(!getStop() && time.compareTo((double) 0) > 0)
            {
                Message message = new Message();
                message.what = 2;
                message.arg1 = time.intValue();
                receiveMessage(message);
                try
                {
                    Thread.sleep(500);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                time -= 0.5;
            }
            Message message = new Message();
            message.arg1 = time.intValue();
            receiveMessage(message);

            if(!getStop())
            {
                Log.d(getClass().getName(), "getStop() = false");
                Event event = new Event(12, null, System.currentTimeMillis());
                Memory.automata.receiveEvent(event);
            }
        }

        private Boolean getStop()
        {
            lock.readLock().lock();
            Boolean res = stop;
            lock.readLock().unlock();
            return res;
        }

        void setStop(Boolean stop)
        {
            lock.writeLock().lock();
            this.stop = stop;
            lock.writeLock().unlock();
        }

        Integer getTime()
        {
            return time.intValue();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        gameInfo = (GameInfo) intent.getSerializableExtra("gameInfo");

        initialize();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(currentQuestion == null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String msg;
            if(gameInfo.getTeam() == 0)
                msg = "游戏即将开始，你被分配到：蓝队";
            else
                msg = "游戏即将开始，你被分配到：红队";
            builder.setMessage(msg);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    Log.d(getClass().getName(), "用户点击了集合点的确认按钮");
                    Memory.networkService.sendMessage(ServerMessage.gameReady(gameInfo.getGameID()));
                    if(currentQuestion == null)
                        textView_description.setText("正在等待其他玩家确认");
                }
            });
            builder.setCancelable(false);

            if(alertDialog != null && alertDialog.isShowing())
                alertDialog.dismiss();
            alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    protected void initialize()
    {
        super.initialize();
        handler = new Handler();

        recyclerView_teamBluePlayers = findViewById(R.id.recyclerView_teamBluePlayers);
        recyclerView_teamRedPlayers = findViewById(R.id.recyclerView_teamRedPlayers);

        textView_teamBlueScore = findViewById(R.id.textView_teamBlueScore);
        textView_countDown = findViewById(R.id.textView_countDown);
        textView_teamRedScore = findViewById(R.id.textView_teamRedScore);

        textView_description = findViewById(R.id.textView_description);

        recyclerView_selections = findViewById(R.id.recyclerView_selections);

        String[] names = gameInfo.getNames();
        playerStatusArr = new PlayerStatus[names.length];
        for(int i = 0; i < playerStatusArr.length; i++)
        {
            playerStatusArr[i] = new PlayerStatus();
            playerStatusArr[i].setName(names[i]);
            playerStatusArr[i].setStatus(0);
        }
        refreshPlayerStatus();

        threadPool = Executors.newSingleThreadExecutor();

        currentQuestion = null;

        uiLock = false;
    }

    @Override
    protected void lockDownUI()
    {
        uiLock = true;
    }

    @Override
    protected void unlockUI()
    {
        uiLock = false;
    }

    @Override
    public void onBackPressed()
    {

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        endCounter();
        threadPool.shutdown();
        if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        if(alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();
    }

    private void setQuestion(Integer i)
    {
        endCounter();
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("开始第" + (i + 1) + "题");
//        builder.setPositiveButton("确定", null);
        if(alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();
//
//        alertDialog = builder.create();
//        alertDialog.show();

        for (PlayerStatus playerStatus : playerStatusArr)
        {
            playerStatus.setStatus(0);
        }
        refreshPlayerStatus();

        Question question = gameInfo.getQuestions()[i];
        textView_description.setText(question.getDescription());
        currentQuestion = i;
        refreshSelection();
        startCounter(question.getTimeLimit());

        if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void startCounter(Integer time)
    {
        counter = new Counter(time);
        threadPool.execute(counter);
    }

    public void endCounter()
    {
        if(counter != null)
            counter.setStop(true);
    }

    private void refreshSelection()
    {
        if(selectionAdapter == null)
        {
             selectionAdapter = new SelectionAdapter(this, gameInfo.getQuestions()[currentQuestion].getSelections());
             LinearLayoutManager manager = new LinearLayoutManager(this);
             manager.setOrientation(RecyclerView.VERTICAL);
             recyclerView_selections.setLayoutManager(manager);
             recyclerView_selections.setAdapter(selectionAdapter);
        }
        else
        {
            selectionAdapter.setArr(gameInfo.getQuestions()[currentQuestion].getSelections());
            selectionAdapter.notifyDataSetChanged();
        }
        unlockUI();
    }

    private void refreshPlayerStatus()
    {
        if(teamBlueAdapter == null)
        {
            teamBlueAdapter = new PlayerAdapter();
            teamBlueAdapter.setStatusArr(getTeamBlueStatusArray());
            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(RecyclerView.HORIZONTAL);
            recyclerView_teamBluePlayers.setLayoutManager(manager);
            recyclerView_teamBluePlayers.setAdapter(teamBlueAdapter);
        }

        if(teamRedAdapter == null)
        {
            teamRedAdapter = new PlayerAdapter();
            teamRedAdapter.setStatusArr(getTeamRedStatusArray());
            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(RecyclerView.HORIZONTAL);
            manager.setStackFromEnd(true);
            recyclerView_teamRedPlayers.setLayoutManager(manager);
            recyclerView_teamRedPlayers.setAdapter(teamRedAdapter);
        }

        teamBlueAdapter.setStatusArr(getTeamBlueStatusArray());
        teamRedAdapter.setStatusArr(getTeamRedStatusArray());

        teamBlueAdapter.notifyDataSetChanged();
        teamRedAdapter.notifyDataSetChanged();
    }


    private PlayerStatus[] getTeamBlueStatusArray()
    {
        return Arrays.copyOfRange(playerStatusArr, 0, playerStatusArr.length / 2);
    }

    private PlayerStatus[] getTeamRedStatusArray()
    {
        return Arrays.copyOfRange(playerStatusArr, playerStatusArr.length / 2, playerStatusArr.length);
    }

    public void userSelect(Integer position)
    {
        Integer[] arr = new Integer[3];
        arr[0] = gameInfo.getGameID();
        arr[1] = position;
        arr[2] = counter.getTime();
        Event event = new Event(11, arr, System.currentTimeMillis());
        Memory.automata.receiveEvent(event);
    }

    public Boolean getUiLock()
    {
        return uiLock;
    }
}
