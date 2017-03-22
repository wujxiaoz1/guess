package com.example.asus.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.asus.activity.LoginActivity;
import com.example.asus.activity.R;
import com.example.asus.bmobbean.User;

import cn.bmob.v3.exception.BmobException;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by yinghao on 2016/12/29.
 * Email：756232212@qq.com
 */

public class BaseActivity extends FragmentActivity {


    //bmob错误码
    public static final int ERROR_CODE_USERNAME_OR_PASSWORD_ERROR = 101;
    public static final int ERROR_CODE_OLD_PASS_INCORRECT = 201;
    public static final int ERROR_CODE_USERNAME_ALREADY_TAKEN = 202;
    public static final int ERROR_CODE_NO_USER_FOUND = 205;
    public static final int ERROR_CODE_NETWORK_TIME_OUT = 9010;
    public static final int ERROR_CODE_UNKNOW_ERROR = 9015; //连接未认证wifi时出现
    public static final int ERROR_CODE_NETWORK_NOT_AVAILABLE = 9016;


    public static boolean waitViewDisplaying;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (waitViewDisplaying) {
                hideProgressbar();
                return true;
            }
            if (getClass().getSimpleName().equals("HomeActivity")) {
                return true;
            }
            finish();
        }
        return false;
    }

    public void logd(String s) {
        Log.d("guess", getClass().getSimpleName() + " : " + s);
    }

    public void loge(String s) {
        Log.e("guess", getClass().getSimpleName() + " : " + s);
    }

    public void showProgressbar() {
        waitViewDisplaying = MyProgressbar.getInstance().show();
        hideSoftInput();
    }

    public void showProgressbarWithText(String text) {
        waitViewDisplaying = MyProgressbar.getInstance().showWithText(text);
        hideSoftInput();
    }

    public void hideProgressbar() {
        waitViewDisplaying = MyProgressbar.getInstance().hide();
    }

    public boolean checkCommonException(BmobException e, Context context) {
        if (e == null) {
            return false;
        }
        if (e.getErrorCode() == ERROR_CODE_NETWORK_NOT_AVAILABLE) {
            MyToast.getInstance().showShortWarn(context, "无网络连接，请检查您的手机网络");
        } else if (e.getErrorCode() == ERROR_CODE_NETWORK_TIME_OUT) {
            MyToast.getInstance().showShortWarn(context, "请求网络超时，请检查您的手机网络");
        } else if (e.getErrorCode() == ERROR_CODE_UNKNOW_ERROR) {
            loge(e.toString());
            MyToast.getInstance().showShortWarn(context, "网络异常");
        } else {
            loge(e.toString());
        }
        return true;
    }

//    public void addActivity(AppCompatActivity activity) {
//        ((BaseApplication) getApplication()).addActivity(activity);
//    }
//
//    public void finishActivity() {
//        ((BaseApplication) getApplication()).finishActivity();
//    }

    @Override
    protected void onStop() {
        hideProgressbar();
        super.onStop();
    }

    @Override
    public void finish() {
        super.finish();
        if (!getClass().getSimpleName().equals("HomeActivity")) {
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
        }
    }

    void hideSoftInput() {
        final View v = this.getWindow().peekDecorView();
        if (v != null && v.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}