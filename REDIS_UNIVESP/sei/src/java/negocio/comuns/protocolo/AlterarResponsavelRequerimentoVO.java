package negocio.comuns.protocolo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

public class AlterarResponsavelRequerimentoVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String motivoAlteracao;
	private Date dataAlteracao;
	private UsuarioVO responsavelAlteracao;
	private FuncionarioVO responsavelRequerimento;
	private FuncionarioVO responsavelAnterior;
	private String requerimentos;
	
	/* Transient */
	private List<RequerimentoVO> requerimentoVOs;

	/**
	 * Construtor padrão da classe <code>AlterarResponsavelRequerimentoVO</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public AlterarResponsavelRequerimentoVO() {
		super();
		inicializarDados();
	}
	
	public void inicializarDados() {

	}

	public static void validarDados(AlterarResponsavelRequerimentoVO obj) throws ConsistirException {
		if (obj.getDataAlteracao() == null) {
			throw new ConsistirException("O campo DATA (Alterar Responsável Requerimento) deve ser informado.");
		}
		if (obj.getMotivoAlteracao().trim().isEmpty()) {
			throw new ConsistirException("O campo MOTIVO (Alterar Responsável Requerimento) deve ser informado.");
		}
		if (obj.getResponsavelRequerimento().getCodigo().equals(0)) {
			throw new ConsistirException("O campo NOVO FUNCIONARIO (Alterar Responsável Requerimento) deve ser informado.");
		}
		if (obj.getResponsavelAnterior().getCodigo().equals(0)) {
			throw new ConsistirException("O campo RESPONSAVEL ANTERIOR (Alterar Responsável Requerimento) deve ser informado.");
		}
		boolean vazio = true;
		for (RequerimentoVO req: obj.getRequerimentoVOs()) {
			if (req.getSelecionado()) {
				vazio = false;
				break;
			}
		}
		if (vazio) {
			throw new ConsistirException("Deve ser selecionado pelo menos 1 (um) requerimento.");
		}
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

	public FuncionarioVO getResponsavelRequerimento() {
		if (responsavelRequerimento == null) {
			responsavelRequerimento = new FuncionarioVO();
		}
		return responsavelRequerimento;
	}

	public void setResponsavelRequerimento(FuncionarioVO responsavelRequerimento) {
		this.responsavelRequerimento = responsavelRequerimento;
	}

	public String getMotivoAlteracao() {
		if (motivoAlteracao == null) {
			motivoAlteracao = "";
		}
		return motivoAlteracao;
	}
	
	public String getMotivoAlteracaoApresentar() {
		if (getMotivoAlteracao().length() > 50) {
			return getMotivoAlteracao().substring(0, 47) + "...";
		} else {
			return getMotivoAlteracao();
		}
	}

	public void setMotivoAlteracao(String motivoAlteracao) {
		this.motivoAlteracao = motivoAlteracao;
	}

	public Date getDataAlteracao() {
		if (dataAlteracao == null) {
			dataAlteracao = new Date();
		}
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}
	
	public String getDataAlteracaoApresentar() {
		return (Uteis.getDataComHora(dataAlteracao));
	}

	public UsuarioVO getResponsavelAlteracao() {
		if (responsavelAlteracao == null) {
			responsavelAlteracao = new UsuarioVO();
		}
		return responsavelAlteracao;
	}

	public void setResponsavelAlteracao(UsuarioVO responsavelAlteracao) {
		this.responsavelAlteracao = responsavelAlteracao;
	}

	public String getRequerimentos() {
		if (requerimentos == null) {
			requerimentos = "";
		}
		return requerimentos;
	}

	public void setRequerimentos(String requerimentos) {
		this.requerimentos = requerimentos;
	}

	public List<RequerimentoVO> getRequerimentoVOs() {
		if (requerimentoVOs == null) {
			requerimentoVOs = new ArrayList<RequerimentoVO>(0);
		}
		return requerimentoVOs;
	}

	public void setRequerimentoVOs(List<RequerimentoVO> requerimentoVOs) {
		this.requerimentoVOs = requerimentoVOs;
	}

	public FuncionarioVO getResponsavelAnterior() {
		if (responsavelAnterior == null) {
			responsavelAnterior = new FuncionarioVO();
		}
		return responsavelAnterior;
	}

	public void setResponsavelAnterior(FuncionarioVO responsavelAnterior) {
		this.responsavelAnterior = responsavelAnterior;
	}

}
