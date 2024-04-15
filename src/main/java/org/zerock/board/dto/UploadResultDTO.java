package org.zerock.board.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@Data
@AllArgsConstructor
@Log4j2
public class UploadResultDTO implements Serializable {
//서버가 다중화(여러개 존재) 되어 있고 세션 클러스터링을 통해 세션 관리를 하는 환경에서 도메인 객체가 세션에 저장될 때, 도메인 객체가 세션에 저장될 때
//도메인 객체에 Serializable 인터페이스 클래스를 구현해야지 정상적으로 세션을 저장하고 꺼내올 수 있다!

    private String fileName;
    private String uuid;
    private String folderPath;

    public String getImageURL(){
        //전체 경로가 필요한 경우를 대비해서

        try {
            //URLEncoder.encode -> 한글넣으면 깨지기때무네 씀
            return URLEncoder.encode(folderPath+"/"+uuid+"_"+fileName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
        }
            return "";
    }

    public String getThumbnailURL(){

        try {
            return URLEncoder.encode(folderPath +"/s_"+uuid+"_"+fileName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }

}
