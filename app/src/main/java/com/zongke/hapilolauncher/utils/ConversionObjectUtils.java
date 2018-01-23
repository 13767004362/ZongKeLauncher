package com.zongke.hapilolauncher.utils;

import android.text.TextUtils;

import com.zhongke.account.model.AccountInfo;
import com.zongke.hapilolauncher.db.entity.DeviceMSG;

/**
 * Created by ${xingen} on 2017/11/16.
 *
 * 转换对象的工具类
 */

public class ConversionObjectUtils {

    /**
     * 将DeviceMSG 对象转换成AccountInfo对象
     * @param deviceMSG
     * @return
     */
    public static AccountInfo conversionDevice(DeviceMSG deviceMSG){
        AccountInfo accountInfo=new AccountInfo();
        DeviceMSG.SysUserBean sysUserBean=deviceMSG.getSysUser();
        accountInfo.nickName=sysUserBean.getNickName();
        accountInfo.icon=sysUserBean.getHeadImageUrl();
        accountInfo.sex=sysUserBean.getSex();
        accountInfo.setDeviceCode(sysUserBean.getDeviceCode());
        accountInfo.name=sysUserBean.getUserName();
        accountInfo.schoolName=sysUserBean.getSchool();
        accountInfo.userId=String.valueOf(sysUserBean.getId());
        accountInfo.setRegisterCode(sysUserBean.getRegisterCode());
        accountInfo.setPhoneCode(sysUserBean.getPhoneCode());
        accountInfo.birth=sysUserBean.getBirthday();
        if (!TextUtils.isEmpty(sysUserBean.getMajorList())){
            accountInfo.setMajrList(sysUserBean.getMajorList().split(","));
        }
        if (!TextUtils.isEmpty(sysUserBean.getTagList())){
            accountInfo.labs=sysUserBean.getTagList().split(",");
        }
        accountInfo.setToken(deviceMSG.getToken());
        return accountInfo;
    }


}
