package com.firebase.firebase;

import com.firebase.firebase.service.FirebaseServiceComentario;
import com.firebase.firebase.service.FirebaseServiceProducto;
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
import java.text.ParseException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.support.SessionStatus;

@Controller
public class ProductoController {

    protected final Log logger = LogFactory.getLog(this.getClass());
    @Autowired
    private FirebaseServiceComentario comen;
    @Autowired
    private FirebaseServiceProducto firebaseServices;

    @RequestMapping(value = {"/listar", "/"}, method = RequestMethod.GET)
    public String listar(Model model
    ) throws InterruptedException, ExecutionException {

        ArrayList<Producto> productList = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference cities = db.collection("libro");
        Query query = cities.select("nombre", "descripcion", "duracion");
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        Producto p = null;
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {

            p = firebaseServices.getProductoDetails(document.getId());
            p.setId(document.getId());
            productList.add(p);

        }

        model.addAttribute("libros", productList);

        model.addAttribute("titulo", "Listado de Libros");
        return "listar";

    }

    @RequestMapping(value = "/mislibros")
    public String Libros(Model model, Principal principal) throws InterruptedException, ExecutionException {

        try {
            final String loggedInUserName = principal.getName();

            ArrayList<Producto> productList = new ArrayList<>();
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference cities = db.collection("libro");
            Query query = cities.select("nombre", "descripcion", "duracion",
                    "persona", "autor", "idioma", "img", "condicion", "precioInicial",
                    "precioPuja");
            ApiFuture<QuerySnapshot> querySnapshot = query.get();

            Producto p = null;
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {

                p = firebaseServices.getProductoDetails(document.getId());

                if (loggedInUserName.equals(p.getPersona())) {
                    p.setId(document.getId());
                    productList.add(p);

                }

            }
            System.out.println(productList);
            model.addAttribute("persona", loggedInUserName);
            model.addAttribute("libros", productList);

        } catch (Exception ex) {
            ex.printStackTrace();
            final String loggedInUserName = "";
            model.addAttribute("persona", loggedInUserName);
        }

        return "mislibros";
    }

    @RequestMapping(value = "/form")
    public String form(Map<String, Object> modelo) throws InterruptedException, ExecutionException, IOException {

        Producto producto = new Producto();
        modelo.put("libro", producto);
        modelo.put("titulo", "Formulario de Libro");
        return "form";
    }

    @GetMapping(value = "/ver/{name}")
    public String ver(@PathVariable(value = "name") String name, Map<String, Object> model, Model modelo, RedirectAttributes flash) throws InterruptedException, ExecutionException, ParseException {

        Producto producto = firebaseServices.getProductoDetails(name);
        if (producto == null) {

            return "redirect:listar";
        }

        model.put("libro", producto);
        Date fecha2 = producto.getDuracion();
        Date actual = new Date();
        if (actual.before(fecha2)) {

            model.put("bandera", true);
        } else if (actual.after(fecha2) ) {
            model.put("bandera", false);
        }

        ArrayList<Comentario> coen = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference cities = db.collection("comentario");
        Query query = cities.select("id", "idUser", "nameUser", "libro");
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        Comentario p = null;
        float x, aux = 0;
        Comentario c = new Comentario();
        producto.setPrecioPuja(producto.getPrecioInicial());
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            p = comen.getComentDetails(document.getId());
            p.setId(document.getId());
            if (name.equals(p.getLibro())) {
                x = Float.parseFloat(p.getOfertaLibro());
                if (x > aux) {
                    aux = x;
                    producto.setPrecioPuja(Float.toString(aux));
                    c = p;
                    
                }

            }

        }

        model.put("titulo", "Detalle Libro");
        model.put("comentarios", c);
        model.put("libro", producto);
        return "ver";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(@Valid Producto producto, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status,
            @RequestParam("file") MultipartFile foto, Principal principal) throws InterruptedException, ExecutionException {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de Cliente");
            return "form";
        }

        final String loggedInUserName = principal.getName();

        if (!foto.isEmpty()) {

            String uniqueFileName = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();
            Path rootPath = Paths.get("img").resolve(uniqueFileName);

            Path rootAbsolute = rootPath.toAbsolutePath();
            try {
                if (!foto.isEmpty()) {

                    Files.copy(foto.getInputStream(), rootAbsolute);
                    producto.setImg(uniqueFileName);

                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        producto.setPersona(loggedInUserName);
        firebaseServices.saveProductDetails(producto);

        status.setComplete();
        flash.addFlashAttribute("success", "El libro se ha guardado con exito.");
        return "redirect:listar";
    }

    @RequestMapping(value = "/form/{name}")
    public String update(@PathVariable(value = "name") String name, Model model, RedirectAttributes flash) throws InterruptedException, ExecutionException {

        Producto producto = null;
        if (!name.isEmpty()) {

            String temp;

            producto = firebaseServices.getProductoDetails(name);

            String id = name;
            temp = firebaseServices.updateProductDetails(producto, id);

            producto = firebaseServices.getProductoDetails(temp);
            firebaseServices.deleteProduct(name);
            model.addAttribute("libro", producto);
            model.addAttribute("titulo", "Editar Cliente");
        } else {
            flash.addFlashAttribute("error", "El libro no existe.");
            return "redirect:/listar";
        }

        return "form";

    }

    @RequestMapping(value = "/delete/{name}")
    public String deleteUser(@PathVariable(value = "name") String name, RedirectAttributes flash) throws InterruptedException, ExecutionException {
        if (!name.isEmpty()) {

            firebaseServices.deleteProduct(name);
            flash.addFlashAttribute("success", "El libro se ha eliminado con exito.");
        }

        return "redirect:/listar";
    }

}
