package cn.showclear.www.controller.data;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.WebAsyncTask;

import cn.com.scooper.common.exception.BusinessException;
import cn.com.scooper.common.resp.APIListJson;
import cn.com.scooper.common.resp.APIPage;
import cn.com.scooper.common.resp.APIPageJson;
import cn.com.scooper.common.resp.APIRespJson;
import cn.com.scooper.common.resp.ResultCode;

import cn.showclear.www.controller.BaseController;


/**
 * 数据 REST API 的基类（其它 REST API 需继承该类）
 * 
 * <p>
 * 该类封装了常用的返回操作，以及统一异常处理（将异常转换为JSON输出）。
 * </p>
 * 
 * @author jiangwenjing
 */
public class BaseDataController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDataController.class);

    @Autowired
    protected MessageSource messageSource;
    
    /**
     * 返回操作成功
     * @return
     */
    protected APIRespJson response() {
        return new APIRespJson();
    }
    
    /**
     * 返回指定错误码
     * @param code
     * @return
     */
    protected APIRespJson response(int code) {
        return new APIRespJson(code, messageSource.getMessage("resultcode." + code, null, Locale.CHINESE));
    }
    
    /**
     * 返回指定错误码，并携带错误信息
     * @param code
     * @param message
     * @return
     */
    protected APIRespJson response(int code, String message) {
        return new APIRespJson(code, message);
    }

    /**
     * 返回数据
     * @param data
     * @return
     */
    protected APIRespJson responseData(Object data) {
        return new APIRespJson().setData(data);
    }
    
    /**
     * 以列表形式返回数据
     * @param data
     * @return
     */
    protected <T> APIRespJson responseList(List<T> data) {
        return new APIListJson<T>(data);
    }

    /**
     * 以分页形式返回数据
     * @param data
     * @return
     */
    protected <T> APIRespJson responsePage(APIPage<T> data) {
        return new APIPageJson<T>(data);
    }
    
    /**
     * 包装异步处理任务，默认超时时间为 30s；
     * 处理超时时返回JSON错误码。
     * 
     * @param callable
     * @return
     */
    protected WebAsyncTask<APIRespJson> webAsync(Callable<APIRespJson> callable) {
        return webAsync(30000L, callable);
    }
    
    /**
     * 包装异步处理任务；
     * 处理超时时返回JSON错误码。
     * 
     * @param timeout 超时时间
     * @param callable
     * @return
     */
    protected WebAsyncTask<APIRespJson> webAsync(long timeout, Callable<APIRespJson> callable) {
        WebAsyncTask<APIRespJson> task = new WebAsyncTask<APIRespJson>(timeout, callable);
        task.onTimeout(onWebAsyncTimeout);
        return task;
    }
    
    // 异步处理超时处理方法
    private final Callable<APIRespJson> onWebAsyncTimeout = new Callable<APIRespJson>() {
        @Override
        public APIRespJson call() throws Exception {
            return response(ResultCode.EXEC_TIMEOUT);
        }
    };

    /**
     * 业务异常
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public APIRespJson handleBusinessException(BusinessException e) {
        LOGGER.error(e.getMessage(), e);
        return response(e.getCode(), e.getMessage());
    }
    
    /**
     * 处理参数错误
     * @param e
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public APIRespJson handleIllegalArgumentException(IllegalArgumentException e) {
        LOGGER.error(e.getMessage(), e);
        return response(ResultCode.PARAM_ERR, e.getMessage());
    }
    
    /**
     * 统一处理未处理的异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    //@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    public APIRespJson handleAllException(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return response(ResultCode.FAIL, e.getMessage());
    }

}
