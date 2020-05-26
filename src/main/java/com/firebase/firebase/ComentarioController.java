package com.firebase.firebase;

import com.firebase.firebase.service.FirebaseServiceComentario;
import com.firebase.firebase.service.FirebaseServiceUser;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author rodot
 */
@Controller
public class ComentarioController {

    @Autowired
    private FirebaseServiceUser user;

    private String n;

    @Autowired
    private FirebaseServiceComentario comen;

    @RequestMapping(value = "/formOferta", method = RequestMethod.POST)
    public String oferta(@Valid Comentario comentario, Principal principal) throws InterruptedException, ExecutionException {

        Person persona = null;
        final String loggedInUserName = principal.getName();
        persona = user.BuscarUserName(loggedInUserName);

        comentario.setIdUser(persona.getId());
        comentario.setNameUser(persona.getName());
        comentario.setLibro(n);

        comen.saveComentDetails(comentario);
        return "redirect:mislibros";
    }

    @RequestMapping(value = "/formOferta/{name}")
    public String formOferta(Map<String, Object> modelo, Principal principal, @PathVariable(value = "name") String name) throws InterruptedException, ExecutionException, IOException {
        Comentario comentar = new Comentario();
        n = name;
        modelo.put("comentario", comentar);
        modelo.put("titulo", "Ofertas");
        return "formOferta";
    }

}
