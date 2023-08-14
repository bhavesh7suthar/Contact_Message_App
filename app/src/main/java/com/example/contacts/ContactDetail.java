package com.example.contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contacts.Data.ContactsContract;

public class ContactDetail extends AppCompatActivity {

    Uri newRowId;
    TextView nameTextView;
    TextView numTextView;
    String num;
    private Uri mContactUri;

    // Variable Declaration

    private EditText number_input;

    //Request Code
    private int request_Code = 101;

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
                num = curNum;
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

    public void callButton(View view){
        int check_permission = ContextCompat.checkSelfPermission(ContactDetail.this, Manifest.permission.CALL_PHONE);

        if (check_permission == PackageManager.PERMISSION_GRANTED) {
            makePhoneCall();
        } else {
            ActivityCompat.requestPermissions(ContactDetail.this, new String[]{Manifest.permission.CALL_PHONE}, request_Code);
        }
    }

    private void makePhoneCall() {

        String phone_Number = num;

        if (phone_Number.trim().length() > 0) {            // it will trim or dlt empty spaces

            String dial = "tel:" + phone_Number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        } else {
            Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 77:
                if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makePhoneCall();
                } else {
                    Toast.makeText(this, "You Don't have permission", Toast.LENGTH_LONG).show();
                }
        }
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