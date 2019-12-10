package men.ngopi.zain.githubuser.data.source;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import men.ngopi.zain.githubuser.data.source.local.entity.UserEntity;
import men.ngopi.zain.githubuser.data.source.remote.RemoteRepository;
import men.ngopi.zain.githubuser.data.source.remote.RemoteRepositoryListener;
import men.ngopi.zain.githubuser.data.source.remote.response.UserResponse;

public class GithubUserRepository implements GithubUserDataSource, RemoteRepositoryListener.OnSearchUser {

    private volatile static GithubUserRepository INSTANCE = null;

    private final RemoteRepository remoteRepository;

    private MutableLiveData<List<UserEntity>> data = new MutableLiveData<>();
    private List<UserEntity> userEntities = new ArrayList<>();

    private GithubUserRepository(@NonNull RemoteRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    public static GithubUserRepository getInstance(RemoteRepository remoteRepository) {
        if (INSTANCE == null) {
            synchronized (GithubUserRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GithubUserRepository(remoteRepository);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void searchUserByName(String query) {
        userEntities.clear();
        data.postValue(userEntities);
        remoteRepository.searchUserByName(query, "1", this);
    }

    @Override
    public void loadMoreUser(String query, String page) {
        remoteRepository.searchUserByName(query, page, this);
    }

    @Override
    public LiveData<List<UserEntity>> getUserList() {
        return data;
    }

    @Override
    public void onSuccessSearchUser(List<UserResponse> userResponses) {
        for (UserResponse user : userResponses) {
            UserEntity userEntity = new UserEntity();
            userEntity.setAvatar_url(user.getAvatar_url());
            userEntity.setLogin(user.getLogin());
            userEntity.setUrl(user.getUrl());
            userEntities.add(userEntity);
        }
        data.postValue(userEntities);
    }

    @Override
    public void onErrorSearchUser(String error) {

    }
}
