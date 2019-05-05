package pravin.kepegawaian;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pravin.kepegawaian.Api.ApiIClient;
import pravin.kepegawaian.Interface.RegisterApi;
import pravin.kepegawaian.List.Data_My_Profile;
import pravin.kepegawaian.Model.Message;
import pravin.kepegawaian.Model.User;
import pravin.kepegawaian.Util.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {

    private List<User> MyProfileResult = new ArrayList<>();
    private Data_My_Profile MyProfileAdapter;
    @BindView(R.id.ProfileRecycler) RecyclerView RecyclerProfile;
    @BindView(R.id.ProgressBarProfile) ProgressBar ProgressBarProfile;
    @BindView(R.id.EmpPhoto) CircleImageView ProfileImg;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String,String> photo = sessionManager.getDetailLogin();
        Picasso.with(this).load("http://192.168.43.66:81/hr_management/public/upload/" + photo.get(sessionManager.PHOTO_EMPLOYEE)).fit().centerCrop()
                .into(ProfileImg);

        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MyProfileAdapter = new Data_My_Profile(this,MyProfileResult);
        RecyclerView.LayoutManager ProfileLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        RecyclerProfile.setLayoutManager(ProfileLayoutManager);
        RecyclerProfile.setItemAnimator(new DefaultItemAnimator());
        RecyclerProfile.setAdapter(MyProfileAdapter);

        LoadProfile();
    }


    private void LoadProfile()
    {
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String,String> id_pegawai = sessionManager.getDetailLogin();

        RegisterApi api = ApiIClient.getClient().create(RegisterApi.class);
        Call<Message> call = api.my_profile(id_pegawai.get(sessionManager.KEY_ID_EMPLOYEE));
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                String code = response.body().getCode();
                ProgressBarProfile.setVisibility(View.GONE);
                if (code.equals("200"))
                {
                    MyProfileResult = response.body().getUser();
                    MyProfileAdapter = new Data_My_Profile(Profile.this,MyProfileResult);
                    RecyclerProfile.setAdapter(MyProfileAdapter);
                }else{
                    Toast.makeText(getApplicationContext(),"Gagal",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
