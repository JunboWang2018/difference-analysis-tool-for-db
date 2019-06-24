package cn.showclear.www.controller.data;

import cn.showclear.www.service.sql.GenerateSQLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Wang Junbo
 * @description
 * @date 2019/6/21
 */
@Controller
@RequestMapping("/data/test")
public class TestDataController extends BaseDataController {

    @Autowired
    private GenerateSQLService generateSQLService;


}
