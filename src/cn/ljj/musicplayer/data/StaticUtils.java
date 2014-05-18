package cn.ljj.musicplayer.data;

public class StaticUtils {
	public static String getDispTime(int mSec) {
		int duration = mSec / 1000;
		int min = duration / 60;
		int sec = duration % 60;
		return new String(min + ":" + sec / 10 + sec % 10);
	}
}
