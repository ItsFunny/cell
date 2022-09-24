package com.cell.component.download.common.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cell.component.download.common.dao.DownloadChainInfoDao;
import com.cell.component.download.common.entity.DownloadChainInfo;
import com.cell.component.download.common.service.DownloadChainInfoService;
import org.springframework.stereotype.Service;


@Service("downloadChainInfoService")
public class DownloadChainInfoServiceImpl extends ServiceImpl<DownloadChainInfoDao, DownloadChainInfo> implements DownloadChainInfoService
{

}

