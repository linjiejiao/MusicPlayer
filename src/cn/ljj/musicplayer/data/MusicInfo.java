package cn.ljj.musicplayer.data;

public class MusicInfo {
	private String name = null;
	private String path = null;
	private String durationStr = null;
	private int duration = 0;

	public MusicInfo(String name, String path, int duration) {
		this.name = name;
		this.path = path;
		this.duration = duration;
		durationStr = getDispTime(duration);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDurationStr() {
		return durationStr;
	}

	public void setDurationStr(String durationStr) {
		this.durationStr = durationStr;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getDuration() {
		return duration;
	}

	public static String getDispTime(int mSec) {
		int duration = mSec / 1000;
		int min = duration / 60;
		int sec = duration % 60;
		return new String(min + ":" + sec / 10 + sec % 10);
	}

	@Override
	public String toString() {
		return "MusicInfo [name=" + name + ", path=" + path + ", durationStr="
				+ durationStr + ", duration=" + duration + "]";
	}
	
}
