package com.recall.cashcard.rest;

import com.recall.cashcard.model.CashCard;
import com.recall.cashcard.repository.CashCardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("/cashcards")
public class CashCardRestController {

    CashCardRepository cardRepository;

    public CashCardRestController(CashCardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @GetMapping("/{requestedID}")
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedID, Principal principal) {
        CashCard card = cardRepository.findByIdAndOwnerAndActiveIsTrue(requestedID, principal.getName());
        if (card != null) {
            return ResponseEntity.ok(card);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    private ResponseEntity<Void> createCashCard(
            @RequestBody CashCard newCard,
            UriComponentsBuilder ucb, Principal principal
    ) {
        CashCard savedCard = cardRepository.save(new CashCard(
                null,
                newCard.amount(),
                principal.getName(),
                true)
        );

        URI locationOfNewCard = ucb
                .path("cashcards/{id}")
                .buildAndExpand(savedCard.id())
                .toUri();
        return ResponseEntity.created(locationOfNewCard).build();
    }

    @GetMapping
    private ResponseEntity<Iterable<CashCard>> findAll(Pageable pageable, Principal principal) {
        Page<CashCard> page = cardRepository.findByOwner(principal.getName(),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize()
                )
        );
        return ResponseEntity.ok(page.getContent());
    }

    @PutMapping("/{requestedID}")
    private ResponseEntity<Void> putCashCard(
            @PathVariable Long requestedID,
            @RequestBody CashCard cardUpdate,
            Principal principal
    ) {
        if (cardRepository.findByIdAndOwnerAndActiveIsTrue(requestedID, principal.getName()) != null) {
            cardRepository.save(new CashCard(requestedID, cardUpdate.amount(), principal.getName(), true));
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteCashCard(@PathVariable Long id, Principal principal) {
        CashCard card = cardRepository.findByIdAndOwner(id, principal.getName());
        if (card != null) {
            cardRepository.save(new CashCard(card.id(), card.amount(), principal.getName(), false));
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
