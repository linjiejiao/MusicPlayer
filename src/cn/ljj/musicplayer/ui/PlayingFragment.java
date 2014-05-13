package cn.ljj.musicplayer.ui;

import java.util.ArrayList;
import java.util.List;

import cn.ljj.musicplayer.R;
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
	private ImageView mLrcMask = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_playing, container, false);
		lrc_view = (LrcView)mRootView.findViewById(R.id.lrc_view);
		mSongPic = (ImageView) mRootView.findViewById(R.id.song_pic);
		mLrcMask = (ImageView) mRootView.findViewById(R.id.lrc_mask);
		mSongPic.setOnClickListener(this);
		mLrcMask.setOnClickListener(this);
		return mRootView;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.lrc_mask:
				if(mLrcMask.getVisibility() == View.GONE){
					setLrcs(null);
					mLrcMask.setVisibility(View.VISIBLE);
					lrc_view.setVisibility(View.VISIBLE);
				}else{
					mLrcMask.setVisibility(View.GONE);
					lrc_view.setVisibility(View.GONE);
				}
				break;
			case R.id.lrc_view:
				break;
			case R.id.song_pic:
				if(mLrcMask.getVisibility() == View.GONE){
					setLrcs(null);
					mLrcMask.setVisibility(View.VISIBLE);
					lrc_view.setVisibility(View.VISIBLE);
				}
				break;
		}
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
		lrc_view.initViews(lrcs);
	}

}
