package cn.showclear.www.controller.data;

import cn.com.scooper.common.resp.APIRespJson;
import cn.showclear.www.pojo.common.Message;
import cn.showclear.www.service.compare.CompareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Wang Junbo
 * @description
 * @date 2019/6/21
 */
@Controller
@RequestMapping("/data/compare")
public class CompareDataController extends BaseDataController {

    @Autowired
    private CompareService compareService;

    @ResponseBody
    @RequestMapping("/database")
    public APIRespJson compareTable() {
        Message message = compareService.compareDBInfo();
        return this.response(message.getCode(), message.getMessage());
    }
}
