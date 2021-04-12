package com.mrguo.service.impl.basedata.goods;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.dao.bill.BillDetailMapper;
import com.mrguo.dao.goods.GoodspriceMapper;
import com.mrguo.dao.goods.GoodsskuMapper;
import com.mrguo.dto.goods.SkuInfoDto;
import com.mrguo.entity.bsd.Level;
import com.mrguo.dto.goods.SkuInfoListDto;
import com.mrguo.service.impl.basedata.*;
import com.mrguo.vo.goods.GoodSkuAllInfoVo;
import com.mrguo.entity.excle.ExcelExportSkuVo;
import com.mrguo.entity.goods.*;
import com.mrguo.service.impl.bill.uils.BillOrderNoServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品Service实现类
 *
 * @author mrguo
 */
@Service("goodsSkuServiceImpl")
public class GoodsSkuServiceImpl {

    @Autowired
    private GoodsskuMapper goodsskuMapper;
    @Autowired
    private GoodspriceMapper goodspriceMapper;
    @Autowired
    private BillDetailMapper billDetailMapper;
    @Autowired
    private GoodsLevelServiceImpl goodsLevelService;
    @Autowired
    private LevelServiceImpl levelService;
    @Autowired
    private BillOrderNoServiceImpl billOrderNoService;
    @Autowired
    private GoodsPriceServiceImpl goodspriceService;
    @Autowired
    private GoodsUnitServiceImpl goodsUnitService;
    @Autowired
    private GoodsBarcodeServiceImpl goodsBarcodeService;
    @Autowired
    private GoodsStockServiceImpl goodsStockService;
    @Autowired
    private GoodsStockWarnServiceImpl goodsStockWarnService;

