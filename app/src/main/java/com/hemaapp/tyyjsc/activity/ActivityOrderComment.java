package com.hemaapp.tyyjsc.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseConfig;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.adapters.SendImageAdapter;
import com.hemaapp.tyyjsc.adapters.StarAdapter;
import com.hemaapp.tyyjsc.model.CartGoodsInfo;
import com.hemaapp.tyyjsc.model.ReplyId;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.CityGridView;
import com.hemaapp.tyyjsc.view.ImageWay;
import com.hemaapp.tyyjsc.view.album.ActivityAlbumSelector;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomImageUtil;

/**
 * 订单评价
 * type:1:商品订单，2:套餐订单
 */
public class ActivityOrderComment extends BaseActivity implements View.OnClickListener {

    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧文字按钮
    private String type;//1，普通订单 2.套餐订单评价
    private String orderid;
    private GridView starGrid = null;
    private StarAdapter starAdapter = null;//星级评论
    private int star_num = 5; // 当前星

    private EditText adviceET = null;
    private TextView numView = null;//评论限制

    // 选择图像
    private CityGridView imgGridView = null;
    private SendImageAdapter sendImageAdapter = null;
    private ArrayList<String> images = new ArrayList<>();
    private View rootView = null;
    private ImageWay imageWay = null;
    // 图片路径
    private String tempPath;
    //商品信息
    private ImageView goodsImg;
    private TextView goodsName;
    private TextView goodsPrice;
    private TextView goodsNum;
    private CartGoodsInfo goodsInfo = null;//商品信息
    private String reply_id = "";
    private int orderby = 0;//上传顺序
    private User user = null;

