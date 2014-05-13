package cn.ljj.musicplayer.ui;

import cn.ljj.musicplayer.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

public class PlayListFragment extends Fragment {
	View mRootView = null;
	ListView mPlayListView = null;
	LinearLayout mSearchView = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_playlist, container, false);
		mPlayListView = (ListView) mRootView.findViewById(R.id.playlist_view);
		mSearchView = (LinearLayout) mRootView.findViewById(R.id.search_view);
		return mRootView;
	}

	

}
