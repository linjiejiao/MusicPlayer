package cn.ljj.musicplayer.ui.lrc;

public class LyricLine {
	private int time = 0;
	private String lyric = null;

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getLyric() {
		return lyric;
	}

	public void setLyric(String lyric) {
		this.lyric = lyric;
	}

	@Override
	public String toString() {
		return "LyricLine [time=" + time + ", lyric=" + lyric + "]";
	}

}