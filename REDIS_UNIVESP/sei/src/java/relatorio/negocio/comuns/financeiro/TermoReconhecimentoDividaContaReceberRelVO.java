package relatorio.negocio.comuns.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.ContaReceberVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class TermoReconhecimentoDividaContaReceberRelVO extends SuperVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String nomeResponsavel;
    private String telResResponsavel;
    private String celularResponsavel;
    private String cpfResponsavel;
    private String rgResponsavel;
    private String orgaoExpedidorResponsavel;
    private String enderecoResponsavel;
    private String bairroResponsavel;
    private String cidadeResponsavel;
    private String ufResponsavel;
    private String cepResponsavel;
    private String nomeAluno;
    private String matriculaAluno;
    private String cursoAluno;
    private String nomeEmpresa;
    private String cnpjEmpresa;
    private String enderecoEmpresa;
    private String bairroEmpresa;
    private String telEmpresa;
    private String cidadeEmpresa;
    private String ufEmpresa;
    private String cepEmpresa;
    private String mantenedoraEmpresa;
    private Date dataAcordoExtenso;
    private List<ContaReceberVO> listaContaReceberVOs;
    private List<TermoReconhecimentoDividaRelVOSubReport> subReport;
    private String texto1;
    private String textoTitulo1;
    private String texto2;
    private String textoTitulo2;
    private String texto3;
    private String textoTitulo3;
    private String texto4;
    private String textoTitulo4;
    private String texto5;
    private String textoTitulo5;
    private String texto6;
    private String textoTitulo6;


	public TermoReconhecimentoDividaContaReceberRelVO() {
		
	}
	
	public JRDataSource getListaContaReceberJR() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaContaReceberVOs().toArray());
        return jr;
    }
	
    public JRDataSource getSubReportJR() {
        JRDataSource jr = new JRBeanArrayDataSource(getSubReport().toArray());
        return jr;
    }


    public String getNomeResponsavel() {
        if (nomeResponsavel == null) {
            nomeResponsavel = "";
        }
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public String getTelResResponsavel() {
        if (telResResponsavel == null) {
            telResResponsavel = "";
        }
        return telResResponsavel;
    }

    public void setTelResResponsavel(String telResResponsavel) {
        this.telResResponsavel = telResResponsavel;
    }

    public String getCelularResponsavel() {
        if (celularResponsavel == null) {
            celularResponsavel = "";
        }
        return celularResponsavel;
    }

    public void setCelularResponsavel(String celularResponsavel) {
        this.celularResponsavel = celularResponsavel;
    }

    public String getOrgaoExpedidorResponsavel() {
        if (orgaoExpedidorResponsavel == null) {
            orgaoExpedidorResponsavel = "";
        }
        return orgaoExpedidorResponsavel;
    }

    public void setOrgaoExpedidorResponsavel(String orgaoExpedidorResponsavel) {
        this.orgaoExpedidorResponsavel = orgaoExpedidorResponsavel;
    }

    public String getEnderecoResponsavel() {
        if (enderecoResponsavel == null) {
            enderecoResponsavel = "";
        }
        return enderecoResponsavel;
    }

    public void setEnderecoResponsavel(String enderecoResponsavel) {
        this.enderecoResponsavel = enderecoResponsavel;
    }

    public String getBairroResponsavel() {
        if (bairroResponsavel == null) {
            bairroResponsavel = "";
        }
        return bairroResponsavel;
    }

    public void setBairroResponsavel(String bairroResponsavel) {
        this.bairroResponsavel = bairroResponsavel;
    }

    public String getCidadeResponsavel() {
        if (cidadeResponsavel == null) {
            cidadeResponsavel = "";
        }
        return cidadeResponsavel;
    }

    public void setCidadeResponsavel(String cidadeResponsavel) {
        this.cidadeResponsavel = cidadeResponsavel;
    }

    public String getUfResponsavel() {
        if (ufResponsavel == null) {
            ufResponsavel = "";
        }
        return ufResponsavel;
    }

    public void setUfResponsavel(String ufResponsavel) {
        this.ufResponsavel = ufResponsavel;
    }

    public String getCepResponsavel() {
        if (cepResponsavel == null) {
            cepResponsavel = "";
        }
        return cepResponsavel;
    }

    public void setCepResponsavel(String cepResponsavel) {
        this.cepResponsavel = cepResponsavel;
    }

    public String getNomeAluno() {
        if (nomeAluno == null) {
            nomeAluno = "";
        }
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public String getMatriculaAluno() {
        if (matriculaAluno == null) {
            matriculaAluno = "000000";
        }
        return matriculaAluno;
    }

    public void setMatriculaAluno(String matriculaAluno) {
        this.matriculaAluno = matriculaAluno;
    }

    public String getCursoAluno() {
        if (cursoAluno == null) {
            cursoAluno = "";
        }
        return cursoAluno;
    }

    public void setCursoAluno(String cursoAluno) {
        this.cursoAluno = cursoAluno;
    }

    public String getNomeEmpresa() {
        if (nomeEmpresa == null) {
            nomeEmpresa = "";
        }
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getCnpjEmpresa() {
        if (cnpjEmpresa == null) {
            cnpjEmpresa = "";
        }
        return cnpjEmpresa;
    }

    public void setCnpjEmpresa(String cnpjEmpresa) {
        this.cnpjEmpresa = cnpjEmpresa;
    }

    public String getEnderecoEmpresa() {
        if (enderecoEmpresa == null) {
            enderecoEmpresa = "";
        }
        return enderecoEmpresa;
    }

    public void setEnderecoEmpresa(String enderecoEmpresa) {
        this.enderecoEmpresa = enderecoEmpresa;
    }

    public String getBairroEmpresa() {
        if (bairroEmpresa == null) {
            bairroEmpresa = "";
        }
        return bairroEmpresa;
    }

    public void setBairroEmpresa(String bairroEmpresa) {
        this.bairroEmpresa = bairroEmpresa;
    }

    public String getTelEmpresa() {
        if (telEmpresa == null) {
            telEmpresa = "";
        }
        return telEmpresa;
    }

    public void setTelEmpresa(String telEmpresa) {
        this.telEmpresa = telEmpresa;
    }

    public String getCidadeEmpresa() {
        if (cidadeEmpresa == null) {
            cidadeEmpresa = "";
        }
        return cidadeEmpresa;
    }

    public void setCidadeEmpresa(String cidadeEmpresa) {
        this.cidadeEmpresa = cidadeEmpresa;
    }

    public String getUfEmpresa() {
        if (ufEmpresa == null) {
            ufEmpresa = "";
        }
        return ufEmpresa;
    }

    public void setUfEmpresa(String ufEmpresa) {
        this.ufEmpresa = ufEmpresa;
    }

    public String getCepEmpresa() {
        if (cepEmpresa == null) {
            cepEmpresa = "";
        }
        return cepEmpresa;
    }

    public void setCepEmpresa(String cepEmpresa) {
        this.cepEmpresa = cepEmpresa;
    }

    public String getCpfResponsavel() {
        if (cpfResponsavel == null) {
            cpfResponsavel = "";
        }
        return cpfResponsavel;
    }

    public void setCpfResponsavel(String cpfResponsavel) {
        this.cpfResponsavel = cpfResponsavel;
    }

    public String getRgResponsavel() {
        if (rgResponsavel == null) {
            rgResponsavel = "";
        }
        return rgResponsavel;
    }

    public void setRgResponsavel(String rgResponsavel) {
        this.rgResponsavel = rgResponsavel;
    }
    
	public List<ContaReceberVO> getListaContaReceberVOs() {
		if (listaContaReceberVOs == null) {
			listaContaReceberVOs = new ArrayList<ContaReceberVO>(0);
		}
		return listaContaReceberVOs;
	}

	public void setListaContaReceberVOs(List<ContaReceberVO> listaContaReceberVOs) {
		this.listaContaReceberVOs = listaContaReceberVOs;
	}
	
    public List<TermoReconhecimentoDividaRelVOSubReport> getSubReport() {
        if (subReport == null) {
            subReport = new ArrayList<TermoReconhecimentoDividaRelVOSubReport>(0);
        }
        return subReport;
    }

    public void setSubReport(List<TermoReconhecimentoDividaRelVOSubReport> subReport) {
        this.subReport = subReport;
    }

	public String getTexto1() {
		if (texto1 == null) {
			texto1 = "";
		}
		return texto1;
	}

	public void setTexto1(String texto1) {
		this.texto1 = texto1;
	}

	public String getTextoTitulo1() {
		if (textoTitulo1 == null) {
			textoTitulo1 = "";
		}
		return textoTitulo1;
	}

	public void setTextoTitulo1(String textoTitulo1) {
		this.textoTitulo1 = textoTitulo1;
	}

	public String getTexto2() {
		if (texto2 == null) {
			texto2 = "";
		}
		return texto2;
	}

	public void setTexto2(String texto2) {
		this.texto2 = texto2;
	}

	public String getTextoTitulo2() {
		if (textoTitulo2 == null) {
			textoTitulo2 = "";
		}
		return textoTitulo2;
	}

	public void setTextoTitulo2(String textoTitulo2) {
		this.textoTitulo2 = textoTitulo2;
	}

	public String getTexto3() {
		if (texto3 == null) {
			texto3 = "";
		}
		return texto3;
	}

	public void setTexto3(String texto3) {
		this.texto3 = texto3;
	}

	public String getTextoTitulo3() {
		if (textoTitulo3 == null) {
			textoTitulo3 = "";
		}
		return textoTitulo3;
	}

	public void setTextoTitulo3(String textoTitulo3) {
		this.textoTitulo3 = textoTitulo3;
	}

	public String getTexto4() {
		if (texto4 == null) {
			texto4 = "";
		}
		return texto4;
	}

	public void setTexto4(String texto4) {
		this.texto4 = texto4;
	}

	public String getTextoTitulo4() {
		if (textoTitulo4 == null) {
			textoTitulo4 = "";
		}
		return textoTitulo4;
	}

	public void setTextoTitulo4(String textoTitulo4) {
		this.textoTitulo4 = textoTitulo4;
	}

	public String getTexto5() {
		if (texto5 == null) {
			texto5 = "";
		}
		return texto5;
	}

	public void setTexto5(String texto5) {
		this.texto5 = texto5;
	}

	public String getTextoTitulo5() {
		if (textoTitulo5 == null) {
			textoTitulo5 = "";
		}
		return textoTitulo5;
	}

	public void setTextoTitulo5(String textoTitulo5) {
		this.textoTitulo5 = textoTitulo5;
	}

	public String getTexto6() {
		if (texto6 == null) {
			texto6 = "";
		}
		return texto6;
	}

	public void setTexto6(String texto6) {
		this.texto6 = texto6;
	}

	public String getTextoTitulo6() {
		if (textoTitulo6 == null) {
			textoTitulo6 = "";
		}
		return textoTitulo6;
	}

	public void setTextoTitulo6(String textoTitulo6) {
		this.textoTitulo6 = textoTitulo6;
	}

	public String getMantenedoraEmpresa() {
		if (mantenedoraEmpresa == null) {
			mantenedoraEmpresa = "";
		}
		return mantenedoraEmpresa;
	}

	public void setMantenedoraEmpresa(String mantenedoraEmpresa) {
		this.mantenedoraEmpresa = mantenedoraEmpresa;
	}

	public Date getDataAcordoExtenso() {
		if (dataAcordoExtenso == null) {
			dataAcordoExtenso = new Date();
		}
		return dataAcordoExtenso;
	}

	public void setDataAcordoExtenso(Date dataAcordoExtenso) {
		this.dataAcordoExtenso = dataAcordoExtenso;
	}


}
