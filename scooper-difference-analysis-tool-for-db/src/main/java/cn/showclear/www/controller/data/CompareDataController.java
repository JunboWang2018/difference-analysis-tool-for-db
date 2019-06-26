package cn.showclear.www.controller.data;

import cn.com.scooper.common.exception.BusinessException;
import cn.com.scooper.common.resp.APIRespJson;
import cn.showclear.www.common.constant.CommonConstant;
import cn.showclear.www.service.compare.CompareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Wang Junbo
 * @description
 * @date 2019/6/21
 */
@Controller
@RequestMapping("/data")
public class CompareDataController extends BaseDataController {

    @Autowired
    private CompareService compareService;

    @ResponseBody
    @RequestMapping("/compare")
    public APIRespJson compare(HttpSession session) {
        String generatePath = session.getServletContext().getRealPath("/") + "\\" + "file";
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
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
