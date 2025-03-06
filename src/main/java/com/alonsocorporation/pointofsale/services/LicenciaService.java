package com.alonsocorporation.pointofsale.services;

import com.alonsocorporation.pointofsale.entities.Licencia;
import com.alonsocorporation.pointofsale.repositories.LicenciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LicenciaService {

    private final LicenciaRepository licenciaRepository;
    private final RestTemplate restTemplate;

    public LicenciaService(LicenciaRepository licenciaRepository) {
        this.licenciaRepository = licenciaRepository;
        this.restTemplate = new RestTemplate();
    }

    public String activarLicencia(String claveLicencia) {
        // Hacer la petición al servicio de licencias
        String url = "http://10.0.0.3:8080/licencias/activar?claveLicencia=" + claveLicencia;
        String resultado = restTemplate.postForObject(url, null, String.class);

        if ("true".equals(resultado)) {
            Licencia licencia = new Licencia();
            licencia.setClaveLicencia(claveLicencia);
            licencia.setEstadoActivacion("activada");
            licenciaRepository.save(licencia);
            return "Licencia activada correctamente";
        } else if ("duplicada".equals(resultado)) {
            return "La licencia ya está activada";
        } else {
            return "Clave de licencia inválida";
        }
    }

    public boolean verificarActivacion() {
        Licencia licencia = licenciaRepository.findAll().stream()
                .findFirst()
                .orElse(null);
        return licencia != null && "activada".equals(licencia.getEstadoActivacion());
    }
}
