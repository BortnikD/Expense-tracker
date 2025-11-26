//package com.bortnik.expensetracker.service;
//
//import com.bortnik.expensetracker.dto.user.UserCreateDTO;
//import com.bortnik.expensetracker.dto.user.UserDTO;
//import com.bortnik.expensetracker.entities.User;
//import com.bortnik.expensetracker.exceptions.user.UserAlreadyExists;
//import com.bortnik.expensetracker.exceptions.user.UserNotFound;
//import com.bortnik.expensetracker.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public class UserServiceTests {
//    private final UserRepository userRepository = mock(UserRepository.class);
//    private final UserService userService = new UserService(userRepository);
//
//    private final UUID uuid = UUID.randomUUID();
//    private final String username = "username";
//    private final String email = "email@gmail.com";
//
//    private final UserCreateDTO createUserDTO = UserCreateDTO.builder()
//            .username(username)
//            .email(email)
//            .build();
//
//    private final UserDTO userDTO = UserDTO.builder()
//            .id(uuid)
//            .username(username)
//            .email(email)
//            .build();
//
//    private final User user = User.builder()
//            .id(uuid)
//            .username(username)
//            .email(email)
//            .password("password")
//            .build();
//
//    @Test
//    void createUser_ShouldCreateUser() {
//        when(userRepository.save(any())).thenReturn(user);
//        final UserDTO createdUser = userService.saveUser(createUserDTO);
//        assertEquals(userDTO, createdUser);
//    }
//
//    @Test
//    void createUser_shouldThrowExceptionIfUserAlreadyExists_byUsername() {
//        when(userRepository.existsByUsername(username)).thenReturn(true);
//        final var exception = assertThrows(UserAlreadyExists.class, () -> userService.saveUser(createUserDTO));
//        assertEquals("user with username = " + username + " already exists", exception.getMessage());
//    }
//
//    @Test
//    void createUser_shouldThrowExceptionIfUserAlreadyExists_byEmail() {
//        when(userRepository.existsByEmail(email)).thenReturn(true);
//        final var exception = assertThrows(UserAlreadyExists.class,
//                () -> userService.saveUser(createUserDTO));
//        assertEquals("user with email = " + email + " already exists", exception.getMessage());
//    }
//
//    @Test
//    void getUserById_shouldReturnUser() {
//        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));
//        final UserDTO foundUser = userService.getUserById(uuid);
//        assertEquals(userDTO, foundUser);
//    }
//
//    @Test
//    void getUserById_shouldThrowExceptionIfUserNotFound() {
//        when(userRepository.findById(uuid)).thenReturn(Optional.empty());
//        final var exception = assertThrows(UserNotFound.class, () -> userService.getUserById(uuid));
//        assertEquals("User with id = " + uuid + " does not exist", exception.getMessage());
//    }
//
//    @Test
//    void getUserByUsername_shouldReturnUser() {
//        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
//        final UserDTO foundUser = userService.getUserByUsername(username);
//        assertEquals(userDTO, foundUser);
//    }
//
//    @Test
//    void getUserById_shouldThrowExceptionIfUserNotFound_byUsername() {
//        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
//        final var exception = assertThrows(UserNotFound.class, () -> userService.getUserByUsername(username));
//        assertEquals("User with username = " + username + " does not exist", exception.getMessage());
//    }
//
//    @Test
//    void getUserByEmail_shouldReturnUser() {
//        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
//        final UserDTO foundUser = userService.getUserByEmail(email);
//        assertEquals(userDTO, foundUser);
//    }
//
//    @Test
//    void getUserById_shouldThrowExceptionIfUserNotFound_byEmail() {
//        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
//        final var exception = assertThrows(UserNotFound.class, () -> userService.getUserByEmail(email));
//        assertEquals("User with email = " + email + " does not exist", exception.getMessage());
//    }
//}
