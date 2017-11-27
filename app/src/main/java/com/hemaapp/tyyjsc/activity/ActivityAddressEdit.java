package com.hemaapp.tyyjsc.activity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseConfig;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.Address;
import com.hemaapp.tyyjsc.model.City;
import com.hemaapp.tyyjsc.model.SignValueInfo;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.AreaDialog;

import static com.hemaapp.tyyjsc.R.id.area;


/**
 * 地址编辑 或 新增收货地址
 */
public class ActivityAddressEdit extends BaseActivity implements View.OnClickListener {

    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧微文字按钮
    private EditText nameEditText = null;
    private EditText telEditText = null;
    private EditText codeEditText = null;
    private TextView areaEditText = null;
    private EditText addressEditText = null;
    private String name;
    private String tel;
    private String zip_code;
    private String ssq;
    private String addr;
    private String is_def;//是否为默认地址
    private String province = "";
    private String city = "";
    private String district = "";
    private String province_id = "";
    private String city_id = "";
    private String district_id = "";
    // 三级地址联动列表
    private AreaDialog areaDialog = null;
    private String keytype = "";//0编辑收货地址；1 新增收货地址
    private Address address = null;
    private User user = null;//个人信息
    private SignValueInfo idInfo = null;//新增或编辑地址成功后返回的地址编码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_address_edit);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        user = BaseApplication.getInstance().getUser();
    }

    public void initAddressView() {
        if (address != null) {
            nameEditText.setText(address.getName());
            telEditText.setText(address.getTel());
            codeEditText.setText(address.getZipcode());
            areaEditText.setText(address.getProvince());
            //截去详细地址中的省市区县
            String psd = address.getProvince();
            String addr = address.getAddress();
            int index = addr.indexOf(psd);
            if (index == 0) {//存在开头为省市区
                addr = addr.substring(index + psd.length(), addr.length());
            }
            addressEditText.setText(addr);
            areaEditText.setTag(address.getProvince_id() + "," + address.getCity_id() + "," + address.getDistrict_id());
            is_def = address.getRec();
        }
    }

    @Override
    protected void findView() {
        //标题
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);
        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmBarNameView.setText(R.string.hm_hlxs_txt_30);
        hmRightBtn = (ImageButton) findViewById(R.id.bar_right_img);
        hmRightBtn.setVisibility(View.GONE);
        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.VISIBLE);
        hmRightTxtView.setText(R.string.hm_hlxs_txt_31);
        nameEditText = (EditText) findViewById(R.id.name);
        nameEditText.setFocusable(true);
        nameEditText.requestFocus();
        telEditText = (EditText) findViewById(R.id.tel);
        codeEditText = (EditText) findViewById(R.id.sn);
        codeEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        areaEditText = (TextView) findViewById(area);
        addressEditText = (EditText) findViewById(R.id.address);
    }

    @Override
    protected void getExras() {
        keytype = getIntent().getStringExtra("keytype");
        address = (Address) getIntent().getSerializableExtra("address");
    }

    @Override
    protected void setListener() {
        if ("0".equals(keytype)) {
            hmBarNameView.setText(R.string.hm_hlxs_txt_32);
            initAddressView();
        } else if ("1".equals(keytype)) {
            hmBarNameView.setText(R.string.hm_hlxs_txt_33);
        }
        hmBackBtn.setOnClickListener(this);
        areaEditText.setOnClickListener(this);
        hmRightTxtView.setOnClickListener(this);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case DISTRICT_LIST:
            case ADDRESS_SAVE:
            case SHIPADDRESS_ADD:
                showProgressDialog(getString(R.string.hm_hlxs_txt_34));
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case DISTRICT_LIST:
            case ADDRESS_SAVE:
            case SHIPADDRESS_ADD:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case DISTRICT_LIST:
                new DownloadImageTask().execute(hemaBaseResult);
                break;
            case ADDRESS_SAVE:
            case SHIPADDRESS_ADD:
                showTextDialog(hemaBaseResult.getMsg());
                HemaArrayResult<SignValueInfo> result = (HemaArrayResult<SignValueInfo>) hemaBaseResult;
                if (result != null && result.getObjects() != null && result.getObjects().size() > 0) {
                    idInfo = result.getObjects().get(0);
                }
                Intent intent = new Intent();
                intent.setAction(BaseConfig.BROADCAST_ADDRESS);
                address = new Address(idInfo == null ? "" : idInfo.getId(), name, tel, province + city + district + addr, province, city, province_id, city_id, district_id, district, zip_code, isNull(is_def) ? "2" : is_def);
                intent.putExtra("address", address);
                sendBroadcast(intent);
                hmBarNameView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 500);
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case DISTRICT_LIST:
            case ADDRESS_SAVE:
            case SHIPADDRESS_ADD:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case DISTRICT_LIST:
            case ADDRESS_SAVE:
            case SHIPADDRESS_ADD:
                showTextDialog("网络请求出错");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_left:
                finish();
                break;
            case area:
                showAreaDialog();
                break;
            case R.id.bar_right_txt://新增或编辑地址
                name = nameEditText.getText().toString().trim();
                tel = telEditText.getText().toString().trim();
                zip_code = codeEditText.getText().toString().trim();
                ssq = areaEditText.getText().toString().trim();
                addr = addressEditText.getText().toString().trim();
                if (isNull(name) || isNull(tel) || isNull(ssq) || isNull(addr)) {
                    showTextDialog(getString(R.string.hm_hlxs_txt_35));
                    break;
                }
                String mobileReg = "^[1][3-8]+\\d{9}";
                if (!tel.matches(mobileReg)) {
                    showTextDialog(getString(R.string.hm_hlxs_txt_36));
                    return;
                }
                String[] arr = ssq.split(",");
                String[] ids = (areaEditText.getTag() == null ? "" : areaEditText.getTag()).toString().split(",");
                if (arr.length == 3) {//省市区三级
                    province = arr[0];
                    city = arr[1];
                    district = arr[2];
                    province_id = ids[0];
                    city_id = ids[1];
                    district_id = ids[2];
                }
                if (arr.length == 2) {//省市二级
                    province = arr[0];
                    city = arr[1];
                    district = "";
                    province_id = ids[0];
                    city_id = ids[1];
                    district_id = "0";
                }
                if (address != null) {
                    getNetWorker().addressSave(user.getToken(), address.getId(), name, tel, zip_code,
                            isNull(province) ? ssq : province + "," + city + "," + district, addr);
                } else
                    getNetWorker().addressAdd(user.getToken(), name,
                            tel, zip_code, province + "," + city + "," + district, addr);
                break;
            default:
                break;
        }
    }

    //展示三级列表
    public void showAreaDialog() {
        if (BaseApplication.getInstance().getCityInfo() != null) {
            if (areaDialog == null) {
                areaDialog = new AreaDialog(mContext);
                areaDialog.setButtonListener(new AreaDialog.OnButtonListener() {

                    @Override
                    public void onRightButtonClick(AreaDialog dialog) {
                        areaDialog.cancel();
                        areaEditText.setText(areaDialog.getText());
                        areaEditText.setTag(areaDialog.getId());
                    }

                    @Override
                    public void onLeftButtonClick(AreaDialog dialog) {
                        areaDialog.cancel();
                    }
                });
            }
            areaDialog.show();
        } else {
            getNetWorker().getAddress("2", "-1");
        }
    }

    // 解析数据
    private class DownloadImageTask extends
            AsyncTask<HemaBaseResult, Void, Void> {

        @Override
        protected void onPreExecute() {
            showProgressDialog(getString(R.string.hm_hlxs_txt_38));
        }

        @Override
        protected Void doInBackground(HemaBaseResult... params) {
            @SuppressWarnings("unchecked")
            HemaArrayResult<City> result = ((HemaArrayResult<City>) params[0]);
            if (result != null && result.getObjects().size() > 0) {
                BaseApplication.getInstance().setCityInfo(
                        result.getObjects().get(0));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            cancelProgressDialog();
            showAreaDialog();
        }
    }
}
