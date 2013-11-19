package com.example.sw_wellfitsquare_nonmember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.common.AsyncUseJson_nonmember;
import com.example.common.ReadData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class SW_WellFitSquareNonmember extends Activity implements OnMarkerClickListener, ReadData{
	
	ArrayList<Member> MapList;
	int temp;
	GoogleMap map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sw__well_fit_square_nonmember);
		map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();
		
		map.setOnMarkerClickListener(this);
		MapList = new ArrayList<Member>();
		
		 
		 
		 HashMap<String, String> ab = new HashMap<String, String>();
			
			List<String> wantGap = new ArrayList<String>();
			ab.put("table", "fitness_list");
			ab.put("field", "uid,name,phone,latitude,longitude,area");
			ab.put("condition", "uid>0");
			wantGap.add("uid");
			wantGap.add("name"); 
			wantGap.add("phone"); 
			wantGap.add("latitude"); 
			wantGap.add("longitude"); 
			wantGap.add("area");

			AsyncUseJson_nonmember a = new AsyncUseJson_nonmember(this, ReadData.Select, wantGap,0);
			a.execute(ab);
			
			
			
			
			
			
	}
	
	
	
	
	

	
	class Member {
		String name;
		String number;
		String locate;
		Double let;
		Double len;
		int uid;
		
		public Member(String _name, String _number, String _locate, Double _let, Double _len, int _uid) {
				name = _name;
				number = _number;
				locate = _locate;
				let = _let;
				len = _len;
				uid = _uid;
			}
		}





	@Override
	public boolean onMarkerClick(Marker arg0) {
		/*Intent intent = new Intent(getApplicationContext(), Dialog_Info.c
		 * lass);
		startActivity(intent);*/
		
//		Toast.makeText(getApplicationContext(), String.valueOf(MapList.)), Toast.LENGTH_SHORT).show();
		
		int temp = 0;
		
		for(int i=0;i<MapList.size();i++)
		{
			if(MapList.get(i).name.equals(arg0.getTitle()))
			{
				temp = i;
			}
		}
		
		Intent intent = new Intent(getApplicationContext(), Dialog_Info.class);
		intent.putExtra("name",MapList.get(temp).name);
		intent.putExtra("number",MapList.get(temp).number);
		intent.putExtra("locate",MapList.get(temp).locate);
		intent.putExtra("uid",MapList.get(temp).uid);
		
		startActivity(intent);
		
		return false;
	}





	@Override
	public void setData(List<HashMap<String, String>> HashList) {
		// TODO Auto-generated method stub
		
	}





	@Override
	public void NullData() {
		
	}


	@Override
	public void setData(List<HashMap<String, String>> hashList, int requestCode) {
		if(requestCode==0)
		{
			for(int i=0;i<hashList.size();i++)
			{
				MapList.add(new Member(hashList.get(i).get("name"), hashList.get(i).get("phone"), hashList.get(i).get("area"),Double.parseDouble(hashList.get(i).get("latitude")),Double.parseDouble(hashList.get(i).get("longitude")), Integer.parseInt(hashList.get(i).get("uid"))));
			}
			
			 
			 map.setMyLocationEnabled(true);
			 
			 LatLng test_gps = new LatLng(37.5044790,127.0489410);
			 map.moveCamera(CameraUpdateFactory.newLatLngZoom(test_gps, 17));
			 
			 		 
				 for(int i=0;i<MapList.size();i++)
				 {
					 test_gps = new LatLng(MapList.get(i).let,MapList.get(i).len);
		
					 map.addMarker(new MarkerOptions()
				        .position(test_gps)
				        .title(MapList.get(i).name)
					 );
				 }
				 
				 
		}
		
		
		
	}





	@Override
	public void NullData(int requestCode) {
		// TODO Auto-generated method stub
		
	}
}
