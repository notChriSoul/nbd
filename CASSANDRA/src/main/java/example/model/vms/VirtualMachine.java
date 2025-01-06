package example.model.vms;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import example.schemas.SchemaConst;

//@Entity(defaultKeyspace = SchemaConst.RENT_A_VM_NAMESPACE)
@PropertyStrategy(mutable = false)
@CqlName(SchemaConst.VMS)
public class VirtualMachine {
    @PartitionKey
    private int vmId;

    private String discriminator;

    @CqlName("is_rented")
    private boolean rented;

    private int ram;
    private int storage;
    private int rentalPrice;

    public VirtualMachine(int vmId, String discriminator, boolean rented, int ram, int storage, int rentalPrice) {
        this.vmId = vmId;
        this.discriminator = discriminator;
        this.rented = rented;
        this.ram = ram;
        this.storage = storage;
        this.rentalPrice = rentalPrice;
    }
}
