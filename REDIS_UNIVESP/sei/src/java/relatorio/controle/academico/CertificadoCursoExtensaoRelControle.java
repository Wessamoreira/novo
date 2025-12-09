package relatorio.controle.academico;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.academico.ExpedicaoDiplomaControle;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAcademicoEnum;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoRequerimento;
import negocio.comuns.utilitarias.dominios.TiposRequerimento;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.CertificadoCursoExtensaoRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.CertificadoCursoExtensaoRel;

@SuppressWarnings("unchecked")
@Controller("CertificadoCursoExtensaoRelControle")
@Scope("viewScope")
@Lazy
public class CertificadoCursoExtensaoRelControle extends SuperControleRelatorio {

    private CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO;
    private List listaConsultaAluno;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private List listaConsultaCurso;
    private String tipoConsultaCurso;
    private String valorConsultaCurso;
    private String campoConsultaCurso;
    private String valorConsultaFuncionario;
    private String campoConsultaFuncionario;
    private String filtro;
    private List<SelectItem> selectItemsCargoFuncionarioPrincipal;
    private List<SelectItem> selectItemsCargoFuncionarioSecundario;
    private List<FuncionarioVO> listaConsultaFuncionario;
    private MatriculaVO matriculaVO;
    private TurmaVO turmaVO;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private List listaSelectItemUnidadeEnsinoVO;
    private List listaSelectItemTurmaVO;
    private String tipoLayout;
    private PeriodoLetivoVO periodoLetivoVO;
    protected List listaSelectItemPeriodoLetivo;
    private List<CertificadoCursoExtensaoRelVO> listaCertificadoErro;
    private Integer textoPadraoDeclaracao;
    private List listaSelectItemTipoTextoPadrao;    
    private List<SelectItem> listaSelectItemsDisciplinaVOs;
    private GradeDisciplinaVO gradeDisciplinaVO;
    private String tipoDisciplinaPeriodoSelecionado = "DisciplinaAtePeriodoSelecionado";
    private File arquivoZip;
    private RequerimentoVO requerimentoVO;
    private Boolean gerarNovoArquivoAssinado;
    private Boolean impressaoContratoExistente;
    private List<File> listaArquivos = new ArrayList<File>(0);
    private boolean permitirExibirRegraEmissao = false;
    private boolean assinarDigitalmente = false;
    private Boolean trazerTodasSituacoesAprovadas;
	private String campoConsultaProgramacaoFormatura;
    private Boolean mostrarSegundoCampoProgramacaoFormatura;
    private String valorConsultaProgramacaoFormatura;
    private Date valorConsultaDataInicioProgramacaoFormatura;
    private Date valorConsultaDataFinalProgramacaoFormatura;
    private String filtroAlunosPresentesColacaoGrau;
    private List<ProgramacaoFormaturaVO> listaConsultaProgramacaoFormatura;
    private Boolean apresentarProgramacaoFormatura ;
    private ProgramacaoFormaturaVO programacaoFormaturaVO;
    private Boolean apresentarResultadoConsultaMatriculaGerarCertificado;   
	private Boolean apresentarListaErros;
    public List<CertificadoCursoExtensaoRelVO> listaCertificadoCursoExtensaoRelVOErros;
    private ProgressBarVO progressBarVO;
    private List<CertificadoCursoExtensaoRelVO> listaCertificadoCursoExtensaoRelVOGerar;
    private  TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO;
    private PlanoEnsinoVO planoEnsinoVO;
    private Boolean painelALunosGerarAberto  ;
    private Boolean painelALunosGerarErroAberto  ;
    private String valorConsultaTurma;
    private String campoConsultaTurma;
    private List<TurmaVO> listaConsultaTurma;

    public CertificadoCursoExtensaoRelControle() throws Exception {
        montarListaSelectItemUnidadeEnsino();
        setTipoRequerimento(TiposRequerimento.CERTIFICADO_MODULAR.getValor());
        verificarPermissaoRegraEmissao();
        //obterUsuarioLogado();
        setMensagemID("msg_entre_prmrelatorio");
    }
    
    public List getListaTipoLayout() {
		List itens = new ArrayList(0);
			itens.add(new SelectItem("CertificadoCursoExtensaoRel", "Layout 1"));
			itens.add(new SelectItem("CertificadoCursoExtensao2Rel", "Layout 2"));
			itens.add(new SelectItem("TextoPadrao", "Texto Padrão"));
		return itens;
	}
    
    public List<SelectItem> tipoFiltroDisciplina;
    public List<SelectItem> getTipoFiltroDisciplina() {
    	if(tipoFiltroDisciplina == null){
    		tipoFiltroDisciplina = new ArrayList<SelectItem>(0);
    		tipoFiltroDisciplina.add(new SelectItem("DisciplinaAtePeriodoSelecionado", UteisJSF.internacionalizar("prt_CertificadoExtensao_disciplinaAtePeriodoSelecionado")));
    		tipoFiltroDisciplina.add(new SelectItem("DisciplinaDoPeriodoSelecionado", UteisJSF.internacionalizar("prt_CertificadoExtensao_disciplinaDoPeriodoSelecionado")));    		
    	}
    	return tipoFiltroDisciplina;
    }
    
