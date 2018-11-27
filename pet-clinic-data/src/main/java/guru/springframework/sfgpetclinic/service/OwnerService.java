package guru.springframework.sfgpetclinic.service;

import guru.springframework.sfgpetclinic.model.Owner;

import java.util.List;

public interface OwnerService extends BaseCrudService<Owner, Long> {

    Owner findByLastName(String lastName);

    List<Owner> findAllByLastName(String lastName);
}
