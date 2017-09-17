package di;

import core.navigation.INavigationProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TestNavigationProvider implements INavigationProvider {
    private List<Class> mNavigationHistory = new ArrayList<>();
    private Stack<Class> mNavigationStack = new Stack<>();

    private static TestNavigationProvider sTestNavigationProvider = new TestNavigationProvider();

    public static TestNavigationProvider getInstance() {
        return sTestNavigationProvider;
    }

    public static void resetInstance() {
        sTestNavigationProvider.mNavigationStack.clear();
        sTestNavigationProvider.mNavigationHistory.clear();
    }

    private TestNavigationProvider() {}

    @Override
    public boolean navigateTo(Class view) {
        mNavigationStack.push(view);
        mNavigationHistory.add(view);

        return true;
    }

    @Override
    public boolean navigatePrevious() {
        if (mNavigationStack.size() > 1) {

            //Remove current view.
            mNavigationStack.pop();
            //Add the one we're navigating to to the history.
            mNavigationHistory.add(mNavigationStack.peek());

            return true;
        }
        return false;
    }

    public List<Class> getNavigationHistory(){
        return mNavigationHistory;
    }
}
