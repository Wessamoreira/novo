package negocio.comuns.financeiro;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.ConteudoPlanejamentoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.EstagioVO;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.ItemPlanoFinanceiroAlunoVO;
import negocio.comuns.academico.MatriculaEnadeVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.academico.PlanoFinanceiroAlunoVO;
import negocio.comuns.academico.PlanoFinanceiroCursoVO;
import negocio.comuns.academico.ProgramacaoFormaturaCursoVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;
import negocio.comuns.academico.enumeradores.AlinhamentoAssinaturaDigitalEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.academico.enumeradores.TipoTextoEnadeEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.TextoPadraoLayoutVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.TipoDesigneTextoEnum;
import negocio.comuns.basico.DadosComerciaisVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Extenso;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisIreport;
import negocio.comuns.utilitarias.ValorExtensoOrdinal;
import negocio.comuns.utilitarias.dominios.CorRaca;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.dominios.OrientacaoPaginaEnum;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.SituacaoVencimentoMatriculaPeriodo;
import negocio.comuns.utilitarias.dominios.TipoDisciplina;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoTextoPadrao;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import relatorio.negocio.comuns.academico.CertificadoCursoExtensaoDisciplinasRelVO;
import relatorio.negocio.comuns.academico.PlanoDisciplinaRelVO;
import relatorio.negocio.comuns.financeiro.DescontoTipoDescontoRelVO;
import relatorio.negocio.comuns.financeiro.TermoReconhecimentoDividaDebitoRelVO;
import relatorio.negocio.comuns.financeiro.TermoReconhecimentoDividaParcelasRelVO;
import relatorio.negocio.comuns.processosel.ChamadaCandidatoAprovadoRelVO;
import relatorio.negocio.comuns.processosel.EstatisticaProcessoSeletivoVO;
import relatorio.negocio.comuns.processosel.enumeradores.TipoRelatorioEstatisticoProcessoSeletivoEnum;
import webservice.servicos.MatriculaRSVO;


/**
 * Reponsável por manter os dados da entidade PlanoTextoPadrao. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "textoPadrao")
public class TextoPadraoVO extends TextoPadraoLayoutVO {

	private Integer codigo;
	private String descricao;
	private String situacao;
	private Date dataDefinicao;
	private String texto;
	private String tipo;
	private UnidadeEnsinoVO unidadeEnsino;
	private String orientacaoDaPagina;
	private String margemDireita;
	private String margemEsquerda;
	private String margemSuperior;
	private String margemInferior;
	private TipoDesigneTextoEnum tipoDesigneTextoEnum;
	private ArquivoVO arquivoIreport;
	private Boolean assinarDigitalmenteTextoPadrao = false;
	private String corAssinaturaDigitalmente;
	private AlinhamentoAssinaturaDigitalEnum alinhamentoAssinaturaDigitalEnum;
	private Float larguraAssinatura;
	private Float alturaAssinatura;
	private HashMap<String, Object> parametrosRel;
	private List<ArquivoVO > listaArquivoIreport;

	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Usuario</code>.
	 */
	private UsuarioVO responsavelDefinicao;
	private List listaTagUtilizado;
	private String tagGeral;
	private List listaDisciplinasCursadasOuMinistradas;
	private List listaDisciplinaCertificado;
	private List<DescontoTipoDescontoRelVO> listaTipoDescontoCurso;
	private List<DescontoTipoDescontoRelVO> listaTodosTipoDesconto_Curso;
	private List<MatriculaPeriodoVencimentoVO> listaAcompanhamentoFinanceiroMatricula;
	private List<MatriculaPeriodoVencimentoVO> listaContaReceberComDescontos;
	private List listaDisciplinasAprovadasPeriodoLetivo;
	private List<PlanoDisciplinaRelVO> listaPlanoDisciplinas;
	
	private FuncionarioVO funcionarioPrimarioVO;
	private FuncionarioVO funcionarioSecundarioVO;
    private CargoVO cargoFuncionarioPrincipalVO;
	private CargoVO cargoFuncionarioSecundarioVO;
	private Boolean assinarDocumentoAutomaticamenteFuncionarioPrimario;
	private Boolean assinarDocumentoAutomaticamenteFuncionarioSecundario;
	private List listaDocumentosEntregues;
	
	
	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>PlanoTextoPadrao</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public TextoPadraoVO() {
		super();
		//inicializarDados();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>PlanoTextoPadraoVO</code>. Todos os tipos de consistência de dados
	 * são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(TextoPadraoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getDescricao().equals("")) {
			throw new ConsistirException("O campo DESCRIÇÃO ( Texto Padrao) deve ser informado.");
		}
		if (obj.getSituacao().equals("")) {
			throw new ConsistirException("O campo SITUAÇÃO (Texto Padrao) deve ser informado.");
		}
		if (obj.getTipo().equals("")) {
			throw new ConsistirException("O campo TIPO (Texto Padrao) deve ser informado.");
		}
		if (obj.getDataDefinicao() == null) {
			throw new ConsistirException("O campo DATA DEFINIÇÃO (Texto Padrao) deve ser informado.");
		}
		if ((obj.getResponsavelDefinicao() == null) || (obj.getResponsavelDefinicao().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo RESPONSÁVEL DEFINIÇÃO ( Texto Padrao) deve ser informado.");
		}
		if (obj.getTexto().equals("")) {
			throw new ConsistirException("O campo TEXTO (Texto Padrao) deve ser informado.");
		}
		if (obj.getTipoDesigneTextoEnum().isPdf() && obj.getListaArquivoIreport().isEmpty()) {
			throw new ConsistirException("O campo ARQUIVO IREPORT (Texto Padrao) deve ser informado.");
		}
		 if (obj.getTipoDesigneTextoEnum().isPdf() &&! obj.possuiArquivoIreportPrincipal()) {
	        	throw new ConsistirException("O arquivo do Ireport (Texto Padrao) Principal deve ser adicionado.");
	        }
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	public void realizarUpperCaseDados() {
		setDescricao(getDescricao().toUpperCase());
		// setTexto(texto.toUpperCase());
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setDescricao("");
		setDataDefinicao(new Date());
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<style type='text/css'>");
		sb.append(" body { margin: 0; padding: 0; font-size:11px; } ");
		sb.append(" th { font-weight: normal; } ");
		sb.append(" * { -moz-box-sizing: border-box; } ");
		sb.append(" .page { width: 21cm; min-height: 29.7cm; padding: 2cm; margin: 1cm auto; } ");
		sb.append(" .subpage { padding-top: 1cm; padding-bottom: 1cm; padding-left: 1cm; padding-right: 1cm; height: 237mm; } ");
		sb.append(" @page { size: A4; margin: 0; } ");
		sb.append(" @media print { .page { margin: 0; border: initial; border-radius: initial; width: initial; min-height: initial; box-shadow: initial; background: initial; page-break-after: always; } } ");
		sb.append("</style>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<div class='page' style='padding: 2cm; width: 21cm; margin: 1cm auto; min-height: 29.7cm;'>");
		sb.append("<div class='subpage' style='border: 1px #CCCCCC dashed; height: 237mm; padding: 1cm;'>");
		sb.append("<div><span style='display:none'>&nbsp;</span>&nbsp;</div>");
		sb.append("</div>");
		sb.append("</div>");
		sb.append("</body>");
		sb.append("</html>");
		setTexto(sb.toString());
		setSituacao("EM");
	}

	public void clonar(UsuarioVO usuario) {
		this.setNovoObj(true);
		this.setCodigo(0);
		this.setDescricao(descricao + " - CLONE");
		setSituacao("EM");
		setDataDefinicao(new Date());
		setArquivoIreport(null);
		setResponsavelDefinicao(usuario);
		if(Uteis.isAtributoPreenchido(this.getListaArquivoIreport())) {
        	this.setListaArquivoIreport(new ArrayList<ArquivoVO>());
        	this.setTexto("");
        }
	}

	protected FacesContext context() {
		return (FacesContext.getCurrentInstance());
	}

	public String removerHtmlBotaoImprimirTextoPadraoContratoMatricula(String textoContrato) throws Exception {
		try {
			if (textoContrato.contains("<table id=\"tableBotao\"")) {
				int pInicio = textoContrato.indexOf("<table id=\"tableBotao\"");
				int pFim = textoContrato.indexOf("<script");
				String tabela = textoContrato.substring(pInicio, pFim);
				textoContrato = textoContrato.replace(tabela, "");
			}
		} catch (Exception e) {
			throw e;
		}
		return textoContrato;
	}

	public String substituirTagsTextoPadraoContratoMatricula(ImpressaoContratoVO impressaoContratoVO, MatriculaVO matricula,  List listaContasReceber, MatriculaPeriodoVO matriculaPeriodo, PlanoFinanceiroAlunoVO planoFinanceiroAluno, List<PlanoDescontoVO> listaPlanoDesconto, DadosComerciaisVO dc, UsuarioVO usuario) throws Exception {
		try {
			if (getTexto().length() != 0) {
				char aspas = '"';
				String espaco = "<p style=" + aspas + "text-align: left;" + aspas + ">";
				String htmlBotao = "<form> ";
				// String htmlBotao2 = " <table id=\"tableBotao\" style = " +
				// aspas + "width: 640px" + aspas + " cellspacing=" + aspas +
				// "0" + aspas + " cellpadding=" + aspas + "0" + aspas +
				// "> <tr> " + "<td style=" + aspas + "height: 19px" + aspas +
				// " align=" + aspas + "center" + aspas + " >" + "<a  id=" +
				// aspas + "Img_btImprimir" + aspas + " title=" + aspas +
				// "Imprimir Boleto" + aspas + " href=" + aspas +
				// "javascript:window.print();" + aspas + " >" + "<img title=" +
				// aspas + "Imprimir Boleto" + aspas + " src=" + aspas +
				// "./imagens/bt_imprimir.gif" + aspas + " border=" + aspas +
				// "0" + aspas + "/> </a>" + "<a id=" + aspas + "Img_btFechar" +
				// aspas + " title=" + aspas + "Fechar Janela" + aspas +
				// " href=" + aspas + "javascript:closeWindow();" + aspas + " >"
				// + "<img title=" + aspas + "Fechar Janela" + aspas + " src=" +
				// aspas + "./imagens/bt_fechar.gif" + aspas + " border=" +
				// aspas + "0" + aspas + " /> </a>" + "</td></tr></table>" +
				// "<script language=" + aspas + "JavaScript" + aspas + " type="
				// + aspas
				// + "text/javascript" + aspas + ">" +
				// "function closeWindow() {" + "if (parent) {" +
				// "parent.close();" + "} else {" +
				// "window.close();}}</script>";
				String textoSub = getTexto();
				textoSub = textoSub.replace("Untitled document", "CONTRATO DE PRESTAÇÃO DE SERVIÇOS EDUCACIONAIS");
				if (textoSub.contains("<body>")) {
					String tagBodyInicio = "<body>";
					String tagBodyFim = "</body>";
					if (!textoSub.contains(tagBodyInicio)) {
						textoSub = tagBodyInicio + textoSub;
					}
					if (!textoSub.contains(tagBodyFim)) {
						textoSub = textoSub + tagBodyFim;
					}
					int posicaoBody = textoSub.indexOf("<body>");
					String parte1 = textoSub.substring(0, posicaoBody + 6);
					String parte2 = textoSub.substring(posicaoBody + 6);
					textoSub = parte1 + htmlBotao + parte2;

					posicaoBody = textoSub.indexOf("</body>");
					parte1 = textoSub.substring(0, posicaoBody);
					parte2 = textoSub.substring(posicaoBody);
					textoSub = parte1 + espaco + "</form>" + parte2;
				}
				textoSub = varrerListaTagUtilizadoTextoPadraoContratoMatricula(textoSub, impressaoContratoVO, matricula, listaContasReceber, matriculaPeriodo, planoFinanceiroAluno, listaPlanoDesconto, dc, usuario);

				if (context() != null) {
					HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
					request.getSession().setAttribute("textoRelatorio", textoSub);
				}
				return textoSub;
			}
		} catch (Exception e) {
			throw e;
		}
		return "";
	}

	public List montarListaTagUtilizada(String texto) throws Exception {
		List listaTag = new ArrayList<TextoPadraoTagVO>(0);
		try {
			while (texto.indexOf("[(") != -1 && texto.indexOf("]") != -1) {
				TextoPadraoTagVO p = new TextoPadraoTagVO();
				int posIniTag = texto.indexOf("[(");
				texto = texto.substring(posIniTag);
				int posFimTag = texto.indexOf("]");
				String tag = texto.substring(0, posFimTag + 1);
				texto = texto.substring(posFimTag + 1);
				p.setTag(tag);
				listaTag.add(p);
			}
			return listaTag;
		} catch (Exception e) {
			// //System.out.println("Erro TextoPadraoVO.montarListaTagUtilizada: "
			// + e.getMessage());
			throw e;
		}
	}

	public String varrerListaTagUtilizadoTextoPadraoContratoMatricula(String texto, ImpressaoContratoVO impressaoContratoVO, MatriculaVO matricula , List listaContasReceber, MatriculaPeriodoVO matriculaPeriodo, PlanoFinanceiroAlunoVO planoFinanceiroAluno, List<PlanoDescontoVO> listaPlanoDesconto, DadosComerciaisVO dc, UsuarioVO usuario) throws Exception {

		MarcadorVO marcador = new MarcadorVO();
		List lista = montarListaTagUtilizada(texto);
		Iterator i = lista.iterator();
		while (i.hasNext()) {
			TextoPadraoTagVO p = (TextoPadraoTagVO) i.next();
			marcador.setTag(p.getTag());

			texto = verificaQualMetodoParaSubstituicaoInvocar(marcador, texto, impressaoContratoVO, matricula, listaContasReceber, matriculaPeriodo, planoFinanceiroAluno, listaPlanoDesconto, dc, null, null, usuario);

		}
		return texto;
	}
	
	public String substituirTagsTextoPadraoContratoFiador(MatriculaVO matricula,   List listaContasReceber, MatriculaPeriodoVO matriculaPeriodo, PlanoFinanceiroAlunoVO planoFinanceiroAluno, List<PlanoDescontoVO> listaPlanoDesconto, DadosComerciaisVO dc, UsuarioVO usuario) throws Exception {

		try {
			if (getTexto().length() != 0) {
				char aspas = '"';
				String espaco = "<p style=" + aspas + "text-align: left;" + aspas + ">";
				String htmlBotao = "<form> ";
				// String htmlBotao2 = " <table  style = " + aspas +
				// "width: 640px" + aspas + " cellspacing=" + aspas + "0" +
				// aspas + " cellpadding=" + aspas + "0" + aspas + "> <tr> " +
				// "<td style=" + aspas + "height: 19px" + aspas + " align=" +
				// aspas + "center" + aspas + " >" + "<a  id=" + aspas +
				// "Img_btImprimir" + aspas + " title=" + aspas +
				// "Imprimir Boleto" + aspas + " href=" + aspas +
				// "javascript:window.print();" + aspas + " >" + "<img title=" +
				// aspas + "Imprimir Boleto" + aspas + " src=" + aspas +
				// "./imagens/bt_imprimir.gif" + aspas + " border=" + aspas +
				// "0" + aspas + "/> </a>" + "<a id=" + aspas + "Img_btFechar" +
				// aspas + " title=" + aspas + "Fechar Janela" + aspas +
				// " href=" + aspas + "javascript:closeWindow();" + aspas + " >"
				// + "<img title=" + aspas + "Fechar Janela" + aspas + " src=" +
				// aspas + "./imagens/bt_fechar.gif" + aspas + " border=" +
				// aspas + "0" + aspas + " /> </a>" + "</td></tr></table>" +
				// "<script language=" + aspas + "JavaScript" + aspas + " type="
				// + aspas
				// + "text/javascript" + aspas + ">" +
				// "function closeWindow() {" + "if (parent) {" +
				// "parent.close();" + "} else {" +
				// "window.close();}}</script>";
				String textoSub = getTexto();
				textoSub = textoSub.replace("Untitled document", "CONTRATO DE PRESTAÇÃO DE SERVIÇOS EDUCACIONAIS");
				if (textoSub.contains("<body>")) {
					String tagBodyInicio = "<body>";
					String tagBodyFim = "</body>";
					if (!textoSub.contains(tagBodyInicio)) {
						textoSub = tagBodyInicio + textoSub;
					}
					if (!textoSub.contains(tagBodyFim)) {
						textoSub = textoSub + tagBodyFim;
					}
					int posicaoBody = textoSub.indexOf("<body>");
					String parte1 = textoSub.substring(0, posicaoBody + 6);
					String parte2 = textoSub.substring(posicaoBody + 6);
					textoSub = parte1 + htmlBotao + parte2;

					posicaoBody = textoSub.indexOf("</body>");
					parte1 = textoSub.substring(0, posicaoBody);
					parte2 = textoSub.substring(posicaoBody);
					textoSub = parte1 + espaco + "</form>" + parte2;
				}
				textoSub = varrerListaTagUtilizadoTextoPadraoContratoFiador(textoSub, matricula,  listaContasReceber, matriculaPeriodo, planoFinanceiroAluno, listaPlanoDesconto, dc, usuario);

				HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
				request.getSession().setAttribute("textoRelatorio", textoSub);
				return textoSub;
			}
		} catch (Exception e) {
			throw e;
		}
		return "";
	}

	public String varrerListaTagUtilizadoTextoPadraoContratoFiador(String texto, MatriculaVO matricula,   List listaContasReceber, MatriculaPeriodoVO matriculaPeriodo, PlanoFinanceiroAlunoVO planoFinanceiroAluno, List<PlanoDescontoVO> listaPlanoDesconto, DadosComerciaisVO dc, UsuarioVO usuario) throws Exception {

		MarcadorVO marcador = new MarcadorVO();
		List lista = montarListaTagUtilizada(texto);
		Iterator i = lista.iterator();
		while (i.hasNext()) {
			TextoPadraoTagVO p = (TextoPadraoTagVO) i.next();
			marcador.setTag(p.getTag());
			texto = verificaQualMetodoParaSubstituicaoInvocar(marcador, texto, null, matricula, listaContasReceber, matriculaPeriodo, planoFinanceiroAluno, listaPlanoDesconto, dc, null, null, usuario);

		}
		return texto;
	}
	
	public void substituirTag(ImpressaoContratoVO impressaoContrato, UsuarioVO usuario) throws Exception {
		MarcadorVO mar = new MarcadorVO();
		TextoPadraoVO t = new TextoPadraoVO();
		PlanoFinanceiroAlunoVO planoFinanceiroVO = new PlanoFinanceiroAlunoVO();
		List<PlanoDescontoVO> planoDescontoVOs = new ArrayList<PlanoDescontoVO>(0);
		DadosComerciaisVO dcAluno = impressaoContrato.getMatriculaVO().getAluno().getDadosComerciaisVOs().stream()
			.filter(p -> p.getEmpregoAtual())
			.findFirst()
			.orElse(impressaoContrato.getMatriculaVO().getAluno().getDadosComerciaisVOs().stream().findFirst().orElse(null));
		
		DadosComerciaisVO dcProfessor = impressaoContrato.getProfessor().getPessoa().getDadosComerciaisVOs().stream()
			.filter(p -> p.getEmpregoAtual())
			.findFirst()
			.orElse(impressaoContrato.getProfessor().getPessoa().getDadosComerciaisVOs().stream().findFirst().orElse(null));
		
		Pattern p = Pattern.compile("\\[(.*?)\\]");
		Matcher m = p.matcher(getTexto());
		while (m.find()) {
			mar.setTag(m.group());
			if (mar.getTag().contains("_Aluno")) {
				setTexto(t.substituirTagAluno(getTexto(), impressaoContrato, mar, dcAluno));
			} else if (mar.getTag().contains("_Professor")) {
				setTexto(t.substituirTagProfessor(getTexto(), impressaoContrato, mar, dcProfessor));
			} else if (mar.getTag().contains("_UnidadeEnsino")) {
				setTexto(t.substituirTagUnidadeEnsino(getTexto(), impressaoContrato, mar));
			} else if (mar.getTag().contains("_Matricula") || mar.getTag().contains("_matricula")) {
				setTexto(t.substituirTagMatricula(getTexto(), impressaoContrato, mar));
			} else if (mar.getTag().contains("_Curso")) {
				setTexto(t.substituirTagCurso(getTexto(), impressaoContrato, mar, planoFinanceiroVO, planoDescontoVOs, usuario));
			} else if (mar.getTag().contains("_Disciplina")) {
				setTexto(t.substituirTagDisciplina(getTexto(), impressaoContrato, mar));
			} else if (mar.getTag().contains("_Estagio")) {
				setTexto(t.substituirTagEstagio(getTexto(), impressaoContrato, mar));
			} else if (mar.getTag().contains("_InscProcSeletivo")) {
				setTexto(t.substituirTagInscProcSeletivo(getTexto(), impressaoContrato, mar));
			} else if (mar.getTag().contains("_Outras")) {
				setTexto(t.substituirTagOutras(getTexto(), mar, impressaoContrato));
			} else if (mar.getTag().contains("_ContaReceber") && Uteis.isAtributoPreenchido(impressaoContrato.getNegociacaoContaReceberVO())) {
				setTexto(t.substituirTagNegociacaoContaReceber(texto, impressaoContrato.getNegociacaoContaReceberVO(), mar));
			} else if (mar.getTag().contains("_ProgramacaoFormaturaAluno")) {
				setTexto(t.substituirTagProgramacaoFormatura(getTexto(), impressaoContrato, mar));
			} else {
				setTexto(t.substituirTagAluno(getTexto(), impressaoContrato, mar, dcAluno));
				setTexto(t.substituirTagProfessor(getTexto(), impressaoContrato, mar, dcProfessor));
				setTexto(t.substituirTagUnidadeEnsino(getTexto(), impressaoContrato, mar));
				setTexto(t.substituirTagMatricula(getTexto(), impressaoContrato, mar));
				setTexto(t.substituirTagCurso(getTexto(), impressaoContrato, mar, planoFinanceiroVO, planoDescontoVOs, usuario));
				setTexto(t.substituirTagDisciplina(getTexto(), impressaoContrato, mar));
				setTexto(t.substituirTagOutras(getTexto(), mar, impressaoContrato));
				setTexto(t.substituirTagProgramacaoFormatura(getTexto(), impressaoContrato, mar));
				setTexto(t.substituirTagRequerimento(getTexto(), impressaoContrato, mar));
			}
		}
		getParametrosRel().putAll(t.getParametrosRel());
		planoFinanceiroVO = null;
		planoDescontoVOs = null;
	}

	public String verificaQualMetodoParaSubstituicaoInvocar(MarcadorVO marcador, String texto, ImpressaoContratoVO impressaoContratoOrigemVO, MatriculaVO matricula, List listaContasReceber, MatriculaPeriodoVO matriculaPeriodo, PlanoFinanceiroAlunoVO planoFinanceiroAluno, List<PlanoDescontoVO> listaPlanoDesconto, DadosComerciaisVO dc, Integer qtdeParcela, Double valorTotal, UsuarioVO usuario) throws Exception {
		ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
		if (impressaoContratoOrigemVO == null) {
			impressaoContratoOrigemVO = new ImpressaoContratoVO();
		}
		impressaoContratoVO.setCargaHorariaRealizadaAtividadeComplementar(impressaoContratoOrigemVO.getCargaHorariaRealizadaAtividadeComplementar());
		impressaoContratoVO.setListaDisciplinasCursadasOuMinistradas(impressaoContratoOrigemVO.getListaDisciplinasCursadasOuMinistradas());
		impressaoContratoVO.setListaDisciplinasPeriodoLetivoAtual(impressaoContratoOrigemVO.getListaDisciplinasPeriodoLetivoAtual());
		impressaoContratoVO.setListaDisciplinasHistoricoPeriodoLetivoSituacao(impressaoContratoOrigemVO.getListaDisciplinasHistoricoPeriodoLetivoSituacao());
		impressaoContratoVO.setPeriodoLetivoIngresso(impressaoContratoOrigemVO.getPeriodoLetivoIngresso());
		impressaoContratoVO.setMatriculaPeriodoVO(matriculaPeriodo);
		impressaoContratoVO.setPessoaEmailInstitucionalVO(impressaoContratoOrigemVO.getPessoaEmailInstitucionalVO());
		impressaoContratoVO.setMatriculaVO(matricula);
		if (marcador.getTag().contains("_Aluno")) {
			return substituirTagAluno(texto, impressaoContratoVO, marcador, dc);
		} else if (marcador.getTag().contains("_UnidadeEnsino")) {
			return substituirTagUnidadeEnsino(texto, impressaoContratoVO, marcador);
		} else if (marcador.getTag().contains("_Matricula")) {
			return substituirTagMatricula(texto, impressaoContratoVO, marcador);
		} else if (marcador.getTag().contains("_Curso")) {
			return substituirTagCurso(texto, impressaoContratoVO, marcador, planoFinanceiroAluno, listaPlanoDesconto, usuario);
		} else if (marcador.getTag().contains("_ContaReceber")) {
			return substituirTagContaReceber(texto, listaContasReceber, marcador);
		} else if (marcador.getTag().contains("_Disciplina")) {
			return substituirTagDisciplinasMatricula(texto, matriculaPeriodo, matriculaPeriodo.getMatriculaPeriodoTumaDisciplinaVOs(), marcador);
		} else if (marcador.getTag().contains("_Estagio")) {
			return substituirTagEstagio(texto, impressaoContratoVO, marcador);
		} else if (marcador.getTag().contains("_InscProcSeletivo")) {
			return substituirTagInscProcSeletivo(texto, impressaoContratoVO, marcador);
		} else if (marcador.getTag().contains("_Outras")) {
			return substituirTagOutras(texto, marcador, impressaoContratoVO);
		} else if (marcador.getTag().contains("_InclusaoReposicao")) {
			return substituirTagInclusaoReposicao(texto, qtdeParcela, valorTotal, marcador);
		}
		return texto;
	}
	
	// Metodo responsável por substituir as tags pelos valores especificos.
	public String substituirTag(String texto, String tag, String valor, String textoTag, int tamanhoOcupacaoTag) {
		// tag = Uteis.trocarAcentuacaoPorAcentuacaoHTML(tag);
		int tam = valor.length();
		if (tamanhoOcupacaoTag == 0) {
			tamanhoOcupacaoTag = tam;
		}
		if (tamanhoOcupacaoTag < tam) {
			valor = valor.substring(0, tamanhoOcupacaoTag);
		}
		texto = texto.replace(tag, textoTag + valor);
		String tagSemTextoSemTamanho = obterTagSemTextoSemTamanho(tag);
		if (getParametrosRel() != null) {
			getParametrosRel().put(tagSemTextoSemTamanho, valor);
		}
		return texto;
	}

	// Metodo responsável por substituir as tags pelos valores especificos.
	public void substituirTagComLista(String texto, String tag, List valor, String textoTag) {
		String tagSemTextoSemTamanho = obterTagSemTextoSemTamanho(tag);
		if (getParametrosRel() != null) {
			getParametrosRel().put(tagSemTextoSemTamanho, valor);
		}
	}

	// Metodo responsável por retornar uma String com caracteres em branco, de
	// acordo com o tamanho da tag
	// assim, a rotina substitui a tag, deixando assim os campos contrato em
	// branco quando não possuem dados.
	public String obterStringEmBrancoParaTag(MarcadorVO marcador) {
		int tamanho = marcador.getTag().length();
		String retorno = "";
		int cont = 0;
		while (cont < tamanho) {
			retorno += " ";
			cont++;
		}
		return retorno;
	}

	public String retirarLinhasHtml(String texto, String tag) {
		// tag = Uteis.trocarAcentuacaoPorAcentuacaoHTML(tag);
		String novoTexto = texto.substring(0, texto.indexOf(tag) + tag.length());
		novoTexto = novoTexto.replace(tag, "");
		texto = novoTexto + texto.substring(texto.indexOf(tag) + tag.length());
		return texto;

	}

	public String gerarLinhasHtml(String texto, MarcadorVO marcador) {
		// int posicaoTag =
		// texto.indexOf(Uteis.trocarAcentuacaoPorAcentuacaoHTML(marcador.getTag()));
		int posicaoTag = texto.indexOf(marcador.getTag());
		if (posicaoTag == -1) {
			posicaoTag = 0;
		}
		String parteStr1 = texto.substring(0, posicaoTag);
		String parteStr2 = texto.substring(posicaoTag);
		texto = parteStr1 + "<br/>" + marcador.getTag() + parteStr2;
		return texto;

	}

	public String substituirTagQuandoConcatenadaAOutrasTAGs(String texto, String tag, String valor, String textoTag, int tamanhoOcupacaoTag) {
		// tag = Uteis.trocarAcentuacaoPorAcentuacaoHTML(tag);
		int tam = valor.length();
		if (tamanhoOcupacaoTag < tam) {
			valor = valor.substring(0, tamanhoOcupacaoTag);
		}
		String novoTexto = texto.substring(0, texto.indexOf(tag) + tag.length());
		novoTexto = novoTexto.replace(tag, textoTag + valor);
		texto = novoTexto + texto.substring(texto.indexOf(tag) + tag.length());
		if (getParametrosRel() != null) {
			String tagSemFormatacao = obterTagSemTextoSemTamanho(tag);
			Object valorParametro = (Object) getParametrosRel().get(tagSemFormatacao);
			if (valorParametro != null && valorParametro instanceof String && !((String) valorParametro).isEmpty()) {
				valorParametro += "<br/>" + valor;
				getParametrosRel().put(tagSemFormatacao, valorParametro);
			} else {
				getParametrosRel().put(tagSemFormatacao, valor);
			}
		}
		return texto;
	}

	public String substituirTagNegociacaoContaReceber(String texto, NegociacaoContaReceberVO negociacaoContaReceberVO, MarcadorVO marcador) throws Exception {
		int tamanho = obterTamanhoTag(marcador.getTag());
		tagGeral = marcador.getTag();

		String textoTag = obterTextoTag(tagGeral);
		String mar = obterTagSemTextoSemTamanhoQuandoConcatenadaAOutraTag(tagGeral);

		if (mar.contains("ValorBase_ContaReceber")) {
			texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, 
					Uteis.isAtributoPreenchido(negociacaoContaReceberVO.getValor()) ? negociacaoContaReceberVO.getValor().toString() : "0", textoTag, tamanho);
		}  else if (mar.contains("ValorJuro_ContaReceber")) {
			texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, 
					Uteis.isAtributoPreenchido(negociacaoContaReceberVO.getJuroCalculado()) ? negociacaoContaReceberVO.getJuroCalculado().toString() : "0", textoTag, tamanho);
		}  else if (mar.contains("ValorDesconto_ContaReceber")) {
			texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, 
					Uteis.isAtributoPreenchido(negociacaoContaReceberVO.getDescontoCalculado()) ? negociacaoContaReceberVO.getDescontoCalculado().toString() : "0", textoTag, tamanho);
		}  else if (mar.contains("ValorAcrescimo_ContaReceber")) {
			texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, 
					Uteis.isAtributoPreenchido(negociacaoContaReceberVO.getAcrescimoGeral()) ? negociacaoContaReceberVO.getAcrescimoGeral().toString() : "0", textoTag, tamanho);
		}  else if (mar.contains("ValorTotal_ContaReceber")) {
			texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, 
					Uteis.isAtributoPreenchido(negociacaoContaReceberVO.getValorTotal()) ? negociacaoContaReceberVO.getValorTotal().toString() : "0", textoTag, tamanho);
		} else if (mar.contains("ValorBaseContaReceber_ContaReceber")) {
			Double valorBaseContasReceber = negociacaoContaReceberVO.getContaReceberNegociadoVOs().stream()
					.mapToDouble(contaReceberNegociado -> contaReceberNegociado.getContaReceber().getValorBaseContaReceber()).sum();
			texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, Uteis.arrendondarForcando2CadasDecimaisStr(valorBaseContasReceber), textoTag, tamanho);
		}  else if (mar.contains("ValorJuroContaReceber_ContaReceber")) {
			Double valorJurosContasReceber = negociacaoContaReceberVO.getContaReceberNegociadoVOs().stream()
					.mapToDouble(contaReceberNegociado -> contaReceberNegociado.getContaReceber().getJuro()).sum();
			texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, Uteis.arrendondarForcando2CadasDecimaisStr(valorJurosContasReceber), textoTag, tamanho);
		}  else if (mar.contains("ValorMultaContaReceber_ContaReceber")) {
			Double valorMultaContasReceber = negociacaoContaReceberVO.getContaReceberNegociadoVOs().stream()
					.mapToDouble(contaReceberNegociado -> contaReceberNegociado.getContaReceber().getMulta()).sum();
			texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, Uteis.arrendondarForcando2CadasDecimaisStr(valorMultaContasReceber), textoTag, tamanho);
		} else if (mar.contains("ValorReajusteContaReceber_ContaReceber")) {
			Double valorReajusteAtrasoContasReceber = negociacaoContaReceberVO.getContaReceberNegociadoVOs().stream()
					.mapToDouble(contaReceberNegociado -> contaReceberNegociado.getContaReceber().getValorIndiceReajustePorAtraso().doubleValue()).sum();
			texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, Uteis.arrendondarForcando2CadasDecimaisStr(valorReajusteAtrasoContasReceber), textoTag, tamanho);
		}  else if (mar.contains("ValorAcrescimoContaReceber_ContaReceber")) {
			Double valorAcrescimoBaseContasReceber = negociacaoContaReceberVO.getContaReceberNegociadoVOs().stream()
					.mapToDouble(contaReceberNegociado -> contaReceberNegociado.getContaReceber().getAcrescimo().doubleValue()).sum();
			texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, Uteis.arrendondarForcando2CadasDecimaisStr(valorAcrescimoBaseContasReceber), textoTag, tamanho);
		}  else if (mar.contains("ValorDescontoContaReceber_ContaReceber")) {
			Double valorDescontosBaseContasReceber = negociacaoContaReceberVO.getContaReceberNegociadoVOs().stream()
					.mapToDouble(contaReceberNegociado -> contaReceberNegociado.getContaReceber().getValorDesconto()).sum();
			texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, Uteis.arrendondarForcando2CadasDecimaisStr(valorDescontosBaseContasReceber), textoTag, tamanho);
		}
		
		if(mar.contains("ResponsavelNegociacao_ContaReceber")) {
			String responsavel = negociacaoContaReceberVO.getResponsavel().getNome();
			texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar,responsavel , textoTag, tamanho);
		}

		return texto;
	}

	public String substituirTagContaReceber(String texto, List listaContasReceber, MarcadorVO marcador) throws Exception {
		String emBranco = obterStringEmBrancoParaTag(marcador);
		tagGeral = marcador.getTag();

		if (listaContasReceber.size() == 0) {
			texto = texto.replace(marcador.getTag(), emBranco);
		} else {
			Ordenacao.ordenarLista(listaContasReceber, "dataVencimento");
			Iterator i = listaContasReceber.iterator();
			while (i.hasNext()) {
				MatriculaPeriodoVencimentoVO matPerVenc = (MatriculaPeriodoVencimentoVO) i.next();
				if (matPerVenc.getVencimentoReferenteMatricula() && matPerVenc.getSituacao().equals(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_NAO_DEVE_SER_GERADA)) {
					continue;
				}
				texto = gerarLinhasHtml(texto, marcador);
				ContaReceberVO contaReceber = matPerVenc.getContaReceber();
				if (contaReceber.getCodigo().equals(0)) {
					contaReceber.setDataVencimento(matPerVenc.getDataVencimento());
					contaReceber.setValor(matPerVenc.getValor());
					contaReceber.setParcela(matPerVenc.getParcela());
					contaReceber.setTipoDesconto(matPerVenc.getTipoDesconto());
				}
				Double valorDescontoConvertidoReais = 0.0;
				if (contaReceber.getTipoDesconto().equals("PO")) {
					valorDescontoConvertidoReais = (contaReceber.getValor() * contaReceber.getValorDesconto()) / 100;
				} else {
					valorDescontoConvertidoReais = contaReceber.getValorDesconto();
				}
				String codigo = matPerVenc.getCodigo().toString();
				if (codigo.equals("0") || codigo.equals("")) {
					codigo = emBranco;
				}
				String descricao = matPerVenc.getDescricaoPagamento();
				if (descricao.equals("")) {
					descricao = emBranco;
				}
				// String nrDocumento = contaReceber.getNrDocumento();
				// if (nrDocumento.equals("")) {
				// nrDocumento = emBranco;
				// }
				String parcela = matPerVenc.getParcela();
				if (parcela.equals("")) {
					parcela = emBranco;
				}
				String dataVcto = matPerVenc.getDataVencimento_Apresentar();
				if (dataVcto.equals("")) {
					dataVcto = emBranco;
				}
				// String tipoOrigem = contaReceber.getTipoOrigem();
				// if (tipoOrigem.equals("")) {
				// tipoOrigem = emBranco;
				// }
				String valor = Uteis.getDoubleFormatado(contaReceber.getValor());
				Extenso extensoValorDivididoPorDois = new Extenso();
				extensoValorDivididoPorDois.setNumber(contaReceber.getValor() / 2);
				Extenso extensoValorDivididoPorTres = new Extenso();
				extensoValorDivididoPorTres.setNumber(contaReceber.getValor() / 3);
				if (valor == null || valor.equals("")) {
					valor = emBranco;
				}
				String valorDesconto = Uteis.getDoubleFormatado(valorDescontoConvertidoReais);
				if (valorDesconto == null || valorDesconto.equals("")) {
					valorDesconto = emBranco;
				}
				String descontoInstituicao = Uteis.getDoubleFormatado(contaReceber.getValorDescontoInstituicao());
				if (descontoInstituicao == null || descontoInstituicao.equals("")) {
					descontoInstituicao = emBranco;
				}
				String descontoProgressivo = Uteis.getDoubleFormatado(contaReceber.getValorDescontoProgressivo());
				if (descontoProgressivo == null || descontoProgressivo.equals("")) {
					descontoProgressivo = emBranco;
				}
				String descontoConvenio = Uteis.getDoubleFormatado(contaReceber.getValorDescontoConvenio());
				if (descontoConvenio == null || descontoConvenio.equals("")) {
					descontoConvenio = emBranco;
				}
				String valorCusteadoContaReceber = Uteis.getDoubleFormatado(contaReceber.getValorCusteadoContaReceber());
				if (valorCusteadoContaReceber == null || valorCusteadoContaReceber.equals("")) {
					valorCusteadoContaReceber = emBranco;
				}
				String valorBaseContareceber = "";
				String valorContareceberMenosDecontoInstituicao = "";
				if(Uteis.isAtributoPreenchido(contaReceber.getValorBaseContaReceber())){
					valorBaseContareceber = Uteis.getDoubleFormatado(contaReceber.getValorBaseContaReceber());
					valorContareceberMenosDecontoInstituicao = Uteis.getDoubleFormatado(contaReceber.getValorBaseContaReceber() - contaReceber.getValorDescontoInstituicao());
				}else{
					valorBaseContareceber = Uteis.getDoubleFormatado(contaReceber.getValor());
					valorContareceberMenosDecontoInstituicao = Uteis.getDoubleFormatado(contaReceber.getValor() - contaReceber.getValorDescontoInstituicao());
				}
				// String totalDesconto =
				// Uteis.getDoubleFormatado(contaReceber.getValorDescontoConvenio()
				// + valorDescontoConvertidoReais +
				// contaReceber.getValorDescontoInstituicao() +
				// contaReceber.getValorDescontoProgressivo());
				String totalDesconto = Uteis.getDoubleFormatado(contaReceber.getValorTotalDescontoContaReceber());
				if (totalDesconto == null || totalDesconto.equals("")) {
					totalDesconto = emBranco;
				}
				// String total =
				// Uteis.getDoubleFormatado(contaReceber.getValor() -
				// contaReceber.getValorDesconto() -
				// contaReceber.getValorDescontoConvenio() -
				// contaReceber.getValorDescontoInstituicao());
				// String total =
				// Uteis.getDoubleFormatado(contaReceber.getValor() -
				// (contaReceber.getValorDescontoConvenio() +
				// valorDescontoConvertidoReais +
				// contaReceber.getValorDescontoInstituicao() +
				// contaReceber.getValorDescontoProgressivo()));
				String total = Uteis.getDoubleFormatado(contaReceber.getValor() - contaReceber.getValorTotalDescontoContaReceberContrato().doubleValue());
				if (total == null || total.equals("")) {
					total = emBranco;
				}
				String valorDescontoCalculadoPrimeiraFaixaDescontos = Uteis.getDoubleFormatado(contaReceber.getValorDescontoCalculadoPrimeiraFaixaDescontos());
				if (valorDescontoCalculadoPrimeiraFaixaDescontos == null || valorDescontoCalculadoPrimeiraFaixaDescontos.equals("")) {
					valorDescontoCalculadoPrimeiraFaixaDescontos = emBranco;
				}
				String valorDescontoCalculadoSegundaFaixaDescontos = Uteis.getDoubleFormatado(contaReceber.getValorDescontoCalculadoSegundaFaixaDescontos());
				if (valorDescontoCalculadoSegundaFaixaDescontos == null || valorDescontoCalculadoSegundaFaixaDescontos.equals("")) {
					valorDescontoCalculadoSegundaFaixaDescontos = emBranco;
				}
				String valorDescontoCalculadoTerceiraFaixaDescontos = Uteis.getDoubleFormatado(contaReceber.getValorDescontoCalculadoTerceiraFaixaDescontos());
				if (valorDescontoCalculadoTerceiraFaixaDescontos == null || valorDescontoCalculadoTerceiraFaixaDescontos.equals("")) {
					valorDescontoCalculadoTerceiraFaixaDescontos = emBranco;
				}
				String valorDescontoCalculadoQuartaFaixaDescontos = Uteis.getDoubleFormatado(contaReceber.getValorDescontoCalculadoQuartaFaixaDescontos());
				if (valorDescontoCalculadoQuartaFaixaDescontos == null || valorDescontoCalculadoQuartaFaixaDescontos.equals("")) {
					valorDescontoCalculadoQuartaFaixaDescontos = emBranco;
				}
				tagGeral = marcador.getTag();

				while ((texto.indexOf(tagGeral) != -1) && !tagGeral.equals("")) {
					int tamanho = obterTamanhoTag(tagGeral);
					String textoTag = obterTextoTag(tagGeral);
					String mar = obterTagSemTextoSemTamanhoQuandoConcatenadaAOutraTag(tagGeral);

					if (mar.contains("Codigo_ContaReceber")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, codigo, textoTag, tamanho);
						// } else if (mar.contains("NrDocumento_ContaReceber"))
						// {
						// texto =
						// substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar,
						// nrDocumento, textoTag, tamanho);
					} else if (mar.contains("Parcela_ContaReceber")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, parcela, textoTag, tamanho);
					} else if (mar.contains("DataVcto_ContaReceber")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, dataVcto, textoTag, tamanho);
					} else if (mar.contains("DescricaoPagamento_ContaReceber")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, descricao, textoTag, tamanho);
						// } else if (mar.contains("TipoOrigem_ContaReceber")) {
						// texto =
						// substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar,
						// tipoOrigem, textoTag, tamanho);
					} else if (mar.contains("Valor_ContaReceber")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, valor, textoTag, tamanho);
					} else if (mar.contains("ValorDivididoPorDois_ContaReceber")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, extensoValorDivididoPorDois.toString(), textoTag, tamanho);
					} else if (mar.contains("ValorDivididoPorTres_ContaReceber")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, extensoValorDivididoPorTres.toString().toString(), textoTag, tamanho);
					} else if (mar.contains("ValorDesconto_ContaReceber")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, valorDesconto, textoTag, tamanho);
					} else if (mar.contains("DescontoInstituicao_ContaReceber")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, descontoInstituicao, textoTag, tamanho);
					} else if (mar.contains("DescontoProgressivo_ContaReceber")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, descontoProgressivo, textoTag, tamanho);
					} else if (mar.contains("DescontoConvenio_ContaReceber")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, descontoConvenio, textoTag, tamanho);
					} else if (mar.contains("ValorCusteado_ContaReceber")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, valorCusteadoContaReceber, textoTag, tamanho);
					} else if (mar.contains("ValorBaseContareceber_ContaReceber")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, valorBaseContareceber, textoTag, tamanho);						
					} else if (mar.contains("ValorContareceberMenosDecontoInstituicao_ContaReceber")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, valorContareceberMenosDecontoInstituicao, textoTag, tamanho);						
					} else if (mar.contains("Total_ContaReceber")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, total, textoTag, tamanho);
					} else if (mar.contains("TotalDesconto_ContaReceber")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, totalDesconto, textoTag, tamanho);
					} else if (mar.contains("DataVencimento_ContaReceber")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, dataVcto, textoTag, tamanho);
					} else if (mar.contains("ValorDescontoCalculadoPrimeiraFaixaDescontos_ContaReceber")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, valorDescontoCalculadoPrimeiraFaixaDescontos, textoTag, tamanho);
					} else if (mar.contains("ValorDescontoCalculadoSegundaFaixaDescontos_ContaReceber")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, valorDescontoCalculadoSegundaFaixaDescontos, textoTag, tamanho);
					} else if (mar.contains("ValorDescontoCalculadoTerceiraFaixaDescontos_ContaReceber")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, valorDescontoCalculadoTerceiraFaixaDescontos, textoTag, tamanho);
					} else if (mar.contains("ValorDescontoCalculadoQuartaFaixaDescontos_ContaReceber")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, valorDescontoCalculadoQuartaFaixaDescontos, textoTag, tamanho);
					}
					break;
				}
			}
			texto = retirarLinhasHtml(texto, marcador.getTag());
		}
		return texto;
	}

	public String substituirTagDisciplinasMatricula(String texto, MatriculaPeriodoVO matriPer, List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinaMatricula, MarcadorVO marcador) throws Exception {
		String emBranco = obterStringEmBrancoParaTag(marcador);
		tagGeral = marcador.getTag();

		if (listaDisciplinaMatricula.size() == 0) {
			texto = texto.replace(marcador.getTag(), emBranco);
		} else {
			Iterator<MatriculaPeriodoTurmaDisciplinaVO> i = listaDisciplinaMatricula.iterator();
			while (i.hasNext()) {
				texto = gerarLinhasHtml(texto, marcador);
				MatriculaPeriodoTurmaDisciplinaVO matriPerTurmaDisciplina = (MatriculaPeriodoTurmaDisciplinaVO) i.next();

				String nome = matriPerTurmaDisciplina.getDisciplina().getNome();
				if (nome.equals("")) {
					nome = emBranco;
				}
				String cargaHoraria = matriPerTurmaDisciplina.getGradeDisciplinaVO().getCargaHoraria().toString();
				if (cargaHoraria.equals("")) {
					cargaHoraria = emBranco;
				}
				String ano = matriPerTurmaDisciplina.getAno().toString();
				if (ano.equals("")) {
					ano = emBranco;
				}
				String semestre = matriPerTurmaDisciplina.getSemestre().toString();
				if (semestre.equals("")) {
					semestre = emBranco;
				}
				String periodo = matriPer.getPeridoLetivo().getPeriodoLetivo().toString();
				if (periodo.equals("")) {
					periodo = emBranco;
				}
				String descricaoPeriodo = matriPer.getPeridoLetivo().getDescricao().toString();
				if (descricaoPeriodo.equals("")) {
					descricaoPeriodo = emBranco;
				}
				tagGeral = marcador.getTag();

				while ((texto.indexOf(tagGeral) != -1) && !tagGeral.equals("")) {
					int tamanho = obterTamanhoTag(tagGeral);
					String textoTag = obterTextoTag(tagGeral);
					String mar = obterTagSemTextoSemTamanhoQuandoConcatenadaAOutraTag(tagGeral);

					if (mar.contains("Nome_Disciplina")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, nome, textoTag, tamanho);
					} else if (mar.contains("CargaHoraria_Disciplina")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, cargaHoraria, textoTag, tamanho);
					} else if (mar.contains("Ano_Disciplina")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, ano, textoTag, tamanho);
					} else if (mar.contains("Semestre_Disciplina")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, semestre, textoTag, tamanho);
					} else if (mar.contains("Periodo_Disciplina")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, periodo, textoTag, tamanho);
					} else if (mar.contains("DescricaoPeriodoLet_Disciplina")) {
						texto = substituirTagQuandoConcatenadaAOutrasTAGs(texto, mar, descricaoPeriodo, textoTag, tamanho);
					}
					break;
				}
			}
			texto = retirarLinhasHtml(texto, marcador.getTag());
		}
		return texto;
	}

	public String obterAnoSemestrePrevisaoConclusao(MatriculaPeriodoVO matriculaPeriodo) {
		try {
			int ano = Integer.valueOf(matriculaPeriodo.getAno());
			String semestre = matriculaPeriodo.getSemestre();
			int nrPeriodosLetivos = matriculaPeriodo.getGradeCurricular().getPeriodoLetivosVOs().size();
			int nrPeriodoLetivoAtual = matriculaPeriodo.getPeriodoLetivo().getPeriodoLetivo();
			int nrPeriodosLetivosFaltamParaConcluir = nrPeriodosLetivos - nrPeriodoLetivoAtual;
			if (nrPeriodosLetivosFaltamParaConcluir == 0) {
				return ano + "/" + semestre;
			}
			while (nrPeriodosLetivosFaltamParaConcluir > 0) {
				if (semestre.equals("1")) {
					semestre = "2";
				} else {
					semestre = "1";
					ano++;
				}
				nrPeriodosLetivosFaltamParaConcluir--;
			}
			return ano + "/" + semestre;
		} catch (Exception e) {

		}
		return "";
	}
	
	public String substituirTagMatricula(String texto, ImpressaoContratoVO impressaoContratoVO, MarcadorVO marcador) throws Exception {
		MatriculaVO matricula = impressaoContratoVO.getMatriculaVO();
		MatriculaPeriodoVO matriculaPeriodo = impressaoContratoVO.getMatriculaPeriodoVO();
		MatriculaEnadeVO matriculaEnadeDataVO = impressaoContratoVO.getMatriculaEnadeDataVO();

		String emBranco = obterStringEmBrancoParaTag(marcador);
		String m = matricula.getMatricula();
		if (m == null || m.equals("0") || m.equals("")) {
			m = emBranco;
		}
		String observacaoDiploma = matricula.getObservacaoDiploma().trim();
		if (observacaoDiploma == null || observacaoDiploma.equals("")) {
			observacaoDiploma = matriculaPeriodo.getExpedicaoDiplomaVO().getObservacao();
			if (observacaoDiploma == null || observacaoDiploma.equals("")) {
				observacaoDiploma = impressaoContratoVO.getExpedicaoDiplomaVO().getObservacao();
			}
		}
		if (observacaoDiploma == null || observacaoDiploma.equals("")) {
			observacaoDiploma = emBranco;
		}

		String dataExtenso = Uteis.getDataCidadeDiaMesPorExtensoEAno(matricula.getUnidadeEnsino().getCidade().getNome(), matricula.getData(), false);
		if (dataExtenso == null || dataExtenso.equals("0") || dataExtenso.equals("")) {
			dataExtenso = emBranco;
		}
		String dataExtenso2 = Uteis.getDataCidadeDiaPorExtensoMesPorExtensoEAnoPorExtenso(matricula.getUnidadeEnsino().getCidade().getNome(), matricula.getData(), false);
		if (dataExtenso2 == null || dataExtenso2.equals("0") || dataExtenso2.equals("")) {
			dataExtenso2 = emBranco;
		}
		String anoSemestrePrevisaoTerminoCurso_Matricula = obterAnoSemestrePrevisaoConclusao(matriculaPeriodo);
		if (anoSemestrePrevisaoTerminoCurso_Matricula == null || anoSemestrePrevisaoTerminoCurso_Matricula.equals("0") || anoSemestrePrevisaoTerminoCurso_Matricula.equals("")) {
			anoSemestrePrevisaoTerminoCurso_Matricula = emBranco;
		}

		String dataMatriPeriodoExtenso = Uteis.getDataCidadeDiaMesPorExtensoEAno(matricula.getUnidadeEnsino().getCidade().getNome(), matriculaPeriodo.getData(), false);
		if (dataMatriPeriodoExtenso == null || dataMatriPeriodoExtenso.equals("0") || dataMatriPeriodoExtenso.equals("")) {
			dataMatriPeriodoExtenso = emBranco;
		}
		String dataMatriPeriodoExtenso2 = Uteis.getDataCidadeDiaPorExtensoMesPorExtensoEAnoPorExtenso(matricula.getUnidadeEnsino().getCidade().getNome(), matriculaPeriodo.getData(), false);
		if (dataMatriPeriodoExtenso2 == null || dataMatriPeriodoExtenso2.equals("0") || dataMatriPeriodoExtenso2.equals("")) {
			dataMatriPeriodoExtenso2 = emBranco;
		}
		String data = matricula.getData_Apresentar();
		if (data == null || data.equals("0") || data.equals("")) {
			data = emBranco;
		}
		String dataMatriPeriodo = matriculaPeriodo.getData_Apresentar();
		if (dataMatriPeriodo == null || dataMatriPeriodo.equals("0") || dataMatriPeriodo.equals("")) {
			dataMatriPeriodo = emBranco;
		}
		String turno = matricula.getTurno().getNome();
		if (turno == null || turno.equals("0") || turno.equals("")) {
			turno = emBranco;
		}
		String descricaoturnocontrato = matricula.getTurno().getDescricaoTurnoContrato();
		if (descricaoturnocontrato == null || descricaoturnocontrato.equals("0") || descricaoturnocontrato.equals("")) {
			descricaoturnocontrato = emBranco;
		}
		String financeiamentoEstud = matricula.getFinanciamentoEstudantil();

		if (financeiamentoEstud == null || financeiamentoEstud.equals("0") || financeiamentoEstud.equals("")) {
			financeiamentoEstud = emBranco;
		}
		String turma = matriculaPeriodo.getTurma().getIdentificadorTurma();
		if (turma == null || turma.equals("0") || turma.equals("")) {
			turma = emBranco;
		}
		String turmaObservacao = matriculaPeriodo.getTurma().getObservacao();
		if (turmaObservacao == null || turmaObservacao.equals("0") || turmaObservacao.equals("")) {
			turmaObservacao = emBranco;
		}

		String nrPeriodoLetivoMatriculaPeriodo = matriculaPeriodo.getNrPeriodoLetivo().toString();
		if (nrPeriodoLetivoMatriculaPeriodo == null || nrPeriodoLetivoMatriculaPeriodo.equals("")) {
			nrPeriodoLetivoMatriculaPeriodo = emBranco;
		}

		ValorExtensoOrdinal extenso = new ValorExtensoOrdinal(matriculaPeriodo.getNrPeriodoLetivo().toString());
		String nrOrdinarioPeriodoLetivoMatriculaPeriodo = extenso.resultado();
		if (nrOrdinarioPeriodoLetivoMatriculaPeriodo == null || nrOrdinarioPeriodoLetivoMatriculaPeriodo.equals("")) {
			nrOrdinarioPeriodoLetivoMatriculaPeriodo = emBranco;
		}

		String horarioTurno = impressaoContratoVO.getMenorHorarioAluno() + " às " + impressaoContratoVO.getMaiorHorarioAluno();
		if (horarioTurno == null || horarioTurno.equals("") || (!Uteis.isAtributoPreenchido(impressaoContratoVO.getMenorHorarioAluno()) && !Uteis.isAtributoPreenchido(impressaoContratoVO.getMaiorHorarioAluno()))) {
			horarioTurno = emBranco;
		}
		String dataColacaoGrau = matricula.getDataColacaoGrau_Apresentar();
		if (dataColacaoGrau == null || dataColacaoGrau.equals("")) {
			dataColacaoGrau = emBranco;
		}
		String dataExtensoColacaoGrau = Uteis.getDataCidadeDiaMesPorExtensoEAno("", matricula.getDataColacaoGrau(), true);
		if (dataExtensoColacaoGrau == null || dataExtensoColacaoGrau.equals("")) {
			dataExtensoColacaoGrau = emBranco;
		}
		String dataExtensoColacaoGrau2 = Uteis.getDataCidadeDiaPorExtensoMesPorExtensoEAnoPorExtenso("", matricula.getDataColacaoGrau(), true);
		if (dataExtensoColacaoGrau2 == null || dataExtensoColacaoGrau2.equals("")) {
			dataExtensoColacaoGrau2 = emBranco;
		}
		String anoTrancamento = impressaoContratoVO.getMatriculaPeriodoVO().getAno();
		if (anoTrancamento == null || anoTrancamento.equals("")) {
			anoTrancamento = emBranco;
		}
		String semestreTrancamento = impressaoContratoVO.getMatriculaPeriodoVO().getSemestre();
		semestreTrancamento += "º Semestre";
		if (semestreTrancamento == null || semestreTrancamento.equals("")) {
			semestreTrancamento = emBranco;
		}
		String dataRetornoTrancamento = impressaoContratoVO.getTrancamentoVO().getDataPossivelRetorno_Apresentar();
		if (dataRetornoTrancamento == null || dataRetornoTrancamento.equals("")) {
			dataRetornoTrancamento = emBranco;
		}
		String dataRetornoTrancamentoExtenso = Uteis.getDataCidadeDiaMesPorExtensoEAno(matricula.getUnidadeEnsino().getCidade().getNome(), impressaoContratoVO.getTrancamentoVO().getDataPossivelRetorno(), false);
		if (dataRetornoTrancamentoExtenso == null || dataRetornoTrancamentoExtenso.equals("0") || dataRetornoTrancamentoExtenso.equals("")) {
			dataRetornoTrancamentoExtenso = emBranco;
		}
		String dataRetornoTrancamentoExtenso2 = Uteis.getDataCidadeDiaPorExtensoMesPorExtensoEAnoPorExtenso(matricula.getUnidadeEnsino().getCidade().getNome(), impressaoContratoVO.getTrancamentoVO().getDataPossivelRetorno(), false);
		if (dataRetornoTrancamentoExtenso2 == null || dataRetornoTrancamentoExtenso2.equals("0") || dataRetornoTrancamentoExtenso2.equals("")) {
			dataRetornoTrancamentoExtenso2 = emBranco;
		}
		String responsavel = matriculaPeriodo.getResponsavelRenovacaoMatricula().getNome();
		if (responsavel == null || responsavel.equals("0") || responsavel.equals("")) {
			responsavel = emBranco;
		}

		String consultor = matricula.getConsultor().getPessoa().getNome();
		if (consultor == null || consultor.equals("0") || consultor.equals("")) {
			consultor = emBranco;
		}
		String rgConsultor = matricula.getConsultor().getPessoa().getRG();
		if (rgConsultor == null || rgConsultor.equals("0") || rgConsultor.equals("")) {
			rgConsultor = emBranco;
		}

		String horaInicioTurno = "";
		String horaFinalTurno = "";
		if (matricula.getTurno().getIsExisteHorarioSegunda()) {
			horaInicioTurno = matricula.getTurno().getTurnoHorarioSegunda().get(0).getHorarioInicioAula();
			horaFinalTurno = matricula.getTurno().getTurnoHorarioSegunda().get(matricula.getTurno().getTurnoHorarioSegunda().size() - 1).getHorarioFinalAula();
		} else if (matricula.getTurno().getIsExisteHorarioTerca()) {
			horaInicioTurno = matricula.getTurno().getTurnoHorarioTerca().get(0).getHorarioInicioAula();
			horaFinalTurno = matricula.getTurno().getTurnoHorarioTerca().get(matricula.getTurno().getTurnoHorarioTerca().size() - 1).getHorarioFinalAula();
		} else if (matricula.getTurno().getIsExisteHorarioQuarta()) {
			horaInicioTurno = matricula.getTurno().getTurnoHorarioQuarta().get(0).getHorarioInicioAula();
			horaFinalTurno = matricula.getTurno().getTurnoHorarioQuarta().get(matricula.getTurno().getTurnoHorarioQuarta().size() - 1).getHorarioFinalAula();
		} else if (matricula.getTurno().getIsExisteHorarioQuinta()) {
			horaInicioTurno = matricula.getTurno().getTurnoHorarioQuinta().get(0).getHorarioInicioAula();
			horaFinalTurno = matricula.getTurno().getTurnoHorarioQuinta().get(matricula.getTurno().getTurnoHorarioQuinta().size() - 1).getHorarioFinalAula();
		} else if (matricula.getTurno().getIsExisteHorarioSexta()) {
			horaInicioTurno = matricula.getTurno().getTurnoHorarioSexta().get(0).getHorarioInicioAula();
			horaFinalTurno = matricula.getTurno().getTurnoHorarioSexta().get(matricula.getTurno().getTurnoHorarioSexta().size() - 1).getHorarioFinalAula();
		} else if (matricula.getTurno().getIsExisteHorarioSabado()) {
			horaInicioTurno = matricula.getTurno().getTurnoHorarioSabado().get(0).getHorarioInicioAula();
			horaFinalTurno = matricula.getTurno().getTurnoHorarioSabado().get(matricula.getTurno().getTurnoHorarioSabado().size() - 1).getHorarioFinalAula();
		} else if (matricula.getTurno().getIsExisteHorarioDomingo()) {
			horaInicioTurno = matricula.getTurno().getTurnoHorarioDomingo().get(0).getHorarioInicioAula();
			horaFinalTurno = matricula.getTurno().getTurnoHorarioDomingo().get(matricula.getTurno().getTurnoHorarioDomingo().size() - 1).getHorarioFinalAula();
		}
		if (horaInicioTurno == null || horaInicioTurno.equals("")) {
			horaInicioTurno = emBranco;
		}
		if (horaFinalTurno == null || horaFinalTurno.equals("")) {
			horaFinalTurno = emBranco;
		}
		String matriculaOrigem = impressaoContratoVO.getMatriculaOrigem().getMatricula();
		if (matriculaOrigem == null || matriculaOrigem.equals("0") || matriculaOrigem.equals("")) {
			matriculaOrigem = emBranco;
		}
		String matriculaDestino = impressaoContratoVO.getMatriculaDestino().getMatricula();
		if (matriculaDestino == null || matriculaDestino.equals("0") || matriculaDestino.equals("")) {
			matriculaDestino = emBranco;
		}
		String turmaOrigem = impressaoContratoVO.getTurmaOrigem().getIdentificadorTurma();
		if (turmaOrigem == null || turmaOrigem.equals("0") || turmaOrigem.equals("")) {
			turmaOrigem = emBranco;
		}
		String turmaDestino = impressaoContratoVO.getTurmaDestino().getIdentificadorTurma();
		if (turmaDestino == null || turmaDestino.equals("0") || turmaDestino.equals("")) {
			turmaDestino = emBranco;
		}
		String turnoOrigem = impressaoContratoVO.getMatriculaOrigem().getTurno().getNome();
		if (turnoOrigem == null || turnoOrigem.equals("0") || turnoOrigem.equals("")) {
			turnoOrigem = emBranco;
		}
		String turnoDestino = impressaoContratoVO.getMatriculaDestino().getTurno().getNome();
		if (turnoDestino == null || turnoDestino.equals("0") || turnoDestino.equals("")) {
			turnoDestino = emBranco;
		}
		String situacaoMatricula = matricula.getSituacao_Apresentar();
		if (situacaoMatricula == null || situacaoMatricula.equals("0") || situacaoMatricula.equals("")) {
			situacaoMatricula = emBranco;
		}
		String situacaoMatriculaPeriodo = matriculaPeriodo.getSituacaoMatriculaPeriodo_Apresentar();
		if (situacaoMatriculaPeriodo == null || situacaoMatriculaPeriodo.equals("0") || situacaoMatriculaPeriodo.equals("")) {
			situacaoMatriculaPeriodo = emBranco;
		}

		String situacaoFinalPeriodoLetivoMatricula = impressaoContratoVO.getSituacaoFinalPeriodoLetivo();
		if (situacaoFinalPeriodoLetivoMatricula == null || situacaoFinalPeriodoLetivoMatricula.equals("")) {
			situacaoFinalPeriodoLetivoMatricula = emBranco;
		}

		Boolean autoDeclaracaoPretoPardoIndigena = matricula.getAutodeclaracaoPretoPardoIndigena();
		String autoDeclaracaoPretoPardoIndigena_Str = "Não";
		if (autoDeclaracaoPretoPardoIndigena) {
			autoDeclaracaoPretoPardoIndigena_Str = "Sim";
		}
		Boolean bolsasAuxilios = matricula.getBolsasAuxilios();
		String bolsasAuxilios_Str = "Não";
		if (bolsasAuxilios) {
			bolsasAuxilios_Str = "Sim";
		}
		Boolean escolaPublica = matricula.getEscolaPublica();
		String escolaPublica_Str = "Não";
		if (escolaPublica) {
			escolaPublica_Str = "Sim";
		}
		
		getListaAcompanhamentoFinanceiroMatricula().clear();
		MatriculaPeriodoVencimentoVO mpvTemp = null;
		String tabelaAcompFin = "";

		int cont = 0;
		for (MatriculaPeriodoVencimentoVO mpv : matriculaPeriodo.getMatriculaPeriodoVencimentoVOs()) {
			if (cont == 0) {
				tabelaAcompFin = "<table style=\"width:100%\" border=1 cellspacing=0 cellpadding=1 bordercolor=\"000000\"><tr><th>Parcela    </th><th>Data Vcto    </th><th>Valor    </th></tr>";
				cont++;
			} else {
				if (mpv.getContaReceber().getCodigo().intValue() > 0) {
					tabelaAcompFin += " <tr  align=center ><td> " + mpv.getContaReceber().getParcela() + " </td><td> " + mpv.getContaReceber().getDataVencimento_Apresentar() + " </td><td align='center'> R$ " + Uteis.getDoubleFormatado(mpv.getContaReceber().getValorBaseContaReceber()) + " </td></tr>";
					mpvTemp = new MatriculaPeriodoVencimentoVO();
					mpvTemp.setParcela(mpv.getContaReceber().getParcela());
					mpvTemp.setDataVencimento(mpv.getContaReceber().getDataVencimento());
					mpvTemp.setValor(mpv.getContaReceber().getValorBaseContaReceber());
					mpvTemp.setValorDescontoInstituicao(mpv.getContaReceber().getValorDescontoInstituicao());
					mpvTemp.setValorDescontoConvenio(mpv.getContaReceber().getValorDescontoConvenio());
					mpvTemp.setValorDesconto(mpv.getContaReceber().getValorDesconto());
					getListaAcompanhamentoFinanceiroMatricula().add(mpvTemp);
				} else {
					tabelaAcompFin += " <tr  align=center><td> " + mpv.getParcela() + " </td><td> " + mpv.getDataVencimento_Apresentar() + " </td><td align='center'> R$ " + Uteis.getDoubleFormatado(mpv.getValor()) + " </td></tr>";
					getListaAcompanhamentoFinanceiroMatricula().add(mpv);
				}
			}
		}
		tabelaAcompFin += "</table>";
		
		String dataAceitouTermoContratoRenovacaoOnline = Uteis.getDataComHora(matriculaPeriodo.getDataAceitouTermoContratoRenovacaoOnline());
		if (dataAceitouTermoContratoRenovacaoOnline == null || dataAceitouTermoContratoRenovacaoOnline.equals("")) {
			dataAceitouTermoContratoRenovacaoOnline = emBranco;
		}
				
		String dataAceitouTermoContratoRenovacaoOnlineExtenso = Uteis.getDataCidadeDiaMesPorExtensoEAno(matricula.getUnidadeEnsino().getCidade().getNome(), matriculaPeriodo.getDataAceitouTermoContratoRenovacaoOnline(), false);
		if (dataAceitouTermoContratoRenovacaoOnlineExtenso == null || dataAceitouTermoContratoRenovacaoOnlineExtenso.equals("0") || dataAceitouTermoContratoRenovacaoOnlineExtenso.equals("")) {
			dataAceitouTermoContratoRenovacaoOnlineExtenso = emBranco;
		}
		
		String numeroProcessoExpedicaoDiploma = Uteis.isAtributoPreenchido(matriculaPeriodo.getExpedicaoDiplomaVO().getNumeroProcesso()) ? 
					matriculaPeriodo.getExpedicaoDiplomaVO().getNumeroProcesso() : impressaoContratoVO.getExpedicaoDiplomaVO().getNumeroProcesso();

		if (numeroProcessoExpedicaoDiploma == null || numeroProcessoExpedicaoDiploma.equals("")) {
			numeroProcessoExpedicaoDiploma = emBranco;
		}
				
		String dataExpedicaoDiploma = "";
		if (Uteis.isAtributoPreenchido(matriculaPeriodo.getExpedicaoDiplomaVO().getCodigo())) {
			dataExpedicaoDiploma = Uteis.getData(matriculaPeriodo.getExpedicaoDiplomaVO().getDataExpedicao() , "dd/MM/yyyy");
		} else {
			dataExpedicaoDiploma = Uteis.getData(impressaoContratoVO.getExpedicaoDiplomaVO().getDataExpedicao() , "dd/MM/yyyy");
		}
		
		if (dataExpedicaoDiploma == null || dataExpedicaoDiploma.equals("")) {
			dataExpedicaoDiploma = emBranco;
		}
		String semestreConclusaoCurso = matricula.getSemestreConclusao();
		if (semestreConclusaoCurso == null || semestreConclusaoCurso.equals("")) {
			semestreConclusaoCurso = emBranco;
		}
		String codigoIntegracaoFinanceiroMatricula_Matricula = matricula.getCodigoFinanceiroMatricula();
		if(!Uteis.isAtributoPreenchido(codigoIntegracaoFinanceiroMatricula_Matricula)) {
			codigoIntegracaoFinanceiroMatricula_Matricula = emBranco;
		}
		
		String mediaGlobalMatricula = impressaoContratoVO.getMediaGlobal().toString();
		if (mediaGlobalMatricula == null || mediaGlobalMatricula.equals("")) {
			mediaGlobalMatricula = emBranco;
		}
		
		String serialExpedicaoDiploma = Uteis.isAtributoPreenchido(matriculaPeriodo.getExpedicaoDiplomaVO().getSerial()) ? 
				matriculaPeriodo.getExpedicaoDiplomaVO().getSerial() : impressaoContratoVO.getExpedicaoDiplomaVO().getSerial();
		if (serialExpedicaoDiploma == null || serialExpedicaoDiploma.equals("")) {
			serialExpedicaoDiploma = emBranco;
		}
		
		String numeroPeriodoLetivoIngresso = null, numeroExtensoPeriodoLetivoIngresso = null, descricaoPeriodoLetivoIngresso = null, nomeCertificacaoPeriodoLetivoIngresso = null;
		if (Uteis.isAtributoPreenchido(impressaoContratoVO.getPeriodoLetivoIngresso())) {
			numeroPeriodoLetivoIngresso = impressaoContratoVO.getPeriodoLetivoIngresso().getPeriodoLetivo().toString();
			numeroExtensoPeriodoLetivoIngresso = Uteis.converterInteiroParaOrdinal(impressaoContratoVO.getPeriodoLetivoIngresso().getPeriodoLetivo());
			descricaoPeriodoLetivoIngresso = impressaoContratoVO.getPeriodoLetivoIngresso().getDescricao();
			nomeCertificacaoPeriodoLetivoIngresso = impressaoContratoVO.getPeriodoLetivoIngresso().getNomeCertificacao();
		}
		if (numeroPeriodoLetivoIngresso == null || numeroPeriodoLetivoIngresso.equals("0") || numeroPeriodoLetivoIngresso.equals("")) {
			numeroPeriodoLetivoIngresso = emBranco;
		}
		if (numeroExtensoPeriodoLetivoIngresso == null || numeroExtensoPeriodoLetivoIngresso.equals("0") || numeroExtensoPeriodoLetivoIngresso.equals("")) {
			numeroExtensoPeriodoLetivoIngresso = emBranco;
		}
		if (descricaoPeriodoLetivoIngresso == null || descricaoPeriodoLetivoIngresso.equals("0") || descricaoPeriodoLetivoIngresso.equals("")) {
			descricaoPeriodoLetivoIngresso = emBranco;
		}
		if (nomeCertificacaoPeriodoLetivoIngresso == null || nomeCertificacaoPeriodoLetivoIngresso.equals("0") || nomeCertificacaoPeriodoLetivoIngresso.equals("")) {
			nomeCertificacaoPeriodoLetivoIngresso = emBranco;
		}
		
		String matriculaEnadeData = Uteis.getData(matriculaEnadeDataVO.getDataEnade(), "dd/MM/yyyy");
		if (matriculaEnadeData == null || matriculaEnadeData.equals("")) {
			matriculaEnadeData = emBranco;
		}
		
		String dataTrancamento = Uteis.getData(impressaoContratoVO.getTrancamentoVO().getData(), "dd/MM/yyyy");
		String dataTrancamentoExtenso = Uteis.getDataCidadeDiaMesPorExtensoEAno("", impressaoContratoVO.getTrancamentoVO().getData(), false);

		if (dataTrancamento == null || dataTrancamento.equals("") 
				|| dataTrancamentoExtenso == null || dataTrancamentoExtenso.equals("")) {
			dataTrancamento = emBranco;
			dataTrancamentoExtenso = emBranco;
		}
		
		String dataCancelamento = Uteis.getData(impressaoContratoVO.getCancelamentoVO().getData(), "dd/MM/yyyy");
		String dataCancelamentoExtenso = Uteis.getDataCidadeDiaMesPorExtensoEAno("", impressaoContratoVO.getCancelamentoVO().getData(), false);

		if (dataCancelamento == null || dataCancelamento.equals("") || impressaoContratoVO.getCancelamentoVO().getCodigo().equals(0)
				|| dataCancelamentoExtenso == null || dataCancelamentoExtenso.equals("")) {
			dataCancelamento = emBranco;
			dataCancelamentoExtenso = emBranco;
		}
		
		String dataTransferenciaEntrada = Uteis.getData(impressaoContratoVO.getTransferenciaEntradaVO().getData(), "dd/MM/yyyy");
		String dataTransferenciaEntradaExtenso = Uteis.getDataCidadeDiaMesPorExtensoEAno("", impressaoContratoVO.getTransferenciaEntradaVO().getData(), false);

		if (dataTransferenciaEntrada == null || dataTransferenciaEntrada.equals("")
				|| dataTransferenciaEntradaExtenso == null || dataTransferenciaEntradaExtenso.equals("")) {
			dataTransferenciaEntrada = emBranco;
			dataTransferenciaEntradaExtenso = emBranco;
		}
		
		String diaSemanaAulaDescricao = impressaoContratoVO.getMatriculaVO().getDiaSemanaAula().getDescricao();
		String diaSemanaAulaAbreviatura = DiaSemana.getAbreviatura(impressaoContratoVO.getMatriculaVO().getDiaSemanaAula().getValor());
		String diaSemanaAulaNumeral = DiaSemana.getAbreviatura(impressaoContratoVO.getMatriculaVO().getDiaSemanaAula().getNumeral());
		
		String turnoAulaDescricao = impressaoContratoVO.getMatriculaVO().getTurnoAula().getValorApresentar();
		String turnoAulaAbreviatura = impressaoContratoVO.getMatriculaVO().getTurnoAula().getSigla();
		String turnoAulaInicio = impressaoContratoVO.getMatriculaVO().getTurnoAula().getHorarioInicio();
		String turnoAulaTermino = impressaoContratoVO.getMatriculaVO().getTurnoAula().getHorarioTermino();
		
		while (texto.indexOf(marcador.getTag()) != -1) {
			int tamanho = obterTamanhoTag(marcador.getTag());
			String mar = obterTagSemTextoSemTamanho(marcador.getTag());
			String textoTag = obterTextoTag(marcador.getTag());
			//
			if (mar.equalsIgnoreCase("Matricula_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), m, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataExtenso_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), dataExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataExtenso2_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), dataExtenso2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("AnoSemestrePrevisaoTerminoCurso_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), anoSemestrePrevisaoTerminoCurso_Matricula, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataMatriculaPeriodoExtenso_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), dataMatriPeriodoExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataMatriculaPeriodoExtenso2_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), dataMatriPeriodoExtenso2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Data_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), data, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataMatriculaPeriodo_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), dataMatriPeriodo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Turno_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), turno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TurnoDescricaoContrato_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), descricaoturnocontrato, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TurnoHoraInicio_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), horaInicioTurno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TurnoHoraFinal_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), horaFinalTurno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Turma_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), turma, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TurmaObservacao_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), turmaObservacao, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FinanciamentoEstudantil_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), financeiamentoEstud, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NrPeriodoLetivo_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), nrPeriodoLetivoMatriculaPeriodo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("HorarioTurno_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), horarioTurno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataColacaoGrau_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), dataColacaoGrau, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataRetornoTrancamentoExtenso2_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), dataRetornoTrancamentoExtenso2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("AnoTrancamento_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), anoTrancamento, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("SemestreTrancamento_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), semestreTrancamento, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataRetornoTrancamento_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), dataRetornoTrancamento, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataRetornoTrancamentoExtenso_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), dataRetornoTrancamentoExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataAtualExtenso2_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), Uteis.getDataCidadeDiaPorExtensoMesPorExtensoEAnoPorExtenso(matricula.getUnidadeEnsino().getCidade().getNome(), new Date(), false), textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataAtualExtenso_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), Uteis.getDataCidadeDiaMesPorExtensoEAno(matricula.getUnidadeEnsino().getCidade().getNome(), new Date(), true), textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Matricula_MatriculaOrigem")) {
				texto = substituirTag(texto, marcador.getTag(), matriculaOrigem, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Matricula_MatriculaDestino")) {
				texto = substituirTag(texto, marcador.getTag(), matriculaDestino, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TurmaOrigem_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), turmaOrigem, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TurmaDestino_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), turmaDestino, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Turno_MatriculaOrigem")) {
				texto = substituirTag(texto, marcador.getTag(), turnoOrigem, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Turno_MatriculaDestino")) {
				texto = substituirTag(texto, marcador.getTag(), turnoDestino, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NrOrdinarioPeriodoLetivo_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), nrOrdinarioPeriodoLetivoMatriculaPeriodo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataExtensoColacaoGrau_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), dataExtensoColacaoGrau, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataExtensoColacaoGrau2_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), dataExtensoColacaoGrau2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ObservacaoDiploma_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), observacaoDiploma, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Situacao_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), situacaoMatricula, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Situacao_MatriculaPeriodo")) {
				texto = substituirTag(texto, marcador.getTag(), situacaoMatriculaPeriodo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("SituacaoFinalPeriodoLetivo_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), situacaoFinalPeriodoLetivoMatricula, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("AutoDeclaracaoPretoPardoIndigena_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), autoDeclaracaoPretoPardoIndigena_Str, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("BolsasAuxilios_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), bolsasAuxilios_Str, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EscolaPublica_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), escolaPublica_Str, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Responsavel_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), responsavel, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Consultor_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), consultor, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("RgConsultor_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), rgConsultor, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TabelaAcompanhamentoFinanceiro_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), tabelaAcompFin, textoTag, tamanho);
				substituirTagComLista(texto, marcador.getTag(), getListaAcompanhamentoFinanceiroMatricula(), textoTag);
			} else if (mar.equalsIgnoreCase("DataAceitouTermoContratoRenovacaoOnline_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), dataAceitouTermoContratoRenovacaoOnline, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NumeroProcessoExpedicaoDiploma_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), numeroProcessoExpedicaoDiploma, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataExpedicaoDiploma_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), dataExpedicaoDiploma, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("SemestreConclusaoCurso_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), semestreConclusaoCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataAceitouTermoContratoRenovacaoOnlineExtenso_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), dataAceitouTermoContratoRenovacaoOnlineExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CodigoIntegracaoFinanceiroMatricula_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), codigoIntegracaoFinanceiroMatricula_Matricula, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("MediaGlobal_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), mediaGlobalMatricula, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("SerialExpedicaoDiploma_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), serialExpedicaoDiploma, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TabelaFinanceiroMatriculaMensalidades_Matricula")) {
				if (!matriculaPeriodo.getMatriculaPeriodoVencimentoVOs().isEmpty()) {
					substituirTagComLista(texto, marcador.getTag(), matriculaPeriodo.getMatriculaPeriodoVencimentoVOs(), textoTag);					
				}
			} else if (mar.equalsIgnoreCase("Nr_Periodo_Letivo_Ingresso_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), numeroPeriodoLetivoIngresso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Nr_Extenso_Periodo_Letivo_Ingresso_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), numeroExtensoPeriodoLetivoIngresso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Descricao_Periodo_Letivo_Ingresso_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), descricaoPeriodoLetivoIngresso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Nome_Certificacao_Periodo_Letivo_Ingresso_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), nomeCertificacaoPeriodoLetivoIngresso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ListaDocumentosEntregues_Matricula")) {
				substituirTagComLista(texto, marcador.getTag(), matricula.getDocumetacaoMatriculaVOs(), textoTag);
			} else if (mar.equalsIgnoreCase("MatriculaEnadeData_Matricula")) {
					texto = substituirTag(texto, marcador.getTag(), matriculaEnadeData, textoTag, tamanho);
			}else if (mar.equalsIgnoreCase("DiaSemanaAulaDescricao_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), diaSemanaAulaDescricao, textoTag, tamanho);
			}else if (mar.equalsIgnoreCase("DiaSemanaAulaAbreviatura_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), diaSemanaAulaAbreviatura, textoTag, tamanho);
			}else if (mar.equalsIgnoreCase("DiaSemanaAulaNumeral_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), diaSemanaAulaNumeral, textoTag, tamanho);
			}else if (mar.equalsIgnoreCase("TurnoAulaDescricao_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), turnoAulaDescricao, textoTag, tamanho);
			}else if (mar.equalsIgnoreCase("TurnoAulaAbreviatura_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), turnoAulaAbreviatura, textoTag, tamanho);
			}else if (mar.equalsIgnoreCase("TurnoAulaInicio_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), turnoAulaInicio, textoTag, tamanho);
			}else if (mar.equalsIgnoreCase("TurnoAulaTermino_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), turnoAulaTermino, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataCancelamento_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), dataCancelamento, textoTag, tamanho);
			} else if(mar.equalsIgnoreCase("DataCancelamentoPorExtenso_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), dataCancelamentoExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataTrancamento_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), dataTrancamento, textoTag, tamanho);
			} else if(mar.equalsIgnoreCase("DataTrancamentoPorExtenso_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), dataTrancamentoExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataTransferencia_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), dataTransferenciaEntrada, textoTag, tamanho);
			} else if(mar.equalsIgnoreCase("DataTransferenciaPorExtenso_Matricula")) {
				texto = substituirTag(texto, marcador.getTag(), dataTransferenciaEntradaExtenso, textoTag, tamanho);
			}
			

			break;
		}
		return texto;
	}

	public Double obterValorDescontoSemValidade(List<PlanoDescontoVO> listaPlanoDesconto, Double valorBase) {
		Double valorDescPlanoDesconto = 0.0;
		Double valorDescAplicar = 0.0;
		Double valorBaseTemp = valorBase;
		for (PlanoDescontoVO planoDescontoVO : listaPlanoDesconto) {
			if (!planoDescontoVO.getDescontoValidoAteDataVencimento() && !planoDescontoVO.getUtilizarDiaFixo() && !planoDescontoVO.getUtilizarDiaUtil() && planoDescontoVO.getDiasValidadeVencimento() == 0) {
				if (!planoDescontoVO.getTipoDescontoParcela().equals("PO")) {
					valorDescPlanoDesconto = valorDescPlanoDesconto + planoDescontoVO.getPercDescontoParcela();
					valorBaseTemp = valorBaseTemp - valorDescPlanoDesconto;
				} else {
					valorDescAplicar = Uteis.arrendondarForcando2CadasDecimais((planoDescontoVO.getPercDescontoParcela() / 100) * valorBaseTemp);
					valorDescPlanoDesconto = valorDescPlanoDesconto + valorDescAplicar;
					valorBaseTemp = valorBaseTemp - valorDescPlanoDesconto;
				}
			}
		}
		return valorDescPlanoDesconto;
	}

	public Double obterValorParcelaDescontoComValidade(List<PlanoDescontoVO> listaPlanoDesconto, Double valorBaseCalculadoDescontoSemValidade) {
		Double valorDescPlanoDesconto = 0.0;
		Double valorDescAplicar = 0.0;
		Double valorBaseTemp = valorBaseCalculadoDescontoSemValidade;
		for (PlanoDescontoVO planoDescontoVO : listaPlanoDesconto) {
			if (planoDescontoVO.getDescontoValidoAteDataVencimento() || planoDescontoVO.getDiasValidadeVencimento() != 0) {
				if (!planoDescontoVO.getTipoDescontoParcela().equals("PO")) {
					valorDescPlanoDesconto = valorDescPlanoDesconto + planoDescontoVO.getPercDescontoParcela();
					valorBaseTemp = valorBaseTemp - valorDescPlanoDesconto;
				} else {
					valorDescAplicar = Uteis.arrendondarForcando2CadasDecimais((planoDescontoVO.getPercDescontoParcela() / 100) * valorBaseTemp);
					valorDescPlanoDesconto = valorDescPlanoDesconto + valorDescAplicar;
					valorBaseTemp = valorBaseTemp - valorDescPlanoDesconto;
				}
			}
		}
		return valorDescPlanoDesconto;
	}

	public Double obterValorPlanoDescontoAteDataVencimento(List<PlanoDescontoVO> listaPlanoDesconto, Double valorBaseCalculadoDescontoSemValidade) {
		Double valorDescPlanoDesconto = 0.0;
		Double valorDescAplicar = 0.0;
		Double valorBaseTemp = valorBaseCalculadoDescontoSemValidade;
		for (PlanoDescontoVO planoDescontoVO : listaPlanoDesconto) {
			if (planoDescontoVO.getDescontoValidoAteDataVencimento()) {
				if (!planoDescontoVO.getTipoDescontoParcela().equals("PO")) {
					valorDescPlanoDesconto = valorDescPlanoDesconto + planoDescontoVO.getPercDescontoParcela();
					valorBaseTemp = valorBaseTemp - valorDescPlanoDesconto;
				} else {
					valorDescAplicar = Uteis.arrendondarForcando2CadasDecimais((planoDescontoVO.getPercDescontoParcela() / 100) * valorBaseTemp);
					valorDescPlanoDesconto = valorDescPlanoDesconto + valorDescAplicar;
					valorBaseTemp = valorBaseTemp - valorDescPlanoDesconto;
				}
			}
		}
		return valorDescPlanoDesconto;
	}

	public Integer obterNrDiasAntesVencimentoPlanoDescontoComValidade(List<PlanoDescontoVO> listaPlanoDesconto) {
		Integer nrDiasAntesVencimento = 0;
		Integer nrDiasTemp = 0;
		Boolean primeiraVez = Boolean.TRUE;
		for (PlanoDescontoVO planoDescontoVO : listaPlanoDesconto) {
			if (planoDescontoVO.getDescontoValidoAteDataVencimento() || planoDescontoVO.getDiasValidadeVencimento() != 0) {
				nrDiasTemp = planoDescontoVO.getDiasValidadeVencimento();
				if (primeiraVez) {
					nrDiasAntesVencimento = nrDiasTemp;
				}
				if (nrDiasAntesVencimento > nrDiasTemp) {
					nrDiasAntesVencimento = nrDiasTemp;
				}
				primeiraVez = Boolean.FALSE;
			}
		}
		return nrDiasAntesVencimento;
	}

	public String obterDescricaoPlanoDesconto(List<PlanoDescontoVO> listaPlanoDesconto) {
		String nomePlanos = "";
		int cont = 1;
		for (PlanoDescontoVO planoDescontoVO : listaPlanoDesconto) {
			nomePlanos += planoDescontoVO.getNome();
			if (cont == listaPlanoDesconto.size()) {
				nomePlanos += ", ";
			}
			cont++;
		}
		return nomePlanos;
	}

	public Double calcularPrimeiroDiaAntecipacaoConvenio(DescontoProgressivoVO descontoProgressivoConvenio, Double valorBase) {
		Double descontoProgressivoASerAplicado = 0.0;
		if (descontoProgressivoConvenio.getIsUsarValorFixo()) {
			descontoProgressivoASerAplicado = Uteis.arrendondarForcando2CadasDecimais(descontoProgressivoConvenio.getValorDescontoLimite1());
		} else {
			descontoProgressivoASerAplicado = Uteis.arrendondarForcando2CadasDecimais((descontoProgressivoConvenio.getPercDescontoLimite1() / 100) * valorBase);
		}
		return descontoProgressivoASerAplicado;
	}
	
	public String substituirTagCurso(String texto, ImpressaoContratoVO impressaoContratoVO, MarcadorVO marcador, PlanoFinanceiroAlunoVO planoFinanceiroAluno, List<PlanoDescontoVO> listaPlanoDesconto, UsuarioVO usuario) throws Exception {
		CursoVO curso = impressaoContratoVO.getMatriculaVO().getCurso();
		MatriculaPeriodoVO matriculaPeriodo = impressaoContratoVO.getMatriculaPeriodoVO();
		MatriculaVO matricula = impressaoContratoVO.getMatriculaVO();
		String emBranco = obterStringEmBrancoParaTag(marcador);
		String codigo = curso.getCodigo().toString();
		if (codigo == null || codigo.equals("0") || codigo.equals("")) {
			codigo = emBranco;
		}
		String nome = curso.getNome();
		if (nome == null || nome.equals("0") || nome.equals("")) {
			nome = emBranco;
		}
		String nomeDocenteResponsavelAssinaturaTermoEstagio = curso.getFuncionarioResponsavelAssinaturaTermoEstagioVO().getPessoa().getNome();
		if (nomeDocenteResponsavelAssinaturaTermoEstagio == null || nomeDocenteResponsavelAssinaturaTermoEstagio.equals("0") || nomeDocenteResponsavelAssinaturaTermoEstagio.equals("")) {
			nomeDocenteResponsavelAssinaturaTermoEstagio = emBranco;
		}
		
		String cpfDocenteResponsavelAssinaturaTermoEstagio = curso.getFuncionarioResponsavelAssinaturaTermoEstagioVO().getPessoa().getCPF();
		if (cpfDocenteResponsavelAssinaturaTermoEstagio == null || cpfDocenteResponsavelAssinaturaTermoEstagio.equals("0") || cpfDocenteResponsavelAssinaturaTermoEstagio.equals("")) {
			cpfDocenteResponsavelAssinaturaTermoEstagio = emBranco;
		}
		
		String emailDocenteResponsavelAssinaturaTermoEstagio = curso.getFuncionarioResponsavelAssinaturaTermoEstagioVO().getPessoa().getEmail();
		if (emailDocenteResponsavelAssinaturaTermoEstagio == null || emailDocenteResponsavelAssinaturaTermoEstagio.equals("0") || emailDocenteResponsavelAssinaturaTermoEstagio.equals("")) {
			emailDocenteResponsavelAssinaturaTermoEstagio = emBranco;
		}
		
		String publicoAlvo = curso.getPublicoAlvo();
		if (publicoAlvo == null || publicoAlvo.equals("0") || publicoAlvo.equals("")) {
			publicoAlvo = emBranco;
		}
		String nomeDocumentacao = curso.getNomeDocumentacao();
		if (nomeDocumentacao == null || nomeDocumentacao.equals("0") || nomeDocumentacao.equals("")) {
			nomeDocumentacao = emBranco;
		}
		String areaConhecimento = curso.getAreaConhecimento().getNome();
		if (areaConhecimento == null || areaConhecimento.equals("0") || areaConhecimento.equals("")) {
			areaConhecimento = emBranco;
		}
		String regime = curso.getRegime_Apresentar();
		if (regime == null || regime.equals("0") || regime.equals("")) {
			regime = emBranco;
		}
		String regimeAprov = curso.getRegimeAprovacao_Apresentar();
		if (regimeAprov == null || regimeAprov.equals("0") || regimeAprov.equals("")) {
			regimeAprov = emBranco;
		}
		String periodicidade = curso.getPeriodicidade_Apresentar();
		if (periodicidade == null || periodicidade.equals("0") || periodicidade.equals("")) {
			periodicidade = emBranco;
		}
		String tituloCurso = curso.getTitulo_Apresentar();
		if (tituloCurso == null || tituloCurso.equals("0") || tituloCurso.equals("")) {
			tituloCurso = emBranco;
		}
		String dataCriacao = curso.getDataCriacao_Apresentar();
		if (dataCriacao == null || dataCriacao.equals("0") || dataCriacao.equals("")) {
			dataCriacao = emBranco;
		}
		String nrRegistroInterno = curso.getNrRegistroInterno();
		if (nrRegistroInterno == null || nrRegistroInterno.equals("0") || nrRegistroInterno.equals("")) {
			nrRegistroInterno = emBranco;
		}
		String dataPublicacao = Uteis.getData(impressaoContratoVO.getAutorizacaoCurso().getData(), "dd/MM/yyyy");
		if (dataPublicacao == null || dataPublicacao.equals("0") || dataPublicacao.equals("")) {
			dataPublicacao = emBranco;
		}
		String nrPeriodoLetivo = curso.getNrPeriodoLetivo().toString();
		if (nrPeriodoLetivo == null || nrPeriodoLetivo.equals("0") || nrPeriodoLetivo.equals("")) {
			nrPeriodoLetivo = emBranco;
		}
		
		String codigoInep = curso.getIdCursoInep().toString();
		if (codigoInep == null || codigoInep.equals("0") || codigoInep.equals("")) {
			codigoInep = emBranco;
		}
		String baseLegal = impressaoContratoVO.getAutorizacaoCurso().getNome();
		if (baseLegal == null || baseLegal.equals("0") || baseLegal.equals("")) {
			baseLegal = emBranco;
		}
		String primeiroReconhecimentoCurso = impressaoContratoVO.getPrimeiroReconhecimentoCurso().getNome();
		if (primeiroReconhecimentoCurso == null || primeiroReconhecimentoCurso.equals("0") || primeiroReconhecimentoCurso.equals("")) {
			primeiroReconhecimentoCurso = emBranco;
		}
		String dataPrimeiroReconhecimentoCurso = Uteis.getData(impressaoContratoVO.getPrimeiroReconhecimentoCurso().getData(), "dd/MM/yyyy");
		if (dataPrimeiroReconhecimentoCurso == null || dataPrimeiroReconhecimentoCurso.equals("0") || dataPrimeiroReconhecimentoCurso.equals("")) {
			dataPrimeiroReconhecimentoCurso = emBranco;
		}
		String renovacaoReconhecimentoCurso = impressaoContratoVO.getRenovacaoReconhecimentoCurso().getNome();
		if (renovacaoReconhecimentoCurso == null || renovacaoReconhecimentoCurso.equals("0") || renovacaoReconhecimentoCurso.equals("")) {
			renovacaoReconhecimentoCurso = emBranco;
		}
		String dataRenovacaoReconhecimentoCurso = Uteis.getData(impressaoContratoVO.getRenovacaoReconhecimentoCurso().getData(), "dd/MM/yyyy");
		if (dataRenovacaoReconhecimentoCurso == null || dataRenovacaoReconhecimentoCurso.equals("0") || dataRenovacaoReconhecimentoCurso.equals("")) {
			dataRenovacaoReconhecimentoCurso = emBranco;
		}
		String textoDeclaracaoPPI = MatriculaRSVO.realizarSubstituicaoTagsMatriculaExterna(curso.getTextoDeclaracaoPPI(), matricula.getAluno().getNome(), matricula.getAluno().getRG(), matricula.getAluno().getCPF());
		if (textoDeclaracaoPPI == null || textoDeclaracaoPPI.equals("0") || textoDeclaracaoPPI.equals("")) {
			textoDeclaracaoPPI = emBranco;
		}
		
		String textoDeclaracaoEscolaridade = MatriculaRSVO.realizarSubstituicaoTagsMatriculaExterna(curso.getTextoDeclaracaoEscolaridadePublica(), matricula.getAluno().getNome(), matricula.getAluno().getRG(), matricula.getAluno().getCPF());
		if (textoDeclaracaoEscolaridade == null || textoDeclaracaoEscolaridade.equals("0") || textoDeclaracaoEscolaridade.equals("")) {
			textoDeclaracaoEscolaridade = emBranco;
		}
		String textoDeclaracaoBolsas = MatriculaRSVO.realizarSubstituicaoTagsMatriculaExterna(curso.getTextoDeclaracaoBolsasAuxilios(), matricula.getAluno().getNome(), matricula.getAluno().getRG(), matricula.getAluno().getCPF());
		if (textoDeclaracaoBolsas == null || textoDeclaracaoBolsas.equals("0") || textoDeclaracaoBolsas.equals("")) {
			textoDeclaracaoBolsas = emBranco;
		}		
		String nivelEduc = curso.getNivelEducacional_Apresentar();
		if (nivelEduc == null || nivelEduc.equals("0") || nivelEduc.equals("")) {
			nivelEduc = emBranco;
		}
		
		
		
		String nrMesesConclusaoMatrizCurricular = null;
		String cargaHoraria = null;
		if (!matricula.getGradeCurricularAtual().getCodigo().equals(matriculaPeriodo.getGradeCurricular().getCodigo())) {
			nrMesesConclusaoMatrizCurricular = matricula.getGradeCurricularAtual().getNrMesesConclusaoMatrizCurricular().toString();
			cargaHoraria = matricula.getGradeCurricularAtual().getCargaHoraria().toString();
		} else {
			nrMesesConclusaoMatrizCurricular = matriculaPeriodo.getGradeCurricular().getNrMesesConclusaoMatrizCurricular().toString();
			cargaHoraria = matriculaPeriodo.getGradeCurricular().getCargaHoraria().toString();
		}
		if (cargaHoraria == null || cargaHoraria.equals("0") || cargaHoraria.equals("")) {
			cargaHoraria = emBranco;
		}
		if (nrMesesConclusaoMatrizCurricular == null || nrMesesConclusaoMatrizCurricular.equals("0") || nrMesesConclusaoMatrizCurricular.equals("")) {
			nrMesesConclusaoMatrizCurricular = emBranco;
		}
		String nomeCertificacao = impressaoContratoVO.getCertificadoCursoExtensaoRelVO().getNomeCertificacao();
		if (nomeCertificacao == null || nomeCertificacao.equals("0") || nomeCertificacao.equals("")) {
			nomeCertificacao = matriculaPeriodo.getPeriodoLetivo().getDescricao();
			if (nomeCertificacao == null || nomeCertificacao.equals("0") || nomeCertificacao.equals("")) {
				nomeCertificacao = emBranco;
			}
		}

		String titulacaoFormando = "";
		if (matricula.getAluno().getSexo().equals("M")) {
			titulacaoFormando = curso.getTitulacaoDoFormando();
		} else {
			titulacaoFormando = curso.getTitulacaoDoFormandoFeminino();
		}
		if (titulacaoFormando == null || titulacaoFormando.equals("")) {
			titulacaoFormando = emBranco;
		}

		// Caso a Habilitação da GradeCurricular esteja preenchida utilizar o mesmo senão utilizar a do curso.
		String habilitacaoCurso = Uteis.isAtributoPreenchido(matricula.getGradeCurricularAtual().getHabilitacao()) ? matricula.getGradeCurricularAtual().getHabilitacao() : curso.getHabilitacao();
		if (habilitacaoCurso == null || habilitacaoCurso.equals("")) {
			habilitacaoCurso = emBranco;
		}
		String competencia = impressaoContratoVO.getMatriculaPeriodoVO().getGradeCurricular().getCompetenciaProfissional();
		if (competencia == null || competencia.equals("")) {
			competencia = emBranco;
		}
		String perfilEgresso = curso.getPerfilEgresso();
		if (perfilEgresso == null || perfilEgresso.equals("")) {
			perfilEgresso = emBranco;
		}

		String anoVencimentoPrimeiraParcela = "";
		String mesVencimentoPrimeiraParcela = "";
		String diaVencimentoPrimeiraParcela = "";
		String dataVencimentoPrimeiraParcela = "";
		String dataVencimentoSegundaParcela = "";
		String dataVencimentoUltimaParcela = "";
		Date diaVencimentoParcela = null;
		Date diaVencimentoApartirSegundaParcela = null;
		double valorMatriculaCursoDouble = 0.0;
		Extenso ext = new Extenso();
		
		String valorIntegralMatriculaCondicaoPagamentoStr = "";
		String valorIntegralParcelaCondicaoPagamentoStr = "";
		String nrParcelasPeriodoCondicaoPagamentoStr = "";
		String valorIntegralMatriculaCondicaoPagamentoExtenso = "";
		String valorIntegralParcelaCondicaoPagamentoExtenso = "";

		if (!matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo().equals(0)) {
			valorIntegralMatriculaCondicaoPagamentoStr = Uteis.getDoubleFormatado(matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getValorMatricula());
			valorIntegralParcelaCondicaoPagamentoStr = Uteis.getDoubleFormatado(matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getValorParcela());
			nrParcelasPeriodoCondicaoPagamentoStr = matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getNrParcelasPeriodo().toString();
			ext.setNumber(Uteis.arrendondarForcando2CasasDecimais(matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getValorMatricula()));
			valorIntegralMatriculaCondicaoPagamentoExtenso = ext.toString();
			ext.setNumber(Uteis.arrendondarForcando2CasasDecimais(matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getValorParcela()));
			valorIntegralParcelaCondicaoPagamentoExtenso = ext.toString();
		}
				
		Double valorMatriculaCursoComDescontosDouble = 0.0;
		Double valorMatriculaCursoApenasDescontosProgressivoDouble = 0.0;
		double valorParcelaCursoDouble = 0.0;
		double valorParcelaCursoComConvenioCusteadoDouble = 0.0;
		Double valorTotalCursoDouble = 0.0;
		Double valorTotalCursoComConvenioCusteado = 0.0;
		Double valorTotalCursoSemDescontoDouble = 0.0;
		Double valorTotalCursoApenasDescontosInstituicaoDouble = 0.0;
		Double valorTotalDescontosInstituicaoDouble = 0.0;
		Double valorParcelaCursoComDescontos = 0.0;
		Double valorParcelaComDescontoConvenio = 0.0;
		Double valorParcelaSemDescontoConvenio = 0.0;
		Double valorPrimeiraParcela = 0.0;
		Double valorDescontoInstitucionalPrimeiraParcela = 0.0;
		
		Double valorDescontoParcelaMaterialDidaticoDoubleValue = 0.0;   
		Double valorTotalParcelasComDescontoIncondicionalDouble = 0.0;
		Double valorParcelaComDescontoIncondicionalDouble = 0.0;
		Double valorParcelaMaterialDidaticoComDescontoIncondicionalDouble = 0.0;
		Double valorDescontoCondicionalMatricula = 0.0;
		Double valorDescontoCondicionalParcela = 0.0;
		Double valorDescontoCondicionalMaterialDidatico = 0.0;
		Double valorDescontoInCondicionalMaterialDidatico = 0.0;
		
		Double valorCusteadoConvenioParcelaMaterialDidaticoCurso = 0.0;
		Double valorCusteadoAlunoParcelaMaterialDidaticoCurso = 0.0;
		
		Double porcentagemCusteadoConvenioParcelaCurso = 0.0;
		Double porcentagemCusteadoAlunoParcelaCurso = 0.0;
		Double valorTotalCusteadoConvenioParcelaCurso = 0.0;
		Double valorCusteadoConvenioParcelaCurso = 0.0;
		Double valorCusteadoAlunoParcelaCurso = 0.0;
		Double valorParcelaComDescontoConvenioSegundaFaixa = 0.0;
		Double valorParcelaComDescontoConvenioTerceiraFaixa = 0.0;
		
		String nomeConvenio = "";
		ContaReceberVO contareceberprimeiroparcela = null;
		ConvenioVO convenio = null;
		for(ItemPlanoFinanceiroAlunoVO itemPlanoFinanceiroAlunoVO: planoFinanceiroAluno.getItemPlanoFinanceiroAlunoVOs()) {
			if(!itemPlanoFinanceiroAlunoVO.getTipoPlanoFinanceiroDescontoInstitucional()) {
				convenio = itemPlanoFinanceiroAlunoVO.getConvenio();
				break;
			}
		}
		Iterator i = matriculaPeriodo.getMatriculaPeriodoVencimentoVOs().iterator();
		Integer valor = matriculaPeriodo.getMatriculaPeriodoVencimentoVOs().size() - 1;		
		while (i.hasNext()) {
			MatriculaPeriodoVencimentoVO mat = (MatriculaPeriodoVencimentoVO) i.next();
			if (mat.getTipoOrigemMatriculaPeriodoVencimento().equals(TipoOrigemContaReceber.MATERIAL_DIDATICO)) {
				if(valorDescontoCondicionalMaterialDidatico.equals(0.0) && !mat.getContaReceber().getListaDescontosAplicavesContaReceber().isEmpty()) {
					valorDescontoCondicionalMaterialDidatico = mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorDescontoSemValidade() + mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorDescontoComValidade();
					valorParcelaMaterialDidaticoComDescontoIncondicionalDouble = mat.getValor() - valorDescontoCondicionalMaterialDidatico;
				}
				if(valorDescontoInCondicionalMaterialDidatico.equals(0.0) && !mat.getContaReceber().getListaDescontosAplicavesContaReceber().isEmpty()) {
					valorDescontoInCondicionalMaterialDidatico = mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorDescontoSemValidade();
				}
				if (!Uteis.isAtributoPreenchido(mat.getContaReceber())) {
					valorDescontoParcelaMaterialDidaticoDoubleValue = Uteis.arrendondarForcando2CadasDecimais(mat.getValor() - mat.getValorDescontoCalculadoPrimeiraFaixaDescontos());
					valorDescontoCondicionalMaterialDidatico = valorDescontoParcelaMaterialDidaticoDoubleValue;
					valorParcelaMaterialDidaticoComDescontoIncondicionalDouble = mat.getValor() - valorDescontoParcelaMaterialDidaticoDoubleValue;
				} else if (!mat.getContaReceber().getListaDescontosAplicavesContaReceber().isEmpty()) {
					valorDescontoParcelaMaterialDidaticoDoubleValue = mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorDescontoSemValidade() + mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorDescontoComValidade();
					if (Uteis.isAtributoPreenchido(mat.getContaReceber().getValorBaseContaReceber())) {
						valorParcelaMaterialDidaticoComDescontoIncondicionalDouble = mat.getContaReceber().getValorBaseContaReceber() - valorDescontoParcelaMaterialDidaticoDoubleValue;
					} else {
						valorParcelaMaterialDidaticoComDescontoIncondicionalDouble = mat.getContaReceber().getValor() - valorDescontoParcelaMaterialDidaticoDoubleValue;
					}
				}
			} else if (mat.getParcela().equals("MA") || mat.getTipoOrigemMatriculaPeriodoVencimento().equals(TipoOrigemContaReceber.MATRICULA)) {
				if(Uteis.isAtributoPreenchido(mat.getContaReceber())){
					valorMatriculaCursoDouble = mat.getContaReceber().getValor();
				} else{
					valorMatriculaCursoDouble = mat.getValor();	
				}	
				if (diaVencimentoParcela == null && matriculaPeriodo.getMatriculaPeriodoVencimentoVOs().size() == 1) {
					diaVencimentoParcela = mat.getDataVencimento();
				}
				if(valorDescontoCondicionalMatricula.equals(0.0) && !mat.getContaReceber().getListaDescontosAplicavesContaReceber().isEmpty()) {
					valorDescontoCondicionalMatricula = mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorDescontoComValidade();
				}
				valorMatriculaCursoComDescontosDouble = mat.getValorDescontoCalculadoPrimeiraFaixaDescontos();
				valorMatriculaCursoApenasDescontosProgressivoDouble = mat.getValorDescontoCalculadoPrimeiraFaixaDescontos() + mat.getValorDescontoInstituicao() + mat.getValorDescontoConvenio();
			} else {
				contareceberprimeiroparcela = mat.getContaReceber();	
				if ((mat.getParcela().contains("1/") || mat.getParcela().contains("01")) && anoVencimentoPrimeiraParcela.equals("")) {
					if (!mat.getDataVencimento_Apresentar().equals("")) {
						dataVencimentoPrimeiraParcela = mat.getDataVencimento_Apresentar();
						anoVencimentoPrimeiraParcela = String.valueOf(Uteis.getAnoData(mat.getDataVencimento()));
					} else {
						dataVencimentoPrimeiraParcela = mat.getContaReceber().getDataVencimento_Apresentar();
						anoVencimentoPrimeiraParcela = String.valueOf(Uteis.getAnoData(mat.getContaReceber().getDataVencimento()));
					}
					
					//ConfiguracoesVO conf = new Configuracoes().consultarConfiguracaoASerUsada(false, Uteis.NIVELMONTARDADOS_COMBOBOX, null, matricula.getUnidadeEnsino().getCodigo());
					//ConfiguracaoFinanceiroVO confFinanceiro = new ConfiguracaoFinanceiro().consultaRapidaConfiguracaoASerUsada(conf.getCodigo(), nivelMontarDados, null);
					if (Uteis.isAtributoPreenchido(mat.getContaReceber())) {
						valorPrimeiraParcela = mat.getContaReceber().getCalcularValorFinal(impressaoContratoVO.getConfiguracaoFinanceiroVO(), usuario);
					} else {
						if (!mat.getContaReceber().getListaDescontosAplicavesContaReceber().isEmpty()) {
							valorPrimeiraParcela = mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorBaseComDescontosJaCalculadosAplicados();
						}
					}
					valorDescontoInstitucionalPrimeiraParcela = mat.getContaReceber().getValorDescontoInstituicao();
				}
				if ((mat.getParcela().contains("1/") || mat.getParcela().contains("01")) && mesVencimentoPrimeiraParcela.equals("")) {
					if (!mat.getDataVencimento_Apresentar().equals("")) {
						mesVencimentoPrimeiraParcela = String.valueOf(Uteis.getMesData(mat.getDataVencimento()));
					} else {
						mesVencimentoPrimeiraParcela = String.valueOf(Uteis.getMesData(mat.getContaReceber().getDataVencimento()));
					}
				}
				if ((mat.getParcela().contains("1/") || mat.getParcela().contains("01")) && diaVencimentoPrimeiraParcela.equals("")) {
					if (!mat.getDataVencimento_Apresentar().equals("")) {
						diaVencimentoPrimeiraParcela = String.valueOf(Uteis.getDiaMesData(mat.getDataVencimento()));
					} else {
						diaVencimentoPrimeiraParcela = String.valueOf(Uteis.getDiaMesData(mat.getContaReceber().getDataVencimento()));
					}
				}
				if ((mat.getParcela().contains("2/") || mat.getParcela().contains("02"))) {
					if (!mat.getDataVencimento_Apresentar().equals("")) {
						dataVencimentoSegundaParcela = mat.getDataVencimento_Apresentar();
						diaVencimentoApartirSegundaParcela = mat.getDataVencimento();
					} else {
						dataVencimentoSegundaParcela = mat.getContaReceber().getDataVencimento_Apresentar();
						diaVencimentoApartirSegundaParcela = mat.getContaReceber().getDataVencimento();
					}
				}
				if (mat.getParcela().contains(valor + "/")) {
					if (!mat.getDataVencimento_Apresentar().equals("")) {
						dataVencimentoUltimaParcela = mat.getDataVencimento_Apresentar();
					} else {
						dataVencimentoUltimaParcela = mat.getContaReceber().getDataVencimento_Apresentar();
					}
				}
				if (diaVencimentoParcela == null) {
					if (!mat.getDataVencimento_Apresentar().equals("")) {
						diaVencimentoParcela = mat.getDataVencimento();
					} else if (mat.getContaReceber().getDataVencimento_Apresentar().equals("")) {
						diaVencimentoParcela = mat.getDataVencimento();
					} else {
						diaVencimentoParcela = mat.getContaReceber().getDataVencimento();
					}
				}
				if(Uteis.isAtributoPreenchido(mat.getContaReceber())){
					valorParcelaCursoDouble = mat.getContaReceber().getValor();
					valorParcelaCursoComConvenioCusteadoDouble = mat.getContaReceber().getValorBaseContaReceber();
				}else{
					valorParcelaCursoDouble = mat.getValor();
					valorParcelaCursoComConvenioCusteadoDouble = mat.getValor(); 
				}
				nomeConvenio = mat.getContaReceber().getConvenio().getDescricao();
				valorParcelaCursoComDescontos = mat.getValorDescontoCalculadoPrimeiraFaixaDescontos();
				valorParcelaComDescontoConvenio = mat.getValorDescontoCalculadoPrimeiraFaixaDescontos();
				valorParcelaSemDescontoConvenio = mat.getValorDescontoCalculadoPrimeiraFaixaDescontos() - mat.getValorDescontoConvenio();
				valorParcelaComDescontoConvenioSegundaFaixa = mat.getValorDescontoCalculadoSegundaFaixaDescontos();
				valorParcelaComDescontoConvenioTerceiraFaixa = mat.getValorDescontoCalculadoTerceiraFaixaDescontos();
			}
			if(Uteis.isAtributoPreenchido(mat.getContaReceber())){
				valorTotalCursoDouble += mat.getContaReceber().getValor();
				valorTotalCursoComConvenioCusteado += mat.getContaReceber().getValorBaseContaReceber();
				valorTotalDescontosInstituicaoDouble +=mat.getContaReceber().getValorDescontoInstituicao();
				if (!mat.getParcela().equals("MA") && !mat.getTipoOrigemMatriculaPeriodoVencimento().equals(TipoOrigemContaReceber.MATRICULA) && !mat.getTipoOrigemMatriculaPeriodoVencimento().equals(TipoOrigemContaReceber.MATERIAL_DIDATICO)) {
					if(valorDescontoCondicionalParcela.equals(0.0) && !mat.getContaReceber().getListaDescontosAplicavesContaReceber().isEmpty()) {
						valorDescontoCondicionalParcela = mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorDescontoComValidade();
					}
					Double descontoIncondicional = mat.getContaReceber().getPlanoDescontoContaReceberVOs().stream()
							.filter(pdcrvo -> pdcrvo.getUtilizarDescontoSemLimiteValidade())
							.mapToDouble(PlanoDescontoContaReceberVO::getValorUtilizadoRecebimento).sum();
					if (!mat.getContaReceber().getListaDescontosAplicavesContaReceber().isEmpty()) {
						if (Uteis.isAtributoPreenchido(mat.getContaReceber().getValorBaseContaReceber())) {
							// Criado para trata um caso especifico de impressão de contratos antigos do IPOG. Terá que ser ajustado com maior tempo.
							valorParcelaComDescontoIncondicionalDouble = mat.getContaReceber().getValorBaseContaReceber() - (mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorDescontoSemValidade() - mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorCusteadoContaReceber());
							//valorParcelaComDescontoIncondicionalDouble = mat.getContaReceber().getValorBaseContaReceber() - (descontoIncondicional + mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorDescontoSemValidade());
							//valorTotalParcelasComDescontoIncondicionalDouble += mat.getContaReceber().getValorBaseContaReceber() - (descontoIncondicional + mat.getContaReceber().getValorDescontoAlunoJaCalculado());
							valorTotalParcelasComDescontoIncondicionalDouble += valorParcelaComDescontoIncondicionalDouble;
						} else {
							valorParcelaComDescontoIncondicionalDouble = mat.getContaReceber().getValor() - (mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorDescontoSemValidade() - mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorCusteadoContaReceber());
							//valorParcelaComDescontoIncondicionalDouble = mat.getContaReceber().getValor() - (descontoIncondicional + mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorDescontoSemValidade());
							valorTotalParcelasComDescontoIncondicionalDouble += valorParcelaComDescontoIncondicionalDouble;
						}
					}
				}
				if (Uteis.isAtributoPreenchido(mat.getContaReceber().getValorBaseContaReceber())){
					valorTotalCursoApenasDescontosInstituicaoDouble += mat.getContaReceber().getValorBaseContaReceber() - mat.getContaReceber().getValorDescontoInstituicao();
				} else {
					valorTotalCursoApenasDescontosInstituicaoDouble += mat.getContaReceber().getValor() - mat.getContaReceber().getValorDescontoInstituicao();	
				}
			}else{
				valorTotalCursoDouble += mat.getValor();
				valorTotalCursoComConvenioCusteado += mat.getValor();
				Double valorDescontoInstituicaoCalculado = mat.getValorDescontoInstituicao();
				if(mat.getSituacao().isSituacaoContaReceberNaoGeracao() && mat.getTipoOrigemMatriculaPeriodoVencimento().isMatricula()){
					valorDescontoInstituicaoCalculado =impressaoContratoVO.getMatriculaPeriodoVO().getListaDescontosMatricula().isEmpty() ? 0 : impressaoContratoVO.getMatriculaPeriodoVO().getListaDescontosMatricula().get(0).getValorDescontoInstituicao();
				}else if(mat.getSituacao().isSituacaoContaReceberNaoGeracao() && mat.getTipoOrigemMatriculaPeriodoVencimento().isMaterialDidatico()){
					valorDescontoInstituicaoCalculado =impressaoContratoVO.getMatriculaPeriodoVO().getListaDescontosMaterialDidatico().isEmpty() ? 0 : impressaoContratoVO.getMatriculaPeriodoVO().getListaDescontosMaterialDidatico().get(0).getValorDescontoInstituicao();			
				}else if(mat.getSituacao().isSituacaoContaReceberNaoGeracao() && mat.getTipoOrigemMatriculaPeriodoVencimento().isMensalidade()){
					valorDescontoInstituicaoCalculado =impressaoContratoVO.getMatriculaPeriodoVO().getListaDescontosMensalidade().isEmpty() ? 0 : impressaoContratoVO.getMatriculaPeriodoVO().getListaDescontosMensalidade().get(0).getValorDescontoInstituicao();
				}
				valorTotalDescontosInstituicaoDouble += valorDescontoInstituicaoCalculado;
				valorTotalCursoApenasDescontosInstituicaoDouble += mat.getValor() - valorDescontoInstituicaoCalculado;
				if (!mat.getParcela().equals("MA") && !mat.getTipoOrigemMatriculaPeriodoVencimento().equals(TipoOrigemContaReceber.MATRICULA) && !mat.getTipoOrigemMatriculaPeriodoVencimento().equals(TipoOrigemContaReceber.MATERIAL_DIDATICO)) {
					if(valorDescontoCondicionalParcela.equals(0.0) && !mat.getContaReceber().getListaDescontosAplicavesContaReceber().isEmpty()) {
						valorDescontoCondicionalParcela = mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorDescontoComValidade();
					}
					if(!mat.getContaReceber().getListaDescontosAplicavesContaReceber().isEmpty()) {
						valorDescontoParcelaMaterialDidaticoDoubleValue = mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorDescontoSemValidade();
						if (Uteis.isAtributoPreenchido(mat.getContaReceber().getValorBaseContaReceber())) {
							valorParcelaComDescontoIncondicionalDouble = mat.getContaReceber().getValorBaseContaReceber() - (mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorDescontoSemValidade() - mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorCusteadoContaReceber());
							valorTotalParcelasComDescontoIncondicionalDouble += mat.getContaReceber().getValorBaseContaReceber() - (mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorDescontoSemValidade() - mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorCusteadoContaReceber());
						} else if (Uteis.isAtributoPreenchido(mat.getContaReceber().getValor())){
							valorParcelaComDescontoIncondicionalDouble = mat.getContaReceber().getValor() - (mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorDescontoSemValidade() - mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorCusteadoContaReceber());
							valorTotalParcelasComDescontoIncondicionalDouble += mat.getContaReceber().getValor() - (mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorDescontoSemValidade() - mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorCusteadoContaReceber());
						} else {
							valorParcelaComDescontoIncondicionalDouble = mat.getValor() - (mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorDescontoSemValidade() - mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorCusteadoContaReceber());
							valorTotalParcelasComDescontoIncondicionalDouble += mat.getValor() - (mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorDescontoSemValidade() - mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0).getValorCusteadoContaReceber());
						}
					}
				}
			}
			if (valorCusteadoConvenioParcelaMaterialDidaticoCurso.equals(0.0) && (mat.getContaReceber().isContaReceberReferenteMaterialDidatico() || mat.getTipoOrigemMatriculaPeriodoVencimento().isMaterialDidatico())) {
				if(Uteis.isAtributoPreenchido(convenio) && !mat.getContaReceber().getListaDescontosAplicavesContaReceber().isEmpty()) {
					PlanoFinanceiroAlunoDescricaoDescontosVO planoFinanceiroAlunoDescricaoDescontosVO = mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0);
					if(planoFinanceiroAlunoDescricaoDescontosVO.getListaDescontosConvenio().containsKey(convenio.getCodigo())) {
						valorCusteadoConvenioParcelaMaterialDidaticoCurso = planoFinanceiroAlunoDescricaoDescontosVO.getListaDescontosConvenio().get(convenio.getCodigo());
						valorCusteadoAlunoParcelaMaterialDidaticoCurso = mat.getContaReceber().getValorBaseContaReceber() - valorCusteadoConvenioParcelaMaterialDidaticoCurso;
					} else {
						valorCusteadoAlunoParcelaMaterialDidaticoCurso = mat.getContaReceber().getValorBaseContaReceber();
					}
				} else if (!Uteis.isAtributoPreenchido(convenio)) {
					valorCusteadoAlunoParcelaMaterialDidaticoCurso = mat.getContaReceber().getValorBaseContaReceber();
				}
			} 
			if (mat.getContaReceber().getIsContaReceberReferenteMensalidade() || mat.getTipoOrigemMatriculaPeriodoVencimento().isMensalidade()) {
				if(Uteis.isAtributoPreenchido(convenio) && !mat.getContaReceber().getListaDescontosAplicavesContaReceber().isEmpty()) {
					PlanoFinanceiroAlunoDescricaoDescontosVO planoFinanceiroAlunoDescricaoDescontosVO = mat.getContaReceber().getListaDescontosAplicavesContaReceber().get(0);
					if(planoFinanceiroAlunoDescricaoDescontosVO.getListaDescontosConvenio().containsKey(convenio.getCodigo())) {
						valorCusteadoConvenioParcelaCurso = planoFinanceiroAlunoDescricaoDescontosVO.getListaDescontosConvenio().get(convenio.getCodigo());
						valorCusteadoAlunoParcelaCurso = mat.getContaReceber().getValorBaseContaReceber() - valorCusteadoConvenioParcelaMaterialDidaticoCurso;
						valorTotalCusteadoConvenioParcelaCurso += valorCusteadoConvenioParcelaCurso;
					} else {
						valorCusteadoAlunoParcelaCurso = mat.getContaReceber().getValorBaseContaReceber();
					}
				} else if (!Uteis.isAtributoPreenchido(convenio)) {
					valorCusteadoAlunoParcelaCurso = mat.getContaReceber().getValorBaseContaReceber();
				}
				if (porcentagemCusteadoConvenioParcelaCurso.equals(0.0) && !mat.getContaReceber().getValorBaseContaReceber().equals(0.0)) {
					porcentagemCusteadoConvenioParcelaCurso = valorCusteadoConvenioParcelaCurso / mat.getContaReceber().getValorBaseContaReceber() * 100;
				}
				if (porcentagemCusteadoAlunoParcelaCurso.equals(0.0) && !mat.getContaReceber().getValorBaseContaReceber().equals(0.0)) {
					porcentagemCusteadoAlunoParcelaCurso = valorCusteadoAlunoParcelaCurso / mat.getContaReceber().getValorBaseContaReceber() * 100;
				}
			}
			valorTotalCursoSemDescontoDouble += mat.getValorDescontoCalculadoPrimeiraFaixaDescontos();
		}
		String nomeConvenio_Matricula = nomeConvenio;
		if (nomeConvenio_Matricula == null || nomeConvenio_Matricula.equals("")) {
			nomeConvenio_Matricula = emBranco;
		}
		
		String valorParcelaComDescontoConvenio_Matricula = Uteis.getDoubleFormatado(valorParcelaComDescontoConvenio);
		if (valorParcelaComDescontoConvenio_Matricula == null || valorParcelaComDescontoConvenio_Matricula.equals("")) {
			valorParcelaComDescontoConvenio_Matricula = emBranco;
		}

		if (valorParcelaComDescontoConvenio < 0) {
			valorParcelaComDescontoConvenio = 0.0;
		}
		ext.setNumber(valorParcelaComDescontoConvenio);
		String valorParcelaComDescontoConvenio_MatriculaExtenso = ext.toString();
		if (valorParcelaComDescontoConvenio_MatriculaExtenso == null || valorParcelaComDescontoConvenio_MatriculaExtenso.equals("")) {
			valorParcelaComDescontoConvenio_MatriculaExtenso = emBranco;
		}
		String valorParcelaSemDescontoConvenio_Matricula = Uteis.getDoubleFormatado(valorParcelaSemDescontoConvenio);
		if (valorParcelaSemDescontoConvenio_Matricula == null || valorParcelaSemDescontoConvenio_Matricula.equals("")) {
			valorParcelaSemDescontoConvenio_Matricula = emBranco;
		}

		if (valorParcelaSemDescontoConvenio < 0) {
			valorParcelaSemDescontoConvenio = 0.0;
		}
		ext.setNumber(valorParcelaSemDescontoConvenio);
		String valorParcelaSemDescontoConvenio_MatriculaExtenso = ext.toString();
		if (valorParcelaSemDescontoConvenio_MatriculaExtenso == null || valorParcelaSemDescontoConvenio_MatriculaExtenso.equals("")) {
			valorParcelaSemDescontoConvenio_MatriculaExtenso = emBranco;
		}

		String diaVencimentoParcelaString = emBranco;
		String diaVencimentoParcelaStringExtenso = emBranco;

		if (diaVencimentoParcela != null) {
			diaVencimentoParcelaString = new Integer(Uteis.getDiaMesData(diaVencimentoParcela)).toString();
			if (diaVencimentoParcelaString == null || diaVencimentoParcelaString.equals("")) {
				diaVencimentoParcelaString = emBranco;
			}
			ext.setNumber(Uteis.getDiaMesData(diaVencimentoParcela));
			ext.setUsarBranco(Boolean.TRUE);
			diaVencimentoParcelaStringExtenso = ext.toString();
			if (diaVencimentoParcelaStringExtenso == null || diaVencimentoParcelaStringExtenso.equals("")) {
				diaVencimentoParcelaStringExtenso = emBranco;
			}
			ext.setUsarBranco(Boolean.FALSE);
		}

		String descricaoPlanoDescontos = obterDescricaoPlanoDesconto(listaPlanoDesconto);
		Integer nrDiasAntesVencimentoPlanoDesconto = obterNrDiasAntesVencimentoPlanoDescontoComValidade(listaPlanoDesconto);
		Integer diaPontualidadePlanoDesconto = 0;
		if (diaVencimentoParcela != null) {
			diaPontualidadePlanoDesconto = Uteis.getDiaMesData(Uteis.obterDataAntiga(diaVencimentoParcela, nrDiasAntesVencimentoPlanoDesconto));
		}

		String diaVencimentoAPartirSegundaParcelaString = emBranco;
		String diaVencimentoAPartirSegundaParcelaStringExtenso = emBranco;

		if (diaVencimentoApartirSegundaParcela != null) {
			diaVencimentoAPartirSegundaParcelaString = new Integer(Uteis.getDiaMesData(diaVencimentoApartirSegundaParcela)).toString();
			if (diaVencimentoAPartirSegundaParcelaString == null || diaVencimentoAPartirSegundaParcelaString.equals("")) {
				diaVencimentoAPartirSegundaParcelaString = emBranco;
			}
			ext.setNumber(Uteis.getDiaMesData(diaVencimentoApartirSegundaParcela));
			ext.setUsarBranco(Boolean.TRUE);
			diaVencimentoAPartirSegundaParcelaStringExtenso = ext.toString();
			if (diaVencimentoAPartirSegundaParcelaStringExtenso == null || diaVencimentoAPartirSegundaParcelaStringExtenso.equals("")) {
				diaVencimentoAPartirSegundaParcelaStringExtenso = emBranco;
			}
			ext.setUsarBranco(Boolean.FALSE);
		}

		String nrDiasAntesVencimentoPlanoDescontoComValidade = diaPontualidadePlanoDesconto.toString();
		if (nrDiasAntesVencimentoPlanoDescontoComValidade == null || nrDiasAntesVencimentoPlanoDescontoComValidade.equals("")) {
			nrDiasAntesVencimentoPlanoDescontoComValidade = emBranco;
		}
		String descricaoPlanoDescontosStr = descricaoPlanoDescontos;
		if (descricaoPlanoDescontosStr == null || descricaoPlanoDescontosStr.equals("")) {
			descricaoPlanoDescontosStr = emBranco;
		}
		String valorMatriculaCurso = Uteis.getDoubleFormatado(valorMatriculaCursoDouble);
		if (valorMatriculaCurso == null || valorMatriculaCurso.equals("")) {
			valorMatriculaCurso = emBranco;
		}
		String valorMatriculaCursoComDescontos = Uteis.getDoubleFormatado(valorMatriculaCursoComDescontosDouble);
		if (valorMatriculaCursoComDescontos == null || valorMatriculaCursoComDescontos.equals("")) {
			valorMatriculaCursoComDescontos = emBranco;
		}
		String valorMatriculaCursoApenasDescontosProgressivo = Uteis.getDoubleFormatado(valorMatriculaCursoApenasDescontosProgressivoDouble);
		if (valorMatriculaCursoApenasDescontosProgressivo == null || valorMatriculaCursoApenasDescontosProgressivo.equals("")) {
			valorMatriculaCursoApenasDescontosProgressivo = emBranco;
		}
		String valorParcelaCursoComDescontosStr = Uteis.getDoubleFormatado(valorParcelaCursoComDescontos);
		if (valorParcelaCursoComDescontosStr == null || valorParcelaCursoComDescontosStr.equals("")) {
			valorParcelaCursoComDescontosStr = emBranco;
		}

		if (valorParcelaCursoComDescontos < 0) {
			valorParcelaCursoComDescontos = 0.0;
		}
		ext.setNumber(valorParcelaCursoComDescontos);
		String valorParcelaComDescontoExtenso = ext.toString();

		if (valorParcelaComDescontoExtenso == null || valorParcelaComDescontoExtenso.equals("")) {
			valorParcelaComDescontoExtenso = emBranco;
		}

		// Extenso ext = new Extenso();
		ext.setNumber(valorMatriculaCursoDouble);
		String valorMatriculaCursoExtenso = ext.toString();
		if (valorMatriculaCursoExtenso == null || valorMatriculaCursoExtenso.equals("")) {
			valorMatriculaCursoExtenso = emBranco;
		}
		ext.setNumber(valorMatriculaCursoComDescontosDouble);
		String valorMatriculaCursoComDescontosExtenso = ext.toString();
		if (valorMatriculaCursoComDescontosExtenso == null || valorMatriculaCursoComDescontosExtenso.equals("")) {
			valorMatriculaCursoComDescontosExtenso = emBranco;
		}
		ext.setNumber(valorMatriculaCursoApenasDescontosProgressivoDouble);
		String valorMatriculaCursoApenasDescontosProgressivoExtenso = ext.toString();
		if (valorMatriculaCursoApenasDescontosProgressivoExtenso == null || valorMatriculaCursoApenasDescontosProgressivoExtenso.equals("")) {
			valorMatriculaCursoApenasDescontosProgressivoExtenso = emBranco;
		}
		String listaDisciplinaOptativa = "";
		boolean primeiraVez = true;
		boolean existeDiscOptativa = false;
		Integer cargaHorariaDisc = 0;
		Iterator mat = matriculaPeriodo.getMatriculaPeriodoTumaDisciplinaVOs().iterator();
		while (mat.hasNext()) {
			MatriculaPeriodoTurmaDisciplinaVO matriPerTurmaDisciplina = (MatriculaPeriodoTurmaDisciplinaVO) mat.next();
			cargaHorariaDisc = cargaHorariaDisc + matriPerTurmaDisciplina.getGradeDisciplinaVO().getCargaHoraria();

			if (matriPerTurmaDisciplina.getGradeDisciplinaVO().getTipoDisciplina().equals(TipoDisciplina.OPTATIVA.getValor())) {
				if (primeiraVez) {
					listaDisciplinaOptativa = "<table border=1 cellspacing=0 cellpadding=1 bordercolor=\"000000\"><tr><th>Disciplina(s) Optativa(s)</th><th>Carga Horária</th></tr>";
				}
				listaDisciplinaOptativa = listaDisciplinaOptativa + " <tr><td>" + matriPerTurmaDisciplina.getDisciplina().getNome().toUpperCase() + " </td><td> " + matriPerTurmaDisciplina.getGradeDisciplinaVO().getCargaHoraria() + " </td></tr>";
				primeiraVez = false;
				existeDiscOptativa = true;
			}
		}
		if (existeDiscOptativa) {
			listaDisciplinaOptativa = listaDisciplinaOptativa + "</table>";
		}

		String cargaHorariaDiscStr = Uteis.getDoubleFormatado(new Double(cargaHorariaDisc));
		if (cargaHorariaDiscStr == null || cargaHorariaDiscStr.equals("")) {
			cargaHorariaDiscStr = "0";
		}
		Integer tamanhoMatriculaPeriodoVencimento = 0;
		if (!matriculaPeriodo.getMatriculaPeriodoVencimentoVOs().isEmpty()) {
			if (planoFinanceiroAluno.getCondicaoPagamentoPlanoFinanceiroCursoVO().getNaoControlarMatricula()) {
				tamanhoMatriculaPeriodoVencimento = matriculaPeriodo.getQtdeParcelaContrato();
			} else {
				boolean possuiMatricula = false;
				for (MatriculaPeriodoVencimentoVO matPer : matriculaPeriodo.getMatriculaPeriodoVencimentoVOs()) {
					if (matPer.getParcela().equals("MA") || matPer.getParcela().equals("Matrícula")) {
						possuiMatricula = true;
					}
				}
				if (possuiMatricula) {
					tamanhoMatriculaPeriodoVencimento = matriculaPeriodo.getMatriculaPeriodoVencimentoVOs().size() - 1;
				} else {
					tamanhoMatriculaPeriodoVencimento = matriculaPeriodo.getMatriculaPeriodoVencimentoVOs().size();
				}
			}

		}

		String numParcelaCurso = new Integer(tamanhoMatriculaPeriodoVencimento).toString();
		if (numParcelaCurso == null || numParcelaCurso.equals("")) {
			numParcelaCurso = emBranco;
		}
		ext.setNumber(new Double(tamanhoMatriculaPeriodoVencimento));
		String numParcelaCursoExtenso = ext.toStringNaoMonetario();
		if (numParcelaCursoExtenso == null || numParcelaCursoExtenso.equals("")) {
			numParcelaCursoExtenso = emBranco;
		}
		String valorParcelaCurso = Uteis.getDoubleFormatado(valorParcelaCursoDouble);
		if (valorParcelaCurso == null || valorParcelaCurso.equals("")) {
			valorParcelaCurso = emBranco;
		}
		ext.setNumber(valorParcelaCursoDouble);
		String valorParcelaCursoExtenso = ext.toString();
		if (valorParcelaCursoExtenso == null || valorParcelaCursoExtenso.equals("")) {
			valorParcelaCursoExtenso = emBranco;
		}

		Double valorPorDisciplinaDouble = 0.0;
		// Integer qtdeDisciplinaGrade = new
		// MatriculaPeriodoTurmaDisciplina().consultarQuantidadeDisciplinasDaGradePorMatriculaPeriodo(matriculaPeriodo.getCodigo(),
		// false, Uteis.NIVELMONTARDADOS_COMBOBOX, null);
		Integer qtdeDisciplinaGrade = matriculaPeriodo.getMatriculaPeriodoTumaDisciplinaVOs().size();
		// if (qtdeDisciplinaGrade.equals(0)) {
		// throw new
		// Exception("Não foram encontradas disciplinas que fazem parte dessa grade, por favor verifique as disciplinas da Turma. Caso necessário, atualize a mesma.");
		// }

		if (matriculaPeriodo.getMatriculaPeriodoTumaDisciplinaVOs().size() > 0) {
			if (matriculaPeriodo.getCodigo() != 0) {
				if (!qtdeDisciplinaGrade.equals(0)) {
					valorPorDisciplinaDouble = Uteis.arrendondarForcando2CadasDecimais((valorTotalCursoDouble - valorMatriculaCursoDouble) / qtdeDisciplinaGrade);
				}
			} else {
				valorPorDisciplinaDouble = Uteis.arrendondarForcando2CadasDecimais((valorTotalCursoDouble - valorMatriculaCursoDouble) / matriculaPeriodo.getMatriculaPeriodoTumaDisciplinaVOs().size());
			}
		}

		String valorPorDisciplina = Uteis.getDoubleFormatado(valorPorDisciplinaDouble);
		if (valorPorDisciplina == null || valorPorDisciplina.equals("")) {
			valorPorDisciplina = emBranco;
		}

		ext.setNumber(valorPorDisciplinaDouble);
		String valorPorDisciplinaExtenso = ext.toString();
		if (valorPorDisciplinaExtenso == null || valorPorDisciplinaExtenso.equals("")) {
			valorPorDisciplinaExtenso = emBranco;
		}

		String valorTotalCurso = Uteis.getDoubleFormatado(valorTotalCursoDouble);
		if (valorTotalCurso == null || valorTotalCurso.equals("")) {
			valorTotalCurso = emBranco;
		}

		String valorTotalCursoComConvenioCusteadoString = Uteis.getDoubleFormatado(valorTotalCursoComConvenioCusteado);
		if (valorTotalCursoComConvenioCusteadoString == null || valorTotalCursoComConvenioCusteadoString.equals("")) {
			valorTotalCursoComConvenioCusteadoString = emBranco;
		}
		String valorParcelaCursoComConvenioCusteadoString = Uteis.getDoubleFormatado(valorParcelaCursoComConvenioCusteadoDouble);
		if (valorParcelaCursoComConvenioCusteadoString == null || valorParcelaCursoComConvenioCusteadoString.equals("")) {
			valorParcelaCursoComConvenioCusteadoString = emBranco;
		}
		
		ext.setNumber(valorTotalCursoDouble);
		String valorTotalCursoExtenso = ext.toString();
		if (valorTotalCursoExtenso == null || valorTotalCursoExtenso.equals("")) {
			valorTotalCursoExtenso = emBranco;
		}
		

		ext.setNumber(valorTotalCursoComConvenioCusteado);
		String valorTotalCursoComConvenioCusteadoExtenso = ext.toString();
		if (valorTotalCursoComConvenioCusteadoExtenso == null || valorTotalCursoComConvenioCusteadoExtenso.equals("")) {
			valorTotalCursoComConvenioCusteadoExtenso = emBranco;
		}
		ext.setNumber(valorParcelaCursoComConvenioCusteadoDouble);
		String valorParcelaCursoComConvenioCusteadoExtenso = ext.toString();
		if (valorParcelaCursoComConvenioCusteadoExtenso == null || valorParcelaCursoComConvenioCusteadoExtenso.equals("")) {
			valorParcelaCursoComConvenioCusteadoExtenso = emBranco;
		}

		// ValorTotalCursoSemDescontoComMatricula
		String valorTotalCursoSemDescontoComMatricula = Uteis.getDoubleFormatado(valorTotalCursoSemDescontoDouble);
		if (valorTotalCursoSemDescontoComMatricula == null || valorTotalCursoSemDescontoComMatricula.equals("")) {
			valorTotalCursoSemDescontoComMatricula = emBranco;
		}

		if (valorTotalCursoSemDescontoDouble < valorMatriculaCursoDouble) {
			valorTotalCursoSemDescontoDouble = valorMatriculaCursoDouble;
		}
		ext.setNumber(valorTotalCursoSemDescontoDouble);
		String valorTotalCursoSemDescontoComMatriculaExtenso = ext.toString();
		if (valorTotalCursoSemDescontoComMatriculaExtenso == null || valorTotalCursoSemDescontoComMatriculaExtenso.equals("")) {
			valorTotalCursoSemDescontoComMatriculaExtenso = emBranco;
		}

		//

		String valorTotalCursoSemDescontoSemMatricula = Uteis.getDoubleFormatado(valorTotalCursoSemDescontoDouble - valorMatriculaCursoDouble);
		if (valorTotalCursoSemDescontoSemMatricula == null || valorTotalCursoSemDescontoSemMatricula.equals("")) {
			valorTotalCursoSemDescontoSemMatricula = emBranco;
		}

		if (valorTotalCursoSemDescontoDouble < valorMatriculaCursoDouble) {
			valorTotalCursoSemDescontoDouble = valorMatriculaCursoDouble;
		}
		ext.setNumber(valorTotalCursoSemDescontoDouble - valorMatriculaCursoDouble);
		String valorTotalCursoSemDescontoSemMatriculaExtenso = ext.toString();
		if (valorTotalCursoSemDescontoSemMatriculaExtenso == null || valorTotalCursoSemDescontoSemMatriculaExtenso.equals("")) {
			valorTotalCursoSemDescontoSemMatriculaExtenso = emBranco;
		}

		if (valorTotalCursoDouble > 0) {
			ext.setNumber(valorTotalCursoDouble / 2);
		} else {
			ext.setNumber(valorTotalCursoDouble);
		}
		String valorTotalCursoExtensoDivididoPorDois = ext.toString();
		if (valorTotalCursoExtensoDivididoPorDois == null || valorTotalCursoExtensoDivididoPorDois.equals("")) {
			valorTotalCursoExtensoDivididoPorDois = emBranco;
		}

		if (valorTotalCursoDouble > 0) {
			ext.setNumber(valorTotalCursoDouble / 3);
		} else {
			ext.setNumber(valorTotalCursoDouble);
		}
		String valorTotalCursoExtensoDivididoPorTres = ext.toString();
		if (valorTotalCursoExtensoDivididoPorTres == null || valorTotalCursoExtensoDivididoPorTres.equals("")) {
			valorTotalCursoExtensoDivididoPorTres = emBranco;
		}

		String valorTotalCursoSemMatricula = Uteis.getDoubleFormatado(valorTotalCursoDouble - valorMatriculaCursoDouble);
		if (valorTotalCursoSemMatricula == null || valorTotalCursoSemMatricula.equals("")) {
			valorTotalCursoSemMatricula = emBranco;
		}
		ext.setNumber(valorTotalCursoDouble - valorMatriculaCursoDouble);
		String valorTotalCursoSemMatriculaExtenso = ext.toString();
		if (valorTotalCursoSemMatriculaExtenso == null || valorTotalCursoSemMatriculaExtenso.equals("")) {
			valorTotalCursoSemMatriculaExtenso = emBranco;
		}

		if ((valorTotalCursoDouble - valorMatriculaCursoDouble) > 0) {
			ext.setNumber((valorTotalCursoDouble - valorMatriculaCursoDouble) / 2);
		} else {
			ext.setNumber(valorTotalCursoDouble - valorMatriculaCursoDouble);
		}
		String valorTotalCursoSemMatriculaDivididoPorDoisExtenso = ext.toString();
		if (valorTotalCursoSemMatriculaDivididoPorDoisExtenso == null || valorTotalCursoSemMatriculaDivididoPorDoisExtenso.equals("")) {
			valorTotalCursoSemMatriculaDivididoPorDoisExtenso = emBranco;
		}

		if ((valorTotalCursoDouble - valorMatriculaCursoDouble) > 0) {
			ext.setNumber((valorTotalCursoDouble - valorMatriculaCursoDouble) / 3);
		} else {
			ext.setNumber(valorTotalCursoDouble - valorMatriculaCursoDouble);
		}
		String valorTotalCursoSemMatriculaDivididoPorTresExtenso = ext.toString();
		if (valorTotalCursoSemMatriculaDivididoPorTresExtenso == null || valorTotalCursoSemMatriculaDivididoPorTresExtenso.equals("")) {
			valorTotalCursoSemMatriculaDivididoPorTresExtenso = emBranco;
		}
		
		String valorTotalCursoApenasDescontosInstituicao = Uteis.getDoubleFormatado(valorTotalCursoApenasDescontosInstituicaoDouble);
		if (valorTotalCursoApenasDescontosInstituicao == null || valorTotalCursoApenasDescontosInstituicao.equals("")) {
			valorTotalCursoApenasDescontosInstituicao = emBranco;
		}
		
		ext.setNumber(valorTotalCursoApenasDescontosInstituicaoDouble);
		String valorTotalCursoApenasDescontosInstituicaoExtenso = ext.toString();
		if (valorTotalCursoApenasDescontosInstituicaoExtenso == null || valorTotalCursoApenasDescontosInstituicaoExtenso.equals("")) {
			valorTotalCursoApenasDescontosInstituicaoExtenso = emBranco;
		}
		
		String valorTotalDescontosInstituicao = Uteis.getDoubleFormatado(valorTotalDescontosInstituicaoDouble);
		if (valorTotalDescontosInstituicao == null || valorTotalDescontosInstituicao.equals("")) {
			valorTotalDescontosInstituicao = emBranco;
		}
		
		ext.setNumber(valorTotalDescontosInstituicaoDouble);
		String valorTotalDescontosInstituicaoExtenso = ext.toString();
		if (valorTotalDescontosInstituicaoExtenso == null || valorTotalDescontosInstituicaoExtenso.equals("")) {
			valorTotalDescontosInstituicaoExtenso = emBranco;
		}

		DescontoProgressivoVO desc = matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getDescontoProgressivoPadrao();
		String nrDiasAntesVcto1 = desc.getDiaLimite1().toString();
		if (nrDiasAntesVcto1 == null || nrDiasAntesVcto1.equals("")) {
			nrDiasAntesVcto1 = emBranco;
		}
		ext.setNumber(new Double(desc.getDiaLimite1()));
		String nrDiasAntesVcto1Extenso = ext.toStringNaoMonetario();
		if (nrDiasAntesVcto1Extenso == null || nrDiasAntesVcto1Extenso.equals("")) {
			nrDiasAntesVcto1Extenso = emBranco;
		}
		String descontoDiasAntesVcto1 = Uteis.getDoubleFormatado(desc.getPercDescontoLimite1());
		if (descontoDiasAntesVcto1 == null || descontoDiasAntesVcto1.equals("")) {
			descontoDiasAntesVcto1 = emBranco;
		}
		ext.setNumber(desc.getPercDescontoLimite1());
		String descontoDiasAntesVcto1Extenso = ext.toString();
		if (descontoDiasAntesVcto1Extenso == null || descontoDiasAntesVcto1Extenso.equals("")) {
			descontoDiasAntesVcto1Extenso = emBranco;
		}
		String descontoProgressivoValorDiasAntesVcto1 = Uteis.getDoubleFormatado(desc.getValorDescontoLimite1());
		if (descontoProgressivoValorDiasAntesVcto1 == null || descontoProgressivoValorDiasAntesVcto1.equals("")) {
			descontoProgressivoValorDiasAntesVcto1 = emBranco;
		}
		ext.setNumber(desc.getValorDescontoLimite1());
		String descontoProgressivoValorDiasAntesVcto1Extenso = ext.toString();
		if (descontoProgressivoValorDiasAntesVcto1Extenso == null || descontoProgressivoValorDiasAntesVcto1Extenso.equals("")) {
			descontoProgressivoValorDiasAntesVcto1Extenso = emBranco;
		}
		String valorParcelaComDescontoConvenioSegundaFaixa_Matricula = Uteis.getDoubleFormatado(valorParcelaComDescontoConvenioSegundaFaixa);
		if (valorParcelaComDescontoConvenio_Matricula == null || valorParcelaComDescontoConvenio_Matricula.equals("")) {
			valorParcelaComDescontoConvenio_Matricula = emBranco;
		}
		String valorParcelaComDescontoConvenioTerceiraFaixa_Matricula = Uteis.getDoubleFormatado(valorParcelaComDescontoConvenioTerceiraFaixa);
		if (valorParcelaComDescontoConvenio_Matricula == null || valorParcelaComDescontoConvenio_Matricula.equals("")) {
			valorParcelaComDescontoConvenio_Matricula = emBranco;
		}
		getListaTipoDescontoCurso().clear();
		String tabelaTipoDescontoBolsaCusteada = "";
		Iterator e = planoFinanceiroAluno.getItemPlanoFinanceiroAlunoVOs().iterator();
		while (e.hasNext()) {
			ItemPlanoFinanceiroAlunoVO item = (ItemPlanoFinanceiroAlunoVO)e.next();
			if (item.getTipoItemPlanoFinanceiro().equals("CO") && item.getConvenio().getAbaterDescontoNoValorParcela() && contareceberprimeiroparcela != null) {
				tabelaTipoDescontoBolsaCusteada += " <tr><td>Desconto Convênio </td><td>" + item.getConvenio().getDescricao() + "</td><td align='Right'> R$ " + Uteis.getDoubleFormatado(contareceberprimeiroparcela.getValorCusteadoContaReceber()) + " </td></tr>";
				getListaTipoDescontoCurso().add(new DescontoTipoDescontoRelVO("Desconto Convênio", item.getConvenio().getDescricao(), contareceberprimeiroparcela.getValorCusteadoContaReceber()));
			}
		}
		String tabelaListaContaReceberComDescontos = "";
		getListaContaReceberComDescontos().clear();
		MatriculaPeriodoVencimentoVO mpvTemp = null;
		int cont = 0;
		for (MatriculaPeriodoVencimentoVO mpv : matriculaPeriodo.getMatriculaPeriodoVencimentoVOs()) {
			if (cont == 0) {
				tabelaListaContaReceberComDescontos = "<table style=\"width:100%\" border=1 cellspacing=0 cellpadding=1 bordercolor=\"000000\"><tr><th>Parcela    </th><th>Data Vcto    </th><th>Valor    </th><th>Convênio    </th><th>Desc. Instituição    </th><th>Outros Descontos    </th></tr>";
				cont++;
			} else {
					tabelaListaContaReceberComDescontos += " <tr  align=center ><td> " + mpv.getContaReceber().getParcela() + " </td><td> " + mpv.getContaReceber().getDataVencimento_Apresentar() + " </td><td align='center'> R$ " + Uteis.getDoubleFormatado(mpv.getContaReceber().getValorBaseContaReceber()) + " </td><td align='center'> R$ " + Uteis.getDoubleFormatado(mpv.getContaReceber().getValorDescontoConvenio()) + " </td><td align='center'> R$ " + Uteis.getDoubleFormatado(mpv.getContaReceber().getValorDescontoInstituicao()) + " </td><td align='center'> R$ " + Uteis.getDoubleFormatado(mpv.getContaReceber().getValorDesconto()) + " </td></tr>";
					mpvTemp = new MatriculaPeriodoVencimentoVO();
					mpvTemp.setParcela(mpv.getContaReceber().getParcela());
					mpvTemp.setDataVencimento(mpv.getContaReceber().getDataVencimento());
					mpvTemp.setValor(mpv.getContaReceber().getValorBaseContaReceber());
					mpvTemp.setValorDescontoConvenio(mpv.getContaReceber().getValorDescontoConvenio());
					mpvTemp.setValorDescontoInstituicao(mpv.getContaReceber().getValorDescontoInstituicao());
					mpvTemp.setValorDesconto(mpv.getContaReceber().getValorDesconto());
					getListaContaReceberComDescontos().add(mpvTemp);
			}
		}
		tabelaListaContaReceberComDescontos += "</table>";		
		Double valorDescontoAPagar1 = 0.0;
		Double valorDescontoAPagar2 = 0.0;
		Double valorDescontoAPagar3 = 0.0;
		Double valorDescontoAPagar4 = 0.0;
		Double valorTotalDescontoDou = 0.0;
		Double valorTotalDescontoDou1 = 0.0;
		Double valorTotalDescontoDou2 = 0.0;
		Double valorTotalDescontoDou3 = 0.0;
		Double valorTotalDescontoDou4 = 0.0;
		Double valorTotalDescontoSemValidade = 0.0;
		Double valorTotalPlanoDescontoSemValidade = 0.0;
		Double valorDescAplicar = 0.0;
		Double valorDescAluno = 0.0;
		Double valorDescConvenio = 0.0;
		Double valorBase = 0.0;
		Double valorDescPlanoDesconto = 0.0;
		Double valorDescPlanoDescontoComValidade = 0.0;
		Double valorDescPlanoDescontoAteDataVencimento = 0.0;
		Double valorPlanoDescontoComValidadeAteDataVencimento = 0.0;
		Double valorParcelaCursoComDescontoSemValidade = 0.0;
		Double valorParcelaCursoComPlanoDescontoSemValidade = 0.0;
		Double valorTotalDescontoComValidade = 0.0;
		Double valorParcelaCursoBaseDescontoComValidade = 0.0;
		Double percentualDescontoConvenio = 0.0;
		Double valorParcelaCalculadoDescAntecipacaoConvenio = 0.0;
		String tabelaTipoDesconto = "";
		String tabelaTipoDescontoTodos = "";
		getListaTodosTipoDesconto_Curso().clear();

		cont = 0;
		for (PlanoFinanceiroAlunoDescricaoDescontosVO plano : matriculaPeriodo.getListaDescontosMensalidade()) {
			if (cont == 0) {
				tabelaTipoDesconto = "<table border=1 cellspacing=0 cellpadding=1 bordercolor=\"000000\"><tr><th>Tipo Desconto</th><th>Desconto</th><th>Valor do Desconto</th></tr>";
				tabelaTipoDescontoTodos = "<table border=1 cellspacing=0 cellpadding=1 bordercolor=\"000000\"><tr><th>Tipo Desconto</th><th>Desconto</th><th>Valor do Desconto</th></tr>";				
				if (!plano.getValorDescontoProgressivo().equals(0.0)) {
					tabelaTipoDesconto += " <tr><td>Desconto Progressivo</td><td> " + planoFinanceiroAluno.getDescontoProgressivo().getNome() + " </td><td align='Right'> R$ " + Uteis.getDoubleFormatado(plano.getValorDescontoProgressivo()) + " </td></tr>";
					getListaTipoDescontoCurso().add(new DescontoTipoDescontoRelVO("Desconto Progressivo", planoFinanceiroAluno.getDescontoProgressivo().getNome(), plano.getValorDescontoProgressivo()));
				}
				if (plano.getDescontoProgressivoVO() != null && plano.getDescontoProgressivoVO().getCodigo() > 0) {
					tabelaTipoDescontoTodos += " <tr><td>Desconto Progressivo </td><td> " + planoFinanceiroAluno.getDescontoProgressivo().getNome() + " </td><td align='Right'> R$ " + Uteis.getDoubleFormatado(plano.getValorDescontoProgressivo()) + " </td></tr>";
					getListaTodosTipoDesconto_Curso().add(new DescontoTipoDescontoRelVO("Desconto Progressivo", planoFinanceiroAluno.getDescontoProgressivo().getNome(), plano.getValorDescontoProgressivo()));
				}
				if (!plano.getValorDescontoAluno().equals(0.0)) {
					tabelaTipoDesconto += " <tr><td>Desconto Aluno </td><td>Desconto Plano Financeiro Aluno</td><td align='Right'> R$ " + Uteis.getDoubleFormatado(plano.getValorDescontoAluno()) + " </td></tr>";
					getListaTipoDescontoCurso().add(new DescontoTipoDescontoRelVO("Desconto Aluno", "Desconto Plano Financeiro Aluno", plano.getValorDescontoAluno()));
				}
				if (!matricula.getPlanoFinanceiroAluno().getValorDescontoParcela().equals(0.0)) {
					tabelaTipoDescontoTodos += " <tr><td>Desconto Aluno </td><td>Desconto Plano Financeiro Aluno</td><td align='Right'> R$ " + Uteis.getDoubleFormatado(plano.getValorDescontoAluno()) + " </td></tr>";
					getListaTodosTipoDesconto_Curso().add(new DescontoTipoDescontoRelVO("Desconto Aluno", "Desconto Plano Financeiro Aluno", plano.getValorDescontoAluno()));
				}				
				if (!plano.getValorDescontoConvenio().equals(0.0)) {
					StringBuilder descricaoConvenio = new StringBuilder();
					List<ItemPlanoFinanceiroAlunoVO> listaItemPlanoFinanceiroAlunoVOs = planoFinanceiroAluno.getItemPlanoFinanceiroAlunoVOs("CO");
					boolean virgula = false;
					for (ItemPlanoFinanceiroAlunoVO itemPlanoFinanceiroAlunoConvenioVO : listaItemPlanoFinanceiroAlunoVOs) {
						if (!virgula) {
							descricaoConvenio.append(itemPlanoFinanceiroAlunoConvenioVO.getConvenio().getDescricao());
						} else {
							descricaoConvenio.append(", " + itemPlanoFinanceiroAlunoConvenioVO.getConvenio().getDescricao());
						}
						virgula = true;
					}
					getListaTipoDescontoCurso().add(new DescontoTipoDescontoRelVO("Desconto Convênio", descricaoConvenio.toString(), plano.getValorDescontoConvenio()));
					tabelaTipoDesconto += " <tr><td>Desconto Convênio </td><td>" + descricaoConvenio + "</td><td align='Right'> R$ " + Uteis.getDoubleFormatado(plano.getValorDescontoConvenio()) + " </td></tr>";
					getListaTodosTipoDesconto_Curso().add(new DescontoTipoDescontoRelVO("Desconto Convênio", descricaoConvenio.toString(), plano.getValorDescontoConvenio()+plano.getValorCusteadoContaReceber()));
					tabelaTipoDescontoTodos += " <tr><td>Desconto Convênio </td><td>" + descricaoConvenio + "</td><td align='Right'> R$ " + Uteis.getDoubleFormatado(plano.getValorDescontoConvenio()+plano.getValorCusteadoContaReceber()) + " </td></tr>";					
				} else if (plano.getValorCusteadoContaReceber().doubleValue() > 0) {
					StringBuilder descricaoConvenio = new StringBuilder();
					List<ItemPlanoFinanceiroAlunoVO> listaItemPlanoFinanceiroAlunoVOs = planoFinanceiroAluno.getItemPlanoFinanceiroAlunoVOs("CO");
					boolean virgula = false;
					for (ItemPlanoFinanceiroAlunoVO itemPlanoFinanceiroAlunoConvenioVO : listaItemPlanoFinanceiroAlunoVOs) {
						if (!virgula) {
							descricaoConvenio.append(itemPlanoFinanceiroAlunoConvenioVO.getConvenio().getDescricao());
						} else {
							descricaoConvenio.append(", " + itemPlanoFinanceiroAlunoConvenioVO.getConvenio().getDescricao());
						}
						virgula = true;
					}
					getListaTodosTipoDesconto_Curso().add(new DescontoTipoDescontoRelVO("Desconto Convênio", descricaoConvenio.toString(), plano.getValorDescontoConvenio()+plano.getValorCusteadoContaReceber()));
					tabelaTipoDescontoTodos += " <tr><td>Desconto Convênio </td><td>" + descricaoConvenio + "</td><td align='Right'> R$ " + Uteis.getDoubleFormatado(plano.getValorDescontoConvenio()+plano.getValorCusteadoContaReceber()) + " </td></tr>";
				}
				if (!plano.getValorDescontoInstituicao().equals(0.0)) {
					StringBuilder descricaoPlanoDesconto = new StringBuilder();
					List<ItemPlanoFinanceiroAlunoVO> listaItemPlanoFinanceiroAlunoVOs = planoFinanceiroAluno.getItemPlanoFinanceiroAlunoVOs("PD");
					boolean virgula = false;
					for (ItemPlanoFinanceiroAlunoVO itemPlanoFinanceiroAlunoConvenioVO : listaItemPlanoFinanceiroAlunoVOs) {
						if (!virgula) {
							descricaoPlanoDesconto.append(itemPlanoFinanceiroAlunoConvenioVO.getPlanoDesconto().getNome());
						} else {
							descricaoPlanoDesconto.append(", " + itemPlanoFinanceiroAlunoConvenioVO.getPlanoDesconto().getNome());
						}
						virgula = true;
					}
					getListaTipoDescontoCurso().add(new DescontoTipoDescontoRelVO("Desconto Instituição", descricaoPlanoDesconto.toString(), plano.getValorDescontoInstituicao()));
					tabelaTipoDesconto += " <tr><td>Desconto Instituição </td><td>" + descricaoPlanoDesconto + "</td><td align='Right'> R$ " + Uteis.getDoubleFormatado(plano.getValorDescontoInstituicao()) + " </td></tr>";
				}
				if (!planoFinanceiroAluno.getItemPlanoFinanceiroAlunoVOs().isEmpty()) {
					StringBuilder descricaoPlanoDesconto = new StringBuilder();
					List<ItemPlanoFinanceiroAlunoVO> listaItemPlanoFinanceiroAlunoVOs = planoFinanceiroAluno.getItemPlanoFinanceiroAlunoVOs();
					if (!plano.getListaDescontosPlanoDesconto().isEmpty() && plano.getListaDescontosPlanoDesconto().values().size() > 1) {
						for (ItemPlanoFinanceiroAlunoVO itemPlanoFinanceiroAlunoVO : listaItemPlanoFinanceiroAlunoVOs) {
							if (itemPlanoFinanceiroAlunoVO.getTipoItemPlanoFinanceiro().equals("PD") && plano.getListaDescontosPlanoDesconto().containsKey(itemPlanoFinanceiroAlunoVO.getPlanoDesconto().getCodigo())) {
								Double valorPlanoFinanceiro = plano.getListaDescontosPlanoDesconto().get(itemPlanoFinanceiroAlunoVO.getPlanoDesconto().getCodigo());
								getListaTodosTipoDesconto_Curso().add(new DescontoTipoDescontoRelVO("Desconto Instituição", itemPlanoFinanceiroAlunoVO.getPlanoDesconto().getNome(), valorPlanoFinanceiro));
								tabelaTipoDescontoTodos += " <tr><td>Desconto Instituição </td><td>" + itemPlanoFinanceiroAlunoVO.getPlanoDesconto().getNome() + "</td><td align='Right'> R$ " + Uteis.getDoubleFormatado(valorPlanoFinanceiro) + " </td></tr>";
							}
						}
					} else {
						descricaoPlanoDesconto.append(listaItemPlanoFinanceiroAlunoVOs.stream().filter(p -> p.getTipoItemPlanoFinanceiro().equals("PD")).map(p -> p.getPlanoDesconto().getNome()).collect(Collectors.joining(", ")));
						getListaTodosTipoDesconto_Curso().add(new DescontoTipoDescontoRelVO("Desconto Instituição", descricaoPlanoDesconto.toString(), plano.getValorDescontoInstituicao()));
						tabelaTipoDescontoTodos += " <tr><td>Desconto Instituição </td><td>" + descricaoPlanoDesconto + "</td><td align='Right'> R$ " + Uteis.getDoubleFormatado(plano.getValorDescontoInstituicao()) + " </td></tr>";
					}
				}	
				tabelaTipoDesconto += "</table>";
				tabelaTipoDescontoTodos += "</table>";

				valorDescontoAPagar1 = plano.getValorDescontoProgressivo();
				valorTotalDescontoDou1 = plano.getValorDescontoAluno().doubleValue() + plano.getValorDescontoConvenio().doubleValue() + plano.getValorDescontoInstituicao().doubleValue() + plano.getValorDescontoProgressivo().doubleValue();
				valorDescAluno = plano.getValorDescontoAluno().doubleValue();
				valorDescConvenio = plano.getValorDescontoConvenio();
				valorBase = plano.getValorBase();
				Double valorBaseAbatendoDesconto = valorBase;

				if (planoFinanceiroAluno.getOrdemConvenio().equals(0)) {
					if (valorBase > 0) {
						percentualDescontoConvenio = Uteis.arrendondarForcando2CadasDecimais((valorDescConvenio / valorBase) * 100);
					}
				} else {
					if (planoFinanceiroAluno.getOrdemConvenio() > planoFinanceiroAluno.getOrdemPlanoDesconto()) {
						if (!planoFinanceiroAluno.getOrdemConvenioValorCheio()) {
							valorBaseAbatendoDesconto = valorBaseAbatendoDesconto - plano.getValorDescontoInstituicao();
							if (valorBaseAbatendoDesconto.equals(0.0) || valorBaseAbatendoDesconto < 0.0) {
								percentualDescontoConvenio = 0.0;
							} else {
								percentualDescontoConvenio = Uteis.arrendondarForcando2CadasDecimais((valorDescConvenio / valorBaseAbatendoDesconto) * 100);
							}
						} else {
							valorBaseAbatendoDesconto = valorBaseAbatendoDesconto - plano.getValorDescontoInstituicao();
							if (valorBaseAbatendoDesconto.equals(0.0) || valorBaseAbatendoDesconto < 0.0) {
								percentualDescontoConvenio = 0.0;
							} else {
								percentualDescontoConvenio = Uteis.arrendondarForcando2CadasDecimais((valorDescConvenio / valorBase) * 100);
							}
						}
					}
					if (planoFinanceiroAluno.getOrdemConvenio() > planoFinanceiroAluno.getOrdemDescontoAluno()) {
						if (!planoFinanceiroAluno.getOrdemConvenioValorCheio()) {
							valorBaseAbatendoDesconto = valorBaseAbatendoDesconto - plano.getValorDescontoAluno();
							if (valorBaseAbatendoDesconto.equals(0.0) || valorBaseAbatendoDesconto < 0.0) {
								percentualDescontoConvenio = 0.0;
							} else {
								percentualDescontoConvenio = Uteis.arrendondarForcando2CadasDecimais((valorDescConvenio / valorBaseAbatendoDesconto) * 100);
							}
						} else {
							valorBaseAbatendoDesconto = valorBaseAbatendoDesconto - plano.getValorDescontoAluno();
							if (valorBaseAbatendoDesconto.equals(0.0) || valorBaseAbatendoDesconto < 0.0) {
								percentualDescontoConvenio = 0.0;
							} else {
								percentualDescontoConvenio = Uteis.arrendondarForcando2CadasDecimais((valorDescConvenio / valorBase) * 100);
							}
						}
					}
					if (planoFinanceiroAluno.getOrdemConvenio() > planoFinanceiroAluno.getOrdemDescontoProgressivo()) {
						if (!planoFinanceiroAluno.getOrdemConvenioValorCheio()) {
							valorBaseAbatendoDesconto = valorBaseAbatendoDesconto - plano.getValorDescontoProgressivo();
							if (valorBaseAbatendoDesconto.equals(0.0) || valorBaseAbatendoDesconto < 0.0) {
								percentualDescontoConvenio = 0.0;
							} else {
								percentualDescontoConvenio = Uteis.arrendondarForcando2CadasDecimais((valorDescConvenio / valorBaseAbatendoDesconto) * 100);
							}
						} else {
							valorBaseAbatendoDesconto = valorBaseAbatendoDesconto - plano.getValorDescontoProgressivo();
							if (valorBaseAbatendoDesconto.equals(0.0) || valorBaseAbatendoDesconto < 0.0) {
								percentualDescontoConvenio = 0.0;
							} else {
								percentualDescontoConvenio = Uteis.arrendondarForcando2CadasDecimais((valorDescConvenio / valorBase) * 100);
							}
						}
					}
				}

			} else if (cont == 1) {
				valorDescontoAPagar2 = plano.getValorDescontoProgressivo();
				valorTotalDescontoDou2 = plano.getValorDescontoAluno().doubleValue() + plano.getValorDescontoConvenio().doubleValue() + plano.getValorDescontoInstituicao().doubleValue() + plano.getValorDescontoProgressivo().doubleValue();
			} else if (cont == 2) {
				valorDescontoAPagar3 = plano.getValorDescontoProgressivo();
				valorTotalDescontoDou3 = plano.getValorDescontoAluno().doubleValue() + plano.getValorDescontoConvenio().doubleValue() + plano.getValorDescontoInstituicao().doubleValue() + plano.getValorDescontoProgressivo().doubleValue();
			} else if (cont == 3) {
				valorDescontoAPagar4 = plano.getValorDescontoProgressivo();
				valorTotalDescontoDou4 = plano.getValorDescontoAluno().doubleValue() + plano.getValorDescontoConvenio().doubleValue() + plano.getValorDescontoInstituicao().doubleValue() + plano.getValorDescontoProgressivo().doubleValue();
			}
			if (valorTotalDescontoDou.doubleValue() == 0) {
				valorTotalDescontoDou = plano.getValorDescontoAluno().doubleValue() + plano.getValorDescontoConvenio().doubleValue() + plano.getValorDescontoInstituicao().doubleValue() + plano.getValorDescontoProgressivo().doubleValue();
			}
			cont++;
		}

		double valorDescAntecipacaoParceiro = 0;
		for (ItemPlanoFinanceiroAlunoVO itemPlano : planoFinanceiroAluno.getItemPlanoFinanceiroAlunoVOs()) {
			if (itemPlano.getTipoItemPlanoFinanceiro().equals("CO")) {
				valorDescAntecipacaoParceiro = calcularPrimeiroDiaAntecipacaoConvenio(itemPlano.getConvenio().getDescontoProgressivoParceiro(), valorBase);
			}
		}

		Double valorParcelaPrimeiroDiaAntecipacaoConvenio = valorBase - (valorDescontoAPagar1 + valorDescAntecipacaoParceiro);
		valorParcelaPrimeiroDiaAntecipacaoConvenio = Uteis.arrendondarForcando2CadasDecimais(valorParcelaPrimeiroDiaAntecipacaoConvenio.doubleValue());
		String valorParcelaPrimeiroDiaAntecipacaoConvenioStr = Uteis.getDoubleFormatado(valorParcelaPrimeiroDiaAntecipacaoConvenio);
		if (valorParcelaPrimeiroDiaAntecipacaoConvenioStr == null || valorParcelaPrimeiroDiaAntecipacaoConvenioStr.equals("")) {
			valorParcelaPrimeiroDiaAntecipacaoConvenioStr = emBranco;
		}
		ext.setNumber(valorParcelaPrimeiroDiaAntecipacaoConvenio);
		String valorParcelaPrimeiroDiaAntecipacaoConvenioStrExtenso = ext.toString();
		if (valorParcelaPrimeiroDiaAntecipacaoConvenioStrExtenso == null || valorParcelaPrimeiroDiaAntecipacaoConvenioStrExtenso.equals("")) {
			valorParcelaPrimeiroDiaAntecipacaoConvenioStrExtenso = emBranco;
		}

		Double valorDescontoAPagarSemDescontoConvenio = valorTotalDescontoDou1 - valorDescConvenio;
		valorDescontoAPagarSemDescontoConvenio = Uteis.arrendondarForcando2CadasDecimais(valorDescontoAPagarSemDescontoConvenio.doubleValue());
		if (valorDescontoAPagarSemDescontoConvenio < 0) {
			valorDescontoAPagarSemDescontoConvenio = 0.0;
		}
		String valorDescontoAPagarSemDescontoConvenioStr = Uteis.getDoubleFormatado(valorDescontoAPagarSemDescontoConvenio);
		if (valorDescontoAPagarSemDescontoConvenioStr == null || valorDescontoAPagarSemDescontoConvenioStr.equals("")) {
			valorDescontoAPagarSemDescontoConvenioStr = emBranco;
		}
		ext.setNumber(valorDescontoAPagarSemDescontoConvenio);
		String valorDescontoAPagarSemDescontoConvenioStrExtenso = ext.toString();
		if (valorDescontoAPagarSemDescontoConvenioStrExtenso == null || valorDescontoAPagarSemDescontoConvenioStrExtenso.equals("")) {
			valorDescontoAPagarSemDescontoConvenioStrExtenso = emBranco;
		}

		valorTotalPlanoDescontoSemValidade = obterValorDescontoSemValidade(listaPlanoDesconto, valorBase);

		// Responsável por verificar o valor do Desconto sem validade
		if (planoFinanceiroAluno.getOrdemConvenio() < planoFinanceiroAluno.getOrdemPlanoDesconto() && planoFinanceiroAluno.getOrdemDescontoAluno() < planoFinanceiroAluno.getOrdemPlanoDesconto() && !planoFinanceiroAluno.getOrdemPlanoDescontoValorCheio()) {
			valorTotalDescontoSemValidade = obterValorDescontoSemValidade(listaPlanoDesconto, valorBase - valorDescConvenio.doubleValue() - valorDescAluno.doubleValue()) + valorDescAluno.doubleValue() + valorDescConvenio.doubleValue();
		} else if (planoFinanceiroAluno.getOrdemDescontoAluno() < planoFinanceiroAluno.getOrdemPlanoDesconto() && !planoFinanceiroAluno.getOrdemPlanoDescontoValorCheio()) {
			valorTotalDescontoSemValidade = obterValorDescontoSemValidade(listaPlanoDesconto, valorBase - valorDescAluno.doubleValue()) + valorDescAluno.doubleValue() + valorDescConvenio.doubleValue();
		} else if (planoFinanceiroAluno.getOrdemConvenio() < planoFinanceiroAluno.getOrdemPlanoDesconto() && !planoFinanceiroAluno.getOrdemPlanoDescontoValorCheio()) {
			valorTotalDescontoSemValidade = obterValorDescontoSemValidade(listaPlanoDesconto, valorBase - valorDescConvenio.doubleValue()) + valorDescAluno.doubleValue() + valorDescConvenio.doubleValue();
		} else {
			valorTotalDescontoSemValidade = obterValorDescontoSemValidade(listaPlanoDesconto, valorBase) + valorDescAluno.doubleValue() + valorDescConvenio.doubleValue();
		}

		Double valorCalculadoDescontoSemValidade = Uteis.arrendondarForcando2CadasDecimais(valorBase - valorTotalDescontoSemValidade);

		valorDescPlanoDescontoComValidade = obterValorParcelaDescontoComValidade(listaPlanoDesconto, valorCalculadoDescontoSemValidade);
		Double valorParcelaCursoCalculadoDescontoComValidade = Uteis.arrendondarForcando2CadasDecimais(valorBase - valorDescPlanoDescontoComValidade);

		// Calcula valor PARCELA curso com validade
		valorParcelaCursoBaseDescontoComValidade = Uteis.arrendondarForcando2CadasDecimais(valorParcelaCursoCalculadoDescontoComValidade.doubleValue());
		String valorParcelaCursoComValidade = Uteis.getDoubleFormatado(valorParcelaCursoBaseDescontoComValidade);
		if (valorParcelaCursoComValidade == null || valorParcelaCursoComValidade.equals("")) {
			valorParcelaCursoComValidade = emBranco;
		}
		ext.setNumber(valorParcelaCursoBaseDescontoComValidade);
		String valorParcelaCursoComValidadeExtenso = ext.toString();
		if (valorParcelaCursoComValidadeExtenso == null || valorParcelaCursoComValidadeExtenso.equals("")) {
			valorParcelaCursoComValidadeExtenso = emBranco;
		}

		// VALOR DESCONTO PLANO DESCONTO VÁLIDO ATE A DATA DE VENCIMENTO
		valorDescPlanoDescontoAteDataVencimento = obterValorPlanoDescontoAteDataVencimento(listaPlanoDesconto, valorCalculadoDescontoSemValidade);
		Double valorPlanoDescontoAteDataVencimento = Uteis.arrendondarForcando2CadasDecimais(valorDescPlanoDescontoAteDataVencimento);

		valorPlanoDescontoComValidadeAteDataVencimento = Uteis.arrendondarForcando2CadasDecimais(valorPlanoDescontoAteDataVencimento.doubleValue());
		String valorDescontoAteDataVencimento = Uteis.getDoubleFormatado(valorPlanoDescontoComValidadeAteDataVencimento);
		if (valorDescontoAteDataVencimento == null || valorDescontoAteDataVencimento.equals("")) {
			valorDescontoAteDataVencimento = emBranco;
		}
		ext.setNumber(valorPlanoDescontoComValidadeAteDataVencimento);
		String valorPlanoDescontoAteDataVencimentoExtenso = ext.toString();
		if (valorPlanoDescontoAteDataVencimentoExtenso == null || valorPlanoDescontoAteDataVencimentoExtenso.equals("")) {
			valorPlanoDescontoAteDataVencimentoExtenso = emBranco;
		}
		// FIM DESCONTO PLANO DESCONTO VÁLIDO ATE A DATA DE VENCIMENTO

		String valorPercentualDescontoConvenio = String.valueOf(percentualDescontoConvenio.intValue());
		if (valorPercentualDescontoConvenio == null || valorPercentualDescontoConvenio.equals("")) {
			valorPercentualDescontoConvenio = emBranco;
		}

		// Calcula valor DESCONTO curso com validade
		valorTotalDescontoComValidade = Uteis.arrendondarForcando2CadasDecimais(valorDescPlanoDescontoComValidade.doubleValue());
		String valorDescontoDescontoComValidade = Uteis.getDoubleFormatado(valorTotalDescontoComValidade);
		if (valorDescontoDescontoComValidade == null || valorDescontoDescontoComValidade.equals("")) {
			valorDescontoDescontoComValidade = emBranco;
		}
		ext.setNumber(valorTotalDescontoComValidade);
		String valorDescontoComValidadeExtenso = ext.toString();
		if (valorDescontoComValidadeExtenso == null || valorDescontoComValidadeExtenso.equals("")) {
			valorDescontoComValidadeExtenso = emBranco;
		}

		valorParcelaCursoComDescontoSemValidade = Uteis.arrendondarForcando2CadasDecimais(valorBase - valorTotalDescontoSemValidade);
		String valorParcelaComDescontoSemValidade = Uteis.getDoubleFormatado(valorParcelaCursoComDescontoSemValidade);
		if (valorParcelaComDescontoSemValidade == null || valorParcelaComDescontoSemValidade.equals("")) {
			valorParcelaComDescontoSemValidade = emBranco;
		}
		if (valorParcelaCursoComDescontoSemValidade < 0) {
			valorParcelaCursoComDescontoSemValidade = 0.0;
		}
		ext.setNumber(valorParcelaCursoComDescontoSemValidade);
		String valorParcelaComDescontoSemValidadeExtenso = ext.toString();
		if (valorParcelaComDescontoSemValidadeExtenso == null || valorParcelaComDescontoSemValidadeExtenso.equals("")) {
			valorParcelaComDescontoSemValidadeExtenso = emBranco;
		}

		valorParcelaCursoComPlanoDescontoSemValidade = Uteis.arrendondarForcando2CadasDecimais(valorBase - valorTotalPlanoDescontoSemValidade);
		String valorParcelaComPlanoDescontoSemValidade = Uteis.getDoubleFormatado(valorParcelaCursoComPlanoDescontoSemValidade);
		if (valorParcelaComPlanoDescontoSemValidade == null || valorParcelaComPlanoDescontoSemValidade.equals("")) {
			valorParcelaComPlanoDescontoSemValidade = emBranco;
		}
		if (valorParcelaCursoComPlanoDescontoSemValidade < 0) {
			valorParcelaCursoComPlanoDescontoSemValidade = 0.0;
		}
		ext.setNumber(valorParcelaCursoComPlanoDescontoSemValidade);
		String valorParcelaComPlanoDescontoSemValidadeExtenso = ext.toString();
		if (valorParcelaComPlanoDescontoSemValidadeExtenso == null || valorParcelaComPlanoDescontoSemValidadeExtenso.equals("")) {
			valorParcelaComPlanoDescontoSemValidadeExtenso = emBranco;
		}

		Double valorDescontoDiasAntesVcto1Double = valorDescontoAPagar1;
		String valorDescontoDiasAntesVcto1 = Uteis.getDoubleFormatado(valorDescontoDiasAntesVcto1Double);
		if (valorDescontoDiasAntesVcto1 == null || valorDescontoDiasAntesVcto1.equals("")) {
			valorDescontoDiasAntesVcto1 = emBranco;
		}
		ext.setNumber(valorDescontoDiasAntesVcto1Double);
		String valorDescontoDiasAntesVcto1Extenso = ext.toString();
		if (valorDescontoDiasAntesVcto1Extenso == null || valorDescontoDiasAntesVcto1Extenso.equals("")) {
			valorDescontoDiasAntesVcto1Extenso = emBranco;
		}

		String valorDescontoSemValidade_Curso = Uteis.getDoubleFormatado(valorTotalDescontoSemValidade);
		if (valorDescontoSemValidade_Curso == null || valorDescontoSemValidade_Curso.equals("")) {
			valorDescontoSemValidade_Curso = emBranco;
		}
		ext.setNumber(valorTotalDescontoSemValidade);
		String valorDescontoSemValidadeExtenso = ext.toString();
		if (valorDescontoSemValidadeExtenso == null || valorDescontoSemValidadeExtenso.equals("")) {
			valorDescontoSemValidadeExtenso = emBranco;
		}

		String valorPlanoDescontoSemValidade_Curso = Uteis.getDoubleFormatado(valorTotalPlanoDescontoSemValidade);
		if (valorPlanoDescontoSemValidade_Curso == null || valorPlanoDescontoSemValidade_Curso.equals("")) {
			valorPlanoDescontoSemValidade_Curso = emBranco;
		}
		ext.setNumber(valorTotalPlanoDescontoSemValidade);
		String valorPlanoDescontoSemValidadeExtenso = ext.toString();
		if (valorPlanoDescontoSemValidadeExtenso == null || valorPlanoDescontoSemValidadeExtenso.equals("")) {
			valorPlanoDescontoSemValidadeExtenso = emBranco;
		}

		String formaIngresso_Curso = matricula.getFormaIngresso_Apresentar();
		if (formaIngresso_Curso == null || formaIngresso_Curso.equals("")) {
			formaIngresso_Curso = emBranco;
		}

		// String formaIngresso_Curso = matricula.getFormaIngresso_Apresentar();
		// if (formaIngresso_Curso == null || formaIngresso_Curso .equals("")) {
		// formaIngresso_Curso = emBranco;
		// }

		// Se for PROUNI zera a o valor a pagar
		Double valorAPagarDescontoDiasAntesVcto1Double = 0.0;
		Double valorAPagarDescontoDiasAntesVcto1ApenasProgressivoDouble = 0.0;
		// if (matricula.getFormaIngresso().equals("PR")) {
		// valorAPagarDescontoDiasAntesVcto1Double = 0.0;
		// valorAPagarDescontoDiasAntesVcto1ApenasProgressivoDouble = 0.0;
		// } else {
		valorAPagarDescontoDiasAntesVcto1Double = valorParcelaCursoDouble - valorDescontoAPagar1;
		valorAPagarDescontoDiasAntesVcto1ApenasProgressivoDouble = valorParcelaCursoDouble - ((desc.getPercDescontoLimite1() / 100) * valorParcelaCursoDouble);
		// }
		String valorAPagarDescontoDiasAntesVcto1 = Uteis.getDoubleFormatado(valorAPagarDescontoDiasAntesVcto1Double);
		if (valorAPagarDescontoDiasAntesVcto1 == null || valorAPagarDescontoDiasAntesVcto1.equals("")) {
			valorAPagarDescontoDiasAntesVcto1 = emBranco;
		}
		String valorAPagarDescontoDiasAntesVcto1ApenasProgressivo = Uteis.getDoubleFormatado(valorAPagarDescontoDiasAntesVcto1ApenasProgressivoDouble);
		if (valorAPagarDescontoDiasAntesVcto1ApenasProgressivo == null || valorAPagarDescontoDiasAntesVcto1ApenasProgressivo.equals("")) {
			valorAPagarDescontoDiasAntesVcto1ApenasProgressivo = emBranco;
		}
		Date dataVencimentoParcela = new Date();
		Date diaVcto1Date = new Date();
		Date dataVencimentoParcelaDate = new Date();
		String diaVcto1 = "";
		String diaVctoVencimentoPrimeiraParcela = "";
		if (matriculaPeriodo.getProcessoMatriculaCalendarioVO().getMesVencimentoPrimeiraMensalidade() == null || matriculaPeriodo.getProcessoMatriculaCalendarioVO().getMesVencimentoPrimeiraMensalidade() == null || matriculaPeriodo.getProcessoMatriculaCalendarioVO().getAnoVencimentoPrimeiraMensalidade() == null) {
		} else {
			if (diaVencimentoParcela != null) {
				dataVencimentoParcela = diaVencimentoParcela;
				diaVcto1Date = Uteis.obterDataFutura(dataVencimentoParcela, desc.getDiaLimite1());
				dataVencimentoParcelaDate = Uteis.obterDataAntiga(dataVencimentoParcela, desc.getDiaLimite1());
				diaVcto1 = Uteis.getData(diaVcto1Date);
				diaVctoVencimentoPrimeiraParcela = Uteis.getData(dataVencimentoParcelaDate);
				if (diaVcto1 == null || diaVcto1.equals("")) {
					diaVcto1 = emBranco;
				}
			}
		}
		String diaVctoVencimentoPrimeiraParcelaExtenso = "";
		if (!diaVctoVencimentoPrimeiraParcela.equals("")) {
			int diaData = Uteis.getDiaMesData(Uteis.getDate(diaVctoVencimentoPrimeiraParcela));
			diaVctoVencimentoPrimeiraParcela = String.valueOf(diaData);

			ext.setNumber(Double.valueOf(diaData));
			ext.setUsarBranco(true);
			diaVctoVencimentoPrimeiraParcelaExtenso = ext.toString();
			ext.setUsarBranco(false);
		}
		if (valorAPagarDescontoDiasAntesVcto1Double < 0) {
			valorAPagarDescontoDiasAntesVcto1Double = 0.0;
		}
		ext.setNumber(valorAPagarDescontoDiasAntesVcto1Double);
		String valorAPagarDescontoDiasAntesVcto1Extenso = ext.toString();
		if (valorAPagarDescontoDiasAntesVcto1Extenso == null || valorAPagarDescontoDiasAntesVcto1Extenso.equals("")) {
			valorAPagarDescontoDiasAntesVcto1Extenso = emBranco;
		}

		String nrDiasAntesVcto2 = desc.getDiaLimite2().toString();
		if (nrDiasAntesVcto2 == null || nrDiasAntesVcto2.equals("")) {
			nrDiasAntesVcto2 = emBranco;
		}
		ext.setNumber(new Double(desc.getDiaLimite2()));
		String nrDiasAntesVcto2Extenso = ext.toStringNaoMonetario();
		if (nrDiasAntesVcto2Extenso == null || nrDiasAntesVcto2Extenso.equals("")) {
			nrDiasAntesVcto2Extenso = emBranco;
		}
		String descontoDiasAntesVcto2 = Uteis.getDoubleFormatado(desc.getPercDescontoLimite2());
		if (descontoDiasAntesVcto2 == null || descontoDiasAntesVcto2.equals("")) {
			descontoDiasAntesVcto2 = emBranco;
		}
		ext.setNumber(desc.getPercDescontoLimite2());
		String descontoDiasAntesVcto2Extenso = ext.toString();
		if (descontoDiasAntesVcto2Extenso == null || descontoDiasAntesVcto2Extenso.equals("")) {
			descontoDiasAntesVcto2Extenso = emBranco;
		}
		String descontoProgressivoValorDiasAntesVcto2 = Uteis.getDoubleFormatado(desc.getValorDescontoLimite2());
		if (descontoProgressivoValorDiasAntesVcto2 == null || descontoProgressivoValorDiasAntesVcto2.equals("")) {
			descontoProgressivoValorDiasAntesVcto2 = emBranco;
		}
		ext.setNumber(desc.getValorDescontoLimite2());
		String descontoProgressivoValorDiasAntesVcto2Extenso = ext.toString();
		if (descontoProgressivoValorDiasAntesVcto2Extenso == null || descontoProgressivoValorDiasAntesVcto2Extenso.equals("")) {
			descontoProgressivoValorDiasAntesVcto2Extenso = emBranco;
		}

		Double valorDescontoDiasAntesVcto2Double = valorDescontoAPagar2;
		String valorDescontoDiasAntesVcto2 = Uteis.getDoubleFormatado(valorDescontoDiasAntesVcto2Double);
		if (valorDescontoDiasAntesVcto2 == null || valorDescontoDiasAntesVcto2.equals("")) {
			valorDescontoDiasAntesVcto2 = emBranco;
		}
		ext.setNumber(valorDescontoDiasAntesVcto2Double);
		String valorDescontoDiasAntesVcto2Extenso = ext.toString();
		if (valorDescontoDiasAntesVcto2Extenso == null || valorDescontoDiasAntesVcto2Extenso.equals("")) {
			valorDescontoDiasAntesVcto2Extenso = emBranco;
		}

		// Se for PROUNI zera a o valor a pagar
		Double valorAPagarDescontoDiasAntesVcto2Double = 0.0;
		Double valorAPagarDescontoDiasAntesVcto2ApenasProgressivoDouble = 0.0;
		// if (matricula.getFormaIngresso().equals("PR")) {
		// valorAPagarDescontoDiasAntesVcto2Double = 0.0;
		// valorAPagarDescontoDiasAntesVcto2ApenasProgressivoDouble = 0.0;
		// } else {
		valorAPagarDescontoDiasAntesVcto2Double = valorParcelaCursoDouble - valorDescontoAPagar2;
		valorAPagarDescontoDiasAntesVcto2ApenasProgressivoDouble = valorParcelaCursoDouble - ((desc.getPercDescontoLimite2() / 100) * valorParcelaCursoDouble);
		// }
		String valorAPagarDescontoDiasAntesVcto2 = Uteis.getDoubleFormatado(valorAPagarDescontoDiasAntesVcto2Double);
		if (valorAPagarDescontoDiasAntesVcto2 == null || valorAPagarDescontoDiasAntesVcto2.equals("")) {
			valorAPagarDescontoDiasAntesVcto2 = emBranco;
		}
		String valorAPagarDescontoDiasAntesVcto2ApenasProgressivo = Uteis.getDoubleFormatado(valorAPagarDescontoDiasAntesVcto2ApenasProgressivoDouble);
		if (valorAPagarDescontoDiasAntesVcto2ApenasProgressivo == null || valorAPagarDescontoDiasAntesVcto2ApenasProgressivo.equals("")) {
			valorAPagarDescontoDiasAntesVcto2ApenasProgressivo = emBranco;
		}
		Date diaVcto2Date = Uteis.obterDataFutura(dataVencimentoParcela, desc.getDiaLimite2());
		String diaVcto2 = Uteis.getData(diaVcto2Date);
		if (diaVcto2 == null || diaVcto2.equals("")) {
			diaVcto2 = emBranco;
		}
		if (valorAPagarDescontoDiasAntesVcto2Double < 0) {
			valorAPagarDescontoDiasAntesVcto2Double = 0.0;
		}
		ext.setNumber(valorAPagarDescontoDiasAntesVcto2Double);
		String valorAPagarDescontoDiasAntesVcto2Extenso = ext.toString();
		if (valorAPagarDescontoDiasAntesVcto2Extenso == null || valorAPagarDescontoDiasAntesVcto2Extenso.equals("")) {
			valorAPagarDescontoDiasAntesVcto2Extenso = emBranco;
		}

		String nrDiasAntesVcto3 = desc.getDiaLimite3().toString();
		if (nrDiasAntesVcto3 == null || nrDiasAntesVcto3.equals("")) {
			nrDiasAntesVcto3 = emBranco;
		}
		ext.setNumber(new Double(desc.getDiaLimite3()));
		String nrDiasAntesVcto3Extenso = ext.toStringNaoMonetario();
		if (nrDiasAntesVcto3Extenso == null || nrDiasAntesVcto3Extenso.equals("")) {
			nrDiasAntesVcto3Extenso = emBranco;
		}
		String descontoDiasAntesVcto3 = Uteis.getDoubleFormatado(desc.getPercDescontoLimite3());
		if (descontoDiasAntesVcto3 == null || descontoDiasAntesVcto3.equals("")) {
			descontoDiasAntesVcto3 = emBranco;
		}
		ext.setNumber(desc.getPercDescontoLimite3());
		String descontoDiasAntesVcto3Extenso = ext.toString();
		if (descontoDiasAntesVcto3Extenso == null || descontoDiasAntesVcto3Extenso.equals("")) {
			descontoDiasAntesVcto3Extenso = emBranco;
		}
		String descontoProgressivoValorDiasAntesVcto3 = Uteis.getDoubleFormatado(desc.getValorDescontoLimite3());
		if (descontoProgressivoValorDiasAntesVcto3 == null || descontoProgressivoValorDiasAntesVcto3.equals("")) {
			descontoProgressivoValorDiasAntesVcto3 = emBranco;
		}
		ext.setNumber(desc.getValorDescontoLimite3());
		String descontoProgressivoValorDiasAntesVcto3Extenso = ext.toString();
		if (descontoProgressivoValorDiasAntesVcto3Extenso == null || descontoProgressivoValorDiasAntesVcto3Extenso.equals("")) {
			descontoProgressivoValorDiasAntesVcto3Extenso = emBranco;
		}
		Double valorDescontoDiasAntesVcto3Double = valorDescontoAPagar3;
		String valorDescontoDiasAntesVcto3 = Uteis.getDoubleFormatado(valorDescontoDiasAntesVcto3Double);
		if (valorDescontoDiasAntesVcto3 == null || valorDescontoDiasAntesVcto3.equals("")) {
			valorDescontoDiasAntesVcto3 = emBranco;
		}
		ext.setNumber(valorDescontoDiasAntesVcto3Double);
		String valorDescontoDiasAntesVcto3Extenso = ext.toString();
		if (valorDescontoDiasAntesVcto3Extenso == null || valorDescontoDiasAntesVcto3Extenso.equals("")) {
			valorDescontoDiasAntesVcto3Extenso = emBranco;
		}

		// Se for PROUNI zera a o valor a pagar
		Double valorAPagarDescontoDiasAntesVcto3Double = 0.0;
		Double valorAPagarDescontoDiasAntesVcto3ApenasProgressivoDouble = 0.0;
		// if (matricula.getFormaIngresso().equals("PR")) {
		// valorAPagarDescontoDiasAntesVcto3Double = 0.0;
		// valorAPagarDescontoDiasAntesVcto3ApenasProgressivoDouble = 0.0;
		// } else {
		valorAPagarDescontoDiasAntesVcto3Double = valorParcelaCursoDouble - valorDescontoAPagar3;
		valorAPagarDescontoDiasAntesVcto3ApenasProgressivoDouble = valorParcelaCursoDouble - ((desc.getPercDescontoLimite3() / 100) * valorParcelaCursoDouble);
		// }
		String valorAPagarDescontoDiasAntesVcto3 = Uteis.getDoubleFormatado(valorAPagarDescontoDiasAntesVcto3Double);
		if (valorAPagarDescontoDiasAntesVcto3 == null || valorAPagarDescontoDiasAntesVcto3.equals("")) {
			valorAPagarDescontoDiasAntesVcto3 = emBranco;
		}
		String valorAPagarDescontoDiasAntesVcto3ApenasProgressivo = Uteis.getDoubleFormatado(valorAPagarDescontoDiasAntesVcto3ApenasProgressivoDouble);
		if (valorAPagarDescontoDiasAntesVcto3ApenasProgressivo == null || valorAPagarDescontoDiasAntesVcto3ApenasProgressivo.equals("")) {
			valorAPagarDescontoDiasAntesVcto3ApenasProgressivo = emBranco;
		}
		Date diaVcto3Date = Uteis.obterDataFutura(dataVencimentoParcela, desc.getDiaLimite3());
		String diaVcto3 = Uteis.getData(diaVcto3Date);
		if (diaVcto3 == null || diaVcto3.equals("")) {
			diaVcto3 = emBranco;
		}
		if (valorAPagarDescontoDiasAntesVcto3Double < 0) {
			valorAPagarDescontoDiasAntesVcto3Double = 0.0;
		}
		ext.setNumber(valorAPagarDescontoDiasAntesVcto3Double);
		String valorAPagarDescontoDiasAntesVcto3Extenso = ext.toString();
		if (valorAPagarDescontoDiasAntesVcto3Extenso == null || valorAPagarDescontoDiasAntesVcto3Extenso.equals("")) {
			valorAPagarDescontoDiasAntesVcto3Extenso = emBranco;
		}

		String nrDiasAntesVcto4 = desc.getDiaLimite4().toString();
		if (nrDiasAntesVcto4 == null || nrDiasAntesVcto4.equals("")) {
			nrDiasAntesVcto4 = emBranco;
		}
		ext.setNumber(new Double(desc.getDiaLimite4()));
		String nrDiasAntesVcto4Extenso = ext.toStringNaoMonetario();
		if (nrDiasAntesVcto4Extenso == null || nrDiasAntesVcto4Extenso.equals("")) {
			nrDiasAntesVcto4Extenso = emBranco;
		}
		String descontoDiasAntesVcto4 = Uteis.getDoubleFormatado(desc.getPercDescontoLimite4());
		if (descontoDiasAntesVcto4 == null || descontoDiasAntesVcto4.equals("")) {
			descontoDiasAntesVcto4 = emBranco;
		}
		ext.setNumber(desc.getPercDescontoLimite4());
		String descontoDiasAntesVcto4Extenso = ext.toString();
		if (descontoDiasAntesVcto4Extenso == null || descontoDiasAntesVcto4Extenso.equals("")) {
			descontoDiasAntesVcto4Extenso = emBranco;
		}
		String descontoProgressivoValorDiasAntesVcto4 = Uteis.getDoubleFormatado(desc.getValorDescontoLimite4());
		if (descontoProgressivoValorDiasAntesVcto4 == null || descontoProgressivoValorDiasAntesVcto4.equals("")) {
			descontoProgressivoValorDiasAntesVcto4 = emBranco;
		}
		ext.setNumber(desc.getValorDescontoLimite4());
		String descontoProgressivoValorDiasAntesVcto4Extenso = ext.toString();
		if (descontoProgressivoValorDiasAntesVcto4Extenso == null || descontoProgressivoValorDiasAntesVcto4Extenso.equals("")) {
			descontoProgressivoValorDiasAntesVcto4Extenso = emBranco;
		}

		Double valorDescontoDiasAntesVcto4Double = valorDescontoAPagar4;
		String valorDescontoDiasAntesVcto4 = Uteis.getDoubleFormatado(valorDescontoDiasAntesVcto4Double);
		if (valorDescontoDiasAntesVcto4 == null || valorDescontoDiasAntesVcto4.equals("")) {
			valorDescontoDiasAntesVcto4 = emBranco;
		}
		ext.setNumber(valorDescontoDiasAntesVcto4Double);
		String valorDescontoDiasAntesVcto4Extenso = ext.toString();
		if (valorDescontoDiasAntesVcto4Extenso == null || valorDescontoDiasAntesVcto4Extenso.equals("")) {
			valorDescontoDiasAntesVcto4Extenso = emBranco;
		}

		// Se for PROUNI zera a o valor a pagar
		Double valorAPagarDescontoDiasAntesVcto4Double = 0.0;
		Double valorAPagarDescontoDiasAntesVcto4ApenasProgressivoDouble = 0.0;
		// if (matricula.getFormaIngresso().equals("PR")) {
		// valorAPagarDescontoDiasAntesVcto4Double = 0.0;
		// valorAPagarDescontoDiasAntesVcto4ApenasProgressivoDouble = 0.0;
		// } else {
		valorAPagarDescontoDiasAntesVcto4Double = valorParcelaCursoDouble - valorDescontoAPagar4;
		valorAPagarDescontoDiasAntesVcto4ApenasProgressivoDouble = valorParcelaCursoDouble - ((desc.getPercDescontoLimite4() / 100) * valorParcelaCursoDouble);
		// }
		String valorAPagarDescontoDiasAntesVcto4 = Uteis.getDoubleFormatado(valorAPagarDescontoDiasAntesVcto4Double);
		if (valorAPagarDescontoDiasAntesVcto4 == null || valorAPagarDescontoDiasAntesVcto4.equals("")) {
			valorAPagarDescontoDiasAntesVcto4 = emBranco;
		}
		String valorAPagarDescontoDiasAntesVcto4ApenasProgressivo = Uteis.getDoubleFormatado(valorAPagarDescontoDiasAntesVcto4ApenasProgressivoDouble);
		if (valorAPagarDescontoDiasAntesVcto4ApenasProgressivo == null || valorAPagarDescontoDiasAntesVcto4ApenasProgressivo.equals("")) {
			valorAPagarDescontoDiasAntesVcto4ApenasProgressivo = emBranco;
		}
		Date diaVcto4Date = Uteis.obterDataFutura(dataVencimentoParcela, desc.getDiaLimite4());
		String diaVcto4 = Uteis.getData(diaVcto4Date);
		if (diaVcto4 == null || diaVcto4.equals("")) {
			diaVcto4 = emBranco;
		}
		if (valorAPagarDescontoDiasAntesVcto4Double < 0) {
			valorAPagarDescontoDiasAntesVcto4Double = 0.0;
		}
		ext.setNumber(valorAPagarDescontoDiasAntesVcto4Double);
		String valorAPagarDescontoDiasAntesVcto4Extenso = ext.toString();
		if (valorAPagarDescontoDiasAntesVcto4Extenso == null || valorAPagarDescontoDiasAntesVcto4Extenso.equals("")) {
			valorAPagarDescontoDiasAntesVcto4Extenso = emBranco;
		}

		Double valorTotalDescontoDouble = valorTotalDescontoDou;
		String valorTotalDesconto = Uteis.getDoubleFormatado(valorTotalDescontoDouble);
		if (valorTotalDesconto == null || valorTotalDesconto.equals("")) {
			valorTotalDesconto = emBranco;
		}
		ext.setNumber(valorTotalDescontoDouble);
		String valorTotalDescontoExtenso = ext.toString();
		if (valorTotalDescontoExtenso == null || valorTotalDescontoExtenso.equals("")) {
			valorTotalDescontoExtenso = emBranco;
		}

		Double valorTotalDescontoDouble1 = valorTotalDescontoDou1;
		String valorTotalDesconto1 = Uteis.getDoubleFormatado(valorTotalDescontoDouble1);
		if (valorTotalDesconto1 == null || valorTotalDesconto1.equals("")) {
			valorTotalDesconto1 = emBranco;
		}
		ext.setNumber(valorTotalDescontoDouble1);
		String valorTotalDescontoExtenso1 = ext.toString();
		if (valorTotalDescontoExtenso1 == null || valorTotalDescontoExtenso1.equals("")) {
			valorTotalDescontoExtenso1 = emBranco;
		}
		Double valorTotalDescontoDouble2 = valorTotalDescontoDou2;
		String valorTotalDesconto2 = Uteis.getDoubleFormatado(valorTotalDescontoDouble2);
		if (valorTotalDesconto2 == null || valorTotalDesconto2.equals("")) {
			valorTotalDesconto2 = emBranco;
		}
		ext.setNumber(valorTotalDescontoDouble2);
		String valorTotalDescontoExtenso2 = ext.toString();
		if (valorTotalDescontoExtenso2 == null || valorTotalDescontoExtenso2.equals("")) {
			valorTotalDescontoExtenso2 = emBranco;
		}
		Double valorTotalDescontoDouble3 = valorTotalDescontoDou3;
		String valorTotalDesconto3 = Uteis.getDoubleFormatado(valorTotalDescontoDouble3);
		if (valorTotalDesconto3 == null || valorTotalDesconto3.equals("")) {
			valorTotalDesconto3 = emBranco;
		}
		ext.setNumber(valorTotalDescontoDouble3);
		String valorTotalDescontoExtenso3 = ext.toString();
		if (valorTotalDescontoExtenso3 == null || valorTotalDescontoExtenso3.equals("")) {
			valorTotalDescontoExtenso3 = emBranco;
		}
		Double valorTotalDescontoDouble4 = valorTotalDescontoDou4;
		String valorTotalDesconto4 = Uteis.getDoubleFormatado(valorTotalDescontoDouble4);
		if (valorTotalDesconto4 == null || valorTotalDesconto4.equals("")) {
			valorTotalDesconto4 = emBranco;
		}
		ext.setNumber(valorTotalDescontoDouble4);
		String valorTotalDescontoExtenso4 = ext.toString();
		if (valorTotalDescontoExtenso4 == null || valorTotalDescontoExtenso4.equals("")) {
			valorTotalDescontoExtenso4 = emBranco;
		}

		String duracaoCurso = matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getDescricaoDuracao();
		if (duracaoCurso == null || duracaoCurso.equals("")) {
			duracaoCurso = emBranco;
		}

		String descricaoCondicaoCurso = matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getDescricao();
		if (descricaoCondicaoCurso == null || descricaoCondicaoCurso.equals("")) {
			descricaoCondicaoCurso = emBranco;
		}

		String nrParcelasPeriodo = matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getNrParcelasPeriodo().toString();
		if (nrParcelasPeriodo == null || nrParcelasPeriodo.equals("")) {
			nrParcelasPeriodo = emBranco;
		}

		String periodoLetivoCurso = matriculaPeriodo.getPeridoLetivo().getDescricao();
		if (periodoLetivoCurso == null || periodoLetivoCurso.equals("")) {
			periodoLetivoCurso = emBranco;
		}
		String CondicaoPagamentoCurso = matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getDescricao();
		if (CondicaoPagamentoCurso == null || CondicaoPagamentoCurso.equals("")) {
			CondicaoPagamentoCurso = emBranco;
		}
		String dataInicioProgAula = Uteis.getData(matriculaPeriodo.getDataInicioAula());
		if (dataInicioProgAula == null || dataInicioProgAula.equals("")) {
			dataInicioProgAula = emBranco;
		}
		String dataFinalProgAula = Uteis.getData(matriculaPeriodo.getDataFinalAula());
		if (dataFinalProgAula == null || dataFinalProgAula.equals("")) {
			dataFinalProgAula = emBranco;
		}
		String dataInicioCurso = Uteis.getData(impressaoContratoVO.getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataInicioPeriodoLetivo());
		if (dataInicioCurso == null || dataInicioCurso.equals("")) {
			dataInicioCurso = Uteis.getData(matricula.getDataInicioCurso());
		}
		
		String dataExtensoInicioCurso = Uteis.getDataCidadeDiaMesPorExtensoEAno("", matricula.getDataInicioCurso(), true);
		if (dataExtensoInicioCurso == null || dataExtensoInicioCurso.equals("")) {
			dataExtensoInicioCurso = emBranco;
		}
		String dataExtensoInicioCurso2 = Uteis.getDataCidadeDiaPorExtensoMesPorExtensoEAnoPorExtenso("", matricula.getDataInicioCurso(), true);
		if (dataExtensoInicioCurso2 == null || dataExtensoInicioCurso2.equals("")) {
			dataExtensoInicioCurso2 = emBranco;
		}
		String dataFinalCurso = Uteis.getData(impressaoContratoVO.getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataFimPeriodoLetivo());
		if (dataFinalCurso == null || dataFinalCurso.equals("")) {
			dataFinalCurso = Uteis.getData(matricula.getDataConclusaoCurso());
		}
		String dataExtensoFinalCurso = Uteis.getDataCidadeDiaMesPorExtensoEAno("", matricula.getDataConclusaoCurso(), true);
		if (dataExtensoFinalCurso == null || dataExtensoFinalCurso.equals("")) {
			dataExtensoFinalCurso = emBranco;
		}
		String dataExtensoFinalCurso2 = Uteis.getDataCidadeDiaPorExtensoMesPorExtensoEAnoPorExtenso("", matricula.getDataConclusaoCurso(), true);
		if (dataExtensoFinalCurso2 == null || dataExtensoFinalCurso2.equals("")) {
			dataExtensoFinalCurso2 = emBranco;
		}
		String anoConclusaoCurso = Uteis.getAno(matricula.getDataConclusaoCurso());
		if (anoConclusaoCurso == null || anoConclusaoCurso.equals("")) {
			anoConclusaoCurso = emBranco;
		}
		String totalSemestresCurso = ((Integer) impressaoContratoVO.getMatriculaPeriodoVO().getGradeCurricular().getPeriodoLetivosVOs().size()).toString();
		if (totalSemestresCurso == null || totalSemestresCurso.equals("")) {
			totalSemestresCurso = emBranco;
		}
		String anoAtualCurso = Uteis.getAnoDataAtual4Digitos();
		if (anoAtualCurso == null || anoAtualCurso.equals("")) {
			anoAtualCurso = emBranco;
		}
		String semestreAtualCurso = matriculaPeriodo.getSemestre();
		// String semestreAtualCurso = Uteis.getSemestreAtual();
		semestreAtualCurso += "º Semestre";
		if (semestreAtualCurso == null || semestreAtualCurso.equals("")) {
			semestreAtualCurso = emBranco;
		}
		String semestreAtual2Curso = matriculaPeriodo.getSemestre();
		// String semestreAtualCurso = Uteis.getSemestreAtual();
		semestreAtual2Curso += "";
		if (semestreAtual2Curso == null || semestreAtual2Curso.equals("")) {
			semestreAtual2Curso = emBranco;
		}
		String informacoesAdicionais = "";
		if (informacoesAdicionais == null || informacoesAdicionais.equals("0") || informacoesAdicionais.equals("")) {
			informacoesAdicionais = emBranco;
		}
		String textoCurso = "";
		if (textoCurso == null || textoCurso.equals("0") || textoCurso.equals("")) {
			textoCurso = emBranco;
		}
		if (planoFinanceiroAluno.getPercDescontoParcela() == 100.0) {
			valorTotalCursoSemMatricula = "0,00";
			valorTotalCursoSemMatriculaExtenso = "Zero";
			numParcelaCurso = "0,00";
			numParcelaCursoExtenso = "Zero";
			valorParcelaCurso = "0,00";
			valorParcelaCursoExtenso = "Zero";
			valorTotalDesconto = "0,00";
			valorTotalDescontoExtenso = "Zero";
		}

		String nomeCursoOrigem = impressaoContratoVO.getMatriculaOrigem().getCurso().getNome();
		if (nomeCursoOrigem == null || nomeCursoOrigem.equals("0") || nomeCursoOrigem.equals("")) {
			nomeCursoOrigem = emBranco;
		}

		String nomeCursoDestino = impressaoContratoVO.getMatriculaDestino().getCurso().getNome();
		if (nomeCursoDestino == null || nomeCursoDestino.equals("0") || nomeCursoDestino.equals("")) {
			nomeCursoDestino = emBranco;
		}

		String previsaoConclusao = "";
		int semestre = Integer.parseInt(Uteis.getSemestreAtual());
		int ano = Integer.parseInt(Uteis.getAnoDataAtual());
		if (impressaoContratoVO.getQuantidadePeriodoLetivoACursar().equals(0)) {
			if (impressaoContratoVO.getQuantidadeDisciplinasNaoCursadas() > 0) {
				if (curso.getPeriodicidade().equals("SE")) {
					if (semestre < 2) {
						semestre++;
					} else {
						ano++;
						semestre--;
					}
					previsaoConclusao = semestre + "/" + ano;
				} else if (curso.getPeriodicidade().equals("AN")) {
					ano++;
					previsaoConclusao = String.valueOf(ano);
				}
			}
		} else {
			if (curso.getPeriodicidade().equals("AN")) {
				for (int x = 0; x < impressaoContratoVO.getQuantidadePeriodoLetivoACursar(); x++) {
					ano++;
				}
				previsaoConclusao = String.valueOf(ano);
			} else if (curso.getPeriodicidade().equals("SE")) {
				for (int x = 0; x < impressaoContratoVO.getQuantidadePeriodoLetivoACursar(); x++) {
					if (semestre < 2) {
						semestre++;
					} else {
						ano++;
						semestre--;
					}
				}
				previsaoConclusao = semestre + "/" + ano;
			}
		}
		if (previsaoConclusao == null || previsaoConclusao.equals("")) {
			previsaoConclusao = emBranco;
		}
		
		String quantidadeParcelasMaterialDidatico = matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getQuantidadeParcelasMaterialDidatico().toString();
		if (quantidadeParcelasMaterialDidatico == null || quantidadeParcelasMaterialDidatico.equals("")) {
			quantidadeParcelasMaterialDidatico = emBranco;
		}
		
		String valorParcelaMaterialDidatico = Uteis.getDoubleFormatado(matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getValorPorParcelaMaterialDidatico());
		if (valorParcelaMaterialDidatico == null || valorParcelaMaterialDidatico.equals("")) {
			valorParcelaMaterialDidatico = emBranco;
		}
		
		ext.setNumber(matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getValorPorParcelaMaterialDidatico());
		String valorExtensoParcelaMaterialDidatico = ext.toString();
		if(valorExtensoParcelaMaterialDidatico == null || valorExtensoParcelaMaterialDidatico.equals("")) {
			valorExtensoParcelaMaterialDidatico = emBranco;
		}
		
		String valorDescontoParcelaMaterialDidatico = Uteis.getDoubleFormatado(valorDescontoParcelaMaterialDidaticoDoubleValue);
		if (valorDescontoParcelaMaterialDidatico == null || valorDescontoParcelaMaterialDidatico.equals("")) {
			valorDescontoParcelaMaterialDidatico = emBranco;
		}
		
		ext.setNumber(valorDescontoParcelaMaterialDidaticoDoubleValue);
		String valorDescontoExtensoParcelaMaterialDidatico = ext.toString();
		if (valorDescontoExtensoParcelaMaterialDidatico == null || valorDescontoExtensoParcelaMaterialDidatico.equals("")) {
			valorDescontoExtensoParcelaMaterialDidatico = emBranco;
		}
		
		Double totalParcelaMaterialDidaticoDoubleValue = Uteis.arrendondarForcando2CasasDecimais(matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getValorPorParcelaMaterialDidatico() * matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getQuantidadeParcelasMaterialDidatico());
		String valorTotalParcelaMaterialDidatico = Uteis.getDoubleFormatado(totalParcelaMaterialDidaticoDoubleValue);
		if (valorTotalParcelaMaterialDidatico == null || valorTotalParcelaMaterialDidatico.equals("")) {
			valorTotalParcelaMaterialDidatico = emBranco;
		}
		
		ext.setNumber(totalParcelaMaterialDidaticoDoubleValue);
		String valorTotalExtensoParcelaMaterialDidatico = ext.toString();
		if (valorTotalExtensoParcelaMaterialDidatico == null || valorTotalExtensoParcelaMaterialDidatico.equals("")) {
			valorTotalExtensoParcelaMaterialDidatico = emBranco;
		}
		
		Double valorTotalComDescontoParcelaMaterialDidativoDoubleValue = Uteis.arrendondarForcando2CasasDecimais(valorParcelaMaterialDidaticoComDescontoIncondicionalDouble * matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getQuantidadeParcelasMaterialDidatico());
		String valorTotalComDescontoParcelaMaterialDidatico = Uteis.getDoubleFormatado(valorTotalComDescontoParcelaMaterialDidativoDoubleValue);
		if (valorTotalComDescontoParcelaMaterialDidatico == null || valorTotalComDescontoParcelaMaterialDidatico.equals("")) {
			valorTotalComDescontoParcelaMaterialDidatico = emBranco;
		}
		
		ext.setNumber(valorTotalComDescontoParcelaMaterialDidativoDoubleValue);
		String valorTotalComDescontoExtensoParcelaMaterialDidatico = ext.toString();
		if (valorTotalComDescontoExtensoParcelaMaterialDidatico == null || valorTotalComDescontoExtensoParcelaMaterialDidatico.equals("")) {
			valorTotalComDescontoExtensoParcelaMaterialDidatico = emBranco;
		}
		
		String diaBaseVencimentoParcelaMaterialDidatico = matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getDiaBaseVencimentoParcelaOutraUnidade().toString();
		if (diaBaseVencimentoParcelaMaterialDidatico == null || diaBaseVencimentoParcelaMaterialDidatico.equals("")) {
			diaBaseVencimentoParcelaMaterialDidatico = emBranco;
		}
		
		UnidadeEnsinoVO unidadeEnsinoVOMaterialDidatico = null;
		if(matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().isUsarUnidadeEnsinoEspecifica()) {
			unidadeEnsinoVOMaterialDidatico = matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getUnidadeEnsinoFinanceira();
		}else {
			unidadeEnsinoVOMaterialDidatico = matricula.getUnidadeEnsino();
		}
		String unidadeEnsinoMaterialDidatico = unidadeEnsinoVOMaterialDidatico.getNome();
		if(unidadeEnsinoMaterialDidatico == null || unidadeEnsinoMaterialDidatico.equals("")) {
			unidadeEnsinoMaterialDidatico = emBranco;
		}
		
		String enderecoUnidadeEnsinoMaterialDidatico = unidadeEnsinoVOMaterialDidatico.getEnderecoCompleto();
		if(enderecoUnidadeEnsinoMaterialDidatico == null || enderecoUnidadeEnsinoMaterialDidatico.equals("")) {
			enderecoUnidadeEnsinoMaterialDidatico = emBranco;
		}
		
		String cnpjUnidadeEnsinoMaterialDidatico = unidadeEnsinoVOMaterialDidatico.getCNPJ();
		if (cnpjUnidadeEnsinoMaterialDidatico == null || cnpjUnidadeEnsinoMaterialDidatico.equals("")) {
			cnpjUnidadeEnsinoMaterialDidatico = emBranco;
		}

		Long numeroTotalDisciplinasPresencialCursoLong = matriculaPeriodo.getMatriculaPeriodoTumaDisciplinaVOs()
				.stream()
				.filter(mptdvo -> mptdvo.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.PRESENCIAL))
				.map(MatriculaPeriodoTurmaDisciplinaVO::getDisciplina)
				.distinct().count();
		
		Long numeroTotalDisciplinasEADCursoLong = matriculaPeriodo.getMatriculaPeriodoTumaDisciplinaVOs()
				.stream()
				.filter(mptdvo -> mptdvo.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.ON_LINE))
				.map(MatriculaPeriodoTurmaDisciplinaVO::getDisciplina)
				.distinct().count();
		
		Long numeroTotalDisciplinasCursoLong = matriculaPeriodo.getMatriculaPeriodoTumaDisciplinaVOs()
				.stream()
				.map(MatriculaPeriodoTurmaDisciplinaVO::getDisciplina)
				.distinct().count();
		String numeroTotalDisciplinasCurso = String.valueOf(numeroTotalDisciplinasCursoLong);
		
		if (numeroTotalDisciplinasCurso == null || numeroTotalDisciplinasCurso.equals("") || numeroTotalDisciplinasCurso.equals("0")) {
			numeroTotalDisciplinasCurso = emBranco;
		}
		
		String numeroTotalDisciplinasPresencialCurso = String.valueOf(numeroTotalDisciplinasPresencialCursoLong);
		if (numeroTotalDisciplinasPresencialCurso == null || numeroTotalDisciplinasPresencialCurso.equals("") || numeroTotalDisciplinasPresencialCurso.equals("0")) {
			numeroTotalDisciplinasPresencialCurso = emBranco;
		}

		String numeroTotalDisciplinasEADCurso = String.valueOf(numeroTotalDisciplinasEADCursoLong);
		if (numeroTotalDisciplinasEADCurso == null || numeroTotalDisciplinasEADCurso.equals("") || numeroTotalDisciplinasEADCurso.equals("0")) {
			numeroTotalDisciplinasEADCurso = emBranco;
		}
		
		Integer quantidadeParcelasCurso = matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getNrParcelasPeriodo();
		ext.setNumber(quantidadeParcelasCurso);
		String quantidadeParcelasExtensoCurso = ext.toStringNaoMonetario();
		if (quantidadeParcelasExtensoCurso == null || quantidadeParcelasExtensoCurso.equals("") || quantidadeParcelasExtensoCurso.equals("0")) {
			quantidadeParcelasExtensoCurso = emBranco;
		}
		
		Double valorTotalParcelasCondicaoPagamentoDouble = matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getValorParcela() * matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getNrParcelasPeriodo();
		String valorTotalParcelasCondicaoPagamento = Uteis.getDoubleFormatado(valorTotalParcelasCondicaoPagamentoDouble);
		if (valorTotalParcelasCondicaoPagamento == null || valorTotalParcelasCondicaoPagamento.equals("") || valorTotalParcelasCondicaoPagamento.equals("0")) {
			valorTotalParcelasCondicaoPagamento = emBranco;
		}
		
		ext.setNumber(valorTotalParcelasCondicaoPagamentoDouble);
		String valorTotalParcelasCondicaoPagamentoExtenso = ext.toString();
		if (valorTotalParcelasCondicaoPagamentoExtenso == null || valorTotalParcelasCondicaoPagamentoExtenso.equals("") || valorTotalParcelasCondicaoPagamentoExtenso.equals("0")) {
			valorTotalParcelasCondicaoPagamentoExtenso = emBranco;
		}
		
		ext.setNumber(valorDescontoInstitucionalPrimeiraParcela);
		String valorDescontoInstitucionalPrimeiraParcelaExtenso = ext.toString();
		if (valorDescontoInstitucionalPrimeiraParcelaExtenso == null || valorDescontoInstitucionalPrimeiraParcelaExtenso.equals("") || valorDescontoInstitucionalPrimeiraParcelaExtenso.equals("0")) {
			valorDescontoInstitucionalPrimeiraParcelaExtenso = emBranco;
		}
		
		Double valorParcelaComDescontoInstitucionalDouble =  valorParcelaCursoDouble - valorDescontoInstitucionalPrimeiraParcela;
		String valorParcelaComDescontoInstitucional = Uteis.getDoubleFormatado(valorParcelaComDescontoInstitucionalDouble);
		if (valorParcelaComDescontoInstitucional == null || valorParcelaComDescontoInstitucional.equals("") || valorParcelaComDescontoInstitucional.equals("0")) {
			valorParcelaComDescontoInstitucional = emBranco;
		}
		
		ext.setNumber(valorParcelaComDescontoInstitucionalDouble);
		String valorParcelaComDescontoInstitucionalExtenso = ext.toString();
		if (valorParcelaComDescontoInstitucionalExtenso == null || valorParcelaComDescontoInstitucionalExtenso.equals("") || valorParcelaComDescontoInstitucionalExtenso.equals("0")) {
			valorParcelaComDescontoInstitucionalExtenso = emBranco;
		}
		
		String valorTotalParcelasComDescontoIncondicional = Uteis.getDoubleFormatado(valorTotalParcelasComDescontoIncondicionalDouble);
		if (valorTotalParcelasComDescontoIncondicional == null || valorTotalParcelasComDescontoIncondicional.equals("") || valorTotalParcelasComDescontoIncondicional.equals("0")) {
			valorTotalParcelasComDescontoIncondicional = emBranco;
		}
		
		ext.setNumber(valorTotalParcelasComDescontoIncondicionalDouble);
		String valorTotalParcelasComDescontoIncondicionalExtenso = ext.toString();
		if (valorTotalParcelasComDescontoIncondicionalExtenso == null || valorTotalParcelasComDescontoIncondicionalExtenso.equals("") || valorTotalParcelasComDescontoIncondicionalExtenso.equals("0")) {
			valorTotalParcelasComDescontoIncondicionalExtenso = emBranco;
		}
		
		String valorParcelaComDescontoIncondicional = Uteis.getDoubleFormatado(valorParcelaComDescontoIncondicionalDouble);
		if (valorParcelaComDescontoIncondicional == null || valorParcelaComDescontoIncondicional.equals("") || valorParcelaComDescontoIncondicional.equals("0")) {
			valorParcelaComDescontoIncondicional = emBranco;
		}

		ext.setNumber(valorParcelaComDescontoIncondicionalDouble);
		String valorParcelaComDescontoIncondicionalExtenso = ext.toString();
		if (valorParcelaComDescontoIncondicionalExtenso == null || valorParcelaComDescontoIncondicionalExtenso.equals("") || valorParcelaComDescontoIncondicionalExtenso.equals("0")) {
			valorParcelaComDescontoIncondicionalExtenso = emBranco;
		}
		
		String valorParcelaMaterialDidaticoComDescontoIncondicional = Uteis.getDoubleFormatado(valorParcelaMaterialDidaticoComDescontoIncondicionalDouble);
		if (valorParcelaMaterialDidaticoComDescontoIncondicional == null || valorParcelaMaterialDidaticoComDescontoIncondicional.equals("") || valorParcelaMaterialDidaticoComDescontoIncondicional.equals("0")) {
			valorParcelaMaterialDidaticoComDescontoIncondicional = emBranco;
		}
		
		ext.setNumber(valorParcelaMaterialDidaticoComDescontoIncondicionalDouble);
		String valorParcelaMaterialDidaticoComDescontoIncondicionalExtenso = ext.toString();
		if (valorParcelaMaterialDidaticoComDescontoIncondicionalExtenso == null || valorParcelaMaterialDidaticoComDescontoIncondicionalExtenso.equals("") || valorParcelaMaterialDidaticoComDescontoIncondicionalExtenso.equals("0")) {
			valorParcelaMaterialDidaticoComDescontoIncondicionalExtenso = emBranco;
		}
		
		String valorDescontoCondicionalMatriculaStr = Uteis.getDoubleFormatado(valorDescontoCondicionalMatricula);
		if (valorDescontoCondicionalMatriculaStr == null || valorDescontoCondicionalMatriculaStr.equals("") || valorDescontoCondicionalMatriculaStr.equals("0")) {
			valorDescontoCondicionalMatriculaStr = emBranco;
		}
		ext.setNumber(valorDescontoCondicionalMatricula);
		String valorDescontoCondicionalMatriculaExtenso = ext.toString();
		if (valorDescontoCondicionalMatriculaExtenso == null || valorDescontoCondicionalMatriculaExtenso.equals("") || valorDescontoCondicionalMatriculaExtenso.equals("0")) {
			valorDescontoCondicionalMatriculaExtenso = emBranco;
		}
		
		String valorDescontoCondicionalParcelaStr = Uteis.getDoubleFormatado(valorDescontoCondicionalParcela);
		if (valorDescontoCondicionalParcelaStr == null || valorDescontoCondicionalParcelaStr.equals("") || valorDescontoCondicionalParcelaStr.equals("0")) {
			valorDescontoCondicionalParcelaStr = emBranco;
		}
		ext.setNumber(valorDescontoCondicionalParcela);
		String valorDescontoCondicionalParcelaExtenso = ext.toString();
		if (valorDescontoCondicionalParcelaExtenso == null || valorDescontoCondicionalParcelaExtenso.equals("") || valorDescontoCondicionalParcelaExtenso.equals("0")) {
			valorDescontoCondicionalParcelaExtenso = emBranco;
		}
		
		String valorDescontoCondicionalMaterialDidaticoStr = Uteis.getDoubleFormatado(valorDescontoCondicionalMaterialDidatico);
		if (valorDescontoCondicionalMaterialDidaticoStr == null || valorDescontoCondicionalMaterialDidaticoStr.equals("") || valorDescontoCondicionalMaterialDidaticoStr.equals("0")) {
			valorDescontoCondicionalMaterialDidaticoStr = emBranco;
		}
		ext.setNumber(valorDescontoCondicionalMaterialDidatico);
		String valorDescontoCondicionalMaterialDidaticoExtenso = ext.toString();
		if (valorDescontoCondicionalMaterialDidaticoExtenso == null || valorDescontoCondicionalMaterialDidaticoExtenso.equals("") || valorDescontoCondicionalMaterialDidaticoExtenso.equals("0")) {
			valorDescontoCondicionalMaterialDidaticoExtenso = emBranco;
		}
		
		String valorDescontoInCondicionalMaterialDidaticoStr = Uteis.getDoubleFormatado(valorDescontoInCondicionalMaterialDidatico);
		if (valorDescontoInCondicionalMaterialDidaticoStr == null || valorDescontoInCondicionalMaterialDidaticoStr.equals("") || valorDescontoInCondicionalMaterialDidaticoStr.equals("0")) {
			valorDescontoInCondicionalMaterialDidaticoStr = emBranco;
		}
		
		String porcentagemCusteadoConvenioParcelaCursoStr = Uteis.getDoubleFormatado(porcentagemCusteadoConvenioParcelaCurso);
		if (porcentagemCusteadoConvenioParcelaCursoStr == null || porcentagemCusteadoConvenioParcelaCursoStr.equals("") || porcentagemCusteadoConvenioParcelaCursoStr.equals("0")) {
			porcentagemCusteadoConvenioParcelaCursoStr = emBranco;
		}
		porcentagemCusteadoAlunoParcelaCurso = 100.0 - porcentagemCusteadoConvenioParcelaCurso;
		String porcentagemCusteadoAlunoParcelaCursoStr = Uteis.getDoubleFormatado(porcentagemCusteadoAlunoParcelaCurso);
		if (porcentagemCusteadoAlunoParcelaCursoStr == null || porcentagemCusteadoAlunoParcelaCursoStr.equals("") || porcentagemCusteadoAlunoParcelaCursoStr.equals("0")) {
			porcentagemCusteadoAlunoParcelaCursoStr = emBranco;
		}
		String valorTotalCusteadoConvenioParcelaCursoStr = Uteis.getDoubleFormatado(valorTotalCusteadoConvenioParcelaCurso);
		if (valorTotalCusteadoConvenioParcelaCursoStr == null || valorTotalCusteadoConvenioParcelaCursoStr.equals("") || valorTotalCusteadoConvenioParcelaCursoStr.equals("0")) {
			valorTotalCusteadoConvenioParcelaCursoStr = emBranco;
		}
		ext.setNumber(valorTotalCusteadoConvenioParcelaCurso);
		ext.setPrimeiraLetraMaiuscula(true);
		String valorTotalCusteadoConvenioParcelaCursoExtenso = ext.toString();
		if (valorTotalCusteadoConvenioParcelaCursoExtenso == null || valorTotalCusteadoConvenioParcelaCursoExtenso.equals("") || valorTotalCusteadoConvenioParcelaCursoExtenso.equals("0")) {
			valorTotalCusteadoConvenioParcelaCursoExtenso = emBranco;
		}
		
		String valorCusteadoAlunoParcelaMaterialDidaticoCursoStr = Uteis.getDoubleFormatado(valorCusteadoAlunoParcelaMaterialDidaticoCurso);
		if (valorCusteadoAlunoParcelaMaterialDidaticoCursoStr == null || valorCusteadoAlunoParcelaMaterialDidaticoCursoStr.equals("") || valorCusteadoAlunoParcelaMaterialDidaticoCursoStr.equals("0")) {
			valorCusteadoAlunoParcelaMaterialDidaticoCursoStr = emBranco;
		}
		
		String valorCusteadoConvenioParcelaMaterialDidaticoCursoStr = Uteis.getDoubleFormatado(valorCusteadoConvenioParcelaMaterialDidaticoCurso);
		if (valorCusteadoConvenioParcelaMaterialDidaticoCursoStr == null || valorCusteadoConvenioParcelaMaterialDidaticoCursoStr.equals("") || valorCusteadoConvenioParcelaMaterialDidaticoCursoStr.equals("0")) {
			valorCusteadoConvenioParcelaMaterialDidaticoCursoStr = emBranco;
		}
		
		ext.setNumber(valorCusteadoAlunoParcelaMaterialDidaticoCurso);
		ext.setPrimeiraLetraMaiuscula(true);
		String valorCusteadoAlunoParcelaMaterialDidaticoCursoExtenso = ext.toString();
		if (valorCusteadoAlunoParcelaMaterialDidaticoCursoExtenso == null || valorCusteadoAlunoParcelaMaterialDidaticoCursoExtenso.equals("") || valorCusteadoAlunoParcelaMaterialDidaticoCursoExtenso.equals("0")) {
			valorCusteadoAlunoParcelaMaterialDidaticoCursoExtenso = emBranco;
		}
		
		ext.setNumber(valorCusteadoConvenioParcelaMaterialDidaticoCurso);
		ext.setPrimeiraLetraMaiuscula(true);
		String valorCusteadoConvenioParcelaMaterialDidaticoCursoExtenso = ext.toString();
		if (valorCusteadoConvenioParcelaMaterialDidaticoCursoExtenso == null || valorCusteadoConvenioParcelaMaterialDidaticoCursoExtenso.equals("") || valorCusteadoConvenioParcelaMaterialDidaticoCursoExtenso.equals("0")) {
			valorCusteadoConvenioParcelaMaterialDidaticoCursoExtenso = emBranco;
		}
		
//		ASSINATURA DIGITAL FUNCIONARIO PRINCIPAL
		String assinaturaDigitalfuncionarioPrincipal = "";
		if (impressaoContratoVO.getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getPastaBaseArquivoEnum() != null) {
			assinaturaDigitalfuncionarioPrincipal = "<img src=\"" + impressaoContratoVO.getConfiguracaoGeralSistemaVO().getUrlExternoDownloadArquivo() + File.separator + impressaoContratoVO.getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getPastaBaseArquivoEnum().getValue() + File.separator + impressaoContratoVO.getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getNome() + "\" style=\"max-height:48px\">";
		}
		if (assinaturaDigitalfuncionarioPrincipal == null || assinaturaDigitalfuncionarioPrincipal.equals("0") || assinaturaDigitalfuncionarioPrincipal.equals("")) {
			assinaturaDigitalfuncionarioPrincipal = emBranco;
		}
//		ASSINATURA DIGITAL FUNCIONARIO PRINCIPAL IREPORT		
		String assinaturaDigitalfuncionarioPrincipalIreport = "";
		if (impressaoContratoVO.getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getPastaBaseArquivoEnum() != null) {
			assinaturaDigitalfuncionarioPrincipalIreport = impressaoContratoVO.getConfiguracaoGeralSistemaVO().getUrlExternoDownloadArquivo() + File.separator + impressaoContratoVO.getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getPastaBaseArquivoEnum().getValue() + File.separator + impressaoContratoVO.getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getNome();
		}
		if (assinaturaDigitalfuncionarioPrincipalIreport == null || assinaturaDigitalfuncionarioPrincipalIreport.equals("0") || assinaturaDigitalfuncionarioPrincipalIreport.equals("")) {
			assinaturaDigitalfuncionarioPrincipalIreport = emBranco;
		}
		
		
//		ASSINATURA DIGITAL FUNCIONARIO SECUNDÁRIO
		String assinaturaDigitalfuncionarioSecundario = "";
		if (impressaoContratoVO.getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getPastaBaseArquivoEnum() != null) {
			assinaturaDigitalfuncionarioSecundario = "<img src=\"" + impressaoContratoVO.getConfiguracaoGeralSistemaVO().getUrlExternoDownloadArquivo() + File.separator + impressaoContratoVO.getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getPastaBaseArquivoEnum().getValue() + File.separator + impressaoContratoVO.getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getNome() + "\" style=\"max-height:48px\">";
		}
		if (assinaturaDigitalfuncionarioSecundario == null || assinaturaDigitalfuncionarioSecundario.equals("0") || assinaturaDigitalfuncionarioSecundario.equals("")) {
			assinaturaDigitalfuncionarioSecundario = emBranco;
		}
//		ASSINATURA DIGITAL FUNCIONARIO SEUNDÁRIO IREPORT		
		String assinaturaDigitalfuncionarioSecundarioIreport = "";
		if (impressaoContratoVO.getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getPastaBaseArquivoEnum() != null) {
			assinaturaDigitalfuncionarioSecundarioIreport = impressaoContratoVO.getConfiguracaoGeralSistemaVO().getUrlExternoDownloadArquivo() + File.separator + impressaoContratoVO.getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getPastaBaseArquivoEnum().getValue() + File.separator + impressaoContratoVO.getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getNome();
		}
		if (assinaturaDigitalfuncionarioSecundarioIreport == null || assinaturaDigitalfuncionarioSecundarioIreport.equals("0") || assinaturaDigitalfuncionarioSecundarioIreport.equals("")) {
			assinaturaDigitalfuncionarioSecundarioIreport = emBranco;
		}
		
		String seloAssinaturaEletronica = "";
		if (impressaoContratoVO.getConfiguracaoGEDVO().getSeloAssinaturaEletronicaVO().getPastaBaseArquivoEnum() != null) {
			seloAssinaturaEletronica = "<img src=\"" + impressaoContratoVO.getConfiguracaoGeralSistemaVO().getUrlExternoDownloadArquivo() + File.separator + impressaoContratoVO.getConfiguracaoGEDVO().getSeloAssinaturaEletronicaVO().getPastaBaseArquivoEnum().getValue() + File.separator + impressaoContratoVO.getConfiguracaoGEDVO().getSeloAssinaturaEletronicaVO().getNome() + "\" style=\"max-height:48px\">";
		}
		if (seloAssinaturaEletronica == null || seloAssinaturaEletronica.equals("0") || seloAssinaturaEletronica.equals("")) {
			seloAssinaturaEletronica = emBranco;
		}
		
		String seloAssinaturaEletronicaIreport = "";
		if (impressaoContratoVO.getConfiguracaoGEDVO().getSeloAssinaturaEletronicaVO().getPastaBaseArquivoEnum() != null) {
			seloAssinaturaEletronicaIreport = impressaoContratoVO.getConfiguracaoGeralSistemaVO().getUrlExternoDownloadArquivo() + File.separator + impressaoContratoVO.getConfiguracaoGEDVO().getSeloAssinaturaEletronicaVO().getPastaBaseArquivoEnum().getValue() + File.separator + impressaoContratoVO.getConfiguracaoGEDVO().getSeloAssinaturaEletronicaVO().getNome();
		}
		if (seloAssinaturaEletronicaIreport == null || seloAssinaturaEletronicaIreport.equals("0") || seloAssinaturaEletronicaIreport.equals("")) {
			seloAssinaturaEletronicaIreport = emBranco;
		}
		
		while (texto.indexOf(marcador.getTag()) != -1) {
			int tamanho = obterTamanhoTag(marcador.getTag());
			String textoTag = obterTextoTag(marcador.getTag());
			String mar = obterTagSemTextoSemTamanho(marcador.getTag());
			if (mar.equalsIgnoreCase("Codigo_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), codigo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Nome_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), nome, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NomeDocumentacao_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), nomeDocumentacao, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("AreaConhecimento_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), areaConhecimento, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Regime_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), regime, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("RegimeAprovacao_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), regimeAprov, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Periodicidade_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), periodicidade, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Titulo_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), tituloCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataCriacao_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), dataCriacao, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NrRegistroInterno_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), nrRegistroInterno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataPublicacao_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), dataPublicacao, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NrPeriodoLetivo_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), nrPeriodoLetivo, textoTag, tamanho);
			}else if (mar.equalsIgnoreCase("codigoInep_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), codigoInep, textoTag, tamanho);
			}else if (mar.equalsIgnoreCase("NivelEducacional_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), nivelEduc, textoTag, tamanho);
			}else if (mar.equalsIgnoreCase("TextoDeclaracaoPPICurso")) {
				texto = substituirTag(texto, marcador.getTag(), textoDeclaracaoPPI, textoTag, tamanho);
			}else if (mar.equalsIgnoreCase("TextoDeclaracaoBolsasAuxilios_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), textoDeclaracaoBolsas, textoTag, tamanho);
			}else if (mar.equalsIgnoreCase("TextoDeclaracaoEscolaridadePublica_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), textoDeclaracaoEscolaridade, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("BaseLegal_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), baseLegal, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("PrimeiroReconhecimento_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), primeiroReconhecimentoCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataPrimeiroReconhecimento_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), dataPrimeiroReconhecimentoCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("RenovacaoReconhecimento_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), renovacaoReconhecimentoCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataRenovacaoReconhecimento_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), dataRenovacaoReconhecimentoCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CargaHoraria_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), cargaHoraria, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NrMesesConclusaoMatrizCurricular_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), nrMesesConclusaoMatrizCurricular, textoTag, tamanho);				
			} else if (mar.equalsIgnoreCase("CargaHorariaPeriodo_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), cargaHorariaDiscStr, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ListaDisciplinaOptativa_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), listaDisciplinaOptativa, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorMatricula_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorMatriculaCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorMatriculaExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorMatriculaCursoExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorMatriculaCursoApenasDescontosProgressivo_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorMatriculaCursoApenasDescontosProgressivo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorMatriculaCursoApenasDescontosProgressivoExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorMatriculaCursoApenasDescontosProgressivoExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorMatriculaCursoComDescontos_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorMatriculaCursoComDescontos, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorMatriculaCursoComDescontosExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorMatriculaCursoComDescontosExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NumParcela_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), numParcelaCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NumParcelaExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), numParcelaCursoExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParcela_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalComConvenioCusteado_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalCursoComConvenioCusteadoString, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParcelaComConvenioCusteado_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaCursoComConvenioCusteadoString, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParcelaExtensoComConvenioCusteado_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaCursoComConvenioCusteadoExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalExtensoComConvenioCusteado_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalCursoComConvenioCusteadoExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParcelaExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaCursoExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotal_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalCursoApenasDescontosInstituicao_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalCursoApenasDescontosInstituicao, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalCursoApenasDescontosInstituicaoExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalCursoApenasDescontosInstituicaoExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalDescontosInstituicao_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalDescontosInstituicao, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalDescontosInstituicaoExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalDescontosInstituicaoExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalSemDescontoSemMatricula_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalCursoSemDescontoSemMatricula, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalSemDescontoComMatricula_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalCursoSemDescontoComMatricula, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalSemDescontoSemMatriculaExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalCursoSemDescontoSemMatriculaExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalSemDescontoComMatriculaExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalCursoSemDescontoComMatriculaExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParcelaCursoComDescontos_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaCursoComDescontosStr, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalCursoExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalExtensoDivididoPorDois_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalCursoExtensoDivididoPorDois, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalExtensoDivididoPorTres_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalCursoExtensoDivididoPorTres, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalSemMatricula_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalCursoSemMatricula, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalSemMatriculaExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalCursoSemMatriculaExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalSemMatriculaExtensoDivididoPorDois_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalCursoSemMatriculaDivididoPorDoisExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalSemMatriculaExtensoDivididoPorTres_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalCursoSemMatriculaDivididoPorTresExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DiaVctoParcela_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), diaVencimentoParcelaString, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DiaVctoParcelaExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), diaVencimentoParcelaStringExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DiaVctoAPartirSegundaParcela_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), diaVencimentoAPartirSegundaParcelaString, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DiaVctoAPartirSegundaParcelaExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), diaVencimentoAPartirSegundaParcelaStringExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NrDiasAntesVcto1_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), nrDiasAntesVcto1, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NrDiasAntesVcto1Extenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), nrDiasAntesVcto1Extenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DescontoDiasAntesVcto1_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), descontoDiasAntesVcto1, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DescontoDiasAntesVcto1Extenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), descontoDiasAntesVcto1Extenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DescontoProgressivoValorDiasAntesVcto1_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), descontoProgressivoValorDiasAntesVcto1, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DescontoProgressivoValorDiasAntesVcto1Extenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), descontoProgressivoValorDiasAntesVcto1Extenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorAPagarDescontoDiasAntesVcto1_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorAPagarDescontoDiasAntesVcto1, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorAPagarDescontoDiasAntesVcto1ApenasProgressivo_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorAPagarDescontoDiasAntesVcto1ApenasProgressivo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorAPagarDescontoDiasAntesVcto1Extenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorAPagarDescontoDiasAntesVcto1Extenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DiaVcto1_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), diaVcto1, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DiaVencimentoPrimeiraParcela_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), diaVctoVencimentoPrimeiraParcela, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DiaVencimentoPrimeiraParcelaExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), diaVctoVencimentoPrimeiraParcelaExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NrDiasAntesVcto2_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), nrDiasAntesVcto2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NrDiasAntesVcto2Extenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), nrDiasAntesVcto2Extenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DescontoDiasAntesVcto2_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), descontoDiasAntesVcto2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DescontoDiasAntesVcto2Extenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), descontoDiasAntesVcto2Extenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DescontoProgressivoValorDiasAntesVcto2_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), descontoProgressivoValorDiasAntesVcto2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DescontoProgressivoValorDiasAntesVcto2Extenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), descontoProgressivoValorDiasAntesVcto2Extenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorAPagarDescontoDiasAntesVcto2_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorAPagarDescontoDiasAntesVcto2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorAPagarDescontoDiasAntesVcto2ApenasProgressivo_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorAPagarDescontoDiasAntesVcto2ApenasProgressivo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorAPagarDescontoDiasAntesVcto2Extenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorAPagarDescontoDiasAntesVcto2Extenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DiaVcto2_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), diaVcto2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NrDiasAntesVcto3_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), nrDiasAntesVcto3, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NrDiasAntesVcto3Extenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), nrDiasAntesVcto3Extenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DescontoDiasAntesVcto3_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), descontoDiasAntesVcto3, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DescontoDiasAntesVcto3Extenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), descontoDiasAntesVcto3Extenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DescontoProgressivoValorDiasAntesVcto3_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), descontoProgressivoValorDiasAntesVcto3, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DescontoProgressivoValorDiasAntesVcto3Extenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), descontoProgressivoValorDiasAntesVcto3Extenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorAPagarDescontoDiasAntesVcto3_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorAPagarDescontoDiasAntesVcto3, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorAPagarDescontoDiasAntesVcto3ApenasProgressivo_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorAPagarDescontoDiasAntesVcto3ApenasProgressivo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorAPagarDescontoDiasAntesVcto3Extenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorAPagarDescontoDiasAntesVcto3Extenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DiaVcto3_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), diaVcto3, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NrDiasAntesVcto4_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), nrDiasAntesVcto4, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NrDiasAntesVcto4Extenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), nrDiasAntesVcto4Extenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DescontoDiasAntesVcto4_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), descontoDiasAntesVcto4, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DescontoDiasAntesVcto4Extenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), descontoDiasAntesVcto4Extenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DescontoProgressivoValorDiasAntesVcto4_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), descontoProgressivoValorDiasAntesVcto4, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DescontoProgressivoValorDiasAntesVcto4Extenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), descontoProgressivoValorDiasAntesVcto4Extenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorAPagarDescontoDiasAntesVcto4_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorAPagarDescontoDiasAntesVcto4, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorAPagarDescontoDiasAntesVcto4ApenasProgressivo_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorAPagarDescontoDiasAntesVcto4ApenasProgressivo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorAPagarDescontoDiasAntesVcto4Extenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorAPagarDescontoDiasAntesVcto4Extenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DiaVcto4_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), diaVcto4, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalDesconto_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalDesconto, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorPorDisciplina_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorPorDisciplina, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalDescontoExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalDescontoExtenso, textoTag, tamanho);

			} else if (mar.equalsIgnoreCase("ValorTotalDesconto1Antecipacao_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalDesconto1, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalDescontoExtenso1Antecipacao_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalDescontoExtenso1, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalDesconto2Antecipacao_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalDesconto2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalDescontoExtenso2Antecipacao_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalDescontoExtenso2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalDesconto3Antecipacao_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalDesconto3, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalDescontoExtenso3Antecipacao_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalDescontoExtenso3, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalDesconto4Antecipacao_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalDesconto4, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalDescontoExtenso4Antecipacao_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalDescontoExtenso4, textoTag, tamanho);

			} else if (mar.equalsIgnoreCase("ValorDescontoDiasAntesVcto4_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoDiasAntesVcto4, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorDescontoDiasAntesVcto4Extenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoDiasAntesVcto4Extenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorDescontoDiasAntesVcto3_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoDiasAntesVcto3, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorDescontoDiasAntesVcto3Extenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoDiasAntesVcto3Extenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorDescontoDiasAntesVcto2_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoDiasAntesVcto2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorDescontoDiasAntesVcto2Extenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoDiasAntesVcto2Extenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorDescontoDiasAntesVcto1_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoDiasAntesVcto1, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorDescontoDiasAntesVcto1Extenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoDiasAntesVcto1Extenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Duracao_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), duracaoCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DescricaoCondicaoCurso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), descricaoCondicaoCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NrParcelasPeriodo_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), nrParcelasPeriodo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("PeriodoLetivo_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), periodoLetivoCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CondicaoPagamento_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), CondicaoPagamentoCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataInicio_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), dataInicioCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataFinal_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), dataFinalCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataFinalExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), dataExtensoFinalCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataFinalExtenso2_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), dataExtensoFinalCurso2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataInicioProgAula_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), dataInicioProgAula, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataFinalProgAula_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), dataFinalProgAula, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("AnoAtual_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), anoAtualCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("SemestreAtual_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), semestreAtualCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("SemestreAtual2_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), semestreAtual2Curso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TotalSemestres_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), totalSemestresCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DiaPrimeiraParcela_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), diaVencimentoPrimeiraParcela, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("MesPrimeiraParcela_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), Uteis.getMesReferenciaExtenso(mesVencimentoPrimeiraParcela), textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("AnoPrimeiraParcela_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), anoVencimentoPrimeiraParcela, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataVencimentoPrimeiraParcela_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), dataVencimentoPrimeiraParcela, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataVencimentoSegundaParcela_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), dataVencimentoSegundaParcela, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataVencimentoUltimaParcela_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), dataVencimentoUltimaParcela, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorDescontoInstitucionalPrimeiraParcela_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoInstitucionalPrimeiraParcela.toString(), textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalDescontoSemValidade_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoSemValidade_Curso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalPlanoDescontoSemValidade_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorPlanoDescontoSemValidade_Curso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalDescontoSemValidadeExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoSemValidadeExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalPlanoDescontoSemValidadeExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorPlanoDescontoSemValidadeExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParcelaCursoComDescontosSemValidade_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaComDescontoSemValidade, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParcelaCursoComDescontosSemValidadeExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaComDescontoSemValidadeExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParcelaCursoComPlanoDescontosSemValidade_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaComPlanoDescontoSemValidade, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParcelaCursoComPlanoDescontosSemValidadeExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaComPlanoDescontoSemValidadeExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParcelaCursoComDescontoExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaComDescontoExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalDescontoComValidade_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoDescontoComValidade, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalDescontoComValidadeExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoComValidadeExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParcelaCursoComDescontosComValidade_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaCursoComValidade, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorPlanoDescontosAteDataVencimento_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoAteDataVencimento, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorPlanoDescontosAteDataVencimentoExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorPlanoDescontoAteDataVencimentoExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParcelaCursoComDescontosComValidadeExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaCursoComValidadeExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("PercentualDescontoConvenio_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorPercentualDescontoConvenio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorPorDisciplinaExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorPorDisciplinaExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NrDiasAntesVctoPlanoDesconto_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), nrDiasAntesVencimentoPlanoDescontoComValidade, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParcelaComDescontoConvenio_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaComDescontoConvenio_Matricula, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParcelaComDescontoConvenio_CursoExtenso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaComDescontoConvenio_MatriculaExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParcelaSemDescontoConvenio_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaSemDescontoConvenio_Matricula, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParcelaSemDescontoConvenio_CursoExtenso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaSemDescontoConvenio_MatriculaExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorDescontoAPagarSemDescontoConvenio_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoAPagarSemDescontoConvenioStr, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorDescontoAPagarSemDescontoConvenio_CursoExtenso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoAPagarSemDescontoConvenioStrExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParcelaPrimeiroDiaAntecipacaoConvenio_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaPrimeiroDiaAntecipacaoConvenioStr, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParcelaPrimeiroDiaAntecipacaoConvenio_CursoExtenso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaPrimeiroDiaAntecipacaoConvenioStrExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorPrimeiraParcelaDescontoComValidade_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorPrimeiraParcela.toString(), textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorDescontoInstitucionalPrimeiraParcelaExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoInstitucionalPrimeiraParcelaExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NomePlanoDescontos_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), descricaoPlanoDescontosStr, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorIntegralMatriculaCondicaoPagamento_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorIntegralMatriculaCondicaoPagamentoStr, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorIntegralParcelaCondicaoPagamento_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorIntegralParcelaCondicaoPagamentoStr, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorIntegralMatriculaCondicaoPagamentoExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorIntegralMatriculaCondicaoPagamentoExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorIntegralParcelaCondicaoPagamentoExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorIntegralParcelaCondicaoPagamentoExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NrParcelasPeriodoCondicaoPagamento_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), nrParcelasPeriodoCondicaoPagamentoStr, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("InformacoesAdicionais_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), informacoesAdicionais, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Texto_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), textoCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FormaIngresso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), formaIngresso_Curso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Nome_CursoOrigem")) {
				texto = substituirTag(texto, marcador.getTag(), nomeCursoOrigem, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Nome_CursoDestino")) {
				texto = substituirTag(texto, marcador.getTag(), nomeCursoDestino, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TabelaTipoDesconto_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), tabelaTipoDesconto, textoTag, tamanho);
				substituirTagComLista(texto, marcador.getTag(), getListaTipoDescontoCurso(), textoTag);
			} else if (mar.equalsIgnoreCase("TabelaTodosTipoDesconto_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), tabelaTipoDescontoTodos, textoTag, tamanho);
				substituirTagComLista(texto, marcador.getTag(), getListaTodosTipoDesconto_Curso(), textoTag);
			} else if (mar.equalsIgnoreCase("TabelaListaContaReceberComDescontos_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), tabelaListaContaReceberComDescontos, textoTag, tamanho);
				substituirTagComLista(texto, marcador.getTag(), getListaContaReceberComDescontos(), textoTag);
			} else if (mar.equalsIgnoreCase("Nome_Certificacao")) {
				texto = substituirTag(texto, marcador.getTag(), nomeCertificacao, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TitulacaoFormando_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), titulacaoFormando, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataExtensoInicioCurso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), dataExtensoInicioCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataExtensoInicioCurso2_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), dataExtensoInicioCurso2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("AnoConclusao_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), anoConclusaoCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Habilitacao_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), habilitacaoCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TabelaListaContaReceberComDescontos_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), tabelaListaContaReceberComDescontos, textoTag, tamanho);
				substituirTagComLista(texto, marcador.getTag(), getListaContaReceberComDescontos(), textoTag);
			} else if (mar.equalsIgnoreCase("PrevisaoConclusao_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), previsaoConclusao, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CompetenciaProfissional_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), competencia, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("PerfilEgresso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), perfilEgresso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("PublicoAlvo_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), publicoAlvo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("QuantidadeParcelasMaterialDidatico_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), quantidadeParcelasMaterialDidatico, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParcelaMaterialDidatico_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaMaterialDidatico, textoTag, tamanho);
			} else if (mar.equals("ValorExtensoParcelaMaterialDidatico_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorExtensoParcelaMaterialDidatico, textoTag, tamanho);
			} else if (mar.equals("ValorDescontoParcelaMaterialDidatico_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoParcelaMaterialDidatico, textoTag, tamanho);
			} else if (mar.equals("ValorDescontoExtensoParcelaMaterialDidatico_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoExtensoParcelaMaterialDidatico, textoTag, tamanho);
			} else if (mar.equals("ValorTotalParcelaMaterialDidatico_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalParcelaMaterialDidatico, textoTag, tamanho);
			} else if (mar.equals("ValorTotalExtensoParcelaMaterialDidatico_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalExtensoParcelaMaterialDidatico, textoTag, tamanho);
			} else if (mar.equals("ValorTotalComDescontoParcelaMaterialDidatico_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalComDescontoParcelaMaterialDidatico, textoTag, tamanho);
			} else if (mar.equals("ValorTotalComDescontoExtensoParcelaMaterialDidatico_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalComDescontoExtensoParcelaMaterialDidatico, textoTag, tamanho);
			} else if (mar.equals("DiaBaseVencimentoParcelaMaterialDidatico_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), diaBaseVencimentoParcelaMaterialDidatico, textoTag, tamanho);
			} else if (mar.equals("UnidadeEnsinoMaterialDidatico_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), unidadeEnsinoMaterialDidatico, textoTag, tamanho);
			} else if (mar.equals("EnderecoUnidadeEnsinoMaterialDidatico_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), enderecoUnidadeEnsinoMaterialDidatico, textoTag, tamanho);
			} else if (mar.equals("CNPJUnidadeEnsinoMaterialDidatico_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), cnpjUnidadeEnsinoMaterialDidatico, textoTag, tamanho);
			} else if (mar.equals("QuantidadeParcelasExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), quantidadeParcelasExtensoCurso, textoTag, tamanho);
			} else if (mar.equals("NumeroTotalDisciplinasEAD_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), numeroTotalDisciplinasEADCurso, textoTag, tamanho);
			} else if (mar.equals("NumeroTotalDisciplinasPresencial_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), numeroTotalDisciplinasPresencialCurso, textoTag, tamanho);
			} else if (mar.equals("NumeroTotalDisciplinas_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), numeroTotalDisciplinasCurso, textoTag, tamanho);
			} else if (mar.equals("ValorTotalParcelasCondicaoPagamento_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalParcelasCondicaoPagamento, textoTag, tamanho);
			} else if (mar.equals("ValorTotalParcelasCondicaoPagamentoExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalParcelasCondicaoPagamentoExtenso, textoTag, tamanho);
			} else if (mar.equals("ValorParcelaComDescontoInstitucional_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaComDescontoInstitucional, textoTag, tamanho);
			} else if (mar.equals("ValorParcelaComDescontoInstitucionalExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaComDescontoInstitucionalExtenso, textoTag, tamanho);
			} else if (mar.equals("ValorTotalParcelasComDescontoIncondicional_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalParcelasComDescontoIncondicional, textoTag, tamanho);
			} else if (mar.equals("ValorTotalParcelasComDescontoIncondicionalExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalParcelasComDescontoIncondicionalExtenso, textoTag, tamanho);
			} else if (mar.equals("ValorParcelaComDescontoIncondicional_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaComDescontoIncondicional, textoTag, tamanho);
			} else if (mar.equals("ValorParcelaComDescontoIncondicionalExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaComDescontoIncondicionalExtenso, textoTag, tamanho);
			} else if (mar.equals("ValorParcelaMaterialDidaticoComDescontoIncondicional_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaMaterialDidaticoComDescontoIncondicional, textoTag, tamanho);
			} else if (mar.equals("ValorParcelaMaterialDidaticoComDescontoIncondicionalExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaMaterialDidaticoComDescontoIncondicionalExtenso, textoTag, tamanho);
			} else if (mar.equals("ValorDescontoCondicionalMatricula_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoCondicionalMatriculaStr, textoTag, tamanho);
			} else if (mar.equals("ValorDescontoCondicionalMatriculaExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoCondicionalMatriculaExtenso, textoTag, tamanho);
			} else if (mar.equals("ValorDescontoCondicionalParcela_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoCondicionalParcelaStr, textoTag, tamanho);
			} else if (mar.equals("ValorDescontoCondicionalParcelaExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoCondicionalParcelaExtenso, textoTag, tamanho);
			} else if (mar.equals("ValorDescontoCondicionalMaterialDidatico_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoCondicionalMaterialDidaticoStr, textoTag, tamanho);
			} else if (mar.equals("ValorDescontoCondicionalMaterialDidaticoExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoCondicionalMaterialDidaticoExtenso, textoTag, tamanho);
			} else if (mar.equals("PorcentagemCusteadoConvenioParcela_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), porcentagemCusteadoConvenioParcelaCursoStr, textoTag, tamanho);
			} else if (mar.equals("PorcentagemCusteadoAlunoParcela_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), porcentagemCusteadoAlunoParcelaCursoStr, textoTag, tamanho);
			} else if (mar.equals("ValorTotalCusteadoConvenioParcela_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalCusteadoConvenioParcelaCursoStr, textoTag, tamanho);
			} else if (mar.equals("ValorTotalCusteadoConvenioParcelaExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalCusteadoConvenioParcelaCursoExtenso, textoTag, tamanho);
			} else if (mar.equals("ValorCusteadoConvenioParcelaMaterialDidatico_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorCusteadoConvenioParcelaMaterialDidaticoCursoStr, textoTag, tamanho);
			} else if (mar.equals("ValorCusteadoConvenioParcelaMaterialDidaticoExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorCusteadoConvenioParcelaMaterialDidaticoCursoExtenso, textoTag, tamanho);
			} else if (mar.equals("ValorCusteadoAlunoParcelaMaterialDidatico_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorCusteadoAlunoParcelaMaterialDidaticoCursoStr, textoTag, tamanho);
			} else if (mar.equals("ValorCusteadoAlunoParcelaMaterialDidaticoExtenso_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorCusteadoAlunoParcelaMaterialDidaticoCursoExtenso, textoTag, tamanho);
			} else if (mar.equals("ValorDescontoInCondicionalMaterialDidatico_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorDescontoInCondicionalMaterialDidaticoStr , textoTag, tamanho);
			} else if (mar.equals("AssinaturaDigitalfuncionarioPrincipal_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), assinaturaDigitalfuncionarioPrincipal , textoTag, tamanho);
			} else if (mar.equals("AssinaturaDigitalfuncionarioPrincipalIreport_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), assinaturaDigitalfuncionarioPrincipalIreport , textoTag, tamanho);
			} else if (mar.equals("SeloAssinaturaEletronica_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), seloAssinaturaEletronica , textoTag, tamanho);
			} else if (mar.equals("SeloAssinaturaEletronicaIreport_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), seloAssinaturaEletronicaIreport , textoTag, tamanho);
			} else if (mar.equals("AssinaturaDigitalfuncionarioSecundario_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), assinaturaDigitalfuncionarioSecundario , textoTag, tamanho);
			} else if (mar.equals("AssinaturaDigitalfuncionarioSecundarioIreport_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), assinaturaDigitalfuncionarioSecundarioIreport , textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParcelaComDescontoConvenioSegundaFaixa_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaComDescontoConvenioSegundaFaixa_Matricula, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParcelaComDescontoConvenioTerceiraFaixa_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaComDescontoConvenioTerceiraFaixa_Matricula, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NomeDocenteResponsavelAssinaturaTermoEstagio_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), nomeDocenteResponsavelAssinaturaTermoEstagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CpfDocenteResponsavelAssinaturaTermoEstagio_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), cpfDocenteResponsavelAssinaturaTermoEstagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EmailDocenteResponsavelAssinaturaTermoEstagio_Curso")) {
				texto = substituirTag(texto, marcador.getTag(), emailDocenteResponsavelAssinaturaTermoEstagio, textoTag, tamanho);
			}
			
			break;
		}
		return texto;
	}
	
	public String substituirTagProgramacaoFormatura(String texto, ImpressaoContratoVO impressaoContratoVO, MarcadorVO marcador) throws Exception {
		
		ProgramacaoFormaturaVO programacaoFormatura = impressaoContratoVO.getProgramacaoFormaturaVO();
		ProgramacaoFormaturaCursoVO programacaoFormaturaCurso = impressaoContratoVO.getProgramacaoFormaturaCursoVO();
		String emBranco = obterStringEmBrancoParaTag(marcador);
		String nome_Curso = programacaoFormatura.getCurso().getNome();
		String nomePresidenteMesa_ColacaoGrau = programacaoFormatura.getColacaoGrauVO().getPresidenteMesa().getNome();
		String nomeSecretariaAcademica_ColacaoGrau = programacaoFormatura.getColacaoGrauVO().getSecretariaAcademica().getNome();
		String dataColacao_ColacaoGrau = Uteis.getData(programacaoFormatura.getColacaoGrauVO().getData(), "dd/MM/yyyy");
		String dataHoraDescrita_ColacaoGrau = programacaoFormatura.getColacaoGrauVO().getDataHoraDescrita();
		String dataComMesDescrita_ColacaoGrau = programacaoFormatura.getColacaoGrauVO().getDataMesDescrita();
		String nrRegistroInternoCurso_ProgramacaoFormaturaCurso = programacaoFormaturaCurso.getNrRegistroInternoCurso();
		if (nome_Curso == null || nome_Curso.equals("0") || nome_Curso.equals("")) {
			nome_Curso = emBranco;
		}
		if (nomePresidenteMesa_ColacaoGrau == null || nomePresidenteMesa_ColacaoGrau.equals("0") || nomePresidenteMesa_ColacaoGrau.equals("")) {
			nomePresidenteMesa_ColacaoGrau = emBranco;
		}
		if (nomeSecretariaAcademica_ColacaoGrau == null || nomeSecretariaAcademica_ColacaoGrau.equals("0") || nomeSecretariaAcademica_ColacaoGrau.equals("")) {
			nomeSecretariaAcademica_ColacaoGrau = emBranco;
		}
		if (dataColacao_ColacaoGrau == null) {
			dataColacao_ColacaoGrau = emBranco;
		}
		if (dataHoraDescrita_ColacaoGrau == null || dataHoraDescrita_ColacaoGrau.equals("")) {
			dataHoraDescrita_ColacaoGrau = emBranco;
		}
		if (dataComMesDescrita_ColacaoGrau == null || dataComMesDescrita_ColacaoGrau.equals("")) {
			dataComMesDescrita_ColacaoGrau = emBranco;
		}
		if (nrRegistroInternoCurso_ProgramacaoFormaturaCurso == null || nrRegistroInternoCurso_ProgramacaoFormaturaCurso.equals("")) {
			nrRegistroInternoCurso_ProgramacaoFormaturaCurso = emBranco;
		}
		while (texto.indexOf(marcador.getTag()) != -1) {
			int tamanho = obterTamanhoTag(marcador.getTag());
			String mar = obterTagSemTextoSemTamanho(marcador.getTag());
			String textoTag = obterTextoTag(marcador.getTag());

			if (mar.equalsIgnoreCase("NomeCurso_ProgramacaoFormatura")) {
				texto = substituirTag(texto, marcador.getTag(), nome_Curso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NomePresidenteMesaColacaoGrau_ProgramacaoFormatura")) {
				texto = substituirTag(texto, marcador.getTag(), nomePresidenteMesa_ColacaoGrau, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NomeSecretariaAcademicaColacaoGrau_ProgramacaoFormatura")) {
				texto = substituirTag(texto, marcador.getTag(), nomeSecretariaAcademica_ColacaoGrau, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataColacaoColacaoGrau_ProgramacaoFormatura")) {
				texto = substituirTag(texto, marcador.getTag(), dataColacao_ColacaoGrau, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataHoraPorExtenso_ColacaoGrau")) {
				texto = substituirTag(texto, marcador.getTag(), dataHoraDescrita_ColacaoGrau, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataComMesPorExtenso_ColacaoGrau")) {
				texto = substituirTag(texto, marcador.getTag(), dataComMesDescrita_ColacaoGrau, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NrRegistroInternoCurso_ProgramacaoFormaturaCurso")) {
				texto = substituirTag(texto, marcador.getTag(), nrRegistroInternoCurso_ProgramacaoFormaturaCurso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ListaProgramacaoFormaturaAluno_ProgramacaoFormatura")) {
				substituirTagComLista(texto, marcador.getTag(), impressaoContratoVO.getProgramacaoFormaturaAlunoVOs(), textoTag);
			}
			
			break;
		}
		return texto;
	}

	public String substituirTagUnidadeEnsino(String texto, ImpressaoContratoVO impressaoContratoVO, MarcadorVO marcador) throws Exception {
		UnidadeEnsinoVO unidadeEnsino = impressaoContratoVO.getMatriculaVO().getUnidadeEnsino();
		String emBranco = obterStringEmBrancoParaTag(marcador);
		String codigo = unidadeEnsino.getCodigo().toString();
		if (codigo == null || codigo.equals("0") || codigo.equals("")) {
			codigo = emBranco;
		}
		String nome = unidadeEnsino.getNome();
		if (nome == null || nome.equals("0") || nome.equals("")) {
			nome = emBranco;
		}
		String fax = unidadeEnsino.getFax();
		if (fax == null || fax.equals("0") || fax.equals("")) {
			fax = emBranco;
		}
		String site = unidadeEnsino.getSite();
		if (site == null || site.equals("0") || site.equals("")) {
			site = emBranco;
		}
		String email = unidadeEnsino.getEmail();
		if (email == null || email.equals("0") || email.equals("")) {
			email = emBranco;
		}
		String telComercial1 = unidadeEnsino.getTelComercial1();
		if (telComercial1 == null || telComercial1.equals("0") || telComercial1.equals("")) {
			telComercial1 = emBranco;
		}
		String telComercial2 = unidadeEnsino.getTelComercial2();
		if (telComercial2 == null || telComercial2.equals("0") || telComercial2.equals("")) {
			telComercial2 = emBranco;
		}
		String telComercial3 = unidadeEnsino.getTelComercial3();
		if (telComercial3 == null || telComercial3.equals("0") || telComercial3.equals("")) {
			telComercial3 = emBranco;
		}
		String inscEstadual = unidadeEnsino.getInscEstadual();
		if (inscEstadual == null || inscEstadual.equals("0") || inscEstadual.equals("")) {
			inscEstadual = emBranco;
		}
		String cnpj = unidadeEnsino.getCNPJ();
		if (cnpj == null || cnpj.equals("0") || cnpj.equals("")) {
			cnpj = emBranco;
		}
		String cep = unidadeEnsino.getCEP();
		if (cep == null || cep.equals("0") || cep.equals("")) {
			cep = emBranco;
		}
		String complemento = unidadeEnsino.getComplemento();
		if (complemento == null || complemento.equals("0") || complemento.equals("")) {
			complemento = emBranco;
		}
		String numero = unidadeEnsino.getNumero();
		if (numero == null || numero.equals("0") || numero.equals("")) {
			numero = emBranco;
		}
		String setor = unidadeEnsino.getSetor();
		if (setor == null || setor.equals("0") || setor.equals("")) {
			setor = emBranco;
		}
		String endereco = unidadeEnsino.getEndereco();
		if (endereco == null || endereco.equals("0") || endereco.equals("")) {
			endereco = emBranco;
		}
		String razaoSocial = unidadeEnsino.getRazaoSocial();
		if (razaoSocial == null || razaoSocial.equals("0") || razaoSocial.equals("")) {
			razaoSocial = emBranco;
		}
		String cidade = unidadeEnsino.getCidade().getNome();
		if (cidade == null || cidade.equals("0") || cidade.equals("")) {
			cidade = emBranco;
		}
		String estado = unidadeEnsino.getCidade().getEstado().getSigla();
		if (estado == null || estado.equals("0") || estado.equals("")) {
			estado = emBranco;
		}
		String nomeUnidadeOrigem = impressaoContratoVO.getMatriculaOrigem().getUnidadeEnsino().getNome();
		if (nome == null || nome.equals("0") || nome.equals("")) {
			nome = emBranco;
		}
		String nomeUnidadeDestino = impressaoContratoVO.getMatriculaDestino().getUnidadeEnsino().getNome();
		if (nome == null || nome.equals("0") || nome.equals("")) {
			nome = emBranco;
		}
		String credenciamentoUnidadeEnsino = (Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaVO().getCurso().getModalidadeCurso()) && impressaoContratoVO.getMatriculaVO().getCurso().getModalidadeCurso().equals(ModalidadeDisciplinaEnum.ON_LINE) ? unidadeEnsino.getCredenciamentoPortariaEAD() : unidadeEnsino.getCredenciamentoPortaria());
		if (credenciamentoUnidadeEnsino == null || credenciamentoUnidadeEnsino.equals("")) {
			credenciamentoUnidadeEnsino = emBranco;
		}

		String logoPadraoUnidadeEnsino = "<img src=\"" + impressaoContratoVO.getConfiguracaoGeralSistemaVO().getUrlExternoDownloadArquivo() + File.separator + unidadeEnsino.getCaminhoBaseLogoRelatorio() + File.separator + unidadeEnsino.getNomeArquivoLogoRelatorio() + "\" style=\"max-height:48px\">";
		if (logoPadraoUnidadeEnsino == null || logoPadraoUnidadeEnsino.equals("")) {
			logoPadraoUnidadeEnsino = emBranco;
		}

		String nomeExpedicaoDiploma = unidadeEnsino.getNomeExpedicaoDiploma();
		if (nomeExpedicaoDiploma == null || nomeExpedicaoDiploma.equals("")) {
			nomeExpedicaoDiploma = emBranco;
		}
		
		String codigoIES = unidadeEnsino.getCodigoIES().toString();
		if (codigoIES == null || codigoIES.equals("0") ||codigoIES.equals("")) {
			codigoIES = emBranco;
		}
		
		String nomeMantenedora = unidadeEnsino.getMantenedora();
		if (nomeMantenedora == null || nomeMantenedora.equals("")) {
			nomeMantenedora = emBranco;
		}
		
		String codigoIesMantenedora = unidadeEnsino.getCodigoIESMantenedora().toString();
		if (codigoIesMantenedora == null || codigoIesMantenedora.equals("0") || codigoIesMantenedora.equals("")) {
			codigoIesMantenedora = emBranco;
		}
		
		String cnpjMantenedora = unidadeEnsino.getCnpjMantenedora();
		if (cnpjMantenedora == null || cnpjMantenedora.equals("")) {
			cnpjMantenedora = emBranco;
		}
		
		String nomeUnidadeCertificadora = unidadeEnsino.getUnidadeCertificadora();
		if (nomeUnidadeCertificadora == null || nomeUnidadeCertificadora.equals("")) {
			nomeUnidadeCertificadora = emBranco;
		}
		
		String codigoIesUnidadeCertificadora = unidadeEnsino.getCodigoIESUnidadeCertificadora().toString();
		if (codigoIesUnidadeCertificadora == null || codigoIesUnidadeCertificadora.equals("0") || codigoIesUnidadeCertificadora.equals("")) {
			codigoIesUnidadeCertificadora = emBranco;
		}
		
		String cnpjUnidadeCertificadora = unidadeEnsino.getCnpjUnidadeCertificadora();
		if (cnpjUnidadeCertificadora == null || cnpjUnidadeCertificadora.equals("")) {
			cnpjUnidadeCertificadora = emBranco;
		}

		// while
		// (texto.indexOf(Uteis.trocarAcentuacaoPorAcentuacaoHTML(marcador.getTag()))
		// != -1) {
		while (texto.indexOf(marcador.getTag()) != -1) {
			int tamanho = obterTamanhoTag(marcador.getTag());
			String mar = obterTagSemTextoSemTamanho(marcador.getTag());
			String textoTag = obterTextoTag(marcador.getTag());

			if (mar.equalsIgnoreCase("Codigo_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), codigo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Fax_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), fax, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Site_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), site, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Email_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), email, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TelComercial1_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), telComercial1, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TelComercial2_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), telComercial2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TelComercial3_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), telComercial3, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("InscEstadual_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), inscEstadual, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Cnpj_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), cnpj, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Cep_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), cep, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Complemento_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), complemento, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Numero_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), numero, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Bairro_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), setor, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Endereco_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), endereco, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("RazaoSocial_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), razaoSocial, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Descricao_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), nome, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Cidade_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), cidade, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Estado_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), estado, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Descricao_UnidadeEnsinoOrigem")) {
				texto = substituirTag(texto, marcador.getTag(), nomeUnidadeOrigem, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Descricao_UnidadeEnsinoDestino")) {
				texto = substituirTag(texto, marcador.getTag(), nomeUnidadeDestino, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Credenciamento_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), credenciamentoUnidadeEnsino, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("LogoPadrao_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), logoPadraoUnidadeEnsino, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NomeExpedicaoDiploma_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), nomeExpedicaoDiploma, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("codigoIES_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), codigoIES, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("nomeMantenedora_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), nomeMantenedora, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("codigoIesMantenedora_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), codigoIesMantenedora, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("cnpjMantenedora_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), cnpjMantenedora, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("nomeUnidadeCertificadora_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), nomeUnidadeCertificadora, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("codigoIesUnidadeCertificadora_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), codigoIesUnidadeCertificadora, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("cnpjUnidadeCertificadora_UnidadeEnsino")) {
				texto = substituirTag(texto, marcador.getTag(), cnpjUnidadeCertificadora, textoTag, tamanho);
			}

			break;
		}
		return texto;
	}

	@SuppressWarnings("unchecked")
	public String substituirTagDisciplina(String texto, ImpressaoContratoVO impressaoContratoVO, MarcadorVO marcador) throws Exception {
		DisciplinaVO disciplina = impressaoContratoVO.getDisciplinaVO();
		GradeDisciplinaVO gradeDisciplinaVO = impressaoContratoVO.getGradeDisciplinaVO();
		PlanoEnsinoVO planoEnsinoVO = impressaoContratoVO.getPlanoEnsinoVO();		
		// List listaDisciplinasCursadasOuMinistradas =
		// impressaoContratoVO.getListaDisciplinasCursadasOuMinistradas();
		
		setListaDisciplinasCursadasOuMinistradas(impressaoContratoVO.getListaDisciplinasCursadasOuMinistradas());
		setListaDisciplinaCertificado(impressaoContratoVO.getListaDisciplinasCursadasOuMinistradas());
		setListaDisciplinasAprovadasPeriodoLetivo(impressaoContratoVO.getListaDisciplinasAprovadasPeriodoLetivo());

		
//		List listaDisciplinasAprovadasPeriodoLetivo = impressaoContratoVO.getListaDisciplinasAprovadasPeriodoLetivo();
		String emBranco = obterStringEmBrancoParaTag(marcador);
		String codigo = disciplina.getCodigo().toString();
		if (codigo == null || codigo.equals("0") || codigo.equals("")) {
			codigo = emBranco;
		}
		String nome = disciplina.getNome();
		if (nome == null || nome.equals("0") || nome.equals("")) {
			nome = emBranco;
		}
		String abreviatura = disciplina.getAbreviatura();
		if (abreviatura == null || abreviatura.equals("0") || abreviatura.equals("")) {
			abreviatura = emBranco;
		}
		String semestre = impressaoContratoVO.getHistoricoVO().getSemestreHistorico();
		if (semestre == null || semestre.equals("0") || semestre.equals("")) {
			semestre = emBranco;
		}
		String situacaoDisciplina = impressaoContratoVO.getHistoricoVO().getSituacao_Apresentar();
		if (situacaoDisciplina == null || situacaoDisciplina.equals("0") || situacaoDisciplina.equals("")) {
			situacaoDisciplina = emBranco;
		}
		String nota = impressaoContratoVO.getHistoricoVO().getMediaFinal_Apresentar();
		if (nota == null || nota.equals("")) {
			nota = emBranco;
		}

		String frequencia = impressaoContratoVO.getHistoricoVO().getFreguencia().toString();
		if (frequencia == null || frequencia.equals("0") || frequencia.equals("")) {
			frequencia = emBranco;
		}
		String diaInicio = disciplina.getDiaInicio().toString();
		if (diaInicio == null || diaInicio.equals("0") || diaInicio.equals("")) {
			diaInicio = emBranco;
		}
		String diaFim = disciplina.getDiaFim().toString();
		if (diaFim == null || diaFim.equals("0") || diaFim.equals("")) {
			diaFim = emBranco;
		}
		String ano = impressaoContratoVO.getHistoricoVO().getAnoHistorico();
		if (ano == null || ano.equals("0") || ano.equals("")) {
			ano = emBranco;
		}
		String mes = disciplina.getMes();
		if (mes == null || mes.equals("0") || mes.equals("")) {
			mes = emBranco;
		}
		String cargaHorarioDisciplina = gradeDisciplinaVO.getCargaHoraria().toString();
		if (cargaHorarioDisciplina == null || cargaHorarioDisciplina.equals("0") || cargaHorarioDisciplina.equals("")) {
			cargaHorarioDisciplina = emBranco;
		}
		String bimestreAnoConclusaoDisciplina = impressaoContratoVO.getHistoricoVO().getAnoBimestreConclusaoDisciplina();
		if (bimestreAnoConclusaoDisciplina == null || bimestreAnoConclusaoDisciplina.equals("0") || bimestreAnoConclusaoDisciplina.equals("")) {
			bimestreAnoConclusaoDisciplina = emBranco;
		}
		String conteudoPlanoEnsinoDisciplina = "";
		boolean primeiraVez1 = true;
		for (ConteudoPlanejamentoVO conteudoPlanejamentoVO : planoEnsinoVO.getConteudoPlanejamentoVOs()) {
			if (primeiraVez1) {
				conteudoPlanoEnsinoDisciplina += conteudoPlanejamentoVO.getConteudo();
			} else {
				conteudoPlanoEnsinoDisciplina += " <br> - " + conteudoPlanejamentoVO.getConteudo();
			}
			primeiraVez1 = false;
		}
		if (conteudoPlanoEnsinoDisciplina == null || conteudoPlanoEnsinoDisciplina.equals("0") || conteudoPlanoEnsinoDisciplina.equals("")) {
			conteudoPlanoEnsinoDisciplina = emBranco;
		}
		String listaDisciplina = "";
		String listaDisciplinaCursadasMinistadas ="";
		String listaDisciplinaVersoDiploma ="";
		String tituloMonografia = "";
		Integer cargaHorariaDisciplinas = 0;
		Double totalMediaFinal  =0.0;
		boolean primeiraVez = true;
		if (!getListaDisciplinasCursadasOuMinistradas().isEmpty()) {
			Iterator i = getListaDisciplinasCursadasOuMinistradas().iterator();
			while (i.hasNext()) {
				HistoricoVO hist = (HistoricoVO) i.next();
				if (Uteis.isAtributoPreenchido(hist.getCodigo())) {
					cargaHorariaDisciplinas = cargaHorariaDisciplinas + hist.getGradeDisciplinaVO().getCargaHoraria().intValue();
					if (primeiraVez) {
						listaDisciplina = "<table border=1 cellspacing=0 cellpadding=1 bordercolor=\"000000\"><tr><th>Disciplina</th><th>Média Final</th><th>Frequência</th><th>Carga Horária</th><th>Professor</th><th>Titulação</th><th>Situação</th></tr>";
						listaDisciplinaCursadasMinistadas = "<table border=1 cellspacing=0 cellpadding=1 bordercolor=\"000000\"><tr><th>Modulos</th><th>Carga Horaria</th><th>Media</th><th>Situacao Final</th><th>Professor</th><th>Titulacao</th></tr>";
						listaDisciplinaVersoDiploma   =  "<table border=1 cellspacing=0 cellpadding=1 bordercolor=\"000000\"><tr><th>Modulos</th><th>Docentes Responsáveis</th><th>Titulação</th><th>H/A</th><th>% Frequência</th><th>MédiaFinal</th></tr>";
					}
					listaDisciplina = listaDisciplina + " <tr><td>" + hist.getDisciplina().getNome().toUpperCase() + " </td><td> " + hist.getMediaFinal_Apresentar() + " </td><td> " + hist.getFrequencia_Apresentar() + " </td><td> " + hist.getGradeDisciplinaVO().getCargaHoraria() + " </td><td> " + hist.getNomeProfessor().toUpperCase() + " </td><td> " + hist.getTitulacaoProfessor_Apresentar() + " </td><td> " + hist.getSituacao_Apresentar().toUpperCase() + "</td></tr>";
					listaDisciplinaCursadasMinistadas = listaDisciplinaCursadasMinistadas + " <tr><td>" + hist.getDisciplina().getNome().toUpperCase() + " </td><td> " +hist.getGradeDisciplinaVO().getCargaHoraria()  + " </td><td> " + hist.getMediaFinal_Apresentar() + " </td><td> " +   hist.getSituacao_Apresentar().toUpperCase()  + " </td><td> " + hist.getNomeProfessor().toUpperCase() + " </td><td> " + hist.getTitulacaoProfessor_Apresentar() + " </td></tr>";
					listaDisciplinaVersoDiploma = listaDisciplinaVersoDiploma + " <tr><td>" + hist.getDisciplina().getNome().toUpperCase() + " </td><td> " + hist.getNomeProfessor().toUpperCase() + " </td><td> " + hist.getTitulacaoProfessor_Apresentar() + " </td><td> " +hist.getGradeDisciplinaVO().getCargaHoraria()  + " </td><td> " +hist.getFrequencia_Apresentar()  + " </td><td> "  + hist.getMediaFinal_Apresentar() +" </td></tr>";

					primeiraVez = false;
				} else {
					cargaHorariaDisciplinas = cargaHorariaDisciplinas + hist.getGradeDisciplinaVO().getCargaHoraria().intValue();
					listaDisciplina = listaDisciplina + "<br />" + hist.getGradeDisciplinaVO().getDisciplina().getNome();
				}
				tituloMonografia = hist.getMatricula().getTituloMonografia();
			}
			if(!Uteis.isAtributoPreenchido(tituloMonografia)) {
				tituloMonografia ="";
			}
			
			 listaDisciplinaCursadasMinistadas = listaDisciplinaCursadasMinistadas + "<tr><td ><b>Artigo Cientifico: </b></td><td colspan='5' align=\"left\" >" + tituloMonografia + "</td> </tr>";
			totalMediaFinal = ((List<HistoricoVO>) getListaDisciplinasCursadasOuMinistradas()).stream().filter(p-> Uteis.isAtributoPreenchido(p.getMediaFinal())).collect(Collectors.summarizingDouble(HistoricoVO::getMediaFinal)).getAverage();
			totalMediaFinal = Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaDouble(totalMediaFinal ,1) ;
			 listaDisciplinaCursadasMinistadas = listaDisciplinaCursadasMinistadas + "<tr><td ><b>TOTAL </b></td><td align=\"center\" >" + cargaHorariaDisciplinas + "</td> <td ><b> </b></td><td align=\"center\" ></td> <td ><b>MEDIA FINAL :</b> " + totalMediaFinal + "</td><td ><b></b> </td></tr>";
			listaDisciplina = listaDisciplina + "</table>";
			listaDisciplinaCursadasMinistadas = listaDisciplinaCursadasMinistadas + "</table>";
			listaDisciplinaVersoDiploma = listaDisciplinaVersoDiploma + "</table>";
		}

		String listaDisciplinaDiploma = "";
		Integer cargaHorariaDisciplinasDiploma = 0;
		Integer totalCargaHorariaDiploma = 0;
		boolean primeiraVezDiploma = true;
		if (!getListaDisciplinasCursadasOuMinistradas().isEmpty()) {
			Iterator i = getListaDisciplinasCursadasOuMinistradas().iterator();
			while (i.hasNext()) {
				HistoricoVO hist = (HistoricoVO) i.next();
				cargaHorariaDisciplinasDiploma = cargaHorariaDisciplinasDiploma + hist.getGradeDisciplinaVO().getCargaHoraria().intValue();
				if (primeiraVezDiploma) {
					listaDisciplinaDiploma = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
				}
				listaDisciplinaDiploma = listaDisciplinaDiploma + "<tr><td style=\"border-right:1px #000 solid;\">" + hist.getDisciplina().getNome().toUpperCase() + "</td><td width=\"67\" align=\"center\">" + hist.getGradeDisciplinaVO().getCargaHoraria() + "</td></tr>";
				primeiraVezDiploma = false;
			}
			totalCargaHorariaDiploma = cargaHorariaDisciplinasDiploma + impressaoContratoVO.getCargaHorariaRealizadaAtividadeComplementar();
			listaDisciplinaDiploma = listaDisciplinaDiploma + "<tr><td style=\"border-top:1px #000 solid;border-right:1px #000 solid;\"><b>SUB-TOTAL</b></td><td align=\"center\" style=\"border-top:1px #000 solid;\">" + cargaHorariaDisciplinasDiploma + "</td></tr>";
			listaDisciplinaDiploma = listaDisciplinaDiploma + "<tr><td style=\"border-top:1px #000 solid;border-right:1px #000 solid;\">Prática Profissional<br />Estágio Supervisionado</td><td align=\"center\" style=\"border-top:1px #000 solid;\">" + impressaoContratoVO.getCargaHorariaRealizadaAtividadeComplementar() + "<br />-</td>";
			listaDisciplinaDiploma = listaDisciplinaDiploma + "<tr><td style=\"border-top:1px #000 solid;border-right:1px #000 solid;\"><b>TOTAL GERAL</b></td><td align=\"center\" style=\"border-top:1px #000 solid;\">" + totalCargaHorariaDiploma + "</td></tr></table>";
		}
		
		
		String listaDisciplinaPeriodoLetivoAtual = "";
		Integer cargaHorariaDisciplinasPeriodoLetivoAtual = 0;
		Integer totalCargaHorariaDiplomaPeriodoLetivoAtual = 0;
		boolean primeiraVezPeriodoLetivoAtual = true;
		if (!impressaoContratoVO.getListaDisciplinasPeriodoLetivoAtual().isEmpty()) {
			Iterator i = impressaoContratoVO.getListaDisciplinasPeriodoLetivoAtual().iterator();
			while (i.hasNext()) {
				HistoricoVO hist = (HistoricoVO) i.next();
				cargaHorariaDisciplinasPeriodoLetivoAtual = cargaHorariaDisciplinasPeriodoLetivoAtual + hist.getCargaHorariaDisciplina();
				if (primeiraVezPeriodoLetivoAtual) {
					listaDisciplinaPeriodoLetivoAtual = "<table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\"> <th>Codigo</th><th>Nome Disciplina</th> <th>C.H</th> <th>Turma</th> <th>Período</th> <th>Situação Disc.</th>";
				}
				listaDisciplinaPeriodoLetivoAtual = listaDisciplinaPeriodoLetivoAtual + "<tr><td >" + hist.getDisciplina().getCodigo() + "</td><td >" + hist.getDisciplina().getNome().toUpperCase() + "</td><td width=\"67\" align=\"center\">" + hist.getCargaHorariaDisciplina() + "</td><td>" + hist.getMatriculaPeriodoTurmaDisciplina().getTurma().getIdentificadorTurma() + "</td><td>" + hist.getPeriodoLetivoMatrizCurricular().getDescricao() + "</td><td>" + hist.getTipoHistorico_Apresentar() + "</td></tr>";
				primeiraVezPeriodoLetivoAtual = false;
			}
			totalCargaHorariaDiplomaPeriodoLetivoAtual = cargaHorariaDisciplinasPeriodoLetivoAtual;
			listaDisciplinaPeriodoLetivoAtual = listaDisciplinaPeriodoLetivoAtual + "<tr><td ><b>TOTAL GERAL</b></td><td align=\"center\" >" + totalCargaHorariaDiplomaPeriodoLetivoAtual + "</td></tr></table>";
		}

		String listaDisciplinaCertificado = "";
		Integer cargaHorariaDisciplinasCertificado = 0;
		boolean primeiraVezCertificado = true;
		if (!getListaDisciplinaCertificado().isEmpty()) {
			Iterator i = getListaDisciplinaCertificado().iterator();
			while (i.hasNext()) {
				HistoricoVO hist = (HistoricoVO) i.next();
				cargaHorariaDisciplinasCertificado = cargaHorariaDisciplinasCertificado + hist.getGradeDisciplinaVO().getCargaHoraria().intValue();
				if (primeiraVezCertificado) {
					listaDisciplinaCertificado = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
				}
				listaDisciplinaCertificado = listaDisciplinaCertificado + "<tr><td width=\"384\" style=\"border-right:1px #000 solid;\">" + hist.getDisciplina().getNome().toUpperCase() + "</td><td width=\"82\" align=\"center\">" + hist.getGradeDisciplinaVO().getCargaHoraria() + "</td></tr>";
				primeiraVezCertificado = false;
			}
			listaDisciplinaCertificado = listaDisciplinaCertificado + "<tr><td style=\"border-top:1px #000 solid;border-right:1px #000 solid;\"><b>TOTAL GERAL</b></td><td align=\"center\" style=\"border-top:1px #000 solid;\">" + cargaHorariaDisciplinasDiploma + "</td></tr></table>";
		}

		String listaDisciplinaAprovada = "";
		Integer cargaHorariaDisciplinaAprovada = 0;
		boolean primeiraVezDisciplinaAprovada = true;
		if (!listaDisciplinasAprovadasPeriodoLetivo.isEmpty()) {
			Iterator i = listaDisciplinasAprovadasPeriodoLetivo.iterator();
			while (i.hasNext()) {
				CertificadoCursoExtensaoDisciplinasRelVO disciplinaAprovada = (CertificadoCursoExtensaoDisciplinasRelVO) i.next();
				cargaHorariaDisciplinaAprovada += Integer.valueOf(disciplinaAprovada.getCargaHoraria());
				if (primeiraVezDisciplinaAprovada) {
					listaDisciplinaAprovada = "<table cellspacing=0 cellpadding=1 border=1 bordercolor=\"000000\"><tr style=\"font-size: 11px;\"><th>Disciplinas</th><th>Carga Horária</th></tr>";
				}
				listaDisciplinaAprovada += " <tr style=\"font-size: 11px;\"><td>" + disciplinaAprovada.getNome() + " </td><td> " + disciplinaAprovada.getCargaHoraria() + "</td></tr>";
				primeiraVezDisciplinaAprovada = false;
			}
			listaDisciplinaAprovada += " <tr style=\"font-size: 11px;\"><td>Total </td><td> " + cargaHorariaDisciplinaAprovada.toString() + "</td></tr>";
			listaDisciplinaAprovada += "</table>";
		}

		String listaDisciplinaHistorico = "";
		Integer cargaHorariaDisciplinaHistorico = 0;
		String nomeMonografia = "";
		String notaMonografia = "";
		boolean primeiraVezDisciplinaHistorico = true;
		if (!listaDisciplinasAprovadasPeriodoLetivo.isEmpty()) {
			Iterator i = listaDisciplinasAprovadasPeriodoLetivo.iterator();
			while (i.hasNext()) {
				CertificadoCursoExtensaoDisciplinasRelVO disciplinaHistorico = (CertificadoCursoExtensaoDisciplinasRelVO) i.next();
				cargaHorariaDisciplinaHistorico += Integer.valueOf(disciplinaHistorico.getCargaHoraria());
				if (primeiraVezDisciplinaHistorico) {
					listaDisciplinaHistorico = "<table width=\"100%\" cellspacing=0 cellpadding=1 border=1 bordercolor=\"000000\"><tr><th rowspan=\"2\"><b>Disciplinas</b></th><th rowspan=\"2\"><b>Carga Horária</b></th><th rowspan=\"2\"><b>Nota</b></th><th rowspan=\"2\"><b>Freqüencia</b></th><th colspan=\"2\"><b>Corpo Docente</b></th></tr><tr><th colspan=\"2\"><b>Nome - Titulação</b></th></tr>";
				}
				listaDisciplinaHistorico += "<tr><th>" + disciplinaHistorico.getNome() + "</th><th>" + disciplinaHistorico.getCargaHoraria() + "</th><th>" + disciplinaHistorico.getMedia() + "</th><th>" + disciplinaHistorico.getFrequencia() + "</th><th colspan=\"2\">" + disciplinaHistorico.getNomeProfessor();
				if (!disciplinaHistorico.getTitulacaoProfessor().equals("")) {
					listaDisciplinaHistorico += " / " + disciplinaHistorico.getTitulacaoProfessor() + "</th></tr>";
				} else {
					listaDisciplinaHistorico += "</th></tr>";
				}
				primeiraVezDisciplinaHistorico = false;
				nomeMonografia = disciplinaHistorico.getNomeMonografia();
				notaMonografia = disciplinaHistorico.getNotaMonografia();
			}
			listaDisciplinaHistorico += "<tr><th colspan=\"4\" align=\"left\">" + nomeMonografia + "</th><th align=\"left\">Nota: " + notaMonografia + "</th><th><b>Carga Horária Total: " + cargaHorariaDisciplinaHistorico.toString() + "</b></th></tr>";
			listaDisciplinaHistorico += "</table>";
		}

		// DISCIPLINA CERTIFICADO ENSINO MÉDIO
		StringBuilder listaDisciplinaCertificadoEnsinoMedio = new StringBuilder();
		listaDisciplinaCertificadoEnsinoMedio.append("<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family:Arial, Helvetica, sans-serif;font-size:11px;\">");
		listaDisciplinaCertificadoEnsinoMedio.append("<tr><td height=\"18\" colspan=\"2\" align=\"right\" style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;padding-right:100px;\" width=\"70%\"><strong>EDUCAÇÃO BÁSICA</strong></td>");

		Map<Integer, Map<String, Integer>> instituicoes = new HashMap<Integer, Map<String, Integer>>(0);
		Map<Integer, Integer> anos = new HashMap<Integer, Integer>(0);
		Map<String, Map<Integer, Integer>> disciplinasBaseNacional = new HashMap<String, Map<Integer, Integer>>(0);
		Map<String, Map<Integer, Integer>> disciplinasDiversificadas = new HashMap<String, Map<Integer, Integer>>(0);
		Iterator<HistoricoVO> i = impressaoContratoVO.getListaDisciplinaCertificadoEnsinoMedio().iterator();
		while (i.hasNext()) {
			HistoricoVO disciplinaHistorico = (HistoricoVO) i.next();
			SituacaoHistorico situacaoHistorico = SituacaoHistorico.getEnum(disciplinaHistorico.getSituacao());
			if (situacaoHistorico.getHistoricoAprovado()) {
				if (!anos.containsKey(disciplinaHistorico.getPeriodoLetivoCursada().getPeriodoLetivo())) {
					anos.put(disciplinaHistorico.getPeriodoLetivoCursada().getPeriodoLetivo(), 0);
					Map<String, Integer> instituicao = new HashMap<String, Integer>();
					instituicao.put(disciplinaHistorico.getInstituicao(), 0);
					instituicoes.put(disciplinaHistorico.getPeriodoLetivoCursada().getPeriodoLetivo(), instituicao);
				}
				if (anos.get(disciplinaHistorico.getPeriodoLetivoCursada().getPeriodoLetivo()).equals(0) || anos.get(disciplinaHistorico.getPeriodoLetivoCursada().getPeriodoLetivo()) > disciplinaHistorico.getAnoHistoricoInteiro()) {
					anos.put(disciplinaHistorico.getPeriodoLetivoCursada().getPeriodoLetivo(), disciplinaHistorico.getAnoHistoricoInteiro());
				}
				for (String instituicao : instituicoes.get(disciplinaHistorico.getPeriodoLetivoCursada().getPeriodoLetivo()).keySet()) {
					instituicoes.get(disciplinaHistorico.getPeriodoLetivoCursada().getPeriodoLetivo()).put(instituicao, instituicoes.get(disciplinaHistorico.getPeriodoLetivoCursada().getPeriodoLetivo()).get(instituicao) + disciplinaHistorico.getCargaHorariaDisciplina());
				}
				if (!disciplinaHistorico.getParteDiversificada()) {
					if (!disciplinasBaseNacional.containsKey(disciplinaHistorico.getDisciplina().getNome())) {
						disciplinasBaseNacional.put(disciplinaHistorico.getDisciplina().getNome(), new HashMap<Integer, Integer>());
					}
					disciplinasBaseNacional.get(disciplinaHistorico.getDisciplina().getNome()).put(disciplinaHistorico.getPeriodoLetivoCursada().getPeriodoLetivo(), disciplinaHistorico.getCargaHorariaDisciplina());
				} else {
					if (!disciplinasDiversificadas.containsKey(disciplinaHistorico.getDisciplina().getNome())) {
						disciplinasDiversificadas.put(disciplinaHistorico.getDisciplina().getNome(), new HashMap<Integer, Integer>());
					}
					disciplinasDiversificadas.get(disciplinaHistorico.getDisciplina().getNome()).put(disciplinaHistorico.getPeriodoLetivoCursada().getPeriodoLetivo(), disciplinaHistorico.getCargaHorariaDisciplina());
				}
			}
		}
		// LISTA DOS ANOS TOPO
		if (anos.containsKey(1)) {
			listaDisciplinaCertificadoEnsinoMedio.append("<td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\" align=\"center\" width=\"10%\">").append(anos.get(1)).append("</td>");
		} else {
			listaDisciplinaCertificadoEnsinoMedio.append("<td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\" align=\"center\" width=\"10%\">***</td>");
		}
		if (anos.containsKey(2)) {
			listaDisciplinaCertificadoEnsinoMedio.append("<td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\" align=\"center\" width=\"10%\">").append(anos.get(2)).append("</td>");
		} else {
			listaDisciplinaCertificadoEnsinoMedio.append("<td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\" align=\"center\" width=\"10%\">***</td>");
		}
		if (anos.containsKey(3)) {
			listaDisciplinaCertificadoEnsinoMedio.append("<td style=\"border-bottom:1px #000000 solid;\" align=\"center\" width=\"10%\">").append(anos.get(3)).append("</td>");
		} else {
			listaDisciplinaCertificadoEnsinoMedio.append("<td style=\"border-bottom:1px #000000 solid;\" align=\"center\" width=\"10%\">***</td>");
		}

		listaDisciplinaCertificadoEnsinoMedio.append("</tr>");
		listaDisciplinaCertificadoEnsinoMedio.append("<tr><td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\" width=\"40%\" rowspan=\"3\" align=\"center\"><strong>BASE NACIONAL COMUM</strong></td>");
		listaDisciplinaCertificadoEnsinoMedio.append("<td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\" width=\"30%\" height=\"36\" align=\"center\">Disciplinas</td>");
		listaDisciplinaCertificadoEnsinoMedio.append("<td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\" align=\"center\" width=\"10%\">1° Ano</td>");
		listaDisciplinaCertificadoEnsinoMedio.append("<td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\" align=\"center\" width=\"10%\">2° Ano</td>");
		listaDisciplinaCertificadoEnsinoMedio.append("<td style=\"border-bottom:1px #000000 solid;\" align=\"center\" width=\"10%\">3° Ano</td></tr>");
		listaDisciplinaCertificadoEnsinoMedio.append("<tr><td colspan=\"4\" rowspan=\"2\" valign=\"top\">");
		listaDisciplinaCertificadoEnsinoMedio.append(realizarCriacaoTabelaDisciplinaHistoricoCertificadoEnsinoMedio(disciplinasBaseNacional));
		listaDisciplinaCertificadoEnsinoMedio.append("</td></tr><tr></tr><tr><td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\" align=\"center\" width=\"40%\"><strong>PARTE DIVERSIFICADA</strong></td><td colspan=\"4\" valign=\"top\">");
		listaDisciplinaCertificadoEnsinoMedio.append(realizarCriacaoTabelaDisciplinaHistoricoCertificadoEnsinoMedio(disciplinasDiversificadas));
		listaDisciplinaCertificadoEnsinoMedio.append("</td></tr>");
		Integer totalGeralCargaHoraria = 0;
		// LISTA DOS ANOS TOTAL
		for (Integer periodoLetivo : instituicoes.keySet()) {
			for (String instituicao : instituicoes.get(periodoLetivo).keySet()) {
				totalGeralCargaHoraria += instituicoes.get(periodoLetivo).get(instituicao);
				listaDisciplinaCertificadoEnsinoMedio.append("<tr><td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;padding-left:5px;\" height=\"19\">").append(instituicao).append("</td>");
				listaDisciplinaCertificadoEnsinoMedio.append("<td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\"><strong style=\"padding-left:5px;\">").append(anos.get(periodoLetivo)).append("</strong></td>");
				if (periodoLetivo.equals(1)) {
					listaDisciplinaCertificadoEnsinoMedio.append("<td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\" align=\"center\"><strong>").append(instituicoes.get(periodoLetivo).get(instituicao)).append("</strong></td>");
					listaDisciplinaCertificadoEnsinoMedio.append("<td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\">&nbsp;</td>");
					listaDisciplinaCertificadoEnsinoMedio.append("<td style=\"border-bottom:1px #000000 solid;\">&nbsp;</td></tr>");
				} else if (periodoLetivo.equals(2)) {
					listaDisciplinaCertificadoEnsinoMedio.append("<td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\">&nbsp;</td>");
					listaDisciplinaCertificadoEnsinoMedio.append("<td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\" align=\"center\"><strong>").append(instituicoes.get(periodoLetivo).get(instituicao)).append("</strong></td>");
					listaDisciplinaCertificadoEnsinoMedio.append("<td style=\"border-bottom:1px #000000 solid;\">&nbsp;</td></tr>");
				} else {
					listaDisciplinaCertificadoEnsinoMedio.append("<td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\">&nbsp;</td>");
					listaDisciplinaCertificadoEnsinoMedio.append("<td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\">&nbsp;</td>");
					listaDisciplinaCertificadoEnsinoMedio.append("<td style=\"border-bottom:1px #000000 solid;\" align=\"center\"><strong>").append(instituicoes.get(periodoLetivo).get(instituicao)).append("</strong></td>");
				}
			}
		}

		listaDisciplinaCertificadoEnsinoMedio.append("<tr><td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\" height=\"19\" colspan=\"2\" align=\"center\"><strong>Total Geral</strong></td>");
		listaDisciplinaCertificadoEnsinoMedio.append("<td colspan=\"3\" style=\"border-bottom:1px #000000 solid;\" align=\"center\"><strong>").append(totalGeralCargaHoraria).append("</strong></td></tr>");
		listaDisciplinaCertificadoEnsinoMedio.append("</table>");
		
		List<DisciplinaVO> disciplinaVOs = impressaoContratoVO.getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs()
				.stream()
				.map(MatriculaPeriodoTurmaDisciplinaVO::getDisciplina)
				.sorted(Comparator.comparing(DisciplinaVO::getNome))
				.distinct()
				.collect(Collectors.toList());

		while (texto.indexOf(marcador.getTag()) != -1) {
			int tamanho = obterTamanhoTag(marcador.getTag());
			String mar = obterTagSemTextoSemTamanho(marcador.getTag());
			String textoTag = obterTextoTag(marcador.getTag());

			if (mar.equalsIgnoreCase("Codigo_Disciplina")) {
				texto = substituirTag(texto, marcador.getTag(), codigo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Nome_Disciplina")) {
				texto = substituirTag(texto, marcador.getTag(), nome, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Abreviatura_Disciplina")) {
				texto = substituirTag(texto, marcador.getTag(), abreviatura, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Situacao_Disciplina")) {
				texto = substituirTag(texto, marcador.getTag(), situacaoDisciplina, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Semestre_Disciplina")) {
				texto = substituirTag(texto, marcador.getTag(), semestre, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Nota_Disciplina")) {
				texto = substituirTag(texto, marcador.getTag(), nota, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Frequencia_Disciplina")) {
				texto = substituirTag(texto, marcador.getTag(), frequencia, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Ano_Disciplina")) {
				texto = substituirTag(texto, marcador.getTag(), ano, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DiaInicio_Disciplina")) {
				texto = substituirTag(texto, marcador.getTag(), diaInicio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DiaFim_Disciplina")) {
				texto = substituirTag(texto, marcador.getTag(), diaFim, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Mes_Disciplina")) {
				texto = substituirTag(texto, marcador.getTag(), mes, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CargaHoraria_Disciplina")) {
				texto = substituirTag(texto, marcador.getTag(), cargaHorarioDisciplina, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("BimestreAnoConclusao_Disciplina")) {
				texto = substituirTag(texto, marcador.getTag(), bimestreAnoConclusaoDisciplina, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ListaDisciplinasCursadasOuMinistradas_Disciplina")) {
				texto = substituirTag(texto, marcador.getTag(), listaDisciplina, textoTag, tamanho);
				substituirTagComLista(texto, marcador.getTag(), getListaDisciplinasCursadasOuMinistradas(), textoTag);
				getParametrosRel().put("tituloMonografia", tituloMonografia);
			} else if (mar.equalsIgnoreCase("CargaHorariaDisciplinasCursadasOuMinistradas_Disciplina")) {
				texto = substituirTag(texto, marcador.getTag(), cargaHorariaDisciplinas.toString(), textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ListaDisciplinasAprovadasPeriodoLetivo_Disciplina")) {
				texto = substituirTag(texto, marcador.getTag(), listaDisciplinaAprovada, textoTag, tamanho);
				substituirTagComLista(texto, marcador.getTag(), getListaDisciplinasAprovadasPeriodoLetivo(), textoTag);
				getParametrosRel().put("cargaHorariaDisciplinaAprovada", cargaHorariaDisciplinaAprovada);
			} else if (mar.equalsIgnoreCase("ListaDisciplinasHistoricoPeriodoLetivo_Disciplina")) {
				texto = substituirTag(texto, marcador.getTag(), listaDisciplinaHistorico, textoTag, tamanho);
				substituirTagComLista(texto, marcador.getTag(), getListaDisciplinasAprovadasPeriodoLetivo(), textoTag);
				getParametrosRel().put("cargaHorariaDisciplinaHistorico", cargaHorariaDisciplinaHistorico);
			} else if (mar.equalsIgnoreCase("ListaDisciplinasHistoricoDiploma_Disciplina")) {
				texto = substituirTag(texto, marcador.getTag(), listaDisciplinaDiploma, textoTag, tamanho);
				substituirTagComLista(texto, marcador.getTag(), getListaDisciplinasCursadasOuMinistradas(), textoTag);
				getParametrosRel().put("cargaHorariaRealizadaAtividadeComplementar", impressaoContratoVO.getCargaHorariaRealizadaAtividadeComplementar());
				getParametrosRel().put("totalCargaHorariaDiploma", totalCargaHorariaDiploma);
			} else if (mar.equalsIgnoreCase("ListaDisciplinasHistoricoCertificado_Disciplina")) {
				texto = substituirTag(texto, marcador.getTag(), listaDisciplinaCertificado, textoTag, tamanho);
				substituirTagComLista(texto, marcador.getTag(), getListaDisciplinaCertificado(), textoTag);
			} else if (mar.equalsIgnoreCase("ListaDisciplinaCertificadoEnsinoMedio_Disciplina")) {
				texto = substituirTag(texto, marcador.getTag(), listaDisciplinaCertificadoEnsinoMedio.toString(), textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Conteudo_PlanoEnsino_Disciplina")) {
				texto = substituirTag(texto, marcador.getTag(), conteudoPlanoEnsinoDisciplina, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ListaDisciplinasPeriodoLetivoAtual_Disciplina")) {
				texto = substituirTag(texto, marcador.getTag(), listaDisciplinaPeriodoLetivoAtual, textoTag, tamanho);
				substituirTagComLista(texto, marcador.getTag(), impressaoContratoVO.getListaDisciplinasPeriodoLetivoAtual(), textoTag);
			} else if (mar.equalsIgnoreCase("ListaDisciplinasAprovadasCertificado_Disciplina")) {
				substituirTagComLista(texto, marcador.getTag(), impressaoContratoVO.getListaDisciplinasCursadasOuMinistradas(), textoTag);
			} else if (mar.equalsIgnoreCase("ListaDisciplinasNotaFrequenciaCargaHorariaProfessorSituacao_Disciplina")) {
				substituirTagComLista(texto, marcador.getTag(), impressaoContratoVO.getListaDisciplinasCursadasOuMinistradas(), textoTag);
			} else if (mar.equalsIgnoreCase("ListaDisciplinasMatriculadasCurso")) {
				substituirTagComLista(texto, marcador.getTag(), disciplinaVOs, textoTag);
			}else if(mar.equalsIgnoreCase("ListaDisciplinasHistoricoPeriodo_Disciplina")) {		
				texto = substituirTag(texto, marcador.getTag(), listaDisciplinaCursadasMinistadas, textoTag, tamanho);
				substituirTagComLista(texto, marcador.getTag(), getListaDisciplinasCursadasOuMinistradas(), textoTag);
				getParametrosRel().put("tituloMonografia", tituloMonografia);
				getParametrosRel().put("totalMediaFinal", totalMediaFinal);
			
			} else if (mar.equalsIgnoreCase("ListaDisciplinasVersoDiploma_Disciplina")) {
				texto = substituirTag(texto, marcador.getTag(), listaDisciplinaVersoDiploma, textoTag, tamanho);
				substituirTagComLista(texto, marcador.getTag(), impressaoContratoVO.getListaDisciplinasCursadasOuMinistradas(), textoTag);
			}
			else if (mar.equalsIgnoreCase("ListaDisciplinasHistoricoPeriodoLetivoSituacao_Disciplina")) {
				substituirTagComLista(texto, marcador.getTag(), impressaoContratoVO.getListaDisciplinasHistoricoPeriodoLetivoSituacao(), textoTag);
			}
			break;
		}
		return texto;
	}

	public StringBuilder realizarCriacaoTabelaDisciplinaHistoricoCertificadoEnsinoMedio(Map<String, Map<Integer, Integer>> disciplinas) {
		StringBuilder table = new StringBuilder("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
		for (String disciplinaKey : disciplinas.keySet()) {
			Map<Integer, Integer> periodoCargaHoraria = disciplinas.get(disciplinaKey);
			table.append("<tr><td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;padding-left:5px;\" width=\"30.15%\" height=\"14\">").append(disciplinaKey).append("</td>");
			if (periodoCargaHoraria.containsKey(1)) {
				table.append("<td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\" align=\"center\" width=\"10%\">").append(periodoCargaHoraria.get(1)).append("</td>");
			} else {
				table.append("<td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\" align=\"center\" width=\"10%\">***</td>");
			}
			if (periodoCargaHoraria.containsKey(2)) {
				table.append("<td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\" align=\"center\" width=\"10%\">").append(periodoCargaHoraria.get(2)).append("</td>");
			} else {
				table.append("<td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\" align=\"center\" width=\"10%\">***</td>");
			}
			if (periodoCargaHoraria.containsKey(3)) {
				table.append("<td style=\"border-bottom:1px #000000 solid;\" align=\"center\" width=\"10%\">").append(periodoCargaHoraria.get(3)).append("</td></tr>");
			} else {
				table.append("<td style=\"border-bottom:1px #000000 solid;\" align=\"center\" width=\"10%\">***</td>");
			}
		}
		if (disciplinas.isEmpty()) {
			table.append("<tr><td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\" height=\"14\" width=\"30.15%\"></td>");
			table.append("<td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\" width=\"10%\">&nbsp;</td>");
			table.append("<td style=\"border-bottom:1px #000000 solid;border-right:1px #000000 solid;\" width=\"10%\">&nbsp;</td>");
			table.append("<td style=\"border-bottom:1px #000000 solid;\" width=\"10%\">&nbsp;</td></tr>");
		}
		table.append("</table>");
		return table;
	}

	public String substituirTagOutras(String texto, MarcadorVO marcador, ImpressaoContratoVO impressaoContrato) throws Exception {
		MatriculaVO matricula = impressaoContrato.getMatriculaVO();
		UsuarioVO usuario = impressaoContrato.getUsuarioLogado();
		String emBranco = obterStringEmBrancoParaTag(marcador);
		String dataAtual = Uteis.getData(new Date());
		if (dataAtual == null) {
			dataAtual = emBranco;
		}
		String dataAtualExtenso = Uteis.getDataCidadeDiaMesPorExtensoEAno(matricula.getUnidadeEnsino().getCidade().getNome() + "/" + matricula.getUnidadeEnsino().getCidade().getEstado().getSigla(), new Date(), true);
		if (dataAtualExtenso == null || dataAtualExtenso.equals("0") || dataAtualExtenso.equals("")) {
			dataAtualExtenso = emBranco;
		}
		String dataAtualExtenso2 = Uteis.getDataCidadeDiaPorExtensoMesPorExtensoEAnoPorExtenso(matricula.getUnidadeEnsino().getCidade().getNome() + "/" + matricula.getUnidadeEnsino().getCidade().getEstado().getSigla(), new Date(), true);
		if (dataAtualExtenso2 == null || dataAtualExtenso2.equals("0") || dataAtualExtenso2.equals("")) {
			dataAtualExtenso2 = emBranco;
		}
		String dataAtualExtensoSemCidade = Uteis.getDataCidadeDiaMesPorExtensoEAno("", new Date(), true);
		if (dataAtualExtensoSemCidade == null || dataAtualExtensoSemCidade.equals("0") || dataAtualExtensoSemCidade.equals("")) {
			dataAtualExtensoSemCidade = emBranco;
		}
		String dataAtualExtensoSemCidade2 = Uteis.getDataCidadeDiaPorExtensoMesPorExtensoEAnoPorExtenso("", new Date(), true);
		if (dataAtualExtensoSemCidade2 == null || dataAtualExtensoSemCidade2.equals("0") || dataAtualExtensoSemCidade2.equals("")) {
			dataAtualExtensoSemCidade2 = emBranco;
		}
		String usuarioResp = usuario.getNome();
		if (usuarioResp == null || usuarioResp.equals("0") || usuarioResp.equals("")) {
			usuarioResp = emBranco;
		}
		String observacao = impressaoContrato.getObservacao();
		if (observacao == null || observacao.equals("0") || observacao.equals("")) {
			observacao = emBranco;
		}

		String funcionarioPrincipal = impressaoContrato.getFuncionarioPrincipalVO().getPessoa().getNome();
		if (funcionarioPrincipal == null || funcionarioPrincipal.equals("0") || funcionarioPrincipal.equals("")) {
			funcionarioPrincipal = emBranco;
		}

		String cargoFuncionarioPrincipal = impressaoContrato.getCargoFuncionarioPrincipal().getNome();
		if (cargoFuncionarioPrincipal == null || cargoFuncionarioPrincipal.equals("0") || cargoFuncionarioPrincipal.equals("")) {
			cargoFuncionarioPrincipal = emBranco;
		}

		String funcionarioSecundario = impressaoContrato.getFuncionarioSecundarioVO().getPessoa().getNome();
		if (funcionarioSecundario == null || funcionarioSecundario.equals("0") || funcionarioSecundario.equals("")) {
			funcionarioSecundario = emBranco;
		}

		String cargoFuncionarioSecundario = impressaoContrato.getCargoFuncionarioSecundario().getNome();
		if (cargoFuncionarioSecundario == null || cargoFuncionarioSecundario.equals("0") || cargoFuncionarioSecundario.equals("")) {
			cargoFuncionarioSecundario = emBranco;
		}

//		String descricaoAreaProfissional = impressaoContrato.getEstagioVO().getAreaProfissionalVO().getDescricaoAreaProfissional();
//		if (descricaoAreaProfissional == null || descricaoAreaProfissional.equals("0") || descricaoAreaProfissional.equals("")) {
//			descricaoAreaProfissional = emBranco;
//		}

		String TituloMonografia  = matricula.getTituloMonografia();
		if (TituloMonografia == null || TituloMonografia.equals("0") || TituloMonografia.equals("")) {
			TituloMonografia = emBranco;
		}
		
		String terceiroFuncionario = impressaoContrato.getFuncionarioTerceiroVO().getPessoa().getNome();
		if (terceiroFuncionario == null || terceiroFuncionario.equals("0") || terceiroFuncionario.equals("")) {
			terceiroFuncionario = emBranco;
		}

		String terceiroFuncionarioCargo = impressaoContrato.getCargoFuncionarioTerceiro().getNome();
		if (terceiroFuncionarioCargo == null || terceiroFuncionarioCargo.equals("0") || terceiroFuncionarioCargo.equals("")) {
			terceiroFuncionarioCargo = emBranco;
		}
		
		while (texto.indexOf(marcador.getTag()) != -1) {
			int tamanho = obterTamanhoTag(marcador.getTag());
			String mar = obterTagSemTextoSemTamanho(marcador.getTag());
			String textoTag = obterTextoTag(marcador.getTag());

			if (mar.equalsIgnoreCase("DataAtual_Outras")) {
				texto = substituirTag(texto, marcador.getTag(), dataAtual, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataAtualExtenso_Outras")) {
				texto = substituirTag(texto, marcador.getTag(), dataAtualExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataAtualExtenso2_Outras")) {
				texto = substituirTag(texto, marcador.getTag(), dataAtualExtenso2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("UsuarioLogado_Outras")) {
				texto = substituirTag(texto, marcador.getTag(), usuarioResp, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Observacao_Outras")) {
				texto = substituirTag(texto, marcador.getTag(), observacao, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Assinatura_PrimeiroFuncionario")) {
				texto = substituirTag(texto, marcador.getTag(), funcionarioPrincipal, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Cargo_PrimeiroFuncionario")) {
				texto = substituirTag(texto, marcador.getTag(), cargoFuncionarioPrincipal, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Assinatura_SegundoFuncionario")) {
				texto = substituirTag(texto, marcador.getTag(), funcionarioSecundario, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Cargo_SegundoFuncionario")) {
				texto = substituirTag(texto, marcador.getTag(), cargoFuncionarioSecundario, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataAtualExtensoSemCidade_Outras")) {
				texto = substituirTag(texto, marcador.getTag(), dataAtualExtensoSemCidade, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataAtualExtensoSemCidade2_Outras")) {
				texto = substituirTag(texto, marcador.getTag(), dataAtualExtensoSemCidade2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Descricao_AreaProfissional")) {
			//	texto = substituirTag(texto, marcador.getTag(), descricaoAreaProfissional, textoTag, tamanho);
			}else if (mar.equalsIgnoreCase("TituloTccMonografia_Outras")) {
				texto = substituirTag(texto, marcador.getTag(), TituloMonografia, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("AssinaturaTerceiroFuncionario_Outras")) {
				texto = substituirTag(texto, marcador.getTag(), terceiroFuncionario, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CargoTerceiroFuncionario_Outras")) {
				texto = substituirTag(texto, marcador.getTag(), terceiroFuncionarioCargo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ListaObservacaoComplementarDiploma_Outras")) {
				substituirTagComLista(texto, marcador.getTag(), impressaoContrato.getObservacaoComplementarDiplomaVOs(), textoTag);
			}
			break;
		}
		return texto;
	}
	
	
	public String substituirTagOutras(String texto, MarcadorVO marcador, FuncionarioVO funcionarioVO) throws Exception {
		
		//MatriculaVO matricula = new MatriculaVO();
		String emBranco = obterStringEmBrancoParaTag(marcador);
		String dataAtual = Uteis.getData(new Date());
		if (dataAtual == null) {
			dataAtual = emBranco;
		}
		String dataAtualExtenso = Uteis.getDataCidadeDiaMesPorExtensoEAno(funcionarioVO.getUnidadeEnsino().getCidade().getNome() + "/" + funcionarioVO.getUnidadeEnsino().getCidade().getEstado().getSigla(), new Date(), true);
		

		while (texto.indexOf(marcador.getTag()) != -1) {
			int tamanho = obterTamanhoTag(marcador.getTag());
			String mar = obterTagSemTextoSemTamanho(marcador.getTag());
			String textoTag = obterTextoTag(marcador.getTag());

			if (mar.equalsIgnoreCase("DataAtual_Outras")) {
				texto = substituirTag(texto, marcador.getTag(), dataAtual, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataAtualExtenso_Outras")) {
				texto = substituirTag(texto, marcador.getTag(), dataAtualExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataAtualExtenso2_Outras")) {
				texto = substituirTag(texto, marcador.getTag(), "", textoTag, tamanho);
			} 
			break;
		}
		return texto;
	}

	public String substituirTagAluno(String texto, ImpressaoContratoVO impressaoContratoVO, MarcadorVO marcador, DadosComerciaisVO dc) throws Exception {
		String emBranco = obterStringEmBrancoParaTag(marcador);
		PessoaVO pessoa = Uteis.isAtributoPreenchido(impressaoContratoVO.getPessoaVO()) ? impressaoContratoVO.getPessoaVO() : impressaoContratoVO.getMatriculaVO().getAluno();
		PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO = impressaoContratoVO.getPessoaEmailInstitucionalVO();
		MatriculaVO matriculaVO = impressaoContratoVO.getMatriculaVO();
		MatriculaEnadeVO matriculaEnadeDataVO = impressaoContratoVO.getMatriculaEnadeDataVO();
		String codigo = pessoa.getCodigo().toString();
		if (codigo == null || codigo.equals("0") || codigo.equals("")) {
			codigo = emBranco;
		}
		String nome = pessoa.getNome();
		if (nome == null || nome.equals("0") || nome.equals("")) {
			nome = emBranco;
		}
		String registroAcademico = pessoa.getRegistroAcademico();
		if (registroAcademico == null || registroAcademico.equals("0") || registroAcademico.equals("")) {
			registroAcademico = emBranco;
		}
		String banco = pessoa.getBanco();
		if (banco == null || banco.equals("0") || banco.equals("") ) {
			banco = emBranco;
		}
		String agencia = pessoa.getAgencia();
		if (agencia == null || agencia.equals("0") || agencia.equals("")) {
			agencia = emBranco;
		}
		String contaCorrente = pessoa.getContaCorrente();
		if (contaCorrente == null || contaCorrente.equals("0") || contaCorrente.equals("")) {
			contaCorrente = emBranco;
		}
		String universidadeParceira = pessoa.getUniversidadeParceira();
		if (universidadeParceira == null || universidadeParceira.equals("0") || universidadeParceira.equals("")) {
			universidadeParceira = emBranco;
		}
		String modalidadeBolsa = pessoa.getModalidadeBolsa().toString();
		if (modalidadeBolsa == null || modalidadeBolsa.equals("0") || modalidadeBolsa.equals("") || modalidadeBolsa.equals("NENHUM")) {
			modalidadeBolsa = emBranco;
		}
		String valorBolsa = Uteis.formatarDoubleParaMoeda(pessoa.getValorBolsa());
		if (valorBolsa == null) {
			valorBolsa = emBranco;
		}
		String valorBolsaPorExtenso = emBranco;
		if(Uteis.isAtributoPreenchido(pessoa.getValorBolsa())) {
			Extenso ext = new Extenso();
			ext.setNumber(pessoa.getValorBolsa());
			valorBolsaPorExtenso = ext.toString();		
		}		
		String sexo = pessoa.getSexo_Apresentar();
		if (sexo == null || sexo.equals("0") || sexo.equals("")) {
			sexo = emBranco;
		}
		String preposicaoAouO = "";
		if (pessoa.getSexo().equals("F")) {
			preposicaoAouO = "a";
		} else {
			preposicaoAouO = "o";
		}
		if (preposicaoAouO == null || preposicaoAouO.equals("")) {
			preposicaoAouO = emBranco;
		}
		String naturalidade = pessoa.getNaturalidade().getNome() + " - " + pessoa.getNaturalidade().getEstado().getSigla();
		if (naturalidade == null || naturalidade.equals("0") || naturalidade.equals("")) {
			naturalidade = emBranco;
		}
		String naturalidadeEstado = pessoa.getNaturalidade().getEstado().getNome();
		if (naturalidadeEstado == null || naturalidadeEstado.equals("")) {
			naturalidadeEstado = emBranco;
		}
		String estadoNaturalidade = pessoa.getNaturalidade().getEstado().getSigla();
		if (estadoNaturalidade == null || estadoNaturalidade.equals("0") || estadoNaturalidade.equals("")) {
			estadoNaturalidade = emBranco;
		}
		String nacionalidade = pessoa.getNacionalidade().getNacionalidade();
		if (nacionalidade == null || nacionalidade.equals("0") || nacionalidade.equals("")) {
			nacionalidade = emBranco;
		}
		String email = pessoa.getEmail();
		if (email == null || email.equals("0") || email.equals("")) {
			email = emBranco;
		}
		String email2 = pessoa.getEmail2();
		if (email2 == null || email2.equals("0") || email2.equals("")) {
			email2 = emBranco;
		}
		String emailPessoaInstitucional = pessoaEmailInstitucionalVO.getEmail();
		if (emailPessoaInstitucional == null || emailPessoaInstitucional.equals("0") || emailPessoaInstitucional.equals("")) {
			emailPessoaInstitucional = emBranco;
		}		
		String estadoCivil = pessoa.getEstadoCivil_Apresentar();
		if (estadoCivil == null || estadoCivil.equals("0") || estadoCivil.equals("")) {
			estadoCivil = emBranco;
		}
		String rg = pessoa.getRG();
		if (rg == null || rg.equals("0") || rg.equals("")) {
			rg = emBranco;
		}
		String orgaoEmissor = pessoa.getOrgaoEmissor();
		if (orgaoEmissor == null || orgaoEmissor.equals("0") || orgaoEmissor.equals("")) {
			orgaoEmissor = emBranco;
		}
		String estadoEmissor = pessoa.getEstadoEmissaoRG_Apresentar();
		if (estadoEmissor == null || estadoEmissor.equals("0") || estadoEmissor.equals("")) {
			estadoEmissor = emBranco;
		}
		String dataEmissaoRG = Uteis.getDataAno4Digitos(pessoa.getDataEmissaoRG());
		if (dataEmissaoRG == null || dataEmissaoRG.equals("0") || dataEmissaoRG.equals("")) {
			dataEmissaoRG = emBranco;
		}
		String cpf = pessoa.getCPF();
		if (cpf == null || cpf.equals("0") || cpf.equals("")) {
			cpf = emBranco;
		}
		String idade = Uteis.calcularIdadePessoa(new Date(), pessoa.getDataNasc()).toString();
		if (idade == null || idade.equals("0") || idade.equals("")) {
			idade = emBranco;
		}
		String dataNascimento = Uteis.getData(pessoa.getDataNasc(), "dd/MM/yyyy");
		if (dataNascimento == null || dataNascimento.equals("0") || dataNascimento.equals("")) {
			dataNascimento = emBranco;
		}
		String dataNascimentoExtenso = Uteis.getDataCidadeDiaMesPorExtensoEAno("", pessoa.getDataNasc(), true);
		if (dataNascimentoExtenso == null || dataNascimentoExtenso.equals("0") || dataNascimentoExtenso.equals("")) {
			dataNascimentoExtenso = emBranco;
		}
		String dataNascimentoExtenso2 = Uteis.getDataCidadeDiaMesPorExtensoEAno("", pessoa.getDataNasc(), true);
		if (dataNascimentoExtenso == null || dataNascimentoExtenso.equals("0") || dataNascimentoExtenso.equals("")) {
			dataNascimentoExtenso = emBranco;
		}
		String diaNascimento = Uteis.getData(pessoa.getDataNasc(), "dd");
		if (diaNascimento == null || diaNascimento.equals("0") || diaNascimento.equals("")) {
			diaNascimento = emBranco;
		}
		String mesNascimento = Uteis.getData(pessoa.getDataNasc(), "MMMMM");
		if (mesNascimento == null || mesNascimento.equals("0") || mesNascimento.equals("")) {
			mesNascimento = emBranco;
		}
		String anoNascimento = Uteis.getData(pessoa.getDataNasc(), "yyyy");
		if (anoNascimento == null || anoNascimento.equals("0") || anoNascimento.equals("")) {
			anoNascimento = emBranco;
		}
		String telefoneRecado = pessoa.getTelefoneRecado();
		if (telefoneRecado == null || telefoneRecado.equals("0") || telefoneRecado.equals("")) {
			telefoneRecado = emBranco;
		}
		String telefoneComercial = pessoa.getTelefoneComer();
		if (telefoneComercial == null || telefoneComercial.equals("0") || telefoneComercial.equals("")) {
			telefoneComercial = emBranco;
		}
		String telefoneRes = pessoa.getTelefoneRes();
		if (telefoneRes == null || telefoneRes.equals("0") || telefoneRes.equals("")) {
			telefoneRes = emBranco;
		}
		String celular = pessoa.getCelular();
		if (celular == null || celular.equals("0") || celular.equals("")) {
			celular = emBranco;
		}
		String cep = pessoa.getCEP();
		if (cep == null || cep.equals("0") || cep.equals("")) {
			cep = emBranco;
		}
		String complemento = pessoa.getComplemento();
		if (complemento == null || complemento.equals("0") || complemento.equals("")) {
			complemento = emBranco;
		}
		String numero = pessoa.getNumero();
		if (numero == null || numero.equals("0") || numero.equals("")) {
			numero = emBranco;
		}
		String setor = pessoa.getSetor();
		if (setor == null || setor.equals("0") || setor.equals("")) {
			setor = emBranco;
		}
		String endereco = pessoa.getEndereco();
		if (endereco == null || endereco.equals("0") || endereco.equals("")) {
			endereco = emBranco;
		}
		String complementoendereco = pessoa.getComplemento();
		if (complementoendereco == null || complementoendereco.equals("0") || complementoendereco.equals("")) {
			complementoendereco = emBranco;
		}
		String cidade = pessoa.getCidade().getNome();
		if (cidade == null || cidade.equals("0") || cidade.equals("")) {
			cidade = emBranco;
		}
		String estado = pessoa.getCidade().getEstado().getSigla();
		if (estado == null || estado.equals("0") || estado.equals("")) {
			estado = emBranco;
		}
		String nomePai = pessoa.getFiliacaoVO_Tipo("PA").getNome();
		if (nomePai == null || nomePai.equals("0") || nomePai.equals("")) {
			nomePai = emBranco;
		}
		String nomeMae = pessoa.getFiliacaoVO_Tipo("MA").getNome();
		if (nomeMae == null || nomeMae.equals("0") || nomeMae.equals("")) {
			nomeMae = emBranco;
		}
		String tituloEleitoral = pessoa.getTituloEleitoral();
		if (tituloEleitoral == null || tituloEleitoral.equals("0") || tituloEleitoral.equals("")) {
			tituloEleitoral = emBranco;
		}
		String zonaEleitoral = pessoa.getZonaEleitoral();
		if (zonaEleitoral == null || zonaEleitoral.equals("0") || zonaEleitoral.equals("")) {
			zonaEleitoral = emBranco;
		}
		String deficiencia = pessoa.getDeficiencia();
		if (deficiencia == null || deficiencia.equals("0") || deficiencia.equals("")) {
			deficiencia = emBranco;
		}
		String corraca = CorRaca.getDescricao(pessoa.getCorRaca());
		if (corraca == null || corraca.equals("0") || corraca.equals("")) {
			corraca = emBranco;
		}
		String paginaPessoalAluno = pessoa.getPaginaPessoal();
		if (paginaPessoalAluno == null || paginaPessoalAluno.equals("")) {
			paginaPessoalAluno = emBranco;
		}

		String tipoAdvertencia = impressaoContratoVO.getAdvertenciaVO().getTipoAdvertenciaVO().getDescricao();
		if (tipoAdvertencia == null || tipoAdvertencia.equals("")) {
			tipoAdvertencia = emBranco;
		}
		String descricaoTipoAdvertencia = impressaoContratoVO.getAdvertenciaVO().getAdvertencia();
		if (descricaoTipoAdvertencia == null || descricaoTipoAdvertencia.equals("")) {
			descricaoTipoAdvertencia = emBranco;
		}
		String observacaoAdvertencia = impressaoContratoVO.getAdvertenciaVO().getObservacao();
		if (observacaoAdvertencia == null || observacaoAdvertencia.equals("")) {
			observacaoAdvertencia = emBranco;
		}
		String dataAdvertencia = Uteis.getData(impressaoContratoVO.getAdvertenciaVO().getDataCadastro(), "dd/MM/yyyy");
		if (dataAdvertencia == null || dataAdvertencia.equals("")) {
			dataAdvertencia = emBranco;
		}
		String dataEmissaoRGAnonimizada = impressaoContratoVO.getPessoaVO().getDataEmissaoRGAnonimizada();
		if(dataEmissaoRGAnonimizada == null || dataEmissaoRGAnonimizada.equals("")) {
			dataEmissaoRGAnonimizada = emBranco;
		}
		
		String anoConclusaoGraduacao = "";
		for (FormacaoAcademicaVO formacaoAcademicaVO : pessoa.getFormacaoAcademicaVOs()) {
			if (!anoConclusaoGraduacao.trim().isEmpty()) {
				anoConclusaoGraduacao += "\n";
			}
			if (formacaoAcademicaVO.getEscolaridade().equals("GR")) {
				if (formacaoAcademicaVO.getSituacao().equals("CU")) {
					anoConclusaoGraduacao += UteisData.getAnoData(formacaoAcademicaVO.getDataFim());	
				} else {
					anoConclusaoGraduacao += formacaoAcademicaVO.getAnoDataFim();
				}
			}
		}
		
		String anoConclusaoEnsinoMedio = "";
		for (FormacaoAcademicaVO formacaoAcademicaVO : pessoa.getFormacaoAcademicaVOs()) {
			if (!anoConclusaoEnsinoMedio.trim().isEmpty()) {
				anoConclusaoEnsinoMedio += "\n";
			}
			if (formacaoAcademicaVO.getEscolaridade().equals("EM")) {
				anoConclusaoEnsinoMedio += formacaoAcademicaVO.getAnoDataFim();
			}
		}
		
		String responsavelLegal = "";
		Iterator<FiliacaoVO> listaFiliacaoResponsavel = pessoa.getFiliacaoVOs().iterator();
		while (listaFiliacaoResponsavel.hasNext()) {
			FiliacaoVO filiacao = (FiliacaoVO) listaFiliacaoResponsavel.next();
			if (filiacao.getResponsavelLegal() || filiacao.getTipo().equals("PA") || filiacao.getTipo().equals("MA")) {
				if (responsavelLegal.equals("")) {
					responsavelLegal += filiacao.getNome();
				} else {
					responsavelLegal += ", " + filiacao.getNome();
				}
			}
		}

		String nomeEmpresa = "";
		String enderecoEmpresa = "";
		String complementoEmpresa = "";
		String cepEmpresa = "";
		String cidadeEmpresa = "";
		String estadoCidadeEmpresa = "";
		String telefoneComercialEmpresa = "";
		String bairroEmpresa = "";
		if (dc != null) {
			nomeEmpresa = dc.getNomeEmpresa();
			if (nomeEmpresa == null || nomeEmpresa.equals("0") || nomeEmpresa.equals("")) {
				nomeEmpresa = emBranco;
			}
			enderecoEmpresa = dc.getEnderecoEmpresa();
			if (enderecoEmpresa == null || enderecoEmpresa.equals("0") || enderecoEmpresa.equals("")) {
				enderecoEmpresa = emBranco;
			}
			complementoEmpresa = dc.getComplementoEmpresa();
			if (complementoEmpresa == null || complementoEmpresa.equals("0") || complementoEmpresa.equals("")) {
				complementoEmpresa = emBranco;
			}
			cepEmpresa = dc.getCepEmpresa();
			if (cepEmpresa == null || cepEmpresa.equals("0") || cepEmpresa.equals("")) {
				cepEmpresa = emBranco;
			}
			cidadeEmpresa = dc.getCidadeEmpresa().getNome();
			if (cidadeEmpresa == null || cidadeEmpresa.equals("0") || cidadeEmpresa.equals("")) {
				cidadeEmpresa = emBranco;
			}
			estadoCidadeEmpresa = dc.getCidadeEmpresa().getEstado().getSigla();
			if (estadoCidadeEmpresa == null || estadoCidadeEmpresa.equals("0") || estadoCidadeEmpresa.equals("")) {
				estadoCidadeEmpresa = emBranco;
			}
			telefoneComercialEmpresa = dc.getTelefoneComer();
			if (telefoneComercialEmpresa == null || telefoneComercialEmpresa.equals("0") || telefoneComercialEmpresa.equals("")) {
				telefoneComercialEmpresa = emBranco;
			}
			// String bairroEmpresa = pessoa.getBairroEmpresa();
			bairroEmpresa = dc.getSetorEmpresa();
			if (bairroEmpresa == null || bairroEmpresa.equals("0") || bairroEmpresa.equals("")) {
				bairroEmpresa = emBranco;
			}
		}

		String numeroRegistroDiploma = "";
		String folhaRegistroDiploma = "";
		String livroRegistroDiploma = "";
		String numeroProcessoDiploma = "";
		String dataPublicacaoDiploma = "";
		String numeroRegistroCertificado = "";
		String folhaRegistroCertificado = "";
		String livroRegistroCertificado = "";
		String dataPublicacaoCertificado = "";
		String viaRegistroCertificado = "";

		
		if (Uteis.isAtributoPreenchido(impressaoContratoVO.getControleLivroFolhaReciboVO().getControleLivroRegistroDiploma()) && impressaoContratoVO.getControleLivroFolhaReciboVO().getControleLivroRegistroDiploma().getTipoLivroRegistroDiplomaEnum().isTipoRegistroDiploma()) {
			numeroRegistroDiploma =  impressaoContratoVO.getExpedicaoDiplomaVO().getNumeroRegistroDiploma().toString();
			if (numeroRegistroDiploma == null || numeroRegistroDiploma.equals("0") || numeroRegistroDiploma.equals("")) {
				numeroRegistroDiploma = emBranco;
			}

			numeroProcessoDiploma = impressaoContratoVO.getExpedicaoDiplomaVO().getNumeroProcesso().toString();
			if (numeroProcessoDiploma == null || numeroProcessoDiploma.equals("0") || numeroProcessoDiploma.equals("")) {
				numeroProcessoDiploma = emBranco;
			}
			
			folhaRegistroDiploma = impressaoContratoVO.getControleLivroFolhaReciboVO().getFolhaReciboAtual().toString();
			if (folhaRegistroDiploma == null || folhaRegistroDiploma.equals("0") || folhaRegistroDiploma.equals("")) {
				folhaRegistroDiploma = emBranco;
			}

			livroRegistroDiploma = impressaoContratoVO.getControleLivroFolhaReciboVO().getControleLivroRegistroDiploma().getNrLivro().toString();
			if (livroRegistroDiploma == null || livroRegistroDiploma.equals("0") || livroRegistroDiploma.equals("")) {
				livroRegistroDiploma = emBranco;
			}

			dataPublicacaoDiploma = Uteis.getData(impressaoContratoVO.getControleLivroFolhaReciboVO().getDataPublicacao(), "dd/MM/yyyy");
			if (dataPublicacaoDiploma == null || dataPublicacaoDiploma.equals("")) {
				dataPublicacaoDiploma = emBranco;
			}
		} else if (Uteis.isAtributoPreenchido(impressaoContratoVO.getControleLivroFolhaReciboVO().getControleLivroRegistroDiploma()) && impressaoContratoVO.getControleLivroFolhaReciboVO().getControleLivroRegistroDiploma().getTipoLivroRegistroDiplomaEnum().isTipoRegistroCertificado()) {
			numeroRegistroCertificado = impressaoContratoVO.getControleLivroFolhaReciboVO().getNumeroRegistro().toString();
			if (numeroRegistroCertificado == null || numeroRegistroCertificado.equals("0") || numeroRegistroCertificado.equals("")) {
				numeroRegistroCertificado = emBranco;
			}
			viaRegistroCertificado = impressaoContratoVO.getControleLivroFolhaReciboVO().getVia();
			if (viaRegistroCertificado == null || viaRegistroCertificado.equals("0") || viaRegistroCertificado.equals("")) {
				viaRegistroCertificado = emBranco;
			}

			folhaRegistroCertificado = impressaoContratoVO.getControleLivroFolhaReciboVO().getFolhaReciboAtual().toString();
			if (folhaRegistroCertificado == null || folhaRegistroCertificado.equals("0") || folhaRegistroCertificado.equals("")) {
				folhaRegistroCertificado = emBranco;
			}

			livroRegistroCertificado = impressaoContratoVO.getControleLivroFolhaReciboVO().getControleLivroRegistroDiploma().getNrLivro().toString();
			if (livroRegistroCertificado == null || livroRegistroCertificado.equals("0") || livroRegistroCertificado.equals("")) {
				livroRegistroCertificado = emBranco;
			}

			dataPublicacaoCertificado = Uteis.getData(impressaoContratoVO.getControleLivroFolhaReciboVO().getDataPublicacao(), "dd/MM/yyyy");
			if (dataPublicacaoCertificado == null || dataPublicacaoCertificado.equals("")) {
				dataPublicacaoCertificado = emBranco;
			}
		}
		
		String semestreIngresso = impressaoContratoVO.getMatriculaVO().getSemestreIngresso();
		if (semestreIngresso == null || semestreIngresso.equals("0") || semestreIngresso.equals("")) {
			semestreIngresso = emBranco;
		}
		String anoIngresso = impressaoContratoVO.getMatriculaVO().getAnoIngresso();
		if (anoIngresso == null || anoIngresso.equals("0") || anoIngresso.equals("")) {
			anoIngresso = emBranco;
		}
		String ultimoSemestreCursado = impressaoContratoVO.getMatriculaPeriodoVO().getSemestre();
		if (ultimoSemestreCursado == null || ultimoSemestreCursado.equals("0") || ultimoSemestreCursado.equals("")) {
			ultimoSemestreCursado = emBranco;
		}
		String ultimoAnoCursado = impressaoContratoVO.getMatriculaPeriodoVO().getAno();
		if (ultimoAnoCursado == null || ultimoAnoCursado.equals("0") || ultimoAnoCursado.equals("")) {
			ultimoAnoCursado = emBranco;
		}
		String listaEnade = "";
		for (MatriculaEnadeVO matriculaEnadeVO : impressaoContratoVO.getMatriculaVO().getMatriculaEnadeVOs()) {
			if (!listaEnade.trim().isEmpty()) {
				listaEnade += "\n";
			}
			if (matriculaEnadeVO.getTextoEnade().getTipoTextoEnade().equals(TipoTextoEnadeEnum.REALIZACAO)) {
				listaEnade += " " + matriculaEnadeVO.getTextoEnade().getTexto() + " - " + Uteis.getData(matriculaEnadeVO.getDataEnade(), "dd/MM/yyyy");
			} else {
				listaEnade += " " + matriculaEnadeVO.getTextoEnade().getTexto();
			}
		}
		String coeficienteRendimentoPeriodoLetivo = String.valueOf(Uteis.arrendondarForcando2CadasDecimais(impressaoContratoVO.getCoeficienteRendimentoPeriodoLetivoAluno()));
		if (coeficienteRendimentoPeriodoLetivo == null || coeficienteRendimentoPeriodoLetivo.equals("0.0") || coeficienteRendimentoPeriodoLetivo.equals("")) {
			coeficienteRendimentoPeriodoLetivo = emBranco;
		}
		String coeficienteRendimentoGeral = String.valueOf(Uteis.arrendondarForcando2CadasDecimais(impressaoContratoVO.getCoeficienteRendimentoGeralAluno()));
		if (coeficienteRendimentoGeral == null || coeficienteRendimentoGeral.equals("0.0") || coeficienteRendimentoGeral.equals("")) {
			coeficienteRendimentoGeral = emBranco;
		}
		String notaMonografiaAlteracoesCadastrais = Uteis.getDoubleFormatado(matriculaVO.getNotaMonografia());
		if (notaMonografiaAlteracoesCadastrais == null || notaMonografiaAlteracoesCadastrais.equals("")) {
			notaMonografiaAlteracoesCadastrais = emBranco;
		}
		boolean possuiResponsavel = false;
		String nomeResponsavelFinanceiro = "";
		String cpfResponsavelFinanceiro = "";
		String rgResponsavelFinanceiro = "";
		String telefoneComerResponsavelFinanceiro = "";
		String telefoneResResponsavelFinanceiro = "";
		String telefoneRecadoResponsavelFinanceiro = "";
		String celularResponsavelFinanceiro = "";
		String emailResponsavelFinanceiro = "";
		String cepResponsavelFinanceiro = "";
		String enderecoResponsavelFinanceiro = "";
		String numeroEnderecoResponsavelFinanceiro = "";
		String complementoEnderecoResponsavelFinanceiro = "";
		String dataNascResponsavelFinanceiro = "";
		String orgaoEmissoResponsavelFinanceiro = "";
		String setorResponsavelFinanceiro = "";
		String cidadeResponsavelFinanceiro = "";
		String estadoResponsavelFinanceiro = "";
		String nacionalidadeResponsavelFinanceiro = "";
		String estadoCivilResponsavelFinanceiro = "";
		String grauParentescoResponsavelFinanceiro = "";
		Iterator listaFiliacao = pessoa.getFiliacaoVOs().iterator();
		while (listaFiliacao.hasNext()) {
			FiliacaoVO filiacao = (FiliacaoVO) listaFiliacao.next();
			if (filiacao.getResponsavelFinanceiro()) {
				possuiResponsavel = true;
				nomeResponsavelFinanceiro = filiacao.getPais().getNome();
				if (nomeResponsavelFinanceiro == null || nomeResponsavelFinanceiro.equals("0") || nomeResponsavelFinanceiro.equals("")) {
					nomeResponsavelFinanceiro = emBranco;
				}
				cpfResponsavelFinanceiro = filiacao.getPais().getCPF();
				if (cpfResponsavelFinanceiro == null || cpfResponsavelFinanceiro.equals("0") || cpfResponsavelFinanceiro.equals("")) {
					cpfResponsavelFinanceiro = emBranco;
				}
				rgResponsavelFinanceiro = filiacao.getPais().getRG();
				if (rgResponsavelFinanceiro == null || rgResponsavelFinanceiro.equals("0") || rgResponsavelFinanceiro.equals("")) {
					rgResponsavelFinanceiro = emBranco;
				}
				telefoneComerResponsavelFinanceiro = filiacao.getPais().getTelefoneComer();
				if (telefoneComerResponsavelFinanceiro == null || telefoneComerResponsavelFinanceiro.equals("0") || telefoneComerResponsavelFinanceiro.equals("")) {
					telefoneComerResponsavelFinanceiro = emBranco;
				}
				telefoneResResponsavelFinanceiro = filiacao.getPais().getTelefoneRes();
				if (telefoneResResponsavelFinanceiro == null || telefoneResResponsavelFinanceiro.equals("0") || telefoneResResponsavelFinanceiro.equals("")) {
					telefoneResResponsavelFinanceiro = emBranco;
				}
				telefoneRecadoResponsavelFinanceiro = filiacao.getPais().getTelefoneRecado();
				if (telefoneRecadoResponsavelFinanceiro == null || telefoneRecadoResponsavelFinanceiro.equals("0") || telefoneRecadoResponsavelFinanceiro.equals("")) {
					telefoneRecadoResponsavelFinanceiro = emBranco;
				}
				celularResponsavelFinanceiro = filiacao.getPais().getCelular();
				if (celularResponsavelFinanceiro == null || celularResponsavelFinanceiro.equals("0") || celularResponsavelFinanceiro.equals("")) {
					celularResponsavelFinanceiro = emBranco;
				}
				emailResponsavelFinanceiro = filiacao.getPais().getEmail();
				if (emailResponsavelFinanceiro == null || emailResponsavelFinanceiro.equals("0") || emailResponsavelFinanceiro.equals("")) {
					emailResponsavelFinanceiro = emBranco;
				}
				cepResponsavelFinanceiro = filiacao.getPais().getCEP();
				if (cepResponsavelFinanceiro == null || cepResponsavelFinanceiro.equals("0") || cepResponsavelFinanceiro.equals("")) {
					cepResponsavelFinanceiro = emBranco;
				}
				enderecoResponsavelFinanceiro = filiacao.getPais().getEndereco();
				if (enderecoResponsavelFinanceiro == null || enderecoResponsavelFinanceiro.equals("0") || enderecoResponsavelFinanceiro.equals("")) {
					enderecoResponsavelFinanceiro = emBranco;
				}
				numeroEnderecoResponsavelFinanceiro = filiacao.getPais().getNumero();
				if (numeroEnderecoResponsavelFinanceiro == null || numeroEnderecoResponsavelFinanceiro.equals("0") || numeroEnderecoResponsavelFinanceiro.equals("")) {
					numeroEnderecoResponsavelFinanceiro = emBranco;
				}
				complementoEnderecoResponsavelFinanceiro = filiacao.getPais().getComplemento();
				if (complementoEnderecoResponsavelFinanceiro == null || complementoEnderecoResponsavelFinanceiro.equals("0") || complementoEnderecoResponsavelFinanceiro.equals("")) {
					complementoEnderecoResponsavelFinanceiro = emBranco;
				}
				orgaoEmissoResponsavelFinanceiro = filiacao.getPais().getOrgaoEmissor();
				if (orgaoEmissoResponsavelFinanceiro == null || orgaoEmissoResponsavelFinanceiro.equals("0") || orgaoEmissoResponsavelFinanceiro.equals("")) {
					orgaoEmissoResponsavelFinanceiro = emBranco;
				}
				setorResponsavelFinanceiro = filiacao.getPais().getSetor();
				if (setorResponsavelFinanceiro == null || setorResponsavelFinanceiro.equals("0") || setorResponsavelFinanceiro.equals("")) {
					setorResponsavelFinanceiro = emBranco;
				}
				cidadeResponsavelFinanceiro = filiacao.getPais().getCidade().getNome();
				if (cidadeResponsavelFinanceiro == null || cidadeResponsavelFinanceiro.equals("0") || cidadeResponsavelFinanceiro.equals("")) {
					cidadeResponsavelFinanceiro = emBranco;
				}
				estadoResponsavelFinanceiro = filiacao.getPais().getCidade().getEstado().getSigla();
				if (estadoResponsavelFinanceiro == null || estadoResponsavelFinanceiro.equals("0") || estadoResponsavelFinanceiro.equals("")) {
					estadoResponsavelFinanceiro = emBranco;
				}
				dataNascResponsavelFinanceiro = filiacao.getPais().getDataNasc_Apresentar();
				if (dataNascResponsavelFinanceiro == null || dataNascResponsavelFinanceiro.equals("0") || dataNascResponsavelFinanceiro.equals("")) {
					dataNascResponsavelFinanceiro = emBranco;
				}
				nacionalidadeResponsavelFinanceiro = filiacao.getPais().getNacionalidade().getNome();
				if (nacionalidadeResponsavelFinanceiro == null || nacionalidadeResponsavelFinanceiro.equals("0") || nacionalidadeResponsavelFinanceiro.equals("")) {
					nacionalidadeResponsavelFinanceiro = emBranco;
				}
				estadoCivilResponsavelFinanceiro = filiacao.getPais().getEstadoCivil_Apresentar();
				if (estadoCivilResponsavelFinanceiro == null || estadoCivilResponsavelFinanceiro.equals("0") || estadoCivilResponsavelFinanceiro.equals("")) {
					estadoCivilResponsavelFinanceiro = emBranco;
				}
				grauParentescoResponsavelFinanceiro = filiacao.getPais().getGrauParentesco();
				if (grauParentescoResponsavelFinanceiro == null || grauParentescoResponsavelFinanceiro.equals("0") || grauParentescoResponsavelFinanceiro.equals("")) {
					grauParentescoResponsavelFinanceiro = emBranco;
				}
			}
		}
		if (!possuiResponsavel) {
			nomeResponsavelFinanceiro = pessoa.getNome();
			cpfResponsavelFinanceiro = pessoa.getCPF();
			rgResponsavelFinanceiro = pessoa.getRG();
			telefoneComerResponsavelFinanceiro = pessoa.getTelefoneComer();
			telefoneResResponsavelFinanceiro = pessoa.getTelefoneRes();
			telefoneRecadoResponsavelFinanceiro = pessoa.getTelefoneRecado();
			celularResponsavelFinanceiro = pessoa.getCelular();
			emailResponsavelFinanceiro = pessoa.getEmail();
			cepResponsavelFinanceiro = pessoa.getCEP();
			enderecoResponsavelFinanceiro = pessoa.getEndereco();
			numeroEnderecoResponsavelFinanceiro = pessoa.getNumero();
			complementoEnderecoResponsavelFinanceiro = pessoa.getComplemento();
			dataNascResponsavelFinanceiro = pessoa.getDataNasc_Apresentar();
			orgaoEmissoResponsavelFinanceiro = pessoa.getOrgaoEmissor();
			setorResponsavelFinanceiro = pessoa.getSetor();
			cidadeResponsavelFinanceiro = pessoa.getCidade().getNome();
			estadoResponsavelFinanceiro = pessoa.getCidade().getEstado().getSigla();
			nacionalidadeResponsavelFinanceiro = pessoa.getNacionalidade().getNome();
			estadoCivilResponsavelFinanceiro = pessoa.getEstadoCivil_Apresentar();
			grauParentescoResponsavelFinanceiro = pessoa.getGrauParentesco();
		}
		String nomeResponsavelFinanceiro2 = "";
		String cpfResponsavelFinanceiro2 = "";
		String rgResponsavelFinanceiro2 = "";
		String telefoneComerResponsavelFinanceiro2 = "";
		String telefoneResResponsavelFinanceiro2 = "";
		String telefoneRecadoResponsavelFinanceiro2 = "";
		String celularResponsavelFinanceiro2 = "";
		String emailResponsavelFinanceiro2 = "";
		String enderecoResponsavelFinanceiro2 = "";
		String numeroEnderecoResponsavelFinanceiro2 = "";
		String complementoEnderecoResponsavelFinanceiro2 = "";
		String cepResponsavelFinanceiro2 = "";
		String dataNascResponsavelFinanceiro2 = "";
		String orgaoEmissoResponsavelFinanceiro2 = "";
		String setorResponsavelFinanceiro2 = "";
		String cidadeResponsavelFinanceiro2 = "";
		String estadoResponsavelFinanceiro2 = "";
		String nacionalidadeResponsavelFinanceiro2 = "";
		String estadoCivilResponsavelFinanceiro2 = "";
		String grauParentescoResponsavelFinanceiro2 = "";
		Iterator listaFiliacao2 = pessoa.getFiliacaoVOs().iterator();
		while (listaFiliacao2.hasNext()) {
			FiliacaoVO filiacao = (FiliacaoVO) listaFiliacao2.next();
			if (filiacao.getResponsavelFinanceiro()) {
				nomeResponsavelFinanceiro2 = filiacao.getPais().getNome();
				if (nomeResponsavelFinanceiro2 == null || nomeResponsavelFinanceiro2.equals("0") || nomeResponsavelFinanceiro2.equals("")) {
					nomeResponsavelFinanceiro2 = emBranco;
				}
				cpfResponsavelFinanceiro2 = filiacao.getPais().getCPF();
				if (cpfResponsavelFinanceiro2 == null || cpfResponsavelFinanceiro2.equals("0") || cpfResponsavelFinanceiro2.equals("")) {
					cpfResponsavelFinanceiro2 = emBranco;
				}
				rgResponsavelFinanceiro2 = filiacao.getPais().getRG();
				if (rgResponsavelFinanceiro2 == null || rgResponsavelFinanceiro2.equals("0") || rgResponsavelFinanceiro2.equals("")) {
					rgResponsavelFinanceiro2 = emBranco;
				}
				telefoneComerResponsavelFinanceiro2 = filiacao.getPais().getTelefoneComer();
				if (telefoneComerResponsavelFinanceiro2 == null || telefoneComerResponsavelFinanceiro2.equals("0") || telefoneComerResponsavelFinanceiro2.equals("")) {
					telefoneComerResponsavelFinanceiro2 = emBranco;
				}
				telefoneResResponsavelFinanceiro2 = filiacao.getPais().getTelefoneRes();
				if (telefoneResResponsavelFinanceiro2 == null || telefoneResResponsavelFinanceiro2.equals("0") || telefoneResResponsavelFinanceiro2.equals("")) {
					telefoneResResponsavelFinanceiro2 = emBranco;
				}
				telefoneRecadoResponsavelFinanceiro2 = filiacao.getPais().getTelefoneRecado();
				if (telefoneRecadoResponsavelFinanceiro2 == null || telefoneRecadoResponsavelFinanceiro2.equals("0") || telefoneRecadoResponsavelFinanceiro2.equals("")) {
					telefoneRecadoResponsavelFinanceiro2 = emBranco;
				}
				celularResponsavelFinanceiro2 = filiacao.getPais().getCelular();
				if (celularResponsavelFinanceiro2 == null || celularResponsavelFinanceiro2.equals("0") || celularResponsavelFinanceiro2.equals("")) {
					celularResponsavelFinanceiro2 = emBranco;
				}
				emailResponsavelFinanceiro2 = filiacao.getPais().getEmail();
				if (emailResponsavelFinanceiro2 == null || emailResponsavelFinanceiro2.equals("0") || emailResponsavelFinanceiro2.equals("")) {
					emailResponsavelFinanceiro2 = emBranco;
				}
				enderecoResponsavelFinanceiro2 = filiacao.getPais().getEndereco();
				if (enderecoResponsavelFinanceiro2 == null || enderecoResponsavelFinanceiro2.equals("0") || enderecoResponsavelFinanceiro2.equals("")) {
					enderecoResponsavelFinanceiro2 = emBranco;
				}
				numeroEnderecoResponsavelFinanceiro2 = filiacao.getPais().getNumero();
				if (numeroEnderecoResponsavelFinanceiro2 == null || numeroEnderecoResponsavelFinanceiro2.equals("0") || numeroEnderecoResponsavelFinanceiro2.equals("")) {
					numeroEnderecoResponsavelFinanceiro2 = emBranco;
				}
				complementoEnderecoResponsavelFinanceiro2 = filiacao.getPais().getComplemento();
				if (complementoEnderecoResponsavelFinanceiro2 == null || complementoEnderecoResponsavelFinanceiro2.equals("0") || complementoEnderecoResponsavelFinanceiro2.equals("")) {
					complementoEnderecoResponsavelFinanceiro2 = emBranco;
				}
				cepResponsavelFinanceiro2 = filiacao.getPais().getCEP();
				if (cepResponsavelFinanceiro2 == null || cepResponsavelFinanceiro2.equals("0") || cepResponsavelFinanceiro2.equals("")) {
					cepResponsavelFinanceiro2 = emBranco;
				}
				orgaoEmissoResponsavelFinanceiro2 = filiacao.getPais().getOrgaoEmissor();
				if (orgaoEmissoResponsavelFinanceiro2 == null || orgaoEmissoResponsavelFinanceiro2.equals("0") || orgaoEmissoResponsavelFinanceiro2.equals("")) {
					orgaoEmissoResponsavelFinanceiro2 = emBranco;
				}
				setorResponsavelFinanceiro2 = filiacao.getPais().getSetor();
				if (setorResponsavelFinanceiro2 == null || setorResponsavelFinanceiro2.equals("0") || setorResponsavelFinanceiro2.equals("")) {
					setorResponsavelFinanceiro2 = emBranco;
				}
				cidadeResponsavelFinanceiro2 = filiacao.getPais().getCidade().getNome();
				if (cidadeResponsavelFinanceiro2 == null || cidadeResponsavelFinanceiro2.equals("0") || cidadeResponsavelFinanceiro2.equals("")) {
					cidadeResponsavelFinanceiro2 = emBranco;
				}
				estadoResponsavelFinanceiro2 = filiacao.getPais().getCidade().getEstado().getSigla();
				if (estadoResponsavelFinanceiro2 == null || estadoResponsavelFinanceiro2.equals("0") || estadoResponsavelFinanceiro2.equals("")) {
					estadoResponsavelFinanceiro2 = emBranco;
				}
				dataNascResponsavelFinanceiro2 = filiacao.getPais().getDataNasc_Apresentar();
				if (dataNascResponsavelFinanceiro2 == null || dataNascResponsavelFinanceiro2.equals("0") || dataNascResponsavelFinanceiro2.equals("")) {
					dataNascResponsavelFinanceiro2 = emBranco;
				}
				nacionalidadeResponsavelFinanceiro2 = filiacao.getPais().getNacionalidade().getNome();
				if (nacionalidadeResponsavelFinanceiro2 == null || nacionalidadeResponsavelFinanceiro2.equals("0") || nacionalidadeResponsavelFinanceiro2.equals("")) {
					nacionalidadeResponsavelFinanceiro2 = emBranco;
				}
				estadoCivilResponsavelFinanceiro2 = filiacao.getPais().getEstadoCivil_Apresentar();
				if (estadoCivilResponsavelFinanceiro2 == null || estadoCivilResponsavelFinanceiro2.equals("0") || estadoCivilResponsavelFinanceiro2.equals("")) {
					estadoCivilResponsavelFinanceiro2 = emBranco;
				}
				grauParentescoResponsavelFinanceiro2 = filiacao.getPais().getGrauParentesco();
				if (grauParentescoResponsavelFinanceiro2 == null || grauParentescoResponsavelFinanceiro2.equals("0") || grauParentescoResponsavelFinanceiro2.equals("")) {
					grauParentescoResponsavelFinanceiro2 = emBranco;
				}
			}
		}
		// Sempre chegará com a lista preenchida com apenas 1 item. Haja vista
		// que no contrato não apresentará a lista, sendo assim
		// é feito uma logica para que quando mande realizar a impressão os
		// sitema deixe apenas um Obejto na lista.
		String cursoFormaca = "";
		String escolaridadeFormaca = "";
		String instituicaoFormaca = "";
		String cidadeFormaca = "";
		String estadoFormaca = "";
		String areaConhecimentoFormaca = "";
		String situacaoFormaca = "";
		String dataInicioFormaca = "";
		String dataFinalFormaca = "";
		String anoDataFinalFormaca = "";
		String justificativa = impressaoContratoVO.getTrancamentoVO().getJustificativa();
		String observacoes = "";
		Iterator listaFormacao = pessoa.getFormacaoAcademicaVOs().iterator();
		while (listaFormacao.hasNext()) {
			FormacaoAcademicaVO formacao = (FormacaoAcademicaVO) listaFormacao.next();
			cursoFormaca = formacao.getCurso();
			if (cursoFormaca == null || cursoFormaca.equals("0") || cursoFormaca.equals("")) {
				cursoFormaca = emBranco;
			}
			escolaridadeFormaca = formacao.getEscolaridade_Apresentar();
			if (escolaridadeFormaca == null || escolaridadeFormaca.equals("0") || escolaridadeFormaca.equals("")) {
				escolaridadeFormaca = emBranco;
			}
			instituicaoFormaca = formacao.getInstituicao();
			if (instituicaoFormaca == null || instituicaoFormaca.equals("0") || instituicaoFormaca.equals("")) {
				instituicaoFormaca = emBranco;
			}
			cidadeFormaca = formacao.getCidade().getNome();
			if (cidadeFormaca == null || cidadeFormaca.equals("0") || cidadeFormaca.equals("")) {
				cidadeFormaca = emBranco;
			}
			estadoFormaca = formacao.getCidade().getEstado().getSigla();
			if (estadoFormaca == null || estadoFormaca.equals("0") || estadoFormaca.equals("")) {
				estadoFormaca = emBranco;
			}
			areaConhecimentoFormaca = formacao.getAreaConhecimento().getNome();
			if (areaConhecimentoFormaca == null || areaConhecimentoFormaca.equals("0") || areaConhecimentoFormaca.equals("")) {
				areaConhecimentoFormaca = emBranco;
			}
			situacaoFormaca = formacao.getSituacao_Apresentar();
			if (situacaoFormaca == null || situacaoFormaca.equals("0") || situacaoFormaca.equals("")) {
				situacaoFormaca = emBranco;
			}
			dataInicioFormaca = formacao.getDataInicio_ApresentarAno2Digitos();
			if (dataInicioFormaca == null || dataInicioFormaca.equals("0") || dataInicioFormaca.equals("")) {
				dataInicioFormaca = emBranco;
			}
			dataFinalFormaca = formacao.getDataFim_ApresentarAno2Digitos();
			anoDataFinalFormaca = formacao.getAnoDataFim_Apresentar();
			if (dataFinalFormaca == null || dataFinalFormaca.equals("0") || dataFinalFormaca.equals("")) {
				dataFinalFormaca = emBranco;
			}
			if (anoDataFinalFormaca == null || anoDataFinalFormaca.equals("")) {
				anoDataFinalFormaca = emBranco;
			}
		}
		
		StringBuilder listaDemostrativoDebitoAluno = new StringBuilder();
		Double totalValorCorrigidoDebito = 0.0;
		if (!impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getSub2().isEmpty()) {
			List listaDemostrativoDebito = impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getSub2().get(0).getDebito();
			Double totalValorPrincipalDebito = 0.0;
			Double totalDescontoDebito = 0.0;
			Double totalAcrescimosDebito = 0.0;
			Double totalJurosDebito = 0.0;
			Double totalMultaDebito = 0.0;
			boolean primeiraVezDemostrativoDebito = true;
			Double totalDescontoDebitoConvenio = 0.0;
			if (!listaDemostrativoDebito.isEmpty()) {
				Iterator i = listaDemostrativoDebito.iterator();
				while (i.hasNext()) {
					TermoReconhecimentoDividaDebitoRelVO debitoVO = (TermoReconhecimentoDividaDebitoRelVO) i.next();
					totalValorPrincipalDebito += debitoVO.getValor();
					totalDescontoDebito += debitoVO.getValorDesconto();
					totalAcrescimosDebito += debitoVO.getAcrescimo();
					totalJurosDebito += debitoVO.getJuro();
					totalMultaDebito += debitoVO.getMulta();
					totalValorCorrigidoDebito += debitoVO.getValorFinal();
					totalDescontoDebitoConvenio += debitoVO.getValorDescontoConvenio();
					if (primeiraVezDemostrativoDebito) {
						listaDemostrativoDebitoAluno
								.append("<table width=\"720\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"border:1px #000000 solid;font-family:Arial, Helvetica, sans-serif;font-size:12px;\"><tbody><tr><td height=\"16\" colspan=\"2\" style=\"border-right:1px #000000 solid;\"><strong>Refer&ecirc;ncia</strong></td><td width=\"84\" style=\"border-right:1px #000000 solid;padding-right:4px;\" rowspan=\"2\" align=\"right\"><strong>Valor <br>Principal</strong></td><td width=\"110\" style=\"border-right:1px #000000 solid;\" rowspan=\"2\" align=\"center\"><strong>Vencimento</strong></td><td width=\"128\" style=\"border-right:1px #000000 solid;padding-right:4px;\" rowspan=\"2\" align=\"center\"><strong>Juros</strong></td><td width=\"108\" style=\"border-right:1px #000000 solid;\" rowspan=\"2\" align=\"center\"><strong>Multa</strong></td>   <td width=\"65\" style=\"border-right:1px #000000 solid;padding-right:4px;border-right:1px #000000 solid;\" rowspan=\"2\" align=\"center\"><strong>Acr&eacute;scimos</strong></td><td width=\"69\" style=\"padding-right:4px;border-right:1px #000000 solid;\" rowspan=\"2\" align=\"right\"><strong>Desconto</strong></td><td width=\"69\" style=\"padding-right:4px;border-right:1px #000000 solid;\" rowspan=\"2\" align=\"right\"><strong>Conv\u00eanio</strong></td><td width=\"69\" style=\"padding-right:4px;border-right:1px #000000 solid;\" rowspan=\"2\" align=\"right\"><strong>Valor<br>Corrigido</strong></td><td width=\"69\" style=\"padding-right:4px;\" rowspan=\"2\" align=\"right\"><strong>Nr. Documento</strong></td></tr><tr><td width=\"54\" height=\"20\" style=\"border-top:1px #000000 solid;border-right:1px #000000 solid;\"><strong>Parcela</strong></td><td width=\"180\" style=\"border-top:1px #000000 solid;border-right:1px #000000 solid;\"><strong>Centro de Receita</strong></td></tr>");
					}
					listaDemostrativoDebitoAluno.append("<tr><td height=\"20\" align=\"center\" style=\"border-top:1px #000000 solid;border-right:1px #000000 solid;\">").append(debitoVO.getParcela()).append("</td><td align=\"center\" style=\"width:350px;border-top:1px #000000 solid;border-right:1px #000000 solid;\">").append(debitoVO.getCentroReceita()).append("</td><td style=\"border-top:1px #000000 solid;border-right:1px #000000 solid;padding-right:4px;\" align=\"right\">").append(Uteis.arrendondarForcando2CadasDecimaisStr(debitoVO.getValor())).append("</td><td style=\"border-top:1px #000000 solid;border-right:1px #000000 solid;\" align=\"center\">").append(debitoVO.getDataVencimento()).append("</td><td style=\"border-top:1px #000000 solid;border-right:1px #000000 solid;padding-right:4px;\" align=\"right\">").append(Uteis.arrendondarForcando2CadasDecimaisStr(debitoVO.getJuro())).append("</td><td style=\"border-top:1px #000000 solid;border-right:1px #000000 solid;\" align=\"center\">")
							.append(Uteis.arrendondarForcando2CadasDecimaisStr(debitoVO.getMulta())).append("</td><td style=\"border-top:1px #000000 solid;border-right:1px #000000 solid;padding-right:4px;\" align=\"right\">").append(Uteis.arrendondarForcando2CadasDecimaisStr(debitoVO.getAcrescimo())).append("</td><td style=\"border-top:1px #000000 solid;padding-right:4px;border-top:1px #000000 solid;border-right:1px #000000 solid;\" align=\"right\">").append(Uteis.arrendondarForcando2CadasDecimaisStr(debitoVO.getValorDesconto() - debitoVO.getValorDescontoConvenio())).append("</td><td style=\"border-top:1px #000000 solid;padding-right:4px;border-top:1px #000000 solid;border-right:1px #000000 solid;\" align=\"right\">").append(Uteis.arrendondarForcando2CadasDecimaisStr(debitoVO.getValorDescontoConvenio())).append("</td><td style=\"border-top:1px #000000 solid;padding-right:4px;border-top:1px #000000 solid;border-right:1px #000000 solid;\" align=\"right\">")
							.append(Uteis.arrendondarForcando2CadasDecimaisStr(debitoVO.getValorFinal())).append("</td><td style=\"border-top:1px #000000 solid;padding-right:4px;\" align=\"right\">").append(debitoVO.getNrDocumento()).append("</td></tr>");
					primeiraVezDemostrativoDebito = false;
				}
				listaDemostrativoDebitoAluno.append("<tr><td height=\"20\" colspan=\"2\" align=\"right\" style=\"border-top:1px #000000 solid;border-right:1px #000000 solid;padding-right:4px;\"><strong>Totais</strong></td><td style=\"border-top:1px #000000 solid;border-right:1px #000000 solid;padding-right:4px;\" align=\"right\">").append(Uteis.arrendondarForcando2CadasDecimaisStr(totalValorPrincipalDebito)).append("</td><td style=\"border-top:1px #000000 solid;border-right:1px #000000 solid;\" align=\"center\">&nbsp;</td><td style=\"border-top:1px #000000 solid;border-right:1px #000000 solid;padding-right:4px;\" align=\"right\">").append(Uteis.arrendondarForcando2CadasDecimaisStr(totalJurosDebito)).append("</td><td style=\"border-top:1px #000000 solid;border-right:1px #000000 solid;\" align=\"center\">").append(Uteis.arrendondarForcando2CadasDecimaisStr(totalMultaDebito)).append("</td><td style=\"border-top:1px #000000 solid;border-right:1px #000000 solid;padding-right:4px;\" align=\"right\">")
						.append(Uteis.arrendondarForcando2CadasDecimaisStr(totalAcrescimosDebito)).append("</td><td style=\"border-top:1px #000000 solid;padding-right:4px;border-top:1px #000000 solid;border-right:1px #000000 solid;\" align=\"right\">").append(Uteis.arrendondarForcando2CadasDecimaisStr(totalDescontoDebito - totalDescontoDebitoConvenio)).append("</td><td style=\"border-top:1px #000000 solid;padding-right:4px;border-top:1px #000000 solid;border-right:1px #000000 solid;\" align=\"right\">").append(Uteis.arrendondarForcando2CadasDecimaisStr(totalDescontoDebitoConvenio)).append("</td><td style=\"border-top:1px #000000 solid;padding-right:4px;border-top:1px #000000 solid;border-right:1px #000000 solid;\" align=\"right\">").append(Uteis.arrendondarForcando2CadasDecimaisStr(totalValorCorrigidoDebito)).append("</td><td style=\"border-top:1px #000000 solid;padding-right:4px;\" align=\"right\">&nbsp;</td></tr></tbody>");
				listaDemostrativoDebitoAluno.append("</table>");
			}
		}
		
		Double valorEntradaDebitoQuandoNaoExistirEntradaNegociacao = 0.0;
		Double totalValorPrincipalDebitoNegociado = 0.0;
		StringBuilder listaDemostrativoDebitoNegociadoAluno = new StringBuilder();
		if (!impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getSub3().isEmpty()) {
			List listaDemostrativoDebitoNegociado = impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getSub3().get(0).getParcelasNovaContaReceber();
			boolean primeiraVezDemostrativoDebitoNegociado = true;
			if (!listaDemostrativoDebitoNegociado.isEmpty()) {
				Iterator i = listaDemostrativoDebitoNegociado.iterator();
				while (i.hasNext()) {
					TermoReconhecimentoDividaParcelasRelVO dividaParcelaVO = (TermoReconhecimentoDividaParcelasRelVO) i.next();
					String valor = "";
					if (dividaParcelaVO.getParcela().contains("/")) {
						valor = dividaParcelaVO.getParcela().substring(0, dividaParcelaVO.getParcela().indexOf("/"));
					} else if (dividaParcelaVO.getParcela().toUpperCase().contains("N")) {
						valor = dividaParcelaVO.getParcela().toUpperCase().substring(0, dividaParcelaVO.getParcela().indexOf("N"));
					}
					if (valor.equals("1")) {
						valorEntradaDebitoQuandoNaoExistirEntradaNegociacao = dividaParcelaVO.getValor();
					}
					totalValorPrincipalDebitoNegociado += dividaParcelaVO.getValor();
					if (primeiraVezDemostrativoDebitoNegociado) {
						listaDemostrativoDebitoNegociadoAluno.append("<table width=\"720\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"border:1px #000000 solid;font-family:Arial, Helvetica, sans-serif;font-size:12px;\"><caption><b>Forma de Pagamento</b></caption><tbody><tr><td width=\"66\" height=\"38\" align=\"center\"><strong>Parcela</strong></td><td width=\"64\"><strong>Centro de Receita</strong></td><td width=\"67\" style=\"padding-right:4px;\" align=\"right\"><strong>Valor</strong></td><td width=\"58\" style=\"padding-right:4px;\" align=\"center\"><strong>Vencimento</strong></td><td width=\"78\" style=\"padding-right:4px;\" align=\"center\"><strong>Nr. Documento</strong></td></tr>");
					}
					listaDemostrativoDebitoNegociadoAluno.append("<tr><td height=\"22\" align=\"center\" style=\"border-top:1px #000000 solid;\">").append(dividaParcelaVO.getParcela()).append("</td><td style=\"border-top:1px #000000 solid;\">").append(dividaParcelaVO.getCentroReceita()).append("</td><td style=\"border-top:1px #000000 solid;padding-right:4px;\" align=\"right\">").append(Uteis.arrendondarForcando2CadasDecimaisStr(dividaParcelaVO.getValor())).append("</td><td style=\"border-top:1px #000000 solid;padding-right:4px;\" align=\"center\">").append(dividaParcelaVO.getDataVencimento()).append("</td><td style=\"border-top:1px #000000 solid;padding-right:4px;\" align=\"center\">").append(dividaParcelaVO.getNrDocumento()).append("</td></tr>");
					primeiraVezDemostrativoDebitoNegociado = false;
				}
				listaDemostrativoDebitoNegociadoAluno.append("<tr><td height=\"21\" colspan=\"2\" align=\"center\" style=\"border-top:1px #000000 solid;\"><strong>Totais</strong></td><td style=\"border-top:1px #000000 solid;padding-right:4px;\" align=\"right\">").append(Uteis.arrendondarForcando2CadasDecimaisStr(totalValorPrincipalDebitoNegociado)).append("</td><td style=\"border-top:1px #000000 solid;padding-right:4px;\" align=\"center\">&nbsp;</td><td style=\"border-top:1px #000000 solid;padding-right:4px;\" align=\"center\">&nbsp;</td></tr></tbody>");
				listaDemostrativoDebitoNegociadoAluno.append("</table>");
			}
		}
		
		String valorTotalDebitoInicial= "";
		String valorTotalDebitoFinal= "";
		String percentualJuroDebito= "";
		String valorJuroDebito = "";
		String acrescimoDebito = "";
		String descontoDebito = "";
		String valorEntradaDebito = "";
		String justificativaDebito = "";
		if(Uteis.isAtributoPreenchido(impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getValor())){
			valorTotalDebitoInicial = Uteis.getDoubleFormatado(impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getValor());	
		}
		if(Uteis.isAtributoPreenchido(impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getJuro())){
			percentualJuroDebito = Uteis.getDoubleFormatado(impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getJuro());	
		}
		if(Uteis.isAtributoPreenchido(impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getJuroReal())){
			valorJuroDebito = Uteis.getDoubleFormatado(impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getJuroReal());	
		}
		
		if(Uteis.isAtributoPreenchido(impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getAcrescimo())){
			acrescimoDebito = Uteis.getDoubleFormatado(impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getAcrescimo());	
		}
		if(Uteis.isAtributoPreenchido(impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getDesconto())){
			descontoDebito = Uteis.getDoubleFormatado(impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getDesconto());	
		}
		if(Uteis.isAtributoPreenchido(impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getEntrada())){
			valorEntradaDebito = Uteis.getDoubleFormatado(impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getEntrada());	
		} else {
			valorEntradaDebito = "0,00";	
		}		
		if(Uteis.isAtributoPreenchido(impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getValorFinal())){
			valorTotalDebitoFinal = Uteis.getDoubleFormatado(impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getValorFinal());	
		}
		if(Uteis.isAtributoPreenchido(impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getJustificativa())){
			justificativaDebito = impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getJustificativa();	
		}
		Double valorParceladoDebitoDouble = impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getValorFinal() - impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getEntrada();
		String valorParcelaDebitoString = Uteis.getDoubleFormatado(valorParceladoDebitoDouble);
		if (valorParcelaDebitoString == null || valorParcelaDebitoString.equals("0") || valorParcelaDebitoString.equals("")) {
			valorParcelaDebitoString = emBranco;
		}
		
		String valorTotalDebitoAluno = "";
		String valorTotalDebitoExtensoAluno = "";
		String numeroContratoDemostrativoDebitoAluno = "";
		if (!impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getSub2().isEmpty()) {
			valorTotalDebitoAluno = Uteis.getDoubleFormatado(totalValorCorrigidoDebito);
			if (valorTotalDebitoAluno == null || valorTotalDebitoAluno.equals("0.0") || valorTotalDebitoAluno.equals("")) {
				valorTotalDebitoAluno = emBranco;
			}
			Extenso ext = new Extenso();
			ext.setNumber(totalValorCorrigidoDebito);
			String valorTotalDebitoExtensoAlunoSemTratamento = ext.toString();
			if (!valorTotalDebitoExtensoAlunoSemTratamento.isEmpty() && valorTotalDebitoExtensoAlunoSemTratamento.length() > 1) {
				valorTotalDebitoExtensoAluno = valorTotalDebitoExtensoAlunoSemTratamento.substring(0, 1).toUpperCase() + valorTotalDebitoExtensoAlunoSemTratamento.substring(1);
			}
			if (valorTotalDebitoExtensoAluno == null || valorTotalDebitoExtensoAluno.equals("")) {
				valorTotalDebitoExtensoAluno = emBranco;
			}
			numeroContratoDemostrativoDebitoAluno = impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getRenegociacaoContaReceber().toString();
			if (numeroContratoDemostrativoDebitoAluno == null || numeroContratoDemostrativoDebitoAluno.equals("0") || numeroContratoDemostrativoDebitoAluno.equals("")) {
				numeroContratoDemostrativoDebitoAluno = emBranco;
			}
		}
		List<TermoReconhecimentoDividaParcelasRelVO> listaContaReceberNovas = new ArrayList<TermoReconhecimentoDividaParcelasRelVO>(0);
		Integer quantParcNegociacao = 0;
		String diaPrimeiroVencimento = "";
		String diaSegundoVencimento = "";
		if (!impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getSub3().isEmpty()) {
			listaContaReceberNovas = impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getSub3().get(0).getParcelasNovaContaReceber();
			quantParcNegociacao = impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getSub3().get(0).getParcelasNovaContaReceber().size();
			if (!listaContaReceberNovas.isEmpty()) {
				diaPrimeiroVencimento = listaContaReceberNovas.get(0).getDataVencimento();
			}
			if (!listaContaReceberNovas.isEmpty() && listaContaReceberNovas.size() > 1) {
				diaSegundoVencimento = listaContaReceberNovas.get(1).getDataVencimento();
			}
		}
		String valorParcelaNegociacao = ""; 
		Double valorParcelaNegociacaoDouble = 0.0;
		if (!listaContaReceberNovas.isEmpty()) {
			if (listaContaReceberNovas.size() == 1) {
				valorParcelaNegociacaoDouble = listaContaReceberNovas.get(0).getValor();
			} else if (listaContaReceberNovas.size() > 1){
				valorParcelaNegociacaoDouble = listaContaReceberNovas.get(1).getValor();
			}
			valorParcelaNegociacao = Uteis.getDoubleFormatado(valorParcelaNegociacaoDouble);
		}
		listaContaReceberNovas = null;
		if (valorParcelaNegociacao == null || valorParcelaNegociacao.equals("0") || valorParcelaNegociacao.equals("")) {
			valorParcelaNegociacao = emBranco;
		}
		Extenso ext = new Extenso();
		ext.setNumber(valorParcelaNegociacaoDouble);
		String valorParcelaNegociacaoDoubleSemTratamento = ext.toString();
		String valorParcelaNegociacaoExtenso = "";
		if (!valorParcelaNegociacaoDoubleSemTratamento.isEmpty() && valorParcelaNegociacaoDoubleSemTratamento.length() > 1) {
			valorParcelaNegociacaoExtenso = valorParcelaNegociacaoDoubleSemTratamento.substring(0, 1).toUpperCase() + valorParcelaNegociacaoDoubleSemTratamento.substring(1);
		}
		if (valorParcelaNegociacaoExtenso == null || valorParcelaNegociacaoExtenso.equals("0") || valorParcelaNegociacaoExtenso.equals("")) {
			valorParcelaNegociacaoExtenso = emBranco;
		}
		String quantidadeParcelaNegociacao = quantParcNegociacao.toString();
		if (quantidadeParcelaNegociacao == null || quantidadeParcelaNegociacao.equals("0") || quantidadeParcelaNegociacao.equals("")) {
			quantidadeParcelaNegociacao = emBranco;
		}
		ext.setNumber(valorParceladoDebitoDouble);
		String valorParcelaDebitoExtensoSemTratamento = ext.toString();
		String valorParcelaDebitoExtenso = "";
		if (!valorParcelaDebitoExtensoSemTratamento.isEmpty() && valorParcelaDebitoExtensoSemTratamento.length() > 1) {
			valorParcelaDebitoExtenso = valorParcelaDebitoExtensoSemTratamento.substring(0, 1).toUpperCase() + valorParcelaDebitoExtensoSemTratamento.substring(1);
		}
		if (valorParcelaDebitoExtenso == null || valorParcelaDebitoExtenso.equals("0") || valorParcelaDebitoExtenso.equals("")) {
			valorParcelaDebitoExtenso = emBranco;
		}
		Double valorResidualDouble = impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getValorFinal() - valorEntradaDebitoQuandoNaoExistirEntradaNegociacao;
		String valorResidual = Uteis.getDoubleFormatado(valorResidualDouble);
		if (valorResidual == null || valorResidual.equals("0") || valorResidual.equals("")) {
			valorResidual = emBranco;
		}
		ext.setNumber(valorResidualDouble);
		String valorResidualExtensoSemTratamento = ext.toString();
		String valorResidualExtenso = "";
		if (!valorResidualExtensoSemTratamento.isEmpty() && valorResidualExtensoSemTratamento.length() > 1) {
			valorResidualExtenso = valorResidualExtensoSemTratamento.substring(0, 1).toUpperCase() + valorResidualExtensoSemTratamento.substring(1);
		}
		if (valorResidualExtenso == null || valorResidualExtenso.equals("0") || valorResidualExtenso.equals("")) {
			valorResidualExtenso = emBranco;
		}
		
		ext.setNumber(quantParcNegociacao);
		String quantidadeParcelaNegociacaoExtenso = ext.toStringNaoMonetario();
		if (quantidadeParcelaNegociacaoExtenso == null || quantidadeParcelaNegociacaoExtenso.equals("0") || quantidadeParcelaNegociacaoExtenso.equals("")) {
			quantidadeParcelaNegociacaoExtenso = emBranco;
		}
		if (diaPrimeiroVencimento == null || diaPrimeiroVencimento.equals("0") || diaPrimeiroVencimento.equals("")) {
			diaPrimeiroVencimento = emBranco;
		}
		Date dataVencimento = null;
		if (diaPrimeiroVencimento.length() > 8) {
			dataVencimento =  Uteis.getData(diaPrimeiroVencimento, "dd/MM/yyyy");
		} else {
			dataVencimento =  Uteis.getData(diaPrimeiroVencimento, "dd/MM/yy");
		}
		String diaPrimeiroVencimentoExtenso = Uteis.getDataCidadeDiaMesPorExtensoEAno("", dataVencimento, false);
		if (diaPrimeiroVencimentoExtenso == null || diaPrimeiroVencimentoExtenso.equals("0") || diaPrimeiroVencimentoExtenso.equals("")) {
			diaPrimeiroVencimentoExtenso = emBranco;
		}
		if (diaSegundoVencimento == null || diaSegundoVencimento.equals("0") || diaSegundoVencimento.equals("")) {
			diaSegundoVencimento = emBranco;
		}
		if (diaPrimeiroVencimento.length() > 8) {
			dataVencimento =  Uteis.getData(diaSegundoVencimento, "dd/MM/yyyy");
		} else {
			dataVencimento =  Uteis.getData(diaSegundoVencimento, "dd/MM/yy");
		}
		String diaSegundoVencimentoExtenso = Uteis.getDataCidadeDiaMesPorExtensoEAno("", dataVencimento, false);
		if (diaSegundoVencimentoExtenso == null || diaSegundoVencimentoExtenso.equals("0") || diaSegundoVencimentoExtenso.equals("")) {
			diaSegundoVencimentoExtenso = emBranco;
		}
		DadosComerciaisVO dadosComerciaisVO = impressaoContratoVO.getMatriculaVO().getAluno().getDadosComerciaisVOs()
				.stream()
				.filter(DadosComerciaisVO::getEmpregoAtual)
				.findFirst().orElse(new DadosComerciaisVO());
		String ultimoCargoAluno = dadosComerciaisVO.getCargoPessoaEmpresa();
		if (ultimoCargoAluno == null || ultimoCargoAluno.equals("0") || ultimoCargoAluno.equals("")) {
			ultimoCargoAluno = emBranco;
		}
		String empresaAluno = dadosComerciaisVO.getNomeEmpresa();
		if (empresaAluno == null || empresaAluno.equals("0") || empresaAluno.equals("")) {
			empresaAluno = emBranco;
		}
		String campoJustificativaNegociacao = impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getJustificativa();
		if (campoJustificativaNegociacao == null || campoJustificativaNegociacao.equals("0") || campoJustificativaNegociacao.equals("")) {
			campoJustificativaNegociacao = emBranco;
		}
		
		String filiacaoMaeNomeAluno = emBranco;
		String filiacaoMaeCPFAluno = emBranco;
		String filiacaoMaeCEPAluno = emBranco;
		String filiacaoMaeRGAluno = emBranco;
		String filiacaoMaeEnderecoAluno = emBranco;
		String filiacaoMaeNumeroAluno = emBranco;
		String filiacaoMaeSetorAluno = emBranco;
		String filiacaoMaeComplementoEnderecoAluno = emBranco;
		String filiacaoMaeEstadoAluno = emBranco;
		String filiacaoMaeCidadeAluno = emBranco;
		String filiacaoMaeUFAluno = emBranco;
		String filiacaoMaeTelefoneComercialAluno = emBranco;
		String filiacaoMaeTelefoneResidencialAluno = emBranco;
		String filiacaoMaeTelefoneRecadoAluno = emBranco;
		String filiacaoMaeEmailAluno = emBranco;
		String filiacaoMaeCelularAluno = emBranco;
		String filiacaoMaeDataNascimentoAluno = emBranco;
		String filiacaoMaeNacionalidadeAluno = emBranco;
		String filiacaoMaeEstadoCivilAluno = emBranco;
		String filiacaoMaeOrgaoEmissorAluno = emBranco;
		String filiacaoPaiNomeAluno = emBranco;
		String filiacaoPaiCPFAluno = emBranco;
		String filiacaoPaiCEPAluno = emBranco;
		String filiacaoPaiRGAluno = emBranco;
		String filiacaoPaiEnderecoAluno = emBranco;
		String filiacaoPaiNumeroAluno = emBranco;
		String filiacaoPaiSetorAluno = emBranco;
		String filiacaoPaiComplementoEnderecoAluno = emBranco;
		String filiacaoPaiEstadoAluno = emBranco;
		String filiacaoPaiCidadeAluno = emBranco;
		String filiacaoPaiUFAluno = emBranco;
		String filiacaoPaiTelefoneComercialAluno = emBranco;
		String filiacaoPaiTelefoneResidencialAluno = emBranco;
		String filiacaoPaiTelefoneRecadoAluno = emBranco;
		String filiacaoPaiEmailAluno = emBranco;
		String filiacaoPaiCelularAluno = emBranco;
		String filiacaoPaiDataNascimentoAluno = emBranco;
		String filiacaoPaiNacionalidadeAluno = emBranco;
		String filiacaoPaiEstadoCivilAluno = emBranco;
		String filiacaoPaiOrgaoEmissorAluno = emBranco;
		
		for (FiliacaoVO filiacao : pessoa.getFiliacaoVOs()) {
			if (filiacao.getTipo().equals("MA")) {
				filiacaoMaeNomeAluno = filiacao.getPais().getNome();
				if (filiacaoMaeNomeAluno == null || filiacaoMaeNomeAluno.equals("") || filiacaoMaeNomeAluno.equals("0")) {
					filiacaoMaeNomeAluno = emBranco;
				}
				filiacaoMaeCPFAluno = filiacao.getPais().getCPF();
				if (filiacaoMaeCPFAluno == null || filiacaoMaeCPFAluno.equals("") || filiacaoMaeCPFAluno.equals("0")) {
					filiacaoMaeCPFAluno = emBranco;
				}
				filiacaoMaeCEPAluno = filiacao.getPais().getCEP();
				if (filiacaoMaeCEPAluno == null || filiacaoMaeCEPAluno.equals("") || filiacaoMaeCEPAluno.equals("0")) {
					filiacaoMaeCEPAluno = emBranco;
				}
				filiacaoMaeRGAluno = filiacao.getPais().getRG();
				if (filiacaoMaeRGAluno == null || filiacaoMaeRGAluno.equals("") || filiacaoMaeRGAluno.equals("0")) {
					filiacaoMaeRGAluno = emBranco;
				}
				filiacaoMaeEnderecoAluno = filiacao.getPais().getEndereco();
				if (filiacaoMaeEnderecoAluno == null || filiacaoMaeEnderecoAluno.equals("") || filiacaoMaeEnderecoAluno.equals("0")) {
					filiacaoMaeEnderecoAluno = emBranco;
				}
				filiacaoMaeNumeroAluno = filiacao.getPais().getNumero();
				if (filiacaoMaeNumeroAluno == null || filiacaoMaeNumeroAluno.equals("") || filiacaoMaeNumeroAluno.equals("0")) {
					filiacaoMaeNumeroAluno = emBranco;
				}
				filiacaoMaeSetorAluno = filiacao.getPais().getSetor();
				if (filiacaoMaeSetorAluno == null || filiacaoMaeSetorAluno.equals("") || filiacaoMaeSetorAluno.equals("0")) {
					filiacaoMaeSetorAluno = emBranco;
				}
				filiacaoMaeComplementoEnderecoAluno = filiacao.getPais().getComplemento();
				if (filiacaoMaeComplementoEnderecoAluno == null || filiacaoMaeComplementoEnderecoAluno.equals("") || filiacaoMaeComplementoEnderecoAluno.equals("0")) {
					filiacaoMaeComplementoEnderecoAluno = emBranco;
				}
				filiacaoMaeEstadoAluno = filiacao.getPais().getCidade().getEstado().getNome();
				if (filiacaoMaeEstadoAluno == null || filiacaoMaeEstadoAluno.equals("") || filiacaoMaeEstadoAluno.equals("0")) {
					filiacaoMaeEstadoAluno = emBranco;
				}
				filiacaoMaeCidadeAluno = filiacao.getPais().getCidade().getNome();
				if (filiacaoMaeCidadeAluno == null || filiacaoMaeCidadeAluno.equals("") || filiacaoMaeCidadeAluno.equals("0")) {
					filiacaoMaeCidadeAluno = emBranco;
				}
				filiacaoMaeUFAluno = filiacao.getPais().getCidade().getEstado().getSigla();
				if (filiacaoMaeUFAluno == null || filiacaoMaeUFAluno.equals("") || filiacaoMaeUFAluno.equals("0")) {
					filiacaoMaeUFAluno = emBranco;
				}
				filiacaoMaeTelefoneComercialAluno = filiacao.getPais().getTelefoneComer();
				if (filiacaoMaeTelefoneComercialAluno == null || filiacaoMaeTelefoneComercialAluno.equals("") || filiacaoMaeTelefoneComercialAluno.equals("0")) {
					filiacaoMaeTelefoneComercialAluno = emBranco;
				}
				filiacaoMaeTelefoneResidencialAluno = filiacao.getPais().getTelefoneRes();
				if (filiacaoMaeTelefoneResidencialAluno == null || filiacaoMaeTelefoneResidencialAluno.equals("") || filiacaoMaeTelefoneResidencialAluno.equals("0")) {
					filiacaoMaeTelefoneResidencialAluno = emBranco;
				}
				filiacaoMaeTelefoneRecadoAluno = filiacao.getPais().getTelefoneRecado();
				if (filiacaoMaeTelefoneRecadoAluno == null || filiacaoMaeTelefoneRecadoAluno.equals("") || filiacaoMaeTelefoneRecadoAluno.equals("0")) {
					filiacaoMaeTelefoneRecadoAluno = emBranco;
				}
				filiacaoMaeEmailAluno = filiacao.getPais().getEmail();
				if (filiacaoMaeEmailAluno == null || filiacaoMaeEmailAluno.equals("") || filiacaoMaeEmailAluno.equals("0")) {
					filiacaoMaeEmailAluno = emBranco;
				}
				filiacaoMaeCelularAluno = filiacao.getPais().getCelular();
				if (filiacaoMaeCelularAluno == null || filiacaoMaeCelularAluno.equals("") || filiacaoMaeCelularAluno.equals("0")) {
					filiacaoMaeCelularAluno = emBranco;
				}
				filiacaoMaeDataNascimentoAluno = filiacao.getPais().getDataNasc_Apresentar();
				if (filiacaoMaeDataNascimentoAluno == null || filiacaoMaeDataNascimentoAluno.equals("") || filiacaoMaeDataNascimentoAluno.equals("0")) {
					filiacaoMaeDataNascimentoAluno = emBranco;
				}
				filiacaoMaeNacionalidadeAluno = filiacao.getPais().getNacionalidade().getNacionalidade();
				if (filiacaoMaeNacionalidadeAluno == null || filiacaoMaeNacionalidadeAluno.equals("") || filiacaoMaeNacionalidadeAluno.equals("0")) {
					filiacaoMaeNacionalidadeAluno = emBranco;
				}
				filiacaoMaeEstadoCivilAluno = filiacao.getPais().getEstadoCivil_Apresentar();
				if (filiacaoMaeEstadoCivilAluno == null || filiacaoMaeEstadoCivilAluno.equals("") || filiacaoMaeEstadoCivilAluno.equals("0")) {
					filiacaoMaeEstadoCivilAluno = emBranco;
				}
				filiacaoMaeOrgaoEmissorAluno = filiacao.getPais().getOrgaoEmissor();
				if (filiacaoMaeOrgaoEmissorAluno == null || filiacaoMaeOrgaoEmissorAluno.equals("") || filiacaoMaeOrgaoEmissorAluno.equals("0")) {
					filiacaoMaeOrgaoEmissorAluno = emBranco;
				}
			} else if (filiacao.getTipo().equals("PA")) {
				filiacaoPaiNomeAluno = filiacao.getPais().getNome();
				if (filiacaoPaiNomeAluno == null || filiacaoPaiNomeAluno.equals("") || filiacaoPaiNomeAluno.equals("0")) {
					filiacaoPaiNomeAluno = emBranco;
				}
				filiacaoPaiCPFAluno = filiacao.getPais().getCPF();
				if (filiacaoPaiCPFAluno == null || filiacaoPaiCPFAluno.equals("") || filiacaoPaiCPFAluno.equals("0")) {
					filiacaoPaiCPFAluno = emBranco;
				}
				filiacaoPaiCEPAluno = filiacao.getPais().getCEP();
				if (filiacaoPaiCEPAluno == null || filiacaoPaiCEPAluno.equals("") || filiacaoPaiCEPAluno.equals("0")) {
					filiacaoPaiCEPAluno = emBranco;
				}
				filiacaoPaiRGAluno = filiacao.getPais().getRG();
				if (filiacaoPaiRGAluno == null || filiacaoPaiRGAluno.equals("") || filiacaoPaiRGAluno.equals("0")) {
					filiacaoPaiRGAluno = emBranco;
				}
				filiacaoPaiEnderecoAluno = filiacao.getPais().getEndereco();
				if (filiacaoPaiEnderecoAluno == null || filiacaoPaiEnderecoAluno.equals("") || filiacaoPaiEnderecoAluno.equals("0")) {
					filiacaoPaiEnderecoAluno = emBranco;
				}
				filiacaoPaiNumeroAluno = filiacao.getPais().getNumero();
				if (filiacaoPaiNumeroAluno == null || filiacaoPaiNumeroAluno.equals("") || filiacaoPaiNumeroAluno.equals("0")) {
					filiacaoPaiNumeroAluno = emBranco;
				}
				filiacaoPaiSetorAluno = filiacao.getPais().getSetor();
				if (filiacaoPaiSetorAluno == null || filiacaoPaiSetorAluno.equals("") || filiacaoPaiSetorAluno.equals("0")) {
					filiacaoPaiSetorAluno = emBranco;
				}
				filiacaoPaiComplementoEnderecoAluno = filiacao.getPais().getComplemento();
				if (filiacaoPaiComplementoEnderecoAluno == null || filiacaoPaiComplementoEnderecoAluno.equals("") || filiacaoPaiComplementoEnderecoAluno.equals("0")) {
					filiacaoPaiComplementoEnderecoAluno = emBranco;
				}
				filiacaoPaiEstadoAluno = filiacao.getPais().getCidade().getEstado().getNome();
				if (filiacaoPaiEstadoAluno == null || filiacaoPaiEstadoAluno.equals("") || filiacaoPaiEstadoAluno.equals("0")) {
					filiacaoPaiEstadoAluno = emBranco;
				}
				filiacaoPaiCidadeAluno = filiacao.getPais().getCidade().getNome();
				if (filiacaoPaiCidadeAluno == null || filiacaoPaiCidadeAluno.equals("") || filiacaoPaiCidadeAluno.equals("0")) {
					filiacaoPaiCidadeAluno = emBranco;
				}
				filiacaoPaiUFAluno = filiacao.getPais().getCidade().getEstado().getSigla();
				if (filiacaoPaiUFAluno == null || filiacaoPaiUFAluno.equals("") || filiacaoPaiUFAluno.equals("0")) {
					filiacaoPaiUFAluno = emBranco;
				}
				filiacaoPaiTelefoneComercialAluno = filiacao.getPais().getTelefoneComer();
				if (filiacaoPaiTelefoneComercialAluno == null || filiacaoPaiTelefoneComercialAluno.equals("") || filiacaoPaiTelefoneComercialAluno.equals("0")) {
					filiacaoPaiTelefoneComercialAluno = emBranco;
				}
				filiacaoPaiTelefoneResidencialAluno = filiacao.getPais().getTelefoneRes();
				if (filiacaoPaiTelefoneResidencialAluno == null || filiacaoPaiTelefoneResidencialAluno.equals("") || filiacaoPaiTelefoneResidencialAluno.equals("0")) {
					filiacaoPaiTelefoneResidencialAluno = emBranco;
				}
				filiacaoPaiTelefoneRecadoAluno = filiacao.getPais().getTelefoneRecado();
				if (filiacaoPaiTelefoneRecadoAluno == null || filiacaoPaiTelefoneRecadoAluno.equals("") || filiacaoPaiTelefoneRecadoAluno.equals("0")) {
					filiacaoPaiTelefoneRecadoAluno = emBranco;
				}
				filiacaoPaiEmailAluno = filiacao.getPais().getEmail();
				if (filiacaoPaiEmailAluno == null || filiacaoPaiEmailAluno.equals("") || filiacaoPaiEmailAluno.equals("0")) {
					filiacaoPaiEmailAluno = emBranco;
				}
				filiacaoPaiCelularAluno = filiacao.getPais().getCelular();
				if (filiacaoPaiCelularAluno == null || filiacaoPaiCelularAluno.equals("") || filiacaoPaiCelularAluno.equals("0")) {
					filiacaoPaiCelularAluno = emBranco;
				}
				filiacaoPaiDataNascimentoAluno = filiacao.getPais().getDataNasc_Apresentar();
				if (filiacaoPaiDataNascimentoAluno == null || filiacaoPaiDataNascimentoAluno.equals("") || filiacaoPaiDataNascimentoAluno.equals("0")) {
					filiacaoPaiDataNascimentoAluno = emBranco;
				}
				filiacaoPaiNacionalidadeAluno = filiacao.getPais().getNacionalidade().getNacionalidade();
				if (filiacaoPaiNacionalidadeAluno == null || filiacaoPaiNacionalidadeAluno.equals("") || filiacaoPaiNacionalidadeAluno.equals("0")) {
					filiacaoPaiNacionalidadeAluno = emBranco;
				}
				filiacaoPaiEstadoCivilAluno = filiacao.getPais().getEstadoCivil_Apresentar();
				if (filiacaoPaiEstadoCivilAluno == null || filiacaoPaiEstadoCivilAluno.equals("") || filiacaoPaiEstadoCivilAluno.equals("0")) {
					filiacaoPaiEstadoCivilAluno = emBranco;
				}
				filiacaoPaiOrgaoEmissorAluno = filiacao.getPais().getOrgaoEmissor();
				if (filiacaoPaiOrgaoEmissorAluno == null || filiacaoPaiOrgaoEmissorAluno.equals("") || filiacaoPaiOrgaoEmissorAluno.equals("0")) {
					filiacaoPaiOrgaoEmissorAluno = emBranco;
				}
			}
		}
		
		String nomeFiador = "";
		String cpfFiador = "";
		String cepFiador = "";
		String enderecoFiador = "";
		String bairroFiador = "";
		String numeroEnderecoFiador = "";
		String complementoEnderecoFiador = "";
		String cidadeFiador = "";
		String estadoFiador = "";
		String telefoneFiador = "";
		String celularFiador = "";
		String estadoCivilFiador = "";
		String rgFiador = "";
		String dataNascimentoFiador = "";
		String profissaoFiador = "";
		String paisFiador = "";
		
		nomeFiador = pessoa.getNomeFiador();
		if (nomeFiador == null || nomeFiador.equals("0") || nomeFiador.equals("")) {
			nomeFiador = emBranco;
		}
		cpfFiador = pessoa.getCpfFiador();
		if (cpfFiador == null || cpfFiador.equals("0") || cpfFiador.equals("")) {
			cpfFiador = emBranco;
		}
		cepFiador = pessoa.getCepFiador();
		if (cepFiador == null || cepFiador.equals("0") || cepFiador.equals("")) {
			cepFiador = emBranco;
		}
		enderecoFiador = pessoa.getEnderecoFiador();
		if (enderecoFiador == null || enderecoFiador.equals("0") || enderecoFiador.equals("")) {
			enderecoFiador = emBranco;
		}
		bairroFiador = pessoa.getSetorFiador();
		if (bairroFiador == null || bairroFiador.equals("0") || bairroFiador.equals("")) {
			bairroFiador = emBranco;
		}
		numeroEnderecoFiador = pessoa.getNumeroEndFiador();
		if (numeroEnderecoFiador == null || numeroEnderecoFiador.equals("0") || numeroEnderecoFiador.equals("")) {
			numeroEnderecoFiador = emBranco;
		}
		complementoEnderecoFiador = pessoa.getComplementoFiador();
		if (complementoEnderecoFiador == null || complementoEnderecoFiador.equals("0") || complementoEnderecoFiador.equals("")) {
			complementoEnderecoFiador = emBranco;
		}
		cidadeFiador = pessoa.getCidadeFiador().getNome().toUpperCase() + " - " + pessoa.getCidadeFiador().getEstado().getSigla();
		if (cidadeFiador == null || cidadeFiador.equals("0") || cidadeFiador.equals("")) {
			cidadeFiador = emBranco;
		}
		estadoFiador = pessoa.getCidadeFiador().getEstado().getNome();
		if (estadoFiador == null || estadoFiador.equals("0") || estadoFiador.equals("")) {
			estadoFiador = emBranco;
		}
		telefoneFiador = pessoa.getTelefoneFiador();
		if (telefoneFiador == null || telefoneFiador.equals("0") || telefoneFiador.equals("")) {
			telefoneFiador = emBranco;
		}
		celularFiador = pessoa.getCelularFiador();
		if (celularFiador == null || celularFiador.equals("0") || celularFiador.equals("")) {
			celularFiador = emBranco;
		}
		estadoCivilFiador = pessoa.getEstadoCivilFiadorApresentar();
		if (estadoCivilFiador == null || estadoCivilFiador.equals("0") || estadoCivilFiador.equals("")) {
			estadoCivilFiador = emBranco;
		}
		rgFiador = pessoa.getRgFiador();
		if (rgFiador == null || rgFiador.equals("0") || rgFiador.equals("")) {
			rgFiador = emBranco;
		}
		dataNascimentoFiador = Uteis.getData(pessoa.getDataNascimentoFiador());
		if (dataNascimentoFiador == null || dataNascimentoFiador.equals("0") || dataNascimentoFiador.equals("")) {
			dataNascimentoFiador = emBranco;
		}
		profissaoFiador = pessoa.getProfissaoFiador();
		if (profissaoFiador == null || profissaoFiador.equals("0") || profissaoFiador.equals("")) {
			profissaoFiador = emBranco;
		}
		paisFiador = pessoa.getPaisFiador().getNome();
		if (paisFiador == null || paisFiador.equals("0") || paisFiador.equals("")) {
			paisFiador = emBranco;
		}
		
		String telefoneComercialAluno = pessoa.getTelefoneComer();
		if (telefoneComercialAluno == null || telefoneComercialAluno.equals("") || telefoneComercialAluno.equals("0")) {
			telefoneComercialAluno = emBranco;
		}
		
		ConvenioVO convenioVO = null;
		String nomeParceiroConvenioAluno = emBranco;
		String cnpjParceiroConvenioAluno = emBranco;
		String nomeResponsavelParceiroConvenioAluno = emBranco;
		String cpfResponsavelParceiroConvenioAluno = emBranco;
		String rgResponsavelParceiroConvenioAluno = emBranco;
		String enderecoResponsavelParceiroConvenioAluno = emBranco;
		String bairroResponsavelParceiroConvenioAluno = emBranco;
		String cidadeResponsavelParceiroConvenioAluno = emBranco;
		String cepResponsavelParceiroConvenioAluno = emBranco;
		String telefoneResponsavelParceiroConvenioAluno = emBranco;
		String emailResponsavelParceiroConvenioAluno = emBranco;
		if (!impressaoContratoVO.getMatriculaVO().getPlanoFinanceiroAluno().getPlanoFinanceiroConvenioVOs().isEmpty()) {
			convenioVO = impressaoContratoVO.getMatriculaVO().getPlanoFinanceiroAluno().getPlanoFinanceiroConvenioVOs().stream().findFirst().orElse(null);
		}
		if (Uteis.isAtributoPreenchido(convenioVO)) {
			nomeParceiroConvenioAluno = convenioVO.getParceiro().getNome();
			if (nomeParceiroConvenioAluno == null || nomeParceiroConvenioAluno.trim().equals("") || nomeParceiroConvenioAluno.equals("0")) {
				nomeParceiroConvenioAluno = emBranco;
			}
			cnpjParceiroConvenioAluno = convenioVO.getParceiro().getCNPJ();
			if (cnpjParceiroConvenioAluno == null || cnpjParceiroConvenioAluno.trim().equals("") || cnpjParceiroConvenioAluno.equals("0")) {
				cnpjParceiroConvenioAluno = emBranco;
			}
			ContatoParceiroVO responsavelContatoParceiroVO = null;
			if (!convenioVO.getParceiro().getContatoParceiroVOs().isEmpty()) {
				responsavelContatoParceiroVO = convenioVO.getParceiro().getContatoParceiroVOs().stream().filter(ContatoParceiroVO::getResponsavelLegal).findFirst().orElse(null);
			}
			if (Uteis.isAtributoPreenchido(responsavelContatoParceiroVO)) {
				nomeResponsavelParceiroConvenioAluno = responsavelContatoParceiroVO.getNome();
				if (nomeResponsavelParceiroConvenioAluno == null || nomeResponsavelParceiroConvenioAluno.trim().equals("") || nomeResponsavelParceiroConvenioAluno.equals("0")) {
					nomeResponsavelParceiroConvenioAluno = emBranco;
				}
				cpfResponsavelParceiroConvenioAluno = responsavelContatoParceiroVO.getCpf();
				if (cpfResponsavelParceiroConvenioAluno == null || cpfResponsavelParceiroConvenioAluno.trim().equals("") || cpfResponsavelParceiroConvenioAluno.equals("0")) {
					cpfResponsavelParceiroConvenioAluno = emBranco;
				}
				rgResponsavelParceiroConvenioAluno = responsavelContatoParceiroVO.getRg();
				if (rgResponsavelParceiroConvenioAluno == null || rgResponsavelParceiroConvenioAluno.trim().equals("") || rgResponsavelParceiroConvenioAluno.equals("0")) {
					rgResponsavelParceiroConvenioAluno = emBranco;
				}
				enderecoResponsavelParceiroConvenioAluno = responsavelContatoParceiroVO.getEndereco() +" "+ responsavelContatoParceiroVO.getComplemento();
				if (enderecoResponsavelParceiroConvenioAluno == null || enderecoResponsavelParceiroConvenioAluno.trim().equals("") || enderecoResponsavelParceiroConvenioAluno.equals("0")) {
					enderecoResponsavelParceiroConvenioAluno = emBranco;
				}
				bairroResponsavelParceiroConvenioAluno = responsavelContatoParceiroVO.getSetor();
				if (bairroResponsavelParceiroConvenioAluno == null || bairroResponsavelParceiroConvenioAluno.trim().equals("") || bairroResponsavelParceiroConvenioAluno.equals("0")) {
					bairroResponsavelParceiroConvenioAluno = emBranco;
				}
				cidadeResponsavelParceiroConvenioAluno = responsavelContatoParceiroVO.getCidade().getNome();
				if (cidadeResponsavelParceiroConvenioAluno == null || cidadeResponsavelParceiroConvenioAluno.trim().equals("") || cidadeResponsavelParceiroConvenioAluno.equals("0")) {
					cidadeResponsavelParceiroConvenioAluno = emBranco;
				}
				cepResponsavelParceiroConvenioAluno = responsavelContatoParceiroVO.getCEP();
				if (cepResponsavelParceiroConvenioAluno == null || cepResponsavelParceiroConvenioAluno.trim().equals("") || cepResponsavelParceiroConvenioAluno.equals("0")) {
					cepResponsavelParceiroConvenioAluno = emBranco;
				}
				telefoneResponsavelParceiroConvenioAluno = responsavelContatoParceiroVO.getTelefone();
				if (telefoneResponsavelParceiroConvenioAluno == null || telefoneResponsavelParceiroConvenioAluno.trim().equals("") || telefoneResponsavelParceiroConvenioAluno.equals("0")) {
					telefoneResponsavelParceiroConvenioAluno = emBranco;
				}
				emailResponsavelParceiroConvenioAluno = responsavelContatoParceiroVO.getEmail();
				if (emailResponsavelParceiroConvenioAluno == null || emailResponsavelParceiroConvenioAluno.trim().equals("") || emailResponsavelParceiroConvenioAluno.equals("0")) {
					emailResponsavelParceiroConvenioAluno = emBranco;
				}
			}
		}

		String numeroRegistroDiplomaPrimeiraVia = "";
		String numeroProcessoDiplomaPrimeiraVia = "";
		String dataExpedicaoDiplomaPrimeiraVia = "";
		String assinaturaPrimeiroFuncionarioDiplomaPrimeiraVia = "";
		String assinaturaSegundoFuncionarioDiplomaPrimeiraVia = "";
		String assinaturaTerceiroFuncionarioDiplomaPrimeiraVia = "";
		String cargoPrimeiroFuncionarioDiplomaPrimeiraVia = "";
		String cargoSegundoFuncionarioDiplomaPrimeiraVia = "";
		String cargoTerceiroFuncionarioDiplomaPrimeiraVia = "";
		
		if (!impressaoContratoVO.getExpedicaoDiplomaVO().getVia().equals("1")) {
			numeroRegistroDiplomaPrimeiraVia = impressaoContratoVO.getExpedicaoDiplomaVO().getNumeroRegistroDiplomaViaAnterior();
			numeroProcessoDiplomaPrimeiraVia = impressaoContratoVO.getExpedicaoDiplomaVO().getNumeroProcessoViaAnterior();
			dataExpedicaoDiplomaPrimeiraVia = Uteis.getData(impressaoContratoVO.getExpedicaoDiplomaVO().getDataRegistroDiplomaViaAnterior() , "dd/MM/yyyy");
			if (Uteis.isAtributoPreenchido(impressaoContratoVO.getPrimeiraViaExpedicaoDiplomaVO())) {
				assinaturaPrimeiroFuncionarioDiplomaPrimeiraVia = 
						Uteis.alterarpreposicaoParaMinusculo(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(impressaoContratoVO.getPrimeiraViaExpedicaoDiplomaVO().getFuncionarioPrimarioVO().getPessoa().getNome().toLowerCase()));
				assinaturaSegundoFuncionarioDiplomaPrimeiraVia = 
						Uteis.alterarpreposicaoParaMinusculo(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(impressaoContratoVO.getPrimeiraViaExpedicaoDiplomaVO().getFuncionarioSecundarioVO().getPessoa().getNome().toLowerCase()));
				assinaturaTerceiroFuncionarioDiplomaPrimeiraVia = 
						Uteis.alterarpreposicaoParaMinusculo(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(impressaoContratoVO.getPrimeiraViaExpedicaoDiplomaVO().getFuncionarioTerceiroVO().getPessoa().getNome().toLowerCase()));
				cargoPrimeiroFuncionarioDiplomaPrimeiraVia = 
						Uteis.alterarpreposicaoParaMinusculo(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(impressaoContratoVO.getPrimeiraViaExpedicaoDiplomaVO().getCargoFuncionarioPrincipalVO().getNome().toLowerCase()));
				cargoSegundoFuncionarioDiplomaPrimeiraVia = 
						Uteis.alterarpreposicaoParaMinusculo(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(impressaoContratoVO.getPrimeiraViaExpedicaoDiplomaVO().getCargoFuncionarioSecundarioVO().getNome().toLowerCase()));
				cargoTerceiroFuncionarioDiplomaPrimeiraVia = 
						Uteis.alterarpreposicaoParaMinusculo(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(impressaoContratoVO.getPrimeiraViaExpedicaoDiplomaVO().getCargoFuncionarioTerceiroVO().getNome().toLowerCase()));
			}
		} else {
			numeroRegistroDiplomaPrimeiraVia = Uteis.isAtributoPreenchido(impressaoContratoVO.getExpedicaoDiplomaVO().getNumeroRegistroDiploma()) ? 
					impressaoContratoVO.getExpedicaoDiplomaVO().getNumeroRegistroDiploma() : impressaoContratoVO.getNumeroRegistroDiploma();
			numeroProcessoDiplomaPrimeiraVia = impressaoContratoVO.getExpedicaoDiplomaVO().getNumeroProcesso();
			dataExpedicaoDiplomaPrimeiraVia = Uteis.getData(impressaoContratoVO.getExpedicaoDiplomaVO().getDataExpedicao(), "dd/MM/yyyy");
		}
		
		if ((Uteis.isAtributoPreenchido(impressaoContratoVO.getControleLivroFolhaReciboVO().getControleLivroRegistroDiploma()) && impressaoContratoVO.getControleLivroFolhaReciboVO().getControleLivroRegistroDiploma().getTipoLivroRegistroDiplomaEnum().isTipoRegistroCertificado()) == false) {
			viaRegistroCertificado = Uteis.isAtributoPreenchido(impressaoContratoVO.getExpedicaoDiplomaVO().getVia()) ? impressaoContratoVO.getExpedicaoDiplomaVO().getVia() : impressaoContratoVO.getControleLivroFolhaReciboVO().getVia();
			if (viaRegistroCertificado == null || viaRegistroCertificado.equals("0") || viaRegistroCertificado.equals("")) {
				viaRegistroCertificado = emBranco;
			}
		}
		
		if (numeroRegistroDiplomaPrimeiraVia == null || numeroRegistroDiplomaPrimeiraVia.equals("0") || numeroRegistroDiplomaPrimeiraVia.equals("")) {
			numeroRegistroDiplomaPrimeiraVia = emBranco;
		}
		
		if (numeroProcessoDiplomaPrimeiraVia == null || numeroProcessoDiplomaPrimeiraVia.equals("0") || numeroProcessoDiplomaPrimeiraVia.equals("")) {
			numeroProcessoDiplomaPrimeiraVia = emBranco;
		}
		
		if (dataExpedicaoDiplomaPrimeiraVia == null || dataExpedicaoDiplomaPrimeiraVia.equals("0") || dataExpedicaoDiplomaPrimeiraVia.equals("")) {
			dataExpedicaoDiplomaPrimeiraVia = emBranco;
		}
		
		if (assinaturaPrimeiroFuncionarioDiplomaPrimeiraVia == null || assinaturaPrimeiroFuncionarioDiplomaPrimeiraVia.equals("0") || assinaturaPrimeiroFuncionarioDiplomaPrimeiraVia.equals("")) {
			assinaturaPrimeiroFuncionarioDiplomaPrimeiraVia = emBranco;
		}
		if (assinaturaSegundoFuncionarioDiplomaPrimeiraVia == null || assinaturaSegundoFuncionarioDiplomaPrimeiraVia.equals("0") || assinaturaSegundoFuncionarioDiplomaPrimeiraVia.equals("")) {
			assinaturaSegundoFuncionarioDiplomaPrimeiraVia = emBranco;
		}
		if (assinaturaTerceiroFuncionarioDiplomaPrimeiraVia == null || assinaturaTerceiroFuncionarioDiplomaPrimeiraVia.equals("0") || assinaturaTerceiroFuncionarioDiplomaPrimeiraVia.equals("")) {
			assinaturaTerceiroFuncionarioDiplomaPrimeiraVia = emBranco;
		}
		if (cargoPrimeiroFuncionarioDiplomaPrimeiraVia == null || cargoPrimeiroFuncionarioDiplomaPrimeiraVia.equals("0") || cargoPrimeiroFuncionarioDiplomaPrimeiraVia.equals("")) {
			cargoPrimeiroFuncionarioDiplomaPrimeiraVia = emBranco;
		}
		if (cargoSegundoFuncionarioDiplomaPrimeiraVia == null || cargoSegundoFuncionarioDiplomaPrimeiraVia.equals("0") || cargoSegundoFuncionarioDiplomaPrimeiraVia.equals("")) {
			cargoSegundoFuncionarioDiplomaPrimeiraVia = emBranco;
		}
		if (cargoTerceiroFuncionarioDiplomaPrimeiraVia == null || cargoTerceiroFuncionarioDiplomaPrimeiraVia.equals("0") || cargoTerceiroFuncionarioDiplomaPrimeiraVia.equals("")) {
			cargoTerceiroFuncionarioDiplomaPrimeiraVia = emBranco;
		}
		
		String urlFotoAluno = matriculaVO.getAluno().getUrlFotoAluno();
		if (urlFotoAluno == null || urlFotoAluno.equals("0") || urlFotoAluno.equals("")) {
			urlFotoAluno = emBranco;
		}
		
		while (texto.indexOf(marcador.getTag()) != -1) {
			int tamanho = obterTamanhoTag(marcador.getTag());
			String mar = obterTagSemTextoSemTamanho(marcador.getTag());
			String textoTag = obterTextoTag(marcador.getTag());

			if (mar.equalsIgnoreCase("Codigo_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), codigo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Nome_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), nome, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Banco_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), banco, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Agencia_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), agencia, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ContaCorrente_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), contaCorrente, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ModalidadeBolsa_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), modalidadeBolsa, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("UniversidadeParceira_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), universidadeParceira, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorBolsa_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), valorBolsa, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorBolsaExtenso_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), valorBolsaPorExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Endereco_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), endereco, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ComplementoEnd_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), complementoendereco, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NumeroEnd_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), numero, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Cidade_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), cidade, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Estado_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), estado, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Bairro_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), setor, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Sexo_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), sexo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Naturalidade_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), naturalidade, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Nacionalidade_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), nacionalidade, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EstadoCivil_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), estadoCivil, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Rg_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), rg, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("OrgaoEmissor_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), orgaoEmissor, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EstadoEmissor_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), estadoEmissor, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataEmissaoRG_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), dataEmissaoRG, textoTag, tamanho);
				texto = substituirTag(texto, marcador.getTag(), dataEmissaoRGAnonimizada, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Cpf_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), cpf, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Idade_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), idade, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataNasc_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), dataNascimento, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataNascExtenso_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), dataNascimentoExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataNascExtenso2_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), dataNascimentoExtenso2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Telefone_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), telefoneRes, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Celular_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), celular, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Comercial_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), telefoneComercial, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CEP_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), cep, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Email_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), email, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Email2_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), email2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NomePai_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), nomePai, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NomeMae_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), nomeMae, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CursoFormacao_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), cursoFormaca, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EscolaridadeFormacao_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), escolaridadeFormaca, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("InstituicaoFormacao_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), instituicaoFormaca, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CidadeFormacao_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), cidadeFormaca, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("AreaConhecimentoFormacao_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), areaConhecimentoFormaca, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("SituacaoFormacao_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), situacaoFormaca, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataInicioFormacao_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), dataInicioFormaca, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataFinalFormacao_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), dataFinalFormaca, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("AnoDataFinalFormacao_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), anoDataFinalFormaca, textoTag, tamanho);

			} else if (mar.equalsIgnoreCase("NomeEmpresa_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), nomeEmpresa, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EnderecoEmpresa_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), enderecoEmpresa, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ComplementoEmpresa_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), complementoEmpresa, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("BairroEmpresa_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), bairroEmpresa, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CepEmpresa_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), cepEmpresa, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CidadeEmpresa_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), cidadeEmpresa + "/" + estadoCidadeEmpresa, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TelefoneEmpresa_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), telefoneComercialEmpresa, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NomeResponsavelFinanceiro_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), nomeResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CPFResponsavelFinanceiro_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), cpfResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("RGResponsavelFinanceiro_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), rgResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TelefoneComerResponsavelFinanceiro_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), telefoneComerResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TelefoneResResponsavelFinanceiro_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), telefoneResResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TelefoneRecadoResponsavelFinanceiro_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), telefoneRecadoResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CelularResponsavelFinanceiro_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), celularResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EmailResponsavelFinanceiro_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), emailResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CepResponsavelFinanceiro_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), cepResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NacionalidadeResponsavelFinanceiro_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), nacionalidadeResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EstadoCivilResponsavelFinanceiro_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), estadoCivilResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("GrauParentescoResponsavelFinanceiro_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), grauParentescoResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EnderecoResponsavelFinanceiro_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), enderecoResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NumeroEnderecoResponsavelFinanceiro_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), numeroEnderecoResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ComplementoEnderecoResponsavelFinanceiro_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), complementoEnderecoResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("OrgaoEmissoResponsavelFinanceiro_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), orgaoEmissoResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EstadoResponsavelFinanceiro_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), estadoResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CidadeResponsavelFinanceiro_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), cidadeResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataNascResponsavelFinanceiro_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), dataNascResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("SetorResponsavelFinanceiro_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), setorResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NomeResponsavelFinanceiro2_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), nomeResponsavelFinanceiro2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CPFResponsavelFinanceiro2_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), cpfResponsavelFinanceiro2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("RGResponsavelFinanceiro2_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), rgResponsavelFinanceiro2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TelefoneComerResponsavelFinanceiro2_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), telefoneComerResponsavelFinanceiro2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TelefoneResResponsavelFinanceiro2_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), telefoneResResponsavelFinanceiro2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TelefoneRecadoResponsavelFinanceiro2_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), telefoneRecadoResponsavelFinanceiro2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CelularResponsavelFinanceiro2_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), celularResponsavelFinanceiro2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EmailResponsavelFinanceiro2_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), emailResponsavelFinanceiro2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EnderecoResponsavelFinanceiro2_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), enderecoResponsavelFinanceiro2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("GrauParentescoResponsavelFinanceiro2_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), grauParentescoResponsavelFinanceiro2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NumeroEnderecoResponsavelFinanceiro2_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), numeroEnderecoResponsavelFinanceiro2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ComplementoEnderecoResponsavelFinanceiro2_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), complementoEnderecoResponsavelFinanceiro2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("OrgaoEmissoResponsavelFinanceiro2_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), orgaoEmissoResponsavelFinanceiro2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EstadoResponsavelFinanceiro2_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), estadoResponsavelFinanceiro2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CidadeResponsavelFinanceiro2_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), cidadeResponsavelFinanceiro2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataNascResponsavelFinanceiro2_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), dataNascResponsavelFinanceiro2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("SetorResponsavelFinanceiro2_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), setorResponsavelFinanceiro2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Justificativa_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), justificativa, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Observacoes_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), observacoes, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NumeroRegistroDiploma_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), numeroRegistroDiploma, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NumeroProcessoDiploma_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), numeroProcessoDiploma, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NumeroRegistroCertificado_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), numeroRegistroCertificado, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NaturalidadeEstado_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), naturalidadeEstado, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("SemestreIngresso_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), semestreIngresso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("AnoIngresso_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), anoIngresso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("UltimoSemestreCursado_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), ultimoSemestreCursado, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("UltimoAnoCursado_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), ultimoAnoCursado, textoTag, tamanho);
			}
			if (mar.equalsIgnoreCase("ListaEnade_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), listaEnade, textoTag, tamanho);
			}
			if (mar.equalsIgnoreCase("CoeficienteRendimentoPeriodoLetivo_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), coeficienteRendimentoPeriodoLetivo, textoTag, tamanho);
			}
			if (mar.equalsIgnoreCase("CoeficienteRendimentoGeral_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), coeficienteRendimentoGeral, textoTag, tamanho);
			}
//			if (mar.equalsIgnoreCase("NumeroProcessoDiploma_Aluno")) {
//				texto = substituirTag(texto, marcador.getTag(), livroRegistroDiploma, textoTag, tamanho);
//			}
			if (mar.equalsIgnoreCase("LivroRegistroDiploma_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), livroRegistroDiploma, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("LivroRegistroCertificado_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), livroRegistroCertificado, textoTag, tamanho);
			}
			if (mar.equalsIgnoreCase("FolhaRegistroDiploma_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), folhaRegistroDiploma, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FolhaRegistroCertificado_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), folhaRegistroCertificado, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ViaCertificado_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), viaRegistroCertificado, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EstadoFormacao_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), estadoFormaca, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DiaNascimento_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), diaNascimento, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("MesNascimento_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), mesNascimento, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("AnoNascimento_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), anoNascimento, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("AnoNascimento_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), anoNascimento, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("PreposicaoAouO_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), preposicaoAouO, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataPublicacaoDiploma_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), dataPublicacaoDiploma, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataPublicacaoCertificado_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), dataPublicacaoCertificado, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ListaDemostrativoDebito_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), listaDemostrativoDebitoAluno.toString(), textoTag, tamanho);
				substituirTagComLista(texto,  marcador.getTag(), impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getSub2(), textoTag);
			} else if (mar.equalsIgnoreCase("ListaDemostrativoDebitoNegociado_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), listaDemostrativoDebitoNegociadoAluno.toString(), textoTag, tamanho);
				substituirTagComLista(texto,  marcador.getTag(), impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getSub3(), textoTag);
			} else if (mar.equalsIgnoreCase("ValorTotalDebito_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalDebitoAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalDebitoExtenso_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalDebitoExtensoAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NumeroContratoDemostrativoDebito_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), numeroContratoDemostrativoDebitoAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("PaginaPessoal_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), paginaPessoalAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TituloEleitoral_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), tituloEleitoral, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ZonaEleitoral_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), zonaEleitoral, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Deficiencia_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), deficiencia, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CorRaca_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), corraca, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TipoAdvertencia_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), tipoAdvertencia, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DescricaoTipoAdvertencia_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), descricaoTipoAdvertencia, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ObservacaoAdvertencia_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), observacaoAdvertencia, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataAdvertencia_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), dataAdvertencia, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ResponsavelLegal_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), responsavelLegal, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalDebitoInicial_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalDebitoInicial, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("PercentualJuroDebito_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), percentualJuroDebito, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorJuroDebito_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), valorJuroDebito, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("AcrescimoDebito_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), acrescimoDebito, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DescontoDebito_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), descontoDebito, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorEntradaDebito_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), valorEntradaDebito, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParceladoDebito_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaDebitoString, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorTotalDebitoFinal_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalDebitoFinal, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("JustificativaDebito_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), justificativaDebito, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("AnoConclusaoGraduacao_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), anoConclusaoGraduacao, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("AnoConclusaoEnsinoMedio_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), anoConclusaoEnsinoMedio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoMaeNome_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoMaeNomeAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoMaeCPF_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoMaeCPFAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoMaeCEP_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoMaeCEPAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoMaeRG_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoMaeRGAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoMaeEndereco_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoMaeEnderecoAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoMaeNumero_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoMaeNumeroAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoMaeSetor_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoMaeSetorAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoMaeComplementoEndereco_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoMaeComplementoEnderecoAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoMaeEstado_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoMaeEstadoAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoMaeCidade_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoMaeCidadeAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoMaeUF_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoMaeUFAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoMaeTelefoneComercial_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoMaeTelefoneComercialAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoMaeTelefoneResidencial_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoMaeTelefoneResidencialAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoMaeTelefoneRecado_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoMaeTelefoneRecadoAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoMaeEmail_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoMaeEmailAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoMaeCelular_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoMaeCelularAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoMaeDataNascimento_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoMaeDataNascimentoAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoMaeNacionalidade_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoMaeNacionalidadeAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoMaeEstadoCivil_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoMaeEstadoCivilAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("AnoConclusaoEnsinoMedio_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), anoConclusaoEnsinoMedio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoMaeOrgaoEmissor_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoMaeOrgaoEmissorAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoPaiNome_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoPaiNomeAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoPaiCPF_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoPaiCPFAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoPaiCEP_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoPaiCEPAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoPaiRG_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoPaiRGAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoPaiEndereco_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoPaiEnderecoAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoPaiNumero_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoPaiNumeroAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoPaiSetor_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoPaiSetorAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoPaiComplementoEndereco_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoPaiComplementoEnderecoAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoPaiEstado_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoPaiEstadoAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoPaiCidade_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoPaiCidadeAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoPaiUF_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoPaiUFAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoPaiTelefoneComercial_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoPaiTelefoneComercialAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoPaiTelefoneResidencial_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoPaiTelefoneResidencialAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoPaiTelefoneRecado_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoPaiTelefoneRecadoAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoPaiEmail_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoPaiEmailAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoPaiCelular_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoPaiCelularAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoPaiDataNascimento_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoPaiDataNascimentoAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoPaiNacionalidade_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoPaiNacionalidadeAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoPaiEstadoCivil_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoPaiEstadoCivilAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("AnoConclusaoEnsinoMedio_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), anoConclusaoEnsinoMedio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("FiliacaoPaiOrgaoEmissor_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), filiacaoPaiOrgaoEmissorAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NomeFiador_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), nomeFiador, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CPFFiador_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), cpfFiador, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CEPFiador_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), cepFiador, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EnderecoFiador_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), enderecoFiador, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("BairroFiador_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), bairroFiador, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NumeroEnderecoFiador_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), numeroEnderecoFiador, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ComplementoEnderecoFiador_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), complementoEnderecoFiador, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CidadeFiador_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), cidadeFiador, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EstadoFiador_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), estadoFiador, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TelefoneFiador_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), telefoneFiador, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CelularFiador_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), celularFiador, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TelefoneComercial_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), telefoneComercialAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EstadoCivilFiador_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), estadoCivilFiador, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("RGFiador_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), rgFiador, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataNascimentoFiador_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), dataNascimentoFiador, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ProfissaoFiador_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), profissaoFiador, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("PaisFiador_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), paisFiador, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParcelaNegociacao_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaNegociacao, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorParcelaNegociacaoExtenso_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaNegociacaoExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("QuantidadeParcelaNegociacao_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), quantidadeParcelaNegociacao, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("QuantidadeParcelaNegociacaoExtenso_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), quantidadeParcelaNegociacaoExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DiaPrimeiroVencimento_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), diaPrimeiroVencimento, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DiaPrimeiroVencimentoExtenso_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), diaPrimeiroVencimentoExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("UltimoCargoAluno_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), ultimoCargoAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EmpresaAluno_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), empresaAluno, textoTag, tamanho);
			}  else if (mar.equalsIgnoreCase("CampoJustificativaNegociacao_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), campoJustificativaNegociacao, textoTag, tamanho);
			}  else if (mar.equalsIgnoreCase("ValorParceladoDebitoExtenso_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), valorParcelaDebitoExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorResidual_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), valorResidual, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ValorResidualExtenso_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), valorResidualExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DiaSegundoVencimento_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), diaSegundoVencimento, textoTag, tamanho);
			}  else if (mar.equalsIgnoreCase("DiaSegundoVencimentoExtenso_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), diaSegundoVencimentoExtenso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NomeParceiroConvenio_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), nomeParceiroConvenioAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CNPJParceiroConvenio_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), cnpjParceiroConvenioAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NomeResponsavelParceiroConvenio_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), nomeResponsavelParceiroConvenioAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("RGResponsavelParceiroConvenio_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), rgResponsavelParceiroConvenioAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CPFResponsavelParceiroConvenio_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), cpfResponsavelParceiroConvenioAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EnderecoResponsavelParceiroConvenio_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), enderecoResponsavelParceiroConvenioAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("BairroResponsavelParceiroConvenio_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), bairroResponsavelParceiroConvenioAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CidadeResponsavelParceiroConvenio_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), cidadeResponsavelParceiroConvenioAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CEPResponsavelParceiroConvenio_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), cepResponsavelParceiroConvenioAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TelefoneResponsavelParceiroConvenio_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), telefoneResponsavelParceiroConvenioAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EmailResponsavelParceiroConvenio_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), emailResponsavelParceiroConvenioAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ListaPlanoEnsinoDisciplina_Aluno")) {
				substituirTagComLista(texto, marcador.getTag(), impressaoContratoVO.getListaPlanoEnsino(), textoTag);
			}  else if (mar.equalsIgnoreCase("ObservacaoComplementar_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), impressaoContratoVO.getTermoReconhecimentoDividaRelVO().getObservacaoComplementar(), textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TituloMonografia_Aluno")) {
				 texto = substituirTag(texto, marcador.getTag(), matriculaVO.getTituloMonografia(), textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NotaMonografia_Aluno")) {
				 texto = substituirTag(texto, marcador.getTag(), notaMonografiaAlteracoesCadastrais, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TipoTrabalhoConclusaoCurso_Aluno")) {
				 texto = substituirTag(texto, marcador.getTag(), matriculaVO.getTipoTrabalhoConclusaoCurso_Apresentar(), textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("OrientadorMonografia_Aluno")) {
				 texto = substituirTag(texto, marcador.getTag(), matriculaVO.getOrientadorMonografia(), textoTag, tamanho);
			}else if (mar.equalsIgnoreCase("ListaDisciplinasHistoricoPeriodoLetivoSituacao_Disciplina")) {
				substituirTagComLista(texto, marcador.getTag(), impressaoContratoVO.getListaDisciplinasHistoricoPeriodoLetivoSituacao(), textoTag);
			}  else if (mar.equalsIgnoreCase("NumeroRegistroDiplomaPrimeiraVia_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), numeroRegistroDiplomaPrimeiraVia, textoTag, tamanho);
			}  else if (mar.equalsIgnoreCase("NumeroProcessoDiplomaPrimeiraVia_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), numeroProcessoDiplomaPrimeiraVia, textoTag, tamanho);
			}  else if (mar.equalsIgnoreCase("DataExpedicaoDiplomaPrimeiraVia_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), dataExpedicaoDiplomaPrimeiraVia, textoTag, tamanho);
			}  else if (mar.equalsIgnoreCase("AssinaturaPrimeiroFuncionarioDiplomaPrimeiraVia_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), assinaturaPrimeiroFuncionarioDiplomaPrimeiraVia, textoTag, tamanho);
			}  else if (mar.equalsIgnoreCase("CargoPrimeiroFuncionarioDiplomaPrimeiraVia_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), cargoPrimeiroFuncionarioDiplomaPrimeiraVia, textoTag, tamanho);
			}  else if (mar.equalsIgnoreCase("AssinaturaSegundoFuncionarioDiplomaPrimeiraVia_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), assinaturaSegundoFuncionarioDiplomaPrimeiraVia, textoTag, tamanho);
			}  else if (mar.equalsIgnoreCase("CargoSegundoFuncionarioDiplomaPrimeiraVia_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), cargoSegundoFuncionarioDiplomaPrimeiraVia, textoTag, tamanho);
			}  else if (mar.equalsIgnoreCase("AssinaturaTerceiroFuncionarioDiplomaPrimeiraVia_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), assinaturaTerceiroFuncionarioDiplomaPrimeiraVia, textoTag, tamanho);
			}  else if (mar.equalsIgnoreCase("CargoTerceiroFuncionarioDiplomaPrimeiraVia_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), cargoTerceiroFuncionarioDiplomaPrimeiraVia, textoTag, tamanho);
			}else if (mar.equalsIgnoreCase("ListaDeclaracaoImpostoRenda_Aluno")) {
				substituirTagComLista(texto, marcador.getTag(), impressaoContratoVO.getListaDeclaracaoImpostoRendaRelVO(), textoTag);
			} else if (mar.equalsIgnoreCase("UrlFoto_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), urlFotoAluno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EmailInstitucional_Aluno")) {
				texto = substituirTag(texto, marcador.getTag(), emailPessoaInstitucional, textoTag, tamanho);
			}else if (mar.equalsIgnoreCase("RegistroAcademico_Aluno")) {
			   texto = substituirTag(texto, marcador.getTag(), registroAcademico, textoTag, tamanho);
		    }
			break;
		}
		return texto;
	}

	public String substituirTagProfessor(String texto, ImpressaoContratoVO impressaoContratoVO, MarcadorVO marcador, DadosComerciaisVO dc) throws Exception {
		PessoaVO pessoa = impressaoContratoVO.getProfessor().getPessoa();
		String emBranco = obterStringEmBrancoParaTag(marcador);

		String codigo = pessoa.getCodigo().toString();
		if (codigo == null || codigo.equals("0") || codigo.equals("")) {
			codigo = emBranco;
		}
		String nome = pessoa.getNome();
		if (nome == null || nome.equals("0") || nome.equals("")) {
			nome = emBranco;
		}
		String sexo = pessoa.getSexo_Apresentar();
		if (sexo == null || sexo.equals("0") || sexo.equals("")) {
			sexo = emBranco;
		}
		String naturalidade = pessoa.getNaturalidade().getNome();
		if (naturalidade == null || naturalidade.equals("0") || naturalidade.equals("")) {
			naturalidade = emBranco;
		}
		String estadoNaturalidade = pessoa.getNaturalidade().getEstado().getNome();
		if (estadoNaturalidade == null || estadoNaturalidade.equals("0") || estadoNaturalidade.equals("")) {
			estadoNaturalidade = emBranco;
		}
		String nacionalidade = pessoa.getNacionalidade().getNome();
		if (nacionalidade == null || nacionalidade.equals("0") || nacionalidade.equals("")) {
			nacionalidade = emBranco;
		}
		String email = pessoa.getEmail();
		if (email == null || email.equals("0") || email.equals("")) {
			email = emBranco;
		}
		String email2 = pessoa.getEmail2();
		if (email2 == null || email2.equals("0") || email2.equals("")) {
			email2 = emBranco;
		}
		String estadoCivil = pessoa.getEstadoCivil_Apresentar();
		if (estadoCivil == null || estadoCivil.equals("0") || estadoCivil.equals("")) {
			estadoCivil = emBranco;
		}
		String rg = pessoa.getRG();
		if (rg == null || rg.equals("0") || rg.equals("")) {
			rg = emBranco;
		}
		String orgaoEmissor = pessoa.getOrgaoEmissor();
		if (orgaoEmissor == null || orgaoEmissor.equals("0") || orgaoEmissor.equals("")) {
			orgaoEmissor = emBranco;
		}
		String estadoEmissor = pessoa.getEstadoEmissaoRG_Apresentar();
		if (estadoEmissor == null || estadoEmissor.equals("0") || estadoEmissor.equals("")) {
			estadoEmissor = emBranco;
		}
		String cpf = pessoa.getCPF();
		if (cpf == null || cpf.equals("0") || cpf.equals("")) {
			cpf = emBranco;
		}
		String dataNascimento = pessoa.getDataNasc_Apresentar();
		if (dataNascimento == null || dataNascimento.equals("0") || dataNascimento.equals("")) {
			dataNascimento = emBranco;
		}
		String telefoneRecado = pessoa.getTelefoneRecado();
		if (telefoneRecado == null || telefoneRecado.equals("0") || telefoneRecado.equals("")) {
			telefoneRecado = emBranco;
		}
		String telefoneRes = pessoa.getTelefoneRes();
		if (telefoneRes == null || telefoneRes.equals("0") || telefoneRes.equals("")) {
			telefoneRes = emBranco;
		}
		String comercial_Professor = pessoa.getTelefoneComer();
		if (comercial_Professor == null || comercial_Professor.equals("0") || comercial_Professor.equals("")) {
			comercial_Professor = emBranco;
		}
		String celular = pessoa.getCelular();
		if (celular == null || celular.equals("0") || celular.equals("")) {
			celular = emBranco;
		}
		String cep = pessoa.getCEP();
		if (cep == null || cep.equals("0") || cep.equals("")) {
			cep = emBranco;
		}
		String complemento = pessoa.getComplemento();
		if (complemento == null || complemento.equals("0") || complemento.equals("")) {
			complemento = emBranco;
		}
		String numero = pessoa.getNumero();
		if (numero == null || numero.equals("0") || numero.equals("")) {
			numero = emBranco;
		}
		String setor = pessoa.getSetor();
		if (setor == null || setor.equals("0") || setor.equals("")) {
			setor = emBranco;
		}
		String endereco = pessoa.getEndereco();
		if (endereco == null || endereco.equals("0") || endereco.equals("")) {
			endereco = emBranco;
		}
		String complementoendereco = pessoa.getComplemento();
		if (complementoendereco == null || complementoendereco.equals("0") || complementoendereco.equals("")) {
			complementoendereco = emBranco;
		}
		String cidade = pessoa.getCidade().getNome();
		if (cidade == null || cidade.equals("0") || cidade.equals("")) {
			cidade = emBranco;
		}
		String estado = pessoa.getCidade().getEstado().getSigla();
		if (estado == null || estado.equals("0") || estado.equals("")) {
			estado = emBranco;
		}
		String nomePai = pessoa.getFiliacaoVO_Tipo("PA").getNome();
		if (nomePai == null || nomePai.equals("0") || nomePai.equals("")) {
			nomePai = emBranco;
		}
		String nomeMae = pessoa.getFiliacaoVO_Tipo("MA").getNome();
		if (nomeMae == null || nomeMae.equals("0") || nomeMae.equals("")) {
			nomeMae = emBranco;
		}

		String nomeEmpresa = "";
		String enderecoEmpresa = "";
		String complementoEmpresa = "";
		String cepEmpresa = "";
		String cidadeEmpresa = "";
		String estadoCidadeEmpresa = "";
		String telefoneComercialEmpresa = "";
		String bairroEmpresa = "";
		if (dc != null) {
			nomeEmpresa = dc.getNomeEmpresa();
			if (nomeEmpresa == null || nomeEmpresa.equals("0") || nomeEmpresa.equals("")) {
				nomeEmpresa = emBranco;
			}
			enderecoEmpresa = dc.getEnderecoEmpresa();
			if (enderecoEmpresa == null || enderecoEmpresa.equals("0") || enderecoEmpresa.equals("")) {
				enderecoEmpresa = emBranco;
			}
			complementoEmpresa = dc.getComplementoEmpresa();
			if (complementoEmpresa == null || complementoEmpresa.equals("0") || complementoEmpresa.equals("")) {
				complementoEmpresa = emBranco;
			}
			cepEmpresa = dc.getCepEmpresa();
			if (cepEmpresa == null || cepEmpresa.equals("0") || cepEmpresa.equals("")) {
				cepEmpresa = emBranco;
			}
			cidadeEmpresa = dc.getCidadeEmpresa().getNome();
			if (cidadeEmpresa == null || cidadeEmpresa.equals("0") || cidadeEmpresa.equals("")) {
				cidadeEmpresa = emBranco;
			}
			estadoCidadeEmpresa = dc.getCidadeEmpresa().getEstado().getSigla();
			if (estadoCidadeEmpresa == null || estadoCidadeEmpresa.equals("0") || estadoCidadeEmpresa.equals("")) {
				estadoCidadeEmpresa = emBranco;
			}
			telefoneComercialEmpresa = dc.getTelefoneComer();
			if (telefoneComercialEmpresa == null || telefoneComercialEmpresa.equals("0") || telefoneComercialEmpresa.equals("")) {
				telefoneComercialEmpresa = emBranco;
			}
			// String bairroEmpresa = pessoa.getBairroEmpresa();
			bairroEmpresa = dc.getSetorEmpresa();
			if (bairroEmpresa == null || bairroEmpresa.equals("0") || bairroEmpresa.equals("")) {
				bairroEmpresa = emBranco;
			}
		}

		boolean possuiResponsavel = false;
		String nomeResponsavelFinanceiro = "";
		String cpfResponsavelFinanceiro = "";
		String rgResponsavelFinanceiro = "";
		String telefoneComerResponsavelFinanceiro = "";
		String telefoneResResponsavelFinanceiro = "";
		String telefoneRecadoResponsavelFinanceiro = "";
		String celularResponsavelFinanceiro = "";
		String emailResponsavelFinanceiro = "";
		Iterator listaFiliacao = pessoa.getFiliacaoVOs().iterator();
		while (listaFiliacao.hasNext()) {
			FiliacaoVO filiacao = (FiliacaoVO) listaFiliacao.next();
			if (filiacao.getResponsavelFinanceiro()) {
				possuiResponsavel = true;
				nomeResponsavelFinanceiro = filiacao.getNome();
				if (nomeResponsavelFinanceiro == null || nomeResponsavelFinanceiro.equals("0") || nomeResponsavelFinanceiro.equals("")) {
					nomeResponsavelFinanceiro = emBranco;
				}
				cpfResponsavelFinanceiro = filiacao.getCPF();
				if (cpfResponsavelFinanceiro == null || cpfResponsavelFinanceiro.equals("0") || cpfResponsavelFinanceiro.equals("")) {
					cpfResponsavelFinanceiro = emBranco;
				}
				rgResponsavelFinanceiro = filiacao.getRG();
				if (rgResponsavelFinanceiro == null || rgResponsavelFinanceiro.equals("0") || rgResponsavelFinanceiro.equals("")) {
					rgResponsavelFinanceiro = emBranco;
				}
				telefoneComerResponsavelFinanceiro = filiacao.getTelefoneComer();
				if (telefoneComerResponsavelFinanceiro == null || telefoneComerResponsavelFinanceiro.equals("0") || telefoneComerResponsavelFinanceiro.equals("")) {
					telefoneComerResponsavelFinanceiro = emBranco;
				}
				telefoneResResponsavelFinanceiro = filiacao.getTelefoneRes();
				if (telefoneResResponsavelFinanceiro == null || telefoneResResponsavelFinanceiro.equals("0") || telefoneResResponsavelFinanceiro.equals("")) {
					telefoneResResponsavelFinanceiro = emBranco;
				}
				telefoneRecadoResponsavelFinanceiro = filiacao.getTelefoneRecado();
				if (telefoneRecadoResponsavelFinanceiro == null || telefoneRecadoResponsavelFinanceiro.equals("0") || telefoneRecadoResponsavelFinanceiro.equals("")) {
					telefoneRecadoResponsavelFinanceiro = emBranco;
				}
				celularResponsavelFinanceiro = filiacao.getCelular();
				if (celularResponsavelFinanceiro == null || celularResponsavelFinanceiro.equals("0") || celularResponsavelFinanceiro.equals("")) {
					celularResponsavelFinanceiro = emBranco;
				}
				emailResponsavelFinanceiro = filiacao.getEmail();
				if (emailResponsavelFinanceiro == null || emailResponsavelFinanceiro.equals("0") || emailResponsavelFinanceiro.equals("")) {
					emailResponsavelFinanceiro = emBranco;
				}

			}
		}
		if (!possuiResponsavel) {
			nomeResponsavelFinanceiro = pessoa.getNome();
			cpfResponsavelFinanceiro = pessoa.getCPF();
			rgResponsavelFinanceiro = pessoa.getRG();
			telefoneComerResponsavelFinanceiro = pessoa.getTelefoneComer();
			telefoneResResponsavelFinanceiro = pessoa.getTelefoneRes();
			telefoneRecadoResponsavelFinanceiro = pessoa.getTelefoneRecado();
			celularResponsavelFinanceiro = pessoa.getCelular();
			emailResponsavelFinanceiro = pessoa.getEmail();
		}

		// Sempre chegará com a lista preenchida com apenas 1 item. Haja vista
		// que no contrato não apresentará a lista, sendo assim
		// é feito uma logica para que quando mande realizar a impressão os
		// sitema deixe apenas um Obejto na lista.
		String cursoFormaca = "";
		String escolaridadeFormaca = "";
		String instituicaoFormaca = "";
		String cidadeFormaca = "";
		String areaConhecimentoFormaca = "";
		String situacaoFormaca = "";
		String dataInicioFormaca = "";
		String dataFinalFormaca = "";
		Iterator listaFormacao = pessoa.getFormacaoAcademicaVOs().iterator();
		while (listaFormacao.hasNext()) {
			FormacaoAcademicaVO formacao = (FormacaoAcademicaVO) listaFormacao.next();
			cursoFormaca = formacao.getCurso();
			if (cursoFormaca == null || cursoFormaca.equals("0") || cursoFormaca.equals("")) {
				cursoFormaca = emBranco;
			}
			escolaridadeFormaca = formacao.getEscolaridade_Apresentar();
			if (escolaridadeFormaca == null || escolaridadeFormaca.equals("0") || escolaridadeFormaca.equals("")) {
				escolaridadeFormaca = emBranco;
			}
			instituicaoFormaca = formacao.getInstituicao();
			if (instituicaoFormaca == null || instituicaoFormaca.equals("0") || instituicaoFormaca.equals("")) {
				instituicaoFormaca = emBranco;
			}
			cidadeFormaca = formacao.getCidade().getNome();
			if (cidadeFormaca == null || cidadeFormaca.equals("0") || cidadeFormaca.equals("")) {
				cidadeFormaca = emBranco;
			}
			areaConhecimentoFormaca = formacao.getAreaConhecimento().getNome();
			if (areaConhecimentoFormaca == null || areaConhecimentoFormaca.equals("0") || areaConhecimentoFormaca.equals("")) {
				areaConhecimentoFormaca = emBranco;
			}
			situacaoFormaca = formacao.getSituacao_Apresentar();
			if (situacaoFormaca == null || situacaoFormaca.equals("0") || situacaoFormaca.equals("")) {
				situacaoFormaca = emBranco;
			}
			dataInicioFormaca = formacao.getDataInicio_ApresentarAno2Digitos();
			if (dataInicioFormaca == null || dataInicioFormaca.equals("0") || dataInicioFormaca.equals("")) {
				dataInicioFormaca = emBranco;
			}
			dataFinalFormaca = formacao.getDataFim_ApresentarAno2Digitos();
			if (dataFinalFormaca == null || dataFinalFormaca.equals("0") || dataFinalFormaca.equals("")) {
				dataFinalFormaca = emBranco;
			}
		}
		while (texto.indexOf(marcador.getTag()) != -1) {
			int tamanho = obterTamanhoTag(marcador.getTag());
			String mar = obterTagSemTextoSemTamanho(marcador.getTag());
			String textoTag = obterTextoTag(marcador.getTag());

			if (mar.equalsIgnoreCase("Codigo_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), codigo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Nome_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), nome, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Endereco_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), endereco, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ComplementoEnd_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), complementoendereco, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NumeroEnd_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), numero, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Cidade_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), cidade, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Estado_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), estado, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Bairro_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), setor, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Sexo_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), sexo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Naturalidade_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), naturalidade + "/" + estadoNaturalidade, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Nacionalidade_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), nacionalidade, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EstadoCivil_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), estadoCivil, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Rg_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), rg, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("OrgaoEmissor_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), orgaoEmissor, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EstadoEmissor_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), estadoEmissor, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Cpf_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), cpf, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataNasc_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), dataNascimento, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Telefone_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), telefoneRes, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Celular_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), celular, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Comercial_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), comercial_Professor, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CEP_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), cep, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Email_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), email, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Email2_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), email2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NomePai_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), nomePai, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NomeMae_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), nomeMae, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CursoFormacao_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), cursoFormaca, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EscolaridadeFormacao_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), escolaridadeFormaca, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("InstituicaoFormacao_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), instituicaoFormaca, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CidadeFormacao_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), cidadeFormaca, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("AreaConhecimentoFormacao_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), areaConhecimentoFormaca, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("SituacaoFormacao_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), situacaoFormaca, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataInicioFormacao_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), dataInicioFormaca, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataFinalFormacao_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), dataFinalFormaca, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NomeEmpresa_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), nomeEmpresa, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EnderecoEmpresa_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), enderecoEmpresa, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ComplementoEmpresa_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), complementoEmpresa, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("BairroEmpresa_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), bairroEmpresa, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CepEmpresa_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), cepEmpresa, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CidadeEmpresa_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), cidadeEmpresa + "/" + estadoCidadeEmpresa, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TelefoneEmpresa_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), telefoneComercialEmpresa, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NomeResponsavelFinanceiro_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), nomeResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CPFResponsavelFinanceiro_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), cpfResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("RGResponsavelFinanceiro_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), rgResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TelefoneComerResponsavelFinanceiro_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), telefoneComerResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TelefoneResResponsavelFinanceiro_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), telefoneResResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CelularResponsavelFinanceiro_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), celularResponsavelFinanceiro, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EmailResponsavelFinanceiro_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), emailResponsavelFinanceiro, textoTag, tamanho);
			}
			break;
		}
		return texto;
	}

	public String substituirTagInscricao(String texto, ImpressaoContratoVO impressaoContratoVO, MarcadorVO marcador, DadosComerciaisVO dc) throws Exception {
		PessoaVO pessoa = impressaoContratoVO.getProfessor().getPessoa();
		String emBranco = obterStringEmBrancoParaTag(marcador);
		
		String codigo = pessoa.getCodigo().toString();
		if (codigo == null || codigo.equals("0") || codigo.equals("")) {
			codigo = emBranco;
		}
		String nome = pessoa.getNome();
		if (nome == null || nome.equals("0") || nome.equals("")) {
			nome = emBranco;
		}
		String cidade = pessoa.getCidade().getNome();
		if (cidade == null || cidade.equals("0") || cidade.equals("")) {
			cidade = emBranco;
		}
		String estado = pessoa.getCidade().getEstado().getSigla();
		if (estado == null || estado.equals("0") || estado.equals("")) {
			estado = emBranco;
		}
		String nomePai = pessoa.getFiliacaoVO_Tipo("PA").getNome();
		if (nomePai == null || nomePai.equals("0") || nomePai.equals("")) {
			nomePai = emBranco;
		}
		
		while (texto.indexOf(marcador.getTag()) != -1) {
			int tamanho = obterTamanhoTag(marcador.getTag());
			String mar = obterTagSemTextoSemTamanho(marcador.getTag());
			String textoTag = obterTextoTag(marcador.getTag());
			
			if (mar.equalsIgnoreCase("Codigo_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), codigo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Nome_Professor")) {
				texto = substituirTag(texto, marcador.getTag(), nome, textoTag, tamanho);
			}
			break;
		}
		return texto;
	}

	public String substituirTagCandidato(String texto, MarcadorVO marcador, InscricaoVO inscricao) throws Exception {
		PessoaVO pessoa = inscricao.getCandidato();
		String emBranco = obterStringEmBrancoParaTag(marcador);
		
		String codigo = pessoa.getCodigo().toString();
		if (codigo == null || codigo.equals("0") || codigo.equals("")) {
			codigo = emBranco;
		}
		String nome = pessoa.getNome();
		if (nome == null || nome.equals("0") || nome.equals("")) {
			nome = emBranco;
		}
		String sexo = pessoa.getSexo_Apresentar();
		if (sexo == null || sexo.equals("0") || sexo.equals("")) {
			sexo = emBranco;
		}
		String preposicaoAouO = "";
		if (pessoa.getSexo().equals("F")) {
			preposicaoAouO = "a";
		} else {
			preposicaoAouO = "o";
		}
		if (preposicaoAouO == null || preposicaoAouO.equals("")) {
			preposicaoAouO = emBranco;
		}
		String naturalidade = pessoa.getNaturalidade().getNome() + " - " + pessoa.getNaturalidade().getEstado().getSigla();
		if (naturalidade == null || naturalidade.equals("0") || naturalidade.equals("")) {
			naturalidade = emBranco;
		}
		String naturalidadeEstado = pessoa.getNaturalidade().getEstado().getNome();
		if (naturalidadeEstado == null || naturalidadeEstado.equals("")) {
			naturalidadeEstado = emBranco;
		}
		String estadoNaturalidade = pessoa.getNaturalidade().getEstado().getSigla();
		if (estadoNaturalidade == null || estadoNaturalidade.equals("0") || estadoNaturalidade.equals("")) {
			estadoNaturalidade = emBranco;
		}
		String nacionalidade = pessoa.getNacionalidade().getNacionalidade();
		if (nacionalidade == null || nacionalidade.equals("0") || nacionalidade.equals("")) {
			nacionalidade = emBranco;
		}
		String email = pessoa.getEmail();
		if (email == null || email.equals("0") || email.equals("")) {
			email = emBranco;
		}
		String email2 = pessoa.getEmail2();
		if (email2 == null || email2.equals("0") || email2.equals("")) {
			email2 = emBranco;
		}
		String estadoCivil = pessoa.getEstadoCivil_Apresentar();
		if (estadoCivil == null || estadoCivil.equals("0") || estadoCivil.equals("")) {
			estadoCivil = emBranco;
		}
		String rg = pessoa.getRG();
		if (rg == null || rg.equals("0") || rg.equals("")) {
			rg = emBranco;
		}
		String orgaoEmissor = pessoa.getOrgaoEmissor();
		if (orgaoEmissor == null || orgaoEmissor.equals("0") || orgaoEmissor.equals("")) {
			orgaoEmissor = emBranco;
		}
		String estadoEmissor = pessoa.getEstadoEmissaoRG_Apresentar();
		if (estadoEmissor == null || estadoEmissor.equals("0") || estadoEmissor.equals("")) {
			estadoEmissor = emBranco;
		}
		String dataEmissaoRG = Uteis.getDataAno4Digitos(pessoa.getDataEmissaoRG());
		if (dataEmissaoRG == null || dataEmissaoRG.equals("0") || dataEmissaoRG.equals("")) {
			dataEmissaoRG = emBranco;
		}
		String cpf = pessoa.getCPF();
		if (cpf == null || cpf.equals("0") || cpf.equals("")) {
			cpf = emBranco;
		}
		String idade = Uteis.calcularIdadePessoa(new Date(), pessoa.getDataNasc()).toString();
		if (idade == null || idade.equals("0") || idade.equals("")) {
			idade = emBranco;
		}
		String dataNascimento = pessoa.getDataNasc_Apresentar();
		if (dataNascimento == null || dataNascimento.equals("0") || dataNascimento.equals("")) {
			dataNascimento = emBranco;
		}
		String dataNascimentoExtenso = Uteis.getDataCidadeDiaMesPorExtensoEAno("", pessoa.getDataNasc(), true);
		if (dataNascimentoExtenso == null || dataNascimentoExtenso.equals("0") || dataNascimentoExtenso.equals("")) {
			dataNascimentoExtenso = emBranco;
		}
		String dataNascimentoExtenso2 = Uteis.getDataCidadeDiaMesPorExtensoEAno("", pessoa.getDataNasc(), true);
		if (dataNascimentoExtenso == null || dataNascimentoExtenso.equals("0") || dataNascimentoExtenso.equals("")) {
			dataNascimentoExtenso = emBranco;
		}
		String diaNascimento = Uteis.getData(pessoa.getDataNasc(), "dd");
		if (diaNascimento == null || diaNascimento.equals("0") || diaNascimento.equals("")) {
			diaNascimento = emBranco;
		}
		String mesNascimento = Uteis.getData(pessoa.getDataNasc(), "MMMMM");
		if (mesNascimento == null || mesNascimento.equals("0") || mesNascimento.equals("")) {
			mesNascimento = emBranco;
		}
		String anoNascimento = Uteis.getData(pessoa.getDataNasc(), "yyyy");
		if (anoNascimento == null || anoNascimento.equals("0") || anoNascimento.equals("")) {
			anoNascimento = emBranco;
		}
		String telefoneComer = pessoa.getTelefoneComer();
		if (telefoneComer == null || telefoneComer.equals("0") || telefoneComer.equals("")) {
			telefoneComer = emBranco;
		}
		String telefoneRecado = pessoa.getTelefoneRecado();
		if (telefoneRecado == null || telefoneRecado.equals("0") || telefoneRecado.equals("")) {
			telefoneRecado = emBranco;
		}
		String telefoneRes = pessoa.getTelefoneRes();
		if (telefoneRes == null || telefoneRes.equals("0") || telefoneRes.equals("")) {
			telefoneRes = emBranco;
		}
		String celular = pessoa.getCelular();
		if (celular == null || celular.equals("0") || celular.equals("")) {
			celular = emBranco;
		}
		String cep = pessoa.getCEP();
		if (cep == null || cep.equals("0") || cep.equals("")) {
			cep = emBranco;
		}
		String complemento = pessoa.getComplemento();
		if (complemento == null || complemento.equals("0") || complemento.equals("")) {
			complemento = emBranco;
		}
		String numero = pessoa.getNumero();
		if (numero == null || numero.equals("0") || numero.equals("")) {
			numero = emBranco;
		}
		String setor = pessoa.getSetor();
		if (setor == null || setor.equals("0") || setor.equals("")) {
			setor = emBranco;
		}
		String endereco = pessoa.getEndereco();
		if (endereco == null || endereco.equals("0") || endereco.equals("")) {
			endereco = emBranco;
		}
		String cidade = pessoa.getCidade().getNome();
		if (cidade == null || cidade.equals("0") || cidade.equals("")) {
			cidade = emBranco;
		}
		String estado = pessoa.getCidade().getEstado().getSigla();
		if (estado == null || estado.equals("0") || estado.equals("")) {
			estado = emBranco;
		}
		String nomePai = pessoa.getFiliacaoVO_Tipo("PA").getNome();
		if (nomePai == null || nomePai.equals("0") || nomePai.equals("")) {
			nomePai = emBranco;
		}
		String nomeMae = pessoa.getFiliacaoVO_Tipo("MA").getNome();
		if (nomeMae == null || nomeMae.equals("0") || nomeMae.equals("")) {
			nomeMae = emBranco;
		}
		
		while (texto.indexOf(marcador.getTag()) != -1) {
			int tamanho = obterTamanhoTag(marcador.getTag());
			String mar = obterTagSemTextoSemTamanho(marcador.getTag());
			String textoTag = obterTextoTag(marcador.getTag());
			
			if (mar.equalsIgnoreCase("Nome_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), nome, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Endereco_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), endereco, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ComplementoEnd_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), complemento, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NumeroEnd_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), numero, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Cidade_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), cidade, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Estado_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), estado, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Bairro_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), setor, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Sexo_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), sexo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Naturalidade_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), naturalidade, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Nacionalidade_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), nacionalidade, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EstadoCivil_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), estadoCivil, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Rg_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), rg, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("OrgaoEmissor_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), orgaoEmissor, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataEmissaoRG_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), dataEmissaoRG, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("EstadoEmissor_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), estadoEmissor, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Cpf_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), cpf, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataNasc_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), dataNascimento, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Idade_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), idade, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Codigo_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), codigo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Telefone_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), telefoneRes, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Celular_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), celular, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Comercial_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), telefoneComer, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CEP_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), cep, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Email_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), email, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Email2_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), email2, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NomePai_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), nomePai, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NomeMae_Candidato")) {
				texto = substituirTag(texto, marcador.getTag(), nomeMae, textoTag, tamanho);
			}
			break;
		}
		return texto;
	}

	public String substituirTagProcessoSeletivo(String texto, MarcadorVO marcador, InscricaoVO inscricao) throws Exception {
		PessoaVO pessoa = inscricao.getCandidato();
		String emBranco = obterStringEmBrancoParaTag(marcador);
		
		String nome = inscricao.getProcSeletivo().getDescricao();
		if (nome == null || nome.equals("0") || nome.equals("")) {
			nome = emBranco;
		}
		
		while (texto.indexOf(marcador.getTag()) != -1) {
			int tamanho = obterTamanhoTag(marcador.getTag());
			String mar = obterTagSemTextoSemTamanho(marcador.getTag());
			String textoTag = obterTextoTag(marcador.getTag());
			
			if (mar.equalsIgnoreCase("Nome_ProcessoSeletivo")) {
				texto = substituirTag(texto, marcador.getTag(), nome, textoTag, tamanho);
			}
			break;
		}
		return texto;
	}

	public String substituirTagListaProcessoSeletivo(String texto, MarcadorVO marcador, List lista, TipoRelatorioEstatisticoProcessoSeletivoEnum tipoRelatorio) throws Exception {
		String listaFinal = "";
		boolean primeiraVez = true;
		boolean existeDiscOptativa = false;
		String cursoAnterior = "";
		String turnoAnterior = "";
		Iterator mat = lista.iterator();
		while (mat.hasNext()) {
			if (tipoRelatorio.equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_APROVADOS) || 
					tipoRelatorio.equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_AUSENTES) || 
					tipoRelatorio.equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_MATRICULADOS) ||
					tipoRelatorio.equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_REPROVADOS) ||
					tipoRelatorio.equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_FREQUENCIA) ||
					tipoRelatorio.equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_PRESENTES) ||
					tipoRelatorio.equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.DADOS_CANDIDATOS) ||
					tipoRelatorio.equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.LEMBRETE_DATA_PROVA) ||
					tipoRelatorio.equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_NAO_MATRICULADOS)) {				
				if (primeiraVez) {
					listaFinal = "<table border=1 cellspacing=0 cellpadding=1 bordercolor=\"000000\"><tr><th>Número Insc.</th><th>Candidato</th><th>Classificação</th><th>Situação</th><th>Nr Chamada</th></tr>";
				}
				InscricaoVO inscricao = (InscricaoVO) mat.next();
				listaFinal = listaFinal + " <tr><td>" + inscricao.getCodigo().toString() + " </td><td> " + inscricao.getCandidato().getNome() + " </td><td> " + inscricao.getClassificacao() + " </td><td> " + inscricao.getSituacao_Apresentar() + " </td><td> " + inscricao.getChamada() + " </td></tr>";				
			} else if (tipoRelatorio.equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_CLASSIFICADOS) || tipoRelatorio.equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_CANDIDATOS_CHAMADOS)) {
				ChamadaCandidatoAprovadoRelVO ch = (ChamadaCandidatoAprovadoRelVO) mat.next();
				
				if (listaFinal.isEmpty()) {
					listaFinal = "<table border=1 cellspacing=0 cellpadding=1 bordercolor=\"000000\">";
				}
				
				if (!ch.getCurso().equals(cursoAnterior) || !ch.getTurno().equals(turnoAnterior)) {
						listaFinal = listaFinal + "<tr style=\"background-color: #dddddd;\"><th style=\"text-align: left; border: none; font-weight: bold;\">Curso</th><td style=\"text-align: center; border: none;\">" + ch.getCurso() +  "</td><th style=\"text-align: left; border: none; font-weight: bold;\">Turno</th><td colspan=\"2\" style=\"text-align: center; border: none;\">" + ch.getTurno() + "</td></tr>";
						listaFinal = listaFinal + "<tr><th>Nº Insc.</th><th>Candidato</th><th>Classificação</th><th>Nr Chamada</th></tr>";
					}
				listaFinal = listaFinal + " <tr><td>" + ch.getNumeroInscricao().toString() + " </td><td> " + ch.getNomeCandidato() + " </td><td> " + ch.getClassificacao() + " </td><td> " + ch.getChamada() + " </td></tr>";
				cursoAnterior = ch.getCurso();
				turnoAnterior = ch.getTurno();
			} else if (tipoRelatorio.equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.INSCRITOS_BAIRRO) ||
				tipoRelatorio.equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_PRESENTE_AUSENTES_CURSO_TURNO_DATA) ||
				tipoRelatorio.equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_MURAL_CANDIDATO) ||
				tipoRelatorio.equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.INSCRITOS_CURSO)) {
				if (primeiraVez) {
					listaFinal = "<tr><th>Número Insc.</th><th>Candidato</th><th>Classificação</th><th>Situação</th><th>Nr Chamada</th></tr>";
				}
				EstatisticaProcessoSeletivoVO es = (EstatisticaProcessoSeletivoVO) mat.next();
				listaFinal = listaFinal + " <tr><td>" + es.getNumeroInscricao().toString() + " </td><td> " + es.getNomeCandidato() + " </td><td>  </td><td>  </td><td>  </td></tr>";				
			} 
			primeiraVez = false;
			existeDiscOptativa = true;
		}
		if (existeDiscOptativa) {
			listaFinal = listaFinal + "</table>";
		}

		
		while (texto.indexOf(marcador.getTag()) != -1) {
			int tamanho = obterTamanhoTag(marcador.getTag());
			String mar = obterTagSemTextoSemTamanho(marcador.getTag());
			String textoTag = obterTextoTag(marcador.getTag());
			
			if (mar.equalsIgnoreCase("Lista_Insc_NomeCand_Classif_Sit_NrCham_ListaProcessoSeletivo") || mar.equalsIgnoreCase("Lista_Insc_NomeCand_ListaProcessoSeletivo") || mar.equalsIgnoreCase("Lista_Insc_NomeCand_LocalProva_ListaProcessoSeletivo")) {
				texto = substituirTag(texto, marcador.getTag(), listaFinal, textoTag, tamanho);
			}
			
			break;
		}
		return texto;
	}

	public String substituirTagResultadoProcessoSeletivo(String texto, MarcadorVO marcador, InscricaoVO inscricao) throws Exception {
		PessoaVO pessoa = inscricao.getCandidato();
		String emBranco = obterStringEmBrancoParaTag(marcador);
		
		String situacao = inscricao.getResultadoProcessoSeletivoVO().getSituacaoAprovacaoGeralProcessoSeletivo();
		if (situacao == null || situacao.equals("0") || situacao.equals("")) {
			situacao = emBranco;
		}
		
		while (texto.indexOf(marcador.getTag()) != -1) {
			int tamanho = obterTamanhoTag(marcador.getTag());
			String mar = obterTagSemTextoSemTamanho(marcador.getTag());
			String textoTag = obterTextoTag(marcador.getTag());
			
			if (mar.equalsIgnoreCase("Situacao_ResultadoProcessoSeletivo")) {
				texto = substituirTag(texto, marcador.getTag(), situacao, textoTag, tamanho);
			}
			break;
		}
		return texto;
	}

	public String substituirTagInscricao(String texto, MarcadorVO marcador, InscricaoVO inscricao) throws Exception {
		PessoaVO pessoa = inscricao.getCandidato();
		String emBranco = obterStringEmBrancoParaTag(marcador);
		
		String numero = inscricao.getCodigo().toString();
		if (numero == null || numero.equals("0") || numero.equals("")) {
			numero = emBranco;
		}
		String unidadeEnsino = inscricao.getUnidadeEnsino().getNome();
		if (unidadeEnsino == null || unidadeEnsino.equals("0") || unidadeEnsino.equals("")) {
			unidadeEnsino = emBranco;
		}
		String curso = inscricao.getCursoOpcao1().getCurso().getNome();
		if (curso == null || curso.equals("0") || curso.equals("")) {
			curso = emBranco;
		}
		String turno = inscricao.getCursoOpcao1().getTurno().getNome();
		if (turno == null || turno.equals("0") || turno.equals("")) {
			turno = emBranco;
		}
		String situacao = inscricao.getSituacao_Apresentar();
		if (situacao == null || situacao.equals("0") || situacao.equals("")) {
			situacao = emBranco;
		}
		String situacaoInscricao = inscricao.getSituacaoInscricao_Apresentar();
		if (situacaoInscricao == null || situacaoInscricao.equals("0") || situacaoInscricao.equals("")) {
			situacaoInscricao = emBranco;
		}
		String classificacao = inscricao.getClassificacao().toString();
		if (classificacao == null || classificacao.equals("0") || classificacao.equals("")) {
			classificacao = emBranco;
		}
		String chamada = inscricao.getChamada().toString();
		if (chamada == null || chamada.equals("0") || chamada.equals("")) {
			chamada = emBranco;
		}
		String dataProva = inscricao.getItemProcessoSeletivoDataProva().getDataProva_Apresentar();
		if (dataProva == null || dataProva.equals("0") || dataProva.equals("")) {
			dataProva = emBranco;
		}
		
		while (texto.indexOf(marcador.getTag()) != -1) {
			int tamanho = obterTamanhoTag(marcador.getTag());
			String mar = obterTagSemTextoSemTamanho(marcador.getTag());
			String textoTag = obterTextoTag(marcador.getTag());
			
			if (mar.equalsIgnoreCase("Numero_Inscricao")) {
				texto = substituirTag(texto, marcador.getTag(), numero, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("UnidadeEnsino_Inscricao")) {
				texto = substituirTag(texto, marcador.getTag(), unidadeEnsino, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Curso_Inscricao")) {
				texto = substituirTag(texto, marcador.getTag(), curso, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Turno_Inscricao")) {
				texto = substituirTag(texto, marcador.getTag(), turno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Situacao_Inscricao")) {
				texto = substituirTag(texto, marcador.getTag(), situacao, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("SituacaoInscricao_Inscricao")) {
				texto = substituirTag(texto, marcador.getTag(), situacaoInscricao, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Classificacao_Inscricao")) {
				texto = substituirTag(texto, marcador.getTag(), classificacao, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Chamada_Inscricao")) {
				texto = substituirTag(texto, marcador.getTag(), chamada, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataProva_Inscricao")) {
				texto = substituirTag(texto, marcador.getTag(), dataProva, textoTag, tamanho);
			}
			break;
		}
		return texto;
	}

	private String getRegexSubTag(String nomeSubTag) {
		String regexSubTag = Pattern.quote("(") + "[\\d]*" + Pattern.quote(")") + Pattern.quote("{") + "*" + Pattern.quote("}") + nomeSubTag;
		return regexSubTag;
	}

	private String getRegexTag() {
		String regexTag = Pattern.quote("[") + ".*?" + Pattern.quote("]");
		return regexTag;
	}

	public int obterTamanhoTag(String tag) throws Exception {
		int posicaoIni = tag.indexOf("(");
		int posicaoFim = tag.indexOf(")");
		if ((posicaoIni == -1) || (posicaoFim == -1)) {
			return 0;
		}
		String tamanho = tag.substring(posicaoIni + 1, posicaoFim);
		Integer num = (tamanho.equals("") ? 0 : new Integer(tamanho));
		return num.intValue();
	}

	public String obterTextoTag(String tag) {
		int posicaoIni = tag.indexOf("{");
		int posicaoFim = tag.indexOf("}");
		if ((posicaoIni == -1) || (posicaoFim == -1)) {
			return "";
		}

		return tag.substring(posicaoIni + 1, posicaoFim);
	}

	public String obterTagSemTextoSemTamanhoQuandoConcatenadaAOutraTag(String tag) {
		int posicao1 = tag.indexOf(",");
		if (posicao1 != -1) {
			tagGeral = tag.substring(posicao1 + 1);
			return tag.substring(0, posicao1 + 1);
		} else {
			tagGeral = "";
			return tag;
		}
	}

	public String obterTagSemTextoSemTamanho(String tag) {
		int posicao1 = tag.indexOf(")");
		int posicao2 = tag.indexOf("}");
		if (posicao2 != -1) {
			return tag.substring(posicao2 + 1, tag.length() - 1);
		} else if (posicao1 != -1) {
			return tag.substring(posicao1 + 1, tag.length() - 1);
		} else {
			return "";
		}
	}

	public String obterTagSemTextoSemTamanhoTagComposta(String tag) {
		String stringFinal = tag;
		while ((stringFinal.indexOf("(") != -1) && (stringFinal.indexOf("}") != -1)) {
			int posicao1 = stringFinal.indexOf("(");
			int posicao2 = stringFinal.indexOf(")");
			if (posicao1 != -1) {
				stringFinal = stringFinal.substring(0, posicao1) + stringFinal.substring(posicao2 + 1);
			}
			int posicao3 = stringFinal.indexOf("{");
			int posicao4 = stringFinal.indexOf("}");
			if (posicao3 != -1) {
				stringFinal = stringFinal.substring(0, posicao3) + stringFinal.substring(posicao4 + 1);
			}
			if ((posicao1 != -1) && (posicao3 != -1)) {
				stringFinal += "";
			}
		}
		return stringFinal;
	}

	public UsuarioVO getResponsavelDefinicao() {
		if (responsavelDefinicao == null) {
			responsavelDefinicao = new UsuarioVO();
		}
		return responsavelDefinicao;
	}

	public void setResponsavelDefinicao(UsuarioVO responsavelDefinicao) {
		this.responsavelDefinicao = responsavelDefinicao;
	}

	/**
	 * Retorna o objeto da classe <code>Colaborador</code> relacionado com (
	 * <code>PlanoTextoPadrao</code>).
	 */
	public String getTexto() {
		if (texto == null) {
			texto = "";
		}
		return (texto);
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Date getDataDefinicao() {
		if (dataDefinicao == null) {
			dataDefinicao = new Date();
		}
		return (dataDefinicao);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getDataDefinicao_Apresentar() {
		return (Uteis.getData(getDataDefinicao()));
	}

	public void setDataDefinicao(Date dataDefinicao) {
		this.dataDefinicao = dataDefinicao;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return (descricao);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "EM";
		}
		return situacao;
	}

	public Boolean getTextoPadraoPodeSerAtivado() {
		if ((this.getSituacao().equals("EM") || this.getSituacao().equals("IN")) && (this.getNovoObj().equals(false))) {
			return true;
		}
		return false;
	}

	public Boolean getTextoPadraoPodeSerDesativado() {
		if (this.getSituacao().equals("AT")) {
			return true;
		}
		return false;
	}

	public Boolean getTextoPadraoPodeSerAlterado() {
		if (this.getSituacao().equals("EM")) {
			return true;
		}
		return false;
	}

	public String getSituacao_Apresentar() {
		if ((situacao == null) || (situacao.equals("EM"))) {
			situacao = "EM";
			return "Em construção";
		}
		if (situacao.equals("IN")) {
			return "Inativo";
		}
		return "Ativo";
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public List getListaTagUtilizado() {
		if (listaTagUtilizado == null) {
			listaTagUtilizado = new ArrayList();
		}
		return listaTagUtilizado;
	}

	public void setListaTagUtilizado(List listaTagUtilizado) {
		this.listaTagUtilizado = listaTagUtilizado;
	}

	public Boolean getTextoPadraoEstaAtivo() {
		if (this.getSituacao().equals("AT")) {
			return true;
		}
		return false;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		if (tipo == null) {
			tipo = "";
		}
		return tipo;
	}

	public String getTipo_Apresentar() {
		return TipoTextoPadrao.getDescricao(getTipo());
	}

	/**
	 * @param tipo
	 *            the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public String substituirTagsTextoPadraoContratoInclusaoReposicao(MatriculaVO matricula, List listaContasReceber, MatriculaPeriodoVO matriculaPeriodo, PlanoFinanceiroAlunoVO planoFinanceiroAluno, List<PlanoDescontoVO> listaPlanoDesconto, DadosComerciaisVO dc, Integer qtdeParcela, Double valorTotal, UsuarioVO usuario) throws Exception {

		try {
			if (getTexto().length() != 0) {
				char aspas = '"';
				String espaco = "<p style=" + aspas + "text-align: left;" + aspas + ">";
				String htmlBotao = "<form> ";
				// String htmlBotao2 = " <table  style = " + aspas +
				// "width: 640px" + aspas + " cellspacing=" + aspas + "0" +
				// aspas + " cellpadding=" + aspas + "0" + aspas + "> <tr> " +
				// "<td style=" + aspas + "height: 19px" + aspas + " align=" +
				// aspas + "center" + aspas + " >" + "<a  id=" + aspas +
				// "Img_btImprimir" + aspas + " title=" + aspas +
				// "Imprimir Boleto" + aspas + " href=" + aspas +
				// "javascript:window.print();" + aspas + " >" + "<img title=" +
				// aspas + "Imprimir Boleto" + aspas + " src=" + aspas +
				// "./imagens/bt_imprimir.gif" + aspas + " border=" + aspas +
				// "0" + aspas + "/> </a>" + "<a id=" + aspas + "Img_btFechar" +
				// aspas + " title=" + aspas + "Fechar Janela" + aspas +
				// " href=" + aspas + "javascript:closeWindow();" + aspas + " >"
				// + "<img title=" + aspas + "Fechar Janela" + aspas + " src=" +
				// aspas + "./imagens/bt_fechar.gif" + aspas + " border=" +
				// aspas + "0" + aspas + " /> </a>" + "</td></tr></table>" +
				// "<script language=" + aspas + "JavaScript" + aspas + " type="
				// + aspas
				// + "text/javascript" + aspas + ">" +
				// "function closeWindow() {" + "if (parent) {" +
				// "parent.close();" + "} else {" +
				// "window.close();}}</script>";
				String textoSub = getTexto();
				textoSub = textoSub.replace("Untitled document", "CONTRATO DE PRESTAÇÃO DE SERVIÇOS EDUCACIONAIS");
				if (textoSub.contains("<body>")) {
					String tagBodyInicio = "<body>";
					String tagBodyFim = "</body>";
					if (!textoSub.contains(tagBodyInicio)) {
						textoSub = tagBodyInicio + textoSub;
					}
					if (!textoSub.contains(tagBodyFim)) {
						textoSub = textoSub + tagBodyFim;
					}
					int posicaoBody = textoSub.indexOf("<body>");
					String parte1 = textoSub.substring(0, posicaoBody + 6);
					String parte2 = textoSub.substring(posicaoBody + 6);
					textoSub = parte1 + htmlBotao + parte2;

					posicaoBody = textoSub.indexOf("</body>");
					parte1 = textoSub.substring(0, posicaoBody);
					parte2 = textoSub.substring(posicaoBody);
					textoSub = parte1 + espaco + "</form>" + parte2;
				}
				textoSub = varrerListaTagUtilizadoTextoPadraoContratoInclusaoReposicao(textoSub, matricula, listaContasReceber, matriculaPeriodo, planoFinanceiroAluno, listaPlanoDesconto, dc, qtdeParcela, valorTotal, usuario);

				HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
				request.getSession().setAttribute("textoRelatorio", textoSub);
				return textoSub;
			}
		} catch (Exception e) {
			throw e;
		}
		return "";
	}

	public String varrerListaTagUtilizadoTextoPadraoContratoInclusaoReposicao(String texto, MatriculaVO matricula, List listaContasReceber, MatriculaPeriodoVO matriculaPeriodo, PlanoFinanceiroAlunoVO planoFinanceiroAluno, List<PlanoDescontoVO> listaPlanoDesconto, DadosComerciaisVO dc, Integer qtdeParcela, Double valorTotal, UsuarioVO usuario) throws Exception {

		MarcadorVO marcador = new MarcadorVO();
		// Iterator i =
		// planoFinanceiroCurso.getTextoPadraoContratoFiador().getListaTagUtilizado().iterator();
		List lista = montarListaTagUtilizada(texto);
		Iterator i = lista.iterator();
		while (i.hasNext()) {
			TextoPadraoTagVO p = (TextoPadraoTagVO) i.next();
			marcador.setTag(p.getTag());
			texto = verificaQualMetodoParaSubstituicaoInvocar(marcador, texto, null, matricula, listaContasReceber, matriculaPeriodo, planoFinanceiroAluno, listaPlanoDesconto, dc, qtdeParcela, valorTotal, usuario);

		}
		return texto;
	}

	public String substituirTagInclusaoReposicao(String texto, Integer qtdeParcela, Double valorTotal, MarcadorVO marcador) throws Exception {
		if (qtdeParcela == null) {
			qtdeParcela = 0;
		}
		if (valorTotal == null) {
			valorTotal = 0.0;
		}
		String emBranco = obterStringEmBrancoParaTag(marcador);
		String parcela = qtdeParcela.toString();
		if (parcela == null || parcela.equals("0") || parcela.equals("")) {
			parcela = emBranco;
		}
		String valor = Uteis.getDoubleFormatado(valorTotal);
		if (valor == null || valor.equals("0") || valor.equals("")) {
			valor = emBranco;
		}
		String valorTotalGeralStr = Uteis.getDoubleFormatado(qtdeParcela * valorTotal);
		if (valorTotalGeralStr == null || valorTotalGeralStr.equals("0") || valorTotalGeralStr.equals("")) {
			valorTotalGeralStr = emBranco;
		}
		// while
		// (texto.indexOf(Uteis.trocarAcentuacaoPorAcentuacaoHTML(marcador.getTag()))
		// != -1) {
		while (texto.indexOf(marcador.getTag()) != -1) {
			int tamanho = obterTamanhoTag(marcador.getTag());
			String mar = obterTagSemTextoSemTamanho(marcador.getTag());
			String textoTag = obterTextoTag(marcador.getTag());

			if (mar.contains("ValorTotalParcela_InclusaoReposicao")) {
				texto = substituirTag(texto, marcador.getTag(), valor, textoTag, tamanho);
			} else if (mar.contains("Parcela_InclusaoReposicao")) {
				texto = substituirTag(texto, marcador.getTag(), parcela, textoTag, tamanho);
			} else if (mar.contains("ValorTotal_InclusaoReposicao")) {
				texto = substituirTag(texto, marcador.getTag(), valorTotalGeralStr, textoTag, tamanho);
			}
			break;
		}
		return texto;
	}

	public Double obterValorDescontoConvenio(ConvenioVO convenioVO, Double valorBase) {
		Double valorDescConvenio = 0.0;
		Double valorDescAplicar = 0.0;
		Double valorBaseTemp = valorBase;
		if (!convenioVO.getTipoDescontoParcela().equals("PO")) {
			valorDescConvenio = valorDescConvenio + convenioVO.getDescontoParcela();
			valorBaseTemp = valorBaseTemp - valorDescConvenio;
		} else {
			valorDescAplicar = Uteis.arrendondarForcando2CadasDecimais((convenioVO.getDescontoParcela() / 100) * valorBaseTemp);
			valorDescConvenio = valorDescConvenio + valorDescAplicar;
			valorBaseTemp = valorBaseTemp - valorDescConvenio;
		}
		return valorDescConvenio;
	}

	@Override
	public OrientacaoPaginaEnum getOrientacaoDaPaginaEnum() {
		if (orientacaoDaPagina == null) {
			return OrientacaoPaginaEnum.RETRATO;
		}
		return OrientacaoPaginaEnum.getEnum(getOrientacaoDaPagina());
	}

	public String getOrientacaoDaPagina() {
		if (orientacaoDaPagina == null) {
			orientacaoDaPagina = "RE";
		}
		return orientacaoDaPagina;
	}

	public void setOrientacaoDaPagina(String orientacaoDaPagina) {
		this.orientacaoDaPagina = orientacaoDaPagina;
	}

	public String getMargemDireita() {
		if (margemDireita == null) {
			margemDireita = "1";
		}
		return margemDireita;
	}

	public void setMargemDireita(String margemDireita) {
		this.margemDireita = margemDireita;
	}

	public String getMargemEsquerda() {
		if (margemEsquerda == null) {
			margemEsquerda = "1";
		}
		return margemEsquerda;
	}

	public void setMargemEsquerda(String margemEsquerda) {
		this.margemEsquerda = margemEsquerda;
	}

	public String getMargemInferior() {
		if (margemInferior == null) {
			margemInferior = "1";
		}
		return margemInferior;
	}

	public void setMargemInferior(String margemInferior) {
		this.margemInferior = margemInferior;
	}

	public String getMargemSuperior() {
		if (margemSuperior == null) {
			margemSuperior = "1";
		}
		return margemSuperior;
	}

	public void setMargemSuperior(String margemSuperior) {
		this.margemSuperior = margemSuperior;
	}

	public TipoDesigneTextoEnum getTipoDesigneTextoEnum() {
		if (tipoDesigneTextoEnum == null) {
			tipoDesigneTextoEnum = TipoDesigneTextoEnum.HTML;
		}
		return tipoDesigneTextoEnum;
	}

	public void setTipoDesigneTextoEnum(TipoDesigneTextoEnum tipoDesigneTextoEnum) {
		this.tipoDesigneTextoEnum = tipoDesigneTextoEnum;
	}

	public ArquivoVO getArquivoIreport() {
		if(!getListaArquivoIreport().isEmpty()){
			arquivoIreport = getListaArquivoIreport().stream().filter(arquivo -> arquivo.getArquivoIreportPrincipal()).findFirst().orElse(new ArquivoVO());
		}
		else if (arquivoIreport == null) {
			arquivoIreport = new ArquivoVO();
		}
		return arquivoIreport;
	}

	public void setArquivoIreport(ArquivoVO arquivoIreport) {
		this.arquivoIreport = arquivoIreport;
	}

	public Boolean getAssinarDigitalmenteTextoPadrao() {
		return assinarDigitalmenteTextoPadrao;
	}

	public void setAssinarDigitalmenteTextoPadrao(Boolean assinarDigitalmenteTextoPadrao) {
		this.assinarDigitalmenteTextoPadrao = assinarDigitalmenteTextoPadrao;
	}

	public AlinhamentoAssinaturaDigitalEnum getAlinhamentoAssinaturaDigitalEnum() {
		if (alinhamentoAssinaturaDigitalEnum == null) {
			alinhamentoAssinaturaDigitalEnum = AlinhamentoAssinaturaDigitalEnum.RODAPE_DIREITA;
		}
		return alinhamentoAssinaturaDigitalEnum;
	}

	public void setAlinhamentoAssinaturaDigitalEnum(AlinhamentoAssinaturaDigitalEnum alinhamentoAssinaturaDigitalEnum) {
		this.alinhamentoAssinaturaDigitalEnum = alinhamentoAssinaturaDigitalEnum;
	}

	public HashMap getParametrosRel() {
		if (parametrosRel == null) {
			parametrosRel = new HashMap<String, Object>();
		}
		return parametrosRel;
	}

	public void setParametrosRel(HashMap<String, Object> parametrosRel) {
		this.parametrosRel = parametrosRel;
	}

	public String substituirTagEstagio(String texto, ImpressaoContratoVO impressaoContratoVO, MarcadorVO marcador) throws Exception {
		EstagioVO estagioVO = impressaoContratoVO.getEstagioVO();
		if(!Uteis.isAtributoPreenchido(estagioVO.getMatriculaVO().getMatricula())) {
			estagioVO.setMatriculaVO(impressaoContratoVO.getMatriculaVO());
		}

		String emBranco = obterStringEmBrancoParaTag(marcador);

		String codigo_Estagio = estagioVO.getCodigo().toString();
		if (codigo_Estagio == null || codigo_Estagio.equals("0") || codigo_Estagio.equals("")) {
			codigo_Estagio = emBranco;
		}
		
		String nomeUnidadeEnsino_Estagio = estagioVO.getMatriculaVO().getUnidadeEnsino().getNome().toString();
		if (nomeUnidadeEnsino_Estagio == null || nomeUnidadeEnsino_Estagio.equals("0") || nomeUnidadeEnsino_Estagio.equals("")) {
			nomeUnidadeEnsino_Estagio = emBranco;
		}
		
		String cnpjUnidadeEnsino_Estagio = estagioVO.getMatriculaVO().getUnidadeEnsino().getCNPJ().toString();
		if (cnpjUnidadeEnsino_Estagio == null || cnpjUnidadeEnsino_Estagio.equals("0") || cnpjUnidadeEnsino_Estagio.equals("")) {
			cnpjUnidadeEnsino_Estagio = emBranco;
		}
		
		String telefonesUnidadeEnsino_Estagio = estagioVO.getMatriculaVO().getUnidadeEnsino().getObservacao().toString();
		if (telefonesUnidadeEnsino_Estagio == null || telefonesUnidadeEnsino_Estagio.equals("0") || telefonesUnidadeEnsino_Estagio.equals("")) {
			telefonesUnidadeEnsino_Estagio = emBranco;
		}
		
		String enderecoCompletoUnidadeEnsino_Estagio = estagioVO.getMatriculaVO().getUnidadeEnsino().getEnderecoCompleto().toString();
		if (enderecoCompletoUnidadeEnsino_Estagio == null || enderecoCompletoUnidadeEnsino_Estagio.equals("0") || enderecoCompletoUnidadeEnsino_Estagio.equals("")) {
			enderecoCompletoUnidadeEnsino_Estagio = emBranco;
		}
		
		String cidadeUnidadeEnsino_Estagio = estagioVO.getMatriculaVO().getUnidadeEnsino().getCidade().getNome().toString();
		if (cidadeUnidadeEnsino_Estagio == null || cidadeUnidadeEnsino_Estagio.equals("0") || cidadeUnidadeEnsino_Estagio.equals("")) {
			cidadeUnidadeEnsino_Estagio = emBranco;
		}
		
		String poloUnidadeEnsino_Estagio = estagioVO.getMatriculaVO().getUnidadeEnsino().getRazaoSocial().toString();
		if (poloUnidadeEnsino_Estagio == null || poloUnidadeEnsino_Estagio.equals("0") || poloUnidadeEnsino_Estagio.equals("")) {
			poloUnidadeEnsino_Estagio = emBranco;
		}
		
		String orientadorEstagioUnidadeEnsino_Estagio = estagioVO.getMatriculaVO().getUnidadeEnsino().getOrientadorPadraoEstagio().getPessoa().getNome();
		if (orientadorEstagioUnidadeEnsino_Estagio == null || orientadorEstagioUnidadeEnsino_Estagio.equals("0") || orientadorEstagioUnidadeEnsino_Estagio.equals("")) {
			orientadorEstagioUnidadeEnsino_Estagio = emBranco;
		}
		
		String emailOrientadorEstagioUnidadeEnsino_Estagio = null;
		if(!estagioVO.getMatriculaVO().getUnidadeEnsino().getOrientadorPadraoEstagio().getPessoa().getListaPessoaEmailInstitucionalVO().isEmpty()) {
			emailOrientadorEstagioUnidadeEnsino_Estagio = estagioVO.getMatriculaVO().getUnidadeEnsino().getOrientadorPadraoEstagio().getPessoa().getListaPessoaEmailInstitucionalVO().get(0).getEmail();
		}
		if (emailOrientadorEstagioUnidadeEnsino_Estagio == null || emailOrientadorEstagioUnidadeEnsino_Estagio.equals("0") || emailOrientadorEstagioUnidadeEnsino_Estagio.equals("")) {
			emailOrientadorEstagioUnidadeEnsino_Estagio = emBranco;
		}
		
		String telefoneOrientadorEstagioUnidadeEnsino_Estagio = "";
		if(Uteis.isAtributoPreenchido(estagioVO.getMatriculaVO().getUnidadeEnsino().getOrientadorPadraoEstagio().getPessoa().getCelular())) {
			telefoneOrientadorEstagioUnidadeEnsino_Estagio = estagioVO.getMatriculaVO().getUnidadeEnsino().getOrientadorPadraoEstagio().getPessoa().getCelular() + ", ";
		}
		if(Uteis.isAtributoPreenchido(estagioVO.getMatriculaVO().getUnidadeEnsino().getOrientadorPadraoEstagio().getPessoa().getTelefoneRes())) {
			telefoneOrientadorEstagioUnidadeEnsino_Estagio = telefoneOrientadorEstagioUnidadeEnsino_Estagio + estagioVO.getMatriculaVO().getUnidadeEnsino().getOrientadorPadraoEstagio().getPessoa().getTelefoneRes() + ", ";
		}
		if(Uteis.isAtributoPreenchido(estagioVO.getMatriculaVO().getUnidadeEnsino().getOrientadorPadraoEstagio().getPessoa().getTelefoneComer())) {
			telefoneOrientadorEstagioUnidadeEnsino_Estagio = telefoneOrientadorEstagioUnidadeEnsino_Estagio + estagioVO.getMatriculaVO().getUnidadeEnsino().getOrientadorPadraoEstagio().getPessoa().getTelefoneComer() + ", ";
		}
		
		if (telefoneOrientadorEstagioUnidadeEnsino_Estagio == null || telefoneOrientadorEstagioUnidadeEnsino_Estagio.equals("0") || telefoneOrientadorEstagioUnidadeEnsino_Estagio.equals("")) {
			telefoneOrientadorEstagioUnidadeEnsino_Estagio = emBranco;
		}else {
			telefoneOrientadorEstagioUnidadeEnsino_Estagio = telefoneOrientadorEstagioUnidadeEnsino_Estagio.substring(0,telefoneOrientadorEstagioUnidadeEnsino_Estagio.length()- 2);
		}
		
		String nomeAluno_Estagio = estagioVO.getMatriculaVO().getAluno().getNome();
		if (nomeAluno_Estagio == null || nomeAluno_Estagio.equals("0") || nomeAluno_Estagio.equals("")) {
			nomeAluno_Estagio = emBranco;
		}
		
		String dataNascimentoAluno_Estagio = estagioVO.getMatriculaVO().getAluno().getDataNasc_Apresentar();
		if (dataNascimentoAluno_Estagio == null || dataNascimentoAluno_Estagio.equals("0") || dataNascimentoAluno_Estagio.equals("")) {
			dataNascimentoAluno_Estagio = emBranco;
		}
		
		String cpfAluno_Estagio = estagioVO.getMatriculaVO().getAluno().getCPF();
		if (cpfAluno_Estagio == null || cpfAluno_Estagio.equals("0") || cpfAluno_Estagio.equals("")) {
			cpfAluno_Estagio = emBranco;
		}
		
		String rgAluno_Estagio = estagioVO.getMatriculaVO().getAluno().getRG();
		if (rgAluno_Estagio == null || rgAluno_Estagio.equals("0") || rgAluno_Estagio.equals("")) {
			rgAluno_Estagio = emBranco;
		}
		
		String orgaoEmissorRgAluno_Estagio = estagioVO.getMatriculaVO().getAluno().getOrgaoEmissor();
		if (orgaoEmissorRgAluno_Estagio == null || orgaoEmissorRgAluno_Estagio.equals("0") || orgaoEmissorRgAluno_Estagio.equals("")) {
			orgaoEmissorRgAluno_Estagio = emBranco;
		}
		
		String cursoAluno_Estagio = estagioVO.getMatriculaVO().getCurso().getNome();
		if (cursoAluno_Estagio == null || cursoAluno_Estagio.equals("0") || cursoAluno_Estagio.equals("")) {
			cursoAluno_Estagio = emBranco;
		}
		
		String totalPeriodoLetivoCursoAluno_Estagio = estagioVO.getMatriculaVO().getCurso().getNrPeriodoLetivo().toString();
		if (totalPeriodoLetivoCursoAluno_Estagio == null || totalPeriodoLetivoCursoAluno_Estagio.equals("0") || totalPeriodoLetivoCursoAluno_Estagio.equals("")) {
			totalPeriodoLetivoCursoAluno_Estagio = emBranco;
		}
		
		String periodoLetivoCursoAluno_Estagio = impressaoContratoVO.getMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo().toString();
		if (periodoLetivoCursoAluno_Estagio == null || periodoLetivoCursoAluno_Estagio.equals("0") || periodoLetivoCursoAluno_Estagio.equals("")) {
			periodoLetivoCursoAluno_Estagio = emBranco;
		}
		
		String matriculaAluno_Estagio = estagioVO.getMatriculaVO().getMatricula();
		if (matriculaAluno_Estagio == null || matriculaAluno_Estagio.equals("0") || matriculaAluno_Estagio.equals("")) {
			matriculaAluno_Estagio = emBranco;
		}
		
		String semetreAnoIngresso_Estagio = estagioVO.getMatriculaVO().getAnoIngresso() + "/" + estagioVO.getMatriculaVO().getSemestreIngresso();
		if (semetreAnoIngresso_Estagio == null || semetreAnoIngresso_Estagio.equals("0") || semetreAnoIngresso_Estagio.equals("") || semetreAnoIngresso_Estagio.length() == 1) {
			semetreAnoIngresso_Estagio = emBranco;
		}
		
		String gradeCurricularEstagioAluno_Estagio = estagioVO.getGradeCurricularEstagioVO().getNome();
		if (gradeCurricularEstagioAluno_Estagio == null || gradeCurricularEstagioAluno_Estagio.equals("0") || gradeCurricularEstagioAluno_Estagio.equals("")) {
			gradeCurricularEstagioAluno_Estagio = emBranco;
		}
		
		String enderecoAluno_Estagio = estagioVO.getMatriculaVO().getAluno().getEndereco();
		if (enderecoAluno_Estagio == null || enderecoAluno_Estagio.equals("0") || enderecoAluno_Estagio.equals("")) {
			enderecoAluno_Estagio = emBranco;
		}
		
		String numeroAluno_Estagio = estagioVO.getMatriculaVO().getAluno().getNumero();
		if (numeroAluno_Estagio == null || numeroAluno_Estagio.equals("0") || numeroAluno_Estagio.equals("")) {
			numeroAluno_Estagio = emBranco;
		}
		
		String complementoAluno_Estagio = estagioVO.getMatriculaVO().getAluno().getComplemento();
		if (complementoAluno_Estagio == null || complementoAluno_Estagio.equals("0") || complementoAluno_Estagio.equals("")) {
			complementoAluno_Estagio = emBranco;
		}
		
		String cepAluno_Estagio = estagioVO.getMatriculaVO().getAluno().getCEP();
		if (cepAluno_Estagio == null || cepAluno_Estagio.equals("0") || cepAluno_Estagio.equals("")) {
			cepAluno_Estagio = emBranco;
		}
		
		String telefonesAluno_Estagio = estagioVO.getMatriculaVO().getAluno().getTelefoneRes();
		if (telefonesAluno_Estagio == null || telefonesAluno_Estagio.equals("0") || telefonesAluno_Estagio.equals("")) {
			telefonesAluno_Estagio = emBranco;
		}
		
		String cidadeAluno_Estagio = estagioVO.getMatriculaVO().getAluno().getCidade().getNome();
		if (cidadeAluno_Estagio == null || cidadeAluno_Estagio.equals("0") || cidadeAluno_Estagio.equals("")) {
			cidadeAluno_Estagio = emBranco;
		}
		
		String tipoConcedenteNome_Estagio = estagioVO.getTipoConcedenteVO().getNome();
		if (tipoConcedenteNome_Estagio == null || tipoConcedenteNome_Estagio.equals("0") || tipoConcedenteNome_Estagio.equals("")) {
			tipoConcedenteNome_Estagio = emBranco;
		}
		
		String cnpjConcedente_Estagio = estagioVO.getCnpj();
		if (cnpjConcedente_Estagio == null || cnpjConcedente_Estagio.equals("0") || cnpjConcedente_Estagio.equals("")) {
			cnpjConcedente_Estagio = emBranco;
		}
		
		String concedenteNome_Estagio = estagioVO.getConcedente();
		if (concedenteNome_Estagio == null || concedenteNome_Estagio.equals("0") || concedenteNome_Estagio.equals("")) {
			concedenteNome_Estagio = emBranco;
		}
		
		String telefoneConcedente_Estagio = estagioVO.getTelefone();
		if (telefoneConcedente_Estagio == null || telefoneConcedente_Estagio.equals("0") || telefoneConcedente_Estagio.equals("")) {
			telefoneConcedente_Estagio = emBranco;
		}
		
		String enderecoConcedente_Estagio = estagioVO.getEndereco();
		if (enderecoConcedente_Estagio == null || enderecoConcedente_Estagio.equals("0") || enderecoConcedente_Estagio.equals("")) {
			enderecoConcedente_Estagio = emBranco;
		}
		
		String numeroConcedente_Estagio = estagioVO.getNumero();
		if (numeroConcedente_Estagio == null || numeroConcedente_Estagio.equals("0") || numeroConcedente_Estagio.equals("")) {
			numeroConcedente_Estagio = emBranco;
		}
		
		String complementoConcedente_Estagio = estagioVO.getComplemento();
		if (complementoConcedente_Estagio == null || complementoConcedente_Estagio.equals("0") || complementoConcedente_Estagio.equals("")) {
			complementoConcedente_Estagio = emBranco;
		}
		
		String cidadeConcedente_Estagio = estagioVO.getCidade();
		if (cidadeConcedente_Estagio == null || cidadeConcedente_Estagio.equals("0") || cidadeConcedente_Estagio.equals("")) {
			cidadeConcedente_Estagio = emBranco;
		}
		
		String cepConcedente_Estagio = estagioVO.getCep();
		if (cepConcedente_Estagio == null || cepConcedente_Estagio.equals("0") || cepConcedente_Estagio.equals("")) {
			cepConcedente_Estagio = emBranco;
		}
		
		String diretorCoordenadorResponsavelConcedente_Estagio = estagioVO.getResponsavelConcedente();
		if (diretorCoordenadorResponsavelConcedente_Estagio == null || diretorCoordenadorResponsavelConcedente_Estagio.equals("0") || diretorCoordenadorResponsavelConcedente_Estagio.equals("")) {
			diretorCoordenadorResponsavelConcedente_Estagio = emBranco;
		}
		
		String telefoneResponsavelConcedente_Estagio = estagioVO.getTelefoneResponsavelConcedente();
		if (telefoneResponsavelConcedente_Estagio == null || telefoneResponsavelConcedente_Estagio.equals("0") || telefoneResponsavelConcedente_Estagio.equals("")) {
			telefoneResponsavelConcedente_Estagio = emBranco;
		}
		
		String emailReponsavelConcedente_Estagio = estagioVO.getEmailResponsavelConcedente();
		if (emailReponsavelConcedente_Estagio == null || emailReponsavelConcedente_Estagio.equals("0") || emailReponsavelConcedente_Estagio.equals("")) {
			emailReponsavelConcedente_Estagio = emBranco;
		}
		
		String cpfResponsavelConcedente_Estagio = estagioVO.getCpfResponsavelConcedente();
		if (cpfResponsavelConcedente_Estagio == null || cpfResponsavelConcedente_Estagio.equals("0") || cpfResponsavelConcedente_Estagio.equals("")) {
			cpfResponsavelConcedente_Estagio = emBranco;
		}
		
		String cargaHoraria_Estagio = estagioVO.getCargaHoraria().toString();
		if (cargaHoraria_Estagio == null || cargaHoraria_Estagio.equals("0") || cargaHoraria_Estagio.equals("")) {
			cargaHoraria_Estagio = emBranco;
		}
		
		String cargaHorariaDiaria_Estagio = estagioVO.getCargaHorariaDiaria().toString();
		if (cargaHorariaDiaria_Estagio == null || cargaHorariaDiaria_Estagio.equals("0") || cargaHorariaDiaria_Estagio.equals("")) {
			cargaHorariaDiaria_Estagio = emBranco;
		}

		String tipo_Estagio = estagioVO.getTipoEstagio().getValorApresentar();
		if (tipo_Estagio == null || tipo_Estagio.equals("")) {
			tipo_Estagio = emBranco;
		}

		String dataInicioVigencia_Estagio = Uteis.getData(estagioVO.getDataInicioVigencia());
		if (dataInicioVigencia_Estagio == null || dataInicioVigencia_Estagio.equals("")) {
			dataInicioVigencia_Estagio = emBranco;
		}
		String dataFinalVigencia_Estagio = Uteis.getData(estagioVO.getDataFinalVigencia());
		if (dataFinalVigencia_Estagio == null || dataFinalVigencia_Estagio.equals("")) {
			dataFinalVigencia_Estagio = emBranco;
		}

		String dataInicioRenovacaoVigencia_Estagio = Uteis.getData(estagioVO.getDataInicioRenovacaoVigencia());
		if (dataInicioRenovacaoVigencia_Estagio == null || dataInicioRenovacaoVigencia_Estagio.equals("")) {
			dataInicioRenovacaoVigencia_Estagio = emBranco;
		}
		String dataFinalRenovacaoVigencia_Estagio = Uteis.getData(estagioVO.getDataFinalRenovacaoVigencia());
		if (dataFinalRenovacaoVigencia_Estagio == null || dataFinalRenovacaoVigencia_Estagio.equals("")) {
			dataFinalRenovacaoVigencia_Estagio = emBranco;
		}
		String codigoDisciplina_Estagio = estagioVO.getDisciplina().getCodigo().toString();
		if (codigoDisciplina_Estagio == null || codigoDisciplina_Estagio.equals("")) {
			codigoDisciplina_Estagio = emBranco;
		}
		String nomeDisciplina_Estagio = estagioVO.getDisciplina().getNome();
		if (nomeDisciplina_Estagio == null || nomeDisciplina_Estagio.equals("")) {
			nomeDisciplina_Estagio = " ________________________________________ ";
		}

		String nomeBeneficiario_Estagio = estagioVO.getNomeBeneficiario();
		if (nomeBeneficiario_Estagio == null || nomeBeneficiario_Estagio.equals("")) {
			nomeBeneficiario_Estagio = " ________________________________________ ";
		}
		String rgBeneficiario_Estagio = estagioVO.getRgBeneficiario();
		if (rgBeneficiario_Estagio == null || rgBeneficiario_Estagio.equals("")) {
			rgBeneficiario_Estagio = " ________________________________________ ";
		}
		String cpfBeneficiario_Estagio = estagioVO.getCpfBeneficiario();
		if (cpfBeneficiario_Estagio == null || cpfBeneficiario_Estagio.equals("")) {
			cpfBeneficiario_Estagio = " ________________________________________ ";
		}
		String telefoneBeneficiario_Estagio = estagioVO.getTelefoneBeneficiario();
		if (telefoneBeneficiario_Estagio == null || telefoneBeneficiario_Estagio.equals("")) {
			telefoneBeneficiario_Estagio = " ________________________________________ ";
		}
		String cepBeneficiario_Estagio = estagioVO.getCepBeneficiario();
		if (cepBeneficiario_Estagio == null || cepBeneficiario_Estagio.equals("")) {
			cepBeneficiario_Estagio = " ________________________________________ ";
		}
		String emailBeneficiario_Estagio = estagioVO.getEmailBeneficiario();
		if (emailBeneficiario_Estagio == null || emailBeneficiario_Estagio.equals("")) {
			emailBeneficiario_Estagio = " ________________________________________ ";
		}
		String cidadeBeneficiario_Estagio = estagioVO.getCidadeBeneficiario();
		if (cidadeBeneficiario_Estagio == null || cidadeBeneficiario_Estagio.equals("")) {
			cidadeBeneficiario_Estagio = " ________________________________________ ";
		}
		String estadoBeneficiario_Estagio = estagioVO.getEstadoBeneficiario();
		if (estadoBeneficiario_Estagio == null || estadoBeneficiario_Estagio.equals("")) {
			estadoBeneficiario_Estagio = " ________________________________________ ";
		}
		String enderecoBeneficiario_Estagio = estagioVO.getEnderecoBeneficiario();
		if (enderecoBeneficiario_Estagio == null || enderecoBeneficiario_Estagio.equals("")) {
			enderecoBeneficiario_Estagio = " ________________________________________ ";
		}
		String numeroBeneficiario_Estagio = estagioVO.getNumeroBeneficiario();
		if (numeroBeneficiario_Estagio == null || numeroBeneficiario_Estagio.equals("")) {
			numeroBeneficiario_Estagio = " ________________________________________ ";
		}
		String setorBeneficiario_Estagio = estagioVO.getSetorBeneficiario();
		if (setorBeneficiario_Estagio == null || setorBeneficiario_Estagio.equals("")) {
			setorBeneficiario_Estagio = " ________________________________________ ";
		}
		String complementoBeneficiario_Estagio = estagioVO.getComplementoBeneficiario();
		if (complementoBeneficiario_Estagio == null || complementoBeneficiario_Estagio.equals("")) {
			complementoBeneficiario_Estagio = " ________________________________________ ";
		}
		
		String nomeSupervisor_Estagio = estagioVO.getNomeSupervisor();
		if (nomeSupervisor_Estagio == null || nomeSupervisor_Estagio.equals("0") || nomeSupervisor_Estagio.equals("")) {
			nomeSupervisor_Estagio = emBranco;
		}
		
		String cpfSupervisor_Estagio = estagioVO.getCpfSupervisor();
		if (cpfSupervisor_Estagio == null || cpfSupervisor_Estagio.equals("0") || cpfSupervisor_Estagio.equals("")) {
			cpfSupervisor_Estagio = emBranco;
		}
		
		String codigoEscolaMEC_Estagio = estagioVO.getCodigoEscolaMEC();
		if (codigoEscolaMEC_Estagio == null || codigoEscolaMEC_Estagio.equals("0") || codigoEscolaMEC_Estagio.equals("")) {
			codigoEscolaMEC_Estagio = emBranco;
		}

		String responsavelLegalAluno_Estagio = "";
		String grauParentescoResponsavelLegalAluno_Estagio = "";
		String rgResponsavelLegalAluno_Estagio = "";
		String cpfResponsavelLegalAluno_Estagio = "";
		Iterator<FiliacaoVO> listaFiliacaoResponsavel = impressaoContratoVO.getMatriculaVO().getAluno().getFiliacaoVOs().iterator();
		while (listaFiliacaoResponsavel.hasNext()) {
			FiliacaoVO filiacao = (FiliacaoVO) listaFiliacaoResponsavel.next();
			if (filiacao.getResponsavelLegal()) {
				responsavelLegalAluno_Estagio = filiacao.getNome();
				grauParentescoResponsavelLegalAluno_Estagio = filiacao.getTipo_Apresentar();
				rgResponsavelLegalAluno_Estagio = filiacao.getRG();
				cpfResponsavelLegalAluno_Estagio = filiacao.getCPF();
				break;
			}
		}

		while (texto.indexOf(marcador.getTag()) != -1) {
			int tamanho = obterTamanhoTag(marcador.getTag());
			String mar = obterTagSemTextoSemTamanho(marcador.getTag());
			String textoTag = obterTextoTag(marcador.getTag());

			if (mar.equalsIgnoreCase("nomeUnidadeEnsino_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), nomeUnidadeEnsino_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("cnpjUnidadeEnsino_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), cnpjUnidadeEnsino_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("telefonesUnidadeEnsino_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), telefonesUnidadeEnsino_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("enderecoCompletoUnidadeEnsino_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), enderecoCompletoUnidadeEnsino_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("cidadeUnidadeEnsino_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), cidadeUnidadeEnsino_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("poloUnidadeEnsino_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), poloUnidadeEnsino_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("orientadorEstagioUnidadeEnsino_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), orientadorEstagioUnidadeEnsino_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("emailOrientadorEstagioUnidadeEnsino_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), emailOrientadorEstagioUnidadeEnsino_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("telefoneOrientadorEstagioUnidadeEnsino_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), telefoneOrientadorEstagioUnidadeEnsino_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("nomeAluno_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), nomeAluno_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("dataNascimentoAluno_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), dataNascimentoAluno_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("cpfAluno_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), cpfAluno_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("rgAluno_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), rgAluno_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("orgaoEmissorRgAluno_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), orgaoEmissorRgAluno_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("cursoAluno_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), cursoAluno_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("totalPeriodoLetivoCursoAluno_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), totalPeriodoLetivoCursoAluno_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("periodoLetivoCursoAluno_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), periodoLetivoCursoAluno_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("matriculaAluno_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), matriculaAluno_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("semetreAnoIngresso_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), semetreAnoIngresso_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("gradeCurricularEstagioAluno_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), gradeCurricularEstagioAluno_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("enderecoAluno_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), enderecoAluno_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("numeroAluno_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), numeroAluno_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("complementoAluno_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), complementoAluno_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("cepAluno_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), cepAluno_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("telefonesAluno_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), telefonesAluno_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("cidadeAluno_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), cidadeAluno_Estagio, textoTag, tamanho);			
			} else if (mar.equalsIgnoreCase("tipoConcedenteNome_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), tipoConcedenteNome_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("cnpjConcedente_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), cnpjConcedente_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("concedenteNome_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), concedenteNome_Estagio, textoTag, tamanho);
			}else if (mar.equalsIgnoreCase("telefoneConcedente_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), telefoneConcedente_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("enderecoConcedente_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), enderecoConcedente_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("numeroConcedente_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), numeroConcedente_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("complementoConcedente_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), complementoConcedente_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("cidadeConcedente_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), cidadeConcedente_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("cepConcedente_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), cepConcedente_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("diretorCoordenadorResponsavelConcedente_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), diretorCoordenadorResponsavelConcedente_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("telefoneResponsavelConcedente_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), telefoneResponsavelConcedente_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("emailReponsavelConcedente_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), emailReponsavelConcedente_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("cpfResponsavelConcedente_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), cpfResponsavelConcedente_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("cargaHoraria_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), cargaHoraria_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("cargaHorariaDiaria_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), cargaHorariaDiaria_Estagio, textoTag, tamanho);			
			} else if (mar.equalsIgnoreCase("Tipo_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), tipo_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("dataInicioVigencia_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), dataInicioVigencia_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("dataFinalVigencia_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), dataFinalVigencia_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("dataInicioRenovacaoVigencia_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), dataInicioRenovacaoVigencia_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("dataFinalRenovacaoVigencia_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), dataFinalRenovacaoVigencia_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("codigoDisciplina_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), codigoDisciplina_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("nomeDisciplina_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), nomeDisciplina_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("nomeBeneficiario_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), nomeBeneficiario_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("rgBeneficiario_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), rgBeneficiario_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("cpfBeneficiario_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), cpfBeneficiario_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("enderecoBeneficiario_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), enderecoBeneficiario_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("numeroBeneficiario_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), numeroBeneficiario_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("cidadeBeneficiario_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), cidadeBeneficiario_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("estadoBeneficiario_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), estadoBeneficiario_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("complementoBeneficiario_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), complementoBeneficiario_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("cepBeneficiario_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), cepBeneficiario_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("setorBeneficiario_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), setorBeneficiario_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("emailBeneficiario_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), emailBeneficiario_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("telefoneBeneficiario_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), telefoneBeneficiario_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("codigo_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), codigo_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("nomeSupervisor_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), nomeSupervisor_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("cpfSupervisor_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), cpfSupervisor_Estagio, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("codigoEscolaMEC_Estagio")) {
				texto = substituirTag(texto, marcador.getTag(), codigoEscolaMEC_Estagio, textoTag, tamanho);
			}
			break;
		}
		return texto;
	}

	public String substituirTagInscProcSeletivo(String texto, ImpressaoContratoVO impressaoContratoVO, MarcadorVO marcador) throws Exception {
		InscricaoVO inscricaoVO = impressaoContratoVO.getInscricaoVO();
		String emBranco = obterStringEmBrancoParaTag(marcador);

		String numInscricao_InscProcSeletivo = inscricaoVO.getCodigo().toString();
		if (numInscricao_InscProcSeletivo == null || numInscricao_InscProcSeletivo.equals("0") || numInscricao_InscProcSeletivo.equals("")) {
			numInscricao_InscProcSeletivo = emBranco;
		}
		String nomeCandidato_InscProcSeletivo = inscricaoVO.getCandidato().getNome();
		if (nomeCandidato_InscProcSeletivo == null || nomeCandidato_InscProcSeletivo.equals("0") || nomeCandidato_InscProcSeletivo.equals("")) {
			nomeCandidato_InscProcSeletivo = emBranco;
		}
		String cpfCandidato_InscProcSeletivo = inscricaoVO.getCandidato().getCPF();
		if (cpfCandidato_InscProcSeletivo == null || cpfCandidato_InscProcSeletivo.equals("0") || cpfCandidato_InscProcSeletivo.equals("")) {
			cpfCandidato_InscProcSeletivo = emBranco;
		}
		String cursoCandidato_InscProcSeletivo = "";
		if (inscricaoVO.getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("AP")) {
			cursoCandidato_InscProcSeletivo = inscricaoVO.getCursoOpcao1().getCurso().getNome();
			if (cursoCandidato_InscProcSeletivo == null || cursoCandidato_InscProcSeletivo.equals("0") || cursoCandidato_InscProcSeletivo.equals("")) {
				cursoCandidato_InscProcSeletivo = emBranco;
			}
		}
		String turnoCandidato_InscProcSeletivo = "";
		if (inscricaoVO.getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("AP")) {
			turnoCandidato_InscProcSeletivo = inscricaoVO.getCursoOpcao1().getCurso().getNome();
			if (turnoCandidato_InscProcSeletivo == null || turnoCandidato_InscProcSeletivo.equals("0") || turnoCandidato_InscProcSeletivo.equals("")) {
				turnoCandidato_InscProcSeletivo = emBranco;
			}
		}
		String dataResultadoCandidato_InscProcSeletivo = "";
		if (inscricaoVO.getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("AP")) {
			dataResultadoCandidato_InscProcSeletivo = inscricaoVO.getResultadoProcessoSeletivoVO().getDataRegistro_Apresentar();
			if (dataResultadoCandidato_InscProcSeletivo == null || dataResultadoCandidato_InscProcSeletivo.equals("0") || dataResultadoCandidato_InscProcSeletivo.equals("")) {
				dataResultadoCandidato_InscProcSeletivo = emBranco;
			}
		}
		String situacaoResultadoCandidato_InscProcSeletivo = "";
		situacaoResultadoCandidato_InscProcSeletivo = inscricaoVO.getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao_Apresentar();
		if (situacaoResultadoCandidato_InscProcSeletivo == null || situacaoResultadoCandidato_InscProcSeletivo.equals("0") || situacaoResultadoCandidato_InscProcSeletivo.equals("")) {
			situacaoResultadoCandidato_InscProcSeletivo = emBranco;
		}
		String unidadeEnsinoCandidato_InscProcSeletivo = inscricaoVO.getUnidadeEnsino().getNome();
		if (unidadeEnsinoCandidato_InscProcSeletivo == null || unidadeEnsinoCandidato_InscProcSeletivo.equals("0") || unidadeEnsinoCandidato_InscProcSeletivo.equals("")) {
			unidadeEnsinoCandidato_InscProcSeletivo = emBranco;
		}
		String ano_InscProcSeletivo = inscricaoVO.getProcSeletivo().getAno();
		if (ano_InscProcSeletivo == null || ano_InscProcSeletivo.equals("0") || ano_InscProcSeletivo.equals("")) {
			ano_InscProcSeletivo = emBranco;
		}
		String semestre_InscProcSeletivo = inscricaoVO.getProcSeletivo().getSemestre();
		if (semestre_InscProcSeletivo == null || semestre_InscProcSeletivo.equals("0") || semestre_InscProcSeletivo.equals("")) {
			semestre_InscProcSeletivo = emBranco;
		}
		String data_InscProcSeletivo = inscricaoVO.getData_Apresentar();
		if (data_InscProcSeletivo == null || data_InscProcSeletivo.equals("0") || data_InscProcSeletivo.equals("")) {
			data_InscProcSeletivo = emBranco;
		}
		String dataProva_InscProcSeletivo = inscricaoVO.getItemProcessoSeletivoDataProva().getDataProva_Apresentar();
		if (dataProva_InscProcSeletivo == null || dataProva_InscProcSeletivo.equals("0") || dataProva_InscProcSeletivo.equals("")) {
			dataProva_InscProcSeletivo = emBranco;
		}
		String classificacao_InscProcSeletivo = inscricaoVO.getResultadoProcessoSeletivoVO().getClassificacao().toString();
		if (classificacao_InscProcSeletivo == null || classificacao_InscProcSeletivo.equals("0") || classificacao_InscProcSeletivo.equals("")) {
			classificacao_InscProcSeletivo = emBranco;
		}
		String pontuacao_InscProcSeletivo = inscricaoVO.getResultadoProcessoSeletivoVO().getMediaNotasProcSeletivo().toString();
		if (pontuacao_InscProcSeletivo == null || pontuacao_InscProcSeletivo.equals("0") || pontuacao_InscProcSeletivo.equals("")) {
			pontuacao_InscProcSeletivo = emBranco;
		}

		while (texto.indexOf(marcador.getTag()) != -1) {
			int tamanho = obterTamanhoTag(marcador.getTag());
			String mar = obterTagSemTextoSemTamanho(marcador.getTag());
			String textoTag = obterTextoTag(marcador.getTag());

			if (mar.equalsIgnoreCase("Ano_InscProcSeletivo")) {
				texto = substituirTag(texto, marcador.getTag(), ano_InscProcSeletivo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Semestre_InscProcSeletivo")) {
				texto = substituirTag(texto, marcador.getTag(), semestre_InscProcSeletivo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Data_InscProcSeletivo")) {
				texto = substituirTag(texto, marcador.getTag(), data_InscProcSeletivo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataResultadoCandidato_InscProcSeletivo")) {
				texto = substituirTag(texto, marcador.getTag(), dataResultadoCandidato_InscProcSeletivo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("DataProva_InscProcSeletivo")) {
				texto = substituirTag(texto, marcador.getTag(), dataProva_InscProcSeletivo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NumInscCandidado_InscProcSeletivo")) {
				texto = substituirTag(texto, marcador.getTag(), numInscricao_InscProcSeletivo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("NomeCandidado_InscProcSeletivo")) {
				texto = substituirTag(texto, marcador.getTag(), nomeCandidato_InscProcSeletivo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CpfCandidado_InscProcSeletivo")) {
				texto = substituirTag(texto, marcador.getTag(), cpfCandidato_InscProcSeletivo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("CursoCandidado_InscProcSeletivo")) {
				texto = substituirTag(texto, marcador.getTag(), cursoCandidato_InscProcSeletivo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("TurnoCandidado_InscProcSeletivo")) {
				texto = substituirTag(texto, marcador.getTag(), turnoCandidato_InscProcSeletivo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("UnidadeEnsinoCandidado_InscProcSeletivo")) {
				texto = substituirTag(texto, marcador.getTag(), unidadeEnsinoCandidato_InscProcSeletivo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("ClassificacaoCandidado_InscProcSeletivo")) {
				texto = substituirTag(texto, marcador.getTag(), classificacao_InscProcSeletivo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("PontuacaoCandidado_InscProcSeletivo")) {
				texto = substituirTag(texto, marcador.getTag(), pontuacao_InscProcSeletivo, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("SituacaoResultadoCandidado_InscProcSeletivo")) {
				texto = substituirTag(texto, marcador.getTag(), situacaoResultadoCandidato_InscProcSeletivo, textoTag, tamanho);
			}
			break;
		}
		return texto;
	}
	
	public String substituirTagRequerimento(String texto, ImpressaoContratoVO impressaoContratoVO, MarcadorVO marcador) throws Exception {
		String emBranco = obterStringEmBrancoParaTag(marcador);
		MatriculaVO matricula = impressaoContratoVO.getMatriculaVO();
		RequerimentoVO requerimento = impressaoContratoVO.getRequerimentoVO();
		String requerimento_TCC_Tema = requerimento.getTemaTccFacilitador();
		if (requerimento_TCC_Tema == null || requerimento_TCC_Tema.equals(Constantes.ZERO) || requerimento_TCC_Tema.equals(Constantes.EMPTY)) {
			requerimento_TCC_Tema = emBranco;
		}
		String requerimento_TCC_Assunto = requerimento.getAssuntoTccFacilitador();
		if (requerimento_TCC_Assunto == null || requerimento_TCC_Assunto.equals(Constantes.ZERO) || requerimento_TCC_Assunto.equals(Constantes.EMPTY)) {
			requerimento_TCC_Assunto = emBranco;
		}
		String requerimento_TCC_AvaliadorExterno = requerimento.getAvaliadorExternoFacilitador();
		if (requerimento_TCC_AvaliadorExterno == null || requerimento_TCC_AvaliadorExterno.equals(Constantes.ZERO) || requerimento_TCC_AvaliadorExterno.equals(Constantes.EMPTY)) {
			requerimento_TCC_AvaliadorExterno = emBranco;
		}
		String requerimento_Nome_Curso_GrupoTCC = Uteis.isAtributoPreenchido(requerimento.getGrupoFacilitador().getCursoVO().getNome()) ? requerimento.getGrupoFacilitador().getCursoVO().getNome() : matricula.getCurso().getNome();
		if (requerimento_Nome_Curso_GrupoTCC == null || requerimento_Nome_Curso_GrupoTCC.equals(Constantes.ZERO) || requerimento_Nome_Curso_GrupoTCC.equals(Constantes.EMPTY)) {
			requerimento_Nome_Curso_GrupoTCC = emBranco;
		}
		String requerimento_Ano_GrupoTCC = requerimento.getGrupoFacilitador().getAno();
		if (requerimento_Ano_GrupoTCC == null || requerimento_Ano_GrupoTCC.equals(Constantes.ZERO) || requerimento_Ano_GrupoTCC.equals(Constantes.EMPTY)) {
			requerimento_Ano_GrupoTCC = emBranco;
		}
		while (texto.indexOf(marcador.getTag()) != -1) {
			int tamanho = obterTamanhoTag(marcador.getTag());
			String mar = obterTagSemTextoSemTamanho(marcador.getTag());
			String textoTag = obterTextoTag(marcador.getTag());
			if (mar.equalsIgnoreCase("Requerimento_TCC_Tema")) {
				texto = substituirTag(texto, marcador.getTag(), requerimento_TCC_Tema, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Requerimento_TCC_Assunto")) {
				texto = substituirTag(texto, marcador.getTag(), requerimento_TCC_Assunto, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Requerimento_TCC_AvaliadorExterno")) {
				texto = substituirTag(texto, marcador.getTag(), requerimento_TCC_AvaliadorExterno, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Requerimento_GrupoTCC_NomeCurso")) {
				texto = substituirTag(texto, marcador.getTag(), requerimento_Nome_Curso_GrupoTCC, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Requerimento_GrupoTCC_Ano")) {
				texto = substituirTag(texto, marcador.getTag(), requerimento_Ano_GrupoTCC, textoTag, tamanho);
			} else if (mar.equalsIgnoreCase("Requerimento_TCC_LISTA_ALUNO")) {
				substituirTagComLista(texto, marcador.getTag(), requerimento.getGrupoFacilitador().getListaAlunos(), textoTag);
			}
			break;
		}
		return texto;
	}

	public List getListaDisciplinasCursadasOuMinistradas() {
		if (listaDisciplinasCursadasOuMinistradas == null) {
			listaDisciplinasCursadasOuMinistradas = new ArrayList();
		}
		return listaDisciplinasCursadasOuMinistradas;
	}

	public void setListaDisciplinasCursadasOuMinistradas(List listaDisciplinasCursadasOuMinistradas) {
		this.listaDisciplinasCursadasOuMinistradas = listaDisciplinasCursadasOuMinistradas;
	}

	public List getListaDisciplinaCertificado() {
		if (listaDisciplinaCertificado == null) {
			listaDisciplinaCertificado = new ArrayList();
		}
		return listaDisciplinaCertificado;
	}

	public void setListaDisciplinaCertificado(List listaDisciplinaCertificado) {
		this.listaDisciplinaCertificado = listaDisciplinaCertificado;
	}

	public String getCorAssinaturaDigitalmente() {
		if (corAssinaturaDigitalmente == null) {
			corAssinaturaDigitalmente = "#000000";
		}
		return corAssinaturaDigitalmente;
	}

	public void setCorAssinaturaDigitalmente(String corAssinaturaDigitalmente) {
		this.corAssinaturaDigitalmente = corAssinaturaDigitalmente;
	}
	
	
	
	public Float getLarguraAssinatura() {
		if (larguraAssinatura == null) {
			larguraAssinatura = 200f;
		}
		return larguraAssinatura;
	}

	public void setLarguraAssinatura(Float larguraAssinatura) {
		this.larguraAssinatura = larguraAssinatura;
	}

	public Float getAlturaAssinatura() {
		if (alturaAssinatura == null) {
			alturaAssinatura = 40f;
		}
		return alturaAssinatura;
	}

	public void setAlturaAssinatura(Float alturaAssinatura) {
		this.alturaAssinatura = alturaAssinatura;
	}

	public List<DescontoTipoDescontoRelVO> getListaTipoDescontoCurso() {
		if (listaTipoDescontoCurso == null) {
			listaTipoDescontoCurso = new ArrayList<DescontoTipoDescontoRelVO>();
		}
		return listaTipoDescontoCurso;
	}

	public void setListaTipoDescontoCurso(List<DescontoTipoDescontoRelVO> listaTipoDescontoCurso) {
		this.listaTipoDescontoCurso = listaTipoDescontoCurso;
	}
	
	

	public List<DescontoTipoDescontoRelVO> getListaTodosTipoDesconto_Curso() {
		if (listaTodosTipoDesconto_Curso == null) {
			listaTodosTipoDesconto_Curso = new ArrayList<DescontoTipoDescontoRelVO>();
		}
		return listaTodosTipoDesconto_Curso;
	}

	public void setListaTodosTipoDesconto_Curso(List<DescontoTipoDescontoRelVO> listaTodosTipoDesconto_Curso) {
		this.listaTodosTipoDesconto_Curso = listaTodosTipoDesconto_Curso;
	}
	
	

	public List<MatriculaPeriodoVencimentoVO> getListaAcompanhamentoFinanceiroMatricula() {
		if (listaAcompanhamentoFinanceiroMatricula == null) {
			listaAcompanhamentoFinanceiroMatricula = new ArrayList<MatriculaPeriodoVencimentoVO>();
		}
		return listaAcompanhamentoFinanceiroMatricula;
	}

	public void setListaAcompanhamentoFinanceiroMatricula(List<MatriculaPeriodoVencimentoVO> listaAcompanhamentoFinanceiroMatricula) {
		this.listaAcompanhamentoFinanceiroMatricula = listaAcompanhamentoFinanceiroMatricula;
	}
	
	@Override
	public List getObjetos() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<MatriculaPeriodoVencimentoVO> getListaContaReceberComDescontos() {
		if (listaContaReceberComDescontos == null) {
			listaContaReceberComDescontos = new ArrayList<MatriculaPeriodoVencimentoVO>();
		}
		return listaContaReceberComDescontos;
	}

	public void setListaContaReceberComDescontos(List<MatriculaPeriodoVencimentoVO> listaContaReceberComDescontos) {
		this.listaContaReceberComDescontos = listaContaReceberComDescontos;
	}

	public List getListaDisciplinasAprovadasPeriodoLetivo() {
		if (listaDisciplinasAprovadasPeriodoLetivo == null) {
			listaDisciplinasAprovadasPeriodoLetivo = new ArrayList<>();
		}
		return listaDisciplinasAprovadasPeriodoLetivo;
	}

	public void setListaDisciplinasAprovadasPeriodoLetivo(List listaDisciplinasAprovadasPeriodoLetivo) {
		this.listaDisciplinasAprovadasPeriodoLetivo = listaDisciplinasAprovadasPeriodoLetivo;
	}

	public List getListaPlanoDisciplinas() {
		if(listaPlanoDisciplinas == null) {
			listaPlanoDisciplinas = new ArrayList<>();
		}
		return listaPlanoDisciplinas;
	}

	public void setListaPlanoDisciplinas(List<PlanoDisciplinaRelVO> listaPlanoDisciplinas) {
		this.listaPlanoDisciplinas = listaPlanoDisciplinas;
	}

	public List<ArquivoVO> getListaArquivoIreport() {
		if(listaArquivoIreport == null) {
			listaArquivoIreport = new ArrayList<ArquivoVO>();
		}
		return listaArquivoIreport;
	}

	public void setListaArquivoIreport(List<ArquivoVO> listaArquivoIreport) {
		this.listaArquivoIreport = listaArquivoIreport;
	}
	
	public Boolean possuiArquivoIreportPrincipal() {
		if(!getListaArquivoIreport().isEmpty()) {
			return getListaArquivoIreport().stream().filter(arquivo -> arquivo.getArquivoIreportPrincipal()).findFirst().isPresent();
		}
		return false;
	}

	public FuncionarioVO getFuncionarioPrimarioVO() {
		if (funcionarioPrimarioVO == null) {
			funcionarioPrimarioVO = new FuncionarioVO();
		}
		return funcionarioPrimarioVO;
	}

	public void setFuncionarioPrimarioVO(FuncionarioVO funcionarioPrimarioVO) {
		this.funcionarioPrimarioVO = funcionarioPrimarioVO;
	}

	public FuncionarioVO getFuncionarioSecundarioVO() {
		if (funcionarioSecundarioVO == null) {
			funcionarioSecundarioVO = new FuncionarioVO();
		}
		return funcionarioSecundarioVO;
	}

	public void setFuncionarioSecundarioVO(FuncionarioVO funcionarioSecundarioVO) {
		this.funcionarioSecundarioVO = funcionarioSecundarioVO;
	}

	public CargoVO getCargoFuncionarioPrincipalVO() {
		if (cargoFuncionarioPrincipalVO == null) {
			cargoFuncionarioPrincipalVO = new CargoVO();
		}
		return cargoFuncionarioPrincipalVO;
	}

	public void setCargoFuncionarioPrincipalVO(CargoVO cargoFuncionarioPrincipalVO) {
		this.cargoFuncionarioPrincipalVO = cargoFuncionarioPrincipalVO;
	}

	public CargoVO getCargoFuncionarioSecundarioVO() {
		if (cargoFuncionarioSecundarioVO == null) {
			cargoFuncionarioSecundarioVO = new CargoVO();
		}
		return cargoFuncionarioSecundarioVO;
	}

	public void setCargoFuncionarioSecundarioVO(CargoVO cargoFuncionarioSecundarioVO) {
		this.cargoFuncionarioSecundarioVO = cargoFuncionarioSecundarioVO;
	}

	public Boolean getAssinarDocumentoAutomaticamenteFuncionarioPrimario() {
		if (assinarDocumentoAutomaticamenteFuncionarioPrimario == null) {
			assinarDocumentoAutomaticamenteFuncionarioPrimario = Boolean.FALSE;
		}
		return assinarDocumentoAutomaticamenteFuncionarioPrimario;
	}

	public void setAssinarDocumentoAutomaticamenteFuncionarioPrimario(
			Boolean assinarDocumentoAutomaticamenteFuncionarioPrimario) {
		this.assinarDocumentoAutomaticamenteFuncionarioPrimario = assinarDocumentoAutomaticamenteFuncionarioPrimario;
	}

	public Boolean getAssinarDocumentoAutomaticamenteFuncionarioSecundario() {
		if (assinarDocumentoAutomaticamenteFuncionarioSecundario == null) {
			assinarDocumentoAutomaticamenteFuncionarioSecundario = Boolean.FALSE;
		}
		return assinarDocumentoAutomaticamenteFuncionarioSecundario;
	}

	public void setAssinarDocumentoAutomaticamenteFuncionarioSecundario(
			Boolean assinarDocumentoAutomaticamenteFuncionarioSecundario) {
		this.assinarDocumentoAutomaticamenteFuncionarioSecundario = assinarDocumentoAutomaticamenteFuncionarioSecundario;
	}

	public List getListaDocumentosEntregues() {
		if (listaDocumentosEntregues == null) {
			listaDocumentosEntregues = new ArrayList<>();
		}
		return listaDocumentosEntregues;
	}

	public void setListaDocumentosEntregues(List listaDocumentosEntregues) {
		this.listaDocumentosEntregues = listaDocumentosEntregues;
	}


	
	
	
	
	
}
