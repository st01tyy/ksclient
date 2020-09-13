package edu.bistu.ksclient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import edu.bistu.ksclient.model.Subject;

public class SubjectAdapter extends Adapter<SubjectAdapter.ViewHolder>
{
    private Subject[] subjects;

    public void setSubjects(Subject[] subjects)
    {
        this.subjects = subjects;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout linearLayout_subjectItem;
        ImageView imageView_subjectIcon;
        TextView textView_subjectName;

        ViewHolder(View view)
        {
            super(view);
            linearLayout_subjectItem = view.findViewById(R.id.linearLayout_subjectItem);
            imageView_subjectIcon = view.findViewById(R.id.imageView_subjectIcon);
            textView_subjectName = view.findViewById(R.id.textView_subjectName);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.textView_subjectName.setText(subjects[position].getName());
        /* 设置图标 */
    }

    @Override
    public int getItemCount()
    {
        return subjects.length;
    }
}
