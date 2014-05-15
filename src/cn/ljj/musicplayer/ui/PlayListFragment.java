package cn.ljj.musicplayer.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import cn.ljj.musicplayer.R;
import cn.ljj.musicplayer.data.MusicInfo;
import cn.ljj.musicplayer.database.MusicPlayerDatabase;
import cn.ljj.musicplayer.playlist.PlayList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class PlayListFragment extends Fragment implements OnItemClickListener, OnMenuItemClickListener {
	static final int MENU_DELETE = 0;
	static final int MENU_EDIT = 1;
	View mRootView = null;
	ListView mPlayListView = null;
	LinearLayout mSearchView = null;
	PlayList mPlaylist = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_playlist, container, false);
		mPlayListView = (ListView) mRootView.findViewById(R.id.playlist_view);
		mSearchView = (LinearLayout) mRootView.findViewById(R.id.search_view);
		mPlaylist = PlayList.getPlayList(getActivity());
		mPlaylist.load("123");
		if(mPlaylist.isEmpty()){
			mPlaylist.loadFromMediaStore();
		}
		SimpleAdapter listAdapter = prepareListDate(mPlaylist.getMusicList());
		mPlayListView.setAdapter(listAdapter);
		mPlayListView.setOnItemClickListener(this);
		mPlayListView.setOnCreateContextMenuListener(this);
		return mRootView;
	}

	private SimpleAdapter prepareListDate(List<MusicInfo> data){
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		int id = 1;
		for (MusicInfo music : data) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", id++ + "");
			map.put(MusicPlayerDatabase.NAME, music.getName());
			map.put(MusicPlayerDatabase.DURATION, music.getDurationStr());
			map.put(MusicPlayerDatabase.ARTIST, music.getArtist());
			map.put(MusicPlayerDatabase.ALBUM, music.getAlbum());
			list.add(map);
		}
		String[] keys = new String[]{
			"id",
			MusicPlayerDatabase.NAME,
			MusicPlayerDatabase.DURATION,
			MusicPlayerDatabase.ARTIST,
			MusicPlayerDatabase.ALBUM
		};
		int[] ids = new int[] {
				R.id.text_index,
				R.id.text_name,
				R.id.text_duration,
				R.id.text_artist,
				R.id.text_album };
		return new SimpleAdapter(getActivity(), list, R.layout.list_item_music, keys, ids);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int index, long arg3) {
		
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (menuInfo != null) {
            menu.setHeaderTitle(R.string.str_operate);
//            menu.add(0, MENU_EDIT, 0, R.string.str_edit)
//            	.setOnMenuItemClickListener(this);
            menu.add(0, MENU_DELETE, 1, R.string.str_remove)
            	.setOnMenuItemClickListener(this);
        }
	}

	@Override
	public boolean onMenuItemClick(MenuItem menu) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menu.getMenuInfo(); 
		int id = (int)info.id;
		switch(menu.getItemId()){
		case MENU_DELETE:
			removeFromList(id);
			break;
		}
		return false;
	}
	
	private void removeFromList(int id){
		mPlaylist.remove(id);
		SimpleAdapter listAdapter = prepareListDate(mPlaylist.getMusicList());
		mPlayListView.setAdapter(listAdapter);
		mPlayListView.setSelection(id-1);
	}
}
