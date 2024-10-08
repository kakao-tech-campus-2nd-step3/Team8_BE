package com.example.sinitto.common.resolver;

public interface MemberIdProvider {
    Long getMemberIdByToken(String token);
}
