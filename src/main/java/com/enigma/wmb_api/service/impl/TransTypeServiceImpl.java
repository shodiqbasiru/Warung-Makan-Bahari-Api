package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.TransTypeEnum;
import com.enigma.wmb_api.entity.TransType;
import com.enigma.wmb_api.repository.TransTypeRepository;
import com.enigma.wmb_api.service.TransTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransTypeServiceImpl implements TransTypeService {

    private final TransTypeRepository typeRepository;

    @Override
    public List<TransType> getAll() {
        return typeRepository.findAll();
    }

    @Override
    public TransType getById(String id) {
        TransTypeEnum typeEnum = TransTypeEnum.valueOf(id);
        Optional<TransType> transTypeId = typeRepository.findById(typeEnum);
        return transTypeId.orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND,"Trans type not found"));
    }

    @Override
    public TransType update(TransType transType) {
        getById(transType.getId().toString());
        return typeRepository.save(transType);
    }
}
