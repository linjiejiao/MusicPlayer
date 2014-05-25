package cn.ljj.musicplayer.ui;

import java.util.Observable;
import java.util.Observer;

import cn.ljj.musicplayer.R;
import cn.ljj.musicplayer.data.MusicInfo;
import cn.ljj.musicplayer.data.StaticUtils;
import cn.ljj.musicplayer.database.Logger;
import cn.ljj.musicplayer.player.service.INotify;
import cn.ljj.musicplayer.player.service.NotifyImpl;
import cn.ljj.musicplayer.player.service.PlayService;
import cn.ljj.musicplayer.playlist.PlayList;
import cn.ljj.musicplayer.ui.lrc.LrcPicManager;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class BaseActivity extends FragmentActivity implements OnClickListener, OnSeekBarChangeListener,Observer{
	public static  String TAG = "BaseActivity";
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	Button mBtnNext = null;
	Button mBtnPrev = null;
	Button mBtnPlay = null;
	TextView mTextTimePassed = null;
	TextView mTextTimeAll = null;
	SeekBar mSeekPlayProgress = null;
	PlayList mPlaylist = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		mPlaylist = PlayList.getPlayList(this);
		initViews();
		bindService();
	}

	private void initViews(){
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mBtnNext = (Button) findViewById(R.id.buttonNext);
		mBtnPrev = (Button) findViewById(R.id.buttonPrev);
		mBtnPlay = (Button) findViewById(R.id.buttonPlay);
		mBtnNext.setOnClickListener(this);
		mBtnPrev.setOnClickListener(this);
		mBtnPlay.setOnClickListener(this);
		mTextTimePassed = (TextView) findViewById(R.id.text_time_passed);
		mTextTimeAll = (TextView) findViewById(R.id.text_time_all);
		mSeekPlayProgress = (SeekBar) findViewById(R.id.seek_play_progress);
		mSeekPlayProgress.setOnSeekBarChangeListener(this);
	}

	PlayingFragment playing = null;
	PlayListFragment playlist = null;
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			if(position == 0){
				playlist = new PlayListFragment();
				playlist.setCallback(mCallback);
				fragment = playlist;
			}else{
				playing = new PlayingFragment();
				fragment = playing;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "PlayList";
			case 1:
				return "Playing";
			}
			return "other";
		}
	}

	@Override
	protected void onDestroy() {
		unbindService(mSerConn);
		Intent service = new Intent();
		service.setClass(this, PlayService.class);
		stopService(service);
		Logger.i(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.base, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.action_add:
//				mPlaylist.deletePlayList("123");
				PlayListDialog listDialog = new PlayListDialog(this);
				listDialog.show();
				break;
			case R.id.action_search:
				if(playlist.mSearchView.getVisibility() == View.VISIBLE && mViewPager.getCurrentItem() == 0){
					playlist.mSearchView.setVisibility(View.GONE);
				}else{
					playlist.mSearchView.setVisibility(View.VISIBLE);
					mViewPager.setCurrentItem(0);
				}
				break;
			case R.id.action_settings:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	int i = 0;
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.buttonNext:
				sendCmd(NotifyImpl.CMD_PLAY_EVENT, -1, 0, null, mPlaylist.getNext());
				break;
			case R.id.buttonPlay:
				if(!BaseActivity.this.equals(mBtnPlay.getTag())){
					MusicInfo music = mPlaylist.get();
					boolean ret = sendCmd(NotifyImpl.CMD_PLAY_EVENT, mPlaylist.getProgress(), 0, null, music);
					if(ret){
						mBtnPlay.setBackgroundResource(R.drawable.button_pause);
						mBtnPlay.setTag(BaseActivity.this);
					}
				}else{
					boolean ret = sendCmd(NotifyImpl.CMD_STOP_EVENT, 0, 0,null,null);
					if(ret){
						mBtnPlay.setBackgroundResource(R.drawable.button_play);
						mBtnPlay.setTag(null);
					}
				}
				break;
			case R.id.buttonPrev:
				sendCmd(NotifyImpl.CMD_PLAY_EVENT, -1, 0, null, mPlaylist.getPrev());
				break;
		}
	}


	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean manual) {
		if(manual){
			sendCmd(NotifyImpl.CMD_SEEK_EVENT, progress, 0, null, null);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
	}

	private INotify mService = null;
	private ServiceConnection mSerConn = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = (INotify) INotify.Stub.asInterface(service);
			try {
				mService.setCallback(mCallback);
				Logger.i(TAG, "onServiceConnected mService.setCallback(mCallback);");
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	};

	private INotify mCallback = new NotifyImpl(){
		@Override
		public void setCallback(INotify callback) throws RemoteException {
			super.setCallback(callback);
		}

		@Override
		public int onNotify(int cmd, final int intValue, final long longValue, String str, MusicInfo music)
				throws RemoteException {
			switch(cmd){
				case CMD_PLAY_EVENT:
					boolean ret = sendCmd(cmd, mPlaylist.getProgress(), longValue, str, music);
					if(ret){
						mBtnPlay.setBackgroundResource(R.drawable.button_pause);
						mBtnPlay.setTag(BaseActivity.this);
					}else{
						return RET_ERROR;
					}
					break;
				case CMD_STOP_EVENT:
				case CMD_SEEK_EVENT:
					break;
				case CMD_UPDATE_PROGRESS:
					mPlaylist.setProgress(intValue);
					final int progress = (int) ((intValue*100)/longValue);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mTextTimeAll.setText(StaticUtils.getDispTime((int)longValue));
							mTextTimePassed.setText(StaticUtils.getDispTime(intValue));
							mSeekPlayProgress.setProgress(progress);
							playing.onProgressChange(intValue, (int)longValue);
						}
					});
					break;
				case CMD_PLAY_REACH_END:
					sendCmd(NotifyImpl.CMD_PLAY_EVENT, -1, 0, null, mPlaylist.getNext());
					Logger.i(TAG, "CMD_PLAY_REACH_END");
					break;
				case CMD_REPORT_STATUS:
					break;
				default :
					return RET_INVALID;
			}
			return super.onNotify(cmd, intValue, longValue, str,music);
		}
	};
	
	private void bindService(){
		Intent service = new Intent();
		service.setClass(getBaseContext(), PlayService.class);
		bindService(service, mSerConn, Service.BIND_AUTO_CREATE );
	}

	private boolean sendCmd(int cmd, int intValue, long longValue, String str, final MusicInfo music){
		boolean ret = false;
		if(mService == null){
			bindService();
			return ret;
		}
		try {
			if(mService.onNotify(cmd, intValue, longValue, str, music) == NotifyImpl.RET_OK){
				if(cmd ==NotifyImpl. CMD_PLAY_EVENT){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							try {
								initLrcPic(music);
								playlist.mPlayListView.setSelection(mPlaylist.getCurrentIndex());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
				ret = true;
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}
	private void initLrcPic(final MusicInfo music){
			playing.setLrc(null);
			playing.setImage(null);
			String lrcPath = LrcPicManager.getLrc(music);
			if(!TextUtils.isEmpty(lrcPath)){
				playing.setLrc(lrcPath);
			}else{
				Logger.e(TAG, "setLrc addObserver");
				music.addObserver(BaseActivity.this);
			}
			String picPath = LrcPicManager.getPic(music);
			if(!TextUtils.isEmpty(picPath)){
				playing.setImage(picPath);
			}else{
				Logger.e(TAG, "setImage addObserver");
				music.addObserver(BaseActivity.this);
			}
	}

	@Override
	public void update(Observable observable, Object data) {
		Logger.v(TAG, "update");
		MusicInfo music = (MusicInfo)data;
		if(!mPlaylist.get().equals(music)){
			return;
		}
		switch(music.getChanged()){
			case MusicInfo.LRCPATH_CHANGED:
				final String lrcPath = music.getLrcPath();
				if(!TextUtils.isEmpty(lrcPath)){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							playing.setLrc(lrcPath);
						}
					});
				}
				break;
			case MusicInfo.PICPATH_CHANGED:
				final String picPath = music.getPicPath();
				if(!TextUtils.isEmpty(picPath)){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							playing.setImage(picPath);
						}
					});
				}
				break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
	    	case KeyEvent.KEYCODE_BACK:
	    		AlertDialog.Builder adb = new AlertDialog.Builder(this);
	    		adb.setTitle("警告");
	    		adb.setMessage("确定要退出播放器？");
	    		adb.setPositiveButton("退出", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
	    		adb.setNegativeButton("后台播放", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						moveTaskToBack(true);
					}
				});
	    		adb.show();
	    		return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
