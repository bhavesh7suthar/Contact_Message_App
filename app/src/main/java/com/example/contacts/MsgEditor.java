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
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.contacts.Data.ContactDbHelper;
import com.example.contacts.Data.ContactsContract;

public class MsgEditor extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //long newRowId;
    Uri insertedUri;
    EditText fromNameEditView;
    EditText toNameEditView;
    EditText textEditView;
    Uri mCurrMsgUri;

//    ContactDbHelper mDbHelper;
//    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_editor);

        Intent intent =getIntent();
        mCurrMsgUri = intent.getData();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(mCurrMsgUri!=null){
            setTitle("Edit Msg");
            getSupportLoaderManager().initLoader(0,null,this);
        }
        else{
            setTitle("New Msg");
        }

//        mDbHelper = new ContactDbHelper(this);
//        db=mDbHelper.getWritableDatabase();

        fromNameEditView =(EditText) findViewById(R.id.from_name_msg);
        toNameEditView =(EditText) findViewById(R.id.to_name_msg);
        textEditView = (EditText) findViewById(R.id.msg_text);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(MsgEditor.this,Messages.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveMsg(){
        String fromName = fromNameEditView.getText().toString().trim();
        String toName = toNameEditView.getText().toString().trim();
        String text = textEditView.getText().toString().trim();

        ContentValues values = new ContentValues();
        values.put(ContactsContract.MsgEntry.MSG_SEND_NAME,fromName);
        values.put(ContactsContract.MsgEntry.MSG_RECEIVE_NAME,toName);
        values.put(ContactsContract.MsgEntry.MSG_TEXT,text);

        insertedUri=getContentResolver().insert(ContactsContract.MsgEntry.Msg_Uri,values);
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void deleteMsg(){
        insertedUri = mCurrMsgUri;
        getContentResolver().delete(mCurrMsgUri,null,null);
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void deleteMsgButton(View view){
        deleteMsg();
        Intent intent = new Intent(MsgEditor.this,Messages.class);
        startActivity(intent);
        Toast.makeText(this, insertedUri+"-Id Msg Deleted", Toast.LENGTH_SHORT).show();

    }
    private void updateMsg(){
        fromNameEditView=(EditText) findViewById(R.id.from_name_msg);
        toNameEditView=(EditText) findViewById(R.id.to_name_msg);
        textEditView=(EditText) findViewById(R.id.msg_text);

        String fromName = fromNameEditView.getText().toString().trim();
        String toName = toNameEditView.getText().toString().trim();
        String text = textEditView.getText().toString().trim();

        insertedUri = mCurrMsgUri;

        ContentValues values = new ContentValues();
        values.put(ContactsContract.MsgEntry.MSG_SEND_NAME,fromName);
        values.put(ContactsContract.MsgEntry.MSG_RECEIVE_NAME,toName);
        values.put(ContactsContract.MsgEntry.MSG_TEXT,text);

        getContentResolver().update(mCurrMsgUri,values,null,null);

    }
    public void saveMsgButton(View view){
        if(mCurrMsgUri!=null){
            updateMsg();
        }
        else{
            saveMsg();
        }
        Intent intent = new Intent(MsgEditor.this,Messages.class);
        startActivity(intent);
        Toast.makeText(this, insertedUri+"-Id Msg Saved", Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                ContactsContract.MsgEntry.MSG_ID,
                ContactsContract.MsgEntry.MSG_SEND_NAME,
                ContactsContract.MsgEntry.MSG_RECEIVE_NAME,
                ContactsContract.MsgEntry.MSG_TEXT
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                ContactsContract.MsgEntry.Msg_Uri,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (data.moveToFirst()) {

            int fromNameColumnIndex = data.getColumnIndex(ContactsContract.MsgEntry.MSG_SEND_NAME);
            int toNameColumnIndex = data.getColumnIndex(ContactsContract.MsgEntry.MSG_RECEIVE_NAME);
            int textColumnIndex = data.getColumnIndex(ContactsContract.MsgEntry.MSG_TEXT);

            String fromName = data.getString(fromNameColumnIndex);
            String toName = data.getString(toNameColumnIndex);
            String text = data.getString(textColumnIndex);

            fromNameEditView.setText(fromName);
            toNameEditView.setText(toName);
            textEditView.setText(text);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        fromNameEditView.setText("");
        toNameEditView.setText("");
        textEditView.setText("");
    }
}