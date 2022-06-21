package com.example.androidchat.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidchat.ChatLand;
import com.example.androidchat.ChatPageActivity;
import com.example.androidchat.Models.Contact;
import com.example.androidchat.R;


import java.util.List;


public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {
    @SuppressLint("StaticFieldLeak")
    static private Context startActivityPage;
    public static int isLand = 0;
    static class ContactViewHolder extends RecyclerView.ViewHolder {



        private final TextView contactName;
        private final TextView contactNickName;
        private final TextView lastMessage;
        private final TextView contactLastSeen;
        private final ImageView contactImage;

        private ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            contactImage = itemView.findViewById(R.id.imgContact);
            contactLastSeen = itemView.findViewById(R.id.contactLastSeen);
            lastMessage = itemView.findViewById(R.id.contactLastMessage);
            contactNickName = itemView.findViewById(R.id.contactNickName);
            contactName = itemView.findViewById(R.id.contactUserName);

            itemView.setOnClickListener(view -> {
                Intent chat;
                if(isLand == 0) {
                    chat = new Intent(startActivityPage, ChatPageActivity.class);
                    chat.putExtra("id", contactName.getText().toString());
                }
                else {
                    chat = new Intent(startActivityPage, ChatLand.class);
                    chat.putExtra("id", contactName.getText().toString());
                }
                startActivityPage.startActivity(chat);

            });
        }
    }

    private final LayoutInflater mInflater;
    private List<Contact> contactList;


    public ContactListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        startActivityPage = context;
    }


    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.contact_item_view, parent, false);
        return new ContactViewHolder(itemView);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        if (contactList != null) {
            final Contact current = contactList.get(position);


            holder.contactName.setText(current.getId());
            holder.contactNickName.setText(current.getNickname());
            holder.contactLastSeen.setText(current.getLastSeen());
            holder.lastMessage.setText(current.getLastMessage());


            //todo set profile image
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (contactList != null)
            return contactList.size();
        return 0;
    }

    public List<Contact> getContactList() {
        return contactList;
    }
}
