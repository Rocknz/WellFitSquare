package com.example.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class AsyncUseJson extends AsyncTask<HashMap<String,String>, Void , Void>{
	ReadData listener;
	Context mContext;
	List<HashMap<String,String>> HashList = new ArrayList<HashMap<String,String>>();
	int what;
	List<String> wantGap = new ArrayList<String>();
	
	public AsyncUseJson(Context listener,int what,List<String> wantGap){
		this.what = what;
		this.mContext = listener;
		this.listener = (ReadData)listener;
		this.wantGap = wantGap;
	}
	
	@Override
	protected Void doInBackground(HashMap<String, String>... params) {
		String C = null;
		switch(what){
		case ReadData.Insert:
			C = "insert";
			break;
		case ReadData.Select:
			C = "select";
			break;
		case ReadData.Update:
			C = "update";
			break;
		}
		
		HashMap<String, String> send = new HashMap<String,String>();
		send = params[0];
		JsonRequestPost client = new JsonRequestPost(send,"http://61.43.139.69/"+C+".php");
		client.requestPost();
		HashList = client.jsonParse(wantGap);
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if(what == ReadData.Select){
			if(HashList.get(0).get("error") != null){
				Toast.makeText(mContext, "접속이 끊겼습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
			}
			else if(HashList.get(0).get("null")!= null){ // 스트링 널 넣음 
				Toast.makeText(mContext, "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
				listener.NullData();
				
			}else{
				listener.setData(HashList);
			}
		}
		else{
			if(HashList.size()!=0)
			{
				if(HashList.get(0).get("error") != null){
					Toast.makeText(mContext, "접속이 끊겼습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
				}
				else{
					listener.setData(HashList);
				}
			}
			else{
//				HashMap<String,String> at = new HashMap<String,String>();
//				at.put("alpha", "beta");
//				HashList.add(at);
				listener.setData(HashList);
			}
		}
	}
}
