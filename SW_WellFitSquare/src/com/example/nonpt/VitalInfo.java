package com.example.nonpt;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.R;
import com.example.sw_wellfitsquare.AsyncUseJson;
import com.example.sw_wellfitsquare.ReadData;

public class VitalInfo extends Activity implements OnClickListener , ReadData{
	LinearLayout vital_select_view;
	Button btn_vital_select_view;
	int view_flag=0;
	Button vital_info_btn_health_date, vital_info_btn_health_add;
	
	TextView vital_info_txt_health_date;
	
	AlertDialog.Builder dialog_builder_health_add;
	AlertDialog.Builder dialog_builder_excite_add;
	
	AlertDialog dialog_health;
	AlertDialog dialog_excite;
	
	EditText edit_pressure;
	EditText edit_weight_p;
	EditText edit_weight;
	
	TextView txt_weight, txt_pressure;
	
	WebView vital_webview, exercise_webview;
	
	Intent getintent_main_to_vitalinfo;
	
	
	int dialog_year;
	int dialog_month;
	int dialog_day;
	
	 String temp_day;
	 
	 int flag_record;
	 
	 int flag_sql;
	 
	 // 운동 정보 확인하는 플래그를 위해 select 함
	 
	 TextView vital_info_health_kind_weight, vital_info_health_kind_pressure;
	 
	 ListView vital_list_excite;
	 
	 LinearLayout vital_info_layout_excite_add;
	 
	 EditText vital_exercise_edit_set;
	 
	 //Radio
	 
	 RadioGroup rg1;
	 RadioButton sel_radio0,sel_radio1,sel_radio2,sel_radio3,sel_radio4;
	
	String radio_select;
	int radio_select_int;
	
	private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			
		
				vital_info_txt_health_date.setText("날짜 : " + year + "년 " + (monthOfYear + 1) + "월 "
					+ dayOfMonth + "일");
			
				 dialog_year = year;
				 dialog_month= monthOfYear+1;
				 dialog_day= dayOfMonth;
			
			 	flag_sql=1;
			 
			 	txt_weight.setText("체중 : ");
				txt_pressure.setText("혈압 : ");

				temp_day = dialog_year +"-" + dialog_month+"-"+ dialog_day;
			 
				HashMap<String,String>ab = new HashMap<String,String>();
				List<String> wantGap = new ArrayList<String>();
				ab.put("table","vital_info");
				ab.put("field", "weight, pressure");;
				ab.put("condition", "member_uid="+getintent_main_to_vitalinfo.getStringExtra("member_uid") + " and date="+"\""+temp_day+"\"");;
				
				wantGap.add("weight");
				wantGap.add("pressure");
				
				AsyncUseJson a = new AsyncUseJson(VitalInfo.this,ReadData.Select,wantGap);
				a.execute(ab);
				
			}
				
					
				//Toast.makeText(getApplicationContext(), temp_day.toString(), Toast.LENGTH_SHORT).show();
		
	};
	
	
	
	ArrayList<Member> arMemberList; // 리스트뷰
	MemberAdapter myMemberAdapter;
    ListView paperlistview;
		
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vital_info);
		getintent_main_to_vitalinfo = getIntent();
		initId();
		initSetting();
		initTab();
		
		
		
		
		
		
		
		
		Context mContext = getApplicationContext();
		LayoutInflater inflater_health = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View layout_health = inflater_health.inflate(R.layout.custom_dialog,(ViewGroup) findViewById(R.id.layout_root));
		
//		Toast.makeText(getApplicationContext(), getintent_main_to_vitalinfo.getStringExtra("member_uid"), Toast.LENGTH_SHORT).show();
		
		
		 HashMap<String,String>ab = new HashMap<String,String>();
		List<String> wantGap = new ArrayList<String>();
		ab.put("table","exercise_info");
		ab.put("field", "back, chest, arm, stomach, leg");
		ab.put("condition", "member_uid="+getintent_main_to_vitalinfo.getStringExtra("member_uid"));
		
		wantGap.add("back");
		wantGap.add("chest");
		wantGap.add("arm");
		wantGap.add("stomach");
		wantGap.add("leg");
		
		AsyncUseJson a = new AsyncUseJson(VitalInfo.this,ReadData.Select,wantGap);
		a.execute(ab);
		
		
		arMemberList = new ArrayList<Member>();
		
		
	    
	
	    
		
		
		
		
		
		dialog_builder_health_add = new AlertDialog.Builder(VitalInfo.this);
