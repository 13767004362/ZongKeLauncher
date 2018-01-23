package com.zongke.hapilolauncher.db.entity;

import java.util.List;

/**
 * Created by ${xingen} on 2017/7/5.
 *
 *  主页中转动的圆盘
 */

public class FunctionList_Entity {


    /**
     * resultCode : 0
     * msg :
     * familyName : 家庭名称
     * list : [{"id":1021,"name":"情商培养","itemList":[{"id":101,"name":"活动"},{"id":102,"name":"音乐"},{"id":103,"name":"电影"}]},{"id":1022,"name":"阅读习惯","itemList":[{"id":104,"name":"活动"},{"id":105,"name":"音乐"},{"id":106,"name":"电影"}]}]
     */


    private String familyName;
    private List<ListBean> list;
    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        // 计算的值，起始和结束的角度
        public int mStartAngle;
        public int mEndAngle;
        /**
         * id : 1021
         * name : 情商培养
         * itemList : [{"id":101,"name":"活动"},{"id":102,"name":"音乐"},{"id":103,"name":"电影"}]
         */
        private int id;
        private String name;
        private List<ItemListBean> itemList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ItemListBean> getItemList() {
            return itemList;
        }

        public void setItemList(List<ItemListBean> itemList) {
            this.itemList = itemList;
        }

        public static class ItemListBean {
            /**
             * id : 101
             * name : 活动
             */

            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
