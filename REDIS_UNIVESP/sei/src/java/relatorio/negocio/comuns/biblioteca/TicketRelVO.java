package relatorio.negocio.comuns.biblioteca;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.ItemEmprestimoVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 * Reponsável por manter os dados da entidade Emprestimo. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class TicketRelVO implements Serializable{

    private String nomeBiblioteca;
    private String textoPadrao;
    private String textoPadraoDevolucao;
    private String textoPadraoRenovacao;
    private String numeroMatricula;
    private String tipoPessoa;
    private String nomePessoa;
    private String nomeCurso;
    private List<ItemEmprestimoVO> itemEmprestimoVOs;
    private List<ItemEmprestimoVO> itemEmprestimoVOsDevolucao;
    private List<ItemEmprestimoVO> itemEmprestimoVOsRenovacao;
//    private List<ExemplarVO> itemEmprestimoVOsRenovacao;

    public String getNomeBiblioteca() {
        return nomeBiblioteca;
    }

    public void setNomeBiblioteca(String nomeBiblioteca) {
        this.nomeBiblioteca = nomeBiblioteca;
    }

    public String getTextoPadrao() {
        return textoPadrao;
    }

    public void setTextoPadrao(String textoPadrao) {
        this.textoPadrao = textoPadrao;
    }

    public String getNumeroMatricula() {
        return numeroMatricula;
    }

    public void setNumeroMatricula(String numeroMatricula) {
        this.numeroMatricula = numeroMatricula;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    public String getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public List<ItemEmprestimoVO> getItemEmprestimoVOs() {
        if (itemEmprestimoVOs == null) {
            itemEmprestimoVOs = new ArrayList<ItemEmprestimoVO>(0);
        }
        return (itemEmprestimoVOs);
    }

    public JRDataSource getItemEmprestimoVOsJR() {
        JRDataSource itemEmprestimoVOsJR = new JRBeanArrayDataSource(getItemEmprestimoVOs().toArray());
        return itemEmprestimoVOsJR;
    }

    public void setItemEmprestimoVOs(List<ItemEmprestimoVO> itemEmprestimoVOs) {
        this.itemEmprestimoVOs = itemEmprestimoVOs;
    }

    public List<ItemEmprestimoVO> getItemEmprestimoVOsDevolucao() {
        if (itemEmprestimoVOsDevolucao == null) {
            itemEmprestimoVOsDevolucao = new ArrayList<ItemEmprestimoVO>(0);
        }
        return itemEmprestimoVOsDevolucao;
    }

    public JRDataSource getItemEmprestimoVOsDevolucaoJR() {
        JRDataSource itemEmprestimoVOsDevolucaoJR = new JRBeanArrayDataSource(getItemEmprestimoVOsDevolucao().toArray());
        return itemEmprestimoVOsDevolucaoJR;
    }

    public void setItemEmprestimoVOsDevolucao(List<ItemEmprestimoVO> itemEmprestimoVOsDevolucao) {
        this.itemEmprestimoVOsDevolucao = itemEmprestimoVOsDevolucao;
    }

    public List<ItemEmprestimoVO> getItemEmprestimoVOsRenovacao() {
        if (itemEmprestimoVOsRenovacao == null) {
            itemEmprestimoVOsRenovacao = new ArrayList<ItemEmprestimoVO>(0);
        }
        return itemEmprestimoVOsRenovacao;
    }
//    public List<ExemplarVO> getItemEmprestimoVOsRenovacao() {
//    	if (itemEmprestimoVOsRenovacao == null) {
//    		itemEmprestimoVOsRenovacao = new ArrayList<ExemplarVO>(0);
//    	}
//    	return itemEmprestimoVOsRenovacao;
//    }

    public JRDataSource getItemEmprestimoVOsRenovacaoJR() {
        JRDataSource itemEmprestimoVOsRenovacaoJR = new JRBeanArrayDataSource(getItemEmprestimoVOsRenovacao().toArray());
        return itemEmprestimoVOsRenovacaoJR;
    }

//    public void setItemEmprestimoVOsRenovacao(List<ExemplarVO> itemEmprestimoVOsRenovacao) {
//        this.itemEmprestimoVOsRenovacao = itemEmprestimoVOsRenovacao;
//    }
    public void setItemEmprestimoVOsRenovacao(List<ItemEmprestimoVO> itemEmprestimoVOsRenovacao) {
    	this.itemEmprestimoVOsRenovacao = itemEmprestimoVOsRenovacao;
    }

    public String getTextoPadraoDevolucao() {
        if (textoPadraoDevolucao == null) {
            textoPadraoDevolucao = "";
        }
        return textoPadraoDevolucao;
    }

    public void setTextoPadraoDevolucao(String textoPadraoDevolucao) {
        this.textoPadraoDevolucao = textoPadraoDevolucao;
    }

	public String getTextoPadraoRenovacao() {
		if (textoPadraoRenovacao == null) {
			textoPadraoRenovacao = "";
		}
		return textoPadraoRenovacao;
	}

	public void setTextoPadraoRenovacao(String textoPadraoRenovacao) {
		this.textoPadraoRenovacao = textoPadraoRenovacao;
	}
}
