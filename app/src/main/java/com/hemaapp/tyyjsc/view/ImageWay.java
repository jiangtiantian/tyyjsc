package com.hemaapp.tyyjsc.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hemaapp.tyyjsc.R;

import java.io.File;

import xtom.frame.XtomObject;
import xtom.frame.util.XtomBaseUtil;
import xtom.frame.util.XtomFileUtil;

/**
 * 选择图片方式 自定义弹框儿
 * */
public class ImageWay extends XtomObject {
	private Activity mContext;// 上下文对象
	private Fragment mFragment;// 上下文对象
	// private Builder mBuilder;// 弹出对象
	private int albumRequestCode;// 相册选择时startActivityForResult方法的requestCode值
	private int cameraRequestCode;// 拍照选择时startActivityForResult方法的requestCode值
	private static final String IMAGE_TYPE = ".jpg";// 图片名后缀
	private String imagePathByCamera;// 拍照时图片保存路径

	// 自定义弹框
	private PopupWindow mWindow;
	private ViewGroup mViewGroup;
	private TextView bigImgView = null;
	private TextView send_buy;
	private TextView send_sell;
	private TextView send_cancel;

	private View line0;
	private View line;

	//头像
	private String path;

	/**
	 * 创建一个选择图片方式实例
	 *
	 * @param mContext
	 *            上下文对象
	 * @param albumRequestCode
	 *            相册选择时startActivityForResult方法的requestCode值
	 * @param cameraRequestCode
	 *            拍照选择时startActivityForResult方法的requestCode值
	 */
	public ImageWay(Activity mContext, int albumRequestCode,
					int cameraRequestCode) {
		this.mContext = mContext;
		this.albumRequestCode = albumRequestCode;
		this.cameraRequestCode = cameraRequestCode;
	}

	/**
	 * 创建一个选择图片方式实例
	 *
	 * @param
	 *
	 * @param albumRequestCode
	 *            相册选择时startActivityForResult方法的requestCode值
	 * @param cameraRequestCode
	 *            拍照选择时startActivityForResult方法的requestCode值
	 */
	public ImageWay(Fragment mFragment, int albumRequestCode,
					int cameraRequestCode) {
		this.mFragment = mFragment;
		this.albumRequestCode = albumRequestCode;
		this.cameraRequestCode = cameraRequestCode;
	}

	/**
	 * 显示图片选择对话
	 */
	@SuppressWarnings("deprecation")
	public void show() {
		if (mWindow != null) {
			mWindow.dismiss();
		}
		mWindow = new PopupWindow(mContext);
		mWindow.setWidth(LayoutParams.MATCH_PARENT);
		mWindow.setHeight(LayoutParams.MATCH_PARENT);
		mWindow.setBackgroundDrawable(new BitmapDrawable());
		mWindow.setFocusable(true);
		mWindow.setAnimationStyle(R.style.PopupAnimation);
		mViewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.pop_imageway, null);

		send_buy = (TextView) mViewGroup.findViewById(R.id.textview);
		send_sell = (TextView) mViewGroup.findViewById(R.id.textview_0);
		line0 = mViewGroup.findViewById(R.id.line_0);



		send_cancel = (TextView) mViewGroup.findViewById(R.id.textview_1);
		mWindow.setContentView(mViewGroup);
		mWindow.showAtLocation(mViewGroup, Gravity.CENTER, 0, 0);
		setPopListener();
	}

	private void setPopListener(){

		send_buy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mWindow.dismiss();
				click(1);
			}
		});
		send_sell.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mWindow.dismiss();
				click(0);
			}
		});
		send_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mWindow.dismiss();
				click(2);
			}
		});
	}

	private void click(int which) {
		switch (which) {
			case 0:
				album();
				break;
			case 1:
				camera();
				break;
			case 2:
				break;
		}
	}

	public void album() {
		Intent it1 = new Intent(Intent.ACTION_PICK,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		if (mContext != null)
			mContext.startActivityForResult(it1, albumRequestCode);
		else
			mFragment.startActivityForResult(it1, albumRequestCode);
	}

	public void camera() {
		String imageName = XtomBaseUtil.getFileName() + IMAGE_TYPE;
		Intent it3 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String imageDir = XtomFileUtil
				.getTempFileDir(mContext == null ? mFragment.getActivity()
						: mContext);
		imagePathByCamera = imageDir + imageName;
		File file = new File(imageDir);
		if (!file.exists())
			file.mkdir();
		// 设置图片保存路径
		File out = new File(file, imageName);
		Uri uri = Uri.fromFile(out);
		it3.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		if (mContext != null)
			mContext.startActivityForResult(it3, cameraRequestCode);
		else
			mFragment.startActivityForResult(it3, cameraRequestCode);
	}

	/**
	 * 获取拍照图片路径
	 *
	 * @return 图片路径
	 */
	public String getCameraImage() {
		return imagePathByCamera;
	}
}
