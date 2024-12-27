package ma.ensaj.skillshare_front.viewmodel;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.loader.content.CursorLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import ma.ensaj.skillshare_front.model.Category;
import ma.ensaj.skillshare_front.model.User;
import ma.ensaj.skillshare_front.network.api.CategorieApi;
import ma.ensaj.skillshare_front.network.api.UserApi;
import ma.ensaj.skillshare_front.network.RetrofitInstance;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<List<Category>> categories;
    private MutableLiveData<User> userLiveData;
    private CategorieApi categorieApi;
    private UserApi userApi;

    public ProfileViewModel() {
        categorieApi = RetrofitInstance.getInstance().create(CategorieApi.class);
        userApi = RetrofitInstance.getInstance().create(UserApi.class);
        categories = new MutableLiveData<>();
        userLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public void fetchCategories() {
        categorieApi.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    categories.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.d("ProfileViewModel", "Échec de la requête fetchCategories : " + t.getMessage());
            }
        });
    }

    public void fetchUser(int userId) {
        userApi.getUserById(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    userLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("ProfileViewModel", "Échec de la requête fetchUser: " + t.getMessage());
            }
        });
    }


    public void updateUser(User user) {
        userApi.updateUser(user.getIdUser(), user).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("ProfileViewModel1", "Mise à jour réussie");
                    fetchUser(user.getIdUser());
                } else {
                    String errorMessage = "Erreur lors de la mise à jour";
                    try {
                        if (response.errorBody() != null) {
                            errorMessage = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        errorMessage = e.getMessage() != null ? e.getMessage() : "Erreur inconnue";
                    }
                    Log.d("ProfileViewModel2", "Erreur de réponse: " + errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String errorMessage = t.getMessage() != null ? t.getMessage() : "Erreur de connexion inconnue";
                Log.d("ProfileViewModel", "Échec de la mise à jour : " + errorMessage);
            }
        });
    }

    public void uploadImage(User userUp ,Context context, Uri imageUri, ProfileViewModel profileViewModel) {
        File file = new File(getRealPathFromURI(context, imageUri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        userApi.uploadImage(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String imageUrl = response.body().string();
                        Log.d("ProfileViewModel", "Image URL: " + imageUrl);

                        Log.d("ID_USER", userUp.getEmail());
                        if (userUp != null) {
                            userUp.setImage(imageUrl);
                            Log.d("ProfileViewModel", "l'utilisateur image: " + userUp.getImage());
                            profileViewModel.updateUser(userUp);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    String errorMessage = "Erreur lors du téléchargement de l'image";
                    try {
                        if (response.errorBody() != null) {
                            errorMessage = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        errorMessage = e.getMessage() != null ? e.getMessage() : "Erreur inconnue";
                    }
                    Log.d("ProfileViewModel", "Erreur de réponse uploadImage: " + errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ProfileViewModel", "Échec de la requête uploadImage: " + t.getMessage());
            }
        });
    }


    private String getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        Log.d("UpdateProfile", "Real path: " + result);
        return result;
    }

    public void addCategory(String nomCategorie) {
        Category newCategory = new Category();
        newCategory.setCategorie(nomCategorie);

        categorieApi.ajouterCategorie(newCategory).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    fetchCategories();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ProfileViewModel", "Échec de la requête addCategory: " + t.getMessage());
            }
        });
    }


}





//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//import ma.ensaj.skillshare_front.model.Category;
//import ma.ensaj.skillshare_front.model.User;
//import ma.ensaj.skillshare_front.network.RetrofitInstance;
//import ma.ensaj.skillshare_front.network.api.CategorieApi;
//import ma.ensaj.skillshare_front.network.api.UserApi;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import java.util.List;
//
//public class ProfileViewModel extends ViewModel {
//    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
//    private final MutableLiveData<List<Category>> categoriesLiveData = new MutableLiveData<>();
//    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
//    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
//    private final UserApi userApi;
//    private final CategorieApi categoryApi;
//
//    public ProfileViewModel() {
//        userApi = RetrofitInstance.getInstance().create(UserApi.class);
//        categoryApi =RetrofitInstance.getInstance().create(CategorieApi.class);
//    }
//
//    public LiveData<User> getUserData() {
//        return userLiveData;
//    }
//
//    public LiveData<List<Category>> getCategoriesData() {
//        return categoriesLiveData;
//    }
//
//    public LiveData<String> getError() {
//        return errorLiveData;
//    }
//
//    public LiveData<Boolean> getLoading() {
//        return loadingLiveData;
//    }
//
//    public void loadUserProfile(int userId) {
//        loadingLiveData.setValue(true);
//        userApi.getUserById(userId).enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                loadingLiveData.setValue(false);
//                if (response.isSuccessful() && response.body() != null) {
//                    userLiveData.setValue(response.body());
//                    loadUserCategories();
//                } else {
//                    errorLiveData.setValue("Erreur lors du chargement du profil");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                loadingLiveData.setValue(false);
//                errorLiveData.setValue("Erreur réseau: " + t.getMessage());
//            }
//        });
//    }
//
//    public void loadUserCategories() {
//        // getCatycorie by user id
//        categoryApi.getCategories().enqueue(new Callback<List<Category>>() {
//            @Override
//            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    categoriesLiveData.setValue(response.body());
//                } else {
//                    errorLiveData.setValue("Erreur lors du chargement des catégories");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Category>> call, Throwable t) {
//                errorLiveData.setValue("Erreur réseau: " + t.getMessage());
//            }
//        });
//    }
//
//    public void updateUserProfile(int userId, User user) {
//        loadingLiveData.setValue(true);
//        userApi.updateUser(userId, user).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                loadingLiveData.setValue(false);
//                if (response.isSuccessful()) {
//                    loadUserProfile(userId);
//                } else {
//                    errorLiveData.setValue("Erreur lors de la mise à jour du profil");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                loadingLiveData.setValue(false);
//                errorLiveData.setValue("Erreur réseau: " + t.getMessage());
//            }
//        });
//    }
//}