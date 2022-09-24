package com.cell.component.download.common.entity;

import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@SuppressWarnings("serial")
public class DownloadChainInfo extends Model<DownloadChainInfo>
{
    @TableId(type = IdType.AUTO)

    private Integer id;

    private String chainName;

    private Long blockNumber;
    /**
     * 0:eth,1:bsc ...
     */
    private Integer chainType;

    private Date createAt;

    private Date updateAt;
    /**
     * 状态,默认值为1代表启用
     */
    private Integer status;
    /**
     * chain别名,如bsctestnet
     */
    private String chainAlias;


    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal()
    {
        return this.id;
    }
}
