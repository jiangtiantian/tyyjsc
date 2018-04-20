package com.hemaapp.tyyjsc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hemaapp.tyyjsc.model.TimeInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xtom.frame.XtomActivityManager;
import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.util.XtomTimeUtil;

/**
 * 工具类
 */
public class BaseUtil {
    private static double EARTH_RADIUS = 6378.137;// 地球半径

    /**
     * 退出软件
     *
     * @param context
     */
    public static void exit(Context context) {
        XtomActivityManager.finishAll();
    }

    /**
     * 转换时间显示形式(与当前系统时间比较),在显示即时聊天的时间时使用
     *
     * @param time 时间字符串
     * @return String
     */
    public static String transTimeChat(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault());
            String current = XtomTimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");
            String dian24 = XtomTimeUtil.TransTime(current, "yyyy-MM-dd")
                    + " 24:00:00";
            String dian00 = XtomTimeUtil.TransTime(current, "yyyy-MM-dd")
                    + " 00:00:00";
            Date now = null;
            Date date = null;
            Date d24 = null;
            Date d00 = null;
            try {
                now = sdf.parse(current); // 将当前时间转化为日期
                date = sdf.parse(time); // 将传入的时间参数转化为日期
                d24 = sdf.parse(dian24);
                d00 = sdf.parse(dian00);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long diff = now.getTime() - date.getTime(); // 获取二者之间的时间差值
            long min = diff / (60 * 1000);
            if (min <= 5)
                return "刚刚";
            if (min < 60)
                return min + "分钟前";

            if (now.getTime() <= d24.getTime()
                    && date.getTime() >= d00.getTime())
                return "今天" + XtomTimeUtil.TransTime(time, "HH:mm");

            int sendYear = Integer
                    .valueOf(XtomTimeUtil.TransTime(time, "yyyy"));
            int nowYear = Integer.valueOf(XtomTimeUtil.TransTime(current,
                    "yyyy"));
            if (sendYear < nowYear)
                return XtomTimeUtil.TransTime(time, "yyyy-MM-dd HH:mm");
            else
                return XtomTimeUtil.TransTime(time, "MM-dd HH:mm");
        } catch (Exception e) {
            return null;
        }

    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 计算两点间的距离
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static Double GetDistance(double lat1, double lng1, double lat2,
                                     double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000.0;
        return s;
    }

    public static String transDistance(double distance) {
        String ds = "";

        if (distance < 1) {
            ds = String.format(Locale.getDefault(), "%.2f", distance * 1000)
                    + "m";
        } else {
            double km = distance;
            ds = (String.format(Locale.getDefault(), "%.2f", km) + "km");
        }
        return ds;
    }

    public static String transDistance1(double distance) {
        String ds = "";
        ds = (String.format(Locale.getDefault(), "%.2f", distance) + "m");
        return ds;
    }

    public static String transDuration(long duration) {
        String ds = "";
        long min = duration / 60;
        if (min < 60) {
            ds += (min + "分钟");
        } else {
            long hour = min / 60;
            long rm = min % 60;
            if (rm > 0)
                ds += (hour + "小时" + rm + "分钟");
            else
                ds += (hour + "小时");
        }
        return ds;
    }

    /**
     * 添加删除线
     *
     * @param view
     * @param msg
     */
    public static void setThroughSpan(TextView view, String msg) {
        if (msg.length() > 0) {
            SpannableString ss = new SpannableString(msg);
            StrikethroughSpan span = new StrikethroughSpan();
            ss.setSpan(span, 0, msg.length(),
                    SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
            view.setText(ss);
        }
    }

    /**
     * 添加下划线
     *
     * @param view
     * @param msg
     */

    public static void setUnderLine(TextView view, String msg) {
        SpannableString ss = new SpannableString(msg);
        UnderlineSpan span = new UnderlineSpan();
        ss.setSpan(span, 0, msg.length(),
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        view.setText(ss);
    }

    /**
     * 处理日期格式
     *
     * @param d
     * @return
     */
    @SuppressLint("UseValueOf")
    public static String transString(Date d) {
        if (d != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
                    Locale.getDefault());
            String str = sdf.format(d);
            return str;
        }
        return "";
    }

    public static Date transDate(String date) {
        SimpleDateFormat sdf = null;
        if (date != null) {
            sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                return sdf.parse(date);
            } catch (ParseException e) {
                return null;
            }
        }
        return null;
    }

