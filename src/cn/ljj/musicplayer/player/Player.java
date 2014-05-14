package cn.ljj.musicplayer.player;

import android.util.Log;
import cn.ljj.musicplayer.data.MusicInfo;
import cn.ljj.musicplayer.player.state.AbstractState;
import cn.ljj.musicplayer.player.state.PlayEvent;
import cn.ljj.musicplayer.player.state.StateError;
import cn.ljj.musicplayer.player.state.StateIdel;
import cn.ljj.musicplayer.player.state.StatePlaying;
import cn.ljj.musicplayer.player.state.StateStop;

public class Player {
	String TAG = "Player";
	private AbstractState mState = null;
	StatePlaying statePlaying = new StatePlaying(this);
	StateStop stateStop = new StateStop(this);
	StateIdel stateIdel = new StateIdel(this);
	StateError stateError = new StateError(this);

	public StateError getStateError() {
		return stateError;
	}

	public StatePlaying getStatePlaying() {
		return statePlaying;
	}

	public StateStop getStateStop() {
		return stateStop;
	}

	public StateIdel getStateIdel() {
		return stateIdel;
	}

	public AbstractState getState() {
		return mState;
	}

	public void setState(AbstractState state) {
		Log.e(TAG, "setState state="+state);
		mState = state;
	}

	public boolean play(MusicInfo music) {
		Log.e(TAG, "play music=" + music);
		return true;
	}

	public boolean stop() {
		Log.e(TAG, "stop");
		return true;
	}

	public boolean seek(int pos) {
		Log.e(TAG, "seek pos=" + pos);
		return true;
	}

	public boolean reset() {
		Log.e(TAG, "reset");
		return true;
	}

	public boolean handelEvent(PlayEvent event){
		return mState.processEvent(event);
	}
}
