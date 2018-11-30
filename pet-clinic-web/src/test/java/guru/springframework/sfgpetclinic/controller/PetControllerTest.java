package guru.springframework.sfgpetclinic.controller;

import com.google.common.collect.ImmutableSet;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.service.OwnerService;
import guru.springframework.sfgpetclinic.service.PetService;
import guru.springframework.sfgpetclinic.service.PetTypeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;

import static guru.springframework.sfgpetclinic.constants.Views.PETS_CREATE_AND_UPDATE_VIEW;
import static guru.springframework.sfgpetclinic.util.TestConstants.FIRST_OWNER_ID;
import static guru.springframework.sfgpetclinic.util.TestConstants.FIRST_PET_ID;
import static guru.springframework.sfgpetclinic.util.TestConstants.OWNER_ATTRIBUTE;
import static guru.springframework.sfgpetclinic.util.TestConstants.PET_ATTRIBUTE;
import static guru.springframework.sfgpetclinic.util.TestConstants.PET_NAME;
import static guru.springframework.sfgpetclinic.util.TestConstants.PET_TYPE_NAME;
import static guru.springframework.sfgpetclinic.util.TestConstants.TYPES_ATTRIBUTE;
import static guru.springframework.sfgpetclinic.util.TestEntityGenerator.createOwner;
import static guru.springframework.sfgpetclinic.util.TestEntityGenerator.createPet;
import static guru.springframework.sfgpetclinic.util.TestEntityGenerator.createPetType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class PetControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PetService petService;

    @Mock
    private OwnerService ownerService;

    @Mock
    private PetTypeService petTypeService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new PetController(petService, ownerService, petTypeService))
            .build();
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(petService, ownerService, petTypeService);
    }

    @Test
    void initCreationForm() throws Exception {
        final Owner owner = createOwner(FIRST_OWNER_ID);
        final Set<PetType> petTypes = ImmutableSet.of(createPetType(PET_TYPE_NAME));

        when(ownerService.findById(FIRST_OWNER_ID)).thenReturn(owner);
        when(petTypeService.findAll()).thenReturn(petTypes);

        mockMvc.perform(get("/owners/1/pets/new"))
            .andExpect(model().attribute(OWNER_ATTRIBUTE, owner))
            .andExpect(model().attribute(TYPES_ATTRIBUTE, petTypes))
            .andExpect(view().name(PETS_CREATE_AND_UPDATE_VIEW))
            .andExpect(status().isOk());

        verify(ownerService).findById(FIRST_OWNER_ID);
        verify(petTypeService).findAll();
    }

    @Test
    void processCreationForm() throws Exception {
        final Owner owner = createOwner(FIRST_OWNER_ID);
        final Set<PetType> petTypes = ImmutableSet.of(createPetType(PET_TYPE_NAME));
        final Pet pet = createPet(FIRST_PET_ID, PET_NAME);

        when(petTypeService.findAll()).thenReturn(petTypes);
        when(petService.save(pet)).thenReturn(pet);

        mockMvc.perform(post("/owners/1/pets/new")
            .flashAttr(OWNER_ATTRIBUTE, owner)
            .flashAttr(PET_ATTRIBUTE, pet))
            .andExpect(view().name("redirect:/owners/1"))
            .andExpect(status().is3xxRedirection());

        verify(petTypeService).findAll();
        verify(petService).save(pet);
    }

    @Test
    void initUpdateForm() throws Exception {
        final Owner owner = createOwner(FIRST_OWNER_ID);
        final Set<PetType> petTypes = ImmutableSet.of(createPetType(PET_TYPE_NAME));
        final Pet pet = createPet(FIRST_PET_ID, PET_NAME);

        when(ownerService.findById(FIRST_OWNER_ID)).thenReturn(owner);
        when(petTypeService.findAll()).thenReturn(petTypes);
        when(petService.findById(pet.getId())).thenReturn(pet);

        mockMvc.perform(get("/owners/1/pets/1/edit"))
            .andExpect(model().attribute(PET_ATTRIBUTE, pet))
            .andExpect(view().name(PETS_CREATE_AND_UPDATE_VIEW))
            .andExpect(status().isOk());

        verify(ownerService).findById(FIRST_OWNER_ID);
        verify(petTypeService).findAll();
        verify(petService).findById(FIRST_PET_ID);
    }

    @Test
    void processUpdateForm() throws Exception {
        final Owner owner = createOwner(FIRST_OWNER_ID);
        final Set<PetType> petTypes = ImmutableSet.of(createPetType(PET_TYPE_NAME));
        final Pet pet = createPet(FIRST_PET_ID, PET_NAME);

        when(ownerService.findById(FIRST_OWNER_ID)).thenReturn(owner);
        when(petTypeService.findAll()).thenReturn(petTypes);

        mockMvc.perform(post("/owners/1/pets/1/edit")
            .flashAttr(PET_ATTRIBUTE, pet))
            .andExpect(view().name("redirect:/owners/1"))
            .andExpect(status().is3xxRedirection());

        verify(ownerService).findById(FIRST_OWNER_ID);
        verify(petTypeService).findAll();
        verify(petService).save(pet);
    }
}