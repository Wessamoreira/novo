package negocio.comuns.processosel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.processosel.enumeradores.EscopoPerguntaEnum;
import negocio.comuns.processosel.enumeradores.TipoLayoutApresentacaoResultadoPerguntaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;


/**
 * Reponsável por manter os dados da entidade Pergunta. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "pergunta")
public class PerguntaVO extends SuperVO {

    protected Integer codigo;
    protected String descricao;
    private String tipoResposta;
    private Integer peso;
    private String texto;
    private String respostaTextual;
    public Boolean apresentarRespostaTextual;
    private Integer quantidadeCasasDecimais;
    private String mascaraData;
    private String mascaraHora;
    private String orientacoesSobreCampo;
    private String extensaoTipoResposta;
    
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>RespostaPergunta</code>.
     */
    private List<RespostaPerguntaVO> respostaPerguntaVOs;
    private Boolean selecionado;
    private Double mediaRespostas;
    private String layoutPergunta;
    private TipoLayoutApresentacaoResultadoPerguntaEnum tipoResultadoGrafico;
    private EscopoPerguntaEnum escopoPergunta;
    private Boolean perguntaEscopoPublico;
    
    private List<PerguntaItemVO> perguntaItemVOs;
    private List<PerguntaChecklistVO> listaPerguntaChecklistVO;
    public static final long serialVersionUID = 1L;
    
    /**
     * Transiente
     */
    private String textoAdicional;
    private Double campoNumerico;
    private Date campoData;
    private String campoHora;
    private Boolean campoVerdadeiroFalso;

    /**
     * Construtor padrão da classe <code>Pergunta</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public PerguntaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>PerguntaVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(PerguntaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getDescricao().equals("")) {
            throw new ConsistirException("O campo DESCRIÇÃO (Pergunta) deve ser informado.");
        }
        if (obj.getTipoResposta().equals("")) {
            throw new ConsistirException("O campo TIPO DE RESPOSTA (Pergunta) deve ser informado.");
        }
        if ((obj.getTipoRespostaMultiplaEscolha() || obj.getTipoRespostaSimplesEscolha() || obj.getTipoRespostaListaSuspensa())
                && obj.getRespostaPerguntaVOs().size() < 2) {
            throw new ConsistirException("O campo RESPOSTA (Pergunta) deve ter no mínimo duas opções.");
        }
        if(obj.getTipoRespostaListaCampos() && !Uteis.isAtributoPreenchido(obj.getPerguntaItemVOs())) {
        	throw new ConsistirException(" Deve ser adicionada ao menos uma pergunta  na listagem de perguntas.");
        }
        if(obj.getTipoRespostaUpload() && !Uteis.isAtributoPreenchido(obj.getExtensaoTipoResposta())) {
        	throw new ConsistirException(" O campo Extensão (Pergunta) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setDescricao("");
        setTipoResposta("");
        setPeso(0);
        setTexto("");
        setSelecionado(false);
    }

//    public String getResposta(Boolean validarImportanciaPergunta, Boolean obrigarResposta) throws ConsistirException {
//        if (getPeso().equals(0) && validarImportanciaPergunta) {
//            throw new ConsistirException("A importância da pergunta ''" + getDescricao() + "'' deve ser informada.");
//        }
//        if (!getTipoRespostaTextual()) {
//            setTexto("");
//            Iterator j = getRespostaPerguntaVOs().iterator();
//            while (j.hasNext()) {
//                RespostaPerguntaVO objExistenteResposta = (RespostaPerguntaVO) j.next();
//                if (objExistenteResposta.getSelecionado()) {
//                    setTexto(getTexto() + "[" + objExistenteResposta.getCodigo() + "]");
//                }
//            }
//        }
//        if (getTexto().equals("") && obrigarResposta) {
//            throw new ConsistirException("A pergunta ''" + getDescricao() + "'' deve ser respondida.");
//        }
//        return getTexto();
//    }
    public void limparResposta() {
        if (!getTipoRespostaTextual()) {
            for(RespostaPerguntaVO objExistenteResposta:getRespostaPerguntaVOs()){
                objExistenteResposta.setSelecionado(false);
            }
        }
        setTexto("");
        setPeso(0);
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>RespostaPerguntaVO</code> ao List <code>respostaPerguntaVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>RespostaPergunta</code> - getDescricao() - como identificador (key)
     * do objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>RespostaPerguntaVO</code> que será
     *            adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjRespostaPerguntaVOs(RespostaPerguntaVO obj) throws Exception {
        RespostaPerguntaVO.validarDados(obj);
        int index = 0;
        for(RespostaPerguntaVO objExistente:getRespostaPerguntaVOs()){
            if (objExistente.getDescricao().equals(obj.getDescricao())) {
                getRespostaPerguntaVOs().set(index, obj);
                                
                return;
            }
            index++;
        }
        
        getRespostaPerguntaVOs().add(obj);
        realizaReorganizacaoOrdemRespostaPergunta();
    }
    
    public void realizaReorganizacaoOrdemRespostaPergunta(){
    	int index = 1;
    	for(RespostaPerguntaVO objExistente:getRespostaPerguntaVOs()){
            objExistente.setOrdem(index++);                       
        }
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>RespostaPerguntaVO</code> no List <code>respostaPerguntaVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>RespostaPergunta</code> - getDescricao() - como identificador (key)
     * do objeto no List.
     *
     * @param descricao
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjRespostaPerguntaVOs(String descricao) throws Exception {
        int index = 0;
        for(RespostaPerguntaVO objExistente:getRespostaPerguntaVOs()){
            if (objExistente.getDescricao().equals(descricao)) {
                getRespostaPerguntaVOs().remove(index);
                realizaReorganizacaoOrdemRespostaPergunta();
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>RespostaPerguntaVO</code> no List <code>respostaPerguntaVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>RespostaPergunta</code> - getDescricao() - como identificador (key)
     * do objeto no List.
     *
     * @param descricao
     *            Parâmetro para localizar o objeto do List.
     */
    public RespostaPerguntaVO consultarObjRespostaPerguntaVO(String descricao) throws Exception {
    	for(RespostaPerguntaVO objExistente:getRespostaPerguntaVOs()){
            if (objExistente.getDescricao().equals(descricao)) {
                return objExistente;
            }
        }
        return null;
    }

    public void setarValorFalsoSimplesEscolha(Integer selecionada) {
    	for(RespostaPerguntaVO objExistente:getRespostaPerguntaVOs()){
            if (!objExistente.getCodigo().equals(selecionada)) {
                objExistente.setSelecionado(false);

            }
        }

    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>RespostaPergunta</code>.
     */
    @XmlElement(name = "respostaPerguntaVOs")
    public List<RespostaPerguntaVO> getRespostaPerguntaVOs() {
        if (respostaPerguntaVOs == null) {
            respostaPerguntaVOs = new ArrayList<RespostaPerguntaVO>(0);
        }
        return (respostaPerguntaVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>RespostaPergunta</code>.
     */
    public void setRespostaPerguntaVOs(List<RespostaPerguntaVO> respostaPerguntaVOs) {
        this.respostaPerguntaVOs = respostaPerguntaVOs;
    }

    @XmlElement(name = "tipoResposta")
    public String getTipoResposta() {
        if (tipoResposta == null) {
            tipoResposta = "";
        }
        return (tipoResposta);
    }

    @XmlElement(name = "pesoApresentar")
    public String getPeso_Apresentar() {
        if (getPeso().equals(0)) {
            return "";
        }
        if (getPeso().equals(1)) {
            return "Baixíssima";
        }
        if (getPeso().equals(2)) {
            return "Baixa";
        }
        if (getPeso().equals(3)) {
            return "Média";
        }
        if (getPeso().equals(4)) {
            return "Alta";
        }
        if (getPeso().equals(5)) {
            return "Altíssima";
        }
        return getPeso().toString();
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    @XmlElement(name = "tipoRespostaApresentar")
    public String getTipoResposta_Apresentar() {
        if (getTipoResposta().equals("ME")) {
            return "Múltipla-Escolha";
        }
        if (getTipoResposta().equals("SE")) {
            return "Simples Escolha";
        }
        if (getTipoResposta().equals("TE")) {
            return "Textual";
        }
        if (getTipoResposta().equals("NU")) {
            return "Númerico";
        }
        if (getTipoResposta().equals("DT")) {
            return "Data";
        }
        if (getTipoResposta().equals("HR")) {
            return "Hora";
        }
        if (getTipoResposta().equals("LC")) {
            return "Lista Campos";
        }
        if (getTipoResposta().equals("LS")) {
            return "Lista Suspensa";
        }
        if (getTipoResposta().equals("VF")) {
            return "Verdadeiro ou Falso";
        }
        if (getTipoResposta().equals("UP")) {
        	return "Upload";
        }
        return (getTipoResposta());
    }
    
    @XmlElement(name = "apresentarRespostaTextual")
    public Boolean getApresentarRespostaTextual() {
    	return this.apresentarRespostaTextual;
    }
    
    public void setApresentarRespostaTextual(Boolean apresentarRespostaTextual){
    	this.apresentarRespostaTextual = apresentarRespostaTextual;
    }

    public void setTipoResposta(String tipoResposta) {
        this.tipoResposta = tipoResposta;
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
        if (codigo == null){
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @XmlElement(name = "tipoRespostaTextual")
    public Boolean getTipoRespostaTextual() {
        if (getTipoResposta().equals("TE")) {
            return true;
        }
        return false;
    }

    @XmlElement(name = "tipoRespostaMultiplaEscolha")
    public Boolean getTipoRespostaMultiplaEscolha() {
        if (getTipoResposta().equals("ME")) {
            return true;
        }
        return false;
    }
     
    @XmlElement(name = "tipoRespostaSimplesEscolha")
    public Boolean getTipoRespostaSimplesEscolha() {
        if (getTipoResposta().equals("SE")) {
            return true;
        }
        return false;
    }
    @XmlElement(name = "tipoRespostaNumerico")
    public Boolean getTipoRespostaNumerico() {
        if (getTipoResposta().equals("NU")) {
            return true;
        }
        return false;
    }
    
    @XmlElement(name = "tipoRespostaData")
    public Boolean getTipoRespostaData() {
        if (getTipoResposta().equals("DT")) {
            return true;
        }
        return false;
    }
    
    @XmlElement(name = "tipoRespostaHora")
    public Boolean getTipoRespostaHora() {
        if (getTipoResposta().equals("HR")) {
            return true;
        }
        return false;
    }
    
    @XmlElement(name = "tipoRespostaListaCampos")
    public Boolean getTipoRespostaListaCampos() {
        if (getTipoResposta().equals("LC")) {
            return true;
        }
        return false;
    }
    
    @XmlElement(name = "tipoRespostaListaSuspensa")
    public Boolean getTipoRespostaListaSuspensa() {
        if (getTipoResposta().equals("LS")) {
            return true;
        }
        return false;
    }
    
    @XmlElement(name = "tipoRespostaVerdadeiroFalso")
    public Boolean getTipoRespostaVerdadeiroFalso() {
        if (getTipoResposta().equals("VF")) {
            return true;
        }
        return false;
    }
    
    @XmlElement(name = "tipoRespostaUpload")
    public Boolean getTipoRespostaUpload() {
    	if (getTipoResposta().equals("UP")) {
    		return true;
    	}
    	return false;
    }

    @XmlElement(name = "texto")
    public String getTexto() {
        if (texto == null){
            texto = "";
        }
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * @return the peso
     */
    @XmlElement(name = "peso")
    public Integer getPeso() {
		if (peso == null) {
    		peso = 0;
    	}
        return peso;
    }

    /**
     * @param peso
     *            the peso to set
     */
    public void setPeso(Integer peso) {
        this.peso = peso;
    }

    /**
     * @return the selecionado
     */
    @XmlElement(name = "selecionado")
    public Boolean getSelecionado() {
        return selecionado;
    }

    /**
     * @param selecionado
     *            the selecionado to set
     */
    public void setSelecionado(Boolean selecionado) {
        this.selecionado = selecionado;
    }

    public void calcularMediaAvaliacaoInstitucionalResposta() throws Exception {
        if (!getRespostaPerguntaVOs().isEmpty()) {
            int soma = 0;
            int qtdeRespostaPerguntaVOs = getRespostaPerguntaVOs().size();
            for (RespostaPerguntaVO obj : getRespostaPerguntaVOs()) {
                soma += obj.getQtdeRespostas();
            }
            setMediaRespostas(Uteis.arredondar((soma / (qtdeRespostaPerguntaVOs * 1.0)), 2, 0));
        }
    }

    @XmlElement(name = "mediaRespostas")
    public Double getMediaRespostas() {
        if (mediaRespostas == null) {
            mediaRespostas = 0.0;
        }
        return mediaRespostas;
    }

    public void setMediaRespostas(Double mediaRespostas) {
        this.mediaRespostas = mediaRespostas;
    }

    /**
     * @return the layoutPergunta
     */
    @XmlElement(name = "layoutPergunta")
    public String getLayoutPergunta() {
        if (layoutPergunta == null) {
            layoutPergunta = "";
        }
        return layoutPergunta;
    }

    /**
     * @param layoutPergunta the layoutPergunta to set
     */
    public void setLayoutPergunta(String layoutPergunta) {
        this.layoutPergunta = layoutPergunta;
    }

    @XmlElement(name = "layoutHorizontal")
    public boolean getLayoutHorizontal() {
        if (getLayoutPergunta().equals("HO")) {
            return true;
        } else {
            return false;
        }
    }

    public Integer getTamanhoRespostaPerguntaVOs() {
//        //System.out.print(" ------------------ ");
//        //System.out.print(" " + getRespostaPerguntaVOs().size());
//        //System.out.print(" ------------------ ");
        return getRespostaPerguntaVOs().size();
    }

	public TipoLayoutApresentacaoResultadoPerguntaEnum getTipoResultadoGrafico() {
		if(tipoResultadoGrafico == null){
			if(getTipoRespostaSimplesEscolha()){
				tipoResultadoGrafico = TipoLayoutApresentacaoResultadoPerguntaEnum.GRAFICO_PIZZA;
			}
			if(getTipoRespostaMultiplaEscolha()){
				tipoResultadoGrafico = TipoLayoutApresentacaoResultadoPerguntaEnum.GRAFICO_COLUNA;
			}
		}
		return tipoResultadoGrafico;
	}

	public void setTipoResultadoGrafico(TipoLayoutApresentacaoResultadoPerguntaEnum tipoResultadoGrafico) {
		this.tipoResultadoGrafico = tipoResultadoGrafico;
	}

	public Integer getNumeroOpcoes(){
		return getRespostaPerguntaVOs().size();
	}

	@XmlElement(name = "textoAdicional")
	public String getTextoAdicional() {
		if(textoAdicional == null){
			textoAdicional = "";
		}
		return textoAdicional;
	}

	public void setTextoAdicional(String textoAdicional) {
		this.textoAdicional = textoAdicional;
	}

	@XmlElement(name = "respostaTextual")
	public String getRespostaTextual() {
		if(respostaTextual == null){
			respostaTextual = "";
		}
		return respostaTextual;
	}

	public void setRespostaTextual(String respostaTextual) {
		this.respostaTextual = respostaTextual;
	}

	@XmlElement(name = "escopoPergunta")
	public EscopoPerguntaEnum getEscopoPergunta() {
		if(escopoPergunta == null){
			escopoPergunta = EscopoPerguntaEnum.AVALIACAO_INSTITUCIONAL;
		}
		return escopoPergunta;
	}

	public void setEscopoPergunta(EscopoPerguntaEnum escopoPergunta) {
		this.escopoPergunta = escopoPergunta;
	}

	@XmlElement(name = "perguntaEscopoPublico")
	public Boolean getPerguntaEscopoPublico() {
		if(perguntaEscopoPublico == null){
			perguntaEscopoPublico = false;
		}
		return perguntaEscopoPublico;
	}

	public void setPerguntaEscopoPublico(Boolean perguntaEscopoPublico) {
		this.perguntaEscopoPublico = perguntaEscopoPublico;
	}

	@XmlElement(name = "quantidadeCasasDecimais")
	public Integer getQuantidadeCasasDecimais() {
		if(quantidadeCasasDecimais == null) {
			quantidadeCasasDecimais = 0;
		}
		return quantidadeCasasDecimais;
	}

	public void setQuantidadeCasasDecimais(Integer quantidadeCasasDecimais) {
		this.quantidadeCasasDecimais = quantidadeCasasDecimais;
	}
	

	public String getExtensaoTipoResposta() {
		if (extensaoTipoResposta == null) {
			extensaoTipoResposta = "";
		}
		return extensaoTipoResposta;
	}

	public void setExtensaoTipoResposta(String extensaoTipoResposta) {
		this.extensaoTipoResposta = extensaoTipoResposta;
	}

	@XmlElement(name = "mascaraData")
	public String getMascaraData() {
		if(mascaraData == null) {
			mascaraData = "";
		}
		return mascaraData;
	}

	public void setMascaraData(String mascaraData) {
		this.mascaraData = mascaraData;
	}

	@XmlElement(name = "mascaraHora")
	public String getMascaraHora() {
		if(mascaraHora == null) {
			mascaraHora = "";
		}
		return mascaraHora;
	}

	public void setMascaraHora(String mascaraHora) {
		this.mascaraHora = mascaraHora;
	}		

	public String getOrientacoesSobreCampo() {
		if(orientacoesSobreCampo == null) {
			orientacoesSobreCampo = "";
		}
		return orientacoesSobreCampo;
	}

	public void setOrientacoesSobreCampo(String orientacoesSobreCampo) {
		this.orientacoesSobreCampo = orientacoesSobreCampo;
	}

	/**
	 * Retorna Atributo responsável por manter os objetos da classe
	 * <code>PerguntaItem</code>.
	 */
	@XmlElement(name = "perguntaItemVOs")
	public List<PerguntaItemVO> getPerguntaItemVOs() {
		if(perguntaItemVOs == null) {
			perguntaItemVOs = new ArrayList<PerguntaItemVO>(0);
		}
		return perguntaItemVOs;
	}

	/**
	 * Define Atributo responsável por manter os objetos da classe
	 * <code>PerguntaItem</code>.
	 */
	public void setPerguntaItemVOs(List<PerguntaItemVO> perguntaItemVOs) {
		this.perguntaItemVOs = perguntaItemVOs;
	}
	
	

	public List<PerguntaChecklistVO> getListaPerguntaChecklistVO() {
		if (listaPerguntaChecklistVO == null) {
			listaPerguntaChecklistVO = new ArrayList<>();
		}
		return listaPerguntaChecklistVO;
	}

	public void setListaPerguntaChecklistVO(List<PerguntaChecklistVO> listaPerguntaChecklistVO) {
		this.listaPerguntaChecklistVO = listaPerguntaChecklistVO;
	}

	public Double getCampoNumerico() {
		if(campoNumerico == null) {
			campoNumerico = 0.0;
		}
		return campoNumerico;
	}

	public void setCampoNumerico(Double campoNumerico) {
		this.campoNumerico = campoNumerico;
	}

	public Date getCampoData() {		
		return campoData;
	}

	public void setCampoData(Date campoData) {
		this.campoData = campoData;
	}

	public String getCampoHora() {
		if(campoHora == null) {
			campoHora = "";
		}
		return campoHora;
	}

	public void setCampoHora(String campoHora) {
		this.campoHora = campoHora;
	}

	public Boolean getCampoVerdadeiroFalso() {
		if(campoVerdadeiroFalso == null) {
			campoVerdadeiroFalso = Boolean.FALSE;
		}
		return campoVerdadeiroFalso;
	}

	public void setCampoVerdadeiroFalso(Boolean campoVerdadeiroFalso) {
		this.campoVerdadeiroFalso = campoVerdadeiroFalso;
	}	
	
	public Integer getNumeroOpcoesPerguntas() {
		return getPerguntaItemVOs().size();
	}
	

//	public Boolean getPerguntaPendente(){
//		return getTexto().trim().isEmpty();
//	}
	
	public String getMascaraHoraApresentar() {
		if(getMascaraHora().equals("HH:mm:ss")) {
			return "99:99:99";
		}
		else if(getMascaraHora().equals("HH:mm")) {
			return "99:99";
		}
		else return "99";
	}
	
	public String getCssPergunta() {
    	if(getRespostaPerguntaVOs().size() <= 2) {
    		return "col-md-6";
    	}else if(getRespostaPerguntaVOs().size() == 3){
    		return "col-md-4";
    	} return "col-md-3";
    }

}
