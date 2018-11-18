package guru.springframework.sfgpetclinic.util;

import guru.springframework.sfgpetclinic.model.Owner;

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
}
