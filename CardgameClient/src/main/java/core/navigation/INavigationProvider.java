package core.navigation;

public interface INavigationProvider {
    boolean navigateTo(Class view);
    boolean navigatePrevious();

    //TODO: Implement some sort of Modal Views.
    //boolean displayModal(Class view);
    //boolean closeModal();
}
