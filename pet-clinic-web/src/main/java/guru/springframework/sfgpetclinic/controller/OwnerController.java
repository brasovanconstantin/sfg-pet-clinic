package guru.springframework.sfgpetclinic.controller;

import guru.springframework.sfgpetclinic.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static guru.springframework.sfgpetclinic.constants.PageUri.DISPLAY_OWNER_BY_ID_URI;
import static guru.springframework.sfgpetclinic.constants.PageUri.FIND_OWNERS_URI;
import static guru.springframework.sfgpetclinic.constants.PageUri.OWNERS_BASE_URI;
import static guru.springframework.sfgpetclinic.constants.Views.NOT_IMPLEMENTED_VIEW;
import static guru.springframework.sfgpetclinic.constants.Views.OWNERS_LIST_VIEW;
import static guru.springframework.sfgpetclinic.constants.Views.OWNER_DETAILS_VIEW;

@RequiredArgsConstructor
@Controller
@RequestMapping(OWNERS_BASE_URI)
public class OwnerController {

    private static final String OWNERS_ATTRIBUTE = "owners";

    private final OwnerService ownerService;

    @GetMapping({"", "/", "/index", "/index.html"})
    public String listOwners(Model model) {
        model.addAttribute(OWNERS_ATTRIBUTE, ownerService.findAll());

        return OWNERS_LIST_VIEW;
    }

    @GetMapping(FIND_OWNERS_URI)
    public String findOwners() {
        return NOT_IMPLEMENTED_VIEW;
    }

    @GetMapping(DISPLAY_OWNER_BY_ID_URI)
    public ModelAndView showOwner(@PathVariable("ownerId") Long ownerId) {
        final ModelAndView model = new ModelAndView(OWNER_DETAILS_VIEW);
        model.addObject(ownerService.findById(ownerId));

        return model;
    }
}