    /**
     * 新增一个sku
     *
     * @param goodSkuAllInfoVo
     * @throws CustomsException
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addData(GoodSkuAllInfoVo goodSkuAllInfoVo) throws CustomsException {
        valiAddData(goodSkuAllInfoVo);
        // sku表
        GoodsSku goodssku = goodSkuAllInfoVo.getGoodsSku();
        goodssku.setId(IDUtil.getSnowflakeId());
        goodssku.setCreateTime(new Date());
        goodssku.setUpdateTime(goodssku.getCreateTime());
        goodsskuMapper.insertSelective(goodssku);
        Long skuId = goodssku.getId();
        // 新增关系
        addSkuRelationData(goodSkuAllInfoVo);
        // stock origin
        List<SkuInfoDto> skuInfoDtos = goodSkuAllInfoVo.getGoodsStockList();
        ArrayList<GoodsStock> goodsStockList = new ArrayList<>();
        for (SkuInfoDto skuInfoDto : skuInfoDtos) {
            GoodsStock goodsStock = new GoodsStock();
            goodsStock.setSkuId(skuId);
            goodsStock.setOriginQuantity(skuInfoDto.getOriginQuantity());
            goodsStockList.add(goodsStock);
        }
        goodsStockService.addListData(goodsStockList);
        // stock warn
        List<GoodsStockWarn> goodsStockWarnList = goodSkuAllInfoVo.getGoodsStockWarnList();
        for (GoodsStockWarn goodsStockWarn : goodsStockWarnList) {
            goodsStockWarn.setSkuId(skuId);
        }
        goodsStockWarnService.addStockWarnListData(goodsStockWarnList);
        return Result.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result delUnitData(GoodsUnit goodsUnit) throws CustomsException {
        List<String> unitIds = new ArrayList<>();
        unitIds.add(String.valueOf(goodsUnit.getUnitId()));
        List<Long> relationUnitIdsList = billDetailMapper.selectUnitsBySkuIdUnitIds(goodsUnit.getSkuId(), unitIds);
        if (relationUnitIdsList.size() > 0) {
            throw new CustomsException("该单位已有单据关联，不能删除!");
        }
        goodsUnitService.delDataBySkuIdUnitId(goodsUnit.getSkuId(), goodsUnit.getUnitId());
        return Result.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result delData(Long skuId) throws CustomsException {
        if (billDetailMapper.countBySkuId(skuId) > 0) {
            throw new CustomsException("该商品在单据中已经存，不能删除！");
        }
        if (goodsskuMapper.deleteByPrimaryKey(skuId) == 0) {
            throw new CustomsException("商品删除失败！");
        }
        return Result.ok();
    }

    /**
     * 编辑一个sku
     * 校验 -> 单位：单位可能新增或者删除
     * 所有和单位有关系的关联属性删除方式 -> 删除+新增
     * <p>
     * 校验 -> 条形码: 条形码只能一个
     * 条形码允许空，所以不能数据库层面做唯一索引
     *
     * @param goodSkuAllInfoVo
     * @throws CustomsException
     */
    @Transactional(rollbackFor = Exception.class)
    public Result updateData(GoodSkuAllInfoVo goodSkuAllInfoVo) throws CustomsException {
        valiUpdateData(goodSkuAllInfoVo);
        Long skuId = goodSkuAllInfoVo.getGoodsSku().getId();
        // 和单位有关属性
        delSkuRelationData(skuId);
        addSkuRelationData(goodSkuAllInfoVo);
        // 期初库存，预警信息
        // stock origin
        List<SkuInfoDto> skuInfoDtos = goodSkuAllInfoVo.getGoodsStockList();
        List<GoodsStock> goodsStockList = new ArrayList<>();
        for (SkuInfoDto skuInfoDto : skuInfoDtos) {
            GoodsStock goodsStock = getGoodStockBySkuInfo(skuInfoDto);
            goodsStock.setSkuId(skuId);
            goodsStockList.add(goodsStock);
        }
        // stock warn
        List<GoodsStockWarn> goodsStockWarnList = goodSkuAllInfoVo.getGoodsStockWarnList();
        for (GoodsStockWarn goodsStockWarn : goodsStockWarnList) {
            goodsStockWarn.setSkuId(skuId);
        }
        if (goodsskuMapper.updateByPrimaryKeySelective(goodSkuAllInfoVo.getGoodsSku()) <= 0) {
            throw new CustomsException("商品修改失败");
        }
        if (goodsStockService.batchUpdateByPrimaryKeySelective(goodsStockList) <= 0) {
            throw new CustomsException("期初库存修改失败");
        }
        if (goodsStockWarnService.batchUpdateByPrimaryKeySelective(goodsStockWarnList) <= 0) {
            throw new CustomsException("库存预警修改失败");
        }
        return Result.ok();
    }

    public void addListData(List<GoodsSku> goodsSkuList) throws CustomsException {
        if (goodsskuMapper.insertList(goodsSkuList) < 0) {
            throw new CustomsException("批量插入sku失败！");
        }
    }

    public Result listData(PageParams<GoodsSku> pageParams) throws IOException {
        Page<GoodsSku> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(goodsskuMapper.listCustom(page, data));
        return Result.ok(page);
    }

    public Result selectContainerStockByStore(PageParams<GoodsSku> pageParams) throws IOException, CustomsException {
        Page<GoodsSku> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        if (data.get("storeId") == null) {
            throw new CustomsException("仓库ID不能为空！");
        }
        page.setRecords(goodsskuMapper.selectContainerStockByStore(page, data));
        return Result.ok(page);
    }

    /**
     * 查询商品包含更多信息（如：价格，单位等）
     *
     * @param pageParams
     * @return
     * @throws IOException
     * @throws CustomsException
     */
    public Result listDataMore(PageParams<GoodsSku> pageParams) throws IOException, CustomsException {
        Page<GoodsSku> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(goodsskuMapper.listDataMore(page, data));
        return Result.ok(page);
    }

    public List<ExcelExportSkuVo> exportExcleData(Map map) {
        return goodsskuMapper.exportExcleData(map);
    }

