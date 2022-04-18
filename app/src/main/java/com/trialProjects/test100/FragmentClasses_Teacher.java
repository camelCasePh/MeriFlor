package com.trialProjects.test100;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.trialProjects.test100.activities.Registration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FragmentClasses_Teacher extends Fragment{

    //widgets
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText className, classSection,accessCode;
    private Button btn_create, btn_cancel;
    private FloatingActionButton fab;

    private FirebaseAuth app_auth;
    private FirebaseFirestore app_fireStore = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser;
    private CollectionReference classesRef = app_fireStore.collection("CLASSES");
    private AddClassesAdapter adapter;
    private View view;
    private DocumentSnapshot classDocSnapshot;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_classes_teacher, container, false);



        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createClass();
            }
        });

        getClassList();
        return view;
    }

    private void getClassList(){
        app_fireStore = FirebaseFirestore.getInstance();

        CollectionReference classRef = app_fireStore.collection("CLASSES");
        Query classQuery = classRef
                    .whereEqualTo("teacherID", FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .orderBy("className", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<AddClasses> options = new FirestoreRecyclerOptions.Builder<AddClasses>().setQuery(classQuery, AddClasses.class).build();
        adapter = new AddClassesAdapter(options);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void addNewClasses(String className, String classSection) {

        DbQuery.createClass(className, classSection, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Created New Class", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Created New Classsss");
            }
            @Override
            public void onFailure() {
                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createClass(){
        dialogBuilder = new AlertDialog.Builder(getContext());
        View createClassView = getLayoutInflater().inflate(R.layout.pop_up_window_create, null);

        className = createClassView.findViewById(R.id.et_class_name);
        classSection = createClassView.findViewById(R.id.et_class_section);
        btn_create = createClassView.findViewById(R.id.btn_create);
        btn_cancel = createClassView.findViewById(R.id.btn_cancel);

        dialogBuilder.setView(createClassView);
        dialog = dialogBuilder.create();
        dialog.show();

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name= className.getText().toString().trim();
                String section= classSection.getText().toString().trim();

                if(name.isEmpty()||section.isEmpty())
                {
                    Toast.makeText(getContext(),"Please enter the class name and section",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    addNewClasses(name,section);
                    dialog.dismiss();
                }

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
