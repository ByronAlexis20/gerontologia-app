package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the clase database table.
 * 
 */
@Entity
@Table(name="clase")
@NamedQueries({
	@NamedQuery(name="Clase.buscarPorPeriodo", query="SELECT c FROM Clase c where c.estado = 'A' and c.idPeriodo = :idPeriodo and Lower(c.descripcion) like Lower(:patron)")
})
public class Clase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_clase")
	private Integer idClase;

	@Lob
	private String descripcion;

	private String estado;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	@Column(name="id_usuario")
	private int idUsuario;

	@Column(name="id_periodo")
	private Integer idPeriodo;
	
	
	@Lob
	private String observacion;

	//bi-directional many-to-one association to Asistencia
	@OneToMany(mappedBy="clase")
	private List<Asistencia> asistencias;

	public Clase() {
	}

	public Integer getIdClase() {
		return this.idClase;
	}

	public void setIdClase(Integer idClase) {
		this.idClase = idClase;
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

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
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
		asistencia.setClase(this);

		return asistencia;
	}

	public Asistencia removeAsistencia(Asistencia asistencia) {
		getAsistencias().remove(asistencia);
		asistencia.setClase(null);

		return asistencia;
	}

	public Integer getIdPeriodo() {
		return idPeriodo;
	}

	public void setIdPeriodo(Integer idPeriodo) {
		this.idPeriodo = idPeriodo;
	}

}