package ir.psoft.psofthttppacketlib;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by pouyadark on 10/27/18.
 */

public class SimpleHttp extends Application {
    private Context context;
    private String url;
    private RequestQueue mRequestQueue;

    public SimpleHttp(Context context, String url) {
        this.context = context;
        this.url = url;
        Setting._context=context;
    }

    public String getUrl() {
        return url;
    }

    public void setApi(String api){
        Setting.setApi(api);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }

        return mRequestQueue;
    }
    public void SendRequest(SimpleHttpCall call){
        if(call!=null){
            call.send(this);
        }
    }
    public void SendRequest(SimpleHttpMultipartCall call){
        if(call!=null){
            call.send(this);
        }
    }
    public Context getInstance() {
        return context;
    }

    public void initiate(Context contextx,String ServerBaseUrl) {
        url = ServerBaseUrl;
        context = contextx;
    }
}
