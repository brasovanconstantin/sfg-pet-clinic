package guru.springframework.sfgpetclinic.controller;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.service.OwnerService;
import guru.springframework.sfgpetclinic.service.PetService;
import guru.springframework.sfgpetclinic.service.PetTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;
import javax.validation.Valid;

import static guru.springframework.sfgpetclinic.constants.Views.PETS_CREATE_AND_UPDATE_VIEW;
import static guru.springframework.sfgpetclinic.constants.Views.REDIRECT;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasLength;

@RequiredArgsConstructor
@Controller
@RequestMapping("/owners/{ownerId}")
public class PetController extends BaseController {

    private static final String PET_ATTRIBUTE = "pet";
    private static final String TYPES_ATTRIBUTE = "types";

    private final PetService petService;
    private final OwnerService ownerService;
    private final PetTypeService petTypeService;

    @ModelAttribute(TYPES_ATTRIBUTE)
    public Set<PetType> populatePetTypes() {
        return petTypeService.findAll();
    }

    @ModelAttribute(OWNER_ATTRIBUTE)
    public Owner findOwner(@PathVariable Long ownerId) {
        return ownerService.findById(ownerId);
    }

    @GetMapping("/pets/new")
    public String initCreationForm(Owner owner, Model model) {
        final Pet pet = Pet.builder().build();
        owner.getPets().add(pet);
        model.addAttribute(PET_ATTRIBUTE, pet);

        return PETS_CREATE_AND_UPDATE_VIEW;
    }

    @PostMapping("/pets/new")
    public String processCreationForm(Owner owner, @Valid Pet pet, BindingResult result, Model model) {
        if (hasLength(pet.getName()) && pet.isNew() && nonNull(owner.getPet(pet.getName(), true))) {
            result.rejectValue("name", "duplicate", "already exists");
        }

        if (result.hasErrors()) {
            model.addAttribute(PET_ATTRIBUTE, pet);

            return PETS_CREATE_AND_UPDATE_VIEW;
        }

        petService.save(pet);

        return REDIRECT + "/owners/" + owner.getId();
    }

    @GetMapping("/pets/{petId}/edit")
    public String initUpdateForm(@PathVariable Long petId, Model model) {
        model.addAttribute(PET_ATTRIBUTE, petService.findById(petId));

        return PETS_CREATE_AND_UPDATE_VIEW;
    }

    @PostMapping("/pets/{petId}/edit")
    public String processUpdateForm(Owner owner, @Valid Pet pet, BindingResult result, Model model) {
        if (result.hasErrors()) {
            pet.setOwner(owner);
            model.addAttribute(PET_ATTRIBUTE, pet);

            return PETS_CREATE_AND_UPDATE_VIEW;
        }

        owner.getPets().add(pet);
        petService.save(pet);

        return REDIRECT + "/owners/" + owner.getId();
    }
}
