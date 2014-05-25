package cn.ljj.musicplayer.data;

import java.io.File;

import android.os.Environment;

public class StaticUtils {
	public static String getLrcPath() {
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				String lrcPath = Environment.getExternalStorageDirectory()
						.getPath() + "/MusicPlayer/Lrc/";
				File file = new File(lrcPath);
				if (!file.exists()) {
					file.mkdirs();
				}
				return lrcPath;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getMusicPath() {
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				String musicPath = Environment.getExternalStorageDirectory()
						.getPath() + "/MusicPlayer/Music/";
				File file = new File(musicPath);
				if (!file.exists()) {
					file.mkdirs();
				}
				return musicPath;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getPicPath() {
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				String picPath = Environment.getExternalStorageDirectory()
						.getPath() + "/MusicPlayer/Pic/";
				File file = new File(picPath);
				if (!file.exists()) {
					file.mkdirs();
				}
				return picPath;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getDispTime(int mSec) {
		int duration = mSec / 1000;
		int min = duration / 60;
		int sec = duration % 60;
		return new String(min + ":" + sec / 10 + sec % 10);
	}
}
