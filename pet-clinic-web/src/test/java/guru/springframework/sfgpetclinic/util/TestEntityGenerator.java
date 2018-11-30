package guru.springframework.sfgpetclinic.util;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;

public class TestEntityGenerator {

    public static Owner createOwner(Long id, String lastName) {
        final Owner owner = createOwner(id);
        owner.setLastName(lastName);

        return owner;
    }

    public static Owner createOwner(Long id) {
        return Owner.builder()
            .id(id)
            .build();
    }

    public static PetType createPetType(String name) {
        return PetType.builder()
            .name(name)
            .build();
    }

    public static Pet createPet(Long id, String name) {
        return Pet.builder()
            .id(id)
            .name(name)
            .build();
    }
}
