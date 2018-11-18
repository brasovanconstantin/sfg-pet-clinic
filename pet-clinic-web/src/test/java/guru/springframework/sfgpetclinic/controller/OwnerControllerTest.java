package guru.springframework.sfgpetclinic.controller;

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

import java.util.Set;

import static guru.springframework.sfgpetclinic.constants.PageUri.FIND_OWNERS_URI;
import static guru.springframework.sfgpetclinic.constants.PageUri.OWNERS_BASE_URI;
import static guru.springframework.sfgpetclinic.constants.Views.NOT_IMPLEMENTED_VIEW;
import static guru.springframework.sfgpetclinic.constants.Views.OWNERS_LIST_VIEW;
import static guru.springframework.sfgpetclinic.util.TestConstants.FIRST_OWNER_ID;
import static guru.springframework.sfgpetclinic.util.TestConstants.OWNERS_ATTRIBUTE;
import static guru.springframework.sfgpetclinic.util.TestConstants.SECOND_OWNER_ID;
import static guru.springframework.sfgpetclinic.util.TestEntityGenerator.createOwner;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

        mockMvc.perform(get(OWNERS_BASE_URI))
            .andExpect(view().name(OWNERS_LIST_VIEW))
            .andExpect(model().attribute(OWNERS_ATTRIBUTE, hasSize(2)))
            .andExpect(model().attribute(OWNERS_ATTRIBUTE, is(owners)))
            .andExpect(status().isOk());

        verify(service).findAll();
    }

    @Test
    void findOwners() throws Exception {
        mockMvc.perform(get(OWNERS_BASE_URI + FIND_OWNERS_URI))
            .andExpect(view().name(NOT_IMPLEMENTED_VIEW))
            .andExpect(status().isOk());
    }
}