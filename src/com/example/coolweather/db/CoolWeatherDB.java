package com.example.coolweather.db;

import java.util.ArrayList;
import java.util.List;

import com.example.coolweather.model.City;
import com.example.coolweather.model.Country;
import com.example.coolweather.model.Province;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.UrlQuerySanitizer.ValueSanitizer;


public class CoolWeatherDB {
	//���ݿ���
	public static final String DB_NAME= "cool_weather";
	
	//���ݿ�汾
	public static final int VERSION=1;
	
	private static CoolWeatherDB coolWeatherDB;
	
	private SQLiteDatabase db;
	
	//˽�й��캯��
	private CoolWeatherDB(Context context){
		CoolWeatherOpenHelper dbHelper=new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db=dbHelper.getWritableDatabase();
	}
	
	//��ȡcoolWeatherDB��ʵ��
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolWeatherDB==null){
			coolWeatherDB=new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	//�洢ʡ����Ϣ
	public void saveProvince(Province province) {
		ContentValues values=new ContentValues();
		values.put("province_name", province.getProvinceName());
		values.put("province_code", province.getProvinceCode());
		db.insert("Province", null, values);
	}
	//��ȡʡ����Ϣ
	public List<Province> loadProvinces() {
		List<Province> list =new ArrayList<Province>();
		Cursor cursor=db.query("province", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
				do{
					Province province=new Province();
					province.setId(cursor.getInt(cursor.getColumnIndex("id")));
					province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
					province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
					list.add(province);
				}while(cursor.moveToNext());			
			}
		return list;
		}
	
	//�洢City
	public void saveCity(City city){
		if (city!=null) {
			ContentValues values=new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);			
		}
	}
	//��ȡCity
	public List<City> loadCities(int provinceId) {
		List<City> list =new ArrayList<City>();
		Cursor cursor=db.query("City", null, "province_id=?", new String[]{String.valueOf(provinceId)}, null, null, null);
		if (cursor.moveToFirst()) {
			do{
				City city=new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			}while(cursor.moveToNext());
		}
		return list;
	}
	
	//�洢country
	public void saveCountry(Country country) {
		if (country!=null) {
			ContentValues values=new ContentValues();
			values.put("country_name", country.getCountryName());
			values.put("country_code", country.getCountryCode());
			values.put("city_id", country.getCityId());
			db.insert("Country", null, values);
		}		
	}
	//��ȡcountry
	public List<Country> loadCountries(int cityId) {
		List<Country> list =new ArrayList<Country>();
		Cursor cursor=db.query("Country", null, "city_id=?", new String[]{String.valueOf(cityId)}, null, null, null);
		if (cursor.moveToFirst()) {
			Country country=new Country();
			country.setId(cursor.getInt(cursor.getColumnIndex("id")));;
			country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
			country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
			country.setCityId(cityId);
			list.add(country);
		}
		return list;
	}
}
