package cn.ljj.musicplayer.player;

import android.util.Log;
import cn.ljj.musicplayer.data.MusicInfo;

public class StatePlaying extends AbstractState {

	public StatePlaying(Player player) {
		super(player);
	}

	@Override
	public boolean processEvent(PlayEvent event) {
		AbstractState nextState = null;
		switch (event.getEventCode()) {
			case PlayEvent.EVENT_PLAY:
				if (mPlayer.play((MusicInfo) event.getObjectValue())) {
					nextState = this;
				}
				nextState = this;
				break;
			case PlayEvent.EVENT_STOP:
				if (mPlayer.stop()) {
					nextState = mPlayer.getStateStop();
				}
				break;
			case PlayEvent.EVENT_PREV:
				if (mPlayer.play((MusicInfo) event.getObjectValue())) {
					nextState = this;
				}
				break;
			case PlayEvent.EVENT_NEXT:
				if (mPlayer.play((MusicInfo) event.getObjectValue())) {
					nextState = this;
				}
				break;
			case PlayEvent.EVENT_RELOADLIST:
				nextState = mPlayer.getStateIdel();
				break;
			case PlayEvent.EVENT_SEEK:
				if (mPlayer.seek(event.getIntValue())) {
					nextState = this;
				}
				break;
			default:
				Log.e("StatePlaying",
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
