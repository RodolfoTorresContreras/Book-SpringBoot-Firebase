package com.firebase.firebase;

import com.firebase.firebase.service.FirebaseServiceUser;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    private FirebaseServiceUser firebaseServices;

    @RequestMapping(value = "/micuenta", method = RequestMethod.GET)//Ver detalles cuenta
    public String getUserDetails(
            Model model, Principal principal) throws InterruptedException, ExecutionException {

        try {
            Person persona = null;
            final String loggedInUserName = principal.getName();
            persona = firebaseServices.BuscarUserName(loggedInUserName);
            model.addAttribute("person", persona);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "micuenta";
    }

    @RequestMapping(value = {"/listarUser"}, method = RequestMethod.GET)
    public String listarUser(Model model) throws InterruptedException, ExecutionException {
        ArrayList<Person> personaList = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference cities = db.collection("usuario");
        Query query = cities.select("name", "password", "admin", "enable", "id",
                "age", "mail", "municipio", "img", "reputacion", "phone", "productos");
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        Person p = null;
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {

            p = firebaseServices.getUserDetails(document.getId());
            p.setId(document.getId());
            personaList.add(p);

        }
        model.addAttribute("personas", personaList);
        model.addAttribute("titulo", "Listado de Usuarios");

        return "listarUser";
    }

    @RequestMapping(value = "/formUser")
    public String form(Map<String, Object> modelo) throws InterruptedException, ExecutionException, IOException {

        Person person = new Person();

        modelo.put("usuario", person);
        modelo.put("titulo", "Formulario de Usuario");
        return "formUser";
    }

    @RequestMapping(value = "/formUser", method = RequestMethod.POST)
    public String guardar(@Valid Person person, Model model, RedirectAttributes flash, SessionStatus status, @RequestParam("file") MultipartFile foto) throws InterruptedException, ExecutionException {

        if (!foto.isEmpty()) {
            String uniqueFileName = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();
            Path rootPath = Paths.get("img").resolve(uniqueFileName);

            Path rootAbsolute = rootPath.toAbsolutePath();

            try {
                if (!foto.isEmpty()) {

                    Files.copy(foto.getInputStream(), rootAbsolute);

                    person.setImg(uniqueFileName);

                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        firebaseServices.saveUserDetails(person);
        status.setComplete();
        flash.addFlashAttribute("success", "El usuario se ha guardado con exito.");
        return "redirect:listarUser";
    }

    @RequestMapping(value = "/formUser/{name}")
    public String update(@PathVariable(value = "name") String name, Model model, RedirectAttributes flash) throws InterruptedException, ExecutionException {

        Person person = firebaseServices.getUserDetails(name);
        System.out.println(person.getAdmin());
       

            if (!name.isEmpty()) {
                String temp;
                person = firebaseServices.getUserDetails(name);
                String nom = name;

                temp = firebaseServices.updateUserDetails(person, nom);

                person = firebaseServices.getUserDetails(temp);
                firebaseServices.deleteUser(name);
            } else {
                flash.addFlashAttribute("error", "El Usuario no existe.");
                return "redirect:/listar";
            }
        
        model.addAttribute("usuario", person);
        model.addAttribute("titulo", "Editar Usuario");

        return "formUser";

    }

    @RequestMapping("/userDelete/{id}")
    public String deleteUser(@PathVariable(value = "id") String name, RedirectAttributes flash) throws InterruptedException, ExecutionException {
        Person user = firebaseServices.getUserDetails(name);
        System.out.println(user.getAdmin());
        if (user.getAdmin() == true) {
            flash.addFlashAttribute("error", "No se pueden borrar USUARIOS ADMINISTRADORES.");
            return "redirect:/listarUser";
        } else {
            if (!name.isEmpty()) {
                firebaseServices.deleteUser(name);
                flash.addFlashAttribute("success", "El Usuario se ha eliminado con exito.");
            }

        }

        return "redirect:/listarUser";
    }
}
