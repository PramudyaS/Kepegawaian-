package pravin.kepegawaian;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pravin.kepegawaian.Api.ApiIClient;
import pravin.kepegawaian.Interface.RegisterApi;
import pravin.kepegawaian.Model.Message;
import pravin.kepegawaian.Util.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LupaAbsen extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    String mediaPath;
    SessionManager sessionManager;
    ProgressDialog progressDialog;

    @BindView(R.id.NamaPegawai) EditText TxtNamaPegawai;
    @BindView(R.id.TanggalLupaAbsen) EditText TxtTglLupaAbsen;
    @OnClick(R.id.BtnChooseFile) void PickFile(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Pilih File");
        alertDialog.setItems(R.array.ChooseOptionFile, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
//                        Intent DocIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                        DocIntent.addCategory(Intent.CATEGORY_OPENABLE);
//                        DocIntent.setType("*/*");
//                        String mimeTypes[] = {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document","application/pdf"};
//                        DocIntent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
//                        startActivityForResult(DocIntent,1);
                        Intent DocIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        DocIntent.addCategory(Intent.CATEGORY_OPENABLE);
                        DocIntent.setType("*application/*");
                        startActivityForResult(Intent.createChooser(DocIntent,"Pilih File"),1);
                        break;
                    case 1:
                        Intent PicIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(PicIntent,0);
                        break;

                }
            }
        });
        AlertDialog dialogInterface = alertDialog.create();
        dialogInterface.show();
    }
    @BindView(R.id.Keterangan) EditText TxtKeterangan;
    @OnClick(R.id.BtnSimpan) void Tambah_LupaAbsen()
    {
        if (TxtNamaPegawai.getText().toString().isEmpty() || TxtTglLupaAbsen.getText().toString().isEmpty()
                || TxtKeterangan.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Isi Semua Kolom !",Toast.LENGTH_SHORT).show();
        }else if(mediaPath == null){
            Toast.makeText(getApplicationContext(),"Silahkan Pilih File",Toast.LENGTH_SHORT).show();
        }
        else{
        progressDialog.setMessage("Mengirim Data........");
        progressDialog.show();
        progressDialog.setCancelable(false);
        File file = new File(mediaPath);

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String,String> id_peg = sessionManager.getDetailLogin();


        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"),file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("attachment",file.getName(),requestBody);
        RequestBody date = RequestBody.create(MediaType.parse("text/plain"),TxtTglLupaAbsen.getText().toString());
        RequestBody id_pegawai = RequestBody.create(MediaType.parse("text/plain"),id_peg.get(sessionManager.KEY_ID_EMPLOYEE));
        RequestBody Keterangan = RequestBody.create(MediaType.parse("text/plain"),TxtKeterangan.getText().toString());

        RegisterApi api = ApiIClient.getClient().create(RegisterApi.class);
        Call<Message> call = api.forgot_absent(fileToUpload,date,id_pegawai,Keterangan);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                progressDialog.dismiss();
                String code = response.body().getCode();
                try{
                    if (code.equals("200"))
                    {
                        Toast.makeText(getApplicationContext(), "Berhasil Input Data", Toast.LENGTH_SHORT).show();
                        TxtKeterangan.setText("");
                        TxtNamaFile.setText("No File Choosen");
                        TxtTglLupaAbsen.setText("");
                    }else
                        {
                            Toast.makeText(getApplicationContext(),"Gagal Mengirim Data !",Toast.LENGTH_SHORT).show();
                        }
                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Koneksi Error,Coba Lagi",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Koneksi Error,Coba Lagi",Toast.LENGTH_SHORT).show();
            }
        });
        }
    };
    @BindView(R.id.NamaFile) TextView TxtNamaFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_absen);
        ButterKnife.bind(this);
        GetNameEmployee();

        getSupportActionBar().setTitle("Input Lupa Absen");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR,year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                datePicker.setMaxDate(System.currentTimeMillis());
                updateLabel();
            }
        };

        TxtTglLupaAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(LupaAbsen.this,date,myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void updateLabel()
    {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        TxtTglLupaAbsen.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
        if (requestCode == 0 && resultCode == RESULT_OK && null != data)
        {
            Uri selectedFile = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedFile,null,null,null,null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String path = cursor.getString(columnIndex);
            String FileName = path.substring(path.lastIndexOf("/")+1);
            TxtNamaFile.setText(FileName);
            mediaPath = cursor.getString(columnIndex);
            cursor.close();
        }
        else if( requestCode == 1 && resultCode == RESULT_OK && null != data)
        {
            Uri selectedFile = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedFile,null,null,null,null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String path = cursor.getString(columnIndex);
            String FileName = path.substring(path.lastIndexOf("/")+1);
            TxtNamaFile.setText(FileName);
            mediaPath = cursor.getString(columnIndex);
            cursor.close();

        }
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Extensi File Tidak Sesuai !",Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void GetNameEmployee()
    {
        sessionManager = new SessionManager(LupaAbsen.this);
        HashMap<String,String> Nampeg = sessionManager.getDetailLogin();
        TxtNamaPegawai.setText(Nampeg.get(sessionManager.NAME_EMPLOYEE));
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
