package guru.springframework.sfgpetclinic.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import static java.util.Objects.nonNull;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "owners")
public class Owner extends Person {

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "telephone")
    private String telephone;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private Set<Pet> pets = new HashSet<>();

    @Builder
    public Owner(Long id,
                 String firstName,
                 String lastName,
                 String address,
                 String city,
                 String telephone,
                 Set<Pet> pets) {
        super(id, firstName, lastName);
        this.address = address;
        this.city = city;
        this.telephone = telephone;

        if (nonNull(pets) && !pets.isEmpty()) {
            this.pets = pets;
        }
    }

    public Pet getPet(String name) {
        return getPet(name, false);
    }

    public Pet getPet(String name, Boolean ignoreNew) {
        return ignoreNew ? null : pets.stream()
            .filter(pet -> !pet.isNew() && pet.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
}
