package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HistoricoNotaParcialVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.TurmaDisciplinaNotaParcialVO;
import negocio.comuns.academico.TurmaDisciplinaNotaTituloVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.TurmaDisciplinaNotaTituloInterfaceFacade;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

@Repository
@Scope("singleton")
@Lazy
public class TurmaDisciplinaNotaTitulo extends ControleAcesso implements TurmaDisciplinaNotaTituloInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -849714577971675631L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<TurmaDisciplinaNotaTituloVO> turmaDisciplinaNotaTituloVOs, List<HistoricoVO> listaHistoricoVOs, TurmaVO turmaLancamentoNotaVO, UsuarioVO usuarioVO,  boolean trazerAlunosTransferenciaMatriz, boolean permiteLancarNotaDisciplinaComposta) throws Exception {
		for(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO: turmaDisciplinaNotaTituloVOs){			
			if(!turmaDisciplinaNotaTituloVO.getTitulo().trim().isEmpty()){				
				alterar(turmaDisciplinaNotaTituloVO, listaHistoricoVOs, turmaLancamentoNotaVO, usuarioVO,  trazerAlunosTransferenciaMatriz, permiteLancarNotaDisciplinaComposta);
			}else {				
				excluir(turmaDisciplinaNotaTituloVO, usuarioVO);
			}
		}

	}
	
