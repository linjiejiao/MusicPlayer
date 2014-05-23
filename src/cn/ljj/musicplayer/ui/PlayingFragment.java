package cn.ljj.musicplayer.ui;

import java.util.List;
import cn.ljj.musicplayer.R;
import cn.ljj.musicplayer.ui.lrc.LrcParser;
import cn.ljj.musicplayer.ui.lrc.LyricLine;
import cn.ljj.musicplayer.ui.lrc.LrcView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	public static String TAG = "PlayingFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_playing, container,
				false);
		lrc_view = (LrcView) mRootView.findViewById(R.id.lrc_view);
		lrc_view.init();
		mSongPic = (ImageView) mRootView.findViewById(R.id.song_pic);
		mSongPic.setOnClickListener(this);
		return mRootView;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.song_pic:
			if (lrc_view.getVisibility() == View.GONE) {
				lrc_view.setVisibility(View.VISIBLE);
			} else {
				lrc_view.setVisibility(View.GONE);
			}
			break;
		}
	}

	public boolean setLrc(String lrcPath) {
		LrcParser parser = new LrcParser();
		if(parser.parser(lrcPath) == -1){
			return false;
		}
		List <LyricLine> list = parser.getLrcList();
		lrc_view.initScrollViews(list);
		return true;
	}

	public void setImage(String picPath) {
		try {
			Bitmap bmp = BitmapFactory.decodeFile(picPath);
			mSongPic.setImageBitmap(bmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onProgressChange(int  progress, int duration){
		lrc_view.updateProgress(progress, duration);
//		Logger.e(TAG , "onProgressChange progress=" + progress + "; duration=" + duration);
	}
}
