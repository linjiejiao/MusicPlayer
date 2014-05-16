package cn.ljj.musicplayer.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Downloader implements Runnable, Defines {
	public interface DownloadCallback {
		public void onProgressChange(int length, int finished);

		public void onFinished(String filePath);

		public void onFaild(int errorCode);
	}

	String mUrl = null;
	String mSavePath = null;
	boolean mCancel = false;
	DownloadCallback mCallback = null;

	public Downloader(String url, String savePath) {
		mUrl = url;
		mSavePath = savePath;
	}

	public void setCallBack(DownloadCallback callbakc) {
		mCallback = callbakc;
	}

	private void onProgressChange(int length, int finished) {
		if (mCallback != null) {
			mCallback.onProgressChange(length, finished);
		}
	}

	private void onFinished(String filePath) {
		if (mCallback != null) {
			mCallback.onFinished(filePath);
		}
	}

	private void onFaild(int errorCode) {
		if (mCallback != null) {
			mCallback.onFaild(errorCode);
		}
	}

	@Override
	public void run() {
		if(mCancel){
			return;
		}
		if ((mSavePath == null) || (mUrl == null)) {
			onFaild(ERROR_PARAMETER);
			return;
		}
		boolean isComplete = false;
		File file = new File(mSavePath);
		FileOutputStream fileops = null;
		InputStream inStream = null;
		try {
			URL url = new URL(mUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			inStream = conn.getInputStream();
			int length = conn.getContentLength();
			int finished = 0;
			file.createNewFile();
			fileops = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int len = inStream.read(buffer);
			while ((len != -1) && (!mCancel)) {
				finished += len;
				fileops.write(buffer, 0, len);
				len = inStream.read(buffer);
				onProgressChange(length, finished);
			}
			if(!mCancel){
				onFinished(mSavePath);
				isComplete = true;
			}
		} catch (MalformedURLException e) {
			onFaild(ERROR_MALFORMEDURL);
			e.printStackTrace();
		} catch (ProtocolException e) {
			onFaild(ERROR_PROTOCOL);
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			onFaild(ERROR_FILENOTFOUND);
			e.printStackTrace();
		} catch (IOException e) {
			onFaild(ERROR_IOEXCEPTION);
			e.printStackTrace();
		} finally {
			try {
				if (fileops != null) {
					fileops.close();
				}
				if (inStream != null) {
					inStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (!isComplete) {
			file.delete();
		}
	}
	public void cancel(){
		mCancel = true;
	}
}
