package pravin.kepegawaian;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pravin.kepegawaian.Api.ApiIClient;
import pravin.kepegawaian.Interface.RegisterApi;
import pravin.kepegawaian.Model.Message;
import pravin.kepegawaian.Model.Reason;
import pravin.kepegawaian.Util.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cuti extends AppCompatActivity {

    ProgressDialog progressDialog;
    SessionManager sessionManager;
    final Calendar myCalendar1 = Calendar.getInstance();
    final Calendar myCalendar2 = Calendar.getInstance();
    private static final int FILE_SELECT_CODE = 0;
    String mediaPath;

    @BindView(R.id.NamaPegawai) EditText TxtNamaPegawai;
    @BindView(R.id.Alasan) Spinner TxtAlasan;
    @BindView(R.id.Keterangan) EditText TxtKeterangan;
    @BindView(R.id.TglMulai) EditText TxtTglMulai;
    @BindView(R.id.TglBerakhir) EditText TxtTglBerakhir;
    @BindView(R.id.Alamat) EditText TxtAlamat;
    @OnClick(R.id.BtnChooseFile) void ChooseFile()
    {
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
    @BindView(R.id.NamaFile) TextView TxtNamaFile;
    @OnClick(R.id.BtnSimpan) void SimpanCuti()
    {
        if(TxtNamaPegawai.getText().toString().isEmpty() || TxtAlasan.getSelectedItem().toString().isEmpty() || TxtKeterangan.getText()
                .toString().isEmpty() || TxtTglMulai.getText().toString().isEmpty() || TxtTglBerakhir.getText().toString().isEmpty() ||
                TxtAlamat.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Isi Semua Kolom !",Toast.LENGTH_SHORT).show();
        }
        else if(mediaPath == null)
            {
                Toast.makeText(getApplicationContext(),"Silahkan Pilih File",Toast.LENGTH_SHORT).show();
            }
            else
                {
                    progressDialog.setMessage("Mengirim Data......");
                    progressDialog.show();
                    progressDialog.setCancelable(false);

                    File file = new File(mediaPath);

                    SessionManager sessionManager = new SessionManager(Cuti.this);
                    HashMap<String,String> id_peg = sessionManager.getDetailLogin();

                    RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"),file);
                    MultipartBody.Part fileToUpload = okhttp3.MultipartBody.Part.createFormData("attachment",file.getName(),requestBody);
                    RequestBody id_pegawai = RequestBody.create(MediaType.parse("text/plain"),id_peg.get(sessionManager.KEY_ID_EMPLOYEE));
                    RequestBody statement = RequestBody.create(MediaType.parse("text/plain"),TxtKeterangan.getText().toString());
                    RequestBody date_start = RequestBody.create(MediaType.parse("text/plain"),TxtTglMulai.getText().toString());
                    RequestBody date_end = RequestBody.create(MediaType.parse("text/plain"),TxtTglBerakhir.getText().toString());
                    RequestBody destination_address = RequestBody.create(MediaType.parse("text/plain"),TxtAlamat.getText().toString());
                    RequestBody reason = RequestBody.create(MediaType.parse("text/plain"),TxtAlasan.getSelectedItem().toString());

                    RegisterApi api = ApiIClient.getClient().create(RegisterApi.class);
                    Call<Message> call = api.not_present(fileToUpload,id_pegawai,reason,statement,date_start,date_end,destination_address);
                    call.enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            String code = response.body().getCode();
                            progressDialog.dismiss();
                            if (code.equals("200"))
                            {
                                Toast.makeText(getApplicationContext(),"Berhasil Input Data",Toast.LENGTH_SHORT).show();
                                ClearData();
                            }else if (code.equals("401"))
                                {
                                Toast.makeText(getApplicationContext(),"Ukuran File Terlalu Besar",Toast.LENGTH_SHORT).show();
                                }
                                else
                                    {
                                        Toast.makeText(getApplicationContext(),"Gagal Input Data,Error 400",Toast.LENGTH_SHORT).show();
                                    }
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Koneksi Error,Coba Lagi",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuti);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Pengajuan Cuti");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);

        TxtAlasan.setSelection(0,false);

        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar1.set(Calendar.YEAR,year);
                myCalendar1.set(Calendar.MONTH,month);
                myCalendar1.set(Calendar.DAY_OF_MONTH,day);
                datePicker.setMaxDate(System.currentTimeMillis());
                updateLabelTglMulai();
            }
        };

        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar2.set(Calendar.YEAR,year);
                myCalendar2.set(Calendar.MONTH,month);
                myCalendar2.set(Calendar.DAY_OF_MONTH,day);
                datePicker.setMaxDate(System.currentTimeMillis());
                updateLabelTglBerakhir();

            }
        };

        TxtTglMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Cuti.this,date1,myCalendar1.get(Calendar.YEAR),
                        myCalendar1.get(Calendar.MONTH),myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        TxtTglBerakhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Cuti.this,date2,myCalendar2.get(Calendar.YEAR),
                        myCalendar2.get(Calendar.MONTH),myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        getAlasan();
        GetNameEmployee();
    }

    private void updateLabelTglMulai()
    {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        TxtTglMulai.setText(sdf.format(myCalendar1.getTime()));
    }

    private void updateLabelTglBerakhir()
    {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        TxtTglBerakhir.setText(sdf.format(myCalendar2.getTime()));
    }

    private void GetNameEmployee()
    {
        sessionManager = new SessionManager(Cuti.this);
        HashMap<String,String> Nampeg = sessionManager.getDetailLogin();
        TxtNamaPegawai.setText(Nampeg.get(sessionManager.NAME_EMPLOYEE));
    }

    private void getAlasan() {
        progressDialog.setMessage("Mengambil Data Cuti.....");
        progressDialog.show();
        progressDialog.setCancelable(false);
        RegisterApi api = ApiIClient.getClient().create(RegisterApi.class);
        Call<Message> call = api.get_reason();
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                progressDialog.dismiss();
                String code = response.body().getCode();
                if (code.equals("200"))
                {
                List<Reason> DataAlasan = response.body().getReason();
                List<String> ListAlasan = new ArrayList<String>();
                for (int i = 0 ; i < DataAlasan.size(); i++ )
                {
                    ListAlasan.add(DataAlasan.get(i).getReason());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item,
                        ListAlasan);
                adapter.setDropDownViewResource(R.layout.spinner_item);
                TxtAlasan.setAdapter(adapter);
                }
                else
                    {
                        Toast.makeText(getApplicationContext(),"Gagal Mengambil Data",Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Koneksi Gagal Coba Lagi",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
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

    private void ClearData()
    {
        TxtAlasan.setSelection(0,false);
        TxtKeterangan.setText("");
        TxtTglMulai.setText("");
        TxtTglBerakhir.setText("");
        TxtAlamat.setText("");
        TxtNamaFile.setText("No File Choosen");
        mediaPath = null;
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
