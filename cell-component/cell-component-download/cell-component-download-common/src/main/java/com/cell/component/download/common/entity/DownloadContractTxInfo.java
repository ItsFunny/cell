package com.cell.component.download.common.entity;

import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@SuppressWarnings("serial")
public class DownloadContractTxInfo extends Model<DownloadContractTxInfo>
{
    @TableId(type = IdType.INPUT)

    private Long id;
    /**
     * 合约地址
     */
    private String contract;

    private Integer chainType;

    private Long blockNumber;
    /**
     * tx数据信息
     */
    private String txData;

    private String txHash;
    /**
     * 压缩算法,0默认不压缩
     */
    private Integer compressType;

    private Date createAt;


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

