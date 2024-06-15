package com.recall.cashcard;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CashCardRepository extends CrudRepository<CashCard, Long> {

    @Override
    Optional<CashCard> findById(Long aLong);
}
