package cn.ljj.musicplayer.playlist;

public class SavedList {
	String mListName = null;
	int mCount = 0;
	int mListId = -1;

	public String getListName() {
		return mListName;
	}

	public void setListName(String listName) {
		mListName = listName;
	}

	public int getCount() {
		return mCount;
	}

	public void setCount(int count) {
		mCount = count;
	}

	public int getListId() {
		return mListId;
	}

	public void setListId(int listId) {
		mListId = listId;
	}

}
