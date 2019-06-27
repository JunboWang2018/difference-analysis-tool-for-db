package cn.showclear.www.service.history;

import cn.showclear.www.pojo.base.HistoryQo;

import java.util.List;

public interface HistoryService {
    /**
     * 获取历史记录
     * @return
     */
    List<HistoryQo> getHistories();
}
