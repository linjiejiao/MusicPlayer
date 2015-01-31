package cn.ljj.musicplayer.ui.listadapter;

import cn.ljj.musicplayer.R;
import cn.ljj.musicplayer.data.MusicInfo;
import android.content.Context;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

public class SearchResualtAdapter extends AbstractListAdapter {
	public static final String TAG = "SearchResualtAdapter";

	public SearchResualtAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
	}

	@Override
	public void proessOnItemClick(AdapterView<?> adapterView, View view,
			int index, long arg3) {

	}

	@Override
	public void processCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View v = mInflater.inflate(R.layout.list_item_music, parent, false);
		return v;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		MusicInfo music = new MusicInfo(cursor);
		TextView index = (TextView) view.findViewById(R.id.text_index);
		index.setText((cursor.getPosition() + 1) + "");
		TextView name = (TextView) view.findViewById(R.id.text_name);
		name.setText(music.getName());
		TextView duration = (TextView) view.findViewById(R.id.text_duration);
		duration.setText(music.getDurationStr());
		TextView artist = (TextView) view.findViewById(R.id.text_artist);
		artist.setText(music.getArtist());
		TextView album = (TextView) view.findViewById(R.id.text_album);
		album.setText(music.getAlbum());
	}

	@Override
	public int getAdapterType() {
		return TYPE_SEARCH_RESULT_LIST;
	}

}
