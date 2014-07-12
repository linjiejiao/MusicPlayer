package cn.ljj.musicplayer.playlist;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import android.content.Context;
import cn.ljj.musicplayer.data.MusicInfo;
import cn.ljj.musicplayer.database.Logger;

public class PlayList implements Observer {
	public static final int SEQ_CIRCLE = 0;
	public static final int SEQ_RAMDON = 1;
	public static final int SEQ_SINGLE = 2;
	private static PlayList mInstance = null;
	private List<MusicInfo> mMusicList = new ArrayList<MusicInfo>();
	private int mCurrentIndex = -1;
	private int mPlaySequence = SEQ_CIRCLE;
	private int mProgress = -1;
	private Context mContext = null;
	PlayListPersister mPersister = null;
	private String mPlayListName = null;
	private String TAG = "PlayList";

	private PlayList(Context context) {
		mContext = context;
		mPersister = new PlayListPersister(mContext);
	}

	public static PlayList getPlayList(Context context) {
		if(mInstance == null){
			mInstance = new PlayList(context);
		}
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

	public void setMusicList(List<MusicInfo> list ,String listNme) {
		mMusicList.clear();
		for(MusicInfo music:list){
			music.addObserver(this);
		}
		mMusicList.addAll(list);
		mCurrentIndex = 0;
		mPlayListName = listNme;
		if(listNme != null){
			sortList();
		}
	}

	public void add(MusicInfo music) {
		Logger.e(TAG , "add music="+music);
		mMusicList.add(music);
		music.addObserver(this);
		if(mPlayListName != null){
			mPersister.persist(music, mPlayListName);
		}
	}

	public void remove(int index) {
		Logger.e(TAG , "remove from " + mPlayListName + " index="+index);
		if(mPlayListName != null){
			long deleteId = get(index).getId();
			long listId = get(index).getListId();
			mPersister.removeMusic(deleteId,listId);
		}
		mMusicList.remove(index);
		if (index == mCurrentIndex) {
			mCurrentIndex = 0;
		} else if (index < mCurrentIndex) {
			mCurrentIndex--;
		}
	}

	public void removeAll() {
		Logger.e(TAG , "removeAll");
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

	public int load(String listName){
		mPlayListName = listName;
		setMusicList(mPersister.load(listName), listName);
		return mMusicList.size();
	}

	public int loadFromMediaStore(){
		setMusicList(mPersister.loadFromMediaStore(), null);
		return mMusicList.size();
	}
	
	public List<SavedList> getAllSavedPlayList() {
		return mPersister.getAllSavedPlayList();
	}

	public int persist(String listName, boolean cover){
		return mPersister.persist(mMusicList, listName, cover);
	}

	public int deletePlayList(String listName){
		Logger.e(TAG , "deletePlayList listName="+listName);
		return mPersister.deletePlayList(listName);
	}

	public int getProgress() {
		return mProgress;
	}

	public void setProgress(int progress) {
		mProgress = progress;
	}

	@Override
	public void update(Observable observable, Object data) {
		if(data != null){
			MusicInfo music = (MusicInfo)data;
			long changeId = music.getId();
			if(changeId != -1){
				mPersister.update(music);
			}
		}
	}
}
