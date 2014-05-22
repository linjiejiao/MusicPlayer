package cn.ljj.musicplayer.files;

import cn.ljj.musicplayer.data.StaticUtils;
import cn.ljj.musicplayer.files.Downloader.DownloadCallback;

public class DownloadFactory {
	public static void DownloadMusic(String url, String saveName, DownloadCallback callback){
		String savePath = StaticUtils.getMusicPath() + saveName;
		Downloader dl = new Downloader(url, savePath);
		dl.setCallBack(callback);
		new Thread(dl).start();
	}
	
public static void DownloadPic(String url, String saveName, DownloadCallback callback){
		
	}

public static void DownloadLrc(String url, String saveName, DownloadCallback callback){
	
}
}