    /**
     * 查询热销商品
     *
     * @param pageParams
     * @return
     * @throws IOException
     * @throws CustomsException
     */
    public Result selectHotSaleData(PageParams<GoodsSku> pageParams) throws IOException, CustomsException {
        Page<GoodsSku> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(goodsskuMapper.selectHotSaleData(page, data));
        return Result.ok(page);
    }

    /**
     * 查询高库存商品
     *
     * @param pageParams
     * @return
     * @throws IOException
     * @throws CustomsException
     */
    public Result selectMaxStockByStore(PageParams<GoodsSku> pageParams) throws IOException, CustomsException {
        Page<GoodsSku> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        if (data.get("storeId") == null) {
            throw new CustomsException("仓库ID不能为空！");
        }
        ArrayList<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setColumn("can_use_quantity");
        orderItem.setAsc(false);
        orderItems.add(orderItem);
        page.setOrders(orderItems);
        page.setRecords(goodsskuMapper.selectContainerStockByStore(page, data));
        return Result.ok(page);
    }

    /**
     * 查询低库存商品
     *
     * @param pageParams
     * @return
     * @throws IOException
     * @throws CustomsException
     */
    public Result selectMinStockByStore(PageParams<GoodsSku> pageParams) throws IOException, CustomsException {
        Page<GoodsSku> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        if (data.get("storeId") == null) {
            throw new CustomsException("仓库ID不能为空！");
        }
        ArrayList<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setColumn("can_use_quantity");
        orderItem.setAsc(true);
        orderItems.add(orderItem);
        page.setOrders(orderItems);
        page.setRecords(goodsskuMapper.selectContainerStockByStore(page, data));
        return Result.ok(page);
    }

    /**
     * 获取价格 by skuid + unitId
     *
     * @param skuId
     * @param unitId
     * @return
     */
    public Result getPriceBySkuAndUnitId(Long skuId, Long unitId) {
        GoodsPrice goodsprice = goodspriceMapper.selectOneBySkuAndUnitId(skuId, unitId);
        if (goodsprice == null) {
            return Result.fail("价格信息不存在!");
        }
        return Result.ok(goodsprice);
    }

    /**
     * 获取sku信息byid
     * 包含分类，价格，单位，条形码
     * 库存信息
     *
     * @param skuId
     * @return
     */
    public SkuInfoListDto getSkuInfoById(Long skuId) {
        GoodsSku goodssku = goodsskuMapper.selectAllDetailById(skuId);
        List<SkuInfoDto> skuInfoDtos = goodsskuMapper.selectAllStockBySkuId(skuId);
        SkuInfoListDto skuInfoListDto = new SkuInfoListDto();
        skuInfoListDto.setGoodssku(goodssku);
        skuInfoListDto.setSkuInfoDtoList(skuInfoDtos);
        return skuInfoListDto;
    }

    /**
     * 获取sku  by 条形码
     *
     * @param barcode
     * @return
     */
    public GoodsSku getSkuByBarcode(String barcode) {
        return goodsskuMapper.selectByBarcode(barcode);
    }

    public Result getGoodSkuCode() throws CustomsException {
        return billOrderNoService.getSkuCode();
    }

    /**
     * 获取商品sku的等级折扣
     * 1. 若没有设置，则获取默认等级折扣
     *
     * @param skuIds
     * @param levelId
     * @return
     * @throws CustomsException
     */
    public Result getDiscountBySkuIdsAndLevelId(List<String> skuIds, Long levelId) throws CustomsException {
        List<GoodsLevel> goodsLevels = goodsLevelService.selectBySkuIdsAndLevelId(skuIds, levelId);
        Level level = levelService.selectByPrimaryKey(levelId);
        if (level == null) {
            return Result.fail("该等级不存在！");
        }
        if (goodsLevels.size() != 0) {
            List<HashMap<Object, Object>> maps = skuIds.stream().map((skuId) -> {
                HashMap<Object, Object> map = new HashMap<>();
                List<GoodsLevel> levels = goodsLevels.stream().filter(item -> {
                    return skuId.equals(String.valueOf(item.getSkuId()));
                }).collect(Collectors.toList());
                map.put("skuId", skuId);
                map.put("levelId", levelId);
                if (levels.size() == 0) {
                    map.put("discount", level.getDiscount());
                } else {
                    GoodsLevel goodsLevel = levels.get(0);
                    map.put("discount", goodsLevel.getDiscount());
                }
                return map;
            }).collect(Collectors.toList());
            return Result.ok(maps);
        } else {
            List<HashMap<Object, Object>> maps = skuIds.stream().map((item) -> {
                HashMap<Object, Object> map = new HashMap<>();
                map.put("skuId", item);
                map.put("levelId", levelId);
                map.put("discount", level.getDiscount());
                return map;
            }).collect(Collectors.toList());
            return Result.ok(maps);
        }
    }

