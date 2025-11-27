package com.foodie.foodieapp.service;



import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        String orginalFileName=file.getOriginalFilename();

        String randomId= UUID.randomUUID().toString();
        String fileName=randomId.concat(orginalFileName.substring(orginalFileName.lastIndexOf('.')));
        String filePath=path+ File.separator+fileName;


        File folder=new File(path);
        if(!folder.exists()){
            folder.mkdirs();
        }
        Files.copy(file.getInputStream(), Paths.get(filePath));

    

        return fileName;
    }

    @Override
    public void deleteImage(String path, String image) {
        String filePath=path+ File.separator+image;
    
        File file=new File(filePath);
        if(file.exists()){
            file.delete();
        }
    }
}