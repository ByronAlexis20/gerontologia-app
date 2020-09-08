package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the instruccion database table.
 * 
 */
@Entity
@Table(name="instruccion")
@NamedQueries({
	@NamedQuery(name="Instruccion.buscarPorPatron", query="SELECT i FROM Instruccion i WHERE LOWER(i.descripcion) LIKE LOWER(:patron) and i.estado = 'A'")
})
public class Instruccion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_instruccion")
	private Integer idInstruccion;

	@Lob
	private String descripcion;

	private String estado;

	//bi-directional many-to-one association to Persona
	@OneToMany(mappedBy="instruccion")
	private List<Persona> personas;

	//bi-directional many-to-one association to Representante
	@OneToMany(mappedBy="instruccion")
	private List<Representante> representantes;

	public Instruccion() {
	}

	public Integer getIdInstruccion() {
		return this.idInstruccion;
	}

	public void setIdInstruccion(Integer idInstruccion) {
		this.idInstruccion = idInstruccion;
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
		persona.setInstruccion(this);

		return persona;
	}

	public Persona removePersona(Persona persona) {
		getPersonas().remove(persona);
		persona.setInstruccion(null);

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
		representante.setInstruccion(this);

		return representante;
	}

	public Representante removeRepresentante(Representante representante) {
		getRepresentantes().remove(representante);
		representante.setInstruccion(null);

		return representante;
	}

}