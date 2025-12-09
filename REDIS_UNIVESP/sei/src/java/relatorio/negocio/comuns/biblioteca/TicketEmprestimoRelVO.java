package relatorio.negocio.comuns.biblioteca;

import java.util.Date;
import java.util.List;



import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.ItemEmprestimoVO;

/**
 * Reponsável por manter os dados da entidade Emprestimo. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class TicketEmprestimoRelVO {

    private Date data;
    private String situacao;
    private Double valorTotalMulta;
    private String tipoPessoa;
    private List<ItemEmprestimoVO> itemEmprestimoVOs;
    private UsuarioVO atendente;
    private BibliotecaVO biblioteca;
    private PessoaVO pessoa;

    public UsuarioVO getAtendente() {
        return atendente;
    }

    public void setAtendente(UsuarioVO atendente) {
        this.atendente = atendente;
    }

    public BibliotecaVO getBiblioteca() {
        return biblioteca;
    }

    public void setBiblioteca(BibliotecaVO biblioteca) {
        this.biblioteca = biblioteca;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public List<ItemEmprestimoVO> getItemEmprestimoVOs() {
        return itemEmprestimoVOs;
    }

    public void setItemEmprestimoVOs(List<ItemEmprestimoVO> itemEmprestimoVOs) {
        this.itemEmprestimoVOs = itemEmprestimoVOs;
    }

    public PessoaVO getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoaVO pessoa) {
        this.pessoa = pessoa;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public Double getValorTotalMulta() {
        return valorTotalMulta;
    }

    public void setValorTotalMulta(Double valorTotalMulta) {
        this.valorTotalMulta = valorTotalMulta;
    }
}
