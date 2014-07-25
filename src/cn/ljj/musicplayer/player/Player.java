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
		Logger.v(TAG, "setState state="+state);
		mState = state;
	}

	protected boolean play(final MusicInfo music, final boolean reset) {
		if(music == null){
			Logger.e(TAG, "play music=" + music);
			return false;
		}
		Logger.i(TAG, "play music=" + music);
		new Thread(){
			@Override
			public void run() {
				try {
					if(reset){
						reset() ;
						mMediaPlayer.setDataSource(music.getMusicPath());
						mMediaPlayer.prepare();
					}
					mMediaPlayer.start();
				}catch (Exception e) {
					setState(getStateStop());
					e.printStackTrace();
				}
				super.run();
			}
		}.start();
		return true;
	}

	protected boolean stop() {
		Logger.v(TAG, "stop");
		try {
			mMediaPlayer.pause();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	protected boolean seek(final int pos) {
		Logger.v(TAG, "seek pos=" + pos);
			new Thread(){
				@Override
				public void run() {
					try {
						int time = (mMediaPlayer.getDuration() * pos)/100;
						mMediaPlayer.seekTo(time);
					} catch (Exception e) {
						setState(getStateStop());
						e.printStackTrace();
					}
				}
			}.start();
		return true;
	}

	protected boolean reset() {
		Logger.v(TAG, "reset");
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
		return mMediaPlayer.isPlaying() && (statePlaying.equals(getState()));
	}

	public int getDuration(){
		return mMediaPlayer.getDuration();
	}

	public void setOnCompletionListener(OnCompletionListener listner){
		mMediaPlayer.setOnCompletionListener(listner);
	}
}
