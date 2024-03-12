package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.BillDetail;

import java.util.List;

public interface BillDetailService {
    List<BillDetail> createBulk(List<BillDetail> billDetails);
}
