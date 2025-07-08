package com.sura.travel.repository;

import com.sura.travel.domain.TravelExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelExpenseRepository extends JpaRepository<TravelExpense, Long> {
    List<TravelExpense> findAllByOrderByEmployee_NameAsc();
}
