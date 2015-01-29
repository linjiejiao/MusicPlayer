
package cn.ljj.musicplayer.ui;

import cn.ljj.musicplayer.R;
import cn.ljj.musicplayer.database.MusicPlayerDatabase;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class SavedListAdapter2 extends CursorAdapter {
    private LayoutInflater mInflater = null;
    public final int COLUMN_INDEX_NAME;
    public final int COLUMN_INDEX_SIZE;
    public final int COLUMN_INDEX_ID;
    public static final  String TAG = "SavedListAdapter2";

    public SavedListAdapter2(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        COLUMN_INDEX_NAME = c.getColumnIndexOrThrow(MusicPlayerDatabase.LIST_NAME);
        COLUMN_INDEX_SIZE = c.getColumnIndexOrThrow(MusicPlayerDatabase.LIST_SIZE);
        COLUMN_INDEX_ID = c.getColumnIndexOrThrow(MusicPlayerDatabase._ID);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.list_name);
        String ListName = cursor.getString(COLUMN_INDEX_NAME);
        int listSize = cursor.getInt(COLUMN_INDEX_SIZE);
        name.setText(ListName + " (" + listSize + ")");
        TextView isPalying = (TextView) view.findViewById(R.id.text_isplaying);
        isPalying.setText("");
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = null;
        view = mInflater.inflate(R.layout.list_item_playlist, parent, false);
        return view;
    }

}
