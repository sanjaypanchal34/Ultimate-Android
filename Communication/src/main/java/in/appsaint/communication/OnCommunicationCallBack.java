package in.appsaint.communication;

import org.json.JSONException;
import org.json.JSONObject;

public interface OnCommunicationCallBack  {
     void onSuccessCallBack(Object tag, JSONObject jsonObject) throws JSONException;

     void onFailCallBack(Object tag, Throwable t);

    void onConnectionChange(boolean b);
}