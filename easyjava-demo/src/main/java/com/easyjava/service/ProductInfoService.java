package com.easyjava.service;
import com.easyjava.entity.po.ProductInfo;
import com.easyjava.entity.query.ProductInfoQuery;
import com.easyjava.entity.vo.PaginationResultVO;
import java.util.List;
/**
 * @author 应明沣
 * @description 商品信息Service接口
 * @date 2026/10/02 00:58:21
 */
public interface ProductInfoService {

	/**
	 * 根据条件查询列表 
	 */
	List<ProductInfo> findListByParam(ProductInfoQuery query);

	/**
	 * 根据条件查询数量 
	 */
	Integer findCountByParam(ProductInfoQuery query);

	/**
	 * 根据条件查询列表,并对结果进行分页 
	 */
	PaginationResultVO<ProductInfo> findListByPage(ProductInfoQuery query);

	/**
	 * 新增 
	 */
	Integer add(ProductInfo bean);

	/**
	 * 批量新增 
	 */
	Integer addBatch(List<ProductInfo> listBean);

	/**
	 * 批量新增或修改 
	 */
	Integer addOrUpdateBatch(List<ProductInfo> listBean);

	/**
	 * 根据Id查询 
	 */
	ProductInfo getProductInfoById(Integer id);

	/**
	 * 根据Id更新 
	 */
	Integer updateProductInfoById(ProductInfo bean, Integer id);

	/**
	 * 根据Id删除 
	 */
	Integer deleteProductInfoById(Integer id);

	/**
	 * 根据Code查询 
	 */
	ProductInfo getProductInfoByCode(String code);

	/**
	 * 根据Code更新 
	 */
	Integer updateProductInfoByCode(ProductInfo bean, String code);

	/**
	 * 根据Code删除 
	 */
	Integer deleteProductInfoByCode(String code);

	/**
	 * 根据SkuTypeAndColorType查询 
	 */
	ProductInfo getProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType);

	/**
	 * 根据SkuTypeAndColorType更新 
	 */
	Integer updateProductInfoBySkuTypeAndColorType(ProductInfo bean, Integer skuType, Integer colorType);

	/**
	 * 根据SkuTypeAndColorType删除 
	 */
	Integer deleteProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType);


}