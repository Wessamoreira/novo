package negocio.interfaces.academico;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.MapaRegistroEvasaoCursoVO;

public interface MapaRegistroEvasaoCursoInterfaceFacade {

	void persistir(MapaRegistroEvasaoCursoVO obj, boolean verificarAcesso, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO);

	void consultar(DataModelo dataModelo, MapaRegistroEvasaoCursoVO obj) throws Exception;
	
	void valiarDados(MapaRegistroEvasaoCursoVO obj);

	MapaRegistroEvasaoCursoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);
	
	/** 
	 * @author Wellington - 13 de jan de 2016 
	 * @param unidadeEnsinoVOs
	 * @param cursoVOs
	 * @param turnoVOs
	 * @param ano
	 * @param semestre
	 * @param trazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte
	 * @param trazerAlunoTrancadoAbandonadoAnoSemestreBase
	 * @param controlarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	void consultarPorUnidadeEnsinoCursoTurnoMapaRegistroAbandonoCursoTrancamento(MapaRegistroEvasaoCursoVO mapaRegistroAbandonoCursoTrancamentoVO, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

}
