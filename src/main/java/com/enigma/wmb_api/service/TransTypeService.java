package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.TransType;

import java.util.List;

public interface TransTypeService {
    List<TransType> getAll();
    TransType getById(String id);
    TransType update(TransType transType);
}
