package com.tm.wechat.service.approval;

import com.alibaba.fastjson.JSONArray;
import com.tm.wechat.config.VersionProperties;
import com.tm.wechat.dao.ApplyInfoNewRepository;
import com.tm.wechat.dao.ApplyInfoRepository;
import com.tm.wechat.dto.approval.ApprovalMonthDto;
import com.tm.wechat.dto.approval.AprrovalReportDto;
import com.tm.wechat.dto.approval.PageDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.utils.commons.CommonUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jpa.HibernateEntityManager;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by LEO on 16/9/13.
 */
@Service
public class ApprovalDataService {
    private static final Logger logger = LoggerFactory.getLogger(ApprovalDataService.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private VersionProperties versionProperties;

    @Autowired
    private ApplyInfoNewRepository applyInfoNewRepository;

    /**
     * 分页返回申请结果
     *
     * @param aprrovalReportDto
     * @param page
     * @param size
     * @return
     */
    public ResponseEntity<Message> approvalSearch(AprrovalReportDto aprrovalReportDto, int page, int size) {
        // 版本号
        HibernateEntityManager hEntityManager = (HibernateEntityManager)entityManager;
        Session session = hEntityManager.getSession();
        Query query = session.createSQLQuery(getQuerySql(aprrovalReportDto , page ,  size,1, aprrovalReportDto.getSearchType())).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        setParams(aprrovalReportDto,query);
        List<Map<String,Object>>  list = query.list();
        PageDto pageDto = new PageDto();
        pageDto.setPage(page);
        pageDto.setSize(size);
        pageDto.setContent(list);
        query = session.createSQLQuery(getQuerySql(aprrovalReportDto , page ,  size,0,aprrovalReportDto.getSearchType()));
        setParams(aprrovalReportDto,query);
        Object  countList = query.uniqueResult();
        pageDto.setTotalElements(Long.parseLong(countList.toString()));
        Message message = new Message(MessageType.MSG_TYPE_SUCCESS, pageDto);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, pageDto), HttpStatus.OK);
    }

    /**
     * 返回申请结果列表
     * @param aprrovalReportDto
     * @return
     */
    public List<AprrovalReportDto> findRecoveryVehicleTaskExport(AprrovalReportDto aprrovalReportDto){
        HibernateEntityManager hEntityManager = (HibernateEntityManager)entityManager;
        Session session = hEntityManager.getSession();
        Query query = session.createSQLQuery(getQuerySql(aprrovalReportDto , 0 , 0, 2,aprrovalReportDto.getSearchType())).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        setParams(aprrovalReportDto,query);
        List<Map<String,Object>>  list = query.list();
        List<AprrovalReportDto> resultList = new ArrayList<>();
        if (list != null && !list.isEmpty()){
            AprrovalReportDto dto;
            for (Map<String,Object> map : list){
                dto = new AprrovalReportDto(map);
                resultList.add(dto);
            }
        }
        return resultList;
    }

    /**
     * 构建sql
     * @param recoveryVehicleTaskDto
     * @param page
     * @param size
     * @return
     */
    public String getQuerySql(AprrovalReportDto recoveryVehicleTaskDto, int page , int size,Integer flag, String searchType){
        StringBuffer querySql = new StringBuffer();
        StringBuffer whereSql = new StringBuffer();
        querySql.append(" select  ");
        if(flag == 1 || flag == 2) {
            querySql.append(" A.item_string_value as name, B.apply_num as applyNum , B.status as status ,B.create_user as fpName, B.create_time as applyDate ");
        }else{
            querySql.append(" count(B.status) ");
        }
        querySql.append(" from  (select * from approval t ");
        querySql.append(" where t.item_key = '#idCardInfoDto#name') A  ");
        querySql.append(" INNER JOIN apply_info B ON A.unique_mark = B.approval_uuid ");
        whereSql.append(" where to_char(B.create_time,'YYYY-MM-DD') between :beginTime and :endTime");
        if("0".equals(searchType)){
            whereSql.append(" and B.status <= '1100'");
        } else if("1".equals(searchType)){
            whereSql.append(" and B.status > '1100'");
        }

        if(!CommonUtils.isNull(recoveryVehicleTaskDto.getName())){
            whereSql.append(" and A.item_string_value like :name ");
        }

        if(!CommonUtils.isNull(recoveryVehicleTaskDto.getApplyNum())){
            whereSql.append(" and B.apply_num like :applyNum ");
        }

        if(!CommonUtils.isNull(recoveryVehicleTaskDto.getFpName())){
            whereSql.append(" and B.create_user like :fpName ");
        }

        if(!CommonUtils.isNull(recoveryVehicleTaskDto.getStatus())){
            whereSql.append(" and B.status = :status ");
        }
        if(flag == 1 || flag == 2){
            whereSql.append(" ORDER BY B.create_time DESC ");
            if(flag == 1)
                whereSql.append(" limit "+ size +" offset "+(page -1) * size);
        }
        String sql = querySql.toString() + whereSql.toString();
        return "1".equals(searchType) ? sql.replaceAll("create_time","submit_time") : sql;
    }

    public void setParams(AprrovalReportDto recoveryVehicleTaskDto,Query query){

        query.setParameter("beginTime",recoveryVehicleTaskDto.getBeginTime());
        query.setParameter("endTime",recoveryVehicleTaskDto.getEndTime());
        if(!CommonUtils.isNull(recoveryVehicleTaskDto.getName())){
            query.setParameter("name",CommonUtils.likePartten(recoveryVehicleTaskDto.getName()));
        }
        if(!CommonUtils.isNull(recoveryVehicleTaskDto.getApplyNum())){
            query.setParameter("applyNum",CommonUtils.likePartten(recoveryVehicleTaskDto.getApplyNum()));
        }

        if(!CommonUtils.isNull(recoveryVehicleTaskDto.getFpName())){
            query.setParameter("fpName",CommonUtils.likePartten(recoveryVehicleTaskDto.getFpName()));
        }

        if(!CommonUtils.isNull(recoveryVehicleTaskDto.getStatus())){
            query.setParameter("status",recoveryVehicleTaskDto.getStatus());
        }

    }

    /**
     * 按月查询不同审批状态的申请数量
     *
     * @param searchType 0:查询预审批相关信息、1：查询在线申请相关信息
     * @return
     */
    public ResponseEntity<Message> getApprovalMonthData(String searchType) {
        // 版本号
        String version = versionProperties.getVersion();
        ApprovalMonthDto dto = new ApprovalMonthDto();
        JSONArray monthArray = buildMonthData();
        JSONArray passArray = null;
        JSONArray refuseArray = null;
        JSONArray returnArray = null;
        if("0".equals(searchType)){
            passArray = buildData(monthArray, "0");
            refuseArray = buildData(monthArray, "1");
        } else if("1".equals(searchType)){
            passArray = buildData(monthArray, "2");
            refuseArray = buildData(monthArray, "3");
            returnArray = buildData(monthArray, "4");
        }
        dto.setMonthArray(monthArray);
        dto.setPassArray(passArray);
        dto.setRefuseArray(refuseArray);
        dto.setReturnArray(returnArray);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, dto), HttpStatus.OK);
    }

    /**
     * 统计数据取得
     *
     * @param flag 0:预审批通过、1:预审批拒绝、2:在线申请通过、3在线申请拒绝、4:在线申请退回待修改
     * @return
     */
    private JSONArray buildData(JSONArray monthArray, String flag) {
        JSONArray array = new JSONArray();
        SimpleDateFormat sf  =new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(new Date());
        gc.add(2,1);
        String endTime = sf.format(gc.getTime());
        GregorianCalendar gc1 = new GregorianCalendar();
        gc1.setTime(new Date());
        gc1.add(2,-5);
        String beginTime = sf.format(gc1.getTime());
        List<Object> dataList = new ArrayList();
        if("0".equals(flag)){
            // 查询预审批通过月份数据
            dataList = applyInfoNewRepository.findMonthDataPass(beginTime, endTime);
        } else if("1".equals(flag)){
            // 查询预审批拒绝月份数据
            dataList = applyInfoNewRepository.findMonthDataRefuse(beginTime, endTime);
        } else if("2".equals(flag)){
            // 查询在线申请通过月份数据
            dataList = applyInfoNewRepository.findMonthDataPassOnline(beginTime, endTime);
        } else if("3".equals(flag)){
            // 查询在线申请拒绝月份数据
            dataList = applyInfoNewRepository.findMonthDataRefuseOnline(beginTime, endTime);
        } else if("4".equals(flag)){
            // 查询在线申请退回待修改月份数据
            dataList = applyInfoNewRepository.findMonthDataReturnOnline(beginTime, endTime);
        }
        //如果每个月都有数据（申请量），依次加入数组
        if(dataList != null && dataList.size() == 6){
            for(Object object :dataList) {
                Object[] objs = (Object[]) object;
                array.add(objs[1]);
            }
        } else if(dataList != null && !dataList.isEmpty()){
            //如果不是每个月都有数据（申请量）
            Map<String,Object> map = new HashMap();
            //将有申请量的月份的数据以月份为key，count为value放入map中
            for(Object object :dataList) {
                Object[] objs = (Object[]) object;
                map.put(objs[0].toString(),objs[1]);
            }
            //近半年以此按照每个月到上述map中取月申请量
            for(int i=0;i<6;i++){
                String key = (String)monthArray.get(i);
                Object count = map.get(key);
                //取不到增设定该月申请量为0
                if(count == null){
                    array.add("");
                } else{
                    //取到则正常赋值
                    array.add(count);
                }
            }
        }
        return array;
    }

    /**
     * 获取近半年月份数组
     * @return
     */
    private JSONArray buildMonthData() {
        JSONArray array = new JSONArray();
        Calendar cale = null;
        cale = Calendar.getInstance();
        int year = cale.get(Calendar.YEAR);
        int month = cale.get(Calendar.MONTH) + 1;
        for(int i = 5; i>=0; i--){
            if(month - i <= 0){
                int monthInt = month + 12 -i;
                //格式化月份为2017-05的形式
                String monthStr = monthInt + "";
                if(monthInt < 10){
                    monthStr = "0" + monthInt;
                }
                array.add((year - 1) + "-" + monthStr);
            } else {
                int monthInt = month -i;
                //格式化月份为2017-05的形式
                String monthStr = monthInt + "";
                if(monthInt < 10){
                    monthStr = "0" + monthInt;
                }
                array.add(year + "-" + monthStr);
            }
        }
        return array;
    }
}
