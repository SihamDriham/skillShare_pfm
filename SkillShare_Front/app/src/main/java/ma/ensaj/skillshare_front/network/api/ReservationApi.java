package ma.ensaj.skillshare_front.network.api;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReservationApi {

    @POST("api/reservation/reserver/{idServicePropose}")
    Call<ResponseBody> reserverService(@Path("idServicePropose") int idServicePropose,
                                       @Header("Authorization") String token,
                                       @Body String dateHeure);

    @GET("api/reservation/check/{idServicePropose}")
    Call<Boolean> checkReservation(@Path("idServicePropose") int idServicePropose,
                                   @Header("Authorization") String token);

    @GET("api/reservation/reservations")
    Call<List<Map<String, Object>>> getReservation(
            @Header("Authorization") String token
    );

    @PUT("api/reservation/accepter/{idReser}")
    Call<ResponseBody> accepter(@Path("idReser") int idReser,
                                @Header("Authorization") String token);

    @PUT("api/reservation/refuser/{idReser}")
    Call<ResponseBody> refuser(@Path("idReser") int idReser,
                               @Header("Authorization") String token);


}
