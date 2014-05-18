package cn.ljj.musicplayer.ui.lrc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LrcParser{
	private int mCount = 0;
	private List<LyricLine> mLrcList = new ArrayList<LyricLine>();
	private String title = " ";
	private String artist = " ";
	private String album = " ";
	private String by = " ";
	
	public LrcParser(){
	}

	public int parser(String str){
		System.out.println("LRC parser start");
		String line = null;
		Pattern pLrc = Pattern.compile("\\[\\d{2}:\\d{2}.\\d{2}\\]");	//���
		Pattern pTi = Pattern.compile("\\[ti:[^>]*\\]");	//title
		Pattern pAr = Pattern.compile("\\[ar:[^>]*\\]");	//artist
		Pattern pAl = Pattern.compile("\\[al:[^>]*\\]");	//album
		Pattern pBy = Pattern.compile("\\[by:[^>]*\\]");	//by
		ArrayList<String> list = readFile(str);
		if(list == null)
			return -1;
		mLrcList.clear();
		mCount = list.size();
		int i=0;
		for(i=0;i<mCount;i++){
			line = list.get(i);
			Matcher m = pLrc.matcher(line);
			if(m.find()){	//���
				while((m = pLrc.matcher(line)).find()){	//ѭ����ȡ�����һ���ʶ�Ӧ���ʱ�̵����
														//�磺[01:23][45:67]��������
					String temp = null;
					LyricLine lyricLine = new LyricLine();
					int index1 = 0;//line.indexOf("[");		
					int index2 = 3;//line.indexOf(":");
					int index3 = 6;//line.indexOf(".");		//���ֲ��淶��ʳ���[01:23:00]
					int index4 = 9;//line.indexOf("]");
					int index5 = line.lastIndexOf("]");
					temp = line.substring(index1+1,index2);
					lyricLine.time = Integer.parseInt(temp)*60*1000;
					temp = line.substring(index2+1,index3);
					lyricLine.time += Integer.parseInt(temp)*1000;
					temp = line.substring(index3+1,index4);
					lyricLine.time += Integer.parseInt(temp)*10;
					//System.out.println("lyricLine.time = "+lyricLine.time);
					temp = line.substring(index5+1);
					line = line.substring(index4+1);
					lyricLine.lyric = temp;
					mLrcList.add(lyricLine);
					//System.out.println("lyricLine.lyric = "+lyricLine.lyric);
				}
			}else{
				Matcher mTi = pTi.matcher(line);
				if(mTi.find()){
					title = line.substring(line.indexOf(":")+1,line.indexOf("]"));
					System.out.println("title=" + title);
				}else{
					Matcher mAr = pAr.matcher(line);
					if(mAr.find()){
						artist = line.substring(line.indexOf(":")+1,line.indexOf("]"));
						//System.out.println("artist=" + artist);
					}else{
						Matcher mAl = pAl.matcher(line);
						if(mAl.find()){
							album = line.substring(line.indexOf(":")+1,line.indexOf("]"));
							//System.out.println("album=" + album);
						}else{
							Matcher mBy = pBy.matcher(line);
							if(mBy.find()){
								by = line.substring(line.indexOf(":")+1,line.indexOf("]"));
								//System.out.println("by=" + by);
							}else{
								//System.out.println("not match line = " + line);
							}
						}
					}
				}
			}
		}
		if(mLrcList.size() == 0)
			return 0;
		int timeEverage = mLrcList.get(0).time/4;
		LyricLine lyricLine1 = new LyricLine();
		lyricLine1.lyric = title;
		lyricLine1.time = 0;
		LyricLine lyricLine2 = new LyricLine();
		lyricLine2.lyric = artist;
		lyricLine2.time = timeEverage;
		LyricLine lyricLine3 = new LyricLine();
		lyricLine3.lyric = album;
		lyricLine3.time = 2*timeEverage;
		LyricLine lyricLine4 = new LyricLine();
		lyricLine4.lyric = by;
		lyricLine4.time = 3*timeEverage;
		mLrcList.add(lyricLine1);
		mLrcList.add(lyricLine2);
		mLrcList.add(lyricLine3);
		mLrcList.add(lyricLine4);
		Collections.sort(mLrcList,comparator); 
		mCount = mLrcList.size();
		System.out.println("LRC parser end");
		return mCount;
	}

	private ArrayList<String> readFile(String filepath) {
		File file = new File(filepath);
		if(!file.exists())
			return null;
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		BufferedReader reader = null;
		ArrayList<String> list = new ArrayList<String>();
		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			reader = new BufferedReader(new InputStreamReader(bis,"utf-8"));
			String str = reader.readLine();
			while (str != null) {
				list.add(str);
				str = reader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return list;
	}

	public List <LyricLine> getLrcList(){
		return mLrcList;
	}

	private Comparator<LyricLine> comparator = new Comparator<LyricLine>(){
	public int compare(LyricLine l1, LyricLine l2) {       
			return l1.time-l2.time;    
		}
	};  
	
	public class LyricLine{
		private int time = 0;
		private String lyric = null;
		public int getTime() {
			return time;
		}
		public void setTime(int time) {
			this.time = time;
		}
		public String getLyric() {
			return lyric;
		}
		public void setLyric(String lyric) {
			this.lyric = lyric;
		}
		
	}
}
