package example.schemas;

import com.datastax.oss.driver.api.core.CqlIdentifier;

public class CQLSchema {
    public static final CqlIdentifier RENT_A_VM_NAMESPACE = CqlIdentifier.fromCql("rent_a_vm");

    public static final CqlIdentifier RENTS_BY_VM = CqlIdentifier.fromCql("rents_by_vm");
    public static final CqlIdentifier RENTS_BY_CLIENT = CqlIdentifier.fromCql("rents_by_client");

    public static final CqlIdentifier RENT_ID = CqlIdentifier.fromCql("rent_id");
    public static final CqlIdentifier BEGIN_TIME = CqlIdentifier.fromCql("begin_time");
    public static final CqlIdentifier END_TIME = CqlIdentifier.fromCql("end_time");
    public static final CqlIdentifier RENT_COST = CqlIdentifier.fromCql("rent_cost");

    public static final CqlIdentifier CLIENTS = CqlIdentifier.fromCql("clients");
    public static final CqlIdentifier TYPE = CqlIdentifier.fromCql("type");
    public static final CqlIdentifier PERSONAL_ID = CqlIdentifier.fromCql("personal_id");
    public static final CqlIdentifier FIRST_NAME = CqlIdentifier.fromCql("first_name");
    public static final CqlIdentifier LAST_NAME = CqlIdentifier.fromCql("last_name");


    public static final CqlIdentifier VMS = CqlIdentifier.fromCql("vms");
    public static final CqlIdentifier VM_ID = CqlIdentifier.fromCql("vm_id");
    public static final CqlIdentifier DISCRIMINATOR = CqlIdentifier.fromCql("discriminator");
    public static final CqlIdentifier IS_RENTED = CqlIdentifier.fromCql("is_rented");
    public static final CqlIdentifier RAM = CqlIdentifier.fromCql("ram");
    public static final CqlIdentifier SOTRAGE = CqlIdentifier.fromCql("storage");
    public static final CqlIdentifier RENTAL_PRICE = CqlIdentifier.fromCql("rental_price");
    public static final CqlIdentifier CORES = CqlIdentifier.fromCql("cores");
    public static final CqlIdentifier SOCKETS = CqlIdentifier.fromCql("sockets");
}
