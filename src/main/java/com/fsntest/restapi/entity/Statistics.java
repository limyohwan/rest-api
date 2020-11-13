package com.fsntest.restapi.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@IdClass(CompositeKey.class)
@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "statistics")
public class Statistics {
    @Id
    private String statisticsDate;
    @Id
    private Integer statisticsTime;
    @Column
    private int requestCnt;
    @Column
    private int responseCnt;
    @Column
    private int clickCnt;
}

class CompositeKey implements Serializable{
    private String statisticsDate;
    private Integer statisticsTime;
}
