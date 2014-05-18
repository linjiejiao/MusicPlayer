package cn.ljj.musicplayer.player.service;

import cn.ljj.musicplayer.data.MusicInfo;
import android.os.RemoteException;

public class NotifyImpl extends INotify.Stub{
	public static final int CMD_PLAY_EVENT = 1;
	public static final int CMD_STOP_EVENT = 2;
//	public static final int CMD_NEXT_EVENT = 3;
//	public static final int CMD_PREV_EVENT = 4;
	public static final int CMD_SEEK_EVENT = 5;
	public static final int CMD_UPDATE_PROGRESS = 11;
	public static final int CMD_PLAY_REACH_END = 12;
	public static final int CMD_REPORT_STATUS = 13;
	
	public static final int RET_OK = 0;
	public static final int RET_ERROR = -1;
	public static final int RET_INVALID = -2;
	@Override
	public void setCallback(INotify callback) throws RemoteException {
		
	}
	@Override
	public int onNotify(int cmd, int intValue, long longValue, String str, MusicInfo music)
			throws RemoteException {
		return RET_OK;
	}

}
