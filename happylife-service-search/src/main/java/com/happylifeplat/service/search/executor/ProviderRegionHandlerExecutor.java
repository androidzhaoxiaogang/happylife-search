package com.happylifeplat.service.search.executor;

import com.happylifeplat.service.search.client.ElasticSearchClient;
import com.happylifeplat.service.search.entity.JobInfo;
import com.happylifeplat.service.search.entity.ProviderRegionEs;
import com.happylifeplat.service.search.helper.LogUtil;
import com.happylifeplat.service.search.helper.SysProps;
import com.happylifeplat.service.search.mapper.ProviderRegionEsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * 供应商区域处理
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/30 9:40
 * @since JDK 1.8
 */
public class ProviderRegionHandlerExecutor implements ElasticSearchExecutor {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsHandlerExecutor.class);

    @Autowired
    private ProviderRegionEsMapper providerRegionEsMapper;

    @Override
    public void execute(JobInfo jobInfo) {
        LogUtil.debug(LOGGER, () -> " 开始执行创建服务区域索引：" + "jobInfo = [" + jobInfo.toString() + "]");
        final String index = jobInfo.getIndex();
        final String type = jobInfo.getType();
        final String param = jobInfo.getParam();
        final String createTime = SysProps.get(param);
        try {
            List<ProviderRegionEs> providerRegionEs =
                    providerRegionEsMapper.listByCreateTime(createTime);
            if (!CollectionUtils.isEmpty(providerRegionEs)) {
                /**
                 * 由于 region表无删除标志 ，无id ，每次都是更新都是删除，再新增 所以
                 * 需要先根据供应商id 删除索引，再新建
                 */
                final List<String> providerIds = providerRegionEs.parallelStream()
                        .map(ProviderRegionEs::getProviderId)
                        .distinct().collect(Collectors.toList());
                //先根据供应商id 删除索引
                ElasticSearchClient.bulkDeleteRegion(index,providerIds);
                final boolean success = ElasticSearchClient.bulkRegionIndex(index, type, providerRegionEs);
                if (success) {
                    SysProps.update(param,LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
                    LogUtil.debug(LOGGER, () -> "服务区域建立索引成功！");
                }
            }
        } catch (Exception e) {
            LogUtil.error(LOGGER,"服务区域建立索引失败：{},", e::getMessage);
        }

    }
}
