/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.compras;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * 
 * @author Rodrigo
 */
public class FornecedorCategoriaProdutoVO extends SuperVO {

    private Integer codigo;
    private Integer fornecedor;
    private CategoriaProdutoVO categoriaProdutoVO;
    public static final long serialVersionUID = 1L;

    public FornecedorCategoriaProdutoVO() {
        inicializarDados();
    }

    public static void validarDados(FornecedorCategoriaProdutoVO obj) throws ConsistirException {
        if (obj.getCategoriaProdutoVO() == null || obj.getCategoriaProdutoVO().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo CATEGORIA PRODUTO (Fornecedor Categoria Produto) deve ser informado");
        }
    }

    public void inicializarDados() {
        setCodigo(0);
        setFornecedor(0);
    }

    public CategoriaProdutoVO getCategoriaProdutoVO() {
        if (categoriaProdutoVO == null) {
            categoriaProdutoVO = new CategoriaProdutoVO();
        }
        return categoriaProdutoVO;
    }

    public void setCategoriaProdutoVO(CategoriaProdutoVO categoriaProdutoVO) {
        this.categoriaProdutoVO = categoriaProdutoVO;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Integer fornecedor) {
        this.fornecedor = fornecedor;
    }
}
