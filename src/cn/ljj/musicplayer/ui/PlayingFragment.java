package cn.ljj.musicplayer.ui;

import java.util.ArrayList;
import java.util.List;

import cn.ljj.musicplayer.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
		mRootView = inflater.inflate(R.layout.fragment_playing, container, false);
		lrc_view = (LrcView)mRootView.findViewById(R.id.lrc_view);
		lrc_view.init();
		mSongPic = (ImageView) mRootView.findViewById(R.id.song_pic);
		mSongPic.setOnClickListener(this);
		setLrcs(null);
		return mRootView;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.song_pic:
				if(lrc_view.getVisibility() == View.GONE){
					setLrcs(null);
					lrc_view.setVisibility(View.VISIBLE);
				}else{
					lrc_view.setVisibility(View.GONE);
				}
				break;
		}
		Log.e("onClick", "v="+v.getId());
	}

	private void setLrcs(List<String> lrcs){
		lrcs = new ArrayList<String>();
		lrcs.add("srdoafhlafalksf");
		lrcs.add("sgtdsgsdg");
		lrcs.add("lhjjgffdyadssxb");
		lrcs.add("12tgoafsaghlafalks阿什佛ifhocadsadhoaidha  收到哦啊的啊苏打水哦啊是很f");
		lrcs.add("u2oiwyfhcnalksf");
		lrcs.add("srdoafhlafalksf");
		lrcs.add("sgtd124333333343333333333334444444444sgsdg");
		lrcs.add("lhjjgff333333333333333333333333333333333dyadssxb");
		lrcs.add("12tgoafsaghlafalksf");
		lrcs.add("u2oiwyfhcnalksf");lrcs.add("srdoafhlafalksf");
		lrcs.add("sgtdsgsdg");
		lrcs.add("lhjjgffdyadssxb");
		lrcs.add("g斯蒂芬斯蒂芬速度dhoaidha  收到哦啊的啊苏打水哦啊是很f");
		lrcs.add("u2oiwyfhcnalksf");
		lrcs.add("srdoafhlafalksf");
		lrcs.add("sgtdsgsdg");
		lrcs.add("lhjjgffdyadssxb");
		lrcs.add("12tgoafsaghlafalksf");
		lrcs.add("u2oiwyfhcnalksf");lrcs.add("srdoafhlafalksf");
		lrcs.add("sgtdsgsdg");
		lrcs.add("lhjjgffdyadssxb");
		lrcs.add("润肤打开alks阿什佛ifhocadsadhoaidha  收到哦啊的啊苏打水哦啊是很f");
		lrcs.add("u2oiwyfhcnalksf");
		lrcs.add("srdoafhlafalksf");
		lrcs.add("sgtdsgsdg");
		lrcs.add("lhjjgffdyadssxb");
		lrcs.add("12tgoafsag333333333333333333333333333hlafalksf");
		lrcs.add("u2oiwyfhcnalksf");
		lrc_view.initScrollViews(lrcs);
	}

}
