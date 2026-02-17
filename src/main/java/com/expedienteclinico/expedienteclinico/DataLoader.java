package com.expedienteclinico.expedienteclinico;

import com.expedienteclinico.expedienteclinico.models.*;
import com.expedienteclinico.expedienteclinico.repositories.*;
import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final IArticulos_textilesRepository iArticulos_textilesRepository;

    private final IEmpleados_LyRRepository iEmpleados_LyRRepository;

    private final IMovimientos_insumosRepository iMovimientos_insumosRepository;

    private final IMovimientos_textilesRepository iMovimientos_textilesRepository;

    private final IInsumo_limpiezaRepository iInsumoLimpiezaRepository;

    public DataLoader(IArticulos_textilesRepository iArticulos_textilesRepository,
                      IEmpleados_LyRRepository iEmpleados_LyRRepository,
                      IInsumo_limpiezaRepository iInsumo_LimpiezaRepository,
                      IMovimientos_insumosRepository iMovimientos_insumosRepository,
                      IMovimientos_textilesRepository iMovimientos_textilesRepository){


        this.iArticulos_textilesRepository = iArticulos_textilesRepository;

        this.iEmpleados_LyRRepository = iEmpleados_LyRRepository;

        this.iMovimientos_insumosRepository = iMovimientos_insumosRepository;

        this.iMovimientos_textilesRepository = iMovimientos_textilesRepository;

        this.iInsumoLimpiezaRepository = iInsumo_LimpiezaRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (iArticulos_textilesRepository.count() == 0) {
            Articulos_textilesModel articulo = new Articulos_textilesModel();
            articulo.setNombre("Batas");
            articulo.setDescripcion("Blancas con logotipo de la clinica ");


            iArticulos_textilesRepository.save(articulo);
        }

        if (iMovimientos_textilesRepository.count() == 0) {
            Movimientos_textilesModel movimientot = new Movimientos_textilesModel();
            movimientot.setTipo_movimiento("Entrada");
            movimientot.setCantidad(50);
            movimientot.setFecha("12/10/2026");
            movimientot.setObservaciones("En perfectas condiciones");


            iMovimientos_textilesRepository.save(movimientot);
        }


        if (iEmpleados_LyRRepository.count() == 0 ){
            Empleados_LyRModel empleado = new Empleados_LyRModel();
            empleado.setNombre("Juan");
            empleado.setApellido("Perez");
            empleado.setArea("Limpieza");
            empleado.setTurno("Matutino");
            empleado.setTelefono(998241989);
            empleado.setEstatus("Activo");

            /* empleado.setNombre("Pedro");
             empleado.setApellido("Santos");
            empleado.setArea("Roperia");
            empleado.setTurno("Vespertino");
            empleado.setTelefono(998240605);
            empleado.setEstatus("Activo"); */


            iEmpleados_LyRRepository.save(empleado);
        }




        if (iInsumoLimpiezaRepository.count() == 0){
            Insumo_limpiezaModel insumo = new Insumo_limpiezaModel();

            insumo.setNombre("Cloro");
            insumo.setStock_actual(50);
            insumo.setStock_minimo(20);
            insumo.setUnidad_medida("ml");
            insumo.setFecha_caducidad("12/10/2026");
            insumo.setEstado("activo");

            iInsumoLimpiezaRepository.save(insumo);
        }

        if (iMovimientos_insumosRepository.count() == 0){
            Movimientos_insumosModel movimientoi = new Movimientos_insumosModel();

            movimientoi.setTipo_movimiento("Entrada");
            movimientoi.setCantidad (10 );
            movimientoi.setFecha("02/10/2026");
            movimientoi.setObservaciones("Llegaron en buen estado");

            iMovimientos_insumosRepository.save(movimientoi);
        }

        System.out.println("Datos insertados Correctamente");
    }
}
