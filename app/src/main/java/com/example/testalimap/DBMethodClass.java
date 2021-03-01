package com.example.testalimap;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DBMethodClass extends AppCompatActivity {

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    public void createEventDB(Events events){
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Events");

        reference.setValue(new Events(events.getEventType(), events.getLongitude(), events.getLatitude(), events.getDescription(), events.getCapacity()));
    }

}
