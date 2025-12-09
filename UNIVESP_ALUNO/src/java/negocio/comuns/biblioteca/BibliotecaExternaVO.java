package negocio.comuns.biblioteca;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

public class BibliotecaExternaVO extends SuperVO {

    private Integer codigo;
    private String nome;
    private Integer configuracaoBiblioteca;
    private String url;
    public static final long serialVersionUID = 1L;

    public BibliotecaExternaVO() {
        super();
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getUrl() {
        if (url == null) {
            url = "";
        }
        return (url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getConfiguracaoBiblioteca() {
        if (configuracaoBiblioteca == null) {
            configuracaoBiblioteca = 0;
        }
        return configuracaoBiblioteca;
    }

    public void setConfiguracaoBiblioteca(Integer configuracaoBiblioteca) {
        this.configuracaoBiblioteca = configuracaoBiblioteca;
    }

    public String getNome() {
        if (nome == null) {
            nome = "";
        }
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static void validarDados(BibliotecaExternaVO obj) throws ConsistirException {
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo BIBLIOTECA EXTERNA (Nome) deve ser informado.");
        }
        if (obj.getUrl().equals("")) {
            throw new ConsistirException("O campo BIBLIOTECA EXTERNA (Url) deve ser informado.");
        }
    }
    
    
    public String urlApresentar;
    public String getUrlApresentar(){
    	if(urlApresentar == null){
    		if(getUrl().startsWith("http")){
    			urlApresentar = getUrl();
    		}else{
    			urlApresentar = "http://"+getUrl();
    		}
    	}
    	return urlApresentar;
    }
}
