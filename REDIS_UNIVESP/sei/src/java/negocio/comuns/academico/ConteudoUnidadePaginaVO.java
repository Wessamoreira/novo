package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import controle.arquitetura.AplicacaoControle;
import negocio.comuns.academico.enumeradores.TipoGraficoEnum;
import negocio.comuns.academico.enumeradores.TipoRecursoEducacionalEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.ListaExercicioVO;
import negocio.comuns.ead.enumeradores.OrigemBackgroundConteudoEnum;
import negocio.comuns.ead.enumeradores.SituacaoListaExercicioEnum;
import negocio.comuns.ead.enumeradores.TamanhoImagemBackgroundConteudoEnum;


public class ConteudoUnidadePaginaVO extends SuperVO {

    /**
     * 
     */
    private static final long serialVersionUID = -442704334218586844L;
        
    private Integer codigo;
    private TipoRecursoEducacionalEnum tipoRecursoEducacional;    
    private String titulo;    
    private String texto;
    private Integer altura;
    private Integer largura;    
    private String caminhoBaseRepositorio;
    private String nomeRealArquivo;
    private String nomeFisicoArquivo;
    private String nomeFisicoArquivoAlternativo;
    private Integer pagina;
    private Integer tempo;
    private Double ponto;
    private UnidadeConteudoVO unidadeConteudo;
    private String caminhoIconeVoltar;
    private String nomeIconeVoltar;
    private String caminhoIconeAvancar;
    private String nomeIconeAvancar;
    private String labelIconeAvancar;
    private String labelIconeVoltar;
    private IconeVO iconeVoltar;
    private IconeVO iconeAvancar;
    private RecursoEducacionalVO recursoEducacional;
    private List<ConteudoUnidadePaginaRecursoEducacionalVO> conteudoUnidadePaginaRecursoEducacionalAnteriorVOs;
    private List<ConteudoUnidadePaginaRecursoEducacionalVO> conteudoUnidadePaginaRecursoEducacionalPosteriorVOs;
    private List<ConteudoUnidadePaginaRecursoEducacionalVO> conteudoUnidadePaginaRecursoEducacionalApoioProfessor;
    
    private String tituloGrafico;
    private String tituloEixoX;
    private String tituloEixoY;
    private Boolean apresentarLegenda;
    private String valorGrafico;
    private String categoriaGrafico;
    private TipoGraficoEnum tipoGrafico;
    private ForumVO forum;
    private ListaExercicioVO listaExercicio;
    private Date dataCadastro;
    private UsuarioVO usuarioCadastro;
    private Date dataAlteracao;
    private UsuarioVO usuarioAlteracao;
    
    /*
     * Atributos de controle
     */
    private Boolean publicarBibliotecaRecursoEducacional;
    private Boolean publicarIconeAvancar;
    private Boolean publicarIconeVoltar;
    private List<ConteudoUnidadePaginaGraficoPizzaVO> conteudoUnidadePaginaGraficoVOs;
    private List<ConteudoUnidadePaginaGraficoCategoriaVO> conteudoUnidadePaginaGraficoCategoriaVOs;
    private List<ConteudoUnidadePaginaGraficoSerieVO> conteudoUnidadePaginaGraficoSerieVOs;
    private List<ConteudoUnidadePaginaRecursoEducacionalVO> conteudoUnidadePaginaRecursoEducacionalManterDisponivelVOs;
    private Boolean paginaJaVisualizada;
    
    private String caminhoBaseBackground;
    private String nomeImagemBackground;
    private String corBackground;
    private TamanhoImagemBackgroundConteudoEnum tamanhoImagemBackgroundConteudo;
    private OrigemBackgroundConteudoEnum origemBackgroundConteudo;
    private Boolean excluirImagemBackground;
	/**
	 * Transient verificar se existe AnotacaoDisciplina
	 */
	private Boolean existeAnotacaoDisciplina;
	/**
	 * Fim Transient
	 */
	
