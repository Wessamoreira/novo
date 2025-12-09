package negocio.facade.jdbc.protocolo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jakarta.faces.model.SelectItem;

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

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.AproveitamentoDisciplinaVO;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CancelamentoVO;
import negocio.comuns.academico.CidTipoRequerimentoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.MaterialRequerimentoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProgramacaoFormaturaUnidadeEnsinoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TrancamentoVO;
import negocio.comuns.academico.TransferenciaEntradaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.TipoAlunoEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.OperacaoFuncionalidadeVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.avaliacaoinst.RespostaAvaliacaoInstitucionalDWVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.biblioteca.ImpressoraVO;
import negocio.comuns.biblioteca.PoolImpressaoVO;
import negocio.comuns.biblioteca.enumeradores.FormatoImpressaoEnum;
import negocio.comuns.blackboard.SalaAulaBlackboardPessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardPessoaEnum;

import negocio.comuns.protocolo.AlterarResponsavelRequerimentoVO;
import negocio.comuns.protocolo.EstatisticaRequerimentoVO;
import negocio.comuns.protocolo.RequerimentoDisciplinaVO;
import negocio.comuns.protocolo.RequerimentoDisciplinasAproveitadasVO;
import negocio.comuns.protocolo.RequerimentoHistoricoVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.protocolo.TipoRequerimentoDepartamentoVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.protocolo.enumeradores.SituacaoRequerimentoDisciplinasAproveitadasEnum;
import negocio.comuns.protocolo.enumeradores.SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum;
import negocio.comuns.protocolo.enumeradores.TipoControleCobrancaViaRequerimentoEnum;
import negocio.comuns.protocolo.enumeradores.TipoDistribuicaoResponsavelEnum;
import negocio.comuns.secretaria.enumeradores.TipoUploadArquivoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.SituacaoRequerimento;
import negocio.comuns.utilitarias.dominios.SituacaoTransferenciaEntrada;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.comuns.utilitarias.dominios.TipoBoletoBancario;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.dominios.TipoVisaoAcesso;
import negocio.comuns.utilitarias.dominios.TiposRequerimento;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.protocolo.RequerimentoInterfaceFacade;
import relatorio.arquitetura.GeradorRelatorio;
import relatorio.negocio.comuns.academico.RequerimentoResumoOperacaoLoteVO;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.RequerimentoRel;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>RequerimentoVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>RequerimentoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see RequerimentoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Requerimento extends ControleAcesso implements RequerimentoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7288447026204415838L;
	protected static String idEntidade;

	public Requerimento() throws Exception {
		super();
		setIdEntidade("Requerimento");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>RequerimentoVO</code>.
	 */
	public RequerimentoVO novo() throws Exception {
		Requerimento.incluir(getIdEntidade());
		RequerimentoVO obj = new RequerimentoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>RequerimentoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>RequerimentoVO</code> que será gravado
	 *            no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final RequerimentoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		try {

			incluir(getIdEntidade(), true, usuario);

			validarDadosValorRequerimento(obj);
			RequerimentoVO.validarDados(obj);
			validarDadosBloqueioRequerimentoAbertoSimultaneo(obj, usuario);
			realizarValidacaoRegrasCriacaoRequerimento(obj, usuario);
			if (obj.getValorTotalFinal().doubleValue() == 0.0) {
				obj.setSituacaoFinanceira("IS");
				if(((!obj.getTipoRequerimento().getIsEmissaoCertificado() && !obj.getTipoRequerimento().getIsDeclaracao() 
				&&  obj.getTipoRequerimento().getDeferirAutomaticamente()) 
				|| ((obj.getTipoRequerimento().getIsEmissaoCertificado() || obj.getTipoRequerimento().getIsDeclaracao()) 
				&& (obj.getIsFormatoCertificadoSelecionadoDigital() &&  obj.getTipoRequerimento().getDeferirAutomaticamente()) 
				|| (obj.getIsFormatoCertificadoSelecionadoImpresso() &&  obj.getTipoRequerimento().getDeferirAutomaticamenteDocumentoImpresso())))
				&& obj.getMotivoNaoAceiteCertificado().equals(""))  {				
					obj.setSituacao(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor());
					obj.setDataFinalizacao(new Date());
				} else {
					obj.setSituacao(SituacaoRequerimento.PENDENTE.getValor());
				}
			}
			List<RespostaAvaliacaoInstitucionalDWVO> listaRespostaQuestionario = null;
			if (obj.getQuestionarioVO().getCodigo() > 0) {
				listaRespostaQuestionario = getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().gerarRespostaQuestionarioRequerimento(obj);
				for (RespostaAvaliacaoInstitucionalDWVO objResp : listaRespostaQuestionario) {
					RespostaAvaliacaoInstitucionalDWVO.validarDados(objResp);
				}
			}
			if (!(Uteis.isAtributoPreenchido(obj.getNomeRequerente()) && Uteis.isAtributoPreenchido(obj.getCpfRequerente())) && Uteis.isAtributoPreenchido(usuario) && Uteis.isAtributoPreenchido(usuario.getPessoa())) {
				obj.setNomeRequerente(usuario.getPessoa().getNome());
				obj.setCpfRequerente(usuario.getPessoa().getCPF());
			}
			final StringBuilder sql = new StringBuilder("INSERT INTO Requerimento( data, tipoRequerimento, valor, dataPrevistaFinalizacao, ");
			sql.append("dataFinalizacao, situacao, situacaoFinanceira, departamentoResponsavel, matricula, nomeRequerente, cpfRequerente, ");
			sql.append("observacao, datarecebimentodocrequerido, responsavelrecebimentodocrequerido, centroReceita, contaReceber, nrDocumento, responsavelEmissaoBoleto, ");
			sql.append("dataEmissaoBoleto, contaCorrente, unidadeEnsino, pessoa, percDesconto, valorDesconto, tipoDesconto, cep, endereco, setor, numero, complemento, cidade, arquivo, ");
			sql.append("responsavel, visaoGerado, valorAdicional, motivoIndeferimento, turma, disciplina, ordemExecucaoTramiteDepartamento, funcionario, questionario, curso, turno, taxaIsentaPorQtdeVia, ");
			sql.append(" taxa, numeroVia, dataUltimaAlteracao, motivoDeferimento, tipoPessoa, unidadeEnsinoTransferenciaInterna, cursoTransferenciaInterna, turnoTransferenciaInterna, ");			
			sql.append(" matriculaPeriodo, disciplinaPorEquivalencia, mapaEquivalenciaDisciplina, mapaEquivalenciaDisciplinaCursada, dataInicioAula, dataTerminoAula, salaLocalAula, cargaHorariaDisciplina, turmareposicao, mensagemChoqueHorario, motivoNaoAceiteCertificado, formatoCertificadoSelecionado, turmaBase, justificativaTrancamento, motivoCancelamentoTrancamento,  ");			
			sql.append(" tipoTrabalhoConclusaoCurso, tituloMonografia, orientadorMonografia, grupofacilitador, temaTccFacilitador, assuntoTccFacilitador, avaliadorExternoFacilitador, cid, dataafastamentoinicio, dataafastamentofim "); 
			sql.append(" ) ");
			sql.append(" VALUES ( ?, ?, ?, ?, ");
			sql.append(" ?, ?, ?, ?, ?, ?, ?, "); //11
			sql.append(" ?, ?, ?, ?, ?, ?, ?, "); //18
			sql.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "); //32
			sql.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "); //44
			sql.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "); //67
			sql.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "); //77
			sql.append(" returning codigo").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					try {
						int x = 1;
						sqlInserir.setTimestamp(x++, Uteis.getDataJDBCTimestamp(obj.getData()));
						sqlInserir.setInt(x++, obj.getTipoRequerimento().getCodigo().intValue());
						sqlInserir.setDouble(x++, obj.getValor().doubleValue());
						sqlInserir.setDate(x++, Uteis.getDataJDBC(obj.getDataPrevistaFinalizacao()));
						sqlInserir.setDate(x++, Uteis.getDataJDBC(obj.getDataFinalizacao()));
						sqlInserir.setString(x++, obj.getSituacao());
						sqlInserir.setString(x++, obj.getSituacaoFinanceira());
						sqlInserir.setInt(x++, obj.getDepartamentoResponsavel().getCodigo().intValue());
						if(!obj.getMatricula().getMatricula().trim().isEmpty()){
							sqlInserir.setString(x++, obj.getMatricula().getMatricula());
						}else{
							sqlInserir.setNull(x++, 0);
						}
						sqlInserir.setString(x++, obj.getNomeRequerente());
						sqlInserir.setString(x++, obj.getCpfRequerente());
						sqlInserir.setString(x++, obj.getObservacao());
						if (obj.getDataRecebimentoDocRequerido() != null) {
							sqlInserir.setDate(x++, Uteis.getDataJDBC(obj.getDataRecebimentoDocRequerido()));
						} else {
							sqlInserir.setNull(x++, 0);
						}
						if (obj.getResponsavelRecebimentoDocRequerido().getCodigo() != 0) {
							sqlInserir.setInt(x++, obj.getResponsavelRecebimentoDocRequerido().getCodigo());
						} else {
							sqlInserir.setNull(x++, 0);
						}
//						sqlInserir.setInt(x++, obj.getCentroReceita().getCodigo().intValue());
//						if (obj.getContaReceber() != null && obj.getContaReceber() > 0) {
//							sqlInserir.setInt(x++, obj.getContaReceber().intValue());
//						} else {
//							sqlInserir.setNull(x++, 0);
//						}
						sqlInserir.setString(x++, obj.getNrDocumento());
						if (obj.getResponsavelEmissaoBoleto().getCodigo().intValue() != 0) {
							sqlInserir.setInt(x++, obj.getResponsavelEmissaoBoleto().getCodigo().intValue());
						} else {
							sqlInserir.setNull(x++, 0);
						}
//						sqlInserir.setDate(x++, Uteis.getDataJDBC(obj.getDataEmissaoBoleto()));
//						if (obj.getContaCorrenteVO().getCodigo().intValue() != 0) {
//							sqlInserir.setInt(x++, obj.getContaCorrenteVO().getCodigo());
//						} else {
//							sqlInserir.setNull(x++, 0);
//						}
						if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
							sqlInserir.setInt(x++, obj.getUnidadeEnsino().getCodigo());
						} else {
							sqlInserir.setNull(x++, 0);
						}
						if (obj.getPessoa().getCodigo().intValue() != 0) {
							sqlInserir.setInt(x++, obj.getPessoa().getCodigo());
						} else {
							sqlInserir.setNull(x++, 0);
						}
						sqlInserir.setDouble(x++, obj.getPercDesconto());
						sqlInserir.setDouble(x++, obj.getValorDesconto());
						sqlInserir.setString(x++, obj.getTipoDesconto());
						sqlInserir.setString(x++, obj.getCEP());
						sqlInserir.setString(x++, obj.getEndereco());
						sqlInserir.setString(x++, obj.getSetor());
						sqlInserir.setString(x++, obj.getNumero());
						sqlInserir.setString(x++, obj.getComplemento());
						if (obj.getCidade().getCodigo().intValue() != 0) {
							sqlInserir.setInt(x++, obj.getCidade().getCodigo());
						} else {
							sqlInserir.setNull(x++, 0);
						}
						if (obj.getArquivoVO().getCodigo().intValue() != 0) {
							sqlInserir.setInt(x++, obj.getArquivoVO().getCodigo());
						} else {
							sqlInserir.setNull(x++, 0);
						}
						if (obj.getResponsavel().getCodigo().intValue() != 0) {
							sqlInserir.setInt(x++, obj.getResponsavel().getCodigo());
						} else {
							sqlInserir.setNull(x++, 0);
						}
						sqlInserir.setString(x++, obj.getVisaoGerado());
						sqlInserir.setDouble(x++, obj.getValorAdicional());
						sqlInserir.setString(x++, obj.getMotivoIndeferimento());
						if (obj.getTurma().getCodigo() != 0) {
							sqlInserir.setInt(x++, obj.getTurma().getCodigo());
						} else {
							sqlInserir.setNull(x++, 0);
						}
						if (obj.getDisciplina().getCodigo() != 0) {
							sqlInserir.setInt(x++, obj.getDisciplina().getCodigo());
						} else {
							sqlInserir.setNull(x++, 0);
						}
						sqlInserir.setInt(x++, obj.getOrdemExecucaoTramiteDepartamento());
						if (obj.getFuncionarioVO().getCodigo() != 0) {
							sqlInserir.setInt(x++, obj.getFuncionarioVO().getCodigo());
						} else {
							sqlInserir.setNull(x++, 0);
						}
						if (obj.getQuestionarioVO().getCodigo() != 0) {
							sqlInserir.setInt(x++, obj.getQuestionarioVO().getCodigo());
						} else {
							sqlInserir.setNull(x++, 0);
						}
						if (obj.getCurso().getCodigo() != 0) {
							sqlInserir.setInt(x++, obj.getCurso().getCodigo());
						} else {
							sqlInserir.setNull(x++, 0);
						}
						if (obj.getTurno().getCodigo() != 0) {
							sqlInserir.setInt(x++, obj.getTurno().getCodigo());
						} else {
							sqlInserir.setNull(x++, 0);
						}
						sqlInserir.setBoolean(x++, obj.getTaxaIsentaPorQtdeVia());
//						if (obj.getTaxa().getCodigo() != 0) {
//							sqlInserir.setInt(x++, obj.getTaxa().getCodigo());
//						} else {
//							sqlInserir.setNull(x++, 0);
//						}
						sqlInserir.setInt(x++, obj.getNumeroVia());
						sqlInserir.setDate(x++, Uteis.getDataJDBC(new Date()));
						sqlInserir.setString(x++, obj.getMotivoDeferimento());
						sqlInserir.setString(x++, obj.getTipoPessoa().name());
						if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoTransferenciaInternaVO())) {
							sqlInserir.setInt(x++, obj.getUnidadeEnsinoTransferenciaInternaVO().getCodigo());
						} else {
							sqlInserir.setNull(x++, 0);
						}
						if (Uteis.isAtributoPreenchido(obj.getCursoTransferenciaInternaVO())) {
							sqlInserir.setInt(x++, obj.getCursoTransferenciaInternaVO().getCodigo());
						} else {
							sqlInserir.setNull(x++, 0);
						}
						if (Uteis.isAtributoPreenchido(obj.getTurnoTransferenciaInternaVO())) {
							sqlInserir.setInt(x++, obj.getTurnoTransferenciaInternaVO().getCodigo());
						} else {
							sqlInserir.setNull(x++, 0);
						}
						if (Uteis.isAtributoPreenchido(obj.getMatriculaPeriodoVO()) && obj.getMatriculaPeriodoVO().getMatricula().equals(obj.getMatricula().getMatricula())) {
							sqlInserir.setInt(x++, obj.getMatriculaPeriodoVO().getCodigo());
						} else {
							sqlInserir.setNull(x++, 0);
						}	
						if(obj.getTipoRequerimento().getIsTipoReposicao()) {
							sqlInserir.setBoolean(x++, obj.getDisciplinaPorEquivalencia());
							if (Uteis.isAtributoPreenchido(obj.getMapaEquivalenciaDisciplinaVO()) && obj.getDisciplinaPorEquivalencia()) {
								sqlInserir.setInt(x++, obj.getMapaEquivalenciaDisciplinaVO().getCodigo());
							} else {
								sqlInserir.setNull(x++, 0);
							}
							if (Uteis.isAtributoPreenchido(obj.getMapaEquivalenciaDisciplinaCursadaVO())  && obj.getDisciplinaPorEquivalencia()) {
								sqlInserir.setInt(x++, obj.getMapaEquivalenciaDisciplinaCursadaVO().getCodigo());
							} else {
								sqlInserir.setNull(x++, 0);
							}
							if (Uteis.isAtributoPreenchido(obj.getDataInicioAula())) {
								sqlInserir.setDate(x++, Uteis.getDataJDBC(obj.getDataInicioAula()));
							} else {
								sqlInserir.setNull(x++, 0);
							}
							if (Uteis.isAtributoPreenchido(obj.getDataTerminoAula())) {
								sqlInserir.setDate(x++, Uteis.getDataJDBC(obj.getDataTerminoAula()));
							} else {
								sqlInserir.setNull(x++, 0);
							}
//							if (Uteis.isAtributoPreenchido(obj.getSalaLocalAulaVO()) && obj.getSalaLocalAulaVO().getCodigo() > 0) {
//								sqlInserir.setInt(x++, obj.getSalaLocalAulaVO().getCodigo());
//							} else {
//								sqlInserir.setNull(x++, 0);
//							}
							sqlInserir.setInt(x++, obj.getCargaHorariaDisciplina());
							if (Uteis.isAtributoPreenchido(obj.getTurmaReposicao())) {
								sqlInserir.setInt(x++, obj.getTurmaReposicao().getCodigo());
							} else {
								sqlInserir.setNull(x++, 0);
							}
							sqlInserir.setString(x++, obj.getMensagemChoqueHorario());
						}else {
							sqlInserir.setNull(x++, 0);
							sqlInserir.setNull(x++, 0);
							sqlInserir.setNull(x++, 0);
							sqlInserir.setNull(x++, 0);
							sqlInserir.setNull(x++, 0);
							sqlInserir.setNull(x++, 0);
							sqlInserir.setNull(x++, 0);
							sqlInserir.setNull(x++, 0);
							sqlInserir.setNull(x++, 0);
						}
						sqlInserir.setString(x++, obj.getMotivoNaoAceiteCertificado());
						sqlInserir.setString(x++, obj.getFormatoCertificadoSelecionado());
						sqlInserir.setString(x++, obj.getTurmaReposicao().getIdentificadorTurmaBase());
						if (Uteis.isAtributoPreenchido(obj.getJustificativaTrancamento())) {
							sqlInserir.setString(x++, obj.getJustificativaTrancamento());
						} else {
							sqlInserir.setNull(x++, 0);
						}
						
						if (Uteis.isAtributoPreenchido(obj.getMotivoCancelamentoTrancamento().getCodigo())) {
							sqlInserir.setInt(x++, obj.getMotivoCancelamentoTrancamento().getCodigo());
						} else {
							sqlInserir.setNull(x++, 0);
						}
						sqlInserir.setString(x++, obj.getTipoTrabalhoConclusaoCurso());
						sqlInserir.setString(x++, obj.getTituloMonografia());
						sqlInserir.setString(x++, obj.getOrientadorMonografia());
						if (obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.CERTIFICADO_PARTICIPACAO_TCC.getValor()) && Uteis.isAtributoPreenchido(obj.getGrupoFacilitador())) {
							sqlInserir.setInt(x++, obj.getGrupoFacilitador().getCodigo());
						} else {
							sqlInserir.setNull(x++, 0);
						}
						if (obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.CERTIFICADO_PARTICIPACAO_TCC.getValor())) {
							sqlInserir.setString(x++, obj.getTemaTccFacilitador().trim());
						} else {
							sqlInserir.setNull(x++, 0);
						}
						if (obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.CERTIFICADO_PARTICIPACAO_TCC.getValor())) {
							sqlInserir.setString(x++, obj.getAssuntoTccFacilitador().trim());
						} else {
							sqlInserir.setNull(x++, 0);
						}
						if (obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.CERTIFICADO_PARTICIPACAO_TCC.getValor())) {
							sqlInserir.setString(x++, obj.getAvaliadorExternoFacilitador().trim());
						} else {
							sqlInserir.setNull(x++, 0);
						}
						sqlInserir.setString(x++, obj.getCid());

						if (obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.OUTROS.getValor()) && obj.getTipoRequerimento().getCampoAfastamento()) {
							if (Uteis.isAtributoPreenchido(obj.getDataAfastamentoInicio())) {
								sqlInserir.setDate(x++, Uteis.getDataJDBC(obj.getDataAfastamentoInicio()));
							} else {
								sqlInserir.setNull(x++, 0);
							}
							if (Uteis.isAtributoPreenchido(obj.getDataAfastamentoFim())) {
								sqlInserir.setDate(x++, Uteis.getDataJDBC(obj.getDataAfastamentoFim()));
							} else {
								sqlInserir.setNull(x++, 0);
							}
						} else {
							sqlInserir.setNull(x++, 0);
							sqlInserir.setNull(x++, 0);
						}

					} catch (Exception x) {
						return null;
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			
//			if (obj.getValorTotalFinal() > 0.0 && !obj.getSituacaoFinanceira().equals("AP")) {
//				emitirBoletoParcela(obj, usuario);
//			}


			if (obj.getQuestionarioVO().getCodigo() > 0 && listaRespostaQuestionario != null) {
				for (RespostaAvaliacaoInstitucionalDWVO objResp : listaRespostaQuestionario) {
					objResp.setRequerimento(obj.getCodigo());
				}
				getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().incluirTodas(listaRespostaQuestionario, usuario);
			}
			if (obj.getTipoRequerimento().getTramitaEntreDepartamentos() && ((obj.getSituacaoFinanceira().equals("IS") && obj.getSituacao().equals("PE")) 
					|| (obj.getSituacaoFinanceira().equals("AP") && obj.getSituacao().equals("PE"))
					|| (obj.getSituacaoFinanceira().equals("PE") && !obj.getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.NAO_REQUERIDA)))) {
				TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO = obj.getTipoRequerimento().getTipoRequerimentoDepartamentoVOs().get(obj.getOrdemExecucaoTramiteDepartamento() - 1);
				if (!Uteis.isAtributoPreenchido(obj.getFuncionarioVO().getCodigo())) {
					obj.setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().realizarDistribuicaoResponsavelRequerimento(obj.getCodigo(), tipoRequerimentoDepartamentoVO, tipoRequerimentoDepartamentoVO.getTipoDistribuicaoResponsavel(), obj.getUnidadeEnsino(), usuario, obj.getTipoRequerimento()));
				}
				obj.gerarNovoRequerimentoHistoricoVODepartamentoAtualTramite(usuario, true, obj.getFuncionarioVO(), tipoRequerimentoDepartamentoVO, false, "");
				alterarFuncionarioResponsavel(obj.getCodigo(), obj.getFuncionarioVO().getCodigo(), usuario);
			}
			getFacadeFactory().getRequerimentoHistoricoFacade().incluirRequerimentoHistoricoVOs(obj.getCodigo(), obj.getRequerimentoHistoricoVOs(), usuario);
			
			if (!obj.getArquivoVO().getNome().equals("") && !obj.getExcluirArquivo()) {
				if (!obj.getArquivoVO().getCodigo().equals(0)) {
					getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoVO(), usuario, configuracaoGeralSistema);
				} else {
					getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVO(), usuario, configuracaoGeralSistema);
					alterarCodigoArquivo(obj, obj.getArquivoVO().getCodigo(), usuario);
				}
			}
			if(!obj.getRequerimentoHistoricoVOs().isEmpty()) {
				for (MaterialRequerimentoVO materialRequerimentoVO : obj.getMaterialRequerimentoVOs()) {
					materialRequerimentoVO.setRequerimentoHistorico(obj.getRequerimentoHistoricoVOs().get(0));
				}
			}
			getFacadeFactory().getMaterialRequerimentoFacade().incluirMaterialRequerimentos(obj.getCodigo(), obj.getMaterialRequerimentoVOs(), usuario, configuracaoGeralSistema);
			getFacadeFactory().getRequerimentoDisciplinasAproveitadasFacade().persistir(obj.getListaRequerimentoDisciplinasAproveitadasVOs(), false, configuracaoGeralSistema, usuario);
			 if(obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.SEGUNDA_CHAMADA.getValor())) {
				 if(obj.getTipoRequerimento().getDeferirAutomaticamente() && ((!obj.getCid().trim().isEmpty() && !obj.getTipoRequerimento().getCidDeferirAutomaticamente().trim().isEmpty()
						 && Arrays.asList(obj.getTipoRequerimento().getCidDeferirAutomaticamente().split(",")).stream().anyMatch(c -> c.trim().equalsIgnoreCase(obj.getCid().trim()))
						 ) || obj.getTipoRequerimento().getCidDeferirAutomaticamente().trim().isEmpty())
						 ) {
					 obj.getRequerimentoDisciplinaVOs().stream().filter(t -> t.getSituacao().equals(SituacaoRequerimentoDisciplinasAproveitadasEnum.AGUARDANDO_ANALISE)).forEach( t ->
					 {
						 t.setSituacao(SituacaoRequerimentoDisciplinasAproveitadasEnum.DEFERIDO);
						 t.setDataDeferimentoIndeferimento(obj.getDataFinalizacao());
						 t.setUsuarioDeferimentoIndeferimento(usuario);
						 
					 });					 
				 }
				 getFacadeFactory().getRequerimentoDisciplinaInterfaceFacade().persistir(obj, usuario);
			 }	
			alterarSigla(obj);
//			if(obj.getValorTotalFinal() > 0 && obj.getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.AGUARDANDO_RESPOSTA)) {
//				incluirSolicitacaoIsencaoTaxa(obj, configuracaoGeralSistema, usuario);
//			}
			if (obj.getTipoRequerimento().getDeferirAutomaticamente() && obj.getTipoRequerimento().getIsTipoReposicao() && obj.getSituacao().equals(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor())) {
				obj.setNivelMontarDados(NivelMontarDados.TODOS);
//				getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().incluirDisciplinaApartirDoRequerimento(obj, usuario, false, false);
			}
			if (obj.getTipoRequerimento().getDeferirAutomaticamente() && obj.getTipoRequerimento().getIsTipoAproveitamentoDisciplina() && obj.getSituacao().equals(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor())) {
				for (RequerimentoDisciplinasAproveitadasVO rda : obj.getListaRequerimentoDisciplinasAproveitadasVOs()) {
					if(rda.getSituacaoRequerimentoDisciplinasAproveitadasEnum().isAguardandoAnalise()) {
						rda.setMotivoSituacao(obj.getMotivoDeferimento());
						getFacadeFactory().getRequerimentoDisciplinasAproveitadasFacade().realizarConfirmacaoDeferido(rda, usuario);	
					}				
				}
				getFacadeFactory().getAproveitamentoDisciplinaFacade().realizarGeracaoAproveitamentoDisciplinaAutomaticoPorRequerimento(obj, usuario);
			}
			if (obj.getIsDeferido()) {
				realizarTrancamentoAlunoAutomatico(obj,  configuracaoGeralSistema, usuario);
				realizarCancelamentoAlunoAutomatico(obj, usuario);
			}
			 if (obj.getTipoRequerimento().getDeferirAutomaticamente() && obj.getTipoRequerimento().getIsTipoTransferenciaInterna() && obj.getSituacao().equals(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor())) {
				 Boolean apresentarVisaoAluno = false ;
				 if(usuario.getIsApresentarVisaoAluno()  != null &&  usuario.getIsApresentarVisaoAluno()  != null ) {
					  apresentarVisaoAluno = usuario.getIsApresentarVisaoAluno() ; 					 
				 }				
				 getFacadeFactory().getTransferenciaEntradaFacade().realizarCriacaoTranferenciaInternaGerandoNovaMatriculaAproveitandoDisciplinasAprovadasProximoPeriodoLetivoPorRequerimento(obj, configuracaoGeralSistema ,  apresentarVisaoAluno ,usuario);
				
				}
			 getFacadeFactory().getRequerimentoCidTipoRequerimentoIntefaceFacade().incluirRequerimentoCidTipoRequerimentoVOs(obj,  usuario);
			 obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setSituacaoFinanceira("PE");
			obj.setSituacao("PE");
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
//			obj.getContaReceberVO().setCodigo(0);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCodigoArquivo(RequerimentoVO obj, Integer codArquivo, UsuarioVO usuario) throws Exception {
		String sql = "UPDATE Requerimento set arquivo=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sql, new Object[] { codArquivo, obj.getCodigo() });
	}
	
	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSigla(RequerimentoVO obj) throws Exception {
		if (obj.getTipoRequerimento().getSigla() != null && !obj.getTipoRequerimento().getSigla().equals("")) {
			obj.setSigla(obj.getTipoRequerimento().getSigla() + obj.getCodigo());
			String sql = "UPDATE Requerimento set sigla=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getSigla(), obj.getCodigo() });
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gravarNovoArquivo(RequerimentoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		if (!obj.getArquivoVO().getNome().equals("") && !obj.getExcluirArquivo()) {
			if (!obj.getArquivoVO().getCodigo().equals(0)) {
				getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoVO(), usuario, configuracaoGeralSistema);
			} else {
				getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVO(), usuario, configuracaoGeralSistema);
				alterarCodigoArquivo(obj, obj.getArquivoVO().getCodigo(), usuario);
			}
		}
	}

	public void validarDadosValorRequerimento(RequerimentoVO requerimentoVO) {
		// if (requerimentoVO.getTipoDesconto().equals("PO")) {
		// if (requerimentoVO.getValor() == 0.0 ||
		// requerimentoVO.getPercDesconto() == 100) {
		// requerimentoVO.setSituacaoFinanceira(SituacaoRequerimento.ISENTO.getValor());
		// }
		// } else {
		// if ((requerimentoVO.getValor() == 0.0) ||
		// (requerimentoVO.getValor().equals(requerimentoVO.getValorDesconto())))
		// {
		// requerimentoVO.setSituacao(SituacaoRequerimento.ISENTO.getValor());
		// }
		// }
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void registrarRecebimentoDoc(final RequerimentoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		try {
			if (obj.getCodigo().intValue() == 0) {
				incluir(obj, usuario,  configuracaoGeralSistema);
			} else {
				Requerimento.alterar(getIdEntidade());
				final String sql = "UPDATE requerimento SET dataRecebimentoDocRequerido = ?, " + "responsavelRecebimentoDocRequerido = ? WHERE codigo = ?";
				getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement sqlAlterar = con.prepareStatement(sql);
						sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getDataRecebimentoDocRequerido()));
						sqlAlterar.setInt(2, obj.getResponsavelRecebimentoDocRequerido().getCodigo().intValue());
						sqlAlterar.setInt(3, obj.getCodigo().intValue());
						return sqlAlterar;
					}
				});
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>RequerimentoVO</code>. Sempre utiliza a chave primária da classe
	 * como atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>RequerimentoVO</code> que será alterada
	 *            no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final RequerimentoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		try {

			alterar(getIdEntidade(), true, usuario);

			
//			if(obj.getContaReceber() != null ){
//				boolean contaReceberPaga = getFacadeFactory().getContaReceberFacade().consultarSituacaoRecebidaContaReceberTipoOrigemRequerimento(String.valueOf(obj.getCodigo()), obj.getValor(),obj.getContaReceber());
//				if(contaReceberPaga){
//					obj.setSituacaoFinanceira("PG");
//					obj.setSituacao("PE");
//				}
//			}

			validarDadosValorRequerimento(obj);
			RequerimentoVO.validarDados(obj);
			validarDadosBloqueioRequerimentoAbertoSimultaneo(obj, usuario);
			final String sql = "UPDATE Requerimento set data=?, tipoRequerimento=?, valor=?, " + "dataPrevistaFinalizacao=?, dataFinalizacao=?, situacao=?, situacaoFinanceira=?, " + "departamentoResponsavel=?, matricula=?, nomeRequerente=?, cpfRequerente=?, observacao=?, " + "datarecebimentodocrequerido=?, responsavelrecebimentodocrequerido=?, " + "centroReceita=?, contaReceber=?, nrDocumento=?, responsavelEmissaoBoleto=?, " + "dataEmissaoBoleto=?, contaCorrente=?, unidadeEnsino=?, pessoa=?, " + "percDesconto=?, valorDesconto=?, tipoDesconto=?, cep=?, endereco=?, " + "setor=?, numero=?, complemento=?, cidade=?, arquivo=?, responsavel=?, " + "visaoGerado=?, valorAdicional=?, motivoIndeferimento=?, turma=?, " + "disciplina=?, ordemExecucaoTramiteDepartamento=?, funcionario = ?, curso=?, turno=?, numeroVia = ?, dataUltimaAlteracao = ?, motivoDeferimento=?, unidadeEnsinoTransferenciaInterna=?, cursoTransferenciaInterna=?, turnoTransferenciaInterna=?, turmaBase=?, turmaReposicao=?, justificativaTrancamento=?, motivoCancelamentoTrancamento=?, tipoTrabalhoConclusaoCurso=?, tituloMonografia=?, orientadorMonografia=?, grupofacilitador=?, temaTccFacilitador=?, assuntoTccFacilitador=?, avaliadorExternoFacilitador=?, cid=?, codigocid=?, dataafastamentoinicio=?, dataafastamentofim=?  WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getData()));
					sqlAlterar.setInt(2, obj.getTipoRequerimento().getCodigo().intValue());
					sqlAlterar.setDouble(3, obj.getValor().doubleValue());
					sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getDataPrevistaFinalizacao()));
					sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getDataFinalizacao()));
					sqlAlterar.setString(6, obj.getSituacao());
					sqlAlterar.setString(7, obj.getSituacaoFinanceira());
					sqlAlterar.setInt(8, obj.getDepartamentoResponsavel().getCodigo().intValue());
					if(!obj.getMatricula().getMatricula().trim().isEmpty()){
						sqlAlterar.setString(9, obj.getMatricula().getMatricula());
					}else{
						sqlAlterar.setNull(9, 0);
					}
					sqlAlterar.setString(10, obj.getNomeRequerente());
					sqlAlterar.setString(11, obj.getCpfRequerente());
					sqlAlterar.setString(12, obj.getObservacao());
					sqlAlterar.setDate(13, Uteis.getDataJDBC(obj.getDataRecebimentoDocRequerido()));
					sqlAlterar.setInt(14, obj.getResponsavelRecebimentoDocRequerido().getCodigo().intValue());
//					sqlAlterar.setInt(15, obj.getCentroReceita().getCodigo().intValue());
					if (obj.getContaReceber() > 0) {
						sqlAlterar.setInt(16, obj.getContaReceber().intValue());
					} else {
						sqlAlterar.setNull(16, 0);
					}
					sqlAlterar.setString(17, obj.getNrDocumento());
					if (obj.getResponsavelEmissaoBoleto().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(18, obj.getResponsavelEmissaoBoleto().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(18, 0);
					}
					sqlAlterar.setDate(19, Uteis.getDataJDBC(obj.getDataEmissaoBoleto()));
//					if (obj.getContaCorrenteVO().getCodigo().intValue() != 0) {
//						sqlAlterar.setInt(20, obj.getContaCorrenteVO().getCodigo());
//					} else {
//						sqlAlterar.setNull(20, 0);
//					}
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(21, obj.getUnidadeEnsino().getCodigo());
					} else {
						sqlAlterar.setNull(21, 0);
					}
					if (obj.getPessoa().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(22, obj.getPessoa().getCodigo());
					} else {
						sqlAlterar.setNull(22, 0);
					}
					sqlAlterar.setDouble(23, obj.getPercDesconto());
					sqlAlterar.setDouble(24, obj.getValorDesconto());
					sqlAlterar.setString(25, obj.getTipoDesconto());
					sqlAlterar.setString(26, obj.getCEP());
					sqlAlterar.setString(27, obj.getEndereco());
					sqlAlterar.setString(28, obj.getSetor());
					sqlAlterar.setString(29, obj.getNumero());
					sqlAlterar.setString(30, obj.getComplemento());
					if (obj.getCidade().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(31, obj.getCidade().getCodigo());
					} else {
						sqlAlterar.setNull(31, 0);
					}
					if (obj.getArquivoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(32, obj.getArquivoVO().getCodigo());
					} else {
						sqlAlterar.setNull(32, 0);
					}
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(33, obj.getResponsavel().getCodigo());
					} else {
						sqlAlterar.setNull(33, 0);
					}
					sqlAlterar.setString(34, obj.getVisaoGerado());
					sqlAlterar.setDouble(35, obj.getValorAdicional());
					sqlAlterar.setString(36, obj.getMotivoIndeferimento());
					if (obj.getTurma().getCodigo() != 0) {
						sqlAlterar.setInt(37, obj.getTurma().getCodigo());
					} else {
						sqlAlterar.setNull(37, 0);
					}
					if (obj.getDisciplina().getCodigo() != 0) {
						sqlAlterar.setInt(38, obj.getDisciplina().getCodigo());
					} else {
						sqlAlterar.setNull(38, 0);
					}
					sqlAlterar.setInt(39, obj.getOrdemExecucaoTramiteDepartamento().intValue());
					if (obj.getFuncionarioVO().getCodigo() != 0) {
						sqlAlterar.setInt(40, obj.getFuncionarioVO().getCodigo());
					} else {
						sqlAlterar.setNull(40, 0);
					}
					if (obj.getCurso().getCodigo() != 0) {
						sqlAlterar.setInt(41, obj.getCurso().getCodigo());
					} else {
						sqlAlterar.setNull(41, 0);
					}
					if (obj.getTurno().getCodigo() != 0) {
						sqlAlterar.setInt(42, obj.getTurno().getCodigo());
					} else {
						sqlAlterar.setNull(42, 0);
					}
					sqlAlterar.setInt(43, obj.getNumeroVia());
					sqlAlterar.setDate(44, Uteis.getDataJDBC(new Date()));
					sqlAlterar.setString(45, obj.getMotivoDeferimento());
					if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoTransferenciaInternaVO())) {
						sqlAlterar.setInt(46, obj.getUnidadeEnsinoTransferenciaInternaVO().getCodigo());
					} else {
						sqlAlterar.setNull(46, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getCursoTransferenciaInternaVO())) {
						sqlAlterar.setInt(47, obj.getCursoTransferenciaInternaVO().getCodigo());
					} else {
						sqlAlterar.setNull(47, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTurnoTransferenciaInternaVO())) {
						sqlAlterar.setInt(48, obj.getTurnoTransferenciaInternaVO().getCodigo());
					} else {
						sqlAlterar.setNull(48, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTurmaReposicao().getIdentificadorTurmaBase())) {
						sqlAlterar.setString(49, obj.getTurmaReposicao().getIdentificadorTurmaBase());
					} else {
						sqlAlterar.setNull(49, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTurmaReposicao().getCodigo())) {
						sqlAlterar.setInt(50, obj.getTurmaReposicao().getCodigo());
					} else {
						sqlAlterar.setNull(50, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getJustificativaTrancamento())) {
						sqlAlterar.setString(51, obj.getJustificativaTrancamento());
					} else {
						sqlAlterar.setNull(51, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getMotivoCancelamentoTrancamento().getCodigo())) {
						sqlAlterar.setInt(52, obj.getMotivoCancelamentoTrancamento().getCodigo());
					} else {
						sqlAlterar.setNull(52, 0);
					}
					sqlAlterar.setString(53, obj.getTipoTrabalhoConclusaoCurso());
					sqlAlterar.setString(54, obj.getTituloMonografia());
					sqlAlterar.setString(55, obj.getOrientadorMonografia());
					if (obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.CERTIFICADO_PARTICIPACAO_TCC.getValor()) && Uteis.isAtributoPreenchido(obj.getGrupoFacilitador())) {
						sqlAlterar.setInt(56, obj.getGrupoFacilitador().getCodigo());
					} else {
						sqlAlterar.setNull(56, 0);
					}
					if (obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.CERTIFICADO_PARTICIPACAO_TCC.getValor())) {
						sqlAlterar.setString(57, obj.getTemaTccFacilitador().trim());
					} else {
						sqlAlterar.setNull(57, 0);
					}
					if (obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.CERTIFICADO_PARTICIPACAO_TCC.getValor())) {
						sqlAlterar.setString(58, obj.getAssuntoTccFacilitador().trim());
					} else {
						sqlAlterar.setNull(58, 0);
					}
					if (obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.CERTIFICADO_PARTICIPACAO_TCC.getValor())) {
						sqlAlterar.setString(59, obj.getAvaliadorExternoFacilitador().trim());
					} else {
						sqlAlterar.setNull(59, 0);
					}
					sqlAlterar.setString(60, obj.getCid());
					if(Uteis.isAtributoPreenchido(obj.getCidTipoRequerimentoVO().getCodigo())) {
						sqlAlterar.setInt(61, obj.getCidTipoRequerimentoVO().getCodigo());
					} else {
						sqlAlterar.setNull(61, 0);
					}
					if (obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.OUTROS.getValor()) && obj.getTipoRequerimento().getCampoAfastamento()) {
						if (Uteis.isAtributoPreenchido(obj.getDataAfastamentoInicio())) {
							sqlAlterar.setDate(62, Uteis.getDataJDBC(obj.getDataAfastamentoInicio()));
						} else {
							sqlAlterar.setNull(62, 0);
						}
						if (Uteis.isAtributoPreenchido(obj.getDataAfastamentoFim())) {
							sqlAlterar.setDate(63, Uteis.getDataJDBC(obj.getDataAfastamentoFim()));
						} else {
							sqlAlterar.setNull(63, 0);
						}
					} else {
						sqlAlterar.setNull(62, 0);
						sqlAlterar.setNull(63, 0);
					}
					sqlAlterar.setInt(64, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

			if (!obj.getArquivoVO().getNome().equals("")) {
				if (!obj.getArquivoVO().getCodigo().equals(0)) {
					getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoVO(), usuario, configuracaoGeralSistema);
				} else {
					getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVO(), usuario, configuracaoGeralSistema);
					alterarCodigoArquivo(obj, obj.getArquivoVO().getCodigo(), usuario);
				}
			}

			getFacadeFactory().getMaterialRequerimentoFacade().alterarMaterialRequerimentos(obj.getCodigo(), obj.getMaterialRequerimentoVOs(), usuario, configuracaoGeralSistema);
			validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getListaRequerimentoDisciplinasAproveitadasVOs(), "requerimentoDisciplinasAproveitadas", idEntidade, obj.getCodigo(), usuario);
			getFacadeFactory().getRequerimentoDisciplinasAproveitadasFacade().persistir(obj.getListaRequerimentoDisciplinasAproveitadasVOs(), false, configuracaoGeralSistema, usuario);
			if(obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.SEGUNDA_CHAMADA.getValor())) {
				 getFacadeFactory().getRequerimentoDisciplinaInterfaceFacade().persistir(obj, usuario);
			 }
		} catch (Exception e) {
			obj.setNovoObj(Boolean.FALSE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDisciplina(final Integer requerimento, final Integer disciplina, UsuarioVO usuarioVO) throws Exception {
			final String sql = "UPDATE Requerimento set disciplina=?  WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					if (disciplina != null) {
						sqlAlterar.setInt(1, disciplina);
					} else {
						sqlAlterar.setNull(1, 0);
					}
					sqlAlterar.setInt(2, requerimento.intValue());
					
					return sqlAlterar;
				}
			});
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacao(final Integer requerimento, final String situacao, final String motivoIndeferimento, final String motivoDeferimento, RequerimentoVO requerimentoVO ,UsuarioVO usuarioVO) throws Exception {
		if (SituacaoRequerimento.getEnum(situacao).equals(SituacaoRequerimento.FINALIZADO_DEFERIDO) || SituacaoRequerimento.getEnum(situacao).equals(SituacaoRequerimento.FINALIZADO_INDEFERIDO)) {
			final String sql = "UPDATE Requerimento set situacao=?,  dataFinalizacao =?, motivoindeferimento=?, motivoDeferimento=?, tipoTrabalhoConclusaoCurso = ?, tituloMonografia = ?, orientadorMonografia =?, notamonografia = ? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, situacao);
					sqlAlterar.setDate(2, Uteis.getDataJDBC(new Date()));
					sqlAlterar.setString(3, motivoIndeferimento);
					sqlAlterar.setString(4, motivoDeferimento);	
					sqlAlterar.setString(5, requerimentoVO.getTipoTrabalhoConclusaoCurso());
					sqlAlterar.setString(6, requerimentoVO.getTituloMonografia());
					sqlAlterar.setString(7, requerimentoVO.getOrientadorMonografia());
					if(requerimentoVO.getNotaMonografia() == null) {
						sqlAlterar.setNull(8, 0);
					}else {
					sqlAlterar.setDouble(8, requerimentoVO.getNotaMonografia());
					}
					sqlAlterar.setInt(9, requerimento.intValue());
					

					return sqlAlterar;
				}
			});
		} else {
			final String sql = "UPDATE Requerimento set situacao=? WHERE codigo = ?";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {			
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, situacao);
					sqlAlterar.setInt(2, requerimento.intValue());
					return sqlAlterar;					
				}
			});
			realizarInicializacaoDepartamentoEResponsavelRequerimento(requerimento,false,situacao,usuarioVO);
		}
		this.alterarDataUltimaAlteracao(requerimento);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarInicializacaoDepartamentoEResponsavelRequerimento(Integer requerimento, Boolean realizandoRecebimento, String situacao, UsuarioVO usuarioVO) throws Exception {
		if ((situacao.equals("PE") || situacao.equals("EX")) && requerimento != null && requerimento > 0) {
			RequerimentoVO obj = new RequerimentoVO();
			obj.setCodigo(requerimento);
			carregarDados(obj, usuarioVO);
			if (obj.getTipoRequerimento().getTramitaEntreDepartamentos() && obj.getRequerimentoHistoricoVOs().isEmpty()) { // && (obj.getDepartamentoResponsavel().getCodigo() == 0 || obj.getFuncionarioVO().getCodigo() == 0)
				if (obj.getTipoRequerimento().getTipoRequerimentoDepartamentoVOs().isEmpty()) {
					obj.setTipoRequerimento(getFacadeFactory().getTipoRequerimentoFacade().consultarPorChavePrimaria(obj.getTipoRequerimento().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
				}
				TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO = new TipoRequerimentoDepartamentoVO();
				if (obj.getTipoRequerimento().getTipoRequerimentoDepartamentoVOs().size() >= obj.getOrdemExecucaoTramiteDepartamento()) {
					tipoRequerimentoDepartamentoVO = obj.getTipoRequerimento().getTipoRequerimentoDepartamentoVOs().get(obj.getOrdemExecucaoTramiteDepartamento() - 1);
					obj.setDepartamentoResponsavel(tipoRequerimentoDepartamentoVO.getDepartamento());
					obj.setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().realizarDistribuicaoResponsavelRequerimento(obj.getCodigo(), tipoRequerimentoDepartamentoVO, tipoRequerimentoDepartamentoVO.getTipoDistribuicaoResponsavel(), obj.getUnidadeEnsino(), usuarioVO, obj.getTipoRequerimento()));
					obj.getRequerimentoHistoricoVOs().clear();
					obj.gerarNovoRequerimentoHistoricoVODepartamentoAtualTramite(usuarioVO, true, obj.getFuncionarioVO(), tipoRequerimentoDepartamentoVO, false, "");
					if (!obj.getFuncionarioVO().getCodigo().equals(0)) {
						alterarFuncionarioResponsavelEDepartamento(obj.getCodigo(), obj.getFuncionarioVO().getCodigo(), obj.getDepartamentoResponsavel().getCodigo(), usuarioVO);
					}
					getFacadeFactory().getRequerimentoHistoricoFacade().alterarRequerimentoHistoricoVOs(obj, obj.getRequerimentoHistoricoVOs(), usuarioVO);
				}
			} else if (obj.getTipoRequerimento().getTramitaEntreDepartamentos() && !obj.getRequerimentoHistoricoVOs().isEmpty()) {
				iniciarExecucaoRequerimentoNoDepartamento(obj, realizandoRecebimento, usuarioVO);

			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarFuncionarioResponsavel(final Integer requerimento, final Integer funcionario, UsuarioVO usuarioLogado) throws Exception {
		if (funcionario != null && funcionario > 0) {
			final String sql = "UPDATE Requerimento set funcionario = ? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					if (funcionario == null || funcionario == 0) {
						sqlAlterar.setNull(1, 0);
					} else {
						sqlAlterar.setInt(1, funcionario);
					}
					sqlAlterar.setInt(2, requerimento.intValue());
					return sqlAlterar;
				}
			});
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarFuncionarioResponsavelEDepartamento(final Integer requerimento, final Integer funcionario, final Integer departamento, UsuarioVO usuarioLogado) throws Exception {
		try {
			final String sql = "UPDATE Requerimento set funcionario = ?, departamentoResponsavel = ? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setInt(1, funcionario);
					sqlAlterar.setInt(2, departamento);
					sqlAlterar.setInt(3, requerimento.intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void iniciarRequerimento(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) throws Exception {
		String situacaoAnt = requerimentoVO.getSituacao();
		try {
			requerimentoVO.setSituacao(SituacaoRequerimento.EM_EXECUCAO.getValor());
			getFacadeFactory().getRequerimentoFacade().alterarSituacao(requerimentoVO.getCodigo(), SituacaoRequerimento.EM_EXECUCAO.getValor(), "", "", new RequerimentoVO(),  usuarioVO);
			this.alterarDataUltimaAlteracao(requerimentoVO.getCodigo());
		} catch (Exception e) {
			requerimentoVO.setSituacao(situacaoAnt);
			throw e;
		}
	}

	@Override
	public List<SelectItem> consultarDepartamentoAnterioresPermiteRetornar(RequerimentoVO requerimentoVO) throws Exception {
		List<SelectItem> tipoRequerimentoDepartamentoVOs = new ArrayList<SelectItem>(0);
		RequerimentoHistoricoVO reqHistVO = requerimentoVO.consultarUltimoRequerimentoHistoricoDepartamentoAtualVOs();
		if (reqHistVO != null) {
			for (TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO : requerimentoVO.getTipoRequerimento().getTipoRequerimentoDepartamentoVOs()) {
				if (tipoRequerimentoDepartamentoVO.getOrdemExecucao() < reqHistVO.getOrdemExecucaoTramite()) {
					tipoRequerimentoDepartamentoVOs.add(new SelectItem(tipoRequerimentoDepartamentoVO.getCodigo(), tipoRequerimentoDepartamentoVO.getOrdemExecucao() + " - " + tipoRequerimentoDepartamentoVO.getDepartamento().getNome()));
				}
			}
		}
		return tipoRequerimentoDepartamentoVOs;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void enviarRequerimentoDepartamentoAnterior(RequerimentoVO requerimentoVO, Integer tipoRequerimentoDepartamentoVoltar, String motivoRetorno, UsuarioVO usuarioVO) throws Exception {
		FuncionarioVO funcionarioAnt = requerimentoVO.getFuncionarioVO();
		DepartamentoVO departamentoAnt = requerimentoVO.getDepartamentoResponsavel();
		Integer ordemTramite = requerimentoVO.getOrdemExecucaoTramiteDepartamento();
		Boolean adicionouHistorico = false;
		String situacaoAnt = requerimentoVO.getSituacao();
		RequerimentoHistoricoVO reqHistVO = requerimentoVO.consultarUltimoRequerimentoHistoricoDepartamentoAtualVOs();
		TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVoltarVO = null;
		try {
//			if (!getFacadeFactory().getFuncionarioCargoFacade().verificaUsuarioDepartamento(reqHistVO.getDepartamento().getCodigo(), usuarioVO.getCodigo())) {
//				try {
//					verificarPermissaoUsuarioFuncionalidade("Requerimento_permiteRealizarTramiteRequerimentoOutroDepartamento", usuarioVO);
//				} catch (Exception e) {
//					throw new Exception("Somente um funcionário do departamento '" + reqHistVO.getDepartamento().getNome().toUpperCase() + "' que pode realizar esta operação.");
//				}
//			}
			for (TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO : requerimentoVO.getTipoRequerimento().getTipoRequerimentoDepartamentoVOs()) {
				if (tipoRequerimentoDepartamentoVO.getCodigo().equals(tipoRequerimentoDepartamentoVoltar)) {
					tipoRequerimentoDepartamentoVoltarVO = tipoRequerimentoDepartamentoVO;
					break;
				}
			}
			if (tipoRequerimentoDepartamentoVoltarVO == null || tipoRequerimentoDepartamentoVoltarVO.getCodigo().equals(0)) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_Requerimento_departamentoAnterior"));
			}
			if (motivoRetorno.trim().isEmpty()) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_Requerimento_motivoRetorno"));
			}
			if (reqHistVO.getDataInicioExecucaoDepartamento() == null) {
				reqHistVO.setDataInicioExecucaoDepartamento(new Date());
			}
			reqHistVO.setDataConclusaoDepartamento(new Date());
			reqHistVO.setObservacaoDepartamento(motivoRetorno);
			reqHistVO.setEnviouDepartamentoAnterior(true);
			reqHistVO.setDepartamentoAnterior(tipoRequerimentoDepartamentoVoltarVO.getDepartamento());
			reqHistVO.setOrdemExecucaoTramiteAnterior(tipoRequerimentoDepartamentoVoltarVO.getOrdemExecucao());
			getFacadeFactory().getRequerimentoHistoricoFacade().alterar(reqHistVO, usuarioVO);

			requerimentoVO.setOrdemExecucaoTramiteDepartamento(tipoRequerimentoDepartamentoVoltarVO.getOrdemExecucao());
			TipoRequerimentoDepartamentoVO proximoTipoRequerimentoDepartamentoVO = requerimentoVO.getTipoRequerimento().consultarTipoRequerimentoDepartamentoVOs(requerimentoVO.getOrdemExecucaoTramiteDepartamento());
			requerimentoVO.setDepartamentoResponsavel(proximoTipoRequerimentoDepartamentoVO.getDepartamento());
			requerimentoVO.setDepartamentoPodeInserirNota(proximoTipoRequerimentoDepartamentoVO.getPodeInserirNota());
			requerimentoVO.setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().realizarDistribuicaoResponsavelRequerimento(requerimentoVO.getCodigo(), proximoTipoRequerimentoDepartamentoVO, proximoTipoRequerimentoDepartamentoVO.getTipoDistribuicaoResponsavel(), requerimentoVO.getUnidadeEnsino(), usuarioVO, requerimentoVO.getTipoRequerimento()));
			alterarFuncionarioResponsavel(requerimentoVO.getCodigo(), requerimentoVO.getFuncionarioVO().getCodigo(), usuarioVO);
			alterarOrdemExecucaoTramiteDepartamento(requerimentoVO.getCodigo(), requerimentoVO.getOrdemExecucaoTramiteDepartamento(), requerimentoVO.getDepartamentoResponsavel().getCodigo());
			requerimentoVO.getSituacaoRequerimentoDepartamentoVO().setCodigo(0);
			alterarSituacaoDepartamento(requerimentoVO.getCodigo(), null , usuarioVO);
			requerimentoVO.gerarNovoRequerimentoHistoricoVODepartamentoAtualTramite(usuarioVO, false, requerimentoVO.getFuncionarioVO(), proximoTipoRequerimentoDepartamentoVO, true, motivoRetorno);
			adicionouHistorico = true;
			getFacadeFactory().getRequerimentoHistoricoFacade().alterarRequerimentoHistoricoVOs(requerimentoVO, requerimentoVO.getRequerimentoHistoricoVOs(), usuarioVO);
			this.alterarDataUltimaAlteracao(requerimentoVO.getCodigo());
		} catch (Exception e) {
			reqHistVO.setDataConclusaoDepartamento(null);
			reqHistVO.setObservacaoDepartamento("");
			reqHistVO.setEnviouDepartamentoAnterior(false);
			reqHistVO.setDepartamentoAnterior(null);
			reqHistVO.setOrdemExecucaoTramiteAnterior(0);
			requerimentoVO.setOrdemExecucaoTramiteDepartamento(reqHistVO.getOrdemExecucaoTramite());
			requerimentoVO.setFuncionarioVO(funcionarioAnt);
			requerimentoVO.setDepartamentoResponsavel(departamentoAnt);
			requerimentoVO.setOrdemExecucaoTramiteDepartamento(ordemTramite);
			requerimentoVO.setSituacao(situacaoAnt);
			if (adicionouHistorico) {
				requerimentoVO.getRequerimentoHistoricoVOs().remove(requerimentoVO.getRequerimentoHistoricoVOs().size() - 1);
			}
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void enviarRequerimentoProximoDepartamento(RequerimentoVO requerimentoVO, RequerimentoHistoricoVO requerimentoHistoricoAtualVO, Boolean usarFuncionarioProximoTramite, Boolean usarCoordenadorEspecificoTramite,   FuncionarioVO funcionarioProximoTramite, UsuarioVO usuarioVO) throws Exception {
		FuncionarioVO funcionarioAnt = requerimentoVO.getFuncionarioVO();
		DepartamentoVO departamentoAnt = requerimentoVO.getDepartamentoResponsavel();
		Integer ordemTramite = requerimentoVO.getOrdemExecucaoTramiteDepartamento();
		Boolean adicionouHistorico = false;
		String situacaoAnt = requerimentoVO.getSituacao();
		try {
			TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVOAtual = requerimentoVO.getTipoRequerimento().consultarTipoRequerimentoDepartamentoVOs(requerimentoVO.getOrdemExecucaoTramiteDepartamento());
			requerimentoHistoricoAtualVO.setPodeInserirNota(tipoRequerimentoDepartamentoVOAtual.getPodeInserirNota());
//			if (!requerimentoVO.getFuncionarioVO().getPessoa().getCodigo().equals(usuarioVO.getPessoa().getCodigo()) && !getFacadeFactory().getFuncionarioCargoFacade().verificaUsuarioDepartamento(tipoRequerimentoDepartamentoVOAtual.getDepartamento().getCodigo(), usuarioVO.getCodigo())) {
//				try {
//					verificarPermissaoUsuarioFuncionalidade("Requerimento_permiteRealizarTramiteRequerimentoOutroDepartamento", usuarioVO);
//				} catch (Exception e) {
//					throw new Exception("Somente um funcionário do departamento '" + tipoRequerimentoDepartamentoVOAtual.getDepartamento().getNome().toUpperCase() + "' que pode realizar esta operação.");
//				}
//			}
			if ((requerimentoHistoricoAtualVO.getObservacaoDepartamento().equals("")) && (tipoRequerimentoDepartamentoVOAtual.getObservacaoObrigatoria())) {
				throw new Exception("Para enviar para o próximo Dpto é necessário que informar uma observação.");
			}
			if(usarFuncionarioProximoTramite && usarCoordenadorEspecificoTramite && !Uteis.isAtributoPreenchido(funcionarioProximoTramite.getPessoa())){
				throw new Exception("O COORDENADOR CURSO do próximo trâmite deve ser informado.");
			}
			if (usarFuncionarioProximoTramite && !usarCoordenadorEspecificoTramite && !Uteis.isAtributoPreenchido(funcionarioProximoTramite)) {
				throw new Exception("O FUNCIONÁRIO do próximo trâmite deve ser informado.");
			}
			if (tipoRequerimentoDepartamentoVOAtual.getPodeInserirNota() && !Uteis.isAtributoPreenchido(requerimentoHistoricoAtualVO.getNotaTCC())) {
				throw new Exception("A Nota deve ser informada.");
			}
			
			requerimentoHistoricoAtualVO.setDataConclusaoDepartamento(new Date());
			if (requerimentoHistoricoAtualVO.getDataInicioExecucaoDepartamento() == null) {
				requerimentoHistoricoAtualVO.setDataInicioExecucaoDepartamento(new Date());
			}
			if (usuarioVO.getPessoa().getCodigo().intValue() > 0 && !requerimentoHistoricoAtualVO.getResponsavelRequerimentoDepartamento().getCodigo().equals(usuarioVO.getPessoa().getCodigo().intValue())) {
				requerimentoHistoricoAtualVO.getResponsavelRequerimentoDepartamento().setCodigo(usuarioVO.getPessoa().getCodigo());
				requerimentoHistoricoAtualVO.getResponsavelRequerimentoDepartamento().setNome(usuarioVO.getPessoa().getNome());
			}
			
			
			if (requerimentoHistoricoAtualVO.getCodigo() != 0) {
				getFacadeFactory().getRequerimentoHistoricoFacade().alterar(requerimentoHistoricoAtualVO, usuarioVO);
			}
			requerimentoVO.setOrdemExecucaoTramiteDepartamento(requerimentoVO.getOrdemExecucaoTramiteDepartamento() + 1);
			TipoRequerimentoDepartamentoVO proximoTipoRequerimentoDepartamentoVO = requerimentoVO.getTipoRequerimento().consultarTipoRequerimentoDepartamentoVOs(requerimentoVO.getOrdemExecucaoTramiteDepartamento());
			requerimentoVO.setDepartamentoResponsavel(proximoTipoRequerimentoDepartamentoVO.getDepartamento());
			requerimentoVO.setDepartamentoPodeInserirNota(proximoTipoRequerimentoDepartamentoVO.getPodeInserirNota());
			if (!usarFuncionarioProximoTramite) {
				requerimentoVO.setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().realizarDistribuicaoResponsavelRequerimento(requerimentoVO.getCodigo(), proximoTipoRequerimentoDepartamentoVO, proximoTipoRequerimentoDepartamentoVO.getTipoDistribuicaoResponsavel(), requerimentoVO.getUnidadeEnsino(), usuarioVO, requerimentoVO.getTipoRequerimento()));
			} else {
				if(usarCoordenadorEspecificoTramite){
					funcionarioProximoTramite = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(funcionarioProximoTramite.getPessoa().getCodigo(), false, usuarioVO);
				}
				requerimentoVO.setFuncionarioVO(funcionarioProximoTramite);
			}
			validarNotaMaxima(tipoRequerimentoDepartamentoVOAtual, requerimentoHistoricoAtualVO.getNotaTCC());
			alterarFuncionarioResponsavel(requerimentoVO.getCodigo(), requerimentoVO.getFuncionarioVO().getCodigo(), usuarioVO);
			alterarOrdemExecucaoTramiteDepartamento(requerimentoVO.getCodigo(), requerimentoVO.getOrdemExecucaoTramiteDepartamento(), requerimentoVO.getDepartamentoResponsavel().getCodigo());
			requerimentoVO.getSituacaoRequerimentoDepartamentoVO().setCodigo(0);
			alterarSituacaoDepartamento(requerimentoVO.getCodigo(), null, usuarioVO);
			requerimentoVO.gerarNovoRequerimentoHistoricoVODepartamentoAtualTramite(usuarioVO, false, requerimentoVO.getFuncionarioVO(), proximoTipoRequerimentoDepartamentoVO, false, "");
			adicionouHistorico = true;
			getFacadeFactory().getRequerimentoHistoricoFacade().alterarRequerimentoHistoricoVOs(requerimentoVO, requerimentoVO.getRequerimentoHistoricoVOs(), usuarioVO);
			this.alterarDataUltimaAlteracao(requerimentoVO.getCodigo());
		} catch (Exception e) {
			requerimentoVO.setFuncionarioVO(funcionarioAnt);
			requerimentoVO.setDepartamentoResponsavel(departamentoAnt);
			requerimentoVO.setOrdemExecucaoTramiteDepartamento(ordemTramite);
			requerimentoVO.setSituacao(situacaoAnt);
			if (adicionouHistorico) {
				requerimentoVO.getRequerimentoHistoricoVOs().remove(requerimentoVO.getRequerimentoHistoricoVOs().size() - 1);
			}
			throw e;
		}
	}

	private void validarNotaMaxima(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVOAtual, Double notaTCCInformada) throws Exception {
		if(tipoRequerimentoDepartamentoVOAtual.getPodeInserirNota() && notaTCCInformada.compareTo(tipoRequerimentoDepartamentoVOAtual.getNotaMaxima()) > BigDecimal.ZERO.intValue()) {
			throw new Exception("A nota Informada é maior que a nota máxima Permitida.");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void iniciarExecucaoRequerimentoNoDepartamento(RequerimentoVO requerimentoVO, Boolean realizandoRecebimento, UsuarioVO usuarioVO) throws Exception {
		RequerimentoHistoricoVO reqHistVO = requerimentoVO.consultarUltimoRequerimentoHistoricoDepartamentoAtualVOs();
		if (reqHistVO == null) {
			reqHistVO = new RequerimentoHistoricoVO();
			reqHistVO.setRequerimento(requerimentoVO.getCodigo());
			reqHistVO.setDepartamento(requerimentoVO.getDepartamentoResponsavel());
			reqHistVO.setDataEntradaDepartamento(new Date());
			reqHistVO.setOrdemExecucaoTramite(1);
		}
		if (usuarioVO.getPessoa().getCodigo().intValue() > 0 && !realizandoRecebimento) {
			if (!requerimentoVO.getFuncionarioVO().getPessoa().getCodigo().equals(usuarioVO.getPessoa().getCodigo())) {
//				if (!getFacadeFactory().getFuncionarioCargoFacade().verificaUsuarioDepartamento(reqHistVO.getDepartamento().getCodigo(), usuarioVO.getCodigo())) {
//					try {
//						verificarPermissaoUsuarioFuncionalidade("Requerimento_permiteRealizarTramiteRequerimentoOutroDepartamento", usuarioVO);
//					} catch (Exception e) {
//						throw new Exception("Somente um funcionário do departamento '" + reqHistVO.getDepartamento().getNome().toUpperCase() + "' que pode realizar esta operação.");
//					}
//				}
			}
			reqHistVO.getResponsavelRequerimentoDepartamento().setCodigo(usuarioVO.getPessoa().getCodigo());
			reqHistVO.getResponsavelRequerimentoDepartamento().setNome(usuarioVO.getPessoa().getNome());
			if (!requerimentoVO.getFuncionarioVO().getPessoa().getCodigo().equals(usuarioVO.getPessoa().getCodigo())) {
				FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(usuarioVO.getPessoa().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
				requerimentoVO.setFuncionarioVO(funcionarioVO);
				alterarFuncionarioResponsavel(requerimentoVO.getCodigo(), funcionarioVO.getCodigo(), usuarioVO);
			}
		}
		reqHistVO.setDataInicioExecucaoDepartamento(new Date());
		requerimentoVO.adicionarRequerimentoHistoricoVOs(reqHistVO);
		getFacadeFactory().getRequerimentoHistoricoFacade().alterar(reqHistVO, usuarioVO);
		this.alterarDataUltimaAlteracao(requerimentoVO.getCodigo());
	}

	@Override
	public RequerimentoHistoricoVO realizarVerificacaoRequerimentoHistoricoAtualPossueQuestionarioResponder(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) throws Exception {
		RequerimentoHistoricoVO reqHistVO = requerimentoVO.consultarUltimoRequerimentoHistoricoDepartamentoAtualVOs();
		if (reqHistVO != null && reqHistVO.getQuestionario().getCodigo() > 0) {
			reqHistVO.setGravarRespostaQuestionario(false);
			if (reqHistVO.getQuestionario().getPerguntaQuestionarioVOs().isEmpty()) {
				reqHistVO.setQuestionario(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(reqHistVO.getQuestionario().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
				reqHistVO.setQuestionarioJaRespondido(getFacadeFactory().getQuestionarioFacade().executarRestauracaoRespostaQuestionarioPorRequerimentoHistorico(reqHistVO.getRequerimento(), reqHistVO.getDepartamento().getCodigo(), reqHistVO.getOrdemExecucaoTramite(), reqHistVO.getQuestionario()));
			}
		}
		return reqHistVO;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deferirRequerimento(RequerimentoVO requerimentoVO, RequerimentoHistoricoVO requerimentoHistoricoAtualVO, UsuarioVO usuarioVO) throws Exception {
		verificarPermissaoFuncionalidadeUsuario("Requerimento_PermitirDeferir", usuarioVO);
		
		if(requerimentoVO.getTipoRequerimento().getTipo().equals("TC") && requerimentoVO.getTituloMonografia().isEmpty()) {
			throw new Exception("O Título da Monografia é Obrigatório.");
		}if(requerimentoVO.getTipoRequerimento().getTipo().equals("TC") && requerimentoVO.getNotaMonografia().equals(BigDecimal.ZERO.doubleValue())) {
			throw new Exception("A Nota Final da Monografia é Obrigatório.");
		}
		Uteis.checkState(requerimentoVO.getTipoRequerimento().getIsTipoAproveitamentoDisciplina()
				&& requerimentoVO.getListaRequerimentoDisciplinasAproveitadasVOs().stream().allMatch(p-> p.getSituacaoRequerimentoDisciplinasAproveitadasEnum().isIndeferido()),
				"Para Autorizar um requerimento do tipo de Aproveitamento de Disciplina é necessário que exista pelo menos um aproveitamento deferido.");
		
		Map<Integer, List<TurmaVO>> mapTurmas = new HashMap<Integer, List<TurmaVO>>();
		List<TurmaVO> turmaVOs = new ArrayList<TurmaVO>();
		if (Uteis.isAtributoPreenchido(requerimentoVO.getTurmaReposicao().getCodigo())) {
			getFacadeFactory().getTurmaFacade().carregarDados(requerimentoVO.getTurmaReposicao(), usuarioVO);
			if (requerimentoVO.getTurmaReposicao().getTurmaAgrupada()) {
				definirTurmaBaseRequerimentosTurmaAgrupada(requerimentoVO, turmaVOs, usuarioVO, mapTurmas, requerimentoVO.getTurmaReposicao());
				alterar(requerimentoVO, usuarioVO, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuarioVO));
			}
		}
		
		if(requerimentoVO.getAguardandoAutorizacaoPagamento() && requerimentoVO.getTipoRequerimento().getPermiteDeferirAguardandoAutorizacaoPagamento()) {
			getFacadeFactory().getRequerimentoFacade().realizarDefinicaoVencimentoContaReceberRequerimento(requerimentoVO, true);
			realizarAutorizacaoPagamentoRequerimento(requerimentoVO,  usuarioVO);
		}
		RequerimentoHistoricoVO reqHistVO = requerimentoVO.consultarUltimoRequerimentoHistoricoDepartamentoAtualVOs();
		if (reqHistVO != null) {
			if (usuarioVO.getPessoa().getCodigo().intValue() > 0 && !requerimentoVO.getFuncionarioVO().getPessoa().getCodigo().equals(usuarioVO.getPessoa().getCodigo())) {
//				if (!getFacadeFactory().getFuncionarioCargoFacade().verificaUsuarioDepartamento(reqHistVO.getDepartamento().getCodigo(), usuarioVO.getCodigo())) {
//					try {
//						verificarPermissaoUsuarioFuncionalidade("Requerimento_permiteRealizarTramiteRequerimentoOutroDepartamento", usuarioVO);
//					} catch (Exception e) {
//						throw new Exception("Somente um funcionário do departamento '" + reqHistVO.getDepartamento().getNome().toUpperCase() + "' que pode realizar esta operação.");
//					}
//
//				}
			}
			if (reqHistVO.getObservacaoDepartamento().equals("")) {
				reqHistVO.setObservacaoDepartamento("Requerimento Deferido.");
			}
			if (reqHistVO.getDataInicioExecucaoDepartamento() == null) {
				reqHistVO.setDataInicioExecucaoDepartamento(new Date());
			}
			reqHistVO.setDataConclusaoDepartamento(new Date());
			reqHistVO.setDptoResposanvelPeloIndeferimento(Boolean.FALSE);
			reqHistVO.setNotaTCC(requerimentoHistoricoAtualVO.getNotaTCC());
			if (usuarioVO.getPessoa().getCodigo().intValue() > 0 && (reqHistVO.getResponsavelRequerimentoDepartamento().getCodigo().equals(usuarioVO.getPessoa().getCodigo()) || !requerimentoVO.getFuncionarioVO().getPessoa().getCodigo().equals(usuarioVO.getPessoa().getCodigo()))) {
				reqHistVO.getResponsavelRequerimentoDepartamento().setCodigo(usuarioVO.getPessoa().getCodigo());
				reqHistVO.getResponsavelRequerimentoDepartamento().setNome(usuarioVO.getPessoa().getNome());
				if (!requerimentoVO.getFuncionarioVO().getPessoa().getCodigo().equals(usuarioVO.getPessoa().getCodigo())) {
					FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(usuarioVO.getPessoa().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
					requerimentoVO.setFuncionarioVO(funcionarioVO);
					alterarFuncionarioResponsavel(requerimentoVO.getCodigo(), funcionarioVO.getCodigo(), usuarioVO);
				}
			}
			requerimentoVO.adicionarRequerimentoHistoricoVOs(reqHistVO);
			getFacadeFactory().getRequerimentoHistoricoFacade().alterarRequerimentoHistoricoVOs(requerimentoVO, requerimentoVO.getRequerimentoHistoricoVOs(), usuarioVO);
			alterarSituacaoDepartamento(requerimentoVO.getCodigo(), reqHistVO.getSituacaoRequerimentoDepartamentoVO().getCodigo(), usuarioVO);
		}
		 
		requerimentoVO.setSituacao(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor());
		requerimentoVO.setDataFinalizacao(new Date());
		realizarTrancamentoAlunoAutomatico(requerimentoVO,  getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuarioVO), usuarioVO);
		realizarCancelamentoAlunoAutomatico(requerimentoVO, usuarioVO);
		alterarSituacao(requerimentoVO.getCodigo(), SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor(), requerimentoVO.getMotivoIndeferimento(), requerimentoVO.getMotivoDeferimento(),requerimentoVO, usuarioVO);
		if(requerimentoVO.getTipoRequerimento().getIsTipoAproveitamentoDisciplina()) {
			for (RequerimentoDisciplinasAproveitadasVO rda : requerimentoVO.getListaRequerimentoDisciplinasAproveitadasVOs()) {
				if(rda.getSituacaoRequerimentoDisciplinasAproveitadasEnum().isAguardandoAnalise()) {
					rda.setMotivoSituacao(requerimentoVO.getMotivoDeferimento());
					getFacadeFactory().getRequerimentoDisciplinasAproveitadasFacade().realizarConfirmacaoDeferido(rda, usuarioVO);	
				}
			}
			getFacadeFactory().getAproveitamentoDisciplinaFacade().realizarGeracaoAproveitamentoDisciplinaAutomaticoPorRequerimento(requerimentoVO, usuarioVO);
		}
		realizarDeferimentoRequerimentoTCC(requerimentoVO, usuarioVO);
		 if ( requerimentoVO.getTipoRequerimento().getIsTipoTransferenciaInterna() ) {
		    	getFacadeFactory().getTransferenciaEntradaFacade().realizarCriacaoTranferenciaInternaGerandoNovaMatriculaAproveitandoDisciplinasAprovadasProximoPeriodoLetivoPorRequerimento(requerimentoVO, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuarioVO) ,  false , usuarioVO);
			
		}
		 if(requerimentoVO.getTipoRequerimento().getTipo().equals(TiposRequerimento.SEGUNDA_CHAMADA.getValor())) {
			 requerimentoVO.getRequerimentoDisciplinaVOs().stream().filter(t -> t.getSituacao().equals(SituacaoRequerimentoDisciplinasAproveitadasEnum.AGUARDANDO_ANALISE)).forEach( t ->
					 {
							t.setSituacao(SituacaoRequerimentoDisciplinasAproveitadasEnum.DEFERIDO);
							t.setDataDeferimentoIndeferimento(requerimentoVO.getDataFinalizacao());
							t.setUsuarioDeferimentoIndeferimento(usuarioVO);
							
							});
			 getFacadeFactory().getRequerimentoDisciplinaInterfaceFacade().persistir(requerimentoVO, usuarioVO);
		 }
		 
		 if (requerimentoVO.getTipoRequerimento().getTipo().equals(TiposRequerimento.CONTRATO_ESTAGIO_NAO_OBRIGATORIO.getValor())) {
			 getFacadeFactory().getEstagioFacade().realizarVisualizacaoTermoEstagioNaoObrigatorio(requerimentoVO, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuarioVO), usuarioVO);
		 }
	    
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void indeferirRequerimento(RequerimentoVO requerimentoVO, boolean validarAcesso, boolean indeferimentoAutomatico, UsuarioVO usuarioVO) throws Exception {
		if (validarAcesso) {
			verificarPermissaoFuncionalidadeUsuario("Requerimento_PermitirIndeferir", usuarioVO);
		}
		if(!indeferimentoAutomatico && requerimentoVO.getTipoRequerimento().getTipo().equals("TC") && requerimentoVO.getTituloMonografia().isEmpty()) {
			throw new Exception("O Título da Monografia é Obrigatório.");
		}if(!indeferimentoAutomatico &&  requerimentoVO.getTipoRequerimento().getTipo().equals("TC") && requerimentoVO.getNotaMonografia().equals(BigDecimal.ZERO.doubleValue())) {
			throw new Exception("A Nota Final da Monografia é Obrigatório.");
		}		
		RequerimentoHistoricoVO reqHistVO = requerimentoVO.consultarUltimoRequerimentoHistoricoDepartamentoAtualVOs();
		if (reqHistVO != null && Uteis.isAtributoPreenchido(reqHistVO.getDepartamento())) {
			if (!indeferimentoAutomatico && usuarioVO.getPessoa().getCodigo().intValue() > 0 && !requerimentoVO.getFuncionarioVO().getPessoa().getCodigo().equals(usuarioVO.getPessoa().getCodigo())) {
//				if (!getFacadeFactory().getFuncionarioCargoFacade().verificaUsuarioDepartamento(reqHistVO.getDepartamento().getCodigo(), usuarioVO.getCodigo())) {
//					try {
//						verificarPermissaoUsuarioFuncionalidade("Requerimento_permiteRealizarTramiteRequerimentoOutroDepartamento", usuarioVO);
//					} catch (Exception e) {
//						throw new Exception("Somente um funcionário do departamento '" + reqHistVO.getDepartamento().getNome().toUpperCase() + "' que pode realizar esta operação.");
//					}
//
//				}
			}
			if (reqHistVO.getObservacaoDepartamento().equals("")) {
				reqHistVO.setObservacaoDepartamento(requerimentoVO.getMotivoIndeferimento());
			} else {
				reqHistVO.setObservacaoDepartamento(reqHistVO.getObservacaoDepartamento() + " MOTIVO INDEFERIMENTO: " + requerimentoVO.getMotivoIndeferimento());
			}
			if (reqHistVO.getDataInicioExecucaoDepartamento() == null) {
				reqHistVO.setDataInicioExecucaoDepartamento(new Date());
			}
			reqHistVO.setDataConclusaoDepartamento(new Date());
			reqHistVO.setDptoResposanvelPeloIndeferimento(Boolean.TRUE);
			if (!indeferimentoAutomatico && usuarioVO.getPessoa().getCodigo().intValue() > 0 && (reqHistVO.getResponsavelRequerimentoDepartamento().getCodigo().equals(usuarioVO.getPessoa().getCodigo()) || !requerimentoVO.getFuncionarioVO().getPessoa().getCodigo().equals(usuarioVO.getPessoa().getCodigo()))) {
				reqHistVO.getResponsavelRequerimentoDepartamento().setCodigo(usuarioVO.getPessoa().getCodigo());
				reqHistVO.getResponsavelRequerimentoDepartamento().setNome(usuarioVO.getPessoa().getNome());
				if (!requerimentoVO.getFuncionarioVO().getPessoa().getCodigo().equals(usuarioVO.getPessoa().getCodigo())) {
					FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(usuarioVO.getPessoa().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
					requerimentoVO.setFuncionarioVO(funcionarioVO);
					alterarFuncionarioResponsavel(requerimentoVO.getCodigo(), funcionarioVO.getCodigo(), usuarioVO);
				}
			}
			reqHistVO.setRequerimentoVO(requerimentoVO);
			requerimentoVO.adicionarRequerimentoHistoricoVOs(reqHistVO);
			getFacadeFactory().getRequerimentoHistoricoFacade().alterarRequerimentoHistoricoVOs(requerimentoVO, requerimentoVO.getRequerimentoHistoricoVOs(), usuarioVO);
			alterarSituacaoDepartamento(requerimentoVO.getCodigo(), reqHistVO.getSituacaoRequerimentoDepartamentoVO().getCodigo(), usuarioVO);
		}
		requerimentoVO.setSituacao(SituacaoRequerimento.FINALIZADO_INDEFERIDO.getValor());
		requerimentoVO.setDataFinalizacao(new Date());
		alterarSituacao(requerimentoVO.getCodigo(), SituacaoRequerimento.FINALIZADO_INDEFERIDO.getValor(), requerimentoVO.getMotivoIndeferimento(), requerimentoVO.getMotivoDeferimento(),requerimentoVO, usuarioVO);
		if(requerimentoVO.getTipoRequerimento().getIsTipoAproveitamentoDisciplina()) {
			for (RequerimentoDisciplinasAproveitadasVO rda : requerimentoVO.getListaRequerimentoDisciplinasAproveitadasVOs()) {
				if(!rda.getSituacaoRequerimentoDisciplinasAproveitadasEnum().isIndeferido()) {
					rda.setMotivoSituacao(requerimentoVO.getMotivoIndeferimento());
					getFacadeFactory().getRequerimentoDisciplinasAproveitadasFacade().realizarConfirmacaoIndeferido(rda, usuarioVO);	
				}				
			}
//			AproveitamentoDisciplinaVO ad = getFacadeFactory().getAproveitamentoDisciplinaFacade().consultarPorCodigoRequerimento(requerimentoVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getAplicacaoControle().getConfiguracaoFinanceiroVO(0), usuarioVO);
//			if(Uteis.isAtributoPreenchido(ad)) {
//				getFacadeFactory().getAproveitamentoDisciplinaFacade().excluir(ad, usuarioVO);
//			}
		}
//		if (Uteis.isAtributoPreenchido(requerimentoVO.getContaReceberVO()) && requerimentoVO.getContaReceberVO().getSituacaoAReceber()) {
//			requerimentoVO.getContaReceberVO().setMotivoCancelamento(requerimentoVO.getMotivoIndeferimento());
//			getFacadeFactory().getContaReceberFacade().cancelarContaReceber(requerimentoVO.getContaReceberVO(), usuarioVO);
//			alterarSituacaoFinanceira(requerimentoVO.getCodigo(), "CA", usuarioVO);
//		}
		if(requerimentoVO.getTipoRequerimento().getTipo().equals(TiposRequerimento.SEGUNDA_CHAMADA.getValor())) {
			requerimentoVO.getRequerimentoDisciplinaVOs().forEach(t -> {
				t.setSituacao(SituacaoRequerimentoDisciplinasAproveitadasEnum.INDEFERIDO);
				t.setDataDeferimentoIndeferimento(requerimentoVO.getDataFinalizacao());
				t.setUsuarioDeferimentoIndeferimento(usuarioVO);
				t.setMotivoIndeferimento(requerimentoVO.getMotivoIndeferimento());
				});
			 getFacadeFactory().getRequerimentoDisciplinaInterfaceFacade().persistir(requerimentoVO, usuarioVO);
		 }
	}

	private MatriculaVO montarDadosMatricula(RequerimentoVO requerimentoVO) {
		MatriculaVO matricula = new MatriculaVO();
		matricula.setTipoTrabalhoConclusaoCurso(requerimentoVO.getTipoTrabalhoConclusaoCurso());
		matricula.setNotaMonografia(requerimentoVO.getNotaMonografia());
		matricula.setTituloMonografia(requerimentoVO.getTituloMonografia());
		matricula.setOrientadorMonografia(requerimentoVO.getOrientadorMonografia());
		matricula.setMatricula(requerimentoVO.getMatricula().getMatricula());
		return matricula;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarOrdemExecucaoTramiteDepartamento(final Integer requerimento, final Integer ordem, final Integer codigoDpto) throws Exception {
		final String sql = "UPDATE Requerimento set ordemExecucaoTramiteDepartamento=?, departamentoResponsavel=? WHERE codigo = ?";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setInt(1, ordem);
				sqlAlterar.setInt(2, codigoDpto);
				sqlAlterar.setInt(3, requerimento.intValue());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoFinanceiraESituacaoExecucao(final Integer requerimento, final Boolean realizandoRecebimento, final String situacao, final String situacaoFinanceira, boolean realizandoBaixaAutomatica, UsuarioVO usuarioVO) throws Exception {
		RequerimentoVO requerimentoVO = new RequerimentoVO();
		requerimentoVO.setCodigo(requerimento);
		getFacadeFactory().getRequerimentoFacade().carregarDados(requerimentoVO, usuarioVO);
		if (Uteis.isAtributoPreenchido(requerimentoVO) && requerimentoVO.getSituacao().equals(SituacaoRequerimento.AGUARDANDO_PAGAMENTO.getValor()) && !situacao.equals("FI")) {
			requerimentoVO.setNivelMontarDados(NivelMontarDados.TODOS);
		if (((!requerimentoVO.getTipoRequerimento().getIsEmissaoCertificado() && !requerimentoVO.getTipoRequerimento().getIsDeclaracao() 
				&&  consultarDeferirAutomaticamentePorCodigoRequerimento(requerimento)) 
				|| ((requerimentoVO.getTipoRequerimento().getIsEmissaoCertificado() || requerimentoVO.getTipoRequerimento().getIsDeclaracao()) 
				&& (requerimentoVO.getFormatoCertificadoSelecionado().equals("DIGITAL") &&  consultarDeferirAutomaticamentePorCodigoRequerimento(requerimento)) 
				|| (requerimentoVO.getFormatoCertificadoSelecionado().equals("IMPRESSO") &&  consultarDeferirAutomaticamenteDocumentoImpressoPorCodigoRequerimento(requerimento))))) {
				/// tipo requerimento é de reposição ? Como ele é automatico, deve ealizar a
				/// inclusao da disciplina pro aluno. Caso ocorra algum erro, deve alterar a
				/// situacao para pago e jogar para o tramite para que alguem gerencie a
				/// situação
			/// 
//			if (consultarTipoRequerimentoReposicaoPorCodigoRequerimento(requerimento)) {				
//				getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().incluirDisciplinaApartirDoRequerimento(requerimentoVO, usuarioVO, realizandoRecebimento, realizandoBaixaAutomatica);
//			}
			
			final String sql = "UPDATE Requerimento set situacao=?, situacaoFinanceira=?,datafinalizacao=? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor());
					sqlAlterar.setString(2, situacaoFinanceira);
					sqlAlterar.setDate(3, Uteis.getDataJDBC(new Date()));
					sqlAlterar.setInt(4, requerimento.intValue());
					return sqlAlterar;
				}
			});
		} else {
			final String sql = "UPDATE Requerimento set situacao=?, situacaoFinanceira=? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
				getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, situacao);
					sqlAlterar.setString(2, situacaoFinanceira);
					sqlAlterar.setInt(3, requerimento.intValue());
					return sqlAlterar;
				}
				});
				if ((situacao.equals("PE") || situacao.equals("EX")) && (situacaoFinanceira.equals("IS") || situacaoFinanceira.equals("PG"))) {
				realizarInicializacaoDepartamentoEResponsavelRequerimento(requerimento, realizandoRecebimento, situacao, usuarioVO);
				}
			}
		} else if (Uteis.isAtributoPreenchido(requerimentoVO) && (requerimentoVO.getSituacaoFinanceira().equals("PE") || requerimentoVO.getSituacaoFinanceira().equals("CA") || requerimentoVO.getSituacaoFinanceira().equals("AP") || (situacaoFinanceira.equals("PE") && requerimentoVO.getSituacaoFinanceira().equals("PG")))) {
			alterarSituacaoFinanceira(requerimento, situacaoFinanceira, usuarioVO);
		}
	}


//	public void emitirBoletoParcela(RequerimentoVO requerimentoVO, UsuarioVO usuario configuracaoFinanceiro) throws Exception {
//		// ConfiguracaoFinanceiroVO configuracaoFinanceiroVO =
//		// getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS,usuario,
//		// null);
//		ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = configuracaoFinanceiro;
//		requerimentoVO.setTipoRequerimento(getFacadeFactory().getTipoRequerimentoFacade().consultarPorChavePrimaria(requerimentoVO.getTipoRequerimento().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
//		CentroReceitaVO centroReceita = null;
//		if (requerimentoVO.getTipoRequerimento().getCentroReceitaRequerimentoPadrao().getCodigo().intValue() > 0) {
//			centroReceita = requerimentoVO.getTipoRequerimento().getCentroReceitaRequerimentoPadrao();
//		} else {
//			centroReceita = configuracaoFinanceiroVO.getCentroReceitaRequerimentoPadrao();
//		}
//
//		ContaCorrenteVO contaCorrente = new ContaCorrenteVO();
//		requerimentoVO.setUnidadeEnsino(getAplicacaoControle().getUnidadeEnsinoVO(requerimentoVO.getUnidadeEnsino().getCodigo(), usuario));
//		//contaCorrente.setCodigo(configuracaoFinanceiroVO.getContaCorrentePadraoRequerimento());
//		if (Uteis.isAtributoPreenchido(requerimentoVO.getUnidadeEnsino().getContaCorrentePadraoRequerimento())) {
//			contaCorrente.setCodigo(requerimentoVO.getUnidadeEnsino().getContaCorrentePadraoRequerimento());
//		} else {
//			contaCorrente.setCodigo(configuracaoFinanceiroVO.getContaCorrentePadraoRequerimento());
//		}
//		
//		if (!Uteis.isAtributoPreenchido(centroReceita) || !Uteis.isAtributoPreenchido(contaCorrente)) {
//			throw new ConsistirException("Os campos (CENTRO DE RECEITA REQUERIMENTO PADRÃO) e (CONTA CORRENTE PADRÃO REQUERIMENTO) devem ser configurados na Configuração Geral do Sistema.");
//		}
//		Double valorConta = requerimentoVO.getValorTotalFinal();
//		if (valorConta > 0) {
//			// executarCalculoDescontoRequerimentoContaReceber(requerimentoVO.getValor()
//			// + requerimentoVO.getValorAdicional(),
//			// requerimentoVO.getPercDesconto(),
//			// requerimentoVO.getValorDesconto(),
//			// requerimentoVO.getTipoDesconto());
//			if (requerimentoVO.getTipoPessoaAluno()) {
//				requerimentoVO.getPessoa().setAluno(true);
//			} else if (usuario.getIsApresentarVisaoProfessor()) {
//				requerimentoVO.getPessoa().setAluno(false);
//				requerimentoVO.getPessoa().setProfessor(true);
//				requerimentoVO.getPessoa().setFuncionario(true);
//			} else if (usuario.getIsApresentarVisaoCoordenador()) {
//				requerimentoVO.getPessoa().setAluno(false);
//				requerimentoVO.getPessoa().setFuncionario(true);
//			} else if (usuario.getIsApresentarVisaoAdministrativa() && usuario.getPessoa().getCodigo().equals(requerimentoVO.getPessoa().getCodigo())) {
//				requerimentoVO.getPessoa().setAluno(false);
//				requerimentoVO.getPessoa().setFuncionario(true);
//			} else {
//				requerimentoVO.getPessoa().setAluno(false);
//				requerimentoVO.getPessoa().setProfessor(false);
//				requerimentoVO.getPessoa().setFuncionario(false);
//				requerimentoVO.getPessoa().setRequisitante(true);
//			}			
//			Integer matriculaPeriodo = 0;
//			if (Uteis.isAtributoPreenchido(requerimentoVO.getMatricula().getMatricula())) {
//				matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultaCodigoUltimaMatriculaPeriodoPorMatricula(requerimentoVO.getMatricula().getMatricula(), false, usuario);
//			}
////			ContaReceberVO contaReceberVO = getFacadeFactory().getContaReceberFacade().criarContaReceber(requerimentoVO.getMatricula(), null, requerimentoVO.getPessoa(), requerimentoVO.getUnidadeEnsino(), requerimentoVO.getUnidadeEnsino(), contaCorrente, requerimentoVO.getCodigo(), TipoOrigemContaReceber.REQUERIMENTO.getValor(), requerimentoVO.getDataVencimentoContaReceber(), requerimentoVO.getDataVencimentoContaReceber(), valorConta, centroReceita.getCodigo(), 1, 1, TipoBoletoBancario.REQUERIMENTO.getValor(), null,  usuario, null, matriculaPeriodo, "", null);
////			requerimentoVO.setContaReceberVO(contaReceberVO);
////			requerimentoVO.setContaReceber(contaReceberVO.getCodigo());
////			requerimentoVO.setCentroReceita(centroReceita);
////			requerimentoVO.setNrDocumento(contaReceberVO.getNrDocumento());
////			requerimentoVO.setDataEmissaoBoleto(new Date());
////			if(requerimentoVO.getSituacaoFinanceira().equals("IS") || requerimentoVO.getSituacaoFinanceira().equals("AP")){
////				requerimentoVO.setSituacaoFinanceira("PE");
////			}
////			alterarDadosFinanceiros(requerimentoVO, usuario);
////			contaReceberVO.setDescricaoPagamento("Taxa do Requerimento (" + requerimentoVO.getTipoRequerimento().getNome() + ")");
////			getFacadeFactory().getContaReceberFacade().alterar(contaReceberVO,  false, usuario);
//		} else {
//			requerimentoVO.setSituacaoFinanceira("IS");
//			requerimentoVO.setSituacao("PE");
//			alterarDadosFinanceiros(requerimentoVO, usuario);			
//		}
//	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void receberBoleto(RequerimentoVO requerimento, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		try {
			if (requerimento.isNovoObj()) {
				incluir(requerimento, usuario,  configuracaoGeralSistema);
			} else {
				// alterarSemComit(requerimento);
			}
//			ContaReceberVO contaReceber = getFacadeFactory().getContaReceberFacade().consultarPorChavePrimaria(requerimento.getContaReceber(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,  usuario);
//			RecebimentoVO recebimento = requerimento.receberBoleto(contaReceber);
//			getFacadeFactory().getRecebimentoFacade().incluir(recebimento, usuario);
//			requerimento.setSituacaoFinanceira("PG");
//			requerimento.setSituacao("EX");
//			// alterarSemComit(requerimento);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>RequerimentoVO</code>. Sempre localiza o registro a ser excluído
	 * através da chave primária da entidade. Primeiramente verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>RequerimentoVO</code> que será removido
	 *            no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(RequerimentoVO obj,  UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, boolean validarPermissao) throws Exception {
		try {
			if(validarPermissao){
				excluir(getIdEntidade(), true, usuario);				
			}
			getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().excluirPorCodigoRequerimento(obj.getCodigo());
			getFacadeFactory().getMaterialRequerimentoFacade().excluirMaterialRequerimentos(obj.getMaterialRequerimentoVOs(), usuario, configuracaoGeralSistema);
//			getFacadeFactory().getImpressaoDeclaracaoFacade().validarExclusaoImpressaoDeclaracaoPorRequerimento(obj.getCodigo(), usuario, configuracaoGeralSistema);
			getFacadeFactory().getCancelamentoFacade().realizarAlteracaoCodigoRequerimentoCancelamento(obj.getCodigo());
			String sql = "DELETE FROM Requerimento WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
//			excluirContaReceber(obj, usuario, configuracaoFinanceiro);
			excluirArquivo(obj, usuario, configuracaoGeralSistema);
			if(Uteis.isAtributoPreenchido(obj.getComprovanteSolicitacaoIsencao())) {
				getFacadeFactory().getArquivoFacade().excluir(obj.getComprovanteSolicitacaoIsencao(), usuario, configuracaoGeralSistema);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirArquivo(RequerimentoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		if (obj.getArquivoVO().getCodigo().intValue() != 0) {
			getFacadeFactory().getArquivoFacade().excluir(obj.getArquivoVO(), usuario, configuracaoGeralSistema);
		}
	}

//	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//	public void excluirContaReceber(RequerimentoVO obj, UsuarioVO usuario configuracaoFinanceiro) throws Exception {
//		if (Uteis.isAtributoPreenchido(obj.getContaReceber())) {
//			ContaReceberVO contaReceberVO = new ContaReceberVO();
//			try {
//				contaReceberVO = getFacadeFactory().getContaReceberFacade().consultarPorChavePrimaria(obj.getContaReceber(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,  usuario);
//				if (contaReceberVO.getSituacao().equals("RE") || contaReceberVO.getSituacao().equals("NE")) {
//					throw new Exception("Não é possivel excluir/alterar este requerimento, existe conta a receber paga ou negociada.");
//				}
//
//				getFacadeFactory().getContaReceberFacade().excluir(contaReceberVO,  false, usuario);
//			} catch (Exception e) {
//				throw e;
//			}
//		}
//	}

	public void verificarExisteSolicitacaoRequerimento(RequerimentoVO requerimentoVO, UsuarioVO usuario) throws Exception {
		if (requerimentoVO.getCodigo() != null && !requerimentoVO.getCodigo().equals(0)) {
			if (requerimentoVO.getTipoRequerimento().getTipo().equals("CA")) {
				CancelamentoVO cancelamentoVO = getFacadeFactory().getCancelamentoFacade().consultarPorCodigoRequerimento(requerimentoVO.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX,  usuario);
				if (!cancelamentoVO.getCodigo().equals(0)) {
					cancelamentoVO.setSituacao(SituacaoRequerimento.FINALIZADO_INDEFERIDO.getValor());
					getFacadeFactory().getCancelamentoFacade().alterarSituacaoCancelamento(cancelamentoVO.getCodigo(), cancelamentoVO.getSituacao(), usuario);
				}
			}

			if (requerimentoVO.getTipoRequerimento().getTipo().equals("TR")) {
				TrancamentoVO trancamentoVO = getFacadeFactory().getTrancamentoFacade().consultarPorCodigoRequerimento(requerimentoVO.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX,  usuario);
				if (!trancamentoVO.getCodigo().equals(0)) {
					trancamentoVO.setSituacao(SituacaoRequerimento.FINALIZADO_INDEFERIDO.getValor());
					getFacadeFactory().getTrancamentoFacade().alterarSituacao(trancamentoVO.getCodigo(), trancamentoVO.getSituacao());
				}
			}

			if (requerimentoVO.getTipoRequerimento().getTipo().equals("TI")) {
				TransferenciaEntradaVO transferenciaEntradaVO = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorCodigoRequerimento(requerimentoVO.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX,  usuario);
				if (!transferenciaEntradaVO.getCodigo().equals(0)) {
					transferenciaEntradaVO.setSituacao(SituacaoTransferenciaEntrada.INDEFERIDO.getValor());
					getFacadeFactory().getTransferenciaEntradaFacade().alterarSituacao(transferenciaEntradaVO.getCodigo(), transferenciaEntradaVO.getSituacao());
				}
			}
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>Requerimento</code>
	 * através do valor do atributo <code>matricula</code> da classe
	 * <code>Matricula</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>RequerimentoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<RequerimentoVO> consultarPorMatriculaMatricula(String valorConsulta, String tipoRequerimento, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Requerimento.* FROM Requerimento, Matricula WHERE Requerimento.matricula = Matricula.matricula and Matricula.matricula like('" + valorConsulta + "%') ";
		if (!tipoRequerimento.equals("")) {
			sqlStr = "SELECT Requerimento.* FROM Requerimento, Matricula, TipoRequerimento WHERE Requerimento.matricula = Matricula.matricula and Matricula.matricula like('" + valorConsulta + "%')  and Requerimento.tipoRequerimento = TipoRequerimento.codigo and upper (TipoRequerimento.tipo) = '" + tipoRequerimento.toUpperCase() + "' ";
		}
		if (unidadeEnsino != 0) {
			sqlStr += " and Requerimento.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}

		sqlStr += " ORDER BY Matricula.matricula";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<RequerimentoVO> consultarPorMatriculaMatriculaSemProgramacaoFormatura(String valorConsulta, String tipoRequerimento, List<ProgramacaoFormaturaUnidadeEnsinoVO> proFormaturaUnidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = " SELECT Requerimento.* FROM Requerimento" + " LEFT JOIN tiporequerimento ON tiporequerimento.codigo = requerimento.tiporequerimento" + " LEFT JOIN matricula ON Requerimento.matricula = Matricula.matricula" + " LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.requerimento = requerimento.codigo" + " WHERE ((programacaoformaturaaluno.colougrau <> 'NI' AND programacaoformaturaaluno.colougrau <> 'SI') OR programacaoformaturaaluno.matricula ISNULL) AND Matricula.matricula like('" + valorConsulta + "%') ";
		if (!tipoRequerimento.equals("")) {
			sqlStr += " AND upper (TipoRequerimento.tipo) = '" + tipoRequerimento.toUpperCase() + "' ";
		}
		if (Uteis.isAtributoPreenchido(proFormaturaUnidadeEnsinoVOs)) {
			sqlStr += " AND Requerimento.unidadeEnsino IN (";
			for (ProgramacaoFormaturaUnidadeEnsinoVO unidadeVO : proFormaturaUnidadeEnsinoVOs) {
				if (!proFormaturaUnidadeEnsinoVOs.get(proFormaturaUnidadeEnsinoVOs.size() - 1).getUnidadeEnsinoVO().getCodigo().equals(unidadeVO.getUnidadeEnsinoVO().getCodigo())) {
					sqlStr += unidadeVO.getUnidadeEnsinoVO().getCodigo()+",";
				} else {
					sqlStr += unidadeVO.getUnidadeEnsinoVO().getCodigo()+")";
				}
			}
		}
		sqlStr += " ORDER BY Matricula.matricula";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<RequerimentoVO> consultarPorMatriculaMatriculaVisaoAluno(String valorConsulta, Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = " SELECT Requerimento.* FROM Requerimento, Matricula, Pessoa WHERE Requerimento.matricula = Matricula.matricula and Matricula.matricula like('" + valorConsulta + "%') " + " and Matricula.aluno = Pessoa.codigo and Pessoa.codigo=" + pessoa.intValue() + " and Matricula.situacao = 'AT' ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and Requerimento.unidadeEnsino = " + unidadeEnsino.intValue();
		}
		sqlStr += " ORDER BY Matricula.matricula";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Requerimento</code>
	 * através do valor do atributo <code>nome</code> da classe
	 * <code>Pessoa</code> Faz uso da operação <code>montarDadosConsulta</code>
	 * que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>RequerimentoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<RequerimentoVO> consultarPorNomePessoa(String valorConsulta, String tipoRequerimento, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Requerimento.* FROM Requerimento, Pessoa WHERE Requerimento.pessoa = Pessoa.codigo and Pessoa.nome ilike('" + valorConsulta + "%') ";
		if (!tipoRequerimento.equals("")) {
			sqlStr = "SELECT Requerimento.* FROM Requerimento, Pessoa, TipoRequerimento  WHERE Requerimento.pessoa = Pessoa.codigo and Pessoa.nome ilike('" + valorConsulta + "%')  and Requerimento.tipoRequerimento = TipoRequerimento.codigo and upper (TipoRequerimento.tipo) = '" + tipoRequerimento.toUpperCase() + "' ";
		}
		if (unidadeEnsino != 0) {
			sqlStr += " and Requerimento.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}

		sqlStr += "ORDER BY Pessoa.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<RequerimentoVO> consultarPorNomePessoaSemProgramacaoFormatura(String valorConsulta, String tipoRequerimento, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = " SELECT Requerimento.* FROM Requerimento" + " LEFT JOIN tiporequerimento ON tiporequerimento.codigo = requerimento.tiporequerimento" + " LEFT JOIN pessoa ON Requerimento.pessoa = Pessoa.codigo" + " LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.requerimento = requerimento.codigo" + " WHERE programacaoformaturaaluno.matricula ISNULL AND Pessoa.nome ilike('" + valorConsulta + "%') ";
		if (!tipoRequerimento.equals("")) {
			sqlStr += " AND upper (TipoRequerimento.tipo) = '" + tipoRequerimento.toUpperCase() + "' ";
		}
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr += " AND Requerimento.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		sqlStr += " ORDER BY Pessoa.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<RequerimentoVO> consultarPorResponsavelSemProgramacaoFormatura(String valorConsulta, String tipoRequerimento, List<ProgramacaoFormaturaUnidadeEnsinoVO> proFormaturaUnidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = " SELECT Requerimento.* FROM Requerimento" + " LEFT JOIN tiporequerimento ON tiporequerimento.codigo = requerimento.tiporequerimento" + " LEFT JOIN usuario ON usuario.codigo = requerimento.responsavelrecebimentodocrequerido" + " LEFT JOIN pessoa ON pessoa.codigo = usuario.pessoa" + " LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.requerimento = requerimento.codigo" + " WHERE ((programacaoformaturaaluno.colougrau <> 'NI' AND programacaoformaturaaluno.colougrau <> 'SI') OR programacaoformaturaaluno.matricula ISNULL) AND Pessoa.nome ilike('" + valorConsulta + "%') ";
		if (!tipoRequerimento.equals("")) {
			sqlStr += " AND upper (TipoRequerimento.tipo) = '" + tipoRequerimento.toUpperCase() + "' ";
		}
		if (Uteis.isAtributoPreenchido(proFormaturaUnidadeEnsinoVOs)) {
			sqlStr += " AND Requerimento.unidadeEnsino IN (";
			for (ProgramacaoFormaturaUnidadeEnsinoVO unidadeVO : proFormaturaUnidadeEnsinoVOs) {
				if (!proFormaturaUnidadeEnsinoVOs.get(proFormaturaUnidadeEnsinoVOs.size() - 1).getUnidadeEnsinoVO().getCodigo().equals(unidadeVO.getUnidadeEnsinoVO().getCodigo())) {
					sqlStr += unidadeVO.getUnidadeEnsinoVO().getCodigo()+",";
				} else {
					sqlStr += unidadeVO.getUnidadeEnsinoVO().getCodigo()+")";
				}
			}
		}
		sqlStr += " ORDER BY Pessoa.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<RequerimentoVO> consultarPorNomeCPFPessoa(String valorConsulta, String tipoRequerimento, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Requerimento.* FROM Requerimento, Pessoa WHERE Requerimento.pessoa = Pessoa.codigo and Pessoa.cpf like('" + valorConsulta + "%') ";
		if (!tipoRequerimento.equals("")) {
			sqlStr = "SELECT Requerimento.* FROM Requerimento, Pessoa, TipoRequerimento  WHERE Requerimento.pessoa = Pessoa.codigo and Pessoa.cpf like('" + valorConsulta + "%') and Requerimento.tipoRequerimento = TipoRequerimento.codigo and upper (TipoRequerimento.tipo) = '" + tipoRequerimento.toUpperCase() + "' ";
		}
		if (unidadeEnsino != 0) {
			sqlStr += " Requerimento.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}

		sqlStr += "ORDER BY Pessoa.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public Integer consultaRapidaRequerimentoPendenteUsuario(Integer codUsuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select count(requerimento.codigo) as qtd from requerimento where departamentoresponsavel in ");
		sqlStr.append("(select cargo.departamento from funcionariocargo inner join cargo on funcionariocargo.cargo = cargo.codigo ");
		sqlStr.append("inner join departamento on departamento.codigo = cargo.departamento ");
		sqlStr.append("inner join funcionario on funcionario.codigo = funcionariocargo.funcionario ");
		sqlStr.append("where funcionario.pessoa = ");
		sqlStr.append(codUsuarioLogado.intValue());
		sqlStr.append(" ) and situacao = 'PE'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("qtd"));
		} else {
			return new Integer(0);
		}
	}

	public Boolean consultaRapidaRequerimentoReposicaoAluno(String matricula, Integer disciplina) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select requerimento.codigo from requerimento where matricula = '").append(matricula).append("'");
		sqlStr.append(" and turma is not null ");
		sqlStr.append(" and disciplina = ").append(disciplina).append("");
		sqlStr.append(" and situacao <> 'FI'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return true;
		} else {
			return false;
		}
	}

	public List<RequerimentoVO> consultaRapidaRequerimentoPendenteUsuarioMenu(Integer codUsuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("where requerimento.departamentoresponsavel in ");
		sqlStr.append("(select cargo.departamento from funcionariocargo inner join cargo on funcionariocargo.cargo = cargo.codigo ");
		sqlStr.append("inner join departamento on departamento.codigo = cargo.departamento ");
		sqlStr.append("inner join funcionario on funcionario.codigo = funcionariocargo.funcionario ");
		sqlStr.append("where funcionario.pessoa = ").append(codUsuarioLogado.intValue()).append(" ) ");
		sqlStr.append("and (requerimento.situacao = 'PE' OR requerimento.situacao = 'EX')");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, configuracaoGeralSistemaVO);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Requerimento</code>
	 * através do valor do atributo <code>String situacaoFinanceira</code>.
	 * Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>RequerimentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<RequerimentoVO> consultarPorSituacaoFinanceira(String valorConsulta, String tipoRequerimento, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Requerimento.* FROM Requerimento WHERE situacaoFinanceira like('" + valorConsulta + "%') ";
		if (!tipoRequerimento.equals("")) {
			sqlStr = "SELECT Requerimento.* FROM Requerimento, TipoRequerimento  WHERE Requerimento.situacaoFinanceira like('" + valorConsulta + "%')  and Requerimento.tipoRequerimento = TipoRequerimento.codigo and upper (TipoRequerimento.tipo) = '" + tipoRequerimento.toUpperCase() + "' ";
		}
		if (unidadeEnsino != 0) {
			sqlStr += " and Requerimento.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}

		sqlStr += "ORDER BY Requerimento.situacaoFinanceira";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<RequerimentoVO> consultarRequerimentoAtrasadosParaExclusao(UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select requerimento.* from requerimento ");
		sqlStr.append(" inner join contareceber on contareceber.codigo = requerimento.contareceber inner join tiporequerimento on tiporequerimento.codigo = requerimento.tipoRequerimento");
		sqlStr.append(" where contareceber.situacao = 'AR' and requerimento.situacao not in ('FD', 'FI') ");
		sqlStr.append(" and tipoRequerimento.diasparaexclusaorequerimentodefazados > 0 and contareceber.datavencimento < ");
		sqlStr.append("(current_date - (tipoRequerimento.diasparaexclusaorequerimentodefazados||' days')::interval) ");
		sqlStr.append(" and contareceber.codigo not in (");
		sqlStr.append(" select contareceberregistroarquivo.contareceber from contareceberregistroarquivo");
		sqlStr.append(" where 1=1 and contareceberregistroarquivo.contareceber = contareceber.codigo");
		sqlStr.append(" and contareceberregistroarquivo.situacao = 'RE'");
		sqlStr.append(" )");
		sqlStr.append(" order by tipoRequerimento.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Requerimento</code>
	 * através do valor do atributo <code>String situacao</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>RequerimentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<RequerimentoVO> consultarPorSituacao(String valorConsulta, String tipoRequerimento, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Requerimento WHERE situacao like('" + valorConsulta + "%') ";
		if (!tipoRequerimento.equals("")) {
			sqlStr = "SELECT Requerimento.* FROM Requerimento, TipoRequerimento WHERE Requerimento.situacao like('" + valorConsulta + "%') and Requerimento.tipoRequerimento = TipoRequerimento.codigo and upper (TipoRequerimento.tipo) = '" + tipoRequerimento.toUpperCase() + "' ";
		}
		if (unidadeEnsino != 0) {
			sqlStr += " and Requerimento.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		sqlStr += "ORDER BY Requerimento.situacao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<RequerimentoVO> consultarPorMatriculaSituacaoDiferenteDe(String valorConsulta, String situacao, String tipoRequerimento, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Requerimento WHERE situacao <> ('" + situacao + "%') ";
		if (!tipoRequerimento.equals("")) {
			sqlStr = "SELECT Requerimento.* FROM Requerimento, TipoRequerimento WHERE Requerimento.matricula = '" + valorConsulta + "' and Requerimento.situacao <>('" + situacao + "%') and Requerimento.tipoRequerimento = TipoRequerimento.codigo and upper (TipoRequerimento.tipo) = '" + tipoRequerimento.toUpperCase() + "' ";
		}
		if (unidadeEnsino != 0) {
			sqlStr += " and Requerimento.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		sqlStr += "ORDER BY Requerimento.situacao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<RequerimentoVO> consultarPorSituacaoSemProgramacaoFormatura(String valorConsulta, String tipoRequerimento, List<ProgramacaoFormaturaUnidadeEnsinoVO> proFormaturaUnidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = " SELECT * FROM Requerimento" + " LEFT JOIN TipoRequerimento ON Requerimento.tipoRequerimento = TipoRequerimento.codigo " + " LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.requerimento = requerimento.codigo" + " WHERE ((programacaoformaturaaluno.colougrau <> 'NI' AND programacaoformaturaaluno.colougrau <> 'SI') OR programacaoformaturaaluno.matricula ISNULL) AND situacao like('" + valorConsulta + "%') ";
		if (!tipoRequerimento.equals("")) {
			sqlStr += " AND upper (TipoRequerimento.tipo) = '" + tipoRequerimento.toUpperCase() + "' ";
		}
		if (Uteis.isAtributoPreenchido(proFormaturaUnidadeEnsinoVOs)) {
			sqlStr += " AND Requerimento.unidadeEnsino IN (";
			for (ProgramacaoFormaturaUnidadeEnsinoVO unidadeVO : proFormaturaUnidadeEnsinoVOs) {
				if (!proFormaturaUnidadeEnsinoVOs.get(proFormaturaUnidadeEnsinoVOs.size() - 1).getUnidadeEnsinoVO().getCodigo().equals(unidadeVO.getUnidadeEnsinoVO().getCodigo())) {
					sqlStr += unidadeVO.getUnidadeEnsinoVO().getCodigo()+",";
				} else {
					sqlStr += unidadeVO.getUnidadeEnsinoVO().getCodigo()+")";
				}
			}
		}
		sqlStr += " ORDER BY Requerimento.situacao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Requerimento</code>
	 * através do valor do atributo <code>Date dataFinalizacao</code>. Retorna
	 * os objetos com valores pertecentes ao período informado por parâmetro.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>RequerimentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<RequerimentoVO> consultarPorDataFinalizacao(Date prmIni, Date prmFim, String tipoRequerimento, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Requerimento.* FROM Requerimento WHERE ((dataFinalizacao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataFinalizacao <= '" + Uteis.getDataJDBC(prmFim) + "')) ";
		if (!tipoRequerimento.equals("")) {
			sqlStr = "SELECT Requerimento.* FROM Requerimento, TipoRequerimento WHERE ((Requerimento.dataFinalizacao >= '" + Uteis.getDataJDBC(prmIni) + "') and (Requerimento.dataFinalizacao <= '" + Uteis.getDataJDBC(prmFim) + "')) and Requerimento.tipoRequerimento = TipoRequerimento.codigo and upper (TipoRequerimento.tipo) = '" + tipoRequerimento.toUpperCase() + "' ";
		}
		if (unidadeEnsino != 0) {
			sqlStr += " and Requerimento.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		sqlStr += "ORDER BY Requerimento.dataFinalizacao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Requerimento</code>
	 * através do valor do atributo <code>Date dataPrevistaFinalizacao</code>.
	 * Retorna os objetos com valores pertecentes ao período informado por
	 * parâmetro. Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>RequerimentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<RequerimentoVO> consultarPorDataPrevistaFinalizacao(Date prmIni, Date prmFim, String tipo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Requerimento.* FROM Requerimento WHERE ((dataPrevistaFinalizacao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataPrevistaFinalizacao <= '" + Uteis.getDataJDBC(prmFim) + "')) ";
		if (!tipo.equals("")) {
			sqlStr = "SELECT Requerimento.* FROM Requerimento, TipoRequerimento WHERE ((Requerimento.dataPrevistaFinalizacao >= '" + Uteis.getDataJDBC(prmIni) + "') and (Requerimento.dataPrevistaFinalizacao <= '" + Uteis.getDataJDBC(prmFim) + "')) and Requerimento.tipoRequerimento = TipoRequerimento.codigo and upper (TipoRequerimento.tipo) = '" + tipo.toUpperCase() + "' ";
		}
		if (unidadeEnsino != 0) {
			sqlStr = " Requerimento.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		sqlStr += "ORDER BY Requerimento.dataPrevistaFinalizacao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Requerimento</code>
	 * através do valor do atributo <code>Double valor</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>RequerimentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<RequerimentoVO> consultarPorValor(Double valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Requerimento* FROM Requerimento WHERE valor >= " + valorConsulta.doubleValue() + " ";
		if (unidadeEnsino != 0) {
			sqlStr = "SELECT Requerimento* FROM Requerimento, Departamento WHERE valor >= " + valorConsulta.doubleValue() + " and unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		sqlStr += "ORDER BY valor";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<RequerimentoVO> consultarPorCodigoCentroReceita(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Requerimento* FROM Requerimento WHERE centroReceita >= " + valorConsulta.intValue() + " ";
		if (unidadeEnsino != 0) {
			sqlStr = "SELECT Requerimento* FROM Requerimento, Departamento WHERE centroReceita >= " + valorConsulta.intValue() + " and unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		sqlStr += "ORDER BY centroReceita";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Requerimento</code>
	 * através do valor do atributo <code>nome</code> da classe
	 * <code>TipoRequerimento</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>RequerimentoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<RequerimentoVO> consultarPorNomeTipoRequerimento(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Requerimento.* FROM Requerimento, TipoRequerimento WHERE Requerimento.tipoRequerimento = TipoRequerimento.codigo and TipoRequerimento.nome like('" + valorConsulta + "%')";
		if (unidadeEnsino != 0) {
			sqlStr = "SELECT Requerimento.* FROM Requerimento, TipoRequerimento WHERE Requerimento.tipoRequerimento = TipoRequerimento.codigo and TipoRequerimento.nome like('" + valorConsulta + "%') and Requerimento.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		sqlStr += "ORDER BY TipoRequerimento.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<RequerimentoVO> consultarPorNomeTipoRequerimento(String valorConsulta, Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Requerimento.* FROM Requerimento, TipoRequerimento, Matricula WHERE Requerimento.tipoRequerimento = TipoRequerimento.codigo and TipoRequerimento.nome like('" + valorConsulta + "%')" + " and Requerimento.matricula = Matricula.matricula and Matricula.aluno = " + pessoa.intValue() + " and Matricula.situacao = 'AT' ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and Requerimento.unidadeEnsino = " + unidadeEnsino.intValue();
		}

		sqlStr += " ORDER BY TipoRequerimento.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<RequerimentoVO> consultarPorCodigo(Integer valorConsulta, Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Requerimento, Matricula WHERE codigo >= " + valorConsulta.intValue() + " and Requerimento.matricula = Matricula.matricula and Matricula.aluno = " + pessoa.intValue() + " and Matricula.situacao = 'AT' ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and Requerimento.unidadeEnsino = " + unidadeEnsino.intValue();
		}

		sqlStr += " ORDER BY Requerimento.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public List<RequerimentoVO> consultarPorCodigo(String listaCodigos, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Requerimento WHERE codigo in (" + listaCodigos + ") ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Requerimento</code>
	 * através do valor do atributo <code>Date data</code>. Retorna os objetos
	 * com valores pertecentes ao período informado por parâmetro. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>RequerimentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<RequerimentoVO> consultarPorData(Date prmIni, Date prmFim, String tipo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Requerimento WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ";
		if (!tipo.equals("")) {
			sqlStr = "SELECT Requerimento.* FROM Requerimento, TipoRequerimento WHERE ((Requerimento.data >= '" + Uteis.getDataJDBC(prmIni) + "') and (Requerimento.data <= '" + Uteis.getDataJDBC(prmFim) + "')) and Requerimento.tipoRequerimento = TipoRequerimento.codigo and upper (TipoRequerimento.tipo) = '" + tipo.toUpperCase() + "' ";
		}
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and Requerimento.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		sqlStr += " ORDER BY data ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<RequerimentoVO> consultarPorDataSemProgramacaoFormatura(Date prmIni, Date prmFim, String tipo, List<ProgramacaoFormaturaUnidadeEnsinoVO> proFormaturaUnidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = " SELECT * FROM Requerimento" + " INNER JOIN TipoRequerimento ON Requerimento.tipoRequerimento = TipoRequerimento.codigo " + " LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.requerimento = requerimento.codigo" + " WHERE ((programacaoformaturaaluno.colougrau <> 'NI' AND programacaoformaturaaluno.colougrau <> 'SI' OR) programacaoformaturaaluno.matricula ISNULL) AND ((requerimento.data >= '" + Uteis.getDataJDBC(prmIni) + "') AND (requerimento.data <= '" + Uteis.getDataJDBC(prmFim) + "')) ";
		if (!tipo.equals("")) {
			sqlStr += " AND upper (TipoRequerimento.tipo) = '" + tipo.toUpperCase() + "' ";
		}
		if (Uteis.isAtributoPreenchido(proFormaturaUnidadeEnsinoVOs)) {
			sqlStr += " AND Requerimento.unidadeEnsino IN (";
			for (ProgramacaoFormaturaUnidadeEnsinoVO unidadeVO : proFormaturaUnidadeEnsinoVOs) {
				if (!proFormaturaUnidadeEnsinoVOs.get(proFormaturaUnidadeEnsinoVOs.size() - 1).getUnidadeEnsinoVO().getCodigo().equals(unidadeVO.getUnidadeEnsinoVO().getCodigo())) {
					sqlStr += unidadeVO.getUnidadeEnsinoVO().getCodigo()+",";
				} else {
					sqlStr += unidadeVO.getUnidadeEnsinoVO().getCodigo()+")";
				}
			}
		}
		sqlStr += " ORDER BY requerimento.data ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Requerimento</code>
	 * através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>RequerimentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<RequerimentoVO> consultarPorCodigo(Integer valorConsulta, String tipo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT requerimento.* " + "FROM Requerimento inner join TipoRequerimento on Requerimento.tipoRequerimento = TipoRequerimento.codigo " + "WHERE Requerimento.codigo >= " + valorConsulta.intValue();
		if (!tipo.equals("")) {
			sqlStr += " and upper (TipoRequerimento.tipo) = '" + tipo.toUpperCase() + "' ";
		}
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and Requerimento.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		sqlStr += " ORDER BY Requerimento.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<RequerimentoVO> consultarPorCodigoSemProgramacaoFormatura(Integer valorConsulta, String tipo, List<ProgramacaoFormaturaUnidadeEnsinoVO> proFormaturaUnidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = " SELECT requerimento.* FROM Requerimento " + " INNER JOIN TipoRequerimento on Requerimento.tipoRequerimento = TipoRequerimento.codigo " + " LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.requerimento = requerimento.codigo" + " WHERE ((programacaoformaturaaluno.colougrau <> 'NI' AND programacaoformaturaaluno.colougrau <> 'SI') OR programacaoformaturaaluno.matricula ISNULL) AND Requerimento.codigo >= " + valorConsulta.intValue();
		if (!tipo.equals("")) {
			sqlStr += " AND upper (TipoRequerimento.tipo) = '" + tipo.toUpperCase() + "' ";
		}
		if (Uteis.isAtributoPreenchido(proFormaturaUnidadeEnsinoVOs)) {
			sqlStr += " AND Requerimento.unidadeEnsino IN (";
			for (ProgramacaoFormaturaUnidadeEnsinoVO unidadeVO : proFormaturaUnidadeEnsinoVOs) {
				if (!proFormaturaUnidadeEnsinoVOs.get(proFormaturaUnidadeEnsinoVOs.size() - 1).getUnidadeEnsinoVO().getCodigo().equals(unidadeVO.getUnidadeEnsinoVO().getCodigo())) {
					sqlStr += unidadeVO.getUnidadeEnsinoVO().getCodigo()+",";
				} else {
					sqlStr += unidadeVO.getUnidadeEnsinoVO().getCodigo()+")";
				}
			}
		}
		sqlStr += " ORDER BY Requerimento.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>RequerimentoVO</code> resultantes da consulta.
	 */
	public  List<RequerimentoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<RequerimentoVO> vetResultado = new ArrayList<RequerimentoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>RequerimentoVO</code>.
	 * 
	 * @return O objeto da classe <code>RequerimentoVO</code> com os dados
	 *         devidamente montados.
	 */
	public  RequerimentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		RequerimentoVO obj = new RequerimentoVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
//		obj.getTaxa().setCodigo(new Integer(dadosSQL.getInt("taxa")));
		obj.setData(dadosSQL.getTimestamp("data"));
		obj.getTipoRequerimento().setCodigo(new Integer(dadosSQL.getInt("tipoRequerimento")));
		obj.setValor(new Double(dadosSQL.getDouble("valor")));
		obj.setDataPrevistaFinalizacao(dadosSQL.getDate("dataPrevistaFinalizacao"));
		obj.setDataFinalizacao(dadosSQL.getDate("dataFinalizacao"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setSituacaoFinanceira(dadosSQL.getString("situacaoFinanceira"));
		obj.getDepartamentoResponsavel().setCodigo(new Integer(dadosSQL.getInt("departamentoResponsavel")));
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.setNomeRequerente(dadosSQL.getString("nomeRequerente"));
		obj.setCpfRequerente(dadosSQL.getString("cpfRequerente"));
		obj.setObservacao(dadosSQL.getString("observacao"));
//		obj.getCentroReceita().setCodigo(new Integer(dadosSQL.getInt("centroReceita")));
		obj.setDataRecebimentoDocRequerido(dadosSQL.getDate("dataRecebimentoDocRequerido"));
		obj.setContaReceber(new Integer(dadosSQL.getInt("contaReceber")));
		obj.setNumeroVia(dadosSQL.getInt("numeroVia"));
		obj.setNrDocumento(dadosSQL.getString("nrDocumento"));
		obj.getResponsavelEmissaoBoleto().setCodigo(new Integer(dadosSQL.getInt("responsavelEmissaoBoleto")));
		obj.setDataEmissaoBoleto(dadosSQL.getDate("dataEmissaoBoleto"));
//		obj.getContaCorrenteVO().setCodigo(new Integer(dadosSQL.getInt("contaCorrente")));
		obj.setPercDesconto((dadosSQL.getDouble("percDesconto")));
		obj.setValorDesconto((dadosSQL.getDouble("valorDesconto")));
		obj.setValorAdicional((dadosSQL.getDouble("valorAdicional")));
		obj.setTipoDesconto(dadosSQL.getString("tipoDesconto"));
		obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
		obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt("pessoa")));
		obj.getQuestionarioVO().setCodigo(new Integer(dadosSQL.getInt("questionario")));
		obj.getCurso().setCodigo(new Integer(dadosSQL.getInt("curso")));
		obj.getTurno().setCodigo(new Integer(dadosSQL.getInt("turno")));
		obj.getTurma().setCodigo(dadosSQL.getInt("turma"));
		obj.getTurmaReposicao().setCodigo(dadosSQL.getInt("turmaReposicao"));
		obj.getDisciplina().setCodigo(dadosSQL.getInt("disciplina"));
		// obj.getCurso().setNome(dadosSQL.getString("nomecurso"));
		obj.getFuncionarioVO().setCodigo(new Integer(dadosSQL.getInt("funcionario")));
		obj.setTaxaIsentaPorQtdeVia(dadosSQL.getBoolean("taxaIsentaPorQtdeVia"));
		obj.setCEP(dadosSQL.getString("CEP"));
		obj.setEndereco(dadosSQL.getString("endereco"));
		obj.setSetor(dadosSQL.getString("setor"));
		obj.setNumero(dadosSQL.getString("numero"));
		obj.setComplemento(dadosSQL.getString("complemento"));
		obj.getCidade().setCodigo(dadosSQL.getInt("cidade"));
		obj.getArquivoVO().setCodigo(dadosSQL.getInt("arquivo"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
		obj.setVisaoGerado(dadosSQL.getString("visaoGerado"));
		obj.setMotivoIndeferimento(dadosSQL.getString("motivoIndeferimento"));
		obj.setOrdemExecucaoTramiteDepartamento(dadosSQL.getInt("ordemExecucaoTramiteDepartamento"));
		obj.setSigla(dadosSQL.getString("sigla"));
		obj.setMotivoDeferimento(dadosSQL.getString("motivoDeferimento"));
		obj.getSituacaoRequerimentoDepartamentoVO().setCodigo(dadosSQL.getInt("situacaoRequerimentoDepartamento"));		
		
		obj.setDataDeferimentoIndeferimentoIsencaoTaxa(dadosSQL.getDate("dataDeferimentoIndeferimentoIsencaoTaxa"));
		obj.setMotivoDeferimentoIndeferimentoIsencaoTaxa(dadosSQL.getString("motivoDeferimentoIndeferimentoIsencaoTaxa"));
		obj.setMensagemChoqueHorario(dadosSQL.getString("mensagemChoqueHorario"));
		obj.setJustificativaSolicitacaoIsencao(dadosSQL.getString("justificativaSolicitacaoIsencao"));
		obj.setCid(dadosSQL.getString("cid"));
		if(dadosSQL.getString("situacaoIsencaoTaxa") != null) {
			obj.setSituacaoIsencaoTaxa(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.valueOf(dadosSQL.getString("situacaoIsencaoTaxa")));
		}
		obj.getComprovanteSolicitacaoIsencao().setCodigo(dadosSQL.getInt("comprovanteSolicitacaoIsencao"));
		obj.getResponsavelDeferimentoIndeferimentoIsencaoTaxa().setCodigo(dadosSQL.getInt("responsavelDeferimentoIndeferimentoIsencaoTaxa"));		
		if (dadosSQL.getString("tipoPessoa") == null || dadosSQL.getString("tipoPessoa").trim().isEmpty()) {
			obj.setTipoPessoa(obj.getMatricula().getMatricula().trim().isEmpty() ? TipoPessoa.REQUERENTE : TipoPessoa.ALUNO);
		} else {
			obj.setTipoPessoa(TipoPessoa.valueOf(dadosSQL.getString("tipoPessoa")));
		}
		obj.getUnidadeEnsinoTransferenciaInternaVO().setCodigo(dadosSQL.getInt("unidadeEnsinoTransferenciaInterna"));
		obj.getCursoTransferenciaInternaVO().setCodigo(dadosSQL.getInt("cursoTransferenciaInterna"));
		obj.getTurnoTransferenciaInternaVO().setCodigo(dadosSQL.getInt("turnoTransferenciaInterna"));
//		obj.getContaReceberVO().setCodigo(dadosSQL.getInt("contaReceber"));
//		if(Uteis.isAtributoPreenchido(dadosSQL.getInt("contaReceber"))){
//			getFacadeFactory().getContaReceberFacade().carregarDados(obj.getContaReceberVO(), NivelMontarDados.BASICO, null, null);	
//		}
		obj.setCargaHorariaDisciplina(dadosSQL.getInt("cargaHorariaDisciplina"));
		if(dadosSQL.getObject("dataInicioAula") != null) {
			obj.setDataInicioAula(dadosSQL.getDate("dataInicioAula"));
		}
		if(dadosSQL.getObject("dataTerminoAula") != null) {
			obj.setDataTerminoAula(dadosSQL.getDate("dataTerminoAula"));
		}
		obj.setDisciplinaPorEquivalencia(dadosSQL.getBoolean("disciplinaPorEquivalencia"));
		obj.getMapaEquivalenciaDisciplinaVO().setCodigo(dadosSQL.getInt("mapaEquivalenciaDisciplina"));
		obj.getMapaEquivalenciaDisciplinaCursadaVO().setCodigo(dadosSQL.getInt("mapaEquivalenciaDisciplinaCursada"));
//		obj.getSalaLocalAulaVO().setCodigo(dadosSQL.getInt("salaLocalAula"));
		obj.getMatriculaPeriodoVO().setCodigo(dadosSQL.getInt("matriculaPeriodo"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("motivoNaoAceiteCertificado"))) {
			obj.setMotivoNaoAceiteCertificado(dadosSQL.getString("motivoNaoAceiteCertificado"));
		}
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("formatoCertificadoSelecionado"))) {
			obj.setFormatoCertificadoSelecionado(dadosSQL.getString("formatoCertificadoSelecionado"));
		}
		
		if (Uteis.isColunaExistente(dadosSQL, "turmabase") && Uteis.isAtributoPreenchido(dadosSQL.getString("turmabase"))) {
			obj.getTurmaReposicao().setIdentificadorTurmaBase(dadosSQL.getString("turmabase"));
		}

		if (Uteis.isColunaExistente(dadosSQL, "justificativaTrancamento") && Uteis.isAtributoPreenchido(dadosSQL.getString("justificativaTrancamento"))) {
			obj.setJustificativaTrancamento(dadosSQL.getString("justificativaTrancamento"));
		}
		
		if (Uteis.isColunaExistente(dadosSQL, "motivoCancelamentoTrancamento") && Uteis.isAtributoPreenchido(dadosSQL.getString("motivoCancelamentoTrancamento"))) {
			obj.getMotivoCancelamentoTrancamento().setCodigo(dadosSQL.getInt("motivoCancelamentoTrancamento"));
		}
		montarDadosArquivo(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);		
		montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosTurno(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosTurma(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosDisciplina(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		obj.setMaterialRequerimentoVOs(getFacadeFactory().getMaterialRequerimentoFacade().consultarPorRequerimento(obj.getCodigo(), nivelMontarDados, usuario));
//		if (!obj.getContaReceber().equals(0)) {
//			obj.setDataVencimentoContaReceber(obj.getContaReceberVO().getDataVencimento());
//		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		if(Uteis.isAtributoPreenchido(obj.getComprovanteSolicitacaoIsencao().getCodigo())) {
			obj.setComprovanteSolicitacaoIsencao(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getComprovanteSolicitacaoIsencao().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}
		if(obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.SEGUNDA_CHAMADA.getValor())) {
			getFacadeFactory().getRequerimentoDisciplinaInterfaceFacade().consultarPorRequerimento(obj, usuario);
		}
		int pessoa = dadosSQL.getInt("responsavelRecebimentoDocRequerido");
		montarDadosTipoRequerimento(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosMatricula(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosCidade(obj, usuario);
		if (pessoa == 0) {
			obj.setResponsavelRecebimentoDocRequerido(new UsuarioVO());
		} else {
			obj.setResponsavelRecebimentoDocRequerido(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(new Integer(pessoa), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
//		montarDadosCentroReceita(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosResponsavelEmissaoBoleto(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosResponsavelIsencaoTaxa(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosDepartamentoResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
//		montarDadosContaCorrente(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
//		montarDadosSalaLocalAula(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosMapaEquivalenciaDisciplina(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosMapaEquivalenciaDisciplinaCursada(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		obj.setRequerimentoHistoricoVOs(getFacadeFactory().getRequerimentoHistoricoFacade().consultarPorCodigoRequerimento(obj.getCodigo(), false, null));
		return obj;
	}
	
	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>MatriculaVO</code> relacionado ao objeto
	 * <code>RequerimentoVO</code>. Faz uso da chave primária da classe
	 * <code>MatriculaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public  void montarDadosMatricula(RequerimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getMatricula().getMatricula() == null) || (obj.getMatricula().getMatricula().equals(""))) {
			obj.setMatricula(new MatriculaVO());
			return;
		}
		obj.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula().getMatricula(), 0, NivelMontarDados.BASICO, usuario));
	}

	public  void montarDadosPessoa(RequerimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getPessoa() == null) || (obj.getPessoa().getCodigo().intValue() == 0)) {
			obj.setPessoa(new PessoaVO());
			return;
		}
		obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoa().getCodigo(), false, nivelMontarDados, usuario));
	}

	public  void montarDadosResponsavelEmissaoBoleto(RequerimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getResponsavelEmissaoBoleto().getCodigo() == null) || (obj.getResponsavelEmissaoBoleto().getCodigo().intValue() == 0)) {
			obj.setResponsavelEmissaoBoleto(new UsuarioVO());
			return;
		}
		obj.setResponsavelEmissaoBoleto(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelEmissaoBoleto().getCodigo(), nivelMontarDados, usuario));
	}

	public  void montarDadosResponsavelIsencaoTaxa(RequerimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(obj.getResponsavelDeferimentoIndeferimentoIsencaoTaxa().getCodigo())) {
			obj.setResponsavelDeferimentoIndeferimentoIsencaoTaxa(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelDeferimentoIndeferimentoIsencaoTaxa().getCodigo(), nivelMontarDados, usuario));
		}
	}
//
//	public static void montarDadosCentroReceita(RequerimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//		if (obj.getCentroReceita() == null || obj.getCentroReceita().getCodigo().equals(0)) {
//			obj.setCentroReceita(new CentroReceitaVO());
//			return;
//		}
//		obj.setCentroReceita(getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimaria(obj.getCentroReceita().getCodigo(), false, nivelMontarDados, usuario));
//	}

	public  void montarDadosUnidadeEnsino(RequerimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsino() == null || obj.getUnidadeEnsino().getCodigo().equals(0)) {
			obj.setUnidadeEnsino(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	}

//	public static void montarDadosContaCorrente(RequerimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//		if (obj.getContaCorrenteVO() == null || obj.getContaCorrenteVO().getCodigo().equals(0)) {
//			obj.setContaCorrenteVO(new ContaCorrenteVO());
//			return;
//		}
//		obj.setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrenteVO().getCodigo(), false, nivelMontarDados, usuario));
//	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PessoaVO</code> relacionado ao objeto <code>RequerimentoVO</code>.
	 * Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public  void montarDadosDepartamentoResponsavel(RequerimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getDepartamentoResponsavel().getCodigo().intValue() == 0) {
			obj.setDepartamentoResponsavel(new DepartamentoVO());
			return;
		}
		obj.setDepartamentoResponsavel(getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(obj.getDepartamentoResponsavel().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>TipoRequerimentoVO</code> relacionado ao objeto
	 * <code>RequerimentoVO</code>. Faz uso da chave primária da classe
	 * <code>TipoRequerimentoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public  void montarDadosTipoRequerimento(RequerimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getTipoRequerimento().getCodigo().intValue() == 0) {
			obj.setTipoRequerimento(new TipoRequerimentoVO());
			return;
		}
		obj.setTipoRequerimento(getFacadeFactory().getTipoRequerimentoFacade().consultarPorChavePrimaria(obj.getTipoRequerimento().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}

	public  void montarDadosFuncionario(RequerimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFuncionarioVO().getCodigo().intValue() == 0) {
			obj.setFuncionarioVO(new FuncionarioVO());
			return;
		}
		obj.setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(obj.getFuncionarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	public  void montarDadosCidade(RequerimentoVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getCidade().getCodigo().intValue() == 0) {
			obj.setCidade(new CidadeVO());
			return;
		}
		obj.setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidade().getCodigo(), false, usuario));
	}

	public  void montarDadosArquivo(RequerimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getArquivoVO().getCodigo().intValue() == 0) {
			obj.setArquivoVO(new ArquivoVO());
			return;
		}
		obj.setArquivoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoVO().getCodigo(), nivelMontarDados, usuario));
	}
	
	

	public  void montarDadosCurso(RequerimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCurso().getCodigo().intValue() == 0) {
			obj.setCurso(new CursoVO());
			return;
		}
		obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), nivelMontarDados, false, usuario));
	}

	public  void montarDadosTurno(RequerimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getTurno().getCodigo().intValue() == 0) {
			obj.setTurno(new TurnoVO());
			return;
		}
		obj.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(obj.getTurno().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>RequerimentoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public RequerimentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM Requerimento WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm.intValue());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public RequerimentoVO consultarPorChavePrimariaFiltrandoPorUnidadeEnsino(Integer codigoPrm, String tipoRequerimento, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM Requerimento WHERE codigo = " + codigoPrm;
		if (unidadeEnsino != 0 && tipoRequerimento.equalsIgnoreCase("")) {
			sql = "SELECT Requerimento.* FROM Requerimento WHERE Requerimento.codigo = " + codigoPrm.intValue() + " and Requerimento.unidadeEnsino = " + unidadeEnsino.intValue() + " ORDER BY codigo";
		}
		if (unidadeEnsino != 0 && (!tipoRequerimento.equalsIgnoreCase(""))) {
			sql = "SELECT Requerimento.* FROM Requerimento, TipoRequerimento WHERE Requerimento.codigo = " + codigoPrm.intValue() + " and Requerimento.tipoRequerimento = TipoRequerimento.codigo and Requerimento.unidadeEnsino = " + unidadeEnsino.intValue() + " and TipoRequerimento.tipo = '" + tipoRequerimento + "' ORDER BY codigo";
		}
		if (unidadeEnsino == 0 && (!tipoRequerimento.equalsIgnoreCase(""))) {
			sql = "SELECT Requerimento.* FROM Requerimento, TipoRequerimento WHERE Requerimento.codigo = " + codigoPrm.intValue() + " and Requerimento.tipoRequerimento = TipoRequerimento.codigo and TipoRequerimento.tipo = '" + tipoRequerimento + "' ORDER BY codigo";
		}
		// PreparedStatement sqlConsultar = con.prepareStatement(sql);
		// sqlConsultar.setInt(1, codigoPrm.intValue());
		// if (unidadeEnsino != 0) {
		// sqlConsultar.setInt(2, unidadeEnsino.intValue());
		// }
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		if (!tabelaResultado.next()) {
			if (!tipoRequerimento.trim().isEmpty()) {
				TiposRequerimento tipoNecessario = TiposRequerimento.getEnum(tipoRequerimento);				
				throw new ConsistirException("Dados Não Encontrados (Requerimento). É necessário informar um requerimento do tipo "+tipoNecessario.getDescricao());				
			}
			throw new ConsistirException("Dados Não Encontrados (Requerimento).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public TiposRequerimento consultarTipoRequerimento(Integer requerimento) {
		StringBuilder sb = new StringBuilder();
		sb.append("select tipoRequerimento.tipo from requerimento ");
		sb.append(" inner join tipoRequerimento on tipoRequerimento.codigo = Requerimento.tipoRequerimento ");
		sb.append(" where requerimento.codigo = ").append(requerimento);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return TiposRequerimento.getEnum(tabelaResultado.getString("tipo"));
		}
		return TiposRequerimento.getEnum("");
	}

	public void carregarDados(RequerimentoVO obj, UsuarioVO usuario) throws Exception {
		carregarDados((RequerimentoVO) obj, NivelMontarDados.TODOS, null, usuario);
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
	public void carregarDados(RequerimentoVO obj, NivelMontarDados nivelMontarDados, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
			montarDadosBasico(obj, resultado, configuracaoGeralSistemaVO);
		}
		if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
			montarDadosCompleto(obj, resultado, usuario);
		}
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codRequerimento, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Requerimento.codigo= ").append(codRequerimento).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codRequerimento, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta(false);
		sqlStr.append(" WHERE (Requerimento.codigo= ").append(codRequerimento).append(")");
//		System.out.println(sqlStr.toString());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	private StringBuffer getSQLPadraoConsultaCompleta(boolean joinCTE) {
		StringBuffer str = new StringBuffer();
		// Requerimentox
		str.append("SELECT distinct requerimento.codigo AS \"requerimento.codigo\", requerimento.nomeRequerente AS \"requerimento.nomeRequerente\",  requerimento.numeroVia as \"requerimento.numeroVia\", ");
		str.append("requerimento.dataPrevistaFinalizacao AS \"requerimento.dataPrevistaFinalizacao\", requerimento.tipoDesconto AS \"requerimento.tipoDesconto\", ");
		str.append("requerimento.data AS \"requerimento.data\", requerimento.situacao AS \"requerimento.situacao\", requerimento.valor AS \"requerimento.valor\", ");
		str.append("contareceber.datavencimento AS \"contareceber.datavencimento\", requerimento.percDesconto AS \"requerimento.percDesconto\", requerimento.valorDesconto AS \"requerimento.valorDesconto\",  ");
		str.append("requerimento.ordemExecucaoTramiteDepartamento as \"requerimento.ordemExecucaoTramiteDepartamento\", requerimento.dataFinalizacao AS \"requerimento.dataFinalizacao\", requerimento.contaReceber AS \"requerimento.contaReceber\", ");
		str.append("requerimento.situacaoFinanceira AS \"requerimento.situacaoFinanceira\", requerimento.nrDocumento AS \"requerimento.nrDocumento\", ");
		str.append("requerimento.observacao AS \"requerimento.observacao\", requerimento.responsavel AS \"requerimento.responsavel\", responsavel.nome AS \"requerimento.nomeResponsavel\", ");
		str.append("requerimento.visaoGerado AS \"requerimento.visaoGerado\", requerimento.valorAdicional, requerimento.motivoIndeferimento, requerimento.motivoDeferimento, requerimento.questionario, requerimento.curso, curso.nome as nomeCurso, requerimento.turno, turno.nome as nometurno, ");
		str.append("requerimento.sigla AS \"requerimento.sigla\", requerimento.taxaIsentaPorQtdeVia as \"requerimento.taxaIsentaPorQtdeVia\", requerimento.taxa as \"requerimento.taxa\", requerimento.tipopessoa as \"requerimento.tipopessoa\", requerimento.codigonovorequerimento as \"requerimento.codigonovorequerimento\", requerimento.dataafastamentoinicio as \"requerimento.dataafastamentoinicio\", requerimento.dataafastamentofim as \"requerimento.dataafastamentofim\", ");
		
		str.append("requerimento.tipoTrabalhoConclusaoCurso AS \"requerimento.tipoTrabalhoConclusaoCurso\", requerimento.tituloMonografia AS \"requerimento.tituloMonografia\", ");
		str.append("requerimento.orientadorMonografia AS \"requerimento.orientadorMonografia\", requerimento.notamonografia AS \"requerimento.notamonografia\", requerimento.numerovia AS \"requerimento.numerovia\", ");
		
		// Dados Disciplina
		str.append("disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", ");
		// Dados Pessoa
		str.append("funcionario.codigo AS \"funcionario.codigo\", pessoaFuncionario.codigo AS \"pessoaFuncionario.codigo\", pessoaFuncionario.nome AS \"pessoaFuncionario.nome\", ");
		// Endereço de Entrega
		str.append("requerimento.endereco AS \"requerimento.endereco\", requerimento.setor AS \"requerimento.setor\", requerimento.numero AS \"requerimento.numero\", ");
		str.append("requerimento.cep AS \"requerimento.cep\", requerimento.complemento AS \"requerimento.complemento\", ");
		str.append("requerimento.situacaoRequerimentoDepartamento as \"requerimento.situacaoRequerimentoDepartamento\", ");
		str.append("requerimento.situacaoIsencaoTaxa as \"requerimento.situacaoIsencaoTaxa\", ");
		str.append("requerimento.comprovanteSolicitacaoIsencao as \"requerimento.comprovanteSolicitacaoIsencao\", ");
		str.append("requerimento.justificativaSolicitacaoIsencao as \"requerimento.justificativaSolicitacaoIsencao\", ");
		str.append("requerimento.motivoDeferimentoIndeferimentoIsencaoTaxa as \"requerimento.motivoDeferimentoIndeferimentoIsencaoTaxa\", ");
		str.append("requerimento.responsavelDeferimentoIndeferimentoIsencaoTaxa as \"requerimento.respDefIndefIsencaoTaxa\", ");
		str.append("requerimento.dataDeferimentoIndeferimentoIsencaoTaxa as \"requerimento.dataDeferimentoIndeferimentoIsencaoTaxa\", ");			
		str.append("requerimento.grupofacilitador as \"requerimento.grupofacilitador\", requerimento.temaTccFacilitador AS \"requerimento.temaTccFacilitador\", requerimento.assuntoTccFacilitador AS \"requerimento.assuntoTccFacilitador\", requerimento.avaliadorExternoFacilitador AS \"requerimento.avaliadorExternoFacilitador\", ");			
		str.append("situacaoRequerimentoDepartamento.situacao as \"situacaoRequerimentoDepartamento.situacao\", ");
		str.append("respDefIndefIsencaoTaxa.nome as \"respDefIndefIsencaoTaxa.nome\", ");
		// Cidade
		str.append("cidade.codigo AS \"cidade.codigo\", cidade.nome AS \"cidade.nome\", ");
		// Departamento
		str.append("departamento.codigo AS \"departamento.codigo\", departamento.nome AS \"departamento.nome\", ");
		// Matricula
		str.append("matricula.matricula AS \"matricula.matricula\", ");
		str.append("matricula.anoingresso AS \"matricula.anoingresso\", matricula.semestreingresso AS \"matricula.semestreingresso\", ");
		str.append("matricula.gradeCurricularAtual AS \"matricula.gradeCurricularAtual\", ");
		// Curso
		// str.append(" curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\", ");
		// Turma
		// Pessoa(Aluno)
		str.append("p1.codigo AS \"aluno.codigo\", p1.nome AS \"aluno.nome\", p1.cpf AS \"aluno.cpf\", p2.codigo AS \"pessoa.codigo\", p2.nome AS \"pessoa.nome\", ");
		str.append("p2.email AS \"pessoa.email\", p2.rg AS \"pessoa.rg\", p2.cpf AS \"pessoa.cpf\",  p2.telefoneres AS \"pessoa.telefoneres\",  ");
		str.append("p2.telefonerecado AS \"pessoa.telefonerecado\", p2.telefonecomer AS \"pessoa.telefonecomer\", p2.celular AS \"pessoa.celular\", ");
		str.append("p2.registroacademico AS \"pessoa.registroacademico\", ");
		str.append("p1.registroacademico AS \"aluno.registroacademico\", ");
		// Tipo Requerimento
		str.append("tipoRequerimento.codigo AS \"tipoRequerimento.codigo\", tipoRequerimento.nome AS \"tipoRequerimento.nome\", tipoRequerimento.tipo AS \"tipoRequerimento.tipo\", ");
		str.append("tipoRequerimento.situacaoMatriculaAtiva AS \"tipoRequerimento.situacaoMatriculaAtiva\", ");
		str.append("tipoRequerimento.situacaoMatriculaPreMatriculada AS \"tipoRequerimento.situacaoMatriculaPreMatriculada\", ");
		str.append("tipoRequerimento.situacaoMatriculaCancelada AS \"tipoRequerimento.situacaoMatriculaCancelada\", ");
		str.append("tipoRequerimento.situacaoMatriculaTrancada AS \"tipoRequerimento.situacaoMatriculaTrancada\", ");
		str.append("tipoRequerimento.situacaoMatriculaAbandonada AS \"tipoRequerimento.situacaoMatriculaAbandonada\", ");
		str.append("tipoRequerimento.situacaoMatriculaTransferida AS \"tipoRequerimento.situacaoMatriculaTransferida\", ");
		str.append("tipoRequerimento.situacaoMatriculaFormada AS \"tipoRequerimento.situacaoMatriculaFormada\", ");
		str.append("tipoRequerimento.verificarPendenciaBiblioteca AS \"tipoRequerimento.verificarPendenciaBiblioteca\", ");
		str.append("tipoRequerimento.verificarPendenciaFinanceira AS \"tipoRequerimento.verificarPendenciaFinanceira\", ");
		str.append("tipoRequerimento.verificarPendenciaBibliotecaAtraso AS \"tipoRequerimento.verificarPendenciaBibliotecaAtraso\", ");
		str.append("tipoRequerimento.verificarPendenciaFinanceiraAtraso AS \"tipoRequerimento.verificarPendenciaFinanceiraAtraso\", ");
		str.append("tipoRequerimento.verificarPendenciaDocumentacao AS \"tipoRequerimento.verificarPendenciaDocumentacao\", ");
		str.append("tipoRequerimento.verificarPendenciaEnade AS \"tipoRequerimento.verificarPendenciaEnade\", ");
		str.append("tipoRequerimento.verificarPendenciaEstagio AS \"tipoRequerimento.verificarPendenciaEstagio\", ");
		str.append("tipoRequerimento.verificarPendenciaAtividadeComplementar AS \"tipoRequerimento.verificarPendenciaAtividadeComplementar\", ");
		str.append("tipoRequerimento.requerimentoVisaoAlunoApresImprimirDeclaracao AS \"tipoRequerimento.requerimentoVisaoAlunoApresImprimirDeclaracao\", ");
		str.append("tipoRequerimento.cobrarApartirVia AS \"tipoRequerimento.cobrarApartirVia\", ");
		str.append("tipoRequerimento.tipoControleCobrancaViaRequerimento AS \"tipoRequerimento.tipoControleCobrancaViaRequerimento\", ");
		str.append("tipoRequerimento.taxa AS \"tipoRequerimento.taxa\", ");
		str.append("tipoRequerimento.deferirAutomaticamente AS \"tipoRequerimento.deferirAutomaticamente\", ");
		str.append("tipoRequerimento.assinarDigitalmenteDeclaracoesGeradasNoRequerimento AS \"tipoRequerimento.assinarDigitalmenteDeclaracoes\", ");
		str.append("tipoRequerimento.tramitaEntreDepartamentos as \"tipoRequerimento.tramitaEntreDepartamentos\", tipoRequerimento.permitirUploadArquivo AS \"tipoRequerimento.permitirUploadArquivo\", ");
		str.append("tipoRequerimento.permitirInformarEnderecoEntrega AS \"tipoRequerimento.permitirInformarEnderecoEntrega\", ");
		str.append("tipoRequerimento.qtdDiasVencimentoRequerimento AS \"tipoRequerimento.qtdDiasVencimentoRequerimento\", ");
		str.append("tipoRequerimento.requerimentoSituacaoFinanceiroVisaoAluno AS \"tipoRequerimento.requerimentoSituacaoFinanceiroVisaoAluno\", ");
		str.append("tipoRequerimento.questionario AS \"tipoRequerimento.questionario\", tipoRequerimento.orientacao as \"tipoRequerimento.orientacao\", ");
		str.append("tipoRequerimento.textoPadrao AS \"tipoRequerimento.textoPadrao\", tipoRequerimento.textoPadraoContratoEstagio AS \"tipoRequerimento.textoPadraoContratoEstagio\", tipoRequerimento.mensagemAlerta AS \"tipoRequerimento.mensagemAlerta\", ");
		str.append("tipoRequerimento.tipoUploadArquivo AS \"tipoRequerimento.tipoUploadArquivo\", tipoRequerimento.extensaoArquivo AS \"tipoRequerimento.extensaoArquivo\", ");
		str.append("tipoRequerimento.orientacaoUploadArquivo AS \"tipoRequerimento.orientacaoUploadArquivo\", ");
		str.append("tipoRequerimento.verificarPendenciaApenasMatriculaRequerimento AS \"tipoRequerimento.verificarPendenciaApenasMatriculaRequerimento\", ");
		str.append("tipoRequerimento.requerAutorizacaoPagamento AS \"tipoRequerimento.requerAutorizacaoPagamento\", ");
		str.append("tipoRequerimento.permiteDeferirAguardandoAutorizacaoPagamento AS \"tipoRequerimento.permiteDeferirAguardandoAutorizacaoPagamento\", ");
		str.append("tipoRequerimento.considerarDiasUteis AS \"tipoRequerimento.considerarDiasUteis\", ");
		str.append("tipoRequerimento.realizarIsencaoTaxaReposicaoMatriculaAposDataAula, ");
		str.append("tipoRequerimento.quantidadeDiasAntesAulaPermitirExclusaoRequerimentoReposicao, ");
		str.append("tipoRequerimento.permitirSolicitacaoIsencaoTaxaRequerimento, ");
		str.append("tipoRequerimento.solicitarAnexoComprovanteIsencaoTaxaRequerimento, ");
		str.append("tipoRequerimento.orientacaoDocumentoComprovanteIsencaoTaxaRequerimento, ");
		str.append("tipoRequerimento.permiteIncluirDisciplinaPorEquivalencia, ");
		str.append("tipoRequerimento.permiteIncluirReposicaoTurmaOutroCurso, ");
		str.append("tipoRequerimento.permiteIncluirReposicaoTurmaOutraUnidade, ");
		str.append("tipoRequerimento.permitirReposicaoComChoqueHorario, ");
		str.append("tipoRequerimento.permitirAlunoRejeitarDocumento, ");
		str.append("tipoRequerimento.registrarTrancamentoProximoSemestre, ");
		str.append("tipoRequerimento.bloquearQuantidadeRequerimentoAbertosSimultaneamente, tipoRequerimento.quantidadeLimiteRequerimentoAbertoSimultaneamente, ");
		str.append("tipoRequerimento.considerarBloqueioSimultaneoRequerimentoDeferido, tipoRequerimento.considerarBloqueioSimultaneoRequerimentoIndeferido, ");
		str.append("tipoRequerimento.permitirImpressaoHistoricoVisaoAluno, tipoRequerimento.nivelEducacional, tipoRequerimento.layoutHistoricoApresentar, ");
		str.append("tipoRequerimento.aprovadoSituacaoHistorico, tipoRequerimento.reprovadoSituacaoHistorico, tipoRequerimento.trancadoSituacaoHistorico, ");
		str.append("tipoRequerimento.cursandoSituacaoHistorico, tipoRequerimento.abandonoCursoSituacaoHistorico, tipoRequerimento.transferidoSituacaoHistorico, ");
		str.append("tipoRequerimento.canceladoSituacaoHistorico, tipoRequerimento.assinarDigitalmenteHistorico,  ");

		str.append("tipoRequerimento.abriroutrorequerimentoaodeferirestetiporequerimento, ");
		str.append("tipoRequerimento.tiporequerimentoabrirdeferimento, ");
		str.append("tipoRequerimento.permitirAlterarDataPrevisaoConclusaoRequerimento, ");
		str.append("tipoRequerimento.deferirAutomaticamenteTrancamento, ");
		str.append("tipoRequerimento.percentualIntegralizacaoCurricularInicial, ");
		str.append("tipoRequerimento.percentualIntegralizacaoCurricularFinal, ");
		str.append("tipoRequerimento.registrarAproveitamentoDisciplinaTCC, ");
		str.append("tipoRequerimento.ano as \"tipoRequerimento.ano\", ");
		str.append("tipoRequerimento.semestre as \"tipoRequerimento.semestre\", ");
		str.append("tipoRequerimento.qtdemaximaindeferidoaproveitamento AS \"tipoRequerimento.qtdemaximaindeferidoaproveitamento\", ");
		str.append("tipoRequerimento.considerarTodasMatriculasAlunoValidacaoAberturaSimultanea, tipoRequerimento.registrarTransferenciaProximoSemestre AS \"tipoRequerimento.registrarTransferenciaProximoSemestre\", ");
		str.append("tipoRequerimento.enviarnotificacaorequerente as \"enviarnotificacaorequerente\", tiporequerimento.campoafastamento as \"campoafastamento\", ");
		str.append("tipoRequerimento.utilizarmensagemdeferimentoexclusivo, tipoRequerimento.utilizarmensagemindeferimentoexclusivo, tipoRequerimento.tipoAluno AS \"tipoRequerimento.tipoAluno\", ");

		// Unidade de Ensino
		str.append("unidadeEnsino.codigo AS \"unidadeEnsino.codigo\", unidadeEnsino.nome AS \"unidadeEnsino.nome\", unidadeEnsino.numero AS \"unidadeEnsino.numero\", unidadeEnsino.setor AS \"unidadeEnsino.setor\", ");
		// Responsável Recebimento
		str.append("responsavelRecebimento.codigo AS \"responsavelRecebimento.codigo\", responsavelRecebimento.nome AS \"responsavelRecebimento.nome\", ");
		str.append("requerimento.dataRecebimentoDocRequerido AS \"responsavelRecebimento.dataRecebimentoDocRequerido\", ");
		// Responsável Boleto
		str.append("responsavelBoleto.codigo AS \"responsavelBoleto.codigo\", responsavelBoleto.nome AS \"responsavelBoleto.nome\", ");
		str.append("requerimento.dataEmissaoBoleto AS \"responsavelBoleto.dataEmissaoBoleto\", requerimento.justificativaTrancamento as \"requerimento.justificativaTrancamento\", requerimento.motivoCancelamentoTrancamento as \"requerimento.motivoCancelamentoTrancamento\", ");
		// Centro Receita
		str.append("centroReceita.codigo AS \"centroReceita.codigo\", centroReceita.descricao AS \"centroReceita.descricao\", ");
		// Conta Corrente
		str.append("contaCorrente.codigo AS \"contaCorrente.codigo\", contaCorrente.numero AS \"contaCorrente.numero\", ");
		// Arquivo
		str.append("arquivo.codigo AS \"arquivo.codigo\", arquivo.nome AS \"arquivo.nome\", ");
		str.append("arquivo.descricao AS \"arquivo.descricao\", arquivo.extensao AS \"arquivo.extensao\", ");
		str.append("arquivo.dataUpload AS \"arquivo.dataUpload\", arquivo.responsavelUpload AS \"arquivo.responsavelUpload\", ");
		str.append("arquivo.codOrigem AS \"arquivo.codOrigem\", arquivo.nivelEducacional AS \"arquivo.nivelEducacional\", ");
		str.append("arquivo.cpfAlunoDocumentacao AS \"arquivo.cpfAlunoDocumentacao\", arquivo.cpfRequerimento AS \"arquivo.cpfRequerimento\", ");
		str.append("arquivo.origem AS \"arquivo.origem\", arquivo.dataIndisponibilizacao AS \"arquivo.dataIndisponibilizacao\", ");
		str.append("arquivo.pastaBaseArquivo AS \"arquivo.pastaBaseArquivo\", arquivo.turma AS \"arquivo.turma\", ");
		str.append("arquivo.professor AS \"arquivo.professor\", arquivo.disciplina AS \"arquivo.disciplina\", ");
		str.append("estado.codigo AS \"estado.codigo\", estado.nome AS \"estado.nome\", estado.sigla AS \"estado.sigla\", ");
		str.append(" case when  (periodoletivo.codigo is null or periodoletivo.codigo = 0) then (periodoletivomatricula.codigo) else periodoletivo.codigo end AS \"periodoletivo.codigo\", ");
		str.append(" case when  (periodoletivo.codigo is null or periodoletivo.codigo = 0) then (periodoletivomatricula.periodoletivo) else periodoletivo.periodoletivo end AS \"periodoletivo.periodoletivo\", ");
		str.append("turma.codigo AS \"turma.codigo\", turma.identificadorturma AS \"turma.identificadorturma\", ");
		str.append("matriculaperiodo.ano, matriculaperiodo.semestre, matriculaperiodo.situacaomatriculaperiodo AS situacaomatriculaperiodo, matriculaperiodo.codigo AS \"matriculaperiodo.codigo\", ");
		str.append("turmaMatriculaPeriodo.codigo as codigoTurmaMatriculaPeriodo, turmaMatriculaPeriodo.identificadorturma as identificadorTurmaMatriculaPeriodo, cursoMatriculaPeriodo.codigo as codigoCursoMatriculaPeriodo, cursoMatriculaPeriodo.nome as nomeCursoMatriculaPeriodo, ");
		str.append("unidadeEnsinoTransferenciaInterna.codigo as unidadeEnsinoTransferenciaInterna_codigo, unidadeEnsinoTransferenciaInterna.nome as unidadeEnsinoTransferenciaInterna_nome, ");
		str.append("cursoTransferenciaInterna.codigo as cursoTransferenciaInterna_codigo, cursoTransferenciaInterna.nome as cursoTransferenciaInterna_nome, ");
		str.append("turnoTransferenciaInterna.codigo as turnoTransferenciaInterna_codigo, turnoTransferenciaInterna.nome as turnoTransferenciaInterna_nome,  ");
		str.append(" disciplinaCursada.codigo as disciplinaCursada_codigo, disciplinaCursada.nome disciplinaCursada_nome, mapaEquivalenciaDisciplinaCursada.cargahoraria as disciplinaCursada_cargahoraria, ");
		str.append(" salaLocalAula.sala as salaLocalAula_sala, localAula.local as localAula_local, requerimento.cargaHorariaDisciplina as cargaHorariaDisciplina, ");
		str.append(" requerimento.dataTerminoAula as requerimento_dataTerminoAula, requerimento.dataInicioAula as requerimento_dataInicioAula, ");
		str.append(" requerimento.salaLocalAula,  requerimento.mapaEquivalenciaDisciplina, requerimento.mapaEquivalenciaDisciplinaCursada, requerimento.mensagemChoqueHorario, ");
		str.append("requerimento.disciplinaPorEquivalencia, unidadereposicao.nome as unidadereposicao, ");
		str.append(" turmareposicao.identificadorTurma as turmareposicao_identificadorTurma, turmareposicao.codigo as turmareposicao_codigo, tipoRequerimento.orientacaoAtendente as \"tipoRequerimento.orientacaoAtendente\", requerimento.motivoNaoAceiteCertificado as \"requerimento.motivoNaoAceiteCertificado\", requerimento.formatoCertificadoSelecionado as \"requerimento.formatoCertificadoSelecionado\", matriculaPeriodo.periodoletivomatricula as \"matriculaPeriodo.periodoletivomatricula\", tipoRequerimento.registrarFormaturaAoRealizarImpressaoCerticadoDigital, requerimento.formatoCertificadoSelecionado as \"requerimento.formatoCertificadoSelecionado\", tipoRequerimento.certificadoImpresso as \"tipoRequerimento.certificadoImpresso\", requerimento.dataUltimaImpressao as \"requerimento.dataUltimaImpressao\", ");
		str.append(" requerimento.dataUltimaAlteracao as \"requerimento.dataUltimaAlteracao\", case when coalesce(p1.nome, '') <> '' then p1.nome else p2.nome end as \"nomerequerente.ordenar\", requerimento.turmabase as \"requerimento.turmabase\", cidtiporequerimento.codcid as codcid, cidtiporequerimento.descricao as descricaocid, cidtiporequerimento.codigo as codigocid, requerimento.cid, TipoRequerimento.bloqueiosimultaneopelo as \"TipoRequerimento.bloqueiosimultaneopelo\", ");
		str.append("cursoAluno.codigo AS \"cursoAluno.codigo\", cursoAluno.nome AS \"cursoAluno.nome\", cursoAluno.periodicidade AS \"cursoAluno.periodicidade\"  ");
		if (joinCTE) {
			str.append(" , cte_requerimento.totalregistros ");
			str.append("FROM cte_requerimento INNER JOIN Requerimento ON cte_requerimento.codigo = Requerimento.codigo ");
		} else {
			str.append("FROM Requerimento ");
		}
		str.append("LEFT JOIN Funcionario on Funcionario.codigo = Requerimento.funcionario  ");
		str.append("LEFT JOIN Pessoa as pessoaFuncionario on Funcionario.pessoa = pessoaFuncionario.codigo  ");
		str.append("LEFT JOIN TipoRequerimento on Requerimento.tipoRequerimento = TipoRequerimento.codigo  ");
		str.append("LEFT JOIN UnidadeEnsino on requerimento.unidadeEnsino = unidadeEnsino.codigo ");
		str.append("LEFT JOIN Matricula on matricula.matricula = requerimento.matricula ");
		str.append("LEFT JOIN Turma on turma.codigo = requerimento.turma ");
		str.append("LEFT JOIN MatriculaPeriodo on MatriculaPeriodo.matricula = matricula.matricula ");
		str.append("and case when Requerimento.matriculaperiodo is not null then  Requerimento.matriculaperiodo = matriculaperiodo.codigo else ");
		str.append(" case when turma.codigo != 0 then matriculaperiodo.turma = turma.codigo else ");
		str.append("matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (MatriculaPeriodo.ano||'/'|| MatriculaPeriodo.semestre) desc, mp.periodoletivomatricula desc limit 1) end end ");
		// str.append(" and MatriculaPeriodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre) desc limit 1) ");
		str.append("LEFT JOIN periodoletivo on periodoletivo.codigo = turma.periodoletivo ");
		str.append("LEFT JOIN periodoletivo periodoletivomatricula on periodoletivomatricula.codigo = matriculaperiodo.periodoletivomatricula ");
		str.append("LEFT JOIN departamento on departamento.codigo = requerimento.departamentoResponsavel ");
		str.append("LEFT JOIN arquivo on arquivo.codigo = requerimento.arquivo ");
		str.append("LEFT JOIN Pessoa p1 on p1.codigo = matricula.aluno ");
		str.append("LEFT JOIN Pessoa p2 on p2.codigo = requerimento.pessoa ");
		str.append("LEFT JOIN cidade on cidade.codigo = requerimento.cidade ");
		str.append("LEFT JOIN estado on estado.codigo = cidade.estado ");
		str.append("LEFT JOIN usuario responsavelRecebimento on responsavelRecebimento.codigo = requerimento.responsavelRecebimentoDocRequerido ");
		str.append("LEFT JOIN usuario responsavelBoleto on responsavelBoleto.codigo = requerimento.responsavelEmissaoBoleto ");
		str.append("LEFT JOIN usuario responsavel on responsavel.codigo = requerimento.responsavel ");
		str.append("LEFT JOIN centroReceita on centroReceita.codigo = requerimento.centroReceita ");
		str.append("LEFT JOIN contaCorrente on contaCorrente.codigo = requerimento.contaCorrente ");
		str.append("LEFT JOIN contareceber on contareceber.codigo = requerimento.contareceber ");
		str.append("LEFT JOIN curso on curso.codigo = requerimento.curso ");
		str.append("LEFT JOIN turno on turno.codigo = requerimento.turno ");
		str.append("LEFT JOIN disciplina on disciplina.codigo = requerimento.disciplina ");
		str.append("LEFT JOIN turma as turmaMatriculaPeriodo on turmaMatriculaPeriodo.codigo = matriculaperiodo.turma ");
		str.append("LEFT JOIN curso as cursoMatriculaPeriodo on cursoMatriculaPeriodo.codigo = turmaMatriculaPeriodo.curso ");
		str.append("LEFT JOIN UnidadeEnsino unidadeEnsinoTransferenciaInterna on requerimento.unidadeEnsinoTransferenciaInterna = unidadeEnsinoTransferenciaInterna.codigo ");
		str.append("LEFT JOIN Curso cursoTransferenciaInterna on requerimento.cursoTransferenciaInterna = cursoTransferenciaInterna.codigo ");
		str.append("LEFT JOIN Turno turnoTransferenciaInterna on requerimento.turnoTransferenciaInterna = turnoTransferenciaInterna.codigo ");		
		str.append("LEFT JOIN situacaoRequerimentoDepartamento on requerimento.situacaoRequerimentoDepartamento = situacaoRequerimentoDepartamento.codigo ");		
		str.append("LEFT JOIN usuario as respDefIndefIsencaoTaxa on requerimento.responsavelDeferimentoIndeferimentoIsencaoTaxa = respDefIndefIsencaoTaxa.codigo ");
		str.append("LEFT JOIN mapaEquivalenciaDisciplinaCursada on requerimento.mapaEquivalenciaDisciplinaCursada = mapaEquivalenciaDisciplinaCursada.codigo ");
		str.append("LEFT JOIN disciplina as disciplinaCursada on mapaEquivalenciaDisciplinaCursada.disciplina = disciplinaCursada.codigo ");
		str.append("LEFT JOIN salaLocalAula on requerimento.salaLocalAula = salaLocalAula.codigo ");
		str.append("LEFT JOIN localAula on salaLocalAula.localAula = localAula.codigo ");
		str.append("LEFT JOIN Turma as turmareposicao on turmareposicao.codigo = requerimento.turmareposicao ");
		str.append("LEFT JOIN unidadeensino as unidadereposicao on unidadereposicao.codigo = turmareposicao.unidadeensino ");
		str.append("LEFT JOIN cidtiporequerimento on cidtiporequerimento.codigo = requerimento.codigocid ");
		str.append("LEFT JOIN curso cursoAluno ON cursoAluno.codigo = matricula.curso ");
		return str;
	}

	/**
	 * Responsável por realizar uma consulta de <code>UnidadeEnsino</code>
	 * através do valor do atributo <code>String nome</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>UnidadeEnsinoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 * 
	 */
	private StringBuffer getSQLPadraoConsultaBasica() {

		StringBuffer str = new StringBuffer();
		str.append("SELECT DISTINCT requerimento.codigo,  requerimento.nomeRequerente AS \"requerimento.nomeRequerente\", requerimento.questionario, requerimento.curso, curso.nome as nomecurso,  requerimento.situacaofinanceira, ");
		str.append("requerimento.dataPrevistaFinalizacao AS \"requerimento.dataPrevistaFinalizacao\", p1.nome AS \"aluno.nome\", p1.codigo AS \"aluno.codigo\", p2.nome AS \"pessoa.nome\", p2.codigo AS \"pessoa.codigo\", ");
		str.append("requerimento.data, requerimento.ordemExecucaoTramiteDepartamento as \"requerimento.ordemExecucaoTramiteDepartamento\", requerimento.situacao, requerimento.valorAdicional, tipoRequerimento.nome AS \"tipoRequerimento.nome\", tipoRequerimento.tipo AS \"tipoRequerimento.tipo\", requerimento.dataFinalizacao as \"requerimento.DataFinalizacao\" , requerimento.dataUltimaImpressao as \"requerimento.dataUltimaImpressao\", ");
		str.append("unidadeEnsino.nome AS \"unidadeEnsino.nome\", requerimento.taxaIsentaPorQtdeVia as \"requerimento.taxaIsentaPorQtdeVia\", requerimento.taxa as \"requerimento.taxa\", requerimento.numeroVia as \"requerimento.numeroVia\", requerimento.tipopessoa as \"requerimento.tipopessoa\", ");
		str.append("requerimento.situacaoRequerimentoDepartamento as \"requerimento.situacaoRequerimentoDepartamento\", ");
		str.append("situacaoRequerimentoDepartamento.situacao as \"situacaoRequerimentoDepartamento.situacao\", ");
		str.append("requerimento.situacaoIsencaoTaxa as \"requerimento.situacaoIsencaoTaxa\", ");
		str.append("requerimento.valor as \"requerimento.valor\", ");
		str.append("requerimento.tipoDesconto as \"requerimento.tipoDesconto\", ");
		str.append("requerimento.percDesconto as \"requerimento.percDesconto\", ");
		str.append("requerimento.valorDesconto as \"requerimento.valorDesconto\", ");
		str.append("requerimento.valorAdicional as \"requerimento.valorAdicional\", ");
		str.append("requerimento.matricula as \"requerimento.matricula\", ");
		str.append("requerimento.justificativaTrancamento AS \"requerimento.justificativaTrancamento\", ");
		str.append("requerimento.motivoCancelamentoTrancamento AS \"requerimento.motivoCancelamentoTrancamento\", requerimento.dataafastamentoinicio AS \"requerimento.dataafastamentoinicio\", requerimento.dataafastamentofim AS \"requerimento.dataafastamentofim\", ");
		str.append("tipoRequerimento.questionario AS \"tipoRequerimento.questionario\", ");
		str.append("tipoRequerimento.textoPadrao AS \"tipoRequerimento.textoPadrao\", ");
		str.append("tipoRequerimento.situacaoMatriculaAtiva AS \"tipoRequerimento.situacaoMatriculaAtiva\", ");
		str.append("tipoRequerimento.situacaoMatriculaPreMatriculada AS \"tipoRequerimento.situacaoMatriculaPreMatriculada\", ");
		str.append("tipoRequerimento.situacaoMatriculaCancelada AS \"tipoRequerimento.situacaoMatriculaCancelada\", ");
		str.append("tipoRequerimento.situacaoMatriculaTrancada AS \"tipoRequerimento.situacaoMatriculaTrancada\", ");
		str.append("tipoRequerimento.situacaoMatriculaAbandonada AS \"tipoRequerimento.situacaoMatriculaAbandonada\", ");
		str.append("tipoRequerimento.situacaoMatriculaTransferida AS \"tipoRequerimento.situacaoMatriculaTransferida\", ");
		str.append("tipoRequerimento.situacaoMatriculaFormada AS \"tipoRequerimento.situacaoMatriculaFormada\", ");
		str.append("tipoRequerimento.verificarPendenciaBiblioteca AS \"tipoRequerimento.verificarPendenciaBiblioteca\", ");
		str.append("tipoRequerimento.verificarPendenciaFinanceira AS \"tipoRequerimento.verificarPendenciaFinanceira\", ");
		str.append("tipoRequerimento.verificarPendenciaBibliotecaAtraso AS \"tipoRequerimento.verificarPendenciaBibliotecaAtraso\", ");
		str.append("tipoRequerimento.verificarPendenciaFinanceiraAtraso AS \"tipoRequerimento.verificarPendenciaFinanceiraAtraso\", ");
		str.append("tipoRequerimento.verificarPendenciaDocumentacao AS \"tipoRequerimento.verificarPendenciaDocumentacao\", ");
		str.append("tipoRequerimento.verificarPendenciaEnade AS \"tipoRequerimento.verificarPendenciaEnade\", ");
		str.append("tipoRequerimento.verificarPendenciaEstagio AS \"tipoRequerimento.verificarPendenciaEstagio\", ");
		str.append("tipoRequerimento.verificarPendenciaAtividadeComplementar AS \"tipoRequerimento.verificarPendenciaAtividadeComplementar\", ");
		str.append("tipoRequerimento.cobrarApartirVia AS \"tipoRequerimento.cobrarApartirVia\", ");
		str.append("tipoRequerimento.tipoControleCobrancaViaRequerimento AS \"tipoRequerimento.tipoControleCobrancaViaRequerimento\", ");
		str.append("tipoRequerimento.requerimentoVisaoAlunoApresImprimirDeclaracao AS \"tipoRequerimento.requerimentoVisaoAlunoApresImprimirDeclaracao\" ,");
		str.append("tipoRequerimento.qtdeDiasDisponivel AS \"tipoRequerimento.qtdeDiasDisponivel\" ,");
		str.append("tipoRequerimento.qtdeDiasAposPrimeiraImpressao AS \"tipoRequerimento.qtdeDiasAposPrimeiraImpressao\" ,");
		str.append("tipoRequerimento.taxa AS \"tipoRequerimento.taxa\", ");
		str.append("tipoRequerimento.assinarDigitalmenteDeclaracoesGeradasNoRequerimento AS \"tipoRequerimento.assinarDigitalmenteDeclaracoes\", ");
		str.append("tipoRequerimento.realizarIsencaoTaxaReposicaoMatriculaAposDataAula, ");
		str.append("tipoRequerimento.quantidadeDiasAntesAulaPermitirExclusaoRequerimentoReposicao, ");
		str.append("tipoRequerimento.permitirSolicitacaoIsencaoTaxaRequerimento, ");
		str.append("tipoRequerimento.solicitarAnexoComprovanteIsencaoTaxaRequerimento, ");
		str.append("tipoRequerimento.orientacaoDocumentoComprovanteIsencaoTaxaRequerimento, ");
		str.append("tipoRequerimento.requerimentoSituacaoFinanceiroVisaoAluno, ");
		str.append("tipoRequerimento.codigo AS \"tipoRequerimento.codigo\", ");
		str.append("tipoRequerimento.bloquearQuantidadeRequerimentoAbertosSimultaneamente, tipoRequerimento.quantidadeLimiteRequerimentoAbertoSimultaneamente, ");
		str.append("tipoRequerimento.considerarBloqueioSimultaneoRequerimentoDeferido, tipoRequerimento.considerarBloqueioSimultaneoRequerimentoIndeferido, tiporequerimento.considerartodasmatriculasalunovalidacaoaberturasimultanea, tiporequerimento.campoafastamento AS \"campoafastamento\",  ");
		str.append("unidadeEnsino.codigo AS \"unidadeEnsino.codigo\", requerimento.cep AS \"requerimento.cep\", requerimento.endereco AS \"requerimento.endereco\", ");
		str.append("requerimento.setor AS \"requerimento.setor\", requerimento.numero AS \"requerimento.numero\", requerimento.complemento AS \"requerimento.complemento\", ");
		str.append("requerimento.cidade AS \"requerimento.cidade\", requerimento.arquivo AS \"requerimento.arquivo\", requerimento.responsavel AS \"requerimento.responsavel\", ");
		str.append("requerimento.visaoGerado AS \"requerimento.visaoGerado\", requerimento.dataUltimaAlteracao AS \"requerimento.dataUltimaAlteracao\", ");
		str.append("funcionario.codigo AS \"funcionario.codigo\", pessoaFuncionario.codigo AS \"pessoaFuncionario.codigo\", pessoaFuncionario.nome AS \"pessoaFuncionario.nome\", pessoaFuncionario.email AS \"pessoaFuncionario.email\", ");
		str.append(" CASE WHEN requerimento.situacao IN ('FD','FI') THEN 2 ELSE 1 END ");
		str.append(", case when (p1.nome is not null and p1.nome <> '') then (p1.nome) else (p2.nome) end as \"requerimento.nomeSolicitante\", tipoRequerimento.orientacaoAtendente as \"tipoRequerimento.orientacaoAtendente\", requerimento.formatoCertificadoSelecionado as \"requerimento.formatoCertificadoSelecionado\", ");
		str.append(" exists(select interacaoRequerimentoHistorico.codigo from interacaoRequerimentoHistorico inner join requerimentohistorico on requerimentohistorico.codigo = interacaoRequerimentoHistorico.requerimentohistorico inner join usuario on usuario.codigo = interacaoRequerimentoHistorico.usuarioInteracao and usuario.pessoa = requerimento.pessoa and interacaojalida = false ");
		str.append(" where  requerimento.codigo = requerimentohistorico.requerimento  limit 1 ) as \"possuiInteracaoRequerenteNaoLida\" ,");
		str.append(" exists(select interacaoRequerimentoHistorico.codigo from interacaoRequerimentoHistorico inner join requerimentohistorico on requerimentohistorico.codigo = interacaoRequerimentoHistorico.requerimentohistorico inner join usuario on usuario.codigo = interacaoRequerimentoHistorico.usuarioInteracao	and usuario.pessoa != requerimento.pessoa and interacaojalida = false ");
		str.append(" where requerimento.codigo = requerimentohistorico.requerimento limit 1 ) as \"possuiInteracaoAtendenteNaoLida\", ");
		str.append(" contareceber.codigo AS \"contareceber.codigo\", ");
		str.append(" requerimento.turmabase AS \"requerimento.turmabase\", requerimento.cid, tipoRequerimento.bloqueiosimultaneopelo as \"TipoRequerimento.bloqueiosimultaneopelo\" ");
		str.append("FROM Requerimento ");
		str.append("INNER JOIN TipoRequerimento on Requerimento.tipoRequerimento = TipoRequerimento.codigo  ");
		str.append("INNER JOIN UnidadeEnsino on requerimento.unidadeEnsino = unidadeEnsino.codigo ");
		str.append("LEFT JOIN Funcionario on Funcionario.codigo = Requerimento.funcionario  ");
		str.append("LEFT JOIN Pessoa as pessoaFuncionario on Funcionario.pessoa = pessoaFuncionario.codigo  ");
		str.append("LEFT JOIN Matricula on matricula.matricula = requerimento.matricula ");
		// str.append("LEFT JOIN MatriculaPeriodo on MatriculaPeriodo.matricula = Matricula.matricula ");
		str.append("LEFT JOIN Pessoa p1 on p1.codigo = matricula.aluno ");
		str.append("LEFT JOIN Pessoa p2 on p2.codigo = requerimento.pessoa ");
		str.append("LEFT JOIN Curso on curso.codigo = requerimento.curso ");
		str.append("LEFT JOIN situacaoRequerimentoDepartamento on requerimento.situacaoRequerimentoDepartamento = situacaoRequerimentoDepartamento.codigo ");
		str.append("LEFT JOIN contareceber on contareceber.codigo = requerimento.contareceber ");
		
		return str;
	}

	public String montaEnderecoRelatorioRequerimento(Integer codRequerimento, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select UnidadeEnsino.endereco || ' - ' || UnidadeEnsino.numero || ' - ' || UnidadeEnsino.setor || ' - ' ");
		sqlStr.append("|| cidade.nome  ||' - ' || estado.sigla || ' - '|| ");
		sqlStr.append("UnidadeEnsino.site ||' - ' || UnidadeEnsino.email as End from UnidadeEnsino ");
		sqlStr.append("inner join cidade on UnidadeEnsino.cidade = cidade.codigo ");
		sqlStr.append("inner join estado on estado.codigo = cidade.estado ");
		sqlStr.append("inner join requerimento on requerimento.unidadeensino = unidadeensino.codigo ");
		sqlStr.append("where requerimento.codigo = ").append(codRequerimento);
		SqlRowSet dadoSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (dadoSQL.next()) {
			return dadoSQL.getString("end");
		} else {
			return "";
		}

	}

	public List<EstatisticaRequerimentoVO> consultaRapidaRequerimentosPendentes(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT count(tiporequerimento.nome) AS \"quantidadeTipoRequerimento\", tipoRequerimento.codigo AS \"tipoRequerimento.codigo\", tipoRequerimento.nome AS \"tipoRequerimento\" ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(", unidadeEnsino.nome AS \"unidadeEnsino.nome\", unidadeEnsino.codigo AS \"unidadeEnsino.codigo\"  ");
		}
		sqlStr.append("FROM Requerimento ");
		sqlStr.append("LEFT JOIN TipoRequerimento on Requerimento.tipoRequerimento = TipoRequerimento.codigo  ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append("LEFT JOIN UnidadeEnsino on requerimento.unidadeEnsino = unidadeEnsino.codigo ");
		}
		sqlStr.append("WHERE (requerimento.situacao = 'PE' OR requerimento.situacao = 'EX') ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append("and Requerimento.unidadeEnsino = ").append(unidadeEnsino.intValue());
			sqlStr.append(" group by tipoRequerimento.nome, unidadeEnsino.nome, unidadeEnsino.codigo, tipoRequerimento.codigo ");
		} else {
			sqlStr.append(" group by tipoRequerimento.nome, tipoRequerimento.codigo ");
		}
		sqlStr.append("ORDER BY tipoRequerimento.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosEstatisticaRequerimentoVO(tabelaResultado, unidadeEnsino);
	}

	public List<RequerimentoVO> consultaRapidaRequerimentosPorCodigoTipoRequerimento(Integer unidadeEnsino, Integer codTipoRequerimento, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE tipoRequerimento.codigo = ").append(codTipoRequerimento.intValue()).append(" ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append("and Requerimento.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		sqlStr.append(" AND (requerimento.situacao  = 'PE' or requerimento.situacao = 'EX') ");
		sqlStr.append(" ORDER BY tipoRequerimento.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, configuracaoGeralSistemaVO);
	}

	public List<RequerimentoVO> consultaRapidaRequerimentoPorCodigo(Integer requerimento, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		return consultaRapidaRequerimentoPorCodigo(requerimento, unidadeEnsino, controlarAcesso, null, usuario, configuracaoGeralSistemaVO);
	}

	public List<RequerimentoVO> consultaRapidaRequerimentoPorCodigo(Integer requerimento, Integer unidadeEnsino, boolean controlarAcesso, String situacao, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE requerimento.codigo = ").append(requerimento);
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and Requerimento.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (situacao != null) {
			sqlStr.append(" and Requerimento.situacao ilike '");
			sqlStr.append(situacao);
			sqlStr.append("%'");
		}
		sqlStr.append(" ORDER BY requerimento.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, configuracaoGeralSistemaVO);
	}

	public List<RequerimentoVO> consultaRapidaRequerimentoPorCodigoAluno(Integer requerimento, Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {

		consultar(getIdEntidade(), controlarAcesso, usuario);

		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		if ((usuario.getIsApresentarVisaoCoordenador() || usuario.getIsApresentarVisaoProfessor())) {
			sqlStr.append(" WHERE pessoa.codigo = ").append(pessoa);
		} else if (pessoa != null && usuario.getIsApresentarVisaoAluno() || usuario.getIsApresentarVisaoPais() ) {
			sqlStr.append(" WHERE matricula.aluno = ");
			sqlStr.append(pessoa);
			/**
			 * Filtro comentado por este recurso só deve validar a abertura do mesmo, exemplo se o 
			 * requerimento for abertio pela visão administrativa deve ser possível o aluno acompanhar o tramite 
			 */
			//sqlStr.append(" and (TipoRequerimento.requerimentoVisaoAluno) ");
		}
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and Requerimento.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY CASE WHEN requerimento.situacao in ('FD', 'FI') then 2 else 1 END, requerimento.data");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, configuracaoGeralSistemaVO);
	}

	public List<RequerimentoVO> consultaRapidaRequerimentoPorData(Date dataInicio, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		return consultaRapidaRequerimentoPorData(dataInicio, dataFim, unidadeEnsino, null, controlarAcesso, usuario, configuracaoGeralSistemaVO);
	}

	public List<RequerimentoVO> consultaRapidaRequerimentoPorData(Date dataInicio, Date dataFim, Integer unidadeEnsino, String situacao, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (requerimento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("') ");
		sqlStr.append(" AND (requerimento.data <= '").append(Uteis.getDataJDBC(dataFim)).append("') ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and Requerimento.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (situacao != null) {
			sqlStr.append(" and Requerimento.situacao ilike '");
			sqlStr.append(situacao);
			sqlStr.append("%'");
		}
		sqlStr.append(" ORDER BY requerimento.data");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, configuracaoGeralSistemaVO);
	}

	public List<RequerimentoVO> consultaRapidaRequerimentoPorNomeTipoRequerimento(String nomeTipoRequerimento, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		return consultaRapidaRequerimentoPorNomeTipoRequerimento(nomeTipoRequerimento, unidadeEnsino, null, controlarAcesso, usuario, configuracaoGeralSistemaVO);
	}

	public List<RequerimentoVO> consultaRapidaRequerimentoPorNomeTipoRequerimento(String nomeTipoRequerimento, Integer unidadeEnsino, String situacao, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE tipoRequerimento.nome ilike('").append(nomeTipoRequerimento).append("%') ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and Requerimento.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (situacao != null) {
			sqlStr.append(" and Requerimento.situacao ilike '");
			sqlStr.append(situacao);
			sqlStr.append("%'");
		}
		sqlStr.append(" ORDER BY tipoRequerimento.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, configuracaoGeralSistemaVO);
	}

	public List<RequerimentoVO> consultaRapidaRequerimentoPorSituacaoRequerimento(String situacao, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (requerimento.situacao) like('").append(situacao).append("%') ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and Requerimento.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY tipoRequerimento.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, configuracaoGeralSistemaVO);
	}

	public List<RequerimentoVO> consultaRapidaRequerimentoPorSituacaoFinanceiraRequerimento(String situacao, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		return consultaRapidaRequerimentoPorSituacaoFinanceiraRequerimento(situacao, unidadeEnsino, null, controlarAcesso, usuario, configuracaoGeralSistemaVO);
	}

	public List<RequerimentoVO> consultaRapidaRequerimentoPorSituacaoFinanceiraRequerimento(String situacaoFinanceira, Integer unidadeEnsino, String situacao, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (requerimento.situacaoFinanceira) like('").append(situacaoFinanceira).append("%') ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and Requerimento.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (situacao != null) {
			sqlStr.append(" and Requerimento.situacao ilike '");
			sqlStr.append(situacao);
			sqlStr.append("%'");
		}
		sqlStr.append(" ORDER BY tipoRequerimento.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, configuracaoGeralSistemaVO);
	}

	public List<RequerimentoVO> consultaRapidaRequerimentoPorNomePessoa(String nomePessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		return consultaRapidaRequerimentoPorNomePessoa(nomePessoa, unidadeEnsino, null, controlarAcesso, usuario, configuracaoGeralSistemaVO);
	}

	public List<RequerimentoVO> consultaRapidaRequerimentoPorNomePessoa(String nomePessoa, Integer unidadeEnsino, String situacao, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE p2.nome ilike('").append(nomePessoa).append("%') ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and Requerimento.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (situacao != null) {
			sqlStr.append(" and Requerimento.situacao ilike '");
			sqlStr.append(situacao);
			sqlStr.append("%'");
		}
		sqlStr.append(" ORDER BY p2.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, configuracaoGeralSistemaVO);
	}

	public List<RequerimentoVO> consultaRapidaRequerimentoPorCpfPessoa(String cpfPessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		return consultaRapidaRequerimentoPorCpfPessoa(cpfPessoa, unidadeEnsino, null, controlarAcesso, usuario, configuracaoGeralSistemaVO);
	}

	public List<RequerimentoVO> consultaRapidaRequerimentoPorCpfPessoa(String cpfPessoa, Integer unidadeEnsino, String situacao, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (p2.cpf) like('").append(cpfPessoa).append("%') ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and Requerimento.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (situacao != null) {
			sqlStr.append(" and Requerimento.situacao ilike '");
			sqlStr.append(situacao);
			sqlStr.append("%'");
		}
		sqlStr.append(" ORDER BY p2.cpf");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, configuracaoGeralSistemaVO);
	}

	public List<RequerimentoVO> consultaRapidaRequerimentoPorMatricula(String matricula, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		return consultaRapidaRequerimentoPorMatricula(matricula, unidadeEnsino, null, controlarAcesso, usuario, configuracaoGeralSistemaVO);
	}

	public List<RequerimentoVO> consultaRapidaRequerimentoPorMatricula(String matricula, Integer unidadeEnsino, String situacao, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (matricula.matricula) like('").append(matricula).append("') ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and Requerimento.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (situacao != null) {
			sqlStr.append(" and Requerimento.situacao ilike '");
			sqlStr.append(situacao);
			sqlStr.append("%'");
		}
		sqlStr.append(" ORDER BY requerimento.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, configuracaoGeralSistemaVO);
	}
	
	public List<RequerimentoVO> consultaRapidaRequerimentoPorMatriculaPeriodo(Integer matriculaPeriodo, Integer unidadeEnsino, String situacao, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE Requerimento.matriculaperiodo = ").append(matriculaPeriodo).append(" ");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and Requerimento.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (situacao != null) {
			sqlStr.append(" and Requerimento.situacao ilike '");
			sqlStr.append(situacao);
			sqlStr.append("%'");
		}
		sqlStr.append(" ORDER BY requerimento.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, configuracaoGeralSistemaVO);
	}

	public List<EstatisticaRequerimentoVO> montarDadosEstatisticaRequerimentoVO(SqlRowSet tabelaResultado, Integer unidadeEnsino) throws Exception {
		List<EstatisticaRequerimentoVO> vetResultado = new ArrayList<EstatisticaRequerimentoVO>(0);
		while (tabelaResultado.next()) {
			EstatisticaRequerimentoVO obj = new EstatisticaRequerimentoVO();
			montarDadosEstatisticaRequerimento(obj, tabelaResultado, unidadeEnsino);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<RequerimentoVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		List<RequerimentoVO> vetResultado = new ArrayList<RequerimentoVO>(0);
		while (tabelaResultado.next()) {
			RequerimentoVO obj = new RequerimentoVO();
			montarDadosBasico(obj, tabelaResultado, configuracaoGeralSistemaVO);
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	public List<RequerimentoVO> montarDadosConsultaCompleta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<RequerimentoVO> vetResultado = new ArrayList<RequerimentoVO>(0);
		while (tabelaResultado.next()) {
			RequerimentoVO obj = new RequerimentoVO();
			montarDadosCompleto(obj, tabelaResultado, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
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
	private void montarDadosEstatisticaRequerimento(EstatisticaRequerimentoVO obj, SqlRowSet dadosSQL, Integer unidadeEnsino) throws Exception {
		// Dados do Estatistica Requerimento
		obj.setTipoRequerimento(dadosSQL.getString("tipoRequerimento"));
		obj.setCodigoTipoRequerimento(new Integer(dadosSQL.getInt("tipoRequerimento.codigo")));
		if (!unidadeEnsino.equals(0)) {
			obj.setUnidadeEnsino(new Integer(dadosSQL.getInt("unidadeEnsino.codigo")));
			obj.setNomeUnidade(dadosSQL.getString("unidadeEnsino.nome"));
		}
		obj.setQuantidadeTipoRequerimento(new Integer(dadosSQL.getInt("quantidadeTipoRequerimento")));
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
	private void montarDadosBasico(RequerimentoVO obj, SqlRowSet dadosSQL, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		// Dados do Requerimento
		obj.setNovoObj(false);
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNomeRequerente(dadosSQL.getString("requerimento.nomeRequerente"));
		obj.setData(dadosSQL.getTimestamp("data"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setSituacaoFinanceira(dadosSQL.getString("situacaofinanceira"));
		obj.getMatricula().getAluno().setCodigo(dadosSQL.getInt("aluno.codigo"));
		obj.getMatricula().getAluno().setNome(dadosSQL.getString("aluno.nome"));
		obj.getMatricula().setMatricula(dadosSQL.getString("requerimento.matricula"));
		obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getPessoa().setNome(dadosSQL.getString("pessoa.nome"));
		obj.setDataPrevistaFinalizacao(dadosSQL.getDate("requerimento.dataPrevistaFinalizacao"));
		obj.setCEP(dadosSQL.getString("requerimento.cep"));
		obj.setEndereco(dadosSQL.getString("requerimento.endereco"));
		obj.setSetor(dadosSQL.getString("requerimento.setor"));
		obj.setNumero(dadosSQL.getString("requerimento.numero"));
		obj.setValorAdicional(dadosSQL.getDouble("valorAdicional"));
		obj.setComplemento(dadosSQL.getString("requerimento.complemento"));
		obj.getCidade().setCodigo(dadosSQL.getInt("requerimento.cidade"));
		obj.getArquivoVO().setCodigo(dadosSQL.getInt("requerimento.arquivo"));
		obj.getQuestionarioVO().setCodigo(dadosSQL.getInt("questionario"));
		obj.getCurso().setCodigo(dadosSQL.getInt("curso"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("requerimento.responsavel"));
		obj.setVisaoGerado(dadosSQL.getString("requerimento.visaoGerado"));
		obj.setOrdemExecucaoTramiteDepartamento(dadosSQL.getInt("requerimento.ordemExecucaoTramiteDepartamento"));
		obj.setTaxaIsentaPorQtdeVia(dadosSQL.getBoolean("requerimento.taxaIsentaPorQtdeVia"));
//		obj.getTaxa().setCodigo(dadosSQL.getInt("requerimento.taxa"));
		obj.setNumeroVia(dadosSQL.getInt("requerimento.numeroVia"));
		obj.setNivelMontarDados(NivelMontarDados.BASICO);
		obj.setDataUltimaAlteracao(dadosSQL.getDate("requerimento.dataUltimaAlteracao"));
		obj.setDataUltimaImpressao(dadosSQL.getDate("requerimento.dataUltimaImpressao"));
		obj.setDataFinalizacao(dadosSQL.getDate("requerimento.dataFinalizacao"));
		obj.setValor(dadosSQL.getDouble("requerimento.valor"));
		obj.setPercDesconto(dadosSQL.getDouble("requerimento.percDesconto"));
		obj.setValorDesconto(dadosSQL.getDouble("requerimento.valorDesconto"));
		obj.setValorAdicional(dadosSQL.getDouble("requerimento.valorAdicional"));
		obj.setTipoDesconto(dadosSQL.getString("requerimento.tipoDesconto"));
		if (dadosSQL.getString("requerimento.tipoPessoa") == null || dadosSQL.getString("requerimento.tipoPessoa").trim().isEmpty()) {
			obj.setTipoPessoa(obj.getMatricula().getMatricula().trim().isEmpty() ? TipoPessoa.REQUERENTE : TipoPessoa.ALUNO);
		} else {
			obj.setTipoPessoa(TipoPessoa.valueOf(dadosSQL.getString("requerimento.tipoPessoa")));
		}
		if(dadosSQL.getObject("requerimento.situacaoIsencaoTaxa") != null) {
			obj.setSituacaoIsencaoTaxa(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.valueOf(dadosSQL.getString("requerimento.situacaoIsencaoTaxa")));
		}
		if(dadosSQL.getString("requerimento.formatoCertificadoSelecionado") != null) {
			obj.setFormatoCertificadoSelecionado(dadosSQL.getString("requerimento.formatoCertificadoSelecionado"));
		}

		// Dados do TipoRequerimento
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("tipoRequerimento.codigo"))) {
			obj.getTipoRequerimento().setCodigo(dadosSQL.getInt("tipoRequerimento.codigo"));
		} else {
			obj.getTipoRequerimento().setCodigo(0);
		}
		obj.getTipoRequerimento().setNome(dadosSQL.getString("tipoRequerimento.nome"));
		obj.getTipoRequerimento().setTipo(dadosSQL.getString("tipoRequerimento.tipo"));
		obj.getTipoRequerimento().getQuestionario().setCodigo(dadosSQL.getInt("tipoRequerimento.questionario"));
		obj.getTipoRequerimento().getTextoPadrao().setCodigo(dadosSQL.getInt("tipoRequerimento.textoPadrao"));
		obj.getFuncionarioVO().setCodigo(dadosSQL.getInt("funcionario.codigo"));
		obj.getFuncionarioVO().getPessoa().setCodigo(dadosSQL.getInt("pessoaFuncionario.codigo"));
		obj.getFuncionarioVO().getPessoa().setNome(dadosSQL.getString("pessoaFuncionario.nome"));
		obj.getFuncionarioVO().getPessoa().setEmail(dadosSQL.getString("pessoaFuncionario.email"));
		obj.getTipoRequerimento().setTipo(dadosSQL.getString("tipoRequerimento.tipo"));
		obj.getTipoRequerimento().setSituacaoMatriculaAtiva(dadosSQL.getBoolean("tipoRequerimento.situacaoMatriculaAtiva"));
		obj.getTipoRequerimento().setSituacaoMatriculaPreMatriculada(dadosSQL.getBoolean("tipoRequerimento.situacaoMatriculaPreMatriculada"));
		obj.getTipoRequerimento().setSituacaoMatriculaCancelada(dadosSQL.getBoolean("tipoRequerimento.situacaoMatriculaCancelada"));
		obj.getTipoRequerimento().setSituacaoMatriculaTrancada(dadosSQL.getBoolean("tipoRequerimento.situacaoMatriculaTrancada"));
		obj.getTipoRequerimento().setSituacaoMatriculaAbandonada(dadosSQL.getBoolean("tipoRequerimento.situacaoMatriculaAbandonada"));
		obj.getTipoRequerimento().setSituacaoMatriculaTransferida(dadosSQL.getBoolean("tipoRequerimento.situacaoMatriculaTransferida"));
		obj.getTipoRequerimento().setSituacaoMatriculaTransferida(dadosSQL.getBoolean("tipoRequerimento.situacaoMatriculaFormada"));
		obj.getTipoRequerimento().setVerificarPendenciaBiblioteca(dadosSQL.getBoolean("tipoRequerimento.verificarPendenciaBiblioteca"));
		obj.getTipoRequerimento().setVerificarPendenciaFinanceira(dadosSQL.getBoolean("tipoRequerimento.verificarPendenciaFinanceira"));
		obj.getTipoRequerimento().setVerificarPendenciaBibliotecaAtraso(dadosSQL.getBoolean("tipoRequerimento.verificarPendenciaBibliotecaAtraso"));
		obj.getTipoRequerimento().setVerificarPendenciaFinanceiraAtraso(dadosSQL.getBoolean("tipoRequerimento.verificarPendenciaFinanceiraAtraso"));
		obj.getTipoRequerimento().setVerificarPendenciaDocumentacao(dadosSQL.getBoolean("tipoRequerimento.verificarPendenciaDocumentacao"));
		obj.getTipoRequerimento().setVerificarPendenciaEnade(dadosSQL.getBoolean("tipoRequerimento.verificarPendenciaEnade"));
		obj.getTipoRequerimento().setVerificarPendenciaEstagio(dadosSQL.getBoolean("tipoRequerimento.verificarPendenciaEstagio"));
		obj.getTipoRequerimento().setVerificarPendenciaAtividadeComplementar(dadosSQL.getBoolean("tipoRequerimento.verificarPendenciaAtividadeComplementar"));
//		obj.getTipoRequerimento().getTaxa().setCodigo(dadosSQL.getInt("tipoRequerimento.taxa"));
		obj.getTipoRequerimento().setCobrarApartirVia(dadosSQL.getInt("tipoRequerimento.cobrarApartirVia"));
		obj.getTipoRequerimento().setRequerimentoVisaoAlunoApresImprimirDeclaracao(dadosSQL.getBoolean("tipoRequerimento.requerimentoVisaoAlunoApresImprimirDeclaracao"));
		obj.getTipoRequerimento().setQtdeDiasDisponivel(dadosSQL.getInt("tipoRequerimento.qtdeDiasDisponivel"));
		obj.getTipoRequerimento().setQtdeDiasAposPrimeiraImpressao(dadosSQL.getInt("tipoRequerimento.qtdeDiasAposPrimeiraImpressao"));
		obj.getTipoRequerimento().setRequerimentoSituacaoFinanceiroVisaoAluno(dadosSQL.getBoolean("requerimentoSituacaoFinanceiroVisaoAluno"));
		obj.getTipoRequerimento().setPermitirSolicitacaoIsencaoTaxaRequerimento(dadosSQL.getBoolean("permitirSolicitacaoIsencaoTaxaRequerimento"));
		obj.getTipoRequerimento().setBloquearQuantidadeRequerimentoAbertosSimultaneamente(dadosSQL.getBoolean("bloquearQuantidadeRequerimentoAbertosSimultaneamente"));
		obj.getTipoRequerimento().setQuantidadeLimiteRequerimentoAbertoSimultaneamente(dadosSQL.getInt("quantidadeLimiteRequerimentoAbertoSimultaneamente"));
		obj.getTipoRequerimento().setConsiderarBloqueioSimultaneoRequerimentoDeferido(dadosSQL.getBoolean("considerarBloqueioSimultaneoRequerimentoDeferido"));
		obj.getTipoRequerimento().setConsiderarBloqueioSimultaneoRequerimentoIndeferido(dadosSQL.getBoolean("considerarBloqueioSimultaneoRequerimentoIndeferido"));
		obj.getTipoRequerimento().setConsiderarTodasMatriculasAlunoValidacaoAberturaSimultanea(dadosSQL.getBoolean("considerartodasmatriculasalunovalidacaoaberturasimultanea"));
		if (dadosSQL.getString("tipoRequerimento.tipoControleCobrancaViaRequerimento") != null) {
			obj.getTipoRequerimento().setTipoControleCobrancaViaRequerimento(TipoControleCobrancaViaRequerimentoEnum.valueOf(dadosSQL.getString("tipoRequerimento.tipoControleCobrancaViaRequerimento")));
		}
		if(dadosSQL.getString("tipoRequerimento.orientacaoAtendente") != null) {
			obj.getTipoRequerimento().setOrientacaoAtendente(dadosSQL.getString("tipoRequerimento.orientacaoAtendente"));
		}
		if (configuracaoGeralSistemaVO != null) {
			obj.setIsApresentarIconeEmAlerta(executarVerificacaoAlertaRequerimento(configuracaoGeralSistemaVO, obj.getDataPrevistaFinalizacao(), obj.getSituacao()));
		}
		// Dados da UnidadeEnsino
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
		obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));
		obj.getSituacaoRequerimentoDepartamentoVO().setCodigo(dadosSQL.getInt("requerimento.situacaoRequerimentoDepartamento"));
		obj.getSituacaoRequerimentoDepartamentoVO().setSituacao(dadosSQL.getString("situacaoRequerimentoDepartamento.situacao"));
		
		// Dados da Interação
		obj.setPossuiInteracaoAtendenteNaoLida(dadosSQL.getBoolean("possuiInteracaoAtendenteNaoLida"));
		obj.setPossuiInteracaoRequerenteNaoLida(dadosSQL.getBoolean("possuiInteracaoRequerenteNaoLida"));
		// Dados contareceber
//		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("contareceber.codigo"))) {
//			obj.getContaReceberVO().setCodigo(dadosSQL.getInt("contareceber.codigo"));
//		}
		if (Uteis.isColunaExistente(dadosSQL, "turmabase") && Uteis.isAtributoPreenchido(dadosSQL.getString("turmabase"))) {
			obj.getTurmaReposicao().setIdentificadorTurmaBase(dadosSQL.getString("turmabase"));
		}
		if (Uteis.isColunaExistente(dadosSQL, "justificativaTrancamento") && Uteis.isAtributoPreenchido(dadosSQL.getString("justificativaTrancamento"))) {
			obj.setJustificativaTrancamento(dadosSQL.getString("justificativaTrancamento"));
		}
		if (Uteis.isColunaExistente(dadosSQL, "motivoCancelamentoTrancamento") && Uteis.isAtributoPreenchido(dadosSQL.getString("motivoCancelamentoTrancamento"))) {
			obj.getMotivoCancelamentoTrancamento().setCodigo(dadosSQL.getInt("motivoCancelamentoTrancamento"));
		}
		if(Uteis.isColunaExistente(dadosSQL, "cid")) {
			obj.setCidDescricao(dadosSQL.getString("cid"));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getDate("requerimento.dataafastamentoinicio"))) {
			obj.setDataAfastamentoInicio(dadosSQL.getDate("requerimento.dataafastamentoinicio"));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getDate("requerimento.dataafastamentofim"))) {
			obj.setDataAfastamentoFim(dadosSQL.getDate("requerimento.dataafastamentofim"));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getBoolean("campoafastamento"))) {
			obj.getTipoRequerimento().setCampoAfastamento(dadosSQL.getBoolean("campoafastamento"));	
		}
	}

	private void montarDadosCompleto(RequerimentoVO obj, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		// Dados Requerimento
		obj.setNovoObj(false);
		obj.setCodigo(dadosSQL.getInt("requerimento.codigo"));
		obj.setNomeRequerente(dadosSQL.getString("requerimento.nomeRequerente"));
		obj.setDataPrevistaFinalizacao(dadosSQL.getDate("requerimento.dataPrevistaFinalizacao"));
		obj.setTipoDesconto(dadosSQL.getString("requerimento.tipoDesconto"));
		obj.setData(dadosSQL.getTimestamp("requerimento.data"));
		obj.setSituacao(dadosSQL.getString("requerimento.situacao"));
		obj.setValor(dadosSQL.getDouble("requerimento.valor"));
		obj.setContaReceber(dadosSQL.getInt("requerimento.contaReceber"));
		obj.setPercDesconto(dadosSQL.getDouble("requerimento.percDesconto"));
		obj.setValorDesconto(dadosSQL.getDouble("requerimento.valorDesconto"));
		obj.setDataVencimentoContaReceber(dadosSQL.getDate("contareceber.datavencimento"));
		obj.setDataFinalizacao(dadosSQL.getDate("requerimento.dataFinalizacao"));
		obj.setSituacaoFinanceira(dadosSQL.getString("requerimento.situacaoFinanceira"));
		obj.setNrDocumento(dadosSQL.getString("requerimento.nrDocumento"));
		obj.setObservacao(dadosSQL.getString("requerimento.observacao"));
		obj.setTaxaIsentaPorQtdeVia(dadosSQL.getBoolean("requerimento.taxaIsentaPorQtdeVia"));
//		obj.getTaxa().setCodigo(dadosSQL.getInt("requerimento.taxa"));
		obj.getQuestionarioVO().setCodigo(dadosSQL.getInt("questionario"));
		if (Uteis.isAtributoPreenchido(obj.getQuestionarioVO().getCodigo())) {
			obj.setQuestionarioVO(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(obj.getQuestionarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}
		obj.getTurno().setCodigo(dadosSQL.getInt("turno"));
		obj.getTurno().setNome(dadosSQL.getString("nometurno"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("requerimento.responsavel"));
		obj.getResponsavel().setNome(dadosSQL.getString("requerimento.nomeResponsavel"));
		obj.setValorAdicional(dadosSQL.getDouble("valorAdicional"));
		obj.setVisaoGerado(dadosSQL.getString("requerimento.visaoGerado"));
		obj.setOrdemExecucaoTramiteDepartamento(dadosSQL.getInt("requerimento.ordemExecucaoTramiteDepartamento"));
		obj.setDataDeferimentoIndeferimentoIsencaoTaxa(dadosSQL.getDate("requerimento.dataDeferimentoIndeferimentoIsencaoTaxa"));
		obj.setMotivoDeferimentoIndeferimentoIsencaoTaxa(dadosSQL.getString("requerimento.motivoDeferimentoIndeferimentoIsencaoTaxa"));
		obj.setJustificativaSolicitacaoIsencao(dadosSQL.getString("requerimento.justificativaSolicitacaoIsencao"));
		obj.setMensagemChoqueHorario(dadosSQL.getString("mensagemChoqueHorario"));
		
		obj.setNotaMonografia(dadosSQL.getDouble("requerimento.notamonografia"));
		obj.setTipoTrabalhoConclusaoCurso(dadosSQL.getString("requerimento.tipoTrabalhoConclusaoCurso"));
		obj.setTituloMonografia(dadosSQL.getString("requerimento.tituloMonografia"));
		obj.setOrientadorMonografia(dadosSQL.getString("requerimento.orientadorMonografia"));
		
		if(dadosSQL.getString("requerimento.situacaoIsencaoTaxa") != null) {
			obj.setSituacaoIsencaoTaxa(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.valueOf(dadosSQL.getString("requerimento.situacaoIsencaoTaxa")));
		}
		obj.getComprovanteSolicitacaoIsencao().setCodigo(dadosSQL.getInt("requerimento.comprovanteSolicitacaoIsencao"));
		if(Uteis.isAtributoPreenchido(obj.getComprovanteSolicitacaoIsencao().getCodigo())) {
			obj.setComprovanteSolicitacaoIsencao(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getComprovanteSolicitacaoIsencao().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}
		obj.getResponsavelDeferimentoIndeferimentoIsencaoTaxa().setCodigo(dadosSQL.getInt("requerimento.respDefIndefIsencaoTaxa"));
		obj.getResponsavelDeferimentoIndeferimentoIsencaoTaxa().setNome(dadosSQL.getString("respDefIndefIsencaoTaxa.nome"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.setSigla(dadosSQL.getString("requerimento.sigla"));
		obj.setMotivoDeferimento(dadosSQL.getString("motivoDeferimento"));
		if (dadosSQL.getString("situacaomatriculaperiodo") != null) {
			obj.setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum.getEnumPorValor(dadosSQL.getString("situacaomatriculaperiodo")).toString());
		}
		if (dadosSQL.getString("requerimento.motivoNaoAceiteCertificado") != null) {
			obj.setMotivoNaoAceiteCertificado(dadosSQL.getString("requerimento.motivoNaoAceiteCertificado"));
		}
		if (dadosSQL.getString("requerimento.formatoCertificadoSelecionado") != null) {
			obj.setFormatoCertificadoSelecionado(dadosSQL.getString("requerimento.formatoCertificadoSelecionado"));
		}
		if(dadosSQL.getString("requerimento.formatoCertificadoSelecionado") != null) {
			obj.setFormatoCertificadoSelecionado(dadosSQL.getString("requerimento.formatoCertificadoSelecionado"));
		}
		
		obj.setDataUltimaImpressao(dadosSQL.getDate("requerimento.dataUltimaImpressao"));
		obj.setDataUltimaAlteracao(dadosSQL.getDate("requerimento.dataUltimaAlteracao"));
		obj.setDataAfastamentoInicio(dadosSQL.getDate("requerimento.dataafastamentoinicio"));
		obj.setDataAfastamentoFim(dadosSQL.getDate("requerimento.dataafastamentofim"));
		// Dados da Disciplina
		obj.getDisciplina().setCodigo(dadosSQL.getInt("disciplina.codigo"));
		obj.getDisciplina().setNome(dadosSQL.getString("disciplina.nome"));

		// Endereço de Entrega
		obj.setEndereco(dadosSQL.getString("requerimento.endereco"));
		obj.setSetor(dadosSQL.getString("requerimento.setor"));
		obj.setNumero(dadosSQL.getString("requerimento.numero"));
		obj.setCEP(dadosSQL.getString("requerimento.cep"));
		obj.setComplemento(dadosSQL.getString("requerimento.complemento"));
		obj.setMotivoIndeferimento(dadosSQL.getString("motivoIndeferimento"));
		obj.getGrupoFacilitador().setCodigo(dadosSQL.getInt("requerimento.grupofacilitador"));
		obj.setTemaTccFacilitador(dadosSQL.getString("requerimento.temaTccFacilitador"));
		obj.setAssuntoTccFacilitador(dadosSQL.getString("requerimento.assuntoTccFacilitador"));
		obj.setAvaliadorExternoFacilitador(dadosSQL.getString("requerimento.avaliadorExternoFacilitador"));
		// Dados da Cidade
		obj.getCidade().setCodigo(dadosSQL.getInt("cidade.codigo"));
		obj.getCidade().setNome(dadosSQL.getString("cidade.nome"));
		obj.getCidade().getEstado().setNome(dadosSQL.getString("estado.nome"));
		obj.getCidade().getEstado().setCodigo(dadosSQL.getInt("estado.codigo"));
		// Dados Departamento
		obj.getDepartamentoResponsavel().setCodigo(dadosSQL.getInt("departamento.codigo"));
		obj.getDepartamentoResponsavel().setNome(dadosSQL.getString("departamento.nome"));
		// Dados do Aluno
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula.matricula"));
		obj.getMatricula().setAnoIngresso(dadosSQL.getString("matricula.anoingresso"));
		obj.getMatricula().setSemestreIngresso(dadosSQL.getString("matricula.semestreingresso"));
		obj.getMatricula().getGradeCurricularAtual().setCodigo(dadosSQL.getInt("matricula.gradeCurricularAtual"));
		obj.getMatricula().getAluno().setCodigo(dadosSQL.getInt("aluno.codigo"));
		obj.getMatricula().getAluno().setRegistroAcademico(dadosSQL.getString("aluno.registroacademico"));
		obj.getMatricula().getAluno().setNome(dadosSQL.getString("aluno.nome"));
		obj.getMatricula().getAluno().setCPF(dadosSQL.getString("aluno.cpf"));
		obj.getMatricula().getCurso().setCodigo(dadosSQL.getInt("cursoAluno.codigo"));
		obj.getMatricula().getCurso().setNome(dadosSQL.getString("cursoAluno.nome"));
		obj.getMatricula().getCurso().setPeriodicidade(dadosSQL.getString("cursoAluno.periodicidade"));
		if (dadosSQL.getInt("codigoTurmaMatriculaPeriodo") > 0) {
			obj.getTurma().setCodigo(dadosSQL.getInt("codigoTurmaMatriculaPeriodo"));
			obj.getTurma().setIdentificadorTurma(dadosSQL.getString("identificadorTurmaMatriculaPeriodo"));
		} else {
			obj.getTurma().setCodigo(dadosSQL.getInt("turma.codigo"));
			obj.getTurma().setIdentificadorTurma(dadosSQL.getString("turma.identificadorturma"));
		}
		obj.getTurma().getPeridoLetivo().setCodigo(dadosSQL.getInt("periodoletivo.codigo"));
		obj.getTurma().getPeridoLetivo().setPeriodoLetivo(dadosSQL.getInt("periodoletivo.periodoletivo"));

		if (dadosSQL.getInt("codigoCursoMatriculaPeriodo") > 0) {
			obj.getCurso().setCodigo(dadosSQL.getInt("codigoCursoMatriculaPeriodo"));
			obj.getCurso().setNome(dadosSQL.getString("nomeCursoMatriculaPeriodo"));
		} else {
			obj.getCurso().setCodigo(dadosSQL.getInt("curso"));
			obj.getCurso().setNome(dadosSQL.getString("nomecurso"));
		}

		// obj.getMatricula().
		// Dados da Pessoa
		obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getPessoa().setRegistroAcademico(dadosSQL.getString("pessoa.registroacademico"));
		obj.getPessoa().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getPessoa().setTelefoneRes(dadosSQL.getString("pessoa.telefoneres"));
		obj.getPessoa().setCelular(dadosSQL.getString("pessoa.celular"));
		obj.getPessoa().setTelefoneRecado(dadosSQL.getString("pessoa.telefonerecado"));
		obj.getPessoa().setTelefoneComer(dadosSQL.getString("pessoa.telefonecomer"));
		obj.getPessoa().setCPF(dadosSQL.getString("pessoa.cpf"));
		obj.setCpfRequerente(dadosSQL.getString("pessoa.cpf"));
		obj.getPessoa().setRG(dadosSQL.getString("pessoa.rg"));
		obj.getPessoa().setEmail(dadosSQL.getString("pessoa.email"));
		if (dadosSQL.getString("requerimento.tipoPessoa") == null || dadosSQL.getString("requerimento.tipoPessoa").trim().isEmpty()) {
			obj.setTipoPessoa(obj.getMatricula().getMatricula().trim().isEmpty() ? TipoPessoa.REQUERENTE : TipoPessoa.ALUNO);
		} else {
			obj.setTipoPessoa(TipoPessoa.valueOf(dadosSQL.getString("requerimento.tipoPessoa")));
		}
		// Tipo de Requerimento
		obj.getTipoRequerimento().setCodigo(dadosSQL.getInt("tipoRequerimento.codigo"));
		obj.getTipoRequerimento().setNome(dadosSQL.getString("tipoRequerimento.nome"));
		obj.getTipoRequerimento().setTipo(dadosSQL.getString("tipoRequerimento.tipo"));
		obj.getTipoRequerimento().setMensagemAlerta(dadosSQL.getString("tipoRequerimento.mensagemAlerta"));
		obj.getTipoRequerimento().setTramitaEntreDepartamentos(dadosSQL.getBoolean("tipoRequerimento.tramitaEntreDepartamentos"));
		obj.getTipoRequerimento().setSituacaoMatriculaAtiva(dadosSQL.getBoolean("tipoRequerimento.situacaoMatriculaAtiva"));
		obj.getTipoRequerimento().setSituacaoMatriculaPreMatriculada(dadosSQL.getBoolean("tipoRequerimento.situacaoMatriculaPreMatriculada"));
		obj.getTipoRequerimento().setSituacaoMatriculaCancelada(dadosSQL.getBoolean("tipoRequerimento.situacaoMatriculaCancelada"));
		obj.getTipoRequerimento().setSituacaoMatriculaTrancada(dadosSQL.getBoolean("tipoRequerimento.situacaoMatriculaTrancada"));
		obj.getTipoRequerimento().setSituacaoMatriculaAbandonada(dadosSQL.getBoolean("tipoRequerimento.situacaoMatriculaAbandonada"));
		obj.getTipoRequerimento().setSituacaoMatriculaTransferida(dadosSQL.getBoolean("tipoRequerimento.situacaoMatriculaTransferida"));
		obj.getTipoRequerimento().setSituacaoMatriculaTransferida(dadosSQL.getBoolean("tipoRequerimento.situacaoMatriculaFormada"));
		obj.getTipoRequerimento().setVerificarPendenciaBiblioteca(dadosSQL.getBoolean("tipoRequerimento.verificarPendenciaBiblioteca"));
		obj.getTipoRequerimento().setVerificarPendenciaFinanceira(dadosSQL.getBoolean("tipoRequerimento.verificarPendenciaFinanceira"));
		obj.getTipoRequerimento().setVerificarPendenciaBibliotecaAtraso(dadosSQL.getBoolean("tipoRequerimento.verificarPendenciaBibliotecaAtraso"));
		obj.getTipoRequerimento().setVerificarPendenciaFinanceiraAtraso(dadosSQL.getBoolean("tipoRequerimento.verificarPendenciaFinanceiraAtraso"));
		obj.getTipoRequerimento().setVerificarPendenciaDocumentacao(dadosSQL.getBoolean("tipoRequerimento.verificarPendenciaDocumentacao"));
		obj.getTipoRequerimento().setVerificarPendenciaEnade(dadosSQL.getBoolean("tipoRequerimento.verificarPendenciaEnade"));
		obj.getTipoRequerimento().setVerificarPendenciaEstagio(dadosSQL.getBoolean("tipoRequerimento.verificarPendenciaEstagio"));
		obj.getTipoRequerimento().setVerificarPendenciaAtividadeComplementar(dadosSQL.getBoolean("tipoRequerimento.verificarPendenciaAtividadeComplementar"));
		obj.getTipoRequerimento().setPermitirUploadArquivo(dadosSQL.getBoolean("tipoRequerimento.permitirUploadArquivo"));
		obj.getTipoRequerimento().setPermitirInformarEnderecoEntrega(dadosSQL.getBoolean("tipoRequerimento.permitirInformarEnderecoEntrega"));
		obj.getTipoRequerimento().setRequerimentoVisaoAlunoApresImprimirDeclaracao(dadosSQL.getBoolean("tipoRequerimento.requerimentoVisaoAlunoApresImprimirDeclaracao"));
//		obj.getTipoRequerimento().getTaxa().setCodigo(dadosSQL.getInt("tipoRequerimento.taxa"));
		obj.getTipoRequerimento().setCobrarApartirVia(dadosSQL.getInt("tipoRequerimento.cobrarApartirVia"));
		obj.getTipoRequerimento().setAssinarDigitalmenteDeclaracoesGeradasNoRequerimento(dadosSQL.getBoolean("tipoRequerimento.assinarDigitalmenteDeclaracoes"));
		obj.getTipoRequerimento().setVerificarPendenciaApenasMatriculaRequerimento(dadosSQL.getBoolean("tipoRequerimento.verificarPendenciaApenasMatriculaRequerimento"));
		obj.getTipoRequerimento().setDeferirAutomaticamente(dadosSQL.getBoolean("tipoRequerimento.deferirAutomaticamente"));
		obj.getTipoRequerimento().setTipoUploadArquivo(TipoUploadArquivoEnum.valueOf(dadosSQL.getString("tipoRequerimento.tipoUploadArquivo")));
		obj.getTipoRequerimento().setExtensaoArquivo(dadosSQL.getString("tipoRequerimento.extensaoArquivo"));
		obj.getTipoRequerimento().setOrientacaoUploadArquivo(dadosSQL.getString("tipoRequerimento.orientacaoUploadArquivo"));		
		obj.getTipoRequerimento().setRequerAutorizacaoPagamento(dadosSQL.getBoolean("tipoRequerimento.requerAutorizacaoPagamento"));		
		obj.getTipoRequerimento().setPermiteDeferirAguardandoAutorizacaoPagamento(dadosSQL.getBoolean("tipoRequerimento.permiteDeferirAguardandoAutorizacaoPagamento"));		
		obj.getTipoRequerimento().setConsiderarDiasUteis(dadosSQL.getBoolean("tipoRequerimento.considerarDiasUteis"));
		obj.getTipoRequerimento().setRealizarIsencaoTaxaReposicaoMatriculaAposDataAula(dadosSQL.getBoolean("realizarIsencaoTaxaReposicaoMatriculaAposDataAula"));
        obj.getTipoRequerimento().setQuantidadeDiasAntesAulaPermitirExclusaoRequerimentoReposicao(dadosSQL.getInt("quantidadeDiasAntesAulaPermitirExclusaoRequerimentoReposicao"));
        obj.getTipoRequerimento().setPermitirSolicitacaoIsencaoTaxaRequerimento(dadosSQL.getBoolean("permitirSolicitacaoIsencaoTaxaRequerimento"));
        obj.getTipoRequerimento().setSolicitarAnexoComprovanteIsencaoTaxaRequerimento(dadosSQL.getBoolean("solicitarAnexoComprovanteIsencaoTaxaRequerimento"));
        obj.getTipoRequerimento().setOrientacaoDocumentoComprovanteIsencaoTaxaRequerimento(dadosSQL.getString("orientacaoDocumentoComprovanteIsencaoTaxaRequerimento"));
        obj.getTipoRequerimento().setPermiteIncluirDisciplinaPorEquivalencia(dadosSQL.getBoolean("permiteIncluirDisciplinaPorEquivalencia"));
        obj.getTipoRequerimento().setPermiteIncluirReposicaoTurmaOutraUnidade(dadosSQL.getBoolean("permiteIncluirReposicaoTurmaOutraUnidade"));
        obj.getTipoRequerimento().setPermiteIncluirReposicaoTurmaOutroCurso(dadosSQL.getBoolean("permiteIncluirReposicaoTurmaOutroCurso"));
        obj.getTipoRequerimento().setPermitirReposicaoComChoqueHorario(dadosSQL.getBoolean("permitirReposicaoComChoqueHorario"));
        obj.getTipoRequerimento().setBloquearQuantidadeRequerimentoAbertosSimultaneamente(dadosSQL.getBoolean("bloquearQuantidadeRequerimentoAbertosSimultaneamente"));
		obj.getTipoRequerimento().setQuantidadeLimiteRequerimentoAbertoSimultaneamente(dadosSQL.getInt("quantidadeLimiteRequerimentoAbertoSimultaneamente"));
		obj.getTipoRequerimento().setConsiderarBloqueioSimultaneoRequerimentoDeferido(dadosSQL.getBoolean("considerarBloqueioSimultaneoRequerimentoDeferido"));
		obj.getTipoRequerimento().setConsiderarBloqueioSimultaneoRequerimentoIndeferido(dadosSQL.getBoolean("considerarBloqueioSimultaneoRequerimentoIndeferido"));
		obj.getTipoRequerimento().setConsiderarTodasMatriculasAlunoValidacaoAberturaSimultanea(dadosSQL.getBoolean("considerarTodasMatriculasAlunoValidacaoAberturaSimultanea"));
		obj.getTipoRequerimento().setPermitirAlunoRejeitarDocumento(dadosSQL.getBoolean("permitirAlunoRejeitarDocumento"));
		obj.getTipoRequerimento().setRegistrarAproveitamentoDisciplinaTCC(dadosSQL.getBoolean("registrarAproveitamentoDisciplinaTCC"));
		obj.getTipoRequerimento().setRegistrarTrancamentoProximoSemestre(dadosSQL.getBoolean("registrarTrancamentoProximoSemestre"));
		obj.getTipoRequerimento().setRegistrarTransferenciaProximoSemestre(dadosSQL.getBoolean("tipoRequerimento.registrarTransferenciaProximoSemestre"));
		obj.getTipoRequerimento().setAno(dadosSQL.getString("tipoRequerimento.ano"));
		obj.getTipoRequerimento().setSemestre(dadosSQL.getString("tipoRequerimento.semestre"));
		if (dadosSQL.getString("tipoRequerimento.tipoControleCobrancaViaRequerimento") != null) {
			obj.getTipoRequerimento().setTipoControleCobrancaViaRequerimento(TipoControleCobrancaViaRequerimentoEnum.valueOf(dadosSQL.getString("tipoRequerimento.tipoControleCobrancaViaRequerimento")));
		}
		obj.getTipoRequerimento().setQtdDiasVencimentoRequerimento(dadosSQL.getInt("tipoRequerimento.qtdDiasVencimentoRequerimento"));
		obj.getTipoRequerimento().setRequerimentoSituacaoFinanceiroVisaoAluno(dadosSQL.getBoolean("tipoRequerimento.requerimentoSituacaoFinanceiroVisaoAluno"));
		obj.getTipoRequerimento().setRequerimentoSituacaoFinanceiroVisaoAluno(dadosSQL.getBoolean("tipoRequerimento.requerimentoSituacaoFinanceiroVisaoAluno"));
		obj.getTipoRequerimento().getQuestionario().setCodigo(dadosSQL.getInt("tipoRequerimento.questionario"));
		if (Uteis.isAtributoPreenchido(obj.getTipoRequerimento().getQuestionario().getCodigo())) {
			obj.getTipoRequerimento().setQuestionario(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(obj.getTipoRequerimento().getQuestionario().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}
		obj.getTipoRequerimento().getTextoPadrao().setCodigo(dadosSQL.getInt("tipoRequerimento.textoPadrao"));
		obj.getTipoRequerimento().getTextoPadraoVO().setCodigo(dadosSQL.getInt("tipoRequerimento.textoPadraoContratoEstagio"));
		obj.getTipoRequerimento().setOrientacao(dadosSQL.getString("tipoRequerimento.orientacao"));
		obj.getTipoRequerimento().setTipoRequerimentoDepartamentoVOs(getFacadeFactory().getTipoRequerimentoDepartamentoFacade().consultarPorCodigoTipoRequerimento(obj.getTipoRequerimento().getCodigo(), false, null));
		if(dadosSQL.getString("tipoRequerimento.orientacaoAtendente") != null) {
			obj.getTipoRequerimento().setOrientacaoAtendente(dadosSQL.getString("tipoRequerimento.orientacaoAtendente"));
		}
		obj.getTipoRequerimento().setRegistrarFormaturaAoRealizarImpressaoCerticadoDigital(dadosSQL.getBoolean("registrarFormaturaAoRealizarImpressaoCerticadoDigital"));
		obj.getTipoRequerimento().getCertificadoImpresso().setCodigo(dadosSQL.getInt("tipoRequerimento.certificadoImpresso"));
		obj.getTipoRequerimento().setAbrirOutroRequerimentoAoDeferirEsteTipoRequerimento(dadosSQL.getBoolean("abriroutrorequerimentoaodeferirestetiporequerimento"));
		obj.getTipoRequerimento().setPermitirAlterarDataPrevisaoConclusaoRequerimento(dadosSQL.getBoolean("permitirAlterarDataPrevisaoConclusaoRequerimento"));
		if(dadosSQL.getInt("tiporequerimentoabrirdeferimento") != 0) {
			obj.getTipoRequerimento().setTipoRequerimentoAbrirDeferimento(getFacadeFactory().getTipoRequerimentoFacade().consultarPorChavePrimaria(dadosSQL.getInt("tiporequerimentoabrirdeferimento"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		obj.getTipoRequerimento().setPermitirImpressaoHistoricoVisaoAluno(dadosSQL.getBoolean("permitirImpressaoHistoricoVisaoAluno"));
		obj.getTipoRequerimento().setNivelEducacional(dadosSQL.getString("nivelEducacional"));
		obj.getTipoRequerimento().setLayoutHistoricoApresentar(dadosSQL.getString("layoutHistoricoApresentar"));
		obj.getTipoRequerimento().setAprovadoSituacaoHistorico(dadosSQL.getBoolean("aprovadoSituacaoHistorico"));
		obj.getTipoRequerimento().setReprovadoSituacaoHistorico(dadosSQL.getBoolean("reprovadoSituacaoHistorico"));
		obj.getTipoRequerimento().setTrancadoSituacaoHistorico(dadosSQL.getBoolean("trancadoSituacaoHistorico"));
		obj.getTipoRequerimento().setCursandoSituacaoHistorico(dadosSQL.getBoolean("cursandoSituacaoHistorico"));
		obj.getTipoRequerimento().setAbandonoCursoSituacaoHistorico(dadosSQL.getBoolean("abandonoCursoSituacaoHistorico"));
		obj.getTipoRequerimento().setTransferidoSituacaoHistorico(dadosSQL.getBoolean("transferidoSituacaoHistorico"));
		obj.getTipoRequerimento().setCanceladoSituacaoHistorico(dadosSQL.getBoolean("canceladoSituacaoHistorico"));
		obj.getTipoRequerimento().setAssinarDigitalmenteHistorico(dadosSQL.getBoolean("assinarDigitalmenteHistorico"));
		obj.getTipoRequerimento().setDeferirAutomaticamenteTrancamento(dadosSQL.getBoolean("deferirAutomaticamenteTrancamento"));
		obj.getTipoRequerimento().setPercentualIntegralizacaoCurricularInicial(dadosSQL.getInt("percentualIntegralizacaoCurricularInicial"));
		obj.getTipoRequerimento().setPercentualIntegralizacaoCurricularFinal(dadosSQL.getInt("percentualIntegralizacaoCurricularFinal"));
		if (dadosSQL.getInt("tipoRequerimento.qtdemaximaindeferidoaproveitamento") != 0) {
			obj.getTipoRequerimento().setQtdeMaximaIndeferidoAproveitamento(dadosSQL.getInt("tipoRequerimento.qtdemaximaindeferidoaproveitamento"));
		}
		if (Uteis.isColunaExistente(dadosSQL, "TipoRequerimento.bloqueiosimultaneopelo")) {
			obj.getTipoRequerimento().setBloqueioSimultaneoPelo(dadosSQL.getString("TipoRequerimento.bloqueiosimultaneopelo"));
		}
		obj.getTipoRequerimento().setEnviarNotificacaoRequerente(dadosSQL.getBoolean("enviarnotificacaorequerente"));
		obj.getTipoRequerimento().setCampoAfastamento(dadosSQL.getBoolean("campoafastamento"));
		obj.getTipoRequerimento().setUtilizarMensagemDeferimentoExclusivo(dadosSQL.getBoolean("utilizarmensagemdeferimentoexclusivo"));
		obj.getTipoRequerimento().setUtilizarMensagemIndeferimentoExclusivo(dadosSQL.getBoolean("utilizarmensagemindeferimentoexclusivo"));
		obj.getTipoRequerimento().setTipoAluno(Uteis.isAtributoPreenchido(dadosSQL.getString("tipoRequerimento.tipoAluno")) ? TipoAlunoEnum.valueOf(dadosSQL.getString("tipoRequerimento.tipoAluno")) : null);
		obj.getFuncionarioVO().setCodigo(dadosSQL.getInt("funcionario.codigo"));
		obj.getFuncionarioVO().getPessoa().setCodigo(dadosSQL.getInt("pessoaFuncionario.codigo"));
		obj.getFuncionarioVO().getPessoa().setNome(dadosSQL.getString("pessoaFuncionario.nome"));
		
		// Dados novo Requeriemnto
		if(dadosSQL.getInt("requerimento.codigonovorequerimento") != 0) {
			obj.setRequerimentoAntigo(consultarPorChavePrimaria(dadosSQL.getInt("requerimento.codigonovorequerimento"), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
		}
		

		// Unidade de Ensino
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
		obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));
		obj.getUnidadeEnsino().setEndereco("");
		// Responsável Recebimento
		obj.getResponsavelRecebimentoDocRequerido().setCodigo(dadosSQL.getInt("responsavelRecebimento.codigo"));
		obj.getResponsavelRecebimentoDocRequerido().setNome(dadosSQL.getString("responsavelRecebimento.nome"));
		obj.setDataRecebimentoDocRequerido(dadosSQL.getDate("responsavelRecebimento.dataRecebimentoDocRequerido"));
		// Responsável Boleto
		obj.getResponsavelEmissaoBoleto().setCodigo(dadosSQL.getInt("responsavelBoleto.codigo"));
		obj.getResponsavelEmissaoBoleto().setNome(dadosSQL.getString("responsavelBoleto.nome"));
		obj.setDataEmissaoBoleto(dadosSQL.getDate("responsavelBoleto.dataEmissaoBoleto"));
		// Centro Receita
//		obj.getCentroReceita().setCodigo(dadosSQL.getInt("centroReceita.codigo"));
//		obj.getCentroReceita().setDescricao(dadosSQL.getString("centroReceita.descricao"));
//		obj.setContaReceber(dadosSQL.getInt("requerimento.contaReceber"));
//		obj.getContaReceberVO().setCodigo(dadosSQL.getInt("requerimento.contaReceber"));
//		if(Uteis.isAtributoPreenchido(dadosSQL.getInt("requerimento.contaReceber"))){
//			getFacadeFactory().getContaReceberFacade().carregarDados(obj.getContaReceberVO(), NivelMontarDados.BASICO, null, usuario);
//			boolean possuiPermissaoEmitirBoletoVencido = getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("ImprimirBoletoVencidoVisaoAluno", usuario);
//			getFacadeFactory().getContaReceberFacade().validarTipoImpressaoPorContaReceber(obj.getContaReceberVO(), possuiPermissaoEmitirBoletoVencido, usuario);
//		}
//		// Conta Corrente
//		obj.getContaCorrenteVO().setCodigo(dadosSQL.getInt("contaCorrente.codigo"));
//		obj.getContaCorrenteVO().setNumero(dadosSQL.getString("contaCorrente.numero"));
		// Arquivo
		obj.getArquivoVO().setCodigo(dadosSQL.getInt("arquivo.codigo"));
		obj.getArquivoVO().setNome(dadosSQL.getString("arquivo.nome"));
		obj.getArquivoVO().setDescricao(dadosSQL.getString("arquivo.descricao"));
		obj.getArquivoVO().setDescricaoAntesAlteracao(dadosSQL.getString("arquivo.descricao"));
		obj.getArquivoVO().setExtensao(dadosSQL.getString("arquivo.extensao"));
		obj.getArquivoVO().setDataUpload(dadosSQL.getDate("arquivo.dataUpload"));
		obj.getArquivoVO().getResponsavelUpload().setCodigo(dadosSQL.getInt("arquivo.responsavelUpload"));
		obj.getArquivoVO().setCodOrigem(dadosSQL.getInt("arquivo.codOrigem"));
		obj.getArquivoVO().setNivelEducacional(dadosSQL.getString("arquivo.nivelEducacional"));
		obj.getArquivoVO().setCpfAlunoDocumentacao(dadosSQL.getString("arquivo.cpfAlunoDocumentacao"));
		obj.getArquivoVO().setCpfRequerimento(dadosSQL.getString("arquivo.cpfRequerimento"));
		obj.getArquivoVO().setOrigem(dadosSQL.getString("arquivo.origem"));
		obj.getArquivoVO().setDataIndisponibilizacao(dadosSQL.getDate("arquivo.dataIndisponibilizacao"));
		obj.getArquivoVO().setPastaBaseArquivo(dadosSQL.getString("arquivo.pastaBaseArquivo"));
		obj.getArquivoVO().getTurma().setCodigo(dadosSQL.getInt("arquivo.turma"));
		obj.getArquivoVO().getProfessor().setCodigo(dadosSQL.getInt("arquivo.professor"));
		obj.getArquivoVO().getDisciplina().setCodigo(dadosSQL.getInt("arquivo.disciplina"));

		obj.setRequerimentoHistoricoVOs(getFacadeFactory().getRequerimentoHistoricoFacade().consultarPorCodigoRequerimento(obj.getCodigo(), false, null));
		if(obj.getTipoRequerimento().getIsTipoAproveitamentoDisciplina() || (obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.TAXA_TCC.getValor()) && obj.getIsDeferido())){
			obj.setListaRequerimentoDisciplinasAproveitadasVOs(getFacadeFactory().getRequerimentoDisciplinasAproveitadasFacade().consultarPorRequerimentoVO(obj, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}
		obj.setMaterialRequerimentoVOs(getFacadeFactory().getMaterialRequerimentoFacade().consultarPorRequerimento(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		obj.getUnidadeEnsinoTransferenciaInternaVO().setCodigo(dadosSQL.getInt("unidadeEnsinoTransferenciaInterna_codigo"));
		obj.getUnidadeEnsinoTransferenciaInternaVO().setNome(dadosSQL.getString("unidadeEnsinoTransferenciaInterna_nome"));
		obj.getCursoTransferenciaInternaVO().setCodigo(dadosSQL.getInt("cursoTransferenciaInterna_codigo"));
		obj.getCursoTransferenciaInternaVO().setNome(dadosSQL.getString("cursoTransferenciaInterna_nome"));
		obj.getTurnoTransferenciaInternaVO().setCodigo(dadosSQL.getInt("turnoTransferenciaInterna_codigo"));
		obj.getTurnoTransferenciaInternaVO().setNome(dadosSQL.getString("turnoTransferenciaInterna_nome"));
		obj.getSituacaoRequerimentoDepartamentoVO().setCodigo(dadosSQL.getInt("requerimento.situacaoRequerimentoDepartamento"));
		obj.getSituacaoRequerimentoDepartamentoVO().setSituacao(dadosSQL.getString("situacaoRequerimentoDepartamento.situacao"));
		
		obj.setCargaHorariaDisciplina(dadosSQL.getInt("cargaHorariaDisciplina"));
		if(dadosSQL.getObject("requerimento_dataInicioAula") != null) {
			obj.setDataInicioAula(dadosSQL.getDate("requerimento_dataInicioAula"));
		}
		if(dadosSQL.getObject("requerimento_dataTerminoAula") != null) {
			obj.setDataTerminoAula(dadosSQL.getDate("requerimento_dataTerminoAula"));
		}
		obj.setDisciplinaPorEquivalencia(dadosSQL.getBoolean("disciplinaPorEquivalencia"));
		obj.getMapaEquivalenciaDisciplinaVO().setCodigo(dadosSQL.getInt("mapaEquivalenciaDisciplina"));
		obj.getMapaEquivalenciaDisciplinaCursadaVO().setCodigo(dadosSQL.getInt("mapaEquivalenciaDisciplinaCursada"));
		obj.getMapaEquivalenciaDisciplinaCursadaVO().getDisciplinaVO().setCodigo(dadosSQL.getInt("disciplinaCursada_codigo"));
		obj.getMapaEquivalenciaDisciplinaCursadaVO().getDisciplinaVO().setNome(dadosSQL.getString("disciplinaCursada_nome"));
		obj.getMapaEquivalenciaDisciplinaCursadaVO().setCargaHoraria(dadosSQL.getInt("disciplinaCursada_cargahoraria"));
		if(obj.getDisciplinaPorEquivalencia()) {
			obj.getMapaEquivalenciaDisciplinaCursadaVO().setNovoObj(false);
			obj.getMapaEquivalenciaDisciplinaVO().setNovoObj(false);
		}
//		obj.getSalaLocalAulaVO().setCodigo(dadosSQL.getInt("salaLocalAula"));
//		obj.getSalaLocalAulaVO().setSala(dadosSQL.getString("salaLocalAula_sala"));
//		obj.getSalaLocalAulaVO().getLocalAula().setLocal(dadosSQL.getString("localAula_local"));
		obj.getTurmaReposicao().setCodigo(dadosSQL.getInt("turmareposicao_codigo"));
		obj.getTurmaReposicao().setIdentificadorTurma(dadosSQL.getString("turmareposicao_identificadorTurma"));
		obj.getTurmaReposicao().getUnidadeEnsino().setNome(dadosSQL.getString("unidadereposicao"));
		obj.getMatriculaPeriodoVO().setCodigo(dadosSQL.getInt("matriculaPeriodo.codigo"));
		obj.getMatriculaPeriodoVO().getPeriodoLetivo().setCodigo(dadosSQL.getInt("matriculaPeriodo.periodoletivomatricula"));
		obj.getMatriculaPeriodoVO().setAno(dadosSQL.getString("ano"));
		obj.getMatriculaPeriodoVO().setSemestre(dadosSQL.getString("semestre"));
		
		if (Uteis.isColunaExistente(dadosSQL, "requerimento.turmabase") && Uteis.isAtributoPreenchido(dadosSQL.getString("requerimento.turmabase"))) {
			obj.getTurmaReposicao().setIdentificadorTurmaBase(dadosSQL.getString("requerimento.turmabase"));
		}
		if (Uteis.isColunaExistente(dadosSQL, "requerimento.numeroVia") && Uteis.isAtributoPreenchido(dadosSQL.getString("requerimento.numeroVia"))) {
			obj.setNumeroVia(dadosSQL.getInt("requerimento.numeroVia"));
		}
		
		if (Uteis.isColunaExistente(dadosSQL, "requerimento.justificativaTrancamento") && Uteis.isAtributoPreenchido(dadosSQL.getString("requerimento.justificativaTrancamento"))) {
			obj.setJustificativaTrancamento(dadosSQL.getString("requerimento.justificativaTrancamento"));
		}
		
		if (Uteis.isColunaExistente(dadosSQL, "requerimento.motivoCancelamentoTrancamento") && Uteis.isAtributoPreenchido(dadosSQL.getString("requerimento.motivoCancelamentoTrancamento"))) {
			obj.getMotivoCancelamentoTrancamento().setCodigo(dadosSQL.getInt("requerimento.motivoCancelamentoTrancamento"));
		}
		if(obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.SEGUNDA_CHAMADA.getValor())) {
			getFacadeFactory().getRequerimentoDisciplinaInterfaceFacade().consultarPorRequerimento(obj, usuario);
		}
		if(Uteis.isColunaExistente(dadosSQL, "codcid") && Uteis.isColunaExistente(dadosSQL, "codigocid") && Uteis.isColunaExistente(dadosSQL, "descricaocid")) {
			obj.getCidTipoRequerimentoVO().setCodCid(dadosSQL.getString("codcid"));
			obj.getCidTipoRequerimentoVO().setCodigo(dadosSQL.getInt("codigocid"));
			obj.getCidTipoRequerimentoVO().setDescricaoCid(dadosSQL.getString("descricaocid"));
		}
		if(Uteis.isColunaExistente(dadosSQL, "cid")) {
			obj.setCidDescricao(dadosSQL.getString("cid"));
			obj.setCid(dadosSQL.getString("cid"));
		}
	}
	@Override
	public List<RequerimentoVO> consultaRapidaRequerimentoPorCodigoPai(Integer codigoPai, String matriculaAluno, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		
		if (usuario.getIsApresentarVisaoAluno() || usuario.getIsApresentarVisaoPais()) {
			consultar("RequerimentoAluno", controlarAcesso, usuario);
		}else {
		
			consultar(getIdEntidade(), controlarAcesso, usuario);
	}
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		
		if (codigoPai != null) {
			sqlStr.append(" WHERE p2.codigo = ");
			sqlStr.append(codigoPai);
		}
		if (matriculaAluno != null) {
			sqlStr.append(" and matricula.matricula = '").append(matriculaAluno).append("'");
		}
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and Requerimento.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY requerimento.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, configuracaoGeralSistemaVO);
	}

	public Boolean executarVerificacaoAlertaRequerimento(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Date dataPrevistaFinalizacao, String situacao) {
		if (dataPrevistaFinalizacao.after(new Date())) {
			Long nr = Uteis.nrDiasEntreDatas(dataPrevistaFinalizacao, new Date());
			Integer qtdeDias = Integer.valueOf(nr.toString());
			if (((qtdeDias <= configuracaoGeralSistemaVO.getQtdeDiasAlertaRequerimento()) && (!situacao.equals("FD") || !situacao.equals("FI")))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return Requerimento.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Requerimento.idEntidade = idEntidade;
	}

	public List<RequerimentoVO> consultarRequerimentosMapaReposicao(RequerimentoVO obj, Integer curso, Integer turma, Integer disciplina, Date dataInicio, Date dataFim, Integer unidadeEnsino, String ordenarPor, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {

		String sqlStr = " select distinct requerimento.matricula as matricula, requerimento.codigo, requerimento.data, pessoa.codigo as codAluno, pessoa.nome as aluno, curso.nome as curso, turma.identificadorturma as turmaorigem, turma.codigo as codTurmaorigem , requerimento.observacao, " + " matricula.data as datamatricula, rep.codigo, rep.identificadorturma as turmareposicao, rep.codigo as codturmareposicao, disciplina.nome as disciplina, disciplina.codigo as codDisciplina, requerimento.situacao, requerimento.situacaofinanceira, matriculaperiodo.ano, matriculaperiodo.semestre, unidadeensino.nome" 
				+ " from requerimento " + " inner join matricula on matricula.matricula = requerimento.matricula " + " inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula " + " inner join pessoa on pessoa.codigo = matricula.aluno " + " inner join curso on curso.codigo = matricula.curso " + " left join turma on matriculaperiodo.turma = turma.codigo " + " left join turma rep on rep.codigo = requerimento.turma "
				+ " inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino " + " left join disciplina on disciplina.codigo = requerimento.disciplina " + " left join historico on (historico.matriculaperiodo = matriculaperiodo.codigo and historico.disciplina = disciplina.codigo and (historico.situacao = 'RE' or historico.situacao = 'RF')) " + " where requerimento.situacao <> 'FD' and requerimento.situacao <> 'FI'" + " and (matriculaperiodo.situacaomatriculaperiodo = 'AT') ";
		if (obj.getTipoRequerimento().getCodigo().intValue() != 0 && obj.getTipoRequerimento().getCodigo() != null) {
			sqlStr += " AND requerimento.tiporequerimento = " + obj.getTipoRequerimento().getCodigo().intValue() + " ";
		}
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr += " AND requerimento.unidadeensino = " + unidadeEnsino.intValue() + " ";
		}
		if (obj.getCodigo().intValue() != 0 && obj.getCodigo() != null) {
			sqlStr += " AND requerimento.codigo = " + obj.getCodigo() + " ";
		}
		if (!obj.getMatricula().getMatricula().equals("") && obj.getMatricula().getMatricula() != null) {
			sqlStr += " AND requerimento.matricula = '" + obj.getMatricula().getMatricula() + "' ";
		}
		if (dataInicio != null) {
			sqlStr += " AND requerimento.data >= '" + Uteis.getDataJDBC(dataInicio) + "' ";
		}
		if (dataFim != null) {
			sqlStr += " AND requerimento.data <= '" + Uteis.getDataJDBC(dataFim) + "' ";
		}
		if (disciplina.intValue() != 0 && disciplina != null) {
			sqlStr += " AND requerimento.disciplina = " + disciplina.intValue() + " ";
		}
		if (curso.intValue() != 0 && curso != null) {
			sqlStr += " AND matricula.curso = " + curso.intValue() + " ";
		}
		if (turma.intValue() != 0 && turma != null) {
			sqlStr += " AND matriculaperiodo.turma = " + turma.intValue() + " ";
		}
		if (ordenarPor.equals("unidadeEnsino")) {
			sqlStr += " ORDER BY unidadeensino.nome, pessoa.nome";
		}
		if (ordenarPor.equals("curso")) {
			sqlStr += " ORDER BY curso.nome, pessoa.nome";
		}
		if (ordenarPor.equals("turma")) {
			sqlStr += " ORDER BY turma.identificadorturma, pessoa.nome";
		}
		if (ordenarPor.equals("disciplina")) {
			sqlStr += " ORDER BY disciplina.nome, pessoa.nome";
		}
		if (ordenarPor.equals("requerimento")) {
			sqlStr += " ORDER BY requerimento.data, pessoa.nome";
		}
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		List<RequerimentoVO> vetResultado = new ArrayList<>(0);
		while (dadosSQL.next()) {
			RequerimentoVO req = new RequerimentoVO();
			req.setNovoObj(Boolean.FALSE);
			req.setCodigo(new Integer(dadosSQL.getInt("codigo")));
			req.setData(dadosSQL.getDate("data"));
			req.setObservacao(dadosSQL.getString("observacao"));
			req.getMatricula().setMatricula(dadosSQL.getString("matricula"));
			req.getMatricula().getAluno().setNome(dadosSQL.getString("aluno"));
			req.getMatricula().getAluno().setCodigo(dadosSQL.getInt("codAluno"));
			req.getMatricula().getCurso().setNome(dadosSQL.getString("curso"));
			req.getMatricula().setData(dadosSQL.getDate("datamatricula"));
			req.getTurma().setIdentificadorTurma(dadosSQL.getString("turmaorigem"));
			req.getDisciplina().setNome(dadosSQL.getString("disciplina"));
			req.setSituacao(dadosSQL.getString("situacao"));
			req.setSituacaoFinanceira(dadosSQL.getString("situacaoFinanceira"));
			req.getTurmaReposicao().setIdentificadorTurma(dadosSQL.getString("turmareposicao"));
			req.setQtdAlunosReposicaoTurma(consultarQtdAlunosReposicaoTurma(dadosSQL.getInt("codturmareposicao"), obj, curso, turma, disciplina, dataInicio, dataFim, unidadeEnsino, ordenarPor, controlarAcesso, nivelMontarDados, usuario));
//			req.setDataAulaReprovado(getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorTurmaDisciplina(dadosSQL.getInt("codTurmaorigem"), dadosSQL.getInt("codDisciplina"), dadosSQL.getString("ano"), dadosSQL.getString("semestre"), null));
			vetResultado.add(req);
		}
		return vetResultado;
	}

	public Integer consultarQtdAlunosReposicaoTurma(Integer codTurmaRep, RequerimentoVO obj, Integer curso, Integer turma, Integer disciplina, Date dataInicio, Date dataFim, Integer unidadeEnsino, String ordenarPor, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {

		String sqlStr = " select count(requerimento.codigo) as qtd " + " from requerimento " + " inner join matricula on matricula.matricula = requerimento.matricula " + " inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula " + " inner join pessoa on pessoa.codigo = matricula.aluno " + " inner join curso on curso.codigo = matricula.curso " + " left join turma on matriculaperiodo.turma = turma.codigo " + " left join turma rep on rep.codigo = requerimento.turma " + " inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino " + " left join disciplina on disciplina.codigo = requerimento.disciplina " + " left join historico on (historico.matriculaperiodo = matriculaperiodo.codigo and historico.disciplina = disciplina.codigo and (historico.situacao = 'RE' or historico.situacao = 'RF')) " + " where requerimento.situacao <> 'FD' and requerimento.situacao <> 'FI'" + " and (matriculaperiodo.situacaomatriculaperiodo = 'AT') ";
		if (obj.getTipoRequerimento().getCodigo().intValue() != 0 && obj.getTipoRequerimento().getCodigo() != null) {
			sqlStr += " AND requerimento.tiporequerimento = " + obj.getTipoRequerimento().getCodigo().intValue() + " ";
		}
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr += " AND requerimento.unidadeensino = " + unidadeEnsino.intValue() + " ";
		}
		if (obj.getCodigo().intValue() != 0 && obj.getCodigo() != null) {
			sqlStr += " AND requerimento.codigo = " + obj.getCodigo() + " ";
		}
		if (!obj.getMatricula().getMatricula().equals("") && obj.getMatricula().getMatricula() != null) {
			sqlStr += " AND requerimento.matricula = '" + obj.getMatricula().getMatricula() + "' ";
		}
		if (dataInicio != null) {
			sqlStr += " AND requerimento.data >= '" + Uteis.getDataJDBC(dataInicio) + "' ";
		}
		if (dataFim != null) {
			sqlStr += " AND requerimento.data <= '" + Uteis.getDataJDBC(dataFim) + "' ";
		}
		if (disciplina.intValue() != 0 && disciplina != null) {
			sqlStr += " AND requerimento.disciplina = " + disciplina.intValue() + " ";
		}
		if (curso.intValue() != 0 && curso != null) {
			sqlStr += " AND requerimento.curso = " + curso.intValue() + " ";
		}
		if (turma.intValue() != 0 && turma != null) {
			sqlStr += " AND matriculaperiodo.turma = " + turma.intValue() + " ";
		}
		if (codTurmaRep.intValue() != 0 && codTurmaRep != null) {
			sqlStr += " AND rep.codigo = " + codTurmaRep.intValue() + " ";
		}
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		while (dadosSQL.next()) {
			if (new Integer(dadosSQL.getInt("qtd")) != null) {
				return new Integer(dadosSQL.getInt("qtd"));
			}
		}
		return 0;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarRegistroNotificacaoRequerimentoEmAtraso(List<Integer> requerimentos) {
		StringBuilder sql = new StringBuilder("UPDATE requerimento set notificado = true where codigo in (0 ");
		for (Integer codigo : requerimentos) {
			sql.append(", ").append(codigo);
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	@Override
	public SqlRowSet consultarDadosParaNotificacaoAtraso() {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select requerimento.codigo, requerimento.data, tipoRequerimento.nome as tipoRequerimento, Pessoa.nome as \"Pessoa.nome\", ");
		sql.append(" Pessoa.codigo as \"Pessoa.codigo\", Pessoa.email as \"Pessoa.email\", aluno.nome as requerente, unidadeensino.codigo as unidadeensino, cidtiporequerimento.codcid as codcid, cidtiporequerimento.descricao as descricaocid, cidtiporequerimento.codigo as codigocid ");
		sql.append(" from requerimento ");

		sql.append(" inner join departamento on departamento.codigo = requerimento.departamentoResponsavel ");
		sql.append(" inner join unidadeEnsino on unidadeEnsino.codigo = requerimento.unidadeEnsino ");
		sql.append(" inner join Configuracoes on Configuracoes.codigo = unidadeEnsino.configuracoes ");
		sql.append(" inner join ConfiguracaoGeralSistema on ConfiguracaoGeralSistema.configuracoes = Configuracoes.codigo ");
		sql.append(" inner join tipoRequerimento on requerimento.tipoRequerimento = tipoRequerimento.codigo ");
		sql.append(" inner join Pessoa on departamento.responsavel = pessoa.codigo ");
		sql.append(" inner join Pessoa as aluno on requerimento.pessoa = aluno.codigo ");
		sql.append(" left join cidtiporequerimento on requerimento.codigocid = cidtiporequerimento.codigo ");
		sql.append(" where requerimento.situacaoFinanceira != 'PE' and requerimento.situacao in ('EX', 'PE') ");
		sql.append(" and configuracaoGeralSistema.qtdeDiasAlertaRequerimento > 0 ");
		sql.append(" and requerimento.dataPrevistaFinalizacao <= (current_date + configuracaoGeralSistema.qtdeDiasAlertaRequerimento) ");
		sql.append(" and notificado = false ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	@Override
	public List<RequerimentoVO> consultarOtimizado(RequerimentoVO obj, List<UnidadeEnsinoVO> listaUnidadeEnsino, List<SelectItem> listaSelectItemDepartamento, CursoVO curso, TurmaVO turma, String situacao, String situacaoFinanceira, Date dataIni, Date dataFim, boolean todoPeriodo, String sigla, Integer situacaoRequerimentoDepartamento, String ordenarPor, boolean ordemCrescente, boolean controlarAcesso, 
			UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, int limit, int offset, Boolean permitirConsultarTodasUnidades, Boolean permiteConsultarRequerimentoOutrosResponsaveis, Boolean permiteConsultarRequerimentoOutrosResponsaveisMesmoDepartanento,  Boolean permitirUsuarioConsultarIncluirApenasRequerimentosProprios, DataModelo dataModelo) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);		
		StringBuffer sqlStr = new StringBuffer(" WITH cte_requerimento AS (");
		realizarMontagemSQLPadraoConsulta(sqlStr, turma);
		if (situacao.equals("AD") || situacao.equals("ED") || situacao.equals("AA")) {
			sqlStr.append(" inner join TipoRequerimentodepartamento on TipoRequerimentodepartamento.TipoRequerimento = TipoRequerimento.codigo ");
			sqlStr.append(" and TipoRequerimentodepartamento.departamento = Requerimento.departamentoresponsavel ");
			sqlStr.append(" and TipoRequerimentodepartamento.ordemexecucao = Requerimento.ordemexecucaotramitedepartamento ");
			sqlStr.append(" inner JOIN RequerimentoHistorico on RequerimentoHistorico.requerimento = requerimento.codigo ");
			sqlStr.append(" and RequerimentoHistorico.codigo = (select max(rh.codigo) from RequerimentoHistorico rh where rh.requerimento = requerimento.codigo ) ");
		}
		if (situacao.equals("AA")) {
			sqlStr.append(" inner join TipoRequerimentodepartamento proximo on proximo.TipoRequerimento = TipoRequerimento.codigo ");
			sqlStr.append(" and proximo.ordemexecucao = (Requerimento.ordemexecucaotramitedepartamento +1) ");
		}
		sqlStr.append(realizarGeracaoCondicaoWhereConsultaRequerimento(obj, listaUnidadeEnsino, listaSelectItemDepartamento, curso, turma, situacao, situacaoFinanceira, dataIni, dataFim, todoPeriodo, sigla, situacaoRequerimentoDepartamento, usuario, configuracaoGeralSistemaVO, permitirConsultarTodasUnidades, permiteConsultarRequerimentoOutrosResponsaveis, permiteConsultarRequerimentoOutrosResponsaveisMesmoDepartanento, permitirUsuarioConsultarIncluirApenasRequerimentosProprios));
		montarRegraOrdenacaoConsultaRequerimento(ordenarPor, ordemCrescente, sqlStr);
		if(limit > 0) {
			sqlStr.append(" limit ").append(limit);
			sqlStr.append(" offset  ").append(offset);
		}
		sqlStr.append(")");
		sqlStr.append(getSQLPadraoConsultaCompleta(true));
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (dataModelo != null) {
			dataModelo.setTotalRegistrosEncontrados(0);
			if (tabelaResultado.next()) {
				dataModelo.setTotalRegistrosEncontrados((tabelaResultado.getInt("totalregistros")));
			}
			tabelaResultado.beforeFirst();
		}
		return montarDadosConsultaCompleta(tabelaResultado, usuario);
	}

	private void montarRegraOrdenacaoConsultaRequerimento(String ordenarPor, boolean ordemCrescente, StringBuffer sqlStr) {
		if (ordenarPor.equals("nomeRequerente")) {
			if (ordemCrescente) {
				sqlStr.append(" ORDER BY \"nomerequerente.ordenar\" asc ");
			} else {
				sqlStr.append(" ORDER BY \"nomerequerente.ordenar\" desc ");
			}
		} else if (ordenarPor.equals("dataRequerimento")) {
			if (ordemCrescente) {
				sqlStr.append(" ORDER BY requerimento.data asc, \"nomerequerente.ordenar\" asc ");
			} else {
				sqlStr.append(" ORDER BY requerimento.data desc, \"nomerequerente.ordenar\" asc ");
			}
		} else if (ordenarPor.equals("dataPrevistaFinalizacao")) {
			if (ordemCrescente) {
				sqlStr.append(" ORDER BY requerimento.dataPrevistaFinalizacao asc, \"nomerequerente.ordenar\" asc ");
			} else {
				sqlStr.append(" ORDER BY requerimento.dataPrevistaFinalizacao desc, \"nomerequerente.ordenar\" asc ");
			}
		} else if (ordenarPor.equals("dataTramiteDepartamento")) {
			if (ordemCrescente) {
				sqlStr.append(" ORDER BY requerimento.dataUltimaAlteracao asc, \"nomerequerente.ordenar\" asc ");
			} else {
				sqlStr.append(" ORDER BY requerimento.dataUltimaAlteracao desc, \"nomerequerente.ordenar\" asc ");
			}
		} else if (ordenarPor.equals("tipoRequerimento")) {
			if (ordemCrescente) {
				sqlStr.append(" ORDER BY tipoRequerimento.nome asc, \"nomerequerente.ordenar\" asc ");
			} else {
				sqlStr.append(" ORDER BY tipoRequerimento.nome desc, \"nomerequerente.ordenar\" asc ");
			}
		}
	}
	
	private StringBuilder realizarGeracaoCondicaoWhereConsultaRequerimento(RequerimentoVO obj, List<UnidadeEnsinoVO> listaUnidadeEnsino, List<SelectItem> listaSelectItemDepartamento, CursoVO curso, TurmaVO turma, String situacao, String situacaoFinanceira, Date dataIni, Date dataFim, boolean todoPeriodo, String sigla, Integer situacaoRequerimentoDepartamento, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean permitirConsultarTodasUnidades, Boolean permiteConsultarRequerimentoOutrosResponsaveis, Boolean permiteConsultarRequerimentoOutrosResponsaveisMesmoDepartamento,  Boolean permitirUsuarioConsultarIncluirApenasRequerimentosProprios) {
		StringBuilder sqlStr = new StringBuilder("");
		
		Boolean unidadeEnsino = Boolean.FALSE;
		for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidadeEnsino) {
			if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
				unidadeEnsino = Boolean.TRUE;
				continue;
			}

		}
		if (unidadeEnsino) {
			if (!listaUnidadeEnsino.isEmpty()) {
				sqlStr.append("  WHERE (unidadeensino.codigo in (");
				int x = 0;
				for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidadeEnsino) {
					if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
						if (x > 0) {
							sqlStr.append(", ");
						}
						sqlStr.append(unidadeEnsinoVO.getCodigo());
						x++;
					}

				}
				sqlStr.append(" ) ");
				
				if (Uteis.isAtributoPreenchido(permitirUsuarioConsultarIncluirApenasRequerimentosProprios) && permitirUsuarioConsultarIncluirApenasRequerimentosProprios) {
							sqlStr.append(" or requerimento.pessoa = ").append(usuario.getPessoa().getCodigo());
							sqlStr.append(" or requerimento.responsavel = ").append(usuario.getPessoa().getCodigo());
						}
				sqlStr.append(" ) ");
			}
		} else {
			sqlStr.append(" WHERE unidadeensino.codigo != 0 ");
		}

		if (obj.getCodigo() != null && obj.getCodigo() != 0) {
			sqlStr.append(" AND requerimento.codigo = ").append(obj.getCodigo());
		}
		if(Uteis.isAtributoPreenchido(situacaoRequerimentoDepartamento)) {
			sqlStr.append(" AND requerimento.situacaoRequerimentoDepartamento = ").append(situacaoRequerimentoDepartamento);
		}
		if (obj.getFuncionarioVO().getDepartamento().getCodigo() != 0 && !situacao.equals("AA")) {
			sqlStr.append(" AND requerimento.departamentoresponsavel = ").append(obj.getFuncionarioVO().getDepartamento().getCodigo());
		} else if (situacao.equals("AA") && obj.getFuncionarioVO().getDepartamento().getCodigo() != 0) {
			sqlStr.append(" and proximo.departamento = ").append(obj.getFuncionarioVO().getDepartamento().getCodigo());
		} else {
			if (listaSelectItemDepartamento != null && !listaSelectItemDepartamento.isEmpty()) {
				if (situacao.equals("AA")) {
					sqlStr.append("  and proximo.departamento in (");
				} else {
					sqlStr.append("  and (requerimento.departamentoresponsavel in (");
				}

				int x = 0;
				Iterator i = null;
				i = listaSelectItemDepartamento.iterator();
				while (i.hasNext()) {
					SelectItem item = (SelectItem) i.next();
					if (x > 0) {
						sqlStr.append(", ");
					}
					sqlStr.append(item.getValue());
					x++;

				}
				sqlStr.append(" ) ");
				if (Uteis.isAtributoPreenchido(permitirUsuarioConsultarIncluirApenasRequerimentosProprios) && permitirUsuarioConsultarIncluirApenasRequerimentosProprios) {
					sqlStr.append(" or requerimento.pessoa = ").append(usuario.getPessoa().getCodigo());
					sqlStr.append(" or requerimento.responsavel = ").append(usuario.getPessoa().getCodigo());
				}
				sqlStr.append(" ) ");
			}
		}		
		if (Uteis.isAtributoPreenchido(permitirUsuarioConsultarIncluirApenasRequerimentosProprios) && permitirUsuarioConsultarIncluirApenasRequerimentosProprios && 
				(permitirConsultarTodasUnidades == null || !permitirConsultarTodasUnidades)
				&& (permiteConsultarRequerimentoOutrosResponsaveis == null || !permiteConsultarRequerimentoOutrosResponsaveis)
				&& (permiteConsultarRequerimentoOutrosResponsaveisMesmoDepartamento == null || !permiteConsultarRequerimentoOutrosResponsaveisMesmoDepartamento)) {
			sqlStr.append(" AND (requerimento.pessoa = ").append(usuario.getPessoa().getCodigo());
			if (obj.getResponsavel().getPessoa().getCodigo() != 0) {
				sqlStr.append(" or  pessoaFuncionario.codigo = ").append(obj.getResponsavel().getPessoa().getCodigo());
			}
			sqlStr.append(" or requerimento.responsavel = ").append(usuario.getCodigo()).append(" )");
		}else if (obj.getResponsavel().getPessoa().getCodigo() != 0) {
			sqlStr.append(" AND  (pessoaFuncionario.codigo = ").append(obj.getResponsavel().getPessoa().getCodigo());
			if (Uteis.isAtributoPreenchido(permitirUsuarioConsultarIncluirApenasRequerimentosProprios) && permitirUsuarioConsultarIncluirApenasRequerimentosProprios) {
				sqlStr.append(" or requerimento.pessoa = ").append(usuario.getPessoa().getCodigo());
				sqlStr.append(" or requerimento.responsavel = ").append(usuario.getCodigo());
			}
			sqlStr.append(" ) ");
			
		}
		if (obj.getPessoa().getCodigo() != 0 && !obj.getMatricula().getMatricula().equals("")) {
			sqlStr.append(" AND p1.codigo = ").append(obj.getPessoa().getCodigo());
		}
		if (obj.getPessoa().getCodigo() != 0 && obj.getMatricula().getMatricula().equals("")) {
			sqlStr.append(" AND p2.codigo = ").append(obj.getPessoa().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getMatricula().getAluno())) {
			sqlStr.append(" AND Matricula.aluno = ").append(obj.getMatricula().getAluno().getCodigo()).append("");
		}			
		if (curso != null && curso.getCodigo() != 0) {
			sqlStr.append(" AND Matricula.curso = ").append(curso.getCodigo());
		}
		if (turma != null && turma.getCodigo() != 0) {
			sqlStr.append(" AND MatriculaPeriodo.turma = ").append(turma.getCodigo());
		}
		if (situacaoFinanceira != null && !situacaoFinanceira.equals("")) {
			if (situacaoFinanceira.equals("PI")) {
				sqlStr.append(" AND requerimento.situacaoFinanceira in('PG', 'IS')");				
			} else if(situacaoFinanceira.equals("SI")) {
				sqlStr.append(" AND requerimento.situacaoFinanceira not in ('PG', 'IS') ");
				sqlStr.append(" AND requerimento.situacaoIsencaoTaxa = '").append(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.AGUARDANDO_RESPOSTA.name()).append("' ");
			} else if(situacaoFinanceira.equals("SID")) {				
				sqlStr.append(" AND requerimento.situacaoIsencaoTaxa = '").append(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.DEFERIDO.name()).append("' ");
			} else if(situacaoFinanceira.equals("SII")) {				
				sqlStr.append(" AND requerimento.situacaoIsencaoTaxa = '").append(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.INDEFERIDO.name()).append("' ");
			} else {
				sqlStr.append(" AND requerimento.situacaoFinanceira = '").append(situacaoFinanceira).append("' ");
			}
		}
		if (situacao != null && !situacao.equals("")) {
			if (situacao.equals("AT")) {
				sqlStr.append(" AND (requerimento.dataprevistafinalizacao::DATE < CURRENT_DATE and (requerimento.situacao = 'PE' or requerimento.situacao = 'EX' or requerimento.situacao = 'AP' or requerimento.situacao = 'PR')) ");
			} else if (situacao.equals("AD")) {// Atrasado Departamento
				sqlStr.append(" and RequerimentoHistorico.dataconclusaodepartamento is null ");
				sqlStr.append(" and RequerimentoHistorico.dataentradadepartamento is not null ");
				sqlStr.append(" and RequerimentoHistorico.dataentradadepartamento + (TipoRequerimentodepartamento.prazoexecucao::VARCHAR||' day')::INTERVAL < current_date ");
			} else if (situacao.equals("ED")) {// Aguardando Execução no
												// Departamento
				sqlStr.append(" and RequerimentoHistorico.datainicioexecucaodepartamento is null ");
			
			} else if (situacao.equals("PD")) {
				sqlStr.append(" AND requerimento.situacao in ('EX', 'PE')");
				sqlStr.append(" AND requerimento.dataprevistafinalizacao::DATE >= CURRENT_DATE ");
			} else if (situacao.equals("FD") || situacao.equals("FI")) {
				sqlStr.append(" AND requerimento.situacao = '").append(situacao).append("' ");
			} else if (situacao.contains("requerimentoOperacaoLote")) {
				sqlStr.append(" AND requerimento.situacao in ('EX', 'PE')");
				sqlStr.append(" and Requerimento.ordemexecucaotramitedepartamento = (select TipoRequerimentodepartamento.ordemexecucao from TipoRequerimentodepartamento where TipoRequerimentodepartamento.TipoRequerimento = TipoRequerimento.codigo order by TipoRequerimentodepartamento.ordemexecucao desc limit 1) ");				
			} else if (!situacao.equals("AA")) {
				sqlStr.append(" AND requerimento.situacao = '").append(situacao).append("' ");
				sqlStr.append(" AND requerimento.dataprevistafinalizacao::DATE >= CURRENT_DATE ");
			}
		}
		if (!todoPeriodo) {
			if (dataIni != null) {
				sqlStr.append(" AND requerimento.data >= '").append(Uteis.getDataJDBC(dataIni)).append("' ");
			}
			if (dataFim != null) {
				sqlStr.append(" AND requerimento.data <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
			}
		}
		if (obj.getTipoRequerimento().getCodigo() != 0) {
			sqlStr.append(" AND tipoRequerimento.codigo = ").append(obj.getTipoRequerimento().getCodigo());
		}

		if (sigla != null && !sigla.equals("")) {
			sqlStr.append(" AND requerimento.sigla like '%%").append(sigla).append("%%'");
		}
		if (Uteis.isAtributoPreenchido(obj.getDisciplina())) {
			sqlStr.append(" AND (requerimento.disciplina = ").append(obj.getDisciplina().getCodigo());
			sqlStr.append(" OR EXISTS (SELECT FROM requerimentodisciplinasaproveitadas WHERE requerimentodisciplinasaproveitadas.requerimento = requerimento.codigo AND requerimentodisciplinasaproveitadas.disciplina = ").append(obj.getDisciplina().getCodigo() + ")");
			sqlStr.append(" OR EXISTS (SELECT FROM requerimentodisciplina WHERE requerimentodisciplina.requerimento = requerimento.codigo AND requerimentodisciplina.disciplina = ").append(obj.getDisciplina().getCodigo() + "))");
			
		}
		return sqlStr;
	}

	@Override
	public Integer consultarTotalRegistros(RequerimentoVO obj, List<UnidadeEnsinoVO> listaUnidadeEnsino, List<SelectItem> listaSelectItemDepartamento, CursoVO curso, TurmaVO turma, String situacao, String situacaoFinanceira, Date dataIni, Date dataFim, boolean todoPeriodo, String sigla, Integer situacaoRequerimentoDepartamento, String ordenarPor, boolean ordemCrescente, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean permitirConsultarTodasUnidades, Boolean permiteConsultarRequerimentoOutrosResponsaveis, Boolean permiteConsultarRequerimentoOutrosResponsaveisMesmoDepartamento,  Boolean permitirUsuarioConsultarIncluirApenasRequerimentosProprios) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);

		
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" SELECT count(distinct Requerimento.codigo) as totalRegistro ");
		sqlStr.append("FROM Requerimento ");
		sqlStr.append("INNER JOIN TipoRequerimento on Requerimento.tipoRequerimento = TipoRequerimento.codigo  ");
		sqlStr.append("INNER JOIN UnidadeEnsino on requerimento.unidadeEnsino = unidadeEnsino.codigo ");
		sqlStr.append("LEFT JOIN Funcionario on Funcionario.codigo = Requerimento.funcionario  ");
		sqlStr.append("LEFT JOIN Pessoa as pessoaFuncionario on Funcionario.pessoa = pessoaFuncionario.codigo  ");
		sqlStr.append("LEFT JOIN Matricula on matricula.matricula = requerimento.matricula ");
		if (turma.getCodigo() != 0) {
			sqlStr.append("LEFT JOIN MatriculaPeriodo on MatriculaPeriodo.matricula = Matricula.matricula ");
		}
		sqlStr.append("LEFT JOIN Pessoa p1 on p1.codigo = matricula.aluno ");
		sqlStr.append("LEFT JOIN Pessoa p2 on p2.codigo = requerimento.pessoa ");
		sqlStr.append("LEFT JOIN Curso on curso.codigo = requerimento.curso ");

		if (situacao.equals("AD") || situacao.equals("ED") || situacao.equals("AA")) {// Atrasado
																						// Departamento
																						// Aguardando
																						// Execução
																						// no
																						// Departamento
																						// Aguardando
																						// Tramite
																						// Departamento
																						// Anterior
			sqlStr.append(" inner join TipoRequerimentodepartamento on TipoRequerimentodepartamento.TipoRequerimento = TipoRequerimento.codigo ");
			sqlStr.append(" and TipoRequerimentodepartamento.departamento = Requerimento.departamentoresponsavel ");
			sqlStr.append(" and TipoRequerimentodepartamento.ordemexecucao = Requerimento.ordemexecucaotramitedepartamento ");
			sqlStr.append(" inner JOIN RequerimentoHistorico on RequerimentoHistorico.requerimento = requerimento.codigo ");
			sqlStr.append(" and RequerimentoHistorico.codigo = (select max(rh.codigo) from RequerimentoHistorico rh where rh.requerimento = requerimento.codigo ) ");
		}
		if (situacao.equals("AA")) {
			sqlStr.append(" inner join TipoRequerimentodepartamento proximo on proximo.TipoRequerimento = TipoRequerimento.codigo ");
			sqlStr.append(" and proximo.ordemexecucao = (Requerimento.ordemexecucaotramitedepartamento +1) ");
		}
		sqlStr.append(realizarGeracaoCondicaoWhereConsultaRequerimento(obj, listaUnidadeEnsino, listaSelectItemDepartamento, curso, turma, situacao, situacaoFinanceira, dataIni, dataFim, todoPeriodo, sigla, situacaoRequerimentoDepartamento, usuario, configuracaoGeralSistemaVO, permitirConsultarTodasUnidades, permiteConsultarRequerimentoOutrosResponsaveis, permiteConsultarRequerimentoOutrosResponsaveisMesmoDepartamento, permitirUsuarioConsultarIncluirApenasRequerimentosProprios));
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("totalRegistro");
		} else {
			return 0;
		}
	}

	@Override
	public String consultarDadosGraficoRequerimento(RequerimentoVO obj, List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<SelectItem> departamentoVOs, CursoVO curso, TurmaVO turma, String situacao, String situacaoFinanceira, Date dataIni, Date dataFim, boolean todoPeriodo, String sigla, Integer situacaoRequerimentoDepartamento, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean permitirConsultarTodasUnidades, Boolean permiteConsultarRequerimentoOutrosResponsaveis, Boolean permiteConsultarRequerimentoOutrosResponsaveisMesmoDepartamento,  Boolean permitirUsuarioConsultarIncluirApenasRequerimentosProprios) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		
		StringBuffer sqlStr = new StringBuffer("");
		if (situacao.equals("AT")) {
			sqlStr.append("SELECT * FROM ( ");
		}
		sqlStr.append("SELECT situacao, SUM(qtde) AS qtde FROM ( ");
		sqlStr.append("SELECT CASE WHEN (requerimento.dataprevistafinalizacao::DATE < CURRENT_DATE AND ( ");
		sqlStr.append("requerimento.situacao = 'PE' or ");
		sqlStr.append("requerimento.situacao = 'EX' or requerimento.situacao = 'AP')) then 'AT' ELSE requerimento.situacao END, ");
		sqlStr.append("COUNT(requerimento.codigo) as qtde FROM requerimento ");
		sqlStr.append("INNER JOIN TipoRequerimento on Requerimento.tipoRequerimento = TipoRequerimento.codigo ");
		sqlStr.append("INNER JOIN UnidadeEnsino on requerimento.unidadeEnsino = unidadeEnsino.codigo ");
		sqlStr.append("LEFT JOIN Funcionario on requerimento.funcionario = Funcionario.codigo  ");
		sqlStr.append("LEFT JOIN Pessoa as pessoaFuncionario on Funcionario.pessoa = pessoaFuncionario.codigo ");
		sqlStr.append("LEFT JOIN Matricula on matricula.matricula = requerimento.matricula ");
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append("LEFT JOIN MatriculaPeriodo on MatriculaPeriodo.matricula = Matricula.matricula ");
		}
		sqlStr.append("LEFT JOIN Pessoa p1 on p1.codigo = matricula.aluno ");
		sqlStr.append("LEFT JOIN Pessoa p2 on p2.codigo = requerimento.pessoa ");
		sqlStr.append("LEFT JOIN Curso ON Curso.codigo = matricula.curso ");
		if (situacao.equals("AD") || situacao.equals("ED") || situacao.equals("AA")) {
			sqlStr.append("INNER JOIN TipoRequerimentodepartamento on TipoRequerimentodepartamento.TipoRequerimento = TipoRequerimento.codigo ");
			sqlStr.append("AND TipoRequerimentodepartamento.departamento = Requerimento.departamentoresponsavel ");
			sqlStr.append("AND TipoRequerimentodepartamento.ordemexecucao = Requerimento.ordemexecucaotramitedepartamento ");
			sqlStr.append("INNER JOIN RequerimentoHistorico on RequerimentoHistorico.requerimento = requerimento.codigo ");
			sqlStr.append("AND RequerimentoHistorico.codigo = (select max(rh.codigo) from RequerimentoHistorico rh where rh.requerimento = requerimento.codigo) ");
		}
		if (situacao.equals("AA")) {
			sqlStr.append("INNER JOIN TipoRequerimentodepartamento proximo on proximo.TipoRequerimento = TipoRequerimento.codigo ");
			sqlStr.append("AND proximo.ordemexecucao = (Requerimento.ordemexecucaotramitedepartamento + 1) ");
		}
		sqlStr.append(realizarGeracaoCondicaoWhereConsultaRequerimento(obj, unidadeEnsinoVOs, departamentoVOs, curso, turma, situacao, situacaoFinanceira, dataIni, dataFim, todoPeriodo, sigla, situacaoRequerimentoDepartamento, usuario, configuracaoGeralSistemaVO, permitirConsultarTodasUnidades, permiteConsultarRequerimentoOutrosResponsaveis, permiteConsultarRequerimentoOutrosResponsaveisMesmoDepartamento, permitirUsuarioConsultarIncluirApenasRequerimentosProprios));
		sqlStr.append(" AND requerimento.situacao not in('FD', 'FI') ");
		sqlStr.append(" GROUP BY requerimento.situacao, requerimento.dataprevistafinalizacao ");
		sqlStr.append(" ) AS t GROUP BY situacao ");
		if (situacao.equals("AT")) {
			sqlStr.append(" ) AS t1 WHERE situacao = '").append(situacao).append("'");
		}
		return montarDadosGrafico(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()));
	}

	public String montarDadosGrafico(SqlRowSet rs) {
		StringBuilder grafico = new StringBuilder("");
		int x = 0;
		while (rs.next()) {
			if (x > 0) {
				grafico.append(", ");
			}
			String cor = "blue";
			if (SituacaoRequerimento.getEnum(rs.getString("situacao")).equals(SituacaoRequerimento.EM_EXECUCAO)) {
				cor = "#007bff"; // azul claro
			}
			if (SituacaoRequerimento.getEnum(rs.getString("situacao")).equals(SituacaoRequerimento.AGUARDANDO_PAGAMENTO)) {
				cor = "#6c757d"; // cinza
			}
			if (SituacaoRequerimento.getEnum(rs.getString("situacao")).equals(SituacaoRequerimento.FINALIZADO_DEFERIDO)) {
				cor = "#28a745"; // verde
			}
			if (SituacaoRequerimento.getEnum(rs.getString("situacao")).equals(SituacaoRequerimento.ISENTO)) {
				cor = "#007bff";
			}
			if (SituacaoRequerimento.getEnum(rs.getString("situacao")).equals(SituacaoRequerimento.PENDENTE)) {
				cor = "#ffc107"; // amarelo
			}
			if (SituacaoRequerimento.getEnum(rs.getString("situacao")).equals(SituacaoRequerimento.PRONTO_PARA_RETIRADA)) {
				cor = "#17a2b8"; // azul escuro
			}
			if (SituacaoRequerimento.getEnum(rs.getString("situacao")).equals(SituacaoRequerimento.ATRASADO)) {
				cor = "#dc3545"; // vermelho
			}
			grafico.append(" {name: '").append(SituacaoRequerimento.getDescricao(rs.getString("situacao")) + " (" + rs.getString("qtde") + ")").append("', data: [").append(rs.getString("qtde")).append("], color: '").append(cor).append("'} ");
			x++;
		}
		return grafico.toString();
	}

	public String consultarDadosGraficoPainelGestorRequerimento(List<UnidadeEnsinoVO> unidadeEnsinoVOs, String situacao, Boolean permissaoConsultarOutrosDepartamentosConsultoresGrafico, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		Boolean unidadeEnsino = false;
		for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
			if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
				unidadeEnsino = true;
				continue;
			}
		}
		StringBuffer sqlStr = new StringBuffer("");
		if (situacao.equals("AT")) {
			sqlStr.append("SELECT * FROM ( ");
		}
		/**
		 * Monta o gráfico com requerimentos vinculados ao usuario, ou seja,
		 * requerimentos que foram criados por ele.
		 */
		sqlStr.append("SELECT situacao, SUM(qtde) AS qtde FROM ( ");
		sqlStr.append("SELECT CASE WHEN (g.dataprevistafinalizacao::date < current_date AND ( ");
		sqlStr.append("g.situacao = 'PE' or ");
		sqlStr.append("g.situacao = 'EX' or g.situacao = 'AP')) then 'AT' ELSE g.situacao END, ");
		sqlStr.append("COUNT(g.codigo) as qtde from ( ");
		sqlStr.append("select distinct t.* from ( ");
		sqlStr.append("select requerimento.situacao, requerimento.codigo, requerimento.dataprevistafinalizacao ");
		sqlStr.append("FROM requerimento ");
		sqlStr.append("INNER JOIN TipoRequerimento on Requerimento.tipoRequerimento = TipoRequerimento.codigo ");
		sqlStr.append("INNER JOIN UnidadeEnsino on requerimento.unidadeEnsino = unidadeEnsino.codigo ");
		sqlStr.append("LEFT JOIN Funcionario on requerimento.funcionario = Funcionario.codigo ");
		sqlStr.append("LEFT JOIN Pessoa as pessoaFuncionario on Funcionario.pessoa = pessoaFuncionario.codigo ");
		sqlStr.append("LEFT JOIN Matricula on matricula.matricula = requerimento.matricula ");
		sqlStr.append("LEFT JOIN Pessoa p1 on p1.codigo = matricula.aluno ");
		sqlStr.append("LEFT JOIN Pessoa p2 on p2.codigo = requerimento.pessoa ");
		sqlStr.append("LEFT JOIN Curso ON Curso.codigo = matricula.curso ");
		sqlStr.append("LEFT JOIN turma ON Turma.codigo = requerimento.turma ");
		if (situacao.equals("AD") || situacao.equals("ED") || situacao.equals("AA")) {
			sqlStr.append("INNER JOIN TipoRequerimentodepartamento on TipoRequerimentodepartamento.TipoRequerimento = TipoRequerimento.codigo ");
			sqlStr.append("AND TipoRequerimentodepartamento.departamento = Requerimento.departamentoresponsavel ");
			sqlStr.append("AND TipoRequerimentodepartamento.ordemexecucao = Requerimento.ordemexecucaotramitedepartamento ");
			sqlStr.append("INNER JOIN RequerimentoHistorico on RequerimentoHistorico.requerimento = requerimento.codigo ");
			sqlStr.append("AND RequerimentoHistorico.codigo = (select max(rh.codigo) from RequerimentoHistorico rh where rh.requerimento = requerimento.codigo) ");
		}
		if (situacao.equals("AA")) {
			sqlStr.append("INNER JOIN TipoRequerimentodepartamento proximo on proximo.TipoRequerimento = TipoRequerimento.codigo ");
			sqlStr.append("AND proximo.ordemexecucao = (Requerimento.ordemexecucaotramitedepartamento + 1) ");
		}
		if (unidadeEnsino) {
			sqlStr.append(" WHERE unidadeensino.codigo in (");
			for (UnidadeEnsinoVO ue : unidadeEnsinoVOs) {
				if (ue.getFiltrarUnidadeEnsino()) {
					sqlStr.append(ue.getCodigo()).append(", ");
				}
			}
			sqlStr.append("0) ");
		} else {
			sqlStr.append(" WHERE unidadeensino.codigo != 0 ");
		}
		if (!permissaoConsultarOutrosDepartamentosConsultoresGrafico) {
			sqlStr.append(" AND pessoaFuncionario.codigo = ").append(usuario.getPessoa().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(situacao)) {
			if (situacao.equals("AT")) {
				sqlStr.append(" AND (requerimento.dataprevistafinalizacao < CURRENT_TIMESTAMP and (requerimento.situacao = 'PE' or requerimento.situacao = 'EX' or requerimento.situacao = 'AP' or requerimento.situacao = 'PR')) ");
			} else if (situacao.equals("AD")) {
				sqlStr.append(" AND RequerimentoHistorico.dataconclusaodepartamento is null ");
				sqlStr.append(" AND RequerimentoHistorico.dataentradadepartamento is not null ");
				sqlStr.append(" AND RequerimentoHistorico.dataentradadepartamento + (TipoRequerimentodepartamento.prazoexecucao::VARCHAR||'day')::INTERVAL < current_date ");
			} else if (situacao.equals("ED")) {
				sqlStr.append(" AND RequerimentoHistorico.datainicioexecucaodepartamento is null ");
			} else if (!situacao.equals("AA")) {
				sqlStr.append(" AND requerimento.situacao = '").append(situacao).append("' ");
				sqlStr.append(" AND requerimento.dataprevistafinalizacao >= CURRENT_TIMESTAMP ");
			}
		}
		sqlStr.append(" AND requerimento.situacao not in ('FD', 'FI') ");

		if (false) {

			/**
			 * Monta o gráfico com requerimentos vinculados ao departamento do
			 * usuario, ou seja, requerimentos de responsabilidade do
			 * departamento do usuario.
			 */
			sqlStr.append(" UNION ALL ");
			sqlStr.append(" SELECT requerimento.situacao, requerimento.codigo, requerimento.dataprevistafinalizacao ");
			sqlStr.append(" from requerimento ");
			sqlStr.append(" INNER JOIN TipoRequerimento on Requerimento.tipoRequerimento = TipoRequerimento.codigo ");
			sqlStr.append(" INNER JOIN UnidadeEnsino on requerimento.unidadeEnsino = unidadeEnsino.codigo ");
			sqlStr.append(" INNER JOIN Matricula on matricula.matricula = requerimento.matricula ");
			sqlStr.append(" INNER JOIN Curso ON Curso.codigo = matricula.curso ");
			sqlStr.append(" INNER JOIN turma ON Turma.codigo = requerimento.turma ");
			sqlStr.append(" INNER JOIN Pessoa p1 on p1.codigo = matricula.aluno ");
			sqlStr.append(" LEFT JOIN Funcionario on requerimento.funcionario = Funcionario.codigo  ");
			sqlStr.append(" LEFT JOIN Pessoa as pessoaFuncionario on Funcionario.pessoa = pessoaFuncionario.codigo  ");
			if (unidadeEnsino) {
				sqlStr.append(" WHERE unidadeensino.codigo in (");
				for (UnidadeEnsinoVO ue : unidadeEnsinoVOs) {
					if (ue.getFiltrarUnidadeEnsino()) {
						sqlStr.append(ue.getCodigo()).append(", ");
					}
				}
				sqlStr.append("0) ");

			} else {
				sqlStr.append(" WHERE unidadeensino.codigo != 0 ");
				sqlStr.append("AND (requerimento.departamentoresponsavel in( ");
				sqlStr.append("SELECT DISTINCT departamento.codigo FROM requerimento ");
				sqlStr.append("INNER JOIN UnidadeEnsino on requerimento.unidadeEnsino = unidadeEnsino.codigo ");
				sqlStr.append("LEFT JOIN Funcionario on requerimento.funcionario = Funcionario.codigo  ");
				sqlStr.append("LEFT JOIN Pessoa as pessoaFuncionario on Funcionario.pessoa = pessoaFuncionario.codigo ");
				sqlStr.append("INNER JOIN funcionariocargo on funcionariocargo.funcionario = funcionario.codigo and funcionariocargo.unidadeensino = unidadeensino.codigo ");
				sqlStr.append("INNER JOIN cargo on cargo.codigo = funcionariocargo.cargo ");
				sqlStr.append("INNER JOIN departamento on departamento.codigo = cargo.departamento ");
				sqlStr.append(" WHERE pessoaFuncionario.codigo = ").append(usuario.getPessoa().getCodigo());
				sqlStr.append(")) ");
			}
			if (Uteis.isAtributoPreenchido(situacao)) {
				if (situacao.equals("AT")) {
					sqlStr.append(" AND (requerimento.dataprevistafinalizacao < CURRENT_TIMESTAMP and (requerimento.situacao = 'PE' or requerimento.situacao = 'EX' or requerimento.situacao = 'AP' or requerimento.situacao = 'PR')) ");
				} else if (situacao.equals("AD")) {
					sqlStr.append(" AND RequerimentoHistorico.dataconclusaodepartamento is null ");
					sqlStr.append(" AND RequerimentoHistorico.dataentradadepartamento is not null ");
					sqlStr.append(" AND RequerimentoHistorico.dataentradadepartamento + (TipoRequerimentodepartamento.prazoexecucao::VARCHAR||'day')::INTERVAL < current_date ");
				} else if (situacao.equals("ED")) {
					sqlStr.append(" AND RequerimentoHistorico.datainicioexecucaodepartamento is null ");
				} else if (!situacao.equals("AA")) {
					sqlStr.append(" AND requerimento.situacao = '").append(situacao).append("' ");
					sqlStr.append(" AND requerimento.dataprevistafinalizacao >= CURRENT_TIMESTAMP ");
				}
			}
			sqlStr.append(" AND requerimento.situacao not in ('FD' ,'FI') ");
		}
		sqlStr.append(" ) as t) as g ");
		sqlStr.append(" GROUP BY g.situacao, g.dataprevistafinalizacao ");

		sqlStr.append(" ) AS t GROUP BY situacao ");
		if (situacao.equals("AT")) {
			sqlStr.append(" ) AS t1 WHERE situacao = '").append(situacao).append("'");
		}
		return montarDadosGrafico(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()));
	}

	@Override
	public void realizarValidacaoRegrasCriacaoRequerimento(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) throws Exception {
		if (!requerimentoVO.getMatricula().getMatricula().trim().isEmpty()) {
			if (requerimentoVO.getTipoRequerimento().getTipo().equals("TR") && requerimentoVO.getMatricula().getSituacao().equals("TR")) {
				requerimentoVO.setMatricula(new MatriculaVO());
				throw new Exception("O Aluno selecionado já está com a matrícula trancada.");

			}
			if (requerimentoVO.getTipoRequerimento().getTipo().equals("RM") && !requerimentoVO.getMatricula().getSituacao().equals("TR") && !requerimentoVO.getMatricula().getSituacao().equals("AC")) {
				requerimentoVO.setMatricula(new MatriculaVO());
				throw new Exception("Não é possível solicitar a reativação da matrícula do Aluno selecionado, pois ela não se encontra trancada.");
			}
			
			if(requerimentoVO.getTipoRequerimento().getValidarMatriculaIntegralizada()) {
				realizarValidacaoMatriculaIntegralizada(requerimentoVO, usuarioVO);
			}
		}
		realizarValidacaoRequisitantePermiteAbrirRequerimento(requerimentoVO);
		realizarValidacaoSituacaoAcademicaAluno(requerimentoVO);
		realizarValidacaoPendenciaBiblioteca(requerimentoVO);
	//	realizarValidacaoPendenciaFinanceira(requerimentoVO, usuarioVO);
		realizarValidacaoPendenciaDocumentacao(requerimentoVO, usuarioVO);
		realizarValidacaoPendenciaEstagio(requerimentoVO);
		realizarValidacaoPendenciaEnade(requerimentoVO);
		realizarValidacaoPendenciaAtividadeComplementar(requerimentoVO);
		realizarValidacaoEntregaTccAluno(requerimentoVO);		
		realizarValidacaoTipoAluno(requerimentoVO);
		if(requerimentoVO.getTipoRequerimento().getValidarAnoSemestreIngresso() && Uteis.isAtributoPreenchido(requerimentoVO.getMatricula().getMatricula())) {
			if(!requerimentoVO.getMatricula().getAnoIngresso().equals(requerimentoVO.getTipoRequerimento().getAnoIngresso()) || !requerimentoVO.getMatricula().getSemestreIngresso().equals(requerimentoVO.getTipoRequerimento().getSemestreIngresso())) {
				throw new ConsistirException("Este requerimento só pode ser aberto para ingressantes de " + requerimentoVO.getTipoRequerimento().getAnoIngresso() + "/" + requerimentoVO.getTipoRequerimento().getSemestreIngresso() +"." );
			}
		}
		requerimentoVO.setSituacaoFinanceira("IS");
		requerimentoVO.setValor(0.0);
//		if(!requerimentoVO.getTipoRequerimento().getIsEmissaoCertificado()) {
//			realizarValidacaoCobrancaRequerimento(requerimentoVO);
//		}
		
//		realizarValidacaoPendenciaFinanceira(requerimentoVO, usuarioVO);
		if(requerimentoVO.getTipoRequerimento().getApenasParaAlunosComTodasAulasRegistradas()) {
			realizarValidacaoAlunosComTodasAulasRegistradas(requerimentoVO, usuarioVO);
		}
		if(requerimentoVO.getTipoRequerimento().getVerificarAlunoPossuiPeloMenosUmaDisciplinaAprovada()) {
			realizarValidacaoVerificarAlunoPossuiPeloMenosUmaDisciplinaAprovada(requerimentoVO, usuarioVO);
		}
		if(requerimentoVO.getTipoRequerimento().getIsTipoTransferenciaInterna() && requerimentoVO.getTipoRequerimento().getValidarVagasPorNumeroComputadoresUnidadeEnsino()
				&& Uteis.isAtributoPreenchido(requerimentoVO.getUnidadeEnsinoTransferenciaInternaVO().getCodigo())
				&& Uteis.isAtributoPreenchido(requerimentoVO.getCursoTransferenciaInternaVO().getCodigo())) {
			List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorCursoAtivo(requerimentoVO.getUnidadeEnsinoTransferenciaInternaVO().getCodigo(),
					requerimentoVO.getCursoTransferenciaInternaVO().getCodigo(),
					requerimentoVO.getTurnoTransferenciaInternaVO().getCodigo(),
					requerimentoVO.getTipoRequerimento().getValidarVagasPorNumeroComputadoresUnidadeEnsino(),
					requerimentoVO.getTipoRequerimento().getValidarVagasPorNumeroComputadoresConsiderandoCurso(),
					requerimentoVO.getTipoRequerimento().getRegistrarTransferenciaProximoSemestre() ? requerimentoVO.getTipoRequerimento().getAno() : requerimentoVO.getMatriculaPeriodoVO().getAno(),
							requerimentoVO.getTipoRequerimento().getRegistrarTransferenciaProximoSemestre() ? requerimentoVO.getTipoRequerimento().getSemestre() : requerimentoVO.getMatriculaPeriodoVO().getSemestre(),
									requerimentoVO.getMatricula().getDiaSemanaAula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO );
			if(unidadeEnsinoVOs.isEmpty()) {
				if(!Uteis.isAtributoPreenchido(requerimentoVO.getUnidadeEnsinoTransferenciaInternaVO().getNome())) {
					requerimentoVO.setUnidadeEnsinoTransferenciaInternaVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(requerimentoVO.getUnidadeEnsinoTransferenciaInternaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_AUDITORIA, usuarioVO));
				}
				throw new Exception("Não foi encontrado vaga disponível no polo "+requerimentoVO.getUnidadeEnsinoTransferenciaInternaVO().getNome().toUpperCase()+" no curso "+requerimentoVO.getCursoTransferenciaInternaVO().getNome().toUpperCase()+" para realizar a transferência.");
			}
			
		}

	}

	private void realizarValidacaoMatriculaIntegralizada(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) throws Exception {
		
		if(requerimentoVO.getTipoRequerimento().getValidarMatriculaIntegralizada()) {
			Boolean integralizou = getFacadeFactory().getMatriculaFacade().isMatriculaIntegralizada(requerimentoVO.getMatricula() ,requerimentoVO.getMatricula().getGradeCurricularAtual().getCodigo(), usuarioVO, null);
			if(requerimentoVO.getTipoRequerimento().getPercentualIntegralizacaoCurricularInicial() == 100 && !integralizou) {
			if(Uteis.isAtributoPreenchido(requerimentoVO.getMatricula().getListaGradeDisciplinaObrigatorioPendente())) {
				StringBuilder disciplinasPendentes = new StringBuilder();
				int cont = 0;
				for(GradeDisciplinaVO gradeDisciplina : requerimentoVO.getMatricula().getListaGradeDisciplinaObrigatorioPendente()) {
					if(cont != 0) {
						disciplinasPendentes.append(", ");
					}
					disciplinasPendentes.append(Uteis.isAtributoPreenchido(gradeDisciplina.getDisciplina().getNome()) ? gradeDisciplina.getDisciplina().getNome() : gradeDisciplina.getDisciplina().getCodigo());
					cont ++;
				}
				throw new Exception("A(s) disciplina(s) obrigatórias "+disciplinasPendentes+" devem ser concluída(s)");
			}
			if(Uteis.isAtributoPreenchido(requerimentoVO.getMatricula().getHorasDisciplinaOptativaExigida()) && requerimentoVO.getMatricula().getHorasDisciplinaOptativaCumprida() < requerimentoVO.getMatricula().getHorasDisciplinaOptativaExigida()) {
				throw new Exception("São exigidas "+requerimentoVO.getMatricula().getHorasDisciplinaOptativaExigida() +" horas de disciplinas e só foram cumpridas "+requerimentoVO.getMatricula().getHorasDisciplinaOptativaCumprida()+" horas");
			}
			if(Uteis.isAtributoPreenchido(requerimentoVO.getMatricula().getHorasEstagioExigida()) && requerimentoVO.getMatricula().getHorasEstagioCumprida() < requerimentoVO.getMatricula().getHorasEstagioExigida()) {
				throw new Exception("São exigidas "+ requerimentoVO.getMatricula().getHorasEstagioExigida()+" horas de estágio e só foram cumpridas "+requerimentoVO.getMatricula().getHorasEstagioCumprida()+" horas");
			}
			if(Uteis.isAtributoPreenchido(requerimentoVO.getMatricula().getHorasAtividadeComplementarExigida()) && requerimentoVO.getMatricula().getHorasAtividadeComplementarCumprida() < requerimentoVO.getMatricula().getHorasAtividadeComplementarExigida()) {
				throw new Exception("São exigidas "+requerimentoVO.getMatricula().getHorasAtividadeComplementarExigida()+" horas de Atividade Complementares e só foram cumpridas "+requerimentoVO.getMatricula().getHorasAtividadeComplementarCumprida()+" horas");
			}
			}else if(!integralizou) {
				if(requerimentoVO.getMatricula().getGradeCurricularAtual().getCargaHoraria().equals(0)) {
					requerimentoVO.getMatricula().getGradeCurricularAtual().setCargaHoraria(getFacadeFactory().getGradeCurricularFacade().consultarCargaHorariaExigidaGrade(requerimentoVO.getMatricula().getGradeCurricularAtual().getCodigo(), usuarioVO));
				}
				Integer percentualCumprido =  requerimentoVO.getMatricula().getPercentualIntegralizacaoCurricular();
				if(percentualCumprido < requerimentoVO.getTipoRequerimento().getPercentualIntegralizacaoCurricularInicial()) {
					throw new Exception("É exigido o mínimo "+requerimentoVO.getTipoRequerimento().getPercentualIntegralizacaoCurricularInicial()+"% de integralização curricular e foi atingido apenas "+percentualCumprido+"%.");
				}
				if(percentualCumprido > requerimentoVO.getTipoRequerimento().getPercentualIntegralizacaoCurricularFinal()) {
					throw new Exception("É exigido o máximo "+requerimentoVO.getTipoRequerimento().getPercentualIntegralizacaoCurricularFinal()+"% de integralização curricular e já foi atingido "+percentualCumprido+"%, ultrapassando o limite máximo.");
				}
			}
		}
		
	}

//	@Override
//	public void realizarValidacaoCobrancaRequerimento(RequerimentoVO requerimentoVO) throws Exception {
//		Double valorDesconto = !Uteis.isAtributoPreenchido(requerimentoVO.getPessoa()) ? 0.0 : requerimentoVO.getTipoDesconto().equals("VA") ? requerimentoVO.getValorDesconto() : requerimentoVO.getPercDesconto();
//		requerimentoVO.setValor(0.0);
//		requerimentoVO.setValorDesconto(0.0);
//		requerimentoVO.setPercDesconto(0.0);
//		requerimentoVO.setTaxaIsentaPorQtdeVia(false);
//		StringBuilder sql = new StringBuilder("");
//		sql.append(" select count(requerimento.codigo) as qtde from requerimento ");
//		if(requerimentoVO.getTipoPessoaAluno()){
//			sql.append(" inner join matricula on matricula.matricula = requerimento.matricula ");
//		}
//		if (requerimentoVO.getTipoRequerimento().getTipoControleCobrancaViaRequerimento().equals(TipoControleCobrancaViaRequerimentoEnum.PERIODO_MATRICULA) && requerimentoVO.getTipoRequerimento().getCobrarApartirVia() > 0 && requerimentoVO.getTipoPessoaAluno()) {
//			sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
//			sql.append(" and matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo not in ('PC') order by ano||'/'||semestre desc limit 1) ");
//		}
//		sql.append(" where tipoRequerimento = ").append(requerimentoVO.getTipoRequerimento().getCodigo());
//		if(!requerimentoVO.getTipoRequerimento().getConsiderarSegundaViaIndependenteSituacaoPrimeiraVia()){
//			sql.append(" and requerimento.situacao =  'FD' ");
//		}
//		if(requerimentoVO.getTipoPessoaAluno()){
//			sql.append(" and matricula.matricula = '").append(requerimentoVO.getMatricula().getMatricula()).append("'  ");
//		}else{
//			sql.append(" and requerimento.pessoa = ").append(requerimentoVO.getPessoa().getCodigo()).append("  ");	
//		}
//		if (requerimentoVO.getTipoPessoaAluno() && requerimentoVO.getTipoRequerimento().getTipoControleCobrancaViaRequerimento().equals(TipoControleCobrancaViaRequerimentoEnum.PERIODO_MATRICULA) && requerimentoVO.getTipoRequerimento().getCobrarApartirVia() > 0) {
//			sql.append(" and requerimento.data >= matriculaperiodo.data  ");
//		}
//		if (requerimentoVO.getTipoPessoaAluno() && requerimentoVO.getTipoRequerimento().getTipoControleCobrancaViaRequerimento().equals(TipoControleCobrancaViaRequerimentoEnum.MENSAL) && requerimentoVO.getTipoRequerimento().getCobrarApartirVia() > 0) {
//			sql.append(" and requerimento.data >= date_trunc('month', current_date) and requerimento.data <= date_trunc('month',current_date) + INTERVAL'1 month' - INTERVAL'1 day'");
//		}
//		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
//		Integer qtdeVia = 1;
//		if (rs.next()) {
//			qtdeVia = rs.getInt("qtde");
//			qtdeVia++;
//		}
//		requerimentoVO.setNumeroVia(qtdeVia);
//		if (requerimentoVO.getTipoRequerimento().getTaxa().getCodigo() > 0) {
//			requerimentoVO.getTaxa().setCodigo(requerimentoVO.getTipoRequerimento().getTaxa().getCodigo());
//			requerimentoVO.setValor(getFacadeFactory().getTaxaFacade().consultarValorTaxaAtual(requerimentoVO.getTipoRequerimento().getTaxa().getCodigo()));
//			
//			if (requerimentoVO.getTipoRequerimento().getCobrarTaxaSomenteCertificadoImpresso() && (requerimentoVO.getTipoRequerimento().getIsEmissaoCertificado() || requerimentoVO.getTipoRequerimento().getIsDeclaracao()) && requerimentoVO.getIsFormatoCertificadoSelecionadoDigital() && !requerimentoVO.getExigePagamento()) {
//				requerimentoVO.setValor(0.0);
//				return;
//			}
//			
//			if (((requerimentoVO.getTipoRequerimento().getCobrarApartirVia() > 0 && qtdeVia < requerimentoVO.getTipoRequerimento().getCobrarApartirVia())
//					//Aqui valida a regra de isenção automática de requerimento de reposição de disciplina onde o aluno tenmha sido matriculado apos a data da aula
//					|| (requerimentoVO.getTipoRequerimento().getIsTipoReposicao() && requerimentoVO.getTipoRequerimento().getRealizarIsencaoTaxaReposicaoMatriculaAposDataAula()
//							&& Uteis.isAtributoPreenchido(requerimentoVO.getDisciplina()) &&  getFacadeFactory().getHistoricoFacade().realizarVerificacaoDataHistoricoPosteriorDataAulaPorMatriculaDisciplina(requerimentoVO.getMatricula().getMatricula(), requerimentoVO.getMatriculaPeriodoVO().getCodigo(), requerimentoVO.getMatricula().getGradeCurricularAtual().getCodigo(), requerimentoVO.getDisciplina().getCodigo(), requerimentoVO.getResponsavel()))
//					|| (requerimentoVO.getTipoRequerimento().getTipo().equals("TC") && !verificarCobrancaComBaseUltimaAula(requerimentoVO)))) {
//				requerimentoVO.setValorDesconto(requerimentoVO.getValor());
//				requerimentoVO.setTipoDesconto("VA");
//				requerimentoVO.setTaxaIsentaPorQtdeVia(true);
//			} else if (requerimentoVO.getTipoDesconto().equals("VA") ) {
//				requerimentoVO.setValorDesconto(valorDesconto);
//				if (requerimentoVO.getValorDesconto() > requerimentoVO.getValor()) {
//					requerimentoVO.setValorDesconto(requerimentoVO.getValor());
//				}
//				requerimentoVO.setPercDesconto(0.0);
//			} else {
//				requerimentoVO.setValorDesconto(0.0);
//				requerimentoVO.setPercDesconto(valorDesconto);
//			}
//			
//			if(requerimentoVO.getTipoRequerimento().getRequerAutorizacaoPagamento() && requerimentoVO.getValorTotalFinal() > 0.0) {
//				requerimentoVO.setSituacaoFinanceira("AP");
//				requerimentoVO.setSituacao("PE");
//			}else if(requerimentoVO.getValorTotalFinal() > 0.0) {
//				requerimentoVO.setSituacaoFinanceira("PE");
//				requerimentoVO.setSituacao(SituacaoRequerimento.AGUARDANDO_PAGAMENTO.getValor());
//			}else if(requerimentoVO.getValorTotalFinal() == 0.0) {
//				requerimentoVO.setSituacaoFinanceira("IS");
//				requerimentoVO.setSituacao("PE");
//			}
//		}
//	}
	
	@Override
	public boolean verificarCobrancaComBaseUltimaAula(RequerimentoVO requerimentoVO) throws Exception {
//			if(requerimentoVO.getTipoRequerimento().getQtdDiasCobrarTaxa() != 0 && requerimentoVO.getMatriculaPeriodoVO().getCodigo() != 0) {
////				Date data = getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaAulaAlunoPorMatriculaPeriodo(requerimentoVO.getMatriculaPeriodoVO().getCodigo());
////				if(data == null){
////					requerimentoVO.setRealizarCobrancaBaseNaUltimaAula(false);
////					return requerimentoVO.getRealizarCobrancaBaseNaUltimaAula();
////				}
//				LocalDate ultimoDiaDeAula = Instant.ofEpochMilli(data.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
//				Long diferencaEmDias = ChronoUnit.DAYS.between(ultimoDiaDeAula, LocalDate.now());
//				if(diferencaEmDias.intValue() >  requerimentoVO.getTipoRequerimento().getQtdDiasCobrarTaxa()) {
//					requerimentoVO.setRealizarCobrancaBaseNaUltimaAula(true);
//					return requerimentoVO.getRealizarCobrancaBaseNaUltimaAula();
//				}else {
//					requerimentoVO.setRealizarCobrancaBaseNaUltimaAula(false);
//					return requerimentoVO.getRealizarCobrancaBaseNaUltimaAula();
//				}
//			}else {
//				requerimentoVO.setRealizarCobrancaBaseNaUltimaAula(false);
//				return requerimentoVO.getRealizarCobrancaBaseNaUltimaAula();
//		}
		
		return false;
	}
	
	@Override
	public Long qtdDiasExedidosDoPrazoComBaseUltimaAula(Integer codigoMatricula) throws Exception {
//			Date data = getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaAulaAlunoPorMatriculaPeriodo(codigoMatricula);
//			LocalDate ultimoDiaDeAula = Instant.ofEpochMilli(data.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
//			Long diferencaEmDias = ChronoUnit.DAYS.between(ultimoDiaDeAula, LocalDate.now());
		return null;
	}

	public void realizarValidacaoPendenciaBiblioteca(RequerimentoVO requerimentoVO) throws Exception {
		String matricula = "";
		if (requerimentoVO.getTipoRequerimento().getVerificarPendenciaApenasMatriculaRequerimento()) {
			matricula = requerimentoVO.getMatricula().getMatricula();
		}
//		if (requerimentoVO.getTipoRequerimento().getVerificarPendenciaBiblioteca() && getFacadeFactory().getItemEmprestimoFacade().consultarPessoaPossuePendenciaBiblioteca(requerimentoVO.getPessoa().getCodigo(), matricula, false)) {
//			throw new ConsistirException(UteisJSF.internacionalizar("msg_Requerimento_possuePendenciaBiblioteca"));
//		}
//		if (requerimentoVO.getTipoRequerimento().getVerificarPendenciaBibliotecaAtraso() && getFacadeFactory().getItemEmprestimoFacade().consultarPessoaPossuePendenciaBiblioteca(requerimentoVO.getPessoa().getCodigo(), matricula, true)) {
//			throw new ConsistirException(UteisJSF.internacionalizar("msg_Requerimento_possuePendenciaBibliotecaAtraso"));
//		}
	}

//	public void realizarValidacaoPendenciaFinanceira(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) throws Exception {
//		String matricula = "";      
//		
//		if (requerimentoVO.getTipoRequerimento().getVerificarPendenciaApenasMatriculaRequerimento()) {
//			matricula = requerimentoVO.getMatricula().getMatricula();
//		}
//		if (requerimentoVO.getTipoRequerimento().getVerificarPendenciaFinanceira() && getFacadeFactory().getContaReceberFacade().consultarExistenciaPendenciaFinanceiraMatricula(requerimentoVO.getPessoa().getCodigo(), matricula, usuarioVO)) {
//			throw new ConsistirException(UteisJSF.internacionalizar("msg_Requerimento_possuePendenciaFinanceira"));
//		}
//		if(requerimentoVO.getSituacaoFinanceira().equals("IS") || requerimentoVO.getValorTotalFinal().equals(0.0)) {
//			if(requerimentoVO.getTipoRequerimento().getValidarDebitoFinanceiroRequerimentoIsento()) {
//				if(getFacadeFactory().getContaReceberFacade().consultarExistenciaPendenciaFinanceiraMatriculaEmAtraso(requerimentoVO.getPessoa().getCodigo(), matricula, usuarioVO)) {
//					  throw new ConsistirException(UteisJSF.internacionalizar("msg_Requerimento_possuePendenciaFinanceiraAtraso"));
//				}
//			}
//		}
//		if (requerimentoVO.getTipoRequerimento().getVerificarPendenciaFinanceiraAtraso() && getFacadeFactory().getContaReceberFacade().consultarExistenciaPendenciaFinanceiraMatriculaEmAtraso(requerimentoVO.getPessoa().getCodigo(), matricula, usuarioVO)) {
//		  throw new ConsistirException(UteisJSF.internacionalizar("msg_Requerimento_possuePendenciaFinanceiraAtraso"));
//	   }
//	}

	public void realizarValidacaoPendenciaDocumentacao(RequerimentoVO requerimentoVO, UsuarioVO usuario) throws Exception {
		String matricula = "";
		if (requerimentoVO.getTipoRequerimento().getVerificarPendenciaApenasMatriculaRequerimento()) {
			matricula = requerimentoVO.getMatricula().getMatricula();
		}
		if (requerimentoVO.getTipoRequerimento().getVerificarPendenciaDocumentacao()) {
			String documentoPendentes = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarExistenciaPendenciaDocumentacaoPorMatricula(requerimentoVO.getPessoa().getCodigo(), matricula, requerimentoVO.getTipoRequerimento().getTipoDocumentoVOs(), false);
			if(Uteis.isAtributoPreenchido(documentoPendentes)) {
				StringBuilder mensagem = new StringBuilder("Verificamos que o(s) documento(s) ");
				mensagem.append(" (").append(documentoPendentes).append(") ");
				mensagem.append("encontra(m)-se pendente(s).");
				throw new ConsistirException(mensagem.toString());
			}
		}
			//throw new ConsistirException(UteisJSF.internacionalizar("msg_Requerimento_possuePendenciaDocumentacao"));
		
	}

	public void realizarValidacaoPendenciaEnade(RequerimentoVO requerimentoVO) throws Exception {
		String matricula = "";
//		if (requerimentoVO.getTipoRequerimento().getVerificarPendenciaApenasMatriculaRequerimento()) {
			matricula = requerimentoVO.getMatricula().getMatricula();
//		}
		if (Uteis.isAtributoPreenchido(matricula) && requerimentoVO.getTipoRequerimento().getVerificarPendenciaEnade() && getFacadeFactory().getMatriculaFacade().consultarAlunoPendenciaEnadePorMatricula(requerimentoVO.getPessoa().getCodigo(), matricula)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_Requerimento_possuePendenciaEnade"));
		}
	}

	public void realizarValidacaoPendenciaEstagio(RequerimentoVO requerimentoVO) throws Exception {
		String matricula = "";
		if (requerimentoVO.getTipoRequerimento().getVerificarPendenciaApenasMatriculaRequerimento()) {
			matricula = requerimentoVO.getMatricula().getMatricula();
		}
//		if (requerimentoVO.getTipoRequerimento().getVerificarPendenciaEstagio() && getFacadeFactory().getEstagioFacade().consultarPendenciaEstagioPorMatricula(requerimentoVO.getPessoa().getCodigo(), matricula)) {
//			throw new ConsistirException(UteisJSF.internacionalizar("msg_Requerimento_possuePendenciaEstagio"));
//		}
	}

	public void realizarValidacaoPendenciaAtividadeComplementar(RequerimentoVO requerimentoVO) throws Exception {
		String matricula = "";
		if (requerimentoVO.getTipoRequerimento().getVerificarPendenciaApenasMatriculaRequerimento()) {
			matricula = requerimentoVO.getMatricula().getMatricula();
		}
		if (requerimentoVO.getTipoRequerimento().getVerificarPendenciaAtividadeComplementar() && getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarPendenciaAtividadeComplementarPorMatricula(requerimentoVO.getPessoa().getCodigo(), matricula)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_Requerimento_possuePendenciaAtividadeComplementar"));
		}
	}
	
	public void realizarValidacaoEntregaTccAluno(RequerimentoVO requerimentoVO) throws Exception {
		if (Uteis.isAtributoPreenchido(requerimentoVO.getMatricula().getMatricula())) {
			if (requerimentoVO.getTipoRequerimento().getValidarEntregaTccAluno() && !getFacadeFactory().getMatriculaFacade().consultarEntregaTccAluno(requerimentoVO.getPessoa().getCodigo(), requerimentoVO.getMatricula().getMatricula())) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_Requerimento_possuePendenciaTcc"));
			}
		}
	}

	public void realizarValidacaoSituacaoAcademicaAluno(RequerimentoVO requerimentoVO) throws Exception {
		if (!requerimentoVO.getMatricula().getMatricula().equals("")) {
			Map<String, String> possiveisSituacoes = new HashMap<String, String>(0);
			if (requerimentoVO.getTipoRequerimento().getSituacaoMatriculaAtiva()) {
				possiveisSituacoes.put("AT", "AT");
				possiveisSituacoes.put("FI", "FI");
			}
			if (requerimentoVO.getTipoRequerimento().getSituacaoMatriculaPreMatriculada()) {
				possiveisSituacoes.put("PR", "PR");
			}
			if (requerimentoVO.getTipoRequerimento().getSituacaoMatriculaCancelada()) {
				possiveisSituacoes.put("CA", "CA");
			}
			if (requerimentoVO.getTipoRequerimento().getSituacaoMatriculaTrancada()) {
				possiveisSituacoes.put("TR", "TR");
			}
			if (requerimentoVO.getTipoRequerimento().getSituacaoMatriculaFormada()) {
				possiveisSituacoes.put("FO", "FO");
			}
			if (requerimentoVO.getTipoRequerimento().getSituacaoMatriculaAbandonada()) {
				possiveisSituacoes.put("AC", "AC");
			}
			if (requerimentoVO.getTipoRequerimento().getSituacaoMatriculaTransferida()) {
				possiveisSituacoes.put("TS", "TS");
				possiveisSituacoes.put("TI", "TI");
			}
			if (requerimentoVO.getTipoRequerimento().getSituacaoMatriculaJubilado()) {
				possiveisSituacoes.put("JU", "JU");
			}
			if (!possiveisSituacoes.isEmpty() && !possiveisSituacoes.containsKey(requerimentoVO.getMatricula().getSituacao())) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_Requerimento_situacaoMatriculaNaoPermitida").replace("{0}", requerimentoVO.getMatricula().getSituacao_Apresentar()));
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataUltimaAlteracao(final Integer requerimento) throws Exception {
		final String sql = "UPDATE Requerimento set dataUltimaAlteracao = ? WHERE codigo = ?";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setDate(1, Uteis.getDataJDBC(new Date()));
				sqlAlterar.setInt(2, requerimento);
				return sqlAlterar;
			}
		});
	}

//	@Override
//	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//	public synchronized void persistirSicronizadoRequerimento(RequerimentoVO requerimento, RequerimentoHistoricoVO requerimentoHistoricoVO, Boolean visaoAluno, Boolean exigePagamento, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuarioLogado) throws Exception {
//		persistirRequerimento(requerimento, requerimentoHistoricoVO, visaoAluno, exigePagamento, configuracaoGeralSistema, unidadeEnsinoLogado, usuarioLogado);	
//	}
//	@Override
//	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//	public void persistirRequerimento(RequerimentoVO requerimento, RequerimentoHistoricoVO requerimentoHistoricoVO, Boolean visaoAluno, Boolean exigePagamento, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuarioLogado) throws Exception {
//		try {
//			
//			if ((requerimento.getSituacaoFinanceira().equals("PE")) || (requerimento.getSituacaoFinanceira().equals("IS") && (requerimento.getSituacao().equals("PE") || requerimento.getSituacao().equals("EX") || requerimento.getSituacao().equals("FD"))) || (requerimento.getSituacaoFinanceira().equals("AP") && (requerimento.getSituacao().equals("PE") || requerimento.getSituacao().equals("EX")))) {
//				if (requerimento.getValorTotalFinal().equals(0.0)) {
//					requerimento.setSituacaoFinanceira("IS");
//					
//				if(((!requerimento.getTipoRequerimento().getIsEmissaoCertificado() && !requerimento.getTipoRequerimento().getIsDeclaracao() 
//				&&  requerimento.getTipoRequerimento().getDeferirAutomaticamente()) 
//				|| ((requerimento.getTipoRequerimento().getIsEmissaoCertificado() || requerimento.getTipoRequerimento().getIsDeclaracao()) 
//				&& (requerimento.getIsFormatoCertificadoSelecionadoDigital() &&  requerimento.getTipoRequerimento().getDeferirAutomaticamente()) 
//				|| (requerimento.getIsFormatoCertificadoSelecionadoImpresso() &&  requerimento.getTipoRequerimento().getDeferirAutomaticamenteDocumentoImpresso())))
//				&& requerimento.getMotivoNaoAceiteCertificado().equals("")) {				
//						requerimento.setSituacao(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor());
//						requerimento.setDataFinalizacao(new Date());
//					} else {
//						if(!requerimento.getSituacao().equals("EX")) {
//							requerimento.setSituacao(SituacaoRequerimento.PENDENTE.getValor());
//						}
//					}
//					exigePagamento = Boolean.FALSE;
//				} else {
//					requerimento.setResponsavelEmissaoBoleto(usuarioLogado);
//					if(requerimento.getTipoRequerimento().getRequerAutorizacaoPagamento()) {
//						if(!Uteis.isAtributoPreenchido(requerimento.getContaReceber())) {
//							requerimento.setSituacaoFinanceira("AP");
//						}
//					}else if(!Uteis.isAtributoPreenchido(requerimento.getDataVencimentoContaReceber())) {
//						throw new Exception("O campo VENCIMENTO BOLETO deve ser informado.");
//					}
//					exigePagamento = Boolean.TRUE;
//				}
//				if (requerimento.getUnidadeEnsino() == null) {
//					requerimento.setUnidadeEnsino(unidadeEnsinoLogado);
//				}
//				montarResponsavelVisaoGerado(requerimento, usuarioLogado);
//				if (!visaoAluno) {
//					if (requerimento.getPessoa().getCodigo() != 0) {
//						getFacadeFactory().getPessoaFacade().alterarPessoaRequisitante(requerimento.getPessoa().getCodigo());
//						requerimento.getPessoa().setRequisitante(Boolean.TRUE);
//					}
//					
//				}
//				if (requerimento.isNovoObj().booleanValue()) {
//					if (visaoAluno) {
//						requerimento.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorMatricula(requerimento.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado));
//					} else {
//						if (!requerimento.getUnidadeEnsino().getConfiguracoes().getCodigo().equals(0)) {
//							ConfiguracoesVO config = getFacadeFactory().getConfiguracoesFacade().consultarConfiguracaoUnidadeEnsino(requerimento.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado);
//							configuracaoGeralSistema = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsadaUnidadEnsino(requerimento.getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
//							configuracaoFinanceira = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorCodigoConfiguracoes(config.getCodigo(), false, usuarioLogado, Uteis.NIVELMONTARDADOS_TODOS);
//						}
//					}
//					if (requerimento.getTipoRequerimento().getIsTipoSegundaChamada()) {
//						List<CidTipoRequerimentoVO> ListaCid = listaCidSelecionados(requerimento.getTipoRequerimento(), usuarioLogado);
//						requerimento.getRequerimentoCidTipoRequerimentoVO().setCidTipoRequerimentoVOs(ListaCid);
//					}
//					incluir(requerimento, usuarioLogado, configuracaoGeralSistema);
//					
//					if(requerimento.getRequerimentoAntigo().getCodigo() != 0) {
//						incluirVinculoRequerimentoNovo(requerimento, usuarioLogado);
//					}
//					
//				} else {
//					alterar(requerimento, usuarioLogado, configuracaoGeralSistema);
//					if(Uteis.isAtributoPreenchido(requerimentoHistoricoVO) && !usuarioLogado.getIsApresentarVisaoAluno() && !usuarioLogado.getIsApresentarVisaoPais()
//							&& !requerimento.getPessoa().getCodigo().equals(usuarioLogado.getPessoa().getCodigo())) {
//						getFacadeFactory().getRequerimentoHistoricoFacade().alterarSituacaoDepartamento(requerimentoHistoricoVO, requerimento, usuarioLogado);
//					}
//				}
//				if(((!requerimento.getTipoRequerimento().getIsEmissaoCertificado() && !requerimento.getTipoRequerimento().getIsDeclaracao() 
//				&&  requerimento.getTipoRequerimento().getDeferirAutomaticamente()) 
//				|| ((requerimento.getTipoRequerimento().getIsEmissaoCertificado() || requerimento.getTipoRequerimento().getIsDeclaracao()) 
//				&& (requerimento.getIsFormatoCertificadoSelecionadoDigital() &&  requerimento.getTipoRequerimento().getDeferirAutomaticamente()) 
//				|| (requerimento.getIsFormatoCertificadoSelecionadoImpresso() &&  requerimento.getTipoRequerimento().getDeferirAutomaticamenteDocumentoImpresso())
//				 && requerimento.getMotivoNaoAceiteCertificado().equals("")))) {				
//					Thread enviarComunicadoTramiteRequerimento = new Thread(new EnviarComunicadoTramiteRequerimento((RequerimentoVO)requerimento.clone(), usuarioLogado));
//					enviarComunicadoTramiteRequerimento.start();
//				}
//			} else {
//				if(requerimento.getSituacaoFinanceira().equals("AP") && !requerimento.getSituacao().equals("PE")) {
//					throw new Exception("Este requerimento já está em trâmite, portanto não pode ser alterado.");
//				}else {
//					throw new Exception("Este requerimento já foi quitado financeiramente, portanto não pode ser alterado.");
//				}
//			}
//			if(requerimento.getTipoRequerimento().getTipo().equals("TI")) {
//				getFacadeFactory().getMatriculaFacade().excluirBolqueioMatricula(requerimento.getMatricula(), usuarioLogado);
//			}
//		} catch (Exception e) {
////			requerimento.setSituacao(SituacaoRequerimento.AGUARDANDO_PAGAMENTO.getValor());
//			throw e;
//		}
//
//	}

	public void montarResponsavelVisaoGerado(RequerimentoVO requerimento, UsuarioVO usuarioLogado) throws Exception {
		try {
			if(requerimento.isNovoObj()) {
			requerimento.setResponsavel(usuarioLogado);
			if (usuarioLogado.getVisaoLogar().equals("aluno")) {
				requerimento.setVisaoGerado(TipoVisaoAcesso.VISAO_ALUNO.getValor());
			} else if (usuarioLogado.getVisaoLogar().equals("pais")) {
				requerimento.setVisaoGerado(TipoVisaoAcesso.VISAO_RESPONSAL_LEGAL.getValor());
			} else if (usuarioLogado.getPerfilAdministrador() && usuarioLogado.getVisaoLogar().equals("")) {
				requerimento.setVisaoGerado(TipoVisaoAcesso.VISAO_DIRETOR_MULTI_CAMPUS.getValor());
			} else if (usuarioLogado.getIsApresentarVisaoCoordenador()) {
				requerimento.setVisaoGerado(TipoVisaoAcesso.VISAO_COORDENADOR.getValor());
			} else if (usuarioLogado.getIsApresentarVisaoProfessor()) {
				requerimento.setVisaoGerado(TipoVisaoAcesso.VISAO_PROFESSOR.getValor());
			} else {
				requerimento.setVisaoGerado(TipoVisaoAcesso.VISAO_FUNCIONARIO.getValor());
			}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public Boolean consultarDeferirAutomaticamentePorCodigoRequerimento(Integer codigoRequerimento) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" select deferirautomaticamente from requerimento ");
			sql.append(" inner join tiporequerimento on tiporequerimento.codigo = requerimento.tiporequerimento ");
			sql.append(" where requerimento.codigo =");
			sql.append(codigoRequerimento);
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (tabelaResultado.next()) {
				return tabelaResultado.getBoolean("deferirautomaticamente");
			}
		} catch (Exception e) {
			throw e;
		}
		return Boolean.FALSE;
	}
	
	public Boolean consultarDeferirAutomaticamenteDocumentoImpressoPorCodigoRequerimento(Integer codigoRequerimento) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" select deferirautomaticamentedocumentoimpresso from requerimento ");
			sql.append(" inner join tiporequerimento on tiporequerimento.codigo = requerimento.tiporequerimento ");
			sql.append(" where requerimento.codigo =");
			sql.append(codigoRequerimento);
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (tabelaResultado.next()) {
				return tabelaResultado.getBoolean("deferirautomaticamentedocumentoimpresso");
			}
		} catch (Exception e) {
			throw e;
		}
		return Boolean.FALSE;
	}

	public Boolean consultarTipoRequerimentoReposicaoPorCodigoRequerimento(Integer codigoRequerimento) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" select tiporequerimento.tipo from requerimento ");
			sql.append(" inner join tiporequerimento on tiporequerimento.codigo = requerimento.tiporequerimento ");
			sql.append(" where (tiporequerimento.tipo = 'RE' or tiporequerimento.tipo = 'INCLUSAO_DISCIPLINA') and requerimento.codigo = ");
			sql.append(codigoRequerimento);
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (tabelaResultado.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public Boolean validarApresentarBotaoImprimirVisaoAluno(RequerimentoVO requerimento) throws Exception {
		try {
			TipoRequerimentoVO tipoRequerimento = requerimento.getTipoRequerimento();
			if (requerimento.getSituacao().equals("FD")&& tipoRequerimento.getRequerimentoVisaoAlunoApresImprimirDeclaracao()) {

				// SE A QUANTIDADE DE DIAS PARA A VALIDADE DO REQUERIMENTO É
				// IGUAL A 0 SIGNIFICA QUE ELE É ILIMITADO NUNCA IRÁ VENCER
				if (tipoRequerimento.getQtdeDiasDisponivel() != null && tipoRequerimento.getQtdeDiasDisponivel() == 0) {

					return validarQuantidadeDiasLimiteImpressao(requerimento, tipoRequerimento);
				}

				// CASO A QUANTIDADE DE DIAS PARA A VALIDADE DO REQUERIMENTO É
				// DIFERENTE DE ZERO EFETUAR AS CONTAS PARA VER SE É POSSÍVEL A
				// IMPRESSÃO
				if (requerimento.getDataFinalizacao() != null && tipoRequerimento.getQtdeDiasDisponivel() != null && tipoRequerimento.getQtdeDiasDisponivel() != 0) {

					Date dataValidade = Uteis.obterDataFutura(requerimento.getDataFinalizacao(), tipoRequerimento.getQtdeDiasDisponivel());

					// SE A DATA DE VALIDADE DO REQUERIMENTO AINDA ENCONTRA-SE
					// ATIVA, OU SEJA ANTERIOR OU IGUAL A HOJE NÃO IMPRIMIR
					if (dataValidade.compareTo(new Date()) < 0) {
						return Boolean.FALSE;

					}
					// SE A DATA DE VALIDADE DO REQUERIMENTO AINDA ENCONTRA-SE
					// INATIVA, OU SEJA MAIOR QUE HOJE IMPRIMIR
					if (dataValidade.compareTo(new Date()) >= 0) {
						return validarQuantidadeDiasLimiteImpressao(requerimento, tipoRequerimento);
					}

				}

			}
		} catch (Exception e) {
			throw e;
		}
		return Boolean.FALSE;
	}

	public Boolean validarQuantidadeDiasLimiteImpressao(RequerimentoVO requerimento, TipoRequerimentoVO tipoRequerimento) {
		// SE A QUANTIDADE DE DIAS APOS A PRIMEIRA IMPRESSAO É IGUAL
		// A 0 ELE SEMPRE PODERÁ IMPRIMIR O REQUERIMENTO
		if (tipoRequerimento.getQtdeDiasAposPrimeiraImpressao() != null && tipoRequerimento.getQtdeDiasAposPrimeiraImpressao() == 0) {
			return Boolean.TRUE;
		}
		// REGRA PARA VALIDAR SE OS DIAS DA IMPRESSÃO ESTÃO CORRETOS
		// QUANDO O VALOR FOR DIFERENTE DE 0
		if (tipoRequerimento.getQtdeDiasAposPrimeiraImpressao() != null && tipoRequerimento.getQtdeDiasAposPrimeiraImpressao() != 0) {
			Date dataUltimaImpressao = requerimento.getDataUltimaImpressao();
			Date dataLimiteImpressao = null;
			if (dataUltimaImpressao != null) {
				dataLimiteImpressao = Uteis.obterDataFutura(dataUltimaImpressao, tipoRequerimento.getQtdeDiasAposPrimeiraImpressao());
			}
			// SE A DATA DA ULTIMA IMPRESSÃO FOR IGUAL A NULO NUNCA
			// FOI IMPRESSO ENTÃO PODERÁ IMPRIMIR
			if (dataUltimaImpressao == null) {
				return Boolean.TRUE;
			}
			// SE A DA TA LIMITE IMPRESSAO FOR DIFERENTE DE NULA E
			// ANTERIOR OU IGUAL A HOJE NÃO IMPRIMIR
			if (dataLimiteImpressao != null && (dataLimiteImpressao.compareTo(new Date()) < 0)) {
				return Boolean.FALSE;
			}
			// SE A DA TA LIMITE IMPRESSAO FOR DIFERENTE DE NULA E
			// POSTERIOR A HOJE IMPRIMIR
			if (dataLimiteImpressao != null && (dataLimiteImpressao.compareTo(new Date()) >= 0)) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	@Override
	public void alterarDataUltimaImpressao(Integer codigoRequerimento) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE requerimento set dataUltimaImpressao = current_date where codigo = ?");
			getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { codigoRequerimento });
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPessoaUnificacaoFuncionario(Integer pessoaAntigo, Integer pessoaNova) throws Exception {
		String sqlStr = "UPDATE Requerimento set pessoa=? WHERE ((pessoa = ?))";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { pessoaNova, pessoaAntigo });
	}

	@Override
	public Boolean realizarVerificarProximoTramiteExigeInformarFuncionarioResponsavel(RequerimentoVO requerimentoVO, FuncionarioVO funcionarioProximoTramite, UsuarioVO usuario) throws Exception {
		TipoRequerimentoDepartamentoVO proximoTipoRequerimentoDepartamentoVO = requerimentoVO.getTipoRequerimento().consultarTipoRequerimentoDepartamentoVOs(requerimentoVO.getOrdemExecucaoTramiteDepartamento() + 1);
		if (Uteis.isAtributoPreenchido(proximoTipoRequerimentoDepartamentoVO)) {			
			if ((proximoTipoRequerimentoDepartamentoVO.getTipoDistribuicaoResponsavel().equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_ESPECIFICO_NO_TRAMITE)) || (proximoTipoRequerimentoDepartamentoVO.getTipoDistribuicaoResponsavel().equals(TipoDistribuicaoResponsavelEnum.COORDENADOR_CURSO_ESPECIFICO_NO_TRAMITE))) {
				funcionarioProximoTramite = getFacadeFactory().getFuncionarioFacade().realizarVerificacaoExistenciaFuncionarioJaRealizouTramite(requerimentoVO.getCodigo(), proximoTipoRequerimentoDepartamentoVO.getDepartamento().getCodigo(), proximoTipoRequerimentoDepartamentoVO.getCargo().getCodigo(), proximoTipoRequerimentoDepartamentoVO.getOrdemExecucao(), proximoTipoRequerimentoDepartamentoVO.getTipoDistribuicaoResponsavel(), usuario);
				return true;
			}
		}
		return false;
	}

	@Override
	public List<RequerimentoVO> consultaRapidaRequerimentoVisaoProfessorCoordenador(Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE ( (p2.codigo = ").append(usuario.getPessoa().getCodigo()).append(") ");
		sqlStr.append(" or (pessoaFuncionario.codigo = ").append(usuario.getPessoa().getCodigo()).append(" and requerimento.situacao not in ('FD', 'FI'))) ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and Requerimento.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY requerimento.data");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, configuracaoGeralSistemaVO);
	}

	@Override
	public Integer consultarQtdeRequerimentoAlunoVisaoProfessorCoordenador(UsuarioVO usuario) {
		StringBuffer str = new StringBuffer();
		str.append("SELECT count(requerimento.codigo) as qtde ");
		str.append("FROM Requerimento ");
		str.append("INNER JOIN TipoRequerimento on Requerimento.tipoRequerimento = TipoRequerimento.codigo  ");
		str.append("INNER JOIN UnidadeEnsino on requerimento.unidadeEnsino = unidadeEnsino.codigo ");
		str.append("LEFT JOIN Funcionario on Funcionario.codigo = Requerimento.funcionario  ");
		str.append("LEFT JOIN Pessoa as pessoaFuncionario on Funcionario.pessoa = pessoaFuncionario.codigo  ");
		str.append("LEFT JOIN Matricula on matricula.matricula = requerimento.matricula ");
		str.append("LEFT JOIN Pessoa p1 on p1.codigo = matricula.aluno ");
		str.append("LEFT JOIN Pessoa p2 on p2.codigo = requerimento.pessoa ");
		str.append("LEFT JOIN Curso on curso.codigo = requerimento.curso ");
		str.append(" WHERE pessoaFuncionario.codigo = ").append(usuario.getPessoa().getCodigo()).append(" and requerimento.situacao not in ('FD', 'FI') ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}

	public RequerimentoVO consultaRapidaUltimoRequerimentoPorMatriculaTipoDocumentoDiploma(String matricula) {
		StringBuffer str = new StringBuffer();
		str.append("select requerimento.codigo, requerimento.data from requerimento  ");
		str.append("inner join tiporequerimento on tiporequerimento.codigo = requerimento.tiporequerimento ");
		str.append("where requerimento.matricula = '").append(matricula).append("' and tiporequerimento.tipo = 'ED' order by requerimento.codigo desc limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		RequerimentoVO obj = new RequerimentoVO();
		if (tabelaResultado.next()) {
			obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
			obj.setData(tabelaResultado.getTimestamp("data"));
		}
		return obj;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAlteracaoFuncionarioResponsavelRequerimento(RequerimentoVO requerimentoVO, FuncionarioVO funcionarioVO, UsuarioVO usuarioVO) throws Exception {
		FuncionarioVO funcAnt = requerimentoVO.getFuncionarioVO();
		try {
			requerimentoVO.setFuncionarioVO(new FuncionarioVO());
			requerimentoVO.setFuncionarioVO(funcionarioVO);
			if (Uteis.isAtributoPreenchido(requerimentoVO) && Uteis.isAtributoPreenchido(funcionarioVO)) {
				RequerimentoHistoricoVO reqHistVO = requerimentoVO.consultarUltimoRequerimentoHistoricoDepartamentoAtualVOs();
				if (!Uteis.isAtributoPreenchido(reqHistVO)) {
					reqHistVO = new RequerimentoHistoricoVO();
					reqHistVO.setRequerimento(requerimentoVO.getCodigo());
					reqHistVO.setDepartamento(requerimentoVO.getDepartamentoResponsavel());
					reqHistVO.setDataEntradaDepartamento(new Date());
					reqHistVO.setOrdemExecucaoTramite(1);
				}
				reqHistVO.getResponsavelRequerimentoDepartamento().setCodigo(funcionarioVO.getPessoa().getCodigo());
				reqHistVO.getResponsavelRequerimentoDepartamento().setNome(funcionarioVO.getPessoa().getNome());
				getFacadeFactory().getRequerimentoHistoricoFacade().alterar(reqHistVO, usuarioVO);
				alterarFuncionarioResponsavel(requerimentoVO.getCodigo(), funcionarioVO.getCodigo(), usuarioVO);
			}
		} catch (Exception e) {
			requerimentoVO.setFuncionarioVO(funcAnt);
			throw e;
		}
	}
	
	@Override
	public void realizarValidacaoRequisitantePermiteAbrirRequerimento(RequerimentoVO requerimentoVO) throws Exception{
		if(requerimentoVO.isNovoObj() && !requerimentoVO.getTipoPessoaAluno() && !requerimentoVO.getTipoRequerimento().getRequerimentoMembroComunidade() && Uteis.isAtributoPreenchido(requerimentoVO.getPessoa().getCodigo())){
			Boolean isPermitido = false;
			String tipoPessoa = "Aluno";
			if (requerimentoVO.getTipoRequerimento().getRequerimentoVisaoCoordenador()) {				
				tipoPessoa += ", Coordenador";
				isPermitido = requerimentoVO.getPessoa().getCoordenador();
			}
			if (!isPermitido && requerimentoVO.getTipoRequerimento().getRequerimentoVisaoProfessor()) {				
				tipoPessoa += ", Professor";
				isPermitido = requerimentoVO.getPessoa().getProfessor();				
			}
			if (!isPermitido && requerimentoVO.getTipoRequerimento().getRequerimentoVisaoFuncionario()) {				
				tipoPessoa += ", Funcionário";				
				isPermitido = requerimentoVO.getPessoa().getFuncionario();				
			}							
			if(!isPermitido){
				requerimentoVO.setPessoa(null);
				throw new Exception("Tipo de Requerimento habilitado apenas para requerente(s) do(s) tipo(s) "+tipoPessoa+".");
			}
		}
	}
	
	public List<RequerimentoVO> consultarOtimizadoParaAlterarResponsavel(AlterarResponsavelRequerimentoVO alterarResponsavelRequerimento,
				List<UnidadeEnsinoVO> unidadeEnsinoVOs, CursoVO curso, TurmaVO turma, DepartamentoVO departamento, TipoRequerimentoVO tipoRequerimento, Date dataIni, Date dataFim,
				Boolean todoPeriodo, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer("SELECT DISTINCT r.codigo, r.data, r.dataPrevistaFinalizacao, r.situacao, r.tipoRequerimento, r.pessoa, r.matricula, ");
		sqlStr.append("p.nome as nomePessoa, tr.nome as nomeTipoRequerimento, r.funcionario, f.pessoa as pessoaFuncionario, pf.nome as nomePessoaFuncionario, ");
		sqlStr.append("m.aluno as alunoMatricula, pm.nome as nomeAlunoMatricula, r.departamentoResponsavel ");
		sqlStr.append("FROM Requerimento as r ");
		sqlStr.append("INNER JOIN TipoRequerimento as tr on r.tipoRequerimento = tr.codigo ");
		sqlStr.append("INNER JOIN Funcionario as f on r.funcionario = f.codigo ");
		sqlStr.append("INNER JOIN Pessoa as pf on f.pessoa = pf.codigo ");
		sqlStr.append("INNER JOIN Pessoa as p on r.pessoa = p.codigo ");
		sqlStr.append("INNER JOIN Matricula as m on m.matricula = r.matricula ");
		sqlStr.append("INNER JOIN Pessoa as pm on m.aluno = pm.codigo ");
		sqlStr.append("WHERE r.situacao not in ('FD', 'FI') ");
		String aux = "";
		StringBuffer sqlAux = new StringBuffer("");
		for (UnidadeEnsinoVO ue : unidadeEnsinoVOs) {
			if (ue.getFiltrarUnidadeEnsino()) {
				sqlAux.append(aux).append(ue.getCodigo());
				aux = ",";
			}
		}
		if (!aux.isEmpty()) {
			sqlStr.append(" AND r.unidadeensino in (").append(sqlAux).append(") ");
		}
		if (alterarResponsavelRequerimento.getResponsavelAnterior().getCodigo() != 0) {
			sqlStr.append(" AND r.funcionario = ").append(alterarResponsavelRequerimento.getResponsavelAnterior().getCodigo());
		}
		if (!curso.getCodigo().equals(0)) {
			sqlStr.append(" AND r.curso = ").append(curso.getCodigo());
		}
		if (!turma.getCodigo().equals(0)) {
			sqlStr.append(" AND r.turma = ").append(turma.getCodigo());
		}
		if (!tipoRequerimento.getCodigo().equals(0)) {
			sqlStr.append(" AND r.tipoRequerimento = ").append(tipoRequerimento.getCodigo());
		}
		if (!departamento.getCodigo().equals(0)) {
			sqlStr.append(" AND r.departamentoresponsavel = ").append(departamento.getCodigo());
		}
		if (!todoPeriodo) {
			if (dataIni != null) {
				sqlStr.append(" AND r.data >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateSemHora(dataIni))).append("' ");
			}
			if (dataFim != null) {
				sqlStr.append(" AND r.data <= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateHoraFinalDia(dataFim))).append("' ");
			}
		}
		sqlStr.append("ORDER BY r.codigo");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<RequerimentoVO> vetResultado = new ArrayList<RequerimentoVO>(0);
		while (dadosSQL.next()) {
			RequerimentoVO obj = new RequerimentoVO();
			obj.setNovoObj(Boolean.FALSE);
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.setData(dadosSQL.getTimestamp("data"));
			obj.setDataPrevistaFinalizacao(dadosSQL.getDate("dataPrevistaFinalizacao"));
			obj.setSituacao(dadosSQL.getString("situacao"));
			obj.getTipoRequerimento().setCodigo(dadosSQL.getInt("tipoRequerimento"));
			obj.getTipoRequerimento().setNome(dadosSQL.getString("nomeTipoRequerimento"));
			obj.getFuncionarioVO().setCodigo(dadosSQL.getInt("funcionario"));
			obj.getFuncionarioVO().getPessoa().setCodigo(dadosSQL.getInt("pessoaFuncionario"));
			obj.getFuncionarioVO().getPessoa().setNome(dadosSQL.getString("nomePessoaFuncionario"));
			obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
			obj.getMatricula().getAluno().setCodigo(dadosSQL.getInt("alunoMatricula"));
			obj.getMatricula().getAluno().setNome(dadosSQL.getString("nomeAlunoMatricula"));
			obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
			obj.getPessoa().setNome(dadosSQL.getString("nomePessoa"));
			obj.getDepartamentoResponsavel().setCodigo(dadosSQL.getInt("departamentoResponsavel"));
			obj.setRequerimentoHistoricoVOs(getFacadeFactory().getRequerimentoHistoricoFacade().consultarPorCodigoRequerimento(obj.getCodigo(), false, null));
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	public  void montarDadosTurma(RequerimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getTurma().getCodigo().intValue() == 0) {
			obj.setTurma(new TurmaVO());
			return;
		}
		obj.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurma().getCodigo(), nivelMontarDados, usuario)); 
		if (obj.getTurmaReposicao().getCodigo().intValue() == 0) {
			obj.setTurmaReposicao(new TurmaVO());
			return;
	}
		obj.setTurmaReposicao(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurmaReposicao().getCodigo(), nivelMontarDados, usuario)); 
	}
	
	public  void montarDadosDisciplina(RequerimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getDisciplina().getCodigo().intValue() == 0) {
			obj.setDisciplina(new DisciplinaVO());
			return;
		}
		obj.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplina().getCodigo(), nivelMontarDados, usuario)); 
	}
	
	@Override
	public String executarVerificacaoProfessorMinistrouAula(String matricula, Integer disciplina, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select array_to_string(");
		sqlStr.append("	array_agg(");
		sqlStr.append("		distinct");
		sqlStr.append("		(case when tiposubturma = 'PRATICA' then 'Prof. Prático: '");
		sqlStr.append("		when tiposubturma = 'TEORICA' then 'Prof. Teórico: '");
		sqlStr.append("		else 'Prof.: ' end) ||	pessoa.nome");
		sqlStr.append("	), ', ') as professores ");
		sqlStr.append("from horarioturma ");
		sqlStr.append("inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sqlStr.append("inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
		sqlStr.append("inner join turma on turma.codigo = horarioturma.turma ");
		sqlStr.append("inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor, ");
		sqlStr.append("(select mptd.codigo, mptd.disciplina, mptd.ano, mptd.semestre from matriculaperiodoturmadisciplina mptd");
		sqlStr.append("	inner join matriculaperiodo mp on mp.codigo = mptd.matriculaperiodo");
		sqlStr.append("	inner join periodoletivo pl on pl.codigo = mp.periodoLetivoMatricula");
		sqlStr.append("	where mptd.matricula = '").append(matricula).append("'");
		sqlStr.append("	and mptd.disciplina = ").append(disciplina);
		sqlStr.append("	and mp.situacaomatriculaperiodo not in ('PC')");
		sqlStr.append("	order by (mp.ano ||'/'|| mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, pl.PeriodoLetivo desc, mp.codigo desc limit 1");
		sqlStr.append(") as mptd ");
		sqlStr.append("where anovigente = mptd.ano ");
		sqlStr.append("and semestrevigente = mptd.semestre ");
		sqlStr.append("and turma in (");
		sqlStr.append("	select turmapratica from matriculaperiodoturmadisciplina");
		sqlStr.append("	where matriculaperiodoturmadisciplina.codigo = mptd.codigo");
		sqlStr.append("	and turmapratica is not null");
		sqlStr.append("	union");
		sqlStr.append("	select turmateorica from matriculaperiodoturmadisciplina");
		sqlStr.append("	where matriculaperiodoturmadisciplina.codigo = mptd.codigo");
		sqlStr.append("	and turmateorica is not null");
		sqlStr.append("	union");
		sqlStr.append("	select turma from matriculaperiodoturmadisciplina");
		sqlStr.append("	where matriculaperiodoturmadisciplina.codigo = mptd.codigo");
		sqlStr.append("	and turmateorica is null");
		sqlStr.append("	and turmapratica is null");
		sqlStr.append("	union");
		sqlStr.append("	select turma.codigo as turma from matriculaperiodoturmadisciplina");
		sqlStr.append("	inner join turmaagrupada on turmaagrupada.turma = matriculaperiodoturmadisciplina.turmapratica");
		sqlStr.append("	inner join turma on turmaagrupada.turmaorigem = turma.codigo");
		sqlStr.append("	where matriculaperiodoturmadisciplina.codigo = mptd.codigo");
		sqlStr.append("	and turma.situacao = 'AB'");
		sqlStr.append("	union");
		sqlStr.append("	select turma.codigo as turma from matriculaperiodoturmadisciplina");
		sqlStr.append("	inner join turmaagrupada on turmaagrupada.turma = matriculaperiodoturmadisciplina.turmateorica");
		sqlStr.append("	inner join turma on turmaagrupada.turmaorigem = turma.codigo");
		sqlStr.append("	where matriculaperiodoturmadisciplina.codigo = mptd.codigo");
		sqlStr.append("	and turma.situacao = 'AB'");
		sqlStr.append("	union");
		sqlStr.append("	select turma.codigo as turma from matriculaperiodoturmadisciplina");
		sqlStr.append("	inner join turmaagrupada on turmaagrupada.turma = matriculaperiodoturmadisciplina.turma and turmateorica is null and turmapratica is null");
		sqlStr.append("	inner join turma on turmaagrupada.turmaorigem = turma.codigo");
		sqlStr.append("	where matriculaperiodoturmadisciplina.codigo = mptd.codigo");
		sqlStr.append("	and turma.situacao = 'AB'");
		sqlStr.append(") ");
		sqlStr.append("and ((turma.turmaagrupada = false and horarioturmadiaitem.disciplina = mptd.disciplina)");
		sqlStr.append("	or (turma.turmaagrupada and horarioturmadiaitem.disciplina in (");
		sqlStr.append("		select mptd.disciplina");
		sqlStr.append("		union select disciplina from disciplinaequivalente where equivalente = mptd.disciplina");
		sqlStr.append("		union select equivalente from disciplinaequivalente where disciplina = mptd.disciplina");
		sqlStr.append("	)))");
		SqlRowSet rowSet = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rowSet.next()) {
			return rowSet.getString("professores");
		}
		return "";
	}
	
	@Override
	public void executarVerificacaoUsuarioPossuiPermissaoInformarDescontoAcrescimo(RequerimentoVO requerimentoVO, boolean valorDescontoSelecionado, String tipoDescontoTemp, Double valorAcrescimoDescontoTemp, String usuarioLiberarOperacaoFuncionalidade, String senhaLiberarOperacaoFuncionalidade, List<OperacaoFuncionalidadeVO> operacaoFuncionalidadeVOs) throws Exception {
		String str = "Desconto";
		UsuarioVO usuarioVO = verificarLoginUsuario(usuarioLiberarOperacaoFuncionalidade, senhaLiberarOperacaoFuncionalidade, true, Uteis.NIVELMONTARDADOS_TODOS);
		try {
			String observacao = "";
			if (valorDescontoSelecionado) {
				verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirInformarDesconto", usuarioVO);
				requerimentoVO.setTipoDesconto(tipoDescontoTemp);
				if (requerimentoVO.getTipoDesconto().equals("VA")) {
					requerimentoVO.setValorDesconto(valorAcrescimoDescontoTemp);
					observacao = "Alterado tipo desconto para " + TipoDescontoAluno.getDescricao(requerimentoVO.getTipoDesconto()) + " e o valor desconto para " + requerimentoVO.getValorDesconto();
				} else {
					requerimentoVO.setPercDesconto(valorAcrescimoDescontoTemp);
					observacao = "Alterado tipo desconto para " + TipoDescontoAluno.getDescricao(requerimentoVO.getTipoDesconto()) + " e o percentual de desconto para " + requerimentoVO.getPercDesconto();
				}
				operacaoFuncionalidadeVOs.add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.REQUERIMENTO, "", OperacaoFuncionalidadeEnum.REQUERIMENTO_ALTERAR_VALOR_DESCONTO, usuarioVO, observacao));
			} else {
				str = "Valor Adicional";
				verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirInformarAcrescimo", usuarioVO);
				requerimentoVO.setValorAdicional(valorAcrescimoDescontoTemp);
				operacaoFuncionalidadeVOs.add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.REQUERIMENTO, "", OperacaoFuncionalidadeEnum.REQUERIMENTO_ALTERAR_ACRESCIMO, usuarioVO, "Alterado o valor adicional para " + requerimentoVO.getValorAdicional()));
			}
			if(requerimentoVO.getValorTotalFinal() > 0 && requerimentoVO.getTipoRequerimento().getRequerAutorizacaoPagamento()) {
				requerimentoVO.setSituacaoFinanceira("AP");
			}else if(requerimentoVO.getValorTotalFinal() == 0.0) {
				requerimentoVO.setSituacaoFinanceira("IS");
			}else if(requerimentoVO.getValorTotalFinal() > 0) {
				requerimentoVO.setSituacaoFinanceira("PE");
			}
		} catch (Exception e) {
			throw new Exception(UteisJSF.internacionalizar("msg_Requerimento_usuarioNaoTemPermissaoAlterarValorDescontoAcrescimo").replace("{0}", usuarioVO.getNome()).replace("{1}", str));
		}
	}
	
	
	@Override
	public void alterarSituacaoRequerimento(final Integer requerimento, final String situacao, UsuarioVO usuarioVO) throws Exception {
		final String sql = "UPDATE Requerimento set situacao=? WHERE codigo = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setString(1, situacao);
				sqlAlterar.setInt(2, requerimento.intValue());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	public RequerimentoVO consultarPorChavePrimariaTipoRequerimentoInclusaoEReposicaoDisciplinaDadosMinimos(Integer codigoPrm, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("select requerimento.codigo, requerimento.matricula, requerimento.situacao, requerimento.situacaofinanceira, requerimento.data, requerimento.unidadeensino, requerimento.disciplina, disciplina.nome AS  \"disciplina.nome\", ");
		sb.append(" tiporequerimento.codigo AS \"tiporequerimento.codigo\", tiporequerimento.nome AS \"tiporequerimento.nome\", tiporequerimento.tipo AS \"tiporequerimento.tipo\" ");
		sb.append(" from requerimento ");
		sb.append(" left join disciplina on disciplina.codigo = requerimento.disciplina ");
		sb.append(" inner join tiporequerimento on tiporequerimento.codigo = requerimento.tiporequerimento ");
		sb.append(" where requerimento.codigo = ").append(codigoPrm);
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sb.append(" and requerimento.unidadeEnsino = ").append(unidadeEnsino);
		}
		sb.append(" and tiporequerimento.tipo in('INCLUSAO_DISCIPLINA', 'RE') ");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		RequerimentoVO obj = null;
		if (tabelaResultado.next()) {
			obj = new RequerimentoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getMatricula().setMatricula(tabelaResultado.getString("matricula"));
			obj.setSituacao(tabelaResultado.getString("situacao"));
			obj.setSituacaoFinanceira(tabelaResultado.getString("situacaoFinanceira"));
			obj.setData(tabelaResultado.getTimestamp("data"));
			obj.getUnidadeEnsino().setCodigo(tabelaResultado.getInt("unidadeEnsino"));
			obj.getDisciplina().setCodigo(tabelaResultado.getInt("disciplina"));
			obj.getDisciplina().setNome(tabelaResultado.getString("disciplina.nome"));
			obj.getTipoRequerimento().setCodigo(tabelaResultado.getInt("tiporequerimento.codigo"));
			obj.getTipoRequerimento().setNome(tabelaResultado.getString("tiporequerimento.nome"));
			obj.getTipoRequerimento().setTipo(tabelaResultado.getString("tiporequerimento.tipo"));
		}
		return obj;
	}


	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirRequerimentoPorMatricula(String matricula, Integer unidadeEnsino, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  UsuarioVO usuarioVO) throws Exception{
		List<RequerimentoVO> requerimentoVOs = consultaRapidaRequerimentoPorMatricula(matricula, unidadeEnsino,null, false, usuarioVO, configuracaoGeralSistemaVO);
		for(RequerimentoVO requerimentoVO: requerimentoVOs){
			carregarDados(requerimentoVO, NivelMontarDados.TODOS, configuracaoGeralSistemaVO, usuarioVO);
			getFacadeFactory().getTransferenciaEntradaFacade().removerVinculoTransferenciaEntradaRequerimento(requerimentoVO.getCodigo(), usuarioVO);
			excluir(requerimentoVO,  usuarioVO, configuracaoGeralSistemaVO, false);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirRequerimentoPorMatriculaPeriodo(Integer matriculaPeriodo, Integer unidadeEnsino, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  UsuarioVO usuarioVO) throws Exception{
		List<RequerimentoVO> requerimentoVOs = consultaRapidaRequerimentoPorMatriculaPeriodo(matriculaPeriodo, unidadeEnsino,null, false, usuarioVO, configuracaoGeralSistemaVO);
		for(RequerimentoVO requerimentoVO: requerimentoVOs){
			carregarDados(requerimentoVO, NivelMontarDados.TODOS, configuracaoGeralSistemaVO, usuarioVO);
			getFacadeFactory().getTransferenciaEntradaFacade().removerVinculoTransferenciaEntradaRequerimento(requerimentoVO.getCodigo(), usuarioVO);
			excluir(requerimentoVO,  usuarioVO, configuracaoGeralSistemaVO, false);
		}
	}
	
	
	@Override
	public Boolean realizarVerificarProximoTramiteExigeInformarCoordenadorCursoResponsavel(RequerimentoVO requerimentoVO, FuncionarioVO funcionarioProximoTramite, UsuarioVO usuario) throws Exception {
		TipoRequerimentoDepartamentoVO proximoTipoRequerimentoDepartamentoVO = requerimentoVO.getTipoRequerimento().consultarTipoRequerimentoDepartamentoVOs(requerimentoVO.getOrdemExecucaoTramiteDepartamento() + 1);
		if (Uteis.isAtributoPreenchido(proximoTipoRequerimentoDepartamentoVO)) {
			return (proximoTipoRequerimentoDepartamentoVO.getTipoDistribuicaoResponsavel().equals(TipoDistribuicaoResponsavelEnum.COORDENADOR_CURSO_ESPECIFICO_NO_TRAMITE));
		}
		return false;
	}

	@Override
	public List<RequerimentoVO> consultarPorMatriculaFichaAluno(String matricula, Integer tipoRequerimento, String situacao, String mesAno, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct requerimento.codigo, requerimento.situacao, requerimento.situacaofinanceira, requerimento.dataPrevistaFinalizacao, requerimento.valor, requerimento.data, requerimento.contaReceber, contaReceber.situacao AS \"contaReceber.situacao\", ");
		sb.append(" requerimento.pessoa, ");
		sb.append(" tipoRequerimento.codigo AS \"tipoRequerimento.codigo\", tipoRequerimento.nome AS \"tipoRequerimento.nome\", ");
		sb.append(" departamentoResponsavel.codigo AS \"departamentoResponsavel.codigo\", departamentoResponsavel.nome AS \"departamentoResponsavel.nome\", ");
		sb.append(" responsavel.codigo AS \"responsavel.codigo\", responsavel.nome AS \"responsavel.nome\"");
		sb.append(" from requerimento ");
		sb.append(" inner join tipoRequerimento on tipoRequerimento.codigo = requerimento.tipoRequerimento ");
		sb.append(" left join departamento AS departamentoResponsavel on departamentoResponsavel.codigo = requerimento.departamentoResponsavel ");
		sb.append(" left join funcionario on funcionario.codigo = requerimento.funcionario ");
		sb.append(" left join pessoa responsavel on responsavel.codigo = funcionario.pessoa ");
		sb.append(" left join contaReceber on contaReceber.codigo = requerimento.contaReceber ");
		sb.append(" where requerimento.matricula = '").append(matricula).append("' ");
		if (situacao != null && !situacao.equals("")) {
			sb.append(" and requerimento.situacao = '").append(situacao).append("' ");
		}
		if (tipoRequerimento != null && !tipoRequerimento.equals(0)) {
			sb.append(" and tipoRequerimento.codigo = ").append(tipoRequerimento);
		}
		if (mesAno != null && !mesAno.equals("")) {
			sb.append(" and extract(month from requerimento.data) = ").append(getMesDataVencimentoContaReceber(mesAno));
			sb.append(" and extract(year from requerimento.data) = ").append(getAnoDataVencimentoContaReceber(mesAno));
		}
		sb.append(" order by data desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		boolean possuiPermissaoEmitirBoletoVencido = verificarPermissaoFuncionalidadeUsuario("ImprimirBoletoVencidoVisaoAluno", usuarioVO);
		List<RequerimentoVO> listaRequerimentoVOs = new ArrayList<RequerimentoVO>(0);
//		while (tabelaResultado.next()) {
//			RequerimentoVO obj = new RequerimentoVO();
//			obj.setCodigo(tabelaResultado.getInt("codigo"));
//			obj.setSituacao(tabelaResultado.getString("situacao"));
//			obj.setSituacaoFinanceira(tabelaResultado.getString("situacaofinanceira"));
//			obj.setDataPrevistaFinalizacao(tabelaResultado.getDate("dataPrevistaFinalizacao"));
//			obj.setData(tabelaResultado.getTimestamp("data"));
//			obj.setValor(tabelaResultado.getDouble("valor"));
//			obj.setContaReceber(tabelaResultado.getInt("contaReceber"));			
//			obj.getContaReceberVO().setCodigo(tabelaResultado.getInt("contaReceber"));						
//			if(Uteis.isAtributoPreenchido(tabelaResultado.getInt("contaReceber"))){
//				getFacadeFactory().getContaReceberFacade().carregarDados(obj.getContaReceberVO(), NivelMontarDados.BASICO, null, usuarioVO);
//				getFacadeFactory().getContaReceberFacade().validarTipoImpressaoPorContaReceber(obj.getContaReceberVO(), possuiPermissaoEmitirBoletoVencido, usuarioVO);
//			}
//			obj.getPessoa().setCodigo(tabelaResultado.getInt("pessoa"));
//			
//			obj.getFuncionarioVO().getPessoa().setCodigo(tabelaResultado.getInt("responsavel.codigo"));
//			obj.getFuncionarioVO().getPessoa().setNome(tabelaResultado.getString("responsavel.nome"));
//			
//			obj.getDepartamentoResponsavel().setCodigo(tabelaResultado.getInt("departamentoResponsavel.codigo"));
//			obj.getDepartamentoResponsavel().setNome(tabelaResultado.getString("departamentoResponsavel.nome"));
//			
//			obj.getTipoRequerimento().setCodigo(tabelaResultado.getInt("tipoRequerimento.codigo"));
//			obj.getTipoRequerimento().setNome(tabelaResultado.getString("tipoRequerimento.nome"));
//			
//			listaRequerimentoVOs.add(obj);
//		}
		
		return listaRequerimentoVOs;
	}
	
	public RequerimentoVO inicializarRequerimentoConformeTipoRequerimento(TipoRequerimentoVO tipoRequerimentoVO, RequerimentoVO requerimento, Boolean forcarDefinicaoData) throws Exception {
		Date dataAux = new Date();
		requerimento.setTipoRequerimento(tipoRequerimentoVO);
		if (!tipoRequerimentoVO.getPermitirInformarEnderecoEntrega()) {
			requerimento.setCEP("");
			requerimento.setEndereco("");
			requerimento.setSetor("");
			requerimento.setNumero("");
			requerimento.setComplemento("");
			requerimento.setCidade(null);
		}
		if (!tipoRequerimentoVO.getPermitirUploadArquivo()) {
			requerimento.setArquivoVO(null);
		}
		//realizarDefinicaoVencimentoContaReceberRequerimento(requerimento, forcarDefinicaoData);
		//requerimento.setValor(tipoRequerimentoVO.getValor());		
		
		if(
				(!tipoRequerimentoVO.getIsEmissaoCertificado() && !tipoRequerimentoVO.getIsDeclaracao()) ||
				(Uteis.isAtributoPreenchido(tipoRequerimentoVO.getValor()) && !Uteis.isAtributoPreenchido(tipoRequerimentoVO.getTextoPadrao().getCodigo()) && !Uteis.isAtributoPreenchido(tipoRequerimentoVO.getCertificadoImpresso())) ||
				(Uteis.isAtributoPreenchido(tipoRequerimentoVO.getValor()) && Uteis.isAtributoPreenchido(tipoRequerimentoVO.getTextoPadrao().getCodigo()) && Uteis.isAtributoPreenchido(tipoRequerimentoVO.getCertificadoImpresso()) && !tipoRequerimentoVO.getCobrarTaxaSomenteCertificadoImpresso()) ||
				(Uteis.isAtributoPreenchido(tipoRequerimentoVO.getValor()) && !Uteis.isAtributoPreenchido(tipoRequerimentoVO.getTextoPadrao().getCodigo()) && Uteis.isAtributoPreenchido(tipoRequerimentoVO.getCertificadoImpresso()) && tipoRequerimentoVO.getCobrarTaxaSomenteCertificadoImpresso()) ||
				(Uteis.isAtributoPreenchido(tipoRequerimentoVO.getValor()) && !tipoRequerimentoVO.getCobrarTaxaSomenteCertificadoImpresso())
				) {
			realizarDefinicaoVencimentoContaReceberRequerimento(requerimento, forcarDefinicaoData);
			requerimento.setValor(tipoRequerimentoVO.getValor());	
		}
		requerimento.setDataPrevistaFinalizacao(atualizarDataPrevistaFinalizacao(requerimento));
		if (!tipoRequerimentoVO.getTramitaEntreDepartamentos()) {
			requerimento.setDepartamentoResponsavel(tipoRequerimentoVO.getDepartamentoResponsavel());
		} else {
			if (tipoRequerimentoVO.getTipoRequerimentoDepartamentoVOs().isEmpty()) {
				throw new Exception("Departamentos para Trâmite do Requerimento Não Foram Definidos no Cadastro do Tipo de Requerimento");
			}
			if(requerimento.getTipoRequerimento().getConsiderarDiasUteis()){
				for (TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO  : requerimento.getTipoRequerimento().getTipoRequerimentoDepartamentoVOs()) {
					tipoRequerimentoDepartamentoVO.setPrazoExecucao(getFacadeFactory().getFeriadoFacade().consultarNrDiasUteisProgredir(dataAux, tipoRequerimentoDepartamentoVO.getPrazoExecucao().intValue(), requerimento.getCidade().getCodigo(), true, ConsiderarFeriadoEnum.ACADEMICO));
				
					dataAux = (Uteis.getDataFuturaConsiderandoDataAtual(dataAux, tipoRequerimentoDepartamentoVO.getPrazoExecucao()));
				}
			}
			requerimento.setDepartamentoResponsavel(tipoRequerimentoVO.getTipoRequerimentoDepartamentoVOs().get(0).getDepartamento());
		}
		return requerimento;
	}
	
	public Date atualizarDataPrevistaFinalizacao(RequerimentoVO requerimentoVO) {
		try {
			Date dataAtual = new Date();
			if (requerimentoVO.getTipoRequerimento().getConsiderarDiasUteis()) {
				dataAtual = Uteis.getDataFuturaConsiderandoDataAtual(new Date(), getFacadeFactory().getFeriadoFacade().consultarNrDiasUteisProgredir(dataAtual, requerimentoVO.getTipoRequerimento().getPrazoExecucao().intValue(), requerimentoVO.getCidade().getCodigo(), true, ConsiderarFeriadoEnum.ACADEMICO));
			} else {
				dataAtual = Uteis.getDataFuturaConsiderandoDataAtual(new Date(), requerimentoVO.getTipoRequerimento().getPrazoExecucao().intValue());
			}
			// setDataPrevistaFinalizacao(dataAtual.getTime());
			return dataAtual;
		} catch (Exception e) {
			// TODO: handle exception
			return new Date();
		}
	}
	
	public Integer getMesDataVencimentoContaReceber(String mesAno) {
		if (mesAno != null && !mesAno.equals("")) {
			String mes = mesAno.substring(0, mesAno.indexOf("/"));
			return Uteis.getMesConcatenadoReferencia(mes);
		}
		return 0;
	}
	
	public Integer getAnoDataVencimentoContaReceber(String mesAno) {
		if (mesAno != null && !mesAno.equals("")) {
			String ano = mesAno.substring(mesAno.indexOf("/") + 1);
			return Integer.parseInt(ano);
		}
		return 0;
	}
	
	@Override
	public List<SelectItem> consultarMesAnoRequerimentoPorAlunoFichaAluno(Integer aluno, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select mes ||'/'|| ano AS mesAno from (");
		sb.append(" select distinct case ");
		sb.append(" when extract(month from requerimento.data) = 1 then 'JAN' ");
		sb.append(" when extract(month from requerimento.data) = 2 then 'FEV' ");
		sb.append(" when extract(month from requerimento.data) = 3 then 'MAR' ");
		sb.append(" when extract(month from requerimento.data) = 4 then 'ABR' ");
		sb.append(" when extract(month from requerimento.data) = 5 then 'MAI' ");
		sb.append(" when extract(month from requerimento.data) = 6 then 'JUN' ");
		sb.append(" when extract(month from requerimento.data) = 7 then 'JUL' ");
		sb.append(" when extract(month from requerimento.data) = 8 then 'AGO' ");
		sb.append(" when extract(month from requerimento.data) = 9 then 'SET' ");
		sb.append(" when extract(month from requerimento.data) = 10 then 'OUT' ");
		sb.append(" when extract(month from requerimento.data) = 11 then 'NOV' ");
		sb.append(" when extract(month from requerimento.data) = 12 then 'DEZ' ");
		sb.append(" end AS mes, extract(year from requerimento.data) AS ano, requerimento.data ");
		sb.append(" from requerimento ");
		sb.append(" inner join matricula on matricula.matricula = requerimento.matricula ");
		sb.append(" where matricula.aluno = ").append(aluno);
		sb.append(" order by requerimento.data desc) as t ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<SelectItem> listaSelectItemMesAnoContaReceberVOs = new ArrayList<SelectItem>(0);
		listaSelectItemMesAnoContaReceberVOs.add(new SelectItem("", ""));
		while (tabelaResultado.next()) {
			listaSelectItemMesAnoContaReceberVOs.add(new SelectItem(tabelaResultado.getString("mesAno"), tabelaResultado.getString("mesAno")));
		}
		return listaSelectItemMesAnoContaReceberVOs;
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void imprimirComprovanteRequerimentoBemateck(RequerimentoVO requerimentoVO, ImpressoraVO impressoraVO, UsuarioVO usuarioVO, String professorMinistrouAula) throws Exception{
		StringBuilder texto = new StringBuilder("");
		PoolImpressaoVO poolImpressaoVO = new PoolImpressaoVO();
		texto.append("Unidade de Ensino: ").append(Uteis.removerAcentos(requerimentoVO.getUnidadeEnsino().getNome())).append(">");		
		texto.append("Num do Req.: ").append(requerimentoVO.getCodigo()).append(" - ").append(requerimentoVO.getNumeroVia()).append(" Via>");
		texto.append("Data Abertura: ").append(Uteis.getData(requerimentoVO.getData())).append(" >");
		texto.append("Data Previsao: ").append(Uteis.getData(requerimentoVO.getDataPrevistaFinalizacao())).append(" >");
		texto.append("Req.: ").append(Uteis.removerAcentos(requerimentoVO.getTipoRequerimento().getNome())).append(">");
		if(requerimentoVO.getTipoPessoaAluno()){
			texto.append("Requerente: ").append(Uteis.removerAcentos(requerimentoVO.getPessoa().getNome())).append(">");
		}else{
			texto.append("Aluno: ").append(Uteis.removerAcentos(requerimentoVO.getPessoa().getNome())).append(">");
		}
		if(!requerimentoVO.getPessoa().getRG().trim().isEmpty() && !requerimentoVO.getPessoa().getCPF().contains("T")){
			texto.append("RG: ").append(requerimentoVO.getPessoa().getRG()).append(" - CPF: ").append(requerimentoVO.getPessoa().getCPF()).append(">");
		}
		if(!requerimentoVO.getPessoa().getRG().trim().isEmpty() && requerimentoVO.getPessoa().getCPF().contains("T")){
			texto.append("RG: ").append(requerimentoVO.getPessoa().getRG()).append(">");
		}
		if(requerimentoVO.getPessoa().getRG().trim().isEmpty() && !requerimentoVO.getPessoa().getCPF().contains("T")){
			texto.append("CPF: ").append(requerimentoVO.getPessoa().getCPF()).append(">");
		}
		if(!requerimentoVO.getPessoa().getTelefoneRes().trim().isEmpty() && !requerimentoVO.getPessoa().getCelular().trim().isEmpty()){
			texto.append("Fones: ").append(requerimentoVO.getPessoa().getTelefoneRes()).append(" / ").append(requerimentoVO.getPessoa().getCelular()).append(">");
		}
		if(!requerimentoVO.getPessoa().getTelefoneRes().trim().isEmpty() && requerimentoVO.getPessoa().getCelular().trim().isEmpty()){
			texto.append("Fone Res: ").append(requerimentoVO.getPessoa().getTelefoneRes()).append(">");
		}
		if(requerimentoVO.getPessoa().getTelefoneRes().trim().isEmpty() && !requerimentoVO.getPessoa().getCelular().trim().isEmpty()){
			texto.append("Celular: ").append(requerimentoVO.getPessoa().getCelular()).append(">");
		}
		
		if(!requerimentoVO.getPessoa().getEmail().trim().isEmpty()){
			texto.append("Email: ").append(requerimentoVO.getPessoa().getEmail()).append(">");
		}
		if(!requerimentoVO.getMatricula().getMatricula().isEmpty()){
			texto.append("Matricula: ").append(requerimentoVO.getMatricula().getMatricula()).append(">");
		}
//		if(!requerimentoVO.getSituacaoMatriculaPeriodo().trim().isEmpty()){
//			texto.append("Sit. Matricula: ").append(Uteis.removerAcentos(requerimentoVO.getSituacaoMatriculaPeriodo())).append(">");
//		}
		if(!requerimentoVO.getCurso().getNome().isEmpty()){
			texto.append("Curso: ").append(Uteis.removerAcentos(requerimentoVO.getCurso().getNome())).append(">");
		}
		if(!requerimentoVO.getTurma().getIdentificadorTurma().isEmpty()){
			texto.append("Turma: ").append(Uteis.removerAcentos(requerimentoVO.getTurma().getIdentificadorTurma())).append(">");
		}
		if(requerimentoVO.getTurma().getPeridoLetivo().getPeriodoLetivo() > 0){
			texto.append("Periodo Letivo: ").append(requerimentoVO.getTurma().getPeridoLetivo().getPeriodoLetivo()).append(">");
		}
//		if(!requerimentoVO.getAno().isEmpty() && !requerimentoVO.getSemestre().trim().isEmpty()){
//			texto.append("Ano/Semestre: ").append(requerimentoVO.getAno()).append("/").append(requerimentoVO.getSemestre()).append(">");			
//		}
		if(!requerimentoVO.getAno().isEmpty() && requerimentoVO.getSemestre().trim().isEmpty()){
			texto.append("Ano: ").append(requerimentoVO.getAno()).append(">");			
		}
		if(Uteis.isAtributoPreenchido(requerimentoVO.getDisciplina().getCodigo())){
			texto.append("Disciplina: ").append(Uteis.removerAcentos(requerimentoVO.getDisciplina().getNome())).append(">");			
		}
		
		if(Uteis.isAtributoPreenchido(professorMinistrouAula)){
			texto.append("Professor Ministrou Aula: ").append(Uteis.removerAcentos(professorMinistrouAula)).append(">");			
		}
		
		if(Uteis.isAtributoPreenchido(requerimentoVO.getUnidadeEnsinoTransferenciaInternaVO().getCodigo())){
			texto.append("Unid. Ens. Transf.: ").append(Uteis.removerAcentos(requerimentoVO.getUnidadeEnsinoTransferenciaInternaVO().getNome())).append(">");			
		}
		if(Uteis.isAtributoPreenchido(requerimentoVO.getCursoTransferenciaInternaVO().getCodigo())){
			texto.append("Curso Transf.: ").append(Uteis.removerAcentos(requerimentoVO.getCursoTransferenciaInternaVO().getNome())).append(">");			
		}
		if(Uteis.isAtributoPreenchido(requerimentoVO.getTurnoTransferenciaInternaVO().getCodigo())){
			texto.append("Turno Transf.: ").append(Uteis.removerAcentos(requerimentoVO.getTurnoTransferenciaInternaVO().getNome())).append(">");			
		}
		if(!requerimentoVO.getTipoRequerimento().getMensagemAlerta().trim().isEmpty()){
			texto.append("Declaro ter ciencia das instrucoes abaixo: ").append(">");
			texto.append(Uteis.removerAcentos(requerimentoVO.getTipoRequerimento().getMensagemAlerta().trim())).append("> ");
			
		}
		if(!requerimentoVO.getObservacao().trim().isEmpty()){
			texto.append("                                         >");
			texto.append("Observacoes: ").append(">");
			texto.append(Uteis.removerAcentos(requerimentoVO.getObservacao().trim())).append(">");
		}
						
		texto.append("                                         >");
		texto.append("                                         >");
		texto.append("&&&&&_______________________________&&&&&>");
		texto.append("            Assinatura Atendente         >");
		texto.append("                                         >");
		texto.append("                                         >");
		texto.append("&&&&&_______________________________&&&&&>");
		texto.append("           Assinatura Requerente         >");		
		texto.append("                                         >");
		texto.append("                                         >");		
		texto.append(Uteis.getDiaMesPorExtensoEAno(new Date(), false)).append(">");
		texto.append("                                         >");		
		texto.append("                                         >");
		poolImpressaoVO.setData(new Date());
		poolImpressaoVO.setImpressoraVO(impressoraVO);
		poolImpressaoVO.setFormatoImpressao(FormatoImpressaoEnum.TEXTO);
		poolImpressaoVO.setImprimir(texto.toString());
		getFacadeFactory().getPoolImpressaoFacade().incluir(poolImpressaoVO, usuarioVO);
	}
	
	@Override
	public Boolean consultarTipoRequerimentoVinculadoRequerimento(TipoRequerimentoVO tipoRequerimentoVO) throws Exception {
		return getConexao().getJdbcTemplate().queryForRowSet("select codigo from requerimento where situacao not in ('FD', 'FI') and tiporequerimento = ? limit 1", tipoRequerimentoVO.getCodigo()).next();
	}
	
	class EnviarComunicadoTramiteRequerimento implements Runnable{
		private RequerimentoVO requerimentoVO;
		private UsuarioVO usuarioLogadoVO;

		public EnviarComunicadoTramiteRequerimento(RequerimentoVO requerimentoVO, UsuarioVO usuarioLogadoVO) {
			super();
			this.requerimentoVO = requerimentoVO;
			try {
				this.usuarioLogadoVO = (UsuarioVO)usuarioLogadoVO.clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemTramiteRequerimento(requerimentoVO, new RequerimentoHistoricoVO(), usuarioLogadoVO);
			} catch (Exception e) {				
				e.printStackTrace();
			}
		}		
		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAutorizacaoPagamentoRequerimento(RequerimentoVO requerimentoVO, UsuarioVO usuario) throws Exception{
		if (requerimentoVO.getValorTotalFinal() > 0.0 && requerimentoVO.getSituacaoFinanceira().equals("AP")) {
			try {
				if(requerimentoVO.getDataVencimentoContaReceber() == null) {
					throw new Exception("O campo VENCIMENTO BOLETO deve ser informado.");
				}
				if(requerimentoVO.getDataVencimentoContaReceber().compareTo(new Date()) <= 0 && !Uteis.getData(requerimentoVO.getDataVencimentoContaReceber()).equals(Uteis.getData(new Date()))) {
					throw new Exception("O campo VENCIMENTO BOLETO não pode ser anterior a "+Uteis.getData(new Date())+".");
				}
				requerimentoVO.setSituacaoFinanceira("PE");
//				if (!Uteis.isAtributoPreenchido(requerimentoVO.getContaReceberVO().getCodigo())) {
//					emitirBoletoParcela(requerimentoVO, usuario);
//				}
			}catch (Exception e) {
				requerimentoVO.setSituacaoFinanceira("AP");
				throw e;
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDadosFinanceiros(final RequerimentoVO obj, UsuarioVO usuario) throws Exception {
		try {			
			final String sql = "UPDATE Requerimento set situacao=?, situacaoFinanceira=?, centroReceita=?, contaReceber=?, nrDocumento=?, responsavelEmissaoBoleto=?, dataEmissaoBoleto=?, contaCorrente=?  WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getSituacao());
					sqlAlterar.setString(2, obj.getSituacaoFinanceira());					
//					sqlAlterar.setInt(3, obj.getCentroReceita().getCodigo().intValue());
					if (obj.getContaReceber() > 0) {
						sqlAlterar.setInt(4, obj.getContaReceber().intValue());
					} else {
						sqlAlterar.setNull(4, 0);
					}
					sqlAlterar.setString(5, obj.getNrDocumento());
					if (obj.getResponsavelEmissaoBoleto().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(6, obj.getResponsavelEmissaoBoleto().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					sqlAlterar.setDate(7, Uteis.getDataJDBC(obj.getDataEmissaoBoleto()));
//					if (obj.getContaCorrenteVO().getCodigo().intValue() != 0) {
//						sqlAlterar.setInt(8, obj.getContaCorrenteVO().getCodigo());
//					} else {
//						sqlAlterar.setNull(8, 0);
//					}					
					sqlAlterar.setInt(9, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			obj.setNovoObj(Boolean.FALSE);
			throw e;
		}
	}
	
	@Override
	public void realizarDefinicaoVencimentoContaReceberRequerimento(RequerimentoVO requerimentoVO, boolean forcarDefinicaoData) throws Exception{
		if(requerimentoVO.getDataVencimentoContaReceber() == null || forcarDefinicaoData) {
		if (requerimentoVO.getTipoRequerimento().getQtdDiasVencimentoRequerimento().intValue() != 0 && (!requerimentoVO.getAguardandoAutorizacaoPagamento() || forcarDefinicaoData)) {
			if (requerimentoVO.getTipoRequerimento().getConsiderarDiasUteis()) {
				requerimentoVO.setDataVencimentoContaReceber(getFacadeFactory().getFeriadoFacade().obterDataFuturaOuRetroativaApenasDiasUteis(new Date(), requerimentoVO.getTipoRequerimento().getQtdDiasVencimentoRequerimento(), requerimentoVO.getUnidadeEnsino().getCidade().getCodigo(), false, false, ConsiderarFeriadoEnum.FINANCEIRO));
			} else {
				requerimentoVO.setDataVencimentoContaReceber(Uteis.obterDataAvancada(new Date(), requerimentoVO.getTipoRequerimento().getQtdDiasVencimentoRequerimento().intValue()));
			}
		} else if(requerimentoVO.getAguardandoAutorizacaoPagamento() && forcarDefinicaoData == false) {
			requerimentoVO.setDataVencimentoContaReceber(null);
		} else {
			requerimentoVO.setDataVencimentoContaReceber(new Date());
		}
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoDepartamento(final Integer requerimento, final Integer situacao, UsuarioVO  usuarioVO) throws Exception{
		try {			
			final String sql = "UPDATE Requerimento set situacaorequerimentodepartamento=?, dataUltimaAlteracao=?  WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					if(Uteis.isAtributoPreenchido(situacao)) {
						sqlAlterar.setInt(1, situacao);
							
					}else {
						sqlAlterar.setNull(1, 0);
					}			
					sqlAlterar.setDate(2, Uteis.getDataJDBC(new Date()));
					sqlAlterar.setInt(3, requerimento);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {			
			throw e;
		}
	}
		
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirSolicitacaoIsencaoTaxa(final RequerimentoVO requerimentoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioVO) throws Exception {
		if (!requerimentoVO.getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.NAO_REQUERIDA)) {
			validarDadosSolicitacaoIsencaoTaxa(requerimentoVO);
			realizarInclusaoArquivoComprovanteSolicitacaoIsencaoTaxa(requerimentoVO, configuracaoGeralSistema, usuarioVO);
			if(!Uteis.isAtributoPreenchido(requerimentoVO.getFuncionarioVO())) {
				TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO = requerimentoVO.getTipoRequerimento().getTipoRequerimentoDepartamentoVOs().get(requerimentoVO.getOrdemExecucaoTramiteDepartamento() - 1);
				if (!Uteis.isAtributoPreenchido(requerimentoVO.getFuncionarioVO().getCodigo())) {
					requerimentoVO.setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().realizarDistribuicaoResponsavelRequerimento(requerimentoVO.getCodigo(), tipoRequerimentoDepartamentoVO, tipoRequerimentoDepartamentoVO.getTipoDistribuicaoResponsavel(), requerimentoVO.getUnidadeEnsino(), usuarioVO, requerimentoVO.getTipoRequerimento()));
					requerimentoVO.gerarNovoRequerimentoHistoricoVODepartamentoAtualTramite(usuarioVO, true, requerimentoVO.getFuncionarioVO(), tipoRequerimentoDepartamentoVO, false, "");
					alterarFuncionarioResponsavel(requerimentoVO.getCodigo(), requerimentoVO.getFuncionarioVO().getCodigo(), usuarioVO);
					getFacadeFactory().getRequerimentoHistoricoFacade().incluirRequerimentoHistoricoVOs(requerimentoVO.getCodigo(), requerimentoVO.getRequerimentoHistoricoVOs(), usuarioVO);
				}
			}
			try {
				final String sql = "UPDATE Requerimento set situacaoIsencaoTaxa=?, comprovanteSolicitacaoIsencao=?, justificativaSolicitacaoIsencao=?  WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
				getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement sqlAlterar = con.prepareStatement(sql);
						sqlAlterar.setString(1, requerimentoVO.getSituacaoIsencaoTaxa().name());
						if (Uteis.isAtributoPreenchido(requerimentoVO.getComprovanteSolicitacaoIsencao())) {
							sqlAlterar.setInt(2, requerimentoVO.getComprovanteSolicitacaoIsencao().getCodigo());
						}else {
							sqlAlterar.setNull(2, 0);
						}
						sqlAlterar.setString(3, requerimentoVO.getJustificativaSolicitacaoIsencao());
						sqlAlterar.setInt(4, requerimentoVO.getCodigo());
						return sqlAlterar;
					}
				});
			} catch (Exception e) {
				throw e;
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRegistroDeferimentoIndeferimentoSolicitacaoIsencaoTaxa(final RequerimentoVO requerimentoVO, final UsuarioVO usuarioVO) throws Exception {
		if (requerimentoVO.getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.DEFERIDO) || requerimentoVO.getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.INDEFERIDO)) {
			if(requerimentoVO.getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.INDEFERIDO)) {	
				if(requerimentoVO.getMotivoDeferimentoIndeferimentoIsencaoTaxa().trim().isEmpty()) {
					throw new Exception(UteisJSF.internacionalizar("msg_Requerimento_motivoDeferimentoIndeferimentoIsencaoTaxa"));
				}
			}
			requerimentoVO.setDataDeferimentoIndeferimentoIsencaoTaxa(new Date());
			requerimentoVO.getResponsavelDeferimentoIndeferimentoIsencaoTaxa().setCodigo(usuarioVO.getCodigo());
			requerimentoVO.getResponsavelDeferimentoIndeferimentoIsencaoTaxa().setNome(usuarioVO.getNome());
			Double valorDesconto =requerimentoVO.getValorDesconto(); 
			Double perDesconto =requerimentoVO.getPercDesconto(); 
			String tipoDesconto =requerimentoVO.getTipoDesconto(); 
			String situacaoFinan = requerimentoVO.getSituacaoFinanceira();
			String situacao = requerimentoVO.getSituacao();
			if (Uteis.isAtributoPreenchido(requerimentoVO.getTurmaReposicao().getCodigo())) {
				getFacadeFactory().getTurmaFacade().carregarDados(requerimentoVO.getTurmaReposicao(), usuarioVO);
				if (requerimentoVO.getTurmaReposicao().getTurmaAgrupada()) {
					Map<Integer, List<TurmaVO>> mapTurmas = new HashMap<Integer, List<TurmaVO>>();
					List<TurmaVO> turmaVOs = new ArrayList<TurmaVO>();
					definirTurmaBaseRequerimentosTurmaAgrupada(requerimentoVO, turmaVOs, usuarioVO, mapTurmas, requerimentoVO.getTurmaReposicao());
					alterar(requerimentoVO, usuarioVO, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuarioVO));
				}
			}
			try {
				if(requerimentoVO.getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.DEFERIDO)) {					
					requerimentoVO.setTipoDesconto(TipoDescontoAluno.PORCENTO.getValor());
					requerimentoVO.setPercDesconto(100.0);
					requerimentoVO.getCalcularValorDesconto();
					requerimentoVO.setSituacao("PE");
					requerimentoVO.setSituacaoFinanceira("IS");
				}
				final String sql = "UPDATE Requerimento set situacaoIsencaoTaxa=?,  motivoDeferimentoIndeferimentoIsencaoTaxa=?, responsavelDeferimentoIndeferimentoIsencaoTaxa=?, dataDeferimentoIndeferimentoIsencaoTaxa = ?, tipodesconto = ?, percdesconto = ?, valordesconto = ?, contareceber = ?, situacaoFinanceira = ?  WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
				getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement sqlAlterar = con.prepareStatement(sql);
						sqlAlterar.setString(1, requerimentoVO.getSituacaoIsencaoTaxa().name());
						sqlAlterar.setString(2, requerimentoVO.getMotivoDeferimentoIndeferimentoIsencaoTaxa());						
						sqlAlterar.setInt(3, usuarioVO.getCodigo());
						sqlAlterar.setTimestamp(4, Uteis.getDataJDBCTimestamp(requerimentoVO.getDataDeferimentoIndeferimentoIsencaoTaxa()));
						sqlAlterar.setString(5, requerimentoVO.getTipoDesconto());
						sqlAlterar.setDouble(6, requerimentoVO.getPercDesconto());
						sqlAlterar.setDouble(7, requerimentoVO.getValorDesconto());
						if(requerimentoVO.getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.DEFERIDO)) {
							sqlAlterar.setNull(8, 0);
						}else {
							sqlAlterar.setInt(8, requerimentoVO.getContaReceber());
						}						
						sqlAlterar.setString(9, requerimentoVO.getSituacaoFinanceira());
						sqlAlterar.setInt(10, requerimentoVO.getCodigo());
						return sqlAlterar;
					}
				});
				if(requerimentoVO.getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.DEFERIDO)) {
					alterarSituacaoFinanceiraESituacaoExecucao(requerimentoVO.getCodigo(), false, situacao.equals(SituacaoRequerimento.AGUARDANDO_PAGAMENTO.getValor()) ? "PE" : situacao, "IS", false, usuarioVO);
					if(Uteis.isAtributoPreenchido(requerimentoVO.getContaReceber())) {
//						excluirContaReceber(requerimentoVO, usuarioVO);
						requerimentoVO.setContaReceber(null);
					}
					if(requerimentoVO.getTipoRequerimento().getDeferirAutomaticamente()) {
						requerimentoVO.setSituacao("FD");
					}else {
						requerimentoVO.setSituacao("PE");
					}
					requerimentoVO.setSituacaoFinanceira("IS");
				}
				TemplateMensagemAutomaticaEnum  templateMensagemAutomaticaEnum = null;
				if(requerimentoVO.getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.DEFERIDO)) {
					if(requerimentoVO.getTipoRequerimento().getIsTipoReposicao()) {
						templateMensagemAutomaticaEnum = TemplateMensagemAutomaticaEnum.MENSAGEM_REQUERIMENTO_REPOSICAO_SOLICITACAO_ISENCAO_TAXA_DEFERIDO;
					}else {
						templateMensagemAutomaticaEnum = TemplateMensagemAutomaticaEnum.MENSAGEM_REQUERIMENTO_SOLICITACAO_ISENCAO_TAXA_DEFERIDO;
					}
				}else if(requerimentoVO.getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.INDEFERIDO)){
					if(requerimentoVO.getTipoRequerimento().getIsTipoReposicao()) {
						templateMensagemAutomaticaEnum = TemplateMensagemAutomaticaEnum.MENSAGEM_REQUERIMENTO_REPOSICAO_SOLICITACAO_ISENCAO_TAXA_INDEFERIDO;
					}else {
						templateMensagemAutomaticaEnum = TemplateMensagemAutomaticaEnum.MENSAGEM_REQUERIMENTO_SOLICITACAO_ISENCAO_TAXA_INDEFERIDO;
					}
				}
				if(templateMensagemAutomaticaEnum != null) {
					Thread notificar = new Thread(new NotificacaoDeferimentoIndeferimentoSolicitacaIsencaoTaxa(templateMensagemAutomaticaEnum, requerimentoVO, usuarioVO));
					notificar.start();
				}
			} catch (Exception e) {		
				if(requerimentoVO.getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.DEFERIDO)) {	
					requerimentoVO.setTipoDesconto(tipoDesconto);
					requerimentoVO.setPercDesconto(perDesconto);
					requerimentoVO.setValorDesconto(valorDesconto);
				}
				requerimentoVO.setSituacao(situacao);
				requerimentoVO.setSituacaoFinanceira(situacaoFinan);
				throw e;
			}
		}
	}
	
	@Override
	public void validarDadosSolicitacaoIsencaoTaxa(RequerimentoVO requerimentoVO) throws ConsistirException {
		requerimentoVO.setSituacaoIsencaoTaxa(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.NAO_REQUERIDA);
		if(!requerimentoVO.getTipoRequerimento().getPermitirSolicitacaoIsencaoTaxaRequerimento()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_Requerimento_solicitacaoIsencaoNaoPermitida"));
		}
		if(!Uteis.isAtributoPreenchido(requerimentoVO.getJustificativaSolicitacaoIsencao())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_Requerimento_justificativaSolicitacaoIsencao"));
		}
		if(requerimentoVO.getJustificativaSolicitacaoIsencao().trim().length() < 15) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_Requerimento_justificativaSolicitacaoIsencaoPequeno"));
		}
		if(requerimentoVO.getTipoRequerimento().getSolicitarAnexoComprovanteIsencaoTaxaRequerimento()) {
			if(!Uteis.isAtributoPreenchido(requerimentoVO.getComprovanteSolicitacaoIsencao().getNome())
					|| !Uteis.isAtributoPreenchido(requerimentoVO.getComprovanteSolicitacaoIsencao().getPastaBaseArquivoEnum())) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_Requerimento_comprovanteSolicitacaoIsencao"));
			}
					
		}
		requerimentoVO.setSituacaoIsencaoTaxa(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.AGUARDANDO_RESPOSTA);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarInclusaoArquivoComprovanteSolicitacaoIsencaoTaxa(RequerimentoVO requerimentoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioVO) throws Exception{
		if (!requerimentoVO.getComprovanteSolicitacaoIsencao().getNome().equals("")) {
			requerimentoVO.getComprovanteSolicitacaoIsencao().setOrigem(OrigemArquivo.REQUERIMENTO.getValor());
			requerimentoVO.getComprovanteSolicitacaoIsencao().setCodOrigem(requerimentoVO.getCodigo());
			if (!requerimentoVO.getComprovanteSolicitacaoIsencao().getCodigo().equals(0)) {
				getFacadeFactory().getArquivoFacade().alterar(requerimentoVO.getArquivoVO(), usuarioVO, configuracaoGeralSistema);
			} else {
				getFacadeFactory().getArquivoFacade().incluir(requerimentoVO.getComprovanteSolicitacaoIsencao(), usuarioVO, configuracaoGeralSistema);				
			}
		}		
	}
	
	@Override
	public void realizarValidacaoChoqueHorarioEVagaTurmaRequerimentoReposicao(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) throws Exception{
		Integer disciplina = requerimentoVO.getDisciplina().getCodigo();
		requerimentoVO.setMensagemChoqueHorario("");
		MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = null;
		try {
//			matriculaPeriodoTurmaDisciplinaVO = getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().realizarPreenchimentoDadosMatriculaPeriodoTurmaDisciplinaAdicionarPorRequerimento(requerimentoVO, true, true,  usuarioVO);
			if (requerimentoVO.getDisciplinaPorEquivalencia()) {
				disciplina = requerimentoVO.getMapaEquivalenciaDisciplinaCursadaVO().getDisciplinaVO().getCodigo();
			}
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().atualizarNrAlunosMatriculadosTurmaDisciplina(requerimentoVO.getMatriculaPeriodoVO(), matriculaPeriodoTurmaDisciplinaVO, matriculaPeriodoTurmaDisciplinaVO.getDisciplina(), matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre(), true, true);
			Integer qtde = consultarQtdeRequerimentoReposicaoNaoFinalizadosNaoVencidosParaContablizacaoVaga(requerimentoVO.getTurmaReposicao(), disciplina, requerimentoVO.getMatriculaPeriodoVO().getAno(), requerimentoVO.getMatriculaPeriodoVO().getSemestre());
			matriculaPeriodoTurmaDisciplinaVO.setNrVagasDisponiveis(matriculaPeriodoTurmaDisciplinaVO.getNrVagasDisponiveis() - qtde);
			if (matriculaPeriodoTurmaDisciplinaVO.getNrVagasDisponiveis() <= 0) {
				throw new Exception("Não Existem Disponibilidade de Vaga Para a Turma Selecionada.");
			}
//			requerimentoVO.setSalaLocalAulaVO(requerimentoVO.getTurmaReposicao().getSalaLocalAulaVO());
			requerimentoVO.setDataInicioAula(requerimentoVO.getTurmaReposicao().getDataPrimeiraAula());
			requerimentoVO.setDataTerminoAula(requerimentoVO.getTurmaReposicao().getDataUltimaAula());
		} catch (Exception e) {
			throw e;
		} finally {
			matriculaPeriodoTurmaDisciplinaVO = null;
			disciplina = null;
		}
	}
	
	public Integer consultarQtdeRequerimentoReposicaoNaoFinalizadosNaoVencidosParaContablizacaoVaga(TurmaVO turmaVO, Integer disciplina, String ano, String semestre) throws Exception {		
		SqlRowSet rs  = getConexao().getJdbcTemplate().queryForRowSet(getSqlQtdeRequerimentoReposicaoNaoFinalizadosNaoVencidosParaContablizacaoVaga("", turmaVO.getCodigo(), disciplina, ano, semestre).toString());
		if(rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}
	
	@Override
	public StringBuilder getSqlQtdeRequerimentoReposicaoNaoFinalizadosNaoVencidosParaContablizacaoVaga(String sqlCampoTurma, Integer turmaEspecifica, Integer disciplina, String ano, String semestre) {
		StringBuilder sql  = new StringBuilder("(select count(requerimento.codigo) as qtde from requerimento ");
		sql.append(" inner join tiporequerimento on  tiporequerimento.codigo = requerimento.tiporequerimento ");
		sql.append(" inner join turma as turmareposicao on  turmareposicao.codigo = requerimento.turmareposicao ");
		sql.append(" inner join matricula mat on  mat.matricula = requerimento.matricula ");
		sql.append(" inner JOIN MatriculaPeriodo matper on matper.matricula = requerimento.matricula ");
		sql.append(" and case when Requerimento.matriculaperiodo is not null then  Requerimento.matriculaperiodo = matper.codigo else ");
		sql.append(" case when requerimento.codigo != 0 then matper.turma = requerimento.turma else ");
		sql.append(" matper.codigo = (select codigo from matriculaperiodo mp where mp.matricula = requerimento.matricula order by (mp.ano||'/'|| mp.semestre) desc, mp.periodoletivomatricula desc limit 1) end end ");		
		sql.append(" left join contareceber on  contareceber.codigo = requerimento.contareceber ");
		sql.append(" left join mapaEquivalenciaDisciplinaCursada on  mapaEquivalenciaDisciplinaCursada.codigo = requerimento.mapaEquivalenciaDisciplinaCursada and requerimento.disciplinaPorEquivalencia ");		
		if(Uteis.isAtributoPreenchido(turmaEspecifica)) {
			sql.append(" where requerimento.turmareposicao = ").append(turmaEspecifica);
		}else {
			sql.append(" where requerimento.turmareposicao = ").append(sqlCampoTurma);
		}
		sql.append(" and (requerimento.disciplina = ").append(disciplina);
		sql.append(" or mapaEquivalenciaDisciplinaCursada.disciplina = ").append(disciplina).append(") ");
		sql.append(" and tiporequerimento.tipo = '").append(TiposRequerimento.REPOSICAO.getValor()).append("' ");
		sql.append(" and mat.situacao = 'AT' and matper.situacaomatriculaperiodo in ('AT', 'PR') ");
		sql.append(" and requerimento.situacao not in ('FD', 'FI')  ");
		sql.append(" and (requerimento.situacaofinanceira = 'IS' or contareceber.codigo is null or contareceber.datavencimento <= current_date) ");
		sql.append(" and ((turmareposicao.semestral and ");
		sql.append(" matper.ano = '").append(ano).append("' ");
		sql.append(" and matper.semestre = '").append(semestre).append("') ");
		sql.append(" or (turmareposicao.anual ");
		sql.append(" and matper.ano = '").append(ano).append("') ");
		sql.append(" or ( turmareposicao.anual = false and turmareposicao.semestral = false))) ");
		return sql;
	}
	
//	public static void montarDadosSalaLocalAula(RequerimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception{
//		if(Uteis.isAtributoPreenchido(obj.getSalaLocalAulaVO())) {
//			obj.setSalaLocalAulaVO(getFacadeFactory().getSalaLocalAulaFacade().consultarPorChavePrimaria(obj.getSalaLocalAulaVO().getCodigo()));
//		}
//	}
	public  void montarDadosMapaEquivalenciaDisciplina(RequerimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception{
		if(Uteis.isAtributoPreenchido(obj.getMapaEquivalenciaDisciplinaVO())) {
			obj.setMapaEquivalenciaDisciplinaVO(getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarPorChavePrimaria(obj.getMapaEquivalenciaDisciplinaVO().getCodigo(), NivelMontarDados.BASICO));
		}
	}
	public  void montarDadosMapaEquivalenciaDisciplinaCursada(RequerimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception{
		if(Uteis.isAtributoPreenchido(obj.getMapaEquivalenciaDisciplinaCursadaVO())) {
			obj.setMapaEquivalenciaDisciplinaCursadaVO(getFacadeFactory().getMapaEquivalenciaDisciplinaCursadaFacade().consultarPorChavePrimaria(obj.getMapaEquivalenciaDisciplinaCursadaVO().getCodigo()));
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)	
	private void alterarSituacaoFinanceira(Integer codigo, String situacaoFinanceira, UsuarioVO usuarioVO) throws Exception {
		final String sql = "UPDATE Requerimento set situacaoFinanceira=? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setString(1, situacaoFinanceira);
				sqlAlterar.setInt(2, codigo.intValue());
				return sqlAlterar;
			}
		});
	}
	
	
	class NotificacaoDeferimentoIndeferimentoSolicitacaIsencaoTaxa implements Runnable{
		
		private RequerimentoVO requerimentoVO;
		private UsuarioVO usuarioVO;
		private TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum; 
		
		
		
		public NotificacaoDeferimentoIndeferimentoSolicitacaIsencaoTaxa(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) {
			super();
			this.requerimentoVO = requerimentoVO;
			this.usuarioVO = usuarioVO;
			this.templateMensagemAutomaticaEnum = templateMensagemAutomaticaEnum;
		}

		public void run() {
			try {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemRequerimentoSolicitacaoIsencaoTaxaDeferido(templateMensagemAutomaticaEnum, requerimentoVO, usuarioVO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
				
	}
	
//	public void reativarContaReceberRequerimento(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) throws Exception {
//		if (Uteis.isAtributoPreenchido(requerimentoVO) && Uteis.isAtributoPreenchido(requerimentoVO.getContaReceberVO()) && requerimentoVO.getContaReceberVO().getSituacao().equals(SituacaoContaReceber.CANCELADO_FINANCEIRO.getValor())) {
//			alterarSituacaoFinanceira(requerimentoVO.getCodigo(), "PE", usuarioVO);
//			getFacadeFactory().getContaReceberFacade().reativarContaReceberCancelada(requerimentoVO.getContaReceberVO(), usuarioVO);
//		}
//	}
	
	public void realizarValidacaoAlunosComTodasAulasRegistradas(RequerimentoVO requerimentoVO, UsuarioVO usuario) throws Exception {
		
		if(Uteis.isAtributoPreenchido(requerimentoVO.getMatricula().getMatricula())) {
		
			List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs;	
						
			matriculaPeriodoTurmaDisciplinaVOs = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarRapidaPorMatriculaTurmaDisciplinaAnoSemestreGradeCurricularAtual(requerimentoVO.getMatricula().getMatricula(), 0, 0, requerimentoVO.getMatriculaPeriodoVO().getAno(), requerimentoVO.getMatriculaPeriodoVO().getSemestre(), false, true, true, true, false, "'PC'", "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		
//			getFacadeFactory().getHorarioAlunoFacade().consultarMeusHorariosAluno(matriculaPeriodoTurmaDisciplinaVOs, new Date(), null, true, requerimentoVO.getMatricula().getUnidadeEnsino(), usuario, true, requerimentoVO.getMatricula().getUnidadeEnsino().getCodigo());
			getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().consultarPorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVOs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario, true);
		}
	}
	
	public void realizarValidacaoVerificarAlunoPossuiPeloMenosUmaDisciplinaAprovada(RequerimentoVO requerimentoVO, UsuarioVO usuario) throws Exception {
		if(Uteis.isAtributoPreenchido(requerimentoVO.getMatricula().getMatricula())) {
			List<HistoricoVO> listaHistorico = new ArrayList<HistoricoVO>(0);
			listaHistorico = getFacadeFactory().getHistoricoFacade().consultarDisciplinaCursadasAlunoPorMatricula(requerimentoVO.getMatricula().getMatricula(), requerimentoVO.getMatricula().getGradeCurricularAtual().getCodigo(), null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario, 1, false);
			if(listaHistorico.isEmpty()) {
				throw new Exception("O Aluno precisa ter ao menos uma disciplina aprovada.");
			}
		}
		
	}
	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarFuncionarioRequerimentoUnificacaoFuncionario(Integer funcionarioAntigo, Integer funcionarioNovo) throws Exception {
		String sqlStr = "UPDATE Requerimento set funcionario=? WHERE ((funcionario = ?))";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { funcionarioNovo, funcionarioAntigo });
	}
	
	@Override
	public void validarDadosBloqueioRequerimentoAbertoSimultaneo(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) throws Exception {

		if (requerimentoVO.getTipoRequerimento().getBloquearQuantidadeRequerimentoAbertosSimultaneamente()) {
			if(requerimentoVO.getTipoRequerimento().getIsPermiteInformarDisciplina() && requerimentoVO.getTipoRequerimento().getTipo().equals(TiposRequerimento.SEGUNDA_CHAMADA.getValor())){
				for(RequerimentoDisciplinaVO requerimentoDisciplinaVO: requerimentoVO.getRequerimentoDisciplinaVOs()) {
					validarDadosBloqueioRequerimentoAbertoSimultaneo(requerimentoVO, requerimentoDisciplinaVO.getDisciplina().getCodigo(), usuarioVO);
				}
			}else if(requerimentoVO.getTipoRequerimento().getIsPermiteInformarDisciplina() && !requerimentoVO.getTipoRequerimento().getTipo().equals(TiposRequerimento.SEGUNDA_CHAMADA.getValor())){ 
				validarDadosBloqueioRequerimentoAbertoSimultaneo(requerimentoVO, requerimentoVO.getDisciplina().getCodigo(), usuarioVO);
			}else {
				validarDadosBloqueioRequerimentoAbertoSimultaneo(requerimentoVO, 0, usuarioVO);
			}
			
		}
	}
	
	
	public void validarDadosBloqueioRequerimentoAbertoSimultaneo(RequerimentoVO requerimentoVO, Integer disciplina, UsuarioVO usuarioVO) throws Exception {

		if (requerimentoVO.getTipoRequerimento().getBloquearQuantidadeRequerimentoAbertosSimultaneamente()) {			
			Integer quantidadeLimiteRequerimentoAbertoSimultaneamente = requerimentoVO.getTipoRequerimento().getQuantidadeLimiteRequerimentoAbertoSimultaneamente();
			Integer quantidadeRequerimentoAberto = consultarQuantidadeRequerimentoAbertoSimultaneo(requerimentoVO, requerimentoVO.getPessoa(), requerimentoVO.getMatricula().getMatricula(), requerimentoVO.getTipoPessoa(), requerimentoVO.getTipoRequerimento().getConsiderarBloqueioSimultaneoRequerimentoDeferido(), requerimentoVO.getTipoRequerimento().getConsiderarBloqueioSimultaneoRequerimentoIndeferido(), requerimentoVO.getTipoRequerimento().getTipo(), requerimentoVO.getTipoRequerimento().getCodigo(), disciplina, usuarioVO);
			
			if (quantidadeRequerimentoAberto >= quantidadeLimiteRequerimentoAbertoSimultaneamente) {
				if (usuarioVO.getIsApresentarVisaoAluno()) {
					if (!disciplina.equals(0)) {
						if (requerimentoVO.getDisciplina().getNome().equals("")) {
							requerimentoVO.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(disciplina, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
						}
						throw new Exception("A Quantidade de Requerimentos abertos simultâneos do Tipo ("+requerimentoVO.getTipoRequerimento().getNome().toUpperCase()+") para a disciplina ("+requerimentoVO.getDisciplina().getNome().toUpperCase()+") ultrapassou o limite permitido. Quantidade de Requerimentos em Aberto: ("+quantidadeRequerimentoAberto+") - Quantidade de Requerimentos configurados permitidos: ("+quantidadeLimiteRequerimentoAbertoSimultaneamente+"). ");
					}
					throw new Exception("A Quantidade de Requerimentos abertos simultâneos ultrapassou o limite permitido. Quantidade de Requerimentos em Aberto: ("+quantidadeRequerimentoAberto+") - Quantidade de Requerimentos configurados permitidos: ("+quantidadeLimiteRequerimentoAbertoSimultaneamente+"). ");
				}
				if (!disciplina.equals(0)) {
					if (requerimentoVO.getDisciplina().getNome().equals("")) {
						requerimentoVO.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(disciplina, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
					}
					throw new Exception("A Quantidade de Requerimentos abertos simultâneos ultrapassou o limite permitido configurado no Tipo de Requerimento "+requerimentoVO.getTipoRequerimento().getNome().toUpperCase()+" para a disciplina ("+requerimentoVO.getDisciplina().getNome().toUpperCase()+"). Quantidade de Requerimentos em Aberto: ("+quantidadeRequerimentoAberto+") - Quantidade de Requerimentos configurados permitidos: ("+quantidadeLimiteRequerimentoAbertoSimultaneamente+"). ");
				}
				throw new Exception("A Quantidade de Requerimentos abertos simultâneos ultrapassou o limite permitido configurado no Tipo de Requerimento "+requerimentoVO.getTipoRequerimento().getNome().toUpperCase()+". Quantidade de Requerimentos em Aberto: ("+quantidadeRequerimentoAberto+") - Quantidade de Requerimentos configurados permitidos: ("+quantidadeLimiteRequerimentoAbertoSimultaneamente+"). ");
			}
		}
	}
	
	public Integer consultarQuantidadeRequerimentoAbertoSimultaneo(RequerimentoVO requerimentoVO, PessoaVO pessoaVO, String matricula, TipoPessoa tipoPessoa, Boolean considerarBloqueioSimultaneoRequerimentoDeferido, Boolean considerarBloqueioSimultaneoRequerimentoIndeferido, String tipo, Integer codigoTipo, Integer disciplina, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select count(distinct requerimento.codigo) AS quantidade from requerimento ");
		sb.append(" inner join tiporequerimento on tiporequerimento.codigo = requerimento.tiporequerimento ");
		if (TipoPessoa.ALUNO.equals(tipoPessoa)) {
			sb.append(" inner join matricula on matricula.matricula = requerimento.matricula ");
			sb.append(" inner join curso on curso.codigo = matricula.curso ");
			sb.append(" inner join matriculaperiodo on matriculaperiodo.codigo = requerimento.matriculaperiodo ");
			
			sb.append(" LEFT join matriculaperiodo as matriculaPeriodoAtual on matriculaPeriodoAtual.matricula = matricula.matricula ");
			sb.append(" and   matriculaPeriodoAtual.codigo = (");
			sb.append(" select codigo from matriculaperiodo  as mp where mp.matricula = matricula.matricula ");
			sb.append(" and mp.situacaomatriculaperiodo <> 'PC' ");
			sb.append(" and case when curso.periodicidade in('SE', 'AN') then mp.ano = '").append(Uteis.getAnoDataAtual4Digitos()).append("' else true end ");
			sb.append(" and case when curso.periodicidade in('SE') then mp.semestre = '").append(Uteis.getSemestreAtual()).append("' else true end ");
			sb.append(" order by (mp.ano || semestre) desc, codigo desc limit 1) ");
			
			if (requerimentoVO.getTipoRequerimento().getConsiderarTodasMatriculasAlunoValidacaoAberturaSimultanea()) {
				sb.append(" where requerimento.pessoa = '").append(pessoaVO.getCodigo()).append("' ");
			}else{
				sb.append(" where requerimento.matricula = '").append(matricula).append("' ");
			}
			
			sb.append(" and case when curso.periodicidade in('SE', 'AN') and matriculaPeriodoAtual.codigo is not null then matriculaperiodo.ano = '").append(Uteis.getAnoDataAtual4Digitos()).append("' ");
			sb.append(" else ");
			sb.append(" case when curso.periodicidade in('SE', 'AN') and matriculaPeriodoAtual.codigo is null     then matriculaperiodo.ano = (");
			sb.append(" select mp.ano from matriculaperiodo  as mp where mp.matricula = matricula.matricula ");
			sb.append(" and mp.situacaomatriculaperiodo <> 'PC' order by (mp.ano || semestre) desc, mp.codigo desc limit 1) ");
			sb.append(" else true end end ");
			if (!requerimentoVO.getTipoRequerimento().getConsiderarTodasMatriculasAlunoValidacaoAberturaSimultanea()) {
				sb.append(" and case when curso.periodicidade in('SE') and matriculaPeriodoAtual.codigo is not null then matriculaperiodo.semestre = '").append(Uteis.getSemestreAtual()).append("' ");
				sb.append(" else case when curso.periodicidade in('SE') and matriculaPeriodoAtual.codigo is null     then matriculaperiodo.semestre = (");
				sb.append(" select mp.semestre from matriculaperiodo  as mp where mp.matricula = matricula.matricula ");
				sb.append(" order by (mp.ano || semestre) desc, mp.codigo desc limit 1) ");
				sb.append(" else true end end ");
			}
		} else {
			sb.append(" where requerimento.pessoa = ").append(pessoaVO.getCodigo());
		}
		if (requerimentoVO.getTipoRequerimento().getBloqueioSimultaneoPelo().equals("TIPO")) {
			if (tipo.equals(TiposRequerimento.CERTIFICADO_PARTICIPACAO_TCC.getValor())) {
				sb.append(" and requerimento.grupofacilitador = '").append(requerimentoVO.getGrupoFacilitador().getCodigo()).append("' ");
			} else {
				sb.append(" and tiporequerimento.tipo = '").append(tipo).append("' ");
			}
		} else {
			sb.append(" and tiporequerimento.codigo = '").append(codigoTipo).append("' ");
		}
		if (!requerimentoVO.getCodigo().equals(0)) {
			sb.append(" and requerimento.codigo <> ").append(requerimentoVO.getCodigo());
		}
		if (!considerarBloqueioSimultaneoRequerimentoDeferido) {
			sb.append(" and requerimento.situacao <> 'FD' ");
		}
		if (!considerarBloqueioSimultaneoRequerimentoIndeferido) {
			sb.append(" and requerimento.situacao <> 'FI' ");
		}
		if (!disciplina.equals(0)) {
			sb.append(" and ((tiporequerimento.tipo != 'SEGUNDA_CHAMADA' and requerimento.disciplina = ").append(disciplina).append(") ");
			sb.append(" or (tiporequerimento.tipo = 'SEGUNDA_CHAMADA' and exists (select requerimentodisciplina.codigo from requerimentodisciplina where requerimento.codigo = requerimentodisciplina.requerimento and requerimentodisciplina.disciplina = ").append(disciplina).append(" limit 1))) ");
			sb.append(" group by tiporequerimento.tipo, requerimento.disciplina ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("quantidade");
		}
		return 0;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluirVinculoRequerimentoNovo(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) {
		alterar(requerimentoVO, "requerimento",
				new AtributoPersistencia().add("codigoNovoRequerimento", requerimentoVO.getCodigo()),
				new AtributoPersistencia().add("codigo", requerimentoVO.getRequerimentoAntigo().getCodigo()), usuarioVO);
	}
	
	@Override
	public Integer consultarTotalInteracaoNaoLida(Integer idPessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" SELECT count (interacaoRequerimentoHistorico.codigo) as totalNaoLida ");
		sqlStr.append("FROM interacaoRequerimentoHistorico ");
		sqlStr.append(" inner join requerimentohistorico on requerimentohistorico.codigo = interacaoRequerimentoHistorico.requerimentohistorico and interacaoRequerimentoHistorico.interacaojalida = false");
		sqlStr.append(" inner join requerimento on requerimento.codigo = requerimentohistorico.requerimento ");
		sqlStr.append(" inner join usuario on usuario.codigo = interacaoRequerimentoHistorico.usuarioInteracao  ");
		sqlStr.append(" where  requerimento.pessoa = ").append(idPessoa);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("totalNaoLida");
		} else {
			return 0;
		}
	}

	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataPrevistaFinalizacaoRequerimento(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) throws Exception {
		alterar(requerimentoVO, "requerimento",
				new AtributoPersistencia().add("dataPrevistaFinalizacao", requerimentoVO.getDataPrevistaFinalizacao()),
				new AtributoPersistencia().add("codigo", requerimentoVO.getCodigo()), usuarioVO);
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarIndeferirRequerimento(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		requerimentoVO.setSituacao(SituacaoRequerimento.FINALIZADO_INDEFERIDO.getValor());
		requerimentoVO.setDataFinalizacao(new Date());
		getFacadeFactory().getRequerimentoFacade().alterarSituacao(requerimentoVO.getCodigo(), SituacaoRequerimento.FINALIZADO_INDEFERIDO.getValor(), requerimentoVO.getMotivoIndeferimento(), "", requerimentoVO, usuarioVO);
		realizarEnvioComunicadoInternoIndeferimentoRequerimento(requerimentoVO, usuarioVO, configuracaoGeralSistemaVO);
	}
	
	private void realizarEnvioComunicadoInternoIndeferimentoRequerimento(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		if (usuarioVO.getPessoa() == null || usuarioVO.getPessoa().getCodigo().equals(0)) {
			throw new Exception("Este usuáro não pode enviar Comunicação Interna, pois não possui nenhuma pessoa vinculada a ele.");
		}
		ComunicacaoInternaVO co = new ComunicacaoInternaVO();
		co.setResponsavel(usuarioVO.getPessoa());
		co.setEnviarEmail(true);
		co.setTipoDestinatario("AL");
		co.setTipoMarketing(false);
		co.setTipoLeituraObrigatoria(false);
		co.setDigitarMensagem(true);
		co.setRemoverCaixaSaida(false);
		co.setAssunto("Seu Requerimento foi Indeferido!");
		co.setMensagem(co.getMensagemComLayout("Requerimento: " + requerimentoVO.getCodigo() + ". Motivo: " + requerimentoVO.getMotivoIndeferimento()));
		getFacadeFactory().getPessoaFacade().carregarDados(requerimentoVO.getMatricula().getAluno(), usuarioVO);
		co.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorMatricula(requerimentoVO.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		co.adicionarObjComunicadoInternoDestinatarioVOs(realizarAdicaoDestinatario(requerimentoVO.getMatricula().getAluno()));
		getFacadeFactory().getComunicacaoInternaFacade().incluir(co, true, usuarioVO, configuracaoGeralSistemaVO,null);
	}

	private ComunicadoInternoDestinatarioVO realizarAdicaoDestinatario(PessoaVO obj) {
		ComunicadoInternoDestinatarioVO co = new ComunicadoInternoDestinatarioVO();
		co.setDestinatario(obj);
		co.setTipoComunicadoInterno("LE");
		co.setDataLeitura(null);
		co.setCiJaRespondida(false);
		co.setCiJaLida(false);
		co.setRemoverCaixaEntrada(false);
		co.setMensagemMarketingLida(false);
		return co;
	}
	
	public void atualizarValorRequerimento(RequerimentoVO requerimentoVO, Boolean forcarDefinicaoData, UsuarioVO usuarioVO) throws Exception {
		Integer campoConsulta = requerimentoVO.getTipoRequerimento().getCodigo();
		if (campoConsulta != null && campoConsulta > 0) {
			TipoRequerimentoVO tipoRe = getFacadeFactory().getTipoRequerimentoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			
			requerimentoVO = getFacadeFactory().getRequerimentoFacade().inicializarRequerimentoConformeTipoRequerimento(tipoRe, requerimentoVO, forcarDefinicaoData);
			getFacadeFactory().getRequerimentoFacade().realizarValidacaoRegrasCriacaoRequerimento(requerimentoVO, usuarioVO);
			if (TiposRequerimento.TRANSF_ENTRADA.getValor().equals(requerimentoVO.getTipoRequerimento().getTipo()) || requerimentoVO.getTipoRequerimento().getRequerimentoVisaoCoordenador() || requerimentoVO.getTipoRequerimento().getRequerimentoVisaoProfessor() || requerimentoVO.getTipoRequerimento().getRequerimentoVisaoFuncionario() || requerimentoVO.getTipoRequerimento().getRequerimentoMembroComunidade()) {
				requerimentoVO.setSomenteAluno(false);
			} else {
				requerimentoVO.setTipoPessoa(TipoPessoa.ALUNO);
				requerimentoVO.setSomenteAluno(true);
			}
			
			if (tipoRe.getValor().equals(0.0) || (!tipoRe.getValor().equals(0.0) && (tipoRe.getIsEmissaoCertificado() || tipoRe.getIsDeclaracao()))) {
				requerimentoVO.setExigePagamento(Boolean.FALSE);
			} else {
				requerimentoVO.setExigePagamento(Boolean.TRUE);
			}
			if (requerimentoVO.getSomenteAluno() && requerimentoVO.getMatricula().getMatricula().trim().isEmpty()) {
				requerimentoVO.setPessoa(null);
			}
		} else {
			requerimentoVO.setValor(0.0);
//			requerimentoVO.setTaxa(null);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String realizarImpressaoComprovanteRequerimento(RequerimentoVO requerimentoVO, SuperParametroRelVO superParametroRelVO, UsuarioVO usuarioVO, String tipoLayout) throws Exception {
//		List<RequerimentoVO> listaRequerimentoRelVO = new ArrayList<RequerimentoVO>(0);
//		GeradorRelatorio geradorRelatorio = new GeradorRelatorio();
//		listaRequerimentoRelVO.add(requerimentoVO);
//		superParametroRelVO.setNomeDesignIreport(RequerimentoRel.getDesignIReportRelatorioAnalitico(tipoLayout.equals("LAYOUT_1") ? "RequerimentoAnalitico2Rel" : tipoLayout.equals("LAYOUT_3") ? "RequerimentoAnalitico4Rel": "RequerimentoAnaliticoRel"));
//		superParametroRelVO.setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
//		superParametroRelVO.setSubReport_Dir(RequerimentoRel.caminhoBaseRelatorio());
//		superParametroRelVO.setNomeUsuario(usuarioVO.getNome());
//		superParametroRelVO.setTituloRelatorio("Requerimento");
//		superParametroRelVO.setListaObjetos(listaRequerimentoRelVO);
//		superParametroRelVO.setCaminhoBaseRelatorio(RequerimentoRel.caminhoBaseRelatorio());
//		superParametroRelVO.setUnidadeEnsino(requerimentoVO.getUnidadeEnsino().getNome());
//		superParametroRelVO.setQuantidade(1);
//		superParametroRelVO.adicionarParametro("exigePagamento", requerimentoVO.getExigePagamento());
//		superParametroRelVO.adicionarParametro("enderecoCompletoUnidade", getFacadeFactory().getRequerimentoFacade().montaEnderecoRelatorioRequerimento(requerimentoVO.getCodigo(), false, usuarioVO));
//		return geradorRelatorio.realizarExportacaoRelatorio(superParametroRelVO);
		return null;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String realizarImpressaoDeclaracaoRequerimento(RequerimentoVO requerimentoVO, ImpressaoContratoVO impressaoContratoFiltro, ImpressaoContratoVO contratoGravar, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, TextoPadraoDeclaracaoVO texto, Boolean persistirDocumentoAssinado, UsuarioVO usuarioVO) throws Exception {
		String caminhoArquivo = "";
		impressaoContratoFiltro.setMatriculaVO(requerimentoVO.getMatricula());
		impressaoContratoFiltro.setRequerimentoVO(requerimentoVO);
		impressaoContratoFiltro.getRequerimentoVO().getArquivoVO().setCodigo(contratoGravar.getDocumentoAssinado().getCodigo());
		impressaoContratoFiltro.setImpressaoRequerimento(true);
		impressaoContratoFiltro.setMatriculaPeriodoVO(requerimentoVO.getMatricula().getUltimoMatriculaPeriodoVO());
		if (impressaoContratoFiltro.getMatriculaPeriodoVO() == null || impressaoContratoFiltro.getMatriculaPeriodoVO().getCodigo() == 0) {
			throw new Exception("Não existe MATRÍCULA PERÍODO ativa");
		}
		if (requerimentoVO.getDisciplina() != null && requerimentoVO.getDisciplina().getCodigo() != 0) {
			impressaoContratoFiltro.setDisciplinaVO(requerimentoVO.getDisciplina());
		} else {
			impressaoContratoFiltro.setDisciplinaVO(new DisciplinaVO());
		}
		if (requerimentoVO.getTurma() != null && requerimentoVO.getTurma().getCodigo() != 0) {
			impressaoContratoFiltro.setTurmaVO(requerimentoVO.getTurma());
		} else {
			impressaoContratoFiltro.setTurmaVO(new TurmaVO());
		}
		
		if (texto.getTipoDesigneTextoEnum().isHtml()){
			impressaoContratoFiltro.setImpressaoPdf(false);
			contratoGravar.setImpressaoPdf(false);
		} else {
			impressaoContratoFiltro.setImpressaoPdf(true);
			contratoGravar.setImpressaoPdf(true);
		}
		if (Uteis.isAtributoPreenchido(requerimentoVO)) {
			if (requerimentoVO.getIsFormatoCertificadoSelecionadoImpresso()) {
				persistirDocumentoAssinado = false;
			} else {
				persistirDocumentoAssinado = true;
			}
		} else {
			if (Uteis.isAtributoPreenchido(requerimentoVO.getMatricula().getMatricula())) {
				persistirDocumentoAssinado = false;
			}
		}
//		caminhoArquivo = getFacadeFactory().getImpressaoDeclaracaoFacade().imprimirDeclaracao(texto.getCodigo(), impressaoContratoFiltro, contratoGravar, "AL", impressaoContratoFiltro.getTurmaVO(), impressaoContratoFiltro.getDisciplinaVO(), usuarioVO, persistirDocumentoAssinado);
		getFacadeFactory().getRequerimentoFacade().alterarDataUltimaImpressao(requerimentoVO.getCodigo());
		return caminhoArquivo;
	}
	
	public List<RequerimentoVO> consultarPorTipoRequerimentoAberto(RequerimentoVO requerimentoVO, UsuarioVO usuarioLogado) throws Exception{
		StringBuilder sqlStr = new StringBuilder("SELECT codigo, situacao, tiporequerimento FROM REQUERIMENTO ");
		sqlStr.append(" WHERE situacao <> 'FD' AND situacao <> 'FI' ");
		if(Uteis.isAtributoPreenchido(requerimentoVO.getTipoRequerimento().getCodigo())) {
			sqlStr.append(" AND tiporequerimento = ").append(requerimentoVO.getTipoRequerimento().getCodigo());
		}
		if(Uteis.isAtributoPreenchido(requerimentoVO.getDisciplina().getCodigo())) {
			sqlStr.append(" AND disciplina = ").append(requerimentoVO.getDisciplina().getCodigo());
		}
		if(Uteis.isAtributoPreenchido(requerimentoVO.getMatriculaPeriodoVO().getCodigo())) {
			sqlStr.append(" AND matriculaperiodo = ").append(requerimentoVO.getMatriculaPeriodoVO().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(requerimentoVO.getCodigo())) {
			sqlStr.append(" AND requerimento.codigo != ").append(requerimentoVO.getCodigo());
		}
		if(!requerimentoVO.getTipoPessoaAluno()) {
			sqlStr.append(" AND pessoa = ").append(requerimentoVO.getPessoa().getCodigo());
		}
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<RequerimentoVO> vetResultado = new ArrayList<RequerimentoVO>(0);
		while (tabelaResultado.next()) {
			RequerimentoVO requerimento = new RequerimentoVO();
			requerimento.setCodigo(tabelaResultado.getInt("codigo"));
			requerimento.getTipoRequerimento().setCodigo(tabelaResultado.getInt("tiporequerimento"));
			requerimento.setSituacao(SituacaoRequerimento.getDescricao(tabelaResultado.getString("situacao")));
			vetResultado.add(requerimento);
		}
		return vetResultado;
		
		
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarUnidadeEnsinoTurmaBaseMatriculaPeriodo (TurmaVO turmaVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		 final StringBuilder sqlStr = new StringBuilder();
		 sqlStr.append("UPDATE requerimento SET unidadeensino = t.unidadeensino  FROM ( ");
		 sqlStr.append("SELECT ");
		 sqlStr.append("  matricula.matricula,turma.unidadeensino ,requerimento.codigo as requerimento  ");
		 sqlStr.append("FROM matriculaperiodo ");
		 sqlStr.append(" INNER JOIN matricula        ON matriculaperiodo.matricula = matricula.matricula ");
		 sqlStr.append(" INNER JOIN requerimento     ON requerimento.matricula     = matricula.matricula ");
		 sqlStr.append(" INNER JOIN turma            ON matriculaperiodo.turma     = turma.codigo ");
		 sqlStr.append("WHERE turma.codigo = ? AND requerimento.unidadeensino IS NOT NULL ");
		 sqlStr.append(") AS t WHERE requerimento.codigo = t.requerimento AND requerimento.unidadeensino <> t.unidadeensino  ; ");
		 sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sqlStr.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(turmaVO.getCodigo(), ++i, sqlAlterar);
				return sqlAlterar;
			}
		});
	}
	
	@Override
    public void gravarAtualizacaoMatricula(RequerimentoVO requerimentoVO,UsuarioVO usuarioVO) throws Exception {
		MatriculaVO matricula = requerimentoVO.getMatricula();
		matricula.setSituacao("FO");
		matricula.setMsgErro("");
		matricula.setDataAtualizacaoMatriculaFormada(new Date());
		matricula.setResponsavelAtualizacaoMatriculaFormada(usuarioVO);
		getFacadeFactory().getMatriculaFacade().alterarSituacaoMatriculaFormadaAtualizacao(matricula, usuarioVO);
	}
	@Override
	public void montarListaSelectItemTurmaAdicionar(RequerimentoVO requerimentoVO, List<TurmaVO> listaTurmaIncluir, UsuarioVO usuarioVO, Map<Integer, List<TurmaVO>> mapTurmas) {
		listaTurmaIncluir.clear();
		requerimentoVO.setValorDesconto(0.0);
		requerimentoVO.setPercDesconto(0.0);
		requerimentoVO.setValorAdicional(0.0);
		if (requerimentoVO.getDisciplina().getCodigo().equals(0) || (requerimentoVO.getDisciplinaPorEquivalencia() 
				&& !Uteis.isAtributoPreenchido(requerimentoVO.getMapaEquivalenciaDisciplinaVO()))) {
			listaTurmaIncluir.clear();
			return;
		}
		Integer disciplinaConsultarTurma = requerimentoVO.getDisciplina().getCodigo();
		Integer cargaHorariaConsultarTurma = requerimentoVO.getCargaHorariaDisciplina();
		try {
			if (requerimentoVO.getDisciplinaPorEquivalencia()) {	
				disciplinaConsultarTurma = requerimentoVO.getMapaEquivalenciaDisciplinaVO().getMapaEquivalenciaDisciplinaCursadaVOs().get(0).getDisciplinaVO().getCodigo();
				cargaHorariaConsultarTurma = requerimentoVO.getMapaEquivalenciaDisciplinaVO().getMapaEquivalenciaDisciplinaCursadaVOs().get(0).getCargaHoraria();
			}

			List<TurmaVO> turmaVOs = getFacadeFactory().getTurmaFacade().consultaRapidaPorDisciplinaUnidadeEnsinoSituacao(disciplinaConsultarTurma, cargaHorariaConsultarTurma, requerimentoVO.getTipoRequerimento().getPermiteIncluirReposicaoTurmaOutraUnidade() ? 0 : requerimentoVO.getUnidadeEnsino().getCodigo(), "AB", true, requerimentoVO.getTipoRequerimento().getPermiteIncluirReposicaoTurmaOutroCurso() ? 0 : requerimentoVO.getMatricula().getCurso().getCodigo(), requerimentoVO.getMatricula().getCurso().getConfiguracaoAcademico().getMatricularApenasDisciplinaAulaProgramada(), requerimentoVO.getMatriculaPeriodoVO().getAno(), requerimentoVO.getMatriculaPeriodoVO().getSemestre(), true, requerimentoVO.getTipoRequerimento().getPermitirReporDisciplinaComAulaProgramada(), requerimentoVO.getTipoRequerimento().getIsTipoReposicao(), true, false, usuarioVO, true); 
			mapTurmas.clear();
			getFacadeFactory().getTurmaFacade().montarListaApresentacaoTurmaAgrupada(mapTurmas, turmaVOs, listaTurmaIncluir, usuarioVO);
		} catch (Exception e) {
			listaTurmaIncluir.clear();
		} finally {
			disciplinaConsultarTurma = null;
			cargaHorariaConsultarTurma = null;
		}
	}
	@Override
	public void definirTurmaBaseRequerimentosTurmaAgrupada(RequerimentoVO requerimentoVO, List<TurmaVO> listaTurmaIncluir, UsuarioVO usuarioVO, Map<Integer, List<TurmaVO>> mapTurmas, TurmaVO turmaVO) throws Exception  {
		try {
			Integer disciplinaConsultarTurma = requerimentoVO.getDisciplina().getCodigo();
			if (requerimentoVO.getDisciplinaPorEquivalencia()) {
				requerimentoVO.setMapaEquivalenciaDisciplinaVO(getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarPorChavePrimaria(requerimentoVO.getMapaEquivalenciaDisciplinaVO().getCodigo(), NivelMontarDados.TODOS));
				disciplinaConsultarTurma = requerimentoVO.getMapaEquivalenciaDisciplinaVO().getMapaEquivalenciaDisciplinaCursadaVOs().get(0).getDisciplinaVO().getCodigo();
			}
			requerimentoVO.setCargaHorariaDisciplina(getFacadeFactory().getGradeDisciplinaFacade().consultarCargaHorariaDisciplinaPorDisciplinaETurma(disciplinaConsultarTurma, requerimentoVO.getMatricula().getMatricula(), usuarioVO));
			if (mapTurmas.isEmpty()) {
				montarListaSelectItemTurmaAdicionar(requerimentoVO, listaTurmaIncluir, usuarioVO, mapTurmas);
			}
			List<TurmaVO> listaTurmasConsiderar = new ArrayList<TurmaVO>();
			listaTurmasConsiderar = mapTurmas.get(turmaVO.getCodigo());
			if (Uteis.isAtributoPreenchido(listaTurmasConsiderar)) {
				String identificadorTurmaBase = turmaVO.getIdentificadorTurma();
				requerimentoVO.setTurmaReposicao(listaTurmasConsiderar.get(0));
				requerimentoVO.getTurmaReposicao().setIdentificadorTurmaBase(identificadorTurmaBase);
				if (!Uteis.isAtributoPreenchido(requerimentoVO.getTurmaReposicao().getDataPrimeiraAula())) {
					requerimentoVO.getTurmaReposicao().setDataPrimeiraAula(turmaVO.getDataPrimeiraAula());
				}
				if (!Uteis.isAtributoPreenchido(requerimentoVO.getTurmaReposicao().getDataUltimaAula())) {
					requerimentoVO.getTurmaReposicao().setDataUltimaAula(turmaVO.getDataUltimaAula());
				}
			} else {
				requerimentoVO.setTurmaReposicao(turmaVO);
			}	
		} catch (Exception e) {
			throw e;
		}
	
	}
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarTrancamentoAlunoAutomatico(RequerimentoVO requerimentoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception {
	try {
		if (requerimentoVO.getTipoRequerimento().getTipo().equals("TR") && requerimentoVO.getIsDeferido().booleanValue() && requerimentoVO.getTipoRequerimento().getDeferirAutomaticamenteTrancamento().booleanValue()) {
			TrancamentoVO trancamentoVO = new TrancamentoVO();
			trancamentoVO.setJustificativa(requerimentoVO.getJustificativaTrancamento());
			trancamentoVO.setData(new Date());
			trancamentoVO.setDescricao(requerimentoVO.getJustificativaTrancamento());
			getFacadeFactory().getMatriculaFacade().carregarDados(requerimentoVO.getMatricula(), usuario);
			trancamentoVO.setMatricula(requerimentoVO.getMatricula());
			trancamentoVO.setSituacao(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor());
			trancamentoVO.setMotivoCancelamentoTrancamento(requerimentoVO.getMotivoCancelamentoTrancamento());
			getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(requerimentoVO.getMatriculaPeriodoVO(),  usuario);
			
			
			// verificado se a matriculaperiodo q ta chegando e matricula ja renovada verificando se o ano da matricula e maior q o ano vigente ou 
			// se o ano e igual porem o semestre e maior que o semestre vigente . caso encaixe nesta regra significa que o aluno ja renovou 
			// e agora no requerimento de trancamento ao invez de renovar e trancar a anterior sera trancada a atual pois o processo de renovaçao aconteceu antes 
			// do trancamento sendo assim  sera trancado a ultima matricula periodo atual renovada.
//			Boolean jaRenovou = (Uteis.isAtributoPreenchido(requerimentoVO.getMatriculaPeriodoVO().getAno())
//					&& ((new BigDecimal(requerimentoVO.getMatriculaPeriodoVO().getAno()).compareTo(new BigDecimal(Uteis.getAnoDataAtual())) > 0)
//							|| ((new BigDecimal(requerimentoVO.getMatriculaPeriodoVO().getAno()).compareTo(new BigDecimal(Uteis.getAnoDataAtual())) == 0)
//									&& Uteis.isAtributoPreenchido(requerimentoVO.getMatriculaPeriodoVO().getSemestre())
//									&& (new BigDecimal(requerimentoVO.getMatriculaPeriodoVO().getSemestre()).compareTo(new BigDecimal(Uteis.getSemestreAtual())) > 0))));
			
			
			
			if(requerimentoVO.getTipoRequerimento().getRegistrarTrancamentoProximoSemestre().booleanValue() &&  
					((!requerimentoVO.getMatriculaPeriodoVO().getAno().equals(requerimentoVO.getTipoRequerimento().getAno()))
					|| !(requerimentoVO.getMatriculaPeriodoVO().getSemestre().equals(requerimentoVO.getTipoRequerimento().getSemestre())))
					&& Integer.valueOf((requerimentoVO.getMatriculaPeriodoVO().getAno()+requerimentoVO.getMatriculaPeriodoVO().getSemestre())) < Integer.valueOf((requerimentoVO.getTipoRequerimento().getAno()+requerimentoVO.getTipoRequerimento().getSemestre()))) {
				trancamentoVO.setAno(requerimentoVO.getTipoRequerimento().getAno());
				trancamentoVO.setSemestre(requerimentoVO.getTipoRequerimento().getSemestre());				
				getFacadeFactory().getTrancamentoFacade().executarVerificacaoEGeracaoMatriculaPeriodoSemestrePosteriorMapaRegistroAbandonoCursoTrancamento(trancamentoVO, requerimentoVO.getMatriculaPeriodoVO(),  usuario);
				if(!Uteis.isAtributoPreenchido(trancamentoVO.getMatriculaPeriodoVO())) {
					trancamentoVO.setUltimaMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatriculaSituacoes(trancamentoVO.getMatricula().getMatricula(), "'AT', 'PR', 'PC', 'AC', 'TR', 'ER'", false, Uteis.NIVELMONTARDADOS_COMBOBOX,  usuario));
					trancamentoVO.setMatriculaPeriodoVO(trancamentoVO.getUltimaMatriculaPeriodoVO());
				}else {
					trancamentoVO.setUltimaMatriculaPeriodoVO(trancamentoVO.getMatriculaPeriodoVO());
				}
				trancamentoVO.setAno(trancamentoVO.getMatriculaPeriodoVO().getAno());
				trancamentoVO.setSemestre(trancamentoVO.getMatriculaPeriodoVO().getSemestre());
			}else {
				trancamentoVO.setMatriculaPeriodoVO(requerimentoVO.getMatriculaPeriodoVO());				
				trancamentoVO.setAno(requerimentoVO.getMatriculaPeriodoVO().getAno());
				trancamentoVO.setSemestre(requerimentoVO.getMatriculaPeriodoVO().getSemestre());
			}
			trancamentoVO.setTipoTrancamento("TR");
			getFacadeFactory().getTrancamentoFacade().incluirEDeferirRequerimento(trancamentoVO, configuracaoGeralSistema,  usuario, true);
//			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(trancamentoVO.getMatricula().getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
//			if(Uteis.isAtributoPreenchido(usuarioVO)) {
//				JobExecutarSincronismoComLdapAoCancelarTransferirMatricula jobExecutarSincronismoComLdapAoCancelarTransferirMatricula = new JobExecutarSincronismoComLdapAoCancelarTransferirMatricula(usuarioVO, trancamentoVO.getMatricula(), false);
//				Thread jobSincronizarCancelamento = new Thread(jobExecutarSincronismoComLdapAoCancelarTransferirMatricula);
//				jobSincronizarCancelamento.start();
//			}			
//			if ( Uteis.isAtributoPreenchido(getAplicacaoControle().getConfiguracaoSeiBlackboardVO())) {
//				PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO =  getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(trancamentoVO.getMatricula().getAluno().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
//				if(Uteis.isAtributoPreenchido(pessoaEmailInstitucionalVO)) {
//					if(!Uteis.isAtributoPreenchido(trancamentoVO.getMatricula().getCurso().getConfiguracaoLdapVO())) {
//						trancamentoVO.getMatricula().getCurso().setConfiguracaoLdapVO(getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarConfiguracaoLdapPorPessoa(trancamentoVO.getMatricula().getAluno().getCodigo()));
//					}
//					if (!getFacadeFactory().getMatriculaFacade().verificarExisteMultiplasMatriculasAtivasDominioLDAP(trancamentoVO.getMatricula(), usuarioVO)) {
//						getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().realizarInativacaoPessoaBlack(pessoaEmailInstitucionalVO.getEmail(), usuario);
//					}
//				}			
//			}
			requerimentoVO.getMatricula().setSituacao(SituacaoVinculoMatricula.TRANCADA.getValor());
		}
	} catch (Exception e) {
		throw e;
	}
}
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarCancelamentoAlunoAutomatico(RequerimentoVO requerimentoVO, UsuarioVO usuario) throws Exception {
		try {
			if (requerimentoVO.getTipoRequerimento().getTipo().equals("CA") && (requerimentoVO.getIsDeferido() || requerimentoVO.getTipoRequerimento().getDeferirAutomaticamenteTrancamento())) {
				getFacadeFactory().getMatriculaFacade().carregarDados(requerimentoVO.getMatricula(), usuario);
				if(!requerimentoVO.getMatricula().getSituacao().equals(SituacaoVinculoMatricula.CANCELADA.getValor())){
				CancelamentoVO cancelamentoVO = new CancelamentoVO();
				cancelamentoVO.setJustificativa(requerimentoVO.getJustificativaTrancamento());
				cancelamentoVO.setData(new Date());
				cancelamentoVO.setDescricao(requerimentoVO.getJustificativaTrancamento());
				cancelamentoVO.setMatricula(requerimentoVO.getMatricula());
				cancelamentoVO.setSituacao(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor());
				cancelamentoVO.setResponsavelAutorizacao(usuario);
				cancelamentoVO.setMotivoCancelamentoTrancamento(requerimentoVO.getMotivoCancelamentoTrancamento());
				cancelamentoVO.setCodigoRequerimento(requerimentoVO);
//				getFacadeFactory().getCancelamentoFacade().incluir(cancelamentoVO, getAplicacaoControle().getConfiguracaoGeralSistemaVO(requerimentoVO.getMatricula().getUnidadeEnsino().getCodigo(), usuario), getAplicacaoControle().getConfiguracaoFinanceiroVO(requerimentoVO.getMatricula().getUnidadeEnsino().getCodigo()), usuario, false);
				StringBuilder sql  = new StringBuilder("UPDATE historico set situacao = '");
				sql.append(SituacaoVinculoMatricula.CANCELADA.getValor()).append("' ");
				sql.append(" where matricula = ?");
				sql.append(" and situacao in ('CS', 'CE') ");
				getConexao().getJdbcTemplate().update(sql.toString(), requerimentoVO.getMatricula().getMatricula());
				requerimentoVO.getMatricula().setSituacao(SituacaoVinculoMatricula.CANCELADA.getValor());
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	private void realizarDeferimentoRequerimentoTCC(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) throws Exception {
		if(requerimentoVO.getTipoRequerimento().getTipo().equals("TC")) {
			getFacadeFactory().getMatriculaFacade().alterarDadosMatriculaDeferimentoIndeferimento(montarDadosMatricula(requerimentoVO), false, usuarioVO);
			if(requerimentoVO.getTipoRequerimento().getRegistrarAproveitamentoDisciplinaTCC()) {
//				getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(requerimentoVO.getMatriculaPeriodoVO(), NivelMontarDados.BASICO, getAplicacaoControle().getConfiguracaoFinanceiroVO(requerimentoVO.getUnidadeEnsino().getCodigo()), usuarioVO);
				List<GradeDisciplinaVO> gradeDisciplinaVOs =  getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinaVOTCCsNaoAprovadoAluno(requerimentoVO.getMatricula().getMatricula(), requerimentoVO.getMatricula().getGradeCurricularAtual().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
				if (gradeDisciplinaVOs != null && Uteis.isAtributoPreenchido(gradeDisciplinaVOs)) {
					for(GradeDisciplinaVO gradeDisciplinaVO : gradeDisciplinaVOs) {
						RequerimentoDisciplinasAproveitadasVO requerimentoDisciplinasAproveitadasVO =  new RequerimentoDisciplinasAproveitadasVO();
						requerimentoDisciplinasAproveitadasVO.setAno(requerimentoVO.getMatriculaPeriodoVO().getAno());
						requerimentoDisciplinasAproveitadasVO.setSemestre(requerimentoVO.getMatriculaPeriodoVO().getSemestre());
						requerimentoDisciplinasAproveitadasVO.setApresentarAprovadoHistorico(false);
						requerimentoDisciplinasAproveitadasVO.setAproveitamentoPorIsencao(true);
						requerimentoDisciplinasAproveitadasVO.setCargaHoraria(gradeDisciplinaVO.getCargaHoraria());
						requerimentoDisciplinasAproveitadasVO.setCargaHorariaCursada(gradeDisciplinaVO.getCargaHoraria());
						requerimentoDisciplinasAproveitadasVO.setGradeDisciplinaVO(gradeDisciplinaVO);
						requerimentoDisciplinasAproveitadasVO.setFrequencia(100.0);
						requerimentoDisciplinasAproveitadasVO.setNota(requerimentoVO.getNotaMonografia());
						requerimentoDisciplinasAproveitadasVO.setUtilizaNotaConceito(true);
						requerimentoDisciplinasAproveitadasVO.setMediaFinalConceito("Dispensado");
						requerimentoDisciplinasAproveitadasVO.setNomeDisciplinaCursada(gradeDisciplinaVO.getDisciplina().getNome());
						requerimentoDisciplinasAproveitadasVO.setConfiguracaoAcademicoVO(gradeDisciplinaVO.getConfiguracaoAcademico());
						requerimentoDisciplinasAproveitadasVO.setDataDeferimento(requerimentoVO.getDataFinalizacao());
						requerimentoDisciplinasAproveitadasVO.setResponsavelDeferimento(usuarioVO);
						requerimentoDisciplinasAproveitadasVO.setDisciplina(gradeDisciplinaVO.getDisciplina());
						requerimentoDisciplinasAproveitadasVO.setExcluirHistoricoDisciplinaCursada(true);
						requerimentoDisciplinasAproveitadasVO.setPeriodoLetivoOrigemDisciplina(gradeDisciplinaVO.getPeriodoLetivo());
						requerimentoDisciplinasAproveitadasVO.setSituacaoRequerimentoDisciplinasAproveitadasEnum(SituacaoRequerimentoDisciplinasAproveitadasEnum.DEFERIDO);
						requerimentoDisciplinasAproveitadasVO.setTipo("AP");
						requerimentoDisciplinasAproveitadasVO.setRequerimentoVO(requerimentoVO);
						getFacadeFactory().getRequerimentoDisciplinasAproveitadasFacade().adicionarRequerimentoDisciplinasAproveitadas(requerimentoVO, requerimentoDisciplinasAproveitadasVO, usuarioVO);
					}
					getFacadeFactory().getRequerimentoDisciplinasAproveitadasFacade().persistir(requerimentoVO.getListaRequerimentoDisciplinasAproveitadasVOs(), false, getAplicacaoControle().getConfiguracaoGeralSistemaVO(requerimentoVO.getUnidadeEnsino().getCodigo(), usuarioVO), usuarioVO);
					getFacadeFactory().getAproveitamentoDisciplinaFacade().realizarGeracaoAproveitamentoDisciplinaAutomaticoPorRequerimento(requerimentoVO, usuarioVO);
					SalaAulaBlackboardVO salaAulaBlackboardVO = (getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardPorMatriculaPorTipoSalaBlackboard(requerimentoVO.getMatricula().getMatricula(), TipoSalaAulaBlackboardEnum.TCC_GRUPO, usuarioVO));
					if(Uteis.isAtributoPreenchido(salaAulaBlackboardVO)) {
						SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO =  getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarPorIdSalaAulaBlackboardMatricula(salaAulaBlackboardVO.getIdSalaAulaBlackboard(), requerimentoVO.getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
						getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().excluirPessoaSalaBlack(salaAulaBlackboardVO.getCodigo(), requerimentoVO.getMatricula().getAluno().getCodigo(), TipoSalaAulaBlackboardPessoaEnum.ALUNO, salaAulaBlackboardVO.getIdSalaAulaBlackboard(), salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().getEmail(), usuarioVO);
					}
				}
			}
		}
	}
	
	@Override
	public void adicionarRequerimentoDisciplina(RequerimentoVO requerimentoVO, RequerimentoDisciplinaVO requerimentoDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		if(!Uteis.isAtributoPreenchido(requerimentoDisciplinaVO.getDisciplina())) {
			throw new Exception("O campo DISCIPLINA deve ser informado.");
		}
		if(!Uteis.isAtributoPreenchido(requerimentoDisciplinaVO.getVariavelNota())) {
			throw new Exception("O campo TIPO AVALIAÇÃO deve ser informado.");
		}
		if(requerimentoDisciplinaVO.getListaSelectItemNota().stream().anyMatch(t -> ((String) t.getValue()).equals(requerimentoDisciplinaVO.getVariavelNota()))){
			requerimentoDisciplinaVO.setTituloNota(requerimentoDisciplinaVO.getListaSelectItemNota().stream().filter(t -> ((String) t.getValue()).equals(requerimentoDisciplinaVO.getVariavelNota())).findFirst().get().getLabel());
		}else {
			throw new Exception("O campo TIPO AVALIAÇÃO deve ser informado.");
		}
		if(!requerimentoVO.getRequerimentoDisciplinaVOs().stream().anyMatch(t -> t.getDisciplina().getCodigo().equals(requerimentoDisciplinaVO.getDisciplina().getCodigo()))) {
			requerimentoDisciplinaVO.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(requerimentoDisciplinaVO.getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
			requerimentoVO.getRequerimentoDisciplinaVOs().add(requerimentoDisciplinaVO);
		}
	}
	
	@Override
	public void removerRequerimentoDisciplina(RequerimentoVO requerimentoVO, RequerimentoDisciplinaVO requerimentoDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		requerimentoVO.getRequerimentoDisciplinaVOs().removeIf(t -> t.getDisciplina().getCodigo().equals(requerimentoDisciplinaVO.getDisciplina().getCodigo()));
	}

	@Override
	public void carregarDadoCampoServidorOnlineArquivo(RequerimentoVO requerimentoVO,UsuarioVO usuarioVO) throws Exception {
		requerimentoVO.getArquivoVO().setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuarioVO).getServidorArquivoOnline()));
	}
	
	private StringBuffer getSQLPadraoConsultaOperacaoLote() {
		StringBuffer str = new StringBuffer();
		// Requerimentox
		str.append("SELECT distinct requerimento.codigo AS \"requerimento.codigo\", requerimento.nomeRequerente AS \"requerimento.nomeRequerente\",  requerimento.numeroVia as \"requerimento.numeroVia\", ");
		str.append("requerimento.dataPrevistaFinalizacao AS \"requerimento.dataPrevistaFinalizacao\", requerimento.data AS \"requerimento.data\",");
		
		str.append(" requerimento.ordemExecucaoTramiteDepartamento as \"requerimento.ordemExecucaoTramiteDepartamento\",  ");
		str.append(" requerimento.responsavel AS \"requerimento.responsavel\", responsavel.nome AS \"requerimento.nomeResponsavel\", ");
		str.append(" requerimento.curso, curso.nome as nomeCurso, requerimento.turno, turno.nome as nometurno, ");
		str.append(" requerimento.tipopessoa as \"requerimento.tipopessoa\", ");
		
		str.append("requerimento.tipoTrabalhoConclusaoCurso AS \"requerimento.tipoTrabalhoConclusaoCurso\", requerimento.tituloMonografia AS \"requerimento.tituloMonografia\", ");
		str.append("requerimento.orientadorMonografia AS \"requerimento.orientadorMonografia\", requerimento.notamonografia AS \"requerimento.notamonografia\", requerimento.numerovia AS \"requerimento.numerovia\", ");
		
		// Dados Pessoa
		str.append("funcionario.codigo AS \"funcionario.codigo\", pessoaFuncionario.codigo AS \"pessoaFuncionario.codigo\", pessoaFuncionario.nome AS \"pessoaFuncionario.nome\", ");
		// Endereço de Entrega
		str.append("requerimento.endereco AS \"requerimento.endereco\", requerimento.setor AS \"requerimento.setor\", requerimento.numero AS \"requerimento.numero\", ");
		str.append("requerimento.cep AS \"requerimento.cep\", requerimento.complemento AS \"requerimento.complemento\", ");
		str.append("requerimento.situacaoRequerimentoDepartamento as \"requerimento.situacaoRequerimentoDepartamento\", ");
		str.append("requerimento.situacaoIsencaoTaxa as \"requerimento.situacaoIsencaoTaxa\", ");
		str.append("requerimento.comprovanteSolicitacaoIsencao as \"requerimento.comprovanteSolicitacaoIsencao\", ");
		str.append("requerimento.justificativaSolicitacaoIsencao as \"requerimento.justificativaSolicitacaoIsencao\", ");
		str.append("requerimento.motivoDeferimentoIndeferimentoIsencaoTaxa as \"requerimento.motivoDeferimentoIndeferimentoIsencaoTaxa\", ");
		str.append("requerimento.responsavelDeferimentoIndeferimentoIsencaoTaxa as \"requerimento.respDefIndefIsencaoTaxa\", ");
		str.append("requerimento.dataDeferimentoIndeferimentoIsencaoTaxa as \"requerimento.dataDeferimentoIndeferimentoIsencaoTaxa\", ");			
		str.append("requerimento.grupofacilitador as \"requerimento.grupofacilitador\", requerimento.temaTccFacilitador AS \"requerimento.temaTccFacilitador\", requerimento.assuntoTccFacilitador AS \"requerimento.assuntoTccFacilitador\", requerimento.avaliadorExternoFacilitador AS \"requerimento.avaliadorExternoFacilitador\", ");			
		
		// Matricula
		str.append("matricula.matricula AS \"matricula.matricula\", ");
		str.append("matricula.gradeCurricularAtual AS \"matricula.gradeCurricularAtual\", ");
		str.append("motivoCancelamentoTrancamento.nome AS \"motivoCancelamentoTrancamento_nome\", ");
		
		// Turma
		// Pessoa(Aluno)
		str.append("p1.codigo AS \"aluno.codigo\", p1.nome AS \"aluno.nome\", p1.cpf AS \"aluno.cpf\", p2.codigo AS \"pessoa.codigo\", p2.nome AS \"pessoa.nome\", ");
		str.append("p2.email AS \"pessoa.email\", p2.rg AS \"pessoa.rg\", p2.cpf AS \"pessoa.cpf\",  p2.telefoneres AS \"pessoa.telefoneres\",  ");
		str.append("p2.telefonerecado AS \"pessoa.telefonerecado\", p2.telefonecomer AS \"pessoa.telefonecomer\", p2.celular AS \"pessoa.celular\", ");
		// Tipo Requerimento
		str.append("tipoRequerimento.codigo AS \"tipoRequerimento.codigo\", tipoRequerimento.nome AS \"tipoRequerimento.nome\", ");

		// Unidade de Ensino
		str.append("unidadeEnsino.codigo AS \"unidadeEnsino.codigo\", unidadeEnsino.nome AS \"unidadeEnsino.nome\", unidadeEnsino.numeroVagaOfertada AS \"unidadeEnsino.numeroVagaOfertada\", ");
		str.append("disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", disciplina.abreviatura as \"disciplina.abreviatura\", ");
		
		// Responsável Boleto
		str.append("requerimento.justificativaTrancamento as \"requerimento.justificativaTrancamento\", requerimento.motivoCancelamentoTrancamento as \"requerimento.motivoCancelamentoTrancamento\", ");
		str.append("matriculaperiodo.ano, matriculaperiodo.semestre, matriculaperiodo.situacaomatriculaperiodo AS situacaomatriculaperiodo, matriculaperiodo.codigo AS \"matriculaperiodo.codigo\", ");
		
		str.append("unidadeEnsinoTransferenciaInterna.codigo as unidadeEnsinoTransferenciaInterna_codigo, unidadeEnsinoTransferenciaInterna.nome as unidadeEnsinoTransferenciaInterna_nome, unidadeEnsinoTransferenciaInterna.numeroVagaOfertada as unidadeEnsinoTransferenciaInterna_numeroVagaOfertada,");
		str.append("cursoTransferenciaInterna.codigo as cursoTransferenciaInterna_codigo, cursoTransferenciaInterna.nome as cursoTransferenciaInterna_nome, ");
		str.append("turnoTransferenciaInterna.codigo as turnoTransferenciaInterna_codigo, turnoTransferenciaInterna.nome as turnoTransferenciaInterna_nome,  ");
		str.append(" case when coalesce(p1.nome, '') <> '' then p1.nome else p2.nome end as \"nomerequerente.ordenar\",  ");
		str.append(" unidadeEnsinoTransferenciaInterna.quantidadeComputadoresAlunos as quantidadeComputadoresAlunos, ");
		str.append(" matricula.diaSemanaAula as matricula_diaSemanaAula, ");
		str.append(" case when tiporequerimento.tipo = 'TI' then  (select count(m.matricula) from matricula as m ");
		str.append(" where m.diasemanaaula = matricula.diasemanaaula and m.situacao = 'AT' and m.unidadeensino = unidadeEnsinoTransferenciaInterna.codigo and m.curso = cursoTransferenciaInterna.codigo ");
		str.append(") else 0 end as vagaPorCursoDiaSemana, ");
		str.append(" case when tiporequerimento.tipo = 'TI' then (select count(m.matricula) from matricula as m ");
		str.append(" where m.diasemanaaula = matricula.diasemanaaula and m.situacao =  'AT' and m.unidadeensino = unidadeEnsinoTransferenciaInterna.codigo  ");
		str.append(") else 0 end as vagaPorUnidadeEnsinoDiaSemana, ");
		str.append(" salaaulablackboard.nomeGrupo as salaaulablackboard_nomeGrupo ");
		str.append("FROM Requerimento ");
		str.append("LEFT JOIN Funcionario on Funcionario.codigo = Requerimento.funcionario  ");
		str.append("LEFT JOIN Pessoa as pessoaFuncionario on Funcionario.pessoa = pessoaFuncionario.codigo  ");
		str.append("LEFT JOIN TipoRequerimento on Requerimento.tipoRequerimento = TipoRequerimento.codigo  ");
		str.append("LEFT JOIN UnidadeEnsino on requerimento.unidadeEnsino = unidadeEnsino.codigo ");
		str.append("LEFT JOIN Matricula on matricula.matricula = requerimento.matricula ");
		str.append("LEFT JOIN MatriculaPeriodo on MatriculaPeriodo.matricula = matricula.matricula ");
		str.append("and case when Requerimento.matriculaperiodo is not null then  Requerimento.matriculaperiodo = matriculaperiodo.codigo else ");		
		str.append(" matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (MatriculaPeriodo.ano||'/'|| MatriculaPeriodo.semestre) desc, mp.periodoletivomatricula desc limit 1) end ");
		str.append("LEFT JOIN departamento on departamento.codigo = requerimento.departamentoResponsavel ");
		str.append("LEFT JOIN Pessoa p1 on p1.codigo = matricula.aluno ");
		str.append("LEFT JOIN Pessoa p2 on p2.codigo = requerimento.pessoa ");
		str.append("LEFT JOIN usuario responsavel on responsavel.codigo = requerimento.responsavel ");
		str.append("LEFT JOIN curso on curso.codigo = (case when requerimento.curso is not null then requerimento.curso else matricula.curso end) ");
		str.append("LEFT JOIN turno on turno.codigo = requerimento.turno ");
		str.append("LEFT JOIN disciplina on disciplina.codigo = requerimento.disciplina ");
		str.append("LEFT JOIN UnidadeEnsino unidadeEnsinoTransferenciaInterna on requerimento.unidadeEnsinoTransferenciaInterna = unidadeEnsinoTransferenciaInterna.codigo ");
		str.append("LEFT JOIN Curso cursoTransferenciaInterna on requerimento.cursoTransferenciaInterna = cursoTransferenciaInterna.codigo ");
		str.append("LEFT JOIN Turno turnoTransferenciaInterna on requerimento.turnoTransferenciaInterna = turnoTransferenciaInterna.codigo ");				
		str.append("LEFT JOIN salaaulablackboard on requerimento.grupoFacilitador = salaaulablackboard.codigo ");				
		str.append("LEFT JOIN motivoCancelamentoTrancamento on requerimento.motivoCancelamentoTrancamento = motivoCancelamentoTrancamento.codigo ");				
		return str;
	}
	
	@Override
	public List<RequerimentoVO> consultarRequerimentoOperacaoLote(RequerimentoVO obj, List<UnidadeEnsinoVO> listaUnidadeEnsino, CursoVO curso, Date dataIni, Date dataFim, boolean todoPeriodo, String ordenarPor, boolean ordemCrescente, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, int limit, int offset, Boolean permitirConsultarTodasUnidades, Boolean permiteConsultarRequerimentoOutrosResponsaveis, Boolean permiteConsultarRequerimentoOutrosResponsaveisMesmoDepartanento,  Boolean permitirUsuarioConsultarIncluirApenasRequerimentosProprios) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);		
		StringBuffer sqlStr = getSQLPadraoConsultaOperacaoLote();
		sqlStr.append(realizarGeracaoCondicaoWhereConsultaRequerimento(obj, listaUnidadeEnsino, null, curso, null, "requerimentoOperacaoLote", "", dataIni, dataFim, todoPeriodo, "", null, usuario, configuracaoGeralSistemaVO, permitirConsultarTodasUnidades, permiteConsultarRequerimentoOutrosResponsaveis, permiteConsultarRequerimentoOutrosResponsaveisMesmoDepartanento, permitirUsuarioConsultarIncluirApenasRequerimentosProprios));
		montarRegraOrdenacaoConsultaRequerimento(ordenarPor, ordemCrescente, sqlStr);		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaOperacaoLote(tabelaResultado, usuario);
	}
	
	public List<RequerimentoVO> montarDadosConsultaOperacaoLote(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<RequerimentoVO> vetResultado = new ArrayList<RequerimentoVO>(0);
		while (tabelaResultado.next()) {
			RequerimentoVO obj = new RequerimentoVO();
			montarDadosCompletoOperacaoLote(obj, tabelaResultado, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	private void montarDadosCompletoOperacaoLote(RequerimentoVO obj, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		// Dados Requerimento
		obj.setNovoObj(false);
		obj.setCodigo(dadosSQL.getInt("requerimento.codigo"));
		obj.setNomeRequerente(dadosSQL.getString("requerimento.nomeRequerente"));
		obj.setTemaTccFacilitador(dadosSQL.getString("requerimento.temaTccFacilitador"));
		obj.setAssuntoTccFacilitador(dadosSQL.getString("requerimento.assuntoTccFacilitador"));
		obj.setAvaliadorExternoFacilitador(dadosSQL.getString("requerimento.avaliadorExternoFacilitador"));
		obj.setTituloMonografia(dadosSQL.getString("requerimento.tituloMonografia"));
		obj.setOrientadorMonografia(dadosSQL.getString("requerimento.orientadorMonografia"));
		obj.setNotaMonografia(dadosSQL.getDouble("requerimento.notaMonografia"));
		obj.getGrupoFacilitador().setNomeGrupo(dadosSQL.getString("salaaulablackboard_nomeGrupo"));
		obj.setDataPrevistaFinalizacao(dadosSQL.getDate("requerimento.dataPrevistaFinalizacao"));
		obj.setData(dadosSQL.getTimestamp("requerimento.data"));
		obj.getTurno().setCodigo(dadosSQL.getInt("turno"));
		obj.getTurno().setNome(dadosSQL.getString("nometurno"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("requerimento.responsavel"));
		obj.getResponsavel().setNome(dadosSQL.getString("requerimento.nomeResponsavel"));
		obj.setOrdemExecucaoTramiteDepartamento(dadosSQL.getInt("requerimento.ordemExecucaoTramiteDepartamento"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		if (dadosSQL.getString("situacaomatriculaperiodo") != null) {
			obj.setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum.getEnumPorValor(dadosSQL.getString("situacaomatriculaperiodo")).toString());
		}
		// Dados do Aluno
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula.matricula"));
		obj.getMatricula().getAluno().setCodigo(dadosSQL.getInt("aluno.codigo"));
		obj.getMatricula().getAluno().setNome(dadosSQL.getString("aluno.nome"));
		obj.getMatricula().getAluno().setCPF(dadosSQL.getString("aluno.cpf"));
		obj.getCurso().setCodigo(dadosSQL.getInt("curso"));
		obj.getCurso().setNome(dadosSQL.getString("nomecurso"));
		
		obj.getDisciplina().setCodigo(dadosSQL.getInt("disciplina.codigo"));
		obj.getDisciplina().setNome(dadosSQL.getString("disciplina.nome"));
		obj.getDisciplina().setAbreviatura(dadosSQL.getString("disciplina.abreviatura"));
		

		// obj.getMatricula().
		// Dados da Pessoa
		obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getPessoa().setNome(dadosSQL.getString("pessoa.nome"));
		// Tipo de Requerimento
		obj.getTipoRequerimento().setCodigo(dadosSQL.getInt("tipoRequerimento.codigo"));
		obj.getTipoRequerimento().setNome(dadosSQL.getString("tipoRequerimento.nome"));
		obj.getFuncionarioVO().setCodigo(dadosSQL.getInt("funcionario.codigo"));
		obj.getFuncionarioVO().getPessoa().setCodigo(dadosSQL.getInt("pessoaFuncionario.codigo"));
		obj.getFuncionarioVO().getPessoa().setNome(dadosSQL.getString("pessoaFuncionario.nome"));
		
		// Unidade de Ensino
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
		obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));
		obj.getUnidadeEnsino().setEndereco("");
		obj.getUnidadeEnsinoTransferenciaInternaVO().setCodigo(dadosSQL.getInt("unidadeEnsinoTransferenciaInterna_codigo"));
		obj.getUnidadeEnsinoTransferenciaInternaVO().setNome(dadosSQL.getString("unidadeEnsinoTransferenciaInterna_nome"));
		obj.getUnidadeEnsinoTransferenciaInternaVO().setNumeroVagaOfertada(dadosSQL.getInt("unidadeEnsinoTransferenciaInterna_numeroVagaOfertada"));
		obj.getCursoTransferenciaInternaVO().setCodigo(dadosSQL.getInt("cursoTransferenciaInterna_codigo"));
		obj.getCursoTransferenciaInternaVO().setNome(dadosSQL.getString("cursoTransferenciaInterna_nome"));
		obj.getTurnoTransferenciaInternaVO().setCodigo(dadosSQL.getInt("turnoTransferenciaInterna_codigo"));
		obj.getTurnoTransferenciaInternaVO().setNome(dadosSQL.getString("turnoTransferenciaInterna_nome"));
		obj.getMatriculaPeriodoVO().setCodigo(dadosSQL.getInt("matriculaPeriodo.codigo"));

		if (Uteis.isColunaExistente(dadosSQL, "requerimento.justificativaTrancamento") && Uteis.isAtributoPreenchido(dadosSQL.getString("requerimento.justificativaTrancamento"))) {
			obj.setJustificativaTrancamento(dadosSQL.getString("requerimento.justificativaTrancamento"));
		}
		
		if (Uteis.isColunaExistente(dadosSQL, "requerimento.motivoCancelamentoTrancamento") && Uteis.isAtributoPreenchido(dadosSQL.getString("requerimento.motivoCancelamentoTrancamento"))) {
			obj.getMotivoCancelamentoTrancamento().setCodigo(dadosSQL.getInt("requerimento.motivoCancelamentoTrancamento"));
			obj.getMotivoCancelamentoTrancamento().setNome(dadosSQL.getString("motivoCancelamentoTrancamento_nome"));
		}
		obj.getUnidadeEnsinoTransferenciaInternaVO().setQuantidadeComputadoresAlunos(dadosSQL.getInt("quantidadeComputadoresAlunos"));
		obj.getMatricula().setDiaSemanaAula(DiaSemana.valueOf(dadosSQL.getString("matricula_diaSemanaAula")));
		obj.setVagaPorCursoDiaSemana(dadosSQL.getInt("vagaPorCursoDiaSemana"));
		obj.setVagaPorUnidadeEnsinoDiaSemana(dadosSQL.getInt("vagaPorUnidadeEnsinoDiaSemana"));
		
	}
	
	@Override	
	public void realizarGeracaoResumoOperacaoLote(List<RequerimentoVO> listaConsulta, TipoRequerimentoVO tipoRequerimentoVO, List<RequerimentoResumoOperacaoLoteVO> resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsino, List<RequerimentoResumoOperacaoLoteVO> resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsinoCurso) {
		if(Uteis.isAtributoPreenchido(tipoRequerimentoVO) && Uteis.isAtributoPreenchido(listaConsulta)) {
			if (tipoRequerimentoVO.getIsTipoTransferenciaInterna()) {
				Map<String, Integer> mapUnidadeEnsinoRequerimentos = new HashMap<>();
				Map<String, Integer> mapUnidadeEnsinoCursoRequerimentos = new HashMap<>();
				for (Iterator<RequerimentoVO> iterator = listaConsulta.iterator(); iterator.hasNext();) {
					final RequerimentoVO r = (RequerimentoVO) iterator.next();
					RequerimentoResumoOperacaoLoteVO requerimentoResumoOperacaoLoteUnidadeEnsinoVO = null;
					if (resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsino.stream().anyMatch(rs -> rs.getUnidadeEnsinoVO().getCodigo().equals(r.getUnidadeEnsinoTransferenciaInternaVO().getCodigo()))) {
						requerimentoResumoOperacaoLoteUnidadeEnsinoVO = resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsino.stream().filter(rs -> rs.getUnidadeEnsinoVO().getCodigo().equals(r.getUnidadeEnsinoTransferenciaInternaVO().getCodigo())).findFirst().get();
					} else {
						requerimentoResumoOperacaoLoteUnidadeEnsinoVO = new RequerimentoResumoOperacaoLoteVO();
						requerimentoResumoOperacaoLoteUnidadeEnsinoVO.setUnidadeEnsinoVO(r.getUnidadeEnsinoTransferenciaInternaVO());
						requerimentoResumoOperacaoLoteUnidadeEnsinoVO.setTipoRequerimentoVO(tipoRequerimentoVO);
						resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsino.add(requerimentoResumoOperacaoLoteUnidadeEnsinoVO);
					}
					RequerimentoResumoOperacaoLoteVO requerimentoResumoOperacaoLoteUnidadeEnsinoCursoVO = null;
					if (resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsinoCurso.stream().anyMatch(rs -> rs.getUnidadeEnsinoVO().getCodigo().equals(r.getUnidadeEnsinoTransferenciaInternaVO().getCodigo()) && rs.getCursoVO().getCodigo().equals(r.getCursoTransferenciaInternaVO().getCodigo()))) {
						requerimentoResumoOperacaoLoteUnidadeEnsinoCursoVO = resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsinoCurso.stream().filter(rs -> rs.getUnidadeEnsinoVO().getCodigo().equals(r.getUnidadeEnsinoTransferenciaInternaVO().getCodigo()) && rs.getCursoVO().getCodigo().equals(r.getCursoTransferenciaInternaVO().getCodigo())).findFirst().get();
					} else {
						requerimentoResumoOperacaoLoteUnidadeEnsinoCursoVO = new RequerimentoResumoOperacaoLoteVO();
						requerimentoResumoOperacaoLoteUnidadeEnsinoCursoVO.setUnidadeEnsinoVO(r.getUnidadeEnsinoTransferenciaInternaVO());
						requerimentoResumoOperacaoLoteUnidadeEnsinoCursoVO.setCursoVO(r.getCursoTransferenciaInternaVO());
						requerimentoResumoOperacaoLoteUnidadeEnsinoCursoVO.setTipoRequerimentoVO(tipoRequerimentoVO);
						resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsinoCurso.add(requerimentoResumoOperacaoLoteUnidadeEnsinoCursoVO);
					}
					requerimentoResumoOperacaoLoteUnidadeEnsinoVO.setQtdeMatriculados(r.getVagaPorUnidadeEnsinoDiaSemana());
					requerimentoResumoOperacaoLoteUnidadeEnsinoVO.setQtdeRequerimento(requerimentoResumoOperacaoLoteUnidadeEnsinoVO.getQtdeRequerimento() + 1);

					requerimentoResumoOperacaoLoteUnidadeEnsinoCursoVO.setQtdeMatriculados(r.getVagaPorCursoDiaSemana());
					requerimentoResumoOperacaoLoteUnidadeEnsinoCursoVO.setQtdeRequerimento(requerimentoResumoOperacaoLoteUnidadeEnsinoCursoVO.getQtdeRequerimento() + 1);
					String chaveUE = r.getUnidadeEnsinoTransferenciaInternaVO().getCodigo() + "-" + r.getTipoRequerimento().getCodigo();
					if (!mapUnidadeEnsinoRequerimentos.containsKey(chaveUE)) {
						mapUnidadeEnsinoRequerimentos.put(chaveUE, consultarContagemRequerimentoDeferidoUnidadeEnsinoCurso(r.getUnidadeEnsinoTransferenciaInternaVO().getCodigo(), null, r.getTipoRequerimento().getCodigo()));
					}
					requerimentoResumoOperacaoLoteUnidadeEnsinoVO.setQtdeRequerimentoDeferido(mapUnidadeEnsinoRequerimentos.get(chaveUE));
					
					String chaveUEC = r.getUnidadeEnsinoTransferenciaInternaVO().getCodigo() + "-" + r.getCursoTransferenciaInternaVO().getCodigo() + "-" + r.getTipoRequerimento().getCodigo();
					if (!mapUnidadeEnsinoCursoRequerimentos.containsKey(chaveUEC)) {
						mapUnidadeEnsinoCursoRequerimentos.put(chaveUEC, consultarContagemRequerimentoDeferidoUnidadeEnsinoCurso(r.getUnidadeEnsinoTransferenciaInternaVO().getCodigo(), r.getCursoTransferenciaInternaVO().getCodigo(), r.getTipoRequerimento().getCodigo()));
					}
					requerimentoResumoOperacaoLoteUnidadeEnsinoCursoVO.setQtdeRequerimentoDeferido(mapUnidadeEnsinoCursoRequerimentos.get(chaveUEC));
				}
				Ordenacao.ordenarLista(resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsinoCurso, "ordenarPorUnidadeEnsinoCurso");
				Ordenacao.ordenarLista(resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsino, "ordenarPorUnidadeEnsino");
			}
		}		
	}
	
	@Override
	public Integer consultarContagemRequerimentoDeferidoUnidadeEnsinoCurso(Integer unidadeEnsinoTransferenciaInterna,  Integer cursoTransferenciaInterna,  Integer tipoRequerimento)  {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT COUNT(*) AS total_requerimentos ");
		sqlStr.append("FROM requerimento ");
		sqlStr.append("INNER JOIN tiporequerimento t ON t.codigo = requerimento.tiporequerimento ");
		sqlStr.append("INNER JOIN matricula ON matricula.aluno = requerimento.pessoa ");
		sqlStr.append("AND matricula.unidadeensino = requerimento.unidadeensinotransferenciainterna ");
		sqlStr.append("AND matricula.curso = requerimento.cursotransferenciainterna ");
		sqlStr.append("INNER JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append("AND matriculaperiodo.codigo = ( ");
		sqlStr.append("  SELECT mp.codigo FROM matriculaperiodo AS mp WHERE mp.matricula = matricula.matricula ");
		sqlStr.append("  ORDER BY mp.ano, mp.semestre LIMIT 1 ");
		sqlStr.append(") ");
		sqlStr.append("INNER JOIN tiporequerimento ON tiporequerimento.codigo = requerimento.tiporequerimento ");
		sqlStr.append("AND ( ");
		sqlStr.append("  (tiporequerimento.registrartransferenciaproximosemestre ");
		sqlStr.append("  AND tiporequerimento.ano = matriculaperiodo.ano ");
		sqlStr.append("  AND tiporequerimento.semestre = matriculaperiodo.semestre) ");
		sqlStr.append("  OR (tiporequerimento.registrartransferenciaproximosemestre = false ");
		sqlStr.append("  AND matriculaperiodo.ano = EXTRACT(YEAR FROM current_date)::varchar ");
		sqlStr.append("  AND matriculaperiodo.semestre = CASE WHEN EXTRACT(MONTH FROM current_date) >= 8 THEN '2' ELSE '1' END ");
		sqlStr.append("  ) ");
		sqlStr.append(") ");
		sqlStr.append("WHERE requerimento.situacao = 'FD' ");
		sqlStr.append("AND requerimento.unidadeensinotransferenciainterna = ").append(unidadeEnsinoTransferenciaInterna).append(" ");
		if(cursoTransferenciaInterna != null &&  cursoTransferenciaInterna != 0) {
			sqlStr.append("AND requerimento.cursotransferenciainterna = ").append(cursoTransferenciaInterna).append(" ");
		}
		sqlStr.append("AND requerimento.tiporequerimento = ").append(tipoRequerimento).append(" ;");
	    Integer totalRequerimentos = getConexao().getJdbcTemplate().queryForObject(sqlStr.toString(), Integer.class);

		return totalRequerimentos;
	}
	
	public List<CidTipoRequerimentoVO> listaCidSelecionados(TipoRequerimentoVO tipoRequerimento, UsuarioVO usuario) throws Exception {
		List<CidTipoRequerimentoVO> lista = new ArrayList<>(0);
		for(CidTipoRequerimentoVO cidTipoRequerimentoVO : tipoRequerimento.getCidTipoRequerimentoVOs()) {
			if(cidTipoRequerimentoVO.getSelecionado()) {
				lista.add(cidTipoRequerimentoVO);
			}
		}
		if(validarDadosCid(tipoRequerimento, usuario) && lista.isEmpty()) {
			throw new Exception("Deve ser adicionado um código CID.");
		}
		return lista;
	}
	
	public Boolean validarDadosCid(TipoRequerimentoVO tipoRequerimento, UsuarioVO usuario) throws Exception {
		List<CidTipoRequerimentoVO> ListaCid =  getFacadeFactory().getCidTipoRequerimentoInterfaceFacade().consultarCidPorTipoRequerimento(tipoRequerimento, usuario);
		return ListaCid.isEmpty() ? false : true;
	}

	public void realizarValidacaoTipoAluno(RequerimentoVO requerimentoVO) throws Exception {
		if (Objects.nonNull(requerimentoVO) && requerimentoVO.isPermitirValidarTipoAlunoTipoRequerimento()) {
			if (requerimentoVO.getTipoRequerimento().isTipoAlunoCalouro()) {
				Uteis.checkState(!requerimentoVO.isAlunoCalouro(), "Tipo requerimento disponível apenas para alunos CALOUROS");
			} else if (requerimentoVO.getTipoRequerimento().isTipoAlunoVeterano()) {
				Uteis.checkState(!requerimentoVO.isAlunoVeterano(), "Tipo requerimento disponível apenas para alunos VETERANOS");
			}
		}
	}
	
	private void realizarMontagemSQLPadraoConsulta(StringBuffer str, TurmaVO turmaVO) {
		str.append("SELECT requerimento.codigo, p2.nome \"nomerequerente.ordenar\", ");
		str.append("requerimento.dataPrevistaFinalizacao, requerimento.dataUltimaAlteracao, tipoRequerimento.nome, requerimento.data, count(requerimento.codigo) OVER () totalregistros ");
		str.append("FROM Requerimento ");
		str.append("LEFT JOIN Funcionario on Funcionario.codigo = Requerimento.funcionario ");
		str.append("LEFT JOIN Pessoa as pessoaFuncionario on Funcionario.pessoa = pessoaFuncionario.codigo  ");
		str.append("INNER JOIN TipoRequerimento on Requerimento.tipoRequerimento = TipoRequerimento.codigo  ");
		str.append("INNER JOIN Pessoa p2 on p2.codigo = requerimento.pessoa ");
		str.append("LEFT JOIN UnidadeEnsino on requerimento.unidadeEnsino = unidadeEnsino.codigo ");
		str.append("LEFT JOIN Matricula on matricula.matricula = requerimento.matricula ");
		str.append("LEFT JOIN Pessoa p1 on p1.codigo = matricula.aluno ");
		str.append("LEFT JOIN Turma on turma.codigo = requerimento.turma ");
		if (Uteis.isAtributoPreenchido(turmaVO)) {
			str.append("LEFT JOIN MatriculaPeriodo on MatriculaPeriodo.matricula = matricula.matricula ");
			str.append("and case when Requerimento.matriculaperiodo is not null then  Requerimento.matriculaperiodo = matriculaperiodo.codigo else ");
			str.append(" case when turma.codigo != 0 then matriculaperiodo.turma = turma.codigo else ");
			str.append("matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (MatriculaPeriodo.ano||'/'|| MatriculaPeriodo.semestre) desc, mp.periodoletivomatricula desc limit 1) end end ");
		}
	}
}