package com.recall.cashcard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/cashcards")
public class CashCardRestController {

    CashCardRepository cardRepository;

    public CashCardRestController(CashCardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @GetMapping("/{requestedID}")
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedID) {
        Optional<CashCard> optCard = cardRepository.findById(requestedID);
        if (optCard.isPresent()) {
            return ResponseEntity.ok(optCard.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
