package com.zongke.hapilolauncher.db.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${xingen} on 2017/9/6.
 */

public class PersonRanking_Entity {
   public  int ranking;
    public String iconUrl;
    public static List<PersonRanking_Entity> createTestDate(){
        List<PersonRanking_Entity> list=new ArrayList<>();
        for (int i=1;i<=3;++i){
            PersonRanking_Entity entity=new PersonRanking_Entity();
            entity.ranking=i;
            list.add(entity);
        }
        return list;
    }
}
