package com.cell.component.download.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cell.component.download.common.dao.DownloadBlockInfoDao;
import com.cell.component.download.common.entity.DownloadBlockInfo;
import com.cell.component.download.common.service.DownloadBlockInfoService;
import org.springframework.stereotype.Service;


@Service("downloadBlockInfoService")
public class DownloadBlockInfoServiceImpl extends ServiceImpl<DownloadBlockInfoDao, DownloadBlockInfo> implements DownloadBlockInfoService
{

}

