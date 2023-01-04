package com.cell.extension.mybatis.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cell.base.common.consumers.IConsumer;
import com.cell.base.common.enums.ErrorEnums;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class PageRespDTOV3<F, T> {
    private List<T> items;
    private Integer totalCount;
    private Integer pages;
    private Integer size;

    public void fromPage(IPage<F> page, IConsumer<T, ErrorEnums> consumer) {
        this.totalCount = Math.toIntExact(page.getTotal());
        this.items = this.conv(page.getRecords(), consumer);
        this.pages = Math.toIntExact(page.getPages());
        this.size = Math.toIntExact(page.getSize());
    }

    public void empty() {
        this.items = new ArrayList<>();
        this.totalCount = 0;
        this.pages = 0;
        this.size = 0;
    }

    public void setBasePage(IPage<F> page) {
        this.totalCount = Math.toIntExact(page.getTotal());
        this.pages = Math.toIntExact(page.getPages());
        this.size = Math.toIntExact(page.getSize());
    }

    protected abstract T conv(F f);

    protected List<T> conv(List<F> f, IConsumer<T, ErrorEnums> consumer) {
        List<T> ret = new ArrayList<>();
        for (F f1 : f) {
            T t = this.conv(f1);
            if (null != consumer) {
                ErrorEnums consume = consumer.consume(t);
                if (consume == ErrorEnums.BREAK) {
                    break;
                }
            }
            ret.add(t);
        }

        return ret;
    }
}
