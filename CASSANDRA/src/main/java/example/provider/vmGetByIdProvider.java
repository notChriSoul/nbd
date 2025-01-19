package example.provider;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import example.model.vms.Normal;
import example.model.vms.Pro;
import example.model.vms.VirtualMachine;
import example.schemas.SchemaConst;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class vmGetByIdProvider {
    private final CqlSession session;

    private EntityHelper<Normal> normalEntityHelper;
    private EntityHelper<Pro> proEntityHelper;

    public vmGetByIdProvider(MapperContext ctx, EntityHelper<Normal> normalEntityHelper, EntityHelper<Pro> proEntityHelper) {
        this.session = ctx.getSession();
        this.normalEntityHelper = normalEntityHelper;
        this.proEntityHelper = proEntityHelper;
    }

    public void create(VirtualMachine vm) {
        session.execute(
        switch (vm.getDiscriminator()) {
            case "Normal" -> {
                Normal normal = (Normal) vm;
                yield session.prepare(normalEntityHelper.insert().build())
                        .bind()
                        .setString(SchemaConst.VM_ID, normal.getVmId())
                        .setString(SchemaConst.DISCRIMINATOR, normal.getDiscriminator())
                        .setBoolean(SchemaConst.IS_RENTED, normal.isRented())
                        .setInt(SchemaConst.RAM, normal.getRam())
                        .setInt(SchemaConst.SOTRAGE, normal.getStorage())
                        .setInt(SchemaConst.RENTAL_PRICE, normal.getRentalPrice())
                        .setInt(SchemaConst.CORES, normal.getCores());
            }

            case "Pro" -> {
                Pro pro = (Pro) vm;
                yield session.prepare(proEntityHelper.insert().build())
                        .bind()
                        .setString(SchemaConst.VM_ID, pro.getVmId())
                        .setString(SchemaConst.DISCRIMINATOR, pro.getDiscriminator())
                        .setBoolean(SchemaConst.IS_RENTED, pro.isRented())
                        .setInt(SchemaConst.RAM, pro.getRam())
                        .setInt(SchemaConst.SOTRAGE, pro.getStorage())
                        .setInt(SchemaConst.RENTAL_PRICE, pro.getRentalPrice())
                        .setInt(SchemaConst.SOCKETS, pro.getSockets());
            }

            default -> throw new IllegalStateException("Unexpected value: " + vm.getDiscriminator());
        }        );
    }

    public VirtualMachine findById(String id) {
        Select selectVm = QueryBuilder
                .selectFrom(CqlIdentifier.fromCql(SchemaConst.VMS))
                .all()
                .where(Relation.column("vm_id").isEqualTo(literal(id)));

        Row row = session.execute(selectVm.build()).one();


        try {
            String discriminator = row.getString(SchemaConst.DISCRIMINATOR);
            return switch (discriminator) {
                case "Normal" -> getNormal(row);
                case "Pro" -> getPro(row);
                default -> throw new IllegalStateException("Unexpected value: " + discriminator);
            };
        } catch (NullPointerException e) {
            return null;
        }
    }

    private Normal getNormal(Row row) {
        return new Normal(
                row.getString(SchemaConst.VM_ID),
                row.getInt(SchemaConst.CORES),
                row.getString(SchemaConst.DISCRIMINATOR),
                row.getBoolean(SchemaConst.IS_RENTED),
                row.getInt(SchemaConst.RAM),
                row.getInt(SchemaConst.SOTRAGE),
                row.getInt(SchemaConst.RENTAL_PRICE));
    }

    private Pro getPro(Row row) {
        return new Pro(
                row.getString(SchemaConst.VM_ID),
                row.getInt(SchemaConst.SOCKETS),
                row.getString(SchemaConst.DISCRIMINATOR),
                row.getBoolean(SchemaConst.IS_RENTED),
                row.getInt(SchemaConst.RAM),
                row.getInt(SchemaConst.SOTRAGE),
                row.getInt(SchemaConst.RENTAL_PRICE));
    }
}
