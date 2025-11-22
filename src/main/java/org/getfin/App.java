package org.getfin;

import org.getfin.controlador.CultivoController;
import org.getfin.modelos.Cultivo;
import org.getfin.modelos.enums.CategoriaCultivo;
import org.getfin.modelos.enums.EstadoCultivo;
import org.getfin.vistas.Login.loginVista;
import org.getfin.vistas.contenidoPrincipal.ventanaPrincipal;

import java.math.BigDecimal;
import java.time.LocalDate;

public class App {

    public static void main(String[] args) {


        Cultivo cultivoMaiz = new Cultivo(
                "Maíz Amarillo",                       // nombreCultivo
                LocalDate.of(2025, 5, 10),             // fechaSiembra
                LocalDate.of(2025, 9, 25),             // fechaCosecha
                new BigDecimal("2.5"),                 // área sembrada en manzanas o hectáreas
                CategoriaCultivo.GRANOS_BASICOS,       // categoría
                EstadoCultivo.CRECIMIENTO,             // estado
                new BigDecimal("1500.00"),             // stock disponible en kg
                new BigDecimal("8500.00")              // monto inicial invertido
        );
        CultivoController.getInstance().guardarCultivo(cultivoMaiz);

        new loginVista();


    }

}
