package cn.showclear.www.service.history;

import cn.com.scooper.common.exception.BusinessException;
import cn.showclear.www.pojo.base.HistoryQo;

import java.util.List;
import java.util.Map;

public interface HistoryService {
    /**
     * 获取历史记录
     * @return
     */
    List<HistoryQo> getHistories() throws BusinessException;

    /**
     * 将历史记录列表整理成返回json的格式
     * @param histories
     * @return
     */
    List<Map<String, HistoryQo>> getHistoryMaps(List<HistoryQo> histories);
}
