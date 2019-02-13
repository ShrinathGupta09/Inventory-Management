package com.realllydan.management;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddItemActivity extends AppCompatActivity {
    private EditText name,cost,quantity;
    private Button submit;
    private String Pname,Pcost,Pquantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        name=findViewById(R.id.et_name);
        cost=findViewById(R.id.et_cost);
        quantity=findViewById(R.id.et_quan);
        submit=findViewById(R.id.bt_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 send_product(name,cost,quantity);
            }
        });
    }
    public  void  send_product(EditText n,EditText c,EditText q)
    {
        Pname=n.getText().toString();
        Pcost=c.getText().toString();
        Pquantity=q.getText().toString();
    }

}
