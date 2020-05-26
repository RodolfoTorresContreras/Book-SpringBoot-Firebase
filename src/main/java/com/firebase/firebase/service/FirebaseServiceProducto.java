package com.firebase.firebase.service;

import com.firebase.firebase.Producto;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import java.util.concurrent.ExecutionException;
import org.springframework.stereotype.Service;

@Service
public class FirebaseServiceProducto {

    public String saveProductDetails(Producto message) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> future = db.collection("libro").document().create(message);
        return future.get().getUpdateTime().toString();
    }

    public Producto getProductoDetails(String name) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("libro").document(name);
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
        // block on response
        DocumentSnapshot document = future.get();
        Producto person = null;
        if (document.exists()) {

            person = document.toObject(Producto.class);
            System.out.println("Name; " + name);

            person.setId(name);
            System.out.println("Person; " + person.getId());
            System.out.println("");
            return person;
        } else {
            return null;
        }
    }

    public String updateProductDetails(Producto message, String id) throws InterruptedException, ExecutionException {

        Firestore db = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> future = db.collection("libro").document(message.getId()).set(message);

        ApiFuture<WriteResult> writeResult = db.collection("libro").document(id).delete();
        return message.getId();
    }

    public String deleteProduct(String name) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection("libro").document(name).delete();
        return writeResult.get().getUpdateTime().toString();
    }

    public Producto BuscarProductName(String username) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();

        CollectionReference cities = db.collection("libro");
        Query query = cities.select("genero", "nombre", "autor", "idioma", "id",
                "descripcion", "condicion", "precioInicial", "img", "precioPuja", "persona");
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        Producto persona = null;
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {

            persona = document.toObject(Producto.class);

        }

        return persona;
    }

}
