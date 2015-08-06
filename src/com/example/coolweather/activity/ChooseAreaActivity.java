package com.example.coolweather.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.coolweather.R;
import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.model.City;
import com.example.coolweather.model.Country;
import com.example.coolweather.model.Province;
import com.example.coolweather.util.HttpCallbackListener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;


import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Advanceable;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {
	//ѡ�񼶱�
	public static final int LEVEL_PROVINCE=0;
	public static final int LEVEL_CITY=1;
	public static final int LEVEL_COUNTRY=2;
	//���ݿ�
	private CoolWeatherDB coolWeatherDB;
	
	//�ؼ�
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	
	//�б���ͼ���
	private ArrayAdapter<String> adapter;
	private List<String> datalist =new ArrayList<String>();
	
	//ʡ�б�
	private List<Province> provinceList;
	//ѡ�е�ʡ
	private Province selectedProcince;
	
	//���б�
	private List<City> cityList;
	//ѡ�е���
	private City selectedCity;
	
	//�����б�
	private List<Country> countryList;
	//ѡ�еĴ���
	private Country selectedCountry;
	
	//��ǰѡ�еļ���
	private int currentLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView=(ListView)findViewById(R.id.list_view);
		titleText=(TextView)findViewById(R.id.title_text);
		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,datalist);
		listView.setAdapter(adapter);
		
		coolWeatherDB=CoolWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (currentLevel == LEVEL_PROVINCE) {
					selectedProcince=provinceList.get(position);
					queryCities();
				}else if (currentLevel==LEVEL_CITY) {
					selectedCity=cityList.get(position);
					queryCountries();
				}
			}
			
		});
		
		//����ʡ������
		queryProvinces();
	}
	
	private void queryProvinces() {
		provinceList=coolWeatherDB.loadProvinces();
		if (provinceList.size()>0) {
			datalist.clear();
			for (Province province : provinceList) {
				datalist.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("�й�");
			currentLevel=LEVEL_PROVINCE;
		}else {
			queryFromServer(null,"province");
		}
	}
	
	
	private void queryCities() {
		cityList=coolWeatherDB.loadCities(selectedProcince.getId());
		if (cityList.size()>0) {
			datalist.clear();
			for (City city : cityList) {
				datalist.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProcince.getProvinceName());;
			currentLevel=LEVEL_CITY;
		}else {
			queryFromServer(selectedProcince.getProvinceCode(),	"city");
		}
	}
	
	private void queryCountries() {
		countryList=coolWeatherDB.loadCountries(selectedCity.getId());
		if (countryList.size()>0) {
			datalist.clear();
			for (Country country : countryList) {
				datalist.add(country.getCountryName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel=LEVEL_COUNTRY;
		}else {
			queryFromServer(selectedCity.getCityCode(), "country");
		}
	}
	
	private void queryFromServer(final String code,final String type) {
		String address;
		if (!TextUtils.isEmpty(code)) {
			address="http://www.weather.com.cn/data/list3/city"+code+".xml";
		}else{
			address="http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequset(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean result=false;
				if("province".equals(type)){
					result=Utility.handleProvincesResponse(coolWeatherDB, response);
				}else if ("city".equals(type)) {
					result=Utility.handleCitiesResponse(coolWeatherDB, response, selectedProcince.getId());
				}else if ("country".equals(type)) {
					result=Utility.handleCountriesResponse(coolWeatherDB, response, selectedCity.getId());
				}
				
				if (result) {
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							if ("province".equals(type)) {
								queryProvinces();
							}else if ("city".equals(type)) {
								queryCities();
							}else if("country".equals(type)) {
								queryCountries();
							}
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new  Runnable() {
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();;
					}
				});
			}
		});
	}
	
	private void showProgressDialog() {
		if (progressDialog==null) {
			progressDialog =new ProgressDialog(this);
			progressDialog.setMessage("���ڼ���...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	
	private void closeProgressDialog() {
		if(progressDialog!=null){
			progressDialog.dismiss();
		}		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		if(currentLevel==LEVEL_COUNTRY){
			queryCities();
		}else if(currentLevel==LEVEL_CITY) {
			queryProvinces();
		}else if(currentLevel==LEVEL_PROVINCE) {
			new AlertDialog.Builder(ChooseAreaActivity.this)
			.setMessage("��ȷ��Ҫ�˳���")  
			.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					finish();//�رս���
				}
			})  
			.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					dialog.dismiss();
				}
			}).show();
		}
	}
	
	
}
