package com.hemaapp.tyyjsc.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.City;
import com.hemaapp.tyyjsc.model.CityChildren;
import com.hemaapp.tyyjsc.model.DistrictInfo;
import com.hemaapp.tyyjsc.model.ProvinceChildren;
import com.hemaapp.tyyjsc.view.wheel.widget.OnWheelChangedListener;
import com.hemaapp.tyyjsc.view.wheel.widget.WheelView;
import com.hemaapp.tyyjsc.view.wheel.widget.adapters.ArrayWheelAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xtom.frame.XtomObject;

public class AreaDialog extends XtomObject implements OnWheelChangedListener {

	private Dialog mDialog;
	private TextView tv_cancel, tv_sure;
	private WheelView id_province, id_city, id_district;
	private Context context;
	/**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	/**
	 * key - 区 values - 邮编
	 */

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName = "";
	/**
	 * 当前省的Position
	 */
	protected int mCurrentProvicePosition = 0;
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName = "";
	/**
	 * 当前市的Position
	 */
	protected int mCurrentCityPosition = 0;
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName = "";
	/**
	 * 当前区的Position
	 */
	protected int mCurrentDistrictPosition = 0;

	private OnButtonListener buttonListener;

	private City city;

	@SuppressWarnings("deprecation")
	public AreaDialog(Activity context) {
		this.context = context;

		mDialog = new Dialog(context, R.style.dialog);

		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_with_area, null);

		setUpViews(view);
		setUpListener();
		setUpData();
		tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
		tv_sure = (TextView) view.findViewById(R.id.tv_sure);
		id_province = (WheelView) view.findViewById(R.id.id_province);
		id_city = (WheelView) view.findViewById(R.id.id_city);
		id_district = (WheelView) view.findViewById(R.id.id_district);

		mDialog.setCancelable(false);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setContentView(view);


