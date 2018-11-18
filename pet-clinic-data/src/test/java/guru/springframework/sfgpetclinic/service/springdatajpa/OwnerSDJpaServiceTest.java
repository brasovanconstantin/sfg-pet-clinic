package guru.springframework.sfgpetclinic.service.springdatajpa;

import com.google.common.collect.ImmutableList;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.repositories.OwnerRepository;
import guru.springframework.sfgpetclinic.repositories.PetRepository;
import guru.springframework.sfgpetclinic.repositories.PetTypeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static guru.springframework.sfgpetclinic.util.TestConstants.FIRST_OWNER_ID;
import static guru.springframework.sfgpetclinic.util.TestConstants.OWNER_LAST_NAME;
import static guru.springframework.sfgpetclinic.util.TestConstants.SECOND_OWNER_ID;
import static guru.springframework.sfgpetclinic.util.TestEntityGenerator.createOwner;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OwnerSDJpaServiceTest {

    @Captor
    private ArgumentCaptor<Owner> ownerArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> idArgumentCaptor;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private PetTypeRepository petTypeRepository;

    private OwnerSDJpaService service;

    @BeforeEach
    void setUp() {
        service = new OwnerSDJpaService(ownerRepository, petRepository, petTypeRepository);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(ownerRepository, petRepository, petTypeRepository);
    }

    @Test
    void findByLastName() {
        when(ownerRepository.findByLastName(OWNER_LAST_NAME))
            .thenReturn(createOwner(FIRST_OWNER_ID, OWNER_LAST_NAME));

        assertThat(service.findByLastName(OWNER_LAST_NAME))
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", FIRST_OWNER_ID)
            .hasFieldOrPropertyWithValue("lastName", OWNER_LAST_NAME);

        verify(ownerRepository).findByLastName(OWNER_LAST_NAME);
    }

    @Test
    void findAll() {
        when(ownerRepository.findAll())
            .thenReturn(ImmutableList.of(createOwner(FIRST_OWNER_ID), createOwner(SECOND_OWNER_ID)));

        assertThat(service.findAll())
            .hasSize(2)
            .extracting("id").containsExactlyInAnyOrder(FIRST_OWNER_ID, SECOND_OWNER_ID);

        verify(ownerRepository).findAll();
    }

    @Test
    void findById() {
        when(ownerRepository.findById(FIRST_OWNER_ID))
            .thenReturn(Optional.of(createOwner(FIRST_OWNER_ID)));

        assertThat(service.findById(FIRST_OWNER_ID))
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", FIRST_OWNER_ID);

        verify(ownerRepository).findById(FIRST_OWNER_ID);
    }

    @Test
    void save() {
        final Owner ownerToBeSaved = createOwner(FIRST_OWNER_ID);

        when(ownerRepository.save(ownerToBeSaved))
            .thenReturn(createOwner(FIRST_OWNER_ID));

        assertThat(service.save(ownerToBeSaved))
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", FIRST_OWNER_ID);

        verify(ownerRepository).save(ownerToBeSaved);
    }

    @Test
    void delete() {
        final Owner ownerToBeDeleted = createOwner(FIRST_OWNER_ID);

        doNothing().when(ownerRepository).delete(ownerToBeDeleted);

        service.delete(ownerToBeDeleted);

        verify(ownerRepository).delete(ownerArgumentCaptor.capture());

        assertThat(ownerArgumentCaptor.getValue())
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", FIRST_OWNER_ID);
    }

    @Test
    void deleteById() {
        doNothing().when(ownerRepository).deleteById(FIRST_OWNER_ID);

        service.deleteById(FIRST_OWNER_ID);

        verify(ownerRepository).deleteById(idArgumentCaptor.capture());

        assertThat(idArgumentCaptor.getValue())
            .isEqualTo(FIRST_OWNER_ID);
    }
}