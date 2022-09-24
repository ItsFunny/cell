package com.cell.component.download.common.entity;

import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@SuppressWarnings("serial")
public class DownloadBlockInfo extends Model<DownloadBlockInfo>
{
    @TableId(type = IdType.INPUT)

    private Long id;

    private Integer chainId;

    private Integer blockNumber;

    private String blockData;

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

