package com.recall.cashcard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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

    @PostMapping
    private ResponseEntity<Void> createCashCard(@RequestBody CashCard newCard, UriComponentsBuilder ucb) {
        CashCard savedCard = cardRepository.save(newCard);

        URI locationOfNewCard = ucb
                .path("cashcards/{id}")
                .buildAndExpand(savedCard.id())
                .toUri();
        return ResponseEntity.created(locationOfNewCard).build();
    }

    @GetMapping
    private ResponseEntity<Iterable<CashCard>> findAll(Pageable pageable) {
        Page<CashCard> page = cardRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize()
                )
        );
        return ResponseEntity.ok(page.getContent());
    }

}