    //是否匿名评论
    private ToggleButton toggleButton;
    private String is_incognito = "0";   //是否匿名,0不匿名，1匿名
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_comment);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        user = BaseApplication.getInstance().getUser();
        imageWay = new ImageWay(mContext, 1, 2) {
            @Override
            public void album() {
                Intent it = new Intent(mContext, ActivityAlbumSelector.class);
                it.putExtra("limit", 4 - images.size());// 图片选择张数限制
                startActivityForResult(it, 1);
            }
        };
        setStarGridViewSize();
        // 星级适配器
        starAdapter = new StarAdapter(mContext, star_num);
        starGrid.setAdapter(starAdapter);

        starGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                star_num = position + 1;
                starAdapter.setPos(star_num);
                starAdapter.notifyDataSetChanged();
            }
        });

        View rootView = LayoutInflater.from(mContext).inflate(
                R.layout.img_item, null);
        sendImageAdapter = new SendImageAdapter(mContext, rootView, images, 4);
        imgGridView.setAdapter(sendImageAdapter);
        //展示商品信息
        numRedChange(1, "0/140");
        goodShow();
    }

    public void goodShow() {
        if (goodsInfo != null) {
            try {
                ImageLoader.getInstance().displayImage(goodsInfo.getImgurl(), goodsImg, BaseUtil.displayImageOption());
            } catch (OutOfMemoryError error) {
                ImageLoader.getInstance().clearMemoryCache();
                goodsImg.setImageResource(R.mipmap.hm_icon_def);
            }
            goodsName.setText(goodsInfo.getName());
            goodsPrice.setText("¥" + goodsInfo.getPrice());
            goodsNum.setText("x " + goodsInfo.getBuycount());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1:// 相册选择图片
                ArrayList<String> imgs = data.getStringArrayListExtra("images");
                for (int i = 0; i < imgs.size(); i++) {
                    new CompressPicTask().execute(imgs.get(i));
                }
                break;
            case 2:// 拍照
                String imagepath = imageWay.getCameraImage();
                new CompressPicTask().execute(imagepath);
                break;
            case 3:// 裁剪
                if (sendImageAdapter != null) {
                    sendImageAdapter.notifyDataSetChanged();
                }
                break;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setStarGridViewSize() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) starGrid.getLayoutParams();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        params.width = dm.widthPixels / 3;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        starGrid.setLayoutParams(params);
    }

    // 选择相册
    public void openAlbum() {
        closeKeyboard();// 关闭键盘
        imageWay.show();
    }

    @Override
    protected void findView() {
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);
        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmBarNameView.setText("评价");
        hmRightBtn = (ImageButton) findViewById(R.id.bar_right_img);
        hmRightBtn.setVisibility(View.INVISIBLE);
        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.VISIBLE);
        hmRightTxtView.setText("提交");
        goodsImg = (ImageView) findViewById(R.id.goods_img);
        goodsName = (TextView) findViewById(R.id.goods_name);
        goodsPrice = (TextView) findViewById(R.id.goods_price);
        goodsNum = (TextView) findViewById(R.id.goods_num);
        adviceET = (EditText) findViewById(R.id.advice);
        numView = (TextView) findViewById(R.id.num);
        starGrid = (GridView) findViewById(R.id.star);
        imgGridView = (CityGridView) findViewById(R.id.imggridview);
        toggleButton = (ToggleButton) findViewById(R.id.togglebutton);
    }

    @Override
    protected void getExras() {
        goodsInfo = (CartGoodsInfo) getIntent().getSerializableExtra("goods");
        type = getIntent().getStringExtra("type");
        orderid = getIntent().getStringExtra("orderid");
    }

    @Override
    protected void setListener() {
        hmBackBtn.setOnClickListener(this);
        hmRightTxtView.setOnClickListener(this);

        adviceET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                numRedChange(String.valueOf(s.toString().length()).length(), String.valueOf(s.toString().length()) + "/140");
            }
        });

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    is_incognito = "1";
                else
                    is_incognito = "0";
            }
        });
    }

    public void numRedChange(int length, String str) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.index_search_bg));
        ssb.setSpan(span, 0, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        numView.setText(ssb);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case REPLY_ADD:
            case FILE_UPLOAD:
                showProgressDialog(R.string.hm_hlxs_txt_59);
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case REPLY_ADD:
            case FILE_UPLOAD:
                cancelProgressDialog();
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case REPLY_ADD:
                HemaArrayResult<ReplyId> result = (HemaArrayResult<ReplyId>) hemaBaseResult;
                ArrayList<ReplyId> ids = result.getObjects();
                if (ids != null && ids.size() > 0) {
                    reply_id = ids.get(0).getId();
                    if (images != null && images.size() > 0) {
                        getNetWorker().fileUpload(user.getToken(), "2",
                                reply_id, "0", String.valueOf(orderby), "无",
                                images.get(0));
                    } else {
                        showTextDialog("评价成功");
                        hmBarNameView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent();
                                intent.putExtra("order_goods_id", goodsInfo == null ? "" : goodsInfo.getId());
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }, 500);
                    }
                }
                break;
            case FILE_UPLOAD:
                images.remove(images.get(0));
                if (images.size() == 0) {
                    showTextDialog("评价成功");
                    hmBarNameView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.putExtra("order_goods_id", goodsInfo == null ? "" : goodsInfo.getId());
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }, 500);
                } else {
                    orderby++;
                    getNetWorker().fileUpload(user.getToken(), "2",
                            reply_id, "0", String.valueOf(orderby), "无",
                            images.get(0));
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case REPLY_ADD:
            case FILE_UPLOAD:
            case ORDER_REFUND:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case REPLY_ADD:
            case ORDER_REFUND:
            case FILE_UPLOAD:
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_left:
                finish();
                break;
            case R.id.bar_right_txt://提交
                String advice = adviceET.getText().toString().trim();
                if (user != null) {
                    if ("1".equals(type))
                        getNetWorker().replyAdd(user.getToken(), "1", goodsInfo == null ? "" : goodsInfo.getCartid(), orderid, String.valueOf(star_num),
                                isNull(advice) ? "" : BaseUtil.replaceBlank(advice), "0", is_incognito);
                    else if ("2".equals(type))
                        getNetWorker().replyAdd(user.getToken(), "2", goodsInfo == null ? "" : goodsInfo.getId(), orderid, String.valueOf(star_num),
                                isNull(advice) ? "" : BaseUtil.replaceBlank(advice), "0", is_incognito);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 压缩图片
     */
    private class CompressPicTask extends AsyncTask<String, Void, Integer> {
        String compressPath;

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String path = params[0];
                String savedir = XtomFileUtil.getTempFileDir(mContext);
                compressPath = XtomImageUtil.compressPictureWithSaveDir(path,
                        BaseConfig.IMAGE_HEIGHT, BaseConfig.IMAGE_WIDTH,
                        BaseConfig.IMAGE_QUALITY, savedir, mContext);
                return 0;
            } catch (IOException e) {
                return 1;
            }
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog("正在压缩图片");
        }

        @Override
        protected void onPostExecute(Integer result) {
            cancelProgressDialog();
            switch (result) {
                case 0:
                    images.add(compressPath);
                    sendImageAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    showTextDialog("图片压缩失败");
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (images != null && images.size() > 0) {
            for (int i = 0; i < images.size(); i++) {
                File file = new File(images.get(i));
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }
}
