package relatorio.negocio.interfaces.administrativo;

import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.RelatorioSEIDecidirVO;
import negocio.comuns.utilitarias.ProgressBarVO;

public interface RelatorioSeiDecidirInterfaceFacade {

	public SqlRowSet realizarConsultarGeracaoRelatorio(RelatorioSEIDecidirVO relatorioSEIDecidirVO, Map<String, Object> mapaParametrosRelatorio, UsuarioVO usuarioVO) throws Exception;

	public String realizarGeracaoRelatorioSeiDecidir(RelatorioSEIDecidirVO relatorioSEIDecidirVO, TipoRelatorioEnum tipoRelatorio, String urlLogoPadraoRelatorio, boolean gerarFormatoExportacaoDados, String campoSeparador, String extensaoArquivo, boolean verificarAcesso, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ProgressBarVO progressBarVO) throws Exception;

	 public SqlRowSet consultaIntegracaoSymplicty();
}
