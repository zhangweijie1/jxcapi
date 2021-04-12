package com.mrguo.dto.goods;

import com.mrguo.entity.goods.GoodsStock;
import com.mrguo.entity.goods.GoodsSku;
import lombok.Data;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName GoodSkuDetailDto
 * @Description 商品详情Dto
 * @date 2019/8/1710:28 PM
 * @updater 郭成兴
 * @updatedate 2019/8/1710:28 PM
 */
@Data
public class SkuInfoListDto {

    private GoodsSku goodssku;

    private List<SkuInfoDto> skuInfoDtoList;
}
