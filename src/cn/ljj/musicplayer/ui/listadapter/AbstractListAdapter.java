
package cn.ljj.musicplayer.ui.listadapter;

import android.content.Context;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.CursorAdapter;

public abstract class AbstractListAdapter extends CursorAdapter {
    public static final int TYPE_NONE = -1;
    public static final int TYPE_SAVED_LIST = 0;
    public static final int TYPE_PLAYING_LIST = 1;
    public static final int TYPE_SEARCH_RESULT_LIST = 2;

    protected LayoutInflater mInflater = null;

    public AbstractListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static final AbstractListAdapter getAdapter(int type, Context context, Cursor c,
            boolean autoRequery) {
        switch (type) {
            case TYPE_SAVED_LIST:
                return new SavedListAdapter(context, c, autoRequery);
            case TYPE_PLAYING_LIST:
                return new PlayListAdapter(context, c, autoRequery);
            case TYPE_SEARCH_RESULT_LIST:
                return new SearchResualtAdapter(context, c, autoRequery);
            case TYPE_NONE:
            default:
        }
        return null;
    }

    public abstract int getAdapterType();
 
    public abstract void proessOnItemClick(AdapterView<?> adapterView, View view, int index,
            long arg3);

    public abstract void processCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo);
}
