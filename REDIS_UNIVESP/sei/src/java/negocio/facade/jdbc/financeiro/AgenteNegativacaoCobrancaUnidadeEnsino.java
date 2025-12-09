package negocio.facade.jdbc.financeiro;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaUnidadeEnsinoVO;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.AgenteNegativacaoCobrancaUnidadeEnsinoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class AgenteNegativacaoCobrancaUnidadeEnsino extends ControleAcesso implements AgenteNegativacaoCobrancaUnidadeEnsinoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1055776885850757337L;
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<AgenteNegativacaoCobrancaUnidadeEnsinoVO> lista, AgenteNegativacaoCobrancaContaReceberVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (AgenteNegativacaoCobrancaUnidadeEnsinoVO agenteNegativacaoCobrancaUnidadeEnsinoVO : lista) {
			agenteNegativacaoCobrancaUnidadeEnsinoVO.setAgenteNegativacaoCobrancaContaReceberVO(obj);
			if(agenteNegativacaoCobrancaUnidadeEnsinoVO.getCodigo() == 0) {
				incluir(agenteNegativacaoCobrancaUnidadeEnsinoVO, verificarAcesso, usuarioVO);
			}else {
				alterar(agenteNegativacaoCobrancaUnidadeEnsinoVO, verificarAcesso, usuarioVO);
			}
		}
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final AgenteNegativacaoCobrancaUnidadeEnsinoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			AgenteNegativacaoCobrancaContaReceber.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			incluir(obj, "agenteNegativacaoCobrancaUnidadeEnsino", new AtributoPersistencia()
					.add("agenteNegativacaoCobrancaContaReceber", obj.getAgenteNegativacaoCobrancaContaReceberVO()) 
					.add("unidadeEnsino", obj.getUnidadeEnsino()), usuarioVO);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AgenteNegativacaoCobrancaUnidadeEnsinoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			AgenteNegativacaoCobrancaContaReceber.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			alterar(obj, "agenteNegativacaoCobrancaUnidadeEnsino", new AtributoPersistencia()
					.add("agenteNegativacaoCobrancaContaReceber", obj.getAgenteNegativacaoCobrancaContaReceberVO()) 
					.add("unidadeEnsino", obj.getUnidadeEnsino()),new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<AgenteNegativacaoCobrancaUnidadeEnsinoVO> consultarPorAgenteNegativacaoCobrancaContaReceberVO(AgenteNegativacaoCobrancaContaReceberVO obj, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder("");
		sb.append(" select agenteNegativacaoCobrancaUnidadeEnsino.codigo, agenteNegativacaoCobrancaUnidadeEnsino.agenteNegativacaoCobrancaContaReceber, ");
		sb.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\"  ");
		sb.append(" from  agenteNegativacaoCobrancaUnidadeEnsino ");
		sb.append(" inner join unidadeensino on unidadeensino.codigo =  agenteNegativacaoCobrancaUnidadeEnsino.unidadeensino ");
		sb.append(" where  agenteNegativacaoCobrancaUnidadeEnsino.agenteNegativacaoCobrancaContaReceber = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), obj.getCodigo());
		return (montarDadosConsulta(tabelaResultado, obj));
	}

	public List<AgenteNegativacaoCobrancaUnidadeEnsinoVO> montarDadosConsulta(SqlRowSet tabelaResultado, AgenteNegativacaoCobrancaContaReceberVO obj) throws Exception {
		List<AgenteNegativacaoCobrancaUnidadeEnsinoVO> vetResultado = new ArrayList<AgenteNegativacaoCobrancaUnidadeEnsinoVO>(0);
		while (tabelaResultado.next()) {
			AgenteNegativacaoCobrancaUnidadeEnsinoVO ancue = montarDados(tabelaResultado);
			ancue.setAgenteNegativacaoCobrancaContaReceberVO(obj);
			vetResultado.add(ancue);
		}
		return vetResultado;
	}

	public AgenteNegativacaoCobrancaUnidadeEnsinoVO montarDados(SqlRowSet dadosSQL) throws Exception {
		AgenteNegativacaoCobrancaUnidadeEnsinoVO obj = new AgenteNegativacaoCobrancaUnidadeEnsinoVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino.codigo"));
		obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino.nome"));
		
		return obj;
	}

}
