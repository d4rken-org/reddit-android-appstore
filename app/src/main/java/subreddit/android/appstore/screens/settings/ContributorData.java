package subreddit.android.appstore.screens.settings;

import java.util.ArrayList;

public class ContributorData {
    private ArrayList<String> contributors;

    public ContributorData() {
        contributors = new ArrayList<String>();
    }

    public void addContributor(String name) {
        contributors.add(name);
    }

    public ArrayList<String> getContributors() {
        return contributors;
    }
}
