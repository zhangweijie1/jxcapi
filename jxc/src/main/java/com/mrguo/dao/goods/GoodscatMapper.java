package com.mrguo.dao.goods;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.bsd.CustomerCat;
import com.mrguo.entity.goods.Goodscat;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName GoodscatMapper
 * @Description 商品类别接口
 * @date 2019/8/5 6:27 PM
 * @updater 郭成兴
 * @updatedate 2019/8/5 6:27 PM
 */
@Repository("goodscatMapper")
public interface GoodscatMapper extends MyMapper<Goodscat> {

    /**
     * 根据父节点查找商品类别
     *
     * @param parentId
     * @return
     */
    @Select("select * from bsd_goods_cat where p_id=#{parentId}")
    List<Goodscat> findByParentId(String parentId);

    /**
     * 根据父id获取son的个数
     *
     * @param pid
     * @return
     */
    @Select("select count(1) from bsd_goods_cat where p_id = #{pid}")
    int countChildrens(@Param("pid") String pid);

    @Select("select max(id) from bsd_goods_cat where p_id = #{pid}")
    String getMaxIdByPid(@Param("pid") String pid);

    List<Goodscat> listDataByNames(List<String> names);

    @Select("select count(1) from bsd_goods_cat where name = #{name}")
    int countByName(@Param("name") String name);

}