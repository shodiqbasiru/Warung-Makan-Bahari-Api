package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.TableRequest;
import com.enigma.wmb_api.entity.MTable;

import java.util.List;

public interface TableService {
    MTable create(TableRequest request);
    MTable getById(String id);
    List<MTable> getAll();
    MTable update(MTable request);
    void delete(String id);
}
