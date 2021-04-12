package com.mrguo.vo.goods;

import com.mrguo.entity.goods.GoodsSpu;
import lombok.Data;
import lombok.NonNull;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author 郭成兴
 * @ClassName GoodsDto
 * @Description 商品组合新增Vo
 * @date 2019/8/1710:28 PM
 * @updater 郭成兴
 * @updatedate 2019/8/1710:28 PM
 */
@Data
public class GoodsAssemblyVo {

    /**
     * spu信息
     */
    @NotNull(message = "商品SPU不能为空")
    @Valid
    private GoodsSpu goodsSpu;

    /**
     * sku信息
     */
    @NotNull(message = "商品SKU信息不能为空")
    @Valid
    List<GoodSkuAllInfoVo> goodSkuAllInfoVoList;
}
