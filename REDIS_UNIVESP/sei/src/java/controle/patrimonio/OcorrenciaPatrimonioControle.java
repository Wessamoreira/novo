/**
 * 
 */
package controle.patrimonio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.enumeradores.TipoConsultaLocalArmazenamentoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.patrimonio.LocalArmazenamentoVO;
import negocio.comuns.patrimonio.OcorrenciaPatrimonioVO;
import negocio.comuns.patrimonio.PatrimonioUnidadeVO;
import negocio.comuns.patrimonio.TextoPadraoPatrimonioVO;
import negocio.comuns.patrimonio.TipoPatrimonioVO;
import negocio.comuns.patrimonio.enumeradores.TipoOcorrenciaPatrimonioEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;

/**
 * @author Rodrigo Wind
 *
 */
@Controller("OcorrenciaPatrimonioControle")
@Scope("viewScope")
@Lazy

public class OcorrenciaPatrimonioControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -560946162252698196L;
	private OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO;
	private DataModelo controleConsultaLocalArmazenamento;
	private DataModelo controleConsultaPatrimonioUnidade;
	private DataModelo controleConsultaFuncionario;
	private List<SelectItem> listaSelectItemOpcaoConsultaLocalArmazenamento;
	private List<SelectItem> listaSelectItemOpcaoConsultaPatrimonioUnidade;
	private List<SelectItem> listaSelectItemOpcaoConsultaFuncionario;
	private List<SelectItem> listaSelectItemOpcaoConsulta;
	private List<SelectItem> listaSelectItemTextoPadraoPatrimonio;
	private List<SelectItem> listaSelectItemTipoOcorrenciaPatrimonio;
	private TipoConsultaLocalArmazenamentoEnum consultarLocalArmazenamentoPor;
	private TipoOcorrenciaPatrimonioEnum tipoOcorrenciaPatrimonioCons;
	private TextoPadraoPatrimonioVO textoPadraoPatrimonioVO;
	private List<SelectItem> listaSelectItemTipoPatrimonio;
	private PatrimonioUnidadeVO patrimonioUnidadeSelecionadoVO;
	private String usernameLiberarLimiteReserva;
	private String senhaLiberarLimiteReserva;
	private String usernameLiberarReservaForaLimiteDataMaxima;
	private String senhaLiberarReservaForaLimiteDataMaxima;
	private Boolean permiteLiberarReservaAcimaQuantidadeLimitePorRequisitante;
	private Boolean permiteLiberarReservaForaLimiteDataMaxima;
	private boolean visaoProfessor;
	private boolean visaoCoordenador;

	@PostConstruct
	public void init(){
		verificarVisaoCoordenadorProfessor();
	}
	public void persistir() {
		try {
			executarValidacaoSimulacaoVisaoCoordenador();
			if(Uteis.isAtributoPreenchido(getOcorrenciaPatrimonioVO().getTipoOcorrenciaPatrimonio())){
				if (getOcorrenciaPatrimonioVO().getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.RESERVA_UNIDADE)) {
					atualizarDataHoras();
					validarFiltro();
					getOcorrenciaPatrimonioVO().setPatrimonioUnidade(getFacadeFactory().getPatrimonioUnidadeFacade().consultarPatrimonioUnidadePorCodigoBarra(getOcorrenciaPatrimonioVO().getNovoObj() ? getPatrimonioUnidadeSelecionadoVO().getCodigoBarra() : getOcorrenciaPatrimonioVO().getPatrimonioUnidade().getCodigoBarra() , getUnidadeEnsinoLogado(), NivelMontarDados.BASICO, getUsuarioLogado()));
					getOcorrenciaPatrimonioVO().setLocalArmazenamentoOrigem(getOcorrenciaPatrimonioVO().getPatrimonioUnidade().getLocalArmazenamento());
				}
				if (getOcorrenciaPatrimonioVO().getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.RESERVA_LOCAL)) {
					atualizarDataHoras();
					validarFiltro();
					getOcorrenciaPatrimonioVO().setLocalArmazenamentoOrigem(getOcorrenciaPatrimonioVO().getLocalReservado());
				}
			}
			OcorrenciaPatrimonioVO ocorrenciaPatrimonioCloneVO = (OcorrenciaPatrimonioVO) getOcorrenciaPatrimonioVO().clone();
			getFacadeFactory().getOcorrenciaPatrimonioFacade().persistir(getOcorrenciaPatrimonioVO(), true, getPermiteLiberarReservaAcimaQuantidadeLimitePorRequisitante(), getPermiteLiberarReservaForaLimiteDataMaxima(), getUsuarioLogado());
			if (getOcorrenciaPatrimonioVO().getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.RESERVA_UNIDADE)) {
				setOcorrenciaPatrimonioVO(ocorrenciaPatrimonioCloneVO);
				carregarListaUnidadesPratrimonio();
			}else if (getOcorrenciaPatrimonioVO().getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.RESERVA_LOCAL)){
			    setOcorrenciaPatrimonioVO(ocorrenciaPatrimonioCloneVO);
			    carregarListaHorariosLocal();
			}
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluir() {
		try {
			getFacadeFactory().getOcorrenciaPatrimonioFacade().excluir(getOcorrenciaPatrimonioVO(), true, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String novo() {
		try {
		    setOcorrenciaPatrimonioVO(null);
			if(getIsVisaoCoordenador() || getIsVisaoProfessor()){
				getOcorrenciaPatrimonioVO().setSolicitanteEmprestimo(getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false,Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			getOcorrenciaPatrimonioVO().getResponsavelOcorrencia().setCodigo(getUsuarioLogado().getCodigo());
			getOcorrenciaPatrimonioVO().getResponsavelOcorrencia().setNome(getUsuarioLogado().getNome());
			permiteLiberarReservaAcimaQuantidadeLimitePorRequisitante = null;
			permiteLiberarReservaForaLimiteDataMaxima = null;
			verificarVisaoCoordenadorProfessor();
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		if(getIsVisaoProfessor()){
		    return  Uteis.getCaminhoRedirecionamentoNavegacao("ocorrenciaPatrimonioProfessorForm");
		}else if (getIsVisaoCoordenador()) {
		    return  Uteis.getCaminhoRedirecionamentoNavegacao("ocorrenciaPatrimonioCoordenadorForm");
		}else{
		    return  Uteis.getCaminhoRedirecionamentoNavegacao("ocorrenciaPatrimonioForm");
		}
	}

	public String editar() {
		try {
			setOcorrenciaPatrimonioVO((OcorrenciaPatrimonioVO) getRequestMap().get("ocorrenciaPatrimonioCons"));
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		if(getIsVisaoProfessor()){
		    return  Uteis.getCaminhoRedirecionamentoNavegacao("ocorrenciaPatrimonioProfessorForm");
		}else if (getIsVisaoCoordenador()) {
		    return  Uteis.getCaminhoRedirecionamentoNavegacao("ocorrenciaPatrimonioCoordenadorForm");
		}else{
		    return  Uteis.getCaminhoRedirecionamentoNavegacao("ocorrenciaPatrimonioForm");
		}
	}

	public void consultarDados() {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getOcorrenciaPatrimonioFacade().consultarOcorrenciaPatrimonio(getControleConsultaOtimizado().getCampoConsulta(), getControleConsultaOtimizado().getValorConsulta(), getTipoOcorrenciaPatrimonioCons(), getControleConsultaOtimizado().getDataIni(), getControleConsultaOtimizado().getDataFim(), getUnidadeEnsinoLogado(), getUsuarioLogado(), true, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getLoginControle().getPermissaoAcessoMenuVO()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getOcorrenciaPatrimonioFacade().consultarTotalOcorrenciaPatrimonio(getControleConsultaOtimizado().getCampoConsulta(), getControleConsultaOtimizado().getValorConsulta(), getTipoOcorrenciaPatrimonioCons(), getControleConsultaOtimizado().getDataIni(), getControleConsultaOtimizado().getDataFim(), getUnidadeEnsinoLogado(), getUsuarioLogado(), getLoginControle().getPermissaoAcessoMenuVO()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarPaginacaoConsulta(DataScrollEvent dataScrollerEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		consultarDados();
	}

	public String consultar() {
		getControleConsultaOtimizado().getListaConsulta().clear();
		if(getIsVisaoProfessor()){
		    return  Uteis.getCaminhoRedirecionamentoNavegacao("ocorrenciaPatrimonioProfessorCons");
		    
		}else if (getIsVisaoCoordenador()) {
		    return  Uteis.getCaminhoRedirecionamentoNavegacao("ocorrenciaPatrimonioCoordenadorCons");
		}else{
		    return  Uteis.getCaminhoRedirecionamentoNavegacao("ocorrenciaPatrimonioCons");
		}
	}
	
	public void inicializarDadosTipoOcorrencia(){
		try{
			if(Uteis.isAtributoPreenchido(getOcorrenciaPatrimonioVO().getPatrimonioUnidade())){
				inicializarDadosPatrimonioUnidade(getOcorrenciaPatrimonioVO().getPatrimonioUnidade());
			}
			if(getOcorrenciaPatrimonioVO().getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.EMPRESTIMO)){
				getOcorrenciaPatrimonioVO().setDataOcorrencia(new Date());
			}
		}catch(Exception e){
			
		}
	}

	public void realizarDevolucaoPatrimonioUnidade() {
		try {
			getFacadeFactory().getOcorrenciaPatrimonioFacade().realizarRegistroDevolucaoPatrimonio(getOcorrenciaPatrimonioVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	
	public void consultarPatrimonioUnidadePorCodigoBarraUnico() {
		try {			
			inicializarDadosPatrimonioUnidade(getFacadeFactory().getPatrimonioUnidadeFacade().consultarPatrimonioUnidadePorCodigoBarra(getOcorrenciaPatrimonioVO().getPatrimonioUnidade().getCodigoBarra(), getUnidadeEnsinoLogado(), NivelMontarDados.TODOS, getUsuarioLogado()));
		} catch (Exception e) {
			getOcorrenciaPatrimonioVO().getPatrimonioUnidade().setCodigoBarra("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inicializarDadosPatrimonioUnidade(PatrimonioUnidadeVO patrimonioUnidadeVO) {
		try {
			getFacadeFactory().getOcorrenciaPatrimonioFacade().inicializarDadosPatrimonioUnidade(getOcorrenciaPatrimonioVO(), patrimonioUnidadeVO, getUsuarioLogado());
			setControleConsultaPatrimonioUnidade(null);
			limparMensagem();
		} catch (Exception e) {			
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarPatrimonioUnidade() {
		try {
			getControleConsultaPatrimonioUnidade().setLimitePorPagina(10);
			getControleConsultaPatrimonioUnidade().setListaConsulta(getFacadeFactory().getPatrimonioUnidadeFacade().consultar(getControleConsultaPatrimonioUnidade().getCampoConsulta(), getControleConsultaPatrimonioUnidade().getValorConsulta(), false, getUsuarioLogado(), getUnidadeEnsinoLogado(), getControleConsultaPatrimonioUnidade().getLimitePorPagina(), getControleConsultaPatrimonioUnidade().getOffset()));
			getControleConsultaPatrimonioUnidade().setTotalRegistrosEncontrados(getFacadeFactory().getPatrimonioUnidadeFacade().consultarTotalRegistro(getControleConsultaPatrimonioUnidade().getCampoConsulta(), getControleConsultaPatrimonioUnidade().getValorConsulta(), getUnidadeEnsinoLogado()));
			setMensagemID("msg_entre_dados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarPaginacaoConsultaPatrimonioUnidade(DataScrollEvent dataScrollerEvent) throws Exception {
		getControleConsultaPatrimonioUnidade().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaPatrimonioUnidade().setPage(dataScrollerEvent.getPage());
		consultarPatrimonioUnidade();
	}
	
	public void selecionarPatrimonioUnidade() {		
		inicializarDadosPatrimonioUnidade((PatrimonioUnidadeVO) getRequestMap().get("patrimonioUnidadeVOLista"));
		
	}
	
	public void limparPatrimonioUnidade(){
		getOcorrenciaPatrimonioVO().setPatrimonioUnidade(null);
		getOcorrenciaPatrimonioVO().setLocalArmazenamentoOrigem(null);
	}
	
	public void limparLocalArmazenamentoDestino(){
		getOcorrenciaPatrimonioVO().setLocalArmazenamentoDestino(null);
	}
	
	public void limparLocalReservado() {
		getOcorrenciaPatrimonioVO().setLocalReservado(null);
	}
	
	public void consultarLocalArmazenamento() {
		try {
		    	getControleConsultaLocalArmazenamento().setLimitePorPagina(10);
			getControleConsultaLocalArmazenamento().setListaConsulta(getFacadeFactory().getLocalArmazenamentoFacade().consultar(getConsultarLocalArmazenamentoPor(), getControleConsultaLocalArmazenamento().getValorConsulta(), false, getUsuarioLogado(), getUnidadeEnsinoLogado(), getControleConsultaLocalArmazenamento().getLimitePorPagina(), getControleConsultaLocalArmazenamento().getOffset(), true));
			getControleConsultaLocalArmazenamento().setTotalRegistrosEncontrados(getFacadeFactory().getLocalArmazenamentoFacade().consultarTotalRegistro(getConsultarLocalArmazenamentoPor(), getControleConsultaLocalArmazenamento().getValorConsulta(), getUnidadeEnsinoLogado(), true));
			setMensagemID("msg_entre_dados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarPaginacaoConsultaLocalArmazenamento(DataScrollEvent dataScrollerEvent) throws Exception {
		getControleConsultaLocalArmazenamento().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaLocalArmazenamento().setPage(dataScrollerEvent.getPage());
		consultarLocalArmazenamento();
	}
	
	public void selecionarLocalArmazenamento() {		
		getOcorrenciaPatrimonioVO().setLocalArmazenamentoDestino((LocalArmazenamentoVO) getRequestMap().get("localArmazenamentoVOLista"));		
	}
	
	public void selecionarLocalASerRervado() {
		getOcorrenciaPatrimonioVO().setLocalReservado((LocalArmazenamentoVO) getRequestMap().get("localAReservarVO"));
		setLocalASerReservado((LocalArmazenamentoVO) getRequestMap().get("localAReservarVO"));
	}
	public void consultarSolicitanteEmprestimo() {
		try {
			getControleConsultaFuncionario().setLimitePorPagina(10);
			getControleConsultaFuncionario().setListaConsulta(getFacadeFactory().getFuncionarioFacade().consultar(getControleConsultaFuncionario().getValorConsulta(), getControleConsultaFuncionario().getCampoConsulta(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));			
			setMensagemID("msg_entre_dados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	
	public void selecionarSolicitanteEmprestimo() {		
		getOcorrenciaPatrimonioVO().setSolicitanteEmprestimo((FuncionarioVO) getRequestMap().get("funcionarioVO"));		
	}

	public void limparSolicitanteEmprestimo(){
		getOcorrenciaPatrimonioVO().setSolicitanteEmprestimo(null);
	}

	public void realizarImpressaoTextoPadrao() {
		try {
			limparMensagem();
			setFazerDownload(false);
			this.setCaminhoRelatorio("");		
			List<PatrimonioUnidadeVO> patrimonioUnidadeVOs = new ArrayList<PatrimonioUnidadeVO>(0);
			patrimonioUnidadeVOs.add(getOcorrenciaPatrimonioVO().getPatrimonioUnidade());
			this.setCaminhoRelatorio(getFacadeFactory().getTextoPadraoPatrimonioFacade().realizarImpressaoTextoPadraoPatrimonio(getTextoPadraoPatrimonioVO(), getOcorrenciaPatrimonioVO().getPatrimonioUnidade().getPatrimonioVO(), getOcorrenciaPatrimonioVO(), patrimonioUnidadeVOs, null, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			setFazerDownload(true);			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			
		}
	}

	/**
	 * @return the ocorrenciaPatrimonioVO
	 */
	public OcorrenciaPatrimonioVO getOcorrenciaPatrimonioVO() {
		if (ocorrenciaPatrimonioVO == null) {
			ocorrenciaPatrimonioVO = new OcorrenciaPatrimonioVO();
		}
		return ocorrenciaPatrimonioVO;
	}

	/**
	 * @param ocorrenciaPatrimonioVO
	 *            the ocorrenciaPatrimonioVO to set
	 */
	public void setOcorrenciaPatrimonioVO(OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO) {
		this.ocorrenciaPatrimonioVO = ocorrenciaPatrimonioVO;
	}

	/**
	 * @return the controleConsultaLocalArmazenamento
	 */
	public DataModelo getControleConsultaLocalArmazenamento() {
		if (controleConsultaLocalArmazenamento == null) {
			controleConsultaLocalArmazenamento = new DataModelo();
		}
		return controleConsultaLocalArmazenamento;
	}

	/**
	 * @param controleConsultaLocalArmazenamento
	 *            the controleConsultaLocalArmazenamento to set
	 */
	public void setControleConsultaLocalArmazenamento(DataModelo controleConsultaLocalArmazenamento) {
		this.controleConsultaLocalArmazenamento = controleConsultaLocalArmazenamento;
	}

	/**
	 * @return the controleConsultaPatrimonioUnidade
	 */
	public DataModelo getControleConsultaPatrimonioUnidade() {
		if (controleConsultaPatrimonioUnidade == null) {
			controleConsultaPatrimonioUnidade = new DataModelo();
		}
		return controleConsultaPatrimonioUnidade;
	}

	/**
	 * @param controleConsultaPatrimonioUnidade
	 *            the controleConsultaPatrimonioUnidade to set
	 */
	public void setControleConsultaPatrimonioUnidade(DataModelo controleConsultaPatrimonioUnidade) {
		this.controleConsultaPatrimonioUnidade = controleConsultaPatrimonioUnidade;
	}

	/**
	 * @return the controleConsultaFuncionario
	 */
	public DataModelo getControleConsultaFuncionario() {
		if (controleConsultaFuncionario == null) {
			controleConsultaFuncionario = new DataModelo();
		}
		return controleConsultaFuncionario;
	}

	/**
	 * @param controleConsultaFuncionario
	 *            the controleConsultaFuncionario to set
	 */
	public void setControleConsultaFuncionario(DataModelo controleConsultaFuncionario) {
		this.controleConsultaFuncionario = controleConsultaFuncionario;
	}

	/**
	 * @return the listaSelectItemOpcaoConsultaLocalArmazenamento
	 */
	public List<SelectItem> getListaSelectItemOpcaoConsultaLocalArmazenamento() {
		if (listaSelectItemOpcaoConsultaLocalArmazenamento == null) {
			listaSelectItemOpcaoConsultaLocalArmazenamento = new ArrayList<SelectItem>(2);
			listaSelectItemOpcaoConsultaLocalArmazenamento.add(new SelectItem(TipoConsultaLocalArmazenamentoEnum.LOCAL, TipoConsultaLocalArmazenamentoEnum.LOCAL.getValorApresentar()));
			listaSelectItemOpcaoConsultaLocalArmazenamento.add(new SelectItem(TipoConsultaLocalArmazenamentoEnum.LOCAL_SUPERIOR, TipoConsultaLocalArmazenamentoEnum.LOCAL_SUPERIOR.getValorApresentar()));
		}
		return listaSelectItemOpcaoConsultaLocalArmazenamento;
	}

	/**
	 * @param listaSelectItemOpcaoConsultaLocalArmazenamento
	 *            the listaSelectItemOpcaoConsultaLocalArmazenamento to set
	 */
	public void setListaSelectItemOpcaoConsultaLocalArmazenamento(List<SelectItem> listaSelectItemOpcaoConsultaLocalArmazenamento) {
		this.listaSelectItemOpcaoConsultaLocalArmazenamento = listaSelectItemOpcaoConsultaLocalArmazenamento;
	}

	/**
	 * @return the listaSelectItemOpcaoConsultaPatrimonioUnidade
	 */
	public List<SelectItem> getListaSelectItemOpcaoConsultaPatrimonioUnidade() {
		if (listaSelectItemOpcaoConsultaPatrimonioUnidade == null) {
			listaSelectItemOpcaoConsultaPatrimonioUnidade = new ArrayList<SelectItem>(0);
			listaSelectItemOpcaoConsultaPatrimonioUnidade.add(new SelectItem("DESCRICAO", "Descrição"));
			listaSelectItemOpcaoConsultaPatrimonioUnidade.add(new SelectItem("CODIGO_BARRA", "Código Barra"));
			listaSelectItemOpcaoConsultaPatrimonioUnidade.add(new SelectItem("LOCAL_ARMAZENAMENTO", "Local"));
		}
		return listaSelectItemOpcaoConsultaPatrimonioUnidade;
	}

	/**
	 * @param listaSelectItemOpcaoConsultaPatrimonioUnidade
	 *            the listaSelectItemOpcaoConsultaPatrimonioUnidade to set
	 */
	public void setListaSelectItemOpcaoConsultaPatrimonioUnidade(List<SelectItem> listaSelectItemOpcaoConsultaPatrimonioUnidade) {
		this.listaSelectItemOpcaoConsultaPatrimonioUnidade = listaSelectItemOpcaoConsultaPatrimonioUnidade;
	}

	/**
	 * @return the listaSelectItemOpcaoConsultaFuncionario
	 */
	public List<SelectItem> getListaSelectItemOpcaoConsultaFuncionario() {
		if (listaSelectItemOpcaoConsultaFuncionario == null) {
			listaSelectItemOpcaoConsultaFuncionario = new ArrayList<SelectItem>(0);
			listaSelectItemOpcaoConsultaFuncionario.add(new SelectItem("nome", "Nome"));
			listaSelectItemOpcaoConsultaFuncionario.add(new SelectItem("matricula", "Matrícula"));
		}
		return listaSelectItemOpcaoConsultaFuncionario;
	}

	/**
	 * @param listaSelectItemOpcaoConsultaFuncionario
	 *            the listaSelectItemOpcaoConsultaFuncionario to set
	 */
	public void setListaSelectItemOpcaoConsultaFuncionario(List<SelectItem> listaSelectItemOpcaoConsultaFuncionario) {
		this.listaSelectItemOpcaoConsultaFuncionario = listaSelectItemOpcaoConsultaFuncionario;
	}

	/**
	 * @return the listaSelectItemOpcaoConsulta
	 */
	public List<SelectItem> getListaSelectItemOpcaoConsulta() {
		if (listaSelectItemOpcaoConsulta == null) {
			listaSelectItemOpcaoConsulta = new ArrayList<SelectItem>(0);
			listaSelectItemOpcaoConsulta.add(new SelectItem("CODIGO_BARRA", "Código Barra"));
			listaSelectItemOpcaoConsulta.add(new SelectItem("PATRIMONIO", "Patrimônio"));
			listaSelectItemOpcaoConsulta.add(new SelectItem("LOCAL_ARMAZENAMENTO", "Local Armazenamento"));
		}
		return listaSelectItemOpcaoConsulta;
	}

	/**
	 * @param listaSelectItemOpcaoConsulta
	 *            the listaSelectItemOpcaoConsulta to set
	 */
	public void setListaSelectItemOpcaoConsulta(List<SelectItem> listaSelectItemOpcaoConsulta) {
		this.listaSelectItemOpcaoConsulta = listaSelectItemOpcaoConsulta;
	}

	public void montarListaSelectItemTextoPadraoPatrimonio() {
		try {
			List<TextoPadraoPatrimonioVO> textoPadraoPatrimonioVOs = getFacadeFactory().getTextoPadraoPatrimonioFacade().consultarPorSituacaoTipoUsoCombobox(StatusAtivoInativoEnum.ATIVO, getOcorrenciaPatrimonioVO().getTipoOcorrenciaPatrimonio().getTipoUsoTextoPadrao());
			setListaSelectItemTextoPadraoPatrimonio(UtilSelectItem.getListaSelectItem(textoPadraoPatrimonioVOs, "codigo", "nome", false));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * @return the listaSelectItemTextoPadraoPatrimonio
	 */
	public List<SelectItem> getListaSelectItemTextoPadraoPatrimonio() {
		if (listaSelectItemTextoPadraoPatrimonio == null) {
			listaSelectItemTextoPadraoPatrimonio = new ArrayList<SelectItem>();
		}
		return listaSelectItemTextoPadraoPatrimonio;
	}

	/**
	 * @param listaSelectItemTextoPadraoPatrimonio
	 *            the listaSelectItemTextoPadraoPatrimonio to set
	 */
	public void setListaSelectItemTextoPadraoPatrimonio(List<SelectItem> listaSelectItemTextoPadraoPatrimonio) {
		this.listaSelectItemTextoPadraoPatrimonio = listaSelectItemTextoPadraoPatrimonio;
	}

	/**
	 * @return the listaSelectItemTipoOcorrenciaPatrimonio
	 */
	public List<SelectItem> getListaSelectItemTipoOcorrenciaPatrimonio() {
			listaSelectItemTipoOcorrenciaPatrimonio = new ArrayList<SelectItem>(0);
			listaSelectItemTipoOcorrenciaPatrimonio.add(new SelectItem(null, ""));
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirCadastrarOcorrenciaDescarte() && !(getIsVisaoCoordenador() || getIsVisaoProfessor())) {
				listaSelectItemTipoOcorrenciaPatrimonio.add(new SelectItem(TipoOcorrenciaPatrimonioEnum.DESCARTE, TipoOcorrenciaPatrimonioEnum.DESCARTE.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirCadastrarOcorrenciaEmprestimo() && !(getIsVisaoCoordenador() || getIsVisaoProfessor())) {
				listaSelectItemTipoOcorrenciaPatrimonio.add(new SelectItem(TipoOcorrenciaPatrimonioEnum.EMPRESTIMO, TipoOcorrenciaPatrimonioEnum.EMPRESTIMO.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirCadastrarOcorrenciaManutencao() && !(getIsVisaoCoordenador() || getIsVisaoProfessor())) {
				listaSelectItemTipoOcorrenciaPatrimonio.add(new SelectItem(TipoOcorrenciaPatrimonioEnum.MANUTENCAO, TipoOcorrenciaPatrimonioEnum.MANUTENCAO.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirCadastrarOcorrenciaSeparacaoDescarte() && !(getIsVisaoCoordenador() || getIsVisaoProfessor())) {
				listaSelectItemTipoOcorrenciaPatrimonio.add(new SelectItem(TipoOcorrenciaPatrimonioEnum.SEPARAR_DESCARTE, TipoOcorrenciaPatrimonioEnum.SEPARAR_DESCARTE.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirCadastrarOcorrenciaTrocaLocal() && !(getIsVisaoCoordenador() || getIsVisaoProfessor())) {
				listaSelectItemTipoOcorrenciaPatrimonio.add(new SelectItem(TipoOcorrenciaPatrimonioEnum.TROCA_LOCAL, TipoOcorrenciaPatrimonioEnum.TROCA_LOCAL.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirCadastrarOcorrenciaReservaUnidade() || getIsVisaoCoordenador() || getIsVisaoProfessor()) {
				listaSelectItemTipoOcorrenciaPatrimonio.add(new SelectItem(TipoOcorrenciaPatrimonioEnum.RESERVA_UNIDADE, TipoOcorrenciaPatrimonioEnum.RESERVA_UNIDADE.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirCadastrarOcorrenciaReservaLocal() && (getControleConsultaOtimizado().getCampoConsulta() == null || !getControleConsultaOtimizado().getCampoConsulta().equals("PATRIMONIO"))) {
			    listaSelectItemTipoOcorrenciaPatrimonio.add(new SelectItem(TipoOcorrenciaPatrimonioEnum.RESERVA_LOCAL, TipoOcorrenciaPatrimonioEnum.RESERVA_LOCAL.getValorApresentar()));
			}
		return listaSelectItemTipoOcorrenciaPatrimonio;
	}


	/**
	 * @param listaSelectItemTipoOcorrenciaPatrimonio
	 *            the listaSelectItemTipoOcorrenciaPatrimonio to set
	 */
	public void setListaSelectItemTipoOcorrenciaPatrimonio(List<SelectItem> listaSelectItemTipoOcorrenciaPatrimonio) {
		this.listaSelectItemTipoOcorrenciaPatrimonio = listaSelectItemTipoOcorrenciaPatrimonio;
	}

	

	/**
	 * @return the consultarLocalArmazenamentoPor
	 */
	public TipoConsultaLocalArmazenamentoEnum getConsultarLocalArmazenamentoPor() {
		if (consultarLocalArmazenamentoPor == null) {
			consultarLocalArmazenamentoPor = TipoConsultaLocalArmazenamentoEnum.LOCAL;
		}
		return consultarLocalArmazenamentoPor;
	}

	/**
	 * @param consultarLocalArmazenamentoPor the consultarLocalArmazenamentoPor to set
	 */
	public void setConsultarLocalArmazenamentoPor(TipoConsultaLocalArmazenamentoEnum consultarLocalArmazenamentoPor) {
		this.consultarLocalArmazenamentoPor = consultarLocalArmazenamentoPor;
	}

	/**
	 * @return the tipoOcorrenciaPatrimonioCons
	 */
	public TipoOcorrenciaPatrimonioEnum getTipoOcorrenciaPatrimonioCons() {		
		return tipoOcorrenciaPatrimonioCons;
	}

	/**
	 * @param tipoOcorrenciaPatrimonioCons
	 *            the tipoOcorrenciaPatrimonioCons to set
	 */
	public void setTipoOcorrenciaPatrimonioCons(TipoOcorrenciaPatrimonioEnum tipoOcorrenciaPatrimonioCons) {
		this.tipoOcorrenciaPatrimonioCons = tipoOcorrenciaPatrimonioCons;
	}
	
	public TextoPadraoPatrimonioVO getTextoPadraoPatrimonioVO() {
		if(textoPadraoPatrimonioVO == null){
			textoPadraoPatrimonioVO = new TextoPadraoPatrimonioVO();
		}
		return textoPadraoPatrimonioVO;
	}

	public void setTextoPadraoPatrimonioVO(TextoPadraoPatrimonioVO textoPadraoPatrimonioVO) {
		this.textoPadraoPatrimonioVO = textoPadraoPatrimonioVO;
	}	

	public List<SelectItem> tipoConsultaComboFuncionario;

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		if (tipoConsultaComboFuncionario == null) {
			tipoConsultaComboFuncionario = new ArrayList<SelectItem>(0);
			tipoConsultaComboFuncionario.add(new SelectItem("codigo", "Codígo"));
			tipoConsultaComboFuncionario.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboFuncionario;
	}

	public List<SelectItem> getListaSelectItemTipoPatrimonio() {
		if (listaSelectItemTipoPatrimonio == null) {
			listaSelectItemTipoPatrimonio = new ArrayList<SelectItem>(0);
			listaSelectItemTipoPatrimonio.add(new SelectItem(null, ""));
			List<TipoPatrimonioVO> listaTipoPatrimonio;
			try {
				listaTipoPatrimonio = getFacadeFactory().getTipoPatrimonioFacede().consultarPorDescricao("%%", false, getUsuarioLogado());
				listaSelectItemTipoPatrimonio.addAll(UtilSelectItem.getListaSelectItem(listaTipoPatrimonio, "codigo", "descricao", false));
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		return listaSelectItemTipoPatrimonio;
	}

	public void setListaSelectItemTipoPatrimonio(List<SelectItem> listaSelectItemTipoPatrimonio) {
		this.listaSelectItemTipoPatrimonio = listaSelectItemTipoPatrimonio;
	}

	public void atualizarDataHoras() {
		Calendar calData = Calendar.getInstance();
		calData.setTime(getOcorrenciaPatrimonioVO().getDataReserva());
		Calendar calHoraInicio = Calendar.getInstance();
		Calendar calHoraTermino = Calendar.getInstance();
		calHoraInicio.setTime(getOcorrenciaPatrimonioVO().getDataInicioReserva());
		calHoraTermino.setTime(getOcorrenciaPatrimonioVO().getDataTerminoReserva());

		calHoraInicio.set(Calendar.DAY_OF_MONTH, calData.get(Calendar.DAY_OF_MONTH));
		calHoraInicio.set(Calendar.MONTH, calData.get(Calendar.MONTH));
		calHoraInicio.set(Calendar.YEAR, calData.get(Calendar.YEAR));

		calHoraTermino.set(Calendar.DAY_OF_MONTH, calData.get(Calendar.DAY_OF_MONTH));
		calHoraTermino.set(Calendar.MONTH, calData.get(Calendar.MONTH));
		calHoraTermino.set(Calendar.YEAR, calData.get(Calendar.YEAR));

		getOcorrenciaPatrimonioVO().setDataInicioReserva(calHoraInicio.getTime());
		getOcorrenciaPatrimonioVO().setDataTerminoReserva(calHoraTermino.getTime());

	}

	Calendar calHoraInicio = Calendar.getInstance();
	Calendar calHoraTermino = Calendar.getInstance();

	public String getGrafico() {
		StringBuilder stg = new StringBuilder();
		StringBuilder stgLinhasGraficos = new StringBuilder();
		int contador = 0;
		
		stg.append(" $(function () {                               ");
		stg.append(" 	window.chart2 = new Highcharts.Chart({         ");
		stg.append(" 		  chart: {                                 ");
		stg.append(" 	        renderTo: 'container',                 ");
		stg.append(" 	        type: 'columnrange',   zoomType: 'xy',  ");
		stg.append(" 	        inverted: true                         ");
		stg.append(" 	    },                                         ");
		stg.append("       title: {                                    ");
		stg.append("           text: 'Selecione a RESERVA (Reservas)' ");
		stg.append("       },                                          ");
		stg.append("       xAxis: {                                    ");
		stg.append(" categories: [  ");
		
		if (getOcorrenciaPatrimonioVO().getIsApresentarDadosReservaUnidade()) {
			for (PatrimonioUnidadeVO unidade : getPatrimonioUnidadeVOs()) {
				atualizarDataHoras();
				boolean conflitou = false;
				boolean selecionadoHorarioLivre = false;
				stg.append(" '").append(unidade.getCodigoBarra()).append(("',  "));
				if (unidade.getCodigoBarra().equals(patrimonioUnidadeSelecionadoVO.getCodigoBarra())) {
					calHoraInicio.setTime(getOcorrenciaPatrimonioVO().getDataInicioReserva());
					calHoraTermino.setTime(getOcorrenciaPatrimonioVO().getDataTerminoReserva());
					StringBuilder strgHoraInicio = new StringBuilder();
					StringBuilder strgHoraTermino = new StringBuilder();
					strgHoraInicio.append(calHoraInicio.get(Calendar.YEAR)).append(", ").append(calHoraInicio.get(Calendar.MONTH)).append(", ").append(calHoraInicio.get(Calendar.DAY_OF_MONTH)).append(", ").append(calHoraInicio.get(Calendar.HOUR_OF_DAY)).append(", ").append(calHoraInicio.get(Calendar.MINUTE)).append(", ").append(calHoraInicio.get(Calendar.SECOND));
					strgHoraTermino.append(calHoraTermino.get(Calendar.YEAR)).append(", ").append(calHoraTermino.get(Calendar.MONTH)).append(", ").append(calHoraTermino.get(Calendar.DAY_OF_MONTH)).append(", ").append(calHoraTermino.get(Calendar.HOUR_OF_DAY)).append(", ").append(calHoraTermino.get(Calendar.MINUTE)).append(", ").append(calHoraTermino.get(Calendar.SECOND));

					stgLinhasGraficos.append("{x: " + contador + ",low: Date.UTC(" + strgHoraInicio + "),high: Date.UTC(" + strgHoraTermino + "),color: 'blue',tipo: 'Selecionado', nomeUnidadeLocal:'"+unidade.getPatrimonioVO().getDescricao()+"'}, ");
					selecionadoHorarioLivre = true;

				}
				for (OcorrenciaPatrimonioVO ocrrencia : unidade.getListaOcorrencias()) {
					StringBuilder strgHoraInicio = new StringBuilder();
					StringBuilder strgHoraTermino = new StringBuilder();
					calHoraInicio.setTime(ocrrencia.getDataInicioReserva());
					calHoraTermino.setTime(ocrrencia.getDataTerminoReserva());

					strgHoraInicio.append(calHoraInicio.get(Calendar.YEAR)).append(", ").append(calHoraInicio.get(Calendar.MONTH)).append(", ").append(calHoraInicio.get(Calendar.DAY_OF_MONTH)).append(", ").append(calHoraInicio.get(Calendar.HOUR_OF_DAY)).append(", ").append(calHoraInicio.get(Calendar.MINUTE)).append(", ").append(calHoraInicio.get(Calendar.SECOND));
					strgHoraTermino.append(calHoraTermino.get(Calendar.YEAR)).append(", ").append(calHoraTermino.get(Calendar.MONTH)).append(", ").append(calHoraTermino.get(Calendar.DAY_OF_MONTH)).append(", ").append(calHoraTermino.get(Calendar.HOUR_OF_DAY)).append(", ").append(calHoraTermino.get(Calendar.MINUTE)).append(", ").append(calHoraTermino.get(Calendar.SECOND));

					if (!(Uteis.getDataJDBCTimestamp(getOcorrenciaPatrimonioVO().getDataInicioReserva()).after(ocrrencia.getDataTerminoReserva()) || Uteis.getDataJDBCTimestamp(getOcorrenciaPatrimonioVO().getDataTerminoReserva()).before(ocrrencia.getDataInicioReserva()))) {
						conflitou = true;
					}
					stgLinhasGraficos.append("{x: " + contador + ",low: Date.UTC(" + strgHoraInicio + "),high: Date.UTC(" + strgHoraTermino + "),color: 'red',tipo: 'Ocupado', nomeUnidadeLocal:'"+unidade.getPatrimonioVO().getDescricao()+"'}, ");
				}
				if (!conflitou && !selecionadoHorarioLivre) {
					calHoraInicio.setTime(getOcorrenciaPatrimonioVO().getDataInicioReserva());
					calHoraTermino.setTime(getOcorrenciaPatrimonioVO().getDataTerminoReserva());
					StringBuilder strgHoraInicio = new StringBuilder();
					StringBuilder strgHoraTermino = new StringBuilder();
					strgHoraInicio.append(calHoraInicio.get(Calendar.YEAR)).append(", ").append(calHoraInicio.get(Calendar.MONTH)).append(", ").append(calHoraInicio.get(Calendar.DAY_OF_MONTH)).append(", ").append(calHoraInicio.get(Calendar.HOUR_OF_DAY)).append(", ").append(calHoraInicio.get(Calendar.MINUTE)).append(", ").append(calHoraInicio.get(Calendar.SECOND));
					strgHoraTermino.append(calHoraTermino.get(Calendar.YEAR)).append(", ").append(calHoraTermino.get(Calendar.MONTH)).append(", ").append(calHoraTermino.get(Calendar.DAY_OF_MONTH)).append(", ").append(calHoraTermino.get(Calendar.HOUR_OF_DAY)).append(", ").append(calHoraTermino.get(Calendar.MINUTE)).append(", ").append(calHoraTermino.get(Calendar.SECOND));

					stgLinhasGraficos.append("{x: " + contador + ",low: Date.UTC(" + strgHoraInicio + "),high: Date.UTC(" + strgHoraTermino + "),color: 'green',tipo: 'Livre', nomeUnidadeLocal:'"+unidade.getPatrimonioVO().getDescricao()+"'}, ");
				}
				contador++;
			}
		}else if(getOcorrenciaPatrimonioVO().getIsApresentarDadosReservaLocal() && getOcorrenciaPatrimonioVO().getLocalReservado().getCodigo() != 0) {
		    	LocalArmazenamentoVO local = getLocalASerReservado();
			carregarListaHorariosLocal();
			boolean conflitou = false;
			boolean selecionadoHorarioLivre = false;
			stg.append(" '").append(local.getCodigo()).append(("',  "));
			
			for(OcorrenciaPatrimonioVO ocrrencia : local.getListaOcorrencias()){
				calHoraInicio.setTime(ocrrencia.getDataInicioReserva());
				calHoraTermino.setTime(ocrrencia.getDataTerminoReserva());
				StringBuilder strgHoraInicio = new StringBuilder();
				StringBuilder strgHoraTermino = new StringBuilder();
				strgHoraInicio.append(calHoraInicio.get(Calendar.YEAR)).append(", ").append(calHoraInicio.get(Calendar.MONTH)).append(", ").append(calHoraInicio.get(Calendar.DAY_OF_MONTH)).append(", ").append(calHoraInicio.get(Calendar.HOUR_OF_DAY)).append(", ").append(calHoraInicio.get(Calendar.MINUTE)).append(", ").append(calHoraInicio.get(Calendar.SECOND));
				strgHoraTermino.append(calHoraTermino.get(Calendar.YEAR)).append(", ").append(calHoraTermino.get(Calendar.MONTH)).append(", ").append(calHoraTermino.get(Calendar.DAY_OF_MONTH)).append(", ").append(calHoraTermino.get(Calendar.HOUR_OF_DAY)).append(", ").append(calHoraTermino.get(Calendar.MINUTE)).append(", ").append(calHoraTermino.get(Calendar.SECOND));

				if (!(Uteis.getDataJDBCTimestamp(getOcorrenciaPatrimonioVO().getDataInicioReserva()).after(ocrrencia.getDataTerminoReserva()) || Uteis.getDataJDBCTimestamp(getOcorrenciaPatrimonioVO().getDataTerminoReserva()).before(ocrrencia.getDataInicioReserva()))) {
					conflitou = true;
				}
				stgLinhasGraficos.append("{x: " + contador + ",low: Date.UTC(" + strgHoraInicio + "),high: Date.UTC(" + strgHoraTermino + "),color: 'red',tipo: 'Ocupado', nomeUnidadeLocal:'"+local.getLocalArmazenamento()+"'}, ");
			
			}
			if (!conflitou && !selecionadoHorarioLivre) {
				calHoraInicio.setTime(getOcorrenciaPatrimonioVO().getDataInicioReserva());
				calHoraTermino.setTime(getOcorrenciaPatrimonioVO().getDataTerminoReserva());
				StringBuilder strgHoraInicio = new StringBuilder();
				StringBuilder strgHoraTermino = new StringBuilder();
				strgHoraInicio.append(calHoraInicio.get(Calendar.YEAR)).append(", ").append(calHoraInicio.get(Calendar.MONTH)).append(", ").append(calHoraInicio.get(Calendar.DAY_OF_MONTH)).append(", ").append(calHoraInicio.get(Calendar.HOUR_OF_DAY)).append(", ").append(calHoraInicio.get(Calendar.MINUTE)).append(", ").append(calHoraInicio.get(Calendar.SECOND));
				strgHoraTermino.append(calHoraTermino.get(Calendar.YEAR)).append(", ").append(calHoraTermino.get(Calendar.MONTH)).append(", ").append(calHoraTermino.get(Calendar.DAY_OF_MONTH)).append(", ").append(calHoraTermino.get(Calendar.HOUR_OF_DAY)).append(", ").append(calHoraTermino.get(Calendar.MINUTE)).append(", ").append(calHoraTermino.get(Calendar.SECOND));

				stgLinhasGraficos.append("{x: " + contador + ",low: Date.UTC(" + strgHoraInicio + "),high: Date.UTC(" + strgHoraTermino + "),color: 'blue',tipo: 'Selecionado', nomeUnidadeLocal:'"+local.getLocalArmazenamento()+"'}, ");
			}
			
			/*calHoraInicio.setTime(getOcorrenciaPatrimonioVO().getDataInicioReserva());
			calHoraTermino.setTime(getOcorrenciaPatrimonioVO().getDataTerminoReserva());
			StringBuilder strgHoraInicio = new StringBuilder();
			StringBuilder strgHoraTermino = new StringBuilder();
			strgHoraInicio.append(calHoraInicio.get(Calendar.YEAR)).append(", ").append(calHoraInicio.get(Calendar.MONTH)).append(", ").append(calHoraInicio.get(Calendar.DAY_OF_MONTH)).append(", ").append(calHoraInicio.get(Calendar.HOUR_OF_DAY)).append(", ").append(calHoraInicio.get(Calendar.MINUTE)).append(", ").append(calHoraInicio.get(Calendar.SECOND));
			strgHoraTermino.append(calHoraTermino.get(Calendar.YEAR)).append(", ").append(calHoraTermino.get(Calendar.MONTH)).append(", ").append(calHoraTermino.get(Calendar.DAY_OF_MONTH)).append(", ").append(calHoraTermino.get(Calendar.HOUR_OF_DAY)).append(", ").append(calHoraTermino.get(Calendar.MINUTE)).append(", ").append(calHoraTermino.get(Calendar.SECOND));
			stgLinhasGraficos.append("{x: " + contador + ",low: Date.UTC(" + strgHoraInicio + "),high: Date.UTC(" + strgHoraTermino + "),color: 'green',tipo: 'Livre'}, ");*/

		}
		
		stg.append(" ],  ");
		stg.append(" 	min: 0,      ");
	 	stg.append(" 	 max: 5,     ");
	    stg.append("  scrollbar: {   ");
	    stg.append("   enabled: true ");
	 	stg.append(" 	 }           ");
		stg.append("       },                                          ");
		stg.append("       yAxis: {                                    ");
		stg.append("           type: 'datetime',                       ");
		stg.append("           title: { text: 'Horário' }              ");
		stg.append("       },                                          ");
		stg.append("  	    legend: { enabled: false },                ");
		stg.append("       exporting: { enabled: false },              ");
		stg.append("       credits: { enabled: false },                ");
		stg.append(" 			tooltip: {                             ");
		stg.append("             formatter: function () {              ");
		stg.append("                 console.log(this);                ");
		stg.append("                 return '<b>  ' + this.x + ' - ' +this.point.nomeUnidadeLocal + '</b><br/><b>'" + " + this.point.tipo + '</b> no período entre <b>'" + " + Highcharts.dateFormat('%H:%M', this.point.low)" + " + '</b> e <b>'" + " + 	Highcharts.dateFormat('%H:%M', this.point.high) + '</b>'; ");
		stg.append("             }                                             ");
		stg.append("         },                                                ");
		stg.append(" plotOptions : {                                                  ");
		stg.append(" 	columnrange: {                                                ");
		stg.append("   	point: {                                                      ");
		stg.append("     	events: {                                                 ");
		stg.append("       	click:function(e) {                                       ");
		stg.append("         if(this.tipo === 'Livre'){                		          ");
		stg.append("         	selecaoUnidade(this.series.xAxis.categories[this.x]); ");
		stg.append("         }                                                        ");
		stg.append("        }                                                         ");
		stg.append("       }                                                          ");
		stg.append("     }                                                            ");
		stg.append("   }                                                              ");
		stg.append(" },																  ");
		stg.append(" 	    series: [{                                         ");
		stg.append(" 	        name: 'Teste',                                 ");
		stg.append("             data: [" + stgLinhasGraficos + "] 			   ");
		stg.append(" 	    }]                                                 ");
		stg.append(" 		});                                                ");
		stg.append(" });                                                       ");

		return stg.toString();
	}

	
	private List<PatrimonioUnidadeVO> patrimonioUnidadeVOs;

	public List<PatrimonioUnidadeVO> getPatrimonioUnidadeVOs() {
		if (patrimonioUnidadeVOs == null) {
			patrimonioUnidadeVOs = new ArrayList<PatrimonioUnidadeVO>(0);
		}
		return patrimonioUnidadeVOs;
	}

	public void setPatrimonioUnidadeVOs(List<PatrimonioUnidadeVO> patrimonioUnidadeVOs) {
		this.patrimonioUnidadeVOs = patrimonioUnidadeVOs;
	}


	public PatrimonioUnidadeVO getPatrimonioUnidadeSelecionadoVO() {
		if (patrimonioUnidadeSelecionadoVO == null) {
			patrimonioUnidadeSelecionadoVO = new PatrimonioUnidadeVO();
		}
		return patrimonioUnidadeSelecionadoVO;
	}

	public void setPatrimonioUnidadeSelecionadoVO(PatrimonioUnidadeVO patrimonioUnidadeSelecionadoVO) {
		this.patrimonioUnidadeSelecionadoVO = patrimonioUnidadeSelecionadoVO;
	}

	public void carregarListaUnidadesPratrimonio() {
		try {
			getPatrimonioUnidadeVOs().clear();
			atualizarDataHoras();
			validarFiltro();
			if (getOcorrenciaPatrimonioVO().getTipoPatrimonioVO().getCodigo() == 0) {
				throw new ConsistirException("Para a pesquisa de Disponibilidade de Unidade o Tipo Patrimonio é Obrigatório");
			}

			getPatrimonioUnidadeVOs().addAll(getFacadeFactory().getPatrimonioUnidadeFacade().consultarPorTipoPatrimonioParaListagemDeOcorrenciasPorUnidade(getOcorrenciaPatrimonioVO().getTipoPatrimonioVO().getCodigo(), getOcorrenciaPatrimonioVO().getDataInicioReserva(), getOcorrenciaPatrimonioVO().getLocalArmazenamentoDestino().getUnidadeEnsinoVO()));
			setPatrimonioUnidadeSelecionadoVO(new PatrimonioUnidadeVO());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	private LocalArmazenamentoVO localASerReservado;
	

	public LocalArmazenamentoVO getLocalASerReservado() {
		if(localASerReservado == null){
			localASerReservado = new LocalArmazenamentoVO();
		}
		return localASerReservado;
	}

	public void setLocalASerReservado(LocalArmazenamentoVO localASerReservado) {
		this.localASerReservado = localASerReservado;
	}

	public void carregarListaHorariosLocal() {
		try {
			atualizarDataHoras();
			validarFiltro();
			if (getOcorrenciaPatrimonioVO().getLocalReservado().getCodigo() == 0) {
				throw new ConsistirException("Para a pesquisa de Disponibilidade de Horários o LOCAL é Obrigatório");
			}
			getLocalASerReservado().setListaOcorrencias(getFacadeFactory().getOcorrenciaPatrimonioFacade().consultarOcorrenciaPatrimonioPorLocalReservado(getLocalASerReservado().getCodigo(), getOcorrenciaPatrimonioVO().getDataInicioReserva()));;
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	private void validarFiltro() throws Exception {
		/*if(getOcorrenciaPatrimonioVO().getDataInicioReserva().before(new Date()) || getOcorrenciaPatrimonioVO().getDataTerminoReserva().before(new Date())){
			throw new Exception("Não é permitido Reservas com data menor que a Atual");
		}*/
		if (getOcorrenciaPatrimonioVO().getDataInicioReserva().after(getOcorrenciaPatrimonioVO().getDataTerminoReserva())) {
			throw new Exception("A Data ( Hora ) Inicial não pode ser DEPOIS da Data Final ");
		}
		
		if ((getOcorrenciaPatrimonioVO().getIsTipoOcorrenciaEmprestimo() || ocorrenciaPatrimonioVO.getIsApresentarDadosReservaUnidade()) && !Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getSolicitanteEmprestimo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_OcorrenciaPatrimonio_solicitanteEmprestimo"));
		}
		if (getOcorrenciaPatrimonioVO().getIsApresentarLocalArmazenamentoDestino() && !Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getLocalArmazenamentoDestino())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_OcorrenciaPatrimonio_localArmazenamentoDestino"));
		}

	}

	public String getUsernameLiberarLimiteReserva() {
		if(usernameLiberarLimiteReserva == null){
			usernameLiberarLimiteReserva = "";
		}
		return usernameLiberarLimiteReserva;
	}

	public void setUsernameLiberarLimiteReserva(String usernameLiberarLimiteReserva) {
		this.usernameLiberarLimiteReserva = usernameLiberarLimiteReserva;
	}

	public String getSenhaLiberarLimiteReserva() {
		if(senhaLiberarLimiteReserva == null){
			senhaLiberarLimiteReserva = "";
		}
		return senhaLiberarLimiteReserva;
	}

	public void setSenhaLiberarLimiteReserva(String senhaLiberarLimiteReserva) {
		this.senhaLiberarLimiteReserva = senhaLiberarLimiteReserva;
	}

	public void realizarVerificacaoUsuarioPossuiPermissaoLiberarReservaAcimaQuantidadeLimitePorRequisitante() throws Exception {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberarLimiteReserva(), this.getSenhaLiberarLimiteReserva(), true, Uteis.NIVELMONTARDADOS_TODOS);
			verificarPermissaoUsuario(usuarioVerif, "PermissaoUsuarioLiberarReservaAcimaQuantidadeLimitePorRequisitante");
			setPermiteLiberarReservaAcimaQuantidadeLimitePorRequisitante(true);
			setMensagemID("msg_funcionalidadeLiberadaComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setPermiteLiberarReservaAcimaQuantidadeLimitePorRequisitante(false);
		} finally {
			setUsernameLiberarLimiteReserva("");
			setSenhaLiberarLimiteReserva("");
		}
	}
	
	public void realizarVerificacaoUsuarioPossuiPermissaoLiberarReservaForaLimiteDataMaxima() throws Exception {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberarReservaForaLimiteDataMaxima(), this.getSenhaLiberarReservaForaLimiteDataMaxima(), true, Uteis.NIVELMONTARDADOS_TODOS);
			verificarPermissaoUsuario(usuarioVerif, "permissaoUsuarioLiberarReservaForaLimiteDataMaxima");
			setPermiteLiberarReservaForaLimiteDataMaxima(true);
			setMensagemID("msg_funcionalidadeLiberadaComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setPermiteLiberarReservaForaLimiteDataMaxima(false);
		} finally {
			setUsernameLiberarReservaForaLimiteDataMaxima("");
			setSenhaLiberarReservaForaLimiteDataMaxima("");
		}
	}
	
	public static void verificarPermissaoUsuario(UsuarioVO usuario, String nomeEntidade) throws Exception {
		ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(nomeEntidade, usuario);
	}

	public boolean getPermiteLiberarReservaAcimaQuantidadeLimitePorRequisitante() {
		if(permiteLiberarReservaAcimaQuantidadeLimitePorRequisitante == null){
			permiteLiberarReservaAcimaQuantidadeLimitePorRequisitante = false;
		}
		return permiteLiberarReservaAcimaQuantidadeLimitePorRequisitante;
	}

	public void setPermiteLiberarReservaAcimaQuantidadeLimitePorRequisitante(boolean permiteLiberarReservaAcimaQuantidadeLimitePorRequisitante) {
		this.permiteLiberarReservaAcimaQuantidadeLimitePorRequisitante = permiteLiberarReservaAcimaQuantidadeLimitePorRequisitante;
	}

	public boolean getPermiteLiberarReservaForaLimiteDataMaxima() {
		if(permiteLiberarReservaForaLimiteDataMaxima == null){
			permiteLiberarReservaForaLimiteDataMaxima = false;
		}
		return permiteLiberarReservaForaLimiteDataMaxima;
	}

	public void setPermiteLiberarReservaForaLimiteDataMaxima(boolean permiteLiberarReservaForaLimiteDataMaxima) {
		this.permiteLiberarReservaForaLimiteDataMaxima = permiteLiberarReservaForaLimiteDataMaxima;
	}

	public String getUsernameLiberarReservaForaLimiteDataMaxima() {
		if(usernameLiberarReservaForaLimiteDataMaxima == null){
			usernameLiberarReservaForaLimiteDataMaxima = "";
		}
		return usernameLiberarReservaForaLimiteDataMaxima;
	}

	public void setUsernameLiberarReservaForaLimiteDataMaxima(String usernameLiberarReservaForaLimiteDataMaxima) {
		this.usernameLiberarReservaForaLimiteDataMaxima = usernameLiberarReservaForaLimiteDataMaxima;
	}

	public String getSenhaLiberarReservaForaLimiteDataMaxima() {
		if(senhaLiberarReservaForaLimiteDataMaxima == null){
			senhaLiberarReservaForaLimiteDataMaxima = "";
		}
		return senhaLiberarReservaForaLimiteDataMaxima;
	}

	public void setSenhaLiberarReservaForaLimiteDataMaxima(String senhaLiberarReservaForaLimiteDataMaxima) {
		this.senhaLiberarReservaForaLimiteDataMaxima = senhaLiberarReservaForaLimiteDataMaxima;
	}
	
	
	
	public void verificarVisaoCoordenadorProfessor() {
		try {
			if (getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
				setVisaoCoordenador(true);
			}else if(getUsuarioLogado().getVisaoLogar().equals("professor")){
				setVisaoProfessor(true);
			}
		} catch (Exception e) {
			setVisaoProfessor(false);
			setVisaoCoordenador(false);
		}
	}
	

	public boolean  getIsVisaoProfessor() {
		return visaoProfessor;
	}

	public void setVisaoProfessor(boolean visaoProfessor) {
		this.visaoProfessor = visaoProfessor;
	}

	public boolean getIsVisaoCoordenador() {
		return visaoCoordenador;
	}

	public void setVisaoCoordenador(boolean visaoCoordenador) {
		this.visaoCoordenador = visaoCoordenador;
	}
	
	
}
