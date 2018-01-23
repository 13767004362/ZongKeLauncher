package com.zongke.hapilolauncher.library.retrofit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ${xingen} on 2017/11/16.
 */

public class BuilderMap {
    /**
     * 用户活跃度列表
     * @param token
     * @param currentPage
     * @param pageSize
     * @return
     */
    public static Map<String,Object>  builderActivityList(String token,int currentPage,int pageSize){
        Map<String,Object> map=new HashMap<>();
        map.put("token",token);
        map.put("pageIndex",currentPage);
        map.put("pageSize",pageSize);
        return map;
    }
}
