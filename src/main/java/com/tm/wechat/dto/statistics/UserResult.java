package com.tm.wechat.dto.statistics;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 统计使用的用户信息
 */
@Data
public class UserResult {

    private String userId; //用户id

    private String parentId; //上级id

    private String username; //用户名

    private String userLevel; //账号级别：主账号、子账号

    private String xtczmc;//系统操作名称

    private List<UserResult> users = new ArrayList<>(); //下级用户信息

    public UserResult(){

    }


    public static List<UserResult> findResult(List<UserResult> datas){
        List<UserResult> results = new ArrayList<>();
        findChild(results,datas,null);
        System.out.println(JSON.toJSONString(results));
        return results;
    }

    public static void findChild(List<UserResult> results, List<UserResult> datas, String userId){
        for(UserResult vo : datas){
            if(vo.getParentId() ==  userId || ( userId != null && userId.equals(vo.getParentId() ) )){
                results.add(vo);
                findChild(vo.getUsers(),datas,vo.getUserId());
            }
        }
    }


}
