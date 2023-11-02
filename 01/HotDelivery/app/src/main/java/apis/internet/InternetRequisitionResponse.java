package apis.internet;

public interface InternetRequisitionResponse {

    public void onLoading(boolean loading);


    public void onSucess(Object object);


    public void onError(Exception e);
}
