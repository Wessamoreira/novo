package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.select.Collector;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ProcessoMatriculaUnidadeEnsinoVO;
import negocio.comuns.academico.ProgramacaoFormaturaUnidadeEnsinoVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ProgramacaoFormaturaUnidadeEnsinoInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class ProgramacaoFormaturaUnidadeEnsino extends ControleAcesso implements ProgramacaoFormaturaUnidadeEnsinoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "programacaoFormaturaUnidaEnsino";
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ProgramacaoFormaturaVO programacaoFormaturaVO, UsuarioVO usuarioVO) throws Exception {
		List<ProgramacaoFormaturaUnidadeEnsinoVO> lista = programacaoFormaturaVO.getProgramacaoFormaturaUnidadeEnsinoVOs().stream().filter(pf -> pf.getUnidadeSelecionado()).collect(Collectors.toList());
		for (ProgramacaoFormaturaUnidadeEnsinoVO programacaoFormaturaUnidadeEnsinoVO : lista) {
			if (programacaoFormaturaUnidadeEnsinoVO.getSelecionado()) {
				if (!Uteis.isAtributoPreenchido(programacaoFormaturaUnidadeEnsinoVO.getCodigo())) {
					programacaoFormaturaUnidadeEnsinoVO.setProgramacaoFormaturaVO(programacaoFormaturaVO);
					incluir(programacaoFormaturaUnidadeEnsinoVO, usuarioVO);
				}
			} 
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(ProgramacaoFormaturaUnidadeEnsinoVO programacaoFormaturaUnidadeEnsinoVO, UsuarioVO usuarioVO) throws Exception {
		incluir(programacaoFormaturaUnidadeEnsinoVO, TABLE_NAME, new AtributoPersistencia().add("programacaoformatura", programacaoFormaturaUnidadeEnsinoVO.getProgramacaoFormaturaVO()).add("unidadeEnsino", programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO()), usuarioVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(Integer codigoProgramacao, UsuarioVO usuarioVO) throws Exception {
		getConexao().getJdbcTemplate().update("delete from programacaoformaturaunidaensino where programacaoformatura = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), codigoProgramacao);
	}

	@Override
	public Boolean consultarProgramacaoFormaturaUnidadeEnsinoVinculadoMatricula(Integer programacaoFormatura, Integer unidadeEnsino) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProgramacaoFormaturaUnidadeEnsinoVO> consultarPorProgramacaoFormatura(Integer programacaoFormatura) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void carregarUnidadeEnsinoNaoSelecionado(ProgramacaoFormaturaVO programacaoFormaturaVO, Integer unidadeEnsinoLogado) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append(" select 0 as codigo, 0 as programacaoFormatura, unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\"  ");		
		sb.append(" from unidadeEnsino ");
		sb.append(" where not exists (select codigo from programacaoformaturaunidaensino where unidadeEnsino.codigo = programacaoformaturaunidaensino.unidadeEnsino ");		
		sb.append(" and programacaoformaturaunidaensino.programacaoformatura = ? ) ");
		if(Uteis.isAtributoPreenchido(unidadeEnsinoLogado)) {
			sb.append(" and unidadeEnsino.codigo =  ").append(unidadeEnsinoLogado);
		}
		sb.append(" order by unidadeEnsino.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), programacaoFormaturaVO.getCodigo());
		programacaoFormaturaVO.getProgramacaoFormaturaUnidadeEnsinoVOs().addAll(montarDadosConsulta(rs));
	}

	private List<ProgramacaoFormaturaUnidadeEnsinoVO> montarDadosConsulta(SqlRowSet rs) {
		List<ProgramacaoFormaturaUnidadeEnsinoVO> programacaoFormaturaUnidadeEnsinoVOs = new ArrayList<ProgramacaoFormaturaUnidadeEnsinoVO>();
		while (rs.next()) {
			programacaoFormaturaUnidadeEnsinoVOs.add(montarDados(rs));
		}
		return programacaoFormaturaUnidadeEnsinoVOs;
	}

	private ProgramacaoFormaturaUnidadeEnsinoVO montarDados(SqlRowSet rs) {
		ProgramacaoFormaturaUnidadeEnsinoVO programacaoFormaturaUnidadeEnsinoVO = new ProgramacaoFormaturaUnidadeEnsinoVO();
		programacaoFormaturaUnidadeEnsinoVO.setNovoObj(rs.getInt("codigo") > 0);
		if (rs.getInt("codigo") > 0) {
			programacaoFormaturaUnidadeEnsinoVO.setSelecionado(true);
		} else {
			programacaoFormaturaUnidadeEnsinoVO.setSelecionado(false);
		}
		programacaoFormaturaUnidadeEnsinoVO.setCodigo(rs.getInt("codigo"));
		programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().setCodigo(rs.getInt("unidadeEnsino.codigo"));
		programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().setNome(rs.getString("unidadeEnsino.nome"));
		programacaoFormaturaUnidadeEnsinoVO.getProgramacaoFormaturaVO().setCodigo(rs.getInt("programacaoFormatura"));
		return programacaoFormaturaUnidadeEnsinoVO;
	}
	
	private StringBuilder getSqlPadrao() {
		StringBuilder sb = new StringBuilder("");
		sb.append("select programacaoformaturaunidaensino.codigo, unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\", ");
		sb.append(" programacaoformaturaunidaensino.programacaoformatura ");
		sb.append(" from programacaoformaturaunidaensino ");
		sb.append(" inner join unidadeEnsino on unidadeEnsino.codigo = programacaoformaturaunidaensino.unidadeEnsino ");
		return sb;
	}

	@Override
	public List<ProgramacaoFormaturaUnidadeEnsinoVO> consultarPorProgramacaoMatricula(Integer programacaoMatricula) throws Exception {
		StringBuilder sb = new StringBuilder(getSqlPadrao());
		sb.append(" where programacaoformaturaunidaensino.programacaoformatura = ? order by unidadeEnsino.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), programacaoMatricula);		
		return montarDadosConsulta(rs);
	
	}
	
}
