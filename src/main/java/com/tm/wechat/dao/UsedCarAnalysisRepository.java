package com.tm.wechat.dao;

import com.tm.wechat.domain.UsedCarAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by pengchao on 2018/7/18.
 */
public interface UsedCarAnalysisRepository extends JpaRepository<UsedCarAnalysis, String> {

    @Query(nativeQuery = true, value = "SELECT\n" +
            "A.create_user,\n" +
            "A.unique_mark,\n" +
            "A.status,\n" +
            "A.update_time,\n" +
            "A.model_name,\n" +
            "B.report_url,\n" +
            "B.error_msg\n" +
            "FROM\n" +
            "used_car_analysis A\n" +
            "INNER JOIN used_car_analysis_record B\n" +
            "ON A.unique_mark = B.analysis_id\n" +
            "WHERE A.create_user = ?1 " +
            "AND A.model_name LIKE ?4 ORDER BY A.update_time DESC LIMIT ?2 OFFSET ?3")
    List<Object> findUsedCarAnalysisRecord(String userName, Integer size, Integer pageSize, String condition);


    @Query(nativeQuery = true, value = "SELECT\n" +
            "A.create_user,\n" +
            "A.unique_mark,\n" +
            "A.status,\n" +
            "A.update_time,\n" +
            "A.model_name,\n" +
            "B.report_url,\n" +
            "B.error_msg\n" +
            "FROM\n" +
            "used_car_analysis A\n" +
            "INNER JOIN used_car_analysis_record B\n" +
            "ON A.unique_mark = B.analysis_id\n" +
            "WHERE A.create_user = ?1 AND A.model_name LIKE ?2 " +
            "ORDER BY A.update_time DESC")
    List<Object> findUsedCarAnalysisRecordCount(String userName, String condition);

    UsedCarAnalysis findByUniqueMark(String unique_mark);

}
