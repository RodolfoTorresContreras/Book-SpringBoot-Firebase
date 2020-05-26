package com.firebase.firebase.service;

import com.firebase.firebase.Comentario;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import java.util.concurrent.ExecutionException;
import org.springframework.stereotype.Service;

/**
 *
 * @author rodot
 */
@Service
public class FirebaseServiceComentario {

    public String saveComentDetails(Comentario message) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> future = db.collection("comentario").document().create(message);
        return future.get().getUpdateTime().toString();
    }

    public Comentario getComentDetails(String name) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("comentario").document(name);
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
        // block on response
        DocumentSnapshot document = future.get();
        Comentario person = null;
        if (document.exists()) {
            person = document.toObject(Comentario.class);

            return person;
        } else {
            return null;
        }
    }

    public String updateComentDetails(Comentario message) throws InterruptedException, ExecutionException {

        Firestore db = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> future = db.collection("comentario").document(message.getIdUser()).set(message);

        ApiFuture<WriteResult> writeResult = db.collection("comentario").document(message.getIdUser()).delete();
        return message.getIdUser();
    }

    public String deleteComent(String name) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection("comentario").document(name).delete();
        return writeResult.get().getUpdateTime().toString();
    }

}
