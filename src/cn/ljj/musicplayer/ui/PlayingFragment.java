package cn.ljj.musicplayer.ui;

import java.util.ArrayList;
import java.util.List;

import cn.ljj.musicplayer.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PlayingFragment extends Fragment {
	View mRootView = null;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_playing, container, false);
		final LrcView lrc_view = (LrcView)mRootView.findViewById(R.id.lrc_view);
		lrc_view.invalidate();
		List<String> lrcs = new ArrayList<String>();
		lrcs.add("srdoafhlafalksf");
		lrcs.add("sgtdsgsdg");
		lrcs.add("lhjjgffdyadssxb");
		lrcs.add("12tgoafsaghlafalks��ʲ��ifhocadsadhoaidha  �յ�Ŷ���İ��մ�ˮŶ���Ǻ�f");
		lrcs.add("u2oiwyfhcnalksf");
		lrcs.add("srdoafhlafalksf");
		lrcs.add("sgtdsgsdg");
		lrcs.add("lhjjgffdyadssxb");
		lrcs.add("12tgoafsaghlafalksf");
		lrcs.add("u2oiwyfhcnalksf");lrcs.add("srdoafhlafalksf");
		lrcs.add("sgtdsgsdg");
		lrcs.add("lhjjgffdyadssxb");
		lrcs.add("g˹�ٷ�˹�ٷ��ٶ�dhoaidha  �յ�Ŷ���İ��մ�ˮŶ���Ǻ�f");
		lrcs.add("u2oiwyfhcnalksf");
		lrcs.add("srdoafhlafalksf");
		lrcs.add("sgtdsgsdg");
		lrcs.add("lhjjgffdyadssxb");
		lrcs.add("12tgoafsaghlafalksf");
		lrcs.add("u2oiwyfhcnalksf");lrcs.add("srdoafhlafalksf");
		lrcs.add("sgtdsgsdg");
		lrcs.add("lhjjgffdyadssxb");
		lrcs.add("�����alks��ʲ��ifhocadsadhoaidha  �յ�Ŷ���İ��մ�ˮŶ���Ǻ�f");
		lrcs.add("u2oiwyfhcnalksf");
		lrcs.add("srdoafhlafalksf");
		lrcs.add("sgtdsgsdg");
		lrcs.add("lhjjgffdyadssxb");
		lrcs.add("12tgoafsaghlafalksf");
		lrcs.add("u2oiwyfhcnalksf");
		lrc_view.initViews(lrcs);
		new Thread(){
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(1000);
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								lrc_view.highlight((int) ((System.currentTimeMillis()/1000)%30));
							}
						});
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
				}
			}
			
		}.start();
		return mRootView;
	}

	

}
