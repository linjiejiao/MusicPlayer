package cn.ljj.musicplayer.data;

import cn.ljj.musicplayer.database.MusicPlayerDatabase;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class MusicInfo implements Parcelable{
	public static final int LRCPATH_CHANGED = 10;
	public static final int PICPATH_CHANGED = 11;

	private long _id = -1;
	private long mListId = -1;
	private String mName = null;
	private String mArtist = null;
	private String mAlbum = null;
	private String mMusicPath = null;
	private String mLrcPath = null;
	private String mPicPath = null;
	private int mDuration = 0;
	//baidu music
	private String mSongId = null;
	private String mAllArtistId = null;
	private String mAlbumId = null;
	private String mLrcLink = null;
	private String mAllRate = null;
	private String mCharge = null;
	private String mResourceType = null;
	private String mHavehigh = null;
	private String mCopyType = null;
	private String mRelateStatus = null;
	private String mHasMV = null;
	private String mSongPicSmall = null;
	private String mSongPicBig = null;
	private String mSongPicRadio = null;
	private String mFormat = null;
	private int mRate = -1;
	private int mSize = -1;
	private String mAppendix = null;
	private String mContent = null;

	public MusicInfo() {
	}

    public MusicInfo(Cursor cursor) {
        _id = cursor.getInt(MusicPlayerDatabase.INDEX_ID);
    	mListId = cursor.getInt(MusicPlayerDatabase.INDEX_LIST_ID);
    	  mName = cursor.getString(MusicPlayerDatabase.INDEX_NAME);
    	  mArtist = cursor.getString(MusicPlayerDatabase.INDEX_ARTIST);
    	  mAlbum = cursor.getString(MusicPlayerDatabase.INDEX_ALBUM);
    	  mMusicPath = cursor.getString(MusicPlayerDatabase.INDEX_MUSIC_PATH);
    	  mLrcPath = cursor.getString(MusicPlayerDatabase.INDEX_LRC_PATH);
    	  mPicPath = cursor.getString(MusicPlayerDatabase.INDEX_PIC_PATH);
    	mDuration = cursor.getInt(MusicPlayerDatabase.INDEX_DURATION);;

    	mSongId = cursor.getString(MusicPlayerDatabase.INDEX_SONG_ID);
    	mAllArtistId = cursor.getString(MusicPlayerDatabase.INDEX_ALL_ARTIST_ID);
    	mAlbumId = cursor.getString(MusicPlayerDatabase.INDEX_ALBUM_ID);
    	mLrcLink = cursor.getString(MusicPlayerDatabase.INDEX_LRC_LINK);
    	mAllRate = cursor.getString(MusicPlayerDatabase.INDEX_ALL_RATE);
    	mCharge = cursor.getString(MusicPlayerDatabase.INDEX_CHARGE);
    	mResourceType = cursor.getString(MusicPlayerDatabase.INDEX_RESOURCE_TYPE);
    	mHavehigh = cursor.getString(MusicPlayerDatabase.INDEX_HAVE_HIGH);
    	mCopyType = cursor.getString(MusicPlayerDatabase.INDEX_COPY_TYPE);
    	mRelateStatus = cursor.getString(MusicPlayerDatabase.INDEX_RELATE_STATUS);
    	mHasMV = cursor.getString(MusicPlayerDatabase.INDEX_HAS_MV);
    	mSongPicSmall = cursor.getString(MusicPlayerDatabase.INDEX_SONG_PIC_SMALL);
    	mSongPicBig = cursor.getString(MusicPlayerDatabase.INDEX_SONG_PIC_BIG);
    	mSongPicRadio = cursor.getString(MusicPlayerDatabase.INDEX_SONG_PIC_RADIO);
    	mFormat = cursor.getString(MusicPlayerDatabase.INDEX_FORMAT);
    	mRate = cursor.getInt(MusicPlayerDatabase.INDEX_RATE);;
    	mSize = cursor.getInt(MusicPlayerDatabase.INDEX_SIZE);;
    	mAppendix = cursor.getString(MusicPlayerDatabase.INDEX_APPENDIX);
    	mContent = cursor.getString(MusicPlayerDatabase.INDEX_CONTENT);
    }

	public MusicInfo(String name, String path) {
		mName = name;
		mMusicPath = path;
	}
	
	public String getArtist() {
		return mArtist;
	}

	public void setArtist(String artist) {
		mArtist = artist;
	}

	public String getAlbum() {
		return mAlbum;
	}

	public void setAlbum(String album) {
		mAlbum = album;
	}

	public String getMusicPath() {
		return mMusicPath;
	}

	public void setMusicPath(String musicPath) {
		mMusicPath = musicPath;
	}

	public String getLrcPath() {
		return mLrcPath;
	}

	public void setLrcPath(String lrcPath) {
		mLrcPath = lrcPath;
	}

	public String getPicPath() {
		return mPicPath;
	}

	public void setPicPath(String picPath) {
		mPicPath = picPath;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public void setDuration(int duration) {
		mDuration = duration;
	}

	public int getDuration() {
		return mDuration;
	}

	public long getId() {
		return _id;
	}

	public void setId(long id) {
		_id = id;
	}

	public String getSongId() {
		return mSongId;
	}


	public void setSongId(String songId) {
		mSongId = songId;
	}


	public String getAllArtistId() {
		return mAllArtistId;
	}


	public void setAllArtistId(String id) {
		mAllArtistId = id;
	}


	public String getAlbumId() {
		return mAlbumId;
	}


	public void setAlbumId(String albumId) {
		mAlbumId = albumId;
	}


	public String getLrclink() {
		return mLrcLink;
	}


	public void setLrclink(String lrclink) {
		mLrcLink = lrclink;
	}


	public String getAllRate() {
		return mAllRate;
	}


	public void setAllRate(String allRate) {
		mAllRate = allRate;
	}


	public String getCharge() {
		return mCharge;
	}


	public void setCharge(String charge) {
		mCharge = charge;
	}


	public String getResourceType() {
		return mResourceType;
	}


	public void setResourceType(String resourceType) {
		mResourceType = resourceType;
	}


	public String getHavehigh() {
		return mHavehigh;
	}


	public void setHavehigh(String havehigh) {
		mHavehigh = havehigh;
	}


	public String getCopyType() {
		return mCopyType;
	}


	public void setCopyType(String copyType) {
		mCopyType = copyType;
	}


	public String getRelateStatus() {
		return mRelateStatus;
	}


	public void setRelateStatus(String relateStatus) {
		mRelateStatus = relateStatus;
	}


	public String getHasMv() {
		return mHasMV;
	}


	public void setHasMv(String has_mv) {
		mHasMV = has_mv;
	}


	public String getSongPicSmall() {
		return mSongPicSmall;
	}


	public void setSongPicSmall(String songPicSmall) {
		mSongPicSmall = songPicSmall;
	}


	public String getSongPicBig() {
		return mSongPicBig;
	}


	public void setSongPicBig(String songPicBig) {
		mSongPicBig = songPicBig;
	}


	public String getSongPicRadio() {
		return mSongPicRadio;
	}


	public void setSongPicRadio(String songPicRadio) {
		mSongPicRadio = songPicRadio;
	}

	public String getFormat() {
		return mFormat;
	}


	public void setFormat(String format) {
		mFormat = format;
	}


	public int getRate() {
		return mRate;
	}


	public void setRate(int rate) {
		mRate = rate;
	}


	public int getSize() {
		return mSize;
	}


	public void setSize(int size) {
		mSize = size;
	}


	public String getAppendix() {
		return mAppendix;
	}


	public void setAppendix(String appendix) {
		mAppendix = appendix;
	}


	public String getContent() {
		return mContent;
	}


	public void setContent(String content) {
		mContent = content;
	}

	@Override
	public String toString() {
		return "MusicInfo [name=" + mName + ", artist="
				+ mArtist + ", album=" + mAlbum + ", musicPath=" + mMusicPath
				+ ", lrcPath=" + mLrcPath + ", picPath=" + mPicPath
				+ ", duration=" + mDuration
				+ ", _id=" + _id + ", mSongId=" + mSongId + ", mAllArtistId="
				+ mAllArtistId + ", mAlbumId=" + mAlbumId + ", mLrcLink="
				+ mLrcLink + ", mAllRate=" + mAllRate + ", mCharge=" + mCharge
				+ ", mResourceType=" + mResourceType + ", mHavehigh="
				+ mHavehigh + ", mCopyType=" + mCopyType + ", mRelateStatus="
				+ mRelateStatus + ", mHasMV=" + mHasMV + ", mSongPicSmall="
				+ mSongPicSmall + ", mSongPicBig=" + mSongPicBig
				+ ", mSongPicRadio=" + mSongPicRadio + ", mFormat=" + mFormat
				+ ", mRate=" + mRate + ", mSize=" + mSize + ", mAppendix="
				+ mAppendix + ", mContent=" + mContent + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mName);
		dest.writeString(mArtist);
		dest.writeString(mAlbum);
		dest.writeString(mMusicPath);
		dest.writeString(mLrcPath);
		dest.writeString(mPicPath);
		dest.writeInt(mDuration);
		dest.writeLong(_id);
		//baidu music
		dest.writeString(mSongId);
		dest.writeString(mAllArtistId);
		dest.writeString(mAlbumId);
		dest.writeString(mLrcLink);
		dest.writeString(mAllRate);
		dest.writeString(mCharge);
		dest.writeString(mResourceType);
		dest.writeString(mHavehigh);
		dest.writeString(mCopyType);
		dest.writeString(mRelateStatus);
		dest.writeString(mHasMV);
		dest.writeString(mSongPicSmall);
		dest.writeString(mSongPicBig);
		dest.writeString(mSongPicRadio);
		dest.writeString(mFormat);
		dest.writeInt(mRate);
		dest.writeInt(mSize);
		dest.writeString(mAppendix);
		dest.writeString(mContent);
	}

	public void readFromParcel(Parcel in) { 
		mName = in.readString();
		mArtist =  in.readString();
		mAlbum =  in.readString();
		mMusicPath = in.readString();
		mLrcPath =  in.readString();
		mPicPath =  in.readString();
		mDuration = in.readInt();
		_id = in.readLong();;
		//baidu music
		mSongId =  in.readString();
		mAllArtistId =  in.readString();
		mAlbumId =  in.readString();
		mLrcLink =  in.readString();
		mAllRate =  in.readString();
		mCharge =  in.readString();
		mResourceType =  in.readString();
		mHavehigh =  in.readString();
		mCopyType =  in.readString();
		mRelateStatus =  in.readString();
		mHasMV =  in.readString();
		mSongPicSmall =  in.readString();
		mSongPicBig =  in.readString();
		mSongPicRadio =  in.readString();
		mFormat =  in.readString();
		mRate = in.readInt();
		mSize = in.readInt();
		
		mAppendix =  in.readString();
		mContent =  in.readString();
	}
	
	public static final Parcelable.Creator<MusicInfo> CREATOR = new Parcelable.Creator<MusicInfo>() {
		public MusicInfo createFromParcel(Parcel in) {
			return new MusicInfo(in);
		}

		public MusicInfo[] newArray(int size) {
			return new MusicInfo[size];
		}
		
	};

	private MusicInfo(Parcel in) {
		readFromParcel(in);
	}

	public long getListId() {
		return mListId;
	}

	public void setListId(long listId) {
		mListId = listId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (_id ^ (_id >>> 32));
		result = prime * result
				+ ((mLrcPath == null) ? 0 : mLrcPath.hashCode());
		result = prime * result
				+ ((mMusicPath == null) ? 0 : mMusicPath.hashCode());
		result = prime * result + ((mName == null) ? 0 : mName.hashCode());
		result = prime * result
				+ ((mPicPath == null) ? 0 : mPicPath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MusicInfo other = (MusicInfo) obj;
		if (_id != other._id)
			return false;
		if (mLrcPath == null) {
			if (other.mLrcPath != null)
				return false;
		} else if (!mLrcPath.equals(other.mLrcPath))
			return false;
		if (mMusicPath == null) {
			if (other.mMusicPath != null)
				return false;
		} else if (!mMusicPath.equals(other.mMusicPath))
			return false;
		if (mName == null) {
			if (other.mName != null)
				return false;
		} else if (!mName.equals(other.mName))
			return false;
		if (mPicPath == null) {
			if (other.mPicPath != null)
				return false;
		} else if (!mPicPath.equals(other.mPicPath))
			return false;
		return true;
	}
}
