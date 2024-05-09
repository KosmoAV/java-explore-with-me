package ru.practicum.ewm.users.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.users.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

}
