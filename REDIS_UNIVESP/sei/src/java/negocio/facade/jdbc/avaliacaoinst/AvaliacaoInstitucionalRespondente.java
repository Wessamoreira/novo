package negocio.facade.jdbc.avaliacaoinst;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalRespondenteVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.avaliacaoinst.RespostaAvaliacaoInstitucionalDWVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.avaliacaoinst.AvaliacaoInstitucionalRespondenteInterfaceFacade;
import relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstitucionalAnaliticoRelVO;

@Repository
@Scope("singleton")
@Lazy
public class AvaliacaoInstitucionalRespondente  extends ControleAcesso implements AvaliacaoInstitucionalRespondenteInterfaceFacade {

	private static final long serialVersionUID = 7476696490267642358L;
	
	protected static String idEntidade;

	public AvaliacaoInstitucionalRespondente() throws Exception {
		super();
		setIdEntidade("AvaliacaoInstitucionalRespondente");
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<AvaliacaoInstitucionalAnaliticoRelVO> lista, Integer codigoAvaliacaoIntitucional, UsuarioVO usuarioVO) throws Exception {
		for( AvaliacaoInstitucionalAnaliticoRelVO avaliacaoInstitucionalAnaliticoRelVO : lista) {
			incluir(montarDadosAvaliacaoInstitucionalRespondente(avaliacaoInstitucionalAnaliticoRelVO,codigoAvaliacaoIntitucional), usuarioVO);
		}
	}

	private void incluir(AvaliacaoInstitucionalRespondenteVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			AvaliacaoInstitucionalRespondente.incluir(getIdEntidade(), false, usuarioVO);
			incluir(obj, "AvaliacaoInstitucionalRespondente", new AtributoPersistencia()
					.add("matricula", Uteis.isAtributoPreenchido(obj.getMatricula()) ? obj.getMatricula() : null )
					.add("pessoa", obj.getPessoa())
					.add("respondido", obj.getJrespondeu())
					.add("avaliacaoinstitucional", obj.getAvaliacaoInstitucionalVO()), usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}
	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void alterarSituacaoAvaliacaoRespondente(RespostaAvaliacaoInstitucionalDWVO ra, Boolean situacao, UsuarioVO usuario) throws Exception {
		alterar(new AvaliacaoInstitucionalRespondenteVO(), "AvaliacaoInstitucionalRespondente",
				new AtributoPersistencia().add("respondido",situacao),
				new AtributoPersistencia().add("pessoa", ra.getPessoa()).add("avaliacaoinstitucional", ra.getAvaliacaoInstitucional()), usuario);
		
	}


	public static String getIdEntidade() {
		return idEntidade;
	}


	public static void setIdEntidade(String idEntidade) {
		AvaliacaoInstitucionalRespondente.idEntidade = idEntidade;
	}
	
	private AvaliacaoInstitucionalRespondenteVO montarDadosAvaliacaoInstitucionalRespondente(AvaliacaoInstitucionalAnaliticoRelVO avaliacaoInstitucionalAnaliticoRelVO, Integer codigoAvalicao) {
		AvaliacaoInstitucionalRespondenteVO obj = new AvaliacaoInstitucionalRespondenteVO();
		obj.setPessoa(avaliacaoInstitucionalAnaliticoRelVO.getUsuarioVO().getPessoa().getCodigo());
		obj.setMatricula(avaliacaoInstitucionalAnaliticoRelVO.getMatricula());
		obj.setAvaliacaoInstitucionalVO(codigoAvalicao);
		obj.setJrespondeu(avaliacaoInstitucionalAnaliticoRelVO.getJaRespondeu());
		return obj;
	}


	@Override
	public boolean consultarAvaliacaoInstitucionalRespondenteExistentePorAvaliacaoInstitucional(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, Integer pessoa, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT * FROM AvaliacaoInstitucionalRespondente WHERE avaliacaoInstitucional = " +avaliacaoInstitucionalVO.getCodigo();
		if (Uteis.isAtributoPreenchido(pessoa)) {
			sqlStr += " AND pessoa = " + pessoa;
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}
	
	@Override
	public void atualizarBaseAvaliacaoInstitucionalRespondente(List<AvaliacaoInstitucionalVO> institucionalVOs, UsuarioVO usuarioVO) throws Exception {
		for(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO : institucionalVOs) {
			List<AvaliacaoInstitucionalAnaliticoRelVO> lista =  getFacadeFactory().getAvaliacaoInstitucionalAnaliticoRelFacade().realizarGeracaoRelatorioAnalitico(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo(), avaliacaoInstitucionalVO, avaliacaoInstitucionalVO.getCurso().getCodigo(), 0, avaliacaoInstitucionalVO.getTurma().getCodigo(), "TODAS", "pessoa.nome", avaliacaoInstitucionalVO.getDataInicio(), avaliacaoInstitucionalVO.getDataFinal(), false, usuarioVO, true);
			getFacadeFactory().getAvaliacaoInstitucionalRespondenteInterfaceFacade().persistir(lista, avaliacaoInstitucionalVO.getCodigo(), usuarioVO);
		}
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirAvaliacaoRespondenteNaoRespondido(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuarioLogado) throws Exception {
		try {
			String sql = "DELETE FROM avaliacaoinstitucionalrespondente WHERE avaliacaoinstitucional = ? and respondido = false" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
			getConexao().getJdbcTemplate().update(sql, new Object[] { avaliacaoInstitucionalVO.getCodigo() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
