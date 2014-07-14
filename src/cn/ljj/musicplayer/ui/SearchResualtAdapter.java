package cn.ljj.musicplayer.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.ljj.musicplayer.R;
import cn.ljj.musicplayer.data.MusicInfo;

public class SearchResualtAdapter extends BaseAdapter {
	public static final String TAG = "SearchResualtAdapter";
	Context mContext = null;
	List<MusicInfo> mListItems = null;
	LayoutInflater mInflater = null;

	public SearchResualtAdapter(Context context, List<MusicInfo> list) {
		mContext = context;
		if (list == null) {
			list = new ArrayList<MusicInfo>();
		}
		mListItems = list;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mListItems.size();
	}

	@Override
	public Object getItem(int position) {
		MusicInfo item = null;
		try {
			item = mListItems.get(position);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView != null) {
			view = convertView;
		} else {
			view = newView(parent);
		}
		bindView(view, position);
		return view;
	}

	private View newView(ViewGroup parent) {
		View v = mInflater.inflate(R.layout.list_item_music, parent, false);
		return v;
	}

	private void bindView(final View view, int position) {
		MusicInfo music = (MusicInfo) getItem(position);
		TextView index = (TextView) view.findViewById(R.id.text_index);
		index.setText((position + 1) + "");
		TextView name = (TextView) view.findViewById(R.id.text_name);
		name.setText(music.getName());
		TextView duration = (TextView) view.findViewById(R.id.text_duration);
		duration.setText(music.getDurationStr());
		TextView artist = (TextView) view.findViewById(R.id.text_artist);
		artist.setText(music.getArtist());
		TextView album = (TextView) view.findViewById(R.id.text_album);
		album.setText(music.getAlbum());
	}
}
