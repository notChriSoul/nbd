package example.model.vms;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import example.schemas.SchemaConst;

//@Entity(defaultKeyspace = SchemaConst.RENT_A_VM_NAMESPACE)
@CqlName(SchemaConst.VMS)
@PropertyStrategy(mutable = false)
public class Pro extends VirtualMachine {

    public Pro(int vmId, String discriminator, boolean rented, int ram, int storage, int rentalPrice) {
        super(vmId, discriminator, rented, ram, storage, rentalPrice);
    }
}