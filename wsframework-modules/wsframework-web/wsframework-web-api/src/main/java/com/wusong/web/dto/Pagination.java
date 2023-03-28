package com.wusong.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * javadoc Pagination
 * <p>
 *     分页数据容器
 * <p>
 * @author weng xiaoyong
 * @date 2022/4/21 11:11
 * @version 1.0.0
 **/
@SuppressWarnings(value = "unused")
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Pagination<T> implements Serializable {

    /**
     * 分页的页索引
     **/
    private Integer page;

    /**
     * 页元素大小
     **/
    private Integer size;

    /**
     * 总页数
     **/
    private Long pages;

    /**
     * 总元素树
     **/
    private Long total;

    /**
     * 页面元素内容
     **/
    private List<T> list;

    public Pagination() {
    }

    public Pagination(Integer page, Integer size, Long pages, Long total, List<T> list) {
        this.page = page;
        this.size = size;
        this.pages = pages;
        this.total = total;
        this.list = list;
    }

    /**
     * javadoc of
     * @apiNote 会自动计算页数
     *
     * @param page 页索引
     * @param size 页大小
     * @param total 元素总大小
     * @param list 元素列表
     * @return com.wusong.web.dto.Pagination<T>
     * @author weng xiaoyong
     * @date 2022/4/21 11:26
     **/
    public static <T> Pagination<T> of(int page, int size, long total, List<T> list){
        long pages = total / size;
        long remainder = total % size;
        if (remainder > 0) {
            pages++;
        }
        return new Pagination<>(page, size, pages, total, list);
    }

    /**
     * javadoc from
     * @apiNote 从一个已有的分页结果数据构造一个新的分页数据
     *
     * @param pagination 已有的分页数据
     * @param mapper 数据元素转换器
     * @return com.wusong.web.dto.Pagination<R>
     * @author weng xiaoyong
     * @date 2022/4/21 11:29
     **/
    public static <T, R> Pagination<R> from(Pagination<T> pagination, Function<T, R> mapper){
        final List<R> list;
        if(Objects.nonNull(pagination.getList())){
            list = pagination.getList().stream().map(mapper).collect(Collectors.toList());
        }else{
            list = new ArrayList<>();
        }
        return new Pagination<>(pagination.getPage(), pagination.getSize(), pagination.getPages(), pagination.getTotal(), list);
    }

    /**
     * javadoc of
     * @apiNote 会自动计算页数
     *
     * @param page 页索引
     * @param size 页大小
     * @param pages 总页数
     * @param total 元素总大小
     * @param list 元素列表
     * @return com.wusong.web.dto.Pagination<T>
     * @author weng xiaoyong
     * @date 2022/4/21 11:26
     **/
    public static <T> Pagination<T> of(int page, int size, long pages, long total, List<T> list){
        return new Pagination<>(page, size, pages, total, list);
    }

    /**
     * javadoc empty
     * @apiNote 构造空的分页元素容器, 默认页数是1, 其它数量全是0
     *
     * @return com.wusong.web.dto.Pagination<T>
     * @author weng xiaoyong
     * @date 2022/4/21 11:27
     **/
    public static <T> Pagination<T> empty(){
        return of(1, 0, 0, 0, new ArrayList<>());
    }

    /**
     * javadoc verifySize
     * @apiNote 检测分页的每页元素数量
     *          如果参数不合法( null / <= 0) 则返回默认值10
     *          如果参数合法, 则直接返回参数
     *
     * @param nullableSize 分页的每页元素数量
     * @return int
     * @author weng xiaoyong
     * @date 2022/4/21 11:27
     **/
    public static int verifySize(Integer nullableSize){
        if(Objects.isNull(nullableSize)){
            return 10;
        }
        if(nullableSize <= 0){
            return 10;
        }
        return nullableSize;
    }

    /**
     * javadoc verifyPage
     * @apiNote 检测分页的页元素索引
     *          如果参数不合法(null / <= 0), 则返回 第一页 1
     *          如果参数合法, 则直接返回参数
     *
     * @param nullablePage 分页的页元素索引
     * @return int
     * @author weng xiaoyong
     * @date 2022/4/21 11:28
     **/
    public static int verifyPage(Integer nullablePage){
        if(Objects.isNull(nullablePage)){
            return 1;
        }
        if(nullablePage <= 0){
            return 1;
        }
        return nullablePage;
    }
}
