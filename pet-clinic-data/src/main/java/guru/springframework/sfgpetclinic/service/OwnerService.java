package guru.springframework.sfgpetclinic.service;

import guru.springframework.sfgpetclinic.model.Owner;

public interface OwnerService extends PetClinicMainService<Owner> {

    Owner findByLastName(String lastName);
}
