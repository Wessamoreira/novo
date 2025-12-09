package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas configuracaoAcademicoForm.jsp configuracaoAcademicoCons.jsp) com as funcionalidades da classe
 * <code>ConfiguracaoAcademico</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see ConfiguracaoAcademico
 * @see ConfiguracaoAcademicoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.ConfiguracaoControleInterface;
import controle.arquitetura.SuperControle;
import controle.basico.ConfiguracoesControle;
import jobs.enumeradores.TipoUsoNotaEnum;
import negocio.comuns.academico.ConfiguracaoAcademicaNotaVO;
import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.ExpedicaoDiplomaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoNotaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.FormulaCalculoNotaEnum;
import negocio.comuns.academico.enumeradores.RegraCalculoDisciplinaCompostaEnum;
import negocio.comuns.academico.enumeradores.TipoCalculoCargaHorariaFrequencia;
import negocio.comuns.academico.enumeradores.TipoControleComposicaoEnum;
import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.comuns.academico.enumeradores.TipoUsoConfiguracaoAcademicoEnum;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.basico.enumeradores.LayoutComprovanteMatriculaEnum;
import negocio.comuns.secretaria.enumeradores.TipoAlteracaoSituacaoHistoricoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.academico.ConfiguracaoAcademico;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas configuracaoAcademicoForm.jsp configuracaoAcademicoCons.jsp) com as funcionalidades da classe
 * <code>ConfiguracaoAcademico</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see ConfiguracaoAcademico
 * @see ConfiguracaoAcademicoVO
 */


@Controller("ConfiguracaoAcademicoControle")
@Scope("viewScope")
@Lazy
public class ConfiguracaoAcademicoControle extends SuperControle implements Serializable, ConfiguracaoControleInterface {

	private ConfiguracaoAcademicoVO configuracaoAcademicoVO;
	private List<ConfiguracaoAcademicoVO> configuracaoAcademicoVOs;
	private String padrao;
	private ConfiguracoesControle configuracoesControle;
	private ConfiguracoesVO configuracoesVO;
	private Boolean editandoAPartirFormConfiguracores;
	private List<SelectItem> listaSelectItemLayoutPadraoComprovanteMatricula;
	private List<SelectItem> listaSelectItemTipoControleAvancoPeriodoPorCreditoOuCH;
	private List<SelectItem> listaSelectItemTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH;

	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO1;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO2;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO3;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO4;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO5;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO6;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO7;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO8;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO9;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO10;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO11;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO12;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO13;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO14;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO15;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO16;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO17;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO18;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO19;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO20;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO21;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO22;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO23;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO24;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO25;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO26;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO27;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO28;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO29;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO30;
	
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO31;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO32;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO33;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO34;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO35;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO36;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO37;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO38;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO39;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO40;
	
	/**
	 * @author Felipe
	 *  
	 *  @apiNote Filreos de boolean de notas
	 *  
	 * @return
	 */
	private Boolean filtrarNotas;
	private Boolean filtrarNotas2;
	private Boolean filtrarNotas3;
	private Boolean filtrarNotas4;
	private Boolean filtrarNotas5;
	private Boolean filtrarNotas6;
	private Boolean filtrarNotas7;
	private Boolean filtrarNotas8;
	private Boolean filtrarNotas9;
	private Boolean filtrarNotas10;
	
	private List<SelectItem> listaSelectItemNota1Conceito;
	private List<SelectItem> listaSelectItemNota2Conceito;
	private List<SelectItem> listaSelectItemNota3Conceito;
	private List<SelectItem> listaSelectItemNota4Conceito;
	private List<SelectItem> listaSelectItemNota5Conceito;
	private List<SelectItem> listaSelectItemNota6Conceito;
	private List<SelectItem> listaSelectItemNota7Conceito;
	private List<SelectItem> listaSelectItemNota8Conceito;
	private List<SelectItem> listaSelectItemNota9Conceito;
	private List<SelectItem> listaSelectItemNota10Conceito;
	private List<SelectItem> listaSelectItemNota11Conceito;
	private List<SelectItem> listaSelectItemNota12Conceito;
	private List<SelectItem> listaSelectItemNota13Conceito;
	private List<SelectItem> listaSelectItemNota14Conceito;
	private List<SelectItem> listaSelectItemNota15Conceito;
	private List<SelectItem> listaSelectItemNota16Conceito;
	private List<SelectItem> listaSelectItemNota17Conceito;
	private List<SelectItem> listaSelectItemNota18Conceito;
	private List<SelectItem> listaSelectItemNota19Conceito;
	private List<SelectItem> listaSelectItemNota20Conceito;
	private List<SelectItem> listaSelectItemNota21Conceito;
	private List<SelectItem> listaSelectItemNota22Conceito;
	private List<SelectItem> listaSelectItemNota23Conceito;
	private List<SelectItem> listaSelectItemNota24Conceito;
	private List<SelectItem> listaSelectItemNota25Conceito;
	private List<SelectItem> listaSelectItemNota26Conceito;
	private List<SelectItem> listaSelectItemNota27Conceito;
	private List<SelectItem> listaSelectItemNota28Conceito;
	private List<SelectItem> listaSelectItemNota29Conceito;
	private List<SelectItem> listaSelectItemNota30Conceito;
	
	private List<SelectItem> listaSelectItemNota31Conceito;
	private List<SelectItem> listaSelectItemNota32Conceito;
	private List<SelectItem> listaSelectItemNota33Conceito;
	private List<SelectItem> listaSelectItemNota34Conceito;
	private List<SelectItem> listaSelectItemNota35Conceito;
	private List<SelectItem> listaSelectItemNota36Conceito;
	private List<SelectItem> listaSelectItemNota37Conceito;
	private List<SelectItem> listaSelectItemNota38Conceito;
	private List<SelectItem> listaSelectItemNota39Conceito;
	private List<SelectItem> listaSelectItemNota40Conceito;

	private List<SelectItem> listaGrupoDestinatario;
	private List<HistoricoVO> historicoFilhaComposicaoVOs;
	private Boolean abrirModalDefinicaoDisciplinaFilhaComposicao;
    private String valorCampoMascaraNumeroProcessoExpedicaoDiploma;
    private List<String> listaNumeroProcessoExpedicaoDiploma1;
    private List<String> listaNumeroProcessoExpedicaoDiploma2;
    
    private String valorCampoMascaraNumeroRegistroDiploma;
    private List<String> listaNumeroRegistroDiploma1;
    private List<String> listaNumeroRegistroDiploma2;
    
	public ConfiguracaoAcademicoControle() throws Exception {
		// obterUsuarioLogado();
		novo();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}
	
	
	
	/**
	 * @author Geber
	 * 
	 * Método para recuperar a configuração criada em configuracoesForm.xhtml, pelo fato do ConfiguracaoAcademicoControle ser instanciado por outro controle (ConfiguraçõesControle)
	 * foi necessário criar essa função, pois após a navegação da página o esse controlador era instanciado e os dados perdidos. 
	 * Após recuperar o objeto da sessão, é setado no atributo configuraçõesVO e removido da sessão.
	 * 
	 */
	@PostConstruct
	private void recuperarConfiguracaoPeloForm(){
		if(context().getExternalContext().getSessionMap().get("configuracoesItem") != null) {
			setConfiguracoesVO((ConfiguracoesVO) context().getExternalContext().getSessionMap().get("configuracoesItem"));
			context().getExternalContext().getSessionMap().remove("configuracoesItem");
			editar();
		}	
	}

	public void iniciarControleConfiguracao(ConfiguracoesVO configuracoesVO, ConfiguracoesControle configuracoesControle) throws Exception {
		setConfiguracoesControle(configuracoesControle);
		setConfiguracoesVO(configuracoesVO);
		setConfiguracaoAcademicoVOs(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorCodigoConfiguracoes(configuracoesVO.getCodigo(), false, getUsuarioLogado()));
		novo();
	}

