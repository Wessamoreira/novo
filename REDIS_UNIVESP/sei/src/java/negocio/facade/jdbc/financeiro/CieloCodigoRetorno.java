package negocio.facade.jdbc.financeiro;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.financeiro.CieloCodigoRetornoVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.CieloCodigoRetornoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class CieloCodigoRetorno extends ControleAcesso implements CieloCodigoRetornoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	
	@Override
	public CieloCodigoRetornoVO consultarPorCodigoRetorno(String codigoRetorno) {
		StringBuilder sb = new StringBuilder();
		sb.append("select * from cieloCodigoRetorno where codigoResposta = '").append(codigoRetorno).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		CieloCodigoRetornoVO obj = new CieloCodigoRetornoVO();
		if (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setCodigoResposta(tabelaResultado.getString("codigoResposta"));
			obj.setSignificado(tabelaResultado.getString("significado"));
			obj.setAcao(tabelaResultado.getString("acao"));
			obj.setPermiteRetentativa(tabelaResultado.getString("permiteRetentativa"));
		}
		return obj;
	}
}
