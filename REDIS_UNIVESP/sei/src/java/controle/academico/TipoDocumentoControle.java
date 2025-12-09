package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.TipoDocumentoEquivalenteVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.academico.enumeradores.TipoDocumentacaoEnum;
import negocio.comuns.administrativo.CategoriaGEDVO;
import negocio.comuns.administrativo.enumeradores.TipoIdadeExigidaEnum;
import negocio.comuns.basico.enumeradores.TipoExigenciaDocumentoEnum;
import negocio.comuns.secretaria.enumeradores.TipoUploadArquivoEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.facade.jdbc.academico.TipoDocumento;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas tipoDocumentoForm.jsp tipoDocumentoCons.jsp) com as funcionalidades
 * da classe <code>TipoDocumento</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see TipoDocumento
 * @see TipoDocumentoVO
 */
@Controller("TipoDocumentoControle")
@Scope("viewScope")
@Lazy
public class TipoDocumentoControle extends SuperControle implements Serializable {

	/**
     *
     */
	private static final long serialVersionUID = 8735700487046446632L;
	private TipoDocumentoVO tipoDocumentoVO;
	private TipoDocumentoEquivalenteVO tipoDocumentoEquivalenteVO;
	private List<SelectItem> listaSelectItemEscolaridade;
	private List<SelectItem> listaSelectItemTipoExigencia;
	private List listaSelectItemTipoDeDocumentoSemAtual;
	private List<SelectItem> listaSelectItemCategoriaGED;
	private List<SelectItem> listaSelectItemTipoArquivoUpload;
	private List<SelectItem> listaSelectItemTipoIdadeExigida;
	private List<SelectItem> listaSelectItemTipoDocumentacao;

	public List<SelectItem> getListaSelectItemEscolaridade() {
		if (listaSelectItemEscolaridade == null) {
			listaSelectItemEscolaridade = new ArrayList<SelectItem>(0);
			listaSelectItemEscolaridade.add(new SelectItem("", ""));
			for (NivelFormacaoAcademica escolaridade : NivelFormacaoAcademica.values()) {
				listaSelectItemEscolaridade.add(new SelectItem(escolaridade.getValor(), escolaridade.getDescricao()));
			}
		}
		return listaSelectItemEscolaridade;
	}

	public void setListaSelectItemEscolaridade(List<SelectItem> listaSelectItemEscolaridade) {
		this.listaSelectItemEscolaridade = listaSelectItemEscolaridade;
	}

	public List<SelectItem> getListaSelectItemTipoExigencia() {
		if (listaSelectItemTipoExigencia == null) {
			listaSelectItemTipoExigencia = new ArrayList<SelectItem>(0);
//			listaSelectItemTipoExigencia.add(new SelectItem("", ""));
			listaSelectItemTipoExigencia.add(new SelectItem(TipoExigenciaDocumentoEnum.EXIGENCIA_ALUNO, TipoExigenciaDocumentoEnum.EXIGENCIA_ALUNO.getValorApresentar()));
			listaSelectItemTipoExigencia.add(new SelectItem(TipoExigenciaDocumentoEnum.EXIGENCIA_CURSO, TipoExigenciaDocumentoEnum.EXIGENCIA_CURSO.getValorApresentar()));
			listaSelectItemTipoExigencia.add(new SelectItem(TipoExigenciaDocumentoEnum.EXIGENCIA_INSTITUCIONAL, TipoExigenciaDocumentoEnum.EXIGENCIA_INSTITUCIONAL.getValorApresentar()));
		}
		return listaSelectItemTipoExigencia;
	}

	public void setListaSelectItemTipoExigencia(List<SelectItem> listaSelectItemTipoExigencia) {
		this.listaSelectItemTipoExigencia = listaSelectItemTipoExigencia;
	}

