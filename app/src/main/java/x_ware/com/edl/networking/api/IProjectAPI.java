package x_ware.com.edl.networking.api;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import x_ware.com.edl.networking.dto.GetListDTO;
import x_ware.com.edl.networking.dto.project.ProjectCommunicationViewDTO;
import x_ware.com.edl.networking.dto.project.ProjectCompanyViewDTO;
import x_ware.com.edl.networking.dto.project.ProjectSpecificationViewDTO;
import x_ware.com.edl.networking.dto.project.ProjectViewDTO;

/**
 * Created by buneavros on 2/23/18.
 */

public interface IProjectAPI {
    @GET("projects")
    Observable<Response<GetListDTO<ProjectViewDTO>>> getProjects(@Query("currentPage") int page);


    @GET("projects/{id}/projectcompanies")
    Observable<Response<GetListDTO<ProjectCompanyViewDTO>>> getProjectCompanies(@Path("id") int id,
                                                                                @Query("currentPage") int page);

    @GET("projects/{id}/projectspecifications")
    Observable<Response<GetListDTO<ProjectSpecificationViewDTO>>> getProjectSpecifications(@Path("id") int id,
                                                                                           @Query("currentPage") int page);

    @GET("projects/{id}/projectcommunications")
    Observable<Response<GetListDTO<ProjectCommunicationViewDTO>>> getProjectCommunications(@Path("id") int id,
                                                                                           @Query("currentPage") int page);

    @GET("projects/{id}/companyPrjs")
    Observable<Response<GetListDTO<ProjectViewDTO>>> getProjectsBoundWithCompany(@Path("id") int id,
                                                                                 @Query("currentPage") int page);
}
