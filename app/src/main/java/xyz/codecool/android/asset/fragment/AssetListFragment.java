package xyz.codecool.android.asset.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import xyz.codecool.android.asset.HomeActivity;
import xyz.codecool.android.asset.R;
import xyz.codecool.android.asset.adapter.AdapterAssetList;
import xyz.codecool.android.asset.modle.AssetModel;
import xyz.codecool.android.asset.modle.GetAssetListModel;
import xyz.codecool.android.asset.okhttp.ApiClient;
import xyz.codecool.android.asset.okhttp.ApiSetting;
import xyz.codecool.android.asset.okhttp.CallServiceListener;

public class AssetListFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetBehavior bottomSheetBehavior;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        getAssetList();

        return view;
    }

    private void getAssetList() {
        ((HomeActivity) getActivity()).showProgressDialog("กำลังโหลด...");
        ApiSetting api = new ApiSetting(getActivity());
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(api.getId()))
                .build();

        ApiClient.POST post = new ApiClient.POST();
        post.setURL(api.getBASE_URL("getAssetList"));
        post.setRequestBody(requestBody);
        post.execute();
        post.setListenerCallService(new CallServiceListener() {
            @Override
            public void ResultData(String data) {
                parseJsonAsset(data);
            }

            @Override
            public void ResultError(String data) {
                ((HomeActivity) getActivity()).dialogAlertError("เกิดข้อผิดพลาด", data.contains("timeout") ? "หมดเวลาเชื่อมต่อ กรุณาลองใหม่อีกครั้ง" : "กรุณาลองใหม่อีกครั้ง");
            }

            @Override
            public void ResultNull(String data) {
                ((HomeActivity) getActivity()).dialogAlertWarning("ไม่พบข้อมูล", "กรุณาติดต่อทีมพัฒนา");
            }
        });
    }

    private void parseJsonAsset(String json) {
        Gson gson = new Gson();
        try {
            GetAssetListModel model = gson.fromJson(json, GetAssetListModel.class);

            if (model.getStatus() == 200) {
                ((HomeActivity) getActivity()).hideProgressDialog();
                updateView(model.getResult());
            } else {
                ((HomeActivity) getActivity()).dialogAlertError("เกิดข้อผิดพลาด.", "กรุณาติดต่อทีมพัฒนา");
            }
        } catch (Exception e) {
            ((HomeActivity) getActivity()).dialogAlertError("เกิดข้อผิดพลาด", "JSON ไม่สมบูรณ์กรุณาลองใหม่อีกครั้ง");
        }
    }

    private void updateView(ArrayList<AssetModel> data) {
        AdapterAssetList poList = new AdapterAssetList(getActivity(), data);
        recyclerView.setAdapter(poList);
        poList.SetOnItemClickListener(new AdapterAssetList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                setBottomSheetDetail(data.get(position));
            }
        });
    }

    private void setBottomSheetDetail(AssetModel model) {
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

        txtInvNumber.setText(model.getInvNumber());
        txtErpNumber.setText(model.getErpNumber());
        txtDescription1.setText(model.getDescription1());
        txtDescription2.setText(model.getDescription2());
        txtCode.setText(model.getCode());
        txtAssetLocation.setText(model.getLocation());
        txtAssetRoom.setText(model.getRoom());
        txtAssetPrice.setText(model.getPrice());
        txtAssetYear.setText(model.getYear());
        txtUserManage.setText(model.getUserManageName());

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

}