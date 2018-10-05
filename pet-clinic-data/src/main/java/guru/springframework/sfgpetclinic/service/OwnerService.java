package guru.springframework.sfgpetclinic.service;

import guru.springframework.sfgpetclinic.model.Owner;

public interface OwnerService extends BaseCrudService<Owner, Long> {

    Owner findByLastName(String lastName);
}
