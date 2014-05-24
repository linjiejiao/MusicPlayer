package cn.ljj.musicplayer.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import cn.ljj.musicplayer.R;
import cn.ljj.musicplayer.data.MusicInfo;
import cn.ljj.musicplayer.database.Logger;
import cn.ljj.musicplayer.database.MusicPlayerDatabase;
import cn.ljj.musicplayer.files.BaiduMusicSearch;
import cn.ljj.musicplayer.files.BaiduMusicSearch.SeachCallback;
import cn.ljj.musicplayer.files.Defines;
import cn.ljj.musicplayer.files.DownloadFactory;
import cn.ljj.musicplayer.files.Downloader.DownloadCallback;
import cn.ljj.musicplayer.player.service.INotify;
import cn.ljj.musicplayer.player.service.NotifyImpl;
import cn.ljj.musicplayer.playlist.PlayList;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class PlayListFragment extends Fragment implements Defines, OnItemClickListener,
			OnMenuItemClickListener, OnClickListener, SeachCallback{
	static final int MENU_DELETE = 0;
	static final int MENU_DOWNLOAD = 1;
	static String TAG = "PlayListFragment";
	View mRootView = null;
	ListView mPlayListView = null;
	LinearLayout mSearchView = null;
	Button mBtnSearch = null;
	EditText mEditSearch = null;
	PlayList mPlaylist = null;
	private INotify mBaseActivityCallBack = null;
	BaiduMusicSearch mSearcher = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_playlist, container, false);
//		mPlayer =  Player.getPlayer();
		mPlaylist = PlayList.getPlayList(getActivity());
		mPlaylist.load("123");
		if(mPlaylist.isEmpty()){
			mPlaylist.loadFromMediaStore();
			mPlaylist.persist("123", true);
		}
		mSearcher = new BaiduMusicSearch();
		mSearcher.setCallBack(this);
		initViews();
		return mRootView;
	}

	private void initViews(){
		mPlayListView = (ListView) mRootView.findViewById(R.id.playlist_view);
		mSearchView = (LinearLayout) mRootView.findViewById(R.id.search_view);
		mBtnSearch = (Button) mRootView.findViewById(R.id.btn_search);
		mEditSearch = (EditText) mRootView.findViewById(R.id.edit_search_keys);
		mBtnSearch.setOnClickListener(this);
		SimpleAdapter listAdapter = prepareListDate(mPlaylist.getMusicList());
		mPlayListView.setAdapter(listAdapter);
		mPlayListView.setOnItemClickListener(this);
		mPlayListView.setOnCreateContextMenuListener(this);
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
		Logger.i(TAG , "onItemClick index="+index);
		MusicInfo music = mPlaylist.get(index);
		mPlaylist.setProgress(-1);
		if(sendCmd(NotifyImpl.CMD_PLAY_EVENT, 0, 0, null, music)){
			mPlaylist.setCurrentIndex(index);
		}
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (menuInfo != null) {
            menu.setHeaderTitle(R.string.str_operate);
            menu.add(0, MENU_DOWNLOAD , 0, R.string.str_download)
            	.setOnMenuItemClickListener(this);
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
		case MENU_DOWNLOAD:
			final MusicInfo music = mPlaylist.get(id);
			String saveName = music.getName() + "." + music.getFormat();
			DownloadFactory.DownloadMusic(music.getMusicPath(), saveName, new DownloadCallback() {
				@Override
				public void onProgressChange(int length, int finished) {
					Logger.e(TAG, "onProgressChange "+finished + "/" + length);
				}
				@Override
				public void onFinished(String filePath) {
					music.setMusicPath(filePath);
					Logger.e(TAG, "onFinished filePath="+filePath);
				}
				@Override
				public void onFaild(int errorCode) {
					Logger.e(TAG, "onFaild errorCode="+errorCode);
				}
			});
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

	private void search(String keys){
		mSearcher.search(keys,10,1);
	}

	@Override
	public void onSearchResult(final List<MusicInfo> resualt) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				SimpleAdapter listAdapter = prepareListDate(resualt);
				mPlayListView.setAdapter(listAdapter);
				mPlaylist.setMusicList(resualt, null);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.btn_search:
					search(mEditSearch.getText().toString());
				break;
			default:
				
		}
	}

	public void setCallback(INotify callback) {
		mBaseActivityCallBack = callback;
	}

	private boolean sendCmd(int cmd, int intValue, long longValue, String str, MusicInfo music){
		boolean ret = false;
		if(mBaseActivityCallBack == null){
			return ret;
		}
		try {
			if(mBaseActivityCallBack.onNotify(cmd, intValue, longValue, str, music) == NotifyImpl.RET_OK){
				ret = true;
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
