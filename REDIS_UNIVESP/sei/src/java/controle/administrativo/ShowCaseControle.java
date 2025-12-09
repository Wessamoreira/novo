package controle.administrativo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.utilitarias.DashboardVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoPessoa;

@Controller("showCaseControle")
@Scope("viewScope")
@Lazy
public class ShowCaseControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2554482493635783920L;
	private Date data;	
	private Boolean trueFalse;
	private String campoTexto;		
	private DashboardVO dashBoard;
	private Map<String, String> regraCampo;

	public Date getData() {
		if(data == null) {
			data =  new Date();
		}
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Boolean getTrueFalse() {
		return trueFalse;
	}

	public void setTrueFalse(Boolean trueFalse) {
		this.trueFalse = trueFalse;
	}

	public String getCampoTexto() {
		return campoTexto;
	}

	public void setCampoTexto(String campoTexto) {
		this.campoTexto = campoTexto;
	}
	
	@PostConstruct
	public void init() {
			setControleConsultaOtimizado(new DataModelo());
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setPage(0);
			getControleConsultaOtimizado().setPaginaAtual(1);
			consultarPessoa();
			getRegraCampo().put("COMBO_1", "Exemplo: \r\n<h:panelGroup layout=\"block\" styleClass=\"col-md-12 pn\">\r\n" + 
					"	<h:selectOneMenu styleClass=\"form-control camposObrigatorios\" value=\"\">\r\n" + 
					"		<f:selectItems itemLabel=\"Opção 1\" itemValue=\"Opo1\"></f:selectItems>\r\n" + 
					"		<f:selectItems itemLabel=\"Opção 2\" itemValue=\"Opo2\"></f:selectItems>\r\n" + 
					"		<f:selectItems itemLabel=\"Opção 3\" itemValue=\"Opo3\"></f:selectItems>\r\n" + 
					"	</h:selectOneMenu>\r\n" + 
					"	<span class=\"append-icon right mr15 mt5\">\r\n" + 
					"		<span class=\"#{msg_botoes.btn_icon_atualizar_embutido}\"></span>\r\n" + 
					"	</span>\r\n" + 
					"</h:panelGroup>");
			getRegraCampo().put("OUT_HELP_1", "Componente: otm:outputHelp\r\n" + 
					"Propriedades:\r\n    id - Obrigatório,\r\n    value - Obrigatório,\r\n    tooltip - Opcional\r\n" + 					 
					"Exemplo:\r\n"
					+ " <otm:outputHelp value=\\\"Output Com Tooltip Simples\\\" tooltip=\\\"Informar no Campo Abaixo o Nome do aluno\\\" id=\\\"outTooltip\\\"/>\r\n" 
					);
			getRegraCampo().put("OUT_HELP_2", "Componente: otm:outputHelp\r\n" + 
					"Propriedades:\r\n    id - Obrigatório,\r\n    value - Obrigatório, \r\n    tooltip - Opcional\r\n" + 					 
					"Exemplo: \r\n"
					+ "<otm:outputHelp value=\"Output Com Tooltip Avançado\" id=\"outTooltipAvancado\">\r\n" + 
					"		<COLOCAR AQUI O TRECO HTML DESEJADO>\r\n" + 
					"</otm:outputHelp>"
					);
			getRegraCampo().put("CALENDAR_1", "Componente: otm:calendar\r\n" + 
					"Propriedades:\r\n "
					+ "   id - Obrigatório, \r\n"
					+ "   value - Obrigatório, \r\n"
					+ "   pattern - default dd/MM/yyyy para campo de data e hora usar dd/MM/yyyy HH:mm, \r\n"
					+ "   enableManualInput (true), \r\n"
					+ "   inputClass (default form-control campos), \r\n"
					+ "   style, rendered (true), \r\n"
					+ "   disabled (false), \r\n"
					+ "   readonly (false), \r\n" 
					+ "   jsFunction (usar quando deseja realizar uma ação ao selecionar uma data, este deve estar vinculado a um a4j:jsFunction ou javascript ) \r\n" +
					"Exemplo:\r\n <a4j:jsFunction name=\"alerta\" oncomplete=\"alert('Ação de seleção de uma data!!!!')\"></a4j:jsFunction>\r\n" + 
					"<otm:calendar id=\"calendar\" value=\"#{showCaseControle.data}\" jsFunction=\"alerta()\" />\r\n" 					
					);
			
			getRegraCampo().put("CHECKBOX_1", "Componente: otm:selectBooleanCheckbox\r\n" + 
					"Propriedades:\r\n    id - Obrigatório,\r\n    value - Obrigatório, \r\n    disabled - default false\r\n    readonly -  default false\r\n" + 					 
					"Exemplo: \r\n"
					+ "<otm:selectBooleanCheckbox id=\"calendar\" value=\"#{showCaseControle.trueFalse}\"  >\r\n" + 
					"		<COLOCAR AQUI AÇÕES AJAX COM a4j:ajax>\r\n" + 
					"</otm:selectBooleanCheckbox>"
					);
			getRegraCampo().put("TABELA_1", "<rich:column filterType=\"custom\" filter=\"#{FilterFactory.filter('nome', 'nome', 'TEXTO')}\">\r\n" + 
					"	<f:facet name=\"header\">\r\n" + 
					"		Nome\r\n" + 
					"		<h:panelGroup layout=\"block\" style=\"width:100%\">\r\n" + 
					"			<h:inputText styleClass=\"form-control campos form-control-input-table\" size=\"15\" value=\"#{FilterFactory.filter('nome', 'nome', 'TEXTO').filtro}\">\r\n" + 
					"				<a4j:ajax event=\"change\" execute=\"@this\" render=\"listaAlunos\" />\r\n" + 
					"			</h:inputText>\r\n" + 
					"		</h:panelGroup>\r\n" + 
					"	</f:facet>\r\n" + 
					"	<a4j:commandLink value=\"#{pessoaItem.nome}\">\r\n" + 
					"	</a4j:commandLink>\r\n" + 
					"</rich:column>"
					);
			getRegraCampo().put("TABELA_2", "<otm:commandIcon icon=\"#{msg_botoes.btn_icon_editar} iconePequeno\"></otm:commandIcon>\r\n" + 
					"<otm:commandIcon icon=\"#{msg_botoes.btn_icon_selecionar} iconePequeno\"></otm:commandIcon>\r\n" + 
					"<otm:commandIcon icon=\"#{msg_botoes.btn_icon_remover} iconePequeno\"></otm:commandIcon>\r\n" + 
					"<otm:commandIcon icon=\"#{msg_botoes.btn_icon_adicionar} iconePequeno\"></otm:commandIcon>\r\n" + 
					"<otm:commandIcon icon=\"#{msg_botoes.btn_icon_subir} iconePequeno\"></otm:commandIcon>\r\n" + 
					"<otm:commandIcon icon=\"#{msg_botoes.btn_icon_descer} iconePequeno\"></otm:commandIcon>"
					);
			getRegraCampo().put("BOTAO_1", "Componente: otm:commandButton\r\n" + 
					"Propriedades:\r\n" + 
					"Todos do commandButton do richface\r\n" + 
					"tooltip - colocar aqui o text que deseja ser apresentado ao passar o mouse\r\n" + 
					"Exemplo:\r\n" + 
					"<otm:commandButton id=\"novo\" immediate=\"true\" value=\"#{msg_botoes.btn_novo}\" accesskey=\"1\" tooltip=\"Iniciar um novo registro\" styleClass=\"btn btn-info btn-novo\" render=\"form\" icon=\"far fa-file-alt\" />"
					);
			getRegraCampo().put("BOTAO_2", "Componente: otm:commandIcon\r\n" + 
					"Propriedades:\r\n" + 
					"Todos do commandLink do richface\r\n" + 
					"tooltip - colocar aqui o text que deseja ser apresentado ao passar o mouse\r\n" + 
					"Exemplo:\r\n" + 
					"<otm:commandIcon id=\"btnEditar\" tooltip=\"Clique para editar\" icon=\"#{msg_botoes.btn_icon_editar} iconePequeno\"></otm:commandIcon>"+
					"Orientações:\r\n" + 
					"     Veja no properties de botoes os icones padrões que começam com btn_icon"
					);
			getRegraCampo().put("BOTAO_3", "Componente: otm:ajuda\r\n" + 
					"Propriedades:\r\n" + 					
					"dir - colocar aqui a pasta base para listas os link de ajuda\r\n" + 
					"Exemplo:\r\n" + 
					"<ui:define name=\"ajuda\"><otm:ajuda dir=\"ACADEMICO/CURSOS/CURSO\"></otm:ajuda></ui:define>"
					);
			getRegraCampo().put("MODAL_1", "CSS MODAL: modalMaximizado50\r\nCSS BARRA BOTAO: barra-btn-flutuante-modal-50\r\n<rich:popupPanel styleClass=\"modalMaximizado50\" id=\"modalMaximizado50\">\r\n" +
					"	<f:facet name=\"header\">\r\n" + 
					"				<h:outputText value=\"Modal 50%\" />\r\n" + 
					"			</f:facet>\r\n" + 
					"			<f:facet name=\"controls\">\r\n" + 
					"				<h:panelGroup>\r\n" + 
					"					<h:graphicImage value=\"#{resource['imagens/close.png']}\" styleClass=\"icon-close-modal\" style=\"cursor:pointer\" id=\"hidelinkmodalMaximizado50\" />\r\n" + 
					"					<rich:componentControl target=\"modalMaximizado50\" operation=\"hide\" event=\"click\" />\r\n" + 
					"				</h:panelGroup>\r\n" + 
					"			</f:facet>"+
					"		<h:panelGroup layout=\"block\" styleClass=\"barra-btn-flutuante-modal-50\">\r\n" + 
					"			<otm:commandButton id=\"fechar\" immediate=\"true\" value=\"#{msg_botoes.btn_fechar}\" oncomplete=\"#{rich:component('modalMaximizado50')}.hide()\" tooltip=\"css=btn btn-primary dark btn-fechar icone=fas fa-times\" styleClass=\"btn btn-primary dark btn-fechar\" icon=\"fas fa-times\" />\r\n" + 
					"		</h:panelGroup>\r\n" + 
					"</rich:popupPanel>"
					);
			getRegraCampo().put("MODAL_2", "CSS MODAL: modalMaximizado\r\nCSS BARRA BOTAO: barra-btn-flutuante-modal-80\r\n<rich:popupPanel styleClass=\"modalMaximizado\" id=\"modalMaximizado80\">\r\n" +
					"	<f:facet name=\"header\">\r\n" + 
					"				<h:outputText value=\"Modal 80%\" />\r\n" + 
					"			</f:facet>\r\n" + 
					"			<f:facet name=\"controls\">\r\n" + 
					"				<h:panelGroup>\r\n" + 
					"					<h:graphicImage value=\"#{resource['imagens/close.png']}\" styleClass=\"icon-close-modal\" style=\"cursor:pointer\" id=\"hidelinkmodalMaximizado80\" />\r\n" + 
					"					<rich:componentControl target=\"modalMaximizado80\" operation=\"hide\" event=\"click\" />\r\n" + 
					"				</h:panelGroup>\r\n" + 
					"			</f:facet>"+
					"		<h:panelGroup layout=\"block\" styleClass=\"barra-btn-flutuante-modal-80\">\r\n" + 
					"			<otm:commandButton id=\"fechar\" immediate=\"true\" value=\"#{msg_botoes.btn_fechar}\" oncomplete=\"#{rich:component('modalMaximizado80')}.hide()\" tooltip=\"css=btn btn-primary dark btn-fechar icone=fas fa-times\" styleClass=\"btn btn-primary dark btn-fechar\" icon=\"fas fa-times\" />\r\n" + 
					"		</h:panelGroup>\r\n" + 
					"</rich:popupPanel>"
					);
			getRegraCampo().put("MODAL_3", "CSS MODAL: modalMaximizado\r\nCSS BARRA BOTAO: barra-btn-flutuante-modal-100\r\n<rich:popupPanel styleClass=\"modalMaximizado100\" id=\"modalMaximizado100\">\r\n" +
					"	<f:facet name=\"header\">\r\n" + 
					"				<h:outputText value=\"Modal 50%\" />\r\n" + 
					"			</f:facet>\r\n" + 
					"			<f:facet name=\"controls\">\r\n" + 
					"				<h:panelGroup>\r\n" + 
					"					<h:graphicImage value=\"#{resource['imagens/close.png']}\" styleClass=\"icon-close-modal\" style=\"cursor:pointer\" id=\"hidelinkmodalMaximizado100\" />\r\n" + 
					"					<rich:componentControl target=\"modalMaximizado100\" operation=\"hide\" event=\"click\" />\r\n" + 
					"				</h:panelGroup>\r\n" + 
					"		</f:facet>"+
					"		<h:panelGroup layout=\"block\" styleClass=\"barra-btn-flutuante-modal-100\">\r\n" + 
					"			<otm:commandButton id=\"fechar\" immediate=\"true\" value=\"#{msg_botoes.btn_fechar}\" oncomplete=\"#{rich:component('modalMaximizado100')}.hide()\" tooltip=\"css=btn btn-primary dark btn-fechar icone=fas fa-times\" styleClass=\"btn btn-primary dark btn-fechar\" icon=\"fas fa-times\" />\r\n" + 
					"		</h:panelGroup>\r\n" + 
					"</rich:popupPanel>"
					);
			getRegraCampo().put("EDITOR_1", "Componente:\r\n    otm:editor\r\n"+
					"Propriedades:\r\n"+
					"    value- Obrigatorio\r\n"+
					"    readonly- default false\r\n"+
					"    options- default full opçoes full e basic \r\n"+
					"    height - default 200 \r\n"+
					"Exemplo - default 200 \r\n"+
					"    <otm:editor value=\"#{showCaseControle.campoTexto}\"></otm:editor>"
					);
			getRegraCampo().put("EDITOR_2", "Componente:\r\n    otm:editor options=\"basic\"\r\n"+
					"Propriedades:\r\n"+
					"    value- Obrigatorio\r\n"+
					"    readonly- default false\r\n"+
					"    options- default full opçoes full e basic \r\n"+
					"    height - default 200 \r\n"+
					"Exemplo - default 200 \r\n"+
					"    <otm:editor options=\"basic\" value=\"#{showCaseControle.campoTexto}\"></otm:editor>"
					);
			getRegraCampo().put("EDITOR_3", "Componente:\r\n    otm:editor readonly=\"true\"\r\n"+
					"Propriedades:\r\n"+
					"    value- Obrigatorio\r\n"+
					"    readonly- default false\r\n"+
					"    options- default full opçoes full e basic \r\n"+
					"    height - default 200 \r\n"+
					"Exemplo - \r\n"+
					"    <otm:editor readonly=\"true\" value=\"#{showCaseControle.campoTexto}\"></otm:editor>"
					);
			getRegraCampo().put("MENSAGEM_1", "Componente:\r\n    otm:mensagem \r\n"+
					"Propriedades:\r\n"+
					"    bean- Obrigatorio aqui o nome do controlador \r\n"+					
					"Exemplo - \r\n"+
					"    <otm:mensagem bean=\"#{showCaseControle}\"></otm:mensagem>"
					);
			getRegraCampo().put("DASBOARD_1", "Componente:\r\n    otm2:dashpanel \r\n"+
					"Propriedades:\r\n"+
					"    id- Obrigatorio \r\n"+					
					"    cols- default 12 representa a quantidade de colunas que o mesmo irá ocupar na tela \r\n"+					
					"    renderedBody- default  \r\n"+					
					"Exemplo - \r\n"+
					"  <otm2:dashpanel id=\"dashboardEvolAcad\" styleClass=\"p-0\" cols=\"12\">\r\n" + 
					"		<ui:define name=\"header\">\r\n" + 
					"			<i class=\"fa fa-user mr-2\"></i>\r\n" + 
					"			Coloque aqui o título\r\n" + 
					"			<otm:commandIcon styleClass=\"float-right p-0\" render=\"panelGeral\" icon=\"fas fa-angle-down text-center mr-2 text-gray-700\"/>\r\n" + 
					"			<otm:commandIcon styleClass=\"float-right p-0\" icon=\"fas fa-angle-up text-center mr-2 text-gray-700 \"/>\r\n" + 
					"			<otm:commandIcon styleClass=\"float-right p-0\" icon=\"fas fa-compress-arrows-alt text-center mr-2 text-gray-700 \"/>"+
					"		</ui:define>\r\n" + 
					"		<ui:define name=\"body\">\r\n" + 
					"			Coloque aqui o conteudo do dashboard\r\n" + 
					"		</ui:define>\r\n" + 					
					"	</otm2:dashpanel>"
					);
			
			getRegraCampo().put("PANEL_1", "Componente:\r\n    otm2:panel \r\n"+
					"Propriedades:\r\n"+
					"    name- Obrigatorio \r\n"+					
					"    opened - default true usado para trazer expandido o panel \r\n"+					
					"    rendered- default true  \r\n"+					
					"    renderedControl- default true, se false oculta os botoes de minimizar e expandir \r\n"+					
					"    stylePanel- default padding:10px !important -  neste usa css puro \r\n"+					
					"    styleClass- fixo panel border rounded, podendo ser adicionados outros css como bg-dark, bg-success e etc \r\n"+					
					"    styleHeader- usar css puro alterando apenas a área do texto do header do panel \r\n"+					
					"    styleClassHeader- usar css, este altera a área total do header \r\n"+					
					"    styleBody- usar css puro, este altera a área do conteudo \r\n"+					
					"    styleClassBody- usar css, este altera a área do conteudo \r\n"+					
					"    styleFooter- usar css puro, este altera a área do rodapé \r\n"+					
					"    styleClassFooter- usar css, este altera a área do rodapé \r\n"+					
					"Exemplo - \r\n"+
					"    <otm2:panel name=\"panelIdPrimary\" >\r\n" + 
					"		<ui:define name=\"header\">\r\n" + 
					"			Coloque aqui o título  do panel\r\n" + 
					"		</ui:define>\r\n" + 
					"		<ui:define name=\"body\">\r\n" + 
					"			Coloque aqui o conteudo do panel\r\n" + 
					"		</ui:define>\r\n" + 
					"		<ui:define name=\"footer\">\r\n" + 
					"			Coloque aqui o rodapé do panel\r\n" + 
					"		</ui:define>\r\n" + 
					"	</otm2:panel>"
					);
			getRegraCampo().put("TIMELINE_1", "Componente:\r\n    otm:timeLine E otm2:timeLineColumn \r\n"+
					"Propriedades otm:timeLine :\r\n"+
					"    id- Obrigatorio \r\n"+					
					"    values - Obrigatorio - lista de objetos a serem listados no timeline \r\n"+					
					"    var- Obrigatorio - variavel que referencia os objetos no  otm2:timeLineColumn \r\n"+					
					"    row- fixo row, usar como contador do timeline \r\n"+													
					"Propriedades otm2:timeLineColumn :\r\n"+										
					"    opened - default true usado para trazer expandido o panel \r\n"+					
					"    renderedHeader- default true  \r\n"+
					"    renderedBody- default true  \r\n"+
					"    renderedFooter- default true  \r\n"+									
					"    stylePanel- default padding:10px !important -  neste usa css puro \r\n"+														
					"    styleClassPanel- fixo panel bg-light -  neste usa css \r\n"+														
					"    styleHeader- usar css puro alterando apenas a área do texto do header do panel \r\n"+														
					"    styleBody- usar css, este altera a área do conteudo \r\n"+										
					"    styleFooter- usar css, este altera a área do rodapé \r\n"+														
					"    styleBackIcon- usar css, este altera a área do icone \r\n"+														
					"Exemplo - \r\n"+
					"	<otm:timeLine id=\"timeLinePessoas\" values=\"#{showCaseControle.controleConsultaOtimizado.listaConsulta}\" var=\"pessoaItem\">\r\n" + 
					"		<otm2:timeLineColumn id=\"matriculaPeriodo_#{row}\">\r\n" + 
					"			<ui:define name=\"iconTime\">\r\n" + 
					"				<h:panelGroup styleClass=\"fas #{pessoaItem.sexo eq 'F' ? 'fa-female' : (pessoaItem.sexo eq 'M' ? 'fa-male' : 'fa-mars') }\"></h:panelGroup>\r\n" + 
					"			</ui:define>\r\n" + 
					"			<ui:define name=\"headerTime\">\r\n" + 
					"				<h:outputText value=\"Header do Timeline\"></h:outputText>\r\n" + 
					"			</ui:define>\r\n" + 
					"			<ui:define name=\"bodyTime\">\r\n" + 
					"				<h:outputText value=\"Conteúdo do Timeline\"></h:outputText>\r\n" + 
					"			</ui:define>\r\n" + 
					"			<ui:define name=\"footerTime\">\r\n" + 
					"				<h:outputText value=\"Ropapé do Timeline\"></h:outputText>\r\n" + 
					"			</ui:define>\r\n" + 
					"		</otm2:timeLineColumn>\r\n" + 
					"	</otm:timeLine>"
					);
			getRegraCampo().put("PDFVIEW_1", "Componente:\r\n    otm:pdfView \r\n"+
					"Propriedades:\r\n"+
					"    id- Obrigatorio \r\n"+					
					"    data- Obrigatorio - url do caminho web do PDF \r\n"+					
					"Exemplo - \r\n"+
					"    <otm:pdfView id=\"pdf\" data=\"#{showCaseControle.configuracaoGeralSistemaVO.urlAcessoExternoAplicacao}/arquivos/API_INTEGRACAO_SEI.pdf\"></otm:pdfView>"
					);
	}
	
	public void consultarPessoa() {		
		try {
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getPessoaFacade().consultaRapidaResumidaPorNome("%%", TipoPessoa.ALUNO.getValor(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getPessoaFacade().consultaTotalDeRegistroRapidaResumidaPorNome("%%", TipoPessoa.ALUNO.getValor(),  false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void paginarPessoa(DataScrollEvent dataScrollEvent) {
		getControleConsultaOtimizado().setPage(dataScrollEvent.getPage());
		getControleConsultaOtimizado().setPaginaAtual(dataScrollEvent.getPage());
		consultarPessoa();
	}

	public DashboardVO getDashBoard() {
		return dashBoard;
	}

	public void setDashBoard(DashboardVO dashBoard) {
		this.dashBoard = dashBoard;
	}

	public Map<String, String> getRegraCampo() {
		if(regraCampo == null) {
			regraCampo = new HashMap<String, String>(0);
		}
		return regraCampo;
	}

	public void setRegraCampo(Map<String, String> regraCampo) {
		this.regraCampo = regraCampo;
	}
	

	private ProgressBarVO progressBarSincronoVO;

	public ProgressBarVO getProgressBarSincronoVO() {
		if(progressBarSincronoVO == null) {
			progressBarSincronoVO =  new ProgressBarVO();
		}
		return progressBarSincronoVO;
	}

	public void setProgressBarSincronoVO(ProgressBarVO progressBarSincronoVO) {
		this.progressBarSincronoVO = progressBarSincronoVO;
	}
	
	public void iniciarProgressBarSincrono() {
		getProgressBarSincronoVO().resetar();
		getProgressBarSincronoVO().iniciar(0l, 20, "Iniciando....", false, this, "");	
		
	}
	
	public void realizarProcessamentoProgressBarAssincrono() {		
		while(getProgressBarSincronoVO().getProgresso() <= getProgressBarSincronoVO().getMaxValue()) {
			try {
				getProgressBarSincronoVO().setStatus("Processando item "+getProgressBarSincronoVO().getProgresso());
				Thread.sleep(1000l);
			} catch (InterruptedException e) {			
				e.printStackTrace();
			}
			getProgressBarSincronoVO().setProgresso(getProgressBarSincronoVO().getProgresso()+1);
		}
	}
	
	public void realizarAtualizacaoProgressBar() {
		
	}
	
	public void iniciarProgressBarAssincrono() {
		getProgressBarSincronoVO().resetar();
		getProgressBarSincronoVO().iniciar(0l, 20, "Iniciando....", true, this, "realizarProcessamentoProgressBarAssincrono");
	}
	
	

}
