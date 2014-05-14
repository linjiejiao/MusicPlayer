package cn.ljj.musicplayer.player;

import cn.ljj.musicplayer.data.MusicInfo;
import cn.ljj.musicplayer.player.state.AbstractState;
import cn.ljj.musicplayer.player.state.StateError;
import cn.ljj.musicplayer.player.state.StateIdel;
import cn.ljj.musicplayer.player.state.StatePlaying;
import cn.ljj.musicplayer.player.state.StateStop;

public class Player {
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
		mState = state;
	}

	public boolean play(MusicInfo music) {
		return true;
	}

	public boolean stop() {
		return true;
	}

	public boolean seek(int pos) {
		return true;
	}

	public boolean reset() {
		return true;
	}
}
