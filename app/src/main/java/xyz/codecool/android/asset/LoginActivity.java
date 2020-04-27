package xyz.codecool.android.asset;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import xyz.codecool.android.asset.helper.BaseActivity;
import xyz.codecool.android.asset.modle.GetAssetListModel;
import xyz.codecool.android.asset.modle.GetUserModel;
import xyz.codecool.android.asset.modle.UserModel;
import xyz.codecool.android.asset.okhttp.ApiClient;
import xyz.codecool.android.asset.okhttp.ApiSetting;
import xyz.codecool.android.asset.okhttp.CallServiceListener;

public class LoginActivity extends BaseActivity{

    @BindView(R.id.inputEmail)
    EditText inputEmail;
    @BindView(R.id.inputPassword)
    EditText inputPassword;
    @BindView(R.id.btnSignin)
    AppCompatButton btnSignin;

    private String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ApiSetting apiSetting = new ApiSetting(this);
        apiSetting.setBaseUrlPublic("http://muieassetproject.site/api/");
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            Toast.makeText(getBaseContext(), "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_LONG).show();
            return;
        }

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        btnSignin.setEnabled(false);
        postLogin(email,password);
    }

    private void postLogin(String email, String password) {
        showProgressDialog("กำลังเข้าสู่ระบบ...");
        ApiSetting api = new ApiSetting(this);
        RequestBody requestBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();

        ApiClient.POST post = new ApiClient.POST();
        post.setURL(api.getBASE_URL("login"));
        post.setRequestBody(requestBody);
        post.execute();
        post.setListenerCallService(new CallServiceListener() {
            @Override
            public void ResultData(String data) {
                parseJsonAsset(data);
            }

            @Override
            public void ResultError(String data) {
                btnSignin.setEnabled(true);
                dialogAlertError("เกิดข้อผิดพลาด", data.contains("timeout") ? "หมดเวลาเชื่อมต่อ กรุณาลองใหม่อีกครั้ง" : "กรุณาลองใหม่อีกครั้ง");
            }

            @Override
            public void ResultNull(String data) {
                btnSignin.setEnabled(true);
                dialogAlertWarning("ไม่พบข้อมูล", "กรุณาติดต่อทีมพัฒนา");
            }
        });
    }

    private void parseJsonAsset(String json) {
        Gson gson = new Gson();
        try {
            GetUserModel model = gson.fromJson(json, GetUserModel.class);

            if (model.getStatus() == 200) {
                updateData(model.getResult());
            } else if (model.getStatus() == 201) {
                btnSignin.setEnabled(true);
                dialogAlertWarning("เกิดข้อผิดพลาด.", "บัญชีถูกระงับ");
            } else if (model.getStatus() == 403) {
                btnSignin.setEnabled(true);
                dialogAlertWarning("เกิดข้อผิดพลาด.", "ไม่พบข้อมูล");
            } else if (model.getStatus() == 500) {
                btnSignin.setEnabled(true);
                dialogAlertError("เกิดข้อผิดพลาด.", "กรอกข้อมูลไม่ครบถ้วน");
            } else {
                btnSignin.setEnabled(true);
                dialogAlertError("เกิดข้อผิดพลาด.", "กรุณาติดต่อทีมพัฒนา");
            }
        } catch (Exception e) {
            btnSignin.setEnabled(true);
           dialogAlertError("เกิดข้อผิดพลาด", "JSON ไม่สมบูรณ์กรุณาลองใหม่อีกครั้ง");
        }
    }

    private void updateData(UserModel result) {
        ApiSetting apiSetting = new ApiSetting(this);
        apiSetting.setId(result.getId());
        apiSetting.setName(result.getName());
        apiSetting.setLogin(true);

        startActivity(new Intent(this, HomeActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    private boolean validate() {
        Log.d(TAG, "validate");
        boolean valid = true;

        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("กรุณากรอกอีเมล์");
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            inputPassword.setError("กรุณากรอกรหัสที่มากกว่าหรือเท่ากับ6 ตัว");
            valid = false;
        } else {
            inputPassword.setError(null);
        }
        Log.d(TAG, "validate:" + valid);
        return valid;
    }

    protected void setStatusBarGradiant(Activity activity) {
        Window window = activity.getWindow();
        Drawable background = activity.getResources().getDrawable(R.drawable.gradient_theme);
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
        window.setBackgroundDrawable(background);
    }
}
