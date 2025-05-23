package com.sky.controller.admin;


import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping ("/admin/common")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;;
    @PostMapping("/upload")
    public Result upload(MultipartFile file){
        //拿到原始文件名
        String originalFilename = file.getOriginalFilename();
        log.info("上传文件：{}",originalFilename);
        //获取文件名后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String url = null;
        try {
            //1.调用AliOssUtil工具类的upload文件上传方法
            String objectName = UUID.randomUUID().toString()+suffix;
            url = aliOssUtil.upload(file.getBytes(),objectName);
        } catch (IOException e) {
            log.info("文件上传失败：{}",e.getMessage());
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }

        //返回图片路径结果
        return Result.success(url);

    }
}
