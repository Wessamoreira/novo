package negocio.comuns.biblioteca;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jakarta.faces. model.SelectItem;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.biblioteca.enumeradores.TipoMidiaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoExemplar;

/**
 * Reponsável por manter os dados da entidade Exemplar. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
// @Entity
// @Table(name = "Exemplar")
// @Analyzer
public class ExemplarVO extends SuperVO {

	// @Id
	// @DocumentId
	private Integer codigo;
	// @Transient
	// @IndexedEmbedded
	// @ManyToOne
	// @JoinColumn(name = "catalogo")
	private CatalogoVO catalogo;
	private String codigoBarra;
	private String situacaoAtual;
	private String estadoExemplar;
	private String edicao;
	private String subtitulo;
	private String mes;
	private String anovolume;
	private String nrEdicaoEspecial;
	private Boolean emprestarSomenteFinalDeSemana;
	private Boolean paraConsulta;
	private Integer nrNotaFiscal;
	private Double valorCompra;
	private Date dataCompra;
	private Date dataAquisicao;
	private Boolean exemplarUnidadeLogado;
	private Boolean desconsiderarReserva;
	private BibliotecaVO bibliotecaDestino;
	private List<LogTransferenciaBibliotecaExemplarVO> logTransferenciaBibliotecaExemplarVOs;
	

	
	/**
	 * Atributo responsável por manter os objetos da classe
	 * <code>HistoricoExemplar</code>.
	 */
	// @Transient
	private List<HistoricoExemplarVO> historicoExemplarVOs;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Biblioteca </code>.
	 */
	// @Transient
	private BibliotecaVO biblioteca;
	// @Field(index = Index.TOKENIZED, store = Store.YES)
	private String local;
	// @Transient
	private SecaoVO secao;
	// @Transient
	private Boolean reservarExemplar;
	private String tipoEntrada;
	private String tipoExemplar;
	// Atributos transientes para ajudar na tela de empréstimo
	// @Transient
	private String operacao;
	// @Transient
	private Boolean exemplarSelecionadoDeUmaReserva;
	private AssinaturaPeriodicoVO assinaturaPeriodico;
	private Integer numeroEdicao;
	private String tituloExemplar;
	// @Temporal(javax.persistence.TemporalType.DATE)
	private Date dataPublicacao;
	public static final long serialVersionUID = 1L;
	private Boolean exemplarSelecionado;
	private String volume;
	// @Transient
	private Integer numeroExemplar;
	private Integer bibliotecaAtual;
	private String abreviacaoTitulo;
	private String tipoMidia;
    private String link;
    private String fasciculos;
	private String anoPublicacao;	
    private Integer nrPaginas;
	private String isbn;
	private String issn;
	
	/**
	 * Construtor padrão da classe <code>Exemplar</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public ExemplarVO() {
		super();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>ExemplarVO</code>. Todos os tipos de consistência de dados são e
	 * devem ser implementadas neste método. São validações típicas: verificação
	 * de campos obrigatórios, verificação de valores válidos para os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(ExemplarVO obj, boolean periodico) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if ((obj.getBiblioteca() == null) || (obj.getBiblioteca().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo BIBLIOTECA (Exemplar) deve ser informado.");
		}
		if (obj.getCatalogo().getCodigo() == 0 && !periodico) {
			throw new ConsistirException("O campo CATÁLOGO (Exemplar) deve ser informado.");
		}
		if ((obj.getNumeroEdicao() == null || obj.getNumeroEdicao() == 0) && periodico) {
			throw new ConsistirException("O campo NÚMERO EDIÇÃO (Exemplar) deve ser informado.");
		}
		if (obj.getDataPublicacao() == null && periodico) {
			throw new ConsistirException("O campo DATA PUBLICAÇÃO (Exemplar) deve ser informado.");
		}
		if (obj.getMes().length() > 100) {
			throw new ConsistirException("O campo MES (Exemplar) não deve ter mais de 100 caracteres");
		}
		if (obj.getAnovolume().length() > 100) {
			throw new ConsistirException("O campo ANO/VOLUME (Exemplar) não deve ter mais de 100 caracteres.");
		}
	}

	public SelectItem getSelectItem() {
		return new SelectItem();
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	public void realizarUpperCaseDados() {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
		setCodigoBarra(getCodigoBarra().toUpperCase());
		setSituacaoAtual(getSituacaoAtual().toUpperCase());
		setEstadoExemplar(getEstadoExemplar().toUpperCase());
	}

	/**
	 * Operação responsável por adicionar um novo objeto da classe
	 * <code>HistoricoExemplarVO</code> ao List
	 * <code>historicoExemplarVOs</code>. Utiliza o atributo padrão de consulta
	 * da classe <code>HistoricoExemplar</code> - getSituacao() - como
	 * identificador (key) do objeto no List.
	 * 
	 * @param obj
	 *            Objeto da classe <code>HistoricoExemplarVO</code> que será
	 *            adiocionado ao Hashtable correspondente.
	 */
	public void adicionarObjHistoricoExemplarVOs(HistoricoExemplarVO obj) throws Exception {
		HistoricoExemplarVO.validarDados(obj);
		int index = 0;
		Iterator i = getHistoricoExemplarVOs().iterator();
		while (i.hasNext()) {
			HistoricoExemplarVO objExistente = (HistoricoExemplarVO) i.next();
			if (objExistente.getSituacao().equals(obj.getSituacao())) {
				getHistoricoExemplarVOs().set(index, obj);
				return;
			}
			index++;
		}
		getHistoricoExemplarVOs().add(obj);
	}

	/**
	 * Operação responsável por excluir um objeto da classe
	 * <code>HistoricoExemplarVO</code> no List
	 * <code>historicoExemplarVOs</code>. Utiliza o atributo padrão de consulta
	 * da classe <code>HistoricoExemplar</code> - getSituacao() - como
	 * identificador (key) do objeto no List.
	 * 
	 * @param situacao
	 *            Parâmetro para localizar e remover o objeto do List.
	 */
	public void excluirObjHistoricoExemplarVOs(String situacao) throws Exception {
		int index = 0;
		Iterator i = getHistoricoExemplarVOs().iterator();
		while (i.hasNext()) {
			HistoricoExemplarVO objExistente = (HistoricoExemplarVO) i.next();
			if (objExistente.getSituacao().equals(situacao)) {
				getHistoricoExemplarVOs().remove(index);
				return;
			}
			index++;
		}
	}

	/**
	 * Operação responsável por consultar um objeto da classe
	 * <code>HistoricoExemplarVO</code> no List
	 * <code>historicoExemplarVOs</code>. Utiliza o atributo padrão de consulta
	 * da classe <code>HistoricoExemplar</code> - getSituacao() - como
	 * identificador (key) do objeto no List.
	 * 
	 * @param situacao
	 *            Parâmetro para localizar o objeto do List.
	 */
	public HistoricoExemplarVO consultarObjHistoricoExemplarVO(String situacao) throws Exception {
		Iterator i = getHistoricoExemplarVOs().iterator();
		while (i.hasNext()) {
			HistoricoExemplarVO objExistente = (HistoricoExemplarVO) i.next();
			if (objExistente.getSituacao().equals(situacao)) {
				return objExistente;
			}
		}
		return null;
	}

	public String getSituacaoAtualApresentar() {
		return SituacaoExemplar.getDescricao(situacaoAtual);
	}

	/**
	 * Retorna o objeto da classe <code>Biblioteca</code> relacionado com (
	 * <code>Exemplar</code>).
	 */
	public BibliotecaVO getBiblioteca() {
		if (biblioteca == null) {
			biblioteca = new BibliotecaVO();
		}
		return (biblioteca);
	}

	/**
	 * Define o objeto da classe <code>Biblioteca</code> relacionado com (
	 * <code>Exemplar</code>).
	 */
	public void setBiblioteca(BibliotecaVO obj) {
		this.biblioteca = obj;
	}

	/**
	 * Retorna Atributo responsável por manter os objetos da classe
	 * <code>HistoricoExemplar</code>.
	 */
	public List<HistoricoExemplarVO> getHistoricoExemplarVOs() {
		if (historicoExemplarVOs == null) {
			historicoExemplarVOs = new ArrayList<HistoricoExemplarVO>(0);
		}
		return (historicoExemplarVOs);
	}

	/**
	 * Define Atributo responsável por manter os objetos da classe
	 * <code>HistoricoExemplar</code>.
	 */
	public void setHistoricoExemplarVOs(List<HistoricoExemplarVO> historicoExemplarVOs) {
		this.historicoExemplarVOs = historicoExemplarVOs;
	}

	public String getEstadoExemplar() {
		if (estadoExemplar == null) {
			estadoExemplar = "";
		}
		return (estadoExemplar);
	}

	public void setEstadoExemplar(String estadoExemplar) {
		this.estadoExemplar = estadoExemplar;
	}

	public String getSituacaoAtual() {
		if (situacaoAtual == null) {
			situacaoAtual = "";
		}
		return (situacaoAtual);
	}

	public void setSituacaoAtual(String situacaoAtual) {
		this.situacaoAtual = situacaoAtual;
	}

	public String getCodigoBarra() {
		if (codigoBarra == null) {
			codigoBarra = "";
		}
		return (codigoBarra);
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	public CatalogoVO getCatalogo() {
		if (catalogo == null) {
			catalogo = new CatalogoVO();
		}
		return catalogo;
	}

	public void setCatalogo(CatalogoVO catalogo) {
		this.catalogo = catalogo;
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

	public String getLocal() {
		if (local == null) {
			local = "";
		}
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public SecaoVO getSecao() {
		if (secao == null) {
			secao = new SecaoVO();
		}
		return secao;
	}

	public void setSecao(SecaoVO secao) {
		this.secao = secao;
	}

	public void setReservarExemplar(Boolean reservarExemplar) {
		this.reservarExemplar = reservarExemplar;
	}

	public Boolean getReservarExemplar() {
		if (reservarExemplar == null) {
			reservarExemplar = Boolean.FALSE;
		}
		return reservarExemplar;
	}

	public void setTipoEntrada(String tipoEntrada) {
		this.tipoEntrada = tipoEntrada;
	}

	public String getTipoEntrada() {
		if (tipoEntrada == null) {
			tipoEntrada = "";
		}
		return tipoEntrada;
	}

	public void setTipoExemplar(String tipoExemplar) {
		this.tipoExemplar = tipoExemplar;
	}

	public String getTipoExemplar() {
		if (tipoExemplar == null) {
			tipoExemplar = "";
		}
		return tipoExemplar;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}

	public String getOperacao() {
		if (operacao == null) {
			operacao = "";
		}
		return operacao;
	}

	public Boolean getExemplarSelecionadoDeUmaReserva() {
		if (exemplarSelecionadoDeUmaReserva == null) {
			exemplarSelecionadoDeUmaReserva = Boolean.FALSE;
		}
		return exemplarSelecionadoDeUmaReserva;
	}

	public void setExemplarSelecionadoDeUmaReserva(Boolean exemplarSelecionadoDeUmaReserva) {
		this.exemplarSelecionadoDeUmaReserva = exemplarSelecionadoDeUmaReserva;
	}

	public AssinaturaPeriodicoVO getAssinaturaPeriodico() {
		if (assinaturaPeriodico == null) {
			assinaturaPeriodico = new AssinaturaPeriodicoVO();
		}
		return assinaturaPeriodico;
	}

	public void setAssinaturaPeriodico(AssinaturaPeriodicoVO assinaturaPeriodico) {
		this.assinaturaPeriodico = assinaturaPeriodico;
	}

	public Integer getNumeroEdicao() {
		if (numeroEdicao == null) {
			numeroEdicao = 0;
		}
		return numeroEdicao;
	}

	public void setNumeroEdicao(Integer numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public String getTituloExemplar() {
		if (tituloExemplar == null) {
			tituloExemplar = "";
		}
		return tituloExemplar;
	}

	public void setTituloExemplar(String tituloExemplar) {
		this.tituloExemplar = tituloExemplar;
	}

	public Date getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(Date dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	public String getDataPublicacao_Apresentar() {
		return Uteis.getDataAno4Digitos(getDataPublicacao());
	}

	public String getDescricaoExemplar_Apresentar() {
		return getCatalogo().getTitulo() + " - " + getCodigo();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ExemplarVO other = (ExemplarVO) obj;
		if (this.codigo != other.codigo && (this.codigo == null || !this.codigo.equals(other.codigo))) {
			return false;
		}
		if ((this.codigoBarra == null) ? (other.codigoBarra != null) : !this.codigoBarra.equals(other.codigoBarra)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 29 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
		hash = 29 * hash + (this.codigoBarra != null ? this.codigoBarra.hashCode() : 0);
		return hash;
	}

	public Boolean getExemplarSelecionado() {
		if (exemplarSelecionado == null) {
			exemplarSelecionado = Boolean.TRUE;
		}
		return exemplarSelecionado;
	}

	public void setExemplarSelecionado(Boolean exemplarSelecionado) {
		this.exemplarSelecionado = exemplarSelecionado;
	}

	public String getVolume() {
		if (volume == null) {
			volume = "";
		}
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public Integer getNumeroExemplar() {
		if (numeroExemplar == null) {
			numeroExemplar = 0;
		}
		return numeroExemplar;
	}

	public void setNumeroExemplar(Integer numeroExemplar) {
		this.numeroExemplar = numeroExemplar;
	}

	public String getEdicao() {
		if (edicao == null) {
			edicao = "";
		}
		return edicao;
	}

	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}

	public String getSubtitulo() {
		if (subtitulo == null) {
			subtitulo = "";
		}
		return subtitulo;
	}

	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}

	public String getMes() {
		if (mes == null) {
			mes = "";
		}
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getAnovolume() {
		if (anovolume == null) {
			anovolume = "";
		}
		return anovolume;
	}

	public void setAnovolume(String anovolume) {
		this.anovolume = anovolume;
	}

	public String getNrEdicaoEspecial() {
		if (nrEdicaoEspecial == null) {
			nrEdicaoEspecial = "";
		}
		return nrEdicaoEspecial;
	}

	public void setNrEdicaoEspecial(String nrEdicaoEspecial) {
		this.nrEdicaoEspecial = nrEdicaoEspecial;
	}

	public Boolean getEmprestarSomenteFinalDeSemana() {
		if (emprestarSomenteFinalDeSemana == null) {
			emprestarSomenteFinalDeSemana = Boolean.FALSE;
		}
		return emprestarSomenteFinalDeSemana;
	}

	public void setEmprestarSomenteFinalDeSemana(Boolean emprestarSomenteFinalDeSemana) {
		this.emprestarSomenteFinalDeSemana = emprestarSomenteFinalDeSemana;
	}

	public Boolean getParaConsulta() {
		if (paraConsulta == null) {
			paraConsulta = Boolean.FALSE;
		}
		return paraConsulta;
	}

	public void setParaConsulta(Boolean paraConsulta) {
		this.paraConsulta = paraConsulta;
	}

	public Integer getNrNotaFiscal() {
		if (nrNotaFiscal == null) {
			nrNotaFiscal = 0;
		}
		return nrNotaFiscal;
	}

	public void setNrNotaFiscal(Integer nrNotaFiscal) {
		this.nrNotaFiscal = nrNotaFiscal;
	}

	

	public Double getValorCompra() {
		if (valorCompra == null) {
			valorCompra = 0.0;
		}
		return valorCompra;
	}

	public void setValorCompra(Double valorCompra) {
		this.valorCompra = valorCompra;
	}

	public Date getDataCompra() {
		if (dataCompra == null) {
			dataCompra = new Date();
		}
		return dataCompra;
	}

	public void setDataCompra(Date dataCompra) {
		this.dataCompra = dataCompra;
	}

	public Integer getBibliotecaAtual() {
		if (bibliotecaAtual == null) {
			bibliotecaAtual = 0;
		}
		return bibliotecaAtual;
	}

	public void setBibliotecaAtual(Integer bibliotecaAtual) {
		this.bibliotecaAtual = bibliotecaAtual;
	}

	public String getAbreviacaoTitulo() {
		if (abreviacaoTitulo == null) {
			abreviacaoTitulo = "";
		}
		return abreviacaoTitulo;
	}

	public void setAbreviacaoTitulo(String abreviacaoTitulo) {
		this.abreviacaoTitulo = abreviacaoTitulo;
	}

	public String getTipoMidia() {
		if (tipoMidia == null) {
			tipoMidia = TipoMidiaEnum.NAO_POSSUI.getKey();
		}
		return tipoMidia;
	}

	public void setTipoMidia(String tipoMidia) {
		this.tipoMidia = tipoMidia;
	}
	
	public String getLink() {
		if (link == null) {
			link = "";
		}
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getFasciculos() {
		if (fasciculos == null) {
			fasciculos = "";
		}
		return fasciculos;
	}

	public void setFasciculos(String fasciculos) {
		this.fasciculos = fasciculos;
	}

	public String getAnoPublicacao() {
		if (anoPublicacao == null) {
			anoPublicacao = "";
		}
		return anoPublicacao;
	}

	public void setAnoPublicacao(String anoPublicacao) {
		this.anoPublicacao = anoPublicacao;
	}
	
	public Integer getNrPaginas() {
		if (this.nrPaginas == null) {
			nrPaginas = 0;
		}

		return this.nrPaginas;
	}

	public void setNrPaginas(Integer nrPaginas) {
		this.nrPaginas = nrPaginas;
	}
	
	public String getIsbn() {
		if (isbn == null) {
			isbn = "";
		}
		return (isbn);
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getIssn() {
		if (issn == null) {
			issn = "";
		}
		return (issn);
	}

	public void setIssn(String issn) {
		this.issn = issn;
	}
	
	public Boolean getExemplarUnidadeLogado() {
		if (exemplarUnidadeLogado == null) {
			exemplarUnidadeLogado = Boolean.FALSE;
		}
		return exemplarUnidadeLogado;
	}

	public void setExemplarUnidadeLogado(Boolean exemplarUnidadeLogado) {
		this.exemplarUnidadeLogado = exemplarUnidadeLogado;
	}


	public BibliotecaVO getBibliotecaDestino() {
		if(bibliotecaDestino == null){
			bibliotecaDestino = new  BibliotecaVO();
		}
		return bibliotecaDestino;
	}

	public void setBibliotecaDestino(BibliotecaVO bibliotecaDestino) {
		this.bibliotecaDestino = bibliotecaDestino;
	}

	public List<LogTransferenciaBibliotecaExemplarVO> getLogTransferenciaBibliotecaExemplarVOs() {
		if(logTransferenciaBibliotecaExemplarVOs == null){
			logTransferenciaBibliotecaExemplarVOs = new ArrayList<LogTransferenciaBibliotecaExemplarVO>(0);
		}
		return logTransferenciaBibliotecaExemplarVOs;
	}

	public void setLogTransferenciaBibliotecaExemplarVOs(List<LogTransferenciaBibliotecaExemplarVO> logTransferenciaBibliotecaExemplarVOs) {
		this.logTransferenciaBibliotecaExemplarVOs = logTransferenciaBibliotecaExemplarVOs;
	}

	public String getCodigoHash() {
		return "BIB:"+getBiblioteca().getCodigo()+"TIT"+Uteis.removerAcentuacao(getTituloExemplar().trim().toUpperCase())+"SUB:"+Uteis.removerAcentuacao(getSubtitulo()).trim().toUpperCase()+"ED:"+Uteis.removerAcentuacao(getEdicao()).trim().toUpperCase()+"ANO"+Uteis.removerAcentuacao(getAnoPublicacao().trim().toUpperCase())+"VOL:"+Uteis.removerAcentuacao(getVolume()).trim().toUpperCase();
	}
	
	public String getCodigoHashComNumeroExemplar() throws Exception {
		return "BIB:"+(getBiblioteca().getNome()+getBiblioteca().getCodigo())+"TIT"+Uteis.removerAcentuacao(getTituloExemplar().trim().toUpperCase())+"SUB:"+Uteis.removerAcentuacao(getSubtitulo()).trim().toUpperCase()+"ED:"+Uteis.removerAcentuacao(getEdicao()).trim().toUpperCase()+"ANO"+Uteis.removerAcentuacao(getAnoPublicacao().trim().toUpperCase())+"VOL:"+Uteis.removerAcentuacao(getVolume()).trim().toUpperCase()+"NUM:"+Uteis.getMontarCodigoBarra(getNumeroExemplar().toString(), 9);		
	}

	public Boolean getDesconsiderarReserva() {
		if (desconsiderarReserva == null) {
			desconsiderarReserva = false;
		}
		return desconsiderarReserva;
	}

	public void setDesconsiderarReserva(Boolean desconsiderarReserva) {
		this.desconsiderarReserva = desconsiderarReserva;
	}

	public Date getDataAquisicao() {
		return dataAquisicao;
	}

	public void setDataAquisicao(Date dataAquisicao) {
		this.dataAquisicao = dataAquisicao;
	}

		
	
}
