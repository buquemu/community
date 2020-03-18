package buquemu.community.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class AliyunService {
    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.endpoint}")
    private String endPoint;
    @Value("${aliyun.bucketName}")
    private String bucketName;
    @Value("${aliyun.fileHost}")
    private String fileHost;
    private static String File_URL;

    public  String upLoad(File file){
        boolean isImage = true;
        try {
            Image image = ImageIO.read(file);
            isImage = image == null?false:true;
        }catch (Exception e){
            e.printStackTrace();
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(new Date());

        if(file == null){
            return null;
        }

        OSSClient ossClient = new OSSClient(endPoint,accessKeyId ,accessKeySecret );
        try {
            if(!ossClient.doesBucketExist(bucketName)){
                ossClient.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }
            //设置文件路径,这里再通过时间分成子文件夹
            String fileUrl = fileHost + "/" + (dateStr + "/" + UUID.randomUUID().toString().replace("-", "")+"-"+file.getName());
            //如果是图片
            if(isImage){
                File_URL = "https://" + bucketName + "." + endPoint + "/" + fileUrl;
            }else {
                File_URL = "非图片，不可预览。文件路径为：" + fileUrl;
            }
            //上传文件
            PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, file));
            //设置公开读权限
            ossClient.setBucketAcl(bucketName,CannedAccessControlList.PublicRead);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(ossClient != null){
                ossClient.shutdown();
            }
        }
        return File_URL;
    }
}

