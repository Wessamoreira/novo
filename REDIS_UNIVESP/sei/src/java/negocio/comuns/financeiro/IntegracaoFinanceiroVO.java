package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

public class IntegracaoFinanceiroVO extends SuperVO {

    private Integer codigo;
    private Date dataProcessamento;
    private UsuarioVO responsavel;
    private ArquivoVO arquivo;
    private List<IntegracaoFinanceiroContaReceberVO> integracaoFinanceiroContaReceberVOs;
    public static final long serialVersionUID = 1L;

    public IntegracaoFinanceiroVO() {
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

	public Date getDataProcessamento() {
		return dataProcessamento;
	}

	public void setDataProcessamento(Date dataProcessamento) {
		this.dataProcessamento = dataProcessamento;
	}
	
    public String getDataProcessamento_Apresentar() {
        return (Uteis.getDataComHora(dataProcessamento));
    }

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public ArquivoVO getArquivo() {
		if (arquivo == null) {
			arquivo = new ArquivoVO();
		}
		return arquivo;
	}

	public void setArquivo(ArquivoVO arquivo) {
		this.arquivo = arquivo;
	}

	public List<IntegracaoFinanceiroContaReceberVO> getIntegracaoFinanceiroContaReceberVOs() {
		if (integracaoFinanceiroContaReceberVOs == null) {
			integracaoFinanceiroContaReceberVOs = new ArrayList<IntegracaoFinanceiroContaReceberVO>(0);
		}
		return integracaoFinanceiroContaReceberVOs;
	}

	public void setIntegracaoFinanceiroContaReceberVOs(
			List<IntegracaoFinanceiroContaReceberVO> integracaoFinanceiroContaReceberVOs) {
		this.integracaoFinanceiroContaReceberVOs = integracaoFinanceiroContaReceberVOs;
	}
	
	public int getQtdeProcessadaComSucesso() {
		int contador = 0;
		for (IntegracaoFinanceiroContaReceberVO integracao : getIntegracaoFinanceiroContaReceberVOs()) {
			if (integracao.getProcessadoComSucesso()) {
				contador++;
			}
		}
		return contador;
	}
	
	public int getQtdeProcessadaComFalha() {
		return getIntegracaoFinanceiroContaReceberVOs().size() - getQtdeProcessadaComSucesso();
	}
	
	public int getQtdeTotalProcessada() {
		return getIntegracaoFinanceiroContaReceberVOs().size();
	}
}
