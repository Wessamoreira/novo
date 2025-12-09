package negocio.comuns.administrativo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.enumeradores.TipoOrigemComunicacaoInternaEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade ComunicacaoInterna. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "comunicacaoInterna")
public class ComunicacaoInternaVO extends SuperVO {

    private Integer codigo;
    private Date data;
    private String tipoComunicadoInterno;
    private String tipoDestinatario;
    private String tipoRemetente;
    private String assunto;
    private String mensagem;
    private String mensagemSMS;
    private Date dataExibicaoInicial;
    private Date dataExibicaoFinal;
    private String prioridade;
    private Boolean enviarEmail;
    private Boolean enviarSMS;
    private Boolean enviarCopiaResponsavelFinanceiro;
    private Boolean enviarSomenteResponsavelFinanceiro;	
    private Boolean enviarCopiaPais;
    private String funcionarioNome;
    private String alunoNome;
    private String professorNome;
    private String turmaNome;
    private PessoaVO pessoa;
    private String pessoaNome;
    private Boolean tipoMarketing;
    private Boolean tipoLeituraObrigatoria;
    private Boolean removerCaixaSaida;
    private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>ComunicadoInternoDestinatario</code>.
     */
    private List comunicadoInternoDestinatarioVOs;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO responsavel;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Funcionario </code>.
     */
    private FuncionarioVO funcionario;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Cargo </code>.
     */
    private CargoVO cargo;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Departamento </code>.
     */
    private DepartamentoVO departamento;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>UnidadeEnsino </code>.
     */
    private UnidadeEnsinoVO unidadeEnsino;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO aluno;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO professor;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>AreaConhecimento </code>.
     */
    private TurmaVO turma;
    private DisciplinaVO disciplina;
    private AreaConhecimentoVO areaConhecimento;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>ComunicacaoInterna </code>.
     */
    private ComunicacaoInternaVO comunicadoInternoOrigem;
    private TipoOrigemComunicacaoInternaEnum tipoOrigemComunicacaoInternaEnum;
    private Integer codigoTipoOrigemComunicacaoInterna;
    
    // auxiliar
    private String resposta;
    private String imagemEnvelope;
    private Boolean remover;
    private ArquivoVO arquivoAnexo;
    private List<ArquivoVO> listaArquivosAnexo;
    //Boolean criado para controlar a apresentação da Mensagem na hora do Marketing para o usuario que está logando
    private Boolean digitarMensagem;
    private String imgCima;
    private String imgBaixo;
    private String caminhoImagemPadraoCima;
    private String caminhoImagemPadraoBaixo;
    private String caminhoImagemAvaliacao_1;
    private String caminhoImagemAvaliacao_2;
    private String caminhoImagemAvaliacao_3;
    private String caminhoImagemAvaliacao_4;
    private String imgAvaliacao_1;
    private String imgAvaliacao_2;
    private String imgAvaliacao_3;
    private String imgAvaliacao_4;
    
    private List<File> listaFileCorpoMensagem;
    private String caminhoPastaWeb;
    private UsuarioVO responsavelCancelamento;
    private Date dataCancelamento;
    private Boolean mensagemLidaVisaoAlunoCoordenador;
    private Boolean redefinirSenha;
    private PessoaVO coordenador;
    private String coordenadorNome;
    private Boolean copiaFollowUp;
    private String ano;
    private String semestre;
    private CursoVO curso;
    private DataModelo listaConsultaDestinatario;
    private Boolean enviarEmailInstitucional;
    private Integer TotalizadorDestinatario;
    public static final long serialVersionUID = 1L;
    
    private ArquivoVO planilhaDestinatarioAnexo;
    
    //TRANSIENT
    private PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaVO;
    private List<String> errosProcessamentoPlanilha;

