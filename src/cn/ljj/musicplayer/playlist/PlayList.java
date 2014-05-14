package cn.ljj.musicplayer.playlist;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import cn.ljj.musicplayer.data.MusicInfo;

public class PlayList {
	public static final int SEQ_CIRCLE = 0;
	public static final int SEQ_RAMDON = 1;
	public static final int SEQ_SINGLE = 2;
	private static PlayList mInstance = new PlayList();
	private List<MusicInfo> mMusicList = new ArrayList<MusicInfo>();
	private int mCurrentIndex = -1;
	private int mPlaySequence = SEQ_CIRCLE;

	private PlayList() {

	}

	public static PlayList getPlayList() {
		return mInstance;
	}

	public MusicInfo get(int index) {
		if (mMusicList.size() == 0) {
			return null;
		}
		if (index >= 0 && index < mMusicList.size()) {
			return mMusicList.get(index);
		}
		return null;
	}

	public MusicInfo get() {
		if (mMusicList.size() == 0) {
			return null;
		}
		return get(mCurrentIndex);
	}

	private int getNextIndex() {
		switch (mPlaySequence) {
		case SEQ_CIRCLE:
			mCurrentIndex++;
			if (mCurrentIndex >= mMusicList.size()) {
				mCurrentIndex = 0;
			}
			return mCurrentIndex;
		case SEQ_RAMDON:
			return getRamdonIndex();
		case SEQ_SINGLE:
			break;
		}
		return mCurrentIndex;
	}

	private int getPrevIndex() {
		switch (mPlaySequence) {
		case SEQ_CIRCLE:
			mCurrentIndex--;
			if (mCurrentIndex < 0) {
				mCurrentIndex = mMusicList.size() - 1;
			}
			return mCurrentIndex;
		case SEQ_RAMDON:
			return getRamdonIndex();
		case SEQ_SINGLE:
			break;
		}
		return mCurrentIndex;
	}

	private int getRamdonIndex() {
		Random random = new Random();
		int next = Math.abs(random.nextInt());
		mCurrentIndex = next % mMusicList.size();
		return mCurrentIndex;
	}

	public MusicInfo getNext() {
		return get(getNextIndex());
	}

	public MusicInfo getPrev() {
		return get(getPrevIndex());
	}

	public List<MusicInfo> getMusicList() {
		return mMusicList;
	}

	public void setMusicList(List<MusicInfo> list) {
		mMusicList.clear();
		mMusicList.addAll(list);
		mCurrentIndex = 0;
		sortList();
	}

	public void add(MusicInfo music) {
		mMusicList.add(music);
	}

	public void remove(int index) {
		mMusicList.remove(index);
		if (index == mCurrentIndex) {
			mCurrentIndex = 0;
		} else if (index < mCurrentIndex) {
			mCurrentIndex--;
		}
	}

	public void removeAll() {
		mMusicList.clear();
		mCurrentIndex = -1;
	}

	public int getCurrentIndex() {
		return mCurrentIndex;
	}

	public void setCurrentIndex(int index) {
		if (index >= 0 && index < mMusicList.size()) {
			mCurrentIndex = index;
		}
	}

	public boolean isEmpty() {
		return mMusicList.isEmpty();
	}

	private void sortList() {
		Collections.sort(mMusicList, comparator);
	}

	private Comparator<MusicInfo> comparator = new Comparator<MusicInfo>() {
		public int compare(MusicInfo m1, MusicInfo m2) {
			Collator collator = Collator.getInstance(java.util.Locale.CHINA);
			if (collator.compare(m1.getName(), m2.getName()) < 0)
				return -1;
			else if (collator.compare(m1.getName(), m2.getName()) > 0)
				return 1;
			else
				return 0;
		}
	};


}
