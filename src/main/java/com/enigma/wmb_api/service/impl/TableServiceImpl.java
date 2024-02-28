package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.TableRequest;
import com.enigma.wmb_api.entity.MTable;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.repository.TableRepository;
import com.enigma.wmb_api.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {
    private final TableRepository tableRepository;
    @Override
    public MTable create(TableRequest request) {
        MTable newTable = MTable.builder()
                .tableName(request.getTableName())
                .build();
        return tableRepository.saveAndFlush(newTable);
    }

    @Override
    public MTable getById(String id) {
        return findBydIdThrowNotFound(id);
    }

    @Override
    public List<MTable> getAll() {
        return tableRepository.findAll();
    }

    @Override
    public MTable update(MTable request) {
        findBydIdThrowNotFound(request.getId());
        return tableRepository.saveAndFlush(request);
    }

    @Override
    public void delete(String id) {
        MTable currentTable = findBydIdThrowNotFound(id);
        tableRepository.delete(currentTable);
    }

    private MTable findBydIdThrowNotFound(String id) {
        return tableRepository.findById(id).orElseThrow(() -> new RuntimeException("Table Not Found"));
    }
}
