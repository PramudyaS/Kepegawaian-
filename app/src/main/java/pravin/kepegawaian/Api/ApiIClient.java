package pravin.kepegawaian.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiIClient {

//    public static final String BASE_URL = "http://kitekodein.com/pravin/hr_management/public/api/";
    public static final String BASE_URL = "http://192.168.43.66:81/hr_management/public/api/";
    private static Retrofit retrofit = null;
    public static Retrofit getClient()
    {
        if (retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

}
