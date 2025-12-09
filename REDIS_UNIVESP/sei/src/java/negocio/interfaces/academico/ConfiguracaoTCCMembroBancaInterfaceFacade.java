package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ConfiguracaoTCCMembroBancaVO;
import negocio.comuns.basico.PessoaVO;

public interface ConfiguracaoTCCMembroBancaInterfaceFacade {
	
	void persistir(ConfiguracaoTCCMembroBancaVO configuracaoTCCMembroBancaVO) throws Exception;
	
	List<ConfiguracaoTCCMembroBancaVO> consultarPorTCC(int tcc) throws Exception;
	
	public void alterarMembrosBanca(Integer tcc, List<ConfiguracaoTCCMembroBancaVO> objetos) throws Exception;

	public void incluirMembrosBanca(Integer tcc, List<ConfiguracaoTCCMembroBancaVO> objetos) throws Exception;
	
	public List<ConfiguracaoTCCMembroBancaVO> montarCoordenadorMembroBanca(PessoaVO coordenador, Integer tcc) throws Exception;
	
	public List<ConfiguracaoTCCMembroBancaVO> consultarPorTurmaDisciplina(int turma, int disciplina, int tcc) throws Exception;
	
}
