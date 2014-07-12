package cn.ljj.musicplayer.ui;

import cn.ljj.musicplayer.R;
import cn.ljj.musicplayer.playlist.PlayList;
import cn.ljj.musicplayer.ui.PlayListFragment.ListChangeListner;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class DialogAddList extends Dialog implements android.view.View.OnClickListener {
	EditText mEditName = null;
	Button mBtnAdd = null;
	PlayList mPlaylist = null;
	ListChangeListner listner = null;
	public DialogAddList(Context context, ListChangeListner l) {
		super(context);
		listner = l;
		setTitle(R.string.str_add_playlist);
		setContentView(R.layout.diaog_add_list);
		mPlaylist = PlayList.getPlayList(context);
		initViews();
	}

	private void initViews(){
		mEditName = (EditText) findViewById(R.id.edit_list_name);
		mBtnAdd = (Button) findViewById(R.id.btn_add);
		mBtnAdd.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		try {
			switch (view.getId()) {
			case R.id.btn_add:
				if (TextUtils.isEmpty(mEditName.getText().toString())) {

				} else {
					String listName = mEditName.getText().toString();
					mPlaylist.removeAll();
					mPlaylist.persist(listName, false);

					if (listner != null) {
						listner.onListChange();
						dismiss();
					}

				}
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
