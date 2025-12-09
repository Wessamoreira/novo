package negocio.facade.jdbc.secretaria;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
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

import negocio.comuns.academico.AutorizacaoCursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.ImpressaoDeclaracaoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoFuncionarioVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TrancamentoVO;
import negocio.comuns.academico.TurmaAberturaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.academico.enumeradores.TipoAssinaturaDocumentoEnum;
import negocio.comuns.academico.enumeradores.TipoDoTextoImpressaoContratoEnum;
import negocio.comuns.academico.enumeradores.TipoLivroRegistroDiplomaEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.OcorrenciaLGPDVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.EmailVO;
import negocio.comuns.arquitetura.TextoPadraoLayoutVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.arquitetura.enumeradores.TipoDesigneTextoEnum;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.ConfiguracaoGedOrigemVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.utilitarias.AssinaturaDigialDocumentoPDF;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.GeradorHtmlParaPdf;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisHTML;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UteisTextoPadrao;
import negocio.comuns.utilitarias.dominios.OrdemHistoricoDisciplina;
import negocio.comuns.utilitarias.dominios.OrientacaoPaginaEnum;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.comuns.utilitarias.dominios.TipoDisciplina;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.dominios.TiposRequerimento;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.academico.Matricula;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.secretaria.ImpressaoDeclaracaoInterfaceFacade;
import relatorio.arquitetura.GeradorRelatorio;
import relatorio.negocio.comuns.academico.BoletimAcademicoRelVO;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import webservice.aws.s3.ServidorArquivoOnlineS3RS;

