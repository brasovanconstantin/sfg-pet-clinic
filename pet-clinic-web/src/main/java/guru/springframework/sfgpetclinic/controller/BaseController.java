package guru.springframework.sfgpetclinic.controller;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

public class BaseController {

    protected static final String OWNER_ATTRIBUTE = "owner";

    @InitBinder(OWNER_ATTRIBUTE)
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }
}
