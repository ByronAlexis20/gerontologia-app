package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the empresa database table.
 * 
 */
@Entity
@Table(name="empresa")
@NamedQueries({
	@NamedQuery(name="Empresa.buscarActivo", query="SELECT e FROM Empresa e where e.estado = 'A'")
})
public class Empresa implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_empresa")
	private Integer idEmpresa;

	@Lob
	private String direccion;

	@Lob
	private String email;

	private String estado;

	@Lob
	@Column(name="logo_institucion")
	private byte[] logoInstitucion;

	@Lob
	@Column(name="razon_social")
	private String razonSocial;

	@Lob
	private String representante;

	@Lob
	private String ruc;

	//bi-directional many-to-one association to Centro
	@OneToMany(mappedBy="empresa")
	private List<Centro> centros;

	public Empresa() {
	}

	public Integer getIdEmpresa() {
		return this.idEmpresa;
	}

	public void setIdEmpresa(Integer idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public byte[] getLogoInstitucion() {
		return this.logoInstitucion;
	}

	public void setLogoInstitucion(byte[] logoInstitucion) {
		this.logoInstitucion = logoInstitucion;
	}

	public String getRazonSocial() {
		return this.razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getRepresentante() {
		return this.representante;
	}

	public void setRepresentante(String representante) {
		this.representante = representante;
	}

	public String getRuc() {
		return this.ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public List<Centro> getCentros() {
		return this.centros;
	}

	public void setCentros(List<Centro> centros) {
		this.centros = centros;
	}

	public Centro addCentro(Centro centro) {
		getCentros().add(centro);
		centro.setEmpresa(this);

		return centro;
	}

	public Centro removeCentro(Centro centro) {
		getCentros().remove(centro);
		centro.setEmpresa(null);

		return centro;
	}

}