		Window dialogWindow = mDialog.getWindow();
		WindowManager.LayoutParams params = dialogWindow.getAttributes();
		params.x = 0;// 设置x坐标
		params.y = context.getWindowManager().getDefaultDisplay().getHeight()
				- params.height;// 设置y坐标
		params.width = context.getWindowManager().getDefaultDisplay()
				.getWidth();
		dialogWindow.setAttributes(params);
	}

	private void setUpViews(View view) {
		tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
		tv_sure = (TextView) view.findViewById(R.id.tv_sure);
		id_province = (WheelView) view.findViewById(R.id.id_province);
		id_city = (WheelView) view.findViewById(R.id.id_city);
		id_district = (WheelView) view.findViewById(R.id.id_district);
	}

	private void setUpListener() {
		// 添加change事件
		id_province.addChangingListener(this);
		// 添加change事件
		id_city.addChangingListener(this);
		// 添加change事件
		id_district.addChangingListener(this);
		// 添加onclick事件
		tv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				log_d("-------tv_cancel-----------" + (buttonListener != null));
				if (buttonListener != null)
					buttonListener.onLeftButtonClick(AreaDialog.this);
			}
		});
		tv_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				log_d("-------tv_sure-----------" + (buttonListener != null));
				if (buttonListener != null)
					buttonListener.onRightButtonClick(AreaDialog.this);
			}
		});
	}

	private void setUpData() {
		initProvinceDatas();
		id_province.setViewAdapter(new ArrayWheelAdapter<String>(context,
				mProvinceDatas));
		// 设置可见条目数量
		id_province.setVisibleItems(5);
		id_city.setVisibleItems(5);
		id_district.setVisibleItems(5);
		updateCities();
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == id_province) {
			updateCities();
		} else if (wheel == id_city) {
			updateAreas();
		} else if (wheel == id_district) {
			mCurrentDistrictPosition = newValue;
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
		}
	}

	public String getText() {
		return mCurrentProviceName +"," +  mCurrentCityName + "," + mCurrentDistrictName;
	}

	public String getId() {
		String id = "";
		if (city.getChildren() != null
				&& city.getChildren().size() > mCurrentProvicePosition) {
			id = city.getChildren().get(mCurrentProvicePosition).getId();
			if (city.getChildren().get(mCurrentProvicePosition).getChildren() != null
					&& city.getChildren().get(mCurrentProvicePosition)
					.getChildren().size() > mCurrentCityPosition) {
				id = id + "," + city.getChildren().get(mCurrentProvicePosition)
						.getChildren().get(mCurrentCityPosition)
						.getId();
				if (city.getChildren().get(mCurrentProvicePosition)
						.getChildren().get(mCurrentCityPosition).getChildren() != null
						&& city.getChildren().get(mCurrentProvicePosition)
						.getChildren().get(mCurrentCityPosition)
						.getChildren().size() > mCurrentDistrictPosition) {
					id = id + "," + city.getChildren().get(mCurrentProvicePosition)
							.getChildren().get(mCurrentCityPosition)
							.getChildren()
							.get(mCurrentDistrictPosition).getId();
				} else {
					id = id + "," + city.getChildren().get(mCurrentProvicePosition)
							.getId();
				}
			} else {
				id = id + "," + id + "," + id;
			}
		} else {
			id = "0,0,0";
		}
		return id;
	}

	private void initProvinceDatas() {
		city = BaseApplication.getInstance().getCityInfo();
		List<ProvinceChildren> provinceList = city.getChildren();
		// */ 初始化默认选中的省、市、区
		if (provinceList != null && !provinceList.isEmpty()) {
			mCurrentProviceName = provinceList.get(0).getName();
			List<CityChildren> cityList = provinceList.get(0).getChildren();
			if (cityList != null && !cityList.isEmpty()) {
				mCurrentCityName = cityList.get(0).getName();
				List<DistrictInfo> districtList = cityList.get(0).getChildren();
				mCurrentDistrictName = districtList.get(0).getName();
			}
		}
		// */
		mProvinceDatas = new String[provinceList.size()];
		for (int i = 0; i < provinceList.size(); i++) {
			// 遍历所有省的数据
			mProvinceDatas[i] = provinceList.get(i).getName();
			List<CityChildren> cityList = provinceList.get(i).getChildren();
			String[] cityNames = new String[cityList.size()];
			for (int j = 0; j < cityList.size(); j++) {
				// 遍历省下面的所有市的数据
				cityNames[j] = cityList.get(j).getName();// 当前省
				List<DistrictInfo> districtList = cityList.get(j).getChildren();// 当前省下的城市
				String[] distrinctNameArray = new String[districtList.size()];// 城市名称列表
				DistrictInfo[] distrinctArray = new DistrictInfo[districtList
						.size()];// 城市列表
				for (int k = 0; k < districtList.size(); k++) {
					// 遍历市下面所有区/县的数据
					DistrictInfo districtModel = districtList.get(k);
					// 区/县对于的邮编，保存到mZipcodeDatasMap
					distrinctArray[k] = districtModel;
					distrinctNameArray[k] = districtModel.getName();
				}
				// 市-区/县的数据，保存到mDistrictDatasMap
				mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
			}
			// 省-市的数据，保存到mCitisDatasMap
			mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
		}

	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = id_city.getCurrentItem();
		mCurrentCityPosition = pCurrent;
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null || areas.length == 0) {
			areas = new String[] { "" };
		}
		id_district
				.setViewAdapter(new ArrayWheelAdapter<String>(context, areas));
		id_district.setCurrentItem(0);
		mCurrentDistrictPosition = 0;
		mCurrentDistrictName = areas[0];
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = id_province.getCurrentItem();
		mCurrentProvicePosition = pCurrent;
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null || cities.length == 0) {
			cities = new String[] { "" };
		}
		id_city.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
		id_city.setCurrentItem(0);
		updateAreas();
	}

	public OnButtonListener getButtonListener() {
		return buttonListener;
	}

	public void setButtonListener(OnButtonListener buttonListener) {
		this.buttonListener = buttonListener;
		log_d("buttonListener" + buttonListener);
	}

	public interface OnButtonListener {
		void onLeftButtonClick(AreaDialog dialog);

		void onRightButtonClick(AreaDialog dialog);
	}

	public void show() {
		new Handler(){}.postDelayed(new Runnable() {
			@Override
			public void run() {
				Window window = mDialog.getWindow();
				window.setWindowAnimations(R.style.mystyle);
				mDialog.show();
			}
		}, 10);
	}

	public void cancel() {
		mDialog.cancel();
	}
}