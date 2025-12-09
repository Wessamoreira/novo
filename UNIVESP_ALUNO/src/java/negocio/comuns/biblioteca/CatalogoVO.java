package negocio.comuns.biblioteca;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Catalogo. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
// @Entity
// @Table(name = "Catalogo")
// @Indexed
@XmlRootElement(name = "catalogo")
public class CatalogoVO extends SuperVO {

	// @Id
	// @DocumentId
	private Integer codigo;
	// @Transient
	private TipoCatalogoVO tipoCatalogo;
	// @Transient
	private Date dataCadastro;
	// @Transient
	private Date dataUltimaAtualizacao;
	private String nrPaginas;
	private Integer nrExemplaresParaConsulta;
	// private String volume;
	// @Transient
	private String numero;
	private String serie;
	// @Field(index = Index.TOKENIZED, store = Store.YES)
	private String isbn;
	// @Field(index = Index.TOKENIZED, store = Store.YES)
	private String issn;
	private String notas;
	private String anoPublicacao;
	private String edicao;
//	private Boolean configuracaoEspecifica;
	// @Transient
//	private ConfiguracaoBibliotecaVO configuracaoBiblioteca;
	// @Field(index = Index.TOKENIZED, store = Store.YES)
	private String titulo;
	// @Field(index = Index.TOKENIZED, store = Store.YES)
	private String subtitulo;
	// @Field(index = Index.TOKENIZED, store = Store.YES)
	private String assunto;
	// @Transient
	// private ClassificacaoBibliograficaVO classificacaoBibliografica;
	
	private String classificacaoBibliografica;
	// @ContainedIn
	// @OneToMany(mappedBy="catalogo", fetch=FetchType.EAGER)
	private List<CatalogoAutorVO> catalogoAutorVOs;
	private Boolean versaoDigital;
	private String cutterPha;
	private String palavrasChave;
	// @Transient
	private LinguaPublicacaoCatalogoVO linguaPublicacaoCatalogo;
	// @Transient
	private CidadePublicacaoCatalogoVO cidadePublicacaoCatalogo;
	// @Transient
	// @ContainedIn
	// @OneToMany(mappedBy="catalogo", fetch=FetchType.LAZY)
	private List<ExemplarVO> exemplarVOs;
	// @Transient
	private EditoraVO editora;
	// @Transient
	private UsuarioVO responsavel;
	// @Transient
	private UsuarioVO responsavelAtualizacao;
	// @Transient
	private List<ArquivoVO> arquivoVOs;
	// @Transient
	private List<ArquivoVO> arquivoSumarioCapaVOs;
	private String nivelBibliografico;
	// @Transient
	public Boolean ocultar;
	// @Transient
	public String dataPadronizada;
	// Lista auxiliar para ajudar na construção da lista subordinada de
	// CatalogoAutor
	// @Transient
	private List<AutorVO> autorVOs;
	// Atributos transientes para mostrar na tela de busca
	// @Transient
	private Integer nrExemplaresParaEmprestimo;
	// @Transient
	private Integer nrExemplaresDisponivel;
	// @Transient
	private Integer nrExemplaresEmprestado;
	private Integer nrExemplaresReservados;
	// @Transient
	private String local;
	// @Transient
	private Boolean controlaTempoDefasagem;
	// @Transient
	private Integer bibliograficaComplementarMes;
	// @Transient
	private Integer bibliograficaBasicaMes;
	// @Transient
	private String biblioteca;

	public static final long serialVersionUID = 1L;
	// @Transient
	private CursoVO curso;
	// @Transient
	private List<CatalogoCursoVO> catalogoCursoVOs;

	private Date dataInicioAssinatura;
	private Date dataFinalAssinatura;
	private String periodicidade;
	private String cdu;
	private String situacaoAssinatura;
	private String abreviacaoTitulo;
	private String nrEdicaoInicial;
	private String nrEdicaoFinal;
	private String anoVolumeInicial;
	private String anoVolumeFinal;


	private Boolean assinaturaPeriodico;
	// @Transient
	private List<CatalogoAreaConhecimentoVO> catalogoAreaConhecimentoVOs;
	private String link;
	// @Transient
	private String classificacaoInicial;
	// @Transient
	private String classificacaoFinal;
	private String numeroControle;
	private String tituloLimitado;
	private String caminhoImagemCapa;
	private String nomeArquivoCapa;
	private Boolean enviadoEbsco;

