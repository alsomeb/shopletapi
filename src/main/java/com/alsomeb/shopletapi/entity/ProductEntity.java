package com.alsomeb.shopletapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Products")
public class ProductEntity {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long id;

    @NotBlank(message = "Product name is mandatory")
    @Size(min = 1, message = "Minimum 2 letters for product name")
    private String name;

    @Min(value = 1, message = "Min 1 product")
    private int amount;

    // it's a good practice to mark the many-to-one side as the owning side.
    // Join Column is the actual FK in table
    @ManyToOne
    @JoinColumn(name = "shoppinglist_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ShoppingListEntity shoppingList;

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
