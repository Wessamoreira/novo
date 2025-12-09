package negocio.facade.jdbc.administrativo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.CampanhaPublicoAlvoVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoGerarAgendaCampanhaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.CampanhaPublicoAlvoProspectVO;
import negocio.comuns.crm.CursoInteresseVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.RegistroEntradaVO;
import negocio.comuns.crm.enumerador.RendaProspectEnum;
import negocio.comuns.crm.enumerador.TipoCampanhaEnum;
import negocio.comuns.crm.enumerador.TipoEmpresaProspectEnum;
import negocio.comuns.segmentacao.SegmentacaoProspectVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.CampanhaPublicoAlvoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class CampanhaPublicoAlvo extends ControleAcesso implements CampanhaPublicoAlvoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4975043061553373920L;
	protected static String idEntidade;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CampanhaPublicoAlvoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			if (obj.getCodigo() != 0 || obj.getCodigo() == null) {
				obj.setCodigo(0);
			}
			final String sql = "INSERT INTO CampanhaPublicoAlvo (unidadeensino, cursoInteresse, renda, campanha, tipoEmpresa, formacaoAcademica, curso, formandos, " + 
			"possiveisFormandos, cursando, preMatriculado, trancados, cancelado, inadimplentes, diasInadimplencia, " + 
					"totalProspectsSelecionadosCampanha, tempoMedioExecucaoWorkflowColaborador, mediaProspectColaborador, registroentrada, campanhaCaptacao " + 
			",alunosComSerasa,alunosSemSerasa,processoSeletivo,aprovado,reprovado,dataInicioProcessoSeletivo,dataTerminoProcessoSeletivo,todasOpcoesCurso, tipoPublicoAlvo, "
			+ "abandonado, transferenciaInterna, transferenciaExterna, preMatriculaCancelada, naoGerarAgendaParaProspectsComAgendaJaExistente, tipoGerarAgendaCampanha, ultimoCursoInteresse, diasSemInteracao, adicionadoDinamicamente) " + 
					"VALUES (?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (obj.getUnidadeEnsino().getCodigo() != 0) {
						sqlInserir.setInt(1, obj.getUnidadeEnsino().getCodigo());
					} else {
						sqlInserir.setNull(1, 0);
					}
					if (obj.getCursoInteresse().getCodigo() != 0) {
						sqlInserir.setInt(2, obj.getCursoInteresse().getCodigo());
					} else {
						sqlInserir.setNull(2, 0);
					}
					sqlInserir.setString(3, obj.getRenda().toString());
					if (obj.getCampanha().getCodigo() != 0) {
						sqlInserir.setInt(4, obj.getCampanha().getCodigo());
					} else {
						sqlInserir.setNull(4, 0);
					}
					sqlInserir.setString(5, obj.getTipoEmpresa().toString());
					sqlInserir.setString(6, obj.getFormacaoAcademica().getEscolaridade());
					if (obj.getCurso().getCodigo() != 0) {
						sqlInserir.setInt(7, obj.getCurso().getCodigo());
					} else {
						sqlInserir.setNull(7, 0);
					}
					sqlInserir.setBoolean(8, obj.getFormandos());
					sqlInserir.setBoolean(9, obj.getPossiveisFormandos());
					sqlInserir.setBoolean(10, obj.getCursando());
					sqlInserir.setBoolean(11, obj.getPreMatriculados());
					sqlInserir.setBoolean(12, obj.getTrancados());
					sqlInserir.setBoolean(13, obj.getCancelado());
					sqlInserir.setBoolean(14, obj.getInadimplentes());
					sqlInserir.setInt(15, obj.getDiasInadimplencia());
					sqlInserir.setInt(16, obj.getTotalProspectsSelecionadosCampanha());
					sqlInserir.setInt(17, obj.getTempoMedioExecucaoWorkflowColaborador());
					sqlInserir.setInt(18, obj.getMediaProspectColaborador());
					if (obj.getRegistroEntrada().getCodigo() != 0) {
						sqlInserir.setInt(19, obj.getRegistroEntrada().getCodigo());
					} else {
						sqlInserir.setNull(19, 0);
					}
					if (obj.getCampanhaCaptacao().getCodigo() != 0) {
						sqlInserir.setInt(20, obj.getCampanhaCaptacao().getCodigo());
					} else {
						sqlInserir.setNull(20, 0);
					}
					sqlInserir.setBoolean(21, obj.getAlunosComSerasa());
					sqlInserir.setBoolean(22, obj.getAlunosSemSerasa());
					if (obj.getProcessoSeletivoVO().getCodigo() != 0) {
						sqlInserir.setInt(23, obj.getProcessoSeletivoVO().getCodigo());
					} else {
						sqlInserir.setNull(23, 0);
					}
					sqlInserir.setBoolean(24, obj.getAprovado());
					sqlInserir.setBoolean(25, obj.getReprovado());
					if (Uteis.isAtributoPreenchido(obj.getDataInicioProcessoSeletivo())) {
						sqlInserir.setDate(26, Uteis.getDataJDBC(obj.getDataInicioProcessoSeletivo()));
					} else {
						sqlInserir.setNull(26, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDataInicioProcessoSeletivo())) {
						sqlInserir.setDate(27, Uteis.getDataJDBC(obj.getDataInicioProcessoSeletivo()));
					} else {
						sqlInserir.setNull(27, 0);
					}
					sqlInserir.setBoolean(28, obj.getTodasOpcoesCurso());
					sqlInserir.setString(29, obj.getTipoPublicoAlvo());
					sqlInserir.setBoolean(30, obj.getAbandonado());
					sqlInserir.setBoolean(31, obj.getTransferenciaInterna());
					sqlInserir.setBoolean(32, obj.getTransferenciaExterna());
					sqlInserir.setBoolean(33, obj.getPreMatriculaCancelada());
					sqlInserir.setBoolean(34, obj.getNaoGerarAgendaParaProspectsComAgendaJaExistente());
					sqlInserir.setString(35, obj.getTipoGerarAgendaCampanha().toString());
					sqlInserir.setBoolean(36, obj.getUltimoCursoInteresse());
					sqlInserir.setInt(37, obj.getDiasSemInteracao());
					sqlInserir.setBoolean(38, obj.getAdicionadoDinamicamente());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
						
			getFacadeFactory().getCampanhaPublicoAlvoProspectFacade().incluirCampanhaPublicoAlvoProspect(obj.getCodigo(), obj.getCampanhaPublicoAlvoProspectVOs(), usuarioVO);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirSemSubordinada(final CampanhaPublicoAlvoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			if (obj.getCodigo() != 0 || obj.getCodigo() == null) {
				obj.setCodigo(0);
			}
			final String sql = "INSERT INTO CampanhaPublicoAlvo (unidadeensino, cursoInteresse, renda, campanha, tipoEmpresa, formacaoAcademica, curso, formandos, " + 
			"possiveisFormandos, cursando, preMatriculado, trancados, cancelado, inadimplentes, diasInadimplencia, " + 
					"totalProspectsSelecionadosCampanha, tempoMedioExecucaoWorkflowColaborador, mediaProspectColaborador, registroentrada, campanhaCaptacao " + 
			",alunosComSerasa,alunosSemSerasa,processoSeletivo,aprovado,reprovado,dataInicioProcessoSeletivo,dataTerminoProcessoSeletivo,todasOpcoesCurso, tipoPublicoAlvo, "
			+ "abandonado, transferenciaInterna, transferenciaExterna, preMatriculaCancelada, naoGerarAgendaParaProspectsComAgendaJaExistente, tipoGerarAgendaCampanha, ultimoCursoInteresse, diasSemInteracao, adicionadoDinamicamente) " + 
					"VALUES (?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (obj.getUnidadeEnsino().getCodigo() != 0) {
						sqlInserir.setInt(1, obj.getUnidadeEnsino().getCodigo());
					} else {
						sqlInserir.setNull(1, 0);
					}
					if (obj.getCursoInteresse().getCodigo() != 0) {
						sqlInserir.setInt(2, obj.getCursoInteresse().getCodigo());
					} else {
						sqlInserir.setNull(2, 0);
					}
					sqlInserir.setString(3, obj.getRenda().toString());
					if (obj.getCampanha().getCodigo() != 0) {
						sqlInserir.setInt(4, obj.getCampanha().getCodigo());
					} else {
						sqlInserir.setNull(4, 0);
					}
					sqlInserir.setString(5, obj.getTipoEmpresa().toString());
					sqlInserir.setString(6, obj.getFormacaoAcademica().getEscolaridade());
					if (obj.getCurso().getCodigo() != 0) {
						sqlInserir.setInt(7, obj.getCurso().getCodigo());
					} else {
						sqlInserir.setNull(7, 0);
					}
					sqlInserir.setBoolean(8, obj.getFormandos());
					sqlInserir.setBoolean(9, obj.getPossiveisFormandos());
					sqlInserir.setBoolean(10, obj.getCursando());
					sqlInserir.setBoolean(11, obj.getPreMatriculados());
					sqlInserir.setBoolean(12, obj.getTrancados());
					sqlInserir.setBoolean(13, obj.getCancelado());
					sqlInserir.setBoolean(14, obj.getInadimplentes());
					sqlInserir.setInt(15, obj.getDiasInadimplencia());
					sqlInserir.setInt(16, obj.getTotalProspectsSelecionadosCampanha());
					sqlInserir.setInt(17, obj.getTempoMedioExecucaoWorkflowColaborador());
					sqlInserir.setInt(18, obj.getMediaProspectColaborador());
					if (obj.getRegistroEntrada().getCodigo() != 0) {
						sqlInserir.setInt(19, obj.getRegistroEntrada().getCodigo());
					} else {
						sqlInserir.setNull(19, 0);
					}
					if (obj.getCampanhaCaptacao().getCodigo() != 0) {
						sqlInserir.setInt(20, obj.getCampanhaCaptacao().getCodigo());
					} else {
						sqlInserir.setNull(20, 0);
					}
					sqlInserir.setBoolean(21, obj.getAlunosComSerasa());
					sqlInserir.setBoolean(22, obj.getAlunosSemSerasa());
					if (obj.getProcessoSeletivoVO().getCodigo() != 0) {
						sqlInserir.setInt(23, obj.getProcessoSeletivoVO().getCodigo());
					} else {
						sqlInserir.setNull(23, 0);
					}
					sqlInserir.setBoolean(24, obj.getAprovado());
					sqlInserir.setBoolean(25, obj.getReprovado());
					if (Uteis.isAtributoPreenchido(obj.getDataInicioProcessoSeletivo())) {
						sqlInserir.setDate(26, Uteis.getDataJDBC(obj.getDataInicioProcessoSeletivo()));
					} else {
						sqlInserir.setNull(26, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDataInicioProcessoSeletivo())) {
						sqlInserir.setDate(27, Uteis.getDataJDBC(obj.getDataInicioProcessoSeletivo()));
					} else {
						sqlInserir.setNull(27, 0);
					}
					sqlInserir.setBoolean(28, obj.getTodasOpcoesCurso());
					sqlInserir.setString(29, obj.getTipoPublicoAlvo());
					sqlInserir.setBoolean(30, obj.getAbandonado());
					sqlInserir.setBoolean(31, obj.getTransferenciaInterna());
					sqlInserir.setBoolean(32, obj.getTransferenciaExterna());
					sqlInserir.setBoolean(33, obj.getPreMatriculaCancelada());
					sqlInserir.setBoolean(34, obj.getNaoGerarAgendaParaProspectsComAgendaJaExistente());
					sqlInserir.setString(35, obj.getTipoGerarAgendaCampanha().toString());
					sqlInserir.setBoolean(36, obj.getUltimoCursoInteresse());
					sqlInserir.setInt(37, obj.getDiasSemInteracao());
					sqlInserir.setBoolean(38, obj.getAdicionadoDinamicamente());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));

			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirCampanhaPublicoAlvo(Integer publicoAlvo, List<CampanhaPublicoAlvoVO> objetos, UsuarioVO usuarioVO) throws Exception {
		Iterator<CampanhaPublicoAlvoVO> e = objetos.iterator();
		while (e.hasNext()) {
			CampanhaPublicoAlvoVO obj = (CampanhaPublicoAlvoVO) e.next();
			obj.getCampanha().setCodigo(publicoAlvo);
			incluir(obj, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCampanhaPublicoAlvo(Integer campanha, List<CampanhaPublicoAlvoVO> objetos, UsuarioVO usuarioVO) throws Exception {
		String str = "DELETE FROM campanhapublicoalvo WHERE campanha = ?";
		Iterator<CampanhaPublicoAlvoVO> i = objetos.iterator();
		while (i.hasNext()) {
			CampanhaPublicoAlvoVO objeto = (CampanhaPublicoAlvoVO) i.next();
			str += " AND codigo <> " + objeto.getCodigo().intValue();
		}
		getConexao().getJdbcTemplate().update(str+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), new Object[] { campanha });
		Iterator<CampanhaPublicoAlvoVO> e = objetos.iterator();
		while (e.hasNext()) {
			CampanhaPublicoAlvoVO objeto = (CampanhaPublicoAlvoVO) e.next();
			if (objeto.getCodigo().equals(0)) {
				objeto.getCampanha().setCodigo(campanha);
				incluir(objeto, usuarioVO);
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCampanhaPublicoAlvoSemSubordinada(Integer campanha, List<CampanhaPublicoAlvoVO> objetos, UsuarioVO usuarioVO) throws Exception {
		String str = "DELETE FROM campanhapublicoalvo WHERE campanha = ?";
		Iterator<CampanhaPublicoAlvoVO> i = objetos.iterator();
		while (i.hasNext()) {
			CampanhaPublicoAlvoVO objeto = (CampanhaPublicoAlvoVO) i.next();
			str += " AND codigo <> " + objeto.getCodigo().intValue();
		}
		getConexao().getJdbcTemplate().update(str+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), new Object[] { campanha });
		Iterator<CampanhaPublicoAlvoVO> e = objetos.iterator();
		while (e.hasNext()) {
			CampanhaPublicoAlvoVO objeto = (CampanhaPublicoAlvoVO) e.next();
			if (objeto.getCodigo().equals(0)) {
				objeto.getCampanha().setCodigo(campanha);
				incluirSemSubordinada(objeto, usuarioVO);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirCampanhaPublicoAlvo(Integer codigo, UsuarioVO usuarioVO) throws Exception {
		try {
			CampanhaMidia.excluir(getIdEntidade());
			String sql = "DELETE FROM campanhapublicoalvo WHERE ((Campanha= ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { codigo });
		} catch (Exception e) {
			throw e;
		}
	}

	public void adicionarObjCampanhaPublicoAlvoVOs(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO obj, Boolean revisitacaoCarteira, List<SegmentacaoProspectVO> segmentacaoProspectVOs) throws Exception {
		obj.setCampanha(campanhaVO);
		validarDadosAdicionar(obj);
		if (revisitacaoCarteira) {
//			obj.setTotalProspectsSelecionadosCampanha(montarTotalProspectsSelecionadosCampanhaRevisitacaoCarteira(campanhaVO, obj));
			List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectVOs = consultarProspectsSelecionadosCampanhaRevisitacaoCarteira(campanhaVO, obj, segmentacaoProspectVOs);
			Iterator<CampanhaPublicoAlvoProspectVO> i = listaCampanhaPublicoAlvoProspectVOs.iterator();
			while (i.hasNext()) {
				obj.getCampanhaPublicoAlvoProspectVOs().add((CampanhaPublicoAlvoProspectVO) i.next());
			}
			obj.setTotalProspectsSelecionadosCampanha(listaCampanhaPublicoAlvoProspectVOs.size());
		} else {
//			obj.setTotalProspectsSelecionadosCampanha(montarTotalProspectsSelecionadosCampanhaRegistroEntrada(campanhaVO, obj));
			List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectVOs = consultarProspectsSelecionadosCampanhaRegistroEntrada(campanhaVO, obj);
			Iterator<CampanhaPublicoAlvoProspectVO> i = listaCampanhaPublicoAlvoProspectVOs.iterator();
			while (i.hasNext()) {
				obj.getCampanhaPublicoAlvoProspectVOs().add((CampanhaPublicoAlvoProspectVO) i.next());
			}
			obj.setTotalProspectsSelecionadosCampanha(listaCampanhaPublicoAlvoProspectVOs.size());
		}
		if (obj.getTotalProspectsSelecionadosCampanha() == 0) {
			throw new Exception("Não foi encontrado nenhum prospect com os filtros utilizados");
		}
		if (campanhaVO.getWorkflow().getCodigo() != 0) {
			obj.setTempoMedioExecucaoWorkflowColaborador((obj.getTotalProspectsSelecionadosCampanha() * campanhaVO.getWorkflow().getTempoMedioGerarAgenda()) / 480);
			if (obj.getTempoMedioExecucaoWorkflowColaborador() == 0) {
				obj.setTempoMedioExecucaoWorkflowColaborador(1);
			}
		}
//		Integer nrConsultores = campanhaVO.getListaCampanhaColaborador().size();
//		if (nrConsultores > 0) {
//			obj.setMediaProspectColaborador(obj.getTotalProspectsSelecionadosCampanha() / nrConsultores);
//		} else {
//			obj.setMediaProspectColaborador(obj.getTotalProspectsSelecionadosCampanha());
//		}
		
		for (CampanhaPublicoAlvoVO objExistente : campanhaVO.getListaCampanhaPublicoAlvo()) {
			if (objExistente.getRegistroEntrada().getCodigo().equals(obj.getRegistroEntrada().getCodigo()) 
					&& objExistente.getUnidadeEnsino().getCodigo().equals(obj.getUnidadeEnsino().getCodigo()) 
					&& objExistente.getCursoInteresse().getCodigo().equals(obj.getCursoInteresse().getCodigo()) 
					&& objExistente.getCampanhaCaptacao().getCodigo().equals(obj.getCampanhaCaptacao().getCodigo()) 
					&& objExistente.getTipoEmpresa().equals(obj.getTipoEmpresa()) 
					&& objExistente.getFormacaoAcademica().equals(obj.getFormacaoAcademica()) 
					&& objExistente.getRenda().equals(obj.getRenda()) 
					&& objExistente.getCurso().getCodigo().equals(obj.getCurso().getCodigo()) 
					&& objExistente.getFormandos().equals(obj.getFormandos()) 
					&& objExistente.getCursando().equals(obj.getCursando())
					&& objExistente.getAbandonado().equals(obj.getAbandonado()) 
					&& objExistente.getTransferenciaInterna().equals(obj.getTransferenciaInterna())
					&& objExistente.getTransferenciaExterna().equals(obj.getTransferenciaExterna())
					&& objExistente.getPreMatriculaCancelada().equals(obj.getPreMatriculaCancelada())
					&& objExistente.getTrancados().equals(obj.getTrancados()) 
					&& objExistente.getDiasInadimplencia().equals(obj.getDiasInadimplencia())
					&& objExistente.getInadimplentes().equals(obj.getInadimplentes()) 
					&& objExistente.getPossiveisFormandos().equals(obj.getPossiveisFormandos()) 
					&& objExistente.getPreMatriculados().equals(obj.getPreMatriculados()) 
					&& objExistente.getCancelado().equals(obj.getCancelado())) {
				return;
			}
		}
		campanhaVO.getListaCampanhaPublicoAlvo().add(obj);
	}

	public void excluirObjCampanhaPublicoAlvoVOs(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO obj) throws Exception {
		campanhaVO.getListaCampanhaPublicoAlvo().remove(obj);
		// int index = 0;
		// for (CampanhaPublicoAlvoVO objExistente :
		// campanhaVO.getListaCampanhaPublicoAlvo()) {
		// campanhaVO.getListaCampanhaPublicoAlvo().remove(index);
		// return;
		//
		// //index++;
		// }
	}

	public Integer montarTotalProspectsSelecionadosCampanhaRegistroEntrada(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO obj) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT count(distinct p.codigo) AS contador FROM registroEntradaProspects AS rep ");
		sqlStr.append("INNER JOIN prospects AS p ON p.codigo = rep.prospects ");
		/**
		 * Filtro foi criado para garantir que aluno matriculados nos curso de
		 * interesse não sejam gerado agenda para que não sejam contactados
		 */

		sqlStr.append("WHERE (p.inativo = false) and rep.registroentrada =").append(obj.getRegistroEntrada().getCodigo());
		if (campanhaVO.getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES) && campanhaVO.getCurso().getCodigo() > 0) {
			sqlStr.append(" AND (p.pessoa is null or  p.pessoa not in (select aluno from matricula where aluno = p.pessoa  and matricula.situacao != 'PR' and curso = " + campanhaVO.getCurso().getCodigo() + ")) ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			return tabelaResultado.getInt("contador");
		}
		return 0;
	}

	public List<CampanhaPublicoAlvoProspectVO> consultarProspectsSelecionadosCampanhaRegistroEntrada(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO obj) throws Exception {
		List<CampanhaPublicoAlvoProspectVO> campanhaPublicoAlvoProspect = new ArrayList<CampanhaPublicoAlvoProspectVO>(0);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT distinct p.codigo AS prospects_codigo, p.emailPrincipal, p.cpf, p.pessoa, p.consultorPadrao AS prospects_consultorPadrao FROM registroEntradaProspects AS rep ");
		sqlStr.append("INNER JOIN prospects AS p ON p.codigo = rep.prospects ");
		sqlStr.append("WHERE (p.inativo = false) and rep.registroentrada =").append(obj.getRegistroEntrada().getCodigo());
		if (campanhaVO.getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES) && campanhaVO.getCurso().getCodigo() > 0) {
			sqlStr.append(" AND (p.pessoa is null or  p.pessoa not in (select aluno from matricula where aluno = p.pessoa  and matricula.situacao != 'PR' and curso = " + campanhaVO.getCurso().getCodigo() + ")) ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO = new CampanhaPublicoAlvoProspectVO();
			campanhaPublicoAlvoProspectVO.getProspect().setCodigo(tabelaResultado.getInt("prospects_codigo"));
			campanhaPublicoAlvoProspectVO.getProspect().getConsultorPadrao().setCodigo(tabelaResultado.getInt("prospects_consultorPadrao"));
			campanhaPublicoAlvoProspectVO.getProspect().getPessoa().setCodigo(tabelaResultado.getInt("pessoa"));
			campanhaPublicoAlvoProspectVO.getProspect().setCpf(tabelaResultado.getString("cpf"));
			campanhaPublicoAlvoProspectVO.getProspect().setEmailPrincipal(tabelaResultado.getString("emailPrincipal"));
			campanhaPublicoAlvoProspect.add(campanhaPublicoAlvoProspectVO);
		}
		return campanhaPublicoAlvoProspect;
	}

	public Integer montarTotalProspectsSelecionadosCampanhaRevisitacaoCarteira(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO obj) throws Exception {
		StringBuilder sqlStr = new StringBuilder();

		if (obj.getTipoPublicoAlvo().equals("CD")) {
			sqlStr.append(" select count(distinct pessoa.codigo) as contador from inscricao ");
			sqlStr.append(" left join pessoa on pessoa.codigo = inscricao.candidato");
			sqlStr.append(" left join resultadoprocessoseletivo on resultadoprocessoseletivo.inscricao = inscricao.codigo ");
			sqlStr.append(" left join procseletivo on inscricao.procseletivo = procseletivo.codigo ");
			sqlStr.append(" left join unidadeensino on inscricao.unidadeensino = unidadeensino.codigo ");
			sqlStr.append(" left join unidadeensinocurso as unidadeensinocurso1 on unidadeensinocurso1.codigo = inscricao.cursoopcao1 ");
			sqlStr.append(" left join unidadeensinocurso as unidadeensinocurso2 on unidadeensinocurso2.codigo = inscricao.cursoopcao2 ");
			sqlStr.append(" left join unidadeensinocurso as unidadeensinocurso3 on unidadeensinocurso3.codigo = inscricao.cursoopcao3 ");
			sqlStr.append(" left join contareceber on pessoa.codigo = contareceber.pessoa ");
			sqlStr.append(" inner join itemprocseletivodataprova on itemprocseletivodataprova.procseletivo = procseletivo.codigo ");
			sqlStr.append(" WHERE 1 = 1 ");
                        sqlStr.append(" and inscricao.situacaoinscricao != '").append("CANCELADO_OUTRA_INSCRICAO").append("'");
                        
			// CAMPO ADICIONADO PARA QUE NÃO SEJA GERADO CAMPANHA PARA
			// RESPONSÁVEL FINANCEIRO
			// CÓDIGO SERÁ IMPLEMENTADO
			// HOJE A CAMPANHA NÃO LIGA PARA O RESPONSÁVEL FINANCEIRO E SIM PARA
			// O ALUNO
			// QUE É ERRADO
			sqlStr.append(" and contareceber.responsavelfinanceiro is null ");
			//sqlStr.append(" and contareceber.tipoorigem in('MAT','MEN') ");
			if (obj.getProcessoSeletivoVO().getCodigo() != null && obj.getProcessoSeletivoVO().getCodigo() != 0) {
				sqlStr.append(" AND procseletivo.codigo = ").append(obj.getProcessoSeletivoVO().getCodigo());
			}
                        if ((obj.getAno() != null) && (!obj.getAno().equals(""))) {
                                if (obj.getAno().length() == 2) {
                                    obj.setAno("20" + obj.getAno());
                                }
				sqlStr.append(" AND procseletivo.ano = '").append(obj.getAno()).append("'");
                        }
                        if ((obj.getSemestre() != null) && (!obj.getSemestre().equals(""))) {
				sqlStr.append(" AND procseletivo.semestre = '").append(obj.getSemestre()).append("'");
                        }                        
			if (obj.getTodasOpcoesCurso() && obj.getCursoInteresse().getCurso().getCodigo() != 0) {
				sqlStr.append(" AND (unidadeensinocurso1.curso = ").append(obj.getCursoInteresse().getCurso().getCodigo());
				sqlStr.append(" or unidadeensinocurso2.curso = ").append(obj.getCursoInteresse().getCurso().getCodigo());
				sqlStr.append(" or unidadeensinocurso3.curso = ").append(obj.getCursoInteresse().getCurso().getCodigo());
				sqlStr.append(" )");
			} else if (obj.getCursoInteresse().getCurso().getCodigo() != 0) {
				sqlStr.append(" AND unidadeensinocurso1.curso = ").append(obj.getCursoInteresse().getCurso().getCodigo());
			}
			if (obj.getDataInicioProcessoSeletivo() != null && obj.getDataTerminoProcessoSeletivo() != null) {
				sqlStr.append(" AND itemprocseletivodataprova.dataprova  BETWEEN '");
				sqlStr.append(Uteis.getData(obj.getDataInicioProcessoSeletivo(), "dd/MM/yyyy"));
				sqlStr.append("' AND '");
				sqlStr.append(Uteis.getData(obj.getDataTerminoProcessoSeletivo(), "dd/MM/yyyy"));
				sqlStr.append("' ");
			}
			if (obj.getTodasOpcoesCurso()) {
				if (obj.getAprovado() && !obj.getReprovado()) {
					sqlStr.append("AND ( resultadoprocessoseletivo.resultadoprimeiraopcao = 'AP' ");
					sqlStr.append(" or resultadoprocessoseletivo.resultadosegundaopcao = 'AP' ");
					sqlStr.append(" or resultadoprocessoseletivo.resultadoterceiraopcao = 'AP' )");
				} else if (!obj.getAprovado() && obj.getReprovado()) {
					sqlStr.append("AND ( resultadoprocessoseletivo.resultadoprimeiraopcao = 'RE' ");
					sqlStr.append(" or resultadoprocessoseletivo.resultadosegundaopcao = 'RE' ");
					sqlStr.append(" or resultadoprocessoseletivo.resultadoterceiraopcao = 'RE' )");
				}
			} else {
				if (obj.getAprovado() && !obj.getReprovado()) {
					sqlStr.append("AND  resultadoprocessoseletivo.resultadoprimeiraopcao = 'AP' ");
				} else if (!obj.getAprovado() && obj.getReprovado()) {
					sqlStr.append("AND  resultadoprocessoseletivo.resultadoprimeiraopcao = 'RE' ");
				}
			}
		} else {
			if (obj.getTipoPublicoAlvo().equals("AL")) {
				sqlStr.append("SELECT count(distinct pes.codigo) AS contador FROM pessoa AS pes ");
				sqlStr.append("INNER JOIN matricula ON  pes.codigo = matricula.aluno ");
				sqlStr.append("LEFT JOIN prospects AS p ON  pes.codprospect = p.codigo ");
				sqlStr.append("LEFT JOIN cursointeresse AS ci ON ci.prospects = p.codigo ");
				
			} else {
				sqlStr.append("SELECT count(distinct p.codigo) AS contador FROM prospects AS p ");
				sqlStr.append("LEFT JOIN cursointeresse AS ci ON ci.prospects = p.codigo ");
				sqlStr.append("LEFT JOIN pessoa AS pes ON  pes.codprospect = p.codigo ");
			}
			if(campanhaVO.getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_ALUNOS_COBRANCA)){
				sqlStr.append(" LEFT JOIN compromissoagendapessoahorario on compromissoagendapessoahorario.prospect = p.codigo ");
			}
			sqlStr.append(" left join contareceber on pes.codigo = contareceber.pessoa ");
			sqlStr.append("LEFT JOIN curso AS cursoProspect ON ci.curso = cursoProspect.codigo ");
			if (obj.getTipoPublicoAlvo().equals("AL") && obj.getAplicarFiltrosParaMatriculadosNoCurso()) {

				sqlStr.append("LEFT JOIN contareceber AS cr ON pes.codigo = cr.pessoa ");
				sqlStr.append("LEFT join historico AS hist ON  matricula.matricula = hist.matricula ");
				sqlStr.append("LEFT join matriculaperiodo AS mp ON mp.codigo = hist.matriculaperiodo ");
				sqlStr.append("INNER JOIN curso AS cur ON cur.codigo = matricula.curso  ");
			}
			if (obj.getCampanhaCaptacao().getCodigo() != 0) {
				sqlStr.append("LEFT JOIN campanhapublicoalvoprospect ON  campanhapublicoalvoprospect.prospect = p.codigo ");
				sqlStr.append("LEFT JOIN campanhapublicoalvo ON  campanhapublicoalvo.codigo = campanhapublicoalvoprospect.campanhapublicoalvo ");
				sqlStr.append("LEFT JOIN campanha ON  campanha.codigo = campanhapublicoalvo.campanha ");
			}
			if (!obj.getFormacaoAcademica().getEscolaridade().equals("") && obj.getTipoPublicoAlvo().equals("AL")) {
				sqlStr.append("LEFT JOIN formacaoAcademica ON  pes.codigo = formacaoAcademica.pessoa ");
			}
			sqlStr.append("WHERE (p.inativo = false  or p.inativo is null) ");
			// CAMPO ADICIONADO PARA QUE NÃO SEJA GERADO CAMPANHA PARA
			// RESPONSÁVEL FINANCEIRO
			// CÓDIGO SERÁ IMPLEMENTADO
			// HOJE A CAMPANHA NÃO LIGA PARA O RESPONSÁVEL FINANCEIRO E SIM PARA
			// O ALUNO
			// QUE É ERRADO
			if(obj.getTipoPublicoAlvo().equals("AL")) {
				sqlStr.append(" and contareceber.responsavelfinanceiro is null ");
				sqlStr.append(" and contareceber.tipoorigem in('MAT','MEN') ");
			}
			if(campanhaVO.getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_ALUNOS_COBRANCA)){
				if(campanhaVO.getCodigo() != 0){
//					sqlStr.append(" and compromissoagendapessoahorario.tiposituacaocompromissoenum <> 'AGUARDANDO_CONTATO' ");
//					sqlStr.append("and compromissoagendapessoahorario.campanha = ").append(campanhaVO.getCodigo());
				}
			}
			
			if (obj.getUnidadeEnsino().getCodigo() != 0) {
				if (obj.getTipoPublicoAlvo().equals("AL")) {
					sqlStr.append(" AND matricula.unidadeEnsino =");
				} else {
					sqlStr.append(" AND p.unidadeEnsino =");
				}
				sqlStr.append(obj.getUnidadeEnsino().getCodigo());
			}
			if (obj.getCampanhaCaptacao().getCodigo() != 0) {
				sqlStr.append(" AND campanha.codigo = ");
				sqlStr.append(obj.getCampanhaCaptacao().getCodigo());
			}
			if (!obj.getTipoEmpresa().equals(TipoEmpresaProspectEnum.NENHUM)) {
				sqlStr.append(" AND p.tipoempresa ='");
				sqlStr.append(obj.getTipoEmpresa());
				sqlStr.append("'");
			}
			if (!obj.getRenda().equals(RendaProspectEnum.NENHUM)) {
				sqlStr.append(" AND p.renda ='");
				sqlStr.append(obj.getRenda());
				sqlStr.append("'");
			}
			if (obj.getCursoInteresse().getCurso().getCodigo() != 0) {
				sqlStr.append(" AND cursoProspect.codigo = ");
				sqlStr.append(obj.getCursoInteresse().getCurso().getCodigo());
				/**
				 * Filtro foi criado para garantir que aluno matriculados nos
				 * curso de interesse não sejam gerado agenda para que não sejam
				 * contactados
				 */
				if (campanhaVO.getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES)) {
					if (obj.getTipoPublicoAlvo().equals("AL")) {
						sqlStr.append(" AND pes.codigo not in ((select aluno from matricula where aluno = pes.codigo  and matricula.situacao != 'PR' and curso = " + obj.getCursoInteresse().getCurso().getCodigo() + "))  ");
					} else {
						sqlStr.append(" AND (pes.codigo is null or  pes.codigo not in (select aluno from matricula where aluno = pes.codigo  and matricula.situacao != 'PR' and curso = " + obj.getCursoInteresse().getCurso().getCodigo() + ")) ");
					}
				}
			}
			/**
			 * Filtro foi criado para garantir que aluno matriculados nos curso
			 * de interesse não sejam gerado agenda para que não sejam
			 * contactados
			 */
			if (campanhaVO.getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES) && campanhaVO.getCurso().getCodigo() > 0) {
				if (obj.getTipoPublicoAlvo().equals("AL")) {
					sqlStr.append(" AND pes.codigo not in ((select aluno from matricula where aluno = pes.codigo  and matricula.situacao != 'PR' and curso = " + campanhaVO.getCurso().getCodigo() + "))  ");
				} else {
					sqlStr.append(" AND (pes.codigo is null or  pes.codigo not in (select aluno from matricula where aluno = pes.codigo  and matricula.situacao != 'PR' and curso = " + campanhaVO.getCurso().getCodigo() + ")) ");
				}
			}
			if (!obj.getFormacaoAcademica().getEscolaridade().equals("0") && !obj.getFormacaoAcademica().getEscolaridade().equals("") && obj.getTipoPublicoAlvo().equals("AL")) {
				sqlStr.append(" AND formacaoAcademica.escolaridade = '");
				sqlStr.append(obj.getFormacaoAcademica().getEscolaridade());
				sqlStr.append("'");
			}
			if (obj.getTipoPublicoAlvo().equals("AL") && obj.getCurso().getCodigo() != 0) {
				sqlStr.append(" AND cur.codigo =");
				sqlStr.append(obj.getCurso().getCodigo());
			}
			if (obj.getTipoPublicoAlvo().equals("AL")) {
				if (obj.getAlunosComSerasa() && obj.getAlunosSemSerasa()) {
					sqlStr.append("");
				} else if (obj.getAlunosComSerasa() && !obj.getAlunosSemSerasa()) {
					sqlStr.append("AND  matricula.matriculaserasa  = 'true' ");
				} else if (obj.getAlunosSemSerasa() && !obj.getAlunosComSerasa()) {
					sqlStr.append(" AND matricula.matriculaserasa  = 'false' ");
				}
			}
			sqlStr.append(realizarValidacaoSituacoesMaticula(obj));
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			return tabelaResultado.getInt("contador");
		}
		return 0;
	}

	@Override
	public List<CampanhaPublicoAlvoProspectVO> consultarProspectsSelecionadosCampanhaRevisitacaoCarteira(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO obj, List<SegmentacaoProspectVO> segmentacaoProspectVOs) throws Exception {
		List<CampanhaPublicoAlvoProspectVO> campanhaPublicoAlvoProspect = new ArrayList<CampanhaPublicoAlvoProspectVO>(0);
		StringBuilder sqlStr = new StringBuilder();
		
		if (obj.getTipoPublicoAlvo().equals("CD")) {
			consultarProspectsSelecinadosPorCandidado(obj, sqlStr);
		} else {
			consultarProspectsSelecinadosPorAlunoPorProspect(campanhaVO, obj, segmentacaoProspectVOs, sqlStr);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO = new CampanhaPublicoAlvoProspectVO();
			if (obj.getTipoPublicoAlvo().equals("AL") || obj.getTipoPublicoAlvo().equals("CD")) {
				campanhaPublicoAlvoProspectVO.getPessoa().setCodigo(tabelaResultado.getInt("pes_codigo"));
				campanhaPublicoAlvoProspectVO.getPessoa().setCPF(tabelaResultado.getString("cpf"));
				campanhaPublicoAlvoProspectVO.getPessoa().setEmail(tabelaResultado.getString("email"));
			} else {
				campanhaPublicoAlvoProspectVO.getProspect().setCodigo(tabelaResultado.getInt("pes_codigo"));
				campanhaPublicoAlvoProspectVO.getPessoa().setCPF(tabelaResultado.getString("cpf"));
				campanhaPublicoAlvoProspectVO.getProspect().getConsultorPadrao().setCodigo(tabelaResultado.getInt("pes_consultorPadrao"));
			}
			campanhaPublicoAlvoProspect.add(campanhaPublicoAlvoProspectVO);
		}
		return campanhaPublicoAlvoProspect;
	}

	private void consultarProspectsSelecinadosPorAlunoPorProspect(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO obj, List<SegmentacaoProspectVO> segmentacaoProspectVOs, StringBuilder sqlStr) throws Exception {
		if (obj.getTipoPublicoAlvo().equals("AL")) {
			sqlStr.append("SELECT distinct pessoa.codigo AS pes_codigo, pessoa.cpf, pessoa.email FROM pessoa ");
			sqlStr.append("INNER JOIN matricula ON  pessoa.codigo = matricula.aluno ");
			sqlStr.append("INNER JOIN matriculaPeriodo mp ON mp.matricula = matricula.matricula ");
			sqlStr.append("and mp.codigo = (select matriculaPeriodo.codigo from matriculaPeriodo where matriculaPeriodo.matricula = matricula.matricula ");
			sqlStr.append("order by (MatriculaPeriodo.ano || '/' || MatriculaPeriodo.semestre) desc, case when matriculaperiodo.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, matriculaperiodo.codigo desc limit 1) ");
			sqlStr.append("LEFT JOIN prospects AS p ON  pessoa.codigo = p.pessoa ");
			sqlStr.append("INNER JOIN curso AS cur ON cur.codigo = matricula.curso  ");
			
		} else {
			sqlStr.append("SELECT distinct p.codigo AS pes_codigo, p.cpf,  p.consultorPadrao AS pes_consultorPadrao FROM prospects AS p ");				
			sqlStr.append("LEFT JOIN pessoa ON  pessoa.codigo = p.pessoa ");
		}							
		if (!obj.getFormacaoAcademica().getEscolaridade().equals("0") && !obj.getFormacaoAcademica().getEscolaridade().equals("") && obj.getTipoPublicoAlvo().equals("AL")) {
			sqlStr.append("INNER JOIN formacaoAcademica ON  pessoa.codigo = formacaoAcademica.pessoa ");
		}
		if (!obj.getTipoPublicoAlvo().equals("AL")) {
			sqlStr.append(" WHERE (p.inativo = false  or p.inativo is null)   ");
			if(!obj.isTrazerProspectAluno()){
				sqlStr.append(" and not exists (select matricula from matricula where  matricula.aluno = p.pessoa ) ");	
			}
			if(!obj.isTrazerProspectCandidado()){
				sqlStr.append(" and not exists (select inscricao.codigo from inscricao where inscricao.candidato = p.pessoa ) ");	
			}
			if(!obj.isTrazerProspectFuncionario()){
				sqlStr.append(" and not exists (select funcionario.codigo from funcionario where funcionario.pessoa = p.pessoa ) ");	
			}
			if(!obj.isTrazerProspectFiliacao()){
				sqlStr.append(" and not exists (select responsavelfinanceiro from contareceber where responsavelfinanceiro = p.pessoa ) ");	
			}
		}else{
			sqlStr.append(" WHERE 1 = 1   ");
		}			

		if (obj.getUnidadeEnsino().getCodigo() != 0) {
			if (obj.getTipoPublicoAlvo().equals("AL")) {
				sqlStr.append(" AND matricula.unidadeEnsino =");
			} else {
				sqlStr.append(" AND p.unidadeEnsino =");
			}
			sqlStr.append(obj.getUnidadeEnsino().getCodigo());
		}
		if (obj.getCampanhaCaptacao().getCodigo() != 0) {
			sqlStr.append(" AND exists (select compromissoagendapessoahorario.codigo from compromissoagendapessoahorario where compromissoagendapessoahorario.prospect = p.codigo and compromissoagendapessoahorario.campanha = ");
			sqlStr.append(obj.getCampanhaCaptacao().getCodigo());
			sqlStr.append(" limit 1)");
		}
		if (obj.getCursoInteresse().getCurso().getCodigo() != 0) {
			sqlStr.append(" and ").append(obj.getCursoInteresse().getCurso().getCodigo()).append(" ");
			sqlStr.append(" IN (select cursointeresse.curso from cursointeresse where cursointeresse.prospects = p.codigo ");
			if(obj.getUltimoCursoInteresse()){
				sqlStr.append(" order by cursointeresse.codigo desc limit 1 ");
			}
			sqlStr.append(" ) ");
			
			/**
			 * Filtro foi criado para garantir que aluno matriculados nos
			 * curso de interesse não sejam gerado agenda para que não sejam
			 * contactados
			 */
			if (campanhaVO.getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES)) {
				if (obj.getTipoPublicoAlvo().equals("AL")) {
					sqlStr.append(" AND pessoa.codigo not in ((select aluno from matricula where aluno = pessoa.codigo  and matricula.situacao != 'PR' and curso = " + obj.getCursoInteresse().getCurso().getCodigo() + "))  ");
				} else {
					sqlStr.append(" AND (pessoa.codigo is null or  pessoa.codigo not in (select aluno from matricula where aluno = pessoa.codigo  and matricula.situacao != 'PR' and curso = " + obj.getCursoInteresse().getCurso().getCodigo() + ")) ");
				}
			}
		}
		/**
		 * Filtro foi criado para garantir que aluno matriculados nos curso
		 * de interesse não sejam gerado agenda para que não sejam
		 * contactados
		 */
		if (campanhaVO.getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES) && campanhaVO.getCurso().getCodigo() > 0) {
			if (obj.getTipoPublicoAlvo().equals("AL")) {
				sqlStr.append(" AND pessoa.codigo not in ((select aluno from matricula where aluno = pessoa.codigo  and matricula.situacao != 'PR' and curso = " + campanhaVO.getCurso().getCodigo() + "))  ");
			} else {
				sqlStr.append(" AND (pessoa.codigo is null or  pessoa.codigo not in (select aluno from matricula where aluno = pessoa.codigo  and matricula.situacao != 'PR' and curso = " + campanhaVO.getCurso().getCodigo() + ")) ");
			}
		}
		if (!obj.getTipoEmpresa().equals(TipoEmpresaProspectEnum.NENHUM)) {
			sqlStr.append(" AND p.tipoempresa ='");
			sqlStr.append(obj.getTipoEmpresa());
			sqlStr.append("'");
		}
		if (!obj.getRenda().equals(RendaProspectEnum.NENHUM)) {
			sqlStr.append(" AND p.renda = '");
			sqlStr.append(obj.getRenda());
			sqlStr.append("'");
		}
		if (!obj.getFormacaoAcademica().getEscolaridade().equals("0") && !obj.getFormacaoAcademica().getEscolaridade().equals("") && obj.getTipoPublicoAlvo().equals("AL")) {
			sqlStr.append(" AND formacaoAcademica.escolaridade = '");
			sqlStr.append(obj.getFormacaoAcademica().getEscolaridade());
			sqlStr.append("'");
		}
		if (obj.getTipoPublicoAlvo().equals("AL") && obj.getCurso().getCodigo() != 0) {
			sqlStr.append(" AND cur.codigo = ");
			sqlStr.append(obj.getCurso().getCodigo());
		}
		if (obj.getTipoPublicoAlvo().equals("AL")) {
			if (obj.getAlunosComSerasa() && obj.getAlunosSemSerasa()) {
				sqlStr.append("");
			} else if (obj.getAlunosComSerasa() && !obj.getAlunosSemSerasa()) {
				sqlStr.append("AND  matricula.matriculaserasa  = 'true' ");
			} else if (obj.getAlunosSemSerasa() && !obj.getAlunosComSerasa()) {
				sqlStr.append(" AND matricula.matriculaserasa  = 'false' ");
			}
		}
		sqlStr.append(realizarValidacaoSituacoesMaticula(obj));
		if (obj.getDiasSemInteracao() > 0) {
			sqlStr.append(" and p.codigo in (select t.prospect from ( select prospect, max(datainicio) as data from interacaoworkflow group by prospect) as t where t.data < (current_date - ").append(obj.getDiasSemInteracao()).append(") ) ");
		}
		if(obj.getSegmentacao() && Uteis.isAtributoPreenchido(segmentacaoProspectVOs) && obj.getTipoPublicoAlvo().equals("PR")){
			StringBuilder in = new StringBuilder("");
			for(SegmentacaoProspectVO segmentacaoProspectVO: segmentacaoProspectVOs){
				if(Uteis.isAtributoPreenchido(segmentacaoProspectVO.getSegmentacaoOpcao().getCodigo())){
					if(in.length()>0){
						in.append(", ");
					}
					in.append(segmentacaoProspectVO.getSegmentacaoOpcao().getCodigo());
				}
			}
			if(in.length()>0){
				sqlStr.append(" and exists (select prospectsegmentacaoopcao.prospect from prospectsegmentacaoopcao ");
				sqlStr.append(" where prospectsegmentacaoopcao.segmentacaoopcao in (").append(in).append(") and prospectsegmentacaoopcao.prospect = p.codigo limit 1) ");
			}
		}
		if (!campanhaVO.getListaCampanhaPublicoAlvo().isEmpty()) {
			String pessoaNotIn = campanhaVO.getListaCampanhaPublicoAlvo().stream()
				.map(CampanhaPublicoAlvoVO::getCampanhaPublicoAlvoProspectVOs)
				.flatMap(Collection::stream)
				.map(cpapvo -> cpapvo.getPessoa().getCodigo().toString())
				.collect(Collectors.joining(", "));
			if (Uteis.isAtributoPreenchido(pessoaNotIn)) {
				sqlStr.append(" AND pessoa.codigo not in(").append(pessoaNotIn).append(") ");
			}
		}
		if (obj.getNaoGerarAgendaParaProspectsComAgendaJaExistente()) {
			sqlStr.append(" AND not exists (SELECT prospects.codigo FROM compromissoagendapessoahorario ");
			sqlStr.append(" INNER JOIN prospects ON prospects.codigo = compromissoagendapessoahorario.prospect ");
			sqlStr.append(" INNER JOIN campanha ON campanha.codigo = compromissoagendapessoahorario.campanha ");
			sqlStr.append(" WHERE p.codigo = prospects.codigo ");
			sqlStr.append(" AND (compromissoagendapessoahorario.datacompromisso::date >= '").append(Uteis.getDataJDBC(campanhaVO.getDataInicialVerificarJaExisteAgendaProspect())).append("' and compromissoagendapessoahorario.datacompromisso::date <='").append(Uteis.getDataJDBC(campanhaVO.getDataFinalVerificarJaExisteAgendaProspect())).append("') ");
			sqlStr.append(" AND ((compromissoagendapessoahorario.tiposituacaocompromissoenum = 'AGUARDANDO_CONTATO') or (compromissoagendapessoahorario.tiposituacaocompromissoenum = 'PARALIZADO'))");
			sqlStr.append(" AND (campanha.tipocampanha <> 'CONTACTAR_ALUNOS_COBRANCA') )");
		}
	}

	private void consultarProspectsSelecinadosPorCandidado(CampanhaPublicoAlvoVO obj, StringBuilder sqlStr) {
		sqlStr.append(" select distinct pessoa.codigo as pes_codigo, pessoa.cpf, pessoa.email from inscricao ");
		sqlStr.append(" left join pessoa on pessoa.codigo = inscricao.candidato");
		sqlStr.append(" left join resultadoprocessoseletivo on resultadoprocessoseletivo.inscricao = inscricao.codigo ");
		sqlStr.append(" left join procseletivo on inscricao.procseletivo = procseletivo.codigo ");
		sqlStr.append(" left join unidadeensino on inscricao.unidadeensino = unidadeensino.codigo ");
		sqlStr.append(" left join unidadeensinocurso as unidadeensinocurso1 on unidadeensinocurso1.codigo = inscricao.cursoopcao1 ");
		sqlStr.append(" left join unidadeensinocurso as unidadeensinocurso2 on unidadeensinocurso2.codigo = inscricao.cursoopcao2 ");
		sqlStr.append(" left join unidadeensinocurso as unidadeensinocurso3 on unidadeensinocurso3.codigo = inscricao.cursoopcao3 ");			
		sqlStr.append(" inner join itemprocseletivodataprova on itemprocseletivodataprova.procseletivo = procseletivo.codigo ");
		sqlStr.append(" WHERE 1 = 1 ");
		sqlStr.append(" and inscricao.situacaoinscricao != '").append("CANCELADO_OUTRA_INSCRICAO").append("'");
		//sqlStr.append(" and contareceber.tipoorigem in('MAT','MEN') ");
		if (obj.getProcessoSeletivoVO().getCodigo() != null && obj.getProcessoSeletivoVO().getCodigo() != 0) {
			sqlStr.append(" AND procseletivo.codigo = ").append(obj.getProcessoSeletivoVO().getCodigo());
		}
		            if ((obj.getAno() != null) && (!obj.getAno().equals(""))) {
		                    if (obj.getAno().length() == 2) {
		                        obj.setAno("20" + obj.getAno());
		                    }
			sqlStr.append(" AND procseletivo.ano = '").append(obj.getAno()).append("'");
		            }
		            if ((obj.getSemestre() != null) && (!obj.getSemestre().equals(""))) {
			sqlStr.append(" AND procseletivo.semestre = '").append(obj.getSemestre()).append("'");
		            }                        
		if (obj.getTodasOpcoesCurso() && obj.getCursoInteresse().getCurso().getCodigo() != 0) {
			sqlStr.append(" AND (unidadeensinocurso1.curso = ").append(obj.getCursoInteresse().getCurso().getCodigo());
			sqlStr.append(" or unidadeensinocurso2.curso = ").append(obj.getCursoInteresse().getCurso().getCodigo());
			sqlStr.append(" or unidadeensinocurso3.curso = ").append(obj.getCursoInteresse().getCurso().getCodigo());
			sqlStr.append(" )");
		} else if (obj.getCursoInteresse().getCurso().getCodigo() != 0) {
			sqlStr.append(" AND unidadeensinocurso1.curso = ").append(obj.getCursoInteresse().getCurso().getCodigo());
		}
		if (obj.getDataInicioProcessoSeletivo() != null && obj.getDataTerminoProcessoSeletivo() != null) {
			sqlStr.append(" AND itemprocseletivodataprova.dataprova  BETWEEN '");
			sqlStr.append(Uteis.getData(obj.getDataInicioProcessoSeletivo(), "dd/MM/yyyy"));
			sqlStr.append("' AND '");
			sqlStr.append(Uteis.getData(obj.getDataTerminoProcessoSeletivo(), "dd/MM/yyyy"));
			sqlStr.append("' ");
		}
		if (obj.getTodasOpcoesCurso()) {
			if (obj.getAprovado() && !obj.getReprovado()) {
				sqlStr.append("AND ( resultadoprocessoseletivo.resultadoprimeiraopcao = 'AP' ");
				sqlStr.append(" or resultadoprocessoseletivo.resultadosegundaopcao = 'AP' ");
				sqlStr.append(" or resultadoprocessoseletivo.resultadoterceiraopcao = 'AP' )");
			} else if (!obj.getAprovado() && obj.getReprovado()) {
				sqlStr.append("AND ( resultadoprocessoseletivo.resultadoprimeiraopcao = 'RE' ");
				sqlStr.append(" or resultadoprocessoseletivo.resultadosegundaopcao = 'RE' ");
				sqlStr.append(" or resultadoprocessoseletivo.resultadoterceiraopcao = 'RE' )");
			}
		} else {
			if (obj.getAprovado() && !obj.getReprovado()) {
				sqlStr.append("AND  resultadoprocessoseletivo.resultadoprimeiraopcao = 'AP' ");
			} else if (!obj.getAprovado() && obj.getReprovado()) {
				sqlStr.append("AND  resultadoprocessoseletivo.resultadoprimeiraopcao = 'RE' ");
			}
		}
	}
		
	@Override
	public List<CampanhaPublicoAlvoProspectVO> consultarProspectsSelecionadosCampanhaRevisitacaoCarteiraResponsavelFinanceiro(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO obj) throws Exception {
		List<CampanhaPublicoAlvoProspectVO> campanhaPublicoAlvoProspect = new ArrayList<CampanhaPublicoAlvoProspectVO>(0);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT distinct ");
		sqlStr.append(" case when cr.responsavelfinanceiro is not null then responsavelFinanceiro.codigo else pessoa.codigo end AS pes_codigo, ");
		sqlStr.append(" case when cr.responsavelfinanceiro is not null then responsavelFinanceiro.nome else pessoa.nome end AS nome, ");
		sqlStr.append(" case when cr.responsavelfinanceiro is not null then responsavelFinanceiro.cpf else pessoa.cpf end AS cpf, ");
		sqlStr.append(" case when cr.responsavelfinanceiro is not null then responsavelFinanceiro.email else pessoa.email end AS email ");
		sqlStr.append(" FROM matricula ");
		sqlStr.append(" INNER JOIN pessoa ON  matricula.aluno = pessoa.codigo ");		
		sqlStr.append(" INNER JOIN contareceber cr on cr.pessoa = matricula.aluno and cr.tipoorigem not in ('REQ', 'IPS', 'BCC') and cr.situacao = 'AR' and cr.datavencimento <= current_date ");
		sqlStr.append(" INNER JOIN matriculaPeriodo mp ON mp.matricula = matricula.matricula ");
		sqlStr.append(" and mp.codigo = (select matriculaPeriodo.codigo from matriculaPeriodo where matriculaPeriodo.matricula = matricula.matricula ");
		sqlStr.append(" order by (MatriculaPeriodo.ano || '/' || MatriculaPeriodo.semestre) desc, case when matriculaperiodo.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, matriculaperiodo.codigo desc limit 1) ");
		sqlStr.append(" INNER JOIN curso cur on cur.codigo = matricula.curso ");
		sqlStr.append(" LEFT JOIN pessoa responsavelFinanceiro on responsavelFinanceiro.codigo = cr.responsavelFinanceiro ");
		

		sqlStr.append("WHERE 1=1 ");		
		if (obj.getUnidadeEnsino().getCodigo() != 0) {
			sqlStr.append(" AND matricula.unidadeEnsino = ");
			sqlStr.append(obj.getUnidadeEnsino().getCodigo());
			sqlStr.append(" AND cr.unidadeEnsino = ");
			sqlStr.append(obj.getUnidadeEnsino().getCodigo());
		}
		if (obj.getCurso().getCodigo() != 0) {
			sqlStr.append(" AND cur.codigo =");
			sqlStr.append(obj.getCurso().getCodigo());
		}

		if (obj.getAlunosComSerasa() && obj.getAlunosSemSerasa()) {
			sqlStr.append("");
		} else if (obj.getAlunosComSerasa() && !obj.getAlunosSemSerasa()) {
			sqlStr.append("AND  matricula.matriculaserasa  = 'true' ");
		} else if (obj.getAlunosSemSerasa() && !obj.getAlunosComSerasa()) {
			sqlStr.append(" AND matricula.matriculaserasa  = 'false' ");
		}

		sqlStr.append(realizarValidacaoSituacoesMaticula(obj));

		if (!campanhaVO.getListaCampanhaPublicoAlvo().isEmpty()) {
			String pessoaNotIn = campanhaVO.getListaCampanhaPublicoAlvo().stream()
				.map(CampanhaPublicoAlvoVO::getCampanhaPublicoAlvoProspectVOs)
				.flatMap(Collection::stream)
				.map(cpapvo -> cpapvo.getPessoa().getCodigo().toString())
				.collect(Collectors.joining(", "));
			if (Uteis.isAtributoPreenchido(pessoaNotIn)) {
				sqlStr.append(" AND pessoa.codigo not in(").append(pessoaNotIn).append(") ");
			}
		}
		if (obj.getNaoGerarAgendaParaProspectsComAgendaJaExistente()) {
			sqlStr.append(" AND pessoa.codigo not in(SELECT prospects.pessoa FROM compromissoagendapessoahorario ");
			sqlStr.append(" INNER JOIN prospects ON prospects.codigo = compromissoagendapessoahorario.prospect ");
			sqlStr.append(" INNER JOIN campanha ON campanha.codigo = compromissoagendapessoahorario.campanha ");
			sqlStr.append(" WHERE 1=1 ");
			sqlStr.append(" AND (compromissoagendapessoahorario.datacompromisso::date >= '").append(Uteis.getDataJDBC(campanhaVO.getDataInicialVerificarJaExisteAgendaProspect())).append("' and compromissoagendapessoahorario.datacompromisso::date <='").append(Uteis.getDataJDBC(campanhaVO.getDataFinalVerificarJaExisteAgendaProspect())).append("') ");
			sqlStr.append(" AND ((compromissoagendapessoahorario.tiposituacaocompromissoenum = 'AGUARDANDO_CONTATO') or (compromissoagendapessoahorario.tiposituacaocompromissoenum = 'PARALIZADO'))");
			sqlStr.append(" AND (campanha.tipocampanha <> 'CONTACTAR_ALUNOS_COBRANCA') ) ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		
		HashMap<String, List<PessoaVO>> mapNomeDuplicadoVOs = new HashMap<String, List<PessoaVO>>(0);
		List<PessoaVO> listaResponsavelFinanceiroDuplicadoVOs = new ArrayList<PessoaVO>(0);
		while (tabelaResultado.next()) {
			CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO = new CampanhaPublicoAlvoProspectVO();
			campanhaPublicoAlvoProspectVO.getPessoa().setCodigo(tabelaResultado.getInt("pes_codigo"));
			campanhaPublicoAlvoProspectVO.getPessoa().setNome(tabelaResultado.getString("nome"));
			campanhaPublicoAlvoProspectVO.getPessoa().setCPF(tabelaResultado.getString("cpf"));
			campanhaPublicoAlvoProspectVO.getPessoa().setEmail(tabelaResultado.getString("email"));
			campanhaPublicoAlvoProspectVO.getProspect().setResponsavelFinanceiro(true);
			
			if (mapNomeDuplicadoVOs.containsKey(tabelaResultado.getString("nome"))) {
				List<PessoaVO> listaMapPessoaVOs = mapNomeDuplicadoVOs.get(tabelaResultado.getString("nome"));
				
				for (PessoaVO pessoaVO : listaMapPessoaVOs) {
					if ((!pessoaVO.getCodigo().equals(campanhaPublicoAlvoProspectVO.getPessoa().getCodigo()) &&
							(pessoaVO.getCPF().replace(".", "").replace("-", "").equals(campanhaPublicoAlvoProspectVO.getPessoa().getCPF().replace(".", "").replace("-", ""))
							|| pessoaVO.getEmail().equals(campanhaPublicoAlvoProspectVO.getPessoa().getEmail())))) {
						listaMapPessoaVOs.add(campanhaPublicoAlvoProspectVO.getPessoa());
						listaResponsavelFinanceiroDuplicadoVOs.addAll(listaMapPessoaVOs);
						break;
					}
				}
			} else {
				List<PessoaVO> listaPessoaVOs = new ArrayList<PessoaVO>(0);
				listaPessoaVOs.add(campanhaPublicoAlvoProspectVO.getPessoa());
				mapNomeDuplicadoVOs.put(tabelaResultado.getString("nome"), listaPessoaVOs);
			}

			campanhaPublicoAlvoProspect.add(campanhaPublicoAlvoProspectVO);
		}
		obj.setListaResponsavelFinanceiroDuplicadoVOs(listaResponsavelFinanceiroDuplicadoVOs);
		return campanhaPublicoAlvoProspect;
	}

	public String realizarValidacaoSituacoesMaticula(CampanhaPublicoAlvoVO obj) throws Exception {
		StringBuilder sqlStrSituacao = new StringBuilder(""); 
		if (obj.getTipoPublicoAlvo().equals("AL") || obj.getTipoPublicoAlvo().equals("RF")) {
			String andOr = " and ( ";
			if (obj.getFormandos()) {
				sqlStrSituacao.append(andOr).append("   matricula.situacao = 'FO' ");
				andOr = " or ";
			}
			if (obj.getPossiveisFormandos()) {
				sqlStrSituacao.append(andOr);
				sqlStrSituacao.append(" exists ( ");						
				sqlStrSituacao.append(" select m.matricula as matricula ");	
				sqlStrSituacao.append(" from matricula  m   ");
				sqlStrSituacao.append(" inner join gradecurricular on  gradecurricular.codigo = m.gradecurricularatual  ");
				sqlStrSituacao.append(" where m.situacao = 'AT' and mp.situacaomatriculaperiodo in ('AT', 'FI') ");
				sqlStrSituacao.append(" and m.matricula = matricula.matricula ");	
				sqlStrSituacao.append(" group by m.matricula, gradecurricular.cargahoraria,  gradecurricular.totalcargahorariaestagio, gradecurricular.totalcargahorariaatividadecomplementar, gradecurricular.codigo ");
				sqlStrSituacao.append(" having not exists (select gradedisciplina.codigo from gradedisciplina ");		
				sqlStrSituacao.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");		
				sqlStrSituacao.append(" where periodoletivo.gradecurricular = m.gradecurricularatual and gradedisciplina.tipodisciplina in ('OB', 'LG') and gradedisciplina.codigo not in ( ");		
				sqlStrSituacao.append(" select gd.codigo from gradedisciplina gd inner join historico  on gd.codigo = historico.gradedisciplina ");		
				sqlStrSituacao.append(" where historico.matricula  = m.matricula and historico.matrizcurricular = m.gradecurricularatual and gd.codigo = gradedisciplina.codigo ");				
				sqlStrSituacao.append(" and gd.tipodisciplina in ('OB', 'LG') and historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'CS', 'AE', 'CE', 'AB') ) limit 1)  ");
				sqlStrSituacao.append(" and ((gradecurricular.cargahoraria - (case when gradecurricular.totalcargahorariaestagio is null then 0 else gradecurricular.totalcargahorariaestagio end) ");
				sqlStrSituacao.append(" - (case when gradecurricular.totalcargahorariaatividadecomplementar is null then 0 else gradecurricular.totalcargahorariaatividadecomplementar end) = 0 ) ");
				sqlStrSituacao.append(" or (gradecurricular.cargahoraria - (case when gradecurricular.totalcargahorariaestagio is null then 0 else gradecurricular.totalcargahorariaestagio end) ");
				sqlStrSituacao.append(" - (case when gradecurricular.totalcargahorariaatividadecomplementar is null then 0 else gradecurricular.totalcargahorariaatividadecomplementar end) <=   ");
				sqlStrSituacao.append(" (select sum(cargahoraria) from (select sum(gradedisciplina.cargahoraria) as cargahoraria from gradedisciplina inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
				sqlStrSituacao.append(" where periodoletivo.gradecurricular = gradecurricular.codigo and gradedisciplina.tipodisciplina not in ('OP', 'LO') ");
				sqlStrSituacao.append(" and exists (select historico.codigo from historico where historico.matricula = m.matricula and historico.matrizcurricular = gradecurricular.codigo and historico.gradedisciplina  = gradedisciplina.codigo and historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CS', 'CH', 'AE', 'AB', 'CE') ) ");
				sqlStrSituacao.append(" union all "); 
				sqlStrSituacao.append(" select historico.cargahorariadisciplina from historico "); 
				sqlStrSituacao.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = historico.gradecurriculargrupooptativadisciplina ");
				sqlStrSituacao.append(" inner join gradecurriculargrupooptativa on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
				sqlStrSituacao.append(" where historico.matricula  = m.matricula and historico.matrizcurricular = gradecurricular.codigo ");
				sqlStrSituacao.append(" and historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CS','CH', 'AE') and gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo ");
				
				sqlStrSituacao.append(" ) as optativas ))) ");
				sqlStrSituacao.append(" ) ");
				andOr = " or ";
			}
			if (obj.getCursando()) {
				sqlStrSituacao.append(andOr).append("  (matricula.situacao = 'AT' AND mp.situacaomatriculaperiodo in ('AT', 'CO', 'FI')) ");
				andOr = " or ";
			}
			if (obj.getCancelado()) {
				sqlStrSituacao.append(andOr).append("   matricula.situacao = 'CA' ");
				andOr = " or ";
			}
			if (obj.getPreMatriculados()) {
				sqlStrSituacao.append(andOr).append("   (matricula.situacao = 'AT' AND mp.situacaomatriculaperiodo = 'PR')  ");
				andOr = " or ";
			}
			if (obj.getTrancados()) {
				sqlStrSituacao.append(andOr).append("   matricula.situacao = 'TR' ");
				andOr = " or ";
			}
			if (obj.getAbandonado()) {
				sqlStrSituacao.append(andOr).append("   matricula.situacao = 'AC' ");
				andOr = " or ";
			}
			if (obj.getTransferenciaInterna()) {
				sqlStrSituacao.append(andOr).append("   matricula.situacao = 'TI' ");
				andOr = " or ";
			}
			if (obj.getTransferenciaExterna()) {
				sqlStrSituacao.append(andOr).append("   matricula.situacao = 'TS' ");
				andOr = " or ";
			}
			if (obj.getPreMatriculaCancelada()) {
				sqlStrSituacao.append(andOr).append("  (matricula.situacao = 'AT'  AND mp.situacaomatriculaperiodo = 'PC') ");
				andOr = " or ";
			}
			if (obj.getFormandos() || obj.getCancelado() || obj.getCursando() || obj.getTrancados() || obj.getPossiveisFormandos() || obj.getPreMatriculados()
					|| obj.getAbandonado() || obj.getTransferenciaInterna() || obj.getTransferenciaExterna() || obj.getPreMatriculaCancelada()) {
				sqlStrSituacao.append(" ) ");
			}
			andOr = " and ( ";
			boolean aplicouFiltroFinanceiro = false;
			if (obj.getInadimplentes() && obj.getDiasInadimplencia().equals(0) && obj.getDataInicialInadimplencia() == null && obj.getDataFinalInadimplencia() == null) {			
				if (obj.getTipoPublicoAlvo().equals("AL")) {
					sqlStrSituacao.append(andOr).append(" exists (select cr.codigo from  contareceber cr where  cr.tipoorigem not in ('REQ', 'BCC', 'IPS') ");
					sqlStrSituacao.append(" and cr.pessoa = pessoa.codigo and cr.responsavelfinanceiro is null ");
					sqlStrSituacao.append(" and cr.situacao = 'AR' AND cr.datavencimento < current_date ");
				}else{
					sqlStrSituacao.append(andOr).append(" cr.situacao = 'AR' AND cr.datavencimento < current_date ");
				}
				if (!obj.getUnidadeEnsino().getCodigo().equals(0)) {
					sqlStrSituacao.append(" and cr.unidadeensino = ").append(obj.getUnidadeEnsino().getCodigo());
				}
				sqlStrSituacao.append(obj.getTipoPublicoAlvo().equals("AL") ? " limit 1)" : "");
				aplicouFiltroFinanceiro = true;
				andOr = " and ";
			}
			if (obj.getDiasInadimplencia() > 0) {
				if (obj.getTipoPublicoAlvo().equals("AL")) {
					sqlStrSituacao.append(andOr).append(" exists (select cr.codigo from  contareceber cr where cr.tipoorigem not in ('REQ', 'BCC', 'IPS') ");
					sqlStrSituacao.append(" and cr.pessoa = pessoa.codigo and cr.responsavelfinanceiro is null ");
					sqlStrSituacao.append(" and cr.situacao = 'AR' AND cr.datavencimento < (now()::date - ").append(obj.getDiasInadimplencia()).append(")");
				}else{
					sqlStrSituacao.append(andOr).append(" cr.datavencimento < (current_date - '").append(obj.getDiasInadimplencia()).append(" day'::INTERVAL ) ");
				}
				if (!obj.getUnidadeEnsino().getCodigo().equals(0)) {
					sqlStrSituacao.append(" and cr.unidadeensino = ").append(obj.getUnidadeEnsino().getCodigo());
				}
				sqlStrSituacao.append(obj.getTipoPublicoAlvo().equals("AL") ? " limit 1)" : "");
				aplicouFiltroFinanceiro = true;
				andOr = " and ";
			}

			if (obj.getDataInicialInadimplencia() != null && obj.getDataFinalInadimplencia() != null) {
				if (obj.getTipoPublicoAlvo().equals("AL")) {
					sqlStrSituacao.append(andOr).append(" exists (select cr.codigo from  contareceber cr where cr.datavencimento < now()::date ");
					sqlStrSituacao.append(" and cr.pessoa = pessoa.codigo and cr.responsavelfinanceiro is null ");
					sqlStrSituacao.append(" and cr.tipoorigem not in ('REQ', 'BCC', 'IPS') and cr.situacao = 'AR' AND cr.datavencimento >= '").append(Uteis.getDataBD0000(obj.getDataInicialInadimplencia())).append("' and (cr.datavencimento <= '").append(Uteis.getDataBD2359(obj.getDataFinalInadimplencia())).append("') limit 1)  ");				
				}else{
					sqlStrSituacao.append(andOr).append(" cr.datavencimento >= '").append(Uteis.getDataBD0000(obj.getDataInicialInadimplencia())).append("' and (cr.datavencimento <= '").append(Uteis.getDataBD2359(obj.getDataFinalInadimplencia())).append("')  ");
				}
				aplicouFiltroFinanceiro = true;
				andOr = " and ";
			}
			if (obj.getDataInicialInadimplencia() != null && obj.getDataFinalInadimplencia() == null) {
				if (obj.getTipoPublicoAlvo().equals("AL")) {
					sqlStrSituacao.append(andOr).append(" exists (select cr.codigo from  contareceber cr where cr.datavencimento < now()::date ");
					sqlStrSituacao.append(" and cr.pessoa = pessoa.codigo and cr.responsavelfinanceiro is null ");
					sqlStrSituacao.append(" and cr.tipoorigem not in ('REQ', 'BCC', 'IPS') and cr.situacao = 'AR' AND cr.datavencimento >= '").append(Uteis.getDataBD0000(obj.getDataInicialInadimplencia())).append("' limit 1)  ");				
				}else{
					sqlStrSituacao.append(andOr).append(" cr.datavencimento >= '").append(Uteis.getDataBD0000(obj.getDataInicialInadimplencia())).append("' ");
				}
				aplicouFiltroFinanceiro = true;
				andOr = " and ";
			}
			if (obj.getDataInicialInadimplencia() == null && obj.getDataFinalInadimplencia() != null) {
				if (obj.getTipoPublicoAlvo().equals("AL")) {
					sqlStrSituacao.append(andOr).append(" exists (select cr.codigo from  contareceber cr where cr.datavencimento < now()::date ");
					sqlStrSituacao.append(" and cr.pessoa = pessoa.codigo and cr.responsavelfinanceiro is null ");
					sqlStrSituacao.append(" and cr.tipoorigem not in ('REQ', 'BCC', 'IPS') and cr.situacao = 'AR' and (cr.datavencimento <= '").append(Uteis.getDataBD2359(obj.getDataFinalInadimplencia())).append("') limit 1)  ");				
				}else{
					sqlStrSituacao.append(andOr).append(" (cr.datavencimento <= '").append(Uteis.getDataBD2359(obj.getDataFinalInadimplencia())).append("')  ");
				}
				aplicouFiltroFinanceiro = true;
				andOr = " and ";
			}
			if (aplicouFiltroFinanceiro) {
				sqlStrSituacao.append(" ) ");
			}
		}
		return sqlStrSituacao.toString();
	}

	public List<ProspectsVO> montarProspectsGeracaoAgenda(CampanhaPublicoAlvoVO obj) throws Exception {
		validarDadosAdicionar(obj);
		List<ProspectsVO> lista = new ArrayList<ProspectsVO>(0);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT prospects.codigo as codigo, prospects.consultorPadrao as consultorPadrao FROM campanhapublicoalvoprospect ");
		sqlStr.append(" inner join prospects on prospects.codigo =  campanhapublicoalvoprospect.prospect ");
		sqlStr.append("WHERE campanhapublicoalvo = ").append(obj.getCodigo());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			lista.add(montarProspects(tabelaResultado));
		}
		return lista;
	}

	public Double montarQuantidadeTotalContatosProspectsDia(Integer codigoCampanha) {
		String str = "SELECT qtdcontato FROM campanhacolaborador WHERE campanha = " + codigoCampanha;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str);
		Double contador = 0.0;
		Integer recebimentoVariavelSQL = 0;
		while (tabelaResultado.next()) {
			recebimentoVariavelSQL = tabelaResultado.getInt("qtdcontato");
			contador = contador + Double.valueOf(recebimentoVariavelSQL.toString());
		}
		return contador;
	}

	public void validarCursoInformadoFiltro(CampanhaPublicoAlvoVO obj) throws Exception {
		if (obj.getCurso().getCodigo() == null && (obj.getFormandos() || obj.getCancelado() || obj.getCursando() || obj.getTrancados() || obj.getInadimplentes() || obj.getDiasInadimplencia() > 0 || obj.getPossiveisFormandos())) {
			throw new Exception("Deve-se informar o curso para usar os filtros: Cancelado ou Formandos ou Trancados ou Inadimplentes");
		}
	}

	public void validarDadosAdicionar(CampanhaPublicoAlvoVO obj) throws Exception {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getCampanha().getWorkflow() == null || obj.getCampanha().getWorkflow().getCodigo() == 0) {
			throw new Exception("Para adicionar um público alvo deve-se informar o workflow (dados básicos)");
		}
		
//			if ((obj.getDataInicialInadimplencia() == null) && (obj.getDataFinalInadimplencia() == null)) {
//				throw new Exception("Para a opção Títulos Vencidos no Período ao menos uma data deve ser informada (data inicial ou final).");
//			}
			if ((obj.getDataInicialInadimplencia() != null) && (obj.getDataFinalInadimplencia() != null) && (obj.getDataInicialInadimplencia().compareTo(obj.getDataFinalInadimplencia()) > 0)) {
				throw new Exception("Para a opção Títulos Vencidos no Período a data inicial deve maior que a data final.");
			}
			if ((obj.getDataFinalInadimplencia() != null) && (obj.getDataFinalInadimplencia().compareTo(new Date()) > 0)) {
				throw new Exception("Para a opção Títulos Vencidos no Período a data final deve ser menor ou igual à data atual.");
			}
		// if (obj.getCurso().getCodigo() == 0 && (obj.getFormandos() ||
		// obj.getCancelado() || obj.getCursando() || obj.getTrancados() ||
		// obj.getInadimplentes() || obj.getInadimplentesDias() ||
		// obj.getPossiveisFormandos())) {
		// throw new
		// Exception("Para adicionar um dos campos de verdadeiro ou falso acima deve-se informar o CURSO (público alvo)");
		// }
	}

	public List<CampanhaPublicoAlvoVO> montarListaCampanhaPublicoAlvo(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT CampanhaPublicoAlvo.* FROM CampanhaPublicoAlvo WHERE Campanha = " + codigo;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public CampanhaPublicoAlvoVO montarCampanhaPublicoAlvo(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT CampanhaPublicoAlvo.* FROM CampanhaPublicoAlvo WHERE codigo =" + codigo;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados, usuario);
		}
		return new CampanhaPublicoAlvoVO();
	}

	public static List<CampanhaPublicoAlvoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<CampanhaPublicoAlvoVO> vetResultado = new ArrayList<CampanhaPublicoAlvoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	public static CampanhaPublicoAlvoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		CampanhaPublicoAlvoVO obj = new CampanhaPublicoAlvoVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		obj.getRegistroEntrada().setCodigo(dadosSQL.getInt("registroEntrada"));
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		obj.getCursoInteresse().setCodigo(dadosSQL.getInt("cursoInteresse"));
		obj.getCurso().setCodigo(dadosSQL.getInt("curso"));
		obj.getCampanha().setCodigo(dadosSQL.getInt("campanha"));
		obj.getCampanhaCaptacao().setCodigo(dadosSQL.getInt("campanhaCaptacao"));
		obj.setRenda(RendaProspectEnum.valueOf(dadosSQL.getString("renda")));
		obj.setTipoEmpresa(TipoEmpresaProspectEnum.valueOf(dadosSQL.getString("tipoEmpresa")));
		obj.setUltimoCursoInteresse(dadosSQL.getBoolean("ultimoCursoInteresse"));
		obj.setDiasSemInteracao(dadosSQL.getInt("diasSemInterecao"));
		obj.getFormacaoAcademica().setEscolaridade(dadosSQL.getString("formacaoAcademica"));
		obj.setFormandos(dadosSQL.getBoolean("formandos"));
		obj.setPossiveisFormandos(dadosSQL.getBoolean("possiveisFormandos"));
		obj.setCursando(dadosSQL.getBoolean("cursando"));
		obj.setPreMatriculados(dadosSQL.getBoolean("preMatriculado"));
		obj.setTrancados(dadosSQL.getBoolean("trancados"));
		obj.setCancelado(dadosSQL.getBoolean("cancelado"));
		
		obj.setAbandonado(dadosSQL.getBoolean("abandonado"));
		obj.setTransferenciaInterna(dadosSQL.getBoolean("transferenciaInterna"));
		obj.setTransferenciaExterna(dadosSQL.getBoolean("transferenciaExterna"));
		obj.setPreMatriculaCancelada(dadosSQL.getBoolean("preMatriculaCancelada"));
		obj.setAdicionadoDinamicamente(dadosSQL.getBoolean("adicionadoDinamicamente"));
		
		obj.setInadimplentes(dadosSQL.getBoolean("inadimplentes"));
		obj.setDiasInadimplencia(dadosSQL.getInt("diasInadimplencia"));
		montarDadosRegistroEntrada(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosCampanha(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosCampanhaCaptacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosCursoInteresse(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		obj.setNovoObj(Boolean.FALSE);
		// montarDadosCampanhaPublicoAlvo(obj,
		// Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
		return obj;
	}

	public static void montarCampanhaPublicoAlvoProspect(CampanhaPublicoAlvoVO obj) throws Exception {
		obj.setCampanhaPublicoAlvoProspectVOs(getFacadeFactory().getCampanhaPublicoAlvoProspectFacade().consultarPorCampanhaPublicoAlvo(obj.getCodigo()));
	}

	public static ProspectsVO montarProspects(SqlRowSet dadosSQL) throws Exception {
		ProspectsVO obj = new ProspectsVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getConsultorPadrao().setCodigo(dadosSQL.getInt("consultorPadrao"));
		return obj;
	}

	public static void montarDadosRegistroEntrada(CampanhaPublicoAlvoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getRegistroEntrada().getCodigo().intValue() == 0) {
			obj.setRegistroEntrada(new RegistroEntradaVO());
			return;
		}

		obj.setRegistroEntrada(getFacadeFactory().getRegistroEntradaFacade().consultarPorChavePrimaria(obj.getRegistroEntrada().getCodigo(), nivelMontarDados, false, usuario));
	}

	public static void montarDadosCampanha(CampanhaPublicoAlvoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCampanha().getCodigo().intValue() == 0) {
			obj.setCampanha(new CampanhaVO());
			return;
		}

		obj.setCampanha(getFacadeFactory().getCampanhaFacade().consultarPorChavePrimaria(obj.getCampanha().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosCampanhaCaptacao(CampanhaPublicoAlvoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCampanhaCaptacao().getCodigo().intValue() == 0) {
			obj.setCampanhaCaptacao(new CampanhaVO());
			return;
		}

		obj.setCampanhaCaptacao(getFacadeFactory().getCampanhaFacade().consultarPorChavePrimaria(obj.getCampanhaCaptacao().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosUnidadeEnsino(CampanhaPublicoAlvoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsino(new UnidadeEnsinoVO());
			return;
		}

		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosCursoInteresse(CampanhaPublicoAlvoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCursoInteresse().getCodigo().intValue() == 0) {
			obj.setCursoInteresse(new CursoInteresseVO());
			return;
		}

		obj.setCursoInteresse(getFacadeFactory().getCursoInteresseFacade().consultarPorChavePrimaria(obj.getCursoInteresse().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosCurso(CampanhaPublicoAlvoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCurso().getCodigo().intValue() == 0) {
			obj.setCurso(new CursoVO());
			return;
		}

		obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), nivelMontarDados, false, usuario));
	}

	public static String getIdEntidade() {
		return CampanhaMidia.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		CampanhaMidia.idEntidade = idEntidade;
	}
	
	public List<CampanhaPublicoAlvoVO> consultarCampanhaPublicoAlvoPorCampanha(Integer campanha, UsuarioVO usuarioVO) {
    	StringBuilder sb = new StringBuilder();
    	sb.append(" select campanhapublicoalvo.codigo AS \"campanhapublicoalvo.codigo\", unidadeEnsino.codigo AS \"unidadeEnsino.codigo\", unidadeEnsino.nome AS \"unidadeEnsino.nome\", campanhapublicoalvo.cursointeresse AS \"campanhapublicoalvo.cursointeresse\", ");
    	sb.append(" ci.codigo AS \"cursointeresse.codigoCurso\", ci.nome AS \"cursointeresse.nomeCurso\",  ");
    	sb.append(" curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", campanhapublicoalvo.renda AS \"campanhapublicoalvo.renda\", campanhapublicoalvo.campanha AS \"campanhapublicoalvo.campanha\", ");
    	sb.append(" registroEntrada.descricao AS \"registroEntrada.descricao\", registroEntrada.codigo AS \"registroEntrada.codigo\", ");
    	sb.append(" campanhapublicoalvo.tipoempresa AS \"campanhapublicoalvo.tipoempresa\", campanhapublicoalvo.formacaoacademica AS \"campanhapublicoalvo.formacaoacademica\", ");
    	sb.append(" campanhapublicoalvo.formandos AS \"campanhapublicoalvo.formandos\", campanhapublicoalvo.possiveisFormandos AS \"campanhapublicoalvo.possiveisFormandos\",  ");
    	sb.append(" campanhapublicoalvo.cursando AS \"campanhapublicoalvo.cursando\", campanhapublicoalvo.prematriculado AS \"campanhapublicoalvo.prematriculado\", campanhapublicoalvo.trancados AS \"campanhapublicoalvo.trancados\", ");
    	sb.append(" campanhapublicoalvo.cancelado AS \"campanhapublicoalvo.cancelado\", campanhapublicoalvo.inadimplentes AS \"campanhapublicoalvo.inadimplentes\", campanhapublicoalvo.inadimplentesdias AS \"campanhapublicoalvo.inadimplentesdias\", ");
    	sb.append(" campanhapublicoalvo.abandonado AS \"campanhapublicoalvo.abandonado\", campanhapublicoalvo.transferenciaInterna AS \"campanhapublicoalvo.transferenciaInterna\", campanhapublicoalvo.transferenciaExterna AS \"campanhapublicoalvo.transferenciaExterna\", campanhapublicoalvo.preMatriculaCancelada AS \"campanhapublicoalvo.preMatriculaCancelada\", ");
    	sb.append(" campanhapublicoalvo.diasinadimplencia AS \"campanhapublicoalvo.diasinadimplencia\", campanhapublicoalvo.totalprospectsselecionadoscampanha AS \"campanhapublicoalvo.totalprospectsselecionadoscampanha\", ");
    	sb.append(" campanhapublicoalvo.tempomedioexecucaoworkflowcolaborador AS \"campanhapublicoalvo.tempomedioexecucaoworkflowcolaborador\", campanhapublicoalvo.mediaprospectcolaborador AS \"campanhapublicoalvo.mediaprospectcolaborador\", ");
    	sb.append(" campanhapublicoalvo.campanhacaptacao AS \"campanhapublicoalvo.campanhacaptacao\", campanhapublicoalvo.alunoscomserasa AS \"campanhapublicoalvo.alunoscomserasa\", ");
    	sb.append(" campanhapublicoalvo.alunossemserasa AS \"campanhapublicoalvo.alunossemserasa\", campanhapublicoalvo.processoseletivo AS \"campanhapublicoalvo.processoseletivo\", campanhapublicoalvo.aprovado AS \"campanhapublicoalvo.aprovado\", ");
    	sb.append(" campanhapublicoalvo.reprovado AS \"campanhapublicoalvo.reprovado\", campanhapublicoalvo.datainicioprocessoseletivo AS \"campanhapublicoalvo.datainicioprocessoseletivo\", ");
    	sb.append(" campanhapublicoalvo.dataterminoprocessoseletivo AS \"campanhapublicoalvo.dataterminoprocessoseletivo\", campanhapublicoalvo.todasopcoescurso AS \"campanhapublicoalvo.todasopcoescurso\", ");
    	sb.append(" campanhapublicoalvo.tipopublicoalvo AS \"campanhapublicoalvo.tipopublicoalvo\", ");
    	sb.append(" campanhapublicoalvo.ultimoCursoInteresse AS \"campanhapublicoalvo.ultimoCursoInteresse\", ");
    	sb.append(" campanhapublicoalvo.diasSemInteracao AS \"campanhapublicoalvo.diasSemInteracao\", ");				
    	sb.append(" campanhapublicoalvo.naoGerarAgendaParaProspectsComAgendaJaExistente,  campanhapublicoalvo.tipoGerarAgendaCampanha, campanhapublicoalvo.adicionadodinamicamente  ");
    	sb.append(" FROM campanhapublicoalvo ");
    	sb.append(" LEFT JOIN unidadeEnsino on unidadeEnsino.codigo = campanhapublicoalvo.unidadeEnsino ");
    	sb.append(" LEFT JOIN curso on curso.codigo = campanhapublicoalvo.curso ");
    	sb.append(" LEFT JOIN registroEntrada ON registroEntrada.codigo = campanhapublicoalvo.registroentrada ");
    	sb.append(" LEFT JOIN cursoInteresse ON cursoInteresse.codigo = campanhapublicoalvo.cursoInteresse ");
    	sb.append(" LEFT JOIN curso ci ON ci.codigo = cursoInteresse.curso ");
    	sb.append(" WHERE campanhapublicoalvo.campanha = ").append(campanha);
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	List<CampanhaPublicoAlvoVO> campanhaPublicoAlvoVOs = new ArrayList<CampanhaPublicoAlvoVO>(0);
    	while (tabelaResultado.next()) {
    		CampanhaPublicoAlvoVO obj = new CampanhaPublicoAlvoVO();
    		obj.setCodigo(tabelaResultado.getInt("campanhapublicoalvo.codigo"));
    		obj.getUnidadeEnsino().setCodigo(tabelaResultado.getInt("unidadeEnsino.codigo"));
    		obj.getUnidadeEnsino().setNome(tabelaResultado.getString("unidadeEnsino.nome"));
    		obj.getCursoInteresse().setCodigo(tabelaResultado.getInt("campanhapublicoalvo.cursointeresse"));
    		obj.getCursoInteresse().getCurso().setCodigo(tabelaResultado.getInt("cursointeresse.codigoCurso"));
    		obj.getCursoInteresse().getCurso().setNome(tabelaResultado.getString("cursointeresse.nomeCurso"));
    		obj.getCurso().setCodigo(tabelaResultado.getInt("curso.codigo"));
    		obj.getCurso().setNome(tabelaResultado.getString("curso.nome"));
    		if (tabelaResultado.getString("campanhapublicoalvo.renda") != null) {			
				obj.setRenda(RendaProspectEnum.valueOf(tabelaResultado.getString("campanhapublicoalvo.renda")));
			} else {
				obj.setRenda(RendaProspectEnum.valueOf("NENHUM"));
			}
    		obj.getCampanha().setCodigo(tabelaResultado.getInt("campanhapublicoalvo.campanha"));
    		obj.getRegistroEntrada().setCodigo(tabelaResultado.getInt("registroEntrada.codigo"));
    		obj.getRegistroEntrada().setDescricao(tabelaResultado.getString("registroEntrada.descricao"));
    		if (tabelaResultado.getString("campanhapublicoalvo.tipoempresa") != null) {			
				obj.setTipoEmpresa(TipoEmpresaProspectEnum.valueOf(tabelaResultado.getString("campanhapublicoalvo.tipoempresa")));
			} else {
				obj.setTipoEmpresa(TipoEmpresaProspectEnum.valueOf("NENHUM"));
			}
    		obj.getFormacaoAcademica().setEscolaridade(tabelaResultado.getString("campanhapublicoalvo.formacaoacademica"));
    		obj.setUltimoCursoInteresse(tabelaResultado.getBoolean("campanhapublicoalvo.ultimoCursoInteresse"));
    		obj.setDiasSemInteracao(tabelaResultado.getInt("campanhapublicoalvo.diasSemInteracao"));
			obj.setFormandos(tabelaResultado.getBoolean("campanhapublicoalvo.formandos"));
    		obj.setPossiveisFormandos(tabelaResultado.getBoolean("campanhapublicoalvo.possiveisFormandos"));
    		obj.setCursando(tabelaResultado.getBoolean("campanhapublicoalvo.cursando"));
    		obj.setPreMatriculados(tabelaResultado.getBoolean("campanhapublicoalvo.prematriculado"));
    		obj.setTrancados(tabelaResultado.getBoolean("campanhapublicoalvo.trancados"));
    		obj.setCancelado(tabelaResultado.getBoolean("campanhapublicoalvo.cancelado"));
    		
    		obj.setAbandonado(tabelaResultado.getBoolean("campanhapublicoalvo.abandonado"));
    		obj.setTransferenciaInterna(tabelaResultado.getBoolean("campanhapublicoalvo.transferenciaInterna"));
    		obj.setTransferenciaExterna(tabelaResultado.getBoolean("campanhapublicoalvo.transferenciaExterna"));
    		obj.setPreMatriculaCancelada(tabelaResultado.getBoolean("campanhapublicoalvo.preMatriculaCancelada"));
    		obj.setAdicionadoDinamicamente(tabelaResultado.getBoolean("adicionadodinamicamente"));
    		
    		obj.setInadimplentes(tabelaResultado.getBoolean("campanhapublicoalvo.inadimplentes"));
    		obj.setDiasInadimplencia(tabelaResultado.getInt("campanhapublicoalvo.diasinadimplencia"));
    		obj.setTotalProspectsSelecionadosCampanha(tabelaResultado.getInt("campanhapublicoalvo.totalprospectsselecionadoscampanha"));
    		obj.setTempoMedioExecucaoWorkflowColaborador(tabelaResultado.getInt("campanhapublicoalvo.tempomedioexecucaoworkflowcolaborador"));
    		obj.setMediaProspectColaborador(tabelaResultado.getInt("campanhapublicoalvo.mediaprospectcolaborador"));
    		obj.getCampanhaCaptacao().setCodigo(tabelaResultado.getInt("campanhapublicoalvo.campanhacaptacao"));
    		obj.setAlunosSemSerasa(tabelaResultado.getBoolean("campanhapublicoalvo.alunossemserasa"));
    		obj.setAlunosComSerasa(tabelaResultado.getBoolean("campanhapublicoalvo.alunoscomserasa"));
    		obj.getProcessoSeletivoVO().setCodigo(tabelaResultado.getInt("campanhapublicoalvo.processoseletivo"));
    		obj.setAprovado(tabelaResultado.getBoolean("campanhapublicoalvo.aprovado"));
    		obj.setReprovado(tabelaResultado.getBoolean("campanhapublicoalvo.reprovado"));
    		obj.setDataInicioProcessoSeletivo(tabelaResultado.getDate("campanhapublicoalvo.datainicioprocessoseletivo"));
    		obj.setDataTerminoProcessoSeletivo(tabelaResultado.getDate("campanhapublicoalvo.dataterminoprocessoseletivo"));
    		obj.setTodasOpcoesCurso(tabelaResultado.getBoolean("campanhapublicoalvo.todasopcoescurso"));
    		obj.setTipoPublicoAlvo(tabelaResultado.getString("campanhapublicoalvo.tipopublicoalvo"));
    		obj.setNaoGerarAgendaParaProspectsComAgendaJaExistente(tabelaResultado.getBoolean("naoGerarAgendaParaProspectsComAgendaJaExistente"));
    		if (tabelaResultado.getString("tipoGerarAgendaCampanha") != null) {
    			obj.setTipoGerarAgendaCampanha(TipoGerarAgendaCampanhaEnum.valueOf(tabelaResultado.getString("tipoGerarAgendaCampanha")));
    		}
                campanhaPublicoAlvoVOs.add(obj);
	}
    	return campanhaPublicoAlvoVOs;
    }
	
	public void adicionarObjCampanhaPublicoAlvoVOs(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO obj, Boolean revisitacaoCarteira, ProgressBarVO progressBar, UsuarioVO usuarioVO, List<SegmentacaoProspectVO> segmentacaoProspectVOs) throws Exception {
		obj.setCampanha(campanhaVO);
		getFacadeFactory().getCampanhaFacade().validarCampanhaGerarAgenda(campanhaVO);
		validarDadosAdicionar(obj);
		if (revisitacaoCarteira) {
			List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectVOs = null;
			if (!obj.getTipoPublicoAlvo().equals("RF")) {
				listaCampanhaPublicoAlvoProspectVOs = consultarProspectsSelecionadosCampanhaRevisitacaoCarteira(campanhaVO, obj, segmentacaoProspectVOs);
			} else {
				listaCampanhaPublicoAlvoProspectVOs = consultarProspectsSelecionadosCampanhaRevisitacaoCarteiraResponsavelFinanceiro(campanhaVO, obj);
			}
			obj.setCampanhaPublicoAlvoProspectVOs(listaCampanhaPublicoAlvoProspectVOs);
			obj.setTotalProspectsSelecionadosCampanha(listaCampanhaPublicoAlvoProspectVOs.size());
		} else {
			List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectVOs = consultarProspectsSelecionadosCampanhaRegistroEntrada(campanhaVO, obj);
			obj.setCampanhaPublicoAlvoProspectVOs(listaCampanhaPublicoAlvoProspectVOs);
			obj.setTotalProspectsSelecionadosCampanha(listaCampanhaPublicoAlvoProspectVOs.size());
		}
		
		if (obj.getTotalProspectsSelecionadosCampanha() == 0) {
			if (obj.getNaoGerarAgendaParaProspectsComAgendaJaExistente()) {
				throw new Exception("Não foi encontrado nenhum prospect com os filtros utilizados. A configuração está marcada para não gerar agenda para prospects com agenda já existente. Nesse caso não irá trazer prospects nessa condição. ");
			} else {
				throw new Exception("Não foi encontrado nenhum prospect com os filtros utilizados");
			}
		}
		progressBar.setMaxValue(obj.getTotalProspectsSelecionadosCampanha());
		if (campanhaVO.getWorkflow().getCodigo() != 0) {
			obj.setTempoMedioExecucaoWorkflowColaborador((obj.getTotalProspectsSelecionadosCampanha() * campanhaVO.getWorkflow().getTempoMedioGerarAgenda()) / 480);
			if (obj.getTempoMedioExecucaoWorkflowColaborador() == 0) {
				obj.setTempoMedioExecucaoWorkflowColaborador(1);
			}
		}
//		Integer nrConsultores = campanhaVO.getListaCampanhaColaborador().size();
//		if (nrConsultores > 0) {
//			obj.setMediaProspectColaborador(obj.getTotalProspectsSelecionadosCampanha() / nrConsultores);
//		} else {
//			obj.setMediaProspectColaborador(obj.getTotalProspectsSelecionadosCampanha());
//		}
		for (CampanhaPublicoAlvoVO objExistente : campanhaVO.getListaCampanhaPublicoAlvo()) {
			if (objExistente.getRegistroEntrada().getCodigo().equals(obj.getRegistroEntrada().getCodigo()) 
					&& objExistente.getUnidadeEnsino().getCodigo().equals(obj.getUnidadeEnsino().getCodigo()) 
					&& objExistente.getCursoInteresse().getCodigo().equals(obj.getCursoInteresse().getCodigo()) 
					&& objExistente.getCampanhaCaptacao().getCodigo().equals(obj.getCampanhaCaptacao().getCodigo()) 
					&& objExistente.getTipoEmpresa().equals(obj.getTipoEmpresa()) 
					&& objExistente.getFormacaoAcademica().getEscolaridade().equals(obj.getFormacaoAcademica().getEscolaridade()) 
					&& objExistente.getRenda().equals(obj.getRenda()) 
					&& objExistente.getCurso().getCodigo().equals(obj.getCurso().getCodigo()) 
					&& objExistente.getFormandos().equals(obj.getFormandos()) 
					&& objExistente.getCursando().equals(obj.getCursando()) 
					&& objExistente.getTrancados().equals(obj.getTrancados()) 
					&& objExistente.getDiasInadimplencia().equals(obj.getDiasInadimplencia())
					&& objExistente.getInadimplentes().equals(obj.getInadimplentes()) 
					&& objExistente.getPossiveisFormandos().equals(obj.getPossiveisFormandos()) 
					&& objExistente.getPreMatriculados().equals(obj.getPreMatriculados())
					&& objExistente.getAbandonado().equals(obj.getAbandonado())
					&& objExistente.getTransferenciaInterna().equals(obj.getTransferenciaInterna())
					&& objExistente.getTransferenciaExterna().equals(obj.getTransferenciaExterna())
					&& objExistente.getPreMatriculaCancelada().equals(obj.getPreMatriculaCancelada())
					&& objExistente.getCancelado().equals(obj.getCancelado())
					&& objExistente.getTipoPublicoAlvo().equals(obj.getTipoPublicoAlvo())) {
				
					 
				return;
			}
		}
		getFacadeFactory().getCampanhaFacade().realizarCarregamentoAgendaDistribuindoProspectsPorConsultor(campanhaVO, obj, progressBar, false, usuarioVO, false);
		campanhaVO.getListaCampanhaPublicoAlvo().add(obj);
		
	}
	
	@Override
	public void alterarCampanhaPublicoAlvoRegeracaoAgenda(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, UsuarioVO usuarioVO) throws Exception {
		if (campanhaPublicoAlvoVO.getCodigo().equals(0) ) {
			incluirSemSubordinada(campanhaPublicoAlvoVO, usuarioVO);
		}
		getFacadeFactory().getCampanhaPublicoAlvoProspectFacade().alterarCampanhaPublicoAlvoProspectPorCampanhaPublicoAlvo(campanhaVO.getCodigo(), campanhaPublicoAlvoVO.getCodigo(), campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs(), usuarioVO);
	}
}
