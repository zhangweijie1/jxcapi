package com.mrguo.service.impl.basedata.goods;

import com.mrguo.common.exception.CustomsException;
import com.mrguo.dao.goods.GoodsskuMapper;
import com.mrguo.service.impl.basedata.SpecsServiceImpl;
import com.mrguo.service.impl.basedata.UnitServiceImpl;
import com.mrguo.service.impl.basedata.goods.utils.GoodsAddDataUtils;
import com.mrguo.vo.goods.GoodSkuAllInfoVo;
import com.mrguo.vo.goods.GoodsAssemblyVo;
import com.mrguo.entity.bsd.Specs;
import com.mrguo.entity.bsd.Unit;
import com.mrguo.dto.goods.GoodAssemblyDto;
import com.mrguo.entity.goods.GoodsSku;
import com.mrguo.entity.goods.Goodscat;
import com.mrguo.service.inter.bsd.GoodscatService;
import com.mrguo.entity.excle.ExcelImportSkuVo;
import com.mrguo.service.impl.excle.ExcleGoodsImportHandle;
import com.mrguo.dto.goods.GoodAssemblyListDto;
import jxl.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/7/299:53 AM
 * @updater 郭成兴
 * @updatedate 2020/7/299:53 AM
 */
@Service
public class GoodsServiceHandleImport {

    @Autowired
    private ExcleGoodsImportHandle excleGoodsImportHandle;
    @Autowired
    private GoodscatService goodscatService;
    @Autowired
    private UnitServiceImpl unitService;
    @Autowired
    private SpecsServiceImpl specsService;
    @Autowired
    private GoodsAddDataUtils goodsAddDataUtils;
    @Autowired
    private GoodsskuMapper goodsskuMapper;

    GoodAssemblyListDto getImportData(Sheet sheet) throws CustomsException {
        // 1. 转化成ExcleData数据并校验
        initAndValiData(sheet);
        List<ExcelImportSkuVo> excleData = excleGoodsImportHandle.getExcleData();
        // 2. 区分新增，修改
        Map<String, List<ExcelImportSkuVo>> splitExcleData = splitExcleData(excleData);
        List<ExcelImportSkuVo> addExcleData = splitExcleData.get("add");
        List<ExcelImportSkuVo> updateExcleData = splitExcleData.get("update");
        List<GoodAssemblyDto> goodAssemblyDtos = excleData2GoodsParamsList(addExcleData);
        List<GoodSkuAllInfoVo> goodSkuAllInfoVos = excleData2GoodsSkuDtoList(updateExcleData);
        GoodAssemblyListDto goodAssemblyListDto = new GoodAssemblyListDto();
        goodAssemblyListDto.setGoodAssemblyDtos(goodAssemblyDtos);
        goodAssemblyListDto.setGoodSkuAllInfoVos(goodSkuAllInfoVos);
        return goodAssemblyListDto;
    }

    private List<GoodAssemblyDto> excleData2GoodsParamsList(List<ExcelImportSkuVo> excelImportSkuVoList) throws CustomsException {
        // 每一行转化成 GoodsDto
        List<GoodsAssemblyVo> goodsAssemblyVos = excleGoodsImportHandle.excleData2GoodsDataList(excelImportSkuVoList);
        // GoodsDto -> GoodParamList
        ArrayList<GoodAssemblyDto> paramLists = new ArrayList<>();
        for (GoodsAssemblyVo goodsAssemblyVo : goodsAssemblyVos) {
            GoodAssemblyDto goodAssemblyDto = goodsAddDataUtils.splitAssemblyGood(goodsAssemblyVo);
            paramLists.add(goodAssemblyDto);
        }
        return paramLists;
    }

    private List<GoodSkuAllInfoVo> excleData2GoodsSkuDtoList(List<ExcelImportSkuVo> excelImportSkuVoList) throws CustomsException {
        ArrayList<GoodSkuAllInfoVo> skuDtos = new ArrayList<>();
        List<String> skuCodes = excelImportSkuVoList.stream().map(ExcelImportSkuVo::getCode).collect(Collectors.toList());
        List<GoodsSku> goodsskuses = goodsskuMapper.selectIdCodeListByCodeList(skuCodes);
        for (ExcelImportSkuVo excelImportSkuVo : excelImportSkuVoList) {
            GoodSkuAllInfoVo goodSkuAllInfoVo = excleGoodsImportHandle.getSkuDtoByExcleData(excelImportSkuVo);
            //
            GoodsSku sku = goodSkuAllInfoVo.getGoodsSku();
            String code = sku.getCode();
            List<GoodsSku> collect = goodsskuses.stream().filter(item -> {
                return item.getCode().equals(code);
            }).collect(Collectors.toList());
            GoodsSku result = collect.get(0);
            sku.setId(result.getId());
            //
            skuDtos.add(goodSkuAllInfoVo);
        }
        return skuDtos;
    }

    private void initAndValiData(Sheet sheet) throws CustomsException {
        // 1. 获取excleData
        Long storeId = new Long("1286193815601938433");
        excleGoodsImportHandle.initData(sheet, storeId);
        // 2. 获取所有的分类，单位，规格数据（不重复）
        List<String> catNameList = excleGoodsImportHandle.getCatNameList();
        List<String> unitNameList = excleGoodsImportHandle.getUnitNameList();
        List<String> specsNameList = excleGoodsImportHandle.getSpecsMultiNameList();
        List<Goodscat> goodscatList = goodscatService.listDataByNames(catNameList);
        List<Unit> unitList = unitService.listDataByNames(unitNameList);
        List<Specs> specsList = specsService.listDataByNames(specsNameList);
        excleGoodsImportHandle.setCatList(goodscatList);
        excleGoodsImportHandle.setUnitList(unitList);
        excleGoodsImportHandle.setSpecsList(specsList);
        // 校验数据
        excleGoodsImportHandle.valiData();
    }

    private Map<String, List<ExcelImportSkuVo>> splitExcleData(List<ExcelImportSkuVo> excelImportSkuVoList) {
        HashMap<String, List<ExcelImportSkuVo>> hashMap = new HashMap<>();
        List<String> codes = excelImportSkuVoList.stream().map(ExcelImportSkuVo::getCode).collect(Collectors.toList());
        List<String> postList = goodsskuMapper.selectCodesByCodeList(codes);
        List<ExcelImportSkuVo> updateExcleData = excelImportSkuVoList.stream().filter(item -> {
            return postList.contains(item.getCode());
        }).collect(Collectors.toList());
        List<ExcelImportSkuVo> addExcleData = excelImportSkuVoList.stream().filter(item -> {
            return !postList.contains(item.getCode());
        }).collect(Collectors.toList());
        hashMap.put("add", addExcleData);
        hashMap.put("update", updateExcleData);
        return hashMap;
    }
}
