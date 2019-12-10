package men.ngopi.zain.githubuser.data.source.remote;

import java.util.List;

import men.ngopi.zain.githubuser.data.source.remote.response.UserResponse;

public class RemoteRepositoryListener {
    public interface OnSearchUser{
        void onSuccessSearchUser(List<UserResponse> userResponses);
        void onErrorSearchUser(String error);
    }
}
