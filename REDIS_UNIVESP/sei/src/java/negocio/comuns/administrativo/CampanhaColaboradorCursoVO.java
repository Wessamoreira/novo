/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.administrativo;

import java.util.Date;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.arquitetura.SuperVO;

/**
 *
 * @author PEDRO
 */
public class CampanhaColaboradorCursoVO  extends SuperVO{

    private Integer codigo;
    private CursoVO cursoVO;
    private CampanhaColaboradorVO campanhaColaboradorVO;
    private Date dataUltimoCompromissoGerado;

    public CampanhaColaboradorVO getCampanhaColaboradorVO() {
        if (campanhaColaboradorVO == null) {
            campanhaColaboradorVO = new CampanhaColaboradorVO();
        }
        return campanhaColaboradorVO;
    }

    public void setCampanhaColaboradorVO(CampanhaColaboradorVO campanhaColaboradorVO) {
        this.campanhaColaboradorVO = campanhaColaboradorVO;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public CursoVO getCursoVO() {
        if (cursoVO == null) {
            cursoVO = new CursoVO();
        }
        return cursoVO;
    }

    public void setCursoVO(CursoVO cursoVO) {
        this.cursoVO = cursoVO;
    }
    
    public Date getDataUltimoCompromissoGerado() {
		return dataUltimoCompromissoGerado;
	}

	public void setDataUltimoCompromissoGerado(Date dataUltimoCompromissoGerado) {
		this.dataUltimoCompromissoGerado = dataUltimoCompromissoGerado;
	}
}
