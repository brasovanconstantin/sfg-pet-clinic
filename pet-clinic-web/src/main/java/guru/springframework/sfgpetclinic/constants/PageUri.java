package guru.springframework.sfgpetclinic.constants;

import lombok.AllArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class PageUri {

    public static final String PATH_SEPARATOR = "/";
    public static final String OWNERS_BASE_URI = "/owners";
    public static final String FIND_OWNERS_URI = "/find";
    public static final String DISPLAY_OWNER_BY_ID_URI = "/{ownerId}";
    public static final String CREATE_OWNER_URI = "/new";
    public static final String UPDATE_OWNER_URI = "{ownerId}/edit";
}
