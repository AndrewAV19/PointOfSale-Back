package com.alonsocorporation.pointofsale.controllers;

import com.alonsocorporation.pointofsale.services.LicenciaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/licencia")
public class LicenciaController {

    private final LicenciaService licenciaService;

    public LicenciaController(LicenciaService licenciaService) {
        this.licenciaService = licenciaService;
    }

    @PostMapping("/activar")
    public String activarLicencia(@RequestParam String claveLicencia) {
        return licenciaService.activarLicencia(claveLicencia);
    }

    @GetMapping("/verificar")
    public boolean verificarActivacion() {
        return licenciaService.verificarActivacion();
    }
}
