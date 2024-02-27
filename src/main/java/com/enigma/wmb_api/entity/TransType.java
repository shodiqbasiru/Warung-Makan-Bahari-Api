package com.enigma.wmb_api.entity;

import com.enigma.wmb_api.constant.ConstantTable;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = ConstantTable.TABLE_TRANS_TYPE)
public class TransType {
    @Id
    private String id;
    @Column(name = "description",nullable = false)
    private String description;
}
