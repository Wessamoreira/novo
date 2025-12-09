package negocio.facade.jdbc.academico;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import negocio.comuns.utilitarias.Constantes;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.AutorizacaoCursoVO;
import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.ExpedicaoDiplomaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.LogImpressaoContratoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.academico.PlanoFinanceiroAlunoVO;
import negocio.comuns.academico.PlanoFinanceiroCursoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoContratoMatriculaEnum;
import negocio.comuns.academico.enumeradores.TipoDoTextoImpressaoContratoEnum;
import negocio.comuns.academico.enumeradores.TipoLivroRegistroDiplomaEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.DadosComerciaisVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ConvenioVO;
import negocio.comuns.financeiro.MatriculaPeriodoVencimentoVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrdemHistoricoDisciplina;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.ImpressaoContratoInterfaceFacade;
import relatorio.negocio.comuns.academico.BoletimAcademicoRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

/**
 *
 * @author Carlos
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class ImpressaoContrato extends ControleAcesso implements ImpressaoContratoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1948440716387619585L;

	public void consultarAlunoPorMatricula(ImpressaoContratoVO impressaoContratoVO, Integer unidadeEnsino, String ano, String semestre, UsuarioVO usuarioLogado) throws Exception {
		MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(impressaoContratoVO.getMatriculaVO().getMatricula(), unidadeEnsino, false, usuarioLogado);
		validarDadosMatricula(matriculaVO.getMatricula(), impressaoContratoVO.getMatriculaVO().getMatricula());
		impressaoContratoVO.setMatriculaVO(matriculaVO);
		impressaoContratoVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatriculaOrdenandoPorAnoSemestrePeriodoLetivo(impressaoContratoVO.getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
		validarDadosMatriculaPeriodo(impressaoContratoVO.getMatriculaPeriodoVO());
	}

	public List<ImpressaoContratoVO> consultarDadosGeracaoContrato(MatriculaVO matricula, String campoFiltroPor, TurmaVO turma, String semestre, String ano, Integer unidadeEnsino, Optional<Date> dataInicio, Optional<Date> dataFim, UsuarioVO usuarioLogado, CursoVO cursoVO, String tipoAluno,String situacaoContratoDigital) throws Exception {
		List<ImpressaoContratoVO> impressaoContratoVOs = new ArrayList<ImpressaoContratoVO>(0);
		if (campoFiltroPor.equals("matricula") || campoFiltroPor.equals("registroAcademico")) {
			getFacadeFactory().getMatriculaFacade().carregarDados(matricula, NivelMontarDados.BASICO, usuarioLogado);			
			validarDadosMatricula(matricula.getMatricula(), matricula.getMatricula());
			List<MatriculaPeriodoVO> matriculaPeriodoVOs = getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaPeriodosComContrato(matricula.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, usuarioLogado, false);
			Ordenacao.ordenarListaDecrescente(matriculaPeriodoVOs, "ordenacao");
			for(MatriculaPeriodoVO matriculaPeriodoVO: matriculaPeriodoVOs ){
				ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
				impressaoContratoVO.setMatriculaVO(matricula);				
				getFacadeFactory().getTurmaFacade().carregarDados(matriculaPeriodoVO.getTurma(), usuarioLogado);
				impressaoContratoVO.setMatriculaPeriodoVO(matriculaPeriodoVO);			
				impressaoContratoVO.getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().addAll(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorMatriculaPeriodo(impressaoContratoVO.getMatriculaPeriodoVO().getCodigo(),Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
				impressaoContratoVO.setListaLogImpressaoContratoVO(getFacadeFactory().getLogImpressaoContratoFacade().consultarPorMatricula(matricula.getMatricula(), usuarioLogado));
				impressaoContratoVOs.add(impressaoContratoVO);
			}
		} else if (campoFiltroPor.equals("turma")) {
			if (turma.getCodigo() == null || turma.getCodigo() == 0) {
				throw new Exception("O campo TURMA deve ser informado.");
			}
			if (!turma.getCurso().getNivelEducacional().equals("PO") && !turma.getCurso().getNivelEducacional().equals("EX") && semestre.trim().isEmpty()) {
				throw new Exception("O campo SEMESTRE deve ser informado.");
			}
			if (!turma.getCurso().getNivelEducacional().equals("PO") && !turma.getCurso().getNivelEducacional().equals("EX") && (ano == null || ano.trim().isEmpty() || ano.trim().length() != 4)) {
				throw new Exception("O campo ANO deve ser informado.");
			}
			List<MatriculaVO> matriculaVOs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorTurma(turma.getCodigo(), "", ano, semestre, dataInicio, dataFim, usuarioLogado, tipoAluno,situacaoContratoDigital);
			for (MatriculaVO matriculaVO : matriculaVOs) {
				ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
				impressaoContratoVO.setMatriculaVO(matriculaVO);
				impressaoContratoVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatriculaSemestreAno(matriculaVO.getMatricula(), semestre, ano, true, false, dataInicio, dataFim, usuarioLogado));
				impressaoContratoVO.getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().addAll(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorMatriculaPeriodo(impressaoContratoVO.getMatriculaPeriodoVO().getCodigo(),Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
				impressaoContratoVOs.add(impressaoContratoVO);
			}
		} else {
			if (cursoVO.getCodigo() == null || cursoVO.getCodigo() == 0) {
				throw new Exception("O campo CURSO deve ser informado.");
			}
			if (!cursoVO.getNivelEducacional().equals("PO") && !cursoVO.getNivelEducacional().equals("EX") && semestre.trim().isEmpty()) {
				throw new Exception("O campo SEMESTRE deve ser informado.");
			}
			if (!cursoVO.getNivelEducacional().equals("PO") && !cursoVO.getNivelEducacional().equals("EX") && (ano == null || ano.trim().isEmpty() || ano.trim().length() != 4)) {
				throw new Exception("O campo ANO deve ser informado.");
			}
			List<MatriculaVO> matriculaVOs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorCurso(cursoVO.getCodigo(), ano, semestre, unidadeEnsino, false, "", usuarioLogado, tipoAluno,situacaoContratoDigital);
			for (MatriculaVO matriculaVO : matriculaVOs) {
				ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
				impressaoContratoVO.setMatriculaVO(matriculaVO);
				impressaoContratoVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatriculaSemestreAno(matriculaVO.getMatricula(), semestre, ano, true, false, dataInicio, dataFim, usuarioLogado));
				impressaoContratoVO.getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().addAll(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorMatriculaPeriodo(impressaoContratoVO.getMatriculaPeriodoVO().getCodigo(),Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
				impressaoContratoVOs.add(impressaoContratoVO);
			}
		}
		return impressaoContratoVOs;
	}

	public List<MatriculaVO> consultarAluno(String valorConsultaAluno, String campoConsultaAluno, Integer unidadeEnsino, UsuarioVO usuarioLogado) throws Exception {
		List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
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
			objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(valorConsultaAluno, unidadeEnsino, false, usuarioLogado);
		}
		if (campoConsultaAluno.equals("nomeCurso")) {
			objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(valorConsultaAluno, unidadeEnsino, false, usuarioLogado);
		}
		if (campoConsultaAluno.equals("registroAcademico")) {
			objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(valorConsultaAluno, unidadeEnsino, false, usuarioLogado);
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
			throw new Exception("Não foi encontrado nenhuma Matricula Periodo para esta Matricula.");
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String imprimirContratoRenovarMatricula(String tipoContrato, MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoFinanceiroVO configFinanceira, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioLogado) throws Exception {
		ImpressaoContratoVO impressaoContratoVO =  new ImpressaoContratoVO();		 
		impressaoContratoVO.setMatriculaVO(matricula);
		impressaoContratoVO.setMatriculaPeriodoVO(matriculaPeriodoVO);
		impressaoContratoVO.setTipoTextoEnum(TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO);
		impressaoContratoVO.setUsuarioLogado(usuarioLogado);
		impressaoContratoVO.setGerarNovoArquivoAssinado(true);
		return imprimirContrato(tipoContrato, impressaoContratoVO, config, configFinanceira, usuarioLogado);
	
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String imprimirContratoVisaoAluno(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoFinanceiroVO configFinanceira, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioLogado) throws Exception {		
		TextoPadraoVO texto = null;
		if (!matricula.getTipoMatricula().equals("EX") && Uteis.isAtributoPreenchido(matriculaPeriodoVO.getContratoMatricula())) {
			texto = matriculaPeriodoVO.getContratoMatricula();
			texto.setTipo("MA");
		} else if (matricula.getTipoMatricula().equals("EX") && Uteis.isAtributoPreenchido(matriculaPeriodoVO.getContratoExtensao())) {
			texto = matriculaPeriodoVO.getContratoExtensao();
			texto.setTipo("EX");
		} else if (Uteis.isAtributoPreenchido(matriculaPeriodoVO.getContratoFiador())) {
			texto = matriculaPeriodoVO.getContratoFiador();
			texto.setTipo("FI");
		}else {
			getFacadeFactory().getMatriculaPeriodoFacade().realizarDefinicaoContratoPadraoSerUsadoMatriculaPeriodo(TipoContratoMatriculaEnum.getEnumPorValor(matricula.getTipoMatricula()), matriculaPeriodoVO, false, true, usuarioLogado);
			if (Uteis.isAtributoPreenchido(matriculaPeriodoVO.getContratoMatricula())) {
				texto = matriculaPeriodoVO.getContratoMatricula();
				texto.setTipo("MA");
			} else if (Uteis.isAtributoPreenchido(matriculaPeriodoVO.getContratoExtensao())) {
				texto = matriculaPeriodoVO.getContratoExtensao();
				texto.setTipo("EX");
			} else if (Uteis.isAtributoPreenchido(matriculaPeriodoVO.getContratoFiador())) {
				texto = matriculaPeriodoVO.getContratoFiador();
			}
		}
		return carregarDadosParaImpressaoContratoVisaoAluno(texto, matricula, matriculaPeriodoVO, configFinanceira, config, usuarioLogado);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String executarValidacaoTipoImpressaoContrato(TextoPadraoVO texto, String contratoPronto, ImpressaoContratoVO impressaoContratoVO, ConfiguracaoGeralSistemaVO config,Boolean regerarDocumentoAssinado, UsuarioVO usuario) throws Exception{
		String nomeArquivo = "";
		if (texto.getTipoDesigneTextoEnum().isHtml()) {
			impressaoContratoVO.setImpressaoPdf(false);
			((HttpServletRequest) context().getExternalContext().getRequest()).getSession().setAttribute("textoRelatorio", contratoPronto);
		} else {
			impressaoContratoVO.setImpressaoPdf(true);
			LogImpressaoContratoVO logImpressaoContrato = getFacadeFactory().getLogImpressaoContratoFacade().consultarUltimoContratoPorMatriculaPorTextoPadrao(impressaoContratoVO.getMatriculaVO().getMatricula(), texto.getCodigo(), texto.getTipo(), usuario);
			if (Uteis.isAtributoPreenchido(logImpressaoContrato.getImpressaoContrato().getDocumentoAssinado()) && !regerarDocumentoAssinado) {
				if(logImpressaoContrato.getImpressaoContrato().getDocumentoAssinado().getProvedorDeAssinaturaEnum().isProvedorCertisign() || logImpressaoContrato.getImpressaoContrato().getDocumentoAssinado().getProvedorDeAssinaturaEnum().isProvedorImprensaOficial()) {
					getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorCertisign(logImpressaoContrato.getImpressaoContrato().getDocumentoAssinado(), config, usuario);
				}
				if(logImpressaoContrato.getImpressaoContrato().getDocumentoAssinado().getProvedorDeAssinaturaEnum().isProvedorTechCert()) {
					getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorTechCert(logImpressaoContrato.getImpressaoContrato().getDocumentoAssinado(), config, usuario);
				}
				getFacadeFactory().getArquivoHelper().disponibilizarArquivoAssinadoParaDowload(config.getLocalUploadArquivoFixo() + File.separator + logImpressaoContrato.getImpressaoContrato().getDocumentoAssinado().getArquivo().getPastaBaseArquivo() + File.separator + logImpressaoContrato.getImpressaoContrato().getDocumentoAssinado().getArquivo().getNome(), logImpressaoContrato.getImpressaoContrato().getDocumentoAssinado().getArquivo().getNome());
				return logImpressaoContrato.getImpressaoContrato().getDocumentoAssinado().getArquivo().getNome();
			}
			logImpressaoContrato.setAssinado(Boolean.FALSE);
			logImpressaoContrato.setMatricula(impressaoContratoVO.getMatriculaVO());
			logImpressaoContrato.setTipoContrato(texto.getTipo());
			logImpressaoContrato.setTextoPadrao(texto);
			logImpressaoContrato.setUsuarioRespImpressao(usuario);
			logImpressaoContrato.setImpressaoContrato(impressaoContratoVO);			
			logImpressaoContrato.getImpressaoContrato().setUsuarioLogado(usuario);
			logImpressaoContrato.getImpressaoContrato().setTextoPadraoDeclaracao(texto.getCodigo());
			logImpressaoContrato.getImpressaoContrato().setTipoTextoEnum(TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO);
			logImpressaoContrato.getImpressaoContrato().setGerarNovoArquivoAssinado(regerarDocumentoAssinado);			
			nomeArquivo = getFacadeFactory().getImpressaoDeclaracaoFacade().executarValidacaoImpressaoEmPdf(logImpressaoContrato.getImpressaoContrato(), texto, contratoPronto, true, config, usuario);
			getFacadeFactory().getImpressaoDeclaracaoFacade().gravarImpressaoContrato(logImpressaoContrato.getImpressaoContrato());
			getFacadeFactory().getLogImpressaoContratoFacade().incluir(logImpressaoContrato, usuario);
			if (Uteis.isAtributoPreenchido(logImpressaoContrato.getImpressaoContrato().getDocumentoAssinado())) {
				if(logImpressaoContrato.getImpressaoContrato().getDocumentoAssinado().getProvedorDeAssinaturaEnum().isProvedorCertisign() || logImpressaoContrato.getImpressaoContrato().getDocumentoAssinado().getProvedorDeAssinaturaEnum().isProvedorImprensaOficial()) {
					getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorCertisign(logImpressaoContrato.getImpressaoContrato().getDocumentoAssinado(), config, usuario);
				}
				if(logImpressaoContrato.getImpressaoContrato().getDocumentoAssinado().getProvedorDeAssinaturaEnum().isProvedorTechCert() || logImpressaoContrato.getImpressaoContrato().getDocumentoAssinado().getProvedorDeAssinaturaEnum().isProvedorTechCert()) {
					getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorTechCert(logImpressaoContrato.getImpressaoContrato().getDocumentoAssinado(), config, usuario);
				}
				getFacadeFactory().getArquivoHelper().disponibilizarArquivoAssinadoParaDowload(config.getLocalUploadArquivoFixo() + File.separator + logImpressaoContrato.getImpressaoContrato().getDocumentoAssinado().getArquivo().getPastaBaseArquivo() + File.separator + logImpressaoContrato.getImpressaoContrato().getDocumentoAssinado().getArquivo().getNome(), logImpressaoContrato.getImpressaoContrato().getDocumentoAssinado().getArquivo().getNome());
				return logImpressaoContrato.getImpressaoContrato().getDocumentoAssinado().getArquivo().getNome();							
			}
		}
		return nomeArquivo;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public String carregarDadosParaImpressaoContratoVisaoAluno(TextoPadraoVO texto, MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoFinanceiroVO configFinanceira, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioLogado) throws Exception {
		if(texto.getTipo().equals("MA")){			
			matriculaPeriodoVO.setContratoMatricula(texto);
		}else if(texto.getTipo().equals("EX")){
				matriculaPeriodoVO.setContratoExtensao(texto);
		}else{
			matriculaPeriodoVO.setContratoFiador(texto);
		}
		return imprimirContratoRenovarMatricula(texto.getTipo(), matricula, matriculaPeriodoVO, configFinanceira, config, usuarioLogado);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String imprimirContrato(String tipoContrato, ImpressaoContratoVO impressaoContratoVO, ConfiguracaoGeralSistemaVO config, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioLogado) throws Exception {

		realizarGravacaoContrato(impressaoContratoVO);
		Matricula.montarDadosUnidadeEnsino(impressaoContratoVO.getMatriculaVO(), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().setOperadorResponsavel(Uteis.montarDadosVO(impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().getOperadorResponsavel().getCodigo(), FuncionarioVO.class, p->getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(p, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado)));
		impressaoContratoVO.getMatriculaPeriodoVO().getResponsavelRenovacaoMatricula().setNome(impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().getOperadorResponsavel().getPessoa().getNome());
		PlanoFinanceiroCursoVO planoFin = impressaoContratoVO.getMatriculaPeriodoVO().getPlanoFinanceiroCurso();
		if(!impressaoContratoVO.getMatriculaVO().getMatriculaOnlineExterna()) {
			impressaoContratoVO.getMatriculaVO().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
			getFacadeFactory().getMatriculaFacade().carregarDados(impressaoContratoVO.getMatriculaVO(), usuarioLogado);
			impressaoContratoVO.getMatriculaVO().setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaVO().getTurno().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
			impressaoContratoVO.getMatriculaPeriodoVO().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
			getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(impressaoContratoVO.getMatriculaPeriodoVO(), NivelMontarDados.TODOS, configuracaoFinanceiroVO, usuarioLogado);
			getFacadeFactory().getMatriculaPeriodoFacade().carregarDadosConsultorUsuarioResp(impressaoContratoVO.getMatriculaVO(), impressaoContratoVO.getMatriculaPeriodoVO(), usuarioLogado);
			impressaoContratoVO.getMatriculaPeriodoVO().setProcessoMatriculaCalendarioVO(getFacadeFactory().getProcessoMatriculaCalendarioFacade().consultarPorMatriculaPeriodoUnidadeEnsinoCurso(impressaoContratoVO.getMatriculaPeriodoVO().getCodigo(), null, null, null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
			impressaoContratoVO.getMatriculaPeriodoVO().setGradeCurricular(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaPeriodoVO().getGradeCurricular().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
			impressaoContratoVO.getMatriculaVO().setAluno(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaVO().getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
			impressaoContratoVO.getMatriculaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuarioLogado));
			impressaoContratoVO.getMatriculaVO().setPlanoFinanceiroAluno(getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaPeriodo(impressaoContratoVO.getMatriculaPeriodoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
			getFacadeFactory().getTurmaFacade().carregarDados(impressaoContratoVO.getMatriculaPeriodoVO().getTurma(), usuarioLogado);
			if(Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaPeriodoVO().getPlanoFinanceiroCurso())){
				planoFin = getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getCodigo(), "", Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
			}
			// devolvendo o planofinanceiro curso inicializado.
			impressaoContratoVO.getMatriculaPeriodoVO().setPlanoFinanceiroCurso(planoFin);
		}
		montarDadosInformadosCCparaGeracaoBoleto(impressaoContratoVO, usuarioLogado);
 		DadosComerciaisVO dc = getFacadeFactory().getDadosComerciaisFacade().consultarEmpregoAtualPorCodigoPessoa(impressaoContratoVO.getMatriculaVO().getAluno().getCodigo(), usuarioLogado);
		impressaoContratoVO.setConfiguracaoGEDVO(getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().getCodigo(), false, usuarioLogado));
		if (!Uteis.isAtributoPreenchido(impressaoContratoVO.getConfiguracaoGeralSistemaVO().getCodigo())) {
			impressaoContratoVO.setConfiguracaoGeralSistemaVO(config);
		}
		String contratoPronto = Constantes.EMPTY;
		TextoPadraoVO texto = new TextoPadraoVO();
		if(isTipoContrato_Normal_Matricula_Extencao_Fiador(tipoContrato)) {
			texto = getTextoPadraoVOTipoContratoNormal_Matricula_Extencao_Fiador(tipoContrato, impressaoContratoVO, usuarioLogado, texto);
			if (!Uteis.isAtributoPreenchido(texto.getCodigo())) {
				Uteis.checkState(Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getInscricao().getCodigo()),"Não foi encontrado nenhum texto padrão de contrato definido para matrícula, no cadastro do curso!");
				throw new Exception("Não foi encontrado nenhum texto padrão de contrato definido para matrícula, condição de pagamento e/ou plano financeiro curso!");
			}
			contratoPronto = getContratoProntoPorTipoContrato_Normal_Matricula_Extencao_Fiador(impressaoContratoVO, config, usuarioLogado, texto,   impressaoContratoVO.getMatriculaPeriodoVO().getMatriculaPeriodoVencimentoVOs(),  dc);
		}else if (tipoContrato.equals("IR")) {
			Uteis.checkState(!Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getTextoPadraoContratoInclusaoReposicao().getCodigo()),"Não existe um Contrato Padrão definido no Plano Financeiro do Curso!");
			texto = impressaoContratoVO.getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getTextoPadraoContratoInclusaoReposicao();
			contratoPronto = getContratoProntoPorTipoContrato_InclusaoReposicao(impressaoContratoVO, usuarioLogado, texto,  impressaoContratoVO.getMatriculaPeriodoVO().getMatriculaPeriodoVencimentoVOs(), dc);
		} else if (tipoContrato.equals("MO")) {
			Uteis.checkState(!Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getTextoPadraoContratoModular().getCodigo()),"Não existe um Contrato Padrão definido no Plano Financeiro do Curso!");
			texto = impressaoContratoVO.getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getTextoPadraoContratoModular();
			contratoPronto = texto.substituirTagsTextoPadraoContratoFiador(impressaoContratoVO.getMatriculaVO(),  impressaoContratoVO.getMatriculaPeriodoVO().getMatriculaPeriodoVencimentoVOs(), impressaoContratoVO.getMatriculaPeriodoVO(), impressaoContratoVO.getMatriculaVO().getPlanoFinanceiroAluno(), new ArrayList<PlanoDescontoVO>(), dc, usuarioLogado);
		}else if (tipoContrato.equals("AD")) {
			Uteis.checkState(!Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getTextoPadraoContratoAditivo().getCodigo()),"Não existe um Contrato Padrão definido no Plano Financeiro do Curso!");
			texto = impressaoContratoVO.getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getTextoPadraoContratoAditivo();
			contratoPronto = texto.substituirTagsTextoPadraoContratoFiador(impressaoContratoVO.getMatriculaVO(),  impressaoContratoVO.getMatriculaPeriodoVO().getMatriculaPeriodoVencimentoVOs(), impressaoContratoVO.getMatriculaPeriodoVO(), impressaoContratoVO.getMatriculaVO().getPlanoFinanceiroAluno(), new ArrayList<PlanoDescontoVO>(), dc, usuarioLogado);
		}else if (tipoContrato.equals("RE")) {
			impressaoContratoVO.getMatriculaPeriodoVO().setProcessoMatriculaVO(getFacadeFactory().getProcessoMatriculaFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaPeriodoVO().getProcessoMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
			impressaoContratoVO.getMatriculaPeriodoVO().getProcessoMatriculaVO().setTextoPadraoContratoRenovacaoOnline(getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaPeriodoVO().getProcessoMatriculaVO().getTextoPadraoContratoRenovacaoOnline().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
			Uteis.checkState(!Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaPeriodoVO().getProcessoMatriculaVO().getTextoPadraoContratoRenovacaoOnline()),"Não existe um Contrato Padrão definido de Renovação Online para a matrícula: " + impressaoContratoVO.getMatriculaVO().getMatricula() + ".");
			texto = impressaoContratoVO.getMatriculaPeriodoVO().getProcessoMatriculaVO().getTextoPadraoContratoRenovacaoOnline();
			contratoPronto = texto.substituirTagsTextoPadraoContratoMatricula(impressaoContratoVO, impressaoContratoVO.getMatriculaVO(),  impressaoContratoVO.getMatriculaPeriodoVO().getMatriculaPeriodoVencimentoVOs(), impressaoContratoVO.getMatriculaPeriodoVO(), impressaoContratoVO.getMatriculaVO().getPlanoFinanceiroAluno(), new ArrayList<PlanoDescontoVO>(), dc, usuarioLogado);
		}
		contratoPronto = getFacadeFactory().getTextoPadraoDeclaracaoFacade().removerBordaDaPagina(contratoPronto);
		contratoPronto = getFacadeFactory().getTextoPadraoDeclaracaoFacade().adicionarStyleFormatoPaginaTextoPadrao(contratoPronto, texto.getOrientacaoDaPagina());
		return executarValidacaoTipoImpressaoContrato(texto, contratoPronto, impressaoContratoVO, config, impressaoContratoVO.getGerarNovoArquivoAssinado(), usuarioLogado);
	}

	private static void realizarGravacaoContrato(ImpressaoContratoVO impressaoContratoVO) throws Exception {
		if(impressaoContratoVO.getMatriculaPeriodoVO().getGravarContratoMatricula()){
			getFacadeFactory().getMatriculaPeriodoFacade().gravarContratoMatricula(impressaoContratoVO.getMatriculaPeriodoVO().getCodigo(), impressaoContratoVO.getMatriculaPeriodoVO().getContratoMatricula().getCodigo());
		}
		if(impressaoContratoVO.getMatriculaPeriodoVO().getGravarContratoExtensao()){
			getFacadeFactory().getMatriculaPeriodoFacade().gravarContratoExtensao(impressaoContratoVO.getMatriculaPeriodoVO().getCodigo(), impressaoContratoVO.getMatriculaPeriodoVO().getContratoExtensao().getCodigo());
		}
		if(impressaoContratoVO.getMatriculaPeriodoVO().getGravarContratoFiador()){
			getFacadeFactory().getMatriculaPeriodoFacade().gravarContratoFiador(impressaoContratoVO.getMatriculaPeriodoVO().getCodigo(), impressaoContratoVO.getMatriculaPeriodoVO().getContratoFiador().getCodigo());
		}
	}

	private static String getContratoProntoPorTipoContrato_InclusaoReposicao(ImpressaoContratoVO impressaoContratoVO, UsuarioVO usuarioLogado, TextoPadraoVO texto,   List<MatriculaPeriodoVencimentoVO> listaContasReceber,  DadosComerciaisVO dc) throws Exception {
		if (impressaoContratoVO.getMatriculaVO().getFormacaoAcademica().getCodigo().intValue() != 0) {
			impressaoContratoVO.getMatriculaVO().getAluno().getFormacaoAcademicaVOs().clear();
			impressaoContratoVO.getMatriculaVO().getAluno().getFormacaoAcademicaVOs().add(getFacadeFactory().getFormacaoAcademicaFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaVO().getFormacaoAcademica().getCodigo(), usuarioLogado));
		}
		String ano = Constantes.EMPTY;
		String semestre = Constantes.EMPTY;
		if (!impressaoContratoVO.getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao()) {
			ano = impressaoContratoVO.getMatriculaPeriodoVO().getAno();
			semestre = impressaoContratoVO.getMatriculaPeriodoVO().getSemestre();
		}
		impressaoContratoVO.getMatriculaPeriodoVO().setDataInicioAula(getFacadeFactory().getHorarioTurmaDiaFacade().consultarPrimeiraDataAulaPorTurmaAgrupada(impressaoContratoVO.getMatriculaPeriodoVO().getTurma().getCodigo(), ano, semestre));
		impressaoContratoVO.getMatriculaPeriodoVO().setDataFinalAula(getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorTurmaAgrupada(impressaoContratoVO.getMatriculaPeriodoVO().getTurma().getCodigo(), ano, semestre));
        return texto.substituirTagsTextoPadraoContratoMatricula(impressaoContratoVO, impressaoContratoVO.getMatriculaVO(), listaContasReceber, impressaoContratoVO.getMatriculaPeriodoVO(), impressaoContratoVO.getMatriculaVO().getPlanoFinanceiroAluno(), new ArrayList<PlanoDescontoVO>(), dc, usuarioLogado);
	}

	private static String getContratoProntoPorTipoContrato_Normal_Matricula_Extencao_Fiador(ImpressaoContratoVO impressaoContratoVO, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioLogado, TextoPadraoVO texto,   List<MatriculaPeriodoVencimentoVO> listaContasReceber,   DadosComerciaisVO dc) throws Exception {

		if (Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getFormacaoAcademica().getCodigo())) {
			try {
				FormacaoAcademicaVO formacaoAcademicaMatricula = getFacadeFactory().getFormacaoAcademicaFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaVO().getFormacaoAcademica().getCodigo(), usuarioLogado);
				Iterator<FormacaoAcademicaVO> i = impressaoContratoVO.getMatriculaVO().getAluno().getFormacaoAcademicaVOs().iterator();
				while (i.hasNext()) {
					FormacaoAcademicaVO formacaoAcademicaVO = (FormacaoAcademicaVO) i.next();
					if (formacaoAcademicaVO.getEscolaridade().equals(formacaoAcademicaMatricula.getEscolaridade()) && !formacaoAcademicaVO.getCodigo().equals(formacaoAcademicaMatricula.getCodigo())){
						i.remove();
					}
				}
			} catch (Exception e) {
				impressaoContratoVO.getMatriculaVO().getAluno().getFormacaoAcademicaVOs().clear();
			}
		}
		String ano = Constantes.EMPTY;
		String semestre =Constantes.EMPTY;
		if (!impressaoContratoVO.getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao()) {
			ano = impressaoContratoVO.getMatriculaPeriodoVO().getAno();
			semestre = impressaoContratoVO.getMatriculaPeriodoVO().getSemestre();
		}
		impressaoContratoVO.getMatriculaPeriodoVO().setDataInicioAula(getFacadeFactory().getHorarioTurmaDiaFacade().consultarPrimeiraDataAulaPorTurmaAgrupada(impressaoContratoVO.getMatriculaPeriodoVO().getTurma().getCodigo(), ano, semestre));
		impressaoContratoVO.getMatriculaPeriodoVO().setDataFinalAula(getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorTurmaAgrupada(impressaoContratoVO.getMatriculaPeriodoVO().getTurma().getCodigo(), ano, semestre));
		if (texto.getTexto().contains("EmailInstitucional_Aluno")) {
			impressaoContratoVO.setPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(impressaoContratoVO.getMatriculaVO().getAluno().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
		}
		if (texto.getTexto().contains("ListaDisciplinasHistoricoDiploma_Disciplina") || texto.getTexto().contains("ListaDisciplinasHistoricoCertificado_Disciplina")
				|| texto.getTexto().contains("ListaDisciplinasCursadasOuMinistradas_Disciplina") || texto.getTexto().contains("ListaDisciplinasHistoricoDiploma_Disciplina")
				|| texto.getTexto().contains("ListaDisciplinasPeriodoLetivoAtual_Disciplina") || texto.getTexto().contains("ListaDisciplinasPeriodoLetivoAtualPre_Requisito_Disciplina_Retrado")
				|| texto.getTexto().contains("ListaDisciplinasVersoDiploma_Disciplina")) {
			impressaoContratoVO.setCargaHorariaRealizadaAtividadeComplementar(getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarCargaHorariaRealizada(impressaoContratoVO.getMatriculaVO().getMatricula(), impressaoContratoVO.getMatriculaPeriodoVO().getGradeCurricular().getCodigo(), false));
			impressaoContratoVO.setListaDisciplinasCursadasOuMinistradas(getFacadeFactory().getHistoricoFacade().consultarDisciplinaCursadasAlunoPorMatricula(impressaoContratoVO.getMatriculaVO().getMatricula(), impressaoContratoVO.getMatriculaVO().getGradeCurricularAtual().getCodigo(), null, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado, 0, false));
			impressaoContratoVO.setListaDisciplinasPeriodoLetivoAtual(getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoLetivoAtualImpressaoDeclaracao(impressaoContratoVO.getMatriculaPeriodoVO().getCodigo(), true, null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado, impressaoContratoVO.getMatriculaVO().getGradeCurricularAtual().getCodigo()));
			getFacadeFactory().getHistoricoFacade().carregarDadosProfessorTitularTitulacao((List<HistoricoVO>) impressaoContratoVO.getListaDisciplinasCursadasOuMinistradas());
			getFacadeFactory().getHistoricoFacade().carregarDadosProfessorTitularTitulacao((List<HistoricoVO>) impressaoContratoVO.getListaDisciplinasPeriodoLetivoAtual());
		}
		if (texto.getTexto().contains("Fiador_Aluno")) {
			getFacadeFactory().getPessoaFacade().carregarDados(impressaoContratoVO.getMatriculaVO().getAluno(), usuarioLogado);
		}

		/* Monta Plano Disciplina */
		if(texto.getTexto().contains("ListaPlanoEnsinoDisciplina_Aluno")) {
			GradeCurricularVO gradeCurricular = impressaoContratoVO.getMatriculaVO().getGradeCurricularAtual();
			List<PeriodoLetivoVO> listaPeriodoLetivo = getFacadeFactory().getPeriodoLetivoFacade().consultarPorGradeCurricular(gradeCurricular.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);

			for(PeriodoLetivoVO periodoLetivoVO : listaPeriodoLetivo ) {
				for(GradeDisciplinaVO gradeDisciplinaVO : periodoLetivoVO.getGradeDisciplinaVOs()) {
					PlanoEnsinoVO pl = getFacadeFactory().getPlanoEnsinoFacade()
							.consultarPorDisciplinaMatriculaAluno(gradeDisciplinaVO.getDisciplina().getCodigo(), impressaoContratoVO.getMatriculaVO().getMatricula() , false, new HistoricoVO(), usuarioLogado);
					if(Uteis.isAtributoPreenchido(pl)) {
						pl.setTotalCargaHoraria(new BigDecimal(gradeDisciplinaVO.getCargaHoraria()));
						impressaoContratoVO.getListaPlanoEnsino().add(pl);
					}
				}
			}
		}

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
		}
		/* Finaliza ListaDisciplinasHistoricoPeriodoLetivoSituacao_Disciplina */

		for (ConvenioVO convenioVO : impressaoContratoVO.getMatriculaVO().getPlanoFinanceiroAluno().getPlanoFinanceiroConvenioVOs()) {
			convenioVO.setParceiro(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(convenioVO.getParceiro().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuarioLogado));
			break;
		}
		if (texto.getTexto().contains("UrlFoto_Aluno")) {
			getFacadeFactory().getPessoaFacade().carregarUrlFotoAluno(config, impressaoContratoVO.getMatriculaVO().getAluno());
		}
		getFacadeFactory().getImpressaoDeclaracaoFacade().carregarTotalSemestresCurso(impressaoContratoVO.getMatriculaPeriodoVO().getGradeCurricular(), texto.getTexto(), usuarioLogado);
		if (Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getMatricula())
				&& (texto.getTexto().contains("Nr_Periodo_Letivo_Ingresso_Matricula")
						|| texto.getTexto().contains("Nr_Extenso_Periodo_Letivo_Ingresso_Matricula")
						|| texto.getTexto().contains("Descricao_Periodo_Letivo_Ingresso_Matricula")
						|| texto.getTexto().contains("Nome_Certificacao_Periodo_Letivo_Ingresso_Matricula"))) {
			impressaoContratoVO.setPeriodoLetivoIngresso(getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivoIngressoPorMatricula(impressaoContratoVO.getMatriculaVO().getMatricula(), usuarioLogado));
		}
		if (texto.getTexto().contains("NomeDocenteResponsavelAssinaturaTermoEstagio_Curso") || texto.getTexto().contains("CpfDocenteResponsavelAssinaturaTermoEstagio_Curso") || texto.getTexto().contains("EmailDocenteResponsavelAssinaturaTermoEstagio_Curso")) {
			impressaoContratoVO.getMatriculaVO().getCurso().setFuncionarioResponsavelAssinaturaTermoEstagioVO(getFacadeFactory().getFuncionarioFacade().consultaFuncionarioResponsavelAssinaturaTermoEstagioRapidaPorCurso(impressaoContratoVO.getMatriculaVO().getCurso().getCodigo(), usuarioLogado));
		}
		String  contratoPronto = texto.substituirTagsTextoPadraoContratoMatricula(impressaoContratoVO, impressaoContratoVO.getMatriculaVO(),  listaContasReceber, impressaoContratoVO.getMatriculaPeriodoVO(), impressaoContratoVO.getMatriculaVO().getPlanoFinanceiroAluno(), new ArrayList<PlanoDescontoVO>(), dc, usuarioLogado);
		TextoPadraoDeclaracaoVO textoDec = new TextoPadraoDeclaracaoVO();
		textoDec.setTexto(contratoPronto);
		textoDec.substituirTag(impressaoContratoVO, usuarioLogado);
		texto.getParametrosRel().putAll(textoDec.getParametrosRel());
		texto.setTexto(textoDec.getTexto());
		return contratoPronto;
	}

	private static boolean isTipoContrato_Normal_Matricula_Extencao_Fiador(String tipoContrato) {
		return tipoContrato.equals("NO") || tipoContrato.equals("MA") || tipoContrato.equals("EX") || tipoContrato.equals("FI");
	}

	private static TextoPadraoVO getTextoPadraoVOTipoContratoNormal_Matricula_Extencao_Fiador(String tipoContrato, ImpressaoContratoVO impressaoContratoVO, UsuarioVO usuarioLogado, TextoPadraoVO texto) throws Exception {
		if (tipoContrato.equals("MA")) {
			MatriculaPeriodo.montarDadosContratoMatricula(impressaoContratoVO.getMatriculaPeriodoVO(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
		}else if(tipoContrato.equals("EX") ) {
			MatriculaPeriodo.montarDadosContratoExtensao(impressaoContratoVO.getMatriculaPeriodoVO(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
		}else if(tipoContrato.equals("FI") ) {
			MatriculaPeriodo.montarDadosContratoFiador(impressaoContratoVO.getMatriculaPeriodoVO(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
		}
		if (tipoContrato.equals("MA") && Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaPeriodoVO().getContratoMatricula().getCodigo())) {
			texto = impressaoContratoVO.getMatriculaPeriodoVO().getContratoMatricula();
		}else if (tipoContrato.equals("EX") &&  Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaPeriodoVO().getContratoExtensao())) {
			texto = impressaoContratoVO.getMatriculaPeriodoVO().getContratoExtensao();
		}else if (tipoContrato.equals("FI") &&  Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaPeriodoVO().getContratoFiador())) {
			texto = impressaoContratoVO.getMatriculaPeriodoVO().getContratoFiador();
		} else {
			getFacadeFactory().getMatriculaPeriodoFacade().realizarDefinicaoContratoPadraoSerUsadoMatriculaPeriodo(TipoContratoMatriculaEnum.getEnumPorValor(tipoContrato), impressaoContratoVO.getMatriculaPeriodoVO(), true, true, usuarioLogado);
			if (tipoContrato.equals("MA")){
				MatriculaPeriodo.montarDadosContratoMatricula(impressaoContratoVO.getMatriculaPeriodoVO(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
			}else if(tipoContrato.equals("EX") ) {
				MatriculaPeriodo.montarDadosContratoExtensao(impressaoContratoVO.getMatriculaPeriodoVO(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
			}else if(tipoContrato.equals("FI") ) {
				MatriculaPeriodo.montarDadosContratoFiador(impressaoContratoVO.getMatriculaPeriodoVO(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
			}
			if (tipoContrato.equals("MA") && Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaPeriodoVO().getContratoMatricula().getCodigo())) {
				texto = impressaoContratoVO.getMatriculaPeriodoVO().getContratoMatricula();
			}else if (tipoContrato.equals("EX") &&  Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaPeriodoVO().getContratoExtensao())) {
				texto = impressaoContratoVO.getMatriculaPeriodoVO().getContratoExtensao();
			}else if (tipoContrato.equals("FI") &&  Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaPeriodoVO().getContratoFiador())) {
				texto = impressaoContratoVO.getMatriculaPeriodoVO().getContratoFiador();
			}
		}
		return texto;
	}

	private static void montarDadosInformadosCCparaGeracaoBoleto(ImpressaoContratoVO impressaoContratoVO, UsuarioVO usuarioLogado) throws Exception {
		if (!impressaoContratoVO.getMatriculaPeriodoVO().getTurma().getUtilizarDadosMatrizBoleto() &&
				Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaPeriodoVO().getTurma().getContaCorrente().getCodigo())) {
			ContaCorrenteVO cc = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaPeriodoVO().getTurma().getContaCorrente().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
			if (cc.getUtilizaDadosInformadosCCparaGeracaoBoleto()) {
				impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().setCNPJ(cc.getCNPJ());
				impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().setNome(cc.getNome());
				impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().setRazaoSocial(cc.getRazaoSocial());
				impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().setMantenedora(cc.getMantenedora());
				impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().setInscEstadual(cc.getInscEstadual());
				impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().setCEP(cc.getCEP());
				impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().setEndereco(cc.getEndereco());
				impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().setSetor(cc.getSetor());
				impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().setComplemento(cc.getComplemento());
				impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().setCidade(cc.getCidade());
				impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().setTelComercial1(cc.getTelComercial1());
				impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().setTelComercial2(cc.getTelComercial2());
				impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().setTelComercial3(cc.getTelComercial3());
				impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().setFax(cc.getFax());
				impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().setEmail(cc.getEmail());
				impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().setSite(cc.getSite());
			}
		}
	}

	public String imprimirContratoInclusaoReposicao(ImpressaoContratoVO impressaoContratoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, TextoPadraoVO textoPadraoContratoInclusao, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO config) throws Exception {
		Boolean imprimirContrato = false;
		List listaOrdemDesconto = new ArrayList(0);
		MatriculaVO matri = impressaoContratoVO.getMatriculaVO();
		getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(impressaoContratoVO.getMatriculaPeriodoVO(), NivelMontarDados.BASICO, configuracaoFinanceiroVO, usuarioLogado);
		impressaoContratoVO.getMatriculaPeriodoVO().setProcessoMatriculaCalendarioVO(getFacadeFactory().getProcessoMatriculaCalendarioFacade().consultarPorMatriculaPeriodoUnidadeEnsinoCurso(impressaoContratoVO.getMatriculaPeriodoVO().getCodigo(), null, null, null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
		Matricula.montarDadosUnidadeEnsino(matri, Uteis.NIVELMONTARDADOS_DADOSBASICOS);

		impressaoContratoVO.getMatriculaPeriodoVO().setCondicaoPagamentoPlanoFinanceiroCurso(getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
		List listaContasReceber = impressaoContratoVO.getMatriculaPeriodoVO().getMatriculaPeriodoVencimentoVOs();
		TextoPadraoVO texto = new TextoPadraoVO();
		matri.setAluno(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(matri.getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
		matri.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(matri.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
		matri.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(matri.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuarioLogado));

		matri.setPlanoFinanceiroAluno(getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaPeriodo(impressaoContratoVO.getMatriculaPeriodoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
		listaOrdemDesconto = (matri.getPlanoFinanceiroAluno().obterOrdemAplicacaoDescontosPadraoAtual());
		getFacadeFactory().getMatriculaPeriodoFacade().realizarCalculoValorMatriculaEMensalidade(matri, impressaoContratoVO.getMatriculaPeriodoVO(), usuarioLogado);
		getFacadeFactory().getMatriculaFacade().calcularTotalDesconto(matri, impressaoContratoVO.getMatriculaPeriodoVO(), listaOrdemDesconto, usuarioLogado, configuracaoFinanceiroVO);

		PlanoFinanceiroAlunoVO plano = getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaPeriodo(impressaoContratoVO.getMatriculaPeriodoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
		plano.setCondicaoPagamentoPlanoFinanceiroCursoVO(impressaoContratoVO.getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso());
		if (impressaoContratoVO.getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getNaoControlarMatricula()) {
			impressaoContratoVO.getMatriculaPeriodoVO().setQtdeParcelaContrato(getFacadeFactory().getMatriculaPeriodoFacade().consultarQuantidadeParcelaContratoMatriculaNaoControlada(impressaoContratoVO.getMatriculaPeriodoVO().getCodigo(), impressaoContratoVO.getMatriculaPeriodoVO().getMatricula(), false, usuarioLogado));
		}
		List<PlanoDescontoVO> listaPlanoDescontoVO = getFacadeFactory().getPlanoDescontoFacade().consultarPorPlanoFinanceiroAluno(plano.getCodigo(), false, usuarioLogado);
		DadosComerciaisVO dc = getFacadeFactory().getDadosComerciaisFacade().consultarEmpregoAtualPorCodigoPessoa(matri.getAluno().getCodigo(), usuarioLogado);

		String contratoPronto = "";
		String nomeArquivoOrigem = "";

		if (impressaoContratoVO.getMatriculaPeriodoVO().getReposicao()) {
			if (impressaoContratoVO.getMatriculaPeriodoVO().getPlanoFinanceiroReposicaoVO().getTextoPadraoContratoVO().getCodigo().intValue() == 0) {
				imprimirContrato = false;
				throw new Exception("Não existe um Contrato Padrão definido no Plano Financeiro do Curso!");
			} else {
				imprimirContrato = true;
			}
			texto = getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaPeriodoVO().getPlanoFinanceiroReposicaoVO().getTextoPadraoContratoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
			contratoPronto = texto.substituirTagsTextoPadraoContratoInclusaoReposicao(matri, listaContasReceber, impressaoContratoVO.getMatriculaPeriodoVO(), plano, listaPlanoDescontoVO, dc, impressaoContratoVO.getMatriculaPeriodoVO().getPlanoFinanceiroReposicaoVO().getQtdeParcela(), impressaoContratoVO.getMatriculaPeriodoVO().getPlanoFinanceiroReposicaoVO().getValor() - impressaoContratoVO.getMatriculaPeriodoVO().getDescontoReposicao(), usuarioLogado);
		} else {
			imprimirContrato = true;
			texto = getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(textoPadraoContratoInclusao.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
			contratoPronto = texto.substituirTagsTextoPadraoContratoInclusaoReposicao(matri, listaContasReceber, impressaoContratoVO.getMatriculaPeriodoVO(), plano, listaPlanoDescontoVO, dc, impressaoContratoVO.getMatriculaPeriodoVO().getNumParcelasInclusaoForaPrazo(), impressaoContratoVO.getMatriculaPeriodoVO().getValorTotalParcelaInclusaoForaPrazo() - impressaoContratoVO.getMatriculaPeriodoVO().getDescontoReposicao(), usuarioLogado);
		}
		
		if (texto.getTipoDesigneTextoEnum().isHtml()) {
			impressaoContratoVO.setImpressaoPdf(false);
			if (context().getExternalContext().getRequest() != null) {
				HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
				request.getSession().setAttribute("textoPadrao", texto);	
				request.getSession().setAttribute("textoRelatorio", contratoPronto);
			}
			nomeArquivoOrigem = Uteis.getData(new Date(), "dd_MM_yy_HH_mm_ss") + "_" + usuarioLogado.getCodigo() + ".html";
		} else {
			impressaoContratoVO.setImpressaoPdf(true);
			impressaoContratoVO.setUsuarioLogado(usuarioLogado);
			impressaoContratoVO.setTextoPadraoDeclaracao(texto.getCodigo());
			nomeArquivoOrigem = getFacadeFactory().getImpressaoDeclaracaoFacade().executarValidacaoImpressaoEmPdf(impressaoContratoVO, texto, contratoPronto, impressaoContratoVO.getGerarNovoArquivoAssinado(), config, usuarioLogado);
			getFacadeFactory().getImpressaoDeclaracaoFacade().gravarImpressaoContrato(impressaoContratoVO);
		}
		return nomeArquivoOrigem;
	}

	@Override
	public String montarDadosContratoTextoPadrao(MatriculaVO matriculaVO, ImpressaoContratoVO impressaoContratoVO, TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		try {
			String textoStr = "";
			
			impressaoContratoVO.setMatriculaVO(matriculaVO);
			impressaoContratoVO.getMatriculaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(matriculaVO.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, false, usuario));
			impressaoContratoVO.getMatriculaVO().setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(matriculaVO.getTurno().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
			impressaoContratoVO.getMatriculaVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(matriculaVO.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
			impressaoContratoVO.getMatriculaVO().setAluno(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(matriculaVO.getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			impressaoContratoVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
			impressaoContratoVO.getMatriculaVO().setGradeCurricularAtual(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaVO().getGradeCurricularAtual().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			impressaoContratoVO.setTurmaVO(impressaoContratoVO.getMatriculaPeriodoVO().getTurma());
			impressaoContratoVO.setTextoPadraoDeclaracao(textoPadraoDeclaracaoVO.getCodigo());
			impressaoContratoVO.setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoFixo());
			if (textoPadraoDeclaracaoVO.getTexto().contains("ListaDisciplinasHistoricoDiploma_Disciplina") || textoPadraoDeclaracaoVO.getTexto().contains("ListaDisciplinasHistoricoCertificado_Disciplina") || textoPadraoDeclaracaoVO.getTexto().contains("ListaDisciplinasCursadasOuMinistradas_Disciplina") || textoPadraoDeclaracaoVO.getTexto().contains("ListaDisciplinasHistoricoDiploma_Disciplina") || textoPadraoDeclaracaoVO.getTexto().contains("ListaDisciplinasPeriodoLetivoAtual_Disciplina") || textoPadraoDeclaracaoVO.getTexto().contains("ListaDisciplinasPeriodoLetivoAtualPre_Requisito_Disciplina_Retrado") || textoPadraoDeclaracaoVO.getTexto().contains("ListaDisciplinasHistoricoPeriodo_Disciplina") ) {
				impressaoContratoVO.setCargaHorariaRealizadaAtividadeComplementar(getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarCargaHorariaRealizada(impressaoContratoVO.getMatriculaVO().getMatricula(), impressaoContratoVO.getMatriculaPeriodoVO().getGradeCurricular().getCodigo(), false));
				impressaoContratoVO.setListaDisciplinasCursadasOuMinistradas(getFacadeFactory().getHistoricoFacade().consultarDisciplinaCursadasAlunoPorMatricula(impressaoContratoVO.getMatriculaVO().getMatricula(), impressaoContratoVO.getMatriculaVO().getGradeCurricularAtual().getCodigo(), null, false, Uteis.NIVELMONTARDADOS_TODOS, usuario, 0, false));
				impressaoContratoVO.setListaDisciplinasPeriodoLetivoAtual(getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoLetivoAtualImpressaoDeclaracao(impressaoContratoVO.getMatriculaPeriodoVO().getCodigo(), true, null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario, impressaoContratoVO.getMatriculaVO().getGradeCurricularAtual().getCodigo()));
				getFacadeFactory().getHistoricoFacade().carregarDadosProfessorTitularTitulacao((List<HistoricoVO>)impressaoContratoVO.getListaDisciplinasCursadasOuMinistradas());
				getFacadeFactory().getHistoricoFacade().carregarDadosProfessorTitularTitulacao((List<HistoricoVO>)impressaoContratoVO.getListaDisciplinasPeriodoLetivoAtual());
			}
			if (textoPadraoDeclaracaoVO.getTexto().contains("CoeficienteRendimentoGeral_Aluno")) {
				impressaoContratoVO.setCoeficienteRendimentoGeralAluno(getFacadeFactory().getHistoricoFacade().calcularCoeficienteRendimentoGeralAluno(matriculaVO.getMatricula()));
			}
			if (textoPadraoDeclaracaoVO.getTexto().contains("CoeficienteRendimentoPeriodoLetivo_Aluno")) {
				impressaoContratoVO.setCoeficienteRendimentoPeriodoLetivoAluno(getFacadeFactory().getHistoricoFacade().calcularCoeficienteRendimentoPeriodoLetivoAluno(matriculaVO.getMatricula(), impressaoContratoVO.getMatriculaPeriodoVO().getAno(), impressaoContratoVO.getMatriculaPeriodoVO().getSemestre()));
			}
			if (textoPadraoDeclaracaoVO.getTexto().contains("DataPublicacao_Curso") || textoPadraoDeclaracaoVO.getTexto().contains("BaseLegal_Curso")) {
				impressaoContratoVO.setAutorizacaoCurso(getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoDataVigenteMatricula(impressaoContratoVO.getMatriculaVO().getCurso().getCodigo(), impressaoContratoVO.getMatriculaVO().getData(), Uteis.NIVELMONTARDADOS_COMBOBOX));
			}
			if (textoPadraoDeclaracaoVO.getTexto().contains("ListaEnade_Aluno")) {
				if (impressaoContratoVO.getMatriculaVO().getCurso().getNivelEducacional().equals("SU")) {
					impressaoContratoVO.getMatriculaVO().setMatriculaEnadeVOs(getFacadeFactory().getMatriculaEnadeFacade().consultarMatriculaEnadePorMatricula(impressaoContratoVO.getMatriculaVO().getMatricula(), usuario, false));
				}
			}
			if (textoPadraoDeclaracaoVO.getTexto().contains("PrevisaoConclusao_Curso")) {
				impressaoContratoVO.setQuantidadePeriodoLetivoACursar(getFacadeFactory().getPeriodoLetivoFacade().consultarQuantidadePeriodoLetivoACursar(impressaoContratoVO.getMatriculaVO().getMatricula(), usuario));
				Uteis.checkState(!Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaPeriodoVO().getGradeCurricular().getCodigo()) && !Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getMatricula()) && !Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaPeriodoVO().getPeriodoLetivo().getCodigo()), "É necessário informar o Período Letivo, a Matricula, e a Grade Curricular do aluno para listar as Disciplinas Pendentes.");
				impressaoContratoVO.setQuantidadeDisciplinasNaoCursadas(getFacadeFactory().getDisciplinaFacade().consultarDisciplinasNaoCumpridasDaGrade(impressaoContratoVO.getMatriculaVO().getMatricula(), impressaoContratoVO.getMatriculaPeriodoVO().getGradeCurricular().getCodigo(), impressaoContratoVO.getMatriculaPeriodoVO().getPeriodoLetivo().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario).size());
			}
			if (textoPadraoDeclaracaoVO.getTexto().contains("Duracao_Curso")) {
				impressaoContratoVO.getMatriculaPeriodoVO().setCondicaoPagamentoPlanoFinanceiroCurso(getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			}
			if (textoPadraoDeclaracaoVO.getTexto().contains("CargaHoraria_Curso") || textoPadraoDeclaracaoVO.getTexto().contains("CompetenciaProfissional_Curso")) {
				impressaoContratoVO.getMatriculaPeriodoVO().setGradeCurricular(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaPeriodoVO().getGradeCurricular().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			}
			if (textoPadraoDeclaracaoVO.getTexto().contains("CargaHoraria_Disciplina")) {
				impressaoContratoVO.setGradeDisciplinaVO(getFacadeFactory().getGradeDisciplinaFacade().consultarPorMatriculaDisciplina(impressaoContratoVO.getMatriculaVO().getMatricula(), impressaoContratoVO.getDisciplinaVO().getCodigo(), usuario));
			}
			if (textoPadraoDeclaracaoVO.getTexto().contains("LivroRegistroDiploma_Aluno") || textoPadraoDeclaracaoVO.getTexto().contains("NumeroRegistroDiploma_Aluno") || textoPadraoDeclaracaoVO.getTexto().contains("DataPublicacaoDiploma_Aluno") || textoPadraoDeclaracaoVO.getTexto().contains("FolhaRegistroDiploma_Aluno")) {
				impressaoContratoVO.setControleLivroFolhaReciboVO(getFacadeFactory().getControleLivroFolhaReciboFacade().consultarPorMatriculaMatriculaMaiorVia(impressaoContratoVO.getMatriculaVO().getMatricula(), TipoLivroRegistroDiplomaEnum.DIPLOMA, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			}
			if (textoPadraoDeclaracaoVO.getTexto().contains("LivroRegistroCertificado_Aluno") || textoPadraoDeclaracaoVO.getTexto().contains("NumeroRegistroCertificado_Aluno") || textoPadraoDeclaracaoVO.getTexto().contains("DataPublicacaoCertificado_Aluno") || textoPadraoDeclaracaoVO.getTexto().contains("FolhaRegistroCertificado_Aluno")) {
				impressaoContratoVO.setControleLivroFolhaReciboVO(getFacadeFactory().getControleLivroFolhaReciboFacade().consultarPorMatriculaMatriculaMaiorVia(impressaoContratoVO.getMatriculaVO().getMatricula(), TipoLivroRegistroDiplomaEnum.CERTIFICADO, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			}
			if (textoPadraoDeclaracaoVO.getTexto().contains("ListaDisciplinaCertificadoEnsinoMedio_Disciplina")) {
				impressaoContratoVO.setListaDisciplinaCertificadoEnsinoMedio(getFacadeFactory().getHistoricoFacade().consultaRapidaPorMatricula(impressaoContratoVO.getMatriculaVO().getMatricula(), impressaoContratoVO.getMatriculaVO().getGradeCurricularAtual().getCodigo(), OrdemHistoricoDisciplina.ANO_SEMESTRE.getValor(), false, null, false, NivelMontarDados.BASICO, false, false, usuario));
				getFacadeFactory().getHistoricoFacade().carregarDadosProfessorTitularTitulacao((List<HistoricoVO>)impressaoContratoVO.getListaDisciplinaCertificadoEnsinoMedio());
			}
			if (textoPadraoDeclaracaoVO.getTexto().contains("SituacaoFinalPeriodoLetivo_Matricula")) {
				List<BoletimAcademicoRelVO> boletimAcademicoRelVOs = getFacadeFactory().getBoletimAcademicoRelFacade().criarObjeto(impressaoContratoVO.getMatriculaVO(), "BoletimAcademicoEnsinoMedioRel", true, impressaoContratoVO.getMatriculaPeriodoVO().getAno() + impressaoContratoVO.getMatriculaPeriodoVO().getSemestre(), impressaoContratoVO.getMatriculaPeriodoVO().getTurma(), impressaoContratoVO.getMatriculaVO().getUnidadeEnsino(), null, false, usuario, false, false, null, null, null, null, null, null, false, true, false, false, new FiltroRelatorioAcademicoVO(), null, false, false, new ArrayList<>(), null);
				if (!boletimAcademicoRelVOs.isEmpty()) {
					impressaoContratoVO.setSituacaoFinalPeriodoLetivo(boletimAcademicoRelVOs.get(0).getSituacaoFinal());
				}
			}
			if ((textoPadraoDeclaracaoVO.getTexto().contains("DataExpedicaoDiploma_Matricula") || textoPadraoDeclaracaoVO.getTexto().contains("NumeroProcessoDiploma_Aluno")) && !Uteis.isAtributoPreenchido(impressaoContratoVO.getExpedicaoDiplomaVO())) {
				ExpedicaoDiplomaVO expedicaoDiplomaVO = getFacadeFactory().getExpedicaoDiplomaFacade().consultarPorMatricula(impressaoContratoVO.getMatriculaVO().getMatricula(),false ,3, usuario);
				if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO)) {
					impressaoContratoVO.setExpedicaoDiplomaVO(expedicaoDiplomaVO);
				}
			}
			
			if (Uteis.isAtributoPreenchido(impressaoContratoVO.getExpedicaoDiplomaVO())) {
				impressaoContratoVO.setObservacaoComplementarDiplomaVOs(impressaoContratoVO.getExpedicaoDiplomaVO().getObservacaoComplementarDiplomaVOs());
				impressaoContratoVO.getObservacaoComplementarDiplomaVOs()
					.stream().filter(obs -> obs.getObservacaoComplementar().getReapresentarNomeAluno())
					.forEach(obs -> obs.getObservacaoComplementar().setNomeAlunoRepresentar(Uteis.alterarpreposicaoParaMinusculo(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(impressaoContratoVO.getMatriculaVO().getAluno().getNome().toLowerCase()))));
				if (!impressaoContratoVO.getExpedicaoDiplomaVO().getVia().equals("1")) {
					impressaoContratoVO.setPrimeiraViaExpedicaoDiplomaVO(getFacadeFactory().getExpedicaoDiplomaFacade().consultarPorMatriculaPrimeiraViaMontandoFuncionarioCargos(impressaoContratoVO.getMatriculaVO().getMatricula(), 
							impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
				}
			}
			if ((textoPadraoDeclaracaoVO.getTexto().contains("DataInicioProgAula_Curso") || textoPadraoDeclaracaoVO.getTexto().contains("DataFinalProgAula_Curso")) 
					&& !textoPadraoDeclaracaoVO.getTexto().contains("ListaDisciplinasAprovadasCertificado_Disciplina")
					&& !textoPadraoDeclaracaoVO.getTexto().contains("ListaDisciplinasNotaFrequenciaCargaHorariaProfessorSituacao_Disciplina")) {
				if(Uteis.isAtributoPreenchido(impressaoContratoVO.getDisciplinaVO().getCodigo())){
					HistoricoVO hist = getFacadeFactory().getHistoricoFacade().consultaRapidaPorMatricula_Disciplina_Turma(impressaoContratoVO.getMatriculaVO().getMatricula(), impressaoContratoVO.getDisciplinaVO().getCodigo(), impressaoContratoVO.getTurmaVO(), impressaoContratoVO.getMatriculaPeriodoVO().getAno(), impressaoContratoVO.getMatriculaPeriodoVO().getSemestre(), textoPadraoDeclaracaoVO.getTexto().contains("BimestreAnoConclusao_Disciplina"), false, usuario);
					if(Uteis.isAtributoPreenchido(hist)) {
						getFacadeFactory().getHistoricoFacade().carregarDadosHorarioAulaAluno(hist, true);	
						impressaoContratoVO.getMatriculaPeriodoVO().setDataInicioAula(hist.getDataPrimeiraAula());		
						impressaoContratoVO.getMatriculaPeriodoVO().setDataFinalAula(hist.getData());
					}
				}else if(Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaPeriodoVO().getCodigo())) {
					if(!Uteis.isAtributoPreenchido(impressaoContratoVO.getListaDisciplinasPeriodoLetivoAtual())) {
						impressaoContratoVO.setListaDisciplinasPeriodoLetivoAtual(getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoLetivoAtualImpressaoDeclaracao(impressaoContratoVO.getMatriculaPeriodoVO().getCodigo(), true, null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario, impressaoContratoVO.getMatriculaVO().getGradeCurricularAtual().getCodigo()));
					}
					realizarObtencaoDatasAulaAluno(impressaoContratoVO.getListaDisciplinasPeriodoLetivoAtual(), impressaoContratoVO, usuario, true);		
				}

			}
			if (textoPadraoDeclaracaoVO.getTexto().contains("ListaDisciplinasAprovadasCertificado_Disciplina") || textoPadraoDeclaracaoVO.getTexto().contains("ListaDisciplinasNotaFrequenciaCargaHorariaProfessorSituacao_Disciplina") || textoPadraoDeclaracaoVO.getTexto().contains("CargaHorariaDisciplinasCursadasOuMinistradas_Disciplina")) {
				impressaoContratoVO.setListaDisciplinasCursadasOuMinistradas(getFacadeFactory().getHistoricoFacade().consultarDisciplinaCursadasAlunoPorMatricula(impressaoContratoVO.getMatriculaVO().getMatricula(), impressaoContratoVO.getMatriculaVO().getGradeCurricularAtual().getCodigo(), null, false, Uteis.NIVELMONTARDADOS_TODOS, usuario, 0, false));
				List<HistoricoVO> lista = realizarObtencaoDatasAulaAluno(impressaoContratoVO.getListaDisciplinasCursadasOuMinistradas(), impressaoContratoVO, usuario, false);				
				getFacadeFactory().getHistoricoFacade().carregarDadosProfessorTitularTitulacao(lista);
				impressaoContratoVO.setListaDisciplinasCursadasOuMinistradas(lista);				
				if(impressaoContratoVO.getMatriculaVO().getCurso().getIntegral()) {
					Ordenacao.ordenarLista(impressaoContratoVO.getListaDisciplinasCursadasOuMinistradas(), "dataInicioAula_formatoDate");
				}
			}
			 
			
			if (textoPadraoDeclaracaoVO.getTexto().contains("Fiador_Aluno")) {
				getFacadeFactory().getPessoaFacade().carregarDados(impressaoContratoVO.getMatriculaVO().getAluno(),	usuario);
			}
			
			if (textoPadraoDeclaracaoVO.getTexto().contains("UrlFoto_Aluno")) {
				getFacadeFactory().getPessoaFacade().carregarUrlFotoAluno(configuracaoGeralSistemaVO, impressaoContratoVO.getMatriculaVO().getAluno());
			}
			if (textoPadraoDeclaracaoVO.getTexto().contains("NomeDocenteResponsavelAssinaturaTermoEstagio_Curso") || textoPadraoDeclaracaoVO.getTexto().contains("CpfDocenteResponsavelAssinaturaTermoEstagio_Curso") || textoPadraoDeclaracaoVO.getTexto().contains("EmailDocenteResponsavelAssinaturaTermoEstagio_Curso")) {
				impressaoContratoVO.getMatriculaVO().getCurso().setFuncionarioResponsavelAssinaturaTermoEstagioVO(getFacadeFactory().getFuncionarioFacade().consultaFuncionarioResponsavelAssinaturaTermoEstagioRapidaPorCurso(impressaoContratoVO.getMatriculaVO().getCurso().getCodigo(), usuario));
			}
			getFacadeFactory().getImpressaoDeclaracaoFacade().carregarTotalSemestresCurso(impressaoContratoVO.getMatriculaPeriodoVO().getGradeCurricular(), textoPadraoDeclaracaoVO.getTexto(), usuario);
			
			
			if (Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getMatricula()) 
					&& (textoPadraoDeclaracaoVO.getTexto().contains("Nr_Periodo_Letivo_Ingresso_Matricula") 
							|| textoPadraoDeclaracaoVO.getTexto().contains("Nr_Extenso_Periodo_Letivo_Ingresso_Matricula") 
							|| textoPadraoDeclaracaoVO.getTexto().contains("Descricao_Periodo_Letivo_Ingresso_Matricula") 
							|| textoPadraoDeclaracaoVO.getTexto().contains("Nome_Certificacao_Periodo_Letivo_Ingresso_Matricula"))) {
				impressaoContratoVO.setPeriodoLetivoIngresso(getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivoIngressoPorMatricula(impressaoContratoVO.getMatriculaVO().getMatricula(), usuario));
			}
			AutorizacaoCursoVO primeiroReconhecimentoCursoVO = new AutorizacaoCursoVO();
			AutorizacaoCursoVO renovacaoReconhecimentoCursoVO = new AutorizacaoCursoVO();
			if (textoPadraoDeclaracaoVO.getTexto().contains("PrimeiroReconhecimento_Curso") || textoPadraoDeclaracaoVO.getTexto().contains("DataPrimeiroReconhecimento_Curso")) {
				if (Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getAutorizacaoCurso())) {
					primeiroReconhecimentoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaVO().getAutorizacaoCurso().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX);
				} else {
					primeiroReconhecimentoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoDataMaisAntigo(impressaoContratoVO.getMatriculaVO().getCurso().getCodigo(), new Date(), Uteis.NIVELMONTARDADOS_COMBOBOX);
				}
				impressaoContratoVO.setPrimeiroReconhecimentoCurso(primeiroReconhecimentoCursoVO);
			}
			
			Date dataPRM = Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getDataConclusaoCurso()) ? impressaoContratoVO.getMatriculaVO().getDataConclusaoCurso() : impressaoContratoVO.getMatriculaVO().getData();
			
			if (textoPadraoDeclaracaoVO.getTexto().contains("RenovacaoReconhecimento_Curso") || textoPadraoDeclaracaoVO.getTexto().contains("DataRenovacaoReconhecimento_Curso")) {
				if (Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getRenovacaoReconhecimentoVO())) {
					renovacaoReconhecimentoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaVO().getRenovacaoReconhecimentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX);
				} else {
					renovacaoReconhecimentoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoDataVigenteMatricula(impressaoContratoVO.getMatriculaVO().getCurso().getCodigo(), dataPRM, Uteis.NIVELMONTARDADOS_COMBOBOX);
					if (!Uteis.isAtributoPreenchido(renovacaoReconhecimentoCursoVO)) {
						if (impressaoContratoVO.getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao()) {
							renovacaoReconhecimentoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoPos(impressaoContratoVO.getMatriculaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX);
						} else {
//							renovacaoReconhecimentoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoDataVigenteMatricula(impressaoContratoVO.getMatriculaVO().getCurso().getCodigo(), new Date(), Uteis.NIVELMONTARDADOS_COMBOBOX);
						}
					}
				}
				impressaoContratoVO.setRenovacaoReconhecimentoCurso(renovacaoReconhecimentoCursoVO);
				if (impressaoContratoVO.getPrimeiroReconhecimentoCurso().getCodigo().equals(impressaoContratoVO.getRenovacaoReconhecimentoCurso().getCodigo())) {
					impressaoContratoVO.setRenovacaoReconhecimentoCurso(null);
				}
			}
			ConfiguracaoGEDVO configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo(), usuario);
			impressaoContratoVO.setConfiguracaoGEDVO(configGEDVO);
			impressaoContratoVO.setUsuarioLogado(usuario);
			textoPadraoDeclaracaoVO.substituirTag(impressaoContratoVO, usuario);
			textoStr = getFacadeFactory().getTextoPadraoDeclaracaoFacade().removerBordaDaPagina(textoPadraoDeclaracaoVO.getTexto());
			textoStr = getFacadeFactory().getTextoPadraoDeclaracaoFacade().adicionarStyleFormatoPaginaTextoPadrao(textoStr, textoPadraoDeclaracaoVO.getOrientacaoDaPagina());
			return textoStr;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public List<HistoricoVO> realizarObtencaoDatasAulaAluno(List<HistoricoVO> historicoVOs, ImpressaoContratoVO impressaoContratoVO, UsuarioVO usuario, Boolean considerarSituacaoCursando) throws Exception {
		List<HistoricoVO> lista = ((List<HistoricoVO>) historicoVOs).stream().filter(h -> {
			SituacaoHistorico situacaoHistorico = SituacaoHistorico.getEnum(h.getSituacao());
			return considerarSituacaoCursando ? situacaoHistorico != null && (situacaoHistorico.getHistoricoAprovado() ||  situacaoHistorico.getHistoricoCursando()) : situacaoHistorico != null && situacaoHistorico.getHistoricoAprovado();
		})
				.collect(Collectors.toList());
		lista.forEach(hist -> {
			hist.setSituacao(hist.getApresentarAprovadoHistorico() ? "AP" : hist.getSituacao());
			getFacadeFactory().getImpressaoDeclaracaoFacade().carregarDadosDataInicioFimAula(impressaoContratoVO, usuario, hist);			
				try {
					if(impressaoContratoVO.getMatriculaPeriodoVO().getDataInicioAula() == null
							|| (Uteis.isAtributoPreenchido(hist.getDataInicioAula()) 
									&& impressaoContratoVO.getMatriculaPeriodoVO().getDataInicioAula().compareTo(hist.getDataInicioAula()) > 0)) {
						impressaoContratoVO.getMatriculaPeriodoVO().setDataInicioAula(hist.getDataInicioAula());
					}
					if(impressaoContratoVO.getMatriculaPeriodoVO().getDataFinalAula() == null
							|| (Uteis.isAtributoPreenchido(hist.getDataFimAula()) && impressaoContratoVO.getMatriculaPeriodoVO().getDataFinalAula().compareTo(hist.getDataFimAula()) < 0)) {
						impressaoContratoVO.getMatriculaPeriodoVO().setDataFinalAula(hist.getDataFimAula());
					}			
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		});		
		
		return lista;
	}
	
	public String realizarCriacaoLegendaSituacaoDisciplinaHistorico(ImpressaoContratoVO impressaoContratoVO, MatriculaVO matriculaVO) {
		StringBuilder stringLengenda = new StringBuilder();
		HashMap<String, String> mapaSituacao = new HashMap<String, String>(0);		

		if(Uteis.isAtributoPreenchido(impressaoContratoVO.getListaDisciplinasHistoricoPeriodoLetivoSituacao())) {
		for (Iterator<HistoricoVO> iterator = impressaoContratoVO.getListaDisciplinasHistoricoPeriodoLetivoSituacao().iterator(); iterator.hasNext();) {
				HistoricoVO historicoVO = (HistoricoVO) iterator.next();
				mapaSituacao.put(historicoVO.getSituacao(),
				historicoVO.getSituacao_Apresentar());
		}
		
		for (Map.Entry<String, String> elemento : mapaSituacao.entrySet()) {
			stringLengenda.append(elemento.getKey());
			stringLengenda.append(" - ");
			stringLengenda.append(elemento.getValue());
			stringLengenda.append(" : ");
		}
		mapaSituacao = null;
		return stringLengenda.toString().substring(0, stringLengenda.toString().length() - 2);
		}
		else {
			return "";
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarNotificacaoPendenciaAssinaturaContrato(DocumentoAssinadoPessoaVO documentoAssinadoPessoa , PersonalizacaoMensagemAutomaticaVO msg, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean enviarPorEmail) throws Exception {
		if (msg == null || (msg.isNovoObj() || msg.getDesabilitarEnvioMensagemAutomatica())) {
			throw new Exception("Não existe uma MENSAGEM PERSONALIZADA cadastrada ou habilidade no sistema.");
		}
		

		  if (Uteis.isAtributoPreenchido(documentoAssinadoPessoa)) {
					ComunicacaoInternaVO comunicacaoInternaVO = new ComunicacaoInternaVO();
					if (usuarioVO.getPessoa() == null || usuarioVO.getPessoa().getCodigo().equals(0)) {
						comunicacaoInternaVO.setResponsavel(configuracaoGeralSistemaVO.getResponsavelPadraoComunicadoInterno());
					} else {
						comunicacaoInternaVO.setResponsavel(usuarioVO.getPessoa());
					}
					comunicacaoInternaVO.setEnviarEmail(enviarPorEmail);
					comunicacaoInternaVO.setEnviarEmailInstitucional(Uteis.isAtributoPreenchido(msg)? msg.getEnviarEmailInstitucional(): false);
					comunicacaoInternaVO.setTipoDestinatario(documentoAssinadoPessoa.getTipoPessoa().getValor());
					comunicacaoInternaVO.setTipoMarketing(false);
					comunicacaoInternaVO.setTipoLeituraObrigatoria(false);
					comunicacaoInternaVO.setDigitarMensagem(true);
					comunicacaoInternaVO.setRemoverCaixaSaida(false);
					String mensagem = msg.getMensagem();
					mensagem = mensagem.replaceAll("NOME_PESSOA", documentoAssinadoPessoa.getPessoaVO().getNome());
					mensagem = mensagem.replaceAll("MATRICULA", documentoAssinadoPessoa.getDocumentoAssinadoVO().getMatricula().getMatricula());
					mensagem = mensagem.replaceAll("DATA_SOLICITACAO", documentoAssinadoPessoa.getDataSolicitacao().toString());
					mensagem = mensagem.replaceAll("NOME_CURSO", documentoAssinadoPessoa.getDocumentoAssinadoVO().getMatricula().getCurso().getNome());
					comunicacaoInternaVO.setAssunto(msg.getAssunto());
					comunicacaoInternaVO.setMensagem(mensagem);
					ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
					comunicadoInternoDestinatarioVO.setTipoComunicadoInterno("LE");
					comunicadoInternoDestinatarioVO.setDataLeitura(null);
					comunicadoInternoDestinatarioVO.setCiJaRespondida(false);
					comunicadoInternoDestinatarioVO.setCiJaLida(false);
					comunicadoInternoDestinatarioVO.setRemoverCaixaEntrada(false);
					comunicadoInternoDestinatarioVO.setMensagemMarketingLida(false);
					comunicadoInternoDestinatarioVO.setDestinatario(documentoAssinadoPessoa.getPessoaVO());
					comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().add(comunicadoInternoDestinatarioVO);
					try {
						getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoInternaVO, false, usuarioVO, configuracaoGeralSistemaVO,null);
					} catch (Exception e) {
						System.out.print("Erro ao enviar!");
					}
				}
			}
}
