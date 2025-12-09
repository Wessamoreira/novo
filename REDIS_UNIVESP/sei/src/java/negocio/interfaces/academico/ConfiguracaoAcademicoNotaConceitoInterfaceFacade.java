package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.comuns.utilitarias.ConsistirException;


public interface ConfiguracaoAcademicoNotaConceitoInterfaceFacade {
    
    void incluirConfiguracaoAcademicoNotaConceito(ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception;
    
    void alterarConfiguracaoAcademicoNotaConceito(ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception;
    
    void consultarPorConfiguracaoAcademico(ConfiguracaoAcademicoVO configuracaoAcademicoVO);
    
    void validarDados(ConfiguracaoAcademicoNotaConceitoVO obj, boolean mediaFinal) throws ConsistirException;

    ConfiguracaoAcademicoNotaConceitoVO consultarPorChavePrimaria(Integer codigo);

	/**
	 * @author Rodrigo Wind - 19/10/2015
	 * @param conf
	 * @param tipoNota
	 * @return
	 */
	List<ConfiguracaoAcademicoNotaConceitoVO> consultarPorConfiguracaoAcademicoTipoNota(Integer conf, TipoNotaConceitoEnum tipoNota);

}