    /**
     * Construtor padrão da classe <code>ComunicacaoInterna</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public ComunicacaoInternaVO() {
        super();
        inicializarDados();
    }

    public String getMensagemComLayout() {
        return getMensagemComLayout("");
    }
    
    public String getCabecarioEmailLayout() {
    	StringBuilder sb = new StringBuilder();
        sb.append("<div>");
        sb.append("<table class=\"MsoNormalTable\" style=\"mso-cellspacing: 0cm; mso-yfti-tbllook: 1184; mso-padding-alt: 0cm 0cm 0cm 0cm;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
        sb.append("<tbody>");
        sb.append("<tr style=\"mso-yfti-irow:2\">");        
        sb.append("<td style=\"background: #FFFFFF; padding: 0cm;\" width=\"750px;\">");
        sb.append("<p ><span style=\"font-family: Arial; color: black; font-size: 10pt;\"><strong>Assunto:</strong>&nbsp;").append(getAssunto()).append(" </span></p>");
        sb.append("<p ><span style=\"font-family: Arial; color: black; font-size: 10pt;\"><strong>Data:</strong>&nbsp;").append(getData_Apresentar()).append(" </span></p>");
        sb.append("<p ><span style=\"font-family: Arial; color: black; font-size: 10pt;\"><strong>Remetente:</strong>&nbsp;").append(getResponsavel().getNome()).append(" - ").append(getResponsavel().getEmail()).append(" </span></p>");
        Boolean destinatario = true;
        int cont = 1;
        for (ComunicadoInternoDestinatarioVO cidVO : getComunicadoInternoDestinatarioVOs()) {
        	if(destinatario){
        		sb.append("<p ><span style=\"font-family: Arial; color: black; font-size: 10pt;\"><strong>Para:</strong>&nbsp;").append(cidVO.getDestinatario().getNome()).append(" - ").append(cidVO.getDestinatario().getEmail()).append(" </span></p>");
        		destinatario = false;
        	}else{
        		sb.append("<p ><span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").append(cidVO.getDestinatario().getNome()).append(" - ").append(cidVO.getDestinatario().getEmail()).append(" </span></p>");
        	}
        	if(cont== 10){
        		sb.append("<p ><span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Mais......................</span></p>");
        		break;	
        	}
        	cont++;
        }
        sb.append("</td>");
        sb.append("</tr>");
        sb.append("</tbody>");
        sb.append("</table>");
        sb.append("</div>");
        sb.append("<br></br>");	
        return sb.toString();
    }
    
    public String getEncaminharMensagemComLayout(String mensagem) {
        StringBuilder sb = new StringBuilder();        
        sb.append("<div class=\"Section1\">");
        sb.append("<div>");
        sb.append("<p ><span style=\"font-family: Arial; color: black; font-size: 10pt;\">------------------------------ Mensagem enviada -------------------------------------</span></p>");
        sb.append("<p ><span style=\"font-family: Arial; color: black; font-size: 10pt;\"><strong>Remetente:</strong>&nbsp;").append(getResponsavel().getNome()).append(" - ").append(getResponsavel().getEmail()).append(" </span></p>");        
        sb.append("<p ><span style=\"font-family: Arial; color: black; font-size: 10pt;\"><strong>Data:</strong>&nbsp;").append(getData_Apresentar()).append(" </span></p>");
        sb.append("<p ><span style=\"font-family: Arial; color: black; font-size: 10pt;\"><strong>Assunto:</strong>&nbsp;").append(getAssunto()).append(" </span></p>");
        sb.append("</div>");        
        sb.append("</div>");
        return sb.toString();
    }

    public String getMensagemComLayout(String mensagem) {

        // obeter o ip ou dns;
//        String dominio = UteisEmail.getURLAplicacao("/imagens/email/");
        String dominio = "";
        try {
          	FacesContext context = FacesContext.getCurrentInstance();
          	String paginaAtual = context == null ? "" : context.getViewRoot().getViewId();
          	if (paginaAtual.contains("visaoProfessor") 
          			|| paginaAtual.contains("visaoAluno") 
          			|| paginaAtual.contains("visaoCoordenador") 
          			|| paginaAtual.contains("visaoCandidato") 
          			|| paginaAtual.contains("visaoParceiro") 
          			|| paginaAtual.contains("visaoPreInscricao")) {
          		dominio = "../resources/imagens/email/";	
          	}else if (paginaAtual.contains("/relatorio/")) {
          		dominio = "../../../resources/imagens/email/";          	
          	} else {
          		dominio = "../../resources/imagens/email/";        		
          	}
          } catch (Exception e) {
             // //System.out.println("Comunicacao Erro:" + e.getMessage());
          }
//        String img_cima = dominio + "cima_sei.jpg";
//        String img_baixo = dominio + "baixo_sei.jpg";
//        String img_meio_cima = dominio + "meio_cima_default1.png";
//        String img_meio_baixo = dominio + "meio_baixo_default1.png";
        setCaminhoImagemPadraoCima(dominio + "cima_sei.jpg");
        setCaminhoImagemPadraoBaixo(dominio + "baixo_sei.jpg");
        
        
        
        setCaminhoImagemAvaliacao_1("../resources/imagens/atendimento/star1.png");
        setCaminhoImagemAvaliacao_2("../resources/imagens/atendimento/star2.png");
        setCaminhoImagemAvaliacao_3("../resources/imagens/atendimento/star3.png");
        setCaminhoImagemAvaliacao_4("../resources/imagens/atendimento/star4.png");
        

        StringBuilder sb = new StringBuilder();
//        sb.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/xhtml; charset=UTF-8\" /></head><body>");
//        sb.append("<div class=\"Section1\">");
//        sb.append("<div>");
//        sb.append("<table class=\"MsoNormalTable\" style=\"mso-cellspacing: 0cm; mso-yfti-tbllook: 1184; mso-padding-alt: 0cm 0cm 0cm 0cm;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
//        sb.append("<tbody>");
//        sb.append("<tr style=\"height: 59.25pt; mso-yfti-irow: 0; mso-yfti-firstrow: yes;\">");
//        sb.append("<td style=\"height: 59.25pt; padding: 0cm;\" colspan=\"1\">");
//        sb.append("<p ><span style=\"font-family: Arial; color: black; font-size: 10pt; mso-no-proof: yes;\">");
        sb.append("<p><img id=\"_x0000_i1025\" src=\"" + getCaminhoImagemPadraoCima() + "\" alt=\"" + getImgCima() + "\" width=\"750px;\" border=\"0\" /></p>");
//        sb.append("</td>");
//        sb.append("</tr>");
        //sb.append("<tr style=\"mso-yfti-irow:1\">");
        //sb.append("<td style=\"padding:0cm 0cm 0cm 0cm\" colspan=\"3\">");
        //sb.append("<p ><span style=\"font-family: Arial; color: black; font-size: 10pt; mso-no-proof: yes;\"><img id=\"_x0000_i1027\" src=\"" + img_meio_cima + "\" border=\"0\" alt=\"" + img_meio_cima + "\" width=\"626\" height=\"20\" /></span></p>");
        //sb.append("</td>");
        //sb.append("</tr>");
//        sb.append("<tr style=\"mso-yfti-irow:2\">");
        //sb.append("<td style=\"width: 9pt; background: #DCD5FE; padding: 0cm;\" width=\"12\">");
        //sb.append("<p><span style=\"font-size:10.0pt;font-family:Arial;color:black\">&nbsp;</span></p>");
        //sb.append("</td>");
//        sb.append("<td style=\"background: #FFFFFF; padding: 0cm;\" width=\"750px\">");
        sb.append("<p ></p>");
        sb.append("<p ></p>");
        sb.append("<p ></p>");
        sb.append("<p ></p>");
        sb.append("<p ></p>");
        sb.append("<p >" + mensagem + "</p>");
        sb.append("<p ></p>");
        sb.append("<p ></p>");
        sb.append("<p ></p>");
        sb.append("<p ></p>");
        sb.append("<p ></p>");
//        sb.append("</span></td>");
        //sb.append("<td style=\"width: 9pt; background: #DCD5FE; padding: 0cm;\" width=\"12\">");
        //sb.append("<p ></p>");
        //sb.append("</td>");
//        sb.append("</tr>");
        //sb.append("<tr style=\"mso-yfti-irow:3\">");
        //sb.append("<td style=\"padding:0cm 0cm 0cm 0cm\" colspan=\"3\">");
        //sb.append("<p ><span style=\"font-family: Arial; color: black; font-size: 10pt; mso-no-proof: yes;\"><img id=\"_x0000_i1027\" src=\"" + img_meio_baixo + "\" border=\"0\" alt=\"" + img_meio_baixo + "\" width=\"626\" height=\"20\" /></span></p>");
        //sb.append("</td>");
        //sb.append("</tr>");
//        sb.append("<tr style=\"height: 43.5pt; mso-yfti-irow: 4; mso-yfti-lastrow: yes;\">");
//        sb.append("<td style=\"height: 43.5pt; padding: 0cm;\" colspan=\"1\">");
//        sb.append("<p ><span style=\"font-family: Arial; color: black; font-size: 10pt; mso-no-proof: yes;\">");
        sb.append("<p><img id=\"_x0000_i1028\" src=\"" + getCaminhoImagemPadraoBaixo() + "\" width=\"750px;\" border=\"0\" alt=\"" + getImgBaixo() + "\" /></p>");
//        sb.append("</td>");
//        sb.append("</tr>");
//        sb.append("</tbody>");
//        sb.append("</table>");
//        sb.append("</div>");
//        sb.append("<p ><span style=\"color: black;\">&nbsp;</span></p>");
//        sb.append("</div>");
        sb.append("</body></html>");
        return sb.toString();
    }

    public String getMensagemComLayoutTextoPadrao(String mensagem) {
    	return getMensagemComLayout(mensagem);
        }
        
    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ComunicacaoInternaVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ComunicacaoInternaVO obj) throws ConsistirException {
    	obj.setDigitarMensagem(!Uteis.retirarCodigoHtmlEspaco(Uteis.retiraTags(obj.getMensagem())).trim().isEmpty());
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA deve ser informado.");
        }
        if ((obj.getResponsavel() == null) || (obj.getResponsavel().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo RESPONSÁVEL deve ser informado.");
        }
        if (obj.getTipoComunicadoInterno().equals("")) {
            throw new ConsistirException("O campo TIPO COMUNICADO INTERNO deve ser informado.");
        }
        // if (obj.getUnidadeEnsino().equals("") ||
        // obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
        // throw new
        // ConsistirException("O campo UNIDADE ENSINO (Comunicação Interna) deve ser informado.");
        // }
        if (obj.getTipoComunicadoInterno().equals("LE") || obj.getTipoComunicadoInterno().equals("RE")) {
            if (obj.getTipoDestinatario().equals("")) {
                if (obj.getComunicadoInternoDestinatarioVOs().isEmpty()) {
                    throw new ConsistirException("O campo TIPO DESTINATÁRIO deve ser informado.");
                }
            }
            // if (obj.getTipoDestinatario().equals("FU")) {
            // if (obj.getFuncionario().equals("") ||
            // obj.getFuncionario().getCodigo().intValue() == 0) {
            // throw new
            // ConsistirException("O campo FUNCIONÁRIO (Comunicação Interna) deve ser informado.");
            // }
            // }
            // if (obj.getTipoDestinatario().equals("AL")) {
            // if (obj.getAluno().equals("") ||
            // obj.getAluno().getCodigo().intValue() == 0) {
            // throw new
            // ConsistirException("O campo ALUNO (Comunicação Interna) deve ser informado.");
            // }
            // }
            // if (obj.getTipoDestinatario().equals("PR")) {
            // if (obj.getProfessor().equals("") ||
            // obj.getProfessor().getCodigo().intValue() == 0) {
            // throw new
            // ConsistirException("O campo PROFESSOR (Comunicação Interna) deve ser informado.");
            // }
            // }
            if (obj.getTipoDestinatario().equals("CA")) {
                if (obj.getCargo().getCodigo().intValue() == 0) {
                    throw new ConsistirException("O campo CARGO deve ser informado.");
                }
            }
            if (obj.getTipoDestinatario().equals("DE")) {
                if (obj.getDepartamento().getCodigo().intValue() == 0) {
                    throw new ConsistirException("O campo DEPARTAMENTO deve ser informado.");
                }
            }
            if (obj.getTipoDestinatario().equals("AR")) {
                if (obj.getAreaConhecimento().equals("") || obj.getAreaConhecimento().getCodigo().intValue() == 0) {
                    throw new ConsistirException("O campo ÁREA CONHECIMENTO deve ser informado.");
                }
            }
            if (obj.getTipoDestinatario().equals("TU")) {
//                if (obj.getTurma().equals("") || obj.getTurma().getCodigo().intValue() == 0) {
//                    throw new ConsistirException("O campo TURMA  deve ser informado.");
//                }
            }
        }

        if (obj.getAssunto().equals("") && ((obj.getTipoMarketing() && obj.getExisteArquivo()) || (!obj.getTipoMarketing() && !obj.getTipoLeituraObrigatoria()) || (obj.getTipoLeituraObrigatoria()))) {
            throw new ConsistirException("O campo ASSUNTO deve ser informado.");
        }
        if (!obj.getDigitarMensagem() && ((!obj.getTipoMarketing() && !obj.getTipoLeituraObrigatoria()) || (obj.getTipoLeituraObrigatoria()))) {
            throw new ConsistirException("O campo MENSAGEM deve ser informado.");
        }
        if (!obj.getTipoMarketing() && ((obj.getListaArquivosAnexo().isEmpty() && !obj.getDigitarMensagem()))) {
            throw new ConsistirException("Selecione um Arquivo de Imagem ou digite uma mensagem !");
        }
        if (((obj.getTipoLeituraObrigatoria() || obj.getTipoMarketing()) && (obj.getDataExibicaoInicial() == null || obj.getDataExibicaoFinal() == null))) {
            throw new ConsistirException("O PERÍODO DE EXIBIÇÃO deve ser informado!");
        }
        if (obj.getPrioridade().equals("")) {
            throw new ConsistirException("O campo PRIORIDADE deve ser informado.");
        }
        if (obj.getComunicadoInternoDestinatarioVOs().isEmpty()) {
            throw new ConsistirException("Não foi informado nenhum Destinatário (Comunicação Interna).");
        }
    }

    public static void validarDadosParaConsultaComunicadoInternoDestinatario(ComunicacaoInternaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getTipoComunicadoInterno().equals("")) {
            throw new ConsistirException("O campo TIPO COMUNICADO INTERNO (Comunicação Interna) deve ser informado.");
        }
        if (obj.getTipoComunicadoInterno().equals("LE") || obj.getTipoComunicadoInterno().equals("RE")) {
            if (obj.getTipoDestinatario().equals("")) {
                throw new ConsistirException("O campo TIPO DESTINATÁRIO (Comunicação Interna) deve ser informado.");
            }
            if (obj.getTipoDestinatario().equals("CA")) {
                if (obj.getCargo().getCodigo().intValue() == 0) {
                    throw new ConsistirException("O campo CARGO (Comunicação Interna) deve ser informado.");
                }
            }
            if (obj.getTipoDestinatario().equals("DE")) {
                if (obj.getDepartamento().getCodigo().intValue() == 0) {
                    throw new ConsistirException("O campo DEPARTAMENTO (Comunicação Interna) deve ser informado.");
                }
            }
            if (obj.getTipoDestinatario().equals("AR")) {
                if (obj.getAreaConhecimento().getCodigo().intValue() == 0) {
                    throw new ConsistirException("O campo ÁREA CONHECIMENTO (Comunicação Interna) deve ser informado.");
                }
            }
            if (obj.getTipoDestinatario().equals("TU")) {
                if (obj.getTurma().getCodigo().intValue() == 0) {
                    throw new ConsistirException("O campo TURMA (Comunicação Interna) deve ser informado.");
                }
            }
        }
    }

    public int getTamList() {
        int x = (getNovoObj() ? getComunicadoInternoDestinatarioVOs().size() : getListaConsultaDestinatario().getTotalRegistrosEncontrados());
        if (x > 4) {
            return 4;
        }
        return x;
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setData(new Date());
        setTipoComunicadoInterno("LE");
        setMensagem(getMensagemComLayout());
        setResposta(getMensagemComLayout());
        setDataExibicaoInicial(new Date());
        setDataExibicaoFinal(new Date());
        setPrioridade("NO");
        setImgCima("cima_sei");
        setImgBaixo("baixo_sei");
        
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>ComunicadoInternoDestinatarioVO</code> ao List
     * <code>comunicadoInternoDestinatarioVOs</code>. Utiliza o atributo padrão
     * de consulta da classe <code>ComunicadoInternoDestinatario</code> -
     * getDestinatario().getCodigo() - como identificador (key) do objeto no
     * List.
     *
     * @param obj
     *            Objeto da classe <code>ComunicadoInternoDestinatarioVO</code>
     *            que será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjComunicadoInternoDestinatarioVOs(ComunicadoInternoDestinatarioVO obj) throws Exception {
        ComunicadoInternoDestinatarioVO.validarDados(obj);
        int index = 0;
        Iterator i = getComunicadoInternoDestinatarioVOs().iterator();
        while (i.hasNext()) {
            ComunicadoInternoDestinatarioVO objExistente = (ComunicadoInternoDestinatarioVO) i.next();
            if (objExistente.getDestinatario().getCodigo().equals(obj.getDestinatario().getCodigo()) && objExistente.getEmail().equals(obj.getEmail())) {
                getComunicadoInternoDestinatarioVOs().set(index, obj);
                return;
            }
            index++;
        }
        getComunicadoInternoDestinatarioVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>ComunicadoInternoDestinatarioVO</code> no List
     * <code>comunicadoInternoDestinatarioVOs</code>. Utiliza o atributo padrão
     * de consulta da classe <code>ComunicadoInternoDestinatario</code> -
     * getDestinatario().getCodigo() - como identificador (key) do objeto no
     * List.
     *
     * @param destinatario
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjComunicadoInternoDestinatarioVOs(Integer destinatario) throws Exception {
        int index = 0;
        Iterator i = getComunicadoInternoDestinatarioVOs().iterator();
        while (i.hasNext()) {
            ComunicadoInternoDestinatarioVO objExistente = (ComunicadoInternoDestinatarioVO) i.next();
            if (objExistente.getDestinatario().getCodigo().equals(destinatario)) {
                getComunicadoInternoDestinatarioVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>ComunicadoInternoDestinatarioVO</code> no List
     * <code>comunicadoInternoDestinatarioVOs</code>. Utiliza o atributo padrão
     * de consulta da classe <code>ComunicadoInternoDestinatario</code> -
     * getDestinatario().getCodigo() - como identificador (key) do objeto no
     * List.
     *
     * @param destinatario
     *            Parâmetro para localizar o objeto do List.
     */
    public ComunicadoInternoDestinatarioVO consultarObjComunicadoInternoDestinatarioVO(Integer destinatario) throws Exception {
        Iterator i = getComunicadoInternoDestinatarioVOs().iterator();
        while (i.hasNext()) {
            ComunicadoInternoDestinatarioVO objExistente = (ComunicadoInternoDestinatarioVO) i.next();
            if (objExistente.getDestinatario().getCodigo().equals(destinatario)) {
                return objExistente;
            }
        }
        return null;
    }

