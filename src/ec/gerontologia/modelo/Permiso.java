package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the permiso database table.
 * 
 */
@Entity
@Table(name="permiso")
@NamedQueries({
	@NamedQuery(name="Permiso.buscarPorId", query="SELECT p FROM Permiso p"),
	@NamedQuery(name="Permiso.buscarPadrePorPerfil", query="SELECT p FROM Permiso p where p.perfil.idPerfil = :idperfil and p.estado = 'A' and p.menu.idMenuPadre = 0"),
	@NamedQuery(name="Permiso.buscarTodosPorPerfil", query="SELECT p FROM Permiso p where p.perfil.idPerfil = :idperfil and p.estado = 'A' and p.menu.idMenuPadre <> 0"),
	@NamedQuery(name="Permiso.buscarPermisoPerfilHijos", query="SELECT p FROM Permiso p where p.perfil.idPerfil = :idperfil and p.estado = 'A' and p.menu.idMenuPadre <> 0"),
	@NamedQuery(name="Permiso.buscarPerfilMenu", query="SELECT p FROM Permiso p where p.perfil.idPerfil = :idPerfil and p.estado = 'A' and p.menu.idMenu = :idMenu"),
})
public class Permiso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_permiso")
	private Integer idPermiso;

	private String estado;

	//bi-directional many-to-one association to Menu
	@ManyToOne
	@JoinColumn(name="id_menu")
	private Menu menu;

	//bi-directional many-to-one association to Perfil
	@ManyToOne
	@JoinColumn(name="id_perfil")
	private Perfil perfil;

	public Permiso() {
	}

	public Integer getIdPermiso() {
		return this.idPermiso;
	}

	public void setIdPermiso(Integer idPermiso) {
		this.idPermiso = idPermiso;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Menu getMenu() {
		return this.menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Perfil getPerfil() {
		return this.perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

}