package cn.ljj.musicplayer.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class LrcView extends ScrollView {
	static final int LRC_COLOR_NORMAL = 0xffffffff;
	static final int LRC_COLOR_PALYING = 0xff00ffff;
	static final int LRC_SIZE_NORMAL = 20;
	static final int LRC_SIZE_PALYING = 25;
	static final int LRC_MARGIN = 10;
	List<TextView> mLrcTexts = new ArrayList<TextView>();
	TextView mTopText = null;
	TextView mBottomText = null;
	int mCurrentPosistion = 0;
	Context mContext = null;
	LinearLayout mLinearLayout = null;
	public LrcView(Context context) {
		super(context);
		mContext = context;
	}

	public LrcView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public int getmCurrentPosistion() {
		return mCurrentPosistion;
	}

	public void initViews(List<String> lrcs){
		removeAllViews();
		setSmoothScrollingEnabled(true);
		addLinearLayout();
		mLrcTexts.clear();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		lp.topMargin = LRC_MARGIN;
		lp.bottomMargin = LRC_MARGIN;
		mTopText = new TextView(mContext);
		mTopText.setTextSize(2*LRC_SIZE_NORMAL);
		mTopText.setGravity(Gravity.CENTER_HORIZONTAL);
		mLinearLayout.addView(mTopText,lp);

		for(String str:lrcs){
			TextView text = new TextView(mContext);
			text.setText(str);
			text.setTextSize(LRC_SIZE_NORMAL);
			text.setTextColor(LRC_COLOR_NORMAL);
			text.setGravity(Gravity.CENTER_HORIZONTAL);
			mLinearLayout.addView(text,lp);
			mLrcTexts.add(text);
		}

		mBottomText = new TextView(mContext);
		mBottomText.setTextSize(2*LRC_SIZE_NORMAL);
		mBottomText.setGravity(Gravity.CENTER_HORIZONTAL);
		mLinearLayout.addView(mBottomText,lp);
	}

	private LinearLayout addLinearLayout(){
		mLinearLayout = new LinearLayout(mContext);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		mLinearLayout.setOrientation(LinearLayout.VERTICAL);
		addView(mLinearLayout,lp);
		return mLinearLayout;
	}

	public void highlight(int posistion){
		if(posistion < mLrcTexts.size()){
			TextView text = mLrcTexts.get(mCurrentPosistion);
			text.setTextColor(LRC_COLOR_NORMAL);
			text.setTextSize(LRC_SIZE_NORMAL);
			text = mLrcTexts.get(posistion);
			text.setTextColor(LRC_COLOR_PALYING);
			text.setTextSize(LRC_SIZE_PALYING);
			mCurrentPosistion = posistion;
		}
		post(mAutoScrollRun);
	}

	private void scrollToPlaying(){
		int allheight = mTopText.getHeight() + 2*LRC_MARGIN;
		for(int i=0;i<=mCurrentPosistion;i++){
			allheight += mLrcTexts.get(i).getHeight() + 2*LRC_MARGIN;
		}
		int delta = (int) (getHeight()/2 + mLrcTexts.get(mCurrentPosistion).getHeight()/2+LRC_MARGIN);
		smoothScrollTo(0, allheight - delta);
	}

	Runnable mAutoScrollRun = new Runnable() {
		@Override
		public void run() {
			scrollToPlaying();
		}
	};

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		removeCallbacks(mAutoScrollRun);
		postDelayed(mAutoScrollRun, 2000);
		super.onScrollChanged(l, t, oldl, oldt);
	}

	
}
