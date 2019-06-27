package cn.showclear.www.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Wang Junbo
 * @description
 * @date 2019/6/27
 */
@Controller
public class IndexViewController {

    @RequestMapping("/")
    public String index(){
        return "index.jsp";
    }
}
