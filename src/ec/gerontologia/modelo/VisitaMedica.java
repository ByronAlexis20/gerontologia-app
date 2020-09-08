package ec.gerontologia.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * The persistent class for the visita_medica database table.
 * 
 */
@Entity
@Table(name="visita_medica")
@NamedQuery(name="VisitaMedica.buscarTodos", query="SELECT v FROM VisitaMedica v where v.estado = 'A' and v.idPeriodo = :idPediodo")
public class VisitaMedica implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_visita")
	private Integer idVisita;

	@Lob
	private String diagnostico;

	private String estado;

	//bi-directional many-to-one association to Encargado
	@ManyToOne
	@JoinColumn(name="id_encargado")
	private Encargado encargado;

	@Column(name="id_periodo")
	private Integer idPeriodo;
	
	//bi-directional many-to-one association to Matricula
	@ManyToOne
	@JoinColumn(name="id_matricula")
	private Matricula matricula;

	@Temporal(TemporalType.DATE)
	private Date fecha;
	
	//bi-directional many-to-one association to UnidadMedica
	@ManyToOne
	@JoinColumn(name="id_unidad")
	private UnidadMedica unidadMedica;

	public VisitaMedica() {
	}

	public Integer getIdVisita() {
		return this.idVisita;
	}

	public void setIdVisita(Integer idVisita) {
		this.idVisita = idVisita;
	}

	public String getDiagnostico() {
		return this.diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Encargado getEncargado() {
		return this.encargado;
	}

	public void setEncargado(Encargado encargado) {
		this.encargado = encargado;
	}

	public Matricula getMatricula() {
		return this.matricula;
	}

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}

	public UnidadMedica getUnidadMedica() {
		return this.unidadMedica;
	}

	public void setUnidadMedica(UnidadMedica unidadMedica) {
		this.unidadMedica = unidadMedica;
	}

	public Integer getIdPeriodo() {
		return idPeriodo;
	}

	public void setIdPeriodo(Integer idPeriodo) {
		this.idPeriodo = idPeriodo;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

}