    // 格式化数据保留两位小数
    public static String transData(double money) {
        DecimalFormat format = new DecimalFormat("##0.00");
        return format.format(money);
    }

    // px dip 转换
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    // 删除空格、制表符、换行符、回车符
    public static String replaceBlank(String content) {

        String reg0 = "\\s+|\t+|\r+|\n+";// 识别空格、制表符、换行符、回车符 + 代表至少存在一个
        String reg1 = "\\?{3,}";// 识别一个问号后面存在至少三个问号的字符串
        Pattern p = Pattern.compile(reg0);
        Matcher m = p.matcher(content);

        String str = m.replaceAll(" ");
        p = Pattern.compile(reg1);
        m = p.matcher(str);
        return m.replaceAll("");
    }

    //重新计算ListView高度,适配器根目录必须为线性布局
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    // 判断某个路径是本地路径还是网络路径
    public static boolean isLocalPath(String path) {
        StringBuffer header = new StringBuffer();
        Enumeration<NetworkInterface> en;
        try {
            en = NetworkInterface.getNetworkInterfaces();
            while (en != null && en.hasMoreElements()) {
                NetworkInterface ni = en.nextElement();
                Enumeration<InetAddress> enAddr = ni.getInetAddresses();
                while (enAddr.hasMoreElements()) {
                    header.append("http://" + enAddr.nextElement());
                    header.append("|");
                }
            }
            header.append("http://" + "localhost");
            //用正则表达式和字符串的相关函数进行判断
            Pattern p = Pattern.compile(header.toString());
            Matcher m = p.matcher(path);
            return m.find();
        } catch (SocketException e) {
        }
        return false;
    }

