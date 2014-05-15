package cn.ljj.musicplayer.player;


public abstract class AbstractState {
	protected Player mPlayer = null;

	public AbstractState(Player player) {
		mPlayer = player;
	}

	public abstract boolean processEvent(PlayEvent event);
}
