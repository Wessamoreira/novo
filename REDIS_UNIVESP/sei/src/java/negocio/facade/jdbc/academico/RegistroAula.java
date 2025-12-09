package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import negocio.comuns.academico.CalendarioRegistroAulaVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.FrequenciaAulaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.HorarioProfessorDiaItemVO;
import negocio.comuns.academico.HorarioProfessorDiaVO;
import negocio.comuns.academico.HorarioProfessorVO;
import negocio.comuns.academico.HorarioTurmaDiaItemVO;
import negocio.comuns.academico.HorarioTurmaDiaVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProfessorTitularDisciplinaTurmaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.TransferenciaEntradaRegistroAulaFrequenciaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoHorarioVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.TipoHorarioEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioPerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.job.NotificacaoRegistroAulaNaoLancadaVO;
import negocio.comuns.secretaria.enumeradores.TipoAlteracaoSituacaoHistoricoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.RegistroAulaInterfaceFacade;
import relatorio.negocio.comuns.academico.AlunoComFrequenciaBaixaRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>RegistroAulaVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>RegistroAulaVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see RegistroAulaVO
 * @see ControleAcesso
 */
@Lazy
@Repository
@Scope("singleton")
public class RegistroAula extends ControleAcesso implements RegistroAulaInterfaceFacade {

	protected static String idEntidade;
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static final long serialVersionUID = 1L;

	@SuppressWarnings("OverridableMethodCallInConstructor")
	public RegistroAula() throws Exception {
		super();
		setIdEntidade("RegistroAula");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>RegistroAulaVO</code>.
	 */
	public RegistroAulaVO novo() throws Exception {
		RegistroAula.incluir(getIdEntidade());
		RegistroAulaVO obj = new RegistroAulaVO();
		return obj;
	}

	public static void verificaPermissaoAlterarCargaHoraria() throws Exception {
//		verificarPermissaoUsuarioFuncionalidade("RegistroAulaAlterarCargaHoraria");
	}

	public void verificarSeExisteProgramacaoAulaParaEssaTurma_e_Disciplina(RegistroAulaVO obj) throws Exception {
		if (existeRegistroAula(obj)) {
			throw new Exception("Já existe aula registrada nesta data, turma e horário.");
		}
	}

	public void validarConsultaDoUsuario(UsuarioVO usuario) throws Exception {
		RegistroAula.consultar(getIdEntidade(), true, usuario);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<RegistroAulaVO> registroAulaVOs, String conteudo, String tipoAula, Boolean permiteLancamentoAulaFutura, String idEntidade, String operacao, UsuarioVO usuarioVO, Boolean validarCalendarioRegistroAula) throws Exception {

		if (!registroAulaVOs.isEmpty()) {
			for (RegistroAulaVO registroAula : registroAulaVOs) {
				this.validarQuantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAulaVisaoProfessor(registroAula.getData(), registroAula.getTurma(), registroAula.getDisciplina(), registroAula.getProfessor(), registroAula.getAno(), registroAula.getSemestre(), usuarioVO);
 
				registroAula.setConteudo(conteudo);
				registroAula.setTipoAula(tipoAula);
				if (registroAula.getCargaHoraria().intValue() == 0) {
					TurnoVO turnoVO = new TurnoVO();
					turnoVO = getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(registroAula.getTurma().getTurno().getCodigo().intValue(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
					switch (Uteis.getDiaSemana(registroAula.getData())) {
					// CASO SEJA DOMINGO
					case 1:
						if (!turnoVO.getTurnoHorarioDomingo().isEmpty() && turnoVO.getTurnoHorarioDomingo().size() >= registroAula.getNrAula()) {
							registroAula.setCargaHoraria(turnoVO.getTurnoHorarioDomingo().get(registroAula.getNrAula()-1).getDuracaoAula());
						}
						break;
					// CASO SEJA SEGUNDA
					case 2:
						if (!turnoVO.getTurnoHorarioSegunda().isEmpty() && turnoVO.getTurnoHorarioSegunda().size() >= registroAula.getNrAula()) {
							registroAula.setCargaHoraria(turnoVO.getTurnoHorarioSegunda().get(registroAula.getNrAula()-1).getDuracaoAula());
						}
						break;
					// CASO SEJA TERÇA
					case 3:
						if (!turnoVO.getTurnoHorarioTerca().isEmpty() && turnoVO.getTurnoHorarioTerca().size() >= registroAula.getNrAula()) {
							registroAula.setCargaHoraria(turnoVO.getTurnoHorarioTerca().get(registroAula.getNrAula()-1).getDuracaoAula());
						}
						break;
					// CASO SEJA QUARTA
					case 4:
						if (!turnoVO.getTurnoHorarioQuarta().isEmpty() && turnoVO.getTurnoHorarioQuarta().size() >= registroAula.getNrAula()) {
							registroAula.setCargaHoraria(turnoVO.getTurnoHorarioQuarta().get(registroAula.getNrAula()-1).getDuracaoAula());
						}
						break;
					// CASO SEJA QUINTA
					case 5:
						if (!turnoVO.getTurnoHorarioQuinta().isEmpty() && turnoVO.getTurnoHorarioQuinta().size() >= registroAula.getNrAula()) {
							registroAula.setCargaHoraria(turnoVO.getTurnoHorarioQuinta().get(registroAula.getNrAula()-1).getDuracaoAula());
						}
						break;
					// CASO SEJA SEXTA
					case 6:
						if (!turnoVO.getTurnoHorarioSexta().isEmpty() && turnoVO.getTurnoHorarioSexta().size() >= registroAula.getNrAula()) {
							registroAula.setCargaHoraria(turnoVO.getTurnoHorarioSexta().get(registroAula.getNrAula()-1).getDuracaoAula());
						}
						break;
					// CASO SEJA SABADO
					case 7:
						if (!turnoVO.getTurnoHorarioSabado().isEmpty() && turnoVO.getTurnoHorarioSabado().size() >= registroAula.getNrAula()) {
							registroAula.setCargaHoraria(turnoVO.getTurnoHorarioSabado().get(registroAula.getNrAula()-1).getDuracaoAula());
						}
						break;
					}

				}
				if (registroAula.isNovoObj().booleanValue()) {
					ControleAcesso.incluir(idEntidade, true, usuarioVO);
					getFacadeFactory().getRegistroAulaFacade().incluir(registroAula, permiteLancamentoAulaFutura, usuarioVO, validarCalendarioRegistroAula);
				} else {
					ControleAcesso.alterar(idEntidade, true, usuarioVO);
					getFacadeFactory().getRegistroAulaFacade().alterar(registroAula, permiteLancamentoAulaFutura, usuarioVO);
				}
				if (registroAula.getDisciplina().getNome().equals("") && !registroAula.getDisciplina().getCodigo().equals(0)) {
					registroAula.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimariaSemExcecao(registroAula.getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
				}
				if (!registroAula.getTurma().getCodigo().equals(0) && registroAula.getTurma().getIdentificadorTurma().equals("")) {
					registroAula.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(registroAula.getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
				}
				getFacadeFactory().getLogRegistroAulaFacade().registrarLogRegistroAula(registroAula, registroAula.getDisciplina().getNome(), operacao, usuarioVO);
			}
			/**
			 * Recurso que lança no historico a frequencia do aluno ao registrar aula,
			 * onde se historico estiver reprovado por falta e a frequencia recalculada for maior que a frequencia minima então é recalculado a nota do aluno
			 *  onde se historico estiver aprovado e a frequencia recalculada for menor que a frequencia minima então é recalculado a nota do aluno reprovando o mesmo por falta
			 */
			
			boolean realizarCalculoHistorico = true;
			if(realizarCalculoHistorico){
			// Altera a frequencia no historico do aluno
			List<Integer> mptdProcessada = new ArrayList<Integer>(0);
			for(RegistroAulaVO registroAulaVO:registroAulaVOs){
				for(FrequenciaAulaVO frequenciaAulaVO: registroAulaVO.getFrequenciaAulaVOs()){
					if(Uteis.isAtributoPreenchido(frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplina()) && !mptdProcessada.contains(frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplina())){
						mptdProcessada.add(frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplina());
						frequenciaAulaVO.setHistoricoVO(getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoTurmaDisciplina(frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplina(), false, false, usuarioVO));
						getFacadeFactory().getHistoricoFacade().executarGeracaoFaltaPrimeiroSegundoTerceiroQuartoBimestreTotalFaltaFrequenciaHistorico(frequenciaAulaVO.getHistoricoVO(), frequenciaAulaVO.getHistoricoVO().getConfiguracaoAcademico(), usuarioVO);
						if((frequenciaAulaVO.getHistoricoVO().getSituacao().equals(SituacaoHistorico.REPROVADO_FALTA.getValor()) && frequenciaAulaVO.getHistoricoVO().getFreguencia() >=  frequenciaAulaVO.getHistoricoVO().getConfiguracaoAcademico().getPercentualFrequenciaAprovacao())
								|| (frequenciaAulaVO.getHistoricoVO().getSituacao().equals(SituacaoHistorico.APROVADO.getValor()) && frequenciaAulaVO.getHistoricoVO().getFreguencia() <  frequenciaAulaVO.getHistoricoVO().getConfiguracaoAcademico().getPercentualFrequenciaAprovacao())){
							getFacadeFactory().getHistoricoFacade().calcularMedia(frequenciaAulaVO.getHistoricoVO(), null, frequenciaAulaVO.getHistoricoVO().getConfiguracaoAcademico(), 0, TipoAlteracaoSituacaoHistoricoEnum.TODOS_HISTORICOS, true, usuarioVO);
							getFacadeFactory().getHistoricoFacade().gravarLancamentoNota(frequenciaAulaVO.getHistoricoVO(), true, usuarioVO.getVisaoLogar(), TipoAlteracaoSituacaoHistoricoEnum.TODOS_HISTORICOS, usuarioVO);						
						}else{
							getFacadeFactory().getHistoricoFacade().alterarFaltaEFrequenciaHistorico(frequenciaAulaVO.getHistoricoVO(), false, usuarioVO);
						}
					}
				}
			}
			}

		}
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>RegistroAulaVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>RegistroAulaVO</code> que será gravado
	 *            no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final RegistroAulaVO obj, Boolean permiteRegistrarAulaFutura, UsuarioVO usuario, Boolean validarCalendarioRegistroAula) throws Exception {
		incluir(obj, permiteRegistrarAulaFutura, 0, usuario, validarCalendarioRegistroAula);
	}

	public void realizarVerificacaoBloqueioLancamentoRegistroAula(RegistroAulaVO registroAulaVO, UsuarioVO usuarioVO) throws Exception {
		if (usuarioVO == null || !usuarioVO.getIsApresentarVisaoProfessor()) {
			return;
		}
		if (registroAulaVO.getTurma().getTurmaAgrupada()) {
			registroAulaVO.getTurma().getCurso().setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorCodigoTurmaAgrupada(registroAulaVO.getTurma().getCodigo(), usuarioVO));
		}else {
			registroAulaVO.getTurma().getCurso().setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorCodigoCurso(registroAulaVO.getTurma().getCurso().getCodigo(), usuarioVO));
		}
		CalendarioRegistroAulaVO calendarioRegistroAulaVO = getFacadeFactory().getCalendarioRegistroAulaFacade().consultarPorCalendarioRegistroAulaUtilizar(registroAulaVO.getTurma().getUnidadeEnsino().getCodigo(), registroAulaVO.getTurma().getCodigo(), registroAulaVO.getTurma().getTurmaAgrupada(), registroAulaVO.getProfessor().getCodigo(), Uteis.getData(registroAulaVO.getData(), "yyyy"), false, usuarioVO);
		if (calendarioRegistroAulaVO == null || calendarioRegistroAulaVO.getCodigo() == null || calendarioRegistroAulaVO.getCodigo() == 0 || Uteis.isAtributoPreenchido(registroAulaVO.getTurma().getCurso().getConfiguracaoAcademico().getQuantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAula())) {
			return;
		}
		Date dataBase = Uteis.getDataJDBC(new Date());
		String dataBaseStr = Uteis.getData(dataBase);
		String mensagemCodigoCalendario = "O calendário de registro de aula utilizado é de código " + calendarioRegistroAulaVO.getCodigo();
		if (Uteis.getMesData(registroAulaVO.getData()) == 1 && Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataJaneiro()).compareTo(dataBase) < 0 && !Uteis.getData(calendarioRegistroAulaVO.getDataJaneiro()).equals(dataBaseStr)) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataJaneiro())) + mensagemCodigoCalendario);
		}
		if (Uteis.getMesData(registroAulaVO.getData()) == 2 && Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataFevereiro()).before(Uteis.getDataJDBC(dataBase)) && !Uteis.getData(calendarioRegistroAulaVO.getDataFevereiro()).equals(dataBaseStr)) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataFevereiro())) + mensagemCodigoCalendario);
		}
		if (Uteis.getMesData(registroAulaVO.getData()) == 3 && Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataMarco()).compareTo(dataBase) < 0 && !Uteis.getData(calendarioRegistroAulaVO.getDataMarco()).equals(dataBaseStr)) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataMarco())) + mensagemCodigoCalendario);
		}
		if (Uteis.getMesData(registroAulaVO.getData()) == 4 && Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataAbril()).compareTo(dataBase) < 0 && !Uteis.getData(calendarioRegistroAulaVO.getDataAbril()).equals(dataBaseStr)) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataAbril())) + mensagemCodigoCalendario);
		}
		if (Uteis.getMesData(registroAulaVO.getData()) == 5 && Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataMaio()).compareTo(dataBase) < 0 && !Uteis.getData(calendarioRegistroAulaVO.getDataMaio()).equals(dataBaseStr)) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataMaio())) + mensagemCodigoCalendario);
		}
		if (Uteis.getMesData(registroAulaVO.getData()) == 6 && Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataJunho()).compareTo(dataBase) < 0 && !Uteis.getData(calendarioRegistroAulaVO.getDataJunho()).equals(dataBaseStr)) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataJunho())) + mensagemCodigoCalendario);
		}
		if (Uteis.getMesData(registroAulaVO.getData()) == 7 && Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataJulho()).compareTo(dataBase) < 0 && !Uteis.getData(calendarioRegistroAulaVO.getDataJulho()).equals(dataBaseStr)) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataJulho())) + mensagemCodigoCalendario);
		}
		if (Uteis.getMesData(registroAulaVO.getData()) == 8 && Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataAgosto()).compareTo(dataBase) < 0 && !Uteis.getData(calendarioRegistroAulaVO.getDataAgosto()).equals(dataBaseStr)) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataAgosto())) + mensagemCodigoCalendario);
		}
		if (Uteis.getMesData(registroAulaVO.getData()) == 9 && Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataSetembro()).compareTo(dataBase) < 0 && !Uteis.getData(calendarioRegistroAulaVO.getDataSetembro()).equals(dataBaseStr)) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataSetembro())) + mensagemCodigoCalendario);
		}
		if (Uteis.getMesData(registroAulaVO.getData()) == 10 && Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataOutubro()).compareTo(dataBase) < 0 && !Uteis.getData(calendarioRegistroAulaVO.getDataOutubro()).equals(dataBaseStr)) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataOutubro())) + mensagemCodigoCalendario);
		}
		if (Uteis.getMesData(registroAulaVO.getData()) == 11 && Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataNovembro()).compareTo(dataBase) < 0 && !Uteis.getData(calendarioRegistroAulaVO.getDataNovembro()).equals(dataBaseStr)) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataNovembro())) + mensagemCodigoCalendario);
		}
		if (Uteis.getMesData(registroAulaVO.getData()) == 12 && Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataDezembro()).compareTo(dataBase) < 0 && !Uteis.getData(calendarioRegistroAulaVO.getDataDezembro()).equals(dataBaseStr)) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistroAula_limiteLancamentoEstourado").replace("{1}", Uteis.getData(calendarioRegistroAulaVO.getDataDezembro())) + mensagemCodigoCalendario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final RegistroAulaVO obj, Boolean permiteRegistrarAulaFutura, Integer perfilPadraoProfessor, UsuarioVO usuario, Boolean validarCalendarioRegistroAula) throws Exception {
		try {
			RegistroAulaVO.validarDados(obj, permiteRegistrarAulaFutura);
			this.validarQuantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAulaVisaoProfessor(obj.getData(), obj.getTurma(), obj.getDisciplina(), obj.getProfessor(), obj.getAno(), obj.getSemestre(), usuario);
			if (validarCalendarioRegistroAula) {
				realizarVerificacaoBloqueioLancamentoRegistroAula(obj, usuario);
			}
			// RegistroAula.incluir(getIdEntidade());
			final String sql = "INSERT INTO RegistroAula( data, cargaHoraria, conteudo, turma, responsavelRegistroAula, dataRegistroAula, disciplina, diaSemana, tipoAula, professor, semestre, ano, horario, atividadeComplementar, nrAula, turmaorigem, praticaSupervisionada ) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
					sqlInserir.setInt(2, obj.getCargaHoraria().intValue());
					sqlInserir.setString(3, obj.getConteudo());
					sqlInserir.setInt(4, obj.getTurma().getCodigo().intValue());
					sqlInserir.setInt(5, obj.getResponsavelRegistroAula().getCodigo().intValue());
					sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getDataRegistroAula()));
					sqlInserir.setInt(7, obj.getDisciplina().getCodigo().intValue());
					sqlInserir.setString(8, obj.getDiaSemana());
					sqlInserir.setString(9, obj.getTipoAula());
					sqlInserir.setInt(10, obj.getProfessor().getCodigo().intValue());
					sqlInserir.setString(11, obj.getSemestre());
					sqlInserir.setString(12, obj.getAno());
					sqlInserir.setString(13, obj.getHorario());
					sqlInserir.setBoolean(14, obj.getAtividadeComplementar());
					sqlInserir.setInt(15, obj.getNrAula());
					if(Uteis.isAtributoPreenchido(obj.getTurmaOrigem())){
						sqlInserir.setInt(16, obj.getTurmaOrigem());
					}else{
						sqlInserir.setNull(16, 0);
					}
					sqlInserir.setString(17, obj.getPraticaSupervisionada());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getFrequenciaAulaFacade().persistirFrequenciaAulaVOs(obj, perfilPadraoProfessor, usuario);
			getFacadeFactory().getProfessorMinistrouAulaTurmaFacade().incluirProfessorMinistrouAulaTurma(obj.getTurma(), obj.getDisciplina().getCodigo(), obj.getProfessor().getCodigo(), obj.getAno(), obj.getSemestre(), obj.getData(), usuario);
		
			if (getFacadeFactory().getTurmaFacade().verificarTurmaAgrupada(obj.getTurma().getCodigo()) && !obj.getTurma().getSubturma()) {
				List<TurmaVO> listaTurmasAgrupadas = new ArrayList<TurmaVO>(0);
				listaTurmasAgrupadas = getFacadeFactory().getTurmaFacade().consultaRapidaTurmasNasQuaisTurmaParticipaDeAgrupamentoPossuemAlunos(obj.getTurma().getCodigo(), obj.getAno(), obj.getSemestre(), false, usuario);				
				for (TurmaVO turma : listaTurmasAgrupadas) {
					RegistroAulaVO regNovo = (RegistroAulaVO) obj.clone();
					DisciplinaVO disciplinaEquivalenteTurma = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaEquivalenteTurmaAgrupadaPorTurma(turma.getCodigo(), obj.getTurma().getCodigo(), obj.getDisciplina().getCodigo(), usuario);
					if (!disciplinaEquivalenteTurma.getCodigo().equals(0)) {
						if (!disciplinaEquivalenteTurma.getCodigo().equals(regNovo.getDisciplina().getCodigo())) {
							getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().alterarDisciplinaEquivalenteTurmaAgrupada(disciplinaEquivalenteTurma.getCodigo(), regNovo.getTurma().getCodigo(), regNovo.getProfessor().getCodigo(), regNovo.getDisciplina().getCodigo(), regNovo.getAno(), regNovo.getSemestre(), usuario);
						}
						regNovo.setDisciplina(disciplinaEquivalenteTurma);
					}
					regNovo.setTurma(turma);
					regNovo.setCodigo(0);
					regNovo.setNovoObj(true);
					regNovo.setAtividadeComplementar(obj.getAtividadeComplementar());
					regNovo.setFrequenciaAulaVOs(null);
					regNovo.setTurmaOrigem(obj.getTurma().getCodigo());
					regNovo.setFrequenciaAulaVOs(montarListaFrequenciaClonada(obj.getFrequenciaAulaVOs(), turma, regNovo));
					if(!regNovo.getFrequenciaAulaVOs().isEmpty()){
						incluir(regNovo, permiteRegistrarAulaFutura, 0, usuario, validarCalendarioRegistroAula);
					}
				}
			}			
		} catch (Exception e) {
			for (FrequenciaAulaVO frequenciaAulaVO : obj.getFrequenciaAulaVOs()) {
				frequenciaAulaVO.setNovoObj(true);
			}
			
			obj.setNovoObj(true);
			obj.setCodigo(0);
			if (e.getMessage().contains("unq_registroaula_data_disciplina_turma_nraula")) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_registroAula_registroDuplicado"));
			}
			throw e;
		} 
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>RegistroAulaVO</code>. Sempre utiliza a chave primária da classe
	 * como atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>RegistroAulaVO</code> que será alterada
	 *            no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final RegistroAulaVO obj, Boolean permiteRegistrarAulaFutura, UsuarioVO usuario) throws Exception {
		alterar(obj, permiteRegistrarAulaFutura, 0, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final RegistroAulaVO obj, Boolean permiteRegistrarAulaFutura, Integer perfilPadraoProfessor, UsuarioVO usuario) throws Exception {
		try {
			RegistroAulaVO.validarDados(obj, permiteRegistrarAulaFutura);
			realizarVerificacaoBloqueioLancamentoRegistroAula(obj, usuario);
			this.validarQuantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAulaVisaoProfessor(obj.getData(), obj.getTurma(), obj.getDisciplina(), obj.getProfessor(), obj.getAno(), obj.getSemestre(), usuario);
			verificarExistenciaRegistroAula(obj);
			validarQuantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAulaVisaoProfessor(obj.getData(), obj.getTurma(), obj.getDisciplina(), obj.getProfessor(), obj.getAno(), obj.getSemestre(), usuario);

			RegistroAula.alterar(getIdEntidade());
			final String sql = "UPDATE RegistroAula set data=?, cargaHoraria=?, conteudo=?, turma=?, responsavelRegistroAula=?, dataRegistroAula=?, disciplina=?, diaSemana=?, tipoAula=?, professor = ?, semestre=?, ano=?, horario =?, atividadeComplementar=?, nrAula = ?, turmaOrigem = ?, praticaSupervisionada=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
					sqlAlterar.setInt(2, obj.getCargaHoraria().intValue());
					sqlAlterar.setString(3, obj.getConteudo());
					sqlAlterar.setInt(4, obj.getTurma().getCodigo().intValue());
					sqlAlterar.setInt(5, obj.getResponsavelRegistroAula().getCodigo().intValue());
					sqlAlterar.setDate(6, Uteis.getDataJDBC(obj.getDataRegistroAula()));
					sqlAlterar.setInt(7, obj.getDisciplina().getCodigo().intValue());
					sqlAlterar.setString(8, obj.getDiaSemana());
					sqlAlterar.setString(9, obj.getTipoAula());
					sqlAlterar.setInt(10, obj.getProfessor().getCodigo().intValue());
					sqlAlterar.setString(11, obj.getSemestre());
					sqlAlterar.setString(12, obj.getAno());
					sqlAlterar.setString(13, obj.getHorario());
					sqlAlterar.setBoolean(14, obj.getAtividadeComplementar());
					sqlAlterar.setInt(15, obj.getNrAula());
					if(Uteis.isAtributoPreenchido(obj.getTurmaOrigem())){
						sqlAlterar.setInt(16, obj.getTurmaOrigem());
					}else{
						sqlAlterar.setNull(16, 0);
					}	
					sqlAlterar.setString(17, obj.getPraticaSupervisionada());
					sqlAlterar.setInt(18, obj.getCodigo().intValue());					
					return sqlAlterar;
				}
			});
			getFacadeFactory().getFrequenciaAulaFacade().persistirFrequenciaAulaVOs(obj, perfilPadraoProfessor, usuario);
			getFacadeFactory().getProfessorMinistrouAulaTurmaFacade().alterarProfessorMinistrouAulaTurma(obj.getTurma(), obj.getDisciplina().getCodigo(), obj.getProfessor().getCodigo(), obj.getAno(), obj.getSemestre(), obj.getData(), usuario);
			if (getFacadeFactory().getTurmaFacade().verificarTurmaAgrupada(obj.getTurma().getCodigo()) && !obj.getTurma().getSubturma()) {
				List<TurmaVO> listaTurmasAgrupadas = new ArrayList<TurmaVO>(0);
				listaTurmasAgrupadas = getFacadeFactory().getTurmaFacade().consultaRapidaTurmasNasQuaisTurmaParticipaDeAgrupamentoPossuemAlunos(obj.getTurma().getCodigo(), obj.getAno(), obj.getSemestre(), false, usuario);
				for (TurmaVO turma : listaTurmasAgrupadas) {
					RegistroAulaVO regNovo = null;
					DisciplinaVO disciplinaEquivalenteTurma = null;
					List<DisciplinaVO> disciplinaVOs = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaPorTurmaParaLancamentoNota(turma.getCodigo(), "codigo", obj.getDisciplina().getCodigo().toString(), false, usuario);
					if(Uteis.isAtributoPreenchido(disciplinaVOs)){
						disciplinaEquivalenteTurma = disciplinaVOs.get(0);
					}else{
						disciplinaEquivalenteTurma = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaEquivalenteTurmaAgrupadaPorTurma(turma.getCodigo(), obj.getTurma().getCodigo(), obj.getDisciplina().getCodigo(), usuario);
					}
					if (!disciplinaEquivalenteTurma.getCodigo().equals(0)) {
						if (!disciplinaEquivalenteTurma.getCodigo().equals(obj.getDisciplina().getCodigo())) {
							getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().alterarDisciplinaEquivalenteTurmaAgrupada(disciplinaEquivalenteTurma.getCodigo(), obj.getTurma().getCodigo(), obj.getProfessor().getCodigo(), obj.getDisciplina().getCodigo(), obj.getAno(), obj.getSemestre(), usuario);
						}
						regNovo = consultaRapidaPorTurmaDataDiaSemanaDisciplinaHorario(turma.getCodigo(), obj.getDataOriginal(), Uteis.getDiaSemanaData(obj.getDataOriginal()), disciplinaEquivalenteTurma.getCodigo(), obj.getSemestre(), obj.getAno(), obj.getHorario(), obj.getAtividadeComplementar(), false, usuario);
					} else {
						regNovo = consultaRapidaPorTurmaDataDiaSemanaDisciplinaHorario(turma.getCodigo(), obj.getDataOriginal(), Uteis.getDiaSemanaData(obj.getDataOriginal()), obj.getDisciplina().getCodigo(), obj.getSemestre(), obj.getAno(), obj.getHorario(), obj.getAtividadeComplementar(), false, usuario);
					}
					if (regNovo.getCodigo().equals(0)) {
						regNovo = (RegistroAulaVO) obj.clone();
						regNovo.setTurma(turma);
						if (Uteis.isAtributoPreenchido(disciplinaEquivalenteTurma)) {
							regNovo.setDisciplina(disciplinaEquivalenteTurma);
						}
						regNovo.setCodigo(0);
						regNovo.setNovoObj(true);
						regNovo.setFrequenciaAulaVOs(null);
						regNovo.setTurmaOrigem(obj.getTurma().getCodigo());
						regNovo.setAtividadeComplementar(obj.getAtividadeComplementar());
						regNovo.setFrequenciaAulaVOs(montarListaFrequenciaClonada(obj.getFrequenciaAulaVOs(), turma, regNovo));
						if(!regNovo.getFrequenciaAulaVOs().isEmpty()){
							incluir(regNovo, permiteRegistrarAulaFutura, 0, usuario, true);
						}
					} else {
						regNovo.setFrequenciaAulaVOs(null);
						regNovo.setConteudo(obj.getConteudo());
						regNovo.setPraticaSupervisionada(obj.getPraticaSupervisionada());
						regNovo.setData(obj.getData());
						regNovo.setDiaSemana(obj.getDiaSemana());
						regNovo.setCargaHoraria(obj.getCargaHoraria());
						regNovo.setTipoAula(obj.getTipoAula());
						regNovo.setAtividadeComplementar(obj.getAtividadeComplementar());
						regNovo.setTurmaOrigem(obj.getTurma().getCodigo());
						getFacadeFactory().getFrequenciaAulaFacade().excluirFrequenciaAulas(regNovo.getCodigo(), usuario);
						regNovo.setFrequenciaAulaVOs(montarListaFrequenciaClonada(obj.getFrequenciaAulaVOs(), turma, regNovo));
						if(!regNovo.getFrequenciaAulaVOs().isEmpty()){
							alterar(regNovo, permiteRegistrarAulaFutura, 0, usuario);
						}else{
							excluir(regNovo, usuario, Boolean.FALSE);
						}
					}					
					regNovo = null;
					disciplinaEquivalenteTurma =  null;
				}
			}
		} catch (Exception e) {
			if(e.getMessage().contains("frequenciaaula_pkey")) {
				String matricula = e.getMessage().substring(e.getMessage().indexOf(")=(")+3, e.getMessage().length());
				matricula = matricula.substring(0, matricula.indexOf(","));
				throw new Exception("A matrícula "+matricula+" possui dois histórico vinculado a disciplina na mesma turma, sendo assim, não é possivel gravar este registro, procure o departamento acadêmico.");
			}
			throw e;
		}
	}

	public List<FrequenciaAulaVO> montarListaFrequenciaClonada(List<FrequenciaAulaVO> listaFrequenciaAulaVO, TurmaVO turma, RegistroAulaVO regNovo) throws Exception {
		List<FrequenciaAulaVO> listaFrequenciaAulaVOClonada = new ArrayList<FrequenciaAulaVO>(0);
		q:
		for (FrequenciaAulaVO frequenciaAulaVO : listaFrequenciaAulaVO) {
			if (!frequenciaAulaVO.getFrequenciaOculta() 
					&& (!Uteis.isAtributoPreenchido(frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo()) 
					|| (Uteis.isAtributoPreenchido(frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo()) 
							&& (frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo().equals(turma.getCodigo()) 
									|| (turma.getSubturma() && turma.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA) 
											&& turma.getCodigo().equals(frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaTeorica().getCodigo()))
									|| (turma.getSubturma() && turma.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA) 
											&& turma.getCodigo().equals(frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaPratica().getCodigo())))))) {
				FrequenciaAulaVO frequenciaClonada = new FrequenciaAulaVO();
				frequenciaClonada = (FrequenciaAulaVO) frequenciaAulaVO.clone();
				frequenciaClonada.setNovoObj(true);
				frequenciaClonada.setRegistroAula(regNovo.getCodigo());
				frequenciaClonada.setTurma(turma.getIdentificadorTurma());
				for(FrequenciaAulaVO frequenciaAulaVO2: listaFrequenciaAulaVOClonada) {
					if(frequenciaAulaVO2.getMatricula().getMatricula().equals(frequenciaClonada.getMatricula().getMatricula())) {
						continue q;
					}
				}
				listaFrequenciaAulaVOClonada.add(frequenciaClonada);
			}
		}
		return listaFrequenciaAulaVOClonada;
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>RegistroAulaVO</code>. Sempre localiza o registro a ser excluído
	 * através da chave primária da entidade. Primeiramente verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>RegistroAulaVO</code> que será removido
	 *            no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(RegistroAulaVO obj, UsuarioVO usuario, Boolean atividadeComplementar) throws Exception {
		try {
			if(obj.getAtividadeComplementar().equals(atividadeComplementar)) {
				RegistroAula.excluir(getIdEntidade());
				realizarVerificacaoBloqueioLancamentoRegistroAula(obj, usuario);
				getFacadeFactory().getFrequenciaAulaFacade().excluirFrequenciaAulas(obj.getCodigo(), usuario);
				StringBuilder sql = new StringBuilder("DELETE FROM RegistroAula WHERE (codigo = ?) ");				
				sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
				getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });	
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(List<RegistroAulaVO> listaRegistrosAula, String acaoLog, UsuarioVO usuario, Boolean atividadeComplementar) throws Exception {
		try {
			RegistroAula.excluir(getIdEntidade());
			RegistroAulaVO objClone = null;
			for (RegistroAulaVO obj : listaRegistrosAula) {
				this.validarQuantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAulaVisaoProfessor(obj.getData(), obj.getTurma(), obj.getDisciplina(), obj.getProfessor(), obj.getAno(), obj.getSemestre(), usuario);
				if (!Uteis.isAtributoPreenchido(objClone)) {
					objClone = obj;
				}
				if (obj.getCodigo() != 0) {
					this.excluir(obj, usuario, atividadeComplementar);
					if (obj.getDisciplina().getNome().trim().isEmpty()) {
						obj.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimariaSemExcecao(obj.getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
					}
					if (obj.getTurma().getIdentificadorTurma().trim().isEmpty()) {
						obj.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
					}
					getFacadeFactory().getLogRegistroAulaFacade().registrarLogRegistroAula(obj, obj.getDisciplina().getNome(), acaoLog, usuario);
				}
			}
			/**
			 * Adicionada regra recursiva para deletar registros de aula de
			 * turmas origem vinculadas a turma agrupada levando em consideração
			 * disciplinas equivalentes
			 */
			if (getFacadeFactory().getTurmaFacade().verificarTurmaAgrupada(objClone.getTurma().getCodigo())) {
				List<TurmaVO> listaTurmasAgrupadas = getFacadeFactory().getTurmaFacade().consultaRapidaTurmasNasQuaisTurmaParticipaDeAgrupamento(objClone.getTurma().getCodigo(), false, usuario);
				for (TurmaVO turma : listaTurmasAgrupadas) {
					DisciplinaVO disciplinaEquivalenteTurma = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaEquivalenteTurmaAgrupadaPorTurma(turma.getCodigo(), objClone.getTurma().getCodigo(), objClone.getDisciplina().getCodigo(), usuario);
					List<RegistroAulaVO> registroAulaVOs = null;
					if (Uteis.isAtributoPreenchido(disciplinaEquivalenteTurma)) {
						registroAulaVOs = this.consultarRegistroAulaPorTurmaDisciplinaPeriodo(turma.getCodigo(), disciplinaEquivalenteTurma.getCodigo(), objClone.getData(), objClone.getData(), objClone.getAno(), objClone.getSemestre(), Uteis.NIVELMONTARDADOS_TODOS, usuario, atividadeComplementar);
					} else {
						registroAulaVOs = this.consultarRegistroAulaPorTurmaDisciplinaPeriodo(turma.getCodigo(), objClone.getDisciplina().getCodigo(), objClone.getData(), objClone.getData(), objClone.getAno(), objClone.getSemestre(), Uteis.NIVELMONTARDADOS_TODOS, usuario, atividadeComplementar);
					}
					if (Uteis.isAtributoPreenchido(registroAulaVOs)) {
						this.excluir(registroAulaVOs, "", usuario, atividadeComplementar);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void montarAlunosTurma(RegistroAulaVO registroAulaVO, UsuarioVO usuario) throws Exception {
		RegistroAulaVO.validarDadosRegistroAulaTurma(registroAulaVO);
		registroAulaVO.setFrequenciaAulaVOs(new ArrayList(0));

		List lista = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorCodigoTurmaDisciplinaSemestreAno(registroAulaVO.getTurma(), registroAulaVO.getDisciplina().getCodigo(), registroAulaVO.getAno(), registroAulaVO.getSemestre(), true, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		Iterator i = lista.iterator();
		Map<String, MatriculaVO> compara = new HashMap<String, MatriculaVO>(0);
		while (i.hasNext()) {
			MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
			MatriculaVO matriculavo = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), usuario.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, usuario);
			if (matriculavo.getSituacao().equalsIgnoreCase("AT")) {
				FrequenciaAulaVO frequenciaAulaVOs = new FrequenciaAulaVO();
				frequenciaAulaVOs.setMatricula(matriculavo);
				frequenciaAulaVOs.setPresente(Boolean.TRUE);
				frequenciaAulaVOs.setRegistroAula(registroAulaVO.getCodigo());
				frequenciaAulaVOs.setMatriculaPeriodoTurmaDisciplinaVO(obj);
				if (getFacadeFactory().getMatriculaPeriodoFacade().consultarSituacaoAcademicaMatriculaPeriodo(obj.getMatriculaPeriodo()).equals("FI")) {
					frequenciaAulaVOs.setEditavel(Boolean.FALSE);
				}
				if (!compara.containsKey(matriculavo.getMatricula()) && !matriculavo.getMatricula().equals("")) {
					registroAulaVO.getFrequenciaAulaVOs().add(frequenciaAulaVOs);
				}
				compara.put(matriculavo.getMatricula(), matriculavo);
			}
		}

		Ordenacao.ordenarLista(registroAulaVO.getFrequenciaAulaVOs(), "ordenacaoSemAcentuacaoNome");
	}	

	/**
	 * Método que utiliza o método original montarRegistrosAula sem passar o
	 * parametro controlarAbonoFaltaNovosRegistrosAula. Caso este parametro
	 * esteja definido como verdadeiro o método irá registrar para matrícula, se
	 * a mesma possui um abono de falta registrado implicando que não será
	 * possível registrar falta para o aluno.
	 * 
	 */
	public void montarRegistrosAula(List<String> listaHorario, HorarioProfessorDiaVO horarioProfessorDia, List<RegistroAulaVO> listaRegistroAulaVO, DisciplinaVO disciplina, PessoaVO professor, String ano, String semestre, Integer cargaHoraria, UsuarioVO responsavelRegistroAula, String tipoAula, TurmaVO turma, String conteudo, boolean filtroVisaoProfessor, Boolean isLancadoRegistro, List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplina, UsuarioVO usuario, boolean trazerAlunosPendenteFinanceiramente, boolean trazerAlunosTransferenciaMatriz, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception {
		montarRegistrosAula(listaHorario, horarioProfessorDia, listaRegistroAulaVO, disciplina, professor, ano, semestre, cargaHoraria, responsavelRegistroAula, tipoAula, turma, conteudo, filtroVisaoProfessor, listaMatriculaPeriodoTurmaDisciplina, usuario, false, isLancadoRegistro, trazerAlunosPendenteFinanceiramente, trazerAlunosTransferenciaMatriz, permitirRealizarLancamentoAlunosPreMatriculados);
	}

	/***
	 * Método responsável por montar um registro de aula, objetivo deste método
	 * está no preenchimento da lista recebida como parâmetro
	 * listaRegistroAulaVO
	 * 
	 * @param controlarAbonoFaltaNovosRegistrosAula
	 *            - caso seja marcado como true o sistema irá buscar por
	 *            abonosdefaltaregistrados para o aluno, na data de registro. Em
	 *            caso de positivo o sistema irá indicar por meio atributo
	 *            abonado ou justificado (atributos de FrequenciaAulaVO) que uma
	 *            falta para o aluno não poderá ser registrada. também será
	 *            apresentado no campo descricaoMotivoAbonoJustificativa
	 *            (atributo transiente de FrequenciaAulaVO) os detalhes do abono
	 *            de falta (período, justificativa)
	 * 
	 * @throws Exception
	 */
	public void montarRegistrosAula(List<String> listaHorario, HorarioProfessorDiaVO horarioProfessorDia, List<RegistroAulaVO> listaRegistroAulaVO, DisciplinaVO disciplina, PessoaVO professor, String ano, String semestre, Integer cargaHoraria, UsuarioVO responsavelRegistroAula, String tipoAula, TurmaVO turma, String conteudo, boolean filtroVisaoProfessor, List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplina, UsuarioVO usuario, Boolean controlarAbonoFaltaNovosRegistrosAula, Boolean isLancadoRegistro, boolean trazerAlunosPendenteFinanceiramente, boolean trazerAlunosTransferenciaMatriz, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception {
		if (!Uteis.isAtributoPreenchido(listaMatriculaPeriodoTurmaDisciplina) && !Uteis.isAtributoPreenchido(listaRegistroAulaVO)) {			
			listaMatriculaPeriodoTurmaDisciplina = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorCodigoTurmaDisciplinaSemestreAnoFiltroVisaoProfessor(turma, disciplina.getCodigo(), ano, semestre, "", true, filtroVisaoProfessor, trazerAlunosPendenteFinanceiramente, "", "", "", null, "", "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario, null, null, null, trazerAlunosTransferenciaMatriz, permitirRealizarLancamentoAlunosPreMatriculados);
		}
		for (HorarioProfessorDiaItemVO aula : horarioProfessorDia.getHorarioProfessorDiaItemVOs()) {
			//Boolean possuiTurmaAgrupada = getFacadeFactory().getTurmaAgrupadaFacade().consultarPossuiTurmaAgrupada(aula.getTurmaVO().getCodigo(), turma.getCodigo().intValue());
			if (aula.getDisciplinaVO().getCodigo().intValue() == disciplina.getCodigo().intValue() && aula.getTurmaVO().getCodigo().equals(turma.getCodigo()) && aula.getHorarioTurmaDiaItemVO().getHorarioTurmaDiaVO().getHorarioTurma().getAnoVigente().equals(ano) && aula.getHorarioTurmaDiaItemVO().getHorarioTurmaDiaVO().getHorarioTurma().getSemestreVigente().equals(semestre)) {
				if (!consultarRegistroAulaExistente(aula.getNrAula().toString(), turma.getCodigo(), disciplina.getCodigo(), Uteis.isAtributoPreenchido(aula.getUsuarioLiberacaoChoqueHorario()) ? professor.getCodigo() : null, ano, semestre, horarioProfessorDia.getData(), turma.getCurso().getLiberarRegistroAulaEntrePeriodo(), usuario)) {
					if (existeRegistroAulaCarregadoListaDiaAulaNaoRegistrada(aula.getNrAula().toString(), turma.getCodigo(), disciplina.getCodigo(), professor.getCodigo(), ano, semestre, horarioProfessorDia.getData(), listaRegistroAulaVO, usuario)) {
						return;
					}
					RegistroAulaVO registroAula = new RegistroAulaVO();
					registroAula.setAno(ano);
					registroAula.setSemestre(semestre);
					if (turma.getTurno().getTipoHorario().equals(TipoHorarioEnum.HORARIO_FIXO)) {
						registroAula.setCargaHoraria(turma.getTurno().getDuracaoAula());
					} else {
						registroAula.setCargaHoraria(aula.getDuracaoAula());
					}
					registroAula.setResponsavelRegistroAula(responsavelRegistroAula);
					registroAula.setDisciplina(disciplina);
					registroAula.setTipoAula(tipoAula);
					registroAula.setProfessor(professor);
					registroAula.setDataRegistroAula(new Date());
					registroAula.setData(horarioProfessorDia.getData());
					registroAula.setTurma(turma);
					registroAula.setConteudo(conteudo);
					registroAula.setHorario(aula.getNrAula() + "ª Aula (" + aula.getHorarioInicio() + " até " + aula.getHorarioTermino() + ")");
					registroAula.setNrAula(aula.getNrAula());
					registroAula.montarDiaSemanaAula();
					
					for(RegistroAulaVO registroAulaVO:listaRegistroAulaVO){
						if(!registroAulaVO.getFrequenciaAulaVOs().isEmpty()){
							for(FrequenciaAulaVO frequenciaAulaVO: registroAulaVO.getFrequenciaAulaVOs()){
								FrequenciaAulaVO clone = (FrequenciaAulaVO)frequenciaAulaVO.clone();
								clone.setNovoObj(true);
								clone.setRegistroAula(0);
								registroAula.getFrequenciaAulaVOs().add(clone);
							}
							break;
						}
					}
					// for (MatriculaPeriodoTurmaDisciplinaVO
					// matriculaPeriodoTurmaDisciplina :
					// listaMatriculaPeriodoTurmaDisciplina) {
					// MatriculaPeriodoVO matriculaPeriodoVO = new
					// MatriculaPeriodoVO();
					// matriculaPeriodoVO.setCodigo(matriculaPeriodoTurmaDisciplina.getMatriculaPeriodo());
					// matriculaPeriodoVO.getMatriculaVO().setMatricula(matriculaPeriodoTurmaDisciplina.getMatricula());
					// getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(matriculaPeriodoVO,
					// null, usuario);
					//
					// FrequenciaAulaVO frequenciaAulaVOs = new
					// FrequenciaAulaVO();
					//
					// frequenciaAulaVOs.setMatricula(matriculaPeriodoVO.getMatriculaVO());
					// frequenciaAulaVOs.getHistoricoVO().setRealizouTransferenciaTurno(getFacadeFactory().getTurnoFacade().consultarTransferenciaTurnoPorMatricula(matriculaPeriodoVO.getMatriculaVO().getMatricula(),
					// usuario));
					// frequenciaAulaVOs.setPresente(Boolean.TRUE);
					// frequenciaAulaVOs.setRegistroAula(registroAula.getCodigo());
					// frequenciaAulaVOs.getHistoricoVO().setMatricula(matriculaPeriodoVO.getMatriculaVO());
					// frequenciaAulaVOs.getHistoricoVO().getMatriculaPeriodo().setSituacao(matriculaPeriodoVO.getSituacao());
					// if
					// (matriculaPeriodoVO.getSituacaoMatriculaPeriodo().equals("FI"))
					// {
					//
					// frequenciaAulaVOs.setEditavel(Boolean.FALSE);
					// }
					// registroAula.getFrequenciaAulaVOs().add(frequenciaAulaVOs);
					//
					// }

					// if (controlarAbonoFaltaNovosRegistrosAula) {
					// // neste caso temos que consultar possíveis abonos de
					// // falta registrados para estes alunos
					// getFacadeFactory().getAbonoFaltaFacade().consultarECarregarAbonoFaltaParaFrequenciasDoRegistroAula(registroAula);
					// }
					listaRegistroAulaVO.add(registroAula);
				}
			}
		}
		turma.getCurso().setConfiguracaoAcademico(getAplicacaoControle().carregarDadosConfiguracaoAcademica(turma.getCurso().getConfiguracaoAcademico().getCodigo()));
		adicionarAlunoRegistroAulaPorMatriculaPeriodoTurmaDisciplinaVO(listaMatriculaPeriodoTurmaDisciplina, listaRegistroAulaVO, turma.getCurso().getConfiguracaoAcademico().getBloquearRegistroAulaAnteriorDataMatricula(), usuario, turma.getCurso().getConfiguracaoAcademico().getRegistrarComoFaltaAulasRealizadasAposDataMatricula());
//		if (controlarAbonoFaltaNovosRegistrosAula) {
			for (RegistroAulaVO registroAulaVO : listaRegistroAulaVO) {
				getFacadeFactory().getAbonoFaltaFacade().consultarECarregarAbonoFaltaParaFrequenciasDoRegistroAula(registroAulaVO);
				if (!Uteis.isAtributoPreenchido(registroAulaVO.getProfessor())) {
					horarioProfessorDia.setHorarioProfessor(getFacadeFactory().getHorarioProfessorFacade().consultaRapidaPorChavePrimaria(horarioProfessorDia.getHorarioProfessor().getCodigo(), usuario));
					registroAulaVO.setProfessor(horarioProfessorDia.getHorarioProfessor().getProfessor());
				}
			}
//		}
		}

	public void adicionarAlunoRegistroAulaPorMatriculaPeriodoTurmaDisciplinaVO(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, List<RegistroAulaVO> listaRegistroAulaVO, Boolean bloquearRegistroAulaAnteriorDataMatricula, UsuarioVO usuarioVO, Boolean registrarComoFaltaAulasRealizadasAposDataMatricula) throws Exception {
//		List<MatriculaPeriodoVO> matriculaPeriodoVOs = new ArrayList<MatriculaPeriodoVO>(0);
		Map<String, Boolean> mapTranferenciaTurno = new HashMap<String, Boolean>(0);
		// Este vinculado os alunos que ainda não foram registrado aula para o
		// dia selecionado, sendo que se for um novo registro de aulas todos os
		// alunos serão adicionados,
		// caso seja uma edição do registro de aula a lista
		// matriculaPeriodoTurmaDisciplinaVOs só terá os alunos que foram
		// matriculados posteriormente ao registro de aula.
		for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : matriculaPeriodoTurmaDisciplinaVOs) {
			//MatriculaPeriodoVO matriculaPeriodoVO = new MatriculaPeriodoVO();
			//matriculaPeriodoVO.setCodigo(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodo());
			//matriculaPeriodoVO.getMatriculaVO().setMatricula(matriculaPeriodoTurmaDisciplinaVO.getMatricula());
			//getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(matriculaPeriodoVO, null, usuarioVO);
			mapTranferenciaTurno.put(matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getMatricula(), getFacadeFactory().getTurnoFacade().consultarTransferenciaTurnoPorMatricula(matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getMatricula(), usuarioVO));
//			matriculaPeriodoVOs.add(matriculaPeriodoVO);
			//matriculaPeriodoTurmaDisciplinaVO.setMatriculaPeriodoObjetoVO(matriculaPeriodoVO);
		}
		// Este é utilizado na edição de um registro de aula a fim de mapear se
		// foi adicionado uma aula nova na programacao de aula, ou seja,
		// se estivesse programado aula para a 1ª e 2ª aula e realizado o
		// registro da aula e depois alterado o registro de aula adicionado mais
		// um horário EX: 3º então
		// os alunos que registram aula somente para a 1ª e 2ª aula deve ser
		// vinculado também a 3ª aula, portanto este for evita uma nova consulta
		// no banco de dados a fim de melhoria de performance
		for (RegistroAulaVO registroAulaVO : listaRegistroAulaVO) {
			if (registroAulaVO.getCodigo() > 0) {
				q: for (FrequenciaAulaVO frequenciaAulaVO : registroAulaVO.getFrequenciaAulaVOs()) {
					for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : matriculaPeriodoTurmaDisciplinaVOs) {
						if (frequenciaAulaVO.getMatricula().getMatricula().equals(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodoObjetoVO().getMatricula())) {
							frequenciaAulaVO.setMatriculaPeriodoTurmaDisciplinaVO(matriculaPeriodoTurmaDisciplinaVO);
							continue q;
						}
					}
					mapTranferenciaTurno.put(frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().getMatricula(), getFacadeFactory().getTurnoFacade().consultarTransferenciaTurnoPorMatricula(frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().getMatriculaVO().getMatricula(), usuarioVO));
					frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().setMatriculaVO(frequenciaAulaVO.getMatricula());
					if (bloquearRegistroAulaAnteriorDataMatricula || registrarComoFaltaAulasRealizadasAposDataMatricula) {
//						if (frequenciaAulaVOs.getHistoricoVO().getMatriculaPeriodo().getData().after(registroAulaVO.getData())) {
						Date dataRegistro = frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().getData();
						if (frequenciaAulaVO.getHistoricoVO().getDataRegistro() != null) {
							dataRegistro = frequenciaAulaVO.getHistoricoVO().getDataRegistro();
						}
						if (dataRegistro.after(registroAulaVO.getData())) {
							frequenciaAulaVO.setBloqueadoDevidoDataMatricula(true);						
							frequenciaAulaVO.setFrequenciaOculta(true);
							frequenciaAulaVO.setPresente(false);
							frequenciaAulaVO.getHistoricoVO().getConfiguracaoAcademico().setRegistrarComoFaltaAulasRealizadasAposDataMatricula(registrarComoFaltaAulasRealizadasAposDataMatricula);
						}
					}
//					matriculaPeriodoVOs.add(frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo());
				}
			}
		}
		// Este cria o registro da frequencia de aula para os alunos que ainda
		// não estvam vinculados ao registro de aula
		for (RegistroAulaVO registroAulaVO : listaRegistroAulaVO) {
			q: for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : matriculaPeriodoTurmaDisciplinaVOs) {
				for (FrequenciaAulaVO frequenciaAulaVO : registroAulaVO.getFrequenciaAulaVOs()) {
					if (frequenciaAulaVO.getMatricula().getMatricula().equals(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodoObjetoVO().getMatriculaVO().getMatricula())) {
						frequenciaAulaVO.setMatriculaPeriodoTurmaDisciplinaVO(matriculaPeriodoTurmaDisciplinaVO);
						continue q;
					}
				}
				FrequenciaAulaVO frequenciaAulaVOs = new FrequenciaAulaVO();
				frequenciaAulaVOs.setMatricula(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodoObjetoVO().getMatriculaVO());
				frequenciaAulaVOs.getHistoricoVO().setRealizouTransferenciaTurno(mapTranferenciaTurno.get(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodoObjetoVO().getMatriculaVO().getMatricula()));
				if(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.ATIVA.getValor()) 
						|| matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.FINALIZADA.getValor())
						|| matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.FORMADO.getValor())
						|| matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA.getValor())){
							frequenciaAulaVOs.setPresente(Boolean.TRUE);
						}else{
							frequenciaAulaVOs.setPresente(Boolean.FALSE);
						}
				frequenciaAulaVOs.setNovoObj(true);
				frequenciaAulaVOs.setRegistroAula(registroAulaVO.getCodigo());
				frequenciaAulaVOs.setDataRegistroAula(registroAulaVO.getData());
				frequenciaAulaVOs.getHistoricoVO().setMatricula(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodoObjetoVO().getMatriculaVO());
				frequenciaAulaVOs.getHistoricoVO().setMatriculaPeriodo(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodoObjetoVO());
				frequenciaAulaVOs.setMatriculaPeriodoTurmaDisciplinaVO(matriculaPeriodoTurmaDisciplinaVO);
				frequenciaAulaVOs.getHistoricoVO().setDataRegistro(matriculaPeriodoTurmaDisciplinaVO.getDataRegistroHistorico());
				if (matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo().equals("FI")) {
					frequenciaAulaVOs.setEditavel(Boolean.FALSE);
				}
				if (bloquearRegistroAulaAnteriorDataMatricula || registrarComoFaltaAulasRealizadasAposDataMatricula) {
//					if (frequenciaAulaVOs.getHistoricoVO().getMatriculaPeriodo().getData().after(registroAulaVO.getData())) {
					Date dataRegistro = frequenciaAulaVOs.getHistoricoVO().getMatriculaPeriodo().getData();
					if (frequenciaAulaVOs.getHistoricoVO().getDataRegistro() != null) {
						dataRegistro = frequenciaAulaVOs.getHistoricoVO().getDataRegistro();
					}
					if (dataRegistro.after(registroAulaVO.getData())) {
						frequenciaAulaVOs.setBloqueadoDevidoDataMatricula(true);						
						frequenciaAulaVOs.setFrequenciaOculta(true);
						frequenciaAulaVOs.setPresente(false);
						frequenciaAulaVOs.getHistoricoVO().getConfiguracaoAcademico().setRegistrarComoFaltaAulasRealizadasAposDataMatricula(registrarComoFaltaAulasRealizadasAposDataMatricula);
					}
				}
				registroAulaVO.getFrequenciaAulaVOs().add(frequenciaAulaVOs);
			}
			Ordenacao.ordenarLista(registroAulaVO.getFrequenciaAulaVOs(), "ordenacaoSemAcentuacaoNome");
		}
	}

	public Boolean existeRegistroAulaCarregadoListaDiaAulaNaoRegistrada(String nrAula, Integer turma, Integer disciplina, Integer professor, String ano, String semestre, Date data, List<RegistroAulaVO> listaRegistroAulaVOs, UsuarioVO usuarioVO) {
		for (RegistroAulaVO registroAulaVO : listaRegistroAulaVOs) {
			/**
			 * Adicionado substring para trazer o número da aula para que seja
			 * possível comparar apenas seu número, devido a estar duplicando
			 * horários de aulas
			 */
			if (registroAulaVO.getNrAula().toString().equals(nrAula) && registroAulaVO.getTurma().getCodigo().equals(turma) && registroAulaVO.getDisciplina().getCodigo().equals(disciplina) && registroAulaVO.getProfessor().getCodigo().equals(professor) && registroAulaVO.getAno().equals(ano) && registroAulaVO.getSemestre().equals(semestre) && registroAulaVO.getData().equals(data)) {
				return true;
			}
		}
		return false;
	}

	public void adicionarMatriculasRegistrosAula(List<String> listaHorario, HorarioProfessorDiaVO horarioProfessorDia, List<RegistroAulaVO> listaRegistroAulaVO, DisciplinaVO disciplina, PessoaVO professor, String ano, String semestre, Integer cargaHoraria, UsuarioVO responsavelRegistroAula, String tipoAula, TurmaVO turma, String conteudo, boolean filtroVisaoProfessor, List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplina, UsuarioVO usuario) throws Exception {
		for (RegistroAulaVO registroAula : listaRegistroAulaVO) {
			Iterator alunos = listaMatriculaPeriodoTurmaDisciplina.iterator();
			while (alunos.hasNext()) {
				MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplina = (MatriculaPeriodoTurmaDisciplinaVO) alunos.next();
				MatriculaVO matriculavo = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matriculaPeriodoTurmaDisciplina.getMatricula(), 0, NivelMontarDados.BASICO, usuario);
				String situacao = getFacadeFactory().getMatriculaPeriodoFacade().consultarSituacaoFinanceiraMatriculaPeriodo(matriculaPeriodoTurmaDisciplina.getMatriculaPeriodo());

				// if ((matriculavo.getSituacao().equalsIgnoreCase("AT") &&
				// !situacao.equals("PF") && filtroVisaoProfessor) ||
				// (!filtroVisaoProfessor)) {
				FrequenciaAulaVO frequenciaAulaVOs = new FrequenciaAulaVO();

				frequenciaAulaVOs.setMatricula(matriculavo);
				frequenciaAulaVOs.setPresente(Boolean.FALSE);
				frequenciaAulaVOs.setRegistroAula(registroAula.getCodigo());
				frequenciaAulaVOs.getHistoricoVO().setMatricula(matriculavo);
				frequenciaAulaVOs.setMatriculaPeriodoTurmaDisciplinaVO(matriculaPeriodoTurmaDisciplina);
				frequenciaAulaVOs.getHistoricoVO().setRealizouTransferenciaTurno(getFacadeFactory().getTurnoFacade().consultarTransferenciaTurnoPorMatricula(matriculavo.getMatricula(), usuario));
				frequenciaAulaVOs.getHistoricoVO().getMatriculaPeriodo().setSituacao(situacao);
				if (getFacadeFactory().getMatriculaPeriodoFacade().consultarSituacaoAcademicaMatriculaPeriodo(matriculaPeriodoTurmaDisciplina.getMatriculaPeriodo()).equals("FI")) {
					frequenciaAulaVOs.setEditavel(Boolean.FALSE);
				}
				registroAula.getFrequenciaAulaVOs().add(frequenciaAulaVOs);
				// }
			}
			Ordenacao.ordenarLista(registroAula.getFrequenciaAulaVOs(), "ordenacaoSemAcentuacaoNome");
		}
	}

	/***
	 * Método responsável por montar uma lista de registros de aula, objetivo
	 * deste método está no preenchimento da lista recebida como parâmetro
	 * listaRegistroAulaVO
	 * 
	 * @param horarioProfessorDia
	 * @param listaRegistroAulaVO
	 * @param disciplina
	 * @param professor
	 * @param ano
	 * @param semestre
	 * @param cargaHoraria
	 * @param responsavelRegistroAula
	 * @param tipoAula
	 * @param turma
	 * @param conteudo
	 * @param listaMatriculaPeriodoTurmaDisciplina
	 * 
	 * @throws Exception
	 */
	public List<RegistroAulaVO> consultarPorHorarioTurmaDia(HorarioProfessorDiaVO horarioProfessorDia, Integer codigoTurma, Integer codigoDisciplina, Integer codigoProfessor, String ano, String semestre, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<RegistroAulaVO> listaRegistroAula = new ArrayList<RegistroAulaVO>(0);
		StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT registroAula.dataregistroaula, registroAula.responsavelregistroaula, registroaula.turma, registroaula.conteudo, registroaula.praticaSupervisionada, ");
		sqlStr.append("registroAula.cargahoraria, registroaula.data, registroAula.codigo, registroaula.disciplina, registroaula.tipoaula, registroaula.diasemana, ");
		sqlStr.append("registroaula.professor, registroaula.semestre, registroaula.ano, registroaula.horario, registroaula.nraula ");
		sqlStr.append("FROM registroaula ");
		sqlStr.append("left JOIN frequenciaaula ON frequenciaaula.registroaula = registroaula.codigo ");
		sqlStr.append("WHERE registroaula.disciplina = ").append(codigoDisciplina);
		sqlStr.append(" AND registroaula.turma = ").append(codigoTurma);
		sqlStr.append(" AND registroaula.professor = ").append(codigoProfessor);
		sqlStr.append(" AND registroaula.semestre = '").append(semestre).append("' ");
		sqlStr.append("AND registroaula.ano = '").append(ano).append("' ");
		sqlStr.append("AND registroaula.data = '").append(horarioProfessorDia.getData()).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			RegistroAulaVO registroAula = new RegistroAulaVO();
			registroAula = montarDadosBasico(tabelaResultado, usuario);
			listaRegistroAula.add(registroAula);
		}
		return listaRegistroAula;
	}

	public List<RegistroAulaVO> consultaRapidaPorHorarioTurmaDia(HorarioProfessorDiaVO horarioProfessorDia, TurmaVO turmaVO, Integer codigoDisciplina, Integer codigoProfessor, String ano, String semestre, Boolean liberarRegistroAulaEntrePeriodo, UsuarioVO usuario, boolean filtroVisaoProfessor, boolean trazerAlunoPendenteFinanceiro, boolean trazerAlunoTransferenciaMatriz) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT registroAula.dataregistroaula as \"registroAula.dataregistroaula\", registroAula.responsavelregistroaula as ");
		sqlStr.append("\"registroAula.responsavelregistroaula\", registroaula.turma as \"registroaula.turma\", registroaula.conteudo as \"registroaula.conteudo\", registroaula.praticasupervisionada as \"registroaula.praticasupervisionada\", ");
		sqlStr.append("registroAula.cargahoraria as \"registroAula.cargahoraria\", registroaula.data as \"registroaula.data\", registroAula.codigo as \"registroAula.codigo\", ");
		sqlStr.append("registroaula.disciplina as \"registroaula.disciplina\", registroaula.tipoaula as \"registroaula.tipoaula\", registroaula.nrAula as \"registroaula.nrAula\", registroaula.diasemana as ");
		sqlStr.append("\"registroaula.diasemana\", registroaula.professor as \"registroaula.professor\", registroaula.semestre as \"registroaula.semestre\", registroAula.atividadeComplementar AS \"registroAula.atividadeComplementar\", ");
		sqlStr.append("registroaula.ano as \"registroaula.ano\", registroaula.horario as \"registroaula.horario\", frequenciaaula.registroaula as \"frequenciaaula.registroaula\", tipojustificativafalta.sigla, tipojustificativafalta.descricao as \"tipojustificativafalta.descricao\", ");
		sqlStr.append("frequenciaaula.presente as \"frequenciaaula.presente\", frequenciaaula.abonado AS \"frequenciaaula.abonado\", frequenciaaula.justificado AS \"frequenciaaula.justificado\", frequenciaaula.matricula as \"frequenciaaula.matricula\", pessoa.nome as \"pessoa.nome\", ");
		sqlStr.append("matriculaperiodo.codigo as \"matriculaperiodo.codigo\", matriculaperiodo.situacaomatriculaperiodo as \"matriculaperiodo.situacaomatriculaperiodo\", ");
		sqlStr.append("matriculaperiodo.situacao as \"matriculaperiodo.situacao\", matriculaperiodo.datafechamentomatriculaperiodo as \"matriculaperiodo.datafechamentomatriculaperiodo\",   matricula.data as \"matricula.data\", matricula.situacao as \"matricula.situacao\", ");
		sqlStr.append("matricula.situacaoFinanceira as \"matricula.situacaoFinanceira\", matricula.dataInicioCurso as \"matricula.dataInicioCurso\", ");
		sqlStr.append("matricula.dataConclusaoCurso as \"matricula.dataConclusaoCurso\", matricula.updated as \"matricula.updated\", matricula.formacaoAcademica as ");
		sqlStr.append("\"matricula.formacaoAcademica\", matricula.autorizacaoCurso as \"matricula.autorizacaoCurso\", matricula.consultor as \"matricula.consultor\", ");
		sqlStr.append("matricula.formaIngresso as \"matricula.formaIngresso\", matricula.tipoMatricula as \"matricula.tipoMatricula\", matricula.nomeMonografia as ");
		sqlStr.append("\"matricula.nomeMonografia\", matricula.notaMonografia as \"matricula.notaMonografia\", Pessoa.nome as \"Pessoa.nome\", ");
		sqlStr.append("Pessoa.codigo as \"Pessoa.codigo\", Pessoa.cpf as \"Pessoa.cpf\", Pessoa.rg as \"Pessoa.rg\", Pessoa.dataNasc as \"Pessoa.dataNasc\", ");
		sqlStr.append("Pessoa.arquivoImagem as codArquivo, Curso.nome as \"Curso.nome\", Curso.codigo as \"Curso.codigo\", Curso.nivelEducacional as \"Curso.nivelEducacional\", ");
		sqlStr.append("Curso.periodicidade as \"Curso.periodicidade\", Curso.configuracaoAcademico as \"Curso.configuracaoAcademico\", UnidadeEnsino.codigo as \"UnidadeEnsino.codigo\", UnidadeEnsino.nome as \"UnidadeEnsino.nome\", ");
		sqlStr.append("UnidadeEnsino.cidade as \"UnidadeEnsino.cidade\", Turno.codigo as \"Turno.codigo\", Turno.nome as \"Turno.nome\", ");
		sqlStr.append(" CASE WHEN (select distinct count(distinct uec.turno) from matriculaperiodo mp");
		sqlStr.append(" inner join unidadeensinocurso uec on uec.codigo = mp.unidadeensinocurso ");
		sqlStr.append(" and mp.matricula = matricula.matricula  ");
		sqlStr.append(" group by matricula ");
		sqlStr.append(" ) > 1 then true ELSE false END AS realizoutransferenciaTurno, ");
		sqlStr.append(" unidadeensinoturma.codigo as \"unidadeensinoturma.codigo\", unidadeensinoturma.nome as \"unidadeensinoturma.nome\", ");
		sqlStr.append(" turma.codigo as \"turma.codigo\", turma.identificadorTurma as \"turma.identificadorTurma\", ");
		sqlStr.append(" disciplina.codigo as \"disciplina.codigo\", disciplina.nome as \"disciplina.nome\",  ");
		sqlStr.append(" mptd.codigo as \"matriculaperiodoturmadisciplina.codigo\", mptd.turma as \"matriculaperiodoturmadisciplina.turma\", ");
		sqlStr.append(" mptd.turmaTeorica as \"matriculaperiodoturmadisciplina.turmaTeorica\", mptd.turmaPratica as \"matriculaperiodoturmadisciplina.turmaPratica\", ");
		
		sqlStr.append(" mptd.modalidadeDisciplina as \"matriculaPeriodoTurmaDisciplina.modalidadeDisciplina\", ");
		sqlStr.append(" mptd.semestre as \"matriculaPeriodoTurmaDisciplina.semestre\", mptd.ano as \"matriculaPeriodoTurmaDisciplina.ano\", ");
		sqlStr.append(" mptd.inclusaoForaPrazo as \"matriculaPeriodoTurmaDisciplina.inclusaoForaPrazo\", ");
		
		sqlStr.append(" matriculaperiodo.alunoTransferidoUnidade as \"matriculaperiodo.alunoTransferidoUnidade\",   ");
		sqlStr.append(" matriculaperiodo.unidadeEnsinoCurso as \"matriculaperiodo.unidadeEnsinoCurso\", matriculaperiodo.processoMatricula as \"matriculaperiodo.processoMatricula\", ");
		sqlStr.append(" matriculaperiodo.gradecurricular as \"MatriculaPeriodo.gradecurricular\", matriculaperiodo.turma as \"MatriculaPeriodo.turma\", ");
		sqlStr.append(" matricula.gradeCurricularAtual as \"matricula.gradeCurricularAtual\", matricula.matriculaSuspensa as \"matricula.matriculaSuspensa\", ");
		
		sqlStr.append(" turma.semestral as \"turma.semestral\", turma.anual as \"turma.anual\", ");
		sqlStr.append(" turma.turmaagrupada as \"turma.turmaagrupada\", turma.sala as \"turma.sala\", turma.gradeCurricular as \"turma.gradeCurricular\", ");
		
		sqlStr.append(" mptd.disciplina as \"matriculaperiodoturmadisciplina.disciplina\", historico.codigo as \"historico.codigo\", historico.dataRegistro as \"historico.dataRegistro\", historico.configuracaoacademico as \"historico.configuracaoacademico\", matriculaperiodo.data as \"matriculaperiodo.data\" ");
		sqlStr.append("FROM registroaula ");
		sqlStr.append(" INNER JOIN turma                               ON registroaula.turma          = turma.codigo ");
		sqlStr.append(" INNER JOIN unidadeensino as unidadeensinoturma ON turma.unidadeensino         = unidadeensinoturma.codigo ");
		sqlStr.append(" INNER JOIN disciplina                          ON registroaula.disciplina     = disciplina.codigo ");
		sqlStr.append(" INNER JOIN frequenciaaula                      ON frequenciaaula.registroaula = registroaula.codigo ");
		sqlStr.append(" INNER JOIN matricula                           ON matricula.matricula         =  frequenciaaula.matricula ");
		sqlStr.append(" INNER JOIN curso                               ON Matricula.curso             = curso.codigo ");
		sqlStr.append(" INNER JOIN matriculaperiodo                    ON matriculaperiodo.matricula  = matricula.matricula ");    
		sqlStr.append(" AND ( ((curso.periodicidade = 'IN' "); 
		if (filtroVisaoProfessor) {
			sqlStr.append(" AND matriculaperiodo.situacao NOT IN ('CA', 'TR') AND matriculaperiodo.situacaoMatriculaPeriodo NOT IN ('PR','TR', 'PC') ");
			if (!trazerAlunoPendenteFinanceiro) {
				sqlStr.append(" AND matriculaperiodo.situacao <> 'PF' ");
			}
		} else {
			sqlStr.append(" AND matriculaperiodo.situacaoMatriculaPeriodo NOT IN ('PC') ");
		}
		sqlStr.append(") OR curso.periodicidade IN ('AN', 'SE')) and (matriculaperiodo.codigo =  (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula ");
		if (filtroVisaoProfessor) {
			sqlStr.append(" AND mp.situacao NOT IN ('CA', 'TR') AND mp.situacaoMatriculaPeriodo NOT IN ('PR','TR', 'PC') ");
			if (!trazerAlunoPendenteFinanceiro) {
				sqlStr.append(" AND mp.situacao <> 'PF' ");
			}
		} else {
			sqlStr.append(" AND mp.situacaoMatriculaPeriodo NOT IN ('PC') ");
		}
		if (turmaVO.getSemestral()) {
			sqlStr.append(" AND mp.semestre = '").append(semestre).append("' ");
			sqlStr.append(" AND mp.ano = '").append(ano).append("' ");
		} else if (turmaVO.getAnual()) {
			sqlStr.append(" AND mp.ano = '").append(ano).append("' ");
		}
		sqlStr.append(" ORDER BY mp.ano||'/'|| mp.semestre DESC, CASE WHEN mp.situacaoMatriculaPeriodo IN ('AT', 'PR', 'FI', 'FO') THEN 1 ELSE 2 END, mp.codigo DESC LIMIT 1))) ");
		if (turmaVO.getSubturma()) {
			if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
				sqlStr.append("INNER JOIN matriculaperiodoturmadisciplina mptd on mptd.matriculaperiodo = matriculaperiodo.codigo and mptd.turmaTeorica = turma.codigo ");
			} else if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
				sqlStr.append("INNER JOIN matriculaperiodoturmadisciplina mptd on mptd.matriculaperiodo = matriculaperiodo.codigo and mptd.turmaPratica = turma.codigo  ");
			} else {
				sqlStr.append("INNER JOIN matriculaperiodoturmadisciplina mptd on mptd.matriculaperiodo = matriculaperiodo.codigo and mptd.turma = turma.codigo and mptd.turmapratica is null and mptd.turmateorica is null ");
			}
		} else {
			if (!turmaVO.getTurmaAgrupada()) {
				sqlStr.append("INNER JOIN matriculaperiodoturmadisciplina mptd on mptd.matriculaperiodo = matriculaperiodo.codigo and mptd.turma = turma.codigo and mptd.turmapratica is null and mptd.turmateorica is null ");
			} else {
				sqlStr.append("INNER JOIN matriculaperiodoturmadisciplina mptd on mptd.matriculaperiodo = matriculaperiodo.codigo AND (mptd.disciplinacomposta is null  or mptd.disciplinacomposta = false) and ((mptd.turma = turma.codigo or mptd.turma in (select turma from turmaAgrupada where turmaOrigem =  ").append(turmaVO.getCodigo()).append("))");
				sqlStr.append("or (mptd.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
				sqlStr.append("or (mptd.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
				sqlStr.append(") ");
			}
		}
		sqlStr.append(" INNER JOIN pessoa on matricula.aluno                = pessoa.codigo ");
		sqlStr.append(" INNER JOIN UnidadeEnsino ON Matricula.unidadeEnsino = unidadeEnsino.codigo ");		
		sqlStr.append(" INNER JOIN Turno ON Matricula.turno                 = turno.codigo ");
		sqlStr.append(" INNER join historico on historico.matriculaperiodoturmadisciplina = mptd.codigo ");
		sqlStr.append(" left join disciplinaabono on registroaula.codigo = disciplinaabono.registroaula and disciplinaabono.matricula = frequenciaaula.matricula and disciplinaabono.disciplina = disciplina.codigo ");
		sqlStr.append(" left join abonofalta on disciplinaabono.abonofalta = abonofalta.codigo ");
		sqlStr.append(" left join tipojustificativafalta on tipojustificativafalta.codigo = abonofalta.tipojustificativafalta ");	
		sqlStr.append(" WHERE (registroaula.disciplina = ").append(codigoDisciplina);
		if (turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" or registroaula.disciplina in(select distinct disciplinaequivalenteTurmaagrupada from turmadisciplina where turmadisciplina.turma = ").append(turmaVO.getCodigo()).append(" and turmadisciplina.disciplina = ").append(codigoDisciplina).append(") ");
			sqlStr.append(" or registroaula.disciplina in (select distinct equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(codigoDisciplina.intValue()).append(") ");
			sqlStr.append(" or registroaula.disciplina in (select distinct disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(codigoDisciplina.intValue()).append(") ");
		}
		sqlStr.append(") ");
		sqlStr.append(" and (mptd.disciplina = ").append(codigoDisciplina);
		if (turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" or mptd.disciplina in (select distinct disciplinaequivalenteTurmaagrupada from turmadisciplina where turmadisciplina.turma = ").append(turmaVO.getCodigo()).append(" and turmadisciplina.disciplina = ").append(codigoDisciplina).append(") ");
			sqlStr.append(" or mptd.disciplina in (select distinct equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(codigoDisciplina.intValue()).append(") ");
			sqlStr.append(" or mptd.disciplina in (select distinct disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(codigoDisciplina.intValue()).append(") ");
		}
		sqlStr.append(" ) ");
		if (filtroVisaoProfessor) {
			sqlStr.append(" and matriculaperiodo.situacao not in ('CA', 'TR') and matriculaperiodo.situacaoMatriculaPeriodo not in ('PR','TR', 'PC') ");
			if (!trazerAlunoPendenteFinanceiro) {
				sqlStr.append(" and matriculaperiodo.situacao <> 'PF' ");
			}
		} else {
			sqlStr.append(" and matriculaperiodo.situacaoMatriculaPeriodo not in ('PC') ");
		}
		sqlStr.append(" AND turma.codigo = ").append(turmaVO.getCodigo());
		// sqlStr.append(" AND (turma.codigo = ").append(turmaVO.getCodigo()).append(" OR turma.codigo in (select turma from turmaagrupada where
		// turmaorigem = ").append(turmaVO.getCodigo()).append(")) ");
		if(Uteis.isAtributoPreenchido(codigoProfessor)) {
			sqlStr.append(" AND (registroaula.professor is null or registroaula.professor = ").append(codigoProfessor).append(")");
		}
		
		if (turmaVO.getSemestral()) {
			sqlStr.append(" AND registroaula.semestre = '").append(semestre).append("' ");
			sqlStr.append(" AND registroaula.ano = '").append(ano).append("' ");
			sqlStr.append(" AND mptd.ano = registroaula.ano and mptd.semestre = registroaula.semestre ");
		} else if (turmaVO.getAnual()) {
			sqlStr.append(" AND registroaula.ano = '").append(ano).append("' ");
			sqlStr.append(" AND mptd.ano = registroaula.ano ");
		}
		sqlStr.append(" AND registroaula.data = '").append(horarioProfessorDia.getData()).append("' ");
		/**
		 * Adicionada regra para resolver impactos relacionados a alunos que estão Cursando por Correspondência e que disciplinas saiam duplicadas no
		 * Boletim Acadêmico
		 */		
		if(!trazerAlunoTransferenciaMatriz){
			sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		}		
		sqlStr.append(" and registroaula.atividadecomplementar = false ");
		
		SqlRowSet tabelaResultado = null;
		if (turmaVO.getTurmaAgrupada()) {
			StringBuffer sql = new StringBuffer();
			sql.append("select t.* from ");
			sql.append("(").append(sqlStr).append(") as t ");
			sql.append(" where  (exists (").append(sqlStr).append(" and historico.disciplina = t.\"matriculaperiodoturmadisciplina.disciplina\" and historico.disciplina = ").append(codigoDisciplina).append(" and historico.matricula = t.\"frequenciaaula.matricula\" limit 1) ") ;
			sql.append(" or not exists (").append(sqlStr).append(" and historico.disciplina = ").append(codigoDisciplina).append(" and historico.matricula = t.\"frequenciaaula.matricula\" limit 1)) ") ;			
			sql.append(" order by t.\"registroaula.nrAula\", t.\"registroAula.codigo\", t.\"pessoa.nome\" ");
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		}else {		
			sqlStr.append(" order by registroaula.nraula, registroaula.codigo, pessoa.nome ");
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		}
		return (montarDadosConsultaCompleta(tabelaResultado, usuario));
	}

	public List<RegistroAulaVO> montarDadosConsultaCompleta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<RegistroAulaVO> listaRegistroAula = new ArrayList<RegistroAulaVO>(0);
		while (tabelaResultado.next()) {
			RegistroAulaVO registroAula = new RegistroAulaVO();
			montarDadosCompletoRegistroAula(registroAula, tabelaResultado);
			listaRegistroAula.add(registroAula);
		}
		return listaRegistroAula;
	 
	}

	private void montarDadosCompletoRegistroAula(RegistroAulaVO obj, SqlRowSet dadosSQL) throws Exception {
		obj.setCodigo(dadosSQL.getInt("registroAula.codigo"));
		obj.setData(dadosSQL.getDate("registroAula.data"));
		obj.getTurma().setCodigo(dadosSQL.getInt("registroAula.turma"));
		obj.getTurma().setIdentificadorTurma(dadosSQL.getString("turma.identificadorTurma"));
		obj.getTurma().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensinoturma.nome"));
		obj.getTurma().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensinoturma.codigo"));
		obj.getDisciplina().setCodigo(dadosSQL.getInt("registroAula.disciplina"));
		obj.getDisciplina().setNome(dadosSQL.getString("disciplina.nome"));
		obj.setCargaHoraria(dadosSQL.getInt("registroAula.cargaHoraria"));
		obj.setConteudo(dadosSQL.getString("registroAula.conteudo"));
		obj.getResponsavelRegistroAula().setCodigo(dadosSQL.getInt("registroAula.responsavelRegistroAula"));
		obj.setDataRegistroAula(dadosSQL.getDate("registroAula.dataRegistroAula"));
		obj.getProfessor().setCodigo(dadosSQL.getInt("registroAula.professor"));
		obj.setDiaSemana(dadosSQL.getString("registroAula.diaSemana"));
		obj.setSemestre(dadosSQL.getString("registroAula.semestre"));
		obj.setAno(dadosSQL.getString("registroAula.ano"));
		obj.setHorario(dadosSQL.getString("registroAula.horario"));
		obj.setTipoAula(dadosSQL.getString("registroAula.tipoAula"));
		obj.setNrAula(dadosSQL.getInt("registroAula.nrAula"));
		obj.setPraticaSupervisionada(dadosSQL.getString("registroaula.praticasupervisionada"));
		
		obj.getTurma().setSemestral(dadosSQL.getBoolean("Turma.semestral"));
		obj.getTurma().setAnual(dadosSQL.getBoolean("Turma.anual"));
		obj.getTurma().setTurmaAgrupada(dadosSQL.getBoolean("Turma.turmaagrupada"));
		obj.getTurma().setSala(dadosSQL.getString("Turma.sala"));
		obj.getTurma().getGradeCurricularVO().setCodigo(dadosSQL.getInt("Turma.gradeCurricular"));
		obj.getTurma().getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getTurma().getCurso().setPeriodicidade(dadosSQL.getString("curso.periodicidade"));
		obj.getTurma().getCurso().setNivelEducacional(dadosSQL.getString("curso.nivelEducacional"));
		obj.getTurma().getTurno().setCodigo(dadosSQL.getInt("turno.codigo"));
		
		obj.setAtividadeComplementar(dadosSQL.getBoolean("registroAula.atividadeComplementar"));
		obj.setPraticaSupervisionada(dadosSQL.getString("registroaula.praticasupervisionada"));
		obj.setNovoObj(Boolean.FALSE);

		FrequenciaAulaVO frequenciaAulaVO = null;
		Integer codigoRegistroAula = 0;
		do {
			if ((codigoRegistroAula != null) && (!codigoRegistroAula.equals(0)) && (!codigoRegistroAula.equals(dadosSQL.getInt("registroAula.codigo")))) {
				dadosSQL.previous();
				break;
			}
			frequenciaAulaVO = new FrequenciaAulaVO();
			frequenciaAulaVO.setPresente(dadosSQL.getBoolean("frequenciaaula.presente"));
			frequenciaAulaVO.setAbonado(dadosSQL.getBoolean("frequenciaaula.abonado"));
			frequenciaAulaVO.setJustificado(dadosSQL.getBoolean("frequenciaaula.justificado"));
			frequenciaAulaVO.getHistoricoVO().setRealizouTransferenciaTurno(dadosSQL.getBoolean("realizoutransferenciaTurno"));
			frequenciaAulaVO.setRegistroAula(dadosSQL.getInt("frequenciaaula.registroAula"));
			// Dados da Matrícula
			frequenciaAulaVO.getMatricula().setMatricula(dadosSQL.getString("frequenciaaula.matricula"));
			frequenciaAulaVO.getMatricula().setData(dadosSQL.getDate("matricula.data"));
			frequenciaAulaVO.getMatricula().setSituacao(dadosSQL.getString("matricula.situacao"));
			frequenciaAulaVO.getMatricula().setSituacaoFinanceira(dadosSQL.getString("matricula.situacaoFinanceira"));
			frequenciaAulaVO.getMatricula().setDataConclusaoCurso(dadosSQL.getDate("matricula.dataConclusaoCurso"));
			frequenciaAulaVO.getMatricula().setDataInicioCurso(dadosSQL.getDate("matricula.dataInicioCurso"));
			frequenciaAulaVO.getMatricula().setUpdated(dadosSQL.getTimestamp("matricula.updated"));
			frequenciaAulaVO.getMatricula().getFormacaoAcademica().setCodigo(dadosSQL.getInt("matricula.formacaoAcademica"));
			frequenciaAulaVO.getMatricula().getAutorizacaoCurso().setCodigo(dadosSQL.getInt("matricula.autorizacaoCurso"));
			frequenciaAulaVO.getMatricula().getConsultor().setCodigo(dadosSQL.getInt("matricula.consultor"));
			frequenciaAulaVO.getMatricula().setFormaIngresso(dadosSQL.getString("matricula.formaIngresso"));
			frequenciaAulaVO.getMatricula().setTipoMatricula(dadosSQL.getString("matricula.tipoMatricula"));
			frequenciaAulaVO.getMatricula().setTituloMonografia(dadosSQL.getString("matricula.nomeMonografia"));
			frequenciaAulaVO.getMatricula().setNotaMonografia(dadosSQL.getDouble("matricula.notaMonografia"));
			frequenciaAulaVO.getMatricula().getGradeCurricularAtual().setCodigo(dadosSQL.getInt("matricula.gradeCurricularAtual"));
			frequenciaAulaVO.getMatricula().setMatriculaSuspensa(dadosSQL.getBoolean("matricula.matriculaSuspensa"));
			frequenciaAulaVO.getMatricula().setNivelMontarDados(NivelMontarDados.BASICO);
			// Dados do Aluno
			frequenciaAulaVO.getMatricula().getAluno().setCodigo(dadosSQL.getInt("Pessoa.codigo"));
			frequenciaAulaVO.getMatricula().getAluno().setNome(dadosSQL.getString("Pessoa.nome"));
			frequenciaAulaVO.getMatricula().getAluno().setCPF(dadosSQL.getString("Pessoa.cpf"));
			frequenciaAulaVO.getMatricula().getAluno().setRG(dadosSQL.getString("Pessoa.rg"));
			frequenciaAulaVO.getMatricula().getAluno().setDataNasc(dadosSQL.getDate("Pessoa.dataNasc"));
			// Dados do Arquivo do Aluno
			frequenciaAulaVO.getMatricula().getAluno().getArquivoImagem().setCodigo(dadosSQL.getInt("codArquivo"));
			frequenciaAulaVO.getMatricula().getAluno().setNivelMontarDados(NivelMontarDados.BASICO);
			// Dados do Curso
			frequenciaAulaVO.getMatricula().getCurso().setCodigo(dadosSQL.getInt("Curso.codigo"));
			frequenciaAulaVO.getMatricula().getCurso().setNome(dadosSQL.getString("Curso.nome"));
			frequenciaAulaVO.getMatricula().getCurso().setNivelEducacional(dadosSQL.getString("Curso.nivelEducacional"));
			frequenciaAulaVO.getMatricula().getCurso().setPeriodicidade(dadosSQL.getString("Curso.periodicidade"));
			frequenciaAulaVO.getMatricula().getCurso().getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("Curso.configuracaoAcademico"));
			frequenciaAulaVO.getMatricula().getCurso().setNivelMontarDados(NivelMontarDados.BASICO);
			// Dados da Unidade
			frequenciaAulaVO.getMatricula().getUnidadeEnsino().setCodigo(dadosSQL.getInt("UnidadeEnsino.codigo"));
			frequenciaAulaVO.getMatricula().getUnidadeEnsino().setNome(dadosSQL.getString("UnidadeEnsino.nome"));
			frequenciaAulaVO.getMatricula().getUnidadeEnsino().getCidade().setCodigo(dadosSQL.getInt("UnidadeEnsino.cidade"));
			frequenciaAulaVO.getMatricula().getUnidadeEnsino().setNivelMontarDados(NivelMontarDados.BASICO);
			// Dados do Turno
			frequenciaAulaVO.getMatricula().getTurno().setCodigo(dadosSQL.getInt("Turno.codigo"));
			frequenciaAulaVO.getMatricula().getTurno().setNome(dadosSQL.getString("Turno.nome"));
			frequenciaAulaVO.getMatricula().getTurno().setNivelMontarDados(NivelMontarDados.BASICO);
			// Dados do MatriculaPeriodo
			frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().setCodigo(dadosSQL.getInt("matriculaperiodo.codigo"));
			frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().setSituacaoMatriculaPeriodo(dadosSQL.getString("matriculaperiodo.situacaomatriculaperiodo"));
			frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().setSituacao(dadosSQL.getString("matriculaperiodo.situacao"));
			frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().setDataFechamentoMatriculaPeriodo(dadosSQL.getDate("matriculaperiodo.datafechamentomatriculaperiodo"));
			frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().setData(dadosSQL.getDate("matriculaperiodo.data"));
			frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().getGradeCurricular().setCodigo(dadosSQL.getInt("matriculaPeriodo.gradecurricular"));
			frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().getTurma().setCodigo(dadosSQL.getInt("matriculaPeriodo.turma"));
			frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().getUnidadeEnsinoCursoVO().setCodigo(dadosSQL.getInt("MatriculaPeriodo.unidadeEnsinoCurso"));
			frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().setProcessoMatricula(dadosSQL.getInt("MatriculaPeriodo.processoMatricula"));
			frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().setUnidadeEnsinoCurso(dadosSQL.getInt("MatriculaPeriodo.unidadeEnsinoCurso"));
			frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().setAlunoTransferidoUnidade(dadosSQL.getBoolean("MatriculaPeriodo.alunoTransferidoUnidade"));
			frequenciaAulaVO.getHistoricoVO().getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("historico.configuracaoAcademico"));
			frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().setNivelMontarDados(NivelMontarDados.BASICO);
			frequenciaAulaVO.getHistoricoVO().setDataRegistro(dadosSQL.getDate("historico.dataRegistro"));
			frequenciaAulaVO.getHistoricoVO().setMatricula(frequenciaAulaVO.getMatricula());
			
			frequenciaAulaVO.setMatriculaPeriodoTurmaDisciplina(dadosSQL.getInt("matriculaPeriodoTurmaDisciplina.codigo"));
			frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().setCodigo(dadosSQL.getInt("matriculaPeriodoTurmaDisciplina.codigo"));
			frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().setCodigo(dadosSQL.getInt("matriculaPeriodoTurmaDisciplina.turma"));
			
			frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaPratica().setCodigo(dadosSQL.getInt("matriculaPeriodoTurmaDisciplina.turmaPratica"));
			frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaTeorica().setCodigo(dadosSQL.getInt("matriculaPeriodoTurmaDisciplina.turmaTeorica"));
			frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().setCodigo(new Integer(dadosSQL.getInt("matriculaPeriodoTurmaDisciplina.disciplina")));
			frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(dadosSQL.getString("matriculaPeriodoTurmaDisciplina.modalidadeDisciplina")));
			frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().setAno(dadosSQL.getString("matriculaPeriodoTurmaDisciplina.ano"));
			frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().setSemestre(dadosSQL.getString("matriculaPeriodoTurmaDisciplina.semestre"));
			frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().setInclusaoForaPrazo(dadosSQL.getBoolean("matriculaPeriodoTurmaDisciplina.inclusaoForaPrazo"));
			frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().setMatricula(frequenciaAulaVO.getMatricula().getMatricula());
			frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().setMatriculaPeriodo(frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().getCodigo());
			frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().setMatriculaPeriodoObjetoVO(frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo());
			frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().setMatriculaObjetoVO(frequenciaAulaVO.getMatricula());
			frequenciaAulaVO.setDataRegistroAula(dadosSQL.getDate("registroAula.data"));
			frequenciaAulaVO.setNovoObj(Boolean.FALSE);
			if (dadosSQL.getString("sigla") != null) {
				frequenciaAulaVO.getAbonoFaltaVO().getTipoJustificativaFaltaVO().setSigla(dadosSQL.getString("sigla"));
			}
			if (dadosSQL.getString("tipojustificativafalta.descricao") != null) {
				frequenciaAulaVO.getAbonoFaltaVO().getTipoJustificativaFaltaVO().setDescricao(dadosSQL.getString("tipojustificativafalta.descricao"));
			}	
			codigoRegistroAula = frequenciaAulaVO.getRegistroAula();
			obj.getFrequenciaAulaVOs().add(frequenciaAulaVO);
			if (dadosSQL.isLast()) {
				return;
			}
		} while (dadosSQL.next());
	}

	public Boolean consultarRegistroAulaExistente(String horario, Integer turma, Integer disciplina, Integer professor, String ano, String semestre, Date data, Boolean liberarRegistroAulaEntrePeriodo, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT registroAula.codigo, registroAula.ano, registroAula.semestre, pessoa.nome as professor FROM registroAula left join pessoa on pessoa.codigo = registroAula.professor ");
//		sqlStr.append(" WHERE registroAula.turma = ").append(turma).append(" AND registroAula.disciplina = ").append(disciplina).append(" AND registroAula.professor = ").append(professor);
		sqlStr.append(" WHERE registroAula.turma = ").append(turma).append(" AND registroAula.disciplina = ").append(disciplina);
		if (!liberarRegistroAulaEntrePeriodo) {
			sqlStr.append(" AND registroAula.ano = '").append(ano).append("' AND registroAula.semestre = '").append(semestre).append("' ");
		}
		sqlStr.append(" AND registroAula.data = '").append(data).append("' ");
		if(Uteis.isAtributoPreenchido(professor)){
			sqlStr.append(" AND registroAula.professor = ").append(professor).append(" ");
		}
		/**
		 * Adicionado substring para trazer o número da aula para que seja
		 * possível comparar apenas seu número, devido a estar duplicando
		 * horários de aulas
		 */
		sqlStr.append(" AND registroAula.nrAula = ").append(horario).append(" ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		if (tabelaResultado.next()) {
			if (!tabelaResultado.getString("ano").equals(ano) || !tabelaResultado.getString("semestre").equals(semestre)) {
				throw new Exception("Esta aula foi registrada na programação de aula " + tabelaResultado.getString("ano") + "/" + tabelaResultado.getString("semestre") + " para o professor " + tabelaResultado.getString("professor") + " nesta turma e disciplina. Para que seja possível registrar esta aula deve ser excluido o registro de aula realizado na outra programação de aula bem como a exclusão da programação de aula neste dia. ");
			}
			return true;
		}
		return false;
	}

	public Boolean consultarRegistroAulaExistentePorAluno(String matricula, Integer registroAula, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT codigo FROM registroAula");
		sqlStr.append(" inner join frequenciaaula on frequenciaaula.registroaula = registroaula.codigo ");
		sqlStr.append(" WHERE codigo = ").append(registroAula);
		sqlStr.append(" AND matricula = '").append(matricula).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (tabelaResultado.next());
	}

	/***
	 * Método responsável por consultar o registro de aula de acordo com um
	 * determinada data
	 * 
	 * @param dataAula
	 * @param turma
	 * @param codigoDisciplina
	 * @param codigoProfessor
	 * @param ano
	 * @param semestre
	 * @param nivelMontarDados
	 * @return
	 * @throws Exception
	 */
	public RegistroAulaVO consultarPorDataTurmaProfessor(Date dataAula, TurmaVO turma, Integer codigoDisciplina, Integer codigoProfessor, String ano, String semestre, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		RegistroAulaVO registroAula = new RegistroAulaVO();
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT registroAula.codigo, registroAula.dataregistroaula, registroAula.responsavelregistroaula, registroaula.turma, registroaula.conteudo, ");
		sqlStr.append("registroAula.cargahoraria, registroaula.data, registroAula.codigo, registroaula.disciplina, registroaula.tipoaula, registroaula.diasemana, ");
		sqlStr.append("registroaula.professor,registroaula.semestre,registroaula.ano,registroaula.horario ");
		sqlStr.append("FROM registroaula ");
		sqlStr.append("INNER JOIN frequenciaaula ON frequenciaaula.registroaula = registroaula.codigo ");
		sqlStr.append("WHERE registroaula.ano = '").append(ano).append("' and registroaula.semestre = '").append(semestre).append("' ");
		sqlStr.append("AND turma = ").append(turma.getCodigo());
		sqlStr.append(" AND disciplina = ").append(codigoDisciplina);
		sqlStr.append(" AND professor = ").append(codigoProfessor);
		sqlStr.append(" AND data = '").append(dataAula).append("' ");
		sqlStr.append("ORDER BY registroaula.codigo limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			registroAula = montarDados(tabelaResultado, nivelMontarDados, usuario);
		}
		return registroAula;
	}

	public void verificarVinculoRegistroAula(Integer turma, Integer disciplina, Integer professor, Date dataAula) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT registroAula.codigo, registroAula.dataregistroaula, registroAula.responsavelregistroaula, registroaula.turma, registroaula.conteudo, ");
		sqlStr.append("registroAula.cargahoraria, registroaula.data, registroAula.codigo, registroaula.disciplina, registroaula.tipoaula, registroaula.diasemana, ");
		sqlStr.append("registroaula.professor,registroaula.semestre,registroaula.ano,registroaula.horario ");
		sqlStr.append("FROM registroaula ");
		sqlStr.append("INNER JOIN frequenciaaula ON frequenciaaula.registroaula = registroaula.codigo ");
		sqlStr.append("WHERE 1=1 ");
		sqlStr.append("AND turma = ").append(turma);
		if (disciplina > 0) {
			sqlStr.append(" AND disciplina = ").append(disciplina);
		}
		if (professor > 0) {
			sqlStr.append(" AND professor = ").append(professor);
		}
		if (dataAula != null) {
			sqlStr.append(" AND registroAula.data = '").append(dataAula).append("' ");
		}
		sqlStr.append(" ORDER BY registroaula.codigo limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			throw new Exception("Não é possível excluir essa programação de aula, pois existe(m) registro(s) de aula(s) cadastrado(s) para a turma, a alteração ou exclusão da programação de aula pode ocasionar erros futuros! Caso seja necessário realizar a opeção é necessário excluir todos os registros de aula dessa turma!");
		}
	}

	public boolean realizarVerificacaoPorDataTurmaProfessorDisciplina(Date dataAula, TurmaVO turma, Integer codigoDisciplina, Integer codigoProfessor, String ano, String semestre) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT registroAula.codigo as \"registroAula.codigo\" ");
		sqlStr.append("FROM registroaula ");
		sqlStr.append(" WHERE 1=1 ");
		// sqlStr.append("INNER JOIN frequenciaaula ON frequenciaaula.registroaula = registroaula.codigo ");
		if (!turma.getCurso().getLiberarRegistroAulaEntrePeriodo()) {
			if (!ano.equals("")) {
				sqlStr.append(" AND registroaula.ano = '").append(ano).append("' ");
			}
			if (!semestre.equals("")) {
				sqlStr.append(" and registroaula.semestre = '").append(semestre).append("' ");
			}
		}
		sqlStr.append("AND turma = ").append(turma.getCodigo());
		sqlStr.append(" AND disciplina = ").append(codigoDisciplina);
		sqlStr.append(" AND professor = ").append(codigoProfessor);
		sqlStr.append(" AND data = '").append(dataAula).append("' ");
		sqlStr.append("ORDER BY registroaula.codigo limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	public List<PessoaVO> carregarCargaHorariaEmontarListaProfessorRegistroAula(RegistroAulaVO registroAulaVO, UsuarioVO usuario) throws Exception {
		registroAulaVO.setFrequenciaAulaVOs(new ArrayList(0));
		int y = Uteis.getDiaSemana(registroAulaVO.getData());
		registroAulaVO.setDiaSemana("0" + y);
		registroAulaVO.setCargaHoraria(0);
		if (registroAulaVO.getTurma().getCodigo().intValue() != 0 && registroAulaVO.getDisciplina().getCodigo() != 0 && !registroAulaVO.getDiaSemana().equals("")) {
			List objs = getFacadeFactory().getHorarioTurmaFacade().consultarPorCodigoTurma(registroAulaVO.getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
			int tam = objs.size();
			if (tam > 0) {
				HorarioTurmaVO obj = (HorarioTurmaVO) objs.get(0);
				carregarCargaHoraria(registroAulaVO, obj, usuario);
				return montarListaProfessorRegistroAula(obj, registroAulaVO, usuario);
			} else {
				throw new Exception("Não existe uma programação de aula cadastrada para esta turma.");
			}
		} else {
			registroAulaVO.setCargaHoraria(0);
		}
		return new ArrayList<PessoaVO>(0);
	}

	public void carregarCargaHoraria(RegistroAulaVO registroAulaVO, HorarioTurmaVO obj, UsuarioVO usuario) throws Exception {
		int x = 1;
		while (x <= registroAulaVO.getTurma().getTurno().getNrAulas().intValue()) {
			getFacadeFactory().getHorarioTurmaFacade().inicializarDadosHorarioTurmaPorHorarioTurma(obj, true, usuario);
			// Integer codigoDisciplina = obj.getCodigoDisciplina(x,
			// DiaSemana.getEnum(registroAulaVO.getDiaSemana()),
			// registroAulaVO.getData(), TipoHorarioTurma.SEMANAL);
			// if
			// (codigoDisciplina.equals(registroAulaVO.getDisciplina().getCodigo()))
			// {
			// //
			// registroAulaVO.setCargaHoraria(registroAulaVO.getCargaHoraria() +
			// new Integer(1));
			// }
			Integer codigoDisciplina = obj.getCodigoDisciplina(x, registroAulaVO.getData());
			if (codigoDisciplina.equals(registroAulaVO.getDisciplina().getCodigo())) {
				// registroAulaVO.setCargaHoraria(registroAulaVO.getCargaHoraria()
				// + registroAulaVO.getTurma().getTurno().getDuracaoAula());
				// registroAulaVO.setCargaHoraria(registroAulaVO.getCargaHoraria()
				// + new Integer(1));
			}
			x++;
		}

	}

	public List<PessoaVO> montarListaProfessorRegistroAula(HorarioTurmaVO obj, RegistroAulaVO registroAulaVO, UsuarioVO usuario) {
		List<PessoaVO> listaProfessor = new ArrayList<PessoaVO>(0);
		try {
			// Integer professor =
			// getFacadeFactory().getHorarioTurmaFacade().consultarProfessorLecionaDisciplina(obj,registroAulaVO.getDisciplina().getCodigo(),
			// TipoHorarioTurma.SEMANAL);
			// if (professor.intValue() != 0) {
			// adicionarProfessor(professor, listaProfessor);
			// }
			List<Integer> professores = getFacadeFactory().getHorarioTurmaFacade().consultarProfessorLecionaDisciplina(obj, registroAulaVO.getDisciplina().getCodigo());
			for (Integer professor : professores) {
				adicionarProfessor(professor, listaProfessor, usuario);
			}

		} catch (Exception e) {
			// System.out.println("Registar Aula Erro:" + e.getMessage());
		}
		return listaProfessor;
	}

	private void adicionarProfessor(Integer codigoProfessor, List<PessoaVO> listaProfessor, UsuarioVO usuario) throws Exception {
		Iterator i = listaProfessor.iterator();
		while (i.hasNext()) {
			PessoaVO item = (PessoaVO) i.next();
			if (item.getCodigo().equals(codigoProfessor)) {
				return;
			}
		}
		PessoaVO professor = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(codigoProfessor, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		listaProfessor.add(professor);
	}

	/**
	 * Responsável por realizar uma consulta de <code>RegistroAula</code>
	 * através do valor do atributo <code>codigo</code> da classe
	 * <code>ProgramacaoAula</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>RegistroAulaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigoTurma(Integer valorConsulta, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Turma WHERE RegistroAula.turma = turma.codigo and turma.codigo >= " + valorConsulta.intValue() + " ";

		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}
		sqlStr += " ORDER BY turma.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorCodigoDisciplina(Integer valorConsulta, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Disciplina WHERE RegistroAula.disciplina = disciplina.codigo and disciplina.codigo = " + valorConsulta.intValue() + " ";
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}

		sqlStr += " ORDER BY disciplina.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorCodigoDisciplinaCodigoTurma(Integer valorConsulta, Integer turma, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Disciplina, Turma WHERE RegistroAula.disciplina = disciplina.codigo " + " and disciplina.codigo = " + valorConsulta.intValue() + " and RegistroAula.turma = turma.codigo and turma.codigo = " + turma.intValue() + " ";
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}

		sqlStr += " ORDER BY disciplina.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorNomeDisciplina(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Disciplina WHERE RegistroAula.disciplina = disciplina.codigo and lower (disciplina.nome) like('" + valorConsulta.toLowerCase() + "%') ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Disciplina, Turma WHERE RegistroAula.turma = Turma.codigo and RegistroAula.disciplina = disciplina.codigo and lower (disciplina.nome) like('" + valorConsulta.toLowerCase() + "%') and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}
		sqlStr += " ORDER BY disciplina.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorNomeDisciplinaProfessor(String valorConsulta, String semestre, String ano, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(disciplina.nome) like('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%')");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND turma.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (!semestre.equals("")) {
			sqlStr.append(" and RegistroAula.semestre = '");
			sqlStr.append(semestre);
			sqlStr.append("'");
		}
		if (!ano.equals("")) {
			sqlStr.append(" and RegistroAula.ano = '");
			sqlStr.append(ano);
			sqlStr.append("'");
		}
		sqlStr.append(" and Pessoa.codigo = ");
		sqlStr.append(professor);
		sqlStr.append(" ORDER BY disciplina.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);

	}

	public List consultarPorNomeProfessor(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Pessoa WHERE RegistroAula.professor = pessoa.codigo and lower (pessoa.nome) like('" + valorConsulta.toLowerCase() + "%') ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Pessoa WHERE RegistroAula.professor = pessoa.codigo and lower (pessoa.nome) like('" + valorConsulta.toLowerCase() + "%') and RegistroAula.turma = turma.codigo and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}

		sqlStr += " ORDER BY pessoa.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorNomeCurso(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Turma, Curso WHERE RegistroAula.turma = Turma.codigo and turma.curso = curso.codigo and lower (curso.nome) like('" + valorConsulta.toLowerCase() + "%') ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}

		sqlStr += " ORDER BY RegistroAula.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorNomeCursoProfessor(String valorConsulta, String semestre, String ano, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(curso.nome) like('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%')");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND turma.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (!semestre.equals("")) {
			sqlStr.append(" and RegistroAula.semestre = '");
			sqlStr.append(semestre);
			sqlStr.append("'");
		}
		if (!ano.equals("")) {
			sqlStr.append(" and RegistroAula.ano = '");
			sqlStr.append(ano);
			sqlStr.append("'");
		}
		sqlStr.append(" and Pessoa.codigo = ");
		sqlStr.append(professor);
		sqlStr.append(" ORDER BY curso.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);

	}

	public List consultarPorIdentificadorTurma(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Turma WHERE RegistroAula.turma = Turma.codigo and lower (Turma.identificadorTurma) like ('" + valorConsulta.toLowerCase() + "%') ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Turma WHERE RegistroAula.turma = Turma.codigo and lower (Turma.identificadorTurma) like ('" + valorConsulta.toLowerCase() + "%') and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}
		sqlStr += " ORDER BY turma.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorIdentificadorTurmaProfessor(String valorConsulta, String semestre, String ano, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(turma.identificadorTurma) like('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%')");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND turma.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (!semestre.equals("")) {
			sqlStr.append(" and RegistroAula.semestre = '");
			sqlStr.append(semestre);
			sqlStr.append("'");
		}
		if (!ano.equals("")) {
			sqlStr.append(" and RegistroAula.ano = '");
			sqlStr.append(ano);
			sqlStr.append("'");
		}
		sqlStr.append(" and Pessoa.codigo = ");
		sqlStr.append(professor);
		sqlStr.append(" ORDER BY turma.identificadorTurma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}


	public List<RegistroAulaVO> consultaRapidaPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupada(Integer codigoTurma, Boolean turmaAgrupada, String semestre, String ano, Integer professor, Integer disciplina, Integer unidadeEnsino, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, String tipoLayout, String tipoAluno, boolean controlarAcesso, NivelMontarDados nivelMontarDados, Boolean liberarRegistroAulaEntrePeriodo, UsuarioVO usuario, Boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append(" SELECT RegistroAula.codigo, registroaula.turma, registroaula.disciplina FROM RegistroAula inner JOIN Turma ON (RegistroAula.turma = Turma.codigo) ");
		if (turmaAgrupada) {
			sqlStr.append(" WHERE (turma.codigo = ").append(codigoTurma).append(")");
		} else {
			sqlStr.append(" WHERE (turma.codigo = ").append(codigoTurma).append(" or turma.codigo in (select turma from turmaagrupada where  turmaorigem = ").append(codigoTurma).append("))");
		}
		sqlStr.append(" AND (RegistroAula.disciplina = ").append(disciplina.intValue()).append(")");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND (Turma.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(")");
		}
		if (professor.intValue() != 0) {
			sqlStr.append(" AND (RegistroAula.professor = ").append(professor.intValue()).append(")");
		}
		sqlStr.append(" and ((turma.anual and RegistroAula.ano = '").append(ano).append("') ");
		sqlStr.append(" or (turma.semestral and RegistroAula.ano = '").append(ano).append("' ").append(" and RegistroAula.semestre = '").append(semestre).append("') ");
		sqlStr.append(" or (turma.semestral = false and  turma.anual =  false) )");		
		sqlStr.append(" ORDER BY registroaula.data, registroaula.horario");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<RegistroAulaVO> vetResultado = new ArrayList<RegistroAulaVO>(0);
		while (tabelaResultado.next()) {
			RegistroAulaVO obj = new RegistroAulaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getTurma().setCodigo(tabelaResultado.getInt("turma"));
			obj.getDisciplina().setCodigo(tabelaResultado.getInt("disciplina"));
     		validarDadosDiarioRegistroAula(tabelaResultado.getInt("codigo"), codigoTurma, usuario);
			carregarDados(obj, nivelMontarDados, usuario, apenasAlunosAtivos, trazerAlunosPendentesFinanceiramente, filtroVisaoProfessor, filtroTipoCursoAluno, tipoLayout, tipoAluno, turmaAgrupada, liberarRegistroAulaEntrePeriodo, permitirRealizarLancamentoAlunosPreMatriculados);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	@Override
	public List<RegistroAulaVO> consultaRapidaPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupada(TurmaVO turmaVO, String semestre, String ano, List<ProfessorTitularDisciplinaTurmaVO> professores, Integer disciplina, Integer unidadeEnsino, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, String tipoLayout, String tipoAluno, boolean controlarAcesso, NivelMontarDados nivelMontarDados, Boolean liberarRegistroAulaEntrePeriodo, String mes, String anoMes, Integer configuracaoAcademico, UsuarioVO usuario, Boolean trazerAlunoTransferido, boolean permitirRealizarLancamentoAlunosPreMatriculados, String tipoFiltroPeriodo, Date dataInicio, Date dataFim) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append(getSQLPadraoConsultaCompletaDiario(turmaVO, filtroVisaoProfessor, liberarRegistroAulaEntrePeriodo, tipoLayout, apenasAlunosAtivos, trazerAlunosPendentesFinanceiramente, tipoAluno, semestre, ano, professores, disciplina, trazerAlunoTransferido));

		if (turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {
			sqlStr.append(" WHERE Turma.codigo = ").append(turmaVO.getCodigo());
			sqlStr.append(" and ((MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null and MatriculaPeriodoTurmaDisciplina.turma in (select turma from turmaAgrupada where turmaOrigem =  ").append(turmaVO.getCodigo()).append(") )");
			sqlStr.append("or (MatriculaPeriodoTurmaDisciplina.turmaPratica is not null and MatriculaPeriodoTurmaDisciplina.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
			sqlStr.append("or (MatriculaPeriodoTurmaDisciplina.turmaTeorica is not null and MatriculaPeriodoTurmaDisciplina.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
			sqlStr.append(")   and regAula.turmaorigem is null  ");
		} else {
			sqlStr.append(" WHERE Turma.codigo = ").append(turmaVO.getCodigo());
		}
		if (!turmaVO.getSubturma() && !turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
		}
		if (Uteis.isAtributoPreenchido(configuracaoAcademico)) {
			sqlStr.append(" AND configuracaoacademico.codigo = ").append(configuracaoAcademico);
		}
		sqlStr.append(" AND (regAula.disciplina = ").append(disciplina.intValue()).append(")");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND (Turma.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(")");
		}
		if (professores != null && !professores.isEmpty()) {
			sqlStr.append(" AND (regAula.professor in (0 ");
			for (ProfessorTitularDisciplinaTurmaVO professor : professores) {
				sqlStr.append(", ").append(professor.getProfessor().getPessoa().getCodigo().intValue());
			}
			sqlStr.append(")) ");
		}

		if (!semestre.equals("") && !liberarRegistroAulaEntrePeriodo) {
			sqlStr.append(" AND (regAula.semestre = '").append(semestre).append("') ");
		}
		if (!ano.equals("") && !liberarRegistroAulaEntrePeriodo) {
			sqlStr.append(" AND (regAula.ano = '").append(ano).append("') ");
		}
		if (mes != null && !mes.trim().isEmpty() && anoMes != null && !anoMes.trim().isEmpty() && !tipoFiltroPeriodo.equals("porPeriodoData") && Uteis.isAtributoPreenchido(tipoFiltroPeriodo)) {
			sqlStr.append(" AND (extract(month from regAula.data)::INT = ").append(Integer.valueOf(mes)).append(") ");
			sqlStr.append(" AND (extract(year from regAula.data)::INT = ").append(Integer.valueOf(anoMes)).append(") ");
		}
		
		if (tipoFiltroPeriodo.equals("porPeriodoData") && Uteis.isAtributoPreenchido(tipoFiltroPeriodo)) {
			sqlStr.append(" AND (regAula.data::date BETWEEN '").append(dataInicio).append("' and '").append(dataFim).append(" ')");
		}
		
		// sqlStr.append(" WHERE (regAula.codigo= ").append(codRegistroAula).append(")");
		if (tipoLayout.equals("DiarioModRetratoRel") && (filtroVisaoProfessor != null && filtroVisaoProfessor)) {
			// este codigo abaixo foi comentado pois quando na visão do
			// professor estava definido para trazer todos os tipos de aluno
			// (normal e reposição) estava desconsiderando as reposições
			// sqlStr.append(" and regAula.turma = matriculaperiodo.turma ");
		}
		sqlStr.append(" and CASE WHEN (MatriculaPeriodoTurmaDisciplina.codigo is not null) then Historico.situacao not in ( 'AA', 'CC', 'CH', 'IS', 'AB') else (1=1) end  ");
		sqlStr.append(" and matriculaPeriodo.situacaomatriculaperiodo in ( 'AT', 'FI', 'FO' ) ");
		if (filtroVisaoProfessor != null && filtroVisaoProfessor) {
			// sqlStr.append(" and matricula.situacao = 'AT' and matriculaPeriodo.situacao <> 'PF' ");
		} else {
			sqlStr.append(" and matricula.situacao not in ('TR', 'CA') ");
		}
		if ((usuario.getIsApresentarVisaoProfessor() || usuario.getIsApresentarVisaoCoordenador()) && !permitirRealizarLancamentoAlunosPreMatriculados || (filtroVisaoProfessor != null && filtroVisaoProfessor) || ((filtroVisaoProfessor == null || !filtroVisaoProfessor) && (tipoLayout.equals("DiarioModRetratoRel") || tipoLayout.equals("DiarioRel") || ((apenasAlunosAtivos != null && apenasAlunosAtivos) && (trazerAlunosPendentesFinanceiramente == null || !trazerAlunosPendentesFinanceiramente))))) {
			sqlStr.append(" and matriculaPeriodo.situacaomatriculaperiodo <> 'PR' ");
		}


		if (filtroVisaoProfessor == null || !filtroVisaoProfessor) {
			if (apenasAlunosAtivos != null && apenasAlunosAtivos) {
				sqlStr.append(" and matricula.situacao = 'AT' ");
			}
		}
		if (Uteis.isAtributoPreenchido(trazerAlunosPendentesFinanceiramente) && !trazerAlunosPendentesFinanceiramente) {
			sqlStr.append(" and matriculaPeriodo.situacao <> 'PF' ");
		}
		
		if (filtroTipoCursoAluno != null && !filtroTipoCursoAluno.equals("") && !filtroTipoCursoAluno.equals("todos")) {
			if (filtroTipoCursoAluno.equals("posGraduacao")) {
				sqlStr.append(" AND matricula.tipomatricula = 'NO'");
			} else if (filtroTipoCursoAluno.equals("extensao")) {
				sqlStr.append(" AND matricula.tipoMatricula = 'EX' ");
			} else if (filtroTipoCursoAluno.equals("modular")) {
				sqlStr.append(" AND matricula.tipoMatricula = 'MO' ");
			}
		}
		if (tipoAluno.equals("normal")) {
			sqlStr.append(" and matriculaperiodoturmadisciplina.turma = matriculaperiodo.turma");
		} else if (tipoAluno.equals("reposicao")) {
			sqlStr.append(" and matriculaperiodoturmadisciplina.turma <> matriculaperiodo.turma");
		}

		if (filtroVisaoProfessor != null && filtroVisaoProfessor) {
			if (!liberarRegistroAulaEntrePeriodo) {
				sqlStr.append(" and matriculaperiodo.ano = regAula.ano and matriculaperiodo.semestre = regAula.semestre ");
			}
			if (tipoAluno.equals("normal")) {
				sqlStr.append(" and matriculaperiodo.turma = turma.codigo ");
			}
			if (tipoAluno.equals("reposicao")) {
				sqlStr.append(" and regAula.turma <> matriculaperiodo.turma ");
			}
		} else {
			if (!liberarRegistroAulaEntrePeriodo) {
				sqlStr.append(" and matriculaperiodo.ano = regAula.ano and matriculaperiodo.semestre = regAula.semestre ");
			}
			if (tipoAluno.equals("normal")) {
				sqlStr.append(" and matriculaperiodo.turma = turma.codigo ");
			}
			if (tipoAluno.equals("reposicao")) {
				sqlStr.append(" and regAula.turma <> matriculaperiodo.turma ");
			}
		}
		if (turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula ");			
		} else {
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.disciplina = disciplina.codigo and MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula ");
			if (tipoAluno.equals("reposicao")) {
				sqlStr.append(" and matriculaperiodoturmadisciplina.turma = turma.codigo ");
			}
		}
		if ((filtroVisaoProfessor != null && filtroVisaoProfessor) || ((filtroVisaoProfessor == null || !filtroVisaoProfessor) && (tipoLayout.equals("DiarioModRetratoRel") || tipoLayout.equals("DiarioRel") || ((apenasAlunosAtivos != null && apenasAlunosAtivos) && (trazerAlunosPendentesFinanceiramente == null || !trazerAlunosPendentesFinanceiramente))))) {
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		}
		sqlStr.append(" and case when curso.niveleducacional = 'PR' and curso.liberarRegistroAulaEntrePeriodo then " + turmaVO.getCodigo() + " else MatriculaPeriodoTurmaDisciplina.turma end = MatriculaPeriodoTurmaDisciplina.turma ");

		sqlStr.append(" order by regAula.data, regAula.codigo, nomeAlunoSemAcento ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		
		List<RegistroAulaVO> vetResultado = new ArrayList<RegistroAulaVO>(0);
		RegistroAulaVO obj = new RegistroAulaVO();
		Integer totalFaltas = 0;
		while (tabelaResultado.next()) {
			if (obj.getCodigo() == 0 || obj.getCodigo() != tabelaResultado.getInt("codigo")) {
				obj = new RegistroAulaVO();
				vetResultado.add(obj);
			}
			montarDadosCompletoDiario(obj, tabelaResultado);
			if(!tabelaResultado.getBoolean("frequenciaaula.presente") && !tabelaResultado.getBoolean("frequenciaaula.abonado")){
				totalFaltas++;
			}
		}
		Map<Integer, HistoricoVO> mapHistorico = new HashMap<Integer, HistoricoVO>(0);
		for(RegistroAulaVO registroAulaVO: vetResultado){
			
			for(FrequenciaAulaVO frequenciaAulaVO: registroAulaVO.getFrequenciaAulaVOs()){
				frequenciaAulaVO.setFaltasGeral(totalFaltas);
				if(Uteis.isAtributoPreenchido(frequenciaAulaVO.getHistoricoVO().getCodigo())){ 
					if(!mapHistorico.containsKey(frequenciaAulaVO.getHistoricoVO().getCodigo())){
						getFacadeFactory().getHistoricoFacade().carregarDadosNotaConceitoHistorico(frequenciaAulaVO.getHistoricoVO());
						mapHistorico.put(frequenciaAulaVO.getHistoricoVO().getCodigo(), frequenciaAulaVO.getHistoricoVO());
					}else{
						frequenciaAulaVO.setHistoricoVO(mapHistorico.get(frequenciaAulaVO.getHistoricoVO().getCodigo()));
					}
				}
			}
		}
		return vetResultado;
	}

	@Override
	public List<RegistroAulaVO> consultaRapidaPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupadaVisaoAdministrativa(TurmaVO turmaVO, String semestre, String ano, List<ProfessorTitularDisciplinaTurmaVO> professores, Integer disciplina, Integer unidadeEnsino, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, String tipoLayout, String tipoAluno, boolean controlarAcesso, NivelMontarDados nivelMontarDados, Boolean liberarRegistroAulaEntrePeriodo, FiltroRelatorioAcademicoVO filtroAcademicoVO, String mes, String anoMes, Integer configuracaoAcademico, UsuarioVO usuario, Boolean trazerAlunoTransferencia, String tipoFiltroPeriodo, Date dataInicio, Date dataFim) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();

		sqlStr.append(getSQLPadraoConsultaCompletaDiarioVisaoAdminstrativa(turmaVO, liberarRegistroAulaEntrePeriodo, tipoLayout, tipoAluno, semestre, ano, professores, disciplina, trazerAlunoTransferencia));

		if (turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {
			sqlStr.append(" WHERE Turma.codigo = ").append(turmaVO.getCodigo());
			sqlStr.append(" and ((MatriculaPeriodoTurmaDisciplina.turma in (select turma from turmaAgrupada where turmaOrigem =  ").append(turmaVO.getCodigo()).append(") )");
			sqlStr.append(" or (MatriculaPeriodoTurmaDisciplina.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
			sqlStr.append(" or (MatriculaPeriodoTurmaDisciplina.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
			sqlStr.append(")   and regAula.turmaorigem is null  ");
		} else {
			sqlStr.append(" WHERE Turma.codigo = ").append(turmaVO.getCodigo());
		}
		if (!turmaVO.getSubturma() && !turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
		}
		sqlStr.append(" AND (regAula.disciplina = ").append(disciplina.intValue()).append(")");
		if (Uteis.isAtributoPreenchido(configuracaoAcademico)) {
			sqlStr.append(" AND historico.configuracaoacademico = ").append(configuracaoAcademico);
		}
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND (Turma.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(")");
		}
		if (professores != null && !professores.isEmpty()) {
			sqlStr.append(" AND (regAula.professor in (0 ");
			for (ProfessorTitularDisciplinaTurmaVO professor : professores) {
				sqlStr.append(", ").append(professor.getProfessor().getPessoa().getCodigo().intValue());
			}
			sqlStr.append(")) ");
		}

		if (!semestre.equals("") && !liberarRegistroAulaEntrePeriodo) {
			sqlStr.append(" AND (regAula.semestre = '").append(semestre).append("') ");
		}
		if (!ano.equals("") && !liberarRegistroAulaEntrePeriodo && !tipoFiltroPeriodo.equals("porPeriodoData")) {
			sqlStr.append(" AND (regAula.ano = '").append(ano).append("') ");
		}
		if (mes != null && !mes.trim().isEmpty() && anoMes != null && !anoMes.trim().isEmpty() && Uteis.isAtributoPreenchido(tipoFiltroPeriodo) && !tipoFiltroPeriodo.equals("porPeriodoData")) {
			sqlStr.append(" AND (extract(month from regAula.data)::INT = ").append(Integer.valueOf(mes)).append(") ");
			sqlStr.append(" AND (extract(year from regAula.data)::INT = ").append(Integer.valueOf(anoMes)).append(") ");
		}
		
		if (Uteis.isAtributoPreenchido(tipoFiltroPeriodo) && tipoFiltroPeriodo.equals("porPeriodoData")) {
			sqlStr.append(" AND (regAula.data::date BETWEEN '").append(dataInicio).append("' and '").append(dataFim).append(" ')");
		}

		sqlStr.append(" and CASE WHEN (MatriculaPeriodoTurmaDisciplina.codigo is not null) then Historico.situacao not in ('AA', 'CC', 'CH', 'IS') else (1=1) end  ");
		
		
		sqlStr.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		sqlStr.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		if (filtroTipoCursoAluno != null && !filtroTipoCursoAluno.equals("") && !filtroTipoCursoAluno.equals("todos")) {
			if (filtroTipoCursoAluno.equals("posGraduacao")) {
				sqlStr.append(" AND matricula.tipomatricula = 'NO'");
			} else if (filtroTipoCursoAluno.equals("extensao")) {
				sqlStr.append(" AND matricula.tipoMatricula = 'EX' ");
			} else if (filtroTipoCursoAluno.equals("modular")) {
				sqlStr.append(" AND matricula.tipoMatricula = 'MO' ");
			}
		}
		if (tipoAluno.equals("normal")) {
			sqlStr.append(" and matriculaperiodoturmadisciplina.turma = matriculaperiodo.turma");
		} else if (tipoAluno.equals("reposicao")) {
			sqlStr.append(" and matriculaperiodoturmadisciplina.turma <> matriculaperiodo.turma");
		}

		if (tipoLayout.equals("DiarioModRetratoRel") || tipoLayout.equals("DiarioRel") || tipoLayout.equals("DiarioMesMesRel") || tipoLayout.equals("DiarioNotaRel") || tipoLayout.equals("DiarioRelFrequenciaNota") || tipoLayout.equals("DiarioRelFrequenciaNota2")) {
			if (!liberarRegistroAulaEntrePeriodo) {
				// Matricula Período
				sqlStr.append(" and case when  turma.anual = false and turma.semestral = false then true else case when  turma.anual then trim(matriculaperiodo.ano) = trim(regAula.ano) ");
				sqlStr.append(" else (trim(matriculaperiodo.ano) = trim(regAula.ano) and trim(matriculaperiodo.semestre) = trim(regAula.semestre) ) end end = true ");
				// MatriculaPeriodoturmaDisciplina
				sqlStr.append(" and case when  turma.anual = false and turma.semestral = false then true else case when  turma.anual then trim(MatriculaPeriodoTurmaDisciplina.ano) = trim(regAula.ano) ");
				sqlStr.append(" else (trim(MatriculaPeriodoTurmaDisciplina.ano) = trim(regAula.ano) and trim(MatriculaPeriodoTurmaDisciplina.semestre) = trim(regAula.semestre) ) end end = true ");
			}
			if (tipoAluno.equals("normal")) {
				sqlStr.append(" and matriculaperiodo.turma = matriculaperiodoturmadisciplina.turma ");
			}
		}
		if(!turmaVO.getTurmaAgrupada()){
			sqlStr.append(" and case when curso.niveleducacional = 'PR' and curso.liberarRegistroAulaEntrePeriodo then " + turmaVO.getCodigo() + "  ");
			if (turmaVO.getSubturma()) {
				if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
					sqlStr.append(" else MatriculaPeriodoTurmaDisciplina.turmateorica end =  MatriculaPeriodoTurmaDisciplina.turmateorica ");
				} else if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
					sqlStr.append(" else MatriculaPeriodoTurmaDisciplina.turmapratica end =  MatriculaPeriodoTurmaDisciplina.turmapratica ");
				} else {
					sqlStr.append(" else MatriculaPeriodoTurmaDisciplina.turma end =  MatriculaPeriodoTurmaDisciplina.turma ");
				}
			} else {
				sqlStr.append(" else MatriculaPeriodoTurmaDisciplina.turma end =  MatriculaPeriodoTurmaDisciplina.turma ");			
			}
		}
		sqlStr.append(" order by regAula.data, regAula.codigo, matricula.tipomatricula desc , aluno.nome ");
//		sqlStr.append(" ) as t ");
//		System.out.println(sqlStr.toString());
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()); 	
		List<RegistroAulaVO> vetResultado = new ArrayList<RegistroAulaVO>(0);
		RegistroAulaVO obj = new RegistroAulaVO();
		Integer totalFaltas = 0;
		while (tabelaResultado.next()) {
			if (obj.getCodigo() == 0 || obj.getCodigo() != tabelaResultado.getInt("codigo")) {
				obj = new RegistroAulaVO();
				vetResultado.add(obj);
			}
			montarDadosCompletoDiario(obj, tabelaResultado);
			if(!tabelaResultado.getBoolean("frequenciaaula.presente") && !tabelaResultado.getBoolean("frequenciaaula.abonado") && (tabelaResultado.getDate("datatrancamento") == null || (tabelaResultado.getDate("datatrancamento") != null && tabelaResultado.getDate("datatrancamento").compareTo(tabelaResultado.getDate("data")) > 0))){
				totalFaltas++;
			}
		}
		Map<Integer, HistoricoVO> mapHistorico = new HashMap<Integer, HistoricoVO>(0);
		for(RegistroAulaVO registroAulaVO: vetResultado){
			
			for(FrequenciaAulaVO frequenciaAulaVO: registroAulaVO.getFrequenciaAulaVOs()){
				frequenciaAulaVO.setFaltasGeral(totalFaltas);
				if(Uteis.isAtributoPreenchido(frequenciaAulaVO.getHistoricoVO().getCodigo())){ 
					if(!mapHistorico.containsKey(frequenciaAulaVO.getHistoricoVO().getCodigo())){
						getFacadeFactory().getHistoricoFacade().carregarDadosNotaConceitoHistorico(frequenciaAulaVO.getHistoricoVO());
						mapHistorico.put(frequenciaAulaVO.getHistoricoVO().getCodigo(), frequenciaAulaVO.getHistoricoVO());
					}else{
						frequenciaAulaVO.setHistoricoVO(mapHistorico.get(frequenciaAulaVO.getHistoricoVO().getCodigo()));
					}
				}
			}
		}
		
		return vetResultado;
	}

	@Override
	public List<RegistroAulaVO> consultaRapidaPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupadaRelVersoVisaoAdministrativa(TurmaVO turmaVO, String semestre, String ano, List<ProfessorTitularDisciplinaTurmaVO> professores, Integer disciplina, Integer unidadeEnsino, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, String tipoAluno, boolean controlarAcesso, NivelMontarDados nivelMontarDados, Boolean liberarRegistroAulaEntrePeriodo, UsuarioVO usuario, FiltroRelatorioAcademicoVO filtroAcademicoVO, String mes, String anoMes, Integer configuracaoAcademico, Boolean trazerAlunoTransferido, String tipoFiltroPeriodo, Date dataInicio, Date dataFim) throws Exception {

		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();

		sqlStr.append(getSQLPadraoConsultaCompletaEspelhoDiario(turmaVO, filtroVisaoProfessor, liberarRegistroAulaEntrePeriodo, apenasAlunosAtivos, trazerAlunosPendentesFinanceiramente, tipoAluno, trazerAlunoTransferido));

		if (turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {
			sqlStr.append(" WHERE Turma.codigo = ").append(turmaVO.getCodigo());
			sqlStr.append(" and ((MatriculaPeriodoTurmaDisciplina.turma in (select turma from turmaAgrupada where turmaOrigem =  ").append(turmaVO.getCodigo()).append(") )");
			sqlStr.append(" or (MatriculaPeriodoTurmaDisciplina.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
			sqlStr.append(" or (MatriculaPeriodoTurmaDisciplina.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
			sqlStr.append(")   and regAula.turmaorigem is null  ");
		} else {
			sqlStr.append(" WHERE Turma.codigo = ").append(turmaVO.getCodigo());
		}
		if (!turmaVO.getSubturma() && !turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
		}
		sqlStr.append(" AND (regAula.disciplina = ").append(disciplina.intValue()).append(")");
		if (Uteis.isAtributoPreenchido(configuracaoAcademico)) {
			sqlStr.append(" AND historico.configuracaoacademico = ").append(configuracaoAcademico);
		}
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND (Turma.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(")");
		}
		if (professores != null && !professores.isEmpty()) {
			sqlStr.append(" AND (regAula.professor in (0 ");
			for (ProfessorTitularDisciplinaTurmaVO professor : professores) {
				sqlStr.append(", ").append(professor.getProfessor().getPessoa().getCodigo().intValue());
			}
			sqlStr.append(")) ");
		}

		if (!semestre.equals("") && !liberarRegistroAulaEntrePeriodo) {
			sqlStr.append(" AND (regAula.semestre = '").append(semestre).append("') ");
		}
		if (!ano.equals("") && !liberarRegistroAulaEntrePeriodo) {
			sqlStr.append(" AND (regAula.ano = '").append(ano).append("') ");
		}

		if (mes != null && !mes.trim().isEmpty() && anoMes != null && !anoMes.trim().isEmpty() && Uteis.isAtributoPreenchido(tipoFiltroPeriodo) && !tipoFiltroPeriodo.equals("porPeriodoData")) {
			sqlStr.append(" AND (extract(month from regAula.data)::INT = ").append(Integer.valueOf(mes)).append(") ");
			sqlStr.append(" AND (extract(year from regAula.data)::INT = ").append(Integer.valueOf(anoMes)).append(") ");
		}
		
		if (Uteis.isAtributoPreenchido(tipoFiltroPeriodo) && tipoFiltroPeriodo.equals("porPeriodoData")) {
			sqlStr.append(" AND (regAula.data::date BETWEEN '").append(dataInicio).append("' and '").append(dataFim).append(" ')");
		}

		sqlStr.append(" and CASE WHEN (MatriculaPeriodoTurmaDisciplina.codigo is not null) then Historico.situacao   not in ( 'AA', 'CC', 'CH', 'IS', 'AB')  else (1=1) end  ");
		
		
		sqlStr.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		sqlStr.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		if (filtroTipoCursoAluno != null && !filtroTipoCursoAluno.equals("") && !filtroTipoCursoAluno.equals("todos")) {
			if (filtroTipoCursoAluno.equals("posGraduacao")) {
				sqlStr.append(" AND matricula.tipomatricula = 'NO'");
			} else if (filtroTipoCursoAluno.equals("extensao")) {
				sqlStr.append(" AND matricula.tipoMatricula = 'EX' ");
			} else if (filtroTipoCursoAluno.equals("modular")) {
				sqlStr.append(" AND matricula.tipoMatricula = 'MO' ");
			}
		}
		if (tipoAluno.equals("normal")) {
			sqlStr.append(" and matriculaperiodoturmadisciplina.turma = matriculaperiodo.turma");
		} else if (tipoAluno.equals("reposicao")) {
			sqlStr.append(" and matriculaperiodoturmadisciplina.turma <> matriculaperiodo.turma");
		}
		sqlStr.append(" order by regAula.data, regAula.codigo ");		
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<RegistroAulaVO> vetResultado = new ArrayList<RegistroAulaVO>(0);
		RegistroAulaVO obj = new RegistroAulaVO();
		while (dadosSQL.next()) {
			obj = new RegistroAulaVO();
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.setData(dadosSQL.getDate("data"));
			obj.setDiaSemana(dadosSQL.getString("diaSemana"));
			obj.setSemestre(dadosSQL.getString("semestre"));
			obj.setHorario(dadosSQL.getString("horario"));
			obj.setAno(dadosSQL.getString("ano"));
			obj.setTipoAula(dadosSQL.getString("tipoAula"));
			obj.setCargaHoraria(dadosSQL.getInt("cargaHoraria"));
			obj.setConteudo(dadosSQL.getString("conteudo"));
			obj.setDataRegistroAula(dadosSQL.getDate("dataRegistroAula"));
			obj.setAtividadeComplementar(dadosSQL.getBoolean("atividadeComplementar"));
			obj.setPraticaSupervisionada(dadosSQL.getString("praticaSupervisionada"));
			// Dados da Turma
			obj.getTurma().setCodigo(dadosSQL.getInt("turma.codigo"));
			obj.getTurma().setIdentificadorTurma(dadosSQL.getString("turma.identificadorTurma"));
			obj.getTurma().setTurmaAgrupada(dadosSQL.getBoolean("turma.turmaAgrupada"));
			obj.getTurma().setSemestral(dadosSQL.getBoolean("turma.semestral"));
			// Dados da Unidade
			obj.getTurma().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
			obj.getTurma().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));
			// Dados do Curso
			obj.getTurma().getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
			obj.getTurma().getCurso().setNome(dadosSQL.getString("curso.nome"));
			obj.getTurma().getCurso().setNivelEducacional(dadosSQL.getString("curso.nivelEducacional"));
			obj.getTurma().getCurso().getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("curso.configuracaoAcademico"));
			// Dados do Turno
			obj.getTurma().getTurno().setNome(dadosSQL.getString("turno.nome"));
			// Dados do Periodo Letivo
			obj.getTurma().getPeridoLetivo().setCodigo(dadosSQL.getInt("periodoLetivo.codigo"));
			obj.getTurma().getPeridoLetivo().setDescricao(dadosSQL.getString("periodoLetivo.descricao"));
			// Dados da Pessoa
			obj.getProfessor().setCodigo(dadosSQL.getInt("professor.codigo"));
			obj.getProfessor().setNome(dadosSQL.getString("professor.nome"));
			// Dados do Usuario
			obj.getResponsavelRegistroAula().setCodigo(dadosSQL.getInt("responsavelRegistroAula.codigo"));
			obj.getResponsavelRegistroAula().setNome(dadosSQL.getString("responsavelRegistroAula.nome"));
			// Dados da Disciplinas
			obj.getDisciplina().setCodigo(dadosSQL.getInt("disciplina.codigo"));
			obj.getDisciplina().setNome(dadosSQL.getString("disciplina.nome"));
			obj.getGradeDisciplinaVO().setCargaHoraria(dadosSQL.getInt("gradedisciplina.cargaHoraria"));
			obj.getGradeDisciplinaVO().getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("gradedisciplina.configuracaoAcademico"));
			obj.setFrequenciaAulaVOs(getFacadeFactory().getFrequenciaAulaFacade().consultarMatriculaFrequenciaAulaPorRegistroAula(obj.getCodigo(), usuario));
			obj.setNovoObj(Boolean.FALSE);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public void validarDadosDiarioRegistroAula(Integer registroAula, Integer turma, UsuarioVO usuarioVO) throws Exception {
		Boolean possuiMatriculaPeriodoAtiva = getFacadeFactory().getMatriculaPeriodoFacade().consultarAlunoAtivoPorRegistroAula(registroAula, turma, usuarioVO);
		if (!possuiMatriculaPeriodoAtiva) {
			throw new Exception("Impossível realizar a operação, pois a situação dos alunos no período não está ativa.");
		}
		Boolean possuiMatriculaAtiva = getFacadeFactory().getMatriculaFacade().consultarMatriculaAtivaPorRegistroAula(registroAula, usuarioVO);
		if (!possuiMatriculaAtiva) {
			throw new Exception("Impossível realizar a operação, pois não existe nenhuma situação ativa na matrícula");
		}
	}

	public RegistroAulaVO consultaRapidaPorTurmaDataDiaSemanaDisciplinaHorario(Integer codigoTurma, Date dataAula, String diaSemana, Integer disciplina, String semestre, String ano, String horario, boolean referenteAulaComplementar, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT regAula.* FROM RegistroAula regAula");
		sqlStr.append(" LEFT JOIN Turma ON (regAula.turma = Turma.codigo) ");
		sqlStr.append(" WHERE (regAula.turma = ").append(codigoTurma).append(")");
		sqlStr.append(" and (regAula.data = '").append(Uteis.getDataJDBC(dataAula)).append("')");
		sqlStr.append(" and (regAula.diaSemana = '").append(diaSemana).append("')");
		sqlStr.append(" AND (regAula.disciplina = ").append(disciplina.intValue()).append(")");
		sqlStr.append(" AND (regAula.horario = '").append(horario).append("')");
		if (!semestre.equals("")) {
			sqlStr.append(" AND (regAula.semestre = '").append(semestre).append("') ");
		}
		if (!ano.equals("")) {
			sqlStr.append(" AND (regAula.ano = '").append(ano).append("') ");
		}
		sqlStr.append(" AND (regAula.atividadecomplementar = ").append(referenteAulaComplementar).append(") ");		
		sqlStr.append(" ORDER BY regAula.data");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		RegistroAulaVO obj = new RegistroAulaVO();
		if (tabelaResultado.next()) {
			// obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj = montarDadosRegistroAula(tabelaResultado);
		}
		return obj;
	}

	@Override
	public List<RegistroAulaVO> consultaRapidaPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupadaEReposicao(TurmaVO turmaVO, String semestre, String ano, List<ProfessorTitularDisciplinaTurmaVO> professores, Integer disciplina, Integer unidadeEnsino, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, boolean controlarAcesso, NivelMontarDados nivelMontarDados, FiltroRelatorioAcademicoVO filtroAcademicoVO, String mes, String anoMes, Integer configuracaoAcademico, UsuarioVO usuario, Boolean trazerAlunoTransferencia, boolean permitirRealizarLancamentoAlunosPreMatriculados, String tipoFiltroPeriodo, Date dataInicio, Date dataFim) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(getSQLPadraoConsultaCompletaDiarioReposicao(turmaVO, disciplina, trazerAlunoTransferencia));
		if (turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {
			sqlStr.append(" WHERE Turma.codigo = ").append(turmaVO.getCodigo());
			sqlStr.append(" and ((MatriculaPeriodoTurmaDisciplina.turma in (select turma from turmaAgrupada where turmaOrigem =  ").append(turmaVO.getCodigo()).append(") )");
			sqlStr.append(" or (MatriculaPeriodoTurmaDisciplina.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
			sqlStr.append(" or (MatriculaPeriodoTurmaDisciplina.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
			sqlStr.append(")   and regAula.turmaorigem is null  ");
		} else {
			sqlStr.append(" WHERE Turma.codigo = ").append(turmaVO.getCodigo());
		}
		if (!turmaVO.getSubturma() && !turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
		}		
		sqlStr.append(" AND (regAula.disciplina = ").append(disciplina.intValue()).append(")");
		if (Uteis.isAtributoPreenchido(configuracaoAcademico)) {
			sqlStr.append(" AND historico.configuracaoacademico = ").append(configuracaoAcademico);
		}
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND (Turma.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(")");
		}
		if (professores != null && !professores.isEmpty()) {
			sqlStr.append(" AND (regAula.professor in (0 ");
			for (ProfessorTitularDisciplinaTurmaVO professor : professores) {
				sqlStr.append(", ").append(professor.getProfessor().getPessoa().getCodigo().intValue());
			}
			sqlStr.append(")) ");
		}

		if (!semestre.equals("")) {
			sqlStr.append(" AND (regAula.semestre = '").append(semestre).append("') ");
		}
		if (!ano.equals("")) {
			sqlStr.append(" AND (regAula.ano = '").append(ano).append("') ");
		}
		if(turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.turma <> matriculaperiodo.turma ");
		}else {
			sqlStr.append(" and regAula.turma <> matriculaperiodo.turma ");
		}

		if (filtroAcademicoVO == null) {
			sqlStr.append(" and matricula.situacao = 'AT'");
		} else {
			sqlStr.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
			sqlStr.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		}
		
		if (usuario != null && (usuario.getIsApresentarVisaoProfessor() || usuario.getIsApresentarVisaoCoordenador()) && !permitirRealizarLancamentoAlunosPreMatriculados) {
			sqlStr.append(" and matriculaperiodo.situacaoMatriculaPeriodo <> 'PR' ");
		}

		if (mes != null && !mes.trim().isEmpty() && anoMes != null && !anoMes.trim().isEmpty() && Uteis.isAtributoPreenchido(tipoFiltroPeriodo) && !tipoFiltroPeriodo.equals("porPeriodoData")) {
			sqlStr.append(" AND (extract(month from regAula.data)::INT = ").append(Integer.valueOf(mes)).append(") ");
			sqlStr.append(" AND (extract(year from regAula.data)::INT = ").append(Integer.valueOf(anoMes)).append(") ");
		}
		
		if (Uteis.isAtributoPreenchido(tipoFiltroPeriodo) && tipoFiltroPeriodo.equals("porPeriodoData")) {
			sqlStr.append(" AND (regAula.data::date BETWEEN '").append(dataInicio).append("' and '").append(dataFim).append(" ')");
		}

		
		
		if (filtroTipoCursoAluno != null && !filtroTipoCursoAluno.equals("") && !filtroTipoCursoAluno.equals("todos")) {
			if (filtroTipoCursoAluno.equals("posGraduacao")) {
				sqlStr.append(" AND matricula.tipomatricula = 'NO'");
			} else if (filtroTipoCursoAluno.equals("extensao")) {
				sqlStr.append(" AND matricula.tipoMatricula = 'EX' ");
			} else if (filtroTipoCursoAluno.equals("modular")) {
				sqlStr.append(" AND matricula.tipoMatricula = 'MO' ");
			}
		}
		sqlStr.append(" order by regAula.data, regAula.nrAula, nomeAlunoSemAcento ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<RegistroAulaVO> vetResultado = new ArrayList<RegistroAulaVO>(0);
		RegistroAulaVO obj = new RegistroAulaVO();
		while (tabelaResultado.next()) {
			if (obj.getCodigo() == 0 || obj.getCodigo() != tabelaResultado.getInt("codigo")) {
				obj = new RegistroAulaVO();
				vetResultado.add(obj);
			}
			montarDadosCompletoReposicaoDiario(obj, tabelaResultado);
		}
		Map<Integer, HistoricoVO> mapHistorico = new HashMap<Integer, HistoricoVO>(0);
		for(RegistroAulaVO registroAulaVO: vetResultado){			
			for(FrequenciaAulaVO frequenciaAulaVO: registroAulaVO.getFrequenciaAulaVOs()){				
				if(Uteis.isAtributoPreenchido(frequenciaAulaVO.getHistoricoVO().getCodigo())){ 
					if(!mapHistorico.containsKey(frequenciaAulaVO.getHistoricoVO().getCodigo())){
						getFacadeFactory().getHistoricoFacade().carregarDadosNotaConceitoHistorico(frequenciaAulaVO.getHistoricoVO());
						mapHistorico.put(frequenciaAulaVO.getHistoricoVO().getCodigo(), frequenciaAulaVO.getHistoricoVO());
					}else{
						frequenciaAulaVO.setHistoricoVO(mapHistorico.get(frequenciaAulaVO.getHistoricoVO().getCodigo()));
					}
				}
			}
		}
		return vetResultado;
	}

	@Override
	public List<RegistroAulaVO> consultaRapidaPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupadaRelVerso(TurmaVO turmaVO, String semestre, String ano, List<ProfessorTitularDisciplinaTurmaVO> professores, Integer disciplina, Integer unidadeEnsino, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, String tipoAluno, boolean controlarAcesso, NivelMontarDados nivelMontarDados, Boolean liberarRegistroAulaEntrePeriodo, String mes, String anoMes, Integer configuracaoAcademico, UsuarioVO usuario, String tipoLayout, Boolean trazerAlunoTransferido, String tipoFiltroPeriodo, Date dataInicio, Date dataFim) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(getSQLPadraoConsultaCompletaEspelhoDiario(turmaVO, filtroVisaoProfessor, liberarRegistroAulaEntrePeriodo, apenasAlunosAtivos, trazerAlunosPendentesFinanceiramente, tipoAluno, trazerAlunoTransferido));
		if (turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {
			sqlStr.append(" WHERE Turma.codigo = ").append(turmaVO.getCodigo());
			sqlStr.append(" and ((MatriculaPeriodoTurmaDisciplina.turma in (select turma from turmaAgrupada where turmaOrigem =  ").append(turmaVO.getCodigo()).append(") )");
			sqlStr.append(" or (MatriculaPeriodoTurmaDisciplina.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
			sqlStr.append(" or (MatriculaPeriodoTurmaDisciplina.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
			sqlStr.append(")   and regAula.turmaorigem is null  ");
		} else {
			sqlStr.append(" WHERE Turma.codigo = ").append(turmaVO.getCodigo());
		}
		if (!turmaVO.getSubturma() && !turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
		}
		if (Uteis.isAtributoPreenchido(configuracaoAcademico)) {
			sqlStr.append(" AND configuracaoacademico.codigo = ").append(configuracaoAcademico);
		}
		sqlStr.append(" AND (regAula.disciplina = ").append(disciplina.intValue()).append(")");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND (Turma.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(")");
		}
		if (professores != null && !professores.isEmpty()) {
			sqlStr.append(" AND (regAula.professor in (0 ");
			for (ProfessorTitularDisciplinaTurmaVO professor : professores) {
				sqlStr.append(", ").append(professor.getProfessor().getPessoa().getCodigo().intValue());
			}
			sqlStr.append(")) ");
		}
		if (!semestre.equals("") && !liberarRegistroAulaEntrePeriodo) {
			sqlStr.append(" AND (regAula.semestre = '").append(semestre).append("') ");
		}
		if (!ano.equals("") && !liberarRegistroAulaEntrePeriodo) {
			sqlStr.append(" AND (regAula.ano = '").append(ano).append("') ");
		}
		if (mes != null && !mes.trim().isEmpty() && anoMes != null && !anoMes.trim().isEmpty() && Uteis.isAtributoPreenchido(tipoFiltroPeriodo) && !tipoFiltroPeriodo.equals("porPeriodoData")) {
			sqlStr.append(" AND (extract(month from regAula.data)::INT = ").append(Integer.valueOf(mes)).append(") ");
			sqlStr.append(" AND (extract(year from regAula.data)::INT = ").append(Integer.valueOf(anoMes)).append(") ");
		}
		
		if (Uteis.isAtributoPreenchido(tipoFiltroPeriodo) && tipoFiltroPeriodo.equals("porPeriodoData")) {
			sqlStr.append(" AND (regAula.data::date BETWEEN '").append(dataInicio).append("' and '").append(dataFim).append(" ')");
		}
		
		sqlStr.append(" and CASE WHEN (MatriculaPeriodoTurmaDisciplina.codigo is not null) then Historico.situacao   not in ( 'AA', 'CC', 'CH', 'IS', 'AB')  else (1=1) end  ");
		sqlStr.append(" and matriculaPeriodo.situacaomatriculaperiodo in ( 'AT', 'FI', 'FO' ) ");
		if (filtroVisaoProfessor != null && filtroVisaoProfessor) {
			// sqlStr.append(" and matricula.situacao = 'AT' and matriculaPeriodo.situacao <> 'PF' ");
		} else {
			sqlStr.append(" and matricula.situacao not in ('TR', 'CA') ");
		}
		if ((filtroVisaoProfessor != null && filtroVisaoProfessor) || ((filtroVisaoProfessor == null || !filtroVisaoProfessor) && (tipoLayout.equals("DiarioModRetratoRel") || tipoLayout.equals("DiarioRel") || ((apenasAlunosAtivos != null && apenasAlunosAtivos) && (trazerAlunosPendentesFinanceiramente == null || !trazerAlunosPendentesFinanceiramente))))) {
			sqlStr.append(" and matriculaPeriodo.situacaomatriculaperiodo <> 'PR' ");
		}
		
		
		if (filtroVisaoProfessor == null || !filtroVisaoProfessor) {
			if (apenasAlunosAtivos != null && apenasAlunosAtivos) {
				sqlStr.append(" and matricula.situacao = 'AT' ");
			}
		}
		if (Uteis.isAtributoPreenchido(trazerAlunosPendentesFinanceiramente) && !trazerAlunosPendentesFinanceiramente) {
			sqlStr.append(" and matriculaPeriodo.situacao <> 'PF' ");
		}
		if (filtroTipoCursoAluno != null && !filtroTipoCursoAluno.equals("") && !filtroTipoCursoAluno.equals("todos")) {
			if (filtroTipoCursoAluno.equals("posGraduacao")) {
				sqlStr.append(" AND matricula.tipomatricula = 'NO'");
			} else if (filtroTipoCursoAluno.equals("extensao")) {
				sqlStr.append(" AND matricula.tipoMatricula = 'EX' ");
			} else if (filtroTipoCursoAluno.equals("modular")) {
				sqlStr.append(" AND matricula.tipoMatricula = 'MO' ");
			}
		}
		if (tipoAluno.equals("normal")) {
			sqlStr.append(" and matriculaperiodoturmadisciplina.turma = matriculaperiodo.turma");
		} else if (tipoAluno.equals("reposicao")) {
			sqlStr.append(" and matriculaperiodoturmadisciplina.turma <> matriculaperiodo.turma");
		}
		if (filtroVisaoProfessor != null && filtroVisaoProfessor) {
			if (!liberarRegistroAulaEntrePeriodo) {
				sqlStr.append(" and matriculaperiodo.ano = regAula.ano and matriculaperiodo.semestre = regAula.semestre ");
			}
			if (tipoAluno.equals("normal")) {
				sqlStr.append(" and matriculaperiodo.turma = turma.codigo ");
			}
			if (tipoAluno.equals("reposicao")) {
				sqlStr.append(" and regAula.turma <> matriculaperiodo.turma ");
			}
		} else {
			if (!liberarRegistroAulaEntrePeriodo) {
				sqlStr.append(" and matriculaperiodo.ano = regAula.ano and matriculaperiodo.semestre = regAula.semestre ");
			}
			if (tipoAluno.equals("normal")) {
				sqlStr.append(" and matriculaperiodo.turma = turma.codigo ");
			}
			if (tipoAluno.equals("reposicao")) {
				sqlStr.append(" and regAula.turma <> matriculaperiodo.turma ");
			}
		}
		if (turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.disciplina = disciplina.codigo and MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula ");
		} else {
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.disciplina = disciplina.codigo and MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula ");
			if (tipoAluno.equals("reposicao")) {
				sqlStr.append(" and matriculaperiodoturmadisciplina.turma = turma.codigo ");
			}
		}
		if ((filtroVisaoProfessor != null && filtroVisaoProfessor) || ((filtroVisaoProfessor == null || !filtroVisaoProfessor) && (tipoLayout.equals("DiarioModRetratoRel") || tipoLayout.equals("DiarioRel") || ((apenasAlunosAtivos != null && apenasAlunosAtivos) && (trazerAlunosPendentesFinanceiramente == null || !trazerAlunosPendentesFinanceiramente))))) {
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		}
		sqlStr.append(" and case when curso.niveleducacional = 'PR' and curso.liberarRegistroAulaEntrePeriodo then " + turmaVO.getCodigo() + " else MatriculaPeriodoTurmaDisciplina.turma end = MatriculaPeriodoTurmaDisciplina.turma ");
		sqlStr.append(" order by regAula.data, regAula.codigo ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<RegistroAulaVO> vetResultado = new ArrayList<RegistroAulaVO>(0);
		RegistroAulaVO obj = new RegistroAulaVO();
		while (dadosSQL.next()) {
			obj = new RegistroAulaVO();
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.setData(dadosSQL.getDate("data"));
			obj.setDiaSemana(dadosSQL.getString("diaSemana"));
			obj.setSemestre(dadosSQL.getString("semestre"));
			obj.setHorario(dadosSQL.getString("horario"));
			obj.setNrAula(dadosSQL.getInt("nrAula"));
			obj.setAno(dadosSQL.getString("ano"));
			obj.setTipoAula(dadosSQL.getString("tipoAula"));
			obj.setCargaHoraria(dadosSQL.getInt("cargaHoraria"));
			obj.setConteudo(dadosSQL.getString("conteudo"));
			obj.setDataRegistroAula(dadosSQL.getDate("dataRegistroAula"));
			obj.setAtividadeComplementar(dadosSQL.getBoolean("atividadeComplementar"));
			obj.setPraticaSupervisionada(dadosSQL.getString("praticaSupervisionada"));
			// Dados da Turma
			obj.getTurma().setCodigo(dadosSQL.getInt("turma.codigo"));
			obj.getTurma().setIdentificadorTurma(dadosSQL.getString("turma.identificadorTurma"));
			obj.getTurma().setTurmaAgrupada(dadosSQL.getBoolean("turma.turmaAgrupada"));
			obj.getTurma().setSemestral(dadosSQL.getBoolean("turma.semestral"));
			// Dados da Unidade
			obj.getTurma().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
			obj.getTurma().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));
			// Dados do Curso
			obj.getTurma().getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
			obj.getTurma().getCurso().setNome(dadosSQL.getString("curso.nome"));
			obj.getTurma().getCurso().setNivelEducacional(dadosSQL.getString("curso.nivelEducacional"));
			obj.getTurma().getCurso().getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("curso.configuracaoAcademico"));
			// Dados do Turno
			obj.getTurma().getTurno().setNome(dadosSQL.getString("turno.nome"));
			// Dados do Periodo Letivo
			obj.getTurma().getPeridoLetivo().setCodigo(dadosSQL.getInt("periodoLetivo.codigo"));
			obj.getTurma().getPeridoLetivo().setDescricao(dadosSQL.getString("periodoLetivo.descricao"));
			// Dados da Pessoa
			obj.getProfessor().setCodigo(dadosSQL.getInt("professor.codigo"));
			obj.getProfessor().setNome(dadosSQL.getString("professor.nome"));
			// Dados do Usuario
			obj.getResponsavelRegistroAula().setCodigo(dadosSQL.getInt("responsavelRegistroAula.codigo"));
			obj.getResponsavelRegistroAula().setNome(dadosSQL.getString("responsavelRegistroAula.nome"));
			// Dados da Disciplinas
			obj.getDisciplina().setCodigo(dadosSQL.getInt("disciplina.codigo"));
			obj.getDisciplina().setNome(dadosSQL.getString("disciplina.nome"));
			obj.getGradeDisciplinaVO().setCargaHoraria(dadosSQL.getInt("gradedisciplina.cargaHoraria"));
			obj.getGradeDisciplinaVO().getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("gradedisciplina.configuracaoAcademico"));
			obj.setFrequenciaAulaVOs(getFacadeFactory().getFrequenciaAulaFacade().consultarMatriculaFrequenciaAulaPorRegistroAula(obj.getCodigo(), usuario));
			obj.setNovoObj(Boolean.FALSE);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<RegistroAulaVO> consultarPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupada(Integer codigoTurma, String semestre, String ano, Integer professor, Integer disciplina, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		List<TurmaVO> listaTurmasNaQualTurmaParticipadaAgrupamento = getFacadeFactory().getTurmaFacade().consultaRapidaTurmasNasQuaisTurmaParticipaDeAgrupamento(codigoTurma, false, usuario);

		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT RegistroAula.* FROM RegistroAula LEFT JOIN Turma ON (RegistroAula.turma = Turma.codigo) ");

		if (listaTurmasNaQualTurmaParticipadaAgrupamento.isEmpty()) {
			sqlStr.append(" WHERE (Turma.codigo = ").append(codigoTurma).append(")");
		} else {
			sqlStr.append(" WHERE ((Turma.codigo = ").append(codigoTurma).append(")");
			for (TurmaVO turmaAgrupadaConsiderarRegistroAula : listaTurmasNaQualTurmaParticipadaAgrupamento) {
				sqlStr.append(" OR (Turma.codigo = ").append(turmaAgrupadaConsiderarRegistroAula.getCodigo()).append(")");
			}
			sqlStr.append(" )");
		}

		sqlStr.append(" AND (RegistroAula.disciplina = ").append(disciplina.intValue()).append(")");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND (Turma.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(")");
		}
		if (!semestre.equals("")) {
			sqlStr.append(" AND (RegistroAula.semestre = '").append(semestre).append("') ");
		}
		if (!ano.equals("")) {
			sqlStr.append(" AND (RegistroAula.ano = '").append(ano).append("') ");
		}
		sqlStr.append(" ORDER BY registroaula.data");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<RegistroAulaVO> consultarPorIdentificadorTurmaProfessorDisciplina(TurmaVO turmaVO, String semestre, String ano, Integer professor, Integer disciplina, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, FiltroRelatorioAcademicoVO filtroAcademicoVO, String mes, String anoMes, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("SELECT RegistroAula.* FROM RegistroAula, Turma ");
		sqlStr.append("WHERE RegistroAula.turma = Turma.codigo ");
		sqlStr.append("and turma.codigo = '").append(turmaVO.getCodigo()).append("' ");
		sqlStr.append("and RegistroAula.disciplina = ").append(disciplina);
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and Turma.unidadeEnsino = ").append(unidadeEnsino);
		}
		if (!semestre.equals("")) {
			sqlStr.append(" and RegistroAula.semestre = '").append(semestre).append("' ");
		}
		if (!ano.equals("")) {
			sqlStr.append(" and RegistroAula.ano = '").append(ano).append("' ");
		}
		if (mes != null && !mes.trim().isEmpty() && anoMes != null && !anoMes.trim().isEmpty()) {
			sqlStr.append(" and (extract(month from RegistroAula.data)::INT = ").append(mes).append(") ");
			sqlStr.append(" and (extract(year from RegistroAula.data)::INT = ").append(anoMes).append(") ");
		}
//		sqlStr.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
//		sqlStr.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		sqlStr.append(" ORDER BY RegistroAula.data");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorIdentificadorTurmaProfessorDisciplinaEntreDatas(String identificadorTurma, String semestre, String ano, Integer professor, Integer disciplina, Integer unidadeEnsino, Date dataInicio, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {

		String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Turma WHERE RegistroAula.turma = Turma.codigo and lower (Turma.identificadorTurma) = '" + identificadorTurma.toLowerCase() + "' " + " and RegistroAula.disciplina = " + disciplina.intValue() + " and RegistroAula.data between '" + Uteis.getDataBD0000(dataInicio) + "' and '" + Uteis.getDataBD2359(dataFim) + "' ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and Turma.unidadeEnsino = " + unidadeEnsino.intValue();
		}
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}
		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}
		sqlStr += " ORDER BY registroaula.data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>RegistroAula</code>
	 * através do valor do atributo <code>String conteudo</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>RegistroAulaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorConteudo(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM RegistroAula WHERE conteudo like('" + valorConsulta + "%') ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * FROM RegistroAula, Turma WHERE RegistroAula.turma = Turma.codigo and conteudo like('" + valorConsulta + "%') and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}
		sqlStr += " ORDER BY conteudo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>RegistroAula</code>
	 * através do valor do atributo <code>Integer cargaHoraria</code>. Retorna
	 * os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho
	 * de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>RegistroAulaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCargaHoraria(Integer valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM RegistroAula WHERE cargaHoraria >= " + valorConsulta.intValue() + " ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * FROM RegistroAula, Turma WHERE RegistroAula.turma = Turma.codigo and cargaHoraria >= " + valorConsulta.intValue() + " and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}
		sqlStr += " ORDER BY cargaHoraria";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>RegistroAula</code>
	 * através do valor do atributo <code>Date data</code>. Retorna os objetos
	 * com valores pertecentes ao período informado por parâmetro. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>RegistroAulaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorData(Date prmIni, Date prmFim, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM RegistroAula WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * FROM RegistroAula, Turma  WHERE RegistroAula.turma = Turma.codigo and ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}
		sqlStr += " ORDER BY data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public RegistroAulaVO consultarUltimoRegistroAulaPorMatricula(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT registroaula.* ");
		sqlStr.append("FROM matricula ");
		sqlStr.append("INNER JOIN matriculaperiodo ON (matricula.matricula = matriculaperiodo.matricula) ");
		sqlStr.append("INNER JOIN registroaula ON (matriculaperiodo.turma = registroaula.turma AND matriculaperiodo.ano = registroaula.ano AND matriculaperiodo.semestre = registroaula.semestre) ");
		sqlStr.append("WHERE matricula.matricula like'").append(matricula).append("' ");
		sqlStr.append("Order BY registroaula.data ");
		sqlStr.append("limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		throw new ConsistirException("Não existe nenhum registro de aula para esse aluno no curso escolhido.");
	}

	public RegistroAulaVO consultarUltimoRegistroAulaPorTurmaProfessor(TurmaVO turma, PessoaVO professor, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select registroaula.* from registroaula ");
		sqlStr.append(" inner join turma on registroaula.turma = turma.codigo");
		sqlStr.append(" inner join pessoa on registroaula.professor = pessoa.codigo");
		sqlStr.append(" where turma.codigo = ? and registroaula.professor = ? ");
		sqlStr.append(" order by registroaula.data desc limit 1 ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), turma.getCodigo(), professor.getCodigo());
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		throw new ConsistirException("Não existe nenhum registro de aula para esse aluno no curso escolhido.");
	}

	public List consultarPorDataProfessor(Date prmIni, Date prmFim, String semestre, String ano, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE (data >= '");
		sqlStr.append(Uteis.getDataJDBC(prmIni));
		sqlStr.append("')");
		sqlStr.append(" and (data <= '");
		sqlStr.append(Uteis.getDataJDBC(prmFim));
		sqlStr.append("')");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND turma.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (!semestre.equals("")) {
			sqlStr.append(" and RegistroAula.semestre = '");
			sqlStr.append(semestre);
			sqlStr.append("'");
		}
		if (!ano.equals("")) {
			sqlStr.append(" and RegistroAula.ano = '");
			sqlStr.append(ano);
			sqlStr.append("'");
		}
		sqlStr.append(" and Pessoa.codigo = ");
		sqlStr.append(professor);
		sqlStr.append(" ORDER BY regAula.data");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)	
	public void atualizarNovaDisciplinaRegistroAulaPorAlteracaoGradeCurricularIntegral(Integer turma, Integer disciplina, Integer novaDisciplina, UsuarioVO usuario) throws Exception {
		String sql = "update registroaula set disciplina = ?  WHERE turma = ? AND disciplina = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] {novaDisciplina, turma, disciplina});
	}
	
	public boolean existeRegistroAula(TurmaVO turma, DisciplinaVO disciplina)  {
		String sql = "SELECT codigo FROM registroaula WHERE turma = ? AND disciplina = ? ";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] {turma.getCodigo(), disciplina.getCodigo()});
		return rs.next();
	}
	
	public boolean existeRegistroAula(RegistroAulaVO ra) throws Exception {
		String sql = "SELECT dataregistroaula, responsavelregistroaula, turma, conteudo, " + "cargahoraria, data, codigo, disciplina, diaSemana, tipoaula , horario " + "FROM registroaula " + "WHERE data = ? " + "AND turma = ? " + "AND disciplina = ? and diaSemana = ? " + "AND codigo != ?";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { Uteis.getDataJDBC(ra.getData()), ra.getTurma().getCodigo().intValue(), ra.getDisciplina().getCodigo().intValue(), ra.getDiaSemana(), ra.getCodigo().intValue() });
		if (rs.next()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>RegistroAula</code>
	 * através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>RegistroAulaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM RegistroAula WHERE codigo >= " + valorConsulta.intValue() + " ";
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}
		sqlStr += " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>RegistroAulaVO</code> resultantes da consulta.
	 */
	public static List<RegistroAulaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<RegistroAulaVO> vetResultado = new ArrayList<RegistroAulaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>RegistroAulaVO</code>.
	 * 
	 * @return O objeto da classe <code>RegistroAulaVO</code> com os dados
	 *         devidamente montados.
	 */
	public static RegistroAulaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		RegistroAulaVO obj = new RegistroAulaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getDate("data"));
		obj.getTurma().setCodigo(new Integer(dadosSQL.getInt("turma")));
		obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
		obj.setCargaHoraria(new Integer(dadosSQL.getInt("cargaHoraria")));
		obj.setConteudo(dadosSQL.getString("conteudo"));
		obj.getResponsavelRegistroAula().setCodigo(new Integer(dadosSQL.getInt("responsavelRegistroAula")));
		obj.setDataRegistroAula(dadosSQL.getDate("dataRegistroAula"));
		obj.getProfessor().setCodigo(new Integer(dadosSQL.getInt("professor")));
		obj.setDiaSemana(dadosSQL.getString("diaSemana"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setHorario(dadosSQL.getString("horario"));
		obj.setNrAula(dadosSQL.getInt("nrAula"));
		obj.setTipoAula(dadosSQL.getString("tipoAula"));
		obj.setAtividadeComplementar(dadosSQL.getBoolean("atividadeComplementar"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			montarDadosTurma(obj, nivelMontarDados, usuario);
			montarDadosDisciplina(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			return obj;
		}
		obj.setFrequenciaAulaVOs(FrequenciaAula.consultarFrequenciaAulas(obj.getCodigo(), false, nivelMontarDados, usuario));
		montarDadosTurma(obj, nivelMontarDados, usuario);
		montarDadosDisciplina(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosProfessor(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosResponsavelRegistroAula(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		return obj;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>RegistroAulaVO</code>.
	 * 
	 * @return O objeto da classe <code>RegistroAulaVO</code> com os dados
	 *         devidamente montados.
	 */
	public static RegistroAulaVO montarDadosBasico(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		RegistroAulaVO obj = new RegistroAulaVO();
		obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
		obj.setData(tabelaResultado.getDate("data"));
		obj.getTurma().setCodigo(new Integer(tabelaResultado.getInt("turma")));
		obj.getDisciplina().setCodigo(new Integer(tabelaResultado.getInt("disciplina")));
		obj.setCargaHoraria(new Integer(tabelaResultado.getInt("cargaHoraria")));
		obj.setConteudo(tabelaResultado.getString("conteudo"));
		obj.getResponsavelRegistroAula().setCodigo(new Integer(tabelaResultado.getInt("responsavelRegistroAula")));
		obj.setDataRegistroAula(tabelaResultado.getDate("dataRegistroAula"));
		obj.getProfessor().setCodigo(new Integer(tabelaResultado.getInt("professor")));
		obj.setDiaSemana(tabelaResultado.getString("diaSemana"));
		obj.setSemestre(tabelaResultado.getString("semestre"));
		obj.setAno(tabelaResultado.getString("ano"));
		obj.setHorario(tabelaResultado.getString("horario"));
		obj.setTipoAula(tabelaResultado.getString("tipoAula"));
		obj.setNrAula(tabelaResultado.getInt("nrAula"));
		obj.setPraticaSupervisionada(tabelaResultado.getString("praticaSupervisionada"));
		obj.setNovoObj(Boolean.FALSE);
		obj.setFrequenciaAulaVOs(FrequenciaAula.consultarFrequenciaAulasComNomeAlunoSituacaoMatriculaPeriodo(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PessoaVO</code> relacionado ao objeto <code>RegistroAulaVO</code>.
	 * Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavelRegistroAula(RegistroAulaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelRegistroAula().getCodigo().intValue() == 0) {
			obj.setResponsavelRegistroAula(new UsuarioVO());
			return;
		}
		obj.setResponsavelRegistroAula(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelRegistroAula().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>ProgramacaoAulaVO</code> relacionado ao objeto
	 * <code>RegistroAulaVO</code>. Faz uso da chave primária da classe
	 * <code>ProgramacaoAulaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosTurma(RegistroAulaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getTurma().getCodigo().intValue() == 0) {
			obj.setTurma(new TurmaVO());
			return;
		}
		obj.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurma().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosProfessor(RegistroAulaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getProfessor().getCodigo().intValue() == 0) {
			obj.setProfessor(new PessoaVO());
			return;
		}
		obj.setProfessor(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getProfessor().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosDisciplina(RegistroAulaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getDisciplina().getCodigo().intValue() == 0) {
			obj.setDisciplina(new DisciplinaVO());
			return;
		}
		obj.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplina().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>RegistroAulaVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public RegistroAulaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM RegistroAula WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public Integer consultarSomaCargaHorarioDisciplina(Integer turma, String semestre, String ano, Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		return consultarSomaCargaHorarioDisciplina(turma, semestre, ano, disciplina, controlarAcesso, usuario, false);
	}

	public Integer consultarSomaCargaHorarioDisciplina(Integer turma, String semestre, String ano, Integer disciplina, boolean controlarAcesso, UsuarioVO usuario, boolean validarConfiguracaoCurso) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder("select SUM(soma) AS soma from (");

		if (validarConfiguracaoCurso) {			
			sql.append(" select sum(case when turno.considerarhoraaulasessentaminutosgeracaodiario then 60 else registroaula.cargahoraria end) as soma from registroaula ");
		} else {			
			sql.append(" select SUM(registroaula.cargahoraria) as soma from registroaula ");
		}
		sql.append(" inner join turma on turma.codigo = registroaula.turma ");
		sql.append(" inner join disciplina on disciplina.codigo = registroaula.disciplina ");
		if (validarConfiguracaoCurso) {
			sql.append(" inner join turno on turma.turno = turno.codigo");
		}
		sql.append(" left join turma turmaorigem on turmaorigem.codigo = registroaula.turmaorigem ");
		sql.append(" where turma.codigo = ").append(turma);
		sql.append(" and (disciplina.codigo = ").append(disciplina);
		sql.append(" or (turma.turmaagrupada and disciplina.codigo in (select distinct disciplinaequivalenteTurmaagrupada from turmadisciplina where turma = ").append(turma).append(" and turmadisciplina.disciplina = ").append(disciplina.intValue()).append(")) ");
		sql.append(" or (turma.turmaagrupada and disciplina.codigo in (select distinct equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina.intValue()).append(")) ");
		sql.append(" or (turma.turmaagrupada and disciplina.codigo in (select distinct disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina.intValue()).append(")) ");
		sql.append(" ) ");
		
		sql.append(" and ((turmaorigem.codigo is null) or (turmaorigem.subturma and turmaorigem.tiposubturma = 'GERAL') or (turmaorigem.turmaagrupada and turmaorigem.subturma = false) or (turmaorigem.turmaagrupada and turmaorigem.subturma and turmaorigem.tiposubturma = 'TEORICA' ) or (turmaorigem.turmaagrupada and turmaorigem.subturma and turmaorigem.tiposubturma = 'PRATICA' )) ");		
		if (semestre != null && !semestre.equals("")) {
			sql.append(" and registroAula.semestre = '").append(semestre).append("' ");
		}
		if (ano != null && !ano.equals("")) {
			sql.append(" and registroAula.ano = '").append(ano).append("' ");
		}
		sql.append(" and atividadecomplementar = false ");
		sql.append(" union all ");
		sql.append(" select SUM(registroaula.cargahoraria * 60) as soma from registroaula ");
		sql.append(" inner join turma on turma.codigo = registroaula.turma ");
		sql.append(" inner join disciplina on disciplina.codigo = registroaula.disciplina ");
		sql.append(" left join turma turmaorigem on turmaorigem.codigo = registroaula.turmaorigem ");
		sql.append(" where turma.codigo = ").append(turma);
		sql.append(" and (disciplina.codigo = ").append(disciplina);
		sql.append(" or (turma.turmaagrupada and disciplina.codigo in (select distinct disciplinaequivalenteTurmaagrupada from turmadisciplina where turma = ").append(turma).append(" and turmadisciplina.disciplina = ").append(disciplina.intValue()).append(")) ");
		sql.append(" or (turma.turmaagrupada and disciplina.codigo in (select distinct equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina.intValue()).append(")) ");
		sql.append(" or (turma.turmaagrupada and disciplina.codigo in (select distinct disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina.intValue()).append(")) ");
		sql.append(" ) ");
		sql.append(" and ((turmaorigem.codigo is null) or (turmaorigem.subturma and turmaorigem.tiposubturma = 'GERAL') or (turmaorigem.turmaagrupada and turmaorigem.subturma = false) or (turmaorigem.turmaagrupada and turmaorigem.subturma and turmaorigem.tiposubturma = 'TEORICA' ) or (turmaorigem.turmaagrupada and turmaorigem.subturma and turmaorigem.tiposubturma = 'PRATICA' )) ");
		if (semestre != null && !semestre.equals("")) {
			sql.append(" and registroAula.semestre = '").append(semestre).append("' ");
		}
		if (ano != null && !ano.equals("")) {
			sql.append(" and registroAula.ano = '").append(ano).append("' ");
		}
		sql.append(" and atividadecomplementar = true ");
		sql.append(") as t ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			return 0;
		}
		return (new Integer(tabelaResultado.getInt("soma")));
	}

	public Integer consultarSomaCargaHorarioDisciplinaMinistradaPorOutroProfessor(Integer turma, Integer professor, String semestre, String ano, Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder("select SUM(registroaula.cargahoraria) as soma from registroaula, turma, disciplina ");
		sql.append(" where registroaula.turma = turma.codigo and turma.codigo = ").append(turma);
		sql.append(" and registroaula.disciplina = disciplina.codigo and disciplina.codigo = ").append(disciplina);
		sql.append(" and registroaula.professor != ").append(professor);
		if (semestre != null && !semestre.equals("")) {
			sql.append(" and registroAula.semestre = '").append(semestre).append("' ");
		}
		if (ano != null && !ano.equals("")) {
			sql.append(" and registroAula.ano = '").append(ano).append("' ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			return 0;
		}
		return (new Integer(tabelaResultado.getInt("soma")));
	}

	public Integer consultarSomaCargaHorarioDisciplinaComposta(Integer turma, String semestre, String ano, Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder("select SUM(registroaula.cargahoraria) as soma from registroaula, turma, disciplina, disciplinaComposta ");
		sql.append(" where registroaula.turma = turma.codigo and turma.codigo = ").append(turma);
		sql.append(" and registroaula.disciplina = disciplina.codigo and disciplinaComposta.composta = disciplina.codigo and disciplinaComposta.disciplina = ").append(disciplina);
		if (semestre != null && !semestre.equals("")) {
			sql.append(" and registroAula.semestre = '").append(semestre).append("' ");
		}
		if (ano != null && !ano.equals("")) {
			sql.append(" and registroAula.ano = '").append(ano).append("' ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			return 0;
		}
		return (new Integer(tabelaResultado.getInt("soma")));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return RegistroAula.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		RegistroAula.idEntidade = idEntidade;
	}

	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT regAula.codigo, regAula.data, regAula.cargaHoraria, regAula.horario, regAula.nrAula,  turma.codigo AS \"turma.codigo\", turma.identificadorTurma AS \"turma.identificadorTurma\", ");
		str.append("disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\" FROM registroAula AS regAula ");
		str.append("LEFT JOIN turma on turma.codigo = regAula.turma ");
		str.append("LEFT JOIN disciplina on disciplina.codigo = regAula.disciplina ");
		str.append("LEFT JOIN pessoa on pessoa.codigo = regAula.professor ");
		str.append("LEFT JOIN curso on curso.codigo = turma.curso ");
		return str;
	}

	/**
	 * Método responsavel por fazer uma seleção completa da Entidade Funcionário
	 * e mais algumas outras entidades que possuem relacionamento com a mesma. É
	 * uma consulta que busca completa e padrão.
	 * 
	 * @return List Contendo vários objetos da classe
	 * @author Carlos
	 */
	private StringBuffer getSQLPadraoConsultaCompleta() {
		StringBuffer str = new StringBuffer();
		// Dados do Registro de Aula
		str.append("SELECT DISTINCT regAula.codigo, regAula.data, regAula.horario,  regAula.nrAula, regAula.diaSemana, regAula.semestre, regAula.ano, regAula.tipoAula, regAula.cargaHoraria, regAula.conteudo, regAula.dataRegistroAula, regAula.atividadeComplementar, regAula.praticaSupervisionada, ");
		str.append("turma.codigo AS \"turma.codigo\", turma.identificadorTurma AS \"turma.identificadorTurma\", turma.turmaAgrupada AS \"turma.turmaAgrupada\", turma.semestral AS \"turma.semestral\", ");
		// Dados do Curso
		str.append(" curso.nome AS \"curso.nome\", curso.codigo AS \"curso.codigo\", curso.nivelEducacional AS \"curso.nivelEducacional\", curso.configuracaoAcademico AS \"curso.configuracaoAcademico\",  ");
		// Dados do Turno
		str.append(" turno.nome AS \"turno.nome\", ");
		// Dados do Periodo Letivo
		str.append(" periodoLetivo.codigo AS \"periodoLetivo.codigo\", periodoLetivo.descricao AS \"periodoLetivo.descricao\", ");
		// Dados da Pessoa
		str.append(" pessoa.codigo AS \"professor.codigo\", pessoa.nome AS \"professor.nome\", ");
		// Dados do Usuario
		str.append(" usuario.nome AS \"responsavelRegistroAula.nome\", usuario.codigo AS \"responsavelRegistroAula.codigo\", ");
		// Dados da Matrícula
		str.append(" matricula.matricula AS \"matricula.matricula\", matricula.updated AS \"matricula.updated\", matricula.data AS \"matricula.data\", matricula.tipomatricula AS \"matricula.tipoMatricula\",");
		// str.append(" matricula.matricula AS \"matricula.matricula\", matricula.updated AS \"matricula.updated\", matricula.extensao AS \"matricula.extensao\", ");
		// Dados do Aluno
		str.append(" aluno.nome AS \"aluno.nome\", ");
		// Dados da Disciplina
		str.append(" disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\",  ");
		// Dados da Unidade
		str.append(" unidadeEnsino.codigo AS \"unidadeEnsino.codigo\", unidadeEnsino.nome AS \"unidadeEnsino.nome\", ");
		// Dados da Disciplina
		str.append(" frequenciaaula.presente AS \"frequenciaaula.presente\", frequenciaaula.abonado AS \"frequenciaaula.abonado\", frequenciaaula.justificado AS \"frequenciaaula.justificado\" , ");
		str.append(" arquivo.codigo as \"codigoarquivo\" , arquivo.nome as \"nomearquivo\", ");
		str.append(" matriculaperiodoturmadisciplina.codigo as \"matriculaperiodoturmadisciplina.codigo\", matriculaperiodoturmadisciplina.turma as \"matriculaperiodoturmadisciplina.turma\", ");
		str.append(" matriculaperiodoturmadisciplina.turmaTeorica as \"matriculaperiodoturmadisciplina.turmaTeorica\", matriculaperiodoturmadisciplina.turmaPratica as \"matriculaperiodoturmadisciplina.turmaPratica\", ");
		str.append(" matriculaperiodoturmadisciplina.disciplina as \"matriculaperiodoturmadisciplina.disciplina\" ");
		str.append(" FROM registroAula AS regAula ");
		str.append(" LEFT JOIN turma on turma.codigo = regAula.turma ");
		str.append(" LEFT JOIN turmaagrupada ON turmaagrupada.turmaorigem = turma.codigo ");
		str.append(" LEFT JOIN turma t1 ON t1.codigo = turmaagrupada.turma ");
		str.append(" LEFT JOIN disciplina on disciplina.codigo = regAula.disciplina ");
		str.append(" LEFT JOIN pessoa on pessoa.codigo = regAula.professor ");
		// Dados do Curso proveniente da turma, para turma agrupada não
		// funciona, pois na mesma não é salvo o código do curso.
		// str.append(" LEFT JOIN curso on curso.codigo = turma.curso ");
		str.append(" LEFT JOIN unidadeEnsino on unidadeEnsino.codigo = turma.unidadeEnsino ");
		str.append(" LEFT JOIN turno on turno.codigo = turma.turno ");
		str.append(" LEFT JOIN periodoLetivo on periodoLetivo.codigo = turma.periodoLetivo ");
		str.append(" LEFT JOIN usuario on usuario.codigo = regAula.responsavelRegistroAula ");
		str.append(" LEFT JOIN frequenciaaula on frequenciaaula.registroaula = regAula.codigo  ");
		str.append(" LEFT JOIN matricula on matricula.matricula = frequenciaAula.matricula   ");		
		// Dados do Curso provenientes da Matricula, pois turma agrupada não
		// registra o código do curso, sendo assim não é possivel chegar a
		// configuração academica.
		str.append(" LEFT JOIN curso on curso.codigo = matricula.curso ");
		str.append(" LEFT JOIN configuracaoAcademico on configuracaoAcademico.codigo = curso.configuracaoAcademico ");
		str.append(" LEFT JOIN pessoa AS \"aluno\" on aluno.codigo = matricula.aluno  ");
		// LEFT JOIN PARA TRAZER O ARQUIVO DE IMAGEM DO PERFIL DA PESSOA
		str.append(" LEFT JOIN arquivo on aluno.arquivoimagem = arquivo.codigo ");
		return str;
	}

	private StringBuffer getSQLPadraoConsultaCompletaDiario(TurmaVO turmaVO, Boolean filtroVisaoProfessor, Boolean liberarRegistroAulaEntrePeriodo, String tipoLayout, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, String tipoAluno, String semestre, String ano, List<ProfessorTitularDisciplinaTurmaVO> professores, Integer disciplina, Boolean trazerAlunoTransferido) {
		StringBuffer str = new StringBuffer();
		// Dados do Registro de Aula
		str.append(" SELECT DISTINCT ");
//		str.append(" (select distinct count(rg.codigo) from registroaula rg ");
//		str.append(" inner join frequenciaaula on frequenciaaula.registroaula = rg.codigo ");
//		if (turmaVO.getCodigo() != null && !turmaVO.getCodigo().equals(0)) {
//			str.append(" and (rg.turma = ").append(turmaVO.getCodigo()).append(" or rg.turma in (select turma from turmaagrupada where  turmaorigem = ").append(turmaVO.getCodigo()).append(")) ");
//		}
//		if (disciplina != null && !disciplina.equals(0)) {
//			str.append(" and rg.disciplina = ").append(disciplina);
//		}
//		if (professores != null && !professores.isEmpty()) {
//			str.append(" AND (rg.professor in (0 ");
//			for (ProfessorTitularDisciplinaTurmaVO professor : professores) {
//				str.append(", ").append(professor.getProfessor().getPessoa().getCodigo().intValue());
//			}
//			str.append(")) ");
//		}
//		if (ano != null && !ano.equals("")) {
//			str.append(" and rg.ano = '").append(ano).append("' ");
//		}
//		if (semestre != null && !semestre.equals("")) {
//			str.append(" and rg.semestre = '").append(semestre).append("' ");
//		}
//		str.append(" and frequenciaaula.matricula = matricula.matricula and frequenciaaula.presente = false and frequenciaaula.abonado = false) as faltasgeral, ");
		str.append("regAula.codigo, regAula.data, regAula.horario, regAula.nrAula, regAula.diaSemana, regAula.semestre, regAula.ano, regAula.tipoAula, regAula.cargaHoraria, regAula.conteudo, regAula.dataRegistroAula, regAula.atividadeComplementar, regAula.praticaSupervisionada, ");
		str.append("turma.codigo AS \"turma.codigo\", turma.identificadorTurma AS \"turma.identificadorTurma\", turma.turmaAgrupada AS \"turma.turmaAgrupada\", turma.semestral AS \"turma.semestral\", ");
		// Dados do Curso
		str.append(" curso.nome AS \"curso.nome\", curso.codigo AS \"curso.codigo\", curso.nivelEducacional AS \"curso.nivelEducacional\", curso.configuracaoAcademico AS \"curso.configuracaoAcademico\",  ");
		// Dados do Turno
		str.append(" turno.nome AS \"turno.nome\", ");
		// Dados do Periodo Letivo
		str.append(" periodoLetivo.codigo AS \"periodoLetivo.codigo\", periodoLetivo.descricao AS \"periodoLetivo.descricao\", ");
		// Dados da Pessoa
		str.append(" pessoa.codigo AS \"professor.codigo\", pessoa.nome AS \"professor.nome\", ");
		// Dados do Usuario
		str.append(" usuario.nome AS \"responsavelRegistroAula.nome\", usuario.codigo AS \"responsavelRegistroAula.codigo\", ");
		// Dados da Matrícula
		str.append(" matricula.matricula AS \"matricula_matricula\", matricula.updated AS \"matricula.updated\", matricula.data AS \"matricula.data\", matricula.tipomatricula AS \"matricula.tipoMatricula\", matricula.situacao as \"matricula.situacao\", ");
		// str.append(" matricula.matricula AS \"matricula.matricula\", matricula.updated AS \"matricula.updated\", matricula.extensao AS \"matricula.extensao\", ");
		// Dados do Aluno
		str.append(" aluno.nome AS \"aluno.nome\", sem_acentos(aluno.nome) as nomeAlunoSemAcento, ");
		// Dados da Disciplina
		str.append(" disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", gradedisciplina.cargaHoraria AS \"gradedisciplina.cargaHoraria\", gradedisciplina.configuracaoAcademico AS \"gradedisciplina.configuracaoAcademico\", ");
		// Dados da Unidade
		str.append(" unidadeEnsino.codigo AS \"unidadeEnsino.codigo\", unidadeEnsino.nome AS \"unidadeEnsino.nome\", ");
		// Dados da Disciplina
		str.append(" frequenciaaula.presente AS \"frequenciaaula.presente\", frequenciaaula.abonado AS \"frequenciaaula.abonado\", frequenciaaula.justificado AS \"frequenciaaula.justificado\",  ");
		str.append(" MatriculaPeriodoTurmaDisciplina.codigo as matriculaPeriodoTurmaDisciplina, matriculaperiodo.codigo as matriculaperiodo, matriculaperiodo.situacaoMatriculaPeriodo, ");
		str.append(" historico.codigo as historico, configuracaoacademico.codigo as configuracaoacademico, historico.tipoHistorico, historico.instituicao, historico.situacao, historico.freguencia, historico.dataRegistro, ");			
		str.append(" historico.mediaFinal, historico.transferenciaEntradaDisciplinasAproveitadas, historico.mediafinalconceito, ");		
		for(int x = 1; x<=40;x++) {
			str.append(" historico.nota").append(x).append(", ");
			str.append(" historico.nota").append(x).append("lancada, ");
			str.append(" historico.nota").append(x).append("conceito, ");
		}
		str.append(" historico.updated, historico.historicoDisciplinaFazParteComposicao, anohistorico, semestrehistorico, ");
		
		str.append(" quantidadeCasasDecimaisPermitirAposVirgula, tipojustificativafalta.sigla ");
		str.append(" FROM registroAula AS regAula ");
		str.append(" INNER JOIN frequenciaaula on frequenciaaula.registroaula = regAula.codigo ");
		str.append(" INNER JOIN matricula on matricula.matricula = frequenciaAula.matricula ");
		str.append(" INNER join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		str.append(" INNER JOIN turma on turma.codigo = regAula.turma ");
		str.append(" INNER JOIN disciplina on disciplina.codigo = regAula.disciplina ");
		str.append("INNER JOIN MatriculaPeriodoTurmaDisciplina ON MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula ");
		if (turmaVO.getSubturma()) {
			if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
				str.append("and turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaTeorica ");
			} else if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
				str.append("and turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaPratica ");
			} else {
				str.append("and MatriculaPeriodoTurmaDisciplina.turma = turma.codigo ");
			}
		}
		if (!turmaVO.getTurmaAgrupada()) {
			str.append(" and MatriculaPeriodoTurmaDisciplina.disciplina = disciplina.codigo ");
			if (!turmaVO.getSubturma()) {
				str.append("and MatriculaPeriodoTurmaDisciplina.turma = turma.codigo ");
			}
		}else {
			str.append(" and (MatriculaPeriodoTurmaDisciplina.disciplina = ").append(disciplina.intValue());
			str.append(" or MatriculaPeriodoTurmaDisciplina.disciplina in (select disciplinaequivalenteturmaagrupada from turmadisciplina where turmadisciplina.disciplina = ").append(disciplina.intValue());
			str.append(" and turmadisciplina.turma = ").append(turmaVO.getCodigo()).append(") ");
			str.append(" or MatriculaPeriodoTurmaDisciplina.disciplina in (select equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina.intValue()).append(") ");
			str.append(" or MatriculaPeriodoTurmaDisciplina.disciplina in (select disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina.intValue()).append(") ");
			str.append(" ) ");
		}
		str.append(" and MatriculaPeriodoTurmaDisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		str.append(" LEFT JOIN pessoa on pessoa.codigo = regAula.professor ");
		// Dados do Curso proveniente da turma, para turma agrupada não
		// funciona, pois na mesma não é salvo o código do curso.
		// str.append(" LEFT JOIN curso on curso.codigo = turma.curso ");
		str.append(" LEFT JOIN unidadeEnsino on unidadeEnsino.codigo = turma.unidadeEnsino ");
		str.append(" LEFT JOIN turno on turno.codigo = turma.turno ");
		str.append(" LEFT JOIN periodoLetivo on periodoLetivo.codigo = turma.periodoLetivo ");
		str.append(" LEFT JOIN gradedisciplina on gradedisciplina.periodoletivo = periodoLetivo.codigo and gradedisciplina.disciplina = disciplina.codigo ");
		str.append(" LEFT JOIN usuario on usuario.codigo = regAula.responsavelRegistroAula ");

		// Dados do Curso provenientes da Matricula, pois turma agrupada não
		// registra o código do curso, sendo assim não é possivel chegar a
		// configuração academica.
		str.append(" LEFT JOIN curso on curso.codigo = matricula.curso ");

		str.append(" LEFT JOIN pessoa AS \"aluno\" on aluno.codigo = matricula.aluno  ");

		str.append(" inner JOIN Historico ON Historico.matricula = matricula.matricula and historico.matriculaperiodo = matriculaperiodo.codigo and Historico.MatriculaPeriodoTurmaDisciplina = MatriculaPeriodoTurmaDisciplina.codigo  ");
		str.append(" and historico.codigo = (select his.codigo from historico his where his.matricula = matricula.matricula and his.matriculaperiodo = matriculaperiodo.codigo and his.MatriculaPeriodoTurmaDisciplina = MatriculaPeriodoTurmaDisciplina.codigo order by case when his.matrizcurricular = matricula.gradecurricularatual then 0 else 1 end limit 1 ) ");
		// str.append(" LEFT JOIN Historico ON Historico.matricula = matricula.matricula  ");
		// str.append(" and Historico.disciplina = MatriculaPeriodoTurmaDisciplina.disciplina ");
		// str.append(" and  Historico.matriculaperiodo = matriculaperiodo.codigo ");
		/**
		 * Adicionada regra para resolver impactos relacionados a alunos que
		 * estão Cursando por Correspondência e que disciplinas saiam duplicadas
		 * no Boletim Acadêmico
		 */
		if (!trazerAlunoTransferido) {
			str.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		}else {
			str.append(" and (").append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" "));
			str.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" or ")).append(" = false )");
		}	

		str.append(" LEFT JOIN configuracaoAcademico on configuracaoAcademico.codigo = Historico.configuracaoAcademico ");
		str.append(" left join disciplinaabono on regAula.codigo = disciplinaabono.registroaula and disciplinaabono.matricula = frequenciaaula.matricula and disciplinaabono.disciplina = disciplina.codigo ");
		str.append(" left join abonofalta on disciplinaabono.abonofalta = abonofalta.codigo ");
		str.append(" left join tipojustificativafalta on tipojustificativafalta.codigo = abonofalta.tipojustificativafalta ");

		return str;
	}

	private StringBuffer getSQLPadraoConsultaCompletaDiarioVisaoAdminstrativa(TurmaVO turmaVO, Boolean liberarRegistroAulaEntrePeriodo, String tipoLayout, String tipoAluno, String semestre, String ano, List<ProfessorTitularDisciplinaTurmaVO> professores, Integer disciplina, Boolean trazerAlunoTransferencia) {
		StringBuffer str = new StringBuffer();
		// Dados do Registro de Aula
//		str.append(" SELECT ");
//		str.append(" (select distinct count(distinct rg.codigo) from registroaula rg ");
//		str.append(" inner join frequenciaaula on frequenciaaula.registroaula = rg.codigo ");
//		if (turmaVO.getCodigo() != null && !turmaVO.getCodigo().equals(0)) {
//			str.append(" and rg.turma = ").append(turmaVO.getCodigo());
//		}
//		if (disciplina != null && !disciplina.equals(0)) {
//			str.append(" and rg.disciplina = ").append(disciplina);
//		}
//		if (professores != null && !professores.isEmpty()) {
//			str.append(" AND (rg.professor in (0 ");
//			for (ProfessorTitularDisciplinaTurmaVO professor : professores) {
//				str.append(", ").append(professor.getProfessor().getPessoa().getCodigo().intValue());
//			}
//			str.append(")) ");
//		}
//		if (Uteis.isAtributoPreenchido(ano)) {
//			str.append(" and rg.ano = '").append(ano).append("' ");
//		}
//		if (Uteis.isAtributoPreenchido(semestre)) {
//			str.append(" and rg.semestre = '").append(semestre).append("' ");
//		}
//		str.append(" and frequenciaaula.matricula = matricula_matricula ");
//		str.append(" and case when (dataTrancamento is not null) then rg.data < datatrancamento else true end ");
//		str.append(" and frequenciaaula.presente = false and frequenciaaula.abonado = false) as faltasgeral, ");
//		str.append(" * from ( ");
		str.append(" SELECT DISTINCT ");
		str.append(" regAula.codigo, regAula.data, regAula.horario, regAula.nrAula, regAula.diaSemana, regAula.semestre, regAula.ano, regAula.tipoAula, regAula.cargaHoraria, regAula.conteudo, regAula.dataRegistroAula, regAula.atividadeComplementar, regAula.praticaSupervisionada, ");
		str.append(" turma.codigo AS \"turma.codigo\", turma.identificadorTurma AS \"turma.identificadorTurma\", turma.turmaAgrupada AS \"turma.turmaAgrupada\", turma.semestral AS \"turma.semestral\", ");
		// Dados do Curso
		str.append(" curso.nome AS \"curso.nome\", curso.codigo AS \"curso.codigo\", curso.nivelEducacional AS \"curso.nivelEducacional\", curso.configuracaoAcademico AS \"curso.configuracaoAcademico\",  ");
		// Dados do Turno
		str.append(" turno.nome AS \"turno.nome\", ");
		// Dados do Periodo Letivo
		str.append(" periodoLetivo.codigo AS \"periodoLetivo.codigo\", periodoLetivo.descricao AS \"periodoLetivo.descricao\", ");
		// Dados da Pessoa
		str.append(" pessoa.codigo AS \"professor.codigo\", pessoa.nome AS \"professor.nome\", ");
		// Dados do Usuario
		str.append(" usuario.nome AS \"responsavelRegistroAula.nome\", usuario.codigo AS \"responsavelRegistroAula.codigo\", ");
		// Dados da Matrícula
		str.append(" matricula.matricula AS matricula_matricula, matricula.updated AS \"matricula.updated\", matricula.data AS \"matricula.data\", matricula.tipomatricula AS \"matricula.tipoMatricula\", matricula.situacao as \"matricula.situacao\", ");
		// str.append(" matricula.matricula AS \"matricula.matricula\", matricula.updated AS \"matricula.updated\", matricula.extensao AS \"matricula.extensao\", ");
		// Dados do Aluno
		str.append(" aluno.nome AS \"aluno.nome\", ");
		// Dados da Disciplina
		str.append(" disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", gradedisciplina.cargaHoraria AS \"gradedisciplina.cargaHoraria\", gradedisciplina.configuracaoAcademico AS \"gradedisciplina.configuracaoAcademico\", ");
		// Dados da Unidade
		str.append(" unidadeEnsino.codigo AS \"unidadeEnsino.codigo\", unidadeEnsino.nome AS \"unidadeEnsino.nome\", ");
		// Dados da Disciplina
		str.append(" frequenciaaula.presente AS \"frequenciaaula.presente\", frequenciaaula.abonado AS \"frequenciaaula.abonado\", frequenciaaula.justificado AS \"frequenciaaula.justificado\", ");
		str.append(" MatriculaPeriodoTurmaDisciplina.codigo as matriculaPeriodoTurmaDisciplina, matriculaperiodo.codigo as matriculaperiodo, matriculaperiodo.situacaoMatriculaPeriodo,  ");
		str.append(" historico.codigo as historico, historico.configuracaoacademico as configuracaoacademico, historico.tipoHistorico, historico.instituicao, historico.situacao, historico.freguencia, historico.dataRegistro, ");
		str.append(" historico.mediaFinal, historico.transferenciaEntradaDisciplinasAproveitadas, historico.mediafinalconceito, ");
		for(int x = 1; x<=40;x++) {
			str.append(" historico.nota").append(x).append(", ");
			str.append(" historico.nota").append(x).append("lancada, ");
			str.append(" historico.nota").append(x).append("conceito, ");
		}		
		str.append(" historico.updated, historico.historicoDisciplinaFazParteComposicao, anohistorico, semestrehistorico, ");		
		str.append(" case when historico.situacao = 'TR' then (select data from trancamento where trancamento.matricula = matricula.matricula order by data desc limit 1) end as dataTrancamento, ");		
		str.append(" quantidadeCasasDecimaisPermitirAposVirgula, tipojustificativafalta.sigla ");
		str.append(" FROM registroAula AS regAula ");
		str.append(" inner JOIN turma on turma.codigo = regAula.turma ");
		str.append(" inner JOIN disciplina on ((disciplina.codigo = regAula.disciplina) ");
		if (turmaVO.getTurmaAgrupada()) {
			str.append(" or (turma.turmaagrupada and turma.subturma = false and disciplina.codigo in (select equivalente from disciplinaequivalente where disciplina = "+disciplina+")) ");
		}
		str.append(" ) ");
		str.append(" inner JOIN unidadeEnsino on unidadeEnsino.codigo = turma.unidadeEnsino ");
		str.append(" inner JOIN turno on turno.codigo = turma.turno ");		
		str.append(" inner JOIN frequenciaaula on frequenciaaula.registroaula = regAula.codigo  ");
		str.append(" inner JOIN matricula on matricula.matricula = frequenciaAula.matricula   ");
		str.append(" inner JOIN pessoa AS \"aluno\" on aluno.codigo = matricula.aluno  ");
		str.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		str.append(" and case when turma.anual = false and turma.semestral = false then true else case when turma.anual ");
		str.append(" then trim(matriculaperiodo.ano) = trim(regAula.ano)  else (trim(matriculaperiodo.ano) = trim(regAula.ano) ");
		str.append(" and trim(matriculaperiodo.semestre) = trim(regAula.semestre) ) end end = true ");
		if (turmaVO.getSubturma()) {
			if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
				str.append("inner JOIN MatriculaPeriodoTurmaDisciplina ON MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula and MatriculaPeriodoTurmaDisciplina.turmaTeorica = regAula.turma ");
			} else if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
				str.append("inner JOIN MatriculaPeriodoTurmaDisciplina ON MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula and MatriculaPeriodoTurmaDisciplina.turmaPratica = regAula.turma ");
			} else {
				str.append("inner JOIN MatriculaPeriodoTurmaDisciplina ON MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula and MatriculaPeriodoTurmaDisciplina.turma = regAula.turma ");
			}
		} else {
			str.append("inner JOIN MatriculaPeriodoTurmaDisciplina ON MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula ");			
		}
		if (!turmaVO.getTurmaAgrupada()) {
			str.append(" and MatriculaPeriodoTurmaDisciplina.disciplina = disciplina.codigo ");
			if(!turmaVO.getSubturma()){
				str.append(" and MatriculaPeriodoTurmaDisciplina.turma = turma.codigo ");
			}
		}else{
			str.append(" and (MatriculaPeriodoTurmaDisciplina.disciplina = disciplina.codigo  ");
			str.append(" or MatriculaPeriodoTurmaDisciplina.disciplina in (select disciplinaequivalenteturmaagrupada from turmadisciplina where turmadisciplina.disciplina = ").append(disciplina.intValue());
			str.append(" and disciplinaequivalenteturmaagrupada is not null and turmadisciplina.turma = ").append(turmaVO.getCodigo()).append(" ");
			str.append(" union all select disciplina from disciplinaequivalente where equivalente =  ").append(disciplina.intValue());
			str.append(" union all select equivalente from disciplinaequivalente where disciplina =  ").append(disciplina.intValue());
			str.append(" )) ");
		}
		str.append(" and MatriculaPeriodoTurmaDisciplina.matriculaperiodo = matriculaperiodo.codigo  ");		
		str.append(" inner JOIN Historico ON Historico.matricula = matricula.matricula and historico.matriculaperiodo = matriculaperiodo.codigo and Historico.MatriculaPeriodoTurmaDisciplina = MatriculaPeriodoTurmaDisciplina.codigo  ");
		str.append(" and historico.codigo = (select his.codigo from historico his where his.matricula = matricula.matricula and his.matriculaperiodo = matriculaperiodo.codigo and his.MatriculaPeriodoTurmaDisciplina = MatriculaPeriodoTurmaDisciplina.codigo order by case when his.matrizcurricular = matricula.gradecurricularatual then 0 else 1 end limit 1 ) ");
		// str.append(" LEFT JOIN Historico ON Historico.matricula = matricula.matricula  ");
		// str.append(" and Historico.disciplina = MatriculaPeriodoTurmaDisciplina.disciplina ");
		// str.append(" and  Historico.matriculaperiodo = matriculaperiodo.codigo ");
		/**
		 * Adicionada regra para resolver impactos relacionados a alunos que
		 * estão Cursando por Correspondência e que disciplinas saiam duplicadas
		 * no Boletim Acadêmico
		 */
		if (!trazerAlunoTransferencia) {
			str.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		}else{
			str.append(" and (").append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" "));
			str.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" or ")).append(" = false )");
		}	
		str.append(" left JOIN curso on curso.codigo = turma.curso ");
		str.append(" left JOIN periodoLetivo on periodoLetivo.codigo = turma.periodoLetivo ");
		str.append(" LEFT JOIN gradedisciplina on gradedisciplina.periodoletivo = periodoLetivo.codigo and gradedisciplina.disciplina = disciplina.codigo ");
		str.append(" LEFT JOIN usuario on usuario.codigo = regAula.responsavelRegistroAula ");
		str.append(" LEFT JOIN configuracaoAcademico on configuracaoAcademico.codigo = curso.configuracaoAcademico ");
		str.append(" LEFT JOIN pessoa on pessoa.codigo = regAula.professor ");
		str.append(" left join disciplinaabono on regAula.codigo = disciplinaabono.registroaula and disciplinaabono.matricula = frequenciaaula.matricula and disciplinaabono.disciplina = disciplina.codigo ");
		str.append(" left join abonofalta on disciplinaabono.abonofalta = abonofalta.codigo ");
		str.append(" left join tipojustificativafalta on tipojustificativafalta.codigo = abonofalta.tipojustificativafalta ");
	
//		System.out.print(str);
		return str;
		
	}

	private StringBuffer getSQLPadraoConsultaCompletaDiarioReposicao(TurmaVO turmaVO, Integer disciplina, Boolean trazerAlunoTransferencia) {
		StringBuffer str = new StringBuffer();
		// Dados do Registro de Aula
		str.append("SELECT DISTINCT regAula.codigo, regAula.data, regAula.horario, regAula.nrAula, regAula.diaSemana, regAula.semestre, regAula.ano, regAula.tipoAula, regAula.cargaHoraria, regAula.conteudo, regAula.dataRegistroAula, regAula.atividadeComplementar, regAula.praticaSupervisionada, ");
		str.append("turmaP.identificadorTurma AS \"turma.identificadorTurmaPadrao\", ");
		str.append("turma.codigo AS \"turma.codigo\", turma.identificadorTurma AS \"turma.identificadorTurma\", turma.turmaAgrupada AS \"turma.turmaAgrupada\", turma.semestral AS \"turma.semestral\", ");
		// Dados do Curso
		str.append(" curso.nome AS \"curso.nome\", curso.codigo AS \"curso.codigo\", curso.nivelEducacional AS \"curso.nivelEducacional\", curso.configuracaoAcademico AS \"curso.configuracaoAcademico\",  ");
		// Dados do Turno
		str.append(" turno.nome AS \"turno.nome\", ");
		// Dados do Periodo Letivo
		str.append(" periodoLetivo.codigo AS \"periodoLetivo.codigo\", periodoLetivo.descricao AS \"periodoLetivo.descricao\", ");
		// Dados da Pessoa
		str.append(" pessoa.codigo AS \"professor.codigo\", pessoa.nome AS \"professor.nome\", ");
		// Dados do Usuario
		str.append(" usuario.nome AS \"responsavelRegistroAula.nome\", usuario.codigo AS \"responsavelRegistroAula.codigo\", ");
		// Dados da Matrícula
		str.append(" matricula.matricula AS \"matricula.matricula\", matricula.updated AS \"matricula.updated\", matricula.data AS \"matricula.data\", matricula.tipomatricula AS \"matricula.tipoMatricula\",");
		// str.append(" matricula.matricula AS \"matricula.matricula\", matricula.updated AS \"matricula.updated\", matricula.extensao AS \"matricula.extensao\", ");
		// Dados do Aluno
		str.append(" aluno.nome AS \"aluno.nome\", sem_acentos(aluno.nome) as nomeAlunoSemAcento, ");
		// Dados da Disciplina
		str.append(" disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", gradedisciplina.cargaHoraria AS \"gradedisciplina.cargaHoraria\", gradedisciplina.configuracaoAcademico AS \"gradedisciplina.configuracaoAcademico\", ");
		// Dados da Unidade
		str.append(" unidadeEnsino.codigo AS \"unidadeEnsino.codigo\", unidadeEnsino.nome AS \"unidadeEnsino.nome\", ");
		// Dados da Disciplina
		str.append(" frequenciaaula.presente AS \"frequenciaaula.presente\", frequenciaaula.abonado AS \"frequenciaaula.abonado\", frequenciaaula.justificado AS \"frequenciaaula.justificado\",  ");
		str.append(" MatriculaPeriodoTurmaDisciplina.codigo as matriculaPeriodoTurmaDisciplina,   ");
		str.append(" historico.codigo as historico, historico.configuracaoacademico as configuracaoacademico, historico.tipoHistorico, historico.instituicao, historico.situacao, historico.freguencia, historico.dataRegistro, ");
		str.append(" historico.mediaFinal, historico.transferenciaEntradaDisciplinasAproveitadas, historico.mediaFinalConceito, ");
		for(int x = 1; x<=40;x++) {
			str.append(" historico.nota").append(x).append(", ");
			str.append(" historico.nota").append(x).append("lancada, ");
			str.append(" historico.nota").append(x).append("conceito, ");
		}
		
		str.append(" historico.updated, historico.historicoDisciplinaFazParteComposicao, anohistorico, semestrehistorico, ");		
		
		str.append("quantidadeCasasDecimaisPermitirAposVirgula, tipojustificativafalta.sigla ");
		str.append(" FROM registroAula AS regAula ");
		str.append(" LEFT JOIN turma on turma.codigo = regAula.turma ");
		str.append(" LEFT JOIN disciplina on disciplina.codigo = regAula.disciplina ");
		str.append(" LEFT JOIN pessoa on pessoa.codigo = regAula.professor ");
		str.append(" LEFT JOIN unidadeEnsino on unidadeEnsino.codigo = turma.unidadeEnsino ");
		str.append(" LEFT JOIN turno on turno.codigo = turma.turno ");
		str.append(" LEFT JOIN periodoLetivo on periodoLetivo.codigo = turma.periodoLetivo ");
		str.append(" LEFT JOIN gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo and gradedisciplina.disciplina = disciplina.codigo ");
		str.append(" LEFT JOIN usuario on usuario.codigo = regAula.responsavelRegistroAula ");
		str.append(" LEFT JOIN frequenciaaula on frequenciaaula.registroaula = regAula.codigo  ");
		str.append(" LEFT JOIN matricula on matricula.matricula = frequenciaAula.matricula   ");
		str.append(" LEFT JOIN curso on curso.codigo = matricula.curso ");
		str.append(" LEFT JOIN configuracaoAcademico on configuracaoAcademico.codigo = curso.configuracaoAcademico ");
		str.append(" LEFT JOIN pessoa AS \"aluno\" on aluno.codigo = matricula.aluno  ");
		str.append(" Inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.ano = regAula.ano and matriculaperiodo.semestre = regAula.semestre ");
		str.append(" Inner JOIN turma turmaP on turmaP.codigo = matriculaperiodo.turma ");		
		str.append(" left join disciplinaabono on regAula.codigo = disciplinaabono.registroaula and disciplinaabono.matricula = frequenciaaula.matricula and disciplinaabono.disciplina = disciplina.codigo ");
		str.append(" left join abonofalta on disciplinaabono.abonofalta = abonofalta.codigo ");
		str.append(" left join tipojustificativafalta on tipojustificativafalta.codigo = abonofalta.tipojustificativafalta ");	
		if (turmaVO.getSubturma()) {
			if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
				str.append(" LEFT JOIN MatriculaPeriodoTurmaDisciplina ON MatriculaPeriodoTurmaDisciplina.turmaTeorica = turma.codigo  and MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula ");
			} else if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
				str.append(" LEFT JOIN MatriculaPeriodoTurmaDisciplina ON MatriculaPeriodoTurmaDisciplina.turmaPratica = turma.codigo  and MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula ");
			} else {
				str.append(" LEFT JOIN MatriculaPeriodoTurmaDisciplina ON MatriculaPeriodoTurmaDisciplina.turma = turma.codigo  and MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula ");
			}
		} else {
			str.append(" LEFT JOIN MatriculaPeriodoTurmaDisciplina ON MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula ");
		}
		if (!turmaVO.getTurmaAgrupada()) {
			str.append(" and MatriculaPeriodoTurmaDisciplina.disciplina = disciplina.codigo ");
			if(!turmaVO.getSubturma()){
				str.append(" and MatriculaPeriodoTurmaDisciplina.turma = turma.codigo ");
			}
		}else{
			str.append(" and (MatriculaPeriodoTurmaDisciplina.disciplina = disciplina.codigo  ");
			str.append(" or MatriculaPeriodoTurmaDisciplina.disciplina in (select disciplinaequivalenteturmaagrupada from turmadisciplina where turmadisciplina.disciplina = ").append(disciplina.intValue());
			str.append(" and disciplinaequivalenteturmaagrupada is not null and turmadisciplina.turma = ").append(turmaVO.getCodigo()).append(" ");
			str.append(" union select disciplinaequivalente.disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina);
			str.append(" union select disciplinaequivalente.equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina);
			str.append(" )) ");
		}
		str.append(" and MatriculaPeriodoTurmaDisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		// Este foi comentado pois nos casos de reposição o sistema estava
		// considerando mais de um histórico visto que de fato existe mais de um
		// histrorico para a mesma disciplina,
		// sendo assim foi adicionado o join com a
		// MatriculaPeriodoTurmaDisciplina para buscar o histórico correto.
		// str.append(" LEFT JOIN Historico ON Historico.matricula = matricula.matricula  ");
		// str.append(" LEFT JOIN Historico ON Historico.MatriculaPeriodoTurmaDisciplina = MatriculaPeriodoTurmaDisciplina.codigo  ");
		// str.append(" and case when curso.niveleducacional = 'PR' and curso.liberarRegistroAulaEntrePeriodo then "
		// + codigoTurma +
		// " else MatriculaPeriodoTurmaDisciplina.turma end = MatriculaPeriodoTurmaDisciplina.turma ");
		// str.append(" and Historico.disciplina = MatriculaPeriodoTurmaDisciplina.disciplina ");
		// str.append(" and  Historico.matriculaperiodo = matriculaperiodo.codigo ");

		str.append(" inner JOIN Historico ON Historico.matricula = matricula.matricula and historico.matriculaperiodo = matriculaperiodo.codigo and Historico.MatriculaPeriodoTurmaDisciplina = MatriculaPeriodoTurmaDisciplina.codigo  ");
		str.append(" and historico.codigo = (select his.codigo from historico his where his.matricula = matricula.matricula and his.matriculaperiodo = matriculaperiodo.codigo and his.MatriculaPeriodoTurmaDisciplina = MatriculaPeriodoTurmaDisciplina.codigo order by case when his.matrizcurricular = matricula.gradecurricularatual then 0 else 1 end limit 1 ) ");
		/**
		 * Adicionada regra para resolver impactos relacionados a alunos que
		 * estão Cursando por Correspondência e que disciplinas saiam duplicadas
		 * no Boletim Acadêmico
		 */
		if (!trazerAlunoTransferencia) {
			str.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		}else {
			str.append(" and (").append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" "));
			str.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" or ")).append(" = false )");
		}			

		

		return str;
	}

	private StringBuffer getSQLPadraoConsultaCompletaEspelhoDiario(TurmaVO turmaVO, Boolean filtroVisaoProfessor, Boolean liberarRegistroAulaEntrePeriodo, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, String tipoAluno, Boolean trazerAlunoTransferido) {
		StringBuffer str = new StringBuffer();
		// Dados do Registro de Aula
		str.append(" SELECT DISTINCT regAula.codigo, regAula.data, regAula.horario, regAula.nrAula, regAula.diaSemana, regAula.semestre, regAula.ano, regAula.tipoAula, regAula.cargaHoraria, regAula.conteudo, regAula.dataRegistroAula, regAula.atividadeComplementar, regAula.praticaSupervisionada, ");
		str.append(" turma.codigo AS \"turma.codigo\", turma.identificadorTurma AS \"turma.identificadorTurma\", turma.turmaAgrupada AS \"turma.turmaAgrupada\", turma.semestral AS \"turma.semestral\", ");
		// Dados do Curso
		str.append(" curso.nome AS \"curso.nome\", curso.codigo AS \"curso.codigo\", curso.nivelEducacional AS \"curso.nivelEducacional\", curso.configuracaoAcademico AS \"curso.configuracaoAcademico\",  ");
		// Dados do Turno
		str.append(" turno.nome AS \"turno.nome\", ");
		// Dados do Periodo Letivo
		str.append(" periodoLetivo.codigo AS \"periodoLetivo.codigo\", periodoLetivo.descricao AS \"periodoLetivo.descricao\", ");
		// Dados da Pessoa
		str.append(" pessoa.codigo AS \"professor.codigo\", pessoa.nome AS \"professor.nome\", ");
		// Dados do Usuario
		str.append(" usuario.nome AS \"responsavelRegistroAula.nome\", usuario.codigo AS \"responsavelRegistroAula.codigo\", ");
		// Dados da Disciplina
		str.append(" disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", gradedisciplina.cargaHoraria AS \"gradedisciplina.cargaHoraria\", gradedisciplina.configuracaoAcademico AS \"gradedisciplina.configuracaoAcademico\", ");
		// Dados da Unidade
		str.append(" unidadeEnsino.codigo AS \"unidadeEnsino.codigo\", unidadeEnsino.nome AS \"unidadeEnsino.nome\" ");
		// Dados da Disciplina

		str.append(" FROM registroAula AS regAula ");

		str.append(" LEFT JOIN turma on turma.codigo = regAula.turma ");

		str.append(" LEFT JOIN disciplina on disciplina.codigo = regAula.disciplina ");
		str.append(" LEFT JOIN pessoa on pessoa.codigo = regAula.professor ");
		// Dados do Curso proveniente da turma, para turma agrupada não
		// funciona, pois na mesma não é salvo o código do curso.
		// str.append(" LEFT JOIN curso on curso.codigo = turma.curso ");
		str.append(" LEFT JOIN unidadeEnsino on unidadeEnsino.codigo = turma.unidadeEnsino ");
		str.append(" LEFT JOIN turno on turno.codigo = turma.turno ");
		str.append(" LEFT JOIN periodoLetivo on periodoLetivo.codigo = turma.periodoLetivo ");
		str.append(" LEFT JOIN gradedisciplina on gradedisciplina.disciplina = disciplina.codigo and gradedisciplina.periodoletivo = periodoletivo.codigo ");
		str.append(" LEFT JOIN usuario on usuario.codigo = regAula.responsavelRegistroAula ");
		str.append(" LEFT JOIN frequenciaaula on frequenciaaula.registroaula = regAula.codigo  ");
		str.append(" LEFT JOIN matricula on matricula.matricula = frequenciaAula.matricula   ");
		if (turmaVO.getTurmaAgrupada()) {
			// Dados do Curso provenientes da Matricula, pois turma agrupada não
			// registra o código do curso, sendo assim não é possivel chegar a
			// configuração academica.
			str.append(" LEFT JOIN curso on curso.codigo = (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turma.codigo = turmaorigem limit 1) ");
		} else {
			str.append(" LEFT JOIN curso on curso.codigo = turma.curso ");
		}
		str.append(" LEFT JOIN configuracaoAcademico on configuracaoAcademico.codigo = curso.configuracaoAcademico ");

		str.append(" LEFT JOIN pessoa AS \"aluno\" on aluno.codigo = matricula.aluno  ");
		if (filtroVisaoProfessor != null && filtroVisaoProfessor) {
			str.append((" Inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula  "));
			if (!liberarRegistroAulaEntrePeriodo) {
				str.append(" and matriculaperiodo.ano = regAula.ano and matriculaperiodo.semestre = regAula.semestre ");
			}
			if (tipoAluno.equals("normal")) {
				str.append(" and matriculaperiodo.turma = turma.codigo ");
			}
		} else if ((filtroVisaoProfessor == null || !filtroVisaoProfessor)) {
			if (!liberarRegistroAulaEntrePeriodo) {
				str.append(" left join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.ano = regAula.ano and matriculaperiodo.semestre = regAula.semestre ");
			} else {
				str.append(" left join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
			}
			if (tipoAluno.equals("normal")) {
				str.append(" and matriculaperiodo.turma = turma.codigo ");
			}
		}
		if (turmaVO.getSubturma()) {
			if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
				str.append("LEFT JOIN MatriculaPeriodoTurmaDisciplina ON MatriculaPeriodoTurmaDisciplina.turmaTeorica = turma.codigo and MatriculaPeriodoTurmaDisciplina.disciplina = disciplina.codigo and MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula ");
			} else if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
				str.append("LEFT JOIN MatriculaPeriodoTurmaDisciplina ON MatriculaPeriodoTurmaDisciplina.turmaPratica = turma.codigo and MatriculaPeriodoTurmaDisciplina.disciplina = disciplina.codigo and MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula ");
			} else {
				str.append("LEFT JOIN MatriculaPeriodoTurmaDisciplina ON MatriculaPeriodoTurmaDisciplina.turma = turma.codigo and MatriculaPeriodoTurmaDisciplina.disciplina = disciplina.codigo and MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula ");
			}
		} else {
			str.append("LEFT JOIN MatriculaPeriodoTurmaDisciplina ON MatriculaPeriodoTurmaDisciplina.disciplina = disciplina.codigo and MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula ");
			if (!turmaVO.getTurmaAgrupada()) {
				str.append(" and MatriculaPeriodoTurmaDisciplina.turma = turma.codigo ");
			}
		}
		if ((filtroVisaoProfessor != null && filtroVisaoProfessor)) {
			str.append("and MatriculaPeriodoTurmaDisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		}
		str.append(" LEFT JOIN Historico ON Historico.matricula = matricula.matricula  ");
		// str.append(" LEFT JOIN Historico ON Historico.MatriculaPeriodoTurmaDisciplina = MatriculaPeriodoTurmaDisciplina.codigo  ");
		// str.append(" and case when curso.niveleducacional = 'PR' and curso.liberarRegistroAulaEntrePeriodo then "
		// + codigoTurma +
		// " else MatriculaPeriodoTurmaDisciplina.turma end = MatriculaPeriodoTurmaDisciplina.turma ");
		str.append(" and Historico.disciplina = MatriculaPeriodoTurmaDisciplina.disciplina ");
		str.append(" and  Historico.matriculaperiodo = matriculaperiodo.codigo ");
		/**
		 * Adicionada regra para resolver impactos relacionados a alunos que
		 * estão Cursando por Correspondência e que disciplinas saiam duplicadas
		 * no Boletim Acadêmico
		 */
		if (!trazerAlunoTransferido) {
			str.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		}else {
			str.append(" and (").append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" "));
			str.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" or ")).append(" = false )");
		}	

		return str;
	}

	private StringBuffer getSQLPadraoConsultaCompletaReposicao() {
		StringBuffer str = new StringBuffer();
		// Dados do Registro de Aula
		str.append("SELECT regAula.codigo, regAula.data, regAula.horario,  regAula.nrAula, regAula.diaSemana, regAula.semestre, regAula.ano, regAula.tipoAula, regAula.cargaHoraria, regAula.conteudo, regAula.dataRegistroAula,  ");
		str.append("turmaP.identificadorTurma AS \"turma.identificadorTurmaPadrao\", ");
		str.append("turma.codigo AS \"turma.codigo\", turma.identificadorTurma AS \"turma.identificadorTurma\", turma.turmaAgrupada AS \"turma.turmaAgrupada\", turma.semestral AS \"turma.semestral\", ");
		// Dados do Curso
		str.append(" curso.nome AS \"curso.nome\", curso.codigo AS \"curso.codigo\", curso.nivelEducacional AS \"curso.nivelEducacional\", curso.configuracaoAcademico AS \"curso.configuracaoAcademico\",  ");
		// Dados do Turno
		str.append(" turno.nome AS \"turno.nome\", ");
		// Dados do Periodo Letivo
		str.append(" periodoLetivo.codigo AS \"periodoLetivo.codigo\", periodoLetivo.descricao AS \"periodoLetivo.descricao\", ");
		// Dados da Pessoa
		str.append(" pessoa.codigo AS \"professor.codigo\", pessoa.nome AS \"professor.nome\", ");
		// Dados do Usuario
		str.append(" usuario.nome AS \"responsavelRegistroAula.nome\", usuario.codigo AS \"responsavelRegistroAula.codigo\", ");
		// Dados da Matrícula
		str.append(" matricula.matricula AS \"matricula.matricula\", matricula.updated AS \"matricula.updated\", matricula.extensao AS \"matricula.extensao\", ");
		// Dados do Aluno
		str.append(" aluno.nome AS \"aluno.nome\", ");
		// Dados da Disciplina
		str.append(" disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", ");
		// Dados da Unidade
		str.append(" unidadeEnsino.codigo AS \"unidadeEnsino.codigo\", unidadeEnsino.nome AS \"unidadeEnsino.nome\", ");
		// Dados da Disciplina
		str.append(" frequenciaaula.presente AS \"frequenciaaula.presente\"  ");

		str.append(" FROM registroAula AS regAula ");

		str.append(" LEFT JOIN turma on turma.codigo = regAula.turma ");
		str.append(" LEFT JOIN disciplina on disciplina.codigo = regAula.disciplina ");
		str.append(" LEFT JOIN pessoa on pessoa.codigo = regAula.professor ");
		// Dados do Curso proveniente da turma, para turma agrupada não
		// funciona, pois na mesma não é salvo o código do curso.
		// str.append(" LEFT JOIN curso on curso.codigo = turma.curso ");
		str.append(" LEFT JOIN unidadeEnsino on unidadeEnsino.codigo = turma.unidadeEnsino ");
		str.append(" LEFT JOIN turno on turno.codigo = turma.turno ");
		str.append(" LEFT JOIN periodoLetivo on periodoLetivo.codigo = turma.periodoLetivo ");
		str.append(" LEFT JOIN usuario on usuario.codigo = regAula.responsavelRegistroAula ");
		str.append(" LEFT JOIN frequenciaaula on frequenciaaula.registroaula = regAula.codigo  ");
		str.append(" LEFT JOIN matricula on matricula.matricula = frequenciaAula.matricula   ");
		// Dados do Curso provenientes da Matricula, pois turma agrupada não
		// registra o código do curso, sendo assim não é possivel chegar a
		// configuração academica.
		str.append(" LEFT JOIN curso on curso.codigo = matricula.curso ");
		str.append(" LEFT JOIN configuracaoAcademico on configuracaoAcademico.codigo = curso.configuracaoAcademico ");

		str.append(" LEFT JOIN pessoa AS \"aluno\" on aluno.codigo = matricula.aluno  ");
		return str;
	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<RegistroAulaVO> consultaRapidaPorData(Date prmIni, Date prmFim, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE (data >= '");
		sqlStr.append(Uteis.getDataJDBC(prmIni));
		sqlStr.append("')");
		sqlStr.append(" and (data <= '");
		sqlStr.append(Uteis.getDataJDBC(prmFim));
		sqlStr.append("')");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND turma.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (!semestre.equals("")) {
			sqlStr.append(" and RegistroAula.semestre = '");
			sqlStr.append(semestre);
			sqlStr.append("'");
		}
		if (!ano.equals("")) {
			sqlStr.append(" and RegistroAula.ano = '");
			sqlStr.append(ano);
			sqlStr.append("'");
		}
		sqlStr.append(" ORDER BY regAula.data");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<RegistroAulaVO> consultaRapidaPorCargaHoraria(Integer valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE regAula.cargaHoraria = ");
		sqlStr.append(valorConsulta.intValue());
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND turma.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (!semestre.equals("")) {
			sqlStr.append(" and RegistroAula.semestre = '");
			sqlStr.append(semestre);
			sqlStr.append("'");
		}
		if (!ano.equals("")) {
			sqlStr.append(" and RegistroAula.ano = '");
			sqlStr.append(ano);
			sqlStr.append("'");
		}
		sqlStr.append(" ORDER BY regAula.cargaHoraria");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<RegistroAulaVO> consultaRapidaPorConteudo(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(regAula.conteudo) like('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%')");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND turma.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (!semestre.equals("")) {
			sqlStr.append(" and RegistroAula.semestre = '");
			sqlStr.append(semestre);
			sqlStr.append("'");
		}
		if (!ano.equals("")) {
			sqlStr.append(" and RegistroAula.ano = '");
			sqlStr.append(ano);
			sqlStr.append("'");
		}
		sqlStr.append(" ORDER BY regAula.conteudo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<RegistroAulaVO> consultaRapidaPorIdentificadorTurma(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(turma.identificadorTurma) like('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%')");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND turma.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (!semestre.equals("")) {
			sqlStr.append(" and RegistroAula.semestre = '");
			sqlStr.append(semestre);
			sqlStr.append("'");
		}
		if (!ano.equals("")) {
			sqlStr.append(" and RegistroAula.ano = '");
			sqlStr.append(ano);
			sqlStr.append("'");
		}
		sqlStr.append(" ORDER BY turma.identificadorTurma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<RegistroAulaVO> consultaRapidaPorNomeDisciplina(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(disciplina.nome) like('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%')");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND turma.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (!semestre.equals("")) {
			sqlStr.append(" and RegistroAula.semestre = '");
			sqlStr.append(semestre);
			sqlStr.append("'");
		}
		if (!ano.equals("")) {
			sqlStr.append(" and RegistroAula.ano = '");
			sqlStr.append(ano);
			sqlStr.append("'");
		}
		sqlStr.append(" ORDER BY disciplina.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<RegistroAulaVO> consultaRapidaPorNomeProfessor(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(pessoa.nome) like('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%')");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND turma.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (!semestre.equals("")) {
			sqlStr.append(" and RegistroAula.semestre = '");
			sqlStr.append(semestre);
			sqlStr.append("'");
		}
		if (!ano.equals("")) {
			sqlStr.append(" and RegistroAula.ano = '");
			sqlStr.append(ano);
			sqlStr.append("'");
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<RegistroAulaVO> consultaRapidaPorNomeCurso(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(curso.nome) like('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%')");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND turma.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (!semestre.equals("")) {
			sqlStr.append(" and RegistroAula.semestre = '");
			sqlStr.append(semestre);
			sqlStr.append("'");
		}
		if (!ano.equals("")) {
			sqlStr.append(" and RegistroAula.ano = '");
			sqlStr.append(ano);
			sqlStr.append("'");
		}
		sqlStr.append(" ORDER BY curso.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<RegistroAulaVO> consultaRapidaPorCodigoTurma(Integer valorConsulta, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE turma.codigo = ");
		sqlStr.append(valorConsulta.intValue());
		if (!semestre.equals("")) {
			sqlStr.append(" and RegistroAula.semestre = '");
			sqlStr.append(semestre);
			sqlStr.append("'");
		}
		if (!ano.equals("")) {
			sqlStr.append(" and RegistroAula.ano = '");
			sqlStr.append(ano);
			sqlStr.append("'");
		}
		sqlStr.append(" ORDER BY turma.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<RegistroAulaVO> consultaRapidaPorCodigoDisciplina(Integer valorConsulta, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE disciplina.codigo = ");
		sqlStr.append(valorConsulta.intValue());
		if (!semestre.equals("")) {
			sqlStr.append(" and RegistroAula.semestre = '");
			sqlStr.append(semestre);
			sqlStr.append("'");
		}
		if (!ano.equals("")) {
			sqlStr.append(" and RegistroAula.ano = '");
			sqlStr.append(ano);
			sqlStr.append("'");
		}
		sqlStr.append(" ORDER BY disciplina.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<RegistroAulaVO> consultaRapidaFaltasAluno(String matriculaAluno, Integer codigoDisciplina, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select registroaula.dataregistroaula as data, disciplina.nome as disciplina, pessoa.nome as professor, registroaula.ano, registroaula.semestre, registroaula.horario, registroaula.nrAula , registroaula.diasemana, turma.identificadorTurma, ");
		sqlStr.append(" CASE WHEN (abonado = true) THEN 'Abonado' WHEN (justificado = true) THEN 'Justificado' END AS situacaoAbonoFalta from frequenciaaula ");
		sqlStr.append("inner join registroaula on registroaula.codigo = frequenciaaula.registroaula ");
		sqlStr.append("inner join disciplina on disciplina.codigo = registroaula.disciplina ");
		sqlStr.append("inner join pessoa on pessoa.codigo = registroaula.professor ");
		sqlStr.append("inner join turma on turma.codigo = registroaula.turma ");
		sqlStr.append("where frequenciaaula.presente = false and frequenciaaula.abonado = false   ");
		if (!semestre.equals("")) {
			sqlStr.append(" and RegistroAula.semestre = '");
			sqlStr.append(semestre);
			sqlStr.append("'");
		}
		if (!ano.equals("")) {
			sqlStr.append(" and RegistroAula.ano = '");
			sqlStr.append(ano);
			sqlStr.append("'");
		}
		sqlStr.append(" and frequenciaaula.matricula = '");
		sqlStr.append(matriculaAluno);
		sqlStr.append("'");
		sqlStr.append("AND (disciplina.codigo = ").append(codigoDisciplina).append(" ");
		sqlStr.append(" or (turma.turmaagrupada = true and disciplina.codigo in (select disciplina from disciplinaequivalente where equivalente = ").append(codigoDisciplina).append("))  ");
		sqlStr.append(" or (turma.turmaagrupada = true and disciplina.codigo in (select equivalente from disciplinaequivalente where disciplina = ").append(codigoDisciplina).append("))  ");		
		sqlStr.append(" )  ");
		sqlStr.append(" order by registroaula.dataregistroaula, registroaula.horario");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapidaFaltas(tabelaResultado);
	}

	public List<RegistroAulaVO> consultaRapidaFaltasAlunoTurma(Integer turma, String matriculaAluno, Integer codigoDisciplina, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select registroaula.data as data, disciplina.nome as disciplina, turma.identificadorturma,  ");
		sqlStr.append(" array_to_string(array_agg(distinct pessoa.nome), ', ') as professor,   ");
		sqlStr.append(" registroaula.ano, registroaula.semestre, registroaula.horario, registroaula.nrAula , registroaula.diasemana, ");
		sqlStr.append(" CASE WHEN (abonado = true) THEN 'Abonado' WHEN (justificado = true) THEN 'Justificado' END AS situacaoAbonoFalta from frequenciaaula ");
		sqlStr.append(" inner join registroaula on registroaula.codigo = frequenciaaula.registroaula ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = registroaula.disciplina ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = registroaula.professor ");
		sqlStr.append(" inner join turma on turma.codigo = registroaula.turma ");
		sqlStr.append(" inner join matricula on matricula.matricula = frequenciaaula.matricula ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo and matriculaperiodoturmadisciplina.codigo = frequenciaaula.matriculaperiodoturmadisciplina ");
		sqlStr.append(" inner join historico on historico.matricula = matricula.matricula and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" where frequenciaaula.presente = false  ");
		sqlStr.append(" and frequenciaaula.abonado = false ");
		if (!semestre.equals("")) {
			sqlStr.append(" and RegistroAula.semestre = '");
			sqlStr.append(semestre);
			sqlStr.append("'");
		}
		if (!ano.equals("")) {
			sqlStr.append(" and RegistroAula.ano = '");
			sqlStr.append(ano);
			sqlStr.append("'");
		}
		if (turma != 0) {
			sqlStr.append(" and RegistroAula.turma = " + turma);
		}else {
			sqlStr.append(" and RegistroAula.turmaorigem is null ");
		}
		sqlStr.append(" and frequenciaaula.matricula = '");
		sqlStr.append(matriculaAluno);
		sqlStr.append("'");
		MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and ");
		if(Uteis.isAtributoPreenchido(codigoDisciplina)) {
			sqlStr.append("AND (disciplina.codigo = ").append(codigoDisciplina).append(" ");
			sqlStr.append(" or (turma.turmaagrupada = true and disciplina.codigo in (select disciplina from disciplinaequivalente where equivalente = ").append(codigoDisciplina).append("))  ");
			sqlStr.append(" or (turma.turmaagrupada = true and disciplina.codigo in (select equivalente from disciplinaequivalente where disciplina = ").append(codigoDisciplina).append("))  ");			
			if (turma != 0) {
				sqlStr.append(" or (turma.turmaagrupada = true and disciplina.codigo in (select distinct disciplinaequivalenteTurmaagrupada from turmadisciplina where turma = ").append(turma).append(" and turmadisciplina.disciplina = ").append(codigoDisciplina.intValue()).append(")) ");
		}
			sqlStr.append(" )  ");
		}
		sqlStr.append(" group by registroaula.data, turma.identificadorturma, disciplina.nome, registroaula.ano, registroaula.semestre, registroaula.horario, registroaula.nrAula , registroaula.diasemana, frequenciaaula.abonado, frequenciaaula.justificado ");
		sqlStr.append(" order by disciplina.nome, registroaula.data, registroaula.horario");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapidaFaltas(tabelaResultado);
	}

	public List<RegistroAulaVO> consultaRapidaFaltasAlunoQuantidade(Integer turma, String matriculaAluno, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();		
		sqlStr.append(" select distinct disciplina.nome as disciplina, disciplina.codigo, ");
		sqlStr.append(" array_to_string(array_agg(distinct pessoa.nome), ', ') as professor, ");
		sqlStr.append(" registroaula.ano, registroaula.semestre, registroaula.turma, turma.identificadorturma, ");
		sqlStr.append(" (select count (distinct rg.codigo) from frequenciaaula fa ");
		sqlStr.append(" inner join registroaula rg on rg.codigo = fa.registroaula ");
		sqlStr.append(" inner join disciplina d on d.codigo = rg.disciplina ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = rg.professor  ");
		sqlStr.append(" left join disciplinaequivalente de1 on	rg.disciplina = de1.disciplina ");
		sqlStr.append(" where fa.presente = false ");
		sqlStr.append(" and fa.abonado = false ");
		sqlStr.append(" and fa.matricula = '").append(matriculaAluno).append("' ");
		sqlStr.append(" and rg.ano = registroaula.ano ");
		sqlStr.append(" and rg.semestre = registroaula.semestre ");
		if (turma != 0) {
			sqlStr.append(" and rg.turma = ").append(turma);
		}
		sqlStr.append(" and (disciplina.codigo = d.codigo or disciplina.codigo = de1.equivalente) ");
		sqlStr.append(" and rg.turma = registroaula.turma) as qtdFaltas ");
		sqlStr.append(" from frequenciaaula ");
		sqlStr.append(" inner join registroaula on registroaula.codigo = frequenciaaula.registroaula ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = registroaula.professor ");
		sqlStr.append(" inner join turma on turma.codigo = registroaula.turma ");
		sqlStr.append(" left join disciplinaequivalente  on turma.turmaagrupada and turma.subturma and registroaula.disciplina = disciplinaequivalente.disciplina ");
		sqlStr.append(" left join disciplinaequivalente discequi2  on turma.turmaagrupada and turma.subturma and registroaula.disciplina = discequi2.equivalente ");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina mptd on mptd.matricula = frequenciaaula.matricula ");
		sqlStr.append(" and (mptd.disciplina = registroaula.disciplina  or (disciplinaequivalente.equivalente is not null and mptd.disciplina = disciplinaequivalente.equivalente) or (discequi2.disciplina is not null and mptd.disciplina = discequi2.disciplina))");
		sqlStr.append(" and ((mptd.turmapratica is null and mptd.turmateorica is null and mptd.turma = registroaula.turma) ");
		sqlStr.append(" or (mptd.turmapratica is not null and mptd.turmapratica = registroaula.turma) ");
		sqlStr.append(" or (mptd.turmateorica is not null and mptd.turmateorica = registroaula.turma)) ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = mptd.matriculaperiodo ");
		sqlStr.append(" and matriculaperiodo.ano = RegistroAula.ano and matriculaperiodo.semestre = RegistroAula.semestre ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = mptd.disciplina ");
		sqlStr.append(" where 1=1 ");
		if (!semestre.equals("")) {
			sqlStr.append(" and RegistroAula.semestre = '").append(semestre).append("' ");
		}
		if (!ano.equals("")) {
			sqlStr.append(" and RegistroAula.ano = '").append(ano).append("' ");
		}
		if (turma != 0) {
			sqlStr.append(" and registroaula.turma = ").append(turma);
		}
		sqlStr.append(" and frequenciaaula.matricula = '").append(matriculaAluno).append("' ");
		sqlStr.append(" group by disciplina.nome, disciplina.codigo, registroaula.ano, registroaula.semestre, registroaula.turma, turma.identificadorturma ");
		sqlStr.append(" order by disciplina.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapidaFaltasQtd(tabelaResultado);
	}

	public Boolean consultarExistenciaRegistroAula(String matricula, Integer turma, Integer disciplina, Integer professor, String semestre, String ano) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT ra.codigo FROM registroaula ra ");
		sqlStr.append("INNER JOIN frequenciaaula fa ON fa.registroaula = ra.codigo ");
		sqlStr.append("INNER JOIN turma ON turma.codigo = ra.turma ");
		sqlStr.append("WHERE 1=1 ");
		if (matricula != null && !matricula.equals("")) {
			sqlStr.append("AND fa.matricula = '").append(matricula).append("' ");
		}
		if (turma != null && !turma.equals(0)) {
			sqlStr.append("AND ra.turma = ").append(turma).append(" ");
		}
		if (professor != null && !professor.equals(0)) {
			sqlStr.append("AND ra.professor = ").append(professor).append(" ");
		}
		if (disciplina != null && !disciplina.equals(0)) {
			sqlStr.append("AND (ra.disciplina = ").append(disciplina).append(" ");
			sqlStr.append(" or (turma.turmaagrupada = true and ra.disciplina in (select disciplina from disciplinaequivalente where equivalente = ").append(disciplina).append("))  ");
			sqlStr.append(" or (turma.turmaagrupada = true and ra.disciplina in (select equivalente from disciplinaequivalente where disciplina = ").append(disciplina).append("))  ");			
			sqlStr.append(" or (turma.turmaagrupada = true and ra.disciplina in (select distinct disciplinaequivalenteTurmaagrupada from turmadisciplina where turma = ").append(turma).append(" and turmadisciplina.disciplina = ").append(disciplina.intValue()).append(")) ");
			sqlStr.append(" )  ");
		}
		if (semestre != null && !semestre.equals("")) {
			sqlStr.append("AND ra.semestre = '").append(semestre).append("' ");
		}
		if (ano != null && !ano.equals("")) {
			sqlStr.append("AND ra.ano = '").append(ano).append("' ");
		}
		sqlStr.append("LIMIT 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	public List<RegistroAulaVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado) throws Exception {
		List<RegistroAulaVO> vetResultado = new ArrayList<RegistroAulaVO>(0);
		while (tabelaResultado.next()) {
			RegistroAulaVO obj = new RegistroAulaVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<RegistroAulaVO> montarDadosConsultaRapidaFaltas(SqlRowSet tabelaResultado) throws Exception {
		List<RegistroAulaVO> vetResultado = new ArrayList<RegistroAulaVO>(0);
		while (tabelaResultado.next()) {
			RegistroAulaVO obj = new RegistroAulaVO();
			montarDadosBasicoFaltas(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<RegistroAulaVO> montarDadosConsultaRapidaFaltasQtd(SqlRowSet tabelaResultado) throws Exception {
		List<RegistroAulaVO> vetResultado = new ArrayList<RegistroAulaVO>(0);
		while (tabelaResultado.next()) {
			RegistroAulaVO obj = new RegistroAulaVO();
			montarDadosBasicoFaltasQtd(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public RegistroAulaVO consultarPorChavePrimaria(Integer codRegistroAula, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		RegistroAulaVO obj = new RegistroAulaVO();
		obj.setCodigo(codRegistroAula);
		carregarDados(obj, nivelMontarDados, usuario);
		return obj;
	}

	public void carregarDados(RegistroAulaVO obj, UsuarioVO usuario) throws Exception {
		carregarDados((RegistroAulaVO) obj, NivelMontarDados.TODOS, usuario);
	}

	/**
	 * Método responsavel por validar se o Nivel de Montar Dados é Básico ou
	 * Completo e faz a consulta de acordo com o nível especificado.
	 * 
	 * @param obj
	 * @param nivelMontarDados
	 * @throws Exception
	 * @author Carlos
	 */
	public void carregarDados(RegistroAulaVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		carregarDados(obj, nivelMontarDados, usuario, false, true, null, null, "", "", obj.getTurma().getTurmaAgrupada(), false, true);
	}

	/**
	 * Método responsavel por validar se o Nivel de Montar Dados é Básico ou
	 * Completo e faz a consulta de acordo com o nível especificado.
	 * 
	 * @param obj
	 * @param nivelMontarDados
	 * @throws Exception
	 * @author Carlos
	 */
	@Override
	public void carregarDados(RegistroAulaVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, String tipoLayout, String tipoAluno, Boolean turmaAgrupada, Boolean liberarRegistroAulaEntrePeriodo, Boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception {
		SqlRowSet resultado = null;
		if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
			montarDadosBasico((RegistroAulaVO) obj, resultado);
		}
		if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj, usuario, apenasAlunosAtivos, trazerAlunosPendentesFinanceiramente, filtroVisaoProfessor, filtroTipoCursoAluno, tipoLayout, tipoAluno, turmaAgrupada, liberarRegistroAulaEntrePeriodo, permitirRealizarLancamentoAlunosPreMatriculados);
			montarDadosCompleto((RegistroAulaVO) obj, resultado);
		}
	}

	public void carregarDadosVisaoAdministrativa(RegistroAulaVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, String tipoLayout, String tipoAluno, Boolean turmaAgrupada, Boolean liberarRegistroAulaEntrePeriodo, FiltroRelatorioAcademicoVO filtroAcademicoVO) throws Exception {
		SqlRowSet resultado = null;
		if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
			montarDadosBasico((RegistroAulaVO) obj, resultado);
		}
		if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletosVisaoAdministrativa(obj.getCodigo(), usuario, filtroTipoCursoAluno, tipoLayout, tipoAluno, turmaAgrupada, liberarRegistroAulaEntrePeriodo, filtroAcademicoVO);
			montarDadosCompleto((RegistroAulaVO) obj, resultado);
		}
	}
	
	private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codRegistroAula, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (registroAula.codigo= ").append(codRegistroAula).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(RegistroAulaVO obj, UsuarioVO usuario, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, String tipoLayout, String tipoAluno, Boolean turmaAgrupada, Boolean liberarRegistroAulaEntrePeriodo, Boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		if (filtroVisaoProfessor != null && filtroVisaoProfessor) {
			sqlStr.append((" Inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula  "));
			if (!liberarRegistroAulaEntrePeriodo) {
				sqlStr.append(" and matriculaperiodo.ano = regAula.ano and matriculaperiodo.semestre = regAula.semestre ");
			}
			if (tipoAluno.equals("normal")) {
				sqlStr.append(" and matriculaperiodo.turma = turma.codigo ");
			}
		} else if ((filtroVisaoProfessor == null || !filtroVisaoProfessor) && (tipoLayout.equals("DiarioModRetratoRel") || tipoLayout.equals("DiarioRel") || ((apenasAlunosAtivos != null && apenasAlunosAtivos) && (trazerAlunosPendentesFinanceiramente == null || !trazerAlunosPendentesFinanceiramente)))) {
			if (!liberarRegistroAulaEntrePeriodo) {
				sqlStr.append(" left join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.ano = regAula.ano and matriculaperiodo.semestre = regAula.semestre ");
			} else {
				sqlStr.append(" left join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
			}
			if (tipoAluno.equals("normal")) {
				sqlStr.append(" and matriculaperiodo.turma = turma.codigo ");
			}
		} else {
			sqlStr.append((" Inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula  "));
			if (!liberarRegistroAulaEntrePeriodo) {
				sqlStr.append(" and matriculaperiodo.ano = regAula.ano and matriculaperiodo.semestre = regAula.semestre ");
			}
			if (tipoAluno.equals("normal")) {
				sqlStr.append(" and matriculaperiodo.turma = turma.codigo ");
			}
		}
		getFacadeFactory().getTurmaFacade().carregarDados(obj.getTurma(), usuario);
		if ((turmaAgrupada || obj.getTurma().getTurmaAgrupada()) && !obj.getTurma().getSubturma()) {
			sqlStr.append("inner JOIN MatriculaPeriodoTurmaDisciplina ON MatriculaPeriodoTurmaDisciplina.turma = t1.codigo  AND MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula ");
		 	sqlStr.append(" AND (MatriculaPeriodoTurmaDisciplina.disciplinacomposta is null  or MatriculaPeriodoTurmaDisciplina.disciplinacomposta = false) ");
			sqlStr.append(" and (MatriculaPeriodoTurmaDisciplina.disciplina = ").append(obj.getDisciplina().getCodigo());
			sqlStr.append(" or MatriculaPeriodoTurmaDisciplina.disciplina in (select distinct disciplinaequivalenteTurmaagrupada from turmadisciplina where turmadisciplina.turma = ").append(obj.getTurma().getCodigo()).append(" and turmadisciplina.disciplina = ").append(obj.getDisciplina().getCodigo()).append(") ");
			sqlStr.append(" or MatriculaPeriodoTurmaDisciplina.disciplina in (select distinct equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(obj.getDisciplina().getCodigo().intValue()).append(") ");
			sqlStr.append(" or MatriculaPeriodoTurmaDisciplina.disciplina in (select distinct disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(obj.getDisciplina().getCodigo().intValue()).append(") ");			
			sqlStr.append(" ) ");
			
//		 	sqlStr.append(" AND (MatriculaPeriodoTurmaDisciplina.disciplina = disciplina.codigo OR matriculaperiodoturmadisciplina.disciplina IN ");
//		 	sqlStr.append("(SELECT DISTINCT turmadisciplina.disciplinaEquivalenteTurmaAgrupada FROM turmadisciplina WHERE turmadisciplina.turma = ").append(obj.getTurma().getCodigo());
//		    sqlStr.append(" AND turmadisciplina.disciplina = ").append(obj.getDisciplina().getCodigo()).append("))");
			
			
		} else {
			if (obj.getTurma().getSubturma()) {
				if (obj.getTurma().getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
					
					sqlStr.append(" inner JOIN MatriculaPeriodoTurmaDisciplina ON MatriculaPeriodoTurmaDisciplina.turmaTeorica = turma.codigo and MatriculaPeriodoTurmaDisciplina.disciplina = disciplina.codigo and MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula ");
				} else if (obj.getTurma().getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
					sqlStr.append(" inner JOIN MatriculaPeriodoTurmaDisciplina ON MatriculaPeriodoTurmaDisciplina.turmaPratica = turma.codigo and MatriculaPeriodoTurmaDisciplina.disciplina = disciplina.codigo and MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula ");
				} else {
					sqlStr.append("inner JOIN MatriculaPeriodoTurmaDisciplina ON MatriculaPeriodoTurmaDisciplina.turma = turma.codigo and MatriculaPeriodoTurmaDisciplina.disciplina = disciplina.codigo and MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula ");
				}
			} else {
				sqlStr.append("inner JOIN MatriculaPeriodoTurmaDisciplina ON MatriculaPeriodoTurmaDisciplina.turma = turma.codigo and MatriculaPeriodoTurmaDisciplina.disciplina = disciplina.codigo and MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula ");
				
				if (tipoAluno.equals("reposicao")) {
					sqlStr.append(" and matriculaperiodoturmadisciplina.turma = turma.codigo ");
				}
			}
			
		}
		if ((filtroVisaoProfessor != null && filtroVisaoProfessor) || ((filtroVisaoProfessor == null || !filtroVisaoProfessor) && (tipoLayout.equals("DiarioModRetratoRel") || tipoLayout.equals("DiarioRel") || ((apenasAlunosAtivos != null && apenasAlunosAtivos) && (trazerAlunosPendentesFinanceiramente == null || !trazerAlunosPendentesFinanceiramente))))) {
			sqlStr.append("and MatriculaPeriodoTurmaDisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		}

		if (Uteis.isAtributoPreenchido(obj.getTurma())) {
		if (obj.getTurma().getIntegral()) {
			sqlStr.append("and MatriculaPeriodoTurmaDisciplina.matriculaperiodo = ( ");
			sqlStr.append("select mp.codigo from matriculaperiodo  mp  ");
			sqlStr.append("inner join MatriculaPeriodoTurmaDisciplina mptd on mptd.matriculaperiodo = mp.codigo  ");
			sqlStr.append("and mptd.turma = MatriculaPeriodoTurmaDisciplina.turma and mptd.disciplina = MatriculaPeriodoTurmaDisciplina.disciplina ");
			sqlStr.append("where mp.matricula = matricula.matricula ");
			sqlStr.append("order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1 )");
		}		
		}
		sqlStr.append("inner JOIN Historico ON Historico.matricula = matricula.matricula AND Historico.MatriculaPeriodoTurmaDisciplina = MatriculaPeriodoTurmaDisciplina.codigo  ");
		sqlStr.append(" AND (historico.historicodisciplinacomposta is null  or historico.historicodisciplinacomposta = false) ");
		/**
		 * Adicionada regra para resolver impactos relacionados a alunos que
		 * estão Cursando por Correspondência e que disciplinas saiam duplicadas
		 * no Boletim Acadêmico
		 */
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));		
		sqlStr.append(" WHERE (regAula.codigo= ").append(obj.getCodigo()).append(")");
		if (tipoLayout.equals("DiarioModRetratoRel") && (filtroVisaoProfessor != null && filtroVisaoProfessor)) {
			sqlStr.append(" and regAula.turma = matriculaperiodo.turma ");
		}
		sqlStr.append(" and CASE WHEN (MatriculaPeriodoTurmaDisciplina.codigo is not null) then Historico.situacao not in ('AA', 'CC', 'CH', 'IS', 'AB') else (1=1) end  ");
		sqlStr.append(" and matriculaperiodo.situacaoMatriculaPeriodo <> 'PC' ");
		if ((filtroVisaoProfessor != null && filtroVisaoProfessor) && !permitirRealizarLancamentoAlunosPreMatriculados) {
			sqlStr.append(" and matriculaperiodo.situacaoMatriculaPeriodo <> 'PR' ");			
		}
		
		
		
		if (filtroVisaoProfessor == null || !filtroVisaoProfessor) {
			if (apenasAlunosAtivos != null && apenasAlunosAtivos) {
				sqlStr.append(" and matricula.situacao = 'AT' ");
			}
			if (!trazerAlunosPendentesFinanceiramente) {
				sqlStr.append(" and matriculaPeriodo.situacao <> 'PF' ");
			}				
		}
		if (Uteis.isAtributoPreenchido(trazerAlunosPendentesFinanceiramente) && !trazerAlunosPendentesFinanceiramente) {
			sqlStr.append(" and matriculaPeriodo.situacao <> 'PF' ");
		}
		if (filtroTipoCursoAluno != null && !filtroTipoCursoAluno.equals("") && !filtroTipoCursoAluno.equals("todos")) {
			if (filtroTipoCursoAluno.equals("posGraduacao")) {
				sqlStr.append(" AND matricula.tipomatricula = 'NO'");
			} else if (filtroTipoCursoAluno.equals("extensao")) {
				sqlStr.append(" AND matricula.tipoMatricula = 'EX' ");
			} else if (filtroTipoCursoAluno.equals("modular")) {
				sqlStr.append(" AND matricula.tipoMatricula = 'MO' ");
			}
		}
		if (tipoAluno.equals("normal")) {
			sqlStr.append(" and matriculaperiodoturmadisciplina.turma = matriculaperiodo.turma");
		} else if (tipoAluno.equals("reposicao")) {
			sqlStr.append(" and matriculaperiodoturmadisciplina.turma <> matriculaperiodo.turma");
		}
		/// Adicionado esse and para evitar de trazer frequencia aula cujo não exista correspondencia no historico, pois isso ocasiona erro ao gravar o registro aula nota pós.
		sqlStr.append(" and historico.codigo is not null ");
		
		SqlRowSet tabelaResultado = null;
		if (turmaAgrupada || obj.getTurma().getTurmaAgrupada()) {
			StringBuffer sql = new StringBuffer();
			sql.append("select t.* from ");
			sql.append("(").append(sqlStr).append(") as t ");
			sql.append(" where  (exists (").append(sqlStr).append(" and historico.disciplina = t.\"matriculaperiodoturmadisciplina.disciplina\" and historico.disciplina = ").append(obj.getDisciplina().getCodigo()).append(" and historico.matricula = t.\"matricula.matricula\" limit 1) ") ;
			sql.append(" or not exists (").append(sqlStr).append(" and historico.disciplina = ").append(obj.getDisciplina().getCodigo()).append(" and historico.matricula = t.\"matricula.matricula\" limit 1)) ") ;
			sql.append("  order by t.\"aluno.nome\" ");			
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());			
		}else {
			//System.out.println(sqlStr.toString());
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			sqlStr.append("  order by matricula.tipomatricula desc , aluno.nome ");
		}
//		System.out.println(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Registro Aula - "+obj.getCodigo()+").");
		}
		return tabelaResultado;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletosVisaoAdministrativa(Integer codRegistroAula, UsuarioVO usuario, String filtroTipoCursoAluno, String tipoLayout, String tipoAluno, Boolean turmaAgrupada, Boolean liberarRegistroAulaEntrePeriodo, FiltroRelatorioAcademicoVO filtroAcademicoVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		if ((tipoLayout.equals("DiarioModRetratoRel") || tipoLayout.equals("DiarioRel"))) {
			if (!liberarRegistroAulaEntrePeriodo) {
				sqlStr.append(" left join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.ano = regAula.ano and matriculaperiodo.semestre = regAula.semestre ");
			} else {
				sqlStr.append(" left join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
			}
			if (tipoAluno.equals("normal")) {
				sqlStr.append(" and matriculaperiodo.turma = turma.codigo ");
			}
		}
		if (turmaAgrupada) {
			sqlStr.append("LEFT JOIN MatriculaPeriodoTurmaDisciplina ON MatriculaPeriodoTurmaDisciplina.turma = t1.codigo and MatriculaPeriodoTurmaDisciplina.disciplina = disciplina.codigo and MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula ");
		} else {
			sqlStr.append("LEFT JOIN MatriculaPeriodoTurmaDisciplina ON MatriculaPeriodoTurmaDisciplina.turma = turma.codigo and MatriculaPeriodoTurmaDisciplina.disciplina = disciplina.codigo and MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula ");
			if (tipoAluno.equals("reposicao")) {
				sqlStr.append(" and matriculaperiodoturmadisciplina.turma = turma.codigo ");
			}
		}
		if ((tipoLayout.equals("DiarioModRetratoRel") || tipoLayout.equals("DiarioRel"))) {
			sqlStr.append("and MatriculaPeriodoTurmaDisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		}
		sqlStr.append(" LEFT JOIN Historico ON Historico.matricula = matricula.matricula  ");
		sqlStr.append(" and Historico.disciplina = MatriculaPeriodoTurmaDisciplina.disciplina ");
		sqlStr.append(" and  Historico.matriculaperiodo = matriculaperiodo.codigo ");
		/**
		 * Adicionada regra para resolver impactos relacionados a alunos que
		 * estão Cursando por Correspondência e que disciplinas saiam duplicadas
		 * no Boletim Acadêmico
		 */
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));		

		sqlStr.append(" WHERE (regAula.codigo= ").append(codRegistroAula).append(")");
		sqlStr.append(" and CASE WHEN (MatriculaPeriodoTurmaDisciplina.codigo is not null) then Historico.situacao <> 'AA' else (1=1) end  ");
		
		
		sqlStr.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		sqlStr.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));		
		if (filtroTipoCursoAluno != null && !filtroTipoCursoAluno.equals("") && !filtroTipoCursoAluno.equals("todos")) {
			if (filtroTipoCursoAluno.equals("posGraduacao")) {
				sqlStr.append(" AND matricula.tipomatricula = 'NO'");
			} else if (filtroTipoCursoAluno.equals("extensao")) {
				sqlStr.append(" AND matricula.tipoMatricula = 'EX' ");
			} else if (filtroTipoCursoAluno.equals("modular")) {
				sqlStr.append(" AND matricula.tipoMatricula = 'MO' ");
			}
		}
		if (tipoAluno.equals("normal")) {
			sqlStr.append(" and matriculaperiodoturmadisciplina.turma = matriculaperiodo.turma");
		} else if (tipoAluno.equals("reposicao")) {
			sqlStr.append(" and matriculaperiodoturmadisciplina.turma <> matriculaperiodo.turma");
		}
		sqlStr.append(" order by matricula.tipomatricula desc , aluno.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	/**
	 * Consulta que espera um ResultSet com os campos mínimos para uma consulta
	 * rápida e intelegente. Desta maneira, a mesma será sempre capaz de montar
	 * os atributos básicos do objeto e alguns atributos relacionados de
	 * relevância para o contexto da aplicação.
	 * 
	 * @param obj
	 * @throws Exception
	 */
	private void montarDadosBasico(RegistroAulaVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados do RegistroAula
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setData(dadosSQL.getDate("data"));
		obj.setHorario(dadosSQL.getString("horario"));
		obj.setNrAula(dadosSQL.getInt("nrAula"));
		obj.setCargaHoraria(dadosSQL.getInt("cargaHoraria"));
		// Dados da Turma
		obj.getTurma().setCodigo(dadosSQL.getInt("turma.codigo"));
		obj.getTurma().setIdentificadorTurma(dadosSQL.getString("turma.identificadorTurma"));
		// Dados da Disciplina
		obj.getDisciplina().setCodigo(dadosSQL.getInt("disciplina.codigo"));
		obj.getDisciplina().setNome(dadosSQL.getString("disciplina.nome"));
	}

	private void montarDadosBasicoFaltas(RegistroAulaVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados do RegistroAula
		obj.setData(dadosSQL.getDate("data"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.setHorario(dadosSQL.getString("horario"));
		obj.setNrAula(dadosSQL.getInt("nrAula"));
		obj.setDiaSemana(dadosSQL.getString("diaSemana"));
		obj.getProfessor().setNome(dadosSQL.getString("professor"));
		obj.getDisciplina().setNome(dadosSQL.getString("disciplina"));
		obj.getTurma().setIdentificadorTurma(dadosSQL.getString("identificadorturma"));
		obj.setSituacaoAbonoFalta(dadosSQL.getString("situacaoAbonoFalta"));
	}

	private void montarDadosBasicoFaltasQtd(RegistroAulaVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados do RegistroAula
		obj.setQtdFaltas(dadosSQL.getInt("qtdfaltas"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.getProfessor().setNome(dadosSQL.getString("professor"));
		obj.getDisciplina().setCodigo(dadosSQL.getInt("codigo"));
		obj.getDisciplina().setNome(dadosSQL.getString("disciplina"));
		obj.getTurma().setCodigo(dadosSQL.getInt("turma"));
		obj.getTurma().setIdentificadorTurma(dadosSQL.getString("identificadorturma"));
	}

	/**
	 * Consulta que espera um ResultSet com todos os campos e dados de objetos
	 * relacionados, Para reconstituir o objeto por completo, de uma determinada
	 * entidade.
	 * 
	 * @param obj
	 * @throws Exception
	 */
	private void montarDadosCompleto(RegistroAulaVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados do Registro Aula
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setData(dadosSQL.getDate("data"));
		obj.setDiaSemana(dadosSQL.getString("diaSemana"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.setHorario(dadosSQL.getString("horario"));
		obj.setNrAula(dadosSQL.getInt("nrAula"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setTipoAula(dadosSQL.getString("tipoAula"));
		obj.setCargaHoraria(dadosSQL.getInt("cargaHoraria"));
		obj.setConteudo(dadosSQL.getString("conteudo"));
		obj.setDataRegistroAula(dadosSQL.getDate("dataRegistroAula"));
		obj.setAtividadeComplementar(dadosSQL.getBoolean("atividadeComplementar"));
		obj.setPraticaSupervisionada(dadosSQL.getString("praticaSupervisionada"));
		// Dados da Turma
		obj.getTurma().setCodigo(dadosSQL.getInt("turma.codigo"));
		obj.getTurma().setIdentificadorTurma(dadosSQL.getString("turma.identificadorTurma"));
		obj.getTurma().setTurmaAgrupada(dadosSQL.getBoolean("turma.turmaAgrupada"));
		obj.getTurma().setSemestral(dadosSQL.getBoolean("turma.semestral"));
		// Dados da Unidade
		obj.getTurma().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
		obj.getTurma().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));
		// Dados do Curso
		obj.getTurma().getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getTurma().getCurso().setNome(dadosSQL.getString("curso.nome"));
		obj.getTurma().getCurso().setNivelEducacional(dadosSQL.getString("curso.nivelEducacional"));
		obj.getTurma().getCurso().getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("curso.configuracaoAcademico"));
		// Dados do Turno
		obj.getTurma().getTurno().setNome(dadosSQL.getString("turno.nome"));
		// Dados do Periodo Letivo
		obj.getTurma().getPeridoLetivo().setCodigo(dadosSQL.getInt("periodoLetivo.codigo"));
		obj.getTurma().getPeridoLetivo().setDescricao(dadosSQL.getString("periodoLetivo.descricao"));
		// Dados da Pessoa
		obj.getProfessor().setCodigo(dadosSQL.getInt("professor.codigo"));
		obj.getProfessor().setNome(dadosSQL.getString("professor.nome"));
		// Dados do Usuario
		obj.getResponsavelRegistroAula().setCodigo(dadosSQL.getInt("responsavelRegistroAula.codigo"));
		obj.getResponsavelRegistroAula().setNome(dadosSQL.getString("responsavelRegistroAula.nome"));
		// Dados da Disciplinas
		obj.getDisciplina().setCodigo(dadosSQL.getInt("disciplina.codigo"));
		obj.getDisciplina().setNome(dadosSQL.getString("disciplina.nome"));
		obj.setNovoObj(Boolean.FALSE);

		FrequenciaAulaVO frequenciaAulaVO = null;		
		do {
			frequenciaAulaVO = new FrequenciaAulaVO();

			frequenciaAulaVO.getMatricula().setMatricula(dadosSQL.getString("matricula.matricula"));
			frequenciaAulaVO.getMatricula().getAluno().setNome(dadosSQL.getString("aluno.nome"));
			frequenciaAulaVO.getMatricula().setUpdated(dadosSQL.getTimestamp("matricula.updated"));
			frequenciaAulaVO.getMatricula().setTipoMatricula(dadosSQL.getString("matricula.tipoMatricula"));
			frequenciaAulaVO.getMatricula().setData(dadosSQL.getTimestamp("matricula.data"));
			// frequenciaAulaVO.getMatricula().set(dadosSQL.getBoolean("matricula.extensao"));
			frequenciaAulaVO.setPresente(dadosSQL.getBoolean("frequenciaaula.presente"));
			frequenciaAulaVO.setAbonado(dadosSQL.getBoolean("frequenciaaula.abonado"));
			frequenciaAulaVO.setJustificado(dadosSQL.getBoolean("frequenciaaula.justificado"));			
			frequenciaAulaVO.setNovoObj(Boolean.FALSE);
			frequenciaAulaVO.getMatricula().getAluno().getArquivoImagem().setCodigo(dadosSQL.getInt("codigoarquivo"));
			frequenciaAulaVO.getMatricula().getAluno().getArquivoImagem().setNome(dadosSQL.getString("nomearquivo"));
			frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().setCodigo(dadosSQL.getInt("matriculaPeriodoTurmaDisciplina.codigo"));			
			frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().setCodigo(dadosSQL.getInt("matriculaPeriodoTurmaDisciplina.turma"));			
			frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaPratica().setCodigo(dadosSQL.getInt("matriculaPeriodoTurmaDisciplina.turmaPratica"));			
			frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaTeorica().setCodigo(dadosSQL.getInt("matriculaPeriodoTurmaDisciplina.turmaTeorica"));
			frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().setMatriculaObjetoVO(frequenciaAulaVO.getMatricula());
			boolean existe =  false;
			for(FrequenciaAulaVO frequenciaAulaVO2: obj.getFrequenciaAulaVOs()) {
				if(frequenciaAulaVO2.getMatricula().getMatricula().equals(frequenciaAulaVO.getMatricula().getMatricula())) {
					existe =  true;
					break;
				}
			}
			if(!existe) {
				obj.getFrequenciaAulaVOs().add(frequenciaAulaVO);
			}
		} while (dadosSQL.next());
	}

	private void montarDadosCompletoDiario(RegistroAulaVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados do Registro Aula
		if (obj.getCodigo() == 0) {
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.setData(dadosSQL.getDate("data"));
			obj.setDiaSemana(dadosSQL.getString("diaSemana"));
			obj.setSemestre(dadosSQL.getString("semestre"));
			obj.setHorario(dadosSQL.getString("horario"));
			obj.setNrAula(dadosSQL.getInt("nrAula"));
			obj.setAno(dadosSQL.getString("ano"));
			obj.setTipoAula(dadosSQL.getString("tipoAula"));
			obj.setCargaHoraria(dadosSQL.getInt("cargaHoraria"));
			obj.setConteudo(dadosSQL.getString("conteudo"));
			obj.setDataRegistroAula(dadosSQL.getDate("dataRegistroAula"));
			obj.setAtividadeComplementar(dadosSQL.getBoolean("atividadeComplementar"));
			obj.setPraticaSupervisionada(dadosSQL.getString("praticaSupervisionada"));
			// Dados da Turma
			obj.getTurma().setCodigo(dadosSQL.getInt("turma.codigo"));
			obj.getTurma().setIdentificadorTurma(dadosSQL.getString("turma.identificadorTurma"));
			obj.getTurma().setTurmaAgrupada(dadosSQL.getBoolean("turma.turmaAgrupada"));
			obj.getTurma().setSemestral(dadosSQL.getBoolean("turma.semestral"));
			// Dados da Unidade
			obj.getTurma().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
			obj.getTurma().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));
			// Dados do Curso
			obj.getTurma().getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
			obj.getTurma().getCurso().setNome(dadosSQL.getString("curso.nome"));
			obj.getTurma().getCurso().setNivelEducacional(dadosSQL.getString("curso.nivelEducacional"));
			obj.getTurma().getCurso().getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("curso.configuracaoAcademico"));
			// Dados do Turno
			obj.getTurma().getTurno().setNome(dadosSQL.getString("turno.nome"));
			// Dados do Periodo Letivo
			obj.getTurma().getPeridoLetivo().setCodigo(dadosSQL.getInt("periodoLetivo.codigo"));
			obj.getTurma().getPeridoLetivo().setDescricao(dadosSQL.getString("periodoLetivo.descricao"));
			// Dados da Pessoa
			obj.getProfessor().setCodigo(dadosSQL.getInt("professor.codigo"));
			obj.getProfessor().setNome(dadosSQL.getString("professor.nome"));
			// Dados do Usuario
			obj.getResponsavelRegistroAula().setCodigo(dadosSQL.getInt("responsavelRegistroAula.codigo"));
			obj.getResponsavelRegistroAula().setNome(dadosSQL.getString("responsavelRegistroAula.nome"));
			// Dados da Disciplinas
			obj.getDisciplina().setCodigo(dadosSQL.getInt("disciplina.codigo"));
			obj.getDisciplina().setNome(dadosSQL.getString("disciplina.nome"));
			obj.getGradeDisciplinaVO().setCargaHoraria(dadosSQL.getInt("gradedisciplina.cargaHoraria"));
			obj.getGradeDisciplinaVO().getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("gradedisciplina.configuracaoAcademico"));
			if(dadosSQL.getInt("configuracaoAcademico") > 0){
				obj.getGradeDisciplinaVO().getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("configuracaoAcademico"));
				obj.getTurma().getCurso().getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("configuracaoAcademico"));
			}
			obj.setNovoObj(Boolean.FALSE);
		}
		FrequenciaAulaVO frequenciaAulaVO = null;
		frequenciaAulaVO = new FrequenciaAulaVO();
		frequenciaAulaVO.getMatricula().setMatricula(dadosSQL.getString("matricula_matricula"));
		frequenciaAulaVO.getMatricula().getAluno().setNome(dadosSQL.getString("aluno.nome"));
		frequenciaAulaVO.getMatricula().setUpdated(dadosSQL.getTimestamp("matricula.updated"));
		frequenciaAulaVO.getMatricula().setTipoMatricula(dadosSQL.getString("matricula.tipoMatricula"));
		frequenciaAulaVO.getMatricula().setData(dadosSQL.getTimestamp("matricula.data"));
		frequenciaAulaVO.getMatricula().setSituacao(dadosSQL.getString("matricula.situacao"));
		frequenciaAulaVO.setPresente(dadosSQL.getBoolean("frequenciaaula.presente"));
		frequenciaAulaVO.setAbonado(dadosSQL.getBoolean("frequenciaaula.abonado"));
		frequenciaAulaVO.setJustificado(dadosSQL.getBoolean("frequenciaaula.justificado"));
		frequenciaAulaVO.setMatriculaPeriodoTurmaDisciplina(dadosSQL.getInt("matriculaPeriodoTurmaDisciplina"));
		frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().setCodigo(dadosSQL.getInt("matriculaPeriodoTurmaDisciplina"));
		frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().setSituacaoMatriculaPeriodo(dadosSQL.getString("situacaoMatriculaPeriodo"));
		
		frequenciaAulaVO.getHistoricoVO().setCodigo(new Integer(dadosSQL.getInt("historico")));
		frequenciaAulaVO.getHistoricoVO().setInstituicao(dadosSQL.getString("instituicao"));
		frequenciaAulaVO.getHistoricoVO().setTipoHistorico(dadosSQL.getString("tipoHistorico"));
		frequenciaAulaVO.getHistoricoVO().setSituacao(dadosSQL.getString("situacao"));
		frequenciaAulaVO.getHistoricoVO().setFreguencia(new Double(dadosSQL.getDouble("freguencia")));
		frequenciaAulaVO.getHistoricoVO().setDataRegistro(dadosSQL.getDate("dataRegistro"));
		frequenciaAulaVO.getHistoricoVO().setAnoHistorico(dadosSQL.getString("anoHistorico"));
		frequenciaAulaVO.getHistoricoVO().setSemestreHistorico(dadosSQL.getString("semestreHistorico"));
		frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().setCodigo(dadosSQL.getInt("matriculaperiodo"));
		frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().setSituacaoMatriculaPeriodo(dadosSQL.getString("situacaoMatriculaPeriodo"));
		frequenciaAulaVO.getHistoricoVO().getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("configuracaoacademico"));
		frequenciaAulaVO.getHistoricoVO().getConfiguracaoAcademico().setQuantidadeCasasDecimaisPermitirAposVirgula(dadosSQL.getInt("quantidadeCasasDecimaisPermitirAposVirgula"));
		if (dadosSQL.getObject("nota1") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota1((Double) dadosSQL.getObject("nota1"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota1(dadosSQL.getDouble("nota1"));
		}
		if (dadosSQL.getObject("nota2") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota2((Double) dadosSQL.getObject("nota2"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota2(dadosSQL.getDouble("nota2"));
		}
		if (dadosSQL.getObject("nota3") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota3((Double) dadosSQL.getObject("nota3"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota3(dadosSQL.getDouble("nota3"));
		}
		if (dadosSQL.getObject("nota4") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota4((Double) dadosSQL.getObject("nota4"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota4(dadosSQL.getDouble("nota4"));
		}
		if (dadosSQL.getObject("nota5") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota5((Double) dadosSQL.getObject("nota5"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota5(dadosSQL.getDouble("nota5"));
		}
		if (dadosSQL.getObject("nota6") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota6((Double) dadosSQL.getObject("nota6"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota6(dadosSQL.getDouble("nota6"));
		}
		if (dadosSQL.getObject("nota7") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota7((Double) dadosSQL.getObject("nota7"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota7(dadosSQL.getDouble("nota7"));
		}
		if (dadosSQL.getObject("nota8") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota8((Double) dadosSQL.getObject("nota8"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota8(dadosSQL.getDouble("nota8"));
		}
		if (dadosSQL.getObject("nota9") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota9((Double) dadosSQL.getObject("nota9"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota9(dadosSQL.getDouble("nota9"));
		}
		if (dadosSQL.getObject("nota10") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota10((Double) dadosSQL.getObject("nota10"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota10(dadosSQL.getDouble("nota10"));
		}
		if (dadosSQL.getObject("nota11") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota11((Double) dadosSQL.getObject("nota11"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota11(dadosSQL.getDouble("nota11"));
		}
		if (dadosSQL.getObject("nota12") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota12((Double) dadosSQL.getObject("nota12"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota12(dadosSQL.getDouble("nota12"));
		}
		if (dadosSQL.getObject("nota13") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota13((Double) dadosSQL.getObject("nota13"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota13(dadosSQL.getDouble("nota13"));
		}
		if (dadosSQL.getObject("nota14") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota14((Double) dadosSQL.getObject("nota14"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota14(dadosSQL.getDouble("nota14"));
		}
		if (dadosSQL.getObject("nota15") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota15((Double) dadosSQL.getObject("nota15"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota15(dadosSQL.getDouble("nota15"));
		}
		if (dadosSQL.getObject("nota16") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota16((Double) dadosSQL.getObject("nota16"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota16(dadosSQL.getDouble("nota16"));
		}
		if (dadosSQL.getObject("nota17") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota17((Double) dadosSQL.getObject("nota17"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota17(dadosSQL.getDouble("nota17"));
		}
		if (dadosSQL.getObject("nota18") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota18((Double) dadosSQL.getObject("nota18"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota18(dadosSQL.getDouble("nota18"));
		}
		if (dadosSQL.getObject("nota19") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota19((Double) dadosSQL.getObject("nota19"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota19(dadosSQL.getDouble("nota19"));
		}
		if (dadosSQL.getObject("nota20") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota20((Double) dadosSQL.getObject("nota20"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota20(dadosSQL.getDouble("nota20"));
		}
		if (dadosSQL.getObject("nota21") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota21((Double) dadosSQL.getObject("nota21"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota21(dadosSQL.getDouble("nota21"));
		}
		if (dadosSQL.getObject("nota22") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota22((Double) dadosSQL.getObject("nota22"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota22(dadosSQL.getDouble("nota22"));
		}
		if (dadosSQL.getObject("nota23") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota23((Double) dadosSQL.getObject("nota23"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota23(dadosSQL.getDouble("nota23"));
		}
		if (dadosSQL.getObject("nota24") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota24((Double) dadosSQL.getObject("nota24"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota24(dadosSQL.getDouble("nota24"));
		}
		if (dadosSQL.getObject("nota25") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota25((Double) dadosSQL.getObject("nota25"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota25(dadosSQL.getDouble("nota25"));
		}
		if (dadosSQL.getObject("nota26") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota26((Double) dadosSQL.getObject("nota26"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota26(dadosSQL.getDouble("nota26"));
		}
		if (dadosSQL.getObject("nota27") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota27((Double) dadosSQL.getObject("nota27"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota27(dadosSQL.getDouble("nota27"));
		}
		if (dadosSQL.getObject("nota28") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota28((Double) dadosSQL.getObject("nota28"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota28(dadosSQL.getDouble("nota28"));
		}
		if (dadosSQL.getObject("nota29") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota29((Double) dadosSQL.getObject("nota29"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota29(dadosSQL.getDouble("nota29"));
		}
		if (dadosSQL.getObject("nota30") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota30((Double) dadosSQL.getObject("nota30"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota30(dadosSQL.getDouble("nota30"));
		}
		
		if (dadosSQL.getObject("nota31") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota31((Double) dadosSQL.getObject("nota31"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota31(dadosSQL.getDouble("nota31"));
		}
		
		if (dadosSQL.getObject("nota32") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota32((Double) dadosSQL.getObject("nota32"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota32(dadosSQL.getDouble("nota32"));
		}
		
		if (dadosSQL.getObject("nota33") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota33((Double) dadosSQL.getObject("nota33"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota33(dadosSQL.getDouble("nota33"));
		}
		
		if (dadosSQL.getObject("nota34") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota34((Double) dadosSQL.getObject("nota34"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota34(dadosSQL.getDouble("nota34"));
		}
		
		if (dadosSQL.getObject("nota35") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota35((Double) dadosSQL.getObject("nota35"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota35(dadosSQL.getDouble("nota35"));
		}
		
		if (dadosSQL.getObject("nota36") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota36((Double) dadosSQL.getObject("nota36"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota36(dadosSQL.getDouble("nota36"));
		}
		
		if (dadosSQL.getObject("nota37") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota37((Double) dadosSQL.getObject("nota37"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota37(dadosSQL.getDouble("nota37"));
		}
		
		if (dadosSQL.getObject("nota38") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota38((Double) dadosSQL.getObject("nota38"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota38(dadosSQL.getDouble("nota38"));
		}
		
		if (dadosSQL.getObject("nota39") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota39((Double) dadosSQL.getObject("nota39"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota39(dadosSQL.getDouble("nota39"));
		}
		
		if (dadosSQL.getObject("nota40") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota40((Double) dadosSQL.getObject("nota40"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota40(dadosSQL.getDouble("nota40"));
		}
		
		frequenciaAulaVO.getHistoricoVO().setNota1Lancada(dadosSQL.getBoolean("nota1Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota2Lancada(dadosSQL.getBoolean("nota2Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota3Lancada(dadosSQL.getBoolean("nota3Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota4Lancada(dadosSQL.getBoolean("nota4Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota5Lancada(dadosSQL.getBoolean("nota5Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota6Lancada(dadosSQL.getBoolean("nota6Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota7Lancada(dadosSQL.getBoolean("nota7Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota8Lancada(dadosSQL.getBoolean("nota8Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota9Lancada(dadosSQL.getBoolean("nota9Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota10Lancada(dadosSQL.getBoolean("nota10Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota11Lancada(dadosSQL.getBoolean("nota11Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota12Lancada(dadosSQL.getBoolean("nota12Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota13Lancada(dadosSQL.getBoolean("nota13Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota14Lancada(dadosSQL.getBoolean("nota14Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota15Lancada(dadosSQL.getBoolean("nota15Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota16Lancada(dadosSQL.getBoolean("nota16Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota17Lancada(dadosSQL.getBoolean("nota17Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota18Lancada(dadosSQL.getBoolean("nota18Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota19Lancada(dadosSQL.getBoolean("nota19Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota20Lancada(dadosSQL.getBoolean("nota20Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota21Lancada(dadosSQL.getBoolean("nota21Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota22Lancada(dadosSQL.getBoolean("nota22Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota23Lancada(dadosSQL.getBoolean("nota23Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota24Lancada(dadosSQL.getBoolean("nota24Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota25Lancada(dadosSQL.getBoolean("nota25Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota26Lancada(dadosSQL.getBoolean("nota26Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota27Lancada(dadosSQL.getBoolean("nota27Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota28Lancada(dadosSQL.getBoolean("nota28Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota29Lancada(dadosSQL.getBoolean("nota29Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota30Lancada(dadosSQL.getBoolean("nota30Lancada"));
		
		frequenciaAulaVO.getHistoricoVO().setNota31Lancada(dadosSQL.getBoolean("nota31Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota32Lancada(dadosSQL.getBoolean("nota32Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota33Lancada(dadosSQL.getBoolean("nota33Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota34Lancada(dadosSQL.getBoolean("nota34Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota35Lancada(dadosSQL.getBoolean("nota35Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota36Lancada(dadosSQL.getBoolean("nota36Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota37Lancada(dadosSQL.getBoolean("nota37Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota38Lancada(dadosSQL.getBoolean("nota38Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota39Lancada(dadosSQL.getBoolean("nota39Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota40Lancada(dadosSQL.getBoolean("nota40Lancada"));
		
		if (dadosSQL.getObject("mediaFinal") == null) {
			frequenciaAulaVO.getHistoricoVO().setMediaFinal((Double) dadosSQL.getObject("mediaFinal"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setMediaFinal(dadosSQL.getDouble("mediaFinal"));
		}
		frequenciaAulaVO.getHistoricoVO().getMediaFinalConceito().setCodigo(dadosSQL.getInt("mediaFinalConceito"));
		frequenciaAulaVO.getHistoricoVO().getNota1Conceito().setCodigo(dadosSQL.getInt("nota1Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota2Conceito().setCodigo(dadosSQL.getInt("nota2Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota3Conceito().setCodigo(dadosSQL.getInt("nota3Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota4Conceito().setCodigo(dadosSQL.getInt("nota4Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota5Conceito().setCodigo(dadosSQL.getInt("nota5Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota6Conceito().setCodigo(dadosSQL.getInt("nota6Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota7Conceito().setCodigo(dadosSQL.getInt("nota7Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota8Conceito().setCodigo(dadosSQL.getInt("nota8Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota9Conceito().setCodigo(dadosSQL.getInt("nota9Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota10Conceito().setCodigo(dadosSQL.getInt("nota10Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota11Conceito().setCodigo(dadosSQL.getInt("nota11Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota12Conceito().setCodigo(dadosSQL.getInt("nota12Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota13Conceito().setCodigo(dadosSQL.getInt("nota13Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota14Conceito().setCodigo(dadosSQL.getInt("nota14Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota15Conceito().setCodigo(dadosSQL.getInt("nota15Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota16Conceito().setCodigo(dadosSQL.getInt("nota16Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota17Conceito().setCodigo(dadosSQL.getInt("nota17Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota18Conceito().setCodigo(dadosSQL.getInt("nota18Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota19Conceito().setCodigo(dadosSQL.getInt("nota19Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota20Conceito().setCodigo(dadosSQL.getInt("nota20Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota21Conceito().setCodigo(dadosSQL.getInt("nota21Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota22Conceito().setCodigo(dadosSQL.getInt("nota22Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota23Conceito().setCodigo(dadosSQL.getInt("nota23Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota24Conceito().setCodigo(dadosSQL.getInt("nota24Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota25Conceito().setCodigo(dadosSQL.getInt("nota25Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota26Conceito().setCodigo(dadosSQL.getInt("nota26Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota27Conceito().setCodigo(dadosSQL.getInt("nota27Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota28Conceito().setCodigo(dadosSQL.getInt("nota28Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota29Conceito().setCodigo(dadosSQL.getInt("nota29Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota30Conceito().setCodigo(dadosSQL.getInt("nota30Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota31Conceito().setCodigo(dadosSQL.getInt("nota31Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota32Conceito().setCodigo(dadosSQL.getInt("nota32Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota33Conceito().setCodigo(dadosSQL.getInt("nota33Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota34Conceito().setCodigo(dadosSQL.getInt("nota34Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota35Conceito().setCodigo(dadosSQL.getInt("nota35Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota36Conceito().setCodigo(dadosSQL.getInt("nota36Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota37Conceito().setCodigo(dadosSQL.getInt("nota37Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota38Conceito().setCodigo(dadosSQL.getInt("nota38Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota39Conceito().setCodigo(dadosSQL.getInt("nota39Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota40Conceito().setCodigo(dadosSQL.getInt("nota40Conceito"));
		frequenciaAulaVO.getHistoricoVO().setHistoricoDisciplinaFazParteComposicao(dadosSQL.getBoolean("historicoDisciplinaFazParteComposicao"));
		frequenciaAulaVO.getHistoricoVO().setTransferenciaEntradaDisciplinasAproveitadas(new Integer(dadosSQL.getInt("transferenciaEntradaDisciplinasAproveitadas")));
//		frequenciaAulaVO.setFaltasGeral(dadosSQL.getInt("faltasgeral"));
//		getFacadeFactory().getHistoricoFacade().carregarDadosNotaConceitoHistorico(frequenciaAulaVO.getHistoricoVO());
		frequenciaAulaVO.setNovoObj(Boolean.FALSE);
		if (dadosSQL.getString("sigla") != null) {
			frequenciaAulaVO.getAbonoFaltaVO().getTipoJustificativaFaltaVO().setSigla(dadosSQL.getString("sigla"));
		}
		obj.getFrequenciaAulaVOs().add(frequenciaAulaVO);
	}

	
	private void montarDadosCompletoReposicaoDiario(RegistroAulaVO obj, SqlRowSet dadosSQL) throws Exception {

		// Dados do Registro Aula
		if (obj.getCodigo() == 0) {
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.setData(dadosSQL.getDate("data"));
			obj.setDiaSemana(dadosSQL.getString("diaSemana"));
			obj.setSemestre(dadosSQL.getString("semestre"));
			obj.setHorario(dadosSQL.getString("horario"));
			obj.setNrAula(dadosSQL.getInt("nrAula"));
			obj.setAno(dadosSQL.getString("ano"));
			obj.setTipoAula(dadosSQL.getString("tipoAula"));
			obj.setCargaHoraria(dadosSQL.getInt("cargaHoraria"));
			obj.setConteudo(dadosSQL.getString("conteudo"));
			obj.setDataRegistroAula(dadosSQL.getDate("dataRegistroAula"));
			obj.setPraticaSupervisionada(dadosSQL.getString("praticaSupervisionada"));
			// Dados da Turma
			obj.getTurma().setCodigo(dadosSQL.getInt("turma.codigo"));
			obj.getTurma().setIdentificadorTurma(dadosSQL.getString("turma.identificadorTurma"));
			obj.getTurma().setTurmaAgrupada(dadosSQL.getBoolean("turma.turmaAgrupada"));
			obj.getTurma().setSemestral(dadosSQL.getBoolean("turma.semestral"));
			// Dados da Unidade
			obj.getTurma().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
			obj.getTurma().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));
			// Dados do Curso
			obj.getTurma().getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
			obj.getTurma().getCurso().setNome(dadosSQL.getString("curso.nome"));
			obj.getTurma().getCurso().getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("curso.configuracaoAcademico"));
			// Dados do Turno
			obj.getTurma().getTurno().setNome(dadosSQL.getString("turno.nome"));
			// Dados do Periodo Letivo
			obj.getTurma().getPeridoLetivo().setCodigo(dadosSQL.getInt("periodoLetivo.codigo"));
			obj.getTurma().getPeridoLetivo().setDescricao(dadosSQL.getString("periodoLetivo.descricao"));
			// Dados da Pessoa
			obj.getProfessor().setCodigo(dadosSQL.getInt("professor.codigo"));
			obj.getProfessor().setNome(dadosSQL.getString("professor.nome"));
			// Dados do Usuario
			obj.getResponsavelRegistroAula().setCodigo(dadosSQL.getInt("responsavelRegistroAula.codigo"));
			obj.getResponsavelRegistroAula().setNome(dadosSQL.getString("responsavelRegistroAula.nome"));
			// Dados da Disciplinas
			obj.getDisciplina().setCodigo(dadosSQL.getInt("disciplina.codigo"));
			obj.getDisciplina().setNome(dadosSQL.getString("disciplina.nome"));
			obj.getGradeDisciplinaVO().setCargaHoraria(dadosSQL.getInt("gradedisciplina.cargaHoraria"));
			obj.getGradeDisciplinaVO().getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("gradedisciplina.configuracaoAcademico"));
			obj.setNovoObj(Boolean.FALSE);
		}
		FrequenciaAulaVO frequenciaAulaVO = new FrequenciaAulaVO();
		frequenciaAulaVO.setTurma(dadosSQL.getString("turma.identificadorTurmaPadrao"));
		frequenciaAulaVO.getMatricula().setMatricula(dadosSQL.getString("matricula.matricula"));
		frequenciaAulaVO.getMatricula().getAluno().setNome(dadosSQL.getString("aluno.nome"));
		frequenciaAulaVO.getMatricula().setUpdated(dadosSQL.getTimestamp("matricula.updated"));
		frequenciaAulaVO.getMatricula().setTipoMatricula(dadosSQL.getString("matricula.tipoMatricula"));
		frequenciaAulaVO.getMatricula().setData(dadosSQL.getTimestamp("matricula.data"));
		frequenciaAulaVO.setPresente(dadosSQL.getBoolean("frequenciaaula.presente"));
		frequenciaAulaVO.setAbonado(dadosSQL.getBoolean("frequenciaaula.abonado"));
		frequenciaAulaVO.setJustificado(dadosSQL.getBoolean("frequenciaaula.justificado"));
		frequenciaAulaVO.setMatriculaPeriodoTurmaDisciplina(dadosSQL.getInt("matriculaPeriodoTurmaDisciplina"));
		frequenciaAulaVO.getHistoricoVO().setCodigo(new Integer(dadosSQL.getInt("historico")));
		frequenciaAulaVO.getHistoricoVO().setInstituicao(dadosSQL.getString("instituicao"));
		frequenciaAulaVO.getHistoricoVO().setTipoHistorico(dadosSQL.getString("tipoHistorico"));
		frequenciaAulaVO.getHistoricoVO().setSituacao(dadosSQL.getString("situacao"));
		frequenciaAulaVO.getHistoricoVO().setFreguencia(new Double(dadosSQL.getDouble("freguencia")));
		frequenciaAulaVO.getHistoricoVO().setDataRegistro(dadosSQL.getDate("dataRegistro"));
		frequenciaAulaVO.getHistoricoVO().setAnoHistorico(dadosSQL.getString("anoHistorico"));
		frequenciaAulaVO.getHistoricoVO().setSemestreHistorico(dadosSQL.getString("semestreHistorico"));
		frequenciaAulaVO.getHistoricoVO().getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("configuracaoacademico"));
		frequenciaAulaVO.getHistoricoVO().getConfiguracaoAcademico().setQuantidadeCasasDecimaisPermitirAposVirgula(dadosSQL.getInt("quantidadeCasasDecimaisPermitirAposVirgula"));
		if (dadosSQL.getObject("nota1") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota1((Double) dadosSQL.getObject("nota1"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota1(dadosSQL.getDouble("nota1"));
		}
		if (dadosSQL.getObject("nota2") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota2((Double) dadosSQL.getObject("nota2"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota2(dadosSQL.getDouble("nota2"));
		}
		if (dadosSQL.getObject("nota3") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota3((Double) dadosSQL.getObject("nota3"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota3(dadosSQL.getDouble("nota3"));
		}
		if (dadosSQL.getObject("nota4") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota4((Double) dadosSQL.getObject("nota4"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota4(dadosSQL.getDouble("nota4"));
		}
		if (dadosSQL.getObject("nota5") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota5((Double) dadosSQL.getObject("nota5"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota5(dadosSQL.getDouble("nota5"));
		}
		if (dadosSQL.getObject("nota6") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota6((Double) dadosSQL.getObject("nota6"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota6(dadosSQL.getDouble("nota6"));
		}
		if (dadosSQL.getObject("nota7") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota7((Double) dadosSQL.getObject("nota7"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota7(dadosSQL.getDouble("nota7"));
		}
		if (dadosSQL.getObject("nota8") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota8((Double) dadosSQL.getObject("nota8"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota8(dadosSQL.getDouble("nota8"));
		}
		if (dadosSQL.getObject("nota9") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota9((Double) dadosSQL.getObject("nota9"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota9(dadosSQL.getDouble("nota9"));
		}
		if (dadosSQL.getObject("nota10") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota10((Double) dadosSQL.getObject("nota10"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota10(dadosSQL.getDouble("nota10"));
		}
		if (dadosSQL.getObject("nota11") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota11((Double) dadosSQL.getObject("nota11"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota11(dadosSQL.getDouble("nota11"));
		}
		if (dadosSQL.getObject("nota12") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota12((Double) dadosSQL.getObject("nota12"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota12(dadosSQL.getDouble("nota12"));
		}
		if (dadosSQL.getObject("nota13") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota13((Double) dadosSQL.getObject("nota13"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota13(dadosSQL.getDouble("nota13"));
		}
		if (dadosSQL.getObject("nota14") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota14((Double) dadosSQL.getObject("nota14"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota14(dadosSQL.getDouble("nota14"));
		}
		if (dadosSQL.getObject("nota15") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota15((Double) dadosSQL.getObject("nota15"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota15(dadosSQL.getDouble("nota15"));
		}
		if (dadosSQL.getObject("nota16") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota16((Double) dadosSQL.getObject("nota16"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota16(dadosSQL.getDouble("nota16"));
		}
		if (dadosSQL.getObject("nota17") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota17((Double) dadosSQL.getObject("nota17"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota17(dadosSQL.getDouble("nota17"));
		}
		if (dadosSQL.getObject("nota18") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota18((Double) dadosSQL.getObject("nota18"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota18(dadosSQL.getDouble("nota18"));
		}
		if (dadosSQL.getObject("nota19") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota19((Double) dadosSQL.getObject("nota19"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota19(dadosSQL.getDouble("nota19"));
		}
		if (dadosSQL.getObject("nota20") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota20((Double) dadosSQL.getObject("nota20"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota20(dadosSQL.getDouble("nota20"));
		}
		if (dadosSQL.getObject("nota21") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota21((Double) dadosSQL.getObject("nota21"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota21(dadosSQL.getDouble("nota21"));
		}
		if (dadosSQL.getObject("nota22") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota22((Double) dadosSQL.getObject("nota22"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota22(dadosSQL.getDouble("nota22"));
		}
		if (dadosSQL.getObject("nota23") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota23((Double) dadosSQL.getObject("nota23"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota23(dadosSQL.getDouble("nota23"));
		}
		if (dadosSQL.getObject("nota24") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota24((Double) dadosSQL.getObject("nota24"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota24(dadosSQL.getDouble("nota24"));
		}
		if (dadosSQL.getObject("nota25") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota25((Double) dadosSQL.getObject("nota25"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota25(dadosSQL.getDouble("nota25"));
		}
		if (dadosSQL.getObject("nota26") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota26((Double) dadosSQL.getObject("nota26"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota26(dadosSQL.getDouble("nota26"));
		}
		if (dadosSQL.getObject("nota27") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota27((Double) dadosSQL.getObject("nota27"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota27(dadosSQL.getDouble("nota27"));
		}
		if (dadosSQL.getObject("nota28") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota28((Double) dadosSQL.getObject("nota28"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota28(dadosSQL.getDouble("nota28"));
		}
		if (dadosSQL.getObject("nota29") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota29((Double) dadosSQL.getObject("nota29"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota29(dadosSQL.getDouble("nota29"));
		}
		if (dadosSQL.getObject("nota30") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota30((Double) dadosSQL.getObject("nota30"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota30(dadosSQL.getDouble("nota30"));
		}
		
		if (dadosSQL.getObject("nota31") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota31((Double) dadosSQL.getObject("nota31"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota31(dadosSQL.getDouble("nota31"));
		}
		if (dadosSQL.getObject("nota32") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota32((Double) dadosSQL.getObject("nota32"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota32(dadosSQL.getDouble("nota32"));
		}
		if (dadosSQL.getObject("nota33") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota33((Double) dadosSQL.getObject("nota33"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota33(dadosSQL.getDouble("nota33"));
		}
		if (dadosSQL.getObject("nota34") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota34((Double) dadosSQL.getObject("nota34"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota34(dadosSQL.getDouble("nota34"));
		}
		if (dadosSQL.getObject("nota35") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota35((Double) dadosSQL.getObject("nota35"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota30(dadosSQL.getDouble("nota35"));
		}
		if (dadosSQL.getObject("nota36") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota36((Double) dadosSQL.getObject("nota36"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota36(dadosSQL.getDouble("nota36"));
		}
		if (dadosSQL.getObject("nota37") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota37((Double) dadosSQL.getObject("nota37"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota37(dadosSQL.getDouble("nota37"));
		}
		if (dadosSQL.getObject("nota38") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota38((Double) dadosSQL.getObject("nota38"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota38(dadosSQL.getDouble("nota38"));
		}
		if (dadosSQL.getObject("nota39") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota39((Double) dadosSQL.getObject("nota39"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota39(dadosSQL.getDouble("nota39"));
		}
		if (dadosSQL.getObject("nota40") == null) {
			frequenciaAulaVO.getHistoricoVO().setNota40((Double) dadosSQL.getObject("nota40"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setNota40(dadosSQL.getDouble("nota40"));
		}
		
		frequenciaAulaVO.getHistoricoVO().setNota1Lancada(dadosSQL.getBoolean("nota1Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota2Lancada(dadosSQL.getBoolean("nota2Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota3Lancada(dadosSQL.getBoolean("nota3Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota4Lancada(dadosSQL.getBoolean("nota4Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota5Lancada(dadosSQL.getBoolean("nota5Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota6Lancada(dadosSQL.getBoolean("nota6Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota7Lancada(dadosSQL.getBoolean("nota7Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota8Lancada(dadosSQL.getBoolean("nota8Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota9Lancada(dadosSQL.getBoolean("nota9Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota10Lancada(dadosSQL.getBoolean("nota10Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota11Lancada(dadosSQL.getBoolean("nota11Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota12Lancada(dadosSQL.getBoolean("nota12Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota13Lancada(dadosSQL.getBoolean("nota13Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota14Lancada(dadosSQL.getBoolean("nota14Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota15Lancada(dadosSQL.getBoolean("nota15Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota16Lancada(dadosSQL.getBoolean("nota16Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota17Lancada(dadosSQL.getBoolean("nota17Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota18Lancada(dadosSQL.getBoolean("nota18Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota19Lancada(dadosSQL.getBoolean("nota19Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota20Lancada(dadosSQL.getBoolean("nota20Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota21Lancada(dadosSQL.getBoolean("nota21Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota22Lancada(dadosSQL.getBoolean("nota22Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota23Lancada(dadosSQL.getBoolean("nota23Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota24Lancada(dadosSQL.getBoolean("nota24Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota25Lancada(dadosSQL.getBoolean("nota25Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota26Lancada(dadosSQL.getBoolean("nota26Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota27Lancada(dadosSQL.getBoolean("nota27Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota28Lancada(dadosSQL.getBoolean("nota28Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota29Lancada(dadosSQL.getBoolean("nota29Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota30Lancada(dadosSQL.getBoolean("nota30Lancada"));
		
		frequenciaAulaVO.getHistoricoVO().setNota31Lancada(dadosSQL.getBoolean("nota31Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota32Lancada(dadosSQL.getBoolean("nota32Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota33Lancada(dadosSQL.getBoolean("nota33Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota34Lancada(dadosSQL.getBoolean("nota34Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota35Lancada(dadosSQL.getBoolean("nota35Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota36Lancada(dadosSQL.getBoolean("nota36Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota37Lancada(dadosSQL.getBoolean("nota37Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota38Lancada(dadosSQL.getBoolean("nota38Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota39Lancada(dadosSQL.getBoolean("nota39Lancada"));
		frequenciaAulaVO.getHistoricoVO().setNota40Lancada(dadosSQL.getBoolean("nota40Lancada"));
		
		if (dadosSQL.getObject("mediaFinal") == null) {
			frequenciaAulaVO.getHistoricoVO().setMediaFinal((Double) dadosSQL.getObject("mediaFinal"));
		} else {
			frequenciaAulaVO.getHistoricoVO().setMediaFinal(dadosSQL.getDouble("mediaFinal"));
		}
		frequenciaAulaVO.getHistoricoVO().getMediaFinalConceito().setCodigo(dadosSQL.getInt("mediaFinalConceito"));
		frequenciaAulaVO.getHistoricoVO().getNota1Conceito().setCodigo(dadosSQL.getInt("nota1Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota2Conceito().setCodigo(dadosSQL.getInt("nota2Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota3Conceito().setCodigo(dadosSQL.getInt("nota3Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota4Conceito().setCodigo(dadosSQL.getInt("nota4Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota5Conceito().setCodigo(dadosSQL.getInt("nota5Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota6Conceito().setCodigo(dadosSQL.getInt("nota6Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota7Conceito().setCodigo(dadosSQL.getInt("nota7Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota8Conceito().setCodigo(dadosSQL.getInt("nota8Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota9Conceito().setCodigo(dadosSQL.getInt("nota9Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota10Conceito().setCodigo(dadosSQL.getInt("nota10Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota11Conceito().setCodigo(dadosSQL.getInt("nota11Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota12Conceito().setCodigo(dadosSQL.getInt("nota12Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota13Conceito().setCodigo(dadosSQL.getInt("nota13Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota14Conceito().setCodigo(dadosSQL.getInt("nota14Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota15Conceito().setCodigo(dadosSQL.getInt("nota15Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota16Conceito().setCodigo(dadosSQL.getInt("nota16Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota17Conceito().setCodigo(dadosSQL.getInt("nota17Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota18Conceito().setCodigo(dadosSQL.getInt("nota18Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota19Conceito().setCodigo(dadosSQL.getInt("nota19Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota20Conceito().setCodigo(dadosSQL.getInt("nota20Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota21Conceito().setCodigo(dadosSQL.getInt("nota21Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota22Conceito().setCodigo(dadosSQL.getInt("nota22Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota23Conceito().setCodigo(dadosSQL.getInt("nota23Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota24Conceito().setCodigo(dadosSQL.getInt("nota24Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota25Conceito().setCodigo(dadosSQL.getInt("nota25Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota26Conceito().setCodigo(dadosSQL.getInt("nota26Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota27Conceito().setCodigo(dadosSQL.getInt("nota27Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota28Conceito().setCodigo(dadosSQL.getInt("nota28Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota29Conceito().setCodigo(dadosSQL.getInt("nota29Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota30Conceito().setCodigo(dadosSQL.getInt("nota30Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota31Conceito().setCodigo(dadosSQL.getInt("nota31Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota32Conceito().setCodigo(dadosSQL.getInt("nota32Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota33Conceito().setCodigo(dadosSQL.getInt("nota33Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota34Conceito().setCodigo(dadosSQL.getInt("nota34Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota35Conceito().setCodigo(dadosSQL.getInt("nota35Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota36Conceito().setCodigo(dadosSQL.getInt("nota36Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota37Conceito().setCodigo(dadosSQL.getInt("nota37Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota38Conceito().setCodigo(dadosSQL.getInt("nota38Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota39Conceito().setCodigo(dadosSQL.getInt("nota39Conceito"));
		frequenciaAulaVO.getHistoricoVO().getNota40Conceito().setCodigo(dadosSQL.getInt("nota40Conceito"));
		frequenciaAulaVO.getHistoricoVO().setHistoricoDisciplinaFazParteComposicao(dadosSQL.getBoolean("historicoDisciplinaFazParteComposicao"));
		frequenciaAulaVO.getHistoricoVO().setTransferenciaEntradaDisciplinasAproveitadas(new Integer(dadosSQL.getInt("transferenciaEntradaDisciplinasAproveitadas")));
		if (dadosSQL.getString("sigla") != null) {
			frequenciaAulaVO.getAbonoFaltaVO().getTipoJustificativaFaltaVO().setSigla(dadosSQL.getString("sigla"));
		}
		//getFacadeFactory().getHistoricoFacade().carregarDadosNotaConceitoHistorico(frequenciaAulaVO.getHistoricoVO());
		frequenciaAulaVO.setNovoObj(Boolean.FALSE);
		obj.getFrequenciaAulaVOs().add(frequenciaAulaVO);
	}

	// public void montarRegistrosAulaComBaseHorarioTurma(HorarioTurmaDiaVO
	// horarioTurmaDia, List<RegistroAulaVO> listaRegistroAulaVO,
	// DisciplinaVO disciplina, PessoaVO professor, Integer cargaHoraria,
	// UsuarioVO responsavelRegistroAula, TurmaVO turma,
	// List<MatriculaPeriodoTurmaDisciplinaVO>
	// listaMatriculaPeriodoTurmaDisciplina, ConfiguracaoFinanceiroVO
	// confFinanVO, UsuarioVO usuarioLogado) throws Exception {
	public List<RegistroAulaVO> montarRegistrosAulaComBaseHorarioTurma(HorarioTurmaDiaVO horarioTurmaDia, DisciplinaVO disciplina, PessoaVO professor, TurnoVO turno, UsuarioVO responsavelRegistroAula, TurmaVO turma, List<HistoricoVO> historicoVOs, String ano, String semestre, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception {
		List<RegistroAulaVO> registroAulaVOs = new ArrayList<RegistroAulaVO>();
		RegistroAulaVO registroAula = null;
		Integer cargaHoraria = null;

		for (HorarioTurmaDiaItemVO aula : horarioTurmaDia.getHorarioTurmaDiaItemVOs()) {
			if (aula.getDisciplinaVO().getCodigo().intValue() == disciplina.getCodigo().intValue() && aula.getFuncionarioVO().getCodigo().intValue() == professor.getCodigo().intValue()) {
				registroAula = new RegistroAulaVO();
				registroAula.setAno(ano);
				registroAula.setSemestre(semestre);
				if (turno.getTipoHorario().equals(TipoHorarioEnum.HORARIO_FLEXIVEL)) {
					registroAula.setCargaHoraria(aula.getDuracaoAula());
				} else {
					registroAula.setCargaHoraria(turno.getDuracaoAula());
				}
				registroAula.setResponsavelRegistroAula(responsavelRegistroAula);
				registroAula.setDisciplina(disciplina);
				registroAula.setTipoAula("P");
				registroAula.setProfessor(professor);
				registroAula.setDataRegistroAula(new Date());
				registroAula.setData(horarioTurmaDia.getData());
				registroAula.setTurma(turma);
				registroAula.setConteudo("Aula Registrada pela tela de Aula/Nota\nConteúdo postado automaticamente.");
				registroAula.setHorario(aula.getNrAula() + "ª Aula (" + aula.getHorario().replace("à", "até") + ")");
				registroAula.setNrAula(aula.getNrAula());
				registroAula.setData(horarioTurmaDia.getData());
				for (HistoricoVO historicoVO : historicoVOs) {
					FrequenciaAulaVO frequenciaAulaVO = new FrequenciaAulaVO();
					frequenciaAulaVO.setMatricula(historicoVO.getMatricula());
					if(historicoVO.getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.ATIVA.getValor()) 
							|| historicoVO.getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.FINALIZADA.getValor())
							|| historicoVO.getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.FORMADO.getValor())
							|| historicoVO.getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA.getValor())){
						frequenciaAulaVO.setPresente(historicoVO.getDataRegistro().before(registroAula.getData()));
						if(historicoVO.getConfiguracaoAcademico().getBloquearRegistroAulaAnteriorDataMatricula() && historicoVO.getDataRegistro().after(registroAula.getData())){						
							frequenciaAulaVO.setFrequenciaOculta(true);
							frequenciaAulaVO.setBloqueadoDevidoDataMatricula(true);
						}
					}else{
						frequenciaAulaVO.setPresente(Boolean.FALSE);
					}
					frequenciaAulaVO.setRegistroAula(registroAula.getCodigo());
					frequenciaAulaVO.setHistoricoVO(historicoVO);
					frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().setCodigo(historicoVO.getMatriculaPeriodoTurmaDisciplina().getCodigo());
					frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().setCodigo(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo());
					frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaPratica().setCodigo(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaPratica().getCodigo());
					frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaTeorica().setCodigo(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaTeorica().getCodigo());
					if (historicoVO.getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals("FI")) {
						frequenciaAulaVO.setEditavel(Boolean.FALSE);
					}
					registroAula.getFrequenciaAulaVOs().add(frequenciaAulaVO);
				}
				Ordenacao.ordenarLista(registroAula.getFrequenciaAulaVOs(), "ordenacaoSemAcentuacaoNome");
				// Abaixo chama-se método responsável por vascular possíveis
				// abonos e justificativas de alunos
				// já registradas e que precisam ser processadas e apresentadas
				// para o professor no momento do lancamento
				getFacadeFactory().getAbonoFaltaFacade().consultarECarregarAbonoFaltaParaFrequenciasDoRegistroAula(registroAula);
				registroAulaVOs.add(registroAula);
			}
		}
		return registroAulaVOs;
	}

	public List<RegistroAulaVO> montarRegistrosAulaComBaseHorarioTurmaDataNaoConstaBase(HorarioTurmaDiaVO horarioTurmaDia, DisciplinaVO disciplina, PessoaVO professor, TurnoVO turno, UsuarioVO responsavelRegistroAula, TurmaVO turma, List<FrequenciaAulaVO> frequenciaAulaVOs, String ano, String semestre, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception {
		List<RegistroAulaVO> registroAulaVOs = new ArrayList<RegistroAulaVO>();
		RegistroAulaVO registroAula = null;
		Integer cargaHoraria = null;

		for (HorarioTurmaDiaItemVO aula : horarioTurmaDia.getHorarioTurmaDiaItemVOs()) {
			if (aula.getDisciplinaVO().getCodigo().intValue() == disciplina.getCodigo().intValue() && aula.getFuncionarioVO().getCodigo().intValue() == professor.getCodigo().intValue()) {
				registroAula = new RegistroAulaVO();
				registroAula.setAno(ano);
				registroAula.setSemestre(semestre);
				if (!turno.getTipoHorario().equals(TipoHorarioEnum.HORARIO_FLEXIVEL)) {
					cargaHoraria = turno.getDuracaoAula();
					registroAula.setCargaHoraria(cargaHoraria);
				} else {					
					registroAula.setCargaHoraria(aula.getDuracaoAula());
				}
				registroAula.setResponsavelRegistroAula(responsavelRegistroAula);
				registroAula.setDisciplina(disciplina);
				registroAula.setTipoAula("P");
				registroAula.setProfessor(professor);
				registroAula.setDataRegistroAula(new Date());
				registroAula.setData(horarioTurmaDia.getData());
				registroAula.setTurma(turma);
				registroAula.setConteudo("Aula Registrada pela tela de Aula/Nota\nConteúdo postado automaticamente.");
				registroAula.setHorario(aula.getNrAula() + "ª Aula (" + aula.getHorario().replace("à", "até") + ")");
				registroAula.setNrAula(aula.getNrAula());
				registroAula.setData(horarioTurmaDia.getData());
				for (FrequenciaAulaVO freq : frequenciaAulaVOs) {
					FrequenciaAulaVO frequenciaAulaVO = new FrequenciaAulaVO();
					frequenciaAulaVO.setMatricula(freq.getMatricula());
					if(freq.getHistoricoVO().getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.ATIVA.getValor()) 
							|| freq.getHistoricoVO().getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.FINALIZADA.getValor())
							|| freq.getHistoricoVO().getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.FORMADO.getValor())
							|| freq.getHistoricoVO().getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA.getValor())){								
						frequenciaAulaVO.setPresente(freq.getHistoricoVO().getDataRegistro().before(registroAula.getData()));
						if(freq.getHistoricoVO().getConfiguracaoAcademico().getBloquearRegistroAulaAnteriorDataMatricula() && freq.getHistoricoVO().getDataRegistro().after(registroAula.getData())){						
							frequenciaAulaVO.setFrequenciaOculta(true);
							frequenciaAulaVO.setBloqueadoDevidoDataMatricula(true);
						}
					}else{
						frequenciaAulaVO.setPresente(Boolean.FALSE);
					}
					
					frequenciaAulaVO.setRegistroAula(registroAula.getCodigo());
					frequenciaAulaVO.setHistoricoVO(freq.getHistoricoVO());
					frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().setCodigo(freq.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().getCodigo());
					frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().setCodigo(freq.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo());
					frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaPratica().setCodigo(freq.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().getTurmaPratica().getCodigo());
					frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaTeorica().setCodigo(freq.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().getTurmaTeorica().getCodigo());
					if (freq.getHistoricoVO().getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals("FI")) {
						frequenciaAulaVO.setEditavel(Boolean.FALSE);
					}
					registroAula.getFrequenciaAulaVOs().add(frequenciaAulaVO);
				}
				Ordenacao.ordenarLista(registroAula.getFrequenciaAulaVOs(), "ordenacaoSemAcentuacaoNome");
				// Abaixo chama-se método responsável por vascular possíveis
				// abonos e justificativas de alunos
				// já registradas e que precisam ser processadas e apresentadas
				// para o professor no momento do lancamento
				getFacadeFactory().getAbonoFaltaFacade().consultarECarregarAbonoFaltaParaFrequenciasDoRegistroAula(registroAula);
				registroAulaVOs.add(registroAula);
			}
		}
		return registroAulaVOs;
	}

	public List<RegistroAulaVO> montarRegistrosAulaComBaseHorarioTurma(List<RegistroAulaVO> registroAulaVOs, DisciplinaVO disciplina, PessoaVO professor, TurnoVO turno, UsuarioVO responsavelRegistroAula, TurmaVO turma, List<HistoricoVO> historicoVOs, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception {
		for (RegistroAulaVO registroAula : registroAulaVOs) {
			for (HistoricoVO historicoVO : historicoVOs) {
				FrequenciaAulaVO frequenciaAulaVO = new FrequenciaAulaVO();
				frequenciaAulaVO.setMatricula(historicoVO.getMatricula());
				if(historicoVO.getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.ATIVA.getValor()) 
						|| historicoVO.getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.FINALIZADA.getValor())
						|| historicoVO.getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.FORMADO.getValor())
						|| historicoVO.getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA.getValor())){					
						frequenciaAulaVO.setPresente(historicoVO.getDataRegistro().before(registroAula.getData()));
						if(historicoVO.getConfiguracaoAcademico().getBloquearRegistroAulaAnteriorDataMatricula() && historicoVO.getDataRegistro().after(registroAula.getData())){
							frequenciaAulaVO.setBloqueadoDevidoDataMatricula(true);
							frequenciaAulaVO.setFrequenciaOculta(true);
						}					
				}else{
					frequenciaAulaVO.setPresente(Boolean.FALSE);
				}
				frequenciaAulaVO.setRegistroAula(registroAula.getCodigo());
				frequenciaAulaVO.setHistoricoVO(historicoVO);
				frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().setCodigo(historicoVO.getMatriculaPeriodoTurmaDisciplina().getCodigo());
				frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().setCodigo(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo());
				frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaPratica().setCodigo(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaPratica().getCodigo());
				frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaTeorica().setCodigo(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaTeorica().getCodigo());
				if (historicoVO.getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals("FI")) {
					frequenciaAulaVO.setEditavel(Boolean.FALSE);
				}
				registroAula.getFrequenciaAulaVOs().add(frequenciaAulaVO);
			}
			Ordenacao.ordenarLista(registroAula.getFrequenciaAulaVOs(), "ordenacaoSemAcentuacaoNome");
			// registroAulaVOs.add(registroAula);
		}
		return registroAulaVOs;
	}

	public Integer carregarDuracaoAula(TurnoVO turno, HorarioTurmaDiaItemVO horarioTurmaDiaItem, Date data) throws Exception {
		DiaSemana diaSemana = Uteis.getDiaSemanaEnum(data);
		if (diaSemana.equals(DiaSemana.DOMINGO)) {
			return verificarCargaHorariaEspecifica(turno.getTurnoHorarioDomingo(), horarioTurmaDiaItem);
		} else if (diaSemana.equals(DiaSemana.SEGUNGA)) {
			return verificarCargaHorariaEspecifica(turno.getTurnoHorarioSegunda(), horarioTurmaDiaItem);
		} else if (diaSemana.equals(DiaSemana.TERCA)) {
			return verificarCargaHorariaEspecifica(turno.getTurnoHorarioTerca(), horarioTurmaDiaItem);
		} else if (diaSemana.equals(DiaSemana.QUARTA)) {
			return verificarCargaHorariaEspecifica(turno.getTurnoHorarioQuarta(), horarioTurmaDiaItem);
		} else if (diaSemana.equals(DiaSemana.QUINTA)) {
			return verificarCargaHorariaEspecifica(turno.getTurnoHorarioQuinta(), horarioTurmaDiaItem);
		} else if (diaSemana.equals(DiaSemana.SEXTA)) {
			return verificarCargaHorariaEspecifica(turno.getTurnoHorarioSexta(), horarioTurmaDiaItem);
		} else if (diaSemana.equals(DiaSemana.SABADO)) {
			return verificarCargaHorariaEspecifica(turno.getTurnoHorarioSabado(), horarioTurmaDiaItem);
		}
		return 0;
	}

	public Integer verificarCargaHorariaEspecifica(List<TurnoHorarioVO> listaTurnoHorarioVO, HorarioTurmaDiaItemVO horarioTurmaDiaItem) throws Exception {
		if (!horarioTurmaDiaItem.getHorarioInicio().trim().isEmpty() && !horarioTurmaDiaItem.getHorarioTermino().trim().isEmpty()) {
			String inicio = horarioTurmaDiaItem.getHorarioInicio();
			String fim = horarioTurmaDiaItem.getHorarioTermino();
			for (TurnoHorarioVO obj : listaTurnoHorarioVO) {
				if (obj.getHorarioInicioAula().equals(inicio) && obj.getHorarioFinalAula().equals(fim) && obj.getNumeroAula().equals(horarioTurmaDiaItem.getNrAula())) {
					return obj.getDuracaoAula();
				}
			}
		}
		return 0;
	}

	// private FrequenciaAulaVO
	// realizarSelecaoFrequenciaAulaAlunoComBaseRegistroAula(RegistroAulaVO
	// registroAulaVO, String matricula) {
	// if (registroAulaVO == null) {
	// return null;
	// }
	// for (FrequenciaAulaVO frequenciaAulaVO :
	// registroAulaVO.getFrequenciaAulaVOs()) {
	// if (frequenciaAulaVO.getMatricula().getMatricula().equals(matricula)) {
	// return frequenciaAulaVO;
	// }
	// }
	// return null;
	// }
	public List<RegistroAulaVO> executarMontagemDadosRegistroAulaENota(Integer unidadeEnsino, TurmaVO turma, DisciplinaVO disciplina, String semestre, String ano, PessoaVO pessoaVO, UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, boolean trazerAlunoPendenteFinanceiramente, ConfiguracaoAcademicoVO configuracaoAcademicoVO, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception {
		return executarMontagemDadosRegistroAulaENota(unidadeEnsino, turma, disciplina, semestre, ano, pessoaVO, false, usuarioVO, configuracaoFinanceiroVO, trazerAlunoPendenteFinanceiramente, configuracaoAcademicoVO, permitirRealizarLancamentoAlunosPreMatriculados);
	}

	public List<RegistroAulaVO> executarMontagemDadosRegistroAulaENota(Integer unidadeEnsino, TurmaVO turma, DisciplinaVO disciplina, String semestre, String ano, PessoaVO pessoaVO, boolean coordenador, UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, boolean trazerAlunoPendenteFinanceiramente, ConfiguracaoAcademicoVO configuracaoAcademicoVO, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception {
		//List<HistoricoVO> historicoVOs = null;
		List<RegistroAulaVO> registroAulaVOs = new ArrayList<RegistroAulaVO>(0);
		//List<HorarioTurmaDiaVO> listaHorarios = null;
		//List<HorarioTurmaDiaVO> listaHorariosNaoConsta = null;
		//HorarioTurmaVO horarioTurmaVO = null;
		HorarioProfessorVO horarioProfessor = null;
		//Map<Integer, ConfiguracaoAcademicoVO> mapConfAcad = new HashMap<Integer, ConfiguracaoAcademicoVO>(0);
		if(turma.getIntegral()){
			ano = "";
			semestre = "";
		}
		if(turma.getAnual()){
			semestre = "";
		}
		try {
			horarioProfessor = getFacadeFactory().getHorarioProfessorFacade().consultarPorProfessorTurnoAnoSemestre(pessoaVO.getCodigo().intValue(), turma.getTurno().getCodigo().intValue(), semestre, ano, turma.getCodigo(), usuarioVO);
			horarioProfessor.setHorarioProfessorDiaVOs(getFacadeFactory().getHorarioProfessorDiaFacade().consultarHorarioProfessorDia(horarioProfessor.getCodigo(), horarioProfessor.getTurno().getCodigo(), horarioProfessor.getProfessor().getCodigo(), disciplina.getCodigo(), turma.getCodigo(), null, null, null, null, null, ano, semestre));			
			for (HorarioProfessorDiaVO horarioProfessorDiaVO : horarioProfessor.getHorarioProfessorDiaVOs()) {
				List<RegistroAulaVO> registroAulaVO2s = (getFacadeFactory().getRegistroAulaFacade().realizarGeracaoRegistroAulaPeloHorarioProfessorDia(horarioProfessorDiaVO, usuarioVO, turma, disciplina, pessoaVO, ano, semestre, false));				
				registroAulaVOs.addAll(registroAulaVO2s);				
			}
			if (registroAulaVOs.isEmpty()) {					
					throw new Exception("Não existem uma programação de aula cadastrada ou a data da aula programada ainda não passou.");
				}
			List<String> matriculaRemover = new ArrayList<String>();
			for(FrequenciaAulaVO frequenciaAulaVO:registroAulaVOs.get(registroAulaVOs.size()-1).getFrequenciaAulaVOs()) {
				HistoricoVO historicoVO = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoTurmaDisciplina(frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplina(), false, false, usuarioVO);
				if(Uteis.isAtributoPreenchido(historicoVO)) {					
					frequenciaAulaVO.setHistoricoVO(historicoVO);
				} else {
					matriculaRemover.add(frequenciaAulaVO.getMatricula().getMatricula());
				}
			}
			for (RegistroAulaVO registroAulaVO : registroAulaVOs) {
				for (Iterator<FrequenciaAulaVO> iterator = registroAulaVO.getFrequenciaAulaVOs().iterator(); iterator.hasNext();) {	
					if(matriculaRemover.contains(iterator.next().getMatricula().getMatricula())) {
						iterator.remove();
						break;
					}
				}
			}
			return registroAulaVOs;
			
//			
//			
//			if (!usuarioVO.getVisaoLogar().equals("professor") && !usuarioVO.getVisaoLogar().equals("coordenador")) {
//				registroAulaVOs = consultaRapidaPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupada(turma.getCodigo(), turma.getTurmaAgrupada(), semestre, ano, pessoaVO.getCodigo(), disciplina.getCodigo(), unidadeEnsino, null, trazerAlunoPendenteFinanceiramente, null, null, "", "", false, NivelMontarDados.TODOS, turma.getCurso().getLiberarRegistroAulaEntrePeriodo(), usuarioVO, true);
//			} else {
//				registroAulaVOs = consultaRapidaPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupada(turma.getCodigo(), turma.getTurmaAgrupada(), semestre, ano, pessoaVO.getCodigo(), disciplina.getCodigo(), unidadeEnsino, null, trazerAlunoPendenteFinanceiramente, usuarioVO.getVisaoLogar().equals("professor"), null, "", "", false, NivelMontarDados.TODOS, turma.getCurso().getLiberarRegistroAulaEntrePeriodo(), usuarioVO, permitirRealizarLancamentoAlunosPreMatriculados);
//			}
//		 	if (usuarioVO.getIsApresentarVisaoAdministrativa()) {
//		 		historicoVOs = getFacadeFactory().getHistoricoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNota(0, 0, disciplina.getCodigo(), turma, ano, semestre, trazerAlunoPendenteFinanceiramente, "'AA', 'CC', 'CH', 'IS', 'TR'", false, false, false, configuracaoAcademicoVO, false, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO, permitirRealizarLancamentoAlunosPreMatriculados);						
//		 	} else if (usuarioVO.getIsApresentarVisaoCoordenador() || !usuarioVO.getIsApresentarVisaoProfessor()) {
//		 		historicoVOs = getFacadeFactory().getHistoricoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNota(0, 0, disciplina.getCodigo(), turma, ano, semestre, trazerAlunoPendenteFinanceiramente, "'AA', 'CC', 'CH', 'IS', 'TR'", false, false, false, configuracaoAcademicoVO, false, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO, permitirRealizarLancamentoAlunosPreMatriculados);
//		 	} else {
//		 		historicoVOs = getFacadeFactory().getHistoricoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessor(0, 0, disciplina.getCodigo(), turma, ano, semestre, "AT", "'AA', 'CC', 'CH', 'IS', 'TR'", false, true, trazerAlunoPendenteFinanceiramente, configuracaoAcademicoVO, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO, permitirRealizarLancamentoAlunosPreMatriculados);
//		 	}				
//			if (registroAulaVOs.isEmpty()) {					
//				if (historicoVOs.isEmpty()) {
//					throw new Exception("Essa disciplina não consta no histórico de nenhum aluno dessa turma.");
//				}
//				for (HistoricoVO historicoVO : historicoVOs) {
//					if (!mapConfAcad.containsKey(historicoVO.getConfiguracaoAcademico().getCodigo())) {
//						mapConfAcad.put(historicoVO.getConfiguracaoAcademico().getCodigo(), getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(historicoVO.getConfiguracaoAcademico().getCodigo(), usuarioVO));
//					}
//					historicoVO.setConfiguracaoAcademico(mapConfAcad.get(historicoVO.getConfiguracaoAcademico().getCodigo()));
//				}
//				if (turma.getTurno().getCodigo() == 0) {
//					turma = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(turma.getCodigo(), NivelMontarDados.BASICO, usuarioVO);
//				}
//
//				horarioTurmaVO = getFacadeFactory().getHorarioTurmaFacade().consultarPorCodigoTurmaUnico(turma.getCodigo(), semestre, ano, false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuarioVO);
//				getFacadeFactory().getHorarioTurmaFacade().carregarDadosHorarioTurma(horarioTurmaVO, pessoaVO.getCodigo(), disciplina.getCodigo(), usuarioVO);
//				listaHorarios = horarioTurmaVO.getHorarioTurmaDiaVOs();
//				if (listaHorarios.isEmpty()) {
//					horarioTurmaVO = getFacadeFactory().getHorarioTurmaFacade().consultarPorCodigoTurmaAgrupadaDisciplinaUnico(turma.getCodigo(), disciplina.getCodigo(), semestre, ano, false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuarioVO);
//					getFacadeFactory().getHorarioTurmaFacade().carregarDadosHorarioTurma(horarioTurmaVO, pessoaVO.getCodigo(), disciplina.getCodigo(), usuarioVO);
//					listaHorarios = horarioTurmaVO.getHorarioTurmaDiaVOs();
//				}
//				for (HorarioTurmaDiaVO horarioTurmaDiaVO : listaHorarios) {
//					// getFacadeFactory().getHorarioTurmaDiaFacade().montarDadosHorarioTurmaDiaItemVOs(horarioTurmaVO,
//					// horarioTurmaDiaVO);
//					registroAulaVOs.addAll(montarRegistrosAulaComBaseHorarioTurma(horarioTurmaDiaVO, disciplina, pessoaVO, horarioTurmaVO.getTurno(), usuarioVO, turma, historicoVOs, horarioTurmaVO.getAnoVigente(), horarioTurmaVO.getSemestreVigente(), configuracaoFinanceiroVO, usuarioVO));
//					Uteis.removerObjetoMemoria(horarioTurmaDiaVO);
//				}
//				if (listaHorarios.isEmpty()) {
//					throw new Exception("Não existem uma programação de aula cadastrada ou a data da aula programada ainda não passou.");
//				}
//			} else {
//				RegistroAulaVO regAux = registroAulaVOs.get(registroAulaVOs.size() - 1);
//				if (historicoVOs.isEmpty()) {
//					throw new Exception("Essa disciplina não consta no histórico de nenhum aluno dessa turma.");
//				}
//				for (HistoricoVO historicoVO : historicoVOs) {
//					if (!mapConfAcad.containsKey(historicoVO.getConfiguracaoAcademico().getCodigo())) {
//						mapConfAcad.put(historicoVO.getConfiguracaoAcademico().getCodigo(), getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(historicoVO.getConfiguracaoAcademico().getCodigo(), usuarioVO));
//					}
//					historicoVO.setConfiguracaoAcademico(mapConfAcad.get(historicoVO.getConfiguracaoAcademico().getCodigo()));
//				}
//				for (FrequenciaAulaVO freqAulaVO : regAux.getFrequenciaAulaVOs()) {					
//					for (HistoricoVO historicoVO : historicoVOs) {
//						if (freqAulaVO.getMatricula().getMatricula().equals(historicoVO.getMatricula().getMatricula())) {
//							freqAulaVO.setHistoricoVO(historicoVO);
//							freqAulaVO.setMatriculaPeriodoTurmaDisciplina(historicoVO.getMatriculaPeriodoTurmaDisciplina().getCodigo());
//							freqAulaVO.setMatriculaPeriodoTurmaDisciplinaVO(historicoVO.getMatriculaPeriodoTurmaDisciplina());
//						}
//					}
//				}
//				for (HistoricoVO historicoVO : historicoVOs) {					
//					for (RegistroAulaVO registroAulaVO : registroAulaVOs) {
//						boolean achou = false;
//						for (FrequenciaAulaVO freqAulaVO : registroAulaVO.getFrequenciaAulaVOs()) {
//							if (freqAulaVO.getMatricula().getMatricula().equals(historicoVO.getMatricula().getMatricula())) {
//								freqAulaVO.setHistoricoVO(historicoVO);
//								freqAulaVO.setMatriculaPeriodoTurmaDisciplina(historicoVO.getMatriculaPeriodoTurmaDisciplina().getCodigo());
//								freqAulaVO.setMatriculaPeriodoTurmaDisciplinaVO(historicoVO.getMatriculaPeriodoTurmaDisciplina());
//								achou = true;
//								break;
//							}
//						}
//
//						if (!achou) {
//							FrequenciaAulaVO frequenciaAulaVO = new FrequenciaAulaVO();
//							frequenciaAulaVO.setMatricula(historicoVO.getMatricula());							
//							if(historicoVO.getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.ATIVA.getValor()) 
//									|| historicoVO.getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.FINALIZADA.getValor())
//									|| historicoVO.getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.FORMADO.getValor())
//									|| historicoVO.getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA.getValor())){								
//								frequenciaAulaVO.setPresente(historicoVO.getDataRegistro().before(registroAulaVO.getData()));
//								if(historicoVO.getConfiguracaoAcademico().getBloquearRegistroAulaAnteriorDataMatricula() && historicoVO.getDataRegistro().after(registroAulaVO.getData())){
//									frequenciaAulaVO.setBloqueadoDevidoDataMatricula(true);
//									frequenciaAulaVO.setFrequenciaOculta(true);
//								}
//							}else{
//								frequenciaAulaVO.setPresente(false);
//							}
//							frequenciaAulaVO.setRegistroAula(registroAulaVO.getCodigo());
//							frequenciaAulaVO.setHistoricoVO(historicoVO);
//							frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().setCodigo(historicoVO.getMatriculaPeriodoTurmaDisciplina().getCodigo());
//							frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().setCodigo(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo());
//							frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaPratica().setCodigo(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaPratica().getCodigo());
//							frequenciaAulaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaTeorica().setCodigo(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaTeorica().getCodigo());							
//							if (historicoVO.getMatriculaPeriodo().getSituacaoMatriculaPeriodo().equals("FI")) {
//								frequenciaAulaVO.setEditavel(Boolean.FALSE);
//							}
//							
//							registroAulaVO.getFrequenciaAulaVOs().add(frequenciaAulaVO);
//							
//						}
//						
//					}
//				}	
//				
//				for (RegistroAulaVO registroAulaVO : registroAulaVOs) {
//					getFacadeFactory().getAbonoFaltaFacade().consultarECarregarAbonoFaltaParaFrequenciasDoRegistroAula(registroAulaVO);
//				}
//
//				listaHorariosNaoConsta = getFacadeFactory().getHorarioTurmaDiaFacade().consultarPorTurmaDisciplinaPeriodoProfessorNaoConstaRegistroAula(turma.getCodigo(), disciplina.getCodigo(), pessoaVO.getCodigo(), ano, semestre, null, null, true, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
//				if (!listaHorariosNaoConsta.isEmpty()) {
//					horarioTurmaVO = getFacadeFactory().getHorarioTurmaFacade().consultarPorCodigoTurmaUnico(turma.getCodigo(), semestre, ano, false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuarioVO);
//					for (HorarioTurmaDiaVO horarioTurmaDiaVO : listaHorariosNaoConsta) {
//						getFacadeFactory().getHorarioTurmaDiaFacade().montarDadosHorarioTurmaDiaItemVOs(horarioTurmaVO, horarioTurmaDiaVO);
//						/**
//						 * Regra responsável por executar validação de que
//						 * nenhum dia e nrAula se duplique no ato de adicionar
//						 * horarios que não constam incluídos.
//						 */
//						for (Iterator<HorarioTurmaDiaItemVO> iterator = horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs().iterator(); iterator.hasNext();) {
//							HorarioTurmaDiaItemVO aula = (HorarioTurmaDiaItemVO) iterator.next();
//							if (aula.getDisciplinaVO().getCodigo().equals(disciplina.getCodigo()) && aula.getFuncionarioVO().getCodigo().equals(pessoaVO.getCodigo())) {
//								for (RegistroAulaVO obj : registroAulaVOs) {
//									if (obj.getData_Apresentar().equals(horarioTurmaDiaVO.getData_Apresentar()) && obj.getNrAula().equals(aula.getNrAula())) {
//										iterator.remove();
//										break;
//									}
//								}
//							}
//						}
//						registroAulaVOs.addAll(montarRegistrosAulaComBaseHorarioTurmaDataNaoConstaBase(horarioTurmaDiaVO, disciplina, pessoaVO, horarioTurmaVO.getTurno(), usuarioVO, turma, regAux.getFrequenciaAulaVOs(), horarioTurmaVO.getAnoVigente(), horarioTurmaVO.getSemestreVigente(), configuracaoFinanceiroVO, usuarioVO));
//						Uteis.removerObjetoMemoria(horarioTurmaDiaVO);
//					}
//					listaHorariosNaoConsta.clear();
//				}
//			}
//			Ordenacao.ordenarLista(registroAulaVOs, "ordenacao");
//			for(RegistroAulaVO registroAulaVO: registroAulaVOs) {
//				Ordenacao.ordenarLista(registroAulaVO.getFrequenciaAulaVOs(), "ordenacaoSemAcentuacaoNome");
//			}
			//return registroAulaVOs;
		} catch (Exception e) {
			throw e;
		} finally {
			horarioProfessor =  null;
//			Uteis.liberarListaMemoria(listaHorarios);
//			Uteis.removerObjetoMemoria(horarioTurmaVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirComBaseNaProgramacaoAula(Integer turma, Integer disciplina, String semestre, String ano, Date dataAula, String nrAula, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" DELETE FROM registroaula WHERE turma = ").append(turma).append(" AND disciplina = ").append(disciplina);
		sqlStr.append(" AND semestre = '").append(semestre).append("' AND ano = '").append(ano).append("' ");
		sqlStr.append(" AND data = '").append(Uteis.getDataJDBC(dataAula)).append("' AND horario LIKE('").append(nrAula).append("%')").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
		try {
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} catch (Exception e) {
			throw e;
		} finally {
			sqlStr = null;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirComBaseNaProgramacaoAulaPorDia(Integer turma, String semestre, String ano, Date dataAula, Integer professor, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" DELETE FROM registroaula WHERE turma = ").append(turma);
		sqlStr.append(" AND semestre = '").append(semestre).append("' AND ano = '").append(ano).append("' ");
		sqlStr.append(" AND data = '").append(Uteis.getDataJDBC(dataAula)).append("' AND professor = ").append(professor).append(" ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
		try {
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} catch (Exception e) {
			throw e;
		} finally {
			sqlStr = null;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirComBaseNaProgramacaoAulaPorAnoSemestreProfessor(Integer turma, String semestre, String ano, Integer professor, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" DELETE FROM registroaula WHERE turma = ").append(turma);
		sqlStr.append(" AND semestre = '").append(semestre).append("' AND ano = '").append(ano).append("' ");
		sqlStr.append(" AND professor = ").append(professor).append(" ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
		try {
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} catch (Exception e) {
			throw e;
		} finally {
			sqlStr = null;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirComBaseNaProgramacaoAula(Integer turma, String semestre, String ano, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" DELETE FROM registroaula WHERE turma = ").append(turma).append(" ");
		sqlStr.append(" AND semestre = '").append(semestre).append("' AND ano = '").append(ano).append("' ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
		try {
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} catch (Exception e) {
			throw e;
		} finally {
			sqlStr = null;
		}
	}

	public static RegistroAulaVO montarDadosRegistroAula(SqlRowSet dadosSQL) throws Exception {
		RegistroAulaVO obj = new RegistroAulaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getDate("data"));
		obj.getTurma().setCodigo(new Integer(dadosSQL.getInt("turma")));
		obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
		obj.setCargaHoraria(new Integer(dadosSQL.getInt("cargaHoraria")));
		obj.setConteudo(dadosSQL.getString("conteudo"));
		obj.getResponsavelRegistroAula().setCodigo(new Integer(dadosSQL.getInt("responsavelRegistroAula")));
		obj.setDataRegistroAula(dadosSQL.getDate("dataRegistroAula"));
		obj.getProfessor().setCodigo(new Integer(dadosSQL.getInt("professor")));
		obj.setDiaSemana(dadosSQL.getString("diaSemana"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setHorario(dadosSQL.getString("horario"));
		obj.setTipoAula(dadosSQL.getString("tipoAula"));
		obj.setNrAula(dadosSQL.getInt("nrAula"));
		obj.setPraticaSupervisionada(dadosSQL.getString("praticaSupervisionada"));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public void validarDadosRegistroAulaNotaTurma(Integer turma, Integer professor, Integer disciplina, ConfiguracaoAcademicoVO configuracaoAcademicoVO, String periodicidade, String ano, String semestre) throws Exception {
		if ((turma == null) || (turma == 0)) {
			throw new ConsistirException("O campo TURMA (Registrar Aula Nota) deve ser informado.");
		}
		if(!periodicidade.equals(PeriodicidadeEnum.INTEGRAL.getValor()) && ano.trim().isEmpty()){
			throw new ConsistirException("O campo ANO (Registrar Aula Nota) deve ser informado.");
		}
		if(!periodicidade.equals(PeriodicidadeEnum.INTEGRAL.getValor()) && ano.trim().length() != 4){
			throw new ConsistirException("O campo ANO (Registrar Aula Nota) deve possuir 4 dígitos.");
		}
		if(periodicidade.equals(PeriodicidadeEnum.SEMESTRAL.getValor()) && semestre.trim().isEmpty()){
			throw new ConsistirException("O campo SEMESTRE (Registrar Aula Nota) deve possuir 4 dígitos.");
		}
		if ((professor == null) || (professor == 0)) {
			throw new ConsistirException("O campo PROFESSOR (Registrar Aula Nota) deve ser informado.");
		}
		if ((disciplina == null) || (disciplina == 0)) {
			throw new ConsistirException("O campo DISCIPLINA (Registrar Aula Nota) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(configuracaoAcademicoVO)) {
			throw new ConsistirException("Não foi encontrado nenhuma CONFIGURAÇÃO ACADÊMICA (Registrar Aula Nota) para a disciplina selecionadam ou a mesma não possui nenhum aluno matriculado.");
		}
	}

	@Override
	public Map<String, List<AlunoComFrequenciaBaixaRelVO>> consultarMatriculaComFrequenciaBaixaEmailAutomatico() {
		Map<String, List<AlunoComFrequenciaBaixaRelVO>> mapAlunoComFrequenciaBaixaRelVOs = new HashMap<String, List<AlunoComFrequenciaBaixaRelVO>>(0);
		StringBuilder sql = new StringBuilder("");
		sql.append(" select matricula.matricula as \"matricula.matricula\", pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\", pessoa.email as \"pessoa.email\", ");
		sql.append(" unidadeensino.nome as \"unidadeensino.nome\", curso.nome as \"curso.nome\", disciplina.nome as \"disciplina.nome\", matriculaperiodoturmadisciplina.codigo as \"matriculaperiodoturmadisciplina.codigo\" ");
		sql.append(" from matricula");
		sql.append(" inner join unidadeensino on   unidadeensino.codigo = matricula.unidadeensino");
		sql.append(" inner join curso on   curso.codigo = matricula.curso");
		sql.append(" inner join configuracoes on   configuracoes.codigo = unidadeensino.configuracoes");
		sql.append(" inner join pessoa on   pessoa.codigo = matricula.aluno");
		sql.append(" inner join matriculaperiodo on   matriculaperiodo.matricula = matricula.matricula");
		sql.append(" inner join matriculaperiodoturmadisciplina on   matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo");
		sql.append(" inner join historico on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo");
		sql.append(" inner join disciplina on   matriculaperiodoturmadisciplina.disciplina = disciplina.codigo");
		sql.append(" where matriculaperiodo.situacaoMatriculaPeriodo = 'AT'");
		sql.append(" and matricula.situacao = 'AT'");
		sql.append(" and historico.situacao = 'CS'");
		sql.append(" and curso.nivelEducacional not in ('PO', 'EX')");
		sql.append(" and enviarMensagemNotificacaoFrequenciaAula = true");
		sql.append(" and (matriculaperiodoturmadisciplina.dataNotificacaoFrequenciaBaixa is null");
		sql.append(" or (periodicidadenotificacaofrequenciaaula > 0 and matriculaperiodoturmadisciplina.dataNotificacaoFrequenciaBaixa+periodicidadenotificacaofrequenciaaula <= current_date ))");
		sql.append(" and ( select sum(numerofaltas)*100/ count(numeroAulas)  from (");
		sql.append(" select frequenciaaula.matricula,");
		sql.append(" case when frequenciaaula.presente is not true then 1 else 0 end as numerofaltas,");
		sql.append(" registroaula.codigo  as numeroAulas");
		sql.append(" from frequenciaaula");
		sql.append(" inner join registroaula on registroaula.codigo = frequenciaaula.registroaula");
		sql.append(" where matricula.matricula = frequenciaaula.matricula and registroaula.disciplina = disciplina.codigo) as t having count(numeroAulas) >= numeroMinimoAulaConsiderarParaNotificacaoFrequenciaAula  ) >= porcentagemConsiderarFrequenciaAulaBaixa ");
		sql.append(" order by matricula.matricula, disciplina.nome limit 25 ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		AlunoComFrequenciaBaixaRelVO alunoComFrequenciaBaixaRelVO = null;
		while (rs.next()) {
			alunoComFrequenciaBaixaRelVO = new AlunoComFrequenciaBaixaRelVO();
			alunoComFrequenciaBaixaRelVO.setMatricula(rs.getString("matricula.matricula"));
			alunoComFrequenciaBaixaRelVO.setCurso(rs.getString("curso.nome"));
			alunoComFrequenciaBaixaRelVO.setDisciplina(rs.getString("disciplina.nome"));
			alunoComFrequenciaBaixaRelVO.setUnidadeEnsino(rs.getString("unidadeEnsino.nome"));
			alunoComFrequenciaBaixaRelVO.setMatriculaPeriodoDisciplina(rs.getInt("matriculaperiodoturmadisciplina.codigo"));
			alunoComFrequenciaBaixaRelVO.getAluno().setCodigo(rs.getInt("pessoa.codigo"));
			alunoComFrequenciaBaixaRelVO.getAluno().setNome(rs.getString("pessoa.nome"));
			alunoComFrequenciaBaixaRelVO.getAluno().setEmail(rs.getString("pessoa.email"));
			if (mapAlunoComFrequenciaBaixaRelVOs.containsKey(rs.getString("matricula.matricula"))) {
				mapAlunoComFrequenciaBaixaRelVOs.get(rs.getString("matricula.matricula")).add(alunoComFrequenciaBaixaRelVO);
			} else {
				List<AlunoComFrequenciaBaixaRelVO> alunoComFrequenciaBaixaRelVOs = new ArrayList<AlunoComFrequenciaBaixaRelVO>(0);
				alunoComFrequenciaBaixaRelVOs.add(alunoComFrequenciaBaixaRelVO);
				mapAlunoComFrequenciaBaixaRelVOs.put(rs.getString("matricula.matricula"), alunoComFrequenciaBaixaRelVOs);
			}
		}

		return mapAlunoComFrequenciaBaixaRelVOs;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.DEFAULT, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<NotificacaoRegistroAulaNaoLancadaVO> consultarDadosNotificacaoNaoLancamentoRegistroAula() {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select turma.codigo, turma.identificadorturma as turma, horarioturmadia.data as dataaula, disciplina.nome as disciplina, pessoa.codigo as professor_codigo, pessoa.nome as professor_nome, pessoa.email as professor_email, 	");
		sql.append(" 		 case when (horarioturmadia.data =  (current_date - primeiraNotificacaoNaoLancamentoRegistroAula)) then 'MENSAGEM_PRIMEIRA_NOTIFICACAO_NAO_LANCAMENTO_AULA'  ");
		sql.append(" 		      when (horarioturmadia.data =  (current_date - segundaNotificacaoNaoLancamentoRegistroAula)) then 'MENSAGEM_SEGUNDA_NOTIFICACAO_NAO_LANCAMENTO_AULA'     ");
		sql.append(" 		      when (horarioturmadia.data =  (current_date - terceiraNotificacaoNaoLancamentoRegistroAula)) then 'MENSAGEM_TERCEIRA_NOTIFICACAO_NAO_LANCAMENTO_AULA'  ");
		sql.append(" 		      else 'MENSAGEM_QUARTA_NOTIFICACAO_NAO_LANCAMENTO_AULA' end as tipoNotificacao   ");
		sql.append(" 		 from horarioturma   ");
		sql.append(" 		 inner join horarioturmadia on horarioturma.codigo = horarioturmadia.horarioturma");
		sql.append(" 	 	 inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sql.append(" 	  	 inner join disciplina on horarioturmadiaitem.disciplina = disciplina.codigo");
		sql.append("  	     inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor");
		sql.append(" 		 inner join turma on turma.codigo = horarioturma.turma ");
		sql.append(" 		 inner join unidadeensino on turma.unidadeensino = unidadeensino.codigo ");
		sql.append(" 		 inner join configuracoes on configuracoes.codigo = unidadeensino.configuracoes ");
		sql.append(" 		 where ( ");
		sql.append(" 	 	 (primeiraNotificacaoNaoLancamentoRegistroAula  > 0  and horarioturmadia.data =   (current_date - primeiraNotificacaoNaoLancamentoRegistroAula) ) or ");
		sql.append(" 		 (segundaNotificacaoNaoLancamentoRegistroAula  > 0 and horarioturmadia.data =  (current_date - segundaNotificacaoNaoLancamentoRegistroAula) ) or");
		sql.append(" 		 (terceiraNotificacaoNaoLancamentoRegistroAula  > 0 and horarioturmadia.data =  (current_date - terceiraNotificacaoNaoLancamentoRegistroAula) )");
		sql.append(" 	     ) 		     ");
		sql.append(" 		 and not exists (select codigo from registroaula  where registroaula.turma = turma.codigo ");
		sql.append(" 		 and disciplina.codigo = registroaula.disciplina and registroaula.professor = pessoa.codigo ");
		sql.append(" 		 and registroaula.data::DATE = horarioturmadia.data::DATE) ");
		sql.append(" 		 group by turma.codigo, turma.identificadorturma, horarioturmadia.data, disciplina.nome, pessoa.codigo, pessoa.nome, pessoa.email, primeiraNotificacaoNaoLancamentoRegistroAula, segundaNotificacaoNaoLancamentoRegistroAula, terceiraNotificacaoNaoLancamentoRegistroAula ");		
		List<NotificacaoRegistroAulaNaoLancadaVO> notificacaoRegistroAulaNaoLancadaVOs = new ArrayList<NotificacaoRegistroAulaNaoLancadaVO>(0);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (rs.next()) {
			NotificacaoRegistroAulaNaoLancadaVO obj = new NotificacaoRegistroAulaNaoLancadaVO();
			obj.setTurma(rs.getString("turma"));
			obj.setTipoNotificacao(rs.getString("tipoNotificacao"));
			obj.setDisciplina(rs.getString("disciplina"));
			obj.setDataAula(rs.getDate("dataAula"));
			obj.getProfessor().setCodigo(rs.getInt("professor_codigo"));
			obj.getProfessor().setNome(rs.getString("professor_nome"));
			obj.getProfessor().setEmail(rs.getString("professor_email"));
			notificacaoRegistroAulaNaoLancadaVOs.add(obj);
		}
		return notificacaoRegistroAulaNaoLancadaVOs;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarCriacaoRegistroAulaAPartirTransferenciaEntrada(String matricula, List<TransferenciaEntradaRegistroAulaFrequenciaVO> transferenciaEntradaRegistroAulaFrequenciaVOs, UsuarioVO usuarioVO) throws Exception {
		for (TransferenciaEntradaRegistroAulaFrequenciaVO transferenciaRegistroAulaVO : transferenciaEntradaRegistroAulaFrequenciaVOs) {
			FrequenciaAulaVO frequenciaAulaVO = new FrequenciaAulaVO();
			frequenciaAulaVO.setAbonado(transferenciaRegistroAulaVO.getAbonado());
			frequenciaAulaVO.setPresente(transferenciaRegistroAulaVO.getPresente());
			frequenciaAulaVO.getMatricula().setMatricula(matricula);
			frequenciaAulaVO.setRegistroAula(transferenciaRegistroAulaVO.getRegistroAulaVO().getCodigo());
			getFacadeFactory().getFrequenciaAulaFacade().incluir(frequenciaAulaVO, usuarioVO);
		}
	}

	public Integer consultarCargaHorariaTotal(RegistroAulaVO registroAula, TurmaVO turmaVO, String semestre, String ano) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct sum(cargahoraria) as cargahorariatotal from registroaula, turma  ");
		sql.append(" where turma = turma.codigo and turma.codigo = ").append(turmaVO.getCodigo());

		if (registroAula.getDisciplina() != null && !registroAula.getDisciplina().getCodigo().equals(0)) {
			sql.append(" and disciplina = ").append(registroAula.getDisciplina().getCodigo());
		}
//		if (registroAula.getProfessor() != null && !registroAula.getProfessor().getCodigo().equals(0)) {
//			sql.append(" and professor = ").append(registroAula.getProfessor().getCodigo());
//		}
		if (semestre != null && !semestre.equals("")) {
			sql.append(" and semestre = '").append(semestre).append("' ");
		}
		if (ano != null && !ano.equals("")) {
			sql.append(" and ano = '").append(ano).append("' ");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			return 0;
		}
		return (new Integer(tabelaResultado.getInt("cargahorariatotal")));
	}

	@Override
	public Integer consultaRapidaFaltasAlunoBoletimAcademico(String matricula, Integer disciplina, String semestre, String ano, Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select count(frequenciaaula.registroaula) as qtdeFaltas ");
		sqlStr.append("from frequenciaaula ");
		sqlStr.append("inner join registroaula on registroaula.codigo = frequenciaaula.registroaula ");
		sqlStr.append("inner join turma on registroaula.turma = turma.codigo ");
		sqlStr.append("where frequenciaaula.presente = false");
		sqlStr.append(" and frequenciaaula.abonado = false");
		sqlStr.append(" and frequenciaaula.matricula = '").append(matricula).append("'");
		sqlStr.append(" and registroaula.turma = ").append(turma);
		sqlStr.append(" and (RegistroAula.disciplina = ").append(disciplina);
		sqlStr.append(" or (turma.turmaagrupada and RegistroAula.disciplina in (select distinct disciplinaequivalenteTurmaagrupada from turmadisciplina where turma = ").append(turma).append(" and turmadisciplina.disciplina = ").append(disciplina.intValue()).append(")) ");
		sqlStr.append(" or (turma.turmaagrupada and RegistroAula.disciplina in (select distinct equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina.intValue()).append(")) ");
		sqlStr.append(" or (turma.turmaagrupada and RegistroAula.disciplina in (select distinct disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina.intValue()).append(")) ");
		sqlStr.append(" ) ");
		if (!semestre.equals("")) {
			sqlStr.append(" and RegistroAula.semestre = '").append(semestre).append("'");
		}
		if (!ano.equals("")) {
			sqlStr.append(" and RegistroAula.ano = '").append(ano).append("'");
		}
		return getConexao().getJdbcTemplate().queryForObject(sqlStr.toString(), Integer.class);
	}

	@Override
	public List<RegistroAulaVO> consultarRegistroAulaPorTurmaDisciplinaPeriodo(Integer turma, Integer disciplina, Date dataInicio, Date dataTermino, String ano, String semestre,  int nivelMontarDados, UsuarioVO usuario, Boolean atividadeComplementar) throws Exception {
		StringBuilder sql = new StringBuilder("select distinct registroaula.codigo, registroaula.data, registroaula.horario, registroaula.nraula, registroaula.atividadecomplementar,  disciplina.nome as \"disciplina.nome\", ");
		sql.append("disciplina.codigo as \"disciplina.codigo\" , pessoa.nome as \"pessoa.nome\", pessoa.codigo as \"pessoa.codigo\", turma.codigo as turma, turma.identificadorturma as identificadorturma ");
		sql.append("from registroaula ");
		sql.append("inner join disciplina on disciplina.codigo =  registroaula.disciplina ");
		sql.append("inner join turma on turma.codigo = registroaula.turma ");
		sql.append(" inner join pessoa on pessoa.codigo =  registroaula.professor ");
		sql.append(" where registroaula.turma = ").append(turma);
		if (disciplina != null && disciplina > 0) {
			sql.append(" and disciplina.codigo = ").append(disciplina);
		}
		sql.append(" and registroaula.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		sql.append(" and registroaula.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		if(ano != null && !ano.trim().isEmpty()){
			sql.append(" and registroaula.ano = '").append(ano).append("' ");	
		}
		if(ano != null && !ano.trim().isEmpty()){
			sql.append(" and registroaula.semestre = '").append(semestre).append("' ");	
		}
		if (atividadeComplementar) {
			sql.append(" and registroaula.atividadecomplementar = true");
		}
		sql.append(" order by registroaula.data ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<RegistroAulaVO> registroAulaVOs = new ArrayList<RegistroAulaVO>(0);
		RegistroAulaVO registroAulaVO = null;
		while (rs.next()) {
			registroAulaVO = new RegistroAulaVO();
			registroAulaVO.setCodigo(rs.getInt("codigo"));
			registroAulaVO.setData(rs.getDate("data"));
			registroAulaVO.setNovoObj(false);
			registroAulaVO.setHorario(rs.getString("horario"));
			registroAulaVO.setNrAula(rs.getInt("nraula"));
			registroAulaVO.setAtividadeComplementar(rs.getBoolean("atividadecomplementar"));
			registroAulaVO.getDisciplina().setCodigo(rs.getInt("disciplina.codigo"));
			registroAulaVO.getDisciplina().setNome(rs.getString("disciplina.nome"));
			registroAulaVO.getProfessor().setNome(rs.getString("pessoa.nome"));
			registroAulaVO.getProfessor().setCodigo(rs.getInt("pessoa.codigo"));
			registroAulaVO.getTurma().setCodigo(rs.getInt("turma"));
			registroAulaVO.getTurma().setIdentificadorTurma(rs.getString("identificadorTurma"));
			registroAulaVOs.add(registroAulaVO);
		}
		return registroAulaVOs;
	}

	public String consultarRegistroAulaPorDisciplinaMatricula(String matricula, Integer disciplina, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select registroaula.codigo from registroaula ");
		sb.append(" inner join frequenciaaula on frequenciaaula.registroaula = registroaula.codigo ");
		sb.append(" where disciplina = ").append(disciplina);
		sb.append(" and frequenciaaula.matricula = '").append(matricula).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return UteisJSF.internacionalizar("msg_InclusaoExclusaoDisciplina_registroAulaLancado");
		}
		return "";
	}

	/**
	 * Método responsável por retornar um MAP com o total de faltas de cada
	 * BIMESTRE que o aluno cursou. Regra válida apenas para cursos de nível
	 * educacional do tipo Infantil (IN), Básico (BA) e Médio (ME).
	 */
	@Override
	public Map<BimestreEnum, Integer> consultarQuantidadeFaltasAlunoBimestre(String matricula, Integer disciplina, String semestre, String ano, Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT DISTINCT ");
		sqlStr.append("(select count(fa.registroaula) from frequenciaaula fa inner join registroaula ra on ra.codigo = fa.registroaula ");
		sqlStr.append("where data >= datainicioperiodoletivoprimeirobimestre and data <= datafimperiodoletivoprimeirobimestre and fa.matricula = matricula.matricula ");
		sqlStr.append("and fa.presente = false and fa.abonado = false and ra.turma = turma.codigo and ra.disciplina = disciplina.codigo) as faltaPrimeiroBimestre, ");
		sqlStr.append("(select count(fa.registroaula) from frequenciaaula fa inner join registroaula ra on ra.codigo = fa.registroaula ");
		sqlStr.append("where data >= datainicioperiodoletivosegundobimestre and data <= datafimperiodoletivosegundobimestre and fa.matricula = matricula.matricula ");
		sqlStr.append("and fa.presente = false and fa.abonado = false and ra.turma = turma.codigo and ra.disciplina = disciplina.codigo) as faltaSegundoBimestre, ");
		sqlStr.append("(select count(fa.registroaula) from frequenciaaula fa inner join registroaula ra on ra.codigo = fa.registroaula ");
		sqlStr.append("where data >= datainicioperiodoletivoterceirobimestre and data <= datafimperiodoletivoterceirobimestre and fa.matricula = matricula.matricula ");
		sqlStr.append("and fa.presente = false and fa.abonado = false and ra.turma = turma.codigo and ra.disciplina = disciplina.codigo) as faltaTerceiroBimestre, ");
		sqlStr.append("(select count(fa.registroaula) from frequenciaaula fa inner join registroaula ra on ra.codigo = fa.registroaula ");
		sqlStr.append("where data >= datainicioperiodoletivoquartobimestre and data <= datafimperiodoletivoquartobimestre and fa.matricula = matricula.matricula ");
		sqlStr.append("and fa.presente = false and fa.abonado = false and ra.turma = turma.codigo and ra.disciplina = disciplina.codigo) as faltaQuartoBimestre ");
		sqlStr.append("from frequenciaaula ");
		sqlStr.append("inner join registroaula on registroaula.codigo = frequenciaaula.registroaula ");
		sqlStr.append("inner join disciplina on disciplina.codigo = registroaula.disciplina ");
		sqlStr.append("inner join turma on turma.codigo = registroaula.turma ");
		sqlStr.append("inner join matricula on matricula.matricula = frequenciaaula.matricula ");
		sqlStr.append("inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = frequenciaaula.matricula ");
		sqlStr.append("and matriculaperiodoturmadisciplina.disciplina = disciplina.codigo and matriculaperiodoturmadisciplina.turma = turma.codigo ");
		sqlStr.append("inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
		sqlStr.append("and matriculaperiodo.ano = RegistroAula.ano and matriculaperiodo.semestre = RegistroAula.semestre ");
		sqlStr.append("inner join unidadeensinocurso on unidadeensinocurso.curso = matricula.curso and unidadeensinocurso.turno = matricula.turno and unidadeensinocurso.unidadeensino = matricula.unidadeensino ");
		sqlStr.append("left join processomatricula on processomatricula.codigo = matriculaperiodo.processomatricula ");		
		sqlStr.append("left join processomatriculacalendario on processomatriculacalendario.processomatricula = processomatricula.codigo and unidadeensinocurso.curso = processomatriculacalendario.turno and unidadeensinocurso.curso = processomatriculacalendario.turno ");
		sqlStr.append("left join periodoletivoativounidadeensinocurso on periodoletivoativounidadeensinocurso.codigo = processomatriculacalendario.periodoletivoativounidadeensinocurso ");
		sqlStr.append("where frequenciaaula.presente = false ");
		sqlStr.append("and frequenciaaula.abonado = false ");
		sqlStr.append("and frequenciaaula.matricula = '").append(matricula).append("'");
		sqlStr.append(" and registroaula.disciplina = ").append(disciplina);
		// sqlStr.append(" and registroaula.turma = ").append(turma);
		if (!semestre.equals("")) {
			sqlStr.append(" and RegistroAula.semestre = '").append(semestre).append("'");
		}
		if (!ano.equals("")) {
			sqlStr.append(" and RegistroAula.ano = '").append(ano).append("'");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		Map<BimestreEnum, Integer> faltaBimestres = new HashMap<BimestreEnum, Integer>(0);
		while (rs.next()) {
			faltaBimestres.put(BimestreEnum.BIMESTRE_01, rs.getInt("faltaPrimeiroBimestre"));
			faltaBimestres.put(BimestreEnum.BIMESTRE_02, rs.getInt("faltaSegundoBimestre"));
			faltaBimestres.put(BimestreEnum.BIMESTRE_03, rs.getInt("faltaTerceiroBimestre"));
			faltaBimestres.put(BimestreEnum.BIMESTRE_04, rs.getInt("faltaQuartoBimestre"));
		}
		return faltaBimestres;
	}

	/**
	 * Operação responsável por adicionar um novo objeto da classe
	 * <code>FrequenciaAulaVO</code> ao List <code>frequenciaAulaVOs</code>.
	 * Utiliza o atributo padrão de consulta da classe
	 * <code>FrequenciaAula</code> - getMatricula().getMatricula() - como
	 * identificador (key) do objeto no List.
	 * 
	 * @author Wellington Rodrigues - 06/04/2015
	 * @param obj
	 * @param frequenciaAulaVOs
	 * @throws Exception
	 */
	@Override
	public void adicionarFrequenciaAulaVOs(FrequenciaAulaVO obj, List<FrequenciaAulaVO> frequenciaAulaVOs) throws Exception {
		FrequenciaAula.validarDados(obj);
		int index = 0;
		Iterator<FrequenciaAulaVO> i = frequenciaAulaVOs.iterator();
		while (i.hasNext()) {
			FrequenciaAulaVO objExistente = i.next();
			if (objExistente.getMatricula().getMatricula().equals(obj.getMatricula().getMatricula())) {
				frequenciaAulaVOs.set(index, obj);
				return;
			}
			index++;
		}
		frequenciaAulaVOs.add(obj);
	}

	/**
	 * Operação responsável por excluir um objeto da classe
	 * <code>FrequenciaAulaVO</code> no List <code>frequenciaAulaVOs</code>.
	 * Utiliza o atributo padrão de consulta da classe
	 * <code>FrequenciaAula</code> - getMatricula().getMatricula() - como
	 * identificador (key) do objeto no List.
	 * 
	 * @author Wellington Rodrigues - 06/04/2015
	 * @param matricula
	 * @param frequenciaAulaVOs
	 * @throws Exception
	 */
	@Override
	public void removerFrequenciaAulaVOs(String matricula, List<FrequenciaAulaVO> frequenciaAulaVOs) throws Exception {
		for (Iterator<FrequenciaAulaVO> iterator = frequenciaAulaVOs.iterator(); iterator.hasNext();) {
			FrequenciaAulaVO frequenciaAulaVO = iterator.next();
			if (frequenciaAulaVO.getMatricula().getMatricula().equals(matricula)) {
				iterator.remove();
				return;
			}
		}
	}
	
	@Override
	public Integer consultarTotalRegistroAula(String matricula, Integer disciplina, String semestre, String ano, Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception {		
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select sum(qtde) from (");
		sqlStr.append("select count(distinct registroaula.codigo) as qtde ");
		sqlStr.append("from registroaula ");		
		sqlStr.append("inner join matriculaperiodoturmadisciplina mptd on ");
		sqlStr.append("mptd.turma = registroaula.turma and mptd.turmapratica is null and mptd.turmateorica is null ");
		sqlStr.append("and mptd.ano = registroaula.ano and mptd.semestre = registroaula.semestre ");
		sqlStr.append("where mptd.matricula = '").append(matricula).append("' ");
		sqlStr.append("and (registroaula.disciplina = mptd.disciplina or registroaula.disciplina in (");
		sqlStr.append("select gdc.disciplina from gradedisciplinacomposta gdc where gdc.gradedisciplina in (");
		sqlStr.append("		select distinct gradedisciplina from matriculaperiodoturmadisciplina mptd1 ");
		sqlStr.append("		where mptd1.matricula = mptd.matricula ");
		sqlStr.append("		and mptd1.turma = mptd.turma ");
		sqlStr.append("		and mptd1.disciplina = mptd.disciplina ");
		sqlStr.append("		and mptd1.ano = mptd.ano ");
		sqlStr.append("		and mptd1.semestre = mptd.semestre))) ");
		sqlStr.append("and mptd.disciplina = ").append(disciplina);
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" and mptd.turma = ").append(turma);
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and mptd.semestre = '").append(semestre).append("'");
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and mptd.ano = '").append(ano).append("'");
		}
		sqlStr.append(" union all ");
		sqlStr.append("select count(distinct registroaula.codigo) as qtde ");
		sqlStr.append("from registroaula ");		
		sqlStr.append("inner join matriculaperiodoturmadisciplina mptd on ");
		sqlStr.append("mptd.turmapratica = registroaula.turma ");
		sqlStr.append("and mptd.ano = registroaula.ano and mptd.semestre = registroaula.semestre ");
		sqlStr.append("where mptd.matricula = '").append(matricula).append("' ");
		sqlStr.append("and (registroaula.disciplina = mptd.disciplina or registroaula.disciplina in (");
		sqlStr.append("select gdc.disciplina from gradedisciplinacomposta gdc where gdc.gradedisciplina in (");
		sqlStr.append("		select distinct gradedisciplina from matriculaperiodoturmadisciplina mptd1 ");
		sqlStr.append("		where mptd1.matricula = mptd.matricula ");
		sqlStr.append("		and mptd1.turma = mptd.turma ");
		sqlStr.append("		and mptd1.disciplina = mptd.disciplina ");
		sqlStr.append("		and mptd1.ano = mptd.ano ");
		sqlStr.append("		and mptd1.semestre = mptd.semestre))) ");
		sqlStr.append("and mptd.disciplina = ").append(disciplina);
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" and mptd.turma = ").append(turma);
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and mptd.semestre = '").append(semestre).append("'");
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and mptd.ano = '").append(ano).append("'");
		}
		sqlStr.append(" union all ");
		sqlStr.append("select count(distinct registroaula.codigo) as qtde ");
		sqlStr.append("from registroaula ");		
		sqlStr.append("inner join matriculaperiodoturmadisciplina mptd on ");
		sqlStr.append("mptd.turmapratica = registroaula.turma ");
		sqlStr.append("and mptd.ano = registroaula.ano and mptd.semestre = registroaula.semestre ");
		sqlStr.append("where mptd.matricula = '").append(matricula).append("' ");
		sqlStr.append("and (registroaula.disciplina = mptd.disciplina or registroaula.disciplina in (");
		sqlStr.append("select gdc.disciplina from gradedisciplinacomposta gdc where gdc.gradedisciplina in (");
		sqlStr.append("		select distinct gradedisciplina from matriculaperiodoturmadisciplina mptd1 ");
		sqlStr.append("		where mptd1.matricula = mptd.matricula ");
		sqlStr.append("		and mptd1.turma = mptd.turma ");
		sqlStr.append("		and mptd1.disciplina = mptd.disciplina ");
		sqlStr.append("		and mptd1.ano = mptd.ano ");
		sqlStr.append("		and mptd1.semestre = mptd.semestre))) ");
		sqlStr.append("and mptd.disciplina = ").append(disciplina);
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" and mptd.turma = ").append(turma);
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and mptd.semestre = '").append(semestre).append("'");
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and mptd.ano = '").append(ano).append("'");
		}
		sqlStr.append(") as t ");
		return getConexao().getJdbcTemplate().queryForInt(sqlStr.toString());
	}
	
	@Override
	public Integer consultarSomaCargaHorariaDisciplinaConsiderantoTurmaTeoricaETurmaPratica(Integer turmaPratica, Integer turmaTeorica, String semestre, String ano, Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		Integer cargaHoraria = getFacadeFactory().getRegistroAulaFacade().consultarSomaCargaHorarioDisciplina(turmaPratica, semestre, ano, disciplina, false, usuario, true);
		cargaHoraria += getFacadeFactory().getRegistroAulaFacade().consultarSomaCargaHorarioDisciplina(turmaTeorica, semestre, ano, disciplina, false, usuario, true);
		return cargaHoraria;
	}
	
	public PersonalizacaoMensagemAutomaticaVO carregarDadosMensagemPersonalizada(UsuarioVO usuarioVO, Integer unidadeEnsino) throws Exception{
		PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaVO = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.NOTIFICACAO_REGISTRO_DE_AULA, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, unidadeEnsino, usuarioVO, null);
		return personalizacaoMensagemAutomaticaVO;
	}
		
	public void executarEnvioComunicadoInternoRegistroAula(ComunicacaoInternaVO comunicacaoInternaVO, String dataRegistro, String conteudo, UsuarioVO usuarioVO,ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,Boolean enviarSms,Boolean enviarEmail) throws Exception{
		 ComunicacaoInternaVO comunicacaoInterna = null;
		    try {
					comunicacaoInterna =  (ComunicacaoInternaVO) comunicacaoInternaVO.clone();
					ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
					comunicadoInternoDestinatarioVO.setTipoComunicadoInterno("LE");
					comunicadoInternoDestinatarioVO.setDestinatario(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(usuarioVO.getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
				    comunicacaoInterna.setProfessor(comunicadoInternoDestinatarioVO.getDestinatario());
					if (Uteis.isAtributoPreenchido(usuarioVO.getPessoa().getEmail())) {
						comunicadoInternoDestinatarioVO.setEmail(usuarioVO.getPessoa().getEmail());
						comunicadoInternoDestinatarioVO.getDestinatario().setEmail(usuarioVO.getPessoa().getEmail());
						comunicacaoInterna.setEnviarEmail(enviarEmail);
					} else if (Uteis.isAtributoPreenchido(usuarioVO.getPessoa().getEmail2())) {
						comunicadoInternoDestinatarioVO.setEmail(usuarioVO.getPessoa().getEmail2());
						comunicadoInternoDestinatarioVO.getDestinatario().setEmail(usuarioVO.getPessoa().getEmail2());
						comunicacaoInterna.setEnviarEmail(enviarEmail);
					} else{
						comunicacaoInterna.setEnviarEmail(false);
					}
					if(Uteis.isAtributoPreenchido(usuarioVO.getPessoa().getCelular())){
						comunicadoInternoDestinatarioVO.getDestinatario().setCelular(usuarioVO.getPessoa().getCelular());
						comunicacaoInterna.setEnviarSMS(enviarSms);
					}else{
						comunicacaoInterna.setEnviarSMS(false);
					}
					comunicadoInternoDestinatarioVO.setNome(usuarioVO.getPessoa().getNome());
					comunicadoInternoDestinatarioVO.getDestinatario().setNome(usuarioVO.getPessoa().getNome());
					comunicacaoInterna.getComunicadoInternoDestinatarioVOs().add(comunicadoInternoDestinatarioVO);
					comunicacaoInterna.setTipoRemetente("FU");
					comunicacaoInterna.setTipoDestinatario("PR");
					comunicacaoInterna.setTipoComunicadoInterno("LE");
					
			    	if (Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getResponsavelPadraoComunicadoInterno().getCodigo())) {
			    	  comunicacaoInterna.setResponsavel(configuracaoGeralSistemaVO.getResponsavelPadraoComunicadoInterno());
				   } else {
					  comunicacaoInterna.setResponsavel(usuarioVO.getPessoa());
				   }
					comunicacaoInterna.setMensagem(getFacadeFactory().getRegistroAulaFacade().realizarSubstituicaoTagMensagem(conteudo, usuarioVO, dataRegistro, comunicacaoInterna.getMensagem()));
					comunicacaoInterna.setMensagemSMS(getFacadeFactory().getRegistroAulaFacade().realizarSubstituicaoTagMensagemSms(conteudo, usuarioVO, dataRegistro, comunicacaoInterna.getMensagemSMS()));			
					getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoInterna, false, usuarioVO, configuracaoGeralSistemaVO,null);
					comunicadoInternoDestinatarioVO = null;
					comunicacaoInterna = null;
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
 	}
	
	public String realizarSubstituicaoTagMensagem(String conteudo, UsuarioVO usuario, String dataRegistro, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CONTEUDO_AULA.name(), conteudo);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PROFESSOR.name(), usuario.getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_REGISTRO.name(), dataRegistro);
		return mensagemTexto;
	}
	
	public String realizarSubstituicaoTagMensagemSms(String conteudo, UsuarioVO usuario, String dataRegistro, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CONTEUDO_AULA.name(), conteudo);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PROFESSOR.name(), usuario.getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_REGISTRO.name(), dataRegistro);
		if (mensagemTexto.length() > 150) {
			mensagemTexto = mensagemTexto.substring(0, 150);
		}
		return mensagemTexto;
	}
	
	@Override
	public List<RegistroAulaVO> realizarGeracaoRegistroAulaPeloHorarioProfessorDia(HorarioProfessorDiaVO horarioProfessorDiaVO, UsuarioVO usuarioVO, TurmaVO turmaVO, DisciplinaVO disciplinaVO, PessoaVO professorVO, String ano, String semestre, Boolean trazerAlunosTransferenciaMatriz) throws Exception {		
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, turmaVO.getUnidadeEnsino().getCodigo());
			boolean permitirRealizarLancamentoAlunosPreMatriculados = configuracaoGeralSistemaVO.getPermitirProfessorRealizarLancamentoAlunosPreMatriculados();
			boolean trazerAlunoPendenteFinanceiramente = usuarioVO.getIsApresentarVisaoProfessor() ? 
					configuracaoGeralSistemaVO.getApresentarAlunoPendenteFinanceiroVisaoProfessor()
					: usuarioVO.getIsApresentarVisaoCoordenador() ? configuracaoGeralSistemaVO.getApresentarAlunoPendenteFinanceiroVisaoCoordenador() : true;
			List<RegistroAulaVO> listaRegistrosAula = new ArrayList<RegistroAulaVO>(0);
			boolean somenteAlunosAtivos = !verificarPermissaoVisualizarMatriculaTR_CA(usuarioVO);
			//CalendarioRegistroAulaVO calendarioRegistroAulaVO = getFacadeFactory().getCalendarioRegistroAulaFacade().consultarPorCalendarioRegistroAulaUtilizar(turmaVO.getUnidadeEnsino().getCodigo(),turmaVO.getCodigo(), turmaVO.getTurmaAgrupada(), professorVO.getCodigo(), Uteis.getData(horarioProfessorDiaVO.getData(), "yyyy"), false, usuarioVO);
			listaRegistrosAula = montarRegistrosAula(horarioProfessorDiaVO, turmaVO, disciplinaVO, professorVO, ano, semestre, permitirRealizarLancamentoAlunosPreMatriculados, somenteAlunosAtivos, trazerAlunosTransferenciaMatriz, trazerAlunoPendenteFinanceiramente, usuarioVO);		
			return listaRegistrosAula;
	}
	
	@Override
	public Boolean verificarPermissaoVisualizarMatriculaTR_CA(UsuarioVO usuarioVO) {
		try {
			if (usuarioVO.getUsuarioPerfilAcessoVOs().isEmpty()) {
				UsuarioPerfilAcessoVO u = new UsuarioPerfilAcessoVO();
				u.setPerfilAcesso(usuarioVO.getPerfilAcesso());
				usuarioVO.getUsuarioPerfilAcessoVOs().add(u);
			}
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("RegistrarAula_VisualizarMatriculaTR_CA", usuarioVO);
			return (true);
		} catch (Exception e) {
			return (false);
		}
	}
	
	
	public List<RegistroAulaVO>  montarRegistrosAula(HorarioProfessorDiaVO horarioProfessorDiaVO, TurmaVO turmaVO, DisciplinaVO disciplina, PessoaVO professor, String ano, String semestre, boolean permitirRealizarLancamentoAlunosPreMatriculados, boolean somenteAlunosAtivos, Boolean trazerAlunosTransferenciaMatriz, boolean trazerAlunoPendenteFinanceiramente, UsuarioVO usuarioVO) throws Exception {
		List<RegistroAulaVO> listaRegistrosAula = new ArrayList<RegistroAulaVO>(0);
		boolean professorConsultarSomenteAlunosAtivos = usuarioVO.getPessoa().getCodigo().equals(professor.getCodigo()) ? somenteAlunosAtivos : false;
		if(horarioProfessorDiaVO.getIsLancadoRegistro()){
			listaRegistrosAula = getFacadeFactory().getRegistroAulaFacade().consultaRapidaPorHorarioTurmaDia(horarioProfessorDiaVO, turmaVO, disciplina.getCodigo(), professor.getCodigo(), ano, semestre, turmaVO.getCurso().getLiberarRegistroAulaEntrePeriodo(), usuarioVO, professorConsultarSomenteAlunosAtivos, trazerAlunoPendenteFinanceiramente, trazerAlunosTransferenciaMatriz);
		}
		
		List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplina = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorCodigoTurmaDisciplinaSemestreAnoFiltroVisaoProfessor(listaRegistrosAula, disciplina.getCodigo(), ano, semestre, "", false, professorConsultarSomenteAlunosAtivos, trazerAlunoPendenteFinanceiramente, "", "", false, usuarioVO, turmaVO, trazerAlunosTransferenciaMatriz, permitirRealizarLancamentoAlunosPreMatriculados);
		String tipoAula = "P";
		String conteudo = !listaRegistrosAula.isEmpty() ? listaRegistrosAula.get(0).getConteudo() : "";
		getFacadeFactory().getRegistroAulaFacade().montarRegistrosAula(null, horarioProfessorDiaVO, listaRegistrosAula, disciplina, professor, ano, semestre, 0, usuarioVO, tipoAula, turmaVO, conteudo, professorConsultarSomenteAlunosAtivos, horarioProfessorDiaVO.getIsLancadoRegistro(), listaMatriculaPeriodoTurmaDisciplina, usuarioVO, trazerAlunoPendenteFinanceiramente, trazerAlunosTransferenciaMatriz, permitirRealizarLancamentoAlunosPreMatriculados);		
		if(turmaVO.getTurmaAgrupada()){
			carregarConfiguracaoAcademicaCursoDaMatricula(listaRegistrosAula, usuarioVO);
		}
		Ordenacao.ordenarLista(listaRegistrosAula, "nrAula");
		for(RegistroAulaVO registroAulaVO: listaRegistrosAula) {
			Ordenacao.ordenarLista(registroAulaVO.getFrequenciaAulaVOs(), "ordenacaoSemAcentuacaoNome");
		}
		return listaRegistrosAula;
		
	}
	
	
	public void carregarConfiguracaoAcademicaCursoDaMatricula(List<RegistroAulaVO> listaRegistrosAula, UsuarioVO usuarioVO) {
		for (RegistroAulaVO registroAulaVO : listaRegistrosAula) { 
			for (FrequenciaAulaVO frequenciaAulaVO : registroAulaVO.getFrequenciaAulaVOs()) {
				try {
					if (Uteis.isAtributoPreenchido(frequenciaAulaVO.getMatricula().getCurso().getConfiguracaoAcademico())) {
						frequenciaAulaVO.getMatricula().getCurso().setConfiguracaoAcademico(getAplicacaoControle().carregarDadosConfiguracaoAcademica(frequenciaAulaVO.getMatricula().getCurso().getConfiguracaoAcademico().getCodigo()));						
						}
					if (frequenciaAulaVO.getMatricula().getCurso().getConfiguracaoAcademico().getBloquearRegistroAulaAnteriorDataMatricula()) {
						Date dataRegistro = frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().getData();
						if (frequenciaAulaVO.getHistoricoVO().getDataRegistro() != null) {
							dataRegistro = frequenciaAulaVO.getHistoricoVO().getDataRegistro();
						}
						if (dataRegistro.after(registroAulaVO.getData())) {
							frequenciaAulaVO.setBloqueadoDevidoDataMatricula(true);						
							frequenciaAulaVO.setFrequenciaOculta(true);
							frequenciaAulaVO.setPresente(false);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}		
	}

	private void verificarExistenciaRegistroAula(RegistroAulaVO registroAulaVO) throws Exception {
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet("Select codigo from registroaula where codigo = ? ", registroAulaVO.getCodigo());
		if (resultado.next()) {
			if (Uteis.isAtributoPreenchido(new Integer(resultado.getInt("codigo")))) {
				return;
			}
		}
		throw new ConsistirException("O registro de aula não existe mais, portanto não pode ser alterado. Por favor, consulte novamente.");
	}

	@Override
	public void validarQuantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAulaVisaoProfessor(Date data, TurmaVO turma, DisciplinaVO disciplina, PessoaVO professor, String ano, String semestre, UsuarioVO usuarioVO) throws Exception {
		if (usuarioVO.getVisaoLogar().equals("professor")) {
			ConfiguracaoAcademicoVO configuracaoAcademicoVO;
			if (turma.getTurmaAgrupada()) {
				 configuracaoAcademicoVO = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorCodigoTurmaAgrupada(turma.getCodigo(), usuarioVO);
			}else {			
				 configuracaoAcademicoVO = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorCodigoCurso(turma.getCurso().getCodigo(), usuarioVO);
			}
			if (Uteis.isAtributoPreenchido(configuracaoAcademicoVO)) {

				if (Uteis.isAtributoPreenchido(configuracaoAcademicoVO.getQuantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAula())) {
					Date dataUltimaAulaProgramada = getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorTurmaDisciplina(turma.getCodigo(), disciplina.getCodigo(), ano, semestre, professor.getCodigo());

					Date dataSomadaComDias = UteisData.adicionarDiasEmData(dataUltimaAulaProgramada, configuracaoAcademicoVO.getQuantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAula());

					if (UteisData.validarDataInicialMaiorFinal(new Date(), dataSomadaComDias)) {						
						throw new ConsistirException(UteisJSF.internacionalizar("msg_registroAula_dataLimiteRegistroAula").replace("{0}", UteisData.getDataFormatada(dataSomadaComDias)));
					}
				}
			}
		}
	}
	
	@Override
	public Integer consultarQtdeRegistroAulaPorGradeDisciplina(Integer gradeDisciplina, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct count(distinct registroAula.codigo) as qtde from registroAula ");
		sb.append(" inner join turma on turma.codigo = registroAula.turma ");
		sb.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo ");
		sb.append(" where  turmadisciplina.gradedisciplina = ?");
		sb.append(" and turmadisciplina.disciplina = registroAula.disciplina ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), new Object[] {gradeDisciplina});
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}
}