package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the etnia database table.
 * 
 */
@Entity
@Table(name="etnia")
@NamedQueries({
	@NamedQuery(name="Etnia.buscarPorPatron", query="SELECT e FROM Etnia e WHERE LOWER(e.descripcion) LIKE LOWER(:patron) and e.estado = 'A'")
})
public class Etnia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_etnia")
	private Integer idEtnia;

	@Lob
	private String descripcion;

	private String estado;

	//bi-directional many-to-one association to Persona
	@OneToMany(mappedBy="etnia")
	private List<Persona> personas;

	public Etnia() {
	}

	public Integer getIdEtnia() {
		return this.idEtnia;
	}

	public void setIdEtnia(Integer idEtnia) {
		this.idEtnia = idEtnia;
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
		persona.setEtnia(this);

		return persona;
	}

	public Persona removePersona(Persona persona) {
		getPersonas().remove(persona);
		persona.setEtnia(null);

		return persona;
	}

}