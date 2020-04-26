package xyz.codecool.android.asset.modle;

import java.util.ArrayList;

public class GetAssetListModel {
    private int status;
    private String message;
    private ArrayList<AssetModel> result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<AssetModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<AssetModel> result) {
        this.result = result;
    }
}
