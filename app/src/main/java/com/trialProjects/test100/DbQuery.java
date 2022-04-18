package com.trialProjects.test100;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class DbQuery {

    public static FirebaseFirestore app_fireStore;
    DocumentSnapshot lastAddedClass;


    public static void createStudentData(String email, String fullName, String schoolId, MyCompleteListener completeListener){

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userDoc = app_fireStore.collection("USERS").document(userID);
        Map<String, Object> userData = new HashMap<>();
        userData.put("EMAIL", email);
        userData.put("FULL_NAME", fullName);
        userData.put("SCHOOL_ID", schoolId);
        userData.put("userType", "Student");

        userDoc.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                completeListener.onSuccess();
            }
        });
    }

    public static void createTeacherData(String email, String fullName, String schoolId, MyCompleteListener completeListener){
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userDoc = app_fireStore.collection("USERS").document(userID);
        Map<String, Object> userData = new HashMap<>();
        userData.put("EMAIL", email);
        userData.put("FULL_NAME", fullName);
        userData.put("SCHOOL_ID", schoolId);
        userData.put("userType", "Teacher");

        userDoc.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                completeListener.onSuccess();
            }
        });
    }

    public static void createClass(String name, String section, MyCompleteListener completeListener) {

        String teacherID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference classRef = app_fireStore
                .collection("CLASSES")
                .document();

        AddClasses classes = new AddClasses();
        classes.setClassName(name);
        classes.setClassID(classRef.getId());
        classes.setTeacherID(teacherID);
        classes.setClassSection(section);
        classes.setAccessCode(classRef.getId());

        classRef.set(classes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    completeListener.onSuccess();
                }else{
                    completeListener.onFailure();
                }
            }
        });
    }

    public static void checkClassExist(String classID, MyCompleteListener completeListener){
        app_fireStore = FirebaseFirestore.getInstance();
        DocumentReference classRef = app_fireStore.collection("CLASSES").document(classID);
        classRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    completeListener.onSuccess();
                }else{
                    completeListener.onFailure();
                }
            }
        });
    }

//    public static void checkStudentClassExist(String classID, MyCompleteListener completeListener){
//        app_fireStore = FirebaseFirestore.getInstance();
//        String studentClassID = classID;
//        String studentID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        CollectionReference classRef = app_fireStore.collection("STUDENTS");
//        classRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()){
//                    for(QueryDocumentSnapshot document: task.getResult()){
//                        String class_id = document.getString("classID");
//                        String class_studentID = document.getString("studentID");
//                        Log.d(TAG, class_id + " " + class_studentID);
//                        //Log.d(TAG, studentClassID + " " + classID);
//
//                        if(class_id.equals(studentClassID) && class_studentID.equals(studentID)){
//                            completeListener.onSuccess();
//                            return;
//                        }
//                    }
//                    completeListener.onFailure();
//                }
//            }
//        });
//    }

    public static void checkStudentClassExist(String classID, MyCompleteListener completeListener){
        app_fireStore = FirebaseFirestore.getInstance();
        String studentID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        CollectionReference classRef = app_fireStore.collection("STUDENTS");
        Query classQuery = classRef
                .whereEqualTo("studentID", studentID);

        classQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Log.d(TAG, document.getString("studentID"));
                        if(document.getString("classID").equals(classID)){
                            completeListener.onSuccess();
                            return;
                        }
                    }
                    completeListener.onFailure();
                }
            }
        });
    }

//    public static void joinClass(String classID, String studentID, MyCompleteListener completeListener){
//
//        DocumentReference classesRef = FirebaseFirestore.getInstance()
//                .collection("CLASSES")
//                .document(classID);
//        classesRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                String className = documentSnapshot.getString("className");
//                String classSection = documentSnapshot.getString("classSection");
//
//                DocumentReference classRef = app_fireStore.collection("STUDENTS")
//                        .document();
//
//                Map<String, Object> userData = new HashMap<>();
//                userData.put("classID", classID);
//                userData.put("studentID", studentID);
//                userData.put("className", className);
//                userData.put("classSection", classSection);
//
//                classRef.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        completeListener.onSuccess();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        completeListener.onFailure();
//                    }
//                });
//            }
//        });
//
//    }

    public static void joinClass(String classID, String studentID, MyCompleteListener completeListener){

        DocumentReference classesRef = FirebaseFirestore.getInstance()
                .collection("CLASSES")
                .document(classID);
        classesRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String className = documentSnapshot.getString("className");
                String classSection = documentSnapshot.getString("classSection");

                CollectionReference classRef = app_fireStore.collection("STUDENTS");

                classRef.add(new JoinClasses(classID, studentID, className, classSection));
                completeListener.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                completeListener.onFailure();
            }
        });

    }
}
