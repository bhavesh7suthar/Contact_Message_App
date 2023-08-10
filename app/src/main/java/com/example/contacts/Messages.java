package com.example.contacts;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.contacts.Data.ContactsContract;

public class Messages extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

//    ContactDbHelper mDbHelper;
//    SQLiteDatabase db;

    /** Adapter for the ListView */
    MsgCursorAdapter mCursorAdapter;

    TextView displayMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        setTitle("Messages");
//        mDbHelper = new ContactDbHelper(this);
//        db = mDbHelper.getReadableDatabase();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ListView msgListView = (ListView) findViewById(R.id.msg_list);
        mCursorAdapter = new MsgCursorAdapter(this,null);
        msgListView.setAdapter(mCursorAdapter);

        msgListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent intent = new Intent(Messages.this,MsgEditor.class);
                Uri currMsgUri = ContentUris.withAppendedId(ContactsContract.MsgEntry.Msg_Uri,id);
                intent.setData(currMsgUri);
                startActivity(intent);
            }
        });
//        public View onCreateView(
//                LayoutInflater inflater,
//                ViewGroup viewGroup,
//                Bundle bundle) {

        // Kick off the loader
        getSupportLoaderManager().initLoader(0, null,this);

        displayMsg=(TextView) findViewById(R.id.msg_info);
        DisplayMsgInfo();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Messages.this,MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllMsg() {
        getContentResolver().delete(ContactsContract.MsgEntry.Msg_Uri,null,null);
        //db.execSQL("DELETE FROM "+ ContactsContract.MsgEntry.MSG_TABLE_NAME);
    }

    private void DisplayMsgInfo(){
        String[] projection ={ContactsContract.MsgEntry.MSG_ID,
                ContactsContract.MsgEntry.MSG_SEND_NAME,
                ContactsContract.MsgEntry.MSG_RECEIVE_NAME,
                ContactsContract.MsgEntry.MSG_TEXT
        };
        String selection;
        String[] selectionArgs;

//        Cursor cursor = db.query(ContactsContract.MsgEntry.MSG_TABLE_NAME,
//                projection,
//                null,
//                null,
//                null,
//                null,
//                null
//        );

        Cursor cursor = getContentResolver().query(ContactsContract.MsgEntry.Msg_Uri,projection,
                null,
                null,
                null
        );

        try{
            displayMsg.setText("ID - FROM - TO - Text\n");
            //find column index
            //store them in constant
            //then iterate loop to access all info

            //get column index inside cursor
            int idIndex = cursor.getColumnIndex(ContactsContract.MsgEntry.MSG_ID);
            int fromNameIndex = cursor.getColumnIndex(ContactsContract.MsgEntry.MSG_SEND_NAME);
            int textIndex = cursor.getColumnIndex(ContactsContract.MsgEntry.MSG_TEXT);
            int toNameIndex = cursor.getColumnIndex(ContactsContract.MsgEntry.MSG_RECEIVE_NAME);

            //loop to iterate all the contact inserted
            while (cursor.moveToNext()) {
                int currId = cursor.getInt(idIndex);
                String currFromName = cursor.getString(fromNameIndex);
                String currToName = cursor.getString(toNameIndex);
                String currText = cursor.getString(textIndex);

                displayMsg.append(currId + "\t" + currFromName + "\t" + currToName + "\t" + currText +"\n");
            }

        }
        finally {
            cursor.close();
        }
    }

    public void addMsg(View view){
        Intent intent = new Intent(Messages.this,MsgEditor.class);
        startActivity(intent);
    }

    public void deleteAllMsgButton(View view){
        deleteAllMsg();
        Toast.makeText(this,"All Messages Deleted", Toast.LENGTH_SHORT).show();
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
                ContactsContract.MsgEntry.MSG_SEND_NAME + " ASC");                  // Default sort order
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
}