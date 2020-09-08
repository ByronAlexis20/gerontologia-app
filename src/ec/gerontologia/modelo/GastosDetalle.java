package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the gastos_detalle database table.
 * 
 */
@Entity
@Table(name="gastos_detalle")
@NamedQuery(name="GastosDetalle.findAll", query="SELECT g FROM GastosDetalle g")
public class GastosDetalle implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_detalle")
	private Integer idDetalle;

	private int cantidad;

	private String estado;

	private double precio;

	@Lob
	private String servicio;

	private double total;

	//bi-directional many-to-one association to Gasto
	@ManyToOne
	@JoinColumn(name="id_gasto")
	private Gasto gasto;

	public GastosDetalle() {
	}

	public Integer getIdDetalle() {
		return this.idDetalle;
	}

	public void setIdDetalle(Integer idDetalle) {
		this.idDetalle = idDetalle;
	}

	public int getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public double getPrecio() {
		return this.precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getServicio() {
		return this.servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public double getTotal() {
		return this.total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public Gasto getGasto() {
		return this.gasto;
	}

	public void setGasto(Gasto gasto) {
		this.gasto = gasto;
	}

}