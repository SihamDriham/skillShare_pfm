package ma.ensaj.skillshare_front.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import ma.ensaj.skillshare_front.MyApp;
import ma.ensaj.skillshare_front.R;
import ma.ensaj.skillshare_front.model.Category;
import ma.ensaj.skillshare_front.model.User;
import ma.ensaj.skillshare_front.view.activity.UpdateProfileActivity;
import ma.ensaj.skillshare_front.view.adapter.ProfileAdapter;
import ma.ensaj.skillshare_front.viewmodel.ProfileViewModel;

public class ProfileFragment extends Fragment implements ProfileAdapter.OnCategoryClickListener{

    private ProfileViewModel profileViewModel;
    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;
    private ImageView editProfileButton;
    private ImageView addCategoryButton;
    private TextView userName;
    private TextView userEmail;
    private TextView quoteOfTheDayTitle;
    private TextView localisation;
    private TextView quoteAuthor;
    private ImageView imageProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        recyclerView = view.findViewById(R.id.categoriesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        editProfileButton = view.findViewById(R.id.editProfileButton);
        addCategoryButton = view.findViewById(R.id.addCategoryButton);
        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userJoinDate);
        quoteOfTheDayTitle = view.findViewById(R.id.quoteOfTheDayTitle);
        localisation = view.findViewById(R.id.quoteOfTheDay);
        quoteAuthor = view.findViewById(R.id.quoteAuthor);
        imageProfile= view.findViewById(R.id.profile_Image);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            profileAdapter = new ProfileAdapter(categories, getContext(), this);
            recyclerView.setAdapter(profileAdapter);
        });

        profileViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            userName.setText(user.getNom() + " " + user.getPrenom());
            userEmail.setText(user.getEmail());
            localisation.setText(user.getLocalisation());
            quoteAuthor.setText("Score : "+ String.valueOf(user.getNoteMoyenne()));
            String baseUrl = "http://192.168.0.7:3600/skillShare"+user.getImage();
            Glide.with(getContext())
                    .load(baseUrl)  // URL de l'image
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_launcher_background)
                    .into(imageProfile);
        });

        profileViewModel.fetchCategories();
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        User userInfo = MyApp.decodeToken(token);
        profileViewModel.fetchUser(userInfo.getIdUser());

        editProfileButton.setOnClickListener(v -> {
            User currentUser = profileViewModel.getUser().getValue();
            if (currentUser != null) {
                Intent intent = new Intent(getContext(), UpdateProfileActivity.class);
                intent.putExtra("user", currentUser);
                startActivity(intent);
            }
        });

        addCategoryButton.setOnClickListener(v -> showAddCategoryDialog());

        return view;
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_category, null);

        EditText categoryNameInput = dialogView.findViewById(R.id.categoryNameInput);

        builder.setView(dialogView)
                .setTitle("Add category")
                .setPositiveButton("Add", (dialog, id) -> {
                    String categoryName = categoryNameInput.getText().toString().trim();
                    if (!categoryName.isEmpty()) {
                        profileViewModel.addCategory(categoryName);
                    } else {
                        Toast.makeText(getContext(), "Enter a category name", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onCategoryClick(Category category) {
        Bundle bundle = new Bundle();
        bundle.putInt("categoryId", category.getIdCategorie());
        Navigation.findNavController(getView()).navigate(R.id.servicesFragment, bundle);
    }
}
