package cn.ljj.musicplayer.files;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import cn.ljj.musicplayer.data.MusicInfo;
import cn.ljj.musicplayer.database.Logger;

public class BaiduMusicSearch implements Defines {
	SeachCallback mCallback = null;
	private String mUrl = null;
	private String TAG = "BaiduMusicSearch";
	SearchThread mSearchThread = null;
	public static final  String KEY_QUERY = "query";
	public static final  String KEY_PAGE_SIZE = "page_size";
	public static final  String KEY_PAGE_NO = "page_no";

	public interface SeachCallback {
		public void onSearchResult(List<MusicInfo> resualt);
	}

	public void setCallBack(SeachCallback callbakc) {
		mCallback = callbakc;
	}

	private void onSearchResult(List<MusicInfo> resualt) {
		if (mCallback != null && !mSearchThread.mCancel) {
			mCallback.onSearchResult(resualt);
		}
	}

	public void search(String keys, int pageSize, int pageNo) {
		try {
			mUrl = BAIDU_QUERY_BASE + "&page_size=" + pageSize + "&page_no="
					+ pageNo + "&query=";
			mUrl += URLEncoder.encode(keys, "UTF-8");
			Logger.i(TAG, "search mUrl=" + mUrl);
			if (mSearchThread != null) {
				mSearchThread.mCancel = true;
			}
			mSearchThread = new SearchThread();
			mSearchThread.start();
		} catch (UnsupportedEncodingException e) {
			onSearchResult(null);
			e.printStackTrace();
		}
	}

	public List<MusicInfo> searchSync(String keys, int pageSize, int pageNo) {
		InputStream inStream = null;
		List<MusicInfo> resualt = null;
		try {
			mUrl = BAIDU_QUERY_BASE + "&page_size=" + pageSize + "&page_no="
					+ pageNo + "&query=";
			mUrl += URLEncoder.encode(keys, "UTF-8");
			Logger.i(TAG, "search mUrl=" + mUrl);
			URL url = new URL(mUrl);
			HttpURLConnection conn = (HttpURLConnection) url
					.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			inStream = conn.getInputStream();
			resualt = SearchResualtPaser.parse(inStream);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (inStream != null) {
					inStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resualt;
	}
	
	public void cancel() {
		if (mSearchThread != null) {
			mSearchThread.mCancel = true;
		}
	}

	class SearchThread extends Thread {
		boolean mCancel = false;

		@Override
		public void run() {
			InputStream inStream = null;
			try {
				URL url = new URL(mUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5 * 1000);
				inStream = conn.getInputStream();
				List<MusicInfo> resualt = SearchResualtPaser.parse(inStream);
				onSearchResult(resualt);
			} catch (Exception e) {
				onSearchResult(null);
				e.printStackTrace();
			} finally {
				try {
					if (inStream != null) {
						inStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
