package com.example.contacts.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Switch;

public class ContactProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = ContactProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the pets table */
    private static final int CONTACTS = 100;
    private static final int CONTACT_ID = 101;
    private static final int MSG = 200;
    private static final int MSG_ID = 201;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.contact/contacts" will map to the
        // integer code {@link #CONTACTS}. This URI is used to provide access to MULTIPLE rows
        // of the pets table.
        sUriMatcher.addURI(ContactsContract.Content_Authority, ContactsContract.CONTACT_PATH, CONTACTS);
        sUriMatcher.addURI(ContactsContract.Content_Authority, ContactsContract.MSG_PATH,MSG);

        // The content URI of the form "content://com.example.android.pets/pets/#" will map to the
        // integer code {@link #CONTACT_ID}. This URI is used to provide access to ONE single row
        // of the pets table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.example.contacts/contacts/3" matches, but
        // "content://com.example.android.pets/pets" (without a number at the end) doesn't match.
        sUriMatcher.addURI(ContactsContract.Content_Authority, ContactsContract.CONTACT_PATH+"/#", CONTACT_ID);
        sUriMatcher.addURI(ContactsContract.Content_Authority, ContactsContract.MSG_PATH+"/#", MSG_ID);
    }

    /**
     * Initialize the provider and the database helper object.
     */

    private ContactDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        // TODO: Create and initialize a PetDbHelper object to gain access to the pets database.
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.

        mDbHelper=new ContactDbHelper(getContext());

        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        //we know mDbHelper object has been made in onCreate method
        //so know only calling database object
        SQLiteDatabase db=mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match){
            case CONTACTS:
                cursor = db.query(ContactsContract.ContactEntry.CONTACT_TABLE_NAME,projection,selection,selectionArgs,
                        null,null,sortOrder);
                break;
            case CONTACT_ID:

                //in this uri where clause will introduced
                //so need to modify selection and selectionArgs
                selection = ContactsContract.ContactEntry.CONTACT_ID + "=?";
                //if we need more column then more ? will be inserted like "=???"
                //then all column id will be inserted in selection

                //selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                //ContentUris Method is converting
                cursor = db.query(ContactsContract.ContactEntry.CONTACT_TABLE_NAME,projection,selection,selectionArgs,
                        null,null,sortOrder);

                break;
            case MSG:
                cursor = db.query(ContactsContract.MsgEntry.MSG_TABLE_NAME,projection,selection,selectionArgs,
                        null,null,sortOrder);
                break;
            case MSG_ID:

                //in this uri where clause will introduced
                //so need to modify selection and selectionArgs
                selection = ContactsContract.MsgEntry.MSG_ID + "=?";
                //if we need more column then more ? will be inserted like "=???"
                //then all column id will be inserted in selection

                //selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                //ContentUris Method is converting
                cursor = db.query(ContactsContract.MsgEntry.MSG_TABLE_NAME,projection,selection,selectionArgs,
                        null,null,sortOrder);

                break;
            default:
                //no saved uri matched
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri insertUri;
        long insertId;
        switch (match){
            case CONTACTS:
                 insertId = db.insert(ContactsContract.ContactEntry.CONTACT_TABLE_NAME,null,contentValues);
                break;
            case MSG:
                insertId = db.insert(ContactsContract.MsgEntry.MSG_TABLE_NAME,null,contentValues);
                break;
//            case CONTACT_ID:
//
//                //in this uri where clause will introduced
//                //so need to modify selection and selectionArgs
//                selection = ContactsContract.ContactEntry.CONTACT_ID + "=?";
//                //if we need more column then more ? will be inserted like "=???"
//                //then all column id will be inserted in selection
//
//                //selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
//                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
//                //ContentUris Method is converting
//                cursor = db.query(ContactsContract.ContactEntry.CONTACT_TABLE_NAME,projection,selection,selectionArgs,
//                        null,null,sortOrder);
//
//                break;
            default:
                //no saved uri matched
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        insertUri=Uri.withAppendedPath(uri,Long.toString(insertId));

        //Notify all lister that the data has changed for the contact uri
        getContext().getContentResolver().notifyChange(uri,null);
        return insertUri;
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        switch (match){
            case CONTACT_ID:

                //in this uri where clause will introduced
                //so need to modify selection and selectionArgs
                selection = ContactsContract.ContactEntry.CONTACT_ID + "=?";
                //if we need more column then more ? will be inserted like "=???"
                //then all column id will be inserted in selection

                //selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                //ContentUris Method is converting
                db.update(ContactsContract.ContactEntry.CONTACT_TABLE_NAME,contentValues,selection,selectionArgs);

                break;
            case MSG_ID:

                //in this uri where clause will introduced
                //so need to modify selection and selectionArgs
                selection = ContactsContract.MsgEntry.MSG_ID + "=?";
                //if we need more column then more ? will be inserted like "=???"
                //then all column id will be inserted in selection

                //selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                //ContentUris Method is converting
                db.update(ContactsContract.MsgEntry.MSG_TABLE_NAME,contentValues,selection,selectionArgs);

                break;
            default:
                //no saved uri matched
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);

        return 0;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        //track the no. of row that were deleted
        int rowDeleted ;

        switch (match) {
            case CONTACTS:
                rowDeleted=db.delete(ContactsContract.ContactEntry.CONTACT_TABLE_NAME, selection, selectionArgs);
                break;
            case CONTACT_ID:
                //in this uri where clause will introduced
                //so need to modify selection and selectionArgs
                selection = ContactsContract.ContactEntry.CONTACT_ID + "=?";
                //if we need more column then more ? will be inserted like "=???"
                //then all column id will be inserted in selection

                //selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                rowDeleted=db.delete(ContactsContract.ContactEntry.CONTACT_TABLE_NAME,selection,selectionArgs);
                break;
            case MSG:
                rowDeleted=db.delete(ContactsContract.MsgEntry.MSG_TABLE_NAME, selection, selectionArgs);
                break;
            case MSG_ID:
                //in this uri where clause will introduced
                //so need to modify selection and selectionArgs
                selection = ContactsContract.MsgEntry.MSG_ID + "=?";
                //if we need more column then more ? will be inserted like "=???"
                //then all column id will be inserted in selection

                //selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                //ContentUris Method is converting
                rowDeleted=db.delete(ContactsContract.MsgEntry.MSG_TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        //Notify all lister that the data has changed for the contact uri
            getContext().getContentResolver().notifyChange(uri, null);

        return rowDeleted;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {

        return null;
    }
}
