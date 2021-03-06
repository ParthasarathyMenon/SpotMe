package com.steelsty.spotme;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.Vector;

public class DbUtil extends SQLiteOpenHelper {
    public DbUtil(Context context) {
        super(context, "SpotMe.db", null,2);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String query= "create table Alarms(id INTEGER PRIMARY KEY NOT NULL, " +
                "place TEXT NOT NULL, " +
                "time TEXT NOT NULL, " +
                "date TEXT NOT NULL, " +
                "lat TEXT NOT NULL," +
                "lng TEXT NOT NULL," +
                "active INTEGER NOT NULL)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Alarms");
        db.close();
        onCreate(db);
    }

    public int alarmID(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT id FROM Alarms";
        Cursor c = db.rawQuery(query, null);
        int id=0;
        if(c.getCount()!=0) {
            c.moveToLast();
            id =c.getInt(0);
        }
        c.close();
        db.close();
        return (id+1);
    }

    public String addressID(int id){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT place FROM Alarms where id="+id;
        Cursor c = db.rawQuery(query, null);
        String place="";
        if(c.getCount()!=0) {
            c.moveToLast();
            place =c.getString(0);
        }
        c.close();
        db.close();
        return place;
    }

    public LatLng latlngID(int id){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT lat,lng FROM Alarms where id="+id;
        Cursor c = db.rawQuery(query, null);
        LatLng l=new LatLng(Globals.lat,Globals.lng);
        if(c.getCount()!=0) {
            c.moveToLast();
            double lat =Double.parseDouble(c.getString(0));
            double lng=Double.parseDouble(c.getString(1));
            l= new LatLng(lat,lng);
        }
        c.close();
        db.close();
        return l;
    }

    public void setActive(int id){
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE Alarms SET active=0 where id=?";
        SQLiteStatement insertStmt      =   db.compileStatement(query);
        insertStmt.clearBindings();
        insertStmt.bindLong(1,id);
        insertStmt.executeUpdateDelete();
        db.close();
    }

    public Vector<String> alarm(int id){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM Alarms where id="+id;
        Cursor c = db.rawQuery(query, null);
        Vector<String> v = new Vector<String>();
        if(c.getCount()!=0) {
            c.moveToFirst();
            v.add(0,c.getInt(0)+"");
            v.add(1,c.getString(1));
            v.add(2,c.getString(2));
            v.add(3,c.getString(3));
            v.add(4,c.getInt(6)+"");
        }
        c.close();
        db.close();
        return v;
    }

    public int getAlarmCount() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM Alarms";
        Cursor c = db.rawQuery(query, null);
        int count = c.getCount();
        c.close();
        db.close();
        return count;
    }

    public void deleteAlarms() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM Alarms");
        db.close();
    }

    public void deleteAlarmsId(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM Alarms where id="+id);
        db.close();
    }

    public void insertAlarm(int id,String place,String time,String date,String lat,String lng,int active){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try{

            String sql   =   "INSERT INTO Alarms "
                    +  "VALUES(?,?,?,?,?,?,?)";

            SQLiteStatement insertStmt      =   db.compileStatement(sql);
            insertStmt.clearBindings();
            insertStmt.bindLong(1,id);
            insertStmt.bindString(2,place);
            insertStmt.bindString(3, time);
            insertStmt.bindString(4,date);
            insertStmt.bindString(5,lat);
            insertStmt.bindString(6,lng);
            insertStmt.bindLong(7, active);
            insertStmt.executeInsert();
            db.setTransactionSuccessful();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            try
            {
                db.endTransaction();
                db.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public Vector<Vector<String>> getAlarms() {
        SQLiteDatabase db = getWritableDatabase();
        Vector<Vector<String>> vectData = new Vector<Vector<String>>();
        try {
            String query = "SELECT * FROM Alarms";
            Cursor c = db.rawQuery(query, null);
            while(c.moveToNext()) {
                Vector<String> vectObj = new Vector<String>();
                vectObj.add(c.getInt(0) + "");
                vectObj.add(c.getString(1));
                vectObj.add(c.getString(2));
                vectObj.add(c.getString(3));
                vectObj.add(c.getLong(6)+"");
                vectData.add(vectObj);
            }
            c.close();
            return vectData;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            db.close();
        }
        return vectData;
    }

}
