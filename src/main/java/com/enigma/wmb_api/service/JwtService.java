package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.JwtClaims;

public interface JwtService {
    String generateToken();
    boolean verify(String token);
    JwtClaims getClaimsByToken(String token);
}
