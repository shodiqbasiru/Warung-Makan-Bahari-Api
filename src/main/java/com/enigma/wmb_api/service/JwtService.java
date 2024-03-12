package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.JwtClaims;
import com.enigma.wmb_api.entity.UserAccount;

public interface JwtService {
    String generateToken(UserAccount account);
    boolean verifyJwtToken(String token);
    JwtClaims getClaimsByToken(String token);
}
