package com.ll.exam.app__2022_10_11.app.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.exam.app__2022_10_11.app.base.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class Member extends BaseEntity {
    @Column(unique = true)
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private boolean emailVerified;

    // name 은 username 값으로 사용
    public String getName() {
        return username;
    }

    public Member(long id) {
        super(id);
    }
}
