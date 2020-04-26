package xyz.codecool.android.asset;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import xyz.codecool.android.asset.helper.BaseActivity;
import xyz.codecool.android.asset.okhttp.ApiSetting;

public class SplashScreenActivity extends BaseActivity {


    ImageView imgLogo;
    TextView txtTitle;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_splash);

        imgLogo = findViewById(R.id.imgLogo);
        txtTitle = findViewById(R.id.txtTitle);
        progress = findViewById(R.id.progress);

        invisibleView(imgLogo,txtTitle,progress);

        visibleView(imgLogo);
        YoYo.with(Techniques.BounceIn)
                .duration(200)
                .playOn(imgLogo);

        visibleView(progress);
        YoYo.with(Techniques.BounceInUp)
                .duration(400)
                .playOn(progress);

        visibleView(txtTitle);
        YoYo.with(Techniques.BounceInDown)
                .duration(600)
                .playOn(txtTitle);

        TextPaint paint = txtTitle.getPaint();
        float width = paint.measureText(getString(R.string.app_name));

        Shader textShader = new LinearGradient(0, 0, width, txtTitle.getTextSize(),
                new int[]{
                        Color.parseColor("#F97C3C"),
                        Color.parseColor("#FDB54E"),
                        Color.parseColor("#64B678"),
                        Color.parseColor("#478AEA"),
                        Color.parseColor("#8446CC"),
                }, null, Shader.TileMode.CLAMP);
        txtTitle.getPaint().setShader(textShader);
        txtTitle.setTextColor(Color.parseColor("#F97C3C"));


        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        ApiSetting setting = new ApiSetting(SplashScreenActivity.this);
                        if (setting.getLogin()) {
                            startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        } else {
                            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        }
                    }
                }, 1000);

    }
}
