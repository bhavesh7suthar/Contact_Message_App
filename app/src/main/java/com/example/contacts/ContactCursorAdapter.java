package com.example.contacts;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;

import com.example.contacts.Data.ContactsContract;
import com.example.contacts.R;

public class ContactCursorAdapter extends CursorAdapter {
    public ContactCursorAdapter(Context context,Cursor c){
        super(context,c,0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //initialize a list item using list_item.xml file
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summeryTextView = (TextView) view.findViewById(R.id.summary);

        int nameColumnIndex = cursor.getColumnIndex(ContactsContract.ContactEntry.CONTACT_NAME);
        int summeryColumnIndex = cursor.getColumnIndex(ContactsContract.ContactEntry.CONTACT_NUM);

        String contactName = cursor.getString(nameColumnIndex);
        String contactSummery = cursor.getString(summeryColumnIndex);

        nameTextView.setText(contactName);
        summeryTextView.setText(contactSummery);
    }
}