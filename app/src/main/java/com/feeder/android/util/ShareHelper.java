package com.feeder.android.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.feeder.model.Article;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.instapaper.Instapaper;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 12/7/16
 */

public class ShareHelper {
    private Activity mActivity;
    private Bitmap mDefaultBitmap;

    public ShareHelper(Activity activity) {
        mActivity = activity;
        mDefaultBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_launcher);
    }

    private PlatformActionListener mPlatformActionListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            showToast(mActivity.getString(R.string.share_complete));
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            showToast(mActivity.getString(R.string.share_failed));
        }

        @Override
        public void onCancel(Platform platform, int i) {
            showToast(mActivity.getString(R.string.share_cancel));
        }
    };

    public void shareToWechat(Article article) {
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(article.getTitle());
        sp.setText(HtmlUtil.getOptimizedDesc(article.getDescription()));
        sp.setUrl(article.getLink());
        // TODO: 12/7/16 how to get source
//        FeedSource feedSource = mFeedItem.getFeedSource();
//        if (feedSource != null
//                && !TextUtils.isEmpty(feedSource.getFavicon())) {
//            sp.setImageUrl(feedSource.getFavicon());
//        } else if (mDefaultBitmap != null) {
//            sp.setImageData(mDefaultBitmap);
//        }
        sp.setImageData(mDefaultBitmap);

        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        platform.setPlatformActionListener(mPlatformActionListener);
        platform.share(sp);
    }

    public void shareToMoment(Article article) {
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Wechat.SHARE_WEBPAGE);
        sp.setTitle(article.getTitle());
        sp.setText(HtmlUtil.getOptimizedDesc(article.getDescription()));
        sp.setUrl(article.getLink());
//        FeedSource feedSource = mFeedItem.getFeedSource();
//        if (feedSource != null
//                && !TextUtils.isEmpty(feedSource.getFavicon())) {
//            sp.setImageUrl(feedSource.getFavicon());
//        } else if (mDefaultBitmap != null) {
//            sp.setImageData(mDefaultBitmap);
//        }
        sp.setImageData(mDefaultBitmap);

        Platform platform = ShareSDK.getPlatform(WechatMoments.NAME);
        platform.setPlatformActionListener(mPlatformActionListener);
        platform.share(sp);
    }

    public void shareToWeibo(Article article) {
        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setText(getPureTextShare(article));

        Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
        platform.setPlatformActionListener(mPlatformActionListener);
        platform.share(sp);
    }

    public void shareToInstapaper(Article article) {
        Instapaper.ShareParams sp = new Instapaper.ShareParams();
        sp.setTitle(article.getTitle());
        sp.setText(HtmlUtil.getOptimizedDesc(article.getDescription()));
        sp.setUrl(article.getLink());

        Platform platform = ShareSDK.getPlatform(Instapaper.NAME);
        platform.setPlatformActionListener(mPlatformActionListener);
        platform.share(sp);
    }

    private String getPureTextShare(Article article) {
        String shareText = article.getTitle()
                + " "
                + article.getLink()
                + " Shared by Feeder app";
        return shareText;
    }

    private void showToast(String toast) {
        Toast.makeText(mActivity, toast, Toast.LENGTH_SHORT).show();
    }
}
