package com.zongke.hapilolauncher.db.entity;

/**
 * Created by ${xingen} on 2017/11/15.
 */

public class UserBean {

    /**
     * SysUser : {"headImageUrl":"http://zhongke.oss-cn-shenzhen.aliyuncs.com/res_pic/AMEibwcaETY222SeCxdrbzXGK1509106813182.jpg","nickName":"苏苏","sex":1,"userPhone":"","deviceCode":"","userName":"testAdmin","orgId":1,"createTime":"2017-10-14","school":"","userState":1,"id":90,"registerCode":"","info":"测试"}
     */

    private SysUserBean SysUser;

    public SysUserBean getSysUser() {
        return SysUser;
    }

    public void setSysUser(SysUserBean SysUser) {
        this.SysUser = SysUser;
    }

    public static class SysUserBean {
        /**
         * headImageUrl : http://zhongke.oss-cn-shenzhen.aliyuncs.com/res_pic/AMEibwcaETY222SeCxdrbzXGK1509106813182.jpg
         * nickName : 苏苏
         * sex : 1
         * userPhone :
         * deviceCode :
         * userName : testAdmin
         * orgId : 1
         * createTime : 2017-10-14
         * school :
         * userState : 1
         * id : 90
         * registerCode :
         * info : 测试
         */

        private String headImageUrl;
        private String nickName;
        private int sex;
        private String userPhone;
        private String deviceCode;
        private String userName;
        private int orgId;
        private String createTime;
        private String school;
        private int userState;
        private int id;
        private String registerCode;
        private String info;

        public String getHeadImageUrl() {
            return headImageUrl;
        }

        public void setHeadImageUrl(String headImageUrl) {
            this.headImageUrl = headImageUrl;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getDeviceCode() {
            return deviceCode;
        }

        public void setDeviceCode(String deviceCode) {
            this.deviceCode = deviceCode;
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

        public int getUserState() {
            return userState;
        }

        public void setUserState(int userState) {
            this.userState = userState;
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
