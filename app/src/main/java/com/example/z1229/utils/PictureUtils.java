package com.example.z1229.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PictureUtils {
	
	public static String byte2file(byte[] data,String fileurl) {
		try {
			FileOutputStream fos = new FileOutputStream(new File(fileurl));
			fos.write(data);
			fos.close();
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	/**
	 * Try to return the absolute file path from the given Uri
	 *
	 * @param context
	 * @param uri
	 * @return the file path or null
	 */
	//uri.toString()得到的地址不正确，用次方法得到正确的地址
	public static String getRealFilePath(final Context context, final Uri uri ) {
		if ( null == uri ) return null;
		final String scheme = uri.getScheme();
		String data = null;
		if ( scheme == null )
			data = uri.getPath();
		else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
			data = uri.getPath();
		} else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
			Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
			if ( null != cursor ) {
				if ( cursor.moveToFirst() ) {
					int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
					if ( index > -1 ) {
						data = cursor.getString( index );
					}
				}
				cursor.close();
			}
		}
		return data;
	}
}
