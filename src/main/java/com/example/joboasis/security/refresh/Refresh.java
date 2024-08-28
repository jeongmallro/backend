package com.example.joboasis.security.refresh;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Refresh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loginId;
    private String refresh;
    private String expireDate;

    public Refresh(String loginId, String refresh, String expireDate) {
        this.loginId = loginId;
        this.refresh = refresh;
        this.expireDate = expireDate;
    }
}
