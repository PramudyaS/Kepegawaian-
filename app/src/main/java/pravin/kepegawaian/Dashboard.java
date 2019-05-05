package pravin.kepegawaian;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pravin.kepegawaian.Util.SessionManager;

public class Dashboard extends AppCompatActivity {

    SessionManager sessionManager;
    @OnClick(R.id.BtnCuti) void Cuti()
    {
        Intent CutiIntent = new Intent(getApplicationContext(),Cuti.class);
        startActivity(CutiIntent);
    }
    @OnClick(R.id.BtnLupaAbsen) void Lupa()
    {
        Intent LupaAbsen = new Intent(getApplicationContext(),LupaAbsen.class);
        startActivity(LupaAbsen);
    }
    @OnClick(R.id.BtnProfile) void MyProfile()
    {
        Intent ProfileIntent = new Intent(getApplicationContext(),Profile.class);
        startActivity(ProfileIntent);
    }
    @OnClick(R.id.BtnLogout) void Logout()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Apakah Anda Yakin ?");
            alertDialog.setItems(R.array.ChooseOptionLogout, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i){
                        case 0 :
                            sessionManager = new SessionManager(Dashboard.this);
                            sessionManager.Logout();
                            Intent login = new Intent(Dashboard.this,Login.class);
                            login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(login);
                            finish();
                            break;
                        default:break;
                    }
                }
            });
            AlertDialog dialogInterface = alertDialog.create();
            dialogInterface.show();

    }

    @BindView(R.id.DisplayName) TextView  Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String,String> MyName = sessionManager.getDetailLogin();
        Name.setText(MyName.get(sessionManager.NAME_EMPLOYEE));
    }
}