	public ConteudoUnidadePaginaVO clone() throws CloneNotSupportedException {
		ConteudoUnidadePaginaVO clone = (ConteudoUnidadePaginaVO) super.clone();
		clone.setNovoObj(true);
        clone.setCodigo(0);
        clone.setUnidadeConteudo(new UnidadeConteudoVO());
        clone.getForum().setNovoObj(true);
        clone.getForum().setCodigo(0);
        clone.getListaExercicio().setNovoObj(true);
        clone.getListaExercicio().setCodigo(0);
        clone.getListaExercicio().setSituacaoListaExercicio(SituacaoListaExercicioEnum.EM_ELABORACAO);
        clone.setUsuarioCadastro(new UsuarioVO());
        clone.getRecursoEducacional().setCodigo(0);
        clone.getRecursoEducacional().setNovoObj(true);
        clone.setConteudoUnidadePaginaRecursoEducacionalAnteriorVOs(new ArrayList<ConteudoUnidadePaginaRecursoEducacionalVO>());
        for (ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalAnteriorVO : this.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs()) {
			ConteudoUnidadePaginaRecursoEducacionalVO cloneConteudoUnidadePaginaRecursoEducacionalAnteriorVO = conteudoUnidadePaginaRecursoEducacionalAnteriorVO.clone();
			clone.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().add(cloneConteudoUnidadePaginaRecursoEducacionalAnteriorVO);
		}
        clone.setConteudoUnidadePaginaRecursoEducacionalPosteriorVOs(new ArrayList<ConteudoUnidadePaginaRecursoEducacionalVO>());
        for (ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalPosteriorVO : this.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs()) {
			ConteudoUnidadePaginaRecursoEducacionalVO cloneConteudoUnidadePaginaRecursoEducacionalPosteriorVO = conteudoUnidadePaginaRecursoEducacionalPosteriorVO.clone();
			clone.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().add(cloneConteudoUnidadePaginaRecursoEducacionalPosteriorVO);
		}
        clone.setConteudoUnidadePaginaRecursoEducacionalApoioProfessor(new ArrayList<ConteudoUnidadePaginaRecursoEducacionalVO>());
        for (ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalApoioProfessorVO : this.getConteudoUnidadePaginaRecursoEducacionalApoioProfessor()) {
			ConteudoUnidadePaginaRecursoEducacionalVO cloneConteudoUnidadePaginaRecursoEducacionalApoioProfessorVO = conteudoUnidadePaginaRecursoEducacionalApoioProfessorVO.clone();
			clone.getConteudoUnidadePaginaRecursoEducacionalApoioProfessor().add(cloneConteudoUnidadePaginaRecursoEducacionalApoioProfessorVO);
		}
        clone.setConteudoUnidadePaginaGraficoVOs(new ArrayList<ConteudoUnidadePaginaGraficoPizzaVO>());
        for (ConteudoUnidadePaginaGraficoPizzaVO conteudoUnidadePaginaGraficoPizzaVO : this.getConteudoUnidadePaginaGraficoVOs()) {
			ConteudoUnidadePaginaGraficoPizzaVO cloneConteudoUnidadePaginaPizzaVO = conteudoUnidadePaginaGraficoPizzaVO.clone();
			clone.getConteudoUnidadePaginaGraficoVOs().add(cloneConteudoUnidadePaginaPizzaVO);
		}
        clone.setConteudoUnidadePaginaGraficoCategoriaVOs(new ArrayList<ConteudoUnidadePaginaGraficoCategoriaVO>());
        for (ConteudoUnidadePaginaGraficoCategoriaVO conteudoUnidadePaginaGraficoCategoriaVO : this.getConteudoUnidadePaginaGraficoCategoriaVOs()) {
			ConteudoUnidadePaginaGraficoCategoriaVO cloneConteudoUnidadePginaGraficoCategoriaVO = conteudoUnidadePaginaGraficoCategoriaVO.clone();
			clone.getConteudoUnidadePaginaGraficoCategoriaVOs().add(cloneConteudoUnidadePginaGraficoCategoriaVO);
		}
        clone.setConteudoUnidadePaginaGraficoSerieVOs(new ArrayList<ConteudoUnidadePaginaGraficoSerieVO>());
        for (ConteudoUnidadePaginaGraficoSerieVO conteudoUnidadePaginaGraficoSerieVO : this.getConteudoUnidadePaginaGraficoSerieVOs()) {
			ConteudoUnidadePaginaGraficoSerieVO cloneConteudoUnidadePaginaGraficoSerieVO = conteudoUnidadePaginaGraficoSerieVO.clone();
			clone.getConteudoUnidadePaginaGraficoSerieVOs().add(cloneConteudoUnidadePaginaGraficoSerieVO);
		}
        clone.setConteudoUnidadePaginaRecursoEducacionalManterDisponivelVOs(new ArrayList<ConteudoUnidadePaginaRecursoEducacionalVO>());
        for (ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalManterDisponivelVO : this.getConteudoUnidadePaginaRecursoEducacionalManterDisponivelVOs()) {
			ConteudoUnidadePaginaRecursoEducacionalVO cloneConteudoUnidadePaginaRecursoEducacionalManterDisponivelVO = conteudoUnidadePaginaRecursoEducacionalManterDisponivelVO.clone();
			clone.getConteudoUnidadePaginaRecursoEducacionalManterDisponivelVOs().add(cloneConteudoUnidadePaginaRecursoEducacionalManterDisponivelVO);
		}
        
		return clone;
	}
	
