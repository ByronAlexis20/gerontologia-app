package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the actividad database table.
 * 
 */
@Entity

@NamedQuery(name="Actividad.buscarPorPatron", query="SELECT a FROM Actividad a WHERE LOWER(a.descripcion) LIKE LOWER(:patron) and a.estado = 'A' and a.idPeriodo = :idPeriodo")
public class Actividad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_actividad")
	private Integer idActividad;

	@Lob
	private String descripcion;

	private String estado;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	@Lob
	private String observacion;

	@Column(name="id_periodo")
	private Integer idPeriodo;
	
	public Actividad() {
	}

	public Integer getIdActividad() {
		return this.idActividad;
	}

	public void setIdActividad(Integer idActividad) {
		this.idActividad = idActividad;
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

	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Integer getIdPeriodo() {
		return idPeriodo;
	}

	public void setIdPeriodo(Integer idPeriodo) {
		this.idPeriodo = idPeriodo;
	}

}