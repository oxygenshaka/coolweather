package com.example.coolweather.util;

import android.text.TextUtils;

import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.model.City;
import com.example.coolweather.model.Country;
import com.example.coolweather.model.Province;

//-----����������з���-----
public class Utility {
	
	//�����ʹ�����������ص�ʡ����Ϣ
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response) { 
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinces=response.split(",");
			if (allProvinces!=null &&allProvinces.length>0) {
				for (String provinceString : allProvinces) {
					String[] array=provinceString.split("\\|");
					Province province=new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					//���������������ݴ洢��Province��
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}else {
				return false;
			}
		}
		return false;
	}
	
	//�����ʹ�����������ص��м���Ϣ
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
	
	//�����ʹ�����������صĴ�����Ϣ
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
