package xyz.codecool.android.asset.modle;

public class GetAssetModel {
    private int status;
    private String message;
    private AssetModel result;

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

    public AssetModel getResult() {
        return result;
    }

    public void setResult(AssetModel result) {
        this.result = result;
    }
}
