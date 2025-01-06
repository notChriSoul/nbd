package example.model;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import example.schemas.SchemaConst;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(defaultKeyspace = SchemaConst.RENT_A_VM_NAMESPACE)
@CqlName(SchemaConst.CLIENTS)
@Data
@NoArgsConstructor
public class Client {
    @PartitionKey
    private String personalId;
    @ClusteringColumn
    private String type = "CLIENT";
    private String firstName;
    private String lastName;

    public Client(String personalId, String firstName, String lastName) {
        this.personalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
