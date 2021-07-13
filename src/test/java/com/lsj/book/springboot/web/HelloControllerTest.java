package com.lsj.book.springboot.web;

import com.lsj.book.springboot.config.auth.SecurityConfig;
import com.lsj.book.springboot.web.HelloController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)    //JUnit을 실행할 때 괄호 안의 실행자로 실행한다.
@WebMvcTest(controllers = HelloController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
    })    //web 어노테이션으로 @Controlle, @ControllerAdvice 사용가능
public class HelloControllerTest {

    @Autowired  //스피링이 관리하는 Bean을 주입받는다.
    private MockMvc mvc;    //웹 API를 테스트 할때 사용. HTTP GET, POST 테스트 할 수 있음

    @WithMockUser(roles="USER")
    @Test
    public void hello가_리턴된다() throws Exception {
        String hello = "hello";

        mvc.perform(get("/hello"))  //MockMvc를 통해 /hello 주소로 HTTP GET 요청을 한다.
            .andExpect(status().isOk())     //mvc.perform의 결과를 검증. 200(ok) 404 500 상태 검증
            .andExpect(content().string(hello));    //Controller에서 "hello"를 리턴하기 때문에 값이 맞는지 검증
    }

    @WithMockUser(roles="USER")
    @Test
    public void helloDto가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 1000;

        mvc.perform(
                get("/hello/dto")
                    .param("name", name)    //API 테스트 시 사용될 요청 파라미터를 설정
                    .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))    //JSON 응답값을 필드로 검증할 수 있는 메소드. $는 필드명
                .andExpect(jsonPath("$.amount", is(amount)));
    }
}

