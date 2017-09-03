package core;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NavigationAdapter extends MouseAdapter {

    private BaseViewController mViewController;
    private String mTargetStage;

    public NavigationAdapter(BaseViewController viewController, String targetStage) {
        mViewController = viewController;
        mTargetStage = targetStage;
    }

    @Override
    public void mousePressed(MouseEvent event) {
        mViewController.navigateTo(mTargetStage);
    }
}
