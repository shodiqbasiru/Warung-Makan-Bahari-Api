package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.TableRequest;
import com.enigma.wmb_api.entity.MTable;
import com.enigma.wmb_api.repository.TableRepository;
import com.enigma.wmb_api.service.TableService;
import com.enigma.wmb_api.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {
    private final TableRepository tableRepository;
    private final ValidationUtil validation;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MTable create(TableRequest request) {
        validation.validate(request);

        MTable newTable = MTable.builder()
                .tableName(request.getTableName())
                .build();
        return tableRepository.saveAndFlush(newTable);
    }

    @Transactional(readOnly = true)
    @Override
    public MTable getById(String id) {
        return findBydIdThrowNotFound(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MTable> getAll() {
        return tableRepository.findAll();
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public MTable update(MTable request) {
        validation.validate(request);

        findBydIdThrowNotFound(request.getId());
        return tableRepository.saveAndFlush(request);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        MTable currentTable = findBydIdThrowNotFound(id);
        tableRepository.delete(currentTable);
    }

    private MTable findBydIdThrowNotFound(String id) {
        return tableRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Table Not Found"));
    }
}
