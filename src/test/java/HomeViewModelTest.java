import di.TestNavigationProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import client.ui.CardDetailView.CardDetailView;
import client.ui.DraggableView.DraggableView;
import client.ui.home.HomeViewModel;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(JUnitPlatform.class)
class HomeViewModelTest extends BaseTest {
    protected HomeViewModel homeViewModel = TestDependencies.getInjector().getInstance(HomeViewModel.class);

    @AfterEach
    void tearDownEach() {
        //Reset the navigation provider. Bad practices but we can refactor later.
        //Tests shouldn't really affect the state of future tests.
//        TestNavigationProvider.resetInstance();
    }

    @Test
    @DisplayName("NavigateToCardDetailView")
    void verifyNavigation_toCardDetailView_onCardDetailViewAction() {
        homeViewModel.getShowCardDetailViewCommand().execute();
        verify(navigationProvider).navigateTo(CardDetailView.class);
    }

    @Test
    @DisplayName("NavigateToDraggableView")
    void verifyNavigation_toDraggableView_onDraggableViewCommand() {
        homeViewModel.getShowDraggableViewCommand().execute();
        verify(navigationProvider).navigateTo(DraggableView.class);
    }
}
