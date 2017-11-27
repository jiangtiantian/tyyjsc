package com.hemaapp.tyyjsc.view;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.User;


/**
 * 提示文本框
 */
public class HmHelpDialog implements View.OnClickListener{

    private Dialog mDiglog = null;
    private Context context = null;//上下文

    private View viewConvert = null;//对话框布局

    //场景一： 提示文本框 + 两个按钮
    private TextView mTextViewHint = null;
    //场景二：两个文本框 + 两个按钮
    private TextView mTextViewHintNext = null;
    private LinearLayout profitLayout = null;
    //场景三：支付 一个提示框 + 一个输入框和提示忘记密码文本框
    private EditText pwdInputView = null;
    private TextView forgetView = null;
    private LinearLayout inputLayout = null;
    private onPwdListener pwdListener = null;
    //场景四 性别选择
    private LinearLayout sexLayout = null;
    private TextView boytxt,girltxt;
    private String sex = "男";//默认
    //公用部分
    private Button cancelBtn = null;
    private Button confirmBtn = null;//左右侧按钮
    private OnCancelOrConfirmListener listener = null;
    private int type = 0;//场景类型
    public HmHelpDialog(Context context, int type){
        this.context = context;
        this.type = type;
        viewConvert = LayoutInflater.from(context).inflate(R.layout.help_dialog, null);
        mDiglog = new Dialog(context, R.style.dialog);
        //场景一
        mTextViewHint = (TextView)viewConvert.findViewById(R.id.opt);
        //场景二
        mTextViewHintNext = (TextView)viewConvert.findViewById(R.id.profit);
        profitLayout = (LinearLayout)viewConvert.findViewById(R.id.profitlayout);
        //场景三
        inputLayout = (LinearLayout)viewConvert.findViewById(R.id.inputlayout);
        pwdInputView = (EditText)viewConvert.findViewById(R.id.pwd);
        forgetView = (TextView)viewConvert.findViewById(R.id.forgetpwd);
        //场景四
        sexLayout = (LinearLayout)viewConvert.findViewById(R.id.sexLayout);
        boytxt = (TextView) viewConvert.findViewById(R.id.boy_txt);
        boytxt.setClickable(true);
        girltxt = (TextView) viewConvert.findViewById(R.id.girl_txt);
        cancelBtn = (Button)viewConvert.findViewById(R.id.cancle);
        confirmBtn = (Button)viewConvert.findViewById(R.id.confirm);
        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        resetPanelView();//重设布局
        mDiglog.setCanceledOnTouchOutside(true);
        mDiglog.setCancelable(true);
        Window window = mDiglog.getWindow();
        WindowManager.LayoutParams windowparams = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        Rect rect = new Rect();
        View view = window.getDecorView();
        view.getWindowVisibleDisplayFrame(rect);
        windowparams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(windowparams);

        mDiglog.setContentView(viewConvert);
    }
    //根据不同场景重设不同布局显示
    public void resetPanelView(){
        switch (type){
            case 0://场景一
                mTextViewHint.setVisibility(View.VISIBLE);
                int value = context.getResources().getDimensionPixelSize(R.dimen.margin_60);
                mTextViewHint.setPadding(value, value, value, value);

                profitLayout.setVisibility(View.GONE);
                inputLayout.setVisibility(View.GONE);
                sexLayout.setVisibility(View.GONE);
                break;
            case 1://场景二
                mTextViewHint.setVisibility(View.VISIBLE);
                profitLayout.setVisibility(View.VISIBLE);
                inputLayout.setVisibility(View.GONE);
                sexLayout.setVisibility(View.GONE);
                break;
            case 2://场景三
                inputLayout.setVisibility(View.VISIBLE);
                mTextViewHint.setVisibility(View.VISIBLE);
                forgetView.setOnClickListener(this);
                profitLayout.setVisibility(View.GONE);
                sexLayout.setVisibility(View.GONE);
                break;
            case 3://场景四
                sexLayout.setVisibility(View.VISIBLE);
                inputLayout.setVisibility(View.GONE);
                profitLayout.setVisibility(View.GONE);
                mTextViewHint.setVisibility(View.GONE);
                boytxt.setOnClickListener(this);
                girltxt.setOnClickListener(this);
                break;
            default:
                break;
        }
    }
    //根据是否设置支付或提现密码显示不同的文字
    public void setPayTxt(String str){
        User user= BaseApplication.getInstance().getUser();
        if(user.getPaypassword() == null || "".equals(user.getPaypassword())){
            forgetView.setText("设置"+ str+ "密码");
        }else{
            forgetView.setText("忘记"+ str+ "密码");
        }
    }
    //窗口标题
    public void setTitleName(String txt){
        mTextViewHint.setText(txt);
    }
    public void setTitleName(int resId){
        mTextViewHint.setText(resId);
    }

