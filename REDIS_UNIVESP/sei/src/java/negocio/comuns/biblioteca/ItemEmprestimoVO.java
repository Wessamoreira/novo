package negocio.comuns.biblioteca;

import java.util.Calendar;
import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoItemEmprestimo;
import negocio.facade.jdbc.biblioteca.Emprestimo;
import webservice.servicos.objetos.enumeradores.MesesAbreviadosAplicativoEnum;

/**
 * Reponsável por manter os dados da entidade ItemEmprestimo. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Emprestimo
 */
//@Entity
//@Table(name = "ItemEmprestimo")
//@Indexed
public class ItemEmprestimoVO extends SuperVO {

//	@Id
//	@DocumentId
	private Integer codigo;
//	@IndexedEmbedded
//	@ManyToOne
//	@JoinColumn(name = "emprestimo")
	private EmprestimoVO emprestimo;
//	@Field(index = Index.UN_TOKENIZED, store = Store.YES)
	private String situacao;
//	@Transient
	private UsuarioVO responsavelDevolucao;
//	@javax.persistence.Temporal(TemporalType.TIMESTAMP)
	private Date dataDevolucao;
//	@javax.persistence.Temporal(TemporalType.TIMESTAMP)
	private Date dataPrevisaoDevolucao;
	private Double valorMulta;
	private Double valorIsencao;
//	@Transient
	private Boolean itemEmprestimoSelecionado;
//	@javax.persistence.Temporal(TemporalType.TIMESTAMP)
	private Date dataPrimeiraNotificacao;
//	@javax.persistence.Temporal(TemporalType.TIMESTAMP)
	private Date dataSegundaNotificacao;
//	@javax.persistence.Temporal(TemporalType.TIMESTAMP)
	private Date dataTerceiraNotificacao;
	private Date dataPrevistaDevolucaoTemp;

//	@Transient
	private Boolean isentarCobrancaMulta;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Exemplar </code>.
	 */
//	@IndexedEmbedded
//	@ManyToOne
//	@JoinColumn(name = "exemplar")
	private ExemplarVO exemplar;
	private Boolean alterado;
	public static final long serialVersionUID = 8983558202217591746L;

//	@Transient
	private Boolean devolverEmprestimo;
//	@Transient
	private Boolean renovarEmprestimo;
//	@Transient
	private Boolean emprestar;
	private Boolean renovadoPeloSolicitante;
	private Integer bibliotecaDevolvida;
	private String tipoEmprestimo;
	private Integer horasEmprestimo;	
	private String mesAbreviado;
	private ContaReceberVO contaReceberVO;
	private String motivoIsencao;
	private String situacao_Multa;

	/**
	 * Construtor padrão da classe <code>ItemEmprestimo</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public ItemEmprestimoVO() {
		super();
	}
	
	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>ItemEmprestimoVO</code>. Todos os tipos de consistência de dados
	 * são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(ItemEmprestimoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if ((obj.getExemplar() == null) || (obj.getExemplar().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo EXEMPLAR (Itens Empréstimo) deve ser informado.");
		}
		if (obj.getDataPrevisaoDevolucao() == null) {
			throw new ConsistirException("O campo DATA PREVISÃO DEVOLUÇÃO (Itens Empréstimo) deve ser informado.");
		}
	}

	/**
	 * Retorna o objeto da classe <code>Exemplar</code> relacionado com (
	 * <code>ItemEmprestimo</code>).
	 */
	public ExemplarVO getExemplar() {
		if (exemplar == null) {
			exemplar = new ExemplarVO();
		}
		return (exemplar);
	}

	/**
	 * Define o objeto da classe <code>Exemplar</code> relacionado com (
	 * <code>ItemEmprestimo</code>).
	 */
	public void setExemplar(ExemplarVO obj) {
		this.exemplar = obj;
	}

	public Double getValorMulta() {
		if (valorMulta == null) {
			valorMulta = 0.0;
		}
		return (valorMulta);
	}

	public void setValorMulta(Double valorMulta) {
		this.valorMulta = valorMulta;
	}

