package com.trialProjects.test100;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DbQuery {

    public static FirebaseFirestore app_fireStore;


    public static void createStudentData(String email, String fullName, String schoolId, MyCompleteListener completeListener){

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userDoc = app_fireStore.collection("USERS").document(userID);
        Map<String, Object> userData = new HashMap<>();
        userData.put("EMAIL", email);
        userData.put("FULL_NAME", fullName);
        userData.put("SCHOOL_ID", schoolId);
        userData.put("isStudent", "1");

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
        userData.put("isTeacher", "1");

        userDoc.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                completeListener.onSuccess();
            }
        });
    }
}
