package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the discapacidad database table.
 * 
 */
@Entity
@Table(name="discapacidad")
@NamedQueries({
	@NamedQuery(name="Discapacidad.buscarPorPatron", query="SELECT d FROM Discapacidad d WHERE LOWER(d.descripcion) LIKE LOWER(:patron) and d.estado = 'A'")
})
public class Discapacidad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_discapacidad")
	private Integer idDiscapacidad;

	@Lob
	private String descripcion;

	private String estado;

	//bi-directional many-to-one association to Persona
	@OneToMany(mappedBy="discapacidad")
	private List<Persona> personas;

	//bi-directional many-to-one association to Representante
	@OneToMany(mappedBy="discapacidad")
	private List<Representante> representantes;

	public Discapacidad() {
	}

	public Integer getIdDiscapacidad() {
		return this.idDiscapacidad;
	}

	public void setIdDiscapacidad(Integer idDiscapacidad) {
		this.idDiscapacidad = idDiscapacidad;
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
		persona.setDiscapacidad(this);

		return persona;
	}

	public Persona removePersona(Persona persona) {
		getPersonas().remove(persona);
		persona.setDiscapacidad(null);

		return persona;
	}

	public List<Representante> getRepresentantes() {
		return this.representantes;
	}

	public void setRepresentantes(List<Representante> representantes) {
		this.representantes = representantes;
	}

	public Representante addRepresentante(Representante representante) {
		getRepresentantes().add(representante);
		representante.setDiscapacidad(this);

		return representante;
	}

	public Representante removeRepresentante(Representante representante) {
		getRepresentantes().remove(representante);
		representante.setDiscapacidad(null);

		return representante;
	}

}