package cn.ljj.musicplayer.player.state;

import android.util.Log;
import cn.ljj.musicplayer.data.MusicInfo;
import cn.ljj.musicplayer.player.Player;

public class StateError extends AbstractState {

	public StateError(Player player) {
		super(player);
	}

	@Override
	public boolean processEvent(PlayEvent event) {
		AbstractState nextState = null;
		if (mPlayer.reset()) {
			switch (event.getEventCode()) {
				case PlayEvent.EVENT_PLAY:
					if (mPlayer.play((MusicInfo) event.getObjectValue())) {
						nextState = mPlayer.getStatePlaying();
					}
					break;
				case PlayEvent.EVENT_STOP:
					nextState = mPlayer.getStateStop();
					break;
				case PlayEvent.EVENT_PREV:
					if (mPlayer.play((MusicInfo) event.getObjectValue())) {
						nextState = mPlayer.getStatePlaying();
					}
					break;
				case PlayEvent.EVENT_NEXT:
					if (mPlayer.play((MusicInfo) event.getObjectValue())) {
						nextState = mPlayer.getStatePlaying();
					}
					break;
				case PlayEvent.EVENT_RELOADLIST:
					nextState = this;
					break;
				case PlayEvent.EVENT_SEEK:
					if (mPlayer.seek(event.getIntValue())) {
						nextState = mPlayer.getStatePlaying();
					}
					break;
				default:
					Log.e("StateError",
							"processEvent: invalid event = " + event.toString());
			}
		}
		if (nextState != null) {
			mPlayer.setState(nextState);
			return true;
		} else {
			mPlayer.setState(mPlayer.getStateError());
			return false;
		}
	}

}
