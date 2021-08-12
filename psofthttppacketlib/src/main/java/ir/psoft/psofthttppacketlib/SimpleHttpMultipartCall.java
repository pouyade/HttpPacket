package ir.psoft.psofthttppacketlib;

import android.util.Log;
import android.webkit.MimeTypeMap;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import ir.psoft.psofthttppacketlib.Events.OnError;
import ir.psoft.psofthttppacketlib.Events.OnSuccess;
import ir.psoft.psofthttppacketlib.Events.OnSuccessJson;
import ir.psoft.psofthttppacketlib.Events.OnSuccessJsonArray;
import ir.psoft.psofthttppacketlib.helper.MultipartRequest;

/**
 * Created by pouyadark on 10/28/18.
 */

public class SimpleHttpMultipartCall {
    public static final int POST = StringRequest.Method.POST;
    public static final int GET = StringRequest.Method.GET;

    protected OnSuccess OnSuccessEvent=null;
    protected OnSuccessJson OnSuccessJsonEvent=null;
    protected OnSuccessJsonArray onSuccessJsonArray=null;
    protected OnError OnErrorEvent=null;
    protected String Url=null;
    protected File file=null;
    protected int Method = Request.Method.POST;
    public String stringname;


    public void send(SimpleHttp client){

        String webpath= client.getUrl() + this.Url;

        MultipartRequest strRequest = new MultipartRequest(Method,webpath, getcustomHeaders(), new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data, "UTF-8");
                    if(OnSuccessEvent!=null)OnSuccessEvent.onRegSuccess(jsonString);
                    OnSuccess(jsonString);
                    try {
                        Object json = new JSONTokener(jsonString).nextValue();
                        if (json instanceof JSONObject) {
                            JSONObject jsonObject = (JSONObject) json;
                            if(OnSuccessJsonEvent!=null) OnSuccessJsonEvent.onRegSuccess(jsonObject);
                            OnSuccessJson(jsonObject);
                            //you have an object
                        }else if (json instanceof JSONArray){
                            JSONArray jsonObject = (JSONArray) json;
                            if(onSuccessJsonArray!=null) onSuccessJsonArray.onRegSuccessArray(jsonObject);
                            OnSuccessJsonarray(jsonObject);
                        }
//                            JSONObject jsonObject = new JSONObject(response);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        OnError(null);

                        if(OnErrorEvent!=null) OnErrorEvent.onReqError();
                    }
                    return;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    OnError(null);

                    if(OnErrorEvent!=null) OnErrorEvent.onReqError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        for (Map.Entry<String, String> entry : getFiles().entrySet())
        {
//            String filename =entry.getValue().substring(entry.getValue().lastIndexOf("\\")+1);
            String filename ="image.jpg";
            byte[] fileContent = readfiletoBytes(entry.getValue());
            strRequest.addPart(new MultipartRequest.FilePart(entry.getKey(),getMimeType(entry.getValue()),filename,fileContent));
        }
        for (Map.Entry<String, String> entry : getParamas().entrySet())
        {
            strRequest.addPart(new MultipartRequest.FormPart(entry.getKey(),entry.getValue()));
        }
//        strRequest.setShouldCache(false);
        strRequest.setTag("upload");

        client.getRequestQueue().add(strRequest);
        strRequest.setRetryPolicy(new DefaultRetryPolicy(
                30 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private byte[] readfiletoBytes(String filePath) {
        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {

            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return bytesArray;
    }

    protected Map<String,String> getParamas() {
        return new HashMap<>();
    }
    protected Map<String,String> getFiles() {
        return new HashMap<>();
    }

    protected Map<String,String> getcustomHeaders() {
        return new HashMap<>();
    }


    protected void OnSuccess(String Response){

    }
    protected void OnSuccessJson(JSONObject JsonResponse){

    }
    protected void OnSuccessJsonarray(JSONArray JsonResponse){

    }
    protected void OnError(Exception error){

    }
    private static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}