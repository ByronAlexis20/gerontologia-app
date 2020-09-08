package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the unidad_medica database table.
 * 
 */
@Entity
@Table(name="unidad_medica")
@NamedQueries({
	@NamedQuery(name="UnidadMedica.findAll", query="SELECT u FROM UnidadMedica u"),
	@NamedQuery(name="UnidadMedica.buscarPorPatron", query="SELECT u FROM UnidadMedica u WHERE LOWER(u.unidad) LIKE LOWER(:patron) and u.estado = 'A'")
	
})
public class UnidadMedica implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_unidad")
	private Integer idUnidad;

	private String estado;

	@Lob
	private String representante;

	@Lob
	private String unidad;

	//bi-directional many-to-one association to VisitaMedica
	@OneToMany(mappedBy="unidadMedica")
	private List<VisitaMedica> visitaMedicas;

	public UnidadMedica() {
	}

	public Integer getIdUnidad() {
		return this.idUnidad;
	}

	public void setIdUnidad(Integer idUnidad) {
		this.idUnidad = idUnidad;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getRepresentante() {
		return this.representante;
	}

	public void setRepresentante(String representante) {
		this.representante = representante;
	}

	public String getUnidad() {
		return this.unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public List<VisitaMedica> getVisitaMedicas() {
		return this.visitaMedicas;
	}

	public void setVisitaMedicas(List<VisitaMedica> visitaMedicas) {
		this.visitaMedicas = visitaMedicas;
	}

	public VisitaMedica addVisitaMedica(VisitaMedica visitaMedica) {
		getVisitaMedicas().add(visitaMedica);
		visitaMedica.setUnidadMedica(this);

		return visitaMedica;
	}

	public VisitaMedica removeVisitaMedica(VisitaMedica visitaMedica) {
		getVisitaMedicas().remove(visitaMedica);
		visitaMedica.setUnidadMedica(null);

		return visitaMedica;
	}

}