	public ConteudoUnidadePaginaVO clonarCopiaExata() throws CloneNotSupportedException {
		ConteudoUnidadePaginaVO clone = (ConteudoUnidadePaginaVO) super.clone();
        clone.setConteudoUnidadePaginaRecursoEducacionalAnteriorVOs(new ArrayList<ConteudoUnidadePaginaRecursoEducacionalVO>());
        for (ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalAnteriorVO : this.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs()) {
			ConteudoUnidadePaginaRecursoEducacionalVO cloneConteudoUnidadePaginaRecursoEducacionalAnteriorVO = conteudoUnidadePaginaRecursoEducacionalAnteriorVO.clone();
			clone.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().add(cloneConteudoUnidadePaginaRecursoEducacionalAnteriorVO);
		}
        clone.setConteudoUnidadePaginaRecursoEducacionalPosteriorVOs(new ArrayList<ConteudoUnidadePaginaRecursoEducacionalVO>());
        for (ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalPosteriorVO : this.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs()) {
			ConteudoUnidadePaginaRecursoEducacionalVO cloneConteudoUnidadePaginaRecursoEducacionalPosteriorVO = conteudoUnidadePaginaRecursoEducacionalPosteriorVO.clone();
			clone.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().add(cloneConteudoUnidadePaginaRecursoEducacionalPosteriorVO);
		}
        clone.setConteudoUnidadePaginaRecursoEducacionalApoioProfessor(new ArrayList<ConteudoUnidadePaginaRecursoEducacionalVO>());
        for (ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalApoioProfessorVO : this.getConteudoUnidadePaginaRecursoEducacionalApoioProfessor()) {
			ConteudoUnidadePaginaRecursoEducacionalVO cloneConteudoUnidadePaginaRecursoEducacionalApoioProfessorVO = conteudoUnidadePaginaRecursoEducacionalApoioProfessorVO.clone();
			clone.getConteudoUnidadePaginaRecursoEducacionalApoioProfessor().add(cloneConteudoUnidadePaginaRecursoEducacionalApoioProfessorVO);
		}
        clone.setConteudoUnidadePaginaGraficoVOs(new ArrayList<ConteudoUnidadePaginaGraficoPizzaVO>());
        for (ConteudoUnidadePaginaGraficoPizzaVO conteudoUnidadePaginaGraficoPizzaVO : this.getConteudoUnidadePaginaGraficoVOs()) {
			ConteudoUnidadePaginaGraficoPizzaVO cloneConteudoUnidadePaginaPizzaVO = conteudoUnidadePaginaGraficoPizzaVO.clone();
			clone.getConteudoUnidadePaginaGraficoVOs().add(cloneConteudoUnidadePaginaPizzaVO);
		}
        clone.setConteudoUnidadePaginaGraficoCategoriaVOs(new ArrayList<ConteudoUnidadePaginaGraficoCategoriaVO>());
        for (ConteudoUnidadePaginaGraficoCategoriaVO conteudoUnidadePaginaGraficoCategoriaVO : this.getConteudoUnidadePaginaGraficoCategoriaVOs()) {
			ConteudoUnidadePaginaGraficoCategoriaVO cloneConteudoUnidadePginaGraficoCategoriaVO = conteudoUnidadePaginaGraficoCategoriaVO.clone();
			clone.getConteudoUnidadePaginaGraficoCategoriaVOs().add(cloneConteudoUnidadePginaGraficoCategoriaVO);
		}
        clone.setConteudoUnidadePaginaGraficoSerieVOs(new ArrayList<ConteudoUnidadePaginaGraficoSerieVO>());
        for (ConteudoUnidadePaginaGraficoSerieVO conteudoUnidadePaginaGraficoSerieVO : this.getConteudoUnidadePaginaGraficoSerieVOs()) {
			ConteudoUnidadePaginaGraficoSerieVO cloneConteudoUnidadePaginaGraficoSerieVO = conteudoUnidadePaginaGraficoSerieVO.clone();
			clone.getConteudoUnidadePaginaGraficoSerieVOs().add(cloneConteudoUnidadePaginaGraficoSerieVO);
		}
        clone.setConteudoUnidadePaginaRecursoEducacionalManterDisponivelVOs(new ArrayList<ConteudoUnidadePaginaRecursoEducacionalVO>());
        for (ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalManterDisponivelVO : this.getConteudoUnidadePaginaRecursoEducacionalManterDisponivelVOs()) {
			ConteudoUnidadePaginaRecursoEducacionalVO cloneConteudoUnidadePaginaRecursoEducacionalManterDisponivelVO = conteudoUnidadePaginaRecursoEducacionalManterDisponivelVO.clone();
			clone.getConteudoUnidadePaginaRecursoEducacionalManterDisponivelVOs().add(cloneConteudoUnidadePaginaRecursoEducacionalManterDisponivelVO);
		}
		return clone;
	}
    
