package cn.ljj.musicplayer.ui.listadapter;

import android.content.Context;
import android.database.Cursor;

public class SearchResualtAdapter extends PlayListAdapter {

	public SearchResualtAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
	}

    @Override
    public int getAdapterType() {
        return TYPE_SEARCH_RESULT_LIST;
    }
}
