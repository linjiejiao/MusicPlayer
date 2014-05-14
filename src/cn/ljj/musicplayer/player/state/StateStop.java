package cn.ljj.musicplayer.player.state;

import android.util.Log;
import cn.ljj.musicplayer.data.MusicInfo;
import cn.ljj.musicplayer.player.Player;

public class StateStop extends AbstractState {

	public StateStop(Player player) {
		super(player);
	}

	@Override
	public boolean processEvent(PlayEvent event) {
		AbstractState nextState = null;
		switch (event.getEventCode()) {
			case PlayEvent.EVENT_PLAY:
				if (mPlayer.play((MusicInfo) event.getObjectValue())) {
					nextState = mPlayer.getStatePlaying();
				}
				break;
			case PlayEvent.EVENT_STOP:
				nextState = this;
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
				if (mPlayer.stop()) {
					nextState = mPlayer.getStateIdel();
				}
				break;
			case PlayEvent.EVENT_SEEK:
				if (mPlayer.seek(event.getIntValue())) {
					nextState = this;
				}
				break;
			default:
				Log.e("StateStop",
						"processEvent: invalid event = " + event.toString());
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
