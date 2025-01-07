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
import example.schemas.VmSchema;

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
                        .setString(VmSchema.ID, normal.getVmId())
                        .setString(VmSchema.DISCRIMINATOR, normal.getDiscriminator())
                        .setBoolean(VmSchema.IS_RENTED, normal.isRented())
                        .setInt(VmSchema.RAM, normal.getRam())
                        .setInt(VmSchema.SOTRAGE, normal.getStorage())
                        .setInt(VmSchema.RENTAL_PRICE, normal.getRentalPrice())
                        .setInt(VmSchema.CORES, normal.getCores());
            }

            case "Pro" -> {
                Pro pro = (Pro) vm;
                yield session.prepare(proEntityHelper.insert().build())
                        .bind()
                        .setString(VmSchema.ID, pro.getVmId())
                        .setString(VmSchema.DISCRIMINATOR, pro.getDiscriminator())
                        .setBoolean(VmSchema.IS_RENTED, pro.isRented())
                        .setInt(VmSchema.RAM, pro.getRam())
                        .setInt(VmSchema.SOTRAGE, pro.getStorage())
                        .setInt(VmSchema.RENTAL_PRICE, pro.getRentalPrice())
                        .setInt(VmSchema.SOCKETS, pro.getSockets());
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


        String discriminator = row.getString(VmSchema.DISCRIMINATOR);

        return switch (discriminator) {
            case "Normal" -> getNormal(row);
            case "Pro" -> getPro(row);
            default -> throw new IllegalStateException("Unexpected value: " + discriminator);
        };
    }

    private Normal getNormal(Row row) {
        return new Normal(
                row.getString(VmSchema.ID),
                row.getInt(VmSchema.CORES),
                row.getString(VmSchema.DISCRIMINATOR),
                row.getBoolean(VmSchema.IS_RENTED),
                row.getInt(VmSchema.RAM),
                row.getInt(VmSchema.SOTRAGE),
                row.getInt(VmSchema.RENTAL_PRICE));
    }

    private Pro getPro(Row row) {
        return new Pro(
                row.getString(VmSchema.ID),
                row.getInt(VmSchema.SOCKETS),
                row.getString(VmSchema.DISCRIMINATOR),
                row.getBoolean(VmSchema.IS_RENTED),
                row.getInt(VmSchema.RAM),
                row.getInt(VmSchema.SOTRAGE),
                row.getInt(VmSchema.RENTAL_PRICE));
    }
}
