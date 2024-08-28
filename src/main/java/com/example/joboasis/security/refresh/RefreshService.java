package com.example.joboasis.security.refresh;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshService {

    private final RefreshRepository refreshRepository;

    @Transactional
    public void addRefresh(String loginId, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        Refresh refreshEntity = new Refresh(loginId, refresh, date.toString());

        refreshRepository.save(refreshEntity);
    }

    @Transactional
    public void deleteByRefresh(String refresh) {
        refreshRepository.deleteByRefresh(refresh);
    }

    @Transactional(readOnly = true)
    public boolean existsByRefresh(String refresh) {
        return refreshRepository.existsByRefresh(refresh);
    }
}
