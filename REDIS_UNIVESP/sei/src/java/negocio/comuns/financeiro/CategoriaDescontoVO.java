package negocio.comuns.financeiro;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 *
 * @see SuperVO
 */
public class CategoriaDescontoVO extends SuperVO {

    private Integer codigo;
    private String nome;
    public static final long serialVersionUID = 1L;

    public CategoriaDescontoVO() {
        super();
    }

    public static void validarDados(CategoriaDescontoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Categoria Desconto) deve ser informado.");
        }
    }

    public String getNome() {
        if (nome == null) {
            nome = "";
        }
        return (nome);
    }

    public void setNome(String nome) {
        this.nome = nome;
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

}

