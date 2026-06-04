package com.example.pinchaapp;
import android.app.Application;
import com.example.pinchaapp.session.SessionManager;
public class MiApp extends  Application{
@Override
    public  void onCreate(){
    super.onCreate();
    // esto iniciara sharedpreferences
    SessionManager.init(this);
}

}
