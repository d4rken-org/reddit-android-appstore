package subreddit.android.appstore.screens.navigation;

import dagger.Component;
import subreddit.android.appstore.AppComponent;
import subreddit.android.appstore.util.dagger.FragmentScope;


@FragmentScope
@Component(modules = NavigationModule.class, dependencies = AppComponent.class)
public interface NavigationComponent {
    void inject(NavigationFragment fragment);
}
