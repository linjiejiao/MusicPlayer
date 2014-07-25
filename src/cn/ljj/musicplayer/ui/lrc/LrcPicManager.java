package cn.ljj.musicplayer.ui.lrc;

import java.io.File;
import java.util.List;

import android.text.TextUtils;
import cn.ljj.musicplayer.data.MusicInfo;
import cn.ljj.musicplayer.data.StaticUtils;
import cn.ljj.musicplayer.database.Logger;
import cn.ljj.musicplayer.files.BaiduMusicSearch;
import cn.ljj.musicplayer.files.BaiduMusicSearch.SeachCallback;
import cn.ljj.musicplayer.files.DownloadFactory;
import cn.ljj.musicplayer.files.Downloader.DownloadCallback;

public class LrcPicManager {
	private static String TAG  = "LrcPicManager";

	public static String getLrc(final MusicInfo music){
		String path = music.getLrcPath();
		if(!TextUtils.isEmpty(path)){
			File file = new File(path);
			if(file.exists()){
				return path;
			}
		}else{
			String saveName = music.getName() + ".Lrc";
			path =  StaticUtils.getLrcPath() + saveName;
			File file = new File(path);
			if(file.exists()){
				music.setLrcPath(path);
				return path;
			}
		}
		String link = music.getLrclink();
		if(TextUtils.isEmpty(link)){
			BaiduMusicSearch mSearcher = new BaiduMusicSearch();
			mSearcher.setCallBack(new SeachCallback() {
				@Override
				public void onSearchResult(List<MusicInfo> resualt) {
					if(resualt != null){
						for(MusicInfo musicInfo:resualt){
							String lrc = musicInfo.getLrclink();
							if(!TextUtils.isEmpty(lrc)){
								music.setLrclink(lrc);
								downloadLrc(music, lrc);
								break;
							}
						}
					}
				}
			});
			mSearcher.search(music.getName(), 3, 1);
		}else{
			downloadLrc(music, link);
		}
		return null;
	}

	private static void downloadLrc(final MusicInfo music, String link){
		String saveName = music.getName() + ".Lrc";
		DownloadFactory.DownloadLrc(link, saveName, new DownloadCallback() {
			@Override
			public void onProgressChange(int length, int finished) {
				Logger.v(TAG , "downloadPic onFinished finished "
						+ ((finished*100)/length) + "%");
			}
			
			@Override
			public void onFinished(String filePath) {
				Logger.v(TAG , "downloadLrc onFinished filePath="+filePath);
				music.setLrcPath(filePath);
			}
			
			@Override
			public void onFaild(int errorCode) {
				Logger.e(TAG , "downloadLrc onFaild errorCode = "+errorCode);
			}
		});
	}
	
	public static String getPic(final MusicInfo music){
		String path = music.getPicPath();
		if(!TextUtils.isEmpty(path)){
			//associated pic
			File file = new File(path);
			if(file.exists()){
				Logger.v(TAG , "associated pic found "+path);
				return path;
			}
		}else{
			//old exited pic
			String saveName = music.getName() + ".pic";
			path =  StaticUtils.getPicPath() + saveName;
			File file = new File(path);
			if(file.exists()){
				music.setPicPath(path);
				Logger.v(TAG , "old pic found "+path);
				return path;
			}
		}
		//download a pic
		Logger.v(TAG , "download a pic");
		String link = music.getSongPicBig();
		if(TextUtils.isEmpty(link)){
			BaiduMusicSearch mSearcher = new BaiduMusicSearch();
			mSearcher.setCallBack(new SeachCallback() {
				@Override
				public void onSearchResult(List<MusicInfo> resualt) {
					if(resualt == null){
						return;
					}
					Logger.e(TAG , "getPic onSearchResult resualt="+resualt);
					for(MusicInfo musicInfo:resualt){
						String pic = musicInfo.getSongPicRadio();
						if(!TextUtils.isEmpty(pic)){
							music.setSongPicRadio(pic);
							downloadPic(music, pic);
							break;
						}
					}
				}
			});
			mSearcher.search(music.getName(), 3, 1);
		}else{
			downloadPic(music, link);
		}
		return null;
	}

	private static void downloadPic(final MusicInfo music, String link){
		String saveName = music.getName() + ".jpg";
		DownloadFactory.DownloadPic(link, saveName, new DownloadCallback() {
			
			@Override
			public void onProgressChange(int length, int finished) {
				Logger.v(TAG , "downloadPic onFinished finished "
						+ ((finished*100)/length) + "%");
			}
			
			@Override
			public void onFinished(String filePath) {
				Logger.v(TAG , "downloadPic onFinished filePath="+filePath);
				music.setPicPath(filePath);
			}
			
			@Override
			public void onFaild(int errorCode) {
				Logger.e(TAG , "downloadPic onFaild errorCode = "+errorCode);
			}
		});
	}
}
