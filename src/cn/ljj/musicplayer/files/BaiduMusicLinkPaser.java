package cn.ljj.musicplayer.files;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.ljj.musicplayer.data.MusicInfo;

public class BaiduMusicLinkPaser {
	String rate = null;
	
	public BaiduMusicLinkPaser(String bitRate){
		rate = bitRate;
	}
	
	public void Parser(MusicInfo info) throws Exception{
		String addr = "http://ting.baidu.com/data/music/links?songIds=" + info.getSongId();
		if(rate != null){
			addr += "&rate="+rate;
		}
		URL url = new URL(addr);
		InputStream ips = url.openConnection().getInputStream();
		
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while( (len=ips.read(buffer)) != -1 ){
			outStream.write(buffer, 0, len);
		}
		ips.close();
		String jason = new String(outStream.toByteArray());
		JSONObject rootJson = new JSONObject(jason);
		String errorCode = rootJson.getString("errorCode");
		if(errorCode.equals("22000")){
			JSONObject data = rootJson.getJSONObject("data");
			JSONArray songList = data.getJSONArray("songList");
			JSONObject item = songList.getJSONObject(0);
			info.setSongPicSmall( item.getString("songPicSmall"));
			info.setSongPicBig(item.getString("songPicBig"));
			info.setSongPicRadio(item.getString("songPicRadio"));
			info.setPicPath(item.getString("songLink"));
			info.setFormat(item.getString("format"));
			info.setRate(item.getInt("rate"));
			info.setSize(item.getInt("size"));
		}
	}
}