	/**
	 * Construtor padrão da classe <code>Catalogo</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public CatalogoVO() {
		super();
	}

	public UsuarioVO getResponsavelAtualizacao() {
		if (responsavelAtualizacao == null) {
			responsavelAtualizacao = new UsuarioVO();
		}
		return (responsavelAtualizacao);
	}

	public void setResponsavelAtualizacao(UsuarioVO obj) {
		this.responsavelAtualizacao = obj;
	}

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return (responsavel);
	}

	public void setResponsavel(UsuarioVO obj) {
		this.responsavel = obj;
	}

	public EditoraVO getEditora() {
		if (editora == null) {
			editora = new EditoraVO();
		}
		return (editora);
	}

	public void setEditora(EditoraVO obj) {
		this.editora = obj;
	}

	public String getAnoPublicacao() {
		if (anoPublicacao == null) {
			anoPublicacao = "";
		}
		return (anoPublicacao);
	}

	public void setAnoPublicacao(String anoPublicacao) {
		this.anoPublicacao = anoPublicacao;
	}

	public String getNotas() {
		if (notas == null) {
			notas = "";
		}
		return (notas);
	}

	public void setNotas(String notas) {
		this.notas = notas;
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

	public String getIsbn() {
		if (isbn == null) {
			isbn = "";
		}
		return (isbn);
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getSerie() {
		if (serie == null) {
			serie = "";
		}
		return (serie);
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getNumero() {
		if (numero == null) {
			numero = "";
		}
		return (numero);
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	// public String getVolume() {
	// if (volume == null) {
	// volume = "";
	// }
	// return (volume);
	// }
	//
	// public void setVolume(String volume) {
	// this.volume = volume;
	// }

	public String getNrPaginas() {
		if (this.nrPaginas == null) {
			nrPaginas = "";
		}

		return this.nrPaginas;
	}

	public void setNrPaginas(String nrPaginas) {
		this.nrPaginas = nrPaginas;
	}

	public Date getDataUltimaAtualizacao() {
		if (dataUltimaAtualizacao == null) {
			dataUltimaAtualizacao = new Date();
		}
		return (dataUltimaAtualizacao);
	}

	public String getDataUltimaAtualizacao_Apresentar() {
		return (Uteis.getData(dataUltimaAtualizacao));
	}

	public void setDataUltimaAtualizacao_Apresentar(String teste) {
	}

	public void setDataUltimaAtualizacao(Date dataUltimaAtualizacao) {
		this.dataUltimaAtualizacao = dataUltimaAtualizacao;
	}

	public Date getDataCadastro() {
		if (dataCadastro == null) {
			dataCadastro = new Date();
		}
		return (dataCadastro);
	}

	public String getDataCadastro_Apresentar() {
		return (Uteis.getData(dataCadastro));
	}

	public void setDataCadastro_Apresentar(String teste) {
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
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

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

//	public void setConfiguracaoBiblioteca(ConfiguracaoBibliotecaVO configuracaoBiblioteca) {
//		this.configuracaoBiblioteca = configuracaoBiblioteca;
//	}
//
//	public ConfiguracaoBibliotecaVO getConfiguracaoBiblioteca() {
//		if (configuracaoBiblioteca == null) {
//			configuracaoBiblioteca = new ConfiguracaoBibliotecaVO();
//		}
//		return configuracaoBiblioteca;
//	}

	public Boolean getVersaoDigital() {
		if (versaoDigital == null) {
			versaoDigital = Boolean.FALSE;
		}
		return versaoDigital;
	}

	public void setVersaoDigital(Boolean versaoDigital) {
		this.versaoDigital = versaoDigital;
	}

	public void setArquivoVOs(List<ArquivoVO> arquivoVOs) {
		this.arquivoVOs = arquivoVOs;
	}

	public List<ArquivoVO> getArquivoVOs() {
		if (arquivoVOs == null) {
			arquivoVOs = new ArrayList<ArquivoVO>(0);
		}
		return arquivoVOs;
	}

//	public Boolean getConfiguracaoEspecifica() {
//		if (configuracaoEspecifica == null) {
//			configuracaoEspecifica = false;
//		}
//		return configuracaoEspecifica;
//	}
//
//	public void setConfiguracaoEspecifica(Boolean configuracaoEspecifica) {
//		this.configuracaoEspecifica = configuracaoEspecifica;
//	}

	public void setNrExemplaresParaConsulta(Integer nrExemplaresParaConsulta) {
		this.nrExemplaresParaConsulta = nrExemplaresParaConsulta;
	}
	
	@XmlElement(name = "nrExemplaresParaConsulta")
	public Integer getNrExemplaresParaConsulta() {
		if (nrExemplaresParaConsulta == null) {
			nrExemplaresParaConsulta = 0;
		}
		return nrExemplaresParaConsulta;
	}

	public List<ArquivoVO> getArquivoSumarioCapaVOs() {
		if (arquivoSumarioCapaVOs == null) {
			arquivoSumarioCapaVOs = new ArrayList<ArquivoVO>(0);
		}
		return arquivoSumarioCapaVOs;
	}

	public void setArquivoSumarioCapaVOs(List<ArquivoVO> arquivoSumarioCapaVOs) {
		this.arquivoSumarioCapaVOs = arquivoSumarioCapaVOs;
	}

	public TipoCatalogoVO getTipoCatalogo() {
		if (tipoCatalogo == null) {
			tipoCatalogo = new TipoCatalogoVO();
		}
		return tipoCatalogo;
	}

	public void setTipoCatalogo(TipoCatalogoVO tipoCatalogo) {
		this.tipoCatalogo = tipoCatalogo;
	}
	
	@XmlElement(name = "titulo")
	public String getTitulo() {
		if (titulo == null) {
			titulo = "";
		}
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	@XmlElement(name = "subTitulo")
	public String getSubtitulo() {
		if (subtitulo == null) {
			subtitulo = "";
		}
		return subtitulo;
	}

	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}

	public String getAssunto() {
		if (assunto == null) {
			assunto = "";
		}
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	// public ClassificacaoBibliograficaVO getClassificacaoBibliografica() {
	// if (classificacaoBibliografica == null) {
	// classificacaoBibliografica = new ClassificacaoBibliograficaVO();
	// }
	// return classificacaoBibliografica;
	// }
	//
	// public void setClassificacaoBibliografica(ClassificacaoBibliograficaVO
	// classificacaoBibliografica) {
	// this.classificacaoBibliografica = classificacaoBibliografica;
	// }

	public String getCutterPha() {
		if (cutterPha == null) {
			cutterPha = "";
		}
		return cutterPha;
	}

	public void setCutterPha(String cutterPha) {
		this.cutterPha = cutterPha;
	}

	public List<ExemplarVO> getExemplarVOs() {
		if (exemplarVOs == null) {
			exemplarVOs = new ArrayList<ExemplarVO>(0);
		}
		return exemplarVOs;
	}

	public void setExemplarVOs(List<ExemplarVO> exemplarVOs) {
		this.exemplarVOs = exemplarVOs;
	}

	public String getPalavrasChave() {
		if (palavrasChave == null) {
			palavrasChave = "";
		}
		return palavrasChave;
	}

	public void setPalavrasChave(String palavrasChave) {
		this.palavrasChave = palavrasChave;
	}

	public LinguaPublicacaoCatalogoVO getLinguaPublicacaoCatalogo() {
		if (linguaPublicacaoCatalogo == null) {
			linguaPublicacaoCatalogo = new LinguaPublicacaoCatalogoVO();
		}
		return linguaPublicacaoCatalogo;
	}

	public void setLinguaPublicacaoCatalogo(LinguaPublicacaoCatalogoVO linguaPublicacaoCatalogo) {
		this.linguaPublicacaoCatalogo = linguaPublicacaoCatalogo;
	}

	public CidadePublicacaoCatalogoVO getCidadePublicacaoCatalogo() {
		if (cidadePublicacaoCatalogo == null) {
			cidadePublicacaoCatalogo = new CidadePublicacaoCatalogoVO();
		}
		return cidadePublicacaoCatalogo;
	}

	public void setCidadePublicacaoCatalogo(CidadePublicacaoCatalogoVO cidadePublicacaoCatalogo) {
		this.cidadePublicacaoCatalogo = cidadePublicacaoCatalogo;
	}

	public void setCatalogoAutorVOs(List<CatalogoAutorVO> catalogoAutorVOs) {
		this.catalogoAutorVOs = catalogoAutorVOs;
	}

	public List<CatalogoAutorVO> getCatalogoAutorVOs() {
		if (catalogoAutorVOs == null) {
			catalogoAutorVOs = new ArrayList<CatalogoAutorVO>(0);
		}
		return catalogoAutorVOs;
	}

	public void setNivelBibliografico(String nivelBibliografico) {
		this.nivelBibliografico = nivelBibliografico;
	}

	public String getNivelBibliografico() {
		if (nivelBibliografico == null) {
			nivelBibliografico = "";
		}
		return nivelBibliografico;
	}

	public Boolean getOcultar() {
		if (ocultar == null) {
			ocultar = Boolean.FALSE;
		}
		return ocultar;
	}

	public void setOcultar(Boolean ocultar) {
		this.ocultar = ocultar;
	}

	public String getDataPadronizada() {
		if (dataPadronizada == null) {
			dataPadronizada = "";
		}
		return dataPadronizada;
	}

	public void setDataPadronizada(String dataPadronizada) {
		this.dataPadronizada = dataPadronizada;
	}

	public void setAutorVOs(List<AutorVO> autorVOs) {
		this.autorVOs = autorVOs;
	}

	public List<AutorVO> getAutorVOs() {
		if (autorVOs == null) {
			autorVOs = new ArrayList<AutorVO>(0);
		}
		return autorVOs;
	}

	public String getNomeAutores() {
		String nomeAutores = "";
		for (int i = 0; i < getAutorVOs().size(); i++) {
			nomeAutores += getAutorVOs().get(i).getNome() + " " + getAutorVOs().get(i).getSiglaAutoria();
			if (i < getAutorVOs().size() - 1) {
				nomeAutores += "; ";
			}
		}
		return nomeAutores;
	}
	
	public String getNomeAutoresNascFalec() {
		String nomeAutores = "";
		for (int i = 0; i < getAutorVOs().size(); i++) {
			if (!getAutorVOs().get(i).getAnoNascimento().equals("") && !getAutorVOs().get(i).getAnoFalecimento().equals("")) {
				nomeAutores += getAutorVOs().get(i).getNome() + "; " + getAutorVOs().get(i).getAnoNascimento() + "-" + getAutorVOs().get(i).getAnoFalecimento();
			} else {
				nomeAutores += getAutorVOs().get(i).getNome();
			}
			if (i < getAutorVOs().size() - 1) {
				nomeAutores += "; ";
			}
		}
		return nomeAutores;
	}

	public void setNrExemplaresParaEmprestimo(Integer nrExemplaresParaEmprestimo) {
		this.nrExemplaresParaEmprestimo = nrExemplaresParaEmprestimo;
	}
	
	@XmlElement(name = "nrExemplaresParaEmprestimo")
	public Integer getNrExemplaresParaEmprestimo() {
		if (nrExemplaresParaEmprestimo == null) {
			nrExemplaresParaEmprestimo = 0;
		}
		return nrExemplaresParaEmprestimo;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getLocal() {
		if (local == null) {
			local = "";
		}
		return local;
	}

	public Boolean getIsHabilitarBotaoAdicionarGuia() {
		if (getNrExemplaresParaEmprestimo() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getControlaTempoDefasagem() {
		if (controlaTempoDefasagem == null) {
			controlaTempoDefasagem = Boolean.FALSE;
		}
		return controlaTempoDefasagem;
	}

	public void setControlaTempoDefasagem(Boolean controlaTempoDefasagem) {
		this.controlaTempoDefasagem = controlaTempoDefasagem;
	}

	public Integer getBibliograficaComplementarMes() {
		if (bibliograficaComplementarMes == null) {
			bibliograficaComplementarMes = 0;
		}
		return bibliograficaComplementarMes;
	}

	public void setBibliograficaComplementarMes(Integer bibliograficaComplementarMes) {
		this.bibliograficaComplementarMes = bibliograficaComplementarMes;
	}

	public Integer getBibliograficaBasicaMes() {
		if (bibliograficaBasicaMes == null) {
			bibliograficaBasicaMes = 0;
		}
		return bibliograficaBasicaMes;
	}

	public void setBibliograficaBasicaMes(Integer bibliograficaBasicaMes) {
		this.bibliograficaBasicaMes = bibliograficaBasicaMes;
	}

	public String getClassificacaoBibliografica() {
		if (classificacaoBibliografica == null) {
			classificacaoBibliografica = "";
		}
		return classificacaoBibliografica;
	}

	public void setClassificacaoBibliografica(String classificacaoBibliografica) {
		this.classificacaoBibliografica = classificacaoBibliografica;
	}

	public String getBiblioteca() {
		if (biblioteca == null) {
			biblioteca = "";
		}
		return biblioteca;
	}

	public void setBiblioteca(String biblioteca) {
		this.biblioteca = biblioteca;
	}

	public String getApresentarListaConcatenadaAutores() {
		String autores = "";
		String virgula = "";
		for (AutorVO autor : getAutorVOs()) {
			autores += autor.getNome();
			autores += virgula;
			virgula = ",";
		}
		return autores;
	}

	public CursoVO getCurso() {
		if (curso == null) {
			curso = new CursoVO();
		}
		return curso;
	}

	public void setCurso(CursoVO curso) {
		this.curso = curso;
	}

	public List<CatalogoCursoVO> getCatalogoCursoVOs() {
		if (catalogoCursoVOs == null) {
			catalogoCursoVOs = new ArrayList<CatalogoCursoVO>(0);
		}
		return catalogoCursoVOs;
	}

	public void setCatalogoCursoVOs(List<CatalogoCursoVO> catalogoCursoVOs) {
		this.catalogoCursoVOs = catalogoCursoVOs;
	}
	
	@XmlElement(name = "nrexemplaresemprestado")
	public Integer getNrExemplaresEmprestado() {
		if (nrExemplaresEmprestado == null) {
			nrExemplaresEmprestado = 0;
		}
		return nrExemplaresEmprestado;
	}

	public void setNrExemplaresEmprestado(Integer nrExemplaresEmprestado) {
		this.nrExemplaresEmprestado = nrExemplaresEmprestado;
	}

	public Date getDataInicioAssinatura() {
		if (dataInicioAssinatura == null) {
			dataInicioAssinatura = new Date();
		}
		return dataInicioAssinatura;
	}

	public void setDataInicioAssinatura(Date dataInicioAssinatura) {
		this.dataInicioAssinatura = dataInicioAssinatura;
	}

	public Date getDataFinalAssinatura() {
		if (dataFinalAssinatura == null) {
			dataFinalAssinatura = new Date();
		}
		return dataFinalAssinatura;
	}

	public void setDataFinalAssinatura(Date dataFinalAssinatura) {
		this.dataFinalAssinatura = dataFinalAssinatura;
	}

	public String getPeriodicidade() {
		if (periodicidade == null) {
			periodicidade = "";
		}
		return periodicidade;
	}

	public void setPeriodicidade(String periodicidade) {
		this.periodicidade = periodicidade;
	}

	public String getCdu() {
		if (cdu == null) {
			cdu = "";
		}
		return cdu;
	}

	public void setCdu(String cdu) {
		this.cdu = cdu;
	}

	public String getSituacaoAssinatura() {
		if (situacaoAssinatura == null) {
			situacaoAssinatura = "";
		}
		return situacaoAssinatura;
	}

	public void setSituacaoAssinatura(String situacaoAssinatura) {
		this.situacaoAssinatura = situacaoAssinatura;
	}

	public Boolean getAssinaturaPeriodico() {
		if (assinaturaPeriodico == null) {
			assinaturaPeriodico = Boolean.FALSE;
		}
		return assinaturaPeriodico;
	}

	public void setAssinaturaPeriodico(Boolean assinaturaPeriodico) {
		this.assinaturaPeriodico = assinaturaPeriodico;
	}

	public String getDataFinalAssinatura_Apresentar() {
		return (Uteis.getData(getDataFinalAssinatura()));
	}

	public String getDataInicioAssinatura_Apresentar() {
		return (Uteis.getData(getDataInicioAssinatura()));
	}

	public List<CatalogoAreaConhecimentoVO> getCatalogoAreaConhecimentoVOs() {
		if (catalogoAreaConhecimentoVOs == null) {
			catalogoAreaConhecimentoVOs = new ArrayList<CatalogoAreaConhecimentoVO>(0);
		}
		return catalogoAreaConhecimentoVOs;
	}

	public void setCatalogoAreaConhecimentoVOs(List<CatalogoAreaConhecimentoVO> catalogoAreaConhecimentoVOs) {
		this.catalogoAreaConhecimentoVOs = catalogoAreaConhecimentoVOs;
	}

//	public AreaConhecimentoVO getAreaConhecimentoVO() {
//		if (areaConhecimentoVO == null) {
//			areaConhecimentoVO = new AreaConhecimentoVO();
//		}
//		return areaConhecimentoVO;
//	}
//
//	public void setAreaConhecimentoVO(AreaConhecimentoVO areaConhecimentoVO) {
//		this.areaConhecimentoVO = areaConhecimentoVO;
//	}
	@XmlElement(name = "nrExemplaresDisponivel")
	public Integer getNrExemplaresDisponivel() {
		if (nrExemplaresDisponivel == null) {
			nrExemplaresDisponivel = 0;
		}
		return nrExemplaresDisponivel;
	}

	public void setNrExemplaresDisponivel(Integer nrExemplaresDisponivel) {
		this.nrExemplaresDisponivel = nrExemplaresDisponivel;
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

	public Integer getNrExemplaresReservados() {
		if (nrExemplaresReservados == null) {
			nrExemplaresReservados = 0;
		}
		return nrExemplaresReservados;
	}

	public void setNrExemplaresReservados(Integer nrExemplaresReservados) {
		this.nrExemplaresReservados = nrExemplaresReservados;
	}

	public String getAnoVolumeInicial() {
		if (anoVolumeInicial == null) {
			anoVolumeInicial = "";
		}
		return anoVolumeInicial;
	}

	public void setAnoVolumeInicial(String anoVolumeInicial) {
		this.anoVolumeInicial = anoVolumeInicial;
	}

	public String getAnoVolumeFinal() {
		if (anoVolumeFinal == null) {
			anoVolumeFinal = "";
		}
		return anoVolumeFinal;
	}

	public void setAnoVolumeFinal(String anoVolumeFinal) {
		this.anoVolumeFinal = anoVolumeFinal;
	}

	public String getNrEdicaoInicial() {
		if (nrEdicaoInicial == null) {
			nrEdicaoInicial = "";
		}
		return nrEdicaoInicial;
	}

	public void setNrEdicaoInicial(String nrEdicaoInicial) {
		this.nrEdicaoInicial = nrEdicaoInicial;
	}

	public String getNrEdicaoFinal() {
		if (nrEdicaoFinal == null) {
			nrEdicaoFinal = "";
		}
		return nrEdicaoFinal;
	}

	public void setNrEdicaoFinal(String nrEdicaoFinal) {
		this.nrEdicaoFinal = nrEdicaoFinal;
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

	public String getClassificacaoInicial() {
		if (classificacaoInicial == null) {
			classificacaoInicial = "";
		}
		return classificacaoInicial;
	}

	public void setClassificacaoInicial(String classificacaoInicial) {
		this.classificacaoInicial = classificacaoInicial;
	}

	public String getClassificacaoFinal() {
		if (classificacaoFinal == null) {
			classificacaoFinal = "";
		}
		return classificacaoFinal;
	}

	public void setClassificacaoFinal(String classificacaoFinal) {
		this.classificacaoFinal = classificacaoFinal;
	}

	public String getNumeroControle() {
		if (numeroControle == null) {
			numeroControle = "";
		}
		return numeroControle;
	}

	public void setNumeroControle(String numeroControle) {
		this.numeroControle = numeroControle;
	}

	public String getTituloLimitado() {
		if (tituloLimitado == null) {
			tituloLimitado = "";
		}
		return tituloLimitado;
	}

	public void setTituloLimitado(String tituloLimitado) {
		this.tituloLimitado = tituloLimitado;
	}

	public String getCaminhoImagemCapa() {
		if (caminhoImagemCapa == null) {
			caminhoImagemCapa = "";
		}
		return caminhoImagemCapa;
	}

	public void setCaminhoImagemCapa(String caminhoImagemCapa) {
		this.caminhoImagemCapa = caminhoImagemCapa;
	}

	public String getNomeArquivoCapa() {
		if (nomeArquivoCapa == null) {
			nomeArquivoCapa = "";
		}
		return nomeArquivoCapa;
	}

	public void setNomeArquivoCapa(String nomeArquivoCapa) {
		this.nomeArquivoCapa = nomeArquivoCapa;
	}

	
	public Boolean getEnviadoEbsco() {
		if(enviadoEbsco == null){
			enviadoEbsco = Boolean.FALSE;
		}
		return enviadoEbsco;
	}

	public void setEnviadoEbsco(Boolean enviadoEbsco) {
		this.enviadoEbsco = enviadoEbsco;
	}

	
	
	
	public final String getCssAssunto() {
		if(getAssunto().length() > 100) {
			return "col-md-12";
		} return "col-md-4";
		
	}
	
}
