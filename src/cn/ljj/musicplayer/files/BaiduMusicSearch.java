package cn.ljj.musicplayer.files;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import cn.ljj.musicplayer.data.MusicInfo;

public class BaiduMusicSearch implements Runnable,Defines{
	public interface SeachCallback{
		public void onSearchResult(List<MusicInfo> resualt);
	}
	SeachCallback mCallback = null;
	private String mUrl = null;
	public void setCallBack(SeachCallback callbakc) {
		mCallback = callbakc;
	}


	private void onSearchResult(List<MusicInfo> resualt) {
		if (mCallback != null) {
			mCallback.onSearchResult(resualt);
		}
	}

	public void search(String keys){
		try {
			mUrl = BAIDU_QUERY_BASE + URLEncoder.encode(keys, "UTF-8");
			new Thread(this).start();
		} catch (UnsupportedEncodingException e) {
			onSearchResult(null);
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		InputStream inStream = null;
		try {
			URL url = new URL(mUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
