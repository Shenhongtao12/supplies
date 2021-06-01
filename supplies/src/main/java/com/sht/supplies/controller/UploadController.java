package com.sht.supplies.controller;

import com.sht.supplies.common.RestResponse;
import com.sht.supplies.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Administrator
 */
@RestController
@RequestMapping({"api/upload"})
@Api(tags = "图片服务")
public class UploadController extends BaseController {
    @Autowired
    private UploadService uploadService;

    @PostMapping({"image"})
    @ApiOperation(value = "上传图片", notes = "上传图片,图片路径为/supplies/images")
    public ResponseEntity<RestResponse> uploadImage(@RequestParam("file") MultipartFile[] file, @RequestParam(name = "site", defaultValue = "/supplies/images") String site) {
        RestResponse url = this.uploadService.upload(file, site);
        if (url.getCode() != 200) {
            return ResponseEntity.badRequest().body(ERROR("上传失败"));
        }

        return ResponseEntity.ok(url);
    }

    //@DeleteMapping({"deleteImage"})
    //@ApiOperation(value = "删除单张图片", notes = "删除图片,传入图片的完整url")
    public ResponseEntity<RestResponse> delFile(@RequestParam(name = "url") String url) {
        String msg = uploadService.deleteImage(url);
        if ("删除成功".equals(msg)) {
            return ResponseEntity.status(HttpStatus.OK).body(SUCCESS(msg));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ERROR(msg));
    }
}
