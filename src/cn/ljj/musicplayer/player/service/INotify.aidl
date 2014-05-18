package cn.ljj.musicplayer.player.service;

import  cn.ljj.musicplayer.data.MusicInfo;
interface INotify {
	void setCallback(INotify callback);
	int onNotify(int cmd, int intValue, long longValue,String str, inout MusicInfo music);
}