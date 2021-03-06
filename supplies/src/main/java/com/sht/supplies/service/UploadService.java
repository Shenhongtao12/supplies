package com.sht.supplies.service;

import com.sht.supplies.common.BaseCommon;
import com.sht.supplies.common.RestResponse;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class UploadService extends BaseCommon {
    //private static final Logger logger = LoggerFactory.getLogger(UploadService.class);

    public RestResponse upload(MultipartFile[] fileArray, String site) {
        try {
            if (fileArray == null) {
                return ERROR("文件不能为空");
            }
            Map<String, Object> result = new HashMap<>();
            String name = "";
            //循环上传图片
            for (int i = 0; i < fileArray.length; i++) {
                MultipartFile file = fileArray[i];

                BufferedImage image = ImageIO.read(file.getInputStream());
                if (image == null) {
                    log.error("上传失败，文件内容不符合");
                    return ERROR("上传失败，文件内容不符合");
                }
                Long size = file.getSize();
                if (size >= 2 * 1024 * 1024) {
                    log.error("文件过大：" + size + " 字节");
                    return ERROR("文件大小不能超过2M");
                }
                //原图
                String fileName = file.getOriginalFilename();
                //截取图片后缀名
                String suffixName = fileName.substring(fileName.lastIndexOf("."));

                String str = ".jpg, .jpeg, .png, .gif, .JPG, .JPEG, .PNG, .GIF";
                if (!str.contains(suffixName)) {
                    log.error("格式错误: " + suffixName);
                    return ERROR("请上传文件格式 'jpg, jpeg, png, gif' 的图片");
                }

                UUID uuid = UUID.randomUUID();
                fileName = uuid + suffixName;

                File dir = new File(site);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                //file.transferTo(new File(dir, fileName));

                //缩略图
                String thumbnailName = uuid + "thumbnail" + suffixName;

                if (size < 200 * 1024) {
                    Thumbnails.of(file.getInputStream()).scale(1.0D).toFile(site + "/" + thumbnailName);
                } else if (size < 600 * 1024){
                    Thumbnails.of(file.getInputStream()).scale(1.0D).outputQuality(0.5f).toFile(site + "/" + thumbnailName);
                }else {
                    Thumbnails.of(file.getInputStream()).scale(1.0D).outputQuality(0.4f).toFile(site + "/" + thumbnailName);
                }
                if (i == 0 ){
                    name += thumbnailName;
                }else {
                    name += ","+thumbnailName;
                }
            }
            result.put("thumbnailName", name);
            return SUCCESS(result, "上传成功");
        } catch (Exception e) {
            return ERROR("上传失败，其他错误.");
        }
    }

    //删除图片,传入完整的url  https://eurasia.plus/eurasia/buy/1f33f675-edd6-4277-8daf-4c4f6f3470cethumbnail.png
    public String deleteImage(String url) {
        String resultInfo;

        String path = url.substring(26); //截掉http://182.92.218.236:8888

        String name2 = path.substring(0, path.indexOf("thumbnail"));
        String jpg = url.substring(url.lastIndexOf("."));
        String path2 = name2 + jpg;
        File file = new File(path);
        File file2 = new File(path2);
        if (file.exists()) {
            if (file.delete() && file2.delete()) {
                resultInfo = "删除成功";
            } else {
                resultInfo = "删除失败";
                log.error("图片删除失败：" + path2);
            }
        } else {
            resultInfo = "文件不存在";
            log.error("图片不存在："  + path);
        }
        return resultInfo;
    }
}
