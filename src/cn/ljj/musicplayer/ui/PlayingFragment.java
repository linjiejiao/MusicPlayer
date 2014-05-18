package cn.ljj.musicplayer.ui;

import java.util.ArrayList;
import java.util.List;
import cn.ljj.musicplayer.R;
import cn.ljj.musicplayer.database.Logger;
import cn.ljj.musicplayer.ui.lrc.LrcParser;
import cn.ljj.musicplayer.ui.lrc.LrcParser.LyricLine;
import cn.ljj.musicplayer.ui.lrc.LrcView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PlayingFragment extends Fragment implements OnClickListener {
	View mRootView = null;
	public LrcView lrc_view = null;
	private ImageView mSongPic = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_playing, container,
				false);
		lrc_view = (LrcView) mRootView.findViewById(R.id.lrc_view);
		lrc_view.init();
		mSongPic = (ImageView) mRootView.findViewById(R.id.song_pic);
		mSongPic.setOnClickListener(this);
		setLrcs();
		return mRootView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.song_pic:
			if (lrc_view.getVisibility() == View.GONE) {
				setLrcs();
				lrc_view.setVisibility(View.VISIBLE);
			} else {
				lrc_view.setVisibility(View.GONE);
			}
			break;
		}
		Logger.e("onClick", "v=" + v.getId());
	}

	private void setLrcs() {
		List<String> lrcs = new ArrayList<String>();
		LrcParser parser = new LrcParser();
		parser.parser("/mnt/internal/MusicPlayer/lrc/王力宏,章子怡 - 爱一点.lrc");
		List <LyricLine> list = parser.getLrcList();
		for(LyricLine lyric : list){
			lrcs.add(lyric.getLyric());
		}
		lrc_view.initScrollViews(lrcs);
	}

	public void onProgressChange(int  progress, int duration){
		lrc_view.updateProgress(progress, duration);
	}
}
