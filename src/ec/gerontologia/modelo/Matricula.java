package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the matricula database table.
 * 
 */
@Entity
@Table(name="matricula")
@NamedQueries({
	@NamedQuery(name="Matricula.buscarPersonaMatricula", query="SELECT m FROM Matricula m where m.persona.cedula = :cedula and m.estado = 'A' and m.periodoLectivo.estadoPeriodo = 'EN PROCESO' "),
	@NamedQuery(name="Matricula.buscarPersonaPorPeriodo", query="SELECT m FROM Matricula m where m.estado = 'A' and m.periodoLectivo.idPeriodo = :idPeriodo"),
	@NamedQuery(name="Matricula.buscarPersonaPorCentro", query="SELECT m FROM Matricula m where m.estado = 'A' and m.periodoLectivo.idcentro = :idCentro")
})
public class Matricula implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_matricula")
	private Integer idMatricula;

	private String estado;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	@Lob
	private String observacion;

	//bi-directional many-to-one association to Asistencia
	@OneToMany(mappedBy="matricula")
	private List<Asistencia> asistencias;

	//bi-directional many-to-one association to Parentesco
	@ManyToOne
	@JoinColumn(name="id_parentesco")
	private Parentesco parentesco;

	//bi-directional many-to-one association to PeriodoLectivo
	@ManyToOne
	@JoinColumn(name="id_periodo")
	private PeriodoLectivo periodoLectivo;

	//bi-directional many-to-one association to Persona
	@ManyToOne
	@JoinColumn(name="id_persona")
	private Persona persona;

	//bi-directional many-to-one association to Representante
	@ManyToOne
	@JoinColumn(name="id_representante")
	private Representante representante;

	//bi-directional many-to-one association to VisitaMedica
	@OneToMany(mappedBy="matricula")
	private List<VisitaMedica> visitaMedicas;

	public Matricula() {
	}

	public Integer getIdMatricula() {
		return this.idMatricula;
	}

	public void setIdMatricula(Integer idMatricula) {
		this.idMatricula = idMatricula;
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

	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public List<Asistencia> getAsistencias() {
		return this.asistencias;
	}

	public void setAsistencias(List<Asistencia> asistencias) {
		this.asistencias = asistencias;
	}

	public Asistencia addAsistencia(Asistencia asistencia) {
		getAsistencias().add(asistencia);
		asistencia.setMatricula(this);

		return asistencia;
	}

	public Asistencia removeAsistencia(Asistencia asistencia) {
		getAsistencias().remove(asistencia);
		asistencia.setMatricula(null);

		return asistencia;
	}

	public Parentesco getParentesco() {
		return this.parentesco;
	}

	public void setParentesco(Parentesco parentesco) {
		this.parentesco = parentesco;
	}

	public PeriodoLectivo getPeriodoLectivo() {
		return this.periodoLectivo;
	}

	public void setPeriodoLectivo(PeriodoLectivo periodoLectivo) {
		this.periodoLectivo = periodoLectivo;
	}

	public Persona getPersona() {
		return this.persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Representante getRepresentante() {
		return this.representante;
	}

	public void setRepresentante(Representante representante) {
		this.representante = representante;
	}

	public List<VisitaMedica> getVisitaMedicas() {
		return this.visitaMedicas;
	}

	public void setVisitaMedicas(List<VisitaMedica> visitaMedicas) {
		this.visitaMedicas = visitaMedicas;
	}

	public VisitaMedica addVisitaMedica(VisitaMedica visitaMedica) {
		getVisitaMedicas().add(visitaMedica);
		visitaMedica.setMatricula(this);

		return visitaMedica;
	}

	public VisitaMedica removeVisitaMedica(VisitaMedica visitaMedica) {
		getVisitaMedicas().remove(visitaMedica);
		visitaMedica.setMatricula(null);

		return visitaMedica;
	}

}