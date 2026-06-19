package com.proyecto;

import com.proyecto.entity.*;
import com.proyecto.repository.*;
import com.proyecto.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    private User estudiante;

    @BeforeEach
    void setUp() {
        estudiante = User.builder()
                .id(1L).fullName("Juan Pérez")
                .email("juan@proyecto.com").password("pass")
                .active(true).codigoEstudiante("2021012345")
                .roles(Set.of()).build();
    }

    @Test
    @DisplayName("Buscar estudiante con query válida → retorna coincidencias")
    void searchStudents_validQuery_shouldReturnResults() {
        when(userRepository.searchStudents("Perez", Role.RoleName.ROLE_ESTUDIANTE))
                .thenReturn(List.of(estudiante));

        List<User> result = userService.searchStudents("Perez");

        assertEquals(1, result.size());
        assertEquals("Juan Pérez", result.get(0).getFullName());
    }

    @Test
    @DisplayName("Buscar estudiante con query vacía → lanza excepción")
    void searchStudents_emptyQuery_shouldThrow() {
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.searchStudents("   "));
        assertTrue(ex.getMessage().contains("código o apellido"));
    }

    @Test
    @DisplayName("Buscar estudiante con query null → lanza excepción")
    void searchStudents_nullQuery_shouldThrow() {
        assertThrows(RuntimeException.class,
                () -> userService.searchStudents(null));
    }

    @Test
    @DisplayName("Buscar código inexistente → retorna lista vacía")
    void searchStudents_noMatches_shouldReturnEmptyList() {
        when(userRepository.searchStudents("99999999", Role.RoleName.ROLE_ESTUDIANTE))
                .thenReturn(List.of());

        List<User> result = userService.searchStudents("99999999");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Asignar código de estudiante válido y libre → éxito")
    void setCodigoEstudiante_validAndFree_shouldSucceed() {
        when(userRepository.findByCodigoEstudiante("2021099999"))
                .thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.setCodigoEstudiante(1L, "2021099999");

        assertEquals("2021099999", result.getCodigoEstudiante());
    }

    @Test
    @DisplayName("Asignar código vacío → lanza excepción")
    void setCodigoEstudiante_blank_shouldThrow() {
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.setCodigoEstudiante(1L, "  "));
        assertTrue(ex.getMessage().contains("vacío"));
    }

    @Test
    @DisplayName("Asignar código ya usado por otro usuario → lanza excepción")
    void setCodigoEstudiante_alreadyTakenByOther_shouldThrow() {
        User otroUsuario = User.builder()
                .id(2L).fullName("Otro Usuario")
                .codigoEstudiante("2021012345").build();

        when(userRepository.findByCodigoEstudiante("2021012345"))
                .thenReturn(Optional.of(otroUsuario));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.setCodigoEstudiante(1L, "2021012345"));
        assertTrue(ex.getMessage().contains("ya está asignado"));
    }

    @Test
    @DisplayName("Reasignar el mismo código al mismo usuario → no lanza excepción")
    void setCodigoEstudiante_sameCodeSameUser_shouldSucceed() {
        when(userRepository.findByCodigoEstudiante("2021012345"))
                .thenReturn(Optional.of(estudiante));
        when(userRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.setCodigoEstudiante(1L, "2021012345");

        assertEquals("2021012345", result.getCodigoEstudiante());
    }
}