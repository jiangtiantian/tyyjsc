package com.hemaapp.tyyjsc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseConfig;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.adapters.ChatAdapter;
import com.hemaapp.tyyjsc.model.Notice;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.DealMsgPop;
import com.hemaapp.tyyjsc.view.DelSlideListView;

import java.util.ArrayList;

import xtom.frame.XtomActivityManager;
import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomRefreshLoadmoreLayout;


/**
 * 消息通知列表
 */
public class ActivityNoticeList extends BaseActivity implements View.OnClickListener, ChatAdapter.onDelNoticeListener {
    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧微文字按钮
    private DealMsgPop dealMsgPop = null;
    private DelSlideListView listView = null;
    private ChatAdapter chatAdapter = null;
    private ArrayList<Notice> notices = new ArrayList<>();
    private RefreshLoadmoreLayout layout = null;
    private User user = null;//用户个人信息
    private int page = 0;//页数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_notice_list);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        user=BaseApplication.getInstance().getUser();
        if(user!=null)
        getSysNoticeList();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getSysNoticeList();
    }
    //获取系统通知
    public void getSysNoticeList() {
        user = BaseApplication.getInstance().getUser();
        getNetWorker().getNoticeList(user.getToken(), "1", String.valueOf(page));
    }
    @Override
    protected void findView() {
        //标题
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);
        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmBarNameView.setText("消息");
        hmRightBtn = (ImageButton) findViewById(R.id.bar_right_img);
        hmRightBtn.setVisibility(View.VISIBLE);
        hmRightBtn.setImageResource(R.mipmap.news_set);
        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.INVISIBLE);
        listView = (DelSlideListView) findViewById(R.id.notices);
        layout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
    }
    @Override
    protected void getExras() {
    }
    @Override
    protected void setListener() {
        hmBackBtn.setOnClickListener(this);
        hmRightBtn.setOnClickListener(this);
        layout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getSysNoticeList();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                getSysNoticeList();
            }
        });
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case NOTICE_LIST:
            case NOTICE_SAVEOPERATE:
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case NOTICE_LIST:
            case NOTICE_SAVEOPERATE:
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case NOTICE_LIST:
                String page = hemaNetTask.getParams().get("page");
                int sysPagesize = BaseApplication.getInstance()
                        .getSysInitInfo().getSys_pagesize();
                @SuppressWarnings("unchecked")
                HemaPageArrayResult<Notice> result = (HemaPageArrayResult<Notice>) hemaBaseResult;
                ArrayList<Notice> data = result.getObjects();
                if (page.equals("0")) {
                    layout.refreshSuccess();
                    notices.clear();
                    notices.addAll(data);

                    if (data.size() < sysPagesize) {
                        layout.setLoadmoreable(false);
                    } else {
                        layout.setLoadmoreable(true);
                    }
                } else {
                    layout.loadmoreSuccess();
                    if (data.size() > 0) {
                        notices.addAll(data);
                        if (data.size() < sysPagesize) {
                            layout.setLoadmoreable(false);
                        } else {
                            layout.setLoadmoreable(true);
                        }
                    } else {
                        layout.setLoadmoreable(false);
                        XtomToastUtil.showShortToast(mContext, getString(R.string.hm_hlxs_txt_89));
                    }
                }
                freshData();
                break;
            case NOTICE_SAVEOPERATE:
                /** 1：置为已读
                 *  2:全部已读
                 3：删除单条
                 4：删除全部 (此处服务器会根据keytype智能判断并处理)
                 */
                String operatetype = hemaNetTask.getParams().get("operatetype");
                if ("1".equals(operatetype)) {
                    String id = hemaNetTask.getParams().get("id");
                    for (Notice notice : notices) {
                        if (id.equals(notice.getId())) {
                            notice.setLooktype("2");
                            chatAdapter.notifyDataSetChanged();
                            Intent intent=new Intent(ActivityNoticeList.this,ActivityNoticeInfo.class);
                            intent.putExtra("notice",notice);
                            startActivity(intent);
                            break;
                        }
                    }
                } else if ("2".equals(operatetype)) {//全部置为已读
                    if (notices != null && notices.size() > 0) {
                        for (Notice notice : notices) {
                            notice.setLooktype("2");
                        }
                        if (chatAdapter != null) {
                            chatAdapter.notifyDataSetChanged();
                        }
                    }
                } else if ("3".equals(operatetype)) {
                    String id = hemaNetTask.getParams().get("id");
                    if (notices != null && notices.size() > 0) {
                        for (Notice notice : notices) {
                            if (id.equals(notice.getId())) {
                                notices.remove(notice);
                                break;
                            }
                        }
                    }
                    if (chatAdapter != null) {
                        chatAdapter.notifyDataSetChanged();
                    }
                    showTextDialog("删除成功");
                } else if ("4".equals(operatetype)) {
                    notices.clear();
                    freshData();
                     showTextDialog("清空成功");
                }
                Intent mintent = new Intent();
                mintent.setAction(BaseConfig.BROADCAST_MSG_NUM);
                sendBroadcast(mintent);
                break;
            default:
                break;
        }
    }

    public void freshData() {
        if (chatAdapter == null) {
            chatAdapter = new ChatAdapter(ActivityNoticeList.this, notices);
            chatAdapter.setEmptyString("暂无消息");
            chatAdapter.setListener(this);
            listView.setAdapter(chatAdapter);
        } else {
            chatAdapter.setEmptyString("暂无消息");
            chatAdapter.setData(notices);
            chatAdapter.notifyDataSetChanged();
        }
        chatAdapter.setListener(this);
        //置为已读
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(notices != null && notices.size() > 0){
                    Notice notice = notices.get(position);
                    if("1".equals(notice.getLooktype())){//该消息尚未浏览
                        getNetWorker().noticeSaveoperate(
                                user.getToken(), notices.get(position).getId(), notices.get(position).getKeytype(),"0","1");//置为已读
                    }else
                    {
                        Intent intent=new Intent(ActivityNoticeList.this,ActivityNoticeInfo.class);
                        intent.putExtra("notice",notice);
                        startActivity(intent);
                    }
                }
            }
        });

    }
    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            default:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case NOTICE_LIST:
                String page = hemaNetTask.getParams().get("page");
                if (isNull(page) || "0".equals(page)) {
                    layout.refreshFailed();
                } else {
                    layout.loadmoreFailed();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == hmRightBtn) {
            if (dealMsgPop == null) {
                dealMsgPop = new DealMsgPop(mContext);
                dealMsgPop.setOnConfirmListener(new DealMsgPop.OnClickConfirmListener() {
                    @Override
                    public void onRead() {// 全部设为已读
                        dealMsgPop.dismiss();
                        if (notices != null && notices.size() > 0) {
                            getNetWorker().noticeSaveoperate(
                                    user.getToken(), "", "0","0","2");
                        }
                    }

                    public void onConfirmListener() {// 清空
                        dealMsgPop.dismiss();
                        if (notices != null && notices.size() > 0) {
                            getNetWorker().noticeSaveoperate(
                                    user.getToken(), "", "0","0","4");
                        }
                    }
                });
            }
            dealMsgPop.show();
        } else if (v == hmBackBtn) {
            finish();
        }
    }
    @Override
    public void finish() {
        Activity activityIndex = XtomActivityManager.getActivityByClass(ActivityIndex.class);
        if(activityIndex == null){
            Intent intent = new Intent(mContext, ActivityIndex.class);
            startActivity(intent);
        }
        super.finish();
    }
    @Override
    public void onDealNotice(Notice notice) {
        if (notice != null) {
            listView.reset();
            listView.deleteItem();
            getNetWorker().noticeSaveoperate(
                    user.getToken(), notice.getId(), notice.getKeytype(),"0","3");//删除单条
        }
    }
}
