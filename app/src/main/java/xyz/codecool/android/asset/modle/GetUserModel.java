package xyz.codecool.android.asset.modle;

public class GetUserModel {
    private int status;
    private String message;
    private UserModel result;

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

    public UserModel getResult() {
        return result;
    }

    public void setResult(UserModel result) {
        this.result = result;
    }
}
