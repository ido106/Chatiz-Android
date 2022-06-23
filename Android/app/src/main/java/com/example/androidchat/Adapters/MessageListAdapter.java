package com.example.androidchat.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidchat.AppSettings.MyApplication;
import com.example.androidchat.Models.Message;
import com.example.androidchat.R;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {

    class MessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView mySendTime;
        private final TextView myData;
        private final LinearLayout myMessageLayoutWarp;


        private final TextView notMySendTime;
        private final TextView notMyData;
        private final LinearLayout notMyMessageLayoutWarp;


        private MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            myMessageLayoutWarp = (LinearLayout) itemView.findViewById(R.id.myMessageLayoutWrap);
            mySendTime = itemView.findViewById(R.id.mySentTimeMessage);
            myData = itemView.findViewById(R.id.myMessageTextData);

            notMyMessageLayoutWarp = (LinearLayout) itemView.findViewById(R.id.notMyMessageLayoutWrap);
            notMySendTime = itemView.findViewById(R.id.notMySentTimeMessage);
            notMyData = itemView.findViewById(R.id.notMyMessageTextData);
        }
    }

    private final LayoutInflater mInflater;
    private List<Message> messageList;


    public MessageListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.message_view, parent, false);
        return new MessageViewHolder(itemView);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        if (messageList != null) {
            final Message current = messageList.get(position);



            holder.mySendTime.setText(current.getTimeSent());
            holder.notMySendTime.setText(current.getTimeSent());

            holder.myData.setText(current.getContent());
            holder.notMyData.setText(current.getContent());


            if (current.isSent()) {
                holder.notMyMessageLayoutWarp.setVisibility(View.GONE);
                holder.myMessageLayoutWarp.setVisibility(View.VISIBLE);
            } else {
                holder.notMyMessageLayoutWarp.setVisibility(View.VISIBLE);
                holder.myMessageLayoutWarp.setVisibility(View.GONE);
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (messageList != null)
            return messageList.size();
        return 0;
    }

    public List<Message> getMessageList() {
        return messageList;
    }
}
