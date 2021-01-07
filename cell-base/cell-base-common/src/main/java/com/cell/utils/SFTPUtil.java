//package com.cell.utils;
//
//import com.jcraft.jsch.*;
//import lombok.Data;
//import org.apache.commons.io.IOUtils;
//
//import java.io.*;
//import java.util.Properties;
//import java.util.Vector;
//
///**
// * @author Charlie
// * @When
// * @Description
// * @Detail
// * @Attention:
// * @Date 创建时间：2020-03-09 09:25
// */
//public class SFTPUtil
//{
//
//    private SFTPWrapper sftpWrapper;
////    private ChannelSftp sftp;
////
////    private Session session;
//    /**
//     * SFTP 登录用户名
//     */
//    private String username;
//    /**
//     * SFTP 登录密码
//     */
//    private String password;
//    /**
//     * 私钥
//     */
//    private String privateKey;
//    /**
//     * SFTP 服务器地址IP地址
//     */
//    private String host;
//    /**
//     * SFTP 端口
//     */
//    private int port;
//
//    @Data
//    public static class SFTPWrapper
//    {
//        private ChannelSftp sftp;
//        private Session session;
//
//        public void logOut()
//        {
//            if (this.sftp != null && this.sftp.isConnected())
//            {
//                this.sftp.disconnect();
//            }
//            if (this.session != null && this.session.isConnected())
//            {
//                this.session.disconnect();
//            }
//        }
//    }
//
//    /**
//     * 构造基于密码认证的sftp对象
//     */
//    public SFTPUtil(String username, String password, String host, int port)
//    {
//        this.username = username;
//        this.password = password;
//        this.host = host;
//        this.port = port;
//    }
//
//    public void prepare(SFTPWrapper sftpWrapper)
//    {
//        this.sftpWrapper = sftpWrapper;
//    }
//
//    /**
//     * 构造基于秘钥认证的sftp对象
//     */
//    public SFTPUtil(String username, String host, int port, String privateKey)
//    {
//        this.username = username;
//        this.host = host;
//        this.port = port;
//        this.privateKey = privateKey;
//    }
//
//    public SFTPUtil() {}
//
//
//    /**
//     * 连接sftp服务器
//     */
//    @Deprecated // use prepare
//    public void login() throws JSchException
//    {
//        try
//        {
//            // 获取
//            this.sftpWrapper = this.accquireSftp();
//        } catch (JSchException e)
//        {
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//    private SFTPWrapper accquireSftp() throws JSchException
//    {
//        SFTPWrapper sftpWrapper = new SFTPWrapper();
//        JSch jsch = new JSch();
//        if (privateKey != null)
//        {
//            jsch.addIdentity(privateKey);// 设置私钥
//        }
//        sftpWrapper.session = jsch.getSession(username, host, port);
//
//        if (password != null)
//        {
//            sftpWrapper.session.setPassword(password);
//        }
//        Properties config = new Properties();
//        config.put("StrictHostKeyChecking", "no");
//
//        sftpWrapper.session.setConfig(config);
//        sftpWrapper.session.connect();
//
//        Channel channel = sftpWrapper.session.openChannel("sftp");
//        channel.connect();
//
//        sftpWrapper.sftp = (ChannelSftp) channel;
//        return sftpWrapper;
//    }
//
//    /**
//     * 关闭连接 server
//     */
//    public void logout()
//    {
//        this.sftpWrapper.logOut();
//    }
//
//
//    /**
//     * 将输入流的数据上传到sftp作为文件。文件完整路径=basePath+directory
//     *
//     * @param basePath     服务器的基础路径
//     * @param directory    上传到该目录
//     * @param sftpFileName sftp端文件名
//     */
//    public void upload(String basePath, String directory, String sftpFileName, InputStream input) throws SftpException
//    {
//        try
//        {
//            sftpWrapper.sftp.cd(basePath);
//            sftpWrapper.sftp.cd(directory);
//        } catch (SftpException e)
//        {
//            //目录不存在，则创建文件夹
//            String[] dirs = directory.split("/");
//            String tempPath = basePath;
//            for (String dir : dirs)
//            {
//                if (null == dir || "".equals(dir)) continue;
//                tempPath += "/" + dir;
//                try
//                {
//                    sftpWrapper.sftp.cd(tempPath);
//                } catch (SftpException ex)
//                {
//                    sftpWrapper.sftp.mkdir(tempPath);
//                    sftpWrapper.sftp.cd(tempPath);
//                }
//            }
//        }
//        sftpWrapper.sftp.put(input, sftpFileName);  //上传文件
//    }
//
//
//    /**
//     * 下载文件。
//     *
//     * @param directory    下载目录
//     * @param downloadFile 下载的文件
//     * @param saveFile     存在本地的路径
//     */
//    public void download(String directory, String downloadFile, String saveFile) throws SftpException, FileNotFoundException
//    {
//        if (directory != null && !"".equals(directory))
//        {
//            sftpWrapper.sftp.cd(directory);
//        }
//        File file = new File(saveFile);
//        sftpWrapper.sftp.get(downloadFile, new FileOutputStream(file));
//    }
//
//    /**
//     * 下载文件
//     *
//     * @param directory    下载目录
//     * @param downloadFile 下载的文件名
//     * @return 字节数组
//     */
//    public byte[] download(String directory, String downloadFile) throws SftpException, IOException
//    {
//        if (directory != null && !"".equals(directory))
//        {
//            sftpWrapper.sftp.cd(directory);
//        }
//        InputStream is = sftpWrapper.sftp.get(downloadFile);
//
//        byte[] fileData = IOUtils.toByteArray(is);
//
//        return fileData;
//    }
//
//
//    /**
//     * 删除文件
//     *
//     * @param directory  要删除文件所在目录
//     * @param deleteFile 要删除的文件
//     */
//    public void delete(String directory, String deleteFile) throws SftpException
//    {
//        sftpWrapper.sftp.cd(directory);
//        sftpWrapper.sftp.rm(deleteFile);
//    }
//
//
//    /**
//     * 列出目录下的文件
//     *
//     * @param directory 要列出的目录
//     */
//    public Vector<?> listFiles(String directory) throws SftpException
//    {
//        return sftpWrapper.sftp.ls(directory);
//    }
//
//    //上传文件测试
//    public static void main(String[] args) throws SftpException, IOException, JSchException
//    {
//        SFTPUtil sftp = new SFTPUtil("vlink", "gh123456!", "39.100.60.58", 22);
//        sftp.login();
//        String filePath = "/Users/joker/Desktop/a.jpg";
//        File file = new File(filePath);
//        InputStream is = new FileInputStream(file);
//
//        sftp.upload("/home/vlink", "files", "test_sftp.jpg", is);
//        sftp.logout();
//    }
//}