    public Integer getCodigo() {
        if(codigo ==null){
            codigo = 0;
        }
        return codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    public TipoRecursoEducacionalEnum getTipoRecursoEducacional() {
        if(tipoRecursoEducacional ==null){
            tipoRecursoEducacional = TipoRecursoEducacionalEnum.TEXTO_HTML;
        }
        return tipoRecursoEducacional;
    }
    
    public void setTipoRecursoEducacional(TipoRecursoEducacionalEnum tipoRecursoEducacional) {
        this.tipoRecursoEducacional = tipoRecursoEducacional;
    }
    
    public String getTitulo() {
        if(titulo == null){
            titulo = "";
        }
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
	public String getTexto() {
		if (texto == null) {
			texto = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html><head><title></title></head><body style=\"background-color:transparent !important;background:transparent !important;\"></body><p></p></html>";
		}
		return texto;
    }
    
    public void setTexto(String texto) {
        this.texto = texto;
    }
    
    public Integer getAltura() {
        if(altura == null){
            altura = 600;
        }
        return altura;
    }
    
    public void setAltura(Integer altura) {
        this.altura = altura;
    }
    
    public Integer getLargura() {
        if(largura == null){
            largura = 800;
        }
        return largura;
    }
    
    public void setLargura(Integer largura) {
        this.largura = largura;
    }
    
    public String getCaminhoBaseRepositorio() {
        if(caminhoBaseRepositorio ==null){
            caminhoBaseRepositorio = "";
        }
        return caminhoBaseRepositorio;
    }
    
    public void setCaminhoBaseRepositorio(String caminhoBaseRepositorio) {
        this.caminhoBaseRepositorio = caminhoBaseRepositorio;
    }
    
    public String getNomeRealArquivo() {
        if(nomeRealArquivo ==null){
            nomeRealArquivo = "";
        }
        return nomeRealArquivo;
    }
    
    public void setNomeRealArquivo(String nomeRealArquivo) {
        this.nomeRealArquivo = nomeRealArquivo;
    }
    
    public String getNomeFisicoArquivo() {
        if(nomeFisicoArquivo ==null){
            nomeFisicoArquivo = "";
        }
        return nomeFisicoArquivo;
    }
    
    public void setNomeFisicoArquivo(String nomeFisicoArquivo) {
        this.nomeFisicoArquivo = nomeFisicoArquivo;
    }
    
    public Integer getPagina() {
        if(pagina ==null){
            pagina = 1;
        }
        return pagina;
    }
    
    public void setPagina(Integer pagina) {
        this.pagina = pagina;
    }
    
    public Integer getTempo() {
        if(tempo ==null){
            tempo = 0;
        }
        return tempo;
    }
    
    public void setTempo(Integer tempo) {
        this.tempo = tempo;
    }
    
    public Double getPonto() {
        if(ponto ==null){
            ponto = 0.0;
        }
        return ponto;
    }
    
    public void setPonto(Double ponto) {
        this.ponto = ponto;
    }
    
    public UnidadeConteudoVO getUnidadeConteudo() {
        if(unidadeConteudo == null){
            unidadeConteudo = new UnidadeConteudoVO();
        }
        return unidadeConteudo;
    }
    
    public void setUnidadeConteudo(UnidadeConteudoVO unidadeConteudo) {
        this.unidadeConteudo = unidadeConteudo;
    }
    
    public String getCaminhoIconeVoltar() {
        if(caminhoIconeVoltar == null){
            caminhoIconeVoltar = "./resources/imagens";
        }
        return caminhoIconeVoltar;
    }
    
    public void setCaminhoIconeVoltar(String caminhoIconeVoltar) {
        this.caminhoIconeVoltar = caminhoIconeVoltar;
    }
    
    public String getCaminhoIconeAvancar() {
        if(caminhoIconeAvancar == null){
            caminhoIconeAvancar = "./resources/imagens";
        }
        return caminhoIconeAvancar;
    }
    
    public void setCaminhoIconeAvancar(String caminhoIconeAvancar) {
        this.caminhoIconeAvancar = caminhoIconeAvancar;
    }
    
    public String getLabelIconeAvancar() {
        if(labelIconeAvancar == null){
            labelIconeAvancar = "Avançar";
        }
        return labelIconeAvancar;
    }
    
    public void setLabelIconeAvancar(String labelIconeAvancar) {
        this.labelIconeAvancar = labelIconeAvancar;
    }
    
    public String getLabelIconeVoltar() {
        if(labelIconeVoltar == null){
            labelIconeVoltar = "Voltar";
        }
        return labelIconeVoltar;
    }
    
    public void setLabelIconeVoltar(String labelIconeVoltar) {
        this.labelIconeVoltar = labelIconeVoltar;
    }

    
    public RecursoEducacionalVO getRecursoEducacional() {
        if(recursoEducacional == null){
            recursoEducacional = new RecursoEducacionalVO();
        }
        return recursoEducacional;
    }

    
    public void setRecursoEducacional(RecursoEducacionalVO recursoEducacional) {
        this.recursoEducacional = recursoEducacional;
    }
    
    
    public Date getDataCadastro() {
        if(dataCadastro == null){
            dataCadastro = new Date();
        }
        return dataCadastro;
    }

    
    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    
    public UsuarioVO getUsuarioCadastro() {
        if(usuarioCadastro == null){
            usuarioCadastro = new UsuarioVO();
        }
        return usuarioCadastro;
    }

    
    public void setUsuarioCadastro(UsuarioVO usuarioCadastro) {
        this.usuarioCadastro = usuarioCadastro;
    }

    
    public Date getDataAlteracao() {
        if(dataCadastro == null){
            dataCadastro = new Date();
        }
        return dataAlteracao;
    }

    
    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    
    public UsuarioVO getUsuarioAlteracao() {
        if(usuarioAlteracao == null){
            usuarioAlteracao = new UsuarioVO();
        }
        return usuarioAlteracao;
    }

    
    public void setUsuarioAlteracao(UsuarioVO usuarioAlteracao) {
        this.usuarioAlteracao = usuarioAlteracao;
    }

    
    public Boolean getPublicarBibliotecaRecursoEducacional() {
        if(publicarBibliotecaRecursoEducacional == null){
            publicarBibliotecaRecursoEducacional = false;
        }
        return publicarBibliotecaRecursoEducacional;
    }

    
    public void setPublicarBibliotecaRecursoEducacional(Boolean publicarBibliotecaRecursoEducacional) {
        this.publicarBibliotecaRecursoEducacional = publicarBibliotecaRecursoEducacional;
    }

    
    public List<ConteudoUnidadePaginaRecursoEducacionalVO> getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs() {
        if(conteudoUnidadePaginaRecursoEducacionalAnteriorVOs ==null){
            conteudoUnidadePaginaRecursoEducacionalAnteriorVOs = new ArrayList<ConteudoUnidadePaginaRecursoEducacionalVO>(0);
        }
        return conteudoUnidadePaginaRecursoEducacionalAnteriorVOs;
    }

    
    public void setConteudoUnidadePaginaRecursoEducacionalAnteriorVOs(List<ConteudoUnidadePaginaRecursoEducacionalVO> conteudoUnidadePaginaRecursoEducacionalAnteriorVOs) {
        this.conteudoUnidadePaginaRecursoEducacionalAnteriorVOs = conteudoUnidadePaginaRecursoEducacionalAnteriorVOs;
    }

    
    public List<ConteudoUnidadePaginaRecursoEducacionalVO> getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs() {
        if(conteudoUnidadePaginaRecursoEducacionalPosteriorVOs ==null){
            conteudoUnidadePaginaRecursoEducacionalPosteriorVOs = new ArrayList<ConteudoUnidadePaginaRecursoEducacionalVO>(0);
        }
        return conteudoUnidadePaginaRecursoEducacionalPosteriorVOs;
    }

    
    public void setConteudoUnidadePaginaRecursoEducacionalPosteriorVOs(List<ConteudoUnidadePaginaRecursoEducacionalVO> conteudoUnidadePaginaRecursoEducacionalPosteriorVOs) {
        this.conteudoUnidadePaginaRecursoEducacionalPosteriorVOs = conteudoUnidadePaginaRecursoEducacionalPosteriorVOs;
    }
    
    public List<ConteudoUnidadePaginaRecursoEducacionalVO> getConteudoUnidadePaginaRecursoEducacionalApoioProfessor() {
   	 if(conteudoUnidadePaginaRecursoEducacionalApoioProfessor ==null){
   		 conteudoUnidadePaginaRecursoEducacionalApoioProfessor = new ArrayList<ConteudoUnidadePaginaRecursoEducacionalVO>(0);
        }
		return conteudoUnidadePaginaRecursoEducacionalApoioProfessor;
	}

	public void setConteudoUnidadePaginaRecursoEducacionalApoioProfessor(List<ConteudoUnidadePaginaRecursoEducacionalVO> conteudoUnidadePaginaRecursoEducacionalApoioProfessor) {
		this.conteudoUnidadePaginaRecursoEducacionalApoioProfessor = conteudoUnidadePaginaRecursoEducacionalApoioProfessor;
	}
    
    
    public String getTituloGrafico() {
        if(tituloGrafico == null){
            tituloGrafico = "";
        }
        return tituloGrafico;
    }

    
    public void setTituloGrafico(String tituloGrafico) {
        this.tituloGrafico = tituloGrafico;
    }

    
    public String getTituloEixoX() {
        if(tituloEixoX == null){
            tituloEixoX = "";
        }
        return tituloEixoX;
    }

    
    public void setTituloEixoX(String tituloEixoX) {
        this.tituloEixoX = tituloEixoX;
    }

    
    public String getTituloEixoY() {
        if(tituloEixoY == null){
            tituloEixoY = "";
        }
        return tituloEixoY;
    }

    
    public void setTituloEixoY(String tituloEixoY) {
        this.tituloEixoY = tituloEixoY;
    }

    
    public Boolean getApresentarLegenda() {
        if(apresentarLegenda == null){
            apresentarLegenda = true;
        }
        return apresentarLegenda;
    }

    
    public void setApresentarLegenda(Boolean apresentarLegenda) {
        this.apresentarLegenda = apresentarLegenda;
    }

    
    public String getValorGrafico() {
        if(valorGrafico==null){
            valorGrafico = "";
        }
        return valorGrafico;
    }

    
    public void setValorGrafico(String valorGrafico) {
        this.valorGrafico = valorGrafico;
    }
    
    public TipoGraficoEnum getTipoGrafico() {
        if(tipoGrafico == null){
            tipoGrafico = TipoGraficoEnum.PIZZA;
        }
        return tipoGrafico;
    }

    
    public void setTipoGrafico(TipoGraficoEnum tipoGrafico) {
        this.tipoGrafico = tipoGrafico;
    }

    
    public String getNomeFisicoArquivoAlternativo() {
        if(nomeFisicoArquivoAlternativo == null){
            nomeFisicoArquivoAlternativo = "";
        }
        return nomeFisicoArquivoAlternativo;
    }

    
    public void setNomeFisicoArquivoAlternativo(String nomeFisicoArquivoAlternativo) {
        this.nomeFisicoArquivoAlternativo = nomeFisicoArquivoAlternativo;
    }

    
    public List<ConteudoUnidadePaginaGraficoPizzaVO> getConteudoUnidadePaginaGraficoVOs() {
        if(conteudoUnidadePaginaGraficoVOs == null){
            conteudoUnidadePaginaGraficoVOs = new ArrayList<ConteudoUnidadePaginaGraficoPizzaVO>(0);
        }
        return conteudoUnidadePaginaGraficoVOs;
    }

    
    public void setConteudoUnidadePaginaGraficoVOs(List<ConteudoUnidadePaginaGraficoPizzaVO> conteudoUnidadePaginaGraficoVOs) {
        
        this.conteudoUnidadePaginaGraficoVOs = conteudoUnidadePaginaGraficoVOs;
    }

    
    public List<ConteudoUnidadePaginaGraficoCategoriaVO> getConteudoUnidadePaginaGraficoCategoriaVOs() {
        if(conteudoUnidadePaginaGraficoCategoriaVOs == null){
            conteudoUnidadePaginaGraficoCategoriaVOs = new ArrayList<ConteudoUnidadePaginaGraficoCategoriaVO>(0);
        }
        return conteudoUnidadePaginaGraficoCategoriaVOs;
    }

    
    public void setConteudoUnidadePaginaGraficoCategoriaVOs(List<ConteudoUnidadePaginaGraficoCategoriaVO> conteudoUnidadePaginaGraficoCategoriaVOs) {
        this.conteudoUnidadePaginaGraficoCategoriaVOs = conteudoUnidadePaginaGraficoCategoriaVOs;
    }

    
    public List<ConteudoUnidadePaginaGraficoSerieVO> getConteudoUnidadePaginaGraficoSerieVOs() {
        if(conteudoUnidadePaginaGraficoSerieVOs == null){
            conteudoUnidadePaginaGraficoSerieVOs = new ArrayList<ConteudoUnidadePaginaGraficoSerieVO>(0);
        }
        return conteudoUnidadePaginaGraficoSerieVOs;
    }

    
    public void setConteudoUnidadePaginaGraficoSerieVOs(List<ConteudoUnidadePaginaGraficoSerieVO> conteudoUnidadePaginaGraficoSerieVOs) {
        this.conteudoUnidadePaginaGraficoSerieVOs = conteudoUnidadePaginaGraficoSerieVOs;
    }

    
    public String getCategoriaGrafico() {
        if(categoriaGrafico == null){
            categoriaGrafico = "";
        }
        return categoriaGrafico;
    }

    
    public void setCategoriaGrafico(String categoriaGrafico) {
        this.categoriaGrafico = categoriaGrafico;
    }

    
    public Boolean getPublicarIconeAvancar() {
        if(publicarIconeAvancar == null){
            publicarIconeAvancar = false;
        }
        return publicarIconeAvancar;
    }

    
    public void setPublicarIconeAvancar(Boolean publicarIconeAvancar) {
        this.publicarIconeAvancar = publicarIconeAvancar;
    }

    
    public Boolean getPublicarIconeVoltar() {
        if(publicarIconeVoltar == null){
            publicarIconeVoltar = false;
        }
        return publicarIconeVoltar;
    }

    
    public void setPublicarIconeVoltar(Boolean publicarIconeVoltar) {
        this.publicarIconeVoltar = publicarIconeVoltar;
    }

    
    public String getNomeIconeVoltar() {
        if(nomeIconeVoltar == null){
            nomeIconeVoltar ="botaoAnteriorIcone.png";
        }
        return nomeIconeVoltar;
    }

    
    public void setNomeIconeVoltar(String nomeIconeVoltar) {
        this.nomeIconeVoltar = nomeIconeVoltar;
    }

    
    public String getNomeIconeAvancar() {
        if(nomeIconeAvancar == null){
            nomeIconeAvancar ="botaoProximoIcone.png";
        }
        return nomeIconeAvancar;
    }

    
    public void setNomeIconeAvancar(String nomeIconeAvancar) {
        this.nomeIconeAvancar = nomeIconeAvancar;
    }

    
    public IconeVO getIconeVoltar() {
        if(iconeVoltar == null){
            iconeVoltar = new IconeVO();
        }
        return iconeVoltar;
    }

    
    public void setIconeVoltar(IconeVO iconeVoltar) {
        this.iconeVoltar = iconeVoltar;
    }

    
    public IconeVO getIconeAvancar() {
        if(iconeAvancar == null){
            iconeAvancar = new IconeVO();
        }
        return iconeAvancar;
    }

    
    public void setIconeAvancar(IconeVO iconeAvancar) {
        this.iconeAvancar = iconeAvancar;
    }

    
    public ForumVO getForum() {
        if(forum == null){
            forum = new ForumVO();
        }
        return forum;
    }

    
    public void setForum(ForumVO forum) {
        this.forum = forum;
    }

    
    public ListaExercicioVO getListaExercicio() {
        if(listaExercicio == null){
            listaExercicio = new ListaExercicioVO();
        }
        return listaExercicio;
    }

    
    public void setListaExercicio(ListaExercicioVO listaExercicio) {
        this.listaExercicio = listaExercicio;
    }

    
    public List<ConteudoUnidadePaginaRecursoEducacionalVO> getConteudoUnidadePaginaRecursoEducacionalManterDisponivelVOs() {
        if(conteudoUnidadePaginaRecursoEducacionalManterDisponivelVOs == null){
            conteudoUnidadePaginaRecursoEducacionalManterDisponivelVOs = new ArrayList<ConteudoUnidadePaginaRecursoEducacionalVO>(0);
            for(ConteudoUnidadePaginaRecursoEducacionalVO obj:getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs()){
                if(obj.getManterRecursoDisponivelPagina()){
                    conteudoUnidadePaginaRecursoEducacionalManterDisponivelVOs.add(obj);
                }
            }
            for(ConteudoUnidadePaginaRecursoEducacionalVO obj:getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs()){
                if(obj.getManterRecursoDisponivelPagina()){
                    conteudoUnidadePaginaRecursoEducacionalManterDisponivelVOs.add(obj);
                }
            }
        }
        return conteudoUnidadePaginaRecursoEducacionalManterDisponivelVOs;
    }

    
    public Boolean getPaginaJaVisualizada() {
        if(paginaJaVisualizada == null){
            paginaJaVisualizada = false;
        }
        return paginaJaVisualizada;
    }

    
    public void setPaginaJaVisualizada(Boolean paginaJaVisualizada) {
        this.paginaJaVisualizada = paginaJaVisualizada;
    }

    
    public String getNomeImagemBackground() {
		if(nomeImagemBackground == null){
			nomeImagemBackground = "";
		}
		return nomeImagemBackground;
	}

	public void setNomeImagemBackground(String nomeImagemBackground) {
		this.nomeImagemBackground = nomeImagemBackground;
	}

	public String getCorBackground() {
		if(corBackground == null){
			corBackground = "#FFFFFF";
		}
		return corBackground;
	}

	public void setCorBackground(String corBackground) {
		this.corBackground = corBackground;
	}
	
	public String getCaminhoBaseBackground() {
		if(caminhoBaseBackground == null){
			caminhoBaseBackground = "";
		}
		return caminhoBaseBackground;
	}

	public void setCaminhoBaseBackground(String caminhoBaseBackground) {
		this.caminhoBaseBackground = caminhoBaseBackground;
	}
	
	public TamanhoImagemBackgroundConteudoEnum getTamanhoImagemBackgroundConteudo() {
		if(tamanhoImagemBackgroundConteudo == null){
			tamanhoImagemBackgroundConteudo = TamanhoImagemBackgroundConteudoEnum.CEM_PORCENTO;
		}
		return tamanhoImagemBackgroundConteudo;
	}

	public void setTamanhoImagemBackgroundConteudo(TamanhoImagemBackgroundConteudoEnum tamanhoImagemBackgroundConteudoEnum) {
		this.tamanhoImagemBackgroundConteudo = tamanhoImagemBackgroundConteudoEnum;
	}
	
	public OrigemBackgroundConteudoEnum getOrigemBackgroundConteudo() {
		if(origemBackgroundConteudo == null){
			origemBackgroundConteudo = OrigemBackgroundConteudoEnum.SEM_BACKGROUND;
		}
		return origemBackgroundConteudo;
	}

	public void setOrigemBackgroundConteudo(OrigemBackgroundConteudoEnum origemBackgroundConteudo) {
		this.origemBackgroundConteudo = origemBackgroundConteudo;
	}

	public Boolean getExcluirImagemBackground() {
		if(excluirImagemBackground == null){
			excluirImagemBackground = true;
		}
		return excluirImagemBackground;
	}

	public void setExcluirImagemBackground(Boolean excluirImagemBackground) {
		this.excluirImagemBackground = excluirImagemBackground;
	}
	
	public Boolean getExisteImagemBackground(){
		return !getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.SEM_BACKGROUND) && (!getCaminhoBaseBackground().trim().isEmpty() || !getCorBackground().trim().isEmpty());
	}

	public Boolean getExisteAnotacaoDisciplina() {
		if (existeAnotacaoDisciplina == null) {
			existeAnotacaoDisciplina = false;
		}
		return existeAnotacaoDisciplina;
	}

	public void setExisteAnotacaoDisciplina(Boolean existeAnotacaoDisciplina) {
		this.existeAnotacaoDisciplina = existeAnotacaoDisciplina;
	}

	public void setConteudoUnidadePaginaRecursoEducacionalManterDisponivelVOs(List<ConteudoUnidadePaginaRecursoEducacionalVO> conteudoUnidadePaginaRecursoEducacionalManterDisponivelVOs) {
		this.conteudoUnidadePaginaRecursoEducacionalManterDisponivelVOs = conteudoUnidadePaginaRecursoEducacionalManterDisponivelVOs;
	}
	
	public int getSizeConteudoUnidadePaginaRecursoEducacionalAnteriorVOs() {
		if (!getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().isEmpty()) {
			return getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().size();
		}
		return 0;
	}
	
	public int getSizeConteudoUnidadePaginaRecursoEducacionalPosteriorVOs() {
		if (!getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().isEmpty()) {
			return getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().size();
		}
		return 0;
	}
	
	private List<String> listaImagensSlide;
	private String conteudoPaginaApresentar;
	
	public String getConteudoPaginaApresentar(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String altura) {
		if(conteudoPaginaApresentar == null || conteudoPaginaApresentar.trim().isEmpty()) {
			StringBuilder texto = new StringBuilder("");
			
			if (!getCaminhoBaseRepositorio().isEmpty() && getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.FLASH)) {
				texto.append("<object width=\"100%\" style=\"z-index:0;\" class=\"col-md-12 pn\" height=\"").append(altura).append("px\" >"); 
				texto.append("<param name=\"movie\" value=\"").append(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo()).append("/").append(getCaminhoBaseRepositorio().replaceAll("\\\\", "/")).append("/").append(getNomeFisicoArquivo()).append("\">");
				texto.append("<param name=\"wmode\" value=\"transparent\">");
				texto.append("<embed WMODE=\"transparent\" style=\"z-index:0;position:relative;\" width=\"100%\" height=\"").append(altura).append("\" type=\"application/x-shockwave-flash\"  class=\"col-md-12\" src=\"").append(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo()).append("/").append(getCaminhoBaseRepositorio().replaceAll("\\\\", "/")).append("/").append(getNomeFisicoArquivo()).append("\"  pluginspage=\"http://www.macromedia.com/go/getflashplayer\" />");
				texto.append("</object>");
			}else if(getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.PAGINA_HTML) && !getTexto().isEmpty()) {
				texto.append("<iframe wmode=\"transparent\" style=\"z-index:1;\"  class=\"col-md-12 pn\" width=\"100%\" height=\"").append(altura).append("px\" src=\"").append(getTexto()).append("?wmode=transparent\" frameborder=\"0\" allowfullscreen></iframe>");
			}else if(getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.IMAGEM) && !getCaminhoBaseRepositorio().isEmpty()) {
				texto.append(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo()).append("/").append(getCaminhoBaseRepositorio().replaceAll("\\\\", "/")).append("/").append(getNomeFisicoArquivo()).append("?UID=").append(Calendar.getInstance().getTime());
			}else if(getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.VIDEO_URL) && !getTexto().isEmpty()) {
				String urlvideo = getTexto();
				if (urlvideo.endsWith(".pdf") || urlvideo.contains("<iframe") || urlvideo.contains("<object") || urlvideo.contains("<OBJECT")) {					
					texto.append(urlvideo);
				} else if (urlvideo.contains("youtube") && urlvideo.indexOf("v=") > 0) {
					if (urlvideo.contains("&")) {
						urlvideo = urlvideo.substring(urlvideo.indexOf("v=") + 2, urlvideo.indexOf("&"));
					} else {
						urlvideo = urlvideo.substring(urlvideo.indexOf("v=") + 2);
					}
					texto.append("<iframe wmode=\"transparent\"  class=\"col-md-12 pn\" style=\"z-index:1;height:").append(altura).append("\"  height=\"").append(altura).append("\"  width=\"100%\" src=\"http://www.youtube.com/embed/").append(urlvideo).append("?wmode=transparent\" frameborder=\"0\" allowfullscreen></iframe>");
				} else if (urlvideo.contains("youtu.be")) {
					urlvideo = urlvideo.substring(urlvideo.lastIndexOf("/") + 1);
					texto.append("<iframe wmode=\"transparent\"  class=\"col-md-12 pn\" style=\"z-index:1;height:").append(altura).append("\" height=\"").append(altura).append("\"  width=\"100%\" src=\"http://www.youtube.com/embed/").append(urlvideo).append("?wmode=transparent\" frameborder=\"0\" allowfullscreen></iframe>");
				} else if (!urlvideo.contains("http") && !urlvideo.contains("youtu") && !urlvideo.contains("www")) {
					texto.append("<iframe wmode=\"transparent\"  class=\"col-md-12 pn\" style=\"z-index:1;height:").append(altura).append("\" height=\"").append(altura).append("\"  width=\"100%\" src=\"http://www.youtube.com/embed/").append(urlvideo).append("?wmode=transparent\" frameborder=\"0\" allowfullscreen></iframe>");
				} else {
					texto.append("<iframe wmode=\"transparent\"  class=\"col-md-12 pn\" style=\"z-index:1;height:").append(altura).append("\"; height=\"").append(altura).append("\"  width=\"100%\" src=\"").append(urlvideo).append("?wmode=transparent\" frameborder=\"0\" allowfullscreen></iframe>");
				}
			}else if(getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.AUDIO) && !getCaminhoBaseRepositorio().isEmpty()) {				
				texto.append("<audio style=\"height: calc(50%);\"  controls>" + "<source src=\"").append(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo()).append("/").append(getCaminhoBaseRepositorio().replaceAll("\\\\", "/")).append("/").append(getNomeFisicoArquivo()).append("?UID=").append(new Date().getTime()).append("\" type=\"audio/mpeg\" >Formatado de audio não reproduzido por este navegador</audio></object>");
			}else if(getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.SLIDE_IMAGEM) && !getCaminhoBaseRepositorio().isEmpty()) {
				for (String arquivo : getListaImagensSlide(configuracaoGeralSistemaVO)) {									
					texto.append("<img src=\"").append(arquivo).append("\" style=\"height:").append(altura).append(";\" />");					
				}				
			}
			conteudoPaginaApresentar = texto.toString();
		}
		return conteudoPaginaApresentar;
	}

	public void setConteudoPaginaApresentar(String conteudoPaginaApresentar) {
		this.conteudoPaginaApresentar = conteudoPaginaApresentar;
	}

	public List<String> getListaImagensSlide(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		if(listaImagensSlide == null) {
			listaImagensSlide =  new ArrayList<String>(0);
			if(getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.SLIDE_IMAGEM) && !getCaminhoBaseRepositorio().toString().isEmpty()) {
				String[] nomeArquivos;
				String[] caminhosArquivos;
				nomeArquivos = getNomeFisicoArquivo().split(",");
				caminhosArquivos = getCaminhoBaseRepositorio().split(",");
				String url = "";				
				int x= 0;
				for (String nomeArquivo : nomeArquivos) {
					if(!nomeArquivo.toString().isEmpty() && !nomeArquivo.contains(",")) {
						url = configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + caminhosArquivos[x].replaceAll("\\\\", "/").trim() + "/" + nomeArquivo.trim();	
						listaImagensSlide.add(url);
					}
					x++;
				}				
			}
		}
		return listaImagensSlide;
	}

	public void setListaImagensSlide(List<String> listaImagensSlide) {
		this.listaImagensSlide = listaImagensSlide;
	}
	
}
