package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirVO;
import negocio.comuns.administrativo.LayoutRelatorioSeiDecidirCampoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface LayoutRelatorioSeiDecidirCampoInterface {

	public void alterar(LayoutRelatorioSeiDecidirCampoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void excluir(LayoutRelatorioSeiDecidirCampoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void excluirLayoutRelatorioSEIDecidir(Integer tipolayoutrelatorioseidecidir, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void persistirLayoutRelatorioSeiDecidirCampoVOs(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public LayoutRelatorioSeiDecidirCampoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	public List<LayoutRelatorioSeiDecidirCampoVO> consultarPorLayoutRelatorio(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

}
