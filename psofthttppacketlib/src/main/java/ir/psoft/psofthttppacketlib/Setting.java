package ir.psoft.psofthttppacketlib;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pouyadark on 10/28/18.
 */

public class Setting {
    static SharedPreferences pref;
    static SharedPreferences.Editor editor;
    static Context _context;
    private static final String PREF_NAME = "ServerSetting";


    private static void setupSetting() {
        if(pref==null) {
            pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            editor = pref.edit();
        }
    }
    public static String getApi() {
        setupSetting();
        return pref.getString("api",null);
    }
    public static void setApi(String api) {
        setupSetting();
        editor.putString("api", api);
        editor.commit();}
}
