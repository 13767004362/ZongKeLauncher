package com.zongke.hapilolauncher.library.retrofit;


import com.zongke.hapilolauncher.db.entity.ActivityDegreeListBean;
import com.zongke.hapilolauncher.db.entity.DeviceMSG;
import com.zongke.hapilolauncher.db.entity.FunctionList_Entity;
import com.zongke.hapilolauncher.db.entity.UserBean;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by ${xingen} on 2017/7/10.
 *
 * 网络请求的Header,Body,Response。
 *
 */

public interface HapiloService {

    @GET//获取主页数据
    Observable<FunctionList_Entity> getHomeData(@Path("path") String path, @QueryMap Map<String, String> options);

    /**
     * 获取用户信息
     * @param params
     * @return
     */
     @POST("/user/get_my_info")
     Observable<HttpResult<UserBean>> getUserMSG(@FieldMap Map<String,Object> params);

    /**
     * 获取用户活跃度列表
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("/user/get_user_activity_degree")
    Observable<HttpResult<ActivityDegreeListBean>> getActivityDegreeList(@FieldMap Map<String,Object> params);

    /**
     * 保存宝箱
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("/contest/save_box")
    Observable<HttpResult> getSaveBox(@FieldMap Map<String,Object> params);

    /**
     * 根据设备号,获取个人信息和token
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("/user/get_user_info_by_devicecode")
    Observable<HttpResult<DeviceMSG>> getDeviceMsg(@FieldMap Map<String,Object> params);
}
