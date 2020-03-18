package buquemu.community.controller;

import buquemu.community.dto.PictureDTO;
import buquemu.community.service.AliyunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Controller
public class PictureController {
    @Autowired
    AliyunService aliyunService;
    @PostMapping("/picture/upload")
    @ResponseBody
    public PictureDTO upload(@RequestParam("editormd-image-file") MultipartFile file, Model model){
        PictureDTO responseResult = new PictureDTO();
        String filename = file.getOriginalFilename();
//        System.out.println(filename);
        try {
            if(file != null){
                if(!"".equals(filename.trim())){
                    File newFile = new File(filename);
                    FileOutputStream outputStream = new FileOutputStream(newFile);
                    outputStream.write(file.getBytes());
                    outputStream.close();
                    file.transferTo(newFile);
                    String url = aliyunService.upLoad(newFile);
                    responseResult.setSuccess(1);
                    responseResult.setUrl(url);
                    responseResult.setMessage("上传成功");
                }
            }
        } catch (FileNotFoundException e) {
            responseResult.setSuccess(0);
            responseResult.setMessage("上传失败");
            e.printStackTrace();
        } catch (IOException e) {
            responseResult.setSuccess(0);
            responseResult.setMessage("上传失败");
            e.printStackTrace();
        }
        return responseResult;
    }

}
