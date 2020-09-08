package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the periodo_lectivo database table.
 * 
 */
@Entity
@Table(name="periodo_lectivo")
@NamedQueries({
	@NamedQuery(name="PeriodoLectivo.buscarPorCentro", query="SELECT p FROM PeriodoLectivo p where p.idcentro = :idCentro and p.estado = 'A'"),
	@NamedQuery(name="PeriodoLectivo.buscarPorCentroDesc", query="SELECT p FROM PeriodoLectivo p where p.idcentro = :idCentro and p.estado = 'A' order by p.idPeriodo desc"),
	@NamedQuery(name="PeriodoLectivo.enProceso", query="SELECT p FROM PeriodoLectivo p where p.idcentro = :idCentro and p.estado = 'A' and p.estadoPeriodo = 'EN PROCESO'")
})
public class PeriodoLectivo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_periodo")
	private Integer idPeriodo;

	@Lob
	private String descripcion;

	private String estado;

	@Lob
	@Column(name="estado_periodo")
	private String estadoPeriodo;

	private Integer idcentro;
	
	
	@Temporal(TemporalType.DATE)
	@Column(name="fecha_fin")
	private Date fechaFin;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_inicio")
	private Date fechaInicio;

	//bi-directional many-to-one association to Matricula
	@OneToMany(mappedBy="periodoLectivo")
	private List<Matricula> matriculas;

	public PeriodoLectivo() {
	}

	public Integer getIdPeriodo() {
		return this.idPeriodo;
	}

	public void setIdPeriodo(Integer idPeriodo) {
		this.idPeriodo = idPeriodo;
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

	public String getEstadoPeriodo() {
		return this.estadoPeriodo;
	}

	public void setEstadoPeriodo(String estadoPeriodo) {
		this.estadoPeriodo = estadoPeriodo;
	}

	public Date getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Date getFechaInicio() {
		return this.fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public List<Matricula> getMatriculas() {
		return this.matriculas;
	}

	public void setMatriculas(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}

	public Matricula addMatricula(Matricula matricula) {
		getMatriculas().add(matricula);
		matricula.setPeriodoLectivo(this);

		return matricula;
	}

	public Matricula removeMatricula(Matricula matricula) {
		getMatriculas().remove(matricula);
		matricula.setPeriodoLectivo(null);

		return matricula;
	}

	public Integer getIdcentro() {
		return idcentro;
	}

	public void setIdcentro(Integer idcentro) {
		this.idcentro = idcentro;
	}

	

}