    /**
     * 删除sku相关的表数据
     */
    private void delSkuRelationData(Long skuId) throws CustomsException {
        // 单位
        goodsUnitService.delDataBySkuId(skuId);
        // 价格数据
        goodspriceService.delDataBySkuId(skuId);
        // 条形码
        goodsBarcodeService.delDataBySkuId(skuId);
    }

    private void addSkuRelationData(GoodSkuAllInfoVo goodSkuAllInfoVo) throws CustomsException {
        Long skuId = goodSkuAllInfoVo.getGoodsSku().getId();
        // 单位
        List<GoodsUnit> goodsUnitList = goodSkuAllInfoVo.getGoodsUnitList();
        for (GoodsUnit goodsUnit : goodsUnitList) {
            goodsUnit.setSkuId(skuId);
        }
        goodsUnitService.addListData(goodsUnitList);
        // 价格数据
        List<GoodsPrice> goodsPriceList = goodSkuAllInfoVo.getGoodsPriceList();
        for (GoodsPrice goodsprice : goodsPriceList) {
            goodsprice.setSkuId(skuId);
        }
        goodspriceService.addPriceListData(goodsPriceList);
        // 条形码
        List<GoodsBarcode> goodsBarcodeList = goodSkuAllInfoVo.getGoodsBarcodeList();
        for (GoodsBarcode goodsBarcode : goodsBarcodeList) {
            goodsBarcode.setSkuId(skuId);
        }
        goodsBarcodeService.addBarcodeListData(goodsBarcodeList);
    }


    /**
     * 修改时校验数据
     *
     * @param goodSkuAllInfoVo
     */
    private void valiUpdateData(GoodSkuAllInfoVo goodSkuAllInfoVo) throws CustomsException {
        valiUnitData(goodSkuAllInfoVo);
        valiBarcodeData(goodSkuAllInfoVo);
    }

    /**
     * 新增时校验
     *
     * @param goodSkuAllInfoVo
     * @throws CustomsException
     */
    private void valiAddData(GoodSkuAllInfoVo goodSkuAllInfoVo) throws CustomsException {
        valiBarcodeData(goodSkuAllInfoVo);
    }

    /**
     * 校验单位，改变的该sku的单位不能在单据中引用
     *
     * @param goodSkuAllInfoVo
     */
    private void valiUnitData(GoodSkuAllInfoVo goodSkuAllInfoVo) throws CustomsException {
        GoodsSku goodssku = goodSkuAllInfoVo.getGoodsSku();
        List<GoodsUnit> goodsUnitList = goodSkuAllInfoVo.getGoodsUnitList();
        List<GoodsUnit> changeUnitList = getChangeUnitList(goodssku, goodsUnitList);
        List<String> unitIds = changeUnitList.stream().map(item -> {
            return String.valueOf(item.getUnitId());
        }).collect(Collectors.toList());
        // 已经关联单据的unit
        List<Long> relationUnitIdsList = billDetailMapper.selectUnitsBySkuIdUnitIds(goodssku.getId(), unitIds);
        List<GoodsUnit> relationUnitList = changeUnitList.stream().filter(item -> {
            return relationUnitIdsList.contains(item.getUnitId());
        }).collect(Collectors.toList());
        // 中文
        List<String> relationUnitName = relationUnitList.stream().map(GoodsUnit::getUnitName).collect(Collectors.toList());
        if (relationUnitList.size() > 0) {
            throw new CustomsException("单位[" + relationUnitName.toString() + "]已有单据关联，不能删除或修改!");
        }
    }