    /**
     * 判断服务器时间是否处于开始时间和结束时间之间
     *
     * @param start_time
     * @param end_time
     * @param service_time
     * @return 抢购中 0 服务器时间介于开始和结束时间之间; 即将开始-1 服务器时间小于开始时间；已结束1 服务器时间大于结束时间
     */
    public static int compareDate(String start_time, String end_time, String service_time) {


        String serviceymd = null;
        try {
            serviceymd = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd").parse(service_time));//获取服务器年月日
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date startTime = format.parse(serviceymd + " " + start_time);
            Date endTime = format.parse(serviceymd + " " + end_time);
            Date serviceTime = format.parse(service_time);//服务器时间

            if (startTime.getTime() <= serviceTime.getTime() && serviceTime.getTime() <= endTime.getTime()) {//处于开始和结束之间
                return 0;
            } else if (serviceTime.getTime() < startTime.getTime()) {
                return -1;
            } else if (serviceTime.getTime() > endTime.getTime()) {
                return 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -2;
    }

    //格式化时间，时间格式为：yyyy-MM-dd hh:ss:mm
    public static Date formatDate(String time, String service_time) {
        String serviceymd = null;
        try {
            serviceymd = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd").parse(service_time));//获取服务器年月日
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(serviceymd + " " + time);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //重新排列时间:冒泡排序
    public static HashMap<Integer, TimeInfo> bubble_sort(HashMap<Integer, TimeInfo> unsorted, String service_time) {
        for (int i = 0; i < unsorted.size(); i++) {
            for (int j = i; j < unsorted.size(); j++) {
                if (formatDate(unsorted.get(i).getStart_time(), service_time).getTime() > formatDate(unsorted.get(j).getStart_time(), service_time).getTime()) {
                    TimeInfo temp = unsorted.get(i);
                    unsorted.put(i, unsorted.get(j));
                    unsorted.put(j, temp);
                }
            }
        }
        return unsorted;
    }

    /**
     * 产生display的配置
     * 没有加载过程的图
     *
     * @return
     */
    public static DisplayImageOptions displayImageOption(int iddefault) {
//        // TODO Auto-generated method stub
//        DisplayImageOptions options;
//        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(iddefault) //设置图片在下载期间显示的图片
//                .showImageForEmptyUri(iddefault)//设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(iddefault)  //设置图片加载/解码过程中错误时候显示的图片
//                .cacheInMemory(false)//设置下载的图片是否缓存在内存中
//                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
//                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
//                .imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示
////			.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
////			.decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//设置图片的解码配置
//                //.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
//                //设置图片加入缓存前，对bitmap进行设置
//                //.preProcessor(BitmapProcessor preProcessor)
//                .resetViewBeforeLoading(false)//设置图片在下载前是否重置，复位
////			.displayer(new FadeInBitmapDisplayer(200))//是否图片加载好后渐入的动画时间
//                .displayer(new SimpleBitmapDisplayer())//解决重新加载图片闪烁 问题
//                .resetViewBeforeLoading(false)
//                .build();//构建完成
//        return options;
        return new DisplayImageOptions.Builder()
                .showImageForEmptyUri(iddefault)
                .resetViewBeforeLoading(true).cacheInMemory(true).showImageForEmptyUri(iddefault)
                .cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

    }

//    public DisplayImageOptions getOptions(int drawableId) {
//        return new DisplayImageOptions.Builder().showImageOnLoading(drawableId)
//                .showImageForEmptyUri(drawableId).showImageOnFail(drawableId)
//                .resetViewBeforeLoading(true).cacheInMemory(true)
//                .cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
//                .bitmapConfig(Bitmap.Config.RGB_565).build();
//    }


    /**
     * 产生display的配置
     * 没有加载过程的图
     *
     * @return
     */
    public static DisplayImageOptions displayImageOption() {
//        // TODO Auto-generated method stub
//        DisplayImageOptions options;
//        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.hm_icon_def) //设置图片在下载期间显示的图片
//                .showImageForEmptyUri(R.mipmap.hm_icon_def)//设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.mipmap.hm_icon_def)  //设置图片加载/解码过程中错误时候显示的图片
//                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
//                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
//                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
//                .imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示
////			.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
////			.decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//设置图片的解码配置
//                //.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
//                //设置图片加入缓存前，对bitmap进行设置
//                //.preProcessor(BitmapProcessor preProcessor)
//                .resetViewBeforeLoading(false)//设置图片在下载前是否重置，复位
//                .displayer(new SimpleBitmapDisplayer())//是否图片加载好后渐入的动画时间
//                .resetViewBeforeLoading(false)
//                .build();//构建完成

        return new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.hm_icon_def)
                .showImageOnLoading(R.mipmap.hm_icon_def) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.hm_icon_def)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.hm_icon_def)  //设置图片加载/解码过程中错误时候显示的图片
                .resetViewBeforeLoading(true).cacheInMemory(true)
                .cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new RoundedBitmapDisplayer(50))//是否设置为圆角，弧度为多少
                .displayer(new SimpleBitmapDisplayer())//是否图片加载好后渐入的动画时间
                .bitmapConfig(Bitmap.Config.RGB_565).build();

    }

    public static DisplayImageOptions displayBannerImageOption() {
//        // TODO Auto-generated method stub
//        DisplayImageOptions options;
//        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.hm_banner_def) //设置图片在下载期间显示的图片
//                .showImageForEmptyUri(R.mipmap.hm_banner_def)//设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.mipmap.hm_banner_def)  //设置图片加载/解码过程中错误时候显示的图片
//                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
//                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
//                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
//                .imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示
////			.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
////			.decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//设置图片的解码配置
//                //.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
//                //设置图片加入缓存前，对bitmap进行设置
//                //.preProcessor(BitmapProcessor preProcessor)
//                .resetViewBeforeLoading(false)//设置图片在下载前是否重置，复位
//                .displayer(new SimpleBitmapDisplayer())//是否图片加载好后渐入的动画时间
//                .resetViewBeforeLoading(false)
//                .build();//构建完成
        return new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.hm_banner_def)
                .resetViewBeforeLoading(true).cacheInMemory(true)
                .cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }
    //获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        if (context == null) {
            return 0;
        }
        int result = 0;
        int resourceId = context.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public static String get2double(double data){
        DecimalFormat df2 = new DecimalFormat("###0.00");
        String value = df2.format(data);
        if(data == 0)
            value = "0.00";
        return value;
    }

}
