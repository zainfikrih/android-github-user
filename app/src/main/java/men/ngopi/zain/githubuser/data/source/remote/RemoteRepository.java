package men.ngopi.zain.githubuser.data.source.remote;

import android.app.Application;
import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import men.ngopi.zain.githubuser.data.source.remote.response.UserResponse;

public class RemoteRepository {
    private static RemoteRepository INSTANCE;

    private RemoteRepository(Context context) {
        AndroidNetworking.initialize(context);
    }

    public static RemoteRepository getInstance(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new RemoteRepository(application.getApplicationContext());
        }
        return INSTANCE;
    }

    public void searchUserByName(String query, String page, RemoteRepositoryListener.OnSearchUser callback) {
        AndroidNetworking.cancel("get_user_list");
        String BASE_URL = "https://api.github.com/";
        AndroidNetworking
                .get(BASE_URL + "search/users?q=" + query + "&page=" + page)
                .setPriority(Priority.HIGH)
                .setTag("get_user_list")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("items");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Gson JSONArrayList to ArrayList<UserResponse>
                        Type listType = new TypeToken<List<UserResponse>>() {
                        }.getType();
                        List<UserResponse> userResponseList;
                        assert jsonArray != null;
                        userResponseList = new Gson().fromJson(jsonArray.toString(), listType);

                        callback.onSuccessSearchUser(userResponseList);
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onErrorSearchUser(anError.getErrorBody());
                    }
                });
    }
}
