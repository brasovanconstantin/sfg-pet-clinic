package guru.springframework.sfgpetclinic.controller;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.service.OwnerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Set;

import static guru.springframework.sfgpetclinic.constants.PageUri.CREATE_OWNER_URI;
import static guru.springframework.sfgpetclinic.constants.PageUri.FIND_OWNERS_URI;
import static guru.springframework.sfgpetclinic.constants.PageUri.OWNERS_BASE_URI;
import static guru.springframework.sfgpetclinic.constants.PageUri.PATH_SEPARATOR;
import static guru.springframework.sfgpetclinic.constants.Views.FIND_OWNERS_VIEW;
import static guru.springframework.sfgpetclinic.constants.Views.OWNERS_CREATE_AND_UPDATE_VIEW;
import static guru.springframework.sfgpetclinic.constants.Views.OWNERS_LIST_INDEX_VIEW;
import static guru.springframework.sfgpetclinic.constants.Views.OWNERS_LIST_VIEW;
import static guru.springframework.sfgpetclinic.constants.Views.OWNER_DETAILS_VIEW;
import static guru.springframework.sfgpetclinic.constants.Views.REDIRECT;
import static guru.springframework.sfgpetclinic.util.TestConstants.FIRST_OWNER_ID;
import static guru.springframework.sfgpetclinic.util.TestConstants.LIKE;
import static guru.springframework.sfgpetclinic.util.TestConstants.OWNERS_ATTRIBUTE;
import static guru.springframework.sfgpetclinic.util.TestConstants.OWNER_ATTRIBUTE;
import static guru.springframework.sfgpetclinic.util.TestConstants.OWNER_LAST_NAME;
import static guru.springframework.sfgpetclinic.util.TestConstants.SECOND_OWNER_ID;
import static guru.springframework.sfgpetclinic.util.TestConstants.SELECTIONS_ATTRIBUTE;
import static guru.springframework.sfgpetclinic.util.TestEntityGenerator.createOwner;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OwnerService service;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new OwnerController(service))
            .build();
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(service);
    }

    @Test
    void listOwners() throws Exception {
        final Set<Owner> owners = ImmutableSet.of(createOwner(FIRST_OWNER_ID), createOwner(SECOND_OWNER_ID));

        when(service.findAll()).thenReturn(owners);

        mockMvc.perform(get(OWNERS_BASE_URI + PATH_SEPARATOR))
            .andExpect(view().name(OWNERS_LIST_INDEX_VIEW))
            .andExpect(model().attribute(OWNERS_ATTRIBUTE, hasSize(2)))
            .andExpect(model().attribute(OWNERS_ATTRIBUTE, is(owners)))
            .andExpect(status().isOk());

        verify(service).findAll();
    }

    @Test
    void findOwners() throws Exception {
        mockMvc.perform(get(OWNERS_BASE_URI + FIND_OWNERS_URI))
            .andExpect(view().name(FIND_OWNERS_VIEW))
            .andExpect(model().attributeExists(OWNER_ATTRIBUTE))
            .andExpect(status().isOk());
    }

    @Test
    void processFindFormReturnOne() throws Exception {
        final Owner owner = createOwner(FIRST_OWNER_ID, OWNER_LAST_NAME);

        when(service.findAllByLastNameLike(LIKE + OWNER_LAST_NAME + LIKE)).thenReturn(ImmutableList.of(owner));

        mockMvc.perform(get(OWNERS_BASE_URI)
            .flashAttr(OWNER_ATTRIBUTE, owner))
            .andExpect(view().name(REDIRECT + OWNERS_BASE_URI + PATH_SEPARATOR + owner.getId()))
            .andExpect(status().is3xxRedirection());

        verify(service).findAllByLastNameLike(LIKE + OWNER_LAST_NAME + LIKE);
    }

    @Test
    void processFindFormReturnMany() throws Exception {
        final List<Owner> owners = ImmutableList.of(createOwner(FIRST_OWNER_ID, OWNER_LAST_NAME),
            createOwner(SECOND_OWNER_ID, OWNER_LAST_NAME));

        when(service.findAllByLastNameLike(LIKE + OWNER_LAST_NAME + LIKE)).thenReturn(owners);

        mockMvc.perform(get(OWNERS_BASE_URI)
            .flashAttr(OWNER_ATTRIBUTE, owners.iterator().next()))
            .andExpect(view().name(OWNERS_LIST_VIEW))
            .andExpect(model().attribute(SELECTIONS_ATTRIBUTE, hasSize(2)))
            .andExpect(model().attribute(SELECTIONS_ATTRIBUTE, owners))
            .andExpect(status().isOk());

        verify(service).findAllByLastNameLike(LIKE + OWNER_LAST_NAME + LIKE);
    }

    @Test
    void processFindFormLastNameNull() throws Exception {
        when(service.findAllByLastNameLike(LIKE + EMPTY + LIKE)).thenReturn(emptyList());

        mockMvc.perform(get(OWNERS_BASE_URI))
            .andExpect(view().name(FIND_OWNERS_VIEW))
            .andExpect(status().isOk());

        verify(service).findAllByLastNameLike(LIKE + EMPTY + LIKE);
    }

    @Test
    void displayOwner() throws Exception {
        final Owner owner = createOwner(FIRST_OWNER_ID);

        when(service.findById(FIRST_OWNER_ID)).thenReturn(owner);

        mockMvc.perform(get(OWNERS_BASE_URI + PATH_SEPARATOR + owner.getId()))
            .andExpect(view().name(OWNER_DETAILS_VIEW))
            .andExpect(model().attribute(OWNER_ATTRIBUTE, is(owner)))
            .andExpect(status().isOk());

        verify(service).findById(FIRST_OWNER_ID);
    }

    @Test
    void initCreationForm() throws Exception {
        final Owner owner = Owner.builder().build();

        mockMvc.perform(get(OWNERS_BASE_URI + CREATE_OWNER_URI))
            .andExpect(view().name(OWNERS_CREATE_AND_UPDATE_VIEW))
            .andExpect(model().attribute(OWNER_ATTRIBUTE, is(owner)))
            .andExpect(status().isOk());

        verifyZeroInteractions(service);
    }

    @Test
    void processCreationForm() throws Exception {
        final Owner owner = createOwner(FIRST_OWNER_ID);

        when(service.save(owner)).thenReturn(owner);

        mockMvc.perform(post(OWNERS_BASE_URI + CREATE_OWNER_URI)
            .flashAttr(OWNER_ATTRIBUTE, owner))
            .andExpect(view().name(REDIRECT + OWNERS_BASE_URI + PATH_SEPARATOR + owner.getId()))
            .andExpect(model().attribute(OWNER_ATTRIBUTE, is(owner)))
            .andExpect(status().is3xxRedirection());

        verify(service).save(owner);
    }

    @Test
    void initUpdateOwnerForm() throws Exception {
        final Owner owner = createOwner(FIRST_OWNER_ID);

        when(service.findById(FIRST_OWNER_ID)).thenReturn(owner);

        mockMvc.perform(get(OWNERS_BASE_URI + PATH_SEPARATOR + owner.getId() + "/edit"))
            .andExpect(view().name(OWNERS_CREATE_AND_UPDATE_VIEW))
            .andExpect(model().attribute(OWNER_ATTRIBUTE, is(owner)))
            .andExpect(status().isOk());

        verify(service).findById(FIRST_OWNER_ID);
    }

    @Test
    void processUpdateOwnerForm() throws Exception {
        final Owner owner = createOwner(FIRST_OWNER_ID);

        when(service.save(owner)).thenReturn(owner);

        mockMvc.perform(post(OWNERS_BASE_URI + PATH_SEPARATOR + owner.getId() + "/edit")
            .flashAttr(OWNER_ATTRIBUTE, owner))
            .andExpect(view().name(REDIRECT + OWNERS_BASE_URI + PATH_SEPARATOR + owner.getId()))
            .andExpect(model().attribute(OWNER_ATTRIBUTE, is(owner)))
            .andExpect(status().is3xxRedirection());

        verify(service).save(owner);
    }
}