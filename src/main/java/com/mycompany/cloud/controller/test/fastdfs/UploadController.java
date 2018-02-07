package com.mycompany.cloud.controller.test.fastdfs;

import com.mycompany.cloud.controller.BaseController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/fastdfs/upload")
public class UploadController extends BaseController {
    public  Log logger = LogFactory.getLog(FastDFSClient.class);

    @PostMapping("/singleFileUpload")
    public Map<String, Object> singleFileUpload(@RequestParam("file") MultipartFile file,  RedirectAttributes redirectAttributes) {

        Map<String, Object> result = new HashMap<>();
        try {
            if (file.isEmpty()) {
                result.put("result", false);
            }else {
                String path = saveFile(file);
                result.put("result", true);
                result.put("path", path);
            }
        } catch (Exception e) {
            result.put("result", false);
            result.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return  result;

    }

    public String saveFile(MultipartFile multipartFile) throws Exception {
        String[] fileAbsolutePath={};
        String fileName=multipartFile.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        byte[] file_buff = null;
        InputStream inputStream=multipartFile.getInputStream();
        if(inputStream!=null){
            int len1 = inputStream.available();
            file_buff = new byte[len1];
            inputStream.read(file_buff);
        }
        inputStream.close();
        FastDFSFile file = new FastDFSFile(fileName, file_buff, ext);
        fileAbsolutePath = FastDFSClient.upload(file);
        String path=FastDFSClient.getTrackerUrl()+fileAbsolutePath[0]+ "/"+fileAbsolutePath[1];
        return path;
    }

    public static void main(String[] args) throws Exception{
        InputStream in = new FileInputStream("/Users/baoya/Documents/WechatIMG8.jpg");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 *4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }

        FastDFSFile file = new FastDFSFile("test.jpg", out.toByteArray(), "jpg");
        in.close();
        String[] fileAbsolutePath = FastDFSClient.upload(file);
        String path=FastDFSClient.getTrackerUrl()+fileAbsolutePath[0]+ "/"+fileAbsolutePath[1];
        System.out.println(path);
    }

}
