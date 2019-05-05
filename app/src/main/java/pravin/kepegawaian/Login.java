package pravin.kepegawaian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pravin.kepegawaian.Api.ApiIClient;
import pravin.kepegawaian.Interface.RegisterApi;
import pravin.kepegawaian.Model.Message;
import pravin.kepegawaian.Model.User;
import pravin.kepegawaian.Util.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private ProgressDialog LoadingProgress;
    private SessionManager sessionManager;
    @BindView(R.id.email) EditText TxtEmail;
    @BindView(R.id.password) EditText TxtPassword;

    @OnClick(R.id.BtnLogin) void Login()
    {
        LoadingProgress = new ProgressDialog(this);
        LoadingProgress.setCancelable(false);
        LoadingProgress.setMessage("Loading.......");


        final String email = TxtEmail.getText().toString();
        final String password = TxtPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please Fill Field",Toast.LENGTH_SHORT).show();
        }
        else{
            LoadingProgress.show();
        RegisterApi api = ApiIClient.getClient().create(RegisterApi.class);
        Call<Message> call = api.login(email,password);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                String code = response.body().getCode();
                LoadingProgress.dismiss();
                List<User> users = response.body().getUser();
                try{
                    if (code.equals("200"))
                    {
                        sessionManager = new SessionManager(Login.this);
                        sessionManager.storeLogin(users.get(0).getId_employee(),users.get(0).getName(),users.get(0).getPhoto());
                        Toast.makeText(Login.this,"Login Success",Toast.LENGTH_SHORT).show();
                        Intent Dashboard = new Intent(getApplicationContext(), pravin.kepegawaian.Dashboard.class);
                        startActivity(Dashboard);
                        finish();
                    }
                    else if(code.equals("400"))
                        {
                          Toast.makeText(Login.this,"Email/Password Salah",Toast.LENGTH_SHORT).show();
                        }
                }
                catch (Exception e)
                {
                    e.getMessage();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                LoadingProgress.dismiss();
                Toast.makeText(getApplicationContext(),"Cek Koneksi dan Coba Lagi",Toast.LENGTH_SHORT).show();
            }
        });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }
}
