package cn.ljj.musicplayer.player;

import cn.ljj.musicplayer.data.MusicInfo;
import cn.ljj.musicplayer.database.Logger;


public class StateIdel extends AbstractState {

	public StateIdel(Player player) {
		super(player);
	}

	@Override
	public boolean processEvent(PlayEvent event) {
		AbstractState nextState = null;
		boolean reset = false;
		MusicInfo music = event.getMusic();
		if(event.getIntValue() == -1){
			reset = true;
		}
		switch (event.getEventCode()) {
			case PlayEvent.EVENT_PLAY:
				if (mPlayer.play(music, reset)) {
					nextState = mPlayer.getStatePlaying();
				}
				break;
			case PlayEvent.EVENT_STOP:
				nextState = mPlayer.getStateStop();
				break;
//			case PlayEvent.EVENT_PREV:
//				if (mPlayer.play(music, reset)) {
//					nextState = mPlayer.getStatePlaying();
//				}
//				break;
//			case PlayEvent.EVENT_NEXT:
//				if (mPlayer.play(music, reset)) {
//					nextState = mPlayer.getStatePlaying();
//				}
//				break;
			case PlayEvent.EVENT_RELOADLIST:
				nextState = this;
				break;
			case PlayEvent.EVENT_SEEK:
				if (mPlayer.seek(event.getIntValue())) {
					nextState = mPlayer.getStatePlaying();
				}
				break;
			default:
				Logger.e("StateIdel",
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
