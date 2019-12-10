package men.ngopi.zain.githubuser.data.source;

import androidx.lifecycle.LiveData;

import java.util.List;

import men.ngopi.zain.githubuser.data.source.local.entity.UserEntity;

public interface GithubUserDataSource {
    void searchUserByName(String query);

    void loadMoreUser(String query, String page);

    LiveData<List<UserEntity>> getUserList();
}
