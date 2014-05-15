package cn.ljj.musicplayer.player;

import android.media.MediaPlayer;
import android.util.Log;
import cn.ljj.musicplayer.data.MusicInfo;

public class Player {
	String TAG = "Player";
	StatePlaying statePlaying = new StatePlaying(this);
	StateStop stateStop = new StateStop(this);
	StateIdel stateIdel = new StateIdel(this);
	StateError stateError = new StateError(this);
	private AbstractState mState = stateIdel;
	MediaPlayer mMediaPlayer = null;
	private static  Player sInstance = new Player();
	
	private Player(){
		mMediaPlayer = new MediaPlayer();
	}

	public static Player getPlayer(){
		return sInstance;
	}
	
	protected StateError getStateError() {
		return stateError;
	}

	protected StatePlaying getStatePlaying() {
		return statePlaying;
	}

	protected StateStop getStateStop() {
		return stateStop;
	}

	protected StateIdel getStateIdel() {
		return stateIdel;
	}

	protected AbstractState getState() {
		return mState;
	}

	protected void setState(AbstractState state) {
		Log.e(TAG, "setState state="+state);
		mState = state;
	}

	protected boolean play(MusicInfo music) {
		Log.e(TAG, "play music=" + music);
		try {
			reset() ;
			mMediaPlayer.setDataSource(music.getMusicPath());
			mMediaPlayer.prepare();
			mMediaPlayer.start();
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	protected boolean stop() {
		Log.e(TAG, "stop");
		try {
			mMediaPlayer.pause();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	protected boolean seek(int pos) {
		Log.e(TAG, "seek pos=" + pos);
		try {
			int time = (mMediaPlayer.getDuration() * pos)/100;
			mMediaPlayer.seekTo(time);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	protected boolean reset() {
		Log.e(TAG, "reset");
		mMediaPlayer.reset();
		return true;
	}

	public synchronized boolean handelEvent(PlayEvent event){
		return mState.processEvent(event);
	}
}
