package com.cell.component.download.common.entity;

import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@SuppressWarnings("serial")
public class DownloadContractInfo extends Model<DownloadContractInfo>
{
    @TableId(type = IdType.INPUT)

    private Long id;
    /**
     * 合约地址
     */
    private String contract;
    /**
     * chain_info表的id,这个合约属于哪个链
     */
    private Long chainId;
    /**
     * 0 代表的是默认方式:定时扫描区块
     */
    private Integer strategy;

    private Long blockNumber;

    private Date createAt;

    private Date updateAt;


    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal()
    {
        return this.id;
    }
}

