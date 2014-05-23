package cn.ljj.musicplayer.files;

import cn.ljj.musicplayer.data.StaticUtils;
import cn.ljj.musicplayer.files.Downloader.DownloadCallback;

public class DownloadFactory {
	public static Downloader DownloadMusic(String url, String saveName,
			DownloadCallback callback) {
		String savePath = StaticUtils.getMusicPath() + saveName;
		Downloader dl = new Downloader(url, savePath);
		dl.setCallBack(callback);
		new Thread(dl).start();
		return dl;
	}

	public static Downloader DownloadPic(String url, String saveName,
			DownloadCallback callback) {
		String savePath = StaticUtils.getPicPath() + saveName;
		Downloader dl = new Downloader(url, savePath);
		dl.setCallBack(callback);
		new Thread(dl).start();
		return dl;
	}

	public static Downloader DownloadLrc(String url, String saveName,
			DownloadCallback callback) {
		String savePath = StaticUtils.getLrcPath() + saveName;
		Downloader dl = new Downloader(url, savePath);
		dl.setCallBack(callback);
		new Thread(dl).start();
		return dl;
	}
}
