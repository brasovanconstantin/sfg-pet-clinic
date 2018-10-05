package guru.springframework.sfgpetclinic.service;

import java.util.Set;

public interface PetClinicMainService<T> {

    T findById(Long id);

    T save(T type);

    Set<T> findAll();
}
