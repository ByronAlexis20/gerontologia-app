package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the menu database table.
 * 
 */
@Entity
@Table(name="menu")
@NamedQueries({
	@NamedQuery(name="Menu.findAll", query="SELECT m FROM Menu m where m.estado = 'A' order by m.posicion asc"),
	@NamedQuery(name="Menu.buscarPorId", query="SELECT m FROM Menu m where m.estado = 'A' and m.idMenu = :idMenu"),
	@NamedQuery(name="Menu.buscarActivos", query="SELECT m FROM Menu m where m.estado = 'A' and m.idMenuPadre <> 0 order by m.idMenu asc"),
	@NamedQuery(name="Menu.buscarHijos", query="SELECT m FROM Menu m where m.estado = 'A' and m.idMenuPadre = :idPadre order by m.posicion asc"),
	@NamedQuery(name="Menu.buscarPadre", query="SELECT m FROM Menu m where m.estado = 'A' and m.idMenu = :idMenuPadre")
})	
public class Menu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_menu")
	private int idMenu;

	@Lob
	private String descripcion;

	private String estado;
	
	private String titulo;

	@Lob
	private String icono;

	@Column(name="id_menu_padre")
	private int idMenuPadre;

	private int posicion;

	@Lob
	private String url;

	//bi-directional many-to-one association to Permiso
	@OneToMany(mappedBy="menu")
	private List<Permiso> permisos;

	public Menu() {
	}

	public int getIdMenu() {
		return this.idMenu;
	}

	public void setIdMenu(int idMenu) {
		this.idMenu = idMenu;
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

	public String getIcono() {
		return this.icono;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}

	public int getIdMenuPadre() {
		return this.idMenuPadre;
	}

	public void setIdMenuPadre(int idMenuPadre) {
		this.idMenuPadre = idMenuPadre;
	}

	public int getPosicion() {
		return this.posicion;
	}

	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Permiso> getPermisos() {
		return this.permisos;
	}

	public void setPermisos(List<Permiso> permisos) {
		this.permisos = permisos;
	}

	public Permiso addPermiso(Permiso permiso) {
		getPermisos().add(permiso);
		permiso.setMenu(this);

		return permiso;
	}

	public Permiso removePermiso(Permiso permiso) {
		getPermisos().remove(permiso);
		permiso.setMenu(null);

		return permiso;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

}