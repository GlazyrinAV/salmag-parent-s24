package ru.avg.managerapp.repository;

import org.springframework.data.repository.CrudRepository;
import ru.avg.managerapp.entity.SelmagUser;

import java.util.Optional;

public interface SelmagUserRepository extends CrudRepository<SelmagUser, Integer> {

    Optional<SelmagUser> findByUsername(String username);
}
