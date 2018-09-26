package com.lftechnology.vtn.sdk.services;


import com.lftechnology.vtn.sdk.dto.Response.AccountStatusResponseDTO;
import com.lftechnology.vtn.sdk.dto.Response.BalanceQueryDTO;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BalanceApiService {


    @POST("RemittanceBalanceStatus.asp")
    @FormUrlEncoded
    Call<BalanceQueryDTO> getBalance(@Field("AccessToken") String accessToken,
                                     @Field("AccessKey") String accessKey);
}
