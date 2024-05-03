package ru.practicum.ewm.users.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.users.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

}