//		aDialog.setTitle("체중 혈압 추가/수정");
		dialog_builder_health_add.setView(layout_health);
								
		dialog_builder_health_add.setPositiveButton("추가/수정", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				edit_pressure = (EditText)layout_health.findViewById(R.id.edit_pressure);
				edit_weight_p = (EditText)layout_health.findViewById(R.id.edit_weight_p);
				edit_weight = (EditText)layout_health.findViewById(R.id.edit_weight);
				
				
				
				
				txt_weight.setText("체중 : "+edit_weight.getText().toString());
				txt_pressure.setText("혈압 : "+edit_pressure.getText().toString());
				//edit_weight_p.setText(edit_weight_p.getText().toString());
				
				flag_sql=2;
				
				if(flag_record==0) //측정기록 없을 
				{
					HashMap<String,String> ab = new HashMap<String,String>();
					List<String> wantGap = new ArrayList<String>();
					ab.put("table","vital_info");
					ab.put("field","member_uid,weight,pressure,date");
					ab.put("data", "\""+getintent_main_to_vitalinfo.getStringExtra("member_uid")+"\",\""+edit_weight.getText().toString()+"\",\""+edit_pressure.getText().toString()+"\",\""+temp_day+"\"");
					
					/*ab.put("field","trainer_uid,member_uid,date,start_date,end_date");
					ab.put("data", "1,1,\""+t_d+"\""+",\""+s_d+"\",\""+e_d+"\"");*/
					AsyncUseJson a = new AsyncUseJson(VitalInfo.this,ReadData.Insert,wantGap);
					a.execute(ab);
				} else if(flag_record==1)
				{
					HashMap<String,String> ab = new HashMap<String,String>();
					List<String> wantGap = new ArrayList<String>();
					ab.put("table","vital_info");
					//ab.put("field","member_uid="+"\""+getintent_main_to_vitalinfo.getStringExtra("member_uid") + "\",weight=\""+edit_weight.getText().toString() +"\",pressure="+"\""+ edit_pressure.getText().toString() +"\", date =\""+temp_day+"\"");
					//ab.put("data", "\""+getintent_main_to_vitalinfo.getStringExtra("member_uid")+"\",\""+edit_weight.getText().toString()+"\",\""+edit_pressure.getText().toString()+"\",\""+temp_day+"\"");
					ab.put("field","weight,pressure");
					ab.put("data", "\""+edit_weight.getText().toString()+"\",\""+edit_pressure.getText().toString()+"\"");
					ab.put("condition","member_uid=\""+getintent_main_to_vitalinfo.getStringExtra("member_uid")+"\" && date=\""+temp_day+"\"");
					ab.put("overlap","0");   // 중복체크는 1
					
					/*ab.put("field","trainer_uid,member_uid,date,start_date,end_date");
					ab.put("data", "1,1,\""+t_d+"\""+",\""+s_d+"\",\""+e_d+"\"");*/

					AsyncUseJson a = new AsyncUseJson(VitalInfo.this,ReadData.Update,wantGap);
					a.execute(ab);
					
				}

			}
		});
		dialog_builder_health_add.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		
		dialog_health = dialog_builder_health_add.create();
		
		
		
//////////////////////////////////////
		
		LayoutInflater inflater_exercite = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View layout_exercite = inflater_exercite.inflate(R.layout.vital_info_dialog_exercite,(ViewGroup) findViewById(R.id.layout_exercise_root));
		
		dialog_builder_excite_add = new AlertDialog.Builder(VitalInfo.this);
