package ru.unn.autorepairshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.unn.autorepairshop.domain.dto.response.UserGetAllResponseDto;
import ru.unn.autorepairshop.service.AdminService;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@RestController
public class AdminController {

    private final AdminService adminService;

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public UserGetAllResponseDto getAllUsers() {
        return adminService.getAllUsers();
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/users/{id}/grant-manager")
    public String grantManager(@PathVariable Long id) {
        return adminService.grantManager(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/users/{id}/revoke-manager")
    public String revokeManager(@PathVariable Long id) {
        return adminService.revokeManager(id);
    }

}
