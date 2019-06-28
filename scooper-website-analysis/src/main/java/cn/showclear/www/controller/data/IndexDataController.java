package cn.showclear.www.controller.data;

import cn.com.scooper.common.exception.BusinessException;
import cn.com.scooper.common.resp.APIRespJson;
import cn.showclear.www.common.constant.CommonConstant;
import cn.showclear.www.pojo.base.HistoryQo;
import cn.showclear.www.service.compare.CompareService;
import cn.showclear.www.service.download.DownloadService;
import cn.showclear.www.service.history.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wang Junbo
 * @description
 * @date 2019/6/27
 */
@Controller
@RequestMapping("/data/index")
public class IndexDataController extends BaseDataController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexDataController.class);

    @Autowired
    private CompareService compareService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private DownloadService downloadService;

    /**
     * 获取历史记录
     * @return
     */
    @ResponseBody
    @RequestMapping("/history")
    public APIRespJson getHistory() {
        List<HistoryQo> histories = historyService.getHistories();
        List<Map<String, HistoryQo>> historyMaps = historyService.getHistoryMaps(histories);
        return this.responseList(historyMaps);
    }

    /**
     * 比较数据库和资源文件
     * @return
     */
    @ResponseBody
    @RequestMapping("/compare")
    public APIRespJson compare() {
        long startTime = System.currentTimeMillis();
        LOGGER.info("start time = " + startTime);
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            String generatePath = compareService.getGeneratePath();

            //对比数据库差异
            String sqlFilePath = compareService.compareDBInfo(generatePath);
            resultMap.put("sqlPath", sqlFilePath);

            //对比文件差异
            String uploadFilePath = compareService.compareFileInfo(generatePath);
            resultMap.put("filePath", uploadFilePath);
        } catch (BusinessException e) {
            this.response(e.getCode(), e.getMessage());
        } catch (Exception e) {
            this.response(CommonConstant.FAILED_CODE, "对比失败！");
        }
        LOGGER.info("end time = " + (System.currentTimeMillis() - startTime));
        return this.responseData(resultMap);
    }


    /**
     * 文件下载
     * @param filePath
     * @param response
     */
    @RequestMapping("/download")
    public APIRespJson download(String filePath, HttpServletResponse response) {
        InputStream inputStream = null;
        try {
            inputStream = downloadService.getFileInputStream(filePath);
        } catch (BusinessException e) {
            return this.response(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return this.response(CommonConstant.FAILED_CODE, "下载失败！");
        }
        if (inputStream == null) {
            return this.response(CommonConstant.FAILED_CODE, "下载失败！");
        }
        File file = new File(filePath);
        //设置文件下载头
        response.addHeader("Content-Disposition", "attachment;filename=" + file.getName());
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        byte[] buff = new byte[1024];
        try {
            OutputStream outputStream = response.getOutputStream();
            int i = 0;
            while ((i = inputStream.read(buff)) != -1) {
                outputStream.write(buff, 0, i);
                outputStream.flush();
            }
        } catch (IOException e) {
            LOGGER.info("用户取消了下载");
        }
        return this.response(CommonConstant.SUCCESS_CODE, "下载成功");
    }
}
