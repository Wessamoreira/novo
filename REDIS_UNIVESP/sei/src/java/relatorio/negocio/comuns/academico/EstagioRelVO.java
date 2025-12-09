package relatorio.negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.EstagioVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoEstagioEnum;
import negocio.comuns.bancocurriculum.AreaProfissionalVO;
import negocio.comuns.financeiro.ParceiroVO;

public class EstagioRelVO {

	private MatriculaVO matriculaVO;
	private String ano;
	private String semestre;
	private ParceiroVO empresaVO;
	private TurmaVO turmaVO;
	private DisciplinaVO disciplinaVO;
	private String nota;
	private TipoEstagioEnum tipoEstagio;
	private Integer cargaHoraria;
	private Integer horasObrigatorias;
	private AreaProfissionalVO areaProfissionalVO;
	private Date dataInicioVigencia;
	private Date dataFimVigencia;	
	private EstagioVO estagioVO;
	private String responsavel;

	public MatriculaVO getMatriculaVO() {
		if(matriculaVO == null){
			matriculaVO =  new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public String getAno() {
		if(ano == null){
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if(semestre == null){
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public ParceiroVO getEmpresaVO() {
		if(empresaVO == null){
			empresaVO = new ParceiroVO();
		}
		return empresaVO;
	}

	public void setEmpresaVO(ParceiroVO empresaVO) {
		this.empresaVO = empresaVO;
	}

	public TurmaVO getTurmaVO() {
		if(turmaVO == null){
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public DisciplinaVO getDisciplinaVO() {
		if(disciplinaVO == null){
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public String getNota() {
		if(nota == null){
			nota = "";
		}
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public TipoEstagioEnum getTipoEstagio() {
		return tipoEstagio;
	}

	public void setTipoEstagio(TipoEstagioEnum tipoEstagio) {
		this.tipoEstagio = tipoEstagio;
	}

	public Integer getCargaHoraria() {
		if(cargaHoraria == null){
			cargaHoraria = 0;
		}
		return cargaHoraria;
	}

	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public Integer getHorasObrigatorias() {
		if(horasObrigatorias== null){
			horasObrigatorias = 0;
		}
		return horasObrigatorias;
	}

	public void setHorasObrigatorias(Integer horasObrigatorias) {
		this.horasObrigatorias = horasObrigatorias;
	}

	public AreaProfissionalVO getAreaProfissionalVO() {
		if(areaProfissionalVO==null){
			areaProfissionalVO = new AreaProfissionalVO();
		}
		return areaProfissionalVO;
	}

	public void setAreaProfissionalVO(AreaProfissionalVO areaProfissionalVO) {
		this.areaProfissionalVO = areaProfissionalVO;
	}

	public Date getDataInicioVigencia() {
		return dataInicioVigencia;
	}

	public void setDataInicioVigencia(Date dataInicioVigencia) {
		this.dataInicioVigencia = dataInicioVigencia;
	}

	public Date getDataFimVigencia() {
		return dataFimVigencia;
	}

	public void setDataFimVigencia(Date dataFimVigencia) {
		this.dataFimVigencia = dataFimVigencia;
	}

	public String getTipoEstagioApresentar() {
		return getTipoEstagio() == null ? "" : getTipoEstagio().getValorApresentar();
	}

	public Integer getCodigoEstagio() {		
		return getEstagioVO().getCodigo();
	}


	public String getResponsavel() {
		if(responsavel == null){
			responsavel = "";
		}
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}
	
	public Integer getHorasRestante(){
		return getHorasObrigatorias() - getCargaHoraria() < 0 ? 0 :  getHorasObrigatorias() - getCargaHoraria();
	}

	public EstagioVO getEstagioVO() {
		if(estagioVO == null){
			estagioVO = new EstagioVO();
		}
		return estagioVO;
	}

	public void setEstagioVO(EstagioVO estagioVO) {
		this.estagioVO = estagioVO;
	}
	

}