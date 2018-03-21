package x_ware.com.edl.networking.api;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import x_ware.com.edl.networking.models.GetListModel;
import x_ware.com.edl.networking.models.appointment.AppointmentCheckInModel;
import x_ware.com.edl.networking.models.appointment.AppointmentCheckOutModel;
import x_ware.com.edl.networking.models.appointment.AppointmentEditDetailModel;
import x_ware.com.edl.networking.models.appointment.AppointmentNewModel;
import x_ware.com.edl.networking.models.appointment.AppointmentViewModel;
import io.reactivex.Observable;

/**
 * Created by buneavros on 2/20/18.
 */

public interface IAppointmentAPI {
    @GET("communications")
    Observable<Response<GetListModel<AppointmentViewModel>>> getAppointments(@Query("currentPage") int page);

    @GET("communications")
    Observable<Response<GetListModel<AppointmentViewModel>>> searchAppointments(@Query("currentPage") int page,
                                                                                @Query("search") String search);

    @GET("communications/{id}")
    Observable<Response<AppointmentViewModel>> getAppointment(@Path("id") int id);

    @PUT("communications/{id}/CheckedIn")
    Observable<Response<AppointmentViewModel>> checkIn(@Path("id") int id,
                                                        @Body AppointmentCheckInModel checkInModel);

    @PUT("communications/{id}/CheckedOut")
    Observable<Response<AppointmentViewModel>> checkOut(@Path("id") int id,
                                                       @Body AppointmentCheckOutModel checkOutModel);

    @PUT("communications/{id}/EditDetail")
    Observable<Response<AppointmentViewModel>> editDetail(@Path("id") int id,
                                                        @Body AppointmentEditDetailModel editDetailModel);

    @POST("communications/Create")
    Observable<Response<AppointmentViewModel>> createNewAppointment(@Body AppointmentNewModel newAppointmentModel);


    @POST("communications/{id}")
    Observable<Response<AppointmentViewModel>> postImage(@Path("id") int id,
                                                         @Query("base64String") String base64String,
                                                         @Query("fileName") String fileName);

}
