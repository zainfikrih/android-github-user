package men.ngopi.zain.githubuser.ui.home;

import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import men.ngopi.zain.githubuser.data.source.GithubUserRepository;
import men.ngopi.zain.githubuser.data.source.local.entity.UserEntity;

public class HomeViewModel extends ViewModel {
    private GithubUserRepository repository;
    private MutableLiveData<HomeViewState> state = new MutableLiveData<>();

    public void setState(HomeViewState state){
        this.state.setValue(state);
    }

    public LiveData<HomeViewState> getState(){
        return state;
    }

    public HomeViewModel(GithubUserRepository repository){
        this.repository = repository;
    }

    public LiveData<List<UserEntity>> getUserList() {
        return repository.getUserList();
    }

    public void searchUserByName(String query) {
        repository.searchUserByName(query);
    }

    public void loadMoreUser(String query, String page) {
        repository.loadMoreUser(query, page);
    }
}