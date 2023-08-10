package com.example.contacts.Data;

import android.net.Uri;

import java.net.URI;

//Contract of App
public class ContactsContract {

    //URI Constant
    public static final String Content_Authority = "com.example.contacts";
    public static final Uri Base_Content_Uri = Uri.parse("content://"+Content_Authority);
    public static final String CONTACT_PATH = "contacts";
    public static final String MSG_PATH = "msg";


    //Contact table of database
    public static class ContactEntry{
        //following are the constant
        public static Uri Contact_Uri = Uri.withAppendedPath(Base_Content_Uri,CONTACT_PATH);
        public static String CONTACT_TABLE_NAME = "contacts";
        public static String CONTACT_ID = "_id";
        public static String CONTACT_NAME = "name";
        public static String CONTACT_NUM = "number";

        public  static String SQL_CONTACT_CREATE_ENTRIES = "CREATE TABLE " +CONTACT_TABLE_NAME+"(" +CONTACT_ID+ " INTEGER, "+CONTACT_NAME+ " TEXT, "+CONTACT_NUM+ " INTEGER)";
    }
    //Msg table od database
    public static class MsgEntry{
        //Following are the constant
        public static Uri Msg_Uri = Uri.withAppendedPath(Base_Content_Uri,MSG_PATH);
        public static String MSG_TABLE_NAME = "msg";
        public static String MSG_ID = "_id";
        public static String MSG_SEND_NAME = "sender";
        public static String MSG_RECEIVE_NAME = "receiver";
        public static String MSG_TEXT = "text";
    }
}
