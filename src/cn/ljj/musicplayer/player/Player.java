package cn.ljj.musicplayer.player;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import cn.ljj.musicplayer.data.MusicInfo;
import cn.ljj.musicplayer.database.Logger;

public class Player {
	String TAG = "Player";
	StatePlaying statePlaying = new StatePlaying(this);
	StateStop stateStop = new StateStop(this);
	StateIdel stateIdel = new StateIdel(this);
	StateError stateError = new StateError(this);
	private AbstractState mState = stateIdel;
	MediaPlayer mMediaPlayer = null;
	
	public Player(){
		mMediaPlayer = new MediaPlayer();
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
		Logger.d(TAG, "setState state="+state);
		mState = state;
	}

	protected boolean play(MusicInfo music, boolean reset) {
		if(music == null){
			Logger.e(TAG, "play music=" + music);
			return false;
		}
		Logger.d(TAG, "play music=" + music);
		try {
			if(reset){
				reset() ;
				mMediaPlayer.setDataSource(music.getMusicPath());
				mMediaPlayer.prepare();
			}
			mMediaPlayer.start();
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	protected boolean stop() {
		Logger.d(TAG, "stop");
		try {
			mMediaPlayer.pause();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	protected boolean seek(int pos) {
		Logger.d(TAG, "seek pos=" + pos);
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
		Logger.d(TAG, "reset");
		mMediaPlayer.reset();
		return true;
	}

	public synchronized boolean handelEvent(PlayEvent event){
		return mState.processEvent(event);
	}
	
	public int getCurrentPosistion(){
		return mMediaPlayer.getCurrentPosition();
	}

	public boolean isPlaying(){
		return mMediaPlayer.isPlaying();
	}

	public int getDuration(){
		return mMediaPlayer.getDuration();
	}

	public void setOnCompletionListener(OnCompletionListener listner){
		mMediaPlayer.setOnCompletionListener(listner);
	}
}
