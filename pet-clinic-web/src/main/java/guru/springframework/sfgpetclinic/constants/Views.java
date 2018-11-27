package guru.springframework.sfgpetclinic.constants;

import lombok.AllArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class Views {

    public static final String OWNERS_LIST_VIEW = "owners/index";
    public static final String NOT_IMPLEMENTED_VIEW = "notimplemented";
    public static final String OWNER_DETAILS_VIEW = "owners/ownerDetails";
}
