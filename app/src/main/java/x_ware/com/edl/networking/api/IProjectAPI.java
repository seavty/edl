package x_ware.com.edl.networking.api;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import x_ware.com.edl.networking.models.GetListModel;
import x_ware.com.edl.networking.models.project.ProjectCommunicationViewModel;
import x_ware.com.edl.networking.models.project.ProjectCompanyViewModel;
import x_ware.com.edl.networking.models.project.ProjectSpecificationViewModel;
import x_ware.com.edl.networking.models.project.ProjectViewModel;

/**
 * Created by buneavros on 2/23/18.
 */

public interface IProjectAPI {
    @GET("projects")
    Observable<Response<GetListModel<ProjectViewModel>>> getProjects(@Query("currentPage") int page);


    @GET("projects/{id}/projectcompanies")
    Observable<Response<GetListModel<ProjectCompanyViewModel>>> getProjectCompanies(@Path("id") int id,
                                                                            @Query("currentPage") int page);

    @GET("projects/{id}/projectspecifications")
    Observable<Response<GetListModel<ProjectSpecificationViewModel>>> getProjectSpecifications(@Path("id") int id,
                                                                                               @Query("currentPage") int page);

    @GET("projects/{id}/projectcommunications")
    Observable<Response<GetListModel<ProjectCommunicationViewModel>>> getProjectCommunications(@Path("id") int id,
                                                                                               @Query("currentPage") int page);

    @GET("projects/{id}/companyPrjs")
    Observable<Response<GetListModel<ProjectViewModel>>> getProjectsBoundWithCompany(@Path("id") int id,
                                                                                               @Query("currentPage") int page);
}
