package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity
@Table(name="usuario")
@NamedQueries({
	@NamedQuery(name="Usuario.buscaUsuario", 
	query="SELECT u FROM Usuario u WHERE u.usuario = :nombreUsuario and u.estado = 'A'"),
	@NamedQuery(name="Usuario.buscarPorCedula", query="SELECT u FROM Usuario u where u.cedula = :patron and u.estado = 'A'"),
	@NamedQuery(name="Usuario.buscarPorUsuario", query="SELECT u FROM Usuario u where u.usuario = :usuario and u.idUsuario <> :idUsuario and u.estado = 'A'"),
	@NamedQuery(name="Usuario.buscarPorPatron", query="SELECT u FROM Usuario u where (lower(u.nombre) like(:patron)"
			+ " or lower(u.apellido) like(:patron)) and u.estado = 'A'"),
	@NamedQuery(name="Usuario.buscarPorPatronTecnico", query="SELECT u FROM Usuario u where (lower(u.nombre) like(:patron) or lower(u.apellido) "
			+ "like(:patron)) and u.estado = 'A' and u.perfil.idPerfil <> :idTipoUsuario")
})	
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_usuario")
	private Integer idUsuario;

	@Lob
	private String apellido;

	@Lob
	private String cedula;

	@Lob
	private String clave;

	@Lob
	private String email;

	private String estado;

	@Lob
	private String nombre;

	@Lob
	private String telefono;

	@Lob
	private String usuario;

	//bi-directional many-to-one association to Encargado
	@OneToMany(mappedBy="usuario")
	private List<Encargado> encargados;

	//bi-directional many-to-one association to Perfil
	@ManyToOne
	@JoinColumn(name="id_perfil")
	private Perfil perfil;

	public Usuario() {
	}

	public Integer getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCedula() {
		return this.cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public List<Encargado> getEncargados() {
		return this.encargados;
	}

	public void setEncargados(List<Encargado> encargados) {
		this.encargados = encargados;
	}

	public Encargado addEncargado(Encargado encargado) {
		getEncargados().add(encargado);
		encargado.setUsuario(this);

		return encargado;
	}

	public Encargado removeEncargado(Encargado encargado) {
		getEncargados().remove(encargado);
		encargado.setUsuario(null);

		return encargado;
	}

	public Perfil getPerfil() {
		return this.perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

}