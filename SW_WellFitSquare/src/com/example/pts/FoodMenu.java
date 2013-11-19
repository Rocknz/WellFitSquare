package com.example.pts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.example.R;
import com.example.sw_wellfitsquare.AsyncUseJson;
import com.example.sw_wellfitsquare.ReadData;

public class FoodMenu extends Activity implements ReadData{

	GestureDetector mGesture;
	float disx,disy;
	Activity parent;
	TabHost tabs;
	ListView[] foodtext = new ListView[3];
	long h=0,h1=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.food_menu);
		parent = this.getParent();
		getData(); 
	}
	
	@SuppressWarnings("unchecked")
	private void getData(){ 
//		식단 입력 받
		Intent intent = this.getIntent();
		String uid = intent.getStringExtra("member_uid");
		HashMap<String,String>ab = new HashMap<String,String>();
		List<String> wantGap = new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		String today = String.valueOf(c.get(Calendar.YEAR)) + "-" + String.valueOf(c.get(Calendar.MONTH)+1) + "-" +String.valueOf(c.get(Calendar.DATE));
		ab.put("table","diet");
		ab.put("field", "breakfast,lunch,dinner");
		ab.put("condition", "member_uid=\""+uid+"\""+"&&date=\""+today+"\"");
		wantGap.add("breakfast");
		wantGap.add("lunch");
		wantGap.add("dinner");
		AsyncUseJson a = new AsyncUseJson(this,ReadData.Select,wantGap);
		a.execute(ab);
		initTab();
		initId();
	}
	@Override
	public void setData(List<HashMap<String, String>> HashList) {
		ArrayAdapter<String> a = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		String[] ap1 = HashList.get(0).get("breakfast").split("/");
		for(String i : ap1){
			a.add(i);
		}
		foodtext[0].setAdapter(a);
		ArrayAdapter<String> a1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		String[] ap2 = HashList.get(0).get("lunch").split("/");
		for(String i : ap2){
			a1.add(i);
		}
		foodtext[1].setAdapter(a1);
		ArrayAdapter<String> a2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		String[] ap3 = HashList.get(0).get("dinner").split("/");
		for(String i : ap3){
			a2.add(i);
		}
		foodtext[2].setAdapter(a2);
	}
	
	private void initId() {
		foodtext[0] = (ListView)findViewById(R.id.food_list_breakfast);
		foodtext[1] = (ListView)findViewById(R.id.food_list_lunch);
		foodtext[2] = (ListView)findViewById(R.id.food_list_dinner);
	}

	@SuppressWarnings("deprecation")
	private void initTab() {
		tabs = (TabHost) findViewById(android.R.id.tabhost);
		
		TabHost.TabSpec ts;
		tabs.setup();
		
		ts = tabs.newTabSpec("one");
		ts.setContent(R.id.tab_breakfast);
		ts.setIndicator("아침");
		tabs.addTab(ts);
		
		ts = tabs.newTabSpec("two");
		ts.setContent(R.id.tab_lunch);
		ts.setIndicator("점심");
		tabs.addTab(ts);
		
		ts = tabs.newTabSpec("three");
		ts.setContent(R.id.tab_dinner);
		ts.setIndicator("저녁");
		tabs.addTab(ts);
		
		tabs.setCurrentTab(0);
		setTabColor(tabs);
		tabs.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				setTabColor(tabs);
			}
		});
		
		mGesture = new GestureDetector(new OnGestureListener(){

			@Override
			public boolean onDown(MotionEvent e) {
				return false;
				}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {return false;}

			@Override
			public void onLongPress(MotionEvent e) {}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				
				disx = e2.getX()-e1.getX();
				disy = e2.getY()-e1.getY();
				if(disx > 0 && abs(disx) > abs(disy) && disx > 50 && e1.getX() > 100){
					h1 = System.currentTimeMillis();
					if(h1 - h > 300){
						h = h1;
						tabflow(0);
					}
					return true;
				}
				if(disx < 0 && abs(disx) > abs(disy) && abs(disx) > 50){
					h1 = System.currentTimeMillis();
					if(h1 - h > 300){
						h = h1;
						tabflow(1);
					}
					return true;
				}
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {}

			@Override
			public boolean onSingleTapUp(MotionEvent e) {return false;}
			
		});
	}

    public float abs(float a){
    	if(a < 0) return (float) (a * (-1.0));
    	return a;
    }
	public void tabflow(int flow){
		int now;
		now = tabs.getCurrentTab();
		if(flow == 0){ //Right
			if(now != 2){
				tabs.setCurrentTab(now+1);
				setTabColor(tabs);
			}
		}
		else{
			if(now != 0){
				tabs.setCurrentTab(now-1);
				setTabColor(tabs);
			}
		}
	}
    public boolean onTouchEvent(MotionEvent event) {
    	mGesture.onTouchEvent(event);
    	parent.onTouchEvent(event);
        super.onTouchEvent(event);  // touch이벤트 발생
        return true;
    }

	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return this.getParent().onKeyDown(keyCode, event);
		}
		return false;
	}
	public static void setTabColor(TabHost tabhost) {
	    for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
	    {
	        tabhost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#FF333333")); //unselected
	    }
	    tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(Color.parseColor("#FF888889")); // selected
	}

	@Override
	public void NullData() {
		// TODO Auto-generated method stub
		
	}
}
