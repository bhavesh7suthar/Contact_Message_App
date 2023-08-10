package com.example.contacts.Data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.contacts.Data.ContactsContract.ContactEntry;


public class ContactDbHelper extends SQLiteOpenHelper {
    //Gave version if we want to update in future and database name constant
    public static final int DATABASE_VERSION = 1;
    public static final String  DATABASE_NAME = "Contact.db";

    //Constructor of this class
    public ContactDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    //SQLite Commands
    public  static String SQL_CONTACT_CREATE_ENTRIES = "CREATE TABLE " +ContactEntry.CONTACT_TABLE_NAME+"(" +ContactEntry.CONTACT_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+ContactEntry.CONTACT_NAME+ " TEXT, "+ContactEntry.CONTACT_NUM+ " INTEGER);";
    public  static String SQL_MSG_CREATE_ENTRIES = "CREATE TABLE " + ContactsContract.MsgEntry.MSG_TABLE_NAME+"(" + ContactsContract.MsgEntry.MSG_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+ ContactsContract.MsgEntry.MSG_SEND_NAME + " TEXT, "+ ContactsContract.MsgEntry.MSG_RECEIVE_NAME+ " TEXT, "+ContactsContract.MsgEntry.MSG_TEXT+ " TEXT);";
    public  static String SQL_MSG_DELETE_ENTRIES = "DROP TABLE "+ContactsContract.MsgEntry.MSG_TABLE_NAME+";";
    public  static String SQL_CONTACT_DELETE_ENTRIES = "DROP TABLE "+ContactEntry.CONTACT_TABLE_NAME+";";


    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CONTACT_CREATE_ENTRIES);
        db.execSQL(SQL_MSG_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db,int old_version,int new_version){
        db.execSQL(SQL_CONTACT_DELETE_ENTRIES);
        db.execSQL(SQL_MSG_DELETE_ENTRIES);
        onCreate(db);
    }
}
