package cn.showclear.www.controller.data;

import cn.com.scooper.common.exception.BusinessException;
import cn.com.scooper.common.resp.APIRespJson;
import cn.showclear.www.common.constant.CommonConstant;
import cn.showclear.www.pojo.base.HistoryQo;
import cn.showclear.www.service.compare.CompareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
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

    @Autowired
    private CompareService compareService;

    /**
     * 获取历史记录
     * @return
     */
    @ResponseBody
    @RequestMapping("/history")
    public APIRespJson getHistory() {
        List<HistoryQo> histories = new ArrayList<HistoryQo>();


        return this.responseList(histories);
    }

    /**
     * 比较数据库和资源文件
     * @return
     */
    @ResponseBody
    @RequestMapping("/compare")
    public APIRespJson compare() {
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
        return this.responseData(resultMap);
    }
}
