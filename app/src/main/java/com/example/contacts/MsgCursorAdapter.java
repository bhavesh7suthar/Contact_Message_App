package com.example.contacts;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;

import com.example.contacts.Data.ContactsContract;

public class MsgCursorAdapter extends CursorAdapter {
    public MsgCursorAdapter(Context context,Cursor c){
        super(context,c,0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.msg_list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView fromNameTextView = (TextView) view.findViewById(R.id.from_name);
        TextView toNameTextView = (TextView) view.findViewById(R.id.to_name);
        TextView textTextView = (TextView) view.findViewById(R.id.text);

        int fromNameColumnIndex = cursor.getColumnIndex(ContactsContract.MsgEntry.MSG_SEND_NAME);
        int toNameColumnIndex = cursor.getColumnIndex(ContactsContract.MsgEntry.MSG_RECEIVE_NAME);
        int textColumnIndex = cursor.getColumnIndex(ContactsContract.MsgEntry.MSG_TEXT);

        String fromName = cursor.getString(fromNameColumnIndex);
        String toName = cursor.getString(toNameColumnIndex);
        String text = cursor.getString(textColumnIndex);

        fromNameTextView.setText(fromName);
        toNameTextView.setText(toName);
        textTextView.setText(text);
    }
}
