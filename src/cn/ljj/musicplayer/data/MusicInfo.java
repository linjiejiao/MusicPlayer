package cn.ljj.musicplayer.data;

import android.os.Parcel;
import android.os.Parcelable;

public class MusicInfo implements Parcelable {
	public static final int LOCATION_LOCAL = 0;
	public static final int LOCATION_ONLINE = 1;
	
	private int mLocation = LOCATION_LOCAL;
	private String mName = null;
	private String mArtist = null;
	private String mAlbum = null;
	private String mMusicPath = null;
	private String mLrcPath = null;
	private String mPicPath = null;
	private String mDurationStr = null;
	private int mDuration = 0;
	private long _id = -1;
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

	public MusicInfo(String name, String path) {
		mName = name;
		mMusicPath = path;
	}

	public int getLocation() {
		return mLocation;
	}

	public void setLocation(int location) {
		mLocation = location;
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

	public String getDurationStr() {
		return mDurationStr;
	}

	public void setDurationStr(String durationStr) {
		mDurationStr = durationStr;
	}

	public void setDuration(int duration) {
		mDuration = duration;
		setDurationStr(StaticUtils.getDispTime(duration));
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
		return "MusicInfo [isLocal=" +mLocation + ", name=" + mName + ", artist="
				+ mArtist + ", album=" + mAlbum + ", musicPath=" + mMusicPath
				+ ", lrcPath=" + mLrcPath + ", picPath=" + mPicPath
				+ ", durationStr=" + mDurationStr + ", duration=" + mDuration
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
		dest.writeInt(mLocation);
		dest.writeString(mName);
		dest.writeString(mArtist);
		dest.writeString(mAlbum);
		dest.writeString(mMusicPath);
		dest.writeString(mLrcPath);
		dest.writeString(mPicPath);
		dest.writeString(mDurationStr);
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
		mLocation = in.readInt();
		mName = in.readString();
		mArtist =  in.readString();
		mAlbum =  in.readString();
		mMusicPath = in.readString();
		mLrcPath =  in.readString();
		mPicPath =  in.readString();
		mDurationStr =  in.readString();
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

}
