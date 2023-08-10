package com.example.contacts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.contacts.Data.ContactDbHelper;
import com.example.contacts.Data.ContactsContract;

import java.sql.RowId;

public class ContactEditor extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    /** Identifier for the pet data loader */
    private static final int EXISTING_PET_LOADER = 0;
    Uri newRowId;
    EditText mContactNameEditView;
    EditText mContactNumEditView;
    private Uri mCurrContactUri;
//    ContactDbHelper mDbHelper;
//    SQLiteDatabase db;
//    private  View.On
//
//    @Override
    private boolean mContactChanged = false;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mContactChanged=true;
            return false;
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_editor);

        Intent intent =getIntent();
        mCurrContactUri = intent.getData();

        mContactNameEditView =(EditText) findViewById(R.id.name_contact);
        mContactNumEditView =(EditText) findViewById(R.id.num_contact);

//        mDbHelper = new ContactDbHelper(this);
//        db=mDbHelper.getWritableDatabase();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(mCurrContactUri!=null){
            setTitle("Edit Text");

            getSupportLoaderManager().initLoader(EXISTING_PET_LOADER,null,this);
        }
        else{
            setTitle("New Pet");
        }

        mContactNumEditView.setOnTouchListener(mTouchListener);
        mContactNameEditView.setOnTouchListener(mTouchListener);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ContactEditor.this,Contacts.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveContact(){



        String ContactName = mContactNameEditView.getText().toString().trim();
        String ContactNum = mContactNumEditView.getText().toString().trim();

        if(ContactNum.length()!=10){
            Toast.makeText(this, "Put 10 digit mobile no.", Toast.LENGTH_SHORT).show();
        }
        else {
            ContentValues values = new ContentValues();
            values.put(ContactsContract.ContactEntry.CONTACT_NAME, ContactName);
            values.put(ContactsContract.ContactEntry.CONTACT_NUM, ContactNum);

            newRowId = getContentResolver().insert(ContactsContract.ContactEntry.Contact_Uri, values);

            Intent intent = new Intent(ContactEditor.this,Contacts.class);
            startActivity(intent);
            Toast.makeText(this, newRowId+" ID Contact Saved", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateContact(){

        mContactNameEditView =(EditText) findViewById(R.id.name_contact);
        mContactNumEditView =(EditText) findViewById(R.id.num_contact);

        String ContactName = mContactNameEditView.getText().toString().trim();
        String ContactNum = mContactNumEditView.getText().toString().trim();

        if(ContactNum.length()!=10){
            Toast.makeText(this, "Put 10 digit mobile no.", Toast.LENGTH_SHORT).show();
        }
        else {
            newRowId = mCurrContactUri;

            ContentValues values = new ContentValues();
            values.put(ContactsContract.ContactEntry.CONTACT_NAME, ContactName);
            values.put(ContactsContract.ContactEntry.CONTACT_NUM, ContactNum);

            getContentResolver().update(mCurrContactUri, values, null, null);

            Intent intent = new Intent(ContactEditor.this,Contacts.class);
            startActivity(intent);
            Toast.makeText(this, newRowId+" ID Contact Saved", Toast.LENGTH_SHORT).show();

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void deleteContact(){
        newRowId = mCurrContactUri;
        getContentResolver().delete(mCurrContactUri,null,null);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void deleteContactButton(View view){

        deleteContact();
        Intent intent = new Intent(ContactEditor.this,Contacts.class);
        startActivity(intent);
        Toast.makeText(this, newRowId+" ID Contact Deleted", Toast.LENGTH_SHORT).show();
    }

    public void saveContactButton(View view){
        if(mCurrContactUri!=null){
            updateContact();
        }
        else{
            saveContact();
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                //ContactsContract.ContactEntry.CONTACT_ID,
                ContactsContract.ContactEntry.CONTACT_NAME,
                ContactsContract.ContactEntry.CONTACT_NUM
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrContactUri,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (data == null || data.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (data.moveToFirst()) {

            int nameColumnIndex = data.getColumnIndex(ContactsContract.ContactEntry.CONTACT_NAME);
            int numColumnIndex = data.getColumnIndex(ContactsContract.ContactEntry.CONTACT_NUM);

            String currName = data.getString(nameColumnIndex);
            String currNum = data.getString(numColumnIndex);

            mContactNameEditView.setText(currName);
            mContactNumEditView.setText(currNum);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mContactNameEditView.setText("");
        mContactNumEditView.setText("");
    }
}