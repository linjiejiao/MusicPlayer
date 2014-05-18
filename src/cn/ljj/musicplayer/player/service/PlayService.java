package cn.ljj.musicplayer.player.service;

import cn.ljj.musicplayer.data.MusicInfo;
import cn.ljj.musicplayer.database.Logger;
import cn.ljj.musicplayer.player.PlayEvent;
import cn.ljj.musicplayer.player.Player;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;

public class PlayService extends Service implements OnCompletionListener {
	private String TAG = "PlayService";
	private Player mPlayer;
	private Handler mHandler = null;

	@Override
	public IBinder onBind(Intent intent) {
		Logger.i(TAG, "onBind");
		return mService;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Logger.i(TAG, "onUnbind");
		PlayEvent event = new PlayEvent();
		event.setEventCode(PlayEvent.EVENT_STOP);
		mPlayer.handelEvent(event);
		return super.onUnbind(intent);
	}

	@Override
	public void onCreate() {
		mPlayer =  new Player();
		mPlayer.setOnCompletionListener(this);
		mHandler = new Handler();
		mHandler.postDelayed(mUpdateTimer, 200);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		mHandler.removeCallbacks(mUpdateTimer);
		Logger.e(TAG, "onDestroy");
		super.onDestroy();
	}
	
	private INotify mCallback = null;
	private NotifyImpl mService = new NotifyImpl(){
		@Override
		public void setCallback(INotify callback) throws RemoteException {
			mCallback = callback;
			super.setCallback(callback);
		}

		@Override
		public int onNotify(int cmd, int intValue, long longValue, String str, MusicInfo music)
				throws RemoteException {
			PlayEvent event = new PlayEvent();
			switch(cmd){
				case CMD_PLAY_EVENT:
					event.setEventCode(PlayEvent.EVENT_PLAY);
					event.setMusic(music);
					event.setIntValue(intValue);
					mPlayer.handelEvent(event);
					break;
				case CMD_STOP_EVENT:
					event.setEventCode(PlayEvent.EVENT_STOP);
					mPlayer.handelEvent(event);
					break;
//				case CMD_NEXT_EVENT:
//					event.setEventCode(PlayEvent.EVENT_NEXT);
//					event.setMusic(music);
//					mPlayer.handelEvent(event);
//					break;
//				case CMD_PREV_EVENT:
//					event.setEventCode(PlayEvent.EVENT_PREV);
//					event.setMusic(music);
//					mPlayer.handelEvent(event);
//					break;
				case CMD_SEEK_EVENT:
					event.setEventCode(PlayEvent.EVENT_SEEK);
					event.setIntValue(intValue);
					mPlayer.handelEvent(event);
					break;
				default :
					return RET_INVALID;
			}
			return super.onNotify(cmd, intValue, longValue, str,music);
		}
	};

	private int onNotify(int cmd, int intValue, long longValue, String str, MusicInfo music){
		int ret = -1;
		if(mCallback == null){
			Logger.e(TAG, "onNotify mCallback==null");
			return ret;
		}
		try {
			ret = mCallback.onNotify(cmd, intValue, longValue, str,music);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	private Runnable mUpdateTimer = new Runnable() {
		public void run() {
			if(mPlayer.isPlaying()){
				onNotify(NotifyImpl.CMD_UPDATE_PROGRESS, mPlayer.getCurrentPosistion(), mPlayer.getDuration(),null,null);
			}
			mHandler.postDelayed(mUpdateTimer, 200);
		}
	};

	@Override
	public void onCompletion(MediaPlayer mp) {
		onNotify(NotifyImpl.CMD_PLAY_REACH_END ,0, 0,null,null);
	}
}