    public List retirarDestinatarioFuncionarioEscolhidoDaLista(List listaDestinatario, Integer destinatario) {
        List lista = new ArrayList();
        Iterator i = listaDestinatario.iterator();
        while (i.hasNext()) {
            FuncionarioVO objExistente = (FuncionarioVO) i.next();
            if (!objExistente.getPessoa().getCodigo().equals(destinatario)) {
                lista.add(objExistente);
            }
        }
        return lista;
    }

    /**
     * Retorna o objeto da classe <code>ComunicacaoInterna</code> relacionado
     * com (<code>ComunicacaoInterna</code>).
     */
    public ComunicacaoInternaVO getComunicadoInternoOrigem() {
        if (comunicadoInternoOrigem == null) {
            comunicadoInternoOrigem = new ComunicacaoInternaVO();
        }
        return (comunicadoInternoOrigem);
    }

    /**
     * Define o objeto da classe <code>ComunicacaoInterna</code> relacionado com
     * (<code>ComunicacaoInterna</code>).
     */
    public void setComunicadoInternoOrigem(ComunicacaoInternaVO obj) {
        this.comunicadoInternoOrigem = obj;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ComunicacaoInterna</code>).
     */
    public PessoaVO getProfessor() {
        if (professor == null) {
            professor = new PessoaVO();
        }
        return (professor);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ComunicacaoInterna</code>).
     */
    public void setProfessor(PessoaVO obj) {
        this.professor = obj;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ComunicacaoInterna</code>).
     */
    public PessoaVO getAluno() {
        if (aluno == null) {
            aluno = new PessoaVO();
        }
        return (aluno);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ComunicacaoInterna</code>).
     */
    public void setAluno(PessoaVO obj) {
        this.aluno = obj;
    }

    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>ComunicacaoInterna</code>).
     */
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    /**
     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>ComunicacaoInterna</code>).
     */
    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
    }

    /**
     * Retorna o objeto da classe <code>Departamento</code> relacionado com (
     * <code>ComunicacaoInterna</code>).
     */
    public DepartamentoVO getDepartamento() {
        if (departamento == null) {
            departamento = new DepartamentoVO();
        }
        return (departamento);
    }

    /**
     * Define o objeto da classe <code>Departamento</code> relacionado com (
     * <code>ComunicacaoInterna</code>).
     */
    public void setDepartamento(DepartamentoVO obj) {
        this.departamento = obj;
    }

    /**
     * Retorna o objeto da classe <code>Cargo</code> relacionado com (
     * <code>ComunicacaoInterna</code>).
     */
    public CargoVO getCargo() {
        if (cargo == null) {
            cargo = new CargoVO();
        }
        return (cargo);
    }

    /**
     * Define o objeto da classe <code>Cargo</code> relacionado com (
     * <code>ComunicacaoInterna</code>).
     */
    public void setCargo(CargoVO obj) {
        this.cargo = obj;
    }

    /**
     * Retorna o objeto da classe <code>Funcionario</code> relacionado com (
     * <code>ComunicacaoInterna</code>).
     */
    public FuncionarioVO getFuncionario() {
        if (funcionario == null) {
            funcionario = new FuncionarioVO();
        }
        return (funcionario);
    }

    /**
     * Define o objeto da classe <code>Funcionario</code> relacionado com (
     * <code>ComunicacaoInterna</code>).
     */
    public void setFuncionario(FuncionarioVO obj) {
        this.funcionario = obj;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ComunicacaoInterna</code>).
     */
    @XmlElement(name = "responsavel")
    public PessoaVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new PessoaVO();
        }
        return (responsavel);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ComunicacaoInterna</code>).
     */
    public void setResponsavel(PessoaVO obj) {
        this.responsavel = obj;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>ComunicadoInternoDestinatario</code>.
     */
    public List<ComunicadoInternoDestinatarioVO> getComunicadoInternoDestinatarioVOs() {
        if (comunicadoInternoDestinatarioVOs == null) {
            comunicadoInternoDestinatarioVOs = new ArrayList<ComunicadoInternoDestinatarioVO>(0);
        }
        return (comunicadoInternoDestinatarioVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>ComunicadoInternoDestinatario</code>.
     */
    public void setComunicadoInternoDestinatarioVOs(List comunicadoInternoDestinatarioVOs) {
        this.comunicadoInternoDestinatarioVOs = comunicadoInternoDestinatarioVOs;
    }

    public Boolean getEnviarEmail() {
        if (enviarEmail == null) {
            enviarEmail = Boolean.TRUE;
        }
        return (enviarEmail);
    }

    public Boolean isEnviarEmail() {
        if (enviarEmail == null) {
            enviarEmail = Boolean.TRUE;
        }
        return (enviarEmail);
    }

    public void setEnviarEmail(Boolean enviarEmail) {
        this.enviarEmail = enviarEmail;
    }

    public String getPrioridade() {
        return (prioridade);
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public Date getDataExibicaoFinal() {
        return (dataExibicaoFinal);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataExibicaoFinal_Apresentar() {
        return (Uteis.getData(getDataExibicaoFinal()));
    }

    public void setDataExibicaoFinal(Date dataExibicaoFinal) {
        this.dataExibicaoFinal = dataExibicaoFinal;
    }

    public Date getDataExibicaoInicial() {
        return (dataExibicaoInicial);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataExibicaoInicial_Apresentar() {
        return (Uteis.getData(getDataExibicaoInicial()));
    }

    public void setDataExibicaoInicial(Date dataExibicaoInicial) {
        this.dataExibicaoInicial = dataExibicaoInicial;
    }
    @XmlElement(name = "mensagem")
    public String getMensagem() {
        return (mensagem);
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    @XmlElement(name = "assunto")
    public String getAssunto() {
        if (assunto == null) {
            assunto = "";
        }
        return (assunto);
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }
    @XmlElement(name = "tipoDestinatario")
    public String getTipoDestinatario() {
        if (tipoDestinatario == null) {
            tipoDestinatario = "";
        }
        return (tipoDestinatario);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoDestinatario_Apresentar() {
        if (getTipoDestinatario().equals("PR")) {
            return "Professor";
        }
        if (getTipoDestinatario().equals("DE")) {
            return "Departamento";
        }
        if (getTipoDestinatario().equals("CA")) {
            return "Cargo";
        }
        if (getTipoDestinatario().equals("AR")) {
            return "Área Conhecimento";
        }
        if (getTipoDestinatario().equals("TU")) {
            return "Turma";
        }
        if (getTipoDestinatario().equals("AL")) {
            return "Aluno";
        }
        if (getTipoDestinatario().equals("RF")) {
            return "Responsável Financeiro";
        }
        if (getTipoDestinatario().equals("FU")) {
            return "Funcionário";
        }
        return (getTipoDestinatario());
    }

    public void setTipoDestinatario(String tipoDestinatario) {
        this.tipoDestinatario = tipoDestinatario;
    }

    @XmlElement(name = "tipoComunicadoInterno")
    public String getTipoComunicadoInterno() {
        return (tipoComunicadoInterno);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoComunicadoInterno_Apresentar() {
        if (getTipoComunicadoInterno().equals("MU")) {
            return "Mural";
        }
        if (getTipoComunicadoInterno().equals("LE")) {
            return "Somente Leitura";
        }
        if (getTipoComunicadoInterno().equals("RE")) {
            return "Exige Resposta";
        }
        return (getTipoComunicadoInterno());
    }

    public void setTipoComunicadoInterno(String tipoComunicadoInterno) {
        this.tipoComunicadoInterno = tipoComunicadoInterno;
    }

    public Date getData() {
        return (data);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    @XmlElement(name = "dataApresentar")
    public String getData_Apresentar() {
        return (Uteis.getDataComHora(getData()));
    }

    public void setData(Date data) {
        this.data = data;
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

    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
    }

    public AreaConhecimentoVO getAreaConhecimento() {
        if (areaConhecimento == null) {
            areaConhecimento = new AreaConhecimentoVO();
        }
        return areaConhecimento;
    }

    public void setAreaConhecimento(AreaConhecimentoVO areaConhecimento) {
        this.areaConhecimento = areaConhecimento;
    }

    public String getFuncionarioNome() {
        if (funcionarioNome == null) {
            funcionarioNome = "";
        }
        return funcionarioNome;
    }

    public void setFuncionarioNome(String funcionarioNome) {
        this.funcionarioNome = funcionarioNome;
    }

    public String getAlunoNome() {
        if (alunoNome == null) {
            alunoNome = "";
        }
        return alunoNome;
    }

    public void setAlunoNome(String alunoNome) {
        this.alunoNome = alunoNome;
    }

    public String getProfessorNome() {
        if (professorNome == null) {
            professorNome = "";
        }
        return professorNome;
    }

    public void setProfessorNome(String professorNome) {
        this.professorNome = professorNome;
    }

    public String getTurmaNome() {
        if (turmaNome == null) {
            turmaNome = "";
        }
        return turmaNome;
    }

    public void setTurmaNome(String turmaNome) {
        this.turmaNome = turmaNome;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public String getPessoaNome() {
        if (pessoaNome == null) {
            pessoaNome = "";
        }
        return pessoaNome;
    }

    public void setPessoaNome(String pessoaNome) {
        this.pessoaNome = pessoaNome;
    }

    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }
        return pessoa;
    }

    public void setPessoa(PessoaVO pessoa) {
        this.pessoa = pessoa;
    }

    public String getImagemEnvelope() {
        if (imagemEnvelope == null) {
            imagemEnvelope = "";
        }
        return imagemEnvelope;
    }

    public void setImagemEnvelope(String imagemEnvelope) {
        this.imagemEnvelope = imagemEnvelope;
    }

    public Boolean getExisteArquivo() {
        if (!getListaArquivosAnexo().isEmpty()) { 
        	return Boolean.TRUE;
        } else if (this.getArquivoAnexo() == null || this.getArquivoAnexo().getNome().equals("")) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
        //return true;
    }

    public Boolean getTipoMarketing() {
        if (tipoMarketing == null) {
            tipoMarketing = Boolean.FALSE;
        }
        return tipoMarketing;
    }

    public void setTipoMarketing(Boolean tipoMarketing) {
        this.tipoMarketing = tipoMarketing;
    }

    @XmlElement(name = "tipoLeituraObrigatoria")
    public Boolean getTipoLeituraObrigatoria() {
        if (tipoLeituraObrigatoria == null) {
            tipoLeituraObrigatoria = Boolean.FALSE;
        }
        return tipoLeituraObrigatoria;
    }

    public void setTipoLeituraObrigatoria(Boolean tipoLeituraObrigatoria) {
        this.tipoLeituraObrigatoria = tipoLeituraObrigatoria;
    }

    /**
     * @return the digitarMensagem
     */
    public Boolean getDigitarMensagem() {
        if (digitarMensagem == null) {
            digitarMensagem = Boolean.FALSE;
        }
        return digitarMensagem;
    }

    /**
     * @param digitarMensagem the digitarMensagem to set
     */
    public void setDigitarMensagem(Boolean digitarMensagem) {
        this.digitarMensagem = digitarMensagem;
    }

    public Boolean getRemover() {
        if (remover == null) {
            remover = Boolean.FALSE;
        }
        return remover;
    }

    public void setRemover(Boolean remover) {
        this.remover = remover;
    }

    public Boolean getRemoverCaixaSaida() {
        if (removerCaixaSaida == null) {
            removerCaixaSaida = Boolean.FALSE;
        }
        return removerCaixaSaida;
    }

    public void setRemoverCaixaSaida(Boolean removerCaixaSaida) {
        this.removerCaixaSaida = removerCaixaSaida;
    }

    public void setArquivoAnexo(ArquivoVO arquivoAnexo) {
        this.arquivoAnexo = arquivoAnexo;
    }

    public ArquivoVO getArquivoAnexo() {
        if (arquivoAnexo == null) {
            arquivoAnexo = new ArquivoVO();
        }
        return arquivoAnexo;
    }

    public String getImgBaixo() {
        if (imgBaixo == null) {
            imgBaixo = "";
        }
        return imgBaixo;
    }

    public void setImgBaixo(String imgBaixo) {
        this.imgBaixo = imgBaixo;
    }

    public String getImgCima() {
        if (imgCima == null) {
            imgCima = "";
        }
        return imgCima;
    }

    public void setImgCima(String imgCima) {
        this.imgCima = imgCima;
    }

    public String getCaminhoImagemPadraoBaixo() {
        if (caminhoImagemPadraoBaixo == null) {
            caminhoImagemPadraoBaixo = "";
        }
        return caminhoImagemPadraoBaixo;
    }

    public void setCaminhoImagemPadraoBaixo(String caminhoImagemPadraoBaixo) {
        this.caminhoImagemPadraoBaixo = caminhoImagemPadraoBaixo;
    }

    public String getCaminhoImagemPadraoCima() {
        if (caminhoImagemPadraoCima == null) {
            caminhoImagemPadraoCima = "";
        }
        return caminhoImagemPadraoCima;
    }

    public void setCaminhoImagemPadraoCima(String caminhoImagemPadraoCima) {
        this.caminhoImagemPadraoCima = caminhoImagemPadraoCima;
    }

    public List<File> getListaFileCorpoMensagem() {
        if (listaFileCorpoMensagem == null) {
            listaFileCorpoMensagem = new ArrayList<File>(0);
        }
        return listaFileCorpoMensagem;
    }

    public void setListaFileCorpoMensagem(List<File> listaFileCorpoMensagem) {
        this.listaFileCorpoMensagem = listaFileCorpoMensagem;
    }

    public String getCaminhoPastaWeb() {
        if (caminhoPastaWeb == null) {
            caminhoPastaWeb = "";
        }
        return caminhoPastaWeb;
    }

    public void setCaminhoPastaWeb(String caminhoPastaWeb) {
        this.caminhoPastaWeb = caminhoPastaWeb;
    }

    /**
     * @return the responsavelCancelamento
     */
    public UsuarioVO getResponsavelCancelamento() {
        if (responsavelCancelamento == null) {
            responsavelCancelamento = new UsuarioVO();
        }
        return responsavelCancelamento;
    }

    /**
     * @param responsavelCancelamento the responsavelCancelamento to set
     */
    public void setResponsavelCancelamento(UsuarioVO responsavelCancelamento) {
        this.responsavelCancelamento = responsavelCancelamento;
    }

    /**
     * @return the dataCancelamento
     */
    public Date getDataCancelamento() {
        return dataCancelamento;
    }

    /**
     * @param dataCancelamento the dataCancelamento to set
     */
    public void setDataCancelamento(Date dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }

    /**
     * @return the mensagemLidaVisaoAlunoCoordenador
     */
    public Boolean getMensagemLidaVisaoAlunoCoordenador() {
        return mensagemLidaVisaoAlunoCoordenador;
    }

    /**
     * @param mensagemLidaVisaoAlunoCoordenador the mensagemLidaVisaoAlunoCoordenador to set
     */
    public void setMensagemLidaVisaoAlunoCoordenador(Boolean mensagemLidaVisaoAlunoCoordenador) {
        this.mensagemLidaVisaoAlunoCoordenador = mensagemLidaVisaoAlunoCoordenador;
    }

    public String getTipoRemetente() {
        if (tipoRemetente == null) {
            tipoRemetente = "";
        }
        return tipoRemetente;
    }

    public void setTipoRemetente(String tipoRemetente) {
        this.tipoRemetente = tipoRemetente;
    }

    public Boolean getRedefinirSenha() {
        if(redefinirSenha == null){
            redefinirSenha = false;
        }
        return redefinirSenha;
    }

    public void setRedefinirSenha(Boolean redefinirSenha) {
        this.redefinirSenha = redefinirSenha;
    }

    
    public DisciplinaVO getDisciplina() {
        if(disciplina == null){
            disciplina = new DisciplinaVO();
        }
        return disciplina;
    }

    
    public void setDisciplina(DisciplinaVO disciplina) {
        this.disciplina = disciplina;
    }

    /**
     * @return the listaArquivosAnexo
     */
    public List<ArquivoVO> getListaArquivosAnexo() {
        if (listaArquivosAnexo == null) {
            listaArquivosAnexo = new ArrayList();
        }
        return listaArquivosAnexo;
    }

    /**
     * @param listaArquivosAnexo the listaArquivosAnexo to set
     */
    public void setListaArquivosAnexo(List<ArquivoVO> listaArquivosAnexo) {
        this.listaArquivosAnexo = listaArquivosAnexo;
    }
    
    public String getApresentarTextoBotaoIrParaTelaOrigem(){
    	if(getIsOrigemComunicaoInternaOuvidoria()){
    		return "Ir para ouvidoria";
    	}
    	if(getIsOrigemComunicaoInternaFaleConosco()){
    		return "Ir para fale conosco";
    	}
    	return "";
    }
    
    

	public Boolean getIsApresentarBotaoIrParaTelaOrigemCadastro(){
		if(getIsOrigemComunicaoInternaOuvidoria() || getIsOrigemComunicaoInternaFaleConosco()){
			return true;
		}
		return false;
	}
	
	public Boolean getIsOrigemComunicaoInternaFaleConosco(){
		if(getTipoOrigemComunicacaoInternaEnum().equals(TipoOrigemComunicacaoInternaEnum.FALE_CONOSCO)){
			return true;
		}
		return false;
	}
	
	public Boolean getIsOrigemComunicaoInternaOuvidoria(){
		if(getTipoOrigemComunicacaoInternaEnum().equals(TipoOrigemComunicacaoInternaEnum.OUVIDORIA)){
			return true;
		}
		return false;
	}
    
    public TipoOrigemComunicacaoInternaEnum getTipoOrigemComunicacaoInternaEnum() {
		if(tipoOrigemComunicacaoInternaEnum == null){
			tipoOrigemComunicacaoInternaEnum = TipoOrigemComunicacaoInternaEnum.NENHUM;
		}
		return tipoOrigemComunicacaoInternaEnum;
	}

	public void setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum tipoOrigemComunicacaoInternaEnum) {
		this.tipoOrigemComunicacaoInternaEnum = tipoOrigemComunicacaoInternaEnum;
	}

	public Integer getCodigoTipoOrigemComunicacaoInterna() {
		if(codigoTipoOrigemComunicacaoInterna == null){
			codigoTipoOrigemComunicacaoInterna = 0;
		}
		return codigoTipoOrigemComunicacaoInterna;
	}

	public void setCodigoTipoOrigemComunicacaoInterna(Integer codigoTipoOrigemComunicacaoInterna) {
		this.codigoTipoOrigemComunicacaoInterna = codigoTipoOrigemComunicacaoInterna;
	}

	public String getCaminhoImagemAvaliacao_1() {
		if(caminhoImagemAvaliacao_1== null){
			caminhoImagemAvaliacao_1 ="";
		}
		return caminhoImagemAvaliacao_1;
	}

	public void setCaminhoImagemAvaliacao_1(String caminhoImagemAvaliacao_1) {
		this.caminhoImagemAvaliacao_1 = caminhoImagemAvaliacao_1;
	}

	public String getCaminhoImagemAvaliacao_2() {
		if(caminhoImagemAvaliacao_2== null){
			caminhoImagemAvaliacao_2 ="";
		}
		return caminhoImagemAvaliacao_2;
	}

	public void setCaminhoImagemAvaliacao_2(String caminhoImagemAvaliacao_2) {
		this.caminhoImagemAvaliacao_2 = caminhoImagemAvaliacao_2;
	}

	public String getCaminhoImagemAvaliacao_3() {
		if(caminhoImagemAvaliacao_3== null){
			caminhoImagemAvaliacao_3 ="";
		}
		return caminhoImagemAvaliacao_3;
	}

	public void setCaminhoImagemAvaliacao_3(String caminhoImagemAvaliacao_3) {
		this.caminhoImagemAvaliacao_3 = caminhoImagemAvaliacao_3;
	}

	public String getCaminhoImagemAvaliacao_4() {
		if(caminhoImagemAvaliacao_4== null){
			caminhoImagemAvaliacao_4 ="";
		}
		return caminhoImagemAvaliacao_4;
	}

	public void setCaminhoImagemAvaliacao_4(String caminhoImagemAvaliacao_4) {
		this.caminhoImagemAvaliacao_4 = caminhoImagemAvaliacao_4;
	}

	public String getImgAvaliacao_1() {
		if(imgAvaliacao_1== null){
			imgAvaliacao_1 ="";
		}
		return imgAvaliacao_1;
	}

	public void setImgAvaliacao_1(String imgAvaliacao_1) {
		this.imgAvaliacao_1 = imgAvaliacao_1;
	}

	public String getImgAvaliacao_2() {
		if(imgAvaliacao_2== null){
			imgAvaliacao_2 ="";
		}
		return imgAvaliacao_2;
	}

	public void setImgAvaliacao_2(String imgAvaliacao_2) {
		this.imgAvaliacao_2 = imgAvaliacao_2;
	}

	public String getImgAvaliacao_3() {
		if(imgAvaliacao_3== null){
			imgAvaliacao_3 ="";
		}
		return imgAvaliacao_3;
	}

	public void setImgAvaliacao_3(String imgAvaliacao_3) {
		this.imgAvaliacao_3 = imgAvaliacao_3;
	}

	public String getImgAvaliacao_4() {
		if(imgAvaliacao_4== null){
			imgAvaliacao_4 ="";
		}
		return imgAvaliacao_4;
	}

	public void setImgAvaliacao_4(String imgAvaliacao_4) {
		this.imgAvaliacao_4 = imgAvaliacao_4;
	}

	public PessoaVO getCoordenador() {
		if (coordenador == null) {
			coordenador = new PessoaVO();
		}
		return coordenador;
	}

	public void setCoordenador(PessoaVO coordenador) {
		this.coordenador = coordenador;
	}

	public String getCoordenadorNome() {
		if (coordenadorNome == null) {
			coordenadorNome = "";
		}
		return coordenadorNome;
	}

	public void setCoordenadorNome(String coordenadorNome) {
		this.coordenadorNome = coordenadorNome;
	}

	public Boolean getCopiaFollowUp() {
		if (copiaFollowUp == null) {
			copiaFollowUp = Boolean.FALSE;
		}
		return copiaFollowUp;
	}

	public void setCopiaFollowUp(Boolean copiaFollowUp) {
		this.copiaFollowUp = copiaFollowUp;
	}

	public String getMensagemSMS() {
		if (mensagemSMS == null) {
			mensagemSMS = "";
		}
		return mensagemSMS;
	}

	public void setMensagemSMS(String mensagemSMS) {
		this.mensagemSMS = mensagemSMS;
	}

	public Boolean getEnviarSMS() {
		if (enviarSMS == null) {
			enviarSMS = Boolean.FALSE;
		}
		return enviarSMS;
	}

	public void setEnviarSMS(Boolean enviarSMS) {
		this.enviarSMS = enviarSMS;
	}
	
	public Boolean getEnviarCopiaResponsavelFinanceiro() {
		if (enviarCopiaResponsavelFinanceiro == null) {
			enviarCopiaResponsavelFinanceiro = Boolean.FALSE;
		}
		return enviarCopiaResponsavelFinanceiro;
	}

	public void setEnviarCopiaResponsavelFinanceiro(Boolean enviarCopiaResponsavelFinanceiro) {
		this.enviarCopiaResponsavelFinanceiro = enviarCopiaResponsavelFinanceiro;
	}

	public Boolean getEnviarCopiaPais() {
		if (enviarCopiaPais == null) {
			enviarCopiaPais = Boolean.FALSE;
		}
		return enviarCopiaPais;
	}

	public void setEnviarCopiaPais(Boolean enviarCopiaPais) {
		this.enviarCopiaPais = enviarCopiaPais;
	}

	public Boolean getEnviarSomenteResponsavelFinanceiro() {
		if (enviarSomenteResponsavelFinanceiro == null) {
			enviarSomenteResponsavelFinanceiro = Boolean.FALSE;
		}
		return enviarSomenteResponsavelFinanceiro;
	}

	public void setEnviarSomenteResponsavelFinanceiro(Boolean enviarSomenteResponsavelFinanceiro) {
		this.enviarSomenteResponsavelFinanceiro = enviarSomenteResponsavelFinanceiro;
	}

	public String getAno() {
		if(ano == null){
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if(semestre == null){
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public CursoVO getCurso() {
		if(curso == null){
			curso = new CursoVO();
		}
		return curso;
	}

	public void setCurso(CursoVO cursoVO) {
		this.curso = cursoVO;
	}
	
	public String destinatarios;
	
	public String getDestinatarios() {
		if(destinatarios == null){
			StringBuilder dest = new StringBuilder("");
			for(ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO: getComunicadoInternoDestinatarioVOs()){
				if(dest.length()>0){
					dest.append("; ");
				}
				if(Uteis.isAtributoPreenchido(comunicadoInternoDestinatarioVO.getDestinatario())){
					dest.append(comunicadoInternoDestinatarioVO.getDestinatario().getNome());
				}else if(Uteis.isAtributoPreenchido(comunicadoInternoDestinatarioVO.getDestinatarioParceiro())){
					dest.append(comunicadoInternoDestinatarioVO.getDestinatarioParceiro().getNome());
				}
			}
			destinatarios = dest.toString();
		}
		return destinatarios;
	}

	public void setDestinatarios(String destinatarios) {
		this.destinatarios = destinatarios;
	}

	public DataModelo getListaConsultaDestinatario() {
		if(listaConsultaDestinatario == null) {
			listaConsultaDestinatario =  new DataModelo();
			listaConsultaDestinatario.setLimitePorPagina(10);
			listaConsultaDestinatario.setPage(1);
			listaConsultaDestinatario.setPaginaAtual(1);
		}
		return listaConsultaDestinatario;
	}

	public void setListaConsultaDestinatario(DataModelo listaConsultaDestinatario) {
		this.listaConsultaDestinatario = listaConsultaDestinatario;
	}
	
	public String getStyleAlunos() {
		if (getComunicadoInternoDestinatarioVOs().size() == 1) {
			return "col-md-12";
		} else if (getComunicadoInternoDestinatarioVOs().size() == 2) {
			return "col-md-6";
		} else if (getComunicadoInternoDestinatarioVOs().size() == 3) {
			return "col-md-4";
		} else if (getComunicadoInternoDestinatarioVOs().size() == 4) {
			return "col-md-3";
		} 
		return "col-md-3";
	}
	
    public Boolean getEnviarEmailInstitucional() {
        if (enviarEmailInstitucional == null) {
        	enviarEmailInstitucional = Boolean.FALSE;
        }
        return (enviarEmailInstitucional);
    }

    public Boolean isEnviarEmailInstitucional() {
        if (enviarEmailInstitucional == null) {
        	enviarEmailInstitucional = Boolean.FALSE;
        }
        return (enviarEmailInstitucional);
    }

    public void setEnviarEmailInstitucional(Boolean enviarEmailInstitucional) {
        this.enviarEmailInstitucional = enviarEmailInstitucional;
    }

	public PersonalizacaoMensagemAutomaticaVO getPersonalizacaoMensagemAutomaticaVO() {
		if (personalizacaoMensagemAutomaticaVO == null) {
			personalizacaoMensagemAutomaticaVO = new PersonalizacaoMensagemAutomaticaVO();
		}
		return personalizacaoMensagemAutomaticaVO;
	}

	public void setPersonalizacaoMensagemAutomaticaVO(
			PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaVO) {
		this.personalizacaoMensagemAutomaticaVO = personalizacaoMensagemAutomaticaVO;
	}
    
	public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO() {
		if (configuracaoGeralSistemaVO == null) {
			configuracaoGeralSistemaVO = new ConfiguracaoGeralSistemaVO();
		}
		return configuracaoGeralSistemaVO;
	}
	
	public void setConfiguracaoGeralSistemaVO(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		this.configuracaoGeralSistemaVO = configuracaoGeralSistemaVO;
	}

	public Integer getTotalizadorDestinatario() {
		if(TotalizadorDestinatario == null) {
			TotalizadorDestinatario = 0;
		}
		return TotalizadorDestinatario;
	}

	public void setTotalizadorDestinatario(Integer totalizadorDestinatario) {
		TotalizadorDestinatario = totalizadorDestinatario;
	}

	public ArquivoVO getPlanilhaDestinatarioAnexo() {
		if (planilhaDestinatarioAnexo == null) {
			planilhaDestinatarioAnexo = new ArquivoVO();
		}
		return planilhaDestinatarioAnexo;
	}

	public void setPlanilhaDestinatarioAnexo(ArquivoVO planilhaDestinatarioAnexo) {
		this.planilhaDestinatarioAnexo = planilhaDestinatarioAnexo;
	}

	public List<String> getErrosProcessamentoPlanilha() {
		if (errosProcessamentoPlanilha == null) {
			errosProcessamentoPlanilha = new ArrayList<>();
		}
		return errosProcessamentoPlanilha;
	}

	public void setErrosProcessamentoPlanilha(List<String> errosProcessamentoPlanilha) {
		this.errosProcessamentoPlanilha = errosProcessamentoPlanilha;
	}
    
	
}