//		aDialog.setTitle("체중 혈압 추가/수정");
		dialog_builder_excite_add.setView(layout_exercite);
		
		
		
		
		
		
		rg1 = (RadioGroup)layout_exercite.findViewById(R.id.radioGroup1);
		
		 sel_radio0 = (RadioButton) layout_exercite.findViewById(R.id.radio0);
		 sel_radio1 = (RadioButton) layout_exercite.findViewById(R.id.radio1);
		 sel_radio2 = (RadioButton) layout_exercite.findViewById(R.id.radio2);
		 sel_radio3 = (RadioButton) layout_exercite.findViewById(R.id.radio3);
		 sel_radio4 = (RadioButton) layout_exercite.findViewById(R.id.radio4);
								
		dialog_builder_excite_add.setPositiveButton("입력", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				/*edit_pressure = (EditText)layout_health.findViewById(R.id.edit_pressure);
				edit_weight_p = (EditText)layout_health.findViewById(R.id.edit_weight_p);
				edit_weight = (EditText)layout_health.findViewById(R.id.edit_weight);
				
				txt_weight.setText("체중 : "+edit_weight.getText().toString());*/

				
				/*rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if(checkedId != -1)
						{
							RadioButton rb = (RadioButton)findViewById(checkedId);
							if(rb !=null)
							{
								radio_select = group.indexOfChild(rb);
							}
						}
					}
				});*/
				
				vital_exercise_edit_set = (EditText)layout_exercite.findViewById(R.id.edit_set);
				
				String select_body[] = {"등","가슴","팔","배","다리"}; 
				
				if(sel_radio0.isChecked())
				{
					radio_select="back";
					radio_select_int=0;
				}else if(sel_radio1.isChecked())
					{
					radio_select="chest";
					radio_select_int=1;
				}else if(sel_radio2.isChecked())
				{
					radio_select="arm";
					radio_select_int=2;
				}else if(sel_radio3.isChecked())
				{
					radio_select="stomach";
					radio_select_int=3;
				}else if(sel_radio4.isChecked())
				{
					radio_select="leg";
					radio_select_int=4;
				}
							
				flag_sql=2;

				
				
				int temp_vital_exercise_edit_set;
				
				if(vital_exercise_edit_set.length()>0)
				{
					temp_vital_exercise_edit_set = Integer.parseInt(vital_exercise_edit_set.getText().toString());
				}else{
					temp_vital_exercise_edit_set=0;
				}
				
				
				
				
				HashMap<String,String> ab = new HashMap<String,String>();
				List<String> wantGap = new ArrayList<String>();
				ab.put("table","exercise_info");
				//ab.put("field","member_uid="+"\""+getintent_main_to_vitalinfo.getStringExtra("member_uid") + "\",weight=\""+edit_weight.getText().toString() +"\",pressure="+"\""+ edit_pressure.getText().toString() +"\", date =\""+temp_day+"\"");
				//ab.put("data", "\""+getintent_main_to_vitalinfo.getStringExtra("member_uid")+"\",\""+edit_weight.getText().toString()+"\",\""+edit_pressure.getText().toString()+"\",\""+temp_day+"\"");
				ab.put("field","member_uid,"+radio_select);
				ab.put("data", "\""+getintent_main_to_vitalinfo.getStringExtra("member_uid")+"\",\""+String.valueOf(Integer.parseInt(VitalInfo_HashList.get(0).get(radio_select))+temp_vital_exercise_edit_set)+"\"");
				ab.put("condition","member_uid=\""+getintent_main_to_vitalinfo.getStringExtra("member_uid")+"\"");
				ab.put("overlap","1");   // 중복체크는 1
				
				/*ab.put("field","trainer_uid,member_uid,date,start_date,end_date");
				ab.put("data", "1,1,\""+t_d+"\""+",\""+s_d+"\",\""+e_d+"\"");*/

				AsyncUseJson a = new AsyncUseJson(VitalInfo.this,ReadData.Update,wantGap);
				a.execute(ab);
				
				
				
				arMemberList.set(radio_select_int, new Member(select_body[radio_select_int],  String.valueOf(Integer.parseInt(VitalInfo_HashList.get(0).get(radio_select))+temp_vital_exercise_edit_set)));
				
				HashMap<String,String> wg = VitalInfo_HashList.get(0);
				wg.put(radio_select,String.valueOf(Integer.parseInt(VitalInfo_HashList.get(0).get(radio_select))+temp_vital_exercise_edit_set));	
				
				
				VitalInfo_HashList.set(0, wg);
				
			    myMemberAdapter.notifyDataSetInvalidated();
			    
//				Toast.makeText(getApplicationContext(), String.valueOf(radio_select), Toast.LENGTH_SHORT).show();
			}



		});
		dialog_builder_excite_add.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		
		dialog_excite = dialog_builder_excite_add.create(); // 원래 위치
	}

	private void initTab() {
		TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);
		TabHost.TabSpec ts;
		tabs.setup();
		
		ts = tabs.newTabSpec("one");
		ts.setContent(R.id.one);
		ts.setIndicator("건강");
		tabs.addTab(ts);
		
		ts = tabs.newTabSpec("two");
		ts.setContent(R.id.two);
		ts.setIndicator("운동");
		tabs.addTab(ts);
		
		tabs.setCurrentTab(0);
		
	}

	private void initId() {
//		vital_select_view = (LinearLayout) findViewById(R.id.vital_select_view);
//		btn_vital_select_view = (Button) findViewById(R.id.btn_vital_select_view);
		vital_info_btn_health_date = (Button)findViewById(R.id.vital_info_btn_health_date);
		vital_info_btn_health_add= (Button)findViewById(R.id.vital_info_btn_health_add);
		
		vital_info_txt_health_date = (TextView)findViewById(R.id.vital_info_txt_health_date);
	
		txt_weight = (TextView)findViewById(R.id.txt_weight);
		txt_pressure = (TextView)findViewById(R.id.txt_pressure);
		
		vital_webview = (WebView)findViewById(R.id.vital_webview);
		exercise_webview = (WebView)findViewById(R.id.exercise_webview);
		
		vital_info_health_kind_weight = (TextView)findViewById(R.id.vital_info_health_kind_weight);
		vital_info_health_kind_pressure = (TextView)findViewById(R.id.vital_info_health_kind_pressure);
		
		vital_list_excite = (ListView) findViewById(R.id.vital_list_excite);

		vital_info_layout_excite_add = (LinearLayout)findViewById(R.id.vital_info_layout_excite_add);
	}

	private void initSetting() {
//		btn_vital_select_view.setOnClickListener(this);
		vital_info_btn_health_date.setOnClickListener(this);
		vital_info_btn_health_add.setOnClickListener(this);
	
		vital_info_health_kind_pressure.setOnClickListener(this);
		vital_info_health_kind_weight.setOnClickListener(this);
		
		vital_info_layout_excite_add.setOnClickListener(this);
		
//		vital_webview.setInitialScale(5);
//		vital_webview.getZoomControls();
		vital_webview.getSettings().setJavaScriptEnabled(true);
		exercise_webview.getSettings().setJavaScriptEnabled(true);
//		vital_webview.loadUrl("http://172.16.100.81:10001/chart/chart_show.php?uid=1&kind=weight");
		
		
		vital_webview.loadUrl("http://61.43.139.69/chart/chart_show_weight.php?uid="+getintent_main_to_vitalinfo.getStringExtra("member_uid"));
		exercise_webview.loadUrl("http://61.43.139.69/chart/chart_exercise.php?uid="+getintent_main_to_vitalinfo.getStringExtra("member_uid"));
		//http://172.16.101.227:10001/chart/chart_show_pressure.php?uid=1
		
		flag_record=0;
	
		//float btn_to_width = vital_info_btn_health_date.getWidth()/2; 
		//float btn_to_height = vital_info_btn_health_date.getHeight()/2;
		
//		Toast.makeText(getApplicationContext(), String.valueOf(btn_to_width), Toast.LENGTH_SHORT).show();
		
//		vital_info_btn_health_date.setLayoutParams((LayoutParams) new LayoutParams(31, 31));
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.vital_info_btn_health_date:
//			new Datepickerdialog(VitalInfo.this, d);
//			new DatePickerDialog(this, dateListener, 2010, 0, 15);
			
			showDialog(0);
			break;
		case R.id.vital_info_btn_health_add:
			
			if(vital_info_txt_health_date.length()<6)
			{
				Toast.makeText(getApplicationContext(), "날짜를 먼저 선택해 주세요.", Toast.LENGTH_SHORT).show();
				return;
			}
			//ad = aDialog.create(); //원래 다른 위치에 있었음
			dialog_health.show();
			break;
			
			
		case R.id.vital_info_health_kind_pressure:
			vital_webview.loadUrl("http://172.16.101.227:10001/chart/chart_show_pressure.php?uid="+getintent_main_to_vitalinfo.getStringExtra("member_uid"));

			//http://172.16.101.227:10001/chart/chart_show_pressure.php?uid=1
			break;
			
		case R.id.vital_info_health_kind_weight:
			vital_webview.loadUrl("http://172.16.101.227:10001/chart/chart_show_weight.php?uid="+getintent_main_to_vitalinfo.getStringExtra("member_uid"));
			break;
			
		case R.id.vital_info_layout_excite_add:
			
			dialog_excite.show();
			break;
			
		/*case R.id.btn_vital_select_view:
			if(view_flag==0)
			{
				vital_select_view.setVisibility(View.VISIBLE);
				view_flag=1;
			} else {
				vital_select_view.setVisibility(View.GONE);
				view_flag=0;
			}
			break;*/
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return this.getParent().onKeyDown(keyCode, event);
		}
		return false;
	}
	
	
	@Override
    protected Dialog onCreateDialog(int id){
        switch(id){
        case 0:
//        	SimpleDateFormat formater = new SimpleDateFormat("yyyy.MM.dd",Locale.KOREA);
//        	Date current = new Date();
        	Calendar calendar = Calendar.getInstance();
        	
        	
        	
        	
        	DatePickerDialog d = new DatePickerDialog(this, dateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//        	d.setTitle("ggggg");
        	
            return  d;
        }
        return null;
    }
	
	
	List<HashMap<String, String>> VitalInfo_HashList;

	@Override
	public void setData(List<HashMap<String, String>> HashList) {
		//String s = HashList.toString();

//		TrainnerHashList =  HashList;
		
//	Toast.makeText(getApplicationContext(), TrainnerHashList.toString(), Toast.LENGTH_SHORT).show();
		//Toast.makeText(getApplicationContext(), s.toString(), Toast.LENGTH_LONG).show();
		
		
		
			if(flag_sql==1)  //  1이면 건강정보 컨트롤   2는 운동정보 컨트롤 
			{
				if(HashList!=null)
				{
					txt_weight.setText("체중 : "+HashList.get(0).get("weight"));
					txt_pressure.setText("혈압 : "+HashList.get(0).get("pressure"));
					flag_record=1;
				}else{
					Toast.makeText(getApplicationContext(), "측정 기록이 없습니다.", Toast.LENGTH_SHORT).show();
					flag_record=0;
				}
			}else if(flag_sql==2)
			{
				Toast.makeText(getApplicationContext(), HashList.toString(), Toast.LENGTH_SHORT).show();
			}
			
			VitalInfo_HashList = HashList;
			
				arMemberList.add(new Member(("등"), VitalInfo_HashList.get(0).get("back")));
				arMemberList.add(new Member(("가슴"), VitalInfo_HashList.get(0).get("chest")));
				arMemberList.add(new Member(("팔"), VitalInfo_HashList.get(0).get("arm")));
				arMemberList.add(new Member(("배"), VitalInfo_HashList.get(0).get("stomach")));
				arMemberList.add(new Member(("다리"), VitalInfo_HashList.get(0).get("leg")));
				

			  
			
		     myMemberAdapter = new MemberAdapter(this, arMemberList);
		     paperlistview = (ListView) findViewById(R.id.vital_list_excite);
		     
		    paperlistview.setAdapter(myMemberAdapter);
		    
		    
		    
		    
		    
//			Toast.makeText(getApplicationContext(), HashList.toString(), Toast.LENGTH_SHORT).show();
			//Toast.makeText(getApplicationContext(), HashList.toString(), Toast.LENGTH_SHORT).show();

	}
	
	
	
	
	
	
	int flag;
	class MemberAdapter extends BaseAdapter{

		LayoutInflater mInflater;
		ArrayList<Member> arSrc;
		Context _context;
		
		public MemberAdapter(Context context, ArrayList<Member> personListItem)	{
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arSrc = personListItem;
			_context = context;
		}
		
		public int getCount() {
			return arSrc.size();
		}

		public Object getItem(int position) {
			return arSrc.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			
			
			
			
			int res = R.layout.vitalinfo_list;
			convertView = mInflater.inflate(res, parent,false);
			
			
			TextView tools_name = (TextView)convertView.findViewById(R.id.name);
			tools_name.setText(arSrc.get(position).name);
			
			TextView sports_name = (TextView)convertView.findViewById(R.id.set);
			sports_name.setText(arSrc.get(position).set);
			
			
			
			
			


			
			return convertView;
		}
	}
	
	
	
	
	class Member{
		int image;
		String tools_name;
		String sports_name;
		String tools_way;
		String sports_content;
		String sports_data;
		String name;
		String set;

		public Member(String _name, String _set)
		{
			name = _name;
			set = _set;
		}
		
		public Member(int _image, String tools_name, String sports_name, String tools_way, String sports_content, String sports_data)
		{
			this.image = _image;
			this.tools_name = tools_name;
			this.sports_name = sports_name;
			this.tools_way = tools_way;
			this.sports_content = sports_content;
			this.sports_data = sports_data;
		}
		
		
	}




	@Override
	public void NullData() {
		// TODO Auto-generated method stub
		
	}
	
	
	

}