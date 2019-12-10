package men.ngopi.zain.githubuser.di;

import android.app.Application;

import men.ngopi.zain.githubuser.data.source.GithubUserRepository;
import men.ngopi.zain.githubuser.data.source.remote.RemoteRepository;

public class Injection {
    public static GithubUserRepository provideRepository(Application application) {
        RemoteRepository remoteRepository = RemoteRepository.getInstance(application);
        return GithubUserRepository.getInstance(remoteRepository);
    }
}
