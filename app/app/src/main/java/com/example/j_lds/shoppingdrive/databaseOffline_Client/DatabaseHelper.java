package com.example.j_lds.shoppingdrive.databaseOffline;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import android.content.Context;

import com.example.j_lds.shoppingdrive.databaseOffline.model.Settings;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "Shopping_Drive.db";

    private static String TABLE_NAME_SETTINGS = "Settings";

    private static String TABLE_SETTING_COL1 = "id";
    private static String TABLE_SETTING_COL2 = "selectedMerchantUid";
    private static String TABLE_SETTING_COL3 = "selectedMerchantCompanyName";


    //Create Query table strings
    private final String createSettingsTable = "create table "+TABLE_NAME_SETTINGS+" ("+
            TABLE_SETTING_COL1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            TABLE_SETTING_COL2+" TEXT, "+
            TABLE_SETTING_COL3+" TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        Log.e("DB: ", "Database created");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createSettingsTable);
        Log.e("DB: ", "Tables created");

        db.execSQL("insert into "+TABLE_NAME_SETTINGS+" values(1, '','')");
        Log.e("DB: ", "Default Settings created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_SETTINGS);
        Log.e("DB: ", "Tables Deleted");
        onCreate(db);
    }


    // CRUD Base Ticket Table
    //Default
    public void defaultSettings(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into "+TABLE_NAME_SETTINGS+" values(1, '','')");
        Log.e("DB: ", "default insert is done");
    }

    //Add
    public boolean insertSettings(Settings settings){
        SQLiteDatabase db = this.getWritableDatabase();

        if (!settings.getSelectedMerchantUid().isEmpty() && !settings.getSelectedMerchantCompanyName().isEmpty()){
            db.execSQL("insert into "+TABLE_NAME_SETTINGS+
                    " values(1, '"+settings.getSelectedMerchantUid()+"', '"+settings.getSelectedMerchantCompanyName()+"')");
            Log.e("DB: ", "insert is done");
            return true;

        }else {
            Log.e("DB: ", "insert failed");
            return false;
        }
    }

    //Get
    public Cursor getAllSettingsData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME_SETTINGS, null);
        return res;
    }

    //Update
    public boolean updateSettingsData(Settings settings) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (!settings.getSelectedMerchantUid().isEmpty() && !settings.getSelectedMerchantCompanyName().isEmpty()){
            db.execSQL("update "+TABLE_NAME_SETTINGS+
                    " set "+TABLE_SETTING_COL2+" = '"+settings.getSelectedMerchantUid()+"', "+
                    TABLE_SETTING_COL3+" = '"+settings.getSelectedMerchantCompanyName()+"' where "+TABLE_SETTING_COL1+" = "+settings.getId());
            Log.e("DB: ", "ID: "+settings.getId()+" is updated");
            return true;
        }else {
            Log.e("DB: ", "ID: "+settings.getId()+" failed to update");
            return false;
        }
    }


    //Delete
    public Integer deleteSettingsData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_SETTINGS, TABLE_SETTING_COL1+" = ?", new String[] {String.valueOf(id)});
    }
}
