package com.smarttoni.dev_tool.database;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import androidx.room.Room;

public class DatabaseCopier {

    private static final  DatabaseCopier DatabaseCopier =new DatabaseCopier();

    private  DatabaseCopier(){}

    public static DatabaseCopier getInstance() {
        return DatabaseCopier;
    }


    public void copyAttachedDatabase(Context context) {
        final File dbPath = context.getDatabasePath("smartoni.db");
        //Log.d("DbPath",dbPath.getAbsolutePath());
        //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/smartoni.db";

        // If the database already exists, return
//        if (dbPath.exists()) {
//            return;
//        }
//        // Make sure we have a path to the file
//        dbPath.getParentFile().mkdirs();
        //Log.d("DbPath",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/smartoni.db");
        // Try to copy database file
        try {
            final OutputStream output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/smartoni.db");//context.getAssets().open("databases/" + databaseName);
            //final InputStream inputStream = context.getAssets().open("databases/" + databaseName);
            final InputStream inputStream = new FileInputStream(dbPath);

            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer, 0, 8192)) > 0) {
                output.write(buffer, 0, length);
            }
            output.flush();
            output.close();
            inputStream.close();
            Toast.makeText(context,"Databse exported to Downloads",Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
            //Log.d(TAG, "Failed to open file", e);
            e.printStackTrace();
        }
    }

}