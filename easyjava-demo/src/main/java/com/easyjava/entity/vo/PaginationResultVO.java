package com.easyjava.entity.vo;

import java.util.ArrayList;
import java.util.List;

public class PaginationResultVO<T> {

    // 成员变量
    private Integer totalCount; // 总记录数
    private Integer pageSize;   // 每页条数
    private Integer pageNo;     // 当前页码
    private Integer pageTotal;  // 总页数
    private List<T> list = new ArrayList<T>(); // 当前页数据列表

    // 构造函数1：基础参数（不包含总页数）
    public PaginationResultVO(Integer totalCount, Integer pageSize, Integer pageNo, List<T> list) {
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.list = list;
    }

    // 构造函数2：全量参数（包含总页数，并对页码为0做了防御性处理）
    public PaginationResultVO(Integer totalCount, Integer pageSize, Integer pageNo, Integer pageTotal, List<T> list) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.pageTotal = pageTotal;
        this.list = list;
    }

    // 构造函数3：只传入数据列表
    public PaginationResultVO(List<T> list) {
        this.list = list;
    }

    // 构造函数4：无参构造函数
    public PaginationResultVO() {
    }

    // ================= Getter 和 Setter 方法 =================

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(Integer pageTotal) {
        this.pageTotal = pageTotal;
    }
}
