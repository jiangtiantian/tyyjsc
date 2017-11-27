package com.hemaapp.tyyjsc.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.tyyjsc.R;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

public class BaseProgressDialog extends Dialog{
	private TextView mTextView;

	private Handler mHandler;
	private Runnable runnable;

	public BaseProgressDialog(Context context, int theme) {
		super(context, theme);
	}
	@Override
	protected void onStart() {
		mHandler = new Handler();
		runnable = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					setContentView(R.layout.dialog_progress1);
					ImageView mImageView = (ImageView) findViewById(R.id.loadingdialog_img);
					mTextView = (TextView) findViewById(R.id.textview1);
					mImageView.setBackgroundResource(R.drawable.anim_loading);
					AnimationDrawable _animaition = (AnimationDrawable) mImageView.getBackground();
					// 是否仅仅启动一次？
					_animaition.setOneShot(true);

					setCanceledOnTouchOutside(false);

					if (_animaition.isRunning()) {
						_animaition.stop();
						for (int i = 0; i < _animaition.getNumberOfFrames(); ++i) {
							Drawable frame = _animaition.getFrame(i);
							if (frame instanceof BitmapDrawable) {
								((BitmapDrawable) frame).getBitmap().recycle();
							}
							frame.setCallback(null);
						}
						_animaition.setCallback(null);
					}
					_animaition.start();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		};
		mHandler.postDelayed(runnable, 600);
		super.onStart();
	}
	public void setCancelable(boolean cancelable) {
		if(mHandler != null && runnable != null){
			mHandler.removeCallbacks(runnable);
			Log.d(TAG, "setCancelable: -------null-----------");
		}
		super.cancel();
	}
	public void setText(String text) {
		if(mTextView!=null)
		mTextView.setText(text);
	}
	public void setText(int textID) {
		if(mTextView!=null)
		mTextView.setText(textID);
	}

	public void show() {
		super.show();
	}

	public void dismiss() {
		super.dismiss();
	}
	@Override
	protected void onStop() {
	    super.onStop();

	}

}
