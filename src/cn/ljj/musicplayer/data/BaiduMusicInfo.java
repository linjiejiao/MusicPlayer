package cn.ljj.musicplayer.data;


public class BaiduMusicInfo extends MusicInfo{
	public BaiduMusicInfo(String name, String path) {
		super(name, path);
	}

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
		return "BaiduMusicInfo [mSongId=" + mSongId + ", mAllArtistId="
				+ mAllArtistId + ", mAlbumId=" + mAlbumId + ", mLrcLink="
				+ mLrcLink + ", mAllRate=" + mAllRate + ", mCharge=" + mCharge
				+ ", mResourceType=" + mResourceType + ", mHavehigh="
				+ mHavehigh + ", mCopyType=" + mCopyType + ", mRelateStatus="
				+ mRelateStatus + ", mHasMV=" + mHasMV + ", mSongPicSmall="
				+ mSongPicSmall + ", mSongPicBig=" + mSongPicBig
				+ ", mSongPicRadio=" + mSongPicRadio + ", mFormat=" + mFormat
				+ ", mRate=" + mRate + ", mSize=" + mSize + ", mAppendix="
				+ mAppendix + ", mContent=" + mContent + "]" 
				+ "; \r\n"+ super.toString();
	}

}
