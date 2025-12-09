package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.ConfiguracaoDiplomaDigitalVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.enumeradores.CampoPeriodoDisciplinaEnum;
import negocio.comuns.academico.enumeradores.CoordenadorCursoDisciplinaAproveitadaEnum;
import negocio.comuns.academico.enumeradores.FormatoCargaHorariaXmlEnum;
import negocio.comuns.academico.enumeradores.VersaoDiplomaDigitalEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import relatorio.controle.arquitetura.SuperControleRelatorio;

@Controller("ConfiguracaoDiplomaDigitalControle")
@Scope("viewScope")
@Lazy
public class ConfiguracaoDiplomaDigitalControle extends SuperControleRelatorio implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String TEXTO_PADRAO = "TextoPadrao";
	private static final String CONFIGURACAO_CONS = "configuracaoDiplomaDigitalCons";
	private static final String CONFIGURACAO_FORM = "configuracaoDiplomaDigitalForm";
	private static final String FUNCIONARIO_PRIMARIO = "funcionarioPrimario";
	private static final String FUNCIONARIO_SECUNDARIO = "funcionarioSecundario";
	private static final String FUNCIONARIO_TERCEIRO = "funcionarioTerceiro";
	private static final String FUNCIONARIO_QUARTO = "funcionarioQuarto";
	private static final String FUNCIONARIO_QUINTO = "funcionarioQuinto";

	private String campoConsulta;
	private String valorConsulta;
	private String funcionarioConsultar;
	private String campoConsultaFuncionario;
	private String valorConsultaFuncionario;
	private Boolean abrirPanelResponsaveis;
	private Boolean abrirPanelLayout;
	private Boolean abrirPanelRegraDisciplina;
	private Boolean abrirPanelHistoricoConsiderar;
	private Boolean abrirPanelRegraDocumentacaoAcademica;
	private ConfiguracaoDiplomaDigitalVO configuracaoDiplomaDigital;
	private List<FuncionarioVO> listaConsultaFuncionario;
	private List<ConfiguracaoDiplomaDigitalVO> listaConsultaConfiguracao;
	private List<SelectItem> tipoConsultaCombo;
	private List<SelectItem> listaSelectUnidadeEnsino;
	private List<SelectItem> listaSelectLayoutGraduacao;
	private List<SelectItem> listaSelectTextoPadraoGraduacao;
	private List<SelectItem> listaSelectLayoutGraduacaoTecnologica;
	private List<SelectItem> listaSelectTextoPadraoGraduacaoTecnologica;
	private List<SelectItem> listaSelectCampoPeriodoDisciplina;
	private List<SelectItem> tipoConsultaComboFuncionario;
	private List<SelectItem> listaCargoFuncionarioPrincipal;
	private List<SelectItem> listaCargoFuncionarioSecundario;
	private List<SelectItem> listaCargoFuncionarioTerceiro;
	private List<SelectItem> listaCargoFuncionarioQuarto;
	private List<SelectItem> listaCargoFuncionarioQuinto;
	private List<SelectItem> listaSelectCoordenadorCursoDisciplinasAproveitadas;
	private List<SelectItem> listaSelectVersao;
	private List<SelectItem> listaSelectFormatoCargaHorariaXML;
	private List<SelectItem> listaTipoLayoutHistoricoGraduacao;
	private List<SelectItem> listaTipoLayoutHistoricoGraduacaoTecnologica;

	public ConfiguracaoDiplomaDigitalControle() throws Exception {
		setCampoConsulta("descricao");
	}

	public String getCampoConsulta() {
		if (campoConsulta == null) {
			campoConsulta = Constantes.EMPTY;
		}
		return campoConsulta;
	}

	public void setCampoConsulta(String campoConsulta) {
		this.campoConsulta = campoConsulta;
	}

	public String getValorConsulta() {
		if (valorConsulta == null) {
			valorConsulta = Constantes.EMPTY;
		}
		return valorConsulta;
	}

	public void setValorConsulta(String valorConsulta) {
		this.valorConsulta = valorConsulta;
	}

	public Boolean getAbrirPanelResponsaveis() {
		if (abrirPanelResponsaveis == null) {
			abrirPanelResponsaveis = Boolean.TRUE;
		}
		return abrirPanelResponsaveis;
	}

	public void setAbrirPanelResponsaveis(Boolean abrirPanelResponsaveis) {
		this.abrirPanelResponsaveis = abrirPanelResponsaveis;
	}

	public Boolean getAbrirPanelLayout() {
		if (abrirPanelLayout == null) {
			abrirPanelLayout = Boolean.TRUE;
		}
		return abrirPanelLayout;
	}

	public void setAbrirPanelLayout(Boolean abrirPanelLayout) {
		this.abrirPanelLayout = abrirPanelLayout;
	}

	public Boolean getAbrirPanelRegraDisciplina() {
		if (abrirPanelRegraDisciplina == null) {
			abrirPanelRegraDisciplina = Boolean.TRUE;
		}
		return abrirPanelRegraDisciplina;
	}

	public void setAbrirPanelRegraDisciplina(Boolean abrirPanelRegraDisciplina) {
		this.abrirPanelRegraDisciplina = abrirPanelRegraDisciplina;
	}

	public String getFuncionarioConsultar() {
		if (funcionarioConsultar == null) {
			funcionarioConsultar = Constantes.EMPTY;
		}
		return funcionarioConsultar;
	}

	public void setFuncionarioConsultar(String funcionarioConsultar) {
		this.funcionarioConsultar = funcionarioConsultar;
	}

	public String getCampoConsultaFuncionario() {
		if (campoConsultaFuncionario == null) {
			campoConsultaFuncionario = Constantes.EMPTY;
		}
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		if (tipoConsultaComboFuncionario == null) {
			tipoConsultaComboFuncionario = new ArrayList<SelectItem>(0);
			tipoConsultaComboFuncionario.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboFuncionario.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaComboFuncionario.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboFuncionario.add(new SelectItem("cargo", "Cargo"));
			tipoConsultaComboFuncionario.add(new SelectItem("departamento", "Departamento"));
		}
		return tipoConsultaComboFuncionario;
	}

	public void setTipoConsultaComboFuncionario(List<SelectItem> tipoConsultaComboFuncionario) {
		this.tipoConsultaComboFuncionario = tipoConsultaComboFuncionario;
	}

	public String getValorConsultaFuncionario() {
		if (valorConsultaFuncionario == null) {
			valorConsultaFuncionario = Constantes.EMPTY;
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList<FuncionarioVO>(0);
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public List<SelectItem> getTipoConsultaCombo() {
		if (tipoConsultaCombo == null) {
			tipoConsultaCombo = new ArrayList<SelectItem>(0);
			tipoConsultaCombo.add(new SelectItem("descricao", "Descrição"));
			tipoConsultaCombo.add(new SelectItem("unidadeEnsino", "Unidade Ensino"));
			tipoConsultaCombo.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaCombo;
	}

	public void setTipoConsultaCombo(List<SelectItem> tipoConsultaCombo) {
		this.tipoConsultaCombo = tipoConsultaCombo;
	}

	public List<ConfiguracaoDiplomaDigitalVO> getListaConsultaConfiguracao() {
		if (listaConsultaConfiguracao == null) {
			listaConsultaConfiguracao = new ArrayList<ConfiguracaoDiplomaDigitalVO>(0);
		}
		return listaConsultaConfiguracao;
	}

	public void setListaConsultaConfiguracao(List<ConfiguracaoDiplomaDigitalVO> listaConsultaConfiguracao) {
		this.listaConsultaConfiguracao = listaConsultaConfiguracao;
	}

	public List<SelectItem> getListaSelectUnidadeEnsino() {
		if (listaSelectUnidadeEnsino == null) {
			listaSelectUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectUnidadeEnsino;
	}

	public void setListaSelectUnidadeEnsino(List<SelectItem> listaSelectUnidadeEnsino) {
		this.listaSelectUnidadeEnsino = listaSelectUnidadeEnsino;
	}

	public List<SelectItem> getListaSelectLayoutGraduacao() {
		if (listaSelectLayoutGraduacao == null) {
			listaSelectLayoutGraduacao = new ArrayList<SelectItem>(0);
		}
		return listaSelectLayoutGraduacao;
	}

	public void setListaSelectLayoutGraduacao(List<SelectItem> listaSelectLayoutGraduacao) {
		this.listaSelectLayoutGraduacao = listaSelectLayoutGraduacao;
	}

	public List<SelectItem> getListaSelectTextoPadraoGraduacao() {
		if (listaSelectTextoPadraoGraduacao == null) {
			listaSelectTextoPadraoGraduacao = new ArrayList<SelectItem>(0);
		}
		return listaSelectTextoPadraoGraduacao;
	}

	public void setListaSelectTextoPadraoGraduacao(List<SelectItem> listaSelectTextoPadraoGraduacao) {
		this.listaSelectTextoPadraoGraduacao = listaSelectTextoPadraoGraduacao;
	}

	public List<SelectItem> getListaSelectLayoutGraduacaoTecnologica() {
		if (listaSelectLayoutGraduacaoTecnologica == null) {
			listaSelectLayoutGraduacaoTecnologica = new ArrayList<SelectItem>(0);
		}
		return listaSelectLayoutGraduacaoTecnologica;
	}

	public void setListaSelectLayoutGraduacaoTecnologica(List<SelectItem> listaSelectLayoutGraduacaoTecnologica) {
		this.listaSelectLayoutGraduacaoTecnologica = listaSelectLayoutGraduacaoTecnologica;
	}

	public List<SelectItem> getListaSelectTextoPadraoGraduacaoTecnologica() {
		if (listaSelectTextoPadraoGraduacaoTecnologica == null) {
			listaSelectTextoPadraoGraduacaoTecnologica = new ArrayList<SelectItem>(0);
		}
		return listaSelectTextoPadraoGraduacaoTecnologica;
	}

	public void setListaSelectTextoPadraoGraduacaoTecnologica(List<SelectItem> listaSelectTextoPadraoGraduacaoTecnologica) {
		this.listaSelectTextoPadraoGraduacaoTecnologica = listaSelectTextoPadraoGraduacaoTecnologica;
	}

	public List<SelectItem> getListaSelectCampoPeriodoDisciplina() {
		if (listaSelectCampoPeriodoDisciplina == null) {
			listaSelectCampoPeriodoDisciplina = new ArrayList<SelectItem>(0);
			listaSelectCampoPeriodoDisciplina.add(new SelectItem(CampoPeriodoDisciplinaEnum.NENHUM, CampoPeriodoDisciplinaEnum.NENHUM.getDescricao()));
			listaSelectCampoPeriodoDisciplina.add(new SelectItem(CampoPeriodoDisciplinaEnum.NUMERO_PERIODO_LETIVO, CampoPeriodoDisciplinaEnum.NUMERO_PERIODO_LETIVO.getDescricao()));
			listaSelectCampoPeriodoDisciplina.add(new SelectItem(CampoPeriodoDisciplinaEnum.ANO_SEMESTRE_CURSADO, CampoPeriodoDisciplinaEnum.ANO_SEMESTRE_CURSADO.getDescricao()));
		}
		return listaSelectCampoPeriodoDisciplina;
	}

	public void setListaSelectCampoPeriodoDisciplina(List<SelectItem> listaSelectCampoPeriodoDisciplina) {
		this.listaSelectCampoPeriodoDisciplina = listaSelectCampoPeriodoDisciplina;
	}

	public ConfiguracaoDiplomaDigitalVO getConfiguracaoDiplomaDigital() {
		if (configuracaoDiplomaDigital == null) {
			configuracaoDiplomaDigital = new ConfiguracaoDiplomaDigitalVO();
		}
		return configuracaoDiplomaDigital;
	}

	public void setConfiguracaoDiplomaDigital(ConfiguracaoDiplomaDigitalVO configuracaoDiplomaDigital) {
		this.configuracaoDiplomaDigital = configuracaoDiplomaDigital;
	}

	public List<SelectItem> getListaCargoFuncionarioPrincipal() {
		if (listaCargoFuncionarioPrincipal == null) {
			listaCargoFuncionarioPrincipal = new ArrayList<SelectItem>(0);
		}
		return listaCargoFuncionarioPrincipal;
	}

	public void setListaCargoFuncionarioPrincipal(List<SelectItem> listaCargoFuncionarioPrincipal) {
		this.listaCargoFuncionarioPrincipal = listaCargoFuncionarioPrincipal;
	}

	public List<SelectItem> getListaCargoFuncionarioSecundario() {
		if (listaCargoFuncionarioSecundario == null) {
			listaCargoFuncionarioSecundario = new ArrayList<SelectItem>(0);
		}
		return listaCargoFuncionarioSecundario;
	}

	public void setListaCargoFuncionarioSecundario(List<SelectItem> listaCargoFuncionarioSecundario) {
		this.listaCargoFuncionarioSecundario = listaCargoFuncionarioSecundario;
	}

	public List<SelectItem> getListaCargoFuncionarioTerceiro() {
		if (listaCargoFuncionarioTerceiro == null) {
			listaCargoFuncionarioTerceiro = new ArrayList<SelectItem>(0);
		}
		return listaCargoFuncionarioTerceiro;
	}

	public void setListaCargoFuncionarioTerceiro(List<SelectItem> listaCargoFuncionarioTerceiro) {
		this.listaCargoFuncionarioTerceiro = listaCargoFuncionarioTerceiro;
	}

	public List<SelectItem> getListaCargoFuncionarioQuarto() {
		if (listaCargoFuncionarioQuarto == null) {
			listaCargoFuncionarioQuarto = new ArrayList<SelectItem>(0);
		}
		return listaCargoFuncionarioQuarto;
	}

	public void setListaCargoFuncionarioQuarto(List<SelectItem> listaCargoFuncionarioQuarto) {
		this.listaCargoFuncionarioQuarto = listaCargoFuncionarioQuarto;
	}

	public List<SelectItem> getListaCargoFuncionarioQuinto() {
		if (listaCargoFuncionarioQuinto == null) {
			listaCargoFuncionarioQuinto = new ArrayList<SelectItem>(0);
		}
		return listaCargoFuncionarioQuinto;
	}

	public void setListaCargoFuncionarioQuinto(List<SelectItem> listaCargoFuncionarioQuinto) {
		this.listaCargoFuncionarioQuinto = listaCargoFuncionarioQuinto;
	}

	public List<SelectItem> getListaSelectCoordenadorCursoDisciplinasAproveitadas() {
		if (listaSelectCoordenadorCursoDisciplinasAproveitadas == null) {
			listaSelectCoordenadorCursoDisciplinasAproveitadas = new ArrayList<SelectItem>(0);
			listaSelectCoordenadorCursoDisciplinasAproveitadas.add(new SelectItem(CoordenadorCursoDisciplinaAproveitadaEnum.NENHUM, CoordenadorCursoDisciplinaAproveitadaEnum.NENHUM.getDescricao()));
			listaSelectCoordenadorCursoDisciplinasAproveitadas.add(new SelectItem(CoordenadorCursoDisciplinaAproveitadaEnum.APENAS_APROVEITAMENTO_SEM_PROFESSOR, CoordenadorCursoDisciplinaAproveitadaEnum.APENAS_APROVEITAMENTO_SEM_PROFESSOR.getDescricao()));
			listaSelectCoordenadorCursoDisciplinasAproveitadas.add(new SelectItem(CoordenadorCursoDisciplinaAproveitadaEnum.TODOS_APROVEITAMENTO, CoordenadorCursoDisciplinaAproveitadaEnum.TODOS_APROVEITAMENTO.getDescricao()));
		}
		return listaSelectCoordenadorCursoDisciplinasAproveitadas;
	}

	public void setListaSelectCoordenadorCursoDisciplinasAproveitadas(List<SelectItem> listaSelectCoordenadorCursoDisciplinasAproveitadas) {
		this.listaSelectCoordenadorCursoDisciplinasAproveitadas = listaSelectCoordenadorCursoDisciplinasAproveitadas;
	}
	
	public List<SelectItem> getListaSelectVersao() {
		if (listaSelectVersao == null) {
			listaSelectVersao = new ArrayList<SelectItem>(0);
			for (VersaoDiplomaDigitalEnum obj : VersaoDiplomaDigitalEnum.values()) {
				listaSelectVersao.add(new SelectItem(obj, obj.getDescricao()));
			}
		}
		return listaSelectVersao;
	}
	
	public void setListaSelectVersao(List<SelectItem> listaSelectVersao) {
		this.listaSelectVersao = listaSelectVersao;
	}
	
	public List<SelectItem> getListaSelectFormatoCargaHorariaXML() {
		if (listaSelectFormatoCargaHorariaXML == null) {
			listaSelectFormatoCargaHorariaXML = new ArrayList<SelectItem>(0);
			listaSelectFormatoCargaHorariaXML.add(new SelectItem(FormatoCargaHorariaXmlEnum.HORA_AULA, FormatoCargaHorariaXmlEnum.HORA_AULA.getDescricao()));
			listaSelectFormatoCargaHorariaXML.add(new SelectItem(FormatoCargaHorariaXmlEnum.HORA_RELOGIO, FormatoCargaHorariaXmlEnum.HORA_RELOGIO.getDescricao()));
		}
		return listaSelectFormatoCargaHorariaXML;
	}
	
	public void setListaSelectFormatoCargaHorariaXML(List<SelectItem> listaSelectFormatoCargaHorariaXML) {
		this.listaSelectFormatoCargaHorariaXML = listaSelectFormatoCargaHorariaXML;
	}
	
	public List<SelectItem> getListaTipoLayoutHistoricoGraduacao() {
		if (listaTipoLayoutHistoricoGraduacao == null) {
			try {
				listaTipoLayoutHistoricoGraduacao = new ArrayList<>(0);
				listaTipoLayoutHistoricoGraduacao.add(new SelectItem(null, Constantes.EMPTY));
				listaTipoLayoutHistoricoGraduacao.addAll(getListaTipoLayoutHistorico(getConfiguracaoDiplomaDigital().getTipoLayoutHistoricoGraduacao(), TipoNivelEducacional.SUPERIOR.getValor()));
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		return listaTipoLayoutHistoricoGraduacao;
	}
	
	public List<SelectItem> getListaTipoLayoutHistoricoGraduacaoTecnologica() {
		if (listaTipoLayoutHistoricoGraduacaoTecnologica == null) {
			try {
				listaTipoLayoutHistoricoGraduacaoTecnologica = new ArrayList<>(0);
				listaTipoLayoutHistoricoGraduacaoTecnologica.add(new SelectItem(null, Constantes.EMPTY));
				listaTipoLayoutHistoricoGraduacaoTecnologica.addAll(getListaTipoLayoutHistorico(getConfiguracaoDiplomaDigital().getTipoLayoutHistoricoGraduacaoTecnologica(), TipoNivelEducacional.GRADUACAO_TECNOLOGICA.getValor()));
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		return listaTipoLayoutHistoricoGraduacaoTecnologica;
	}
	
	public void setListaTipoLayoutHistoricoGraduacaoTecnologica(List<SelectItem> listaTipoLayoutHistoricoGraduacaoTecnologica) {
		this.listaTipoLayoutHistoricoGraduacaoTecnologica = listaTipoLayoutHistoricoGraduacaoTecnologica;
	}
	
	public void consultarConfiguracao() throws Exception {
		try {
			setListaConsultaConfiguracao(new ArrayList<ConfiguracaoDiplomaDigitalVO>(0));
			setListaConsultaConfiguracao(getFacadeFactory().getConfiguracaoDiplomaDigitalInterfaceFacade().consultar(getValorConsulta(), getCampoConsulta(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado(), true));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaConfiguracao(new ArrayList<ConfiguracaoDiplomaDigitalVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String editar() {
		try {
			ConfiguracaoDiplomaDigitalVO obj = (ConfiguracaoDiplomaDigitalVO) context().getExternalContext().getRequestMap().get("configuracaoItens");
			getFacadeFactory().getConfiguracaoDiplomaDigitalInterfaceFacade().carregarDados(obj, getUsuarioLogado());
			setConfiguracaoDiplomaDigital(obj);
			montarListaSelectTextoPadraoGraduacao();
			montarListaSelectTextoPadraoGraduacaoTecnologica();
			limparDadosFuncionario();
			montarListaSelectUnidadeEnsino();
			montarListaSelectLayoutGraduacao();
			montarListaSelectLayoutGraduacaoTecnologica();
			montarDadosCargoFuncionarios();
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao(CONFIGURACAO_FORM);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public String novo() {
		try {
			setConfiguracaoDiplomaDigital(new ConfiguracaoDiplomaDigitalVO());
			setAbrirPanelResponsaveis(Boolean.TRUE);
			setAbrirPanelLayout(Boolean.TRUE);
			setAbrirPanelRegraDisciplina(Boolean.TRUE);
			setAbrirPanelHistoricoConsiderar(Boolean.TRUE);
			setAbrirPanelRegraDocumentacaoAcademica(Boolean.TRUE);
			setListaCargoFuncionarioPrincipal(new ArrayList<SelectItem>(0));
			setListaCargoFuncionarioSecundario(new ArrayList<SelectItem>(0));
			setListaCargoFuncionarioTerceiro(new ArrayList<SelectItem>(0));
			setListaCargoFuncionarioQuarto(new ArrayList<SelectItem>(0));
			setListaCargoFuncionarioQuinto(new ArrayList<SelectItem>(0));
			setListaSelectTextoPadraoGraduacao(new ArrayList<SelectItem>(0));
			setListaSelectTextoPadraoGraduacaoTecnologica(new ArrayList<SelectItem>(0));
			limparDadosFuncionario();
			montarListaSelectUnidadeEnsino();
			montarListaSelectLayoutGraduacao();
			montarListaSelectLayoutGraduacaoTecnologica();
			return Uteis.getCaminhoRedirecionamentoNavegacao(CONFIGURACAO_FORM);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public String inicializarConsultar() {
		setConfiguracaoDiplomaDigital(new ConfiguracaoDiplomaDigitalVO());
		setListaSelectUnidadeEnsino(new ArrayList<SelectItem>(0));
		setListaSelectLayoutGraduacao(new ArrayList<SelectItem>(0));
		setListaSelectLayoutGraduacaoTecnologica(new ArrayList<SelectItem>(0));
		setListaConsultaConfiguracao(new ArrayList<ConfiguracaoDiplomaDigitalVO>(0));
		setValorConsulta(Constantes.EMPTY);
		return Uteis.getCaminhoRedirecionamentoNavegacao(CONFIGURACAO_CONS);
	}

	public void gravar() {
		try {
			getFacadeFactory().getConfiguracaoDiplomaDigitalInterfaceFacade().persistir(getConfiguracaoDiplomaDigital(), getUsuarioLogado(), true);
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void excluir() {
		try {
			getFacadeFactory().getConfiguracaoDiplomaDigitalInterfaceFacade().excluir(getConfiguracaoDiplomaDigital(), getUsuarioLogado(), true);
			novo();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectLayoutGraduacao() throws Exception {
		setListaSelectLayoutGraduacao(new ArrayList<SelectItem>(0));
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("DiplomaAlunoSuperiorRel", "Layout 1 - Graduação"));
		itens.add(new SelectItem("DiplomaAlunoSuperior2Rel", "Layout 2 - Graduação"));
		itens.add(new SelectItem("DiplomaAlunoSuperior3Rel", "Layout 3 - Graduação"));
		itens.add(new SelectItem("DiplomaAlunoSuperior4Rel", "Layout 4 - Graduação"));
		itens.add(new SelectItem("DiplomaAlunoSuperior5Rel", "Layout 5 - Graduação"));
		itens.add(new SelectItem("DiplomaAlunoSuperior6Rel", "Layout 6 - Graduação"));
		itens.add(new SelectItem("TextoPadrao", "Texto Padrão"));
		setListaSelectLayoutGraduacao(itens);
	}

	public void montarListaSelectLayoutGraduacaoTecnologica() throws Exception {
		setListaSelectLayoutGraduacaoTecnologica(new ArrayList<SelectItem>(0));
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("DiplomaAlunoGradTecnologicaRel", "Layout 1 - Graduação Tecnológica"));
		itens.add(new SelectItem("DiplomaAlunoSuperior5Rel", "Layout 2 - Graduação Tecnológica"));
		itens.add(new SelectItem("DiplomaAlunoSuperior6Rel", "Layout 3 - Graduação Tecnológica"));
		itens.add(new SelectItem("TextoPadrao", "Texto Padrão"));
		setListaSelectLayoutGraduacaoTecnologica(itens);
	}

	public void montarListaSelectTextoPadraoGraduacao() throws Exception {
		setListaSelectTextoPadraoGraduacao(new ArrayList<SelectItem>(0));
		if (getConfiguracaoDiplomaDigital().getLayoutGraduacaoPadrao().equals(TEXTO_PADRAO)) {
			List<TextoPadraoDeclaracaoVO> lista = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("DI", Uteis.isAtributoPreenchido(getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao()) ? getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao().getCodigo() : 0, TipoNivelEducacional.SUPERIOR.getValor(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			for (TextoPadraoDeclaracaoVO objeto : lista) {
				getListaSelectTextoPadraoGraduacao().add(new SelectItem(objeto.getCodigo(), objeto.getDescricao()));
			}
		}
	}

	public void montarListaSelectTextoPadraoGraduacaoTecnologica() throws Exception {
		setListaSelectTextoPadraoGraduacaoTecnologica(new ArrayList<SelectItem>(0));
		if (getConfiguracaoDiplomaDigital().getLayoutGraduacaoTecnologicaPadrao().equals(TEXTO_PADRAO)) {
			List<TextoPadraoDeclaracaoVO> lista = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("DI", Uteis.isAtributoPreenchido(getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao()) ? getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao().getCodigo() : 0, TipoNivelEducacional.GRADUACAO_TECNOLOGICA.getValor(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			for (TextoPadraoDeclaracaoVO objeto : lista) {
				getListaSelectTextoPadraoGraduacaoTecnologica().add(new SelectItem(objeto.getCodigo(), objeto.getDescricao()));
			}
		}
	}

	public void montarListaSelectUnidadeEnsino() throws Exception {
		setListaSelectUnidadeEnsino(new ArrayList<SelectItem>(0));
		List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(0, false, true, getUsuarioLogado());
		if (unidadeEnsinoVOs != null && !unidadeEnsinoVOs.isEmpty()) {
			getListaSelectUnidadeEnsino().add(new SelectItem(0, ""));
			for (UnidadeEnsinoVO obj : unidadeEnsinoVOs) {
				getListaSelectUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
		}
	}

	public void limparDadosFuncionario() {
		setValorConsultaFuncionario(Constantes.EMPTY);
		getListaConsultaFuncionario().clear();
	}

	public void consultarFuncionario() {
		List<FuncionarioVO> objs = new ArrayList<>(0);
		try {
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(), getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaFuncionario(), getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCidade(getValorConsultaFuncionario(), getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaFuncionario(), getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCargo(getValorConsultaFuncionario(), getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeDepartamento(getValorConsultaFuncionario(), "FU", getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeUnidadeEnsino(getValorConsultaFuncionario(), "FU", getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			objs = null;
		}
	}

	public void selecionarFuncionario() throws Exception {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
			FuncionarioVO objCompleto = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getCodigo(), obj.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			if (getFuncionarioConsultar().equals(FUNCIONARIO_PRIMARIO)) {
				getConfiguracaoDiplomaDigital().setFuncionarioPrimario(objCompleto);
				montarComboCargoFuncionario(getConfiguracaoDiplomaDigital().getFuncionarioPrimario().getFuncionarioCargoVOs());
			} else if (getFuncionarioConsultar().equals(FUNCIONARIO_SECUNDARIO)) {
				getConfiguracaoDiplomaDigital().setFuncionarioSecundario(objCompleto);
				montarComboCargoFuncionario(getConfiguracaoDiplomaDigital().getFuncionarioSecundario().getFuncionarioCargoVOs());
			} else if (getFuncionarioConsultar().equals(FUNCIONARIO_TERCEIRO)) {
				getConfiguracaoDiplomaDigital().setFuncionarioTerceiro(objCompleto);
				montarComboCargoFuncionario(getConfiguracaoDiplomaDigital().getFuncionarioTerceiro().getFuncionarioCargoVOs());
			} else if (getFuncionarioConsultar().equals(FUNCIONARIO_QUARTO)) {
				getConfiguracaoDiplomaDigital().setFuncionarioQuarto(objCompleto);
				montarComboCargoFuncionario(getConfiguracaoDiplomaDigital().getFuncionarioQuarto().getFuncionarioCargoVOs());
			} else {
				getConfiguracaoDiplomaDigital().setFuncionarioQuinto(objCompleto);
				montarComboCargoFuncionario(getConfiguracaoDiplomaDigital().getFuncionarioQuinto().getFuncionarioCargoVOs());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarComboCargoFuncionario(List<FuncionarioCargoVO> cargos) throws Exception {
		List<SelectItem> selectItems = new ArrayList<SelectItem>(0);
		for (FuncionarioCargoVO obj : cargos) {
			if (!selectItems.stream().anyMatch(s -> s.getValue().equals(obj.getCargo().getCodigo()))) {
				selectItems.add(new SelectItem(obj.getCargo().getCodigo(), obj.getCargo().getNome()));
			}
		}
		if (getFuncionarioConsultar().equals(FUNCIONARIO_PRIMARIO)) {
			setListaCargoFuncionarioPrincipal(selectItems);
		} else if (getFuncionarioConsultar().equals(FUNCIONARIO_SECUNDARIO)) {
			setListaCargoFuncionarioSecundario(selectItems);
		} else if (getFuncionarioConsultar().equals(FUNCIONARIO_TERCEIRO)) {
			setListaCargoFuncionarioTerceiro(selectItems);
		} else if (getFuncionarioConsultar().equals(FUNCIONARIO_QUARTO)) {
			setListaCargoFuncionarioQuarto(selectItems);
		} else {
			setListaCargoFuncionarioQuinto(selectItems);
		}
	}

	public void limparDadosFuncionario(String funcionario) {
		if (funcionario.equals(FUNCIONARIO_PRIMARIO)) {
			getConfiguracaoDiplomaDigital().setFuncionarioPrimario(new FuncionarioVO());
			getConfiguracaoDiplomaDigital().setCargoFuncionarioPrimario(new CargoVO());
			getConfiguracaoDiplomaDigital().setTituloFuncionarioPrimario(Constantes.EMPTY);
			setListaCargoFuncionarioPrincipal(new ArrayList<SelectItem>(0));
		} else if (funcionario.equals(FUNCIONARIO_SECUNDARIO)) {
			getConfiguracaoDiplomaDigital().setFuncionarioSecundario(new FuncionarioVO());
			getConfiguracaoDiplomaDigital().setCargoFuncionarioSecundario(new CargoVO());
			getConfiguracaoDiplomaDigital().setTituloFuncionarioSecundario(Constantes.EMPTY);
			setListaCargoFuncionarioSecundario(new ArrayList<SelectItem>(0));
		} else if (funcionario.equals(FUNCIONARIO_TERCEIRO)) {
			getConfiguracaoDiplomaDigital().setFuncionarioTerceiro(new FuncionarioVO());
			getConfiguracaoDiplomaDigital().setCargoFuncionarioTerceiro(new CargoVO());
			getConfiguracaoDiplomaDigital().setTituloFuncionarioTerceiro(Constantes.EMPTY);
			setListaCargoFuncionarioTerceiro(new ArrayList<SelectItem>(0));
		} else if (funcionario.equals(FUNCIONARIO_QUARTO)) {
			getConfiguracaoDiplomaDigital().setFuncionarioQuarto(new FuncionarioVO());
			getConfiguracaoDiplomaDigital().setCargoFuncionarioQuarto(new CargoVO());
			getConfiguracaoDiplomaDigital().setTituloFuncionarioQuarto(Constantes.EMPTY);
			setListaCargoFuncionarioQuarto(new ArrayList<SelectItem>(0));
		} else {
			getConfiguracaoDiplomaDigital().setFuncionarioQuinto(new FuncionarioVO());
			getConfiguracaoDiplomaDigital().setCargoFuncionarioQuinto(new CargoVO());
			getConfiguracaoDiplomaDigital().setTituloFuncionarioQuinto(Constantes.EMPTY);
			setListaCargoFuncionarioQuinto(new ArrayList<SelectItem>(0));
		}
	}

	public void montarDadosCargoFuncionarios() throws Exception {
		setListaCargoFuncionarioPrincipal(new ArrayList<SelectItem>(0));
		setListaCargoFuncionarioSecundario(new ArrayList<SelectItem>(0));
		setListaCargoFuncionarioTerceiro(new ArrayList<SelectItem>(0));
		setListaCargoFuncionarioQuarto(new ArrayList<SelectItem>(0));
		setListaCargoFuncionarioQuinto(new ArrayList<SelectItem>(0));
		if (Uteis.isAtributoPreenchido(getConfiguracaoDiplomaDigital().getFuncionarioPrimario())) {
			FuncionarioVO objCompleto = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(getConfiguracaoDiplomaDigital().getFuncionarioPrimario().getCodigo(), getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getConfiguracaoDiplomaDigital().setFuncionarioPrimario(objCompleto);
			setFuncionarioConsultar(FUNCIONARIO_PRIMARIO);
			montarComboCargoFuncionario(getConfiguracaoDiplomaDigital().getFuncionarioPrimario().getFuncionarioCargoVOs());
		}
		if (Uteis.isAtributoPreenchido(getConfiguracaoDiplomaDigital().getFuncionarioSecundario())) {
			FuncionarioVO objCompleto = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(getConfiguracaoDiplomaDigital().getFuncionarioSecundario().getCodigo(), getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getConfiguracaoDiplomaDigital().setFuncionarioSecundario(objCompleto);
			setFuncionarioConsultar(FUNCIONARIO_SECUNDARIO);
			montarComboCargoFuncionario(getConfiguracaoDiplomaDigital().getFuncionarioSecundario().getFuncionarioCargoVOs());
		}
		if (Uteis.isAtributoPreenchido(getConfiguracaoDiplomaDigital().getFuncionarioTerceiro())) {
			FuncionarioVO objCompleto = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(getConfiguracaoDiplomaDigital().getFuncionarioTerceiro().getCodigo(), getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getConfiguracaoDiplomaDigital().setFuncionarioTerceiro(objCompleto);
			setFuncionarioConsultar(FUNCIONARIO_TERCEIRO);
			montarComboCargoFuncionario(getConfiguracaoDiplomaDigital().getFuncionarioTerceiro().getFuncionarioCargoVOs());
		}
		if (Uteis.isAtributoPreenchido(getConfiguracaoDiplomaDigital().getFuncionarioQuarto())) {
			FuncionarioVO objCompleto = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(getConfiguracaoDiplomaDigital().getFuncionarioQuarto().getCodigo(), getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getConfiguracaoDiplomaDigital().setFuncionarioQuarto(objCompleto);
			setFuncionarioConsultar(FUNCIONARIO_QUARTO);
			montarComboCargoFuncionario(getConfiguracaoDiplomaDigital().getFuncionarioQuarto().getFuncionarioCargoVOs());
		}
		if (Uteis.isAtributoPreenchido(getConfiguracaoDiplomaDigital().getFuncionarioQuinto())) {
			FuncionarioVO objCompleto = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(getConfiguracaoDiplomaDigital().getFuncionarioQuinto().getCodigo(), getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getConfiguracaoDiplomaDigital().setFuncionarioQuinto(objCompleto);
			setFuncionarioConsultar(FUNCIONARIO_QUINTO);
			montarComboCargoFuncionario(getConfiguracaoDiplomaDigital().getFuncionarioQuinto().getFuncionarioCargoVOs());
		}
	}

	public void consultarFuncionario(String funcionario) {
		try {
			if (funcionario.equals(FUNCIONARIO_PRIMARIO)) {
				FuncionarioVO objCompleto = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getConfiguracaoDiplomaDigital().getFuncionarioPrimario().getMatricula(), getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				getConfiguracaoDiplomaDigital().setFuncionarioPrimario(objCompleto);
				setFuncionarioConsultar(FUNCIONARIO_PRIMARIO);
				montarComboCargoFuncionario(getConfiguracaoDiplomaDigital().getFuncionarioPrimario().getFuncionarioCargoVOs());
			} else if (funcionario.equals(FUNCIONARIO_SECUNDARIO)) {
				FuncionarioVO objCompleto = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getConfiguracaoDiplomaDigital().getFuncionarioSecundario().getMatricula(), getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				getConfiguracaoDiplomaDigital().setFuncionarioSecundario(objCompleto);
				setFuncionarioConsultar(FUNCIONARIO_SECUNDARIO);
				montarComboCargoFuncionario(getConfiguracaoDiplomaDigital().getFuncionarioSecundario().getFuncionarioCargoVOs());
			} else if (funcionario.equals(FUNCIONARIO_TERCEIRO)) {
				FuncionarioVO objCompleto = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getConfiguracaoDiplomaDigital().getFuncionarioTerceiro().getMatricula(), getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				getConfiguracaoDiplomaDigital().setFuncionarioTerceiro(objCompleto);
				setFuncionarioConsultar(FUNCIONARIO_TERCEIRO);
				montarComboCargoFuncionario(getConfiguracaoDiplomaDigital().getFuncionarioTerceiro().getFuncionarioCargoVOs());
			} else if (funcionario.equals(FUNCIONARIO_QUARTO)) {
				FuncionarioVO objCompleto = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getConfiguracaoDiplomaDigital().getFuncionarioQuarto().getMatricula(), getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				getConfiguracaoDiplomaDigital().setFuncionarioQuarto(objCompleto);
				setFuncionarioConsultar(FUNCIONARIO_QUARTO);
				montarComboCargoFuncionario(getConfiguracaoDiplomaDigital().getFuncionarioQuarto().getFuncionarioCargoVOs());
			} else {
				FuncionarioVO objCompleto = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getConfiguracaoDiplomaDigital().getFuncionarioQuinto().getMatricula(), getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				getConfiguracaoDiplomaDigital().setFuncionarioQuinto(objCompleto);
				setFuncionarioConsultar(FUNCIONARIO_QUINTO);
				montarComboCargoFuncionario(getConfiguracaoDiplomaDigital().getFuncionarioQuinto().getFuncionarioCargoVOs());
			}
		} catch (Exception e) {
			if (funcionario.equals(FUNCIONARIO_PRIMARIO)) {
				limparDadosFuncionario(FUNCIONARIO_PRIMARIO);
			} else if (funcionario.equals(FUNCIONARIO_SECUNDARIO)) {
				limparDadosFuncionario(FUNCIONARIO_SECUNDARIO);
			} else if (funcionario.equals(FUNCIONARIO_TERCEIRO)) {
				limparDadosFuncionario(FUNCIONARIO_TERCEIRO);
			} else if (funcionario.equals(FUNCIONARIO_QUARTO)) {
				limparDadosFuncionario(FUNCIONARIO_QUARTO);
			} else {
				limparDadosFuncionario(FUNCIONARIO_QUINTO);
			}
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparTodosDadosFuncionario() {
		limparDadosFuncionario(FUNCIONARIO_PRIMARIO);
		limparDadosFuncionario(FUNCIONARIO_SECUNDARIO);
		limparDadosFuncionario(FUNCIONARIO_TERCEIRO);
		limparDadosFuncionario(FUNCIONARIO_QUARTO);
		limparDadosFuncionario(FUNCIONARIO_QUINTO);
	}
	public void validarHoraRelogio() {
		try {
			if (getConfiguracaoDiplomaDigital().getHoraRelogio() > 60) {
				throw new Exception("A hora relógio não pode ser maior de que 60 minutos.");
			} else if (getConfiguracaoDiplomaDigital().getHoraRelogio() < 0) {
				throw new Exception("Não pode ser informado um valor negativo.");
			}
		} catch (Exception e) {
			getConfiguracaoDiplomaDigital().setHoraRelogio(0);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public Boolean getAbrirPanelHistoricoConsiderar() {
		if (abrirPanelHistoricoConsiderar == null)  {
			abrirPanelHistoricoConsiderar = Boolean.TRUE;
		}
		return abrirPanelHistoricoConsiderar;
	}
	
	public void setAbrirPanelHistoricoConsiderar(Boolean abrirPanelHistoricoConsiderar) {
		this.abrirPanelHistoricoConsiderar = abrirPanelHistoricoConsiderar;
	}
	
	public Boolean getAbrirPanelRegraDocumentacaoAcademica() {
		if (abrirPanelRegraDocumentacaoAcademica == null) {
			abrirPanelRegraDocumentacaoAcademica = Boolean.TRUE;
		}
		return abrirPanelRegraDocumentacaoAcademica;
	}

	public void setAbrirPanelRegraDocumentacaoAcademica(Boolean abrirPanelRegraDocumentacaoAcademica) {
		this.abrirPanelRegraDocumentacaoAcademica = abrirPanelRegraDocumentacaoAcademica;
	}
}
