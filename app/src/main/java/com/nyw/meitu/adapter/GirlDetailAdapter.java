package com.nyw.meitu.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.king.base.util.SharedPreferencesUtils;
import com.nyw.meitu.Constants;
import com.nyw.meitu.R;
import com.nyw.meitu.model.GirlResult;
import com.nyw.meitu.tool.SharedPreferences;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * @since 2017/1/23
 */
public class GirlDetailAdapter extends BasePagerAdapter<GirlResult.Girl> {

    private static final int REQUEST_CODE_SAVE_IMG = 10;
    private LayoutInflater layoutInflater;
    private PhotoView photoView;
    private List<GirlResult.Girl> list=new ArrayList();
    public GirlDetailAdapter(Context context, List<GirlResult.Girl> listData) {
        super(context,listData);
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final ViewGroup container, final GirlResult.Girl girl, int position) {
        View view = layoutInflater.inflate(R.layout.list_girl_detail_item,container,false);
        photoView = (PhotoView)view.findViewById(R.id.photoView);
        photoView.enable();
        Glide.with(context).load(girl.getUrl()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(photoView);
        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertView("提示", null, "取消", new String[]{"收藏图片","保存图片", "分享图片", "设为壁纸"}, null,
                        context, AlertView.Style.ActionSheet, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        switch (position){
                            case 0:
                                Snackbar.make(photoView,"正在开发中", Snackbar.LENGTH_SHORT).show();
                               /* Glide.with(context).load(girl.getUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        Log.e("收藏", String.valueOf(resource));
                                        SharedPreferencesUtils.put(context, Constants.FIRST_GIRL_URL2, String.valueOf(resource));
                                    }
                                });*/

                                break;
                            case 1:
                                final String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MeiTu/";
                                Glide.with(context).load(girl.getUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        saveBitmap(context,resource,dir,true);
                                        MediaStore.Images.Media.insertImage(context.getContentResolver(), resource, "title", "description");
                                    }
                                });
                                break;
                            case 2:
                               UMImage image =  new UMImage(context,girl.getUrl());
                                new ShareAction((Activity) context)
                                        .withMedia(image)
                                        .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,
                                                SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
                                                SHARE_MEDIA.SMS,SHARE_MEDIA.EMAIL,SHARE_MEDIA.MORE)
                                        .setCallback(shareListener)
                                        .open();
                                break;
                            case 3:
                                Glide.with(context).load(girl.getUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        setAsWallpaper(resource,context);
                                    }
                                });
                                Toast.makeText(context, "主人，正在为你后台设置壁纸", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }).show();
                return false;
            }
        });
        return view;
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(context,"成功了",Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(context,"失败"+t.getMessage(),Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(context,"取消了",Toast.LENGTH_LONG).show();

        }
    };
    /*
    * 设置壁纸
    * */
    public boolean setAsWallpaper(Bitmap bitmap,Context cont) {
        //设置壁纸一行代码搞定；
        try {
            cont.setWallpaper(bitmap);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return true;
    }


    /***
         * 保存bitmap到本地
         * @param context 上下文
         * @param mBitmap
         * @param imageSavePath 文件夹名
         * @param isTimerMillisName 是否按照时间生成图片名
         * @return
          */
            public static String saveBitmap(Context context, Bitmap mBitmap,String imageSavePath,boolean isTimerMillisName) {
                String savePath;
                File filePic;
                try {
                    if (Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        savePath = Environment.getExternalStorageDirectory().getCanonicalPath() + "/"+imageSavePath;
                    } else {
                        savePath = context.getApplicationContext().getFilesDir()
                                .getAbsolutePath()
                                + "/"+imageSavePath;
                    }
                    if(isTimerMillisName){
                        filePic = new File(savePath +"/"+ System.currentTimeMillis() + ".jpg");
                    }else {
                        filePic = new File(savePath +"/" + "share.jpg");
                    }
                    if (!filePic.exists()) {
                        filePic.getParentFile().mkdirs();
                        filePic.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(filePic);
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    Toast.makeText(context, "图片已保存到" + savePath, Toast.LENGTH_SHORT).show();
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return null;
                }
                return filePic.getAbsolutePath();
            }
}
