package pravin.kepegawaian.Interface;

import java.lang.ref.ReferenceQueue;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pravin.kepegawaian.Model.Message;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RegisterApi {

    @FormUrlEncoded
    @POST("login")
    Call<Message> login(@Field("email") String email,@Field("password") String password);

    @Multipart
    @POST("forgot_absent")
    Call<Message> forgot_absent(@Part MultipartBody.Part attachment,
                                @Part("date")RequestBody date,
                                @Part("id_employee") RequestBody id_employee,
                                @Part("statement") RequestBody statement
                                );
    @GET("get_reason")
    Call<Message> get_reason();

    @Multipart
    @POST("not_present")
    Call<Message> not_present(@Part MultipartBody.Part attachment,
                              @Part ("id_employee") RequestBody id_employee,
                              @Part ("reason") RequestBody reason,
                              @Part ("statement") RequestBody statement,
                              @Part ("date_start") RequestBody date_start,
                              @Part ("date_end") RequestBody date_end,
                              @Part ("destination_address") RequestBody destination_address
                              );

    @GET("my_profile/{id}")
    Call<Message> my_profile(@Path("id") String id);
}
