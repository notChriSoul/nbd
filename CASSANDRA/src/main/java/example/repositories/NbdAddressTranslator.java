package example.repositories;

import com.datastax.oss.driver.api.core.addresstranslation.AddressTranslator;
import com.datastax.oss.driver.api.core.context.DriverContext;
import lombok.NonNull;

import java.net.InetSocketAddress;

public class NbdAddressTranslator implements AddressTranslator {
    public NbdAddressTranslator(DriverContext dctx) {}

    @NonNull
    public InetSocketAddress translate(InetSocketAddress address) {
        String hostAddress = address.getAddress().getHostAddress();
        return switch (hostAddress) {
            case "172.19.0.2" -> new InetSocketAddress("cassadnra1", 9042);
            case "172.19.0.3" -> new InetSocketAddress("cassadnra2", 9043);
            default -> throw new RuntimeException("Unknown host: " + hostAddress);
        };
    }

    @Override
    public void close() {

    }
}
