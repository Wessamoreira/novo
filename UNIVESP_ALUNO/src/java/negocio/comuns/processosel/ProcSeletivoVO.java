package negocio.comuns.processosel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.enumeradores.TipoAvaliacaoProcessoSeletivoEnum;
import negocio.comuns.processosel.enumeradores.TipoLayoutComprovanteEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import webservice.DateAdapterMobile;
import webservice.servicos.objetos.SelectComboRSVO;

/**
 * Reponsável por manter os dados da entidade ProcSeletivo. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */

@XmlRootElement(name = "procSeletivo")
public class ProcSeletivoVO extends SuperVO {
	
    private Integer codigo;
    private String descricao;
    private Date dataInicio;
    private Date dataFim;
    private Date dataInicioInternet;
    private Date dataFimInternet;
    private Double valorInscricao;
    private Date dataProva;
    private String documentaoObrigatoria;
    private String nrOpcoesCurso;
    private String requisitosGerais;
    private String nivelEducacional;
    private String horarioProva;
    private Double mediaMinimaAprovacao;
    private Double notaMinimaRedacao;
    private QuestionarioVO questionario;
//    private List<ItemProcSeletivoDataProvaVO> itemProcSeletivoDataProvaVOs;
    private Integer qtdeDiasVencimentoAposInscricao;
    private Boolean informarQtdeDiasVencimentoAposInscricao;
    private String regimeAprovacao;
    private Integer quantidadeAcertosMinimosAprovacao;
    private Double valorPorAcerto;
    private CampanhaVO campanhaGerarAgendaInscritos;
    
    private Boolean tipoProcessoSeletivo;
    private Boolean tipoEnem;
    private Boolean tipoPortadorDiploma;
    private Boolean uploadComprovanteEnem;
    private Boolean uploadComprovantePortadorDiploma;
    private String  orientacaoUploadEnem;
    private String  orientacaoUploadPortadorDiploma;
    private Boolean tipoTransferencia;
    private Boolean obrigarUploadComprovanteTransferencia;
    private String orientacaoTransferencia;
    private Boolean permitirAlunosMatriculadosInscreverMesmoCurso;
    
    private Boolean informarDadosBancarios;
    
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>ProcSeletivoDisciplinasProcSeletivo</code>.
     */
//    private List<ProcSeletivoDisciplinasProcSeletivoVO> procSeletivoDisciplinasProcSeletivoVOs;
//    /**
//     * Atributo responsável por manter os objetos da classe
//     * <code>ProcSeletivoUnidadeEnsino</code>.
//     */
//    private List<ProcSeletivoUnidadeEnsinoVO> procSeletivoUnidadeEnsinoVOs;
//
//    private List<ProcSeletivoUnidadeEnsinoEixoCursoVO> procSeletivoUnidadeEnsinoEixoCursoVOs;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private UsuarioVO responsavel;
    private TipoLayoutComprovanteEnum tipoLayoutComprovante;
    private Boolean filtrarProcessoSeletivo;
    public static final long serialVersionUID = 1L;
    
    /**
     * Campos criados para gestão do painel gestor no monitoramento acadêmico/processo seletivo. 
     * facilitando assim a filtragem dos dados
     */
    private String ano;
    private String semestre;
    private TipoAvaliacaoProcessoSeletivoEnum tipoAvaliacaoProcessoSeletivo;
    private Boolean realizarProvaOnline;
    private Integer tempoRealizacaoProvaOnline;
    private String termoAceiteProva;
    private Boolean utilizarAutenticacaoEmail;
    private Boolean utilizarAutenticacaoSMS;
    private BigDecimal valorInscricaoApresentar;
    /**atributo transiente responsavel pelo preencheimento de lista de tipo de processoSeletivo webservice 
     * 
     */
    private List<SelectComboRSVO> listaTipoIngressoProcSeletivo;

    
    
//    private List<PeriodoChamadaProcSeletivoVO> periodoChamadaProcSeletivoVOs;

