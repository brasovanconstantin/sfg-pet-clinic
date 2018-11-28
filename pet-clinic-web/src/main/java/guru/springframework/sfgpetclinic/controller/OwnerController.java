package guru.springframework.sfgpetclinic.controller;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import javax.validation.Valid;

import static guru.springframework.sfgpetclinic.constants.PageUri.CREATE_OWNER_URI;
import static guru.springframework.sfgpetclinic.constants.PageUri.DISPLAY_OWNER_BY_ID_URI;
import static guru.springframework.sfgpetclinic.constants.PageUri.FIND_OWNERS_URI;
import static guru.springframework.sfgpetclinic.constants.PageUri.OWNERS_BASE_URI;
import static guru.springframework.sfgpetclinic.constants.PageUri.PATH_SEPARATOR;
import static guru.springframework.sfgpetclinic.constants.PageUri.UPDATE_OWNER_URI;
import static guru.springframework.sfgpetclinic.constants.Views.FIND_OWNERS_VIEW;
import static guru.springframework.sfgpetclinic.constants.Views.OWNERS_CREATE_AND_UPDATE_VIEW;
import static guru.springframework.sfgpetclinic.constants.Views.OWNERS_LIST_INDEX_VIEW;
import static guru.springframework.sfgpetclinic.constants.Views.OWNERS_LIST_VIEW;
import static guru.springframework.sfgpetclinic.constants.Views.OWNER_DETAILS_VIEW;
import static guru.springframework.sfgpetclinic.constants.Views.REDIRECT;
import static java.util.Objects.isNull;

@RequiredArgsConstructor
@Controller
@RequestMapping(OWNERS_BASE_URI)
public class OwnerController {

    private static final String OWNERS_ATTRIBUTE = "owners";
    private static final String OWNER_ATTRIBUTE = "owner";
    private static final String SELECTIONS_ATTRIBUTE = "selections";
    private static final String LIKE = "%";

    private final OwnerService ownerService;

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping({"/", "/index", "/index.html"})
    public String listOwners(Model model) {
        model.addAttribute(OWNERS_ATTRIBUTE, ownerService.findAll());

        return OWNERS_LIST_INDEX_VIEW;
    }

    @GetMapping(FIND_OWNERS_URI)
    public String findOwners(Model model) {
        model.addAttribute(OWNER_ATTRIBUTE, Owner.builder().build());

        return FIND_OWNERS_VIEW;
    }

    @GetMapping("")
    public String processFindForm(Owner owner, BindingResult result, Model model) {
        if (isNull(owner.getLastName())) {
            owner.setLastName("");
        }

        final List<Owner> owners = ownerService.findAllByLastNameLike(LIKE + owner.getLastName() + LIKE);

        if (owners.isEmpty()) {
            result.rejectValue("lastName", "notFound", "not found");

            return FIND_OWNERS_VIEW;
        } else if (owners.size() == 1) {
            owner = owners.iterator().next();

            return "redirect:/owners/" + owner.getId();
        } else {
            model.addAttribute(SELECTIONS_ATTRIBUTE, owners);

            return OWNERS_LIST_VIEW;
        }
    }

    @GetMapping(DISPLAY_OWNER_BY_ID_URI)
    public ModelAndView showOwner(@PathVariable Long ownerId) {
        final ModelAndView model = new ModelAndView(OWNER_DETAILS_VIEW);
        model.addObject(ownerService.findById(ownerId));

        return model;
    }

    @GetMapping(CREATE_OWNER_URI)
    public String initCreationForm(Model model) {
        model.addAttribute(OWNER_ATTRIBUTE, Owner.builder().build());

        return OWNERS_CREATE_AND_UPDATE_VIEW;
    }

    @PostMapping(CREATE_OWNER_URI)
    public String processCreationForm(@Valid Owner owner, BindingResult result) {
        if (result.hasErrors()) {
            return OWNERS_CREATE_AND_UPDATE_VIEW;
        }

        final Owner savedOwner = ownerService.save(owner);

        return REDIRECT + OWNERS_BASE_URI + PATH_SEPARATOR + savedOwner.getId();
    }

    @GetMapping(UPDATE_OWNER_URI)
    public String initUpdateOwnerForm(@PathVariable Long ownerId, Model model) {
        model.addAttribute(OWNER_ATTRIBUTE, ownerService.findById(ownerId));

        return OWNERS_CREATE_AND_UPDATE_VIEW;
    }

    @PostMapping(UPDATE_OWNER_URI)
    public String processUpdateOwnerForm(@Valid Owner owner, @PathVariable Long ownerId, BindingResult result) {
        if (result.hasErrors()) {
            return OWNERS_CREATE_AND_UPDATE_VIEW;
        }

        owner.setId(ownerId);
        final Owner savedOwner = ownerService.save(owner);

        return REDIRECT + OWNERS_BASE_URI + PATH_SEPARATOR + savedOwner.getId();
    }
}