	public Date getDataPrevisaoDevolucao() {
		if (dataPrevisaoDevolucao == null) {
			dataPrevisaoDevolucao = new Date();
		}
		return (dataPrevisaoDevolucao);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getDataPrevisaoDevolucao_Apresentar() {
		if(getTipoEmprestimo().equals("NO")) {
			return (Uteis.getData(dataPrevisaoDevolucao));
		}else {
			return (Uteis.getDataComHora(dataPrevisaoDevolucao));
		}
	}
	
	public void setDataPrevisaoDevolucao(Date dataPrevisaoDevolucao) {
		this.dataPrevisaoDevolucao = dataPrevisaoDevolucao;
	}

	public Date getDataDevolucao() {
		return (dataDevolucao);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getDataDevolucao_Apresentar() {
		return (Uteis.getData(dataDevolucao));
	}

	public void setDataDevolucao(Date dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return (situacao);
	}
	
	public Boolean getHabilitarBotaoRenovar(){
		if(Uteis.nrDiasEntreDatas(dataPrevisaoDevolucao, new Date()) < 3 ){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo
	 * com um domínio específico. Com base no valor de armazenamento do atributo
	 * esta função é capaz de retornar o de apresentação correspondente. Útil
	 * para campos como sexo, escolaridade, etc.
	 */
	public String getSituacao_Apresentar() {
		return SituacaoItemEmprestimo.getDescricao(situacao);
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public EmprestimoVO getEmprestimo() {
		if (emprestimo == null) {
			emprestimo = new EmprestimoVO();
		}
		return (emprestimo);
	}

	public void setEmprestimo(EmprestimoVO emprestimo) {
		this.emprestimo = emprestimo;
	}

	public void setResponsavelDevolucao(UsuarioVO responsavelDevolucao) {
		this.responsavelDevolucao = responsavelDevolucao;
	}

	public UsuarioVO getResponsavelDevolucao() {
		if (responsavelDevolucao == null) {
			responsavelDevolucao = new UsuarioVO();
		}
		return responsavelDevolucao;
	}

	public void setItemEmprestimoSelecionado(Boolean itemEmprestimoSelecionado) {
		this.itemEmprestimoSelecionado = itemEmprestimoSelecionado;
	}

	public Boolean getItemEmprestimoSelecionado() {
		if (itemEmprestimoSelecionado == null) {
			itemEmprestimoSelecionado = Boolean.FALSE;
		}
		return itemEmprestimoSelecionado;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Boolean getAlterado() {
		if (alterado == null) {
			alterado = false;
		}
		return alterado;
	}

	public void setAlterado(Boolean alterado) {
		this.alterado = alterado;
	}

	public Boolean getDevolverEmprestimo() {
		if (devolverEmprestimo == null) {
			devolverEmprestimo = false;
		}
		return devolverEmprestimo;
	}

	public void setDevolverEmprestimo(Boolean devolverEmprestimo) {
		this.devolverEmprestimo = devolverEmprestimo;
	}

	public Boolean getEmprestar() {
		if (emprestar == null) {
			emprestar = false;
		}
		return emprestar;
	}

	public void setEmprestar(Boolean emprestar) {
		this.emprestar = emprestar;
	}

	public Boolean getRenovarEmprestimo() {
		if (renovarEmprestimo == null) {
			renovarEmprestimo = false;
		}
		return renovarEmprestimo;
	}

	public void setRenovarEmprestimo(Boolean renovarEmprestimo) {
		this.renovarEmprestimo = renovarEmprestimo;
	}

	public Boolean getIsentarCobrancaMulta() {
		if (isentarCobrancaMulta == null) {
			isentarCobrancaMulta = false;
		}
		return isentarCobrancaMulta;
	}

	public void setIsentarCobrancaMulta(Boolean isentarCobrancaMulta) {
		this.isentarCobrancaMulta = isentarCobrancaMulta;
	}

	public Boolean getEmprestimoEmAtraso() {
		return Uteis.getDataComHoraSetadaParaUltimoMinutoDia(getDataPrevisaoDevolucao()).compareTo(new Date()) < 0;
	}

	public Date getDataPrimeiraNotificacao() {
		return dataPrimeiraNotificacao;
	}

	public void setDataPrimeiraNotificacao(Date dataPrimeiraNotificacao) {
		this.dataPrimeiraNotificacao = dataPrimeiraNotificacao;
	}

	public Date getDataSegundaNotificacao() {
		return dataSegundaNotificacao;
	}

	public void setDataSegundaNotificacao(Date dataSegundaNotificacao) {
		this.dataSegundaNotificacao = dataSegundaNotificacao;
	}

	public Date getDataTerceiraNotificacao() {
		return dataTerceiraNotificacao;
	}

	public void setDataTerceiraNotificacao(Date dataTerceiraNotificacao) {
		this.dataTerceiraNotificacao = dataTerceiraNotificacao;
	}

	public Boolean getIsEmprestimoAtrasado()  throws Exception{
		return  !getCodigo().equals(0) && ((getTipoEmprestimo().equals("NO") && Uteis.getDateSemHora(getDataPrevisaoDevolucao()).compareTo(Uteis.getDateSemHora(new Date())) < 0) || (!getTipoEmprestimo().equals("NO") && Uteis.getDataComHora(getDataPrevisaoDevolucao()).compareTo(Uteis.getDataComHora(new Date()) ) < 0)) ;
	}

	public Double getValorIsencao() {
		if (valorIsencao == null) {
			valorIsencao = 0.0;
		}
		return valorIsencao;
	}

	public void setValorIsencao(Double valorIsencao) {
		this.valorIsencao = valorIsencao;
	}

	public Date getDataPrevistaDevolucaoTemp() {
		if (dataPrevistaDevolucaoTemp == null) {
			dataPrevistaDevolucaoTemp = getDataPrevisaoDevolucao();
		}
		return dataPrevistaDevolucaoTemp;
	}
	
	public String getDataPrevistaDevolucaoTemp_Apresentar() {
		if(getTipoEmprestimo().equals("NO")) {
			return (Uteis.getData(dataPrevistaDevolucaoTemp));
		}else {
			return (Uteis.getDataComHora(dataPrevistaDevolucaoTemp));
		}
	}
	
	public String getDataPrevistaDevolucaoTemp_comHora() {
		return (Uteis.getDataComHora(dataPrevistaDevolucaoTemp));
	}
	
	public void setDataPrevistaDevolucaoTemp(Date dataPrevistaDevolucaoTemp) {
		this.dataPrevistaDevolucaoTemp = dataPrevistaDevolucaoTemp;
	}

	public Boolean getRenovadoPeloSolicitante() {
		if(renovadoPeloSolicitante == null){
			renovadoPeloSolicitante = Boolean.FALSE;
		}
		return renovadoPeloSolicitante;
	}

	public void setRenovadoPeloSolicitante(Boolean renovadoPeloSolicitante) {
		this.renovadoPeloSolicitante = renovadoPeloSolicitante;
	}

	public Integer getBibliotecaDevolvida() {
		if (bibliotecaDevolvida == null) {
			bibliotecaDevolvida = 0;
		}
		return bibliotecaDevolvida;
	}

	public void setBibliotecaDevolvida(Integer bibliotecaDevolvida) {
		this.bibliotecaDevolvida = bibliotecaDevolvida;
	}

	public String getTipoEmprestimo() {
		if (tipoEmprestimo == null) {
			tipoEmprestimo = "NO";
		}
		return tipoEmprestimo;
	}

	public void setTipoEmprestimo(String tipoEmprestimo) {
		this.tipoEmprestimo = tipoEmprestimo;
	}

	public Integer getHorasEmprestimo() {
		if (horasEmprestimo == null) {
			horasEmprestimo = 1;
		}
		return horasEmprestimo;
	}

	public void setHorasEmprestimo(Integer horasEmprestimo) {
		this.horasEmprestimo = horasEmprestimo;
	}
	
	public String getCssTimeLineFichaAluno() {
		if (getSituacao().equals(SituacaoItemEmprestimo.EM_EXECUCAO.getValor()) && getDataPrevisaoDevolucao().compareTo(new Date()) < 0) {
			return "timelineItemEmprestimo-badge-atrasado";
		} else if (getSituacao().equals(SituacaoItemEmprestimo.EM_EXECUCAO.getValor()) && getDataPrevisaoDevolucao().compareTo(new Date()) >= 0) {
			return "timelineItemEmprestimo-badge-emprestado";
		} else if (getSituacao().equals(SituacaoItemEmprestimo.DEVOLVIDO.getValor())) {
			return "timelineItemEmprestimo-badge-devolvido";
		} 
		return "timelineItemEmprestimo-badge";
	}
	
	public String getMesAbreviadoAnoDataEmprestimo() {
		if (mesAbreviado == null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(getEmprestimo().getData());
			mesAbreviado = MesesAbreviadosAplicativoEnum.getEnumValor(calendar.get(Calendar.MONTH) + 1).getValor();
		}
		return mesAbreviado + "/" + Uteis.getAno2Digitos(getEmprestimo().getData());
	}

	public String getMesAbreviado() {
		if (mesAbreviado == null) {
			mesAbreviado = "";
		}
		return mesAbreviado;
	}

	public void setMesAbreviado(String mesAbreviado) {
		this.mesAbreviado = mesAbreviado;
	}
	
	public String getSituacao_Apresentar_FichaAluno() {
		if (getSituacao().equals(SituacaoItemEmprestimo.EM_EXECUCAO.getValor()) && getDataPrevisaoDevolucao().before(new Date()) && !Uteis.getData(getDataPrevisaoDevolucao()).equals(Uteis.getData(new Date()))) {
			return "Atrasado";
		}
		if (getSituacao().equals(SituacaoItemEmprestimo.EM_EXECUCAO.getValor()) && (!getDataPrevisaoDevolucao().before(new Date()) || Uteis.getData(getDataPrevisaoDevolucao()).equals(Uteis.getData(new Date())))) {
			return "Emprestado";
		}
		if (getSituacao().equals(SituacaoItemEmprestimo.DEVOLVIDO.getValor())) {
			return "Devolvido";
		}
		if (getSituacao().equals(SituacaoItemEmprestimo.RENOVADO.getValor())) {
			return "Renovado";
		}
		return SituacaoItemEmprestimo.EM_EXECUCAO.getValor();
	}
	
	public ContaReceberVO getContaReceberVO() {
		if (contaReceberVO == null) {
			contaReceberVO = new ContaReceberVO();
		}
		return contaReceberVO;
	}
	
	public void setContaReceberVO(ContaReceberVO contaReceberVO) {
		this.contaReceberVO = contaReceberVO;
	}

	public String getMotivoIsencao() {
		
		if (motivoIsencao == null) {
			motivoIsencao = "";
		}
		return motivoIsencao;
	}

	public void setMotivoIsencao(String motivoIsencao) {
		this.motivoIsencao = motivoIsencao;
	}

	public String getSituacao_Multa() {
		if (situacao_Multa == null) {
			situacao_Multa = "";
		}
		return situacao_Multa;
	}

	public void setSituacao_Multa(String situacao_Multa) {
		this.situacao_Multa = situacao_Multa;
	}
	

	
	
}
