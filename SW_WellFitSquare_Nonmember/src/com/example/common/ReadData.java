package com.example.common;

import java.util.HashMap;
import java.util.List;

public interface ReadData {
	static int Insert = 1;
	static int Select = 2;
	static int Update = 3;
	void setData(List<HashMap<String,String>> HashList);
	void NullData();
	void NullData(int requestCode);
	void setData(List<HashMap<String, String>> hashList, int requestCode);
}
