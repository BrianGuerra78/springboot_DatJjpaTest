package org.bguerra.test.springboot.app;

import org.bguerra.test.springboot.app.models.Cuenta;
import org.bguerra.test.springboot.app.repositories.CuentaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class IntegracionJpaTest {

    @Autowired
    CuentaRepository cuentaRepository;

    @Test
    void testFindById() {
        Optional<Cuenta> cuenta = cuentaRepository.findById(1L);
        assertTrue(cuenta.isPresent());
        assertEquals("Brian", cuenta.orElseThrow().getPersona());
    }

    @Test
    void testFindByPersona() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Brian");
        assertTrue(cuenta.isPresent());
        assertEquals("Brian", cuenta.orElseThrow().getPersona());
        assertEquals("1000.00", cuenta.orElseThrow().getSaldo().toPlainString());
    }

    @Test
    void testFindByPersonaThrowException() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Rod");
        /*assertThrows(NoSuchFieldError.class, () -> {
            cuenta.orElseThrow();
        });*/
        assertThrows(NoSuchElementException.class, cuenta::orElseThrow);
        //assertTrue(cuenta.isPresent());
        assertFalse(cuenta.isPresent());
    }

    @Test
    void testFindAll() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        assertFalse(cuentas.isEmpty());
        assertEquals(2, cuentas.size());
    }

    @Test
    void testSave() {
        //Given
        Cuenta cuentaPepe = new Cuenta(null, "Pepe", new BigDecimal("3000"));
        //cuentaRepository.save(cuentaPepe);
        //Cuenta cuentaSave = cuentaRepository.save(cuentaPepe);

        //When
        Cuenta cuenta = cuentaRepository.save(cuentaPepe);
        //Cuenta cuenta = cuentaRepository.findByPersona("Pepe").orElseThrow();
        //Cuenta cuenta = cuentaRepository.findById(cuentaSave.getId()).orElseThrow();

        //Then
        assertEquals("Pepe", cuenta.getPersona());
        assertEquals("3000", cuenta.getSaldo().toPlainString());
        //assertEquals(3, cuenta.getId());
    }

    @Test
    void testUpdate() {
        //Given
        Cuenta cuentaPepe = new Cuenta(null, "Pepe", new BigDecimal("3000"));

        //When
        Cuenta cuenta = cuentaRepository.save(cuentaPepe);

        //Then
        assertEquals("Pepe", cuenta.getPersona());
        assertEquals("3000", cuenta.getSaldo().toPlainString());

        //When
        cuenta.setSaldo(new BigDecimal("3800"));
        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);

        //Then
        assertEquals("Pepe", cuentaActualizada.getPersona());
        assertEquals("3800", cuentaActualizada.getSaldo().toPlainString());
    }

    @Test
    void testDelete() {
        Cuenta cuenta = cuentaRepository.findById(2L).orElseThrow();
        assertEquals("John", cuenta.getPersona());

        cuentaRepository.delete(cuenta);

        assertThrows(NoSuchElementException.class, () -> {
            //cuentaRepository.findByPersona("John").orElseThrow();
            cuentaRepository.findById(2L).orElseThrow();
        });
        assertEquals(1, cuentaRepository.findAll().size());
    }
}
