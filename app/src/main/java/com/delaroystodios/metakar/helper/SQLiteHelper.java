package com.delaroystodios.metakar.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelper extends SQLiteOpenHelper
{

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    public void queryData(String sql)
    {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData( byte[] image)
    {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO Image_User VALUES (NULL , ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindBlob(1 , image);

        statement.executeInsert();

    }

    public void updateData(String name , String price , byte[] image , int id)
    {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE FOOD SET name = ? , price = ? , image = ? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1 , name);
        statement.bindString(2 , price);
        statement.bindBlob(3 , image);
        statement.bindDouble(4 , (double) id);

        statement.execute();
        database.close();

    }

    public void deleteData(int id)
    {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "DELETE FROM FOOD WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1 , (double)id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql)
    {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql , null);

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}










