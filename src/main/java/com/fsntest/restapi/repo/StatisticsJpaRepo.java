package com.fsntest.restapi.repo;

import com.fsntest.restapi.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticsJpaRepo extends JpaRepository<Statistics, Long> {

    List<Statistics> findByStatisticsDate(String statisticsDate);

    Statistics findByStatisticsDateAndStatisticsTime(String statisticsDate, Integer statisticsTime);
}
