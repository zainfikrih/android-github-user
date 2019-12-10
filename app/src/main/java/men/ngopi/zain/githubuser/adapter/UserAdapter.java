package men.ngopi.zain.githubuser.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import men.ngopi.zain.githubuser.R;
import men.ngopi.zain.githubuser.data.source.local.entity.UserEntity;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<UserEntity> userEntities = new ArrayList<>();

    public void setUserEntities(List<UserEntity> userEntities) {
        this.userEntities = userEntities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bindViewHolder(userEntities.get(position));
    }

    @Override
    public int getItemCount() {
        return userEntities.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_name_user)
        TextView tvName;
        @BindView(R.id.iv_item_profile_user)
        ImageView ivProfile;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindViewHolder(UserEntity userEntity) {
            tvName.setText(userEntity.getLogin());
            Glide.with(this.ivProfile.getContext()).load(Uri.parse(userEntity.getAvatar_url())).centerCrop().into(ivProfile);
        }
    }
}
