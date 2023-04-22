package com.alsomeb.shopletapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@NoArgsConstructor
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

    // Not owning side, that's Product Entity
    // A many-to-many rel doesn't have an owning side in the db
    // we configure the join table in the Product entity and ref it here only
    @ManyToMany(mappedBy = "shoppingLists")
    @JsonIgnore
    @ToString.Exclude
    Set<ProductEntity> products;

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
