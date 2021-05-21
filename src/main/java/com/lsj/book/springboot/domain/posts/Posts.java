package com.lsj.book.springboot.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor  //기본 생성자 자동 추가
@Entity //테이블과 링크될 클래스임을 나타냄. 파스칼 표기법을 스네이크 표기법으로 매칭
public class Posts {

    @Id //해당 테이블의 PK 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY) //PK 생성 규칙. 스프링 부트 2.0버전에선 GenerationType.IDENTITY 옵션을 추가 해야만 auto_increment가 됨
    private Long id;

    @Column(length = 500, nullable = false) //테이블의 칼럼(안써도 칼럼은 되지만 기본 설정에서 변경을 원할 시 사용한다.
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;  //@Column 어노테이션을 사용하지 않았기 때문에 VARCHAR(255) 기본설정을 사용

    @Builder    //해당 클래스의 빌더 패턴 클래스를 생성
    public Posts(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
