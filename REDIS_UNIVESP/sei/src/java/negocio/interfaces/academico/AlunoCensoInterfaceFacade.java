package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.AlunoCensoVO;

public interface AlunoCensoInterfaceFacade {

	public List<AlunoCensoVO> consultarPorAnoSemestreUnidadeEnsinoCenso(String ano, String semestre, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados) throws Exception;
      
}