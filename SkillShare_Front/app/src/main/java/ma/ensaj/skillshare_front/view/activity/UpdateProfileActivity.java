package ma.ensaj.skillshare_front.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import ma.ensaj.skillshare_front.R;
import ma.ensaj.skillshare_front.model.User;
import ma.ensaj.skillshare_front.viewmodel.ProfileViewModel;


public class UpdateProfileActivity extends AppCompatActivity {
    private EditText editNom, editPrenom, editEmail, editLocalisation;
    private ImageView profileImageView;
    private Button saveButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        editNom = findViewById(R.id.editNom);
        editPrenom = findViewById(R.id.editPrenom);
        editEmail = findViewById(R.id.editEmail);
        editLocalisation = findViewById(R.id.editLocalisation);
        profileImageView = findViewById(R.id.profileImageView);
        saveButton = findViewById(R.id.saveButton);

        currentUser = (User) getIntent().getSerializableExtra("user");

        if (currentUser != null) {
            editNom.setText(currentUser.getNom());
            editPrenom.setText(currentUser.getPrenom());
            editEmail.setText(currentUser.getEmail());
            editLocalisation.setText(currentUser.getLocalisation());

            String baseUrl = "http://192.168.0.7:3600/skillShare" + currentUser.getImage();
            Glide.with(this)
                    .load(baseUrl)
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_launcher_background)
                    .into(profileImageView);
        }

        profileImageView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Sélectionner une image"), PICK_IMAGE_REQUEST);
        });

        saveButton.setOnClickListener(v -> updateProfile());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
        }
    }

    private void updateProfile() {
        currentUser.setNom(editNom.getText().toString());
        currentUser.setPrenom(editPrenom.getText().toString());
        currentUser.setEmail(editEmail.getText().toString());
        currentUser.setLocalisation(editLocalisation.getText().toString());
        ProfileViewModel viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        if (imageUri != null) {
            viewModel.uploadImage(currentUser,this, imageUri, viewModel);
        } else {
            viewModel.updateUser(currentUser);
        }

    }


}






