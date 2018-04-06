package x_ware.com.edl.networking.api;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import x_ware.com.edl.networking.dto.GetListDTO;
import x_ware.com.edl.networking.dto.appointment.AppointmentCheckInDTO;
import x_ware.com.edl.networking.dto.appointment.AppointmentCheckOutDTO;
import x_ware.com.edl.networking.dto.appointment.AppointmentEditDetailDTO;
import x_ware.com.edl.networking.dto.appointment.AppointmentNewDTO;
import x_ware.com.edl.networking.dto.appointment.AppointmentUploadDTO;
import x_ware.com.edl.networking.dto.appointment.AppointmentViewDTO;
import io.reactivex.Observable;

/**
 * Created by buneavros on 2/20/18.
 */

public interface IAppointmentAPI {
    @GET("communications")
    Observable<Response<GetListDTO<AppointmentViewDTO>>> getAppointments(@Query("currentPage") int page);

    @GET("communications")
    Observable<Response<GetListDTO<AppointmentViewDTO>>> searchAppointments(@Query("currentPage") int page,
                                                                            @Query("search") String search);

    @GET("communications/{id}")
    Observable<Response<AppointmentViewDTO>> getAppointment(@Path("id") int id);

    @PUT("communications/{id}/CheckedIn")
    Observable<Response<AppointmentViewDTO>> checkIn(@Path("id") int id,
                                                     @Body AppointmentCheckInDTO checkInModel);

    @PUT("communications/{id}/CheckedOut")
    Observable<Response<AppointmentViewDTO>> checkOut(@Path("id") int id,
                                                      @Body AppointmentCheckOutDTO checkOutModel);

    @PUT("communications/{id}/EditDetail")
    Observable<Response<AppointmentViewDTO>> editDetail(@Path("id") int id,
                                                        @Body AppointmentEditDetailDTO editDetailModel);

    @POST("communications/Create")
    Observable<Response<AppointmentViewDTO>> createNewAppointment(@Body AppointmentNewDTO newAppointmentModel);


    @POST("communications/{id}/upload")
    Observable<Response<Void>> uploadImage(@Path("id") int id, @Body AppointmentUploadDTO appointmentUploadImage);

}
