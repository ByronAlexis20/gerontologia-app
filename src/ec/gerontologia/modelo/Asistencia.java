package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the asistencia database table.
 * 
 */
@Entity
@Table(name="asistencia")
@NamedQueries({
	@NamedQuery(name="Asistencia.buscarEncargadoCentro", query="SELECT a FROM Asistencia a where a.estado = 'A' and a.clase.idClase = :idClase and a.matricula.idMatricula = :idMatricula")
})
public class Asistencia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_asistencia")
	private Integer idAsistencia;

	private String estado;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	@Lob
	private String justificado;

	@Lob
	private String observacion;

	//bi-directional many-to-one association to Clase
	@ManyToOne
	@JoinColumn(name="id_clase")
	private Clase clase;

	//bi-directional many-to-one association to Matricula
	@ManyToOne
	@JoinColumn(name="id_matricula")
	private Matricula matricula;

	public Asistencia() {
	}

	public Integer getIdAsistencia() {
		return this.idAsistencia;
	}

	public void setIdAsistencia(Integer idAsistencia) {
		this.idAsistencia = idAsistencia;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getJustificado() {
		return this.justificado;
	}

	public void setJustificado(String justificado) {
		this.justificado = justificado;
	}

	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Clase getClase() {
		return this.clase;
	}

	public void setClase(Clase clase) {
		this.clase = clase;
	}

	public Matricula getMatricula() {
		return this.matricula;
	}

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}

}