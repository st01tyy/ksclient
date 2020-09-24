package edu.bistu.ksclient;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import edu.bistu.ksclient.automata.Event;
import edu.bistu.ksclient.model.Subject;

public class SubjectAdapter extends Adapter<SubjectAdapter.ViewHolder>
{
    private Subject[] subjects;

    Integer val = 0;

    public void setSubjects(Subject[] subjects)
    {
        this.subjects = subjects;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout linearLayout_subjectItem;
        ImageView imageView_subjectIcon;
        TextView textView_subjectName;

        Long subjectID;

        Integer tag = val + 1;

        ViewHolder(View view)
        {
            super(view);
            Log.d(getClass().getName(), "view holder constructor()");
            linearLayout_subjectItem = view.findViewById(R.id.linearLayout_subjectItem);
            imageView_subjectIcon = view.findViewById(R.id.imageView_subjectIcon);
            textView_subjectName = view.findViewById(R.id.textView_subjectName);
            subjectID = 0L;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.linearLayout_subjectItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                builder.setTitle(viewHolder.textView_subjectName.getText());
                builder.setMessage("开始游戏？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        Log.d(getClass().getName(), "用户点击了科目" + viewHolder.subjectID + viewHolder.textView_subjectName.getText() +
                                "viewHolder.tag = " + viewHolder.tag);
                        Event event = new Event(4, viewHolder.subjectID, System.currentTimeMillis());
                        Memory.automata.receiveEvent(event);
                    }
                });
                builder.setNegativeButton("取消", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.textView_subjectName.setText(subjects[position].getName());
        holder.subjectID = subjects[position].getId();
        Log.d(getClass().getName(), "position " + position + "bind complete"
                + "holder.subjectID = " + holder.subjectID + "subjects[position].getId() = " + subjects[position].getId() +
                " viewHolder.tag = " + holder.tag);
        /* 设置图标 */
    }

    @Override
    public int getItemCount()
    {
        return subjects.length;
    }
}
