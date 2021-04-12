package com.mrguo.dao.bill;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/7/88:22 AM
 * @updater 郭成兴
 * @updatedate 2020/7/88:22 AM
 */
@Repository("billTrackingMapper")
public interface BillTrackingMapper {

    List<Map<String, Object>> selectSaleTrack(Page page, @Param("record") Map map);
}
