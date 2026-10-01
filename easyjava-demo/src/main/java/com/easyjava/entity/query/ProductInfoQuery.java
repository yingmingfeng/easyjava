package com.easyjava.entity.query;
import java.util.Date;
import java.math.BigDecimal;
import lombok.Setter;
import lombok.Getter;
/**
 * @author 应明沣
 * @description 商品信息
 * @date 2026/10/02 00:58:21
 */
@Getter
@Setter
public class ProductInfoQuery extends BaseQuery {
	/**
	 * 自增ID 
	 */
	private Integer id;

	/**
	 * 公司ID 
	 */
	private String companyId;

	private String companyIdFuzzy;

	/**
	 * 商品编号 
	 */
	private String code;

	private String codeFuzzy;

	/**
	 * 商品名称 
	 */
	private String productName;

	private String productNameFuzzy;

	/**
	 * 价格 
	 */
	private BigDecimal price;

	/**
	 * sku类型 
	 */
	private Integer skuType;

	/**
	 * 颜色类型 
	 */
	private Integer colorType;

	/**
	 * 创建时间 
	 */
	private Date createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * 创建日期 
	 */
	private Date createDate;

	private String createDateStart;

	private String createDateEnd;

	/**
	 * 库存 
	 */
	private Long stock;

	/**
	 * 状态：0 未上架 1已上架 
	 */
	private Integer status;

	/**
	 * 0:删除 1:正常 
	 */
	private Integer isDel;


}