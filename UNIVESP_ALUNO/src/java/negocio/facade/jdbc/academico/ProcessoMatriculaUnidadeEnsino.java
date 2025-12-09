package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ProcessoMatriculaUnidadeEnsinoVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ProcessoMatriculaUnidadeEnsinoInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class ProcessoMatriculaUnidadeEnsino extends ControleAcesso
		implements ProcessoMatriculaUnidadeEnsinoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4758943165619533891L;
	private static final String  TABLE_NAME = "processoMatriculaUnidadeEnsino";

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ProcessoMatriculaVO processoMatriculaVO, UsuarioVO usuarioVO) throws Exception {
		for(ProcessoMatriculaUnidadeEnsinoVO processoMatriculaUnidadeEnsinoVO: processoMatriculaVO.getProcessoMatriculaUnidadeEnsinoVOs()) {
			if(processoMatriculaUnidadeEnsinoVO.getSelecionado()) {
				if(!Uteis.isAtributoPreenchido(processoMatriculaUnidadeEnsinoVO.getCodigo())) {
					processoMatriculaUnidadeEnsinoVO.setProcessoMatriculaVO(processoMatriculaVO);
					incluir(processoMatriculaUnidadeEnsinoVO, usuarioVO);
				}				
			}else if(Uteis.isAtributoPreenchido(processoMatriculaUnidadeEnsinoVO.getCodigo())) {
				if(!consultarProcessoMatriculaUnidadeEnsinoVinculadoMatricula(processoMatriculaUnidadeEnsinoVO.getProcessoMatriculaVO().getCodigo(), processoMatriculaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo())) {
					excluir(processoMatriculaUnidadeEnsinoVO, usuarioVO);
				}else {
					throw new Exception(UteisJSF.internacionalizar("msg_processoMatriculaUnidadeEnsino_jaVinculado").replace("{0}", processoMatriculaUnidadeEnsinoVO.getUnidadeEnsinoVO().getNome()));
				}
			}			
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(ProcessoMatriculaUnidadeEnsinoVO processoMatriculaUnidadeEnsinoVO, UsuarioVO usuarioVO) throws Exception {
		incluir(processoMatriculaUnidadeEnsinoVO, TABLE_NAME, 
				new AtributoPersistencia().add("processoMatricula", processoMatriculaUnidadeEnsinoVO.getProcessoMatriculaVO())
				.add("unidadeEnsino", processoMatriculaUnidadeEnsinoVO.getUnidadeEnsinoVO()), usuarioVO);

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ProcessoMatriculaUnidadeEnsinoVO processoMatriculaUnidadeEnsinoVO, UsuarioVO usuarioVO) throws Exception {
		getConexao().getJdbcTemplate().update("delete from processoMatriculaUnidadeEnsino where processoMatricula = ? and unidadeEnsino = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), processoMatriculaUnidadeEnsinoVO.getProcessoMatriculaVO().getCodigo(), processoMatriculaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo());
	}

	@Override
	public Boolean consultarProcessoMatriculaUnidadeEnsinoVinculadoMatricula(Integer processoMatricula,
			Integer unidadeEnsino) throws Exception {
		return getConexao().getJdbcTemplate().queryForRowSet("select matriculaperiodo.codigo from matriculaperiodo inner join unidadeensinocurso on  unidadeensinocurso.codigo = matriculaperiodo.unidadeensinocurso where matriculaperiodo.processoMatricula = ? and unidadeensinocurso.unidadeEnsino = ? limit 1", processoMatricula, unidadeEnsino).next();		
	}

	@Override
	public List<ProcessoMatriculaUnidadeEnsinoVO> consultarPorProcessoMatricula(Integer processoMatricula)throws Exception {
		StringBuilder sb = new StringBuilder(getSqlPadrao());
		sb.append(" where processoMatriculaUnidadeEnsino.processoMatricula = ? order by unidadeEnsino.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), processoMatricula);		
		return montarDadosConsulta(rs);
	}
	
	private List<ProcessoMatriculaUnidadeEnsinoVO> montarDadosConsulta(SqlRowSet rs) throws Exception{
		List<ProcessoMatriculaUnidadeEnsinoVO> processoMatriculaUnidadeEnsinoVOs =  new ArrayList<ProcessoMatriculaUnidadeEnsinoVO>(0);
		while(rs.next()) {
			processoMatriculaUnidadeEnsinoVOs.add(montarDados(rs));
		}
		return processoMatriculaUnidadeEnsinoVOs;		
	}
	
	private ProcessoMatriculaUnidadeEnsinoVO montarDados(SqlRowSet rs) throws Exception{
		ProcessoMatriculaUnidadeEnsinoVO processoMatriculaUnidadeEnsinoVO =  new ProcessoMatriculaUnidadeEnsinoVO();
		processoMatriculaUnidadeEnsinoVO.setNovoObj(rs.getInt("codigo") > 0);
		processoMatriculaUnidadeEnsinoVO.setSelecionado(rs.getInt("codigo") > 0);
		processoMatriculaUnidadeEnsinoVO.setCodigo(rs.getInt("codigo"));
		processoMatriculaUnidadeEnsinoVO.getUnidadeEnsinoVO().setCodigo(rs.getInt("unidadeEnsino.codigo"));
		processoMatriculaUnidadeEnsinoVO.getUnidadeEnsinoVO().setNome(rs.getString("unidadeEnsino.nome"));
		processoMatriculaUnidadeEnsinoVO.getProcessoMatriculaVO().setCodigo(rs.getInt("processoMatricula"));
		return processoMatriculaUnidadeEnsinoVO;		
	}
	
	private StringBuilder getSqlPadrao() {
		StringBuilder sb = new StringBuilder("");
		sb.append("select processoMatriculaUnidadeEnsino.codigo, unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\", ");
		sb.append(" processoMatriculaUnidadeEnsino.processoMatricula ");
		sb.append(" from processoMatriculaUnidadeEnsino ");
		sb.append(" inner join unidadeEnsino on unidadeEnsino.codigo = processoMatriculaUnidadeEnsino.unidadeEnsino ");
		return sb;
	}
		
	@Override
	public void carregarUnidadeEnsinoNaoSelecionado(ProcessoMatriculaVO processoMatriculaVO, Integer unidadeEnsinoLogado) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append(" select 0 as codigo, 0 as processoMatricula, unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\"  ");		
		sb.append(" from unidadeEnsino ");
		sb.append(" where not exists (select codigo from processoMatriculaUnidadeEnsino where unidadeEnsino.codigo = processoMatriculaUnidadeEnsino.unidadeEnsino ");		
		sb.append(" and processoMatriculaUnidadeEnsino.processoMatricula = ? ) order by unidadeEnsino.nome  ");
		if(Uteis.isAtributoPreenchido(unidadeEnsinoLogado)) {
			sb.append(" and unidadeEnsino.codigo =  ").append(unidadeEnsinoLogado);
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), processoMatriculaVO.getCodigo());
		processoMatriculaVO.getProcessoMatriculaUnidadeEnsinoVOs().addAll(montarDadosConsulta(rs));
//		Ordenacao.ordenarLista(processoMatriculaVO.getProcessoMatriculaUnidadeEnsinoVOs(), "unidadeEnsinoVO.nome");
	}

}
