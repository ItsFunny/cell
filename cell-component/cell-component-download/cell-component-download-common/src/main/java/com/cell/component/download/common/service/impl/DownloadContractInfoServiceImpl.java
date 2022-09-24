package com.cell.component.download.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cell.component.download.common.dao.DownloadContractInfoDao;
import com.cell.component.download.common.entity.DownloadContractInfo;
import com.cell.component.download.common.service.DownloadContractInfoService;
import org.springframework.stereotype.Service;


@Service("downloadContractInfoService")
public class DownloadContractInfoServiceImpl extends ServiceImpl<DownloadContractInfoDao, DownloadContractInfo> implements DownloadContractInfoService
{

}