    //左侧按钮提示文字
    public void setLeftButtonText(String txt){
        cancelBtn.setText(txt);
    }
    public void setLeftButtonText(int resId){
        cancelBtn.setText(resId);
    }
    //右侧按钮显示文字
    public void setRightButtonText(String txt){
        confirmBtn.setText(txt);
    }
    public void setRightButtonText(int resId){
        confirmBtn.setText(resId);
    }
    //密码置为空
    public void clearPwdET(){
        pwdInputView.setText("");
    }
    //设置性别
    public void setCurSex(String sex){
        this.sex = sex;
        if("男".equals(sex)){
            boytxt.setBackgroundColor(context.getResources().getColor(R.color.index_search_bg));
            boytxt.setTextColor(context.getResources().getColor(R.color.white));
            girltxt.setBackgroundColor(context.getResources().getColor(R.color.white));
            girltxt.setTextColor(context.getResources().getColor(R.color.black));
        }else{
            girltxt.setBackgroundColor(context.getResources().getColor(R.color.index_search_bg));
            girltxt.setTextColor(context.getResources().getColor(R.color.white));
            boytxt.setBackgroundColor(context.getResources().getColor(R.color.white));
            boytxt.setTextColor(context.getResources().getColor(R.color.black));
        }
    }

    public void show() {
        mDiglog.show();
    }
    public void cancel() {
        mDiglog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.boy_txt:
               // boyImg.setImageResource(R.mipmap.hm_icon_selector_p);
                sex = "男";
                setCurSex(sex);
                break;
            case R.id.girl_txt:
               // girlImg.setImageResource(R.mipmap.hm_icon_selector_p);
                sex = "女";
                setCurSex(sex);
                break;
            case R.id.forgetpwd:
                User user= BaseApplication.getInstance().getUser();
                if(user.getPaypassword() == null || "".equals(user.getPaypassword())){
                    if(pwdListener != null){
                        cancel();
                        pwdListener.onSetPwd();
                    }
                }else{
                    cancel();
                    pwdListener.onGetPwd();
                }
                break;
            case R.id.cancle:
                if(listener != null){ // 0 关闭弹框不做任何操作
                    if(2 == type){
                        clearPwdInput();
                    }
                    listener.onCancel(type);
                }
                break;
            case R.id.confirm:
                if(listener != null){
                    listener.onConfirm(sex);
                }
                break;
            default:
                break;
        }
    }
    public String getPassword(){
        return pwdInputView.getText().toString().trim();
    }
    public void setListener(OnCancelOrConfirmListener listener){
       this.listener = listener;
    }
    public interface  OnCancelOrConfirmListener{
        void onCancel(int type);
        void onConfirm(String msg);
    }
    public void clearPwdInput(){
        pwdInputView.setText("");
    }
    public void setListener(onPwdListener listener){
        this.pwdListener = listener;
    }
    public interface onPwdListener{
        void onSetPwd();
        void onGetPwd();
    }
}
