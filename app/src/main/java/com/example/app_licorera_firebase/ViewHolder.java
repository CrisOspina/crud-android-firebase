package com.example.app_licorera_firebase;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView mNombre, mDescripcion, mPrecio;
    View mView;

    //View Holder


    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });
        //item long click listener
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });

        //initialize views with model_layout.xml
        mNombre = itemView.findViewById(R.id.rNombre);
        mDescripcion = itemView.findViewById(R.id.rDescripcion);
        mPrecio = itemView.findViewById(R.id.rPrecio);
    }

    private ViewHolder.ClickLlistener mClickListener;

    //iterface for click listener
    public interface ClickLlistener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder.ClickLlistener clickListener){
        mClickListener = clickListener;
    }
}
