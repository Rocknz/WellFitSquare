package com.example.exercise;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.R;
import com.example.sw_wellfitsquare.AsyncUseJson;
import com.example.sw_wellfitsquare.AsyncUseJson_nonmember;
import com.example.sw_wellfitsquare.ReadData;


public class NFC_MainActivity extends Activity implements ReadData{

    public int seconds;
    public static int prev_seconds;

    private TextView timer_view;
    private TextView measure_tool_name;
    private Button save_button;
    private Button play_button;
    private Button stop_button;
    public String member_uid;
    private Timer timer;
    public String kind;
    public int flag = 0;
    int before_time = -1;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";

    private NfcAdapter mNfcAdapter;
    public static boolean mIsTag = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_main);
        Intent intent = getIntent();
        member_uid = intent.getStringExtra("member_uid");
        
        measure_tool_name = (TextView)findViewById(R.id.measure_tool_name);
        timer_view = (TextView)findViewById(R.id.measure_time_view);
        
//        save_button = (Button)findViewById(R.id.measure_save);
//        save_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                timer.cancel();
//                SelectSaveDialogFragment select = new SelectSaveDialogFragment().newInstance();
//                select.show(getFragmentManager(), "saveFragment");
//            }
//        });

//        play_button = (Button)findViewById(R.id.measure_play_btn);
//        play_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                timer.schedule(new TimerShow(), 0, 1000);
//            }
//        });
//
//        stop_button = (Button)findViewById(R.id.measure_stop_btn);
//        stop_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                timer.cancel();
//            }
//        });
        settingNFC();
    }
    public class TimerShow extends TimerTask{

        @Override
        public void run() {
            seconds++;            
            runOnUiThread(new Runnable() 
            {
	           public void run() 
	           {
	            	timer_view.setText(makeTime(seconds));
	           }
            });
        }
    }
    public void settingNFC(){
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        handleIntent(getIntent());
    }
    
    public static class SelectSaveDialogFragment extends DialogFragment {

        public static SelectSaveDialogFragment newInstance(){
            SelectSaveDialogFragment frag = new SelectSaveDialogFragment();
            Bundle args = new Bundle();
            frag.setArguments(args);

            return frag;
        }

//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState){
//            return new AlertDialog.Builder(getActivity()).setTitle("test").setMessage("qwer")
//                    .setCancelable(false)
//                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            (new SaveNFCTask()).execute();
//                        }
//                    })
//                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dismiss();
//                        }
//                    })
//                    .create();
//        }
    }
    public String makeTime(int var){
    	String alpha = new String();
    	if(var/3600 < 10){ alpha = "0"+ String.valueOf(var/3600);}
    	else {alpha = String.valueOf(var/3600);}
    	var %= 3600;
    	alpha+=":";
    	if(var/60 < 10){ alpha += "0"+ String.valueOf(var/60);}
    	else {alpha += String.valueOf(var/60);}
    	var %= 60;
    	alpha+=":";
    	if(var < 10){ alpha += "0"+ String.valueOf(var);}
    	else {alpha += String.valueOf(var);
    	}
    	return alpha;
    }
    @Override
    protected void onResume() {
        super.onResume();

        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
            byte[] payload = record.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageCodeLength = payload[0] & 0063;
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
            	if(!mIsTag){
                    Calendar now = Calendar.getInstance();
                    int now_time = now.get(Calendar.HOUR_OF_DAY)*3600+now.get(Calendar.MINUTE)*60+now.get(Calendar.SECOND);
                    if(before_time + 5 < now_time){
                    	before_time = now_time;
                        mIsTag = (!mIsTag) ? true : false;
                        measure_tool_name.setText(result + " 운동중");
                        kind = result;
                        Toast.makeText(getBaseContext(), result+" 운동이 시작되었습니다.", Toast.LENGTH_SHORT).show();
                        timer = new Timer();
                        timer.schedule(new TimerShow(), 0, 1000);
                        getData();
                    }
            	}else{
                    Calendar now = Calendar.getInstance();
                    int now_time = now.get(Calendar.HOUR_OF_DAY)*3600+now.get(Calendar.MINUTE)*60+now.get(Calendar.SECOND);
                    if(before_time + 5 < now_time){
                    	mIsTag = (!mIsTag) ? true : false;
                        Toast.makeText(getBaseContext(), "운동이 끝났습니다.", Toast.LENGTH_SHORT).show();
                        Save();
                    	finish();
                    }
            	}
            }
        }
    }
    public void Save(){
    	flag = 2;
        beforetime += seconds;
        beforenum += 1;
		Calendar c = Calendar.getInstance();
		String date = String.valueOf(c.get(Calendar.YEAR))+"-"+String.valueOf(c.get(Calendar.MONTH)+1)+"-"+String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		String t_time = makeTime(beforetime);
		//저장.
		HashMap<String,String> a = new HashMap<String,String>();
		List<String> Wantgap = new ArrayList<String>();
		a.put("table", "exercise");
		a.put("field", "member_uid,date,kind,num,time");
		a.put("data", member_uid+",\""+date+"\",\""+kind+"\","+beforenum+",\""+t_time+"\"");
		a.put("condition","member_uid="+ member_uid +"&& date=\""+ date +"\" && kind=\""+ kind +"\"");
		a.put("overlap", "1");
		AsyncUseJson ab = new AsyncUseJson(this,ReadData.Update,Wantgap);
		ab.execute(a);

    }
    public void getData(){
    	flag = 1;
		Calendar c = Calendar.getInstance();
		String date = String.valueOf(c.get(Calendar.YEAR))+"-"+String.valueOf(c.get(Calendar.MONTH)+1)+"-"+String.valueOf(c.get(Calendar.DAY_OF_MONTH));
    	HashMap<String,String> a = new HashMap<String,String>();
		List<String> Wantgap = new ArrayList<String>();
		a.put("table", "exercise");
		a.put("field", "num,time");
		a.put("condition","member_uid="+ member_uid +"&& date=\""+ date +"\" && kind=\""+ kind +"\"");
		Wantgap.add("num");
		Wantgap.add("time");
		AsyncUseJson ab = new AsyncUseJson(this,ReadData.Select,Wantgap);
		ab.execute(a);
    }
    int beforenum = 0;
	int beforetime = 0;
	@Override
	
	public void setData(List<HashMap<String, String>> HashList) {
		if(flag == 1){
			beforenum = Integer.parseInt(HashList.get(0).get("num"));
			String[] ar = HashList.get(0).get("time").split(":");
			beforetime = Integer.parseInt(ar[0])*3600;
			beforetime += Integer.parseInt(ar[1])*60;
			beforetime += Integer.parseInt(ar[2]);
		}
	}

	@Override
	public void NullData() {
		if(flag == 1){
			beforenum = 0;
			beforetime = 0; 
		}
	}
}