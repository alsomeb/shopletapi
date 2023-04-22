package com.alsomeb.shopletapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.Hibernate;

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
@Table(name = "Products")
public class ProductEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty(message = "Product name is mandatory")
    private String name;

    // Flera Producter Kan finnas på Flera ShoppingLists
    // https://www.baeldung.com/jpa-many-to-many
    // Behöver ej def namn o join columns och join table, men jag vill ha namnen på mitt sätt!
    @ManyToMany
    @JoinTable(
            name = "shoppinglist_product",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "shoppinglist_id")
    )
    @ToString.Exclude
    Set<ShoppingListEntity> shoppingLists;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductEntity that = (ProductEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
