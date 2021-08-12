package ir.psoft.psofthttppacketlib;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

import ir.psoft.psofthttppacketlib.Events.OnError;
import ir.psoft.psofthttppacketlib.Events.OnSuccess;
import ir.psoft.psofthttppacketlib.Events.OnSuccessJson;
import ir.psoft.psofthttppacketlib.Events.OnSuccessJsonArray;

/**
 * Created by pouyadark on 10/28/18.
 */

public class SimpleHttpCall {
    public static final int POST = StringRequest.Method.POST;
    public static final int GET = StringRequest.Method.GET;

    protected OnSuccess OnSuccessEvent=null;
    protected OnSuccessJson OnSuccessJsonEvent=null;
    protected OnSuccessJsonArray onSuccessJsonArray=null;
    protected OnError OnErrorEvent=null;
    protected String Url=null;
    protected int Method = Request.Method.POST;
    public void send(SimpleHttp client){

        String webpath= client.getUrl() + this.Url +( Url.contains("?")?"&":"?")+ "time="+ System.currentTimeMillis();

        StringRequest strRequest = new StringRequest(Method, webpath,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // Log.e("response",response);

                        if(OnSuccessEvent!=null)OnSuccessEvent.onRegSuccess(response);
                        OnSuccess(response);

                        try {
                            Object json = new JSONTokener(response).nextValue();
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
                        }
                        return;
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        OnError(error);

                        if(OnErrorEvent!=null) OnErrorEvent.onReqError();
                    }
                })
        {
            @Override
            public byte[] getBody() throws AuthFailureError {
                byte[] body = SimpleHttpCall.this.getBody();
                if(body!=null){
                    return body;
                }
                return super.getBody();
            }

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> x = getParamas();
                if(Setting.getApi()!=null)x.put("api", Setting.getApi());
                return x;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getcustomHeaders();
            }
        };
        strRequest.setShouldCache(false);
        strRequest.setTag("search");
        client.getRequestQueue().add(strRequest);
        strRequest.setRetryPolicy(new DefaultRetryPolicy(
                60 * 1000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    protected byte[] getBody() {
      return null;
    }

    protected Map<String,String> getParamas() {
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

}