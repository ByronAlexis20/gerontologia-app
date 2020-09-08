package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the vivienda database table.
 * 
 */
@Entity
@Table(name="vivienda")
@NamedQueries({
	@NamedQuery(name="Vivienda.buscarViviendas", query="SELECT v FROM Vivienda v where v.estado = 'A'")
})
public class Vivienda implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_vivienda")
	private int idVivienda;

	@Lob
	private String descripcion;

	private String estado;

	//bi-directional many-to-one association to Persona
	@OneToMany(mappedBy="vivienda")
	private List<Persona> personas;

	public Vivienda() {
	}

	public int getIdVivienda() {
		return this.idVivienda;
	}

	public void setIdVivienda(int idVivienda) {
		this.idVivienda = idVivienda;
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
		persona.setVivienda(this);

		return persona;
	}

	public Persona removePersona(Persona persona) {
		getPersonas().remove(persona);
		persona.setVivienda(null);

		return persona;
	}

}