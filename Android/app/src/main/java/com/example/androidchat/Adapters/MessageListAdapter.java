package com.example.androidchat.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidchat.Models.Message;
import com.example.androidchat.R;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {

    class MessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView sendTime;
        private final TextView isMine;
        private final TextView data;


        private MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            sendTime = itemView.findViewById(R.id.sendTime);
            isMine = itemView.findViewById(R.id.isMine);
            data = itemView.findViewById(R.id.data);
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
        View itemView = mInflater.inflate(R.layout.my_message_item, parent, false);
        return new MessageViewHolder(itemView);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        if (messageList != null) {
            final Message current = messageList.get(position);
            holder.sendTime.setText(current.getTimeSent());
            if (current.isSent())
                holder.isMine.setText("@strings/myMessage");
            else
                holder.isMine.setText("@strings/notMyMessage");
            holder.data.setText(current.getContent());
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
