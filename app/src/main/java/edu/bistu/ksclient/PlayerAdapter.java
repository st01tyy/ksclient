package edu.bistu.ksclient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.bistu.ksclient.model.PlayerStatus;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder>
{
    private PlayerStatus[] statusArr;

    class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView_playerStatus;
        TextView textView_playerName;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imageView_playerStatus = itemView.findViewById(R.id.imageView_playerStatus);
            textView_playerName = itemView.findViewById(R.id.textView_playerName);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        ViewHolder viewHolder=  new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.textView_playerName.setText(statusArr[position].getName());
        Integer status = statusArr[position].getStatus();
        if(status == 0)
            holder.imageView_playerStatus.setImageResource(R.mipmap.idle);
        else if(status == 1)
            holder.imageView_playerStatus.setImageResource(R.mipmap.correct);
        else
            holder.imageView_playerStatus.setImageResource(R.mipmap.incorrect);
    }

    @Override
    public int getItemCount()
    {
        return statusArr.length;
    }

    public void setStatusArr(PlayerStatus[] statusArr)
    {
        this.statusArr = statusArr;
    }
}