//	private boolean validarDadosAptoInclusao(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO){
//		if(!Uteis.isAtributoPreenchido(turmaDisciplinaNotaTituloVO.getTurmaVO())){
//			return false;
//		}
//		if(!Uteis.isAtributoPreenchido(turmaDisciplinaNotaTituloVO.getDisciplinaVO())){
//			return false;
//		}
//		if(!Uteis.isAtributoPreenchido(turmaDisciplinaNotaTituloVO.getConfiguracaoAcademicoVO())){
//			return false;
//		}
//		if(turmaDisciplinaNotaTituloVO.getAno().trim().isEmpty() && !turmaDisciplinaNotaTituloVO.getTurmaVO().getIntegral()){
//			return false;
//		}
//		if(turmaDisciplinaNotaTituloVO.getSemestre().trim().isEmpty() && turmaDisciplinaNotaTituloVO.getTurmaVO().getSemestral()){
//			return false;
//		}
//		if(turmaDisciplinaNotaTituloVO.getTitulo().trim().isEmpty()){
//			return false;
//		}
//		return true;
//	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql =  new StringBuilder("delete from turmaDisciplinaNotaTitulo where turma = ? and disciplina = ? and ano = ? and semestre = ? and configuracaoacademico = ? and nota = ? ");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), turmaDisciplinaNotaTituloVO.getTurmaVO().getCodigo(), turmaDisciplinaNotaTituloVO.getDisciplinaVO().getCodigo(), turmaDisciplinaNotaTituloVO.getAno(), turmaDisciplinaNotaTituloVO.getSemestre(), turmaDisciplinaNotaTituloVO.getConfiguracaoAcademicoVO().getCodigo(), turmaDisciplinaNotaTituloVO.getNota().name());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO, List<HistoricoVO> listaHistoricoVOs, TurmaVO turmaLancamentoNotaVO, final UsuarioVO usuarioVO,  boolean trazerAlunosTransferenciaMatriz, boolean permiteLancarNotaDisciplinaComposta) throws Exception {
		final String sql = "INSERT INTO turmaDisciplinaNotaTitulo( turma, disciplina, ano, semestre, configuracaoacademico, titulo, nota, data, usuario, possuiFormula, formula ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		turmaDisciplinaNotaTituloVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setInt(1, turmaDisciplinaNotaTituloVO.getTurmaVO().getCodigo());
				sqlInserir.setInt(2, turmaDisciplinaNotaTituloVO.getDisciplinaVO().getCodigo());
				sqlInserir.setString(3, turmaDisciplinaNotaTituloVO.getAno());
				sqlInserir.setString(4, turmaDisciplinaNotaTituloVO.getSemestre());
				sqlInserir.setInt(5, turmaDisciplinaNotaTituloVO.getConfiguracaoAcademicoVO().getCodigo());
				sqlInserir.setString(6, turmaDisciplinaNotaTituloVO.getTitulo());
				sqlInserir.setString(7, turmaDisciplinaNotaTituloVO.getNota().name());
				sqlInserir.setTimestamp(8, Uteis.getDataJDBCTimestamp(turmaDisciplinaNotaTituloVO.getData()));
				sqlInserir.setInt(9, usuarioVO.getCodigo());
				sqlInserir.setBoolean(10, turmaDisciplinaNotaTituloVO.getPossuiFormula());
				sqlInserir.setString(11, turmaDisciplinaNotaTituloVO.getFormula());
				return sqlInserir;
			}
		}, new ResultSetExtractor<Integer>() {

			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					turmaDisciplinaNotaTituloVO.setNovoObj(Boolean.FALSE);
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		if(!turmaDisciplinaNotaTituloVO.getTurmaDisciplinaNotaTituloVOs().isEmpty()){
			for(TurmaDisciplinaNotaTituloVO tituloVO: turmaDisciplinaNotaTituloVO.getTurmaDisciplinaNotaTituloVOs()){
				tituloVO.setTitulo(turmaDisciplinaNotaTituloVO.getTitulo());
			}
			persistir(turmaDisciplinaNotaTituloVO.getTurmaDisciplinaNotaTituloVOs(), listaHistoricoVOs, turmaLancamentoNotaVO, usuarioVO,  trazerAlunosTransferenciaMatriz, permiteLancarNotaDisciplinaComposta);
		}
		if(Uteis.isAtributoPreenchido(turmaDisciplinaNotaTituloVO.getTurmaDisciplinaNotaParcialVOs())) {
//			List<HistoricoVO> lista = null;
//			lista = getFacadeFactory().getHistoricoFacade().consultaRapidaHistoricoPorDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHist(turmaDisciplinaNotaTituloVO.getDisciplinaVO().getCodigo(), turmaLancamentoNotaVO, turmaDisciplinaNotaTituloVO.getAno(), turmaDisciplinaNotaTituloVO.getSemestre(),false, turmaDisciplinaNotaTituloVO.getConfiguracaoAcademicoVO(), Uteis.NIVELMONTARDADOS_TODOS,  usuarioVO, trazerAlunosTransferenciaMatriz, permiteLancarNotaDisciplinaComposta);
			for (TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcialVO : turmaDisciplinaNotaTituloVO.getTurmaDisciplinaNotaParcialVOs()) {
				turmaDisciplinaNotaParcialVO.setTurmaDisciplinaNotaTituloVO(turmaDisciplinaNotaTituloVO);
				if(!Uteis.isAtributoPreenchido(turmaDisciplinaNotaParcialVO.getCodigo())) {
					getFacadeFactory().getTurmaDisciplinaNotaParcialInterfaceFacade().incluir(turmaDisciplinaNotaParcialVO, usuarioVO);
					
					for (HistoricoVO historicoVO : listaHistoricoVOs) {
						HistoricoNotaParcialVO historicoNotaParcialVO = new HistoricoNotaParcialVO();
						historicoNotaParcialVO.setHistorico(historicoVO);
						historicoNotaParcialVO.setTurmaDisciplinaNotaParcial(turmaDisciplinaNotaParcialVO);
						historicoNotaParcialVO.setTipoNota(turmaDisciplinaNotaTituloVO.getNota());
						getFacadeFactory().getHistoricoNotaParcialInterfaceFacade().incluir(historicoNotaParcialVO, usuarioVO);
						adicionarTipoNotaParcialHistorico(historicoVO, historicoNotaParcialVO);
					}
				}
			}
		}
		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO, List<HistoricoVO> listaHistoricoVOs, TurmaVO turmaLancamentoNotaVO, UsuarioVO usuarioVO,  boolean trazerAlunosTransferenciaMatriz, boolean permiteLancarNotaDisciplinaComposta) throws Exception {
		final String sql = "UPDATE turmaDisciplinaNotaTitulo set titulo = ?, data = ?, usuario = ?, possuiFormula = ?, formula = ? where turma = ? and disciplina = ? and ano = ? and semestre = ? and configuracaoacademico = ? and nota = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setString(1, turmaDisciplinaNotaTituloVO.getTitulo());
				sqlInserir.setTimestamp(2, Uteis.getDataJDBCTimestamp(turmaDisciplinaNotaTituloVO.getData()));
				sqlInserir.setInt(3, usuarioVO.getCodigo());
				sqlInserir.setBoolean(4, turmaDisciplinaNotaTituloVO.getPossuiFormula());
				sqlInserir.setString(5, turmaDisciplinaNotaTituloVO.getFormula());
				sqlInserir.setInt(6, turmaDisciplinaNotaTituloVO.getTurmaVO().getCodigo());
				sqlInserir.setInt(7, turmaDisciplinaNotaTituloVO.getDisciplinaVO().getCodigo());
				sqlInserir.setString(8, turmaDisciplinaNotaTituloVO.getAno());
				sqlInserir.setString(9, turmaDisciplinaNotaTituloVO.getSemestre());
				sqlInserir.setInt(10, turmaDisciplinaNotaTituloVO.getConfiguracaoAcademicoVO().getCodigo());
				sqlInserir.setString(11, turmaDisciplinaNotaTituloVO.getNota().name());	
				return sqlInserir;
			}
		}) == 0){
			incluir(turmaDisciplinaNotaTituloVO, listaHistoricoVOs, turmaLancamentoNotaVO, usuarioVO,  trazerAlunosTransferenciaMatriz, permiteLancarNotaDisciplinaComposta);
			return;
		}
		if(!turmaDisciplinaNotaTituloVO.getTurmaDisciplinaNotaTituloVOs().isEmpty()){
			for(TurmaDisciplinaNotaTituloVO tituloVO: turmaDisciplinaNotaTituloVO.getTurmaDisciplinaNotaTituloVOs()){
				tituloVO.setTitulo(turmaDisciplinaNotaTituloVO.getTitulo());
			}
			persistir(turmaDisciplinaNotaTituloVO.getTurmaDisciplinaNotaTituloVOs(), listaHistoricoVOs, turmaLancamentoNotaVO, usuarioVO,  trazerAlunosTransferenciaMatriz, permiteLancarNotaDisciplinaComposta);
		}
		
		if(Uteis.isAtributoPreenchido(turmaDisciplinaNotaTituloVO.getTurmaDisciplinaNotaParcialVOs())) {
//			List<HistoricoVO> lista = null;
//			lista = getFacadeFactory().getHistoricoFacade().consultaRapidaHistoricoPorDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHist(turmaDisciplinaNotaTituloVO.getDisciplinaVO().getCodigo(), turmaLancamentoNotaVO, turmaDisciplinaNotaTituloVO.getAno(), turmaDisciplinaNotaTituloVO.getSemestre(), false, turmaDisciplinaNotaTituloVO.getConfiguracaoAcademicoVO(), Uteis.NIVELMONTARDADOS_TODOS,  usuarioVO, trazerAlunosTransferenciaMatriz, permiteLancarNotaDisciplinaComposta);
			for (TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcialVO : turmaDisciplinaNotaTituloVO.getTurmaDisciplinaNotaParcialVOs()) {
				turmaDisciplinaNotaParcialVO.setTurmaDisciplinaNotaTituloVO(turmaDisciplinaNotaTituloVO);
				if(!Uteis.isAtributoPreenchido(turmaDisciplinaNotaParcialVO.getCodigo())) {
					getFacadeFactory().getTurmaDisciplinaNotaParcialInterfaceFacade().incluir(turmaDisciplinaNotaParcialVO, usuarioVO);
					
					for (HistoricoVO historicoVO : listaHistoricoVOs) {
						HistoricoNotaParcialVO historicoNotaParcialVO = new HistoricoNotaParcialVO();
						historicoNotaParcialVO.setHistorico(historicoVO);
						historicoNotaParcialVO.setTurmaDisciplinaNotaParcial(turmaDisciplinaNotaParcialVO);
						historicoNotaParcialVO.setTipoNota(turmaDisciplinaNotaTituloVO.getNota());
						getFacadeFactory().getHistoricoNotaParcialInterfaceFacade().incluir(historicoNotaParcialVO, usuarioVO);
						adicionarTipoNotaParcialHistorico(historicoVO, historicoNotaParcialVO);
					}
				}else {
					getFacadeFactory().getTurmaDisciplinaNotaParcialInterfaceFacade().alterar(turmaDisciplinaNotaParcialVO, usuarioVO);
				}
			}
		}
		else {
			for (HistoricoVO historicoVO : listaHistoricoVOs) {
			removerTipoNotaParcialHistorico(historicoVO, turmaDisciplinaNotaTituloVO);
			}
		}
	}
	
	public void adicionarTipoNotaParcialHistorico(HistoricoVO historicoVO, HistoricoNotaParcialVO historicoNotaParcialVO) {
		if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_1)) {
			historicoVO.getHistoricoNotaParcialNota1VOs().add(historicoNotaParcialVO);
		} 
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_2)) {
			historicoVO.getHistoricoNotaParcialNota2VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_3)) {
			historicoVO.getHistoricoNotaParcialNota3VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_4)) {
			historicoVO.getHistoricoNotaParcialNota4VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_5)) {
			historicoVO.getHistoricoNotaParcialNota5VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_6)) {
			historicoVO.getHistoricoNotaParcialNota6VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_7)) {
			historicoVO.getHistoricoNotaParcialNota7VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_8)) {
			historicoVO.getHistoricoNotaParcialNota8VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_9)) {
			historicoVO.getHistoricoNotaParcialNota9VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_10)) {
			historicoVO.getHistoricoNotaParcialNota10VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_11)) {
			historicoVO.getHistoricoNotaParcialNota11VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_12)) {
			historicoVO.getHistoricoNotaParcialNota12VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_13)) {
			historicoVO.getHistoricoNotaParcialNota13VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_14)) {
			historicoVO.getHistoricoNotaParcialNota14VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_15)) {
			historicoVO.getHistoricoNotaParcialNota15VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_16)) {
			historicoVO.getHistoricoNotaParcialNota16VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_17)) {
			historicoVO.getHistoricoNotaParcialNota17VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_18)) {
			historicoVO.getHistoricoNotaParcialNota18VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_19)) {
			historicoVO.getHistoricoNotaParcialNota19VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_20)) {
			historicoVO.getHistoricoNotaParcialNota20VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_21)) {
			historicoVO.getHistoricoNotaParcialNota21VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_22)) {
			historicoVO.getHistoricoNotaParcialNota22VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_23)) {
			historicoVO.getHistoricoNotaParcialNota23VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_24)) {
			historicoVO.getHistoricoNotaParcialNota24VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_25)) {
			historicoVO.getHistoricoNotaParcialNota25VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_26)) {
			historicoVO.getHistoricoNotaParcialNota26VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_27)) {
			historicoVO.getHistoricoNotaParcialNota27VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_28)) {
			historicoVO.getHistoricoNotaParcialNota28VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_29)) {
			historicoVO.getHistoricoNotaParcialNota29VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_30)) {
			historicoVO.getHistoricoNotaParcialNota30VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_31)) {
			historicoVO.getHistoricoNotaParcialNota31VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_32)) {
			historicoVO.getHistoricoNotaParcialNota32VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_33)) {
			historicoVO.getHistoricoNotaParcialNota33VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_34)) {
			historicoVO.getHistoricoNotaParcialNota34VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_35)) {
			historicoVO.getHistoricoNotaParcialNota35VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_36)) {
			historicoVO.getHistoricoNotaParcialNota36VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_37)) {
			historicoVO.getHistoricoNotaParcialNota37VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_38)) {
			historicoVO.getHistoricoNotaParcialNota38VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_39)) {
			historicoVO.getHistoricoNotaParcialNota39VOs().add(historicoNotaParcialVO);
		}
		else if (historicoNotaParcialVO.getTipoNota().equals(TipoNotaConceitoEnum.NOTA_40)) {
			historicoVO.getHistoricoNotaParcialNota40VOs().add(historicoNotaParcialVO);
		}
	}
	
	public void removerTipoNotaParcialHistorico(HistoricoVO historicoVO, TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO) {
		if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_1)) {
			historicoVO.getHistoricoNotaParcialNota1VOs().clear();
		} 
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_2)) {
			historicoVO.getHistoricoNotaParcialNota2VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_3)) {
			historicoVO.getHistoricoNotaParcialNota3VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_4)) {
			historicoVO.getHistoricoNotaParcialNota4VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_5)) {
			historicoVO.getHistoricoNotaParcialNota5VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_6)) {
			historicoVO.getHistoricoNotaParcialNota6VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_7)) {
			historicoVO.getHistoricoNotaParcialNota7VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_8)) {
			historicoVO.getHistoricoNotaParcialNota8VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_9)) {
			historicoVO.getHistoricoNotaParcialNota9VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_10)) {
			historicoVO.getHistoricoNotaParcialNota10VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_11)) {
			historicoVO.getHistoricoNotaParcialNota11VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_12)) {
			historicoVO.getHistoricoNotaParcialNota12VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_13)) {
			historicoVO.getHistoricoNotaParcialNota13VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_14)) {
			historicoVO.getHistoricoNotaParcialNota14VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_15)) {
			historicoVO.getHistoricoNotaParcialNota15VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_16)) {
			historicoVO.getHistoricoNotaParcialNota16VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_17)) {
			historicoVO.getHistoricoNotaParcialNota17VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_18)) {
			historicoVO.getHistoricoNotaParcialNota18VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_19)) {
			historicoVO.getHistoricoNotaParcialNota19VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_20)) {
			historicoVO.getHistoricoNotaParcialNota20VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_21)) {
			historicoVO.getHistoricoNotaParcialNota21VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_22)) {
			historicoVO.getHistoricoNotaParcialNota22VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_23)) {
			historicoVO.getHistoricoNotaParcialNota23VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_24)) {
			historicoVO.getHistoricoNotaParcialNota24VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_25)) {
			historicoVO.getHistoricoNotaParcialNota25VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_26)) {
			historicoVO.getHistoricoNotaParcialNota26VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_27)) {
			historicoVO.getHistoricoNotaParcialNota27VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_28)) {
			historicoVO.getHistoricoNotaParcialNota28VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_29)) {
			historicoVO.getHistoricoNotaParcialNota29VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_30)) {
			historicoVO.getHistoricoNotaParcialNota30VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_31)) {
			historicoVO.getHistoricoNotaParcialNota31VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_32)) {
			historicoVO.getHistoricoNotaParcialNota32VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_33)) {
			historicoVO.getHistoricoNotaParcialNota33VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_34)) {
			historicoVO.getHistoricoNotaParcialNota34VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_35)) {
			historicoVO.getHistoricoNotaParcialNota35VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_36)) {
			historicoVO.getHistoricoNotaParcialNota36VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_37)) {
			historicoVO.getHistoricoNotaParcialNota37VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_38)) {
			historicoVO.getHistoricoNotaParcialNota38VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_39)) {
			historicoVO.getHistoricoNotaParcialNota39VOs().clear();
		}
		else if (turmaDisciplinaNotaTituloVO.getNota().equals(TipoNotaConceitoEnum.NOTA_40)) {
			historicoVO.getHistoricoNotaParcialNota40VOs().clear();
		}
	}
	
	public StringBuilder getSqlConsultaCompleta(){
		StringBuilder sql = new StringBuilder("");
		sql.append(" select turmaDisciplinaNotaTitulo.codigo, turmaDisciplinaNotaTitulo.nota, turmaDisciplinaNotaTitulo.titulo, turmaDisciplinaNotaTitulo.ano, ");
		sql.append(" turmaDisciplinaNotaTitulo.semestre, turmaDisciplinaNotaTitulo.turma, turmaDisciplinaNotaTitulo.disciplina, turmaDisciplinaNotaTitulo.possuiFormula, turmaDisciplinaNotaTitulo.formula, turmaDisciplinaNotaTitulo.configuracaoAcademico, ");
		sql.append(" turmadisciplinanotaparcial.codigo as \"turmadisciplinanotaparcial.codigo\", turmadisciplinanotaparcial.tituloNota \"turmadisciplinanotaparcial.tituloNota\", turmadisciplinanotaparcial.variavel as \"turmadisciplinanotaparcial.variavel\" from turmaDisciplinaNotaTitulo ");
		sql.append(" inner join turma on turma.codigo = turmaDisciplinaNotaTitulo.turma ");
		sql.append(" inner join disciplina on disciplina.codigo = turmaDisciplinaNotaTitulo.disciplina ");
		sql.append(" inner join configuracaoacademico on configuracaoacademico.codigo = turmaDisciplinaNotaTitulo.configuracaoacademico ");		
		sql.append(" left join turmadisciplinanotaparcial on turmadisciplinanotaparcial.turmaDisciplinaNotaTitulo = turmaDisciplinaNotaTitulo.codigo ");
		return sql;
	}

	@Override
	public List<TurmaDisciplinaNotaTituloVO> consultarPorTurmaDisciplinaAnoSemestreConfiguracaoAcademica(
			TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre,
			ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = getSqlConsultaCompleta();
		try {
			sql.append(" where 1=1 ");
			if (Uteis.isAtributoPreenchido(turmaVO)) {
				sql.append(" and turma.codigo = ").append(turmaVO.getCodigo());
			}
			if (Uteis.isAtributoPreenchido(disciplinaVO)) {
				sql.append(" and disciplina.codigo = ").append(disciplinaVO.getCodigo());
			}
			if (Uteis.isAtributoPreenchido(ano)) {
				sql.append(" and turmaDisciplinaNotaTitulo.ano = '").append(ano).append("' ");
			}
			if (Uteis.isAtributoPreenchido(semestre)) {
				sql.append(" and turmaDisciplinaNotaTitulo.semestre = '").append(semestre).append("' ");
			}
			if (Uteis.isAtributoPreenchido(configuracaoAcademicoVO)) {
				sql.append(" and configuracaoAcademico.codigo = '").append(configuracaoAcademicoVO.getCodigo()).append("' ");
			}
			sql.append(" order by turma, disciplina, ano, semestre, configuracaoacademico, nota, turmaDisciplinaNotaTitulo.codigo ");
			return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), usuarioVO);
		} catch (Exception e) {
			throw e;
		} finally {
			sql = null;
		}
	}
	
	public List<TurmaDisciplinaNotaTituloVO> montarDadosConsulta(SqlRowSet rs, UsuarioVO usuarioVO) throws Exception{
		List<TurmaDisciplinaNotaTituloVO> turmaDisciplinaNotaTituloVOs = new ArrayList<TurmaDisciplinaNotaTituloVO>(0);
		while(rs.next()){			
			turmaDisciplinaNotaTituloVOs.add(montarDados(rs, usuarioVO));
		}
		rs = null;
		return  turmaDisciplinaNotaTituloVOs;
	}
	
	public TurmaDisciplinaNotaTituloVO montarDados(SqlRowSet rs, UsuarioVO usuarioVO) throws Exception{
		TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO = new TurmaDisciplinaNotaTituloVO();
		turmaDisciplinaNotaTituloVO.setNovoObj(false);
		turmaDisciplinaNotaTituloVO.setAno(rs.getString("ano"));
		turmaDisciplinaNotaTituloVO.setSemestre(rs.getString("semestre"));
		turmaDisciplinaNotaTituloVO.setTitulo(rs.getString("titulo"));
		turmaDisciplinaNotaTituloVO.setNota(TipoNotaConceitoEnum.valueOf(rs.getString("nota")));
		turmaDisciplinaNotaTituloVO.setCodigo(rs.getInt("codigo"));
		turmaDisciplinaNotaTituloVO.getTurmaVO().setCodigo(rs.getInt("turma"));
		turmaDisciplinaNotaTituloVO.getDisciplinaVO().setCodigo(rs.getInt("disciplina"));
		turmaDisciplinaNotaTituloVO.getConfiguracaoAcademicoVO().setCodigo(rs.getInt("configuracaoAcademico"));
		turmaDisciplinaNotaTituloVO.setTituloOriginal(rs.getString("titulo"));	
		turmaDisciplinaNotaTituloVO.setPossuiFormula(rs.getBoolean("possuiFormula"));
		turmaDisciplinaNotaTituloVO.setFormula(rs.getString("formula"));
		do {
			if(rs.getInt("codigo") != turmaDisciplinaNotaTituloVO.getCodigo().intValue()) {				
				break;
			}
			TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcialVO =  new TurmaDisciplinaNotaParcialVO();
			turmaDisciplinaNotaParcialVO.setCodigo(rs.getInt("turmaDisciplinaNotaParcial.codigo"));
			turmaDisciplinaNotaParcialVO.setVariavel(rs.getString("turmaDisciplinaNotaParcial.variavel"));
			turmaDisciplinaNotaParcialVO.setTituloNota(rs.getString("turmaDisciplinaNotaParcial.tituloNota"));
			turmaDisciplinaNotaParcialVO.setNovoObj(false);
			turmaDisciplinaNotaParcialVO.setTurmaDisciplinaNotaTituloVO(turmaDisciplinaNotaTituloVO);
			if(Uteis.isAtributoPreenchido(turmaDisciplinaNotaParcialVO)) {
				turmaDisciplinaNotaTituloVO.getTurmaDisciplinaNotaParcialVOs().add(turmaDisciplinaNotaParcialVO);
			}
		}while((rs.next()));
		rs.previous();
		return turmaDisciplinaNotaTituloVO;		
	}

	@Override
	public TurmaDisciplinaNotaTituloVO consultarPorTurmaDisciplinaAnoSemestreConfiguracaoAcademicaTipoNota(
			TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre,
			ConfiguracaoAcademicoVO configuracaoAcademicoVO, TipoNotaConceitoEnum tipoNotaConceitoEnum,
			UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = getSqlConsultaCompleta();
		SqlRowSet rs = null;
		try {
			sql.append(" where 1=1 ");
			if (Uteis.isAtributoPreenchido(turmaVO)) {
				sql.append(" and turma.codigo = ").append(turmaVO.getCodigo());
			}
			if (Uteis.isAtributoPreenchido(disciplinaVO)) {
				sql.append(" and disciplina.codigo = ").append(disciplinaVO.getCodigo());
			}
			if (Uteis.isAtributoPreenchido(ano)) {
				sql.append(" and turmaDisciplinaNotaTitulo.ano = '").append(ano).append("' ");
			}
			if (Uteis.isAtributoPreenchido(semestre)) {
				sql.append(" and turmaDisciplinaNotaTitulo.semestre = '").append(semestre).append("' ");
			}
			if (Uteis.isAtributoPreenchido(configuracaoAcademicoVO)) {
				sql.append(" and configuracaoAcademico.codigo = '").append(configuracaoAcademicoVO.getCodigo()).append("' ");
			}
			sql.append(" order by turma, disciplina, ano, semestre, configuracaoacademico, nota ");
			rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if(rs.next()){
				return montarDados(rs, usuarioVO);
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			sql = null;
			rs = null;
		}
	}

	@Override
	public List<TurmaDisciplinaNotaTituloVO> consultarPorMatriculaPeriodoTurmaDisciplina(
			MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		return consultarPorTurmaDisciplinaAnoSemestreConfiguracaoAcademica(matriculaPeriodoTurmaDisciplinaVO.getTurma(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina(), matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre(), matriculaPeriodoTurmaDisciplinaVO.getConfiguracaoAcademicoVO(), usuarioVO);
	}

	@Override
	public List<TurmaDisciplinaNotaTituloVO> consultarPorHistorico(HistoricoVO historicoVO, UsuarioVO usuarioVO)
			throws Exception {
		return consultarPorTurmaDisciplinaAnoSemestreConfiguracaoAcademica(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma(), historicoVO.getDisciplina(), historicoVO.getAnoHistorico(), historicoVO.getSemestreHistorico(), historicoVO.getConfiguracaoAcademico(), usuarioVO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarCriacaoTurmaDisciplinaNotaTituloComBaseTipoTurma(TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre, ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuarioVO) throws Exception{
		if(Uteis.isAtributoPreenchido(turmaVO) && Uteis.isAtributoPreenchido(disciplinaVO)  && Uteis.isAtributoPreenchido(configuracaoAcademicoVO)){
			if(turmaVO.getIntegral()){
				ano = "";
				semestre = "";
			}else if(turmaVO.getAnual()){
				semestre = "";
			}
		List<TurmaDisciplinaNotaTituloVO> turmaDisciplinaNotaTituloVOs = new ArrayList<TurmaDisciplinaNotaTituloVO>(0);		
		if(turmaVO.getTurmaAgrupada()){
			turmaDisciplinaNotaTituloVOs =  consultarPorTurmaDisciplinaAnoSemestreConfiguracaoAcademica(turmaVO, disciplinaVO, ano, semestre, configuracaoAcademicoVO, usuarioVO);
			realizarCriacaoTurmaDisciplinaNotaTituloComBaseConfiguracaoAcademica(turmaDisciplinaNotaTituloVOs, turmaVO, disciplinaVO, ano, semestre, configuracaoAcademicoVO, usuarioVO);
			List<TurmaDisciplinaVO> turmaDisciplinaVOs = getFacadeFactory().getTurmaDisciplinaFacade().consultarTurmaDisciplinaTurmaBasePartindoTurmaAgrupadaEDisciplina(turmaVO.getCodigo(), disciplinaVO.getCodigo());
			for(TurmaDisciplinaVO turmaDisciplinaVO: turmaDisciplinaVOs){
				for (TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO : turmaDisciplinaNotaTituloVOs) {
					TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO2 = new TurmaDisciplinaNotaTituloVO(turmaDisciplinaNotaTituloVO.getNota(), turmaDisciplinaVO.getTurmaDescricaoVO(), turmaDisciplinaVO.getDisciplina(), ano, semestre,configuracaoAcademicoVO);
					if (!turmaDisciplinaNotaTituloVO.getTurmaDisciplinaNotaTituloVOs().contains(turmaDisciplinaNotaTituloVO2)) {
						turmaDisciplinaNotaTituloVO2.setTitulo(turmaDisciplinaNotaTituloVO.getTitulo());
						turmaDisciplinaNotaTituloVO.getTurmaDisciplinaNotaTituloVOs().add(turmaDisciplinaNotaTituloVO2);
					} else {
						turmaDisciplinaNotaTituloVO2 = null;
					}
				}
			}							
			Uteis.liberarListaMemoria(turmaDisciplinaVOs);
		}else if(turmaVO.getSubturma()){
//			COMENTADO PRA VER COM O RODRIGO
			TurmaVO turmaPrincipal = new TurmaVO();
			turmaPrincipal.setCodigo(turmaVO.getTurmaPrincipal());
			getFacadeFactory().getTurmaFacade().carregarDados(turmaPrincipal, NivelMontarDados.TODOS, usuarioVO);
			
			turmaDisciplinaNotaTituloVOs =  consultarPorTurmaDisciplinaAnoSemestreConfiguracaoAcademica(turmaPrincipal, disciplinaVO, ano, semestre, configuracaoAcademicoVO, usuarioVO);
			realizarCriacaoTurmaDisciplinaNotaTituloComBaseConfiguracaoAcademica(turmaDisciplinaNotaTituloVOs, turmaPrincipal, disciplinaVO, ano, semestre, configuracaoAcademicoVO, usuarioVO);
		}else{
			turmaDisciplinaNotaTituloVOs =  consultarPorTurmaDisciplinaAnoSemestreConfiguracaoAcademica(turmaVO, disciplinaVO, ano, semestre, configuracaoAcademicoVO, usuarioVO);
			realizarCriacaoTurmaDisciplinaNotaTituloComBaseConfiguracaoAcademica(turmaDisciplinaNotaTituloVOs, turmaVO, disciplinaVO, ano, semestre, configuracaoAcademicoVO, usuarioVO);
		}		
		for(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO: turmaDisciplinaNotaTituloVOs){
			UtilReflexao.invocarMetodo(configuracaoAcademicoVO, "setTurmaDisciplinaNotaTitulo"+turmaDisciplinaNotaTituloVO.getNota().getNumeroNota(), turmaDisciplinaNotaTituloVO);
			turmaDisciplinaNotaTituloVO.setTituloConfiguracao((String)UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "tituloNotaApresentar"+turmaDisciplinaNotaTituloVO.getNota().getNumeroNota()));
			turmaDisciplinaNotaTituloVO.setVariavelConfiguracao((String)UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "tituloNota"+turmaDisciplinaNotaTituloVO.getNota().getNumeroNota()));
		}
		}else if(Uteis.isAtributoPreenchido(disciplinaVO)  && Uteis.isAtributoPreenchido(configuracaoAcademicoVO)) {
			List<TurmaDisciplinaNotaTituloVO> turmaDisciplinaNotaTituloVOs = new ArrayList<TurmaDisciplinaNotaTituloVO>(0);	
			realizarCriacaoTurmaDisciplinaNotaTituloComBaseConfiguracaoAcademica(turmaDisciplinaNotaTituloVOs, turmaVO, disciplinaVO, ano, semestre, configuracaoAcademicoVO, usuarioVO);
			for(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO: turmaDisciplinaNotaTituloVOs){
				UtilReflexao.invocarMetodo(configuracaoAcademicoVO, "setTurmaDisciplinaNotaTitulo"+turmaDisciplinaNotaTituloVO.getNota().getNumeroNota(), turmaDisciplinaNotaTituloVO);
				turmaDisciplinaNotaTituloVO.setTituloConfiguracao((String)UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "tituloNotaApresentar"+turmaDisciplinaNotaTituloVO.getNota().getNumeroNota()));
				turmaDisciplinaNotaTituloVO.setVariavelConfiguracao((String)UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "tituloNota"+turmaDisciplinaNotaTituloVO.getNota().getNumeroNota()));
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)	
	private List<TurmaDisciplinaNotaTituloVO> realizarCriacaoTurmaDisciplinaNotaTituloComBaseConfiguracaoAcademica(List<TurmaDisciplinaNotaTituloVO> turmaDisciplinaNotaTituloVOs, TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre, ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuarioVO) throws Exception{	
		for(int x = 1; x<= 40; x++){
			if((Boolean)UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "apresentarNota"+x) && (Boolean)UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "utilizarNota"+x)){
				TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO = new TurmaDisciplinaNotaTituloVO(TipoNotaConceitoEnum.getTipoNota(x), turmaVO, disciplinaVO, ano, semestre, configuracaoAcademicoVO);
				if(!turmaDisciplinaNotaTituloVOs.contains(turmaDisciplinaNotaTituloVO)){
					turmaDisciplinaNotaTituloVOs.add(turmaDisciplinaNotaTituloVO);
				} else {
					for (TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO1 : turmaDisciplinaNotaTituloVOs) {
						turmaDisciplinaNotaTituloVO1.setTurmaVO(turmaVO);
						turmaDisciplinaNotaTituloVO1.setDisciplinaVO(disciplinaVO);
						turmaDisciplinaNotaTituloVO1.setConfiguracaoAcademicoVO(configuracaoAcademicoVO);
					}
				}
			}else{
				TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO = new TurmaDisciplinaNotaTituloVO(TipoNotaConceitoEnum.getTipoNota(x), turmaVO, disciplinaVO, ano, semestre, configuracaoAcademicoVO);
				if(turmaDisciplinaNotaTituloVOs.contains(turmaDisciplinaNotaTituloVO)){
					turmaDisciplinaNotaTituloVOs.remove(turmaDisciplinaNotaTituloVO);
					excluir(turmaDisciplinaNotaTituloVO, usuarioVO);
				}
			}
		}
		return turmaDisciplinaNotaTituloVOs;
	}
	
	public TurmaDisciplinaNotaTituloVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = getSqlConsultaCompleta()+" WHERE codigo = ? ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( turmaDisciplinaNotaTitulo = " + codigoPrm + " ).");
		}
		return (montarDados(tabelaResultado, usuario));
	}
	
	public String setarTituloNotaApresentar(String tipoNota, String tituloNotaApresentarConfiguracaoAcademico) {
		
		String tituloNotaApresentar = "";
		
		if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_1.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_2.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_3.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_4.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_5.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_6.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_7.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_8.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_9.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}			
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_10.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}			
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_11.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_12.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_13.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_14.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_15.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}			
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_16.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}			
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_17.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_18.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_19.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_20.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_21.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_22.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_23.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_24.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_25.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_26.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_27.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_28.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_29.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}	
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_30.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_31.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_32.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_33.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_34.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}			
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_35.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}			
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_36.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_37.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_38.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}			
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_39.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		else if(tipoNota.equals(TipoNotaConceitoEnum.NOTA_40.name())) {
			tituloNotaApresentar = tituloNotaApresentarConfiguracaoAcademico;
		}
		
		return tituloNotaApresentar;
	}
	

}
