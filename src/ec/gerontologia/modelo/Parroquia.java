package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the parroquia database table.
 * 
 */
@Entity
@Table(name="parroquia")
@NamedQueries({
	@NamedQuery(name="Parroquia.buscarPorCanton", query="SELECT p FROM Parroquia p where p.estado = 'A' and p.canton.idCanton = :idCanton")
})
public class Parroquia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_parroquia")
	private int idParroquia;

	@Lob
	private String descripcion;

	private String estado;

	//bi-directional many-to-one association to Canton
	@ManyToOne
	@JoinColumn(name="id_canton")
	private Canton canton;

	//bi-directional many-to-one association to Persona
	@OneToMany(mappedBy="parroquia")
	private List<Persona> personas;

	//bi-directional many-to-one association to Representante
	@OneToMany(mappedBy="parroquia")
	private List<Representante> representantes;

	public Parroquia() {
	}

	public int getIdParroquia() {
		return this.idParroquia;
	}

	public void setIdParroquia(int idParroquia) {
		this.idParroquia = idParroquia;
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

	public Canton getCanton() {
		return this.canton;
	}

	public void setCanton(Canton canton) {
		this.canton = canton;
	}

	public List<Persona> getPersonas() {
		return this.personas;
	}

	public void setPersonas(List<Persona> personas) {
		this.personas = personas;
	}

	public Persona addPersona(Persona persona) {
		getPersonas().add(persona);
		persona.setParroquia(this);

		return persona;
	}

	public Persona removePersona(Persona persona) {
		getPersonas().remove(persona);
		persona.setParroquia(null);

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
		representante.setParroquia(this);

		return representante;
	}

	public Representante removeRepresentante(Representante representante) {
		getRepresentantes().remove(representante);
		representante.setParroquia(null);

		return representante;
	}

}