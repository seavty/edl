package x_ware.com.edl.networking.api;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import x_ware.com.edl.networking.dto.user.UserModel;
import x_ware.com.edl.networking.dto.user.UserLoginDTO;

/**
 * Created by buneavros on 2/27/18.
 */

public interface IUserAPI {

    @POST("users")
    Observable<Response<UserModel>> login(@Body UserLoginDTO userLogin);
}
