package negocio.interfaces.ead;

import java.util.List;

import negocio.comuns.ead.GraficoAproveitamentoAvaliacaoVO;

public interface GraficoAproveitamentoAvaliacaoInterfaceFacade {

	List<GraficoAproveitamentoAvaliacaoVO> consultarAproveitamentoAvaliacaoOnlineAluno(Integer codigoAvaliacaoOnlineMatricula) throws Exception;

	List<GraficoAproveitamentoAvaliacaoVO> realizarParametrosGraficoAvaliacaoOnlineMatricula(List<GraficoAproveitamentoAvaliacaoVO> graficoAproveitamentoAvaliacaoVOs, String nomeDisciplina, Integer parametroWidthGrafico) throws Exception;

	GraficoAproveitamentoAvaliacaoVO consultarAproveitamentoAvaliacaoOnlineAlunoPorDisciplina(Integer codigoAvaliacaoOnlineMatricula) throws Exception;

	GraficoAproveitamentoAvaliacaoVO consultarAproveitamentoAvaliacaoOnlineAlunoPorAssunto(Integer codigoAvaliacaoOnlineMatricula, Integer codigoTemaAssunto) throws Exception;

}
