package cn.ljj.musicplayer.ui;

import cn.ljj.musicplayer.R;
import cn.ljj.musicplayer.data.MusicInfo;
import cn.ljj.musicplayer.database.Logger;
import cn.ljj.musicplayer.database.MusicPlayerDatabase;
import cn.ljj.musicplayer.database.MusicProvider;
import cn.ljj.musicplayer.files.BaiduMusicSearch;
import cn.ljj.musicplayer.files.Defines;
import cn.ljj.musicplayer.files.DownloadFactory;
import cn.ljj.musicplayer.files.Downloader.DownloadCallback;
import cn.ljj.musicplayer.player.service.INotify;
import cn.ljj.musicplayer.player.service.NotifyImpl;
import cn.ljj.musicplayer.playlist.PlayList;
import cn.ljj.musicplayer.ui.listadapter.AbstractListAdapter;
import cn.ljj.musicplayer.ui.listadapter.PlayListAdapter;
import cn.ljj.musicplayer.ui.listadapter.SavedListAdapter;
import cn.ljj.musicplayer.ui.listadapter.SearchResualtAdapter;
import android.annotation.SuppressLint;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

@SuppressLint("ValidFragment")
public class PlayListFragment extends BaseFragment implements Defines,
		OnItemClickListener, OnMenuItemClickListener {
	static final int REQ_CODE_GET_MUSIC = 10;
	static final int MENU_DELETE = 0;
	static final int MENU_DOWNLOAD = 1;
	static final int MENU_ADD_MUSIC = 2;
	static final int MASK_LOADING = 101;
	static String TAG = "PlayListFragment";
	private View mRootView = null;
	private ListView mListView = null;
	private View mFooterView = null;
	private LinearLayout mSearchView = null;
	private LinearLayout mMaskLoading = null;
	private Button mBtnSearch = null;
	private EditText mEditSearch = null;
	private PlayList mPlaylist = null;
	private AbstractListAdapter mListAdapter = null;
	private SavedListAdapter mSavedListAdapter = null;
	private PlayListAdapter mPlayListAdapter = null;
	private SearchResualtAdapter mSearchResualtAdapter = null;
	private INotify mBaseActivityCallBack = null;
    private Context mContext = null;
    private BackgroundHandler mQueryHandler = null;

	public PlayListFragment(Context context){
	    mContext = context;
	    mQueryHandler = new BackgroundHandler(context.getContentResolver());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_playlist, container,
				false);
		mPlaylist = PlayList.getPlayList(getActivity());
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

	private void initViews() {
		mListView = (ListView) mRootView.findViewById(R.id.playlist_view);
		mSearchView = (LinearLayout) mRootView.findViewById(R.id.search_view);
		mMaskLoading = (LinearLayout) mRootView.findViewById(R.id.mask_loading);
		mBtnSearch = (Button) mRootView.findViewById(R.id.btn_search);
		mEditSearch = (EditText) mRootView.findViewById(R.id.edit_search_keys);
		mBtnSearch.setOnClickListener(this);
		mSavedListAdapter = new SavedListAdapter(mContext, null, true);
		mPlayListAdapter = new PlayListAdapter(mContext, null, true);
		mSearchResualtAdapter = new SearchResualtAdapter(mContext, null, true);
		mListAdapter = mSavedListAdapter;
		mListView.setAdapter(mListAdapter);
		mQueryHandler.startQuery( BackgroundHandler.TOKEN_QUERY_SAVED_LIST, null,
		        MusicProvider.URI_LIST, null, null, null, null);
		mListView.setOnItemClickListener(this);
		mListView.setOnCreateContextMenuListener(this);
		mFooterView= getActivity().getLayoutInflater().inflate(R.layout.layout_buttom_add_item,
				mListView, false);
        Button add = (Button) mFooterView.findViewById(R.id.btn_add_to_list);
        add.setOnClickListener(this);
	}

	private void showMask(int m, boolean show){
		switch(m){
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
		Cursor cursor = (Cursor)mListAdapter.getItem(index);
		switch(mListAdapter.getAdapterType()){
		    case AbstractListAdapter.TYPE_SAVED_LIST:
		        long listId = cursor.getLong(cursor.getColumnIndexOrThrow(MusicPlayerDatabase._ID));
		        mPlaylist.setListId(listId);
                Uri uri = MusicProvider.URI_MUSIC;
                mQueryHandler.startQuery( BackgroundHandler.TOKEN_QUERY_PLAY_LIST, null,
                        uri, null, MusicPlayerDatabase.LIST_ID + "=" + listId, null, MusicPlayerDatabase.NAME);
		        break;
            case AbstractListAdapter.TYPE_PLAYING_LIST:
                MusicInfo music2 = new MusicInfo(cursor);
                mPlaylist.setProgress(-1);
                if (sendCmd(NotifyImpl.CMD_PLAY_EVENT, 0, 0, null, music2)) {
                }
                break;
            case AbstractListAdapter.TYPE_SEARCH_RESULT_LIST:
                MusicInfo music = new MusicInfo(cursor);
                mPlaylist.setProgress(-1);
                if (sendCmd(NotifyImpl.CMD_PLAY_EVENT, 0, 0, null, music)) {
                }
                break;
            default:
                Logger.e(TAG, "onItemClick Error Adapter Type=" + mListAdapter.getAdapterType());
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (menuInfo != null) {
			menu.setHeaderTitle(R.string.str_operate);
	        switch(mListAdapter.getAdapterType()){
	            case  AbstractListAdapter.TYPE_SAVED_LIST:
	                menu.add(0, MENU_ADD_MUSIC, 0, R.string.str_add_music)
	                .setOnMenuItemClickListener(this);
	                menu.add(0, MENU_DELETE, 1, R.string.str_remove)
	                .setOnMenuItemClickListener(this);
	                break;
	            case  AbstractListAdapter.TYPE_PLAYING_LIST:
	                menu.add(0, MENU_DELETE, 1, R.string.str_remove)
	                .setOnMenuItemClickListener(this);
	                break;
	            case  AbstractListAdapter.TYPE_SEARCH_RESULT_LIST:
	                menu.add(0, MENU_DOWNLOAD, 0, R.string.str_download)
                    .setOnMenuItemClickListener(this);
	                break;
	            default :
	                Logger.e(TAG, "onCreateContextMenu Error Adapter Type=" + mListAdapter.getAdapterType());
	        }
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem menu) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menu
				.getMenuInfo();
		int id = (int) info.id;
        Cursor cursor = (Cursor) mListAdapter.getItem(id);
		Logger.e(TAG, "onMenuItemClick id=" + id);
		switch (menu.getItemId()) {
		case MENU_DELETE:
			removeFromList(id);
			break;
		case MENU_DOWNLOAD:
			final MusicInfo music = new MusicInfo(cursor);//mSearchResualtList.get(id);
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
            Uri uri = MusicProvider.URI_LIST_MUSIC.buildUpon()
                    .appendPath(String.valueOf(id)).build();
            mQueryHandler.startQuery( BackgroundHandler.TOKEN_QUERY_PLAY_LIST, null,
                    uri, null, null, null, null);
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
                try {
                    if(data != null && data.getData() != null){
                    	Uri uri = data.getData();
                    	MusicInfo music = getMusicInfo(uri);
                    	mPlaylist.add(music);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void removeFromList(int id) {
        switch(mListAdapter.getAdapterType()){
            case AbstractListAdapter.TYPE_SAVED_LIST:
                mPlaylist.deletePlayList(id);
                break;
            case AbstractListAdapter.TYPE_PLAYING_LIST:
                mPlaylist.remove(id);
                break;
            default:
                Logger.e(TAG, "removeFromList Error Adapter Type=" + mListAdapter.getAdapterType());
        }
	}

	private void search(String keys) {
    	mListAdapter.changeCursor(null);
		Uri uri = MusicProvider.URI_INTERNET.buildUpon()
				.appendQueryParameter(BaiduMusicSearch.KEY_QUERY, keys)
				.appendQueryParameter(BaiduMusicSearch.KEY_PAGE_SIZE, "10")
				.appendQueryParameter(BaiduMusicSearch.KEY_PAGE_NO, "1")
				.build();
		mQueryHandler.startQuery(BackgroundHandler.TOKEN_QUERY_SEARCH_RESULT_LIST,
				null, uri, null, null, null, null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_search:
			search(mEditSearch.getText().toString());
			break;
		case R.id.btn_add_to_list:
	        switch(mListAdapter.getAdapterType()){
	            case AbstractListAdapter.TYPE_SAVED_LIST:
	                DialogAddList dal = new DialogAddList(getActivity());
	                dal.show();
	                break;
                case AbstractListAdapter.TYPE_PLAYING_LIST:
                    selectMusic();
                    break;
	        }
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
	    switch(mListAdapter.getAdapterType()){
	        case AbstractListAdapter.TYPE_SAVED_LIST:
	            return false;
            case AbstractListAdapter.TYPE_SEARCH_RESULT_LIST:
                showMask(MASK_LOADING, false);
                mQueryHandler.cancelOperation(BackgroundHandler.TOKEN_QUERY_SEARCH_RESULT_LIST);
                mListAdapter.changeCursor(null);
                mQueryHandler.startQuery( BackgroundHandler.TOKEN_QUERY_SAVED_LIST, null,
                        MusicProvider.URI_LIST, null, null, null, null);
                return true;
            case AbstractListAdapter.TYPE_PLAYING_LIST:
                mListAdapter.changeCursor(null);
                mQueryHandler.startQuery( BackgroundHandler.TOKEN_QUERY_SAVED_LIST, null,
                        MusicProvider.URI_LIST, null, null, null, null);
                return true;
	    }
        return false;
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
	
	class BackgroundHandler extends AsyncQueryHandler {
	    static final int TOKEN_QUERY_SAVED_LIST = 0;
        static final int TOKEN_QUERY_PLAY_LIST = 1;
        static final int TOKEN_QUERY_SEARCH_RESULT_LIST = 2;
        public BackgroundHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
		public void startQuery(int token, Object cookie, Uri uri,
				String[] projection, String selection, String[] selectionArgs,
				String orderBy) {
    		mListView.removeFooterView(mFooterView);
            showMask(MASK_LOADING, true);
			super.startQuery(token, cookie, uri, projection, selection, selectionArgs,
					orderBy);
		}

		@Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            switch(token){
                case TOKEN_QUERY_SAVED_LIST:
                    mSavedListAdapter.changeCursor(cursor);
                    mListAdapter = mSavedListAdapter;
                    mListView.setAdapter(mListAdapter);
            		mListView.addFooterView(mFooterView);
            		if(cursor.getCount() == 0){
                        mPlaylist.loadFromMediaStore();
                        mPlaylist.persist("MediaStore", true);
            		}
                    break;
                case TOKEN_QUERY_PLAY_LIST:
                    mPlayListAdapter.changeCursor(cursor);
                    mListAdapter = mPlayListAdapter;
                    mListView.setAdapter(mListAdapter);
            		mListView.addFooterView(mFooterView);
            		mPlaylist.loadFromCursor(cursor, -1);
                    break;
                case TOKEN_QUERY_SEARCH_RESULT_LIST:
                	mSearchResualtAdapter.changeCursor(cursor);
                	mListAdapter = mSearchResualtAdapter;
                    mListView.setAdapter(mListAdapter);
                    break;
            }
            showMask(MASK_LOADING, false);
            super.onQueryComplete(token, cookie, cursor);
        }
	    
    };
}
