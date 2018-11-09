package com.tm.wechat.controller.file;

import com.tm.wechat.dto.approval.AprrovalReportDto;
import com.tm.wechat.service.ExcelService;
import com.tm.wechat.service.approval.ApprovalDataService;
import com.tm.wechat.utils.commons.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by huzongcheng on 2017/11/27.
 */
@RestController
@RequestMapping(value = "excel")
public class ExportExcelController {

    @Autowired
    private ApprovalDataService approvalDataService;

    @Autowired
    private ExcelService excelService;

    /**
     * 检修任务列表导出
     *
     * @param aprrovalReportDto 检索条件
     * @param response      页面请求
     */
    @RequestMapping(value = "approvalSearch", method = RequestMethod.GET)
    public void approvalSearch(AprrovalReportDto aprrovalReportDto, HttpServletResponse response) {
        String searchType = aprrovalReportDto.getSearchType();
        String name = "0".equals(searchType) ? "预审批列表" : "在线申请列表";
        // 对前台加密URL参数解码，解决IE等浏览器中文参数乱码问题
        aprrovalReportDto.setName((CommonUtils.urlDecoder(aprrovalReportDto.getName())));
        List<AprrovalReportDto> recoveryVehicleTaskDtos = approvalDataService.findRecoveryVehicleTaskExport(aprrovalReportDto);
        try {
            OutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment; filename=" + new String(new String(name.getBytes("gb2312"), "iso8859-1") + ".xlsx"));
            excelService.exportList(name, recoveryVehicleTaskDtos, AprrovalReportDto.class, response.getOutputStream());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
