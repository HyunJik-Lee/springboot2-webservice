package org.example.web;

import org.example.config.auth.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
// 테스트를 진행할 때 JUnit에 내장된 실행자 외에 다른 실행자 실행(스프링 부트 테스트와 JUnit 사이의 연결자)
@WebMvcTest(controllers = HelloController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
})
// 여러 스프링 테스트 어노테이션 중, Web(Spring MVC)에 집중할 수 있는 어노테이션
// 선언할 경우 @Controller @ControllerAdvice를 사용할 수 있다.
// @Service, @Component, @Repository는 사용할 수 없다.
public class HelloControllerTest {

    @Autowired
    // 스프링이 관리하는 빈을 주입 받는 부분
    private MockMvc mvc;
    // 웹 API를 테스트할 때 사용
    // 스프링 MVC 테스트의 시작점

    @Test
    @WithMockUser(roles = "USER")
    public void returns_string_hello() throws Exception{
        String hello = "hello";
        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void returns_helloDto() throws Exception{
        String name = "hello";
        int amount = 1000;
        mvc.perform(
                get("/hello/dto")
                    .param("name", name)
                    .param("amount", String.valueOf(amount))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));

    }
}
