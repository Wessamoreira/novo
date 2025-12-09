package relatorio.controle.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoFinanceiroEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.DeclaracaoImpostoRendaRelVO;

@Controller("DeclaracaoImpostoRendaRelControle")
@Scope("viewScope")
@Lazy
public class DeclaracaoImpostoRendaRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private List<MatriculaVO> listaConsultaAluno;
	private DeclaracaoImpostoRendaRelVO declaracaoImpostoRendaRelVO;
	private List<DeclaracaoImpostoRendaRelVO> listaAnoDeclaracaoImpostoRendaRel;
	private List<SelectItem> listaSelectItemTipoPessoa;
	private String tipoPessoa;

	private String campoConsultaResponsavelFinanceiro;
	private String valorConsultaResponsavelFinanceiro;
	private List<PessoaVO> listaConsultaResponsavelFinanceiro;

	private List<ParceiroVO> listaConsultaParceiro;
	private String valorConsultaParceiro;
	private String campoConsultaParceiro;
	private Boolean apresentarDataPrevisaoRecebimentoVencimentoConta;
	private Boolean apresentarMaterialDidatico;
	private PessoaVO pessoaDeclaracaoImpostoRenda;
	private List<SelectItem> listaSelectItemFiliacaoVOs;
	private List<SelectItem> listaSelectItemMatriculaAlunoSelecionado;
	List<SelectItem> listaSelectItemTipoTextoPadrao;
	private String anoConsulta;
	private Integer textoPadrao;
	private String textoTMP;

	public DeclaracaoImpostoRendaRelControle() throws Exception {
		montarListaSelectItemTipoPessoa();
		montarListaSelectItemFiliacaoVOs();
		verificarPerfilAcessoUsuario();
		setMensagemID("msg_entre_prmrelatorio");
	}

	private void verificarPerfilAcessoUsuario() {
		verificarPermissaoApresentarDataPrevisaoRecebimentoVencimentoConta();
		verificarPermissaoApresentarMaterialDidatico();
	}

	@PostConstruct
	public void realizarImpressaoDeclaracaoImpostoRendaVindoTelaFichaAluno() {
		if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getSessionMap().get("matriculaImprimirDeclaracaoImpostoRendaFichaAluno");
			if (obj != null && !obj.getMatricula().equals("")) {
				try {
					getDeclaracaoImpostoRendaRelVO().getMatricula().setMatricula(obj.getMatricula());
					getDeclaracaoImpostoRendaRelVO().setTipoOrigemMensalidade(Boolean.TRUE);
					setTipoPessoa("AL");
					consultarAlunoDadosCompletos();
					//consultarAlunoPorMatricula();
				} catch (Exception e) {
					setMensagemDetalhada("msg_erro", e.getMessage());
				} finally {
					context().getExternalContext().getSessionMap().remove("matriculaImprimirDeclaracaoImpostoRendaFichaAluno");
				}
			}
		} else if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
			try {
				getDeclaracaoImpostoRendaRelVO().getMatricula().setMatricula(getVisaoAlunoControle().getMatricula().getMatricula());
				getFacadeFactory().getDeclaracaoImpostoRendaRelFacade().consultarPermissoesImpressaoDeclaracaoVisaoAluno(getDeclaracaoImpostoRendaRelVO(),getUsuarioLogado());
				consultarListaDeclaracaoImpostoRendaAluno();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
	}

	public void inicializarTextoPadrao(MatriculaVO matriculaVO) throws Exception {
		setListaSelectItemTipoTextoPadrao(new ArrayList<SelectItem>());
		List<TextoPadraoDeclaracaoVO> lista = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("DQ",matriculaVO.getUnidadeEnsino().getCodigo(), matriculaVO.getCurso().getNivelEducacional(), false,Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		if(Uteis.isAtributoPreenchido(lista)) {
			setListaSelectItemTipoTextoPadrao(UtilSelectItem.getListaSelectItem(lista,"codigo", "descricao", false));
		}
		getListaSelectItemTipoTextoPadrao().add(0, new SelectItem("0", "Layout Padrão"));
	}

	public void imprimirPDF() {
		try {
			validarDados();
			List<DeclaracaoImpostoRendaRelVO> declaracaoImpostoRendaRelVOs = getFacadeFactory().getDeclaracaoImpostoRendaRelFacade().criarObjeto(getDeclaracaoImpostoRendaRelVO(), getUsuarioLogado(), getTipoPessoa(), getDeclaracaoImpostoRendaRelVO(), getApresentarDataPrevisaoRecebimentoVencimentoConta());
			if (!declaracaoImpostoRendaRelVOs.isEmpty()) {
				adicionarParametroLogoRelatorio(declaracaoImpostoRendaRelVOs.get(0).getUnidadeEnsino(), getSuperParametroRelVO());
				getSuperParametroRelVO().setTituloRelatorio("DECLARAÇÃO PARA IMPOSTO DE RENDA");
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getDeclaracaoImpostoRendaRelFacade().designIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getDeclaracaoImpostoRendaRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getDeclaracaoImpostoRendaRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
				getSuperParametroRelVO().adicionarParametro("assinarDigitalmente", isAssinarDigitalmente());
				getSuperParametroRelVO().adicionarParametro("tipoPessoa", getTipoPessoa());
				getSuperParametroRelVO().setListaObjetos(declaracaoImpostoRendaRelVOs);
				setMensagemID("msg_relatorio_ok");
				
				realizarImpressaoRelatorio();
				gravarFiltrosPadroes();
//				removerObjetoMemoria(this);
				verificarPermissaoApresentarDataPrevisaoRecebimentoVencimentoConta();
				consultarFiltrosPadroes();
				montarListaSelectItemTipoPessoa();
				if(isAssinarDigitalmente()){
					String descricao = TipoOrigemDocumentoAssinadoEnum.IMPOSTO_RENDA.getDescricao()+"-"+getDeclaracaoImpostoRendaRelVO().getAno();
					setCaminhoRelatorio(getFacadeFactory().getDocumentoAssinadoFacade().realizarInclusaoDocumentoAssinadoPorImpostoRenda(getCaminhoRelatorio(), getDeclaracaoImpostoRendaRelVO().getMatricula(), getDeclaracaoImpostoRendaRelVO().getMatricula().getGradeCurricularAtual(), null, null, "", "", TipoOrigemDocumentoAssinadoEnum.IMPOSTO_RENDA,getProvedorDeAssinaturaEnum(), OrigemArquivo.IMPOSTO_RENDA.getValor(),descricao, getCorAssinaturaDigitalmente(), getAlturaAssinatura(), getLarguraAssinatura(),  getConfiguracaoGeralPadraoSistema(), 0, getUsuarioLogado()));
					getListaDocumentoAsssinados().clear();
				}
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<SelectItem> getComboboxProvedorAssinaturaPadrao(){
    	Integer codigoUnidadeEnsino = Uteis.isAtributoPreenchido(getDeclaracaoImpostoRendaRelVO().getMatricula().getUnidadeEnsino().getCodigo()) ? getDeclaracaoImpostoRendaRelVO().getMatricula().getUnidadeEnsino().getCodigo() : 0;
    	if(!Uteis.isAtributoPreenchido(codigoUnidadeEnsino)){
    		return new ArrayList<SelectItem>();
    	}
    	return this.getComboboxProvedorAssinaturaPadrao(codigoUnidadeEnsino, TipoOrigemDocumentoAssinadoEnum.IMPOSTO_RENDA);
    }
	
	public void gravarFiltrosPadroes() throws Exception {
		if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			getDeclaracaoImpostoRendaRelVO().getTipoOrigemBiblioteca();
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getDeclaracaoImpostoRendaRelVO().getUtilizarDataVencimentoParaDataRecebimento().toString(), DeclaracaoImpostoRendaRelControle.class.getSimpleName(), "utilizarDataVencimentoParaDataRecebimento", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getDeclaracaoImpostoRendaRelVO().getAno(), DeclaracaoImpostoRendaRelControle.class.getSimpleName(), "ano", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getDeclaracaoImpostoRendaRelVO().getTipoOrigemBiblioteca().toString(), 
					DeclaracaoImpostoRendaRelControle.class.getSimpleName(), "tipoOrigemBiblioteca", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getDeclaracaoImpostoRendaRelVO().getTipoOrigemBolsaCusteadaConvenio().toString(), 
					DeclaracaoImpostoRendaRelControle.class.getSimpleName(), "tipoOrigemBolsaCusteadaConvenio", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getDeclaracaoImpostoRendaRelVO().getTipoOrigemContratoReceita().toString(), 
					DeclaracaoImpostoRendaRelControle.class.getSimpleName(), "tipoOrigemContratoReceita", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getDeclaracaoImpostoRendaRelVO().getTipoOrigemDevolucaoCheque().toString(), 
					DeclaracaoImpostoRendaRelControle.class.getSimpleName(), "tipoOrigemDevolucaoCheque", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getDeclaracaoImpostoRendaRelVO().getTipoOrigemInclusaoReposicao().toString(), 
					DeclaracaoImpostoRendaRelControle.class.getSimpleName(), "tipoOrigemInclusaoReposicao", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getDeclaracaoImpostoRendaRelVO().getTipoOrigemInscricaoProcessoSeletivo().toString(), 
					DeclaracaoImpostoRendaRelControle.class.getSimpleName(), "tipoOrigemInscricaoProcessoSeletivo", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getDeclaracaoImpostoRendaRelVO().getTipoOrigemMatricula().toString(), 
					DeclaracaoImpostoRendaRelControle.class.getSimpleName(), "tipoOrigemMatricula", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getDeclaracaoImpostoRendaRelVO().getTipoOrigemMensalidade().toString(), 
					DeclaracaoImpostoRendaRelControle.class.getSimpleName(), "tipoOrigemMensalidade", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getDeclaracaoImpostoRendaRelVO().getTipoOrigemNegociacao().toString(), 
					DeclaracaoImpostoRendaRelControle.class.getSimpleName(), "tipoOrigemNegociacao", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getDeclaracaoImpostoRendaRelVO().getTipoOrigemOutros().toString(), 
					DeclaracaoImpostoRendaRelControle.class.getSimpleName(), "tipoOrigemOutros", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(
					getDeclaracaoImpostoRendaRelVO().getTipoOrigemMaterialDidatico().toString(),
					DeclaracaoImpostoRendaRelControle.class.getSimpleName(), "tipoOrigemMaterialDidatico",
					getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(
					getDeclaracaoImpostoRendaRelVO().getTipoOrigemRequerimento().toString(),
					DeclaracaoImpostoRendaRelControle.class.getSimpleName(), "tipoOrigemRequerimento",
					getUsuarioLogado());

		}
	}
	
	@PostConstruct
	public void consultarFiltrosPadroes() {
		try {
			if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				
				Map<String, String> retorno = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[] { "ano", "utilizarDataVencimentoParaDataRecebimento",
						"tipoOrigemBiblioteca", "tipoOrigemBolsaCusteadaConvenio", "tipoOrigemContratoReceita", "tipoOrigemDevolucaoCheque", "tipoOrigemInclusaoReposicao", 
						"tipoOrigemInscricaoProcessoSeletivo", "tipoOrigemMatricula", "tipoOrigemMensalidade", "tipoOrigemNegociacao", "tipoOrigemOutros", "tipoOrigemRequerimento"}, DeclaracaoImpostoRendaRelControle.class.getSimpleName());
				if (retorno.containsKey("ano")) {
					getDeclaracaoImpostoRendaRelVO().setAno(retorno.get("ano"));
				}
				if (retorno.containsKey("utilizarDataVencimentoParaDataRecebimento")) {
					getDeclaracaoImpostoRendaRelVO().setUtilizarDataVencimentoParaDataRecebimento(Boolean.valueOf(retorno.get("utilizarDataVencimentoParaDataRecebimento")));
				}
				if (retorno.containsKey("tipoOrigemBiblioteca")) {
					getDeclaracaoImpostoRendaRelVO().setTipoOrigemBiblioteca(Boolean.valueOf(retorno.get("tipoOrigemBiblioteca")));
				}
				if (retorno.containsKey("tipoOrigemBolsaCusteadaConvenio")) {
					getDeclaracaoImpostoRendaRelVO().setTipoOrigemBolsaCusteadaConvenio(Boolean.valueOf(retorno.get("tipoOrigemBolsaCusteadaConvenio")));
				}
				if (retorno.containsKey("tipoOrigemContratoReceita")) {
					getDeclaracaoImpostoRendaRelVO().setTipoOrigemContratoReceita(Boolean.valueOf(retorno.get("tipoOrigemContratoReceita")));
				}
				if (retorno.containsKey("tipoOrigemDevolucaoCheque")) {
					getDeclaracaoImpostoRendaRelVO().setTipoOrigemDevolucaoCheque(Boolean.valueOf(retorno.get("tipoOrigemDevolucaoCheque")));
				}
				if (retorno.containsKey("tipoOrigemInclusaoReposicao")) {
					getDeclaracaoImpostoRendaRelVO().setTipoOrigemInclusaoReposicao(Boolean.valueOf(retorno.get("tipoOrigemInclusaoReposicao")));
				}
				if (retorno.containsKey("tipoOrigemInscricaoProcessoSeletivo")) {
					getDeclaracaoImpostoRendaRelVO().setTipoOrigemInscricaoProcessoSeletivo(Boolean.valueOf(retorno.get("tipoOrigemInscricaoProcessoSeletivo")));
				}
				if (retorno.containsKey("tipoOrigemMatricula")) {
					getDeclaracaoImpostoRendaRelVO().setTipoOrigemMatricula(Boolean.valueOf(retorno.get("tipoOrigemMatricula")));
				}
				if (retorno.containsKey("tipoOrigemMensalidade")) {
					getDeclaracaoImpostoRendaRelVO().setTipoOrigemMensalidade(Boolean.valueOf(retorno.get("tipoOrigemMensalidade")));
				}
				if (retorno.containsKey("tipoOrigemNegociacao")) {
					getDeclaracaoImpostoRendaRelVO().setTipoOrigemNegociacao(Boolean.valueOf(retorno.get("tipoOrigemNegociacao")));
				}
				if (retorno.containsKey("tipoOrigemOutros")) {
					getDeclaracaoImpostoRendaRelVO().setTipoOrigemOutros(Boolean.valueOf(retorno.get("tipoOrigemOutros")));
				}
				if (retorno.containsKey("tipoOrigemRequerimento")) {
					getDeclaracaoImpostoRendaRelVO().setTipoOrigemRequerimento(Boolean.valueOf(retorno.get("tipoOrigemRequerimento")));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void imprimirPDFVisaoAluno() {
		try {
			DeclaracaoImpostoRendaRelVO declaracaoImpostoRendaRelVO = (DeclaracaoImpostoRendaRelVO) getRequestMap().get("anoDeclaracaoImpostoRendaItens");
			declaracaoImpostoRendaRelVO.setUtilizarDataVencimentoParaDataRecebimento(getDeclaracaoImpostoRendaRelVO().getUtilizarDataVencimentoParaDataRecebimento());
			TextoPadraoDeclaracaoVO padraoDeclaracaoVO = new TextoPadraoDeclaracaoVO();
			
			if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
				padraoDeclaracaoVO = consultarTextoPadraoDeclaracaoQuitacaoDebitos();
			} else if (getUsuarioLogado().getIsApresentarVisaoAdministrativa() && Uteis.isAtributoPreenchido(getTextoPadrao())) {
				padraoDeclaracaoVO = consultarTextoPadraoDeclaracaoQuitacaoDebitosPorCodigo();
			}

			if (!declaracaoImpostoRendaRelVO.getMatricula().getIsNivelMontarDadosTodos() && Uteis.isAtributoPreenchido(declaracaoImpostoRendaRelVO.getMatricula().getMatricula())) {
				getFacadeFactory().getMatriculaFacade().carregarDados(declaracaoImpostoRendaRelVO.getMatricula(),NivelMontarDados.TODOS, getUsuarioLogado());
			}

			if (declaracaoImpostoRendaRelVO.getAno().equals("")) {
				throw new Exception("O campo ANO deve ser informado para emissão da declaração de imposto de renda!");
			}

			if (getUsuarioLogado().getIsApresentarVisaoAluno()) {
				setTipoPessoa("AL");
				declaracaoImpostoRendaRelVO.setLayoutPadrao(!padraoDeclaracaoVO.getModeloPadraoVisaoAluno());
				getDeclaracaoImpostoRendaRelVO().setTipoOrigemMaterialDidatico(getApresentarMaterialDidatico());
			} else if (getUsuarioLogado().getIsApresentarVisaoPais()) {
				setTipoPessoa("RF");
				declaracaoImpostoRendaRelVO.setLayoutPadrao(!padraoDeclaracaoVO.getModeloPadraoVisaoAluno());
				getDeclaracaoImpostoRendaRelVO().setPessoa(getUsuarioLogado().getPessoa());
			} else if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				declaracaoImpostoRendaRelVO.setLayoutPadrao(!Uteis.isAtributoPreenchido(getTextoPadrao()));
				declaracaoImpostoRendaRelVO.setPessoa(getDeclaracaoImpostoRendaRelVO().getPessoa());
			}

			List<DeclaracaoImpostoRendaRelVO> declaracaoImpostoRendaRelVOs = getFacadeFactory().getDeclaracaoImpostoRendaRelFacade().criarObjeto(declaracaoImpostoRendaRelVO, getUsuarioLogado(),getTipoPessoa(), getDeclaracaoImpostoRendaRelVO(),getApresentarDataPrevisaoRecebimentoVencimentoConta());
			if (!declaracaoImpostoRendaRelVOs.isEmpty()) {
				if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
					if (declaracaoImpostoRendaRelVO.getLayoutPadrao()) {
						impressaoPadrao(declaracaoImpostoRendaRelVO, declaracaoImpostoRendaRelVOs);
					} else {
						impressaoComTextoPadrao(declaracaoImpostoRendaRelVO, declaracaoImpostoRendaRelVOs, padraoDeclaracaoVO);
					}
				} else if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
					if (!Uteis.isAtributoPreenchido(getTextoPadrao())) {
						impressaoPadrao(declaracaoImpostoRendaRelVO, declaracaoImpostoRendaRelVOs);
					} else {
						impressaoComTextoPadrao(declaracaoImpostoRendaRelVO, declaracaoImpostoRendaRelVOs,padraoDeclaracaoVO);
					}
				}
				setMensagemID("msg_relatorio_ok");
				realizarImpressaoRelatorio();
				if (isAssinarDigitalmente() && declaracaoImpostoRendaRelVO.getLayoutPadrao()) {
					assinarDeclaracaoImpostoRenda();
				}else if(isAssinarDigitalmente() && padraoDeclaracaoVO.getAssinarDigitalmenteTextoPadrao()) {
					assinarDeclaracaoImpostoRenda();
				}
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void assinarDeclaracaoImpostoRenda() throws Exception {
		String descricao = TipoOrigemDocumentoAssinadoEnum.IMPOSTO_RENDA.getDescricao()+"-"+getDeclaracaoImpostoRendaRelVO().getAno();
		if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
			setCaminhoRelatorio(getFacadeFactory().getDocumentoAssinadoFacade().realizarInclusaoDocumentoAssinadoPorImpostoRenda(getCaminhoRelatorio(),getVisaoAlunoControle().getMatricula(),getVisaoAlunoControle().getMatricula().getGradeCurricularAtual(), null, null,"", "", TipoOrigemDocumentoAssinadoEnum.IMPOSTO_RENDA,getProvedorDeAssinaturaEnum(), OrigemArquivo.IMPOSTO_RENDA.getValor(), descricao,getCorAssinaturaDigitalmente(), getAlturaAssinatura(), getLarguraAssinatura(),getConfiguracaoGeralPadraoSistema(), 0, getUsuarioLogado()));
			getListaDocumentoAsssinados().clear();
		} else {
			setCaminhoRelatorio(getFacadeFactory().getDocumentoAssinadoFacade().realizarInclusaoDocumentoAssinadoPorImpostoRenda(getCaminhoRelatorio(),getDeclaracaoImpostoRendaRelVO().getMatricula(),getDeclaracaoImpostoRendaRelVO().getMatricula().getGradeCurricularAtual(), null,null, "", "", TipoOrigemDocumentoAssinadoEnum.IMPOSTO_RENDA,getProvedorDeAssinaturaEnum(), OrigemArquivo.IMPOSTO_RENDA.getValor(), descricao,getCorAssinaturaDigitalmente(), getAlturaAssinatura(), getLarguraAssinatura(),getConfiguracaoGeralPadraoSistema(), 0, getUsuarioLogado()));
			getListaDocumentoAsssinados().clear();
		}
	}

	private TextoPadraoDeclaracaoVO consultarTextoPadraoDeclaracaoQuitacaoDebitosPorCodigo() throws Exception {
		return getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(getTextoPadrao(),
				Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	public TextoPadraoDeclaracaoVO consultarTextoPadraoDeclaracaoQuitacaoDebitos() throws Exception { 
		List<TextoPadraoDeclaracaoVO> textoPadraoDeclaracaoVOs = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("DQ", getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo(), getVisaoAlunoControle().getMatricula().getCurso().getNivelEducacional(), false,Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuario());
		if (Uteis.isAtributoPreenchido(textoPadraoDeclaracaoVOs)) {
			if (textoPadraoDeclaracaoVOs.size() > 1) {
				return textoPadraoDeclaracaoVOs.stream().filter(tp -> tp.getUnidadeEnsino().getCodigo().equals(getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo())).findFirst().get();
			} else {
				return textoPadraoDeclaracaoVOs.iterator().next();
			}
		}
		return new TextoPadraoDeclaracaoVO();
	}

	private void impressaoComTextoPadrao(DeclaracaoImpostoRendaRelVO declaracaoImpostoRendaRelVO, List<DeclaracaoImpostoRendaRelVO> declaracaoImpostoRendaRelVOs, TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO) throws Exception {

		String caminhoArquivo = (!textoPadraoDeclaracaoVO.getArquivoIreport().getPastaBaseArquivo().startsWith(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo()) ? getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator : "") + textoPadraoDeclaracaoVO.getArquivoIreport().getPastaBaseArquivo() + File.separator + textoPadraoDeclaracaoVO.getArquivoIreport().getNome();
		String caminhoSubRelatorio = getFacadeFactory().getImpressaoDeclaracaoFacade().criarCaminhoPastaAteDiretorioSubRelatorio(textoPadraoDeclaracaoVO, getConfiguracaoGeralPadraoSistema());
		
		adicionarParametroLogoRelatorio(declaracaoImpostoRendaRelVO.getUnidadeEnsino(), getSuperParametroRelVO());
		getSuperParametroRelVO().setNomeDesignIreport(caminhoArquivo);
		getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
		getSuperParametroRelVO().setSubReport_Dir("relatorio" + File.separator + "designRelatorio" + File.separator + "textoPadrao" + File.separator);
		getSuperParametroRelVO().setSubRelatorio_Dir(caminhoSubRelatorio + File.separator);
		getSuperParametroRelVO().setCaminhoBaseRelatorio("relatorio" + File.separator + "designRelatorio" + File.separator + "textoPadrao" + File.separator);
		getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
		getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
		getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
		getSuperParametroRelVO().adicionarParametro("assinarDigitalmente", isAssinarDigitalmente());
		getSuperParametroRelVO().adicionarParametro("tipoPessoa", getTipoPessoa());
		getSuperParametroRelVO().setListaObjetos(declaracaoImpostoRendaRelVOs);
	}

	private void impressaoPadrao(DeclaracaoImpostoRendaRelVO declaracaoImpostoRendaRelVO,List<DeclaracaoImpostoRendaRelVO> declaracaoImpostoRendaRelVOs) throws Exception {
			adicionarParametroLogoRelatorio(declaracaoImpostoRendaRelVO.getUnidadeEnsino(), getSuperParametroRelVO());
			getSuperParametroRelVO().setTituloRelatorio("DECLARAÇÃO PARA IMPOSTO DE RENDA");
			getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getDeclaracaoImpostoRendaRelFacade().designIReportRelatorio());
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getDeclaracaoImpostoRendaRelFacade().caminhoBaseRelatorio());
			getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getDeclaracaoImpostoRendaRelFacade().caminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
			getSuperParametroRelVO().adicionarParametro("assinarDigitalmente", isAssinarDigitalmente());
			getSuperParametroRelVO().adicionarParametro("tipoPessoa", getTipoPessoa());
			getSuperParametroRelVO().setListaObjetos(declaracaoImpostoRendaRelVOs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.academico.DeclaracaoConclusaoCursoRelInterfaceFacade
	 * #getApresentarCampos()
	 */
	public Boolean getApresentarCampos() {
		if ((getDeclaracaoImpostoRendaRelVO().getMatricula().getAluno() != null) && (getDeclaracaoImpostoRendaRelVO().getMatricula().getAluno().getCodigo() != 0)) {
			return true;
		} else if (Uteis.isAtributoPreenchido(getTipoPessoa()) && Uteis.isAtributoPreenchido(getDeclaracaoImpostoRendaRelVO().getParceiro().getCodigo())) {
			return true;
		} else if (Uteis.isAtributoPreenchido(getTipoPessoa()) && Uteis.isAtributoPreenchido(getDeclaracaoImpostoRendaRelVO().getPessoa().getCodigo())) {
			return true;
		}
		return false;
	}

	public void consultarAluno() {
		try {
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				if (!matriculaVO.getMatricula().equals("")) {
					getListaConsultaAluno().add(matriculaVO);
				} else {
					removerObjetoMemoria(matriculaVO);
				}
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		getDeclaracaoImpostoRendaRelVO().setMatricula(obj);
		consultarAlunoDadosCompletos();
		obj = null;
		valorConsultaAluno = "";
		campoConsultaAluno = "";
		getListaConsultaAluno().clear();
		if (getApresentarMaterialDidatico()) {
			inicializarTextoPadrao(getDeclaracaoImpostoRendaRelVO().getMatricula());
		}
		if (!getTipoPessoa().equals("PA")) {
			consultarListaDeclaracaoImpostoRendaAluno();
		}
	}

	public void consultarAlunoDadosCompletos() throws Exception {
		try {
			getFacadeFactory().getMatriculaFacade().carregarDados(getDeclaracaoImpostoRendaRelVO().getMatricula(), NivelMontarDados.TODOS, getUsuarioLogado());
//			getDeclaracaoImpostoRendaRelVO().getMatricula().setMatriculaPeriodoVOs(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaUltimaMatriculaPeriodoPorMatricula(getDeclaracaoImpostoRendaRelVO().getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			setMensagemDetalhada("");
			if (!getTipoPessoa().equals("PA")) {
				consultarListaDeclaracaoImpostoRendaAluno();
			}
			if(getApresentarMaterialDidatico()) {
				inicializarTextoPadrao(getDeclaracaoImpostoRendaRelVO().getMatricula());
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			getDeclaracaoImpostoRendaRelVO().setMatricula(new MatriculaVO());
		}
	}

	public void consultarAlunoPorMatricula() throws Exception {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getDeclaracaoImpostoRendaRelVO().getMatricula().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getDeclaracaoImpostoRendaRelVO().getMatricula().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			this.getDeclaracaoImpostoRendaRelVO().setMatricula(objAluno);
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			getDeclaracaoImpostoRendaRelVO().setMatricula(new MatriculaVO());
		}
	}
	
	public void consultarListaDeclaracaoImpostoRendaAluno() throws Exception {
		try {
			if (getUsuarioLogado().getIsApresentarVisaoAluno()) {
				setTipoPessoa("AL");
				setListaAnoDeclaracaoImpostoRendaRel(getFacadeFactory().getDeclaracaoImpostoRendaRelFacade().executarConsultaParametrizada(getDeclaracaoImpostoRendaRelVO(),  getUsuarioLogado(), getTipoPessoa(), getAnoConsulta()));      
			} else if (getUsuarioLogado().getIsApresentarVisaoPais()) {
				setTipoPessoa("RF");
				getDeclaracaoImpostoRendaRelVO().setPessoa(getUsuarioLogado().getPessoa());
				setListaAnoDeclaracaoImpostoRendaRel(getFacadeFactory().getDeclaracaoImpostoRendaRelFacade().executarConsultaParametrizada(getDeclaracaoImpostoRendaRelVO(),  getUsuarioLogado(), getTipoPessoa(), getAnoConsulta()));
			} else if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				setListaAnoDeclaracaoImpostoRendaRel(getFacadeFactory().getDeclaracaoImpostoRendaRelFacade().executarConsultaParametrizada(getDeclaracaoImpostoRendaRelVO(),  getUsuarioLogado(), getTipoPessoa(), getAnoConsulta()));
			}
			if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				gravarFiltrosPadroes();
			}
			if (getListaAnoDeclaracaoImpostoRendaRel().isEmpty()) {
				setMensagemID("msg_relatorio_sem_dados");				
			} else {
				setMensagemID("msg_dados_consultados");
			}
        } catch (Exception e) {
        	setListaAnoDeclaracaoImpostoRendaRel(new ArrayList<DeclaracaoImpostoRendaRelVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
	}

	public void limparDadosAluno() throws Exception {
		getDeclaracaoImpostoRendaRelVO().setMatricula(null);
		setMensagemID("msg_entre_prmrelatorio");
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	/**
	 * @return the listaConsultaAluno
	 */
	public List<MatriculaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaAluno;
	}

	/**
	 * @param listaConsultaAluno
	 *            the listaConsultaAluno to set
	 */
	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}	

	/**
	 * @return the valorConsultaAluno
	 */
	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	/**
	 * @param valorConsultaAluno
	 *            the valorConsultaAluno to set
	 */
	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	/**
	 * @return the campoConsultaAluno
	 */
	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	/**
	 * @param campoConsultaAluno
	 *            the campoConsultaAluno to set
	 */
	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public DeclaracaoImpostoRendaRelVO getDeclaracaoImpostoRendaRelVO() {
		if (declaracaoImpostoRendaRelVO == null) {
			declaracaoImpostoRendaRelVO = new DeclaracaoImpostoRendaRelVO();
		}
		return declaracaoImpostoRendaRelVO;
	}

	public void setDeclaracaoImpostoRendaRelVO(DeclaracaoImpostoRendaRelVO declaracaoImpostoRendaRelVO) {
		this.declaracaoImpostoRendaRelVO = declaracaoImpostoRendaRelVO;
	}

	/**
	 * @return the listaAnoDeclaracaoImpostoRendaRel
	 */
	public List<DeclaracaoImpostoRendaRelVO> getListaAnoDeclaracaoImpostoRendaRel() {
		if (listaAnoDeclaracaoImpostoRendaRel == null) {
			listaAnoDeclaracaoImpostoRendaRel = new ArrayList<DeclaracaoImpostoRendaRelVO>(0);
		}
		return listaAnoDeclaracaoImpostoRendaRel;
	}

	/**
	 * @param listaAnoDeclaracaoImpostoRendaRel
	 *            the listaAnoDeclaracaoImpostoRendaRel to set
	 */
	public void setListaAnoDeclaracaoImpostoRendaRel(List<DeclaracaoImpostoRendaRelVO> listaAnoDeclaracaoImpostoRendaRel) {
		this.listaAnoDeclaracaoImpostoRendaRel = listaAnoDeclaracaoImpostoRendaRel;
	}
	
	public boolean isAssinarDigitalmente() {
		try {
    		return ControleAcesso.verificarPermissaoFuncionalidadeUsuario("ImprimirAssinaturaDigital", getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return false;
		}
	}
	
	
	
	public List<SelectItem> getListaSelectItemTipoPessoa() {
		if (listaSelectItemTipoPessoa == null) {
			listaSelectItemTipoPessoa = new ArrayList<SelectItem>();
		}
		return listaSelectItemTipoPessoa;
	}

	public void setListaSelectItemTipoPessoa(List<SelectItem> listaSelectItemTipoPessoa) {
		this.listaSelectItemTipoPessoa = listaSelectItemTipoPessoa;
	}

	public void montarListaSelectItemTipoPessoa() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", null));
		objs.add(new SelectItem("AL", "Aluno"));
		objs.add(new SelectItem("RF", "Responsável Financeiro"));
		objs.add(new SelectItem("PA", "Parceiro"));
		setListaSelectItemTipoPessoa(objs);
	}

	public String getTipoPessoa() {
		if (tipoPessoa == null) {
			tipoPessoa = "";
		}
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}
	
	public String getCampoConsultaResponsavelFinanceiro() {
		if (campoConsultaResponsavelFinanceiro == null) {
			campoConsultaResponsavelFinanceiro = "";
		}
		return campoConsultaResponsavelFinanceiro;
	}

	public void setCampoConsultaResponsavelFinanceiro(String campoConsultaResponsavelFinanceiro) {
		this.campoConsultaResponsavelFinanceiro = campoConsultaResponsavelFinanceiro;
	}
	
	private List<SelectItem> tipoConsultaComboResponsavelFinanceiro;

	public List<SelectItem> getTipoConsultaComboResponsavelFinanceiro() {
		if (tipoConsultaComboResponsavelFinanceiro == null) {
			tipoConsultaComboResponsavelFinanceiro = new ArrayList<SelectItem>(0);
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nomeAluno", "Aluno"));
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("CPF", "CPF"));
		}
		return tipoConsultaComboResponsavelFinanceiro;
	}
	
	public String getValorConsultaResponsavelFinanceiro() {
		if (valorConsultaResponsavelFinanceiro == null) {
			valorConsultaResponsavelFinanceiro = "";
		}
		return valorConsultaResponsavelFinanceiro;
	}

	public void setValorConsultaResponsavelFinanceiro(String valorConsultaResponsavelFinanceiro) {
		this.valorConsultaResponsavelFinanceiro = valorConsultaResponsavelFinanceiro;
	}
	
	public void consultarResponsavelFinanceiro() {
		try {

			if (getValorConsultaResponsavelFinanceiro().trim().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("ResponsavelFinanceiro");
			getListaConsultaResponsavelFinanceiro().clear();
			if (getCampoConsultaResponsavelFinanceiro().equals("nome")) {
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorNomeResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), null, false, getUsuarioLogado()));
			}
			if (getCampoConsultaResponsavelFinanceiro().equals("nomeAluno")) {
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorNomeAlunoResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), null, false, getUsuarioLogado()));
			}
			if (getCampoConsultaResponsavelFinanceiro().equals("CPF")) {
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorCpfResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), null, false, getUsuarioLogado()));
			}

			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaResponsavelFinanceiro(new ArrayList<PessoaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<PessoaVO> getListaConsultaResponsavelFinanceiro() {
		if (listaConsultaResponsavelFinanceiro == null) {
			listaConsultaResponsavelFinanceiro = new ArrayList<PessoaVO>(0);
		}
		return listaConsultaResponsavelFinanceiro;
	}

	public void setListaConsultaResponsavelFinanceiro(List<PessoaVO> listaConsultaResponsavelFinanceiro) {
		this.listaConsultaResponsavelFinanceiro = listaConsultaResponsavelFinanceiro;
	}
	
	public void selecionarResponsavelFinanceiro() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("responsavelFinanceiroItens");
			getDeclaracaoImpostoRendaRelVO().setPessoa(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado()));
			consultarListaDeclaracaoImpostoRendaAluno();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List getListaConsultaParceiro() {
		if (listaConsultaParceiro == null) {
			listaConsultaParceiro = new ArrayList(0);
		}
		return listaConsultaParceiro;
	}

	public void setListaConsultaParceiro(List listaConsultaParceiro) {
		this.listaConsultaParceiro = listaConsultaParceiro;
	}

	public String getValorConsultaParceiro() {
		if (valorConsultaParceiro == null) {
			valorConsultaParceiro = "";
		}
		return valorConsultaParceiro;
	}

	public void setValorConsultaParceiro(String valorConsultaParceiro) {
		this.valorConsultaParceiro = valorConsultaParceiro;
	}

	public String getCampoConsultaParceiro() {
		if (campoConsultaParceiro == null) {
			campoConsultaParceiro = "";
		}
		return campoConsultaParceiro;
	}

	public void setCampoConsultaParceiro(String campoConsultaParceiro) {
		this.campoConsultaParceiro = campoConsultaParceiro;
	}
	
	public List getTipoConsultaParceiro() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão social"));
		return itens;
	}

	public void selecionarParceiro() {
		try {
			ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
			getDeclaracaoImpostoRendaRelVO().setParceiro(obj);
			listaConsultaParceiro.clear();
			this.setValorConsultaParceiro("");
			this.setCampoConsultaParceiro("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void validarDados() throws Exception {
		try {
			if (getDeclaracaoImpostoRendaRelVO().getAno().equals("")) {
				throw new ConsistirException("O campo ANO deve ser informado para emissão da declaração de imposto de renda!");
			}
			if (!Uteis.isAtributoPreenchido(getTipoPessoa())) {
				throw new ConsistirException("O campo TIPO PESSOA deve ser informado.");
			}
			if (!Uteis.isAtributoPreenchido(getDeclaracaoImpostoRendaRelVO().getMatricula().getMatricula()) && getTipoPessoa().equals("AL")) {
				throw new ConsistirException("O campo MATRÍCULA deve ser preenchido");
			}
			if (!Uteis.isAtributoPreenchido(getDeclaracaoImpostoRendaRelVO().getPessoa().getCodigo()) && getTipoPessoa().equals("RF")) {
				throw new ConsistirException("O campo RESPONSÁVEL FINANCEIRO deve ser preenchido");
			}
			if (!Uteis.isAtributoPreenchido(getDeclaracaoImpostoRendaRelVO().getParceiro().getCodigo()) && getTipoPessoa().equals("PA")) {
				throw new ConsistirException("O campo PARCEIRO deve ser preenchido");
			}
		} catch (Exception e) {
			throw e;
		}

	}
	
	public void consultarParceiro() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaParceiro().equals("nome")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorNome(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("razaoSocial")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocial(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaParceiro(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	
	
	public Boolean getApresentarDataPrevisaoRecebimentoVencimentoConta() {
		if (apresentarDataPrevisaoRecebimentoVencimentoConta == null) {
			apresentarDataPrevisaoRecebimentoVencimentoConta = false;
		}
		return apresentarDataPrevisaoRecebimentoVencimentoConta;
	}

	public void setApresentarDataPrevisaoRecebimentoVencimentoConta(Boolean apresentarDataPrevisaoRecebimentoVencimentoConta) {
		this.apresentarDataPrevisaoRecebimentoVencimentoConta = apresentarDataPrevisaoRecebimentoVencimentoConta;
	}

	public void verificarPermissaoApresentarDataPrevisaoRecebimentoVencimentoConta() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidade("ApresentarDataPrevisaoRecebimentoVencimentoConta", getUsuarioLogado());
			setApresentarDataPrevisaoRecebimentoVencimentoConta(Boolean.TRUE);
		} catch (Exception e) {
			setApresentarDataPrevisaoRecebimentoVencimentoConta(Boolean.FALSE);
		}
	}

	public void verificarPermissaoApresentarMaterialDidatico() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(
					PerfilAcessoPermissaoFinanceiroEnum.APRESENTAR_CONTA_MATERIAL_DIDATICO, getUsuarioLogado());
			setApresentarMaterialDidatico(Boolean.TRUE);
		} catch (Exception e) {
			setApresentarMaterialDidatico(Boolean.FALSE);
		}
	}

	public void montarListaMatriculaAluno() {
		try {
			if (!getPessoaDeclaracaoImpostoRenda().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo()) && !getPessoaDeclaracaoImpostoRenda().getCodigo().equals(getDeclaracaoImpostoRendaRelVO().getPessoa().getCodigo())) {
				List listaMatricula = getFacadeFactory().getMatriculaFacade().consultaRapidaCompletaPorCodigoPessoa(getPessoaDeclaracaoImpostoRenda().getCodigo(), 0, false, getUsuarioLogado());
				setListaSelectItemMatriculaAlunoSelecionado(null);
				List resultadoConsulta = null;
				Iterator i = null;
				i = listaMatricula.iterator();
				List objs = new ArrayList(0);
				while (i.hasNext()) {
					MatriculaVO obj = (MatriculaVO) i.next();
					objs.add(new SelectItem(obj.getMatricula(), obj.getMatricula() + " - " + obj.getCurso().getNome().toUpperCase() + " (" + obj.getSituacao_Apresentar().toUpperCase() + ")"));
					getDeclaracaoImpostoRendaRelVO().setMatricula((MatriculaVO) listaMatricula.get(0));
				}
				setListaSelectItemMatriculaAlunoSelecionado(objs);
			} else {
				getDeclaracaoImpostoRendaRelVO().setMatricula(null);
			}
			selecionarPessoaDeclaracaoImpostoRenda();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void selecionarPessoaDeclaracaoImpostoRenda() {
		try {
			if (Uteis.isAtributoPreenchido(getDeclaracaoImpostoRendaRelVO().getMatricula().getMatricula())) {
				getFacadeFactory().getMatriculaFacade().carregarDados(getDeclaracaoImpostoRendaRelVO().getMatricula(), NivelMontarDados.BASICO, getUsuarioLogado());
				try {
					if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
						getFacadeFactory().getDeclaracaoImpostoRendaRelFacade().consultarPermissoesImpressaoDeclaracaoVisaoAluno(getDeclaracaoImpostoRendaRelVO(), getUsuarioLogado());
						consultarListaDeclaracaoImpostoRendaAluno();
					}

				} catch (Exception e) {
					setMensagemDetalhada("msg_erro", e.getMessage());
				}
			} else {
				if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
//					if (!Uteis.isAtributoPreenchido(listaMatricula)) {
//						getDeclaracaoImpostoRendaRelVO().setMatricula(null);
//					}
					consultarListaDeclaracaoImpostoRendaAluno();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public PessoaVO getPessoaDeclaracaoImpostoRenda() {
		if (pessoaDeclaracaoImpostoRenda == null) {
			pessoaDeclaracaoImpostoRenda = new PessoaVO();
		}
		return pessoaDeclaracaoImpostoRenda;
	}

	public void setPessoaDeclaracaoImpostoRenda(PessoaVO pessoaDeclaracaoImpostoRenda) {
		this.pessoaDeclaracaoImpostoRenda = pessoaDeclaracaoImpostoRenda;
	}

	public List<SelectItem> getListaSelectItemFiliacaoVOs() {
		if (listaSelectItemFiliacaoVOs == null) {
			listaSelectItemFiliacaoVOs = new ArrayList<SelectItem>();
		}
		return listaSelectItemFiliacaoVOs;
	}

	public void setListaSelectItemFiliacaoVOs(List<SelectItem> listaSelectItemFiliacaoVOs) {
		this.listaSelectItemFiliacaoVOs = listaSelectItemFiliacaoVOs;
	}

    public void montarListaSelectItemFiliacaoVOs() throws Exception {
    	setListaSelectItemFiliacaoVOs(null);
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarFiliadosResponsavelFinanceiro();
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
//            objs.add(new SelectItem(null, ""));
            if (getUsuarioLogado().getIsApresentarVisaoPais()) {
            	objs.add(new SelectItem(getUsuarioLogado().getPessoa().getCodigo(), getUsuarioLogado().getPessoa().getNome()));
            } else {
            	objs.add(new SelectItem(getDeclaracaoImpostoRendaRelVO().getPessoa().getCodigo(), getDeclaracaoImpostoRendaRelVO().getPessoa().getNome()));
            }
            while (i.hasNext()) {
                UsuarioVO obj = (UsuarioVO) i.next();
                objs.add(new SelectItem(obj.getPessoa().getCodigo(), obj.getNome()));
            }
            setListaSelectItemFiliacaoVOs(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }
    
    public List consultarFiliadosResponsavelFinanceiro() throws Exception {
    	List<UsuarioVO> listaAlunos = new ArrayList<>();
    	if (getUsuarioLogado().getIsApresentarVisaoPais()) {
    		listaAlunos = (getFacadeFactory().getUsuarioFacade().consultaRapidaPorResponsavelLegal(getUsuarioLogado().getPessoa().getCodigo(), false, getUsuarioLogado()));
    	} else {
    		listaAlunos = (getFacadeFactory().getUsuarioFacade().consultaRapidaPorResponsavelLegal(getDeclaracaoImpostoRendaRelVO().getPessoa().getCodigo(), false, getUsuarioLogado()));
    	}
        return listaAlunos;
    }

	public List<SelectItem> getListaSelectItemMatriculaAlunoSelecionado() {
		if (listaSelectItemMatriculaAlunoSelecionado == null) {
			listaSelectItemMatriculaAlunoSelecionado = new ArrayList<SelectItem>();
		}
		return listaSelectItemMatriculaAlunoSelecionado;
	}

	public void setListaSelectItemMatriculaAlunoSelecionado(List<SelectItem> listaSelectItemMatriculaAlunoSelecionado) {
		this.listaSelectItemMatriculaAlunoSelecionado = listaSelectItemMatriculaAlunoSelecionado;
	}

	public String getAnoConsulta() {
		if (anoConsulta == null) {
			anoConsulta = UteisData.getAnoAnteriorDataString(new Date());
		}
		return anoConsulta;
	}

	public void setAnoConsulta(String anoConsulta) {
		this.anoConsulta = anoConsulta;
	}
	
	public void alterarFiltroTipoOrigem() {
		getDeclaracaoImpostoRendaRelVO().setMatricula(new MatriculaVO());
		getDeclaracaoImpostoRendaRelVO().setPessoa(new PessoaVO());
		getDeclaracaoImpostoRendaRelVO().setParceiro(new ParceiroVO());
		getListaAnoDeclaracaoImpostoRendaRel().clear();
	}
		
	
	private Boolean considerarDataRecebimentoContaDeclaracaoImpostoRenda;
		
	public Boolean getConsiderarDataRecebimentoContaDeclaracaoImpostoRenda() {
		if(considerarDataRecebimentoContaDeclaracaoImpostoRenda == null) {
			try {
				considerarDataRecebimentoContaDeclaracaoImpostoRenda = ControleAcesso.verificarPermissaoFuncionalidadeUsuario("ConsiderarDataRecebimentoContaDeclaracaoImpostoRenda", getUsuarioLogado());
			} catch (Exception e) {
				considerarDataRecebimentoContaDeclaracaoImpostoRenda =  false;
}
		}
		return considerarDataRecebimentoContaDeclaracaoImpostoRenda;
	}

	public void setConsiderarDataRecebimentoContaDeclaracaoImpostoRenda(
			Boolean considerarDataRecebimentoContaDeclaracaoImpostoRenda) {
		this.considerarDataRecebimentoContaDeclaracaoImpostoRenda = considerarDataRecebimentoContaDeclaracaoImpostoRenda;
	}

	public Boolean getApresentarMaterialDidatico() {
		if (apresentarMaterialDidatico == null) {
			apresentarMaterialDidatico = false;
		}
		return apresentarMaterialDidatico;
	}

	public void setApresentarMaterialDidatico(Boolean apresentarMaterialDidatico) {
		this.apresentarMaterialDidatico = apresentarMaterialDidatico;
	}

	public List<SelectItem> getListaSelectItemTipoTextoPadrao() {
		if(listaSelectItemTipoTextoPadrao == null) {
			listaSelectItemTipoTextoPadrao = new ArrayList<SelectItem>();
		}
		return listaSelectItemTipoTextoPadrao;
	}

	public void setListaSelectItemTipoTextoPadrao(List<SelectItem> listaSelectItemTipoTextoPadrao) {
		this.listaSelectItemTipoTextoPadrao = listaSelectItemTipoTextoPadrao;
	}

	public Integer getTextoPadrao() {
		if (textoPadrao == null) {
			textoPadrao = 0;
		}
		return textoPadrao;
	}

	public void setTextoPadrao(Integer textoPadrao) {
		this.textoPadrao = textoPadrao;
	}

	public String getTextoTMP() {
		if (textoTMP == null) {
			textoTMP = "";
		}
		return textoTMP;
	}

	public void setTextoTMP(String textoTMP) {
		this.textoTMP = textoTMP;
	}

}
