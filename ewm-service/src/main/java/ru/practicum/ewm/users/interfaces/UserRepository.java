package ru.practicum.ewm.users.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.users.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
