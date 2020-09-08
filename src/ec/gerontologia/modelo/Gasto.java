package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the gastos database table.
 * 
 */
@Entity
@Table(name="gastos")
@NamedQuery(name="Gasto.buscarGasto", query="SELECT g FROM Gasto g where g.estado = 'A' and g.idActividad = :idActividad")
public class Gasto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_gasto")
	private Integer idGasto;

	private String estado;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	@Column(name="id_actividad")
	private int idActividad;

	@Column(name="id_usuario")
	private int idUsuario;
	
	private String actividad;

	private float total;

	//bi-directional many-to-one association to GastosDetalle
	@OneToMany(mappedBy="gasto", cascade = CascadeType.ALL)
	private List<GastosDetalle> gastosDetalles;

	public Gasto() {
	}

	public Integer getIdGasto() {
		return this.idGasto;
	}

	public void setIdGasto(Integer idGasto) {
		this.idGasto = idGasto;
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

	public int getIdActividad() {
		return this.idActividad;
	}

	public void setIdActividad(int idActividad) {
		this.idActividad = idActividad;
	}

	public int getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public float getTotal() {
		return this.total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public List<GastosDetalle> getGastosDetalles() {
		return this.gastosDetalles;
	}

	public void setGastosDetalles(List<GastosDetalle> gastosDetalles) {
		this.gastosDetalles = gastosDetalles;
	}

	public GastosDetalle addGastosDetalle(GastosDetalle gastosDetalle) {
		getGastosDetalles().add(gastosDetalle);
		gastosDetalle.setGasto(this);

		return gastosDetalle;
	}

	public GastosDetalle removeGastosDetalle(GastosDetalle gastosDetalle) {
		getGastosDetalles().remove(gastosDetalle);
		gastosDetalle.setGasto(null);

		return gastosDetalle;
	}

	public String getActividad() {
		return actividad;
	}

	public void setActividad(String actividad) {
		this.actividad = actividad;
	}

}