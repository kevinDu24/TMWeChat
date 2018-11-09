package com.tm.wechat.dto.order;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 组织架构及数量dto
 * Created by LEO on 16/9/29.
 */
@Data
public class OrganizationAndCountListResult {

    private String userId; //用户id

    private String parentId; //上级id

    private String userName; //用户名

    private String xtczmc; //姓名

    private String applyCount; //申请数量

    private List<OrganizationAndCountListResult> users = new ArrayList<>(); //下级用户信息

    public OrganizationAndCountListResult(){

    }
//
//    public OrganizationAndCountListResult(String userId,String parentId,String userName,String applyCount, String xtczmc){
//        this.userId = userId;
//        this.parentId = parentId;
//        this.userName = userName;
//        this.xtczmc = xtczmc;
//        this.applyCount = applyCount;
//    }


    public static List<OrganizationAndCountListResult> findResult(List<OrganizationAndCountListResult> datas){
        List<OrganizationAndCountListResult> results = new ArrayList<>();
        findChild(results,datas,null);
        System.out.println(JSON.toJSONString(results));
        return results;
    }

    public static void findChild(List<OrganizationAndCountListResult> results,List<OrganizationAndCountListResult> datas,String userId){
        for(OrganizationAndCountListResult vo : datas){
            if(vo.getParentId() ==  userId || ( userId != null && userId.equals(vo.getParentId() ) )){
                results.add(vo);
                findChild(vo.getUsers(),datas,vo.getUserId());
            }
        }
    }

//
//    public static void main(String[] args) {
//        OrganizationAndCountListResult vo1 = new OrganizationAndCountListResult("1","345","SH000","5","安徽汽车销售公司");
//        OrganizationAndCountListResult vo2 = new OrganizationAndCountListResult("2","1","SH000_01","5","小明");
//        OrganizationAndCountListResult vo3 = new OrganizationAndCountListResult("3","2","SH000_01_01","5","小明1");
//        OrganizationAndCountListResult vo4 = new OrganizationAndCountListResult("4","3","SH000_01_01_01","5","小明11");
//        OrganizationAndCountListResult vo5 = new OrganizationAndCountListResult("5","4","SH000_01_01_01_01","5","小明111");
//        OrganizationAndCountListResult vo6 = new OrganizationAndCountListResult("6","2","SH000_02","5","小王");
//        OrganizationAndCountListResult vo7 = new OrganizationAndCountListResult("7","6","SH000_02_01","5","小王1");
//        List<OrganizationAndCountListResult> datas = new ArrayList<>();
//        datas.add(vo1);
//        datas.add(vo2);
//        datas.add(vo3);
//        datas.add(vo5);
//        datas.add(vo4);
//        datas.add(vo6);
//        datas.add(vo4);
//        datas.get(0).setParentId(null);
//        findResult(datas);
//    }

}
