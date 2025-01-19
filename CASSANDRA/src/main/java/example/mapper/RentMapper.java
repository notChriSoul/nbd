package example.mapper;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.DaoTable;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import example.dao.RentDao;

@Mapper
public interface RentMapper {
    @DaoFactory
    RentDao getRentDao(@DaoKeyspace String keyspace, @DaoTable String table);

    @DaoFactory
    RentDao getRentDao();
}
