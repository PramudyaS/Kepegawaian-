package pravin.kepegawaian.Util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import pravin.kepegawaian.Dashboard;
import pravin.kepegawaian.Login;

public class SessionManager {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    public static final String KEY_ID_EMPLOYEE = "id_employee";
    public static final String NAME_EMPLOYEE = "name";
    public static final String PHOTO_EMPLOYEE = "photo";
    private static final String is_login = "logginstatus";
    private static final String SHARE_NAME = "loginsession";
    private final int MODE_PRIVATE = 0;
    private Context context;

    public SessionManager(Context context)
    {
        this.context = context;
        sp = context.getSharedPreferences(SHARE_NAME,MODE_PRIVATE);
        editor = sp.edit();
    }

    public void storeLogin(String id_employee,String Employee_Name,String Employee_Photo)
    {
        editor.putBoolean(is_login,true);
        editor.putString(KEY_ID_EMPLOYEE,id_employee);
        editor.putString(NAME_EMPLOYEE,Employee_Name);
        editor.putString(PHOTO_EMPLOYEE,Employee_Photo);
        editor.commit();
    }

    public HashMap getDetailLogin()
    {
        HashMap<String,String> map = new HashMap<>();
        map.put(KEY_ID_EMPLOYEE,sp.getString(KEY_ID_EMPLOYEE,null));
        map.put(NAME_EMPLOYEE,sp.getString(NAME_EMPLOYEE,null));
        map.put(PHOTO_EMPLOYEE,sp.getString(PHOTO_EMPLOYEE,null));
        return map;
    }

    public void checkLogin()
    {
        if(!this.Loggin()){
            Intent Login = new Intent(context, pravin.kepegawaian.Login.class);
            Login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(Login);
        }
        else{
            Intent Dashboard = new Intent(context, pravin.kepegawaian.Dashboard.class);
            Dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Dashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(Dashboard);
        }
    }

    public void Logout()
    {
        editor.clear();
        editor.commit();
    }

    public Boolean Loggin()
    {
        return sp.getBoolean(is_login,false);
    }

}
