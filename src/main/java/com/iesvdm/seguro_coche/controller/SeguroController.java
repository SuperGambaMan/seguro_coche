package com.iesvdm.seguro_coche.controller;

import com.iesvdm.seguro_coche.model.CotizacionSeguro;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller

@RequestMapping("/seguros")
public class SeguroController {

    @GetMapping("/calculos/cotizacion/paso1")
    public String paso1 (Model model, HttpSession httpSession){

        //Hacemos una sesion httpSession para mantener los datos entre peticiones
        var cotizacionSeguroHttpSession = (CotizacionSeguro) httpSession.getAttribute("cotizacionSeguro");

        if (cotizacionSeguroHttpSession != null){
            model.addAttribute("cotizacionSeguro", cotizacionSeguroHttpSession);
        }   else{
            model.addAttribute("cotizacionSeguro", new CotizacionSeguro());
        }

        //Pedimos un objeto vacio para rellenar el formulario
        model.addAttribute("cotizacionSeguro", new CotizacionSeguro() );

        return "paso1";
    }

    @GetMapping("/calculos/cotizacion/paso2" )
    public String anteriorPaso2 (Model model, HttpSession httpSession){

        var cotizacionSeguroHttpSession = (CotizacionSeguro) httpSession.getAttribute("cotizacionSeguro");

        model.addAttribute("cotizacionSeguro", cotizacionSeguroHttpSession);

        return "paso2";
    }


    @PostMapping("/calculos/cotizacion/paso2" )
    public String paso1Post (Model model, @ModelAttribute CotizacionSeguro cotizacionSeguro, HttpSession httpSession){

        // Recuerda que podrias utilizar un mecanismo mas abastracto mediante @SessionAttributes
        httpSession.setAttribute("cotizacionSeguro", cotizacionSeguro);

        //Entre peticiones consecutivas se mantiene el mapa de httpSession<String, Object>
        model.addAttribute("cotizacionSeguro", cotizacionSeguro);

        return "paso2";
    }

    @PostMapping("/calculos/cotizacion/paso3" )
    public String paso2Post (Model model, @ModelAttribute CotizacionSeguro cotizacionSeguro, HttpSession httpSession){

        var cotizacionSeguroHttpSession = (CotizacionSeguro) httpSession.getAttribute("cotizacionSeguro");

        if(cotizacionSeguro.getTipoCobertura()==null){
            cotizacionSeguroHttpSession.setTipoCobertura(cotizacionSeguro.getTipoCobertura());
            cotizacionSeguroHttpSession.setAsistencia(cotizacionSeguro.isAsistencia());
            cotizacionSeguroHttpSession.setVehSustitucion(cotizacionSeguro.isVehSustitucion());
        }

        cotizacionSeguroHttpSession.setMarca(cotizacionSeguro.getMarca());
        cotizacionSeguroHttpSession.setModelo(cotizacionSeguro.getModelo());
        cotizacionSeguroHttpSession.setAnioMat(cotizacionSeguro.getAnioMat());
        cotizacionSeguroHttpSession.setUso(cotizacionSeguro.getUso());

        // Recuerda que podrias utilizar un mecanismo mas abastracto mediante @SessionAttributes
        httpSession.setAttribute("cotizacionSeguro", cotizacionSeguro);

        //Entre peticiones consecutivas se mantiene el mapa de httpSession<String, Object>
        model.addAttribute("cotizacionSeguro", cotizacionSeguroHttpSession);

        //Calculamos los datos de la cotizacion
        model.addAttribute("datosConductor", cotizacionSeguroHttpSession.getNombre()
                + " | " + cotizacionSeguroHttpSession.getEdad()
                + " | " + cotizacionSeguroHttpSession.getAnioCarnet());
        model.addAttribute("datosVehiculo", cotizacionSeguroHttpSession.getMarca()
                + " | " + cotizacionSeguroHttpSession.getModelo()
                + " | " + cotizacionSeguroHttpSession.getAnioMat()
                + " | " + cotizacionSeguroHttpSession.getUso());

        if (cotizacionSeguro.getTipoCobertura() != null) {
            cotizacionSeguroHttpSession.setTipoCobertura(cotizacionSeguro.getTipoCobertura());
            cotizacionSeguroHttpSession.setAsistencia(cotizacionSeguro.isAsistencia());
            cotizacionSeguroHttpSession.setVehSustitucion(cotizacionSeguro.isVehSustitucion());

            model.addAttribute("datosCobertura", cotizacionSeguroHttpSession.getTipoCobertura()
                    + " | " + cotizacionSeguroHttpSession.isAsistencia()
                    + " | " + cotizacionSeguroHttpSession.isVehSustitucion());
        }


        return "paso3";
    }



}
