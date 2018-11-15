package com.example.alilo.alichat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by alilo on 11/10/2018.
 */

public class MyrecycleViewAdapterS extends RecyclerView.Adapter<MyrecycleViewAdapterS.MyviewHolder> {
    private Context mContext;
    private List<message> messagesList;

    public MyrecycleViewAdapterS(Context mContext, List<message> messagesListe) {
        this.mContext = mContext;
        this.messagesList = messagesListe;
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       /* View view;
        LayoutInflater inflater= LayoutInflater.from(mContext);
        view =inflater.inflate(R.layout.message_single_layout,parent,false);*/


        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout2, parent, false);


        return new MyviewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {
//holder.tv_title.setText(messagesList.get(position).getName());
        holder.tvdescr.setText(messagesList.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public static class MyviewHolder extends RecyclerView.ViewHolder {
        TextView tv_title ;
        TextView tvdescr;





        public MyviewHolder(View itemView) {
            super(itemView);

          //  tv_title=(TextView)  itemView.findViewById(R.id.name);
            tvdescr=(TextView)  itemView.findViewById(R.id.messageId);
        }
    }
}
