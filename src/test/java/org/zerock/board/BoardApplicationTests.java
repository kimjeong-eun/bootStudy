package org.zerock.board;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
class BoardApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("sout콘솔 테스트");
        log.info("롬복으로 로그찍기 테스트");
    }
}
