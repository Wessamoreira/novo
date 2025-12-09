package negocio.facade.jdbc.academico;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.academico.CalendarioLancamentoPlanoEnsinoVO;
import negocio.comuns.academico.ConteudoPlanejamentoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PerguntaItemRespostaOrigemVO;
import negocio.comuns.academico.PerguntaRespostaOrigemVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.PlanoEnsinoHorarioAulaVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.academico.ReferenciaBibliograficaVO;
import negocio.comuns.academico.RespostaPerguntaRespostaOrigemVO;
import negocio.comuns.academico.enumeradores.SituacaoPlanoEnsinoEnum;
import negocio.comuns.academico.enumeradores.SituacaoQuestionarioRespostaOrigemEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.enumeradores.EscopoPerguntaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisCalculoFolhaPagamento;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.PlanoEnsinoInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class PlanoEnsino extends ControleAcesso implements PlanoEnsinoInterfaceFacade {

	private static final long serialVersionUID = -570452295543720689L;

	private static final String idEntidade = "PlanoEnsino";

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(PlanoEnsinoVO planoEnsinoVO, UsuarioVO usuarioLogado) throws Exception {
		if (planoEnsinoVO.getNovoObj()) {
			planoEnsinoVO.setDataCadastro(new Date());
			planoEnsinoVO.getResponsavelAutorizacao();
			planoEnsinoVO.getResponsavel().setCodigo(usuarioLogado.getCodigo());
			planoEnsinoVO.getResponsavel().setNome(usuarioLogado.getNome());
			incluir(planoEnsinoVO, usuarioLogado);
		} else {
			alterar(planoEnsinoVO, usuarioLogado);
		}
		
	}
	
	@Override
	public void realizarClonagem(PlanoEnsinoVO planoEnsinoVO, boolean clonarVisaoProfessor, boolean clonarVisaoAdmCoordenador, UsuarioVO usuarioVO) throws Exception {
		if (clonarVisaoProfessor) {
			if (Uteis.isAtributoPreenchido(planoEnsinoVO.getProfessorResponsavel()) && !planoEnsinoVO.getProfessorResponsavel().getCodigo().equals(usuarioVO.getPessoa().getCodigo())) {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidade("ClonarPlanoEnsinoProfessor", usuarioVO);
			}
			planoEnsinoVO.getResponsavel().setPessoa(usuarioVO.getPessoa());
			planoEnsinoVO.setProfessorResponsavel(usuarioVO.getPessoa());
		}
		if (clonarVisaoAdmCoordenador) {
			planoEnsinoVO.getResponsavel().setCodigo(usuarioVO.getCodigo());
			planoEnsinoVO.getResponsavel().setPessoa(usuarioVO.getPessoa());
			planoEnsinoVO.setProfessorResponsavel(new PessoaVO());
		}
		planoEnsinoVO.setNovoObj(true);
		planoEnsinoVO.setAno(null);
		planoEnsinoVO.setSemestre(null);
		planoEnsinoVO.setCodigo(0);
		planoEnsinoVO.setSituacao(SituacaoPlanoEnsinoEnum.PENDENTE.getValor());
		planoEnsinoVO.setDescricao(planoEnsinoVO.getDescricao() + " - CLONE");
		for (ReferenciaBibliograficaVO referenciaBibliograficaVO : planoEnsinoVO.getReferenciaBibliograficaVOs()) {
			referenciaBibliograficaVO.setCodigo(0);
			referenciaBibliograficaVO.setNovoObj(true);
			referenciaBibliograficaVO.setPlanoEnsino(planoEnsinoVO);
		}
		if(Uteis.isAtributoPreenchido(planoEnsinoVO.getQuestionarioRespostaOrigemVO().getQuestionarioVO().getCodigo())) {
			planoEnsinoVO.getQuestionarioRespostaOrigemVO().setCodigo(0);
			planoEnsinoVO.getQuestionarioRespostaOrigemVO().setNovoObj(true);
			planoEnsinoVO.getQuestionarioRespostaOrigemVO().setPlanoEnsinoVO(planoEnsinoVO);
			for(PerguntaRespostaOrigemVO perguntaRespostaOrigemVO : planoEnsinoVO.getQuestionarioRespostaOrigemVO().getPerguntaRespostaOrigemVOs()) {
				perguntaRespostaOrigemVO.setCodigo(0);
				perguntaRespostaOrigemVO.setNovoObj(true);
				perguntaRespostaOrigemVO.setQuestionarioRespostaOrigemVO(planoEnsinoVO.getQuestionarioRespostaOrigemVO());
				for(PerguntaItemRespostaOrigemVO perguntaItemRespostaOrigemVO: perguntaRespostaOrigemVO.getPerguntaItemRespostaOrigemVOs()) {
					perguntaItemRespostaOrigemVO.setCodigo(0);
					perguntaItemRespostaOrigemVO.setNovoObj(true);
					perguntaItemRespostaOrigemVO.setPerguntaRespostaOrigemPrincipalVO(perguntaRespostaOrigemVO);
					perguntaItemRespostaOrigemVO.getPerguntaRespostaOrigemVO().setCodigo(0);
				}
				for(RespostaPerguntaRespostaOrigemVO respostaPerguntaRespostaOrigemVO : perguntaRespostaOrigemVO.getRespostaPerguntaRespostaOrigemVOs()) {
					respostaPerguntaRespostaOrigemVO.setCodigo(0);
					respostaPerguntaRespostaOrigemVO.setNovoObj(true);
					respostaPerguntaRespostaOrigemVO.setPerguntaRespostaOrigemVO(perguntaRespostaOrigemVO);
				}
				for(List<PerguntaItemRespostaOrigemVO> perguntaItemRespostaOrigemAdicionadaVOs : perguntaRespostaOrigemVO.getPerguntaItemRespostaOrigemAdicionadaVOs()) {
					for(PerguntaItemRespostaOrigemVO perguntaItemRespostaOrigemVO: perguntaItemRespostaOrigemAdicionadaVOs) {
						perguntaItemRespostaOrigemVO.setCodigo(0);
						perguntaItemRespostaOrigemVO.setNovoObj(true);
						perguntaItemRespostaOrigemVO.setPerguntaRespostaOrigemPrincipalVO(perguntaRespostaOrigemVO);
						perguntaItemRespostaOrigemVO.getPerguntaRespostaOrigemVO().setCodigo(0);
						for(RespostaPerguntaRespostaOrigemVO respostaPerguntaRespostaOrigemVO : perguntaItemRespostaOrigemVO.getPerguntaRespostaOrigemVO().getRespostaPerguntaRespostaOrigemVOs()) {
							respostaPerguntaRespostaOrigemVO.setNovoObj(true);
							respostaPerguntaRespostaOrigemVO.setPerguntaRespostaOrigemVO(perguntaItemRespostaOrigemVO.getPerguntaRespostaOrigemVO());
						}
					}
				}
			}
		}else {
			for (ConteudoPlanejamentoVO conteudoPlanejamentoVO : planoEnsinoVO.getConteudoPlanejamentoVOs()) {
				conteudoPlanejamentoVO.setCodigo(0);
				conteudoPlanejamentoVO.setNovoObj(true);
				conteudoPlanejamentoVO.setPlanoEnsino(planoEnsinoVO);
			}
		}
		for (PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO : planoEnsinoVO.getPlanoEnsinoHorarioAulaVOs()) {
			planoEnsinoHorarioAulaVO.setCodigo(0);
			planoEnsinoHorarioAulaVO.setNovoObj(true);
			planoEnsinoHorarioAulaVO.setPlanoEnsinoVO(planoEnsinoVO);
		}
	}

	public void validarDados(PlanoEnsinoVO planoEnsinoVO) throws ConsistirException {
		ConsistirException consistirException = new ConsistirException();
		validarDadosBasico(planoEnsinoVO, consistirException);

		Ordenacao.ordenarLista(planoEnsinoVO.getPlanoEnsinoHorarioAulaVOs(), "ordenacao");
		int x = 0;
		for (PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO : planoEnsinoVO.getPlanoEnsinoHorarioAulaVOs()) {
			getFacadeFactory().getPlanoEnsinoHorarioAulaFacade().validarDados(planoEnsinoHorarioAulaVO);
			int y  = 0;
			for (PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO2 : planoEnsinoVO.getPlanoEnsinoHorarioAulaVOs()) {
				if (x != y && planoEnsinoHorarioAulaVO2.getDiaSemana().equals(planoEnsinoHorarioAulaVO.getDiaSemana())) {
					if ((Uteis.realizarValidacaoHora1MaiorHora2(planoEnsinoHorarioAulaVO.getInicioAula(),planoEnsinoHorarioAulaVO2.getInicioAula())
							&& (Uteis.realizarValidacaoHora1MaiorHora2(planoEnsinoHorarioAulaVO2.getTerminoAula(),planoEnsinoHorarioAulaVO.getTerminoAula())
									|| planoEnsinoHorarioAulaVO2.getTerminoAula().equals(planoEnsinoHorarioAulaVO.getTerminoAula())))
							|| ((Uteis.realizarValidacaoHora1MaiorHora2(planoEnsinoHorarioAulaVO2.getInicioAula(),
									planoEnsinoHorarioAulaVO.getInicioAula())
									|| planoEnsinoHorarioAulaVO2.getInicioAula()
											.equals(planoEnsinoHorarioAulaVO.getInicioAula()))
									&& Uteis.realizarValidacaoHora1MaiorHora2(planoEnsinoHorarioAulaVO.getTerminoAula(),
											planoEnsinoHorarioAulaVO2.getInicioAula()))) {
						StringBuilder sb = new StringBuilder("A aula do(a) ");
						sb.append(planoEnsinoHorarioAulaVO.getDiaSemana().getDescricao()).append(" as ").append(planoEnsinoHorarioAulaVO.getInicioAula()).append(" - ").append(planoEnsinoHorarioAulaVO.getTerminoAula());
						sb.append(" está com choque de horário com a ");
						sb.append("aula do(a) ");
						sb.append(planoEnsinoHorarioAulaVO2.getDiaSemana().getDescricao()).append(" as ")
								.append(planoEnsinoHorarioAulaVO2.getInicioAula()).append(" - ")
								.append(planoEnsinoHorarioAulaVO2.getTerminoAula());
						throw new ConsistirException(sb.toString());
					}
				}
				y++;
			}
			x++;
		}
		if (!consistirException.getListaMensagemErro().isEmpty()) {
			throw consistirException;
		}
		consistirException = null;
	}
	
	private void validarDadosBasico(PlanoEnsinoVO planoEnsinoVO, ConsistirException consistirException) throws ConsistirException {
		if (planoEnsinoVO.getDescricao() == null || planoEnsinoVO.getDescricao().isEmpty()) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_PlanoEnsino_descricao"));
		}
//		if (!Uteis.isAtributoPreenchido(planoEnsinoVO.getUnidadeEnsino())) {
//			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_PlanoEnsino_unidadeEnsino"));
//		}
//		if (!Uteis.isAtributoPreenchido(planoEnsinoVO.getCurso())) {
//			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_PlanoEnsino_curso"));
//		}
		if (!Uteis.isAtributoPreenchido(planoEnsinoVO.getDisciplina())) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_PlanoEnsino_disciplina"));
		}
		if (planoEnsinoVO.getCurso().getAnual() || planoEnsinoVO.getCurso().getSemestral() || planoEnsinoVO.getPeriodicidade().equals("AN") || planoEnsinoVO.getPeriodicidade().equals("SE")) {
			if (planoEnsinoVO.getAno() == null || planoEnsinoVO.getAno().trim().isEmpty()) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_PlanoEnsino_ano"));
			}
			if (planoEnsinoVO.getAno() != null && planoEnsinoVO.getAno().trim().length() != 4) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_PlanoEnsino_anoInvalido"));
			}			
		}
		if (planoEnsinoVO.getCurso().getSemestral() || planoEnsinoVO.getPeriodicidade().equals("SE")) {
			if (!Uteis.isAtributoPreenchido(planoEnsinoVO.getSemestre())) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_PlanoEnsino_semestre"));
			}			
		}
		if (planoEnsinoVO.getSituacao().equals("0")) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_PlanoEnsino_situacao"));
		}
		if (!consistirException.getListaMensagemErro().isEmpty()) {
			throw consistirException;
		}
	}

	public void validarUnicidade(PlanoEnsinoVO planoEnsinoVO) throws Exception {
		List<PlanoEnsinoVO> planoEnsinoVOs = new ArrayList<PlanoEnsinoVO>(0);
		
		planoEnsinoVOs = consultarPlanoPorUnidadeEnsinoCursoDisciplinaAnoSemestre(planoEnsinoVO.getUnidadeEnsino().getCodigo(), planoEnsinoVO.getCurso().getCodigo(), planoEnsinoVO.getDisciplina().getCodigo(), planoEnsinoVO.getAno(), planoEnsinoVO.getSemestre(), planoEnsinoVO.getTurno().getCodigo(),  Uteis.NIVELMONTARDADOS_DADOSBASICOS, planoEnsinoVO.getResponsavel());
		if (Uteis.isAtributoPreenchido(planoEnsinoVOs)){
			for(PlanoEnsinoVO planoEnsinoVO2 : planoEnsinoVOs) {
				if (planoEnsinoVO != null && (!planoEnsinoVO2.getCodigo().equals(planoEnsinoVO.getCodigo()))) {
					if( planoEnsinoVO2.getUnidadeEnsino().getCodigo().equals(planoEnsinoVO.getUnidadeEnsino().getCodigo())
							&& planoEnsinoVO2.getCurso().getCodigo().equals(planoEnsinoVO.getCurso().getCodigo())
							&& planoEnsinoVO2.getDisciplina().getCodigo().equals(planoEnsinoVO.getDisciplina().getCodigo())
							&& planoEnsinoVO2.getAno().equals(planoEnsinoVO.getAno())
							&& planoEnsinoVO2.getSemestre().equals(planoEnsinoVO.getSemestre())
							&& planoEnsinoVO2.getProfessorResponsavel().getCodigo().equals(planoEnsinoVO.getProfessorResponsavel().getCodigo())
							&& planoEnsinoVO2.getTurno().getCodigo().equals(planoEnsinoVO.getTurno().getCodigo())) {
						throw new Exception(UteisJSF.internacionalizar("msg_PlanoEnsino_existente"));
					}			
				}
			}
		}				
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final PlanoEnsinoVO planoEnsinoVO, final UsuarioVO usuarioLogado) throws Exception {
		try {
			if(!usuarioLogado.getIsApresentarVisaoCoordenador() && !usuarioLogado.getIsApresentarVisaoProfessor()){
				incluir(idEntidade, usuarioLogado);
			}else if(usuarioLogado.getIsApresentarVisaoCoordenador() && usuarioLogado.getIsApresentarVisaoProfessor()){
				ControleAcesso.verificarPermissaoFuncionalidadeUsuario("CriarNovoPlanoEnsino", usuarioLogado);
			}
			if (usuarioLogado.getIsApresentarVisaoProfessor()) {
				validarCalendarioLancamentoPlanoEnsino(planoEnsinoVO, usuarioLogado.getIsApresentarVisaoProfessor());
			}
			if(Uteis.isAtributoPreenchido(planoEnsinoVO.getCurso())) {			
				validarPeriodicidadeCursoParaPlanoEnsino(planoEnsinoVO);
			}				

			validarDados(planoEnsinoVO);
			validarUnicidade(planoEnsinoVO);
			planoEnsinoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("INSERT INTO PlanoEnsino ");
					sql.append("(disciplina, curso, unidadeEnsino, ano, semestre, descricao, dataCadastro, responsavel, ementa, objetivoGeral, objetivoEspecifico, ");
					sql.append(" perfilegresso, estrategiaAvaliacao, procedimentoDidatico, situacao, professorResponsavel, turno, calendarioLancamentoPlanoEnsino, motivo, periodicidade, responsavelautorizacao) ");
					sql.append(" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?, ?, ?, ?) RETURNING codigo");
					sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, planoEnsinoVO.getDisciplina().getCodigo());
					if (Uteis.isAtributoPreenchido(planoEnsinoVO.getCurso())) {
						ps.setInt(x++, planoEnsinoVO.getCurso().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					if (Uteis.isAtributoPreenchido(planoEnsinoVO.getUnidadeEnsino())) {
						ps.setInt(x++, planoEnsinoVO.getUnidadeEnsino().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					ps.setString(x++, planoEnsinoVO.getAno());
					ps.setString(x++, planoEnsinoVO.getSemestre());
					ps.setString(x++, planoEnsinoVO.getDescricao());
					ps.setDate(x++, Uteis.getDataJDBC(planoEnsinoVO.getDataCadastro()));
					ps.setInt(x++, planoEnsinoVO.getResponsavel().getCodigo());
					ps.setString(x++, planoEnsinoVO.getEmenta());
					ps.setString(x++, planoEnsinoVO.getObjetivoGeral());
					ps.setString(x++, planoEnsinoVO.getObjetivoEspecifico());
					ps.setString(x++, planoEnsinoVO.getPerfilEgresso());
					ps.setString(x++, planoEnsinoVO.getEstrategiaAvaliacao());
					ps.setString(x++, planoEnsinoVO.getProcedimentoDidatico());
					ps.setString(x++, planoEnsinoVO.getSituacao());
					if (Uteis.isAtributoPreenchido(planoEnsinoVO.getProfessorResponsavel())) {
						ps.setInt(x++, planoEnsinoVO.getProfessorResponsavel().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}

					if (Uteis.isAtributoPreenchido(planoEnsinoVO.getTurno())) {
						ps.setInt(x++, planoEnsinoVO.getTurno().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					if (Uteis.isAtributoPreenchido(planoEnsinoVO.getCalendarioLancamentoPlanoEnsinoVO())) {
						ps.setInt(x++, planoEnsinoVO.getCalendarioLancamentoPlanoEnsinoVO().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					ps.setString(x++, planoEnsinoVO.getMotivo());
					ps.setString(x++, planoEnsinoVO.getPeriodicidade());
					if (Uteis.isAtributoPreenchido(planoEnsinoVO.getResponsavelAutorizacao())) {
						ps.setInt(x++, planoEnsinoVO.getResponsavelAutorizacao().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					return ps;
				}
			}, new ResultSetExtractor<Integer>() {
				@Override
				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getReferenciaBibliograficaFacade().incluirReferenciaBibliograficas(planoEnsinoVO, usuarioLogado);
			getFacadeFactory().getConteudoPlanejamentoFacade().incluirConteudoPlanejamentoVOs(planoEnsinoVO, usuarioLogado);
			getFacadeFactory().getPlanoEnsinoHorarioAulaFacade().incluirPlanoEnsinoHorarioAulaVOs(planoEnsinoVO, usuarioLogado);
			
			if (Uteis.isAtributoPreenchido(planoEnsinoVO.getQuestionarioRespostaOrigemVO().getQuestionarioVO())) {
				getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().incluir(planoEnsinoVO.getQuestionarioRespostaOrigemVO(), usuarioLogado);
			}			

			planoEnsinoVO.setNovoObj(false);
		} catch (Exception e) {
			planoEnsinoVO.setNovoObj(true);
			throw e;
		}
	}

	@Override
	public void validarCalendarioLancamentoPlanoEnsino(PlanoEnsinoVO planoEnsinoVO, boolean visaoProfessor) throws ConsistirException {
		ConsistirException consistirException = new ConsistirException();
		//validarDadosBasico(planoEnsinoVO, consistirException);
		CalendarioLancamentoPlanoEnsinoVO obj = new CalendarioLancamentoPlanoEnsinoVO();
		try {
			obj = getFacadeFactory().getCalendarioLancamentoPlanoEnsinoInterfaceFacade().consultarCalendarioLancamentoPorPlanoEnsino(planoEnsinoVO, visaoProfessor);
		} catch (Exception e) {
			throw consistirException;
		}

		if (Uteis.isAtributoPreenchido(obj)) {
			try {
				if (!UteisData.isDataDentroDoPeriodo(obj.getDataInicio(), obj.getDataFim(), new Date())) {
					throw new ConsistirException(UteisJSF.internacionalizar("msg_CalendarioLancamentoPlanoEnsino_validarPeriodoCadastrado")
							.replace("{0}", UteisData.getDataFormatada(obj.getDataInicio())).replace("{1}", UteisData.getDataFormatada(obj.getDataFim())));
				}
			} catch (Exception e) {
				consistirException.adicionarListaMensagemErro(e.getMessage());
				throw consistirException;
			}
			planoEnsinoVO.setCalendarioLancamentoPlanoEnsinoVO(obj);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final PlanoEnsinoVO planoEnsinoVO, final UsuarioVO usuarioLogado) throws Exception {
		try {
			if(!usuarioLogado.getIsApresentarVisaoCoordenador() && !usuarioLogado.getIsApresentarVisaoProfessor()){
				alterar(idEntidade, usuarioLogado);
			}
			if(Uteis.isAtributoPreenchido(planoEnsinoVO.getCurso())) {			
				validarPeriodicidadeCursoParaPlanoEnsino(planoEnsinoVO);
			}	
			validarDados(planoEnsinoVO);
			validarUnicidade(planoEnsinoVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("UPDATE PlanoEnsino SET ");
					sql.append("disciplina=?, curso=?, unidadeEnsino=?, ano=?, semestre=?, descricao=?, dataCadastro=?, responsavel=?, ementa=?, objetivoGeral=?, objetivoEspecifico=?, ");
					sql.append(" perfilegresso=?, estrategiaAvaliacao=?, procedimentoDidatico=?, situacao=?, professorResponsavel=?, turno =?, calendarioLancamentoPlanoEnsino = ?, motivo = ?, periodicidade = ?, responsavelAutorizacao = ?");
					sql.append(" WHERE codigo = ? ");
					sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, planoEnsinoVO.getDisciplina().getCodigo());
					if (Uteis.isAtributoPreenchido(planoEnsinoVO.getCurso())) {
						ps.setInt(x++, planoEnsinoVO.getCurso().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					if (Uteis.isAtributoPreenchido(planoEnsinoVO.getUnidadeEnsino())) {
						ps.setInt(x++, planoEnsinoVO.getUnidadeEnsino().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					ps.setString(x++, planoEnsinoVO.getAno());
					ps.setString(x++, planoEnsinoVO.getSemestre());
					ps.setString(x++, planoEnsinoVO.getDescricao());
					ps.setDate(x++, Uteis.getDataJDBC(planoEnsinoVO.getDataCadastro()));
					ps.setInt(x++, planoEnsinoVO.getResponsavel().getCodigo());
					ps.setString(x++, planoEnsinoVO.getEmenta());
					ps.setString(x++, planoEnsinoVO.getObjetivoGeral());
					ps.setString(x++, planoEnsinoVO.getObjetivoEspecifico());
					ps.setString(x++, planoEnsinoVO.getPerfilEgresso());
					ps.setString(x++, planoEnsinoVO.getEstrategiaAvaliacao());
					ps.setString(x++, planoEnsinoVO.getProcedimentoDidatico());
					ps.setString(x++, planoEnsinoVO.getSituacao());
					if (Uteis.isAtributoPreenchido(planoEnsinoVO.getProfessorResponsavel())) {
						ps.setInt(x++, planoEnsinoVO.getProfessorResponsavel().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}

					if (Uteis.isAtributoPreenchido(planoEnsinoVO.getTurno())) {
						ps.setInt(x++, planoEnsinoVO.getTurno().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					if (Uteis.isAtributoPreenchido(planoEnsinoVO.getCalendarioLancamentoPlanoEnsinoVO())) {
						ps.setInt(x++, planoEnsinoVO.getCalendarioLancamentoPlanoEnsinoVO().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					ps.setString(x++, planoEnsinoVO.getMotivo());
					ps.setString(x++, planoEnsinoVO.getPeriodicidade());
					if (Uteis.isAtributoPreenchido(planoEnsinoVO.getResponsavelAutorizacao())) {
						ps.setInt(x++, planoEnsinoVO.getResponsavelAutorizacao().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					ps.setInt(x++, planoEnsinoVO.getCodigo());
					return ps;
				}
			});
			getFacadeFactory().getReferenciaBibliograficaFacade().alterarReferenciaBibliograficas(planoEnsinoVO, usuarioLogado);
			getFacadeFactory().getConteudoPlanejamentoFacade().alterarConteudoPlanejamentoVOs(planoEnsinoVO, usuarioLogado);
			getFacadeFactory().getPlanoEnsinoHorarioAulaFacade().alterarPlanoEnsinoHorarioAulaVOs(planoEnsinoVO, usuarioLogado);
			if(Uteis.isAtributoPreenchido(planoEnsinoVO.getQuestionarioRespostaOrigemVO())) {
				getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().alterar(planoEnsinoVO.getQuestionarioRespostaOrigemVO(), usuarioLogado);
			}
			planoEnsinoVO.setNovoObj(false);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final PlanoEnsinoVO planoEnsinoVO, final UsuarioVO usuarioLogado) throws Exception {
		try {
			if (!usuarioLogado.getIsApresentarVisaoCoordenador() && !usuarioLogado.getIsApresentarVisaoProfessor()) {
				excluir(idEntidade, usuarioLogado);
			}else{
				ControleAcesso.verificarPermissaoFuncionalidadeUsuario("PermiteExcluirPlanoEnsino", usuarioLogado);
			}
			getFacadeFactory().getReferenciaBibliograficaFacade().excluirReferenciaBibliograficaVOs(planoEnsinoVO, usuarioLogado);
			getFacadeFactory().getConteudoPlanejamentoFacade().excluirConteudoPlanejamentoVOs(planoEnsinoVO, usuarioLogado);
			PlanoEnsinoVO planoEnsinoVO2 = new PlanoEnsinoVO();
			planoEnsinoVO2.setCodigo(planoEnsinoVO.getCodigo());
			getFacadeFactory().getPlanoEnsinoHorarioAulaFacade().excluirPlanoEnsinoHorarioAulaVOs(planoEnsinoVO2, usuarioLogado);
			String sql = "DELETE FROM PlanoEnsino WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
			getConexao().getJdbcTemplate().update(sql, new Object[] { planoEnsinoVO.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	public StringBuilder getSelectDadosBasicos(boolean trazerSituacaoHistorico) {
		StringBuilder sql = new StringBuilder("SELECT planoensino.*, ");
		sql.append(" responsavelAutorizacao.codigo as \"responsavelAutorizacao.codigo\", ");
		sql.append(" pessoaAutorizacao.nome as \"responsavelAutorizacao.nome\", ");
		sql.append(" pessoaAutorizacao.codigo as \"pessoaUsuario.codigo\", ");
		sql.append(" pessoaAutorizacao.nome as \"responsavelAutorizacao.nome\", ");
		sql.append(" pessoaAutorizacao.cpf as \"pessoaAutorizacao.cpf\", ");
		sql.append(" pessoaAutorizacao.email as \"pessoaAutorizacao.email\", ");
		sql.append(" pessoaAutorizacao.nome as \"pessoaAutorizacao.nome\", ");
		sql.append(" disciplina.nome as \"disciplina.nome\", ");		
		sql.append(" curso.nome as \"curso.nome\", ");
		sql.append(" curso.codigo as \"curso.codigo\", ");
		sql.append(" curso.periodicidade as \"curso.periodicidade\", ");
		sql.append(" curso.questionario as \"curso.questionario\", ");
		sql.append(" unidadeEnsino.nome as \"unidadeEnsino.nome\", ");
		sql.append(" responsavel.nome as \"responsavel.nome\" ");
		sql.append(trazerSituacaoHistorico ? ", situacao_historico, apresentarAprovadoHistorico " : "");
		sql.append(" from PlanoEnsino ");
		sql.append(" inner join Disciplina on Disciplina.codigo= PlanoEnsino.Disciplina ");		
		sql.append(" left join Curso on Curso.codigo= PlanoEnsino.Curso ");
		sql.append(" left join UnidadeEnsino on UnidadeEnsino.codigo= PlanoEnsino.UnidadeEnsino ");
		sql.append(" inner join Usuario as responsavel on responsavel.codigo= PlanoEnsino.responsavel ");
		sql.append(" left join turno on PlanoEnsino.turno = turno.codigo ");
		sql.append(" left join Usuario as responsavelAutorizacao on responsavelAutorizacao.codigo= PlanoEnsino.responsavelAutorizacao ");
		sql.append(" left join pessoa as pessoaAutorizacao on pessoaAutorizacao.codigo = responsavelAutorizacao.pessoa");
		return sql;
	}

	@Override
	public List<PlanoEnsinoVO> consultar(Integer unidadeEnsino, Integer curso, Integer disciplina, String ano, String semestre, String descricao, String situacao, Integer limit, Integer offset, boolean validarAcesso, Integer periodoLetivo, Integer turma, UsuarioVO usuarioLogado) throws Exception {
		List<Object> param = new ArrayList<>();
		StringBuilder sql = getSelectDadosBasicos();
		sql.append(" WHERE 1 = 1 ");
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			sql.append(" and unidadeEnsino.codigo = ").append(unidadeEnsino);
		}
		if (curso != null && curso > 0) {
			sql.append(" and curso.codigo = ").append(curso);
		}
		if (disciplina != null && disciplina > 0) {
			sql.append(" and disciplina.codigo = ").append(disciplina);
		}
		if (ano != null && !ano.trim().isEmpty()) {
			param.add(ano);
			sql.append(" and planoensino.ano = ?");
		}
		if (semestre != null && !semestre.trim().isEmpty()) {
			param.add(semestre);
			sql.append(" and planoensino.semestre = ? ");
		}
		if (descricao != null && !descricao.trim().isEmpty()) {
			param.add(descricao.trim() + PERCENT);
			sql.append(" and upper(sem_acentos(planoEnsino.descricao)) like upper(sem_acentos(?)) ");
		}
		if (!situacao.equals("") && !situacao.equals("TO")) {
			sql.append(" and planoensino.situacao = '").append(situacao).append("' ");
		}
		if (periodoLetivo != null && periodoLetivo > 0) {
			sql.append(" and exists ( select 1 from gradedisciplina ");
			sql.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
			sql.append(" inner join gradecurricular on gradecurricular.codigo = periodoletivo.gradecurricular");
			sql.append(" inner join curso on curso.codigo = gradecurricular.curso");
			sql.append(" where gradedisciplina.disciplina = disciplina.codigo");
			sql.append(" and curso.codigo = Curso.codigo");
			sql.append(" and periodoletivo.periodoletivo = ").append(periodoLetivo);
			sql.append(" )");
		}
		
		if (turma != null && turma > 0) {
			sql.append(" and exists (");
			sql.append(" select turma.codigo from turma ");
			sql.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
			sql.append(" inner join turma as t on turmaagrupada.turma = t.codigo");
			sql.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sql.append(" left join periodoletivo on turma.periodoletivo = periodoletivo.codigo");
			sql.append(" where turma.turmaagrupada");
			sql.append(" and curso.codigo = t.curso");
			sql.append(" and disciplina.codigo = turmadisciplina.disciplina");
			sql.append(" and turma.codigo = ").append(turma);
			sql.append(" and (turma.turno =  planoensino.turno or planoensino.turno is null) ");
			if (periodoLetivo != null && periodoLetivo > 0) {
				sql.append(" and periodoletivo.periodoletivo = ").append(periodoLetivo);
			}
			sql.append(" union all");
			sql.append(" select turma.codigo from turma ");
			sql.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sql.append(" left join periodoletivo on turma.periodoletivo = periodoletivo.codigo");
			sql.append(" where turma.curso = curso.codigo ");
			sql.append(" and turmadisciplina.disciplina = disciplina.codigo ");
			sql.append(" and turma.codigo = ").append(turma);
			sql.append(" and (turma.turno =  planoensino.turno or planoensino.turno is null) ");
			if (periodoLetivo != null && periodoLetivo > 0) {
				sql.append(" and periodoletivo.periodoletivo = ").append(periodoLetivo);
			}
			sql.append(" ) ");
		}
		
		sql.append(" order by planoensino.ano desc, planoensino.semestre desc, planoensino.descricao ");
		if (limit != null && limit > 0) {
			sql.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), param.toArray());
		return montarDadosBasico(rs, usuarioLogado);
	}

	public List<PlanoEnsinoVO> montarDadosBasico(SqlRowSet rs, UsuarioVO usuarioLogado) throws Exception {
		List<PlanoEnsinoVO> planoEnsinoVOs = new ArrayList<PlanoEnsinoVO>(0);
		PlanoEnsinoVO obj = null;
		while (rs.next()) {
			obj = montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
			obj.getDisciplina().setNome(rs.getString("disciplina.nome"));
			obj.getUnidadeEnsino().setNome(rs.getString("unidadeensino.nome"));
			obj.getCurso().setNome(rs.getString("curso.nome"));
			obj.getCurso().setCodigo(rs.getInt("curso.codigo"));
			obj.getCurso().setPeriodicidade(rs.getString("curso.periodicidade"));
			obj.getCurso().getQuestionarioVO().setCodigo(rs.getInt("curso.questionario"));
			obj.getResponsavel().setNome(rs.getString("responsavel.nome"));
			obj.setPeriodicidade(rs.getString("periodicidade"));
			obj.getResponsavelAutorizacao().setCodigo(rs.getInt("responsavelAutorizacao.codigo"));
			obj.getResponsavelAutorizacao().setNome(rs.getString("responsavelAutorizacao.nome"));
			obj.getResponsavelAutorizacao().getPessoa().setCodigo(rs.getInt("pessoaUsuario.codigo"));
			obj.getResponsavelAutorizacao().getPessoa().setCPF(rs.getString("pessoaAutorizacao.cpf"));
			obj.getResponsavelAutorizacao().getPessoa().setEmail(rs.getString("pessoaAutorizacao.email"));
			obj.getResponsavelAutorizacao().getPessoa().setNome(rs.getString("pessoaAutorizacao.nome"));

			planoEnsinoVOs.add(obj);
		}
		return planoEnsinoVOs;
	}

	public PlanoEnsinoVO montarDados(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		PlanoEnsinoVO obj = new PlanoEnsinoVO();
		obj.setNovoObj(false);
		obj.setCodigo(rs.getInt("codigo"));
		obj.getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino"));
		obj.getCurso().setCodigo(rs.getInt("curso"));
		obj.getDisciplina().setCodigo(rs.getInt("disciplina"));
		obj.getResponsavel().setCodigo(rs.getInt("responsavel"));
		obj.setDescricao(rs.getString("descricao"));
		obj.setAno(rs.getString("ano"));
		obj.setSemestre(rs.getString("semestre"));
		obj.setEmenta(rs.getString("ementa"));
		obj.setObjetivoEspecifico(rs.getString("objetivoEspecifico"));
		obj.setObjetivoGeral(rs.getString("objetivoGeral"));
		obj.setPerfilEgresso(rs.getString("perfilegresso"));
		obj.setEstrategiaAvaliacao(rs.getString("estrategiaAvaliacao"));
		obj.setProcedimentoDidatico(rs.getString("procedimentoDidatico"));
		obj.setDataCadastro(rs.getDate("dataCadastro"));
		obj.setSituacao(rs.getString("situacao"));
		obj.setMotivo(rs.getString("motivo"));
		obj.setPeriodicidade(rs.getString("periodicidade"));
		obj.getProfessorResponsavel().setCodigo(rs.getInt("professorResponsavel"));
		obj.getResponsavelAutorizacao().setNome(rs.getString("responsavelAutorizacao.nome"));
		obj.getResponsavelAutorizacao().setCodigo(rs.getInt("responsavelAutorizacao.codigo"));

		if (Uteis.isAtributoPreenchido(rs.getInt("turno"))) {
			obj.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(rs.getInt("turno"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
		}
		if (Uteis.isAtributoPreenchido(rs.getInt("calendarioLancamentoPlanoEnsino"))) {
			obj.setCalendarioLancamentoPlanoEnsinoVO(getFacadeFactory().getCalendarioLancamentoPlanoEnsinoInterfaceFacade().consultarPorChavePrimaria(rs.getLong("calendarioLancamentoPlanoEnsino")));
		}

		montarDadosResponsavelAutorizacao(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado);
		montarDadosProfessorResponsavel(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setReferenciaBibliograficaVOs(getFacadeFactory().getReferenciaBibliograficaFacade().consultarReferenciaBibliograficaPorPlanoEnsino(obj.getCodigo(), false, usuarioLogado));
		obj.setConteudoPlanejamentoVOs(getFacadeFactory().getConteudoPlanejamentoFacade().consultarConteudoPlanejamentoPorPlanoEnsino(obj.getCodigo(), false, usuarioLogado));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado);
		montarDadosDisciplina(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado);
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado);
		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado);
		montarDadosResponsavelAutorizacao(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado);
		return obj;
	}

	public void montarDadosResponsavelAutorizacao(PlanoEnsinoVO obj, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		if (obj.getResponsavelAutorizacao().getCodigo() == 0) {
			return;
		}
		obj.setResponsavelAutorizacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelAutorizacao().getCodigo(), nivelMontarDados, usuarioLogado));
	}
	
	public void montarDadosUnidadeEnsino(PlanoEnsinoVO obj, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo() == 0) {
			return;
		}
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuarioLogado));
	}

	public void montarDadosCurso(PlanoEnsinoVO obj, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		if (obj.getCurso().getCodigo() == 0) {
			return;
		}
		obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), nivelMontarDados, false, usuarioLogado));
	}

	public void montarDadosDisciplina(PlanoEnsinoVO obj, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		if (obj.getDisciplina().getCodigo() == 0) {
			return;
		}
		obj.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplina().getCodigo(), nivelMontarDados, usuarioLogado));
	}

	public void montarDadosResponsavel(PlanoEnsinoVO obj, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		if (obj.getResponsavel().getCodigo() == 0) {
			return;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuarioLogado));
	}

	@Override
	public Integer consultarTotalRegistro(Integer unidadeEnsino, Integer curso, Integer disciplina, String ano, String semestre, String descricao, String situacao, Integer periodoLetivo, Integer turma) throws Exception {
		List<Object> param = new ArrayList<>();
		StringBuilder sql = new StringBuilder("SELECT count(PlanoEnsino.codigo) as qtde ");
		sql.append(" from PlanoEnsino ");
		sql.append(" inner join Disciplina on Disciplina.codigo= PlanoEnsino.Disciplina ");
		sql.append(" left join Curso on Curso.codigo= PlanoEnsino.Curso ");
		sql.append(" left join UnidadeEnsino on UnidadeEnsino.codigo= PlanoEnsino.UnidadeEnsino ");
		sql.append(" inner join Usuario as responsavel on responsavel.codigo= PlanoEnsino.responsavel ");
		sql.append(" WHERE 1 = 1 ");
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			sql.append(" and unidadeEnsino.codigo = ").append(unidadeEnsino);
		}
		if (curso != null && curso > 0) {
			sql.append(" and curso.codigo = ").append(curso);
		}
		if (disciplina != null && disciplina > 0) {
			sql.append(" and disciplina.codigo = ").append(disciplina);
		}
		if (ano != null && !ano.trim().isEmpty()) {
			param.add(ano);
			sql.append(" and planoensino.ano = ? ");
		}
		if (semestre != null && !semestre.trim().isEmpty()) {
			param.add(semestre);
			sql.append(" and planoensino.semestre = ? ");
		}
		if (!situacao.equals("") && !situacao.equals("TO")) {
			sql.append(" and planoensino.situacao = '").append(situacao).append("' ");
		}
		if (periodoLetivo != null && periodoLetivo > 0) {
			sql.append(" and exists ( select 1 from gradedisciplina ");
			sql.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
			sql.append(" inner join gradecurricular on gradecurricular.codigo = periodoletivo.gradecurricular");
			sql.append(" inner join curso on curso.codigo = gradecurricular.curso");
			sql.append(" where gradedisciplina.disciplina = disciplina.codigo");
			sql.append(" and curso.codigo = Curso.codigo");
			sql.append(" and periodoletivo.periodoletivo = ").append(periodoLetivo);
			sql.append(" )");
		}
		
		if (turma != null && turma > 0) {
			sql.append(" and exists (");
			sql.append(" select turma.codigo from turma ");
			sql.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
			sql.append(" inner join turma as t on turmaagrupada.turma = t.codigo");
			sql.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sql.append(" where turma.turmaagrupada");
			sql.append(" and curso.codigo = t.curso");
			sql.append(" and disciplina.codigo = turmadisciplina.disciplina");
			sql.append(" and turma.codigo = ").append(turma);
			sql.append(" union all");
			sql.append(" select turma.codigo from turma ");
			sql.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sql.append(" where turma.curso = curso.codigo ");
			sql.append(" and turmadisciplina.disciplina = disciplina.codigo ");
			sql.append(" and turma.codigo = ").append(turma);;
			sql.append(" ) ");
		}
		if (descricao != null && !descricao.trim().isEmpty()) {
			param.add(descricao.trim() + PERCENT);
			sql.append(" and upper(sem_acentos(planoensino.descricao)) like upper(sem_acentos(?)) ");
		}

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), param.toArray());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	@Override
	public PlanoEnsinoVO consultarPorChavePrimaria(Integer codigo, int nivelMontardados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = getSelectDadosBasicos();
		sql.append(" WHERE planoEnsino.codigo = ").append(codigo);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<PlanoEnsinoVO> planoEnsinoVOs = montarDadosBasico(rs, usuarioLogado);
		if (!planoEnsinoVOs.isEmpty()) {
			PlanoEnsinoVO obj = planoEnsinoVOs.get(0);
			if (nivelMontardados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
				obj.setReferenciaBibliograficaVOs(getFacadeFactory().getReferenciaBibliograficaFacade().consultarReferenciaBibliograficaPorPlanoEnsino(obj.getCodigo(), false, usuarioLogado));
				obj.setConteudoPlanejamentoVOs(getFacadeFactory().getConteudoPlanejamentoFacade().consultarConteudoPlanejamentoPorPlanoEnsino(obj.getCodigo(), false, usuarioLogado));
				obj.setPlanoEnsinoHorarioAulaVOs(getFacadeFactory().getPlanoEnsinoHorarioAulaFacade().consultarPorPlanoEnsino(obj.getCodigo(), usuarioLogado));
			}
			return obj;
		}
		throw new Exception("Dados não encontrados (Plano de Ensino). ");
	}

	@Override
	public PlanoEnsinoVO consultarPorUnidadeEnsinoCursoDisciplinaAnoSemestre(Integer unidadeEnsino, Integer curso, Integer disciplina, String ano, String semestre, Integer turno, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = getSelectDadosBasicos();
		sql.append(" WHERE 1 = 1 ");
		sql.append(" and unidadeEnsino.codigo = ").append(unidadeEnsino);
		sql.append(" and curso.codigo = ").append(curso);
		sql.append(" and disciplina.codigo = ").append(disciplina);

		if (ano != null && !ano.equals("")) {
			sql.append(" and planoensino.ano = '").append(ano).append("' ");
		}

		if (semestre != null && !semestre.equals("")) {
			sql.append(" and planoensino.semestre = '").append(semestre).append("' ");
		}

		if (Uteis.isAtributoPreenchido(turno)) {
			sql.append(" and planoensino.turno = ").append(turno);			
		}

		sql.append(" order by planoensino.ano desc, planoensino.semestre desc  ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<PlanoEnsinoVO> planoEnsinoVOs = montarDadosBasico(rs, usuarioVO);
		if (!planoEnsinoVOs.isEmpty()) {
			PlanoEnsinoVO obj = planoEnsinoVOs.get(0);
			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
				obj.setReferenciaBibliograficaVOs(getFacadeFactory().getReferenciaBibliograficaFacade().consultarReferenciaBibliograficaPorPlanoEnsino(obj.getCodigo(), false, usuarioVO));
				obj.setConteudoPlanejamentoVOs(getFacadeFactory().getConteudoPlanejamentoFacade().consultarConteudoPlanejamentoPorPlanoEnsino(obj.getCodigo(), false, usuarioVO));
			}
			return obj;
		}
		return new PlanoEnsinoVO();
	}

	@Override
	public PlanoEnsinoVO consultarPorDisciplinaMatriculaAluno(Integer disciplina, String matricula, boolean buscarPlanoVinculadoProfessorResponsavel, String nomeProfessor, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = getSelectDadosBasicos(true);
		sql.append(" inner join lateral (");		
		sql.append(" select ");
		sql.append(" planoensino.codigo as planoensino, historico.situacao as situacao_historico, historico.apresentarAprovadoHistorico ");
		sql.append(" from matricula");
		sql.append(" inner join curso on curso.codigo = matricula.curso");
		sql.append(" inner join historico on matricula.matricula = historico.matricula");
		sql.append(" and historico.matrizcurricular = matricula.gradecurricularatual");
		sql.append(" and historico.codigo = (");
		sql.append("      select his.codigo from historico his where  matricula.matricula = his.matricula");
		sql.append(" 	 and his.matrizcurricular = matricula.gradecurricularatual");
		sql.append(" 	and his.disciplina = historico.disciplina");
		sql.append(" 	order by his.anohistorico desc, his.semestrehistorico desc, his.codigo desc limit 1");
		sql.append(" )");
		sql.append(" left join periodoauladisciplinaaluno(historico.codigo) as horario on horario.professor_codigo is not null");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo");
		sql.append(" inner join turma as turmabase on matriculaperiodo.turma = turmabase.codigo");
		sql.append(" left join matriculaperiodoturmadisciplina ");
		sql.append(" on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina");
		sql.append(" left join turma on turma.codigo = matriculaperiodoturmadisciplina.turma");
		sql.append(" inner join consultarPlanoEnsino(matricula.unidadeensino, matricula.curso, historico.anohistorico, historico.semestrehistorico, historico.disciplina, case when turma.codigo is not null then turma.turno else turmabase.turno end, horario.professor_codigo, 'AU'  ) as planoensino on ");
		sql.append(" planoensino.codigo is not null");
		sql.append(" where matricula.matricula = ? and historico.disciplina = ? ");
		sql.append(" LIMIT 1) as plano_ensino on planoensino.codigo = plano_ensino.planoensino ");
		sql.append(" order by planoensino.ano||'/'||planoensino.semestre desc limit 1 ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula, disciplina);
		if (rs.next()) {
			PlanoEnsinoVO obj = montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			obj.getDisciplina().setNome(rs.getString("disciplina.nome"));
			obj.getUnidadeEnsino().setNome(rs.getString("unidadeensino.nome"));
			obj.getCurso().setNome(rs.getString("curso.nome"));
			obj.getCurso().setCodigo(rs.getInt("curso.codigo"));
			obj.getCurso().setPeriodicidade(rs.getString("curso.periodicidade"));
			obj.getCurso().getQuestionarioVO().setCodigo(rs.getInt("curso.questionario"));
			obj.getResponsavel().setNome(rs.getString("responsavel.nome"));
			String situacaoHistoricoStr = rs.getString("situacao_historico");
			if (Uteis.isAtributoPreenchido(situacaoHistoricoStr) && SituacaoHistorico.getEnum(situacaoHistoricoStr) != null) {
				situacaoHistoricoStr = situacaoHistoricoStr.equals("AA") && rs.getBoolean("apresentarAprovadoHistorico") ? "AP" : situacaoHistoricoStr;
				SituacaoHistorico situacaoHistorico = SituacaoHistorico.getEnum(situacaoHistoricoStr);
				obj.setSituacaoHistorico(situacaoHistorico.getDescricao());
				obj.setHistoricoAprovado(situacaoHistorico.getHistoricoAprovado());
			}
			obj.setReferenciaBibliograficaVOs(getFacadeFactory().getReferenciaBibliograficaFacade().consultarReferenciaBibliograficaPorPlanoEnsino(obj.getCodigo(), false, usuarioVO));
			obj.setConteudoPlanejamentoVOs(getFacadeFactory().getConteudoPlanejamentoFacade().consultarConteudoPlanejamentoPorPlanoEnsino(obj.getCodigo(), false, usuarioVO));
			obj.setPlanoEnsinoHorarioAulaVOs(getFacadeFactory().getPlanoEnsinoHorarioAulaFacade().consultarPorPlanoEnsino(obj.getCodigo(), usuarioVO));			
			return obj;
		}
		return new PlanoEnsinoVO();
	}

	@Override
	public void adicionarReferenciaBibliografiaVOs(PlanoEnsinoVO planoEnsinoVO, ReferenciaBibliograficaVO referenciaBibliograficaVO) throws Exception {
		ReferenciaBibliograficaVO.validarDados(referenciaBibliograficaVO);
		int x = 0;
		for (ReferenciaBibliograficaVO referenciaBibliograficaVO2 : planoEnsinoVO.getReferenciaBibliograficaVOs()) {
			if ((referenciaBibliograficaVO.getPublicacaoExistenteBiblioteca() 
					&& referenciaBibliograficaVO2.getCatalogo().getCodigo().equals(referenciaBibliograficaVO.getCatalogo().getCodigo()))
				|| (!referenciaBibliograficaVO.getPublicacaoExistenteBiblioteca() 
					&& referenciaBibliograficaVO2.getTitulo().trim().equals(referenciaBibliograficaVO.getTitulo().trim())
					&& referenciaBibliograficaVO2.getAnoPublicacao().equals(referenciaBibliograficaVO.getAnoPublicacao())
					&& referenciaBibliograficaVO2.getTipoReferencia().equals(referenciaBibliograficaVO.getTipoReferencia()))) {
				planoEnsinoVO.getReferenciaBibliograficaVOs().set(x, referenciaBibliograficaVO);
				return;
			}
			x++;
		}
		planoEnsinoVO.getReferenciaBibliograficaVOs().add(referenciaBibliograficaVO);
	}

	@Override
	public void removerReferenciaBibliografiaVOs(PlanoEnsinoVO planoEnsinoVO, ReferenciaBibliograficaVO referenciaBibliograficaVO) throws Exception {
		if (Uteis.isAtributoPreenchido(referenciaBibliograficaVO)) {
			planoEnsinoVO.getReferenciaBibliograficaVOs().removeIf(p -> p.getCodigo().equals(referenciaBibliograficaVO.getCodigo()));
		} else {
			planoEnsinoVO.getReferenciaBibliograficaVOs().removeIf(p -> 
					p.getTitulo().equals(referenciaBibliograficaVO.getTitulo())
					&& p.getAnoPublicacao().equals(referenciaBibliograficaVO.getAnoPublicacao())
					&& p.getTipoReferencia().equals(referenciaBibliograficaVO.getTipoReferencia())
			);
		}
	}

	@Override
	public void adicionarConteudoPlanejamentoVOs(PlanoEnsinoVO planoEnsinoVO, ConteudoPlanejamentoVO conteudoPlanejamentoVO) throws Exception {
		ConteudoPlanejamentoVO.validarDados(conteudoPlanejamentoVO);
		int x = 0;
		for (ConteudoPlanejamentoVO  conteudoPlanejamentoVO2 : planoEnsinoVO.getConteudoPlanejamentoVOs()) {
			if (conteudoPlanejamentoVO2.getOrdem().equals(conteudoPlanejamentoVO.getOrdem())) {
				planoEnsinoVO.getConteudoPlanejamentoVOs().set(x, conteudoPlanejamentoVO);
				return;
			}
			x++;
		}
		conteudoPlanejamentoVO.setOrdem(planoEnsinoVO.getConteudoPlanejamentoVOs().size() + 1);
		planoEnsinoVO.getConteudoPlanejamentoVOs().add(conteudoPlanejamentoVO);
	}
	
	@Override
	public void removerConteudoPlanejamentoVOs(PlanoEnsinoVO planoEnsinoVO, ConteudoPlanejamentoVO conteudoPlanejamentoVO) throws Exception {
		int x = 0;
		for (ConteudoPlanejamentoVO  conteudoPlanejamentoVO2 : planoEnsinoVO.getConteudoPlanejamentoVOs()) {
			if (conteudoPlanejamentoVO2.getOrdem().equals(conteudoPlanejamentoVO.getOrdem())) {
				planoEnsinoVO.getConteudoPlanejamentoVOs().remove(x);
				break;
			}
			x++;
		}
		x = 1;
		for (ConteudoPlanejamentoVO  conteudoPlanejamentoVO2 : planoEnsinoVO.getConteudoPlanejamentoVOs()) {
			conteudoPlanejamentoVO2.setOrdem(x);
			x++;
		}
	}
	
	@Override
	public void alterarOrdenacaoConteudoPlanejamentoVO(PlanoEnsinoVO planoEnsinoVO, ConteudoPlanejamentoVO obj1, ConteudoPlanejamentoVO obj2) throws Exception {
		int ordem1 = obj1.getOrdem();
		obj1.setOrdem(obj2.getOrdem());
		obj2.setOrdem(ordem1);
		Ordenacao.ordenarLista(planoEnsinoVO.getConteudoPlanejamentoVOs(), "ordem");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacao(int codigo, String situacao, String motivo, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDadosEmRevisao(situacao, motivo);
			final String sql = "UPDATE planoensino set situacao=?, motivo = ?, responsavelAutorizacao =? WHERE codigo = ? "+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, situacao);
					sqlAlterar.setString(2, motivo);
					if(situacao.equals("AU")) {
						sqlAlterar.setInt(3, usuarioVO.getCodigo());
					}else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setInt(4, codigo);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	private void validarDadosEmRevisao(String situacao, String motivo) throws Exception {
		if (situacao.equals(SituacaoPlanoEnsinoEnum.EM_REVISAO.getValor()) && !Uteis.isAtributoPreenchido(motivo)) {
			throw new Exception(UteisJSF.internacionalizar("msg_Requerimento_motivoRetorno"));
		}
	}

	@Override
	public List<Integer> consultarDisciplinaMatriculaAlunoQuePossuiPlanoEnsino(String matricula, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct disciplina.codigo as disciplina ");
		sql.append(" from PlanoEnsino ");
		sql.append(" inner join Disciplina on Disciplina.codigo= PlanoEnsino.Disciplina ");		
		sql.append(" inner join Curso on Curso.codigo= PlanoEnsino.Curso ");
		sql.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo= PlanoEnsino.UnidadeEnsino ");
		sql.append(" inner join Usuario as responsavel on responsavel.codigo= PlanoEnsino.responsavel ");
		sql.append(" inner join matricula on matricula.matricula = '").append(matricula).append("' ");
		sql.append(" and matricula.unidadeEnsino = planoEnsino.unidadeEnsino and matricula.curso = curso.codigo ");
		sql.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
		sql.append(" and matriculaPeriodo.codigo = (select mp.codigo from matriculaPeriodo mp where  mp.matricula = matricula.matricula order by (mp.ano || mp.semestre) desc, mp.codigo desc limit 1) ");
		sql.append(" WHERE 1 = 1 ");		
		sql.append(" and disciplina.codigo in (");
		sql.append(" select gradedisciplina.disciplina from periodoletivo ");
		sql.append(" inner join gradedisciplina on periodoletivo.codigo = gradedisciplina.periodoletivo ");
		sql.append(" where periodoletivo.gradecurricular = matricula.gradecurricularatual ");
		sql.append(" union ");
		sql.append(" select gradedisciplinacomposta.disciplina from periodoletivo ");
		sql.append(" inner join gradedisciplina on periodoletivo.codigo = gradedisciplina.periodoletivo ");
		sql.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradedisciplina = gradedisciplina.codigo ");
		sql.append(" where periodoletivo.gradecurricular = matricula.gradecurricularatual ");
		sql.append(" union ");
		sql.append(" select gradedisciplinacomposta.disciplina from gradecurriculargrupooptativa ");
		sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
		sql.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ");
		sql.append(" where gradecurriculargrupooptativa.gradecurricular = matricula.gradecurricularatual ");
		sql.append(" union ");
		sql.append(" select gradecurriculargrupooptativadisciplina.disciplina from gradecurriculargrupooptativa ");
		sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");		
		sql.append(" where gradecurriculargrupooptativa.gradecurricular = matricula.gradecurricularatual ");
		sql.append(" ) "); 
		sql.append(" and (case when curso.periodicidade = 'SE' then ");
		sql.append(" (planoensino.ano||'/'||planoensino.semestre)::VARCHAR <= (matriculaPeriodo.ano||'/'||matriculaPeriodo.semestre)::VARCHAR ");
		sql.append(" else case when curso.periodicidade = 'AN' then planoensino.ano <= matriculaPeriodo.ano ");
		sql.append(" else planoensino.ano||'/'||planoensino.semestre <= (extract(year from matriculaPeriodo.data)::VARCHAR||'/'||(case when extract(month from matriculaPeriodo.data) <7 then '1' else '2' end)) end end ");
		sql.append(" or curso.periodicidade = 'IN' ) and planoensino.situacao = 'AU' ");		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<Integer> disciplinas = new ArrayList<Integer>(0);
		while(rs.next()){
			disciplinas.add(rs.getInt("disciplina"));
		}
		return disciplinas;
	}
	
	public List<Integer> consultarDisciplinaMatriculaAlunoQuePossuiPlanoEnsino(String matricula) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select historico.disciplina as disciplina from matricula");
		sql.append(" inner join curso on curso.codigo = matricula.curso");
		sql.append(" inner join historico on matricula.matricula = historico.matricula");
		sql.append(" and historico.matrizcurricular = matricula.gradecurricularatual");
		sql.append(" and historico.codigo = (");
		sql.append(" select his.codigo from historico his where  matricula.matricula = his.matricula");
		sql.append(" and his.matrizcurricular = matricula.gradecurricularatual");
		sql.append(" and his.disciplina = historico.disciplina");
		sql.append(" order by his.anohistorico desc, his.semestrehistorico desc, his.codigo desc limit 1)");
		sql.append(" left join periodoauladisciplinaaluno(historico.codigo) as horario on horario.professor_codigo is not null");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo");
		sql.append(" inner join turma as turmabase on matriculaperiodo.turma = turmabase.codigo");
		sql.append(" left join matriculaperiodoturmadisciplina");
		sql.append(" on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina");
		sql.append(" left join turma on turma.codigo = matriculaperiodoturmadisciplina.turma");
		sql.append(" inner join consultarPlanoEnsino(matricula.unidadeensino, matricula.curso, historico.anohistorico, historico.semestrehistorico, historico.disciplina, case when turma.codigo is not null then turma.turno else turmabase.turno end, horario.professor_codigo, 'AU'  ) as planoensino on ");
		sql.append(" planoensino.codigo is not null");
		sql.append(" where matricula.matricula = ?");
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula);
		List<Integer> disciplinas = new ArrayList<>();
		while(rs.next()){
			disciplinas.add(rs.getInt("disciplina"));
		}
		return disciplinas;
	}
	
	@Override
	public PlanoEnsinoVO consultarPlanoEnsino(
			Integer unidadeEnsino, Integer curso, String ano, String semestre, Integer disciplina,
			Integer turno, Integer professor, String situacao, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select codigo from consultarPlanoEnsino(?, ?, ?, ?, ?, ?, ?, ?) ");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), unidadeEnsino, curso, ano, semestre, 
				disciplina, turno, professor, situacao);
		PlanoEnsinoVO planoEnsinoVO = new PlanoEnsinoVO();
		if (rs.next()) {
			planoEnsinoVO = this.consultarPorChavePrimaria(rs.getInt("codigo"), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuarioLogado);
		}

		return planoEnsinoVO;
	}

	@Override
	public void realizarVerificacaoDisciplinaAlunoQuePossuiPlanoEnsino(MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception{
		List<Integer> disciplinas = consultarDisciplinaMatriculaAlunoQuePossuiPlanoEnsino(matriculaVO.getMatricula());
		if(!disciplinas.isEmpty()){
			
			for(PeriodoLetivoVO periodoLetivoVO: matriculaVO.getGradeCurricularAtual().getPeriodoLetivosVOs()){
				if(disciplinas.isEmpty()){
					break;
				}
				q:
				for(GradeDisciplinaVO gradeDisciplinaVO: periodoLetivoVO.getGradeDisciplinaVOs()){
					if(disciplinas.isEmpty()){
						break;
					}
					for(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO: gradeDisciplinaVO.getGradeDisciplinaCompostaVOs()){
						if(disciplinas.contains(gradeDisciplinaCompostaVO.getDisciplina().getCodigo())){
							gradeDisciplinaCompostaVO.setSelecionado(true);
							disciplinas.remove(gradeDisciplinaCompostaVO.getDisciplina().getCodigo());							
						}
					}
					if(disciplinas.contains(gradeDisciplinaVO.getDisciplina().getCodigo())){
						gradeDisciplinaVO.setSelecionado(true);
						disciplinas.remove(gradeDisciplinaVO.getDisciplina().getCodigo());
						continue q;
					}
				}
			}
			
			if(!disciplinas.isEmpty()){
				
				for(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO:matriculaVO.getGradeCurricularAtual().getGradeCurricularGrupoOptativaVOs()){					
					if(disciplinas.isEmpty()){
						break;
					}
					w:
					for(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO: gradeCurricularGrupoOptativaVO.getGradeCurricularGrupoOptativaDisciplinaVOs()){
						if(disciplinas.isEmpty()){
							break;
						}
						for(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO: gradeCurricularGrupoOptativaDisciplinaVO.getGradeDisciplinaCompostaVOs()){
							if(disciplinas.contains(gradeDisciplinaCompostaVO.getDisciplina().getCodigo())){
								gradeDisciplinaCompostaVO.setSelecionado(true);
								disciplinas.remove(gradeDisciplinaCompostaVO.getDisciplina().getCodigo());								
							}
						}
						if(disciplinas.contains(gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo())){
							gradeCurricularGrupoOptativaDisciplinaVO.setSelecionado(true);
							disciplinas.remove(gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo());
							continue w;
						}
					}
				}
			}
		}
	}

	public PlanoEnsinoVO atualizarTotalCargaHoraria(PlanoEnsinoVO planoEnsinoVO) {
		BigDecimal total = BigDecimal.ZERO;
		for (ConteudoPlanejamentoVO obj : planoEnsinoVO.getConteudoPlanejamentoVOs()) {
			total = total.add(obj.getCargahoraria());
		}
		planoEnsinoVO.setTotalCargaHoraria(total.setScale(2, BigDecimal.ROUND_HALF_DOWN));
		return planoEnsinoVO;
	}

	@Override
	public List<PlanoEnsinoVO> consultarPlanoEnsinoProfessor(String campoConsulta, String valorConsultar, String ano, String semestre, SituacaoPlanoEnsinoEnum situacaoPlanoEnsino, boolean filtrarTodosPlanosEnsino, 
			boolean controlarAcesso, boolean trazerApenasPlanosSemProfessorResponsavelOuVinculadosAoProfessor, Integer professorCodigoPessoa, Integer turma, Integer curso, Integer periodoLetivo,  UsuarioVO usuarioVO) throws Exception{
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		if(campoConsulta.equals("codigo") && (!Uteis.getIsValorNumerico(valorConsultar) || valorConsultar.trim().isEmpty())){
			throw new Exception("Informe no campo de consulta apenas números.");
		}	
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select disciplina.*, ");
		sqlStr.append(" planoensino.codigo as planoensino, planoensino.ano, planoensino.semestre, planoensino.descricao, planoensino.datacadastro, planoensino.situacao, planoensino.curso as cursoPlanoEnsino, planoensino.unidadeensino as unidadeEnsinoPlanoEnsino,  ");
		sqlStr.append(" usuario.codigo as usuario_codigo, usuario.nome as usuario_nome, planoensino.professorresponsavel as professorresponsavel , planoensino.turno as turno, planoensino.periodicidade as planoensino_periodicidade, turno.nome as turno_nome ");
		sqlStr.append(" from ( ");
		sqlStr.append(" select distinct disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome, unidadeensino.codigo as unidadeensino_codigo, unidadeensino.nome as unidadeensino_nome, ");
		sqlStr.append(" curso.codigo as curso_codigo, curso.nome  as curso_nome, curso.periodicidade ");
		sqlStr.append(" from horarioTurma ");
		sqlStr.append(" inner join turma on turma.codigo = horarioTurma.turma ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
		sqlStr.append(" inner join horarioTurmadia on horarioTurmadia.horarioTurma = horarioTurma.codigo ");
		sqlStr.append(" inner join horarioTurmadiaitem on horarioTurmadiaitem.horarioTurmadia = horarioTurmadia.codigo ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = horarioTurmadiaitem.professor ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = horarioTurmadiaitem.disciplina ");
		sqlStr.append(" inner join curso on curso.codigo = turma.curso ");		
		sqlStr.append(" where pessoa.codigo = ");
		sqlStr.append(usuarioVO.getPessoa().getCodigo());
		if(campoConsulta.equals("codigo")){
			sqlStr.append(" and disciplina.codigo = ? ");			
		}else if(campoConsulta.equals("nome")){			
			sqlStr.append(" and sem_acentos(upper(disciplina.nome)) ilike sem_acentos(upper( ? ))");
		}else if(campoConsulta.equals("curso") && curso != null && curso > 0){			
//			sqlStr.append(" and sem_acentos(upper(curso.nome)) ilike sem_acentos(upper( ? ))");
			sqlStr.append(" and curso.codigo = ").append(curso);
		}else if(campoConsulta.equals("turma") && turma != null && turma > 0) {
			sqlStr.append(" and turma.codigo = ").append(turma);;
		}
		
		sqlStr.append(" and turma.turmaagrupada = false  ");
		/*if((Uteis.isAtributoPreenchido(ano)) && Uteis.isAtributoPreenchido(semestre)){
			sqlStr.append("and (( turma.semestral and horarioTurma.anoVigente = '").append(ano).append("' and horarioturma.semestreVigente = '").append(semestre).append("') ");
			sqlStr.append(" or ( turma.anual and horarioTurma.anoVigente = '");
			sqlStr.append(ano).append("') or (turma.anual = false and turma.semestral = false and extract(year from horarioturmadiaitem.data) = '").append(ano).append("'");
			sqlStr.append(" and extract(month from horarioturmadiaitem.data) between ").append(semestre == "1" ? "1 and 7" : "8 and 12").append("))");
		}else if(!Uteis.isAtributoPreenchido(ano) && Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and (( turma.semestral and horarioturma.semestreVigente = '").append(semestre).append("') ");
			sqlStr.append(" or (turma.anual = false and turma.semestral = false and extract(month from horarioturmadiaitem.data) between ").append(semestre == "1" ? "1 and 7" : "8 and 12").append("))");
		}else  if(Uteis.isAtributoPreenchido(ano) && !Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and (( turma.semestral and horarioTurma.anoVigente = '").append(ano).append("' ) ");
			sqlStr.append(" or ( turma.anual and horarioTurma.anoVigente = '").append(ano).append("') ");
			sqlStr.append(" or (turma.anual = false and turma.semestral = false and extract(year from horarioturmadiaitem.data) = '").append(ano).append("'))");
		}*/
		if(periodoLetivo != null && periodoLetivo > 0) {
			sqlStr.append(" and exists  (select codigo from periodoletivo where periodoletivo.codigo = turma.periodoletivo and periodoletivo.periodoletivo = ").append(periodoLetivo).append(") ");
		}
		
		sqlStr.append(" union ");
		
		sqlStr.append(" select distinct disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome, unidadeensino.codigo as unidadeensino_codigo, unidadeensino.nome as unidadeensino_nome, ");
		sqlStr.append(" curso.codigo as curso_codigo, curso.nome  as curso_nome, curso.periodicidade ");
		sqlStr.append(" from horarioTurma ");
		sqlStr.append(" inner join turma on turma.codigo = horarioTurma.turma ");
		sqlStr.append(" inner join turmaagrupada on turma.codigo = turmaagrupada.turmaorigem ");
		sqlStr.append(" inner join turma as t on t.codigo = turmaagrupada.turma ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
		sqlStr.append(" inner join horarioTurmadia on horarioTurmadia.horarioTurma = horarioTurma.codigo ");
		sqlStr.append(" inner join horarioTurmadiaitem on horarioTurmadiaitem.horarioTurmadia = horarioTurmadia.codigo ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = horarioTurmadiaitem.professor ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = horarioTurmadiaitem.disciplina ");
		sqlStr.append(" inner join curso on curso.codigo = t.curso ");		
		sqlStr.append(" where pessoa.codigo = ");
		sqlStr.append(usuarioVO.getPessoa().getCodigo());
		if(campoConsulta.equals("codigo")){
			sqlStr.append(" and disciplina.codigo = ? ");			
		}else if(campoConsulta.equals("nome")){			
			sqlStr.append(" and sem_acentos(upper(disciplina.nome)) ilike sem_acentos(upper( ? ))");
		}else if(campoConsulta.equals("curso") && curso != null && curso > 0){			
//			sqlStr.append(" and sem_acentos(upper(curso.nome)) ilike sem_acentos(upper( ? ))");
			sqlStr.append(" and curso.codigo = ").append(curso);
		} else if(campoConsulta.equals("turma") && turma != null && turma > 0) {
			sqlStr.append(" and turma.codigo = ").append(turma);;
		}
		if(Uteis.isAtributoPreenchido(ano) && Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and (( turma.semestral and horarioTurma.anoVigente = '").append(ano).append("' and horarioturma.semestreVigente = '").append(semestre).append("') ");
			sqlStr.append(" or ( turma.anual and horarioTurma.anoVigente = '");
			sqlStr.append(ano).append("') or (turma.anual = false and turma.semestral = false and extract(year from horarioturmadiaitem.data) = '").append(ano).append("'");
			sqlStr.append(" and extract(month from horarioturmadiaitem.data) between ").append(semestre == "1" ? "1 and 7" : "8 and 12").append("))");
		}else if(!Uteis.isAtributoPreenchido(ano) && Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and (( turma.semestral and horarioturma.semestreVigente = '").append(semestre).append("') ");
			sqlStr.append(" or (turma.anual = false and turma.semestral = false and extract(month from horarioturmadiaitem.data) between ").append(semestre == "1" ? "1 and 7" : "8 and 12").append("))");
		}else  if(Uteis.isAtributoPreenchido(ano) && !Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and (( turma.semestral and horarioTurma.anoVigente = '").append(ano).append("' ) ");
			sqlStr.append(" or ( turma.anual and horarioTurma.anoVigente = '").append(ano).append("') ");
			sqlStr.append(" or (turma.anual = false and turma.semestral = false and extract(year from horarioturmadiaitem.data) = '").append(ano).append("'))");
		}
		if(periodoLetivo != null && periodoLetivo > 0) {
			sqlStr.append(" and exists  (select codigo from periodoletivo where periodoletivo.codigo = t.periodoletivo and periodoletivo.periodoletivo = ").append(periodoLetivo).append(") ");
		}
		
		sqlStr.append(" ) as disciplina ");
		sqlStr.append(" left join planoensino on planoensino.disciplina = disciplina.disciplina_codigo ");
		//sqlStr.append(" and planoensino.curso = disciplina.curso_codigo ");
		//sqlStr.append(" and planoensino.unidadeensino = disciplina.unidadeensino_codigo ");
		
		if(!filtrarTodosPlanosEnsino){
			sqlStr.append(" and planoensino.codigo in (select pe.codigo from planoensino pe where pe.disciplina = disciplina.disciplina_codigo ");
			//sqlStr.append(" and pe.curso = disciplina.curso_codigo ");		
			
			if((Uteis.isAtributoPreenchido(ano)) && Uteis.isAtributoPreenchido(semestre)){
				sqlStr.append(" and (( disciplina.periodicidade = 'SE' and pe.ano = '").append(ano).append("' and pe.semestre = '").append(semestre).append("') ");
				sqlStr.append(" or ( disciplina.periodicidade = 'AN' and pe.ano = ' ))");
				sqlStr.append(ano).append("') or (disciplina.periodicidade = 'IN' and ((pe.ano = '' and pe.semestre = '') ");
				sqlStr.append(" or (pe.ano = '").append(ano).append("' and pe.semestre = '").append(semestre).append("')))) ");
			} else if (Uteis.isAtributoPreenchido(semestre) && !Uteis.isAtributoPreenchido(ano)) {
				sqlStr.append(" and ( (disciplina.periodicidade = 'SE' and pe.semestre = '").append(semestre).append("')) ");
			} else if (Uteis.isAtributoPreenchido(ano) && !Uteis.isAtributoPreenchido(semestre)) {
				sqlStr.append(" and (( disciplina.periodicidade = 'SE' and pe.ano = '").append(ano).append("') ");
				sqlStr.append(" or ( disciplina.periodicidade = 'AN' and pe.ano = '").append(ano).append("')) ");
			}
			if (trazerApenasPlanosSemProfessorResponsavelOuVinculadosAoProfessor && Uteis.isAtributoPreenchido(professorCodigoPessoa)) {
				sqlStr.append(" and (pe.professorresponsavel is null or pe.professorresponsavel = ").append(professorCodigoPessoa).append(" )");
				//sqlStr.append(" and pe.unidadeensino = disciplina.unidadeensino_codigo ");
				sqlStr.append(" order by pe.ano desc, pe.semestre desc, pe.professorresponsavel asc ");
			} else if (!trazerApenasPlanosSemProfessorResponsavelOuVinculadosAoProfessor && Uteis.isAtributoPreenchido(professorCodigoPessoa)) {
				//sqlStr.append(" and pe.unidadeensino = disciplina.unidadeensino_codigo ");
				sqlStr.append(" order by pe.ano desc, pe.semestre desc, ");
				sqlStr.append(" case when pe.professorresponsavel = ").append(professorCodigoPessoa).append(" then 0 ");
				sqlStr.append(" else case when pe.professorresponsavel is null then 1 ");
				sqlStr.append(" else 2 end end asc ");				
			} 
			sqlStr.append(" ) ");
		}
		sqlStr.append(" left join usuario on usuario.codigo = planoensino.responsavel ");
		sqlStr.append(" left join turno on turno.codigo = planoensino.turno ");
		if(situacaoPlanoEnsino != null && !SituacaoPlanoEnsinoEnum.TODOS.getValor().equals(situacaoPlanoEnsino.getValor())){
			if(!SituacaoPlanoEnsinoEnum.NAO_CADASTRADO.equals(situacaoPlanoEnsino)){
				sqlStr.append(" where planoensino.situacao = '").append(situacaoPlanoEnsino.getValor()).append("' ");
			}else{
				sqlStr.append(" where planoensino.codigo is null ");
			}
		}
		if(campoConsulta.equals("codigo")){
			sqlStr.append(" order by disciplina_codigo, disciplina_nome, curso_nome, unidadeensino_nome, ano desc, semestre desc ");
		}else{
			sqlStr.append(" order by disciplina_nome, curso_nome, unidadeensino_nome, ano desc, semestre desc ");
		}	
		//System.out.println(sqlStr);
		SqlRowSet tabelaResultado = null;
		if(campoConsulta.equals("codigo")){
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), Integer.valueOf(valorConsultar) , Integer.valueOf(valorConsultar));
		}else if(campoConsulta.equals("curso") || campoConsulta.equals("turma")) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		}else{
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), "%"+valorConsultar+"%", "%"+valorConsultar+"%");
		}
		List<PlanoEnsinoVO> planoEnsinoVOs =  new ArrayList<PlanoEnsinoVO>(0);
		montarDadosConsultaProfessorCoordenador(tabelaResultado, planoEnsinoVOs, usuarioVO);
		return planoEnsinoVOs;
	}

	@Override
	public List<PlanoEnsinoVO> consultarPlanoEnsinoCoordenador(String campoConsulta, String valorConsultar, Integer unidadeEnsino, String ano, String semestre, SituacaoPlanoEnsinoEnum situacaoPlanoEnsino, boolean filtrarTodosPlanosEnsino, boolean controlarAcesso, Integer turma, Integer curso, Integer periodoLetivo, UsuarioVO usuarioVO) throws Exception{
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		if(campoConsulta.equals("codigo") && (!Uteis.getIsValorNumerico(valorConsultar) || valorConsultar.trim().isEmpty())){
			throw new Exception("Informe no campo de consulta apenas números.");
		}	
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select disciplina.*, ");
		sqlStr.append(" planoensino.codigo as planoensino, planoensino.ano, planoensino.semestre, planoensino.descricao, planoensino.datacadastro, planoensino.situacao, planoensino.professorresponsavel as professorresponsavel, planoensino.turno as turno, planoensino.curso as cursoPlanoEnsino, planoensino.unidadeensino as unidadeEnsinoPlanoEnsino, planoensino.periodicidade as planoensino_periodicidade, ");
		sqlStr.append(" usuario.codigo as usuario_codigo, usuario.nome as usuario_nome, turno.nome as turno_nome ");
		sqlStr.append(" from ( ");
		
		//traz a disciplina da grade disciplina
		sqlStr.append(" (select distinct disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome, unidadeensino.codigo as unidadeensino_codigo, unidadeensino.nome as unidadeensino_nome, ");
		sqlStr.append(" curso.codigo as curso_codigo, curso.nome  as curso_nome, curso.periodicidade ");
		sqlStr.append(" from gradecurricular ");
		sqlStr.append(" inner join periodoletivo on  periodoletivo.gradecurricular = gradecurricular.codigo ");
		sqlStr.append(" inner join gradedisciplina on  gradedisciplina.periodoletivo = periodoletivo.codigo ");
		sqlStr.append(" inner join disciplina on  gradedisciplina.disciplina = disciplina.codigo ");
		sqlStr.append(" inner join cursoCoordenador on  cursoCoordenador.curso = gradecurricular.curso ");
		sqlStr.append(" inner join curso on  cursoCoordenador.curso = curso.codigo ");
		sqlStr.append(" left join unidadeensino on  cursoCoordenador.unidadeensino = unidadeensino.codigo");
		sqlStr.append(" inner join funcionario on  cursoCoordenador.funcionario = funcionario.codigo ");
		sqlStr.append(" where funcionario.pessoa =  ").append(usuarioVO.getPessoa().getCodigo());
		if(campoConsulta.equals("codigo")){
			sqlStr.append(" and disciplina.codigo = ? ");			
		}else if(campoConsulta.equals("nome")){			
			sqlStr.append(" and sem_acentos(upper(disciplina.nome)) ilike sem_acentos(upper( ? ))");
		}else if(campoConsulta.equals("curso") && curso != null && curso > 0){			
//			sqlStr.append(" and sem_acentos(upper(curso.nome)) ilike sem_acentos(upper( ? ))");
			sqlStr.append(" and curso.codigo = ").append(curso);
		}else if(campoConsulta.equals("turma") && turma != null && turma > 0) {
				sqlStr.append(" and exists (");
				sqlStr.append(" select turma.codigo from turma ");
				sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
				sqlStr.append(" inner join turma as t on turmaagrupada.turma = t.codigo");
				sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
				sqlStr.append(" where turma.turmaagrupada");
				sqlStr.append(" and curso.codigo = t.curso");
				sqlStr.append(" and disciplina.codigo = turmadisciplina.disciplina");
				sqlStr.append(" and turma.codigo = ").append(turma);
				sqlStr.append(" union all");
				sqlStr.append(" select turma.codigo from turma ");
				sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
				sqlStr.append(" where turma.curso = curso.codigo ");
				sqlStr.append(" and turmadisciplina.disciplina = disciplina.codigo ");
				sqlStr.append(" and turma.codigo = ").append(turma);;
				sqlStr.append(" ) ");
		}
		
		if (unidadeEnsino != null) {
			sqlStr.append(" AND (cursoCoordenador.unidadeEnsino = ").append(unidadeEnsino).append(" or cursoCoordenador.unidadeensino is null ) ");
		}		
		
		if(periodoLetivo != null && periodoLetivo > 0 ) {
			sqlStr.append(" AND periodoletivo.periodoletivo = ").append(periodoLetivo);
		}
		
		//traz a disciplina filha da composição da grade disciplina
		sqlStr.append(" ) union (");
		sqlStr.append(" select distinct disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome, unidadeensino.codigo as unidadeensino_codigo, unidadeensino.nome as unidadeensino_nome, ");
		sqlStr.append(" curso.codigo as curso_codigo, curso.nome  as curso_nome, curso.periodicidade ");
		sqlStr.append(" from gradecurricular ");
		sqlStr.append(" inner join periodoletivo on  periodoletivo.gradecurricular = gradecurricular.codigo ");
		sqlStr.append(" inner join gradedisciplina on  gradedisciplina.periodoletivo = periodoletivo.codigo ");
		sqlStr.append(" inner join gradedisciplinacomposta on  gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina ");
		sqlStr.append(" inner join disciplina on  gradedisciplinacomposta.disciplina = disciplina.codigo ");
		sqlStr.append(" inner join cursoCoordenador on  cursoCoordenador.curso = gradecurricular.curso ");
		sqlStr.append(" inner join curso on  cursoCoordenador.curso = curso.codigo ");
		sqlStr.append(" left join unidadeensino on  cursoCoordenador.unidadeensino = unidadeensino.codigo");
		sqlStr.append(" inner join funcionario on  cursoCoordenador.funcionario = funcionario.codigo ");
		sqlStr.append(" where funcionario.pessoa = ").append(usuarioVO.getPessoa().getCodigo());
		if(campoConsulta.equals("codigo")){
			sqlStr.append(" and disciplina.codigo = ? ");			
		}else if(campoConsulta.equals("nome")){			
			sqlStr.append(" and sem_acentos(upper(disciplina.nome)) ilike sem_acentos(upper( ? ))");
		}else if(campoConsulta.equals("curso") && curso != null && curso > 0){			
//			sqlStr.append(" and sem_acentos(upper(curso.nome)) ilike sem_acentos(upper( ? ))");
			sqlStr.append(" and curso.codigo = ").append(curso);
		}else if (campoConsulta.equals("turma") && turma != null && turma > 0) {
			sqlStr.append(" and exists (");
			sqlStr.append(" select turma.codigo from turma ");
			sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
			sqlStr.append(" inner join turma as t on turmaagrupada.turma = t.codigo");
			sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sqlStr.append(" where turma.turmaagrupada");
			sqlStr.append(" and curso.codigo = t.curso");
			sqlStr.append(" and disciplina.codigo = turmadisciplina.disciplina");
			sqlStr.append(" and turma.codigo = ").append(turma);
			sqlStr.append(" union all");
			sqlStr.append(" select turma.codigo from turma ");
			sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sqlStr.append(" where turma.curso = curso.codigo ");
			sqlStr.append(" and turmadisciplina.disciplina = disciplina.codigo ");
			sqlStr.append(" and turma.codigo = ").append(turma);;
			sqlStr.append(" ) ");
		}
		
		if (unidadeEnsino != null) {
			sqlStr.append(" AND (cursoCoordenador.unidadeEnsino = ").append(unidadeEnsino).append(" or cursoCoordenador.unidadeensino is null ) ");
		}	
		
		if(periodoLetivo != null && periodoLetivo > 0) {
			sqlStr.append(" AND periodoletivo.periodoletivo = ").append(periodoLetivo);
		}
		
		//traz a disciplina do grupo de disciplina optativa
		sqlStr.append(" ) union (");
		sqlStr.append(" select distinct disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome, unidadeensino.codigo as unidadeensino_codigo, unidadeensino.nome as unidadeensino_nome, ");
		sqlStr.append(" curso.codigo as curso_codigo, curso.nome  as curso_nome, curso.periodicidade ");
		sqlStr.append(" from gradecurricular ");
		sqlStr.append(" inner join periodoletivo on  periodoletivo.gradecurricular = gradecurricular.codigo ");
		sqlStr.append(" inner join gradecurriculargrupooptativa on  periodoletivo.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
		sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on  gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");			
		sqlStr.append(" inner join disciplina on  gradecurriculargrupooptativadisciplina.disciplina = disciplina.codigo ");
		sqlStr.append(" inner join cursoCoordenador on  cursoCoordenador.curso = gradecurricular.curso ");
		sqlStr.append(" inner join curso on  cursoCoordenador.curso = curso.codigo ");
		sqlStr.append(" left join unidadeensino on  cursoCoordenador.unidadeensino = unidadeensino.codigo");
		sqlStr.append(" inner join funcionario on  cursoCoordenador.funcionario = funcionario.codigo ");				
		sqlStr.append(" where  funcionario.pessoa = ").append(usuarioVO.getPessoa().getCodigo());
		if(campoConsulta.equals("codigo")){
			sqlStr.append(" and disciplina.codigo = ? ");			
		}else if(campoConsulta.equals("nome")){			
			sqlStr.append(" and sem_acentos(upper(disciplina.nome)) ilike sem_acentos(upper( ? ))");
		}else if(campoConsulta.equals("curso") && curso != null && curso > 0){			
//					sqlStr.append(" and sem_acentos(upper(curso.nome)) ilike sem_acentos(upper( ? ))");
			sqlStr.append(" and curso.codigo = ").append(curso);
		}else if(campoConsulta.equals("turma") && turma != null && turma > 0) {
			sqlStr.append(" and exists (");
			sqlStr.append(" select turma.codigo from turma ");
			sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
			sqlStr.append(" inner join turma as t on turmaagrupada.turma = t.codigo");
			sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sqlStr.append(" where turma.turmaagrupada");
			sqlStr.append(" and curso.codigo = t.curso");
			sqlStr.append(" and disciplina.codigo = turmadisciplina.disciplina");
			sqlStr.append(" and turma.codigo = ").append(turma);
			sqlStr.append(" union all");
			sqlStr.append(" select turma.codigo from turma ");
			sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sqlStr.append(" where turma.curso = curso.codigo ");
			sqlStr.append(" and turmadisciplina.disciplina = disciplina.codigo ");
			sqlStr.append(" and turma.codigo = ").append(turma);;
			sqlStr.append(" ) ");
		}
		
		if (unidadeEnsino != null) {
			sqlStr.append(" AND (cursoCoordenador.unidadeEnsino = ").append(unidadeEnsino).append(" or cursoCoordenador.unidadeensino is null ) ");
		}
		if(periodoLetivo != null && periodoLetivo > 0) {
			sqlStr.append(" AND periodoletivo.periodoletivo = ").append(periodoLetivo);
		}
				
		//traz a disciplina filha da composição do grupo de disciplina optativa
		sqlStr.append(" ) union (");
		sqlStr.append(" select distinct disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome, unidadeensino.codigo as unidadeensino_codigo, unidadeensino.nome as unidadeensino_nome, ");
		sqlStr.append(" curso.codigo as curso_codigo, curso.nome  as curso_nome, curso.periodicidade ");
		sqlStr.append(" from gradecurricular ");
		sqlStr.append(" inner join periodoletivo on  periodoletivo.gradecurricular = gradecurricular.codigo ");
		sqlStr.append(" inner join gradecurriculargrupooptativa on  periodoletivo.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
		sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on  gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
		sqlStr.append(" inner join gradedisciplinacomposta on  gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina ");
		sqlStr.append(" inner join disciplina on  gradedisciplinacomposta.disciplina = disciplina.codigo ");
		sqlStr.append(" inner join cursoCoordenador on  cursoCoordenador.curso = gradecurricular.curso ");
		sqlStr.append(" inner join curso on  cursoCoordenador.curso = curso.codigo ");
		sqlStr.append(" left join unidadeensino on  cursoCoordenador.unidadeensino = unidadeensino.codigo");
		sqlStr.append(" inner join funcionario on  cursoCoordenador.funcionario = funcionario.codigo ");
		sqlStr.append(" where funcionario.pessoa = ").append(usuarioVO.getPessoa().getCodigo());
		if(campoConsulta.equals("codigo")){
			sqlStr.append(" and disciplina.codigo = ? ");			
		}else if(campoConsulta.equals("nome")){			
			sqlStr.append(" and sem_acentos(upper(disciplina.nome)) ilike sem_acentos(upper( ? ))");
		}else if(campoConsulta.equals("curso") && curso != null && curso > 0){			
//			sqlStr.append(" and sem_acentos(upper(curso.nome)) ilike sem_acentos(upper( ? ))");
			sqlStr.append(" and curso.codigo = ").append(curso);
		}else if(campoConsulta.equals("turma") && turma != null && turma > 0) {
			sqlStr.append(" and exists (");
			sqlStr.append(" select turma.codigo from turma ");
			sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo");
			sqlStr.append(" inner join turma as t on turmaagrupada.turma = t.codigo");
			sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sqlStr.append(" where turma.turmaagrupada");
			sqlStr.append(" and curso.codigo = t.curso");
			sqlStr.append(" and disciplina.codigo = turmadisciplina.disciplina");
			sqlStr.append(" and turma.codigo = ").append(turma);
			sqlStr.append(" union all");
			sqlStr.append(" select turma.codigo from turma ");
			sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo");
			sqlStr.append(" where turma.curso = curso.codigo ");
			sqlStr.append(" and turmadisciplina.disciplina = disciplina.codigo ");
			sqlStr.append(" and turma.codigo = ").append(turma);;
			sqlStr.append(" ) ");
		}
		
		if (unidadeEnsino != null) {
			sqlStr.append(" AND (cursoCoordenador.unidadeEnsino = ").append(unidadeEnsino).append(" or cursoCoordenador.unidadeensino is null ) ");
		}		
		if(periodoLetivo != null && periodoLetivo > 0) {
			sqlStr.append(" AND periodoletivo.periodoletivo = ").append(periodoLetivo);
		}
		sqlStr.append(" )");
		sqlStr.append(" ) as disciplina ");
		sqlStr.append(" left join planoensino on planoensino.disciplina = disciplina.disciplina_codigo ");
		//sqlStr.append(" and planoensino.curso = disciplina.curso_codigo ");
	//	sqlStr.append(" and planoensino.unidadeensino = disciplina.unidadeensino_codigo ");
		if(!filtrarTodosPlanosEnsino){
			sqlStr.append(" and exists (select pe.codigo from planoensino pe where pe.disciplina = disciplina.disciplina_codigo ");
			//sqlStr.append(" and pe.curso = disciplina.curso_codigo ");		
			/*if((ano != null && !ano.trim().isEmpty()) || (ano != null && !ano.trim().isEmpty() && semestre != null && !semestre.trim().isEmpty())){
				sqlStr.append(" and (( disciplina.periodicidade = 'SE' and pe.ano = '").append(ano).append("' and pe.semestre = '").append(semestre).append("') ");
				sqlStr.append(" or ( disciplina.periodicidade = 'AN' and pe.ano = '");
				sqlStr.append(ano).append("') or (disciplina.periodicidade = 'IN' and ((pe.ano = '' and pe.semestre = '') ");
				sqlStr.append(" or (pe.ano = '").append(ano).append("' and pe.semestre = '").append(semestre).append("')))) ");
			}*/
			if((Uteis.isAtributoPreenchido(ano)) && Uteis.isAtributoPreenchido(semestre)){
				sqlStr.append(" and (( disciplina.periodicidade = 'SE' and pe.ano = '").append(ano).append("' and pe.semestre = '").append(semestre).append("') ");
				sqlStr.append(" or ( disciplina.periodicidade = 'AN' and pe.ano = ' ))");
				sqlStr.append(ano).append("') or (disciplina.periodicidade = 'IN' and ((pe.ano = '' and pe.semestre = '') ");
				sqlStr.append(" or (pe.ano = '").append(ano).append("' and pe.semestre = '").append(semestre).append("')))) ");
			} else if (Uteis.isAtributoPreenchido(semestre) && !Uteis.isAtributoPreenchido(ano)) {
				sqlStr.append(" and ( (disciplina.periodicidade = 'SE' and pe.semestre = '").append(semestre).append("')) ");
			} else if (Uteis.isAtributoPreenchido(ano) && !Uteis.isAtributoPreenchido(semestre)) {
				sqlStr.append(" and (( disciplina.periodicidade = 'SE' and pe.ano = '").append(ano).append("') ");
				sqlStr.append(" or ( disciplina.periodicidade = 'AN' and pe.ano = '").append(ano).append("')) ");
			}
			sqlStr.append(" and planoensino.codigo = pe.codigo ");
			//sqlStr.append(" and pe.unidadeensino = disciplina.unidadeensino_codigo ");
			sqlStr.append(" order by pe.ano desc, pe.semestre desc limit 1 ) ");
		}
		sqlStr.append(" left join usuario on usuario.codigo = planoensino.responsavel ");
		sqlStr.append(" left join turno on turno.codigo = planoensino.turno ");
		if(situacaoPlanoEnsino != null && !SituacaoPlanoEnsinoEnum.TODOS.equals(situacaoPlanoEnsino)){
			if(!SituacaoPlanoEnsinoEnum.NAO_CADASTRADO.equals(situacaoPlanoEnsino)){
				sqlStr.append(" where planoensino.situacao = '").append(situacaoPlanoEnsino.getValor()).append("' ");
			}else{
				sqlStr.append(" where planoensino.codigo is null ");
			}
		}
		if(campoConsulta.equals("codigo")){
			sqlStr.append(" order by disciplina_codigo, disciplina_nome, curso_nome, unidadeensino_nome, ano desc, semestre desc ");
		}else{
			sqlStr.append(" order by disciplina_nome, curso_nome, unidadeensino_nome, ano desc, semestre desc ");
		}	
		//System.out.println(sqlStr);
		SqlRowSet tabelaResultado = null;
		if(campoConsulta.equals("codigo")){
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), Integer.valueOf(valorConsultar), Integer.valueOf(valorConsultar), Integer.valueOf(valorConsultar), Integer.valueOf(valorConsultar));
		}else if(campoConsulta.equals("curso") || campoConsulta.equals("turma")) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		} else{
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), "%"+valorConsultar+"%", "%"+valorConsultar+"%", "%"+valorConsultar+"%", "%"+valorConsultar+"%");
		}
		List<PlanoEnsinoVO> planoEnsinoVOs =  new ArrayList<PlanoEnsinoVO>(0);
		montarDadosConsultaProfessorCoordenador(tabelaResultado, planoEnsinoVOs, usuarioVO);
		return planoEnsinoVOs;
	}
	
	@Override
	public void removerPlanoEnsinoHorarioAula(PlanoEnsinoVO planoEnsinoVO, PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO) throws Exception{
		planoEnsinoVO.getPlanoEnsinoHorarioAulaVOs().remove(planoEnsinoHorarioAulaVO);
		Ordenacao.ordenarLista(planoEnsinoVO.getPlanoEnsinoHorarioAulaVOs(), "ordenacao");
	}
	@Override
	public void adicionarPlanoEnsinoHorarioAula(PlanoEnsinoVO planoEnsinoVO, PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO) throws Exception{
		getFacadeFactory().getPlanoEnsinoHorarioAulaFacade().validarDados(planoEnsinoHorarioAulaVO);
		if(planoEnsinoVO.getPlanoEnsinoHorarioAulaVOs().contains(planoEnsinoHorarioAulaVO)){			
			planoEnsinoVO.getPlanoEnsinoHorarioAulaVOs().set(planoEnsinoVO.getPlanoEnsinoHorarioAulaVOs().indexOf(planoEnsinoHorarioAulaVO), planoEnsinoHorarioAulaVO);
			return;
		}		
		for(PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO2: planoEnsinoVO.getPlanoEnsinoHorarioAulaVOs()){
			if(planoEnsinoHorarioAulaVO2.getDiaSemana().equals(planoEnsinoHorarioAulaVO.getDiaSemana())){
					if((Uteis.realizarValidacaoHora1MaiorHora2(planoEnsinoHorarioAulaVO.getInicioAula(), planoEnsinoHorarioAulaVO2.getInicioAula())
							&& (Uteis.realizarValidacaoHora1MaiorHora2(planoEnsinoHorarioAulaVO2.getTerminoAula(), planoEnsinoHorarioAulaVO.getTerminoAula())
									|| planoEnsinoHorarioAulaVO2.getTerminoAula().equals(planoEnsinoHorarioAulaVO.getTerminoAula())))
						|| ((Uteis.realizarValidacaoHora1MaiorHora2(planoEnsinoHorarioAulaVO2.getInicioAula(), planoEnsinoHorarioAulaVO.getInicioAula())
								|| planoEnsinoHorarioAulaVO2.getInicioAula().equals(planoEnsinoHorarioAulaVO.getInicioAula()))
							&& Uteis.realizarValidacaoHora1MaiorHora2(planoEnsinoHorarioAulaVO.getTerminoAula(), planoEnsinoHorarioAulaVO2.getInicioAula()))) {
	                    StringBuilder sb = new StringBuilder("A aula a ser adicionado está com choque de horário com a ");
	                    sb.append("aula do(a) ");
	                    sb.append(planoEnsinoHorarioAulaVO2.getDiaSemana().getDescricao()).append(" as ").append(planoEnsinoHorarioAulaVO2.getInicioAula()).append(" - ").append(planoEnsinoHorarioAulaVO2.getTerminoAula());
	                    throw new ConsistirException(sb.toString());
	                }
	              
			}
		}
		planoEnsinoVO.getPlanoEnsinoHorarioAulaVOs().add(planoEnsinoHorarioAulaVO);
		Ordenacao.ordenarLista(planoEnsinoVO.getPlanoEnsinoHorarioAulaVOs(), "ordenacao");
		
	} 
	

	@Override
	public PlanoEnsinoVO consultarPlanoEnsinoValidoPorUnidadeEnsinoCursoDisciplinaAnoSemestre(Integer unidadeEnsino, Integer curso, Integer disciplina, String ano, String semestre, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = getSelectDadosBasicos();
		sql.append(" WHERE ");
		sql.append(" unidadeEnsino.codigo = ").append(unidadeEnsino);
		sql.append(" and curso.codigo = ").append(curso);
		sql.append(" and disciplina.codigo = ").append(disciplina);
		sql.append(" and planoensino.situacao = '").append(SituacaoPlanoEnsinoEnum.AUTORIZADO.getValor()).append("'");
		if (ano != null && !ano.equals("") && (semestre == null || semestre.trim().isEmpty())) {
			sql.append(" and planoensino.ano <= '").append(ano).append("' ");
		}
		if (semestre != null && !semestre.equals("")) {
			sql.append(" and (planoensino.ano||planoensino.semestre) <= '").append(ano+semestre).append("' ");
		}
		sql.append(" order by (planoensino.ano||planoensino.semestre) desc limit 1  ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<PlanoEnsinoVO> planoEnsinoVOs = montarDadosBasico(rs, usuarioVO);
		if (!planoEnsinoVOs.isEmpty()) {
			PlanoEnsinoVO obj = planoEnsinoVOs.get(0);
			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
				obj.setReferenciaBibliograficaVOs(getFacadeFactory().getReferenciaBibliograficaFacade().consultarReferenciaBibliograficaPorPlanoEnsino(obj.getCodigo(), false, usuarioVO));
				obj.setConteudoPlanejamentoVOs(getFacadeFactory().getConteudoPlanejamentoFacade().consultarConteudoPlanejamentoPorPlanoEnsino(obj.getCodigo(), false, usuarioVO));
				obj.setPlanoEnsinoHorarioAulaVOs(getFacadeFactory().getPlanoEnsinoHorarioAulaFacade().consultarPorPlanoEnsino(obj.getCodigo(), usuarioVO));
			}
			return obj;
		}
		return new PlanoEnsinoVO();
	}
	
	private void validarPeriodicidadeCursoParaPlanoEnsino(PlanoEnsinoVO planoEnsinoVO) throws Exception{
		CursoVO curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(planoEnsinoVO.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, null);
		if (curso.getIntegral()) {
			planoEnsinoVO.setAno("");
			planoEnsinoVO.setSemestre("");
		} else if (curso.getAnual()) {
			planoEnsinoVO.setSemestre("");
		}
	}
	
	private void montarDadosConsultaProfessorCoordenador(SqlRowSet tabelaResultado, List<PlanoEnsinoVO> planoEnsinoVOs, UsuarioVO usuarioVO) throws Exception {
		PlanoEnsinoVO planoEnsinoVO = null;
		while(tabelaResultado.next()){
			planoEnsinoVO = new PlanoEnsinoVO();
			planoEnsinoVO.setCodigo(tabelaResultado.getInt("planoensino"));
			if(tabelaResultado.getInt("planoensino") == 0){
				planoEnsinoVO.setNovoObj(true);
				planoEnsinoVO.setSituacao("NC");
			}else{
				planoEnsinoVO.setSituacao(tabelaResultado.getString("situacao"));
				planoEnsinoVO.setNovoObj(false);
			}
			planoEnsinoVO.setAno(tabelaResultado.getString("ano"));
			planoEnsinoVO.setDescricao(tabelaResultado.getString("descricao"));
			planoEnsinoVO.setDataCadastro(tabelaResultado.getDate("dataCadastro"));
			planoEnsinoVO.setSemestre(tabelaResultado.getString("semestre"));
			planoEnsinoVO.getDisciplina().setCodigo(tabelaResultado.getInt("disciplina_codigo"));
			planoEnsinoVO.getDisciplina().setNome(tabelaResultado.getString("disciplina_nome"));
			planoEnsinoVO.getCurso().setCodigo(tabelaResultado.getInt("cursoPlanoEnsino"));
			//planoEnsinoVO.getCurso().setNome(tabelaResultado.getString("curso_nome"));
			planoEnsinoVO.setPeriodicidade(tabelaResultado.getString("planoensino_periodicidade"));
			planoEnsinoVO.getUnidadeEnsino().setCodigo(tabelaResultado.getInt("unidadeEnsinoPlanoEnsino"));
		//	planoEnsinoVO.getUnidadeEnsino().setNome(tabelaResultado.getString("unidadeensino_nome"));
			planoEnsinoVO.getResponsavel().setCodigo(tabelaResultado.getInt("usuario_codigo"));
			planoEnsinoVO.getResponsavel().setNome(tabelaResultado.getString("usuario_nome"));
			planoEnsinoVO.getProfessorResponsavel().setCodigo(tabelaResultado.getInt("professorresponsavel"));
			planoEnsinoVO.getTurno().setCodigo(tabelaResultado.getInt("turno"));
			planoEnsinoVO.getTurno().setNome(tabelaResultado.getString("turno_nome"));
			planoEnsinoVO.setPeriodicidade(tabelaResultado.getString("planoensino_periodicidade"));
			if(Uteis.isAtributoPreenchido(planoEnsinoVO.getUnidadeEnsino().getCodigo())) {
				planoEnsinoVO.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(planoEnsinoVO.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			}
			if(Uteis.isAtributoPreenchido(planoEnsinoVO.getCurso().getCodigo())) {
				planoEnsinoVO.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(planoEnsinoVO.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuarioVO));
			}
			montarDadosProfessorResponsavel(planoEnsinoVO, Uteis.NIVELMONTARDADOS_COMBOBOX, null);
			planoEnsinoVOs.add(planoEnsinoVO);
		}
	}
	
	private void montarDadosProfessorResponsavel(PlanoEnsinoVO planoEnsinoVO, int NivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		if (planoEnsinoVO.getProfessorResponsavel().getCodigo() == 0) {
			return;
		}
		planoEnsinoVO.setProfessorResponsavel(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(planoEnsinoVO.getProfessorResponsavel().getCodigo(), false, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
	}
	
	public PlanoEnsinoVO consultarPorDisciplinaMatriculaAluno(Integer disciplina, String matricula, boolean buscarPlanoVinculadoProfessorResponsavel, HistoricoVO historicoVO, UsuarioVO usuarioVO) throws Exception {
		return consultarPorDisciplinaMatriculaAluno(disciplina, matricula, buscarPlanoVinculadoProfessorResponsavel, "", usuarioVO);
	}
	
	@Override
	public void realizarCriacaoQuestionarioRespostaOrigem(PlanoEnsinoVO planoEnsinoVO, QuestionarioVO questionarioVO, UsuarioVO usuarioVO) throws Exception{
		if(Uteis.isAtributoPreenchido(questionarioVO)) {
			questionarioVO =  getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(questionarioVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			planoEnsinoVO.setQuestionarioRespostaOrigemVO(getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().preencherQuestionarioRespostaOrigem(questionarioVO, usuarioVO));
			planoEnsinoVO.getQuestionarioRespostaOrigemVO().setEscopo(EscopoPerguntaEnum.PLANO_ENSINO);
			planoEnsinoVO.getQuestionarioRespostaOrigemVO().setSituacaoQuestionarioRespostaOrigemEnum(SituacaoQuestionarioRespostaOrigemEnum.DEFERIDO);
			planoEnsinoVO.getQuestionarioRespostaOrigemVO().setPlanoEnsinoVO(planoEnsinoVO);
		}
	}
	
	public void preencherDadosPlanoEnsinoQuestionarioRespostaOrigem(PlanoEnsinoVO planoEnsinoVO, Integer codigoQuestionarioPlanoEnsino, UsuarioVO usuarioVO) throws Exception{		
		Integer codigoQuestionario = 0;
		if(Uteis.isAtributoPreenchido(planoEnsinoVO.getCurso())) {
			planoEnsinoVO.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(planoEnsinoVO.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuarioVO));
			codigoQuestionario = planoEnsinoVO.getCurso().getQuestionarioVO().getCodigo();
		}else {
			codigoQuestionario = codigoQuestionarioPlanoEnsino;
		}
		
		planoEnsinoVO.setQuestionarioRespostaOrigemVO(getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().consultarPorQuestionarioPlanoEnsino(codigoQuestionario, planoEnsinoVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuarioVO));
		if(Uteis.isAtributoPreenchido(planoEnsinoVO.getQuestionarioRespostaOrigemVO())) {
			planoEnsinoVO.getQuestionarioRespostaOrigemVO().setPerguntaRespostaOrigemVOs(getFacadeFactory().getPerguntaRespostaOrigemInterfaceFacade().consultarPorQuestionarioPlanoEnsino(codigoQuestionario, planoEnsinoVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuarioVO));
			 
			for (PerguntaRespostaOrigemVO perguntaRespostaOrigemPrincipalVO : planoEnsinoVO.getQuestionarioRespostaOrigemVO().getPerguntaRespostaOrigemVOs()) {
				getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().editarListaPerguntaItemRespostaOrigemAdicionadasVO(perguntaRespostaOrigemPrincipalVO, perguntaRespostaOrigemPrincipalVO.getPerguntaItemRespostaOrigemVOs(), usuarioVO);
			}
		}
	
	}
	
	@Override
	public List<PlanoEnsinoVO> consultarPlanoPorUnidadeEnsinoCursoDisciplinaAnoSemestre(Integer unidadeEnsino, Integer curso, Integer disciplina, String ano, String semestre, Integer turno, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = getSelectDadosBasicos();
		sql.append(" WHERE 1 = 1 ");
		if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sql.append(" and unidadeEnsino.codigo = ").append(unidadeEnsino);
		}

		if(Uteis.isAtributoPreenchido(curso)) {
			sql.append(" and curso.codigo = ").append(curso);
		}

		sql.append(" and disciplina.codigo = ").append(disciplina);

		if (ano != null && !ano.equals("")) {
			sql.append(" and planoensino.ano = '").append(ano).append("' ");
		}

		if (semestre != null && !semestre.equals("")) {
			sql.append(" and planoensino.semestre = '").append(semestre).append("' ");
		}

		if (Uteis.isAtributoPreenchido(turno)) {
			sql.append(" and planoensino.turno = ").append(turno);			
		}

		sql.append(" order by planoensino.ano desc, planoensino.semestre desc  ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<PlanoEnsinoVO> planoEnsinoVOs = new ArrayList<PlanoEnsinoVO>(0);
		planoEnsinoVOs = montarDadosBasico(rs, usuarioVO);
		if (!planoEnsinoVOs.isEmpty()) {
			for(PlanoEnsinoVO obj: planoEnsinoVOs) {
				if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
					obj.setReferenciaBibliograficaVOs(getFacadeFactory().getReferenciaBibliograficaFacade().consultarReferenciaBibliograficaPorPlanoEnsino(obj.getCodigo(), false, usuarioVO));
					obj.setConteudoPlanejamentoVOs(getFacadeFactory().getConteudoPlanejamentoFacade().consultarConteudoPlanejamentoPorPlanoEnsino(obj.getCodigo(), false, usuarioVO));
				}
			}
		}
		return planoEnsinoVOs;
	}

	public StringBuilder getSelectDadosBasicos() {
		return getSelectDadosBasicos(false);
	}
}
