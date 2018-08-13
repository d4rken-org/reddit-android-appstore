package subreddit.android.appstore.util;

import java.util.ArrayList;
import java.util.Collection;

import io.realm.RealmList;
import io.realm.RealmObject;

public class RealmString extends RealmObject {
    private String aString;

    public RealmString() {

    }

    private RealmString(String aString) {
        this.aString = aString;
    }

    public static RealmList<RealmString> fromCollection(Collection<String> stringCollection) {
        RealmList<RealmString> realmStrings = new RealmList<>();
        for (String string : stringCollection) realmStrings.add(new RealmString(string));
        return realmStrings;
    }

    public static Collection<String> toCollection(RealmList<RealmString> realmStrings) {
        Collection<String> stringCollection = new ArrayList<>();
        for (RealmString realmString : realmStrings) stringCollection.add(realmString.aString);
        return stringCollection;
    }
}
