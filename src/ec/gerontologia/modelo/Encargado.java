package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the encargado database table.
 * 
 */
@Entity
@Table(name="encargado")
@NamedQueries({
	@NamedQuery(name="Encargado.buscarEncargadoCentro", query="SELECT e FROM Encargado e where e.estado = 'A' and e.centro.idCentro = :idCentro"),
	@NamedQuery(name="Encargado.buscarAsignados", query="SELECT e FROM Encargado e where e.estado = 'A'"),
	@NamedQuery(name="Encargado.buscarUsuario", query="SELECT e FROM Encargado e where e.estado = 'A' and e.usuario.idUsuario = :idUsuario"),
})
public class Encargado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_encargado")
	private Integer idEncargado;

	private String estado;

	//bi-directional many-to-one association to Centro
	@ManyToOne
	@JoinColumn(name="id_centro")
	private Centro centro;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="id_usuario")
	private Usuario usuario;

	//bi-directional many-to-one association to VisitaMedica
	@OneToMany(mappedBy="encargado")
	private List<VisitaMedica> visitaMedicas;

	public Encargado() {
	}

	public Integer getIdEncargado() {
		return this.idEncargado;
	}

	public void setIdEncargado(Integer idEncargado) {
		this.idEncargado = idEncargado;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Centro getCentro() {
		return this.centro;
	}

	public void setCentro(Centro centro) {
		this.centro = centro;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<VisitaMedica> getVisitaMedicas() {
		return this.visitaMedicas;
	}

	public void setVisitaMedicas(List<VisitaMedica> visitaMedicas) {
		this.visitaMedicas = visitaMedicas;
	}

	public VisitaMedica addVisitaMedica(VisitaMedica visitaMedica) {
		getVisitaMedicas().add(visitaMedica);
		visitaMedica.setEncargado(this);

		return visitaMedica;
	}

	public VisitaMedica removeVisitaMedica(VisitaMedica visitaMedica) {
		getVisitaMedicas().remove(visitaMedica);
		visitaMedica.setEncargado(null);

		return visitaMedica;
	}

}