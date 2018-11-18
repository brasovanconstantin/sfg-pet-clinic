package guru.springframework.sfgpetclinic.controller;

import guru.springframework.sfgpetclinic.service.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import static guru.springframework.sfgpetclinic.constants.PageUri.FIND_OWNERS_URI;
import static guru.springframework.sfgpetclinic.constants.PageUri.OWNERS_BASE_URI;
import static guru.springframework.sfgpetclinic.constants.Views.NOT_IMPLEMENTED_VIEW;
import static guru.springframework.sfgpetclinic.constants.Views.OWNERS_LIST_VIEW;

@Controller
@RequestMapping(OWNERS_BASE_URI)
public class OwnerController {

    private static final String OWNERS_ATTRIBUTE = "owners";

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @RequestMapping({"", "/", "/index", "/index.html"})
    public String listOwners(Model model) {
        model.addAttribute(OWNERS_ATTRIBUTE, ownerService.findAll());

        return OWNERS_LIST_VIEW;
    }

    @RequestMapping(FIND_OWNERS_URI)
    public String findOwners() {
        return NOT_IMPLEMENTED_VIEW;
    }
}
