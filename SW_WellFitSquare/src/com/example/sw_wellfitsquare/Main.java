package com.example.sw_wellfitsquare;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.R;
import com.example.exercise.Exercise;
import com.example.nonpt.GeneralCurriculum;
import com.example.nonpt.ToolsInstructions;
import com.example.nonpt.TrainerView;
import com.example.nonpt.VitalInfo;
import com.example.pts.FoodMenu;
import com.example.pts.PtTime;
import com.example.pts.mission.Mission;
import com.example.sns.ActivityBoardContent;
import com.example.sns.Evaluate;


@SuppressWarnings("deprecation")
public class Main extends TabActivity implements ReadData{

	public int now = 0; // 0: off / 1: on
	int nowMove = 0;
	Animation ltrans_l,mtrans_l;
	Animation ltrans_r,mtrans_r;
	LinearLayout leftlayout;
	LinearLayout mainlayout;
	Button menu_button;
	Button main_name;
	Button main_setting;
	TextView main_title;
	ImageView memberImage;
	GestureDetector mGesture;
	float disx,disy;
	String member_uid = null;
	String trainer_uid = null;
	int pt = 0; // false : not pt / true :pt
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main); 
		Intent intent = this.getIntent();
		member_uid = intent.getStringExtra("member_uid");
		// TouchEvent때문에 미리 설정해야함.
		leftlayout = (LinearLayout)findViewById(R.id.MenuLayout);
        mainlayout = (LinearLayout)findViewById(R.id.MainLayout);
        menu_button = (Button)findViewById(R.id.menu_button);
        main_name = (Button)findViewById(R.id.main_name); 
        main_title = (TextView)findViewById(R.id.main_title);
        main_setting = (Button)findViewById(R.id.main_setting);
        memberImage = (ImageView)findViewById(R.id.Image_rock);
        
		setAnimation();
		setButton();
		setClickOpen();
		setClickClose();
		(new GetMemberImage()).execute();
		
		// Data 가져오기 (이름,pt인지,trainer_uid);
		
		HashMap<String,String>ab = new HashMap<String,String>();
		List<String> wantGap = new ArrayList<String>();
		
		//pt인지 받아야됨
		ab.put("table","member");
		ab.put("field", "trainer_uid,name");
		ab.put("condition", "uid=\""+member_uid+"\"");
		wantGap.add("trainer_uid");
		wantGap.add("name");
		
		AsyncUseJson a = new AsyncUseJson(this,ReadData.Select,wantGap);
		a.execute(ab);
	}
    public boolean onTouchEvent(MotionEvent event) {
    	mGesture.onTouchEvent(event);
        super.onTouchEvent(event);  // touch이벤트 발생
        return true;
    }
    public float abs(float a){
    	if(a < 0) return (float) (a * (-1.0));
    	return a;
    }
    public class GetMemberImage extends AsyncTask<Void, Void, Boolean>{
    	public Bitmap bm;
        public int dpToPx(int dp) {
            DisplayMetrics displayMetrics = getBaseContext().getResources().getDisplayMetrics();
            int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
            return px;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                URL url = new URL("http://61.43.139.69/member_image/" + member_uid + ".jpg");
                bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return true;
            }catch (IOException ex){
                    return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            int x = dpToPx(120);
            if(result == true) memberImage.setImageBitmap(Bitmap.createScaledBitmap(bm, x, x, true));
            else memberImage.setImageResource(R.drawable.rock2);
        }
    }
    public void moveMenu(){
		if(now == 0){
			leftlayout.setVisibility(View.VISIBLE);
			leftlayout.startAnimation(ltrans_r);
			mainlayout.startAnimation(mtrans_r);
		}
		else{
			leftlayout.startAnimation(ltrans_l);
			mainlayout.startAnimation(mtrans_l);
		}
    }
	private void setClickOpen() {
		menu_button.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				moveMenu();
            }
		});
	    mGesture = new GestureDetector(new OnGestureListener() {
			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}
			public void onShowPress(MotionEvent e) {
			}
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
					float distanceY) {
				disx = e2.getX()-e1.getX();
				disy = e2.getY()-e1.getY();
				if(disx > 0 && abs(disx) > abs(disy) && disx > 50 && e1.getX() < 100){
					if(now == 0 &&nowMove == 0){
						nowMove = 1;
						moveMenu();
						return true;
					}
				}
				if(disx < 0 && abs(disx) > abs(disy) && abs(disx) > 50){
					if(now == 1&&nowMove == 0){
						nowMove = 1;
						moveMenu();
						return true;
					}
				}
				return false;
			}
			public void onLongPress(MotionEvent e) {
			}
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				return false;
			}
			public boolean onDown(MotionEvent e) {
				return false;
			}
		});
	}
	private void setClickClose() {
		TextView blank = (TextView)findViewById(R.id.blank);
		blank.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(now == 1){
					moveMenu();
				}
			}
		});
		blank.setOnTouchListener(new OnTouchListener(){
			public boolean onTouch(View v, MotionEvent event) {
				onTouchEvent(event);
				return false;
			}
			
		});
	}
	public void setAnimation(){
		// animation
		ltrans_l = AnimationUtils.loadAnimation(this, R.animator.lt_l);
        ltrans_r = AnimationUtils.loadAnimation(this, R.animator.lt_r);
        
        mtrans_l = AnimationUtils.loadAnimation(this, R.animator.mt_l);
        mtrans_r = AnimationUtils.loadAnimation(this, R.animator.mt_r);

        AnimationListener animListener = new AnimationListener(){
        	public void onAnimationEnd(Animation animation) {
				if(now == 1){
					leftlayout.setVisibility(View.INVISIBLE);
					now = 0;
				}
				else{
					now = 1;
				}
				nowMove = 0;
			}
			public void onAnimationRepeat(Animation animation) {}
			public void onAnimationStart(Animation animation) {}
        };
        ltrans_l.setAnimationListener(animListener);
        ltrans_r.setAnimationListener(animListener);
        
	}
	public void setTab(){
		TabHost tabhost = getTabHost();
		TabHost.TabSpec spec = null;
		Intent intent = null;
		
		//01
		spec = tabhost.newTabSpec("tab01");
 	    intent = new Intent(this, GeneralCurriculum.class);
	   	intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	   	intent.putExtra("member_uid", member_uid);
	   	intent.putExtra("trainer_uid", trainer_uid);
 	    spec.setContent(intent);
 	    spec.setIndicator("Menu01");
 	    tabhost.addTab(spec);

 	    //02 
 	    spec = tabhost.newTabSpec("tab02");
 	    intent = new Intent(this, ToolsInstructions.class);
	   	intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	   	intent.putExtra("member_uid", member_uid);
	   	intent.putExtra("trainer_uid", trainer_uid);
 	    spec.setContent(intent);
 	    spec.setIndicator("Menu02");
 	    tabhost.addTab(spec);
 	    
 	    //03 
 	    spec = tabhost.newTabSpec("tab03");
 	    intent = new Intent(this, TrainerView.class);
	   	intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	   	intent.putExtra("member_uid", member_uid);
	   	intent.putExtra("trainer_uid", trainer_uid);
 	    spec.setContent(intent);
 	    spec.setIndicator("Menu03");
 	    tabhost.addTab(spec);
 	    
 	    //04
 	    spec = tabhost.newTabSpec("tab04");
 	    intent = new Intent(this, VitalInfo.class);
	   	intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	   	intent.putExtra("member_uid", member_uid);
	   	intent.putExtra("trainer_uid", trainer_uid);
 	    spec.setContent(intent);
 	    spec.setIndicator("Menu04");
 	    tabhost.addTab(spec);
 	    
 	    //05
 	    spec = tabhost.newTabSpec("tab05");
 	    intent = new Intent(this, FoodMenu.class);
	   	intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	   	intent.putExtra("member_uid", member_uid);
	   	intent.putExtra("trainer_uid", trainer_uid);
 	    spec.setContent(intent);
 	    spec.setIndicator("Menu05");
 	    tabhost.addTab(spec);

 	    //06
 	    spec = tabhost.newTabSpec("tab06");
 	    intent = new Intent(this, Mission.class);
	   	intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	   	intent.putExtra("member_uid", member_uid);
	   	intent.putExtra("trainer_uid", trainer_uid);
 	    spec.setContent(intent);
 	    spec.setIndicator("Menu06");
 	    tabhost.addTab(spec);
 	    
 	    //07
 	    spec = tabhost.newTabSpec("tab07");
 	    intent = new Intent(this, PtTime.class);
	   	intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	   	intent.putExtra("member_uid", member_uid);
	   	intent.putExtra("trainer_uid", trainer_uid);
 	    spec.setContent(intent);
 	    spec.setIndicator("Menu07");
 	    tabhost.addTab(spec);
 	    
 	    //08
 	    spec = tabhost.newTabSpec("tab08");
 	    intent = new Intent(this, ActivityBoardContent.class);
	   	intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	   	intent.putExtra("member_uid", member_uid);
	   	intent.putExtra("trainer_uid", trainer_uid);
 	    spec.setContent(intent);
 	    spec.setIndicator("Menu08");
 	    tabhost.addTab(spec);
 	    
 	    //09
 	    spec = tabhost.newTabSpec("tab09");
 	    intent = new Intent(this, Exercise.class);
	   	intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	   	intent.putExtra("member_uid", member_uid);
	   	intent.putExtra("trainer_uid", trainer_uid);
 	    spec.setContent(intent);
 	    spec.setIndicator("Menu09");
 	    tabhost.addTab(spec);
 	    
 	    //
 	    tabhost.setCurrentTab(0);
	}
	public void setButton(){
		main_setting.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(),Evaluate.class);
				intent.putExtra("member_uid", member_uid);
				startActivity(intent);
			}
		});
		
		Button[] b = new Button[10];
		int i;
		b[0] = (Button)findViewById(R.id.bt1);
		b[1] = (Button)findViewById(R.id.bt2);
		b[2] = (Button)findViewById(R.id.bt3);
		b[3] = (Button)findViewById(R.id.bt4);
		b[4] = (Button)findViewById(R.id.bt5);
		b[5] = (Button)findViewById(R.id.bt6);
		b[6] = (Button)findViewById(R.id.bt7);
		b[7] = (Button)findViewById(R.id.bt8);
		b[8] = (Button)findViewById(R.id.bt9);
		for(i=0;i<9;i++){
			b[i].setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					int a = Integer.parseInt(v.getTag().toString());
					Button c = (Button)v;
					main_title.setText(c.getText());
					TabHost tabhost = getTabHost();
					tabhost.setCurrentTab(a);
					moveMenu();
				}
			});
		}
		main_title.setText(b[0].getText());
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(now == 0){
				moveMenu();
				return true;
			}
			else{
//				moveMenu();
				finish();
				return false;
			}
		}
		return false;
	}
	@Override
	public void setData(List<HashMap<String, String>> HashList) {
		// 데이터를 다 가져오고 불러옴.
		trainer_uid = HashList.get(0).get("trainer_uid");
		main_name.setText(HashList.get(0).get("name"));
		setTab();
	}
	@Override
	public void NullData() {
		// TODO Auto-generated method stub
		
	}
}
