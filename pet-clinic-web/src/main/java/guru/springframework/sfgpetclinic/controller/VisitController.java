package guru.springframework.sfgpetclinic.controller;

import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.service.PetService;
import guru.springframework.sfgpetclinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static guru.springframework.sfgpetclinic.constants.PageUri.OWNERS_BASE_URI;
import static guru.springframework.sfgpetclinic.constants.Views.REDIRECT;
import static guru.springframework.sfgpetclinic.constants.Views.VISITS_CREATE_AND_UPDATE_VIEW;

@RequiredArgsConstructor
@Controller
@RequestMapping(OWNERS_BASE_URI)
public class VisitController extends BaseController {

    private static final String VISIT_ATTRIBUTE = "visit";
    private static final String PET_ATTRIBUTE = "pet";

    private final VisitService visitService;
    private final PetService petService;

    @ModelAttribute(VISIT_ATTRIBUTE)
    public Visit loadPetWithVisit(@PathVariable Long petId, Model model) {
        final Pet pet = petService.findById(petId);
        model.addAttribute(PET_ATTRIBUTE, pet);
        final Visit visit = Visit.builder().build();
        pet.getVisits().add(visit);
        visit.setPet(pet);

        return visit;
    }

    @GetMapping("/*/pets/{petId}/visits/new")
    public String initNewVisitForm(@PathVariable Long petId, Model model) {
        return VISITS_CREATE_AND_UPDATE_VIEW;
    }

    @PostMapping("/{ownerId}/pets/{petId}/visits/new")
    public String processNewVisitForm(@Valid Visit visit, BindingResult result) {
        if (result.hasErrors()) {
            return VISITS_CREATE_AND_UPDATE_VIEW;
        } else {
            visitService.save(visit);

            return REDIRECT + OWNERS_BASE_URI + "/{ownerId}";
        }
    }
}
