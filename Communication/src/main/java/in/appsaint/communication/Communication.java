package in.appsaint.communication;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Communication {

    private final Activity activity;
    private final OnCommunicationCallBack callBack;
    private final boolean instantRetry = false;
    private String headerKey, headerValue;
    private KProgressHUD progressDialog;

    public Communication(@NonNull Activity activity, @NonNull OnCommunicationCallBack callback) {
        this.callBack = callback;
        this.activity = activity;

    }

    private static RequestBody toRequestBody(String value) {
        return RequestBody.create(
                MultipartBody.FORM, value);
    }

    private void loader() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setMaxProgress(100)
                .setSize(60, 60)
                .setCancellable(false)
                .setAnimationSpeed(2);
    }

    private void sendCallBack(Call<String> call, Response<String> response) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        try {
            JSONObject jsonObject = new JSONObject(response.body());
            jsonObject.put("api_req_tag", call.request().tag());
            callBack.onSuccessCallBack(call.request().tag(), jsonObject);
        } catch (JSONException e) {
            callBack.onFailCallBack(call.request().tag(), e);
        }
    }

    private void sendCallError(Call<String> call, Throwable t) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (callBack != null) {
            callBack.onFailCallBack(call.request().tag(), t);
        } else {
            Log.e("ERROR:::", call.request().tag() + "--CallBack Revoked");
        }
    }

    public void callPOST(@NonNull Api url, Object tag, @NonNull HashMap<String, String> param) {
        loader();
        if (progressDialog != null) {
            progressDialog.show();
        }
        RetroInterface service = Client.getRetrofitClient(tag).create(RetroInterface.class);
        Call<String> stringCall = service.getPostAsString(url.toString(), param);
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    sendCallBack(call, response);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        jsonObject.put("api_req_tag", call.request().tag());
                        callBack.onSuccessCallBack(call.request().tag(), jsonObject);
                    } catch (JSONException e) {
                        callBack.onFailCallBack(call.request().tag(), e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                sendCallError(call, t);
            }
        });
    }

    public void callPOST(@NonNull Api url, Object extra, Object tag, @NonNull HashMap<String, String> param, @NonNull HashMap<String, String> header) {
        loader();
        if (progressDialog != null && tag !="SEND_CHAT_MSG" ) {
            progressDialog.show();
        }
        RetroInterface service = Client.getRetrofitClient(tag).create(RetroInterface.class);
        Call<String> stringCall = service.getPostAsStringWithHeader(String.format("%s%s", url, extra), param, header);
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    sendCallBack(call, response);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        jsonObject.put("api_req_tag", call.request().tag());
                        callBack.onSuccessCallBack(call.request().tag(), jsonObject);
                    } catch (JSONException e) {
                        callBack.onFailCallBack(call.request().tag(), e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                sendCallError(call, t);
            }
        });
    }

    public void callGET(@NonNull Api url, Object tag, @NonNull HashMap<String, String> header) {
        loader();
        if (progressDialog != null) {
            progressDialog.show();
        }
        RetroInterface service = Client.getRetrofitClient(tag).create(RetroInterface.class);
        Call<String> stringCall = service.getGetAsStringWithHeader(url.toString(), header);
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    if (callBack != null) {
                        sendCallBack(call, response);
                    } else {
                        Log.e("ERROR:::", call.request().tag() + "--CallBack Revoked");
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        jsonObject.put("api_req_tag", call.request().tag());
                        callBack.onSuccessCallBack(call.request().tag(), jsonObject);
                    } catch (JSONException e) {
                        callBack.onFailCallBack(call.request().tag(), e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                sendCallError(call, t);
            }
        });
    }

    public void callGET(@NonNull Api url, Object extra, Object tag, @NonNull HashMap<String, String> header) {
        loader();
        if (progressDialog != null) {
            progressDialog.show();
        }
        RetroInterface service = Client.getRetrofitClient(tag).create(RetroInterface.class);
        Call<String> stringCall = service.getGetAsStringWithHeader(String.format("%s%s", url, extra), header);
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    if (callBack != null) {
                        sendCallBack(call, response);
                    } else {
                        Log.e("ERROR:::", call.request().tag() + "--CallBack Revoked");
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject("{}");
                        jsonObject.put("api_req_tag", call.request().tag());
                        callBack.onSuccessCallBack(call.request().tag(), jsonObject);
                    } catch (JSONException e) {
                        callBack.onFailCallBack(call.request().tag(), e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                sendCallError(call, t);
            }
        });
    }

    public void callPOST(@NonNull Api url, Object tag, @NonNull HashMap<String, String> param,  @NonNull PART part) {

        loader();
        if (progressDialog != null) {
            progressDialog.show();
        }
        RetroInterface service = Client.getRetrofitClient(tag).create(RetroInterface.class);
        Call<String> stringCall;
        if (part.getFile() == null) {
            stringCall = service.getPostAsStringWithHeader(url.toString(), param, new HashMap<>());
        } else {
            stringCall = service.getPostAsStringWithFile(url.toString(), getParam(param), Params.createMultiPart(part), new HashMap<>());
        }
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    sendCallBack(call, response);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        jsonObject.put("api_req_tag", call.request().tag());
                        callBack.onSuccessCallBack(call.request().tag(), jsonObject);
                    } catch (JSONException e) {
                        callBack.onFailCallBack(call.request().tag(), e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                sendCallError(call, t);
            }
        });

    }
    public void uploadAWS(@NonNull String url, Object tag, File file) {
        String CONTENT_IMAGE = "*/*";

        RequestBody requestFile = RequestBody.create(MediaType.parse(CONTENT_IMAGE), file);

        loader();
        if (progressDialog != null) {
            progressDialog.show();
        }
        RetroInterface service = Client.getRetrofitClient(tag).create(RetroInterface.class);
        Call<String> stringCall;
        stringCall = service.updateImage(url,requestFile);
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("api_req_tag", call.request().tag());
                    callBack.onSuccessCallBack(call.request().tag(), jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                sendCallError(call, t);
            }
        });

    }
    public void callPOST(@NonNull Api url, Object tag, @NonNull HashMap<String, String> param, @NonNull HashMap<String, String> header, @NonNull PART part) {

        loader();
        if (progressDialog != null) {
            progressDialog.show();
        }
        RetroInterface service = Client.getRetrofitClient(tag).create(RetroInterface.class);
        Call<String> stringCall;
        if (part.getFile() == null) {
            stringCall = service.getPostAsStringWithHeader(url.toString(), param, header);
        } else {
            stringCall = service.getPostAsStringWithFile(url.toString(), getParam(param), Params.createMultiPart(part), header);
        }
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    sendCallBack(call, response);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        jsonObject.put("api_req_tag", call.request().tag());
                        callBack.onSuccessCallBack(call.request().tag(), jsonObject);
                    } catch (JSONException e) {
                        callBack.onFailCallBack(call.request().tag(), e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                sendCallError(call, t);
            }
        });

    }
    public void callPOST(@NonNull Api url,Object extra, Object tag, @NonNull HashMap<String, String> param, @NonNull HashMap<String, String> header, @NonNull PART part) {

        loader();
        if (progressDialog != null) {
            progressDialog.show();
        }
        RetroInterface service = Client.getRetrofitClient(tag).create(RetroInterface.class);
        Call<String> stringCall;
        if (part.getFile() == null) {
            stringCall = service.getPostAsStringWithHeader(String.format("%s%s", url, extra), param, header);
        } else {
            stringCall = service.getPostAsStringWithFile(String.format("%s%s", url, extra), getParam(param), Params.createMultiPart(part), header);
        }
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    sendCallBack(call, response);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        jsonObject.put("api_req_tag", call.request().tag());
                        callBack.onSuccessCallBack(call.request().tag(), jsonObject);
                    } catch (JSONException e) {
                        callBack.onFailCallBack(call.request().tag(), e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                sendCallError(call, t);
            }
        });

    }

    private HashMap<String, RequestBody> getParam(HashMap<String, String> param) {
        HashMap<String, RequestBody> tempParam = new HashMap<>();
        for (String key : param.keySet()) {
            tempParam.put(key, toRequestBody(param.get(key)));
        }

        return tempParam;
    }


}
