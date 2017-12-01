package client.ui.discovery;

import client.core.ConnectionProvider;
import client.core.navigation.INavigationProvider;
import client.core.socketio.SocketIOClientProvider;
import client.ui.BaseViewModel;
import client.ui.login.LoginView;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.inject.Inject;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DiscoveryViewModel extends BaseViewModel {
    private SocketIOClientProvider mSocketIOClientProvider;

    private Command mJoinServerCommand;

    private ObservableList<String> mServersListProperty = FXCollections.observableArrayList();
    private Property<String> mSelectedServerProperty = new SimpleStringProperty();

    @Inject
    public DiscoveryViewModel(ConnectionProvider connectionProvider, INavigationProvider navigationProvider, SocketIOClientProvider socketIOClientProvider) {
        super(connectionProvider, navigationProvider);
        mSocketIOClientProvider = socketIOClientProvider;

        mJoinServerCommand = new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                if (mSelectedServerProperty.getValue().isEmpty()) {
                    return;
                }

                mSocketIOClientProvider.createNewClient(mSelectedServerProperty.getValue());
                mNavigationProvider.navigateTo(LoginView.class);
            }
        });

        beginDiscovery();
    }

    private void beginDiscovery() {
        new Thread(() -> {
            byte[] buf = new byte[1000];

            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName("255.255.255.255"), 12000);
                DatagramSocket socket = new DatagramSocket();
                socket.send(packet);
                socket.receive(packet);

                String address = new String(packet.getData(), packet.getOffset(), packet.getLength());
                System.out.println("[ADDRESS_DISCOVERY]: Address discovered ["+ address +"]");

                mServersListProperty.add(address);
            } catch (IOException e) {
                e.printStackTrace();
                mServersListProperty.add("127.0.0.1:8087");
            }
        }).run();
    }

    public Command getJoinServerCommand() {
        return mJoinServerCommand;
    }

    public ObservableList<String> getServersListProperty() {
        return mServersListProperty;
    }

    public Property<String> getSelectedServerProperty() {
        return mSelectedServerProperty;
    }
}
