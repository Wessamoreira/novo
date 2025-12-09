/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.administrativo;

/**
 *
 * @author Rodrigo
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.PlanoFinanceiroAlunoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ConvenioVO;

public class ContaReceberPainelGestorVO implements Serializable {

    private ContaReceberVO contaReceberVO;
    private List<ConvenioVO> listaConvenio;
    private List<PlanoFinanceiroAlunoVO> listaPlanoFinanceiroAluno;
    private List<PlanoDescontoVO> listaPlanoDesconto;
    private List<DescontoProgressivoVO> listaDescontoProgressivo;
    private Map<Integer, String> hashMapDesconto;
    private String mesAno;
    private Double valorRecebidoNoMes;
    private Double valorRecebidoDoMes;
    private Boolean contaDoPeriodo;
    
    
    public static final long serialVersionUID = 1L;

    public Boolean getContaDoPeriodo() {
        if (contaDoPeriodo == null) {
            contaDoPeriodo = Boolean.TRUE;
        }
        return contaDoPeriodo;
    }

    public void setContaDoPeriodo(Boolean contaDoPeriodo) {
        this.contaDoPeriodo = contaDoPeriodo;
    }

    public Double getValorRecebidoDoMes() {
        if (valorRecebidoDoMes == null) {
            valorRecebidoDoMes = 0.0;
        }
        return valorRecebidoDoMes;
    }

    public void setValorRecebidoDoMes(Double valorRecebidoDoMes) {
        this.valorRecebidoDoMes = valorRecebidoDoMes;
    }

    public Double getValorRecebidoNoMes() {
        if (valorRecebidoNoMes == null) {
            valorRecebidoNoMes = 0.0;
        }
        return valorRecebidoNoMes;
    }

    public void setValorRecebidoNoMes(Double valorRecebidoNoMes) {
        this.valorRecebidoNoMes = valorRecebidoNoMes;
    }

    public String getMesAno() {
        if (mesAno == null) {
            mesAno = "";
        }
        return mesAno;
    }

    public void setMesAno(String mesAno) {
        this.mesAno = mesAno;
    }

    public ContaReceberVO getContaReceberVO() {
        if (contaReceberVO == null) {
            contaReceberVO = new ContaReceberVO();
        }
        return contaReceberVO;
    }

    public void setContaReceberVO(ContaReceberVO contaReceberVO) {
        this.contaReceberVO = contaReceberVO;
    }

    public Map<Integer, String> getHashMapDesconto() {
        if (hashMapDesconto == null) {
            hashMapDesconto = new HashMap<Integer, String>();
        }
        return hashMapDesconto;
    }

    public void setHashMapDesconto(Map<Integer, String> hashMapDesconto) {
        this.hashMapDesconto = hashMapDesconto;
    }

    public List<ConvenioVO> getListaConvenio() {
        if (listaConvenio == null) {
            listaConvenio = new ArrayList<ConvenioVO>(0);
        }
        return listaConvenio;
    }

    public void setListaConvenio(List<ConvenioVO> listaConvenio) {
        this.listaConvenio = listaConvenio;
    }

    public List<DescontoProgressivoVO> getListaDescontoProgressivo() {
        if (listaDescontoProgressivo == null) {
            listaDescontoProgressivo = new ArrayList<DescontoProgressivoVO>(0);
        }

        return listaDescontoProgressivo;
    }

    public void setListaDescontoProgressivo(List<DescontoProgressivoVO> listaDescontoProgressivo) {
        this.listaDescontoProgressivo = listaDescontoProgressivo;
    }

    public List<PlanoDescontoVO> getListaPlanoDesconto() {
        if (listaPlanoDesconto == null) {
            listaPlanoDesconto = new ArrayList<PlanoDescontoVO>(0);
        }
        return listaPlanoDesconto;
    }

    public void setListaPlanoDesconto(List<PlanoDescontoVO> listaPlanoDesconto) {
        this.listaPlanoDesconto = listaPlanoDesconto;
    }

    public List<PlanoFinanceiroAlunoVO> getListaPlanoFinanceiroAluno() {
        if (listaPlanoFinanceiroAluno == null) {
            listaPlanoFinanceiroAluno = new ArrayList<PlanoFinanceiroAlunoVO>(0);
        }
        return listaPlanoFinanceiroAluno;
    }

    public void setListaPlanoFinanceiroAluno(List<PlanoFinanceiroAlunoVO> listaPlanoFinanceiroAluno) {
        this.listaPlanoFinanceiroAluno = listaPlanoFinanceiroAluno;
    }

}
