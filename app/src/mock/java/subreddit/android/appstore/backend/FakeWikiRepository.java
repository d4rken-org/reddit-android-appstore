package subreddit.android.appstore.backend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;

import rx.Observable;
import rx.functions.Func0;


public class FakeWikiRepository implements WikiRepository {
    public FakeWikiRepository() {
    }

    @Override
    public Observable<Collection<AppInfo>> getAppList() {
        return Observable.defer(new Func0<Observable<Collection<AppInfo>>>() {
            @Override
            public Observable<Collection<AppInfo>> call() {
                Collection<AppInfo> testValues = new ArrayList<>();
                AppInfo app1 = new AppInfo();
                app1.appName = "Awesome app 1";
                app1.description = "This is awesome app 1. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
                app1.contact = "test@gmail.com";
                app1.getTags().add(AppInfo.Tag.ADS);
                app1.getTags().add(AppInfo.Tag.GAME);
                app1.getTags().add(AppInfo.Tag.IAP);
                app1.getTags().add(AppInfo.Tag.TABLET);
                testValues.add(app1);
                AppInfo app2 = new AppInfo();
                app2.appName = "Awesome app 2";
                app2.description = "This is awesome app 2. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
                app2.contact = "http://google.com";
                app2.getTags().add(AppInfo.Tag.FREE);
                app2.getTags().add(AppInfo.Tag.APP);
                app2.getTags().add(AppInfo.Tag.PHONE);
                testValues.add(app2);
                for (int i = 0; i < 1000; i++) {
                    AppInfo randomApp = new AppInfo();
                    randomApp.appName = UUID.randomUUID().toString() + " app";
                    randomApp.description = UUID.randomUUID().toString() + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
                    randomApp.contact = UUID.randomUUID().toString() + " http://google.com";
                    for (int j = 0; j < 7; j++) {
                        randomApp.getTags().add(randomEnum(AppInfo.Tag.class));
                    }
                    testValues.add(randomApp);
                }
                return Observable.just(testValues);
            }
        });
    }

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = new Random().nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }
}
