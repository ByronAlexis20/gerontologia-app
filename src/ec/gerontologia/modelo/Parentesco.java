package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the parentesco database table.
 * 
 */
@Entity
@NamedQuery(name="Parentesco.findAll", query="SELECT p FROM Parentesco p")
public class Parentesco implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_parentesco")
	private int idParentesco;

	@Lob
	private String descripcion;

	private String estado;

	//bi-directional many-to-one association to Matricula
	@OneToMany(mappedBy="parentesco")
	private List<Matricula> matriculas;

	public Parentesco() {
	}

	public int getIdParentesco() {
		return this.idParentesco;
	}

	public void setIdParentesco(int idParentesco) {
		this.idParentesco = idParentesco;
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

	public List<Matricula> getMatriculas() {
		return this.matriculas;
	}

	public void setMatriculas(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}

	public Matricula addMatricula(Matricula matricula) {
		getMatriculas().add(matricula);
		matricula.setParentesco(this);

		return matricula;
	}

	public Matricula removeMatricula(Matricula matricula) {
		getMatriculas().remove(matricula);
		matricula.setParentesco(null);

		return matricula;
	}

}