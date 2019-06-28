package cn.showclear.www.service.history.impl;

import cn.com.scooper.common.exception.BusinessException;
import cn.showclear.www.common.constant.CommonConstant;
import cn.showclear.www.dao.base.count.CountDao;
import cn.showclear.www.pojo.base.HistoryQo;
import cn.showclear.www.service.history.HistoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Wang Junbo
 * @description
 * @date 2019/6/27
 */
@Service
public class HistoryServiceImpl implements HistoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HistoryServiceImpl.class);

    @Autowired
    private PropertiesFactoryBean propFactory;

    @Autowired
    private CountDao countDao;

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取历史记录
     * @return
     */
    @Override
    public List<HistoryQo> getHistories() throws BusinessException {
        //获取路径
        Properties properties = null;
        try {
            properties = propFactory.getObject();
        } catch (IOException e) {
            String excepMsg = "读取导出路径配置失败！";
            LOGGER.error(excepMsg);
            throw new BusinessException(CommonConstant.FAILED_CODE, excepMsg);
        }
        String exportPath = "";
        if (isLinux()) {
            exportPath = properties.getProperty("linux.generate.file.path");
        } else {
            exportPath = properties.getProperty("windows.generate.file.path");
        }
        if (StringUtils.isEmpty(exportPath)) {
            String excepMsg = "没有配置导出路径！";
            LOGGER.error(excepMsg);
            throw new BusinessException(CommonConstant.FAILED_CODE, excepMsg);
        }

        return this.getHistories(exportPath);
    }

    /**
     * 判断是否是linux系统
     *
     * @return true：是linux系统|false：不是linux系统
     */
    protected boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }


    private List<HistoryQo> getHistories(String exportPath) throws BusinessException {
        List<HistoryQo> histories = new ArrayList<HistoryQo>();
        File exportFile = new File(exportPath);
        if (!exportFile.exists()) {
            throw new BusinessException(CommonConstant.FAILED_CODE, "导出路径不存在！");
        }
        this.arrangeHistory(exportPath, histories);
        return histories;
    }

    /**
     * 递归遍历导出路径下的所有文件，并添加到集合
     * @param path
     * @param histories
     */
    private void arrangeHistory(String path, List<HistoryQo> histories) {
        HistoryQo historyQo = new HistoryQo();
        File file = new File(path);
        if (!file.isFile()) {
            //递归遍历文件夹
            File[] files = file.listFiles();
            for (File file1 : files) {
                this.arrangeHistory(file1.getPath(), histories);
            }
        } else {
            //添加文件信息
            String fileName = file.getName();
            String[] strs = fileName.split("_");
            if (strs.length == 3) {
                int count = Integer.parseInt(strs[1]);
                historyQo.setId(count);
                if (CommonConstant.SQL_FILE_NAME_START.equals(strs[0])) {
                    strs[2] = strs[2].replaceAll(CommonConstant.SQL_FILE_NAME_END, "");
                }
                if (CommonConstant.ZIP_FILE_NAME_START.equals(strs[0])) {
                    strs[2] = strs[2].replaceAll(CommonConstant.ZIP_FILE_NAME_END, "");
                }
                historyQo.setDate(df.format(new Date(Long.parseLong(strs[2]))));
            }
            historyQo.setFileName(fileName);
            if (path.contains(File.separator)) {
                String path1 = path.substring(0, path.lastIndexOf(File.separator));
              /*  path1 = path1.replaceAll("\\\\", "\\\\\\\\");*/
                historyQo.setPath(path1);
            }
            histories.add(historyQo);
        }
    }

    /**
     * 将历史记录列表整理成返回json的格式，按时间从近到远
     * @param histories
     * @return
     */
    @Override
    public List<Map<String, HistoryQo>> getHistoryMaps(List<HistoryQo> histories) {
        List<Map<String, HistoryQo>> historyMaps = new ArrayList<Map<String, HistoryQo>>();
        //获取计数
        int count = countDao.getCount();
        for (int i = count; i >= 1; i--) {
            Map<String, HistoryQo> historyMap = new HashMap<String, HistoryQo>();
            for (int j = 0; j < histories.size(); j++) {
                if (histories.get(j).getId() == i) {
                    if (histories.get(j).getFileName().startsWith(CommonConstant.SQL_FILE_NAME_START)) {
                        historyMap.put("sql", histories.get(j));
                    }
                    if (histories.get(j).getFileName().startsWith(CommonConstant.ZIP_FILE_NAME_START)) {
                        historyMap.put("zip", histories.get(j));
                    }
                }
            }
            if (!historyMap.isEmpty()) {
                historyMaps.add(historyMap);
            }
        }
        return historyMaps;
    }
}
