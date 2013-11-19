package com.example.nonpt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.R;
import com.example.sw_wellfitsquare.AsyncUseJson;
import com.example.sw_wellfitsquare.ReadData;

public class TrainerView extends Activity implements OnItemClickListener,
		ReadData {
	Button trainnerview_btn_call;

	List<HashMap<String, String>> TrainnerHashList;

	ArrayList<Member> arMemberList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trainerview);

		TrainnerHashList = new ArrayList<HashMap<String, String>>();

		HashMap<String, String> ab = new HashMap<String, String>();

		List<String> wantGap = new ArrayList<String>();
		ab.put("table", "trainer");
		ab.put("field", "name,phone,intro");
		ab.put("condition", "uid>0");
		wantGap.add("name");
		wantGap.add("phone");
		wantGap.add("intro");

		AsyncUseJson a = new AsyncUseJson(this, ReadData.Select, wantGap);
		a.execute(ab);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void setData(List<HashMap<String, String>> HashList) {
		if (HashList != null) {
			String s = HashList.toString();

			TrainnerHashList = HashList;

			arMemberList = new ArrayList<Member>();

			int temp_img[] = { R.drawable.trainner_girl, R.drawable.nicenet,
					R.drawable.df3, R.drawable.ic_launcher,
					R.drawable.ic_launcher, R.drawable.ic_launcher };

			for (int i = 0; i < TrainnerHashList.size(); i++) {
				arMemberList.add(new Member(temp_img[i], TrainnerHashList
						.get(i).get("name"), TrainnerHashList.get(i).get(
						"phone")));
			}
			MemberAdapter myMemberAdapter = new MemberAdapter(this,
					arMemberList);
			ListView paperlistview = (ListView) findViewById(R.id.tranner_list);
			paperlistview.setAdapter(myMemberAdapter);

			paperlistview.setOnItemClickListener(this);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return this.getParent().onKeyDown(keyCode, event);
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}

	class Member {
		int image;
		String tools_name;
		String sports_name;
		String tools_way;
		String sports_content;
		String sports_data;

		String name;
		String phone;

		public Member(int _image, String _name) {
			tools_name = _name;
			image = _image;
		}

		public Member(int _image, String tools_name, String sports_name,
				String tools_way, String sports_content, String sports_data) {
			this.image = _image;
			this.tools_name = tools_name;
			this.sports_name = sports_name;
			this.tools_way = tools_way;
			this.sports_content = sports_content;
			this.sports_data = sports_data;
		}

		public Member(int _image, String _name, String _phone) {
			name = _name;
			image = _image;
			phone = _phone;
		}

	}

	int flag;

	class MemberAdapter extends BaseAdapter {

		LayoutInflater mInflater;
		ArrayList<Member> arSrc;
		Context _context;

		public MemberAdapter(Context context, ArrayList<Member> personListItem) {
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

		public View getView(final int position, View convertView,
				ViewGroup parent) {

			int res = R.layout.trainerview_list;
			convertView = mInflater.inflate(res, parent, false);

			LinearLayout trainer_list_background = (LinearLayout) convertView
					.findViewById(R.id.trainer_list_background);
			trainer_list_background
					.setBackgroundResource(arSrc.get(position).image);

			TextView trainer_list_info = (TextView) convertView
					.findViewById(R.id.trainer_list_info);
			TextView trainer_list_self = (TextView) convertView
					.findViewById(R.id.trainer_list_self);
			trainer_list_self.setText(arSrc.get(position).name);

			Button trainer_list_btn_call = (Button) convertView
					.findViewById(R.id.trainer_list_btn_call);
			trainer_list_btn_call.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri
							.parse("tel:0" + arSrc.get(position).phone));
					startActivity(intent);
				}
			});

			return convertView;
		}
	}

	@Override
	public void NullData() {
		// TODO Auto-generated method stub
		
	}

}
