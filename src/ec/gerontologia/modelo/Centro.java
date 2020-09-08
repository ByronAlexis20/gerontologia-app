package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the centro database table.
 * 
 */
@Entity
@Table(name="centro")
@NamedQueries({
	@NamedQuery(name="Centro.buscarPorPatron", query="SELECT c FROM Centro c WHERE LOWER(c.nombreCentro) LIKE LOWER(:patron) and c.estado = 'A'")
})
public class Centro implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_centro")
	private Integer idCentro;

	@Lob
	private String direccion;

	private String estado;

	@Lob
	@Column(name="nombre_centro")
	private String nombreCentro;

	//bi-directional many-to-one association to Empresa
	@ManyToOne
	@JoinColumn(name="id_empresa")
	private Empresa empresa;

	//bi-directional many-to-one association to Encargado
	@OneToMany(mappedBy="centro", cascade = CascadeType.ALL)
	private List<Encargado> encargados;

	public Centro() {
	}

	public Integer getIdCentro() {
		return this.idCentro;
	}

	public void setIdCentro(Integer idCentro) {
		this.idCentro = idCentro;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNombreCentro() {
		return this.nombreCentro;
	}

	public void setNombreCentro(String nombreCentro) {
		this.nombreCentro = nombreCentro;
	}

	public Empresa getEmpresa() {
		return this.empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public List<Encargado> getEncargados() {
		return this.encargados;
	}

	public void setEncargados(List<Encargado> encargados) {
		this.encargados = encargados;
	}

	public Encargado addEncargado(Encargado encargado) {
		getEncargados().add(encargado);
		encargado.setCentro(this);

		return encargado;
	}

	public Encargado removeEncargado(Encargado encargado) {
		getEncargados().remove(encargado);
		encargado.setCentro(null);

		return encargado;
	}

}