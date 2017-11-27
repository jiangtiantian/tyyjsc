package com.hemaapp.tyyjsc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.adapters.NewCommentAdapter;
import com.hemaapp.tyyjsc.model.Comment;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 评价列表
 */
public class ActivityComment extends BaseActivity {

    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧微文字按钮
    private RefreshLoadmoreLayout layout = null;
    private XtomListView listView = null;
    private NewCommentAdapter commentAdapter = null;
    private ArrayList<Comment> data = new ArrayList<Comment>();
    private FrameLayout topView = null;
    private int page = 0;
    private String keyid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_comment);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        getCommentList(keyid);
    }
    //评论列表
    public void getCommentList(String keyid) {
        getNetWorker().getCommentList("1", keyid, String.valueOf(page));
    }
    @Override
    protected void callBeforeDataBack(HemaNetTask netTask) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case REPLY_LIST:
                showProgressDialog(R.string.hm_hlxs_txt_59);
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask netTask) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case REPLY_LIST:
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
            case REPLY_LIST:
                String page = hemaNetTask.getParams().get("page");
                int sysPagesize = BaseApplication.getInstance().getSysInitInfo()
                        .getSys_pagesize();
                HemaPageArrayResult<Comment> nResult = (HemaPageArrayResult<Comment>) hemaBaseResult;
                ArrayList<Comment> list = nResult.getObjects();
                if ("0".equals(page)) {// 刷新
                    layout.refreshSuccess();
                    data.clear();
                    data.addAll(list);
                    if (list.size() < sysPagesize)
                        layout.setLoadmoreable(false);
                    else
                        layout.setLoadmoreable(true);
                } else {// 更多
                    layout.loadmoreSuccess();
                    if (list.size() > 0) {
                        data.addAll(list);
                        if (list.size() < sysPagesize)
                            layout.setLoadmoreable(false);
                        else
                            layout.setLoadmoreable(true);
                    } else {
                        layout.setLoadmoreable(false);
                        XtomToastUtil.showShortToast(ActivityComment.this, getString(R.string.hm_hlxs_txt_89));
                    }
                }
                freshData();
                break;
            default:
                break;
        }
    }

    private void freshData() {
        if (commentAdapter == null) {
            commentAdapter = new NewCommentAdapter(mContext, data);
            commentAdapter.setEmptyString(getString(R.string.hm_hlxs_txt_175));
            listView.setAdapter(commentAdapter);
        } else {
            commentAdapter.setData(data);
            commentAdapter.setEmptyString(getString(R.string.hm_hlxs_txt_175));
            commentAdapter.notifyDataSetChanged();
        }
        if(data == null || data.size() == 0){
            topView.setVisibility(View.GONE);
        }else{
            topView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask,
                                           HemaBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case REPLY_LIST:
                showTextDialog(baseResult.getMsg());
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask netTask, int failedType) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case REPLY_LIST:

                break;
            default:
                break;
        }
    }

    @Override
    protected void findView() {

        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);

        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmBarNameView.setText(R.string.hm_hlxs_txt_177);

        hmRightBtn = (ImageButton) findViewById(R.id.bar_right_img);
        hmRightBtn.setVisibility(View.GONE);

        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.INVISIBLE);

        listView = (XtomListView) findViewById(R.id.cms);
        layout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);

        topView = (FrameLayout) findViewById(R.id.top);
    }

    @Override
    protected void getExras() {
        keyid = getIntent().getStringExtra("keyid");
    }

    @Override
    protected void setListener() {
        //返回
        hmBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        layout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getCommentList(keyid);
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                getCommentList(keyid);
            }
        });
        topView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.smoothScrollToPosition(0);
            }
        });
    }
}
