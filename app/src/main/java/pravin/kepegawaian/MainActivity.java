package pravin.kepegawaian;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pravin.kepegawaian.Util.SessionManager;

public class MainActivity extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sessionManager = new SessionManager(MainActivity.this);
                sessionManager.checkLogin();
                finish();
            }
        },4000);
    }
}
