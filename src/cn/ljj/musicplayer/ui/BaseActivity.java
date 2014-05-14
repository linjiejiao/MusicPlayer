package cn.ljj.musicplayer.ui;

import cn.ljj.musicplayer.R;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class BaseActivity extends FragmentActivity implements OnClickListener, OnSeekBarChangeListener{
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	Button mBtnNext = null;
	Button mBtnPrev = null;
	Button mBtnPlay = null;
	TextView mTextTimePassed = null;
	TextView mTextTimeAll = null;
	SeekBar mSeekPlayProgress = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		initViews();
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
			if(position == 1){
				playlist = new PlayListFragment();
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.base, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.action_add:
				break;
			case R.id.action_search:
				if(playlist.mSearchView.getVisibility() == View.VISIBLE){
					playlist.mSearchView.setVisibility(View.GONE);
				}else{
					playlist.mSearchView.setVisibility(View.VISIBLE);
				}
				mViewPager.setCurrentItem(1);
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
//				i++;
//				if(i>29){
//					i=0;
//				}
//				playing.lrc_view.highlight(i);
				break;
			case R.id.buttonPlay:
//				i=0;
//				playing.lrc_view.highlight(i);
				break;
			case R.id.buttonPrev:
//				i--;
//				if(i<0){
//					i=29;
//				}
//				playing.lrc_view.highlight(i);
				
				break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean manual) {
		if(manual){
			
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
	}

}