    /**
     * 校验条形码
     *
     * @param goodSkuAllInfoVo
     * @throws CustomsException
     */
    private void valiBarcodeData(GoodSkuAllInfoVo goodSkuAllInfoVo) throws CustomsException {
        GoodsSku goodssku = goodSkuAllInfoVo.getGoodsSku();
        // 1 前端来的数据是否有相同的
        List<GoodsBarcode> barcodeListReq = goodSkuAllInfoVo.getGoodsBarcodeList();
        List<String> codeListReq = new ArrayList<>();
        for (GoodsBarcode barcode : barcodeListReq) {
            if (codeListReq.contains(barcode.getBarcode())) {
                throw new CustomsException("条形码[" + barcode.getBarcode() + "]重复！");
            }
            if (StringUtils.isNotBlank(barcode.getBarcode())) {
                codeListReq.add(barcode.getBarcode());
            }
        }
        // 2 前端没有重复
        if (goodssku.getId() == null) {
            // 新增状态，直接查询数据库是否有barcode
            List<String> barcodeListByCodes = goodsBarcodeService.getBarcodeListByCodes(codeListReq);
            if (barcodeListByCodes.size() > 0) {
                throw new CustomsException("条形码[" + barcodeListByCodes.toString() + "]已存在！");
            }
        } else {
            // 修改状态
            List<String> barcodeListBySkuIdPost = goodsBarcodeService.getBarcodeListBySkuId(goodssku.getId());
            List<String> barcodeListChange = codeListReq.stream().filter(item -> {
                return !barcodeListBySkuIdPost.contains(item);
            }).collect(Collectors.toList());
            if (barcodeListChange.size() > 0) {
                List<String> barcodeListByCodes = goodsBarcodeService.getBarcodeListByCodes(barcodeListChange);
                if (barcodeListByCodes.size() > 0) {
                    throw new CustomsException("条形码[" + barcodeListByCodes.toString() + "]已存在！");
                }
            }
        }
    }

    /**
     * 和已经存在的goodsunit对比，获取改变了的unit
     * 包含，改变的 + 删除的
     *
     * @return
     */
    private List<GoodsUnit> getChangeUnitList(GoodsSku goodssku, List<GoodsUnit> goodsUnitListReq) {
        Long skuId = goodssku.getId();
        List<GoodsUnit> goodsUnitListPost = goodsUnitService.getUnitListBySkuId(skuId);
        List<Long> skuidReqList = goodsUnitListReq.stream().map(item -> {
            return item.getUnitId();
        }).collect(Collectors.toList());

        List<GoodsUnit> skuExceptionIdList = new ArrayList<>();
        for (GoodsUnit unit : goodsUnitListPost) {
            if (skuidReqList.contains(unit.getUnitId())) {
                // 改变的
                GoodsUnit goodsUnit = goodsUnitListReq.stream().filter(item ->
                        item.getUnitId().equals(unit.getUnitId())
                ).findAny().get();
                if (unit.getMulti().compareTo(goodsUnit.getMulti()) != 0) {
                    skuExceptionIdList.add(unit);
                }
            } else {
                // 删除的
                skuExceptionIdList.add(unit);
            }
        }
        return skuExceptionIdList;
    }

    private GoodsStock getGoodStockBySkuInfo(SkuInfoDto skuInfoDto) {
        GoodsStock goodsStock = new GoodsStock();
        goodsStock.setSkuId(skuInfoDto.getSkuId());
        goodsStock.setOriginQuantity(skuInfoDto.getOriginQuantity());
        goodsStock.setStoreId(skuInfoDto.getStoreId());
        return goodsStock;
    }
}
