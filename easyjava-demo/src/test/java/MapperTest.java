import com.easyjava.RunDemoApplication;
import com.easyjava.entity.po.ProductInfo;
import com.easyjava.entity.query.ProductInfoQuery;
import com.easyjava.mappers.ProductInfoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yingmingfeng
 * @classname MapperTest
 * @description 测试模板代码
 * @date 2026/9/28 23:39
 */
@SpringBootTest(classes = RunDemoApplication.class)
@RunWith(SpringRunner.class)
public class MapperTest {
    @Resource
    private ProductInfoMapper<ProductInfo, ProductInfoQuery> productInfoMapper;

    @Test
    public void selectList() {
        ProductInfoQuery query = new ProductInfoQuery();
        query.setSkuType(2);
        query.setCreateDateStart("2025-01-05");
        query.setCodeFuzzy("P00");
        List<ProductInfo> dataList = productInfoMapper.selectList(query);
        for(ProductInfo productInfo : dataList){
            System.out.println(productInfo);
        }

        Integer count = productInfoMapper.selectCount(query);
        System.out.println(count);
    }

    @Test
    public void insert() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCompanyId("C005");
        productInfo.setCode("P011");
        productInfo.setProductName("测试商品1");
        productInfo.setPrice(new BigDecimal(1999));
        productInfo.setSkuType(4);
        productInfo.setStock(100L);
        productInfo.setStatus(1);
        productInfo.setIsDel(0);
        productInfo.setColorType(0);
        productInfo.setCreateTime(new Date());
        productInfo.setCreateDate(new Date());
        productInfoMapper.insert(productInfo);
        System.out.println(productInfo.getId());
    }

    @Test
    public void insertOrUpdate(){
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(25);
        productInfo.setCompanyId("C006666");
        productInfo.setCode("77777");

        productInfoMapper.insertOrUpdate(productInfo);
        System.out.println(productInfo.getId());
    }

    //进行批量操作的时候，所有的值都要填充进去，否则就是空了
    @Test
    public void insertBatch(){
        List<ProductInfo> productList = new ArrayList<>();
        for(int i = 1; i < 10; i++){
            ProductInfo productInfo = new ProductInfo();
            productInfo.setCreateDate(new Date());
            productInfo.setProductName("测试批量插入" + i);
            productInfo.setCode("000000" + i);
            productList.add(productInfo);
        }

        productInfoMapper.insertBatch(productList);
    }

    @Test
    public void insertOrUpdateBatch(){
        List<ProductInfo> productList = new ArrayList<>();
        for(int i = 1; i < 10; i++){
            ProductInfo productInfo = new ProductInfo();
            productInfo.setCreateDate(new Date());
            productInfo.setProductName("测试批量插入或更新" + i);
            productInfo.setCode("000000" + i);
            productList.add(productInfo);
        }

        productInfoMapper.insertOrUpdateBatch(productList);
    }

    @Test
    public void selectByKey(){
        ProductInfo productInfo = productInfoMapper.selectById(25);
        System.out.println(productInfo);
    }

    @Test
    public void deleteByKey(){
        productInfoMapper.deleteById(91);
        System.out.println("删除成功");
    }

    @Test
    public void updateByKey(){
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductName("智能666");
        productInfo.setSkuType(3);
        productInfoMapper.updateById(productInfo, 19);
        System.out.println("更新成功");
    }

    @Test
    public void selectByCode(){
        ProductInfo productInfo = productInfoMapper.selectByCode("P006");
        System.out.println(productInfo);
    }

    @Test
    public void deleteByCode(){
        productInfoMapper.deleteByCode("66666");
        System.out.println("删除成功");
    }

    @Test
    public void updateByCode(){
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductName("智能777");
        //productInfo.setSkuType(3);
        Integer count = productInfoMapper.updateByCode(productInfo, "P010");
        System.out.println(count);
    }

    @Test
    public void selectBySkuTypeAndColorType(){
        ProductInfo productInfo = productInfoMapper.selectBySkuTypeAndColorType(4, 0);
        System.out.println(productInfo);
    }

    @Test
    public void deleteBySkuTypeAndColorType(){
        productInfoMapper.deleteBySkuTypeAndColorType(5, 2);
        System.out.println("删除成功");
    }

    @Test
    public void updateBySkuTypeAndColorType(){
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductName("智能hhhh");
        //productInfo.setCode("P009");
        Integer count = productInfoMapper.updateBySkuTypeAndColorType(productInfo, 4,1);
        System.out.println(count);
    }
}
