package com.webcont.lumen_project_1.repository;

import com.webcont.lumen_project_1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    List<User> findByRole(User.Role role);
    
    @Query("SELECT u FROM User u WHERE u.role = 'USER' ORDER BY u.createdAt DESC")
    List<User> findAllUsersOrderByCreatedDate();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'USER'")
    Long countTotalUsers();
    
    @Query("SELECT u FROM User u WHERE u.name LIKE %?1% OR u.email LIKE %?1%")
    List<User> searchUsersByNameOrEmail(String searchTerm);
}
