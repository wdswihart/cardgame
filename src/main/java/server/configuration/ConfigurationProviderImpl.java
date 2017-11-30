package server.configuration;

public class ConfigurationProviderImpl implements ConfigurationProvider {

    private static ConfigurationProviderImpl sInstance = new ConfigurationProviderImpl();
    public static ConfigurationProviderImpl getInstance() {
        return sInstance;
    }
    public static void setInstance(ConfigurationProviderImpl impl) {
        sInstance = impl;
    }

    private String mHost = "localhost";
    private int mPort = 8087;

    public ConfigurationProviderImpl(String host, int port) {
        mHost = host;
        mPort = port;
    }

    public ConfigurationProviderImpl() {

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
