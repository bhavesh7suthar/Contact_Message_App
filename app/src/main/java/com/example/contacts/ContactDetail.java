package com.example.contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contacts.Data.ContactsContract;

public class ContactDetail extends AppCompatActivity {

    Uri newRowId;
    TextView nameTextView;
    TextView numTextView;
    private Uri mContactUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        nameTextView = (TextView) findViewById(R.id.contact_name_view);
        numTextView=findViewById(R.id.contact_num_view);

        Intent intent =getIntent();
        mContactUri=intent.getData();


        String[] projection = {
                //ContactsContract.ContactEntry.CONTACT_ID,
                ContactsContract.ContactEntry.CONTACT_NAME,
                ContactsContract.ContactEntry.CONTACT_NUM
        };

        Cursor cursor = getContentResolver().query(mContactUri,projection,null,null,null);

        try {
            //get column index inside cursor
            int nameIndex = cursor.getColumnIndex(ContactsContract.ContactEntry.CONTACT_NAME);
            int numIndex = cursor.getColumnIndex(ContactsContract.ContactEntry.CONTACT_NUM);

            //loop to iterate all the contact inserted
            while (cursor.moveToNext()) {
                String currName = cursor.getString(nameIndex);
                String curNum = cursor.getString(numIndex);

                nameTextView.append(currName);
                numTextView.append(curNum);

            }
        }
        finally {
            cursor.close();
        }

    }

    private void deleteCurrContact(){
        newRowId = mContactUri;
        getContentResolver().delete(newRowId,null,null);
    }

    public void deleteContact(View view){
        deleteCurrContact();
        Intent intent = new Intent(ContactDetail.this,Contacts.class);
        startActivity(intent);
        Toast.makeText(this, newRowId+" ID Contact Deleted", Toast.LENGTH_SHORT).show();
    }

    public void editContact(View view){
        Intent intent = new Intent(ContactDetail.this,ContactEditor.class);
        intent.setData(mContactUri);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ContactDetail.this,Contacts.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}