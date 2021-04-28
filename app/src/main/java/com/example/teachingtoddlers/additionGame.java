package com.example.teachingtoddlers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class additionGame extends AppCompatActivity {
    Button levelOneButton,levelTwoButton,levelThreeButton;
    TextView goToHome;
    long levelOneTotalCorrect,levelTwoTotalCorrect, levelOneTotalquestions, levelTwoTotalquestions, levelThreeTotalCorrect, levelThreeTotalquestions;
    String Id, levelOneTotalCorrectStr;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    double currentHighPercentLevelOne,currentHighPercentLevelTwo,currentHighPercentLevelThree, passing = .70;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addition_game);



        goToHome = (TextView) (findViewById(R.id.backToHome));

        goToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(additionGame.this, homePage.class));
            }
        });


        levelOneButton = (Button) (findViewById(R.id.levelOne));
        levelOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(additionGame.this, additionLevelOne.class));
            }
        });


        //level two buttons
           levelTwoButton = (Button) (findViewById(R.id.levelTwo));
           levelTwoButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   startActivity(new Intent(additionGame.this, additionLevelTwo.class));
               }
           });



       //level three buttons
        levelThreeButton = (Button) (findViewById(R.id.levelThree));
            levelThreeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(additionGame.this, additionLevelThree.class));
                }
            });




        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        rootNode= FirebaseDatabase.getInstance();
        reference =  rootNode.getReference("Users");

        Id = FirebaseAuth.getInstance().getCurrentUser().getUid();


        reference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.child("email").getValue().equals(userEmail)) {

                        levelOneTotalCorrect = ds.child("additionLevelOneCorrect").getValue(Long.class);
                        levelOneTotalquestions = ds.child("additionLevelOneTotal").getValue(Long.class);

                        levelTwoTotalCorrect = ds.child("additionLevelTwoCorrect").getValue(Long.class);
                        levelTwoTotalquestions = ds.child("additionLevelTwoTotal").getValue(Long.class);

                        levelThreeTotalCorrect = ds.child("additionLevelThreeCorrect").getValue(Long.class);
                        levelThreeTotalquestions = ds.child("additionLevelThreeTotal").getValue(Long.class);

                        currentHighPercentLevelOne = ds.child("additionLevelOneScore").getValue(Double.class);
                        currentHighPercentLevelTwo = ds.child("additionLevelTwoScore").getValue(Double.class);




                        if( (currentHighPercentLevelOne > passing) && (levelOneTotalquestions>=5))
                            levelTwoButton.setEnabled(true);


                        if(currentHighPercentLevelTwo > passing && (levelTwoTotalquestions>=5))
                            levelThreeButton.setEnabled(true);




                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }
}