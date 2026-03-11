package com.neusoft.smartcommunity.common.api;

import java.util.Collections;
import java.util.List;

/**
 * 分页结果封装，用于列表接口统一返回。
 *
 * @param <T> 列表元素类型
 */
public class PageResult<T> {

    /** 总记录数 */
    private long total;

    /** 当前页数据列表 */
    private List<T> list;

    public PageResult() {
        this.total = 0;
        this.list = Collections.emptyList();
    }

    public PageResult(long total, List<T> list) {
        this.total = total;
        this.list = list != null ? list : Collections.emptyList();
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
