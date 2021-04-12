package com.mrguo.dto.goods;

import com.mrguo.vo.goods.GoodSkuAllInfoVo;
import lombok.Data;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/7/2910:26 AM
 * @updater 郭成兴
 * @updatedate 2020/7/2910:26 AM
 */
@Data
public class GoodAssemblyListDto {

    List<GoodAssemblyDto> goodAssemblyDtos;

    List<GoodSkuAllInfoVo> goodSkuAllInfoVos;
}
