package com.example.sns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.R;
import com.example.sw_wellfitsquare.AsyncUseJson;
import com.example.sw_wellfitsquare.ReadData;

public class Sns_room extends Activity implements ReadData{
	List<room_List> rooms = new ArrayList<room_List>();
    room_Adapter adapter = new room_Adapter();
    String member_uid;
	int check = 0; // check == 0 -> 
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.sns_room);
	    Intent intent = getIntent();
	    member_uid = intent.getStringExtra("member_uid");
	    
	    Button Bt = (Button)findViewById(R.id.sns_room_makeroom);
	    
	    ListView Lv = (ListView)findViewById(R.id.sns_room_list);
	    Lv.setAdapter(adapter);
	    Bt.setOnClickListener(new OnClickListener(){
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			public void onClick(View v) {
				MakingRoom a = new MakingRoom();
				a.show(getFragmentManager(), null);
			}
	    });
	    getData();
	}
	public void getData(){
		if(check == 0){
			check = 1;
			rooms.clear();
			rooms = new ArrayList<room_List>();
			
			HashMap<String,String> a = new HashMap<String,String>();
			List<String> Wantgap = new ArrayList<String>();
			a.put("table", "room");
			a.put("field", "uid,r_name,p_num");
			a.put("condition", "uid >= 0");
			Wantgap.add("uid");
			Wantgap.add("r_name");
			Wantgap.add("p_num");
			AsyncUseJson ab = new AsyncUseJson(this,ReadData.Select,Wantgap);
			ab.execute(a);
		}
		else {
			Toast.makeText(getBaseContext(), "로딩중입니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void Click(int position){
		// 들어가있는 방의 변경 값 눌림.
		// 멤버가 값을 선택함. 
		// check == 2;
		SharedPreferences sharedPref;
		SharedPreferences.Editor editor;
		sharedPref= getSharedPreferences("sns_room_uid",Activity.MODE_PRIVATE);
		editor = sharedPref.edit();
		editor.putString("room_uid",String.valueOf(rooms.get(position).uid));
		editor.commit();
		Toast.makeText(getBaseContext(),rooms.get(position).name + " 방에 입장하였습니다.", Toast.LENGTH_SHORT).show();
		setResult(RESULT_OK);
		finish();
	}
	
	public class room_List{
		int uid;
		String name;
		int P_num;
	}
	
	public class room_Adapter extends BaseAdapter{

		@Override
		public int getCount() {
			return rooms.size();
		}

		@Override
		public Object getItem(int position) {
			return rooms.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.sns_room_list,parent,false);
			
			TextView TV_Name = (TextView)convertView.findViewById(R.id.sns_room_name);
			TextView TV_Pnum = (TextView)convertView.findViewById(R.id.sns_room_pnum);
			TV_Name.setText(rooms.get(position).name);
			TV_Pnum.setText(String.valueOf(rooms.get(position).P_num));
			
			Button room_Button = (Button)convertView.findViewById(R.id.sns_room_button);
			final int pisi = position;
			room_Button.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					Click(pisi);
				}
			});
			return convertView;
		}
		
	}
	@SuppressLint("ValidFragment")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public class MakingRoom extends DialogFragment 
	{
		public Dialog onCreateDialog(Bundle savedInstanceState) {
		    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		    LayoutInflater inflater = getActivity().getLayoutInflater();
		    final View dialogview = inflater.inflate(R.layout.sns_room_making, null); 
     	    
		    builder.setView(dialogview)
		    // Add action buttons
		           .setPositiveButton("Making", new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int id) {
		             	   EditText Et = (EditText)dialogview.findViewById(R.id.sns_room_making_name_ET);
		            	   MakingRoom(Et.getText().toString());
		               }
		           })
		           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int id) {
		                   MakingRoom.this.getDialog().cancel();
		               }
		           });      

     	   	
		    return builder.create();
		}
	}
	public void MakingRoom(String name){
 		Toast.makeText(getBaseContext(),"\""+name+"\""+" 방을 생성합니다.", Toast.LENGTH_SHORT).show();
		if(check == 0){
			check = 3;
			HashMap<String,String> a = new HashMap<String,String>();
			List<String> Wantgap = new ArrayList<String>();
			a.put("table", "room");
			a.put("field", "r_name,p_num");
			a.put("data","\""+name+"\""+", 1");
			AsyncUseJson ab = new AsyncUseJson(this,ReadData.Insert,Wantgap);
			ab.execute(a);
		}
		else {
			Toast.makeText(getBaseContext(), "로딩중입니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void setData(List<HashMap<String, String>> HashList) {
		if(check == 1){
			// 방들을 받아옴 
		    for(HashMap<String,String> i : HashList){
			    room_List rl = new room_List();
		    	rl.uid = Integer.parseInt(i.get("uid"));
			    rl.name = i.get("r_name");
			    rl.P_num = Integer.parseInt(i.get("p_num"));
			    rooms.add(rl);
		    }
		    adapter.notifyDataSetChanged();
		}
		else if(check == 3){
			// 방만듬.
			reloadlist ab = new reloadlist();
			ab.execute();
			return;
		}
		check = 0;
	}
	public class reloadlist extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
				check = 0;
				getData();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}
		
	}
	@Override
	public void NullData() {
		// TODO Auto-generated method stub
		
	}
}
