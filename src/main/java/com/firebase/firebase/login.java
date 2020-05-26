package com.firebase.firebase;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class login {

    @GetMapping("/login")
    public String login(@RequestParam(value = "logout", required = false) String lo,
            @RequestParam(value = "error", required = false) String error,
            Model model, Principal principal, RedirectAttributes flash) {
        if (principal != null) {
            flash.addFlashAttribute("info", "Ya ha iniciado sesión anteriormente");
            return "redirect:/";
        }
        if (error != null) {
            model.addAttribute("error", "Error en el login: Nombre de usuario o contraseña incorrecta, por favor vuelva a intentarlo!");

        }
        if (lo != null) {
            model.addAttribute("success", "Ha cerrado la sesión con éxito!");
        }

        return "login";
    }

}
