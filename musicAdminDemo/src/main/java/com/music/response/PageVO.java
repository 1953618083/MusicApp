package com.music.response;

import lombok.Data;
import java.util.List;

/**
 * 分页数据封装类
 * @param <T> 数据类型
 */
@Data
public class PageVO<T> {
    // 当前页数据列表
    private List<T> list;

    // 总记录数
    private Long total;

    // 当前页码
    private Integer pageNum;

    // 每页数量
    private Integer pageSize;

    /**
     * 快速构建分页对象
     * @param list 当前页数据
     * @param total 总记录数
     * @param pageNum 页码
     * @param pageSize 每页数量
     */
    public static <T> PageVO<T> of(List<T> list, Long total, Integer pageNum, Integer pageSize) {
        PageVO<T> page = new PageVO<>();
        page.setList(list);
        page.setTotal(total);
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        return page;
    }
}
