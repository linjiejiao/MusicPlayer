package cn.ljj.musicplayer.files;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import cn.ljj.musicplayer.data.MusicInfo;
import cn.ljj.musicplayer.database.Logger;
import android.util.Xml;

public class SearchResualtPaser {
	static String TAG = "SearchResualtPaser";

	public static ArrayList <MusicInfo> parse(InputStream xml) throws XmlPullParserException, IOException {
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(xml, "UTF-8");
		int eventType = parser.getEventType();
		ArrayList <MusicInfo> infos = null;
		MusicInfo info = null;
		while(eventType != XmlPullParser.END_DOCUMENT){
			switch(eventType){
				case XmlPullParser.START_DOCUMENT:
					infos = new ArrayList <MusicInfo>();
					break;
				case XmlPullParser.START_TAG:
					if("song_list".equals(parser.getName())){
						if(parser.getAttributeName(0).equals("false"))
							return null;
					}
					if("song".equals(parser.getName())){
						info = new MusicInfo();
						break;
					}
					if(info != null){
						if("title".equals(parser.getName())){
							info.setName(titleRefix(parser.nextText()));
							break;
						}
						if("song_id".equals(parser.getName())){
							info.setSongId(parser.nextText()) ;
							break;
						}
						if("author".equals(parser.getName())){
							info.setArtist(titleRefix(parser.nextText()));
							break;
						}
						if("all_artist_id".equals(parser.getName())){
							info.setAllArtistId(parser.nextText()) ;
							break;
						}
						if("album_title".equals(parser.getName())){
							info.setAlbum(titleRefix(parser.nextText()));
							break;
						}
						if("album_id".equals(parser.getName())){
							info.setAlbumId(parser.nextText());
							break;
						}
						if("lrclink".equals(parser.getName())){
							String lrclink = parser.nextText();
							if((lrclink != null)&&
									(lrclink.substring(lrclink.lastIndexOf(".")+1).equals("lrc")))
								info.setLrclink("http://ting.baidu.com" + lrclink);
							break;
						}
						if("all_rate".equals(parser.getName())){
							info.setAllRate(parser.nextText());
							break;
						}
						if("charge".equals(parser.getName())){
							info.setCharge(parser.nextText());
							break;
						}
						if("resource_type".equals(parser.getName())){
							info.setResourceType(parser.nextText());
							break;
						}
						if("havehigh".equals(parser.getName())){
							info.setHavehigh(parser.nextText());
							break;
						}
						if("copy_type".equals(parser.getName())){
							info.setCopyType(parser.nextText());
							break;
						}
						if("relate_status".equals(parser.getName())){
							info.setRelateStatus(parser.nextText());
							break;
						}
						if("has_mv".equals(parser.getName())){
							info.setHasMv(parser.nextText());
							break;
						}
						if("appendix".equals(parser.getName())){
							info.setAppendix(parser.nextText());
							break;
						}
						if("content".equals(parser.getName())){
							info.setContent(parser.nextText());
							break;
						}
						
					}
				case XmlPullParser.END_TAG:
					if("song".equals(parser.getName())){
						try {
							getLinks(info, null);
						} catch (Exception e) {
							e.printStackTrace();
							info = null;
							break;
						} 
						info.setLocation(MusicInfo.LOCATION_ONLINE);
						infos.add(info);
						info = null;
					}
					break;
			}
			eventType = parser.next();
		}
		return infos;
	}
	
	static String titleRefix(String title){
		if(title.contains("<em>")){
			String temp = new String();
			String strings[] = title.split("<em>");
			for(String str:strings){
				temp += str;
			}
			title = temp;
		}
		if(title.contains("</em>")){
			String temp = new String();
			String strings[] = title.split("</em>");
			for(String str:strings){
				temp += str;
			}
			title = temp;
		}
		return title;
	}

	public static void getLinks(MusicInfo info, String rate) throws Exception{
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
//			String
			info.setSongPicSmall(fixLink(item.getString("songPicSmall")));
			info.setSongPicBig(fixLink(item.getString("songPicBig")));
			info.setSongPicRadio(fixLink(item.getString("songPicRadio")));
			info.setMusicPath(fixLink(item.getString("songLink")));
			info.setFormat(item.getString("format"));
			info.setRate(item.getInt("rate"));
			info.setSize(item.getInt("size"));
		}else{
			Logger.e(TAG, "getLinks error errorCode=" + errorCode);
		}
	}

	private static String fixLink(String link){
		//"http://c.hiphotos.baidu.com/ting/pic/item/http://qukufile2.qianqian.com/data2/pic/115439298/115439298.jpg.jpg"
		if(link.contains("http://")){
			link = link.substring(link.lastIndexOf("http://"));
		}
		return link;
	}
}
