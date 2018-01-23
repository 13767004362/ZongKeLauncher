package com.zongke.hapilolauncher.db.entity;

/**
 * Created by ${xingen} on 2017/11/15.
 */

public class DeviceMSG {

    /**
     * SysUser : {"birthday":"2000-01-01 00:00:00","nickName":"何","headImageUrl":"http://zhongke.oss-cn-shenzhen.aliyuncs.com/res_pic/cDD4FwaRMnGi5PMnabcwBrbHf1510645575103.png","roleId":10,"userPass":"","userPhone":"A1234b","sex":1,"fullName":"","majorList":"com.zhongke.weiduo.mvp.model.entity.LabInfo@4292e850,com.zhongke.weiduo.mvp.model.entity.LabInfo@4292c688,","deviceCode":"A1234b","registerType":2,"userName":"A1234b","orgId":1,"familyAddress":"","tagList":"com.zhongke.weiduo.mvp.model.entity.LabInfo@429357d0,com.zhongke.weiduo.mvp.model.entity.LabInfo@42932290,com.zhongke.weiduo.mvp.model.entity.LabInfo@42931e78,","userState":1,"createTime":"2017-11-15 10:58:51","school":"深圳","phoneCode":"13767004362","id":129,"registerCode":"hbkszd","info":""}
     * token : vlo3ftfj1510742328134
     */

    private SysUserBean SysUser;
    private String token;

    public SysUserBean getSysUser() {
        return SysUser;
    }

    public void setSysUser(SysUserBean SysUser) {
        this.SysUser = SysUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class SysUserBean {
        /**
         * birthday : 2000-01-01 00:00:00
         * nickName : 何
         * headImageUrl : http://zhongke.oss-cn-shenzhen.aliyuncs.com/res_pic/cDD4FwaRMnGi5PMnabcwBrbHf1510645575103.png
         * roleId : 10
         * userPass :
         * userPhone : A1234b
         * sex : 1
         * fullName :
         * majorList : com.zhongke.weiduo.mvp.model.entity.LabInfo@4292e850,com.zhongke.weiduo.mvp.model.entity.LabInfo@4292c688,
         * deviceCode : A1234b
         * registerType : 2
         * userName : A1234b
         * orgId : 1
         * familyAddress :
         * tagList : com.zhongke.weiduo.mvp.model.entity.LabInfo@429357d0,com.zhongke.weiduo.mvp.model.entity.LabInfo@42932290,com.zhongke.weiduo.mvp.model.entity.LabInfo@42931e78,
         * userState : 1
         * createTime : 2017-11-15 10:58:51
         * school : 深圳
         * phoneCode : 13767004362
         * id : 129
         * registerCode : hbkszd
         * info :
         */

        private String birthday;
        private String nickName;
        private String headImageUrl;
        private int roleId;
        private String userPass;
        private String userPhone;
        private int sex;
        private String fullName;
        private String majorList;
        private String deviceCode;
        private int registerType;
        private String userName;
        private int orgId;
        private String familyAddress;
        private String tagList;
        private int userState;
        private String createTime;
        private String school;
        private String phoneCode;
        private int id;
        private String registerCode;
        private String info;

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getHeadImageUrl() {
            return headImageUrl;
        }

        public void setHeadImageUrl(String headImageUrl) {
            this.headImageUrl = headImageUrl;
        }

        public int getRoleId() {
            return roleId;
        }

        public void setRoleId(int roleId) {
            this.roleId = roleId;
        }

        public String getUserPass() {
            return userPass;
        }

        public void setUserPass(String userPass) {
            this.userPass = userPass;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getMajorList() {
            return majorList;
        }

        public void setMajorList(String majorList) {
            this.majorList = majorList;
        }

        public String getDeviceCode() {
            return deviceCode;
        }

        public void setDeviceCode(String deviceCode) {
            this.deviceCode = deviceCode;
        }

        public int getRegisterType() {
            return registerType;
        }

        public void setRegisterType(int registerType) {
            this.registerType = registerType;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getOrgId() {
            return orgId;
        }

        public void setOrgId(int orgId) {
            this.orgId = orgId;
        }

        public String getFamilyAddress() {
            return familyAddress;
        }

        public void setFamilyAddress(String familyAddress) {
            this.familyAddress = familyAddress;
        }

        public String getTagList() {
            return tagList;
        }

        public void setTagList(String tagList) {
            this.tagList = tagList;
        }

        public int getUserState() {
            return userState;
        }

        public void setUserState(int userState) {
            this.userState = userState;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getPhoneCode() {
            return phoneCode;
        }

        public void setPhoneCode(String phoneCode) {
            this.phoneCode = phoneCode;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRegisterCode() {
            return registerCode;
        }

        public void setRegisterCode(String registerCode) {
            this.registerCode = registerCode;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}
