package relatorio.negocio.comuns.crm;

import java.util.ArrayList;
import java.util.List;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.crm.RankingVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author Carlos
 */
public class ExtratoComissaoRelVO extends SuperVO {

    private FuncionarioVO funcionarioVO;
    private Double valorComissao;
    private TurmaVO turmaVO;
    private List<RankingVO> listaRankingVOs;
    private String nomeConsultor;

    public ExtratoComissaoRelVO() {
    }

    public JRDataSource getListaRanking() {
        return new JRBeanArrayDataSource(getListaRankingVOs().toArray());
    }

    public TurmaVO getTurmaVO() {
        if (turmaVO == null) {
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
    }

    public FuncionarioVO getFuncionarioVO() {
        if (funcionarioVO == null) {
            funcionarioVO = new FuncionarioVO();
        }
        return funcionarioVO;
    }

    public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
        this.funcionarioVO = funcionarioVO;
    }

    public Double getValorComissao() {
        if (valorComissao == null) {
            valorComissao = 0.0;
        }
        return valorComissao;
    }

    public void setValorComissao(Double valorComissao) {
        this.valorComissao = valorComissao;
    }

    public List<RankingVO> getListaRankingVOs() {
        if (listaRankingVOs == null) {
            listaRankingVOs = new ArrayList(0);
        }
        return listaRankingVOs;
    }

    public void setListaRankingVOs(List<RankingVO> listaRankingVOs) {
        this.listaRankingVOs = listaRankingVOs;
    }

    public String getNomeConsultor() {
        if (nomeConsultor == null) {
            nomeConsultor = "";
        }
        return nomeConsultor;
    }

    public void setNomeConsultor(String nomeConsultor) {
        this.nomeConsultor = nomeConsultor;
    }
}
