package com.happylifeplat.service.search.facade;


import com.happylifeplat.facade.search.entity.SearchRequest;
import com.happylifeplat.facade.search.enums.SortType;
import com.happylifeplat.facade.search.exception.SearchException;
import com.happylifeplat.facade.search.service.ElasticSearchService;
import com.happylifeplat.service.search.helper.RegionIdUtils;
import com.happylifeplat.service.search.query.SearchEntity;
import org.springframework.stereotype.Service;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *  es搜索服务
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/29 17:42
 * @since JDK 1.8
 */
@Service("elasticSearchService")
public class ElasticSearchServiceImpl  implements ElasticSearchService{


    /**
     * es 查询接口
     *
     * @param searchRequest 查询参数
     * @return json格式字符串
     * @throws SearchException 异常信息
     */
    @Override
    public String search(SearchRequest searchRequest) throws SearchException {
        SearchEntity searchEntity =  buildSearchEntity(searchRequest);


        return null;
    }

    private SearchEntity buildSearchEntity(SearchRequest searchRequest){
        SearchEntity searchEntity = new SearchEntity();
        searchEntity.setKeywords(searchRequest.getKeywords());
        searchEntity.setRegionId(RegionIdUtils.convert(searchRequest.getRegionId()));
        searchEntity.setPage(searchRequest.getPage());
        searchEntity.setSize(searchRequest.getSize());
        searchEntity.setSortOrder(searchRequest.getSort());
        if(searchRequest.getSortType().equals(SortType.PRICE.toString())){
            searchEntity.setOrderField("price");
        }else{
            searchEntity.setOrderField("index");
        }

        //设置多个字段查询
        String[] fields = new String[]{"name"};
        searchEntity.setFields(fields);
        return  searchEntity;
    }
}
