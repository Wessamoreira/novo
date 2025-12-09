package relatorio.negocio.interfaces.administrativo;

import java.util.Date;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.TipoMidiaCaptacaoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface CampanhaMarketingRelInterfaceFacade {

	public String emitirRelatorio(UsuarioVO usuarioVO) throws Exception;

	public Date getDataAutorizacao();

	public void setDataAutorizacao(Date dataAutorizacao);

	public Date getDataFimVinculacao();

	public void setDataFimVinculacao(Date dataFimVinculacao);

	public Date getDataInicioVinculacao();

	public void setDataInicioVinculacao(Date dataInicioVinculacao);

	public TipoMidiaCaptacaoVO getMidia();

	public void setMidia(TipoMidiaCaptacaoVO midia);

	public FuncionarioVO getRequisitante();

	public void setRequisitante(FuncionarioVO requisitante);

	public String getSituacao();

	public void setSituacao(String situacao);

	public Date getDataFimAutorizacao();

	public void setDataFimAutorizacao(Date dataFimAutorizacao);

}