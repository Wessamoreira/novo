package negocio.comuns.gsuite;

import java.util.Date;

import negocio.comuns.administrativo.enumeradores.TipoAdminSdkIntegracaoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.gsuite.enumeradores.TipoServicoAdminSdkGoogleEnum;

public class AdminSdkIntegracaoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6616529832513608198L;

	private Integer codigo;
	private UsuarioVO usuarioVO;
	private Date dataRegistro;
	private Long totalRegistro;
	private Long totalProcessado;
	private Long totalErro;
	boolean criarContaFuncionario = false; 
	boolean criarContaAluno = false;
	private TipoServicoAdminSdkGoogleEnum tipoServicoAdminSdkGoogleEnum;
	private TipoAdminSdkIntegracaoEnum tipoAdminSdkIntegracaoEnum;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public UsuarioVO getUsuarioVO() {
		if (usuarioVO == null) {
			usuarioVO = new UsuarioVO();
		}
		return usuarioVO;
	}

	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}

	public Date getDataRegistro() {
		if (dataRegistro == null) {
			dataRegistro = new Date();
		}
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public Long getTotalRegistro() {
		if (totalRegistro == null) {
			totalRegistro = 0L;
		}
		return totalRegistro;
	}

	public void setTotalRegistro(Long totalRegistro) {
		this.totalRegistro = totalRegistro;
	}

	public Long getTotalProcessado() {
		if (totalProcessado == null) {
			totalProcessado = 0L;
		}
		return totalProcessado;
	}

	public void setTotalProcessado(Long totalProcessado) {
		this.totalProcessado = totalProcessado;
	}

	public Long getTotalErro() {
		if (totalErro == null) {
			totalErro = 0L;
		}
		return totalErro;
	}

	public void setTotalErro(Long totalErro) {
		this.totalErro = totalErro;
	}	
	
	public boolean isCriarContaFuncionario() {
		return criarContaFuncionario;
	}

	public void setCriarContaFuncionario(boolean criarContaFuncionario) {
		this.criarContaFuncionario = criarContaFuncionario;
	}

	public boolean isCriarContaAluno() {
		return criarContaAluno;
	}

	public void setCriarContaAluno(boolean criarContaAluno) {
		this.criarContaAluno = criarContaAluno;
	}	
	
	public TipoServicoAdminSdkGoogleEnum getTipoServicoAdminSdkGoogleEnum() {
		if(tipoServicoAdminSdkGoogleEnum == null) {
			tipoServicoAdminSdkGoogleEnum = TipoServicoAdminSdkGoogleEnum.NENHUM;
		}
		return tipoServicoAdminSdkGoogleEnum;
	}

	public void setTipoServicoAdminSdkGoogleEnum(TipoServicoAdminSdkGoogleEnum tipoServicoAdminSdkGoogleEnum) {
		this.tipoServicoAdminSdkGoogleEnum = tipoServicoAdminSdkGoogleEnum;
	}

	public TipoAdminSdkIntegracaoEnum getTipoAdminSdkIntegracaoEnum() {
		if (tipoAdminSdkIntegracaoEnum == null) {
			tipoAdminSdkIntegracaoEnum = TipoAdminSdkIntegracaoEnum.NENHUM;
		}
		return tipoAdminSdkIntegracaoEnum;
	}

	public void setTipoAdminSdkIntegracaoEnum(TipoAdminSdkIntegracaoEnum tipoAdminSdkIntegracaoEnum) {
		this.tipoAdminSdkIntegracaoEnum = tipoAdminSdkIntegracaoEnum;
	}	
	
	

}
