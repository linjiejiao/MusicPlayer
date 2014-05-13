package cn.ljj.musicplayer.ui;

import cn.ljj.musicplayer.R;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BaseActivity extends FragmentActivity implements OnClickListener{
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	Button mBtnNext = null;
	Button mBtnPrev = null;
	Button mBtnPlay = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
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
	}
	PlayingFragment playing = null;
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			if(position == 1)
				fragment = new PlayListFragment();
			else{
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
	int i = 0;
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.buttonNext:
				i++;
				if(i>29){
					i=0;
				}
				playing.lrc_view.highlight(i);
				break;
			case R.id.buttonPlay:
				i=0;
				playing.lrc_view.highlight(i);
				break;
			case R.id.buttonPrev:
				i--;
				if(i<0){
					i=29;
				}
				playing.lrc_view.highlight(i);
				
				break;
		}
	}

}