/**
 *
 * @author Carlos
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class ImpressaoDeclaracao extends ControleAcesso implements ImpressaoDeclaracaoInterfaceFacade {

    protected static String idEntidade;

    public ImpressaoDeclaracao() throws Exception {
        super();
        setIdEntidade("ImpressaoDeclaracao");
    }

    public void consultarAlunoPorMatricula(ImpressaoContratoVO impressaoContratoVO, Integer unidadeEnsino, UsuarioVO usuarioLogado) throws Exception {
        MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(impressaoContratoVO.getMatriculaVO().getMatricula(), unidadeEnsino, false, usuarioLogado);
        validarDadosMatricula(matriculaVO.getMatricula(), impressaoContratoVO.getMatriculaVO().getMatricula());
        impressaoContratoVO.setMatriculaVO(matriculaVO);
        impressaoContratoVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(impressaoContratoVO.getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
        validarDadosMatriculaPeriodo(impressaoContratoVO.getMatriculaPeriodoVO());
    }

    public List consultarAluno(String valorConsultaAluno, String campoConsultaAluno, Integer unidadeEnsino, UsuarioVO usuarioLogado) throws Exception {
        List objs = new ArrayList(0);

        if (valorConsultaAluno.equals("")) {
            throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
        }
        if (campoConsultaAluno.equals("matricula")) {
            MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(valorConsultaAluno, unidadeEnsino, NivelMontarDados.BASICO, usuarioLogado);

            if (!obj.getMatricula().equals("")) {
                objs.add(obj);
            }
            return objs;
        }
        if (campoConsultaAluno.equals("nomePessoa")) {
            return objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(valorConsultaAluno, unidadeEnsino, false, usuarioLogado);

        }
        if (campoConsultaAluno.equals("nomeCurso")) {
            return objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(valorConsultaAluno, unidadeEnsino, false, usuarioLogado);
        }
        return objs;
    }

    public void validarDadosMatricula(String matricula, String matriculaDigitada) throws Exception {
        if (matricula.equals("")) {
            throw new Exception("Aluno de matrícula " + matriculaDigitada + " não encontrado ou a situação da matrícula não possibilita a alteração da mesma.");
        }
    }

    public void validarDadosMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodoVO) throws Exception {
        if (matriculaPeriodoVO == null || matriculaPeriodoVO.getCodigo() == 0) {
            throw new Exception("Não foi encontrado nehuma Matricula Periodo para esta Matricula.");
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public String imprimirDeclaracao(Integer textoPadraoDeclaracao, ImpressaoContratoVO impressaoContratoVO, ImpressaoContratoVO impressaoContratoGravarVO, String tipoDeclaracao, TurmaVO turmaVO, DisciplinaVO disciplinaVO, ConfiguracaoGeralSistemaVO configGeralSistema, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioLogado, boolean persistirDocumentoAssinado) throws Exception {
    	String nomeArquivoOrigem = null;
    	try {
			List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade().consultarHistoricoCalculoCoeficienteRendimento(impressaoContratoVO.getMatriculaVO().getMatricula());
			Boolean imprimirContrato = false;			
			TextoPadraoDeclaracaoVO texto = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(textoPadraoDeclaracao, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
			validarDocumentacaoPendente(texto, impressaoContratoVO.getMatriculaVO().getMatricula(), usuarioLogado);
			impressaoContratoVO.setConfiguracaoFinanceiroVO(configuracaoFinanceiroVO);
			impressaoContratoGravarVO.setTurmaVO(turmaVO);
			impressaoContratoGravarVO.setDisciplinaVO(disciplinaVO);
			impressaoContratoGravarVO.setProfessor(impressaoContratoVO.getProfessor());
			impressaoContratoGravarVO.setTextoPadraoDeclaracao(textoPadraoDeclaracao);
			impressaoContratoGravarVO.setUsuarioLogado(usuarioLogado);
			impressaoContratoGravarVO.setRequerimentoVO(impressaoContratoVO.getRequerimentoVO());
			impressaoContratoGravarVO.setHorarioProfessorDiaVO(impressaoContratoVO.getHorarioProfessorDiaVO());
			impressaoContratoGravarVO.setImpressaoDoc(impressaoContratoVO.isImpressaoDoc());
			if (Uteis.isAtributoPreenchido(impressaoContratoVO.getFuncionarioPrincipalVO().getPessoa())) {
				impressaoContratoGravarVO.setFuncionarioPrincipalVO(impressaoContratoVO.getFuncionarioPrincipalVO());	
				impressaoContratoGravarVO.setCargoFuncionarioPrincipal(impressaoContratoVO.getCargoFuncionarioPrincipal());
			}
			if (Uteis.isAtributoPreenchido(impressaoContratoVO.getFuncionarioSecundarioVO().getPessoa())) {
				impressaoContratoGravarVO.setFuncionarioSecundarioVO(impressaoContratoVO.getFuncionarioSecundarioVO());	
				impressaoContratoGravarVO.setCargoFuncionarioSecundario(impressaoContratoVO.getCargoFuncionarioSecundario());
			}
			if (Uteis.isAtributoPreenchido( impressaoContratoVO.getFuncionarioTerceiroVO().getPessoa())) {
				impressaoContratoGravarVO.setFuncionarioTerceiroVO(impressaoContratoVO.getFuncionarioTerceiroVO());	
				impressaoContratoGravarVO.setCargoFuncionarioTerceiro(impressaoContratoVO.getCargoFuncionarioTerceiro());
			}
			impressaoContratoVO.setConfiguracaoFinanceiroVO(configuracaoFinanceiroVO);
			impressaoContratoVO.setConfiguracaoGeralSistemaVO(configGeralSistema);
			if ((!tipoDeclaracao.equals("PR")) && (!tipoDeclaracao.equals("EC")) && (!tipoDeclaracao.equals("AC"))) {
				MatriculaVO matri = impressaoContratoGravarVO.getMatriculaVO();
				getFacadeFactory().getMatriculaFacade().carregarDados(matri, NivelMontarDados.TODOS, usuarioLogado);
//				matri.setAluno(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(matri.getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));				
				getFacadeFactory().getPessoaFacade().carregarDados(matri.getAluno(), usuarioLogado);
				matri.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(matri.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
				matri.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(matri.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuarioLogado));
				matri.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(matri.getTurno().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
				getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(impressaoContratoVO.getMatriculaPeriodoVO(), NivelMontarDados.TODOS, configuracaoFinanceiroVO, usuarioLogado);
				Matricula.montarDadosUnidadeEnsino(matri, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
				if (Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo())) {
					impressaoContratoVO.getMatriculaPeriodoVO().setCondicaoPagamentoPlanoFinanceiroCurso(getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
				}
				impressaoContratoVO.setMatriculaVO(matri);
				impressaoContratoVO.getMatriculaPeriodoVO().setProcessoMatriculaCalendarioVO(getFacadeFactory().getProcessoMatriculaCalendarioFacade().consultarPorMatriculaPeriodoUnidadeEnsinoCurso(
						impressaoContratoVO.getMatriculaPeriodoVO().getCodigo(), matri.getCurso().getCodigo(), matri.getTurno().getCodigo(), 
						matri.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
			}
			imprimirContrato = true;
			impressaoContratoVO.setUsuarioLogado(usuarioLogado);
			if (Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getGradeCurricularAtual())) {
				impressaoContratoVO.getMatriculaVO().setGradeCurricularAtual(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaVO().getGradeCurricularAtual().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
			}
			if (impressaoContratoVO.getDisciplinaVO().getCodigo().intValue() > 0 && !tipoDeclaracao.equals("PR")) {
				Boolean trazerBimestreAnoConclusaoDiscplina = texto.getTexto().contains("BimestreAnoConclusao_Disciplina");
				HistoricoVO hist = getFacadeFactory().getHistoricoFacade().consultaRapidaPorMatricula_Disciplina_Turma(impressaoContratoVO.getMatriculaVO().getMatricula(), impressaoContratoVO.getDisciplinaVO().getCodigo(), impressaoContratoVO.getTurmaVO(), impressaoContratoVO.getMatriculaPeriodoVO().getAno(), impressaoContratoVO.getMatriculaPeriodoVO().getSemestre(), trazerBimestreAnoConclusaoDiscplina, false, usuarioLogado);
				try {
					getFacadeFactory().getHistoricoFacade().carregarDados(hist, NivelMontarDados.TODOS, usuarioLogado);
				} catch (Exception e) {
					throw e;
				}
				getFacadeFactory().getHistoricoFacade().carregarDadosHorarioAulaAluno(hist, true);
				
				Date dataFim = hist.getData();
				Date dataInicio = hist.getDataPrimeiraAula();
				if (dataInicio == null) {
					// impressaoContratoVO.getMatriculaPeriodoVO().setDataInicioAula(null);
					impressaoContratoVO.getDisciplinaVO().setDiaInicio(0);
				} else {
					// impressaoContratoVO.getMatriculaPeriodoVO().setDataInicioAula(dataInicio);
					impressaoContratoVO.getDisciplinaVO().setDiaInicio(Uteis.getDiaMesData(dataInicio));
				}
				impressaoContratoVO.setHistoricoVO(hist);
				if (dataFim == null) {
					impressaoContratoVO.getDisciplinaVO().setDiaFim(0);
					impressaoContratoVO.getDisciplinaVO().setAno("");
					impressaoContratoVO.getDisciplinaVO().setMes("");
					// impressaoContratoVO.getMatriculaPeriodoVO().setDataFinalAula(null);
				} else {
					// impressaoContratoVO.getMatriculaPeriodoVO().setDataFinalAula(dataFim);
					impressaoContratoVO.getDisciplinaVO().setDiaFim(Uteis.getDiaMesData(dataFim));
					impressaoContratoVO.getDisciplinaVO().setAno(String.valueOf(Uteis.getAnoData(dataFim)));
					impressaoContratoVO.getDisciplinaVO().setMes(String.valueOf(Uteis.getMesData(dataFim)));
				}
				
				impressaoContratoVO.getDisciplinaVO().setFrequencia(hist.getFrequencia_Apresentar());
				if(!Uteis.isAtributoPreenchido(hist.getGradeDisciplinaVO())){
					impressaoContratoVO.setGradeDisciplinaVO(hist.getGradeDisciplinaVO());
				}else if(Uteis.isAtributoPreenchido(hist.getGradeCurricularGrupoOptativaDisciplinaVO())){					
					impressaoContratoVO.getGradeDisciplinaVO().setCodigo(hist.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo());
					impressaoContratoVO.getGradeDisciplinaVO().setDisciplina(hist.getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina());
					impressaoContratoVO.getGradeDisciplinaVO().setCargaHoraria(hist.getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria());
					impressaoContratoVO.getGradeDisciplinaVO().setCargaHorariaPratica(hist.getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHorariaPratica());
					impressaoContratoVO.getGradeDisciplinaVO().setCargaHorariaTeorica(hist.getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHorariaTeorica());
					impressaoContratoVO.getGradeDisciplinaVO().setNrCreditos(hist.getGradeCurricularGrupoOptativaDisciplinaVO().getNrCreditos());
					impressaoContratoVO.getGradeDisciplinaVO().setNrCreditoFinanceiro(hist.getGradeCurricularGrupoOptativaDisciplinaVO().getNrCreditoFinanceiro());
					impressaoContratoVO.getGradeDisciplinaVO().setTipoDisciplina(TipoDisciplina.OPTATIVA.getValor());
					impressaoContratoVO.getGradeDisciplinaVO().setModalidadeDisciplina(hist.getGradeCurricularGrupoOptativaDisciplinaVO().getModalidadeDisciplina());
				}else if(Uteis.isAtributoPreenchido(hist.getGradeDisciplinaComposta())){					
					impressaoContratoVO.getGradeDisciplinaVO().setCodigo(hist.getGradeDisciplinaComposta().getCodigo());
					impressaoContratoVO.getGradeDisciplinaVO().setDisciplina(hist.getGradeDisciplinaComposta().getDisciplina());
					impressaoContratoVO.getGradeDisciplinaVO().setCargaHoraria(hist.getGradeDisciplinaComposta().getCargaHoraria());
					impressaoContratoVO.getGradeDisciplinaVO().setCargaHorariaPratica(hist.getGradeDisciplinaComposta().getCargaHorariaPratica());
					impressaoContratoVO.getGradeDisciplinaVO().setCargaHorariaTeorica(hist.getGradeDisciplinaComposta().getCargaHorariaTeorica());
					impressaoContratoVO.getGradeDisciplinaVO().setNrCreditos(hist.getGradeDisciplinaComposta().getNrCreditos());
					impressaoContratoVO.getGradeDisciplinaVO().setNrCreditoFinanceiro(hist.getGradeDisciplinaComposta().getNrCreditoFinanceiro());
					if(Uteis.isAtributoPreenchido(hist.getGradeDisciplinaComposta().getGradeDisciplina())){
						impressaoContratoVO.getGradeDisciplinaVO().setTipoDisciplina(hist.getGradeDisciplinaComposta().getGradeDisciplina().getTipoDisciplina());
					}else{
						impressaoContratoVO.getGradeDisciplinaVO().setTipoDisciplina(TipoDisciplina.OPTATIVA.getValor());
					}
					impressaoContratoVO.getGradeDisciplinaVO().setModalidadeDisciplina(hist.getGradeDisciplinaComposta().getModalidadeDisciplina());
				}else{
					impressaoContratoVO.getGradeDisciplinaVO().setCodigo(hist.getCodigo());
					impressaoContratoVO.getGradeDisciplinaVO().setDisciplina(hist.getDisciplina());
					impressaoContratoVO.getGradeDisciplinaVO().setCargaHoraria(hist.getCargaHorariaDisciplina());
					impressaoContratoVO.getGradeDisciplinaVO().setCargaHorariaPratica(0);
					impressaoContratoVO.getGradeDisciplinaVO().setCargaHorariaTeorica(hist.getCargaHorariaDisciplina());
					impressaoContratoVO.getGradeDisciplinaVO().setNrCreditos(hist.getCreditoDisciplina());
					impressaoContratoVO.getGradeDisciplinaVO().setNrCreditoFinanceiro(hist.getCreditoDisciplina().doubleValue());
					impressaoContratoVO.getGradeDisciplinaVO().setTipoDisciplina(TipoDisciplina.OPTATIVA.getValor());					
					impressaoContratoVO.getGradeDisciplinaVO().setModalidadeDisciplina(hist.getMatriculaPeriodoTurmaDisciplina().getModalidadeDisciplina());
				}						
			}
			try {
				getFacadeFactory().getTurmaAberturaFacade().carregarDados(impressaoContratoVO.getMatriculaPeriodoVO().getTurma(), usuarioLogado);
				impressaoContratoVO.getMatriculaPeriodoVO().setTurma(impressaoContratoVO.getMatriculaPeriodoVO().getTurma());
				// impressaoContratoVO.getMatriculaVO().setCurso(impressaoContratoVO.getTurmaVO().getCurso());
				if (impressaoContratoVO.getMatriculaPeriodoVO().getTurma().getCodigo().intValue() != 0) {
					if (texto.getTexto().contains("DataInicioProgAula_Curso") || texto.getTexto().contains("DataFinalProgAula_Curso")) {
						impressaoContratoVO.getMatriculaPeriodoVO().setDataInicioAula(null);
						impressaoContratoVO.getMatriculaPeriodoVO().setDataFinalAula(null);
						impressaoContratoVO.getListaDisciplinasCursadasOuMinistradas().clear();
						impressaoContratoVO.setListaDisciplinasCursadasOuMinistradas(getFacadeFactory().getHistoricoFacade().consultarDisciplinaCursadasAlunoPorMatricula(impressaoContratoVO.getMatriculaVO().getMatricula(), impressaoContratoVO.getMatriculaVO().getGradeCurricularAtual().getCodigo(), impressaoContratoVO.getDisciplinaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado, 0, true ));
						List<HistoricoVO> historicos = getFacadeFactory().getImpressaoContratoFacade().realizarObtencaoDatasAulaAluno(impressaoContratoVO.getListaDisciplinasCursadasOuMinistradas(), impressaoContratoVO, usuarioLogado, true);
					}
					getFacadeFactory().getTurmaFacade().carregarDados(impressaoContratoVO.getMatriculaPeriodoVO().getTurma(), usuarioLogado);
					getFacadeFactory().getTurmaAberturaFacade().carregarDados(impressaoContratoVO.getMatriculaPeriodoVO().getTurma(), usuarioLogado);
					Iterator i = impressaoContratoVO.getMatriculaPeriodoVO().getTurma().getTurmaAberturaVOs().iterator();
					Date dataInicioAula = new Date();
					while (i.hasNext()) {
						TurmaAberturaVO turmaAbert = (TurmaAberturaVO) i.next();
						if (turmaAbert.getSituacao().equals("IN")) {
							dataInicioAula = turmaAbert.getData();
						}
					}
					Date dataFinalAula = impressaoContratoVO.getMatriculaPeriodoVO().getTurma().getDataPrevisaoFinalizacao();
					if (impressaoContratoVO.getMatriculaPeriodoVO().getDataInicioAula() == null) {
						impressaoContratoVO.getMatriculaPeriodoVO().setDataInicioAula(dataInicioAula);
					}
					if (impressaoContratoVO.getMatriculaPeriodoVO().getDataFinalAula() == null) {
						impressaoContratoVO.getMatriculaPeriodoVO().setDataFinalAula(dataFinalAula);
					}
				}
			} catch (Exception e) {
			}
			if (texto.getTexto().contains("AnoSemestrePrevisaoTerminoCurso_Matricula")) {
                            List periodosLetivosGrade = getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivos(impressaoContratoVO.getMatriculaPeriodoVO().getGradeCurricular().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado);
                            impressaoContratoVO.getMatriculaPeriodoVO().getGradeCurricular().setPeriodoLetivosVOs(periodosLetivosGrade);
                        }
			if (texto.getTexto().contains("ListaDisciplinasCursadasOuMinistradas_Disciplina")
					|| texto.getTexto().contains("ListaDisciplinasHistoricoCertificado_Disciplina")
					|| texto.getTexto().contains("ListaDisciplinasHistoricoDiploma_Disciplina")
					|| texto.getTexto().contains("ListaDisciplinasPeriodoLetivoAtual_Disciplina")
					|| texto.getTexto().contains("ListaDisciplinasAprovadasCertificado_Disciplina")
					|| texto.getTexto().contains("ListaDisciplinasNotaFrequenciaCargaHorariaProfessorSituacao_Disciplina")
					|| texto.getTexto().contains("ListaDisciplinasHistoricoPeriodo_Disciplina")
                    || texto.getTexto().contains("ListaDisciplinasVersoDiploma_Disciplina")
                    || texto.getTexto().contains("CargaHorariaDisciplinasCursadasOuMinistradas_Disciplina")){
				if (tipoDeclaracao.equals("PR")) {
					// disciplinas professor
					try {
						List<GradeDisciplinaVO> lista = getFacadeFactory().getGradeDisciplinaFacade().consultarDisciplinaMinistrouHorarioProfessorPorCodigo(impressaoContratoVO.getProfessor().getPessoa().getCodigo(), impressaoContratoVO.getMatriculaPeriodoVO().getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, usuarioLogado);
						for (GradeDisciplinaVO gradeDisciplinaVO : lista) {
							HistoricoVO hist = new HistoricoVO();
							hist.setDisciplina(gradeDisciplinaVO.getDisciplina());
							hist.setGradeDisciplinaVO(gradeDisciplinaVO);
							impressaoContratoVO.getListaDisciplinasCursadasOuMinistradas().add(hist);	
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (tipoDeclaracao.equals("AL") || tipoDeclaracao.equals("RE") || tipoDeclaracao.equals("TU") || tipoDeclaracao.equals("PF")) {
					// disciplinas aluno
					try {
						impressaoContratoVO.getMatriculaPeriodoVO().setDataInicioAula(null);
						impressaoContratoVO.getMatriculaPeriodoVO().setDataFinalAula(null);
						impressaoContratoVO.setListaDisciplinasCursadasOuMinistradas(getFacadeFactory().getHistoricoFacade().consultarDisciplinaCursadasAlunoPorMatricula(impressaoContratoVO.getMatriculaVO().getMatricula(), impressaoContratoVO.getMatriculaVO().getGradeCurricularAtual().getCodigo(), impressaoContratoVO.getDisciplinaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado, 0, false));
						impressaoContratoVO.setListaDisciplinasPeriodoLetivoAtual(getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoLetivoAtualImpressaoDeclaracao(impressaoContratoVO.getMatriculaPeriodoVO().getCodigo(), true, impressaoContratoVO.getDisciplinaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado, impressaoContratoVO.getMatriculaVO().getGradeCurricularAtual().getCodigo()));
						List<HistoricoVO> historicos = getFacadeFactory().getImpressaoContratoFacade().realizarObtencaoDatasAulaAluno(impressaoContratoVO.getListaDisciplinasCursadasOuMinistradas(), impressaoContratoVO, usuarioLogado, false);
						getFacadeFactory().getHistoricoFacade().carregarDadosProfessorTitularTitulacao((List<HistoricoVO>)impressaoContratoVO.getListaDisciplinasCursadasOuMinistradas());
						getFacadeFactory().getHistoricoFacade().carregarDadosProfessorTitularTitulacao((List<HistoricoVO>)impressaoContratoVO.getListaDisciplinasPeriodoLetivoAtual());
						Iterator i = impressaoContratoVO.getListaDisciplinasCursadasOuMinistradas().iterator();
						while (i.hasNext()) {
							HistoricoVO hist = (HistoricoVO) i.next();
							try {
								carregarDadosDataInicioFimAula(impressaoContratoVO, usuarioLogado, hist);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
						if (texto.getTexto().contains("ListaDisciplinasAprovadasCertificado_Disciplina") || texto.getTexto().contains("ListaDisciplinasNotaFrequenciaCargaHorariaProfessorSituacao_Disciplina")) {
							List<HistoricoVO> lista = impressaoContratoVO.getListaDisciplinasCursadasOuMinistradas().stream().filter(HistoricoVO::getAprovado).collect(Collectors.toList());
							impressaoContratoVO.setListaDisciplinasCursadasOuMinistradas(lista);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if (texto.getTexto().contains("NumeroProcessoExpedicaoDiploma_Matricula") || texto.getTexto().contains("LivroRegistroDiploma_Aluno") || texto.getTexto().contains("NumeroProcessoDiploma_Aluno") || texto.getTexto().contains("NumeroRegistroDiploma_Aluno") || texto.getTexto().contains("DataPublicacaoDiploma_Aluno") || texto.getTexto().contains("FolhaRegistroDiploma_Aluno") || texto.getTexto().contains("SerialExpedicaoDiploma_Matricula") || tipoDeclaracao.equals("LD")) {
				impressaoContratoVO.setControleLivroFolhaReciboVO(getFacadeFactory().getControleLivroFolhaReciboFacade().consultarPorMatriculaMatriculaMaiorVia(impressaoContratoVO.getMatriculaVO().getMatricula(), TipoLivroRegistroDiplomaEnum.DIPLOMA, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
				impressaoContratoVO.setExpedicaoDiplomaVO(getFacadeFactory().getExpedicaoDiplomaFacade().consultarPorMatricula(impressaoContratoVO.getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
			}
			if (!Uteis.isAtributoPreenchido(impressaoContratoVO.getControleLivroFolhaReciboVO()) && (texto.getTexto().contains("LivroRegistroCertificado_Aluno") || texto.getTexto().contains("NumeroProcessoDiploma_Aluno") || texto.getTexto().contains("NumeroRegistroCertificado_Aluno") || texto.getTexto().contains("DataPublicacaoCertificado_Aluno") || texto.getTexto().contains("FolhaRegistroCertificado_Aluno"))) {
				impressaoContratoVO.setControleLivroFolhaReciboVO(getFacadeFactory().getControleLivroFolhaReciboFacade().consultarPorMatriculaMatriculaMaiorVia(impressaoContratoVO.getMatriculaVO().getMatricula(), TipoLivroRegistroDiplomaEnum.CERTIFICADO, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
				impressaoContratoVO.setExpedicaoDiplomaVO(getFacadeFactory().getExpedicaoDiplomaFacade().consultarPorMatricula(impressaoContratoVO.getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
			}
			if (Uteis.isAtributoPreenchido(impressaoContratoVO.getExpedicaoDiplomaVO())) {
				impressaoContratoVO.setObservacaoComplementarDiplomaVOs(impressaoContratoVO.getExpedicaoDiplomaVO().getObservacaoComplementarDiplomaVOs());
				impressaoContratoVO.getObservacaoComplementarDiplomaVOs()
					.stream().filter(obs -> obs.getObservacaoComplementar().getReapresentarNomeAluno())
					.forEach(obs -> obs.getObservacaoComplementar().setNomeAlunoRepresentar(Uteis.alterarpreposicaoParaMinusculo(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(impressaoContratoVO.getMatriculaVO().getAluno().getNome().toLowerCase()))));
				if (!impressaoContratoVO.getExpedicaoDiplomaVO().getVia().equals("1")) {
					impressaoContratoVO.setPrimeiraViaExpedicaoDiplomaVO(getFacadeFactory().getExpedicaoDiplomaFacade().consultarPorMatriculaPrimeiraViaMontandoFuncionarioCargos(impressaoContratoVO.getMatriculaVO().getMatricula(), 
							impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
				}
			}
			if (texto.getTexto().contains("ListaEnade_Aluno")) {
				if (impressaoContratoVO.getMatriculaVO().getCurso().getNivelEducacional().equals("SU")) {
					impressaoContratoVO.getMatriculaVO().setMatriculaEnadeVOs(getFacadeFactory().getMatriculaEnadeFacade().consultarMatriculaEnadePorMatricula(impressaoContratoVO.getMatriculaVO().getMatricula(), usuarioLogado, false));
				}
			}
			if (texto.getTexto().contains("Trancamento")) {
				List lista = getFacadeFactory().getTrancamentoFacade().consultaRapidaPorMatricula(impressaoContratoVO.getMatriculaVO().getMatricula(), impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().getCodigo(), false, usuarioLogado);
				if (!lista.isEmpty()) {
					impressaoContratoVO.setTrancamentoVO((TrancamentoVO)lista.get(0));
				}
			}
			if (texto.getTexto().contains("PrevisaoConclusao_Curso")) {
				impressaoContratoVO.setQuantidadePeriodoLetivoACursar(getFacadeFactory().getPeriodoLetivoFacade().consultarQuantidadePeriodoLetivoACursar(impressaoContratoVO.getMatriculaVO().getMatricula(), usuarioLogado));
				Uteis.checkState(!Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaPeriodoVO().getGradeCurricular().getCodigo()) && !Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getMatricula()) && !Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaPeriodoVO().getPeriodoLetivo().getCodigo()), "É necessário informar o Período Letivo, a Matricula, e a Grade Curricular do aluno para listar as Disciplinas Pendentes.");
				impressaoContratoVO.setQuantidadeDisciplinasNaoCursadas(getFacadeFactory().getDisciplinaFacade().consultarDisciplinasNaoCumpridasDaGrade(impressaoContratoVO.getMatriculaVO().getMatricula(), impressaoContratoVO.getMatriculaPeriodoVO().getGradeCurricular().getCodigo(), impressaoContratoVO.getMatriculaPeriodoVO().getPeriodoLetivo().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado).size());
			}
			if (texto.getTexto().contains("Duracao_Curso")  && Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getGradeCurricularAtual().getCodigo())) {
				impressaoContratoVO.getMatriculaPeriodoVO().setGradeCurricular(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaVO().getGradeCurricularAtual().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
			}
			AutorizacaoCursoVO primeiroReconhecimentoCursoVO = new AutorizacaoCursoVO();
			AutorizacaoCursoVO renovacaoReconhecimentoCursoVO = new AutorizacaoCursoVO();
			if (texto.getTexto().contains("PrimeiroReconhecimento_Curso") || texto.getTexto().contains("DataPrimeiroReconhecimento_Curso")) {
				if (Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getAutorizacaoCurso())) {
					primeiroReconhecimentoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaVO().getAutorizacaoCurso().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX);
				} else if (!Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getAutorizacaoCurso())) {
					primeiroReconhecimentoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoDataMaisAntigo(impressaoContratoVO.getMatriculaVO().getCurso().getCodigo(), new Date(), Uteis.NIVELMONTARDADOS_COMBOBOX);
				}
				if (texto.getTexto().contains("_ProgramacaoFormatura")) {
					primeiroReconhecimentoCursoVO = impressaoContratoVO.getPrimeiroReconhecimentoCurso();
					primeiroReconhecimentoCursoVO.setNome(impressaoContratoVO.getPrimeiroReconhecimentoCurso().getNome());
				}
				impressaoContratoVO.setPrimeiroReconhecimentoCurso(primeiroReconhecimentoCursoVO);
			}
			Date dataPRM = Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getDataConclusaoCurso()) ? impressaoContratoVO.getMatriculaVO().getDataConclusaoCurso() : impressaoContratoVO.getMatriculaVO().getData();
			if (texto.getTexto().contains("RenovacaoReconhecimento_Curso") || texto.getTexto().contains("DataRenovacaoReconhecimento_Curso")) {
				if (Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getRenovacaoReconhecimentoVO())) {
					renovacaoReconhecimentoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaVO().getRenovacaoReconhecimentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX);
				} else {
					renovacaoReconhecimentoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoDataVigenteMatricula(impressaoContratoVO.getMatriculaVO().getCurso().getCodigo(), dataPRM, Uteis.NIVELMONTARDADOS_COMBOBOX);
					if (!Uteis.isAtributoPreenchido(renovacaoReconhecimentoCursoVO)) {
						if (impressaoContratoVO.getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao()) {
							renovacaoReconhecimentoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoPos(impressaoContratoVO.getMatriculaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX);
						} else if (!impressaoContratoVO.getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao()) {
							renovacaoReconhecimentoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoDataVigenteMatricula(impressaoContratoVO.getMatriculaVO().getCurso().getCodigo(), new Date(), Uteis.NIVELMONTARDADOS_COMBOBOX);
						}
					}
				}
				if (texto.getTexto().contains("_ProgramacaoFormatura")) {
					renovacaoReconhecimentoCursoVO = impressaoContratoVO.getRenovacaoReconhecimentoCurso();
					renovacaoReconhecimentoCursoVO.setNome(impressaoContratoVO.getRenovacaoReconhecimentoCurso().getNome());
				}
				impressaoContratoVO.setRenovacaoReconhecimentoCurso(renovacaoReconhecimentoCursoVO);
			}
			if (texto.getTexto().contains("CargaHoraria_Curso")) {
				impressaoContratoVO.getMatriculaPeriodoVO().setGradeCurricular(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaPeriodoVO().getGradeCurricular().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
			}
			if (texto.getTexto().contains("CoeficienteRendimentoGeral_Aluno")) {
				if (Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getCurso().getConfiguracaoAcademico().getFormulaCoeficienteRendimento())) {
					impressaoContratoVO.setCoeficienteRendimentoGeralAluno(getFacadeFactory().getConfiguracaoAcademicoFacade().realizarCalculoCoeficienteRendimento(impressaoContratoVO.getMatriculaVO().getCurso().getConfiguracaoAcademico().getFormulaCoeficienteRendimento(), historicoVOs));
				} else {
				impressaoContratoVO.setCoeficienteRendimentoGeralAluno(getFacadeFactory().getHistoricoFacade().calcularCoeficienteRendimentoGeralAluno(impressaoContratoVO.getMatriculaVO().getMatricula()));
				}
			}
			if (texto.getTexto().contains("CoeficienteRendimentoPeriodoLetivo_Aluno")) {
				if (Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getCurso().getConfiguracaoAcademico().getFormulaCoeficienteRendimento())) {
					impressaoContratoVO.setCoeficienteRendimentoGeralAluno(getFacadeFactory().getConfiguracaoAcademicoFacade().realizarCalculoCoeficienteRendimento(impressaoContratoVO.getMatriculaVO().getCurso().getConfiguracaoAcademico().getFormulaCoeficienteRendimento(), historicoVOs));
				} else {
				impressaoContratoVO.setCoeficienteRendimentoPeriodoLetivoAluno(getFacadeFactory().getHistoricoFacade().calcularCoeficienteRendimentoPeriodoLetivoAluno(impressaoContratoVO.getMatriculaVO().getMatricula(), impressaoContratoVO.getMatriculaPeriodoVO().getAno(), impressaoContratoVO.getMatriculaPeriodoVO().getSemestre()));
				}
			}
			if (texto.getTexto().contains("ListaDisciplinaCertificadoEnsinoMedio_Disciplina")) {
				impressaoContratoVO.setListaDisciplinaCertificadoEnsinoMedio(getFacadeFactory().getHistoricoFacade().consultaRapidaPorMatricula(impressaoContratoVO.getMatriculaVO().getMatricula(), impressaoContratoVO.getMatriculaPeriodoVO().getGradeCurricular().getCodigo(), OrdemHistoricoDisciplina.ANO_SEMESTRE.getValor(), false, impressaoContratoVO.getDisciplinaVO().getCodigo(), false, NivelMontarDados.BASICO, false, false, usuarioLogado));
	        	getFacadeFactory().getHistoricoFacade().carregarDadosProfessorTitularTitulacao(impressaoContratoVO.getListaDisciplinaCertificadoEnsinoMedio());
			}
	        if (texto.getTexto().contains("SituacaoFinalPeriodoLetivo_Matricula")) {
	        	List<BoletimAcademicoRelVO> boletimAcademicoRelVOs = getFacadeFactory().getBoletimAcademicoRelFacade().criarObjeto(impressaoContratoVO.getMatriculaVO(), "BoletimAcademicoEnsinoMedioRel", true, impressaoContratoVO.getMatriculaPeriodoVO().getAno()+impressaoContratoVO.getMatriculaPeriodoVO().getSemestre(), impressaoContratoVO.getMatriculaPeriodoVO().getTurma(), impressaoContratoVO.getMatriculaVO().getUnidadeEnsino(), null, false, usuarioLogado, false, false, null, null, null, null, null, null, false, true, false, false, new FiltroRelatorioAcademicoVO(), impressaoContratoVO.getMatriculaVO().getGradeCurricularAtual(), false, false, new ArrayList<>(), null);
	        	if (!boletimAcademicoRelVOs.isEmpty()) {
	        		impressaoContratoVO.setSituacaoFinalPeriodoLetivo(boletimAcademicoRelVOs.get(0).getSituacaoFinal());
	        	}
	        }
			if (texto.getTexto().contains("DataPublicacao_Curso") || texto.getTexto().contains("BaseLegal_Curso")) {
				impressaoContratoVO.setAutorizacaoCurso(getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoDataVigenteMatricula(impressaoContratoVO.getMatriculaVO().getCurso().getCodigo(), impressaoContratoVO.getMatriculaVO().getData(), Uteis.NIVELMONTARDADOS_DADOSBASICOS));
			}
							
			/* Monta Plano Disciplina */
			if(texto.getTexto().contains("ListaPlanoEnsinoDisciplina_Aluno")) {
				impressaoContratoVO.setListaPlanoEnsino(new ArrayList<>());
				if (Uteis.isAtributoPreenchido(impressaoContratoVO.getDisciplinaVO().getCodigo())) {
					PlanoEnsinoVO pl = getFacadeFactory().getPlanoEnsinoFacade().consultarPorDisciplinaMatriculaAluno(impressaoContratoVO.getDisciplinaVO().getCodigo(), impressaoContratoVO.getMatriculaVO().getMatricula() , false, new HistoricoVO(), usuarioLogado);
					if (Uteis.isAtributoPreenchido(pl)) {
						GradeDisciplinaVO gradeDisciplinaVO = getFacadeFactory().getGradeDisciplinaFacade().consultarPorGradeCurricularEDisciplina(impressaoContratoVO.getMatriculaVO().getGradeCurricularAtual().getCodigo(), impressaoContratoVO.getDisciplinaVO().getCodigo(), usuarioLogado, null);
						pl.setTotalCargaHoraria(new BigDecimal(gradeDisciplinaVO.getCargaHoraria()));
						impressaoContratoVO.getListaPlanoEnsino().add(pl);
					}
				} else {
					GradeCurricularVO gradeCurricular = impressaoContratoVO.getMatriculaVO().getGradeCurricularAtual();
					List<PeriodoLetivoVO> listaPeriodoLetivo = getFacadeFactory().getPeriodoLetivoFacade().consultarPorGradeCurricular(gradeCurricular.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
					for(PeriodoLetivoVO periodoLetivoVO : listaPeriodoLetivo ) {
						for(GradeDisciplinaVO gradeDisciplinaVO : periodoLetivoVO.getGradeDisciplinaVOs()) {
							PlanoEnsinoVO pl = getFacadeFactory().getPlanoEnsinoFacade().consultarPorDisciplinaMatriculaAluno(gradeDisciplinaVO.getDisciplina().getCodigo(), impressaoContratoVO.getMatriculaVO().getMatricula() , false, new HistoricoVO(), usuarioLogado);
							if(Uteis.isAtributoPreenchido(pl)) {
								pl.setTotalCargaHoraria(new BigDecimal(gradeDisciplinaVO.getCargaHoraria()));
								impressaoContratoVO.getListaPlanoEnsino().add(pl);
							}
						}	
					}	
				}
			}
			/* Finaliza Plano Disciplina */
			
			/* Monta ListaDisciplinasHistoricoPeriodoLetivoSituacao_Disciplina */
			if (texto.getTexto().contains("ListaDisciplinasHistoricoPeriodoLetivoSituacao_Disciplina")) {
				impressaoContratoVO.getListaDisciplinasHistoricoPeriodoLetivoSituacao().clear();
				impressaoContratoVO.setListaDisciplinasHistoricoPeriodoLetivoSituacao(getFacadeFactory().getHistoricoFacade().consultarDisciplinaCursadasPeriodoLetivoAlunoPorMatricula(impressaoContratoVO.getMatriculaVO().getMatricula(), impressaoContratoVO.getMatriculaVO().getGradeCurricularAtual().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado, 0 ));			
				if (Uteis.isAtributoPreenchido(impressaoContratoVO.getDisciplinaVO().getCodigo())) {
					for(HistoricoVO obj : impressaoContratoVO.getListaDisciplinasCursadasOuMinistradas()){
						if (obj.getDisciplina().getCodigo().equals(impressaoContratoVO.getDisciplinaVO().getCodigo())) {
							impressaoContratoVO.getListaDisciplinasHistoricoPeriodoLetivoSituacao().clear();
							impressaoContratoVO.getListaDisciplinasHistoricoPeriodoLetivoSituacao().add(obj);
							break;
						}
					}
					
				}
				impressaoContratoGravarVO.setListaDisciplinasHistoricoPeriodoLetivoSituacao(impressaoContratoVO.getListaDisciplinasHistoricoPeriodoLetivoSituacao());
			}
			/* Finaliza ListaDisciplinasHistoricoPeriodoLetivoSituacao_Disciplina */
			String textoStr = "";
			impressaoContratoVO.getListaDisciplinasCursadasOuMinistradas().sort(Comparator.comparing(HistoricoVO::getDateDataInicioAula , Comparator.nullsFirst(Comparator.naturalOrder())));
			
			if (texto.getTexto().contains("HorarioTurno_Matricula")) {
				HashMap<String, String> menorHorarioMaiorHorarioAluno = getFacadeFactory().getHorarioTurmaDiaItemFacade().consultarMenorHorarioMaiorHorarioProgramacaoAulaDisciplinasAluno(impressaoContratoVO.getMatriculaVO().getMatricula(),impressaoContratoVO.getMatriculaPeriodoVO().getAno(), impressaoContratoVO.getMatriculaPeriodoVO().getSemestre(), false, usuarioLogado);
				if (menorHorarioMaiorHorarioAluno.containsKey("min")) {
					impressaoContratoVO.setMenorHorarioAluno(menorHorarioMaiorHorarioAluno.get("min"));
				}
				if (menorHorarioMaiorHorarioAluno.containsKey("max")) {
					impressaoContratoVO.setMaiorHorarioAluno(menorHorarioMaiorHorarioAluno.get("max"));
				}
			}
			
			if (texto.getTexto().contains("MediaGlobal_Matricula")) {
				impressaoContratoVO.setMediaGlobal(getFacadeFactory().getHistoricoFacade().consultarMediaAprovadasReprovadasAlunoPorMatricula(impressaoContratoVO.getMatriculaVO().getMatricula(), impressaoContratoVO.getMatriculaVO().getGradeCurricularAtual().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
                
            }
			
			if (texto.getTexto().contains("UrlFoto_Aluno")) {
				getFacadeFactory().getPessoaFacade().carregarUrlFotoAluno(configGeralSistema, impressaoContratoVO.getMatriculaVO().getAluno());
			}
			if (texto.getTexto().contains("NomeDocenteResponsavelAssinaturaTermoEstagio_Curso") || texto.getTexto().contains("CpfDocenteResponsavelAssinaturaTermoEstagio_Curso") || texto.getTexto().contains("EmailDocenteResponsavelAssinaturaTermoEstagio_Curso")) {
				impressaoContratoVO.getMatriculaVO().getCurso().setFuncionarioResponsavelAssinaturaTermoEstagioVO(getFacadeFactory().getFuncionarioFacade().consultaFuncionarioResponsavelAssinaturaTermoEstagioRapidaPorCurso(impressaoContratoVO.getMatriculaVO().getCurso().getCodigo(), usuarioLogado));
			}
			carregarTotalSemestresCurso(impressaoContratoVO.getMatriculaPeriodoVO().getGradeCurricular(), texto.getTexto(), usuarioLogado);
			if (Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getMatricula()) 
					&& (texto.getTexto().contains("Nr_Periodo_Letivo_Ingresso_Matricula") 
							|| texto.getTexto().contains("Nr_Extenso_Periodo_Letivo_Ingresso_Matricula") 
							|| texto.getTexto().contains("Descricao_Periodo_Letivo_Ingresso_Matricula") 
							|| texto.getTexto().contains("Nome_Certificacao_Periodo_Letivo_Ingresso_Matricula"))) {
				impressaoContratoVO.setPeriodoLetivoIngresso(getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivoIngressoPorMatricula(impressaoContratoVO.getMatriculaVO().getMatricula(), usuarioLogado));
			}
			if (texto.getTexto().contains("EmailInstitucional_Aluno")) {
				impressaoContratoVO.setPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(impressaoContratoVO.getMatriculaVO().getAluno().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
			}
			if (texto.getTexto().contains("matriculaEnadeData_Matricula")) {
				impressaoContratoVO.setMatriculaEnadeDataVO(getFacadeFactory().getMatriculaEnadeFacade().consultarMatriculaEnadePorData(impressaoContratoVO.getMatriculaVO().getMatricula(), usuarioLogado, true));
			}
			if (tipoDeclaracao.equals("AC")) {
				if (texto.getTexto().contains("NomeCurso_ProgramacaoFormatura") || texto.getTexto().contains("NomePresidenteMesaColacaoGrau_ProgramacaoFormatura") 
						|| texto.getTexto().contains("NomeSecretariaAcademicaColacaoGrau_ProgramacaoFormatura") 
						|| texto.getTexto().contains("NomeSecretariaAcademicaColacaoGrau_ProgramacaoFormatura") 
						|| texto.getTexto().contains("ListaProgramacaoFormaturaAluno_ProgramacaoFormatura")) {
					impressaoContratoVO.getProgramacaoFormaturaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(impressaoContratoVO.getProgramacaoFormaturaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuarioLogado));
					impressaoContratoVO.getProgramacaoFormaturaVO().setColacaoGrauVO(getFacadeFactory().getColacaoGrauFacade().consultarPorChavePrimaria(impressaoContratoVO.getProgramacaoFormaturaVO().getColacaoGrauVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
					if(!Uteis.isAtributoPreenchido(impressaoContratoVO.getProgramacaoFormaturaAlunoVOs())) {
						impressaoContratoVO.setProgramacaoFormaturaAlunoVOs(getFacadeFactory().getProgramacaoFormaturaAlunoFacade().consultarProgramacaoFormaturaAlunoPorCurso(impressaoContratoVO.getProgramacaoFormaturaVO().getCodigo(), impressaoContratoVO.getProgramacaoFormaturaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado));	
					}
					String dataComple = Uteis.getData(impressaoContratoVO.getProgramacaoFormaturaVO().getColacaoGrauVO().getData(), "dd/MM/yyyy");
					String dia = dataComple.replaceAll("/", "").substring(0, 2);
					String mes = dataComple.replaceAll("/", "").substring(2, 4);
					String ano = dataComple.replaceAll("/", "").substring(4, 8);
					String dataDescrita = Uteis.getDataCidadeDiaPorExtensoMesPorExtensoEAnoPorExtenso("", impressaoContratoVO.getProgramacaoFormaturaVO().getColacaoGrauVO().getData(), false);
					String dataMesDescrita = Uteis.getMesReferenciaExtenso(mes);
					String horaColacao =  impressaoContratoVO.getProgramacaoFormaturaVO().getColacaoGrauVO().executarMontagemHorarioColacaoPorExtenso(impressaoContratoVO.getProgramacaoFormaturaVO().getColacaoGrauVO().getHorario());
					impressaoContratoVO.getProgramacaoFormaturaVO().getColacaoGrauVO().setDataMesDescrita(dia + " de " + dataMesDescrita + " de " + ano);
					impressaoContratoVO.getProgramacaoFormaturaVO().getColacaoGrauVO().setDataHoraDescrita(dataDescrita + (horaColacao.trim().equals("") ? "" : " as " + horaColacao + " horas "));
				}
			}
			if (texto.getTexto().contains("DataColacaoGrau_Matricula")) {
				impressaoContratoVO.getMatriculaVO().setDataColacaoGrau(getFacadeFactory().getMatriculaFacade().obterDataColacaoGrauMatricula(impressaoContratoVO.getMatriculaVO().getMatricula()));
			}
			if(texto.getTexto().contains("DataCancelamento_Matricula") || texto.getTexto().contains("DataCancelamentoPorExtenso_Matricula")){
				impressaoContratoVO.setCancelamentoVO(getFacadeFactory().getCancelamentoFacade().consultarPorMatricula(impressaoContratoVO.getMatriculaVO().getMatricula(), Uteis.NIVELMONTARDADOS_COMBOBOX, null, usuarioLogado));
			}
			if(texto.getTexto().contains("DataTrancamento_Matricula") || texto.getTexto().contains("DataTrancamentoPorExtenso_Matricula")){
				impressaoContratoVO.getTrancamentoVO().setData(getFacadeFactory().getTrancamentoFacade().consultarDataTrancamentoPorMatricula(impressaoContratoVO.getMatriculaVO().getMatricula(), usuarioLogado,true));
			}
			if(texto.getTexto().contains("DataTransferencia_Matricula") || texto.getTexto().contains("DataTransferenciaPorExtenso_Matricula")){
				impressaoContratoVO.getTransferenciaEntradaVO().setData(getFacadeFactory().getTransferenciaEntradaFacade().consultarDataTransferenciaExternaPorMatricula(impressaoContratoVO.getMatriculaVO().getMatricula(), usuarioLogado));
			}
			if (texto.getTexto().contains("Requerimento_TCC")) {
				impressaoContratoVO.getRequerimentoVO().setGrupoFacilitador(getFacadeFactory().getSalaAulaBlackboardFacade().consultarPorChavePrimaria(impressaoContratoVO.getRequerimentoVO().getGrupoFacilitador().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS));
			}
			texto.substituirTag(impressaoContratoVO, usuarioLogado);
			if (impressaoContratoGravarVO.getControleGeracaoAssinatura().equals("NAO_GERAR_DOC_ASSINADO")) {
				texto.setAssinarDigitalmenteTextoPadrao(false);
			}
			textoStr = getFacadeFactory().getTextoPadraoDeclaracaoFacade().removerBordaDaPagina(texto.getTexto());
			textoStr = getFacadeFactory().getTextoPadraoDeclaracaoFacade().adicionarStyleFormatoPaginaTextoPadrao(textoStr, texto.getOrientacaoDaPagina());

			if ((!impressaoContratoVO.isImpressaoPdf() && !impressaoContratoVO.isImpressaoDoc()) || (impressaoContratoVO.isImpressaoDoc() && texto.getTipoDesigneTextoEnum().equals(TipoDesigneTextoEnum.HTML))) {
				HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
				request.getSession().setAttribute("textoRelatorio", textoStr);
				nomeArquivoOrigem = "DECLARACAO_" + Uteis.getData(new Date(), "dd_MM_yy_HH_mm_ss") + "_" + usuarioLogado.getCodigo() + ".html";
			} else {
				nomeArquivoOrigem = executarValidacaoImpressaoEmPdf(impressaoContratoGravarVO, texto, textoStr, persistirDocumentoAssinado, configGeralSistema, usuarioLogado);
			}
						
			impressaoContratoVO.setImpressaoContratoExistente(impressaoContratoGravarVO.getImpressaoContratoExistente());
			impressaoContratoVO.setDocumentoAssinado(impressaoContratoGravarVO.getDocumentoAssinado());			
			if (impressaoContratoVO.getImpressaoRequerimento()) {
				if (Uteis.isAtributoPreenchido(impressaoContratoVO.getRequerimentoVO())) {
					if (persistirDocumentoAssinado && impressaoContratoVO.getGerarNovoArquivoAssinado()) {
						gravarImpressaoContrato(impressaoContratoGravarVO);
					}
				}
			} else {
				if(impressaoContratoVO.getGravarImpressaoContrato()) {
					gravarImpressaoContrato(impressaoContratoGravarVO);
				}
			}
			impressaoContratoGravarVO = null;
		} catch (Exception e) {
			throw e;
		}
		return nomeArquivoOrigem;
	}
    
    @Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
    	
    public String executarValidacaoImpressaoEmPdf(ImpressaoContratoVO obj, TextoPadraoLayoutVO texto, String textoStr, boolean persistirDocumentoAssinado, ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuarioLogado) throws Exception {    	
		if(persistirDocumentoAssinado && !texto.getTipo().equals("AC")) {
	    	ImpressaoContratoVO impressaoContratoExistente = consultarSeJaExisteImpressaoDeclaracaoParaTextoPadrao(obj, texto.getCodigo());		
			File file = new File(configGeralSistema.getLocalUploadArquivoFixo() + File.separator + impressaoContratoExistente.getDocumentoAssinado().getArquivo().getPastaBaseArquivo() + File.separator + impressaoContratoExistente.getDocumentoAssinado().getArquivo().getNome());		
			if (texto.getTipoDesigneTextoEnum().isPdf() && Uteis.isAtributoPreenchido(impressaoContratoExistente.getDocumentoAssinado()) && !obj.getGerarNovoArquivoAssinado() && file.exists()) {
				if(impressaoContratoExistente.getDocumentoAssinado().getArquivo().getPastaBaseArquivo().startsWith(configGeralSistema.getLocalUploadArquivoFixo())) {
					getFacadeFactory().getArquivoHelper().disponibilizarArquivoAssinadoParaDowload(impressaoContratoExistente.getDocumentoAssinado().getArquivo().getPastaBaseArquivo() + File.separator + impressaoContratoExistente.getDocumentoAssinado().getArquivo().getNome(), impressaoContratoExistente.getDocumentoAssinado().getArquivo().getNome());
				}else {
					getFacadeFactory().getArquivoHelper().disponibilizarArquivoAssinadoParaDowload(configGeralSistema.getLocalUploadArquivoFixo() + File.separator + impressaoContratoExistente.getDocumentoAssinado().getArquivo().getPastaBaseArquivo() + File.separator + impressaoContratoExistente.getDocumentoAssinado().getArquivo().getNome(), impressaoContratoExistente.getDocumentoAssinado().getArquivo().getNome());
				}
				obj.setDocumentoAssinado(impressaoContratoExistente.getDocumentoAssinado());
				obj.setImpressaoContratoExistente(true);
				return impressaoContratoExistente.getDocumentoAssinado().getArquivo().getNome();
			}
		}
		if (Uteis.isAtributoPreenchido(obj.getRequerimentoVO())) {
			if (texto.getTipoDesigneTextoEnum().isHtml() && obj.getRequerimentoVO().getTipoRequerimento().getAssinarDigitalmenteDeclaracoesGeradasNoRequerimento()) {
				return converterHtmlParaPDFComAssinaturaDigital(textoStr, obj, texto, configGeralSistema, usuarioLogado);
			} else if (texto.getTipoDesigneTextoEnum().isHtml() && !obj.getRequerimentoVO().getTipoRequerimento().getAssinarDigitalmenteDeclaracoesGeradasNoRequerimento()) {
				return executarConversaoHtmlParaPdf(textoStr, texto, usuarioLogado);
			} else {
				return executarImpressaoPorTextoPadraoIreport(texto, obj, persistirDocumentoAssinado, configGeralSistema, usuarioLogado);
			}
		} else if (texto.getTipoDesigneTextoEnum().isHtml()) {
			return executarConversaoHtmlParaPdf(textoStr, texto, usuarioLogado);
		} else {
			return executarImpressaoPorTextoPadraoIreport(texto, obj, persistirDocumentoAssinado, configGeralSistema, usuarioLogado);
		}
	}

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	private String executarImpressaoPorTextoPadraoIreport(TextoPadraoLayoutVO textoLayout, ImpressaoContratoVO impressaoContratoVO, boolean persistirDocumentoAssinado, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception {
		GeradorRelatorio geradorRelatorio = null;
		SuperParametroRelVO superParamentro = null;
		String caminhoRelatorio = null;
		try {
			if (!Uteis.isAtributoPreenchido(textoLayout.getArquivoIreport().getDescricao())) {
				throw new ConsistirException("O campo ARQUIVO IREPORT (Texto Padrao) deve ser informado.");
			}			
			superParamentro = new SuperParametroRelVO();
			if(impressaoContratoVO.isImpressaoDoc()) {
				superParamentro.setTipoRelatorioEnum(TipoRelatorioEnum.DOC);
			}else {				
				superParamentro.setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				impressaoContratoVO.setImpressaoPdf(true);
			}
			String caminhoArquivo = (!textoLayout.getArquivoIreport().getPastaBaseArquivo().startsWith(config.getLocalUploadArquivoFixo()) ? config.getLocalUploadArquivoFixo() + File.separator  : "") + textoLayout.getArquivoIreport().getPastaBaseArquivo() + File.separator + textoLayout.getArquivoIreport().getNome();
			String caminhoArquivoSubRelatorio = criarCaminhoPastaAteDiretorioSubRelatorio(textoLayout, config);
			superParamentro.setNomeDesignIreport(caminhoArquivo);
			superParamentro.setCaminhoBaseRelatorio("relatorio" + File.separator + "designRelatorio" + File.separator + "textoPadrao" + File.separator);
			superParamentro.setSubReport_Dir("relatorio" + File.separator + "designRelatorio" + File.separator + "textoPadrao" + File.separator);
			superParamentro.setSubRelatorio_Dir(caminhoArquivoSubRelatorio);
			superParamentro.setNomeUsuario(usuario.getNome());
			
			superParamentro.setNomeEspecificoRelatorio(impressaoContratoVO.getMatriculaVO().getMatricula());
			if (!impressaoContratoVO.getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().isEmpty()) {
				superParamentro.adicionarParametro("Lista_AulasMinistradasProfessor", impressaoContratoVO.getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs());
			}
			if (textoLayout.getTipo().equals("CO")) {
				superParamentro.setSubReport_Dir("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
				superParamentro.setListaObjetos(textoLayout.getObjetos());
			} else {
				superParamentro.getParametros().putAll(textoLayout.getParametrosRel());
			}
			if(Uteis.isAtributoPreenchido(impressaoContratoVO.getListaDisciplinasHistoricoPeriodoLetivoSituacao())) {
			impressaoContratoVO.setLegendaSituacaoHistorico(getFacadeFactory().getImpressaoContratoFacade()
					.realizarCriacaoLegendaSituacaoDisciplinaHistorico(impressaoContratoVO, impressaoContratoVO.getMatriculaVO()));
			superParamentro.adicionarParametro("legendaSituacaoHistorico", impressaoContratoVO.getLegendaSituacaoHistorico());
			}
			
			if (Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getMatricula())) {
				superParamentro.adicionarParametro("matriculaAluno", impressaoContratoVO.getMatriculaVO());
			}
			
			geradorRelatorio = new GeradorRelatorio();
			caminhoRelatorio = geradorRelatorio.realizarExportacaoRelatorio(superParamentro);
//			System.out.println("Caminho do Relatorio na geração: "+caminhoRelatorio);
			if (superParamentro.getTipoRelatorioEnum().equals(TipoRelatorioEnum.PDF) && impressaoContratoVO.getGerarNovoArquivoAssinado() && (textoLayout.getAssinarDigitalmenteTextoPadrao() || impressaoContratoVO.getRequerimentoVO().getTipoRequerimento().getAssinarDigitalmenteDeclaracoesGeradasNoRequerimento())) {
				caminhoRelatorio = executarAssinaturaParaImpressaoContrato(caminhoRelatorio, textoLayout, impressaoContratoVO, persistirDocumentoAssinado, config, usuario);
				if (impressaoContratoVO.getTipoImpressaoContratoDiplomaDigital() && Uteis.isAtributoPreenchido(impressaoContratoVO.getExpedicaoDiplomaVO()) && impressaoContratoVO.getExpedicaoDiplomaVO().getGerarXMLDiploma()) {
					if (!Uteis.isAtributoPreenchido(impressaoContratoVO.getExpedicaoDiplomaVO().getCodigoValidacaoDiplomaDigital()) && impressaoContratoVO.getDocumentoAssinado().getArquivoVisual() != null) {
						if (impressaoContratoVO.getDocumentoAssinado().getArquivoVisual().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
							ServidorArquivoOnlineS3RS servidorExternoAmazon = new ServidorArquivoOnlineS3RS(config.getUsuarioAutenticacao(), config.getSenhaAutenticacao(), config.getNomeRepositorio());
							File fileAssinar = new File(getFacadeFactory().getArquivoFacade().realizarDownloadArquivoAmazon(impressaoContratoVO.getDocumentoAssinado().getArquivoVisual(), servidorExternoAmazon, config, false));
							String arquivoOrigem = fileAssinar.getPath().contains(config.getLocalUploadArquivoFixo()) ? fileAssinar.getPath() : UteisJSF.getCaminhoWeb() + "relatorio/" + fileAssinar.getPath();
							getFacadeFactory().getDocumentoAssinadoFacade().adicionarMarcaDaguaPDF(arquivoOrigem, config, getCaminhoPastaWeb(), impressaoContratoVO.getDocumentoAssinado());
							getFacadeFactory().getDocumentoAssinadoFacade().realizarUploadArquivoAmazon(impressaoContratoVO.getDocumentoAssinado().getArquivoVisual(), config, true);
						}else {
							String nomeArquivoOrigem = config.getLocalUploadArquivoFixo() + File.separator + impressaoContratoVO.getDocumentoAssinado().getArquivoVisual().getPastaBaseArquivo() + File.separator + impressaoContratoVO.getDocumentoAssinado().getArquivoVisual().getNome();
							String arquivoOrigem = nomeArquivoOrigem.contains(config.getLocalUploadArquivoFixo()) ? nomeArquivoOrigem : UteisJSF.getCaminhoWeb() + "relatorio/" + nomeArquivoOrigem;
							getFacadeFactory().getDocumentoAssinadoFacade().adicionarMarcaDaguaPDF(arquivoOrigem, config, getCaminhoPastaWeb(), impressaoContratoVO.getDocumentoAssinado());
						}
					}
				}
			}
			return caminhoRelatorio;
		} catch (Exception e) {
//			e.printStackTrace();
			if (impressaoContratoVO.getTipoImpressaoContratoDiplomaDigital() && Uteis.isAtributoPreenchido(caminhoRelatorio)) {
				File representacaoVisual = new File(UteisJSF.getCaminhoWeb() + "relatorio/" + caminhoRelatorio);
				if (Uteis.isAtributoPreenchido(representacaoVisual)) {
					representacaoVisual.delete();
				}
			}
			throw e;		
		} finally {
			geradorRelatorio = null;
			superParamentro = null;
			caminhoRelatorio = null;
		}
	}
	
	@Override
	public String criarCaminhoPastaAteDiretorioSubRelatorio(TextoPadraoLayoutVO textoLayout, ConfiguracaoGeralSistemaVO config) {
		if(!textoLayout.getListaArquivoIreport().isEmpty() && textoLayout.getListaArquivoIreport().size() > 1) {
			String caminhoBase = config.getLocalUploadArquivoFixo();
			String caminhoRelativo = textoLayout.getListaArquivoIreport().stream().filter(arquivo -> !arquivo.getArquivoIreportPrincipal()).findFirst().map(arquivo -> arquivo.getPastaBaseArquivo()).get();
			return caminhoBase + File.separator + caminhoRelativo+File.separator;
		}
		return "";
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	private void gerarHtmlParaPdf(String htmlRelatorio, String arquivoOrigem, TextoPadraoLayoutVO textoPadrao, UsuarioVO usuarioVO) throws Exception {
		GeradorHtmlParaPdf geradorHtmlParaPdf = new GeradorHtmlParaPdf("", "", htmlRelatorio);
		geradorHtmlParaPdf.setPageSize(UteisHTML.getOrientacaoPagina(textoPadrao.getOrientacaoDaPaginaEnum()));
		geradorHtmlParaPdf.setMarginBottom(UteisTextoPadrao.converterCentimetroParaPontos(Float.valueOf(textoPadrao.getMargemInferior().replaceAll(",", "."))));
		geradorHtmlParaPdf.setMarginTop(UteisTextoPadrao.converterCentimetroParaPontos(Float.valueOf(textoPadrao.getMargemSuperior().replaceAll(",", "."))));
		geradorHtmlParaPdf.setMarginRight(UteisTextoPadrao.converterCentimetroParaPontos(Float.valueOf(textoPadrao.getMargemDireita().replaceAll(",", "."))));
		geradorHtmlParaPdf.setMarginLeft(UteisTextoPadrao.converterCentimetroParaPontos(Float.valueOf(textoPadrao.getMargemEsquerda().replaceAll(",", "."))));
		geradorHtmlParaPdf.setSizeTopo(UteisTextoPadrao.converterCentimetroParaPontos(Float.valueOf("0")));
		geradorHtmlParaPdf.setSizeRodape(UteisTextoPadrao.converterCentimetroParaPontos(Float.valueOf("0")));
		geradorHtmlParaPdf.realizarGeracaoDocumentoPdf(arquivoOrigem);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	private String converterHtmlParaPDFComAssinaturaDigital(String htmlRelatorio, ImpressaoContratoVO impressaoContratoVO, TextoPadraoLayoutVO textoPadrao, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoGEDVO configuracaoGEDVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().getCodigo(), false, usuarioVO);
		if (!Uteis.isAtributoPreenchido(configuracaoGEDVO) && !Uteis.isAtributoPreenchido(config.getCertificadoParaDocumento())) {
			throw new Exception("Não foi encontrado o certificado para assinatura do documento. Por favor verificar o cadastro de configuração geral do sistema");
		}
		if (!Uteis.isAtributoPreenchido(configuracaoGEDVO) && !Uteis.isAtributoPreenchido(config.getSenhaCertificadoParaDocumento())) {
			throw new Exception("A senha para certificado digital não foi informado. Por favor verificar o cadastro de configuração geral do sistema");
		}

		String nomeArquivoOrigem = Uteis.getData(new Date(), "dd_MM_yy_HH_mm_ss") + "_" + usuarioVO.getCodigo() + ".pdf";
		String arquivoOrigem = UteisJSF.getCaminhoWeb() + "relatorio/" + nomeArquivoOrigem;
		if (Uteis.isSistemaOperacionalWindows() && arquivoOrigem.startsWith("/")) {
			arquivoOrigem = arquivoOrigem.substring(1, arquivoOrigem.length());
		}
		gerarHtmlParaPdf(htmlRelatorio, arquivoOrigem, textoPadrao, usuarioVO);
		realizarInclusaoArquivoDigitalmenteAssinadoParaImpressaoContrato(textoPadrao, impressaoContratoVO, true, config, usuarioVO);
		getFacadeFactory().getDocumentoAssinadoFacade().adicionarQRCodePDF(impressaoContratoVO.getDocumentoAssinado(), arquivoOrigem, config, configuracaoGEDVO);		
		preencherAssinadorDigitalDocumentoPdf(arquivoOrigem, textoPadrao, impressaoContratoVO, config, configuracaoGEDVO, true);
		return impressaoContratoVO.getDocumentoAssinado().getArquivo().getNome();
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	private String executarAssinaturaParaImpressaoContrato(String nomeArquivoOrigem, TextoPadraoLayoutVO textoPadraoLayout, ImpressaoContratoVO impressaoContratoVO, boolean persistirDocumentoAssinado, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoGedOrigemVO configuracaoGedOrigemVO = new ConfiguracaoGedOrigemVO();
		if(!Uteis.isAtributoPreenchido(impressaoContratoVO.getConfiguracaoGEDVO()) && Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getUnidadeEnsino())){
			impressaoContratoVO.setConfiguracaoGEDVO(getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().getCodigo(), usuarioVO));
			configuracaoGedOrigemVO = impressaoContratoVO.getConfiguracaoGEDVO().getConfiguracaoGedOrigemVO(verificarTipoOrigemDocumentoAssinado(impressaoContratoVO, textoPadraoLayout));
		}else if(!Uteis.isAtributoPreenchido(impressaoContratoVO.getConfiguracaoGEDVO()) && Uteis.isAtributoPreenchido(impressaoContratoVO.getDocumentoAssinado().getUnidadeEnsinoVO())){
			impressaoContratoVO.setConfiguracaoGEDVO(getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(impressaoContratoVO.getDocumentoAssinado().getUnidadeEnsinoVO().getCodigo(), usuarioVO));
			configuracaoGedOrigemVO = impressaoContratoVO.getConfiguracaoGEDVO().getConfiguracaoGedOrigemVO(verificarTipoOrigemDocumentoAssinado(impressaoContratoVO, textoPadraoLayout));
		}else if(Uteis.isAtributoPreenchido(impressaoContratoVO.getConfiguracaoGEDVO())) {
			configuracaoGedOrigemVO = impressaoContratoVO.getConfiguracaoGEDVO().getConfiguracaoGedOrigemVO(verificarTipoOrigemDocumentoAssinado(impressaoContratoVO, textoPadraoLayout));
		}
		if(configuracaoGedOrigemVO.getProvedorAssinaturaPadraoEnum().isProvedorSei()) {
			if (!Uteis.isAtributoPreenchido(impressaoContratoVO.getConfiguracaoGEDVO()) && !Uteis.isAtributoPreenchido(config.getCertificadoParaDocumento())) {
				throw new Exception("Não foi encontrado o certificado para assinatura do documento. Por favor verificar o cadastro de configuração geral do sistema");
			}
			if (!Uteis.isAtributoPreenchido(impressaoContratoVO.getConfiguracaoGEDVO()) && !Uteis.isAtributoPreenchido(config.getSenhaCertificadoParaDocumento())) {
				throw new Exception("A senha para certificado digital não foi informado. Por favor verificar o cadastro de configuração geral do sistema");
			}
		}
		String arquivoOrigem = UteisJSF.getCaminhoWeb() + "relatorio/" + nomeArquivoOrigem;
		if (Uteis.isSistemaOperacionalWindows() && arquivoOrigem.startsWith("/")) {
			arquivoOrigem = arquivoOrigem.substring(1, arquivoOrigem.length());
		}
		realizarInclusaoArquivoDigitalmenteAssinadoParaImpressaoContrato(textoPadraoLayout, impressaoContratoVO, persistirDocumentoAssinado, config, usuarioVO);		
		if((configuracaoGedOrigemVO.getProvedorAssinaturaPadraoEnum().isProvedorTechCert()
				|| configuracaoGedOrigemVO.getProvedorAssinaturaPadraoEnum().isProvedorCertisign()
				|| configuracaoGedOrigemVO.getProvedorAssinaturaPadraoEnum( ).isProvedorImprensaOficial())
				&& (impressaoContratoVO.getTipoImpressaoContratoDiplomaDigital()
				&& impressaoContratoVO.getTipoImpressaoContratoExpedicaoDiploma())) {
			File fileDiretorioCorreto = new File(config.getLocalUploadArquivoFixo() + File.separator + impressaoContratoVO.getDocumentoAssinado().getArquivo().getPastaBaseArquivo() + File.separator + impressaoContratoVO.getDocumentoAssinado().getArquivo().getNome());
			File arquivo = new File(arquivoOrigem);
			getFacadeFactory().getArquivoHelper().copiar(arquivo, fileDiretorioCorreto);
			getFacadeFactory().getArquivoHelper().disponibilizarArquivoAssinadoParaDowload(config.getLocalUploadArquivoFixo() + File.separator +impressaoContratoVO.getDocumentoAssinado().getArquivo().getPastaBaseArquivo() + File.separator + impressaoContratoVO.getDocumentoAssinado().getArquivo().getNome(), impressaoContratoVO.getDocumentoAssinado().getArquivo().getNome());
		}
		if (persistirDocumentoAssinado) {
			if (impressaoContratoVO.getTipoImpressaoContratoDiplomaDigital() && impressaoContratoVO.getExpedicaoDiplomaVO().getGerarXMLDiploma()) {
				if (Uteis.isAtributoPreenchido(impressaoContratoVO.getDocumentoAssinado().getArquivo())) {
					File fileDiretorioCorreto = new File(config.getLocalUploadArquivoFixo() + File.separator + impressaoContratoVO.getDocumentoAssinado().getArquivo().getPastaBaseArquivo() + File.separator + impressaoContratoVO.getDocumentoAssinado().getArquivo().getNome());
					File fileDiretorioTMP = new File(config.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP.getValue() + File.separator + impressaoContratoVO.getDocumentoAssinado().getArquivo().getNome());
					if (!fileDiretorioCorreto.exists() && fileDiretorioTMP.exists()) {
						getFacadeFactory().getArquivoHelper().copiar(fileDiretorioTMP, fileDiretorioCorreto);
					}
				}
			}
		}
		
		TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum = impressaoContratoVO.getDocumentoAssinado().getTipoOrigemDocumentoAssinadoEnum();		
		if(Uteis.isAtributoPreenchido(impressaoContratoVO.getConfiguracaoGEDVO())) {
			if(impressaoContratoVO.getDocumentoAssinado().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.REQUERIMENTO) && Uteis.isAtributoPreenchido(impressaoContratoVO.getRequerimentoVO())) {
				if (Uteis.isAtributoPreenchido(impressaoContratoVO.getRequerimentoVO().getTipoRequerimento().getTextoPadrao()) || impressaoContratoVO.getRequerimentoVO().getTipoRequerimento().getTipo().equals("DE")) {
					tipoOrigemDocumentoAssinadoEnum = TipoOrigemDocumentoAssinadoEnum.DECLARACAO;
				} else if (impressaoContratoVO.getRequerimentoVO().getTipoRequerimento().getTipo().equals("HI")) {
					tipoOrigemDocumentoAssinadoEnum = TipoOrigemDocumentoAssinadoEnum.HISTORICO;
				} else if (impressaoContratoVO.getRequerimentoVO().getTipoRequerimento().getTipo().equals("EC")) {
					tipoOrigemDocumentoAssinadoEnum = TipoOrigemDocumentoAssinadoEnum.EMISSAO_CERTIFICADO;
				} else if (impressaoContratoVO.getRequerimentoVO().getTipoRequerimento().getTipo().equals("ED")) {
					tipoOrigemDocumentoAssinadoEnum = TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA;
				} else {
					tipoOrigemDocumentoAssinadoEnum = TipoOrigemDocumentoAssinadoEnum.NENHUM;
				}
			}
			impressaoContratoVO.getDocumentoAssinado().setTipoOrigemDocumentoAssinadoEnum(tipoOrigemDocumentoAssinadoEnum);
			impressaoContratoVO.getDocumentoAssinado().setCodigoValidacaoDiplomaDigital(impressaoContratoVO.getExpedicaoDiplomaVO().getCodigoValidacaoDiplomaDigital());
			if (Uteis.isAtributoPreenchido(textoPadraoLayout) && textoPadraoLayout.getTipo().equals("MA")) {
				impressaoContratoVO.getDocumentoAssinado().setDocumentoContrato(Boolean.TRUE);
			}
			try {
				if (impressaoContratoVO.getTipoImpressaoContratoDiplomaDigital() && impressaoContratoVO.getExpedicaoDiplomaVO().getGerarXMLDiploma() && !Uteis.isAtributoPreenchido(impressaoContratoVO.getExpedicaoDiplomaVO().getCodigoValidacaoDiplomaDigital())) {
					getFacadeFactory().getDocumentoAssinadoFacade().realizarVerificacaoProvedorDeAssinatura(impressaoContratoVO.getDocumentoAssinado(),impressaoContratoVO.getDocumentoAssinado().getUnidadeEnsinoVO().getCodigo(), false, false, nomeArquivoOrigem,textoPadraoLayout.getAlinhamentoAssinaturaDigitalEnum(), textoPadraoLayout.getCorAssinaturaDigitalmente(), textoPadraoLayout.getAlturaAssinatura(),textoPadraoLayout.getLarguraAssinatura(),8f,380, 10, 0, 0,config,true, usuarioVO, true);
				} else {
					getFacadeFactory().getDocumentoAssinadoFacade().realizarVerificacaoProvedorDeAssinatura(impressaoContratoVO.getDocumentoAssinado(),impressaoContratoVO.getDocumentoAssinado().getUnidadeEnsinoVO().getCodigo(), true, true, nomeArquivoOrigem,textoPadraoLayout.getAlinhamentoAssinaturaDigitalEnum(), textoPadraoLayout.getCorAssinaturaDigitalmente(), textoPadraoLayout.getAlturaAssinatura(),textoPadraoLayout.getLarguraAssinatura(),8f,380, 10, 0, 0,config,true, usuarioVO, true);
				}
			} catch (Exception e) {
				if(persistirDocumentoAssinado 
						&& tipoOrigemDocumentoAssinadoEnum != null
						&& (tipoOrigemDocumentoAssinadoEnum.equals(TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA) || tipoOrigemDocumentoAssinadoEnum.equals(TipoOrigemDocumentoAssinadoEnum.EMISSAO_CERTIFICADO))
						&& Uteis.isAtributoPreenchido(impressaoContratoVO.getDocumentoAssinado())
						&& (impressaoContratoVO.getDocumentoAssinado().getProvedorDeAssinaturaEnum().isProvedorCertisign()
							 || impressaoContratoVO.getDocumentoAssinado().getProvedorDeAssinaturaEnum().isProvedorTechCert()
							 || impressaoContratoVO.getDocumentoAssinado().getProvedorDeAssinaturaEnum().isProvedorImprensaOficial())
						&& Uteis.isAtributoPreenchido(impressaoContratoVO.getDocumentoAssinado().getCodigoProvedorDeAssinatura())) {
					getFacadeFactory().getDocumentoAssinadoFacade().realizarExclusaoDocument(impressaoContratoVO.getDocumentoAssinado(), impressaoContratoVO.getConfiguracaoGEDVO());	
				}
				throw e;
			}
		}else {
			getFacadeFactory().getDocumentoAssinadoFacade().adicionarQRCodePDF(impressaoContratoVO.getDocumentoAssinado(), arquivoOrigem, config, impressaoContratoVO.getConfiguracaoGEDVO());
			getFacadeFactory().getDocumentoAssinadoFacade().adicionarSeloPDF(impressaoContratoVO.getDocumentoAssinado(), arquivoOrigem, config, impressaoContratoVO.getConfiguracaoGEDVO());
			preencherAssinadorDigitalDocumentoPdf(arquivoOrigem, textoPadraoLayout, impressaoContratoVO, config, impressaoContratoVO.getConfiguracaoGEDVO(), true);
		}
		return impressaoContratoVO.getTipoImpressaoContratoExpedicaoDiploma() ? nomeArquivoOrigem : impressaoContratoVO.getDocumentoAssinado().getArquivo().getNome();
	}
	
	public TipoOrigemDocumentoAssinadoEnum verificarTipoOrigemDocumentoAssinado(ImpressaoContratoVO impressaoContratoVO,TextoPadraoLayoutVO textoPadraoLayout) {
		if (Uteis.isAtributoPreenchido(impressaoContratoVO.getRequerimentoVO())) {
			return TipoOrigemDocumentoAssinadoEnum.REQUERIMENTO;
		}else if (Uteis.isAtributoPreenchido(impressaoContratoVO.getExpedicaoDiplomaVO())) {
			return TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA;
		}else if (Uteis.isAtributoPreenchido(textoPadraoLayout) && textoPadraoLayout.getTipo().equals("MA") && Uteis.isAtributoPreenchido(impressaoContratoVO.getTextoPadraoDeclaracao())) {
			return TipoOrigemDocumentoAssinadoEnum.CONTRATO;
		}else if (Uteis.isAtributoPreenchido(textoPadraoLayout) && textoPadraoLayout.getTipo().equals("CE") && Uteis.isAtributoPreenchido(impressaoContratoVO.getTextoPadraoDeclaracao())) {
		  return TipoOrigemDocumentoAssinadoEnum.EMISSAO_CERTIFICADO;
	    }else {
			return TipoOrigemDocumentoAssinadoEnum.DECLARACAO;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	private void realizarInclusaoArquivoDigitalmenteAssinadoParaImpressaoContrato(TextoPadraoLayoutVO textoPadraoLayout, ImpressaoContratoVO impressaoContratoVO, boolean persistirDocumentoAssinado, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception {
		String nomeArquivo = Constantes.EMPTY;
		boolean utilizarNomeOrigem = false;
		if (utilizarNomeOrigem = (textoPadraoLayout.getTipo().equals("DI") || textoPadraoLayout.getTipo().equals("CE"))) {
			String origem = Uteis.getTipoOrigemArquivoParaNome(textoPadraoLayout.getTipo());
			if (textoPadraoLayout.getTipo().equals("DI") 
					&& Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getCurso().getNivelEducacional())
					&& impressaoContratoVO.getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao()) {
				origem = Uteis.getTipoOrigemArquivoParaNome("CE");
			}
			nomeArquivo = getFacadeFactory().getExpedicaoDiplomaFacade().realizarGeracaoNomeArquivoExpedicaoDiploma(origem, impressaoContratoVO.getMatriculaVO(), usuarioVO);
		} else if(impressaoContratoVO.getUtilizarNomeRelatorioEspecifico() && !impressaoContratoVO.getNomeRelatorioEspecifico().equals(Constantes.EMPTY)) {
        	nomeArquivo = (impressaoContratoVO.getNomeRelatorioEspecifico()+"_"+Uteis.getDataComHoraCompleta(new Date()).replaceAll("/", "-")).replace(" ", "_");
		}else if (Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getMatricula())) {
			nomeArquivo = Uteis.removeCaractersEspeciais(impressaoContratoVO.getMatriculaVO().getMatricula()+"-"+impressaoContratoVO.getMatriculaVO().getAluno().getNome()+(impressaoContratoVO.getMatriculaPeriodoVO().getAno().isEmpty()?"":"-"+impressaoContratoVO.getMatriculaPeriodoVO().getAno())+(impressaoContratoVO.getMatriculaPeriodoVO().getSemestre().isEmpty()?"":impressaoContratoVO.getMatriculaPeriodoVO().getSemestre())+"-"+Uteis.getDataComHoraCompleta(new Date()).replaceAll("/", "-")).replace(" ", "_");
        }else {		
			if (!Uteis.isAtributoPreenchido(nomeArquivo)) {
				nomeArquivo = nomeArquivo.replace(" ", "_") + new Date().getTime();
			}
			nomeArquivo =Constantes.UNDERSCORE + usuarioVO.getCodigo() + Constantes.UNDERSCORE + new Date().getTime() ;
        }
		UnidadeEnsinoVO unidadeEnsinoCodigo = impressaoContratoVO.getDocumentoAssinado().getUnidadeEnsinoVO();
		impressaoContratoVO.setDocumentoAssinado(new DocumentoAssinadoVO());
		impressaoContratoVO.getDocumentoAssinado().setImpressaoContratoMatriculaExterna(impressaoContratoVO.getImpressaoContratoMatriculaExterna()); 
		impressaoContratoVO.getDocumentoAssinado().setDataRegistro(new Date());
		impressaoContratoVO.getDocumentoAssinado().setUsuario(usuarioVO);
		impressaoContratoVO.getDocumentoAssinado().getArquivo().setNome(nomeArquivo +".pdf");
		if (utilizarNomeOrigem) {
			impressaoContratoVO.getDocumentoAssinado().getArquivo().setDescricao(nomeArquivo);
		} else {
			impressaoContratoVO.getDocumentoAssinado().getArquivo().setDescricao(textoPadraoLayout.getDescricao() + "_" + nomeArquivo);
		}
		impressaoContratoVO.getDocumentoAssinado().getArquivo().setResponsavelUpload(usuarioVO);
		impressaoContratoVO.getDocumentoAssinado().getArquivo().setDataUpload(new Date());
		impressaoContratoVO.getDocumentoAssinado().getArquivo().setDataDisponibilizacao(new Date());
		impressaoContratoVO.getDocumentoAssinado().getArquivo().setManterDisponibilizacao(true);
		impressaoContratoVO.getDocumentoAssinado().getArquivo().setDataIndisponibilizacao(null);
		impressaoContratoVO.getDocumentoAssinado().getArquivo().setSituacao(SituacaoArquivo.ATIVO.getValor());
		impressaoContratoVO.getDocumentoAssinado().getArquivo().setPastaBaseArquivo(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP.getValue());
		impressaoContratoVO.getDocumentoAssinado().getArquivo().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP);
		impressaoContratoVO.getDocumentoAssinado().setProvedorAssinaturaVisaoAdm(impressaoContratoVO.getProvedorDeAssinaturaEnum());
		if (Uteis.isAtributoPreenchido(impressaoContratoVO.getRequerimentoVO())) {
			if (impressaoContratoVO.getRequerimentoVO().getTipoRequerimento().getTipo().equals(TiposRequerimento.CONTRATO_ESTAGIO_NAO_OBRIGATORIO.getValor())) {
				impressaoContratoVO.getDocumentoAssinado().setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum.TERMO_ESTAGIO_NAO_OBRIGATORIO);
				List<DocumentoAssinadoPessoaVO> documentoAssinadoPessoaVOs = new ArrayList<DocumentoAssinadoPessoaVO>();
				if (Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getAluno())) {
					documentoAssinadoPessoaVOs.add(new DocumentoAssinadoPessoaVO(new Date(), impressaoContratoVO.getMatriculaVO().getAluno(), TipoPessoa.ALUNO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, impressaoContratoVO.getDocumentoAssinado(), 2, "", "", null, impressaoContratoVO.getMatriculaVO().getAluno().getTipoAssinaturaDocumentoEnum()));
				}
				if (Uteis.isAtributoPreenchido(impressaoContratoVO.getRequerimentoVO().getTipoRequerimento().getTextoPadraoVO().getFuncionarioPrimarioVO())) {
					documentoAssinadoPessoaVOs.add(new DocumentoAssinadoPessoaVO(new Date(), impressaoContratoVO.getRequerimentoVO().getTipoRequerimento().getTextoPadraoVO().getFuncionarioPrimarioVO().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, impressaoContratoVO.getDocumentoAssinado(), 2, "", "", null, impressaoContratoVO.getRequerimentoVO().getTipoRequerimento().getTextoPadraoVO().getFuncionarioPrimarioVO().getPessoa().getTipoAssinaturaDocumentoEnum()));
				}
				if (Uteis.isAtributoPreenchido(impressaoContratoVO.getRequerimentoVO().getTipoRequerimento().getTextoPadraoVO().getFuncionarioSecundarioVO())) {
					documentoAssinadoPessoaVOs.add(new DocumentoAssinadoPessoaVO(new Date(), impressaoContratoVO.getRequerimentoVO().getTipoRequerimento().getTextoPadraoVO().getFuncionarioSecundarioVO().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, impressaoContratoVO.getDocumentoAssinado(), 2, "", "", null, impressaoContratoVO.getRequerimentoVO().getTipoRequerimento().getTextoPadraoVO().getFuncionarioSecundarioVO().getPessoa().getTipoAssinaturaDocumentoEnum()));
				}
				impressaoContratoVO.getDocumentoAssinado().setListaDocumentoAssinadoPessoa(documentoAssinadoPessoaVOs);
			} else {
				impressaoContratoVO.getDocumentoAssinado().setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum.REQUERIMENTO);
			}
			impressaoContratoVO.getDocumentoAssinado().getArquivo().setOrigem(OrigemArquivo.REQUERIMENTO.getValor());
			impressaoContratoVO.getDocumentoAssinado().getArquivo().setCodOrigem(impressaoContratoVO.getRequerimentoVO().getCodigo());
			impressaoContratoVO.getDocumentoAssinado().getArquivo().setPessoaVO(impressaoContratoVO.getRequerimentoVO().getPessoa());
		} else if (impressaoContratoVO.getTipoImpressaoContratoDiplomaDigital()) {
			ConfiguracaoGEDVO configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(impressaoContratoVO.getExpedicaoDiplomaVO().getUnidadeEnsinoCertificadora().getCodigo(), usuarioVO);
			impressaoContratoVO.getDocumentoAssinado().getArquivo().setPessoaVO(impressaoContratoVO.getMatriculaVO().getAluno());
			impressaoContratoVO.getDocumentoAssinado().getArquivo().getPessoaVO().setCodigo(impressaoContratoVO.getMatriculaVO().getAluno().getCodigo());
			getFacadeFactory().getDocumentoAssinadoPessoaFacade().realizarRejeicaoDocumentosAssinados(impressaoContratoVO.getExpedicaoDiplomaVO(), impressaoContratoVO.getExpedicaoDiplomaVO().getExisteDiplomaDigitalGerado(), usuarioVO);
			getFacadeFactory().getExpedicaoDiplomaFacade().realizarGeracaoXMLDiplomaDigital(impressaoContratoVO.getExpedicaoDiplomaVO(), configGEDVO, config, null, usuarioVO, null, TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL, false, false, null);
			impressaoContratoVO.getDocumentoAssinado().setUnidadeEnsinoVO(impressaoContratoVO.getExpedicaoDiplomaVO().getUnidadeEnsinoCertificadora());
			impressaoContratoVO.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().setUnidadeEnsinoVO(impressaoContratoVO.getExpedicaoDiplomaVO().getUnidadeEnsinoCertificadora());
			impressaoContratoVO.getDocumentoAssinado().setDecisaoJudicial((impressaoContratoVO.getExpedicaoDiplomaVO().getEmitidoPorDecisaoJudicial()));
			impressaoContratoVO.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().setDecisaoJudicial((impressaoContratoVO.getExpedicaoDiplomaVO().getEmitidoPorDecisaoJudicial()));
			impressaoContratoVO.getDocumentoAssinado().setIdDiplomaDigital(impressaoContratoVO.getExpedicaoDiplomaVO().getIdDiplomaDigital());
			impressaoContratoVO.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().setIdDiplomaDigital(impressaoContratoVO.getExpedicaoDiplomaVO().getIdDiplomaDigital());
			impressaoContratoVO.getDocumentoAssinado().setIdDadosRegistrosDiplomaDigital(impressaoContratoVO.getExpedicaoDiplomaVO().getIdDadosRegistrosDiplomaDigital());
			impressaoContratoVO.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().setIdDadosRegistrosDiplomaDigital(impressaoContratoVO.getExpedicaoDiplomaVO().getIdDadosRegistrosDiplomaDigital());
			impressaoContratoVO.getDocumentoAssinado().setArquivoVisual(impressaoContratoVO.getDocumentoAssinado().getArquivo());
			impressaoContratoVO.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().setArquivoVisual(impressaoContratoVO.getDocumentoAssinado().getArquivo());
			if (impressaoContratoVO.getExpedicaoDiplomaVO().getNovaGeracaoRepresentacaoVisualDiplomaDigital() && impressaoContratoVO.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivoVisual() != null && Uteis.isAtributoPreenchido(impressaoContratoVO.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivoVisual().getNome())) {
				getFacadeFactory().getArquivoFacade().incluir(impressaoContratoVO.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivoVisual(), usuarioVO, config);
			}
			impressaoContratoVO.getDocumentoAssinado().setArquivo(impressaoContratoVO.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivo());
			if (impressaoContratoVO.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getVersaoDiploma() != null) {
				impressaoContratoVO.getDocumentoAssinado().setVersaoDiploma(impressaoContratoVO.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getVersaoDiploma());
			}
			List<DocumentoAssinadoPessoaVO> documentoAssinadoPessoaVOs = new ArrayList<DocumentoAssinadoPessoaVO>();
			documentoAssinadoPessoaVOs.addAll(impressaoContratoVO.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa());
			impressaoContratoVO.getDocumentoAssinado().setListaDocumentoAssinadoPessoa(documentoAssinadoPessoaVOs);
			for (DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO : impressaoContratoVO.getDocumentoAssinado().getListaDocumentoAssinadoPessoa()) {
				documentoAssinadoPessoaVO.setDocumentoAssinadoVO(impressaoContratoVO.getDocumentoAssinado());
			}
			impressaoContratoVO.getDocumentoAssinado().setTipoOrigemDocumentoAssinadoEnum(impressaoContratoVO.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getTipoOrigemDocumentoAssinadoEnum());
			if (Uteis.isAtributoPreenchido(configGEDVO) && !Uteis.isAtributoPreenchido(impressaoContratoVO.getConfiguracaoGEDVO())) {
				impressaoContratoVO.setConfiguracaoGEDVO(configGEDVO);
			}
			impressaoContratoVO.getDocumentoAssinado().setExpedicaoDiplomaVO(impressaoContratoVO.getExpedicaoDiplomaVO());
		} else if (impressaoContratoVO.getTipoImpressaoContratoExpedicaoDiploma()) {
			impressaoContratoVO.getDocumentoAssinado().setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA);
			impressaoContratoVO.getDocumentoAssinado().setUnidadeEnsinoVO(impressaoContratoVO.getExpedicaoDiplomaVO().getUnidadeEnsinoCertificadora());
			if (Uteis.isAtributoPreenchido(impressaoContratoVO.getExpedicaoDiplomaVO().getFuncionarioPrimarioVO().getPessoa())) {
				impressaoContratoVO.getDocumentoAssinado().getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), impressaoContratoVO.getExpedicaoDiplomaVO().getFuncionarioPrimarioVO().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, impressaoContratoVO.getDocumentoAssinado(), 1, impressaoContratoVO.getExpedicaoDiplomaVO().getCargoFuncionarioPrincipalVO().getNome(), impressaoContratoVO.getExpedicaoDiplomaVO().getTituloFuncionarioPrincipal(), null, impressaoContratoVO.getExpedicaoDiplomaVO().getFuncionarioPrimarioVO().getPessoa().getTipoAssinaturaDocumentoEnum()));
			}
			if (Uteis.isAtributoPreenchido(impressaoContratoVO.getExpedicaoDiplomaVO().getFuncionarioSecundarioVO().getPessoa())) {
				impressaoContratoVO.getDocumentoAssinado().getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(),  impressaoContratoVO.getExpedicaoDiplomaVO().getFuncionarioSecundarioVO().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, impressaoContratoVO.getDocumentoAssinado(), 2, impressaoContratoVO.getExpedicaoDiplomaVO().getCargoFuncionarioSecundarioVO().getNome(), impressaoContratoVO.getExpedicaoDiplomaVO().getTituloFuncionarioSecundario(), null, impressaoContratoVO.getExpedicaoDiplomaVO().getFuncionarioSecundarioVO().getPessoa().getTipoAssinaturaDocumentoEnum()));
			}
			if (Uteis.isAtributoPreenchido( impressaoContratoVO.getExpedicaoDiplomaVO().getFuncionarioTerceiroVO().getPessoa())) {
				impressaoContratoVO.getDocumentoAssinado().getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(),  impressaoContratoVO.getExpedicaoDiplomaVO().getFuncionarioTerceiroVO().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, impressaoContratoVO.getDocumentoAssinado(), 3, impressaoContratoVO.getExpedicaoDiplomaVO().getCargoFuncionarioTerceiroVO().getNome(), impressaoContratoVO.getExpedicaoDiplomaVO().getTituloFuncionarioTerceiro(), null, impressaoContratoVO.getExpedicaoDiplomaVO().getFuncionarioTerceiroVO().getPessoa().getTipoAssinaturaDocumentoEnum()));
			}
			impressaoContratoVO.getDocumentoAssinado().setExpedicaoDiplomaVO(impressaoContratoVO.getExpedicaoDiplomaVO());
		}else if (Uteis.isAtributoPreenchido(textoPadraoLayout) &&  textoPadraoLayout.getTipo().equals("MA") && Uteis.isAtributoPreenchido(impressaoContratoVO.getTextoPadraoDeclaracao())){
			impressaoContratoVO.getDocumentoAssinado().setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum.CONTRATO);
			impressaoContratoVO.getDocumentoAssinado().getArquivo().setPessoaVO(impressaoContratoVO.getMatriculaVO().getAluno());
			validarCamposAssinaturaContrato(impressaoContratoVO);
			validarAssinaturaFuncionarioContrato(impressaoContratoVO.getTextoPadraoDeclaracao(), impressaoContratoVO);
			
		} 
		else {
			if (Uteis.isAtributoPreenchido(textoPadraoLayout) &&  textoPadraoLayout.getTipo().equals("CE") && Uteis.isAtributoPreenchido(impressaoContratoVO.getCertificadoCursoExtensaoRelVO().getCertificadoCursoExtensaoDisciplinasRelVOs())) {
				impressaoContratoVO.getDocumentoAssinado().setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum.EMISSAO_CERTIFICADO);
			} else {
				impressaoContratoVO.getDocumentoAssinado().setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum.DECLARACAO);
			}
			impressaoContratoVO.getDocumentoAssinado().getArquivo().setPessoaVO(impressaoContratoVO.getMatriculaVO().getAluno());
			if (Uteis.isAtributoPreenchido(impressaoContratoVO.getFuncionarioPrincipalVO().getPessoa())) {
				impressaoContratoVO.getDocumentoAssinado().getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), impressaoContratoVO.getFuncionarioPrincipalVO().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, impressaoContratoVO.getDocumentoAssinado(), 1, impressaoContratoVO.getCargoFuncionarioPrincipal().getNome(), "", null, impressaoContratoVO.getFuncionarioPrincipalVO().getPessoa().getTipoAssinaturaDocumentoEnum()));
			}
			if (Uteis.isAtributoPreenchido(impressaoContratoVO.getFuncionarioSecundarioVO().getPessoa())) {
				impressaoContratoVO.getDocumentoAssinado().getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(),  impressaoContratoVO.getFuncionarioSecundarioVO().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, impressaoContratoVO.getDocumentoAssinado(), 2, impressaoContratoVO.getCargoFuncionarioSecundario().getNome(), "", null, impressaoContratoVO.getFuncionarioSecundarioVO().getPessoa().getTipoAssinaturaDocumentoEnum()));
			}
			if (Uteis.isAtributoPreenchido( impressaoContratoVO.getFuncionarioTerceiroVO().getPessoa())) {
				impressaoContratoVO.getDocumentoAssinado().getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(),  impressaoContratoVO.getFuncionarioTerceiroVO().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, impressaoContratoVO.getDocumentoAssinado(), 3, impressaoContratoVO.getCargoFuncionarioTerceiro().getNome(), "", null, impressaoContratoVO.getFuncionarioTerceiroVO().getPessoa().getTipoAssinaturaDocumentoEnum()));
			}
		}
		impressaoContratoVO.getDocumentoAssinado().getArquivo().setTurma(impressaoContratoVO.getTurmaVO());
		impressaoContratoVO.getDocumentoAssinado().getArquivo().setDisciplina(impressaoContratoVO.getDisciplinaVO());
		impressaoContratoVO.getDocumentoAssinado().getArquivo().setArquivoAssinadoDigitalmente(true);
		impressaoContratoVO.getDocumentoAssinado().setMatricula(impressaoContratoVO.getMatriculaVO());
		if (!(Uteis.isAtributoPreenchido(impressaoContratoVO.getExpedicaoDiplomaVO()) && impressaoContratoVO.getExpedicaoDiplomaVO().getGerarXMLDiploma() || (Uteis.isAtributoPreenchido(impressaoContratoVO.getExpedicaoDiplomaVO().getMatricula()) && impressaoContratoVO.getExpedicaoDiplomaVO().getGerarXMLDiplomaLote()))) {
			if (Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getUnidadeEnsino())) {
				impressaoContratoVO.getDocumentoAssinado().setUnidadeEnsinoVO(impressaoContratoVO.getMatriculaVO().getUnidadeEnsino());
			} else {
				if (Uteis.isAtributoPreenchido(unidadeEnsinoCodigo)) {
					impressaoContratoVO.getDocumentoAssinado().setUnidadeEnsinoVO(unidadeEnsinoCodigo);
				}
			}
		}
		if (persistirDocumentoAssinado && ((impressaoContratoVO.getDocumentoAssinado().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL) && !impressaoContratoVO.getExpedicaoDiplomaVO().getNovaGeracaoRepresentacaoVisualDiplomaDigital() || !impressaoContratoVO.getDocumentoAssinado().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL)))) {
			getFacadeFactory().getDocumentoAssinadoFacade().incluir(impressaoContratoVO.getDocumentoAssinado(), false, usuarioVO, config);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void preencherAssinadorDigitalDocumentoPdf(String arquivoOrigem, TextoPadraoLayoutVO textoPadraoLayout, ImpressaoContratoVO impressaoContratoVO, ConfiguracaoGeralSistemaVO config, ConfiguracaoGEDVO configuracaoGED, boolean assinaturaUnidadeEnsino) throws Exception {		
		String caminhoCertificado = "";
		String senha = "";
		if(Uteis.isAtributoPreenchido(configuracaoGED) && assinaturaUnidadeEnsino) {
			caminhoCertificado = (!configuracaoGED.getCertificadoDigitalUnidadeEnsinoVO().getPastaBaseArquivo().startsWith(config.getLocalUploadArquivoFixo()) ? config.getLocalUploadArquivoFixo() + File.separator : "") + configuracaoGED.getCertificadoDigitalUnidadeEnsinoVO().getPastaBaseArquivo() + File.separator + configuracaoGED.getCertificadoDigitalUnidadeEnsinoVO().getNome();
			senha = configuracaoGED.getSenhaCertificadoDigitalUnidadeEnsino();
		}else if(Uteis.isAtributoPreenchido(configuracaoGED) && !assinaturaUnidadeEnsino) {
			caminhoCertificado = (!configuracaoGED.getCertificadoDigitalUnidadeEnsinoVO().getPastaBaseArquivo().startsWith(config.getLocalUploadArquivoFixo()) ? config.getLocalUploadArquivoFixo() + File.separator : "") + configuracaoGED.getCertificadoDigitalUnidadeEnsinoVO().getPastaBaseArquivo() + File.separator + configuracaoGED.getCertificadoDigitalUnidadeEnsinoVO().getNome();
			senha = configuracaoGED.getSenhaCertificadoDigitalUnidadeCertificadora();
		}else if(!Uteis.isAtributoPreenchido(configuracaoGED) && assinaturaUnidadeEnsino)  {
			caminhoCertificado = (!config.getCertificadoParaDocumento().getPastaBaseArquivo().startsWith(config.getLocalUploadArquivoFixo()) ? config.getLocalUploadArquivoFixo() + File.separator : "") + config.getCertificadoParaDocumento().getPastaBaseArquivo() + File.separator + config.getCertificadoParaDocumento().getNome();
			senha = config.getSenhaCertificadoParaDocumento();
		}
		if(!Uteis.isAtributoPreenchido(caminhoCertificado) || !new File(caminhoCertificado).exists()) {
			if(assinaturaUnidadeEnsino) {
				throw new Exception("Não foi localizado o CERTIFICADO DA UNIDADE DE ENSINO para realizar a assinatura digital.");
			}else {
				throw new Exception("Não foi localizado o CERTIFICADO DA UNIDADE CERTIFICADORA para realizar a assinatura digital.");
			}
		}
		if(!Uteis.isAtributoPreenchido(senha)) {
			if(assinaturaUnidadeEnsino) {
				throw new Exception("Não foi informado a SENHA do CERTIFICADO DA UNIDADE DE ENSINO para realizar a assinatura digital.");
			}else {
				throw new Exception("Não foi localizado a SENHA do  CERTIFICADO DA UNIDADE CERTIFICADORA para realizar a assinatura digital.");
			}

		}
		AssinaturaDigialDocumentoPDF assinador = new AssinaturaDigialDocumentoPDF();
		assinador.setToken(true);
		assinador.setPathKeyStore(caminhoCertificado);
		assinador.setSenhaKeyStore(senha);
		assinador.setArquivoOrigem(arquivoOrigem);
		String pastaBaseArquivo =  (!impressaoContratoVO.getDocumentoAssinado().getArquivo().getPastaBaseArquivo().startsWith(config.getLocalUploadArquivoFixo()) ? config.getLocalUploadArquivoFixo() + File.separator : "") + impressaoContratoVO.getDocumentoAssinado().getArquivo().getPastaBaseArquivo();
		assinador.setCaminhoArquivoDestino(pastaBaseArquivo);
		assinador.setNomeArquivoDestino(impressaoContratoVO.getDocumentoAssinado().getArquivo().getNome());
		assinador.setIsValidarArquivoExistente(true);
		assinador.setRazaoCertificado(impressaoContratoVO.getDocumentoAssinado().getNumeroDocumentoAssinado());
		assinador.setCorAssinaturaDigitalmente(textoPadraoLayout.getCorAssinaturaDigitalmente());
		assinador.setAlturaAssinatura(textoPadraoLayout.getAlturaAssinatura());
		assinador.setLarguraAssinatura(textoPadraoLayout.getLarguraAssinatura());
		assinador.setDataAssinatura(impressaoContratoVO.getDocumentoAssinado().getDataRegistro());
		assinador.setApresentarAssinaturaDigital(Uteis.isAtributoPreenchido(configuracaoGED) && assinaturaUnidadeEnsino && Uteis.isAtributoPreenchido(configuracaoGED.getConfiguracaoGedOrigemVO(impressaoContratoVO.getDocumentoAssinado().getTipoOrigemDocumentoAssinadoEnum())) ? configuracaoGED.getConfiguracaoGedOrigemVO(impressaoContratoVO.getDocumentoAssinado().getTipoOrigemDocumentoAssinadoEnum()).getApresentarAssinaturaUnidadeEnsino()
: true);
		if(textoPadraoLayout.getAlinhamentoAssinaturaDigitalEnum().isRodapeDireita()){
			assinador.setIsPosicaoAssinaturaCima(false);
			assinador.setIsPosicaoAssinaturaEsquerdo(false);
		}else if(textoPadraoLayout.getAlinhamentoAssinaturaDigitalEnum().isRodapeEsquerda()){
			assinador.setIsPosicaoAssinaturaCima(false);
			assinador.setIsPosicaoAssinaturaEsquerdo(true);
		}else if(textoPadraoLayout.getAlinhamentoAssinaturaDigitalEnum().isTopoDireita()){
			assinador.setIsPosicaoAssinaturaCima(true);
			assinador.setIsPosicaoAssinaturaEsquerdo(false);
		}else if(textoPadraoLayout.getAlinhamentoAssinaturaDigitalEnum().isTopoEsquerda()){
			assinador.setIsPosicaoAssinaturaCima(true);
			assinador.setIsPosicaoAssinaturaEsquerdo(true);
		}
			
		assinador.realizarGeracaoDocumentoPdf();		
		getFacadeFactory().getArquivoHelper().disponibilizarArquivoAssinadoParaDowload(pastaBaseArquivo + File.separator + impressaoContratoVO.getDocumentoAssinado().getArquivo().getNome(), impressaoContratoVO.getDocumentoAssinado().getArquivo().getNome());
		FileUtils.forceDelete(new File(arquivoOrigem));
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public String executarConversaoHtmlParaPdf(String htmlRelatorio, TextoPadraoLayoutVO textoPadrao, UsuarioVO usuarioVO) throws Exception {
		String nomeArquivoOrigem = "DECLARACAO_" + Uteis.getData(new Date(), "dd_MM_yy_HH_mm_ss") + "_" + usuarioVO.getCodigo() + ".pdf";
		String arquivoOrigem = UteisJSF.getCaminhoWeb() + "relatorio/" + nomeArquivoOrigem;
		if (Uteis.isSistemaOperacionalWindows() && arquivoOrigem.startsWith("/")) {
			arquivoOrigem = arquivoOrigem.substring(1, arquivoOrigem.length());
		}
		gerarHtmlParaPdf(htmlRelatorio, arquivoOrigem, textoPadrao, usuarioVO);
		return nomeArquivoOrigem;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public String executarConversaoHtmlParaPdfComunicadoInterno(ComunicacaoInternaVO obj, Boolean imprimirPDF, UsuarioVO usuarioVO) throws Exception {
		String nomeArquivoOrigem = "Email_" + Uteis.getData(new Date(), "dd_MM_yy_HH_mm_ss") + "_" + usuarioVO.getCodigo() + ".pdf";
		String arquivoOrigem = UteisJSF.getCaminhoWeb() + "relatorio/" + nomeArquivoOrigem;
		if (Uteis.isSistemaOperacionalWindows() && arquivoOrigem.startsWith("/")) {
			arquivoOrigem = arquivoOrigem.substring(1, arquivoOrigem.length());
		}

		String texto = obj.getMensagem();
		int parametro = texto.indexOf("<div class=\"Section1\">");
		String textoAntes = texto.substring(0, parametro) + " <div class=\"Section1\"> ";
		String textoDepois = obj.getCabecarioEmailLayout() + texto.substring(parametro + 23, texto.length());
		texto = textoAntes + textoDepois;
		if (imprimirPDF) {
			GeradorHtmlParaPdf geradorHtmlParaPdf = new GeradorHtmlParaPdf("", "", texto);
			geradorHtmlParaPdf.setPageSize(UteisHTML.getOrientacaoPagina(OrientacaoPaginaEnum.RETRATO));
			geradorHtmlParaPdf.setMarginBottom(UteisTextoPadrao.converterCentimetroParaPontos(Float.valueOf(0.5f)));
			geradorHtmlParaPdf.setMarginTop(UteisTextoPadrao.converterCentimetroParaPontos(Float.valueOf(0.5f)));
			geradorHtmlParaPdf.setMarginRight(UteisTextoPadrao.converterCentimetroParaPontos(Float.valueOf(0.5f)));
			geradorHtmlParaPdf.setMarginLeft(UteisTextoPadrao.converterCentimetroParaPontos(Float.valueOf(0.5f)));
			geradorHtmlParaPdf.setSizeTopo(UteisTextoPadrao.converterCentimetroParaPontos(Float.valueOf(0.5f)));
			geradorHtmlParaPdf.setSizeRodape(UteisTextoPadrao.converterCentimetroParaPontos(Float.valueOf(0.5f)));
			geradorHtmlParaPdf.realizarGeracaoDocumentoPdf(arquivoOrigem);
		} else {
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("textoRelatorio", texto);
		}
		return nomeArquivoOrigem;
	}
	
	

    public Boolean imprimirDeclaracaoProcSeletivo(Integer textoPadraoDeclaracao, ImpressaoContratoVO impressaoContratoVO, ImpressaoContratoVO impressaoContratoGravarVO, String tipoDeclaracao, TurmaVO turmaVO, DisciplinaVO disciplinaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioLogado) throws Exception {
    	try {
    		Boolean imprimirContrato = false;
    		TextoPadraoDeclaracaoVO texto = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(textoPadraoDeclaracao, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
			//getFacadeFactory().getInscricaoFacade().carregarDados(impressaoContratoVO.getInscricaoVO(), usuarioLogado);
    		imprimirContrato = true;
    		impressaoContratoVO.setUsuarioLogado(usuarioLogado);    		
    		String textoStr = "";
    		impressaoContratoVO.setConfiguracaoFinanceiroVO(configuracaoFinanceiroVO);
    		texto.substituirTag(impressaoContratoVO, usuarioLogado);
    		textoStr = getFacadeFactory().getTextoPadraoDeclaracaoFacade().removerBordaDaPagina(texto.getTexto());
    		textoStr = getFacadeFactory().getTextoPadraoDeclaracaoFacade().adicionarStyleFormatoPaginaTextoPadrao(textoStr, texto.getOrientacaoDaPagina());
    		HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
    		request.getSession().setAttribute("textoRelatorio", textoStr);
    		impressaoContratoGravarVO.getUsuarioLogado().setCodigo(usuarioLogado.getCodigo());
    		impressaoContratoGravarVO.setTurmaVO(turmaVO);
    		impressaoContratoGravarVO.setProfessor(impressaoContratoVO.getProfessor());
    		impressaoContratoGravarVO.setDisciplinaVO(disciplinaVO);
    		impressaoContratoGravarVO.setTextoPadraoDeclaracao(textoPadraoDeclaracao);
    		gravarImpressaoContrato(impressaoContratoGravarVO);
    		impressaoContratoGravarVO = null;
    		return imprimirContrato;
    	} catch (Exception e) {
    		throw e;
    	}
    }
    
    public void validarDocumentacaoPendente(TextoPadraoDeclaracaoVO texto, String matricula, UsuarioVO usuarioLogado) throws Exception {
        if (texto.getControlarDocumentacaoPendente()) {
            if (texto.getValidarTodosDocumentosCurso()) {
                // caso exista alguma documentação pendente para a matricula selecionada
                // é lançada uma exceção
                List listaDocumentacaoPendente = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarDocumetacaoMatriculaPorMatriculaAlunoEntregue(matricula, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, false, usuarioLogado);
                if (listaDocumentacaoPendente.size() > 0) {
                    throw new Exception("Existe Documentação Pendente para a Matricula Selecionada");
                }
            } else if (texto.getValidarApenasDocumentoSuspensao()) {
                // caso exista alguma documentação QUE GERA SUSPENSÃO pendente para a matricula selecionada
                // é lançada uma exceção
                List listaDocumentacaoPendente = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarDocumetacaoMatriculaPorMatriculaAlunoPendenteEntregaSuspendeMatricula(matricula, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuarioLogado);
                if (listaDocumentacaoPendente.size() > 0) {
                    throw new Exception("Existe Documentação que Geram Suspensão Pendente para a Matricula Selecionada");
                }
            }
        }
    }

    public static String getIdEntidade() {
        return ImpressaoDeclaracao.idEntidade;
    }

    public void setIdEntidade(String idEntidade) {
        ImpressaoDeclaracao.idEntidade = idEntidade;
    }
    
    public void validarDados(ImpressaoContratoVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getMatriculaVO().getMatricula())) {
			throw new ConsistirException("O campo MATRICULA (IMPRESSAO CONTRATO)deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getTextoPadraoDeclaracao())) {
			throw new ConsistirException("O campo TEXTO PADRÃO (IMPRESSAO CONTRATO)deve ser informado.");
		}

	}

    public void gravarImpressaoContrato(ImpressaoContratoVO obj) throws Exception {
//        if (obj.getNovoObj()) {
        incluir(obj);
//        } else {
//            alterar(obj);
//        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ImpressaoContratoVO obj) throws Exception {
		try {
			final String sql = "INSERT INTO impressaodeclaracao( matricula, professor, dataGeracao, usuarioGeracao, disciplina, turma, textoPadraoDeclaracao, entregue, requerimento, documentoassinado, tipotextoenum ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					int x = 1;
					if (!obj.getMatriculaVO().getMatricula().trim().isEmpty()) {
						sqlInserir.setString(x++, obj.getMatriculaVO().getMatricula());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (obj.getProfessor().getCodigo() > 0) {
						sqlInserir.setInt(x++, obj.getProfessor().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setTimestamp(x++, Uteis.getDataJDBCTimestamp(new Date()));
					sqlInserir.setInt(x++, obj.getUsuarioLogado().getCodigo());
					if (obj.getDisciplinaVO().getCodigo() > 0) {
						sqlInserir.setInt(x++, obj.getDisciplinaVO().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (obj.getTurmaVO().getCodigo() > 0) {
						sqlInserir.setInt(x++, obj.getTurmaVO().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}

					sqlInserir.setInt(x++, obj.getTextoPadraoDeclaracao());

					sqlInserir.setBoolean(x++, Boolean.TRUE);
					if (Uteis.isAtributoPreenchido(obj.getRequerimentoVO().getCodigo())) {
						sqlInserir.setInt(x++, obj.getRequerimentoVO().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (obj.isImpressaoPdf() && Uteis.isAtributoPreenchido(obj.getDocumentoAssinado().getCodigo())) {
						sqlInserir.setInt(x++, obj.getDocumentoAssinado().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setString(x++, obj.getTipoTextoEnum().name());
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
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}
    
    @Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void validarExclusaoImpressaoDeclaracaoPorRequerimento(Integer requerimento, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception{
		List<ImpressaoContratoVO> lista = consultarSeJaExisteImpressaoDeclaracaoParaRequerimento(requerimento);
		if(Uteis.isAtributoPreenchido(lista)){
			excluirImpressaoDeclaracao(lista, usuario, configuracaoGeralSistemaVO);
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void excluirImpressaoDeclaracao(List<ImpressaoContratoVO> lista, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception{
		String sql = "DELETE FROM impressaodeclaracao WHERE codigo in ( "+ UteisTexto.converteListaEntidadeCampoCodigoParaString(lista)+") " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql);
		for (ImpressaoContratoVO impressaoContratoVO : lista) {
			getFacadeFactory().getDocumentoAssinadoFacade().excluir(impressaoContratoVO.getDocumentoAssinado(), false, usuario, configuracaoGeralSistemaVO);	
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private String getSqlConsultaPadraoImpressaoDeclaracaoDocumentoAssinado(){
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select  impressaodeclaracao.codigo as \"impressaodeclaracao.codigo\", impressaodeclaracao.matricula as \"impressaodeclaracao.matricula\", ");
		sqlStr.append("documentoassinado.codigo as \"documentoassinado.codigo\", documentoassinado.dataregistro as \"documentoassinado.dataregistro\", ");
		sqlStr.append("documentoassinado.provedordeassinaturaenum as \"documentoassinado.provedordeassinaturaenum\", ");
		sqlStr.append("documentoassinado.chaveProvedorDeAssinatura as \"documentoassinado.chaveProvedorDeAssinatura\", ");
		sqlStr.append("documentoassinado.urlProvedorDeAssinatura as \"documentoassinado.urlProvedorDeAssinatura\", ");
		sqlStr.append("documentoassinado.codigoProvedorDeAssinatura as \"documentoassinado.codigoProvedorDeAssinatura\", ");
		sqlStr.append("documentoassinado.tipoorigemdocumentoassinado as \"documentoassinado.tipoorigemdocumentoassinado\", ");
		sqlStr.append("usuario.codigo as \"usuario.codigo\" , usuario.nome as \"usuario.nome\", ");
		sqlStr.append("arquivo.codigo as \"arquivo.codigo\" , arquivo.codOrigem as \"arquivo.codOrigem\", arquivo.nome as \"arquivo.nome\", ");
		sqlStr.append("arquivo.apresentarPortalCoordenador as \"arquivo.apresentarPortalCoordenador\", arquivo.apresentarPortalProfessor as \"arquivo.apresentarPortalProfessor\", arquivo.apresentarPortalAluno  as \"arquivo.apresentarPortalAluno\", ");
		sqlStr.append("arquivo.descricao as \"arquivo.descricao\", arquivo.dataupload as \"arquivo.dataupload\", arquivo.dataDisponibilizacao as \"arquivo.dataDisponibilizacao\", arquivo.dataIndisponibilizacao as \"arquivo.dataIndisponibilizacao\", ");
		sqlStr.append("arquivo.manterDisponibilizacao as \"arquivo.manterDisponibilizacao\", arquivo.origem as \"arquivo.origem\",arquivo.situacao as \"arquivo.situacao\", arquivo.controlardownload as \"arquivo.controlardownload\", ");
		sqlStr.append("arquivo.responsavelupload as \"arquivo.responsavelupload\", arquivo.disciplina as \"arquivo.disciplina\", arquivo.turma as \"arquivo.turma\", arquivo.extensao as \"arquivo.extensao\", arquivo.apresentarDeterminadoPeriodo as \"arquivo.apresentarDeterminadoPeriodo\", ");
		sqlStr.append("arquivo.permitirArquivoResposta as \"arquivo.permitirArquivoResposta\", arquivo.pastabasearquivo as \"arquivo.pastabasearquivo\", arquivo.arquivoResposta as \"arquivo.arquivoResposta\", arquivo.pessoa as \"arquivo.pessoa\", ");
		sqlStr.append("arquivo.professor as \"arquivo.professor\", arquivo.nivelEducacional as \"arquivo.nivelEducacional\", arquivo.cpfAlunoDocumentacao as \"arquivo.cpfAlunoDocumentacao\", arquivo.cpfRequerimento as \"arquivo.cpfRequerimento\", ");
		sqlStr.append("arquivo.indice as \"arquivo.indice\", arquivo.agrupador as \"arquivo.agrupador\", arquivo.indiceagrupador as \"arquivo.indiceagrupador\", arquivo.arquivoAssinadoDigitalmente as \"arquivo.arquivoAssinadoDigitalmente\", ");
		sqlStr.append("arquivo.curso as \"arquivo.curso\"");
		sqlStr.append("FROM impressaodeclaracao ");
		return sqlStr.toString();
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ImpressaoContratoVO>  consultarSeJaExisteImpressaoDeclaracaoParaRequerimento(Integer requerimento) throws Exception {
		List<ImpressaoContratoVO> lista = new ArrayList<>();
		StringBuilder sqlStr = new StringBuilder(getSqlConsultaPadraoImpressaoDeclaracaoDocumentoAssinado());
		sqlStr.append(" inner join documentoassinado on impressaodeclaracao.documentoassinado = documentoassinado.codigo ");
		sqlStr.append(" inner join arquivo on arquivo.codigo = documentoassinado.arquivo ");
		sqlStr.append(" inner join usuario on usuario.codigo = documentoassinado.usuario ");
		sqlStr.append(" WHERE impressaodeclaracao.requerimento = ").append(requerimento);
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (dadosSQL.next()) {
			ImpressaoContratoVO obj = montarDadosImpressaoDeclaracao(dadosSQL, requerimento, 0);
			lista.add(obj);
		}
		return lista;
	}
	

	public ImpressaoContratoVO consultarSeJaExisteImpressaoDeclaracaoParaTextoPadrao(ImpressaoContratoVO impressaoContrato, Integer textoPadrao) throws Exception {
		StringBuilder sqlStr = new StringBuilder(getSqlConsultaPadraoImpressaoDeclaracaoDocumentoAssinado());
		sqlStr.append("inner join documentoassinado on impressaodeclaracao.documentoassinado = documentoassinado.codigo ");
		sqlStr.append("inner join arquivo on arquivo.codigo = documentoassinado.arquivo ");
		sqlStr.append("inner join usuario on usuario.codigo = documentoassinado.usuario ");
		sqlStr.append(" WHERE impressaodeclaracao.textopadraodeclaracao = ").append(textoPadrao);
		sqlStr.append(" AND impressaodeclaracao.tipotextoenum = '").append(impressaoContrato.getTipoTextoEnum().name()).append("' ");

		if (Uteis.isAtributoPreenchido(impressaoContrato.getMatriculaVO().getMatricula())) {
			sqlStr.append(" AND impressaodeclaracao.matricula = '").append(impressaoContrato.getMatriculaVO().getMatricula()).append("' ");
		}
		if (Uteis.isAtributoPreenchido(impressaoContrato.getRequerimentoVO())) {
			sqlStr.append(" AND impressaodeclaracao.requerimento = ").append(impressaoContrato.getRequerimentoVO().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(impressaoContrato.getDisciplinaVO())) {
			sqlStr.append(" AND impressaodeclaracao.disciplina = ").append(impressaoContrato.getDisciplinaVO().getCodigo());
		}
		sqlStr.append(" AND impressaodeclaracao.datageracao = ( select max(datageracao) from impressaodeclaracao ");
		sqlStr.append(" WHERE textopadraodeclaracao = ").append(textoPadrao);
		sqlStr.append(" AND impressaodeclaracao.tipotextoenum = '").append(impressaoContrato.getTipoTextoEnum().name()).append("' ");
		if (Uteis.isAtributoPreenchido(impressaoContrato.getMatriculaVO().getMatricula())) {
			sqlStr.append(" AND matricula = '").append(impressaoContrato.getMatriculaVO().getMatricula()).append("' ");
		}
		if (Uteis.isAtributoPreenchido(impressaoContrato.getRequerimentoVO())) {
			sqlStr.append(" AND requerimento = ").append(impressaoContrato.getRequerimentoVO().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(impressaoContrato.getDisciplinaVO())) {
			sqlStr.append(" AND disciplina = ").append(impressaoContrato.getDisciplinaVO().getCodigo());
		}
		sqlStr.append(")");
		
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(dadosSQL.next()){
			return montarDadosImpressaoDeclaracao(dadosSQL, impressaoContrato.getRequerimentoVO().getCodigo(), textoPadrao);
		}
		ImpressaoContratoVO obj = new ImpressaoContratoVO();
		obj.setNovoObj(true);
		return obj;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private ImpressaoContratoVO montarDadosImpressaoDeclaracao(SqlRowSet dadosSQL, Integer requerimento, Integer textoPadrao){
			ImpressaoContratoVO obj = new ImpressaoContratoVO();
			obj.setCodigo(new Integer(dadosSQL.getInt("impressaodeclaracao.codigo")));
			obj.getMatriculaVO().setMatricula(dadosSQL.getString("impressaodeclaracao.matricula"));
			obj.setTextoPadraoDeclaracao(textoPadrao);
			obj.getRequerimentoVO().setCodigo(requerimento);
			obj.setDocumentoAssinado(new DocumentoAssinadoVO());
			obj.getDocumentoAssinado().setNovoObj(false);
			obj.getDocumentoAssinado().setCodigo(new Integer(dadosSQL.getInt("documentoassinado.codigo")));
			obj.getDocumentoAssinado().setDataRegistro(dadosSQL.getDate("documentoassinado.dataregistro"));
			if(Uteis.isAtributoPreenchido(dadosSQL.getString("documentoassinado.tipoorigemdocumentoassinado"))) {
				obj.getDocumentoAssinado().setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum.valueOf(dadosSQL.getString("documentoassinado.tipoorigemdocumentoassinado")));
			}
			if(Uteis.isAtributoPreenchido(dadosSQL.getString("documentoassinado.provedordeassinaturaenum"))) {
				obj.getDocumentoAssinado().setProvedorDeAssinaturaEnum(ProvedorDeAssinaturaEnum.valueOf(dadosSQL.getString("documentoassinado.provedordeassinaturaenum")));
			}
			obj.getDocumentoAssinado().setChaveProvedorDeAssinatura(dadosSQL.getString("documentoassinado.chaveProvedorDeAssinatura"));
			obj.getDocumentoAssinado().setUrlProvedorDeAssinatura(dadosSQL.getString("documentoassinado.urlProvedorDeAssinatura"));
			obj.getDocumentoAssinado().setCodigoProvedorDeAssinatura(dadosSQL.getString("documentoassinado.codigoProvedorDeAssinatura"));
			obj.getDocumentoAssinado().getUsuario().setCodigo(new Integer(dadosSQL.getInt("usuario.codigo")));
			obj.getDocumentoAssinado().getUsuario().setNome(dadosSQL.getString("usuario.nome"));
			obj.getDocumentoAssinado().getArquivo().setCodigo(new Integer(dadosSQL.getInt("arquivo.codigo")));
			obj.getDocumentoAssinado().getArquivo().setNome(dadosSQL.getString("arquivo.nome"));
			obj.getDocumentoAssinado().getArquivo().setDescricao(dadosSQL.getString("arquivo.descricao"));
			obj.getDocumentoAssinado().getArquivo().setDescricaoAntesAlteracao(dadosSQL.getString("arquivo.descricao"));
			obj.getDocumentoAssinado().getArquivo().setExtensao(dadosSQL.getString("arquivo.extensao"));
			obj.getDocumentoAssinado().getArquivo().setDataUpload(dadosSQL.getDate("arquivo.dataUpload"));
			obj.getDocumentoAssinado().getArquivo().getResponsavelUpload().setCodigo((dadosSQL.getInt("arquivo.responsavelUpload")));
			obj.getDocumentoAssinado().getArquivo().setCodOrigem(dadosSQL.getInt("arquivo.codOrigem"));
			obj.getDocumentoAssinado().getArquivo().setPastaBaseArquivo(dadosSQL.getString("arquivo.pastaBaseArquivo"));
			obj.getDocumentoAssinado().getArquivo().getTurma().setCodigo((dadosSQL.getInt("arquivo.turma")));
			obj.getDocumentoAssinado().getArquivo().getProfessor().setCodigo((dadosSQL.getInt("arquivo.professor")));
			obj.getDocumentoAssinado().getArquivo().setDataDisponibilizacao(dadosSQL.getDate("arquivo.dataDisponibilizacao"));
			obj.getDocumentoAssinado().getArquivo().setDataIndisponibilizacao(dadosSQL.getDate("arquivo.dataIndisponibilizacao"));
			obj.getDocumentoAssinado().getArquivo().setManterDisponibilizacao((dadosSQL.getBoolean("arquivo.manterDisponibilizacao")));
			obj.getDocumentoAssinado().getArquivo().setOrigem(dadosSQL.getString("arquivo.origem"));
			obj.getDocumentoAssinado().getArquivo().setNivelEducacional(dadosSQL.getString("arquivo.nivelEducacional"));
			obj.getDocumentoAssinado().getArquivo().getDisciplina().setCodigo((dadosSQL.getInt("arquivo.disciplina")));
			obj.getDocumentoAssinado().getArquivo().setSituacao(dadosSQL.getString("arquivo.situacao"));
			obj.getDocumentoAssinado().getArquivo().setApresentarPortalProfessor((dadosSQL.getBoolean("arquivo.apresentarPortalProfessor")));
			obj.getDocumentoAssinado().getArquivo().setApresentarPortalAluno((dadosSQL.getBoolean("arquivo.apresentarPortalAluno")));
			obj.getDocumentoAssinado().getArquivo().setApresentarPortalCoordenador((dadosSQL.getBoolean("arquivo.apresentarPortalCoordenador")));
			obj.getDocumentoAssinado().getArquivo().setControlarDownload((dadosSQL.getBoolean("arquivo.controlarDownload")));
			obj.getDocumentoAssinado().getArquivo().setPermitirArquivoResposta((dadosSQL.getBoolean("arquivo.permitirArquivoResposta")));
			obj.getDocumentoAssinado().getArquivo().setArquivoResposta((dadosSQL.getInt("arquivo.arquivoResposta")));
			obj.getDocumentoAssinado().getArquivo().setCpfAlunoDocumentacao(dadosSQL.getString("arquivo.cpfAlunoDocumentacao"));
			obj.getDocumentoAssinado().getArquivo().setCpfRequerimento(dadosSQL.getString("arquivo.cpfRequerimento"));
			obj.getDocumentoAssinado().getArquivo().getResponsavelUpload().setCodigo(dadosSQL.getInt("usuario.codigo"));
			obj.getDocumentoAssinado().getArquivo().getResponsavelUpload().setNome(dadosSQL.getString("usuario.nome"));
			obj.getDocumentoAssinado().getArquivo().getProfessor().setCodigo(dadosSQL.getInt("arquivo.professor"));
			obj.getDocumentoAssinado().getArquivo().getPessoaVO().setCodigo(dadosSQL.getInt("arquivo.pessoa"));
			obj.getDocumentoAssinado().getArquivo().getCurso().setCodigo(dadosSQL.getInt("arquivo.curso"));
			obj.getDocumentoAssinado().getArquivo().getTurma().setCodigo(dadosSQL.getInt("arquivo.turma"));
			obj.getDocumentoAssinado().getArquivo().getDisciplina().setCodigo(dadosSQL.getInt("arquivo.disciplina"));
			obj.getDocumentoAssinado().getArquivo().setApresentarDeterminadoPeriodo(dadosSQL.getBoolean("arquivo.apresentarDeterminadoPeriodo"));
			obj.getDocumentoAssinado().getArquivo().setIndice(dadosSQL.getInt("arquivo.indice"));
			obj.getDocumentoAssinado().getArquivo().setAgrupador(dadosSQL.getString("arquivo.agrupador"));
			obj.getDocumentoAssinado().getArquivo().setIndiceAgrupador(dadosSQL.getInt("arquivo.indiceAgrupador"));
			obj.setNovoObj(false);
			return obj;
	}
	
	
    public ImpressaoContratoVO consultarPorMatriculaTextoPadrao(String matricula, Integer textoPadrao, int codigoProfessor) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT matricula, professor, textopadraodeclaracao, count(codigo) as quantidade FROM impressaodeclaracao ");
        sqlStr.append(" WHERE 1=1 AND textopadraodeclaracao = ").append(textoPadrao);
        if (Uteis.isAtributoPreenchido(matricula) && !Uteis.isAtributoPreenchido(codigoProfessor)) {
        	sqlStr.append(" and  impressaodeclaracao.matricula ilike '").append(matricula).append("' ");
		}
        if (!Uteis.isAtributoPreenchido(matricula) && Uteis.isAtributoPreenchido(codigoProfessor)) {
        	sqlStr.append(" and  impressaodeclaracao.professor = ").append(codigoProfessor);
		}
        sqlStr.append(" group by matricula, professor, textopadraodeclaracao ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

        ImpressaoContratoVO obj = new ImpressaoContratoVO();
        if (tabelaResultado.next()) {
            obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
            obj.getProfessor().setCodigo(tabelaResultado.getInt("professor"));
            obj.setTextoPadraoDeclaracao(tabelaResultado.getInt("textopadraodeclaracao"));
            obj.setQuantidade(tabelaResultado.getInt("quantidade"));
            obj.setNovoObj(false);
            return obj;
        } else {
            obj.setNovoObj(true);
            return obj;
        }
    }
    
    public List<ImpressaoDeclaracaoVO>  consultarImpressaoDeclaracaoPorTipoDeclaracao(String matricula, Integer professor, Integer textoPadrao, String tipoDeclaracao, UsuarioVO usuarioVO) throws Exception {
    	if (Uteis.isAtributoPreenchido(tipoDeclaracao) && tipoDeclaracao.equals("PR")) {
    		return consultarPorProfessor(professor, tipoDeclaracao, textoPadrao, usuarioVO);
    	} 
    	return consultarPorMatricula(matricula, tipoDeclaracao, textoPadrao, usuarioVO);
    }
    
    public List<ImpressaoDeclaracaoVO> consultarPorMatriculaPorTextoPadrao(String matricula, Integer textoPadrao, TipoDoTextoImpressaoContratoEnum tipoTextoImpressao,  UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT impressaodeclaracao.matricula, pessoa.nome, impressaodeclaracao.textopadraodeclaracao, textoPadraoDeclaracao.descricao, ");
		sqlStr.append(" impressaodeclaracao.dataGeracao, impressaodeclaracao.requerimento, documentoassinado.codigo as \"documentoassinadoCodigo\", arquivo.codigo as \"arquivoCodigo\" FROM impressaodeclaracao ");
		sqlStr.append(" INNER JOIN textoPadraoDeclaracao ON textoPadraoDeclaracao.codigo = impressaodeclaracao.textopadraodeclaracao ");
		sqlStr.append(" INNER JOIN matricula on matricula.matricula = impressaoDeclaracao.matricula ");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = matricula.aluno ");
		sqlStr.append(" INNER JOIN documentoassinado ON impressaodeclaracao.documentoassinado = documentoassinado.codigo ");
		sqlStr.append(" INNER JOIN arquivo ON documentoassinado.arquivo = arquivo.codigo ");
		sqlStr.append(" WHERE impressaodeclaracao.matricula like '").append(matricula).append("' ");
		sqlStr.append(" AND impressaodeclaracao.tipotextoenum = '").append(tipoTextoImpressao.name()).append("' ");
		sqlStr.append(" AND impressaodeclaracao.textopadraodeclaracao = ").append(textoPadrao).append(" ");
//		sqlStr.append(" AND impressaodeclaracao.documentoassinado is not null");
		sqlStr.append(" order by impressaodeclaracao.dataGeracao desc");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<ImpressaoDeclaracaoVO> listaImpressaoDeclaracao = new ArrayList<ImpressaoDeclaracaoVO>(0);
		while (tabelaResultado.next()) {
			ImpressaoDeclaracaoVO obj = new ImpressaoDeclaracaoVO();
			obj.setDataGeracao(tabelaResultado.getDate("dataGeracao"));
			obj.getMatricula().setMatricula(tabelaResultado.getString("matricula"));
			obj.getMatricula().getAluno().setNome(tabelaResultado.getString("nome"));
			obj.getTextoPadraoDeclaracao().setCodigo(tabelaResultado.getInt("textopadraodeclaracao"));
			obj.getTextoPadraoDeclaracao().setDescricao(tabelaResultado.getString("descricao"));
			obj.getRequerimentoVO().setCodigo(tabelaResultado.getInt("requerimento"));
			obj.getDocumentoAssinadoVO().setCodigo(tabelaResultado.getInt("documentoassinadoCodigo"));
			obj.getDocumentoAssinadoVO().getArquivo().setCodigo(tabelaResultado.getInt("arquivoCodigo"));			
			listaImpressaoDeclaracao.add(obj);
		}
		return listaImpressaoDeclaracao;
	}

    public List<ImpressaoDeclaracaoVO> consultarPorMatricula(String matricula, String tipoDeclaracao, Integer textoPadrao, UsuarioVO usuarioVO) throws Exception {
    	StringBuilder sqlStr = new StringBuilder("SELECT impressaodeclaracao.matricula, pessoa.nome, impressaodeclaracao.textopadraodeclaracao, textoPadraoDeclaracao.descricao, count(impressaodeclaracao.codigo) as quantidade, impressaodeclaracao.entregue FROM impressaodeclaracao ");
    	sqlStr.append(" INNER JOIN textoPadraoDeclaracao ON textoPadraoDeclaracao.codigo = impressaodeclaracao.textopadraodeclaracao ");
   		sqlStr.append(" INNER JOIN matricula on matricula.matricula = impressaoDeclaracao.matricula ");
   		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = matricula.aluno ");
   		sqlStr.append(" WHERE impressaodeclaracao.matricula like '").append(matricula).append("' ");
   		if(Uteis.isAtributoPreenchido(textoPadrao)){
   			sqlStr.append(" and textoPadraoDeclaracao.codigo = ").append(textoPadrao).append("  ");
   		}
    	sqlStr.append(" group by impressaodeclaracao.matricula, pessoa.nome, impressaodeclaracao.textopadraodeclaracao, textoPadraoDeclaracao.descricao, impressaodeclaracao.entregue ");
    	sqlStr.append(" order by textoPadraoDeclaracao.descricao");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    	List<ImpressaoDeclaracaoVO> listaImpressaoDeclaracao = new ArrayList<ImpressaoDeclaracaoVO>(0);
    	while (tabelaResultado.next()) {
    		ImpressaoDeclaracaoVO obj = new ImpressaoDeclaracaoVO();
    		obj.getMatricula().setMatricula(tabelaResultado.getString("matricula"));
    		obj.getMatricula().getAluno().setNome(tabelaResultado.getString("nome"));
    		obj.getTextoPadraoDeclaracao().setCodigo(tabelaResultado.getInt("textopadraodeclaracao"));
    		obj.getTextoPadraoDeclaracao().setDescricao(tabelaResultado.getString("descricao"));
    		obj.setQtdeImpressoes(tabelaResultado.getInt("quantidade"));
    		obj.setEntregue(tabelaResultado.getBoolean("entregue"));
    		listaImpressaoDeclaracao.add(obj);
    	}
    	return listaImpressaoDeclaracao;
    }
    
    public List<ImpressaoDeclaracaoVO> consultarPorProfessor(Integer professor, String tipoDeclaracao, Integer textoPadrao, UsuarioVO usuarioVO) throws Exception {
    	StringBuilder sqlStr = new StringBuilder("SELECT funcionario.matricula, pessoa.nome, impressaodeclaracao.professor, impressaodeclaracao.textopadraodeclaracao, textoPadraoDeclaracao.descricao, count(impressaodeclaracao.codigo) as quantidade, impressaodeclaracao.entregue FROM impressaodeclaracao ");
    	sqlStr.append(" INNER JOIN textoPadraoDeclaracao ON textoPadraoDeclaracao.codigo = impressaodeclaracao.textopadraodeclaracao ");
   		sqlStr.append(" INNER JOIN funcionario on funcionario.codigo = impressaoDeclaracao.professor ");
   		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");
   		sqlStr.append(" WHERE impressaodeclaracao.professor = ").append(professor);
   		if(Uteis.isAtributoPreenchido(textoPadrao)){
   			sqlStr.append(" and textoPadraoDeclaracao.codigo = ").append(textoPadrao).append("  ");
   		}
    	sqlStr.append(" group by funcionario.matricula, pessoa.nome, impressaodeclaracao.professor, impressaodeclaracao.textopadraodeclaracao, textoPadraoDeclaracao.descricao, impressaodeclaracao.entregue ");
    	sqlStr.append(" order by textoPadraoDeclaracao.descricao");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    	List<ImpressaoDeclaracaoVO> listaImpressaoDeclaracao = new ArrayList<ImpressaoDeclaracaoVO>(0);
    	while (tabelaResultado.next()) {
    		ImpressaoDeclaracaoVO obj = new ImpressaoDeclaracaoVO();
    		obj.getProfessor().setMatricula(tabelaResultado.getString("matricula"));
    		obj.getProfessor().getPessoa().setNome(tabelaResultado.getString("nome"));
    		obj.getProfessor().setCodigo(tabelaResultado.getInt("professor"));
    		obj.getTextoPadraoDeclaracao().setCodigo(tabelaResultado.getInt("textopadraodeclaracao"));
    		obj.getTextoPadraoDeclaracao().setDescricao(tabelaResultado.getString("descricao"));
    		obj.setQtdeImpressoes(tabelaResultado.getInt("quantidade"));
    		obj.setEntregue(tabelaResultado.getBoolean("entregue"));
    		listaImpressaoDeclaracao.add(obj);
    	}
    	return listaImpressaoDeclaracao;
    }
    
    public List<ImpressaoContratoVO> consultarAlunosTurmaVOs(Integer turma, UsuarioVO usuarioVO) {
        StringBuilder sb = new StringBuilder();
        sb.append("select matricula.matricula, turma.identificadorturma, pessoa.nome, pessoa.codigo, matriculaPeriodo.codigo AS \"matriculaPeriodo\" from turma ");
        sb.append(" inner join matriculaperiodo on matriculaperiodo.turma = turma.codigo ");
        sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
        sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
        sb.append(" where turma.codigo = ").append(turma);
        sb.append(" order by pessoa.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        List<ImpressaoContratoVO> listaAlunosTurma = new ArrayList<ImpressaoContratoVO>(0);
        while (tabelaResultado.next()) {
            ImpressaoContratoVO obj = new ImpressaoContratoVO();
            obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
            obj.getMatriculaVO().getAluno().setCodigo(tabelaResultado.getInt("codigo"));
            obj.getMatriculaVO().getAluno().setNome(tabelaResultado.getString("nome"));
            obj.getTurmaVO().setIdentificadorTurma(tabelaResultado.getString("identificadorturma"));
            obj.getMatriculaPeriodoVO().setCodigo(tabelaResultado.getInt("matriculaPeriodo"));
            listaAlunosTurma.add(obj);
        }
        return listaAlunosTurma;
    }

    public void realizarGarantiaMarcacaoUnicoCheck(List<ImpressaoContratoVO> listaAlunosVOs, ImpressaoContratoVO obj) {
        listaAlunosVOs.forEach(impressaoContrato->{impressaoContrato.setAlunoSelecionado(impressaoContrato.getMatriculaVO().getMatricula().equals(obj.getMatriculaVO().getMatricula()));});
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoEntregueTextoPadraoMatricula(final Integer textoPadraoDeclaracao, final String matricula, final Boolean entregue, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "UPDATE impressaoDeclaracao set entregue=? WHERE textoPadraoDeclaracao = ? AND matricula = ?";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    int i = 0;
                    sqlAlterar.setBoolean(++i, entregue);
                    sqlAlterar.setInt(++i, textoPadraoDeclaracao.intValue());
                    sqlAlterar.setString(++i, matricula.toString());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoEntregueTextoPadraoProfessor(final Integer textoPadraoDeclaracao, final Integer professor, final Boolean entregue, UsuarioVO usuario) throws Exception {
    	try {
    		final String sql = "UPDATE impressaoDeclaracao set entregue=? WHERE textoPadraoDeclaracao = ? AND professor = ?";
    		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
    			
    			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
    				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
    				int i = 0;
    				sqlAlterar.setBoolean(++i, entregue);
    				sqlAlterar.setInt(++i, textoPadraoDeclaracao.intValue());
    				sqlAlterar.setInt(++i, professor);
    				return sqlAlterar;
    			}
    		});
    	} catch (Exception e) {
    		throw e;
    	}
    }
    
    
    public void alterarImpressaoDeclaracaoSituacaoEntregue(List<ImpressaoDeclaracaoVO> listaImpressaoDeclaracaoVOs, String tipoDeclaracao, UsuarioVO usuarioVO) throws Exception {
    	if (tipoDeclaracao.equals("AL")) {
    		for (ImpressaoDeclaracaoVO impressaoDeclaracaoVO : listaImpressaoDeclaracaoVOs) {
    			alterarSituacaoEntregueTextoPadraoMatricula(impressaoDeclaracaoVO.getTextoPadraoDeclaracao().getCodigo(), impressaoDeclaracaoVO.getMatricula().getMatricula(), impressaoDeclaracaoVO.getEntregue(), usuarioVO);
    		}
    	} else if (tipoDeclaracao.equals("PR")) {
    		for (ImpressaoDeclaracaoVO impressaoDeclaracaoVO : listaImpressaoDeclaracaoVOs) {
    			alterarSituacaoEntregueTextoPadraoProfessor(impressaoDeclaracaoVO.getTextoPadraoDeclaracao().getCodigo(), impressaoDeclaracaoVO.getProfessor().getCodigo(), impressaoDeclaracaoVO.getEntregue(), usuarioVO);
    		}
    	} else {
    		for (ImpressaoDeclaracaoVO impressaoDeclaracaoVO : listaImpressaoDeclaracaoVOs) {
    			alterarSituacaoEntregueTextoPadraoMatricula(impressaoDeclaracaoVO.getTextoPadraoDeclaracao().getCodigo(), impressaoDeclaracaoVO.getMatricula().getMatricula(), impressaoDeclaracaoVO.getEntregue(), usuarioVO);
    		}
    	}
    }
    
    @Override
	public void validarDadosPermissaoImpressaoContrato(TextoPadraoDeclaracaoVO textoPadraoImprimirVO, UsuarioVO usuario) throws Exception {
		if (!textoPadraoImprimirVO.getTextoPadraoDeclaracaofuncionarioVOs().isEmpty()) {
			Iterator i = textoPadraoImprimirVO.getTextoPadraoDeclaracaofuncionarioVOs().iterator();
			boolean encontrouFuncionario = false;
			while (i.hasNext()) {
				TextoPadraoDeclaracaoFuncionarioVO func = (TextoPadraoDeclaracaoFuncionarioVO)i.next();
				if (func.getFuncionario().getPessoa().getCodigo().intValue() == usuario.getPessoa().getCodigo().intValue()) {
					encontrouFuncionario = true;
					return;
				}
			}
			if (!encontrouFuncionario) {
				throw new Exception("O Usuário " + usuario.getNome() + " não possui permissão para realizar impressão do contrato (" + textoPadraoImprimirVO.getDescricao() + ")");
			}
		}
	}
    
    public void carregarDadosDataInicioFimAula(ImpressaoContratoVO impressaoContratoVO, UsuarioVO usuarioLogado, HistoricoVO hist) {
		try {
			
			Date dataInicioAula = null;
			Date dataFimAula = null;
			if(hist.getDataInicioAula() == null) {
			if (Uteis.isAtributoPreenchido(hist.getMatriculaPeriodoTurmaDisciplina().getTurmaTeorica())) {
				dataInicioAula = getFacadeFactory().getHorarioTurmaDiaFacade().consultarPrimeiraDataAulaPorTurmaDisciplina(hist.getMatriculaPeriodoTurmaDisciplina().getTurmaTeorica().getCodigo(), hist.getDisciplina().getCodigo(), hist.getAnoHistorico(), hist.getSemestreHistorico());
				dataFimAula = getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorTurmaDisciplina(hist.getMatriculaPeriodoTurmaDisciplina().getTurmaTeorica().getCodigo(), hist.getDisciplina().getCodigo(), hist.getAnoHistorico(), hist.getSemestreHistorico(), null);
			} else if (Uteis.isAtributoPreenchido(hist.getMatriculaPeriodoTurmaDisciplina().getTurmaPratica())) {
				dataInicioAula = getFacadeFactory().getHorarioTurmaDiaFacade().consultarPrimeiraDataAulaPorTurmaDisciplina(hist.getMatriculaPeriodoTurmaDisciplina().getTurmaPratica().getCodigo(), hist.getDisciplina().getCodigo(), hist.getAnoHistorico(), hist.getSemestreHistorico());
				dataFimAula = getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorTurmaDisciplina(hist.getMatriculaPeriodoTurmaDisciplina().getTurmaPratica().getCodigo(), hist.getDisciplina().getCodigo(), hist.getAnoHistorico(), hist.getSemestreHistorico(), null);
			} else if (Uteis.isAtributoPreenchido(hist.getMatriculaPeriodoTurmaDisciplina().getTurma())) {
				dataInicioAula = getFacadeFactory().getHorarioTurmaDiaFacade().consultarPrimeiraDataAulaPorTurmaDisciplina(hist.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo(), hist.getDisciplina().getCodigo(), hist.getAnoHistorico(), hist.getSemestreHistorico());
				dataFimAula = getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorTurmaDisciplina(hist.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo(), hist.getDisciplina().getCodigo(), hist.getAnoHistorico(), hist.getSemestreHistorico(), null);
			} else {
				dataInicioAula = getFacadeFactory().getHorarioTurmaDiaFacade().consultarPrimeiraDataAulaPorTurmaDisciplina(impressaoContratoVO.getMatriculaPeriodoVO().getTurma().getCodigo(), hist.getDisciplina().getCodigo(), hist.getAnoHistorico(), hist.getSemestreHistorico());
				dataFimAula = getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorTurmaDisciplina(impressaoContratoVO.getMatriculaPeriodoVO().getTurma().getCodigo(), hist.getDisciplina().getCodigo(), hist.getAnoHistorico(), hist.getSemestreHistorico(), null);
			}
			if(!Uteis.isAtributoPreenchido(hist.getDataInicioAula())) {
				hist.setDataInicioAula(dataInicioAula);
				hist.setDateDataInicioAula(dataInicioAula);
			}
			if(!Uteis.isAtributoPreenchido(hist.getDataFimAula())) {
				hist.setDataFimAula(dataFimAula);
			}
			}
			
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
    
    private List<ImpressaoContratoVO> consultarImpressaoDeclaracaoPorMatricula(String matricula) throws Exception {
		List<ImpressaoContratoVO> lista = new ArrayList<>();
		StringBuilder sqlStr = new StringBuilder(getSqlConsultaPadraoImpressaoDeclaracaoDocumentoAssinado());
		sqlStr.append(" left join documentoassinado on impressaodeclaracao.documentoassinado = documentoassinado.codigo ");
		sqlStr.append(" left join arquivo on arquivo.codigo = documentoassinado.arquivo ");
		sqlStr.append(" left join usuario on usuario.codigo = documentoassinado.usuario ");
		sqlStr.append(" WHERE impressaodeclaracao.matricula = ? ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
		while (dadosSQL.next()) {
			ImpressaoContratoVO obj = montarDadosImpressaoDeclaracao(dadosSQL, 0, 0);
			lista.add(obj);
		}
		return lista;
	}
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
    public void excluirImpressaoDeclaracaoPorMatricula(MatriculaVO matriculaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
    	if (matriculaVO != null && !matriculaVO.getMatricula().isEmpty()) {
    		List<ImpressaoContratoVO> impressaoContratoVOs = consultarImpressaoDeclaracaoPorMatricula(matriculaVO.getMatricula());
    		excluirImpressaoDeclaracao(impressaoContratoVOs, usuarioVO, configuracaoGeralSistemaVO);
    	}
    }
    
	public void carregarTotalSemestresCurso(GradeCurricularVO gradeCurricularVO, String texto, UsuarioVO usuarioVO) throws Exception {
		if (texto.contains("TotalSemestres_Curso") && Uteis.isAtributoPreenchido(gradeCurricularVO)	&& gradeCurricularVO.getPeriodoLetivosVOs().isEmpty()) {
			gradeCurricularVO.setPeriodoLetivosVOs(getFacadeFactory().getPeriodoLetivoFacade().consultarPorGradeCurricular(gradeCurricularVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		}
	}
    
    private void validarAssinaturaFuncionarioContrato(int codigoTextoPadrao ,ImpressaoContratoVO impressaoContratoVO) {
    	try {
	    	TextoPadraoVO textoPadrao = new TextoPadraoVO();
			textoPadrao = getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(codigoTextoPadrao, Uteis.NIVELMONTARDADOS_TODOS, null);
	
			if (Uteis.isAtributoPreenchido(textoPadrao) && Uteis.isAtributoPreenchido(textoPadrao.getFuncionarioPrimarioVO())) {
				impressaoContratoVO.getDocumentoAssinado().getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), textoPadrao.getFuncionarioPrimarioVO().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, impressaoContratoVO.getDocumentoAssinado(), 2, textoPadrao.getCargoFuncionarioPrincipalVO().getNome(), "" , textoPadrao.getAssinarDocumentoAutomaticamenteFuncionarioPrimario(), textoPadrao.getFuncionarioPrimarioVO().getPessoa().getTipoAssinaturaDocumentoEnum()));
			}
			if (Uteis.isAtributoPreenchido(textoPadrao) && Uteis.isAtributoPreenchido(textoPadrao.getFuncionarioSecundarioVO())) {
				impressaoContratoVO.getDocumentoAssinado().getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(),  textoPadrao.getFuncionarioSecundarioVO().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, impressaoContratoVO.getDocumentoAssinado(), 3, textoPadrao.getCargoFuncionarioSecundarioVO().getNome(), "" , textoPadrao.getAssinarDocumentoAutomaticamenteFuncionarioSecundario(), textoPadrao.getFuncionarioSecundarioVO().getPessoa().getTipoAssinaturaDocumentoEnum()));
			}
			if (Uteis.isAtributoPreenchido(textoPadrao) && Uteis.isAtributoPreenchido(impressaoContratoVO.getDocumentoAssinado().getArquivo().getPessoaVO())) {
				impressaoContratoVO.getDocumentoAssinado().getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(),  impressaoContratoVO.getDocumentoAssinado().getArquivo().getPessoaVO(), TipoPessoa.ALUNO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, impressaoContratoVO.getDocumentoAssinado(), 1, null, "" , null, impressaoContratoVO.getDocumentoAssinado().getArquivo().getPessoaVO().getTipoAssinaturaDocumentoEnum()));
			}
			if(Uteis.isAtributoPreenchido(textoPadrao ) && Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().getOperadorResponsavel().getPessoa())) {
				 String nomeCargo = "";
				if(!impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().getOperadorResponsavel().getFuncionarioCargoVOs().isEmpty()) {
					if(Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().getOperadorResponsavel().getFuncionarioCargoVOs().get(0).getCargo().getNome())) {
						nomeCargo =impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().getOperadorResponsavel().getFuncionarioCargoVOs().get(0).getCargo().getNome();
					}	           
				}
				impressaoContratoVO.getDocumentoAssinado().getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(),  impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().getOperadorResponsavel().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, impressaoContratoVO.getDocumentoAssinado(), 4, nomeCargo, "" , Boolean.TRUE, impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().getOperadorResponsavel().getPessoa().getTipoAssinaturaDocumentoEnum()));
			}
			
		
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private void validarCamposAssinaturaContrato(ImpressaoContratoVO impressaoContratoVO) {
		if (Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaPeriodoVO())) {
			impressaoContratoVO.getDocumentoAssinado().setMatriculaPeriodo(impressaoContratoVO.getMatriculaPeriodoVO());
		}
		if (Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaPeriodoVO().getAno())) {
			impressaoContratoVO.getDocumentoAssinado().setAno(impressaoContratoVO.getMatriculaPeriodoVO().getAno());
		}
		if (Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaPeriodoVO().getSemestre())) {
			impressaoContratoVO.getDocumentoAssinado().setSemestre(impressaoContratoVO.getMatriculaPeriodoVO().getSemestre());
		}
		if (Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaPeriodoVO().getTurma())) {
			impressaoContratoVO.getDocumentoAssinado().setTurma(impressaoContratoVO.getMatriculaPeriodoVO().getTurma());
		}
	}    
   
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
    public String gerarDeclaracaoLGPD(UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuarioVO) throws Exception {
    	String nomeArquivoOrigem = null;
    	try {
 
    		ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getAplicacaoControle().getConfiguracaoGeralSistemaVO(unidadeEnsinoVO.getCodigo(), usuarioVO);	
			PessoaVO pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(usuarioVO.getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			TextoPadraoDeclaracaoVO texto = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(configuracaoGeralSistemaVO.getTextoPadraoDadosSensiveisLGPD().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			OcorrenciaLGPDVO ocorrenciaLGPDVO = new OcorrenciaLGPDVO();
		
			impressaoContratoVO.setTextoPadraoDeclaracao(texto.getCodigo());
			impressaoContratoVO.setPessoaVO(pessoaVO);
			impressaoContratoVO.setUsuarioLogado(usuarioVO);
			impressaoContratoVO.setConfiguracaoGeralSistemaVO(configuracaoGeralSistemaVO);
			
			if(texto.getTexto().contains("Cpf_Aluno") 
			 ||texto.getTexto().contains("Rg_Aluno")
			 ||texto.getTexto().contains("Email_Aluno")
			 ||texto.getTexto().contains("Endereco_Aluno")
			 ||texto.getTexto().contains("ComplementoEnd_Aluno")
			 ||texto.getTexto().contains("CEP_Aluno")
			 ||texto.getTexto().contains("Celular_Aluno")
			 ||texto.getTexto().contains("Bairro_Aluno")
			 ||texto.getTexto().contains("DataEmissaoRG_Aluno")){
				
			impressaoContratoVO.getPessoaVO().setCPF(Uteis.realizarAnonimizacaoIgnorandoTagNegrito(pessoaVO.getCPF(),10,3,2)); 
			impressaoContratoVO.getPessoaVO().setRG(Uteis.realizarAnonimizacaoIgnorandoTagNegrito(pessoaVO.getRG(),5,1,2)); 
			impressaoContratoVO.getPessoaVO().setEmail(Uteis.realizarAnonimizacaoEmailIgnorandoTagNegrito(pessoaVO.getEmail()));
			impressaoContratoVO.getPessoaVO().setEndereco(Uteis.realizarAnonimizacaoIgnorandoTagNegrito(pessoaVO.getEndereco(),5,2,4));
			impressaoContratoVO.getPessoaVO().setComplemento(Uteis.realizarAnonimizacaoIgnorandoTagNegrito(pessoaVO.getComplemento(),5,0,4));
			impressaoContratoVO.getPessoaVO().setCEP(Uteis.realizarAnonimizacaoIgnorandoTagNegrito(pessoaVO.getCEP(),8,0,3));
			impressaoContratoVO.getPessoaVO().setCelular(Uteis.realizarAnonimizacaoIgnorandoTagNegrito(pessoaVO.getCelular(), 10,0,4));
			impressaoContratoVO.getPessoaVO().setSetor(Uteis.realizarAnonimizacaoIgnorandoTagNegrito(pessoaVO.getSetor(), 5,2,4));
			impressaoContratoVO.getPessoaVO().setDataEmissaoRGAnonimizada(pessoaVO.getDataEmissaoRGAnonimizada());
			}

			String textoStr = "";
			texto.substituirTag(impressaoContratoVO, usuarioVO);
			textoStr = getFacadeFactory().getTextoPadraoDeclaracaoFacade().removerBordaDaPagina(texto.getTexto());
			textoStr = getFacadeFactory().getTextoPadraoDeclaracaoFacade().adicionarStyleFormatoPaginaTextoPadrao(textoStr, texto.getOrientacaoDaPagina());

			nomeArquivoOrigem = executarValidacaoImpressaoEmPdf(impressaoContratoVO, texto, textoStr, true, configuracaoGeralSistemaVO, usuarioVO);
			impressaoContratoVO.setImpressaoPdf(true);
			impressaoContratoVO.setImpressaoContratoExistente(impressaoContratoVO.getImpressaoContratoExistente());
			gravarImpressaoContrato(impressaoContratoVO);
		
			getFacadeFactory().getOcorrenciaLGPDInterfaceFacade().incluir(ocorrenciaLGPDVO, usuarioVO);

			if(ControleAcesso.verificarPermissaoFuncionalidadeUsuario("AcessoDados_permitirEnviarEmailDadosLGPD",usuarioVO) && configuracaoGeralSistemaVO.getAtivarIntegracaoLGPD()) {
				
			
			String mensagem = configuracaoGeralSistemaVO.getMensagemLGPD();
			mensagem = mensagem.replace("NOME_ALUNO", pessoaVO.getNome());
			mensagem = mensagem.replace("REGISTRO_ACADEMICO", pessoaVO.getRegistroAcademico());
			mensagem = mensagem.replace("NR_PROTOCOLO", ocorrenciaLGPDVO.getCodigo().toString());
			mensagem = mensagem.replace("UNIDADE_ENSINO", unidadeEnsinoVO.getNome());
			
			String assunto = configuracaoGeralSistemaVO.getAssuntoMensagemLGPD();
			
			assunto = assunto.replace("NOME_ALUNO", pessoaVO.getNome());
			assunto = assunto.replace("REGISTRO_ACADEMICO", pessoaVO.getRegistroAcademico());
			assunto = assunto.replace("NR_PROTOCOLO", ocorrenciaLGPDVO.getCodigo().toString());
			
			EmailVO email = new EmailVO();
			email.setAssunto(assunto);
			email.setMensagem(mensagem);
			email.setAnexarImagensPadrao(true);
			email.setEmailRemet(configuracaoGeralSistemaVO.getEmailRemetente());
			email.setNomeRemet(configuracaoGeralSistemaVO.getResponsavelPadraoComunicadoInterno().getNome());
			email.setEmailDest(configuracaoGeralSistemaVO.getEmailDestinatarioIntegracaoLGPD());
			email.setNomeDest(configuracaoGeralSistemaVO.getNomeDestinatarioEmailIntegracaoLGPD());
				
			email.setCaminhoAnexos(getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + nomeArquivoOrigem);
			getFacadeFactory().getEmailFacade().incluir(email);
			}
    	} catch (Exception e) {
				throw e;
			}
    	return nomeArquivoOrigem;
    }
}
