package com.easyjava.controller;

import com.easyjava.entity.po.ProductInfo;
import com.easyjava.entity.query.ProductInfoQuery;

import java.util.List;

import com.easyjava.entity.vo.ResponseVO;
import com.easyjava.service.ProductInfoService;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @author 应明沣
 * @description 商品信息Controller实现类
 * @date 2026/10/02 00:58:21
 */
@RestController
@RequestMapping("/productInfo")
public class ProductInfoController extends ABaseController {

	@Resource
	private ProductInfoService productInfoService;

	/**
	 * 根据条件分页查询 
	 */
	@RequestMapping("loadDataList")
	public ResponseVO loadDataList(ProductInfoQuery query){
		return this.getSuccessResponseVO(productInfoService.findListByPage(query));
	}

	/**
	 * 新增 
	 */
	@RequestMapping("add")
	public ResponseVO add(ProductInfo bean){
		productInfoService.add(bean);
		return this.getSuccessResponseVO(null);
	}

	/**
	 * 批量新增 
	 */
	@RequestMapping("addBatch")
	public ResponseVO addBatch(@RequestBody List<ProductInfo> listBean){
		productInfoService.addBatch(listBean);
		return this.getSuccessResponseVO(null);
	}

	/**
	 * 批量新增或修改 
	 */
	@RequestMapping("addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<ProductInfo> listBean){
		productInfoService.addOrUpdateBatch(listBean);
		return this.getSuccessResponseVO(null);
	}

	/**
	 * 根据Id查询 
	 */
	@RequestMapping("getProductInfoById")
	public ResponseVO getProductInfoById(Integer id){
		return this.getSuccessResponseVO(productInfoService.getProductInfoById(id));
	}

	/**
	 * 根据Id更新 
	 */
	@RequestMapping("updateProductInfoById")
	public ResponseVO updateProductInfoById(ProductInfo bean, Integer id){
		productInfoService.updateProductInfoById(bean, id);
		return this.getSuccessResponseVO(null);
	}

	/**
	 * 根据Id删除 
	 */
	@RequestMapping("deleteProductInfoById")
	public ResponseVO deleteProductInfoById(Integer id){
		productInfoService.deleteProductInfoById(id);
		return this.getSuccessResponseVO(null);
	}

	/**
	 * 根据Code查询 
	 */
	@RequestMapping("getProductInfoByCode")
	public ResponseVO getProductInfoByCode(String code){
		return this.getSuccessResponseVO(productInfoService.getProductInfoByCode(code));
	}

	/**
	 * 根据Code更新 
	 */
	@RequestMapping("updateProductInfoByCode")
	public ResponseVO updateProductInfoByCode(ProductInfo bean, String code){
		productInfoService.updateProductInfoByCode(bean, code);
		return this.getSuccessResponseVO(null);
	}

	/**
	 * 根据Code删除 
	 */
	@RequestMapping("deleteProductInfoByCode")
	public ResponseVO deleteProductInfoByCode(String code){
		productInfoService.deleteProductInfoByCode(code);
		return this.getSuccessResponseVO(null);
	}

	/**
	 * 根据SkuTypeAndColorType查询 
	 */
	@RequestMapping("getProductInfoBySkuTypeAndColorType")
	public ResponseVO getProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType){
		return this.getSuccessResponseVO(productInfoService.getProductInfoBySkuTypeAndColorType(skuType, colorType));
	}

	/**
	 * 根据SkuTypeAndColorType更新 
	 */
	@RequestMapping("updateProductInfoBySkuTypeAndColorType")
	public ResponseVO updateProductInfoBySkuTypeAndColorType(ProductInfo bean, Integer skuType, Integer colorType){
		productInfoService.updateProductInfoBySkuTypeAndColorType(bean, skuType, colorType);
		return this.getSuccessResponseVO(null);
	}

	/**
	 * 根据SkuTypeAndColorType删除 
	 */
	@RequestMapping("deleteProductInfoBySkuTypeAndColorType")
	public ResponseVO deleteProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType){
		productInfoService.deleteProductInfoBySkuTypeAndColorType(skuType, colorType);
		return this.getSuccessResponseVO(null);
	}

}