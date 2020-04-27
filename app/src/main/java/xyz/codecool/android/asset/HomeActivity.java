package xyz.codecool.android.asset;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jetradarmobile.snowfall.SnowfallView;
import com.ornach.nobobutton.NoboButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.codecool.android.asset.fragment.AssetListFragment;
import xyz.codecool.android.asset.fragment.CountFragment;
import xyz.codecool.android.asset.helper.BaseActivity;
import xyz.codecool.android.asset.okhttp.ApiSetting;

public class HomeActivity extends BaseActivity {

    boolean doubleBackToExitPressedOnce = false;

    String TAG = "HomeActivity";

    @BindView(R.id.txtAppName)
    TextView txtAppName;
    @BindView(R.id.snowView)
    SnowfallView snowView;
    @BindView(R.id.bar)
    CardView bar;
    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.navigationView)
    BottomNavigationView navigationView;
    @BindView(R.id.btnInformation)
    ImageView btnInformation;
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
        content = findViewById(R.id.content);
        btnInformation = findViewById(R.id.btnInformation);
        btnInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBottomSheet();
            }
        });
        setNavigation();
        setPage(1);

    }

    private void setPage(int page) {
        switch (page) {
            case 1:
                navigationView.setSelectedItemId(R.id.navigation_search);
                break;
            case 2:
                navigationView.setSelectedItemId(R.id.navigation_asset);
                break;
        }
    }

    private void setNavigation() {
        navigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_search:
                    setFram(new CountFragment(), "");
                    break;
                case R.id.navigation_asset:
//                    if (navigationView.getSelectedItemId() != R.id.navigation_asset) {
//                        setFram(new AssetListFragment(), "");
//                    }

                    setFram(new AssetListFragment(), "");
                    break;
            }
            return true;
        });
    }

    public void setFram(Fragment fram, String title) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.content, fram);
        ft.commitAllowingStateLoss();
    }

    private void setBottomSheet() {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheets_about, null);
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());

        bottomSheetBehavior.setPeekHeight(1500);

        ImageView imgClose = bottomSheetView.findViewById(R.id.imgClose);
        TextView txtVersion = bottomSheetView.findViewById(R.id.txtVersion);
        NoboButton btnLogout = bottomSheetView.findViewById(R.id.btnLogout);

        txtVersion.setText("v" + BuildConfig.VERSION_NAME);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiSetting setting = new ApiSetting(HomeActivity.this);
                setting.setLogin(false);
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.hide();
            }
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == 1) {
                    bottomSheetDialog.hide();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        bottomSheetDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
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
