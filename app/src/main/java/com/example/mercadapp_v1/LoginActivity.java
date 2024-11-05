package com.example.mercadapp_v1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.animation.ValueAnimator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.mindrot.jbcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {

    private TextView btnNewUser;
    private EditText txtUsuario, txtContrasena;
    private Button btnIngresar;
    private CheckBox checkboxShowPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsuario = findViewById(R.id.txtUsuario);
        txtContrasena = findViewById(R.id.txtContrasena);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnNewUser = findViewById(R.id.btnNewUser);
        checkboxShowPassword = findViewById(R.id.checkboxShowPassword);

        checkboxShowPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            applyTextFadeAnimation(txtContrasena);

            if (isChecked) {
                txtContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                txtContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            txtContrasena.setSelection(txtContrasena.getText().length()); // Mueve el cursor al final del texto
        });

        btnIngresar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                validarLogin();
            }
        });

        btnNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nuevoUsuario();
            }
        });
    }

    private void applyTextFadeAnimation(EditText editText) {
        String text = editText.getText().toString();
        SpannableString spannableString = new SpannableString(text);

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(100);
        animator.addUpdateListener(animation -> {
            float alpha = (float) animation.getAnimatedValue();
            int alphaValue = (int) (alpha * 255);

            spannableString.setSpan(new ForegroundColorSpan(0x00FFFFFF | (alphaValue << 24)),
                    0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            editText.setText(spannableString);
        });

        animator.start();
    }

    public void validarLogin() {
        try {
            String user = txtUsuario.getText().toString();
            String passw = txtContrasena.getText().toString();

            SQLiteDatabase db = openOrCreateDatabase("MercadAppBD", Context.MODE_PRIVATE, null);

            String sql = "SELECT contrasenia FROM usuario WHERE nombre = ?";
            Cursor c = db.rawQuery(sql, new String[]{user});

            if(c.moveToFirst()){
                String hashedPassw = c.getString(0);

                if(BCrypt.checkpw(passw, hashedPassw)){
                    Intent i = new Intent(this, InicioActivity.class);
                    i.putExtra("nombreUsuario", user);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
            }
            c.close();
            db.close();
        }
        catch(Exception e)
        {
            Toast.makeText(this, "Error al iniciar sesión, intente nuevamente", Toast.LENGTH_SHORT).show();
        }
    }

    public void nuevoUsuario(){
        Intent intent = new Intent(this, NewUserActivity.class);
        startActivity(intent);
    }
}