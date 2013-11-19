package com.example.sw_wellfitsquare;

import java.util.HashMap;
import java.util.List;

public interface ReadData {
	static int Insert = 1;
	static int Select = 2;
	static int Update = 3;
	static int Delete = 4;
	void setData(List<HashMap<String,String>> HashList);
	void NullData();
}
