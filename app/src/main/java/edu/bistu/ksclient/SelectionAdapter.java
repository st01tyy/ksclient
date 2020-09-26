package edu.bistu.ksclient;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import edu.bistu.ksclient.model.Selection;

public class SelectionAdapter extends RecyclerView.Adapter<SelectionAdapter.ViewHolder>
{
    private GameActivity master;
    private Selection[] selections;

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private Button button_selection;

        private Drawable drawable;

        private Integer position;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            button_selection = itemView.findViewById(R.id.button_selection);
            drawable = button_selection.getBackground();
            button_selection.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(!master.getUiLock())
                    {
                        master.lockDownUI();
                        if(selections[position].getAnswer())
                            button_selection.setBackgroundColor(ContextCompat.getColor(master, R.color.colorTrue));
                        else
                            button_selection.setBackgroundColor(ContextCompat.getColor(master, R.color.colorFalse));

                        master.userSelect(position);
                    }
                }
            });
        }

        void setPosition(Integer position)
        {
            this.position = position;
        }
    }

    public SelectionAdapter(GameActivity master, Selection[] selections)
    {
        this.master = master;
        this.selections = selections;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selection, parent, false);
        ViewHolder viewHolder=  new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.button_selection.setText(selections[position].getDescription());
        holder.button_selection.setBackground(holder.drawable);
        holder.setPosition(position);
    }

    @Override
    public int getItemCount()
    {
        return selections.length;
    }

    public void setArr(Selection[] arr) {
        this.selections = arr;
    }
}
