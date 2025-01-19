package example.provider;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchType;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.datastax.oss.driver.api.querybuilder.term.Term;
import com.datastax.oss.driver.api.querybuilder.update.Update;
import example.model.Rent;
import example.schemas.CQLSchema;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.*;

public class RentProvider {
    private final CqlSession session;

    public RentProvider(MapperContext ctx) {
        this.session = ctx.getSession();
    }

    public void create(Rent rent) {
        Term beginTime = toTimestamp(now());
        Insert rents_by_client = QueryBuilder.insertInto(CQLSchema.RENTS_BY_CLIENT)
                .value(CQLSchema.PERSONAL_ID, literal(rent.getClientID()))
                .value(CQLSchema.RENT_ID, literal(rent.getRentID()))
                .value(CQLSchema.BEGIN_TIME, beginTime)
                .value(CQLSchema.END_TIME, literal(0))
                .value(CQLSchema.VM_ID, literal(rent.getVmID()))
                .value(CQLSchema.RENT_COST, literal(rent.getRentCost()))
                .ifNotExists();

        Insert rents_by_vm = QueryBuilder.insertInto(CQLSchema.RENTS_BY_VM)
                .value(CQLSchema.VM_ID, literal(rent.getVmID()))
                .value(CQLSchema.RENT_ID, literal(rent.getRentID()))
                .value(CQLSchema.BEGIN_TIME, beginTime)
                .value(CQLSchema.END_TIME, literal(0))
                .value(CQLSchema.PERSONAL_ID, literal(rent.getClientID()))
                .value(CQLSchema.RENT_COST, literal(rent.getRentCost()))
                .ifNotExists();

        session.execute(rents_by_client.build());
        session.execute(rents_by_vm.build());
    }

    public void endRent(Rent rent) {
        Term endTime = toTimestamp(now());
        Update rents_by_client = QueryBuilder.update(CQLSchema.RENTS_BY_CLIENT)
                .setColumn(CQLSchema.END_TIME, endTime)
                .where(Relation.column(CQLSchema.PERSONAL_ID).isEqualTo(literal(rent.getClientID())))
                .where(Relation.column(CQLSchema.RENT_ID).isEqualTo(literal(rent.getRentID())));

        Update rents_by_vm = QueryBuilder.update(CQLSchema.RENTS_BY_VM)
                .setColumn(CQLSchema.END_TIME, endTime)
                .where(Relation.column(CQLSchema.VM_ID).isEqualTo(literal(rent.getVmID())))
                .where(Relation.column(CQLSchema.RENT_ID).isEqualTo(literal(rent.getRentID())));

        BatchStatement batch = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(rents_by_client.build())
                .addStatement(rents_by_vm.build())
                .build();
        session.execute(batch);
    }

    public List<Rent> findAllByTable(CqlIdentifier table) {
        List<Rent> rents = new ArrayList<>();
        Select select = QueryBuilder.selectFrom(table).all();
        ResultSet row = session.execute(select.build());
        row.forEach(rent -> rents.add(convert(rent)));
        return rents;
    }


    public List<Rent> findByClientId(String clientID) {
        List<Rent> rents = new ArrayList<>();
        Select select = QueryBuilder.selectFrom(CQLSchema.RENTS_BY_CLIENT).all()
                .whereColumn(CQLSchema.PERSONAL_ID).isEqualTo(literal(clientID));
        ResultSet row = session.execute(select.build());
        row.forEach(rent -> rents.add(convert(rent)));
        return rents;
    }

    public List<Rent> findByVMachineId(String vmID) {
        List<Rent> rents = new ArrayList<>();
        Select select = QueryBuilder.selectFrom(CQLSchema.RENTS_BY_VM).all()
                .whereColumn(CQLSchema.VM_ID).isEqualTo(literal(vmID));
        ResultSet row = session.execute(select.build());
        row.forEach(rent -> rents.add(convert(rent)));
        return rents;
    }

    private Rent convert(Row row) {

        return Rent.build(
                row.getString(CQLSchema.RENT_ID),
                row.getString(CQLSchema.PERSONAL_ID),
                row.getString(CQLSchema.VM_ID),
                localDateTimeFromField(row, CQLSchema.BEGIN_TIME),
                localDateTimeFromField(row, CQLSchema.END_TIME),
                row.getDouble(CQLSchema.RENT_COST)
        );
    }

    private LocalDateTime localDateTimeFromField(Row row, CqlIdentifier field) {
        if(row.getInstant(field) == null) {
            return null;
        } else {
            return LocalDateTime.ofInstant(Objects.requireNonNull(row.getInstant(field)), ZoneOffset.UTC);
        }
    }




}
