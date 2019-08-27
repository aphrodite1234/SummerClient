package com.example.z1229.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PictureUtils {
	
	public static String byte2file(byte[] data,String fileurl) {
		try {
			FileOutputStream fos = new FileOutputStream(new File(fileurl));
			fos.write(data);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileurl;
	}
	
	public static byte[] file2byte(String fileurl) {
		byte[] data = null;
		try {
			FileInputStream fis = new FileInputStream(fileurl);
			data = new byte[fis.available()];
			fis.read(data);
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
}
