package cn.ljj.musicplayer.ui;

import java.util.ArrayList;
import java.util.List;
import cn.ljj.musicplayer.R;
import cn.ljj.musicplayer.data.MusicInfo;
import cn.ljj.musicplayer.database.Logger;
import cn.ljj.musicplayer.database.MusicProvider;
import cn.ljj.musicplayer.files.BaiduMusicSearch;
import cn.ljj.musicplayer.files.BaiduMusicSearch.SeachCallback;
import cn.ljj.musicplayer.files.Defines;
import cn.ljj.musicplayer.files.DownloadFactory;
import cn.ljj.musicplayer.files.Downloader.DownloadCallback;
import cn.ljj.musicplayer.player.service.INotify;
import cn.ljj.musicplayer.player.service.NotifyImpl;
import cn.ljj.musicplayer.playlist.PlayList;
import cn.ljj.musicplayer.playlist.SavedList;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PlayListFragment extends BaseFragment implements Defines,
		OnItemClickListener, OnMenuItemClickListener, SeachCallback {
	static final int REQ_CODE_GET_MUSIC = 10;
	static final int MENU_DELETE = 0;
	static final int MENU_DOWNLOAD = 1;
	static final int MENU_ADD_MUSIC = 2;
	static final int MASK_EMPTY_LIST = 100;
	static final int MASK_LOADING = 101;
	static String TAG = "PlayListFragment";
	private View mRootView = null;
	private ListView mListView = null;
	private LinearLayout mSearchView = null;
	private LinearLayout mMaskLoading = null;
	private LinearLayout mMaskEmptyList = null;
	private Button mBtnSearch = null;
	private EditText mEditSearch = null;
	private PlayList mPlaylist = null;
	private List<SavedList> mAllList = new ArrayList<SavedList>();
	private List<MusicInfo> mMusicList = new ArrayList<MusicInfo>();
	private List<MusicInfo> mSearchResualtList = new ArrayList<MusicInfo>();
	private BaseAdapter mListAdapter = null;
	private SavedListAdapter mSavedListAdapter = null;
	private PlayListAdapter mPlayListAdapter = null;
	private SearchResualtAdapter mSearchResualtAdapter = null;
	private INotify mBaseActivityCallBack = null;
	private BaiduMusicSearch mSearcher = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_playlist, container,
				false);
		mPlaylist = PlayList.getPlayList(getActivity());
		mPlaylist.load("MediaStore");
		if (mPlaylist.isEmpty()) {
			mPlaylist.loadFromMediaStore();
			mPlaylist.persist("MediaStore", true);
		}
		mSearcher = new BaiduMusicSearch();
		mSearcher.setCallBack(this);
		try {
			initViews();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mRootView;
	}

	public ListView getListView() {
		return mListView;
	}

	public LinearLayout getSearchView() {
		return mSearchView;
	}

	private BaseAdapter getSavedListAdapter(boolean update) {
//		if (update || mSavedListAdapter == null) {
//			mAllList.clear();
//			mAllList.addAll(mPlaylist.getAllSavedPlayList());
//			mSavedListAdapter = new SavedListAdapter(getActivity(), mAllList);
//			mSavedListAdapter.setAddButtonListener(this);
//		}
	    if(mSavedListAdapter == null){
	        Uri uri = Uri.parse(MusicProvider.URI_BASE)
	                .buildUpon()
	                .appendPath(MusicProvider.URI_ALL_LIST)
	                .build();
	        Cursor c = getActivity().getContentResolver().query(uri, null, null, null, null);
	        mListAdapter = new SavedListAdapter2(getActivity(), c, true);//getSavedListAdapter(true);
	    }
		return mListAdapter;
	}

	private PlayListAdapter getPlayListAdapter(boolean update) {
		if (update || mPlayListAdapter == null) {
			mMusicList.clear();
			mMusicList.addAll(mPlaylist.getMusicList());
			mPlayListAdapter = new PlayListAdapter(getActivity(), mMusicList);
		}
		return mPlayListAdapter;
	}

	private SearchResualtAdapter getSearchResualtAdapter(boolean update) {
		if (update || mSearchResualtAdapter == null) {
			mSearchResualtAdapter = new SearchResualtAdapter(getActivity(), mSearchResualtList);
		}
		return mSearchResualtAdapter;
	}

	private void initViews() {
		mListView = (ListView) mRootView.findViewById(R.id.playlist_view);
		mSearchView = (LinearLayout) mRootView.findViewById(R.id.search_view);
		mMaskLoading = (LinearLayout) mRootView.findViewById(R.id.mask_loading);
		mMaskEmptyList = (LinearLayout) mRootView.findViewById(R.id.mask_empty_list);
		Button btnAdd = (Button) mRootView.findViewById(R.id.btn_add_music);
		btnAdd.setOnClickListener(this);
		mBtnSearch = (Button) mRootView.findViewById(R.id.btn_search);
		mEditSearch = (EditText) mRootView.findViewById(R.id.edit_search_keys);
		mBtnSearch.setOnClickListener(this);
		mListAdapter = getSavedListAdapter(true);
		mListView.setAdapter(mListAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setOnCreateContextMenuListener(this);
		View footerView = getActivity().getLayoutInflater().inflate(R.layout.layout_buttom_add_item,
                null, false);
        Button add = (Button) footerView.findViewById(R.id.btn_add_list);
        add.setOnClickListener(this);
		mListView.addFooterView(footerView);
	}

	private void showMask(int m, boolean show){
		switch(m){
		case MASK_EMPTY_LIST:
			if(show){
				mMaskEmptyList.setVisibility(View.VISIBLE);
			}else{
				mMaskEmptyList.setVisibility(View.GONE);
			}
			break;
		case MASK_LOADING:
			if(show){
				mMaskLoading.setVisibility(View.VISIBLE);
			}else{
				mMaskLoading.setVisibility(View.GONE);
			}
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int index,
			long arg3) {
		Logger.i(TAG, "onItemClick index=" + index);
		if (adapterView.getAdapter() instanceof SavedListAdapter2) {
			SavedList list = mAllList.get(index);
			mPlaylist.load(list.getListName());
			mListAdapter = getPlayListAdapter(true);
			getListView().setAdapter(mListAdapter);
			showMask(MASK_EMPTY_LIST, mPlaylist.isEmpty());
		} else if (adapterView.getAdapter() instanceof SearchResualtAdapter) {
			MusicInfo music = mSearchResualtList.get(index);
			mPlaylist.setProgress(-1);
			if (sendCmd(NotifyImpl.CMD_PLAY_EVENT, 0, 0, null, music)) {
			}
		} else if (adapterView.getAdapter() instanceof PlayListAdapter) {
			MusicInfo music = mPlaylist.get(index);
			mPlaylist.setProgress(-1);
			mPlaylist.setCurrentIndex(index);
			if (sendCmd(NotifyImpl.CMD_PLAY_EVENT, 0, 0, null, music)) {
			}
		}else {
			Logger.e(TAG, "Error Adapter Type");
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (menuInfo != null) {
			// jiajian
			menu.setHeaderTitle(R.string.str_operate);
			if (mListAdapter instanceof SavedListAdapter2) {
				menu.add(0, MENU_ADD_MUSIC, 0, R.string.str_add_music)
				.setOnMenuItemClickListener(this);
				menu.add(0, MENU_DELETE, 1, R.string.str_remove)
				.setOnMenuItemClickListener(this);
			}else if (mListAdapter instanceof SearchResualtAdapter) {
				menu.add(0, MENU_DOWNLOAD, 0, R.string.str_download)
						.setOnMenuItemClickListener(this);
			}else{
				menu.add(0, MENU_DELETE, 1, R.string.str_remove)
				.setOnMenuItemClickListener(this);
			}
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem menu) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menu
				.getMenuInfo();
		int id = (int) info.id;
		Logger.e(TAG, "onMenuItemClick id=" + id);
		switch (menu.getItemId()) {
		case MENU_DELETE:
			removeFromList(id);
			break;
		case MENU_DOWNLOAD:
			final MusicInfo music = mSearchResualtList.get(id);
			String saveName = music.getName() + "." + music.getFormat();
			DownloadFactory.DownloadMusic(music.getMusicPath(), saveName,
					new DownloadCallback() {
						@Override
						public void onProgressChange(int length, int finished) {
							Logger.e(TAG, "onProgressChange " + finished/1024 + "K/"
									+ length/1024 + "K");
						}

						@Override
						public void onFinished(String filePath) {
							music.setMusicPath(filePath);
							Logger.e(TAG, "onFinished filePath=" + filePath);
						}

						@Override
						public void onFaild(int errorCode) {
							Logger.e(TAG, "onFaild errorCode=" + errorCode);
						}
					});
			break;
		case MENU_ADD_MUSIC:
			mPlaylist.load(mAllList.get(id).getListName());
			selectMusic();
			break;
		}
		return false;
	}

	private void selectMusic(){
		Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
		innerIntent.setType("audio/*");
		Intent wrapperIntent = Intent.createChooser(innerIntent, null);
		startActivityForResult(wrapperIntent, REQ_CODE_GET_MUSIC);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
		case REQ_CODE_GET_MUSIC:
			if(data != null && data.getData() != null){
				showMask(MASK_EMPTY_LIST, false);
				Uri uri = data.getData();
				MusicInfo music = getMusicInfo(uri);
				mPlaylist.add(music);
				mListAdapter = getPlayListAdapter(true);
				getListView().setAdapter(mListAdapter);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void removeFromList(int id) {
		if (mListAdapter instanceof SavedListAdapter2) {
			mPlaylist.deletePlayList(mAllList.get(id).getListName());
			mAllList.remove(id);
			getSavedListAdapter(false).notifyDataSetChanged();
		} else if (mListAdapter instanceof PlayListAdapter) {
			mPlaylist.remove(id);
			mMusicList.remove(id);
			getPlayListAdapter(false).notifyDataSetChanged();
		}
	}

	private void search(String keys) {
		mSearcher.search(keys, 10, 1);
		mSearchResualtList.clear();
		mListAdapter = getSearchResualtAdapter(true);
		getListView().setAdapter(mListAdapter);
		showMask(MASK_LOADING, true);
	}

	@Override
	public void onSearchResult(final List<MusicInfo> resualt) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showMask(MASK_LOADING, false);
				if(resualt == null){
					Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_LONG).show();
					return;
				}
				mSearchResualtList.addAll(resualt);
				mListAdapter = getSearchResualtAdapter(true);
				getListView().setAdapter(mListAdapter);
				mPlaylist.setMusicList(mSearchResualtList, null);
			}
		});
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_search:
			search(mEditSearch.getText().toString());
			break;
		case R.id.btn_add_music:
			selectMusic();
			break;
		case R.id.btn_add_list:
			DialogAddList dal = new DialogAddList(getActivity(),
					new ListChangeListner() {
						@Override
						public void onListChange() {
							mListAdapter = getSavedListAdapter(true);
							getListView().setAdapter(mListAdapter);
						}
					});
			dal.show();
			break;
		default:

		}
	}

	public void setCallback(INotify callback) {
		mBaseActivityCallBack = callback;
	}

	private boolean sendCmd(int cmd, int intValue, long longValue, String str,
			MusicInfo music) {
		boolean ret = false;
		if (mBaseActivityCallBack == null) {
			return ret;
		}
		try {
			if (mBaseActivityCallBack.onNotify(cmd, intValue, longValue, str,
					music) == NotifyImpl.RET_OK) {
				ret = true;
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public boolean pressBack() {
		if (mListAdapter instanceof SavedListAdapter2) {
			return false;
		} else {
			if (mListAdapter instanceof SearchResualtAdapter){
				showMask(MASK_LOADING, false);
				mSearcher.cancel();
			}
			mListAdapter = getSavedListAdapter(true);
			getListView().setAdapter(mListAdapter);
			showMask(MASK_EMPTY_LIST, mAllList.isEmpty());
			return true;
		}
	}

	interface ListChangeListner{
		public void onListChange();
	}

	private MusicInfo getMusicInfo(Uri uri) {
		MusicInfo music = null;
		if (uri.toString().startsWith("content://media/")) {
			Cursor c = null;
			try {
				c = getActivity().getContentResolver().query(uri, null, null,
						null, null);
				if (c.moveToFirst()) {
					int isMusic = c
							.getInt(c
									.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC));
					if (isMusic == 1) {
						String name = c
								.getString(c
										.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
						String path = c
								.getString(c
										.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
						String duration = c
								.getString(c
										.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
						String artist = c
								.getString(c
										.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
						String album = c
								.getString(c
										.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
						int durationInt = Integer.parseInt(duration);
						String format = "";
						if (name.contains(".")) {
							String temp = name;
							name = temp.substring(0, temp.lastIndexOf("."));
							format = temp.substring(temp.lastIndexOf(".") + 1);
						}
						music = new MusicInfo(name, path);
						music.setDuration(durationInt);
						music.setAlbum(album);
						music.setArtist(artist);
						music.setFormat(format);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (c != null) {
					c.close();
				}
			}
		} else {
			String path = uri.getPath();
			String name = path.substring(path.lastIndexOf("/")+1,path.lastIndexOf("."));
			music = new MusicInfo(name, path);
			Logger.e(TAG, "onActivityResult path=" + path);
		}

		return music;
	}
}
