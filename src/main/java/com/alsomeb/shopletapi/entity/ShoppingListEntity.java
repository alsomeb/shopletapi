package com.alsomeb.shopletapi.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDate;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "shopping_lists")
public class ShoppingListEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty(message = "Description is mandatory")
    private String description;

    @NotNull(message = "missing date field 'added'")
    @FutureOrPresent(message = "date added must be present or in the future")
    private LocalDate added;

    // https://www.baeldung.com/hibernate-one-to-many
    // Inverse side, Bidirectional relationship
    @ToString.Exclude
    @OneToMany(mappedBy = "shoppingList", cascade = CascadeType.ALL)
    private Set<ProductEntity> products;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ShoppingListEntity that = (ShoppingListEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
