import di.TestGameProviderImpl;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import client.ui.CardDetailView.CardDetailView;
import client.ui.DraggableView.DraggableView;
import client.ui.home.HomeViewModel;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@Category(UnitTest.class)
@RunWith(JUnitPlatform.class)
class HomeViewModelTest extends BaseTest {
    protected HomeViewModel homeViewModel;

    @BeforeEach
    protected void setup() {
        super.setup();
        homeViewModel = new HomeViewModel(connectionProvider, navigationProvider, TestGameProviderImpl.getInstance());
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