    /**
     * Construtor padrão da classe <code>ProcSeletivo</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public ProcSeletivoVO() {
        super();
    }
    
    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return (responsavel);
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ProcSeletivoVO</code>. Todos os tipos de consistência de dados são
     * e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     * 
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ProcSeletivoVO obj) throws ConsistirException {
        if (obj.getDescricao().equals("")) {
            throw new ConsistirException("O campo DESCRIÇÃO (Dados Básicos) deve ser informado.");
        }
        if (obj.getDataInicio() == null) {
            throw new ConsistirException("O campo DATA INÍCIO (Dados Básicos) deve ser informado.");
        }
        if (obj.getDataFim() == null) {
            throw new ConsistirException("O campo DATA FIM (Dados Básicos) deve ser informado.");
        }
		if (obj.getTipoEnem() == false && obj.getTipoPortadorDiploma() == false && obj.getTipoProcessoSeletivo() == false && obj.getTipoTransferencia() == false ) {
			throw new ConsistirException("O campo TIPO SELEÇÃO (Dados Básicos) deve ser informado.");
		}
//		if (obj.getDataProva() == null) {
//			throw new ConsistirException("O campo DATA PROVA (Dados Básicos) deve ser informado.");
//		}
//		if (obj.getDataProva().before(obj.getDataFim())) {
//			throw new ConsistirException("O campo DATA PROVA não pode ser anterior ao período de Inscrição.");
//		}
//		if (obj.getHorarioProva().equals("")) {
//			throw new ConsistirException("O campo HORÁRIO PROVA (Dados Básicos) deve ser informado.");
//		}
//        if ((obj.getResponsavel() == null) || (obj.getResponsavel().getCodigo().intValue() == 0)) {
//            throw new ConsistirException("O campo RESPONSÁVEL (Dados Básicos) deve ser informado.");
//        }
        if (obj.getSemestre().trim().isEmpty()) {
        	throw new ConsistirException("O campo SEMESTRE (Dados Básicos) deve ser informado.");
        }
        if (obj.getAno().trim().isEmpty()) {
        	throw new ConsistirException("O campo ANO (Dados Básicos) deve ser informado.");
        }
		if (obj.getRegimeAprovacao().equals("quantidadeAcertos") || obj.getRegimeAprovacao().equals("quantidadeAcertosRedacao")) {
			if (!Uteis.isAtributoPreenchido(obj.getValorPorAcerto()) || obj.getValorPorAcerto() <= 0) {
				throw new ConsistirException("O campo VALOR POR ACERTO (Dados Básicos) deve ser maior que 0.");
			}
		}
//		if (TipoAvaliacaoProcessoSeletivoEnum.AVALIACAO_CURRICULAR.equals(obj.getTipoAvaliacaoProcessoSeletivo()) && obj.getItemProcSeletivoDataProvaVOs().size() > 1) {
//			throw new ConsistirException(UteisJSF.internacionalizar("msg_ProcSeletivo_adicionarDataProcessoSeletivo"));
//		}
		if(obj.getRealizarProvaOnline() && obj.getTipoProcessoSeletivo() &&  obj.getTempoRealizacaoProvaOnline() <= 0) {
			throw new ConsistirException("O campo Tempo Realização da Prova On-line (Minutos) (Dados Básicos) deve ser maior que 0.");
		}
		if(obj.getInformarQtdeDiasVencimentoAposInscricao() && obj.getQtdeDiasVencimentoAposInscricao() <= 0) {
			throw new ConsistirException("O campo Qtde Dias Vencimento após Inscrição (Dados Básicos) deve ser maior que 0.");
		}
        // if (obj.isValidoParaTodosCursos().booleanValue()) {
        // if (obj.getProcSeletivoCursoVOs().size() != 0) {
        // throw new
        // ConsistirException("Não deve haver Cursos selecionados, haja vista que o campo VÁLIDO PARA TODOS OS CURSOS está selecionado.");
        // }
        // } else {
        // if (obj.getProcSeletivoCursoVOs().size() == 0) {
        // throw new
        // ConsistirException("É necessário indicar os Cursos válidos para este Dados Básicos. Ou o campo VÁLIDO PARA TODOS OS CURSOS deve estar selecionado.");
        // }
        // }
//		if (obj.getMediaMinimaAprovacao().doubleValue() == 0) {
//			throw new ConsistirException("O campo MÉDIA MÍNIMA APROVAÇÃO (Dados Básicos) deve ser informado.");
//		}
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>ProcSeletivoUnidadeEnsinoVO</code> ao List
     * <code>procSeletivoUnidadeEnsinoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>ProcSeletivoUnidadeEnsino</code> -
     * getUnidadeEnsino().getCodigo() - como identificador (key) do objeto no
     * List.
     * 
     * @param obj
     *            Objeto da classe <code>ProcSeletivoUnidadeEnsinoVO</code> que
     *            será adiocionado ao Hashtable correspondente.
     */
//    public void adicionarObjProcSeletivoUnidadeEnsinoVOs(ProcSeletivoUnidadeEnsinoVO obj) throws Exception {
//        ProcSeletivoUnidadeEnsinoVO.validarDados(obj);
//        int index = 0;
//        Iterator<ProcSeletivoUnidadeEnsinoVO> i = getProcSeletivoUnidadeEnsinoVOs().iterator();
//        while (i.hasNext()) {
//            ProcSeletivoUnidadeEnsinoVO objExistente = (ProcSeletivoUnidadeEnsinoVO) i.next();
//            if (objExistente.getUnidadeEnsino().getCodigo().equals(obj.getUnidadeEnsino().getCodigo())) {
//                getProcSeletivoUnidadeEnsinoVOs().set(index, objExistente);
//                return;
//            }
//            index++;
//        }
//        getProcSeletivoUnidadeEnsinoVOs().add(obj);
//        Ordenacao.ordenarLista(getProcSeletivoUnidadeEnsinoVOs(), "ordenacao");
//    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>ProcSeletivoUnidadeEnsinoVO</code> no List
     * <code>procSeletivoUnidadeEnsinoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>ProcSeletivoUnidadeEnsino</code> -
     * getUnidadeEnsino().getCodigo() - como identificador (key) do objeto no
     * List.
     * 
     * @param unidadeEnsino
     *            Parâmetro para localizar e remover o objeto do List.
     */
//    public void excluirObjProcSeletivoUnidadeEnsinoVOs(Integer unidadeEnsino) throws Exception {
//        int index = 0;
//        Iterator<ProcSeletivoUnidadeEnsinoVO> i = getProcSeletivoUnidadeEnsinoVOs().iterator();
//        while (i.hasNext()) {
//            ProcSeletivoUnidadeEnsinoVO objExistente = (ProcSeletivoUnidadeEnsinoVO) i.next();
//            if (objExistente.getUnidadeEnsino().getCodigo().equals(unidadeEnsino)) {
//                getProcSeletivoUnidadeEnsinoVOs().remove(index);
//                return;
//            }
//            index++;
//        }
//    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>ProcSeletivoUnidadeEnsinoVO</code> no List
     * <code>procSeletivoUnidadeEnsinoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>ProcSeletivoUnidadeEnsino</code> -
     * getUnidadeEnsino().getCodigo() - como identificador (key) do objeto no
     * List.
     * 
     * @param unidadeEnsino
     *            Parâmetro para localizar o objeto do List.
     */
//    public ProcSeletivoUnidadeEnsinoVO consultarObjProcSeletivoUnidadeEnsinoVO(Integer unidadeEnsino) throws Exception {
//        Iterator<ProcSeletivoUnidadeEnsinoVO> i = getProcSeletivoUnidadeEnsinoVOs().iterator();
//        while (i.hasNext()) {
//            ProcSeletivoUnidadeEnsinoVO objExistente = (ProcSeletivoUnidadeEnsinoVO) i.next();
//            if (objExistente.getUnidadeEnsino().getCodigo().equals(unidadeEnsino)) {
//                return objExistente;
//            }
//        }
//        return null;
//    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>ProcSeletivoDisciplinasProcSeletivoVO</code> ao List
     * <code>procSeletivoDisciplinasProcSeletivoVOs</code>. Utiliza o atributo
     * padrão de consulta da classe
     * <code>ProcSeletivoDisciplinasProcSeletivo</code> -
     * getDisciplinasProcSeletivo().getCodigo() - como identificador (key) do
     * objeto no List.
     * 
     * @param obj
     *            Objeto da classe
     *            <code>ProcSeletivoDisciplinasProcSeletivoVO</code> que será
     *            adiocionado ao Hashtable correspondente.
     */
//    public void adicionarObjProcSeletivoDisciplinasProcSeletivoVOs(ProcSeletivoDisciplinasProcSeletivoVO obj) throws Exception {
//        ProcSeletivoDisciplinasProcSeletivoVO.validarDados(obj);
//        int index = 0;
//        Iterator<ProcSeletivoDisciplinasProcSeletivoVO> i = getProcSeletivoDisciplinasProcSeletivoVOs().iterator();
//        while (i.hasNext()) {
//            ProcSeletivoDisciplinasProcSeletivoVO objExistente = (ProcSeletivoDisciplinasProcSeletivoVO) i.next();
//            if (objExistente.getDisciplinasProcSeletivo().getCodigo().equals(obj.getDisciplinasProcSeletivo().getCodigo())) {
//                getProcSeletivoDisciplinasProcSeletivoVOs().set(index, obj);
//                return;
//            }
//            index++;
//        }
//        getProcSeletivoDisciplinasProcSeletivoVOs().add(obj);
//        // adicionarObjSubordinadoOC
//    }
//    
//    public void adicionarObjItemProcSeletivoDataProvaVOs(ItemProcSeletivoDataProvaVO obj) throws Exception {
//        ItemProcSeletivoDataProvaVO.validarDados(obj);
//        
//        Iterator<ItemProcSeletivoDataProvaVO> i = getItemProcSeletivoDataProvaVOs().iterator();
//        while (i.hasNext()) {
//            ItemProcSeletivoDataProvaVO objExistente = (ItemProcSeletivoDataProvaVO) i.next();
//            if (objExistente.getDataProva_Apresentar().equals(obj.getDataProva_Apresentar())) {
//            	objExistente.setHora(Uteis.getData(obj.getDataProva(), "HH:mm"));
//            	objExistente.setDataProva(obj.getDataProva());
//            	objExistente.setDataInicioInscricao(obj.getDataInicioInscricao());
//            	objExistente.setDataTerminoInscricao(obj.getDataTerminoInscricao());                
//                if(getDataFimInternet().before(obj.getDataTerminoInscricao())){
//                	setDataFimInternet(obj.getDataTerminoInscricao());
//                }
//                if(getDataInicioInternet().after(obj.getDataInicioInscricao())){
//                	setDataInicioInternet(obj.getDataInicioInscricao());
//                }
//                Ordenacao.ordenarLista(getItemProcSeletivoDataProvaVOs(), "dataProva");
//                return;
//            }            
//        }
//        obj.setHora(Uteis.getData(obj.getDataProva(), "HH:mm"));
//        if(getDataFimInternet().before(obj.getDataTerminoInscricao())){
//        	setDataFimInternet(obj.getDataTerminoInscricao());
//        }
//        if(getDataInicioInternet().after(obj.getDataInicioInscricao())){
//        	setDataInicioInternet(obj.getDataInicioInscricao());
//        }
//        getItemProcSeletivoDataProvaVOs().add(obj);
//        Ordenacao.ordenarLista(getItemProcSeletivoDataProvaVOs(), "dataProva");
//    }
//
//    public void adicionarObjItemProcSeletivoDataGabaritoVOs(ItemProcSeletivoDataProvaVO obj) throws Exception {
//    	ItemProcSeletivoDataProvaVO.validarDados(obj);
//    	
//		for (Iterator<ItemProcSeletivoDataProvaVO> iterator = getItemProcSeletivoDataProvaVOs().iterator(); iterator.hasNext();) {
//    		ItemProcSeletivoDataProvaVO objExistente = (ItemProcSeletivoDataProvaVO) iterator.next();
//    		if (objExistente.getDataProva_Apresentar().equals(obj.getDataProva_Apresentar())) {
//    			objExistente.setHora(Uteis.getData(obj.getDataProva(), "HH:mm"));
//            	objExistente.setDataProva(obj.getDataProva());
//            	objExistente.setDataInicioInscricao(obj.getDataInicioInscricao());
//            	objExistente.setDataTerminoInscricao(obj.getDataTerminoInscricao());                
//                if(getDataFimInternet().before(obj.getDataTerminoInscricao())){
//                	setDataFimInternet(obj.getDataTerminoInscricao());
//                }
//                if(getDataInicioInternet().after(obj.getDataInicioInscricao())){
//                	setDataInicioInternet(obj.getDataInicioInscricao());
//                }
//                return;
//    		}
//    		
//    	}
//    	obj.setHora(Uteis.getData(obj.getDataProva(), "HH:mm"));
//    	if(getDataFimInternet().before(obj.getDataTerminoInscricao())){
//        	setDataFimInternet(obj.getDataTerminoInscricao());
//        }
//        if(getDataInicioInternet().after(obj.getDataInicioInscricao())){
//        	setDataInicioInternet(obj.getDataInicioInscricao());
//        }
//    	getItemProcSeletivoDataProvaVOs().add(obj);
//    	Ordenacao.ordenarLista(getItemProcSeletivoDataProvaVOs(), "dataProva");
//    }
//    
//    /**
//     * Operação responsável por excluir um objeto da classe
//     * <code>ProcSeletivoDisciplinasProcSeletivoVO</code> no List
//     * <code>procSeletivoDisciplinasProcSeletivoVOs</code>. Utiliza o atributo
//     * padrão de consulta da classe
//     * <code>ProcSeletivoDisciplinasProcSeletivo</code> -
//     * getDisciplinasProcSeletivo().getCodigo() - como identificador (key) do
//     * objeto no List.
//     * 
//     * @param disciplinasProcSeletivo
//     *            Parâmetro para localizar e remover o objeto do List.
//     */
//    public void excluirObjProcSeletivoDisciplinasProcSeletivoVOs(Integer disciplinasProcSeletivo) throws Exception {
//        int index = 0;
//        Iterator<ProcSeletivoDisciplinasProcSeletivoVO> i = getProcSeletivoDisciplinasProcSeletivoVOs().iterator();
//        while (i.hasNext()) {
//            ProcSeletivoDisciplinasProcSeletivoVO objExistente = (ProcSeletivoDisciplinasProcSeletivoVO) i.next();
//            if (objExistente.getDisciplinasProcSeletivo().getCodigo().equals(disciplinasProcSeletivo)) {
//                getProcSeletivoDisciplinasProcSeletivoVOs().remove(index);
//                
//                return;
//            }
//            index++;
//        }
//        // excluirObjSubordinadoOC
//    }
//
//    public void excluirObjItemProcSeletivoDataProvaVOs(Date dataProva, String hora) throws Exception {
//        int index = 0;
//        Iterator<ItemProcSeletivoDataProvaVO> i = getItemProcSeletivoDataProvaVOs().iterator();
//        while (i.hasNext()) {
//            ItemProcSeletivoDataProvaVO objExistente = (ItemProcSeletivoDataProvaVO) i.next();
//            if (objExistente.getDataProva_Apresentar().equals(Uteis.getData(dataProva, "dd/MM/yyyy HH:mm"))) {
//                getItemProcSeletivoDataProvaVOs().remove(index);
//                return;
//            }
//            index++;
//        }
//    }
//
//    /**
//     * Operação responsável por consultar um objeto da classe
//     * <code>ProcSeletivoDisciplinasProcSeletivoVO</code> no List
//     * <code>procSeletivoDisciplinasProcSeletivoVOs</code>. Utiliza o atributo
//     * padrão de consulta da classe
//     * <code>ProcSeletivoDisciplinasProcSeletivo</code> -
//     * getDisciplinasProcSeletivo().getCodigo() - como identificador (key) do
//     * objeto no List.
//     * 
//     * @param disciplinasProcSeletivo
//     *            Parâmetro para localizar o objeto do List.
//     */
//    public ProcSeletivoDisciplinasProcSeletivoVO consultarObjProcSeletivoDisciplinasProcSeletivoVO(Integer disciplinasProcSeletivo) throws Exception {
//        Iterator<ProcSeletivoDisciplinasProcSeletivoVO> i = getProcSeletivoDisciplinasProcSeletivoVOs().iterator();
//        while (i.hasNext()) {
//            ProcSeletivoDisciplinasProcSeletivoVO objExistente = (ProcSeletivoDisciplinasProcSeletivoVO) i.next();
//            if (objExistente.getDisciplinasProcSeletivo().getCodigo().equals(disciplinasProcSeletivo)) {
//                return objExistente;
//            }
//        }
//        return null;
//        // consultarObjSubordinadoOC
//    }
//
//    public ItemProcSeletivoDataProvaVO consultarObjItemProcSeletivoDataProvaVO(Date dataProva) throws Exception {
//        Iterator<ItemProcSeletivoDataProvaVO> i = getItemProcSeletivoDataProvaVOs().iterator();
//        while (i.hasNext()) {
//            ItemProcSeletivoDataProvaVO objExistente = (ItemProcSeletivoDataProvaVO) i.next();
//            if (objExistente.getDataProva_Apresentar().equals(Uteis.getData(dataProva, "dd/MM/yyyy hh:mm"))) {
//                return objExistente;
//            }
//        }
//        return null;
//    }
//
//    /**
//     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
//     * <code>ProcSeletivo</code>).
//     */
//    public UsuarioVO getResponsavel() {
//        if (responsavel == null) {
//            responsavel = new UsuarioVO();
//        }
//        return (responsavel);
//    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ProcSeletivo</code>).
     */
    public void setResponsavel(UsuarioVO obj) {
        this.responsavel = obj;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>ProcSeletivoUnidadeEnsino</code>.
     */
//    public List<ProcSeletivoUnidadeEnsinoVO> getProcSeletivoUnidadeEnsinoVOs() {
//        if (procSeletivoUnidadeEnsinoVOs == null) {
//            procSeletivoUnidadeEnsinoVOs = new ArrayList<ProcSeletivoUnidadeEnsinoVO>(0);
//        }
//        return (procSeletivoUnidadeEnsinoVOs);
//    }
//
//    /**
//     * Define Atributo responsável por manter os objetos da classe
//     * <code>ProcSeletivoUnidadeEnsino</code>.
//     */
//    public void setProcSeletivoUnidadeEnsinoVOs(List<ProcSeletivoUnidadeEnsinoVO> procSeletivoUnidadeEnsinoVOs) {
//        this.procSeletivoUnidadeEnsinoVOs = procSeletivoUnidadeEnsinoVOs;
//    }
//
//    /**
//     * Retorna Atributo responsável por manter os objetos da classe
//     * <code>ProcSeletivoDisciplinasProcSeletivo</code>.
//     */
//    public List<ProcSeletivoDisciplinasProcSeletivoVO> getProcSeletivoDisciplinasProcSeletivoVOs() {
//        if (procSeletivoDisciplinasProcSeletivoVOs == null) {
//            procSeletivoDisciplinasProcSeletivoVOs = new ArrayList<ProcSeletivoDisciplinasProcSeletivoVO>(0);
//        }
//        return (procSeletivoDisciplinasProcSeletivoVOs);
//    }
//
//    /**
//     * Define Atributo responsável por manter os objetos da classe
//     * <code>ProcSeletivoDisciplinasProcSeletivo</code>.
//     */
//    public void setProcSeletivoDisciplinasProcSeletivoVOs(List<ProcSeletivoDisciplinasProcSeletivoVO> procSeletivoDisciplinasProcSeletivoVOs) {
//        this.procSeletivoDisciplinasProcSeletivoVOs = procSeletivoDisciplinasProcSeletivoVOs;
//    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>ProcSeletivoCurso</code>.
     */
    public Date getDataProva() {
//		if (dataProva == null) {
//			return new Date();
//		}
        return (dataProva);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataProva_Apresentar() {
        if (dataProva == null) {
            return "";
        }
        return (Uteis.getData(dataProva));
    }

    public void setDataProva(Date dataProva) {
        this.dataProva = dataProva;
    }
    
    @XmlElement(name = "valorInscricao")
    public Double getValorInscricao() {
        if (valorInscricao == null) {
            valorInscricao = 0.0;
        }
        return (valorInscricao);
    }

    public void setValorInscricao(Double valorInscricao) {
        this.valorInscricao = valorInscricao;
    }

    @XmlElement(name = "dataFimInternet")
    @XmlJavaTypeAdapter(DateAdapterMobile.class)
    public Date getDataFimInternet() {
        if (dataFimInternet == null) {
            dataFimInternet = Uteis.getDataUltimoDiaMes(new Date());
        }
        return (dataFimInternet);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataFimInternet_Apresentar() {
        return (Uteis.getData(dataFimInternet));
    }

    public void setDataFimInternet(Date dataFimInternet) {
        this.dataFimInternet = dataFimInternet;
    }
    @XmlElement(name = "dataInicioInternet")
    @XmlJavaTypeAdapter(DateAdapterMobile.class)
    public Date getDataInicioInternet() {
        if (dataInicioInternet == null) {
            dataInicioInternet = Uteis.getDataPrimeiroDiaMes(new Date());
        }
        return (dataInicioInternet);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataInicioInternet_Apresentar() {
        return (Uteis.getData(dataInicioInternet));
    }

    public void setDataInicioInternet(Date dataInicioInternet) {
        this.dataInicioInternet = dataInicioInternet;
    }

    public Date getDataFim() {
        if (dataFim == null) {
            dataFim = Uteis.getDataUltimoDiaMes(new Date());
        }
        return (dataFim);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataFim_Apresentar() {
        return (Uteis.getData(dataFim));
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataInicio() {
        if (dataInicio == null) {
            dataInicio = Uteis.getDataPrimeiroDiaMes(new Date());
        }
        return (dataInicio);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataInicio_Apresentar() {
        return (Uteis.getData(dataInicio));
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }
    
    @XmlElement(name = "descricao")
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

    public String getNivelEducacional() {
        if (nivelEducacional == null) {
            nivelEducacional = "";
        }
        return (nivelEducacional);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getNivelEducacional_Apresentar() {
        if (nivelEducacional.equals("BA")) {
            return "Ensino Básico";
        }
        if (nivelEducacional.equals("PO")) {
            return "Pós-graduação";
        }
        if (nivelEducacional.equals("SU")) {
            return "Ensino Superior";
        }
        if (nivelEducacional.equals("ME")) {
            return "Ensino Médio";
        }
        return (nivelEducacional);
    }

    public void setNivelEducacional(String nivelEducacional) {
        this.nivelEducacional = nivelEducacional;
    }

    @XmlElement(name = "requisitosGerais")
    public String getRequisitosGerais() {
        if (requisitosGerais == null) {
            requisitosGerais = "";
        }
        return (requisitosGerais);
    }

    public void setRequisitosGerais(String requisitosGerais) {
        this.requisitosGerais = requisitosGerais;
    }
   
    @XmlElement(name = "nrOpcoesCurso")
    public String getNrOpcoesCurso() {
        if (nrOpcoesCurso == null) {
            nrOpcoesCurso = "1";
        }
        return (nrOpcoesCurso);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getNrOpcoesCurso_Apresentar() {
        if (nrOpcoesCurso.equals("3")) {
            return "3";
        }
        if (nrOpcoesCurso.equals("2")) {
            return "2";
        }
        if (nrOpcoesCurso.equals("1")) {
            return "1";
        }
        return (nrOpcoesCurso);
    }

    public void setNrOpcoesCurso(String nrOpcoesCurso) {
        this.nrOpcoesCurso = nrOpcoesCurso;
    }

    @XmlElement(name = "documentaoObrigatoria")
    public String getDocumentaoObrigatoria() {
        return (documentaoObrigatoria);
    }

    public void setDocumentaoObrigatoria(String documentaoObrigatoria) {
        this.documentaoObrigatoria = documentaoObrigatoria;
    }

    public String getHorarioProva() {
		if (horarioProva == null) {
			horarioProva = "";
		}
        return horarioProva;
    }

    public void setHorarioProva(String horarioProva) {
        this.horarioProva = horarioProva;
    }

    @XmlElement(name = "mediaMinimaAprovacao")
    public Double getMediaMinimaAprovacao() {
        if (mediaMinimaAprovacao == null) {
            mediaMinimaAprovacao = 0d;
        }
        return mediaMinimaAprovacao;
    }

    public void setMediaMinimaAprovacao(Double mediaMinimaAprovacao) {
        this.mediaMinimaAprovacao = mediaMinimaAprovacao;
    }

//    /**
//     * @return the itemProcSeletivoDataProvaVOs
//     */
//    public List<ItemProcSeletivoDataProvaVO> getItemProcSeletivoDataProvaVOs() {
//        if (itemProcSeletivoDataProvaVOs == null) {
//            itemProcSeletivoDataProvaVOs = new ArrayList<ItemProcSeletivoDataProvaVO>(0);
//        }
//        return itemProcSeletivoDataProvaVOs;
//    }
//
//    /**
//     * @param itemProcSeletivoDataProvaVOs the itemProcSeletivoDataProvaVOs to set
//     */
//    public void setItemProcSeletivoDataProvaVOs(List<ItemProcSeletivoDataProvaVO> itemProcSeletivoDataProvaVOs) {
//        this.itemProcSeletivoDataProvaVOs = itemProcSeletivoDataProvaVOs;
//    }

    /**
     * @return the questionario
     */
    
    @XmlElement(name = "questionario")
    public QuestionarioVO getQuestionario() {
        if (questionario == null) {
            questionario = new QuestionarioVO();
        }
        return questionario;
    }

    /**
     * @param questionario the questionario to set
     */
    public void setQuestionario(QuestionarioVO questionario) {
        this.questionario = questionario;
    }

    @XmlElement(name = "informarQtdeDiasVencimentoAposInscricao")
    public Boolean getInformarQtdeDiasVencimentoAposInscricao() {
        if (informarQtdeDiasVencimentoAposInscricao == null) {
            informarQtdeDiasVencimentoAposInscricao = false;
        }
        return informarQtdeDiasVencimentoAposInscricao;
    }

    public void setInformarQtdeDiasVencimentoAposInscricao(Boolean informarQtdeDiasVencimentoAposInscricao) {
        this.informarQtdeDiasVencimentoAposInscricao = informarQtdeDiasVencimentoAposInscricao;
    }

    @XmlElement(name = "qtdeDiasVencimentoAposInscricao")
    public Integer getQtdeDiasVencimentoAposInscricao() {
        if (qtdeDiasVencimentoAposInscricao == null) {
            qtdeDiasVencimentoAposInscricao = 0;
        }
        return qtdeDiasVencimentoAposInscricao;
    }

    public void setQtdeDiasVencimentoAposInscricao(Integer qtdeDiasVencimentoAposInscricao) {
        this.qtdeDiasVencimentoAposInscricao = qtdeDiasVencimentoAposInscricao;
    }


    @XmlElement(name = "regimeAprovacao")
    public String getRegimeAprovacao() {
        if (regimeAprovacao == null) {
            regimeAprovacao = "notaPorDisciplina";
        }
        return regimeAprovacao;
    }

    public void setRegimeAprovacao(String regimeAprovacao) {
        this.regimeAprovacao = regimeAprovacao;
    }

    @XmlElement(name = "quantidadeAcertosMinimosAprovacao")
    public Integer getQuantidadeAcertosMinimosAprovacao() {
        if (quantidadeAcertosMinimosAprovacao == null) {
            quantidadeAcertosMinimosAprovacao = 0;
        }
        return quantidadeAcertosMinimosAprovacao;
    }

    public void setQuantidadeAcertosMinimosAprovacao(Integer quantidadeAcertosMinimosAprovacao) {
        this.quantidadeAcertosMinimosAprovacao = quantidadeAcertosMinimosAprovacao;
    }

    @XmlElement(name = "tipoLayoutComprovante")
	public TipoLayoutComprovanteEnum getTipoLayoutComprovante() {
		if (tipoLayoutComprovante == null) {
			tipoLayoutComprovante = TipoLayoutComprovanteEnum.LAYOUT_2;
		}
		return tipoLayoutComprovante;
	}

	public void setTipoLayoutComprovante(TipoLayoutComprovanteEnum tipoLayoutComprovante) {
		this.tipoLayoutComprovante = tipoLayoutComprovante;
	}


    @XmlElement(name = "notaMinimaRedacao")
	public Double getNotaMinimaRedacao() {
		if (notaMinimaRedacao == null) {
			notaMinimaRedacao = 0.0;
		}
		return notaMinimaRedacao;
	}

	public void setNotaMinimaRedacao(Double notaMinimaRedacao) {
		this.notaMinimaRedacao = notaMinimaRedacao;
	}
	
	 @XmlElement(name = "ano")
	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	 @XmlElement(name = "semestre")
	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

    @XmlElement(name = "valorPorAcerto")
	public Double getValorPorAcerto() {
		if (valorPorAcerto == null) {
			valorPorAcerto = 1.0;
		}
		return valorPorAcerto;
	}

	public void setValorPorAcerto(Double valorPorAcerto) {
		this.valorPorAcerto = valorPorAcerto;
	}
	
    /**
     * @return the campanhaGerarAgendaInscritos
     */
    public CampanhaVO getCampanhaGerarAgendaInscritos() {
        if (campanhaGerarAgendaInscritos == null) {
            campanhaGerarAgendaInscritos = new CampanhaVO();
        }
        return campanhaGerarAgendaInscritos;
    }

    /**
     * @param campanhaGerarAgendaInscritos the campanhaGerarAgendaInscritos to set
     */
    public void setCampanhaGerarAgendaInscritos(CampanhaVO campanhaGerarAgendaInscritos) {
        this.campanhaGerarAgendaInscritos = campanhaGerarAgendaInscritos;
    }

    @XmlElement(name = "tipoAvaliacaoProcessoSeletivo")
	public TipoAvaliacaoProcessoSeletivoEnum getTipoAvaliacaoProcessoSeletivo() {
		if (tipoAvaliacaoProcessoSeletivo == null) {
			tipoAvaliacaoProcessoSeletivo = TipoAvaliacaoProcessoSeletivoEnum.PRESENCIAL;
		}
		return tipoAvaliacaoProcessoSeletivo;
	}

	public void setTipoAvaliacaoProcessoSeletivo(TipoAvaliacaoProcessoSeletivoEnum tipoAvaliacaoProcessoSeletivo) {
		this.tipoAvaliacaoProcessoSeletivo = tipoAvaliacaoProcessoSeletivo;
	}	

    public Boolean getFiltrarProcessoSeletivo() {
		if (filtrarProcessoSeletivo == null) {
			filtrarProcessoSeletivo = Boolean.TRUE;
		}
		return filtrarProcessoSeletivo;
	}

	public void setFiltrarProcessoSeletivo(Boolean filtrarProcessoSeletivo) {
		this.filtrarProcessoSeletivo = filtrarProcessoSeletivo;
	}

	@XmlElement(name = "tipoProcessoSeletivo")
	public Boolean getTipoProcessoSeletivo() {
		if (tipoProcessoSeletivo == null) {
			tipoProcessoSeletivo = Boolean.TRUE;
		}
		return tipoProcessoSeletivo;
	}

	public void setTipoProcessoSeletivo(Boolean tipoProcessoSeletivo) {
		this.tipoProcessoSeletivo = tipoProcessoSeletivo;
	}

	@XmlElement(name = "tipoEnem")
	public Boolean getTipoEnem() {
		if (tipoEnem == null) {
			tipoEnem = Boolean.FALSE;
		}		
		return tipoEnem;
	}

	public void setTipoEnem(Boolean tipoEnem) {
		this.tipoEnem = tipoEnem;
	}

	@XmlElement(name = "tipoPortadorDiploma")
	public Boolean getTipoPortadorDiploma() {
		if (tipoPortadorDiploma == null) {
			tipoPortadorDiploma = Boolean.FALSE;
		}
		return tipoPortadorDiploma;
	}

	public void setTipoPortadorDiploma(Boolean tipoPortadorDiploma) {
		this.tipoPortadorDiploma = tipoPortadorDiploma;
	}

	@XmlElement(name = "uploadComprovanteEnem")
	public Boolean getUploadComprovanteEnem() {
		if (uploadComprovanteEnem == null) {
			uploadComprovanteEnem = Boolean.FALSE;
		}
		if (!getTipoEnem()) {
			uploadComprovanteEnem = Boolean.FALSE;
		}
		return uploadComprovanteEnem;
	}

	public void setUploadComprovanteEnem(Boolean uploadComprovanteEnem) {
		this.uploadComprovanteEnem = uploadComprovanteEnem;
	}

	@XmlElement(name = "uploadComprovantePortadorDiploma")
	public Boolean getUploadComprovantePortadorDiploma() {
		if (uploadComprovantePortadorDiploma == null) {
			uploadComprovantePortadorDiploma = Boolean.FALSE;
		}
		if (!getTipoPortadorDiploma()) {
			uploadComprovantePortadorDiploma = Boolean.FALSE;
		}
		return uploadComprovantePortadorDiploma;
	}

	public void setUploadComprovantePortadorDiploma(Boolean uploadComprovantePortadorDiploma) {
		this.uploadComprovantePortadorDiploma = uploadComprovantePortadorDiploma;
	}

	@XmlElement(name = "orientacaoUploadEnem")
	public String getOrientacaoUploadEnem() {
		if (orientacaoUploadEnem == null) {
			orientacaoUploadEnem = " ";
		}
		return orientacaoUploadEnem;
	}

	public void setOrientacaoUploadEnem(String orientacaoUploadEnem) {
		this.orientacaoUploadEnem = orientacaoUploadEnem;
	}

	@XmlElement(name = "orientacaoUploadPortadorDiploma")
	public String getOrientacaoUploadPortadorDiploma() {
		if (orientacaoUploadPortadorDiploma == null) {
			orientacaoUploadPortadorDiploma = " ";
		}
		
		return orientacaoUploadPortadorDiploma;
	}

	public void setOrientacaoUploadPortadorDiploma(String orientacaoUploadPortadorDiploma) {
		this.orientacaoUploadPortadorDiploma = orientacaoUploadPortadorDiploma;
	}
	
	@XmlElement(name = "tipoTransferencia")
	public Boolean getTipoTransferencia() {
		if(tipoTransferencia == null) {
			tipoTransferencia = Boolean.FALSE;
		}
		return tipoTransferencia;
	}

	public void setTipoTransferencia(Boolean tipoTransferencia) {
		this.tipoTransferencia = tipoTransferencia;
	}

	@XmlElement(name = "obrigarUploadComprovanteTransferencia")
	public Boolean getObrigarUploadComprovanteTransferencia() {
		if(obrigarUploadComprovanteTransferencia == null) {
			obrigarUploadComprovanteTransferencia = Boolean.FALSE;
		}
		if (!getTipoTransferencia()) {
			obrigarUploadComprovanteTransferencia = Boolean.FALSE;
		}
		return obrigarUploadComprovanteTransferencia;
	}

	public void setObrigarUploadComprovanteTransferencia(Boolean obrigarUploadComprovanteTransferencia) {
		this.obrigarUploadComprovanteTransferencia = obrigarUploadComprovanteTransferencia;
	}

	@XmlElement(name = "orientacaoTransferencia")
	public String getOrientacaoTransferencia() {
		if(orientacaoTransferencia == null) {
			orientacaoTransferencia = "";
		}
		return orientacaoTransferencia;
	}

	public void setOrientacaoTransferencia(String orientacaoTransferencia) {
		this.orientacaoTransferencia = orientacaoTransferencia;
	}

	@XmlElement(name = "realizarProvaOnline")
	public Boolean getRealizarProvaOnline() {
		if (realizarProvaOnline == null) {
			realizarProvaOnline = false;
		}
		return realizarProvaOnline;
	}

	public void setRealizarProvaOnline(Boolean realizarProvaOnline) {
		this.realizarProvaOnline = realizarProvaOnline;
	}

	@XmlElement(name = "tempoRealizacaoProvaOnline")
	public Integer getTempoRealizacaoProvaOnline() {
		if (tempoRealizacaoProvaOnline == null) {
			tempoRealizacaoProvaOnline = 0;
		}
		return tempoRealizacaoProvaOnline;
	}

	public void setTempoRealizacaoProvaOnline(Integer tempoRealizacaoProvaOnline) {
		this.tempoRealizacaoProvaOnline = tempoRealizacaoProvaOnline;
	}

	@XmlElement(name = "termoAceiteProva")
	public String getTermoAceiteProva() {
		if (termoAceiteProva == null) {
			termoAceiteProva = "";
		}
		return termoAceiteProva;
	}

	public void setTermoAceiteProva(String termoAceiteProva) {
		this.termoAceiteProva = termoAceiteProva;
	}

	@XmlElement(name = "utilizarAutenticacaoEmail")
	public Boolean getUtilizarAutenticacaoEmail() {
		if (utilizarAutenticacaoEmail == null) {
			utilizarAutenticacaoEmail = false;
		}
		return utilizarAutenticacaoEmail;
	}

	public void setUtilizarAutenticacaoEmail(Boolean utilizarAutenticacaoEmail) {
		this.utilizarAutenticacaoEmail = utilizarAutenticacaoEmail;
	}

	@XmlElement(name = "utilizarAutenticacaoSMS")
	public Boolean getUtilizarAutenticacaoSMS() {
		if (utilizarAutenticacaoSMS == null) {
			utilizarAutenticacaoSMS = false;
		}
		return utilizarAutenticacaoSMS;
	}

	public void setUtilizarAutenticacaoSMS(Boolean utilizarAutenticacaoSMS) {
		this.utilizarAutenticacaoSMS = utilizarAutenticacaoSMS;
	}

	@XmlElement(name = "listaTipoIngressoProcSeletivo")
	public List<SelectComboRSVO> getListaTipoIngressoProcSeletivo() {
		if(listaTipoIngressoProcSeletivo == null ) {
			listaTipoIngressoProcSeletivo= new ArrayList<SelectComboRSVO>(0);
		}
		return listaTipoIngressoProcSeletivo;
	}

	public void setListaTipoIngressoProcSeletivo(List<SelectComboRSVO> listaTipoIngressoProcSeletivo) {
		this.listaTipoIngressoProcSeletivo = listaTipoIngressoProcSeletivo;
	}
	
	
	public void montarListaTipoProcSeletivo() {
		 List<SelectComboRSVO> listaTipoProcSeletivo = new ArrayList<SelectComboRSVO>(0);
		if(getTipoProcessoSeletivo()) {
			listaTipoProcSeletivo.add(new SelectComboRSVO("PS","PROCESSO SELETIVO"));
		}
		if(getTipoEnem()) {
			listaTipoProcSeletivo.add(new SelectComboRSVO("EN", "ENEM/ANÁLISE DOCUMENTO"));	
						}
		if(getTipoPortadorDiploma()) {
			listaTipoProcSeletivo.add(new SelectComboRSVO("PD","PORTADOR DE DIPLOMA"));
		}
        if(getTipoTransferencia()) {
        	listaTipoProcSeletivo.add(new SelectComboRSVO("TR","TRANSFERÊNCIA"));
		}
        setListaTipoIngressoProcSeletivo(listaTipoProcSeletivo);
		}

	public Boolean getPermitirAlunosMatriculadosInscreverMesmoCurso() {
		if(permitirAlunosMatriculadosInscreverMesmoCurso == null ) {
			permitirAlunosMatriculadosInscreverMesmoCurso = Boolean.FALSE;
		}
		return permitirAlunosMatriculadosInscreverMesmoCurso;
	}

	public void setPermitirAlunosMatriculadosInscreverMesmoCurso(
			Boolean permitirAlunosMatriculadosInscreverMesmoCurso) {
		this.permitirAlunosMatriculadosInscreverMesmoCurso = permitirAlunosMatriculadosInscreverMesmoCurso;
	}
//	Criado Para Apresentação na aplicação do processo seletivo
	@XmlElement(name = "valorInscricaoApresentar")
	public BigDecimal getValorInscricaoApresentar() {
		if (valorInscricaoApresentar == null) {
			valorInscricaoApresentar = BigDecimal.ZERO;
		}
		return valorInscricaoApresentar;
	}

	public void setValorInscricaoApresentar(BigDecimal valorInscricaoApresentar) {
		this.valorInscricaoApresentar = valorInscricaoApresentar;
	}
	
	public Boolean getInformarDadosBancarios() {
		if (informarDadosBancarios == null) {
			informarDadosBancarios = false;
		}
		return informarDadosBancarios;
	}
	
	public void setInformarDadosBancarios(Boolean informarDadosBancarios) {
		this.informarDadosBancarios = informarDadosBancarios;
	}

//	public List<ProcSeletivoUnidadeEnsinoEixoCursoVO> getProcSeletivoUnidadeEnsinoEixoCursoVOs() {
//		if(procSeletivoUnidadeEnsinoEixoCursoVOs == null) {
//			procSeletivoUnidadeEnsinoEixoCursoVOs = new ArrayList<>(0);
//		}
//		return procSeletivoUnidadeEnsinoEixoCursoVOs;
//	}
//
//	public void setProcSeletivoUnidadeEnsinoEixoCursoVOs(
//			List<ProcSeletivoUnidadeEnsinoEixoCursoVO> procSeletivoUnidadeEnsinoEixoCursoVOs) {
//		this.procSeletivoUnidadeEnsinoEixoCursoVOs = procSeletivoUnidadeEnsinoEixoCursoVOs;
//	}
//
//	public List<PeriodoChamadaProcSeletivoVO> getPeriodoChamadaProcSeletivoVOs() {
//		if(periodoChamadaProcSeletivoVOs ==null ) {
//			periodoChamadaProcSeletivoVOs = new ArrayList<PeriodoChamadaProcSeletivoVO>(0);
//		}
//		return periodoChamadaProcSeletivoVOs;
//	}
//
//	public void setPeriodoChamadaProcSeletivoVOs(List<PeriodoChamadaProcSeletivoVO> periodoChamadaProcSeletivoVOs) {
//		this.periodoChamadaProcSeletivoVOs = periodoChamadaProcSeletivoVOs;
//	} 
			
}
