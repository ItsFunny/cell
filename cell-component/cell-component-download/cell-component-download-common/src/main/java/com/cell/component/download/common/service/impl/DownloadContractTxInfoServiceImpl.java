package com.cell.component.download.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cell.component.download.common.dao.DownloadContractTxInfoDao;
import com.cell.component.download.common.entity.DownloadContractTxInfo;
import com.cell.component.download.common.service.DownloadContractTxInfoService;
import org.springframework.stereotype.Service;


@Service("downloadContractTxInfoService")
public class DownloadContractTxInfoServiceImpl extends ServiceImpl<DownloadContractTxInfoDao, DownloadContractTxInfo> implements DownloadContractTxInfoService
{

}

