package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.academico.enumeradores.SituacaoRecursoEducacionalEnum;
import negocio.comuns.academico.enumeradores.TipoGraficoEnum;
import negocio.comuns.academico.enumeradores.TipoRecursoEducacionalEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;


public class RecursoEducacionalVO extends SuperVO {

    /**
     * 
     */
    private static final long serialVersionUID = 7801306757067689037L;
    
    private Integer codigo;
    private TipoRecursoEducacionalEnum tipoRecursoEducacional;
    private SituacaoRecursoEducacionalEnum situacaoRecursoEducacional;
    private String titulo;
    private String descricao;
    private String texto;
    private Integer altura;
    private Integer largura;
    private DisciplinaVO disciplina;
    private String caminhoBaseRepositorio;
    private String nomeRealArquivo;
    private String nomeFisicoArquivo;
    
    private Date dataCadastro;
    private UsuarioVO usuarioCadastro;
    private Date dataAlteracao;
    private UsuarioVO usuarioAlteracao;
    private String tituloGrafico;
    private String tituloEixoX;
    private String tituloEixoY;
    private Boolean apresentarLegenda;
    private String valorGrafico;
    private String categoriaGrafico;
    private TipoGraficoEnum tipoGrafico;
    
    
    private ConteudoUnidadePaginaVO conteudoUnidadePagina;
    private ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacional;

    
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
        return tipoRecursoEducacional;
    }

    
    public void setTipoRecursoEducacional(TipoRecursoEducacionalEnum tipoRecursoEducacional) {
        this.tipoRecursoEducacional = tipoRecursoEducacional;
    }

    
    public SituacaoRecursoEducacionalEnum getSituacaoRecursoEducacional() {
        if(situacaoRecursoEducacional == null){
            situacaoRecursoEducacional = SituacaoRecursoEducacionalEnum.ATIVO;
        }
        return situacaoRecursoEducacional;
    }

    
    public void setSituacaoRecursoEducacional(SituacaoRecursoEducacionalEnum situacaoRecursoEducacional) {
        this.situacaoRecursoEducacional = situacaoRecursoEducacional;
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

    
    public String getDescricao() {
        if(descricao == null){
            descricao = "";
        }
        return descricao;
    }

    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    
    public String getTexto() {
        if(texto == null){
            texto = "";
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
            largura = 600;
        }
        return largura;
    }

    
    public void setLargura(Integer largura) {
        this.largura = largura;
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

    
    public String getCaminhoBaseRepositorio() {
        if(caminhoBaseRepositorio == null){
            caminhoBaseRepositorio = "";
        }
        return caminhoBaseRepositorio;
    }

    
    public void setCaminhoBaseRepositorio(String caminhoBaseRepositorio) {
        this.caminhoBaseRepositorio = caminhoBaseRepositorio;
    }

    
    public String getNomeRealArquivo() {
        if(nomeRealArquivo == null){
            nomeRealArquivo = "";
        }
        return nomeRealArquivo;
    }

    
    public void setNomeRealArquivo(String nomeRealArquivo) {
        this.nomeRealArquivo = nomeRealArquivo;
    }

    
    public String getNomeFisicoArquivo() {
        if(nomeFisicoArquivo == null){
            nomeFisicoArquivo = "";
        }
        return nomeFisicoArquivo;
    }

    
    public void setNomeFisicoArquivo(String nomeFisicoArquivo) {
        this.nomeFisicoArquivo = nomeFisicoArquivo;
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

    
    public ConteudoUnidadePaginaVO getConteudoUnidadePagina() {
        if(conteudoUnidadePagina == null){
            conteudoUnidadePagina = new ConteudoUnidadePaginaVO();
        }
        return conteudoUnidadePagina;
    }

    
    public void setConteudoUnidadePagina(ConteudoUnidadePaginaVO conteudoUnidadePagina) {
        this.conteudoUnidadePagina = conteudoUnidadePagina;
    }


    
    public ConteudoUnidadePaginaRecursoEducacionalVO getConteudoUnidadePaginaRecursoEducacional() {
        if(conteudoUnidadePaginaRecursoEducacional == null){
            conteudoUnidadePaginaRecursoEducacional = new ConteudoUnidadePaginaRecursoEducacionalVO();
        }
        return conteudoUnidadePaginaRecursoEducacional;
    }


    
    public void setConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacional) {
        this.conteudoUnidadePaginaRecursoEducacional = conteudoUnidadePaginaRecursoEducacional;
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
    
    public String getCategoriaGrafico() {
        if(categoriaGrafico == null){
            categoriaGrafico = "";
        }
        return categoriaGrafico;
    }

    
    public void setCategoriaGrafico(String categoriaGrafico) {
        this.categoriaGrafico = categoriaGrafico;
    }

    private Boolean treeFilho;


	public Boolean getTreeFilho() {
		if (treeFilho == null) {
			treeFilho = true;
		}
		return treeFilho;
	}


	public void setTreeFilho(Boolean treeFilho) {
		this.treeFilho = treeFilho;
	}
    
    

}
