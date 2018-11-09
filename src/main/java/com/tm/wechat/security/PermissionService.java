package com.tm.wechat.security;

import com.jayway.restassured.path.json.JsonPath;
import com.tm.wechat.domain.SysUser;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.sysUser.RoleDto;
import com.tm.wechat.service.common.BaseService;
import com.tm.wechat.service.sysUser.SysUserFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by LEO on 16/9/8.
 */
@Service("permission")
public class PermissionService extends BaseService {

    @Autowired
    private SysUserFunctionService sysUserFunctionService;




    /**
     * 判断是否是总部人员
     * @param userName
     * @return
     */
    public Boolean isHplUser(String userName, String headerParam){
        String customer = JsonPath.from(headerParam).get("systemflag");
        SysUser sysUser = getSysUser(userName,customer);

        return sysUser.getXTJGID().equals(81l) || ("区域销售经理岗").equals(sysUser.getXTJSMC());
    }
    /**
     * 判断是否是太盟经销商
     * @param userName
     * @return
     */
    public Boolean isDealerUser(String userName){
        SysUser sysUser = getSysUser(userName,"taimeng");
        return ("经销商").equals(sysUser.getXTJSMC());
    }

    /**
     * 判断是否有二手车评估权限
     * @param userName
     * @return
     */
    public Boolean isHaveUsedCarAnalysis1Power(String userName){
        String usedCarAnalysis1Power = "0";
        ResponseEntity<Message> responseEntity = sysUserFunctionService.getUsedCarAnalysis1Power(userName);
        usedCarAnalysis1Power = responseEntity.getBody().getData().toString();
        if("1".equals(usedCarAnalysis1Power)){
            return true;
        }else {
            return false;
        }
    }


    /**
     * 获取用户角色
     * @param userName
     * @param headerParam
     * @return
     */
    public RoleDto getRole(String userName, String headerParam){
        String customer = JsonPath.from(headerParam).get("systemflag");
        SysUser sysUser = getSysUser(userName,customer);

        RoleDto roleDto = new RoleDto();
        roleDto.setRoleId(sysUser.getXTJSID());
        roleDto.setRoleName(sysUser.getXTJSMC());
        return roleDto;
    }
    public Boolean isSameUser(String userName, String loginUserName){
        return loginUserName.equals(userName);
    }
}
