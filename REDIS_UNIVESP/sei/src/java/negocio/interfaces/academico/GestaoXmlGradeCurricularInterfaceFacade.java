package negocio.interfaces.academico;

import negocio.comuns.academico.GestaoXmlGradeCurricularVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;

public interface GestaoXmlGradeCurricularInterfaceFacade {

	public void consultar(GestaoXmlGradeCurricularVO gestaoXmlGradeCurricular) throws Exception;

	public void realizarMontagemCurriculoEscolarDigital(GestaoXmlGradeCurricularVO gestaoXmlGradeCurricular, SuperParametroRelVO superParametroRelVO, SuperControleRelatorio superControleRelatorio, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public void realizarMontagemCurriculoEscolarDigitalLote(GestaoXmlGradeCurricularVO gestaoXmlGradeCurricular, ProgressBarVO progressBarVO, SuperParametroRelVO superParametroRelVO, SuperControleRelatorio superControleRelatorio) throws Exception;

}
