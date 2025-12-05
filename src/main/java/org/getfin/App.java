package org.getfin;

import org.getfin.controlador.AnimalController;
import org.getfin.controlador.CultivoController;
import org.getfin.controlador.UsuarioController;
import org.getfin.modelos.Animal;
import org.getfin.modelos.Cultivo;
import org.getfin.modelos.Usuario;
import org.getfin.modelos.enums.*;
import org.getfin.vistas.Login.loginVista;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {

        Usuario userAd = new Usuario("axel","123", Rol.ADMINISTRADOR);
        List<Usuario> usuarios = UsuarioController.getInstance().getClientes();

        if (!usuarios.contains(userAd)) {
            UsuarioController.getInstance().guardarCliente(userAd);
            new loginVista();
        }

//        Cultivo cultivoMaiz = new Cultivo(
//                "Maíz Amarillo",                       // nombreCultivo
//                LocalDate.of(2025, 5, 10),             // fechaSiembra
//                LocalDate.of(2025, 9, 25),             // fechaCosecha
//                new String("2.5"),                 // área sembrada en manzanas o hectáreas
//                CategoriaCultivo.GRANOS_BASICOS,       // categoría
//                EstadoCultivo.CRECIMIENTO,             // estado
//                new BigDecimal("1500.00"),             // stock disponible en kg
//                new BigDecimal("8500.00")
//        );
//        CultivoController.getInstance().guardarCultivo(cultivoMaiz);
//
//        Animal animalVaca = new Animal(
//                "panda",
//                "a3ds",
//                TipoAnimal.BOVINOS,
//                1,
//                new BigDecimal("300.0"),
//                "",
//                EstadoAnimal.DISPONIBLE
//        );
//        AnimalController.getInstance().guardarAnimal(animalVaca);

    }

}
