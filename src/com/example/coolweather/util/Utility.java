package com.example.coolweather.util;

import android.text.TextUtils;

import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.model.City;
import com.example.coolweather.model.Country;
import com.example.coolweather.model.Province;

//-----解析处理城市分类-----
public class Utility {
	
	//解析和处理服务器返回的省级信息
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response) { 
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinces=response.split(",");
			if (allProvinces!=null &&allProvinces.length>0) {
				for (String provinceString : allProvinces) {
					String[] array=provinceString.split("\\|");
					Province province=new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					//将解析出来的数据存储到Province表
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}else {
				return false;
			}
		}
		return false;
	}
	
	//解析和处理服务器返回的市级信息
	public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCities=response.split(",");
			if (allCities!=null && allCities.length>0) {
				for (String cityString : allCities) {
					String[] array=cityString.split("\\|");
					City city=new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					coolWeatherDB.saveCity(city);
				}
				return true;
			}else {
				return false;
			}
		}
		return false;
	}
	
	//解析和处理服务器返回的村镇级信息
	public synchronized static boolean handleCountriesResponse(CoolWeatherDB coolWeatherDB,String response, int cityId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCountrise=response.split(",");
			if (allCountrise!=null&&allCountrise.length>0) {
				for (String countryString : allCountrise) {
					String[] array=countryString.split("\\|");
					Country country=new Country();
					country.setCountryCode(array[0]);
					country.setCountryName(array[1]);
					country.setCityId(cityId);
					coolWeatherDB.saveCountry(country);
				}
				return true;
			}else {
				return false;
			}			
		}
		return false;
	}
}
