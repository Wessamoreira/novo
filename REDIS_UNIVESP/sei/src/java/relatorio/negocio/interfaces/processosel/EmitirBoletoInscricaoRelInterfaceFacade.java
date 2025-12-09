package relatorio.negocio.interfaces.processosel;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public interface EmitirBoletoInscricaoRelInterfaceFacade {

	public String emitirRelatorio(Integer codigo) throws Exception;

	public SqlRowSet executarConsultaParametrizada(Integer codigo) throws Exception;

}