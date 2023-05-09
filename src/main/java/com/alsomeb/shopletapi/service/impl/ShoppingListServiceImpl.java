package com.alsomeb.shopletapi.service.impl;

import com.alsomeb.shopletapi.dto.ShoppingListDto;
import com.alsomeb.shopletapi.entity.ShoppingListEntity;
import com.alsomeb.shopletapi.exception.ShoppingListNotFoundException;
import com.alsomeb.shopletapi.repository.ShoppingListRepository;
import com.alsomeb.shopletapi.service.ShoppingListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ShoppingListServiceImpl implements ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;

    @Autowired
    public ShoppingListServiceImpl(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    // CRUD + getListsOrderByDateDesc()
    @Override
    public List<ShoppingListDto> getAllShoppingLists() {
        final var foundEntities = shoppingListRepository.findAll();
        return foundEntities.stream()
                .map(shoppingListEntity -> shoppingListEntityToDTO(shoppingListEntity))
                .collect(Collectors.toList());
    }

    @Override
    public List<ShoppingListDto> getListsOrderByDateAsc() {
        return shoppingListRepository.findAll().stream()
                .sorted(Comparator.comparing(ShoppingListEntity::getAdded))
                .map(shoppingListEntity -> shoppingListEntityToDTO(shoppingListEntity))
                .collect(Collectors.toList());
    }

    @Override
    public ShoppingListDto getById(long id) {
        final var foundEntity = shoppingListRepository.findById(id)
                .orElseThrow(() -> new ShoppingListNotFoundException("Cant find ShoppingList with id: " + id));

        return shoppingListEntityToDTO(foundEntity);
    }

    @Override
    public ShoppingListDto save(ShoppingListDto shoppingListDto) {
        /*
         1. We don't leak our info on Entity
         2. Takes A ShoppingListDto
         3. Converts a ShoppingListDto to ShoppingListEntity
         4. We then save our ShoppingListEntity with repository that returns a new SAVED ShoppingListEntity
         5. Then we return our saved ShoppingListEntity converted to a DTO
         */
        var exists = doesListExist(shoppingListDto);

        if(exists) {
            // Hämta fr DB By Id
            var fromDB = getById(shoppingListDto.getId());

            // Upd Fält + Spara ner till DB
            final ShoppingListEntity shoppingListEntity = shoppingListDTOToEntity(fromDB);
            shoppingListEntity.setDescription(shoppingListDto.getDescription());
            shoppingListEntity.setAdded(shoppingListDto.getAdded());
            final ShoppingListEntity savedEntity = shoppingListRepository.save(shoppingListEntity);
            return shoppingListEntityToDTO(savedEntity);
        }

        // Ny Skapande
        final ShoppingListEntity shoppingListEntity = shoppingListDTOToEntity(shoppingListDto);
        final ShoppingListEntity savedEntity = shoppingListRepository.save(shoppingListEntity);

        return shoppingListEntityToDTO(savedEntity);
    }

    @Override
    public boolean deleteById(long id) {
        Optional<ShoppingListEntity> match = shoppingListRepository.findById(id);
        if(match.isPresent()) {
            shoppingListRepository.deleteById(id);
            log.info("DELETE Shopping list by id: {}", match.get().getId());
            return true;
        }
        return false;
    }

    @Override
    public boolean doesListExist(ShoppingListDto shoppingListDto) {
        final ShoppingListEntity shoppingListEntity = shoppingListDTOToEntity(shoppingListDto);
        return shoppingListRepository.existsById(shoppingListEntity.getId());
    }

    // Helper Classes
    private ShoppingListEntity shoppingListDTOToEntity(ShoppingListDto shoppingListDto) {
        return ShoppingListEntity.builder()
                .id(shoppingListDto.getId())
                .added(shoppingListDto.getAdded())
                .description(shoppingListDto.getDescription())
                .build();
    }

    public ShoppingListDto shoppingListEntityToDTO(ShoppingListEntity shoppingListEntity) {
        return ShoppingListDto.builder()
                .id(shoppingListEntity.getId())
                .added(shoppingListEntity.getAdded())
                .description(shoppingListEntity.getDescription())
                .build();
    }
}
