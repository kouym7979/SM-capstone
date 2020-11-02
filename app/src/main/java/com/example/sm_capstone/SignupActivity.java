package com.example.sm_capstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private Button val_Button;//중복확인버튼
    private Button regButton;

    private EditText nameEdit, passEdit,passChkEdit,phoneNumEdit,emailEdit;

    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private FirebaseFirestore mstore = FirebaseFirestore.getInstance();

    private CheckBox manager;//관리자인지 직원인지
    private CheckBox employee;
    int dep;
    Boolean check = false;
    String type;//관리자, 직원 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_acitivity);
        setTitle("회원가입");

        val_Button = findViewById(R.id.valButton);
        regButton = findViewById(R.id.regButton);

        nameEdit = findViewById(R.id.username);
        passEdit = findViewById(R.id.password);
        passChkEdit = findViewById(R.id.passCheck);
        phoneNumEdit = findViewById(R.id.phoneNum);
        emailEdit = findViewById(R.id.email);

        //checkBox
        manager = (CheckBox) findViewById(R.id.manager);
        employee = (CheckBox) findViewById(R.id.employee);

        manager.setOnCheckedChangeListener(this);
        employee.setOnCheckedChangeListener(this);

        val_Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDuplicate();
            }
        });

        //regButton ->회원가입 완료버튼
        regButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerInfo();
            }
        });

    }

    public void checkDuplicate(){//아이디 중복확인 메소드
        mAuth.fetchSignInMethodsForEmail(emailEdit.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if(task.getResult().getSignInMethods().size()==0){
                            Toast.makeText(getApplicationContext(), "이 이메일을 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                            val_Button.setEnabled(false);
                            val_Button.setBackgroundColor(Color.GREEN);
                            val_Button.setText("success");
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "이 이메일은 이미 사용중입니다.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void registerInfo() {//회원가입 등록 메소드
        if (val_Button.isEnabled()) {
            Toast.makeText(getApplicationContext(), "ID중복검사를 해주세요", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "현재 회원가입 중", Toast.LENGTH_SHORT).show();
            mAuth.createUserWithEmailAndPassword(emailEdit.getText().toString(), passEdit.getText().toString()).
                    addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Map<String, Object> userMap = new HashMap<>();
                                //EmploID란 클래스를 통해서 사용자별 정보를 객체별로 저장하는 클래스
                                userMap.put(EmployID.documentId, user.getUid());//고유 식별번호
                                userMap.put(EmployID.name,nameEdit.getText().toString());
                                userMap.put(EmployID.phone_number,phoneNumEdit.getText().toString());
                                userMap.put(EmployID.email, emailEdit.getText().toString());
                                userMap.put(EmployID.password, passEdit.getText().toString());
                                userMap.put(EmployID.type, type);
                                mstore.collection("User").add(user);
                                finish();
                            } else {
                                Toast.makeText(SignupActivity.this, "error.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(manager.isChecked()==true)
        {
            employee.setChecked(false);
            type="manager";
        }
        else if(employee.isChecked()==true)
        {
            manager.setChecked(false);
            type="employee";
        }
    }
}