package xyz.codecool.android.asset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import xyz.codecool.android.asset.helper.BaseActivity;

public class Camera_QRscan extends BaseActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
    public static final String EXTRA_DATA = "EXTRA_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.setAutoFocus(true);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.setAutoFocus(true);
        mScannerView.startCamera();
    }


    @Override
    public void handleResult(Result rawResult) {
        Log.e("handler", rawResult.getText());

        String string = rawResult.getText();
        final Intent data = new Intent();
        data.putExtra(EXTRA_DATA, string);
        setResult(Activity.RESULT_OK, data);
        finish();
    }
}
