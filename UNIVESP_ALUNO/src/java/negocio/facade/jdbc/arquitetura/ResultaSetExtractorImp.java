package negocio.facade.jdbc.arquitetura;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class ResultaSetExtractorImp implements ResultSetExtractor<Integer> {

	@Override
	public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
		if(arg0.next()) {
			return arg0.getInt("codigo");
		}
		return null;
	}

}
