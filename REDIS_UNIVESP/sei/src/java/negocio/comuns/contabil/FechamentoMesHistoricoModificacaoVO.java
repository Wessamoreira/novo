package negocio.comuns.contabil;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
	
public class FechamentoMesHistoricoModificacaoVO extends SuperVO {

    protected Integer codigo;
    protected FechamentoMesVO fechamentoMes;
	protected String descricao;
	protected UsuarioVO usuarioResponsavel;
	protected TipoOrigemHistoricoBloqueioEnum tipoOrigemBloqueio;
	protected Integer codigoOrigem;
	protected Date dataModificacaoBloqueio;
	protected String detalheModificacao;

    public static final long serialVersionUID = 1L;
    public FechamentoMesHistoricoModificacaoVO() {
        super();
        inicializarDados();
    }

    public static void validarDados(FechamentoMesHistoricoModificacaoVO obj) throws ConsistirException {
        if ((obj.getFechamentoMes() == null) || (obj.getFechamentoMes().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo FECHAMENTO COMPETÊNCIA (Histórico de Modificação Fechamento da Competência) deve ser informado.");
        }
        if ((obj.getUsuarioResponsavel() == null) || (obj.getUsuarioResponsavel().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo USUÁRIO (Histórico de Modificação Fechamento da Competência) deve ser informado.");
        }
        if (obj.getDataModificacaoBloqueio() == null) {
            throw new ConsistirException("O campo DATA MODIFICAÇÃO (Histórico de Modificação Fechamento da Competência) deve ser informado.");
        }
        if ((obj.getDescricao() == null) || (obj.getDescricao().equals(""))) {
            throw new ConsistirException("O campo DESCRIÇÃO (Histórico de Modificação Fechamento da Competência) deve ser informado.");
        }        
    }

    public void inicializarDados() {
        setCodigo(0);
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
    
    public FechamentoMesVO getFechamentoMes() {
    	if (fechamentoMes == null) { 
    		fechamentoMes = new FechamentoMesVO(); 
    	}
		return fechamentoMes;
	}

	public void setFechamentoMes(FechamentoMesVO fechamentoMes) {
		this.fechamentoMes = fechamentoMes;
	}

	public String getDescricao() {
		if (descricao == null) { 
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public UsuarioVO getUsuarioResponsavel() {
		if (usuarioResponsavel == null) { 
			usuarioResponsavel = new UsuarioVO();
		}
		return usuarioResponsavel;
	}

	public void setUsuarioResponsavel(UsuarioVO usuarioResponsavel) {
		this.usuarioResponsavel = usuarioResponsavel;
	}

	public TipoOrigemHistoricoBloqueioEnum getTipoOrigemBloqueio() {
		if (tipoOrigemBloqueio == null) { 
			tipoOrigemBloqueio = TipoOrigemHistoricoBloqueioEnum.FECHAMENTOMES; 
		}
		return tipoOrigemBloqueio;
	}
	
	public String getTipoOrigemBloqueio_Apresentar() {
		return getTipoOrigemBloqueio().getValorApresentar();
	}

	public void setTipoOrigemBloqueio(TipoOrigemHistoricoBloqueioEnum tipoOrigemBloqueio) {
		this.tipoOrigemBloqueio = tipoOrigemBloqueio;
	}

	public Integer getCodigoOrigem() {
		if (codigoOrigem == null) { 
			codigoOrigem = 0;
		}
		return codigoOrigem;
	}

	public void setCodigoOrigem(Integer codigoOrigem) {
		this.codigoOrigem = codigoOrigem;
	}
	
	public String getDataModificacaoBloqueio_Apresentar() {
		if (dataModificacaoBloqueio == null) {
			return "";
		}
		return (Uteis.getDataComHora(dataModificacaoBloqueio));
	}	

	public Date getDataModificacaoBloqueio() {
		if (dataModificacaoBloqueio == null) { 
			dataModificacaoBloqueio = new Date();
		}
		return dataModificacaoBloqueio;
	}

	public void setDataModificacaoBloqueio(Date dataModificacaoBloqueio) {
		this.dataModificacaoBloqueio = dataModificacaoBloqueio;
	}

	public String getDetalheModificacao() {
		if (detalheModificacao == null) { 
			detalheModificacao = "";
		}
		return detalheModificacao;
	}

	public void setDetalheModificacao(String detalheModificacao) {
		this.detalheModificacao = detalheModificacao;
	}
    
}
