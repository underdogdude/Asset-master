package xyz.codecool.android.asset.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    public static String AUTH = "กำลังเข้าสู่ระบบ...";
    public static String REGIS = "กำลังสมัครสมาชิก...";
    public static String LOAD = "กำลังโหลดข้อมูล...";
    public static String VERIFY = "กำลังตรวจสอบ...";

    public SweetAlertDialog pDialog;

    public void showProgressDialog(String message) {

        if (pDialog == null) {
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#03a9f4"));
            pDialog.setContentText("");
            pDialog.setTitleText(message);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
        }

        pDialog.show();
    }

    public void hideProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    public void dialogAlertError(String title, String message) {
        pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        pDialog.setTitleText(title);
        pDialog.setContentText(message);
        pDialog.setConfirmText("ตกลง");
        pDialog.setConfirmClickListener(sDialog -> hideProgressDialog());
        pDialog.show();
    }

    public void dialogAlertWarning(String title, String message) {
        pDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
        pDialog.setTitleText(title);
        pDialog.setContentText(message);
        pDialog.setConfirmText("ตกลง");
        pDialog.setConfirmClickListener(sDialog -> hideProgressDialog());
        pDialog.show();
    }

    public void dialogSuccess(String title, String message) {
        pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(title);
        pDialog.setContentText(message);
        pDialog.setConfirmText("ตกลง");
        pDialog.setConfirmClickListener(sDialog -> hideProgressDialog());
        pDialog.show();
    }

    public void invisibleView(View... views) {
        for (View v : views) {
            v.setVisibility(View.INVISIBLE);
        }
    }

    public void visibleView(View... views) {
        for (View v : views) {
            v.setVisibility(View.VISIBLE);
        }
    }

    public String getAppversion(Activity context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showKeyboardFrom() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
