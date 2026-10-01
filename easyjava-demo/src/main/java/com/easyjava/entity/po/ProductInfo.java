package com.easyjava.entity.po;
import java.io.Serializable;
import com.easyjava.enums.DateTimePatternEnum;
import com.easyjava.utils.DateUtils;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Setter;
import lombok.Getter;
/**
 * @author 应明沣
 * @description 商品信息
 * @date 2026/10/02 00:58:21
 */
@Getter
@Setter
@JsonIgnoreProperties({"companyId","status"})
public class ProductInfo implements Serializable {
	/**
	 * 自增ID 
	 */
	private Integer id;

	/**
	 * 公司ID 
	 */
	private String companyId;

	/**
	 * 商品编号 
	 */
	private String code;

	/**
	 * 商品名称 
	 */
	private String productName;

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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 创建日期 
	 */
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDate;

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

	@Override
	public String toString() {
		return "自增ID:" + (id == null ? "空" : id) + ",公司ID:" + (companyId == null ? "空" : companyId) + ",商品编号:" + (code == null ? "空" : code) + ",商品名称:" + (productName == null ? "空" : productName) + ",价格:" + (price == null ? "空" : price) + ",sku类型:" + (skuType == null ? "空" : skuType) + ",颜色类型:" + (colorType == null ? "空" : colorType) + ",创建时间:" + (createTime == null ? "空" : DateUtils.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + ",创建日期:" + (createDate == null ? "空" : DateUtils.format(createDate, DateTimePatternEnum.YYYY_MM_DD.getPattern())) + ",库存:" + (stock == null ? "空" : stock) + ",状态：0 未上架 1已上架:" + (status == null ? "空" : status) + ",0:删除 1:正常:" + (isDel == null ? "空" : isDel);
	}
}