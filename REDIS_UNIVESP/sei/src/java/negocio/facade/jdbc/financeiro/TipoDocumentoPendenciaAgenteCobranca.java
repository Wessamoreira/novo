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
import negocio.comuns.financeiro.TipoDocumentoPendenciaAgenteCobrancaVO;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.TipoDocumentoPendenciaAgenteCobrancaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class TipoDocumentoPendenciaAgenteCobranca extends ControleAcesso implements TipoDocumentoPendenciaAgenteCobrancaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4088366380601844516L;
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<TipoDocumentoPendenciaAgenteCobrancaVO> lista, AgenteNegativacaoCobrancaContaReceberVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (TipoDocumentoPendenciaAgenteCobrancaVO tipoDocumentoPendenciaAgenteCobrancaVO : lista) {
			tipoDocumentoPendenciaAgenteCobrancaVO.setAgenteNegativacaoCobrancaContaReceberVO(obj);
			if(tipoDocumentoPendenciaAgenteCobrancaVO.getCodigo() == 0) {
				incluir(tipoDocumentoPendenciaAgenteCobrancaVO, verificarAcesso, usuarioVO);
			}else {
				alterar(tipoDocumentoPendenciaAgenteCobrancaVO, verificarAcesso, usuarioVO);
			}
		}
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TipoDocumentoPendenciaAgenteCobrancaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			AgenteNegativacaoCobrancaContaReceber.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			incluir(obj, "tipoDocumentoPendenciaAgenteCobranca", new AtributoPersistencia()
					.add("agenteNegativacaoCobrancaContaReceber", obj.getAgenteNegativacaoCobrancaContaReceberVO()) 
					.add("tipoDocumento", obj.getTipoDocumento()), usuarioVO);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TipoDocumentoPendenciaAgenteCobrancaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			AgenteNegativacaoCobrancaContaReceber.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			alterar(obj, "tipoDocumentoPendenciaAgenteCobranca", new AtributoPersistencia()
					.add("agenteNegativacaoCobrancaContaReceber", obj.getAgenteNegativacaoCobrancaContaReceberVO()) 
					.add("tipoDocumento", obj.getTipoDocumento()),new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<TipoDocumentoPendenciaAgenteCobrancaVO> consultarPorAgenteNegativacaoCobrancaContaReceberVO(AgenteNegativacaoCobrancaContaReceberVO obj, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder("");
		sb.append(" select tipoDocumentoPendenciaAgenteCobranca.codigo, tipoDocumentoPendenciaAgenteCobranca.agenteNegativacaoCobrancaContaReceber, ");
		sb.append(" tipodocumento.codigo as \"tipodocumento.codigo\", tipodocumento.nome as \"tipodocumento.nome\"  ");
		sb.append(" from  tipoDocumentoPendenciaAgenteCobranca ");
		sb.append(" inner join tipodocumento on tipodocumento.codigo =  tipoDocumentoPendenciaAgenteCobranca.tipodocumento ");
		sb.append(" where  tipoDocumentoPendenciaAgenteCobranca.agenteNegativacaoCobrancaContaReceber = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), obj.getCodigo());
		return (montarDadosConsulta(tabelaResultado, obj));
	}

	public List<TipoDocumentoPendenciaAgenteCobrancaVO> montarDadosConsulta(SqlRowSet tabelaResultado, AgenteNegativacaoCobrancaContaReceberVO obj) throws Exception {
		List<TipoDocumentoPendenciaAgenteCobrancaVO> vetResultado = new ArrayList<TipoDocumentoPendenciaAgenteCobrancaVO>(0);
		while (tabelaResultado.next()) {
			TipoDocumentoPendenciaAgenteCobrancaVO ancue = montarDados(tabelaResultado);
			ancue.setAgenteNegativacaoCobrancaContaReceberVO(obj);
			vetResultado.add(ancue);
		}
		return vetResultado;
	}

	public TipoDocumentoPendenciaAgenteCobrancaVO montarDados(SqlRowSet dadosSQL) throws Exception {
		TipoDocumentoPendenciaAgenteCobrancaVO obj = new TipoDocumentoPendenciaAgenteCobrancaVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getTipoDocumento().setCodigo(dadosSQL.getInt("tipodocumento.codigo"));
		obj.getTipoDocumento().setNome(dadosSQL.getString("tipodocumento.nome"));
		return obj;
	}

}
