package com.lsj.book.springboot.config.auth;

import com.lsj.book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity  /*Sping Security 설정 활성화 */
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .headers().frameOptions().disable() /*h2-console 화면을 사용하기 위한 옵션들을 disable함*/
            .and()
                .authorizeRequests()    /*URL 별 관리권한 설정*/
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
                .antMatchers("/api/v1/**").hasRole(Role.USER.name())    /*권한 관리 대상을 지정. URL, HTTP 메소드별로 관리가 가능*/
                .anyRequest().authenticated()   /*설정값 이외 나머지 URL을 인증된(로그인 된)사용자들에게만 허용*/
            .and()
                .logout()
                    .logoutSuccessUrl("/")     /*로그아웃 기능. 성공시 "/"로 이동*/
            .and()
                .oauth2Login()  /*OAuth2 로그인 기능에 대한 설정의 진입점*/
                    .userInfoEndpoint() /*OAuth2 로그인 성공 이후 사용자 정보를 가져올 떄의 설정들을 담당*/
                        .userService(customOAuth2UserService);  /*소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록.*/
    }
}
