package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the estado_vivienda database table.
 * 
 */
@Entity
@Table(name="estado_vivienda")
@NamedQueries({
	@NamedQuery(name="EstadoVivienda.buscarEstadosVivienda", query="SELECT e FROM EstadoVivienda e where e.estado = 'A'")
})
public class EstadoVivienda implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_estado_vivienda")
	private int idEstadoVivienda;

	@Lob
	private String descripcion;

	private String estado;

	//bi-directional many-to-one association to Persona
	@OneToMany(mappedBy="estadoVivienda")
	private List<Persona> personas;

	public EstadoVivienda() {
	}

	public int getIdEstadoVivienda() {
		return this.idEstadoVivienda;
	}

	public void setIdEstadoVivienda(int idEstadoVivienda) {
		this.idEstadoVivienda = idEstadoVivienda;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<Persona> getPersonas() {
		return this.personas;
	}

	public void setPersonas(List<Persona> personas) {
		this.personas = personas;
	}

	public Persona addPersona(Persona persona) {
		getPersonas().add(persona);
		persona.setEstadoVivienda(this);

		return persona;
	}

	public Persona removePersona(Persona persona) {
		getPersonas().remove(persona);
		persona.setEstadoVivienda(null);

		return persona;
	}

}