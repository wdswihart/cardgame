package server.configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConfigurationProviderImpl implements ConfigurationProvider {

    private static ConfigurationProviderImpl sInstance = new ConfigurationProviderImpl();
    public static ConfigurationProviderImpl getInstance() {
        return sInstance;
    }
    public static void setInstance(ConfigurationProviderImpl impl) {
        sInstance = impl;
    }

    private String mHost = "";
    private int mPort = 8087;

    public ConfigurationProviderImpl() {
        try {
            mHost = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getHost() {
        return mHost;
    }

    @Override
    public int getPort() {
        return mPort;
    }

    @Override
    public int getMaxHandSize() {
        return 7;
    }
}
