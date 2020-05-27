package com.android.safecarbs.main.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AssetsDataBaseHelper {

    private Context context;
    private String DB_NAME;
    private String DB_PATH;
    private String TAG = "DbAssetHelper";

    public AssetsDataBaseHelper(Context context, String database_name) {
        this.context = context;
        this.DB_NAME = database_name;
    }

    public void saveDatabase() {
        File dbFile = context.getDatabasePath(DB_NAME);
        DB_PATH = context.getDatabasePath(DB_NAME).getPath();

        if (!dbFile.exists()) {

            copyDatabase(dbFile);
            Log.i(TAG,"DB " + DB_NAME + " created in " +  context.getDatabasePath(DB_NAME).toString());
        }else {
            Log.i(TAG, "DB " + DB_NAME + " exists");
        }

            SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READONLY);
    }

    private void copyDatabase(File dbFile)  {

        try {
            InputStream    is = context.getAssets().open(DB_NAME);
            Log.i(TAG, is.toString());
            OutputStream os = new FileOutputStream(DB_PATH);

            byte[] buffer = new byte[1024];
            while (is.read(buffer) > 0) {
                os.write(buffer);
            }

            os.flush();
            os.close();
            is.close();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }

}