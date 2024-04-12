package ru.inno;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Repo extends JpaRepository<Users, Integer> {
    public Users findByUsername(String name);
    public boolean  existsByUsername(String name);
}
