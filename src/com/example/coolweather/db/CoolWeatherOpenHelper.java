package com.example.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

	//Province±í´´½¨Óï¾ä
	public static final String CREATE_PROVINCE_TABLE="create table Province ("
													 +"id integer primary key autoincrement,"
													 +"province_name text,"
													 +"province_code text)";
	public static final String CREATE_CITY_TABLE    ="create table City ("
													 +"id integer primary key autoincrement,"
													 +"city_name text,"
													 +"city_code text,"
													 +"province_id integer)";
	public static final String CREATE_COUNTRY_TABLE ="create table Country ("
												     +"id integer primary key autoincrement,"
												     +"country_name text,"
												     +"country_code text,"
												     +"city_id integer)";
	
	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_PROVINCE_TABLE);
		db.execSQL(CREATE_CITY_TABLE);
		db.execSQL(CREATE_COUNTRY_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
