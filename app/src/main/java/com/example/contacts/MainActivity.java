package com.example.contacts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void msgView(View view){
        Intent intent = new Intent(MainActivity.this,Messages.class);
        startActivity(intent);
    }

    public void contactView(View view){
        Intent intent = new Intent(MainActivity.this,Contacts.class);
        startActivity(intent);
    }


}