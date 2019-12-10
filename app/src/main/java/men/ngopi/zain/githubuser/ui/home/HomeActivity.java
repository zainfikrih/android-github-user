package men.ngopi.zain.githubuser.ui.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import men.ngopi.zain.githubuser.R;
import men.ngopi.zain.githubuser.adapter.UserAdapter;
import men.ngopi.zain.githubuser.data.source.local.entity.UserEntity;
import men.ngopi.zain.githubuser.viewmodel.ViewModelFactory;

public class HomeActivity extends AppCompatActivity {

    private HomeViewModel viewModel;
    private UserAdapter userAdapter;
    private List<UserEntity> userEntityList = new ArrayList<>();
    private HomeViewState state = new HomeViewState();

    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount, page = 1;
    private String query;

    @BindView(R.id.rv_user_home)
    RecyclerView rvUser;
    @BindView(R.id.iv_empty_user_home)
    ImageView ivEmptyUser;
    @BindView(R.id.pb_load_user_home)
    ProgressBar pbLoadUser;
    @BindView(R.id.toolbar_home)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        viewModel = obtainViewModel(this);

        viewModel.getState().observe(this, homeViewState -> {
            pbLoadUser.setVisibility(homeViewState.getPbVisibility());
            ivEmptyUser.setVisibility(homeViewState.getIvEmptyVisibility());
        });

        viewModel.getUserList().observe(this, userEntities -> {
            if (userEntities != null && userEntities.size() > 0) {
                state.setPbVisibility(View.GONE);
                state.setIvEmptyVisibility(View.GONE);
                viewModel.setState(state);
                userEntityList = userEntities;
                userAdapter.setUserEntities(userEntityList);
                loading = true;
            }
        });

        setUpRecyclerViewUser();
    }

    private void setUpRecyclerViewUser() {
        userAdapter = new UserAdapter();
        userAdapter.setUserEntities(userEntityList);
        LinearLayoutManager llm = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvUser.setLayoutManager(llm);
        rvUser.setHasFixedSize(true);
        rvUser.setItemAnimator(new DefaultItemAnimator());
        rvUser.setAdapter(userAdapter);

        rvUser.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    visibleItemCount = llm.getChildCount();
                    totalItemCount = llm.getItemCount();
                    pastVisiblesItems = llm.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            loadMore(query, String.valueOf(++page));
                            state.setPbVisibility(View.VISIBLE);
                            viewModel.setState(state);
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem mSearch = menu.findItem(R.id.appSearchBar);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search user");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                query = newText;
                if (!TextUtils.isEmpty(newText)) {
                    state.setPbVisibility(View.VISIBLE);
                    state.setIvEmptyVisibility(View.GONE);
                    viewModel.setState(state);
                    searchUser(newText);
                } else {
                    state.setPbVisibility(View.GONE);
                    state.setIvEmptyVisibility(View.VISIBLE);
                    viewModel.setState(state);
                    userEntityList.clear();
                    userAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void searchUser(String query) {
        viewModel.searchUserByName(query);
    }

    private void loadMore(String query, String page) {
        viewModel.loadMoreUser(query, page);
    }

    @NonNull
    private static HomeViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(HomeViewModel.class);
    }
}
