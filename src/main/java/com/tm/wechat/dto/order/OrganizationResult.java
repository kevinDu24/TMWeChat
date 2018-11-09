package com.tm.wechat.dto.order;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 组织架构to
 * Created by LEO on 16/9/29.
 */
@Data
public class OrganizationResult implements Serializable {

    private String userId; //用户id

    private String parentId; //上级id

    private String userName; //用户名

    private String userLevel; //账号级别：主账号、子账号

    private String xtczmc;//系统操作名称

    private List<OrganizationResult> users = new ArrayList<>(); //下级用户信息

    public OrganizationResult(){

    }
//
//    public OrganizationResult(String userId, String parentId, String userName, String userLevel){
//        this.userId = userId;
//        this.parentId = parentId;
//        this.userName = userName;
//        this.userLevel = userLevel;
//    }


    public static List<OrganizationResult> findResult(List<OrganizationResult> datas){
        List<OrganizationResult> results = new ArrayList<>();
        findChild(results,datas,null);
        System.out.println(JSON.toJSONString(results));
        return results;
    }

    public static void findChild(List<OrganizationResult> results, List<OrganizationResult> datas, String userId){
        for(OrganizationResult vo : datas){
            if(vo.getParentId() ==  userId || ( userId != null && userId.equals(vo.getParentId() ) )){
                results.add(vo);
                findChild(vo.getUsers(),datas,vo.getUserId());
            }
        }
    }

//
//    public static void main(String[] args) {
//        OrganizationResult vo1 = new OrganizationResult("1","345","SH000","主账号");
//        OrganizationResult vo2 = new OrganizationResult("2","1","SH000_01","子账号");
//        OrganizationResult vo3 = new OrganizationResult("3","2","SH000_01_01","子账号");
//        OrganizationResult vo4 = new OrganizationResult("4","3","SH000_01_01_01","子账号");
//        OrganizationResult vo5 = new OrganizationResult("5","4","SH000_01_01_01_01","子账号");
//        OrganizationResult vo6 = new OrganizationResult("6","2","SH000_02","子账号");
//        OrganizationResult vo7 = new OrganizationResult("7","6","SH000_02_01","子账号");
//        List<OrganizationResult> datas = new ArrayList<>();
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
