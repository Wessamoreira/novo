package negocio.comuns.administrativo;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade FraseInspiracao. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class FraseInspiracaoVO extends SuperVO {

    private Integer codigo;
    private String frase;
    private String autor;
    private Integer quantidadeExibicoes;
    private Date dataUltimaExibicao;
    private UsuarioVO responsavelCadastro;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>FraseInspiracao</code>. Cria uma nova instância
     * desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public FraseInspiracaoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>FraseInspiracaoVO</code>. Todos os tipos de consistência de dados são e devem
     * ser implementadas neste método. São validações típicas: verificação de
     * campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(FraseInspiracaoVO obj) throws ConsistirException {
        if (obj.getFrase().equals("")) {
            throw new ConsistirException("O campo NOME (FRASE INSPIRAÇÃO) deve ser informado.");
        }
        if (obj.getResponsavelCadastro() == null) {
            throw new ConsistirException("O campo RESPONSÁVEL (FRASE INSPIRAÇÃO) deve ser informado.");
        }
        if (obj.getAutor().trim().equals("")) {
            obj.setAutor("Autor Desconhecido");
        }
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

    public String getFrase() {
        if (frase == null) {
            frase = "";
        }
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public String getAutor() {
        if (autor == null) {
            autor = "";
        }
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Integer getQuantidadeExibicoes() {
        if (quantidadeExibicoes == null) {
            quantidadeExibicoes = 0;
        }
        return quantidadeExibicoes;
    }

    public void setQuantidadeExibicoes(Integer quantidadeExibicoes) {
        this.quantidadeExibicoes = quantidadeExibicoes;
    }

    public Date getDataUltimaExibicao() {
        return dataUltimaExibicao;
    }

    public String getDataUltimoAcesso() {
        if (dataUltimaExibicao == null) {
            Uteis.getData(new Date());
        }
        return Uteis.getData(dataUltimaExibicao);
    }

    public void setDataUltimaExibicao(Date dataUltimaExibicao) {
        this.dataUltimaExibicao = dataUltimaExibicao;
    }

    public UsuarioVO getResponsavelCadastro() {
        if (responsavelCadastro == null) {
            return new UsuarioVO();
        }
        return responsavelCadastro;
    }

    public void setResponsavelCadastro(UsuarioVO responsavelCadastro) {
        this.responsavelCadastro = responsavelCadastro;
    }
}
