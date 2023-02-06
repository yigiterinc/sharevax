package com.sharevax.core.service;

import com.sharevax.core.model.Delivery;
import com.sharevax.core.model.Demand;
import com.sharevax.core.model.Suggestion;
import com.sharevax.core.model.Suggestion.ApprovalStatus;
import com.sharevax.core.model.Supply;
import com.sharevax.core.model.dto.SuggestionResponseDto;
import com.sharevax.core.repository.SuggestionRepository;
import com.sharevax.core.repository.SupplyRepository;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

@Service
public class SuggestionService {

    private final SuggestionRepository suggestionRepository;
    private final DeliveryService deliveryService;
    private DemandService demandService;
    private SupplyService supplyService;

    public SuggestionService(SuggestionRepository suggestionRepository,
        DeliveryService deliveryService,
        DemandService demandService, SupplyService supplyService) {
        this.suggestionRepository = suggestionRepository;
        this.deliveryService = deliveryService;
        this.demandService = demandService;
        this.supplyService = supplyService;
    }

    public Suggestion createSuggestion(Supply supply, Demand demand) {
        Suggestion suggestion = new Suggestion();
        suggestion.setSupply(supply);
        suggestion.setDemand(demand);
        suggestion.setQuantity(BigInteger.valueOf(
            Math.min(supply.getQuantity().intValue(), demand.getQuantity().intValue())));
        suggestionRepository.save(suggestion);
        return suggestion;
    }

    public boolean setApprovalStatus(SuggestionResponseDto response) {
        Suggestion suggestion = suggestionRepository.findById(response.getSuggestionId())
            .orElseThrow(() -> new RuntimeException("Suggestion not found"));

        if (response.getApprovalStatus().equals(ApprovalStatus.DENIED)) {
            suggestionRepository.deleteById(response.getSuggestionId());
            return true;
        }
        if (response.getApprovalStatus().equals(ApprovalStatus.APPROVED)) {
            boolean isDemander =
                    Objects.equals(suggestion.getDemand().getCountry().getId(), response.getCountryId());
            boolean isSupplier =
                    Objects.equals(suggestion.getSupply().getCountry().getId(), response.getCountryId());
            if (isSupplier) {
                suggestion.setSupplierStatus(response.getApprovalStatus());
            } else if (isDemander) {
                suggestion.setDemanderStatus(response.getApprovalStatus());
            } else {
                throw new RuntimeException("Neither demander nor supplier. Wrong country id");
            }
        }

        boolean allApproved = suggestion.getDemanderStatus() == ApprovalStatus.APPROVED
            && suggestion.getSupplierStatus() == ApprovalStatus.APPROVED;
        if (allApproved) {
            createDelivery(suggestion, response.getCurrentDate());
            deleteSuggestion(suggestion);
        } else {
            suggestionRepository.save(suggestion);
        }

        return true;
    }

    private void deleteSuggestion(Suggestion suggestion) {
        // delete the suggestion
        suggestionRepository.deleteById(suggestion.getId());

        // decrease the amount in supply and demand, and check if it is deleted after update
        demandService.decreaseQuantity(suggestion.getDemand().getId(), suggestion.getQuantity());
        supplyService.decreaseQuantity(suggestion.getSupply().getId(), suggestion.getQuantity());

        // delete all related suggestions
        suggestionRepository.deleteSuggestionByDemand(suggestion.getDemand().getId());
        suggestionRepository.deleteSuggestionBySupply(suggestion.getSupply().getId());
    }

    private void createDelivery(Suggestion suggestion, Date currentDate) {
         Delivery delivery = deliveryService.createDelivery(
            suggestion.getSupply().getCountry().getHarbors().get(0), // TODO use closest harbor
            suggestion.getDemand().getCountry().getHarbors().get(0), // TODO use closest harbor
            currentDate,
            suggestion);
        System.out.println("Created delivery: " + delivery);
    }

    public List<Suggestion> getSuggestionsByCountryId(Integer countryId) {
        return suggestionRepository.findRelatedSuggestions(countryId);
    }

    public void deleteAllSuggestions() {
        suggestionRepository.deleteAll();
    }

    public void wipeOutSuggestions() {
        suggestionRepository.deleteAll();
    }
}
