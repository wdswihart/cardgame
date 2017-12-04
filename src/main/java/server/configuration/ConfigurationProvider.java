package server.configuration;

public interface ConfigurationProvider {
    String getHost();
    int getPort();

    int getMaxHandSize();
}
