package subreddit.android.appstore.backend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.ReplaySubject;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.AppTags;


public class FakeWikiRepository implements WikiRepository {
    ReplaySubject<Collection<AppInfo>> replaySubject;

    public FakeWikiRepository() {
    }

    @Override
    public Observable<Collection<AppInfo>> getAppList() {
        if (replaySubject == null) {
            replaySubject = ReplaySubject.createWithSize(1);
            refresh();
        }
        return replaySubject;
    }

    private Observable<Collection<AppInfo>> loadData() {
        return Observable.defer(new Callable<ObservableSource<? extends Collection<AppInfo>>>() {
            @Override
            public ObservableSource<? extends Collection<AppInfo>> call() throws Exception {
                Collection<AppInfo> testValues = new ArrayList<>();
                AppInfo app1 = new AppInfo();
                app1.setAppName("Awesome app 1");
                app1.setDescription("This is awesome app 1. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");

                app1.getTags().add(AppTags.FREE);
                app1.getTags().add(AppTags.PHONE);
                testValues.add(app1);
                AppInfo app2 = new AppInfo();
                app2.setAppName("Awesome app 2");
                app2.setDescription("This is awesome app 2. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");

                app2.getTags().add(AppTags.TABLET);
                app2.getTags().add(AppTags.WEAR);
                app2.getTags().add(AppTags.PAID);
                testValues.add(app2);
                for (int i = 0; i < 1000; i++) {
                    AppInfo randomApp = new AppInfo();
                    randomApp.setAppName(UUID.randomUUID().toString() + " app");
                    randomApp.setDescription(UUID.randomUUID().toString() + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");

                    for (int j = 0; j < 7; j++) {
                        randomApp.getTags().add(randomEnum(AppTags.class));
                    }
                    testValues.add(randomApp);
                }
                return Observable.just(testValues);
            }
        });
    }

    @Override
    public void refresh() {
        loadData().subscribe(new Consumer<Collection<AppInfo>>() {
            @Override
            public void accept(Collection<AppInfo> appInfos) throws Exception {
                replaySubject.onNext(appInfos);
            }
        });
    }

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = new Random().nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }
}
