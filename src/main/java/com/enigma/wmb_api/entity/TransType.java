package com.enigma.wmb_api.entity;

import com.enigma.wmb_api.constant.ConstantTable;
import com.enigma.wmb_api.constant.TransTypeEnum;
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
    @Enumerated(EnumType.STRING)
    private TransTypeEnum id;
    @Column(name = "description", nullable = false)
    private String description;
}
