package cn.ljj.musicplayer.data;

public class MusicInfo {
	private String name = null;
	private String artist = null;
	private String album = null;
	private String musicPath = null;
	private String lrcPath = null;
	private String picPath = null;
	private String durationStr = null;
	private int duration = 0;
	private long _id = -1;

	public MusicInfo(String name, String path) {
		this.name = name;
		this.musicPath = path;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getMusicPath() {
		return musicPath;
	}

	public void setMusicPath(String musicPath) {
		this.musicPath = musicPath;
	}

	public String getLrcPath() {
		return lrcPath;
	}

	public void setLrcPath(String lrcPath) {
		this.lrcPath = lrcPath;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDurationStr() {
		return durationStr;
	}

	public void setDurationStr(String durationStr) {
		this.durationStr = durationStr;
	}

	public void setDuration(int duration) {
		this.duration = duration;
		setDurationStr(getDispTime(duration));
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

	public long getId() {
		return _id;
	}

	public void setId(long id) {
		_id = id;
	}

	@Override
	public String toString() {
		return "MusicInfo [name=" + name + ", artist="
				+ artist + ", album=" + album + ", musicPath=" + musicPath
				+ ", lrcPath=" + lrcPath + ", picPath=" + picPath
				+ ", durationStr=" + durationStr + ", duration=" + duration
				+ ", _id=" + _id
				+ "]";
	}
	
}
