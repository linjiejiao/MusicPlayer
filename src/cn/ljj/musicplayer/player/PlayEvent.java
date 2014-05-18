package cn.ljj.musicplayer.player;

import cn.ljj.musicplayer.data.MusicInfo;

public class PlayEvent {

	public static final int EVENT_PLAY = 0;
	public static final int EVENT_STOP = 1;
//	public static final int EVENT_NEXT = 2;
//	public static final int EVENT_PREV = 3;
	public static final int EVENT_SEEK = 4;
	public static final int EVENT_RELOADLIST = 5;

	private int mEventCode = -1;
	private int mIntValue = 0;
	private String mStringValue = null;
	private MusicInfo mMusic = null;
	public int getEventCode() {
		return mEventCode;
	}

	public void setEventCode(int eventCode) {
		mEventCode = eventCode;
	}

	public int getIntValue() {
		return mIntValue;
	}

	public void setIntValue(int intValue) {
		mIntValue = intValue;
	}

	public String getStringValue() {
		return mStringValue;
	}

	public void setStringValue(String stringValue) {
		mStringValue = stringValue;
	}

	public MusicInfo getMusic() {
		return mMusic;
	}

	public void setMusic(MusicInfo music) {
		mMusic = music;
	}

}