	public TipoDocumentoControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>TipoDocumento</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setTipoDocumentoVO(new TipoDocumentoVO());
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("tipoDocumentoForm");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>TipoDocumento</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		TipoDocumentoVO obj = (TipoDocumentoVO) context().getExternalContext().getRequestMap().get("tipo");
		obj.setNovoObj(Boolean.FALSE);
		setTipoDocumentoVO(obj);
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("tipoDocumentoForm");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>TipoDocumento</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (tipoDocumentoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getTipoDeDocumentoFacade().incluir(tipoDocumentoVO, getUsuarioLogado());
			} else {
				getFacadeFactory().getTipoDeDocumentoFacade().alterar(tipoDocumentoVO, getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoDocumentoForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoDocumentoForm");
		}
	}
	
    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ContaReceberCons.jsp. Define o tipo de consulta a
     * ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
    }

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * TipoDocumentoCons.jsp. Define o tipo de consulta a ser executada, por
	 * meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
	 * Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List<TipoDocumentoVO> objs = new ArrayList<TipoDocumentoVO>(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				if(!Uteis.getIsValorNumerico(getControleConsulta().getValorConsulta())) {
					throw new Exception("Informe apenas valores numéricos.");
	            }
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getTipoDeDocumentoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getTipoDeDocumentoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("identificadorGED")) {
				objs = getFacadeFactory().getTipoDeDocumentoFacade().consultarPorIdentificadorGED(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("categoriaGED")) {
				objs = getFacadeFactory().getTipoDeDocumentoFacade().consultarPorCategoriaGED(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "";
		} catch (Exception e) {
			setListaConsulta(new ArrayList<TipoDocumentoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void adicionarTipoDocumentoEquivalente() throws Exception {
		try {
			if (!getTipoDocumentoVO().getCodigo().equals(0)) {
				getTipoDocumentoEquivalenteVO().setTipoDocumento(getTipoDocumentoVO().getCodigo());
			}
			if (getTipoDocumentoEquivalenteVO().getTipoDocumentoEquivalente().getCodigo().intValue() != 0) {
				Integer campoConsulta = getTipoDocumentoEquivalenteVO().getTipoDocumentoEquivalente().getCodigo();
				TipoDocumentoVO tipoDocumento = getFacadeFactory().getTipoDeDocumentoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				getTipoDocumentoEquivalenteVO().setTipoDocumentoEquivalente(tipoDocumento);
			}
			getTipoDocumentoVO().adicionarObjTipoDocumentoEquivalenteVOs(getTipoDocumentoEquivalenteVO());
			this.setTipoDocumentoEquivalenteVO(new TipoDocumentoEquivalenteVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}	
	

	public void removerTipoDocumentoEquivalente() throws Exception {
		TipoDocumentoEquivalenteVO obj = (TipoDocumentoEquivalenteVO) context().getExternalContext().getRequestMap().get("tipoDocumentoEq");
		getTipoDocumentoVO().excluirObjTipoDocumentoEquivalenteVOs(obj.getTipoDocumentoEquivalente().getCodigo());
		setMensagemID("msg_dados_excluidos");
	}

	public void montarListaSelectItemTipoDeDocumentoSemAtual() throws Exception {
		List resultadoConsulta = getFacadeFactory().getTipoDeDocumentoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		Iterator i = resultadoConsulta.iterator();
		while (i.hasNext()) {
			TipoDocumentoVO t = (TipoDocumentoVO) i.next();
			if (t.getCodigo().intValue() == getTipoDocumentoVO().getCodigo().intValue()) {
				resultadoConsulta.remove(t);
				break;
			}
		}
		List<SelectItem> lista = UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome");
		setListaSelectItemTipoDeDocumentoSemAtual(lista);
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>TipoDocumentoVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getTipoDeDocumentoFacade().excluir(tipoDocumentoVO, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoDocumentoForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoDocumentoForm");
		}
	}

	/**
	 * Rotina responsável por atribui um javascript com o método de mascara para
	 * campos do tipo Data, CPF, CNPJ, etc.
	 */
	public String getMascaraConsulta() {
		return "";
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("identificadorGED", "Identificador GED"));
		itens.add(new SelectItem("categoriaGED", "Categoria GED"));
		itens.add(new SelectItem("codigo", "Codigo"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("tipoDocumentoCons");
	}
	
	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do
	 * backing bean. Garantindo uma melhor atuação do Garbage Coletor do Java. A
	 * mesma é automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		tipoDocumentoVO = null;
	}

	public TipoDocumentoVO getTipoDocumentoVO() {
		if (tipoDocumentoVO == null) {
			tipoDocumentoVO = new TipoDocumentoVO();
		}
		return tipoDocumentoVO;
	}

	public void setTipoDocumentoVO(TipoDocumentoVO tipoDocumentoVO) {
		this.tipoDocumentoVO = tipoDocumentoVO;
	}

	/**
	 * @return the tipoDocumentoEquivalenteVO
	 */
	public TipoDocumentoEquivalenteVO getTipoDocumentoEquivalenteVO() {
		if (tipoDocumentoEquivalenteVO == null) {
			tipoDocumentoEquivalenteVO = new TipoDocumentoEquivalenteVO();
		}
		return tipoDocumentoEquivalenteVO;
	}

	/**
	 * @param tipoDocumentoEquivalenteVO
	 *            the tipoDocumentoEquivalenteVO to set
	 */
	public void setTipoDocumentoEquivalenteVO(TipoDocumentoEquivalenteVO tipoDocumentoEquivalenteVO) {
		this.tipoDocumentoEquivalenteVO = tipoDocumentoEquivalenteVO;
	}

	/**
	 * @return the listaSelectItemTipoDeDocumentoSemAtual
	 */
	public List getListaSelectItemTipoDeDocumentoSemAtual() {
		if (listaSelectItemTipoDeDocumentoSemAtual == null) {
			listaSelectItemTipoDeDocumentoSemAtual = new ArrayList();
		}
		return listaSelectItemTipoDeDocumentoSemAtual;
	}

	/**
	 * @param listaSelectItemTipoDeDocumentoSemAtual
	 *            the listaSelectItemTipoDeDocumentoSemAtual to set
	 */
	public void setListaSelectItemTipoDeDocumentoSemAtual(List listaSelectItemTipoDeDocumentoSemAtual) {
		this.listaSelectItemTipoDeDocumentoSemAtual = listaSelectItemTipoDeDocumentoSemAtual;
	}

	public List<SelectItem> listaSelectItemSexo;
	public List<SelectItem> getListaSelectItemSexo() {
		
		if(listaSelectItemSexo == null){
			listaSelectItemSexo = new ArrayList<SelectItem>(0);
			listaSelectItemSexo.add(new SelectItem("AMBOS", "Ambos"));
			listaSelectItemSexo.add(new SelectItem("HOMEM", "Homem"));
			listaSelectItemSexo.add(new SelectItem("MULHER", "Mulher"));
		}
		return listaSelectItemSexo;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>estadoCivil</code>
	 */
	public List<SelectItem> listaSelectItemEstadoCivil;

	public List<SelectItem> getListaSelectItemEstadoCivil() throws Exception {
		if (listaSelectItemEstadoCivil == null) {
			listaSelectItemEstadoCivil = new ArrayList<SelectItem>(0);			
			Hashtable estadoCivils = (Hashtable) Dominios.getEstadoCivil();
			Enumeration keys = estadoCivils.keys();
			listaSelectItemEstadoCivil.add(new SelectItem("", "Todos"));
			while (keys.hasMoreElements()) {
				String value = (String) keys.nextElement();
				String label = (String) estadoCivils.get(value);
				listaSelectItemEstadoCivil.add(new SelectItem(value, label));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) listaSelectItemEstadoCivil, ordenador);
		}
		return listaSelectItemEstadoCivil;
	}

	public void alterarExtensaoArquivo() {
		if(getTipoDocumentoVO().getTipoUploadArquivo().name().equals(TipoUploadArquivoEnum.DOCUMENTO.name())) {
			getTipoDocumentoVO().setExtensaoArquivo("pdf");
		}else if(getTipoDocumentoVO().getTipoUploadArquivo().name().equals(TipoUploadArquivoEnum.IMAGEM.name())){
			getTipoDocumentoVO().setExtensaoArquivo(getTipoDocumentoVO().getTipoUploadArquivo().getExtensao());
		}else {
			getTipoDocumentoVO().setExtensaoArquivo("jpg, png, JPEG, jpeg, gif, bmp, pdf");
		}
	}
	
	public List<SelectItem> getListaSelectItemTipoArquivoUpload() {
		if (listaSelectItemTipoArquivoUpload == null) {
			listaSelectItemTipoArquivoUpload = new ArrayList<SelectItem>(0);
			listaSelectItemTipoArquivoUpload.add(new SelectItem(TipoUploadArquivoEnum.DOCUMENTO,TipoUploadArquivoEnum.DOCUMENTO.getValorApresentar()));
			listaSelectItemTipoArquivoUpload.add(new SelectItem(TipoUploadArquivoEnum.IMAGEM,TipoUploadArquivoEnum.IMAGEM.getValorApresentar()));
			listaSelectItemTipoArquivoUpload.add(new SelectItem(TipoUploadArquivoEnum.TODOS,TipoUploadArquivoEnum.TODOS.getValorApresentar()));
		}
		return listaSelectItemTipoArquivoUpload;
	}
	
	public void setListaSelectItemTipoArquivoUpload(List<SelectItem> listaSelectItemTipoArquivoUpload) {
		this.listaSelectItemTipoArquivoUpload = listaSelectItemTipoArquivoUpload;
	}
	
	public List<SelectItem> listaSelectItemEstrangeiro;

	public List<SelectItem> getListaSelectItemEstrangeiro() throws Exception {
		if (listaSelectItemEstrangeiro == null) {
			listaSelectItemEstrangeiro = new ArrayList<SelectItem>(0);
			listaSelectItemEstrangeiro.add(new SelectItem("", "Todos"));
			listaSelectItemEstrangeiro.add(new SelectItem("S", "Sim"));
			listaSelectItemEstrangeiro.add(new SelectItem("N", "Não"));			
		}
		return listaSelectItemEstrangeiro;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getListaSelectItemCategoriaGED() {
		if (listaSelectItemCategoriaGED == null || listaSelectItemCategoriaGED.isEmpty()) {
			listaSelectItemCategoriaGED = new ArrayList<>();
			listaSelectItemCategoriaGED.add(new SelectItem(0, ""));
			try {
				List<CategoriaGEDVO> listaCategoriaGED = getFacadeFactory().getCategoriaGEDInterfaceFacade().consultar(Boolean.FALSE, getUsuarioLogado());
				if (!listaCategoriaGED.isEmpty()) {
					for (CategoriaGEDVO categoriaGEDVO : listaCategoriaGED) {
						listaSelectItemCategoriaGED.add(new SelectItem(categoriaGEDVO.getCodigo(), categoriaGEDVO.getDescricao()));
					}
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		return listaSelectItemCategoriaGED;
	}

	public void setListaSelectItemCategoriaGED(List<SelectItem> listaSelectItemCategoriaGED) {
		this.listaSelectItemCategoriaGED = listaSelectItemCategoriaGED;
	}
	
	public Boolean getTipoExisgenciaInstitucional() {
		return getTipoDocumentoVO().getTipoExigenciaDocumento().equals(TipoExigenciaDocumentoEnum.EXIGENCIA_INSTITUCIONAL);
	}
	
	public void inicializarDadosInativacaoCamposTipoDocumento() {
		getTipoDocumentoVO().setContrato(false);
		getTipoDocumentoVO().setUtilizaFuncionario(false);
		getTipoDocumentoVO().setEscolaridade("");
		getTipoDocumentoVO().setTransferencia(false);
		getTipoDocumentoVO().setPortadorDiploma(false);
		getTipoDocumentoVO().setInscricaoProcessoSeletivo(false);
		getTipoDocumentoVO().setEnem(false);
		getTipoDocumentoVO().setReabertura(false);
		getTipoDocumentoVO().setRenovacao(false);
		getTipoDocumentoVO().setDocumentoFrenteVerso(false);
		getTipoDocumentoVO().setPermitirPostagemPortalAluno(false);
		getTipoDocumentoVO().setIdentificadorGED("");
	}

	public List<SelectItem> getListaSelectItemTipoIdadeExigida() {
		if (listaSelectItemTipoIdadeExigida == null) {
			listaSelectItemTipoIdadeExigida = new ArrayList<SelectItem>(0);
			listaSelectItemTipoIdadeExigida.add(new SelectItem(TipoIdadeExigidaEnum.MINIMA, TipoIdadeExigidaEnum.MINIMA.getDescricao()));
			listaSelectItemTipoIdadeExigida.add(new SelectItem(TipoIdadeExigidaEnum.MAXIMA, TipoIdadeExigidaEnum.MAXIMA.getDescricao()));
		}
		return listaSelectItemTipoIdadeExigida;
	}

	public void setListaSelectItemTipoIdadeExigida(List<SelectItem> listaSelectItemTipoIdadeExigida) {
		this.listaSelectItemTipoIdadeExigida = listaSelectItemTipoIdadeExigida;
	}	

	public List<SelectItem> getListaSelectItemTipoDocumentacao() {
		if (listaSelectItemTipoDocumentacao == null) {
			listaSelectItemTipoDocumentacao = new ArrayList<>();
//			listaSelectItemTipoDocumentacao.add(new SelectItem(TipoDocumentacaoEnum.TERMO_RESPONSABILIDADE, TipoDocumentacaoEnum.TERMO_RESPONSABILIDADE.getValorApresentar()));
			listaSelectItemTipoDocumentacao.add(new SelectItem(TipoDocumentacaoEnum.DOCUMENTO_IDENTIDADE_DO_ALUNO, TipoDocumentacaoEnum.DOCUMENTO_IDENTIDADE_DO_ALUNO.getValorApresentar()));
			listaSelectItemTipoDocumentacao.add(new SelectItem(TipoDocumentacaoEnum.PROVA_CONCLUSAO_ENSINO_MEDIO, TipoDocumentacaoEnum.PROVA_CONCLUSAO_ENSINO_MEDIO.getValorApresentar()));
//			listaSelectItemTipoDocumentacao.add(new SelectItem(TipoDocumentacaoEnum.HISTORICO_ESCOLAR, TipoDocumentacaoEnum.HISTORICO_ESCOLAR.getValorApresentar()));
			listaSelectItemTipoDocumentacao.add(new SelectItem(TipoDocumentacaoEnum.PROVA_COLACAO, TipoDocumentacaoEnum.PROVA_COLACAO.getValorApresentar()));
			listaSelectItemTipoDocumentacao.add(new SelectItem(TipoDocumentacaoEnum.COMPROVACAO_ESTAGIO_CURRICULAR, TipoDocumentacaoEnum.COMPROVACAO_ESTAGIO_CURRICULAR.getValorApresentar()));
			listaSelectItemTipoDocumentacao.add(new SelectItem(TipoDocumentacaoEnum.CERTIDAO_NASCIMENTO, TipoDocumentacaoEnum.CERTIDAO_NASCIMENTO.getValorApresentar()));
			listaSelectItemTipoDocumentacao.add(new SelectItem(TipoDocumentacaoEnum.CERTIDAO_CASAMENTO, TipoDocumentacaoEnum.CERTIDAO_CASAMENTO.getValorApresentar()));
			listaSelectItemTipoDocumentacao.add(new SelectItem(TipoDocumentacaoEnum.TITULO_ELEITOR, TipoDocumentacaoEnum.TITULO_ELEITOR.getValorApresentar()));
			listaSelectItemTipoDocumentacao.add(new SelectItem(TipoDocumentacaoEnum.ATO_NATURALIZACAO, TipoDocumentacaoEnum.ATO_NATURALIZACAO.getValorApresentar()));
			listaSelectItemTipoDocumentacao.add(new SelectItem(TipoDocumentacaoEnum.OUTROS, TipoDocumentacaoEnum.OUTROS.getValorApresentar()));
		}
		return listaSelectItemTipoDocumentacao;
	}

	public void setListaSelectItemTipoDocumentacao(List<SelectItem> listaSelectItemTipoDocumentacao) {
		this.listaSelectItemTipoDocumentacao = listaSelectItemTipoDocumentacao;
	}
}
