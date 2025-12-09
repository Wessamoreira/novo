alter table if exists configuracaoatualizacaocadastral
add column if not exists editarCampoRG boolean default false,
add column if not exists editarNaturalidadeNacionalidade boolean default false,
add column if not exists editarCampoDataNascimento boolean default false,
add column if not exists permitirAlterarEndereco boolean default false;

update configuracaoatualizacaocadastral
set enderecoobrigatorio = false , trazerdadoenderecoembranco = false where permitiralterarendereco = false; 

update configuracaoatualizacaocadastral
set enderecoobrigatorio = false ,trazerdadoenderecoembranco = false, permitiralterarendereco = false where apresentarcampoendereco = false;


update configuracaoatualizacaocadastral 
set apresentarcampocontatofiliacao = false,apresentarcampocpffiliacao=false,apresentarcampodatanascfiliacao=false,apresentarcampoenderecofiliacao=false,apresentarcampoestadocivilfiliacao=false,apresentarcamponacionalidadefiliacao=false,
apresentarcamporgfiliacao = false where apresentarcampofiliacao = false;


update configuracaoatualizacaocadastral
set registromilitarobrigatorio = false where apresentarcamporegistromilitar = false;


update configuracaoatualizacaocadastral
set rgobrigatorio = false where apresentarcamporg = false;

update configuracaoatualizacaocadastral
set estadocivilobrigatorio = false where apresentarcampoestadocivil = false;

update configuracaoatualizacaocadastral
set corracaobrigatorio = false where apresentarcampocorraca = false;


update configuracaoatualizacaocadastral
set naturalidadenacionalidadeobrigatorio = false where apresentarcamponaturalidadenacionalidade = false;


update configuracaoatualizacaocadastral
set datanascimentoobrigatorio = false where apresentarcampodatanascimento = false;

update configuracaoatualizacaocadastral
set umtelefonefixoobrigatorio = false,celularobrigatorio=false,trazerdadocontatoembranco=false where apresentarcampocontato = false;

update configuracaoatualizacaocadastral
set dadoeleitoralobrigatorio = false where apresentarcampodadoeleitoral = false;

update configuracaoatualizacaocadastral
set permitiratualizarcpf = false where apresentarcampocpf = false;


update configuracaoatualizacaocadastral
set trazerdadocertidaonascembranco = false,certidaonascobrigatorio=false where apresentarcampocertidaonasc = false;


alter table if exists pessoa
add column if not exists atualizardatacadastral boolean;

drop trigger IF exists tg_pessoa_cadastrar on pessoa;
   
create trigger tg_pessoa_cadastrar after
insert or update on
public.pessoa for each row when (coalesce(new.atualizardatacadastral,false) is false ) execute function inserirdadoscomerciais();
   
