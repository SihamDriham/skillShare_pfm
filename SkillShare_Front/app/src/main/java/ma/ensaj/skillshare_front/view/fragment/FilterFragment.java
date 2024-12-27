package ma.ensaj.skillshare_front.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import ma.ensaj.skillshare_front.R;
import ma.ensaj.skillshare_front.model.Category;
import ma.ensaj.skillshare_front.network.RetrofitInstance;
import ma.ensaj.skillshare_front.network.api.CategorieApi;
import ma.ensaj.skillshare_front.viewmodel.MainViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterFragment extends Fragment {

    private MainViewModel mainViewModel;

    private TextInputLayout cityInputLayout;
    private AutoCompleteTextView categoryInputLayout;
    private AutoCompleteTextView cityAutoComplete;
    private TextInputEditText minPriceEditText;
    private TextInputEditText maxPriceEditText;
    private MaterialButton applyFiltersButton;
    private MaterialButton resetFiltersButton;
    private List<Category> categories;

    public FilterFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filter, container, false);

        cityInputLayout = rootView.findViewById(R.id.cityInputLayout);
        cityAutoComplete = rootView.findViewById(R.id.cityAutoComplete);

        String[] cities = getResources().getStringArray(R.array.cities_array);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, cities);

        cityAutoComplete.setAdapter(adapter);
        categoryInputLayout = rootView.findViewById(R.id.categoryAutoComplete);
        minPriceEditText = rootView.findViewById(R.id.minPriceEditText);
        maxPriceEditText = rootView.findViewById(R.id.maxPriceEditText);
        applyFiltersButton = rootView.findViewById(R.id.applyFiltersButton);
        resetFiltersButton = rootView.findViewById(R.id.resetFiltersButton);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        fetchCategories();

        applyFiltersButton.setOnClickListener(v -> applyFilters());

        resetFiltersButton.setOnClickListener(v -> resetFilters());

        return rootView;
    }

    private void applyFilters() {
        String city = cityInputLayout.getEditText() != null ?
                cityInputLayout.getEditText().getText().toString().trim() : null;

        if (city != null && city.isEmpty()) {
            city = null;
        }
        String category = categoryInputLayout.getEditableText() != null ?
                categoryInputLayout.getEditableText().toString().trim() : null;
        if (category != null && category.isEmpty()) {
            category = null;
        }

        float minPrice = 0f;
        float maxPrice = 1000f;

        try {
            String minPriceStr = minPriceEditText.getText() != null ? minPriceEditText.getText().toString() : "";
            String maxPriceStr = maxPriceEditText.getText() != null ? maxPriceEditText.getText().toString() : "";

            if (!minPriceStr.isEmpty()) {
                minPrice = Float.parseFloat(minPriceStr);
            }
            if (!maxPriceStr.isEmpty()) {
                maxPrice = Float.parseFloat(maxPriceStr);
            }

            if (minPrice > maxPrice) {
                Toast.makeText(getContext(), "Le prix minimum ne peut pas être supérieur au prix maximum", Toast.LENGTH_SHORT).show();
                return;
            }

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Veuillez entrer des prix valides", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("FilterFragment", "City: " + city);
        Log.d("FilterFragment", "Category: " + category);
        Log.d("FilterFragment", "Min Price: " + minPrice);
        Log.d("FilterFragment", "Max Price: " + maxPrice);

        mainViewModel.searchAndFilterServices(minPrice, maxPrice, category, city, "");

        navigateToBlankFragment();
    }

    private void navigateToBlankFragment() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.navigation_result);
    }

    private void fetchCategories() {
        CategorieApi categorieApi = RetrofitInstance.getInstance().create(CategorieApi.class);

        categorieApi.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories = response.body();
                    String[] categoryNames = new String[categories.size()];
                    for (int i = 0; i < categories.size(); i++) {
                        categoryNames[i] = categories.get(i).getCategorie();
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            categoryNames
                    );
                    categoryInputLayout.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Failed to load categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error fetching categories: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetFilters() {
        if (cityInputLayout.getEditText() != null) {
            cityInputLayout.getEditText().setText("");
        }
        if (categoryInputLayout.getEditableText() != null) {
            categoryInputLayout.getEditableText().clear();
        }

        minPriceEditText.setText("");
        maxPriceEditText.setText("");
    }
}