    private boolean verificarPermissaoRegraEmissao() {
    	try {
    		ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.PERMITE_DEFINIR_REGRA_EMISSAO_CERTIFICADO, getUsuarioLogado());
    		setPermitirExibirRegraEmissao(true);
		} catch (Exception e) {
			setPermitirExibirRegraEmissao(false);
		}
    	return isPermitirExibirRegraEmissao();
    }
    
    public void persistirDadosPadroes(){
    	try {
    		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoLayout(), "CertExtensao_"+getUnidadeEnsinoVO().getCodigo(), "layout", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getCertificadoCursoExtensaoRelVO().getFuncionarioPrincipalVO().getCodigo().toString(), 
					"CertExtensao_"+getUnidadeEnsinoVO().getCodigo(), "funcionarioPrincipal", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getCertificadoCursoExtensaoRelVO().getCargoFuncionarioPrincipal().getCodigo().toString(), 
					"CertExtensao_"+getUnidadeEnsinoVO().getCodigo(), "cargoFuncionarioPrincipal", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getCertificadoCursoExtensaoRelVO().getFuncionarioSecundarioVO().getCodigo().toString(), 
					"CertExtensao_"+getUnidadeEnsinoVO().getCodigo(), "funcionarioSecundario", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getCertificadoCursoExtensaoRelVO().getCargoFuncionarioSecundario().getCodigo().toString(), 
					"CertExtensao_"+getUnidadeEnsinoVO().getCodigo(), "cargoFuncionarioSecundario", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoDisciplinaPeriodoSelecionado(), 
					"CertExtensao_"+getUnidadeEnsinoVO().getCodigo(), "tipoDisciplinaPeriodoSelecionado", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTextoPadraoDeclaracao().toString(), 
					"CertExtensao_"+getUnidadeEnsinoVO().getCodigo(), "textoPadraoDeclaracao", getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
    }
    
    public void carregarDadosPadroes(){
    	try {
    		if(Uteis.isAtributoPreenchido(getUnidadeEnsinoVO().getCodigo())){
    		Map<String, String> resultados = new HashMap<>();
			resultados = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[]{"layout", "funcionarioPrincipal", "cargoFuncionarioPrincipal",	"funcionarioSecundario", "cargoFuncionarioSecundario", "tipoDisciplinaPeriodoSelecionado", "textoPadraoDeclaracao"}, "CertExtensao_"+getUnidadeEnsinoVO().getCodigo());
			for(String key: resultados.keySet()){
				if(key.equals("layout")){
					setTipoLayout(resultados.get(key));
					montarListaSelectItemPeriodoLetivo();
				}else if(key.equals("funcionarioPrincipal") && resultados.get(key) != null  && !resultados.get(key).equals("null")  && !resultados.get(key).equals("0")){
					try{
						FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorChavePrimaria(Integer.valueOf(resultados.get(key)), false, getUsuarioLogado());
						getCertificadoCursoExtensaoRelVO().setFuncionarioPrincipalVO(funcionarioVO);
				        consultarFuncionarioPrincipal();
					}catch(Exception e){
						
					}
				}else if(key.equals("cargoFuncionarioPrincipal") && resultados.get(key) != null  && !resultados.get(key).equals("null")  && !resultados.get(key).equals("0")){
					try{						
						getCertificadoCursoExtensaoRelVO().getCargoFuncionarioPrincipal().setCodigo(Integer.valueOf(resultados.get(key)));						
					}catch(Exception e){
						
					}
				}else if(key.equals("funcionarioSecundario") && resultados.get(key) != null  && !resultados.get(key).equals("null")  && !resultados.get(key).equals("0")){
					try{
						FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorChavePrimaria(Integer.valueOf(resultados.get(key)), false, getUsuarioLogado());
						getCertificadoCursoExtensaoRelVO().setFuncionarioSecundarioVO(funcionarioVO);
						consultarFuncionarioSecundario();
					}catch(Exception e){
						
					}
				}else if(key.equals("cargoFuncionarioSecundario") && resultados.get(key) != null  && !resultados.get(key).equals("null")  && !resultados.get(key).equals("0")){
					try{						
						getCertificadoCursoExtensaoRelVO().getCargoFuncionarioSecundario().setCodigo(Integer.valueOf(resultados.get(key)));						
					}catch(Exception e){
						
					}
				}else if(key.equals("tipoDisciplinaPeriodoSelecionado")){
					setTipoDisciplinaPeriodoSelecionado(resultados.get(key));
				}else if(key.equals("textoPadraoDeclaracao")  && resultados.get(key) != null  && !resultados.get(key).equals("null")  && !resultados.get(key).equals("0")){
					setTextoPadraoDeclaracao(Integer.valueOf(resultados.get(key)));
				}
			}
    		}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
    }

public void imprimirPDF() {
		
		List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs = new ArrayList<CertificadoCursoExtensaoRelVO>(0);
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "CertificadoCursoExtensaoRelControle", "Inicializando Geração de Relátorio Certificado Curso de Extensão", "Emitindo Relatório");
			CertificadoCursoExtensaoRel.validarDados(getMatriculaVO(), getFiltro(), getTipoLayout(), getTurmaVO(), getRequerimentoVO(), getCertificadoCursoExtensaoRelVO(),getUnidadeEnsinoVO(),  getProgramacaoFormaturaVO());
			if (!getTipoLayout().equals("TextoPadrao")) {
				montarRelatorioPorLayoutDiferenteDeTextoPadrao(certificadoCursoExtensaoRelVOs);
			} else {
				montarRelatorioPorLayoutTextoPadrao(certificadoCursoExtensaoRelVOs);
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "CertificadoCursoExtensaoRelControle", "Finalizando Geração de Relátorio Certificado Curso de Extensão", "Emitindo Relatório");
			// apresentarRelatorioObjetos(CertificadoCursoExtensaoRel.getIdEntidade(),
			// titulo, nomeEmpresa, "", "PDF", "", design,
			// getUsuarioLogado().getNome(),
			// getFacadeFactory().getCertificadoCursoExtensaoRelFacade().getDescricaoFiltros(),
			// certificadoCursoExtensaoRelVOs,
			// CertificadoCursoExtensaoRel.getCaminhoBaseRelatorio());
		} catch (Exception e) {  
			setFazerDownload(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(certificadoCursoExtensaoRelVOs);
			//removerObjetoMemoria(this);

		}
	}

	public List<SelectItem> getComboboxProvedorAssinaturaPadrao(){
		
		Integer codigoUnidadeEnsino = Uteis.isAtributoPreenchido(getMatriculaVO().getUnidadeEnsino().getCodigo()) ? getMatriculaVO().getUnidadeEnsino().getCodigo() : 0;
		if(!Uteis.isAtributoPreenchido(codigoUnidadeEnsino)){
			return new ArrayList<SelectItem>();
		}
		return this.getComboboxProvedorAssinaturaPadrao(codigoUnidadeEnsino, TipoOrigemDocumentoAssinadoEnum.EMISSAO_CERTIFICADO);
	}
	
	public void montarRelatorioPorLayoutDiferenteDeTextoPadrao(List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs) throws Exception{
		String titulo = null;
		String design = null;
		getProvedorDeAssinaturaEnum();
		getFacadeFactory().getCertificadoCursoExtensaoRelFacade().setDescricaoFiltros("");
		titulo = "CERTIFICADO CURSO DE EXTENSÃO";
		design = getDesignIReportRelatorio();
		certificadoCursoExtensaoRelVO.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()));
		certificadoCursoExtensaoRelVOs = getFacadeFactory().getCertificadoCursoExtensaoRelFacade().criarObjeto(getCertificadoCursoExtensaoRelVO(), getMatriculaVO(), getPeriodoLetivoVO(), getTurmaVO(), getTipoLayout(), getFiltro(), getConfiguracaoFinanceiroPadraoSistema(), getTipoDisciplinaPeriodoSelecionado().equals("DisciplinaDoPeriodoSelecionado"), getGradeDisciplinaVO().getDisciplina(),  getUsuarioLogado(), getTrazerTodasSituacoesAprovadas(), getProgramacaoFormaturaVO(),getListaCertificadoCursoExtensaoRelVOErros(), getListaCertificadoCursoExtensaoRelVOGerar());

		if (!certificadoCursoExtensaoRelVOs.isEmpty()) {
			setListaCertificadoErro(getFacadeFactory().getCertificadoCursoExtensaoRelFacade().executarValidarSituacaoDisciplinaAprovada(certificadoCursoExtensaoRelVOs));
			getFacadeFactory().getCertificadoCursoExtensaoRelFacade().validarRegraEmissao(certificadoCursoExtensaoRelVOs, getListaCertificadoErro(),  getUsuarioLogado(), getFiltro());
			
			for (CertificadoCursoExtensaoRelVO certificadoErro : getListaCertificadoErro()) {
				if (certificadoCursoExtensaoRelVOs.contains(certificadoErro)) {
					certificadoCursoExtensaoRelVOs.remove(certificadoErro);
				}
			}
			if (!certificadoCursoExtensaoRelVOs.isEmpty()) {
				executaGeracaoRelatorio(certificadoCursoExtensaoRelVOs, titulo, design,getUsuarioLogado() ,isAssinarDigitalmente());
				persistirDadosPadroes();
//				removerObjetoMemoria(this);
				montarListaSelectItemUnidadeEnsino();
			} else {
				setFazerDownload(Boolean.FALSE);
			}
		} else {
			setMensagemID("msg_relatorio_sem_dados");
			setFazerDownload(Boolean.FALSE);
		}
	}
	
	public void executaGeracaoRelatorio(List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs, String titulo, String design, UsuarioVO usuarioLogado, Boolean apresentarNomeArquivoCustomizado) throws Exception{
		String nomeCustomizado = "";
		getSuperParametroRelVO().setTituloRelatorio(titulo);
		getSuperParametroRelVO().setNomeDesignIreport(design);
		getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
		getSuperParametroRelVO().setSubReport_Dir(CertificadoCursoExtensaoRel.getCaminhoBaseRelatorio());
		getSuperParametroRelVO().setCaminhoBaseRelatorio(CertificadoCursoExtensaoRel.getCaminhoBaseRelatorio());
		getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
		getSuperParametroRelVO().setNomeUsuario(usuarioLogado.getNome());
		getSuperParametroRelVO().setListaObjetos(certificadoCursoExtensaoRelVOs);		
		if(apresentarNomeArquivoCustomizado) {		
		     nomeCustomizado = "CERTIFICADO_" + Uteis.removeCaractersEspeciais(certificadoCursoExtensaoRelVOs.get(0).getMatriculaVO().getAluno().getNome().replace("/", "_").replace(" ", "_")).toUpperCase() +"_"+getUsuarioLogado().getCodigo()+"_"+ new Date().getTime();		
			 getSuperParametroRelVO().setNomeEspecificoRelatorio(nomeCustomizado);
			
			}

		montarDadosAssinaturaDigitalFuncionario(usuarioLogado);
		realizarImpressaoRelatorio();
		if (isAssinarDigitalmente()) {
			setCaminhoRelatorio(getFacadeFactory().getDocumentoAssinadoFacade().realizarInclusaoDocumentoAssinadoPorEmissaoCertificado(getCaminhoRelatorio(), certificadoCursoExtensaoRelVOs.get(0).getMatriculaVO(), getUnidadeEnsinoVO(),  getTurmaVO(), TipoOrigemDocumentoAssinadoEnum.EMISSAO_CERTIFICADO,getProvedorDeAssinaturaEnum(), getCorAssinaturaDigitalmente(), 40f, getLarguraAssinatura(), getCertificadoCursoExtensaoRelVO().getFuncionarioPrincipalVO(), getCertificadoCursoExtensaoRelVO().getCargoFuncionarioPrincipal().getNome(),"", getCertificadoCursoExtensaoRelVO().getFuncionarioSecundarioVO(), getCertificadoCursoExtensaoRelVO().getCargoFuncionarioSecundario().getNome(), "", getConfiguracaoGeralPadraoSistema(),usuarioLogado, apresentarNomeArquivoCustomizado , nomeCustomizado ));
			getListaDocumentoAsssinados().clear();
		}
		setMensagemID("msg_relatorio_ok");
	}
	
	private void montarDadosAssinaturaDigitalFuncionario(UsuarioVO usuarioLogado) throws Exception {
		if(isAssinarDigitalmente()) {
			ConfiguracaoGEDVO configGEDVO = null;
			if (getIsFiltrarPorAluno()) {
				configGEDVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getCodigo(), false, usuarioLogado);
			} else if (getIsFiltrarPorProgramacaoFormatura()){
				configGEDVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(getProgramacaoFormaturaVO().getUnidadeEnsino().getCodigo(), false, usuarioLogado);
			}else {
				configGEDVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(getTurmaVO().getUnidadeEnsino().getCodigo(), false, usuarioLogado);
			}
			if (!Uteis.isAtributoPreenchido(configGEDVO.getCodigo())) {
				return;
			}
			if(configGEDVO.getConfiguracaoGedCertificadoVO().getApresentarAssinaturaDigitalizadoFuncionario()) {
				if (Uteis.isAtributoPreenchido(getCertificadoCursoExtensaoRelVO().getFuncionarioPrincipalVO().getCodigo())) {
					getCertificadoCursoExtensaoRelVO().getFuncionarioPrincipalVO().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarAssinaturaDigitalFuncionarioPorCodigoFuncionario(getCertificadoCursoExtensaoRelVO().getFuncionarioPrincipalVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, usuarioLogado));
					if (Uteis.isAtributoPreenchido(getCertificadoCursoExtensaoRelVO().getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getCodigo())) {
						getSuperParametroRelVO().adicionarParametro("assinaturaDigitalFuncionarioPrimario", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getCertificadoCursoExtensaoRelVO().getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getPastaBaseArquivoEnum().getValue() + File.separator + getCertificadoCursoExtensaoRelVO().getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getNome());
						getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioPrimario", configGEDVO.getConfiguracaoGedCertificadoVO().getApresentarAssinaturaDigitalizadoFuncionario());
					} else {
						getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioPrimario", false);
					}
				}
				if (Uteis.isAtributoPreenchido(getCertificadoCursoExtensaoRelVO().getFuncionarioSecundarioVO().getCodigo())) {
					getCertificadoCursoExtensaoRelVO().getFuncionarioSecundarioVO().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarAssinaturaDigitalFuncionarioPorCodigoFuncionario(getCertificadoCursoExtensaoRelVO().getFuncionarioSecundarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, usuarioLogado));
					if (Uteis.isAtributoPreenchido(getCertificadoCursoExtensaoRelVO().getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getCodigo())) {
						getSuperParametroRelVO().adicionarParametro("assinaturaDigitalFuncionarioSecundario", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getCertificadoCursoExtensaoRelVO().getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getPastaBaseArquivoEnum().getValue() + File.separator + getCertificadoCursoExtensaoRelVO().getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getNome());
						getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioSecundario", configGEDVO.getConfiguracaoGedCertificadoVO().getApresentarAssinaturaDigitalizadoFuncionario());
					} else {
						getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioSecundario", false);
					}
				} 
			}
		}
	}

	public void montarRelatorioPorLayoutTextoPadrao(List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs) throws Exception{
		if (Uteis.isAtributoPreenchido(getGradeDisciplinaVO())) {
			setGradeDisciplinaVO(getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimaria(getGradeDisciplinaVO().getCodigo(), getUsuarioLogado()));
		}
		if (!Uteis.isAtributoPreenchido(getGradeDisciplinaVO()) || Uteis.isAtributoPreenchido(getGradeDisciplinaVO().getDisciplina())) {
			getGradeDisciplinaVO().setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getGradeDisciplinaVO().getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		}
		certificadoCursoExtensaoRelVOs = getFacadeFactory().getCertificadoCursoExtensaoRelFacade().criarObjeto(getCertificadoCursoExtensaoRelVO(), getMatriculaVO(), getPeriodoLetivoVO(), getTurmaVO(), getTipoLayout(), getFiltro(), getConfiguracaoFinanceiroPadraoSistema(), getTipoDisciplinaPeriodoSelecionado().equals("DisciplinaDoPeriodoSelecionado"), getGradeDisciplinaVO().getDisciplina(), getUsuarioLogado(), getTrazerTodasSituacoesAprovadas(), getProgramacaoFormaturaVO(),getListaCertificadoCursoExtensaoRelVOErros() ,getListaCertificadoCursoExtensaoRelVOGerar());
		if (!certificadoCursoExtensaoRelVOs.isEmpty()) {
			setListaCertificadoErro(getFacadeFactory().getCertificadoCursoExtensaoRelFacade().executarValidarSituacaoDisciplinaAprovada(certificadoCursoExtensaoRelVOs));
			for (CertificadoCursoExtensaoRelVO certificadoErro : getListaCertificadoErro()) {
				if (certificadoCursoExtensaoRelVOs.contains(certificadoErro)) {
					certificadoCursoExtensaoRelVOs.remove(certificadoErro);
				}
			}
			if (!certificadoCursoExtensaoRelVOs.isEmpty()) {
				PlanoEnsinoVO plano = null;
				TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(textoPadraoDeclaracao, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(getRequerimentoVO())) {
					setGerarNovoArquivoAssinado(getGerarNovoArquivoAssinado() && textoPadraoDeclaracaoVO.getAssinarDigitalmenteTextoPadrao());
				} else {
					setGerarNovoArquivoAssinado(textoPadraoDeclaracaoVO.getAssinarDigitalmenteTextoPadrao());
				}
				getFacadeFactory().getCertificadoCursoExtensaoRelFacade().validarRegraEmissao(certificadoCursoExtensaoRelVOs,  getListaCertificadoCursoExtensaoRelVOErros(), getUsuarioLogado(), getFiltro());
				
				if (!getGradeDisciplinaVO().getCodigo().equals(0) || !getGradeDisciplinaVO().getDisciplina().getCodigo().equals(0)) {
					String ano = "";
					String semestre = ""; 
					if(!certificadoCursoExtensaoRelVOs.get(0).getCertificadoCursoExtensaoDisciplinasRelVOs().isEmpty()){
						ano = certificadoCursoExtensaoRelVOs.get(0).getCertificadoCursoExtensaoDisciplinasRelVOs().get(0).getAno();
						semestre = certificadoCursoExtensaoRelVOs.get(0).getCertificadoCursoExtensaoDisciplinasRelVOs().get(0).getSemestre();
					}else{
						ano = obterAnoDeAcordoPeriodicidadeCurso();
						semestre = obterSemestreDeAcordoPeriodicidadeCurso();
					}
					if(!getGradeDisciplinaVO().getDisciplina().getCodigo().equals(0) && !certificadoCursoExtensaoRelVOs.get(0).getCertificadoCursoExtensaoDisciplinasRelVOs().isEmpty()){
						getGradeDisciplinaVO().setCargaHoraria(Integer.valueOf(certificadoCursoExtensaoRelVOs.get(0).getCertificadoCursoExtensaoDisciplinasRelVOs().get(0).getCargaHoraria()));
					}
					plano = (getFacadeFactory().getPlanoEnsinoFacade().consultarPorUnidadeEnsinoCursoDisciplinaAnoSemestre(getMatriculaVO().getUnidadeEnsino().getCodigo(), getMatriculaVO().getCurso().getCodigo(), getGradeDisciplinaVO().getDisciplina().getCodigo(), ano, semestre, null, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
				}
				Boolean perssistirDocumento = true;
				if (Uteis.isAtributoPreenchido(getRequerimentoVO())) {
					if (getRequerimentoVO().getIsFormatoCertificadoSelecionadoImpresso()) {
						perssistirDocumento = false;
					} else {
						perssistirDocumento = true;
					}
				} else {
					if (Uteis.isAtributoPreenchido(getRequerimentoVO().getMatricula().getMatricula())) {
						perssistirDocumento = false;
					}
				}
				HashMap<String, Object> hashMap = getFacadeFactory().getCertificadoCursoExtensaoRelFacade().realizarMontagemRelatorioPorTextoPadrao(certificadoCursoExtensaoRelVOs, getListaArquivos(), textoPadraoDeclaracaoVO, plano, getGradeDisciplinaVO(),  getRequerimentoVO(),  getConfiguracaoGeralPadraoSistema(), getGerarNovoArquivoAssinado(), perssistirDocumento, getUsuarioLogado());
				setImpressaoContratoExistente(((Boolean)hashMap.get("impressaoContratoExistente")));
				if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()){
					persistirDadosPadroes();
				}
				executarValidacaoParaQualLayoutImprimirRelatorio(textoPadraoDeclaracaoVO, ((String)hashMap.get("texto")));
				getListaArquivos().clear();
				
			} else {
				setVisualizarCertificado(false);
				setFazerDownload(Boolean.FALSE);
			}
		}else {
			if (Uteis.isAtributoPreenchido(getListaCertificadoCursoExtensaoRelVOErros()) && Uteis.isAtributoPreenchido(getNomeTelaAtual()) && (getNomeTelaAtual().contains("requerimento"))) {
				throw new Exception(getListaCertificadoCursoExtensaoRelVOErros().stream().map(c -> c.getMotivoErro()).collect(Collectors.joining("; ")));
			}
			setMensagemID("msg_relatorio_sem_dados");
			setFazerDownload(Boolean.FALSE);
		}
	}
	
	public void executarValidacaoParaQualLayoutImprimirRelatorio (TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO, String textos) throws IOException{		
		if( (textoPadraoDeclaracaoVO == null || textoPadraoDeclaracaoVO.getTipoDesigneTextoEnum().isPdf() )&& getListaArquivos().size() > 1){
			setArquivoZip(new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.ARQUIVO.getValue() + File.separator + new Date().getTime() + ".zip"));
			getFacadeFactory().getArquivoHelper().zip(getListaArquivos().toArray(new File[getListaArquivos().size()]), getArquivoZip());
			setFazerDownload(false);
			setVisualizarCertificado(false);
			setImpressaoContratoExistente(false);
		}else if(textoPadraoDeclaracaoVO != null &&  textoPadraoDeclaracaoVO.getTipoDesigneTextoEnum().isPdf() && getListaArquivos().size() == 1){
			setCaminhoRelatorio(getListaArquivos().get(0).getName());
			setVisualizarCertificado(false);
			setArquivoZip(null);
			if(getImpressaoContratoExistente()){
				setFazerDownload(false);
			}else{
				setGerarNovoArquivoAssinado(false);
				setFazerDownload(true);	
			}
		}else if(textoPadraoDeclaracaoVO != null ) {
			setVisualizarCertificado(true);
			setFazerDownload(false);
			setArquivoZip(null);
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("textoRelatorio", textos);	
		}
	}
	
	public String obterAnoDeAcordoPeriodicidadeCurso() {
		if (getMatriculaVO().getCurso().getPeriodicidade().equals("AN") || getMatriculaVO().getCurso().getPeriodicidade().equals("SE")) {
			return Uteis.getAnoDataAtual4Digitos();
		}
		return "";
	}
	
	public String obterSemestreDeAcordoPeriodicidadeCurso() {
		if (getMatriculaVO().getCurso().getPeriodicidade().equals("SE")) {
			return Uteis.getSemestreAtual();
		}
		return "";
	}

    public void consultarFuncionario() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaFuncionario().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");

            if (getCampoConsultaFuncionario().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("nomeCidade")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCidade(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("CPF")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("cargo")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCargo(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("departamento")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeDepartamento(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeUnidadeEnsino(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaFuncionario(objs);
            executarMetodoControle(ExpedicaoDiplomaControle.class.getSimpleName(), "setMensagemID", "msg_dados_consultados");
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaFuncionario(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarFuncionarioPrincipal() throws Exception {
    	try {
	        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
	        getCertificadoCursoExtensaoRelVO().setFuncionarioPrincipalVO(obj);
	        consultarFuncionarioPrincipal();
            setMensagemID("msg_dados_consultados");
    	} catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
	}

    public void selecionarFuncionarioSecundario() throws Exception {
    	try {
	        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
	        getCertificadoCursoExtensaoRelVO().setFuncionarioSecundarioVO(obj);
	        consultarFuncionarioSecundario();
	        setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
	        setMensagemDetalhada("msg_erro", e.getMessage());
	    }
    }
    
    public void consultarAluno() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaAluno().equals("")) {
                throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                objs = getFacadeFactory().getMatriculaFacade().consultarPorMatriculaNivelEducacional(getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), "", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoaNivelEducacional(getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), "", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultarPorNomeCursoNivelEducacional(getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), "", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("registroAcademico")) {
            	objs = getFacadeFactory().getMatriculaFacade().consultarPorRegistroAcademicoNivelEducacional(getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), "", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarFuncionarioPrincipal() throws Exception {
        try {
            getCertificadoCursoExtensaoRelVO().setFuncionarioPrincipalVO(consultarFuncionarioPorMatricula(getCertificadoCursoExtensaoRelVO().getFuncionarioPrincipalVO().getMatricula()));
            setSelectItemsCargoFuncionarioPrincipal(montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(getCertificadoCursoExtensaoRelVO().getFuncionarioPrincipalVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())));
            if (getSelectItemsCargoFuncionarioPrincipal() != null && !getSelectItemsCargoFuncionarioPrincipal().isEmpty()) {
                getCertificadoCursoExtensaoRelVO().getCargoFuncionarioPrincipal().setCodigo((Integer) getSelectItemsCargoFuncionarioPrincipal().get(0).getValue());
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarFuncionarioSecundario() throws Exception {
        try {
            getCertificadoCursoExtensaoRelVO().setFuncionarioSecundarioVO(consultarFuncionarioPorMatricula(getCertificadoCursoExtensaoRelVO().getFuncionarioSecundarioVO().getMatricula()));
            setSelectItemsCargoFuncionarioSecundario(montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(getCertificadoCursoExtensaoRelVO().getFuncionarioSecundarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())));
            if (getSelectItemsCargoFuncionarioSecundario() != null  && !getSelectItemsCargoFuncionarioSecundario().isEmpty()) {
    			getCertificadoCursoExtensaoRelVO().getCargoFuncionarioSecundario().setCodigo((Integer) getSelectItemsCargoFuncionarioSecundario().get(0).getValue());
    		}
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public FuncionarioVO consultarFuncionarioPorMatricula(String matricula) throws Exception {
        FuncionarioVO funcionarioVO = null;
        try {
            funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(matricula, 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            if (Uteis.isAtributoPreenchido(funcionarioVO)) {
                return funcionarioVO;
            } else {
                setMensagemDetalhada("msg_erro", "Funcionário de matrícula " + matricula + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return new FuncionarioVO();
    }

    public void selecionarAluno() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
        obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());        
        setMatriculaVO(obj);
        setUnidadeEnsinoVO(obj.getUnidadeEnsino());
        carregarDadosPadroes();
        valorConsultaAluno = "";
        campoConsultaAluno = "";
        getListaConsultaAluno().clear();
    }
    
    public void consultarListaSelectItemTipoTextoPadrao(Integer unidadeEnsino) {
        try {
            getListaSelectItemTipoTextoPadrao().clear();
            List<TextoPadraoDeclaracaoVO> lista = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("CE", unidadeEnsino, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            for (TextoPadraoDeclaracaoVO objeto : lista) {
                getListaSelectItemTipoTextoPadrao().add(new SelectItem(objeto.getCodigo(), objeto.getDescricao()));
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List<SelectItem> montarComboCargoFuncionario(List<FuncionarioCargoVO> cargos) throws Exception {
        if (cargos != null && !cargos.isEmpty()) {
            List<SelectItem> selectItems = new ArrayList<SelectItem>();
            for (FuncionarioCargoVO funcionarioCargoVO : cargos) {
                selectItems.add(new SelectItem(funcionarioCargoVO.getCargo().getCodigo(), funcionarioCargoVO.getCargo().getNome() + " - " + funcionarioCargoVO.getUnidade().getNome()));
            }
            return selectItems;
        } else {
            setMensagemDetalhada("O Funcionário selecionado não possui cargo configurado");
        }
        return null;
    }

    public void consultarAlunoPorMatricula() throws Exception {
        try {
            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatriculaVO().getMatricula(), getUnidadeEnsinoVO().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
            if (objAluno.getMatricula().equals("")) {
                throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            setMatriculaVO(objAluno);
            setUnidadeEnsinoVO(objAluno.getUnidadeEnsino());
            carregarDadosPadroes();
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setMatriculaVO(new MatriculaVO());
        }
    }

    public void consultarCurso() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCurso().equals("codigo")) {
                if (getValorConsultaCurso().equals("")) {
                    throw new Exception("Informe um código para realização da consulta!");
                }
                int valorInt = Integer.parseInt(getValorConsultaCurso());
                objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCursoUnidadeEnsinoNivelEducacional(
                        valorInt, getUnidadeEnsinoVO().getCodigo(), "EX", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaCurso().equals("nome")) {
                objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsinoNivelEducacional(
                        getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), "EX", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), "");
            }

            setListaConsultaCurso(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaCurso(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void selecionarCurso() throws Exception {
        try {
            UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocursoItens");
            setMensagemDetalhada("");
            getTurmaVO().setCurso(unidadeEnsinoCurso.getCurso());
            getTurmaVO().getUnidadeEnsino().setCodigo(unidadeEnsinoCurso.getUnidadeEnsino());
            setUnidadeEnsinoVO( getTurmaVO().getUnidadeEnsino());
            montarListaSelectItemTurma();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void selecionarRequerimento() {
		try {
			RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getRequestMap().get("requerimentoItens");
			if(!obj.getSituacao().equals(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor())){
				throw new Exception("Não é possível imprimir um requerimento que não esteja com a situação " + SituacaoRequerimento.FINALIZADO_DEFERIDO.getDescricao());
			}
			setRequerimentoVO(getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema()));
			setMatriculaVO(getRequerimentoVO().getMatricula());				
			setTipoLayout("TextoPadrao");
			setTextoPadraoDeclaracao(getRequerimentoVO().getTipoRequerimento().getTextoPadrao().getCodigo());			
			setPeriodoLetivoVO(getFacadeFactory().getPeriodoLetivoFacade().consultarPorGradeCurricularDisciplina(getRequerimentoVO().getDisciplina().getCodigo(), getRequerimentoVO().getMatricula().getGradeCurricularAtual().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));		
			setGradeDisciplinaVO(getFacadeFactory().getGradeDisciplinaFacade().consultarPorCodigoPeriodoLetivoCodigoDisciplina(getPeriodoLetivoVO().getCodigo(), getRequerimentoVO().getDisciplina().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			getGradeDisciplinaVO().setDisciplina(getRequerimentoVO().getDisciplina());
			setTurmaVO(getRequerimentoVO().getTurma());
			setTipoDisciplinaPeriodoSelecionado("DisciplinaDoPeriodoSelecionado");
			montarListaSelectItemPeriodoLetivo();
			carregarDadosPadroes();
			getListaConsultaRequerimento().clear();
			setCampoConsultaRequerimento("");
			setValorConsultaRequerimento("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
    
    public void limparDadosRequerimento() throws Exception {
    	setRequerimentoVO(new RequerimentoVO());
    	setMatriculaVO(new MatriculaVO());
    	setPeriodoLetivoVO(new PeriodoLetivoVO());
    	setGradeDisciplinaVO(new GradeDisciplinaVO());
        setTextoPadraoDeclaracao(0);
        getListaSelectItemTipoTextoPadrao().clear();
        getListaConsultaRequerimento().clear();
		setCampoConsultaRequerimento("");
		setValorConsultaRequerimento("");
    }
    
    public void montarListaSelectItemPeriodoLetivo() {
    	try {
    		montarListaSelectItemPeriodoLetivo(0);
            if (getTipoLayout().equals("TextoPadrao")) {
            	consultarListaSelectItemTipoTextoPadrao(0);
            }
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public void montarListaSelectItemPeriodoLetivo(Integer maiorPeriodoLetivo) {
		List resultado = null;
		Iterator i = null;
		try {
			List objs = new ArrayList(0);
			if ((getIsFiltrarPorAluno() || getIsFiltrarPorRequerimento()) && (getTipoLayout().equals("CertificadoCursoExtensao2Rel") || getTipoLayout().equals("TextoPadrao"))) {
				resultado = consultarPeriodoLetivoMatricula();
				objs.add(new SelectItem(0, ""));
				i = resultado.iterator();
				while (i.hasNext()) {
					PeriodoLetivoVO obj = (PeriodoLetivoVO) i.next();
					String value = obj.getPeriodoLetivo().toString() + " - Período";
					objs.add(new SelectItem(obj.getCodigo(), value));
				}
				if (getTipoLayout().equals("TextoPadrao")) {
					consultarDisciplinaPorPeriodoLetivo();
				}
			} else {
				objs.add(new SelectItem(maiorPeriodoLetivo, maiorPeriodoLetivo.toString()));
			}
			setListaSelectItemPeriodoLetivo(objs);
		} catch (Exception e) {
			setListaSelectItemPeriodoLetivo(new ArrayList(0));
		} finally {
			Uteis.liberarListaMemoria(resultado);
			i = null;
		}

	}    
    
	public List<SelectItem> consultarPeriodoLetivoMatricula() throws Exception {
		List lista = getFacadeFactory().getPeriodoLetivoFacade().consultarPorMatriculaPeriodoLetivo(getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

    public void montarListaSelectItemTurma() {
        try {
            List resultadoConsulta = consultarTurmaPorUnidadeEnsinoCurso();
            setListaSelectItemTurmaVO(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "identificadorTurma", false));
        } catch (Exception e) {
            ////System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public List consultarTurmaPorUnidadeEnsinoCurso() throws Exception {
        List lista = getFacadeFactory().getTurmaFacade().consultarPorUnidadeEnsinoCurso(getUnidadeEnsinoVO().getCodigo(), getTurmaVO().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
        return lista;
    }

   
   	 public void limparTodosDados() throws Exception {
	        limparDadosAluno();
	        limparDadosCurso();
	        limparDadosFuncionarioPrincipal();
	        limparDadosFuncionarioSecundario();
	        limparDadosRequerimento();
	        limparDadosProgramacaoFormatura();
	        getListaArquivos().clear();
	        getListaCertificadoCursoExtensaoRelVOGerar().clear();
		    getListaCertificadoCursoExtensaoRelVOErros().clear();
	       
	        setApresentarResultadoConsultaMatriculaGerarCertificado(Boolean.FALSE);
	        setArquivoZip(null);
	        setCaminhoRelatorio("");

	    }

    public void limparDadosAluno() throws Exception {
        setMatriculaVO(new MatriculaVO());
        setMensagemID("msg_entre_prmconsulta");
        setPeriodoLetivoVO(null);
        setTipoLayout("");
    }

    public void limparModalDadosAluno() throws Exception {
        setMatriculaVO(new MatriculaVO());
        setValorConsultaAluno("");
        getListaConsultaAluno().clear();
        setMensagemID("msg_entre_prmconsulta");
    }

    public void limparModalDadosCurso() throws Exception {
        setTurmaVO(new TurmaVO());
        getListaConsultaCurso().clear();
        setValorConsultaCurso("");
        setMensagemID("msg_entre_prmconsulta");
    }

    public void limparDadosCurso() throws Exception {
        setTurmaVO(new TurmaVO());
        setMensagemID("msg_entre_prmconsulta");
    }

    public void limparDadosFuncionarioPrincipal() {
        getCertificadoCursoExtensaoRelVO().setFuncionarioPrincipalVO(new FuncionarioVO());
    }

    public void limparModalFuncionarioPrincipal() {
        getCertificadoCursoExtensaoRelVO().setFuncionarioPrincipalVO(new FuncionarioVO());
        setValorConsultaFuncionario("");
        setMensagemID("msg_entre_prmconsulta");
    }

    public void limparModalFuncionarioSecundario() {
        getCertificadoCursoExtensaoRelVO().setFuncionarioSecundarioVO(new FuncionarioVO());
        setCampoConsultaFuncionario("");
        getListaConsultaFuncionario().clear();
    }

    public void limparDadosFuncionarioSecundario() {
        getCertificadoCursoExtensaoRelVO().setFuncionarioSecundarioVO(new FuncionarioVO());
    }

    public boolean getIsApresentarCampoCargoFuncionarioPrincipal() {
        return getIsApresentarCampos() && Uteis.isAtributoPreenchido(getCertificadoCursoExtensaoRelVO().getFuncionarioPrincipalVO());
    }

    public boolean getIsApresentarCampoCargoFuncionarioSecundario() {
        return getIsApresentarCampos() && Uteis.isAtributoPreenchido(getCertificadoCursoExtensaoRelVO().getFuncionarioSecundarioVO());
    }

    public List getListaSelectItemFiltros() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("aluno", "Aluno"));
        itens.add(new SelectItem("turma", "Turma"));
        itens.add(new SelectItem("programacaoFormatura", "Programação de Formatura"));
        return itens;
    }

    public List getTipoConsultaComboAluno() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public List getTipoConsultaComboFuncionario() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("cargo", "Cargo"));
        itens.add(new SelectItem("departamento", "Departamento"));

        return itens;
    }

    public List getTipoConsultaComboCurso() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public boolean getIsApresentarCampos() {
        if ((getMatriculaVO().getAluno() != null && getMatriculaVO().getAluno().getCodigo() != 0) || (getIsCursoPreenchido()) || getApresentarProgramacaoFormatura()) {
            return true;
        }
        return false;
    }

    private void montarListaSelectItemUnidadeEnsino() {
        try {
            List resultadoConsulta = consultarUnidadeEnsinoPorNome("");
            setListaSelectItemUnidadeEnsinoVO(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", true));
        } catch (Exception e) {
            ////System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public List getListaConsultaCurso() {
        if (listaConsultaCurso == null) {
            listaConsultaCurso = new ArrayList(0);
        }
        return listaConsultaCurso;
    }

    public void setListaConsultaCurso(List listaConsultaCurso) {
        this.listaConsultaCurso = listaConsultaCurso;
    }

    public String getValorConsultaAluno() {
        if (valorConsultaAluno == null) {
            valorConsultaAluno = "";
        }
        return valorConsultaAluno;
    }

    public void setValorConsultaAluno(String valorConsultaAluno) {
        this.valorConsultaAluno = valorConsultaAluno;
    }

    public String getCampoConsultaAluno() {
        if (campoConsultaAluno == null) {
            campoConsultaAluno = "";
        }
        return campoConsultaAluno;
    }

    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
    }

    public List getListaConsultaAluno() {
        if (listaConsultaAluno == null) {
            listaConsultaAluno = new ArrayList(0);
        }
        return listaConsultaAluno;
    }

    public void setListaConsultaAluno(List listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    public MatriculaVO getMatriculaVO() {
        if (matriculaVO == null) {
            matriculaVO = new MatriculaVO();
        }
        return matriculaVO;
    }

    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
    }

    public List<SelectItem> getSelectItemsCargoFuncionarioPrincipal() {
        if (selectItemsCargoFuncionarioPrincipal == null) {
            selectItemsCargoFuncionarioPrincipal = new ArrayList<SelectItem>();
        }
        return selectItemsCargoFuncionarioPrincipal;
    }

    public void setSelectItemsCargoFuncionarioPrincipal(List<SelectItem> selectItemsCargoFuncionarioPrincipal) {
        this.selectItemsCargoFuncionarioPrincipal = selectItemsCargoFuncionarioPrincipal;
    }

    public List<SelectItem> getSelectItemsCargoFuncionarioSecundario() {
        if (selectItemsCargoFuncionarioSecundario == null) {
            selectItemsCargoFuncionarioSecundario = new ArrayList<SelectItem>();
        }
        return selectItemsCargoFuncionarioSecundario;
    }

    public void setSelectItemsCargoFuncionarioSecundario(List<SelectItem> selectItemsCargoFuncionarioSecundario) {
        this.selectItemsCargoFuncionarioSecundario = selectItemsCargoFuncionarioSecundario;
    }

    public List<FuncionarioVO> getListaConsultaFuncionario() {
        if (listaConsultaFuncionario == null) {
        	listaConsultaFuncionario = new ArrayList<FuncionarioVO>();
        }
    	return listaConsultaFuncionario;
    }

    public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
        this.listaConsultaFuncionario = listaConsultaFuncionario;
    }

    public String getValorConsultaFuncionario() {
        if (valorConsultaFuncionario == null) {
            valorConsultaFuncionario = "";
        }
        return valorConsultaFuncionario;
    }

    public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
        this.valorConsultaFuncionario = valorConsultaFuncionario;
    }

    public String getCampoConsultaFuncionario() {
        if (campoConsultaFuncionario == null) {
            campoConsultaFuncionario = "";
        }
        return campoConsultaFuncionario;
    }

    public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
        this.campoConsultaFuncionario = campoConsultaFuncionario;
    }

    public CertificadoCursoExtensaoRelVO getCertificadoCursoExtensaoRelVO() {
        if (certificadoCursoExtensaoRelVO == null) {
            certificadoCursoExtensaoRelVO = new CertificadoCursoExtensaoRelVO();
        }
        return certificadoCursoExtensaoRelVO;
    }

    public void setCertificadoCursoExtensaoRelVO(CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO) {
        this.certificadoCursoExtensaoRelVO = certificadoCursoExtensaoRelVO;
    }

    public TurmaVO getTurmaVO() {
        if (turmaVO == null) {
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
    }

    public UnidadeEnsinoVO getUnidadeEnsinoVO() {
        if (unidadeEnsinoVO == null) {
            unidadeEnsinoVO = new UnidadeEnsinoVO();
        }
        return unidadeEnsinoVO;
    }

    public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
        this.unidadeEnsinoVO = unidadeEnsinoVO;
    }

    public String getTipoConsultaCurso() {
        return tipoConsultaCurso;
    }

    public void setTipoConsultaCurso(String tipoConsultaCurso) {
        this.tipoConsultaCurso = tipoConsultaCurso;
    }

    public String getCampoConsultaCurso() {
        return campoConsultaCurso;
    }

    public void setCampoConsultaCurso(String campoConsultaCurso) {
        this.campoConsultaCurso = campoConsultaCurso;
    }

    public String getValorConsultaCurso() {
        return valorConsultaCurso;
    }

    public void setValorConsultaCurso(String valorConsultaCurso) {
        this.valorConsultaCurso = valorConsultaCurso;
    }

    public List getListaSelectItemUnidadeEnsinoVO() {
        return listaSelectItemUnidadeEnsinoVO;
    }

    public void setListaSelectItemUnidadeEnsinoVO(List listaSelectItemUnidadeEnsinoVO) {
        this.listaSelectItemUnidadeEnsinoVO = listaSelectItemUnidadeEnsinoVO;
    }

    public List getListaSelectItemTurmaVO() {
        if (listaSelectItemTurmaVO == null) {
            listaSelectItemTurmaVO = new ArrayList(0);
        }
        return listaSelectItemTurmaVO;
    }

    public void setListaSelectItemTurmaVO(List listaSelectItemTurmaVO) {
        this.listaSelectItemTurmaVO = listaSelectItemTurmaVO;
    }

    public String getFiltro() {
        if (filtro == null) {
            filtro = "aluno";
        }
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public boolean getIsFiltrarPorAluno() {
        return getFiltro().equals("aluno");
    }

    public boolean getIsFiltrarPorTurma() {
        return getFiltro().equals("turma");
    }
    
    public boolean getIsFiltrarPorRequerimento() {
    	return getFiltro().equals("requerimento");
    }

    public boolean getIsMatriculaPreenchida() {
        if (getMatriculaVO() != null && !getMatriculaVO().getMatricula().equals("") && (getFiltro().equals("aluno") || getFiltro().equals("requerimento"))) {
            return true;
        }
        return false;
    }

    public boolean getIsTurmaPreenchida() {
        if (getTurmaVO() != null && getTurmaVO().getCodigo() != 0 && getFiltro().equals("turma")) {
            return true;
        }
        return false;
    }

    public boolean getIsPossuiUnidadeEnsinoLogado() throws Exception {
        if (getUnidadeEnsinoLogado() != null && getUnidadeEnsinoLogado().getCodigo() != 0) {
            return true;
        }
        return false;
    }

    public boolean getIsCursoPreenchido() {
        if (getTurmaVO().getCurso() != null && getTurmaVO().getCurso().getCodigo() != 0) {
            return true;
        }
        return false;
    }
    
    
    public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = "";
		}
		return tipoLayout;
	}
	
	public String getDesignIReportRelatorio(){
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getTipoLayout() + ".jrxml");
			
	}

	public PeriodoLetivoVO getPeriodoLetivoVO() {
		if (periodoLetivoVO == null) {
			periodoLetivoVO = new PeriodoLetivoVO();
		}
		return periodoLetivoVO;
	}



	public void setPeriodoLetivoVO(PeriodoLetivoVO periodoLetivoVO) {
		this.periodoLetivoVO = periodoLetivoVO;
	}



	public List getListaSelectItemPeriodoLetivo() {
		if (listaSelectItemPeriodoLetivo == null) {
			listaSelectItemPeriodoLetivo = new ArrayList(0);
		}
		return listaSelectItemPeriodoLetivo;
	}



	public void setListaSelectItemPeriodoLetivo(List listaSelectItemPeriodoLetivo) {
		this.listaSelectItemPeriodoLetivo = listaSelectItemPeriodoLetivo;
	}



	public List<CertificadoCursoExtensaoRelVO> getListaCertificadoErro() {
		if (listaCertificadoErro == null) {
			listaCertificadoErro = new ArrayList<CertificadoCursoExtensaoRelVO>(0);
		}
		return listaCertificadoErro;
	}



	public void setListaCertificadoErro(List<CertificadoCursoExtensaoRelVO> listaCertificadoErro) {
		this.listaCertificadoErro = listaCertificadoErro;
	}

	public String getExibirModalCertificadoErro() {
		if (!getListaCertificadoErro().isEmpty()) {
			return "RichFaces.$('panelCertificadoErro').show()";
    	} 
		return "";
	}

	public Integer getTextoPadraoDeclaracao() {
		if (textoPadraoDeclaracao == null) {
			textoPadraoDeclaracao = 0;
		}
		return textoPadraoDeclaracao;
	}



	public void setTextoPadraoDeclaracao(Integer textoPadraoDeclaracao) {
		this.textoPadraoDeclaracao = textoPadraoDeclaracao;
	}

	public List getListaSelectItemTipoTextoPadrao() {
		if (listaSelectItemTipoTextoPadrao == null) {
			listaSelectItemTipoTextoPadrao = new ArrayList();
		}
		return listaSelectItemTipoTextoPadrao;
	}

	public void setListaSelectItemTipoTextoPadrao(List listaSelectItemTipoTextoPadrao) {
		this.listaSelectItemTipoTextoPadrao = listaSelectItemTipoTextoPadrao;
	}	
	
    public void consultarDisciplinaPorPeriodoLetivo() throws Exception {
        try {
        	List<GradeDisciplinaVO> listaGradeDisciplinaVOs = getFacadeFactory().getGradeDisciplinaFacade().consultaRapidaGradeDisciplinaPorPeriodoLetivo(getPeriodoLetivoVO().getCodigo(), false, getUsuarioLogado());
            setListaSelectItemsDisciplinaVOs(montarComboDisciplina(listaGradeDisciplinaVOs));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public List<SelectItem> montarComboDisciplina(List<GradeDisciplinaVO> listaGradeDisciplinaVOs) throws Exception {
        if (listaGradeDisciplinaVOs != null && !listaGradeDisciplinaVOs.isEmpty()) {
            List<SelectItem> selectItems = new ArrayList<SelectItem>();
            selectItems.add(new SelectItem("", ""));
            for (GradeDisciplinaVO gradeDisciplinaVO : listaGradeDisciplinaVOs) {
                selectItems.add(new SelectItem(gradeDisciplinaVO.getCodigo(), gradeDisciplinaVO.getDisciplina().getNome()));
            }
            return selectItems;
        } 
        return null;
    }

	public List<SelectItem> getListaSelectItemsDisciplinaVOs() {
		if (listaSelectItemsDisciplinaVOs == null) {
			listaSelectItemsDisciplinaVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemsDisciplinaVOs;
	}

	public void setListaSelectItemsDisciplinaVOs(List<SelectItem> listaSelectItemsDisciplinaVOs) {
		this.listaSelectItemsDisciplinaVOs = listaSelectItemsDisciplinaVOs;
	}

	/**
	 * @return the gradeDisciplinaVO
	 */
	public GradeDisciplinaVO getGradeDisciplinaVO() {
		if (gradeDisciplinaVO == null) {
			gradeDisciplinaVO = new GradeDisciplinaVO();
		}
		return gradeDisciplinaVO;
	}

	/**
	 * @param gradeDisciplinaVO the gradeDisciplinaVO to set
	 */
	public void setGradeDisciplinaVO(GradeDisciplinaVO gradeDisciplinaVO) {
		this.gradeDisciplinaVO = gradeDisciplinaVO;
	}
	
	public Boolean getApresentarComboBoxDisciplina() {
		return getIsMatriculaPreenchida() && getTipoLayout().equals("TextoPadrao") && !getPeriodoLetivoVO().getCodigo().equals(0);
	}

	/**
	 * @return the tipoDisciplinaPeriodoSelecionado
	 */
	public String getTipoDisciplinaPeriodoSelecionado() {
		if (tipoDisciplinaPeriodoSelecionado == null) {
			tipoDisciplinaPeriodoSelecionado = "DisciplinaAtePeriodoSelecionado";
		}
		return tipoDisciplinaPeriodoSelecionado;
	}

	/**
	 * @param tipoDisciplinaPeriodoSelecionado the tipoDisciplinaPeriodoSelecionado to set
	 */
	public void setTipoDisciplinaPeriodoSelecionado(String tipoDisciplinaPeriodoSelecionado) {
		this.tipoDisciplinaPeriodoSelecionado = tipoDisciplinaPeriodoSelecionado;
	}
	
	public File getArquivoZip() {
		return arquivoZip;
	}

	public void setArquivoZip(File arquivoZip) {
		this.arquivoZip = arquivoZip;
	}
	
	public List<File> getListaArquivos() {
		if(listaArquivos == null ) {
			listaArquivos = new ArrayList<File>();
		}
	return listaArquivos;
	}

	public void setListaArquivos(List<File> listaArquivos) {
		this.listaArquivos = listaArquivos;
	}	

	public String getDownloadCertificadosCompactadas() {
		try {
			if (getArquivoZip() != null && !getArquivoZip().getAbsolutePath().equals("")) {
				context().getExternalContext().getSessionMap().put("nomeArquivo", getArquivoZip().getName());
				context().getExternalContext().getSessionMap().put("pastaBaseArquivo", getArquivoZip().getParent());
				context().getExternalContext().getSessionMap().put("deletarArquivo", true);
				return "location.href='../../../DownloadSV'";
			} else {
				return "";
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}
	
	public void validarImpressaoContratoExistente(Boolean gerarNovoContrato ) {
		try {
			if(gerarNovoContrato){
				setGerarNovoArquivoAssinado(true);
				List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs = new ArrayList<CertificadoCursoExtensaoRelVO>(0);
				montarRelatorioPorLayoutTextoPadrao(certificadoCursoExtensaoRelVOs);
			}else if(Uteis.isAtributoPreenchido(getCaminhoRelatorio()) ){
				setFazerDownload(true);
				setImpressaoContratoExistente(false);
			}	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String getDownload() {
		if (getFazerDownload()) {
			try {
				HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
				String caminho = request.getRequestURL().toString().replace(request.getRequestURI().toString(), "") + request.getContextPath() + "/";
				return "location.href='" + caminho + "DownloadRelatorioSV?relatorio=" + getCaminhoRelatorio() + "'";
			} catch (Exception ex) {
				Logger.getLogger(SuperControleRelatorio.class.getName()).log(Level.SEVERE, null, ex);
			}
		}else if(getImpressaoContratoExistente()){
			return "RichFaces.$('panelImpressaoContratoExistente').show()";
		}
		return "";
	}


	public RequerimentoVO getRequerimentoVO() {
		if(requerimentoVO == null){
			requerimentoVO = new RequerimentoVO();
		}
		return requerimentoVO;
	}

	public void setRequerimentoVO(RequerimentoVO requerimentoVO) {
		this.requerimentoVO = requerimentoVO;
	}

	public Boolean getGerarNovoArquivoAssinado() {
		if (gerarNovoArquivoAssinado == null) { 
			gerarNovoArquivoAssinado = false;
		}
		return gerarNovoArquivoAssinado;
	}

	public void setGerarNovoArquivoAssinado(Boolean gerarNovoArquivoAssinado) {
		this.gerarNovoArquivoAssinado = gerarNovoArquivoAssinado;
	}
	
	public Boolean getImpressaoContratoExistente() {
		if(impressaoContratoExistente == null){
			impressaoContratoExistente = false;
		}
		return impressaoContratoExistente;
	}

	public void setImpressaoContratoExistente(Boolean impressaoContratoExistente) {
		this.impressaoContratoExistente = impressaoContratoExistente;
	}

	public void setTipoFiltroDisciplina(List<SelectItem> tipoFiltroDisciplina) {
		this.tipoFiltroDisciplina = tipoFiltroDisciplina;
	}

	public boolean isPermitirExibirRegraEmissao() {
		return permitirExibirRegraEmissao;
	}

	public void setPermitirExibirRegraEmissao(boolean permitirExibirRegraEmissao) {
		this.permitirExibirRegraEmissao = permitirExibirRegraEmissao;
	}
	
	public boolean isAssinarDigitalmente() {
		return assinarDigitalmente;
	}

	public void setAssinarDigitalmente(boolean assinarDigitalmente) {
		this.assinarDigitalmente = assinarDigitalmente;
	}

	public Boolean getTrazerTodasSituacoesAprovadas() {
		if (trazerTodasSituacoesAprovadas == null) {
			trazerTodasSituacoesAprovadas = false;
		}
		return trazerTodasSituacoesAprovadas;
	}

	public void setTrazerTodasSituacoesAprovadas(Boolean trazerTodasSituacoesAprovadas) {
		this.trazerTodasSituacoesAprovadas = trazerTodasSituacoesAprovadas;
	}
	
	
	public String getCampoConsultaProgramacaoFormatura() {
    	if(campoConsultaProgramacaoFormatura == null) {
    		campoConsultaProgramacaoFormatura = "codigo";
    	}
		return campoConsultaProgramacaoFormatura;
	}

	public void setCampoConsultaProgramacaoFormatura(String campoConsultaProgramacaoFormatura) {
		this.campoConsultaProgramacaoFormatura = campoConsultaProgramacaoFormatura;
	}
	
	
	public Boolean getMostrarSegundoCampoProgramacaoFormatura() {
        if (mostrarSegundoCampoProgramacaoFormatura == null) {
        	mostrarSegundoCampoProgramacaoFormatura = false;
        }
        return mostrarSegundoCampoProgramacaoFormatura;
    }

    /**
     * @param mostrarSegundoCampoProgramacaoFormatura
     *            the mostrarSegundoCampoProgramacaoFormatura to set
     */
    public void setMostrarSegundoCampoProgramacaoFormatura(Boolean mostrarSegundoCampoProgramacaoFormatura) {
        this.mostrarSegundoCampoProgramacaoFormatura = mostrarSegundoCampoProgramacaoFormatura;
    }
    
    
    public String getValorConsultaProgramacaoFormatura() {
    	if(valorConsultaProgramacaoFormatura == null) {
    		valorConsultaProgramacaoFormatura = "";
    	}
		return valorConsultaProgramacaoFormatura;
	}

	public void setValorConsultaProgramacaoFormatura(String valorConsultaProgramacaoFormatura) {
		this.valorConsultaProgramacaoFormatura = valorConsultaProgramacaoFormatura;
	}
    
	
	/**
     * @return the valorConsultaDataInicioProgramacaoFormatura
     */
    public Date getValorConsultaDataInicioProgramacaoFormatura() {
        return valorConsultaDataInicioProgramacaoFormatura;
    }

    /**
     * @param valorConsultaDataInicioProgramacaoFormatura
     *            the valorConsultaDataInicioProgramacaoFormatura to set
     */
    public void setValorConsultaDataInicioProgramacaoFormatura(Date valorConsultaDataInicioProgramacaoFormatura) {
        this.valorConsultaDataInicioProgramacaoFormatura = valorConsultaDataInicioProgramacaoFormatura;
    }

    /**
     * @return the valorConsultaDataFinalProgramacaoFormatura
     */
    public Date getValorConsultaDataFinalProgramacaoFormatura() {
        return valorConsultaDataFinalProgramacaoFormatura;
    }

    /**
     * @param valorConsultaDataFinalProgramacaoFormatura
     *            the valorConsultaDataFinalProgramacaoFormatura to set
     */
    public void setValorConsultaDataFinalProgramacaoFormatura(Date valorConsultaDataFinalProgramacaoFormatura) {
        this.valorConsultaDataFinalProgramacaoFormatura = valorConsultaDataFinalProgramacaoFormatura;
    }
    
    public String getFiltroAlunosPresentesColacaoGrau() {
		if(filtroAlunosPresentesColacaoGrau == null) {
			filtroAlunosPresentesColacaoGrau = "AM";
		}
	
		return filtroAlunosPresentesColacaoGrau;
	}

	public void setFiltroAlunosPresentesColacaoGrau(String filtroAlunosPresentesColacaoGrau) {
		this.filtroAlunosPresentesColacaoGrau = filtroAlunosPresentesColacaoGrau;
	}
	
	public List<ProgramacaoFormaturaVO> getListaConsultaProgramacaoFormatura() {
		if (listaConsultaProgramacaoFormatura == null) {
			listaConsultaProgramacaoFormatura = new ArrayList<ProgramacaoFormaturaVO>(0);
		}
		return listaConsultaProgramacaoFormatura;
	}

	public void setListaConsultaProgramacaoFormatura(List<ProgramacaoFormaturaVO> listaConsultaProgramacaoFormatura) {
		this.listaConsultaProgramacaoFormatura = listaConsultaProgramacaoFormatura;
	} 
	
	public Boolean getApresentarProgramacaoFormatura() {
		if(apresentarProgramacaoFormatura == null ) {
			apresentarProgramacaoFormatura = Boolean.FALSE;
		}
		return apresentarProgramacaoFormatura;
	}

	public void setApresentarProgramacaoFormatura(Boolean apresentarProgramacaoFormatura) {
		this.apresentarProgramacaoFormatura = apresentarProgramacaoFormatura;
	}
	
	

	public ProgramacaoFormaturaVO getProgramacaoFormaturaVO() {
		if (programacaoFormaturaVO == null) {
			programacaoFormaturaVO = new ProgramacaoFormaturaVO();
		}
		return programacaoFormaturaVO;
	}

	public void setProgramacaoFormaturaVO(ProgramacaoFormaturaVO programacaoFormaturaVO) {
		this.programacaoFormaturaVO = programacaoFormaturaVO;
	}
	
	 public boolean getIsFiltrarPorProgramacaoFormatura() {
	        return getFiltro().equals("programacaoFormatura");
	    }

    
    
    public void mostrarSegundoCampoProgramacaoFormatura() {
        if (getCampoConsultaProgramacaoFormatura().equals("periodoRequerimento") || getCampoConsultaProgramacaoFormatura().equals("periodoColacaoGrau")
                || getCampoConsultaProgramacaoFormatura().equals("periodoCadastro")) {
        	setMostrarSegundoCampoProgramacaoFormatura(true);
        } else {
        	setMostrarSegundoCampoProgramacaoFormatura(false);
        }
        setValorConsultaProgramacaoFormatura("");
        getListaConsultaProgramacaoFormatura().clear();
    }
    
	
	 public void selecionarProgramacaoFormatura() throws Exception {		
	        ProgramacaoFormaturaVO obj = (ProgramacaoFormaturaVO) context().getExternalContext().getRequestMap().get("programacaoFormaturaItens");
	        obj.setNovoObj(Boolean.FALSE);
	        setProgramacaoFormaturaVO(getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
	        setUnidadeEnsinoVO(getProgramacaoFormaturaVO().getProgramacaoFormaturaUnidadeEnsinoVOs().get(0).getUnidadeEnsinoVO());		
			setApresentarProgramacaoFormatura(true);
			setValorConsultaProgramacaoFormatura("");
			setCampoConsultaProgramacaoFormatura("");
	        getListaConsultaProgramacaoFormatura().clear();
	        getListaCertificadoCursoExtensaoRelVOGerar().clear();
	        setApresentarResultadoConsultaMatriculaGerarCertificado(Boolean.FALSE);
	        montarListaCertificadoCursoExencaoPorProgramacaoFormatura(getProgramacaoFormaturaVO());
	        
	    }  
	
	 
	 private void montarListaCertificadoCursoExencaoPorProgramacaoFormatura(ProgramacaoFormaturaVO programacaoFormaturaVO) {
		 if(!programacaoFormaturaVO.getProgramacaoFormaturaAlunoVOs().isEmpty()) {
		    setApresentarResultadoConsultaMatriculaGerarCertificado(Boolean.TRUE);
            for(ProgramacaoFormaturaAlunoVO obj : programacaoFormaturaVO.getProgramacaoFormaturaAlunoVOs()) {
            	CertificadoCursoExtensaoRelVO certificado = new CertificadoCursoExtensaoRelVO();
            	certificado.setMatriculaVO(obj.getMatricula());
            	getListaCertificadoCursoExtensaoRelVOGerar().add(certificado);
            }
		 }
		
	}

	public List getTipoConsultaComboProgramacaoFormatura() {
	        List itens = new ArrayList(0);
	        itens.add(new SelectItem("codigo", "Código"));
	        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
	        itens.add(new SelectItem("nomeCurso", "Curso"));
	        itens.add(new SelectItem("nomeTurno", "Turno"));
	        itens.add(new SelectItem("identificadorTurmaTurma", "Turma"));
	        itens.add(new SelectItem("matriculaMatricula", "Matricula"));
	        itens.add(new SelectItem("periodoRequerimento", "Período Requerimento"));
	        itens.add(new SelectItem("periodoColacaoGrau", "Período Colação Grau"));
	        itens.add(new SelectItem("periodoCadastro", "Período Cadastro"));
	        itens.add(new SelectItem("nomeUsuario", "Responsável Cadastro"));
	        return itens;
	    }
	 
	  public List getComboFiltroAlunosPresentesColacaoGrau() {
	        List itens = new ArrayList(0);
	        itens.add(new SelectItem("SI", "Presentes"));
	        itens.add(new SelectItem("NO", "Ausentes"));
	        itens.add(new SelectItem("NI", "Não Informado"));
	        itens.add(new SelectItem("AM", "Ambos"));
	        return itens;
	    }
	  
	  public String consultarProgramacaoFormatura() {
	        try {
	            super.consultar();
	            List objs = new ArrayList(0);
				

		            if (getCampoConsultaProgramacaoFormatura().equals("codigo")) {
		                if (getValorConsultaProgramacaoFormatura().equals("")) {
		                    setValorConsultaProgramacaoFormatura("0");
		                }
		                if (getValorConsultaProgramacaoFormatura().trim() != null || !getValorConsultaProgramacaoFormatura().trim().isEmpty()) {
		                    Uteis.validarSomenteNumeroString(getValorConsultaProgramacaoFormatura().trim());
		                }
		                int valorInt = Integer.parseInt(getValorConsultaProgramacaoFormatura());
		                if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {               	
		                	objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		                }else {
		                	objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorCodigoFiltroAlunosPresentesColacaoGrau(new Integer(valorInt), getFiltroAlunosPresentesColacaoGrau(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		                }
		            }
		            if (getCampoConsultaProgramacaoFormatura().equals("nomeUnidadeEnsino")) {
		            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {
		            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorProgramacaoUnidadeEnsino(getValorConsultaProgramacaoFormatura(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		            	}else {
		            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeUnidadeEnsinoFiltroAlunosPresentesColacaoGrau(getValorConsultaProgramacaoFormatura(), getFiltroAlunosPresentesColacaoGrau(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		            	}
		            }
		            if (getCampoConsultaProgramacaoFormatura().equals("nomeCurso")) {
		            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {            
		            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeCurso(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), false,
		                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		            	}else {
		            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeCursoFiltroAlunosPresentesColacaoGrau(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), false,
		                            Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()); 
		            	}
		            }
		            if (getCampoConsultaProgramacaoFormatura().equals("nomeTurno")) {
		            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {            
		            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeTurno(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), false,
		                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		            	}else {
		            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeTurnoFiltroAlunosPresentesColacaoGrau(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), false,
		                            Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()); 
		            	}
		            }
		            if (getCampoConsultaProgramacaoFormatura().equals("identificadorTurmaTurma")) {
		            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {
		            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorIdentificadorTurmaTurma(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), false,
		                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		            	}else {
		            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorIdentificadorTurmaFiltroAlunosPresentesColacaoGrau(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), false,
		                            Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()); 
		            	}
		            }
		            if (getCampoConsultaProgramacaoFormatura().equals("matriculaMatricula")) {
		            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {
		            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorMatriculaMatricula(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), false,
		                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		            	}else {
		                	objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorMatriculaMatriculaFiltroAlunosPresentesColacaoGrau(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), false,
		                            Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()); 
		            	}
		            }
		            if (getCampoConsultaProgramacaoFormatura().equals("nomeUsuario")) {
		            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {            
		            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeUsuario(getValorConsultaProgramacaoFormatura(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		            	}else {
		            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeUsuarioFiltroAlunosPresentesColacaoGrau(getValorConsultaProgramacaoFormatura(), getFiltroAlunosPresentesColacaoGrau(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()); 
		            	}
		            }
		            if (getCampoConsultaProgramacaoFormatura().equals("periodoRequerimento") || getCampoConsultaProgramacaoFormatura().equals("periodoColacaoGrau")
		                    || getCampoConsultaProgramacaoFormatura().equals("periodoCadastro")) {
		                objs = validarDataConsultaProgramacaoFormatura(objs);
		            }
		            setListaConsultaProgramacaoFormatura(objs);	           
		            setMensagemID("msg_dados_consultados");
		            return "consultar";

	        } catch (Exception e) {
	        	setListaConsultaProgramacaoFormatura(new ArrayList(0));
	            setMensagemDetalhada("msg_erro", e.getMessage());
	            return "consultar";
	        }
	    }
	    
	  
	  public boolean getApresentarResultadoConsultaProgramacaoFormatura() {
			if (this.getListaConsultaProgramacaoFormatura() == null || this.getListaConsultaProgramacaoFormatura().size() == 0) {
				return false;
			}
			return true;
		}
	  
	  public List validarDataConsultaProgramacaoFormatura(List objs) throws Exception {
	        if (getValorConsultaDataFinalProgramacaoFormatura() != null && getValorConsultaDataInicioProgramacaoFormatura() != null) {
	            if (getCampoConsultaProgramacaoFormatura().equals("periodoRequerimento")) {
	            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {          		            	
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataRequerimento(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
	                        Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            	}else {
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataRequerimentoFiltroAlunosPresentesColacaoGrau(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
	                            Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            	}
	            }
	            if (getCampoConsultaProgramacaoFormatura().equals("periodoColacaoGrau")) {
	            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataColacaoGrau(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
	                        Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            	}else {
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataColacaoGrauFiltroAlunosPresentesColacaoGrau(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
	                            Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            	}
	            }
	            if (getCampoConsultaProgramacaoFormatura().equals("periodoCadastro")) {
	            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataCadastro(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
	                        Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            	}else {
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataCadastroFiltroAlunosPresentesColacaoGrau(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
	                            Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            	}
	            }
	        } else {
	            throw new ConsistirException("Por favor digite uma data válida.");
	        }

	        return objs;
	    }
	  
	  
		public void limparCampoProgramacaoFormatura() {
			removerObjetoMemoria(this);
			setApresentarProgramacaoFormatura(Boolean.FALSE);
			limparMensagem();
			setMensagemID("msg_entre_dados");
		}
		
		
		
		
		
		
		public Boolean getApresentarListaErros() {
			if(apresentarListaErros == null ) {
				apresentarListaErros = Boolean.FALSE;
			}
			return apresentarListaErros;
		}

		public void setApresentarListaErros(Boolean apresentarListaErros) {
			this.apresentarListaErros = apresentarListaErros;
		}

		public List<CertificadoCursoExtensaoRelVO> getListaCertificadoCursoExtensaoRelVOErros() {
			if(listaCertificadoCursoExtensaoRelVOErros ==null ) {
				listaCertificadoCursoExtensaoRelVOErros = new ArrayList<CertificadoCursoExtensaoRelVO>(0);
			}
			return listaCertificadoCursoExtensaoRelVOErros;
		}

		public void setListaCertificadoCursoExtensaoRelVOErros(
				List<CertificadoCursoExtensaoRelVO> listaCertificadoCursoExtensaoRelVOErros) {
			this.listaCertificadoCursoExtensaoRelVOErros = listaCertificadoCursoExtensaoRelVOErros;
		}

		public ProgressBarVO getProgressBarVO() {
			return progressBarVO;
		}

		public void setProgressBarVO(ProgressBarVO progressBarVO) {
			this.progressBarVO = progressBarVO;
		}

		public TextoPadraoDeclaracaoVO getTextoPadraoDeclaracaoVO() {
			if(textoPadraoDeclaracaoVO == null ) {
				textoPadraoDeclaracaoVO = new TextoPadraoDeclaracaoVO();
			}
			return textoPadraoDeclaracaoVO;
		}

		public void setTextoPadraoDeclaracaoVO(TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO) {
			this.textoPadraoDeclaracaoVO = textoPadraoDeclaracaoVO;
		}

		
		public List<CertificadoCursoExtensaoRelVO> getListaCertificadoCursoExtensaoRelVOGerar() {
			if(listaCertificadoCursoExtensaoRelVOGerar == null ) {
				listaCertificadoCursoExtensaoRelVOGerar = new ArrayList<CertificadoCursoExtensaoRelVO>(0);
			}
			return listaCertificadoCursoExtensaoRelVOGerar;
		}

		public void setListaCertificadoCursoExtensaoRelVOGerar(List<CertificadoCursoExtensaoRelVO> listaCertificadoCursoExtensaoRelVOGerar) {
			this.listaCertificadoCursoExtensaoRelVOGerar = listaCertificadoCursoExtensaoRelVOGerar;
		}
		
		
		public PlanoEnsinoVO getPlanoEnsinoVO() {
			if(planoEnsinoVO == null ) {
				planoEnsinoVO = new PlanoEnsinoVO();
			}
			return planoEnsinoVO;
		}

		public void setPlanoEnsinoVO(PlanoEnsinoVO planoEnsinoVO) {
			this.planoEnsinoVO = planoEnsinoVO;
		}
		
		 public Boolean getApresentarResultadoConsultaMatriculaGerarCertificado() {
			 if(apresentarResultadoConsultaMatriculaGerarCertificado == null ) {
				 apresentarResultadoConsultaMatriculaGerarCertificado = Boolean.FALSE;
			 }
				return apresentarResultadoConsultaMatriculaGerarCertificado;
			}

			public void setApresentarResultadoConsultaMatriculaGerarCertificado(
					Boolean apresentarResultadoConsultaMatriculaGerarCertificado) {
				this.apresentarResultadoConsultaMatriculaGerarCertificado = apresentarResultadoConsultaMatriculaGerarCertificado;
			}

		
		public void imprimirPDFZipado() {			
			try {
				 registrarAtividadeUsuario(getUsuarioLogado(), "CertificadoCursoExtensaoRelControle", "Inicializando Geração de Relátorio Certificado Curso de Extensão", "Emitindo Relatório");
				
				 CertificadoCursoExtensaoRel.validarDados(getMatriculaVO(), getFiltro(), getTipoLayout(), getTurmaVO(), getRequerimentoVO(), getCertificadoCursoExtensaoRelVO(),getUnidadeEnsinoVO(),getProgramacaoFormaturaVO());
				    setProgressBarVO(new ProgressBarVO());			
					getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
					getProgressBarVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
					getProgressBarVO().setUnidadeEnsinoVO(getUnidadeEnsinoLogado());
					getProgressBarVO().setCaminhoWebRelatorio(getCaminhoPastaWeb());			
					getProgressBarVO().resetar();
					getProgressBarVO().iniciar(0l, (2), "Carregando Matriculas", true, this,	"montarRelatorioLayoutCertificadoCursoExtensaoZipado");									
				    registrarAtividadeUsuario(getUsuarioLogado(), "CertificadoCursoExtensaoRelControle", "Finalizando Geração de Relátorio Certificado Curso de Extensão", "Emitindo Relatório");
				} catch (Exception e) {  
				 setArquivoZip(null);
				 setFazerDownload(Boolean.FALSE);
				 setMensagemDetalhada("msg_erro", e.getMessage());
				}
		}

		public void montarRelatorioLayoutCertificadoCursoExtensaoZipado() throws Exception {	
			getListaCertificadoCursoExtensaoRelVOErros().clear();
			setApresentarListaErros(Boolean.FALSE);
			boolean isTextoPadrao  =  getTipoLayout().equals("TextoPadrao");
			TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO =   null ;	
			getListaArquivos().clear();
			if(isTextoPadrao) {
				textoPadraoDeclaracaoVO = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(textoPadraoDeclaracao, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				setGerarNovoArquivoAssinado(textoPadraoDeclaracaoVO.getAssinarDigitalmenteTextoPadrao());				
			 }

		   List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs = getFacadeFactory().getCertificadoCursoExtensaoRelFacade().realizarMontagemDadosRelatorioLayoutCertificadoCursoExtensao(
				        getListaCertificadoCursoExtensaoRelVOErros(),
						getCertificadoCursoExtensaoRelVO(),
						getMatriculaVO(), 
						getPeriodoLetivoVO(),
						getTurmaVO(),
						getTipoLayout(), 
						getFiltro(), 
						getConfiguracaoFinanceiroPadraoSistema(),
						getTipoDisciplinaPeriodoSelecionado().equals("DisciplinaDoPeriodoSelecionado"), 
						getGradeDisciplinaVO()	,						
						getProgressBarVO().getUsuarioVO(),
						getTrazerTodasSituacoesAprovadas(),
						getProgramacaoFormaturaVO(),
						getUnidadeEnsinoVO(),
						getPlanoEnsinoVO(),
						getListaCertificadoCursoExtensaoRelVOGerar());
		   
		   
		      getProgressBarVO().setMaxValue(certificadoCursoExtensaoRelVOs.size());
			
			 if (certificadoCursoExtensaoRelVOs != null && !certificadoCursoExtensaoRelVOs.isEmpty()) {	
				 
				 for(CertificadoCursoExtensaoRelVO obj : certificadoCursoExtensaoRelVOs) {
						try {
						 getProgressBarVO().setStatus("Processando Matricula " + (getProgressBarVO().getProgresso().equals(0L)  ? "1" : getProgressBarVO().getProgresso())+ " de " + (getProgressBarVO().getMaxValue() ) + " ");
						 List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOsAux = new ArrayList<CertificadoCursoExtensaoRelVO>(0);
						 certificadoCursoExtensaoRelVOsAux.add(obj);
						 
						 if (isTextoPadrao) {
							    List<File> listaArquivos = new  ArrayList<File>(0); 
							    getFacadeFactory().getCertificadoCursoExtensaoRelFacade().realizarMontagemRelatorioPorTextoPadrao(
							    		certificadoCursoExtensaoRelVOsAux,
							    		listaArquivos, 
							    		textoPadraoDeclaracaoVO, 
							    		getPlanoEnsinoVO(),
							    		getGradeDisciplinaVO(), 
							    		getRequerimentoVO(), 
							    		getConfiguracaoGeralPadraoSistema(),
							    		getGerarNovoArquivoAssinado(), 
							    		true,
							    		getProgressBarVO().getUsuarioVO());
							    
							  	getListaArquivos().add(listaArquivos.get(0));
							   
	
						 } else {				 
				
							   executaGeracaoRelatorio(
									     certificadoCursoExtensaoRelVOsAux,
									     "CERTIFICADO CURSO DE EXTENSÃO",
									     getDesignIReportRelatorio(),getProgressBarVO().getUsuarioVO() ,true);		
							   
								String arquivoOrigem = getCaminhoRelatorio().contains(UteisJSF.getCaminhoWeb() + "relatorio/") ?  getCaminhoRelatorio() : (getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + getCaminhoRelatorio());		
							    getListaArquivos().add(new File(arquivoOrigem));					      
						 }						 
							
						}catch(Exception e ) {	
							obj.setPossuiErro(Boolean.TRUE);
							obj.setMotivoErro(e.getMessage());
							getListaCertificadoCursoExtensaoRelVOErros().add(obj);
							e.getMessage();
					   }finally {
						 getProgressBarVO().incrementar();
							
					 } 
				 }
				 persistirDadosPadroes();
				 montarListaSelectItemUnidadeEnsino();				
				 executarValidacaoParaQualLayoutImprimirRelatorio(textoPadraoDeclaracaoVO,"");		
				
				 			
			 }else {
			    setMensagemID("msg_relatorio_sem_dados");			   
			    setArquivoZip(null);
			 }			 
			 if(!getListaCertificadoCursoExtensaoRelVOErros().isEmpty()) {
				setApresentarListaErros(Boolean.TRUE);
			 }
			 setVisualizarCertificado(false);
			 setFazerDownload(Boolean.FALSE);
			 getListaArquivos().clear();			
		     getProgressBarVO().incrementar();
			 getProgressBarVO().setForcarEncerramento(true);			
		}

		
		public void removerCertificacaoCursoGerar() {
			
			CertificadoCursoExtensaoRelVO obj = (CertificadoCursoExtensaoRelVO) context().getExternalContext().getRequestMap().get("itens");
			Iterator it = getListaCertificadoCursoExtensaoRelVOGerar().iterator();
			while(it.hasNext()) {
				CertificadoCursoExtensaoRelVO exp = (CertificadoCursoExtensaoRelVO) it.next();
				if(exp.getMatriculaVO().getMatricula().equals(obj.getMatriculaVO().getMatricula())) {
					it.remove();
				}
				
			}
			
			
		}
		
		
		
		public Boolean getPainelALunosGerarErroAberto() {
			if(painelALunosGerarErroAberto == null ) {
				painelALunosGerarErroAberto = Boolean.FALSE; 
			}
			return painelALunosGerarErroAberto;
		}

		public void setPainelALunosGerarErroAberto(Boolean painelALunosGerarErroAberto) {
			this.painelALunosGerarErroAberto = painelALunosGerarErroAberto;
		}
		
		public Boolean getPainelALunosGerarAberto() {
			if(painelALunosGerarAberto == null ) {
				painelALunosGerarAberto = Boolean.TRUE;
			}
			return painelALunosGerarAberto;
		}

		public void setPainelALunosGerarAberto(Boolean painelALunosGerarAberto) {
			this.painelALunosGerarAberto = painelALunosGerarAberto;
		}
		
		
		public void selecionarTurma() {
	        TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");	                
	        setTurmaVO(obj);
	        setUnidadeEnsinoVO( getTurmaVO().getUnidadeEnsino());
	        setPeriodoLetivoVO(getTurmaVO().getPeridoLetivo());
	        getListaCertificadoCursoExtensaoRelVOGerar().clear();
	        getListaCertificadoCursoExtensaoRelVOErros().clear();
	        setApresentarResultadoConsultaMatriculaGerarCertificado(Boolean.FALSE);
	        montarListaCertificadoCursoExencaoPorTurma(getTurmaVO());
	    }

		
	   

		public void limparDadosTurma() {
	    	 setTurmaVO(new TurmaVO());
	    }
		
		public void limparDadosProgramacaoFormatura() {
			 setApresentarProgramacaoFormatura(Boolean.FALSE);
			 setProgramacaoFormaturaVO(new ProgramacaoFormaturaVO());
			
	    }
	
	    
	    public String consultarTurma() {
	        try {
	            super.consultar();
	            List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
	            if (getCampoConsultaTurma().equals("identificadorTurma")) {
	                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
	            }

	            if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
	                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false,
	                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
	            }

	            if (getCampoConsultaTurma().equals("nomeCurso")) {
	                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
	            }

	            if (getCampoConsultaTurma().equals("nomeTurno")) {
	                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false,
	                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
	            }
	            setListaConsultaTurma(objs);
	            setMensagemID("msg_dados_consultados");
	            return "consultar";
	        } catch (Exception e) {
	            setListaConsulta(new ArrayList<TurmaVO>(0));
	            setMensagemDetalhada("msg_erro", e.getMessage());
	            return "consultar";
	        }

	    }
	    
	    public String getValorConsultaTurma() {
	        if (valorConsultaTurma == null) {
	            valorConsultaTurma = "";
	        }
	        return valorConsultaTurma;
	    }

	    public void setValorConsultaTurma(String valorConsultaTurma) {
	        this.valorConsultaTurma = valorConsultaTurma;
	    }

	    public String getCampoConsultaTurma() {
	        if (campoConsultaTurma == null) {
	            campoConsultaTurma = "";
	        }
	        return campoConsultaTurma;
	    }

	    public void setCampoConsultaTurma(String campoConsultaTurma) {
	        this.campoConsultaTurma = campoConsultaTurma;
	    }

	    public List<TurmaVO> getListaConsultaTurma() {
	        if (listaConsultaTurma == null) {
	            listaConsultaTurma = new ArrayList<TurmaVO>(0);
	        }
	        return listaConsultaTurma;
	    }

	    public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
	        this.listaConsultaTurma = listaConsultaTurma;
	    }
	    
	    public List<SelectItem> getTipoConsultaCombo() {
	        List<SelectItem> itens = new ArrayList<SelectItem>(0);
	        itens.add(new SelectItem("identificadorTurma", "Identificador"));
	        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
	        itens.add(new SelectItem("nomeTurno", "Turno"));
	        itens.add(new SelectItem("nomeCurso", "Curso"));
	        return itens;
	    }
	    
	    private void montarListaCertificadoCursoExencaoPorTurma(TurmaVO turmaVO) {
        	try {
				List<MatriculaVO> listaMatricula = getFacadeFactory().getMatriculaFacade().executarConsultaPorTurmaCurso(turmaVO.getCodigo(), turmaVO.getCurso().getCodigo(), turmaVO.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				 if(!listaMatricula.isEmpty()) {
		     		    setApresentarResultadoConsultaMatriculaGerarCertificado(Boolean.TRUE);
		                 for(MatriculaVO obj : listaMatricula) {
		                 	CertificadoCursoExtensaoRelVO certificado = new CertificadoCursoExtensaoRelVO();
		                 	certificado.setMatriculaVO(obj);
		                 	getListaCertificadoCursoExtensaoRelVOGerar().add(certificado);
		                 }
		     		 }
        	} catch (Exception e) {
        		getListaCertificadoCursoExtensaoRelVOGerar().clear();
        		setApresentarResultadoConsultaMatriculaGerarCertificado(Boolean.FALSE);
				e.printStackTrace();
			}

        	
		}

	
	
	
}