	public void limparCamposParaClone() {
		for (ConfiguracaoAcademicoVO config : getConfiguracaoAcademicoVOs()) {
			config.setCodigo(0);
			config.setNovoObj(true);
			config.setConfiguracoesVO(new ConfiguracoesVO());
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito1() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO1(), TipoNotaConceitoEnum.NOTA_1, getConfiguracaoAcademicoVO().getNota1MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO1(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito2() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO2(), TipoNotaConceitoEnum.NOTA_2, getConfiguracaoAcademicoVO().getNota2MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO2(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito3() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO3(), TipoNotaConceitoEnum.NOTA_3, getConfiguracaoAcademicoVO().getNota3MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO3(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito4() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO4(), TipoNotaConceitoEnum.NOTA_4, getConfiguracaoAcademicoVO().getNota4MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO4(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito5() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO5(), TipoNotaConceitoEnum.NOTA_5, getConfiguracaoAcademicoVO().getNota5MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO5(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito6() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO6(), TipoNotaConceitoEnum.NOTA_6, getConfiguracaoAcademicoVO().getNota6MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO6(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito7() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO7(), TipoNotaConceitoEnum.NOTA_7, getConfiguracaoAcademicoVO().getNota7MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO7(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito8() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO8(), TipoNotaConceitoEnum.NOTA_8, getConfiguracaoAcademicoVO().getNota8MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO8(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito9() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO9(), TipoNotaConceitoEnum.NOTA_9, getConfiguracaoAcademicoVO().getNota9MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO9(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito10() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO10(), TipoNotaConceitoEnum.NOTA_10, getConfiguracaoAcademicoVO().getNota10MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO10(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito11() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO11(), TipoNotaConceitoEnum.NOTA_11, getConfiguracaoAcademicoVO().getNota11MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO11(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito12() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO12(), TipoNotaConceitoEnum.NOTA_12, getConfiguracaoAcademicoVO().getNota12MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO12(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito13() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO13(), TipoNotaConceitoEnum.NOTA_13, getConfiguracaoAcademicoVO().getNota13MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO13(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito14() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO14(), TipoNotaConceitoEnum.NOTA_14, getConfiguracaoAcademicoVO().getNota14MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO14(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito15() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO15(), TipoNotaConceitoEnum.NOTA_15, getConfiguracaoAcademicoVO().getNota15MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO15(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito16() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO16(), TipoNotaConceitoEnum.NOTA_16, getConfiguracaoAcademicoVO().getNota16MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO16(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito17() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO17(), TipoNotaConceitoEnum.NOTA_17, getConfiguracaoAcademicoVO().getNota17MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO17(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito18() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO18(), TipoNotaConceitoEnum.NOTA_18, getConfiguracaoAcademicoVO().getNota18MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO18(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito19() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO19(), TipoNotaConceitoEnum.NOTA_19, getConfiguracaoAcademicoVO().getNota19MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO19(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito20() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO20(), TipoNotaConceitoEnum.NOTA_20, getConfiguracaoAcademicoVO().getNota20MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO20(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito21() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO21(), TipoNotaConceitoEnum.NOTA_21, getConfiguracaoAcademicoVO().getNota21MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO21(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito22() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO22(), TipoNotaConceitoEnum.NOTA_22, getConfiguracaoAcademicoVO().getNota22MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO22(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito23() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO23(), TipoNotaConceitoEnum.NOTA_23, getConfiguracaoAcademicoVO().getNota23MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO23(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito24() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO24(), TipoNotaConceitoEnum.NOTA_24, getConfiguracaoAcademicoVO().getNota24MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO24(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito25() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO25(), TipoNotaConceitoEnum.NOTA_25, getConfiguracaoAcademicoVO().getNota25MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO25(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito26() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO26(), TipoNotaConceitoEnum.NOTA_26, getConfiguracaoAcademicoVO().getNota26MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO26(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito27() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO27(), TipoNotaConceitoEnum.NOTA_27, getConfiguracaoAcademicoVO().getNota27MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO27(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito28() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO28(), TipoNotaConceitoEnum.NOTA_28, getConfiguracaoAcademicoVO().getNota28MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO28(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito29() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO29(), TipoNotaConceitoEnum.NOTA_29, getConfiguracaoAcademicoVO().getNota29MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO29(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito30() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO30(), TipoNotaConceitoEnum.NOTA_30, getConfiguracaoAcademicoVO().getNota30MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO30(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerConfiguracaoAcademicoNotaConceito() {
		try {
			ConfiguracaoAcademicoNotaConceitoVO obj = (ConfiguracaoAcademicoNotaConceitoVO) context().getExternalContext().getRequestMap().get("configuracaoAcademicoNotaConceitoItens");
			getFacadeFactory().getConfiguracaoAcademicoFacade().removerConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), obj, obj.getTipoNotaConceito());

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademico() {
		try {
			int index = 0;
			getConfiguracaoAcademicoVO().setConfiguracoesVO(getConfiguracoesVO());
			ConfiguracaoAcademicoVO.validarDados(getConfiguracaoAcademicoVO(), getHistoricoVO(), getHistoricoFilhaComposicaoVOs());
			for (ConfiguracaoAcademicoVO config : getConfiguracaoAcademicoVOs()) {
				if (config.getNome().equals(getConfiguracaoAcademicoVO().getNome())) {
					getConfiguracaoAcademicoVOs().set(index, config);
					if (getConfiguracaoAcademicoVO().isNovoObj()) {
						getFacadeFactory().getConfiguracaoAcademicoFacade().incluir(getConfiguracaoAcademicoVO(), getUsuarioLogado());
					} else {
						getFacadeFactory().getConfiguracaoAcademicoFacade().alterar(getConfiguracaoAcademicoVO(), getUsuarioLogado());
						getAplicacaoControle().removerConfiguracaoAcademica(getConfiguracaoAcademicoVO().getCodigo());
					}
					novo();
					setMensagemDetalhada("");
					setMensagemID("msg_dados_gravados");
					return;
				}
				index++;
			}
			getConfiguracaoAcademicoVO().setNovoObj(true);
			if (getConfiguracaoAcademicoVO().isNovoObj()) {
				getFacadeFactory().getConfiguracaoAcademicoFacade().incluir(getConfiguracaoAcademicoVO(), getUsuarioLogado());
			} else {
				getFacadeFactory().getConfiguracaoAcademicoFacade().alterar(getConfiguracaoAcademicoVO(), getUsuarioLogado());
				getAplicacaoControle().removerConfiguracaoAcademica(getConfiguracaoAcademicoVO().getCodigo());
			}
			getConfiguracaoAcademicoVOs().add(getConfiguracaoAcademicoVO());
			novo();
			setMensagemDetalhada("");
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>ConfiguracaoAcademico</code> para edição pelo usuário da aplicação.
	 */
	public void novo() {
		setConfiguracaoAcademicoVO(new ConfiguracaoAcademicoVO());
		getHistoricoFilhaComposicaoVOs().clear();
		setPadrao("");
	}

	public String editar() {
		try {			
			setConfiguracoesVO(getFacadeFactory().getConfiguracoesFacade().consultarPorChavePrimaria(getConfiguracoesVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));				
			setConfiguracaoAcademicoVOs(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorCodigoConfiguracoes(getConfiguracoesVO().getCodigo(), false, getUsuarioLogado()));			
			montarListaSelectItemGrupoDestinatario();
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
//		return "editarConfiguracaoAcademico";
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoAcademicoForm");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>ConfiguracaoAcademico</code> para alteração. O objeto desta classe
	 * é disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public void editarSelecionado() {
		ConfiguracaoAcademicoVO obj = (ConfiguracaoAcademicoVO) context().getExternalContext().getRequestMap().get("configuracaoAcademicoItem");
		obj.setNovoObj(Boolean.FALSE);
		setConfiguracaoAcademicoVO(obj);
		setPadrao("");
	}

	public void excluirSelecionado() {
		try {
			ConfiguracaoAcademicoVO obj = (ConfiguracaoAcademicoVO) context().getExternalContext().getRequestMap().get("configuracaoAcademicoItem");
			getFacadeFactory().getConfiguracaoAcademicoFacade().excluir(obj, getUsuarioLogado());
			int index = 0;
			for (ConfiguracaoAcademicoVO config : getConfiguracaoAcademicoVOs()) {
				if (config.getCodigo().intValue() == obj.getCodigo().intValue()) {
					getConfiguracaoAcademicoVOs().remove(index);
					return;
				}
				index++;
			}
			getAplicacaoControle().removerConfiguracaoAcademica(obj.getCodigo());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void gravar() {
		try {
			getFacadeFactory().getConfiguracoesFacade().alterarSomenteConfiguracores(configuracoesVO);
			for (ConfiguracaoAcademicoVO config : getConfiguracaoAcademicoVOs()) {
				config.setConfiguracoesVO(configuracoesVO);
				if (config.isNovoObj().booleanValue()) {
					getFacadeFactory().getConfiguracaoAcademicoFacade().incluir(config, getUsuarioLogado());
				} else {
					getFacadeFactory().getConfiguracaoAcademicoFacade().alterar(config, getUsuarioLogado());
				}
				getAplicacaoControle().removerConfiguracaoAcademica(config.getCodigo());
			}
			setMensagemID("msg_dados_gravados");
//			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
//			return "editar";
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>ConfiguracaoAcademico</code>. Caso o objeto seja novo
	 * (ainda não gravado no BD) é acionado a operação <code>incluir()</code>.
	 * Caso contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public void gravar(ConfiguracoesVO configuracoesVO) throws Exception {
		if (configuracoesVO.getCodigo() != 0) {
			getFacadeFactory().getConfiguracaoAcademicoFacade().excluirConfiguracaoAcademico(getConfiguracaoAcademicoVOs(), configuracoesVO.getCodigo());
		}
		for (ConfiguracaoAcademicoVO config : getConfiguracaoAcademicoVOs()) {
			config.setConfiguracoesVO(configuracoesVO);
			if (config.isNovoObj().booleanValue()) {
				getFacadeFactory().getConfiguracaoAcademicoFacade().incluir(config, getUsuarioLogado());
			}
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * ConfiguracaoAcademicoCons.jsp. Define o tipo de consulta a ser executada,
	 * por meio de ComboBox denominado campoConsulta, disponivel neste mesmo
	 * JSP. Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorCodigo(new Integer(valorInt), true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, getUsuarioLogado());
			}
			objs = ControleConsulta.obterSubListPaginaApresentar(objs, controleConsulta);
			definirVisibilidadeLinksNavegacao(controleConsulta.getPaginaAtual(), controleConsulta.getNrTotalPaginas());
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>ConfiguracaoAcademicoVO</code> Após a exclusão ela automaticamente
	 * aciona a rotina para uma nova inclusão.
	 */
	public String excluir() throws Exception {
		for (ConfiguracaoAcademicoVO config : getConfiguracaoAcademicoVOs()) {
			getFacadeFactory().getConfiguracaoAcademicoFacade().excluir(config, getUsuarioLogado());
		}
		setConfiguracaoAcademicoVO(new ConfiguracaoAcademicoVO());
		return "";
	}
	
	public void verificarRegrasControleInclusaoDisciplinasPorNrCreditoCargaHoraria() {
		try {
			if(!getConfiguracaoAcademicaVO().getControlarInclusaoDisciplinaPorNrMaxCreditoOuCH()) {
				getConfiguracaoAcademicaVO().setHabilitarDistribuicaoDisciplinaDependenciaAutomatica(false);
				getConfiguracaoAcademicoVO().setTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH("CR");
				getConfiguracaoAcademicoVO().setAcumularCreditosOuCHPeriodosAnterioresNaoCumpridos(true);
			}	
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getListaPadraoAcademico() {
		List<SelectItem> obj = new ArrayList<SelectItem>(0);
		obj.add(new SelectItem("", ""));
		obj.add(new SelectItem("P1", "Padrão Default 1"));
		obj.add(new SelectItem("P2", "Padrão Default 2"));
		obj.add(new SelectItem("P3", "Padrão Default 3"));
		return obj;
	}

	public List<SelectItem> getListaTipoCargaHorariaFrequencia() {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem(TipoCalculoCargaHorariaFrequencia.CARGA_HORARIA_REGISTRO_AULA.name(), TipoCalculoCargaHorariaFrequencia.CARGA_HORARIA_REGISTRO_AULA.getDescricao()));
		lista.add(new SelectItem(TipoCalculoCargaHorariaFrequencia.CARGA_HORARIA_DISCIPLINA.name(), TipoCalculoCargaHorariaFrequencia.CARGA_HORARIA_DISCIPLINA.getDescricao()));
		lista.add(new SelectItem(TipoCalculoCargaHorariaFrequencia.HORA_AULA_DISCIPLINA.name(), TipoCalculoCargaHorariaFrequencia.HORA_AULA_DISCIPLINA.getDescricao()));
		return lista;
	}

	public void usarPadraoDefault() {
		Integer codigo = getConfiguracaoAcademicoVO().getCodigo();
		setConfiguracaoAcademicoVO(new ConfiguracaoAcademicoVO(getPadrao()));
		getConfiguracaoAcademicoVO().setCodigo(codigo);
	}

	public List<SelectItem> getListaSelectItemBimestreNota() {
		return BimestreEnum.getListaSelectItemItemBimestre();
	}

	public void irPaginaInicial() throws Exception {
		controleConsulta.setPaginaAtual(1);
		this.consultar();
	}

	public void irPaginaAnterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setPaginaAtualDeTodas("0/0");
		setListaConsulta(new ArrayList(0));
		definirVisibilidadeLinksNavegacao(0, 0);
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracoesCons");
	}

	public String getPadrao() {
		return padrao;
	}

	public void setPadrao(String padrao) {
		this.padrao = padrao;
	}

	public ConfiguracaoAcademicoVO getConfiguracaoAcademicoVO() {
		if (configuracaoAcademicoVO == null) {
			configuracaoAcademicoVO = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademicoVO;
	}

	public void setConfiguracaoAcademicoVO(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		this.configuracaoAcademicoVO = configuracaoAcademicoVO;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		configuracaoAcademicoVO = null;
	}

	public List<ConfiguracaoAcademicoVO> getConfiguracaoAcademicoVOs() {
		if (configuracaoAcademicoVOs == null) {
			configuracaoAcademicoVOs = new ArrayList<ConfiguracaoAcademicoVO>(0);
		}
		return configuracaoAcademicoVOs;
	}

	public void setConfiguracaoAcademicoVOs(List<ConfiguracaoAcademicoVO> configuracaoAcademicoVOs) {
		this.configuracaoAcademicoVOs = configuracaoAcademicoVOs;
	}

	public ConfiguracoesControle getConfiguracoesControle() {
		return configuracoesControle;
	}

	public void setConfiguracoesControle(ConfiguracoesControle configuracoesControle) {
		this.configuracoesControle = configuracoesControle;
	}

	/**
	 * @return the configuracoesVO
	 */
	public ConfiguracoesVO getConfiguracoesVO() {
		if (configuracoesVO == null) {
			configuracoesVO = new ConfiguracoesVO();
		}
		return configuracoesVO;
	}

	/**
	 * @param configuracoesVO
	 *            the configuracoesVO to set
	 */
	public void setConfiguracoesVO(ConfiguracoesVO configuracoesVO) {
		this.configuracoesVO = configuracoesVO;
	}

	public Boolean getIsLimitarQtdeDiasDownload() {
		if (getConfiguracaoAcademicoVO().getLimitarQtdeDiasMaxDownload()) {
			return true;
		}
		return false;
	}

	public void inicializarDadosNotasDeCincoEmCincoDecimos() {
		if (getConfiguracaoAcademicoVO().getNotasDeCincoEmCincoDecimos()) {
			getConfiguracaoAcademicoVO().setNotasDeCincoEmCincoDecimosApenasMedia(Boolean.FALSE);
		}
	}

	public void inicializarDadosNotasDeCincoEmCincoDecimosApenasMedia() {
		if (getConfiguracaoAcademicoVO().getNotasDeCincoEmCincoDecimosApenasMedia()) {
			getConfiguracaoAcademicoVO().setNotasDeCincoEmCincoDecimos(Boolean.FALSE);
		}
	}

	/**
	 * @return the editandoAPartirFormConfiguracores
	 */
	public Boolean getEditandoAPartirFormConfiguracores() {
		if (editandoAPartirFormConfiguracores == null) {
			return Boolean.FALSE;
		}
		return editandoAPartirFormConfiguracores;
	}

	/**
	 * @param editandoAPartirFormConfiguracores
	 *            the editandoAPartirFormConfiguracores to set
	 */
	public void setEditandoAPartirFormConfiguracores(Boolean editandoAPartirFormConfiguracores) {
		this.editandoAPartirFormConfiguracores = editandoAPartirFormConfiguracores;
	}

	public List<SelectItem> getListaSelectItemLayoutPadraoComprovanteMatricula() {
		if (listaSelectItemLayoutPadraoComprovanteMatricula == null) {
			listaSelectItemLayoutPadraoComprovanteMatricula = new ArrayList<SelectItem>(0);
			for (LayoutComprovanteMatriculaEnum obj : LayoutComprovanteMatriculaEnum.values()) {
				listaSelectItemLayoutPadraoComprovanteMatricula.add(new SelectItem(obj, obj.getValorApresentar()));
			}
		}
		return listaSelectItemLayoutPadraoComprovanteMatricula;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO1() {
		if (configuracaoAcademicoNotaConceitoVO1 == null) {
			configuracaoAcademicoNotaConceitoVO1 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO1;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO1(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO1) {
		this.configuracaoAcademicoNotaConceitoVO1 = configuracaoAcademicoNotaConceitoVO1;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO2() {
		if (configuracaoAcademicoNotaConceitoVO2 == null) {
			configuracaoAcademicoNotaConceitoVO2 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO2;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO2(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO2) {
		this.configuracaoAcademicoNotaConceitoVO2 = configuracaoAcademicoNotaConceitoVO2;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO3() {
		if (configuracaoAcademicoNotaConceitoVO3 == null) {
			configuracaoAcademicoNotaConceitoVO3 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO3;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO3(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO3) {
		this.configuracaoAcademicoNotaConceitoVO3 = configuracaoAcademicoNotaConceitoVO3;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO4() {
		if (configuracaoAcademicoNotaConceitoVO4 == null) {
			configuracaoAcademicoNotaConceitoVO4 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO4;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO4(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO4) {
		this.configuracaoAcademicoNotaConceitoVO4 = configuracaoAcademicoNotaConceitoVO4;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO5() {
		if (configuracaoAcademicoNotaConceitoVO5 == null) {
			configuracaoAcademicoNotaConceitoVO5 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO5;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO5(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO5) {
		this.configuracaoAcademicoNotaConceitoVO5 = configuracaoAcademicoNotaConceitoVO5;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO6() {
		if (configuracaoAcademicoNotaConceitoVO6 == null) {
			configuracaoAcademicoNotaConceitoVO6 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO6;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO6(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO6) {
		this.configuracaoAcademicoNotaConceitoVO6 = configuracaoAcademicoNotaConceitoVO6;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO7() {
		if (configuracaoAcademicoNotaConceitoVO7 == null) {
			configuracaoAcademicoNotaConceitoVO7 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO7;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO7(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO7) {
		this.configuracaoAcademicoNotaConceitoVO7 = configuracaoAcademicoNotaConceitoVO7;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO8() {
		if (configuracaoAcademicoNotaConceitoVO8 == null) {
			configuracaoAcademicoNotaConceitoVO8 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO8;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO8(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO8) {
		this.configuracaoAcademicoNotaConceitoVO8 = configuracaoAcademicoNotaConceitoVO8;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO9() {
		if (configuracaoAcademicoNotaConceitoVO9 == null) {
			configuracaoAcademicoNotaConceitoVO9 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO9;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO9(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO9) {
		this.configuracaoAcademicoNotaConceitoVO9 = configuracaoAcademicoNotaConceitoVO9;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO10() {
		if (configuracaoAcademicoNotaConceitoVO10 == null) {
			configuracaoAcademicoNotaConceitoVO10 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO10;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO10(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO10) {
		this.configuracaoAcademicoNotaConceitoVO10 = configuracaoAcademicoNotaConceitoVO10;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO11() {
		if (configuracaoAcademicoNotaConceitoVO11 == null) {
			configuracaoAcademicoNotaConceitoVO11 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO11;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO11(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO11) {
		this.configuracaoAcademicoNotaConceitoVO11 = configuracaoAcademicoNotaConceitoVO11;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO12() {
		if (configuracaoAcademicoNotaConceitoVO12 == null) {
			configuracaoAcademicoNotaConceitoVO12 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO12;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO12(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO12) {
		this.configuracaoAcademicoNotaConceitoVO12 = configuracaoAcademicoNotaConceitoVO12;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO13() {
		if (configuracaoAcademicoNotaConceitoVO13 == null) {
			configuracaoAcademicoNotaConceitoVO13 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO13;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO13(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO13) {
		this.configuracaoAcademicoNotaConceitoVO13 = configuracaoAcademicoNotaConceitoVO13;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO14() {
		if (configuracaoAcademicoNotaConceitoVO14 == null) {
			configuracaoAcademicoNotaConceitoVO14 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO14;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO14(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO14) {
		this.configuracaoAcademicoNotaConceitoVO14 = configuracaoAcademicoNotaConceitoVO14;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO15() {
		if (configuracaoAcademicoNotaConceitoVO15 == null) {
			configuracaoAcademicoNotaConceitoVO15 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO15;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO15(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO15) {
		this.configuracaoAcademicoNotaConceitoVO15 = configuracaoAcademicoNotaConceitoVO15;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO16() {
		if (configuracaoAcademicoNotaConceitoVO16 == null) {
			configuracaoAcademicoNotaConceitoVO16 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO16;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO16(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO16) {
		this.configuracaoAcademicoNotaConceitoVO16 = configuracaoAcademicoNotaConceitoVO16;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO17() {
		if (configuracaoAcademicoNotaConceitoVO17 == null) {
			configuracaoAcademicoNotaConceitoVO17 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO17;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO17(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO17) {
		this.configuracaoAcademicoNotaConceitoVO17 = configuracaoAcademicoNotaConceitoVO17;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO18() {
		if (configuracaoAcademicoNotaConceitoVO18 == null) {
			configuracaoAcademicoNotaConceitoVO18 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO18;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO18(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO18) {
		this.configuracaoAcademicoNotaConceitoVO18 = configuracaoAcademicoNotaConceitoVO18;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO19() {
		if (configuracaoAcademicoNotaConceitoVO19 == null) {
			configuracaoAcademicoNotaConceitoVO19 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO19;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO19(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO19) {
		this.configuracaoAcademicoNotaConceitoVO19 = configuracaoAcademicoNotaConceitoVO19;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO20() {
		if (configuracaoAcademicoNotaConceitoVO20 == null) {
			configuracaoAcademicoNotaConceitoVO20 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO20;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO20(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO20) {
		this.configuracaoAcademicoNotaConceitoVO20 = configuracaoAcademicoNotaConceitoVO20;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO21() {
		if (configuracaoAcademicoNotaConceitoVO21 == null) {
			configuracaoAcademicoNotaConceitoVO21 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO21;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO21(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO21) {
		this.configuracaoAcademicoNotaConceitoVO21 = configuracaoAcademicoNotaConceitoVO21;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO22() {
		if (configuracaoAcademicoNotaConceitoVO22 == null) {
			configuracaoAcademicoNotaConceitoVO22 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO22;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO22(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO22) {
		this.configuracaoAcademicoNotaConceitoVO22 = configuracaoAcademicoNotaConceitoVO22;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO23() {
		if (configuracaoAcademicoNotaConceitoVO23 == null) {
			configuracaoAcademicoNotaConceitoVO23 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO23;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO23(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO23) {
		this.configuracaoAcademicoNotaConceitoVO23 = configuracaoAcademicoNotaConceitoVO23;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO24() {
		if (configuracaoAcademicoNotaConceitoVO24 == null) {
			configuracaoAcademicoNotaConceitoVO24 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO24;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO24(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO24) {
		this.configuracaoAcademicoNotaConceitoVO24 = configuracaoAcademicoNotaConceitoVO24;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO25() {
		if (configuracaoAcademicoNotaConceitoVO25 == null) {
			configuracaoAcademicoNotaConceitoVO25 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO25;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO25(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO25) {
		this.configuracaoAcademicoNotaConceitoVO25 = configuracaoAcademicoNotaConceitoVO25;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO26() {
		if (configuracaoAcademicoNotaConceitoVO26 == null) {
			configuracaoAcademicoNotaConceitoVO26 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO26;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO26(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO26) {
		this.configuracaoAcademicoNotaConceitoVO26 = configuracaoAcademicoNotaConceitoVO26;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO27() {
		if (configuracaoAcademicoNotaConceitoVO27 == null) {
			configuracaoAcademicoNotaConceitoVO27 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO27;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO27(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO27) {
		this.configuracaoAcademicoNotaConceitoVO27 = configuracaoAcademicoNotaConceitoVO27;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO28() {
		if (configuracaoAcademicoNotaConceitoVO28 == null) {
			configuracaoAcademicoNotaConceitoVO28 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO28;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO28(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO28) {
		this.configuracaoAcademicoNotaConceitoVO28 = configuracaoAcademicoNotaConceitoVO28;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO29() {
		if (configuracaoAcademicoNotaConceitoVO29 == null) {
			configuracaoAcademicoNotaConceitoVO29 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO29;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO29(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO29) {
		this.configuracaoAcademicoNotaConceitoVO29 = configuracaoAcademicoNotaConceitoVO29;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO30() {
		if (configuracaoAcademicoNotaConceitoVO30 == null) {
			configuracaoAcademicoNotaConceitoVO30 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO30;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO30(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO30) {
		this.configuracaoAcademicoNotaConceitoVO30 = configuracaoAcademicoNotaConceitoVO30;
	}

	/**
	 * @return the listaSelectItemTipoControleAvancoPeriodoPorCreditoOuCH
	 */
	public List<SelectItem> getListaSelectItemTipoControleAvancoPeriodoPorCreditoOuCH() {
		if (listaSelectItemTipoControleAvancoPeriodoPorCreditoOuCH == null) {
			listaSelectItemTipoControleAvancoPeriodoPorCreditoOuCH = new ArrayList<SelectItem>(0);
			listaSelectItemTipoControleAvancoPeriodoPorCreditoOuCH.add(new SelectItem("CR", "Créditos Disciplinas Período Letivo"));
			listaSelectItemTipoControleAvancoPeriodoPorCreditoOuCH.add(new SelectItem("CH", "Carga Horária Disciplinas Período Letivo"));
			listaSelectItemTipoControleAvancoPeriodoPorCreditoOuCH.add(new SelectItem("QT", "Quantidade Disciplinas Reprovadas"));
		}
		return listaSelectItemTipoControleAvancoPeriodoPorCreditoOuCH;
	}

	/**
	 * @param listaSelectItemTipoControleAvancoPeriodoPorCreditoOuCH
	 *            the listaSelectItemTipoControleAvancoPeriodoPorCreditoOuCH to
	 *            set
	 */
	public void setListaSelectItemTipoControleAvancoPeriodoPorCreditoOuCH(List<SelectItem> listaSelectItemTipoControleAvancoPeriodoPorCreditoOuCH) {
		this.listaSelectItemTipoControleAvancoPeriodoPorCreditoOuCH = listaSelectItemTipoControleAvancoPeriodoPorCreditoOuCH;
	}

	/**
	 * @return the
	 *         listaSelectItemTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH
	 */
	public List<SelectItem> getListaSelectItemTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH() {
		if (listaSelectItemTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH == null) {
			listaSelectItemTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH = new ArrayList<SelectItem>(0);
			listaSelectItemTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH.add(new SelectItem("CR", "Crédito"));
			listaSelectItemTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH.add(new SelectItem("CH", "Carga Horária"));
		}
		return listaSelectItemTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH;
	}

	/**
	 * @param listaSelectItemTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH
	 *            the
	 *            listaSelectItemTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH
	 *            to set
	 */
	public void setListaSelectItemTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH(List<SelectItem> listaSelectItemTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH) {
		this.listaSelectItemTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH = listaSelectItemTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH;
	}

	public void desabilitarcontrolarAvancoPeriodoPorCreditoOuCH() {
		if (this.getConfiguracaoAcademicoVO().getRenovacaoMatriculaSequencial()) {
			this.getConfiguracaoAcademicoVO().setControlarAvancoPeriodoPorCreditoOuCH(Boolean.FALSE);
			this.getConfiguracaoAcademicoVO().setPermiteEvoluirPeriodoLetivoCasoReprovado(true);
		}
	}

	public void atualizarRenovacaoMatriculaSequencial() {
		if (this.getConfiguracaoAcademicoVO().getRenovacaoMatriculaSequencial()) {
			this.getConfiguracaoAcademicoVO().setControlarAvancoPeriodoPorCreditoOuCH(false);
		}
	}

	public List<SelectItem> getListaSelectItemTipoApresentarFrequenciaVisaoAluno() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("PO", "Porcentagem"));
		objs.add(new SelectItem("QT", "Quantidade"));
		return objs;
	}

	public List<SelectItem> getListaGrupoDestinatario() {
		if (listaGrupoDestinatario == null) {
			listaGrupoDestinatario = new ArrayList<SelectItem>(0);
		}
		return listaGrupoDestinatario;
	}

	public void setListaGrupoDestinatario(List<SelectItem> listaGrupoDestinatario) {
		this.listaGrupoDestinatario = listaGrupoDestinatario;
	}

	public void montarListaSelectItemGrupoDestinatario() {
		try {
//			getListaGrupoDestinatario().addAll(getFacadeFactory().getGrupoDestinatariosFacade().consultarDadosListaSelectItem(Obrigatorio.NAO));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	private HistoricoVO historicoVO;
	private List<HistoricoVO> historicoVOs;

	/**
	 * @return the historicoVOs
	 */
	public List<HistoricoVO> getHistoricoVOs() {
		if (historicoVOs == null) {
			historicoVOs = new ArrayList<HistoricoVO>(0);
		}
		return historicoVOs;
	}

	/**
	 * @param historicoVOs
	 *            the historicoVOs to set
	 */
	public void setHistoricoVOs(List<HistoricoVO> historicoVOs) {
		this.historicoVOs = historicoVOs;
	}

	/**
	 * @return the historicoVO
	 */
	public HistoricoVO getHistoricoVO() {
		if (historicoVO == null) {
			historicoVO = new HistoricoVO();
		}
		return historicoVO;
	}

	/**
	 * @param historicoVO
	 *            the historicoVO to set
	 */
	public void setHistoricoVO(HistoricoVO historicoVO) {
		this.historicoVO = historicoVO;
	}

	private Boolean abrirModalTesteConfiguracaoAcademica;

	/**
	 * @return the abrirModalTesteConfiguracaoAcademica
	 */
	public Boolean getAbrirModalTesteConfiguracaoAcademica() {
		if (abrirModalTesteConfiguracaoAcademica == null) {
			abrirModalTesteConfiguracaoAcademica = false;
		}
		return abrirModalTesteConfiguracaoAcademica;
	}

	/**
	 * @param abrirModalTesteConfiguracaoAcademica
	 *            the abrirModalTesteConfiguracaoAcademica to set
	 */
	public void setAbrirModalTesteConfiguracaoAcademica(Boolean abrirModalTesteConfiguracaoAcademica) {
		this.abrirModalTesteConfiguracaoAcademica = abrirModalTesteConfiguracaoAcademica;
	}

	public String getModalTesteConfiguracaoAcademica() {
		return getAbrirModalTesteConfiguracaoAcademica() ? "RichFaces.$('modalTesteConfiguracao').show();RichFaces.$('modalTesteConfiguracaoComposta').hide()" : "";
	}

	public void inicializarTesteConfiguracaoAcademica() {
		try {
			setAbrirModalTesteConfiguracaoAcademica(false);
			setAbrirModalDefinicaoDisciplinaFilhaComposicao(ConfiguracaoAcademicoVO.realizarValidacaoConfiguracaoAcademicaTipoComposta(getConfiguracaoAcademicoVO()));
			if (getAbrirModalDefinicaoDisciplinaFilhaComposicao()) {
				setListaSelecItemConfiguracaoAcademico(null);
				if(getConfiguracaoAcademicoVO().getQtdeDisciplinaFilhaComposicao() > 0) {
					realizarCriacaoHistoricoParaValidacaoConnfiguracaoAcademica();
				}			
			}
			GradeDisciplinaVO gradeDisciplinaVO = getHistoricoVO().getGradeDisciplinaVO();
			setHistoricoVO(null);
			getHistoricoVO().setConfiguracaoAcademico(getConfiguracaoAcademicoVO());
			getHistoricoVO().setFreguencia(100.0);
			getHistoricoVO().setSituacao("CS");
			getHistoricoVO().setCargaHorariaDisciplina(60);
			getHistoricoVO().setCreditoDisciplina(3);
			if(getAbrirModalDefinicaoDisciplinaFilhaComposicao()) {
				getHistoricoVO().setGradeDisciplinaVO(gradeDisciplinaVO);
				getHistoricoVO().getGradeDisciplinaVO().setDisciplinaComposta(true);
				getHistoricoVO().getGradeDisciplinaVO().setTipoControleComposicao(TipoControleComposicaoEnum.ESTUDAR_TODAS_COMPOSTAS);
				getHistoricoVO().getGradeDisciplinaVO().setFormulaCalculoNota(FormulaCalculoNotaEnum.MEDIA);
				getHistoricoVO().getGradeDisciplinaVO().setCodigo(1);
				getHistoricoVO().setHistoricoDisciplinaFilhaComposicaoVOs(getHistoricoFilhaComposicaoVOs());
				getFacadeFactory().getHistoricoFacade().executarAtualizacaoDisciplinaFilhaComposicaoComBaseDisciplinaCompostaComposta(getHistoricoVO(),  TipoAlteracaoSituacaoHistoricoEnum.TODOS_HISTORICOS, false, getUsuarioLogado());
			}
			if (!getAbrirModalDefinicaoDisciplinaFilhaComposicao()) {
				validarDadosTesteConfiguracaoAcademico();
			}
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarCalculoNota() {
		try {
			limparMensagem();
			getHistoricoVO().setCodigo(1);
			for (HistoricoVO historicoVO : getHistoricoFilhaComposicaoVOs()) {			
				realizarCalculoNota(historicoVO);
			}
			if(getHistoricoVO().getHistoricoDisciplinaComposta()) {
				getHistoricoVO().setHistoricoDisciplinaFilhaComposicaoVOs(getHistoricoFilhaComposicaoVOs());
			}
			realizarCalculoNota(getHistoricoVO());								
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarCalculoNota(HistoricoVO historicoVO) throws Exception {
		historicoVO.setSituacao("CS");
		historicoVO.setMediaFinal(null);
		historicoVO.getHistoricoNotaVOs().clear();
		getFacadeFactory().getHistoricoFacade().verificarNotasLancadas(historicoVO, getUsuarioLogado());
		getFacadeFactory().getHistoricoFacade().verificaAlunoReprovadoFalta(historicoVO, historicoVO.getConfiguracaoAcademico(), getUsuarioLogado());
		boolean resultado = false;
		if(!historicoVO.getHistoricoDisciplinaComposta()) {
			resultado = historicoVO.getConfiguracaoAcademico().substituirVariaveisFormulaPorValores(historicoVO, historicoVO.getHistoricoDisciplinaFilhaComposicaoVOs(), true);
		}else {
			resultado = getFacadeFactory().getHistoricoFacade().executarAtualizacaoDisciplinaComposta(historicoVO, historicoVO.getHistoricoDisciplinaFilhaComposicaoVOs(), TipoAlteracaoSituacaoHistoricoEnum.TODOS_HISTORICOS, false, resultado, getUsuarioLogado());
		}
		if(!resultado){
			if(!historicoVO.getSituacao().equals("RF")){
				if(historicoVO.getMediaFinal() != null ){													
					historicoVO.setSituacao("RE");						
				}else{						
					historicoVO.setSituacao("CS");
				}
			}
		}
		if(historicoVO.getHistoricoDisciplinaComposta()) {
			getFacadeFactory().getHistoricoFacade().executarAtualizacaoDisciplinaFilhaComposicaoComBaseDisciplinaCompostaComposta(historicoVO,  TipoAlteracaoSituacaoHistoricoEnum.TODOS_HISTORICOS, false, getUsuarioLogado());			
		}else {
			getFacadeFactory().getHistoricoFacade().realizarCriacaoHistoricoNotaVO(historicoVO, historicoVO.getHistoricoDisciplinaFilhaComposicaoVOs());
		}
		for (Iterator<HistoricoNotaVO> iterator = historicoVO.getHistoricoNotaVOs().iterator(); iterator.hasNext();) {
			HistoricoNotaVO object = iterator.next();
			if (!object.getNotaRecuperacao()) {
				iterator.remove();
			}
		}
		if (historicoVO.getMediaFinal() != null) {
			if (historicoVO.getConfiguracaoAcademico().getUtilizarArredondamentoMediaParaMais()) {
				historicoVO.setMediaFinal(Uteis.arredondarMultiploDeCincoParaCima(historicoVO.getMediaFinal()));
			} else if (historicoVO.getConfiguracaoAcademico().getNotasDeCincoEmCincoDecimos() || historicoVO.getConfiguracaoAcademico().getNotasDeCincoEmCincoDecimosApenasMedia()) {
				historicoVO.setMediaFinal(Math.round(2 * historicoVO.getMediaFinal()) / 2.0);
			}
			if (!historicoVO.getHistoricoDisciplinaFazParteComposicao() || (historicoVO.getHistoricoDisciplinaFazParteComposicao() && !historicoVO.getConfiguracaoAcademico().getSituacaoDisciplinaQueFazParteComposicaoControladaDisciplinaPrincipal())) {
				if ((!historicoVO.getSituacao().equals(SituacaoHistorico.REPROVADO_FALTA.getValor())) && (!historicoVO.getSituacao().equals(SituacaoHistorico.ISENTO.getValor())) && !historicoVO.getSituacao().equals("")) {
					if (resultado) {
						historicoVO.setSituacao(SituacaoHistorico.APROVADO.getValor());
					} else {
						historicoVO.setSituacao(SituacaoHistorico.REPROVADO.getValor());
					}
				}
			/**
			 * Caso a disciplina faça parte de composição e a opção na configuração esteja marcada da situação da filha ser controlada pela situação da mãe,
			 * deverá então realizar o cálculo da mãe para definir então a situação da filha
			 * Autor: Carlos Eugênio	
			 */
			} else if (historicoVO.getHistoricoDisciplinaFazParteComposicao() && historicoVO.getConfiguracaoAcademico().getSituacaoDisciplinaQueFazParteComposicaoControladaDisciplinaPrincipal()) {					
				historicoVO.setSituacao(historicoVO.getSituacaoHistoricoDisciplinaComposta());
			}
			
			if (!historicoVO.getSituacao().equals("RF")) {
				if (resultado) {
					historicoVO.setSituacao(SituacaoHistorico.APROVADO.getValor());
				} else {
					historicoVO.setSituacao(SituacaoHistorico.REPROVADO.getValor());
				}
			}
		} else {
			historicoVO.setSituacao(SituacaoHistorico.CURSANDO.getValor());
		}	
	}

	public void checarFormatarValoresNota1() {

		limparMensagem();
		if (getHistoricoVO().getNota1() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 1);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota2() {

		limparMensagem();
		if (getHistoricoVO().getNota2() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 2);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota3() {

		limparMensagem();
		if (getHistoricoVO().getNota3() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 3);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota4() {

		limparMensagem();
		if (getHistoricoVO().getNota4() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 4);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota5() {

		limparMensagem();
		if (getHistoricoVO().getNota5() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 5);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota6() {

		limparMensagem();
		if (getHistoricoVO().getNota6() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 6);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota7() {

		limparMensagem();
		if (getHistoricoVO().getNota7() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 7);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota8() {

		limparMensagem();
		if (getHistoricoVO().getNota8() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 8);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota9() {

		limparMensagem();
		if (getHistoricoVO().getNota9() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 9);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota10() {

		limparMensagem();
		if (getHistoricoVO().getNota10() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 10);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota11() {

		limparMensagem();
		if (getHistoricoVO().getNota11() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 11);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota12() {

		limparMensagem();
		if (getHistoricoVO().getNota12() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 12);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota13() {

		limparMensagem();
		if (getHistoricoVO().getNota13() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 13);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota14() {

		limparMensagem();
		if (getHistoricoVO().getNota14() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 14);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota15() {

		limparMensagem();
		if (getHistoricoVO().getNota15() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 15);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota16() {

		limparMensagem();
		if (getHistoricoVO().getNota16() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 16);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota17() {

		limparMensagem();
		if (getHistoricoVO().getNota17() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 17);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota18() {

		limparMensagem();
		if (getHistoricoVO().getNota18() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 18);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota19() {

		limparMensagem();
		if (getHistoricoVO().getNota19() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 19);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota20() {

		limparMensagem();
		if (getHistoricoVO().getNota20() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 20);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota21() {

		limparMensagem();
		if (getHistoricoVO().getNota21() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 21);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota22() {

		limparMensagem();
		if (getHistoricoVO().getNota22() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 22);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota23() {

		limparMensagem();
		if (getHistoricoVO().getNota23() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 23);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota24() {

		limparMensagem();
		if (getHistoricoVO().getNota24() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 24);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota25() {

		limparMensagem();
		if (getHistoricoVO().getNota25() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 25);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota26() {

		limparMensagem();
		if (getHistoricoVO().getNota26() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 26);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota27() {

		limparMensagem();
		if (getHistoricoVO().getNota27() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 27);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota28() {

		limparMensagem();
		if (getHistoricoVO().getNota28() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 28);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota29() {

		limparMensagem();
		if (getHistoricoVO().getNota29() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 29);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota30() {

		limparMensagem();
		if (getHistoricoVO().getNota30() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistoricoVO(), getHistoricoFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 30);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public Boolean getIsNota1Media() {
		if (!getConfiguracaoAcademicoVO().getNota1TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota2Media() {
		if (!getConfiguracaoAcademicoVO().getNota2TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota3Media() {
		if (!getConfiguracaoAcademicoVO().getNota3TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota4Media() {
		if (!getConfiguracaoAcademicoVO().getNota4TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota5Media() {
		if (!getConfiguracaoAcademicoVO().getNota5TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota6Media() {
		if (!getConfiguracaoAcademicoVO().getNota6TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota7Media() {
		if (!getConfiguracaoAcademicoVO().getNota7TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota8Media() {
		if (!getConfiguracaoAcademicoVO().getNota8TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota9Media() {
		if (!getConfiguracaoAcademicoVO().getNota9TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota10Media() {
		if (!getConfiguracaoAcademicoVO().getNota10TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota11Media() {
		if (!getConfiguracaoAcademicoVO().getNota11TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota12Media() {
		if (!getConfiguracaoAcademicoVO().getNota12TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota13Media() {
		if (!getConfiguracaoAcademicoVO().getNota13TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota14Media() {
		if (!getConfiguracaoAcademicoVO().getNota14TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota15Media() {
		if (!getConfiguracaoAcademicoVO().getNota15TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota16Media() {
		if (!getConfiguracaoAcademicoVO().getNota16TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota17Media() {
		if (!getConfiguracaoAcademicoVO().getNota17TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota18Media() {
		if (!getConfiguracaoAcademicoVO().getNota18TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota19Media() {
		if (!getConfiguracaoAcademicoVO().getNota19TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota20Media() {
		if (!getConfiguracaoAcademicoVO().getNota20TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota21Media() {
		if (!getConfiguracaoAcademicoVO().getNota21TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota22Media() {
		if (!getConfiguracaoAcademicoVO().getNota22TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota23Media() {
		if (!getConfiguracaoAcademicoVO().getNota23TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota24Media() {
		if (!getConfiguracaoAcademicoVO().getNota24TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota25Media() {
		if (!getConfiguracaoAcademicoVO().getNota25TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota26Media() {
		if (!getConfiguracaoAcademicoVO().getNota26TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota27Media() {
		if (!getConfiguracaoAcademicoVO().getNota27TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota28Media() {
		if (!getConfiguracaoAcademicoVO().getNota28TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota29Media() {
		if (!getConfiguracaoAcademicoVO().getNota29TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota30Media() {
		if (!getConfiguracaoAcademicoVO().getNota30TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}
	
	public void inicializarOpcaoNotaConceito() {
		setListaSelectItemNota1Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota1ConceitoVOs()));
		setListaSelectItemNota2Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota2ConceitoVOs()));
		setListaSelectItemNota3Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota3ConceitoVOs()));
		setListaSelectItemNota4Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota4ConceitoVOs()));
		setListaSelectItemNota5Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota5ConceitoVOs()));
		setListaSelectItemNota6Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota6ConceitoVOs()));
		setListaSelectItemNota7Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota7ConceitoVOs()));
		setListaSelectItemNota8Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota8ConceitoVOs()));
		setListaSelectItemNota9Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota9ConceitoVOs()));
		setListaSelectItemNota10Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota10ConceitoVOs()));
		setListaSelectItemNota11Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota11ConceitoVOs()));
		setListaSelectItemNota12Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota12ConceitoVOs()));
		setListaSelectItemNota13Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota13ConceitoVOs()));
		setListaSelectItemNota14Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota14ConceitoVOs()));
		setListaSelectItemNota15Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota15ConceitoVOs()));
		setListaSelectItemNota16Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota16ConceitoVOs()));
		setListaSelectItemNota17Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota17ConceitoVOs()));
		setListaSelectItemNota18Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota18ConceitoVOs()));
		setListaSelectItemNota19Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota19ConceitoVOs()));
		setListaSelectItemNota20Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota20ConceitoVOs()));
		setListaSelectItemNota21Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota21ConceitoVOs()));
		setListaSelectItemNota22Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota22ConceitoVOs()));
		setListaSelectItemNota23Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota23ConceitoVOs()));
		setListaSelectItemNota24Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota24ConceitoVOs()));
		setListaSelectItemNota25Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota25ConceitoVOs()));
		setListaSelectItemNota26Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota26ConceitoVOs()));
		setListaSelectItemNota27Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota27ConceitoVOs()));
		setListaSelectItemNota28Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota28ConceitoVOs()));
	}
	
	private List<SelectItem> getListaSelectItemOpcaoNotaConceito(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNotaConceitoVOs) {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem(0, ""));
		for (ConfiguracaoAcademicoNotaConceitoVO obj : configuracaoAcademicoNotaConceitoVOs) {
			itens.add(new SelectItem(obj.getCodigo(), obj.getConceitoNota()));
		}
		return itens;

	}
	
	public List<SelectItem> getListaSelectItemNota1Conceito() {
		if (listaSelectItemNota1Conceito == null) {
			listaSelectItemNota1Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota1Conceito;
	}

	public void setListaSelectItemNota1Conceito(List<SelectItem> listaSelectItemNota1Conceito) {
		this.listaSelectItemNota1Conceito = listaSelectItemNota1Conceito;
	}

	public List<SelectItem> getListaSelectItemNota2Conceito() {
		if (listaSelectItemNota2Conceito == null) {
			listaSelectItemNota2Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota2Conceito;
	}

	public void setListaSelectItemNota2Conceito(List<SelectItem> listaSelectItemNota2Conceito) {
		this.listaSelectItemNota2Conceito = listaSelectItemNota2Conceito;
	}

	public List<SelectItem> getListaSelectItemNota3Conceito() {
		if (listaSelectItemNota3Conceito == null) {
			listaSelectItemNota3Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota3Conceito;
	}

	public void setListaSelectItemNota3Conceito(List<SelectItem> listaSelectItemNota3Conceito) {
		this.listaSelectItemNota3Conceito = listaSelectItemNota3Conceito;
	}

	public List<SelectItem> getListaSelectItemNota4Conceito() {
		if (listaSelectItemNota4Conceito == null) {
			listaSelectItemNota4Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota4Conceito;
	}

	public void setListaSelectItemNota4Conceito(List<SelectItem> listaSelectItemNota4Conceito) {
		this.listaSelectItemNota4Conceito = listaSelectItemNota4Conceito;
	}

	public List<SelectItem> getListaSelectItemNota5Conceito() {
		if (listaSelectItemNota5Conceito == null) {
			listaSelectItemNota5Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota5Conceito;
	}

	public void setListaSelectItemNota5Conceito(List<SelectItem> listaSelectItemNota5Conceito) {
		this.listaSelectItemNota5Conceito = listaSelectItemNota5Conceito;
	}

	public List<SelectItem> getListaSelectItemNota6Conceito() {
		if (listaSelectItemNota6Conceito == null) {
			listaSelectItemNota6Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota6Conceito;
	}

	public void setListaSelectItemNota6Conceito(List<SelectItem> listaSelectItemNota6Conceito) {
		this.listaSelectItemNota6Conceito = listaSelectItemNota6Conceito;
	}

	public List<SelectItem> getListaSelectItemNota7Conceito() {
		if (listaSelectItemNota7Conceito == null) {
			listaSelectItemNota7Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota7Conceito;
	}

	public void setListaSelectItemNota7Conceito(List<SelectItem> listaSelectItemNota7Conceito) {
		this.listaSelectItemNota7Conceito = listaSelectItemNota7Conceito;
	}

	public List<SelectItem> getListaSelectItemNota8Conceito() {
		if (listaSelectItemNota8Conceito == null) {
			listaSelectItemNota8Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota8Conceito;
	}

	public void setListaSelectItemNota8Conceito(List<SelectItem> listaSelectItemNota8Conceito) {
		this.listaSelectItemNota8Conceito = listaSelectItemNota8Conceito;
	}

	public List<SelectItem> getListaSelectItemNota9Conceito() {
		if (listaSelectItemNota9Conceito == null) {
			listaSelectItemNota9Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota9Conceito;
	}

	public void setListaSelectItemNota9Conceito(List<SelectItem> listaSelectItemNota9Conceito) {
		this.listaSelectItemNota9Conceito = listaSelectItemNota9Conceito;
	}

	public List<SelectItem> getListaSelectItemNota10Conceito() {
		if (listaSelectItemNota10Conceito == null) {
			listaSelectItemNota10Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota10Conceito;
	}

	public void setListaSelectItemNota10Conceito(List<SelectItem> listaSelectItemNota10Conceito) {
		this.listaSelectItemNota10Conceito = listaSelectItemNota10Conceito;
	}

	public List<SelectItem> getListaSelectItemNota11Conceito() {
		if (listaSelectItemNota11Conceito == null) {
			listaSelectItemNota11Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota11Conceito;
	}

	public void setListaSelectItemNota11Conceito(List<SelectItem> listaSelectItemNota11Conceito) {
		this.listaSelectItemNota11Conceito = listaSelectItemNota11Conceito;
	}

	public List<SelectItem> getListaSelectItemNota12Conceito() {
		if (listaSelectItemNota12Conceito == null) {
			listaSelectItemNota12Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota12Conceito;
	}

	public void setListaSelectItemNota12Conceito(List<SelectItem> listaSelectItemNota12Conceito) {
		this.listaSelectItemNota12Conceito = listaSelectItemNota12Conceito;
	}

	public List<SelectItem> getListaSelectItemNota13Conceito() {
		if (listaSelectItemNota13Conceito == null) {
			listaSelectItemNota13Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota13Conceito;
	}

	public void setListaSelectItemNota13Conceito(List<SelectItem> listaSelectItemNota13Conceito) {
		this.listaSelectItemNota13Conceito = listaSelectItemNota13Conceito;
	}

	public List<SelectItem> getListaSelectItemNota14Conceito() {
		if (listaSelectItemNota14Conceito == null) {
			listaSelectItemNota14Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota14Conceito;
	}

	public void setListaSelectItemNota14Conceito(List<SelectItem> listaSelectItemNota14Conceito) {
		this.listaSelectItemNota14Conceito = listaSelectItemNota14Conceito;
	}

	public List<SelectItem> getListaSelectItemNota15Conceito() {
		if (listaSelectItemNota15Conceito == null) {
			listaSelectItemNota15Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota15Conceito;
	}

	public void setListaSelectItemNota15Conceito(List<SelectItem> listaSelectItemNota15Conceito) {
		this.listaSelectItemNota15Conceito = listaSelectItemNota15Conceito;
	}

	public List<SelectItem> getListaSelectItemNota16Conceito() {
		if (listaSelectItemNota16Conceito == null) {
			listaSelectItemNota16Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota16Conceito;
	}

	public void setListaSelectItemNota16Conceito(List<SelectItem> listaSelectItemNota16Conceito) {
		this.listaSelectItemNota16Conceito = listaSelectItemNota16Conceito;
	}

	public List<SelectItem> getListaSelectItemNota17Conceito() {
		if (listaSelectItemNota17Conceito == null) {
			listaSelectItemNota17Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota17Conceito;
	}

	public void setListaSelectItemNota17Conceito(List<SelectItem> listaSelectItemNota17Conceito) {
		this.listaSelectItemNota17Conceito = listaSelectItemNota17Conceito;
	}

	public List<SelectItem> getListaSelectItemNota18Conceito() {
		if (listaSelectItemNota18Conceito == null) {
			listaSelectItemNota18Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota18Conceito;
	}

	public void setListaSelectItemNota18Conceito(List<SelectItem> listaSelectItemNota18Conceito) {
		this.listaSelectItemNota18Conceito = listaSelectItemNota18Conceito;
	}

	public List<SelectItem> getListaSelectItemNota19Conceito() {
		if (listaSelectItemNota19Conceito == null) {
			listaSelectItemNota19Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota19Conceito;
	}

	public void setListaSelectItemNota19Conceito(List<SelectItem> listaSelectItemNota19Conceito) {
		this.listaSelectItemNota19Conceito = listaSelectItemNota19Conceito;
	}

	public List<SelectItem> getListaSelectItemNota20Conceito() {
		if (listaSelectItemNota20Conceito == null) {
			listaSelectItemNota20Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota20Conceito;
	}

	public void setListaSelectItemNota20Conceito(List<SelectItem> listaSelectItemNota20Conceito) {
		this.listaSelectItemNota20Conceito = listaSelectItemNota20Conceito;
	}

	public List<SelectItem> getListaSelectItemNota21Conceito() {
		if (listaSelectItemNota21Conceito == null) {
			listaSelectItemNota21Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota21Conceito;
	}

	public void setListaSelectItemNota21Conceito(List<SelectItem> listaSelectItemNota21Conceito) {
		this.listaSelectItemNota21Conceito = listaSelectItemNota21Conceito;
	}

	public List<SelectItem> getListaSelectItemNota22Conceito() {
		if (listaSelectItemNota22Conceito == null) {
			listaSelectItemNota22Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota22Conceito;
	}

	public void setListaSelectItemNota22Conceito(List<SelectItem> listaSelectItemNota22Conceito) {
		this.listaSelectItemNota22Conceito = listaSelectItemNota22Conceito;
	}

	public List<SelectItem> getListaSelectItemNota23Conceito() {
		if (listaSelectItemNota23Conceito == null) {
			listaSelectItemNota23Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota23Conceito;
	}

	public void setListaSelectItemNota23Conceito(List<SelectItem> listaSelectItemNota23Conceito) {
		this.listaSelectItemNota23Conceito = listaSelectItemNota23Conceito;
	}

	public List<SelectItem> getListaSelectItemNota24Conceito() {
		if (listaSelectItemNota24Conceito == null) {
			listaSelectItemNota24Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota24Conceito;
	}

	public void setListaSelectItemNota24Conceito(List<SelectItem> listaSelectItemNota24Conceito) {
		this.listaSelectItemNota24Conceito = listaSelectItemNota24Conceito;
	}

	public List<SelectItem> getListaSelectItemNota25Conceito() {
		if (listaSelectItemNota25Conceito == null) {
			listaSelectItemNota25Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota25Conceito;
	}

	public void setListaSelectItemNota25Conceito(List<SelectItem> listaSelectItemNota25Conceito) {
		this.listaSelectItemNota25Conceito = listaSelectItemNota25Conceito;
	}

	public List<SelectItem> getListaSelectItemNota26Conceito() {
		if (listaSelectItemNota26Conceito == null) {
			listaSelectItemNota26Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota26Conceito;
	}

	public void setListaSelectItemNota26Conceito(List<SelectItem> listaSelectItemNota26Conceito) {
		this.listaSelectItemNota26Conceito = listaSelectItemNota26Conceito;
	}

	public List<SelectItem> getListaSelectItemNota27Conceito() {
		if (listaSelectItemNota27Conceito == null) {
			listaSelectItemNota27Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota27Conceito;
	}

	public void setListaSelectItemNota27Conceito(List<SelectItem> listaSelectItemNota27Conceito) {
		this.listaSelectItemNota27Conceito = listaSelectItemNota27Conceito;
	}

	public List<SelectItem> getListaSelectItemNota28Conceito() {
		if (listaSelectItemNota28Conceito == null) {
			listaSelectItemNota28Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota28Conceito;
	}

	public void setListaSelectItemNota28Conceito(List<SelectItem> listaSelectItemNota28Conceito) {
		this.listaSelectItemNota28Conceito = listaSelectItemNota28Conceito;
	}
	
	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO31() {
		if (configuracaoAcademicoNotaConceitoVO31 == null) {
			configuracaoAcademicoNotaConceitoVO31 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO31;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO31(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO31) {
		this.configuracaoAcademicoNotaConceitoVO31 = configuracaoAcademicoNotaConceitoVO31;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO32() {
		if (configuracaoAcademicoNotaConceitoVO32 == null) {
			configuracaoAcademicoNotaConceitoVO32 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO32;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO32(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO32) {
		this.configuracaoAcademicoNotaConceitoVO32 = configuracaoAcademicoNotaConceitoVO32;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO33() {
		if (configuracaoAcademicoNotaConceitoVO33 == null) {
			configuracaoAcademicoNotaConceitoVO33 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO33;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO33(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO33) {
		this.configuracaoAcademicoNotaConceitoVO33 = configuracaoAcademicoNotaConceitoVO33;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO34() {
		if (configuracaoAcademicoNotaConceitoVO34 == null) {
			configuracaoAcademicoNotaConceitoVO34 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO34;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO34(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO34) {
		this.configuracaoAcademicoNotaConceitoVO34 = configuracaoAcademicoNotaConceitoVO34;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO35() {
		if (configuracaoAcademicoNotaConceitoVO35 == null) {
			configuracaoAcademicoNotaConceitoVO35 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO35;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO35(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO35) {
		this.configuracaoAcademicoNotaConceitoVO35 = configuracaoAcademicoNotaConceitoVO35;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO36() {
		if (configuracaoAcademicoNotaConceitoVO36 == null) {
			configuracaoAcademicoNotaConceitoVO36 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO36;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO36(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO36) {
		this.configuracaoAcademicoNotaConceitoVO36 = configuracaoAcademicoNotaConceitoVO36;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO37() {
		if (configuracaoAcademicoNotaConceitoVO37 == null) {
			configuracaoAcademicoNotaConceitoVO37 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO37;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO37(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO37) {
		this.configuracaoAcademicoNotaConceitoVO37 = configuracaoAcademicoNotaConceitoVO37;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO38() {
		if (configuracaoAcademicoNotaConceitoVO38 == null) {
			configuracaoAcademicoNotaConceitoVO38 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO38;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO38(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO38) {
		this.configuracaoAcademicoNotaConceitoVO38 = configuracaoAcademicoNotaConceitoVO38;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO39() {
		if (configuracaoAcademicoNotaConceitoVO39 == null) {
			configuracaoAcademicoNotaConceitoVO39 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO39;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO39(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO39) {
		this.configuracaoAcademicoNotaConceitoVO39 = configuracaoAcademicoNotaConceitoVO39;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO40() {
		if (configuracaoAcademicoNotaConceitoVO40 == null) {
			configuracaoAcademicoNotaConceitoVO40 = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO40;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO40(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO40) {
		this.configuracaoAcademicoNotaConceitoVO40 = configuracaoAcademicoNotaConceitoVO40;
	}

	public List<SelectItem> getListaSelectItemNota29Conceito() {
		if (listaSelectItemNota29Conceito == null) {
			listaSelectItemNota29Conceito = new ArrayList<SelectItem>();
		}
		return listaSelectItemNota29Conceito;
	}

	public void setListaSelectItemNota29Conceito(List<SelectItem> listaSelectItemNota29Conceito) {
		this.listaSelectItemNota29Conceito = listaSelectItemNota29Conceito;
	}

	public List<SelectItem> getListaSelectItemNota30Conceito() {
		if (listaSelectItemNota30Conceito == null) {
			listaSelectItemNota30Conceito = new ArrayList<SelectItem>();
		}
		return listaSelectItemNota30Conceito;
	}

	public void setListaSelectItemNota30Conceito(List<SelectItem> listaSelectItemNota30Conceito) {
		this.listaSelectItemNota30Conceito = listaSelectItemNota30Conceito;
	}

	public List<SelectItem> getListaSelectItemNota31Conceito() {
		if (listaSelectItemNota31Conceito == null) {
			listaSelectItemNota31Conceito = new ArrayList<SelectItem>();
		}
		return listaSelectItemNota31Conceito;
	}

	public void setListaSelectItemNota31Conceito(List<SelectItem> listaSelectItemNota31Conceito) {
		this.listaSelectItemNota31Conceito = listaSelectItemNota31Conceito;
	}

	public List<SelectItem> getListaSelectItemNota32Conceito() {
		if (listaSelectItemNota32Conceito == null) {
			listaSelectItemNota32Conceito = new ArrayList<SelectItem>();
		}
		return listaSelectItemNota32Conceito;
	}

	public void setListaSelectItemNota32Conceito(List<SelectItem> listaSelectItemNota32Conceito) {
		this.listaSelectItemNota32Conceito = listaSelectItemNota32Conceito;
	}

	public List<SelectItem> getListaSelectItemNota33Conceito() {
		if (listaSelectItemNota33Conceito == null) {
			listaSelectItemNota33Conceito = new ArrayList<SelectItem>();
		}
		return listaSelectItemNota33Conceito;
	}

	public void setListaSelectItemNota33Conceito(List<SelectItem> listaSelectItemNota33Conceito) {
		this.listaSelectItemNota33Conceito = listaSelectItemNota33Conceito;
	}

	public List<SelectItem> getListaSelectItemNota34Conceito() {
		if (listaSelectItemNota34Conceito == null) {
			listaSelectItemNota34Conceito = new ArrayList<SelectItem>();
		}
		return listaSelectItemNota34Conceito;
	}

	public void setListaSelectItemNota34Conceito(List<SelectItem> listaSelectItemNota34Conceito) {
		this.listaSelectItemNota34Conceito = listaSelectItemNota34Conceito;
	}

	public List<SelectItem> getListaSelectItemNota35Conceito() {
		if (listaSelectItemNota35Conceito == null) {
			listaSelectItemNota35Conceito = new ArrayList<SelectItem>();
		}
		return listaSelectItemNota35Conceito;
	}

	public void setListaSelectItemNota35Conceito(List<SelectItem> listaSelectItemNota35Conceito) {
		this.listaSelectItemNota35Conceito = listaSelectItemNota35Conceito;
	}

	public List<SelectItem> getListaSelectItemNota36Conceito() {
		if (listaSelectItemNota36Conceito == null) {
			listaSelectItemNota36Conceito = new ArrayList<SelectItem>();
		}
		return listaSelectItemNota36Conceito;
	}

	public void setListaSelectItemNota36Conceito(List<SelectItem> listaSelectItemNota36Conceito) {
		this.listaSelectItemNota36Conceito = listaSelectItemNota36Conceito;
	}

	public List<SelectItem> getListaSelectItemNota37Conceito() {
		if (listaSelectItemNota37Conceito == null) {
			listaSelectItemNota37Conceito = new ArrayList<SelectItem>();
		}
		return listaSelectItemNota37Conceito;
	}

	public void setListaSelectItemNota37Conceito(List<SelectItem> listaSelectItemNota37Conceito) {
		this.listaSelectItemNota37Conceito = listaSelectItemNota37Conceito;
	}

	public List<SelectItem> getListaSelectItemNota38Conceito() {
		if (listaSelectItemNota38Conceito == null) {
			listaSelectItemNota38Conceito = new ArrayList<SelectItem>();
		}
		return listaSelectItemNota38Conceito;
	}

	public void setListaSelectItemNota38Conceito(List<SelectItem> listaSelectItemNota38Conceito) {
		this.listaSelectItemNota38Conceito = listaSelectItemNota38Conceito;
	}

	public List<SelectItem> getListaSelectItemNota39Conceito() {
		if (listaSelectItemNota39Conceito == null) {
			listaSelectItemNota39Conceito = new ArrayList<SelectItem>();
		}
		return listaSelectItemNota39Conceito;
	}

	public void setListaSelectItemNota39Conceito(List<SelectItem> listaSelectItemNota39Conceito) {
		this.listaSelectItemNota39Conceito = listaSelectItemNota39Conceito;
	}

	public List<SelectItem> getListaSelectItemNota40Conceito() {
		if (listaSelectItemNota40Conceito == null) {
			listaSelectItemNota40Conceito = new ArrayList<SelectItem>();
		}
		return listaSelectItemNota40Conceito;
	}

	public void setListaSelectItemNota40Conceito(List<SelectItem> listaSelectItemNota40Conceito) {
		this.listaSelectItemNota40Conceito = listaSelectItemNota40Conceito;
	}

	public void adicionarConfiguracaoAcademicoNotaConceito31() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO31(), TipoNotaConceitoEnum.NOTA_31, getConfiguracaoAcademicoVO().getNota31MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO31(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito32() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO32(), TipoNotaConceitoEnum.NOTA_32, getConfiguracaoAcademicoVO().getNota32MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO32(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito33() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO33(), TipoNotaConceitoEnum.NOTA_33, getConfiguracaoAcademicoVO().getNota33MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO33(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito34() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO34(), TipoNotaConceitoEnum.NOTA_34, getConfiguracaoAcademicoVO().getNota34MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO34(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito35() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO35(), TipoNotaConceitoEnum.NOTA_35, getConfiguracaoAcademicoVO().getNota35MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO35(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito36() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO36(), TipoNotaConceitoEnum.NOTA_36, getConfiguracaoAcademicoVO().getNota36MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO36(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito37() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO37(), TipoNotaConceitoEnum.NOTA_37, getConfiguracaoAcademicoVO().getNota37MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO37(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito38() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO38(), TipoNotaConceitoEnum.NOTA_38, getConfiguracaoAcademicoVO().getNota38MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO38(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito39() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO39(), TipoNotaConceitoEnum.NOTA_39, getConfiguracaoAcademicoVO().getNota39MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO39(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarConfiguracaoAcademicoNotaConceito40() {
		try {
			getFacadeFactory().getConfiguracaoAcademicoFacade().adicionarConfiguracaoAcademicoNotaConceito(getConfiguracaoAcademicoVO(), getConfiguracaoAcademicoNotaConceitoVO40(), TipoNotaConceitoEnum.NOTA_40, getConfiguracaoAcademicoVO().getNota40MediaFinal());
			setConfiguracaoAcademicoNotaConceitoVO40(new ConfiguracaoAcademicoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void clonar(){
		try {
			setConfiguracaoAcademicoVO(getConfiguracaoAcademicoVO().clone());
			getConfiguracaoAcademicoVO().setNome(getConfiguracaoAcademicoVO().getNome()+" - clone "+Uteis.getAnoDataAtual()+"/"+Uteis.getSemestreAtual());
			getConfiguracaoAcademicoVO().setConfiguracoesVO(getConfiguracoesVO());
			setMensagemID("msg_dados_clonados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		
	}
	
	public void editarEclonar(){
		editarSelecionado();
		clonar();
	}
	
	public List<HistoricoVO> getHistoricoFilhaComposicaoVOs() {
		if (historicoFilhaComposicaoVOs == null) {
			historicoFilhaComposicaoVOs = new ArrayList<HistoricoVO>(0);
		}
		return historicoFilhaComposicaoVOs;
	}

	public void setHistoricoFilhaComposicaoVOs(List<HistoricoVO> historicoFilhaComposicaoVOs) {
		this.historicoFilhaComposicaoVOs = historicoFilhaComposicaoVOs;
	}

	public Boolean getAbrirModalDefinicaoDisciplinaFilhaComposicao() {
		if (abrirModalDefinicaoDisciplinaFilhaComposicao == null) {
			abrirModalDefinicaoDisciplinaFilhaComposicao = false;
		}
		return abrirModalDefinicaoDisciplinaFilhaComposicao;
	}

	public void setAbrirModalDefinicaoDisciplinaFilhaComposicao(Boolean abrirModalDefinicaoDisciplinaFilhaComposicao) {
		this.abrirModalDefinicaoDisciplinaFilhaComposicao = abrirModalDefinicaoDisciplinaFilhaComposicao;
	}

	public void realizarCriacaoHistoricoParaValidacaoConnfiguracaoAcademica() {		
		try {
			limparMensagem();
			setHistoricoFilhaComposicaoVOs(getFacadeFactory().getHistoricoFacade().realizarCriacaoHistoricoParaValidacaoConnfiguracaoAcademica(getConfiguracaoAcademicoVO().getQtdeDisciplinaFilhaComposicao(), getHistoricoFilhaComposicaoVOs()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void checarFormatarValoresNota(HistoricoVO historicoVO, Object nota, Boolean notaConceito, int numeroNota) {
		limparMensagem();
		try {
			if (nota != null) {
				if(notaConceito) {
					UtilReflexao.invocarMetodo(historicoVO, "setNota"+numeroNota+"Conceito", nota);
				}else {
					UtilReflexao.invocarMetodo(historicoVO, "setNota"+numeroNota, nota);
					getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoVO, historicoVO.getHistoricoDisciplinaFilhaComposicaoVOs(), historicoVO.getConfiguracaoAcademico(), numeroNota);
				}
			}else {
				if(notaConceito) {
					UtilReflexao.invocarMetodoSetParametroNull(historicoVO, "nota"+numeroNota+"Conceito");
				}else {
					UtilReflexao.invocarMetodoSetParametroNull(historicoVO, "nota"+numeroNota);
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public List<SelectItem> listaSelecItemConfiguracaoAcademico;

	public List<SelectItem> getListaSelecItemConfiguracaoAcademico() {
		if (listaSelecItemConfiguracaoAcademico == null) {
			listaSelecItemConfiguracaoAcademico = new ArrayList<SelectItem>(0);
			for(ConfiguracaoAcademicoVO configuracaoAcademicoVO: getConfiguracaoAcademicoVOs()) {
				listaSelecItemConfiguracaoAcademico.add(new SelectItem(configuracaoAcademicoVO.getCodigo(), configuracaoAcademicoVO.getCodigo()+" - "+configuracaoAcademicoVO.getNome()));
			}
		}
		return listaSelecItemConfiguracaoAcademico;
	}

	public void setListaSelecItemConfiguracaoAcademico(List<SelectItem> listaSelecItemConfiguracaoAcademico) {
		this.listaSelecItemConfiguracaoAcademico = listaSelecItemConfiguracaoAcademico;
	}

	public void validarDadosTesteConfiguracaoAcademico() {
		try {
			setAbrirModalTesteConfiguracaoAcademica(false);
			if(getAbrirModalDefinicaoDisciplinaFilhaComposicao()) {
				if(!Uteis.isAtributoPreenchido(getConfiguracaoAcademicoVO().getQtdeDisciplinaFilhaComposicao())) {
					throw new Exception("Deve ser informado a quantidade de disciplina filha da composição para o teste desta configuração.");
				}
				int ch = 0;
				int cr = 0;
				for(HistoricoVO historicoVO: getHistoricoFilhaComposicaoVOs()) {
					historicoVO.setSituacao("CS");
					historicoVO.setMediaFinal(null);
					historicoVO.getHistoricoNotaVOs().clear();
					for(int i = 1; i <=40; i++) {
						UtilReflexao.invocarMetodoSetParametroNull(historicoVO, "nota"+i);						
					}					
					if(!Uteis.isAtributoPreenchido(historicoVO.getConfiguracaoAcademico().getCodigo())) {
						throw new Exception("Deve ser informado a configuração acadêmica da disciplina "+historicoVO.getNomeDisciplina()+".");
					}else {
						historicoVO.setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(historicoVO.getConfiguracaoAcademico().getCodigo(), getUsuarioLogado()));
						historicoVO.getConfiguracaoAcademico().setNivelMontarDados(NivelMontarDados.TODOS);
					}
					ch += historicoVO.getCargaHorariaDisciplina();
					cr += historicoVO.getCreditoDisciplina();
				}
				getHistoricoVO().setCargaHorariaDisciplina(ch);
				getHistoricoVO().setCreditoDisciplina(cr);
				getHistoricoVO().setHistoricoDisciplinaComposta(true);
				getHistoricoVO().setHistoricoDisciplinaFilhaComposicaoVOs(getHistoricoFilhaComposicaoVOs());
			}
			List<HistoricoVO> historicos = new ArrayList<HistoricoVO>(0);
			historicos.add(getHistoricoVO());
			getFacadeFactory().getHistoricoFacade().realizarVerificacaoBloqueiNotaDisciplinaComposta(historicos, getConfiguracaoAcademicaVO(), getUsuario());
			getFacadeFactory().getConfiguracaoAcademicoNotaFacade().replicarInformacaoConfiguracaoAcademicoParaConfiguracaoAcademicoNota(getConfiguracaoAcademicoVO());
			ConfiguracaoAcademicoVO.validarDados(getConfiguracaoAcademicoVO(), getHistoricoVO(), getHistoricoFilhaComposicaoVOs());
			getConfiguracaoAcademicoVO().setConfiguracaoAcademicaNotaUtilizarVOs(null);
			getHistoricoVO().setConfiguracaoAcademico(getConfiguracaoAcademicoVO());			
			for (int y = 1; y <= 40; y++) {
				if ((Boolean) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "utilizarNota" + y)) {
					ConfiguracaoAcademicaNotaVO conf = (ConfiguracaoAcademicaNotaVO) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "configuracaoAcademicaNota" + y + "VO");
						conf.setNotaDigitada(null);
						conf.setNotaConceitoSelecionado(null);
						if(!getAbrirModalDefinicaoDisciplinaFilhaComposicao()) {
							getFacadeFactory().getConfiguracaoAcademicoNotaFacade().validarDados(configuracaoAcademicoVO, conf);
						}
					}
				}
			
			setAbrirModalTesteConfiguracaoAcademica(true);
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String getModalTesteConfiguracaoAcademicaComposta() {
		return getAbrirModalDefinicaoDisciplinaFilhaComposicao() ? "RichFaces.$('modalTesteConfiguracaoComposta').show()" : "";
	}
	
	public List<SelectItem> listaSelectItemTipoUsoConfiguracaoAcademica;
	public List<SelectItem> listaSelectItemRegraCalculoDisciplinaComposta;

	public List<SelectItem> getListaSelectItemTipoUsoConfiguracaoAcademica() {
		if (listaSelectItemTipoUsoConfiguracaoAcademica == null) {
			listaSelectItemTipoUsoConfiguracaoAcademica = new ArrayList<SelectItem>(0);
			listaSelectItemTipoUsoConfiguracaoAcademica.add(new SelectItem(TipoUsoConfiguracaoAcademicoEnum.GERAL, TipoUsoConfiguracaoAcademicoEnum.GERAL.getValorApresentar()));
			listaSelectItemTipoUsoConfiguracaoAcademica.add(new SelectItem(TipoUsoConfiguracaoAcademicoEnum.COMPOSTA, TipoUsoConfiguracaoAcademicoEnum.COMPOSTA.getValorApresentar()));
			listaSelectItemTipoUsoConfiguracaoAcademica.add(new SelectItem(TipoUsoConfiguracaoAcademicoEnum.FILHA_COMPOSICAO, TipoUsoConfiguracaoAcademicoEnum.FILHA_COMPOSICAO.getValorApresentar()));
		}
		return listaSelectItemTipoUsoConfiguracaoAcademica;
	}

	public void setListaSelectItemTipoUsoConfiguracaoAcademica(List<SelectItem> listaSelectItemTipoUsoConfiguracaoAcademica) {
		this.listaSelectItemTipoUsoConfiguracaoAcademica = listaSelectItemTipoUsoConfiguracaoAcademica;
	}

	public List<SelectItem> getListaSelectItemRegraCalculoDisciplinaComposta() {
		if (listaSelectItemRegraCalculoDisciplinaComposta == null) {
			listaSelectItemRegraCalculoDisciplinaComposta =  new ArrayList<SelectItem>(0);
			listaSelectItemRegraCalculoDisciplinaComposta.add(new SelectItem(RegraCalculoDisciplinaCompostaEnum.MEDIA_FILHA_COMPOSICAO, RegraCalculoDisciplinaCompostaEnum.MEDIA_FILHA_COMPOSICAO.getValorApresentar()));
			listaSelectItemRegraCalculoDisciplinaComposta.add(new SelectItem(RegraCalculoDisciplinaCompostaEnum.FUNCAO_COMPOSICAO, RegraCalculoDisciplinaCompostaEnum.FUNCAO_COMPOSICAO.getValorApresentar()));
		}
		return listaSelectItemRegraCalculoDisciplinaComposta;
	}

	public void setListaSelectItemRegraCalculoDisciplinaComposta(List<SelectItem> listaSelectItemRegraCalculoDisciplinaComposta) {
		this.listaSelectItemRegraCalculoDisciplinaComposta = listaSelectItemRegraCalculoDisciplinaComposta;
	}
	

	private List<SelectItem> listaSelectFormulaCalculoNota;

	/**
	 * @return the listaSelectFormulaCalculoNota
	 */
	public List<SelectItem> getListaSelectFormulaCalculoNota() {
		if (listaSelectFormulaCalculoNota == null) {
			listaSelectFormulaCalculoNota = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(FormulaCalculoNotaEnum.class, "name", "valorApresentar", false);
		}
		return listaSelectFormulaCalculoNota;
	}
	
	public boolean getApresentarVariavelNotaDisciplinaComposta(){		
		return getHistoricoVO().getGradeDisciplinaVO().getFormulaCalculoNota().equals(FormulaCalculoNotaEnum.FORMULA_CALCULO) || (getHistoricoVO().getGradeDisciplinaVO().getApresentarOpcaoRecuperacao() && getHistoricoVO().getGradeDisciplinaVO().getControlarRecuperacaoPelaDisciplinaPrincipal());
	}
	
	private StringBuilder logCalculoNota;

	public StringBuilder getLogCalculoNota() {
		if (logCalculoNota == null) {
			logCalculoNota = new StringBuilder("");
		}
		return logCalculoNota;
	}

	public void setLogCalculoNota(StringBuilder logCalculoNota) {
		this.logCalculoNota = logCalculoNota;
	}
	
	public List<SelectItem> getListaSelectItemTipoUsoNota() {
		return TipoUsoNotaEnum.getListaSelectItemItemTipoUsoNota();
	}
	
	
	

	
   public List<String> getListaTagsMascaraNumeroProcessoRegistroExpedicaoDiploma(){	   
	   return new ExpedicaoDiplomaVO().getListaTagMascaraNumeroRegistroExpedicaoDiploma();
   }


	public String getValorCampoMascaraNumeroProcessoExpedicaoDiploma() {
		if(valorCampoMascaraNumeroProcessoExpedicaoDiploma == null) {
			valorCampoMascaraNumeroProcessoExpedicaoDiploma = "";
		}
		return valorCampoMascaraNumeroProcessoExpedicaoDiploma;
	}



	public void setValorCampoMascaraNumeroProcessoExpedicaoDiploma(String valorCampoMascaraNumeroProcessoExpedicaoDiploma) {
		this.valorCampoMascaraNumeroProcessoExpedicaoDiploma = valorCampoMascaraNumeroProcessoExpedicaoDiploma;
	}



	public List<String> getListaNumeroProcessoExpedicaoDiploma1() {
		  if(listaNumeroProcessoExpedicaoDiploma1 == null ) {
			  listaNumeroProcessoExpedicaoDiploma1 = new ArrayList<String>();
		  }
		return listaNumeroProcessoExpedicaoDiploma1;
	}



	public void setListaNumeroProcessoExpedicaoDiploma1(List<String> listaNumeroProcessoExpedicaoDiploma1) {		
		this.listaNumeroProcessoExpedicaoDiploma1 = listaNumeroProcessoExpedicaoDiploma1;
	}



	public List<String> getListaNumeroProcessoExpedicaoDiploma2() {
		if(listaNumeroProcessoExpedicaoDiploma2 == null) {
			listaNumeroProcessoExpedicaoDiploma2 = new ArrayList<String>();
		}
		return listaNumeroProcessoExpedicaoDiploma2;
	}



	public void setListaNumeroProcessoExpedicaoDiploma2(List<String> listaNumeroProcessoExpedicaoDiploma2) {
		this.listaNumeroProcessoExpedicaoDiploma2 = listaNumeroProcessoExpedicaoDiploma2;
	}
	
	public void adicionarTagMascara() {
		String  obj = (String) context().getExternalContext().getRequestMap().get("mascaraItens");
		getListaNumeroProcessoExpedicaoDiploma1().remove(obj);
		if(!getListaNumeroProcessoExpedicaoDiploma2().contains(obj)) {
			getListaNumeroProcessoExpedicaoDiploma2().add(obj);
		}
		
		setValorCampoMascaraNumeroProcessoExpedicaoDiploma(getValorCampoMascaraNumeroProcessoExpedicaoDiploma() + obj);
		//this.getConfiguracaoAcademicoVO().setMascaraNumeroProcessoExpedicaoDiploma(getValorCampoMascaraNumeroProcessoExpedicaoDiploma());
	}
	
	
	public void removerTagMascara() {
		String  obj = (String) context().getExternalContext().getRequestMap().get("mascaraItens");
		if(!getListaNumeroProcessoExpedicaoDiploma1().contains(obj)) {
		getListaNumeroProcessoExpedicaoDiploma1().add(obj);
		}
		getListaNumeroProcessoExpedicaoDiploma2().remove(obj);
		
	}
	public void adicionarTagMascaraRegistroDiploma() {
		String  obj = (String) context().getExternalContext().getRequestMap().get("mascaraNregistroItens");
		getListaNumeroRegistroDiploma1().remove(obj);
		if(!getListaNumeroRegistroDiploma2().contains(obj)) {
			getListaNumeroRegistroDiploma2().add(obj);
		}
		
		setValorCampoMascaraNumeroRegistroDiploma(getValorCampoMascaraNumeroRegistroDiploma() + obj);
		
	}
	
	
	public void removerTagMascaraRegistroDiploma() {
		String  obj = (String) context().getExternalContext().getRequestMap().get("mascaraNregistroItens");
		if(!getListaNumeroRegistroDiploma1().contains(obj)) {
			getListaNumeroRegistroDiploma1().add(obj);
		}
		getListaNumeroRegistroDiploma2().remove(obj);
		
	}
	
	public void montarListaTagsNumeroProcesso() {
		if(this.getConfiguracaoAcademicoVO().getMascaraNumeroProcessoExpedicaoDiploma().isEmpty()) {			
			setListaNumeroProcessoExpedicaoDiploma1(new ExpedicaoDiplomaVO().getListaTagMascaraNumeroProcessoExpedicaoDiploma());
			getListaNumeroProcessoExpedicaoDiploma2().clear();
		}else {
			getListaNumeroProcessoExpedicaoDiploma1().clear();
			getListaNumeroProcessoExpedicaoDiploma2().clear();
			if(this.getConfiguracaoAcademicoVO().getMascaraNumeroProcessoExpedicaoDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.MATRICULA.getValor())) {
				getListaNumeroProcessoExpedicaoDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.MATRICULA.getValor());
			}else {
				getListaNumeroProcessoExpedicaoDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.MATRICULA.getValor());
			}
	        if(this.getConfiguracaoAcademicoVO().getMascaraNumeroProcessoExpedicaoDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_EXPEDICAO.getValor())) {
	        	getListaNumeroProcessoExpedicaoDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_EXPEDICAO.getValor());
			}else {
				getListaNumeroProcessoExpedicaoDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_EXPEDICAO.getValor());
			}
			if(this.getConfiguracaoAcademicoVO().getMascaraNumeroProcessoExpedicaoDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO_ATUAL.getValor())) {
				getListaNumeroProcessoExpedicaoDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO_ATUAL.getValor());
			}else {
				getListaNumeroProcessoExpedicaoDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO_ATUAL.getValor());
			}
			if(this.getConfiguracaoAcademicoVO().getMascaraNumeroProcessoExpedicaoDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE_ATUAL.getValor())) {
				getListaNumeroProcessoExpedicaoDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE_ATUAL.getValor());
			}else {
				getListaNumeroProcessoExpedicaoDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE_ATUAL.getValor());
			}
	        if(this.getConfiguracaoAcademicoVO().getMascaraNumeroProcessoExpedicaoDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO.getValor())) {
	        	getListaNumeroProcessoExpedicaoDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO.getValor());
	        }else {
	        	getListaNumeroProcessoExpedicaoDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO.getValor());
	        }
	        if(this.getConfiguracaoAcademicoVO().getMascaraNumeroProcessoExpedicaoDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE.getValor())) {
	        	getListaNumeroProcessoExpedicaoDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE.getValor());
	        }else {
	        	getListaNumeroProcessoExpedicaoDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE.getValor());
	        }
	        if(this.getConfiguracaoAcademicoVO().getMascaraNumeroProcessoExpedicaoDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ABREV_NOME_CURSO.getValor())) {
	        	getListaNumeroProcessoExpedicaoDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ABREV_NOME_CURSO.getValor());
	        }else {
	        	getListaNumeroProcessoExpedicaoDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ABREV_NOME_CURSO.getValor());
	        }
	        if(this.getConfiguracaoAcademicoVO().getMascaraNumeroProcessoExpedicaoDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_CURSO.getValor())) {
				getListaNumeroProcessoExpedicaoDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_CURSO.getValor());
			}else {
				getListaNumeroProcessoExpedicaoDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_CURSO.getValor());
			}
			if(this.getConfiguracaoAcademicoVO().getMascaraNumeroProcessoExpedicaoDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_IES.getValor())) {
				getListaNumeroProcessoExpedicaoDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_IES.getValor());
			}else {
				getListaNumeroProcessoExpedicaoDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_IES.getValor());
			}
			if(this.getConfiguracaoAcademicoVO().getMascaraNumeroProcessoExpedicaoDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_CURSO.getValor())) {
				getListaNumeroProcessoExpedicaoDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_CURSO.getValor());
			}else {
				getListaNumeroProcessoExpedicaoDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_CURSO.getValor());
			}
			if(this.getConfiguracaoAcademicoVO().getMascaraNumeroProcessoExpedicaoDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_IES.getValor())) {
				getListaNumeroProcessoExpedicaoDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_IES.getValor());
			}else {
				getListaNumeroProcessoExpedicaoDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_IES.getValor());
			}
			if(this.getConfiguracaoAcademicoVO().getMascaraNumeroProcessoExpedicaoDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_REGISTRO_DIPLOMA.getValor())) {
				getListaNumeroProcessoExpedicaoDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_REGISTRO_DIPLOMA.getValor());
			}else {
				getListaNumeroProcessoExpedicaoDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_REGISTRO_DIPLOMA.getValor());
			}
			if(this.getConfiguracaoAcademicoVO().getMascaraNumeroProcessoExpedicaoDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_FOLHA_LIVRO.getValor())) {
				getListaNumeroProcessoExpedicaoDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_FOLHA_LIVRO.getValor());
			}else {
				getListaNumeroProcessoExpedicaoDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_FOLHA_LIVRO.getValor());
			}

		}		
	}
	public void montarListaTagsNumeroRegistroDiploma() {
		if(this.getConfiguracaoAcademicoVO().getMascaraNumeroRegistroDiploma().isEmpty()) {			
			setListaNumeroRegistroDiploma1(new ExpedicaoDiplomaVO().getListaTagMascaraNumeroRegistroExpedicaoDiploma());
			getListaNumeroRegistroDiploma2().clear();
		}else {
			getListaNumeroRegistroDiploma1().clear();
			getListaNumeroRegistroDiploma2().clear();
			if(this.getConfiguracaoAcademicoVO().getMascaraNumeroRegistroDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.MATRICULA.getValor())) {
				getListaNumeroRegistroDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.MATRICULA.getValor());
			}else {
				getListaNumeroRegistroDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.MATRICULA.getValor());
			}
			if(this.getConfiguracaoAcademicoVO().getMascaraNumeroRegistroDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_EXPEDICAO.getValor())) {
				getListaNumeroRegistroDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_EXPEDICAO.getValor());
			}else {
				getListaNumeroRegistroDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_EXPEDICAO.getValor());
			}
			if(this.getConfiguracaoAcademicoVO().getMascaraNumeroRegistroDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO_ATUAL.getValor())) {
				getListaNumeroRegistroDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO_ATUAL.getValor());
			}else {
				getListaNumeroRegistroDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO_ATUAL.getValor());
			}
			if(this.getConfiguracaoAcademicoVO().getMascaraNumeroRegistroDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE_ATUAL.getValor())) {
				getListaNumeroRegistroDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE_ATUAL.getValor());
			}else {
				getListaNumeroRegistroDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE_ATUAL.getValor());
			}
			if(this.getConfiguracaoAcademicoVO().getMascaraNumeroRegistroDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO.getValor())) {
				getListaNumeroRegistroDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO.getValor());
			}else {
				getListaNumeroRegistroDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO.getValor());
			}
			if(this.getConfiguracaoAcademicoVO().getMascaraNumeroRegistroDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE.getValor())) {
				getListaNumeroRegistroDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE.getValor());
			}else {
				getListaNumeroRegistroDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE.getValor());
			}
			if(this.getConfiguracaoAcademicoVO().getMascaraNumeroRegistroDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ABREV_NOME_CURSO.getValor())) {
				getListaNumeroRegistroDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ABREV_NOME_CURSO.getValor());
			}else {
				getListaNumeroRegistroDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ABREV_NOME_CURSO.getValor());
			}
			if(this.getConfiguracaoAcademicoVO().getMascaraNumeroRegistroDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_PROCESSO.getValor())) {
				getListaNumeroRegistroDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_PROCESSO.getValor());
			}else {
				getListaNumeroRegistroDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_PROCESSO.getValor());
			}
			if(this.getConfiguracaoAcademicoVO().getMascaraNumeroRegistroDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_CURSO.getValor())) {
				getListaNumeroRegistroDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_CURSO.getValor());
			}else {
				getListaNumeroRegistroDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_CURSO.getValor());
			}
			if(this.getConfiguracaoAcademicoVO().getMascaraNumeroRegistroDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_IES.getValor())) {
				getListaNumeroRegistroDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_IES.getValor());
			}else {
				getListaNumeroRegistroDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_IES.getValor());
			}
			if(this.getConfiguracaoAcademicoVO().getMascaraNumeroRegistroDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_CURSO.getValor())) {
				getListaNumeroRegistroDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_CURSO.getValor());
			}else {
				getListaNumeroRegistroDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_CURSO.getValor());
			}
			if(this.getConfiguracaoAcademicoVO().getMascaraNumeroRegistroDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_IES.getValor())) {
				getListaNumeroRegistroDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_IES.getValor());
			}else {
				getListaNumeroRegistroDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_IES.getValor());
			}
			if(this.getConfiguracaoAcademicoVO().getMascaraNumeroRegistroDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_REGISTRO_DIPLOMA.getValor())) {
				getListaNumeroRegistroDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_REGISTRO_DIPLOMA.getValor());
			}else {
				getListaNumeroRegistroDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_REGISTRO_DIPLOMA.getValor());
			}
			if(this.getConfiguracaoAcademicoVO().getMascaraNumeroRegistroDiploma().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_FOLHA_LIVRO.getValor())) {
				getListaNumeroRegistroDiploma2().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_FOLHA_LIVRO.getValor());
			}else {
				getListaNumeroRegistroDiploma1().add(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_FOLHA_LIVRO.getValor());
			}

		}		
	}
	
	public void limparCampoNumeroProcessoExpedicaoDiploma() {
		this.getConfiguracaoAcademicoVO().setMascaraNumeroProcessoExpedicaoDiploma("");
		setValorCampoMascaraNumeroProcessoExpedicaoDiploma("");
	}
	
	
	public void preencherCampoMascaraNumeroProcesso() {
		
		String campoMascaraNProcesso="";
		for(String campoMascara : getListaNumeroProcessoExpedicaoDiploma2()) {	
			if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO_ATUAL.getValor())) {
				campoMascaraNProcesso += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO_ATUAL.getValor() +"  ";
				continue;
			}
			if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE_ATUAL.getValor())) {
				campoMascaraNProcesso += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE_ATUAL.getValor() +"  ";
				continue;
			}
			if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.MATRICULA.getValor())) {
				campoMascaraNProcesso += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.MATRICULA.getValor() +"  ";
				continue;
			}
			if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ABREV_NOME_CURSO.getValor())) {
	        	campoMascaraNProcesso += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ABREV_NOME_CURSO.getValor() +"  ";
	        	continue;
	        }
			
			if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO.getValor())) {
				campoMascaraNProcesso += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO.getValor() +"  ";
				continue;
			}
	        if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_EXPEDICAO.getValor())) {
	        	campoMascaraNProcesso += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_EXPEDICAO.getValor() +"  ";
	        	continue;
			}
	        if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE.getValor())) {
	        	campoMascaraNProcesso += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE.getValor() +"  ";
	        	continue;
	        }
	        if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_CURSO.getValor())) {
	        	campoMascaraNProcesso += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_CURSO.getValor() +"  ";
	        	continue;
	        }
	        if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_IES.getValor())) {
	        	campoMascaraNProcesso += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_IES.getValor() +"  ";
	        	continue;
	        }
	        if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_CURSO.getValor())) {
	        	campoMascaraNProcesso += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_CURSO.getValor() +"  ";
	        	continue;
	        }
	        if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_IES.getValor())) {
	        	campoMascaraNProcesso += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_IES.getValor() +"  ";
	        	continue;
	        }
	        if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_REGISTRO_DIPLOMA.getValor())) {
	        	campoMascaraNProcesso += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_REGISTRO_DIPLOMA.getValor() +"  ";
	        	continue;
	        }
	        if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_FOLHA_LIVRO.getValor())) {
	        	campoMascaraNProcesso += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_FOLHA_LIVRO.getValor() +"  ";
	        	continue;
	        }
		
		}
		
        
	this.getConfiguracaoAcademicoVO().setMascaraNumeroProcessoExpedicaoDiploma(campoMascaraNProcesso);
	}
	
	
	public void limparCampoNumeroRegistroDiploma() {
		this.getConfiguracaoAcademicoVO().setMascaraNumeroRegistroDiploma("");
		setValorCampoMascaraNumeroRegistroDiploma("");
	}
	
	
	public void preencherCampoMascaraNumeroRegistroDiploma() {
		
		String campoMascaraNRegistro="";
		for(String campoMascara : getListaNumeroRegistroDiploma2()) {	
			if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO_ATUAL.getValor())) {
				campoMascaraNRegistro += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO_ATUAL.getValor() +"  ";
				continue;
			}
			if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE_ATUAL.getValor())) {
				campoMascaraNRegistro += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE_ATUAL.getValor() +"  ";
				continue;
			}
			if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.MATRICULA.getValor())) {
				campoMascaraNRegistro += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.MATRICULA.getValor() +"  ";
				continue;
			}
			if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ABREV_NOME_CURSO.getValor())) {
				campoMascaraNRegistro += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ABREV_NOME_CURSO.getValor() +"  ";
				continue;
			}
			if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO.getValor())) {
				campoMascaraNRegistro += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO.getValor() +"  ";
				continue;
			}
			if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_EXPEDICAO.getValor())) {
				campoMascaraNRegistro += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_EXPEDICAO.getValor() +"  ";
				continue;
			}
			if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE.getValor())) {
				campoMascaraNRegistro += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE.getValor() +"  ";
				continue;
			}
			if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_PROCESSO.getValor())) {
				campoMascaraNRegistro += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_PROCESSO.getValor() +"  ";
				continue;
			}
			if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_CURSO.getValor())) {
				campoMascaraNRegistro += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_CURSO.getValor() +"  ";
				continue;
			}
			if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_IES.getValor())) {
				campoMascaraNRegistro += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_IES.getValor() +"  ";
				continue;
			}
			if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_CURSO.getValor())) {
				campoMascaraNRegistro += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_CURSO.getValor() +"  ";
				continue;
			}
			if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_IES.getValor())) {
				campoMascaraNRegistro += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_IES.getValor() +"  ";
				continue;
			}
			if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_REGISTRO_DIPLOMA.getValor())) {
				campoMascaraNRegistro += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_REGISTRO_DIPLOMA.getValor() +"  ";
				continue;
			}
			if(campoMascara.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_FOLHA_LIVRO.getValor())) {
				campoMascaraNRegistro += ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_FOLHA_LIVRO.getValor() +"  ";
				continue;
			}
			
		}
		
		
		this.getConfiguracaoAcademicoVO().setMascaraNumeroRegistroDiploma(campoMascaraNRegistro);
	}
	
	/**
	 * @author Felipe
	 *  
	 *  @apiNote Filreos de boolean de notas
	 *  
	 * @return
	 */
	public Boolean getFiltrarNotas() {
		if (filtrarNotas == null) {
			filtrarNotas = false;
		}
		return filtrarNotas;
	}
	
	public void setFiltrarNotas(Boolean filtrarNotas) {
		this.filtrarNotas = filtrarNotas;
	}
	
	public Boolean getFiltrarNotas2() {
		if (filtrarNotas2 == null) {
			filtrarNotas2 = false;
		}
		return filtrarNotas2;
	}
	
	public void setFiltrarNotas2(Boolean filtrarNotas2) {
		this.filtrarNotas2 = filtrarNotas2;
	}
	
	public Boolean getFiltrarNotas3() {
		if (filtrarNotas3 == null) {
			filtrarNotas3 = false;
		}
		return filtrarNotas3;
	}
	
	public void setFiltrarNotas3(Boolean filtrarNotas3) {
		this.filtrarNotas3 = filtrarNotas3;
	}
	
	public Boolean getFiltrarNotas4() {
		if (filtrarNotas4 == null) {
			filtrarNotas4 = false;
		}
		return filtrarNotas4;
	}
	
	public void setFiltrarNotas4(Boolean filtrarNotas4) {
		this.filtrarNotas4 = filtrarNotas4;
	}
	public Boolean getFiltrarNotas5() {
		if (filtrarNotas5 == null) {
			filtrarNotas5 = false;
		}
		return filtrarNotas5;
	}
	
	public void setFiltrarNotas5(Boolean filtrarNotas5) {
		this.filtrarNotas5 = filtrarNotas5;
	}
	public Boolean getFiltrarNotas6() {
		if (filtrarNotas6 == null) {
			filtrarNotas6 = false;
		}
		return filtrarNotas6;
	}
	
	public void setFiltrarNotas6(Boolean filtrarNotas6) {
		this.filtrarNotas6 = filtrarNotas6;
	}
	
	public Boolean getFiltrarNotas7() {
		if (filtrarNotas7 == null) {
			filtrarNotas7 = false;
		}
		return filtrarNotas7;
	}
	
	public void setFiltrarNotas7(Boolean filtrarNotas7) {
		this.filtrarNotas7 = filtrarNotas7;
	}
	
	public Boolean getFiltrarNotas8() {
		if (filtrarNotas8 == null) {
			filtrarNotas8 = false;
		}
		return filtrarNotas8;
	}
	
	public void setFiltrarNotas8(Boolean filtrarNotas8) {
		this.filtrarNotas8 = filtrarNotas8;
	}
	
	public Boolean getFiltrarNotas9() {
		if (filtrarNotas9 == null) {
			filtrarNotas9 = false;
		}
		return filtrarNotas9;
	}
	
	public void setFiltrarNotas9(Boolean filtrarNotas9) {
		this.filtrarNotas9 = filtrarNotas9;
	}
	
	public Boolean getFiltrarNotas10() {
		if (filtrarNotas10 == null) {
			filtrarNotas10 = false;
		}
		return filtrarNotas10;
	}
	
	public void setFiltrarNotas10(Boolean filtrarNotas10) {
		this.filtrarNotas10 = filtrarNotas10;
	}



	public List<String> getListaNumeroRegistroDiploma1() {
		if(listaNumeroRegistroDiploma1 == null ) {
			listaNumeroRegistroDiploma1 = new ArrayList<String>(0);
		}
		return listaNumeroRegistroDiploma1;
	}



	public void setListaNumeroRegistroDiploma1(List<String> listaNumeroRegistroDiploma1) {
		this.listaNumeroRegistroDiploma1 = listaNumeroRegistroDiploma1;
	}



	public List<String> getListaNumeroRegistroDiploma2() {
		if(listaNumeroRegistroDiploma2 ==null ) {
			listaNumeroRegistroDiploma2 = new ArrayList<String>(0);
		}
		return listaNumeroRegistroDiploma2;
	}



	public void setListaNumeroRegistroDiploma2(List<String> listaNumeroRegistroDiploma2) {
		this.listaNumeroRegistroDiploma2 = listaNumeroRegistroDiploma2;
	}



	public String getValorCampoMascaraNumeroRegistroDiploma() {
		if(valorCampoMascaraNumeroRegistroDiploma == null ) {
			valorCampoMascaraNumeroRegistroDiploma ="";
		}
		return valorCampoMascaraNumeroRegistroDiploma;
	}



	public void setValorCampoMascaraNumeroRegistroDiploma(String valorCampoMascaraNumeroRegistroDiploma) {
		this.valorCampoMascaraNumeroRegistroDiploma = valorCampoMascaraNumeroRegistroDiploma;
	}
	
}
