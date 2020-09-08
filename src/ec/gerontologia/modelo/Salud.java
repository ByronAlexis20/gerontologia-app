package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the salud database table.
 * 
 */
@Entity
@Table(name="salud")
@NamedQueries({
	@NamedQuery(name="Salud.buscarSalud", query="SELECT s FROM Salud s where s.estado = 'A'")
})
public class Salud implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_salud")
	private int idSalud;

	@Lob
	private String descripcion;

	private String estado;

	//bi-directional many-to-one association to Persona
	@OneToMany(mappedBy="salud")
	private List<Persona> personas;

	public Salud() {
	}

	public int getIdSalud() {
		return this.idSalud;
	}

	public void setIdSalud(int idSalud) {
		this.idSalud = idSalud;
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
		persona.setSalud(this);

		return persona;
	}

	public Persona removePersona(Persona persona) {
		getPersonas().remove(persona);
		persona.setSalud(null);

		return persona;
	}

}