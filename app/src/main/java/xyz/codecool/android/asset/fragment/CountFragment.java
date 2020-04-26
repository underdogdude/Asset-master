package xyz.codecool.android.asset.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.ornach.nobobutton.NoboButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import xyz.codecool.android.asset.Camera_QRscan;
import xyz.codecool.android.asset.HomeActivity;
import xyz.codecool.android.asset.R;
import xyz.codecool.android.asset.modle.GetAssetModel;
import xyz.codecool.android.asset.okhttp.ApiClient;
import xyz.codecool.android.asset.okhttp.ApiSetting;
import xyz.codecool.android.asset.okhttp.CallServiceListener;

@SuppressLint("ValidFragment")

public class CountFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.txtInvNumber)
    TextView txtInvNumber;
    @BindView(R.id.txtErpNumber)
    TextView txtErpNumber;
    @BindView(R.id.txtDescription1)
    TextView txtDescription1;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.inputProductNumber)
    EditText inputProductNumber;
    @BindView(R.id.btnCancel)
    TextView btnCancel;
    @BindView(R.id.btnSearchProductID)
    NoboButton btnSearchProductID;
    @BindView(R.id.viewInputProductNumber)
    CardView viewInputProductNumber;
    @BindView(R.id.viewLoadContent)
    LinearLayout viewLoadContent;
    @BindView(R.id.txtAlert)
    TextView txtAlert;
    @BindView(R.id.viewAlertContent)
    LinearLayout viewAlertContent;
    @BindView(R.id.btnQrcode)
    NoboButton btnQrcode;
    @BindView(R.id.btnProductId)
    NoboButton btnProductId;
    @BindView(R.id.viewBtnMenu)
    CardView viewBtnMenu;
    @BindView(R.id.txtTag)
    TextView txtTag;
    @BindView(R.id.chevronTagStatus)
    ImageView chevronTagStatus;
    @BindView(R.id.btnChooseStatus)
    NoboButton btnChooseStatus;
    @BindView(R.id.viewStatus)
    FrameLayout viewStatus;
    @BindView(R.id.btnSaveStatus)
    NoboButton btnSaveStatus;
    @BindView(R.id.viewInputStatus)
    CardView viewInputStatus;
    private int requestCodeStockCount = 1152;
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetBehavior bottomSheetBehavior;
    private GetAssetModel modelActive;
    private int statusSelect = 0;
    private boolean vStatus = false;
    private SweetAlertDialog pDialogSave;

    @SuppressLint("ValidFragment")
    public CountFragment() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCodeStockCount) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra(Camera_QRscan.EXTRA_DATA);
                converData(result);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_count, container, false);
        ButterKnife.bind(this, view);

        YoYo.with(Techniques.FadeOutLeft)
                .duration(300)
                .playOn(viewInputProductNumber);

        YoYo.with(Techniques.FadeOutLeft)
                .duration(300)
                .playOn(viewInputStatus);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        txtTag.setOnClickListener(this);
        btnChooseStatus.setOnClickListener(this);

        btnCancel.setOnClickListener(this);
        btnQrcode.setOnClickListener(this);
        btnSearchProductID.setOnClickListener(this);
        btnProductId.setOnClickListener(this);

        inputProductNumber.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                searchProductID();
                return true;
            }
            return false;
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnChooseStatus:
                dialogChooseStatus();
                break;
            case R.id.btnQrcode:
                checkPermissionCamera();
                break;
            case R.id.btnProductId:
                disableInputStatus();
                enableInputProduct();
                break;
            case R.id.btnSaveStatus:
                setDialogInsert();
                break;
            case R.id.btnCancel:
                disableInputProduct();
                break;
            case R.id.btnSearchProductID:
                searchProductID();
                break;
            case R.id.txtTag:
                if (modelActive != null) {
                    setBottomSheetDetail(modelActive);
                }
                break;
        }
    }

    private void dialogChooseStatus() {
        chevronTagStatus.animate().rotation(180);
        final String[] listItems = {"ใช้งานอยู่", "ชำรุด/เสื่อมสภาพ", "ไม่จำเป็นใช้งาน", "หาไม่พบ"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose item");
        builder.setSingleChoiceItems(listItems, statusSelect, (dialog, which) -> {
            statusSelect = which;
            btnChooseStatus.setText(listItems[which]);
            chevronTagStatus.animate().rotation(0);
            dialog.dismiss();
        });
        builder.setOnCancelListener(dialogInterface -> chevronTagStatus.animate().rotation(0));
        builder.show();
    }

    private void searchProductID() {
        String productNumber = inputProductNumber.getText().toString().trim();
        if (!productNumber.isEmpty()) {
            clearView();
            getProduct(productNumber);
        } else {
            Toast.makeText(getActivity(), "กรุณากรอกรหัสครุภัณฑ์", Toast.LENGTH_SHORT).show();
        }
    }

    private void getProduct(String code) {
        ((HomeActivity) getActivity()).showProgressDialog("กำลังโหลด...");

        ApiSetting api = new ApiSetting(getActivity());
        RequestBody requestBody = new FormBody.Builder()
                .add("code", code)
                .build();
        ApiClient.POST post = new ApiClient.POST();
        post.setURL(api.getBASE_URL("getAsset"));
        post.setRequestBody(requestBody);
        post.execute();
        post.setListenerCallService(new CallServiceListener() {
            @Override
            public void ResultData(String data) {
                parseJsonProduct(data);
            }

            @Override
            public void ResultError(String data) {
                vStatus = false;
                ((HomeActivity) getActivity()).dialogAlertError("เกิดข้อผิดพลาด", data.contains("timeout") ? "หมดเวลาเชื่อมต่อ กรุณาลองใหม่อีกครั้ง" : "กรุณาลองใหม่อีกครั้ง");
            }

            @Override
            public void ResultNull(String data) {
                vStatus = false;
                ((HomeActivity) getActivity()).dialogAlertWarning("ไม่พบข้อมูล", "กรุณาติดต่อทีมพัฒนา");
            }
        });
    }

    private void parseJsonProduct(String json) {
        Gson gson = new Gson();
        GetAssetModel model = gson.fromJson(json, GetAssetModel.class);
        try {
            if (model.getStatus() == 404) {
                vStatus = false;
                ((HomeActivity) getActivity()).dialogAlertWarning("ไม่พบครุภัณฑ์", "");
            } else if (model.getStatus() == 200) {
                updateView(model);
            } else {
                vStatus = false;
                ((HomeActivity) getActivity()).dialogAlertError("เกิดข้อผิดพลาด.", "กรุณาติดต่อทีมพัฒนา");
            }
        } catch (Exception e) {
            ((HomeActivity) getActivity()).dialogAlertError("เกิดข้อผิดพลาด", "JSON ไม่สมบูรณ์กรุณาลองใหม่อีกครั้ง");
        }
    }

    private void clearView() {
        txtInvNumber.setText("");
        txtErpNumber.setText("");
        txtDescription1.setText("");
        txtTag.setVisibility(View.GONE);
        modelActive = null;
        statusSelect = 0;
    }

    private void updateView(GetAssetModel model) {
        txtInvNumber.setText(model.getResult().getInvNumber());
        txtErpNumber.setText(model.getResult().getErpNumber());
        txtDescription1.setText(model.getResult().getDescription1());
        inputProductNumber.setText("");
        viewInputProductNumber.setVisibility(View.GONE);
        txtTag.setVisibility(View.VISIBLE);
        modelActive = model;
        statusSelect = 0;
        enableInputStatus();
        ((HomeActivity) getActivity()).hideProgressDialog();
    }

    private void checkPermissionCamera() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                startActivityForResult(new Intent(getActivity(), Camera_QRscan.class), requestCodeStockCount);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                checkPermissionCamera();
            }
        };

        TedPermission.with(getActivity())
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("จำเป็นต้องเปิดสิทธิ์การใช้งานกล้องถ่ายรูป เพื่อแสกน QR Code")
                .setRationaleConfirmText("ตกลง")
                .setDeniedMessage("กรุณาเปิดสิทธิ์ที่ [Setting] > > [การอนุญาต] > [กล้องถ่ายรูป] > [เปิด]")
                .setGotoSettingButtonText("ตกลง")
                .setPermissions(Manifest.permission.CAMERA)
                .check();
    }

    private void converData(String result) {
        if (result != null) {
            if (result.startsWith("A#")) {
                final String[] strSplit = result.split("#");
                getProduct(strSplit[1]);
                return;
            }
        }

        Toast.makeText(getActivity(), "QR Code format error.", Toast.LENGTH_SHORT).show();
    }

    private void disableInputProduct() {
        inputProductNumber.setEnabled(false);
        btnSearchProductID.setOnClickListener(null);
        btnCancel.setOnClickListener(null);
        YoYo.with(Techniques.FadeOutLeft)
                .duration(300)
                .playOn(viewInputProductNumber);

        if (vStatus) {
            enableInputStatus();
        }
    }

    private void enableInputProduct() {
        inputProductNumber.setText("");
        inputProductNumber.setEnabled(true);
        btnSearchProductID.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        viewInputProductNumber.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeInLeft)
                .duration(300)
                .playOn(viewInputProductNumber);
        inputProductNumber.requestFocus();
        inputProductNumber.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
        inputProductNumber.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
    }

    private void disableInputStatus() {
        statusSelect = 0;
        btnChooseStatus.setText("ใช้งานอยู่");
        viewInputStatus.setVisibility(View.INVISIBLE);
        btnChooseStatus.setOnClickListener(null);
        btnSaveStatus.setOnClickListener(null);
        YoYo.with(Techniques.FadeOutLeft)
                .duration(300)
                .playOn(viewInputStatus);
    }

    private void enableInputStatus() {
        vStatus = true;
        btnChooseStatus.setOnClickListener(this);
        btnSaveStatus.setOnClickListener(this);
        viewInputStatus.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeInLeft)
                .duration(300)
                .playOn(viewInputStatus);
    }

    private void setDialogInsert() {
        if (pDialogSave == null) {
            pDialogSave = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
            pDialogSave.setTitleText("ยืนยันการบันทึก");
            pDialogSave.setContentText("กด 'ตกลง' เพื่อยืนยันการบันทึก");
            pDialogSave.setConfirmText("ตกลง");
            pDialogSave.setConfirmClickListener(sDialog -> {
                sDialog.dismiss();
                pDialogSave = null;
                postSaveStatus();
            });
            pDialogSave.setCancelText("Cancel");
            pDialogSave.showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            pDialogSave = null;
                            sDialog.cancel();
                        }
                    });
            pDialogSave.show();
        }
    }

    private void postSaveStatus() {
        ((HomeActivity) getActivity()).showProgressDialog("กำลังตรวจสอบ...");

        ApiSetting api = new ApiSetting(getActivity());
        RequestBody requestBody = new FormBody.Builder()
                .add("assetId", String.valueOf(modelActive.getResult().getId()))
                .add("statusId", String.valueOf(statusSelect + 1))
                .add("userId", String.valueOf(api.getId()))
                .build();
        ApiClient.POST post = new ApiClient.POST();
        post.setURL(api.getBASE_URL("addCheckAsset"));
        post.setRequestBody(requestBody);
        post.execute();
        post.setListenerCallService(new CallServiceListener() {
            @Override
            public void ResultData(String data) {
                parseJsonStatus(data);
            }

            @Override
            public void ResultError(String data) {
                vStatus = false;
                ((HomeActivity) getActivity()).dialogAlertError("เกิดข้อผิดพลาด", data.contains("timeout") ? "หมดเวลาเชื่อมต่อ กรุณาลองใหม่อีกครั้ง" : "กรุณาลองใหม่อีกครั้ง");
            }

            @Override
            public void ResultNull(String data) {
                vStatus = false;
                ((HomeActivity) getActivity()).dialogAlertWarning("ไม่พบข้อมูล", "กรุณาติดต่อทีมพัฒนา");
            }
        });
    }

    private void parseJsonStatus(String json) {
        Gson gson = new Gson();
        GetAssetModel model = gson.fromJson(json, GetAssetModel.class);

        try {
            if (model.getStatus() == 500) {
                vStatus = false;
                ((HomeActivity) getActivity()).dialogAlertWarning("กรอกข้อมูลไม่ครบ", "");
            } else if (model.getStatus() == 200) {
                statusSelect = 0;
                btnChooseStatus.setText("ใช้งานอยู่");
                ((HomeActivity) getActivity()).dialogSuccess("บันทึกข้อมูลสำเร็จ", "");
            }  else if (model.getStatus() == 403) {
                ((HomeActivity) getActivity()).dialogAlertWarning("ไม่มีสิทธิ์บันทึก", "");
            } else {
                vStatus = false;
                ((HomeActivity) getActivity()).dialogAlertError("เกิดข้อผิดพลาด.", "กรุณาติดต่อทีมพัฒนา");
            }
        } catch (Exception e) {
            ((HomeActivity) getActivity()).dialogAlertError("เกิดข้อผิดพลาด", "JSON ไม่สมบูรณ์กรุณาลองใหม่อีกครั้ง");
        }

    }

    private void setBottomSheetDetail(GetAssetModel model) {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_detail_asset, null);
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());

        bottomSheetBehavior.setPeekHeight(1500);

        ImageView imgClose = bottomSheetView.findViewById(R.id.imgClose);
        TextView txtInvNumber = bottomSheetView.findViewById(R.id.txtInvNumber);
        TextView txtErpNumber = bottomSheetView.findViewById(R.id.txtErpNumber);
        TextView txtDescription1 = bottomSheetView.findViewById(R.id.txtDescription1);
        TextView txtDescription2 = bottomSheetView.findViewById(R.id.txtDescription2);
        TextView txtCode = bottomSheetView.findViewById(R.id.txtCode);
        TextView txtAssetLocation = bottomSheetView.findViewById(R.id.txtAssetLocation);
        TextView txtAssetRoom = bottomSheetView.findViewById(R.id.txtAssetRoom);
        TextView txtAssetPrice = bottomSheetView.findViewById(R.id.txtAssetPrice);
        TextView txtAssetYear = bottomSheetView.findViewById(R.id.txtAssetYear);
        TextView txtUserManage = bottomSheetView.findViewById(R.id.txtUserManage);

        txtInvNumber.setText(model.getResult().getInvNumber());
        txtErpNumber.setText(model.getResult().getErpNumber());
        txtDescription1.setText(model.getResult().getDescription1());
        txtDescription2.setText(model.getResult().getDescription2());
        txtCode.setText(model.getResult().getCode());
        txtAssetLocation.setText(model.getResult().getLocation());
        txtAssetRoom.setText(model.getResult().getRoom());
        txtAssetPrice.setText(model.getResult().getPrice());
        txtAssetYear.setText(model.getResult().getYear());
        txtUserManage.setText(model.getResult().getUserManageName());

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
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return AnimationUtils.loadAnimation(getActivity(),
                enter ? android.R.anim.fade_in : android.R.anim.fade_out);
    }

}