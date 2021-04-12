package com.mrguo.service.inter.bsd;

import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.entity.goods.Goodscat;

import java.util.List;

/**
 * 商品类别Service接口
 *
 * @author mrguo
 */
public interface GoodscatService {

    /**
     * 根据id查询商品类别实体
     *
     * @param id
     * @return
     */
    public Goodscat findById(String id);

    /**
     * 根据父节点查找商品类别
     *
     * @param parentId
     * @return
     */
    public List<Goodscat> findByParentId(String parentId);


    int countByParentId(String parentId);

    /**
     * 添加或者修改商品类别信息
     *
     * @param goodsType
     */
    public Result addOrUpdateData(Goodscat goodsType) throws CustomsException;

    /**
     * 根据id删除商品类别信息
     *
     * @param id
     */
    public Result delete(String id) throws CustomsException;

    List<Goodscat> listAllData();

    List<Goodscat> listDataByNames(List<String> names);
}
