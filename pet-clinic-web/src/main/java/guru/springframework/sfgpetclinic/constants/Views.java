package guru.springframework.sfgpetclinic.constants;

import lombok.AllArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class Views {

    public static final String REDIRECT = "redirect:";
    public static final String OWNERS_LIST_INDEX_VIEW = "owners/index";
    public static final String FIND_OWNERS_VIEW = "owners/findOwners";
    public static final String OWNER_DETAILS_VIEW = "owners/ownerDetails";
    public static final String OWNERS_LIST_VIEW = "owners/ownersList";
    public static final String OWNERS_CREATE_AND_UPDATE_VIEW = "owners/createOrUpdateOwnerForm";
    public static final String PETS_CREATE_AND_UPDATE_VIEW = "pets/createOrUpdatePetForm";
}
