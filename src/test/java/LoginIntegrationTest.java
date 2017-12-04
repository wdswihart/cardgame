import client.core.ConnectionProvider;
import de.saxsys.mvvmfx.guice.internal.GuiceInjector;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import server.GameServer;
import util.GuiceUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

//This is our integration test.
//The classes it is testing integration on:
//
// *ClientSide
// - ConnectionProviderImpl
// - SocketIOClientProviderImpl
//
// *ServerSide
// - GameServer
// - StorageProviderImpl
// - SocketIOServerProviderImpl
// - UserProviderImpl
// - CreateAccountEventHandler
// - LoginEventHandler


@Category(IntegrationTest.class)
@RunWith(JUnitPlatform.class)
public class LoginIntegrationTest {
    ConnectionProvider connectionProvider;
    GameServer server;

    @BeforeEach
    protected void setup() {
        //We need a cooldown to allow notifications to finish.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //TODO: Clear persistent data.
        server = GuiceUtils.getInjector().getInstance(GameServer.class);
        server.startServer();
        connectionProvider = GuiceUtils.getInjector().getInstance(ConnectionProvider.class);
    }

    @AfterEach
    protected void teardown() {
        server.stopServer();
    }

    @Test
    public void createAccount_updatesCreatedUserProperty() {
        String username = UUID.randomUUID().toString();
        assertTrue(waitForCreateUser(username, username));
    }

    @Test
    public void loginAccount_withExistingAccount_notifiesValidUser() {
        String username = UUID.randomUUID().toString();
        assertTrue(waitForCreateUser(username, username));
        assertTrue(waitForLoginUser(username, username));
    }

    @Test
    public void loginAccount_withoutExistingAccount_doesNotNotifyValidUser() {
        assertFalse(waitForLoginUser("non-existent", ""));
    }

    @Test
    public void createAccount_thatExists_doesNotNotifyValidUser() {
        String username = UUID.randomUUID().toString();

        assertTrue(waitForCreateUser(username, username));
        assertFalse(waitForCreateUser(username, username));
    }

    private boolean waitForCreateUser(String username, String password) {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        connectionProvider.getCreatedUser().addListener((obs, oldVal, newVal) -> {
            if (newVal.isDefault()) {
                return;
            }

            lock.lock();
            condition.signal();
            lock.unlock();
        });

        connectionProvider.createAccount(username, password);

        try {
            lock.lock();
            return condition.await(5, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            fail("Error creating account");
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }

        return false;
    }

    private boolean waitForLoginUser(String username, String password) {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        connectionProvider.getAuthenticatedUser().addListener((obs, oldVal, newVal) -> {
            if (newVal.isDefault()) {
                return;
            }

            lock.lock();
            assertEquals(username, newVal.getUsername());
            assertEquals(password, newVal.getPassword());
            condition.signal();
            lock.unlock();
        });

        connectionProvider.loginUser(username, password);

        try {
            lock.lock();
            return condition.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            fail("Exception while waiting.");
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
        return false;
    }
}
