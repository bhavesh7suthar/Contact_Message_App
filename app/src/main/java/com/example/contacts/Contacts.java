package com.example.contacts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contacts.Data.ContactsContract;

public class Contacts extends AppCompatActivity implements View.OnClickListener,LoaderManager.LoaderCallbacks<Cursor> {

//    ContactDbHelper mDbHelper;
//    SQLiteDatabase db;

    /** Identifier for the pet data loader */
    private static final int CONTACT_LOADER = 0;

    /** Adapter for the ListView */
    ContactCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

//        mDbHelper = new ContactDbHelper(this);
//        db = mDbHelper.getReadableDatabase();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ListView contactListView = (ListView) findViewById(R.id.list);
        mCursorAdapter = new ContactCursorAdapter(this,null);
        contactListView.setAdapter(mCursorAdapter);

        DisplayDataBaseInfo();
        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent intent = new Intent(Contacts.this,ContactDetail.class);
                Uri currContactUri = ContentUris.withAppendedId(ContactsContract.ContactEntry.Contact_Uri,id);
                intent.setData(currContactUri);
                startActivity(intent);
            }
        });
//        public View onCreateView(
//                LayoutInflater inflater,
//                ViewGroup viewGroup,
//                Bundle bundle) {

        // Kick off the loader
        getSupportLoaderManager().initLoader(CONTACT_LOADER, null,this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Contacts.this,MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

//    public boolean onCreateOptionMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.main_menu,menu);
//        return true;
//    }

    private void deleteAllContact() {
        getContentResolver().delete(ContactsContract.ContactEntry.Contact_Uri,null,null);
        Toast.makeText(this, " ID Contact Deleted", Toast.LENGTH_SHORT).show();
    }

    private void DisplayDataBaseInfo(){
        String[] projection = {ContactsContract.ContactEntry.CONTACT_ID,
                ContactsContract.ContactEntry.CONTACT_NAME,
                ContactsContract.ContactEntry.CONTACT_NUM
        };
        String selection;
        String[] selectionArgs;

//        Cursor cursor = db.query(ContactsContract.ContactEntry.CONTACT_TABLE_NAME,
//                projection,
//                null,
//                null,
//                null,
//                null,
//                null
//        );



        Cursor cursor = getContentResolver().query(ContactsContract.ContactEntry.Contact_Uri,projection,
                null,
                null,
                null
        );

//        // Kick off the loader
//        getLoaderManager().initLoader(CONTACT_LOADER,null,this);

        TextView DisplayView = (TextView) findViewById(R.id.contact_info_text_view);
        try {
            DisplayView.setText("CONTACT INFO\n ID  -   NAME   -   NUMBER\n");

            //get column index inside cursor
            int idIndex = cursor.getColumnIndex(ContactsContract.ContactEntry.CONTACT_ID);
            int nameIndex = cursor.getColumnIndex(ContactsContract.ContactEntry.CONTACT_NAME);
            int numIndex = cursor.getColumnIndex(ContactsContract.ContactEntry.CONTACT_NUM);

            //loop to iterate all the contact inserted
            while (cursor.moveToNext()) {
                int currId = cursor.getInt(idIndex);
                String currName = cursor.getString(nameIndex);
                String curNum = cursor.getString(numIndex);

                DisplayView.append(currId + "\t" + currName + "\t" + curNum + "\n");
            }
        }
        finally {
            cursor.close();
        }

    }

    public void addContact(View view){
        Intent intent = new Intent(Contacts.this,ContactEditor.class);
        startActivity(intent);
    }

    public void deleteAllContactButton(View view){
        deleteAllContact();
        Toast.makeText(this,"All Contacts Deleted", Toast.LENGTH_SHORT).show();
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                ContactsContract.ContactEntry.CONTACT_ID,
                ContactsContract.ContactEntry.CONTACT_NAME,
                ContactsContract.ContactEntry.CONTACT_NUM
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                ContactsContract.ContactEntry.Contact_Uri,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                ContactsContract.ContactEntry.CONTACT_NAME);                  // Default sort order
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // Update {@link PetCursorAdapter} with this new cursor containing updated pet data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }

    @Override
    public void onClick(View view) {

    }
}