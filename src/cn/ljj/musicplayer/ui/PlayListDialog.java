package cn.ljj.musicplayer.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.ljj.musicplayer.R;
import cn.ljj.musicplayer.playlist.PlayList;
import cn.ljj.musicplayer.playlist.SavedList;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

public class PlayListDialog extends Dialog implements android.view.View.OnClickListener {
	PlayList mPlayList = null;
	List<SavedList> mSavedList = null;
	ListView mListView = null;
	Button mBtnAdd = null;
	EditText mEditName = null;
	RelativeLayout mWaitMask = null;
	Context mContext = null;

	public PlayListDialog(Context context) {
		super(context);
		 mPlayList = PlayList.getPlayList(context);
		mSavedList = mPlayList.getAllSavedPlayList();
		mContext = context;
		setContentView(R.layout.dialog_playlist);
		setTitle("已保存播放列表");
		
		mListView = (ListView) findViewById(R.id.lists);
		mBtnAdd = (Button) findViewById(R.id.btn_add);
		mEditName = (EditText) findViewById(R.id.edit_playlist_name);
		mWaitMask = (RelativeLayout) findViewById(R.id.wait_saving);
		mBtnAdd.setOnClickListener(this);
		initList();
	}

	private void initList(){
		SimpleAdapter adapter = prepareListDate(mSavedList);
		mListView.setAdapter(adapter);
	}

	private SimpleAdapter prepareListDate(List<SavedList> data){
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		int id = 1;
		String[] keys = new String[]{
			"id",
			"list_name",
			"playing",
		};
		int[] ids = new int[] {
				R.id.list_index,
				R.id.list_name,
				R.id.playing,};
		
		for (SavedList savedList : data) {
			HashMap<String, String> map = new HashMap<String, String>();
			String playing = "";
			if(savedList.getListId() == mPlayList.get().getListId()){
				playing = mContext.getString(R.string.st_playing);
			}
			String dispName = savedList.getListName() +"(" + savedList.getCount() + ")";
			map.put(keys[0], id++ + "");
			map.put(keys[1], dispName);
			map.put(keys[2], playing);
			list.add(map);
		}
		return new SimpleAdapter(mContext, list, R.layout.list_item_playlist, keys, ids);
	}
	
	@Override
	public void onClick(View arg0) {
		if(mEditName.getVisibility() == View.GONE){
			mEditName.setVisibility(View.VISIBLE);
			mWaitMask.setVisibility(View.VISIBLE);
		}else{
			mEditName.setVisibility(View.GONE);
			mWaitMask.setVisibility(View.GONE);
		}
	}

}
