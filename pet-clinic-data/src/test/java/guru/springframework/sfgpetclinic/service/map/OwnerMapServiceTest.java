package guru.springframework.sfgpetclinic.service.map;

import com.google.common.collect.ImmutableList;
import guru.springframework.sfgpetclinic.model.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static guru.springframework.sfgpetclinic.util.TestConstants.FIRST_OWNER_ID;
import static guru.springframework.sfgpetclinic.util.TestConstants.OWNER_LAST_NAME;
import static guru.springframework.sfgpetclinic.util.TestConstants.SECOND_OWNER_ID;
import static guru.springframework.sfgpetclinic.util.TestConstants.THIRD_OWNER_ID;
import static guru.springframework.sfgpetclinic.util.TestEntityGenerator.createOwner;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class OwnerMapServiceTest {

    private OwnerMapService ownerMapService;

    @BeforeEach
    void setUp() {
        ownerMapService = new OwnerMapService(new PetTypeMapService(), new PetMapService());

        saveOwners(ImmutableList.of(
            createOwner(FIRST_OWNER_ID),
            createOwner(SECOND_OWNER_ID)));
    }

    @Test
    void findAll() {
        final Set<Owner> actual = ownerMapService.findAll();

        assertThat(actual)
            .hasSize(2)
            .extracting("id")
            .containsExactlyInAnyOrder(FIRST_OWNER_ID, SECOND_OWNER_ID);
    }

    @Test
    void deleteById() {
        ownerMapService.deleteById(SECOND_OWNER_ID);

        assertThat(ownerMapService.findById(SECOND_OWNER_ID))
            .isNull();
    }

    @Test
    void delete() {
        ownerMapService.delete(ownerMapService.findById(FIRST_OWNER_ID));

        assertThat(ownerMapService.findAll())
            .hasSize(1)
            .extracting("id")
            .doesNotContain(FIRST_OWNER_ID)
            .containsExactlyInAnyOrder(SECOND_OWNER_ID);
    }

    @Test
    void saveExistingId() {
        assertThat(ownerMapService.save(createOwner(SECOND_OWNER_ID)))
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", SECOND_OWNER_ID);
    }

    @Test
    void saveNoId() {
        assertThat(ownerMapService.save(Owner.builder().build()))
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", THIRD_OWNER_ID);
    }

    @Test
    void findById() {
        assertThat(ownerMapService.findById(FIRST_OWNER_ID))
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", FIRST_OWNER_ID);
    }

    @Test
    void findByLastName() {
        ImmutableList.of(FIRST_OWNER_ID, SECOND_OWNER_ID).forEach(id -> ownerMapService.deleteById(id));
        ownerMapService.save(createOwner(THIRD_OWNER_ID, OWNER_LAST_NAME));

        assertThat(ownerMapService.findByLastName(OWNER_LAST_NAME))
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", THIRD_OWNER_ID)
            .hasFieldOrPropertyWithValue("lastName", OWNER_LAST_NAME);
    }

    private void saveOwners(List<Owner> owners) {
        owners.forEach(owner -> ownerMapService.save(owner));
    }
}