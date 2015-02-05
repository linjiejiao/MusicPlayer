
package cn.ljj.musicplayer.playlist;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import android.content.Context;
import android.database.Cursor;
import cn.ljj.musicplayer.data.MusicInfo;
import cn.ljj.musicplayer.database.Logger;
import cn.ljj.musicplayer.database.MusicPlayerDatabase;
import cn.ljj.musicplayer.database.MusicProvider;

public class PlayList {
    public static final String TAG = "PlayList";
    public static final int SEQ_CIRCLE = 0;
    public static final int SEQ_RAMDON = 1;
    public static final int SEQ_SINGLE = 2;
    private static PlayList mInstance = null;
    private List<MusicInfo> mMusicList = new ArrayList<MusicInfo>();
    private int mCurrentIndex = -1;
    private int mPlaySequence = SEQ_CIRCLE;
    private int mProgress = -1;
    private Context mContext = null;
    private PlayListPersister mPersister = null;
    private long mListId = -1;

    private PlayList(Context context) {
        mContext = context;
        mPersister = new PlayListPersister(mContext);
    }

    public static PlayList getPlayList(Context context) {
        if (mInstance == null) {
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

    public long getListId() {
        return mListId;
    }

    public void setListId(long id) {
        mListId = id;
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

    public void setMusicList(List<MusicInfo> list, long listId) {
        mMusicList.clear();
        mMusicList.addAll(list);
        mCurrentIndex = 0;
        mListId = listId;
    }

    public void add(MusicInfo music) {
        mMusicList.add(music);
        music.setListId(mListId);
        mPersister.persistMusic(music, mListId);
    }

    public void remove(int index) {
        Logger.e(TAG, "remove from mListId=" + mListId + " index=" + index);
        if (mListId != -1) {
            mPersister.deleteMusic(index);
        }
    }

    public void removeAll() {
        Logger.e(TAG, "removeAll");
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

    public int loadFromMediaStore() {
        setMusicList(mPersister.loadFromMediaStore(), -1);
        sortList();
        return mMusicList.size();
    }

    public int loadFromCursor(Cursor cursor, long listId) {
        if (listId == -1 && cursor.moveToFirst()) {
            listId = cursor.getLong(MusicPlayerDatabase.INDEX_LIST_ID);
        }
        setMusicList(mPersister.getMusics(cursor), listId);
        Logger.e(TAG, "loadFromCursor listId =" + listId);
        return mMusicList.size();
    }

    public long persist(String listName, boolean cover) {
        return mPersister.persist(mMusicList, listName, cover);
    }

    public int deletePlayList(int listId) {
        return mPersister.deletePlayList(listId);
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        mProgress = progress;
    }

    public long update(MusicInfo music) {
        return mPersister.updateMusic(music);
    }
}
