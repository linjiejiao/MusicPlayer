package cn.ljj.musicplayer.ui.lrc;

import java.io.File;
import java.util.List;

import android.text.TextUtils;
import cn.ljj.musicplayer.data.MusicInfo;
import cn.ljj.musicplayer.files.BaiduMusicSearch;
import cn.ljj.musicplayer.files.BaiduMusicSearch.SeachCallback;
import cn.ljj.musicplayer.files.DownloadFactory;
import cn.ljj.musicplayer.files.Downloader.DownloadCallback;

public class LrcPicManager {
	public static String getLrc(final MusicInfo music){
		String path = music.getLrcPath();
		if(!TextUtils.isEmpty(path)){
			File file = new File(path);
			if(file.exists()){
				return path;
			}
		}
		String link = music.getLrclink();
		if(!TextUtils.isEmpty(link)){
			BaiduMusicSearch mSearcher = new BaiduMusicSearch();
			mSearcher.setCallBack(new SeachCallback() {
				@Override
				public void onSearchResult(List<MusicInfo> resualt) {
					for(MusicInfo musicInfo:resualt){
						String lrc = musicInfo.getLrclink();
						if(!TextUtils.isEmpty(lrc)){
							music.setLrclink(lrc);
							downloadLrc(music, lrc);
						}
					}
				}
			});
			mSearcher.search(music.getName(), 3, 0);
		}else{
			downloadLrc(music, link);
		}
		return path;
	}

	private static void downloadLrc(final MusicInfo music, String link){
		String savePath = music.getName() + ".Lrc";
		DownloadFactory.DownloadLrc(link, savePath, new DownloadCallback() {
			
			@Override
			public void onProgressChange(int length, int finished) {
			}
			
			@Override
			public void onFinished(String filePath) {
				music.setLrcPath(filePath);
				music.notifyObservers(music);
			}
			
			@Override
			public void onFaild(int errorCode) {
			}
		});
	}
	
	public static String getPic(final MusicInfo music){
		String path = music.getPicPath();
		if(!TextUtils.isEmpty(path)){
			File file = new File(path);
			if(file.exists()){
				return path;
			}
		}
		String link = music.getSongPicBig();
		if(!TextUtils.isEmpty(link)){
			BaiduMusicSearch mSearcher = new BaiduMusicSearch();
			mSearcher.setCallBack(new SeachCallback() {
				@Override
				public void onSearchResult(List<MusicInfo> resualt) {
					for(MusicInfo musicInfo:resualt){
						String pic = musicInfo.getSongPicBig();
						if(!TextUtils.isEmpty(pic)){
							music.setSongPicBig(pic);
							downloadPic(music, pic);
						}
					}
				}
			});
			mSearcher.search(music.getName(), 3, 0);
		}else{
			downloadPic(music, link);
		}
		return path;
	}

	private static void downloadPic(final MusicInfo music, String link){
		String savePath = music.getName() + ".jpg";
		DownloadFactory.DownloadLrc(link, savePath, new DownloadCallback() {
			
			@Override
			public void onProgressChange(int length, int finished) {
			}
			
			@Override
			public void onFinished(String filePath) {
				music.setPicPath(filePath);
				music.notifyObservers(music);
			}
			
			@Override
			public void onFaild(int errorCode) {
			}
		});
	}
}
