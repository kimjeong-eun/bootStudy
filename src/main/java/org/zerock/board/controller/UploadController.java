package org.zerock.board.controller;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.board.dto.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {

    @Value("${org.zerock.upload.path}") //application.properties의 변수
    private String uploadPath;


    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles){

        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        for(MultipartFile uploadFile : uploadFiles){

            //이미지 파일만 업로드 가능
            if(uploadFile.getContentType().startsWith("image") == false){
                log.warn("this file is not image type");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            //실제 파일 이름 IE나 Edge는 전체 경로가 들어오므로
            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\")+1);

            log.info("fileName: "+fileName);

            //날짜 폴더 생성
            String folderPath = makeFolder();

            //UUID
            String uuid = UUID.randomUUID().toString();

            //저장할 파일 이름 중간에 "_"를 이용해서 구분
            String saveName = uploadPath + File.separator
                    + folderPath + File.separator + uuid
                    + "_" + fileName;

            Path savePath = Paths.get(saveName);

            log.info("============savePath===========");
            log.info(savePath);
            log.info("=================================");

            try {
                //원본파일 저장
                uploadFile.transferTo(savePath);

                //섬네일 생성
                String thumnailSaveName = uploadPath + File.separator + folderPath + File.separator
                        +"s_"+uuid + "_" + fileName;

                //섬네일 파일 이름은 중간에 s_로 시작하도록
                File thumnailFile = new File(thumnailSaveName);

                //섬네일 생성                  //경로로 파일객체로 변환 (썸네일 만들 원본파일)
                Thumbnailator.createThumbnail(savePath.toFile(),thumnailFile,100,100);

                resultDTOList.add(new UploadResultDTO(fileName,uuid,folderPath));

            } catch (IOException e) {

                e.printStackTrace();
            }
        }//end for
        return  new ResponseEntity<>(resultDTOList,HttpStatus.OK);
    }

    private String makeFolder() {

        String str = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("/", File.separator);

        //male folder url 참조
        File uploadPathFolder = new File(uploadPath,folderPath);

        if(uploadPathFolder.exists()==false){
            //업로드 패스 폴더가 존재하지 않는다면 만들어라
            uploadPathFolder.mkdirs();
        }
        return  folderPath;
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName ,String size){

        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName =URLDecoder.decode(fileName,"UTF-8");
            log.info("fileName:"+srcFileName);

            File file = new File(uploadPath+File.separator+srcFileName);

            if(size != null && size.equals("1")){
                file = new File(file.getParent(),file.getName().substring(2));
            }

            log.info("file :" +file);
            //헤더 생성
            HttpHeaders header = new HttpHeaders();

            //MIME 타입 처리                 //probeContentType: 파라미터로 전달받은 파일의 타입을 문자열로 변환해 주는 메서드.
                                             // file의 Path (파일경로)를 받아 해당 파일의 타입을 확인 후 문자열로 변환
            header.add("Content-Type", Files.probeContentType(file.toPath()));
            //파일 데이터 처리
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),header,HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  result;
    }

    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName){
        //파일삭제시 섬네일 파일까지 같이 삭제

        String srcFileName= null;

        try {
            srcFileName = URLDecoder.decode(fileName , "UTF-8");
            File file = new File(uploadPath + File.separator + srcFileName);
            boolean result = file.delete();

            File thumbnail = new File(file.getParent(), "s_" + file.getName());

            result = thumbnail.delete();

            return new ResponseEntity<>(result , HttpStatus.OK);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
