package com.firebase.firebase.service;

import com.firebase.firebase.Person;
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
public class FirebaseServiceUser {

    public String saveUserDetails(Person message) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> future = db.collection("usuario").document().create(message);
        return future.get().getUpdateTime().toString();
    }

    public Person getUserDetails(String name) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("usuario").document(name);

        ApiFuture<DocumentSnapshot> future = docRef.get();

        DocumentSnapshot document = future.get();
        Person person = null;
        if (document.exists()) {

            person = document.toObject(Person.class);
            System.out.println("Name; " + name);

            person.setId(name);
            System.out.println("Person; " + person.getId());
            System.out.println("");
            return person;
        } else {
            return null;
        }
    }

    public Person BuscarUserName(String username) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();

        CollectionReference cities = db.collection("usuario");
        Query query = cities.select("name", "password", "admin", "enable", "id",
                "age", "mail", "municipio", "img", "reputacion", "phone", "productos");
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        Person persona = null;
        Person person = null;
        int cont = 3;
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {

            persona = document.toObject(Person.class);

            if (persona.getName().equals(username)) {
                cont = 1;
                persona.setId(document.getId());
            } else {
                cont = 0;
            }
            if (cont == 1) {
                person = persona;
            }

        }

        return person;
    }

    public String updateUserDetails(Person message, String nom) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> future = db.collection("usuario").document(message.getId()).set(message);

        return message.getId();
    }

    public String deleteUser(String name) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection("usuario").document(name).delete();
        return writeResult.get().getUpdateTime().toString();
    }

}
